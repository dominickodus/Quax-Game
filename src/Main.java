import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override

    public void start(Stage stage) {

        Theme selectedTheme = ThemeSelector.promptThemeSelection(stage);
        if (selectedTheme == null) {
            System.exit(0);
        }
        ThemeSet themeSet = new ThemeSet(selectedTheme);
        QuaxBoard boardState = new QuaxBoard(selectedTheme);

        new BoardFx(stage, boardState, themeSet);
    }

    public static void main(String[] args) {
        launch();
    }
}
