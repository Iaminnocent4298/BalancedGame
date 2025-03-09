export class playerData {
    isAlive;
    lives;
    name;
    lvl;
    lvlxp;
    nextlvl;
    abilityPoints;
    hp;
    maxhp; hpregen;
    mana; maxmana;
    manaregen;
    stamina; staminaregen; maxstamina;
    neutraldmg;
    elements;
    potionBag;
    inventory;
    lockoutProgress;
    location;
    spells;
    weapons;
    armour;
    modifier;
    /**
     * The constructor for player data
     * @param n The name of the player
     */
    playerData(n) {
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
        maxstamina = 10;
        stamina = maxstamina;
        staminaregen = 1;
        neutraldmg = 0;
        elements = [];
        for (i=0; i<2; i++) {
            elements[i] = [];
        }
        potionBag = [];
        inventory = new Map();
        lockoutProgress = new Map();
        location = 1;
        spells = [];
        for (i=0; i<5; i++) {
            spells[i] = null;
        }
        weapons = new weapon[2];
        for (i=0; i<2; i++) {
            weapons[i] = null;
        }
        armour = new armour[4];
        for (i=0; i<4; i++) {
            armour[i] = null;
        }
        modifier = "None";
    }

    //GETTERS
    isAlive() {return isAlive;}
    getLives() {return lives;}
    getName() {return name;}
    getLvl() {return lvl;}
    getLXP() {return lvlxp;}
    getNL() {return nextlvl;}
    getAP() {return abilityPoints;}
    getHP() {return hp;}
    getHPRegen() {return hpregen;}
    getMaxHP() {return maxhp;}
    getMana() {return mana;}
    getMR() {return manaregen;}
    getMaxMana() {return maxmana;}
    getStamina() {return stamina;}
    getMaxStamina() {return maxstamina;}
    getStaminaRegen() {return staminaregen;}
    getND() {return neutraldmg;}
    getElements() {return elements;}
    /**
     * Returns the data value of the elements and their defences
     * @param i 0 for attack, 1 for defence
     * @param j The element type (0 = earth, 1 = thunder, etc)
     * @return The value of the specified element
     */
    getElement(i, j) {return elements[i][j];}
    getPB() {return potionBag;}
    getInventoryValue(s) {
        if (inventory.has(s)) {
            return inventory.get(s);
        }
        return 0;
    }
    getLockoutProgressValue(s) {
        if (lockoutProgress.has(s)) {
            return lockoutProgress.get(s);
        }
        return 0;
    }
    getLockoutProgress() {return lockoutProgress;}
    getLocation() {return location;}
    /**
     * Gets the spell
     * @param spellNum The spell number (0-indexed)
     * @param s The data of the spell
     */
    getSpell(spellNum) {return spells[spellNum];}
    getWeapon(weaponNum) {return weapons[weaponNum];}
    getArmour(armourNum) {return armour[armourNum];}
    getModifier() {return modifier;}
    //SETTERS
    setAlive(b) {isAlive = b;}
    setLives(i) {lives = i;}
    setName(n) {name = n;}
    setLXP(d) {lvlxp = d;}
    setAP(i) {abilityPoints=i;}
    setHP(d) {hp = d;}
    setHPRegen(i) {hpregen = i;}
    setMana(i) {mana = i;}
    setMR(i) {manaregen = i;}
    setStamina(i) {stamina = i;}
    setMaxStamina(i) {maxstamina = i;}
    setStaminaRegen(i) {staminaregen = i;}
    setPB(pb) {potionBag = pb;}
    setInventoryValue(s, i) {
        if (i==0) {
            inventory.delete(s);
            return;
        }
        inventory.set(s,i);
    }
    setElement(i, j, v) {elements[i][j] = v;}
    setLockoutProgressValue(s, d) {
        if (lockoutProgress.has(s)) {
            lockoutProgress.set(s, lockoutProgress.get(s)+r2(d));
        }
        else lockoutProgress.set(s,d);
    }
    setLocation(i) {location = i;}
    /**
     * Sets the spell
     * @param spellNum The spell number (0-indexed)
     * @param s The data of the spell
     */
    setSpell(spellNum, s) {spells[spellNum] = s;}
    setWeapon(weaponNum, w) {weapons[weaponNum] = w;}
    setArmour(armourNum, a) {armour[armourNum] = a;}
    setModifier(s) {modifier = s;}
    //MUTATORS
    addLives(v) {lives+=v;}
    addLvl(i) {lvl+=i;}
    addLXP(v) {lvlxp = r2(lvlxp+v);}
    addNL(i) {nextlvl+=i;}
    addAP(v) {abilityPoints+=v;}
    addHP(v) {hp+=v;}
    addHPRegen(v) {hpregen+=v;}
    addMaxHP(v) {maxhp+=v;}
    addMana(v) {mana+=v;}
    addMR(v) {manaregen+=v;}
    addMaxMana(v) {maxmana+=v;}
    addStamina(i) {stamina+=i;}
    addMaxStamina(i) {maxstamina+=i;}
    addStaminaRegen(i) {staminaregen+=i;}
    addND(v) {neutraldmg+=v;}
    addInventoryValue(s, i) {
        inventory.set(s, inventory.get(s)+i);
        if (inventory.get(s)==0) {
            inventory.delete(s);
        }
    }
    /**
     * 
     * @param i 0 for attack, 1 for defence
     * @param j The element type (0 = earth, 1 = thunder, etc)
     * @param v The value to increase by
     */
    addElement(i, j, v) {elements[i][j]+=v;}
    addLockoutProgressValue(s, d) {lockoutProgress.set(s, r2(lockoutProgress.get(s)+d));}
    //OTHERS
    kill() {isAlive = false;}
    /**
     * 
     * @param s The name of the object
     * @param i The amount of the object you want to remove
     * @return If it possible to remove i of object s from the inventory
     */
    inventoryRemovable(s, i) {
        if (!inventory.has(s) || inventory.get(s)<i) {
            return false;
        }
        return true;
    }
    r2(d) {
        let s = d.toString();
        if (s.includes(".")) {
            let index = s.indexOf(".");
            if (s.substring(index + 1).length <= 2) {
                return parseFloat(s);
            } else {
                s = s.substring(0, index + 3);
                return parseFloat(s);
            }
        }
        return d;
    }
    
}