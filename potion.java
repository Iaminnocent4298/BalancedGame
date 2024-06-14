public class potion {
    private int lvlreq;
    private String type;
    private int value;
    private int turns;
    private int cost;
    public potion(int lr, String t, int i, int duration, int c) {
        lvlreq = lr;
        type = t;
        value = i;
        turns = duration;
        cost = c;
    }
    //GETTERS
    public int getLvlReq() {return lvlreq;}
    public String getType() {return type;}
    public int getValue() {return value;}
    public int getTurns() {return turns;}
    public int getCost() {return cost;}
}
