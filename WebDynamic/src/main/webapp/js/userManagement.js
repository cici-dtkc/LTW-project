
document.addEventListener('DOMContentLoaded', () => {
    try {
        const searchInput = document.getElementById('search-input');
        const roleFilter = document.getElementById('role-filter');
        const statusFilter = document.getElementById('status-filter');
        const userTableBody = document.getElementById('user-table-body');
        const addUserBtn = document.getElementById('add-user-btn');

        if (!userTableBody) {
            console.warn('userManagement.js: #user-table-body not found');
            return;
        }

        // Click delegation for edit/save/cancel
        userTableBody.addEventListener('click', async (e) => {
            const target = e.target;
            const row = target.closest('tr');
            if (!row) return;

            const roleCell = row.querySelector('.role-cell');
            const statusCell = row.querySelector('.status-cell');
            const editBtn = row.querySelector('.edit-icon');
            const saveBtn = row.querySelector('.save-icon');
            const cancelBtn = row.querySelector('.cancel-icon');

            if (target.classList.contains('edit-icon')) {
                const currentRole = (roleCell && roleCell.textContent || '').trim();
                const currentStatus = (statusCell && statusCell.textContent || '').trim();
                if (roleCell) roleCell.dataset.originalRole = currentRole;
                if (statusCell) statusCell.dataset.originalStatus = currentStatus;

                // create selects
                const roleSelect = document.createElement('select');
                roleSelect.className = 'edit-role';
                ['Admin','User'].forEach(r => {
                    const opt = document.createElement('option'); opt.value = r; opt.text = r; if (r===currentRole) opt.selected = true; roleSelect.appendChild(opt);
                });

                const statusSelect = document.createElement('select');
                statusSelect.className = 'edit-status';
                [['Hoạt động','Hoạt động'],['Tạm khóa','Tạm khóa']].forEach(s => { const opt = document.createElement('option'); opt.value = s[0]; opt.text = s[1]; if (s[0]===currentStatus) opt.selected = true; statusSelect.appendChild(opt); });

                if (roleCell) { roleCell.innerHTML = ''; roleCell.appendChild(roleSelect); }
                if (statusCell) { statusCell.innerHTML = ''; statusCell.appendChild(statusSelect); }

                if (editBtn) editBtn.style.display = 'none';
                if (saveBtn) saveBtn.style.display = 'inline';
                if (cancelBtn) cancelBtn.style.display = 'inline';
            }

            if (target.classList.contains('save-icon')) {
                const newRole = row.querySelector('.edit-role').value;
                const newStatus = row.querySelector('.edit-status').value;
                const userId = row.dataset.id;

                // Disable buttons while saving
                if (saveBtn) saveBtn.disabled = true;

                try {
                    // compute context-aware API path. Prefer server-injected `window.appContext` if present.
                    const contextPath = (typeof window.appContext !== 'undefined') ? window.appContext : (function(){ const seg = location.pathname.split('/'); return (seg.length>1 && seg[1]) ? '/' + seg[1] : ''; })();
                    const apiUrl = window.location.origin + contextPath + '/admin/users';
                    const res = await fetch(apiUrl, {
                        method: 'POST', headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ action: 'update', id: parseInt(userId, 10), role: newRole, status: newStatus })
                    });
                    const j = await res.json();
                    if (j && j.success === true && j.user) {
                        // update UI with returned authoritative user
                        const u = j.user;
                        if (roleCell) roleCell.textContent = mapRoleText(u.role);
                        if (statusCell) statusCell.textContent = mapStatusText(u.status);
                        if (editBtn) editBtn.style.display = 'inline';
                        if (saveBtn) { saveBtn.style.display = 'none'; saveBtn.disabled = false; }
                        if (cancelBtn) cancelBtn.style.display = 'none';
                    } else {
                        // revert
                        if (roleCell) roleCell.textContent = roleCell.dataset.originalRole || 'User';
                        if (statusCell) statusCell.textContent = statusCell.dataset.originalStatus || 'Hoạt động';
                        alert('Cập nhật người dùng thất bại');
                    }
                } catch (err) {
                    console.error('Failed to update user:', err);
                    if (roleCell) roleCell.textContent = roleCell.dataset.originalRole || 'User';
                    if (statusCell) statusCell.textContent = statusCell.dataset.originalStatus || 'Hoạt động';
                    alert('Lỗi kết nối khi cập nhật người dùng');
                } finally {
                    if (saveBtn) saveBtn.disabled = false;
                }
            }

            if (target.classList.contains('cancel-icon')) {
                if (roleCell) roleCell.textContent = roleCell.dataset.originalRole || roleCell.textContent;
                if (statusCell) statusCell.textContent = statusCell.dataset.originalStatus || statusCell.textContent;
                if (editBtn) editBtn.style.display = 'inline';
                if (saveBtn) saveBtn.style.display = 'none';
                if (cancelBtn) cancelBtn.style.display = 'none';
            }
        });

        // Filters & Search
        function applyFilters() {
            const q = searchInput ? searchInput.value.trim().toLowerCase() : '';
            const role = roleFilter ? roleFilter.value : '';
            const status = statusFilter ? statusFilter.value : '';
            Array.from(userTableBody.querySelectorAll('tr')).forEach(tr => {
                const text = tr.textContent.toLowerCase();
                const roleCellText = tr.querySelector('.role-cell') ? tr.querySelector('.role-cell').textContent : '';
                const statusCellText = tr.querySelector('.status-cell') ? tr.querySelector('.status-cell').textContent : '';
                let visible = true;
                if (q && !text.includes(q)) visible = false;
                if (role && roleCellText.trim() !== role) visible = false;
                if (status && statusCellText.trim() !== status) visible = false;
                tr.style.display = visible ? '' : 'none';
            });
        }

        if (searchInput) searchInput.addEventListener('input', applyFilters);
        if (roleFilter) roleFilter.addEventListener('change', applyFilters);
        if (statusFilter) statusFilter.addEventListener('change', applyFilters);

        if (addUserBtn) addUserBtn.addEventListener('click', () => { alert('Chức năng thêm người dùng chưa được triển khai'); });

        function mapRoleText(roleVal) {
            // server returns integer role
            if (roleVal === 1 || roleVal === '1') return 'Admin';
            return 'User';
        }

        function mapStatusText(statusVal) {
            if (statusVal === 1 || statusVal === '1') return 'Hoạt động';
            return 'Tạm khóa';
        }

    } catch (err) {
        console.error('userManagement.js runtime error:', err);
    }
});

