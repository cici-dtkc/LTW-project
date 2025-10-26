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

// Mock data (sau này thay bằng dữ liệu từ database)
let users = [
    {
        id: 1,
        avatar: "./assert/img/admin.jpg",
        name: "Đỗ Thị",
        email: "chi@example.com",
        phone: "0987xxxxxx",
        role: "Admin",
        status: "Hoạt động",
    },
    {
        id: 2,
        avatar: "./assert/img/admin.jpg",
        name: "Hé Mầm",
        email: "mam@example.com",
        phone: "0997xxxxxx",
        role: "Admin",
        status: "Tạm khóa",
    },
    {
        id: 3,
        avatar: "./assert/img/admin.jpg",
        name: "Da Vân",
        email: "van@example.com",
        phone: "0987xxxxxx",
        role: "User",
        status: "Hoạt động",
    },
];

// ==============================
//  Render danh sách người dùng
// ==============================
function renderUsers(list) {
    userTableBody.innerHTML = "";

    if (!list || list.length === 0) {
        userTableBody.innerHTML = `<tr><td colspan="8" style="text-align:center; color:#888;">Không có người dùng nào</td></tr>`;
        recordInfo.textContent = "Hiển thị 0 người dùng";
        return;
    }

    list.forEach((user, index) => {
        const row = document.createElement("tr");
        row.dataset.id = user.id; // lưu id để dùng khi sửa/xóa
        row.innerHTML = `
            <td>${index + 1}</td>
            <td><img src="${user.avatar}" alt="avatar" style="width:32px; height:32px; border-radius:50%;"></td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.phone}</td>
            <td>${user.role}</td>
            <td>${user.status}</td>
            <td>
                <i class="fa-solid fa-pen edit-icon" style="color:var(--color-text-user); cursor:pointer;" title="Sửa"></i>
                &nbsp;
                <i class="fa-solid fa-trash delete-icon" style="color:#888888; cursor:pointer;" title="Xóa"></i>
            </td>
        `;
        userTableBody.appendChild(row);
    });

    recordInfo.textContent = `Hiển thị ${list.length} người dùng`;
}

// ==============================
//   Lọc và tìm kiếm
// ==============================
function applyFilters() {
    const searchValue = searchInput.value.toLowerCase();
    const selectedRole = roleFilter.value;
    const selectedStatus = statusFilter.value;

    let filtered = users.filter(user => {
        const matchSearch =
            user.name.toLowerCase().includes(searchValue) ||
            user.email.toLowerCase().includes(searchValue) ||
            user.phone.toLowerCase().includes(searchValue);

        const matchRole = selectedRole === "" || user.role === selectedRole;
        const matchStatus = selectedStatus === "" || user.status === selectedStatus;

        return matchSearch && matchRole && matchStatus;
    });

    renderUsers(filtered);
}

// ==============================
//  Xử lý sự kiện icon sửa / xóa
// ==============================
userTableBody.addEventListener("click", (e) => {
    const target = e.target;
    const row = target.closest("tr");
    const userId = parseInt(row.dataset.id);

    // ---- Xóa người dùng ----
    if (target.classList.contains("delete-icon")) {
        const confirmDelete = confirm("Bạn có chắc muốn xóa người dùng này?");
        if (confirmDelete) {
            users = users.filter(user => user.id !== userId);
            renderUsers(users);
        }
    }

    // ---- Sửa người dùng ----
    if (target.classList.contains("edit-icon")) {
        const user = users.find(u => u.id === userId);
        if (!user) return;

        const newName = prompt("Nhập tên mới:", user.name);
        const newEmail = prompt("Nhập email mới:", user.email);
        const newPhone = prompt("Nhập số điện thoại mới:", user.phone);
        const newRole = prompt("Nhập vai trò (Admin/User):", user.role);
        const newStatus = prompt("Nhập trạng thái (Hoạt động/Tạm khóa):", user.status);

        if (newName && newEmail && newPhone && newRole && newStatus) {
            user.name = newName;
            user.email = newEmail;
            user.phone = newPhone;
            user.role = newRole;
            user.status = newStatus;
            renderUsers(users);
        }
    }
});

// ==============================
//  Xử lý sự kiện khác
// ==============================
searchInput.addEventListener("input", applyFilters);
roleFilter.addEventListener("change", applyFilters);
statusFilter.addEventListener("change", applyFilters);

addUserBtn.addEventListener("click", () => {
    alert("Tính năng thêm người dùng sẽ được kết nối với database sau!");
});

// ==============================
//  Khởi tạo ban đầu
// ==============================
document.addEventListener("DOMContentLoaded", () => {
    renderUsers(users);
});
