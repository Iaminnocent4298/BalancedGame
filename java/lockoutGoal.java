public class lockoutGoal {
    private String type;
    private int value;
    private int apreward;
    private boolean done;
    public lockoutGoal(String t, int v, int apr) {
        type = t;
        value = v;
        apreward = apr;
        done = false;
    }
    //GETTERS
    public String getType() {return type;}
    public int getValue() {return value;}
    public int getAPR() {return apreward;}
    public boolean getDone() {return done;}
    //SETTERS
    public void setDone(boolean b) {done = b;}
}