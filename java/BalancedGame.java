/*
"Balanced Game" - made by Iaminnocent4298, web server made by Creative0708
Version 2.9 - En Garde!
*/
import java.util.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.*;

import static java.lang.System.out;
public class BalancedGame {
    static int turn;
    static int subturn;
    static int playerCount;
    static int islandLim = 26;
    static int islandsBuilt;
    static int lockoutReset;
    static playerData[] arr;
    static lockoutGoal[] goals = new lockoutGoal[12];
    static ArrayList<event> lst = new ArrayList<>();
    static ArrayList<event> curevent = new ArrayList<>();
    static event weather = new event();
    static event season = new event();
    static int temperature;
    static event temp = new event();
    static event tide = new event();
    static event disaster = new event();
    static island[] islandResources = new island[26];
    static ArrayList<bridge>[] bridges;
    static ArrayList<String> eventLog = new ArrayList<>();
    static int playersAlive;
    static int potionnum = 35; //lvls 0-12
    static potion[] potionShop;
    static ArrayList<peffect> potionEffects = new ArrayList<>();
    static String[] arrowTypes = {"Normal Arrow","Earth-Infused Arrow","Thunder-Infused Arrow","Water-Infused Arrow","Fire-Infused Arrow",
    "Air-Infused Arrow","Rainbow Arrow"};
    static String[] eventTypes = {"Spell Damage","Neutral Damage","Melee Damage","Ranged Damage","Weapon Damage","Health Regen","Mana Regen","Spell Cost",
    "All Damage","Earth Damage","Thunder Damage","Water Damage","Fire Damage","Air Damage","Strength","Dexterity","Intelligence","Defence","Agility"};
    static String[] lockoutTypes = {"Neutral Damage","Earth Damage","Thunder Damage","Water Damage","Fire Damage","Air Damage","Heal","Mana","Spell Damage","Melee Damage","Ranged Damage"};
    static int[] weapondps = {20,25,35,50,70,95}; //base
    static int[] spelldps = {30,35,45,60,80,105}; //base spell 1
    /**
     * The total "score" of each rarity of armour
     */
    static int[] armourWeight = {5,10,15,20,25,35};
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static String path;
    public static void main(String[] args) throws Exception {
        int ans = 1;
        while (ans>=1 && ans<=4) {
            out.println("Which game?");
            out.println("1: Main Game A");
            out.println("2: Main Game B");
            out.println("3: RTP Cup");
            out.println("4: RTP Second Chance");
            out.println("Other to quit");
            ans = Integer.parseInt(br.readLine());
            playerCount = 6;
            switch(ans) {
                case 1: path = "maina.json"; break;
                case 2: path = "mainb.json"; break;
                case 3: path = "rtpcup.json"; break;
                case 4: path = "rtp2chance.json"; break;
                default: return;
            }
            input();
            int menuOpt = 1;
            while (menuOpt>=1 && menuOpt<=3) {
                out.println("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName());
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
        season = game.season;
        weather = game.weather;
        temperature = game.temperature;
        disaster = game.disaster;
        temp = game.temp;
        tide = game.tide;
        islandsBuilt = game.islandsBuilt;
        lockoutReset = game.lockoutReset;
        curevent = game.curevent;
        bridges = game.bridges;
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
        game.version = "2.9.2";
        game.turn = turn;
        game.subturn = subturn;
        game.islandLim = islandLim;
        game.season = season;
        game.weather = weather;
        game.temperature = temperature;
        game.disaster = disaster;
        game.temp = temp;
        game.tide = tide;
        game.islandsBuilt = islandsBuilt;
        game.lockoutReset = lockoutReset;
        game.curevent = curevent;
        game.bridges = bridges;
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
        int attackerIndex = subturn-1;
        int choice = 0;
        while (choice!=-1) {
            out.println("Actions - "+(arr[attackerIndex].getName()));
            out.println("Island #: "+arr[attackerIndex].getLocation());
            out.println("1: Use AP");
            out.println("2: Weapon/Spell Menu");
            out.println("3: Other Menus");
            out.println("4: Gifting");
            out.println("5: Item Shop");
            out.println("100: Dev Modify");
            out.println("0: Pause and save current turn");
            out.println("-1: End turn");
            choice = Integer.parseInt(br.readLine());
            if (choice==0) {
                return;
            }
            else if (choice==1) {
                useAP();
            }
            else if (choice==2) {
                out.println("Choose a Menu:");
                out.println("1: Weapon Attack");
                out.println("2: Spell Attack");
                out.println("3: (Re)rolling");
                int WSChoice = Integer.parseInt(br.readLine());
                if (WSChoice==1) out.print("Player? ");
                else if (WSChoice==2) out.print("Player/Island? ");
                else if (WSChoice==3) rerollMenu(attackerIndex);
                if (WSChoice==1 || WSChoice==2) {
                    String name = br.readLine();
                    try {
                        int islandNum = Integer.parseInt(name);
                        if (WSChoice==2) {
                            if (arr[attackerIndex].getModifier().equals("Confusion")) {
                                int confusionRoll = (int) (Math.random()*100)+1;
                                if (confusionRoll<=10) {
                                    out.println("You hit yourself in confusion!");
                                    addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" hit their own island in confusion!");
                                    spell(arr[attackerIndex].getLocation());
                                }
                                else spell(islandNum);
                            }
                            else spell(islandNum);
                        }
                        else {
                            out.println("Error! This weapon cannot attack entire islands at once.");
                        }
                    }
                    catch (NumberFormatException e) {
                        int targetIndex = findPlayer(name);
                        if (name.equals(arr[attackerIndex].getName().toLowerCase())) {
                            out.println("You can't attack yourself!");
                        }
                        else {
                            if (!arr[targetIndex].isAlive()) {
                                out.println("The opponent is dead!");
                            }
                            else if (WSChoice==1) {
                                out.println("Choose attack method:");
                                out.println("1: Melee");
                                out.println("2: Ranged");
                                out.println("3: Both");
                                int ans = Integer.parseInt(br.readLine());
                                if (ans==1) weaponAttack(targetIndex, true);
                                else if (ans==2) weaponAttack(targetIndex, false);
                                else if (ans==3) {
                                    if (arr[attackerIndex].getModifier().equals("Confusion")) {
                                        int confusionRoll = (int) (Math.random()*100)+1;
                                        if (confusionRoll<=10) {
                                            out.println("You hit yourself in confusion!");
                                            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" hit themselves in confusion!");
                                            weaponAttack(attackerIndex, true);
                                            weaponAttack(attackerIndex, false);
                                        }
                                        else {
                                            weaponAttack(targetIndex, true);
                                            weaponAttack(targetIndex, false);
                                        }
                                    }
                                    else {
                                        weaponAttack(targetIndex, true);
                                        weaponAttack(targetIndex, false);
                                    }
                                }
                            }
                            else if (WSChoice==2) {
                                if (arr[attackerIndex].getModifier().equals("Confusion")) {
                                    int confusionRoll = (int) (Math.random()*100)+1;
                                    if (confusionRoll<=10) {
                                        out.println("You hit yourself in confusion!");
                                        addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" hit themselves in confusion!");
                                        spell(attackerIndex);
                                    }
                                    else spell(targetIndex);
                                }
                                else spell(targetIndex);
                            }
                        }
                    }
                }
            }
            else if (choice==3) {
                out.println("Select a menu:");
                out.println("1: Potion");
                out.println("2: Map");
                out.println("3: Elemental Dust");
                out.println("4: Amplifier");
                int menuChoice = Integer.parseInt(br.readLine());
                if (menuChoice==1) potionMenu(subturn-1);
                else if (menuChoice==2) mapMenu(subturn-1);
                else if (menuChoice==3) dustMenu(subturn-1);
                else if (menuChoice==4) amplifierMenu(subturn-1);
            }
            else if (choice==4) {
                out.print("Player? ");
                String name = br.readLine();
                int i = findPlayer(name);
                gift(i);
            }
            else if (choice==5) {
                itemShop(subturn-1);
            }
            else if (choice==100) {
                devMode();
            }
            output();
        }
        subturn++;
        /**
         * MIDDLE OF TURN SHENANIGANS
         */
        //Decreasing timer for temporary bridges
        for (int i=0; i<26; i++) {
            for (int j=0; j<bridges[i].size(); j++) {
                if (bridges[i].get(j).getTurnsUntilCollapse()!=-1) {
                    bridges[i].get(j).decreaseCollapseTimer();
                    if (bridges[i].get(j).getTurnsUntilCollapse()==0) {
                        bridges[i].remove(j);
                    }
                }
            }
        }
        //Modifying potion durations
        Set<peffect> curEffects = new HashSet<>();
        for (peffect p:potionEffects) {
            if (subturn==p.getSubturnUsed()) {
                p.lowerTurn();
                if (p.getTurns()==0) {
                    curEffects.add(p);
                    int i = findPlayer(p.getName());
                    switch(p.getType()) {
                        case "Health Regen": arr[i].setHPRegen(arr[i].getHPRegen()-p.getValue()); break;
                        case "Mana Regen": arr[i].setMR(arr[i].getMR()-p.getValue()); break;
                        case "Strength": arr[i].setElement(0, 0, arr[i].getElement(0,0)-p.getValue()); break;
                        case "Dexterity": arr[i].setElement(0, 1, arr[i].getElement(0,1)-p.getValue()); break;
                        case "Intelligence": arr[i].setElement(0, 2, arr[i].getElement(0,2)-p.getValue()); break;
                        case "Defence": arr[i].setElement(0, 3, arr[i].getElement(0,3)-p.getValue()); break;
                        case "Agility": arr[i].setElement(0, 4, arr[i].getElement(0,4)-p.getValue()); break;
                    }
                }
            }
        }
        for (peffect p:curEffects) {
            potionEffects.remove(p);
        }
        //END OF TURN SHENANIGANS
        if (subturn>playerCount) {
            subturn = 1;
            turn++;
            double hpmult=1+eventChecker("Health Regen")/100.0;
            double manamult=1+eventChecker("Mana Regen")/100.0;
            for (int i=0; i<playerCount; i++) {
                if (!arr[i].isAlive()) continue;
                arr[i].addAP(5);
                arr[i].setHP(r2(Math.min(arr[i].getHPRegen()*hpmult+arr[i].getHP(),arr[i].getMaxHP())));
                if (arr[i].getModifier().equals("Decaying Heart")) {
                    arr[i].setHP(r2(arr[i].getHP()-arr[i].getMaxHP()*0.01));
                    isDead(i);
                }
                arr[i].addLockoutProgressValue("Heal",r2(arr[i].getHPRegen()*hpmult));
                arr[i].setMana((int) Math.round(Math.min(arr[i].getMana()+arr[i].getMR()*manamult,arr[i].getMaxMana())));
                arr[i].setStamina(Math.min(arr[i].getMaxStamina(),arr[i].getStamina()+arr[i].getStaminaRegen()));
                completeLockout(i);
            }

            int index = 0;
            while (index<lst.size()) {
                if (lst.get(index).getEnd()<turn) {
                    lst.remove(index);
                }
                else index++;
            }
            index = 0;
            while (index<curevent.size()) {
                if (curevent.get(index).getEnd()<turn) {
                    curevent.remove(index);
                }
                else index++;
            }
            eventGen();
            if (turn==lockoutReset) {
                lockoutGen();
                lockoutReset+=10;
            }
            weatherGen();
            if (turn%3==1) seasonGen();
            tempGen();
            disasterGen();
            tideGen();
        }
        output();
    }

