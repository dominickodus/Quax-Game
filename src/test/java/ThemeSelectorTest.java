import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


//Test Class for Theme Selector UI
//Note that theme selection is an added feature of ours, so these tests are not mapped to any SRs
public class ThemeSelectorTest extends ApplicationTest {

    private Theme result;

    @Override
    public void start(Stage stage) {
    }


    //Testing basic theme selection (alternate or classic)
    @Test
    void test_Custom_ClassicThemeSelection() {
        interact(() -> {
            new Thread(() -> result = ThemeSelector.promptThemeSelection(null)).start();
        });
        clickOn("Classic");
        assertEquals(Theme.Classic, result);
    }
    @Test
    void test_Custom_AlternateThemeSelection() {

        interact(() -> {
            new Thread(() -> result = ThemeSelector.promptThemeSelection(null)).start();
        });

        clickOn("Alternate");

        assertEquals(Theme.Alternate, result);
    }

    //Testing that you can select themes in the 'More' section
    @Test
    void test_Custom_JungleThemeSelection() {

        interact(() -> {
            new Thread(() -> result = ThemeSelector.promptThemeSelection(null)).start();
        });

        clickOn("More");
        clickOn("Jungle");

        assertEquals(Theme.Jungle, result);
    }
    @Test
    void test_Custom_DesertThemeSelection() {

        interact(() -> {
            new Thread(() -> result = ThemeSelector.promptThemeSelection(null)).start();
        });

        clickOn("More");
        clickOn("Desert");

        assertEquals(Theme.Desert, result);
    }


    //Test that Cancel returns null
    @Test
    void test_Custom_CancelSelection() {

        interact(() -> {
            new Thread(() -> result = ThemeSelector.promptThemeSelection(null)).start();
        });

        clickOn("Cancel");

        assertNull(result);
    }
}