// ===== KHỞI TẠO KHI TRANG LOAD =====
document.addEventListener("DOMContentLoaded", () => {
    initFilterSystem();
    initSortSystem();
    initProductCards();
    initLoadMore();
    initCartSystem();
});

// ===== HỆ THỐNG BỘ LỌC =====
function initFilterSystem() {
    const filterItems = document.querySelectorAll('.filter-item');
    const dropdowns = document.querySelectorAll('.dropdown');

    // Khi click vào nút lọc
    filterItems.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            const id = btn.getAttribute('data-filter');
            const dropdown = document.getElementById(id);

            // Toggle active cho nút hiện tại
            const isActive = btn.classList.contains('active');
            closeAllFilters();

            if (!isActive) {
                btn.classList.add('active');
                dropdown.classList.add('active');

                // Căn vị trí dropdown khớp với nút lọc
                positionDropdown(btn, dropdown);
            }
        });
    });

    // Xử lý button trong option-group (toggle multiple selection)
    document.querySelectorAll('.option-group button').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.classList.toggle('active');
        });
    });

    // Nút đóng
    document.querySelectorAll('.btn-close').forEach(btn => {
        btn.addEventListener('click', () => {
            closeAllFilters();
        });
    });

    // Nút áp dụng
    document.querySelectorAll('.btn-apply').forEach(btn => {
        btn.addEventListener('click', () => {
            applyFilters();
            closeAllFilters();
        });
    });

    // Click bên ngoài để đóng
    window.addEventListener('click', (e) => {
        if (!e.target.closest('.filter-item') && !e.target.closest('.dropdown')) {
            closeAllFilters();
        }
    });

    // Xử lý chọn thương hiệu
    document.querySelectorAll('.brand').forEach(brand => {
        brand.addEventListener('click', () => {
            document.querySelectorAll('.brand').forEach(b => b.classList.remove('active'));
            brand.classList.add('active');
            // TODO: Lọc sản phẩm theo thương hiệu
        });
    });
}

function positionDropdown(btn, dropdown) {
    const filterBarRect = document.querySelector('.filter-options').getBoundingClientRect();
    const btnRect = btn.getBoundingClientRect();
    const leftOffset = btnRect.left - filterBarRect.left;
    dropdown.style.left = `${leftOffset}px`;
}

function closeAllFilters() {
    document.querySelectorAll('.dropdown').forEach(d => d.classList.remove('active'));
    document.querySelectorAll('.filter-item').forEach(b => b.classList.remove('active'));
}

function applyFilters() {
    // Thu thập các filter đã chọn
    const selectedFilters = {
        price: getPriceRange(),
        memory: getSelectedOptions('bonho'),
        color: getSelectedOptions('mausac'),
        year: getSelectedOptions('namramat')
    };

    console.log('Áp dụng bộ lọc:', selectedFilters);
    // TODO: Gọi API hoặc filter products trong DOM
}

function getPriceRange() {
    const priceInputs = document.querySelectorAll('#gia input[type="number"]');
    return {
        min: priceInputs[0]?.value || null,
        max: priceInputs[1]?.value || null
    };
}

function getSelectedOptions(filterId) {
    const activeButtons = document.querySelectorAll(`#${filterId} .option-group button.active`);
    return Array.from(activeButtons).map(btn => btn.textContent.trim());
}

// ===== HỆ THỐNG SẮP XẾP =====
function initSortSystem() {
    const sortItems = document.querySelectorAll("#sortList li");

    sortItems.forEach(item => {
        item.addEventListener("click", () => {
            sortItems.forEach(li => li.classList.remove("active"));
            item.classList.add("active");

            const sortType = item.textContent.trim();
            sortProducts(sortType);
        });
    });
}

function sortProducts(sortType) {
    const productList = document.getElementById('product-list');
    const products = Array.from(productList.querySelectorAll('.product-card'));

    products.sort((a, b) => {
        switch(sortType) {
            case 'Bán chạy':
                return getSoldCount(b) - getSoldCount(a);
            case 'Giảm giá':
                return getDiscount(b) - getDiscount(a);
            case 'Giá':
                // Toggle giá tăng/giảm
                const arrow = document.querySelector('#sortList li:last-child .arrow');
                if (arrow.classList.contains('asc')) {
                    arrow.classList.remove('asc');
                    return getPrice(b) - getPrice(a); // Giảm dần
                } else {
                    arrow.classList.add('asc');
                    return getPrice(a) - getPrice(b); // Tăng dần
                }
            case 'Mới':
                // Giả sử sản phẩm mới nhất ở đầu danh sách
                return 0;
            default: // Nổi bật
                return 0;
        }
    });

    // Re-render products
    products.forEach(product => productList.appendChild(product));
}

function getSoldCount(card) {
    const text = card.querySelector('.sold-count')?.textContent || '0';
    return parseInt(text.replace(/\D/g, '')) || 0;
}

function getDiscount(card) {
    const text = card.querySelector('.discount-badge')?.textContent || '0';
    return parseInt(text.replace(/\D/g, '')) || 0;
}

function getPrice(card) {
    const text = card.querySelector('.price-new')?.textContent || '0';
    return parseInt(text.replace(/\D/g, '')) || 0;
}

// ===== XỬ LÝ PRODUCT CARDS =====
function initProductCards() {
    const productCards = document.querySelectorAll('.product-card');

    productCards.forEach(card => {
        initCapacitySelection(card);
        initColorSelection(card);
    });
}

