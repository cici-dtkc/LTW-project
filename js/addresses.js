// address.js - Address Management

document.addEventListener("DOMContentLoaded", function () {
    // Initialize address data (mock data)
    let addresses = [
        {
            id: 1,
            name: "Nguyễn Văn A",
            phone: "1234567",
            addressLine1: "nhà Số 13 khu phố tây B đông hoà dĩ an bình dương",
            addressLine2: "Phường Đông Hòa, Thành Phố Dĩ An, Bình Dương",
            type: "home",
            isDefault: true
        },

        {
            id: 2,
            name: "Phạm Thị B",
            phone: "(+84) 852 399 329",
            addressLine1: "Ấp Đất Mới",
            addressLine2: "Xã Long Phước, Huyện Long Thành, Đồng Nai",
            type: "office",
            isDefault: false
        },
        {
            id: 3,
            name: "Nguyễn Văn C",
            phone: "(+84) 325 883 448",
            addressLine1: "300/23/16 nguyễn văn linh q7",
            addressLine2: "Phường Bình Thuận, Quận 7, TP. Hồ Chí Minh",
            type: "home",
            isDefault: false
        },

    ];

    // DOM Elements
    const btnAddAddress = document.getElementById("btnAddAddress");
    const modalOverlay = document.getElementById("modalOverlay");
    const btnBack = document.getElementById("btnBack");
    const addressForm = document.getElementById("addressForm");
    const addressList = document.getElementById("addressList");
    const addressTypeButtons = document.querySelectorAll(".address-type-btn");
    let selectedAddressType = "home";

    // Display addresses using innerHTML
    function displayAddresses() {
        let html = "";
        addresses.forEach(address => {
            const deleteLink = address.isDefault
                ? ""
                : `<a href="#" class="address-action-link" data-action="delete" data-id="${address.id}">Xóa</a>`;

            const defaultBadge = address.isDefault
                ? '<span class="address-default-badge">Mặc định</span>'
                : "";

            html += `
                <div class="address-item" data-id="${address.id}">
                    <div class="address-info">
                        <div class="address-name-phone">
                            <span class="address-name">${address.name}</span>
                            <span class="address-separator">|</span>
                            <span class="address-phone">${address.phone}</span>
                        </div>
                        <div class="address-details">
                            <div>${address.addressLine1}</div>
                            <div>${address.addressLine2}</div>
                        </div>
                        ${defaultBadge}
                    </div>
                    <div class="address-actions">
                        <div class="address-action-links">
                            <a href="#" class="address-action-link" data-action="update" data-id="${address.id}">Cập nhật</a>
                            ${deleteLink}
                        </div>
                        <button class="btn-set-default" data-action="set-default" data-id="${address.id}">Thiết lập mặc định</button>
                    </div>
                </div>
            `;
        });
        addressList.innerHTML = html;
    }

    // Show modal
    function showModal() {
        modalOverlay.classList.add("active");
        resetForm();
    }

    // Hide modal
    function hideModal() {
        modalOverlay.classList.remove("active");
        resetForm();
    }

    // Reset form
    function resetForm() {
        addressForm.reset();
        selectedAddressType = "home";
        addressTypeButtons.forEach(btn => {
            if (btn.dataset.type === "home") {
                btn.classList.add("active");
            } else {
                btn.classList.remove("active");
            }
        });
    }

    // Set default address
    function setDefaultAddress(id) {
        addresses.forEach(addr => {
            addr.isDefault = (addr.id === parseInt(id));
        });
        displayAddresses();
    }

    // Delete address
    function deleteAddress(id) {
        if (confirm("Bạn có chắc chắn muốn xóa địa chỉ này?")) {
            addresses = addresses.filter(addr => addr.id !== parseInt(id));
            displayAddresses();
        }
    }

    // Edit address
    function editAddress(id) {
        const address = addresses.find(addr => addr.id === id);
        if (address) {
            document.getElementById("fullName").value = address.name;
            document.getElementById("phoneNumber").value = address.phone;
            document.getElementById("specificAddress").value = address.addressLine1;

            // Set location dropdown
            const locationSelect = document.getElementById("location");
            locationSelect.value = address.addressLine2;

            // Set address type
            selectedAddressType = address.type;
            addressTypeButtons.forEach(btn => {
                if (btn.dataset.type === address.type) {
                    btn.classList.add("active");
                } else {
                    btn.classList.remove("active");
                }
            });

            document.getElementById("setDefault").checked = address.isDefault;

            showModal();

            // Store editing address id
            addressForm.dataset.editingId = id;
        }
    }

    // Event Listeners
    btnAddAddress.addEventListener("click", () => {
        delete addressForm.dataset.editingId;
        showModal();
    });

    btnBack.addEventListener("click", () => {
        hideModal();
    });

    // Close modal when clicking overlay
    modalOverlay.addEventListener("click", (e) => {
        if (e.target === modalOverlay) {
            hideModal();
        }
    });

    // Address type buttons
    addressTypeButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            addressTypeButtons.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
            selectedAddressType = btn.dataset.type;
        });
    });

    // Form submission
    addressForm.addEventListener("submit", (e) => {
        e.preventDefault();

        const fullName = document.getElementById("fullName").value.trim();
        const phoneNumber = document.getElementById("phoneNumber").value.trim();
        const location = document.getElementById("location").value;
        const specificAddress = document.getElementById("specificAddress").value.trim();
        const setDefault = document.getElementById("setDefault").checked;

        // Validation
        if (!fullName) {
            alert("Vui lòng nhập họ và tên");
            return;
        }

        if (!phoneNumber) {
            alert("Vui lòng nhập số điện thoại");
            return;
        }

        if (!location) {
            alert("Vui lòng chọn địa điểm");
            return;
        }

        if (!specificAddress) {
            alert("Vui lòng nhập địa chỉ cụ thể");
            return;
        }

        // Get location text (value is the full text)
        const locationText = location;

        const addressData = {
            name: fullName,
            phone: phoneNumber,
            addressLine1: specificAddress,
            addressLine2: locationText,
            type: selectedAddressType,
            isDefault: setDefault
        };

        const editingId = addressForm.dataset.editingId;
        if (editingId) {
            // Update existing address
            const index = addresses.findIndex(addr => addr.id === parseInt(editingId));
            if (index !== -1) {
                addressData.id = parseInt(editingId);
                addresses[index] = addressData;

                // If setting as default, unset others
                if (setDefault) {
                    addresses.forEach(addr => {
                        if (addr.id !== parseInt(editingId)) {
                            addr.isDefault = false;
                        }
                    });
                }
            }
        } else {
            // Add new address
            const newId = addresses.length > 0 ? Math.max(...addresses.map(a => a.id)) + 1 : 1;
            addressData.id = newId;

            // If setting as default, unset others
            if (setDefault) {
                addresses.forEach(addr => {
                    addr.isDefault = false;
                });
            }

            addresses.push(addressData);
        }

        displayAddresses();
        hideModal();
    });

    // Event delegation for address actions
    addressList.addEventListener("click", function(e) {
        // Find the closest element with data-action attribute
        const clickable = e.target.closest("[data-action]");
        if (!clickable) return;

        const action = clickable.getAttribute("data-action");
        const id = clickable.getAttribute("data-id");

        if (!action || !id) return;

        e.preventDefault();

        if (action === "update") {
            editAddress(parseInt(id));
        } else if (action === "delete") {
            deleteAddress(parseInt(id));
        } else if (action === "set-default") {
            setDefaultAddress(parseInt(id));
        }
    });

    // Sidebar submenu toggle
    const menuAccountMain = document.getElementById("menuAccountMain");
    const accountSubmenu = document.getElementById("accountSubmenu");

    if (menuAccountMain && accountSubmenu) {
        // Open submenu by default since we're on the address page
        accountSubmenu.classList.add("open");

        menuAccountMain.addEventListener("click", (e) => {
            e.preventDefault();
            accountSubmenu.classList.toggle("open");
        });
    }

    // Initial display
    displayAddresses();
});

