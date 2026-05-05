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
    private Color textColour;

    public ThemeSet (Theme theme) {
        switch (theme) {
            case Classic:
                background = new Background(
                        new BackgroundFill(Color.rgb(224, 218, 148), null, null)
                );
                textColour = Color.BLACK;
                break;
            case Alternate:
                background = new Background(
                        new BackgroundFill(Color.rgb(140, 160, 210), null, null)
                );
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
                textColour = Color.WHITE;
                break;
        }
    }

    public Background getBackground() {
        return background;
    }

    public Color getTextColour() {
        return textColour;
    }
}
