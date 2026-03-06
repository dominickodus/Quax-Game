import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override

    public void start(Stage stage) {

        /*only using STANDARD for now,
        need to find way to switch themes AFTER board is rendered
         */
        Theme selectedTheme = ThemeSelector.promptThemeSelection(stage);
        if (selectedTheme == null) {
            System.exit(0);
        }
        ThemeSet themeSet = new ThemeSet(selectedTheme);
        QuaxBoard boardState = new QuaxBoard(selectedTheme);

        new BoardFx(stage, boardState, themeSet); //BLANK WINDOW
    }

    public static void main(String[] args) {
        launch();
    }
}
