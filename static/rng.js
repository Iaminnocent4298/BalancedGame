
export function init() {
    const rngMin = document.getElementById("rng-min");
    const rngMax = document.getElementById("rng-max");
    const rngInclusive = document.getElementById("rng-inclusive");
    const rngGenerate = document.getElementById("rng-generate");
    const rngResult = document.getElementById("rng-result");

    function generateRandomValue(cryptographic) {
        const random = cryptographic ?
            crypto.getRandomValues(new Uint32Array(1))[0] / 0xffffffff :
            Math.random();
        let max = parseFloat(rngMax.value);
        let min = parseFloat(rngMin.value);
        const isInteger = Number.isInteger(max) && Number.isInteger(min) && !rngMax.value.includes(".") && !rngMin.value.includes(".");
        if (isInteger && rngInclusive.checked)
            max++;
        const res = isInteger ? Math.floor(random * (max - min)) + min : (random * (max - min) + min).toFixed(2);
        rngResult.textContent = res;
        rngResult.classList.remove("inactive");
    }

    let animationInterval = null;
    let moreAnimationFrames = 0;
    function animateRandomValue() {
        moreAnimationFrames = 5;
        if (animationInterval === null) {
            animationInterval = window.setInterval(() => {
                moreAnimationFrames--;
                if (moreAnimationFrames <= 0) {
                    window.clearInterval(animationInterval);
                    animationInterval = null;
                    generateRandomValue(true);
                } else {
                    generateRandomValue(false);
                }
            }, 50);
        }
    }

    rngGenerate.addEventListener("click", animateRandomValue);
}