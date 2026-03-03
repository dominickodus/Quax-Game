import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class QuaxGameTest {
    //Each Test is mapped to its related SR

    //SR2
    @Test
    void placeStoneSwitchesTurn() {
        QuaxBoard board = new QuaxBoard(null);

        assertEquals(Turn.Player1, board.getTurn());

        QuaxGame.placeStone(board);

        assertEquals(Turn.Player2, board.getTurn());
    }


    //SR2
    @Test
    void PlaceStoneAlternateTurns() {
        QuaxBoard board = new QuaxBoard(null);

        QuaxGame.placeStone(board);
        QuaxGame.placeStone(board);
        QuaxGame.placeStone(board);

        assertEquals(Turn.Player2, board.getTurn());
    }

}
