package Data;
import BoardModel.*;
import Tiles.*;

//for debugging
import View.Colors;

/*
 * Serves as the data storage for the agent in the
 * form of a tree. Contains a state of the game through
 * agent in every node. Simulates the possible situation
 * per node as a clone of every object in the game.
 *
 * These are traversed by exploring a node instead of loading
 * all preloaded nodes already.
*/
public class Node {
  //private static int DEPTHLIMIT = 12;
  private Node root;
  private Node leftNode;
  private Node midNode;
  private Node rightNode;
  private int pathCost;
  private int hValue;

  private int depth;
  private boolean scanned;
  private boolean[] scanDir;
  private GoldMiner agent;

  private boolean explored;
  private boolean foundGold;
  private boolean lockMove;
  private boolean bcnFound;

  private int turnCount;
  private int nodePath;

  /**
   * This is only used for the root node to instantiate
   * the entire tree.
   * @param agent is a gold miner containing the entire state of the game with its reference to mining area. Although without direct reference to the tile types we can use 
   */
  public Node(GoldMiner agent) {
    this.agent = new GoldMiner(agent);
    this.depth = 0;
    this.turnCount = 0;
    this.scanDir = new boolean[4];
    this.scanDir[0] = false;
    this.scanDir[1] = false;
    this.scanDir[2] = false;
    this.scanDir[3] = false;

    this.lockMove = false;
    this.foundGold = false;
    this.bcnFound = false;

    pathCost = 1;
    explored = false;
    scanned = false;
    insertExploreNodes();
  }

  /*
   * Node heuristic variation through exploration.
   * The node path is set through other functions
   * which insert these nodes.
   * @param root parent to branch out of
   */
  public Node(Node root) {
    this.root = root;
    this.agent = new GoldMiner(root.getAgent());
    this.depth = root.getDepth()+1;
    this.turnCount = root.getTurn();
    this.explored = false;
    this.scanDir = new boolean[4];
    this.scanDir[0] = root.getScanDir(0);
    this.scanDir[1] = root.getScanDir(1);
    this.scanDir[2] = root.getScanDir(2);
    this.scanDir[3] = root.getScanDir(3);

    this.bcnFound = root.bcnFound;
    this.lockMove = root.lockMove;
    this.foundGold = root.foundGold;
    this.updatePathCost();
  }

  /*
   * Checks into a child node at a specific position
   * @param nodePos to know which node to look into
   */
  public void exploreNode(int nodePos) {
    if (nodePos == 0) {
      this.leftNode.explored = true;
      this.leftNode.insertExploreNodes();
    }
    else if (nodePos ==1) {
      this.midNode.explored = true;
      this.midNode.insertExploreNodes();
    }
    else if (nodePos == 2) {
      this.rightNode.explored = true;
      GoldMiner rAgent = this.rightNode.getAgent();
      Location aLoc = rAgent.getLoc();
      MiningArea mRef = rAgent.getMA();
      MemoryBank memoRef = rAgent.getMemo();
      
      //Checks if move node is in beacon
      //Recalculates if gold candidate is possible as well.
      if (mRef.getTile(aLoc) instanceof Beacon && memoRef.getBcn(aLoc) >= 0) {
        memoRef.setBeaconGrid(aLoc, ((Beacon) mRef.getTile(aLoc)).getGoldDistance());

        this.rightNode.setLock(false);
        this.rightNode.setBcnLock(true);

        //Makes beacon unusable.
        //memoRef.getBcn()[aLoc.r()][aLoc.c()] = -1;

        //Converts beacon to a normal tile.
        mRef.setTile(aLoc);
      }
    
      this.rightNode.insertExploreNodes();
    }
  }

