const searchInput = document.getElementById('searchInput');
const statusFilter = document.getElementById('statusFilter');
const tableBody = document.querySelector('#ordersTable tbody');

function loadOrders() {
    const keyword = searchInput.value.trim();
    const status = statusFilter.value;

    const params = new URLSearchParams();
    if (keyword) params.append('keyword', keyword);
    if (status) params.append('status', status);
    params.append('ajax', 'true');

    fetch(`${contextPath}/admin/orders?` + params.toString())
        .then(res => res.text())
        .then(html => {
            tableBody.innerHTML = html;
            bindStatusChange();
        });
}

searchInput.addEventListener('input', loadOrders);
statusFilter.addEventListener('change', loadOrders);

/* ===== TOAST ===== */
function showToast(message, success) {
    const toast = document.getElementById('toast');

    toast.className = 'toast';
    toast.textContent = message;

    toast.classList.add('show');
    toast.classList.add(success ? 'success' : 'error');

    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

/* ===== UPDATE COLOR SELECT ===== */
function updateStatusColor(select) {
    switch (select.value) {
        case '1': // Đang lên đơn
            select.style.backgroundColor = '#fef3c7';
            select.style.color = '#f59e0b';
            select.style.borderColor = '#f59e0b';
            break;

        case '2': // Đang giao
            select.style.backgroundColor = '#e0f2fe';
            select.style.color = '#0ea5e9';
            select.style.borderColor = '#0ea5e9';
            break;

        case '3': // Đã giao
            select.style.backgroundColor = '#dcfce7';
            select.style.color = '#16a34a';
            select.style.borderColor = '#16a34a';
            break;

        case '5': // Hủy
            select.style.backgroundColor = '#fee2e2';
            select.style.color = '#dc2626';
            select.style.borderColor = '#dc2626';
            break;

        default:
            select.style.backgroundColor = '#fff';
            select.style.color = '#333';
            select.style.borderColor = '#ddd';
    }
}


/* ===== BIND CHANGE ===== */
function bindStatusChange() {
    document.querySelectorAll('.status-select').forEach(select => {

        // set màu ban đầu
        updateStatusColor(select);

        select.addEventListener('change', function () {

            updateStatusColor(this);

            fetch(`${contextPath}/admin/orders/update-status`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `orderId=${this.dataset.id}&status=${this.value}`
            })
                .then(res => res.json())
                .then(data => {
                    showToast(data.message, data.success);
                    if (!data.success) {
                        loadOrders();
                    }
                })
                .catch(() => {
                    showToast('Lỗi kết nối server', false);
                    loadOrders();
                });
        });
    });
}

bindStatusChange();
