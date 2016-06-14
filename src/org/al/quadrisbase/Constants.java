package org.al.quadrisbase;

public class Constants {
    public static final char PIECE_I = 'I';
    public static final char PIECE_J = 'J';
    public static final char PIECE_L = 'L';
    public static final char PIECE_O = 'O';
    public static final char PIECE_S = 'S';
    public static final char PIECE_T = 'T';
    public static final char PIECE_Z = 'Z';

    public static final char[] PIECES = new char[]{
            PIECE_I,
            PIECE_J,
            PIECE_L,
            PIECE_O,
            PIECE_S,
            PIECE_T,
            PIECE_Z
    };

    /**
     * @deprecated - use Config.BOARD_WIDTH in the org.al.config package instead.
     */
    public static final int BOARD_WIDTH = 4;

    /**
     * @deprecated - use Config.BOARD_HEIGHT in the org.al.config package instead.
     */
    public static final int BOARD_HEIGHT = 4;


    @Deprecated
    protected static final int WON_THRESHOLD = 2;
}
