public class peffect {
    private String name;
    private String type;
    private int value;
    private int turns;
    public peffect(String n, String t, int v, int duration) {
        name = n;
        type = t;
        value = v;
        turns = duration;
    }
    //GETTERS
    public String getName() {return name;}
    public String getType() {return type;}
    public int getValue() {return value;}
    public int getTurns() {return turns;}
    //SETTERS
    //MUTATORS
    public void lowerTurn() {turns--;}
}