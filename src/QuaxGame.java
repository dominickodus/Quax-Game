
public class QuaxGame {
    // Instance variables
    int turnNum = 1;
    Controller player1Controller;
    Controller player2Controller;
    QuaxBoard board;


    // Constructor
    public QuaxGame(Controller player1Controller, Controller player2Controller, QuaxBoard board) {
        this.player1Controller = player1Controller;
        this.player2Controller = player2Controller;
        this.board = board;
    }

    // Methods
    public void start() {

    }

    public static boolean placeStone(QuaxBoard board, Colour colour){

        /*
        WIN CHECKS HERE, LATER
         */

        if(colour != Colour.NULL){ //return if position occupied
            return false;
        }

        board.switchTurn();
        return true;
    }
}
