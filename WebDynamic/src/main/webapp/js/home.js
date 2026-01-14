// let index = 0;
// const slideEls = Array.from(document.querySelectorAll(".slide"));
//
// function showSlide(i) {
//     const len = slideEls.length;
//     if (len === 0) return;
//     index = ((i % len) + len) % len; // safe modulo
//
//     slideEls.forEach((slide, idx) => {
//         if (idx === index) {
//             slide.classList.add("active");
//         } else {
//             slide.classList.remove("active");
//         }
//     });
//
// }
//
// function nextSlide() {
//     showSlide(index + 1);
// }
//
// function prevSlide() {
//     showSlide(index - 1);
// }
//
// // Auto slide
// let autoTimer = setInterval(nextSlide, 4000);
//
// // Buttons
// const nextBtn = document.querySelector(".next");
// const prevBtn = document.querySelector(".prev");
// if (nextBtn) nextBtn.onclick = () => { clearInterval(autoTimer); nextSlide(); autoTimer = setInterval(nextSlide, 4000); };
// if (prevBtn) prevBtn.onclick = () => { clearInterval(autoTimer); prevSlide(); autoTimer = setInterval(nextSlide, 4000); };
//
// // Init
// showSlide(0);
//
// // Update price when variant button is clicked
// document.querySelectorAll('.capacity button').forEach(button => {
//     button.addEventListener('click', function() {
//         const productId = this.getAttribute('data-product-id');
//         const newPrice = parseFloat(this.getAttribute('data-price'));
//         const oldPrice = parseFloat(this.getAttribute('data-old-price'));
//
//         const priceNewEl = document.getElementById('price-new-' + productId);
//         const priceOldEl = document.getElementById('price-old-' + productId);
//
//         const formatter = new Intl.NumberFormat('vi-VN');
//
//         if (priceNewEl && !isNaN(newPrice)) {
//             priceNewEl.textContent = formatter.format(Math.round(newPrice)) + '₫';
//         }
//         if (priceOldEl && !isNaN(oldPrice)) {
//             priceOldEl.textContent = formatter.format(Math.round(oldPrice)) + '₫';
//         }
//
//         // Active class logic
//         const parent = this.parentElement;
//         parent.querySelectorAll('button').forEach(btn => btn.classList.remove('active'));
//         this.classList.add('active');
//     });
// });
//
// // Product navigation for phones
// const phonesList = document.getElementById('product-list-phones');
// const phonesCards = phonesList.querySelectorAll('.product-card');
// let phonesCurrentIndex = 0;
// const phonesPerPage = 4;
//
// function showPhones(startIndex) {
//     phonesCards.forEach((card, index) => {
//         if (index >= startIndex && index < startIndex + phonesPerPage) {
//             card.classList.remove('hidden');
//         } else {
//             card.classList.add('hidden');
//         }
//     });
//     // Update button states
//     document.getElementById('phones-prev').disabled = startIndex === 0;
//     document.getElementById('phones-next').disabled = startIndex + phonesPerPage >= phonesCards.length;
// }
//
// document.getElementById('phones-next').addEventListener('click', () => {
//     if (phonesCurrentIndex + phonesPerPage < phonesCards.length) {
//         phonesCurrentIndex += phonesPerPage;
//         showPhones(phonesCurrentIndex);
//     }
// });
//
// document.getElementById('phones-prev').addEventListener('click', () => {
//     if (phonesCurrentIndex > 0) {
//         phonesCurrentIndex -= phonesPerPage;
//         showPhones(phonesCurrentIndex);
//     }
// });
//
// // Product navigation for accessories
// const accessoriesList = document.getElementById('product-list-accessories');
// const accessoriesCards = accessoriesList.querySelectorAll('.product-card');
// let accessoriesCurrentIndex = 0;
// const accessoriesPerPage = 4;
//
// function showAccessories(startIndex) {
//     accessoriesCards.forEach((card, index) => {
//         if (index >= startIndex && index < startIndex + accessoriesPerPage) {
//             card.classList.remove('hidden');
//         } else {
//             card.classList.add('hidden');
//         }
//     });
//     // Update button states
//     document.getElementById('accessories-prev').disabled = startIndex === 0;
//     document.getElementById('accessories-next').disabled = startIndex + accessoriesPerPage >= accessoriesCards.length;
// }
//
// document.getElementById('accessories-next').addEventListener('click', () => {
//     if (accessoriesCurrentIndex + accessoriesPerPage < accessoriesCards.length) {
//         accessoriesCurrentIndex += accessoriesPerPage;
//         showAccessories(accessoriesCurrentIndex);
//     }
// });
//
// document.getElementById('accessories-prev').addEventListener('click', () => {
//     if (accessoriesCurrentIndex > 0) {
//         accessoriesCurrentIndex -= accessoriesPerPage;
//         showAccessories(accessoriesCurrentIndex);
//     }
// });

