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
    //initializeTestMap();
    //initializeTestMap2();
    view.setMiningArea(agent.getMA());
    refreshScreen();

    startAI(sc);
  }

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

  //temporary
  private void refreshScreen(){
    view.showMiningArea();
    view.showAgentInfo(agent);
  }

  public static int randomNum (int min, int max){
    Random rand = new Random();
    return rand.nextInt(max - min + 1) + min; 
  }

  private void initializeTestMap(){
    agent = new GoldMiner(miningArea);
    miningArea.addAgent(agent);

    //SPECIAL NOTE : Use locations programmatically.
    // Subtract by 1 against board
    //gsq = new GoldSquare(randomNum(1,SIZE-1), randomNum(1,SIZE-1));
    gsq = new GoldSquare(13,1);
    miningArea.addGoldSquare(gsq);

    //miningArea.addPit(new Pit(0,1));
    //miningArea.addPit(new Pit(1,0));
    
    //Add beacon testing
    Beacon bcn = new Beacon(4,12);
    miningArea.addBeacon(bcn);
    bcn.setGoldDistance(calculateGoldDist(bcn));

    Beacon bcn2 = new Beacon(7,9);
    miningArea.addBeacon(bcn2);
    bcn2.setGoldDistance(calculateGoldDist(bcn2));
    
    miningArea.addPit(new Pit(8,7));
    miningArea.addPit(new Pit(9,6));
    miningArea.addPit(new Pit(13,12));
    //miningArea.addPit(new Pit(5,4));

    miningArea.addPit(new Pit(15,2));

  }
  private void initializeTestMap2(){
    agent = new GoldMiner(miningArea);
    miningArea.addAgent(agent);

    //SPECIAL NOTE : Use locations programmatically.
    // Subtract by 1 against board
    //gsq = new GoldSquare(randomNum(1,SIZE-1), randomNum(1,SIZE-1));
    gsq = new GoldSquare(5,3);
    miningArea.addGoldSquare(gsq);

    //miningArea.addPit(new Pit(0,1));
    //miningArea.addPit(new Pit(1,0));

    //Add beacon testing
    Beacon bcn = new Beacon(1,2);
    miningArea.addBeacon(bcn);
    bcn.setGoldDistance(calculateGoldDist(bcn));
    
    miningArea.addPit(new Pit(5,4));
    miningArea.addPit(new Pit(5,2));
    miningArea.addPit(new Pit(4,3));
    miningArea.addPit(new Pit(6,3));
  }

  /**
   * Helps initialize the random objects involved in the
   * grid by preparing a random position for each.
   */
  private void initializeGameObjects(){
    //Note by erik: used the overloaded function for maintenance, remove once sure na
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