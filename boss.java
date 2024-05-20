public class boss extends mob {
    spell ultimate;
    int cooldown;
    int untilnextattack;
    public boss(int maxhp, double hp, int[][] dmg, int[] def, Object[] loot, double[] rates, String name, spell spells, int cd) {
        super(maxhp, hp, dmg, def, loot, rates, name);
        ultimate = spells;
        cooldown = cd;
        untilnextattack = 0;
    }
    //ACCESSORS
    //MUTATORS
    //OTHERS
    public double[] spellAttack() {
        if (untilnextattack==0) {
            double[] dmgs = new double[6];
            for (int i=0; i<6; i++) {
                dmgs[i] = (int) (Math.random()*(ultimate.getDmgs()[1][i]-ultimate.getDmgs()[0][i]+1))+ultimate.getDmgs()[0][i];
            }
            return dmgs;
        }
        else {
            return attack();
        }
    }
}
