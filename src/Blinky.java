public class Blinky extends Enemy {

    public Blinky(Position pos, BFSPathFinder finder) {
        super(pos,finder);
    }

    public Position selectTarget(Player player, MapData mapData){

        Position[] corners = mapData.getCorners(); // get corners from mapdata
        Position closestCorner = new Position(0,0); // set top left by default
        double leastDistance=Double.MAX_VALUE; // initially set to a very high number
        for (Position corner : corners){ // check each corner
            double distance = Math.pow(player.getPos().getRow()-corner.getRow(),2)+Math.pow(player.getPos().getCol()-corner.getCol(),2); // we will not take the sqrt to make better comparisons
            if (distance<leastDistance){ // this < does the tiebreaking job (it selects the one that appears first when distances are equal)
                leastDistance=distance;
                closestCorner=corner;
            }
        }

        return closestCorner; // closest corner
    }

}
