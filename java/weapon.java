public class weapon {
    private int[][] damages;
    private int rerollcost;
    private String name;
    private String rarity;
    private int tier;
    public weapon(int[][] dmg, int rc, String n, String r) {
        damages = dmg;
        rerollcost = rc;
        name = n;
        rarity = r;
        tier = 1;
    }
    //GETTERS
    public int[][] getDmgs() {return damages;}
    public int getRC() {return rerollcost;}
    public int getTier() {return tier;}
    //SETTERS
    public void setRC(int i) {rerollcost = i;}
    //MUTATORS
    public void addDmgs(int i, int j, int v) {damages[i][j]+=v;}
    public void addTier(int i) {tier+=i;}
    //OTHERS
    public double[] rollDmg() {
        double[] values = new double[6];
        for (int i=0; i<6; i++) {
            values[i] = (int) (Math.random()*(damages[1][i]-damages[0][i]+1))+damages[0][i];
        }
        return values;
    }
}