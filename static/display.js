import * as islands from "./islands.js";
import * as rng from "./rng.js";
import * as potions from "./potions.js";

const QUOTES = [
  "99% of gamblers quit before they win big!",
  "Rolling a mythic is only 1 roll away! (or 99)",
  "Setting a player ablaze does not deal damage",
  "Neutral damage? Where's positive and negative damage?",
  "Rerolling a weapon does NOT cost 0 AP",
  "“WOOF???????” - Brian",
  "I am Brian's dog - *lava bucket*",
  "Wolf tried to swim in lava",
  "Misinput misinput IT WAS A MISINPUT CALM DOWN",
  "“To defeat your enemy, you must first create the universe”",
  "“NO I DIDN'T WANT TO DONATE ALL MY AP TO BRIAN”",
  "Nginx is actually pronounced en-ginks",
  "“Processing is Javascript” - Ms. Krasteva 2024",
  "Made with Ready to Program!",
  "How many updates can i release in 24 hours challenge",
  "v$version - Removed Justin",
  "How are there more mythic rolls than legendaries",
  "“Reset my luck” “Common Common Common Common Common”",
  "“New Event! -100% ALL DAMAGE” L bozo",
  "“WHY IS IT THROWING AN ERROR- oh.”",
  "No, the logo is not the spikes from Boxel Rebound",
  'Ah yes, I love the weapon name "0 1"',
  "When you reroll a common and it was worse than the previous common",
  '"Lemme poke Brian" -> java.lang.NullPointerException',
  '"It\'s winter!" "Great what\'s the temperature" "20 degrees-"',
  '"Why do i keep rolling commons" Also has a Fabled',
  "Game balancing? What's that?",
  "The goal of the game is to make your opponent's health 0 while keeping your own health above 0",
  "Bing Bong noises",
  "Unga Bunga",
  '"object Object',
  '"No o no No No"',
  "ap bankN!KNKNK!NK!N",
  "1900 DAMAGE WHAT",
  '"This is fine actually" meanwhile Brian with a literal 400 dps weapon',
  "When the disaster stops you from completing a lockout challenge",
  '"You should be able to complete the exam in 30-45 mins" - Ms. Krasteva',
  "Blåhaj",
  "*Matches 0 numbers* YOU HAVE WON THE JACKPOT!",
  "RTP CUP!",
  "Colin really has the worst luck in the world...",
  '"I have a genius strat" *Does 0 damage*',
  "How has a single group roll 2 MYTHICS AND 1 FABLED IN THE SAME GAME",
  "Ruh Ruh Raggy - unknown",
  "😰 - Fei",
  "🤩 - April",
  "bonk bonk banana sun",
  "VEHICULAR MANSLAUGHTER!!",
  "bob the builder",
  "all hail google rng",
  '"I\'M GETTING STUN-LOCKED" - Brian',
  "Kenneth after dying twice in the first 6 turns of the game",
  '"Nuh uh" - Kenneth',
  "Petition to ban April from rolling anything other than a common",
  "Imagine being the 13th player",
  "The damage calculation is ALWAYS incorrect!",
  "With great features come great bugs",
];

function prettify(str) {
  if (!str) return str;

  const words = str.toLowerCase().split(/[ _]+/);
  return words.map((x) => x && x[0].toUpperCase() + x.substring(1)).join(" ");
}

function prettyJoin(arr) {
  if (arr.length == 0) return "";
  if (arr.length == 1) return arr[0];
  if (arr.length == 2) return arr[0] + " and " + arr[1];

  let last = arr.pop();
  return arr.join(", ") + ", and " + last;
}

export async function getGameData() {
  const res = await fetch(window.BASE_URL + "/api/game_data");
  return await res.json();
}

async function loadForumPosts() {
  const res = await fetch(window.BASE_URL + "/api/changelog");
  const html = await res.text();
  document.getElementById("changelog").innerHTML = html;
}

function getSeason(data) {
  if (!data) return data;

  const { intensity, type } = data.season;

  const time = {
    50: "Early",
    75: "Mid",
    25: "Late",
  }[intensity];

  const [symbol, season] = {
    Air_Damage: ["❄", "Winter"],
    Earth_Damage: ["⚘", "Spring"],
    Fire_Damage: ["☀", "Summer"],
    Water_Damage: ["🙒", "Fall"],
  }[type];

  return `${symbol} ${time} ${season}`;
}

