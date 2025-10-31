const promoList = document.querySelector(".promo-list");
const promoNext = document.querySelector(".promo-control.next");
const promoPrev = document.querySelector(".promo-control.prev");

if (promoNext && promoPrev) {
    promoNext.addEventListener("click", () => {
        promoList.scrollBy({ left: 200, behavior: "smooth" });
    });
    promoPrev.addEventListener("click", () => {
        promoList.scrollBy({ left: -200, behavior: "smooth" });
    });
}