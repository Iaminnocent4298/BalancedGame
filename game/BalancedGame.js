/*
"Balanced Game" - made by Iaminnocent4298, web server made by Creative0708
Version 2.9 - En Garde!
*/
import * as armour from'./armour.js';
import * as bridge from'./bridge.js';
import * as event from'./event.js';
import * as island from'./island.js';
import * as lockoutGoal from'./lockoutGoal.js';
import * as peffect from'./peffect.js';
import * as playerData from'./playerData.js';
import * as potion from'./potion.js';
import * as spell from'./spell.js';
import * as weapon from'./weapon.js';

let turn;
let subturn;
let playerCount;
let islandLim = 26;
let islandsBuilt;
let lockoutReset;
let arr;
let goals;
let lst;
let curevent;
let weather;
let season;
let temperature;
let temp;
let tide;
let disaster;
let islandResources;
let bridges;
let eventLog;
let playersAlive;
let potionnum = 35; //lvls 0-12
let potionShop;
let potionEffects;
let arrowTypes = ["Normal Arrow","Earth-Infused Arrow","Thunder-Infused Arrow","Water-Infused Arrow","Fire-Infused Arrow",
"Air-Infused Arrow","Rainbow Arrow"];
let eventTypes = ["Spell Damage","Neutral Damage","Melee Damage","Ranged Damage","Weapon Damage","Health Regen","Mana Regen","Spell Cost",
"All Damage","Earth Damage","Thunder Damage","Water Damage","Fire Damage","Air Damage","Strength","Dexterity","Intelligence","Defence","Agility"];
let lockoutTypes = ["Neutral Damage","Earth Damage","Thunder Damage","Water Damage","Fire Damage","Air Damage","Heal","Mana","Spell Damage","Melee Damage","Ranged Damage"];
let weapondps = [20,25,35,50,70,95]; //base
let spelldps = [30,35,45,60,80,105]; //base spell 1
/**
 * The total "score" of each rarity of armour
 */
let armourWeight = [5,10,15,20,25,35];
let path;

let storedValue;

let ans = 0;

function addText(message) {
    const app = document.getElementById("app");
    const textElement = document.createElement("p");
    textElement.textContent = message;
    app.appendChild(textElement);
}



function addTextBox(placeholder) {
    const app = document.getElementById("app");
    const textbox = document.createElement("input");
    textbox.type = "text";
    textbox.placeholder = placeholder;
    app.appendChild(textbox);
}

function clearAll() {
    const app = document.getElementById("app");
    app.innerHTML = ""; // Clears all elements inside #app
}

function addButton(text, value) {
    const app = document.getElementById("app");
    const button = document.createElement("button");
    button.textContent = text;
    button.onclick = function() {
        storedValue = value;
    };
    app.appendChild(button);
}

addButton("Example Button 1", 1);
addButton("Example Button 2", 2);
addButton("Example Button 3", 3);

waitForButtonClick().then(() => {
    console.log("Button clicked, storedValue:", storedValue);
    // ...continue with the rest of the program logic...
});

function waitForButtonClick() {
    return new Promise((resolve) => {
        const checkValue = setInterval(() => {
            if (storedValue !== undefined) {
                clearInterval(checkValue);
                resolve();
            }
        }, 100);
    });
}
