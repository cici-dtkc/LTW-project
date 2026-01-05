document.addEventListener("DOMContentLoaded", function () {
    const registerForm = document.querySelector(".form-regis");
    const username = document.getElementById("username");
    const password = document.getElementById("password");
    const confirmPass = document.getElementById("confirm");
    const email = document.getElementById("email");
    const firstName = document.getElementById("fname");
    const lastName = document.getElementById("lname");
    const errorMessageDiv = document.getElementById("error-message");

    registerForm.addEventListener("submit", function (e) {
        e.preventDefault(); // Ngăn reload trang

        // --- Lấy giá trị input ---
        const usernameVal = username.value.trim();
        const passwordVal = password.value.trim();
        const confirmVal = confirmPass.value.trim();
        const emailVal = email.value.trim();
        const firstVal = firstName.value.trim();
        const lastVal = lastName.value.trim();

        // --- Reset lỗi trước ---
        errorMessageDiv.textContent = "";

        // --- Kiểm tra thông tin rỗng ---
        if (!usernameVal || !passwordVal || !emailVal || !firstVal || !lastVal) {
            errorMessageDiv.textContent = "Vui lòng nhập đầy đủ thông tin!";
            return;
        }

        // --- Kiểm tra mật khẩu khớp ---
        if (passwordVal !== confirmVal) {
            errorMessageDiv.textContent = "Mật khẩu và xác nhận mật khẩu không khớp!";
            return;
        }

        // --- Kiểm tra email hợp lệ ---
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailVal)) {
            errorMessageDiv.textContent = "Email không hợp lệ! Vui lòng nhập đúng định dạng.";
            email.focus();
            return;
        }

        // --- Kiểm tra mật khẩu mạnh ---
        const strongPasswordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/;
        if (!strongPasswordRegex.test(passwordVal)) {
            errorMessageDiv.textContent = "Mật khẩu phải ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt!";
            password.focus();
            return;
        }

        // --- Nếu không lỗi, submit form ---
        registerForm.submit();
    });
});
