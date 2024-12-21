public class bridge {
    private int endingIsland;
    private int travelCost;
    private int turnsUntilCollapse;
    private int subturnBuilt;
    public bridge(int endingIsland, int travelCost, int turnsUntilCollapse, int subturnBuilt) {
        this.endingIsland = endingIsland;
        this.travelCost = travelCost;
        this.turnsUntilCollapse = turnsUntilCollapse;
        this.subturnBuilt = subturnBuilt;
    }

    public int getEndingIsland() {return endingIsland;}
    public int getTravelCost() {return travelCost;}
    public int getTurnsUntilCollapse() {return turnsUntilCollapse;}
    public int getSubturnBuilt() {return subturnBuilt;}

    public void decreaseCollapseTimer() {turnsUntilCollapse--;}
}
