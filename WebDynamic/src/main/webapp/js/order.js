// ===========================
// TOAST NOTIFICATION
// ===========================
function showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.style.cssText = `
    background: ${type == 'success' ? '#4CAF50' : '#f44336'};
    color: white;
    padding: 15px 20px;
    margin-bottom: 10px;
    border-radius: 5px;
    box-shadow: 0 2px 5px rgba(0,0,0,0.2);
    animation: slideIn 0.3s ease-out;
    min-width: 250px;
  `;
    toast.textContent = message;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// CSS animations
const style = document.createElement('style');
style.textContent = `
  @keyframes slideIn {
    from { transform: translateX(400px); opacity: 0; }
    to { transform: translateX(0); opacity: 1; }
  }
  @keyframes slideOut {
    from { transform: translateX(0); opacity: 1; }
    to { transform: translateX(400px); opacity: 0; }
  }
`;
document.head.appendChild(style);

// ===========================
// HỦY ĐƠN HÀNG
// ===========================
function cancelOrder(orderId) {
    if (confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
        fetch('<%= request.getContextPath() %>/user/order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'action=cancel&orderId=' + orderId
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showToast('Hủy đơn hàng thành công!', 'success');
                    setTimeout(() => location.reload(), 1500);
                } else {
                    showToast(data.message || 'Không thể hủy đơn hàng', 'error');
                }
            })
            .catch(error => {
                showToast('Có lỗi xảy ra: ' + error, 'error');
            });
    }
}

// ===========================
// MUA LẠI ĐƠN HÀNG
// ===========================
function repurchaseOrder(orderId) {
    if (confirm('Bạn có muốn mua lại đơn hàng này?')) {
        location.href = '<%= request.getContextPath() %>/user/repurchase?orderId=' + orderId;
    }
}

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
