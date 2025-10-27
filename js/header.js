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
    let searchBox;

    if (searchBtn) {
        searchBtn.addEventListener("click", (e) => {
            e.preventDefault();

            if (!searchBox) {
                searchBox = document.createElement("input");
                searchBox.type = "text";
                searchBox.placeholder = "🔍 Tìm kiếm sản phẩm...";
                searchBox.className = "search-input";
                document.body.appendChild(searchBox);

                // Hiển thị với animation
                setTimeout(() => {
                    searchBox.classList.add("show");
                    searchBox.focus();
                }, 50);
            } else {
                // Nếu đã có -> ẩn với hiệu ứng ngược
                searchBox.classList.remove("show");
                setTimeout(() => {
                    searchBox.remove();
                    searchBox = null;
                }, 300);
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
    const userBtn = document.getElementById("btn-user");
    if (userBtn) {
        userBtn.addEventListener("click", (e) => {
            e.preventDefault();
            window.location.href = "./login.html";
        });
    }

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
