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

    protected Game.Direction getDirectionFromPositions(Position from, Position to){
        int rowDiff = to.getRow()-from.getRow();
        int colDiff = to.getCol()-from.getCol();

        for (Game.Direction dir : new Game.Direction[]{Game.Direction.UP, Game.Direction.DOWN, Game.Direction.LEFT, Game.Direction.RIGHT}){
            if (dir.getDRow()==rowDiff && dir.getDCol()==colDiff){
                return dir;
            }
        }
        return Game.Direction.NONE;
    }

    public void move(Player player, MapData mapData){
        // to prevent bugs from comparing double and int strictly, we check for extremely small difference
        if (Math.abs(visualRow-pos.getRow())<0.06 && Math.abs(visualCol-pos.getCol())<0.06){
            Position goal = selectTarget(player,mapData);
            ArrayList<Position> path = finder.getFullShortestPath(pos,goal,mapData);

            if (path==null || path.size()<2){ // size 1 means ghost is already standing on the target
                direction = Game.Direction.NONE;
            } else {
                direction=getDirectionFromPositions(pos,path.get(1)); // 0 is the start position
            }

        }

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

    public abstract Position selectTarget(Player player, MapData mapData);
}


