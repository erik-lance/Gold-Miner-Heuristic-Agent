package BoardModel;

import Data.*;
import Tiles.*;
import View.*;

import java.util.*;

/**
 * The main gold miner agent which will traverse
 * the mining area. It houses a scan/move/turn
 * function which serve as tools in traversing
 * the mining area. It also has two types of
 * rationality with a random and smart variant.
 */
public class GoldMiner {

  public static final Location defaultLoc = new Location(0, 0);

  private Location pt;
  private Location prevPt;

  private int direction;
  private MiningArea mGrid;
  private MemoryBank agentMemo;
  private ActionCount aCounter;

  private boolean lockedMove;
  private Queue<Node> pathway; 
  private Tree tree;
  private int action; //the node path

  //visual debugging
  private boolean showHighlight;
  private Beacon exploredBcn;


  /**
   * Constructs the agent of the environment. Always set at (1,1) to start and
   * currently faces east.
   * 
   * @param mGrid grants miner a referece to the mining area.
   */
  public GoldMiner(MiningArea mGrid) {
    this.aCounter = new ActionCount();
    pt = defaultLoc;
    direction = 1; // RIGHT
    this.mGrid = mGrid;
    agentMemo = new MemoryBank(mGrid.getCol());
  }

  /**
   * An overloading function that allows for special cases to do maintenance
   * testing with the agent.
   * 
   * @param mGrid grants miner a referece to the mining area.
   * @param loc   sets the coordinates of the agent
   * @param dir   sets the direction the agent will face
   */
  public GoldMiner(MiningArea mGrid, Location loc, int dir) {
    this.aCounter = new ActionCount();
    this.pt = loc;
    direction = dir;
    this.mGrid = mGrid;
    agentMemo = new MemoryBank(mGrid.getCol());
  }

  /**
   * This constructor creates a new agent object for
   * the sake of possible new routes to avoid referencing
   * the same agent in every node.
   * @param cloneAgent a new instance of goldminer to copy
   */
  public GoldMiner(GoldMiner cloneAgent) {
    this.aCounter = new ActionCount(cloneAgent.getCounter());

    this.pt = new Location(cloneAgent.getLoc());
    if (cloneAgent.getPrevLoc() != null)
      this.prevPt = new Location(cloneAgent.getPrevLoc());

    this.direction = cloneAgent.getDir();
    this.mGrid = new MiningArea(cloneAgent.getMA());
    this.agentMemo = new MemoryBank(cloneAgent.getMemo());

    this.lockedMove = cloneAgent.lockedMove;
    this.pathway = cloneAgent.pathway;
    this.tree = cloneAgent.tree;

    this.action = cloneAgent.action;
  }

  /*
   * Moves the miner in the direction facing.
   * @return move if forward tile is valid
   */
  public boolean move() {

    if (getFrontLoc()==null)
      return false;
    else {
      aCounter.setMoves();

      mGrid.getTile(this.pt).setAgent(null);
      mGrid.getTile(this.pt).setTravelled(true);
      prevPt = pt;
      setLoc(new Location(getFrontLoc()));
      mGrid.getTile(this.pt).setAgent(this);
      //System.out.println("[" + direction + "]Moving from " + prevPt + " to " + pt);

      //visual debugging
      if (mGrid.getTile(this.pt) instanceof Beacon){
        showHighlight = true;
        exploredBcn = (Beacon) mGrid.getTile(this.pt);
      }

      //Updates the highlights of possible gold locations
      if(showHighlight)
        for(Location loc : exploredBcn.getRegion(mGrid.getSize()))
          mGrid.getTile(loc).highlighted = agentMemo.getDangerLevel(loc) == 1;

      return true;
    }
  }

