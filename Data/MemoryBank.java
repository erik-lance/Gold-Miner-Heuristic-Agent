package Data;

import java.util.*;
/**
 * Helps the bot remember or deduct which tiles may
 * or may not contain the pit/beacon/gold. Uses a matrix
 * that adds a number by scan to understand which tiles
 * it has scanned.

  POSSIBLE CONCERNS
  scanning on own tile?
  possible location issues, need to test soon.

*/
public class MemoryBank {
  private int[][] goldMemory;
  private int[][] bcnMemory;
  private int[][] pitMemory;

  private int nGrid;

  /**
   * Produces a memory bank for objects in the
   * agent's simulation.
   * @param n is an integer representing grid size
   */
  public MemoryBank(int n) {
    this.nGrid = n;
    goldMemory = new int[n][n];
    bcnMemory = new int[n][n];
    pitMemory = new int[n][n];

    for (int i = 0; i < n; i++) {
      for(int j = 0; j < n; j++) {
        //System.out.println("gM: "+goldMemory[i][j]);
          goldMemory[i][j] = 0;
          bcnMemory[i][j] = 1;
          pitMemory[i][j] = 1;
      }
    }

    goldMemory[0][0] = 0;
    bcnMemory[0][0] = 0;
    pitMemory[0][0] = 0;

  }
  
  /*
   * Clone of memorybank to assist with deep copying.
   * @param cloneBank object of memorybank to clone.
   */
  public MemoryBank(MemoryBank cloneBank) {
    this.nGrid = cloneBank.nGrid;
    this.goldMemory = new int[this.nGrid][this.nGrid];
    this.bcnMemory = new int[this.nGrid][this.nGrid];
    this.pitMemory = new int[this.nGrid][this.nGrid];

    for (int i = 0; i < this.nGrid; i++) {
      for (int j = 0; j < this.nGrid; j++) {
        this.goldMemory[i][j] = cloneBank.getGold()[i][j];
        this.bcnMemory[i][j] = cloneBank.getBcn()[i][j];
        this.pitMemory[i][j] = cloneBank.getPit()[i][j];
      }
    }

  }
  
  /*
   * When agent's scan returns gold, adds 1 to certainty
   * of coordinate where gold is. Should an intersection
   * occur, will turn every other coordinate to 0 and
   * the intersection will be 1 since there is only
   * 1 gold tile.
   * @param loc coordinate agent is on
   * @param dir direction agent is facing.
   */
  public void scanGold(Location loc, int dir) {
    if (dir==0) { //NORTH
      for (int y = loc.r()-1; y >= 0; y--) {
          goldMemory[y][loc.c()] += 1;
      }
    }
    else if (dir==1) { //EAST
      for (int x = loc.c()+1; x < nGrid; x++) {
          goldMemory[loc.r()][x] += 1;
      }
    }
    else if (dir==2) { //SOUTH
      for (int y = loc.r()+1; y < nGrid; y++) {
          goldMemory[y][loc.c()] += 1;
      }
    }
    else if (dir==3) { //WEST
      for (int x = loc.c()-1; x >= 0; x--) {
          goldMemory[loc.r()][x] += 1;
      }
    }
    
  }

  /*
   * When agent's scan returns pit, adds 1 to certainty
   * of coordinate where pit is.
   * @param loc coordinate agent is on
   * @param dir direction agent is facing.
   */
  public void scanPit(Location loc, int dir) {
    if (dir==0) { //NORTH
      for (int y = loc.r()-1; y >= 0; y--) {
        if (pitMemory[y][loc.c()] != 0)
          pitMemory[y][loc.c()] += 1;
      }
    }
    else if (dir==1) { //EAST
      for (int x = loc.c()+1; x < nGrid; x++) {
        if (pitMemory[loc.r()][x] != 0)
          pitMemory[loc.r()][x] += 1;
      }
    }
    else if (dir==2) { //SOUTH
      for (int y = loc.r()+1; y < nGrid; y++) {
        if (pitMemory[y][loc.c()] != 0)
          pitMemory[y][loc.c()] += 1;
      }
    }
    else if (dir==3) { //WEST
      for (int x = loc.c()-1; x >= 0; x--) {
        if (pitMemory[loc.r()][x] != 0)
          pitMemory[loc.r()][x] += 1;
      }
    }
  }

