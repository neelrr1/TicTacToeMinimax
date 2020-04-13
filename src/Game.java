import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final char EMPTY = '\u0000';

    private char[] board = new char[9];

    private char turn = 'X';

    public Game(){}

    public Game(Game game) {
        board = game.board.clone();
        turn = game.turn;
    }

    public char getTurn() {
        return turn;
    }

    public boolean hasWinner() {
        return winner() != EMPTY;
    }

    public boolean isFull() {
        for (char c : board) {
            if (c == EMPTY) return false;
        }
        return true;
    }

    public char winner() {
        // Check row 1
        if (board[0] == board[1] && board[1] == board[2]) {
            return board[0];
        }
        // Check row 2
        if (board[3] == board[4] && board[3] == board[5]) {
            return board[3];
        }
        // Check row 3
        if (board[6] == board[7] && board[6] == board[8]) {
            return board[6];
        }

        // Check col 1
        if (board[0] == board[3] && board[0] == board[6]) {
            return board[0];
        }
        // Check col 2
        if (board[1] == board[4] && board[1] == board[7]) {
            return board[1];
        }
        // Check col 3
        if (board[2] == board[5] && board[2] == board[8]) {
            return board[2];
        }

        // Check diag 1
        if (board[0] == board[4] && board[0] == board[8]) {
            return board[0];
        }
        // Check diag 2
        if (board[2] == board[4] && board[2] == board[6]) {
            return board[2];
        }

        return EMPTY; // No winner
    }

    public String winnerString() {
        char c = winner();
        if (c == EMPTY) return "Draw!";
        return c + " Won!";
    }

    public char opposite(char c) {
        return c == 'X' ? 'O' : 'X';
    }

    public void makeMove(int index) {
        assert isEmpty(index) : "Move index is not empty";
        assert index >= 0 && index < 9 : "Move index out of bounds";
        board[index] = turn;
        turn = opposite(turn);
    }

    public boolean isEmpty(int index) {
        return board[index] == EMPTY;
    }

    public List<Integer> validMoves() {
        ArrayList<Integer> out = new ArrayList<>(board.length);
        for (int i = 0; i < board.length; i++) {
            if (isEmpty(i)) out.add(i);
        }
        return out;
    }

    public boolean isValid(int index) {
        return validMoves().contains(index);
    }

    public boolean isOver() {
        return (isFull() || hasWinner());
    }

    public char[] getBoard() {
        return board;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < board.length; i+=3) {
            out.append(String.format("%1c %1c %1c\n", board[i], board[i + 1], board[i + 2]));
        }
        return out.toString();
    }

    public char getSquare(int index) {
        return board[index];
    }
}
