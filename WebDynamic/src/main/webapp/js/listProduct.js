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
            
            // Áp dụng lọc thương hiệu ngay lập tức
            const brandId = brand.getAttribute('data-brand-id') || getBrandIdFromImage(brand);
            if (brandId) {
                const url = new URL(window.location.href);
                url.searchParams.set('brandId', brandId);
                window.location.href = url.toString();
            }
        });
    });
    
    // Khôi phục trạng thái brand từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const currentBrandId = urlParams.get('brandId');
    if (currentBrandId) {
        document.querySelectorAll('.brand').forEach(brand => {
            const brandId = brand.getAttribute('data-brand-id') || getBrandIdFromImage(brand);
            if (brandId && brandId.toString() === currentBrandId) {
                brand.classList.add('active');
            }
        });
    }

    // Xử lý chọn category tag (cho linh kiện)
    document.querySelectorAll('.category').forEach(category => {
        category.addEventListener('click', () => {
            const isActive = category.classList.contains('active');
            
            // Toggle active
            if (isActive) {
                category.classList.remove('active');
            } else {
                category.classList.add('active');
            }
            
            // Áp dụng filter ngay lập tức
            applyCategoryFilters();
        });
    });
    
    // Khôi phục trạng thái category từ URL
    const currentTypes = urlParams.getAll('type');
    if (currentTypes && currentTypes.length > 0) {
        document.querySelectorAll('.category').forEach(category => {
            const categoryType = category.getAttribute('data-type');
            if (currentTypes.includes(categoryType)) {
                category.classList.add('active');
            }
        });
    }
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
    const priceRange = getPriceRange();
    const memory = getSelectedOptions('bonho');
    const colors = getSelectedOptions('mausac');
    const year = getSelectedOptions('namramat');
    const type = getSelectedOptions('loai');
    const brand = getSelectedBrand();
    const condition = getSelectedOptions('tinhtrang');
    const model = getSelectedOptions('model');

    // Xây dựng URL với các tham số
    const url = new URL(window.location.href);
    
    // Xóa các tham số cũ
    url.searchParams.delete('priceMin');
    url.searchParams.delete('priceMax');
    url.searchParams.delete('memory');
    url.searchParams.delete('color');
    url.searchParams.delete('year');
    url.searchParams.delete('type');
    url.searchParams.delete('brandId');
    url.searchParams.delete('condition');
    url.searchParams.delete('model');

    // Thêm các tham số mới
    if (priceRange.min) {
        url.searchParams.set('priceMin', priceRange.min);
    }
    if (priceRange.max) {
        url.searchParams.set('priceMax', priceRange.max);
    }
    if (memory && memory.length > 0) {
        memory.forEach(m => url.searchParams.append('memory', m));
    }
    if (colors && colors.length > 0) {
        colors.forEach(c => url.searchParams.append('color', c));
    }
    if (year && year.length > 0) {
        url.searchParams.set('year', year[0]); // Chỉ lấy năm đầu tiên
    }
    if (type && type.length > 0) {
        type.forEach(t => url.searchParams.append('type', t));
    }
    if (brand) {
        url.searchParams.set('brandId', brand);
    }
    if (condition && condition.length > 0) {
        url.searchParams.set('condition', condition[0]);
    }
    if (model && model.length > 0) {
        model.forEach(m => url.searchParams.append('model', m));
    }

    // Giữ nguyên sort nếu có
    const currentSort = url.searchParams.get('sort');
    if (!currentSort) {
        url.searchParams.delete('sort');
    }

    // Reload trang với các tham số mới
    window.location.href = url.toString();
}

// Hàm áp dụng filter khi click vào category tag
function applyCategoryFilters() {
    // Thu thập các category đang active
    const activeCategories = Array.from(document.querySelectorAll('.category.active'))
        .map(cat => cat.getAttribute('data-type'))
        .filter(type => type !== null);

    // Thu thập các filter khác
    const priceRange = getPriceRange();
    const brand = getSelectedBrand();
    const brandFromDropdown = getSelectedOptions('thuonghieu');
    const model = getSelectedOptions('model');

    // Xây dựng URL với các tham số
    const url = new URL(window.location.href);
    
    // Xóa các tham số cũ
    url.searchParams.delete('priceMin');
    url.searchParams.delete('priceMax');
    url.searchParams.delete('type');
    url.searchParams.delete('brandId');
    url.searchParams.delete('model');

    // Thêm các tham số mới
    if (priceRange.min) {
        url.searchParams.set('priceMin', priceRange.min);
    }
    if (priceRange.max) {
        url.searchParams.set('priceMax', priceRange.max);
    }
    if (activeCategories.length > 0) {
        activeCategories.forEach(t => url.searchParams.append('type', t));
    }
    if (brand) {
        url.searchParams.set('brandId', brand);
    } else if (brandFromDropdown && brandFromDropdown.length > 0) {
        // Nếu không có brand từ brand-list, lấy từ dropdown
        const brandMap = {
            'Samsung': 1,
            'iPhone': 2,
            'Oppo': 3,
            'Vivo': 4,
            'Generic': 5
        };
        const brandId = brandMap[brandFromDropdown[0]];
        if (brandId) {
            url.searchParams.set('brandId', brandId);
        }
    }
    if (model && model.length > 0) {
        model.forEach(m => url.searchParams.append('model', m));
    }

    // Giữ nguyên sort nếu có
    const currentSort = url.searchParams.get('sort');
    if (!currentSort) {
        url.searchParams.delete('sort');
    }

    // Reload trang với các tham số mới
    window.location.href = url.toString();
}

