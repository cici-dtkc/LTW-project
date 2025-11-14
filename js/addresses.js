// address.js - Address Management (NO RENDER, NO ADD NEW ITEM)

document.addEventListener("DOMContentLoaded", function () {

    // ===============================
    // ELEMENTS
    // ===============================
    const btnAddAddress = document.getElementById("btnAddAddress");
    const modalOverlay = document.getElementById("modalOverlay");
    const btnBack = document.getElementById("btnBack");
    const addressForm = document.getElementById("addressForm");
    const addressList = document.getElementById("addressList");
    const addressTypeButtons = document.querySelectorAll(".address-type-btn");

    let selectedAddressType = "home";
    let editingId = null; // chỉ lưu ID khi cập nhật, null = thêm mới (nhưng không tạo mới)

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
    // READ DATA
    // ===============================
    function readAddressData(container) {
        return {
            id: parseInt(container.dataset.id),
            name: container.querySelector(".address-name").textContent.trim(),
            phone: container.querySelector(".address-phone").textContent.trim(),
            addressLine1: container.querySelector(".addr-line1").textContent.trim(),
            addressLine2: container.querySelector(".addr-line2").textContent.trim(),
            type: container.dataset.type,
            isDefault: container.classList.contains("default")
        };
    }

    // ===============================
    // UPDATE DATA BACK TO HTML
    // ===============================
    function updateAddressHTML(container, data) {
        container.dataset.type = data.type;

        container.querySelector(".address-name").textContent = data.name;
        container.querySelector(".address-phone").textContent = data.phone;
        container.querySelector(".addr-line1").textContent = data.addressLine1;
        container.querySelector(".addr-line2").textContent = data.addressLine2;

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

    // ===============================
    // SET DEFAULT ADDRESS
    // ===============================
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
    btnAddAddress.addEventListener("click", () => {
        editingId = null; // thêm mới nhưng KHÔNG tạo HTML
        showModal();
    });

    btnBack.addEventListener("click", hideModal);

    modalOverlay.addEventListener("click", e => {
        if (e.target === modalOverlay) hideModal();
    });

    addressTypeButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            addressTypeButtons.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
            selectedAddressType = btn.dataset.type;
        });
    });

    // ===============================
    // SUBMIT FORM
    // ===============================
    addressForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const data = {
            id: editingId,
            name: document.getElementById("fullName").value,
            phone: document.getElementById("phoneNumber").value,
            addressLine1: document.getElementById("specificAddress").value,
            addressLine2: document.getElementById("location").value,
            type: selectedAddressType,
            isDefault: document.getElementById("setDefault").checked
        };

        if (data.isDefault) setDefaultAddress(data.id);

        if (editingId) {
            // UPDATE ONLY — DO NOT CREATE NEW
            const container = document.querySelector(`.address-item[data-id="${editingId}"]`);
            updateAddressHTML(container, data);
        } else {
            // THÊM MỚI: KHÔNG LÀM GÌ
            console.warn("Bạn chọn cấu hình C: Không tạo địa chỉ mới bằng JS.");
        }

        hideModal();
    });

    // ===============================
    // CLICK ACTIONS
    // ===============================
    addressList.addEventListener("click", function (e) {
        const btn = e.target.closest("[data-action]");
        if (!btn) return;

        e.preventDefault();

        const id = parseInt(btn.dataset.id);
        const action = btn.dataset.action;

        if (action === "delete") deleteAddress(id);
        else if (action === "update") editAddress(id);
        else if (action === "set-default") setDefaultAddress(id);
    });

});
