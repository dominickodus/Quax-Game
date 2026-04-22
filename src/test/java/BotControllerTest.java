import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BotControllerTest {

    @Test
    void test_SR8_isBotReturnsTrue() {
        BotController botController = new BotController();
        assertTrue(botController.isBot());
    }

    @Test
    void test_SR8_lastStrategyInitiallyEmpty() {
        BotController botController = new BotController();
        assertEquals("", botController.getLastStrategyUsed());
    }

    @Test
    void test_SR8_lastScoresInitiallyEmpty() {
        BotController botController = new BotController();
        assertTrue(botController.getLastScores().isEmpty());
    }

    @Test
    void test_SR8_lastMoveInitiallyNull() {
        BotController botController = new BotController();
        assertNull(botController.getLastMove());
    }

    @Test
    void test_SR8_makeMoveSetsLastMove() {
        QuaxBoard board = new QuaxBoard((Theme) null);
        BotController botController = new BotController();

        botController.makeMove(board);

        assertNotNull(botController.getLastMove());
    }

    @Test
    void test_SR8_ScoresPopulate() {
        QuaxBoard board = new QuaxBoard((Theme) null);
        BotController botController = new BotController();

        botController.makeMove(board); // opening
        botController.makeMove(board); // scoring kicks in

        assertFalse(botController.getLastScores().isEmpty());
    }
}