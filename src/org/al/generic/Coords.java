package org.al.generic;

public class Coords implements Comparable<Coords> {
    public int r;
    public int c;

    @Override
    public String toString() {
        return "Coords{" +
                "r=" + r +
                ", c=" + c +
                '}';
    }

    public Coords() {
        this.r = 0;
        this.c = 0;
    }

    public Coords(int r, int c) {
        this.r = r;
        this.c = c;
    }

    @Override
    public int compareTo(Coords anotherCoords)
    {
        if (this.r != anotherCoords.r)
            return this.r - anotherCoords.r;
        else
            return this.c - anotherCoords.c;
    }
}