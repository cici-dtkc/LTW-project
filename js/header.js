// ============ HEADER INTERACTIONS ============
document.addEventListener("DOMContentLoaded", () => {
    initHeaderSearch();
    initCart();
    initUserArea();
    initNavigation();
    initMegaMenu();
});

// ===== SEARCH =====
function initHeaderSearch() {
    const searchBtn = document.getElementById("btn-search");
    const searchInput = document.getElementById("header-search");
    if (!searchBtn || !searchInput) return;

    searchBtn.onclick = (e) => {
        e.preventDefault();
        const show = !searchInput.classList.contains("show");
        searchInput.classList.toggle("show", show);
        if (show) searchInput.focus();
    };

    searchInput.onblur = () => {
        if (searchInput.value.trim() === "") searchInput.classList.remove("show");
    };
}

// ===== USER LOGIN CHECK =====
function getCurrentUser() {
    try { return JSON.parse(localStorage.getItem("currentUser")); }
    catch { return null; }
}

// ===== CART =====
function getCartItems() {
    try { return JSON.parse(localStorage.getItem("cart")) || []; }
    catch { return []; }
}

function getCartTotalQuantity() {
    return getCartItems().reduce((sum, item) => sum + (item.quantity || 1), 0);
}

function updateCartBadge() {
    const badge = document.getElementById("cart-badge");
    const user = getCurrentUser();

    if (!badge) return;

    // Không đăng nhập → ẩn badge
    if (!user) {
        badge.style.display = "none";
        return;
    }

    const count = getCartTotalQuantity();
    if (count > 0) {
        badge.textContent = count > 99 ? "99+" : count;
        badge.style.display = "flex";
    } else {
        badge.style.display = "none";
    }
}

function initCart() {
    const cartBtn = document.getElementById("btn-cart");
    const user = getCurrentUser();

    if (!cartBtn) return;

    cartBtn.onclick = (e) => {
        e.preventDefault();

        // Chưa đăng nhập → chuyển sang login
        if (!user) {
            window.location.href = "login.html";
            return;
        }

        window.location.href = "cart.html";
    };

    updateCartBadge();

    window.addEventListener("storage", (e) => {
        if (e.key === "cart") updateCartBadge();
    });
}

// ===== USER AREA =====
function initUserArea() {
    const userArea = document.getElementById("user-area");
    const profileBtn = document.getElementById("user-profile");
    const dropdown = document.getElementById("user-dropdown");
    const logoutLink = document.getElementById("logout-link");
    const usernameSpan = document.getElementById("header-username");

    const user = getCurrentUser();

    if (!userArea || !profileBtn || !dropdown || !logoutLink || !usernameSpan) return;

    // Nếu chưa đăng nhập
    if (!user) {
        usernameSpan.textContent = "Đăng nhập";
        dropdown.style.display = "none";
        profileBtn.onclick = () => window.location.href = "login.html";
        return;
    }

    // Nếu đã đăng nhập
    usernameSpan.textContent = user.username || "Người dùng";

    profileBtn.onclick = (e) => {
        e.preventDefault();
        userArea.classList.toggle("open");
    };

    document.addEventListener("click", (e) => {
        if (!userArea.contains(e.target)) userArea.classList.remove("open");
    });

    logoutLink.onclick = (e) => {
        e.preventDefault();
        localStorage.removeItem("currentUser");
        localStorage.removeItem("cart"); // reset giỏ hàng sau khi logout
        updateCartBadge();
        window.location.href = "login.html";
    };
}

// ===== NAVIGATION =====
function initNavigation() {
    const navHome = document.getElementById("nav-home");
    const navPhone = document.getElementById("nav-phone");
    const navAccessory = document.getElementById("nav-accessory");
    const navContact = document.getElementById("nav-contact");

    if (navHome) navHome.onclick = () => (window.location.href = "home.html");
    if (navPhone) navPhone.onclick = () => (window.location.href = "listproduct.html");
    if (navAccessory) navAccessory.onclick = () => (window.location.href = "listproduct_accessory.html");
    if (navContact) navContact.onclick = () => (window.location.hash = "footer");
}

// ===== MEGA MENU =====
function initMegaMenu() {
    const accessoryItem = document.getElementById("nav-accessory-item");
    const megaAccessory = document.getElementById("mega-accessory");

    if (!accessoryItem || !megaAccessory) return;

    accessoryItem.onclick = (e) => {
        if (window.innerWidth <= 768) {
            e.preventDefault();
            const isOpen = accessoryItem.classList.toggle("open");
            megaAccessory.style.display = isOpen ? "block" : "none";
        }
    };

    window.addEventListener("resize", () => {
        if (window.innerWidth > 768) {
            megaAccessory.style.display = "";
            accessoryItem.classList.remove("open");
        }
    });
}

// ===== ADD TO CART =====
function addToCart(product) {
    const user = getCurrentUser();

    if (!user) {
        window.location.href = "login.html";
        return;
    }

    const cart = getCartItems();
    const idx = cart.findIndex(i => i.id === product.id);

    if (idx >= 0) cart[idx].quantity += 1;
    else cart.push({ ...product, quantity: 1 });

    localStorage.setItem("cart", JSON.stringify(cart));
    updateCartBadge();
}

window.addToCart = addToCart;
window.updateCartBadge = updateCartBadge;
