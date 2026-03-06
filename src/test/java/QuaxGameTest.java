import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuaxGameTest {
    //Each Test is mapped to its related SR

    //SR2
    @Test
    void test_SR2placeStoneSwitchesTurn() {
        QuaxBoard board = new QuaxBoard(null);

        assertEquals(Turn.Player1, board.getTurn());

        QuaxGame.placeStone(board, 0, 0);

        assertEquals(Turn.Player2, board.getTurn());
    }

    //SR2
    @Test
    void test_SR2_PlaceStoneAlternateTurns() {
        QuaxBoard board = new QuaxBoard(null);

        QuaxGame.placeStone(board, 0, 0);
        QuaxGame.placeStone(board, 0, 1);
        QuaxGame.placeStone(board, 0, 2);

        assertEquals(Turn.Player2, board.getTurn());
    }

    // SR5/6
    @Test
    void test_SR5_InvalidPlacementRejected() {
        QuaxBoard board = new QuaxBoard(null);

        QuaxGame.placeStone(board, 0, 0); // Now player 2's turn
        QuaxGame.placeStone(board, 0, 0); // Invalid move, should still be player 2's turn

        assertEquals(Turn.Player2, board.getTurn());
    }

    // SR 5/6
    // Tests rhombic stones with upper left / lower right stones
    @Test
    void test_SR5_RhombicStonePlacement1() {
        QuaxBoard board = new QuaxBoard(null);

        QuaxGame.placeStone(board, 0, 0); // Player 1
        QuaxGame.placeStone(board, 10, 10); // Player 2
        QuaxGame.placeStone(board, 1, 1); //  Player 1, should now be able to place rhombus
        QuaxGame.placeStone(board, 9, 9); // Player 2
        QuaxGame.placeRhombus(board, 0, 0); // Player 1

        assertEquals(Turn.Player2, board.getTurn());
    }

    // SR 5/6
    // Tests rhombic stones with upper right / lower left stones
    @Test
    void RhombicStonePlacement2() {
        QuaxBoard board = new QuaxBoard(null);

        QuaxGame.placeStone(board, 1, 0); // Player 1
        QuaxGame.placeStone(board, 10, 10); // Player 2
        QuaxGame.placeStone(board, 0, 1); //  Player 1, should now be able to place rhombus
        QuaxGame.placeStone(board, 9, 9); // Player 2
        QuaxGame.placeRhombus(board, 0, 0); // Player 1

        assertEquals(Turn.Player2, board.getTurn());
    }

    // SR 5/6
    // Assert rhombuses must be placed between octagonal stones
    @Test
    void test_SR5_InvalidRhombicStonePlacement() {
        QuaxBoard board = new QuaxBoard(null);
        QuaxGame.placeRhombus(board, 0, 0); // Invalid, should be player 1's turn still
        assertEquals(Turn.Player1, board.getTurn());
    }

    // SR 5/6
    // Assert rhombus cannot be placed on an occupied rhombus
    @Test
    void test_SR5_RhombicStoneValidation() {
        QuaxBoard board = new QuaxBoard(null);

        QuaxGame.placeStone(board, 0, 0); // Player 1
        QuaxGame.placeStone(board, 1, 0); // Player 2
        QuaxGame.placeStone(board, 1, 1); // Player 1, should now be able to place rhombus
        QuaxGame.placeStone(board, 0, 1); // Player 2, should now be able to place rhombus
        QuaxGame.placeRhombus(board, 0, 0); // Player 1
        QuaxGame.placeRhombus(board, 0, 0); // Player 2, invalid so it should be player 2's move still

        assertEquals(Turn.Player2, board.getTurn());
    }
}
