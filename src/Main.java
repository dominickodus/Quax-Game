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
        Theme selectedTheme = ThemeSelector.promptThemeSelection(stage);
        ThemeSet themeSet = new ThemeSet(selectedTheme);
        QuaxBoard boardState = new QuaxBoard(selectedTheme);

        new BoardFx(stage, boardState, themeSet); //BLANK WINDOW
    }

    public static void main(String[] args) {
        launch();
    }
}
