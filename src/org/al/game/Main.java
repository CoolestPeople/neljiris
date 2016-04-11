package org.al.game;

import org.al.api.Api;
import org.al.generic.Coords;
import org.al.quadrisbase.Piece;
import org.al.quadrisexceptions.NonexistentTetrisPieceException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Main {
    private static final int runs = 100000;

    private static boolean displayGame = false;

    private static JTextArea printedTetrisBoard;

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, NonexistentTetrisPieceException {
        /* App init stuff */

        // set the look and feel
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

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

        List<Integer> scores = new ArrayList<>();

        for (int i = 0; i < runs; i++) {
            scores.add(new Main().play());
        }

        OptionalDouble optionalAverage = scores
                .stream()
                .mapToDouble(a -> a)
                .average();

        double average = optionalAverage.isPresent() ? optionalAverage.getAsDouble() : 0;

        System.out.println("Average from " + runs + " runs: " + average + " rows.");
    }

    private int play() throws InterruptedException, NonexistentTetrisPieceException {
        Api api = new Api();

        api.newGame();

        while (true) {
            Piece currentPiece = api.getPiece();

            List<Coords[]> possiblePositions = api.possibleMoves(currentPiece);

            // Find the lowest possible one. (Stupid method, good control group)
            Coords[] lowestPosition = possiblePositions.get(0);

            for (int i = 1; i < possiblePositions.size(); i++) {
                Coords[] anotherPosition = possiblePositions.get(i);
//                System.out.println(Arrays.toString(anotherPosition));

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

            boolean placed = api.placePiece(lowestPosition);

            if (!placed) { // Game lost
//                System.out.println("Final score: " + api.getScore() + ".");
                return api.getScore();
            }

            // Print the current board to the JTextArea
            if (displayGame) {
                printedTetrisBoard.setText(api.getBoard().toString());

                Thread.sleep(1000);
            }
        }
    }
}
