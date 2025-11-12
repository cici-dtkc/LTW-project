const cartBody = document.getElementById("cart-body");
const cartHeader = document.getElementById("cart-header");
const subTotal = document.getElementById("sub-total");
const selectAll = document.getElementById("selectAll");
const deleteSelected = document.getElementById("deleteSelected");

// Khi load trang, nếu không có sản phẩm thì ẩn header
if (cartBody.querySelectorAll("tr").length === 0) {
    cartHeader.style.display = "none";
}

// ===============================
// Xử lý các nút trong bảng
// ===============================
cartBody.addEventListener("click", e => {
    const row = e.target.closest("tr");
    if (!row) return;

    // Xóa sản phẩm
    if (e.target.classList.contains("delete")) {
        row.remove();
        if (cartBody.querySelectorAll("tr").length === 0) showEmptyMessage();
        updateSubTotal();
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

        const basePrice = getBasePrice(row);
        if (isNaN(basePrice)) {
            const raw = row.querySelector(".price").textContent.replace(/[^\d]/g, "");
            const unit = Math.max(1, Math.floor(Number(raw) / Math.max(1, qty)));
            updateRowTotal(row, unit, qty);
        } else {
            updateRowTotal(row, basePrice, qty);
        }
        updateSubTotal();

    }
});

// ===============================
// Checkbox chọn tất cả
// ===============================
selectAll.addEventListener("change", e => {
    document.querySelectorAll(".select-item").forEach(chk => chk.checked = e.target.checked);
    updateSubTotal();
});

// ===============================
// Checkbox từng sản phẩm
// ===============================
cartBody.addEventListener("change", e => {
    if (e.target.classList.contains("select-item")) {
        const all = document.querySelectorAll(".select-item");
        const checked = document.querySelectorAll(".select-item:checked");
        selectAll.checked = all.length > 0 && all.length === checked.length;
        updateSubTotal();
    }
});

// ===============================
// Nút xóa các sản phẩm đã chọn
// ===============================
deleteSelected.addEventListener("click", () => {
    document.querySelectorAll(".select-item:checked").forEach(chk => chk.closest("tr").remove());
    if (cartBody.querySelectorAll("tr").length === 0) showEmptyMessage();
    updateSubTotal();
});

// ===============================
// Khi giỏ hàng trống
// ===============================
function showEmptyMessage() {
    const tr = document.createElement("tr");
    cartHeader.style.display = "none";
    tr.id = "empty-row";
    tr.innerHTML = `
    <td colspan="5" class="empty-cart">
      <h3>Không có sản phẩm nào được thêm vào giỏ hàng</h3>
    </td>`;
    cartBody.appendChild(tr);
}

// ===============================
// Cập nhật giá từng dòng
// ===============================
function updateRowTotal(row, basePrice, qty) {
    const total = basePrice * qty;
    const priceCell = row.querySelector(".price");
    priceCell.textContent = total.toLocaleString("vi-VN") + "₫";
    priceCell.dataset.base = String(basePrice);
}

// ===============================
// Lấy giá gốc (unit price)
function getBasePrice(row) {
    const priceCell = row.querySelector(".price");
    return Number(priceCell.dataset.base);
}

// ===============================
// Cập nhật tổng phụ
function updateSubTotal() {
    let total = 0;
    document.querySelectorAll(".select-item:checked").forEach(chk => {
        const row = chk.closest("tr");
        const base = Number(row.querySelector(".price").dataset.base);
        const qty = Number(row.querySelector(".quantity").textContent);
        if (!isNaN(base)) {
            total += base * qty;
        } else {
            const raw = row.querySelector(".price").textContent.replace(/[^\d]/g, "");
            total += Number(raw);
        }
    });
    subTotal.textContent = total.toLocaleString("vi-VN") + "₫";
}


