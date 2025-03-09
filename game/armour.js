export class armour {
    maxHealthBuff;
    elementalDefences;
    name;
    rarity;
    rerollCost;
    constructor(maxHealthBuff,elementalDefences,name,rarity,rerollCost) {
        this.maxHealthBuff = maxHealthBuff;
        this.elementalDefences = elementalDefences;
        this.name = name;
        this.rarity = rarity;
        this.rerollCost = rerollCost;
    }

    getMaxHealthBuff() {return maxHealthBuff;}
    getElementalDefences() {return elementalDefences;}
    getRerollCost() {return rerollCost;}
    getName() {return name;}
    getRarity() {return rarity;}

    setRerollCost(i) {rerollCost = i;}

    addDefences(i, v) {
        elementalDefences[i]+=v;
    }
}