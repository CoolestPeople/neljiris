package org.al.game;

import org.al.api.Api;
import org.al.config.Config;
import org.al.generic.Coords;
import org.al.quadrisbase.Board;
import org.al.quadrisbase.Constants;
import org.al.quadrisbase.MiniBoard;
import org.al.quadrisbase.Piece;
import org.al.quadrisexceptions.NonexistentTetrisPieceException;
import org.al.save.Saver;
import org.al.utils.Utils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.al.config.Config.QMAP_PATH;

import static org.al.config.Config.BOARD_WIDTH;
import static org.al.config.Config.BOARD_HEIGHT;


public class Main {
    private static boolean displayGame;

    private static JTextArea printedTetrisBoard;
    private static NumberFormat twoDecimalPoints;
    private static NumberFormat threeDecimalPoints;

    private static double alpha = 0.5;
    private static double gamma = 1;

    // left is Q value, second is right of visits
    private static Map<ImmutablePair<MiniBoard, Integer>, ImmutablePair<Integer, Integer>> qMap = new HashMap<>();
    private static int numGames = 0;


    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, NonexistentTetrisPieceException, IOException {
        /* App init stuff */
        init();

        /* Regurgitate Q matrix */
        regurgitate();

        int curNumRuns = Config.TOTAL_RUNS;
        System.out.println(new Main().qLearn(curNumRuns));

        System.out.println("Saving...");
        save();
        System.out.println("Saved!");
    }

    private static void save() {
        System.out.print("Saving... ");
        Saver s = new Saver(qMap, numGames);
        new Thread(s).run();
        System.out.println("Saved!");
    }
    private static void regurgitate() throws IOException, ClassNotFoundException {
        System.out.println(Utils.getBetween("dfhkjadfhldskajfhlksssvjRegurgitating...asdfdsddfsdf", "fhlksssvj", "asdfdsddf"));

        File qMatrixFile = new File(QMAP_PATH);
        if (qMatrixFile.exists() && !qMatrixFile.isDirectory()) {
            List<String> lines = Files.readAllLines(qMatrixFile.toPath(), Charset.defaultCharset());

            for (String line : lines) {

                /* SPECIAL CASES */

                if (line.length() == 0) {
                    continue;
                }
                if (line.contains("----------->")) {
                    numGames = Integer.parseInt(Utils.getBetween(line, "----------->", "<-----------"));
                    continue;
                }
                if (line.contains("            *     ,MMM8&&&.            *")) { //Cats!
                    break;
                }


                /* GENERAL CASE OF QMAP ENTRY */

                String miniBoardString = Utils.getBetween(line, "a:^^^", "^^^:a");
                MiniBoard miniBoard = new MiniBoard(miniBoardString);
                Integer b = Integer.parseInt(Utils.getBetween(line, "b:^^^", "^^^:b"));
                Integer c = Integer.parseInt(Utils.getBetween(line, "c:^^^", "^^^:c"));
                Integer d = Integer.parseInt(Utils.getBetween(line, "d:^^^", "^^^:d"));
                qMap.put(new ImmutablePair<>(miniBoard, b), new ImmutablePair<>(c, d));
            }

            System.out.println("Regurgitation complete.");
        } else {
            System.out.println("Nothing to regurgitate.");
        }
    }

    private static void init() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Set displayGame from configuration
        displayGame = Config.DISPLAY_GAME;

        // set the look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        if (displayGame) {
            printedTetrisBoard = new JTextArea("Loading...");

            final JFrame frame = new JFrame();

            printedTetrisBoard.setFont(new Font("monospaced", Font.PLAIN, 12));

