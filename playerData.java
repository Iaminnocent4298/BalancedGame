import java.util.*;
public class playerData {
    private int gameLevel;
    private int lives;
    private String name;
    private int lvl;
    private double lvlxp;
    private int nextlvl;
    private int abilityPoints;
    private double hp;
    private int maxhp, hpregen;
    private int mana, maxmana;
    private int manaregen;
    private int neutraldmg;
    private int[][] elements;
    private ArrayList<potion> potionBag;
    private lockoutProgress[] status;
    /**
     * 
     * @param n The name of the player
     * @param lt The number of lockout types
     */
    public playerData(String n, int lt) {
        gameLevel = 0;
        lives = 2;
        name = n;
        abilityPoints = 0;
        lvl = 0;
        lvlxp = 0;
        nextlvl = 100;
        maxhp = 2500;
        hp = maxhp;
        hpregen = 10;
        maxmana = 100;
        mana = maxmana;
        manaregen = 1;
        neutraldmg = 0;
        elements = new int[2][5];
        potionBag = new ArrayList<>();
        status = new lockoutProgress[lt];
    }

    //GETTERS
    public int getGL() {return gameLevel;}
    public int getLives() {return lives;}
    public String getName() {return name;}
    public int getLvl() {return lvl;}
    public double getLXP() {return lvlxp;}
    public int getNL() {return nextlvl;}
    public int getAP() {return abilityPoints;}
    public double getHP() {return hp;}
    public int getHPRegen() {return hpregen;}
    public int getMaxHP() {return maxhp;}
    public int getMana() {return mana;}
    public int getMR() {return manaregen;}
    public int getMaxMana() {return maxmana;}
    public int getND() {return neutraldmg;}
    public int[][] getElements() {return elements;}
    public int getElement(int i, int j) {return elements[i][j];}
    public ArrayList<potion> getPB() {return potionBag;}
    public lockoutProgress[] getLP() {return status;}
    //SETTERS
    public void setGL(int i) {gameLevel = i;}
    public void setLives(int i) {lives = i;}
    public void setName(String n) {name = n;}
    public void setLXP(double d) {lvlxp = d;}
    public void setAP(int i) {abilityPoints=i;}
    public void setHP(double d) {hp = d;}
    public void setHPRegen(int i) {hpregen = i;}
    public void setMana(int i) {mana = i;}
    public void setMR(int i) {manaregen = i;}
    public void setPB(ArrayList<potion> pb) {potionBag = pb;}
    public void setElement(int i, int j, int v) {elements[i][j] = v;}
    public void setLP(lockoutProgress[] lp) {status = lp;}
    //MUTATORS
    public void addLives(int v) {lives-=v;}
    public void addLvl(int i) {lvl+=i;}
    public void addLXP(double v) {lvlxp+=v;}
    public void addNL(int i) {nextlvl+=i;}
    public void addAP(int v) {abilityPoints+=v;}
    public void addHP(double v) {hp+=v;}
    public void addHPRegen(int v) {hpregen+=v;}
    public void addMaxHP(int v) {maxhp+=v;}
    public void addMana(int v) {mana+=v;}
    public void addMR(int v) {manaregen+=v;}
    public void addMaxMana(int v) {maxmana+=v;}
    public void addND(int v) {neutraldmg+=v;}
    public void addElement(int i, int j, int v) {elements[i][j]+=v;}
    //OTHERS
    public void kill() {gameLevel = -1;}
}


