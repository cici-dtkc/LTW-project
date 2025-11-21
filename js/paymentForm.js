document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("bankForm");

    const bankSelect = document.getElementById("bankSelect");
    const accountNumber = document.getElementById("accountNumber");
    const ownerName = document.getElementById("ownerName");

    const btnBack = document.getElementById("btnBack");
    const btnSubmit = document.getElementById("btnSubmit");
    const menuAccountMain = document.getElementById("menuAccountMain");
    const accountSubmenu = document.getElementById("accountSubmenu");

    let isEditMode = false;

    /* ======================================================
       1. Khởi tạo: Disable các trường và lưu giá trị hiện tại
    ====================================================== */
    function disableFields() {
        bankSelect.disabled = true;
        accountNumber.disabled = true;
        ownerName.disabled = true;
        isEditMode = false;
        btnBack.textContent = "Sửa";
    }

    function enableFields() {
        bankSelect.disabled = false;
        accountNumber.disabled = false;
        ownerName.disabled = false;
        isEditMode = true;
        btnBack.textContent = "Hủy";
    }

    // Lưu/khôi phục giá trị chỉ trong phiên hiện tại
    const originalValues = {
        bank: bankSelect.value || "",
        accountNumber: accountNumber.value || "",
        ownerName: ownerName.value || ""
    };

    function snapshotCurrentValues() {
        originalValues.bank = bankSelect.value;
        originalValues.accountNumber = accountNumber.value;
        originalValues.ownerName = ownerName.value;
    }

    function restoreSnapshotValues() {
        bankSelect.value = originalValues.bank;
        accountNumber.value = originalValues.accountNumber;
        ownerName.value = originalValues.ownerName;
    }

    // Khởi tạo: disable và lưu lại giá trị ban đầu
    disableFields();
    snapshotCurrentValues();


    /* ======================================================
       2. Chỉ cho nhập số ở Số tài khoản
    ====================================================== */
    accountNumber.addEventListener("input", () => {
        if (isEditMode) {
            accountNumber.value = accountNumber.value.replace(/\D/g, "");
        }
    });


    /* ======================================================
       3. Validate tên chủ tài khoản (IN HOA, KHÔNG DẤU)
    ====================================================== */
    ownerName.addEventListener("input", () => {
        if (isEditMode) {
            ownerName.value = ownerName.value
                .toUpperCase()
                .replace(/[^A-Z ]/g, ""); // chỉ A-Z và khoảng trắng
        }
    });


    /* ======================================================
       4. Nút SỬA / HỦY
    ====================================================== */
    btnBack.addEventListener("click", (e) => {
        e.preventDefault();

        if (!isEditMode) {
            // Chuyển sang chế độ chỉnh sửa
            snapshotCurrentValues(); // lưu lại giá trị trước khi sửa
            enableFields();
            accountNumber.focus();
        } else {
            // Hủy chỉnh sửa, khôi phục dữ liệu
            restoreSnapshotValues();
            disableFields();
        }
    });


    /* ======================================================
       5. SUBMIT FORM - Lưu dữ liệu
    ====================================================== */
    form.addEventListener("submit", (e) => {
        e.preventDefault();

        if (!isEditMode) {
            showAlert("Vui lòng nhấn nút 'Sửa' để chỉnh sửa thông tin!", "error");
            return;
        }

        // Validate
        if (bankSelect.value === "") {
            showAlert("Vui lòng chọn ngân hàng!", "error");
            bankSelect.focus();
            return;
        }

        if (accountNumber.value.trim() === "" || accountNumber.value.length < 6) {
            showAlert("Số tài khoản phải có ít nhất 6 chữ số!", "error");
            accountNumber.focus();
            return;
        }

        if (ownerName.value.trim() === "") {
            showAlert("Vui lòng nhập tên chủ tài khoản!", "error");
            ownerName.focus();
            return;
        }

        // Cập nhật snapshot và disable lại
        snapshotCurrentValues();
        disableFields();
        showAlert("Lưu thông tin thành công!", "success");
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

    // ===========================
// SIDEBAR submenu toggle
// ===========================


    if (menuAccountMain && accountSubmenu) {
        accountSubmenu.classList.add("open");
        menuAccountMain.addEventListener("click", (e) => {
            e.preventDefault();
            accountSubmenu.classList.toggle("open");
        });
    }

});