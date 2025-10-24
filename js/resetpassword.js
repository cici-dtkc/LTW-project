
// Hỗ trợ luồng: forgotpassword.html -> changepassword.html -> login.html

// ----------- Trang forgotpassword.html -----------
var forgotForm = document.querySelector('#forgot-password form');

if (forgotForm) {
    forgotForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const emailInput = forgotForm.querySelector('input[type="email"]');
        const email = emailInput.value.trim();

        if (!email) {
            alert("Vui lòng nhập email của bạn!");
            return;
        }

        // Sinh mã 6 chữ số và lưu tạm trong sessionStorage
        const code = Math.floor(100000 + Math.random() * 900000).toString();
        sessionStorage.setItem('resetEmail', email);
        sessionStorage.setItem('resetCode', code);
        sessionStorage.setItem('resetCodeCreatedAt', Date.now().toString());

        // Giả lập gửi email — để tiện test hiển thị luôn mã trong alert (bỏ phần này khi chạy thực tế)
        alert(`Mã xác nhận đã được gửi tới ${email}. Mã (thử): ${code}`);

        // Chuyển sang trang nhập mã (lưu ý: file tên hiện tại là changepassword.html)
        window.location.href = 'changepassword.html';
    });
}


// ----------- Trang changepasswsord.html -----------
const verifyStep = document.getElementById('verify-step');
const confirmStep = document.getElementById('confirm-step');
const resetStep = document.getElementById('reset-step');

