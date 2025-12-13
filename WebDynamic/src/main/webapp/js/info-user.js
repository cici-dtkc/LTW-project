document.addEventListener("DOMContentLoaded", function () {

    // Lấy contextPath từ thẻ body
    const context = document.body.getAttribute("data-context-path") || "";

    const btnEdit = document.getElementById("btn-edit");
    const btnSave = document.getElementById("btn-save");
    const btnLogout = document.getElementById("btn-logout");

    const firstNameInput = document.getElementById("firstName");
    const lastNameInput = document.getElementById("lastName");
    const emailInput = document.getElementById("emailInput");

    const avatarInput = document.getElementById("avatarInput");
    const avatarPreview = document.getElementById("user-avatar");
    const sidebarAvatar = document.getElementById("userAvatar");
    const menuAccountMain = document.getElementById("menuAccountMain");
    const accountSubmenu = document.getElementById("accountSubmenu");
    const updateForm = document.getElementById("update-form");

    // HÀM HIỂN THỊ TOAST NOTIFICATION
    function showToast(message, type = 'success') {
        // Tạo toast element
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.innerHTML = `
            <div class="toast-icon">
                ${type === 'success' ? '✓' : '✕'}
            </div>
            <div class="toast-message">${message}</div>
        `;

        // Thêm vào body
        document.body.appendChild(toast);

        // Hiển thị toast
        setTimeout(() => toast.classList.add('show'), 100);

        // Tự động ẩn sau 3 giây
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }

    // Bật chế độ chỉnh sửa
    if (btnEdit) {
        btnEdit.addEventListener("click", function () {
            firstNameInput.disabled = false;
            lastNameInput.disabled = false;
            emailInput.disabled = false;

            btnEdit.classList.add("hidden");
            btnLogout.classList.add("hidden");
            btnSave.classList.remove("hidden");
        });
    }

    // Đăng xuất
    if (btnLogout) {
        btnLogout.addEventListener("click", function () {
            window.location.href = context + "/logout";
        });
    }

    // Submit form update
    if (updateForm) {
        updateForm.addEventListener("submit", async function (e) {
            e.preventDefault();

            // Nếu disabled → FormData sẽ bỏ qua → bật lại
            firstNameInput.disabled = false;
            lastNameInput.disabled = false;
            emailInput.disabled = false;

            const fd = new FormData(updateForm);
            fd.append("action", "update-info");

            // Hiển thị loading
            btnSave.disabled = true;
            btnSave.textContent = "Đang lưu...";

            try {
                const res = await fetch(context + "/user/profile", {
                    method: "POST",
                    body: fd
                });

                if (!res.ok) throw new Error("HTTP " + res.status);

                const result = await res.json();

                if (result.success) {
                    showToast("Cập nhật thông tin thành công!", "success");
                    setTimeout(() => location.reload(), 1500);
                } else {
                    showToast(result.message || "Cập nhật thất bại!", "error");
                    btnSave.disabled = false;
                    btnSave.textContent = "Lưu thay đổi";
                }

            } catch (error) {
                console.error("Lỗi khi cập nhật:", error);
                showToast("Có lỗi xảy ra. Vui lòng thử lại.", "error");
                btnSave.disabled = false;
                btnSave.textContent = "Lưu thay đổi";
            }
        });
    }

    // Upload avatar khi chọn ảnh
    if (avatarInput) {
        avatarInput.addEventListener("change", async function () {
            const file = avatarInput.files[0];
            if (!file) return;

            // Kiểm tra kích thước file (tối đa 5MB)
            if (file.size > 5 * 1024 * 1024) {
                showToast("Kích thước ảnh không được vượt quá 5MB", "error");
                avatarInput.value = "";
                return;
            }

            // Kiểm tra định dạng file
            if (!file.type.match(/image\/(png|jpeg|jpg|gif)/)) {
                showToast("Chỉ chấp nhận file ảnh (PNG, JPG, JPEG, GIF)", "error");
                avatarInput.value = "";
                return;
            }

            const fd = new FormData();
            fd.append("action", "upload-avatar");
            fd.append("avatar", file);

            // Hiển thị loading trên avatar
            if (avatarPreview) {
                avatarPreview.style.opacity = "0.5";
            }

            try {
                const res = await fetch(context + "/user/profile", {
                    method: "POST",
                    body: fd
                });

                if (!res.ok) throw new Error("HTTP " + res.status);

                const result = await res.json();

                if (result.success) {
                    // Thêm timestamp để tránh cache ảnh cũ
                    const newAvatarUrl = result.url + "?t=" + Date.now();

                    // Cập nhật avatar ở form chính
                    if (avatarPreview) {
                        avatarPreview.src = newAvatarUrl;
                        avatarPreview.style.opacity = "1";
                    }

                    // Cập nhật avatar ở sidebar
                    if (sidebarAvatar) {
                        sidebarAvatar.src = newAvatarUrl;
                    }

                    showToast("Upload ảnh đại diện thành công!", "success");
                } else {
                    if (avatarPreview) {
                        avatarPreview.style.opacity = "1";
                    }
                    showToast(result.message || "Upload thất bại!", "error");
                }

            } catch (error) {
                console.error("Lỗi khi upload avatar:", error);
                if (avatarPreview) {
                    avatarPreview.style.opacity = "1";
                }
                showToast("Có lỗi xảy ra. Vui lòng thử lại.", "error");
            }

            // Reset input để có thể chọn lại cùng một file
            avatarInput.value = "";
        });
    }

    // SIDEBAR submenu toggle
    if (menuAccountMain && accountSubmenu) {
        accountSubmenu.classList.add("open");
        menuAccountMain.addEventListener("click", (e) => {
            e.preventDefault();
            accountSubmenu.classList.toggle("open");
        });
    }

});