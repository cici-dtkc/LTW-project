

// DOM elements
const searchInput = document.getElementById("search-input");
const roleFilter = document.getElementById("role-filter");
const statusFilter = document.getElementById("status-filter");
const userTableBody = document.getElementById("user-table-body");
const recordInfo = document.getElementById("record-info");
const pagination = document.getElementById("pagination-number");
const addUserBtn = document.getElementById("add-user-btn");



// ==============================
//  Inline Edit
// ==============================
userTableBody.addEventListener("click", (e) => {
    const target = e.target;
    const row = target.closest("tr");
    if (!row) return;

    const roleCell = row.querySelector(".role-cell");
    const statusCell = row.querySelector(".status-cell");
    const editBtn = row.querySelector(".edit-icon");
    const saveBtn = row.querySelector(".save-icon");
    const cancelBtn = row.querySelector(".cancel-icon");

    // Click Edit
    if (target.classList.contains("edit-icon")) {
        const currentRole = roleCell.textContent.trim();
        const currentStatus = statusCell.textContent.trim();

        // Lưu giá trị ban đầu vào data attributes
        roleCell.dataset.originalRole = currentRole;
        statusCell.dataset.originalStatus = currentStatus;

        roleCell.innerHTML = `
            <select id="edit-role">
                <option ${currentRole === "Admin" ? "selected" : ""}>Admin</option>
                <option ${currentRole === "User" ? "selected" : ""}>User</option>
            </select>`;

        statusCell.innerHTML = `
            <select id="edit-status">
                <option ${currentStatus === "Hoạt động" ? "selected" : ""}>Hoạt động</option>
                <option ${currentStatus === "Tạm khóa" ? "selected" : ""}>Tạm khóa</option>
            </select>`;

        editBtn.style.display = "none";
        saveBtn.style.display = "inline";
        cancelBtn.style.display = "inline";
    }

    // Click Save
    if (target.classList.contains("save-icon")) {
        const newRole = row.querySelector("#edit-role").value;
        const newStatus = row.querySelector("#edit-status").value;

        roleCell.textContent = newRole;
        statusCell.textContent = newStatus;

        editBtn.style.display = "inline";
        saveBtn.style.display = "none";
        cancelBtn.style.display = "none";
    }

    // Click Cancel
    if (target.classList.contains("cancel-icon")) {
        // Lấy giá trị ban đầu từ data attributes hoặc từ HTML
        const originalRole = roleCell.dataset.originalRole || roleCell.textContent;
        const originalStatus = statusCell.dataset.originalStatus || statusCell.textContent;

        roleCell.textContent = originalRole;
        statusCell.textContent = originalStatus;

        editBtn.style.display = "inline";
        saveBtn.style.display = "none";
        cancelBtn.style.display = "none";
    }
});

