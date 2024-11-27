const PI = Math.PI;
const TAU = PI * 2;
const FONT = "Roboto";

let data = null;
export function updateData(newData) {
  data = newData;
  deltaTime = 0;
  draw(true);
}

/** @type { HTMLCanvasElement } */
let canvas;
/** @type { CanvasRenderingContext2D } */
let ctx;

let pixelWidth, pixelHeight;
let radX, radY;
let transformationMatrix;

function getCanvas() {
  return document.getElementById("island-canvas");
}

let mouseX, mouseY;
let mousePressed = false,
  mouseMoved = false;
export function init() {
  document.addEventListener("mousemove", (e) => {
    if (e.target === canvas) {
      mouseX = ((e.offsetX / pixelWidth) * 2 - 1) * radX;
      mouseY = ((e.offsetY / pixelHeight) * 2 - 1) * radY;
      mouseMoved = true;
    } else {
      mouseX += (e.movementX / pixelWidth) * 2 * radX;
      mouseY += (e.movementY / pixelHeight) * 2 * radY;
    }
  });
  document.addEventListener("mousedown", (e) => {
    if (e.button == 0) mousePressed = true;
  });
  document.addEventListener("mouseup", (e) => {
    if (e.button == 0) mousePressed = false;
  });
}

let queuedResizeEl = null;
export function resizeCanvasTo(el) {
  if (!el) return;
  queuedResizeEl = el;
}

let lastTime,
  deltaTime,
  timeElapsed = 0;
let timeUntilNextSave = 10;
export function animationFrame(time) {
  const newCanvas = getCanvas();
  if (!newCanvas) {
    window.requestAnimationFrame(animationFrame);
    return;
  }
  if (newCanvas !== canvas) {
    // Assume previous canvas is deleted
    canvas = newCanvas;
    console.log("getting new context");
    ctx = canvas.getContext("2d");
  }

  if (queuedResizeEl) {
    const rect = queuedResizeEl.getBoundingClientRect();
    canvas.width = pixelWidth = rect.width;
    canvas.height = pixelHeight = rect.height;

    // Constrain to 4:3
    let xRatio = pixelWidth / 4;
    let yRatio = pixelHeight / 3;
    let min = Math.min(xRatio, yRatio);
    radX = ((xRatio / min) * 4) / 2;
    radY = ((yRatio / min) * 3) / 2;

    transformationMatrix = new DOMMatrix();
    transformationMatrix.translateSelf(pixelWidth / 2, pixelHeight / 2);
    transformationMatrix.scaleSelf(
      pixelWidth / radX / 2,
      pixelHeight / radY / 2,
    );

    queuedResizeEl = null;

    deltaTime = 0;
    ctx.setTransform(transformationMatrix);
    draw(true);
  }
  if (canvas && data) {
    if (lastTime === undefined) lastTime = time;

    deltaTime = Math.min((time - lastTime) / 1000, 0.1);
    timeElapsed += deltaTime;
    timeUntilNextSave -= deltaTime;
    if (timeUntilNextSave <= 0) {
      timeUntilNextSave += 10;
      save();
    }

    ctx.setTransform(transformationMatrix);
    draw();

    lastTime = time;
  }

  window.requestAnimationFrame(animationFrame);
}

function hypotInRad(x, y, rad) {
  return x * x + y * y <= rad * rad;
}

const islands = [];

const ISLAND_RADIUS = 0.2;
const ISLAND_GRAVITY_FORCE = 0.03;
const ISLAND_EDGE_FORCE = 0.2;
const ISLAND_EDGE_SIZE = 0.7;
const ISLAND_MAX_EDGE_DISTANCE = 1.4;
const ISLAND_DAMPING = 2;
const ISLAND_DRAG_FORCE = 0.5;
const ISLAND_DRAG_DAMPING = 3;
const ISLAND_REPEL_RADIUS = 1.8;
const ISLAND_REPEL_FORCE = 1.8;

