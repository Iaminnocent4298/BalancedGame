public class peffect {
    private String playerName;
    private String potionType;
    private int effectValue;
    private int duration;
    private int subturnUsed;
    public peffect(String playerName, String potionType, int effectValue, int duration, int subturnUsed) {
        this.playerName = playerName;
        this.potionType = potionType;
        this.effectValue = effectValue;
        this.duration = duration;
        this.subturnUsed = subturnUsed;
    }
    //GETTERS
    public String getName() {return playerName;}
    public String getType() {return potionType;}
    public int getValue() {return effectValue;}
    public int getTurns() {return duration;}
    public int getSubturnUsed() {return subturnUsed;}
    //SETTERS
    //MUTATORS
    public void lowerTurn() {duration--;}
}