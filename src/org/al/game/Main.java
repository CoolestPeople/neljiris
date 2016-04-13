package org.al.game;

import org.al.api.Api;
import org.al.generic.Coords;
import org.al.quadrisbase.Piece;
import org.al.quadrisexceptions.NonexistentTetrisPieceException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    }

    private int play() throws InterruptedException, NonexistentTetrisPieceException {
        Api api = new Api();

        api.newGame();

        while (true) {
            Piece currentPiece = api.getPiece();

            List<Coords[]> possiblePositions = api.possibleMoves(currentPiece);

            int position = (int) (Math.random() * ((possiblePositions.size() - 1) + 1));

            Coords[] chosenPosition = possiblePositions.get(position);

            boolean placed = api.placePiece(chosenPosition);

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
}
