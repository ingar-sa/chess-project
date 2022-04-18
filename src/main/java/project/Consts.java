package project;

public final class Consts {
    
    public static final int GAME_NOT_OVER        = 0;
    public static final int PAT                  = 1;
    public static final int CHECKMATE            = 2;
    public static final int CHECKMATE_FOR_WHITE  = 4;
    public static final int CHECKMATE_FOR_BLACK  = 5;

    private Consts() {
    //This class should never be constructed, and this prevents that 
    throw new AssertionError("Cannot be constructed");
  }
}