document.addEventListener("DOMContentLoaded", () => {
    // 1. XỬ LÝ SLIDER BANNER
    let slideIndex = 0;
    const slideEls = Array.from(document.querySelectorAll(".slide"));
    const nextBtn = document.querySelector(".next");
    const prevBtn = document.querySelector(".prev");

    function showSlide(i) {
        const len = slideEls.length;
        if (len === 0) return;
        slideIndex = ((i % len) + len) % len;
        slideEls.forEach((slide, idx) => {
            slide.classList.toggle("active", idx === slideIndex);
        });
    }

    if (slideEls.length > 0) {
        let autoTimer = setInterval(() => showSlide(slideIndex + 1), 4000);

        if (nextBtn) {
            nextBtn.onclick = () => {
                clearInterval(autoTimer);
                showSlide(slideIndex + 1);
                autoTimer = setInterval(() => showSlide(slideIndex + 1), 4000);
            };
        }
        if (prevBtn) {
            prevBtn.onclick = () => {
                clearInterval(autoTimer);
                showSlide(slideIndex - 1);
                autoTimer = setInterval(() => showSlide(slideIndex + 1), 4000);
            };
        }
        showSlide(0);
    }

    // 2. CẬP NHẬT GIÁ KHI CHỌN BIẾN THỂ (CAPACITY)
    document.querySelectorAll('.capacity button').forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            const productId = this.getAttribute('data-product-id');
            const newPrice = parseFloat(this.getAttribute('data-price'));
            const oldPrice = parseFloat(this.getAttribute('data-old-price'));

            const priceNewEl = document.getElementById('price-new-' + productId);
            const priceOldEl = document.getElementById('price-old-' + productId);
            const formatter = new Intl.NumberFormat('vi-VN');

            if (priceNewEl && !isNaN(newPrice)) {
                priceNewEl.textContent = formatCurrency(newPrice);
            }


            if (priceOldEl && !isNaN(oldPrice)) {
                priceOldEl.textContent = formatCurrency(oldPrice);

                priceOldEl.style.display = 'inline-block';
            } else if (priceOldEl) {

                priceOldEl.style.display = 'none';
            }

            const parent = this.parentElement;
            parent.querySelectorAll('button').forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
        });
    });

    function formatCurrency(number) {
        return new Intl.NumberFormat('vi-VN').format(Math.round(number)) + '₫';
    }

    // 3. ĐIỀU HƯỚNG SẢN PHẨM (PHONES & ACCESSORIES)
    function setupNavigation(listId, prevBtnId, nextBtnId) {
        const listEl = document.getElementById(listId);
        const btnPrev = document.getElementById(prevBtnId);
        const btnNext = document.getElementById(nextBtnId);

        if (!listEl || !btnPrev || !btnNext) return;

        const cards = listEl.querySelectorAll('.product-card');
        let currentIndex = 0;
        const perPage = 4;

        function updateDisplay(start) {
            cards.forEach((card, i) => {
                card.style.display = (i >= start && i < start + perPage) ? 'block' : 'none';
            });
            btnPrev.disabled = (start === 0);
            btnNext.disabled = (start + perPage >= cards.length);
        }

        btnNext.onclick = () => {
            if (currentIndex + perPage < cards.length) {
                currentIndex += perPage;
                updateDisplay(currentIndex);
            }
        };

        btnPrev.onclick = () => {
            if (currentIndex > 0) {
                currentIndex -= perPage;
                updateDisplay(currentIndex);
            }
        };

        updateDisplay(0); // Khởi tạo trạng thái ban đầu
    }

    setupNavigation('product-list-phones', 'phones-prev', 'phones-next');
    setupNavigation('product-list-accessories', 'accessories-prev', 'accessories-next');

    // 4. FIX LỖI VỠ FORM GIỎ HÀNG (AJAX)
    const cartButtons = document.querySelectorAll('.cart-btn.add-to-cart');
    cartButtons.forEach(btn => {
        btn.onclick = function (e) {
            e.preventDefault();
            e.stopPropagation();

            const productCard = btn.closest('.product-card');
            const activeVariant = productCard.querySelector('.capacity button.active');

            if (!activeVariant) {
                alert("Vui lòng chọn phiên bản sản phẩm!");
                return;
            }

            const vcId = activeVariant.getAttribute('data-id');
            const header = document.getElementById('header');
            const contextPath = header ? header.getAttribute('data-context-path') : '';

            fetch(`${contextPath}/cart?action=add&vcId=${vcId}`)
                .then(response => {
                    if (response.status === 401) {
                        alert("Vui lòng đăng nhập để thêm vào giỏ hàng!");
                        window.location.href = contextPath + "/login";

                        throw new Error("UNAUTHORIZED");
                    }
                    if (!response.ok) throw new Error("SERVER_ERROR");
                    return response.text();
                })
                .then(totalCount => {
                    // Chỉ thực hiện khi đã đăng nhập (totalCount là con số)
                    const cartBadge = document.getElementById('cart-badge');
                    if (cartBadge && totalCount) {
                        cartBadge.textContent = totalCount.trim();
                    }
                    alert("Đã thêm vào giỏ hàng!");
                })
                .catch(err => {
                    if (err.message !== "UNAUTHORIZED") {
                        console.error("Lỗi:", err);
                    }
                });
        };
    });
});