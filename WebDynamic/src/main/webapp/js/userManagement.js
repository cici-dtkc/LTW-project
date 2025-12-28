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

    function exitEditMode(elements, updateText = false) {
        const { editBtn, saveBtn, cancelBtn, roleText, roleSelect, statusText, statusSelect } = elements;

        roleText.style.display = "inline-block";
        roleSelect.style.display = "none";

        statusText.style.display = "inline-block";
        statusSelect.style.display = "none";

        saveBtn.style.display = "none";
        cancelBtn.style.display = "none";
        editBtn.style.display = "inline-block";

        if (updateText) {
            roleText.textContent = roleSelect.value == "1" ? "Admin" : "User";
            statusText.textContent = statusSelect.value == "1" ? "Hoạt động" : "Tạm khóa";
        }
    }

    function resetToOldValues(elements, oldValues) {
        elements.roleSelect.value = oldValues.role;
        elements.statusSelect.value = oldValues.status;
    }

    function updateOldValues(oldValues, newValues) {
        oldValues.role = newValues.role;
        oldValues.status = newValues.status;
    }

    function setButtonLoading(btn, isLoading) {
        btn.style.opacity = isLoading ? "0.5" : "1";
        btn.style.pointerEvents = isLoading ? "none" : "auto";
    }

    // ================= SEARCH & FILTER (CẬP NHẬT URL) =================
    const searchInput = document.getElementById("search-input");
    const roleFilter = document.getElementById("role-filter");
    const statusFilter = document.getElementById("status-filter");
    const resetBtn = document.getElementById("reset-filter-btn");

    // Debounce function để tránh gọi quá nhiều lần
    let debounceTimer;
    function debounce(func, delay) {
        return function() {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(func, delay);
        };
    }

    function applyFilter() {
        const searchTerm = searchInput.value.trim();
        const roleValue = roleFilter.value;
        const statusValue = statusFilter.value;

        // Tạo URL mới với query parameters
        const params = new URLSearchParams();

        if (searchTerm) params.append('search', searchTerm);
        if (roleValue) params.append('role', roleValue);
        if (statusValue) params.append('status', statusValue);

        // Reload trang với filter mới
        const newUrl = window.location.pathname + (params.toString() ? '?' + params.toString() : '');
        window.location.href = newUrl;
    }

    // Gắn sự kiện với debounce cho search input
    if (searchInput) {
        searchInput.addEventListener("input", debounce(applyFilter, 500));
    }

    // Gắn sự kiện cho select filters
    if (roleFilter) {
        roleFilter.addEventListener("change", applyFilter);
    }
    if (statusFilter) {
        statusFilter.addEventListener("change", applyFilter);
    }

    // Reset filter
    if (resetBtn) {
        resetBtn.addEventListener("click", () => {
            window.location.href = window.location.pathname;
        });
    }

    // ================= ROW EDITING =================
    allRows.forEach(row => {
        // Bỏ qua row "no results"
        if (row.id === 'no-results-row') return;

        const id = row.dataset.id;

        const elements = {
            editBtn: row.querySelector(".edit-icon"),
            saveBtn: row.querySelector(".save-icon"),
            cancelBtn: row.querySelector(".cancel-icon"),
            roleText: row.querySelector(".role-text"),
            roleSelect: row.querySelector(".role-select"),
            statusText: row.querySelector(".status-text"),
            statusSelect: row.querySelector(".status-select")
        };

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

                updateOldValues(oldValues, {
                    role: elements.roleSelect.value,
                    status: elements.statusSelect.value
                });

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