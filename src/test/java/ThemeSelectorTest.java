import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

public class ThemeSelectorTest extends ApplicationTest {

    private Theme result;

    @Override
    public void start(Stage stage) {}

    private void openDialog() {
        new Thread(() -> {
            try {
                WaitForAsyncUtils.asyncFx(() ->
                        result = ThemeSelector.promptThemeSelection(null)
                ).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void test_Custom_ClassicThemeSelection() {
        openDialog();
        clickOn("Classic");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(Theme.Classic, result);
    }

    @Test
    void test_Custom_AlternateThemeSelection() {
        openDialog();
        clickOn("Alternate");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(Theme.Alternate, result);
    }

    @Test
    void test_Custom_JungleThemeSelection() {
        openDialog();
        clickOn("More");
        clickOn("Jungle");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(Theme.Jungle, result);
    }

    @Test
    void test_Custom_DesertThemeSelection() {
        openDialog();
        clickOn("More");
        clickOn("Desert");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(Theme.Desert, result);
    }

    @Test
    void test_Custom_CancelSelection() {
        openDialog();
        clickOn("Cancel");
        WaitForAsyncUtils.waitForFxEvents();
        assertNull(result);
    }
}