  /*
   * When agent's scan returns beacon, adds 1 to certainty
   * of coordinate where beacon is.
   * @param loc coordinate agent is on
   * @param dir direction agent is facing.
   */
  public void scanBcn(Location loc, int dir) {
    if (dir==0) { //NORTH
      for (int y = loc.r()-1; y >= 0; y--) {
        if (bcnMemory[y][loc.c()] != 0)
          bcnMemory[y][loc.c()] += 1;
      }
    }
    else if (dir==1) { //EAST
      for (int x = loc.c()+1; x < nGrid; x++) {
        if (bcnMemory[loc.r()][x] != 0)
          bcnMemory[loc.r()][x] += 1;
      }
    }
    else if (dir==2) { //SOUTH
      for (int y = loc.r()+1; y < nGrid; y++) {
        if (bcnMemory[y][loc.c()] != 0)
          bcnMemory[y][loc.c()] += 1;
      }
    }
    else if (dir==3) { //WEST
      for (int x = loc.c()-1; x >= 0; x--) {
        if (bcnMemory[loc.r()][x] != 0)
          bcnMemory[loc.r()][x] += 1;
      }
    }
  }

  /*
   * When agent's scan returns nothing, it is assured
   * that these tiles are safe.
   * @param loc coordinate agent is on
   * @param dir direction agent is facing.
   */
  public void scanEmpty(Location loc, int dir) {
    if (dir==0) { //NORTH
      for (int y = loc.r(); y >=0; y--) {
        goldMemory[y][loc.c()]  = 0;
        bcnMemory[y][loc.c()]  = 0;
        pitMemory[y][loc.c()]   = 0;
      }
    }
    else if (dir==1) { //EAST
      for (int x = loc.c(); x < nGrid; x++) {
        goldMemory[loc.r()][x]  = 0;
        bcnMemory[loc.r()][x]   = 0;
        pitMemory[loc.r()][x]   = 0;
      }
    }
    else if (dir==2) { //SOUTH
      for (int y = loc.r(); y < nGrid; y++) {
        goldMemory[y][loc.c()]  = 0;
        bcnMemory[y][loc.c()]  = 0;
        pitMemory[y][loc.c()]   = 0;
      }
    }
    else if (dir==3) { //WEST
      for (int x = loc.c(); x >= 0; x--) {
        goldMemory[loc.r()][x]  = 0;
        bcnMemory[loc.r()][x]   = 0;
        pitMemory[loc.r()][x]   = 0;
      }
    }
  }

  /*
   * Sets specific tile to be empty, guaranteed safe.
   * Often due to travelling to said tile by move.
   * @param loc current location guaranteed safe
   */
  public void setEmpty(Location loc) {
    goldMemory[loc.r()][loc.c()]  = 0;
    bcnMemory[loc.r()][loc.c()]   = 0;
    pitMemory[loc.r()][loc.c()]   = 0;
  }

  /**
   * Grabs entire bank of gold
   * @return gold memory bank
  */
  public int[][] getGold()  { return goldMemory;}

  /**
   * Grabs entire bank of beacon
   * @return beacon memory bank
   */
  public int[][] getBcn()   { return bcnMemory; }

  /**
   * Grabs entire bank of pit
   * @return pit memory bank
   */
  public int[][] getPit()   { return pitMemory; }

  /*
   * Gets the level of the gold tile
   * @param loc tile location
   * @return gold tile's certainty
   */
  public int getGoldLevel(Location loc) {
    return goldMemory[loc.r()][loc.c()];
  }

  /*
   * Gets the level of the beacon tile
   * @param loc tile location
   * @return beacon tile's certainty
   */
  public int getBcn(Location loc) {
    return bcnMemory[loc.r()][loc.c()];
  }

