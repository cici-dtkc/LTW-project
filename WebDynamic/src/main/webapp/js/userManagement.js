document.addEventListener("DOMContentLoaded", () => {

    // ================= CONFIGURATION =================
    function getContextPath() {
        return "/" + window.location.pathname.split("/")[1];
    }

    const apiUrl = getContextPath() + "/admin/users";
    const allRows = Array.from(document.querySelectorAll("#user-table-body tr"));

    // ================= TOAST NOTIFICATION =================
    function showToast(message, type = 'success') {
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.innerHTML = `
            <div class="toast-icon">
                ${type === 'success' ? '✓' : '✕'}
            </div>
            <div class="toast-message">${message}</div>
        `;

        document.body.appendChild(toast);
        setTimeout(() => toast.classList.add('show'), 100);

        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }

    // ================= UI CONTROL FUNCTIONS =================

    // Chuyển sang chế độ edit
    function enterEditMode(elements) {
        const { editBtn, saveBtn, cancelBtn, roleText, roleSelect, statusText, statusSelect } = elements;

        editBtn.style.display = "none";
        saveBtn.style.display = "inline-block";
        cancelBtn.style.display = "inline-block";

        roleText.style.display = "none";
        roleSelect.style.display = "inline-block";

        statusText.style.display = "none";
        statusSelect.style.display = "inline-block";
    }

    // Thoát chế độ edit (về chế độ view)
    function exitEditMode(elements, updateText = false) {
        const { editBtn, saveBtn, cancelBtn, roleText, roleSelect, statusText, statusSelect } = elements;

        roleText.style.display = "inline-block";
        roleSelect.style.display = "none";

        statusText.style.display = "inline-block";
        statusSelect.style.display = "none";

        saveBtn.style.display = "none";
        cancelBtn.style.display = "none";
        editBtn.style.display = "inline-block";

        // Cập nhật text nếu cần (sau khi save thành công)
        if (updateText) {
            roleText.textContent = roleSelect.value == "1" ? "Admin" : "User";
            statusText.textContent = statusSelect.value == "1" ? "Hoạt động" : "Tạm khóa";
        }
    }

    // Reset về giá trị cũ
    function resetToOldValues(elements, oldValues) {
        elements.roleSelect.value = oldValues.role;
        elements.statusSelect.value = oldValues.status;
    }

    // Cập nhật giá trị cũ
    function updateOldValues(oldValues, newValues) {
        oldValues.role = newValues.role;
        oldValues.status = newValues.status;
    }

    // Set loading state cho save button
    function setButtonLoading(btn, isLoading) {
        btn.style.opacity = isLoading ? "0.5" : "1";
        btn.style.pointerEvents = isLoading ? "none" : "auto";
    }

    // ================= SEARCH & FILTER =================
    const searchInput = document.getElementById("search-input");
    const roleFilter = document.getElementById("role-filter");
    const statusFilter = document.getElementById("status-filter");

    function filterTable() {
        const searchTerm = searchInput.value.toLowerCase().trim();
        const roleValue = roleFilter.value;
        const statusValue = statusFilter.value;

        let visibleCount = 0;

        allRows.forEach(row => {
            const id = row.querySelector(".id-cell").textContent.toLowerCase();
            const username = row.querySelectorAll("td")[2].textContent.toLowerCase();
            const roleText = row.querySelector(".role-text").textContent;
            const statusText = row.querySelector(".status-text").textContent;

            const matchesSearch = id.includes(searchTerm) || username.includes(searchTerm);
            const matchesRole = !roleValue || roleText === roleValue;
            const matchesStatus = !statusValue || statusText === statusValue;

            if (matchesSearch && matchesRole && matchesStatus) {
                row.style.display = "";
                visibleCount++;
            } else {
                row.style.display = "none";
            }
        });

        updateNoResultsMessage(visibleCount);
    }

    function updateNoResultsMessage(count) {
        let noResultsRow = document.getElementById("no-results-row");

        if (count === 0) {
            if (!noResultsRow) {
                noResultsRow = document.createElement("tr");
                noResultsRow.id = "no-results-row";
                noResultsRow.innerHTML = `
                    <td colspan="6" style="text-align: center; padding: 40px; color: #666;">
                        <i class="fa-solid fa-search" style="font-size: 48px; color: #ddd; margin-bottom: 16px;"></i>
                        <p style="margin: 0; font-size: 16px;">Không tìm thấy người dùng nào</p>
                    </td>
                `;
                document.getElementById("user-table-body").appendChild(noResultsRow);
            }
        } else {
            if (noResultsRow) {
                noResultsRow.remove();
            }
        }
    }

    // Gắn sự kiện filter
    if (searchInput) searchInput.addEventListener("input", filterTable);
    if (roleFilter) roleFilter.addEventListener("change", filterTable);
    if (statusFilter) statusFilter.addEventListener("change", filterTable);

    // ================= ROW EDITING =================
    allRows.forEach(row => {
        const id = row.dataset.id;

        // Lấy tất cả elements cần thiết
        const elements = {
            editBtn: row.querySelector(".edit-icon"),
            saveBtn: row.querySelector(".save-icon"),
            cancelBtn: row.querySelector(".cancel-icon"),
            roleText: row.querySelector(".role-text"),
            roleSelect: row.querySelector(".role-select"),
            statusText: row.querySelector(".status-text"),
            statusSelect: row.querySelector(".status-select")
        };

        // Lưu giá trị gốc
        const oldValues = {
            role: elements.roleSelect.value,
            status: elements.statusSelect.value
        };

        // ================= EVENT: EDIT =================
        elements.editBtn.addEventListener("click", () => {
            enterEditMode(elements);
        });

        // ================= EVENT: CANCEL =================
        elements.cancelBtn.addEventListener("click", () => {
            resetToOldValues(elements, oldValues);
            exitEditMode(elements);
        });

        // ================= EVENT: SAVE =================
        elements.saveBtn.addEventListener("click", async () => {
            const formData = new URLSearchParams();
            formData.append("id", id);
            formData.append("role", elements.roleSelect.value);
            formData.append("status", elements.statusSelect.value);

            setButtonLoading(elements.saveBtn, true);

            try {
                const response = await fetch(apiUrl, {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: formData
                });

                const result = await response.json();

                setButtonLoading(elements.saveBtn, false);

                if (!result.success) {
                    showToast("Cập nhật thất bại", "error");
                    return;
                }

                // Cập nhật giá trị cũ
                updateOldValues(oldValues, {
                    role: elements.roleSelect.value,
                    status: elements.statusSelect.value
                });

                // Thoát edit mode và cập nhật text
                exitEditMode(elements, true);

                showToast("Cập nhật người dùng thành công", "success");

            } catch (error) {
                console.error("Error:", error);
                setButtonLoading(elements.saveBtn, false);
                showToast("Lỗi kết nối khi cập nhật người dùng", "error");
            }
        });
    });

});