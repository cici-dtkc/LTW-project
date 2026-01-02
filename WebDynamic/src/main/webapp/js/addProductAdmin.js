function showPhone() {
    document.getElementById("phoneForm").style.display = "block";
    document.getElementById("partForm").style.display = "none";
}

function showPart() {
    document.getElementById("phoneForm").style.display = "none";
    document.getElementById("partForm").style.display = "block";
}
// Cập nhập thêm tham số data để điền giá trị
function addTech(targetId, data = null) {
    let container = document.getElementById(targetId);
    let tpl = document.getElementById("techTpl").content.cloneNode(true);

    if (data) {
        tpl.querySelector('input[name="techName[]"]').value = data.tech_name;
        tpl.querySelector('input[name="techValue[]"]').value = data.tech_value;
        tpl.querySelector('input[name="techPriority[]"]').value = data.priority;
    }

    container.appendChild(tpl);
}
function addVariant(targetId, data = null) {
    let tplId = (targetId === 'phoneVariant') ? "phoneVariantTpl" : "partVariantTpl";
    const templateElement = document.getElementById(tplId);

    if (templateElement) {
        let tpl = templateElement.content.cloneNode(true);
        let variantDiv = tpl.querySelector('.variant');

        if (data) {
            variantDiv.querySelector('input[name="variantName[]"]').value = data.variant_name;
            variantDiv.querySelector('input[name="basePrice[]"]').value = data.base_price;
        }

        document.getElementById(targetId).appendChild(variantDiv);

        // Nếu có dữ liệu màu sắc đi kèm (cho điện thoại)
        if (data && data.colors) {
            data.colors.forEach(c => {
                addColorManual(variantDiv, c);
            });
        }
    }
}

function addColor(btn) {
    // 1. Tìm container chứa các màu của Variant hiện tại
    let colorBox = btn.parentElement.querySelector(".colors");

    // 2. Tìm xem Variant này đang đứng thứ mấy trong form (index)
    // Tìm tất cả các div có class 'variant' trong form hiện tại
    const form = btn.closest('form');
    const allVariants = Array.from(form.querySelectorAll('.variant'));
    const currentVariantDiv = btn.closest('.variant');
    const vIndex = allVariants.indexOf(currentVariantDiv);

    // 3. Clone template
    let tpl = document.getElementById("colorTpl").content.cloneNode(true);

    // 4. Gán index vào trường ẩn
    tpl.querySelector(".variant-index-input").value = vIndex;

    colorBox.appendChild(tpl);
}


function removeBlock(btn) {
    btn.parentElement.remove();
}
function toggleBrand(select) {
    const row = select.closest('.brand-row');
    if (!row) return;

    const input = row.querySelector('input[name="customBrand"]');
    if (!input) return;

    if (select.value === 'custom') {
        input.style.display = 'inline-block';
        input.required = true;
    } else {
        input.style.display = 'none';
        input.value = '';
        input.required = false;
    }
}
function toggleColor(select) {
    const row = select.closest('.color-row');
    if (!row) return;

    const input = row.querySelector('input[name="customColor[]"]');
    if (!input) return;

    if (select.value === 'custom') {
        input.style.display = 'inline-block';
        input.required = true;
    } else {
        input.style.display = 'none';
        input.value = '';
        input.required = false;
    }
}
/**
 *  LOGIC ĐỔ DỮ LIỆU KHI EDIT
 */
document.addEventListener('DOMContentLoaded', () => {
    // Biến productData này được định nghĩa trực tiếp trong file JSP (xem hướng dẫn ở dưới)
    if (typeof productData !== 'undefined' && productData !== null) {
        console.log("Đang ở chế độ Edit, khởi tạo dữ liệu...");

        const categoryId = parseInt(productData.category_id);
        const formId = categoryId === 1 ? 'phoneForm' : 'partForm';
        const techContainerId = categoryId === 1 ? 'phoneTech' : 'partTech';
        const variantContainerId = categoryId === 1 ? 'phoneVariant' : 'partVariant';

        // 1. Hiện form tương ứng
        if (categoryId === 1) showPhone(); else showPart();

        // 2. Điền thông tin cơ bản (Cần chọn đúng form đang hiện)
        const form = document.getElementById(formId);
        form.querySelector('input[name="productName"]').value = productData.product_name;
        form.querySelector('textarea[name="description"]').value = productData.description || "";

        // 3. Đổ thông số kỹ thuật
        if (productData.techs) {
            productData.techs.forEach(t => addTech(techContainerId, t));
        }

        // 4. Đổ phiên bản và màu sắc
        if (productData.variants) {
            productData.variants.forEach(v => addVariant(variantContainerId, v));
        }

        // 5. Ẩn nút chọn loại nếu đang edit
        const typeSelect = document.querySelector('.type-select');
        if (typeSelect) typeSelect.style.display = 'none';

        // 6. Đổi nút submit
        form.querySelector('button[type="submit"]').innerText = "Cập nhật sản phẩm";
    }
});


