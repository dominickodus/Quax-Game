import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class QuaxBoardTest {

    //Each Test is mapped to its related SR

    //SR 2.1
    @Test
    void test_SR2_initialTurnIsPlayer1() {
        QuaxBoard board = new QuaxBoard(null);
        assertEquals(Turn.Player1, board.getTurn());
    }

    //SR 2.1 + 2.2
    @Test
    void test_SR2_switchTurnToggles() {
        QuaxBoard board = new QuaxBoard(null);

        board.switchTurn();
        assertEquals(Turn.Player2, board.getTurn());

        board.switchTurn();
        assertEquals(Turn.Player1, board.getTurn());
    }

    //SR 2.1 + 2.2 consistency
    @Test
    void test_SR2_multipleTurnSwitches() {
        QuaxBoard board = new QuaxBoard(null);

        for (int i = 0; i < 10; i++) {
            board.switchTurn();
        }
        assertEquals(Turn.Player1, board.getTurn());
    }
}
