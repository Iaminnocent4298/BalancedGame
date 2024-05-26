public class spell {
    private int[][] damages;
    private int manacost;
    private int rerollcost;
    private String name;
    private String rarity;
    private int tier;
    public spell(int[][] dmg, int mc, int rc, String n, String r) {
        damages = dmg;
        manacost = mc;
        rerollcost = rc;
        name = n;
        rarity = r;
        tier = 1;
    }

    //GETTERS
    public int[][] getDmgs() {return damages;}
    public int getMC() {return manacost;}
    public int getRC() {return rerollcost;}
    public String getName() {return name;}
    public String getRarity() {return rarity;}
    public int getTier() {return tier;}
    //SETTERS
    public void setRC(int i) {rerollcost = i;}
    //MUTATORS
    public void addDmgs(int i, int j, int v) {damages[i][j]+=v;}
    public void addTier(int i) {tier+=i;}
    //OTHERS
    public double[] rollDmg() {
        double[] values = new double[7];
        for (int i=0; i<7; i++) {
            values[i] = (int) (Math.random()*(damages[1][i]-damages[0][i]+1))+damages[0][i];
        }
        return values;
    }
}