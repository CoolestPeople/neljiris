package org.al.game;

import org.al.generic.Coords;

import java.util.Arrays;

public class PositionRows implements Comparable<PositionRows> {
    private Coords[] position;
    private int rows;

    public PositionRows(Coords[] position, int rows) {
        this.position = position;
        this.rows = rows;
    }

    public Coords[] getPosition() {
        return position;
    }

    public void setPosition(Coords[] position) {
        this.position = position;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PositionRows{" +
                "position=" + Arrays.toString(position) +
                ", rows=" + rows +
                '}';
    }

    @Override
    public int compareTo(PositionRows o) {
        return o.getRows() - this.getRows();
    }
}
