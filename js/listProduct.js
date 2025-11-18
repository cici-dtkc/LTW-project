// Khi click vào nút lọc (Giá, Bộ nhớ, Màu sắc, Năm ra mắt)
document.querySelectorAll('.filter-item').forEach(btn => {
    btn.addEventListener('click', () => {
        const id = btn.getAttribute('data-filter');
        const dropdown = document.getElementById(id);

        // Bỏ active ở tất cả filter-item và dropdown khác
        document.querySelectorAll('.filter-item').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.dropdown').forEach(d => d.classList.remove('active'));

        // Thêm active cho filter hiện tại
        btn.classList.add('active');
        dropdown.classList.add('active');
        // 🔹 Căn vị trí trái cho dropdown khớp với nút lọc
        const filterBarRect = document.querySelector('.filter-options').getBoundingClientRect();
        const btnRect = btn.getBoundingClientRect();

        // Tính khoảng cách từ đầu thanh filter đến nút
        const leftOffset = btnRect.left - filterBarRect.left;

        dropdown.style.left = `${leftOffset}px`;
    });
});

// Khi bấm "Đóng" hoặc "Xem kết quả" → tắt dropdown và bỏ active ở filter
document.querySelectorAll('.btn-close, .btn-apply').forEach(btn => {
    btn.addEventListener('click', () => {
        const dropdown = btn.closest('.dropdown');
        dropdown.classList.remove('active');

        // Bỏ active ở nút filter tương ứng
        const id = dropdown.getAttribute('id');
        document.querySelector(`.filter-item[data-filter="${id}"]`)?.classList.remove('active');
    });
});

// Khi click ra ngoài → tắt tất cả dropdown và bỏ active filter
window.addEventListener('click', e => {
    if (!e.target.closest('.filter-item') && !e.target.closest('.dropdown')) {
        document.querySelectorAll('.dropdown').forEach(d => d.classList.remove('active'));
        document.querySelectorAll('.filter-item').forEach(b => b.classList.remove('active'));
    }
});

// Toggle chọn trong các button option (bộ nhớ, màu sắc, năm, ...)
document.querySelectorAll('.option-group button').forEach(btn => {
    btn.addEventListener('click', () => {
        btn.classList.toggle('active');
    });
});

// Khi chọn thương hiệu → có hiệu ứng active
document.querySelectorAll('.brand').forEach(brand => {
    brand.addEventListener('click', () => {
        document.querySelectorAll('.brand').forEach(b => b.classList.remove('active'));
        brand.classList.add('active');
    });
});
document.querySelectorAll('.category').forEach(category => {
    category.addEventListener('click', () => {
        document.querySelectorAll('.category').forEach(b => b.classList.remove('active'));
        category.classList.add('active');
    });
});

// ========================
// Xử lý menu sắp xếp
// ========================
document.querySelectorAll("#sortList li").forEach(item => {
    item.addEventListener("click", () => {
        document.querySelectorAll("#sortList li").forEach(li => li.classList.remove("active"));
        item.classList.add("active");
    });
});

// ========================
//  Xử lý chọn màu & dung lượng
// ========================
function initProductCard(productCard) {
    initColorSelection(productCard);
    initCapacitySelection(productCard);
}

function initColorSelection(productCard) {
    const colors = productCard.querySelectorAll(".colors .color");
    colors.forEach(color => {
        color.addEventListener("click", () => {
            colors.forEach(c => c.classList.remove("active"));
            color.classList.add("active");
            console.log("Màu đã chọn:", color.style.background);
        });
    });
}

function initCapacitySelection(productCard) {
    const buttons = productCard.querySelectorAll(".capacity button");
    buttons.forEach(btn => {
        btn.addEventListener("click", () => {
            buttons.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
            console.log("Dung lượng đã chọn:", btn.textContent.trim());
        });
    });
}
document.querySelectorAll(".product-card").forEach(card => {
    const capacityButtons = card.querySelectorAll(".capacity button");

    capacityButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            // Xóa active cũ
            capacityButtons.forEach(b => b.classList.remove("active"));

            // Thêm active cho nút đang được bấm
            btn.classList.add("active");
        });
    });
});
document.addEventListener("DOMContentLoaded", () => {
    const productCards = Array.from(document.querySelectorAll("#product-list .product-card"));
    const loadMoreBtn = document.getElementById("loadMoreBtn");
    const spinner = document.getElementById("loadMoreSpinner");
    const pageSize = 8;
    let currentIndex = 0;

    // Hiển thị sản phẩm ban đầu (n lần đầu)
    function showNextProducts() {
        spinner.style.display = "inline";
        loadMoreBtn.disabled = true;

        setTimeout(() => {
            const nextProducts = productCards.slice(currentIndex, currentIndex + pageSize);
            nextProducts.forEach(card => card.style.display = "flex"); // hiển thị
            currentIndex += nextProducts.length;

            spinner.style.display = "none";
            loadMoreBtn.disabled = false;

            // Ẩn nút nếu hết sản phẩm
            if (currentIndex >= productCards.length) {
                loadMoreBtn.style.display = "none";
            }
        }, 300); // mô phỏng load
    }

    // Hiển thị lần đầu
    showNextProducts();

    // Event nút Xem thêm
    loadMoreBtn.addEventListener("click", showNextProducts);
});
// Lấy các nút giỏ hàng
const cartButtons = document.querySelectorAll('.cart-btn');
const cartBadge = document.getElementById('cart-badge');
let cartCount = parseInt(cartBadge.textContent) || 0;

// Hàm tạo hiệu ứng "bay vào giỏ hàng"
function flyToCart(productImg) {
    const imgClone = productImg.cloneNode(true);
    const rect = productImg.getBoundingClientRect();
    imgClone.style.position = 'fixed';
    imgClone.style.left = rect.left + 'px';
    imgClone.style.top = rect.top + 'px';
    imgClone.style.width = rect.width + 'px';
    imgClone.style.height = rect.height + 'px';
    imgClone.style.transition = 'all 0.8s ease-in-out';
    imgClone.style.zIndex = 1000;
    document.body.appendChild(imgClone);

    const cartIcon = document.getElementById('btn-cart');
    const cartRect = cartIcon.getBoundingClientRect();

    setTimeout(() => {
        imgClone.style.left = cartRect.left + 'px';
        imgClone.style.top = cartRect.top + 'px';
        imgClone.style.width = '30px';
        imgClone.style.height = '30px';
        imgClone.style.opacity = '0.5';
    }, 50);

    setTimeout(() => {
        document.body.removeChild(imgClone);
    }, 900);
}

// Gắn sự kiện click cho từng nút giỏ hàng
cartButtons.forEach(btn => {
    btn.addEventListener('click', (e) => {
        const productCard = e.target.closest('.product-card');
        const productImg = productCard.querySelector('.product-img img');

        // Hiệu ứng bay vào giỏ hàng
        flyToCart(productImg);

        // Cập nhật số lượng giỏ hàng
        cartCount++;
        cartBadge.textContent = cartCount;
    });
});
// Chọn tất cả ảnh sản phẩm
const productImages = document.querySelectorAll('.product-card .product-img img');

// Gắn sự kiện click
productImages.forEach(img => {
    img.addEventListener('click', () => {
        window.location.href = 'productDetail.html';
    });
});
