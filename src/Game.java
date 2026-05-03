public class Game {

    public enum GameState {
        START_SCREEN,
        READY,
        PLAYING,
        PAUSED,
        WON,
        LOST,
        BACKTRACKING // added backtracking state for indicating backtracking
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

    private Player player;
    private Enemy[] enemies;
    private GameState gameState;
    private final MapData mapData;
    private int pelletCount;

    // for backtracking
    private Stack<Snapshot> memoryStack;
    

    public Game(Player player, Enemy[] enemies, MapData mapData) {
        this.player = player;
        this.enemies = enemies;
        this.mapData = mapData;
        this.gameState = GameState.START_SCREEN;
        memoryStack=new Stack<>();
        // counts all pellets at start
        for (int i=0;i< mapData.getRows();i++){
            for(int j=0;j< mapData.getCols();j++){
                if (mapData.hasPellet(i,j)){
                    pelletCount++;
                }
            }
        }
    }

    // handles player and enemy movement, collisions, states by returning game states
    public GameState update(){
        // before evaluating movement etc. we take a snapshot of the game
        Position playerCurrentPos = player.getPos();
        int currentScore = player.getScore();
        Position[] enemiesCurrentPositions = new Position[enemies.length];
        boolean wasPelletEaten;
        for (int i=0;i< enemies.length;i++){ // saves each enemies' position
            enemiesCurrentPositions[i]=enemies[i].getPos();
        }

        player.nextMove(mapData);
        for (int i=0;i< enemies.length;i++){ // get each enemy, move them according each of their movement logic and if they collide with player game over
            enemies[i].move(player,mapData);

            if (checkCollision(player,enemies[i])){
                System.out.println("Game Over");
                return GameState.LOST; // game over
            }
        }

        boolean needSnapshot=false; // whether a snapshot is needed

        // checks whether the score or player position changed
        if (player.getScore()!=currentScore || !player.getPos().equals(playerCurrentPos)){
            needSnapshot=true;
        }
        // checks whether an enemy's posiiton changed
        for (int i=0;i< enemies.length;i++) {
            if (!enemies[i].getPos().equals(enemiesCurrentPositions[i])){
                needSnapshot=true;
            }
        }

        if (needSnapshot){
            // determines whether a pellet is eaten or not by checking the score with previous saved score at the beginning
            if (player.getScore()>currentScore){
                wasPelletEaten=true;
            } else {
                wasPelletEaten=false;
            }

            Position[] enemiesEvaluatedPositions = new Position[enemies.length];
            Direction[] enemiesEvaluatedDirections = new Direction[enemies.length];

            for (int j=0;j<enemies.length;j++){ // gets enemies' positions and directions after their movement etc.
                enemiesEvaluatedPositions[j]=enemies[j].getPos();
                enemiesEvaluatedDirections[j]=enemies[j].getDirection();
            }

            // pushes the snapshot to the stack
            memoryStack.push(new Snapshot(player.getPos(),player.getScore(),wasPelletEaten,enemiesEvaluatedPositions,enemiesEvaluatedDirections,player.getCurrentDirection()));
        }

        if (player.getScore()/10==pelletCount){ // when player collects all pellets (each pellet gives +10 score)
            return GameState.WON;
        }
        return GameState.PLAYING; // by default return playing state and keep updating the entity movements in main game loop

    }

    // collision checker
    public boolean checkCollision(Player player, Enemy enemy){
        double playerRow = player.getVisualRow();
        double playerCol = player.getVisualCol();

        double enemyRow = enemy.getVisualRow();
        double enemyCol = enemy.getVisualCol();

        // if they both in the same column and same row visually, they are colliding
        if (Math.abs(playerRow-enemyRow)<0.50 && Math.abs(playerCol-enemyCol)<0.50){
            return true;
        }

        return false;
    }

    // restart function
    public void restartGame(){
        for (int i=0;i< mapData.getRows();i++){ // restores all pellets by looping all map row and cols
            for (int j=0;j< mapData.getCols();j++){
                mapData.restorePellet(i,j);
            }
        }
        player = new Player(mapData.getPlayerStart()); // resets player object by creating a new player object
        enemies = new Enemy[]{new Pinky(mapData.getPinkyStart(),new BFSPathFinder()), new Inky(mapData.getInkyStart(),new BFSPathFinder()), new Blinky(mapData.getBlinkyStart(),new BFSPathFinder())}; // resets enemies by creating each of them again
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

    // memory stack getter
    public Stack<Snapshot> getMemoryStack() {
        return memoryStack;
    }


}