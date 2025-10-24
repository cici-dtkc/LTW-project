// ===== Modal Handling =====
const modal = document.getElementById("promoModal");
const btnOpen = document.getElementById("btnOpenModal");
const btnClose = document.getElementById("btnCloseModal");
const promoForm = document.getElementById("promoForm");

btnOpen.onclick = () => modal.style.display = "flex";
btnClose.onclick = () => modal.style.display = "none";
window.onclick = (e) => { if (e.target === modal) modal.style.display = "none"; };

// ===== Thêm khuyến mãi =====
promoForm.addEventListener("submit", (e) => {
    e.preventDefault();

    const code = document.getElementById("promoCode").value.trim();
    const name = document.getElementById("promoName").value.trim();
    const discount = document.getElementById("promoDiscount").value;
    const start = document.getElementById("promoStart").value;
    const end = document.getElementById("promoEnd").value;

    if (!code || !name || !discount || !start || !end) return showToast("Vui lòng nhập đầy đủ thông tin!");

    const tbody = document.getElementById("promoBody");
    const row = document.createElement("tr");
    row.innerHTML = `
                    <td>${code}</td>
                    <td>${name}</td>
                    <td>${discount}</td>
                    <td>${start}</td>
                    <td>${end}</td>
                    <td><span class="status active">Đang áp dụng</span></td>
                    <td>
                        <button class="btn-toggle">Tắt</button>
                        <button class="btn-edit">Sửa</button>
                        <button class="btn-delete">Xóa</button>
                    </td>`;
    tbody.appendChild(row);
    modal.style.display = "none";
    promoForm.reset();
    showToast("Đã thêm khuyến mãi mới!");
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
    if (btn.classList.contains("btn-delete")) {
        row.remove();
        showToast("Đã xóa khuyến mãi!");
    }else if (btn.classList.contains("btn-edit")) {
        const code = row.cells[0].textContent;
        const name = row.cells[1].textContent;
        const discount = row.cells[2].textContent;
        const start = row.cells[3].textContent;
        const end = row.cells[4].textContent;
        document.getElementById("promoCode").value = code;
        document.getElementById("promoName").value = name;
        document.getElementById("promoDiscount").value = discount;
        document.getElementById("promoStart").value = start;
        document.getElementById("promoEnd").value = end;
        modal.style.display = "flex";
        row.remove();
        showToast("Chỉnh sửa thông tin khuyến mãi!");
    }else if (btn.classList.contains("btn-toggle")) {
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
