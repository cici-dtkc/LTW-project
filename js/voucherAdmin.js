// ===== Modal Handling =====
const modal = document.getElementById("promoModal");
const btnOpen = document.getElementById("btnOpenModal");
const btnClose = document.getElementById("btnCloseModal");
const promoForm = document.getElementById("promoForm");
let editRow = null; // Biến lưu dòng đang được chỉnh sửa
document.addEventListener("DOMContentLoaded", function () {

    // 👉 Nếu có query ?addPromo=true từ dashboard → mở modal
    const params = new URLSearchParams(window.location.search);
    if (params.get("addPromo") === "true") {
        modal.style.display = "flex";
    }

    // ===== Nút mở modal trong chính trang này =====
    btnOpen.addEventListener("click", () => {
        modal.style.display = "flex";
    });

    // ===== Nút đóng modal =====
    btnClose.addEventListener("click", () => {
        modal.style.display = "none";
    });

    // ===== Đóng modal khi click ra ngoài =====
    window.addEventListener("click", (e) => {
        if (e.target === modal) modal.style.display = "none";
    });
});

// ===== Thêm khuyến mãi =====
promoForm.addEventListener("submit", (e) => {
    e.preventDefault();

    const code = document.getElementById("promoCode").value.trim();
    const name = document.getElementById("promoName").value.trim();
    const type = document.getElementById("promoType").value;
    const discount = document.getElementById("promoDiscount").value;
    const maxDiscount = document.getElementById("promoMaxDiscount").value;
    const minOrder = document.getElementById("promoMinOrder").value;
    const quantity = document.getElementById("promoQuantity").value;
    const start = document.getElementById("promoStart").value;
    const end = document.getElementById("promoEnd").value;

    if (!code || !name || !discount || !start || !end)
        return showToast("Vui lòng nhập đầy đủ thông tin!");

    const tbody = document.getElementById("promoBody");

    if (editRow) {
        // Cập nhật hàng hiện tại
        editRow.innerHTML = `
            <td>${code}</td>
            <td>${name}</td>
            <td>${getPromoTypeText(type)}</td>
            <td>${discount}</td>
            <td>${maxDiscount}</td>
            <td>${minOrder}</td>
            <td>${quantity}</td>
            <td>${start}</td>
            <td>${end}</td>
            <td><span class="status active">Đang áp dụng</span></td>
            <td>
                <button class="btn-toggle">Tắt</button>
                <button class="btn-edit">Sửa</button>
            </td>`;
        showToast("Đã cập nhật khuyến mãi!");
        editRow = null;
    } else {
        // Thêm mới
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${code}</td>
            <td>${name}</td>
            <td>${getPromoTypeText(type)}</td>
            <td>${discount}</td>
            <td>${maxDiscount}</td>
            <td>${minOrder}</td>
            <td>${quantity}</td>
            <td>${start}</td>
            <td>${end}</td>
            <td><span class="status active">Đang áp dụng</span></td>
            <td>
                <button class="btn-toggle">Tắt</button>
                <button class="btn-edit">Sửa</button>
            </td>`;
        tbody.appendChild(row);
        showToast("Đã thêm khuyến mãi mới!");
    }

    promoForm.reset();
    modal.style.display = "none";
});

// Hàm chuyển giá trị type sang chữ hiển thị
function getPromoTypeText(type) {
    switch (type) {
        case "percent": return "Phần trăm";
        case "amount": return "Tiền mặt";
        case "gift": return "Tặng quà";
        default: return "Khác";
    }
}

// ===== Toast Message =====
function showToast(message) {
    const toast = document.getElementById("toast");
    toast.textContent = message;
    toast.className = "show";
    setTimeout(() => toast.className = toast.className.replace("show", ""), 3000);
}

// ===== Xử lý bảng =====
document.getElementById("promoBody").addEventListener("click", (e) => {
    const btn = e.target;
    const row = btn.closest("tr");

    if (btn.classList.contains("btn-edit")) {
        editRow = row; // lưu hàng đang chỉnh sửa
        const cells = row.cells;

        document.getElementById("promoCode").value = cells[0].textContent;
        document.getElementById("promoName").value = cells[1].textContent;
        document.getElementById("promoType").value = getPromoTypeValue(cells[2].textContent);
        document.getElementById("promoDiscount").value = cells[3].textContent;
        document.getElementById("promoMaxDiscount").value = cells[4].textContent;
        document.getElementById("promoMinOrder").value = cells[5].textContent;
        document.getElementById("promoQuantity").value = cells[6].textContent;
        document.getElementById("promoStart").value = cells[7].textContent;
        document.getElementById("promoEnd").value = cells[8].textContent;

        modal.style.display = "flex";
        showToast("Chỉnh sửa thông tin khuyến mãi!");
    }
    else if (btn.classList.contains("btn-toggle")) {
        const statusSpan = row.querySelector(".status");
        if (statusSpan.classList.contains("active")) {
            statusSpan.classList.replace("active", "inactive");
            statusSpan.textContent = "Hết hạn";
            btn.textContent = "Bật";
            showToast("Đã tắt khuyến mãi!");
        } else {
            statusSpan.classList.replace("inactive", "active");
            statusSpan.textContent = "Đang áp dụng";
            btn.textContent = "Tắt";
            showToast("Đã bật khuyến mãi!");
        }
    }
});

// Hàm chuyển text thành value khi edit
function getPromoTypeValue(text) {
    switch (text) {
        case "Phần trăm": return "percent";
        case "Tiền mặt": return "amount";
        case "Tặng quà": return "gift";
        default: return "";
    }
}

// ===== Tìm kiếm & Lọc =====
const searchInput = document.getElementById("searchInput");
const filterStatus = document.getElementById("filterStatus");

function filterTable() {
    const keyword = searchInput.value.toLowerCase();
    const filter = filterStatus.value;
    const rows = document.querySelectorAll("#promoBody tr");

    rows.forEach(row => {
        const name = row.cells[1].textContent.toLowerCase();
        const status = row.querySelector(".status").classList.contains("active") ? "active" : "inactive";
        const matchesSearch = name.includes(keyword);
        const matchesFilter = (filter === "all") || (filter === status);
        row.style.display = (matchesSearch && matchesFilter) ? "" : "none";
    });
}

searchInput.addEventListener("input", filterTable);
filterStatus.addEventListener("change", filterTable);
