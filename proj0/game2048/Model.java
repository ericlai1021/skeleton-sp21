package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Board {
    /** Current contents of the board. */
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        super(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        super(rawValues, score);
        int size = rawValues.length;
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }


    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        super.clear();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        super.addTile(tile);
        checkGameOver();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        if (side == Side.NORTH) {
            for (int c = 0; c < super.size(); c++) {
                for (int r = super.size() - 1; r >= 0; r--) {
                    Tile t = super.tile(c, r);
                    if (t != null) {
                        int cnt = 0;
                        for (int rTmp = r - 1; rTmp >= 0; rTmp--) {
                            Tile tB = super.tile(c, rTmp);
                            if (tB == null) {
                                cnt++;
                            }
                            else {
                                if (tB.value() == t.value()) {
                                    super.move(c, r, tB);
                                }
                                else {
                                    if (cnt > 0) {
                                        super.move(c, r - 1, tB);
                                        r--;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    else {
                        int flag = 0;
                        for (int rTmp1 = r; rTmp1 >= 0; rTmp1--) {
                            for (int rTmp2 = rTmp1 - 1; rTmp2 >= 0; rTmp2--) {
                                Tile tB = super.tile(c, rTmp2);
                                if (tB != null) {
                                    super.move(c, rTmp1, tB);
                                    flag = 1;
                                    break;
                                }
                            }
                        }
                        if (flag == 1) r++;
                    }

                    changed = true;
                    score += 3;
                }
            }
        } else if (side == Side.SOUTH) {
            for (int c = 0; c < super.size(); c++) {
                for (int r = 0; r < super.size(); r++) {
                    Tile t = super.tile(c, r);
                    if (t != null) {
                        int cnt = 0;
                        for (int rTmp = r + 1; rTmp < super.size(); rTmp++) {
                            Tile tB = super.tile(c, rTmp);
                            if (tB == null) {
                                cnt++;
                            }
                            else {
                                if (tB.value() == t.value()) {
                                    super.move(c, r, tB);
                                }
                                else {
                                    if (cnt > 0) {
                                        super.move(c, r + 1, tB);
                                        r++;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    else {
                        int flag = 0;
                        for (int rTmp1 = r; rTmp1 < super.size(); rTmp1++) {
                            for (int rTmp2 = rTmp1 + 1; rTmp2 < super.size(); rTmp2++) {
                                Tile tB = super.tile(c, rTmp2);
                                if (tB != null) {
                                    super.move(c, rTmp1, tB);
                                    flag = 1;
                                    break;
                                }
                            }
                        }
                        if (flag == 1) r--;
                    }

                    changed = true;
                    score += 3;
                }
            }
        } else if (side == Side.WEST) {
            for (int r = 0; r < super.size(); r++) {
                for (int c = 0; c < super.size(); c++) {
                    Tile t = super.tile(c, r);
                    if (t != null) {
                        int cnt = 0;
                        for (int cTmp = c + 1; cTmp < super.size(); cTmp++) {
                            Tile tB = super.tile(cTmp, r);
                            if (tB == null) {
                                cnt++;
                            }
                            else {
                                if (tB.value() == t.value()) {
                                    super.move(c, r, tB);
                                }
                                else {
                                    if (cnt > 0) {
                                        super.move(c + 1, r, tB);
                                        c++;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    else {
                        int flag = 0;
                        for (int cTmp1 = c; cTmp1 < super.size(); cTmp1++) {
                            for (int cTmp2 = cTmp1 + 1; cTmp2 < super.size(); cTmp2++) {
                                Tile tB = super.tile(cTmp2, r);
                                if (tB != null) {
                                    super.move(cTmp1, r, tB);
                                    flag = 1;
                                    break;
                                }
                            }
                        }
                        if (flag == 1) c--;
                    }

                    changed = true;
                    score += 3;
                }
            }
        } else if (side == Side.EAST) {
            for (int r = 0; r < super.size(); r++) {
                for (int c = super.size() - 1; c >= 0; c--) {
                    Tile t = super.tile(c, r);
                    if (t != null) {
                        int cnt = 0;
                        for (int cTmp = c - 1; cTmp >= 0; cTmp--) {
                            Tile tB = super.tile(cTmp, r);
                            if (tB == null) {
                                cnt++;
                            }
                            else {
                                if (tB.value() == t.value()) {
                                    super.move(c, r, tB);
                                }
                                else {
                                    if (cnt > 0) {
                                        super.move(c - 1, r, tB);
                                        c--;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    else {
                        int flag = 0;
                        for (int cTmp1 = c; cTmp1 >= 0; cTmp1--) {
                            for (int cTmp2 = cTmp1 - 1; cTmp2 >= 0; cTmp2--) {
                                Tile tB = super.tile(cTmp2, r);
                                if (tB != null) {
                                    super.move(cTmp1, r, tB);
                                    flag = 1;
                                    break;
                                }
                            }
                        }
                        if (flag == 1) c++;
                    }

                    changed = true;
                    score += 3;
                }
            }
        }

        checkGameOver();

        return changed;
    }

    /** Determine whether game is over. */
    public void checkGameOver() {
        boolean maxTileExists = false;
        boolean atLeastOneMoveExists = false;
        for(int i = 0; i < super.size(); i++){
            if (!maxTileExists) {
                for (int j = 0; j < super.size(); j++) {
                    if (super.tile(i, j) == null) continue;
                    if (super.tile(i, j).value() == MAX_PIECE) {
                        maxTileExists = true;
                        break;
                    }
                    //System.out.println(b.tile(i,j));
                }
            } else {
                break;
            }
        }

        for(int i = 0; i < super.size(); i++){
            if (!atLeastOneMoveExists) {
                for (int j = 0; j < super.size(); j++) {
                    if (super.tile(i, j) == null){
                        atLeastOneMoveExists = true;
                        break;
                    }
                }
            } else {
                break;
            }
        }
        if (!atLeastOneMoveExists) {
            for (int c = 0; c < super.size(); c++) {
                if (!atLeastOneMoveExists) {
                    for (int r = 1; r < super.size(); r++) {
                        if (super.tile(c, r - 1).value() == super.tile(c, r).value()) {
                            atLeastOneMoveExists = true;
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            if (!atLeastOneMoveExists) {
                for (int r = 0; r < super.size(); r++) {
                    if (!atLeastOneMoveExists) {
                        for (int c = 1; c < super.size(); c++) {
                            if (super.tile(c - 1, r).value() == super.tile(c, r).value()) {
                                atLeastOneMoveExists = true;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        gameOver = maxTileExists || !atLeastOneMoveExists;
    }

    public boolean checkBoardEmpty() {
        for(int i = 0; i < super.size(); i++){
            for(int j = 0; j < super.size(); j++){
                if (super.tile(i,j) == null) return true;
            }
        }
        return false;
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
