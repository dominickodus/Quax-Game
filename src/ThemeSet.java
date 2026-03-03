import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class ThemeSet {
    private Background background;
    private Color player1FillColour;
    private Color player2FillColour;
    private String player1ColourText;
    private String player2ColourText;
    private Color textColour;

    public ThemeSet (Theme theme) {
        switch (theme) {
            case Standard:
                background = new Background(
                        new BackgroundFill(Color.rgb(224, 218, 148), null, null)
                );
                player1FillColour = Color.BLACK;
                player2FillColour = Color.WHITE;
                player1ColourText = "BLACK";
                player2ColourText = "WHITE";
                textColour = Color.BLACK;
                break;
            case Colourful:
                background = new Background(
                        new BackgroundFill(Color.rgb(114, 191, 109), null, null)
                );
                player1FillColour = Color.RED;
                player2FillColour = Color.BLUE;
                player1ColourText = "RED";
                player2ColourText = "BLUE";
                textColour = Color.BLACK;
                break;
        }
    }

    public Background getBackground() {
        return background;
    }

    public Color getPlayer1FillColour() {
        return player1FillColour;
    }

    public Color getPlayer2FillColour() {
        return player2FillColour;
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