function getWeather(data) {
  if (!data) return data;

  const { intensity, type } = data.weather;

  return {
    "75 Water_Damage": "🌧 Rain",
    "50 Air_Damage": "🌨 Snow",
    "300 Thunder_Damage": "🌩 Thunderstorm",
    "150 Water_Damage": "🌧 Sleet",
    "75 Air_Damage": "🙛 Windy",
    "50 Earth_Damage": "☁ Cloudy",
    "25 Earth_Damage": "🌤 Partly Cloudy",
    "100 Fire_Damage": "☀ Sunny",
  }[intensity + " " + type];
}

function temperatureIndicator(temp) {
  if (temp >= 31) return "fire";
  else if (temp >= 21) return "neutral";
  else if (temp >= 11) return "thunder";
  else if (temp >= 1) return "earth";
  else if (temp >= -9) return "mana";
  else if (temp >= -19) return "water";
  else return "lvl";
}

function toRomanNumerals(num) {
  if (isNaN(+num)) return NaN;
  var digits = [...String(num)],
    key = [
      "",
      "C",
      "CC",
      "CCC",
      "CD",
      "D",
      "DC",
      "DCC",
      "DCCC",
      "CM",
      "",
      "X",
      "XX",
      "XXX",
      "XL",
      "L",
      "LX",
      "LXX",
      "LXXX",
      "XC",
      "",
      "I",
      "II",
      "III",
      "IV",
      "V",
      "VI",
      "VII",
      "VIII",
      "IX",
    ],
    roman = "",
    i = 3;
  while (i--) roman = (key[+digits.pop() + i * 10] || "") + roman;
  return Array(+digits.join("") + 1).join("M") + roman;
}

const topContainer = document.getElementById("top-container");
const tableContainer = document.getElementById("table-container");
const versionEl = document.getElementById("version");
const splashTextEl = document.getElementById("splash-text");
const eventLogEl = document.getElementById("event-log");

const tooltipEl = document.getElementById("tooltip");

let isFirstLoad = true;

function setQuote() {
  splashTextEl.textContent = QUOTES[
    Math.floor(Math.random() * QUOTES.length)
  ].replace("$version", version);
}
window.rerollQuote = setQuote;

function handleSizeChange() {
  islands.resizeCanvasTo(document.getElementById("island-canvas-container"));
}

