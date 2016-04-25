package org.al.training;

import java.util.Arrays;

public class Situation {
    private char[][] board;
    private int quality;

    public Situation(char[][] board, int quality) {

        this.board = board;
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "Situation{" +
                "board=" + Arrays.deepToString(board) +
                ", quality=" + quality +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Situation situation = (Situation) o;

        if (quality != situation.quality) return false;
        return Arrays.deepEquals(board, situation.board);

    }

    @Override
    public int hashCode() {
        int result = board != null ? Arrays.deepHashCode(board) : 0;
        result = 31 * result + quality;
        return result;
    }

    public char[][] getBoard() {

        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
