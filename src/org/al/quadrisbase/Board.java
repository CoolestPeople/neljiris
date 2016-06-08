package org.al.quadrisbase;

import org.al.generic.Coords;
import org.al.quadrisexceptions.NonexistentTetrisPieceException;

import java.util.*;
import java.util.stream.Collectors;

import static org.al.config.Config.BOARD_HEIGHT;
import static org.al.config.Config.BOARD_WIDTH;

public class Board { //TODO: Move ALL_SPACES to the Constants class
    private static Coords[][][] ALLSPACES =
            {
                    {{new Coords(0, 0), new Coords(1, 0), new Coords(0, 1), new Coords(1, 1)}},

                    {{new Coords(0, 0), new Coords(1, 0), new Coords(2, 0), new Coords(2, 1)},
                            {new Coords(1, -1), new Coords(1, 0), new Coords(1, 1), new Coords(0, 1)},
                            {new Coords(0, -1), new Coords(0, 0), new Coords(1, 0), new Coords(2, 0)},
                            {new Coords(1, -1), new Coords(2, -1), new Coords(1, 0), new Coords(1, 1)}},

                    {{new Coords(0, 0), new Coords(1, 0), new Coords(2, 0), new Coords(0, 1)},
                            {new Coords(0, -1), new Coords(1, -1), new Coords(1, 0), new Coords(1, 1)},
                            {new Coords(2, -1), new Coords(0, 0), new Coords(1, 0), new Coords(2, 0)},
                            {new Coords(1, -1), new Coords(1, 0), new Coords(1, 1), new Coords(2, 1)}},

                    {{new Coords(0, 0), new Coords(1, 0), new Coords(1, 1), new Coords(2, 1)},
                            {new Coords(1, 0), new Coords(0, 1), new Coords(1, 1), new Coords(0, 2)}},

                    {{new Coords(1, 0), new Coords(2, 0), new Coords(0, 1), new Coords(1, 1)},
                            {new Coords(0, 0), new Coords(0, 1), new Coords(1, 1), new Coords(1, 2)}},

                    {{new Coords(1, 0), new Coords(0, 1), new Coords(1, 1), new Coords(2, 1)},
                            {new Coords(1, 0), new Coords(1, 1), new Coords(2, 1), new Coords(1, 2)},
                            {new Coords(0, 1), new Coords(1, 1), new Coords(2, 1), new Coords(1, 2)},
                            {new Coords(1, 0), new Coords(0, 1), new Coords(1, 1), new Coords(1, 2)}},

                    {{new Coords(0, 0), new Coords(1, 0), new Coords(2, 0), new Coords(3, 0)},
                            {new Coords(1, -1), new Coords(1, 0), new Coords(1, 1), new Coords(1, 2)}}
            };
    // board[r][c] gives state of board at row r and column c
    // '.' is empty, 'X' is full
    private char[][] board;
    // Next block of pieces to be played
    private Queue<Piece> block;

    // Initializes board and other variables, setting each position empty.
    // COMPLETE
    // Number of rows that have been completed and deleted
    private int num_completed_rows;

