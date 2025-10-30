document.querySelectorAll("#sortList li").forEach(item => {
    item.addEventListener("click", () => {
        document.querySelectorAll("#sortList li").forEach(li => li.classList.remove("active"));
        item.classList.add("active");
    });
});
// ========================
// Quản lý sản phẩm
// ========================

/**
 * Khởi tạo sự kiện chọn màu và dung lượng cho 1 product-card
 * @param {HTMLElement} productCard - phần tử thẻ sản phẩm
 */
function initProductCard(productCard) {
    initColorSelection(productCard);
    initCapacitySelection(productCard);
}

/**
 * Xử lý chọn màu
 * @param {HTMLElement} productCard
 */
function initColorSelection(productCard) {
    const colors = productCard.querySelectorAll('.colors .color');

    colors.forEach(color => {
        color.addEventListener('click', () => {
            colors.forEach(c => c.classList.remove('active'));
            color.classList.add('active');
            const selectedColor = color.style.background;
            console.log('Màu đã chọn:', selectedColor);
        });
    });
}

/**
 * Xử lý chọn dung lượng (RAM / bộ nhớ)
 * @param {HTMLElement} productCard
 */
function initCapacitySelection(productCard) {
    const buttons = productCard.querySelectorAll('.capacity button');

    buttons.forEach(btn => {
        btn.addEventListener('click', () => {
            buttons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            const selectedCapacity = btn.textContent.trim();
            console.log('Dung lượng đã chọn:', selectedCapacity);
        });
    });
}

/**
 * Gọi init cho tất cả product-card trên trang
 */
function initAllProductCards() {
    const productCards = document.querySelectorAll('.product-card');
    productCards.forEach(card => initProductCard(card));
}

// ========================
// Gọi khởi tạo khi DOM sẵn sàng
// ========================
document.addEventListener('DOMContentLoaded', initAllProductCards);
