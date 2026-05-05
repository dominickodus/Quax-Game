import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuaxGameTest {

    @Test
    void test_SR2placeStoneSwitchesTurn() {
        QuaxBoard board = new QuaxBoard();

        assertEquals(Turn.Player1, board.getTurn());

        QuaxGame.placeStone(board, 0, 0);

        assertEquals(Turn.Player2, board.getTurn());
    }


    @Test
    void test_SR2_PlaceStoneAlternateTurns() {
        QuaxBoard board = new QuaxBoard();

        QuaxGame.placeStone(board, 0, 0);
        QuaxGame.placeStone(board, 0, 1);
        QuaxGame.placeStone(board, 0, 2);

        assertEquals(Turn.Player2, board.getTurn());
    }


    @Test
    void test_SR5_InvalidPlacementRejected() {
        QuaxBoard board = new QuaxBoard();
        QuaxGame.placeStone(board, 0, 0);
        QuaxGame.placeStone(board, 0, 0);

        assertEquals(Turn.Player2, board.getTurn());
    }

    @Test
    void test_SR5_RhombicStonePlacement1() {
        QuaxBoard board = new QuaxBoard();

        QuaxGame.placeStone(board, 0, 0);
        QuaxGame.placeStone(board, 10, 10);
        QuaxGame.placeStone(board, 1, 1);
        QuaxGame.placeStone(board, 9, 9);
        QuaxGame.placeRhombus(board, 0, 0);

        assertEquals(Turn.Player2, board.getTurn());
    }
    @Test
    void RhombicStonePlacement2() {
        QuaxBoard board = new QuaxBoard();

        QuaxGame.placeStone(board, 1, 0);
        QuaxGame.placeStone(board, 10, 10);
        QuaxGame.placeStone(board, 0, 1);
        QuaxGame.placeStone(board, 9, 9);
        QuaxGame.placeRhombus(board, 0, 0);

        assertEquals(Turn.Player2, board.getTurn());
    }

    // Assert rhombuses must be placed between octagonal stones
    @Test
    void test_SR5_InvalidRhombicStonePlacement() {
        QuaxBoard board = new QuaxBoard();
        QuaxGame.placeRhombus(board, 0, 0);
        assertEquals(Turn.Player1, board.getTurn());
    }

    @Test
    void test_SR5_RhombicStoneValidation() {
        QuaxBoard board = new QuaxBoard();
        QuaxGame.placeStone(board, 0, 0);
        QuaxGame.placeStone(board, 1, 0);
        QuaxGame.placeStone(board, 1, 1);
        QuaxGame.placeStone(board, 0, 1);
        QuaxGame.placeRhombus(board, 0, 0);
        QuaxGame.placeRhombus(board, 0, 0);

        assertEquals(Turn.Player2, board.getTurn());
    }

    @Test
    void test_SR3_pieRuleEnabled(){
        QuaxBoard board = new QuaxBoard();
        assertFalse(board.isPieRuleEnabled());
        QuaxGame.applyPieRule(board);
        assertTrue(board.isPieRuleEnabled());
    }

    @Test
    void test_SR3_whiteAfterPie(){
        QuaxBoard board = new QuaxBoard();
        board.switchTurn();
        assertEquals(board.getTurn(), Turn.Player2);
        QuaxGame.applyPieRule(board);
        assertEquals(board.getTurn(), Turn.Player2);
    }

    @Test
    void test_SR3_turnCountAfterPie(){
        QuaxBoard board = new QuaxBoard();
        board.switchTurn();
        assertEquals(board.getTurnsPassed(), 1);
        QuaxGame.applyPieRule(board);
        assertEquals(board.getTurnsPassed(), 2);
    }

    @Test
    void test_SR6_blackWin() {
        QuaxBoard board = new QuaxBoard();
        for (int i = 0; i < 11; i++) {
            QuaxGame.placeStone(board, 0, i);
            QuaxGame.placeStone(board, 1, i);
        }
        assert(board.doesWinnerExist());
    }

    @Test
    void test_SR7_whiteWin() {
        QuaxBoard board = new QuaxBoard();
        for (int i = 0; i < 11; i++) {
            QuaxGame.placeStone(board, i, 0);
            QuaxGame.placeStone(board, i, 1);
        }
        assert(board.doesWinnerExist());
    }

    @Test
    void test_SR6_falseWin() {
        QuaxBoard board = new QuaxBoard();
        board.placeStoneAt(0, 0);
        assert(!board.doesWinnerExist());
    }

    @Test
    void test_SR6_blackWinWithRhombus() {
        QuaxBoard board = new QuaxBoard();
        QuaxGame.placeStone(board, 0, 0);
        QuaxGame.placeStone(board, 10, 0);
        QuaxGame.placeStone(board, 1, 1);
        QuaxGame.placeStone(board, 10, 1);
        QuaxGame.placeRhombus(board, 0, 0);
        QuaxGame.placeStone(board, 10, 2);
        for (int i = 2; i < 11; i++) {
            QuaxGame.placeStone(board, 1, i);
            QuaxGame.placeStone(board, 9, i);
        }
        assert(board.doesWinnerExist());
    }

    @Test
    void test_SR7_whiteWinWithRhombus() {
        QuaxBoard board = new QuaxBoard();
        QuaxGame.placeStone(board, 10, 0);
        QuaxGame.placeStone(board, 0, 0);
        QuaxGame.placeStone(board, 10, 1);
        QuaxGame.placeStone(board, 1, 1);
        QuaxGame.placeStone(board, 10, 2);
        QuaxGame.placeRhombus(board, 0, 0);
        for (int i = 2; i < 11; i++) {
            QuaxGame.placeStone(board, 9, i);
            QuaxGame.placeStone(board, 1, i);
        }
        assert(board.doesWinnerExist());
    }
}
