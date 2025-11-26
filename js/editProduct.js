// Fake data – bạn sẽ replace bằng API fetch
const product = {
    id: 10,
    category: "part",
    name: "Cáp sạc USB-C",
    brand_id: 4,
    subcategory: 3,
    description: "Cáp sạc nhanh 65W",
    warranty: 6,
    discount: 10,
    attributes: [
        {name: "Chiều dài", value: "1m"},
        {name: "Công suất", value: "65W"}
    ],
    variants: [
        {name: "Màu", value: "Trắng", price: 150000, quantity: 30},
        {name: "Màu", value: "Đen", price: 150000, quantity: 20}
    ]
};

const phoneProduct = {
    id: 1,
    category: "phone",
    name: "iPhone 15",
    brand_id: 1,
    description: "Điện thoại cao cấp",
    release_date: "2024-01-01",
    warranty: 12,
    discount: 5,
    main_image: "link_ảnh.jpg",
    attributes: [
        {name: "Màn hình", value: "6.1 inch"},
        {name: "CPU", value: "A17 Pro"}
    ],
    variants: [
        {name: "Màu", value: "Xanh", price: 25000000, quantity: 10},
        {name: "Màu", value: "Đen", price: 25000000, quantity: 5}
    ]
};

// Khi DOM ready
document.addEventListener("DOMContentLoaded", () => {
    // Load mặc định là product (linh kiện)
    loadProductData(phoneProduct);

    // Lắng nghe thay đổi radio category
    document.getElementById("phone").addEventListener("change", () => loadProductData(phoneProduct));
    document.getElementById("part").addEventListener("change", () => loadProductData(product));
});

function loadProductData(p) {
    // -----------------------------
    // Chọn category (phone / part)
    // -----------------------------
    document.getElementById(p.category === "phone" ? "phone" : "part").checked = true;

    document.getElementById("phoneForm").style.display = p.category === "phone" ? "block" : "none";
    document.getElementById("partForm").style.display = p.category === "part" ? "block" : "none";

    // -----------------------------
    // GÁN DỮ LIỆU CHO PHONE
    // -----------------------------
    if (p.category === "phone") {
        document.getElementById("phoneName").value = p.name || "";
        document.getElementById("phoneBrand").value = p.brand_id || "";
        document.getElementById("phoneDescription").value = p.description || "";
        document.getElementById("phoneReleaseDate").value = p.release_date || "";
        document.getElementById("phoneWarranty").value = p.warranty || "";
        document.getElementById("phoneDiscount").value = p.discount || "";

        if (p.main_image) {
            document.getElementById("previewMainImage").src = p.main_image;
        }
    }
        // -----------------------------
        // GÁN DỮ LIỆU CHO PART (LINH KIỆN)
    // -----------------------------
    else {
        document.getElementById("partName").value = p.name || "";
        document.getElementById("partBrand").value = p.brand_id || "";
        document.getElementById("partSubcategory").value = p.subcategory || "";
        document.getElementById("partDescription").value = p.description || "";
        document.getElementById("partWarranty").value = p.warranty || "";
        document.getElementById("partDiscount").value = p.discount || "";

        if (p.main_image) {
            document.getElementById("previewPartMainImage").src = p.main_image;
        }
    }

    // -----------------------------
    // LOAD ATTRIBUTES
    // -----------------------------
    const container = p.category === "phone"
        ? document.getElementById("phoneAttributesContainer")
        : document.getElementById("partAttributesContainer");

    container.innerHTML = ""; // clear trước

    (p.attributes || []).forEach(att => {
        const row = document.getElementById("attributeTemplate").content.cloneNode(true);
        row.querySelector(".attName").value = att.name || "";
        row.querySelector(".attValue").value = att.value || "";

        // thêm nút xóa
        const removeBtn = row.querySelector(".removeBtn");
        removeBtn.addEventListener("click", () => row.remove());

        container.appendChild(row);
    });

    // -----------------------------
    // LOAD VARIANTS
    // -----------------------------
    const vContainer = p.category === "phone"
        ? document.getElementById("phoneVariantsContainer")
        : document.getElementById("partVariantsContainer");

    vContainer.innerHTML = ""; // clear trước

    (p.variants || []).forEach(v => {
        const row = document.getElementById("variantTemplate").content.cloneNode(true);
        row.querySelector(".varName").value = v.name || "";
        row.querySelector(".varValue").value = v.value || "";
        row.querySelector(".varPrice").value = v.price || "";
        row.querySelector(".varQuantity").value = v.quantity || "";

        // thêm nút xóa
        const removeBtn = row.querySelector(".removeBtn");
        removeBtn.addEventListener("click", () => row.remove());

        vContainer.appendChild(row);
    });
}
