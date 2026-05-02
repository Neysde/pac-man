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

    public Game.Direction getDirection() {
        return direction;
    }

    public Position getPos() {
        return pos;
    }
}