  /**
   * Checks a specific coordinate for the danger level. Where 0 is the safest and the increasing level indicates its danger.
   * @param loc is the location of the coordinate to check
   * @return danger level of specified coordinate.
   */
  public int getDangerLevel(Location loc) {
    return pitMemory[loc.r()][loc.c()];
  }

  /*
   * Checks for the number of tiles that are undiscovered. Uses
   * the pit memory as basis for discovery.
   * @return number of unknown tiles.
   */
  public int getUnknown() {
    int nUnk = 0;
    for (int i = 0; i < nGrid; i++) 
      for (int j = 0; j < nGrid; j++) 
        if (getDangerLevel(new Location(i,j))>=1)
          nUnk++;
    
    return nUnk;
  }

  /*
   * Overloaded function for getUnknown().
   * Checks for the number of tiles that are undiscovered in 
   * a certain direction. Uses the pit memory as basis for discovery.
   * @param loc current position
   * @param dir current direction
   * @return number of unknown tiles.
   */
  public int getUnknown(Location loc, int dir) {
    int nUnk = 0;

    /*
      Note: we use >=, because we need to check if
      a tile is truly a pit because we're given a
      vague direction.
    */

    if (dir==0) {  //NORTH
      for (int y = loc.r()-1; y >= 0; y--) 
          if (pitMemory[y][loc.c()] >= 1)
            nUnk++;
    }
    else if (dir==1)  {//EAST
      for (int x = loc.c()+1; x < nGrid; x++) 
          if (pitMemory[loc.r()][x] >= 1)
            nUnk++;
    }
    else if (dir==2) {//SOUTH
      for (int y = loc.r()+1; y < nGrid; y++) 
          if(pitMemory[y][loc.c()] >= 1)
            nUnk++;
    }  
    else if (dir==3){  //WEST
      for (int x = loc.c()-1; x >= 0; x--) 
          if (pitMemory[loc.r()][x] >= 1)
            nUnk++;
    }

    return nUnk;
  }

  /*
   * When scanning in a direction, get the type that
   * shows up first that is not unknown.
   * @param loc current location
   * @param dir current direction
   * @return type of tile (0-2) [gold/beacon/pit] else -1
   */
  public int getClosestType(Location loc, int dir) {
 
    if (dir==0) {  //NORTH
      for (int y = loc.r()-1; y >= 0; y--) {
        if      (goldMemory[y][loc.c()] > 0) return 0;
        else if (bcnMemory[y][loc.c()] > 1)  return 1;
        else if (pitMemory[y][loc.c()] > 1)  return 2;
      }
    }
    else if (dir==1)  {//EAST
      for (int x = loc.c()+1; x < nGrid; x++) {
        if      (goldMemory[loc.r()][x] > 0) return 0;
        else if (bcnMemory[loc.r()][x] > 1)  return 1;
        else if (pitMemory[loc.r()][x] > 1)  return 2;
      }
    }
    else if (dir==2) {//SOUTH
      for (int y = loc.r()+1; y < nGrid; y++) {
        if      (goldMemory[y][loc.c()] > 0) return 0;
        else if (bcnMemory[y][loc.c()] > 1)  return 1;
        else if (pitMemory[y][loc.c()] > 1)  return 2;
      }
    }  
    else if (dir==3){  //WEST
      for (int x = loc.c()-1; x >= 0; x--) {
        if      (goldMemory[loc.r()][x] > 0) return 0;
        else if (bcnMemory[loc.r()][x] > 1)  return 1;
        else if (pitMemory[loc.r()][x] > 1)  return 2;
      }
    }

    return -1;
  }

