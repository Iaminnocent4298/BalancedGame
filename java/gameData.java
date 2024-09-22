import java.util.*;
public class gameData {
    String version;
    int turn;
    int subturn;
    int nextEvent;
    int islandLim;
    int islandCost;
    int lockoutReset;
    event weather;
    event season;
    event disaster;
    int temperature;
    event temp;
    spell[][] spells;
    weapon[][] weapons;
    playerData[] arr;
    ArrayList<event> lst;
    ArrayList<event> curevent;
    lockoutGoal[] goals;
    ArrayList<tuple>[] bridges;
    int[] location;
    ArrayList<String> eventLog;
    int playersAlive;
    potion[] potionShop;
    ArrayList<peffect> potionEffects;
    public gameData() {}
    /**
     * Creates the gameData object.
     * @param p The number of players in the game.
     * @param vers The version number.
     * @param s The number of lockout goals
     * @param names The names of the p players.
     * @param lt The number of lockout types there are.
     * @param ps The potion shop.
     */
    @SuppressWarnings("unchecked")
    public gameData(int p, String vers, int s, String[] names, int lt, potion[] ps) {
        version = vers;
        turn = 0;
        subturn = p;
        nextEvent = 2;
        islandLim = 26;
        islandCost = 1;
        lockoutReset = 10;
        weather = new event();
        season = new event();
        disaster = new event();
        spells = new spell[p][5];
        weapons = new weapon[p][2];
        arr = new playerData[p];
        lst = new ArrayList<>();
        curevent = new ArrayList<>();
        goals = new lockoutGoal[s];
        bridges = new ArrayList[26];
        for (int i=0; i<26; i++) {
            bridges[i] = new ArrayList<>();
        }
        location = new int[p];
        eventLog = new ArrayList<>();
        playersAlive = p;
        potionShop = ps;
        potionEffects = new ArrayList<>();
        for (int i=0; i<p; i++) {
            arr[i] = new playerData(names[i], lt);
            arr[i].setAlive(true);
            location[i] = 1;
        }
    }
}