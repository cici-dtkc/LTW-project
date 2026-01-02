const searchInput = document.getElementById('searchInput');
const statusFilter = document.getElementById('statusFilter');
const tableBody = document.querySelector('#ordersTable tbody');

// LOAD ORDERS
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

// SEARCH + FILTER
searchInput.addEventListener('input', loadOrders);
statusFilter.addEventListener('change', loadOrders);

// UPDATE STATUS
function showToast(message, isError = false) {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = 'toast show' + (isError ? ' error' : '');

    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

function bindStatusChange() {
    document.querySelectorAll('.status-select').forEach(select => {
        select.addEventListener('change', function () {
            fetch(`${contextPath}/admin/orders`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `orderId=${this.dataset.id}&status=${this.value}`
            })
                .then(res => res.text())
                .then(data => {
                    if (data === 'success') {
                        showToast('Cập nhật trạng thái thành công');
                    } else {
                        showToast(data, true);
                        loadOrders(); // revert lại trạng thái đúng
                    }
                });
        });
    });
}


// INIT
bindStatusChange();

