// ============ HEADER INTERACTIONS ============

// Initialize header events
function initializeHeaderEvents() {
    // === 1️⃣ TÌM KIẾM ===
    const searchBtn = document.getElementById("btn-search");
    const searchInput = document.getElementById("header-search");
    if (searchBtn && searchInput) {
        searchBtn.addEventListener("click", (e) => {
            e.preventDefault();
            const willShow = !searchInput.classList.contains("show");
            if (willShow) {
                searchInput.classList.add("show");
                setTimeout(() => searchInput.focus(), 30);
            } else {
                searchInput.classList.remove("show");
            }
        });

        // Tự ẩn khi blur nếu trống
        searchInput.addEventListener("blur", () => {
            if (searchInput.value.trim() === "") {
                searchInput.classList.remove("show");
            }
        });
    }

    // === 2️⃣ GIỎ HÀNG ===
    const cartBtn = document.getElementById("btn-cart");
    if (cartBtn) {
        cartBtn.addEventListener("click", (e) => {
            e.preventDefault();
            window.location.href = "../cart.html";
        });
    }

    // Cập nhật số lượng giỏ hàng
    updateCartBadge();

    // === 3️⃣ NGƯỜI DÙNG / LOGIN ===
    renderUserArea();

    // === 4️⃣ MENU MOBILE ===
    const menu = document.getElementById("menu");
    const mobileMenuBtn = document.getElementById("mobile-menu-btn");
    const accessoryItem = document.getElementById("nav-accessory-item");
    const megaAccessory = document.getElementById("mega-accessory");

    if (mobileMenuBtn && menu) {
        // Handle resize
        const handleResize = () => {
            if (window.innerWidth <= 768) {
                mobileMenuBtn.style.display = "block";
                menu.style.display = "none";
            } else {
                mobileMenuBtn.style.display = "none";
                menu.style.display = "flex";
                menu.classList.remove("active");
            }
        };

        window.addEventListener("resize", handleResize);
        handleResize();

        // Toggle mobile menu
        mobileMenuBtn.addEventListener("click", () => {
            menu.classList.toggle("active");
            mobileMenuBtn.classList.toggle("active");
        });

        // Mobile: tap to open/close mega menu under Accessories
        if (accessoryItem && megaAccessory) {
            accessoryItem.addEventListener("click", (e) => {
                if (window.innerWidth <= 768) {
                    e.preventDefault();
                    const isOpen = accessoryItem.classList.toggle("open");
                    megaAccessory.style.display = isOpen ? "block" : "none";
                }
            });
        }

        // Close menu when clicking outside
        document.addEventListener("click", (e) => {
            if (!menu.contains(e.target) && !mobileMenuBtn.contains(e.target)) {
                menu.classList.remove("active");
                mobileMenuBtn.classList.remove("active");
                if (accessoryItem && megaAccessory) {
                    accessoryItem.classList.remove("open");
                    megaAccessory.style.display = "none";
                }
            }
        });
    }

    // === 5️⃣ NAVIGATION LINKS ===
    const navHome = document.getElementById("nav-home");
    const navPhone = document.getElementById("nav-phone");
    const navAccessory = document.getElementById("nav-accessory");
    const navContact = document.getElementById("nav-contact");

    if (navHome) navHome.addEventListener("click", (e) => { e.preventDefault(); window.location.href = "./home.html"; });
    if (navPhone) navPhone.addEventListener("click", (e) => { e.preventDefault(); /* Add phone page */ });
    if (navAccessory) navAccessory.addEventListener("click", (e) => { e.preventDefault(); /* Add accessory page */ });
    if (navContact) navContact.addEventListener("click", (e) => { e.preventDefault(); /* Add contact page */ });
}

// Chờ DOM tải xong
document.addEventListener("DOMContentLoaded", () => {
    initializeHeaderEvents();
    // Khởi tạo giỏ hàng giả lập để test
    setTimeout(() => {
        initMockCart();
        updateCartBadge(); // Đảm bảo cập nhật badge sau khi init
    }, 100);
});

// ===== Cart helpers =====
function getCartItems() {
    try {
        const cart = localStorage.getItem("cart");
        return cart ? JSON.parse(cart) : [];
    } catch {
        return [];
    }
}

