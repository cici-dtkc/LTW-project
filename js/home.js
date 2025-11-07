let index = 0;
const slideEls = Array.from(document.querySelectorAll(".slide"));

function showSlide(i) {
    const len = slideEls.length;
    if (len === 0) return;
    index = ((i % len) + len) % len; // safe modulo

    slideEls.forEach((slide, idx) => {
        if (idx === index) {
            slide.classList.add("active");
        } else {
            slide.classList.remove("active");
        }
    });

}

function nextSlide() {
    showSlide(index + 1);
}

function prevSlide() {
    showSlide(index - 1);
}

// Auto slide
let autoTimer = setInterval(nextSlide, 4000);

// Buttons
const nextBtn = document.querySelector(".next");
const prevBtn = document.querySelector(".prev");
if (nextBtn) nextBtn.onclick = () => { clearInterval(autoTimer); nextSlide(); autoTimer = setInterval(nextSlide, 4000); };
if (prevBtn) prevBtn.onclick = () => { clearInterval(autoTimer); prevSlide(); autoTimer = setInterval(nextSlide, 4000); };

// Init
showSlide(0);
