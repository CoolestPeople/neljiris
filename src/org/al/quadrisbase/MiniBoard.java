package org.al.quadrisbase;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

public class MiniBoard implements Serializable {
    private int[] topRow;
    private char pieceType;

    public MiniBoard(int[] topRow, char pieceType) {
        this.topRow = topRow;
        this.pieceType = pieceType;
    }

    public MiniBoard(String saveString) {
        // Looks like [10 5 5 6] {L}

        this.topRow = Stream.of(saveString.split("]")[0].split("\\[")[1].split(" ")).mapToInt(Integer::parseInt).toArray();
        this.pieceType = saveString.split("}")[0].split("\\{")[1].charAt(0);
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

    public String toSaveString() {
        String s = "[";
        for (int k : topRow) {
            s += k + " ";
        }
        s = s.substring(0, s.length() - 1);
        s += "] {";
        s += pieceType;
        s += "}";

        return s;
    }
}