    public static void useAP() throws IOException {
        out.println(arr[subturn-1].getAP()+" AP owned");
        out.println("Would u like to spend or remove AP?");
        out.println("1 to spend");
        out.println("2 to remove");
        int choice = Integer.parseInt(br.readLine());
        if (choice==2) {
            out.println("NOTICE: Removing AP will only return half the AP");
        }
        else if (choice!=1) return;
        out.print("How much AP to use/remove? ");
        int amount = 0;
        while (true) {
            amount = Integer.parseInt(br.readLine());
            if (choice==1 && arr[subturn-1].getAP()-amount>=0) break;
            else if (choice==2) break;
            out.println("Error: Insufficient AP");
        }
        if (choice==1) arr[subturn-1].addAP(-amount);
        else if (choice==2) {
            amount*=(-1);
            arr[subturn-1].addAP(-amount/2);
        }
        out.print("Stat? ");
        String use = br.readLine().toLowerCase();
        if ((use.equals("health regen") || use.equals("hp regen")) && arr[subturn-1].getModifier().equals("UHC")) {
            out.println("You have the UHC modifier active! You cannot allocate health regen.");
        }
        switch(use) {
            case "health": case "hp": arr[subturn-1].addMaxHP(50*amount); break;
            case "health regen": case "hp regen": arr[subturn-1].addHPRegen(2*amount); break;
            case "mana regen": arr[subturn-1].addMR(amount); break;
            case "neutral damage": case "neutral": arr[subturn-1].addND(amount); break;
            case "strength": case "str": arr[subturn-1].addElement(0, 0, amount); break;
            case "dexterity": case "dex": arr[subturn-1].addElement(0, 1, amount); break;
            case "intelligence": case "intel": case "int": arr[subturn-1].addElement(0, 2, amount); break;
            case "defence": case "def": arr[subturn-1].addElement(0, 3, amount); break;
            case "agility": case "agi": arr[subturn-1].addElement(0, 4, amount); break;
            default: out.println("That's not valid!"); arr[subturn-1].addAP(amount); break;
        }
        out.println(arr[subturn-1].getAP()+" AP remaining");
    }

    public static void weaponAttack(int targetIndex, boolean isMelee) throws IOException {
        /**
         * The index of the player attacking
         */
        int attackerIndex = subturn-1;
        if (isMelee & arr[attackerIndex].getLocation()!=arr[targetIndex].getLocation()) {
            out.println("You cannot attack them since they are on a different island!");
            return;
        }
        if (!isMelee & arr[attackerIndex].getWeapon(1)==null) {
            out.println("You do not own a bow!");
            return;
        }
        boolean hasArrow = false;
        String arrowType = "";
        if (!isMelee) {
            for (int i=0; i<arrowTypes.length; i++) {
                if (arr[attackerIndex].getInventoryValue(arrowTypes[i])>0) {
                    hasArrow = true;
                    break;
                }
            }
            if (!hasArrow) {
                out.println("You have no arrows!");
                return;
            }
            else {
                while (true) {
                    out.println("Choose an arrow type:");
                    for (int i=0; i<arrowTypes.length; i++) {
                        if (arr[attackerIndex].getInventoryValue(arrowTypes[i])>0) {
                            out.println((i+1)+": "+arrowTypes[i]);
                        }
                    }
                    int arrowChoice = Integer.parseInt(br.readLine());
                    if (arrowChoice<1 || arrowChoice>arrowTypes.length) {
                        out.println("Invalid choice!");
                    }
                    else {
                        if (arr[attackerIndex].getInventoryValue(arrowTypes[arrowChoice-1])==0) {
                            out.println("You do not have any of this arrow type!");
                        }
                        else {
                            arrowType = arrowTypes[arrowChoice-1];
                            arr[attackerIndex].addInventoryValue(arrowType, -1);
                            break;
                        }
                    }
                }
            }
        }
        out.println("Attacking "+arr[targetIndex].getName());
        double attackerStrength = Math.min(effectiveness(arr[attackerIndex].getElement(0, 0)+eventChecker("Strength")),99);
        if (arr[attackerIndex].getModifier().equals("Dumb and Weak")) {
            attackerStrength = 0;
        }
        double attackerDexterity = Math.min(effectiveness(arr[attackerIndex].getElement(0, 1)+eventChecker("Dexterity")),99);
        double defenderDefence = Math.min(effectiveness(arr[targetIndex].getElement(0, 3)+eventChecker("Defence"))*0.75,99);
        double defenderAgility = Math.min(effectiveness(arr[targetIndex].getElement(0, 4)+eventChecker("Agility")),99);
        double eventMultiplier = (eventChecker("All Damage")+eventChecker("Weapon Damage"))/100.0;
        if (isMelee) {
            eventMultiplier+=eventChecker("Melee Damage")/100.0;
        }
        else {
            eventMultiplier+=eventChecker("Ranged Damage")/100.0;
        }
        attackerStrength = Math.max(-100,attackerStrength-defenderDefence);

        if (arr[attackerIndex].getModifier().equals("Bullseyen't") && !isMelee) {
            int missRoll = (int) (Math.random()*100)+1;
            if (missRoll<=25) {
                out.println("You missed your shot!");
                addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" MISSED their shot!");
                return;
            }
        }

