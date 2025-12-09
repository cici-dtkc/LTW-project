document.addEventListener("DOMContentLoaded", function () {
    // Lấy các phần tử modal
    const modal = document.getElementById("promoModal");
    const promoForm = document.getElementById("promoForm");
    const btnClose = document.getElementById("btnCloseModal");
    const btnOpen = document.getElementById("btnOpenModal");

    // Lấy contextPath từ JSP
    const contextPath = document.body.getAttribute("data-context");

    // 🟢 Nút mở modal "Thêm mới"
    btnOpen.addEventListener("click", () => {
        document.querySelector("#promoModal h3").innerText = "Thêm khuyến mãi";
        promoForm.reset();
        document.getElementById("editId").value = "";
        promoForm.querySelector("input[name='action']").value = "addVoucher";
        modal.style.display = "flex";
    });

    //   Đóng modal
    btnClose.addEventListener("click", () => {
        modal.style.display = "none";
    });

    // Nhấn ngoài modal để đóng
    window.addEventListener("click", (e) => {
        if (e.target === modal) modal.style.display = "none";
    });

    //  Mở modal sửa
    window.openEditModal = function (btn) {
        document.querySelector("#promoModal h3").innerText = "Sửa khuyến mãi";
        promoForm.querySelector("input[name='action']").value = "updateVoucher";

        document.getElementById("editId").value = btn.dataset.id;
        document.getElementById("promoCode").value = btn.dataset.code;
        document.getElementById("promoType").value = btn.dataset.type;
        document.getElementById("discountValue").value = btn.dataset.discount;
        document.getElementById("maxDiscount").value = btn.dataset.max;
        document.getElementById("minOrder").value = btn.dataset.min;
        document.getElementById("quantity").value = btn.dataset.quantity;
        document.getElementById("startDate").value = btn.dataset.start;
        document.getElementById("endDate").value = btn.dataset.end;

        modal.style.display = "flex";
    };

    // Bật/Tắt trạng thái
    window.toggleVoucher = function (btn) {
        const id = btn.dataset.id;
        const status = parseInt(btn.dataset.status);

        fetch(`${contextPath}/admin/vouchers`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `action=toggleStatus&id=${id}&status=${status}`
        }).then(() => location.reload());
    };

    window.editRow = function (btn) {
        const tr = btn.closest("tr");
        const id = btn.dataset.id;

        // Lấy dữ liệu từ từng cột bảng
        const code = tr.children[0].innerText.trim();
        const type = tr.children[1].innerText.trim();
        const discount = tr.children[2].innerText.trim();
        const max = tr.children[3].innerText.trim();
        const min = tr.children[4].innerText.trim();
        const quantity = tr.children[5].innerText.trim();
        const start = tr.children[6].innerText.trim();
        const end = tr.children[7].innerText.trim();

        // Gán vào input
        tr.children[0].innerHTML = `<input value="${code}">`;

        tr.children[1].innerHTML = `
        <select>
            <option value="1" ${type === "Phần trăm" ? "selected" : ""}>Phần trăm</option>
            <option value="2" ${type === "Tiền mặt" ? "selected" : ""}>Tiền mặt</option>
            <option value="3" ${type === "Tặng quà" ? "selected" : ""}>Tặng quà</option>
        </select>`;

        tr.children[2].innerHTML = `<input type="number" value="${discount}">`;
        tr.children[3].innerHTML = `<input type="number" value="${max}">`;
        tr.children[4].innerHTML = `<input type="number" value="${min}">`;
        tr.children[5].innerHTML = `<input type="number" value="${quantity}">`;

        // Chuyển date dd/MM/yyyy → yyyy-MM-dd nếu cần
        tr.children[6].innerHTML = `<input type="date" value="${start}">`;
        tr.children[7].innerHTML = `<input type="date" value="${end}">`;

        // Đổi nút sửa thành lưu/hủy
        tr.children[9].innerHTML = `
        <button onclick="saveRow(this)" data-id="${id}">Lưu</button>
        <button onclick="cancelEdit()">Hủy</button>
    `;
    };


    window.saveRow = function (btn) {
        const tr = btn.closest("tr");
        const id = btn.dataset.id;

        const promoCode = tr.children[0].querySelector("input").value;
        const promoType = tr.children[1].querySelector("select").value;
        const discountValue = tr.children[2].querySelector("input").value;
        const maxDiscount = tr.children[3].querySelector("input").value;
        const minOrder = tr.children[4].querySelector("input").value;
        const quantity = tr.children[5].querySelector("input").value;
        const startDate = tr.children[6].querySelector("input").value;
        const endDate = tr.children[7].querySelector("input").value;

        const body = new URLSearchParams();
        body.append("action", "updateVoucher");
        body.append("id", id);
        body.append("promoCode", promoCode);
        body.append("promoType", promoType);
        body.append("discountValue", discountValue);
        body.append("maxDiscount", maxDiscount);
        body.append("minOrder", minOrder);
        body.append("quantity", quantity);
        body.append("startDate", startDate);
        body.append("endDate", endDate);

        fetch(`${contextPath}/admin/vouchers`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: body.toString()
        }).then(() => location.reload());
    };

});
