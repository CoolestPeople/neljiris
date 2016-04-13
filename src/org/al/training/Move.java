package org.al.training;

import org.al.generic.Coords;

import java.util.Arrays;

public class Move {
    private char[][] board; // The board before the move
    private Coords[] piece; // Where the piece is to be placed
    private int quality;

    public Move(char[][] board, Coords[] piece, int quality) {

        this.board = board;
        this.piece = piece;
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "Move{" +
                "board=" + Arrays.deepToString(board) +
                ", piece=" + Arrays.toString(piece) +
                ", quality=" + quality +
                '}';
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public Coords[] getPiece() {
        return piece;
    }

    public void setPiece(Coords[] piece) {
        this.piece = piece;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
