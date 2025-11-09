document.addEventListener("DOMContentLoaded", function () {
    const btnCancel = document.getElementById("btn-request-cancel");
    const actionsContainer = document.querySelector(".actions");

    /** Xử lý khi người dùng yêu cầu hủy đơn */
    function handleCancelOrder() {
        const confirmCancel = confirm("Bạn có chắc muốn yêu cầu hủy đơn hàng này không?");
        if (!confirmCancel) return;

        // Cập nhật trạng thái hiển thị
        const orderStatus = document.getElementById("order-status");
        const paidText = document.getElementById("paid-text");

        orderStatus.textContent = "Đã hủy";
        orderStatus.style.color = "#c00";
        paidText.textContent = "Đã hủy";

        // Cập nhật nút hủy
        btnCancel.disabled = true;
        btnCancel.textContent = "Đã yêu cầu hủy";
        btnCancel.classList.add("disabled-btn");

        // Tạo nút "Mua lại"
        const btnReorder = document.createElement("button");
        btnReorder.textContent = "Mua lại";
        btnReorder.className = "btn btn-ghost";
        btnReorder.id = "btn-reorder";
        btnReorder.addEventListener("click", handleReorder);

        // Thêm nút vào giao diện, ngay sau nút hủy
        actionsContainer.appendChild(btnReorder);

        alert("Yêu cầu hủy đơn hàng đã được gửi!");
    }

    /** Xử lý khi người dùng chọn Mua lại */
    function handleReorder() {
        window.location.href = "cart.html";
    }

    // Gắn sự kiện
    btnCancel?.addEventListener("click", handleCancelOrder);
});