  /*
   * Calculates heuristic based on state of game and
   * type of node passed through a set of rules. Performs
   * operations on current node after calculating the state.
   * @param nodePos the position of node to determine which action was taken previously.
   */
  public int calculateHeuristic(int nodePos) {
    MemoryBank memoRef = this.agent.getMemo();
    MiningArea miningRef = this.agent.getMA();
    Location aLoc = this.agent.getLoc();
    Location aFront = this.agent.getFrontLoc();
    int aDir = this.agent.getDir();
    //This is the accumulated cost after calculations.
    int accumCost = 1;

    //Rule add heuristic based on number of unkown tiles. 
    int numUnk = this.root.agent.getMemo().getUnknown();

    //Assures the tile moved into is set empty.
    memoRef.setEmpty(aLoc);

    //[Left Node] Scan Heuristics
    if (nodePos==0) {
      //Checks viability by scan's amount of tiles that can be uncovered.
      GoldMiner rootMiner = this.root.getAgent();
      Location rLoc = rootMiner.getLoc();
      int rDir = rootMiner.getDir();

      accumCost -= rootMiner.getMemo().getUnknown(rLoc, rDir);

      //If gold hasn't been found, scan must recheck if gold is real
      if (this.root.getBcnLock() && !this.root.hasFoundGold()) {
        int nearestType = rootMiner.getMemo().getClosestType(rLoc, rDir);
        if      (nearestType == 0)  return 1; //Gold
        else if (nearestType == 1)  return 2; //Beacon
        else if (nearestType == 2)  return 3; //Pit
      }
    }

    //[Middle Node] Turning Heuristics
    if (nodePos==1) {
      //Rule 6
      if (this.turnCount < 3) accumCost+= this.turnCount-1;
      else                    accumCost+= (this.turnCount*2);

      if (aFront == null)
        accumCost+= 1;

      //Rule, if turn ends near unknown tile, remove punishment to reward scanning
      if (memoRef.isNearUnknown(aLoc))
        accumCost -= this.turnCount;

      //If beacon used, tries to look for nearest gold and if
      //turn assists in being closer to gold.
      if (getBcnLock()) {
        Location gNear = memoRef.findNearestGold(aLoc, aDir);
        
        int gDist = 0;

        if (gNear != null) {
          gDist = memoRef.distance(aLoc, gNear, aDir);
          accumCost += gDist;
        }
      }

      //Favour down/right direction
      //SPECIAL CONDITION IF PIT BOTTOM
      if (aLoc.r() < miningRef.getRow()-1) {
        Location botLoc = new Location(aLoc.r()+1, aLoc.c());
        if (aLoc.c() < miningRef.getCol()-1) {
          if (memoRef.getDangerLevel(botLoc) > 1) {
            if (aDir==2)
              accumCost+=1;
            //Favours transitioning to right
            if (aDir==0)
              accumCost-=1;
            if (aDir==1)
              accumCost-=2;
          }
        }
        else { //Special if far right
          if(memoRef.getDangerLevel(botLoc) > 1) {
            if (aDir==2)
              accumCost-=2;
            if (aDir==3) 
              accumCost+=3;
          }
        }
        
      }
      
      GoldMiner rAgent = this.root.getAgent();
      Location rFront = rAgent.getFrontLoc();

      //Encourages agent to turn away from danger to avoid moving into danger
      if (rFront != null) {
        int rDanger = rAgent.getMemo().getDangerLevel(rFront);
        if (rAgent.getMemo().getDangerLevel(rFront) > 1) {
          accumCost -= rDanger*5;
        }

      }

      //BOTTOM ROW HEURISTICS SINCE AGENT FAVOURS DIAGONAL
      if (aDir == 2 && aLoc.r() == miningRef.getRow()-1) {
        //If a lot of tiles to scan up
        if (memoRef.getUnknown(aLoc, 0) > 0) {
          accumCost -= 2;
        }
      }

      //RIGHT COLUMN HEURISTICS SINCE AGENT FAVOURS DIAGONAL
      if (aDir == 2 && aLoc.c()==miningRef.getCol()-1) {
        //If a lot of tiles to scan left
        if (memoRef.getUnknown(aLoc,3) > 0) {
          accumCost -= 2;
        }
      }


    }


    //[Right Node] Moving Heuristics
    if (nodePos==2) {
      GoldMiner rAgent = this.root.getAgent();
      Location rTile = rAgent.getFrontLoc();

      

      int nDanger = rAgent.getMemo().getDangerLevel(rTile);
      boolean exploredTile = miningRef.getTile(rTile).hasTravelled();

      if (getBcnLock()) {
        Location gNear = memoRef.findNearestGold(aLoc, aDir);
        int gDist = 0;

        if (gNear != null) {
          gDist = memoRef.distance(aLoc, gNear, aDir);
          accumCost += gDist;
        }
      }

      //Avoid backing to topleft corner early-game
      //and if not in far column.
      if (numUnk > miningRef.getCol()/2) {
        if (aLoc.c() < miningRef.getCol()-1) {
          if (aDir == 0) {
            accumCost +=7;
          }
          if (aDir == 3) {
            accumCost +=4;
          }
        }
        
      }

      //Avoid moving up if safe already and traversing middle or higher.
      if (aDir==0 && !memoRef.scanValue(aLoc, 0) && aLoc.r()<miningRef.getRow()/2) {
        accumCost+= 10;
      }

      //Rule Beacon Lock move
      if (this.lockMove)  { return 1; }
      //Rule Gold Lock move
      if (this.foundGold) { return 1; }
      //Rule and If tile unknown, multiply by danger
      if (nDanger > 0) { 
        //If pit and gold/bcn are same row/col, scan first
        if (rAgent.getMemo().getGoldLevel(rTile)>0) 
          accumCost -= 1;
        else if (rAgent.getMemo().getBcn(rTile)>1) 
          accumCost -= 1;
        else
          accumCost+= 5*nDanger; 
      }
      //Rule If tile is explored, +3*Number of traverses  

      if (exploredTile) { 
        int nTraverses = miningRef.getTile(aLoc).getTraverses();
        
        if (nTraverses < 3 && aLoc.c() < miningRef.getCol()-1)
          accumCost += 5*nTraverses;
        else
          accumCost += 10*nTraverses;
      
      }
      
    }
    
    //Rule, unscanned tiles added to encourage scanning
    return accumCost + numUnk;
  }

