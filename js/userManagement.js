// ==============================
// Quản lý người dùng - JavaScript
// ==============================

// DOM elements
const searchInput = document.getElementById("search-input");
const roleFilter = document.getElementById("role-filter");
const statusFilter = document.getElementById("status-filter");
const userTableBody = document.getElementById("user-table-body");
const recordInfo = document.getElementById("record-info");
const pagination = document.getElementById("pagination-number");
const addUserBtn = document.getElementById("add-user-btn");

// Mock data
let users = [
    { id: 1, avatar: "../assert/img/admin.jpg", name: "Đỗ Thị", email: "chi@example.com", role: "Admin", status: "Hoạt động"},
    { id: 2, avatar: "../assert/img/admin.jpg", name: "Hé Mầm", email: "mam@example.com", role: "Admin", status: "Tạm khóa"},
    { id: 3, avatar: "../assert/img/admin.jpg", name: "Da Vân", email: "van@example.com", role: "User", status: "Hoạt động"},
];

// ==============================
//  Render danh sách người dùng
// ==============================
function renderUsers(list) {
    userTableBody.innerHTML = "";

    if (!list || list.length === 0) {
        userTableBody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:#888;">Không có người dùng nào</td></tr>`;
        recordInfo.textContent = "Hiển thị 0 người dùng";
        return;
    }

    list.forEach((user, index) => {
        const row = document.createElement("tr");
        row.dataset.id = user.id;
        row.innerHTML = `
            <td>${index + 1}</td>
            <td><img src="${user.avatar}" style="width:32px; height:32px; border-radius:50%;"></td>
            <td>${user.name}</td>
            <td>${user.email}</td>

            <td class="role-cell">${user.role}</td>
            <td class="status-cell">${user.status}</td>

            <td>
                <i class="fa-solid fa-pen edit-icon" style="cursor:pointer;"></i>
                <i class="fa-solid fa-floppy-disk save-icon" style="cursor:pointer; display:none;"></i>
                <i class="fa-solid fa-xmark cancel-icon" style="cursor:pointer; display:none;"></i>
            </td>
        `;
        userTableBody.appendChild(row);
    });

    recordInfo.textContent = `Hiển thị ${list.length} người dùng`;
}

// ==============================
// Lọc và tìm kiếm
// ==============================
function applyFilters() {
    const searchValue = searchInput.value.toLowerCase();
    const selectedRole = roleFilter.value;
    const selectedStatus = statusFilter.value;

    const filtered = users.filter(user =>
        (user.name.toLowerCase().includes(searchValue) ||
            user.email.toLowerCase().includes(searchValue)) &&
        (selectedRole === "" || user.role === selectedRole) &&
        (selectedStatus === "" || user.status === selectedStatus)
    );

    renderUsers(filtered);
}

// ==============================
//  Inline Edit
// ==============================
userTableBody.addEventListener("click", (e) => {
    const target = e.target;
    const row = target.closest("tr");
    if (!row) return;

    const userId = parseInt(row.dataset.id);
    const user = users.find(u => u.id === userId);

    const roleCell = row.querySelector(".role-cell");
    const statusCell = row.querySelector(".status-cell");
    const editBtn = row.querySelector(".edit-icon");
    const saveBtn = row.querySelector(".save-icon");
    const cancelBtn = row.querySelector(".cancel-icon");

    // Click Edit
    if (target.classList.contains("edit-icon")) {
        roleCell.innerHTML = `
            <select id="edit-role">
                <option ${user.role === "Admin" ? "selected" : ""}>Admin</option>
                <option ${user.role === "User" ? "selected" : ""}>User</option>
            </select>`;

        statusCell.innerHTML = `
            <select id="edit-status">
                <option ${user.status === "Hoạt động" ? "selected" : ""}>Hoạt động</option>
                <option ${user.status === "Tạm khóa" ? "selected" : ""}>Tạm khóa</option>
            </select>`;

        editBtn.style.display = "none";
        saveBtn.style.display = "inline";
        cancelBtn.style.display = "inline";
    }

    // Click Save
    if (target.classList.contains("save-icon")) {
        user.role = row.querySelector("#edit-role").value;
        user.status = row.querySelector("#edit-status").value;

        renderUsers(users);
    }

    // Click Cancel
    if (target.classList.contains("cancel-icon")) {
        renderUsers(users);
    }
});

// ==============================
// Events
// ==============================
searchInput.addEventListener("input", applyFilters);
roleFilter.addEventListener("change", applyFilters);
statusFilter.addEventListener("change", applyFilters);

document.addEventListener("DOMContentLoaded", () => renderUsers(users));
