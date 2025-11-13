document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("bankForm");

    const bankSelect = document.getElementById("bankSelect");
    const branchSelect = document.getElementById("branchSelect");
    const accountNumber = document.getElementById("accountNumber");
    const ownerName = document.getElementById("ownerName");
    const defaultAccount = document.getElementById("defaultAccount");

    const btnBack = document.getElementById("btnBack");


    /* ======================================================
       1. Chỉ cho nhập số ở Số tài khoản
    ====================================================== */
    accountNumber.addEventListener("input", () => {
        accountNumber.value = accountNumber.value.replace(/\D/g, "");
    });


    /* ======================================================
       2. Validate tên chủ tài khoản (IN HOA, KHÔNG DẤU)
    ====================================================== */
    ownerName.addEventListener("input", () => {
        ownerName.value = ownerName.value
            .toUpperCase()
            .replace(/[^A-Z ]/g, ""); // chỉ A-Z và khoảng trắng
    });


    /* ======================================================
       3. SUBMIT FORM
    ====================================================== */
    form.addEventListener("submit", (e) => {
        e.preventDefault();

        if (bankSelect.value === "") {
            showAlert("Vui lòng chọn ngân hàng!");
            return;
        }

        if (branchSelect.value === "") {
            showAlert("Vui lòng chọn chi nhánh!");
            return;
        }

        if (accountNumber.value.trim() === "" || accountNumber.value.length < 6) {
            showAlert("Số tài khoản không hợp lệ!");
            return;
        }

        if (ownerName.value.trim() === "") {
            showAlert("Vui lòng nhập tên chủ tài khoản!");
            return;
        }

        showAlert("Thêm tài khoản thành công!", "success");
    });


    /* ======================================================
       4. Nút TRỞ LẠI
    ====================================================== */
    btnBack.addEventListener("click", () => {
        window.history.back();
    });


    /* ======================================================
       5. HÀM THÔNG BÁO
    ====================================================== */
    function showAlert(message, type = "error") {
        const old = document.querySelector(".alert");
        if (old) old.remove();

        const alert = document.createElement("div");
        alert.className = "alert";
        alert.textContent = message;

        Object.assign(alert.style, {
            position: "fixed",
            top: "20px",
            left: "50%",
            transform: "translateX(-50%)",
            padding: "12px 20px",
            background: type === "success" ? "#28a745" : "#dc3545",
            color: "white",
            borderRadius: "6px",
            boxShadow: "0 2px 6px rgba(0,0,0,0.25)",
            opacity: "0",
            transition: "opacity .4s",
            zIndex: "9999",
        });

        document.body.appendChild(alert);

        setTimeout(() => (alert.style.opacity = "1"), 10);
        setTimeout(() => {
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 2500);
    }
    const menuAccountMain = document.getElementById("menuAccountMain");
    const accountSubmenu = document.getElementById("accountSubmenu");

    // Toggle submenu
    menuAccountMain.addEventListener("click", (e) => {
        e.preventDefault();
        accountSubmenu.classList.toggle("open");
    });

});
