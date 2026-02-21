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
    //Test turn goes from White to Black
    void testTurnAfterPlacement(){
        QuaxGame.placeStone(board);
        Assertions.assertEquals(Turn.Player2, board.getTurn());
    }

    @Test
    //Test turn goes back to White
    void testTurnAlternates() {
        Assertions.assertEquals(Turn.Player1, board.getTurn());

        QuaxGame.placeStone(board);
        Assertions.assertEquals(Turn.Player2, board.getTurn());

        QuaxGame.placeStone(board);
        Assertions.assertEquals(Turn.Player1, board.getTurn());
    }
}
