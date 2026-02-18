import java.util.ArrayList;

public class QuaxBoard {
    // Instance variables
    ArrayList<Tile> tiles;
    boolean pieRuleEnabled;

    // Constructor
    public QuaxBoard(Theme boardTheme) {
        pieRuleEnabled = true;
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

    public void setPieRuleEnabled(boolean enabled) {

    }
}
