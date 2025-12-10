
document.addEventListener("DOMContentLoaded", function () {
    const modalOverlay = document.querySelector("#modalOverlay")
    const addressForm = document.querySelector("#addressForm")
    let editingId;
    let selectedAddressType;
    let addressTypeButtons;
    const btnAddAddress  = document.querySelector("#btnAddAddress");
    const btnBack  = document.querySelector("#btnBack");
    try {

    (function () {
        if (window.__addresses_sidebar_inited) return;
        window.__addresses_sidebar_inited = true;

        const menuAccountMain = document.getElementById("menuAccountMain");
        const accountSubmenu = document.getElementById("accountSubmenu");

        if (menuAccountMain && accountSubmenu) {
            accountSubmenu.classList.add("open");
            menuAccountMain.addEventListener("click", (e) => {
                e.preventDefault();
                accountSubmenu.classList.toggle("open");
            });
        }
    })();
    // ===============================
    // SHOW / HIDE MODAL
    // ===============================
    function showModal() {
        modalOverlay.classList.add("active");
        resetForm();
    }

    function hideModal() {
        modalOverlay.classList.remove("active");
        resetForm();
        editingId = null;
    }

    function resetForm() {
        addressForm.reset();
        selectedAddressType = "home";


        addressTypeButtons.forEach(btn => {
            btn.classList.toggle("active", btn.dataset.type === "home");
        });
    }

    // ===============================
    // READ DATA (robust: handle different markup variants)
    // ===============================
    function readAddressData(container) {
        if (!container) return null;
        const id = parseInt(container.dataset.id) || null;
        const nameEl = container.querySelector(".address-name");
        const phoneEl = container.querySelector(".address-phone");
        const detailsEl = container.querySelector(".address-details");

        // Try to extract addr-line1 / addr-line2 if present, otherwise use full details text
        const addrLine1El = container.querySelector('.addr-line1');
        const addrLine2El = container.querySelector('.addr-line2');

        let addressLine1 = '';
        let addressLine2 = '';
        if (addrLine1El || addrLine2El) {
            addressLine1 = addrLine1El ? addrLine1El.textContent.trim() : '';
            addressLine2 = addrLine2El ? addrLine2El.textContent.trim() : '';
        } else if (detailsEl) {
            // If address-details contains one or multiple divs, join them with comma/newline
            const parts = Array.from(detailsEl.children).map(c => c.textContent.trim()).filter(Boolean);
            if (parts.length === 0) {
                addressLine1 = detailsEl.textContent.trim();
            } else if (parts.length === 1) {
                addressLine1 = parts[0];
            } else {
                addressLine1 = parts[0];
                addressLine2 = parts.slice(1).join(', ');
            }
        }

        return {
            id: id,
            name: nameEl ? nameEl.textContent.trim() : '',
            phone: phoneEl ? phoneEl.textContent.trim() : '',
            addressLine1: addressLine1,
            addressLine2: addressLine2,
            type: container.dataset.type || 'home',
            isDefault: container.classList.contains("default")
        };
    }


    // UPDATE DATA BACK TO HTML
    function updateAddressHTML(container, data) {
        if (!container) return;
        container.dataset.type = data.type;

        const nameEl = container.querySelector(".address-name");
        const phoneEl = container.querySelector(".address-phone");
        const detailsEl = container.querySelector(".address-details");

        if (nameEl) nameEl.textContent = data.name || '';
        if (phoneEl) phoneEl.textContent = data.phone || '';

        // If container uses addr-line1/addr-line2, update them; otherwise replace .address-details innerHTML
        const addrLine1El = container.querySelector('.addr-line1');
        const addrLine2El = container.querySelector('.addr-line2');
        if (addrLine1El || addrLine2El) {
            if (addrLine1El) addrLine1El.textContent = data.addressLine1 || '';
            if (addrLine2El) addrLine2El.textContent = data.addressLine2 || '';
        } else if (detailsEl) {
            let html = '';
            if (data.addressLine1) html += `<div>${escapeHtml(data.addressLine1)}</div>`;
            if (data.addressLine2) html += `<div>${escapeHtml(data.addressLine2)}</div>`;
            if (!html) html = `<div>${escapeHtml(data.addressLine1 || data.addressLine2 || '')}</div>`;
            detailsEl.innerHTML = html;
        }

        const badge = container.querySelector(".address-default-badge");

        if (data.isDefault) {
            container.classList.add("default");
            if (!badge) {
                container.querySelector(".address-info")
                    .insertAdjacentHTML("beforeend",
                        '<span class="address-default-badge">Mặc định</span>');
            }
        } else {
            container.classList.remove("default");
            if (badge) badge.remove();
        }
    }

    function escapeHtml(s) {
        if (!s) return '';
        return s.replace(/[&<>"']/g, function (c) {
            return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"}[c];
        });
    }


    // SET DEFAULT ADDRESS
    function setDefaultAddress(id) {
        document.querySelectorAll(".address-item").forEach(item => {
            const badge = item.querySelector(".address-default-badge");

            if (parseInt(item.dataset.id) === id) {
                item.classList.add("default");
                if (!badge)
                    item.querySelector(".address-info")
                        .insertAdjacentHTML("beforeend",
                            '<span class="address-default-badge">Mặc định</span>');
            } else {
                item.classList.remove("default");
                if (badge) badge.remove();
            }
        });
    }

    // ===============================
    // DELETE ADDRESS
    // ===============================
    function deleteAddress(id) {
        const item = document.querySelector(`.address-item[data-id="${id}"]`);
        if (item) item.remove();
    }

    // ===============================
    // EDIT ADDRESS
    // ===============================
    function editAddress(id) {
        const container = document.querySelector(`.address-item[data-id="${id}"]`);
        if (!container) return;

        const data = readAddressData(container);

        document.getElementById("fullName").value = data.name;
        document.getElementById("phoneNumber").value = data.phone;
        document.getElementById("specificAddress").value = data.addressLine1;
        document.getElementById("location").value = data.addressLine2;
        document.getElementById("setDefault").checked = data.isDefault;

        selectedAddressType = data.type;

        addressTypeButtons.forEach(btn => {
            btn.classList.toggle("active", btn.dataset.type === data.type);
        });

        editingId = id;

        showModal();
    }

    // ===============================
    // EVENT: SHOW MODAL WHEN ADD NEW
    // ===============================
    if (!btnAddAddress) {
        console.warn('addresses.js: #btnAddAddress not found — Add button will not work');
    } else {
        btnAddAddress.addEventListener("click", () => {
            editingId = null; // thêm mới nhưng KHÔNG tạo HTML
            showModal();
        });
    }

    if (btnBack) btnBack.addEventListener("click", hideModal);

    if (modalOverlay) {
        modalOverlay.addEventListener("click", e => {
            if (e.target === modalOverlay) hideModal();
        });
    } else {
        console.warn('addresses.js: #modalOverlay not found — modal cannot be shown');
    }

    if (addressTypeButtons && addressTypeButtons.length > 0) {
        addressTypeButtons.forEach(btn => {
            btn.addEventListener("click", () => {
                addressTypeButtons.forEach(b => b.classList.remove("active"));
                btn.classList.add("active");
                selectedAddressType = btn.dataset.type;
            });
        });
    }

    // ===============================
    // SUBMIT FORM
    // ===============================
    async function postAction(params) {
        const resp = await fetch('/user/addresses', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: new URLSearchParams(params)
        });
        try {
            return await resp.json();
        } catch (e) {
            return { success: false };
        }
    }

    if (!addressForm) {
        console.warn('addresses.js: #addressForm not found — form submit disabled');
    } else {
        addressForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        const nameVal = document.getElementById("fullName") ? document.getElementById("fullName").value.trim() : '';
        const phoneVal = document.getElementById("phoneNumber") ? document.getElementById("phoneNumber").value.trim() : '';
        const specificVal = document.getElementById("specificAddress") ? document.getElementById("specificAddress").value.trim() : '';
        const locationVal = document.getElementById("location") ? document.getElementById("location").value.trim() : '';
        const setDefaultVal = document.getElementById("setDefault") ? (document.getElementById("setDefault").checked ? '1' : '0') : '0';

        // Build fullAddress according to model (fullAddress field)
        let fullAddress = specificVal;
        if (locationVal) {
            if (fullAddress) fullAddress += ', ' + locationVal;
            else fullAddress = locationVal;
        }

        const payload = {
            // Model-style fields used by Address and DAO
            name: nameVal,
            phoneNumber: phoneVal,
            fullAddress: fullAddress,
            status: setDefaultVal
        };

        if (editingId) {
            payload.action = 'update';
            payload.id = editingId;
            const res = await postAction(payload);
            if (res && res.success) {
                const container = document.querySelector(`.address-item[data-id="${editingId}"]`);
                updateAddressHTML(container, {
                    name: payload.name,
                    phone: payload.phoneNumber,
                    addressLine1: payload.fullAddress || '',
                    addressLine2: '',
                    type: selectedAddressType,
                    isDefault: payload.status === '1'
                });
            } else {
                alert('Cập nhật địa chỉ thất bại');
            }
        } else {
            payload.action = 'add';
            const res = await postAction(payload);
            if (res && res.success) {
                // reload để lấy danh sách mới từ server
                window.location.reload();
            } else {
                alert('Thêm địa chỉ thất bại');
            }
        }

        hideModal();
        });
    }

    // ===============================
    // CLICK ACTIONS
    // ===============================
    if (!addressList) {
        console.warn('addresses.js: #addressList not found — action links disabled');
    } else {
        addressList.addEventListener("click", async function (e) {
        const btn = e.target.closest("[data-action]");
        if (!btn) return;

        e.preventDefault();

        const id = parseInt(btn.dataset.id);
        const action = btn.dataset.action;

        if (action === "delete") {
            if (!confirm('Bạn chắc chắn muốn xóa địa chỉ này?')) return;
            const res = await postAction({ action: 'delete', id });
            if (res && res.success) deleteAddress(id);
            else alert('Xóa thất bại');
        } else if (action === "update") {
            editAddress(id);
        } else if (action === "set-default") {
            const res = await postAction({ action: 'set-default', id });
            if (res && res.success) setDefaultAddress(id);
            else alert('Không thể đặt mặc định');
        }
        });
    }

    } catch (err) {
        console.error('addresses.js runtime error:', err);
    }
});
