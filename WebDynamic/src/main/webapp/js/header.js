// ============ HEADER INTERACTIONS ============
document.addEventListener("DOMContentLoaded", () => {
    initHeaderSearch();
    initUserDropdown();
    initMegaMenu();
    initLoginRedirect();
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
        if (searchInput.value.trim() === "") {
            searchInput.classList.remove("show");
        }
    };
}

// ===== USER DROPDOWN (chỉ toggle menu, không thay đổi username) =====
function initUserDropdown() {
    const userArea = document.getElementById("user-area");
    const profileBtn = document.getElementById("user-profile");
    const usernameSpan = document.getElementById("header-username");

    if (!userArea || !profileBtn || !usernameSpan) return;

    // Kiểm tra xem user đã đăng nhập chưa (dựa vào nội dung span)
    const isLoggedIn = usernameSpan.textContent.trim() !== "Đăng nhập";

    if (isLoggedIn) {
        // Đã đăng nhập: toggle dropdown
        profileBtn.onclick = (e) => {
            e.preventDefault();
            userArea.classList.toggle("open");
        };

        // Click outside để đóng dropdown
        document.addEventListener("click", (e) => {
            if (!userArea.contains(e.target)) {
                userArea.classList.remove("open");
            }
        });
    } else {
        // Chưa đăng nhập: chuyển đến trang login
        profileBtn.onclick = (e) => {
            e.preventDefault();
            window.location.href = getContextPath() + "/login";
        };
    }
}

// ===== LOGIN REDIRECT - Xử lý khi click vào text "Đăng nhập" =====
function initLoginRedirect() {
    const usernameSpan = document.getElementById("header-username");

    if (!usernameSpan) return;

    // Nếu nội dung là "Đăng nhập", cho phép click
    if (usernameSpan.textContent.trim() === "Đăng nhập") {
        usernameSpan.style.cursor = "pointer";
        usernameSpan.onclick = (e) => {
            e.preventDefault();
            window.location.href = getContextPath() + "/login";
        };
    }
}

// ===== MEGA MENU =====
function initMegaMenu() {
    const accessoryItem = document.getElementById("nav-accessory-item");
    const megaAccessory = document.getElementById("mega-accessory");

    if (!accessoryItem || !megaAccessory) return;

    // Mobile menu toggle
    accessoryItem.addEventListener("mouseenter", () => {
        if (window.innerWidth > 768) {
            megaAccessory.style.display = "block";
        }
    });

    accessoryItem.addEventListener("mouseleave", () => {
        if (window.innerWidth > 768) {
            megaAccessory.style.display = "none";
        }
    });

    // Mobile click toggle
    const accessoryLink = document.getElementById("nav-accessory");
    if (accessoryLink) {
        accessoryLink.onclick = (e) => {
            if (window.innerWidth <= 768) {
                e.preventDefault();
                const isOpen = accessoryItem.classList.toggle("open");
                megaAccessory.style.display = isOpen ? "block" : "none";
            }
        };
    }

    // Reset on window resize
    window.addEventListener("resize", () => {
        if (window.innerWidth > 768) {
            megaAccessory.style.display = "";
            accessoryItem.classList.remove("open");
        }
    });
}

// ===== UTILITY: Get Context Path =====
function getContextPath() {
    // Cách 1: Lấy từ data attribute của header
    const header = document.querySelector('header');
    if (header && header.getAttribute('data-context-path')) {
        return header.getAttribute('data-context-path');
    }

    // Cách 2: Lấy từ URL hiện tại (fallback)
    // Ví dụ: http://localhost:8080/WebDynamic_war/home -> /WebDynamic_war
    const pathArray = window.location.pathname.split('/');
    return pathArray.length > 1 && pathArray[1] ? '/' + pathArray[1] : '';
}

// ===== EXPORT cho các file khác sử dụng =====
window.getContextPath = getContextPath;