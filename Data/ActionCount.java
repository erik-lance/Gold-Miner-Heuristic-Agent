package Data;

/*
 * A class integrated into agent purely for
 * counting all actions the agent performs.
 * Is set as a separate class in order to
 * organize data separately and be called
 * from other classes easier to avoid bloating
 * the GoldMiner java file.
 * 
 * Each setter function comes with an overloaded function
 * where setting without a parameter adds by one but
 * setting with a parameter sets it to the number passed.
 */
public class ActionCount {

  int pathCost;
  int scans;
  int turns;
  int moves;
  int backtracks;

  /*
   * Main constructor of counters for checking and
   * displaying.
   */
  public ActionCount() {
    this.pathCost = 0;
    this.scans = 0;
    this.turns = 0;
    this.moves = 0;
    this.backtracks=0;
  }

  /*
   * Cloning constructor for values of actions.
   * @param clone object to copy
   */
  public ActionCount(ActionCount clone) {
    this.pathCost   = clone.getPathCost();
    this.scans      = clone.getScans();
    this.turns      = clone.getTurns();
    this.moves      = clone.getMoves();
    this.backtracks = clone.getBacktracks();
  }

  /*
   * Adds pathcost by 1.
   */
  public void setPathCost() { this.pathCost += 1; }

  /*
   * Overloaded function setting pathcost manually.
   * @param set integer to manually override cost.
   */
  public void setPathCost(int set) { this.pathCost = set; }

  /*
   * Adds scan count by 1.
   */
  public void setScans() { this.scans += 1; }

  /*
   * Overloaded function setting scans manually.
   * @param set integer to manually override scans.
   */
  public void setScans(int set) { this.scans = set; }

  /*
   * Adds turn count by 1.
   */
  public void setTurns() { this.turns +=1; }

  /*
   * Overloaded function setting turn manually.
   * @param set integer to manually override turn.
   */
  public void setTurns(int set) { this.turns = set; }

  /*
   * Adds move count by 1.
   */
  public void setMoves() { this.moves +=1 ;}
  
  /*
   * Overloaded function setting move manually.
   * @param set integer to manually override move.
   */
  public void setMoves(int set) { this.moves = set; }

  /*
   * Adds backtrack count by 1.
   */
  public void setBacktracks() { this.backtracks +=1; }

  /*
   * Overloaded function setting backtrack manually.
   * @param set integer to manually override backtrack.
   */
  public void setBacktracks(int set) { this.backtracks = set; }

  /*
   * Gets path cost of agent
   * @return path cost of agent currently
   */
  public int getPathCost() { return this.pathCost; }

  /*
   * Gets scan count of agent
   * @return scan count of agent currently
   */
  public int getScans() { return this.scans; }

  /*
   * Gets turn count of agent
   * @return turn count of agent currently
   */
  public int getTurns() { return this.turns; }

  /*
   * Gets move count of agent
   * @return move count of agent currently
   */
  public int getMoves() { return this.moves; }

  /*
   * Gets backtrack count of agent
   * @return backtrack count of agent currently
   */
  public int getBacktracks() { return this.backtracks; }

}