  /*
   * When scanning in a direction, remove the type
   * that shows up first that is not unknown.
   * @param loc current location
   * @param dir current direction
   * @return type of tile (0-2) [gold/beacon/pit] else -1
   */
  public void removeClosestType(Location loc, int dir) {
    if (dir==0) {  //NORTH
      for (int y = loc.r()-1; y >= 0; y--) {
        if (goldMemory[y][loc.c()] > 0) {
          goldMemory[y][loc.c()] = 0;
          break;
        }
        else if (bcnMemory[y][loc.c()] > 1) {
          bcnMemory[y][loc.c()] = 0;
          break;
        }
        else if (pitMemory[y][loc.c()] > 1)  {
          pitMemory[y][loc.c()] = 0;
          break;
        }
      }
    }
    else if (dir==1)  {//EAST
      for (int x = loc.c()+1; x < nGrid; x++) {
        if      (goldMemory[loc.r()][x] > 0) {
          goldMemory[loc.r()][x] = 0;
          break;
        }
        else if (bcnMemory[loc.r()][x] > 1)  {
          bcnMemory[loc.r()][x] = 0;
          break;
        }
        else if (pitMemory[loc.r()][x] > 1)  {
          pitMemory[loc.r()][x] = 0;
          break;
        }
      }
    }
    else if (dir==2) {//SOUTH
      for (int y = loc.r()+1; y < nGrid; y++) {
        if      (goldMemory[y][loc.c()] > 0) {
          goldMemory[y][loc.c()] = 0;
          break;
        }
        else if (bcnMemory[y][loc.c()] > 1) {
          bcnMemory[y][loc.c()] = 0;
          break;
        }
        else if (pitMemory[y][loc.c()] > 1)  {
          pitMemory[y][loc.c()] = 0;
          break;
        }
      }
    }  
    else if (dir==3){  //WEST
      for (int x = loc.c()-1; x >= 0; x--) {
        if (goldMemory[loc.r()][x] > 0) {
          goldMemory[loc.r()][x] = 0;
          break;
        }
        else if (bcnMemory[loc.r()][x] > 1)  {
          bcnMemory[loc.r()][x] = 0;
          break;
        }
        else if (pitMemory[loc.r()][x] > 1)  {
          pitMemory[loc.r()][x] = 0;
          break;
        }
      }
    }
  }

