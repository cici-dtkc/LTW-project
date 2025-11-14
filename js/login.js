document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("login-form");
    const usernameInput = document.getElementById("login-username");
    const passwordInput = document.getElementById("login-password");
    const checkboxRemember = document.getElementById("remember-checkbox");

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
        checkboxRemember.checked = true;
    }

    // Xử lý đăng nhập
    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const username = usernameInput.value.trim();
        const password = passwordInput.value.trim();

        if (!username || !password) return;

        // admin login
        if (username === "admin" && password === "123456") {
            if (checkboxRemember.checked) {
                localStorage.setItem("rememberedUsername", username);
                localStorage.setItem("rememberChecked", "true");
            } else {
                localStorage.removeItem("rememberedUsername");
                localStorage.removeItem("rememberChecked");
            }

            window.location.href = "../admin/dashboardAdmin.html";
            return;
        }

        // user đăng ký
        const users = JSON.parse(localStorage.getItem("users")) || [];
        const user = users.find(
            (u) => u.username === username && u.password === password
        );

        if (user) {
            localStorage.setItem("currentUser", JSON.stringify(user));

            if (checkboxRemember.checked) {
                localStorage.setItem("rememberedUsername", username);
                localStorage.setItem("rememberChecked", "true");
            } else {
                localStorage.removeItem("rememberedUsername");
                localStorage.removeItem("rememberChecked");
            }

            window.location.href = "home.html";
        }
    });
});
