import java.util.ArrayList;
import java.util.Map;

public abstract class Enemy {
    protected Position pos;
    protected Game.Direction direction;
    protected BFSPathFinder finder;

    protected double visualRow;
    protected double visualCol;

    public Enemy(Position pos){
        this.pos = pos;
        this.direction = Game.Direction.NONE;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
    }

    public Enemy(Position pos, BFSPathFinder finder) {
        this.pos = pos;
        this.finder = finder;
        this.direction = Game.Direction.NONE;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
    }

    public double getVisualRow() {
        return visualRow;
    }

    public double getVisualCol() {
        return visualCol;
    }

    public void setVisualRow(double visualRow) {
        this.visualRow = visualRow;
    }

    public void setVisualCol(double visualCol) {
        this.visualCol = visualCol;
    }

    public Game.Direction getDirection() {
        return direction;
    }

    public void setDirection(Game.Direction direction) {
        this.direction = direction;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    // calculates the movement direction from one position to another
    protected Game.Direction getDirectionFromPositions(Position from, Position to){
        int rowDiff = to.getRow()-from.getRow();
        int colDiff = to.getCol()-from.getCol();

        // checks which direction fits with the direction delta and returns that direction
        for (Game.Direction dir : new Game.Direction[]{Game.Direction.UP, Game.Direction.DOWN, Game.Direction.LEFT, Game.Direction.RIGHT}){
            if (dir.getDRow()==rowDiff && dir.getDCol()==colDiff){
                return dir;
            }
        }
        return Game.Direction.NONE; // if none fits, return NONE direction
    }

    // moves the enemy towards a target position determined by select target(abstract method)(determined differently in each enemy child class)
    public void move(Player player, MapData mapData){
        // to prevent bugs from comparing double and int strictly, we check for extremely small difference
        if (Math.abs(visualRow-pos.getRow())<0.06 && Math.abs(visualCol-pos.getCol())<0.06){ // if position and visual position are similar
            Position goal = selectTarget(player,mapData); // goal position determined by logic in selectTarget
            ArrayList<Position> path = finder.getFullShortestPath(pos,goal,mapData); //bfs logic gives the full shortest path for that goal position

            if (path==null || path.size()<2){ // size 1 means enemy is already standing on the target
                direction = Game.Direction.NONE;
            } else {
                direction=getDirectionFromPositions(pos,path.get(1)); // 0 is the start position in path arraylist
            }
        }

        // enemies move slower than player visually
        if (direction!=Game.Direction.NONE){
            visualRow+=direction.getDRow()*0.067;
            visualCol+=direction.getDCol()*0.067;
        }

        // snaps the row and column to prevent decimal error
        int roundedRow = (int) Math.round(visualRow);
        int roundedCol = (int) Math.round(visualCol);
        if (Math.abs(visualRow-roundedRow)<0.06){
            visualRow=roundedRow;
        }
        if (Math.abs(visualCol-roundedCol)<0.06){
            visualCol=roundedCol;
        }

        // (if enemy snapped on the row and on the column) and (if row or column have changed)
        if ((visualRow==roundedRow && visualCol==roundedCol) && (visualRow!= pos.getRow() || visualCol!=pos.getCol())){
            pos=new Position(roundedRow,roundedCol);
        }

    }

    // abstract method, subclasses implement according to their strategy
    public abstract Position selectTarget(Player player, MapData mapData);
}


