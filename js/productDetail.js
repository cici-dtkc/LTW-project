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

// --- Xử lý thay đổi phiên bản và cập nhật giá ---
document.addEventListener("DOMContentLoaded", function () {
    const versionButtons = document.querySelectorAll(".version-select .version");
    const currentPriceEl = document.querySelector(".current-price");
    const oldPriceEl = document.querySelector(".old-price");
    const discountEl = document.querySelector(".discount");

    // Dữ liệu giả định cho từng phiên bản
    const versionPrices = {
        "12GB / 256GB": {
            current: 8490000,
            old: 9990000
        },
        "8GB / 256GB": {
            current: 7990000,
            old: 9490000
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
                const discountPercent = Math.round(
                    100 - (priceData.current / priceData.old) * 100
                );
                discountEl.textContent = `-${discountPercent}%`;
            }
        });
    });
});


//Thay đổi màu ảnh theo lựa chọn
document.addEventListener("DOMContentLoaded", function () {
    const colorItems = document.querySelectorAll(".color-item");
    const imgFeature = document.querySelector(".img-feature");
    const listImageContainer = document.querySelector(".list-image");

    // Dữ liệu ảnh theo màu
    const colorImages = {
        "Xanh biển": [
            "assert/img/product/OppoReno13_blue.png",
            "assert/img/product/OppoReno13_blue.png",
            "assert/img/product/OppoReno13_blue.png"
        ],
        "Bạc ánh sao": [
            "assert/img/product/iphone15.jpg",
            "assert/img/product/iphone15_behind.jpg",
            "assert/img/product/iphone15_after.jpg",
            "assert/img/product/iphone15_camera.jpg",
            "assert/img/product/iphone15.jpg",

        ],
        "Tím mộng mơ": [
            "assert/img/product/OppoReno13_purple.png",
            "assert/img/product/OppoReno13_purple.png",
            "assert/img/product/OppoReno13_purple.png"
        ]
    };

    // Khi chọn màu
    colorItems.forEach(item => {
        item.addEventListener("click", () => {
            // Xóa active cũ
            document.querySelector(".color-item.active")?.classList.remove("active");
            item.classList.add("active");

            // Lấy tên màu
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
        });
    });

    // Cho phép click ảnh nhỏ đổi ảnh chính
    document.addEventListener("click", e => {
        if (e.target.closest(".list-image img")) {
            imgFeature.src = e.target.src;
        }
    });
});