  /*
   * Scans for possible unique tiles in front of agent
   * @return type scanned [0,1,2] (Gold/Beacon/Pit)
   */
  public int scan() {
    aCounter.setScans();
    boolean exists = false;
    
    if (direction == 0) { // NORTH
      for (int y = pt.r() - 1; y >= 0; y--) {
        if (mGrid.getTile(new Location(y, pt.c())) instanceof Pit) {
          agentMemo.scanPit(this.pt, direction);
          exists = true;
          return 2;
        } else if (mGrid.getTile(new Location(y, pt.c())) instanceof Beacon) {
          agentMemo.scanBcn(this.pt, direction);
          exists = true;
          return 1;
        }
        else if (mGrid.getTile(new Location(y, pt.c())) instanceof GoldSquare) {
          agentMemo.scanGold(this.pt, direction);
          exists = true;
          return 0;
        }
      }
    } else if (direction == 1) { // EAST

      for (int x = pt.c() + 1; x < mGrid.getCol(); x++) {
        if (mGrid.getTile(new Location(pt.r(), x)) instanceof Pit) {
          agentMemo.scanPit(this.pt, direction);
          exists = true;
          return 2;
        } else if (mGrid.getTile(new Location(pt.r(), x)) instanceof Beacon) {
          agentMemo.scanBcn(this.pt, direction);
          exists = true;
          return 1;
        }
        else if (mGrid.getTile(new Location(pt.r(), x)) instanceof GoldSquare) {
          agentMemo.scanGold(this.pt, direction);
          exists = true;
          return 0;
        }
      }
    } else if (direction == 2) { // SOUTH
      for (int y = pt.r() + 1; y < mGrid.getCol(); y++) {
        if (mGrid.getTile(new Location(y, pt.c())) instanceof Pit) {
          agentMemo.scanPit(this.pt, direction);
          exists = true;
          return 2;
        } else if (mGrid.getTile(new Location(y, pt.c())) instanceof Beacon) {
          agentMemo.scanBcn(this.pt, direction);
          exists = true;
          return 1;
        }
        else if (mGrid.getTile(new Location(y, pt.c())) instanceof GoldSquare) {
          agentMemo.scanGold(this.pt, direction);
          exists = true;
          return 0;
        }
      }
    } else if (direction == 3) { // WEST
      for (int x = pt.c() - 1; x >= 0; x--) {
        if (mGrid.getTile(new Location(pt.r(), x)) instanceof Pit) {
          agentMemo.scanPit(this.pt, direction);
          exists = true;
          return 2;
        } else if (mGrid.getTile(new Location(pt.r(), x)) instanceof Beacon) {
          agentMemo.scanBcn(this.pt, direction);
          exists = true;
          return 1;
        }
        else if (mGrid.getTile(new Location(pt.r(), x)) instanceof GoldSquare) {
          agentMemo.scanGold(this.pt, direction);
          exists = true;
          return 0;
        }
      }
    }
    if(!exists)
      agentMemo.scanEmpty(this.pt, direction);

    //Updates the highlights of possible gold locations
    if(showHighlight)
      for(Location loc : exploredBcn.getRegion(mGrid.getSize()))
        mGrid.getTile(loc).highlighted = agentMemo.getDangerLevel(loc) == 1;

    return -1;
  }

  /**
   * This rotates the face goldminer is staring. Only makes sharp right turns. Can
   * only face in four directions.
   */
  public void rotate() {
    aCounter.setTurns();

    this.direction++;
    if (direction > 3)
      direction = 0;
  }

  /*
   * Initializes the Random Agent for traversal
   */
  public void initializeTreeRandom(){
    tree = new Tree(this);    
    pathway =new LinkedList<>(tree.searchRandom());
  }

  /*
   * Initializes the Smart Agent for traversal
   */
  public void initializeTreeSmart(){
    tree = new Tree(this);    
    pathway =new LinkedList<>(tree.searchLowCost());
  }

  public boolean act(){
    Node n = pathway.remove();
    aCounter.setPathCost();

    if (mGrid.getTile(pt) instanceof Pit){
      aCounter.setBacktracks();
      mGrid.getTile(this.pt).setAgent(null);
      mGrid.getTile(this.pt).setTravelled(true);
      setLoc(n.getAgent().getLoc());
      mGrid.getTile(this.pt).setAgent(this);
      prevPt = n.getAgent().getPrevLoc();
    }

    if(!this.getLoc().equals(mGrid.getGoldLoc())){
      action = n.getNodePath();

      if      (action == 0) scan();
      else if (action == 1) rotate();
      else                  move();

      return true;
    }
    else
      return false;
  }


