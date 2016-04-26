    // Makes sure not to make holes; fits pieces together perfectly, and withing this constraint, goes as low as possible.
    private int playBetter() throws InterruptedException, NonexistentTetrisPieceException {
        Api api = new Api();
        api.newGame();

        while (true) {
            Piece currentPiece = api.getPiece();

            List<Coords[]> possiblePositions = api.possibleMoves(currentPiece);
            Board cur_board = api.getBoard();
            char[][] board = cur_board.getBoard();

            Coords[] decision = possiblePositions.get(0);
            boolean decision_no_holes = false;
            int decision_bottom = decision[0].r;

            for (Coords[] current : possiblePositions) {
                int bottom = current[0].r;
                boolean no_holes = true;
                for (Coords co : current) {
                    bottom = Math.max(bottom, co.r);
                    boolean in_bottom_layer = true;
                    for (Coords other : current)
                        if (other.r > co.r && other.c == co.c)
                            in_bottom_layer = false;
                    if (in_bottom_layer) {
                        if (co.r < board.length - 1)
                            if (board[co.r + 1][co.c] == '.') // there is a hole
                                no_holes = false;
                    }
                }
                if (decision_no_holes == false) {
                    if (no_holes == false) {
                        if (bottom > decision_bottom) {
                            decision_bottom = bottom;
                            decision = current;
                        }
                    }
                    else {
                        decision_no_holes = true;
                        decision_bottom = bottom;
                        decision = current;
                    }
                }
                else {
                    if (no_holes) {
                        if (bottom > decision_bottom) {
                            decision_bottom = bottom;
                            decision = current;
                        }
                    }
                }
            }

            boolean placed = api.placePiece(decision);

            if (!placed) { // Game lost
//                System.out.println("Final score: " + api.getScore() + ".");
                return api.getScore();
            }

            // Print the current board to the JTextArea
            if (displayGame) {
                printedTetrisBoard.setText(api.getBoard().toString());

                Thread.sleep(500);
            }
        }
    }
