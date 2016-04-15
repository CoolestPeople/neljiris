package org.al.game;

import org.al.api.Api;
import org.al.generic.Coords;
import org.al.quadrisbase.Piece;
import org.al.quadrisexceptions.NonexistentTetrisPieceException;
import org.al.training.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int runs = 1;

    private static boolean displayGame = true;

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

//        List<Move> moves = new Main().teach();

//        if (moves != null) {
//            moves.forEach(System.out::println);
//        }
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
//                System.out.println("Final score: " + api.getScore() + ".");
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

    private List<Move> teach() throws NonexistentTetrisPieceException, InterruptedException {
        List<Move> moves = new ArrayList<>();

        if (!displayGame) { // Can't teach if the board isn't allowed to show
            System.out.println("Board can't be displayed. Set displayGame to true.");
            return null;
        }


        Api api = new Api();

        api.newGame();

        while (true) {
            char[][] currentBoard = api.getBoard().getBoard();

            Piece currentPiece = api.getPiece();

            Coords[] lowestPosition = getMoveByGetLowest(api, currentPiece);

            boolean placed = api.placePiece(lowestPosition);

            if (!placed) { // Game lost
                break;
            }

            // Print the current board to the JTextArea
            if (displayGame) {
                printedTetrisBoard.setText(api.getBoard().toString());

                Thread.sleep(1000);
            }


            System.out.println("What was the move quality? {1, 2, 3}");
            int moveQuality = in.nextInt();

            moves.add(new Move(currentBoard, lowestPosition, moveQuality));
        }

        return moves;
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

        return lowestPosition;
    }
}