if (verifyStep) {
    // Hiển thị email đã lưu (nếu có)
    const savedEmail = sessionStorage.getItem('resetEmail');
    const infoBold = verifyStep.querySelector('p b');
    if (!savedEmail) {
        // Nếu người dùng vào trực tiếp trang verif không qua form thì quay lại nhập email
        alert('Vui lòng nhập email trước khi đặt lại mật khẩu.');
        window.location.href = 'forgotpassword.html';
    } else if (infoBold) {
        infoBold.textContent = savedEmail;
    }

    const verifyBtn = document.getElementById('verify-btn');
    const confirmBtn = document.getElementById('confirm-btn');
    const updateBtn = document.getElementById('update-btn');
    const resendLink = document.getElementById('resend-link');

    //mã hiệu lực trong 1 phút
    let resendTimerInterval = null;
    function formatTime(s) {
        const m = Math.floor(s / 60).toString().padStart(2, '0');
        const ss = (s % 60).toString().padStart(2, '0');
        return `${m}:${ss}`;
    }

    function setResendDisabled(remainingSec) {
        if (!resendLink) return;
        resendLink.classList.add('disabled');
        resendLink.setAttribute('aria-disabled', 'true');
        resendLink.style.pointerEvents = 'none';
        resendLink.textContent = `Gửi lại sau ${formatTime(remainingSec)}`;
        // clear any existing interval
        if (resendTimerInterval) clearInterval(resendTimerInterval);
        resendTimerInterval = setInterval(() => {
            remainingSec -= 1;
            if (remainingSec <= 0) {
                clearInterval(resendTimerInterval);
                resendTimerInterval = null;
                resendLink.classList.remove('disabled');
                resendLink.removeAttribute('aria-disabled');
                resendLink.style.pointerEvents = '';
                resendLink.textContent = 'Gửi lại email';
            } else {
                resendLink.textContent = `Gửi lại sau ${formatTime(remainingSec)}`;
            }
        }, 1000);
    }

    // Nếu đã có mã vừa được tạo trong vòng 60s -> bắt đầu cooldown với phần thời gian còn lại
    (function initResendState() {
        if (!resendLink) return;
        const createdAt = parseInt(sessionStorage.getItem('resetCodeCreatedAt') || '0', 10);
        if (!createdAt) {
            // không có mã -> link sẵn sàng
            resendLink.classList.remove('disabled');
            resendLink.removeAttribute('aria-disabled');
            resendLink.style.pointerEvents = '';
            resendLink.textContent = 'Gửi lại email';
            return;
        }
        const elapsed = Math.floor((Date.now() - createdAt) / 1000);
        const cooldown = 60; // 60 seconds
        if (elapsed < cooldown) {
            setResendDisabled(cooldown - elapsed);
        } else {
            // hết cooldown, cho bấm nhưng đừng tự động tạo mã mới
            resendLink.classList.remove('disabled');
            resendLink.removeAttribute('aria-disabled');
            resendLink.style.pointerEvents = '';
            resendLink.textContent = 'Gửi lại email';
        }
    })();

    // Xử lý click gửi lại: nếu đang disabled thì không làm gì; nếu được bấm -> tạo mã mới, lưu, alert, bắt đầu cooldown
    if (resendLink) {
        resendLink.addEventListener('click', function (e) {
            e.preventDefault();
            // nếu đang trong cooldown, chặn (safety)
            if (resendLink.classList.contains('disabled')) return;

            const email = sessionStorage.getItem('resetEmail');
            if (!email) {
                alert('Không tìm thấy email. Vui lòng nhập lại email.');
                window.location.href = 'forgotpassword.html';
                return;
            }

            // Sinh mã mới và cập nhật createdAt
            const newCode = Math.floor(100000 + Math.random() * 900000).toString();
            sessionStorage.setItem('resetCode', newCode);
            sessionStorage.setItem('resetCodeCreatedAt', Date.now().toString());

            // Hiển thị mã (chỉ test)
            alert(`Mã xác nhận mới đã được gửi tới ${email}. Mã (thử): ${newCode}`);

            // Bắt đầu lại cooldown 60s
            setResendDisabled(60);
        });
    }

    // Nhập mã xác minh
    verifyBtn.addEventListener('click', function () {
        const codeInputs = document.querySelectorAll('#verify-step .code-inputs input');
        let code = '';
        codeInputs.forEach(input => code += input.value.trim());

        if (code.length < 6) {
            alert('Vui lòng nhập đủ 6 chữ số mã xác nhận!');
            return;
        }

        const storedCode = sessionStorage.getItem('resetCode');
        const createdAt = parseInt(sessionStorage.getItem('resetCodeCreatedAt') || '0', 10);
        // mã hết hạn sau 15 phút
        const expired = Date.now() - createdAt > 15 * 60 * 1000;

        if (!storedCode || expired) {
            alert('Mã xác nhận không hợp lệ hoặc đã hết hạn. Vui lòng gửi lại mã.');
            // xóa và quay về trang nhập email
            sessionStorage.removeItem('resetCode');
            sessionStorage.removeItem('resetCodeCreatedAt');
            window.location.href = 'forgotpassword.html';
            return;
        }

        if (code !== storedCode) {
            alert('Mã xác nhận không đúng. Vui lòng kiểm tra lại.');
            return;
        }

        // Xác minh thành công -> chuyển sang bước xác nhận
        verifyStep.classList.add('hidden');
        confirmStep.classList.remove('hidden');
    });

    // Bấm "Xác nhận" → sang bước nhập mật khẩu
    confirmBtn.addEventListener('click', function () {
        confirmStep.classList.add('hidden');
        resetStep.classList.remove('hidden');
    });

    // Cập nhật mật khẩu mới
    updateBtn.addEventListener('click', function () {
        const newPassInput = document.getElementById('new-pass');
        const confirmPassInput = document.getElementById('confirm-pass');
        const newPass = newPassInput ? newPassInput.value.trim() : '';
        const confirmPass = confirmPassInput ? confirmPassInput.value.trim() : '';

        if (!newPass || !confirmPass) {
            alert('Vui lòng nhập đầy đủ thông tin!');
            return;
        }

        if (newPass.length < 6) {
            alert('Mật khẩu mới cần ít nhất 6 ký tự.');
            return;
        }

        if (newPass !== confirmPass) {
            alert('Mật khẩu xác nhận không khớp!');
            return;
        }

        // Giả lập lưu mật khẩu mới (thực tế: gọi API backend ở đây)
        // Xóa thông tin tạm và chuyển về trang đăng nhập
        sessionStorage.removeItem('resetCode');
        sessionStorage.removeItem('resetCodeCreatedAt');
        sessionStorage.removeItem('resetEmail');

        alert('Cập nhật mật khẩu thành công!');
        window.location.href = 'login.html';
    });

    // Tự động focus qua ô kế tiếp khi nhập mã
    const codeInputs = document.querySelectorAll('#verify-step .code-inputs input');
    codeInputs.forEach((input, index) => {
        input.addEventListener('input', () => {
            if (input.value && index < codeInputs.length - 1) {
                codeInputs[index + 1].focus();
            }
        });
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && !input.value && index > 0) {
                codeInputs[index - 1].focus();
            }
        });
    });

    // Ẩn / hiện mật khẩu (icon mắt)
    // đảm bảo trạng thái icon ban đầu
    document.querySelectorAll('.toggle').forEach(toggle => {
        const eye = toggle.querySelector('.fa-eye');
        const eyeSlash = toggle.querySelector('.fa-eye-slash');
        if (eye) eye.style.display = 'inline';
        if (eyeSlash) eyeSlash.style.display = 'none';

        toggle.addEventListener('click', () => {
            const input = toggle.parentElement.querySelector('input');
            const eye = toggle.querySelector('.fa-eye');
            const eyeSlash = toggle.querySelector('.fa-eye-slash');

            if (!input) return;

            if (input.type === 'password') {
                input.type = 'text';
                if (eye) eye.style.display = 'none';
                if (eyeSlash) eyeSlash.style.display = 'inline';
            } else {
                input.type = 'password';
                if (eye) eye.style.display = 'inline';
                if (eyeSlash) eyeSlash.style.display = 'none';
            }
        });
    });
}
