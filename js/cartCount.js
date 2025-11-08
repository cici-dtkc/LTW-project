// Thêm vào giỏ hàng
document.addEventListener("DOMContentLoaded", function () {
    // Lấy tất cả các nút thêm vào giỏ hàng trên trang chi tiết
    const cartButtons = document.querySelectorAll(".btn-cart, .cart-btn, .rb-cart-btn");

    // Lấy phần tử hiển thị số lượng giỏ hàng
    const cartCount = document.getElementById("cart-badge");
    let count = parseInt(cartCount.textContent) || 0;

    cartButtons.forEach(button => {
        button.addEventListener("click", function (e) {
            e.preventDefault();
            count++;
            cartCount.textContent = count;
        });
    });
});
