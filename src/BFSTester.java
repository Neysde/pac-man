import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BFSTester {

    public static void main(String[] args) {
        // Change this string to the name of the test file you want to check!
        String testFileName = "tests/mapTest.txt";

        try {
            System.out.println("Running test for: " + testFileName);
            System.out.println("--------------------------------------------------");

            // ==========================================================
            // STEP 1: Parse the Test File
            // ==========================================================
            Scanner scanner = new Scanner(new File(testFileName));

            // Read Start Position (Line 1)
            int startRow = scanner.nextInt();
            int startCol = scanner.nextInt();
            Position start = new Position(startRow, startCol);

            // Read Goal Position (Line 2)
            int goalRow = scanner.nextInt();
            int goalCol = scanner.nextInt();
            Position goal = new Position(goalRow, goalCol);

            // Read Expected Output Path (Remaining Lines)
            ArrayList<Position> expectedPath = new ArrayList<>();
            while (scanner.hasNextInt()) {
                int pRow = scanner.nextInt();
                int pCol = scanner.nextInt();
                expectedPath.add(new Position(pRow, pCol));
            }
            scanner.close();

            // ==========================================================
            // STEP 2: Load the Map
            // ==========================================================
            // TODO: You must instantiate your MapData object here using 
            // whatever method you wrote to load "map.txt" in your main game!
            // Example: MapData mapData = readMapFile("map.txt");

            MapData mapData = loadMapData("data/map.txt"); // Replace this line!

            if (mapData == null) {
                System.out.println("⚠️ ERROR: You need to plug in your MapData loading logic at Step 2!");
                return;
            }

            // ==========================================================
            // STEP 3: Run the Algorithm
            // ==========================================================
            BFSPathFinder finder = new BFSPathFinder();
            ArrayList<Position> actualPath = finder.getFullShortestPath(start, goal, mapData);

            // ==========================================================
            // STEP 4: Compare and Report
            // ==========================================================
            comparePaths(expectedPath, actualPath);

        } catch (FileNotFoundException e) {
            System.out.println("❌ ERROR: Could not find the file named: " + testFileName);
            System.out.println("Make sure it is in the correct folder (usually the project root).");
        }
    }

    /**
     * Helper method to compare the expected path with your actual path 
     * and print a detailed report to the console.
     */
    private static void comparePaths(ArrayList<Position> expected, ArrayList<Position> actual) {

        // NEW: Check for the "No Path Exists" requirement!
        // If the text file has no expected path lines, we EXPECT a null return.
        if (expected.isEmpty()) {
            if (actual == null) {
                System.out.println("✅ PASS! Correctly returned null for an unreachable goal.");
                return;
            } else {
                System.out.println("❌ FAIL: Expected null (no path exists), but your BFS returned a path of size " + actual.size());
                return;
            }
        }

        // If we expected a path, but your BFS returned null...
        if (actual == null) {
            System.out.println("❌ FAIL: Your BFS returned null, but a valid path exists! (Expected length: " + expected.size() + ")");
            return;
        }

        // Check if the lengths match exactly
        if (expected.size() != actual.size()) {
            System.out.println("❌ FAIL: Path lengths differ!");
            System.out.println("   -> Expected steps: " + expected.size());
            System.out.println("   -> Your steps:     " + actual.size());
            return;
        }

        // Check strict UP-DOWN-LEFT-RIGHT priority via exact coordinate matching
        boolean passed = true;
        for (int i = 0; i < expected.size(); i++) {
            Position expPos = expected.get(i);
            Position actPos = actual.get(i);

            if (expPos.getRow() != actPos.getRow() || expPos.getCol() != actPos.getCol()) {
                System.out.println("❌ FAIL: Paths diverge at step " + (i + 1) + "!");
                System.out.println("   -> Expected: (" + expPos.getRow() + ", " + expPos.getCol() + ")");
                System.out.println("   -> Actual:   (" + actPos.getRow() + ", " + actPos.getCol() + ") (Check your direction priority!)");
                passed = false;
                break;
            }
        }

        if (passed) {
            System.out.println("✅ PASS! Your BFS algorithm perfectly matched the expected output.");
            System.out.println("   -> Total steps evaluated: " + expected.size());
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