  /*
   * Overloaded function
   * When scanning in a direction, removes until it
   * reaches the original memorybank.
   * @param loc current location
   * @param dir current direction
   * @return type of tile (0-2) [gold/beacon/pit] else -1
   */
  public void removeClosestType(Location loc, int dir, int type) {
    if (dir==0) {  //NORTH
      for (int y = loc.r()-1; y >= 0; y--) {
        // memory Gold/Bcn/Pit to delete
        if (type == 0) {
          if (goldMemory[y][loc.c()] > 0) {
            goldMemory[y][loc.c()] = 0;
            bcnMemory[y][loc.c()] = 0;
            pitMemory[y][loc.c()] = 0;
            break;
          }
          if (bcnMemory[y][loc.c()] > 1)
            bcnMemory[y][loc.c()] = 0;
          if (pitMemory[y][loc.c()] > 1)  
            pitMemory[y][loc.c()] = 0;
        }
        else if (type == 1) {
          if (bcnMemory[y][loc.c()] > 1) {
            goldMemory[y][loc.c()] = 0;
            bcnMemory[y][loc.c()] = 0;
            pitMemory[y][loc.c()] = 0;
            break;
          }
          if (goldMemory[y][loc.c()] > 0)
            goldMemory[y][loc.c()] = 0;
          if (pitMemory[y][loc.c()] > 1)  
            pitMemory[y][loc.c()] = 0;
        }
        else if (type ==2) {
          if (pitMemory[y][loc.c()] > 1) {
            goldMemory[y][loc.c()] = 0;
            bcnMemory[y][loc.c()] = 0;
            pitMemory[y][loc.c()] = 0;
            break;
          }
          if (bcnMemory[y][loc.c()] > 1)
            bcnMemory[y][loc.c()] = 0;
          if (goldMemory[y][loc.c()] > 0)  
            goldMemory[y][loc.c()] = 0;
        }
      }
    }
    else if (dir==1)  {//EAST
      for (int x = loc.c()+1; x < nGrid; x++) {
        // memory Gold/Bcn/Pit to delete
        if (type == 0) {
          if (goldMemory[loc.r()][x] > 0) {
            goldMemory[loc.r()][x] = 0;
            bcnMemory[loc.r()][x] = 0;
            pitMemory[loc.r()][x] = 0;
            break;
          }
          if (bcnMemory[loc.r()][x] > 1)
            bcnMemory[loc.r()][x] = 0;
          if (pitMemory[loc.r()][x] > 1)  
            pitMemory[loc.r()][x] = 0;
        }
        else if (type == 1) {
          if (bcnMemory[loc.r()][x] > 1) {
            goldMemory[loc.r()][x] = 0;
            bcnMemory[loc.r()][x] = 0;
            pitMemory[loc.r()][x] = 0;
            break;
          }
          if (goldMemory[loc.r()][x] > 0)
            goldMemory[loc.r()][x] = 0;
          if (pitMemory[loc.r()][x] > 1)  
            pitMemory[loc.r()][x] = 0;
        }
        else if (type == 2) {
          if (pitMemory[loc.r()][x] > 1) {
            goldMemory[loc.r()][x] = 0;
            bcnMemory[loc.r()][x] = 0;
            pitMemory[loc.r()][x] = 0;
            break;
          }
          if (bcnMemory[loc.r()][x] > 1)
            bcnMemory[loc.r()][x] = 0;
          if (goldMemory[loc.r()][x] > 0)  
            goldMemory[loc.r()][x] = 0;
        }
      }
    }
    else if (dir==2) {//SOUTH
      for (int y = loc.r()+1; y < nGrid; y++) {
        // memory Gold/Bcn/Pit to delete
        if (type == 0) {
          if (goldMemory[y][loc.c()] > 0) {
            goldMemory[y][loc.c()] = 0;
            bcnMemory[y][loc.c()] = 0;
            pitMemory[y][loc.c()] = 0;
            break;
          }
          if (bcnMemory[y][loc.c()] > 1)
            bcnMemory[y][loc.c()] = 0;
          if (pitMemory[y][loc.c()] > 1)  
            pitMemory[y][loc.c()] = 0;
        }
        else if (type == 1) {
          if (bcnMemory[y][loc.c()] > 1) {
            goldMemory[y][loc.c()] = 0;
            bcnMemory[y][loc.c()] = 0;
            pitMemory[y][loc.c()] = 0;
            break;
          }
          if (goldMemory[y][loc.c()] > 0)
            goldMemory[y][loc.c()] = 0;
          if (pitMemory[y][loc.c()] > 1)  
            pitMemory[y][loc.c()] = 0;
        }
        else if (type == 2) {
          if (pitMemory[y][loc.c()] > 1) {
            goldMemory[y][loc.c()] = 0;
            bcnMemory[y][loc.c()] = 0;
            pitMemory[y][loc.c()] = 0;
            break;
          }
          if (bcnMemory[y][loc.c()] > 1)
            bcnMemory[y][loc.c()] = 0;
          if (goldMemory[y][loc.c()] > 0)  
            goldMemory[y][loc.c()] = 0;
        }
      }
    }  
    else if (dir==3){  //WEST
      for (int x = loc.c()-1; x >= 0; x--) {
        if (type == 0) {
          if (goldMemory[loc.r()][x] > 0) {
            goldMemory[loc.r()][x] = 0;
            bcnMemory[loc.r()][x] = 0;
            pitMemory[loc.r()][x] = 0;
            break;
          }
          if (bcnMemory[loc.r()][x] > 1)
            bcnMemory[loc.r()][x] = 0;
          if (pitMemory[loc.r()][x] > 1)  
            pitMemory[loc.r()][x] = 0;
        }
        else if (type == 1) {
          if (bcnMemory[loc.r()][x] > 1) {
            goldMemory[loc.r()][x] = 0;
            bcnMemory[loc.r()][x] = 0;
            pitMemory[loc.r()][x] = 0;
            break;
          }
          if (goldMemory[loc.r()][x] > 0)
            goldMemory[loc.r()][x] = 0;
          if (pitMemory[loc.r()][x] > 1)  
            pitMemory[loc.r()][x] = 0;
        }
        else if (type == 2) {
          if (pitMemory[loc.r()][x] > 1) {
            goldMemory[loc.r()][x] = 0;
            bcnMemory[loc.r()][x] = 0;
            pitMemory[loc.r()][x] = 0;
            break;
          }
          if (bcnMemory[loc.r()][x] > 1)
            bcnMemory[loc.r()][x] = 0;
          if (goldMemory[loc.r()][x] > 0)  
            goldMemory[loc.r()][x] = 0;
        }
      }
    }
  }

