package Tiles;
import View.*;

//visual debugging
import java.util.*;
import Data.Location;

/**
  A beacon on a square indicates that from that square, the golden square can be reached in m squares in any vertical or horizontal direction, where m < n, without ever falling into a pit. The value of m is not returned by the action scan.
*/
public class Beacon extends Tile{

  private int goldDistance;

  /**
  *   Instantiates the coordinate of the pot of gold.
  *   @param x supplies the x coordinate
  *   @param y supplies the y coordinate
  */
  public Beacon(int x, int y) {
    super(x,y);
  }

  /**
   * Creates a clone of beacon tile.
   * @param cloneTile object to copy
   */
  public Beacon(Beacon cloneTile) {    
    super(cloneTile);
    this.goldDistance = cloneTile.getGoldDistance();
  }

  /**
   * Sets the distance from gold square
   * @param dist Manhattan distance
   */
  public void setGoldDistance (int dist) {
    this.goldDistance = dist;
  }

  /**
   * Returns the Manhattan distance from gold square
   * @return distance value
   */
  public int getGoldDistance(){
    return goldDistance;
  }

  //visual debugging
  public List<Location> getRegion(int nGrid){
    Location beaconPos = getLoc();
    int distance = goldDistance;

    List<Location> posLoc = new ArrayList<Location>();
    Location rightPoint = new Location(beaconPos.r(), beaconPos.c() + distance);
    Location leftPoint = new Location(beaconPos.r(), beaconPos.c() - distance);
    Location upPoint = new Location(beaconPos.r() + distance, beaconPos.c());
    Location downPoint = new Location(beaconPos.r() - distance, beaconPos.c());
    
    posLoc.add(rightPoint);
    posLoc.add(leftPoint);
    posLoc.add(upPoint);
    posLoc.add(downPoint);

    int numOfDiagonals = distance - 1;

    //first Quadrant ie. top Right
    for(int j = 1; j < numOfDiagonals + 1; j++){
        Location firstQdiagonal = new Location(rightPoint.r() - j, rightPoint.c() - j);
        posLoc.add(firstQdiagonal);
    }

    //Second Quadrant ie. top left
    for(int k = 1; k < numOfDiagonals + 1; k++){
      Location secQdiagonal = new Location(leftPoint.r() - k, leftPoint.c() + k);
      posLoc.add(secQdiagonal);
    }

    //Third Quadrant ie. bottom left
    for(int l = 1; l < numOfDiagonals + 1; l++){
      Location thirdQdiagonal = new Location(leftPoint.r() + l, leftPoint.c() + l );
      posLoc.add(thirdQdiagonal);
    }

    //Fourth Quadrant ie. bottom right
    for(int m = 1; m < numOfDiagonals + 1; m++){
      Location fourthQdiagonal = new Location(rightPoint.r() + m, rightPoint.c() - m);
      posLoc.add(fourthQdiagonal);
    }

    for(int y = 0; posLoc.size() > y; y++){
      Location printer = posLoc.get(y);
      if(printer.c() < 0 || printer.c() >= nGrid || printer.r() < 0 || printer.r() >= nGrid){
        posLoc.remove(y);
        y--;
      }
    }

    return posLoc;
  }

  /**
   * Finds the location of the GoldSquare
   * 
   */
  @Override
  public String tileAbility() {
    super.setTravelled(true);
    return "beacon";
  }

  
  @Override
  public String toString(){
    String str;

    if(this.hasTravelled())
      if(getAgent() != null)
        str = Symbols.AGENT_BCN;
      else
        str = Colors.PURPLE + "X" + Colors.RESET;
    else
      if(getAgent() != null)
        str = Symbols.AGENT_BCN;
      else
        str = Symbols.BEACON;

    return str;
  }
}