import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.Key;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            // Load map data and initialize game components. This class is immutable.
            MapData mapData = loadMapData("data/map.txt");

            // Create the player based on the starting position defined in the map data. Currently this class is also half-implemented, you should complete it based on the specifications given.
            Player player = new Player(mapData.getPlayerStart());

            // Create enemies based on the starting positions defined in the map data. Currently, enemy classess are not implemented, you should implement them based on the specifications given.
            Enemy[] enemies = new Enemy[]{new Pinky(mapData.getPinkyStart(),new BFSPathFinder()), new Inky(mapData.getInkyStart(),new BFSPathFinder()), new Blinky(mapData.getBlinkyStart(),new BFSPathFinder())};

            // Initialize the game with the player, enemies, and map data. You should also complete the Game class to handle game logic, state management, and interactions between the player and enemies.
            Game game = new Game(player, enemies, mapData);

            // Draw the initial game state
            // You should have a game loop here that updates the game state and calls tickAnimation() and drawGame() repeatedly, but for simplicity, we just draw the initial state.
            GameRenderer renderer = new GameRenderer(mapData, game);
            renderer.setupDraw();

            // to prevent flickering when pressing these keys we store their previous press values
            boolean wasPauseKeyPressed=false;
            boolean wasRestartKeyPressed=false;

            long startTime=0; // stores the exact time in miliseconds when user pressed space at start.
            while(true){
                if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE) && game.getGameState()==Game.GameState.START_SCREEN){
                    startTime=System.currentTimeMillis();
                    game.setGameState(Game.GameState.READY);
                }

                // moving from ready state to playing state when player wants to move
                if (game.getGameState()==Game.GameState.READY){

                    long elapsedTime=System.currentTimeMillis()-startTime; // calculates how much time passed since player pressed space
                    if (elapsedTime>=1000){ // after 1 second start the game actually by setting the state to playing
                        game.setGameState(Game.GameState.PLAYING);
                        game.getPlayer().setRequestedDirection(Game.Direction.NONE); // player has direction right by default
                    }
                }

                // taking direction requests
                if (game.getGameState()==Game.GameState.PLAYING){
                    if (StdDraw.isKeyPressed(KeyEvent.VK_UP)){
                        game.getPlayer().setRequestedDirection(Game.Direction.UP);
                    } else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                        game.getPlayer().setRequestedDirection(Game.Direction.DOWN);
                    } else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                        game.getPlayer().setRequestedDirection(Game.Direction.LEFT);
                    } else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                        game.getPlayer().setRequestedDirection(Game.Direction.RIGHT);
                    }

                    game.setGameState(game.update()); // if game state is playing update the player, enemy movements and set the game state each time what it returns
                }

                // pause logic. must be in playing state or paused state at first
                if (game.getGameState()==Game.GameState.PLAYING || game.getGameState()==Game.GameState.PAUSED){
                    if (StdDraw.isKeyPressed(KeyEvent.VK_P) && !wasPauseKeyPressed){ // only pauses when pressed and was not pressed the previous frame
                        if (game.getGameState()==Game.GameState.PLAYING){
                            game.setGameState(Game.GameState.PAUSED);
                        } else {
                            game.setGameState(Game.GameState.PLAYING);
                        }
                    }
                    // save the key status to prevent flickering
                    if (StdDraw.isKeyPressed(KeyEvent.VK_P)){
                        wasPauseKeyPressed=true;
                    } else {
                        wasPauseKeyPressed=false;
                    }
                }

                // quit logic (from any state)
                if (StdDraw.isKeyPressed(KeyEvent.VK_Q)){
                    System.exit(0);
                }

                // when player lost or won, handles restart and quit operations
                if (game.getGameState()==Game.GameState.LOST || game.getGameState()==Game.GameState.WON){
                    // restart logic
                    if (StdDraw.isKeyPressed(KeyEvent.VK_R) && !wasRestartKeyPressed){ // only pauses when pressed and was not pressed the previous frame
                        game.restartGame();
                        game.setGameState(Game.GameState.START_SCREEN);
                    }
                    // save the key status to prevent flickering
                    if (StdDraw.isKeyPressed(KeyEvent.VK_R)){
                        wasRestartKeyPressed=true;
                    } else {
                        wasRestartKeyPressed=false;
                    }
                }

                renderer.tickAnimation(); // updates animation state
                renderer.drawGame(); // drawer function
                StdDraw.pause(40); // fps=1000/t

            }

        }
        catch (FileNotFoundException e) {
            System.out.println("Map file could not be loaded.");
            e.printStackTrace();
        }
    }


    // Method for loading map data from a file. You should not need to modify this method.
    private static MapData loadMapData(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));

        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        scanner.nextLine();

        String[] playerLine = scanner.nextLine().split(" ");
        Position playerStart = new Position(
                Integer.parseInt(playerLine[1]),
                Integer.parseInt(playerLine[2])
        );

        String[] directEnemyLine = scanner.nextLine().split(" ");
        Position directEnemyStart = new Position(
                Integer.parseInt(directEnemyLine[1]),
                Integer.parseInt(directEnemyLine[2])
        );

        String[] randomChaseEnemyLine = scanner.nextLine().split(" ");
        Position randomChaseEnemyStart = new Position(
                Integer.parseInt(randomChaseEnemyLine[1]),
                Integer.parseInt(randomChaseEnemyLine[2])
        );

        String[] closestCornerEnemyLine = scanner.nextLine().split(" ");
        Position closestCornerEnemyStart = new Position(
                Integer.parseInt(closestCornerEnemyLine[1]),
                Integer.parseInt(closestCornerEnemyLine[2])
        );

        String[] cornerHeader = scanner.nextLine().split(" ");
        int cornerCount = Integer.parseInt(cornerHeader[1]);

        Position[] corners = new Position[cornerCount];
        for (int i = 0; i < cornerCount; i++) {
            int r = scanner.nextInt();
            int c = scanner.nextInt();
            scanner.nextLine();
            corners[i] = new Position(r, c);
        }

        char[][] map = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < cols; j++) {
                map[i][j] = line.charAt(j);
            }
        }

        scanner.close();

        return new MapData(
                map,
                playerStart,
                directEnemyStart,
                randomChaseEnemyStart,
                closestCornerEnemyStart,
                corners
        );
    }
}