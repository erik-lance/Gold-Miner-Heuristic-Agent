package Tiles;

import BoardModel.GoldMiner;
import Data.Location;
import View.Symbols;

/**
 * Abstract of the tile class extended by the three types
 * namely Beacon, GoldSquare, and Pit with their overwritten
 * abilities and a reference to the player if tile is occupied
 * by the player
 */
public class Tile {

  private boolean travelled;
  private int traverses;
  private Location coords;
  private GoldMiner agent;

  //visual debugging
  public boolean highlighted;

  public Tile(int x, int y) {
    travelled = false;
    traverses = 0;
    this.coords = new Location(x, y);

    highlighted = false;
  }

  /*
   * This constructor clones the specified tile.
   * @param cloneTile new object of tile
   */
  public Tile(Tile cloneTile) {
    this.travelled = cloneTile.hasTravelled();
    this.traverses = cloneTile.getTraverses();
    this.coords = new Location(cloneTile.getLoc());
    this.agent = cloneTile.getAgent();
  }

  /**
   * Gets the status of tile if it has been travelled. This
   * helps with the bot's decision on how much space is left
   * @return status of travel
  */
  public boolean hasTravelled() {
    return travelled;
  }

  public int getTraverses() {
    return this.traverses;
  }

  /*
   * Manually sets the travel boolean if player has
   * stepped over said tile.
   * @param t true if travelled, else false.
   */
  public void setTravelled(boolean t) {
    this.traverses += 1;
    this.travelled = t;
  }

  /**
   * When a tile gets occupied, the agent variable is 
   * filled with a reference to the agent. However, this
   * is set to null when the agent leaves.
   * @param agent is the reference to the gold miner
  */
  public void setAgent(GoldMiner agent) {
    this.agent = agent;
  }
  

  /**
   * Grabs the reference of the player.
   * @return player is the reference to the gold miner
   */
  public GoldMiner getAgent() {
    return agent;
  }

  /**
   * Overwritten by tiles extending to tile. This lets the
   * tile use its special ability based on type.
   **/
  public String tileAbility() { 
    return "tile";
  }

  /**
   * Gets the location this tile takes.
   * @return coords of type Location
   */
  public Location getLoc(){
    return coords;
  }

  
  @Override
  public String toString(){
    return (agent != null) ? agent.toString() : 
    (travelled) ? Symbols.VISITED : 
    (highlighted) ? "\033[0;33m\u25AA\033[0m" : " ";
  }
}