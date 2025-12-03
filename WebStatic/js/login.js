document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("login-form");
    const usernameInput = document.getElementById("login-username");
    const passwordInput = document.getElementById("login-password");
    const checkboxRemember = document.getElementById("remember-checkbox");
    const messageDiv = document.getElementById("login-message");

    // Hàm hiển thị thông báo
    function showMessage(message, type) {
        messageDiv.textContent = message;
        messageDiv.className = `login-message ${type} show`;

        // Thêm icon tương ứng
        let icon = "";
        if (type === "success") {
            icon = '<i class="fa fa-check-circle"></i>';
        } else if (type === "error") {
            icon = '<i class="fa fa-exclamation-circle"></i>';
        } else if (type === "warning") {
            icon = '<i class="fa fa-exclamation-triangle"></i>';
        }
        messageDiv.innerHTML = icon + " " + message;

        // Tự động ẩn sau 3 giây (trừ khi là success - sẽ redirect)
        if (type !== "success") {
            setTimeout(() => {
                messageDiv.classList.remove("show");
            }, 3000);
        }
    }

    // Hàm ẩn thông báo
    function hideMessage() {
        messageDiv.classList.remove("show");
    }

    // Hàm xóa class error từ input
    function clearErrorInputs() {
        usernameInput.classList.remove("error");
        passwordInput.classList.remove("error");
    }
    // Seed 1 user mặc định nếu chưa có
    try {
        const existingUsers = JSON.parse(localStorage.getItem("users") || "[]");
        if (!Array.isArray(existingUsers) || existingUsers.length === 0) {
            const seeded = [
                {
                    username: "testuser",
                    password: "123456",
                    name: "Test User",
                    email: "test@example.com",
                    phone: "+84 987654321"
                }
            ];
            localStorage.setItem("users", JSON.stringify(seeded));
        }
    } catch (_) {
        const seeded = [
            {
                username: "testuser",
                password: "123456",
                name: "Test User",
                email: "test@example.com",
                phone: "+84 987654321"
            }
        ];
        localStorage.setItem("users", JSON.stringify(seeded));
    }

    // Tự động điền username nếu được ghi nhớ
    const rememberedUsername = localStorage.getItem("rememberedUsername");
    const rememberChecked = localStorage.getItem("rememberChecked");

    if (rememberChecked === "true" && rememberedUsername) {
        usernameInput.value = rememberedUsername;
        if (checkboxRemember) {
            checkboxRemember.checked = true;
        }
    }

    // Xử lý đăng nhập
    form.addEventListener("submit", function (e) {
        e.preventDefault();
        hideMessage();
        clearErrorInputs();

        const username = usernameInput.value.trim();
        const password = passwordInput.value.trim();

        // Kiểm tra trường rỗng
        if (!username && !password) {
            showMessage("Vui lòng nhập tên đăng nhập và mật khẩu!", "error");
            usernameInput.classList.add("error");
            passwordInput.classList.add("error");
            return;
        }

        if (!username) {
            showMessage("Vui lòng nhập tên đăng nhập!", "error");
            usernameInput.classList.add("error");
            return;
        }

        if (!password) {
            showMessage("Vui lòng nhập mật khẩu!", "error");
            passwordInput.classList.add("error");
            return;
        }

        // Kiểm tra độ dài mật khẩu (tối thiểu 6 ký tự)
        if (password.length < 6) {
            showMessage("Mật khẩu phải có ít nhất 6 ký tự!", "error");
            passwordInput.classList.add("error");
            return;
        }
        // admin login
        if (username === "admin" && password === "123456") {
            showMessage("Đăng nhập thành công! Đang chuyển hướng...", "success");
            if (checkboxRemember && checkboxRemember.checked)  {
                localStorage.setItem("rememberedUsername", username);
                localStorage.setItem("rememberChecked", "true");
            } else {
                localStorage.removeItem("rememberedUsername");
                localStorage.removeItem("rememberChecked");
            }


            setTimeout(() => {
                window.location.href = "../admin/dashboardAdmin.html";
            }, 1000);
            return;
        }

        // user đăng ký
        const users = JSON.parse(localStorage.getItem("users")) || [];
        const user = users.find(
            (u) => u.username === username && u.password === password
        );

        if (user) {
            showMessage("Đăng nhập thành công! Đang chuyển hướng...", "success");
            localStorage.setItem("currentUser", JSON.stringify(user));

            if (checkboxRemember && checkboxRemember.checked) {
                localStorage.setItem("rememberedUsername", username);
                localStorage.setItem("rememberChecked", "true");
            } else {
                localStorage.removeItem("rememberedUsername");
                localStorage.removeItem("rememberChecked");
            }
            setTimeout(() => {
                window.location.href = "home.html";
            }, 1000);
        } else {
            // Kiểm tra xem username có tồn tại không
            const userExists = users.find((u) => u.username === username);
                showMessage("Tên đăng nhập hoặc mật khẩu không đúng!", "error");
                usernameInput.classList.add("error");
                passwordInput.classList.add("error");

        }
    });

    // Xóa thông báo và error khi người dùng bắt đầu nhập
    usernameInput.addEventListener("input", function() {
        if (this.classList.contains("error")) {
            this.classList.remove("error");
            hideMessage();
        }
    });

    passwordInput.addEventListener("input", function() {
        if (this.classList.contains("error")) {
            this.classList.remove("error");
            hideMessage();
        }

    });
});
