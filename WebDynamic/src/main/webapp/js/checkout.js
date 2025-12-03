const addressList = document.getElementById("addressList");
const changeBtn = document.getElementById("changeAddressBtn");

// Danh sách địa chỉ (có thể sau này lấy từ API)
const addresses = [
    { name: "Nguyễn Văn A", phone: "(+84) 912 345 678", address: "123 Lê Lợi, Quận 1, TP.HCM" },
    { name: "Trần Thị B", phone: "(+84) 988 112 233", address: "45 Nguyễn Huệ, Quận 3, TP.HCM" },
    { name: "Huỳnh Đức", phone: "(+84) 902 275 080", address: "Sau Ủy Ban Khánh Bình, Xã Khánh Bình, Huyện An Phú, An Giang" }
];

// Tạo danh sách địa chỉ
function renderAddressList() {
    addressList.innerHTML = `
    <h4>Chọn địa chỉ giao hàng khác:</h4>
    <ul>
      ${addresses.map((a, index) => `
        <li data-index="${index}">
          <strong>${a.name}</strong> <span>${a.phone}</span><br>
          <small>${a.address}</small>
        </li>
      `).join('')}
    </ul>
  `;
}

// Khi nhấn "Thay đổi" thì hiện/ẩn danh sách
changeBtn.addEventListener("click", (e) => {
    e.preventDefault();
    addressList.classList.toggle("hidden");
});

// Khi chọn 1 item
addressList.addEventListener("click", (e) => {
    const item = e.target.closest("li");
    if (!item) return; // click ra ngoài thì bỏ qua

    const index = item.getAttribute("data-index");
    const selected = addresses[index];

    // Cập nhật thông tin người nhận
    document.getElementById("receiver-name").textContent = selected.name;
    document.getElementById("receiver-phone").textContent = selected.phone;
    document.getElementById("receiver-address").childNodes[0].textContent = selected.address + ' ';

    // Ẩn danh sách sau khi chọn
    addressList.classList.add("hidden");
});

renderAddressList();


// voucher
const scrollContainer = document.getElementById("voucherScroll");

document.getElementById("nextBtn").addEventListener("click", () => {
    scrollContainer.scrollBy({ left: 400, behavior: "smooth" });
});

document.getElementById("prevBtn").addEventListener("click", () => {
    scrollContainer.scrollBy({ left: -400, behavior: "smooth" });
});
document.addEventListener("DOMContentLoaded", function () {
    const vouchers = document.querySelectorAll(".voucher");

    // ✅ Lấy giá trị trực tiếp từ HTML theo ID
    const subtotalEl = document.getElementById("subtotal");
    const shippingEl = document.getElementById("shipping");
    const discountEl = document.getElementById("discount");
    const totalEl = document.getElementById("grandTotal");

    let activeVoucher = null;

    // 👉 Chuyển "1.290.000₫" → 1290000
    function parseCurrency(value) {
        return parseInt(value.replace(/[^\d]/g, "")) || 0;
    }

    // 👉 Cập nhật lại phần tóm tắt đơn hàng
    function updateSummary(discount) {
        const subtotal = parseCurrency(subtotalEl.textContent);
        const shipping = parseCurrency(shippingEl.textContent);
        const total = subtotal + shipping - discount;

        discountEl.textContent = discount.toLocaleString("vi-VN") + "₫";
        totalEl.textContent = total.toLocaleString("vi-VN") + "₫";
    }

    // 👉 Xử lý khi chọn voucher
    vouchers.forEach(voucher => {
        const useBtn = voucher.querySelector("button");
        const percentText = voucher.querySelector("h3").innerText;
        const percent = parseInt(percentText.replace(/\D/g, ""));

        useBtn.addEventListener("click", () => {
            // Nếu voucher đang được áp dụng → bỏ chọn
            if (activeVoucher === voucher) {
                voucher.classList.remove("active");
                useBtn.textContent = "Sử dụng";
                activeVoucher = null;
                updateSummary(0);
                return;
            }

            // Bỏ chọn voucher cũ (nếu có)
            if (activeVoucher) {
                const oldBtn = activeVoucher.querySelector("button");
                activeVoucher.classList.remove("active");
                oldBtn.textContent = "Sử dụng";
            }

            // Áp dụng voucher mới
            voucher.classList.add("active");
            useBtn.textContent = "Đã áp dụng";
            activeVoucher = voucher;

            const subtotal = parseCurrency(subtotalEl.textContent);
            let discount = subtotal * (percent / 100);

            // Giới hạn giảm tối đa ("Giảm tối đa 100kđ")
            const maxText = voucher.querySelector("p").innerText;
            const maxMatch = maxText.match(/(\d+)k/);
            if (maxMatch) {
                const maxDiscount = parseInt(maxMatch[1]) * 1000;
                discount = Math.min(discount, maxDiscount);
            }

            updateSummary(discount);
        });
    });
});