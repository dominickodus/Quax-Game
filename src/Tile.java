import java.util.ArrayList;

public class Tile {
    // Instance variables
    TileShape shape;
    ArrayList<Tile> neighbours;
    int positionX;
    int positionY;
    int owner = 0;

    // Constructor
    public Tile(TileShape shape, int positionX, int positionY) {
        this.shape = shape;
        this.positionX = positionX;
        this.positionY = positionY;
        neighbours = new ArrayList<>();
    }

    // Methods
    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getOwner() {
        return owner;
    }

    public ArrayList<Tile> getNeighbours() {
        return neighbours;
    }
}
