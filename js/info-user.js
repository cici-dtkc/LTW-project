// --- info-user.js ---

document.addEventListener("DOMContentLoaded", function () {
    const btnLogout = document.getElementById("btn-logout");
    const btnEdit = document.getElementById("btn-edit");

    const fullNameInput = document.getElementById("fullName");
    const passwordFields = document.getElementById("password-fields");
    const currentPasswordInput = document.getElementById("currentPassword");
    const newPasswordInput = document.getElementById("newPassword");
    const confirmPasswordInput = document.getElementById("confirmPassword");
    const emailInput = document.getElementById("emailInput");
    const phoneInput = document.getElementById("phoneInput");
    const emailError = document.getElementById("emailError");
    const phoneError = document.getElementById("phoneError");
    const currentPwError = document.getElementById("currentPwError");
    const newPwError = document.getElementById("newPwError");
    const confirmPwError = document.getElementById("confirmPwError");

    // Mocked user data (replace with real data source when available)
    const user = {
        username: "abc",
        name: "Admin",
        email: "admin@example.com",
        phone: "+84 12345678",
        password: "123456" // demo only
    };

    // Initialize UI
    document.getElementById("username-view").textContent = user.username;
    emailInput.value = user.email;
    phoneInput.value = user.phone;
    fullNameInput.value = user.name;

    // Logout
    btnLogout.addEventListener("click", function () {
        window.location.href = "./login.html";
    });

    // Toggle edit / save
    let editing = false;
    btnEdit.addEventListener("click", function () {
        if (!editing) {
            editing = true;
            btnEdit.textContent = "Lưu";
            fullNameInput.disabled = false;
            emailInput.disabled = false;
            phoneInput.disabled = false;
            passwordFields.classList.remove("hidden");
            fullNameInput.focus();
            return;
        }

        // Validate and save
        clearErrors();
        const name = fullNameInput.value.trim();
        if (name.length === 0) { setFieldError(fullNameInput, "Tên không được để trống"); return; }

        const currentPw = currentPasswordInput.value;
        const newPw = newPasswordInput.value;
        const confirmPw = confirmPasswordInput.value;

        if (!isValidEmail(emailInput.value)) { emailError.textContent = "Email không hợp lệ"; return; }
        if (!isValidPhone(phoneInput.value)) { phoneError.textContent = "Số điện thoại không hợp lệ"; return; }

        if (currentPw || newPw || confirmPw) {
            if (currentPw !== user.password) { currentPwError.textContent = "Mật khẩu hiện tại không đúng"; return; }
            if (newPw.length < 6) { newPwError.textContent = "Mật khẩu mới phải từ 6 ký tự"; return; }
            if (newPw !== confirmPw) { confirmPwError.textContent = "Xác nhận mật khẩu không khớp"; return; }
            user.password = newPw;
        }

        user.name = name;
        user.email = emailInput.value.trim();
        user.phone = phoneInput.value.trim();

        // Reset UI state
        editing = false;
        btnEdit.textContent = "Chỉnh sửa thông tin";
        fullNameInput.disabled = true;
        emailInput.disabled = true;
        phoneInput.disabled = true;
        passwordFields.classList.add("hidden");
        currentPasswordInput.value = "";
        newPasswordInput.value = "";
        confirmPasswordInput.value = "";
    });

    function clearErrors() {
        emailError.textContent = "";
        phoneError.textContent = "";
        currentPwError.textContent = "";
        newPwError.textContent = "";
        confirmPwError.textContent = "";
        removeFieldError(fullNameInput);
    }

    function setFieldError(input, message) {
        input.classList.add("invalid");
        // try to place the message in a sibling .error span if exists
        const row = input.parentElement;
        const err = row.querySelector('.error');
        if (err) err.textContent = message;
    }
    function removeFieldError(input) {
        input.classList.remove("invalid");
    }
});

// masking helpers removed, showing inputs instead

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isValidPhone(phone) {
    return /^[0-9+()\s-]{8,20}$/.test(phone);
}
