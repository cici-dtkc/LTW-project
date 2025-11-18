document.addEventListener("DOMContentLoaded", function () {
    // 1. Lấy tất cả các nút thêm vào giỏ hàng
    const cartButtons = document.querySelectorAll(".btn-cart, .cart-btn");

    // 2. Lấy phần tử hiển thị số lượng giỏ hàng
    const cartCount = document.getElementById("cart-badge");
    let count = parseInt(cartCount.textContent) || 0;

    // 3. LẤY VỊ TRÍ ĐÍCH (Đã sửa lỗi selector)
    // Lấy phần tử có class .cart-item nằm trong #icon-box
    const cartIconContainer = document.querySelector('#icon-box .cart-item');

    if (!cartIconContainer) {
        console.error("Không tìm thấy phần tử giỏ hàng trên header (cart-item).");
        return;
    }

    // Tọa độ Giỏ hàng (đích đến)
    const cartIconRect = cartIconContainer.getBoundingClientRect();
    const targetX = cartIconRect.left + cartIconRect.width / 2;
    const targetY = cartIconRect.top + cartIconRect.height / 2;


    cartButtons.forEach(button => {
        button.addEventListener("click", function (e) {
            e.preventDefault();

            // --- Tìm Hình ảnh Sản phẩm (Linh hoạt) ---
            let productImage = null;

            if (button.classList.contains('btn-cart')) {
                // Trang chi tiết: Hình ảnh chính là .img-feature
                productImage = document.querySelector('.product-detail .img-feature');
            } else if (button.classList.contains('cart-btn')) {
                // Sản phẩm liên quan: Hình ảnh nằm trong .product-card > .product-img > img
                const productCard = button.closest('.product-card');
                if (productCard) {
                    productImage = productCard.querySelector('.product-img > img');
                }
            }


            // --- Bắt đầu hiệu ứng bay ---
            if (productImage) {
                const startRect = productImage.getBoundingClientRect();
                const startX = startRect.left;
                const startY = startRect.top;

                // A. Tạo bản sao (clone) của hình ảnh
                const flyingImage = productImage.cloneNode(true);
                flyingImage.classList.add('flying-image');
                flyingImage.style.width = `${startRect.width}px`;
                flyingImage.style.height = `${startRect.height}px`;

                // B. Thiết lập vị trí ban đầu (cần cộng thêm scrollY/scrollX vì element.top/left là tương đối với viewport)
                flyingImage.style.top = `${startY + window.scrollY}px`;
                flyingImage.style.left = `${startX + window.scrollX}px`;

                // C. Thêm vào body
                document.body.appendChild(flyingImage);

                // D. Tính toán khoảng cách di chuyển
                const startCenterX = startX + startRect.width / 2;
                const startCenterY = startY + startRect.height / 2;

                const offsetX = targetX - startCenterX;
                const offsetY = targetY - startCenterY;

                // E. Kích hoạt hiệu ứng (setTimeout ngắn cho phép trình duyệt render vị trí ban đầu)
                setTimeout(() => {
                    // Áp dụng di chuyển (translate) và thu nhỏ (scale)
                    flyingImage.style.transform = `translate(${offsetX}px, ${offsetY}px) scale(0.1)`;
                    flyingImage.style.opacity = '0';

                    // F. Tăng số lượng giỏ hàng sau khi hiệu ứng gần kết thúc
                    setTimeout(() => {
                        count++;
                        cartCount.textContent = count;

                        // Xóa phần tử bay khỏi DOM
                        flyingImage.remove();
                    }, 750); // 750ms < 800ms (transition)
                }, 50);
            } else {
                // Nếu không tìm thấy hình ảnh, chỉ tăng số lượng
                count++;
                cartCount.textContent = count;
            }
        });
    });
});