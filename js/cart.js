const cartBody = document.getElementById("cart-body");
const cartHeader = document.getElementById("cart-header");
const totalDisplay = document.getElementById("totalDisplay");
const selectAll = document.getElementById("selectAll");
const deleteSelected = document.getElementById("deleteSelected");

const products = [
    { id: 1, name: "Apple iPad Mini G2356", price: 1250000, image: "img/anh-mo-ta.jpg" },
    { id: 2, name: "MacBook Air M3", price: 29990000, image: "https://cdn.tgdd.vn/Products/Images/44/299444/macbook-air-13-inch-m3-bac-thumb-600x600.jpg" },
];

cartHeader.style.display = "none";  // ẩn header khi giỏ hàng trống

// Giả lập thêm sản phẩm
document.getElementById("addProduct").addEventListener("click", () => {
    // Xóa dòng "Không có sản phẩm" nếu có (lấy mới mỗi lần)
    const existingEmpty = document.getElementById("empty-row");
    if (existingEmpty) existingEmpty.remove();

    // Hiện header
    cartHeader.style.display = "table-header-group";

    // Duyệt mảng sản phẩm và thêm vào bảng
    products.forEach(p => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
      <td><input type="checkbox" class="select-item"></td>
      <td style="display:flex;align-items:center;gap:10px;">
        <img src="${p.image}" alt="${p.name}" style="width:80px;height:auto;">
        <span class="product-name">${p.name}</span>
      </td>
      <td>
        <div class="qty-control">
          <button class="minus">-</button>
          <span class="quantity">1</span>
          <button class="plus">+</button>
        </div>
      </td>
      <td class="price" data-base="${p.price}">${p.price.toLocaleString("vi-VN")}₫</td>
      <td><button class="round-black-btn delete">Xóa</button></td>
    `;
        cartBody.appendChild(tr);
    });

    // đảm bảo checkbox "chọn tất cả" reset
    selectAll.checked = false;
    updateTotal();
});

// Xử lý các nút trong bảng
cartBody.addEventListener("click", e => {
    const row = e.target.closest("tr");
    if (!row) return;

    // Xóa sản phẩm
    if (e.target.classList.contains("delete")) {
        row.remove();
        if (cartBody.querySelectorAll("tr").length === 0) showEmptyMessage();
        updateTotal();
        return;
    }

    // Nút + / -
    if (e.target.classList.contains("plus") || e.target.classList.contains("minus")) {
        const qtySpan = row.querySelector(".quantity");
        let qty = parseInt(qtySpan.textContent, 10);
        if (e.target.classList.contains("plus")) qty++;
        if (e.target.classList.contains("minus") && qty > 1) qty--;
        qtySpan.textContent = qty;

        const basePrice = getBasePrice(row); // từ data-base
        // bảo vệ nếu basePrice không hợp lệ
        if (isNaN(basePrice)) {
            // fallback: parse từ text trong ô price (loại bỏ ký tự không phải số)
            const raw = row.querySelector(".price").textContent.replace(/[^\d]/g, "");
            const unit = Math.max(1, Math.floor(Number(raw) / Math.max(1, qty)));
            updateRowTotal(row, unit, qty);
        } else {
            updateRowTotal(row, basePrice, qty);
        }
        updateTotal();
    }
});

// Checkbox chọn tất cả
selectAll.addEventListener("change", e => {
    document.querySelectorAll(".select-item").forEach(chk => chk.checked = e.target.checked);
    updateTotal();
});

// Checkbox từng sản phẩm (bắt sự kiện change bubbling)
cartBody.addEventListener("change", e => {
    if (e.target.classList.contains("select-item")) {
        // cập nhật trạng thái selectAll
        const all = document.querySelectorAll(".select-item");
        const checked = document.querySelectorAll(".select-item:checked");
        selectAll.checked = all.length > 0 && all.length === checked.length;
        updateTotal();
    }
});

// Nút xóa sản phẩm đã chọn
deleteSelected.addEventListener("click", () => {
    document.querySelectorAll(".select-item:checked").forEach(chk => chk.closest("tr").remove());
    if (cartBody.querySelectorAll("tr").length === 0) showEmptyMessage();
    updateTotal();
});

//  Khi giỏ hàng trống
function showEmptyMessage() {
    const tr = document.createElement("tr");
    cartHeader.style.display = "none";

    tr.id = "empty-row";
    tr.innerHTML = `<td colspan="5" class="empty-cart">
     <h1> Không có sản phẩm nào được thêm vào giỏ hàng</h1>
    </td>`;
    cartBody.appendChild(tr);
}

// Cập nhật tổng giá của từng dòng (dùng data-base)
function updateRowTotal(row, basePrice, qty) {
    const total = basePrice * qty;
    const priceCell = row.querySelector(".price");
    priceCell.textContent = total.toLocaleString("vi-VN") + "₫";
    // đảm bảo lưu data-base là số (lưu dưới dạng chuỗi)
    priceCell.dataset.base = String(basePrice);
}

// Lấy giá gốc (chưa nhân với số lượng) từ data attribute
function getBasePrice(row) {
    const priceCell = row.querySelector(".price");
    return Number(priceCell.dataset.base);
}

// Cập nhật tổng cả giỏ
function updateTotal() {
    let total = 0;
    document.querySelectorAll(".select-item:checked").forEach(chk => {
        const row = chk.closest("tr");
        const base = Number(row.querySelector(".price").dataset.base);
        const qty = Number(row.querySelector(".quantity").textContent);
        if (!isNaN(base)) {
            total += base * qty;
        } else {
            // fallback: lấy trực tiếp từ ô price (nếu data-base không tồn tại)
            const raw = row.querySelector(".price").textContent.replace(/[^\d]/g, "");
            total += Number(raw);
        }
    });
    totalDisplay.textContent = "Tổng: " + total.toLocaleString("vi-VN") + "₫";
}
