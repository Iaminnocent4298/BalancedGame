import java.util.ArrayList;

public class mob {
    private int maxhp;
    private int level;
    private double hp;
    private int[][] damages;
    private int[] defences;
    //private double[] rates;
    private int island;
    private String name;
    /**
     * Constructor for the mob class.
     * @param maxhp The mob's max health
     * @param dmg The damages the mob deals
     * @param def The elemental defences the mob has
     * @param name The name of the mob
     * @param isHostile Is the mob hostile or neutral
     */
    public mob(int maxhp, int level, int[][] dmg, int[] def, String name) {
        this.level = level;
        this.maxhp = maxhp;
        hp = maxhp;
        damages = dmg;
        defences = def;
        //this.rates = rates;
        island = 1;
        this.name = name;
    }
    //ACCESSORS
    public String getName() {return name;}
    public int getLevel() {return level;}
    public boolean isDead() {return hp<=0.0;}
    public int getIsland() {return island;}
    //MUTATORS
    public void setHP(double d) {hp=d;}
    public void spawn(int maxIslands) {
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
}