function getCartTotalQuantity() {
    const items = getCartItems();
    return items.reduce((total, item) => total + (item.quantity || 1), 0);
}

function updateCartBadge() {
    const badge = document.getElementById("cart-badge");
    if (!badge) {
        console.log("Badge not found!");
        return;
    }

    const count = getCartTotalQuantity();
    console.log("Cart count:", count); // Debug

    if (count > 0) {
        badge.textContent = count > 99 ? "99+" : count.toString();
        badge.classList.remove("hidden");
        badge.style.display = "flex"; // Đảm bảo hiển thị
        badge.style.visibility = "visible"; // Đảm bảo visible
    } else {
        badge.textContent = "0";
        badge.classList.add("hidden");
    }
}

// ===== GIẢ LẬP DỮ LIỆU GIỎ HÀNG ĐỂ TEST =====
function initMockCart() {
    // Kiểm tra xem đã có giỏ hàng chưa
    const existingCart = localStorage.getItem("cart");

    // Nếu chưa có, tạo dữ liệu giả lập
    if (!existingCart) {
        const mockCart = [
            { id: "iphone-15-pro", name: "iPhone 15 Pro", price: 24990000, quantity: 2, image: "assert/img/product/iphone17.webp" },
            { id: "samsung-s24", name: "Samsung Galaxy S24", price: 19990000, quantity: 1, image: "assert/img/product/iphone14.jpg" }
        ];
        localStorage.setItem("cart", JSON.stringify(mockCart));
    }

    // Cập nhật badge ngay lập tức
    updateCartBadge();
}

// Hàm để các trang khác gọi khi thêm sản phẩm vào giỏ
function addToCart(product) {
    const cart = getCartItems();
    const existingIndex = cart.findIndex(item => item.id === product.id);

    if (existingIndex >= 0) {
        cart[existingIndex].quantity = (cart[existingIndex].quantity || 1) + 1;
    } else {
        cart.push({ ...product, quantity: 1 });
    }

    localStorage.setItem("cart", JSON.stringify(cart));
    updateCartBadge();

    // Dispatch event để các trang khác có thể lắng nghe
    window.dispatchEvent(new CustomEvent("cartUpdated"));
}

// Export hàm để các trang khác có thể dùng
window.addToCart = addToCart;
window.updateCartBadge = updateCartBadge;

// Lắng nghe sự kiện cập nhật giỏ hàng từ các trang khác
window.addEventListener("cartUpdated", updateCartBadge);
window.addEventListener("storage", (e) => {
    if (e.key === "cart") {
        updateCartBadge();
    }
});

// ===== User helpers =====
function getCurrentUser() {
    try {
        const raw = localStorage.getItem("currentUser");
        if (raw) return JSON.parse(raw);
    } catch {}
    const remembered = localStorage.getItem("rememberedUsername");
    if (remembered) return { username: remembered };
    return null;
}

function renderUserArea() {
    const userArea = document.getElementById("user-area");
    if (!userArea) return;

    const user = getCurrentUser();
    if (!user) {
        userArea.innerHTML = '<a href="../login.html" id="login-link" class="user-profile"><i class="fa-solid fa-user"></i><span class="username">Đăng nhập</span></a>';

        return;
    }

    userArea.innerHTML = `
        <div class="user-profile" id="user-profile">
            <i class="fa-solid fa-user"></i>
            <span class="username">${user.username}</span>
        </div>
        <div class="user-dropdown" id="user-dropdown">
            <a href="../info-user.html">Tài Khoản Của Tôi</a>
            <a href="../order_detail.html">Đơn Mua</a>
            <a href="../login.html" id="logout-link">Đăng Xuất</a>
        </div>
    `;

    const profile = document.getElementById("user-profile");
    const dropdown = document.getElementById("user-dropdown");
    const logoutLink = document.getElementById("logout-link");

    if (profile && dropdown) {
        profile.addEventListener("click", (e) => {
            e.preventDefault();
            userArea.classList.toggle("open");
        });

        document.addEventListener("click", (e) => {
            if (!userArea.contains(e.target)) userArea.classList.remove("open");
        });
    }

    if (logoutLink) {
        logoutLink.addEventListener("click", (e) => {
            e.preventDefault();
            localStorage.removeItem("currentUser");
            localStorage.removeItem("rememberedUsername");
            window.location.href = "../login.html";
        });
    }
}
