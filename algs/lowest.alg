private int play() throws InterruptedException, NonexistentTetrisPieceException {
        Api api = new Api();

        api.newGame();

        while (true) {
            Piece currentPiece = api.getPiece();

            List<Coords[]> possiblePositions = api.possibleMoves(currentPiece);

            // Find the lowest possible one. (Stupid method, good control group)
            Coords[] lowestPosition = possiblePositions.get(0);

            for (int i = 1; i < possiblePositions.size(); i++) {
                Coords[] anotherPosition = possiblePositions.get(i);
//                System.out.println(Arrays.toString(anotherPosition));

                int lowestOfLowestPosition = lowestPosition[0].r;
                int lowestOfAnotherPosition = anotherPosition[0].r;

                for (int it = 1; it < 4; it++) {
                    if (lowestPosition[it].r > lowestOfLowestPosition) { //If it's even lower
                        lowestOfLowestPosition = lowestPosition[it].r;
                    }
                }

                for (int it = 1; it < 4; it++) {
                    if (anotherPosition[it].r > lowestOfAnotherPosition) { //If it's even lower
                        lowestOfAnotherPosition = anotherPosition[it].r;
                    }
                }


                if (lowestOfAnotherPosition > lowestOfLowestPosition) {
                    lowestPosition = anotherPosition;
                }
            }

            boolean placed = api.placePiece(lowestPosition);

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