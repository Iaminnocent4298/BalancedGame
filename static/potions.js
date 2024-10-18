const POTION_SPRITE_DATA = {
  sprites: [
    [
      "00000000",
      "00000000",
      "00033000",
      "00333300",
      "00200200",
      "00255200",
      "00145200",
      "00111100",
    ],
    [
      "00000000",
      "00033000",
      "00333300",
      "00200200",
      "00255200",
      "00145200",
      "00154100",
      "00111100",
    ],
    [
      "00000000",
      "00033000",
      "00333300",
      "00200200",
      "02555520",
      "02454520",
      "01445410",
      "00111100",
    ],
    [
      "00033000",
      "00333300",
      "00200200",
      "02555520",
      "01545420",
      "02454510",
      "01444410",
      "00111100",
    ],
    [
      "00033000",
      "00333300",
      "02000020",
      "25555552",
      "24545452",
      "14454552",
      "24444441",
      "01111110",
    ],
  ],
  palettes: {
    hpregen: {
      dark: "#d92525",
      light: "#ff5667",
    },
    manaregen: {
      dark: "#0cbbcc",
      light: "#56e0ff",
    },
    strength: {
      dark: "#1bc32a",
      light: "#54dd36",
    },
    dexterity: {
      dark: "#d0c129",
      light: "#f2e23e",
    },
    intelligence: {
      dark: "#1c7ccc",
      light: "#3ea0f2",
    },
    defence: {
      dark: "#c34124",
      light: "#ff6e4d",
    },
    agility: {
      dark: "#b6bcc8",
      light: "#c6d5e1",
    },
  },
  base_palette: ["#000000", "#cdd9f2", "#deeeff", "#934c20"],
};

export let potionShopData = undefined;

const potionShopDataPromise = fetch("/static/potions.json")
  .then((res) => res.json())
  .then((json) => {
    console.log("loaded potion data");
    potionShopData = json;
  });

export const potionSprites = {};
let numSpritesLoaded = 0;

function fromHex(hex) {
  return [
    parseInt(hex.substring(1, 3), 16),
    parseInt(hex.substring(3, 5), 16),
    parseInt(hex.substring(5, 7), 16),
  ];
}

export function init(callback) {
  const canvas = document.createElement("canvas");
  canvas.width = 8;
  canvas.height = 8;
  const ctx = canvas.getContext("2d");
  const pixels = new ImageData(8, 8);
  const pixelData = pixels.data;
  for (const buff of [
    "hpregen",
    "manaregen",
    "strength",
    "dexterity",
    "intelligence",
    "defence",
    "agility",
  ]) {
    potionSprites[buff] = Array(5);
    const paletteData = POTION_SPRITE_DATA.palettes[buff];
    const palette = [
      ...POTION_SPRITE_DATA.base_palette,
      paletteData.dark,
      paletteData.light,
    ];
    for (let size = 0; size < 5; ++size) {
      const sprite = POTION_SPRITE_DATA.sprites[size];
      for (let y = 0; y < 8; ++y) {
        for (let x = 0; x < 8; ++x) {
          let index = +sprite[y][x];
          let rgba;
          if (index === 0) {
            rgba = [0, 0, 0, 0];
          } else {
            rgba = [...fromHex(palette[index]), 255];
          }
          let pixelIndex = (y * 8 + x) * 4;
          pixelData[pixelIndex] = rgba[0];
          pixelData[pixelIndex + 1] = rgba[1];
          pixelData[pixelIndex + 2] = rgba[2];
          pixelData[pixelIndex + 3] = rgba[3];
        }
      }

      ctx.putImageData(pixels, 0, 0);

      canvas.toBlob((blob) => {
        potionSprites[buff][size] = URL.createObjectURL(blob);
        numSpritesLoaded++;
        if (numSpritesLoaded == 7 * 5) {
          console.log("loaded potion sprites");
          potionShopDataPromise.then(callback);
        }
      });
    }
  }
}
