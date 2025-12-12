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

    const updateForm = document.getElementById("update-form");

    // Bật chế độ chỉnh sửa
    btnEdit.addEventListener("click", function () {
        firstNameInput.disabled = false;
        lastNameInput.disabled = false;
        emailInput.disabled = false;

        btnEdit.classList.add("hidden");
        btnLogout.classList.add("hidden");
        btnSave.classList.remove("hidden");
    });

    // Đăng xuất
    btnLogout.addEventListener("click", function () {
        window.location.href = context + "/logout";
    });

    // Submit form update
    updateForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        // Nếu disabled → FormData sẽ bỏ qua → bật lại
        firstNameInput.disabled = false;
        lastNameInput.disabled = false;
        emailInput.disabled = false;

        const fd = new FormData(updateForm);
        fd.append("action", "update-info");

        try {
            const res = await fetch(context + "/user/profile", {
                method: "POST",
                body: fd
            });

            if (!res.ok) throw new Error("HTTP " + res.status);

            const result = await res.json();

            if (result.success) {
                alert("Cập nhật thông tin thành công!");
                location.reload();
            } else {
                alert(result.message || "Cập nhật thất bại!");
            }

        } catch (error) {
            console.error("Lỗi khi cập nhật:", error);
            alert("Có lỗi xảy ra. Vui lòng thử lại.");
        }
    });

    // Upload avatar khi chọn ảnh
    avatarInput.addEventListener("change", async function () {
        const file = avatarInput.files[0];
        if (!file) return;

        const fd = new FormData();
        fd.append("action", "upload-avatar");
        fd.append("avatar", file);

        try {
            const res = await fetch(context + "/user/profile", {
                method: "POST",
                body: fd
            });

            if (!res.ok) throw new Error("HTTP " + res.status);

            const result = await res.json();

            if (result.success) {
                // thêm timestamp để tránh cache ảnh cũ
                avatarPreview.src = result.url + "?t=" + Date.now();
                alert("Upload ảnh đại diện thành công!");
            } else {
                alert(result.message || "Upload thất bại!");
            }

        } catch (error) {
            console.error("Lỗi khi upload avatar:", error);
            alert("Có lỗi xảy ra. Vui lòng thử lại.");
        }
    });

});
