package org.al.quadrisbase;

import org.al.quadrisexceptions.NonexistentTetrisPieceException;

public class Piece {
    private char type;
    private int rotation;

    public Piece() {
    }

    public Piece(char type, int rotation) throws NonexistentTetrisPieceException {
        verifyType(type);

        this.type = type;
        this.rotation = rotation;
    }

    public Piece(char type) throws NonexistentTetrisPieceException {
        verifyType(type);

        this.type = type;
        this.rotation = 0;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    private void verifyType(char type) throws NonexistentTetrisPieceException {
        if (!(type == 'I' || type == 'J' || type == 'L' || type == 'O' || type == 'S' || type == 'T' || type == 'Z')) {
            throw new NonexistentTetrisPieceException();
        }
    }
}






















// Obsolete stuff


/////////////////////////
//    OBSOLETE!!!      //
/////////////////////////

/* THIS IS NO LONGER USED. USE C-h a RET tetris -BASED ROTATIONS INSTEAD. */              //  (OBSOLETE!!!)
//            (OBSOLETE!!!)
//    Original pieces are rotated as represented by the letters.                (OBSOLETE!!!)
//               (OBSOLETE!!!)
//    Rotation example:               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//    If the original piece is (rotation 0):               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//    ▉▉▉               (OBSOLETE!!!)
//     ▉               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//    Rotation 1:               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//     ▉               (OBSOLETE!!!)
//    ▉▉               (OBSOLETE!!!)
//     ▉               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//    Rotation 2:               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//     ▉               (OBSOLETE!!!)
//    ▉▉▉               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//    Rotation 3:               (OBSOLETE!!!)
//               (OBSOLETE!!!)
//    ▉               (OBSOLETE!!!)
//    ▉▉               (OBSOLETE!!!)
//    ▉               (OBSOLETE!!!)
