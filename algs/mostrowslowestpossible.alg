private int play() throws InterruptedException, NonexistentTetrisPieceException {
        Api api = new Api();

        api.newGame();

        while (true) {
            Piece currentPiece = api.getPiece();

            List<Coords[]> possiblePositions = api.possibleMoves(currentPiece);

            List<PositionRows> goodPositions = new ArrayList<>();
            goodPositions.add(new PositionRows(possiblePositions.get(0), api.tentativeRowsRemoved(possiblePositions.get(0))));

            for (Coords[] position : possiblePositions) {
                Collections.sort(goodPositions);

                int rowsRemovedWithThisPosition = api.tentativeRowsRemoved(position);

                if (rowsRemovedWithThisPosition > goodPositions.get(0).getRows()) {
                    goodPositions.clear();
                    goodPositions.add(new PositionRows(position, rowsRemovedWithThisPosition));
                } else if (rowsRemovedWithThisPosition == goodPositions.get(0).getRows()) {
                    goodPositions.add(new PositionRows(position, rowsRemovedWithThisPosition));
                }
            }

//            System.out.println(goodPositions);

            // Get best position of good positions
            Coords[] bestPosition = lowest(goodPositions);

            boolean placed = api.placePiece(bestPosition);

            if (!placed) { // Game lost
//                System.out.println("Final score: " + api.getScore() + ".");
                return api.getScore();
            }

            // Print the current board to the JTextArea
            if (displayGame) {
                printedTetrisBoard.setText(api.getBoard().toString());

                Thread.sleep(1000);
            }
        }
    }