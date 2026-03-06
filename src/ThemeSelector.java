import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Optional;

public class ThemeSelector {

    public static Theme promptThemeSelection(Stage stage) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Select Theme");
        dialog.setContentText("Choose a theme to use on the board");

        ButtonType classicButton = new ButtonType("Classic");
        ButtonType alternateButton = new ButtonType("Alternate");
        ButtonType moreButton = new ButtonType("More");

        dialog.getDialogPane().getButtonTypes().addAll(
                classicButton,
                alternateButton,
                moreButton
        );

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {

            if (result.get() == classicButton) {
                return Theme.Classic;
            }

            if (result.get() == alternateButton) {
                return Theme.Alternate;
            }

            if (result.get() == moreButton) {
                return promptCustomTheme(stage);
            }
        }

        return null;
    }


    private static Theme promptCustomTheme(Stage stage) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Custom Themes");
        dialog.setContentText("Choose a custom theme");

        ButtonType desertButton = new ButtonType("Desert");
        ButtonType jungleButton = new ButtonType("Jungle");
        ButtonType backButton = new ButtonType("Back");

        dialog.getDialogPane().getButtonTypes().addAll(
                desertButton,
                jungleButton,
                backButton
        );

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {

            if (result.get() == desertButton) {
                return Theme.Desert;
            }

            if (result.get() == jungleButton) {
                return Theme.Jungle;
            }

            if (result.get() == backButton) {
                return promptThemeSelection(stage);
            }
        }

        return null;
    }
}
