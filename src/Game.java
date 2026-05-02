public class Game {

    public enum GameState {
        START_SCREEN,
        READY,
        PLAYING,
        PAUSED,
        WON,
        LOST,
    }

    public enum Direction {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1), NONE(0, 0); // we use -1 for up because row 0 is the top of the map file

        private final int dRow;
        private final int dCol;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }

        public int getDRow() { return dRow; }
        public int getDCol() { return dCol; }
    }

    private final Player player;
    private final Enemy[] enemies;
    private GameState gameState;
    private final MapData mapData;
    

    public Game(Player player, Enemy[] enemies, MapData mapData) {
        this.player = player;
        this.enemies = enemies;
        this.mapData = mapData;
        this.gameState = GameState.START_SCREEN;
    }

    public void update(){
        player.nextMove(mapData);
        for (Enemy enemy : enemies){
            enemy.move(player,mapData);
            if (checkCollision(player,enemy)){
                System.out.println("Game Over");
                break;
            }
        }

    }

    public boolean checkCollision(Player player, Enemy enemy){
        double playerRow = player.getVisualRow();
        double playerCol = player.getVisualCol();

        double enemyRow = enemy.getVisualRow();
        double enemyCol = enemy.getVisualCol();

        if (Math.abs(playerRow-enemyRow)<0.50 && Math.abs(playerCol-enemyCol)<0.50){
            return true;
        }

        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public GameState getGameState() {
        return gameState;
    }

    // game state setter
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}