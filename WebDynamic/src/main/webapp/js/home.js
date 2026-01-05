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

// Update price when variant button is clicked
document.querySelectorAll('.capacity button').forEach(button => {
    button.addEventListener('click', function() {
        const productId = this.getAttribute('data-product-id');
        const newPrice = this.getAttribute('data-price');
        const oldPrice = this.getAttribute('data-old-price');

        // Update price-new
        const priceNewEl = document.getElementById('price-new-' + productId);
        if (priceNewEl) {
            priceNewEl.textContent = new Intl.NumberFormat('vi-VN').format(newPrice) + '₫';
        }

        // Update price-old if exists
        const priceOldEl = document.getElementById('price-old-' + productId);
        if (priceOldEl) {
            priceOldEl.textContent = new Intl.NumberFormat('vi-VN').format(oldPrice) + '₫';
        }

        // Update active class
        this.parentElement.querySelectorAll('button').forEach(btn => btn.classList.remove('active'));
        this.classList.add('active');
    });
});

// Product navigation for phones
const phonesList = document.getElementById('product-list-phones');
const phonesCards = phonesList.querySelectorAll('.product-card');
let phonesCurrentIndex = 0;
const phonesPerPage = 4;

function showPhones(startIndex) {
    phonesCards.forEach((card, index) => {
        if (index >= startIndex && index < startIndex + phonesPerPage) {
            card.classList.remove('hidden');
        } else {
            card.classList.add('hidden');
        }
    });
    // Update button states
    document.getElementById('phones-prev').disabled = startIndex === 0;
    document.getElementById('phones-next').disabled = startIndex + phonesPerPage >= phonesCards.length;
}

document.getElementById('phones-next').addEventListener('click', () => {
    if (phonesCurrentIndex + phonesPerPage < phonesCards.length) {
        phonesCurrentIndex += phonesPerPage;
        showPhones(phonesCurrentIndex);
    }
});

document.getElementById('phones-prev').addEventListener('click', () => {
    if (phonesCurrentIndex > 0) {
        phonesCurrentIndex -= phonesPerPage;
        showPhones(phonesCurrentIndex);
    }
});

// Product navigation for accessories
const accessoriesList = document.getElementById('product-list-accessories');
const accessoriesCards = accessoriesList.querySelectorAll('.product-card');
let accessoriesCurrentIndex = 0;
const accessoriesPerPage = 4;

function showAccessories(startIndex) {
    accessoriesCards.forEach((card, index) => {
        if (index >= startIndex && index < startIndex + accessoriesPerPage) {
            card.classList.remove('hidden');
        } else {
            card.classList.add('hidden');
        }
    });
    // Update button states
    document.getElementById('accessories-prev').disabled = startIndex === 0;
    document.getElementById('accessories-next').disabled = startIndex + accessoriesPerPage >= accessoriesCards.length;
}

document.getElementById('accessories-next').addEventListener('click', () => {
    if (accessoriesCurrentIndex + accessoriesPerPage < accessoriesCards.length) {
        accessoriesCurrentIndex += accessoriesPerPage;
        showAccessories(accessoriesCurrentIndex);
    }
});

document.getElementById('accessories-prev').addEventListener('click', () => {
    if (accessoriesCurrentIndex > 0) {
        accessoriesCurrentIndex -= accessoriesPerPage;
        showAccessories(accessoriesCurrentIndex);
    }
});