import { updateGameDisplay, addIdChangeListener } from "./display.js";

let socket = null;

const terminalTextContainer = document.getElementById(
  "terminal-text-container",
);
const terminalEl = document.getElementById("terminal-text");
const terminalStartButton = document.getElementById("terminal-start");

let closed = false;

let firstTime = true;
let hasInteracted = false;

let inputEl;
let caretEl;

let animationInterval;

function resetCaretAnimationCycle() {
  if (animationInterval !== undefined) {
    window.clearInterval(animationInterval);
  }
  if (caretEl) caretEl.hidden = false;
  animationInterval = window.setInterval(() => {
    if (caretEl) caretEl.hidden = !caretEl.hidden;
    else window.clearInterval(animationInterval);
  }, 400);
}

function moveInputToLast() {
  if (!inputEl) {
    function updateInput() {
      inputEl.style.width = inputEl.value.length + "ch";
      resetCaretAnimationCycle();
    }

    inputEl = document.createElement("input");
    inputEl.id = "terminal-input";
    inputEl.addEventListener("input", updateInput);
    inputEl.addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        send();
      }
    });
    updateInput();
  }
  inputEl.disabled = false;
  terminalEl.lastChild.append(inputEl);
  if (!caretEl) {
    caretEl = document.createElement("div");
    caretEl.id = "terminal-caret";
  }
  inputEl.after(caretEl);
  resetCaretAnimationCycle();
  terminalTextContainer.scrollTop = terminalTextContainer.scrollHeight;
  if (hasInteracted) inputEl.focus();
}

function write(fd, text) {
  if (!text) return;

  const el = document.createElement("span");
  if (fd == 2) el.className = "bad";
  else if (fd == 1) el.className = "slight-inactive";

  el.textContent = text;
  terminalEl.appendChild(el);
}

function updateTerminal(data) {
  console.log("term_data", data);
  if (inputEl?.value) {
    write(0, inputEl.value + "\n");
    inputEl.value = "";
    inputEl.style.width = "0";
  }
  if (closed && !data.closed) {
    // Program restarted, clear the terminal
    terminalEl.replaceChildren();
  }
  write(1, data.stdout);
  write(2, data.stderr);

  setClosed(data.closed);
  if (closed) {
    terminalStartButton.disabled = false;
    const el = document.createElement("div");
    el.textContent = `--- Program stopped ---`;
    el.className = "inactive";
    terminalEl.appendChild(el);
    return;
  }

  moveInputToLast();
}

function setLast() {
  socket.emit("term_last", (data) => {
    console.log("term_last got", data);

    if (data.closed) {
      setClosed(true);
      return;
    }

    const el = document.createElement("div");
    el.textContent = `--- Session ${firstTime ? "restored" : "lost, restarting"} ---`;
    el.className = "inactive";
    terminalEl.appendChild(el);

    firstTime = false;

    for (const [fd, text] of data.last) {
      write(fd, text);
    }

    moveInputToLast();
  });
}

const containerEl = document.getElementById("terminal-text-container");

const fileUploadButtonEl = document.querySelector(
  '#upload-gamedata input[type="submit"]',
);

const uploadGameDataEl = document.getElementById("upload-gamedata");

addIdChangeListener((gameId) => {
  uploadGameDataEl.querySelector("form").action =
    `/api/set_game_data/${gameId}`;
  uploadGameDataEl.querySelector("b").textContent = `${gameId}.json`;
});

window.addEventListener("load", () => {
  document.body.classList.add("loaded");
  containerEl.addEventListener("click", () => {
    hasInteracted = true;
    inputEl?.focus();
    resetCaretAnimationCycle();
  });
  containerEl.addEventListener("blur", () => {
    window.clearInterval(animationInterval);
    caretEl.hidden = false;
  });

  terminalStartButton.addEventListener("click", () => {
    if (!closed) return;

    socket.emit("term_start");
    terminalStartButton.disabled = true;
  });

  window.socket = socket = io(window.location.origin, {
    path: window.BASE_URL + "/socket.io",
  });
  socket.on("data", (data) => {
    updateGameDisplay(data);
  });

  socket.on("term_data", (data) => updateTerminal(data));

  socket.once("connect", () => setLast());

  let nodcon = false;

  socket.on("dont_force_dcon_youre_fine", () => {
    nodcon = true;
  });
  socket.on("force_disconnect", () => {
    if (nodcon) {
      nodcon = false;
      return;
    }
    socket.disconnect();
    setClosed(false);
    document.body.classList.add("disconnected");
  });
  socket.on("disconnect", () => {
    const el = document.createElement("div");
    el.textContent = "--- Connection closed ---";
    el.className = "inactive";
    terminalEl.appendChild(el);
    if (inputEl) inputEl.disabled = true;
  });

  setClosed(closed);

  updateGameDisplay(window.LOADED_DATA, true);
});

function send(text) {
  if (text === undefined) {
    text = inputEl.value;
    window.clearInterval(animationInterval);
  }
  socket.emit("term_send", { input: text + "\n" });
}

function setClosed(newClosed) {
  closed = newClosed;
  if (newClosed) {
    document.body.classList.add("closed");

    fileUploadButtonEl.classList.remove("inactive");
    fileUploadButtonEl.removeAttribute("disabled");
    fileUploadButtonEl.value = "Upload";
  } else {
    document.body.classList.remove("closed");

    console.log(fileUploadButtonEl);
    fileUploadButtonEl.classList.add("inactive");
    fileUploadButtonEl.setAttribute("disabled", "");
    fileUploadButtonEl.value = "Program is running";
  }
}
