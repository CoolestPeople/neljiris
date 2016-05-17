package org.al.quadrisbase;

import java.util.Arrays;

/**
 * Created by lhscompsci on 5/10/16.
 */
public class MiniBoard {
    private int[] topRow;
    private char pieceType;

    public MiniBoard(int[] topRow, char pieceType) {
        this.topRow = topRow;
        this.pieceType = pieceType;
    }

    public int[] getTopRow() {
        return topRow;
    }

    public char getPieceType() {
        return pieceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MiniBoard miniBoard = (MiniBoard) o;

        if (pieceType != miniBoard.pieceType) return false;
        return Arrays.equals(topRow, miniBoard.topRow);

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(topRow);
        result = 31 * result + (int) pieceType;
        return result;
    }
}
