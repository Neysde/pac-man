public class Pinky extends Enemy {

    public Pinky(Position pos, BFSPathFinder finder) {
        super(pos, finder);
    }

    public Position selectTarget(Player player, MapData mapData){
        return player.getPos(); // follows player constantly
    }

}
