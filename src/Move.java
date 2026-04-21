public class Move {
    private int x;
    private int y;
    private boolean rhombus;

    public Move(int x, int y, boolean rhombus) {
        this.x = x;
        this.y = y;
        this.rhombus = rhombus;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isRhombus() {
        return rhombus;
    }
}