import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlacementTests {

    QuaxBoard board;

    @BeforeEach
    void setUp(){
        board = new QuaxBoard(Theme.STANDARD);
    }

    @Test
    void testTurnAfterPlacement(){
        QuaxGame.placeStone(board);
        Assertions.assertEquals(Turn.Player2, board.getTurn());
    }
}