  /*
   * Get manhattan distance of two tiles. will
   * be used for heuristics. Including direction.
   * @param start is initial loc
   * @param dest is ending loc
   * @param dir is initial direction
   */
  public int distance(Location start, Location dest, int dir) {
    int xDiff = Math.abs(start.c() - dest.c());
    int yDiff = Math.abs(start.r() - dest.r());
    int matDist = xDiff + yDiff;

    int dirDistance = 0;

    /*
      Calculates direction distance for heuristics.
      No punishment if goal is within quadrant of dir.
    */

    if (dir == 0) {     //NORTH
      //If facing north but same y level
      if (dest.r() == start.r()) {
        if (dest.c() < start.c())
          dirDistance += 3;
        else
          dirDistance += 1;
      }
      
      //If facing north and goal is south
      if (dest.r() > start.r()) {
        if (dest.c() < start.c())
          dirDistance += 3;
        else
          dirDistance += 2;
      }

      //If facing north but goal is northwest
      if (dest.r() < start.r())
        if (dest.c() < start.c())
          dirDistance += 4;
      
    }
    else if (dir == 1) { //EAST
      //If facing east but same x level
      if (dest.c() == start.c())    {
        if (dest.r() < start.r())
          dirDistance += 3;
        else
          dirDistance += 1;
      }

      //If facing east but goal is west
      if (dest.c() < start.c()) {
        if (dest.r() < start.r())
          dirDistance += 3;
        else
          dirDistance += 2;
      }

      //If facing east but goal is northeast
      if (dest.c() > start.c())
        if (dest.r() < start.r())
          dirDistance += 4;
    }
    else if (dir == 2) {    //SOUTH
      //If facing south but same y level
      if (dest.r() == start.r()) {
        if (dest.c() > start.c())
          dirDistance += 3;
        else
          dirDistance += 1;
      }
      
      //If facing south and goal is north
      if (dest.r() < start.r()) {
        if (dest.c() > start.c())
          dirDistance += 3;
        else
          dirDistance += 2;
      }

      //If facing south but goal is southeast
      if (dest.r() > start.r())
        if (dest.c() > start.c())
          dirDistance += 4;
    }
    else if (dir == 3) {    //WEST
      //If facing west but same x level
      if (dest.c() == start.c())    {
        if (dest.r() > start.r())
          dirDistance += 3;
        else
          dirDistance += 1;
      }

      //If facing west but goal is east
      if (dest.c() > start.c()) {
        if (dest.r() > start.r())
          dirDistance += 3;
        else
          dirDistance += 2;
      }

      //If facing west but goal is southwest
      if (dest.c() < start.c())
        if (dest.r() > start.r())
          dirDistance += 4;
    }
    

    return matDist + dirDistance;
  }

  /*
   * Finds the nearest gold tile to change to search based
   * on beacon's hints.
   * @param loc is current location of agent
   * @param dir is current direction of agent
   * @return location of closest gold tile
   */
  public Location findNearestGold(Location loc, int dir) {
    List<Location> posLoc = new ArrayList<Location>();
    int closestDist = 0;
    int greatestGold = 0;
    int nIndex = -1;

    //Gets all possible gold locations
    for (int i = 0; i < nGrid; i++) 
      for (int j = 0; j < nGrid; j++) 
        if (goldMemory[i][j] > 0) posLoc.add(new Location(i,j));
      
    if (posLoc.size() > 0) {
      closestDist = distance(loc, posLoc.get(0), dir);
      nIndex = 0;
      for (int i = 1; i<posLoc.size(); i++) {
        if (distance(loc, posLoc.get(i), dir) < closestDist) {
          closestDist = distance(loc, posLoc.get(i), dir);
          nIndex = i;
        }
      }

      return posLoc.get(nIndex);
    }
    else
      return null;      

  }

