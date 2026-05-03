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

    // moves player according to inouts and map borders
    public void nextMove(MapData mapData){

        // to prevent bugs from comparing double and int strictly, we check for extremely small difference
        if (Math.abs(visualRow-pos.getRow())<0.06 && Math.abs(visualCol-pos.getCol())<0.06){ // if position and visual position are similar
            // sets the row and col position by adding requested direction's (up-down-left-right) row and col delta
            int nextRow=pos.getRow()+requestedDirection.getDRow();
            int nextCol=pos.getCol()+requestedDirection.getDCol();
            if (mapData.isValidMove(nextRow,nextCol)){
                currentDirection=requestedDirection; // sets the requested direction to current direction that player moves if player be able to move there(no wall blocking) (otherwise, player keeps moving with its current direction)
            }
            // sets the row and col position by adding the current direction's row and col delta
            nextRow=pos.getRow()+currentDirection.getDRow();
            nextCol=pos.getCol()+currentDirection.getDCol();

            // moving status checker
            if (currentDirection==Game.Direction.NONE){
                moving=false; // if player's direction status is none
            }else if (mapData.isValidMove(nextRow,nextCol)){
                moving=true; // if player can move there
            } else {
                moving=false; // if player cannot move there
            }

        }

        // gives player smooth movement by changing the visual row and col every frame by adding a partial value of direction delta
        if (moving){
            visualRow+=currentDirection.getDRow()*0.10;
            visualCol+=currentDirection.getDCol()*0.10;
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

        // (if player snapped on the row and on the column) and (if row or column have changed)
        if ((visualRow==roundedRow && visualCol==roundedCol) && (visualRow!= pos.getRow() || visualCol!=pos.getCol())){
            pos=new Position(roundedRow,roundedCol); // set new position as player fully moved there logically
            if (mapData.hasPellet(pos.getRow(), pos.getCol())){ // if this new position contains pellet, player collects it thus its score increases
                mapData.removePellet(pos.getRow(), pos.getCol());
                score+=10;
            }
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
