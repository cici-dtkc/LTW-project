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
        "12GB / 256GB": {
            current: 8490000, old: 9990000
        }, "8GB / 256GB": {
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


//Thay đổi màu ảnh theo lựa chọn
document.addEventListener("DOMContentLoaded", function () {
    const colorItems = document.querySelectorAll(".color-item");
    const imgFeature = document.querySelector(".img-feature");
    const listImageContainer = document.querySelector(".list-image");

    // Dữ liệu ảnh theo màu
    const colorImages = {
        "Xanh biển": ["assert/img/product/OppoReno13_blue.png", "assert/img/product/OppoReno13_blue.png", "assert/img/product/OppoReno13_blue.png"],
        "Bạc ánh sao": ["assert/img/product/iphone15.jpg", "assert/img/product/iphone15_behind.jpg", "assert/img/product/iphone15_after.jpg", "assert/img/product/iphone15_camera.jpg", "assert/img/product/iphone15.jpg",

        ],
        "Tím mộng mơ": ["assert/img/product/OppoReno13_purple.png", "assert/img/product/OppoReno13_purple.png", "assert/img/product/OppoReno13_purple.png"]
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
            reviewSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
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

// Khởi tạo ảnh đầu tiên
showImage(0);

// //Thêm vào giỏ hàng
// document.addEventListener("DOMContentLoaded", function () {
//     // Lấy tất cả các nút thêm vào giỏ hàng trên trang chi tiết
//     const cartButtons = document.querySelectorAll(".btn-cart, .cart-btn, .rb-cart-btn");
//
//     cartButtons.forEach(button => {
//         button.addEventListener("click", function (e) {
//             e.preventDefault();
//
//             // Lấy phần tử hiển thị số lượng giỏ hàng
//             let cartCount = document.getElementById("cart-count");
//             let count = parseInt(localStorage.getItem("cartCount")) || 0;
//             count++;
//
//             // Cập nhật giao diện và lưu vào localStorage
//             cartCount.textContent = count;
//             localStorage.setItem("cartCount", count);
//
//             // Thêm hiệu ứng nhỏ cho vui mắt
//             cartCount.classList.add("animate");
//             setTimeout(() => cartCount.classList.remove("animate"), 500);
//         });
//     });
// });
