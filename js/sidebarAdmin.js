


// Thực hiện tao tác logout -> thoát ra trang đăng nhập
document.addEventListener("click", function(e) {
    const logoutLink = e.target.closest('a[data-target="logout"]');
    if (!logoutLink) return;

    e.preventDefault();
    if (confirm("Bạn có chắc chắn muốn đăng xuất?")) {
        window.location.href = "../login.html";
    }
});

// Load sidebarAdmin cho các trang của admin
(async function loadSidebar() {
    const container = document.getElementById("sidebarContainer");
    if (!container) return;

    try {
        const res = await fetch("sideBarAdmin.html");
        if (!res.ok) throw new Error("Không thể load Sidebar");

        const html = await res.text();
        const doc = new DOMParser().parseFromString(html, "text/html");
        const aside = doc.querySelector("aside");

        if (!aside) return console.error("Không tìm thấy thẻ <aside> trong sidebar");

        container.replaceWith(aside);
        // gọi hàm đóng mở khi load sidebar
        toggleSidebar();

    } catch (err) {
        console.error(err);
    }
})();

// đóng mở menu
function toggleSidebar() {
    const sidebar = document.getElementById("sidebar");
    const sidebarToggle = document.querySelector(".sidebar-toggle");
    sidebarToggle.addEventListener("click", function() {
        sidebar.classList.toggle("collapsed");
    });
}