class Island {
  constructor(id) {
    this.id = id;

    // Spawn the atom somewhere on the edge
    let edgePos = Math.random() * 4;
    if (edgePos < 2) {
      this.x = edgePos > 1 ? -radX : radX;
      this.y = ((edgePos % 1) * 2 - 1) * radY;
    } else {
      this.x = ((edgePos % 1) * 2 - 1) * radX;
      this.y = edgePos > 3 ? -radY : radY;
    }

    // Add some random displacement
    this.x += (Math.random() - 0.5) * 5;
    this.y += (Math.random() - 0.5) * 5;

    this.velX = -this.x * 0.01;
    this.velY = -this.y * 0.01;

    this.connected = [];
  }
  prepareUpdate() {
    for (const island of islands) {
      if (island === this) continue;
      if (
        hypotInRad(this.x - island.x, this.y - island.y, ISLAND_REPEL_RADIUS)
      ) {
        let repelFac =
          (1 -
            Math.hypot(this.x - island.x, this.y - island.y) /
              ISLAND_REPEL_RADIUS) **
            6 *
          ISLAND_REPEL_FORCE *
          deltaTime;
        this.velX += (this.x - island.x) * repelFac;
        this.velY += (this.y - island.y) * repelFac;
      }
    }
    for (const island of this.connected) {
      const dx = island.x - this.x,
        dy = island.y - this.y;
      const fac =
        Math.min(
          Math.hypot(dx, dy) - ISLAND_EDGE_SIZE,
          ISLAND_MAX_EDGE_DISTANCE,
        ) **
          2 *
        ISLAND_EDGE_FORCE *
        deltaTime;
      this.velX += dx * fac;
      this.velY += dy * fac;
    }
    this.velX -= this.x * ISLAND_GRAVITY_FORCE * deltaTime;
    this.velY -= this.y * ISLAND_GRAVITY_FORCE * deltaTime;
    const speed = Math.hypot(this.velX, this.velY);
    const fac = Math.max(
      0,
      1 - Math.max(1, speed ** 3 * 9) * ISLAND_DAMPING * deltaTime,
    );
    this.velX *= fac;
    this.velY *= fac;
  }
  update() {
    this.x += this.velX;
    this.y += this.velY;
  }
  addDragForce() {
    this.velX += ISLAND_DRAG_FORCE * (mouseX - this.x) * deltaTime;
    this.velY += ISLAND_DRAG_FORCE * (mouseY - this.y) * deltaTime;
    this.velX *= 1 - ISLAND_DRAG_DAMPING * deltaTime;
    this.velY *= 1 - ISLAND_DRAG_DAMPING * deltaTime;
  }
}

let draggedIsland = null;
let motionTimer = 1.0;