    public Board() {
        board = new char[BOARD_HEIGHT][BOARD_WIDTH];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = '.';
            }
        }
        block = new LinkedList<>();
        num_completed_rows = 0;
    }

    public static Coords[] getSpaces(Piece piece) throws NonexistentTetrisPieceException {
        switch (piece.getType()) {
            case 'I':
                return ALLSPACES[6][piece.getRotation() % 2];
            case 'J':
                return ALLSPACES[2][piece.getRotation() % 4];
            case 'L':
                return ALLSPACES[1][piece.getRotation() % 4];
            case 'O':
                return ALLSPACES[0][piece.getRotation() % 1];
            case 'S':
                return ALLSPACES[3][piece.getRotation() % 2];
            case 'T':
                return ALLSPACES[5][piece.getRotation() % 4];
            case 'Z':
                return ALLSPACES[4][piece.getRotation() % 2];
        }

        throw new NonexistentTetrisPieceException();
    }

    // Complete
    private void setCompletedRows(int old_completed_rows) {
        this.num_completed_rows = old_completed_rows;
    }

    public Board boardClone() {
        Board new_board = new Board();
        new_board.setBoard(this.board);
        new_board.setBlock(this.block);
        new_board.setCompletedRows(this.num_completed_rows);

        return new_board;
    }

    // COMPLETE
    public char[][] getBoard() {
        return this.board;
    }

    private void setBoard(char[][] old_board) {
        for (int r = 0; r < this.board.length; r++)
            System.arraycopy(old_board[r], 0, this.board[r], 0, this.board[0].length);
    }

    // Gives all possible possible moves based on piece.getType().
    // Ignores pieces.getRotation(), and simply tries all locations
    // Each array of Coords in the returned list has length 4; 1 coordinate
    // for each square in the tetronimo.
    public List<Coords[]> possibleMoves(Piece piece) throws NonexistentTetrisPieceException {
        List<Coords[]> possible_moves = new ArrayList<>();
        if (isLost())
            return possible_moves;

        for (int rot = 0; rot < 4; rot++) {
            Piece cur_piece = new Piece(piece.getType(), rot);
            Coords[] spaces = getSpaces(cur_piece).clone();
            for (int col = -4; col < board[0].length + 4; col++) {
                Coords[] cur_spaces = new Coords[4];
                Coords[] prev_spaces = new Coords[4];
                for (int i = 0; i < 4; i++) {
                    cur_spaces[i] = new Coords();
                    prev_spaces[i] = new Coords();
                }
                for (int i = 0; i < spaces.length; i++) { // Set column of current piece
                    cur_spaces[i].r = spaces[i].r - 4;
                    cur_spaces[i].c = spaces[i].c + col;
                    prev_spaces[i].r = -5; // prev_spaces has not been set yet
                    prev_spaces[i].c = -5; // prev_spaces has not been set yet
                }
                for (int row = -4; row < board.length + 4; row++, moveDown(cur_spaces)) {
                    boolean spot_free = false;
                    if (isOnBoard(cur_spaces))
                        spot_free = spotIsFree(cur_spaces);
                    if (spot_free) {
                        for (int i = 0; i < cur_spaces.length; i++) {
                            prev_spaces[i].r = cur_spaces[i].r;
                            prev_spaces[i].c = cur_spaces[i].c;
                        }
                    }
                }
                if (prev_spaces[0].r != -5 && prev_spaces[0].c != -5) // prev_spaces initialized
                    possible_moves.add(prev_spaces);
            }
        }
        return possible_moves;
    }

    // COMPLETE
    public boolean spotIsFree(Coords[] cur_spaces) {
        for (Coords co : cur_spaces)
            if (board[co.r][co.c] != '.')
                return false;
        return true;
    }

    // COMPLETE
    public void moveDown(Coords[] cur_spaces) {
        for (Coords cur_space : cur_spaces) cur_space.r++;
    }

    // COMPLETE
    public boolean isOnBoard(Coords[] spaces) {
        for (Coords co : spaces)
            if (co.r < 0 || co.r >= board.length || co.c < 0 || co.c >= board[0].length)
                return false;
        return true;
    }

    private void setBlock(Queue<Piece> old_block) {
        this.block = new LinkedList<>(old_block);
    }

    // COMPLETE
    @Override
    public String toString() {
        String bs = "";
        for (char[] aBoard : board) {
            for (char anABoard : aBoard) {
                bs += anABoard;
            }
            bs += "\n";
        }

        return bs;
    }

    // Returns true if game is lost (board too full), false otherwise
    // Defined to be if any of the top 4 rows are not empty (can change this)
    // COMPLETE
    public boolean isLost() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == 'X') {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Defined to be won if all filled in squares are in the bottom WON_THRESHOLD rows (can change this)
     *
     * @return true if game is won (board almost empty), false otherwise
     */
    // COMPLETE
    @Deprecated
    public boolean isWon() {
        for (int r = 0; r < board.length - Constants.WON_THRESHOLD; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == 'X') {
                    return false;
                }
            }
        }

        return true;
    }



    
    /* OLD BAD (UNCOMPLETED) FUNCTION
    // col is leftmost column of piece
    // Returns true if piece placed successfully, false if not.
    // (For example, if the col makes the piece be off the board, it returns false)
    public boolean placePiece(Piece piece, int col)
    {
	// spaces is as small an array as possible containing '.'s and 'X's such that the
	// 'X's form the given piece
	char[][] spaces = getSpaces(piece.getType(), piece.getRotation());

	// Move spaces down until it can't be lowered further; final location contains
	// coordinates of the 4 points in final location of the tetronimo
	int[][] final_location = locateSpaces(spaces, col);

	// Make sure that each point is a valid spot on the board
	for (int[] point : final_location) {
	    if (point[0] < 0 || point[0] >= board.length || point[1] < 0 || point[1] >= board[0].length) {
		return false;
	    }
	}

	// Add piece onto board
	for (int[] point : final_location) {
	    board[point[0]][point[1]] = 'X';
	}

	// Delete any complete rows on board
	deleteCompleteRows();
    }
    */

    // COMPLETE
    // Doesn't make sure pieceCoords represents a valid move.
    // Returns true if game is lost, false otherwise.
    public boolean placePiece(Coords[] pieceCoords) {
//        System.out.println(Arrays.toString(pieceCoords));

        for (Coords c : pieceCoords) {
            board[c.r][c.c] = 'X';
        }

        // Delete full rows
        deleteFullRows();

        return !isLost();
    }

    /**
     * Returns how many rows will be filled if a piece is placed in a certain place
     *
     * @param pieceCoords - the coordinates of the piece
     * @return - the number of rows that will be filled
     */
    public int tentativeRowsRemoved(Coords[] pieceCoords) {
        char[][] tempBoard = this.boardClone().getBoard();

        for (Coords c : pieceCoords) {
            tempBoard[c.r][c.c] = 'X';
        }

        return getCompleteRows().length;
    }



    private void deleteFullRows() {
        for (int row : getCompleteRows()) {
            num_completed_rows++;
            deleteRowAndShift(row);
        }
    }
    private void deleteRowAndShift(int row) {
        for (int i = 0; i < board[row].length; i++) {
            board[row][i] = '.';
        }

        for (int k = row - 1; k > 0; k--) {
            for (int i = 0; i < board[k + 1].length; i++) {
                board[k + 1][i] = '.';
            }

            board[k + 1] = board[k].clone();
        }
    }

    private int[] getCompleteRows() {
        List<Integer> fullRows = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            char[] row = board[i];
            if (entireRowIs(row, 'X')) {
                fullRows.add(i);
            }
        }

        return fullRows.stream().mapToInt(i -> i).toArray();
    }

    private boolean entireRowIs(char[] row, char what) {
        for (char c : row) {
            if (c != what) {
                return false;
            }
        }

        return true;
    }

    // COMPLETE
    public Piece getPiece() {
        if (block.size() < 2) {
            generateSevenPieces();
        }

        return block.remove()/*poll*/;
    }

    // COMPLETE
    public Piece nextPiece() {
        return block.element();
    }

    // COMPLETE
    private void generateSevenPieces() {
        List<Piece> new_pieces = new ArrayList<>();
        try {
            new_pieces.add(new Piece('I'));
            new_pieces.add(new Piece('J'));
            new_pieces.add(new Piece('L'));
            new_pieces.add(new Piece('O'));
            new_pieces.add(new Piece('S'));
            new_pieces.add(new Piece('T'));
            new_pieces.add(new Piece('Z'));
        } catch (NonexistentTetrisPieceException e) {
            e.printStackTrace();
        }
        Collections.shuffle(new_pieces);
        block.addAll(new_pieces.stream().collect(Collectors.toList()));
    }

    public int getNum_completed_rows() {
        return num_completed_rows;
    }

    public void setNum_completed_rows(int num_completed_rows) {
        this.num_completed_rows = num_completed_rows;
    }

    public int getScore() {
        return num_completed_rows;
    }

    public MiniBoard toMiniBoard(char pieceType) {
        int[] topRow = new int[board[0].length];

        for (int i = 0; i < board[0].length; i++) {
            int j;
            for (j = 0; (j >= board.length) ? false : board[j][i] == '.'; j++)
                ;
            topRow[i] = j;
        }

        return new MiniBoard(topRow, pieceType);
    }

    private int getColumnHeightFromBottom(int column) {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            if (board[i][column] == 'X') {
                return BOARD_HEIGHT - i;
            }
        }

        return 0;
    }

    /**
     * Based on the NUMBER OF BLOCKS in each column, NOT the index of the highest one.
     * @return - the average height of the highest blocks in all of the columns
     */
    public double getAverageHeight() {
        int total = 0;
        for (int column = 0; column < BOARD_WIDTH; column++) {
//            System.out.print(getColumnHeightFromBottom(column) + " ");
            total += getColumnHeightFromBottom(column);
        }
//        System.out.println();

        return (double) total / BOARD_WIDTH;
    }

    public int getMaxHeight() {
        int maxHeight = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            maxHeight = Math.max(maxHeight, getColumnHeightFromBottom(i));
        }
        return maxHeight;
    }
}