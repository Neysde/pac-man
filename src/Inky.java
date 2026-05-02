import java.util.ArrayList;

public class Inky extends Enemy {

    public Inky(Position pos,BFSPathFinder finder) {
        super(pos,finder);
    }

    public Position selectTarget(Player player, MapData mapData){
        double randomNum = Math.random();

        if (randomNum<0.60){
            return player.getPos();
        } else {
            ArrayList<Position> validNeighbors = new ArrayList<>();
            for (Game.Direction dir : new Game.Direction[]{Game.Direction.UP, Game.Direction.DOWN, Game.Direction.LEFT, Game.Direction.RIGHT}){
                int nextRow= pos.getRow()+dir.getDRow();
                int nextCol= pos.getCol()+dir.getDCol();

                if (mapData.isValidMove(nextRow,nextCol)){
                    validNeighbors.add(new Position(nextRow,nextCol));
                }
            }

            if (!validNeighbors.isEmpty()){
                // returns a random position from the neighbors of the enemy that can travel
                return validNeighbors.get((int) (Math.random()*validNeighbors.size())); // type casting has higher precedence than * so make sure use () to enclose the equation
            } else {
                return pos;
            }
        }
    }

}
