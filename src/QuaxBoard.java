import java.util.ArrayList;

public class QuaxBoard {
    // Instance variables
    ArrayList<Tile> tiles;
    boolean pieRuleEnabled;
    Turn turn;

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
    public void setPieRuleEnabled(boolean enabled) {

    }
}
