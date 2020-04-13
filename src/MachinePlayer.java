import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MachinePlayer {
    // Choose random move from a list of empty squares
    public static int makeRandomMove(Game game) {
        List<Integer> validMoves = game.validMoves();
        return validMoves.get(new Random().nextInt(validMoves.size()));
    }

    // Make immediate winning moves, or block immediate losing moves
    public static int makeBasicMove(Game game) {
        char[] board = game.getBoard();
        char myPiece = game.getTurn();

        List<Integer> winningMoves = new ArrayList<>(9);
        List<Integer> blockingMoves = new ArrayList<>(9);

        // Check rows
        for (int i = 0; i < board.length; i += 3) {
            int myCount = 0;
            int emptyCount = 0;
            int otherCount = 0;
            int emptyIndex = -1;

            for (int j = i; j < 3 + i; j++) {
                if (game.isEmpty(j)) {
                    emptyCount++;
                    emptyIndex = j;
                } else if (board[j] == myPiece) myCount++;
                else otherCount++;
            }
            // I can win, find winning move
            if (myCount == 2 && emptyCount == 1) {
                winningMoves.add(emptyIndex);
            } else if (otherCount == 2 && emptyCount == 1) { // I can lose, find blocking move
                blockingMoves.add(emptyIndex);
            }
        }

        // Check cols
        for (int i = 0; i < 3; i++) {
            int myCount = 0;
            int emptyCount = 0;
            int otherCount = 0;
            int emptyIndex = -1;

            for (int j = i; j < board.length; j+=3) {
                if (game.isEmpty(j)) {
                    emptyCount++;
                    emptyIndex = j;
                } else if (board[j] == myPiece) myCount++;
                else otherCount++;
            }
            // I can win, find winning move
            if (myCount == 2 && emptyCount == 1) {
                winningMoves.add(emptyIndex);
            } else if (otherCount == 2 && emptyCount == 1) { // I can lose, find blocking move
                blockingMoves.add(emptyIndex);
            }
        }

        // Check diags
        for (int i = 0, myCount = 0, emptyCount = 0, otherCount = 0, emptyIndex = -1; i < board.length; i += 4) {
            if (game.isEmpty(i)) {
                emptyCount++;
                emptyIndex = i;
            } else if (board[i] == myPiece) myCount++;
            else otherCount++;
            // I can win, find winning move
            if (myCount == 2 && emptyCount == 1) {
                winningMoves.add(emptyIndex);
            } else if (otherCount == 2 && emptyCount == 1) { // I can lose, find blocking move
                blockingMoves.add(emptyIndex);
            }
        }
        for (int i = 2, myCount = 0, emptyCount = 0, otherCount = 0, emptyIndex = -1; i < board.length; i += 2) {
            if (game.isEmpty(i)) {
                emptyCount++;
                emptyIndex = i;
            } else if (board[i] == myPiece) myCount++;
            else otherCount++;
            // I can win, find winning move
            if (myCount == 2 && emptyCount == 1) {
                winningMoves.add(emptyIndex);
            } else if (otherCount == 2 && emptyCount == 1) { // I can lose, find blocking move
                blockingMoves.add(emptyIndex);
            }
        }

        if (winningMoves.size() != 0) return winningMoves.get(0);
        if (blockingMoves.size() != 0) return blockingMoves.get(0);

        // If neither side is threatening to win, make a random move
        return makeRandomMove(game);
    }

    public static int makeBestMove(Game game) {
        int[] values = minimax(game, -1);
        System.out.println("Eval: " + values[0]);
        return values[1];
    }

    // Use -1 (or any negative number) for infinite depth
    // Returns an int array of length 2, first value is eval, second is best move
    // Eval of 0 is draw, 1 is X victory, -1 is O victory
    private static int[] minimax(Game game, int depth) {
        int[] out = {-1, -1};
        if (depth == 0 || game.isOver()) {
            if (game.hasWinner()) out[0] = game.winner() == 'X' ? 1 : -1;
            else out[0] = 0;

            return out;
        }
        // Treats X as maximizing player, O as minimizing
        // Therefore, maximizing = game.getTurn() == 'X';
        boolean maximizing = game.getTurn() == 'X';
        List<Integer> validMoves = game.validMoves();
        if (maximizing) {
            int max = Integer.MIN_VALUE;
            for (int i : validMoves) {
                Game temp = new Game(game);
                temp.makeMove(i);
                int eval = minimax(temp, depth -1)[0];
                max = Math.max(max, eval);

                // Set best move
                if (max == eval) {
                    out[1] = i;
                }
            }
            out[0] = max;
        } else {
            int min = Integer.MAX_VALUE;
            for (int i : validMoves) {
                Game temp = new Game(game);
                temp.makeMove(i);
                int eval = minimax(temp, depth -1 )[0];
                min = Math.min(min, eval);

                // Set best move
                if (min == eval) {
                    out[1] = i;
                }
            }
            out[0] = min;
        }
        return out;
    }

    // Implementation of minimax algorithm with alpha-beta pruning
    private static int[] alphabeta(Game game, int depth, int alpha, int beta) {
        int[] out = {-1, -1};
        if (depth == 0 || game.isOver()) {
            if (game.hasWinner()) out[0] = game.winner() == 'X' ? 1 : -1;
            else out[0] = 0;

            return out;
        }
        // Treats X as maximizing player, O as minimizing
        // Therefore, maximizing = game.getTurn() == 'X';
        boolean maximizing = game.getTurn() == 'X';
        List<Integer> validMoves = game.validMoves();
        if (maximizing) {
            int max = Integer.MIN_VALUE;
            for (int i : validMoves) {
                Game temp = new Game(game);
                temp.makeMove(i);
                int eval = alphabeta(temp, depth -1, alpha, beta)[0];
                max = Math.max(max, eval);
                alpha = Math.max(alpha, eval);

                // Set best move
                if (max == eval) {
                    out[1] = i;
                }

                if (beta <= alpha) break;

            }
            out[0] = max;
        } else {
            int min = Integer.MAX_VALUE;
            for (int i : validMoves) {
                Game temp = new Game(game);
                temp.makeMove(i);
                int eval = alphabeta(temp, depth -1, alpha, beta)[0];
                min = Math.min(min, eval);
                beta = Math.min(beta, eval);

                // Set best move
                if (min == eval) {
                    out[1] = i;
                }

                if (beta <= alpha) break;

            }
            out[0] = min;
        }
        return out;
    }

    // Illustrate the impact of alpha-beta pruning
    public static void benchmark() {
        Game game1 = new Game();
        Game game2 = new Game();

        long start1 = System.nanoTime();
        minimax(game1, -1);
        long end1 = System.nanoTime();

        System.out.println(TimeUnit.NANOSECONDS.toMillis(end1 - start1));

        long start2 = System.nanoTime();
        alphabeta(game2, -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        long end2 = System.nanoTime();

        System.out.println(TimeUnit.NANOSECONDS.toMillis(end2 - start2));
    }

}
