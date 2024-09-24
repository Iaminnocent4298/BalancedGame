public class weapon {
    private int[][] damages;
    private int rerollcost;
    private String name;
    private String rarity;
    public weapon(int[][] dmg, int rc, String n, String r) {
        damages = dmg;
        rerollcost = rc;
        name = n;
        rarity = r;
    }
    //GETTERS
    public int[][] getDmgs() {return damages;}
    public int getRC() {return rerollcost;}
    public String getName() {return name;}
    public String getRarity() {return rarity;}
    //SETTERS
    public void setRC(int i) {rerollcost = i;}
    //MUTATORS
    /**
     * Increases the damage of a certain element
     * @param i The element (0-7)
     * @param v The amount of damage to increase by
     */
    public void addDmgs(int i, int v) {
        damages[0][i]+=v;
        damages[1][i]+=v;
    }
    //OTHERS
    public double[] rollDmg() {
        double[] values = new double[6];
        for (int i=0; i<6; i++) {
            values[i] = (int) (Math.random()*(damages[1][i]-damages[0][i]+1))+damages[0][i];
        }
        return values;
    }
}