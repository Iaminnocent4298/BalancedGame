export class lockoutGoal {
    type;
    value;
    apreward;
    done;
    constructor(type, value, apreward) {
        this.type = type;
        this.value = value;
        this.apreward = apreward;
        this.done = false;
    }
    //GETTERS
    getType() {return type;}
    getValue() {return value;}
    getAPR() {return apreward;}
    getDone() {return done;}
    //SETTERS
    setDone(b) {done = b;}
}