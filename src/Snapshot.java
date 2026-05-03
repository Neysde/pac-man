public class Snapshot {

    // stores player position, direction, score, whether a pellet eaten in player's position, enemies' positions and directions
    private Position playerCurrentPos;
    private int currentScore;
    private boolean wasPelletEaten;
    private Position[] enemiesEvaluatedPositions;
    private Game.Direction[] enemiesEvaluatedDirections;
    private Game.Direction playerEvaluatedDirection;

    Snapshot(Position playerCurrentPos, int currentScore, boolean wasPelletEaten, Position[] enemiesEvaluatedPositions, Game.Direction[] enemiesEvaluatedDirections, Game.Direction playerEvaluatedDirection){
        this.playerCurrentPos=playerCurrentPos;
        this.currentScore=currentScore;
        this.wasPelletEaten=wasPelletEaten;
        this.enemiesEvaluatedPositions=enemiesEvaluatedPositions;
        this.enemiesEvaluatedDirections=enemiesEvaluatedDirections;
        this.playerEvaluatedDirection=playerEvaluatedDirection;
    }

    public Position getPlayerCurrentPos() {
        return playerCurrentPos;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public boolean isWasPelletEaten() {
        return wasPelletEaten;
    }

    public Position[] getEnemiesEvaluatedPositions() {
        return enemiesEvaluatedPositions;
    }

    public Game.Direction[] getEnemiesEvaluatedDirections() {
        return enemiesEvaluatedDirections;
    }

    public Game.Direction getPlayerEvaluatedDirection() {
        return playerEvaluatedDirection;
    }
}
