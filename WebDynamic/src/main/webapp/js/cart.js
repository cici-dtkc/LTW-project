
document.addEventListener("DOMContentLoaded", function () {
    const selectAll = document.getElementById("selectAll");
    const itemCheckboxes = document.querySelectorAll(".select-item");
    const subTotalDisplay = document.getElementById("sub-total");
    const checkoutBtn = document.querySelector(".checkout-btn");

    // 1. Hàm định dạng tiền tệ chuẩn VN
    function formatVND(amount) {
        return new Intl.NumberFormat('vi-VN').format(amount) + "đ";
    }

    // 2. Hàm tính tổng tiền dựa trên các checkbox được tích
    function updateTotalPrice() {
        let total = 0;
        let hasChecked = false;

        // Chỉ duyệt qua các checkbox đang được chọn
        const checkedItems = document.querySelectorAll(".select-item:checked");

        checkedItems.forEach(checkbox => {
            // Lấy giá trị từ thuộc tính data-price đã thêm ở JSP
            total += parseFloat(checkbox.dataset.price || 0);
            hasChecked = true;
        });

        // Cập nhật hiển thị
        subTotalDisplay.innerText = formatVND(total);

        // 3. Vô hiệu hóa nút thanh toán nếu không tích mục nào
        if (checkoutBtn) {
            checkoutBtn.disabled = !hasChecked;
            checkoutBtn.style.opacity = hasChecked ? "1" : "0.5"; // Thêm hiệu ứng mờ nếu muốn
        }
    }

    // 4. Sự kiện cho checkbox "Chọn tất cả"
    if (selectAll) {
        selectAll.addEventListener("change", function () {
            itemCheckboxes.forEach(cb => cb.checked = selectAll.checked);
            updateTotalPrice();
        });
    }

    // 5. Sự kiện cho từng checkbox sản phẩm
    itemCheckboxes.forEach(checkbox => {
        checkbox.addEventListener("change", function () {
            // Nếu có 1 ô bị bỏ tích, ô "Chọn tất cả" phải bỏ tích theo
            if (!this.checked) {
                if(selectAll) selectAll.checked = false;
            }
            // Nếu tất cả ô con được tích, tự động tích ô "Chọn tất cả"
            else {
                const allChecked = document.querySelectorAll(".select-item:checked").length === itemCheckboxes.length;
                if(selectAll) selectAll.checked = allChecked;
            }
            updateTotalPrice();
        });
    });


    updateTotalPrice();
});


function updateQty(id, delta) {
    window.location.href = "cart?action=update&vcId=" + id + "&delta=" + delta;
}

function removeItem(id) {
    if(confirm('Bạn có chắc muốn xóa sản phẩm này?')) {
        window.location.href = "cart?action=remove&vcId=" + id;
    }
}