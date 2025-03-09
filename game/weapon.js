export class weapon {
    damages;
    rerollcost;
    name;
    rarity;
    weapon(damages, rerollcost, name, rarity) {
        this.damages = damages;
        this.rerollcost = rerollcost;
        this.name = name;
        this.rarity = rarity;
    }
    //GETTERS
    getDmgs() {return damages;}
    getRC() {return rerollcost;}
    getName() {return name;}
    getRarity() {return rarity;}
    //SETTERS
    setRC(i) {rerollcost = i;}
    //MUTATORS
    /**
     * Increases the damage of a certain element
     * @param i The element (0-6)
     * @param v The amount of damage to increase by
     */
    addDmgs(i, v) {
        damages[0][i]+=v;
        damages[1][i]+=v;
    }
    //OTHERS
    rollDmg() {
        values = [];
        for (i=0; i<6; i++) {
            values[i] = (int) (Math.random()*(damages[1][i]-damages[0][i]+1))+damages[0][i];
        }
        return values;
    }
}