import { getGameData, updateGameDisplay } from "./display.js";

let socket = null;

window.addEventListener("load", () => {
  socket = io(window.location.origin, { path: window.BASE_URL + "/socket.io" });
  socket.on("data", (data) => updateGameDisplay(data));

  updateGameDisplay(window.LOADED_DATA, true);
});
