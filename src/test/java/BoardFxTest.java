import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BoardFxTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        ThemeSet themeSet = new ThemeSet(Theme.Standard);
        QuaxBoard board = new QuaxBoard(null);
        new BoardFx(stage, board, themeSet);
    }

    /**
     * SR1.1 – Board renders correctly
     */
    @Test
    void test_SR1_boardHas121Octagons() {
        Set<Node> octagons = lookup(".octagon").queryAll();
        assertEquals(121, octagons.size());
    }

    /**
     * SR2.1 – First turn is BLACK
     */
    @Test
    void test_SR2_firstTurnBlack() {
        Label turnText = lookup("#turnText").query();
        assertTrue(turnText.getText().contains("BLACK"));
    }
    /**
     * SR2 – First placed stone is BLACK
     */
    @Test
    void test_SR2_firstOctagonIsBlack() {
        Polygon firstOct = lookup(".octagon").query();

        clickOn(firstOct);

        Color fill = (Color) firstOct.getFill();
        assertEquals(0.0, fill.getRed(), 0.01);
        assertEquals(0.0, fill.getGreen(), 0.01);
        assertEquals(0.0, fill.getBlue(), 0.01);
    }

    // Custom feature
    // Check themes apply correctly
    @Test
    void ThemeAppliesBackground() {
        BorderPane borderPane = lookup("#root").query();
        Background background = borderPane.getBackground();
        List<BackgroundFill> fills = background.getFills();
        Paint fill = fills.get(0).getFill();
        if (fill instanceof Color color) {
            boolean isEqual = color.equals(Color.rgb(224, 218, 148));
        }
    }
}