  /*
   * Calculates if it's worth scanning the row/column
   * if there's at least one unknown tile.
   * @param loc current location of agent.
   * @param dir for direction.
   * @return true if there is at least one unknown tile in the direction of the agent, otherwise false.
   */
  public boolean scanValue(Location loc, int dir) {

    if (dir==0) { //NORTH
      for (int y = loc.r()-1; y >=0; y--) {
        if (getDangerLevel(new Location(y, loc.c()))>=1)
          return true;
      }
    }
    else if (dir==1) { //EAST
      for (int x = loc.c()+1; x < nGrid; x++) {
        if (getDangerLevel(new Location(loc.r(), x))>=1)
          return true;
      }
    }
    else if (dir==2) { //SOUTH
      for (int y = loc.r()+1; y < nGrid; y++) {
        if (getDangerLevel(new Location(y, loc.c()))>=1)
          return true;
      }
    }
    else if (dir==3) { //WEST
      for (int x = loc.c()-1; x >= 0; x--) {
        if (getDangerLevel(new Location(loc.r(), x))>=1)
          return true;
      }
    }
    return false;
  }

  /*
   * Checks if there is a tile that is undiscovered within the
   * four directions of the agent. Mostly used to favour turning
   * in order to scan the undiscovered tile.
   * @param loc is the current location of the agent
   * @return true if there is an unknown tile, otherwise, false.
   */
  public boolean isNearUnknown(Location loc) {
    Location North = new Location(loc.r()-1,loc.c());
    Location East = new Location(loc.r(),loc.c()+1);
    Location South = new Location(loc.r()+1,loc.c());
    Location West = new Location(loc.r(),loc.c()-1);

    if (North.r() >= 0)
      if (getDangerLevel(North)==1)
        return true;
    
    if (South.r() < nGrid)
      if (getDangerLevel(South)==1)
        return true;

    if (East.c() < nGrid)
      if (getDangerLevel(East)==1)
        return true;

    if (West.c() >= 0)
      if (getDangerLevel(West)==1)
        return true;

    return false;
  }

  /*
   * Calculates the location of tiles to add from beacon.
   * @param beaconPos location of the beacon
   * @param distance is the manhattan distance calculated by gold manager.
   */
  public void setBeaconGrid(Location beaconPos, int distance){
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

    /*
    //Testing
    for(int x = 0; posLoc.size() > x; x++){
      Location printer = posLoc.get(x);
      System.out.println("Row = " + (printer.r() + 1) + "  Column = " + (printer.c() + 1));
    }*/

    //Adds all possible locations to gold memory
    //Getdangerlevel 0 is an assurance that it has been explored AND no pit/beacon/gold
    for (int i = 0; i < posLoc.size(); i++) {
      if (getDangerLevel(posLoc.get(i)) != 0) {
        //System.out.println("BEACON: "+posLoc.get(i).toString());
        
        //System.out.println("GOLD1: "+goldMemory[posLoc.get(i).r()][posLoc.get(i).c()]);

        goldMemory[posLoc.get(i).r()][posLoc.get(i).c()] += 1;

        //System.out.println("GOLD2: "+goldMemory[posLoc.get(i).r()][posLoc.get(i).c()]);
      }
    }

    //Gold memory debugger.
    /*
    System.out.println("\nGOOOLD");
    for (int i = 0; i < nGrid; i++) {
      for (int j = 0; j < nGrid; j++) {
        System.out.print(goldMemory[i][j]+" ");
      }
      System.out.println("");
    }
      */
  }
     
}

