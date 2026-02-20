import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;


/*The purpose of this new class is to have a no co-existence of UI and Back-end code in QuaxBoard.
* however, this class should be heavily linked to QuaxBoard */

// feel free to delete and do another way if more efficient
public class BoardFx {

    private static String numToLetter(int num) {
        switch (num) {
            case 0:
                return "A";
                case 1:
                    return "B";
                    case 2:
                        return "C";
                        case 3:
                            return "D";
                            case 4:
                                return "E";
                                case 5:
                                    return "F";
                                    case 6:
                                        return "G";
                                        case 7:
                                            return "H";
                                            case 8:
                                                return "I";
                                                case 9:
                                                    return "J";
                                                    case 10:
                                                        return "K";
        }
        return "";
    }
    private BorderPane root;
    private Label turnLabel; //we declare all our different text labels, screen components, ui parts, etc.

    public BoardFx(Stage stage, QuaxBoard boardState) {

        root = new BorderPane();
        root.setStyle("-fx-background-color: #e0da94;");

        //TITLE
        Label title = new Label("QUAX GAME");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        //TURN INDICATOR (UNPOLISHED)
        turnLabel = new Label();
        turnLabel.setStyle("-fx-font-size: 18px;");
        root.setBottom(turnLabel);

        //SET UP
        VBox topSection = new VBox(title);
        topSection.setSpacing(10);
        topSection.setStyle("-fx-alignment: center; -fx-padding: 20;");
        root.setTop(topSection);

        //root.setTop(topSection);
        Scene scene = new Scene(root, 800, 600); //set dimensions

        //title
        stage.setTitle("Quax Game (Player vs. Player)");
        stage.setScene(scene);

        /* EACH METHOD FOR DISPLAYING COMPONENTS UNDER  HERE */

        // Grid
        GridPane boardGrid = new GridPane();
        boardGrid.setGridLinesVisible(true);
        boardGrid.setStyle("-fx-alignment: center;");
        for (int i = 0; i < 11; i++) {
            Label numLabel = new Label(String.format("%d", i + 1));
            Label letterLabel = new Label(numToLetter(i));
            numLabel.setPrefSize(40, 40);
            letterLabel.setPrefSize(40, 40);
            numLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-alignment: center;");
            letterLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-alignment: center;");
            boardGrid.add(numLabel, 0, i + 1);
            boardGrid.add(letterLabel, i + 1, 0);
        }

        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                Button button = new Button();
                button.setStyle("-fx-background-color: rgba(0, 0, 0, 0); " +
                        "-fx-pref-width: 40px; -fx-pref-height: 40px;");
                boardGrid.add(button, x + 1, y + 1);
            }
        }
        root.setCenter(boardGrid);

        //Turn
        displayTurn(boardState);

        stage.show();
    }

    public void displayTurn(QuaxBoard boardState) {
        String turnText =
                boardState.getTurn() == Turn.Player1 ? "WHITE" : "BLACK";

        this.turnLabel.setText("CURRENT TURN: " + turnText);
    }


    public BorderPane getRoot() {
        return root;
    }
}
