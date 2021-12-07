package Data;
import BoardModel.GameManager;
import BoardModel.GoldMiner;
import BoardModel.MiningArea;
import View.Colors;
import Tiles.Pit;

import java.util.*;

/**
 * Main tree for data handling actions of the agent. This
 * will hold all the algorithms agent will handle.
 */
public class Tree {
  private Node root;
  private GoldMiner agent;
  private int traverseLimit;

  /**
   * Begins the tree at said root node.
   * @param agent to set the starting node with an agent
   */
  public Tree(GoldMiner agent) {
    this.agent = agent;
    root = new Node(agent);
    traverseLimit = agent.getMA().getSize() * 30;
  }

  /**
   * Compares the heuristics and always traverses with the node
   * that has the lowest heuristic value.
   * @return returns the traversed nodes
   */
  public List<Node> searchLowCost(){
    Node n = root;
    List<Node> path = new ArrayList<Node>();
    boolean end = false;
    Location goldLoc = n.getAgent().getMA().getGoldLoc(), currLoc;
    
    //limit is used when node is null so that it won't be picked
    int left, mid, right, traverse=0, limit=999*agent.getMA().getSize();
    
    System.out.println("\n"+Colors.BLUE+"searchLowCost()"+Colors.RESET);

    while(!end && traverse<traverseLimit){
      left = (n.getLeftNode() != null) ? n.getLeftNode().getHeuristic() : limit;
      mid = (n.getMidNode() != null) ? n.getMidNode().getHeuristic() : limit;
      right = (n.getRightNode() != null) ? n.getRightNode().getHeuristic() : limit;

      //[START] VISUAL DEBUGGING PURPOSES
      String leftStr = (left==limit)?"null":String.valueOf(left);
      String midStr = (mid==limit)?"null":String.valueOf(mid);
      String rightStr = (right==limit)?"null":String.valueOf(right);

      System.out.println("("+n.getAgent().getLoc()+" - "+n.getAgent().getFront()+" ) "+leftStr+" "+midStr+" "+rightStr);
      //[END]

      if(left != limit || mid != limit || right != limit){
        if(left <= mid && left <= right){
          n.exploreNode(0);
          n = n.getLeftNode();
          System.out.println("Left node! "+Colors.RED+"(SCAN)"+Colors.RESET);
        }
        else if(mid <= left && mid <= right){
          
          n.exploreNode(1);
          n = n.getMidNode();
          System.out.println("Mid node! "+Colors.YELLOW+"(TURN)"+Colors.RESET);
        }
        else{
          n.exploreNode(2);
          n = n.getRightNode();
          System.out.println("Right node! "+Colors.PURPLE+"(MOVE)"+Colors.RESET);
        }
        path.add(n);
        traverse++;
        System.out.println(Colors.BLUE+traverse+Colors.RESET+": "+n.getHeuristic() + " (end-while)\n");

        
        if(n.hasFoundGold())
          System.out.println(Colors.YELLOW+"KITA NA"+Colors.RESET);

        currLoc = n.getAgent().getLoc();

        if(currLoc.equals(goldLoc)){
          System.out.println(Colors.YELLOW+"GUDS NA"+Colors.RESET);
          end = true;
        }
        else if(n.getAgent().getMA().getTile(currLoc) instanceof Pit){
          System.out.println(Colors.RED+"GAME OVER"+Colors.RESET);
          end = true;
        }
      }
      else
        end = true;      
    }

    return path;
  }

  
  /**
   * Traverse nodes randomly and will only go back if the
   * current node has no children.
   * @return returns the traversed nodes
   */
  public List<Node> searchRandom(){
    Node n = root;
    List<Node> path = new ArrayList<Node>();
    boolean end = false, notNull, holdNotNull=false;
    int rand, holdRand=0, traverse=0;
    Location goldLoc = n.getAgent().getMA().getGoldLoc(), currLoc;
    
    System.out.println("\n"+Colors.BLUE+"searchRandom()"+Colors.RESET);

    while(!end && traverse<traverseLimit){
      notNull = false;

      //[START] VISUAL DEBUGGING PURPOSES
      String hVal = String.format("%s %s %s", (n.getLeftNode() != null) ? n.getLeftNode().getHeuristic() : "null", (n.getMidNode() != null) ? n.getMidNode().getHeuristic() : "null", (n.getRightNode() != null) ? n.getRightNode().getHeuristic() : "null");

      System.out.println("("+n.getAgent().getLoc()+" - "+n.getAgent().getFront()+" ) "+hVal);
      //[END]
      
      //delete node
      if(holdNotNull){
        n.deletePath(holdRand);
        n = n.getRoot(); //balik sa parent

        hVal = String.format("%s %s %s", (n.getLeftNode() != null) ? n.getLeftNode().getHeuristic() : "null", (n.getMidNode() != null) ? n.getMidNode().getHeuristic() : "null", (n.getRightNode() != null) ? n.getRightNode().getHeuristic() : "null");

        System.out.println("("+n.getAgent().getLoc()+" - "+n.getAgent().getFront()+" ) "+hVal);
      }

      if(!n.isEmpty()){
        do {
          rand = GameManager.randomNum(0,2);
          notNull = n.getNodeAt(rand) != null;
        } while(!notNull);
        
        if(n.hasFoundGold()){
          System.out.println(Colors.YELLOW+"KITA NA"+Colors.RESET);
          rand = 2;
        }

        n.exploreNode(rand);
        n = n.getNodeAt(rand);

        if(rand==0){
          System.out.println("Left node! "+Colors.RED+"(SCAN)"+Colors.RESET);
        }
        else if(rand==1){
          System.out.println("Mid node! "+Colors.YELLOW+"(TURN)"+Colors.RESET);
        }
        else{
          System.out.println("Right node! "+Colors.PURPLE+"(MOVE)"+Colors.RESET);
        }

        currLoc = n.getAgent().getLoc();
        
        //IF DEAD-END SA NODE, GO BACK
        if(n.isEmpty()){
          System.out.println("Going back!");
          holdRand = rand;
          holdNotNull = true;
        }
        else{          
          path.add(n);
          holdNotNull = false;
          traverse++;

          //IF PIT, GO BACK
          if(n.getAgent().getMA().getTile(currLoc) instanceof Pit){
            System.out.println("Add as backtrack!");
            holdRand = rand;
            holdNotNull = true;
          }

        }
        
        System.out.println(Colors.BLUE+traverse+Colors.RESET+": "+n.getHeuristic() + " (end-while)\n");

        if(currLoc.equals(goldLoc)){
          System.out.println(Colors.YELLOW+"GUDS NA"+Colors.RESET);
          end = true;
        }
      }
      else{
        System.out.println(Colors.RED+"END"+Colors.RESET);
        end = true;
      }
    }

    return path;
  }


}