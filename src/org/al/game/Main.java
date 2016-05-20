package org.al.game;

import org.al.api.Api;
import org.al.generic.Coords;
import org.al.quadrisbase.Board;
import org.al.quadrisbase.Constants;
import org.al.quadrisbase.MiniBoard;
import org.al.quadrisbase.Piece;
import org.al.quadrisexceptions.NonexistentTetrisPieceException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static boolean displayGame = true;

    private static JTextArea printedTetrisBoard;

    private static double alpha = 0.5;
    private static double gamma = 1;

    // left is Q value, second is right of visits
    private static Map<ImmutablePair<MiniBoard, Coords[]>, MutablePair<Integer, Integer>> qMap = new HashMap<>();
    private static int numGames = 0;


    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, NonexistentTetrisPieceException {
        /* App init stuff */

        init();

        int curNumRuns = 10000000;
        System.out.println(new Main().qLearn(curNumRuns));


    }

    private static void init() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // set the look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        if (displayGame) {
            printedTetrisBoard = new JTextArea("Loading...");

            final JFrame frame = new JFrame();

            printedTetrisBoard.setFont(new Font("monospaced", Font.PLAIN, 12));

            printedTetrisBoard.setColumns(Constants.BOARD_WIDTH);
            printedTetrisBoard.setRows(Constants.BOARD_HEIGHT);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new GridLayout());

            frame.getContentPane().add(printedTetrisBoard);
            frame.pack();

            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }

    private static int R(Board b, Coords[] decision) {
        Board bc = b.boardClone();
        bc.placePiece(decision);

        if (bc.isLost()) {
            return -100;
        } else if (bc.isWon()) {
            return 100;
        } else {
            return 0;
        }
    }

    private static int Q(Board b, char pieceType, Coords[] decision) {
        MutablePair<Integer, Integer> entry = qMap.get(new ImmutablePair<>(b.toMiniBoard(pieceType), decision));
        Integer result = (entry == null) ? null : entry.getLeft();
        return (result != null) ? result : 0;
    }

    private static int numQVisits(Board b, char pieceType, Coords[] decision) {
        MutablePair<Integer, Integer> entry = qMap.get(new ImmutablePair<>(b.toMiniBoard(pieceType), decision));
        Integer result = (entry == null) ? null : entry.getRight();
        return (result != null) ? result : 0;
    }

    private static void setQ(Board b, char pieceType, Coords[] decision, int value) {
        MutablePair<Integer, Integer> entry = qMap.get(new ImmutablePair<>(b.toMiniBoard(pieceType), decision));
        if (entry == null) {
            entry = new MutablePair<>();
            entry.setLeft(value);
            entry.setRight(1);
            qMap.put(new ImmutablePair<>(b.toMiniBoard(pieceType), decision), entry);
        } else {
            entry.setLeft(value);
            entry.setRight(entry.getLeft() + 1);
            qMap.put(new ImmutablePair<>(b.toMiniBoard(pieceType), decision), entry);
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

    private String qLearn(int numRuns) throws NonexistentTetrisPieceException, InterruptedException {
        int k = numRuns;
        int c = 0;
        int o = 0;

        while (numRuns-- > 0) { // while loop goes through numRuns loops
            c++;
            int percentDone = c * 100 / k;
            if (o != percentDone) {
                o = percentDone;
                System.out.println(o + "% complete." + " Map size: " + qMap.size());
            }


            displayGame = numRuns % 10000 == 0;

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

                if (Math.random() < 1 - epsilon) { // Take optimal action
                    decision = optimalAction(api.getBoard(), possiblePositions, pieceType);

                } else { // Take uniformly random action
                    decision = possiblePositions.get(ThreadLocalRandom.current().nextInt(0, possiblePositions.size()));
                }

                // alpha = 1 / (numQVisits(api.getBoard(), pieceType, decision) + 1); // Includes current visit, hence the + 1
                alpha = 0.5; // for testing
                gamma = 0.5; // for testing

                Board old_board = api.getBoard().boardClone();

                boolean placed = api.placePiece(decision);

                // UPDATE Q FUNCTION HERE

                int oldQValue = Q(old_board, pieceType, decision);
                int newQValue = (int) (oldQValue + alpha * (R(old_board, decision) + gamma * bestFutureAction(api.getBoard()) - oldQValue));
                setQ(old_board, pieceType, decision, newQValue);

                // Print the current board to the JTextArea
                if (displayGame) {
                    printedTetrisBoard.setText(api.getBoard().toString());

                    Thread.sleep(250);
                }


                if (!placed) { // Game lost
//                System.out.println("Final score: " + api.getScore() + ".");
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
