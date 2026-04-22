import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BotControllerTest {
    @Test
    void test_isBot() {
        BotController botController = new BotController();
        assertTrue(botController.isBot());
    }

    @Test
    void test_lastStrategyUsedNull() {
        BotController botController = new BotController();
        assertEquals("", botController.getLastStrategyUsed());
    }

    @Test
    void test_lastScoresNull() {
        BotController botController = new BotController();
        assertTrue(botController.getLastScores().isEmpty());
    }

    @Test
    void test_lastMoveNull() {
        BotController botController = new BotController();
        assertNull(botController.getLastMove());
    }

    @Test
    void test_lastMove() {
        QuaxBoard quaxBoard = new QuaxBoard((Theme) null);
        BotController botController = new BotController();
        botController.makeMove(quaxBoard);
        assertNotNull(botController.getLastMove());
    }

    @Test
    void test_lastScores() {
        QuaxBoard quaxBoard = new QuaxBoard((Theme) null);
        BotController botController = new BotController();
        botController.makeMove(quaxBoard);
        assertFalse(botController.getLastScores().isEmpty());
    }
}
