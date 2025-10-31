document.addEventListener("DOMContentLoaded", function () {
    const orderStatus = document.getElementById("order-status");
    const timelineSteps = document.querySelectorAll(".timeline .step");
    const btnCancel = document.getElementById("btn-request-cancel");
    const btnReorder = document.getElementById("btn-reorder");

    // Danh sách thứ tự các bước trong timeline
    const stepOrder = ["ordered", "confirmed", "shipped", "delivered"];

    /** Cập nhật trạng thái đơn hàng (hiển thị + timeline) */
    function updateOrderStatus(status) {
        orderStatus.textContent = status;
        orderStatus.style.color = "#222";

        switch (status) {
            case "Đã đặt":
                setActiveStep("ordered");
                break;
            case "Đã xác nhận":
                setActiveStep("confirmed");
                break;
            case "Đang giao":
                setActiveStep("shipped");
                break;
            case "Đã giao":
                setActiveStep("delivered");
                break;
            case "Đã hủy":
                resetTimeline();
                orderStatus.style.color = "#c00";
                break;
            default:
                console.warn("Trạng thái không hợp lệ:", status);
        }
    }

    /** Đặt các bước active trong timeline */
    function setActiveStep(stepName) {
        const currentIndex = stepOrder.indexOf(stepName);

        timelineSteps.forEach((step, index) => {
            // Sáng tất cả các bước từ đầu đến current
            if (index <= currentIndex) {
                step.classList.add("active");
                step.style.opacity = "1";
            } else {
                step.classList.remove("active");
                step.style.opacity = "0.5";
            }
        });
    }

    /** Làm mờ toàn bộ timeline khi hủy đơn */
    function resetTimeline() {
        timelineSteps.forEach(step => {
            step.classList.remove("active");
            step.style.opacity = "0.5";
        });
    }

    /** Xử lý khi người dùng yêu cầu hủy đơn */
    function handleCancelOrder() {
        const confirmCancel = confirm("Bạn có chắc muốn yêu cầu hủy đơn hàng này không?");
        if (!confirmCancel) return;

        updateOrderStatus("Đã hủy");
        btnCancel.disabled = true;
        btnCancel.textContent = "Đã yêu cầu hủy";
        alert("Yêu cầu hủy đơn hàng đã được gửi!");
    }

    /** Xử lý khi người dùng chọn Mua lại */
    function handleReorder() {
        alert("Các sản phẩm trong đơn hàng đã được thêm lại vào giỏ hàng!");
    }

    // ===== GẮN SỰ KIỆN =====
    btnCancel?.addEventListener("click", handleCancelOrder);
    btnReorder?.addEventListener("click", handleReorder);

    updateOrderStatus("Đang giao");
});