import java.util.ArrayList;

public class mob {
    private int maxhp;
    private double hp;
    private int[][] damages;
    private int[] defences;
    private Object[] lootpool;
    private double[] rates;
    private int island;
    private String name;
    public mob(int maxhp, double hp, int[][] dmg, int[] def, Object[] loot, double[] rates, String name) {
        this.maxhp = maxhp;
        this.hp = hp;
        damages = dmg;
        defences = def;
        lootpool = loot;
        this.rates = rates;
        island = 1;
        this.name = name;
    }
    //ACCESSORS
    public String getName() {return name;}
    public boolean isDead() {return hp<=0.0;}
    public int getIsland() {return island;}
    //MUTATORS
    public void setHP(double d) {hp=d;}
    public void setIsland(int maxIslands) {
        island = (int) (Math.random()*maxIslands)+1;
    }
    //OTHERS
    public double[] attack() {
        double[] dmgs = new double[6];
        for (int i=0; i<6; i++) {
            dmgs[i] = (int) (Math.random()*(damages[1][i]-damages[0][i]+1))+damages[0][i];
        }
        return dmgs;
    }
    public void getHit(double[] dmgs) {
        double totaldmg = 0;
        for (int i=0; i<5; i++) {
            totaldmg+=(dmgs[i]-defences[i]);
        }
        hp-=totaldmg;
    }
    public ArrayList<Object> dropItems() {
        ArrayList<Object> lst = new ArrayList<>();
        for (int i=0; i<lootpool.length; i++) {
            double roll = Math.random();
            if (roll<rates[i]) lst.add(lootpool[i]);
        }
        return lst;
    }
}