  /*********************************************
   * Everything beyond this are getters/setters
   * included in gold miner for referencing and
   * setting values. Keep all other functions
   * before this comment to keep consistency
   * and cleanliness.
   ********************************************/

  /*
   * Sets the memorybank reference of the agent.
   * @param memo reference to store in agent.
   */
  public void setMemo(MemoryBank memo) {
    this.agentMemo = memo;
  }

  /**
   * Sets the current position GoldMiner is in.
   * @param pt is a location object with coordinates.
   */
  public void setLoc(Location pt) {
    this.pt = new Location(pt.r(),pt.c());
  }
  
  /**
   * Sets the lock on gold agent if it has
   * found gold or to generally lock its actions
   * to moving only.
   * @return true if locked, else return false.
   */
  public void setLocked(boolean lock) {
    this.lockedMove = lock;
  }

  /*
   * Gets the memorybank stored in agent's data for referencing.
   * @return MemoryBank inside agent.
   */
  public MemoryBank getMemo() { return agentMemo; }

  /*
   * Gets the mining area stored in agent's data for referencing.
   * @return MiningArea inside agent.
   */
  public MiningArea getMA() { return mGrid; }

  /*
   * Gets the direction of agent in terms of 0/1/2/3.
   * Follows NORTH/EAST/SOUTH/WEST respectively.
   * @return direction the agent is facing numerically.
   */
  public int getDir() { return direction; }

  /**
   * Gets the direction this agent is facing in string format.
   * @return string direction of the agent
   */
  public String getFront() {
    String front = "";

    if      (direction == 0)
      front = "UP "+ Colors.GREEN+"\uD83E\uDC79"+Colors.RESET;
    else if (direction == 1)
      front = "RIGHT "+ Colors.GREEN+"\uD83E\uDC7A"+Colors.RESET;
    else if (direction == 2)
      front = "DOWN "+ Colors.GREEN+"\uD83E\uDC7B"+Colors.RESET;
    else if (direction == 3)
      front = "LEFT "+ Colors.GREEN+"\uD83E\uDC78"+Colors.RESET;

    return front;
  }

  /*
   * Gets the location of tile in front of agent
   * @return location of tile in front.
   */
  public Location getFrontLoc() {
    int x = 0;
    int y = 0;

    if (this.direction == 0) {
      x = 0;
      y = -1;
    } else if (this.direction == 1) {
      x = 1;
      y = 0;
    } else if (this.direction == 2) {
      x = 0;
      y = 1;
    } else if (this.direction == 3) {
      x = -1;
      y = 0;
    }

    Tile newTile = mGrid.getTile(new Location(pt.r()+y, pt.c()+x));

    if (newTile != null)
      return new Location(newTile.getLoc());
    else
      return null;
  }

  /**
   * Gets the location this agent.
   * @return coords of type Location
   */
  public Location getLoc() { return pt; }

  /*
   * Gets the previous location stored in agent's data for referencing.
   * @return previous location of type Location.
   */
  public Location getPrevLoc() { return prevPt; }

  /**
   * Gets the locked boolean to see if agent is
   * locked on moving forward.
   * @return true if agent is locked, else false.
   */
  public boolean getLocked() { return this.lockedMove; }

  /*
   * Gets the counter class that counts all actions of agent.
   * @return action counter of agent.
   */
  public ActionCount getCounter() { return this.aCounter;}

  /*
   * This gets the current action being performed by the agent.
   * Only used for debugging and GUI purposes.
   * @param string action performed by agent namely SCAN/TURN/MOVE.
   */
  public String getAction(){
    String str="";

    if      (this.action==0) str = Colors.RED + "SCAN" + Colors.RESET;
    else if (this.action==1) str = Colors.YELLOW + "TURN" + Colors.RESET;
    else                     str = Colors.PURPLE + "MOVE" + Colors.RESET;

    return str;
  }

  /*
   * Overrides the toString function to help with display on the gamedisplay.
   * @return agent's symbol based on class Symbols.
   */
  @Override
  public String toString() {
    return Symbols.AGENT;
  }
}