  /*
   * Inserts nodes into the node. Only called when
   * tree explores said node to open branching
   * nodes into.
   */
  public void insertExploreNodes() {

    boolean empty = false;

    /*---------- [SCAN] Left Node ----------*/
    if (!this.getScanDir(this.getAgent().getDir())) {
      Node scan = new Node(this);
      GoldMiner sAgent = scan.getAgent();
      Location sLoc = sAgent.getLoc();
      int sDir = sAgent.getDir();

      int scanType = sAgent.scan();
      
      scan.setScanDir(sAgent.getDir(), true);

      //Note may interfere with beacon values. Locks gold.
      if (scanType==0) {
        scan.setGoldLock(true);
        scan.setLock(true);
      }

      //If beacon found
      if (scanType==1) {
        scan.setLock(true);
      }
      
      //COMPARE TILE SCANS W MEMORY
      int closestTile = sAgent.getMemo().getClosestType(sLoc, sAgent.getDir());

      System.out.println("SCANTYPE[left]: "+scanType);
      System.out.println("CLOSESTT[left]: "+closestTile);

      // Removes fake memory in situations where tiles are in same row/column for accuracy.
      // sample: If closest memory is gold, but scan is !gold:
      if (scanType != closestTile) {
        
        System.out.println("SCANTYPE: "+scanType);
        System.out.println("CLOSESTT: "+closestTile);
        
        sAgent.getMemo().removeClosestType(sLoc, sDir, closestTile);
      }

      scan.setNodePath(0);
      scan.setHeuristic(scan.calculateHeuristic(0));
      this.leftNode = scan;
    }

    /*---------- [TURN] Middle Node ----------*/
    if (turnCount < 6) {
      Node turn = new Node(this);
      turn.agent.rotate();

      
      
      //Turn counter to avoid useless 360s.
      turn.setTurn();

      //Checks if tile in front is safe to close scan node.
      if (turn.agent.getFrontLoc()==null) 
        turn.setScanDir(turn.getAgent().getDir(),true);
      else if (!turn.agent.getMemo().scanValue(turn.agent.getLoc(),turn.agent.getDir()))
          turn.setScanDir(turn.getAgent().getDir(),true);
      
      turn.setNodePath(1);
      turn.setHeuristic(turn.calculateHeuristic(1));
      this.midNode = turn;
    }

    /*---------- [MOVE] Right Node ----------*/
    if (this.agent.getFrontLoc() != null) {
      Node move = new Node(this);
      boolean stillScanned = this.getScanDir(this.getAgent().getDir());
      move.agent.move();
      move.setScanDir(0, false);
      move.setScanDir(1, false);
      move.setScanDir(2, false);
      move.setScanDir(3, false);

      move.setTurn(0); //resets counter
      Location agentFront = move.agent.getFrontLoc();


      if (agentFront == null)
        move.setScanDir(move.getAgent().getDir(),true);
      else if (!move.agent.getMemo().scanValue(move.agent.getLoc(),move.agent.getDir()))
        move.setScanDir(move.getAgent().getDir(),true);
      else if (stillScanned)
        move.setScanDir(move.getAgent().getDir(),true);

      

      move.setNodePath(2);
      move.setHeuristic(move.calculateHeuristic(2));
      move.agent.getMemo().setEmpty(move.agent.getLoc());
      this.rightNode = move;
    }
  }

  /**
   * Adds a path cost to all previous parent nodes. Goes through every parent nodes previously set until it reaches the tree's root node where it doesn't have a root node anymore.
   */
  public void updatePathCost() {
    Node parent = this.root;
    do{
      parent.pathCost += 1;
      parent = parent.getRoot();
    }while (parent != null);
  }

  /**
   * In an instance where pit or not a solution, removes
   * added path cost from recursion.
   * @param nodePos position in parent's children.
   */
  public void deletePath(int nodePos) {
    Node parent = this.root;

    if      (nodePos == 0) {parent.leftNode = null;}
    else if (nodePos == 1) {parent.midNode = null;}
    else if (nodePos == 2) {parent.rightNode = null;}

    do{
      parent.pathCost -= 1;
      parent = parent.getRoot();
    }while (parent != null);
    
  }