        double dodgeRoll = Math.random()*100;
        if (!isMelee && dodgeRoll<=defenderAgility) {
            out.println("The opponent dodged!");
            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[targetIndex].getName()+" dodged "+arr[attackerIndex].getName()+"'s shot");
            return;
        }

        //ATTACKS
        double[] weaponDamages = {1,0,0,0,0,0};
        if (isMelee && arr[attackerIndex].getWeapon(0)!=null) {
            weaponDamages = arr[attackerIndex].getWeapon(0).rollDmg();
        }
        else if (!isMelee && arr[attackerIndex].getWeapon(1)!=null) {
            weaponDamages = arr[attackerIndex].getWeapon(1).rollDmg();
            if (arrowType.equals("Earth-Infused Arrow")) {
                weaponDamages[1]+=5;
            }
            else if (arrowType.equals("Thunder-Infused Arrow")) {
                weaponDamages[2]+=5;
            }
            else if (arrowType.equals("Water-Infused Arrow")) {
                weaponDamages[3]+=5;
            }
            else if (arrowType.equals("Fire-Infused Arrow")) {
                weaponDamages[4]+=5;
            }
            else if (arrowType.equals("Air-Infused Arrow")) {
                weaponDamages[5]+=5;
            }
            else if (arrowType.equals("Rainbow Arrow")) {
                for (int i=1; i<6; i++) {
                    weaponDamages[i]+=5;
                }
            }
        }
        //DEFENCES
        int[] temp = arr[targetIndex].getElements()[1];
        double[] defenderDefences = new double[5];
        for (int j=0; j<5; j++) {
            defenderDefences[j] = temp[j]*0.75;
        }
        double critDamage = 1;
        double critRoll = Math.random()*100;
        if (critRoll<=attackerDexterity) {
            out.println("Critical Hit!");
            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" dealt a critical hit!");
            critDamage=(2+attackerStrength/100.0);
            if (arr[targetIndex].getModifier().equals("Brittle Bones")) {
                critDamage=(5+attackerStrength/100.0);
            }
        }
        //CALCULATIONS
        if (!arr[attackerIndex].getModifier().equals("\"The Normal One\"")) {
            weaponDamages[0] = (weaponDamages[0])*(1+arr[attackerIndex].getND()*1.5/100)*(1+attackerStrength/100.0)*critDamage
            *((1+eventChecker("Neutral Damage")/100.0)+eventMultiplier);
            if (arr[attackerIndex].getModifier().equals("April Sun")) {
                weaponDamages[0]*=0.5;
            }
            String[] elementDamageBuffs = {"Neutral Damage","Earth Damage","Thunder Damage","Water Damage","Fire Damage","Air Damage"};
            for (int j=1; j<6; j++) {
                weaponDamages[j] = (weaponDamages[j])*(1+arr[attackerIndex].getElement(0, j-1)/100)*(1+attackerStrength/100.0)*critDamage
                *((1+eventChecker(elementDamageBuffs[j])/100.0)+eventMultiplier);
                if (arr[attackerIndex].getModifier().equals("April Sun")) {
                    weaponDamages[j]*=0.5;
                }
            }
        }
        if (!isMelee) {
            for (int j=0; j<6; j++) {
                weaponDamages[j]*=(1-(Math.abs(arr[targetIndex].getLocation()-arr[attackerIndex].getLocation())*20.0)/100.0);
            }
        }
        weaponDamages[0] = r2(Math.max(0,weaponDamages[0]));
        for (int j=1; j<6; j++) {
            weaponDamages[j] = Math.max(0,r2(weaponDamages[j]-defenderDefences[j-1]));
        }
        out.println("Damage dealt:");
        String[] dmgTypes = {"Neutral","Earth","Thunder","Water","Fire","Air"};
        double totalDamage = 0;
        for (int j=0; j<6; j++) {
            out.println(weaponDamages[j]+" "+dmgTypes[j]);
            totalDamage = r2(totalDamage+weaponDamages[j]);
        }
        String[] damageTypes = {"Neutral Damage","Earth Damage","Thunder Damage","Water Damage","Fire Damage","Air Damage"};
        for (int j=0; j<6; j++) {
            arr[attackerIndex].addLockoutProgressValue(damageTypes[j], weaponDamages[j]);
        }
        if (isMelee) arr[attackerIndex].addLockoutProgressValue("Melee Damage", totalDamage);
        else arr[attackerIndex].addLockoutProgressValue("Ranged Damage", totalDamage);
        
        if (isMelee && (arr[targetIndex].getModifier().equals("Reflexless") || arr[targetIndex].getModifier().equals("Glass Cannon"))) {
            out.println("Modifier! "+arr[targetIndex].getName()+" took double damage");
            arr[targetIndex].setHP(r2(arr[targetIndex].getHP()-totalDamage*2));
        }
        else if (!isMelee && (arr[targetIndex].getModifier().equals("Easy Target") || arr[targetIndex].getModifier().equals("Glass Cannon"))) {
            out.println("Modifier! "+arr[targetIndex].getName()+" took double damage");
            arr[targetIndex].setHP(r2(arr[targetIndex].getHP()-totalDamage*2));
        }
        else {
            arr[targetIndex].setHP(r2(arr[targetIndex].getHP()-totalDamage));
        }
        if (isMelee) addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" bonked "+arr[targetIndex].getName()+" for "+totalDamage+" damage");
        else if (!isMelee) addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" shot "+arr[targetIndex].getName()+" for "+totalDamage+" damage");

        out.println(arr[targetIndex].getName()+" has "+arr[targetIndex].getHP()+" health remaining");
        arr[attackerIndex].addLXP((arr[attackerIndex].getModifier().equals("Slow Learner")) ? r2(totalDamage*0.5) : r2(totalDamage));
        out.println(arr[attackerIndex].getName()+" gained "+totalDamage+" xp");
        out.println("Level "+arr[attackerIndex].getLvl()+": "+arr[attackerIndex].getLXP()+"/"+arr[attackerIndex].getNL()+" xp");
        levelUp(attackerIndex);
        completeLockout(attackerIndex);
        isDead(targetIndex);
    }
    
    /**
     * The "Spell Attack" menu
     * @param targetIndex The index of the player, or island #, to be attacked
     * @throws Exception
     */
    public static void spell(int targetIndex) throws Exception {
        int attackerIndex = subturn-1;
        out.println("Which spell to use? (1-5, -1 to quit) ");
        int num = Integer.parseInt(br.readLine());
        if (num==-1) return;
        else if (num>0 && num<6) {
            if (arr[attackerIndex].getSpell(num-1) == null) {
                out.println("This spell has not been unlocked yet!");
            }
            else {
                double manaCost = arr[attackerIndex].getSpell(num-1).getMC();
                if (arr[attackerIndex].getModifier().equals("Amateur Wizard")) {
                    manaCost*=2;
                }
                double spellCostReduction = Math.min(95,effectiveness(arr[attackerIndex].getElement(0,2)));
                manaCost*=((1+eventChecker("Spell Cost")/100.0)*(1-spellCostReduction/100.0));
                manaCost = Math.round(manaCost);
                if (manaCost>arr[subturn-1].getMana()) {
                    out.println("You do not have enough mana to use this spell!");
                }
                else {
                    if (num!=5) {
                        spellCalc(targetIndex,num-1);
                    }
                    else {
                        spellCalc(targetIndex,4);
                    }
                }
            }
        }
    }

    /**
     * Calculates the spell damage
     * @param targetIndex The index of the player to be attacked, or the island number to be AOE'd
     * @param spellNum The index of the spell to be used
     * @throws Exception
     */
    public static void spellCalc(int targetIndex, int spellNum) throws Exception {
        int attackerIndex = subturn-1;
        double attackerIntelligence = Math.min(effectiveness(arr[attackerIndex].getElement(0, 2)+eventChecker("Intelligence"))*1.5,99);
        if (arr[attackerIndex].getModifier().equals("Dumb and Weak")) {
            attackerIntelligence = 0;
        }
        double attackerDexterity = Math.min(effectiveness(arr[attackerIndex].getElement(0, 1)+eventChecker("Dexterity")),99);
        double defenderDefence = 0;
        double defenderAgility = 0;
        if (spellNum!=4) defenderDefence = Math.min(effectiveness(arr[targetIndex].getElement(0, 3)+eventChecker("Agility")),99);
        else {
            int count = 0;
            for (int k=0; k<playerCount; k++) {
                if (arr[k].getLocation()==targetIndex && arr[k].isAlive()) {
                    count++;
                    defenderDefence+=arr[k].getElement(0, 3);
                }
            }
            if (count>0) defenderDefence/=count;
            defenderDefence = Math.min(effectiveness((int) defenderDefence+eventChecker("Defence"))*0.75,99);
        }
        double manaCost = arr[attackerIndex].getSpell(spellNum).getMC();
        //ATTACKS
        double[] spellDamages = arr[attackerIndex].getSpell(spellNum).rollDmg();
        //DEFENCES
        double[] defenderDefences = {0,0,0,0,0};
        if (spellNum!=4) {
            for (int k=0; k<5; k++) {
                defenderDefences[k] = arr[targetIndex].getElement(1, k)*0.75;
            }
        }
        else {
            int count = 0;
            for (int k=0; k<playerCount; k++) {
                if (arr[k].getLocation()==targetIndex && arr[k].isAlive()) {
                    count++;
                    for (int l=0; l<5; l++) {
                        defenderDefences[l] += arr[k].getElement(1, l)*0.75;
                    }
                }
            }
            if (count>0) {
                for (int k=0; k<5; k++) {
                    defenderDefences[k]/=count;
                }
            }
        }
        //CALCULATIONS

        if (arr[attackerIndex].getModifier().equals("Bullseyen't")) {
            int missRoll = (int) (Math.random()*100)+1;
            if (missRoll<=25) {
                out.println("You missed your spell!");
                addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" MISSED their spell!");
                return;
            }
        }

        double critDamage = 1;
        if (spellNum!=4) {
            int dodgeRoll = (int) (Math.random()*100)+1;
            if (dodgeRoll<=defenderAgility) {
                out.println("The opponent dodged!");
                addToEventLog("Turn "+turn+"-"+subturn+": "+arr[targetIndex].getName()+" dodged "+arr[attackerIndex].getName()+"'s spell");
                return;
            }
        }

        int critRoll = (int) (Math.random()*100)+1;
        if (critRoll<=attackerDexterity) {
            out.println("Critical Hit!");
            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" dealt a critical hit!");
            critDamage=(2+attackerIntelligence/100.0);
            if (arr[targetIndex].getModifier().equals("Brittle Bones")) {
                critDamage=(5+attackerIntelligence/100.0);
            }
        }
        double eventMultiplier = eventChecker("Spell Damage")/100.0+eventChecker("All Damage")/100.0;
        double sr = Math.min(95,effectiveness(arr[attackerIndex].getElement(0,2)));
        attackerIntelligence = Math.max(-100,attackerIntelligence-defenderDefence);
        manaCost*=((1+eventChecker("Spell Cost")/100.0)*(1-sr/100.0));
        if (arr[attackerIndex].getModifier().equals("Amateur Wizard")) {
            manaCost*=2;
        }

        if (!arr[attackerIndex].getModifier().equals("\"The Normal One\"")) {
            spellDamages[0] = (spellDamages[0])*(1+arr[attackerIndex].getND()*1.5/100)*(1+attackerIntelligence/100.0)*critDamage
            *((1+eventChecker("Neutral Damage")/100.0)+eventMultiplier);
            if (arr[attackerIndex].getModifier().equals("April Sun")) {
                spellDamages[0]*=0.5;
            }
            String[] elementDamageBuffs = {"Neutral Damage","Earth Damage","Thunder Damage","Water Damage","Fire Damage","Air Damage"};
            for (int j=1; j<6; j++) {
                spellDamages[j] = (spellDamages[j])*(1+arr[attackerIndex].getElement(0, j-1)/100)*(1+attackerIntelligence/100.0)*critDamage
                *((1+eventChecker(elementDamageBuffs[j])/100.0)+eventMultiplier);
                if (arr[attackerIndex].getModifier().equals("April Sun")) {
                    spellDamages[j]*=0.5;
                }
            }
        }
        spellDamages[0] = r2(Math.max(0,spellDamages[0]));
        for (int j=1; j<6; j++) {
            spellDamages[j] = Math.max(0,r2(spellDamages[j]-defenderDefences[j-1]));
        }

        manaCost = Math.round(manaCost);
        int playersHit = 1;
        Set<Integer> s = new HashSet<>();
        if (spellNum==4) {
            int count = 0;
            for (int k=0; k<playerCount; k++) {
                if (arr[k].getLocation()==targetIndex && arr[k].isAlive()) {
                    if (k==attackerIndex) count--;
                    s.add(k);
                    count++;
                }
            }
            playersHit = count;
        }
        out.println("Damage dealt (to each player):");
        String[] elementTypes = {"Neutral","Earth","Thunder","Water","Fire","Air"};
        double dmg = 0;
        for (int k=0; k<6; k++) {
            out.println(spellDamages[k]+" "+elementTypes[k]);
            dmg = r2(dmg+spellDamages[k]);
        }
        String[] damageTypes = {"Neutral Damage","Earth Damage","Thunder Damage","Water Damage","Fire Damage","Air Damage","Heal"};
        for (int k=0; k<6; k++) {
            arr[attackerIndex].addLockoutProgressValue(damageTypes[k], spellDamages[k]*playersHit);
        }
        arr[attackerIndex].addLockoutProgressValue("Spell Damage", dmg*playersHit);
        arr[attackerIndex].addLockoutProgressValue("Mana",manaCost);
        if (spellNum!=4) {
            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" spell "+(spellNum+1)+"'d "+arr[targetIndex].getName()+" for "+dmg+" damage");
            if (arr[targetIndex].getModifier().equals("Magic Doubter") || arr[targetIndex].getModifier().equals("Glass Cannon")) {
                out.println("Modifier! "+arr[targetIndex].getName()+" took double damage");
                dmg*=2;
            }
            arr[targetIndex].setHP(r2(arr[targetIndex].getHP()-dmg));
            out.println(arr[targetIndex].getName()+" has "+arr[targetIndex].getHP()+" health remaining");
        }
        else {
            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[attackerIndex].getName()+" AOE'd island "+targetIndex+" for "+dmg+" damage each");
            for (int k:s) {
                if (arr[k].getModifier().equals("Magic Doubter") || arr[k].getModifier().equals("Glass Cannon")) {
                    out.println("Modifier! "+arr[k].getName()+" took double damage");
                    arr[k].setHP(r2(arr[k].getHP()-dmg*2));
                }
                else {
                    arr[k].setHP(r2(arr[k].getHP()-dmg));
                }
                out.println(arr[k].getName()+" has "+arr[k].getHP()+" health remaining");
            }
        }
        out.println(arr[attackerIndex].getName()+" healed "+spellDamages[6]*playersHit+" health");
        arr[attackerIndex].setHP(r2(Math.min(arr[attackerIndex].getHP()+spellDamages[6]*playersHit,arr[attackerIndex].getMaxHP()+0.0)));
        out.println(arr[attackerIndex].getName()+" now has "+arr[attackerIndex].getHP()+" health");
        out.println(arr[attackerIndex].getName()+" used "+manaCost+" mana on the spell");
        arr[attackerIndex].setMana(arr[attackerIndex].getMana()-(int) manaCost);
        out.println(arr[attackerIndex].getName()+" has "+arr[attackerIndex].getMana()+" mana remaining");
        arr[attackerIndex].addLXP((arr[attackerIndex].getModifier().equals("Slow Learner")) ? r2(dmg*playersHit*0.5) : r2(dmg*playersHit));
        out.println(arr[attackerIndex].getName()+" gained "+dmg*playersHit+" xp");
        out.println("Level "+arr[attackerIndex].getLvl()+": "+arr[attackerIndex].getLXP()+"/"+arr[attackerIndex].getNL()+" xp");
        levelUp(attackerIndex);
        completeLockout(attackerIndex);
        if (spellNum!=4) {
            isDead(targetIndex);
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
                    out.println(arr[i].getName()+" has "+arr[i].getAP()+" AP now");
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
    public static void rerollMenu(int i) throws Exception {
        out.println("What would you like to purchase/reroll?");
        out.println("1-4 for regular spell");
        out.println("5 for AOE spell");
        out.println("6 for melee weapon");
        out.println("7 for ranged weapon");
        out.println("8-11 for armour");
        int num = Integer.parseInt(br.readLine());
        if (num>11 || num<1) return;
        if (num>=8 && num<=11) {
            int[] armourCosts = {8,16,12,8};
            int APCost = 0;
            if (arr[i].getArmour(num-8).getRerollCost()==0) {
                out.println("You are rolling your armour piece!");
                APCost = armourCosts[num-8];
            }
            else {
                out.println("You are rerolling your armour piece!");
                APCost = arr[i].getArmour(num-8).getRerollCost();
            }
            out.println("This will cost "+APCost+" ap");
            out.print("Would you like to proceed? (y/n) ");
            char ans = br.readLine().charAt(0);
            if (ans=='y') {
                if (arr[i].getAP()<APCost) {
                    out.println("You do not have AP to do this!");
                }
                else {
                    if (arr[i].getInventoryValue("Reroll Amplifier I")>0 || arr[i].getInventoryValue("Reroll Amplifier II")>0
                    || arr[i].getInventoryValue("Reroll Amplifier III")>0) {
                        out.print("Do you wish to use an amplifier? (y/n) ");
                        ans = br.readLine().charAt(0);
                        if (ans=='y') {
                            String[] amplifierTiers = {" I"," II"," III"};
                            out.print("Tier? ");
                            int tier = Integer.parseInt(br.readLine());
                            if (arr[i].getInventoryValue("Reroll Amplifier"+amplifierTiers[tier-1])==0) {
                                out.println("You do not have this amplifier!");
                                return;
                            }
                            else {
                                arr[i].setAP(arr[i].getAP()-APCost);
                                weaponGen(num,tier);
                                arr[i].addInventoryValue("Reroll Amplifier"+amplifierTiers[tier-1],-1);
                            }
                        }
                        else {
                            arr[i].setAP(arr[i].getAP()-APCost);
                            armourGen(i, num-8, 0);
                        }
                    }
                    else {
                        arr[i].setAP(arr[i].getAP()-APCost);
                        armourGen(i, num-8, 0);
                    }
                    if (arr[i].getArmour(num-8).getRerollCost()==0) arr[i].getArmour(num-8).setRerollCost(1);
                    else arr[i].getArmour(num-8).setRerollCost(Math.min(8,arr[i].getArmour(num-8).getRerollCost()*2));
                }
            }
        }
        else if (num==6 || num==7) {
            int cost = 0;
            if (arr[i].getWeapon((num==6) ? 0 : 1)==null) {
                if (num==6) {
                    out.println("You are rolling your melee weapon!");
                    cost = 12;
                }
                else {
                    out.println("You are rolling your ranged weapon!");
                    cost = 12;
                }
            }
            else {
                if (num==6) {
                    out.println("You are rerolling your melee weapon!");
                }
                else {
                    out.println("You are rerolling your ranged weapon!");
                }
                cost = arr[i].getWeapon((num==6) ? 0 : 1).getRC();
            }
            out.println("This will cost "+cost+" ap");
            out.print("Would you like to proceed? (y/n) ");
            char ans = br.readLine().charAt(0);
            if (ans=='y') {
                if (arr[i].getAP()<cost) {
                    out.println("You do not have AP to do this!");
                }
                else {
                    if (arr[i].getInventoryValue("Reroll Amplifier I")>0 || arr[i].getInventoryValue("Reroll Amplifier II")>0
                    || arr[i].getInventoryValue("Reroll Amplifier III")>0) {
                        out.print("Do you wish to use an amplifier? (y/n) ");
                        ans = br.readLine().charAt(0);
                        if (ans=='y') {
                            String[] amplifierTiers = {" I"," II"," III"};
                            out.print("Tier? ");
                            int tier = Integer.parseInt(br.readLine());
                            if (arr[i].getInventoryValue("Reroll Amplifier"+amplifierTiers[tier-1])==0) {
                                out.println("You do not have this amplifier!");
                                return;
                            }
                            else {
                                arr[i].setAP(arr[i].getAP()-cost);
                                weaponGen(num,tier);
                                arr[i].addInventoryValue("Reroll Amplifier"+amplifierTiers[tier-1],-1);
                            }
                        }
                        else {
                            arr[i].setAP(arr[i].getAP()-cost);
                            weaponGen(num, 0);
                        }
                    }
                    else {
                        arr[i].setAP(arr[i].getAP()-cost);
                        weaponGen(num, 0);
                    }
                    if (arr[i].getWeapon((num==6) ? 0 : 1).getRC()==0) arr[i].getWeapon((num==6) ? 0 : 1).setRC(1);
                    else arr[i].getWeapon((num==6) ? 0 : 1).setRC(Math.min(8,arr[i].getWeapon((num==6) ? 0 : 1).getRC()*2));
                }
            }
        }
        else {
            int cost = 0;
            if (arr[i].getSpell(num-1)==null) {
                out.println("You are rolling your spell!");
                if (num!=5) cost = 5*(int) (Math.pow(2,num-1));
                else cost = 7;
            }
            else {
                out.println("You are rerolling your spell!");
                cost = arr[i].getSpell(num-1).getRC();
            }
            out.println("This will cost "+cost+" ap");
            out.print("Would you like to proceed? (y/n) ");
            char ans = br.readLine().charAt(0);
            if (ans=='y' && arr[i].getAP()>=cost) {
                if (arr[i].getInventoryValue("Reroll Amplifier I")>0 || arr[i].getInventoryValue("Reroll Amplifier II")>0
                || arr[i].getInventoryValue("Reroll Amplifier III")>0) {
                    out.print("Do you wish to use an amplifier? (y/n) ");
                    ans = br.readLine().charAt(0);
                    if (ans=='y') {
                        String[] amplifierTiers = {" I"," II"," III"};
                        out.print("Tier? ");
                        int tier = Integer.parseInt(br.readLine());
                        if (arr[i].getInventoryValue("Reroll Amplifier"+amplifierTiers[tier-1])==0) {
                            out.println("You do not have this amplifier!");
                            return;
                        }
                        else {
                            arr[i].setAP(arr[i].getAP()-cost);
                            weaponGen(num,tier);
                            arr[i].addInventoryValue("Reroll Amplifier"+amplifierTiers[tier-1],-1);
                        }
                    }
                    else {
                        arr[i].setAP(arr[i].getAP()-cost);
                        weaponGen(num, 0);
                    }
                }
                else {
                    arr[i].setAP(arr[i].getAP()-cost);
                    weaponGen(num, 0);
                }
                if (arr[i].getSpell(num-1).getRC()==0) arr[i].getSpell(num-1).setRC(1);
                else arr[i].getSpell(num-1).setRC(Math.min(8,cost*2));
            }
            else if (ans=='y' && arr[i].getAP()<cost) {
                out.println("You do not have AP to do this!");
            }
        }
    }
    public static int rarityGen(int amplifierTier) {
        double rarity = (int) (Math.random()*100)+1;
        rarity*=(1+amplifierTier*5/100.0);
        if (arr[subturn-1].getModifier().equals("Colin Luck") && rarity>65) rarity = 65;
        if (rarity<=35)  rarity = 0;
        else if (rarity<=65) rarity = 1;
        else if (rarity<=85) rarity = 2;
        else if (rarity<=95) rarity = 3;
        else if (rarity<=99) rarity = 4;
        else rarity = 5;
        return (int) rarity;
    }
    public static void weaponGen(int x, int amplifierTier) throws IOException { //1-5 = spell, 6-7 = weapon
        String[] rarityName = {"Common","Unique","Rare","Legendary","Fabled","Mythic"};
        int[] dmgmult = {1,2,3,4};
        int rarity = rarityGen(amplifierTier);
        int dps; int manacost;
        out.print("Name: ");
        String name = br.readLine();
        if (x<=4) {
            dps = spelldps[rarity]*dmgmult[x-1];
        }
        else if (x==5) {
            dps = spelldps[rarity]*4/5;
        }
        else dps = weapondps[rarity];
        if (x<=5) {
            manacost = 60;
        }
        else manacost = 0;
        double multiplier = Math.random()*1+0.5;
        if (x<=5) dps = (int) (Math.round(dps*multiplier));
        manacost = (int) (Math.round(manacost*multiplier));
        int elements = (int) (Math.random()*100)+1;
        if (elements<=40) elements = 1;
        else if (elements<=70) elements = 2;
        else if (elements<=90) elements = 3;
        else if (elements<=97) elements = 4;
        else elements = 5;
        int[][] minmax = new int[2][7];
        ArrayList<Integer> damageTypes = new ArrayList<>();
        int elementsAdded = 0;
        while (elementsAdded<elements) {
            int elementType = (int) (Math.random()*((x<=5) ? 7 : 6));
            if (!damageTypes.contains(elementType)) {
                elementsAdded++;
                damageTypes.add(elementType);
            }
        }
        for (int i=0; i<dps; i++) {
            int index = (int) (Math.random()*elements);
            minmax[0][damageTypes.get(index)]++;
            minmax[1][damageTypes.get(index)]++;
        }
        for (int i=0; i<((x<=5) ? 7 : 6); i++) {
            int damageRange = (int) (Math.random()*minmax[0][i]/2);
            minmax[0][i]-=damageRange;
            minmax[1][i]+=damageRange;
        }
        out.println("Number of elements: "+elements);
        out.println("Rarity: "+rarityName[rarity]);
        int reroll = 0;
        if (x<=5) {
            if (arr[subturn-1].getSpell(x-1)!=null) reroll = arr[subturn-1].getSpell(x-1).getRC();
            arr[subturn-1].setSpell(x-1,new spell(minmax,manacost,reroll,name,rarityName[rarity]));
            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName()+" rolled a spell and got a "+rarityName[rarity]+"!");
        }
        else {
            if (arr[subturn-1].getWeapon(x-6)!=null) reroll = arr[subturn-1].getWeapon(x-6).getRC();
            arr[subturn-1].setWeapon(x-6, new weapon(minmax,reroll,name,rarityName[rarity]));
            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[subturn-1].getName()+" rolled a weapon and got a "+rarityName[rarity]+"!");
        }
    }

    /**
     * Generates the armour stats
     * @param playerNum The ID of the player in the array
     * @param armourType The type of armour (helmet, chestplate, leggings, boots)
     * @param amplifierTier The amplifier tier used
     */
    public static void armourGen(int playerNum, int armourType, int amplifierTier) throws IOException {
        out.print("Name: ");
        String name = br.readLine();
        if (arr[playerNum].getArmour(armourType)!=null) {
            arr[playerNum].addMaxHP(-arr[playerNum].getArmour(armourType).getMaxHealthBuff());
            int[] elementalDefences = arr[playerNum].getArmour(armourType).getElementalDefences();
            for (int i=0; i<5; i++) {
                arr[playerNum].addElement(1, i, -elementalDefences[i]);
            }
        }
        String[] rarityName = {"Common","Unique","Rare","Legendary","Fabled","Mythic"};
        double[] armourTypeBuff = {1,2,1.5,1};
        int rarityTier = rarityGen(amplifierTier);
        int armourScore = armourWeight[rarityTier];
        int healthScore = (int) (Math.random()*(armourScore*0.8-armourScore*0.4+1))+(int) (armourScore*0.4);
        int elementalDefenceScore = armourScore-healthScore;
        int maxHealthBuff = (int) (healthScore*armourTypeBuff[armourType]*25);
        int elementalDefenceBuff = (int) (elementalDefenceScore*armourTypeBuff[armourType]*5);
        int elementCount = (int) (Math.random()*100)+1;
        if (elementCount<=20) elementCount = 1;
        else if (elementCount<=40) elementCount = 2;
        else if (elementCount<=60) elementCount = 3;
        else if (elementCount<=80) elementCount = 4;
        else elementCount = 5;
        //out.println("Number of Elements: "+elementCount);

        // The number of elements with negative defence values
        int negativeElements = (int) (Math.random()*(elementCount-1))+1;
        int roundToNearest5 = (int) (Math.random()*armourWeight[rarityTier]);
        int negativeDefence = roundToNearest5-roundToNearest5%5;
        elementalDefenceBuff+=negativeDefence;
        ArrayList<Integer> elementalDefences = new ArrayList<>();
        ArrayList<Integer> negativeElementalDefences = new ArrayList<>();
        int elementsAdded = 0;
        while (elementsAdded<elementCount) {
            int elementType = (int) (Math.random()*5);
            if (!elementalDefences.contains(elementType)) {
                elementsAdded++;
                elementalDefences.add(elementType);
            }
        }
        elementsAdded = 0;
        while (elementsAdded<negativeElements) {
            int index = (int) (Math.random()*elementCount);
            if (!negativeElementalDefences.contains(elementalDefences.get(index))) {
                elementsAdded++;
                negativeElementalDefences.add(elementalDefences.get(index));
            }
        }

        int[] elementalDefence = new int[5];
        for (int i=0; i<elementalDefenceBuff/5; i++) {
            int index = (int) (Math.random()*elementCount);
            elementalDefence[elementalDefences.get(index)]+=5;
        }
        if (negativeElements>0) {
            for (int i=0; i<negativeDefence/5; i++) {
                int index = (int) (Math.random()*negativeElements);
                elementalDefence[negativeElementalDefences.get(index)]-=5;
            }
        }
        out.println("Rarity: "+rarityName[rarityTier]);
        int rerollCost = 0;
        if (arr[playerNum].getArmour(armourType)!=null) {
            rerollCost = arr[playerNum].getArmour(armourType).getRerollCost();
        }
        arr[playerNum].setArmour(armourType, new armour(maxHealthBuff, elementalDefence, name, rarityName[rarityTier], rerollCost));
        arr[playerNum].addMaxHP(maxHealthBuff);
        for (int i=0; i<5; i++) {
            arr[playerNum].addElement(1, i, elementalDefence[i]);
        }
        addToEventLog("Turn "+turn+"-"+subturn+": "+arr[playerNum].getName()+" rolled an armour piece and got a "+rarityName[rarityTier]+"!");
    }
    public static void potionMenu(int i) throws IOException {
        out.println("Options:");
        out.println("1: Drink potion");
        out.println("2: Potion shop");
        int opt = Integer.parseInt(br.readLine());
        if (opt==1) {
            usePotion(i);
        }
        else if (opt==2) {
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
        peffect p = new peffect(arr[i].getName(),arr[i].getPB().get(num).getType(),arr[i].getPB().get(num).getValue(),arr[i].getPB().get(num).getTurns(),subturn);
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
    public static void traverse(int playerIndex) throws IOException {
        out.println("You are on island "+arr[playerIndex].getLocation());
        int chosenDestination = 0;
        while (true) {
            out.println("Possible locations to travel to:");
            for (bridge p:bridges[arr[playerIndex].getLocation()]) {
                if (p.getTravelCost()>=0) {
                    out.print(p.getEndingIsland()+" ");
                    if (p.getTravelCost()==0) out.println("(Free)");
                    else out.println("(Toll - "+p.getTravelCost()+" AP)");
                }
            }
            out.println("Destination? -1 to exit: ");
            chosenDestination = Integer.parseInt(br.readLine());
            if (chosenDestination==-1) break;
            if (chosenDestination==arr[playerIndex].getLocation()) {
                out.println("You are already on this island!");
            }
            else {
                boolean bridgeExists = false;
                bridge bridgeToTraverse = new bridge(0,0,0,0);
                for (bridge b:bridges[arr[playerIndex].getLocation()]) {
                    if (b.getEndingIsland()==chosenDestination && b.getTravelCost()!=-1) {
                        bridgeToTraverse = b;
                        bridgeExists = true;
                        break;
                    }
                }
                if (!bridgeExists) {
                    out.println("There is no bridge connecting directly to it!");
                }
                else {
                    if (arr[playerIndex].getAP()<bridgeToTraverse.getTravelCost()) {
                        out.println("You're too poor to travel");
                    }
                    else if (arr[playerIndex].getStamina()==0 
                    || (arr[playerIndex].getStamina()<=1 && arr[playerIndex].getModifier().equals("Out of Shape"))) {
                        out.println("You're too tired to travel!");
                    }
                    else {
                        arr[playerIndex].setAP(arr[playerIndex].getAP()-bridgeToTraverse.getTravelCost());
                        out.println("Travelled to island "+bridgeToTraverse.getEndingIsland());
                        arr[playerIndex].setLocation(bridgeToTraverse.getEndingIsland());
                        arr[playerIndex].addStamina((arr[playerIndex].getModifier().equals("Out of Shape")) ? -2 : -1);
                    }
                }
            }
        }
    }

    /**
     * Menu to build islands and bridges
     * @param playerIndex The index of the player
     * @throws IOException
     */
    public static void build(int playerIndex) throws IOException {
        int inputChoice = 0;
        out.println("What would you like to build?");
        out.println("Press 1 for island");
        out.println("Press 2 for bridge");
        out.println("Other to exit");
        inputChoice = Integer.parseInt(br.readLine());
        if (inputChoice==1) {
            out.print("Confirm: (y/n) ");
            char confirmationChoice = br.readLine().charAt(0);
            if (confirmationChoice=='n') return;
            if (arr[playerIndex].getAP()<3) {
                out.println("You do not have enough AP!");
            }
            else {
                arr[playerIndex].setAP(arr[playerIndex].getAP()-3);
                out.println("A new island has been built!");
                addToEventLog("Turn "+turn+"-"+subturn+": "+arr[playerIndex].getName()+" built a new island");
            }
        }
        else if (inputChoice==2) {
            int startingIsland = 0, endingIsland = 0;
            out.print("Enter the two numbers (or -1 to quit): ");
            StringTokenizer st = new StringTokenizer(br.readLine());
            while (st.hasMoreTokens()) {
                startingIsland = Integer.parseInt(st.nextToken());
                if (startingIsland==-1) return;
                endingIsland = Integer.parseInt(st.nextToken());
            }
            out.println("You are currently building a bridge connecting islands "+startingIsland+" and "+endingIsland);
            out.println("Your options are:");
            out.println("1: Free (4 AP)");
            out.println("2: Toll (8 AP)");
            out.println("3: Temporary (? AP)");
            out.print("Select an option (1-3): ");
            int option = Integer.parseInt(br.readLine());
            int cost = 0;
            if (option==1) cost = 4;
            else if (option==2) cost = 8;
            else if (option==3) {
                out.print("How many turns? ");
                int num = Integer.parseInt(br.readLine());
                cost = num;
            }
            if (cost>arr[playerIndex].getAP()) {
                out.println("Insufficient AP!");
            }
            else {
                arr[playerIndex].setAP(arr[playerIndex].getAP()-cost);
                out.println("Bridge connecting islands "+startingIsland+" and "+endingIsland+" has been connected!");
                if (option==3) {
                    out.println("This bridge will last for "+cost+" turns before collapsing");
                    addToEventLog("Turn "+turn+"-"+subturn+": "+arr[playerIndex].getName()+" built a temporary bridge from "+startingIsland+" to "+endingIsland);
                }
                else if (option==2) {
                    addToEventLog("Turn "+turn+"-"+subturn+": "+arr[playerIndex].getName()+" built a toll bridge from "+startingIsland+" to "+endingIsland);
                }
                else if (option==1) {
                    addToEventLog("Turn "+turn+"-"+subturn+": "+arr[playerIndex].getName()+" built a bridge from "+startingIsland+" to "+endingIsland);
                }
                bridges[startingIsland].add(new bridge(endingIsland,(cost==8) ? (int) Math.ceil(bfs(startingIsland, endingIsland)/2.0) : 0,(option==3) ? cost : -1,subturn));
                bridges[endingIsland].add(new bridge(startingIsland,(cost==8) ? (int) Math.ceil(bfs(startingIsland, endingIsland)/2.0) : 0,(option==3) ? cost : -1,subturn));
            }
        }
    }

    /**
     * Menu to burn a bridge
     * @param playerIndex The index of the player
     * @throws IOException
     */
    public static void destroy(int playerIndex) throws IOException {
        out.println("You are burning a bridge!");
        int destroyCost = 6;
        out.println("This will cost "+destroyCost+" AP");
        int startingIsland = 0, endingIsland = 0;
        out.print("Enter the endingIsland numbers (or -1 to quit): ");
        StringTokenizer st = new StringTokenizer(br.readLine());
        while (st.hasMoreTokens()) {
            startingIsland = Integer.parseInt(st.nextToken());
            if (startingIsland==-1) return;
            if (startingIsland<=0 || startingIsland>islandsBuilt) {
                out.println("Error: Island "+startingIsland+" does not exist");
            }
            endingIsland = Integer.parseInt(st.nextToken());
            boolean bridgeExists = false;
            for (bridge p:bridges[startingIsland]) {
                if (p.getEndingIsland()==endingIsland && p.getTravelCost()!=-1) {
                    bridgeExists = true;
                    break;
                }
            }
            if (endingIsland<=0 || endingIsland>islandsBuilt) {
                out.println("Error: Island "+endingIsland+" does not exist");
            }
            else if (endingIsland==startingIsland) {
                out.println("You cannot built a bridge to itself!");
            }
            else if (!bridgeExists) {
                out.println("There is no bridge connecting these two islands!");
            }
        }
        out.println("You will be burning the bridge between islands "+startingIsland+" and "+endingIsland);
        out.println("Cost is: "+destroyCost+" AP!");
        for (int j=0; j<bridges[startingIsland].size(); j++) {
            if (bridges[startingIsland].get(j).getEndingIsland()==endingIsland) {
                if (bridges[startingIsland].get(j).getTurnsUntilCollapse()!=-1) {
                    out.println("Since this bridge is temporary, the cost is reduced to "+bridges[startingIsland].get(j).getTurnsUntilCollapse()+" AP");
                    destroyCost = bridges[startingIsland].get(j).getTurnsUntilCollapse();
                }
            }
        }
        out.print("Confirm to burn (y/n): ");
        char s = br.readLine().charAt(0);
        if (s=='y') {
            if (arr[playerIndex].getAP()<destroyCost) {
                out.println("You do not have enough AP to burn a bridge!");
                return;
            }
            arr[playerIndex].setAP(arr[playerIndex].getAP()-destroyCost);
            for (int j=0; j<bridges[startingIsland].size(); j++) {
                if (bridges[startingIsland].get(j).getEndingIsland()==endingIsland) {
                    bridges[startingIsland].remove(j);
                    break;
                }
            }
            for (int j=0; j<bridges[endingIsland].size(); j++) {
                if (bridges[endingIsland].get(j).getEndingIsland()==startingIsland) {
                    bridges[endingIsland].remove(j);
                    break;
                }
            }
            out.println("Burned bridge connecting islands "+startingIsland+" and "+endingIsland);
            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[playerIndex].getName()+" burned a bridge from "+startingIsland+" to "+endingIsland);
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
            for (bridge u:bridges[v]) {
                if (dist[u.getEndingIsland()]==-1) {
                    dist[u.getEndingIsland()] = dist[v]+1;
                }
            }
        }
        if (dist[end]==-1) return 1;
        return dist[end];
    }

    public static void dustMenu(int i) throws IOException {
        out.println("Would you like to add dust to weapons or upgrade dust?");
        out.println("1: Add dust");
        out.println("2: Upgrade dust tier");
        int choice = Integer.parseInt(br.readLine());
        if (choice==1) {
            out.println("Selection Menu: ");
            out.println("Spell: 1-5");
            out.println("Weapon: 6-7");
            out.println("Armour: 8-11");
            int optionType = Integer.parseInt(br.readLine());
            out.print("Type? ");
            String elementType = br.readLine().toLowerCase();
            elementType = elementType.substring(0,1).toUpperCase()+elementType.substring(1);
            out.print("Tier? ");
            String[] dustTiers = {" I"," II"," III"," IV"};
            int tier = Integer.parseInt(br.readLine());
            String item = elementType+" Dust"+dustTiers[tier-1];
            out.print("Amount? ");
            int amount = Integer.parseInt(br.readLine());
            if (!arr[i].inventoryRemovable(item,amount)) {
                out.println("You do not have enough of this item!");
                return;
            }

            int[] tierDamage = {1,5,21,85};
            int elementNum = -1;
            switch (elementType) {
                case "Earth": elementNum = 0; break;
                case "Thunder": elementNum = 1; break;
                case "Water": elementNum = 2; break;
                case "Fire": elementNum = 3; break;
                case "Air": elementNum = 4; break;
            }
            if (optionType>=1 && optionType<=5) {
                arr[i].getSpell(optionType-1).addDmgs(elementNum+1, amount*tierDamage[tier-1]);
                out.println("You increased the damage on your spell!");
            }
            else if (optionType>=6 && optionType<=7) {
                arr[i].getWeapon(optionType-6).addDmgs(elementNum+1, amount*tierDamage[tier-1]);
                out.println("You increased the damage on your weapon!");
            }
            else if (optionType>=8 && optionType<=11) {
                arr[i].getArmour(optionType-8).addDefences(elementNum, 2*amount*tierDamage[tier-1]);
                out.println("You increased the damage on your armour piece!");
            }
            arr[i].addInventoryValue(item, -amount);
        }
        else if (choice==2) {
            out.println("Do you wish to combine as much as possible?");
            out.println("1: Yes");
            out.println("2: No");
            int choice1 = Integer.parseInt(br.readLine());
            String[] dustTiers = {" I"," II"," III"," IV"};
            String[] elementTypes = {"Earth","Thunder","Water","Fire","Air"};
            if (choice1==1) {
                for (String elementType:elementTypes) {
                    for (int dustTier = 0; dustTier<dustTiers.length; dustTier++) {
                        String item = elementType+" Dust"+dustTiers[dustTier];
                        if (arr[i].getInventoryValue(item)<4) {
                            break;
                        }
                        else if (dustTiers[dustTier].equals(" IV")) {
                            break;
                        }
                        while (true) {
                            if (arr[i].getInventoryValue(item)<4) {
                                break;
                            }
                            arr[i].addInventoryValue(item, -4);
                            item = elementType+" Dust"+dustTiers[dustTier+1];
                            arr[i].addInventoryValue(item, 1);
                            item = elementType+" Dust"+dustTiers[dustTier];
                        }
                    }
                }
                return;
            }
            else if (choice1!=2) {
                return;
            }
            out.print("Type? ");
            String elementType = br.readLine().toLowerCase();
            elementType = elementType.substring(0,1).toUpperCase()+elementType.substring(1);
            out.print("Tier? ");
            int tier = Integer.parseInt(br.readLine());
            String item = elementType+" Dust"+dustTiers[tier-1];
            out.print("How many times? ");
            int repeat = Integer.parseInt(br.readLine());
            if (arr[i].getInventoryValue(item)<4) {
                out.println("You do not have enough of this dust to combine!");
                return;
            }
            else if (tier==4) {
                out.println("This is the highest tier of dust! You cannot upgrade anymore");
                return;
            }
            else {
                for (int j=0; j<repeat; j++) {
                    if (arr[i].getInventoryValue(item)<4) {
                        out.println("You do not have enough of this dust to combine!");
                        break;
                    }
                    arr[i].addInventoryValue(item, -4);
                    item = elementType+" Dust"+dustTiers[tier];
                    arr[i].addInventoryValue(item, 1);
                    item = elementType+" Dust"+dustTiers[tier-1];
                }
            }
        }
    }

    public static void amplifierMenu(int playerIndex) throws IOException {
        String[] amplifierTiers = {" I"," II"," III"};
        out.println("Welcome to the Amplifier Upgrader!");
        out.println("What tier would you like to upgrade?");
        int tier = Integer.parseInt(br.readLine());
        if (tier<=0 || tier>=3) {
            out.println("This is not legal!");
        }
        else {
            String amplifierName = "Reroll Amplifier"+amplifierTiers[tier-1];
            if (arr[playerIndex].getInventoryValue(amplifierName)<2) {
                out.println("You do not have enough 2+ of this amplifier tier to upgrade!");
                return;
            }
            int APCost = 0;
            if (tier==1) APCost = 1;
            else APCost = 3;
            out.println("It will cost "+APCost+" AP to combine!");
            if (arr[playerIndex].getAP()<APCost) {
                out.println("However, you do not have enough AP to combine...");
                return;
            }
            out.print("Would you like to continue? (y/n) ");
            char decision = br.readLine().charAt(0);
            if (decision=='y') {
                arr[playerIndex].addAP(-APCost);
                arr[playerIndex].addInventoryValue(amplifierName, -2);
                amplifierName = "Reroll Amplifier"+amplifierTiers[tier];
                arr[playerIndex].addInventoryValue(amplifierName, 1);
                out.println("Action Completed");
            }
            else {
                return;
            }
        }
    }

    /**
     * Item shop menu
     * @param playerIndex The index of the player
     * @throws IOException
     */
    public static void itemShop(int playerIndex) throws IOException {
        out.println("Welcome to the Item Shop!");
        out.println("Unfortunately we only have arrows for sale at the moment");
        out.println("What type of arrow would you like?");
        int[] arrowCost = {40,20,20,20,20,20,10}; //Arrows per AP
        for (int i=0; i<arrowTypes.length; i++) {
            out.println((i+1)+": "+arrowTypes[i]+" - Cost: 1 AP for "+arrowCost[i]);
        }
        int choice = Integer.parseInt(br.readLine());
        if (choice>=1 && choice<=arrowTypes.length) {
            out.print("How many bundles of arrows would you like to purchase? ");
            int bundleCount = Integer.parseInt(br.readLine());
            if (arr[playerIndex].getAP()<bundleCount) {
                out.println("You do not have enough AP to purchase this item!");
            }
            else {
                arr[playerIndex].addAP(-bundleCount);
                arr[playerIndex].addInventoryValue(arrowTypes[choice-1], bundleCount*arrowCost[choice-1]);
                out.println("Purchased "+bundleCount*arrowCost[choice-1]+" "+arrowTypes[choice-1]);
            }
        }
    }

    /**
     * Creates and adds game events to the "lst" arraylist
     */
    public static void eventGen() {
        int multiplier = ((int) (Math.random()*4)+1)*25;
        int dmg = (int) (Math.random()*eventTypes.length);
        if (eventTypes[dmg].equals("Spell Cost")) {multiplier = ((int) (Math.random()*3)+1)*25;}
        int length = (int) (Math.random()*4)+2;
        event e = new event(multiplier,eventTypes[dmg],turn,turn+length-1);
        lst.add(e);
        curevent.add(e);
    }

    public static void weatherGen() {
        int[] intense = {75,300,75,50,25,100};
        int[] altint = {50,150};
        String[] type = {"Water Damage","Thunder Damage","Air Damage","Earth Damage","Earth Damage","Fire Damage"};
        String[] alttype = {"Air Damage","Water Damage"};
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
        String[] type = {"Air Damage","Earth Damage","Fire Damage","Water Damage"};
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
        temp = new event(temperature, "All Damage", turn, turn);
        lst.add(temp);
    }
    public static void disasterGen() {
        event v = new event();
        disaster = v;
        disaster.setBegin(turn);
        disaster.setEnd(turn);
        boolean isdisaster = false;
        if (weather.getIntensity()==50 && weather.getType().equals("Air Damage")) {
            int n = (int) (Math.random()*100)+1;
            if (n<=20) {
                disaster.setType("Air Damage");
                disaster.setIntensity(250);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (turn%36<=18 && turn%36>9) {
            int n = (int) (Math.random()*100)+1;
            if (n<=5) {
                disaster.setType("Earth Damage");
                disaster.setIntensity(250);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (weather.getIntensity()==50 && weather.getType().equals("Water Damage")) {
            int n = (int) (Math.random()*100)+1;
            if (n<=10) {
                disaster.setType("Water Damage");
                disaster.setIntensity(250);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (turn%36<=27 && turn%36>18) {
            int n = (int) (Math.random()*100)+1;
            if (n<=5) {
                disaster.setType("Fire Damage");
                disaster.setIntensity(250);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (weather.getType().equals("Thunder Damage")) {
            int n = (int) (Math.random()*100)+1;
            if (n<=25) {
                disaster.setType("Thunder Damage");
                disaster.setIntensity(350);
                lst.add(disaster);
                isdisaster = true;
            }
        }
        if (isdisaster) {
            for (String s:eventTypes) {
                if (s.endsWith(" Damage") && !s.equals(disaster.getType()) && 
                !s.equals("Melee Damage") && !s.equals("Spell Damage") && !s.equals("Ranged Damage")
                && !s.equals("Weapon Damage") && !s.equals("All Damage")) {
                    event e = new event(-1000,s,turn,turn);
                    lst.add(e);
                }
            }
        }
        else {
            disaster = null;
        }
    }

    public static void tideGen() {
        event e = new event();
        int rand = (int) (Math.random()*3)+1;
        if (rand==1) {
            String[] types = {"Strength","Dexterity","Intelligence","Defence","Agility"};
            e.setType(types[(int) (Math.random()*5)]);
            e.setIntensity(5);
            e.setBegin(turn);
            e.setEnd(turn+1);
            lst.add(e);
            tide = e;
        }
        else {
            tide = null;
        }
    }

    public static void levelUp(int playerIndex) throws IOException {
        while (arr[playerIndex].getLXP()>=arr[playerIndex].getNL()) {
            out.println(arr[playerIndex].getName()+" has levelled up!");
            arr[playerIndex].addLvl(1);
            arr[playerIndex].setLXP(r2(arr[playerIndex].getLXP()-arr[playerIndex].getNL()));
            arr[playerIndex].addNL(100*(arr[playerIndex].getLvl()/5+1));
            out.println("Select a level up choice:");
            out.println("1: gain elemental dust");
            out.println("2: to gain AP");
            out.println("3: to increase stamina regen");
            out.println("4: to obtain reroll amplifier");
            int ans = -1;
            while (ans<1 || ans>4) {
                ans = Integer.parseInt(br.readLine());
                if (ans==1) {
                    out.println("Gained "+arr[playerIndex].getLvl()+" elemental dust!");
                    String[] dustTypes = {"Earth Dust I","Thunder Dust I","Water Dust I","Fire Dust I","Air Dust I"};
                    for (int j=0; j<arr[playerIndex].getLvl(); j++) {
                        int elementType = (int) (Math.random()*5);
                        arr[playerIndex].addInventoryValue(dustTypes[elementType], 1);
                    }
                }
                else if (ans==2) {
                    if (arr[playerIndex].getModifier().equals("Good Luck")) {
                        out.println("Your modifier does not let you select this option!");
                    }
                    else {
                        arr[playerIndex].addAP(arr[playerIndex].getLvl());
                        out.println("+"+arr[playerIndex].getLvl()+" AP");
                    }
                }
                else if (ans==3) {
                    arr[playerIndex].addStaminaRegen(1);
                    out.println("+1 Stamina Regen");
                }
                else if (ans==4) {
                    arr[playerIndex].addInventoryValue("Reroll Amplifier I", 1);
                }
                else {
                    out.println("That's not a valid choice!");
                }
            }
            arr[playerIndex].addMaxMana(5);
            arr[playerIndex].addStamina(1);
        }
    }

    public static void isDead(int i) {
        if (arr[i].getHP()<=0) {
            out.println(arr[i].getName()+" has lost a life!");
            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[i].getName()+" has lost a life");
            arr[i].setHP(arr[i].getMaxHP()+0.0);
            arr[i].addLives(-1);
            if (arr[i].getLives()==0) {
                out.println(arr[i].getName()+" has been sent to the gulag");
                addToEventLog("Turn "+turn+"-"+subturn+": "+arr[i].getName()+" has been sent to the gulag");
                arr[i] = new playerData(arr[i].getName());
                arr[i].setAlive(false);
                for (Map.Entry<String,Double> map:arr[i].getLockoutProgress().entrySet()) {
                    map.setValue(0.0);
                }
                if (!arr[i].isAlive()) playersAlive--;
                if (playersAlive==1) {
                    out.println("THE GAME HAS ENDED!");
                    for (int k=0; k<playerCount; k++) {
                        if (arr[k].isAlive()) {
                            out.println("THE WINNER IS: "+arr[k].getName());
                            addToEventLog("Turn "+turn+"-"+subturn+": "+arr[k].getName()+" HAS WON THE GAME");
                            break;
                        }
                    }
                    endGame();
                }
            }
        }
    }

    public static void lockoutGen() {
        for (int i=0; i<playerCount; i++) {
            for (int j=0; j<lockoutTypes.length; j++) {
                arr[i].setLockoutProgressValue(lockoutTypes[j], 0.0);
            }
            for (Map.Entry<String,Double> map:arr[i].getLockoutProgress().entrySet()) {
                map.setValue(0.0);
            }
        }
        int specificdmg = 50;
        int origmana = 20;
        int originalreward = 1;
        int totaldmg = 100;
        for (int i=0; i<12; i++) {
            int choice = (int) (Math.random()*lockoutTypes.length);
            switch (lockoutTypes[choice]) {
                case "Mana": goals[i] = new lockoutGoal(lockoutTypes[choice],origmana,originalreward); break;
                case "Spell Damage": case "Melee Damage": case "Weapon Damage": goals[i] = new lockoutGoal(lockoutTypes[choice],totaldmg,originalreward); break;
                default: goals[i] = new lockoutGoal(lockoutTypes[choice],specificdmg,originalreward); break;
            }
            if (i%2==1) {
                specificdmg*=2;
                origmana*=2;
                originalreward*=2;
                totaldmg*=2;
            }
        }
    }
    public static void completeLockout(int playerIndex) {
        for (int j=0; j<goals.length; j++) {
            if (arr[playerIndex].getLockoutProgressValue(goals[j].getType())>=goals[j].getValue() && !goals[j].getDone()) {
                System.out.println(arr[playerIndex].getName()+" completed goal "+(j+1));
                addToEventLog("Turn "+turn+"-"+subturn+": "+arr[playerIndex].getName()+" completed lockout goal "+(j+1));
                arr[playerIndex].addAP(goals[j].getAPR());
                goals[j].setDone(true);
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
        out.println("8: Set Modifier");
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
            endGame();
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
        else if (x==8) {
            out.print("Player name: ");
            int i = findPlayer(br.readLine());
            int[] startingAP = {15,15,20,15,10,20,50,60,20,30,15,20,20,150,35,40};
            String[] modifierNames = {"Reflexless","Easy Target","Magic Doubter","Colin Luck","Out of Shape",
            "Slow Learner","Glass Cannon","UHC","Decaying Heart","Amateur Wizard","\"The Normal One\"","Bullseyen't",
            "Dumb and Weak","Brittle Bones","Good Luck","April Sun","Confusion"};
            out.println("Available Modifiers: ");
            out.println("1: Reflexless (+15 AP, take DOUBLE damage from MELEE WEAPONS)");
            out.println("2: Easy Target (+15 AP, take DOUBLE damage from RANGED WEAPONS)");
            out.println("3: Magic Doubter (+20 AP, take DOUBLE damage from SPELLS)");
            out.println("4: Colin Luck (+15 AP, ALL rolls are ONLY common or unique)");
            out.println("5: Out of Shape (+10 AP, crossing bridges takes 2x stamina)");
            out.println("6: Slow Learner (+20 AP, gain HALF the XP as you usually would)");
            out.println("7: Glass Cannon (+50 AP, take DOUBLE damage from ALL SOURCES)");
            out.println("8: UHC (+60 AP, start the game with ONLY 1 LIFE, NO HEALTH REGEN)");
            out.println("9: Decaying Heart (+20 AP, your health decreases by 1% after each turn)");
            out.println("10: Amateur Wizard (+30 AP, spells cost DOUBLE mana)");
            out.println("11: \"The Normal One\" (+15 AP, gain no elemental damage buffs of ANY SOURCE)");
            out.println("12: Bullseyen't (+30 AP, Spells and Ranged Weapons have a 25% chance to MISS)");
            out.println("13: Dumb and Weak (+20 AP, NO EFFECT from Strength and Intelligence)");
            out.println("14: Brittle Bones (+20 AP, Any player that does a Critical Hit on you will deal 5x damage, instead of the normal 2x)");
            out.println("15: Good Luck (+150 AP, The AP you get is all you will get)");
            out.println("16: April Sun (+35 AP, deal 0.5x all damage)");
            out.println("17: Confusion (+40 AP, you have a 10% chance to hit yourself in confusion)");
            int modifierChoice = Integer.parseInt(br.readLine());
            if (modifierChoice>=1 && modifierChoice<=modifierNames.length) {
                arr[i].addAP(startingAP[modifierChoice-1]);
                arr[i].setModifier(modifierNames[modifierChoice-1]);
            }
            if (modifierChoice==8) {
                double totalHealth = 0;
                if (arr[i].getLives()==2) {
                    totalHealth = arr[i].getMaxHP()+arr[i].getHP();
                }
                else totalHealth = arr[i].getHP();
                arr[i].setLives(1);
                arr[i].setHPRegen(0);
                arr[i].setHP(r2(totalHealth/2));
            }
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
        String s = n+"";
        if (s.contains(".")) {
            int index = s.indexOf(".");
            if (s.substring(index+1).length()<=2) {
                return Double.parseDouble(s);
            }
            else {
                s = s.substring(0, index+3);
                return Double.parseDouble(s);
            }
        }
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
    public static void addToEventLog(String s) {
        eventLog.add(s);
        if (eventLog.size()>50) eventLog.remove(0);
    }
    public static void endGame() {
        eventLog.clear();
        playersAlive = playerCount;
        for (int k=0; k<playerCount; k++) {
            arr[k] = new playerData(arr[k].getName());
            for (int i=0; i<lockoutTypes.length; i++) {
                arr[k].setLockoutProgressValue(lockoutTypes[i], 0);
            }
            arr[k].setModifier("None");
        }
        lockoutGen();
        potionEffects.clear();
        lockoutReset = turn+10;
    }
    public static double effectiveness(int n) {
        if (n>200) return 200;
        return -(n-200)*(n-200)/400.0+100.0;
    }
}