function getPriceRange() {
    const priceInputs = document.querySelectorAll('#gia input[type="number"]');
    return {
        min: priceInputs[0]?.value || null,
        max: priceInputs[1]?.value || null
    };
}

function getSelectedOptions(filterId) {
    const dropdown = document.getElementById(filterId);
    if (!dropdown) return null;
    const activeButtons = dropdown.querySelectorAll('.option-group button.active');
    const options = Array.from(activeButtons).map(btn => btn.textContent.trim());
    return options.length > 0 ? options : null;
}

function getSelectedBrand() {
    const activeBrand = document.querySelector('.brand.active');
    if (!activeBrand) return null;
    // Lấy brand ID từ data attribute hoặc từ alt text
    const brandId = activeBrand.getAttribute('data-brand-id');
    if (brandId) return brandId;
    
    // Map brand name to ID (có thể cần cập nhật dựa trên database)
    const brandName = activeBrand.querySelector('img')?.alt || '';
    const brandMap = {
        'Samsung': 1,
        'iPhone': 2,
        'Oppo': 3,
        'Vivo': 4
    };
    return brandMap[brandName] || null;
}

// ===== HỆ THỐNG SẮP XẾP =====
function initSortSystem() {
    const sortItems = document.querySelectorAll("#sortList li");

    // Khôi phục trạng thái sort từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const currentSort = urlParams.get('sort');
    if (currentSort) {
        const sortMap = {
            'banchay': 'Bán chạy',
            'giamgia': 'Giảm giá',
            'moi': 'Mới',
            'giatang': 'Giá',
            'giagiam': 'Giá'
        };
        const sortText = sortMap[currentSort] || 'Nổi bật';
        sortItems.forEach(li => {
            if (li.textContent.trim().includes(sortText)) {
                li.classList.add('active');
            }
        });
    }

    sortItems.forEach(item => {
        item.addEventListener("click", () => {
            sortItems.forEach(li => li.classList.remove("active"));
            item.classList.add("active");

            const sortType = item.textContent.trim();
            applySort(sortType);
        });
    });
}

function applySort(sortType) {
    // Xây dựng URL với tham số sort
    const url = new URL(window.location.href);
    
    // Map sort type to parameter value
    let sortParam = '';
    switch(sortType) {
        case 'Bán chạy':
            sortParam = 'banchay';
            break;
        case 'Giảm giá':
            sortParam = 'giamgia';
            break;
        case 'Mới':
            sortParam = 'moi';
            break;
        case 'Giá':
            // Toggle giá tăng/giảm
            const arrow = document.querySelector('#sortList li:last-child .arrow');
            const currentSort = url.searchParams.get('sort');
            if (arrow && arrow.classList.contains('asc') || currentSort === 'giatang') {
                arrow.classList.remove('asc');
                sortParam = 'giagiam';
            } else {
                if (arrow) arrow.classList.add('asc');
                sortParam = 'giatang';
            }
            break;
        default: // Nổi bật
            sortParam = '';
            break;
    }

    if (sortParam) {
        url.searchParams.set('sort', sortParam);
    } else {
        url.searchParams.delete('sort');
    }

    // Reload trang với tham số sort mới
    window.location.href = url.toString();
}

// Hàm sort trên client (fallback nếu cần)
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

function getBrandIdFromImage(brandElement) {
    const img = brandElement.querySelector('img');
    if (!img) return null;
    
    const alt = img.alt || '';
    const brandMap = {
        'Samsung': 1,
        'iPhone': 2,
        'Oppo': 3,
        'Vivo': 4
    };
    return brandMap[alt] || null;
}