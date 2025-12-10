document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ voucherAdmin.js loaded");

    const modal = document.getElementById("promoModal");
    const promoForm = document.getElementById("promoForm");
    const btnClose = document.getElementById("btnCloseModal");
    const btnOpen = document.getElementById("btnOpenModal");

    /* ================= OPEN ADD FORM ================= */
    if (btnOpen) {
        btnOpen.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            console.log("🔵 Opening ADD modal...");

            if (!modal) {
                console.error("❌ Modal not found!");
                return;
            }

            if (!promoForm) {
                console.error("❌ Form not found!");
                return;
            }

            promoForm.reset();
            promoForm.querySelectorAll("input, select").forEach((el) => {
                el.disabled = false;
                el.readOnly = false;
            });

            const formAction = document.getElementById("formAction");
            const editId = document.getElementById("editId");

            if (formAction) {
                formAction.value = "addVoucher";
                console.log("✅ Form action set to:", formAction.value);
            } else {
                console.error("❌ formAction element not found!");
            }

            if (editId) {
                editId.value = "";
            }

            const modalTitle = document.querySelector("#promoModal h3");
            if (modalTitle) {
                modalTitle.innerText = "Thêm khuyến mãi mới";
            }

            modal.classList.add("show");
            // Force display with inline style to ensure it shows
            modal.style.display = "flex";
            console.log("✅ Modal opened, classes:", modal.className);
            console.log(
                "✅ Modal display style:",
                window.getComputedStyle(modal).display
            );
        });
    } else {
        console.error("❌ btnOpenModal not found!");
    }

    /* ================= CLOSE MODAL ================= */
    function closeModal() {
        modal.classList.remove("show");
        modal.style.display = "none";
    }

    if (btnClose) {
        btnClose.addEventListener("click", function () {
            closeModal();
        });
    }

    window.addEventListener("click", function (e) {
        if (e.target === modal) {
            closeModal();
        }
    });

    // Prevent modal from closing when clicking inside modal-content
    const modalContent = document.querySelector(".modal-content");
    if (modalContent) {
        modalContent.addEventListener("click", function (e) {
            e.stopPropagation();
        });
    }

    /* ================= EDIT FUNCTION ================= */
    window.editRow = function (btn) {
        console.log("🟡 Opening EDIT modal...");

        if (!btn) {
            console.error("❌ Button is null!");
            return;
        }

        if (!modal) {
            console.error("❌ Modal not found!");
            return;
        }

        const row = btn.closest("tr");
        if (!row) {
            console.error("❌ Row not found!");
            return;
        }

        promoForm.querySelectorAll("input, select").forEach((el) => {

            el.disabled = false;
            el.readOnly = false;
        });

        document.getElementById("formAction").value = "update";
        document.getElementById("editId").value = btn.dataset.id;

        document.getElementById("promoCode").value =
            row.children[0].innerText.trim();

        const typeText = row.children[1].innerText.trim();
        document.getElementById("promoType").value =
            typeText === "Phần trăm" ? "1" : typeText === "Tiền mặt" ? "2" : "3";

        const discountText = row.children[2].innerText.trim();

        const discountValue = discountText.replace(/[^\d]/g, "");
        document.getElementById("discountValue").value = discountValue || 0;

        // Parse max discount and min order (remove commas and currency symbols)
        const maxDiscountText = row.children[3].innerText.replace(/[^\d]/g, "");
        document.getElementById("maxDiscount").value = maxDiscountText || 0;

        const minOrderText = row.children[4].innerText.replace(/[^\d]/g, "");
        document.getElementById("minOrder").value = minOrderText || 0;

        document.getElementById("quantity").value =
            parseInt(row.children[5].innerText.trim()) || 1;

        const startDateCell = row.children[6];
        const startDateText =
            startDateCell.getAttribute("data-date") || startDateCell.innerText.trim();
        document.getElementById("startDate").value = startDateText;

        const endDateCell = row.children[7];
        const endDateText =
            endDateCell.getAttribute("data-date") || endDateCell.innerText.trim();
        document.getElementById("endDate").value = endDateText;

        document.querySelector("#promoModal h3").innerText = "Cập nhật khuyến mãi";
        modal.classList.add("show");
        // Force display with inline style to ensure it shows
        modal.style.display = "flex";

        console.log(
            "✅ Edit modal opened, action:",
            document.getElementById("formAction").value
        );
        console.log(
            "✅ Modal display style:",
            window.getComputedStyle(modal).display
        );
    };

    /* ================= FORM VALIDATION ================= */
    if (promoForm) {
        promoForm.addEventListener("submit", function (e) {
            console.log("📤 Submitting form...");

            const action = document.getElementById("formAction").value;
            const id = document.getElementById("editId").value;

            console.log("Action:", action);
            console.log("ID:", id);

            // Validate action is set
            if (!action) {
                e.preventDefault();
                alert("Lỗi: Action không được xác định!");
                console.error("❌ Form action is empty!");
                return false;
            }

            // Validate dates
            const startDateValue = document.getElementById("startDate").value;
            const endDateValue = document.getElementById("endDate").value;

            if (!startDateValue || !endDateValue) {
                e.preventDefault();
                alert("Vui lòng nhập đầy đủ ngày bắt đầu và ngày kết thúc!");
                return false;
            }

            const startDate = new Date(startDateValue);
            const endDate = new Date(endDateValue);

            if (endDate < startDate) {
                e.preventDefault();
                alert("Ngày kết thúc phải sau ngày bắt đầu!");
                return false;
            }

            // Validate discount
            const discountType = document.getElementById("promoType").value;
            const discountValue = parseInt(
                document.getElementById("discountValue").value
            );

            if (isNaN(discountValue) || discountValue <= 0) {
                e.preventDefault();
                alert("Mức giảm phải là số lớn hơn 0!");
                return false;
            }

            if (discountType === "1" && discountValue > 100) {
                e.preventDefault();
                alert("Giảm theo % không được vượt quá 100%!");
                return false;
            }

            console.log("✅ Form validation passed, submitting...");
            if (action === "addVoucher") {
                alert("🎉 Thêm khuyến mãi thành công!");
            }
            else {
                alert("🎉 Sửa khuyến mãi thành công!");
            }

            return true;


        });
    }
});