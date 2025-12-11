document.addEventListener("DOMContentLoaded", () => {

    // Lấy contextPath động (VD: /WebDynamic_war)
    function getContextPath() {
        return "/" + window.location.pathname.split("/")[1];
    }

    const apiUrl = getContextPath() + "/admin/users";

    document.querySelectorAll("#user-table-body tr").forEach(row => {

        const id = row.dataset.id;

        const editBtn = row.querySelector(".edit-icon");
        const saveBtn = row.querySelector(".save-icon");
        const cancelBtn = row.querySelector(".cancel-icon");

        const roleText = row.querySelector(".role-text");
        const roleSelect = row.querySelector(".role-select");

        const statusText = row.querySelector(".status-text");
        const statusSelect = row.querySelector(".status-select");

        // Lưu giá trị gốc
        let oldRole = roleSelect.value;
        let oldStatus = statusSelect.value;

        // ================= EDIT =================
        editBtn.addEventListener("click", () => {
            editBtn.style.display = "none";
            saveBtn.style.display = "inline-block";
            cancelBtn.style.display = "inline-block";

            roleText.style.display = "none";
            roleSelect.style.display = "inline-block";

            statusText.style.display = "none";
            statusSelect.style.display = "inline-block";
        });

        // ================= CANCEL =================
        cancelBtn.addEventListener("click", () => {
            roleSelect.value = oldRole;
            statusSelect.value = oldStatus;

            roleText.style.display = "inline-block";
            roleSelect.style.display = "none";

            statusText.style.display = "inline-block";
            statusSelect.style.display = "none";

            saveBtn.style.display = "none";
            cancelBtn.style.display = "none";
            editBtn.style.display = "inline-block";
        });

        // ================= SAVE =================
        saveBtn.addEventListener("click", () => {

            const formData = new URLSearchParams();
            formData.append("action", "update");
            formData.append("id", id);
            formData.append("role", roleSelect.value);
            formData.append("status", statusSelect.value);

            fetch(apiUrl, {
                method: "POST",
                body: formData
            })
                .then(res => res.json())
                .then(result => {
                    if (!result.success) {
                        alert("Cập nhật thất bại");
                        return;
                    }

                    // Cập nhật UI
                    roleText.textContent = roleSelect.value == "1" ? "Admin" : "User";
                    statusText.textContent = statusSelect.value == "1" ? "Hoạt động" : "Tạm khóa";

                    oldRole = roleSelect.value;
                    oldStatus = statusSelect.value;

                    roleText.style.display = "inline-block";
                    roleSelect.style.display = "none";

                    statusText.style.display = "inline-block";
                    statusSelect.style.display = "none";

                    saveBtn.style.display = "none";
                    cancelBtn.style.display = "none";
                    editBtn.style.display = "inline-block";
                })
                .catch(() => {
                    alert("Lỗi kết nối khi cập nhật người dùng");
                });
        });

    });
});
