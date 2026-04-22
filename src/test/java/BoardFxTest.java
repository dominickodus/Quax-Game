import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
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
        new BoardFx(stage, board, themeSet, null);
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
    void test_Custom_ThemeAppliesBackground() {
        BorderPane borderPane = lookup("#root").query();
        Background background = borderPane.getBackground();
        List<BackgroundFill> fills = background.getFills();
        Paint fill = fills.get(0).getFill();
        if (fill instanceof Color color) {
            assertEquals(Color.rgb(224, 218, 148), color);
        }
    }

    //Custom Feature
    //Test that custom boards apply background image.
    @Test
    void test_Custom_themeAppliesImageBackground() {

        interact(() -> {
            ThemeSet themeSet = new ThemeSet(Theme.Jungle);
            QuaxBoard board = new QuaxBoard(Theme.Classic);
            new BoardFx((Stage) lookup("#root").query().getScene().getWindow(), board, themeSet, null);
        });

        BorderPane borderPane = lookup("#root").query();

        Background background = borderPane.getBackground();
        List<BackgroundImage> images = background.getImages();

        assertFalse(images.isEmpty());

        String url = images.get(0).getImage().getUrl();

        assertTrue(url.contains("JungleBackground.png"));
    }


    //Pie Rule Button Tests
    @Test
    void test_SR3_PieButtonHiddenAtStart() {
        Node pieButton = lookup("#pieButton").query();
        assertFalse(pieButton.isVisible());
    }

    @Test
    void test_SR3_PieButtonAppears() {
        Polygon firstOct = lookup(".octagon").query();
        clickOn(firstOct);
        Node pieButton = lookup("#pieButton").query();
        assertTrue(pieButton.isVisible());
    }

    @Test
    void test_SR4_PieButtonDisappearsAfterMove() {

        List<Polygon> octagons = lookup(".octagon").queryAll().stream().map(n -> (Polygon) n).toList();

        clickOn(octagons.get(0));
        clickOn(octagons.get(1));
        Node pieButton = lookup("#pieButton").query();
        assertFalse(pieButton.isVisible());
    }

    @Test
    void test_PieButtonDisappearsAfterClick() {

        Polygon firstOct = lookup(".octagon").query();
        clickOn(firstOct);
        Node pieButton = lookup("#pieButton").query();
        assertTrue(pieButton.isVisible());
        clickOn(pieButton);
        assertFalse(pieButton.isVisible());
    }

    @Test
    void test_PieRuleActivatesOnClick() {
        Polygon firstOct = lookup(".octagon").query();
        clickOn(firstOct);
        assertFalse(board.isPieRuleEnabled());
        clickOn("#pieButton");
        assertTrue(board.isPieRuleEnabled());
    }
}
