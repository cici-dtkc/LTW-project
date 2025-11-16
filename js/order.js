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
    const priceText = p.textContent.replace(/[^\d]/g, '');
    const quantityText = p.parentElement.textContent.match(/Số lượng:\s*(\d+)/);
    const quantity = quantityText ? parseInt(quantityText[1]) : 1;
    total += parseInt(priceText) * quantity;
  });

  const formattedTotal = total.toLocaleString('vi-VN') + 'đ';
  const totalEl = order.querySelector('.total-price');
  if (totalEl) totalEl.textContent = formattedTotal;
});

// ===========================
// NÚT HỦY & MUA LẠI
// ===========================
document.querySelectorAll('.order-card').forEach(order => {
  const btn = order.querySelector('.btn');

  btn.addEventListener('click', () => {
    const statusEl = order.querySelector('.status');

    // Nếu đơn đang giao → hủy
    if (order.dataset.status === 'shipping') {
      const confirmCancel = confirm("Bạn có chắc muốn hủy đơn này?");
      if (confirmCancel) {
        order.dataset.status = 'cancelled';
        statusEl.className = 'status cancelled';
        statusEl.innerHTML = '<i class="fa-solid fa-xmark"></i> Đã hủy';
        btn.textContent = 'Mua lại';
        alert("Đơn hàng đã được hủy!");
      }
    }
    // Nếu đơn đã giao hoặc đã hủy → mua lại
    else if (order.dataset.status === 'cancelled' || order.dataset.status === 'delivered') {
      window.location.href = "../order_detail.html";
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
