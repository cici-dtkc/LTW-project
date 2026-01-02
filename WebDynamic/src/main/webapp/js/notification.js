/**
 * Notification Utils - Toast và Confirm Dialog
 * File này chứa các hàm dùng chung cho tất cả các trang
 */

/* ==========================
   TOAST NOTIFICATION
========================== */
/**
 * Hiển thị toast notification
 * @param {string} message - Nội dung thông báo
 * @param {string} type - Loại thông báo: 'success', 'error', 'warning', 'info'
 * @param {number} duration - Thời gian hiển thị (ms), mặc định 3000
 */
function showToast(message, type = 'success', duration = 3000) {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;

    // Icon dựa trên type
    const icons = {
        'success': '✓',
        'error': '✕',
        'warning': '⚠',
        'info': 'ℹ'
    };
    const icon = icons[type] || '✓';

    toast.innerHTML = `
        <div class="toast-icon">${icon}</div>
        <div class="toast-message">${message}</div>
    `;

    document.body.appendChild(toast);

    // Trigger animation
    setTimeout(() => toast.classList.add('show'), 10);

    // Auto remove
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

/* ==========================
   CONFIRM DIALOG
========================== */
/**
 * Hiển thị confirm dialog
 * @param {string} message - Nội dung thông báo
 * @param {string} title - Tiêu đề dialog
 * @param {object} options - Các tùy chọn: iconType, confirmText, cancelText, confirmButtonClass
 * @returns {Promise<boolean>} - true nếu user confirm, false nếu cancel
 */
function showConfirmDialog(message, title = 'Xác nhận', options = {}) {
    return new Promise((resolve) => {
        const {
            iconType = 'info', // 'info', 'success', 'warning', 'danger'
            confirmText = 'Xác nhận',
            cancelText = 'Hủy',
            confirmButtonClass = 'btn-confirm' // 'btn-confirm', 'btn-confirm-delete', 'btn-confirm-primary'
        } = options;

        // Icon dựa trên iconType
        const icons = {
            'info': '<i class="fa-solid fa-circle-question"></i>',
            'success': '<i class="fa-solid fa-circle-check"></i>',
            'warning': '<i class="fa-solid fa-triangle-exclamation"></i>',
            'danger': '<i class="fa-solid fa-circle-exclamation"></i>'
        };
        const iconHtml = icons[iconType] || icons['info'];

        const overlay = document.createElement('div');
        overlay.className = 'confirm-overlay';

        overlay.innerHTML = `
            <div class="confirm-dialog">
                <div class="confirm-icon ${iconType}">
                    ${iconHtml}
                </div>
                <h3 class="confirm-title">${title}</h3>
                <p class="confirm-message">${message}</p>
                <div class="confirm-actions">
                    <button class="btn-cancel" id="confirmCancel">${cancelText}</button>
                    <button class="${confirmButtonClass}" id="confirmOk">${confirmText}</button>
                </div>
            </div>
        `;

        document.body.appendChild(overlay);
        setTimeout(() => overlay.classList.add('show'), 10);

        const btnCancel = overlay.querySelector('#confirmCancel');
        const btnConfirm = overlay.querySelector('#confirmOk');

        function close(result) {
            overlay.classList.remove('show');
            setTimeout(() => {
                overlay.remove();
                resolve(result);
            }, 300);
        }

        btnCancel.addEventListener('click', () => close(false));
        btnConfirm.addEventListener('click', () => close(true));
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) close(false);
        });

        // ESC key to close
        const escHandler = (e) => {
            if (e.key === 'Escape') {
                close(false);
                document.removeEventListener('keydown', escHandler);
            }
        };
        document.addEventListener('keydown', escHandler);
    });
}

/* ==========================
   SHORTHAND FUNCTIONS
========================== */
/**
 * Confirm xóa (red button, danger icon)
 */
function confirmDelete(itemName = 'mục này') {
    return showConfirmDialog(
        `Bạn có chắc chắn muốn xóa <strong>${itemName}</strong>?`,
        'Xác nhận xóa',
        {
            iconType: 'danger',
            confirmText: 'Xóa',
            confirmButtonClass: 'btn-confirm-delete'
        }
    );
}

/**
 * Confirm logout
 */
function confirmLogout() {
    return showConfirmDialog(
        'Bạn có chắc chắn muốn đăng xuất khỏi tài khoản?',
        'Xác nhận đăng xuất',
        {
            iconType: 'info',
            confirmText: 'Đăng xuất',
            confirmButtonClass: 'btn-confirm'
        }
    );
}

/**
 * Confirm action chung
 */
function confirmAction(message, title = 'Xác nhận') {
    return showConfirmDialog(message, title, {
        iconType: 'warning',
        confirmText: 'Xác nhận',
        confirmButtonClass: 'btn-confirm'
    });
}

/* ==========================
   LOADING OVERLAY
========================== */
let loadingOverlay = null;

/**
 * Hiển thị loading overlay toàn màn hình
 */
function showLoading() {
    if (loadingOverlay) return; // Đã có loading

    loadingOverlay = document.createElement('div');
    loadingOverlay.className = 'loading-overlay';
    loadingOverlay.innerHTML = '<div class="loading-spinner"></div>';

    document.body.appendChild(loadingOverlay);
    setTimeout(() => loadingOverlay.classList.add('show'), 10);
}

/**
 * Ẩn loading overlay
 */
function hideLoading() {
    if (!loadingOverlay) return;

    loadingOverlay.classList.remove('show');
    setTimeout(() => {
        if (loadingOverlay) {
            loadingOverlay.remove();
            loadingOverlay = null;
        }
    }, 300);
}

/* ==========================
   UTILITY FUNCTIONS
========================== */
/**
 * Validate email
 */
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

/**
 * Validate phone number (10 digits)
 */
function validatePhone(phone) {
    const re = /^[0-9]{10}$/;
    return re.test(phone);
}

/**
 * Format number với dấu phẩy
 */
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

/**
 * Debounce function
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/* ==========================
   EXPORT (if using modules)
========================== */
// Nếu sử dụng ES6 modules, uncomment dòng dưới:
// export { showToast, showConfirmDialog, confirmDelete, confirmLogout, confirmAction, showLoading, hideLoading, validateEmail, validatePhone, formatNumber, debounce };