export class potion {
    lvlreq;
    type;
    value;
    turns;
    cost;
    potion(lvlreq, type, value, turns, cost) {
        this.lvlreq = lvlreq;
        this.type = type;
        this.value = value;
        this.turns = turns;
        this.cost = cost;
    }
    //GETTERS
    getLvlReq() {return lvlreq;}
    getType() {return type;}
    getValue() {return value;}
    getTurns() {return turns;}
    getCost() {return cost;}
}
