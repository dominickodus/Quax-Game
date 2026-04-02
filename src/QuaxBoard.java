
/**
 * Model class representing the state of a quax board.
 * It stores the position of all tiles and provides methods for validating and applying moves.
 * Game logic such as win detection will use these arrays to determine connectivity.
 */

public class QuaxBoard {
    private static final int N = 11; // Board size: 11x11 octagonal stone cells

     private boolean pieRuleEnabled; // Whether the pie rule is enable (To be implemented)
     private Turn turn;
     private int turnsPassed;
     private boolean winnerExists;

     // Stores stones placed on the 11x11 grid of octagonal cells
     private final Colour[][] stones = new Colour[N][N];
     // Stores rhombus tiles placed between stones.
     // A rhombus at (x,y) lies between 4 octagons (x,y), (x+1,y), (x,y+1), (x+1,y+1)
     private final Colour[][] rhombi = new Colour[N-1][N-1];

    public QuaxBoard(Theme boardTheme) {
        pieRuleEnabled = false;
        winnerExists = false;
        turn = Turn.Player1;

        // init all board locations to empty
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) stones[x][y] = Colour.NULL;
        }
        for (int x = 0; x < N-1; x++) {
            for (int y = 0; y < N-1; y++) {
                rhombi[x][y] = Colour.NULL; //
            }
        }
    }

    public boolean doesWinnerExist(){
        return winnerExists;
    }

    public void setWinner(){
        winnerExists = true;
    }



    public Turn getTurn() { return turn; }

    public int getTurnsPassed(){return turnsPassed;}

    public void addTurnsPassed(int x){
        turnsPassed += x;
    }


    // Switches to other players turn after a successful move.
    public void switchTurn() {

        turn = (turn == Turn.Player1) ? Turn.Player2 : Turn.Player1;
        turnsPassed ++;
    }

    // Maps the current turn to a colour
    public Colour currentPlayerColour() {
        return (turn == Turn.Player1) ? Colour.BLACK : Colour.WHITE;
    }

    // Checks if (x,y) is a valid octagon coordinate
    public boolean inBoundsStone(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }

    public Colour getStone(int x, int y) {
        if (!inBoundsStone(x,y)) return null;
        return stones[x][y];
    }

    // Attempts to place a octagon stone at (x,y)
    // Only valid if cell is in board and unoccupied
    public boolean placeStoneAt(int x, int y) {
        if (!inBoundsStone(x,y)) return false;
        if (stones[x][y] != Colour.NULL) return false;

        stones[x][y] = currentPlayerColour();
        switchTurn();
        return true;
    }

    // Checks if (x,y) is a valid rhombus coordinate
    public boolean inBoundsRhombus(int x, int y) {
        return x >= 0 && x < N-1 && y >= 0 && y < N-1;
    }

    public Colour getRhombus(int x, int y) {
        if (!inBoundsRhombus(x,y)) return null;
        return rhombi[x][y];
    }

    // Attempts to place a rhombus tile at (x,y)
    // Only places if:
    //  - the rhombus cell is empty, and
    //  - the current player has at least one diagonal pair of stones surrounding it
    public boolean placeRhombusAt(int x, int y) {
        if (!inBoundsRhombus(x,y)) return false;
        if (rhombi[x][y] != Colour.NULL) return false;

        Colour c = currentPlayerColour();

        // Unoriented rule: valid if either diagonal pair exists
        boolean diag1 = stones[x][y] == c && stones[x+1][y+1] == c;   // "\"
        boolean diag2 = stones[x+1][y] == c && stones[x][y+1] == c;   // "/"

        if (!diag1 && !diag2) return false;

        rhombi[x][y] = c;
        switchTurn();
        return true;
    }


    public void setPieRuleEnabled() {
            this.pieRuleEnabled = true;
    }

    public boolean isPieRuleEnabled(){
        return pieRuleEnabled;
    }
}
