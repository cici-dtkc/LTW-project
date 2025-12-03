
// Thực hiện tao tác logout -> thoát ra trang đăng nhập
document.addEventListener("click", function(e) {
    const logoutLink = e.target.closest('a[data-target="logout"]');
    if (!logoutLink) return;

    e.preventDefault();
    if (confirm("Bạn có chắc chắn muốn đăng xuất?")) {
        window.location.href = "../login.html";
    }
});

toggleSidebar();
activeSidebar();

// đóng mở menu
function toggleSidebar() {
    const sidebar = document.getElementById("sidebar");
    const sidebarToggle = document.querySelector(".sidebar-toggle");
    sidebarToggle.addEventListener("click", function() {
        sidebar.classList.toggle("collapsed");
        toggleHeader();
    });
}

//co dãn theo toggle
function toggleHeader() {
    const sidebar = document.getElementById("sidebar");
    const topbar = document.querySelector(".topbar");
    const container = document.querySelector(".container");
    //colappsed header khi đóng mở sidebar
    if (sidebar.classList.contains("collapsed")) {
        // Collapsed state
        sidebar.style.width = "60px";
        if (container) container.style.marginLeft = "175px";
        if (topbar) {
            topbar.style.width = "calc(100% - 60px)";
            topbar.style.left = "60px";
        }
    } else {
        // Expanded state
        sidebar.style.width = "250px";
        if (container) container.style.marginLeft = "250px";
        if (topbar) {
            topbar.style.width = "calc(100% - 250px)";
            topbar.style.left = "250px";
        }

    }
}
// Active sidebar theo href
function activeSidebar() {
    const navLinks = document.querySelectorAll(".nav-link");

    let currentPath = window.location.pathname;
    let currentFile = currentPath.split("/").pop().split("?")[0];
    // Nếu không có file hoặc chỉ là "#"
    if (!currentFile || currentFile === "#" || !currentFile.includes(".html")) {
        currentFile = "dashboardAdmin.html"; // Mặc định Dashboard
    }
    let foundActive = false;

    navLinks.forEach(link => {
        const href = link.getAttribute("href");
        if (!href) return;

        const linkFile = href.split("/").pop(); // Lấy tên file trong href

        // Xóa active cũ
        link.classList.remove("active");

        if (linkFile === currentFile) {
            link.classList.add("active");
            foundActive = true;
        }
    });

    // Không tìm thấy file trùng → active Dashboard
    if (!foundActive) {
        const dashboardLink = document.querySelector('.nav-link[data-target="dashboard"]');
        if (dashboardLink) dashboardLink.classList.add("active");
    }
};