            printedTetrisBoard.setColumns(BOARD_WIDTH);
            printedTetrisBoard.setRows(BOARD_HEIGHT);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new GridLayout());

            frame.getContentPane().add(printedTetrisBoard);
            frame.pack();

            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }


        // Set number formatters
        twoDecimalPoints = DecimalFormat.getInstance();
        twoDecimalPoints.setMinimumFractionDigits(2);
        twoDecimalPoints.setMaximumFractionDigits(2);
        twoDecimalPoints.setRoundingMode(RoundingMode.DOWN);

        threeDecimalPoints = DecimalFormat.getInstance();
        threeDecimalPoints.setMinimumFractionDigits(3);
        threeDecimalPoints.setMaximumFractionDigits(3);
        threeDecimalPoints.setRoundingMode(RoundingMode.DOWN);
    }

    private static int R(Board b, Coords[] decision) {
        Board bc = b.boardClone();
        bc.placePiece(decision);

        double maxHeight = bc.getMaxHeight();

        if (bc.isLost()) {
            return -100;
        } else {
            int numberOfRowsToScale = BOARD_HEIGHT - 4; // 4 rows give -100
            double scaleFactor = (double) 100 / (numberOfRowsToScale + 1);
            double distanceFromTopOfScale = (double) BOARD_HEIGHT - (double) 4 - maxHeight;
            double reward = scaleFactor * distanceFromTopOfScale - 100;
            return (int) Math.round(reward);
        }

        /*
        ANDREW'S VERSION
        double averageHeight = bc.getAverageHeight();

        if (bc.isLost()) {
            return -100;
        } else if (bc.isWon()) {
            return 100;
        } else {
            int numberOfRowsToScale = Constants.BOARD_HEIGHT - 4 - 2; // 4 rows give -100; 2 rows give +100
            double scaleFactor = (double) 200 / (numberOfRowsToScale + 1);
            double distanceFromTopOfScale = (double) Constants.BOARD_HEIGHT - (double) 4 - averageHeight;
            double reward = scaleFactor * distanceFromTopOfScale - 100;
            return (int) Math.round(reward);
        }
        */

        /*
        ORIGINAL VERSION
            if (bc.isLost()) {
                return -100;
            } else if (bc.isWon()) {
                return 100;
            } else {
                return 0;
            }
        */
    }

    private static int hash(Coords[] pieceCoords) {
        return Arrays.hashCode(pieceCoords);
    }

    private static int Q(Board b, char pieceType, Coords[] decision) {
        ImmutablePair<Integer, Integer> entry = qMap.get(new ImmutablePair<>(b.toMiniBoard(pieceType), hash(decision)));
//        if (entry != null)
//            System.out.println("IT WORKED!!!!");
        Integer result = (entry == null) ? null : entry.getLeft();
        return (result != null) ? result : 0;
    }

    private static int numQVisits(Board b, char pieceType, Coords[] decision) {
        ImmutablePair<Integer, Integer> entry = qMap.get(new ImmutablePair<>(b.toMiniBoard(pieceType), hash(decision)));
        Integer result = (entry == null) ? null : entry.getRight();
        return (result != null) ? result : 0;
    }

    private static void setQ(Board b, char pieceType, Coords[] decision, int value) {
        ImmutablePair<Integer, Integer> entry = qMap.get(new ImmutablePair<>(b.toMiniBoard(pieceType), hash(decision)));
        if (entry == null) {
            entry = new ImmutablePair<>(value, 1);
            qMap.put(new ImmutablePair<>(b.toMiniBoard(pieceType), hash(decision)), entry);
        } else {
            entry = new ImmutablePair<>(value, entry.getRight() + 1);
            qMap.put(new ImmutablePair<>(b.toMiniBoard(pieceType), hash(decision)), entry);
        }
    }

    public static Coords[] optimalAction(Board cur_board, List<Coords[]> possiblePositions, char pieceType) {
        Coords[] decision = possiblePositions.get(0);

        int curMaxQ = Q(cur_board, pieceType, decision);
        List<Coords[]> optimalDecisions = new ArrayList<>();
        for (Coords[] option : possiblePositions) {

            if (Q(cur_board, pieceType, option) > curMaxQ) {
                curMaxQ = Q(cur_board, pieceType, option);
                optimalDecisions.clear();
                optimalDecisions.add(option);
            } else if (Q(cur_board, pieceType, option) == curMaxQ) {
                optimalDecisions.add(option);
            }
        }

        return optimalDecisions.get(ThreadLocalRandom.current().nextInt(0, optimalDecisions.size()));
    }

    private String qLearn(int numRuns) throws NonexistentTetrisPieceException, InterruptedException, IOException {
        int k = numRuns;
        int c = 0;
        int o = 0;

        int numRows = 0;
        int numCurRuns = 0;
        int numAlreadyVisited = 0;
        int numTotalVisits = 0;

        int max = 0;

        while (numRuns --> 0) { // while loop goes through numRuns loops
            c++;
            numCurRuns++;
            int percentDone = c * 100 / k;
            if (o != percentDone) {
                o = percentDone;

                System.out.println(o + "% complete." + " Map size: " + qMap.size() + ". Average score: " + threeDecimalPoints.format((double) (numRows) / (numCurRuns))
                        + ". " + twoDecimalPoints.format((1 - (double) (numAlreadyVisited) / (numTotalVisits)) * 100) + "% first time visits. (Current record = " + max + ")");
                numRows = numCurRuns = 0;

                displayGame = Config.DISPLAY_GAME;
            }

            if (c % Config.SAVE_CHUNK == 0) {
                save();
            }

            Api api = new Api();
            api.newGame();
            numGames++;
            double epsilon = 1.0 / numGames;

            while (true) {
                Piece currentPiece = api.getPiece();
                char pieceType = currentPiece.getType();

                List<Coords[]> possiblePositions = api.possibleMoves(currentPiece);

                Coords[] decision = possiblePositions.get(0);

                // SET decision HERE

//                // Start test
//
//                setQ(api.getBoard(), pieceType, decision, 3);
//                System.out.println("This should be 3: " + Q(api.getBoard().boardClone(), pieceType, decision.clone()));
//
//                // End test

                if (Math.random() < 1 - epsilon) { // Take optimal action
                    decision = optimalAction(api.getBoard(), possiblePositions, pieceType);

                } else { // Take uniformly random action
                    decision = possiblePositions.get(ThreadLocalRandom.current().nextInt(0, possiblePositions.size()));
                }

                alpha = 1.0 / (numQVisits(api.getBoard(), pieceType, decision) + 1); // Includes current visit, hence the + 1
                if (alpha < 0.9) {
                    numAlreadyVisited++;
                }
                numTotalVisits++;
//                alpha = 0.5; // for testing
//                gamma = 0.5; // for testing

                Board old_board = api.getBoard().boardClone();

                boolean placed = api.placePiece(decision);

                // UPDATE Q FUNCTION HERE

                int oldQValue = Q(old_board, pieceType, decision);
                int newQValue = (int) (oldQValue + alpha * (R(old_board, decision) + gamma * bestFutureAction(api.getBoard()) - oldQValue));
                setQ(old_board, pieceType, decision, newQValue);

//                System.out.println(oldQValue);

                // Print the current board to the JTextArea
                if (displayGame) {
                    printedTetrisBoard.setText(api.getBoard().toString());

                    Thread.sleep(250);
                }


                if (!placed) { // Game lost
//                System.out.println("Final score: " + api.getScore() + ".");
                    if (api.getScore() > max) {
                        max = api.getScore();
                    }
                    numRows += api.getScore();
                    break;
                }
            }

            displayGame = false;
        }

        return "We're smart";


    }

    // Average of best future actions for each possible future piece type
    private int bestFutureAction(Board cur_board) throws NonexistentTetrisPieceException {
        int averageBest = 0;
        char[] possible_futures = {Constants.PIECE_I, Constants.PIECE_J, Constants.PIECE_L, Constants.PIECE_O, Constants.PIECE_S, Constants.PIECE_T, Constants.PIECE_Z};
        for (char pieceType : possible_futures) {
            Piece cur_piece = new Piece(pieceType, 0);
            List<Coords[]> possiblePositions = cur_board.possibleMoves(cur_piece);

            int maxQValue = -100;

            for (Coords[] cur_decision : possiblePositions) {
                Board tester = cur_board.boardClone();
                tester.placePiece(cur_decision);

                int qLookupResult = Q(tester, pieceType, cur_decision);

                maxQValue = Math.max(maxQValue, qLookupResult);
            }

            averageBest += maxQValue;
        }
        return averageBest / possible_futures.length;
    }
}
