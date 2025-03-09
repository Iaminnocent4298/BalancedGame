export class bridge {
    endingIsland;
    travelCost;
    turnsUntilCollapse;
    subturnBuilt;
    constructor(endingIsland, travelCost, turnsUntilCollapse, subturnBuilt) {
        this.endingIsland = endingIsland;
        this.travelCost = travelCost;
        this.turnsUntilCollapse = turnsUntilCollapse;
        this.subturnBuilt = subturnBuilt;
    }

    getEndingIsland() {return endingIsland;}
    getTravelCost() {return travelCost;}
    getTurnsUntilCollapse() {return turnsUntilCollapse;}
    getSubturnBuilt() {return subturnBuilt;}

    decreaseCollapseTimer() {turnsUntilCollapse--;}
}
