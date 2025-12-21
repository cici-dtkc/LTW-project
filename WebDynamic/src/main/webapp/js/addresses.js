document.addEventListener("DOMContentLoaded", () => {

    const modal = document.getElementById("modalOverlay");
    const form = document.getElementById("addressForm");
    const addressList = document.getElementById("addressList");
    const btnAdd = document.getElementById("btnAddAddress");
    const btnBack = document.getElementById("btnBack");

    const inputName = document.getElementById("name");
    const inputPhone = document.getElementById("phoneNumber");
    const inputAddress = document.getElementById("fullAddress");
    const inputStatus = document.getElementById("status");

    let editingId = null;
    const menuAccountMain = document.getElementById("menuAccountMain");
    const accountSubmenu = document.getElementById("accountSubmenu");
    /* ==========================
       MODAL
    ========================== */
    function openModal() {
        modal.classList.add("active");
    }

    function closeModal() {
        modal.classList.remove("active");
        form.reset();
        editingId = null;
    }

    btnAdd?.addEventListener("click", () => {
        openModal();
    });

    btnBack?.addEventListener("click", closeModal);

    modal?.addEventListener("click", e => {
        if (e.target === modal) closeModal();
    });

    /* ==========================
       AJAX
    ========================== */
    async function post(data) {
        const res = await fetch(`${window.contextPath}/user/addresses`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams(data)
        });
        return res.json();
    }

    /* ==========================
       EDIT
    ========================== */
    function fillForm(item) {
        inputName.value = item.querySelector(".address-name").innerText.trim();
        inputPhone.value = item.querySelector(".address-phone").innerText.trim();
        inputAddress.value = item.querySelector(".address-details").innerText.trim();
        inputStatus.checked = item.classList.contains("default");
    }

    /* ==========================
       SUBMIT FORM
    ========================== */
    form?.addEventListener("submit", async e => {
        e.preventDefault();

        const payload = {
            name: inputName.value.trim(),
            phoneNumber: inputPhone.value.trim(),
            fullAddress: inputAddress.value.trim(),
            status: inputStatus.checked ? 1 : 0
        };

        if (editingId) {
            payload.action = "update";
            payload.id = editingId;
        } else {
            payload.action = "add";
        }

        const res = await post(payload);

        if (res.success) {
            location.reload();
        } else {
            alert("Thao tác thất bại");
        }
    });

    /* ==========================
       CLICK ACTIONS
    ========================== */
    addressList?.addEventListener("click", async e => {
        const btn = e.target.closest("[data-action]");
        if (!btn) return;

        e.preventDefault();
        const id = btn.dataset.id;
        const action = btn.dataset.action;
        const item = document.querySelector(`.address-item[data-id="${id}"]`);

        if (action === "update") {
            editingId = id;
            fillForm(item);
            openModal();
        }

        if (action === "delete") {
            if (!confirm("Bạn chắc chắn muốn xóa?")) return;
            const res = await post({ action: "delete", id });
            if (res.success) item.remove();
            else alert("Xóa thất bại");
        }

        if (action === "set-default") {
            const res = await post({ action: "set-default", id });
            if (res.success) location.reload();
            else alert("Không thể đặt mặc định");
        }
    });
    // SIDEBAR submenu toggle
    if (menuAccountMain && accountSubmenu) {
        accountSubmenu.classList.add("open");
        menuAccountMain.addEventListener("click", (e) => {
            e.preventDefault();
            accountSubmenu.classList.toggle("open");
        });
    }
});
