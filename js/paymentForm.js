document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector(".payment-form");
    const btnSubmit = document.querySelector(".btn");

    // Lấy các input
    const paymentOptions = document.querySelectorAll("input[name='payment']");
    const cardName = document.getElementById("card_name");
    const cardNumber = document.getElementById("card_number");
    const yearSelect = document.getElementById("year");
    const monthSelect = document.getElementById("month");
    const cvvInput = document.getElementById("cvv2");

    // --- Tự động format số thẻ: 0000 0000 0000 0000 ---
    cardNumber.addEventListener("input", () => {
        let value = cardNumber.value.replace(/\D/g, "").substring(0, 16);
        cardNumber.value = value.replace(/(\d{4})(?=\d)/g, "$1 ");
    });

    // --- Xử lý khi nhấn Gửi Thanh Toán ---
    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const selectedMethod = [...paymentOptions].find(opt => opt.checked);
        if (!selectedMethod) {
            showAlert("Vui lòng chọn phương thức thanh toán!");
            return;
        }

        if (selectedMethod.value === "card") {
            if (cardName.value.trim() === "" ||
                cardNumber.value.trim() === "" ||
                yearSelect.value === "-" ||
                monthSelect.value === "-" ||
                cvvInput.value.trim() === "") {
                showAlert("Vui lòng nhập đầy đủ thông tin thẻ!");
                return;
            }

            const cardNumOnly = cardNumber.value.replace(/\s+/g, '');
            if (!/^\d{16}$/.test(cardNumOnly)) {
                showAlert("Số thẻ phải gồm 16 chữ số!");
                return;
            }

            if (!/^\d{3}$/.test(cvvInput.value)) {
                showAlert("Mã CVV phải gồm 3 chữ số!");
                return;
            }
        }

        // --- Giả lập xử lý thanh toán ---
        showAlert("Cập nhập thành công!", "success");

        setTimeout(() => {
            window.location.href = "thankyou.html"; // Trang cảm ơn
        }, 2000);
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
        setTimeout(() => (alert.style.opacity = "1"), 10);
        setTimeout(() => {
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 2500);
    }
});
