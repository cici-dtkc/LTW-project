// ===========================
// CHUYỂN TAB TRẠNG THÁI
// ===========================
const tabs = document.querySelectorAll('.tab');
const orders = document.querySelectorAll('.order-card');

tabs.forEach(tab => {
  tab.addEventListener('click', () => {
    tabs.forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    const status = tab.dataset.status;

    orders.forEach(order => {
      if (status === 'all' || order.dataset.status === status) {
        order.style.display = 'block';
      } else {
        order.style.display = 'none';
      }
    });
  });
});


// ===========================
// TÍNH TỔNG TIỀN MỖI ĐƠN
// ===========================
orders.forEach(order => {
  const priceEls = order.querySelectorAll('.price');
  let total = 0;

  priceEls.forEach(p => {
    // Lấy giá trị số (bỏ ký tự đ, .)
    const priceText = p.textContent.replace(/[^\d]/g, '');
    const quantityText = p.parentElement.textContent.match(/Số lượng:\s*(\d+)/);
    const quantity = quantityText ? parseInt(quantityText[1]) : 1;
    total += parseInt(priceText) * quantity;
  });

  // Định dạng lại tiền
  const formattedTotal = total.toLocaleString('vi-VN') + 'đ';
  const totalEl = order.querySelector('.total-price');
  if (totalEl) totalEl.textContent = formattedTotal;
});


// ===========================
// NÚT MUA LẠI
// ===========================
document.querySelectorAll('.btn.repurchase').forEach(btn => {
  btn.addEventListener('click', () => {
    window.location.href = 'payment.html';
  });
});


// ===========================
// NÚT HỦY ĐƠN
// ===========================
document.querySelectorAll('.btn.cancel').forEach(btn => {
  btn.addEventListener('click', () => {
    const confirmCancel = confirm("Bạn có chắc muốn hủy đơn này?");
    if (confirmCancel) {
      const order = btn.closest('.order-card');
      order.dataset.status = 'cancelled';
      order.querySelector('.status').className = 'status cancelled';
      order.querySelector('.status').innerHTML = '<i class="fa-solid fa-xmark"></i> Đã hủy';
      alert("Đơn hàng đã được hủy!");
    }
  });
});


// ===========================
// SIDEBAR submenu toggle
// ===========================
const menuAccountMain = document.getElementById("menuAccountMain");
const accountSubmenu = document.getElementById("accountSubmenu");

if (menuAccountMain && accountSubmenu) {
  accountSubmenu.classList.add("open");

  menuAccountMain.addEventListener("click", (e) => {
    e.preventDefault();
    accountSubmenu.classList.toggle("open");
  });
}
