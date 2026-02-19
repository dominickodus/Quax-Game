import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {

    @Override

    public void start(Stage stage) {

        /*only using STANDARD for now,
        need to find way to switch themes AFTER board is rendered
         */
        QuaxBoard boardState = new QuaxBoard(Theme.STANDARD);

        new BoardFx(stage, boardState); //BLANK WINDOW
    }

    public static void main(String[] args) {
        launch();

    }
}
