// --- info-user.js ---

document.addEventListener("DOMContentLoaded", function () {
    const btnLogout = document.getElementById("btn-logout");
    const btnEdit = document.getElementById("btn-edit");

    // Khi nhấn nút "Đăng xuất"
    btnLogout.addEventListener("click", function () {

            // Chuyển hướng sang trang đăng nhập
            window.location.href = "./login.html";

    });

    //  Khi nhấn "Chỉnh sửa thông tin"
    btnEdit.addEventListener("click", function () {
        alert("Tính năng chỉnh sửa thông tin đang được phát triển!");
    });
});
