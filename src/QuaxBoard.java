import java.util.ArrayList;

public class QuaxBoard {
    // Instance variables
    ArrayList<Tile> tiles;
     private boolean pieRuleEnabled;
     private Turn turn;

    // Constructor
    public QuaxBoard(Theme boardTheme) {
        pieRuleEnabled = true;
        turn = Turn.Player1;
    }

    // Methods
    public void initialise() {

    }

    public boolean didPlayer1Win() {
        return false;
    }

    public boolean didPlayer2Win() {
        return false;
    }

    public ArrayList<Tile> getPlayer1Tiles() {
        return null;
    }

    public ArrayList<Tile> getPlayer2Tiles() {
        return null;
    }

    public Turn getTurn(){return turn;}

    public void switchTurn() {
        turn = (turn == Turn.Player1) ? Turn.Player2 : Turn.Player1;
    }
    public void setPieRuleEnabled(boolean enabled) {

    }
}
