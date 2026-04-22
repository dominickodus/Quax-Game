import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardFxTest extends ApplicationTest {

    private QuaxBoard board;

    @Override
    public void start(Stage stage) {
        ThemeSet themeSet = new ThemeSet(Theme.Classic);
        board = new QuaxBoard(Theme.Classic);
        new BoardFx(stage, board, themeSet, null, Turn.Player2, true);
    }

    @Test
    void test_SR1_boardHas121Octagons() {
        Set<Node> octagons = lookup(".octagon").queryAll();
        assertEquals(121, octagons.size());
    }

    @Test
    void test_SR2_firstTurnBlack() {
        Label turnText = lookup("#turnText").query();
        assertTrue(turnText.getText().contains("BLACK"));
    }

    @Test
    void test_SR2_firstOctagonIsBlack() {
        Polygon firstOct = lookup(".octagon").query();
        clickOn(firstOct);
        assertEquals(Color.BLACK, firstOct.getFill());
    }

    @Test
    void test_Custom_themeAppliesBackgroundColour() {
        BorderPane root = lookup("#root").query();
        Background background = root.getBackground();
        Paint fill = background.getFills().get(0).getFill();
        assertTrue(fill instanceof Color);
        assertEquals(Color.rgb(224, 218, 148), fill);
    }

    @Test
    void test_Custom_themeAppliesImageBackground() {
        interact(() -> {
            ThemeSet themeSet = new ThemeSet(Theme.Jungle);
            new BoardFx(
                    (Stage) lookup("#root").query().getScene().getWindow(),
                    new QuaxBoard(Theme.Classic),
                    themeSet,
                    null,
                    Turn.Player2,
                    true
            );
        });

        BorderPane root = lookup("#root").query();
        Background background = root.getBackground();
        List<BackgroundImage> images = background.getImages();

        assertFalse(images.isEmpty());
        assertTrue(images.get(0).getImage().getUrl().contains("JungleBackground.png"));
    }

    @Test
    void test_SR3_pieButtonHiddenInitially() {
        Node pieButton = lookup("#pieButton").query();
        assertFalse(pieButton.isVisible());
    }


}