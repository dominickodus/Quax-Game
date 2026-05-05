import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        launchGame();
    }

    private void launchGame() {
        Theme selectedTheme = ThemeSelector.promptThemeSelection(stage);
        if (selectedTheme == null) {
            System.exit(0);
        }
        ThemeSet themeSet = new ThemeSet(selectedTheme);
        QuaxBoard boardState = new QuaxBoard();

        new BoardFx(stage, boardState, themeSet, () -> {
            stage.setScene(null);
            launchGame();
        }, null, false);
    }

    public static void main(String[] args) {
        launch();
    }
}
