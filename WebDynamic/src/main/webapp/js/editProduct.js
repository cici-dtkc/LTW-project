document.addEventListener("DOMContentLoaded", () => {
    const btnPhone = document.getElementById("btnPhone");
    const btnAccessory = document.getElementById("btnAccessory");
    const formPhone = document.getElementById("editFormPhone");
    const formAccessory = document.getElementById("editFormAccessory");
    const productTag = document.querySelector(".product-tag");

    // --- LOGIC CHUYỂN TAB ---
    function showPhone() {
        formPhone.style.display = "block";
        formAccessory.style.display = "none";
        btnPhone.classList.add("active");
        btnAccessory.classList.remove("active");
        productTag.textContent = "Điện thoại";
    }

    function showAccessory() {
        formPhone.style.display = "none";
        formAccessory.style.display = "block";
        btnAccessory.classList.add("active");
        btnPhone.classList.remove("active");
        productTag.textContent = "Linh kiện";
    }

    btnPhone.addEventListener("click", showPhone);
    btnAccessory.addEventListener("click", showAccessory);

    // --- LOGIC XEM TRƯỚC ẢNH (PREVIEW) ---
    function handleImagePreview(inputName, formId) {
        const input = document.querySelector(`#${formId} input[name="${inputName}"]`);
        const previewImg = document.querySelector(`#${formId} .current-image-box img`);

        if (input && previewImg) {
            input.addEventListener("change", function() {
                const file = this.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        previewImg.src = e.target.result;
                    };
                    reader.readAsDataURL(file);
                }
            });
        }
    }

    // Khởi chạy preview cho cả 2 form
    handleImagePreview("imagePhone", "editFormPhone");
    handleImagePreview("imageAccessory", "editFormAccessory");
});