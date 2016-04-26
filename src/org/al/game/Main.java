package org.al.game;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import org.al.api.Api;
import org.al.quadrisbase.Constants;
import org.al.generic.Coords;
import org.al.quadrisbase.Board;
import org.al.quadrisbase.Piece;
import org.al.quadrisexceptions.NonexistentTetrisPieceException;
import org.al.statisticsutils.Deviation;
import org.al.training.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import org.al.training.Situation;
import org.al.training.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class Main {
    private static final int runs = 100;

    private static boolean displayGame = false;

    private static boolean teach = true;
    private static boolean play = !teach;

    private static JTextArea printedTetrisBoard;
    private static Scanner in;

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, NonexistentTetrisPieceException {
        /* App init stuff */

        in = new Scanner(System.in);

        // set the look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        if (displayGame) {
            printedTetrisBoard = new JTextArea("Loading...");

            final JFrame frame = new JFrame();

            printedTetrisBoard.setFont(new Font("monospaced", Font.PLAIN, 12));

            printedTetrisBoard.setColumns(10);
            printedTetrisBoard.setRows(20);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new GridLayout());

            frame.getContentPane().add(printedTetrisBoard);
            frame.pack();

            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }


        for (int i = 0; i < runs; i++) {
            scores.add(new Main().playBetter());
            if (i % 1000 == 0) {
                System.out.println((double) i / (double) runs * (double) 100 + "% complete.");
        if (play) {
            List<Integer> scores = new ArrayList<>();

            for (int i = 0; i < runs; i++) {
                scores.add(new Main().play());
                if (i % 1000 == 0) {
                    System.out.println((double) i / (double) runs * (double) 100 + "% complete.");
                }
            }

            double average = scores
                    .stream()
                    .mapToDouble(a -> a)
                    .average().isPresent() ? scores
                    .stream()
                    .mapToDouble(a -> a)
                    .average().getAsDouble() : 0;

            System.out.println("Average from " + runs + " runs: " + average + " rows.");
        } else {
            List<Situation> situations = new Main().teach();

            assert situations != null;



            // SAVE THE SITUATIONS TO PNGS     TODO: Make this into a function


            // Make the directory for saving the images
            new File(org.al.etc.Constants.USER_DIR + "/situations").mkdirs();


            int c = 0;

        double maxscore = Collections.max(scores);
        double minscore = Collections.min(scores);

        Deviation dev = new Deviation();
        double deviation = dev.findDeviation(scores.stream().mapToDouble(d -> d).toArray()); //identity function, Java unboxes automatically to get the double value);

        System.out.println("Average from " + runs + " runs: " + average + " rows.");
        System.out.println("Maximum from " + runs + " runs: " + maxscore + " rows.");
        System.out.println("Minimum from " + runs + " runs: " + minscore + " rows.");
        System.out.println("Standard deviation from " + runs + " runs: " + deviation + " rows.");
            // For each situation
            for (Situation situation : situations) {
                // Save the situation as a png

                ImageInfo info = new ImageInfo(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT, 8, true);
                PngWriter pngw = new PngWriter(new File(org.al.etc.Constants.USER_DIR + "/situations/situation" + c + "_q_" + situation.getQuality() + ".png"), info, true);
                for (int i = 0; i < Constants.BOARD_HEIGHT; i++) {
                    int[] rgbs = new int[Constants.BOARD_WIDTH];

                    for (int j = 0; j < Constants.BOARD_WIDTH; j++) {
                        rgbs[j] = situation.getBoard()[i][j] == 'X' ? new Color(0, 0, 0).getRGB() : new Color(255, 255, 255).getRGB();
                    }

                    ImageLineInt line = new ImageLineInt(info);
                    for (int k = 0; k < rgbs.length; k++) {
                        ImageLineHelper.setPixelRGBA8(line, k, rgbs[k]);
                    }

                    pngw.writeRow(line, i);
                }
                pngw.end();

                c++;
            }
        }
    }

    private int play() throws InterruptedException, NonexistentTetrisPieceException {
        Api api = new Api();

        api.newGame();

        while (true) {
            Piece currentPiece = api.getPiece();

            Coords[] lowestPosition = getMoveByGetLowest(api, currentPiece);

            if (lowestPosition.length != 4) {
                System.err.println("Something has gone horribly wrong nothing is as it seems fair is foul and foul is fair with semicolons flowing out of your eyes like liquid pain he comes he comes ");
            }

            boolean placed = api.placePiece(lowestPosition);

            if (!placed) { // Game lost
                printedTetrisBoard.setText(api.getBoard().toString());
                return api.getScore();
            }

            // Print the current board to the JTextArea
            if (displayGame) {
                printedTetrisBoard.setText(api.getBoard().toString());

                Thread.sleep(500);
            }
        }
    }

    private List<Situation> teach() throws NonexistentTetrisPieceException, InterruptedException {
        List<Situation> situations = new LinkedList<>();

        if (!displayGame) { // Can't teach if the board isn't allowed to show
            System.out.println("Board can't be displayed. Set displayGame to true.");
            return null;
        }


        Api api = new Api();

        api.newGame();

        while (true) {
            Piece currentPiece = api.getPiece();

            Coords[] lowestPosition = getMoveByGetLowest(api, currentPiece);

            boolean placed = api.placePiece(lowestPosition);

            if (!placed) { // Game lost
                break;
            }

            // Print the current board to the JTextArea
            if (displayGame && Utils.shouldDisplayBoard()) {
                printedTetrisBoard.setText(api.getBoard().toString());

                System.out.println("What is the quality of the current situation? {1, 2, 3, 4, 5}");
                int moveQuality = in.nextInt();

                // Ohh boy Java and I have an intense love-hate relationship
                situations.add(new Situation(api.getBoard().boardClone().getBoard().clone(), moveQuality));
            }
        }

        System.out.println("The situations at the end of the teach method are as follows:");

        for (int i = 0; i < situations.size() - 1; i++) {
            if (Arrays.deepEquals(situations.get(i).getBoard(), situations.get(i + 1).getBoard())) {
                System.err.println(":(");
            }
        }

        return situations;
    }

    private Coords[] lowest(List<PositionRows> positions) {
        Coords[] position = positions.get(0).getPosition();
        int lowest = 0;

        for (PositionRows p : positions) {
            if (getLowest(p.getPosition()) > lowest) {
                lowest = getLowest(p.getPosition());
                position = p.getPosition();
            }
        }

        return position;
    }

    private int getLowest(Coords[] position) {
        int lowest = 0;

        for (Coords c : position) {
            if (c.r > lowest) {
                lowest = c.r;
            }
        }

        return lowest;
    }

    private Coords[] getMoveByGetLowest(Api api, Piece currentPiece) throws NonexistentTetrisPieceException {
        List<Coords[]> possiblePositions = api.possibleMoves(currentPiece);

        // Find the lowest possible one. (Stupid method, good control group)
        Coords[] lowestPosition = possiblePositions.get(0);

        for (int i = 1; i < possiblePositions.size(); i++) {
            Coords[] anotherPosition = possiblePositions.get(i);

            int lowestOfLowestPosition = lowestPosition[0].r;
            int lowestOfAnotherPosition = anotherPosition[0].r;

            for (int it = 1; it < 4; it++) {
                if (lowestPosition[it].r > lowestOfLowestPosition) { //If it's even lower
                    lowestOfLowestPosition = lowestPosition[it].r;
                }
            }

            for (int it = 1; it < 4; it++) {
                if (anotherPosition[it].r > lowestOfAnotherPosition) { //If it's even lower
                    lowestOfAnotherPosition = anotherPosition[it].r;
                }
            }


            if (lowestOfAnotherPosition > lowestOfLowestPosition) {
                lowestPosition = anotherPosition;
            }
        }

        return lowestPosition;
    }

    // Makes sure not to make holes; fits pieces together perfectly, and withing this constraint, goes as low as possible.
    private int playBetter() throws InterruptedException, NonexistentTetrisPieceException {
        Api api = new Api();
        api.newGame();

        while (true) {
            Piece currentPiece = api.getPiece();

            List<Coords[]> possiblePositions = api.possibleMoves(currentPiece);
            Board cur_board = api.getBoard();
            char[][] board = cur_board.getBoard();

            Coords[] decision = possiblePositions.get(0);
            boolean decision_no_holes = false;
            int decision_bottom = decision[0].r;

            for (Coords[] current : possiblePositions) {
                int bottom = current[0].r;
                boolean no_holes = true;
                for (Coords co : current) {
                    bottom = Math.max(bottom, co.r);
                    boolean in_bottom_layer = true;
                    for (Coords other : current)
                        if (other.r > co.r && other.c == co.c)
                            in_bottom_layer = false;
                    if (in_bottom_layer) {
                        if (co.r < board.length - 1)
                            if (board[co.r + 1][co.c] == '.') // there is a hole
                                no_holes = false;
                    }
                }
                if (decision_no_holes == false) {
                    if (no_holes == false) {
                        if (bottom > decision_bottom) {
                            decision_bottom = bottom;
                            decision = current;
                        }
                    }
                    else {
                        decision_no_holes = true;
                        decision_bottom = bottom;
                        decision = current;
                    }
                }
                else {
                    if (no_holes) {
                        if (bottom > decision_bottom) {
                            decision_bottom = bottom;
                            decision = current;
                        }
                    }
                }
            }

            boolean placed = api.placePiece(decision);

            if (!placed) { // Game lost
//                System.out.println("Final score: " + api.getScore() + ".");
                return api.getScore();
            }

            // Print the current board to the JTextArea
            if (displayGame) {
                printedTetrisBoard.setText(api.getBoard().toString());

                Thread.sleep(500);
            }
        }
    }
}
