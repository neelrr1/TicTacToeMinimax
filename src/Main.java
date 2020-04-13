import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {

    private static final int DEFAULT_SIZE = 750; // Helps if multiple of 3

    // Assumes labels array is all initialized
    private void updateUI(Text[] labels, Game game) {
        for (int i = 0; i < labels.length; i++) {
            Text label = labels[i];
            label.setText(game.getSquare(i) + "");
        }
    }

    @Override
    public void start(Stage stage) {
        int gameSize = DEFAULT_SIZE;
        int squareSize = gameSize / 3;

        Game game = new Game();
        HashMap<Rectangle, Integer> rectsToIndex = new HashMap<>();
        AnchorPane root = new AnchorPane();
        Text[] labels = new Text[9];

        // Font for labels
        Font font = Font.font("monospace", squareSize);

        // For making checkerboard fill pattern
        boolean dark = false;

        for (int i = 0; i < 9; i++) {
            // Set up rectangle
            Rectangle rect = new Rectangle(squareSize, squareSize);
            rect.setX((i % 3) * squareSize);
            rect.setY((i / 3) * squareSize);
            rect.setFill(dark ? Color.LIGHTGRAY : Color.WHITE);

            dark = !dark;

            // Set up labels
            Text label = new Text();
            label.setFont(font);
            label.setX(rect.getX() + 0.167 * squareSize);
            label.setY(rect.getY() + 0.75 * squareSize);

            // Most game logic goes here
            rect.setOnMouseClicked(e -> {
                int index = rectsToIndex.get(rect);
                System.out.println(index + " clicked!");
                if (!game.isOver() && game.isValid(index)) {
                    // Update labels
                    label.setText(game.getTurn() + "");

                    // Update game state
                    game.makeMove(index);

                    // Game is not over, make machine player's move
                    if (!game.isOver()) {
//                        int move = MachinePlayer.makeBestMove(game);
//                        int move = MachinePlayer.makeBasicMove(game);
                        int move = MachinePlayer.makeBestMove(game);
                        labels[move].setText(game.getTurn() + "");
                        game.makeMove(move);
                    }
                }
                // Show alert if someone won the game
                if (game.hasWinner() || game.isFull()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, game.winnerString());
                    alert.setTitle("Game Over");
                    alert.showAndWait();
                }
            });
            rectsToIndex.put(rect, i);
            root.getChildren().addAll(rect, label);
            labels[i] = label;
        }

        // If you want to play as O, uncomment these lines
//        game.makeMove(MachinePlayer.makeRandomMove(game));
//        updateUI(labels, game);

        MachinePlayer.benchmark();

        stage.setTitle("Tic Tac Toe!");
        stage.setScene(new Scene(root, gameSize, gameSize));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
