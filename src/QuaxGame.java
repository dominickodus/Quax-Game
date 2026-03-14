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

        boolean ok = board.placeStoneAt(x, y);

        boolean winningMove = false;

        if (board.getTurnsPassed()  >= 11 && ok){
            winningMove = checkWin(board, x , y);
        }

        if(winningMove){
            System.out.println("winner"); //just for now for testing
        }
        return ok ;
    }

    // Places a rhombus stone at (x,y)
    public static boolean placeRhombus(QuaxBoard board, int x, int y) {
        return board.placeRhombusAt(x, y);
    }

    // DOES NOT INCLUDE RHOMBI LOGIC YET
    public static boolean checkWin(QuaxBoard board, int x, int y) {
        Colour colour = board.getStone(x, y);

        boolean[][] visited = new boolean[11][11]; //keeping track of what cells have been visited in the recursion

        boolean[] boardEdges = new boolean[2];  // if both these booleans are ture, that means both edges have been touched hence a valid chain is made

        return depthFirstSearch(board, x,y,colour, visited, boardEdges); //recursive
    }

    // DOES NOT INCLUDE RHOMBI LOGIC YET
    public static boolean depthFirstSearch(QuaxBoard board, int x, int y, Colour colour, boolean[][] visited, boolean[] boardEdges){

        if(visited[x][y]) return false; //base case, cell already explored

        visited[x][y] = true; //mark

        // top to bottom edges
        // 0, and 10 and the limits in our grid
        if (colour == Colour.BLACK) {
            if (y == 0) boardEdges[0] = true;        // top
            if (y == 10) boardEdges[1] = true;    // bottom
        }

        //left to right
        if (colour == Colour.WHITE) {
            if (x == 0) boardEdges[0] = true;        // left
            if (x == 10) boardEdges[1] = true;    // right
        }

        if(boardEdges[0] && boardEdges[1]) return true; // base (win) case, both edges touched

        //now check all (octagon only currently) neighbours

        // Right neighbour
        if (board.inBoundsStone(x + 1, y) && board.getStone(x + 1, y) == colour) {
            if (depthFirstSearch(board, x + 1, y, colour, visited, boardEdges))
                return true;
        }

        // left neighbour
        if (board.inBoundsStone(x - 1, y) && board.getStone(x - 1, y) == colour) {
            if (depthFirstSearch(board, x - 1, y, colour, visited, boardEdges))
                return true;
        }

        // up neighbour
        if (board.inBoundsStone(x, y + 1) && board.getStone(x, y + 1) == colour) {
            if (depthFirstSearch(board, x, y + 1, colour, visited, boardEdges))
                return true;
        }

        // down neighbour
        if (board.inBoundsStone(x, y - 1) && board.getStone(x, y - 1) == colour) {
            if (depthFirstSearch(board, x, y - 1, colour, visited, boardEdges))
                return true;
        }

        return false; //return false if never found a win
    }

    // Placeholder for pie rule logic
    public static void applyPieRule(QuaxBoard board) {
        board.setPieRuleEnabled();
        board.switchTurn();
        board.switchTurn(); //twice as after pie rule, turn should still be white
        board.addTurnsPassed(-1);  // take away 1 from turn count as pie rule is only one turn
    }

}