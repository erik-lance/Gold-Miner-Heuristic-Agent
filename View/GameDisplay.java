package View;
import BoardModel.GoldMiner;
import BoardModel.MiningArea;
import Data.ActionCount;
/**
 * This class is responsible for the display of the whole game.
 * It grabs a reference to the main game's grid and translates
 * the instances into distinguishable symbols.
*/
public class GameDisplay{
  private MiningArea mGrid;

  /*
   * Unicode title screen that shows the title of the
   * simulation.
   */
  public void showTitle(){
    cls();
    System.out.println(Colors.BRIGHT_YELLOW+" \u2588\u2588\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2588\u2588\u2588\u2588\u2557 \u2588\u2588\u2557     \u2588\u2588\u2588\u2588\u2588\u2588\u2557 "+ /*Colors.RESET+*/"    \u2588\u2588\u2588\u2557   \u2588\u2588\u2588\u2557\u2588\u2588\u2557\u2588\u2588\u2588\u2557   \u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2557 ");
    System.out.println(Colors.BRIGHT_YELLOW2+"\u2588\u2588\u2554\u2550\u2550\u2550\u2550\u255D \u2588\u2588\u2554\u2550\u2550\u2550\u2588\u2588\u2557\u2588\u2588\u2551     \u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557"+ /*Colors.RESET+*/ "    \u2588\u2588\u2588\u2588\u2557 \u2588\u2588\u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u2550\u2550\u255D\u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557");
    System.out.println(Colors.BRIGHT_YELLOW3+"\u2588\u2588\u2551  \u2588\u2588\u2588\u2557\u2588\u2588\u2551   \u2588\u2588\u2551\u2588\u2588\u2551     \u2588\u2588\u2551  \u2588\u2588\u2551"+ /*Colors.RESET+*/"    \u2588\u2588\u2554\u2588\u2588\u2588\u2588\u2554\u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2554\u2588\u2588\u2557 \u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255D");
    System.out.println(Colors.BRIGHT_YELLOW4+"\u2588\u2588\u2551   \u2588\u2588\u2551\u2588\u2588\u2551   \u2588\u2588\u2551\u2588\u2588\u2551     \u2588\u2588\u2551  \u2588\u2588\u2551"+ /*Colors.RESET+*/"    \u2588\u2588\u2551\u255A\u2588\u2588\u2554\u255D\u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2551\u255A\u2588\u2588\u2557\u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u255D  \u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557");
    System.out.println(Colors.BRIGHT_YELLOW5+"\u255A\u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255D\u255A\u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255D\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255D" + /*Colors.RESET+*/"    \u2588\u2588\u2551 \u255A\u2550\u255D \u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2551 \u255A\u2588\u2588\u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2551  \u2588\u2588\u2551");
    System.out.println(Colors.BRIGHT_YELLOW5+" \u255A\u2550\u2550\u2550\u2550\u2550\u255D  \u255A\u2550\u2550\u2550\u2550\u2550\u255D \u255A\u2550\u2550\u2550\u2550\u2550\u2550\u255D\u255A\u2550\u2550\u2550\u2550\u2550\u255D "+ /*Colors.RESET+*/"    \u255A\u2550\u255D     \u255A\u2550\u255D\u255A\u2550\u255D\u255A\u2550\u255D  \u255A\u2550\u2550\u2550\u255D\u255A\u2550\u2550\u2550\u2550\u2550\u2550\u255D\u255A\u2550\u255D  \u255A\u2550\u255D" + Colors.RESET);

    System.out.println();
  }

  /*
   * Debug/maintenance texts to view agent's progress
   * in the simulation.
   * @param agent reference to observe.
   */
  public void showAgentInfo(GoldMiner agent){
    ActionCount aCounter = agent.getCounter();
    System.out.println("\n["+Colors.BLUE+"Gold Miner"+ Colors.RESET+ " info]");

    System.out.print("Scans: "+aCounter.getScans());
    System.out.println("\t\tLocation: "+ agent.getLoc());

    System.out.print("Turns: "+aCounter.getTurns());
    System.out.println("\t\tPrev loc: "+ agent.getPrevLoc());

    System.out.print("Moves: "+aCounter.getMoves());
    System.out.println("\t\tFront: "+ agent.getFront());

    System.out.print("Backs: "+aCounter.getBacktracks());
    System.out.println("\t\tAction: "+ agent.getAction());

    System.out.println(Format.BOLD+Format.REVERSED+"Total: "+aCounter.getPathCost()+" "+Format.RESET);
  }

  /**
   * Main dialogue/screen to show if agent succeeds.
   */
  public void showAgentSuccess(){
     System.out.println("\n"+Colors.BLUE+"Gold Miner"+ Colors.RESET+ " has found "+ Colors.YELLOW +"Gold " + Symbols.GOLD_SQUARE+ " !");
  }

  /**
   * Main dialogue/screen to show if agent failed.
   */
  public void showAgentFail(){
     System.out.println("\n"+Colors.BLUE+"Gold Miner"+ Format.BOLD + Colors.RED+ " failed" + Format.RESET + " to find "+ Colors.YELLOW +"Gold " + Symbols.GOLD_SQUARE+ ".");
  }

  /**
   * Sets the reference mining area being observed
   * @param area mining area reference to check
   */
  public void setMiningArea(MiningArea area){
    mGrid = area;
  }

  /**
   * Displays the main mining area into the terminal.
   */
  public void showMiningArea(){
    cls();
    System.out.print("     ");
    for(int col = 0; col<mGrid.getCol(); col++){
      System.out.print(String.format("%s%s", col+1, (col+1 < 9) ? "   " : "  "));
    }
    System.out.println();
    printHln();
    for(int i=0; i<mGrid.getRow(); i++){
      System.out.print(String.format("\n%2s %s ", i+1, Symbols.VERT_LINE));
      for(int j=0; j<mGrid.getCol(); j++){
        if(mGrid.getArea()[i][j] == null)
          System.out.print(String.format("  %s ", Symbols.VERT_LINE));
        else
          System.out.print(String.format("%s %s ",mGrid.getArea()[i][j], Symbols.VERT_LINE));
      }
      System.out.println();
      printHln();
    }
  }

  /**
   * Prints a horizontal or new line into the terminal.
   */
  public void printHln(){
    System.out.print("   -----");
    for(int hl=0; hl<mGrid.getCol()-1; hl++)
      System.out.print("----");
  }

  /**
  * This will clear the screen on console.
  * <br>
  * REFERENCE:
  * <br>
  * How to Clear Screen in Java - Javatpoint. (n.d.). Retrieved July 25, 2021, from https://www.javatpoint.com/how-to-clear-screen-in-java
  */
  public static void cls() {  
      System.out.print("\033[H\033[2J");  
      System.out.flush();  
  }  
  
}