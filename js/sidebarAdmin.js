
// Thực hiện tao tác logout -> thoát ra trang đăng nhập
document.addEventListener("DOMContentLoaded", function () {
    const iconLogout = document.querySelector('.fa-right-from-bracket');

    iconLogout.addEventListener("click", function (e) {
        e.preventDefault(); // Ngăn mặc định chuyển trang

        const confirmLogout = confirm("Bạn có chắc chắn muốn đăng xuất?");
        if (confirmLogout) {
            window.location.href = "../login.html";
        }
    });
});
