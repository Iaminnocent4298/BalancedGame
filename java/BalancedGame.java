/*
"Balanced Game" - made by Iaminnocent4298, web server made by Creative0708
Version 2.7 - Sniper
*/
import java.util.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.*;

import static java.lang.System.out;
class tuple {
    int first;
    int second;
    int third;
}
public class BalancedGame {
    static int turn;
    static int subturn;
    static int playerCount;
    static int islandLim = 26;
    static int bridgeCost;
    static int bridgesBuilt;
    static int islandCost;
    static int islandFund;
    static int crossed;
    static int nextEvent;
    static spell[][] spells;
    static weapon[][] weapons;
    static playerData[] arr;
    static lockoutGoal[] goals;
    static ArrayList<event> lst = new ArrayList<>();
    static ArrayList<event> curevent = new ArrayList<>();
    static event weather = new event();
    static event season = new event();
    static int temperature;
    static event temp = new event();
    static event disaster = new event();
    static ArrayList<tuple>[] bridges;
    static int[] location;
    static ArrayList<String> eventLog = new ArrayList<>();
    static int playersAlive;
    static int potionnum = 35; //lvls 0-12
    static potion[] potionShop;
    static ArrayList<peffect> potionEffects = new ArrayList<>();
    static String[] eventTypes = {"Spell_Damage","Neutral_Damage","Melee_Damage","Ranged_Damage","Weapon_Damage","Health_Regen","Mana_Regen","Spell_Cost",
    "ALL_DAMAGE","Earth_Damage","Earth_Defence","Thunder_Damage","Thunder_Defence","Water_Damage","Water_Defence","Fire_Damage","Fire_Defence","Air_Damage","Air_Defence"};
    static String[] lockoutTypes = {"Neutral Damage","Earth Damage","Thunder Damage","Water Damage","Fire Damage","Air Damage","Heal","Mana","Spell Damage","Melee Damage","Ranged Damage"};
    static int[] weapondps = {14,19,27,40,55,75}; //base
    static int[] spelldps = {28,35,48,65,85,112}; //base spell 1
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static String path;
    //@SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        int ans = 1;
        while (ans>=1 && ans<=8) {
            out.println("Which game?");
            out.println("1: Main Game A");
            out.println("2: Main Game B");
            out.println("3: RTP Cup");
            out.println("Other to quit");
            ans = Integer.parseInt(br.readLine());
            switch(ans) {
                case 1: path = "maina.json"; playerCount = 6; break;
                case 2: path = "mainb.json"; playerCount = 6; break;
                case 3: path = "rtpcup.json"; playerCount = 6; break;
                default: return;
            }
            //path = args[0];
            input();
            int menuOpt = 1;
            while (menuOpt>=1 && menuOpt<=3) {
                out.println("Turn: "+turn+"-"+subturn+" : "+arr[subturn-1].getName());
                out.println("1: Play turn");
                out.println("2: Go back a turn");
                out.println("3: Skip turn");
                out.println("Other: Quit");
                menuOpt = Integer.parseInt(br.readLine());
                if (menuOpt==1) {
                    nextTurn();
                    crossed = 0;
                }
                else if (menuOpt==2) {
                    subturn--;
                    if (subturn<=0) {
                        turn--;
                        subturn = playerCount;
                    }
                }
                else if (menuOpt==3) {
                    subturn++;
                    if (subturn>playerCount) {
                        turn++;
                        subturn = 1;
                    }
                }
                output();
            }
            
        }
        br.close();
    }
    /**
     * Reads in the .json file.
     * @throws IOException
     */
    public static void input() throws IOException {
        Gson gson = new Gson();
        String content = new String(Files.readAllBytes(Paths.get(path)));
        gameData game = gson.fromJson(content, gameData.class);
        turn = game.turn;
        subturn = game.subturn;
        islandLim = game.islandLim;
        bridgeCost = game.bridgeCost;
        bridgesBuilt = game.bridgesBuilt;
        islandCost = game.islandCost;
        crossed = game.crossed;
        season = game.season;
        weather = game.weather;
        temperature = game.temperature;
        disaster = game.disaster;
        temp = game.temp;
        nextEvent = game.nextEvent;
        curevent = game.curevent;
        spells = game.spells;
        weapons = game.weapons;
        bridges = game.bridges;
        location = game.location;
        goals = game.goals;
        arr = game.arr;
        lst = game.lst;
        eventLog = game.eventLog;
        playersAlive = game.playersAlive;
        potionEffects = game.potionEffects;
        content = new String(Files.readAllBytes(Paths.get("potions.json")));
        game = gson.fromJson(content, gameData.class);
        potionShop = game.potionShop;
    }
    /**
     * Outputs the game's data to the .json file.
     * @throws IOException
     */
    public static void output() throws IOException {
        gameData game = new gameData();
        game.version = "2.7.10";
        game.turn = turn;
        game.subturn = subturn;
        game.islandLim = islandLim;
        game.bridgeCost = bridgeCost;
        game.bridgesBuilt = bridgesBuilt;
        game.islandCost = islandCost;
        game.crossed = crossed;
        game.season = season;
        game.weather = weather;
        game.temperature = temperature;
        game.temp = temp;
        game.nextEvent = nextEvent;
        game.curevent = curevent;
        game.spells = spells;
        game.weapons = weapons;
        game.bridges = bridges;
        game.location = location;
        game.goals = goals;
        game.arr = arr;
        game.lst = lst;
        game.eventLog = eventLog;
        game.playersAlive = playersAlive;
        game.potionEffects = potionEffects;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String content = gson.toJson(game);
        Files.write(Paths.get(path),content.getBytes());
    }
    /**
     * Does the next turn.
     * @throws Exception
     */
    public static void nextTurn() throws Exception {
        char choice = ' ';
        while (choice!='Z') {
            out.println("Actions - "+(arr[subturn-1].getName()));
            out.println("Island #: "+location[subturn-1]);
            out.println("A: AP usage");
            out.println("B: Weapon attack");
            out.println("C: Spell attack");
            out.println("D: Gifting");
            out.println("E: (Re)Rolling");
            out.println("F: Potion Menu");
            out.println("G: Island Travel");
            out.println("H: Build Island/Bridge");
            out.println("I: Burn Bridge");
            out.println("X: Dev Modify");
            out.println("Y: Pause and save current turn");
            out.println("Z: End turn");
            choice = br.readLine().charAt(0);
            switch (choice) {
                case 'A': usePts(); break;
                case 'E': attackGen(subturn-1); break;
                case 'F': potionMenu(subturn-1); break;
                case 'G': traverse(subturn-1); break;
                case 'H': mapFund(subturn-1); break;
                case 'I': destroy(subturn-1); break;
                case 'X': devMode(); break;
                default: break;
            }
            if (choice=='Y') {
                return;
            }
            else if (choice>'A' && choice<'E') {
                out.print("Player/Island? ");
                String name = br.readLine();
                try {
                    int bleh = Integer.parseInt(name);
                    spell(bleh);
                }
                catch (NumberFormatException e) {
                    int i = findPlayer(name);
                    if (name.equals(arr[subturn-1].getName())) {
                        out.println("You can't attack yourself!");
                    }
                    else {
                        if (arr[i].getGL()!=arr[subturn-1].getGL()) {
                            out.println("They are in a different server!");
                            break;
                        }
                        switch (choice) {
                            case 'B': 
                                out.println("M: Melee");
                                out.println("R: Ranged");
                                //out.println("B: Both");
                                char ans = br.readLine().charAt(0);
                                if (ans=='M') {
                                    dmgCalc(i, false); 
                                }
                                else if (ans=='R') {
                                    dmgCalc(i, true);
                                }
                                break;
                            case 'C': spell(i); break;
                            case 'D': gift(i); break;
                        }
                    }
                }
            }
            output();
        }
        //END OF TURN SHENANIGANS
        subturn++;
        if (subturn>playerCount) {
            subturn = 1;
            turn++;
            Set<event> s = new HashSet<>();
            for (event e:lst) {
                if (e.getEnd()<turn) {
                    s.add(e);
                }
            }
            for (event e:s) {
                lst.remove(e);
            }
            s.clear();
            for (event e:curevent) {
                if (e.getEnd()<turn) {
                    s.add(e);
                }
            }
            for (event e:s) {
                curevent.remove(e);
            }
            if (turn==nextEvent) {
                eventMaker();
            }
            if (turn%10==1) lockoutGen();
            double hpmult=1+eventChecker("Health_Regen")/100.0;
            double manamult=1+eventChecker("Mana_Regen")/100.0;
            for (int i=0; i<playerCount; i++) {
                if (arr[i].getGL()==-1) continue;
                arr[i].addAP((turn%10>0 && turn%10<6) ? 4 : 5);
                arr[i].setHP(r2(Math.min(arr[i].getHPRegen()*hpmult+arr[i].getHP(),arr[i].getMaxHP())));
                for (lockoutProgress lp: arr[i].getLP()) {
                    if (lp.getType().equals("Heal")) {
                        lp.setValue(r2(lp.getValue()+arr[i].getHPRegen()*hpmult));
                    }
                }
                arr[i].setMana((int) Math.round(Math.min(arr[i].getMana()+arr[i].getMR()*manamult,arr[i].getMaxMana())));
                completeLockout(i);
            }
            Set<peffect> curEffects = new HashSet<>();
            for (peffect p:potionEffects) {
                p.lowerTurn();
                if (p.getTurns()==0) {
                    curEffects.add(p);
                    int i = findPlayer(p.getName());
                    switch(p.getType()) {
                        case "Health Regen": arr[i].setHPRegen(arr[i].getHPRegen()-p.getValue());
                        case "Mana Regen": arr[i].setMR(arr[i].getMR()-p.getValue());
                        case "Strength": arr[i].setElement(0, 0, arr[i].getElement(0,0)-p.getValue());
                        case "Dexterity": arr[i].setElement(0, 1, arr[i].getElement(0,1)-p.getValue());
                        case "Intelligence": arr[i].setElement(0, 2, arr[i].getElement(0,2)-p.getValue());
                        case "Defence": arr[i].setElement(0, 3, arr[i].getElement(0,3)-p.getValue());
                        case "Agility": arr[i].setElement(0, 4, arr[i].getElement(0,4)-p.getValue());
                    }
                }
            }
            for (peffect p:curEffects) {
                potionEffects.remove(p);
            }
            weatherGen();
            if (turn%3==1) seasonGen();
            tempGen();
            disasterGen();
        }
        output();
    }
    public static void usePotion(int i) throws IOException {
        if (arr[i].getPB()==null) {
            out.println("Your potion bag is empty!");
            return;
        }
        out.println("Your potion bag contains:");
        for (potion p:arr[i].getPB()) {
            out.println("+"+p.getValue()+" "+p.getType()+": "+p.getTurns()+"⇆");
        }
        out.print("Select potion to drink (1-"+arr[i].getPB().size()+"): ");
        int num = Integer.parseInt(br.readLine());
        if (num<1 || num>arr[i].getPB().size()) {
            return;
        }
        num--;
        Set<String> activeEffects = new HashSet<>();
        for (peffect p: potionEffects) {
            if (p.getName().equals(arr[i].getName())) activeEffects.add(p.getType());
        }
        if (activeEffects.contains(arr[i].getPB().get(num).getType())) {
            out.println("You already have this effect active!");
            return;
        }
        out.println("Consumed potion!");
        peffect p = new peffect(arr[i].getName(),arr[i].getPB().get(num).getType(),arr[i].getPB().get(num).getValue(),arr[i].getPB().get(num).getTurns());
        switch(p.getType()) {
            case "Health Regen": arr[i].setHPRegen(arr[i].getHPRegen()+p.getValue()); break;
            case "Mana Regen": arr[i].setMR(arr[i].getMR()+p.getValue()); break;
            case "Strength": arr[i].setElement(0, 0, arr[i].getElement(0,0)+p.getValue()); break;
            case "Dexterity": arr[i].setElement(0, 1, arr[i].getElement(0,1)+p.getValue()); break;
            case "Intelligence": arr[i].setElement(0, 2, arr[i].getElement(0,2)+p.getValue()); break;
            case "Defence": arr[i].setElement(0, 3, arr[i].getElement(0,3)+p.getValue()); break;
            case "Agility": arr[i].setElement(0, 4, arr[i].getElement(0,4)+p.getValue()); break;
        }
        potionEffects.add(p);
        arr[i].getPB().remove(num);
    }

    public static void dmgCalc(int i, boolean ranged) throws Exception {
        if (location[subturn-1]!=location[i] && !ranged) {
            out.println("You cannot attack them since they are on a different island!");
            return;
        }
        out.println("Attacking "+arr[i].getName());
        double sa = arr[subturn-1].getElement(0, 0);
        double da = arr[subturn-1].getElement(0, 1);
        double dd = arr[i].getElement(0, 3);
        double ag = arr[i].getElement(0, 4);
        double mult = eventChecker("ALL_DAMAGE")/100.0+eventChecker("Weapon_Damage")/100.0;
        sa*=(1+eventChecker("Strength")/100.0);
        da*=(1+eventChecker("Dexterity")/100.0);
        dd*=(1+eventChecker("Defence")/100.0);
        ag*=(1+eventChecker("Agility")/100.0);
        if (!ranged) mult+=eventChecker("Melee_Damage")/100.0;
        else mult+=eventChecker("Ranged_Damage")/100.0;
        sa = Math.max(-100,sa-dd);
        //ATTACKS
        double[] damages = {1,0,0,0,0,0};
        if (!ranged && weapons[subturn-1][0]!=null) {
            damages = weapons[subturn-1][0].rollDmg();
        }
        else if (ranged && weapons[subturn-1][1]!=null) {
            damages = weapons[subturn-1][1].rollDmg();
        }
        //DEFENCES
        int[] temp = arr[i].getElements()[1];
        double[] defences = new double[5];
        for (int j=0; j<5; j++) {
            defences[j] = temp[j];
        }
        double cdmg = 1;
        int dodge = (int) (Math.random()*100)+1;
        if (dodge<=ag) {
            out.println("The opponent dodged!");
            eventLog.add("Turn "+turn+"-"+subturn+": "+arr[i].getName()+" dodged "+arr[subturn-1].getName()+"'s attack");
            if (eventLog.size()>25) eventLog.remove(0);
            return;
        }
        int crit = (int) (Math.random()*100)+1;
        if (crit<=da) {
            out.println("Critical Hit!");
            cdmg=(2+sa/100.0);
        }
        //CALCULATIONS
        defences[0]*=(1+eventChecker("Earth_Defence")/100.0);
        defences[1]*=(1+eventChecker("Thunder_Defence")/100.0);
        defences[2]*=(1+eventChecker("Water_Defence")/100.0);
        defences[3]*=(1+eventChecker("Fire_Defence")/100.0);
        defences[4]*=(1+eventChecker("Air_Defence")/100.0);
        damages[0]*=((1+eventChecker("Neutral_Damage")/100.0)+mult);
        damages[1]*=((1+eventChecker("Earth_Damage")/100.0)+mult);
        damages[2]*=((1+eventChecker("Thunder_Damage")/100.0)+mult);
        damages[3]*=((1+eventChecker("Water_Damage")/100.0)+mult);
        damages[4]*=((1+eventChecker("Fire_Damage")/100.0)+mult);
        damages[5]*=((1+eventChecker("Air_Damage")/100.0)+mult);
        damages[0] = (damages[0]+arr[i].getND())*(1+sa/100.0)*cdmg;
        for (int j=1; j<6; j++) {
            damages[j] = (damages[j]+arr[i].getElement(0, j-1))*(1+sa/100.0)*cdmg;
        }
        if (!ranged) {
            for (int j=0; j<6; j++) {
                damages[j]*=(1-(crossed*20.0)/100.0);
            }
        }
        else {
            for (int j=0; j<6; j++) {
                damages[j]*=(1-(Math.abs(location[i]-location[subturn-1])*20.0)/100.0);
            }
        }
        damages[0] = Math.max(0,damages[0]);
        for (int j=1; j<6; j++) {
            damages[j] = Math.max(0,r2(damages[j]-defences[j-1]*2));
        }
        out.println("Damage dealt:");
        String[] dmgTypes = {"Neutral","Earth","Thunder","Water","Fire","Air"};
        double dmg = 0;
        for (int j=0; j<6; j++) {
            out.println(damages[j]+" "+dmgTypes[j]);
            dmg = r2(dmg+damages[j]);
        }
        for (lockoutProgress lp:arr[subturn-1].getLP()) {
            switch(lp.getType()) {
                case "Neutral Damage": lp.setValue(r2(lp.getValue()+damages[0])); break;
                case "Earth Damage": lp.setValue(r2(lp.getValue()+damages[1])); break;
                case "Thunder Damage": lp.setValue(r2(lp.getValue()+damages[2])); break;
                case "Water Damage": lp.setValue(r2(lp.getValue()+damages[3])); break;
                case "Fire Damage": lp.setValue(r2(lp.getValue()+damages[4])); break;
                case "Air Damage": lp.setValue(r2(lp.getValue()+damages[5])); break;
                case "Melee Damage":
                    if (!ranged) lp.setValue(r2(lp.getValue()+dmg)); break;
                case "Ranged Damage":
                    if (ranged) lp.setValue(r2(lp.getValue()+dmg)); break;
            }
        }
        arr[i].setHP(r2(arr[i].getHP()-dmg));
        if (!ranged) eventLog.add("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName()+" bonked "+arr[i].getName()+" for "+dmg+" damage");
        else eventLog.add("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName()+" shot "+arr[i].getName()+" for "+dmg+" damage");
        if (eventLog.size()>25) eventLog.remove(0);
        out.println(arr[i].getName()+" has "+arr[i].getHP()+" health remaining");
        arr[subturn-1].setLXP(r2(arr[subturn-1].getLXP()+dmg));
        out.println(arr[subturn-1].getName()+" gained "+dmg+" xp");
        out.println("Level "+arr[subturn-1].getLvl()+": "+arr[subturn-1].getLXP()+"/"+arr[subturn-1].getNL()+" xp");
        levelUp(subturn-1);
        isDead(i);
        completeLockout(subturn-1);
    }
    public static void usePts() throws IOException {
        out.println(arr[subturn-1].getAP()+" AP owned");
        out.print("How much AP to use? ");
        int x = 1000000000;
        while (arr[subturn-1].getAP()-x<0) {
            x = Integer.parseInt(br.readLine());
            if (arr[subturn-1].getAP()-x>=0) break;
            out.println("Error: Insufficient AP");
        }
        arr[subturn-1].addAP(-x);
        out.print("Stat? ");
        String use = br.readLine();
        switch(use) {
            case "Health": arr[subturn-1].addMaxHP(50*x); break;
            case "Health Regen": arr[subturn-1].addHPRegen(2*x); break;
            case "Mana": arr[subturn-1].addMaxMana(5*x); break;
            case "Mana Regen": arr[subturn-1].addMR(x); break;
            case "Neutral Damage": arr[subturn-1].addND(2*x); break;
            case "Strength": arr[subturn-1].addElement(0, 0, x); break;
            case "Dexterity": arr[subturn-1].addElement(0, 1, x); break;
            case "Intelligence": arr[subturn-1].addElement(0, 2, x); break;
            case "Defence": arr[subturn-1].addElement(0, 3, x); break;
            case "Agility": arr[subturn-1].addElement(0, 4, x); break;
            case "Earth Defence": arr[subturn-1].addElement(1, 0, x); break;
            case "Thunder Defence": arr[subturn-1].addElement(1, 1, x); break;
            case "Water Defence": arr[subturn-1].addElement(1, 2, x); break;
            case "Fire Defence": arr[subturn-1].addElement(1, 3, x); break;
            case "Air Defence": arr[subturn-1].addElement(1, 4, x); break;
        }
        out.println(arr[subturn-1].getAP()+" AP remaining");
    }
    public static void spell(int i) throws Exception {
        int num = 0;
        Set<Integer> s = new HashSet<>();
        while (num!=100) {
            out.println("Which spell to use? (1-5, 100 to quit) ");
            num = Integer.parseInt(br.readLine());
            if (num>0 && num<6) {
                if (spells[subturn-1][num-1] == null) {
                    out.println("This spell has not been unlocked yet!");
                }
                else {
                    double mc = spells[subturn-1][num-1].getMC();
                    double sr = Math.min(80,arr[subturn-1].getElement(0,2));
                    mc*=((1+eventChecker("Spell_Cost")/100.0)*(1-sr/100.0));
                    mc = Math.round(mc);
                    if (mc>arr[subturn-1].getMana()) {
                        out.println("You do not have enough mana to use this spell!");
                    }
                    else if (s.contains(num)) {
                        out.println("You already used this spell!");
                    }
                    else {
                        if (num!=5) {
                            spellCalc(i,num-1);
                            s.add(num);
                        }
                        else {
                            spellCalc(i,4);
                            s.add(5);
                        }
                    }
                }
            }
        }
    }
    public static void eventMaker() {
        int type = (int) (Math.random()*2)+1;
        int multiplier = ((int) (Math.random()*4)+1)*25;
        int dmg = (int) (Math.random()*eventTypes.length);
        if (type!=1) multiplier*=-1;
        int length = (int) (Math.random()*4)+2;
        nextEvent+=((int) (Math.random()*3)+1);
        event e = new event(multiplier,eventTypes[dmg],turn,turn+length-1);
        lst.add(e);
        curevent.add(e);
    }
    public static void isDead(int i) throws InterruptedException {
        if (arr[i].getHP()<=0) {
            out.println(arr[i].getName()+" has died!");
            eventLog.add(arr[i].getName()+" has died");
            if (eventLog.size()>25) eventLog.remove(0);
            arr[i].setHP(arr[i].getMaxHP()+0.0);
            arr[i].addLives(-1);
            if (arr[i].getLives()==0) {
                out.println(arr[i].getName()+" has lost the game L BOZO");
                eventLog.add(arr[i].getName()+" has been sent to the gulag");
                if (eventLog.size()>25) eventLog.remove(0);
                arr[i] = new playerData(arr[i].getName(), lockoutTypes.length);
                arr[i].setGL(-1);
                for (int j=0; j<5; j++) {
                    spells[i][j] = null;
                }
                weapons[i][0] = null;
                weapons[i][1] = null;
                lockoutProgress[] lp = new lockoutProgress[lockoutTypes.length];
                for (int j=0; j<lockoutTypes.length; j++) {
                    lp[j] = new lockoutProgress(lockoutTypes[j], 0.0);
                }
                arr[i].setLP(lp);
                if (arr[i].getGL()==0) playersAlive--;
                if (playersAlive==1) {
                    out.println("THE GAME HAS ENDED!");
                    for (int k=0; k<playerCount; k++) {
                        if (arr[k].getGL()==0) {
                            out.println("THE WINNER IS: "+arr[k].getName());
                            eventLog.add(arr[k].getName()+" HAS WON THE GAME");
                            if (eventLog.size()>25) eventLog.remove(0);
                            playersAlive = playerCount;
                            arr[k] = new playerData(arr[k].getName(), lockoutTypes.length);
                            for (int j=0; j<5; j++) {
                                spells[k][j] = null;
                            }
                            weapons[k][0] = null;
                            weapons[k][1] = null;
                            arr[k].setLP(lp);
                            break;
                        }
                    }
                    for (int k=0; k<playerCount; k++) {
                        arr[k].setGL(0);
                        arr[k].setAP(0);
                    }
                    for (peffect p:potionEffects) {
                        int k = findPlayer(p.getName());
                        switch(p.getType()) {
                            case "Health Regen": arr[k].setHPRegen(arr[k].getHPRegen()-p.getValue());
                            case "Mana Regen": arr[k].setMR(arr[k].getMR()-p.getValue());
                            case "Strength": arr[k].setElement(0, 0, arr[k].getElement(0,0)-p.getValue());
                            case "Dexterity": arr[k].setElement(0, 1, arr[k].getElement(0,1)-p.getValue());
                            case "Intelligence": arr[k].setElement(0, 2, arr[k].getElement(0,2)-p.getValue());
                            case "Defence": arr[k].setElement(0, 3, arr[k].getElement(0,3)-p.getValue());
                            case "Agility": arr[k].setElement(0, 4, arr[k].getElement(0,4)-p.getValue());
                        }
                    }
                    while (!potionEffects.isEmpty()) {
                        potionEffects.remove(0);
                    }
                }
            }
        }
    }
    public static void spellCalc(int i, int j) throws Exception { //i player/island (AOE), j spell num (0-4)
        double ia = arr[subturn-1].getElement(0, 2)*1.5;
        double nd = 0;
        if (j!=4) nd = arr[i].getElement(0, 3);
        double mc = spells[subturn-1][j].getMC();
        //ATTACKS
        double[] damages = spells[subturn-1][j].rollDmg();
        //DEFENCES
        double[] defences = {0,0,0,0,0};
        if (j!=4) {
            for (int k=0; k<5; k++) {
                defences[k] = arr[i].getElement(1, k);
            }
        }
        //CALCULATIONS
        ia*=(1+eventChecker("Intelligence")/100.0);
        nd*=(1+eventChecker("Defence")/100.0);
        defences[0]*=(1+eventChecker("Earth_Defence")/100.0);
        defences[1]*=(1+eventChecker("Thunder_Defence")/100.0);
        defences[2]*=(1+eventChecker("Water_Defence")/100.0);
        defences[3]*=(1+eventChecker("Fire_Defence")/100.0);
        defences[4]*=(1+eventChecker("Air_Defence")/100.0);
        double mult = eventChecker("Spell_Damage")/100.0+eventChecker("ALL_DAMAGE")/100.0;
        double sr = Math.min(80,arr[subturn-1].getElement(0,2));
        ia = Math.max(-100,ia-nd);
        mc*=((1+eventChecker("Spell_Cost")/100.0)*(1-sr/100.0));
        damages[0]*=((1+eventChecker("Neutral_Damage")/100.0)+mult);
        damages[1]*=((1+eventChecker("Earth_Damage")/100.0)+mult);
        damages[2]*=((1+eventChecker("Thunder_Damage")/100.0)+mult);
        damages[3]*=((1+eventChecker("Water_Damage")/100.0)+mult);
        damages[4]*=((1+eventChecker("Fire_Damage")/100.0)+mult);
        damages[5]*=((1+eventChecker("Air_Damage")/100.0)+mult);
        for (int k=0; k<5; k++) {
            damages[k+1]+=arr[subturn-1].getElement(0, k);
            damages[k+1]*=(1+ia/100.0);
        }
        damages[0] = Math.max(0,r2(damages[0]));
        for (int k=1; k<=5; k++) {
            damages[k] = Math.max(0,r2(damages[k]-defences[k-1]*2));
        }
        mc = Math.round(mc);
        int playersHit = 1;
        Set<Integer> s = new HashSet<>();
        if (j==4) {
            int count = 0;
            for (int k=0; k<playerCount; k++) {
                if (location[k]==i && arr[k].getGL()==0) {
                    if (k==subturn-1) count--;
                    s.add(k);
                    count++;
                }
            }
            playersHit = count;
        }
        out.println("Damage dealt:");
        String[] dmgTypes = {"Neutral","Earth","Thunder","Water","Fire","Air"};
        double dmg = 0;
        for (int k=0; k<6; k++) {
            out.println(damages[k]*playersHit+" "+dmgTypes[k]);
            dmg = r2(dmg+damages[k]*playersHit);
        }
        for (lockoutProgress lp:arr[subturn-1].getLP()) {
            switch(lp.getType()) {
                case "Neutral Damage": lp.setValue(r2(lp.getValue()+damages[0]*playersHit)); break;
                case "Earth Damage": lp.setValue(r2(lp.getValue()+damages[1]*playersHit)); break;
                case "Thunder Damage": lp.setValue(r2(lp.getValue()+damages[2]*playersHit)); break;
                case "Water Damage": lp.setValue(r2(lp.getValue()+damages[3]*playersHit)); break;
                case "Fire Damage": lp.setValue(r2(lp.getValue()+damages[4]*playersHit)); break;
                case "Air Damage": lp.setValue(r2(lp.getValue()+damages[5]*playersHit)); break;
                case "Heal": lp.setValue(r2(lp.getValue()+damages[6]*playersHit)); break;
                case "Spell Damage": lp.setValue(r2(lp.getValue()+dmg*playersHit)); break;
                case "Mana": lp.setValue(r2(lp.getValue()+mc)); break;
            }
        }
        if (j!=4) {
            arr[i].setHP(r2(arr[i].getHP()-dmg));
            eventLog.add("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName()+" spell "+(j+1)+"'d "+arr[i].getName()+" for "+dmg+" damage");
            if (eventLog.size()>25) eventLog.remove(0);
            out.println(arr[i].getName()+" has "+arr[i].getHP()+" health remaining");
            isDead(i);
        }
        else {
            for (int k:s) {
                arr[k].setHP(r2(arr[k].getHP()-dmg));
            }
            eventLog.add("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName()+" AOE'd island "+i+" for "+dmg+" damage");
            if (eventLog.size()>25) eventLog.remove(0);
            for (int k:s) {
                out.println(arr[k].getName()+" has "+arr[k].getHP()+" health remaining");
                isDead(k);
            }
        }
        out.println(arr[subturn-1].getName()+" healed "+damages[6]*playersHit+" health");
        arr[subturn-1].setHP(r2(Math.min(arr[subturn-1].getHP()+damages[6]*playersHit,arr[subturn-1].getMaxHP()+0.0)));
        out.println(arr[subturn-1].getName()+" now has "+arr[subturn-1].getHP()+" health");
        out.println(arr[subturn-1].getName()+" used "+mc+" mana on the spell");
        arr[subturn-1].setMana(arr[subturn-1].getMana()-(int) mc);
        out.println(arr[subturn-1].getName()+" has "+arr[subturn-1].getMana()+" mana remaining");
        arr[subturn-1].setLXP(arr[subturn-1].getLXP()+r2(arr[subturn-1].getLXP()+dmg*playersHit));
        out.println(arr[subturn-1].getName()+" gained "+dmg*playersHit+" xp");
        out.println("Level "+arr[subturn-1].getLvl()+": "+arr[subturn-1].getLXP()+"/"+arr[subturn-1].getNL()+" xp");
        levelUp(subturn-1);
        completeLockout(subturn-1);
    }
    public static void gift(int i) throws IOException {
        if (!path.equals("maina.json") || !path.equals("mainb.json")) {
            out.println("This is a tourney! You cannot gift things to other players.");
            return;
        }
        int choice = 0;
        while (choice!=100) {
            out.println("You have:");
            out.println(arr[subturn-1].getAP()+" ability points");
            out.println(arr[subturn-1].getHP()+" hp");
            out.println(arr[subturn-1].getMana()+" mana");
            out.println("----------");
            out.println("What would you like to gift? ");
            out.println("1 for ability points");
            out.println("2 for hp");
            out.println("3 for mana");
            out.println("100 to exit");
            choice = Integer.parseInt(br.readLine());
            if (choice==100) return;
            else if (choice==1) {
                out.print ("How many? ");
                int count = Integer.parseInt(br.readLine());
                if (count>arr[subturn-1].getAP()) {
                    out.println("You do not have enough ability points to do that");
                }
                else {
                    out.println("Gifted "+count+" ability points to "+arr[i].getName());
                    arr[subturn-1].setAP(arr[subturn-1].getAP()-count);
                    arr[i].addAP(count);
                    out.println(arr[subturn-1].getName()+" has "+arr[subturn-1].getAP()+" ability points remaining");
                    out.println(arr[i].getName()+" has "+arr[i].getAP()+" ability points remaining");
                }
            }
            else if (choice==2) {
                out.print ("How many? ");
                double count = Double.parseDouble(br.readLine());
                if (count>=arr[subturn-1].getHP()) {
                    out.println("You do not have enough health to do that");
                }
                else {
                    out.println("Gifted "+count+" health to "+arr[i].getName());
                    arr[subturn-1].setHP(r2(arr[subturn-1].getHP()-count));
                    arr[i].setHP(r2(Math.min(arr[i].getMaxHP(),arr[i].getHP()+count)));
                    out.println(arr[subturn-1].getName()+" has "+arr[subturn-1].getHP()+" health remaining");
                    out.println(arr[i].getName()+" has "+arr[i].getHP()+" health remaining");
                }
            }
            else if (choice==3) {
                out.print ("How many? ");
                int count = Integer.parseInt(br.readLine());
                if (count>arr[subturn-1].getMana()) {
                    out.println("You do not have enough mana to do that");
                }
                else {
                    out.println("Gifted "+count+" mana to "+arr[i].getName());
                    arr[subturn-1].setMana(arr[subturn-1].getMana()-count);
                    arr[i].setMana(Math.min(arr[i].getMaxMana(), arr[i].getMana()+count));
                    out.println(arr[subturn-1].getName()+" has "+arr[subturn-1].getMana()+" mana remaining");
                    out.println(arr[i].getName()+" has "+arr[i].getMana()+" mana remaining");
                }
            }
        }
    }
    public static void attackGen(int i) throws Exception {
        out.println("Which spell would you like to purchase/reroll?");
        out.println("1-4 for regular spell");
        out.println("5 for AOE spell");
        out.println("6 for melee weapon");
        out.println("7 for ranged weapon");
        int num = Integer.parseInt(br.readLine());
        if (num>7 || num<1) return;
        if (num==7) {
            int cost = 0;
            if (weapons[i][1]==null) {
                out.println("You are rolling your ranged weapon!");
                cost = 35;
            }
            else {
                out.println("You are rerolling your ranged weapon!");
                cost = weapons[i][1].getRC();
            }
            out.println("This will cost "+cost+" ap");
            out.print("Would you like to proceed? (y/n) ");
            char ans = br.readLine().charAt(0);
            if (ans=='y') {
                if (arr[i].getAP()<cost) {
                    out.println("You do not have ability points to do this!");
                }
                else {
                    arr[i].setAP(arr[i].getAP()-cost);
                    generator(num);
                    if (weapons[i][1].getRC()==0) weapons[i][1].setRC(1);
                    else weapons[i][1].setRC(Math.min(16,weapons[i][1].getRC()*2));
                }
            }
        }
        else if (num==6) {
            int cost = 0;
            if (weapons[i][0]==null) {
                out.println("You are rolling your melee weapon!");
                cost = 25;
            }
            else {
                out.println("You are rerolling your melee weapon!");
                cost = weapons[i][0].getRC();
            }
            out.println("This will cost "+cost+" ap");
            out.print("Would you like to proceed? (y/n) ");
            char ans = br.readLine().charAt(0);
            if (ans=='y') {
                if (arr[i].getAP()<cost) {
                    out.println("You do not have ability points to do this!");
                }
                else {
                    arr[i].setAP(arr[i].getAP()-cost);
                    generator(num);
                    if (weapons[i][0].getRC()==0) weapons[i][0].setRC(1);
                    else weapons[i][0].setRC(Math.min(16,weapons[i][0].getRC()*2));
                }
            }
        }
        else {
            int cost = 0;
            if (spells[i][num-1]==null) {
                out.println("You are rolling your spell!");
                if (num!=5) cost = 10*(int) (Math.pow(2,num-1));
                else cost = 15;
            }
            else {
                out.println("You are rerolling your spell!");
                cost = spells[i][num-1].getRC();
            }
            out.println("This will cost "+cost+" ap");
            out.print("Would you like to proceed? (y/n) ");
            char ans = br.readLine().charAt(0);
            if (ans=='y' && arr[i].getAP()>=cost) {
                arr[i].setAP(arr[i].getAP()-cost);
                generator(num);
                if (spells[i][num-1].getRC()==0) spells[i][num-1].setRC((num!=5) ? num : 1);
                else spells[i][num-1].setRC(Math.min(16*num,cost*2));
            }
            else if (ans=='y' && arr[i].getAP()<cost) {
                out.println("You do not have ability points to do this!");
            }
        }
    }
    public static void generator(int x) throws IOException { //true = spell, false = weapon
        String[] rarityName = {"Common","Unique","Rare","Legendary","Fabled","Mythic"};
        int[] dmgmult = {1,3,5,7};
        int[] manamult = {1,2,3,4};
        int rarity = (int) (Math.random()*100)+1;
        int dps; int manacost;
        if (rarity<=35)  rarity = 0;
        else if (rarity<=65) rarity = 1;
        else if (rarity<=85) rarity = 2;
        else if (rarity<=95) rarity = 3;
        else if (rarity<=99) rarity = 4;
        else rarity = 5;
        out.print("Name: ");
        String name = br.readLine();
        if (x<=4) {
            dps = spelldps[rarity]*dmgmult[x-1];
        }
        else if (x==5) {
            dps = spelldps[rarity];
        }
        else dps = weapondps[rarity];
        if (x<=4) {
            manacost = 40*manamult[x-1];
        }
        else if (x==5) {
            manacost = 60;
        }
        else manacost = 0;
        double multiplier = Math.random()*1+0.5;
        if (x<=5) dps = (int) (Math.round(dps*multiplier));
        manacost = (int) (Math.round(manacost*multiplier));
        int elements = (int) (Math.random()*10)+1;
        if (elements<=6) elements = 1;
        else if (elements<=9) elements = 2;
        else elements = 3;
        dps-=(elements-1)*5;
        int[][] minmax = new int[2][7];
        int base = -1;
        for (int a=elements-1; a>=0; a--) {
            int avgdps = (a==0) ? dps : (int) (Math.random()*(dps-a))+1;
            dps-=avgdps;
            int dmgtype;
            while (true) {
                int limit = (x<=5) ? 7 : 6;
                dmgtype = (int) (Math.random()*limit);
                if (base==-1) base = dmgtype;
                if (minmax[1][dmgtype]==0) break;
            }
            int range = (int) (Math.random()*avgdps);
            minmax[0][dmgtype] = avgdps-range;
            minmax[1][dmgtype] = avgdps+range;
        }
        out.println("Number of elements: "+elements);
        out.println("Rarity: "+rarityName[rarity]);
        int reroll = 0;
        if (x<=5) {
            if (spells[subturn-1][x-1]!=null) reroll = spells[subturn-1][x-1].getRC();
            spells[subturn-1][x-1] = new spell(minmax,manacost,reroll,name,rarityName[rarity]);
        }
        else {
            if (weapons[subturn-1][x-6]!=null) reroll = weapons[subturn-1][x-6].getRC();
            weapons[subturn-1][x-6] = new weapon(minmax,reroll,name,rarityName[rarity]);
        }
    }
    public static void potionMenu(int i) throws IOException {
        out.println("Options:");
        out.println("D: Drink potion");
        out.println("S: Potion shop");
        char opt = br.readLine().charAt(0);
        if (opt=='D') {
            usePotion(i);
            return;
        }
        if (arr[i].getPB()==null) {
            arr[i].setPB(new ArrayList<>());
        }
        out.println("Which potion type would you like to buy?");
        out.println("1: HP Regen");
        out.println("2: Mana Regen");
        out.println("3: Strength");
        out.println("4: Dexterity");
        out.println("5: Intelligence");
        out.println("6: Defence");
        out.println("7: Agility");
        int choice = Integer.parseInt(br.readLine());
        int index = choice-1;
        out.println("Current "+potionShop[index].getType()+" potions for sale: ");
        String[] sizes = {"Tiny","Small","Medium","Large","Extra Large"};
        for (int j=0; j<potionnum; j+=7) {
            out.println(sizes[j/7]+" "+potionShop[index+j].getValue()+" potion - Cost: "+potionShop[index+j].getCost()
            +", Req Lvl: "+potionShop[index+j].getLvlReq());
        }
        out.println("Which would you like to buy? (1-"+potionnum/7+")");
        int num = Integer.parseInt(br.readLine());
        if (num<1 || num>potionnum/7) {
            return;
        }
        if (potionShop[index+(num-1)*7].getCost()>arr[i].getAP()) {
            out.println("You do not have enough ability points to purchase this item!");
        }
        else if (potionShop[index+(num-1)*7].getLvlReq()>arr[i].getLvl()) {
            out.println("You do not meet the level requirements to purchase this item!");
        }
        else {
            out.println("Purchased potion!");
            arr[i].getPB().add(potionShop[index+(num-1)*7]);
            arr[i].setAP(arr[i].getAP()-potionShop[index+(num-1)*7].getCost());
        }
    }
    public static void traverse(int i) throws IOException {
        out.println("You are on island "+location[i]);
        int dest = 0;
        while (dest!=100) {
            out.println("Possible locations to travel to:");
            for (tuple p:bridges[location[i]]) {
                if (p.second>=0) {
                    out.print(p.first+" ");
                    if (p.second==0) out.println("(Free)");
                    else out.println("(Toll - "+p.second+" ability points)");
                }
            }
            out.println("Which island to Ago to? (1-25), 100 to exit: ");
            dest = Integer.parseInt(br.readLine());
            if (dest==100) break;
            if (dest==location[i]) {
                out.println("You are already on this island!");
            }
            else {
                boolean exists = false;
                tuple bridge = new tuple();
                for (tuple b:bridges[location[i]]) {
                    if (b.first==dest && b.second!=-1) {
                        bridge = b;
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    out.println("There is no bridge connecting directly to it!");
                }
                else {
                    out.println("This traversal will cost "+bridge.second+" ability points!");
                    out.print("Would u like to travel? (y/n): ");
                    char ans = br.readLine().charAt(0);
                    if (ans=='y') {
                        if (arr[i].getAP()<bridge.second) {
                            out.println("Ew you're too poor to travel");
                        }
                        else if (crossed==5) {
                            out.println("You already reached your bridge crossing limit!");
                        }
                        else {
                            arr[i].setAP(arr[i].getAP()-bridge.second);
                            out.println("Travelled to island "+bridge.first);
                            location[i] = bridge.first;
                            crossed++;
                        }
                    }
                }
            }
        }
    }
    public static void mapFund(int i) throws IOException {
        int choice = 0;
        while (choice!=100) {
            out.println("What would you like to fund?");
            out.println("Press 1 for island");
            out.println("Press 2 for bridge");
            out.println("Press 100 to exit");
            choice = Integer.parseInt(br.readLine());
            if (choice==1) {
                out.println("You are currently funding island "+(islandCost+1));
                out.println("There is currently "+islandFund+"/"+(islandCost)+" ability points already funded");
                out.println("How many AP would you like to fund towards the island? ");
                int amt = Integer.parseInt(br.readLine());
                if (amt>arr[i].getAP()) {
                    out.println("You do not have enough ability points!");
                }
                else if (amt>islandCost-islandFund) {
                    out.println("You are funding too much!");
                }
                else {
                    arr[i].setAP(arr[i].getAP()-amt);
                    out.println("You have donated "+amt+" ability points to the island funding!");
                    islandFund+=amt;
                    fund(islandCost+1,-1);
                }
            }
            else if (choice==2) {
                int one = 0, two = 0;
                out.print("Enter the two numbers (or -1 to quit): ");
                StringTokenizer st = new StringTokenizer(br.readLine());
                while (st.hasMoreTokens()) {
                    one = Integer.parseInt(st.nextToken());
                    if (one==-1) return;
                    two = Integer.parseInt(st.nextToken());
                }
                int smol = 0;
                int big = 0;
                if (one<two) {
                    smol = one;
                    big = two;
                }
                else {
                    smol = two;
                    big = one;
                }
                int index = -1;
                for (int j=0; j<bridges[smol].size(); j++) {
                    if (bridges[smol].get(j).first==big) {
                        index = j;
                        break;
                    }
                }
                if (index==-1) {
                    tuple t = new tuple();
                    t.first = big;
                    t.second = -1;
                    t.third = 0;
                    bridges[smol].add(t);
                    index = bridges[smol].size()-1;
                }
                out.println("You are currently funding a bridge connecting islands "+smol+" and "+big);
                out.println("There is currently "+bridges[smol].get(index).third+"/"+bridgeCost+" ability points already funded");
                out.println("How many AP would you like to fund towards the island? ");
                int amt = Integer.parseInt(br.readLine());
                if (amt>arr[i].getAP()) {
                    out.println("You do not have enough ability points!");
                }
                else if (amt>bridgeCost-bridges[smol].get(index).third) {
                    out.println("You are funding too much!");
                }
                else {
                    arr[i].setAP(arr[i].getAP()-amt);
                    out.println("You have donated "+amt+" ability points to the island funding!");
                    bridges[smol].get(index).third+=amt;
                    fund(smol, big);
                }
            }
        }
    }
    public static void fund(int i, int j) {
        if (j==-1) {
            if (islandFund==islandCost) {
                out.println("Island "+(islandCost+1)+" has been unlocked!");
                islandFund = 0;
                islandCost++;
            }
        }
        else {
            for (int x=0; x<bridges[i].size(); x++) {
                if (bridges[i].get(x).first==j && bridges[i].get(x).second==-1 && bridges[i].get(x).third==bridgeCost) {
                    out.println("Bridge connecting islands "+i+" and "+j+" has been connected!");
                    bridges[i].get(x).second = 0;
                    bridges[i].get(x).third = 0;
                    tuple c = new tuple();
                    c.first = i;
                    c.second = 0;
                    c.third = 0;
                    int toll = (int) (Math.random()*10)+1;
                    if (toll==10) {
                        out.println("This bridge is a toll bridge!");
                        int price = (int) Math.ceil(bfs(i,j)/2.0);
                        out.println("The price is "+price+" ap to cross!");
                        bridges[i].get(x).second = price;
                        c.second = price;
                    }
                    else {
                        out.println("This bridge is a free bridge!");
                    }
                    bridges[j].add(c);
                    bridgesBuilt++;
                    if (bridgesBuilt==bridgeCost/2) {
                        bridgesBuilt = 0;
                        bridgeCost+=2;
                    }
                    break;
                }
            }
        }
    }
    public static void destroy(int i) throws IOException {
        out.println("You are burning a bridge!");
        int destroycost = bridgeCost*3/2;
        out.println("This will cost "+destroycost+" ability points");
        if (arr[i].getAP()<destroycost) {
            out.println("You do not have enough ability points to burn a bridge!");
            out.println("You need "+destroycost+" ability points!");
            return;
        }
        int one = 0, two = 0;
        out.print("Enter the two numbers (or -1 to quit): ");
        StringTokenizer st = new StringTokenizer(br.readLine());
        while (st.hasMoreTokens()) {
            one = Integer.parseInt(st.nextToken());
            if (one==-1) return;
            if (one<=0 || one>islandCost) {
                out.println("That bridge is illegal since island "+one+" does not exist yet");
            }
            two = Integer.parseInt(st.nextToken());
            boolean connected = false;
            for (tuple p:bridges[one]) {
                if (p.first==two && p.second!=-1) {
                    connected = true;
                    break;
                }
            }
            if (two<=0 || two>islandCost) {
                out.println("That bridge is illegal since island "+two+" does not exist yet");
            }
            else if (two==one) {
                out.println("You cannot built a bridge to itself!");
            }
            else if (!connected) {
                out.println("There is no bridge connecting these two islands!");
            }
        }
        out.println("You will be burning the bridge between islands "+one+" and "+two);
        out.println("Cost is: "+destroycost+" ability points!");
        out.print("Confirm to burn (y/n): ");
        char s = br.readLine().charAt(0);
        if (s=='y') {
            arr[i].setAP(arr[i].getAP()-destroycost);
            for (int j=0; j<bridges[one].size(); j++) {
                if (bridges[one].get(j).first==two) {
                    bridges[one].remove(j);
                    break;
                }
            }
            for (int j=0; j<bridges[two].size(); j++) {
                if (bridges[two].get(j).first==one) {
                    bridges[two].remove(j);
                    break;
                }
            }
            out.println("Burned bridge connecting islands "+one+" and "+two);
        }
    }
    public static int bfs(int start, int end) {
        int[] dist = new int[islandLim];
        for (int i=1; i<islandLim; i++) {
            dist[i] = -1;
        }
        Queue<Integer> q = new LinkedList<>();
        q.add(start);
        dist[start] = 0;
        while (!q.isEmpty()) {
            int v = q.poll();
            for (tuple u:bridges[v]) {
                if (dist[u.first]==-1 && u.second==0) {
                    dist[u.first] = dist[v]+1;
                }
            }
        }
        if (dist[end]==-1) return 1;
        return dist[end];
    }
    public static void weatherGen() {
        int[] intense = {75,300,75,50,25,100};
        int[] altint = {50,150};
        String[] type = {"Water_Damage","Thunder_Damage","Air_Damage","Earth_Damage","Earth_Damage","Fire_Damage"};
        String[] alttype = {"Air_Damage","Water_Damage"};
        weather.setBegin(turn);
        weather.setEnd(turn);
        int pop = (int) (Math.random()*100)+1;
        if (pop<=25) pop = 0;
        else if (pop<=35) pop = 1;
        else if (pop<=50) pop = 2;
        else if (pop<=65) pop = 3;
        else if (pop<=80) pop = 4;
        else pop = 5;
        if (pop==0 && temperature<=0) {
            weather.setIntensity(altint[0]);
            weather.setType(alttype[0]);
        }
        else if (pop==1 && temperature<=5) {
            weather.setIntensity(altint[1]);
            weather.setType(alttype[1]);
        }
        else {
            weather.setIntensity(intense[pop]);
            weather.setType(type[pop]);
        }
        lst.add(weather);
    }
    public static void seasonGen() {
        season.setBegin(turn);
        season.setEnd(turn+2);
        int[] intense = {50,75,25};
        String[] type = {"Air_Damage","Earth_Damage","Fire_Damage","Water_Damage"};
        if (turn%36!=0 && turn%36<=9) season.setType(type[0]);
        else if (turn%36<=18) season.setType(type[1]);
        else if (turn%36<=27) season.setType(type[2]);
        else season.setType(type[3]);
        if (turn%9!=0 && turn%9<=3) season.setIntensity(intense[0]);
        else if (turn%9<=6) season.setIntensity(intense[1]);
        else season.setIntensity(intense[2]);
        lst.add(season);
    }
    public static void tempGen() {
        int mintemp;
        int maxtemp;
        if (turn%36>=4 && turn%36<23) mintemp = -33+4*((turn-4)%36);
        else mintemp = 39-4*((turn+14)%36);
        maxtemp = mintemp+5;
        temperature = (int) (Math.random()*(maxtemp-mintemp+1))+mintemp;
        out.println("The temperature is "+temperature+"°C");
        temp = new event(temperature, "ALL_DAMAGE", turn, turn);
        lst.add(temp);
    }
    public static void disasterGen() {
        event v = new event();
        disaster = v;
        disaster.setBegin(turn);
        disaster.setEnd(turn);
        boolean isdisaster = false;
        if (weather.getIntensity()==50 && weather.getType().equals("Air_Damage")) {
            int n = (int) (Math.random()*100)+1;
            if (n<=20) {
                out.println("DISASTER: Blizzard");
                out.println("Effects: +150% Air Damage");
                disaster.setType("Air_Damage");
                disaster.setIntensity(150);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (turn%36<=18 && turn%36>9) {
            int n = (int) (Math.random()*100)+1;
            if (n<=5) {
                out.println("DISASTER: Earthquake");
                out.println("Effects: +150% Earth Damage");
                disaster.setType("Earth_Damage");
                disaster.setIntensity(150);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (weather.getIntensity()==50 && weather.getType().equals("Water_Damage")) {
            int n = (int) (Math.random()*100)+1;
            if (n<=10) {
                out.println("DISASTER: Tsunami");
                out.println("Effects: +150% Air Damage");
                disaster.setType("Water_Damage");
                disaster.setIntensity(150);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (turn%36<=27 && turn%36>18) {
            int n = (int) (Math.random()*100)+1;
            if (n<=5) {
                out.println("DISASTER: Volcano Eruption");
                out.println("Effects: +150% Fire Damage");
                disaster.setType("Fire_Damage");
                disaster.setIntensity(150);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (weather.getType().equals("Thunder_Damage")) {
            int n = (int) (Math.random()*100)+1;
            if (n<=25) {
                out.println("DISASTER: Lightning Storm");
                out.println("Effects: +250% Thunder Damage");
                disaster.setType("Thunder_Damage");
                disaster.setIntensity(250);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (isdisaster) {
            for (String s:eventTypes) {
                if (s.endsWith("_Damage") && !s.equals(disaster.getType()) && 
                !s.equals("Melee_Damage") && !s.equals("Spell_Damage") && !s.equals("Ranged_Damage")
                && !s.equals("Weapon_Damage")) {
                    event e = new event(-1000,s,turn,turn);
                    lst.add(e);
                }
            }
        }
        else {
            disaster = null;
        }
    }
    public static void levelUp(int i) throws IOException {
        while (arr[i].getLXP()>=arr[i].getNL()) {
            out.println(arr[i].getName()+" has levelled up!");
            arr[i].addLvl(1);
            arr[i].setLXP(r2(arr[i].getLXP()-arr[i].getNL()));
            arr[i].addNL(200*(arr[i].getLvl()/5+1));
            out.println("Would you like to upgrade your weapon by 1 tier or gain ability points?");
            out.println("1 for +1 tier to weapon/spell");
            out.println("2 to gain ability points");
            int ans = Integer.parseInt(br.readLine());
            if (ans==1) {
                out.print("1-5 for spell, 6 for melee weapon, 7 for ranged weapon: ");
                int num = Integer.parseInt(br.readLine());
                if (num==6 || num==7) {
                    for (int j=0; j<6; j++) {
                        if (weapons[i][num-6].getDmgs()[1][j]>0) {
                            weapons[i][num-6].addDmgs(0, j, weapons[i][num-6].getTier());
                        }
                    }
                    out.println("Stats ↑ "+weapons[i][num-6].getTier());
                    weapons[i][num-6].addTier(1);
                }
                else {
                    for (int j=0; j<7; j++) {
                        if (weapons[i][num-1].getDmgs()[1][j]>0) {
                            weapons[i][num-1].addDmgs(0, j, weapons[i][num-6].getTier());
                        }
                    }
                    out.println("Stats ↑ "+spells[i][num-1].getTier());
                    spells[i][num-1].addTier(1);
                }
            }
            else if (ans==2) {
                arr[i].addAP(arr[i].getLvl()*2);
                out.println("+"+(arr[i].getLvl()*2)+" AP");
            }
        }
    }
    public static void lockoutGen() {
        for (int i=0; i<playerCount; i++) {
            for (int j=0; j<lockoutTypes.length; j++) {
                arr[i].getLP()[j] = new lockoutProgress(lockoutTypes[j],0);
            }
        }
        int specificdmg = 50;
        int origmana = 20;
        int originalreward = 1;
        int totaldmg = 100;
        for (int i=0; i<6; i++) {
            int choice = (int) (Math.random()*lockoutTypes.length);
            switch (lockoutTypes[choice]) {
                case "Mana": goals[i] = new lockoutGoal(lockoutTypes[choice],origmana,originalreward); break;
                case "Spell Damage": case "Melee Damage": case "Weapon Damage": goals[i] = new lockoutGoal(lockoutTypes[choice],totaldmg,originalreward); break;
                default: goals[i] = new lockoutGoal(lockoutTypes[choice],specificdmg,originalreward); break;
            }
            specificdmg*=2;
            origmana*=2;
            originalreward*=2;
            totaldmg*=2;
        }
    }
    public static void completeLockout(int i) throws InterruptedException {
        for (int j=0; j<6; j++) {
            for (int k=0; k<lockoutTypes.length; k++) {
                if (goals[i].getType().equals(arr[i].getLP()[k].getType()) 
                && arr[i].getLP()[k].getValue()>goals[i].getValue()
                && !goals[i].getDone()) {
                    System.out.println(arr[i].getName()+" completed goal "+j);
                    arr[i].addAP(goals[i].getAPR());
                    goals[i].setDone(true);
                }
            }
        }
    }
    public static void devMode() throws IOException {
        out.println("Options");
        out.println("A: Ordering players");
        out.println("B: HP setting");
        out.println("C: Setting lives");
        out.println("D: Reset Game");
        char x = br.readLine().charAt(0);
        if (x=='A') {
            out.print("Type order of players: ");
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i=0; i<playerCount; i++) {
                arr[i].setName(st.nextToken());
            }
        }
        else if (x=='B') {
            out.print("Player name: ");
            int i = findPlayer(br.readLine());
            out.print("Set new HP: ");
            double h = Double.parseDouble(br.readLine());
            arr[i].setHP(h);
        }
        else if (x=='C') {
            out.print("Player name: ");
            int i = findPlayer(br.readLine());
            out.print("Set new lives (0 = Gulag): ");
            int h = Integer.parseInt(br.readLine());
            arr[i].setLives(h);
            if (h==0) {
                arr[i].kill();
            }
        }
        else if (x=='D') {
            for (int i=0; i<playerCount; i++) {
                arr[i] = new playerData(arr[i].getName(), lockoutTypes.length);
            }
        }
    }
    public static int findPlayer(String s) {
        for (int i=0; i<playerCount; i++) {
            if (arr[i].getName().equals(s)) {
                return i;
            }
        }
        return -1;
    }
    public static double r2(double n) {
        n = ((int) (n*100))/100.0;
        return n;
    }
    public static int eventChecker(String s) {
        int ans = 0;
        for (event e:lst) {
            if (e.getType().equals(s)) {
                ans+=e.getIntensity();
            }
        }
        return ans;
    }
}
