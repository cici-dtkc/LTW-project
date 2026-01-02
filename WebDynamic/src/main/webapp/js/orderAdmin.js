document.querySelectorAll('.status-select').forEach(select => {
    select.addEventListener('change', function () {
        fetch('/admin/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `orderId=${this.dataset.id}&status=${this.value}`
        })
            .then(res => res.text())
            .then(data => {
                if (data !== 'success') {
                    alert('Cập nhật trạng thái thất bại');
                }
            });
    });
});

// Chức năng search
document.getElementById('statusFilter').addEventListener('change', function () {
    const status = this.value;
    window.location.href = `${pageContext.request.contextPath}/admin/orders?status=${status}`;})


