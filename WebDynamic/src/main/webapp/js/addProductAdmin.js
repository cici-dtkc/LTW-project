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
    let tpl = document.getElementById("variantTpl").content.cloneNode(true);
    document.getElementById(targetId).appendChild(tpl);
}

function addColor(btn) {
    let colorBox = btn.parentElement.querySelector(".colors");
    let tpl = document.getElementById("colorTpl").content.cloneNode(true);
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



