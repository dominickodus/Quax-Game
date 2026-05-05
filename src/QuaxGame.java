/**
 *  This acts as the rules layer for the game
 *  QuaxGame acts between the UI (BoardFx) and the board state (QuaxBoard)>
 *  At the moment it mainly forwards move requests to QuaxBoard, but will contain
 *  higher level game logic such as win detection etc.
 */

public class QuaxGame {
    public static boolean placeStone(QuaxBoard board, int x, int y) {

        boolean ok = board.placeStoneAt(x, y);

        if (board.getTurnsPassed()  >= 11 && ok){
            checkWin(board, x, y);
        }

        return ok ;
    }

    public static boolean placeRhombus(QuaxBoard board, int x, int y) {

        boolean ok = board.placeRhombusAt(x, y);
        if (!ok) return false;

        boolean winningMove = false;
        Colour colour = board.getRhombus(x, y);

        if (board.getTurnsPassed() >= 11) {

            // Check both diagonals around this rhombus

            if (board.getStone(x, y) == colour) {
                winningMove = checkWin(board, x, y);
            }

            if (!winningMove && board.getStone(x + 1, y + 1) == colour) {
                winningMove = checkWin(board, x + 1, y + 1);
            }

            if (!winningMove && board.getStone(x + 1, y) == colour) {
                winningMove = checkWin(board, x + 1, y);
            }
            if (!winningMove && board.getStone(x, y + 1) == colour) {
                checkWin(board, x, y + 1);
            }
        }

        return true;
    }


    public static boolean checkWin(QuaxBoard board, int x, int y) {
        Colour colour = board.getStone(x, y);

        boolean[][] visited = new boolean[11][11]; //keeping track of what cells have been visited in the recursion

        boolean[] boardEdges = new boolean[2];  // if both these booleans are ture, that means both edges have been touched hence a valid chain is made

        boolean winnerExists = depthFirstSearch(board, x,y,colour, visited, boardEdges); //recursive

        if (winnerExists){
            board.setWinner();
        }

        return winnerExists;
    }

    public static boolean depthFirstSearch(QuaxBoard board, int x, int y, Colour colour, boolean[][] visited, boolean[] boardEdges){

        if(visited[x][y]) return false; //base case, cell already explored

        visited[x][y] = true;

        // top to bottom edges
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

        //Direct (non-diagonal) neighbours

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

        // ---- Diagonal neighbours through rhombi ----

        // Down-right diagonal
        if (board.inBoundsStone(x + 1, y + 1)
                && board.getStone(x + 1, y + 1) == colour
                && board.inBoundsRhombus(x, y)
                && board.getRhombus(x, y) == colour) {
            if (depthFirstSearch(board, x + 1, y + 1, colour, visited, boardEdges))
                return true;
        }

        // Up-left diagonal
        if (board.inBoundsStone(x - 1, y - 1)
                && board.getStone(x - 1, y - 1) == colour
                && board.inBoundsRhombus(x - 1, y - 1)
                && board.getRhombus(x - 1, y - 1) == colour) {
            if (depthFirstSearch(board, x - 1, y - 1, colour, visited, boardEdges))
                return true;
        }

        // Up-right diagonal
        if (board.inBoundsStone(x + 1, y - 1)
                && board.getStone(x + 1, y - 1) == colour
                && board.inBoundsRhombus(x, y - 1)
                && board.getRhombus(x, y - 1) == colour) {
            if (depthFirstSearch(board, x + 1, y - 1, colour, visited, boardEdges))
                return true;
        }

        // Down-left diagonal
        if (board.inBoundsStone(x - 1, y + 1)
                && board.getStone(x - 1, y + 1) == colour
                && board.inBoundsRhombus(x - 1, y)
                && board.getRhombus(x - 1, y) == colour) {
            return (depthFirstSearch(board, x - 1, y + 1, colour, visited, boardEdges));
        }

        return false; //return false if never found a win
    }


    public static void applyPieRule(QuaxBoard board) {
        board.setPieRuleEnabled();
        board.switchTurn();
        board.switchTurn(); //twice as after pie rule, turn should still be white
        board.addTurnsPassed(-1);  // take away 1 from turn count as pie rule is only one turn
    }

}