package Data;
/**
 * A class used for referencing positions.
 * Note: Uses row and column for clarity.
 * Refer to row in terms of y and column in terms of x.
*/
public class Location{
  private int nRow;
  private int nCol;

  /**
  * This assigns nRow and nCol with the row and column of the object.
  * @param r the row located in
  * @param c the column located in 
  */
  public Location (int r, int c){
    nRow = r;
    nCol = c;
  }

  /**
   * This assigns the location of the piece given its letter and number position on the board.
   * @param a a combination of a letter and a number corresponding to the position of the piece on the board.
  */
  public Location (String a){
    if(a.length() ==2){
      nRow = (int)a.toUpperCase().charAt(0)-'A';
      nCol = (int)a.charAt(1)-'1';
    }
    else
      System.out.println("Wrong input");
  }

  /*
   * This constructor is for cloning locations.
   * @param cloneLoc of type location to copy information.
   */
  public Location (Location cloneLoc) {
    this.nRow = cloneLoc.r();
    this.nCol = cloneLoc.c();
  }

  /**
  * This returns the row of the object
  * Note: row is in terms of y axis.
  * @return nRow of object
  */
  public int r(){
    return nRow;
  }

  /**
  * This returns the column of the object
  * Note: col is in terms of x axis.
  * @return nCol of object
  */
  public int c(){
    return nCol;
  }
  
  /*Setter for Row
  
  public void setR(int R){
    nRow = R;
  }

  */

  /*Setter for Column
  
  public void  setC(int C){
    nCol = C;
  }
  */

  /**
  * For debugging purposes only
  * This is used to check the row and column of the object
  * @return string for displaying the location of the object
  */
  @Override
  public String toString(){
    return (nRow+1) + ", " + (nCol+1);
  }

  /**
  * This checks if the row and column of the Location matches with given object (Location)
  * @param obj the Location to be compared
  * @return true if object matches passed parameter; false otherwise
  */
  @Override
  public boolean equals(Object obj){
    if(obj != null)
    {
      Location newPoint = (Location)obj;
      return nRow == newPoint.nRow && nCol == newPoint.nCol;
    }
    return false;
  }

}