import java.util.ArrayList;

public class Inky extends Enemy {

    public Inky(Position pos,BFSPathFinder finder) {
        super(pos,finder);
    }

    public Position selectTarget(Player player, MapData mapData){
        double randomNum = Math.random(); // random double between 0(incl)-1.0(excl)

        if (randomNum<0.60){ // by 60% chance, follow the player
            return player.getPos();
        } else { // by 40% chance, selects one of the possible neighboring tiles again randomly as its target
            ArrayList<Position> validNeighbors = new ArrayList<>();
            for (Game.Direction dir : new Game.Direction[]{Game.Direction.UP, Game.Direction.DOWN, Game.Direction.LEFT, Game.Direction.RIGHT}){ // valid neighbor checking and adding to the arraylist
                int nextRow= pos.getRow()+dir.getDRow();
                int nextCol= pos.getCol()+dir.getDCol();

                if (mapData.isValidMove(nextRow,nextCol)){
                    validNeighbors.add(new Position(nextRow,nextCol));
                }
            }

            if (!validNeighbors.isEmpty()){
                // returns a random position from the neighbors of the enemy that can travel
                return validNeighbors.get((int) (Math.random()*validNeighbors.size())); // type casting has higher precedence than * so make sure use () to enclose the equation
            } else { // if there is no valid neighbors it stays in its location
                return pos;
            }
        }
    }

}
