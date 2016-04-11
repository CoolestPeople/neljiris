package org.al.quadrisexceptions;

public class NonexistentTetrisPieceException extends Throwable {
    public NonexistentTetrisPieceException() {
    }

    public NonexistentTetrisPieceException(String message) {
        super(message);
    }

    public NonexistentTetrisPieceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonexistentTetrisPieceException(Throwable cause) {
        super(cause);
    }

    public NonexistentTetrisPieceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
