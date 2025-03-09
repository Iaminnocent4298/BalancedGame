export class peffect {
    playerName;
    potionType;
    effectValue;
    duration;
    subturnUsed;
    constructor(playerName, potionType, effectValue, duration, subturnUsed) {
        this.playerName = playerName;
        this.potionType = potionType;
        this.effectValue = effectValue;
        this.duration = duration;
        this.subturnUsed = subturnUsed;
    }
    //GETTERS
    getName() {return playerName;}
    getType() {return potionType;}
    getValue() {return effectValue;}
    getTurns() {return duration;}
    getSubturnUsed() {return subturnUsed;}
    //SETTERS
    //MUTATORS
    lowerTurn() {duration--;}
}