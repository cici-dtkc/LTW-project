function toggleVisibility(btn) {
    const wrapper = btn.parentElement; // div.tooltip
    const icon = btn.querySelector('i'); // icon bên trong button

    // Kiểm tra trạng thái hiện tại dựa vào icon
    if (icon.classList.contains('fa-eye')) {
        // Đang hiển thị, đổi thành ẩn
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash'); // đổi icon
        wrapper.setAttribute('data-tooltip', 'Hiện sản phẩm'); // đổi tooltip
        console.log("Sản phẩm đã ẩn"); // ở đây có thể gọi API ẩn sản phẩm
    } else {
        // Đang ẩn, đổi thành hiển thị
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye'); // đổi icon
        wrapper.setAttribute('data-tooltip', 'Ẩn sản phẩm'); // đổi tooltip
        console.log("Sản phẩm đã hiển thị"); // gọi API nếu cần
    }
}