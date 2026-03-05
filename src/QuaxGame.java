/**
 *  This acts as the rules layer for the game
 *
 *  QuaxGame acts between the UI (BoardFx) and the board state (QuaxBoard)>
 *  At the moment it mainly forwrads move requests to QuaxBoard, but will contain
 *  higher level game logic such as win detection etc.
 */

public class QuaxGame {

    // Places an octagon stone at (x,y)
    public static boolean placeStone(QuaxBoard board, int x, int y) {
        return board.placeStoneAt(x, y);
    }

    // Places a rombus stone at (x,y)
    public static boolean placeRhombus(QuaxBoard board, int x, int y) {
        return board.placeRhombusAt(x, y);
    }

    // Placeholder for win detection
    public static boolean checkWin(QuaxBoard board, Colour player) {
        return false;
    }

    // Placeholder for pie rule logic
    public static boolean applyPieRule(QuaxBoard board) {
        return false;
    }

}