function escapeHTML(text) {
  return text
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function* enumerate(iter) {
  let i = 0;
  for (const val of iter) {
    yield [i++, val];
  }
}

function init(data) {
  window.version = data.version;
  document.title = document.title.trim() + " v" + data.version;
  versionEl.className = "primary";
  versionEl.textContent = data.version;

  setQuote();
  splashTextEl.addEventListener("click", setQuote);

  function updateTooltip(e) {
    const target = e.target.closest("[tooltip]");
    if (!target) {
      tooltipEl.hidden = true;
      return;
    }
    tooltipEl.innerHTML = target.getAttribute("tooltip");
    tooltipEl.style.left = e.clientX + "px";
    tooltipEl.style.top = e.clientY + "px";
    tooltipEl.hidden = false;
  }
  document.body.addEventListener("mousemove", updateTooltip);
  window.addEventListener("resize", handleSizeChange);
  requestAnimationFrame(islands.animationFrame);

  islands.init();
  rng.init();
  potions.init(() => updateGameDisplay(undefined, true));

  window.onselectstart = () => false;
}

let game_datas;
export function updateGameDisplay(received, overwriteAll) {
  if (overwriteAll) {
    if (received) game_datas = received;
  } else {
    game_datas[received.game_id] = received.data;
  }

  const data = (window.gameData = game_datas[window.GAME_ID]);

  console.log("updating with", data);

  if (isFirstLoad) {
    isFirstLoad = false;
    init(data);
  }

  const { arr: players } = data;

  eventLogEl.innerHTML =
    data.eventLog.length == 0
      ? '<span class="inactive">Nothing here yet!</span>'
      : [...data.eventLog]
          .reverse()
          .map((text) => {
            text = escapeHTML(text);
            for (const [regex, className] of [
              [/turn \d+(-\d+)?/gi, "primary"],
              [/spell \d+/gi, "primary"],
              [/[0-9.]+ damage/gi, "neutral"],
              [/\d+ (ap|ability points?)/gi, "ap"],
              [
                RegExp(
                  `\\b(${players.map((player) => player.name.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")).join("|")})\\b`,
                  "g",
                ),
                "secondary",
              ],
            ]) {
              text = text.replaceAll(
                regex,
                (match) => `<span class="${className}">${match}</span>`,
              );
            }
            return text;
          })
          .join("\n");

  const numPlayers = data.arr.length;

  function item(dat, options) {
    let el;
    const defaultTag = (options ?? {}).defaultTag ?? "div";

    if (dat == null) {
      el = document.createElement(defaultTag);
    } else if (dat.tagName?.toLowerCase() == defaultTag) {
      el = dat;
    } else if (dat instanceof HTMLElement) {
      el = document.createElement(defaultTag);
      el.appendChild(dat);
    } else if (Array.isArray(dat)) {
      el = document.createElement(defaultTag);
      for (const x of dat)
        if (x)
          el.appendChild(
            item(x, {
              defaultTag: (options ?? {}).deepDefaultTag,
              deepDefaultTag: (options ?? {}).deeperDefaultTag,
            }),
          );
    } else {
      el = document.createElement(defaultTag);
      el.textContent = dat;
    }
    if (options) {
      const {
        defaultTag: _,
        force: _2,
        deepDefaultTag: _3,
        deeperDefaultTag: _4,
        tooltip,
        className,
        ...rest
      } = options;
      Object.assign(el, rest);
      if (className) el.className = className;
      if (tooltip) el.setAttribute("tooltip", tooltip);
    }
    return el;
  }

  function list(arr) {
    let el = document.createElement("ul");

    for (const x of arr)
      if (x)
        el.appendChild(item(x, { defaultTag: "li", deepDefaultTag: "span" }));

    return el;
  }

  function element(tagName, text, options) {
    const el = document.createElement(tagName);
    if (text) el.textContent = text;
    if (options) Object.assign(el, options);
    return el;
  }
  function htmlElement(tagName, html, options) {
    const el = document.createElement(tagName);
    if (html) {
      el.innerHTML = html;
    }
    if (options) Object.assign(el, options);
    return el;
  }
  function classDiv(dat, className) {
    return item(dat, { className });
  }
  function classSpan(dat, className) {
    return item(dat, { defaultTag: "span", deepDefaultTag: "span", className });
  }
  function classDivInline(dat, className) {
    return item(dat, { deepDefaultTag: "span", className });
  }
  function classTable(dat, className) {
    return item(dat, {
      defaultTag: "table",
      deepDefaultTag: "tr",
      deeperDefaultTag: "td",
      className,
    });
  }
  function newline() {
    const el = document.createElement("div");
    el.className = "newline";
    return el;
  }

  function infobox(boxId, header, child) {
    let boxEl = document.getElementById("infobox-" + boxId);
    if (!boxEl) {
      boxEl = document.createElement("div");
      boxEl.id = "infobox-" + boxId;
      boxEl.className = "infobox";
      topContainer.appendChild(boxEl);
    } else {
      boxEl.replaceChildren();
    }
    const headerEl = item(header, { defaultTag: "h3" });
    if (Array.isArray(child) && Array.isArray(child[0])) {
      const childEl = document.createElement("div");
      boxEl.classList.add("infobox-flex");
      let isFirst = true;
      for (const x of child) {
        const el = item(x);
        if (isFirst) {
          el.insertBefore(headerEl, el.firstChild);
          isFirst = false;
        }
        boxEl.appendChild(el);
      }

      boxEl.appendChild(childEl);
    } else {
      boxEl.appendChild(headerEl);
      boxEl.appendChild(item(child));
    }
  }

  // The "Nick Map": a hashmap represented as a list of entries
  // i.e.
  // [
  //     {"type": "Neutral Damage", "value": 0},
  //     {"type": "Earth Damage", "value": 0},
  //     {"type": "Thunder Damage", "value": 0},
  //     {"type": "Water Damage", "value": 0},
  //     {"type": "Fire Damage", "value": 0},
  //     {"type": "Air Damage", "value": 0},
  //     {"type": "Heal", "value": 10},
  //     {"type": "Mana", "value": 0},
  //     {"type": "Spell Damage", "value": 0},
  //     {"type": "Melee Damage", "value": 0},
  //     {"type": "Ranged Damage", "value ":0}
  // ]
  // instead of using a _regular_ hashmap because "hashmap syntax is so annoying". >:(
  function nickMapGet(nickMap, key) {
    return nickMap.find((el) => el.type === key).value;
  }

  function formatBuff(buff, hideTime) {
    const { intensity, type, begin, end } = buff;
    return `${!hideTime && end !== undefined ? `[${begin}-${end}] ` : ""}${intensity >= 0 ? "+" : ""}${intensity}% ${prettify(type)}`;
  }

  function type(str) {
    if (!str) return str;
    return str.split("_")[0].toLowerCase();
  }

  let season = getSeason(data);
  let weather = getWeather(data);

  let yearEl = document.createElement("div");
  yearEl.className = "secondary";
  yearEl.append(
    "Year: ",
    classSpan(Math.floor((data.turn - 1) / 36) + 1, "primary"),
    ", Season: ",
    classSpan(season, type(data.season.type)),
  );

  let weatherEl = document.createElement("div");
  weatherEl.className = "secondary";

  const temperature = data.temperature;
  weatherEl.append(
    "Current Weather: ",
    classSpan(weather, type(data.weather.type)),
    ", ",
    classSpan(temperature + "°C", temperatureIndicator(temperature)),
  );

  infobox("gameinfo", "Game Info", [
    [
      classDiv(
        `Turn: ${data.turn}-${data.subturn} (${players[data.subturn - 1]?.name}'s turn)`,
        "primary",
      ),
      yearEl,
      weatherEl,
      classDiv("Next Event: Turn " + data.nextEvent, "lvl"),
      element("h4", "Disaster Info"),
      ...(data.disaster
        ? [
            classDivInline([
              "Current disaster: ",
              classSpan(
                {
                  Air_Damage: "🏔 Blizzard",
                  Earth_Damage: "🌎 Earthquake",
                  Water_Damage: "🌊Tsunami",
                  Fire_Damage: "🌋 Volcano Eruption",
                  Thunder_Damage: "🗱 Lightning Storm",
                }[data.disaster.type],
                type(data.disaster.type),
              ),
            ]),
            classDivInline([
              "All non-",
              classSpan(prettify(data.disaster.type), type(data.disaster.type)),
              " does nothing this turn!",
            ]),
            classDivInline([
              "All ",
              classSpan(prettify(data.disaster.type), type(data.disaster.type)),
              " is increased by ",
              classSpan(
                data.disaster.intensity + "%",
                type(data.disaster.type),
              ),
            ]),
          ]
        : [
            classDivInline([
              "Current disaster: ",
              classSpan("None", "inactive"),
            ]),
          ]),
    ],
    [
      classDiv("Season/Weather Buffs:", "secondary"),
      list([
        classSpan(
          season + ": " + formatBuff(data.season, true),
          type(data.season.type),
        ),
        classSpan(
          weather + ": " + formatBuff(data.weather, true),
          type(data.weather.type),
        ),
        classSpan(
          "Temperature: " +
            formatBuff({ intensity: data.temperature, type: "All Damage" }),
          temperatureIndicator(data.temperature),
        ),
      ]),
      classDiv("Current Events:", "secondary"),
      list(
        data.curevent.map((buff) =>
          classSpan(
            formatBuff(buff),
            (buff.intensity < 0) ^ (buff.type == "Spell_Cost") ? "bad" : "ap",
          ),
        ),
      ),
    ],
  ]);

  function lockout(lockoutData, i) {
    const msg = {
      "Neutral Damage": "Deal %d Neutral Damage",
      "Earth Damage": "Deal %d Earth Damage",
      "Thunder Damage": "Deal %d Thunder Damage",
      "Water Damage": "Deal %d Water Damage",
      "Fire Damage": "Deal %d Fire Damage",
      "Air Damage": "Deal %d Air Damage",
      "Spell Damage": "Deal %d Spell Damage",
      "Melee Damage": "Deal %d Melee Damage",
      "Ranged Damage": "Deal %d Ranged Damage",
      Mana: "Use %d Mana",
      Heal: "Heal %d Health",
    }[lockoutData.type].replace("%d", lockoutData.value);

    const checkboxEl = document.createElement("input");
    checkboxEl.type = "checkbox";
    checkboxEl.disabled = true;
    checkboxEl.checked = lockoutData.done;

    const el = document.createElement("span");
    el.appendChild(checkboxEl);
    el.appendChild(
      classSpan(
        `${msg} - ${lockoutData.apreward} AP`,
        ["common", "unique", "rare", "legendary", "fabled", "mythic"][i],
      ),
    );

    if (lockoutData.done) {
      el.className = "strikethrough";
    }

    return el;
  }

  infobox("lockout", "Lockout Challenges", [
    classSpan([
      "Next Reset: Turn ",
      classSpan(data.lockoutReset, "primary")
    ]),
    newline(),
    newline(),
    list(data.goals.map((x, i) => lockout(x, i))),
  ]);

  function tableGroup(groupId) {
    let groupEl = document.getElementById("tablegroup-" + groupId);
    if (!groupEl) {
      groupEl = document.createElement("div");
      groupEl.id = "tablegroup-" + groupId;
      groupEl.className = "tablegroup";
      tableContainer.appendChild(groupEl);
    } else {
      groupEl.replaceChildren();
    }
    return groupEl;
  }

  function table(tableId, parent) {
    let tableEl = document.getElementById("table-" + tableId);
    if (!tableEl) {
      tableEl = document.createElement("table");
      tableEl.id = "table-" + tableId;
      (parent ?? tableContainer).appendChild(tableEl);
    } else {
      tableEl.replaceChildren();
    }

    function row(header, callback, className, style, tooltip) {
      const tr = document.createElement("tr");
      if (className) tr.className = className;
      if (style) Object.assign(tr.style, style);

      function rowItem(i, dat, options) {
        const el = item(dat, { defaultTag: "td", options });
        if (i === data.subturn - 1) el.classList.add("highlighted");
        else if (players[i]?.gameLevel) el.classList.add("darkened");
        tr.appendChild(el);
        return el;
      }
      function headerItem(dat, options) {
        const el = rowItem(null, dat, options);
        if (tooltip) {
          el.setAttribute("tooltip", tooltip);
        }
      }

      if (callback == null) {
        headerItem(header, { colSpan: numPlayers + 1, force: true });
      } else {
        headerItem(header, { force: true });
        for (let i = 0; i < numPlayers; i++)
          rowItem(i, callback(players[i], i));
      }

      tableEl.appendChild(tr);
    }

    return row;
  }
  function itemTable(tableId, dat, parent) {
    let tableEl = document.getElementById("itemtable-" + tableId);
    if (!tableEl) {
      tableEl = classTable(dat);
      tableEl.id = "itemtable-" + tableId;
      (parent ?? tableContainer).appendChild(tableEl);
    } else {
      tableEl.replaceChildren();
    }
    tableEl.append(...classTable(dat).children);
  }
  function tableContainerDiv(divId, dat, parent) {
    let tableEl = document.getElementById("tablediv-" + divId);
    if (!tableEl) {
      tableEl = document.createElement("div");
      tableEl.id = "tablediv-" + divId;
      tableEl.className = "tablediv";
      (parent ?? tableContainer).appendChild(tableEl);
    } else {
      tableEl.replaceChildren();
    }
    tableEl.appendChild(item(dat));
  }

  function playerHeader(player) {
    const td = document.createElement("td");
    if (player.name === "Victor") {
      td.style.backgroundColor = "black";
      td.setAttribute(
        "tooltip",
        "This player has been censored under the groundhog act of 2024",
      );
    } else {
      const arr = [
        player.name + " (",
        element("span", "❤".repeat(player.lives), { className: "hp" }),
        ")",
      ];
      if (player.gameLevel) arr.push(classDiv("In Gulag", "bad"));
      td.append(...arr);
    }
    return td;
  }

  function tdTooltip(content, tooltip) {
    return item(content, { tooltip, defaultTag: "td" });
  }

  let row = table("stats");

  row(element("h3", "Player Stats"), playerHeader);

  row(
    "✷ Level",
    (player) =>
      `${player.lvl} ➩ ${player.lvl + 1}\n[${player.lvlxp}/${player.nextlvl}]`,
    "lvl",
    null,
    `
        <h3>Level</h3>
        <p>Level up to gain ability points or increase weapon/spell tiers</p>
        <p>Deal damage to level up!</p>
    `,
  );
  row(
    "✷ Ability Points",
    (player) => player.abilityPoints,
    "ap",
    null,
    `
        <h3>Ability Points</h3>
        <p>The main form of "currency" in Balanced Game</p>
        <p>You gain ability points every turn</p>
    `,
  );
  row(
    "❤ Health",
    (player, i) =>
      tdTooltip(
        `${player.hp}/${player.maxhp}`,
        `${nickMapGet(player.status, "Heal")} health healed this lockout cycle`,
      ),
    "hp",
    null,
    `
        <h3>Health</h3>
        <p>Health is your lifeline</p>
        <p>Make your opponent's health 0 while keeping your own health above 0</p>
    `,
  );
  row(
    "❣ Health Regen",
    (player) => player.hpregen,
    "hp",
    null,
    `
        <h3>Health Regen</h3>
        <p>Health Regen heals you!</p>
        <p>You gain health at the end of every turn</p>
    `,
  );
  row(
    "✷ Mana",
    (player, i) =>
      tdTooltip(
        `${player.mana}/${player.maxmana}`,
        `${nickMapGet(player.status, "Mana")} mana used this lockout cycle`,
      ),
    "mana",
    null,
    `
        <h3>Mana</h3>
        <p>Mana is used to cast spells</p>
        <p>You can also increase your mana cap!</p>
    `,
  );
  row(
    "⸎ Mana Regen",
    (player) => player.manaregen,
    "manarg",
    null,
    `
        <h3>Mana Regen</h3>
        <p>Mana Regen refills your mana storage!</p>
        <p>You gain mana at the end of every turn</p>
    `,
  );

  row(
    "🗲 Stamina",
    (player, i) => {
      let className;
      let staminaRatio = (player.stamina / player.maxstamina) * 100;
      if (staminaRatio >= 75) {
        className = null;
      } else if (staminaRatio >= 50) {
        className = "stamina-1";
      } else if (staminaRatio >= 25) {
        className = "stamina-2";
      } else {
        className = "stamina-3";
      }
      return classSpan(`${player.stamina}/${player.maxstamina}`, className);
    },
    "stamina",
    null,
    `
        <h3>Stamina</h3>
        <p>Stamina is used to cross bridges</p>
        <p>You can increase your max stamina with AP!</p>
    `,
  );
  row(
    "◆ Stamina Regen",
    (player) => player.staminaregen,
    "stamina",
    null,
    `
        <h3>Stamina Regen</h3>
        <p>Stamina Regen replenishes your stamina after every turn!</p>
        <p>You get tired pretty quickly</p>
    `,
  );

  row(
    "✤ Neutral Damage",
    (player, i) =>
      tdTooltip(
        player.neutraldmg,
        `${nickMapGet(player.status, "Neutral Damage")} neutral damage dealt this lockout cycle`,
      ),
    "neutral",
    null,
    `
        <h3>Neutral Damage</h3>
        <p>Neutral Damage is the only non-elemental damage in the game</p>
        <p>Increase Neutral Damage by allocating ability points to it!</p>
    `,
  );

  for (const [index, id, el, symbol, tooltip] of [
    [
      0,
      "strength",
      "earth",
      "✤",
      `
            <h3>Strength/Earth Defence</h3>
            <p>Strength increases your Earth Damage, and melee damage</p>
            <p>Earth Defence decreases the amount of Earth damage dealt to you</p>
        `,
    ],
    [
      1,
      "dexterity",
      "thunder",
      "✦",
      `
            <h3>Dexterity/Thunder Defence</h3>
            <p>Dexterity increases your Thunder Damage, and provides you with a chance to crit (2x damage)!</p>
            <p>Thunder Defence decreases the amount of Thunder damage dealt to you</p>
        `,
    ],
    [
      2,
      "intelligence",
      "water",
      "❉",
      `
            <h3>Intelligence/Water Defence</h3>
            <p>Intelligence increases your Water Damage, and deals extra spell damage!</p>
            <p>Water Defence decreases the amount of Water damage dealt to you</p>
        `,
    ],
    [
      3,
      "defence",
      "fire",
      "✹",
      `
            <h3>Defence/Fire Defence</h3>
            <p>Defence increases your Fire Damage, and decreases the amount of all damage you take</p>
            <p>Fire Defence decreases the amount of Fire damage dealt to you</p>
        `,
    ],
    [
      4,
      "agility",
      "air",
      "❋",
      `
            <h3>Agility/Air Defence</h3>
            <p>Agility increases your Air Damage, and provides you with a chance to dodge the opponent's attack!</p>
            <p>Air Defence decreases the amount of Air damage dealt to you</p>
        `,
    ],
  ]) {
    row(
      symbol + " " + prettify(id),
      (player, i) =>
        tdTooltip(
          `${symbol} ${player.elements[0][index]}\n❈ ${player.elements[1][index]}`,
          `${nickMapGet(player.status, prettify(el) + " Damage")} ${el} damage dealt this lockout cycle`,
        ),
      el,
      null,
      tooltip,
    );
  }

  row(
    "Active Effects",
    (player, i) => {
      const arr = [];
      for (const effect of data.potionEffects) {
        if (effect.name !== player.name) continue;

        const [symbol, className] = {
          Heal: ["❣", "heal"],
          Mana: ["⸎", "manarg"],
          Strength: ["✤", "earth"],
          Dexterity: ["✦", "thunder"],
          Intelligence: ["❉", "water"],
          Defence: ["✹", "fire"],
          Agility: ["❋", "air"],
        }[effect.type];

        arr.push([
          classSpan(`+${symbol} ${effect.value} `, className),
          classSpan(`(${effect.turns} ⇆)`, "lvl"),
        ]);
      }
      if (arr.length)
        return item(arr, { defaultTag: "td", deeperDefaultTag: "span" });
      return element("td", "None", { className: "inactive" });
    },
    "misc",
  );

  tableContainerDiv("islands", [
    element("h3", "Islands"),
    item(
      [
        item([
          element("h4", "Island Info"),
          list([
            [
              classSpan("Number of islands: ", "secondary"),
              classSpan(data.islandCost, "primary"),
            ],
            [
              classSpan("Island limit: ", "secondary"),
              classSpan(data.islandLim - 1, "primary"),
            ],
            [
              classSpan("Island build cost: ", "secondary"),
              classSpan(data.islandCost, "primary"),
            ],
          ]),
        ]),
        item(element("canvas", null, { id: "island-canvas" }), {
          id: "island-canvas-container",
        }),
      ],
      { id: "island-map-container" },
    ),
  ]);

  row = table("spells");

  row(element("h3", "Spells"), playerHeader);

  function weapon(weapon) {
    if (!weapon)
      return element("td", "Not Unlocked", { className: "inactive" });

    const arr = [];
    arr.push(
      classDiv(
        weapon.name + " " + toRomanNumerals(weapon.tier),
        "underline " + weapon.rarity.toLowerCase(),
      ),
    );
    arr.push(newline());

    for (const [index, el, symbol] of [
      [0, "neutral", "✤"],
      [1, "earth", "✤"],
      [2, "thunder", "✦"],
      [3, "water", "❉"],
      [4, "fire", "✹"],
      [5, "air", "❋"],
      [6, "heal", "❣"],
    ]) {
      const min = weapon.damages[0][index],
        max = weapon.damages[1][index];
      if (max == 0) continue;

      arr.push(classDiv(`${symbol} ${min}-${max}`, el));
    }

    if (weapon.manacost !== undefined) {
      arr.push(newline());
      arr.push(classDiv("✷ " + weapon.manacost, "mana"));
    }
    arr.push(newline());
    arr.push(classDiv("↻ " + weapon.rerollcost, "tertiary"));
    arr.push(newline());
    arr.push(classDiv(prettify(weapon.rarity), weapon.rarity.toLowerCase()));

    return arr;
  }

  for (const [spellId, name, cost] of [
    [0, "Spell 1", 10],
    [1, "Spell 2", 20],
    [2, "Spell 3", 40],
    [3, "Spell 4", 80],
    [4, "AOE Spell", 15],
  ]) {
    row(
      [
        classDiv(name, "primary"),
        cost != null && classDiv(`Cost: ${cost} AP`, "secondary"),
      ],
      (_, i) => weapon(data.spells[i][spellId]),
    );
  }

  row = table("weapons");

  row(element("h3", "Weapons"), playerHeader);

  for (let weaponId = 0; weaponId < data.weapons[0].length; weaponId++) {
    row(
      [
        [classDiv("Melee Weapon", "primary"), classDiv("25 AP", "secondary")],
        [classDiv("Ranged Weapon", "primary"), classDiv("35 AP", "secondary")],
      ][weaponId],
      (_, i) => weapon(data.weapons[i][weaponId]),
    );
  }

  tableContainerDiv("useful-info", [
    element("h3", "Useful Info"),
    htmlElement(
      "div",
      `
            <div>
                <h4>Spell/Weapon Rarity Chances</h4>
                <ul>
                    <li class="common">Common: 35%</li>
                    <li class="unique">Unique: 30%</li>
                    <li class="rare">Rare: 20%</li>
                    <li class="legendary">Legendary: 10%</li>
                    <li class="fabled">Fabled: 4%</li>
                    <li class="mythic">Mythic: 1%</li>
                </ul>
            </div>
            <div>
                <h4>Spell/Weapon Component Chances</h4>
                <ul>
                    <li class="common">1 Component: 60%</li>
                    <li class="unique">2 Components: 30%</li>
                    <li class="rare">3 Components: 10%</li>
                </ul>
                <p>Mythic spells/weapons are exempt from this.</p>
                <p>Nick just does what he wants in that case</p>
            </div>
            <div>
                <h4>Weather Chances</h4>
                <ul>
                    <li><span class="water">🌧 Rain (Temp > 0)</span>/<span class="air">🌨 Snow (Temp ≤ 0)</span>: 25%</li>
                    <li><span class="thunder">🌩 Thunder (Temp > 5)</span>/<span class="water">🌧 Sleet (Temp ≤ 5)</span>: 10%</li>
                    <li><span class="air">🙛 Windy</span>: 15%</li>
                    <li><span class="earth">☁ Cloudy</span>: 15%</li>
                    <li><span class="earth">🌤 Partly Cloudy</span>: 15%</li>
                    <li><span class="fire">☀ Sunny</span>: 20%</li>
                </ul>
            </div>
        `,
      { className: "useful-info" },
    ),
  ]);

  const miscInfoGroup = tableGroup("misc");

  const potionData = Array(8)
    .fill(0)
    .map((_) => Array(6));

  potionData[0][0] = element("h3", "Potion Costs");

  for (const [i, buff] of enumerate([
    "Health Regen",
    "Mana Regen",
    "Strength",
    "Dexterity",
    "Intelligence",
    "Defence",
    "Agility",
  ])) {
    potionData[i + 1][0] = buff;
  }

  for (const [i, size] of enumerate([
    "Tiny",
    "Small",
    "Medium",
    "Large",
    "Extra Large",
  ])) {
    potionData[0][i + 1] = size;
  }

  for (const [i, [prettyBuff, buff, className, symbol]] of enumerate([
    ["Health Regen", "hpregen", "heal", "❣"],
    ["Mana Regen", "manaregen", "manarg", "⸎"],
    ["Strength", "strength", "earth", "✤"],
    ["Dexterity", "dexterity", "thunder", "✦"],
    ["Intelligence", "intelligence", "water", "❉"],
    ["Defence", "defence", "fire", "✹"],
    ["Agility", "agility", "air", "❋"],
  ])) {
    for (const [j, size] of enumerate([
      "Tiny",
      "Small",
      "Medium",
      "Large",
      "Extra Large",
    ])) {
      let shopData = potions.POTION_SHOP_DATA.potionShop[j * 7 + i];

      const potionImage = potions.potionSprites[buff][j];
      if (potionImage) {
        let tooltip = `
                    <h3>${size} ${prettyBuff} Potion</h3>
                    <p>Level requirement: <span class="lvl">${shopData.lvlreq}</span></p>
                    <p>Cost: <span class="ap">${shopData.cost} AP</span></p>
                    <p><span class="${className}">${symbol} +${shopData[buff + "add"]}</span> for <span class="primary">${shopData.turns} turns</span></p>
                `;
        potionData[i + 1][j + 1] = item("", {
          defaultTag: "img",
          src: potionImage,
          tooltip,
        });
      }
    }
  }

  miscInfoGroup.appendChild(classTable(potionData, "potion-info"));

  islands.updateData(data);
  handleSizeChange();
}

const listeners = [];
export function addIdChangeListener(listener) {
  listeners.push(listener);
}

window.addEventListener("load", () => {
  loadForumPosts();

  for (const gameLink of document.getElementsByClassName("game-link")) {
    gameLink.addEventListener("click", (e) => {
      e.preventDefault();
      const gameId = gameLink.dataset.gameId;
      history.pushState({ gameId }, null, gameLink.href);
      window.GAME_ID = gameId;
      updateGameDisplay(undefined, true);
      for (const listener of listeners) {
        listener(gameId);
      }
    });
  }
});

window.addEventListener("popstate", (e) => {
  let { gameId } = e.state ?? {};
  gameId = gameId ?? "maina";
  window.GAME_ID = gameId;
  updateGameDisplay(undefined, true);
  for (const listener of listeners) {
    listener(gameId);
  }
});
