import java.util.*;
public class playerData {
    private boolean isAlive;
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
    private int stamina, staminaregen, maxstamina;
    private int neutraldmg;
    private int[][] elements;
    private ArrayList<potion> potionBag;
    private Map<String,Integer> inventory;
    private int amplifiercount;
    private Map<String,Double> lockoutProgress;
    private int location;
    private spell[] spells;
    private weapon[] weapons;
    /**
     * The constructor for player data
     * @param n The name of the player
     * @param lt The number of lockout types
     */
    public playerData(String n, int lt) {
        isAlive = true;
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
        maxstamina = 25;
        stamina = maxstamina;
        staminaregen = 1;
        neutraldmg = 0;
        elements = new int[2][5];
        potionBag = new ArrayList<>();
        inventory = new HashMap<>();
        amplifiercount = 0;
        lockoutProgress = new HashMap<>();
        location = 1;
        spells = new spell[5];
        for (int i=0; i<5; i++) {
            spells[i] = null;
        }
        weapons = new weapon[2];
        for (int i=0; i<2; i++) {
            weapons[i] = null;
        }
    }

    //GETTERS
    public boolean isAlive() {return isAlive;}
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
    public int getStamina() {return stamina;}
    public int getMaxStamina() {return maxstamina;}
    public int getStaminaRegen() {return staminaregen;}
    public int getND() {return neutraldmg;}
    public int[][] getElements() {return elements;}
    public int getElement(int i, int j) {return elements[i][j];}
    public ArrayList<potion> getPB() {return potionBag;}
    public int getInventoryValue(String s) {
        if (inventory.containsKey(s)) {
            return inventory.get(s);
        }
        inventory.put(s,0);
        return 0;
    }
    public int getAmplifierCount() {return amplifiercount;}
    public double getLockoutProgressValue(String s) {
        if (lockoutProgress.containsKey(s)) {
            return lockoutProgress.get(s);
        }
        lockoutProgress.put(s,0.0);
        return 0;
    }
    public Map<String,Double> getLockoutProgress() {return lockoutProgress;}
    public int getLocation() {return location;}
    /**
     * Gets the spell
     * @param spellNum The spell number (0-indexed)
     * @param s The data of the spell
     */
    public spell getSpell(int spellNum) {return spells[spellNum];}
    public weapon getWeapon(int weaponNum) {return weapons[weaponNum];}
    //SETTERS
    public void setAlive(boolean b) {isAlive = b;}
    public void setLives(int i) {lives = i;}
    public void setName(String n) {name = n;}
    public void setLXP(double d) {lvlxp = d;}
    public void setAP(int i) {abilityPoints=i;}
    public void setHP(double d) {hp = d;}
    public void setHPRegen(int i) {hpregen = i;}
    public void setMana(int i) {mana = i;}
    public void setMR(int i) {manaregen = i;}
    public void setStamina(int i) {stamina = i;}
    public void setMaxStamina(int i) {maxstamina = i;}
    public void setStaminaRegen(int i) {staminaregen = i;}
    public void setPB(ArrayList<potion> pb) {potionBag = pb;}
    public void setInventoryValue(String s, int i) {
        if (inventory.containsKey(s)) {
            inventory.replace(s, i);
        }
        else inventory.put(s,i);
    }
    public void setElement(int i, int j, int v) {elements[i][j] = v;}
    public void getAmplifierCount(int i) {amplifiercount = i;}
    public void setLockoutProgressValue(String s, double d) {
        if (lockoutProgress.containsKey(s)) {
            lockoutProgress.replace(s, lockoutProgress.get(s), r2(d));
        }
        else lockoutProgress.put(s,d);
    }
    public void setLocation(int i) {location = i;}
    /**
     * Sets the spell
     * @param spellNum The spell number (0-indexed)
     * @param s The data of the spell
     */
    public void setSpell(int spellNum, spell s) {spells[spellNum] = s;}
    public void setWeapon(int weaponNum, weapon w) {weapons[weaponNum] = w;}
    //MUTATORS
    public void addLives(int v) {lives+=v;}
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
    public void addStamina(int i) {stamina+=i;}
    public void addMaxStamina(int i) {maxstamina+=i;}
    public void addStaminaRegen(int i) {staminaregen+=i;}
    public void addND(int v) {neutraldmg+=v;}
    public void addInventoryValue(String s, int i) {
        if (inventory.containsKey(s)) {
            inventory.replace(s, inventory.get(s)+i);
        }
        else {
            inventory.put(s,i);
        }
    }
    public void addElement(int i, int j, int v) {elements[i][j]+=v;}
    public void addAmplifierCount(int i) {amplifiercount+=i;}
    public void addLockoutProgressValue(String s, double d) {
        if (lockoutProgress.containsKey(s)) {
            lockoutProgress.replace(s, r2(lockoutProgress.get(s)+d));
        }
        else lockoutProgress.put(s,d);
    }
    //OTHERS
    public void kill() {isAlive = false;}
    /**
     * 
     * @param s The name of the object
     * @param i The amount of the object you want to remove
     * @return If it possible to remove i of object s from the inventory
     */
    public boolean inventoryRemovable(String s, int i) {
        if (!inventory.containsKey(s) || inventory.get(s)<i) {
            return false;
        }
        return true;
    }
    private double r2(double d) {
        d = Math.round(d*100)/100.0;
        return d;
    }
}


