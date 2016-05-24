package org.al.generic;

public class Coords implements Comparable<Coords> {
    public int r;
    public int c;

    public Coords() {
        this.r = 0;
        this.c = 0;
    }

    public Coords(int r, int c) {
        this.r = r;
        this.c = c;
    }

    @Override
    public String toString() {
        return "Coords{" +
                "r=" + r +
                ", c=" + c +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coords coords = (Coords) o;

        if (r != coords.r) return false;
        return c == coords.c;

    }

    @Override
    public int hashCode() {
        int result = r;
        result = 31 * result + c;
        return result;
    }

    @Override
    public int compareTo(Coords anotherCoords) {
        if (this.r != anotherCoords.r)
            return this.r - anotherCoords.r;
        else
            return this.c - anotherCoords.c;
    }
}