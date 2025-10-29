// ============ HEADER INTERACTIONS ============

// Load header dynamically
function loadHeader() {
    const headerContainer = document.getElementById("header-container");
    if (headerContainer) {
        fetch("header.html")
            .then(response => response.text())
            .then(html => {
                headerContainer.innerHTML = html;
                initializeHeaderEvents();
            })
            .catch(error => {
                console.error("Error loading header:", error);
            });
    } else {
        initializeHeaderEvents();
    }
}

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
            window.location.href = "./cart.html";
        });
    }

    // === 3️⃣ NGƯỜI DÙNG / LOGIN ===
    renderUserArea();

    // === 4️⃣ MENU MOBILE ===
    const menu = document.getElementById("menu");
    const mobileMenuBtn = document.getElementById("mobile-menu-btn");

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

        // Close menu when clicking outside
        document.addEventListener("click", (e) => {
            if (!menu.contains(e.target) && !mobileMenuBtn.contains(e.target)) {
                menu.classList.remove("active");
                mobileMenuBtn.classList.remove("active");
            }
        });
    }

    // === 5️⃣ NAVIGATION LINKS ===
    const navHome = document.getElementById("nav-home");
    const navPhone = document.getElementById("nav-phone");
    const navAccessory = document.getElementById("nav-accessory");
    const navContact = document.getElementById("nav-contact");

    if (navHome) navHome.addEventListener("click", (e) => { e.preventDefault(); window.location.href = "./index.html"; });
    if (navPhone) navPhone.addEventListener("click", (e) => { e.preventDefault(); /* Add phone page */ });
    if (navAccessory) navAccessory.addEventListener("click", (e) => { e.preventDefault(); /* Add accessory page */ });
    if (navContact) navContact.addEventListener("click", (e) => { e.preventDefault(); /* Add contact page */ });
}

// Chờ DOM tải xong
document.addEventListener("DOMContentLoaded", () => {
    loadHeader();
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
            <a href="./info-user.html">Tài Khoản Của Tôi</a>
            <a href="./order_detail.html">Đơn Mua</a>
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
            window.location.href = "./login.html"; // hoặc "../login.html" tùy cấp thư mục
        });
    }
}
