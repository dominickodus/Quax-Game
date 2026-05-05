import java.util.*;

public class BotController {

    private static final int INF = 1_000_000;

    private String lastStrategyUsed = "";
    private String lastExplanation = "";
    private final HashMap<Move, Integer> moveScores = new HashMap<>();
    private Move lastMove;

    public boolean isBot() {
        return true;
    }

    public Move chooseMove(QuaxBoard board) {

        if (board.getTurnsPassed() == 0) {
            List<Move> centralMoves = new ArrayList<>();

            int[][] preferred = {
                    {5,5}, {5,4}, {5,6}, {4,5}, {6,5}
            };

            for (int[] p : preferred) {
                if (board.getStone(p[0], p[1]) == Colour.NULL) {
                    centralMoves.add(new Move(p[0], p[1], false));
                }
            }

            if (!centralMoves.isEmpty()) {
                Move randomMove = centralMoves.get((int)(Math.random() * centralMoves.size()));

                lastStrategyUsed = "Opening Strategy";
                lastExplanation = "Bot selected a random central opening move.";
                return randomMove;
            }
        }


        List<Move> validMoves = getAllValidMoves(board);
        if (validMoves.isEmpty()) return null;

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        moveScores.clear();

        for (Move move : validMoves) {
            int score = scoreMove(board, move);
            moveScores.put(move, score);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        setMoveExplanation(board, bestMove);
        return bestMove;
    }

    //Decides what Strategy to Display
    private void setMoveExplanation(QuaxBoard board, Move move) {
        if (move == null) {
            lastStrategyUsed = "No Move";
            lastExplanation = "Bot found no legal move.";
            return;
        }

        Colour me = board.currentPlayerColour();
        Colour opponent = (me == Colour.BLACK) ? Colour.WHITE : Colour.BLACK;

        if (isWinningMove(board, move)) {
            lastStrategyUsed = "Winning Strategy";
            lastExplanation = "Bot chose this move because it wins immediately.";
            return;
        }

        if (blocksOpponentImmediateWin(board, move)) {
            lastStrategyUsed = "Blocking Strategy";
            lastExplanation = "Bot chose this move because it blocks the opponent's immediate winning move.";
            return;
        }

        if (move.isRhombus()) {
            int x = move.getX();
            int y = move.getY();

            boolean connectsMine = false;
            boolean blocksOpponent = false;

            if (board.inBoundsStone(x, y) && board.inBoundsStone(x + 1, y + 1)) {
                if (board.getStone(x, y) == me && board.getStone(x + 1, y + 1) == me) {
                    connectsMine = true;
                }
                if (board.getStone(x, y) == opponent && board.getStone(x + 1, y + 1) == opponent) {
                    blocksOpponent = true;
                }
            }

            if (board.inBoundsStone(x + 1, y) && board.inBoundsStone(x, y + 1)) {
                if (board.getStone(x + 1, y) == me && board.getStone(x, y + 1) == me) {
                    connectsMine = true;
                }
                if (board.getStone(x + 1, y) == opponent && board.getStone(x, y + 1) == opponent) {
                    blocksOpponent = true;
                }
            }

            if (connectsMine && blocksOpponent) {
                lastStrategyUsed = "Rhombus Dual Strategy";
                lastExplanation = "Bot chose this rhombus as there was a blocking threat on its own diagonal";
            } else if (connectsMine) {
                lastStrategyUsed = "Rhombus Connection Strategy";
                lastExplanation = "Bot chose this rhombus because it strongly connects its diagonal stones.";
            } else if (blocksOpponent) {
                lastStrategyUsed = "Rhombus Blocking Strategy";
                lastExplanation = "Bot chose this rhombus because it interferes with the opponent's diagonal connection.";
            } else {
                lastStrategyUsed = "Rhombus Strategy";
                lastExplanation = "Bot chose this rhombus because it improves board connectivity.";
            }
            return;
        }

        int friendlyNeighbours = countFriendlyNeighbours(board, move.getX(), move.getY(), me);
        int opponentNeighbours = countFriendlyNeighbours(board, move.getX(), move.getY(), opponent);

        int maxContiguous = getMaxContiguous(move, board, opponent);

        if (maxContiguous >= 3) {
            lastStrategyUsed = "Defensive Strategy";
            lastExplanation = "Bot chose this move to break a strong opponent line.";
            return;
        }

        if (createsRhombusOpportunity(board, move.getX(), move.getY(), me)) {
            lastStrategyUsed = "Setup Strategy";
            lastExplanation = "Bot chose this move to create a future rhombus opportunity.";
            return;
        }

        if (friendlyNeighbours >= 2) {
            lastStrategyUsed = "Connection Strategy";
            lastExplanation = "Bot chose this move to strengthen its existing chain of stones.";
            return;
        }

        if (opponentNeighbours >= 2) {
            lastStrategyUsed = "Pressure Strategy";
            lastExplanation = "Bot chose this move to apply pressure near the opponent's stones.";
            return;
        }

        lastStrategyUsed = "Pathfinding Strategy";
        lastExplanation = "Bot chose the move that best improved its path across the board.";
    }

    private int getMaxContiguous(Move move, QuaxBoard board, Colour opponent) {
        int vCount = countContinuousStones(board, move.getX(), move.getY(), 0, 1, opponent)
                + countContinuousStones(board, move.getX(), move.getY(), 0, -1, opponent);
        int hCount = countContinuousStones(board, move.getX(), move.getY(), 1, 0, opponent)
                + countContinuousStones(board, move.getX(), move.getY(), -1, 0, opponent);

        if (opponent == Colour.BLACK) {
            return vCount;
        } else {
            return hCount;
        }
    }

    private boolean wasPlaced(Move move, QuaxBoard copy) {
        if (move.isRhombus()) {
            return QuaxGame.placeRhombus(copy, move.getX(), move.getY());
        } else {
            return QuaxGame.placeStone(copy, move.getX(), move.getY());
        }
    }

    public void makeMove(QuaxBoard board) {
        Move move = chooseMove(board);
        lastMove = move;

        if (move == null) {
            return;
        }

        if (move.isRhombus()) {
            QuaxGame.placeRhombus(board, move.getX(), move.getY());
        } else {
            QuaxGame.placeStone(board, move.getX(), move.getY());
        }
    }

    private List<Move> getAllValidMoves(QuaxBoard board) {
        List<Move> moves = new ArrayList<>();

        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                if (isValidStoneMove(board, x, y)) {
                    moves.add(new Move(x, y, false));
                }
            }
        }

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (isValidRhombusMove(board, x, y)) {
                    moves.add(new Move(x, y, true));
                }
            }
        }

        return moves;
    }

    private boolean isValidStoneMove(QuaxBoard board, int x, int y) {
        return board.inBoundsStone(x, y) && board.getStone(x, y) == Colour.NULL;
    }

    private boolean isValidRhombusMove(QuaxBoard board, int x, int y) {
        if (!board.inBoundsRhombus(x, y)) return false;
        if (board.getRhombus(x, y) != Colour.NULL) return false;

        Colour c = board.currentPlayerColour();

        boolean diag1 = board.getStone(x, y) == c && board.getStone(x + 1, y + 1) == c;
        boolean diag2 = board.getStone(x + 1, y) == c && board.getStone(x, y + 1) == c;

        return diag1 || diag2;
    }

    private boolean isWinningMove(QuaxBoard board, Move move) {
        QuaxBoard copy = new QuaxBoard(board);

        boolean placed = wasPlaced(move, copy);
        if (!placed) return false;

        if (!move.isRhombus()) {
            return QuaxGame.checkWin(copy, move.getX(), move.getY());
        }

        Colour colour = copy.getRhombus(move.getX(), move.getY());
        int x = move.getX();
        int y = move.getY();

        if (copy.getStone(x, y) == colour && QuaxGame.checkWin(copy, x, y)) return true;
        if (copy.getStone(x + 1, y + 1) == colour && QuaxGame.checkWin(copy, x + 1, y + 1)) return true;
        if (copy.getStone(x + 1, y) == colour && QuaxGame.checkWin(copy, x + 1, y)) return true;
        return (copy.getStone(x, y + 1) == colour && QuaxGame.checkWin(copy, x, y + 1));
    }

    private boolean blocksOpponentImmediateWin(QuaxBoard board, Move myMove) {
        QuaxBoard opponentBoardBefore = new QuaxBoard(board);
        opponentBoardBefore.switchTurn();

        List<Move> opponentMovesBefore = getAllValidMoves(opponentBoardBefore);
        List<Move> opponentWinningMoves = new ArrayList<>();

        for (Move oppMove : opponentMovesBefore) {
            if (isWinningMove(opponentBoardBefore, oppMove)) {
                opponentWinningMoves.add(oppMove);
            }
        }

        if (opponentWinningMoves.isEmpty()) return false;

        QuaxBoard afterMyMove = new QuaxBoard(board);
        boolean placed = wasPlaced(myMove, afterMyMove);
        if (!placed) return false;

        List<Move> opponentMovesAfter = getAllValidMoves(afterMyMove);

        for (Move oldWinningMove : opponentWinningMoves) {
            boolean stillWinning = false;

            for (Move newOppMove : opponentMovesAfter) {
                if (sameMove(oldWinningMove, newOppMove) && isWinningMove(afterMyMove, newOppMove)) {
                    stillWinning = true;
                    break;
                }
            }

            if (!stillWinning) {
                return true;
            }
        }

        return false;
    }

    //helper that counts continuous lines of stones instead of just neighbours
    private int countContinuousStones(QuaxBoard board, int x, int y, int dx, int dy, Colour targetColour) {
        int count = 0;
        int nx = x + dx;
        int ny = y + dy;

        // Keep walking in the direction as long as we find the target colour
        while (board.inBoundsStone(nx, ny) && board.getStone(nx, ny) == targetColour) {
            count++;
            nx += dx;
            ny += dy;
        }
        return count;
    }

    private boolean sameMove(Move a, Move b) {
        return a.getX() == b.getX()
                && a.getY() == b.getY()
                && a.isRhombus() == b.isRhombus();
    }


    // Scores a move based on strategic impact:
    // Prioritises winning moves and blocking opponent wins
    // Uses pathfinding to minimise our path cost and increase opponent's
    // Adds bonuses for connections, pressure, and rhombus opportunities

    private int scoreMove(QuaxBoard board, Move move) {
        Colour me = board.currentPlayerColour();
        Colour opponent = (me == Colour.BLACK) ? Colour.WHITE : Colour.BLACK;

        if (isWinningMove(board, move)) return 1_000_000;
        if (blocksOpponentImmediateWin(board, move)) return 800_000;

        int myBefore = shortestPathCost(board, me);
        int oppBefore = shortestPathCost(board, opponent);

        QuaxBoard copy = new QuaxBoard(board);
        boolean placed;
        if (move.isRhombus()) {
            placed = QuaxGame.placeRhombus(copy, move.getX(), move.getY());
        } else {
            placed = QuaxGame.placeStone(copy, move.getX(), move.getY());
        }

        if (!placed) return Integer.MIN_VALUE;

        int myAfter = shortestPathCost(copy, me);
        int oppAfter = shortestPathCost(copy, opponent);

        int score = 0;

        // Most important heuristic:
        // Improve the bot path to victory while worsening the human's path
        score += 10000 * (myBefore - myAfter);
        score += 10000 * (oppAfter - oppBefore);

        if (!move.isRhombus()) {
            int maxContiguous = getMaxContiguous(move, board, opponent);

            if (maxContiguous >= 3) {
                score += 720;
            }
        }


        // Small tie-breakers
        if (!move.isRhombus()) {
            int friendlyNeighbours = countFriendlyNeighbours(board, move.getX(), move.getY(), me);
            int opponentNeighbours = countFriendlyNeighbours(board, move.getX(), move.getY(), opponent);

            score += 120 * friendlyNeighbours;
            score += 60 * opponentNeighbours;

            if (friendlyNeighbours == 0) {
                score -= 180;
            }

            if (createsRhombusOpportunity(board, move.getX(), move.getY(), me)) {
                score += 180;
            }
        } else {
            score += rhombusConnectionStrength(board, move.getX(), move.getY(), me);
        }

        return score;
    }

    private int countFriendlyNeighbours(QuaxBoard board, int x, int y, Colour colour) {
        int count = 0;
        int[][] dirs = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
        };

        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (board.inBoundsStone(nx, ny) && board.getStone(nx, ny) == colour) {
                count++;
            }
        }

        return count;
    }

    private boolean createsRhombusOpportunity(QuaxBoard board, int x, int y, Colour me) {
        if (board.inBoundsRhombus(x - 1, y - 1) && board.getStone(x - 1, y - 1) == me) return true;

        if (board.inBoundsRhombus(x, y)) {
            if (board.inBoundsStone(x + 1, y + 1) && board.getStone(x + 1, y + 1) == me) return true;
            if (board.inBoundsStone(x + 1, y) && board.getStone(x + 1, y) == me) return true;
            if (board.inBoundsStone(x, y + 1) && board.getStone(x, y + 1) == me) return true;
        }

        if (board.inBoundsRhombus(x, y - 1) && board.inBoundsStone(x + 1, y - 1) && board.getStone(x + 1, y - 1) == me) return true;
        return (board.inBoundsRhombus(x - 1, y) && board.inBoundsStone(x - 1, y + 1) && board.getStone(x - 1, y + 1) == me);
    }

    private int rhombusConnectionStrength(QuaxBoard board, int x, int y, Colour me) {
        int bonus = 0;
        Colour opponent = (me == Colour.BLACK) ? Colour.WHITE : Colour.BLACK;

        // Friendly diagonal: (x,y) - (x+1,y+1)
        if (board.inBoundsStone(x, y) && board.inBoundsStone(x + 1, y + 1)) {
            if (board.getStone(x, y) == me && board.getStone(x + 1, y + 1) == me) {
                bonus += 600;
            }
            if (board.getStone(x, y) == opponent && board.getStone(x + 1, y + 1) == opponent) {
                bonus += 500;
            }
        }

        // Friendly diagonal: (x+1,y) - (x,y+1)
        if (board.inBoundsStone(x + 1, y) && board.inBoundsStone(x, y + 1)) {
            if (board.getStone(x + 1, y) == me && board.getStone(x, y + 1) == me) {
                bonus += 600;
            }
            if (board.getStone(x + 1, y) == opponent && board.getStone(x, y + 1) == opponent) {
                bonus += 500;
            }
        }

        return bonus;
    }

    private int shortestPathCost(QuaxBoard board, Colour player) {
        int[][] dist = new int[11][11];
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                dist[x][y] = INF;
            }
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));

        if (player == Colour.WHITE) {
            for (int y = 0; y < 11; y++) {
                int cost = stoneCost(board, 0, y, player);
                if (cost < INF) {
                    dist[0][y] = cost;
                    pq.add(new Node(0, y, cost));
                }
            }
        } else {
            for (int x = 0; x < 11; x++) {
                int cost = stoneCost(board, x, 0, player);
                if (cost < INF) {
                    dist[x][0] = cost;
                    pq.add(new Node(x, 0, cost));
                }
            }
        }

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.cost != dist[cur.x][cur.y]) continue;

            if (player == Colour.WHITE && cur.x == 10) return cur.cost;
            if (player == Colour.BLACK && cur.y == 10) return cur.cost;

            for (int[] next : stoneNeighbours(board, cur.x, cur.y, player)) {
                int nx = next[0];
                int ny = next[1];
                int nd = cur.cost + stoneCost(board, nx, ny, player);

                if (nd < dist[nx][ny]) {
                    dist[nx][ny] = nd;
                    pq.add(new Node(nx, ny, nd));
                }
            }
        }

        return INF;
    }

    private List<int[]> stoneNeighbours(QuaxBoard board, int x, int y, Colour player) {
        List<int[]> neighbours = new ArrayList<>();

        int[][] orth = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        for (int[] d : orth) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (board.inBoundsStone(nx, ny)) {
                neighbours.add(new int[]{nx, ny});
            }
        }

        // Diagonal via rhombi
        if (board.inBoundsRhombus(x, y) && rhombusTraversable(board, x, y, player)) {
            if (board.inBoundsStone(x + 1, y + 1)) neighbours.add(new int[]{x + 1, y + 1});
        }

        if (board.inBoundsRhombus(x - 1, y - 1) && rhombusTraversable(board, x - 1, y - 1, player)) {
            if (board.inBoundsStone(x - 1, y - 1)) neighbours.add(new int[]{x - 1, y - 1});
        }

        if (board.inBoundsRhombus(x, y - 1) && rhombusTraversable(board, x, y - 1, player)) {
            if (board.inBoundsStone(x + 1, y - 1)) neighbours.add(new int[]{x + 1, y - 1});
        }

        if (board.inBoundsRhombus(x - 1, y) && rhombusTraversable(board, x - 1, y, player)) {
            if (board.inBoundsStone(x - 1, y + 1)) neighbours.add(new int[]{x - 1, y + 1});
        }

        return neighbours;
    }

    private boolean rhombusTraversable(QuaxBoard board, int x, int y, Colour player) {
        Colour rh = board.getRhombus(x, y);
        return rh == Colour.NULL || rh == player;
    }

    private int stoneCost(QuaxBoard board, int x, int y, Colour player) {
        Colour stone = board.getStone(x, y);
        if (stone == player) return 0;
        if (stone == Colour.NULL) return 1;
        return INF;
    }

    public String getLastStrategyUsed() {
        return lastStrategyUsed;
    }

    public String getLastExplanation() {
        return lastExplanation;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public HashMap<Move, Integer> getLastScores() {
        return moveScores;
    }

    private static class Node {
        int x;
        int y;
        int cost;

        Node(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }
    }
}