const promoList = document.querySelector(".promo-list");
const promoNext = document.querySelector(".promo-control.next");
const promoPrev = document.querySelector(".promo-control.prev");

if (promoNext && promoPrev) {
    promoNext.addEventListener("click", () => {
        promoList.scrollBy({left: 200, behavior: "smooth"});
    });
    promoPrev.addEventListener("click", () => {
        promoList.scrollBy({left: -200, behavior: "smooth"});
    });
}

// --- Xử lý thay đổi phiên bản và cập nhật giá ---
document.addEventListener("DOMContentLoaded", function () {
    const versionButtons = document.querySelectorAll(".version-select .version");
    const currentPriceEl = document.querySelector(".current-price");
    const oldPriceEl = document.querySelector(".old-price");
    const discountEl = document.querySelector(".discount");

    // Dữ liệu giả định cho từng phiên bản
    const versionPrices = {
        "12GB": {
            current: 8490000, old: 9990000
        }, "8GB": {
            current: 7990000, old: 9490000
        }
    };

    versionButtons.forEach(button => {
        button.addEventListener("click", () => {
            // Bỏ active cũ và thêm active mới
            versionButtons.forEach(btn => btn.classList.remove("active"));
            button.classList.add("active");

            const versionName = button.textContent.trim();
            const priceData = versionPrices[versionName];

            if (priceData) {
                // Cập nhật giá
                currentPriceEl.textContent = priceData.current.toLocaleString("vi-VN") + "₫";
                oldPriceEl.textContent = priceData.old.toLocaleString("vi-VN") + "₫";

                // Tính phần trăm giảm
                const discountPercent = Math.round(100 - (priceData.current / priceData.old) * 100);
                discountEl.textContent = `-${discountPercent}%`;
            }
        });
    });
});
function updateGalleryEvents() {
    const listImgs = document.querySelectorAll('.list-image img');
    let currentIndexq = 0;

    function showImage(index) {
        listImgs.forEach(img => img.parentElement.classList.remove('active'));
        mainImg.src = listImgs[index].src;
        listImgs[index].parentElement.classList.add('active');
        currentIndexq = index;
    }

    // Gắn click cho thumbnail mới
    listImgs.forEach((img, index) => {
        img.addEventListener('click', () => showImage(index));
    });

    // Cập nhật lại hành vi prev/next
    prevBtn.onclick = () => {
        currentIndexq = (currentIndexq - 1 + listImgs.length) % listImgs.length;
        showImage(currentIndexq);
    };
    nextBtn.onclick = () => {
        currentIndexq = (currentIndexq + 1) % listImgs.length;
        showImage(currentIndexq);
    };

    // Hiển thị ảnh đầu tiên
    showImage(0);
}


//Thay đổi màu ảnh theo lựa chọn
document.addEventListener("DOMContentLoaded", function () {
    const colorItems = document.querySelectorAll(".color-item");
    const imgFeature = document.querySelector(".img-feature");
    const listImageContainer = document.querySelector(".list-image");
    // Dữ liệu ảnh theo màu
    const colorImages = {
        "Xanh biển": ["assert/img/product/iphone-15-xanh.jpg" ,"assert/img/product/iphone-15-blue-2.jpg", "assert/img/product/iphone-15-xanh.jpg" , "assert/img/product/iphone-15-2blue-camera.jpg","assert/img/product/iphone-15-blue-2.jpg"],
        "Bạc ánh sao": ["assert/img/product/iphone15.jpg", "assert/img/product/iphone15_behind.jpg", "assert/img/product/iphone15_after.jpg", "assert/img/product/iphone15_camera.jpg", "assert/img/product/iphone15.jpg",

        ],
        "Vàng mộng mơ": ["assert/img/product/iphone-15-plus-vang-126gb.jpg","assert/img/product/iphone-15-plus-yellow-2.jpg", "assert/img/product/iphone-15-plus-128gb-vang.jpg","assert/img/product/iphone-15-plus-vang-126gb.jpg","assert/img/product/iphone-15-plus-yellow-2.jpg"]
    };

    // Khi chọn màu
    colorItems.forEach(item => {
        item.addEventListener("click", () => {
            document.querySelector(".color-item.active")?.classList.remove("active");
            item.classList.add("active");

            const colorName = item.querySelector("span:last-child").textContent.trim();
            const images = colorImages[colorName];
            if (!images || images.length === 0) return;

            // Đổi ảnh chính (hiệu ứng mượt)
            imgFeature.classList.add("fade-out");
            setTimeout(() => {
                imgFeature.src = images[0];
                imgFeature.classList.remove("fade-out");
                imgFeature.classList.add("fade-in");
                setTimeout(() => imgFeature.classList.remove("fade-in"), 300);
            }, 200);

            // Cập nhật danh sách ảnh nhỏ
            listImageContainer.innerHTML = "";
            images.forEach(src => {
                const div = document.createElement("div");
                const img = document.createElement("img");
                img.src = src;
                div.appendChild(img);
                listImageContainer.appendChild(div);
            });

            //  Sau khi thay đổi màu, cập nhật lại listImgs và gắn sự kiện Prev/Next
            updateGalleryEvents();
        });
    });


    // Cho phép click ảnh nhỏ đổi ảnh chính
    document.addEventListener("click", e => {
        if (e.target.closest(".list-image img")) {
            imgFeature.src = e.target.src;
        }
    });
});
// ========  Cuộn xuống bảng thông số khi bấm "Thông số" ========
document.querySelector('.spec-link').addEventListener('click', function (e) {
    e.preventDefault();
    const specs = document.querySelector('.tech-specs');
    if (specs) {
        specs.scrollIntoView({behavior: 'smooth', block: 'start'});
    }
});

// ========  Cuộn xuống phần đánh giá khi bấm vào ngôi sao ========
const ratingElement = document.querySelector('.rating');
if (ratingElement) {
    ratingElement.addEventListener('click', () => {
        const reviewSection = document.querySelector('.review-section');
        if (reviewSection) {
            reviewSection.scrollIntoView({behavior: 'smooth', block: 'start'});
        }
    });
}

// ======== 2 Xử lý prev/next ảnh trong gallery ========
const mainImg = document.querySelector('.img-feature');
const listImgs = document.querySelectorAll('.list-image img');
const prevBtn = document.querySelector('.control.prev');
const nextBtn = document.querySelector('.control.next');

let currentIndex = 0;

// Cập nhật ảnh chính
function showImage(index) {
    listImgs.forEach(img => img.parentElement.classList.remove('active'));
    mainImg.src = listImgs[index].src;
    listImgs[index].parentElement.classList.add('active');
    currentIndex = index;
}

// Khi bấm vào thumbnail
listImgs.forEach((img, index) => {
    img.addEventListener('click', () => showImage(index));
});

// Khi bấm Prev / Next
prevBtn.addEventListener('click', () => {
    currentIndex = (currentIndex - 1 + listImgs.length) % listImgs.length;
    showImage(currentIndex);
});
nextBtn.addEventListener('click', () => {
    currentIndex = (currentIndex + 1) % listImgs.length;
    showImage(currentIndex);
});


// Xử lý chọn sao
const stars = document.querySelectorAll('.star');
stars.forEach(star => {
    star.addEventListener('click', () => {
        stars.forEach(s => s.classList.remove('active'));
        star.classList.add('active');
        let val = star.getAttribute('data-value');
        for (let i = 0; i < val; i++) {
            stars[i].classList.add('active');
        }
    });
});



