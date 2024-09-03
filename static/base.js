const errorContainerEl = document.getElementById("error-container");

window.addEventListener("error", (e) => {
  errorContainerEl.hidden = false;
  errorContainerEl.querySelector("pre").textContent = e.error.stack;
});
errorContainerEl.querySelector("button").addEventListener("click", () => {
  errorContainerEl.hidden = true;
});
