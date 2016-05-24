package org.al.api;

import org.al.generic.Coords;
import org.al.quadrisbase.*;
import org.al.quadrisexceptions.NonexistentTetrisPieceException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Api {
    private Board board;

    private Queue<Piece> block = new LinkedList<>();


    public void newGame() {
        board = new Board();
    }

    public Piece getPiece() {
        return board.getPiece();
    }

    public boolean placePiece(Coords[] pieceCoords) {
        return board.placePiece(pieceCoords);

    }

    public List<Coords[]> possibleMoves(Piece pieceToPlace) throws NonexistentTetrisPieceException {
        return board.possibleMoves(pieceToPlace);
    }

    public int getScore() {
        return board.getScore();
    }

    public Board getBoard() {
        return board;
    }

    public int tentativeRowsRemoved(Coords[] pieceCoords) {
        return board.tentativeRowsRemoved(pieceCoords);
    }

    public double getAverageHeight() {
        return board.getAverageHeight();
    }
}
