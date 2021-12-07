package BoardModel;
import Tiles.*;
import View.*;
import Data.Location;

import java.util.*;
import java.math.*;

/**
 * The game master responsible for every function stitching and
 * communication between the components.
 */
public class GameManager{

  public static final int MIN = 8;
  public static final int MAX = 64;
  public static final int DELAY = 500;
  private int SIZE;

  public MiningArea miningArea;
  public GoldMiner agent;
  public GoldSquare gsq;
  private GameDisplay view;

  private float pitMulti = 0.25f;
  private float bcnMulti = 0.1f;

  private char level;
  private boolean isFast;

  /**
   * Constructs the main game controller. There is no need for
   * any parameters to pass as it will instantiate all needed
   * objects to prepare the environment.
  */
  public GameManager(){
    
    view = new GameDisplay ();
    startScreen();
  }
  
  /**
   * The GUI for the Gold Miner. This is the start screen that
   * appears on run and asks for the size of grid. It is also
   * responsible for transitioning to the main board.
  */
  private void startScreen(){
    int step = 1;
    boolean valid = false;
    view.showTitle();

    Scanner sc = new Scanner(System.in);
    System.out.println("\t\t\t\t\tWelcome to Group 43's MCO1: Gold Miner!");
    
    do{
      try{
        switch(step){
          case 1:
            System.out.print("\t\t\t\t\t\tInput grid size [8-64]: ");
            SIZE = Integer.parseInt(sc.nextLine());

            if(SIZE >= MIN && SIZE <= MAX){
              step = 2;
            }
            else
              System.out.println(Colors.RED + "\t\t\t\t\t\tFrom 8 to 64 only!" + Colors.RESET);
            break;
          case 2:
            System.out.print("\t\t\t\t\t\t Random or Smart [R/S]: ");
            level = Character.toLowerCase(sc.nextLine().charAt(0));

            if(level == 'r' || level == 's'){
              step = 3;
            }
            else
              System.out.println(Colors.RED + "\t\t\t\t\t\t R or S only!" + Colors.RESET);
            break;
          case 3:
            System.out.print("\t\t\t\t\t\t\t  Fast mode? [Y/N]: ");
            char ans = Character.toLowerCase(sc.nextLine().charAt(0));
            if(ans == 'y' || ans == 'n'){
              isFast = ans == 'y';
              valid=true;
            }
            else
              System.out.println(Colors.RED + "\t\t\t\t\t\t\t  Y or N only!" + Colors.RESET);
            break;
        }
      }catch(Exception ex){
        System.out.println(Colors.RED + "\t\t\t\t\t\tEnter valid input!"+ Colors.RESET);
      }
    }while(!valid);
    //sc.close();

    miningArea = new MiningArea(SIZE);
    initializeGameObjects();
    view.setMiningArea(agent.getMA());
    refreshScreen();

    startAI(sc);
  }

  /*
   * Responsible for helping print the AI's movements using Java's
   * scanner. Also helps initialize the AI rationality.
   * @param sc scanner reference to adjust print and type reading.
   */
  private void startAI(Scanner sc){
    
    if(level == 'r')  agent.initializeTreeRandom();
    else              agent.initializeTreeSmart();
    
    try{   
      while(agent.act()){
        refreshScreen();

        if(isFast)  Thread.sleep(DELAY);
        else {
          System.out.println(Colors.GRAY+"Press enter to continue..."+Colors.RESET);
          sc.nextLine();      
        }
      }
    }catch(Exception ex){
      System.out.println(ex);
    }
    finally{
      sc.close();
    }
    
    //final results
    refreshScreen();
    if(agent.getLoc().equals(gsq.getLoc())) 
      view.showAgentSuccess();
    else
      view.showAgentFail();
  }

  /*
   * Helps refresh the screen upon printing every text
   * of the GUI.
   */
  private void refreshScreen(){
    view.showMiningArea();
    view.showAgentInfo(agent);
  }

  /*
   * Helps print a random number for agent's usage
   * @param min is the minimum of the number to reach
   * @param max is the maximum of the number to reach
   * @return random integer from min to max.
   */
  public static int randomNum (int min, int max){
    Random rand = new Random();
    return rand.nextInt(max - min + 1) + min; 
  }

  /**
   * Helps initialize the random objects involved in the
   * grid by preparing a random position for each.
   */
  private void initializeGameObjects(){
    //Note by erik: used the overloaded function for maintenance for
    //programmers to alter if ever wanted to.
    agent = new GoldMiner(miningArea, GoldMiner.defaultLoc,1);
    miningArea.addAgent(agent);

    for(int i=0; i<SIZE*pitMulti; i++){
      if(!miningArea.addPit(new Pit(randomNum(1,SIZE-1), randomNum(1,SIZE-1))))
        i--;
    }
    
    gsq = new GoldSquare(randomNum(1,SIZE-1), randomNum(1,SIZE-1));
    while(!miningArea.addGoldSquare(gsq)){}

    Beacon bcnTemp;
    for(int i=0; i<SIZE*bcnMulti; i++){
      bcnTemp = new Beacon(randomNum(1,SIZE-1), randomNum(1,SIZE-1));
      if(!miningArea.addBeacon(bcnTemp))
        i--;
      else
        bcnTemp.setGoldDistance(calculateGoldDist(bcnTemp));
    }
    
  }

  /*
   * Returns the Manhattan distance of tiles to reach the gold
   * @param bcn tile to get distance from
   * @return beacon distance from gold in terms of tiles calculated by column and row of bcn subtracted to gold tile.
  */
  public int calculateGoldDist(Beacon bcn) {
    int x;
    int y;
    x = Math.abs(bcn.getLoc().c() - gsq.getLoc().c());
    y = Math.abs(bcn.getLoc().r() - gsq.getLoc().r());

    return x+y;
    
  }
}