  /*
   * Returns the status of foundGold
   * @return if gold has been found or not thru scan.
   */
  public boolean hasFoundGold() {return foundGold; }

  /*
   * Returns the status of locked move
   * @return if move is locked on, otherwise false.
   */
  public boolean isLocked() {return lockMove; }

  /*
   * Returns a reference to agent of node.
   * @return agent reference in current node.
   */
  public GoldMiner getAgent(){ return this.agent; }

  /*
   * Gets status if it doesn't need to scan or if it doesn
   * @return true if no need to scan, else false.
   */
  public boolean getScan() { return this.scanned; }

  /*
   * Gets the position of the node in the three.
   * @return nodePath position in children
   */
  public int getNodePath() { return this.nodePath; }

  /*
   * Grabs the specific node at a certain path
   * @param pos integer of child's position
   * @return node at certain position
   */
  public Node getNodeAt(int pos) {
    if      (pos == 0) {return this.leftNode; }
    else if (pos == 1) {return this.midNode; }
    else if (pos == 2) {return this.rightNode; }
    else                return null;
  }

  /*
   * Gets current number of turns performed
   * @return turns of agent
   */
  public int getTurn() { return this.turnCount; }
  
  /*
   * Gets the heuristic value/cost of node to perform
   * @return heuristic value
   */
  public int getHeuristic() { return this.hValue; }

  /**
   * Gets the depth of current node. Can be used for IDS or for checking into possible infinite loops.
   * @return depth of node
   */
  public int getDepth() { return this.depth; }

  /**
   * Gets the current path cost of node from previous node
   * @return path cost to previous node
   */
  public int getPathCost() { return this.pathCost; }

  /*
   * Gets the status of node if it has been explored or not.
   * @return true if explored, else false.
   */
  public boolean getExplored() {return this.explored;}

  /*
   * Gets the status of beacon if it has been found or not.
   * @return true if found, else false.
   */
  public boolean getBcnLock() { return this.bcnFound; }

  /**
   * Gets the root or parent node of current node. Mostly used for recursive pathcost calculations or references to MiningArea
   * @return parent of current node
   */
  public Node getRoot() { return this.root; }

  /*
   * Gets the node at the [scan] left child of this node.
   * @return [scan] leftNode of this node.
   */
  public Node getLeftNode() { return this.leftNode; }

  /*
   * Gets the node at the [turn] middle child of this node.
   * @return [turn] midNode of this node.
   */
  public Node getMidNode() { return this.midNode; }

  /*
   * Gets the node at the [move] right child of this node.
   * @return [move] rightNode of this node
   */
  public Node getRightNode() { return this.rightNode; }

  /*
   * Checks if node's children exist.
   * @return true if no children, else return false.
   */
  public boolean isEmpty() {
    return (this.leftNode==null && this.midNode==null && this.rightNode==null);
  }

  /*
   * Checks if scanned already in said direction.
   * @param dir direction of agent
   * @return true if scanned, else false
   */
  public boolean getScanDir(int dir) {
    if (dir >= 0 && dir < 4)
      return this.scanDir[dir];
    else
      return false;
  }

  /*
   * Sets scan of direction if already scanned.
   * @param dir direction of agent
   * @param done if agent scanned or not in said direction.
   */
  public void setScanDir(int dir, boolean done) {
    if (dir >= 0 && dir < 4)
      this.scanDir[dir] = done;
  }

  /*
   * Sets the scan condition if agent shouldn't try to scan.
   * @param scanTry true if there is no need to scan, else false.
   */
  public void setScan(boolean scanTry) { this.scanned = scanTry; }

  /*
   * Sets the node path position of this node to its parent.
   * @param nPath integer denoting its position (left/mid/right) at 0/1/2 respectively.
   */
  public void setNodePath(int nPath) { this.nodePath = nPath; }

  /*
   * Sets the value of the turncount for heuristic and logic Checks
   * @param count number of turns currently
   */
  public void setTurn(int count) { this.turnCount = count; }

  /*
   * Overloading function to simply add turncount instead.
   */
  public void setTurn() {this.turnCount+=1;}

  /*
   * Sets the heuristic value on path to this node.
   * @param hVal is the value of the heuristic on this node
   */
  public void setHeuristic(int hVal) { this.hValue = hVal; }

  /*
   * Sets a lock on move if gold has been found
   * @param lock the decision on locking move.
   */
  public void setGoldLock(boolean lock) { this.foundGold = lock; }

  /*
   * Sets a lock on move if a priority 
   * target has been found
   * @param lock the decision on locking move.
   */
  public void setLock (boolean lock) { this.lockMove = lock; }

  /*
   * Sets whether beacon has been found or not
   * @param lock to set if beacon has been found or not
   */
  public void setBcnLock (boolean lock) { this.bcnFound = lock;}
}