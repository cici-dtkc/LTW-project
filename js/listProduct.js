// ========================
// 1️⃣ Xử lý menu sắp xếp
// ========================
document.querySelectorAll("#sortList li").forEach(item => {
    item.addEventListener("click", () => {
        document.querySelectorAll("#sortList li").forEach(li => li.classList.remove("active"));
        item.classList.add("active");
    });
});

// ========================
// 2️⃣ Xử lý chọn màu & dung lượng
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
// 3️⃣ Giả lập dữ liệu sản phẩm (chỉ dùng cho load thêm)
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
// 4️⃣ Chức năng "Xem thêm" sản phẩm
// ========================
const productList = document.getElementById("product-list");
const loadMoreBtn = document.getElementById("loadMoreBtn");
const spinner = document.getElementById("loadMoreSpinner");

const pageSize = 6;
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
// 5️⃣ Khởi động
// ========================
document.addEventListener("DOMContentLoaded", () => {
    initAllProductCards(); // khởi tạo các card có sẵn trong HTML
    loadMoreBtn.addEventListener("click", loadMore);
});
