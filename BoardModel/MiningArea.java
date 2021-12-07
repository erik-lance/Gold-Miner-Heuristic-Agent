package BoardModel;
import Data.Location;
import Tiles.*;

/*
 * This is the mining area data which places
 * the pieces of the board. It is responsible for
 * housing all the tiles of the board.
 */
public class MiningArea {

  private Tile[][] miningArea;
  private int nSize;
  private static Location gsqLoc;
  
  
  public MiningArea(int n){
    miningArea = new Tile[n][n];
    this.nSize = n;

    for(int i=0; i<n; i++)
      for(int j=0; j<n; j++)
        miningArea[i][j] = new Tile(i,j);

  }
  /*
   * Clones the mining area and creates a deep
   * copy through tiles. This is to avoid referencing
   * the same mining area.
   * @param cloneMA copy of miningArea object.
   */
  public MiningArea(MiningArea cloneMA) {
    //this.miningArea = cloneMA.getTiles();
    
    this.nSize = cloneMA.getSize();
    this.miningArea = new Tile[nSize][nSize];

    for (int i =0; i<cloneMA.getSize(); i++) {
      for (int j = 0; j<cloneMA.getSize(); j++) {
        //System.out.println("\nMINING:"+i+" " + j);
        if(cloneMA.getTile(new Location(i,j)) instanceof Pit)
          this.miningArea[i][j] = new Pit((Pit) cloneMA.getTile(new Location(i,j)));
        else if(cloneMA.getTile(new Location(i,j)) instanceof Beacon)
          this.miningArea[i][j] = new Beacon((Beacon) cloneMA.getTile(new Location(i,j)));
        else if(cloneMA.getTile(new Location(i,j)) instanceof GoldSquare)
          this.miningArea[i][j] = new GoldSquare((GoldSquare) cloneMA.getTile(new Location(i,j)));
        else
          this.miningArea[i][j] = new Tile(cloneMA.getTile(new Location(i,j)));
      }
    }
    
  }

  public void activateTile(Location loc) {
    miningArea[loc.r()][loc.c()].tileAbility();
  }

  public void addAgent(GoldMiner agent){
    miningArea[agent.getLoc().r()][agent.getLoc().c()].setAgent(agent); 
  }

  public boolean addGoldSquare(GoldSquare gsq) {
    int x=gsq.getLoc().r(), y=gsq.getLoc().c();

    if(!gsq.getLoc().equals(GoldMiner.defaultLoc) && !(miningArea[x][y] instanceof Pit)){
      gsqLoc = gsq.getLoc();
      miningArea[x][y] = gsq;
      return true;
    }
    return false;
  }

  public boolean addPit(Pit pit) {
    if(!pit.getLoc().equals(GoldMiner.defaultLoc)){
      miningArea[pit.getLoc().r()][pit.getLoc().c()] = pit;
      return true;
    }
    return false;
  }

  public boolean addBeacon(Beacon bcn) {
    int x=bcn.getLoc().r(), y=bcn.getLoc().c();

    if(!bcn.getLoc().equals(GoldMiner.defaultLoc) && !(miningArea[x][y] instanceof Pit)  && !(miningArea[x][y] instanceof GoldSquare)){
      miningArea[x][y] = bcn;
      return true;
    }
    return false;
  }

  /*
   * Manual set tile to normal function. This
   * is for disabling beacons to avoid repetitions.
   */
  public void setTile(Location loc) {
    this.miningArea[loc.r()][loc.c()] = new Tile(loc.r(), loc.c());
    this.miningArea[loc.r()][loc.c()].setTravelled(true);
  }

  /*
   * Gets tile at certain location.
   * @param loc to find tile at this spot
   * @return tile at certain loc if it exists, else return null.
   */
  public Tile getTile(Location loc) { 
    if (loc.r() >= 0 && loc.r() < nSize && loc.c() >= 0 && loc.c() < nSize)
      return miningArea[loc.r()][loc.c()]; 
    else
      return null;
  }

  /*
   * Gets all the tiles in a 2D array of the mining area.
   * Similar to getArea() without cloning.
   * @return 2D array of tiles
   */
  public Tile[][] getTiles() { return miningArea; }

  /*
   * Gets the size of the grid regards to n.
   * @return n size of grid.
   */
  public int getSize() { return this.nSize; }

  /*
   * Returns a reference to the area of current tiles using Java's clone method, which may be unreliable depending on what is being done.
   * @return a 2d array of all the tiles in the area.
   */
  public Tile[][] getArea() { return miningArea.clone(); }

  /*
   * Gets the length of the rows of the board.
   * [Note]: Returns the same as colum due to the size of the board being n*n
   * @return size of the board in terms of rows.
   */
  public int getRow() { return miningArea.length; }

  /*
   * Gets the length of the columns of the board.
   * [Note]: Returns the same as row due to the size of the board being n*n
   * @return size of the board in terms of columns.
   */
  public int getCol() { return miningArea[0].length; }

  /*
   * Gets the location of the gold tile.
   * @return Location of GoldSquare
   */
  public Location getGoldLoc() { return gsqLoc; }

}