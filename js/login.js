document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("login-form");
    const usernameInput = document.getElementById("login-username");
    const passwordInput = document.getElementById("login-password");
    const checkboxRemember = document.getElementById("remember-checkbox");
    const loginButton = document.getElementById("btn-login");

    // --- Chỉ tự động điền username nếu đã ghi nhớ và checkbox còn được bật ---
    const rememberedUsername = localStorage.getItem("rememberedUsername");
    const rememberChecked = localStorage.getItem("rememberChecked");

    if (rememberChecked === "true" && rememberedUsername) {
        usernameInput.value = rememberedUsername;
        checkboxRemember.checked = true;
    } else {
        checkboxRemember.checked = false;
        usernameInput.value = ""; // đảm bảo không còn hiển thị "admin"
    }

    // --- Xử lý khi nhấn Đăng nhập ---
    form.addEventListener("submit", function (e) {
        e.preventDefault(); // Ngăn reload trang

        const username = usernameInput.value.trim();
        const password = passwordInput.value.trim();

        // --- Kiểm tra rỗng ---
        if (username === "" || password === "") {
            showAlert("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            return;
        }

        // --- Giả lập đăng nhập ---
        if (username === "admin" && password === "123456") {
            showAlert("Đăng nhập thành công!", "success");

            // Lưu hoặc xóa tên người dùng theo checkbox
            if (checkboxRemember.checked) {
                localStorage.setItem("rememberedUsername", username);
                localStorage.setItem("rememberChecked", "true");
            } else {
                localStorage.removeItem("rememberedUsername");
                localStorage.removeItem("rememberChecked");
            }

            // Giả lập chuyển hướng sau khi đăng nhập
            setTimeout(() => {
                window.location.href = "info-user.html";
            }, 1500);
        } else {
            showAlert("Tên đăng nhập hoặc mật khẩu không đúng!", "error");
        }
    });

    // --- Cho phép nhấn Enter để đăng nhập ---
    form.addEventListener("keypress", function (e) {
        if (e.key === "Enter") {
            e.preventDefault();
            loginButton.click();
        }
    });

    // --- Hàm hiển thị thông báo ---
    function showAlert(message, type = "error") {
        const oldAlert = document.querySelector(".alert");
        if (oldAlert) oldAlert.remove();

        const alert = document.createElement("div");
        alert.className = `alert ${type}`;
        alert.textContent = message;

        Object.assign(alert.style, {
            position: "fixed",
            top: "20px",
            left: "50%",
            transform: "translateX(-50%)",
            padding: "12px 20px",
            borderRadius: "6px",
            color: "#fff",
            zIndex: "9999",
            fontSize: "15px",
            backgroundColor: type === "success" ? "#28a745" : "#dc3545",
            boxShadow: "0 2px 6px rgba(0,0,0,0.2)",
            opacity: "0",
            transition: "opacity 0.5s ease",
        });

        document.body.appendChild(alert);

        // Hiện mượt
        setTimeout(() => (alert.style.opacity = "1"), 10);

        // Ẩn dần rồi xóa
        setTimeout(() => {
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 2500);
    }
});
