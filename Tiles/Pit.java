package Tiles;
import View.*;

/**
  If the miner moves into square that is a pit, it is game-over! Remember that the miner cannot see the locations of the pits.
*/
public class Pit extends Tile{
  /**
  *   Instantiates the coordinate of the pot of gold.
  *   @param x supplies the x coordinate
  *   @param y supplies the y coordinate
  */
  public Pit(int x, int y) {
    super(x,y);
  }

  /*
   * Creates a clone of pit tile.
   * @param cloneTile object to copy
   */
  public Pit(Pit cloneTile){
    super(cloneTile);
  }

  /**
   * Sets travel to true.
   * @return string debug of tile name
   */
  @Override
  public String tileAbility() {
    super.setTravelled(true);
    return "pit";
    //END GAME
  }

  /*
   * Helps translate pit to unicode in GUI.
   * @return visual representation of pit in unicode.
   */
  @Override
  public String toString(){
    String str;

    if(this.hasTravelled())
      if(getAgent() != null)
        str = Symbols.AGENT_PIT;
      else
        str = Colors.RED + "X" + Colors.RESET;
    else
      if(getAgent() != null)
        str = Symbols.AGENT_PIT;
      else
        str = Symbols.PIT;

    return str;
  }

}