function initCapacitySelection(card) {
    const capacityButtons = card.querySelectorAll('.capacity button');

    capacityButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();

            // Remove active từ tất cả các nút
            capacityButtons.forEach(b => b.classList.remove('active'));

            // Thêm active vào nút được click
            btn.classList.add('active');

            // Lấy giá từ data attributes
            const newPrice = btn.getAttribute('data-price');
            const oldPrice = btn.getAttribute('data-old-price');

            // Cập nhật giá hiển thị
            if (newPrice) {
                updatePrice(card, newPrice, oldPrice);
            }

            console.log('Dung lượng đã chọn:', btn.textContent.trim());
        });
    });
}

function initColorSelection(card) {
    const colors = card.querySelectorAll('.colors .color');

    colors.forEach(color => {
        color.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();

            colors.forEach(c => c.classList.remove('active'));
            color.classList.add('active');

            console.log('Màu đã chọn:', color.getAttribute('data-color'));
        });
    });
}

function updatePrice(card, newPrice, oldPrice) {
    const priceNewElement = card.querySelector('.price-new');
    const priceOldElement = card.querySelector('.price-old');

    if (priceNewElement) {
        priceNewElement.textContent = formatPrice(newPrice) + '₫';
    }

    if (priceOldElement && oldPrice) {
        priceOldElement.textContent = formatPrice(oldPrice) + '₫';

        // Hiển thị/ẩn giá cũ dựa trên discount
        if (parseFloat(newPrice) < parseFloat(oldPrice)) {
            priceOldElement.style.display = 'inline';
        } else {
            priceOldElement.style.display = 'none';
        }
    }
}

// ===== HỆ THỐNG GIỎ HÀNG =====
function initCartSystem() {
    const cartButtons = document.querySelectorAll('.cart-btn');
    const cartBadge = document.getElementById('cart-badge');
    let cartCount = parseInt(cartBadge?.textContent) || 0;

    cartButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();

            const productCard = btn.closest('.product-card');
            const productImg = productCard.querySelector('.product-img img');
            const productName = productCard.querySelector('h2').textContent;
            const activeCapacity = productCard.querySelector('.capacity button.active');
            const capacity = activeCapacity?.textContent.trim() || '';
            const price = productCard.querySelector('.price-new').textContent;

            // Hiệu ứng bay vào giỏ hàng
            flyToCart(productImg);

            // Animation nút
            const originalHTML = btn.innerHTML;
            btn.innerHTML = '<i class="fa-solid fa-check"></i>';
            btn.style.backgroundColor = '#4caf50';

            setTimeout(() => {
                btn.innerHTML = originalHTML;
                btn.style.backgroundColor = '';
            }, 1500);

            // Cập nhật số lượng giỏ hàng
            cartCount++;
            if (cartBadge) {
                cartBadge.textContent = cartCount;
            }

            console.log('Đã thêm vào giỏ:', {
                name: productName,
                capacity: capacity,
                price: price
            });

            // TODO: Gọi API thêm vào giỏ hàng thực tế
        });
    });
}

function flyToCart(productImg) {
    const imgClone = productImg.cloneNode(true);
    const rect = productImg.getBoundingClientRect();

    imgClone.style.position = 'fixed';
    imgClone.style.left = rect.left + 'px';
    imgClone.style.top = rect.top + 'px';
    imgClone.style.width = rect.width + 'px';
    imgClone.style.height = rect.height + 'px';
    imgClone.style.transition = 'all 0.8s cubic-bezier(0.4, 0, 0.2, 1)';
    imgClone.style.zIndex = '9999';
    imgClone.style.pointerEvents = 'none';

    document.body.appendChild(imgClone);

    const cartIcon = document.getElementById('btn-cart');
    if (!cartIcon) return;

    const cartRect = cartIcon.getBoundingClientRect();

    setTimeout(() => {
        imgClone.style.left = cartRect.left + 'px';
        imgClone.style.top = cartRect.top + 'px';
        imgClone.style.width = '30px';
        imgClone.style.height = '30px';
        imgClone.style.opacity = '0';
    }, 50);

    setTimeout(() => {
        if (document.body.contains(imgClone)) {
            document.body.removeChild(imgClone);
        }
    }, 900);
}

// ===== HỆ THỐNG LOAD MORE =====
function initLoadMore() {
    const productCards = Array.from(document.querySelectorAll("#product-list .product-card"));
    const loadMoreBtn = document.getElementById("loadMoreBtn");
    const spinner = document.getElementById("loadMoreSpinner");
    const pageSize = 8;
    let currentIndex = 0;

    if (!loadMoreBtn || productCards.length === 0) return;

    function showNextProducts() {
        spinner.style.display = "inline";
        loadMoreBtn.disabled = true;

        setTimeout(() => {
            const nextProducts = productCards.slice(currentIndex, currentIndex + pageSize);
            nextProducts.forEach(card => {
                card.style.display = "flex";
                // Re-init events for newly shown cards
                initCapacitySelection(card);
                initColorSelection(card);
            });

            currentIndex += nextProducts.length;

            spinner.style.display = "none";
            loadMoreBtn.disabled = false;

            // Ẩn nút nếu hết sản phẩm
            if (currentIndex >= productCards.length) {
                loadMoreBtn.style.display = "none";
            }
        }, 300);
    }

    // Hiển thị sản phẩm ban đầu
    showNextProducts();

    // Event nút Xem thêm
    loadMoreBtn.addEventListener("click", showNextProducts);
}

// ===== HELPER FUNCTIONS =====
function formatPrice(price) {
    const num = parseFloat(price);
    if (isNaN(num)) return '0';
    return num.toLocaleString('vi-VN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    });
}