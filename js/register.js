document.addEventListener("DOMContentLoaded", function () {
    // --- Lấy phần tử form và các input ---
    const registerForm = document.querySelector(".form-regis");
    const username = document.getElementById("username");
    const password = document.getElementById("password");
    const confirmPass = document.getElementById("confirm");
    const email = document.getElementById("email");
    const firstName = document.getElementById("fname");
    const lastName = document.getElementById("lname");
    const emailInput = registerForm.querySelector('input[type="email"]');

    // --- Xử lý khi nhấn nút "Tạo tài khoản" ---
    registerForm.addEventListener("submit", function (e) {
        e.preventDefault(); // Ngăn reload trang

        const usernameVal = username.value.trim();
        const passwordVal = password.value.trim();
        const confirmVal = confirmPass.value.trim();
        const emailVal = email.value.trim();
        const firstVal = firstName.value.trim();
        const lastVal = lastName.value.trim();

        // --- Kiểm tra thông tin rỗng ---
        if (!usernameVal || !passwordVal || !emailVal) {
            showAlert("Vui lòng nhập đầy đủ thông tin!", "error");
            return;
        }

        // --- Kiểm tra mật khẩu khớp ---
        if (passwordVal !== confirmVal) {
            showAlert("Mật khẩu và xác nhận mật khẩu không khớp!", "error");
            return;
        }
        // Regex kiểm tra email hợp lệ
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailVal)) {
            e.preventDefault();
            alert("Email không hợp lệ! Vui lòng nhập đúng định dạng.");
            emailInput.focus();
            return;
        }
        // --- Lấy danh sách người dùng hiện có ---
        let users = JSON.parse(localStorage.getItem("users")) || [];

        // --- Kiểm tra trùng username ---
        if (users.some(u => u.username === usernameVal)) {
            showAlert("Tên đăng nhập đã tồn tại!", "error");
            return;
        }

        // --- Kiểm tra trùng email ---
        if (users.some(u => u.email === emailVal)) {
            showAlert("Email này đã được sử dụng!", "error");
            return;
        }

        // --- Lưu người dùng mới ---
        users.push({
            username: usernameVal,
            password: passwordVal,
            email: emailVal,
            firstName: firstVal,
            lastName: lastVal
        });

        localStorage.setItem("users", JSON.stringify(users));

        // --- Hiển thị thông báo và chuyển hướng ---
        showAlert("Đăng ký thành công! Đang chuyển hướng đến trang trang chủ...", "success");

        // Xóa form và chuyển hướng sau 1.5s
        setTimeout(() => {
            registerForm.reset();
            window.location.href = "login.html";
        }, 1500);
    });

    // --- Hàm hiển thị thông báo ---
    function showAlert(message, type = "error") {
        // Xóa thông báo cũ (nếu có)
        const oldAlert = document.querySelector(".alert");
        if (oldAlert) oldAlert.remove();

        const alert = document.createElement("div");
        alert.textContent = message;
        alert.className = `alert ${type}`;

        Object.assign(alert.style, {
            position: "fixed",
            top: "20px",
            left: "50%",
            transform: "translateX(-50%)",
            padding: "12px 20px",
            borderRadius: "6px",
            backgroundColor: type === "success" ? "#28a745" : "#dc3545",
            color: "#fff",
            zIndex: "9999",
            fontSize: "15px",
            boxShadow: "0 2px 6px rgba(0,0,0,0.2)",
            opacity: "0",
            transition: "opacity 0.5s ease",
        });

        document.body.appendChild(alert);

        // Hiện mượt
        setTimeout(() => (alert.style.opacity = "1"), 10);

        // Tự ẩn sau 2.5s
        setTimeout(() => {
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 2500);
    }
});
