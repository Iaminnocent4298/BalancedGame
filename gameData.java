import java.util.*;
public class gameData {
    String version;
    int turn;
    int subturn;
    int nextEvent;
    int islandLim;
    int bridgeCost;
    int bridgesBuilt;
    int islandCost;
    int crossed;
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
    public gameData(int p, String vers, int s, String[] names, int lt, potion[] ps) {
        version = vers;
        turn = 0;
        subturn = 1;
        nextEvent = 2;
        islandLim = 26;
        bridgeCost = 2;
        bridgesBuilt = 0;
        islandCost = 1;
        crossed = 0;
        weather = new event();
        season = new event();
        disaster = new event();
        spells = new spell[5][p];
        weapons = new weapon[2][p];
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
            arr[i] = new playerData(names[i], new ArrayList<>(), lt);
        }
    }
}