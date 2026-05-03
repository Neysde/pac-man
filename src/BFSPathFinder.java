import java.util.ArrayList;
import java.util.Collections;

public class BFSPathFinder {

    public ArrayList<Position> getFullShortestPath (Position start, Position goal, MapData mapData){

        // first verify the goal is inside map bounds
        if (goal.getRow() < 0 || goal.getRow() >= mapData.getRows() || goal.getCol() < 0 || goal.getCol() >= mapData.getCols()) {
            return null;
        }

        Queue<Position> tiles = new Queue<>(); // positions stacked in queue
        tiles.enqueue(start); // start from the start

        boolean[][] visited = new boolean[mapData.getRows()][mapData.getCols()]; // visited positions

        Position[][] parent = new Position[mapData.getRows()][mapData.getCols()]; // you use the coordinates of neighbor as the index to store the position of where you came from

        visited[start.getRow()][start.getCol()]=true;



        // checks until there is no position left on the queue
        while(!tiles.isEmpty()){
            Position current = tiles.dequeue();
            if (current.equals(goal)){
                break;
            }

            // checks the directions in UP->DOWN->LEFT->RIGHT order (defined a new array containing the direction to eliminate checking NONE direction)
            for (Game.Direction dir : new Game.Direction[]{Game.Direction.UP, Game.Direction.DOWN, Game.Direction.LEFT, Game.Direction.RIGHT}){
                int nextRow = current.getRow()+dir.getDRow();
                int nextCol = current.getCol()+dir.getDCol();

                if (mapData.isValidMove(nextRow,nextCol) && !visited[nextRow][nextCol]){ // if the neighbor is not a wall and not visited
                    visited[nextRow][nextCol]=true; // go to the neighbor
                    parent[nextRow][nextCol]=current; // indicates the neighbor tile was reached from the current tile
                    tiles.enqueue(new Position(nextRow,nextCol));
                }
            }
        }

        // if there is no path (we also check start is not equal to goal, if start==goal, path must contain only the start position (as loop above breaks and goal position of the parent array is still null))
        if (parent[goal.getRow()][goal.getCol()]==null && !start.equals(goal)){
            return null;
        }

        ArrayList<Position> path = new ArrayList<>(); // stores the positions that constructs the path backwards at first
        Position currentStep = goal; // start from the end

        // finds the path by tracing the tile path we constructed in parent in backwards
        while(!currentStep.equals(start)){
            path.add(currentStep);
            currentStep = parent[currentStep.getRow()][currentStep.getCol()];
        }
        path.add(start); // while loop breaks when currentStep equals start so start position is also needed to add to the path

        Collections.reverse(path); // reverses the positions to make them start from the start and end at the goal

        return path;

    }
}
