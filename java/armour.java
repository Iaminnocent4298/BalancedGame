public class armour {
    /**
     * The amount of max health increased to the player
     */
    private int maxHealthBuff;
    private int[] elementalDefences;
    private int rerollCost;
    private String name;
    private String rarity;
    public armour(int maxHealthBuff, int[] elementalDefences, String name, String rarity, int rerollCost) {
        this.maxHealthBuff = maxHealthBuff;
        this.elementalDefences = elementalDefences;
        this.name = name;
        this.rarity = rarity;
        this.rerollCost = rerollCost;
    }
    
    public int getMaxHealthBuff() {return maxHealthBuff;}
    public int[] getElementalDefences() {return elementalDefences;}
    public int getRerollCost() {return rerollCost;}
    public String getName() {return name;}
    public String getRarity() {return rarity;}

    public void setRerollCost(int i) {rerollCost = i;}

    public void addDefences(int i, int v) {
        elementalDefences[i]+=v;
    }
}
