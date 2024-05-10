public class mob {
    private int maxhp;
    private double hp;
    private int[][] damages;
    private int[][] defences;
    private Object[] lootpool;
    private double[] rates;
    public mob(int maxhp, double hp, int[][] dmg, int[][] def, Object[] loot, double[] rates) {
        this.maxhp = maxhp;
        this.hp = hp;
        damages = dmg;
        defences = def;
        lootpool = loot;
        this.rates = rates;
    }
    //ACCESSORS
    //MUTATORS

}
