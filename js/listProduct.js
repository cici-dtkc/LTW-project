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

function initAllProductCards() {
    document.querySelectorAll(".product-card").forEach(card => initProductCard(card));
}

// ========================
//  Giả lập dữ liệu sản phẩm (chỉ dùng cho load thêm)
// ========================
const allProducts = [
    { name: "Samsung S24 Ultra", price: "36.000.000₫", discount: "-8%", sold: "Đã bán 980" },
    { name: "Xiaomi 15 Ultra", price: "25.000.000₫", discount: "-10%", sold: "Đã bán 1.5k", image: "https://via.placeholder.com/300x200?text=SP3" },
    { name: "OPPO Find X7", price: "19.500.000₫", discount: "-5%", sold: "Đã bán 2k", image: "https://via.placeholder.com/300x200?text=SP4" },
    { name: "Vivo X100 Pro", price: "18.900.000₫", discount: "-7%", sold: "Đã bán 870", image: "https://via.placeholder.com/300x200?text=SP5" },
    { name: "Realme GT Neo 6", price: "12.000.000₫", discount: "-15%", sold: "Đã bán 1.1k", image: "https://via.placeholder.com/300x200?text=SP6" },
    { name: "Huawei P70", price: "20.000.000₫", discount: "-10%", sold: "Đã bán 670", image: "https://via.placeholder.com/300x200?text=SP7" },
    { name: "Pixel 9 Pro", price: "30.000.000₫", discount: "-6%", sold: "Đã bán 560", image: "https://via.placeholder.com/300x200?text=SP8" },
    { name: "Asus ROG 9", price: "35.000.000₫", discount: "-9%", sold: "Đã bán 320", image: "https://via.placeholder.com/300x200?text=SP9" },
    { name: "Nokia Magic Max", price: "15.000.000₫", discount: "-11%", sold: "Đã bán 1.9k", image: "https://via.placeholder.com/300x200?text=SP10" },
    { name: "Honor Magic6", price: "22.000.000₫", discount: "-4%", sold: "Đã bán 900", image: "https://via.placeholder.com/300x200?text=SP11" },
    { name: "OnePlus 13", price: "28.000.000₫", discount: "-7%", sold: "Đã bán 1.3k", image: "https://via.placeholder.com/300x200?text=SP12" }
];

// ========================
// Chức năng "Xem thêm" sản phẩm
// ========================
const productList = document.getElementById("product-list");
const loadMoreBtn = document.getElementById("loadMoreBtn");
const spinner = document.getElementById("loadMoreSpinner");

const pageSize = 8;
let currentIndex = 0;

/**
 * Render sản phẩm ra giao diện
 */
function renderProducts(items) {
    const frag = document.createDocumentFragment();
    items.forEach(p => {
        const div = document.createElement("div");
        div.className = "product-card";
        div.innerHTML = `
      <div class="product-img">
         <img src="${p.image || "assert/img/placeholder.png"}" alt="${p.name}">
        ${p.discount && p.discount.trim() !== ""
            ? `<span class="discount-badge">${p.discount}</span>`
            : ""}
      </div>

      <div class="product-info">
        <h3>${p.name}</h3>
        <div class="price">${p.price}</div>

        <div class="colors">
          <div class="color active" style="background:#eab308"></div>
          <div class="color" style="background:#f1f1f1"></div>
          <div class="color" style="background:#1f2937"></div>
        </div>

        <div class="capacity">
          <button class="active">256 GB</button>
          <button>512 GB</button>
          <button>1 TB</button>
          <button>2 TB</button>
        </div>

        <div class="rating-cart">
          <div class="rating">
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-regular fa-star"></i>
          </div>
        </div>

        <div class="bottom-info">
          <span class="sold-count">${p.sold}</span>
          <button class="cart-btn">
            <i class="fa-solid fa-cart-plus"></i>
          </button>
        </div>
      </div>
    `;
        frag.appendChild(div);
    });

    productList.appendChild(frag);
    initAllProductCards(); // khởi tạo click màu & dung lượng cho sản phẩm mới
}

/**
 * Khi nhấn "Xem thêm"
 */
function loadMore() {
    loadMoreBtn.disabled = true;
    spinner.style.display = "inline";

    setTimeout(() => {
        const nextProducts = allProducts.slice(currentIndex, currentIndex + pageSize);
        renderProducts(nextProducts);
        currentIndex += nextProducts.length;

        // Nếu hết sản phẩm → ẩn nút
        if (currentIndex >= allProducts.length) {
            loadMoreBtn.style.display = "none";
        } else {
            loadMoreBtn.disabled = false;
        }

        spinner.style.display = "none";
    }, 800); // mô phỏng thời gian load
}

// ========================
// Khởi động
// ========================
document.addEventListener("DOMContentLoaded", () => {
    initAllProductCards(); // khởi tạo các card có sẵn trong HTML
    loadMoreBtn.addEventListener("click", loadMore);
});
