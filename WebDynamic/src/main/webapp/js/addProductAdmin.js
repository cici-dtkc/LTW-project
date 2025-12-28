function showPhone() {
    document.getElementById("phoneForm").style.display = "block";
    document.getElementById("partForm").style.display = "none";
}

function showPart() {
    document.getElementById("phoneForm").style.display = "none";
    document.getElementById("partForm").style.display = "block";
}

function addTech(targetId) {
    let tpl = document.getElementById("techTpl").content.cloneNode(true);
    document.getElementById(targetId).appendChild(tpl);
}
function addVariant(targetId) {
    let tplId = "";

    // Kiểm tra xem nút bấm thuộc về form Điện thoại hay Linh kiện
    if (targetId === 'phoneVariant') {
        tplId = "phoneVariantTpl";
    } else if (targetId === 'partVariant') {
        tplId = "partVariantTpl";
    }

    // Tìm template tương ứng
    const templateElement = document.getElementById(tplId);

    if (templateElement) {
        // Clone nội dung từ template và thêm vào vùng hiển thị
        let tpl = templateElement.content.cloneNode(true);
        document.getElementById(targetId).appendChild(tpl);
    } else {
        console.error("Không tìm thấy template với ID: " + tplId);
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



