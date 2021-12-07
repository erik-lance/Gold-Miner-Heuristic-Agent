package Tiles;
import View.Symbols;

/**
  If the miner moves into square that contains the pot of gold,   the miner stops, and delivers a “search successful” message.  
*/
public class GoldSquare extends Tile {
  /**
   *   Instantiates the coordinate of the pot of gold.
   *   @param x supplies the x coordinate
   *   @param y supplies the y coordinate
  */
  public GoldSquare(int x, int y) {
    super(x,y);
  }
  /*
   * Creates a clone of gold tile.
   * @param cloneTile object to copy
   */
  public GoldSquare(GoldSquare cloneTile) {
    super(cloneTile);
  }

  /**
   * Ends the game if player occupies this tile.
   * Player wins
   */
   @Override
  public String tileAbility() {
    super.setTravelled(true);
    return "gold";
  }

  
  @Override
  public String toString(){
    return (getAgent() == null)? Symbols.GOLD_SQUARE : Symbols.AGENT_GOLD;
  }

}