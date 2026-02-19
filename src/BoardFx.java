import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;


/*The purpose of this new class is to have a no co-existence of UI and Back-end code in QuaxBoard.
* however, this class should be heavily linked to QuaxBoard */

// feel free to delete and do another way if more efficient
public class BoardFx {

    private BorderPane root;
    private Label turnLabel; //we declare all our different text labels, screen components, ui parts, etc.

    public BoardFx(Stage stage, QuaxBoard boardState) {

        root = new BorderPane();

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
        Scene scene = new Scene(root, 800, 600); //set dimensions


        //title
        stage.setTitle("Quax Game");
        stage.setScene(scene);

        /* EACH METHOD FOR DISPLAYING COMPONENTS UNDER  HERE */

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
