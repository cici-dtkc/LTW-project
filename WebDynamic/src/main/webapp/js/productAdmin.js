function toggleProduct(productId, btn) {
    const icon = btn.querySelector('i');

    if (icon.classList.contains('fa-eye')) {
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
        btn.setAttribute('data-tooltip', 'Hiển thị sản phẩm');
        console.log('Ẩn sản phẩm ID:', productId);
    } else {
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
        btn.setAttribute('data-tooltip', 'Ẩn sản phẩm');
        console.log('Hiển thị sản phẩm ID:', productId);
    }
}
