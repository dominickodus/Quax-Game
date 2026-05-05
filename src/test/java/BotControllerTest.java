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
        QuaxBoard board = new QuaxBoard();
        BotController botController = new BotController();

        botController.makeMove(board);

        assertNotNull(botController.getLastMove());
    }

    @Test
    void test_SR8_ScoresPopulate() {
        QuaxBoard board = new QuaxBoard();
        BotController botController = new BotController();

        botController.makeMove(board); // opening
        botController.makeMove(board); // scoring kicks in

        assertFalse(botController.getLastScores().isEmpty());
    }

    @Test
    void test_SR8_openingStrategyIsUsedOnFirstMove() {
        QuaxBoard board = new QuaxBoard();
        BotController bot = new BotController();

        bot.makeMove(board);

        assertEquals("Opening Strategy", bot.getLastStrategyUsed());
    }

    @Test
    void test_SR8_openingMoveIsCentral() {
        QuaxBoard board = new QuaxBoard();
        BotController bot = new BotController();

        bot.makeMove(board);
        Move move = bot.getLastMove();

        boolean isCentral =
                (move.getX() == 5 && move.getY() == 5) ||
                        (move.getX() == 5 && move.getY() == 4) ||
                        (move.getX() == 5 && move.getY() == 6) ||
                        (move.getX() == 4 && move.getY() == 5) ||
                        (move.getX() == 6 && move.getY() == 5);

        assertTrue(isCentral);
    }

    @Test
    void test_SR8_pathfindingStrategyUsedAfterOpening() {
        QuaxBoard board = new QuaxBoard();
        BotController bot = new BotController();

        bot.makeMove(board);
        bot.makeMove(board);

        assertEquals("Pathfinding Strategy", bot.getLastStrategyUsed());
    }

    @Test
    void test_SR8_strategyExplanationIsSet() {
        QuaxBoard board = new QuaxBoard();
        BotController bot = new BotController();

        bot.makeMove(board);

        assertNotEquals("", bot.getLastExplanation());
    }

    @Test
    void test_SR8_moveIsAppliedToBoard() {
        QuaxBoard board = new QuaxBoard();
        BotController bot = new BotController();

        bot.makeMove(board);
        Move move = bot.getLastMove();

        if (move.isRhombus()) {
            assertNotEquals(Colour.NULL, board.getRhombus(move.getX(), move.getY()));
        } else {
            assertNotEquals(Colour.NULL, board.getStone(move.getX(), move.getY()));
        }
    }

    @Test
    void test_SR8_moveIsWithinBounds() {
        QuaxBoard board = new QuaxBoard();
        BotController bot = new BotController();

        bot.makeMove(board);
        Move move = bot.getLastMove();

        if (move.isRhombus()) {
            assertTrue(board.inBoundsRhombus(move.getX(), move.getY()));
        } else {
            assertTrue(board.inBoundsStone(move.getX(), move.getY()));
        }
    }

    @Test
    void test_SR8_botChoosesWinningMove() {
        QuaxBoard board = new QuaxBoard();
        BotController bot = new BotController();

        // Builds vertical black chain
        for (int y = 0; y < 10; y++) {
            QuaxGame.placeStone(board, 5, y);
            QuaxGame.placeStone(board, 0, y);
        }

        //Makes sure bot is Black and chooses the winning move
        while (board.getTurn() != Turn.Player1) {
            board.switchTurn();
        }

        Move move = bot.chooseMove(board);

        assertNotNull(move);

        QuaxBoard copy = new QuaxBoard(board);
        QuaxGame.placeStone(copy, move.getX(), move.getY());

        boolean wins = QuaxGame.checkWin(copy, move.getX(), move.getY());
        assertTrue(wins);
    }

    @Test
    void test_SR8_botBlocksWinningMove() {
        QuaxBoard board = new QuaxBoard();
        BotController bot = new BotController();

        // Build white chain
        for (int x = 0; x < 10; x++) {

            while (board.getTurn() != Turn.Player2) {
                board.switchTurn();
            }
            QuaxGame.placeStone(board, x, 5);

            while (board.getTurn() != Turn.Player1) {
                board.switchTurn();
            }
            QuaxGame.placeStone(board, x, 0);
        }

        //force bot to be BLACK
        while (board.getTurn() != Turn.Player1) {
            board.switchTurn();
        }

        // Bot should block at (10,5)
        Move move = bot.chooseMove(board);

        assertNotNull(move);
        assertEquals(10, move.getX());
        assertEquals(5, move.getY());
    }



}