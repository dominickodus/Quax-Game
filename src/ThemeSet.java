import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class ThemeSet {
    private Background background;
    private String player1ColourText;
    private String player2ColourText;
    private Color textColour;

    public ThemeSet (Theme theme) {
        switch (theme) {
            case Standard:
                background = new Background(
                        new BackgroundFill(Color.rgb(224, 218, 148), null, null)
                );
                player1ColourText = "BLACK";
                player2ColourText = "WHITE";
                textColour = Color.BLACK;
                break;
            case Colourful:
                background = new Background(
                        new BackgroundFill(Color.rgb(114, 191, 109), null, null)
                );
                break;
        }
    }

    public Background getBackground() {
        return background;
    }

    public String getPlayer1ColourText() {
        return player1ColourText;
    }

    public String getPlayer2ColourText() {
        return player2ColourText;
    }

    public Color getTextColour() {
        return textColour;
    }
}
