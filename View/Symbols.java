package View;
/** 
  A class containing strings (some are in unicode) as symbols based on each name.
*/

public class Symbols {
  public static final String VISITED =  Colors.GRAY+"X"+Colors.RESET;//"\u2593";
  public static final String GOLD_SQUARE = Colors.BRIGHT_YELLOW+"\u25AE"+Colors.RESET;
  public static final String BEACON = Colors.PURPLE+"\u25C8"+Colors.RESET;
  public static final String PIT = Colors.RED+"\u25EF"+Colors.RESET;
  public static final String VERT_LINE = "\u2502";
  public static final String AGENT = Colors.BLUE+"\u0D9E"+Colors.RESET;
  public static final String AGENT_GOLD = Colors.BRIGHT_YELLOW+"\u0D9E"+Colors.RESET;
  public static final String AGENT_PIT = Colors.RED+"\u0D9E"+Colors.RESET;
  public static final String AGENT_BCN = Colors.PURPLE+"\u0D9E"+Colors.RESET;
}