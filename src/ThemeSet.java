import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;

public class ThemeSet {
    private Background background;
    private String player1ColourText;
    private String player2ColourText;
    private Color textColour;

    public ThemeSet (Theme theme) {
        switch (theme) {
            case Classic:
                background = new Background(
                        new BackgroundFill(Color.rgb(224, 218, 148), null, null)
                );
                player1ColourText = "BLACK";
                player2ColourText = "WHITE";
                textColour = Color.BLACK;
                break;
            case Alternate:
                background = new Background(
                        new BackgroundFill(Color.rgb(140, 160, 210), null, null)
                );
                player1ColourText = "BLACK";
                player2ColourText = "WHITE";
                textColour = Color.WHITE;
                break;
            case Jungle:
                Image jungle = new Image("assets/images/JungleBackground.png");

                BackgroundImage jungleBg = new BackgroundImage(
                        jungle,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(
                                BackgroundSize.AUTO,
                                BackgroundSize.AUTO,
                                false,
                                false,
                                true,
                                true
                        )
                );

                background = new Background(jungleBg);

                player1ColourText = "BLACK";
                player2ColourText = "WHITE";
                textColour = Color.BLACK;
                break;
            case Desert:
                Image desert = new Image("assets/images/DesertBackground.png");

                BackgroundImage desertBg = new BackgroundImage(
                        desert,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(
                                BackgroundSize.AUTO,
                                BackgroundSize.AUTO,
                                false,
                                false,
                                true,
                                true
                        )
                );

                background = new Background(desertBg);

                player1ColourText = "BLACK";
                player2ColourText = "WHITE";
                textColour = Color.WHITE;
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