function draw(force) {
  if (!ctx || (!force && motionTimer <= 0 && !mousePressed && !mouseMoved)) {
    return;
  }
  mouseMoved = false;

  ctx.clearRect(-radX, -radY, radX * 2, radY * 2);

  while (islands.length < data.islandsBuilt) {
    islands.push(new Island(islands.length + 1));
  }
  while (islands.length > data.islandsBuilt) {
    islands.pop();
  }

  for (const island of islands) {
    island.prepareUpdate();
  }

  if (!mousePressed) draggedIsland = null;

  ctx.beginPath();
  const tollBridges = [];
  for (const island of islands) {
    const connected = [];
    for (const { first: otherId, second: cost } of data.bridges[island.id]) {
      if (cost == -1) continue;
      const other = islands[otherId - 1];
      connected.push(other);
      if (otherId > island.id) {
        if (cost > 0) {
          tollBridges.push([island, other, cost]);
        } else {
          ctx.moveTo(island.x, island.y);
          ctx.lineTo(other.x, other.y);
        }
      }
    }
    island.connected = connected;
  }
  ctx.lineWidth = 0.15;
  ctx.strokeStyle = "#a66ecc60";
  ctx.stroke();
  ctx.lineWidth = 0.02;
  ctx.strokeStyle = "#a66eccd0";
  ctx.stroke();

  ctx.beginPath();
  for (const [i1, i2, cost] of tollBridges) {
    ctx.moveTo(i1.x, i1.y);
    ctx.lineTo(i2.x, i2.y);
  }
  ctx.lineWidth = 0.15;
  ctx.strokeStyle = "#00b02060";
  ctx.stroke();
  ctx.lineWidth = 0.02;
  ctx.strokeStyle = "#00e03060";
  ctx.stroke();

  ctx.beginPath();
  ctx.textAlign = "center";
  let hoveredIsland = null;
  let isThereMotionThisFrame = false;
  for (const island of islands) {
    island.update();
    if (!hypotInRad(island.velX, island.velY, 0.0001)) {
      isThereMotionThisFrame = true;
    }
    if (
      !draggedIsland &&
      mouseX !== undefined &&
      hypotInRad(mouseX - island.x, mouseY - island.y, ISLAND_RADIUS)
    ) {
      hoveredIsland = island;
    } else {
      ctx.moveTo(island.x + ISLAND_RADIUS, island.y);
      ctx.arc(island.x, island.y, ISLAND_RADIUS, 0, TAU);
    }
  }
  ctx.lineWidth = 0.02;
  ctx.fillStyle = "#672f97";
  ctx.strokeStyle = "#a66ecc";
  ctx.fill();
  ctx.stroke();

  ctx.fillStyle = "#00e820";
  ctx.font = `0.15px '${FONT}'`;
  for (const [i1, i2, cost] of tollBridges) {
    ctx.fillText(cost + " AP", (i1.x + i2.x) / 2, (i1.y + i2.y) / 2 + 0.05);
  }

  ctx.fillStyle = "#cd8fff";
  ctx.font = `0.18px '${FONT}'`;
  for (const island of islands) {
    if (hoveredIsland !== island) {
      ctx.fillText(island.id, island.x - 0.008, island.y + 0.062);
    }
  }

  ctx.font = `0.09px '${FONT}'`;
  let islandPlayerCount = Array(islands.length).fill(0);
  for (let i = 0; i < data.arr.length; i++) {
    const islandId = data.arr[i].location - 1;
    const island = islands[islandId];
    const playerName = data.arr[i].name;
    if (data.arr[i].isAlive == false) continue
    let playerCount = islandPlayerCount[islandId]++;

    ctx.fillStyle = hoveredIsland === island ? "#ffe5fb" : "#f6c3ff";
    ctx.fillText(playerName, island.x, island.y - playerCount * 0.1 - 0.35);
  }

  if (draggedIsland) {
    canvas.style.cursor = "grabbing";

    draggedIsland.addDragForce();
    ctx.beginPath();
    ctx.moveTo(draggedIsland.x, draggedIsland.y);
    ctx.lineTo(mouseX, mouseY);
    ctx.strokeStyle = "#cd96e6";
    ctx.lineWidth = 0.012;
    ctx.stroke();
  } else if (hoveredIsland) {
    canvas.style.cursor = "pointer";

    ctx.lineWidth = 0.02;
    ctx.beginPath();
    ctx.fillStyle = "#7944a5";
    ctx.strokeStyle = "#cd96e6";
    ctx.arc(hoveredIsland.x, hoveredIsland.y, ISLAND_RADIUS, 0, TAU);
    ctx.fill();
    ctx.stroke();

    ctx.font = `0.18px '${FONT}'`;
    ctx.fillStyle = "#f6c3ff";
    ctx.fillText(
      hoveredIsland.id,
      hoveredIsland.x - 0.008,
      hoveredIsland.y + 0.062,
    );

    if (mousePressed) draggedIsland = hoveredIsland;
  } else {
    canvas.style.cursor = "auto";
  }

  if (isThereMotionThisFrame) {
    motionTimer = 1.0;
  } else {
    motionTimer -= deltaTime;
  }
}

function save() {}
