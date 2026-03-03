import javafx.scene.control.*;
import javafx.stage.Stage;


public class ThemeSelector {
    public static Theme promptThemeSelection(Stage stage) {
        Dialog<Theme> dialog = new Dialog<>();
        dialog.setTitle("Select Theme");
        dialog.setContentText("Choose a theme to use on the board");

        ButtonType standardButton = new ButtonType("Standard");
        ButtonType colourfulButton = new ButtonType("Colourful");
        dialog.getDialogPane().getButtonTypes().add(standardButton);
        dialog.getDialogPane().getButtonTypes().add(colourfulButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == standardButton) {
                return Theme.Standard;
            } else if (buttonType == colourfulButton) {
                return Theme.Colourful;
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }
}
