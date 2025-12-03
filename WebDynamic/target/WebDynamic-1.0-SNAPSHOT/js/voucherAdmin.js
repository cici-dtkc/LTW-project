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
