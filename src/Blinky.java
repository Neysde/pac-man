public class Blinky extends Enemy {

    public Blinky(Position pos, BFSPathFinder finder) {
        super(pos,finder);
    }

    public Position selectTarget(Player player, MapData mapData){

        Position[] corners = mapData.getCorners();
        Position closestCorner = new Position(0,0); // set top left by default
        double leastDistance=Double.MAX_VALUE; // initially set to a very high number
        for (Position corner : corners){
            double distance = Math.pow(player.getPos().getRow()-corner.getRow(),2)+Math.pow(player.getPos().getCol()-corner.getCol(),2); // we will not take the sqrt to make better comparisons
            if (distance<leastDistance){ // this < does the tiebreaking job (it selects the one that appears first when distances are equal)
                leastDistance=distance;
                closestCorner=corner;
            }
        }

        return closestCorner;


        // stores the position object of top-left, top-right, bottom-left, bottom-right corners
        /*Position[] corners = {new Position(0,0),new Position(0, mapData.getCols()-1),new Position(mapData.getRows()-1,0),new Position(mapData.getRows()-1,mapData.getCols()-1)};
        Position closestCorner = new Position(0,0); // set top left by default
        double leastDistance=9999999; // initially set to a very high number
        for (Position corner : corners){
            double distance = Math.pow(player.getPos().getRow()-corner.getRow(),2)+Math.pow(player.getPos().getCol()-corner.getCol(),2); // we will not take the sqrt to make better comparisons
            if (distance<leastDistance){
                leastDistance=distance;
                closestCorner=corner;
            }
        }

        Position closestCornerWalkable = new Position(0,0); // set top left by default
        double leastDistanceWalkable=9999999;
        for (int i=0;i< mapData.getRows();i++){
            for(int j=0;j< mapData.getCols();j++){
                if (mapData.isValidMove(i,j)){
                    double distance = Math.pow(i-closestCorner.getRow(),2)+Math.pow(j-closestCorner.getCol(),2); // we will not take the sqrt to make better comparisons
                    if (distance<leastDistanceWalkable){
                        leastDistanceWalkable=distance;
                        closestCornerWalkable=new Position(i,j);
                    }
                }

            }
        }
        return closestCornerWalkable;*/
    }

}
