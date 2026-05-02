public class Player {
    private Position pos;
    private int score;

    private Game.Direction currentDirection;
    private Game.Direction requestedDirection;

    private double visualRow;
    private double visualCol;

    private boolean moving;

    public Player(Position pos) {
        this.currentDirection = Game.Direction.NONE;
        this.requestedDirection = Game.Direction.NONE;
        this.pos = pos;
        this.score = 0;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
        this.moving = false;

    }

    public void nextMove(MapData mapData){

        // to prevent bugs from comparing double and int strictly, we check for extremely small difference
        if (Math.abs(visualRow-pos.getRow())<0.06 && Math.abs(visualCol-pos.getCol())<0.06){
            int nextRow=pos.getRow()+requestedDirection.getDRow();
            int nextCol=pos.getCol()+requestedDirection.getDCol();
            if (mapData.isValidMove(nextRow,nextCol)){
                currentDirection=requestedDirection;
            }
            nextRow=pos.getRow()+currentDirection.getDRow();
            nextCol=pos.getCol()+currentDirection.getDCol();

            if (mapData.isValidMove(nextRow,nextCol)){
                moving=true;
            } else {
                moving=false;
            }

        }

        if (moving){
            visualRow+=currentDirection.getDRow()*0.10;
            visualCol+=currentDirection.getDCol()*0.10;
        }

        // snaps the row and column to prevent decimal error
        int roundedRow = (int) Math.round(visualRow);
        int roundedCol = (int) Math.round(visualCol);

        if (Math.abs(visualRow-roundedRow)<0.001){
            visualRow=roundedRow;

        }
        if (Math.abs(visualCol-roundedCol)<0.001){
            visualCol=roundedCol;
        }

        // (if player snapped on the row and on the column) and (if row or column have changed)
        if ((visualRow==roundedRow && visualCol==roundedCol) && (visualRow!= pos.getRow() || visualCol!=pos.getCol())){
            pos=new Position(roundedRow,roundedCol);
        }
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Position getPos() {
        return pos;
    }

    public Game.Direction getCurrentDirection() {
        return currentDirection;
    }

    public double getVisualRow() {
        return visualRow;
    }

    public double getVisualCol() {
        return visualCol;
    }

    public int getScore() {
        return score;
    }

    // requestedDirection setter
    public void setRequestedDirection(Game.Direction requestedDirection) {
        this.requestedDirection = requestedDirection;
    }
}
