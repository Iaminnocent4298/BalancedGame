/*
"Balanced Game" - made by Iaminnocent4298, web server made by Creative0708
Version 2.8 - The Mild Update
*/
import java.util.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.*;

import static java.lang.System.out;
class tuple {
    /**
     * The other vertex of the bridge
     */
    int first;
    /**
     * The cost to cross the bridge
     */
    int second;
    /**
     * The amount of turns left before collapse
     */
    int third;
}
public class BalancedGame {
    static int turn;
    static int subturn;
    static int playerCount;
    static int islandLim = 26;
    static int islandCost;
    static int islandFund;
    static int nextEvent;
    static int lockoutReset;
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
    public static void main(String[] args) throws Exception {
        int ans = 1;
        while (ans>=1 && ans<=8) {
            out.println("DOUBLE XP EVENT ACTIVE UNTIL: 09/12/24");
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
        islandCost = game.islandCost;
        season = game.season;
        weather = game.weather;
        temperature = game.temperature;
        disaster = game.disaster;
        temp = game.temp;
        nextEvent = game.nextEvent;
        lockoutReset = game.lockoutReset;
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
        game.version = "2.8";
        game.turn = turn;
        game.subturn = subturn;
        game.islandLim = islandLim;
        game.islandCost = islandCost;
        game.season = season;
        game.weather = weather;
        game.temperature = temperature;
        game.disaster = disaster;
        game.temp = temp;
        game.nextEvent = nextEvent;
        game.lockoutReset = lockoutReset;
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
        int choice = 0;
        while (choice!=-1) {
            out.println("Actions - "+(arr[subturn-1].getName()));
            out.println("Island #: "+location[subturn-1]);
            out.println("1: AP usage");
            out.println("2: Weapon attack");
            out.println("3: Spell attack");
            out.println("4: Gifting");
            out.println("5: (Re)Rolling");
            out.println("6: Potion Menu");
            out.println("7: Map Menu");
            out.println("100: Dev Modify");
            out.println("0: Pause and save current turn");
            out.println("-1: End turn");
            choice = Integer.parseInt(br.readLine());
            switch (choice) {
                case 1: usePts(); break;
                case 5: attackGen(subturn-1); break;
                case 6: potionMenu(subturn-1); break;
                case 7: mapMenu(subturn-1); break;
                case 100: devMode(); break;
                default: break;
            }
            if (choice==0) {
                return;
            }
            else if (choice>1 && choice<5) {
                if (choice!=3) out.print("Player/Island? ");
                else out.print("Player? ");
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
                        if (!arr[i].getAlive()) {
                            out.println("The opponent is dead!");
                            break;
                        }
                        switch (choice) {
                            case 2: 
                                out.println("1: Melee");
                                out.println("2: Ranged");
                                out.println("3: Both");
                                int ans = Integer.parseInt(br.readLine());
                                if (ans==1) melee(i);
                                else if (ans==2) ranged(i);
                                else if (ans==3) {
                                    melee(i);
                                    ranged(i);
                                }
                                break;
                            case 3: spell(i); break;
                            case 4: gift(i); break;
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
            if (turn==lockoutReset) {
                lockoutGen();
                lockoutReset+=10;
            }
            double hpmult=1+eventChecker("Health_Regen")/100.0;
            double manamult=1+eventChecker("Mana_Regen")/100.0;
            for (int i=0; i<playerCount; i++) {
                if (!arr[i].getAlive()) continue;
                arr[i].addAP((turn%10>0 && turn%10<6) ? 4 : 5);
                arr[i].setHP(r2(Math.min(arr[i].getHPRegen()*hpmult+arr[i].getHP(),arr[i].getMaxHP())));
                for (lockoutProgress lp: arr[i].getLP()) {
                    if (lp.getType().equals("Heal")) {
                        lp.setValue(r2(lp.getValue()+arr[i].getHPRegen()*hpmult));
                    }
                }
                arr[i].setMana((int) Math.round(Math.min(arr[i].getMana()+arr[i].getMR()*manamult,arr[i].getMaxMana())));
                arr[i].setStamina(Math.min(arr[i].getMaxStamina(),arr[i].getStamina()+arr[i].getStaminaRegen()));
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
            //Decreasing timer for temporary bridges
            for (int i=0; i<26; i++) {
                for (int j=0; j<bridges[i].size(); j++) {
                    if (bridges[i].get(j).third!=-1) {
                        bridges[i].get(j).third--;
                        if (bridges[i].get(j).third==0) {
                            bridges[i].remove(j);
                        }
                    }
                }
            }
        }
        output();
    }
    /**
     * User selects which potion to consume.
     * @param i The ID of the player in the array
     * @throws IOException
     */
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

    public static void melee(int i) throws IOException {
        if (location[subturn-1]!=location[i]) {
            out.println("You cannot attack them since they are on a different island!");
            return;
        }
        out.println("Attacking "+arr[i].getName());
        double sa = Math.min(arr[subturn-1].getElement(0, 0),80);
        double da = Math.min(arr[subturn-1].getElement(0, 1),80);
        double dd = Math.min(arr[i].getElement(0, 3),80);
        double mult = (eventChecker("ALL_DAMAGE")+eventChecker("Weapon_Damage")+eventChecker("Melee_Damage"))/100.0;
        sa*=(1+eventChecker("Strength")/100.0);
        da*=(1+eventChecker("Dexterity")/100.0);
        dd*=(1+eventChecker("Defence")/100.0);
        sa = Math.max(-100,sa-dd);
        //ATTACKS
        double[] damages = {1,0,0,0,0,0};
        if (weapons[subturn-1][0]!=null) {
            damages = weapons[subturn-1][0].rollDmg();
        }
        //DEFENCES
        int[] temp = arr[i].getElements()[1];
        double[] defences = new double[5];
        for (int j=0; j<5; j++) {
            defences[j] = temp[j];
        }
        double cdmg = 1;
        int crit = (int) (Math.random()*100)+1;
        if (crit<=da) {
            out.println("Critical Hit!");
            cdmg=(2+sa/100.0);
        }
        //CALCULATIONS
        damages[0] = (damages[0]+arr[subturn-1].getND()*1.5)*(1+sa/100.0)*cdmg;
        for (int j=1; j<6; j++) {
            damages[j] = (damages[j]+arr[subturn-1].getElement(0, j-1))*(1+sa/100.0)*cdmg;
        }
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
        damages[0] = r2(Math.max(0,damages[0]));
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
                case "Melee Damage": lp.setValue(r2(lp.getValue()+dmg)); break;
            }
        }
        arr[i].setHP(r2(arr[i].getHP()-dmg));
        eventLog.add("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName()+" bonked "+arr[i].getName()+" for "+dmg+" damage");
        if (eventLog.size()>25) eventLog.remove(0);
        out.println(arr[i].getName()+" has "+arr[i].getHP()+" health remaining");
        arr[subturn-1].addLXP(dmg);
        out.println(arr[subturn-1].getName()+" gained "+dmg+" xp");
        out.println("Level "+arr[subturn-1].getLvl()+": "+arr[subturn-1].getLXP()+"/"+arr[subturn-1].getNL()+" xp");
        levelUp(subturn-1);
        completeLockout(subturn-1);
        isDead(i);
    }
    public static void ranged(int i) throws Exception {
        if (weapons[subturn-1][1]==null) {
            out.println("You don't own a ranged weapon!");
            return;
        }
        out.println("Attacking "+arr[i].getName());
        double sa = Math.min(arr[subturn-1].getElement(0, 0),80);
        double da = Math.min(arr[subturn-1].getElement(0, 1),80);
        double dd = Math.min(arr[i].getElement(0, 3),80);
        double ag = Math.min(arr[i].getElement(0, 4),80);
        double mult = (eventChecker("ALL_DAMAGE")+eventChecker("Weapon_Damage")+eventChecker("Ranged_Damage"))/100.0;
        sa*=(1+eventChecker("Strength")/100.0);
        da*=(1+eventChecker("Dexterity")/100.0);
        dd*=(1+eventChecker("Defence")/100.0);
        ag*=(1+eventChecker("Agility")/100.0);
        sa = Math.max(-100,sa-dd);
        //ATTACKS
        double[] damages = {1,0,0,0,0,0};
        if (weapons[subturn-1][1]!=null) {
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
            eventLog.add("Turn "+turn+"-"+subturn+": "+arr[i].getName()+" dodged "+arr[subturn-1].getName()+"'s shot");
            if (eventLog.size()>25) eventLog.remove(0);
            return;
        }
        int crit = (int) (Math.random()*100)+1;
        if (crit<=da) {
            out.println("Critical Hit!");
            cdmg=(2+sa/100.0);
        }
        //CALCULATIONS
        damages[0] = (damages[0]+arr[subturn-1].getND()*1.5)*(1+sa/100.0)*cdmg;
        for (int j=1; j<6; j++) {
            damages[j] = (damages[j]+arr[subturn-1].getElement(0, j-1))*(1+sa/100.0)*cdmg;
        }
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
        for (int j=0; j<6; j++) {
            damages[j]*=(1-(Math.abs(location[i]-location[subturn-1])*20.0)/100.0);
        }
        damages[0] = r2(Math.max(0,damages[0]));
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
                case "Ranged Damage": lp.setValue(r2(lp.getValue()+dmg)); break;
            }
        }
        arr[i].setHP(r2(arr[i].getHP()-dmg));
        eventLog.add("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName()+" shot "+arr[i].getName()+" for "+dmg+" damage");
        if (eventLog.size()>25) eventLog.remove(0);
        out.println(arr[i].getName()+" has "+arr[i].getHP()+" health remaining");
        arr[subturn-1].addLXP(dmg);
        out.println(arr[subturn-1].getName()+" gained "+dmg+" xp");
        out.println("Level "+arr[subturn-1].getLvl()+": "+arr[subturn-1].getLXP()+"/"+arr[subturn-1].getNL()+" xp");
        levelUp(subturn-1);
        completeLockout(subturn-1);
        isDead(i);
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
            case "Mana Regen": arr[subturn-1].addMR(x); break;
            case "Stamina": arr[subturn-1].addMaxStamina(5*x); break;
            case "Neutral Damage": arr[subturn-1].addND(x); break;
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
            default: out.println("That's not valid!"); arr[subturn-1].addAP(x); break;
        }
        out.println(arr[subturn-1].getAP()+" AP remaining");
    }
    public static void spell(int i) throws Exception {
        out.println("Which spell to use? (1-5, -1 to quit) ");
        int num = Integer.parseInt(br.readLine());
        if (num==-1) return;
        else if (num>0 && num<6) {
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
                else {
                    if (num!=5) {
                        spellCalc(i,num-1);
                    }
                    else {
                        spellCalc(i,4);
                    }
                }
            }
        }
    }
    public static void eventMaker() {
        int type = (int) (Math.random()*2)+1;
        int multiplier = ((int) (Math.random()*4)+1)*25;
        int dmg = (int) (Math.random()*eventTypes.length);
        if (eventTypes[dmg].equals("Spell_Cost")) {multiplier = ((int) (Math.random()*3)+1)*25;}
        if (type!=1) multiplier*=-1;
        int length = (int) (Math.random()*4)+2;
        nextEvent+=1;
        event e = new event(multiplier,eventTypes[dmg],turn,turn+length-1);
        lst.add(e);
        curevent.add(e);
    }
    public static void isDead(int i) {
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
                arr[i].setAlive(false);
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
                if (!arr[i].getAlive()) playersAlive--;
                if (playersAlive==1) {
                    out.println("THE GAME HAS ENDED!");
                    for (int k=0; k<playerCount; k++) {
                        if (arr[k].getAlive()) {
                            out.println("THE WINNER IS: "+arr[k].getName());
                            eventLog.add(arr[k].getName()+" HAS WON THE GAME");
                            if (eventLog.size()>25) eventLog.remove(0);
                            playersAlive = playerCount;
                            break;
                        }
                    }
                    for (int k=0; k<playerCount; k++) {
                        arr[k] = new playerData(arr[k].getName(), lockoutTypes.length);
                        arr[k].setAlive(true);
                        arr[k].setAP(0);
                        for (int z=0; z<5; z++) {
                            spells[k][z] = null;
                        }
                        weapons[k][0] = null;
                        weapons[k][1] = null;
                        arr[k].setLP(lp);
                        location[k] = 1;
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
                    lockoutReset = turn+10;
                }
            }
        }
    }
    public static void spellCalc(int i, int j) throws Exception { //i player/island (AOE), j spell num (0-4)
        double ia = arr[subturn-1].getElement(0, 2)*1.5;
        double da = Math.min(arr[subturn-1].getElement(0, 1),80);
        double nd = 0;
        double opponentAgility = 0;
        if (j!=4) nd = arr[i].getElement(0, 3);
        else {
            int count = 0;
            for (int k=0; k<playerCount; k++) {
                if (location[k]==i && arr[k].getAlive()) {
                    count++;
                    nd+=arr[k].getElement(0, 3);
                }
            }
            if (count>0) nd/=count;
        }
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
        else {
            int count = 0;
            for (int k=0; k<playerCount; k++) {
                if (location[k]==i && arr[k].getAlive()) {
                    count++;
                    for (int l=0; l<5; l++) {
                        defences[l] = arr[k].getElement(1, l);
                    }
                }
            }
            if (count>0) {
                for (int k=0; k<5; k++) {
                    defences[k]/=count;
                }
            }
        }
        //CALCULATIONS
        ia*=(1+eventChecker("Intelligence")/100.0);
        da*=(1+eventChecker("Dexterity")/100.0);
        nd*=(1+eventChecker("Defence")/100.0);
        double cdmg = 1;
        if (j!=4) {
            int dodge = (int) (Math.random()*100)+1;
            if (dodge<=opponentAgility) {
                out.println("The opponent dodged!");
                eventLog.add("Turn "+turn+"-"+subturn+": "+arr[i].getName()+" dodged "+arr[subturn-1].getName()+"'s spell");
                if (eventLog.size()>25) eventLog.remove(0);
                return;
            }
        }
        int crit = (int) (Math.random()*100)+1;
        if (crit<=da) {
            out.println("Critical Hit!");
            cdmg=(2+ia/100.0);
        }
        defences[0]*=(1+eventChecker("Earth_Defence")/100.0);
        defences[1]*=(1+eventChecker("Thunder_Defence")/100.0);
        defences[2]*=(1+eventChecker("Water_Defence")/100.0);
        defences[3]*=(1+eventChecker("Fire_Defence")/100.0);
        defences[4]*=(1+eventChecker("Air_Defence")/100.0);
        double mult = eventChecker("Spell_Damage")/100.0+eventChecker("ALL_DAMAGE")/100.0;
        double sr = Math.min(80,arr[subturn-1].getElement(0,2));
        ia = Math.max(-100,ia-nd);
        mc*=((1+eventChecker("Spell_Cost")/100.0)*(1-sr/100.0));
        damages[0] = (damages[0]+arr[subturn-1].getND()*1.5)*(1+ia/100.0)*cdmg;
        for (int k=0; k<5; k++) {
            damages[k+1]+=arr[subturn-1].getElement(0, k);
            damages[k+1]*=(1+ia/100.0)*cdmg;
        }
        damages[0]*=((1+eventChecker("Neutral_Damage")/100.0)+mult);
        damages[1]*=((1+eventChecker("Earth_Damage")/100.0)+mult);
        damages[2]*=((1+eventChecker("Thunder_Damage")/100.0)+mult);
        damages[3]*=((1+eventChecker("Water_Damage")/100.0)+mult);
        damages[4]*=((1+eventChecker("Fire_Damage")/100.0)+mult);
        damages[5]*=((1+eventChecker("Air_Damage")/100.0)+mult);
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
                if (location[k]==i && arr[k].getAlive()) {
                    if (k==subturn-1) count--;
                    s.add(k);
                    count++;
                }
            }
            playersHit = count;
        }
        out.println("Damage dealt (to each player):");
        String[] dmgTypes = {"Neutral","Earth","Thunder","Water","Fire","Air"};
        double dmg = 0;
        for (int k=0; k<6; k++) {
            out.println(damages[k]+" "+dmgTypes[k]);
            dmg = r2(dmg+damages[k]);
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
        }
        else {
            for (int k:s) {
                arr[k].setHP(r2(arr[k].getHP()-dmg));
            }
            eventLog.add("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName()+" AOE'd island "+i+" for "+dmg+" damage each");
            if (eventLog.size()>25) eventLog.remove(0);
            for (int k:s) {
                out.println(arr[k].getName()+" has "+arr[k].getHP()+" health remaining");
            }
        }
        out.println(arr[subturn-1].getName()+" healed "+damages[6]*playersHit+" health");
        arr[subturn-1].setHP(r2(Math.min(arr[subturn-1].getHP()+damages[6]*playersHit,arr[subturn-1].getMaxHP()+0.0)));
        out.println(arr[subturn-1].getName()+" now has "+arr[subturn-1].getHP()+" health");
        out.println(arr[subturn-1].getName()+" used "+mc+" mana on the spell");
        arr[subturn-1].setMana(arr[subturn-1].getMana()-(int) mc);
        out.println(arr[subturn-1].getName()+" has "+arr[subturn-1].getMana()+" mana remaining");
        arr[subturn-1].addLXP(dmg*playersHit);
        out.println(arr[subturn-1].getName()+" gained "+dmg*playersHit+" xp");
        out.println("Level "+arr[subturn-1].getLvl()+": "+arr[subturn-1].getLXP()+"/"+arr[subturn-1].getNL()+" xp");
        levelUp(subturn-1);
        completeLockout(subturn-1);
        if (j!=4) {
            isDead(i);
        }
        else {
            for (int k:s) {
                isDead(k);
            }
        }
    }
    public static void gift(int i) throws IOException {
        if (!(path.equals("maina.json") || path.equals("mainb.json"))) {
            out.println("This is a tourney! You cannot gift things to other players.");
            return;
        }
        int choice = 0;
        while (choice!=100) {
            out.println("You have:");
            out.println(arr[subturn-1].getAP()+" AP");
            out.println(arr[subturn-1].getHP()+" hp");
            out.println(arr[subturn-1].getMana()+" mana");
            out.println("----------");
            out.println("What would you like to gift? ");
            out.println("1 for AP");
            out.println("2 for hp");
            out.println("3 for mana");
            out.println("-1 to exit");
            choice = Integer.parseInt(br.readLine());
            if (choice==-1) return;
            else if (choice==1) {
                out.print ("How many? ");
                int count = Integer.parseInt(br.readLine());
                if (count>arr[subturn-1].getAP()) {
                    out.println("You do not have enough AP to do that");
                }
                else {
                    out.println("Gifted "+count+" AP to "+arr[i].getName());
                    arr[subturn-1].setAP(arr[subturn-1].getAP()-count);
                    arr[i].addAP(count);
                    out.println(arr[subturn-1].getName()+" has "+arr[subturn-1].getAP()+" AP remaining");
                    out.println(arr[i].getName()+" has "+arr[i].getAP()+" AP remaining");
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
                    out.println("You do not have AP to do this!");
                }
                else {
                    if (arr[i].getAmplifierCount()>0) {
                        out.print("Do you wish to use an amplifier? (y/n)");
                        ans = br.readLine().charAt(0);
                        if (ans=='y') {
                            arr[i].setAP(arr[i].getAP()-cost);
                            generator(num,true);
                            arr[i].addAmplifierCount(-1);
                        }
                        else {
                            arr[i].setAP(arr[i].getAP()-cost);
                            generator(num, false);
                        }
                    }
                    else {
                        arr[i].setAP(arr[i].getAP()-cost);
                        generator(num, false);
                    }
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
                    out.println("You do not have AP to do this!");
                }
                else {
                    if (arr[i].getAmplifierCount()>0) {
                        out.print("Do you wish to use an amplifier? (y/n)");
                        ans = br.readLine().charAt(0);
                        if (ans=='y') {
                            arr[i].setAP(arr[i].getAP()-cost);
                            generator(num,true);
                            arr[i].addAmplifierCount(-1);
                        }
                        else {
                            arr[i].setAP(arr[i].getAP()-cost);
                            generator(num, false);
                        }
                    }
                    else {
                        arr[i].setAP(arr[i].getAP()-cost);
                        generator(num, false);
                    }
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
                if (arr[i].getAmplifierCount()>0) {
                    out.print("Do you wish to use an amplifier? (y/n)");
                    ans = br.readLine().charAt(0);
                    if (ans=='y') {
                        arr[i].setAP(arr[i].getAP()-cost);
                        generator(num,true);
                        arr[i].addAmplifierCount(-1);
                    }
                    else {
                        arr[i].setAP(arr[i].getAP()-cost);
                        generator(num, false);
                    }
                }
                else {
                    arr[i].setAP(arr[i].getAP()-cost);
                    generator(num, false);
                }
                if (spells[i][num-1].getRC()==0) spells[i][num-1].setRC((num!=5) ? num : 1);
                else spells[i][num-1].setRC(Math.min(16*num,cost*2));
            }
            else if (ans=='y' && arr[i].getAP()<cost) {
                out.println("You do not have AP to do this!");
            }
        }
    }
    public static int rarityGen(boolean useAmplifier) {
        double rarity = (int) (Math.random()*100)+1;
        if (useAmplifier) {
            double multiplier = Math.random()*0.1+1;
            rarity*=multiplier;
        }
        if (rarity<=35)  rarity = 0;
        else if (rarity<=65) rarity = 1;
        else if (rarity<=85) rarity = 2;
        else if (rarity<=95) rarity = 3;
        else if (rarity<=99) rarity = 4;
        else rarity = 5;
        return (int) rarity;
    }
    public static void generator(int x, boolean useAmplifier) throws IOException { //1-5 = spell, 6-7 = weapon
        String[] rarityName = {"Common","Unique","Rare","Legendary","Fabled","Mythic"};
        int[] dmgmult = {1,3,5,7};
        int[] manamult = {1,2,3,4};
        int rarity = rarityGen(useAmplifier);
        int dps; int manacost;
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
        out.println("1: Drink potion");
        out.println("2: Potion shop");
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
            out.println("You do not have enough AP to purchase this item!");
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
    public static void mapMenu(int i) throws IOException {
        out.println("Options:");
        out.println("1: Travel");
        out.println("2: Build");
        out.println("3: Burn");
        int choice = Integer.parseInt(br.readLine());
        switch (choice) {
            case 1: traverse(i); break;
            case 2: build(i); break;
            case 3: destroy(i); break;
        }
    }
    /**
     * Travel to other islands
     * @param i The index of the current player
     * @throws IOException
     */
    public static void traverse(int i) throws IOException {
        out.println("You are on island "+location[i]);
        int dest = 0;
        while (dest!=100) {
            out.println("Possible locations to travel to:");
            for (tuple p:bridges[location[i]]) {
                if (p.second>=0) {
                    out.print(p.first+" ");
                    if (p.second==0) out.println("(Free)");
                    else out.println("(Toll - "+p.second+" AP)");
                }
            }
            out.println("Destination? -1 to exit: ");
            dest = Integer.parseInt(br.readLine());
            if (dest==-1) break;
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
                    int staminaCost = Math.abs(location[i]-dest);
                    if (arr[i].getAP()<bridge.second) {
                        out.println("You're too poor to travel");
                    }
                    else if (arr[i].getStamina()<staminaCost) {
                        out.println("You're too tired to travel!");
                    }
                    else {
                        arr[i].setAP(arr[i].getAP()-bridge.second);
                        out.println("Travelled to island "+bridge.first);
                        location[i] = bridge.first;
                        arr[i].addStamina(staminaCost*-1);
                    }
                }
            }
        }
    }
    public static void build(int i) throws IOException {
        int choice = 0;
        out.println("What would you like to build?");
        out.println("Press 1 for island");
        out.println("Press 2 for bridge");
        out.println("Other to exit");
        choice = Integer.parseInt(br.readLine());
        if (choice==1) {
            out.println("You are currently building island "+(islandCost+1));
            out.println("There is currently "+islandFund+"/"+(islandCost)+" AP already funded");
            out.println("How many AP would you like to fund towards the island? ");
            int amt = Integer.parseInt(br.readLine());
            if (amt>arr[i].getAP()) {
                out.println("You do not have enough AP!");
            }
            else if (amt>islandCost-islandFund) {
                out.println("You are funding too much!");
            }
            else {
                arr[i].setAP(arr[i].getAP()-amt);
                out.println("You have donated "+amt+" AP to the island funding!");
                islandFund+=amt;
                if (islandFund==islandCost) {
                    out.println("Island "+(islandCost+1)+" has been unlocked!");
                    islandFund = 0;
                    islandCost++;
                }
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
            out.println("You are currently building a bridge connecting islands "+smol+" and "+big);
            out.println("Your options are:");
            out.println("1: Free (6 AP)");
            out.println("2: Toll (12 AP)");
            out.println("3: Temporary (? AP)");
            out.print("Select an option (1-3): ");
            int option = Integer.parseInt(br.readLine());
            int cost = 0;
            if (option==1) cost = 6;
            else if (option==2) cost = 12;
            else if (option==3) {
                out.print("How many turns? ");
                int num = Integer.parseInt(br.readLine());
                cost = num;
            }
            if (cost>arr[i].getAP()) {
                out.println("Insufficient AP!");
            }
            else {
                arr[i].setAP(arr[i].getAP()-cost);
                out.println("Bridge connecting islands "+smol+" and "+big+" has been connected!");
                if (option==3) {
                    out.println("This bridge will last for "+cost+" turns before collapsing");
                }
                tuple a = new tuple();
                a.first = big;
                a.second = (cost==12) ? (int) Math.ceil(bfs(smol, big)/2.0) : 0;
                a.third = (option==3) ? cost : -1;
                tuple b = new tuple();
                b.first = smol;
                b.second = a.second;
                b.third = a.third;
                bridges[smol].add(a);
                bridges[big].add(b);
            }
        }
    }
    public static void destroy(int i) throws IOException {
        out.println("You are burning a bridge!");
        int destroycost = 9;
        out.println("This will cost "+destroycost+" AP");
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
        out.println("Cost is: "+destroycost+" AP!");
        for (int j=0; j<bridges[one].size(); j++) {
            if (bridges[one].get(j).first==two) {
                if (bridges[one].get(j).third!=-1) {
                    out.println("Since this bridge is temporary, the cost is reduced to "+bridges[one].get(j).third+" AP");
                    destroycost = bridges[one].get(j).third;
                }
            }
        }
        out.print("Confirm to burn (y/n): ");
        char s = br.readLine().charAt(0);
        if (s=='y') {
            if (arr[i].getAP()<destroycost) {
                out.println("You do not have enough AP to burn a bridge!");
                return;
            }
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
                if (dist[u.first]==-1) {
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
            out.println("Select a level up choice:");
            out.println("1 for +1 tier to weapon/spell");
            out.println("2 to gain AP");
            out.println("3 to increase stamina regen");
            out.println("4 to obtain reroll amplifier");
            int ans = -1;
            while (ans<1 || ans>4) {
                ans = Integer.parseInt(br.readLine());
                if (ans==1) {
                    out.print("1-5 for spell, 6 for melee weapon, 7 for ranged weapon: ");
                    int num = Integer.parseInt(br.readLine());
                    if (num==6 || num==7) {
                        for (int j=0; j<6; j++) {
                            if (weapons[i][num-6].getDmgs()[1][j]>0) {
                                weapons[i][num-6].addDmgs(0, j, weapons[i][num-6].getTier());
                                weapons[i][num-6].addDmgs(1, j, weapons[i][num-6].getTier());
                            }
                        }
                        out.println("Stats ↑ "+weapons[i][num-6].getTier());
                        weapons[i][num-6].addTier(1);
                    }
                    else {
                        for (int j=0; j<7; j++) {
                            if (spells[i][num-1].getDmgs()[1][j]>0) {
                                spells[i][num-1].addDmgs(0, j, spells[i][num-1].getTier());
                                spells[i][num-1].addDmgs(1, j, spells[i][num-1].getTier());
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
                else if (ans==3) {
                    arr[i].addStaminaRegen(1);
                    out.println("+1 Stamina Regen");
                }
                else if (ans==4) {
                    arr[i].addAmplifierCount(1);
                }
                else {
                    out.println("That's not a valid choice!");
                }
            }
            arr[i].addMaxMana(5);
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
    public static void completeLockout(int i) {
        for (int j=0; j<6; j++) {
            for (int k=0; k<lockoutTypes.length; k++) {
                if (goals[j].getType().equals(arr[i].getLP()[k].getType()) 
                && arr[i].getLP()[k].getValue()>goals[j].getValue()
                && !goals[j].getDone()) {
                    System.out.println(arr[i].getName()+" completed goal "+(j+1));
                    arr[i].addAP(goals[j].getAPR());
                    goals[j].setDone(true);
                }
            }
        }
    }
    public static void devMode() throws IOException {
        out.println("Options");
        out.println("1: Ordering players");
        out.println("2: HP setting");
        out.println("3: Setting lives");
        out.println("4: Reset Game");
        out.println("5: Add Stamina");
        out.println("6: Set Mana");
        out.println("7: Set AP");
        int x = Integer.parseInt(br.readLine());
        if (x==1) {
            out.print("Type order of players: ");
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i=0; i<playerCount; i++) {
                arr[i].setName(st.nextToken());
            }
        }
        else if (x==2) {
            out.print("Player name: ");
            int i = findPlayer(br.readLine());
            out.print("Set new HP: ");
            double h = Double.parseDouble(br.readLine());
            arr[i].setHP(h);
        }
        else if (x==3) {
            out.print("Player name: ");
            int i = findPlayer(br.readLine());
            out.print("Set new lives (0 = Gulag): ");
            int h = Integer.parseInt(br.readLine());
            arr[i].setLives(h);
            if (h==0) {
                arr[i].kill();
            }
        }
        else if (x==4) {
            lockoutProgress[] lp = new lockoutProgress[lockoutTypes.length];
            for (int j=0; j<lockoutTypes.length; j++) {
                lp[j] = new lockoutProgress(lockoutTypes[j], 0.0);
            }
            for (int k=0; k<playerCount; k++) {
                arr[k] = new playerData(arr[k].getName(), lockoutTypes.length);
                arr[k].setAlive(true);
                arr[k].setAP(0);
                arr[k].setLP(lp);
                for (int z=0; z<5; z++) {
                    spells[k][z] = null;
                }
                weapons[k][0] = null;
                weapons[k][1] = null;
                arr[k].setLP(lp);
                location[k] = 1;
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
            lockoutReset = turn+10;
        }
        else if (x==5) {
            out.print("Player name: ");
            int i = findPlayer(br.readLine());
            out.print("Add stamina: ");
            int s = Integer.parseInt(br.readLine());
            arr[i].addStamina(s);
        }
        else if (x==6) {
            out.print("Player name: ");
            int i = findPlayer(br.readLine());
            out.print("Set new Mana: ");
            int h = Integer.parseInt(br.readLine());
            arr[i].setMana(h);
        }
        else if (x==7) {
            out.print("Player name: ");
            int i = findPlayer(br.readLine());
            out.print("Set new AP: ");
            int h = Integer.parseInt(br.readLine());
            arr[i].setAP(h);
        }
    }
    public static int findPlayer(String s) {
        for (int i=0; i<playerCount; i++) {
            if (arr[i].getName().toLowerCase().equals(s.toLowerCase())) {
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
