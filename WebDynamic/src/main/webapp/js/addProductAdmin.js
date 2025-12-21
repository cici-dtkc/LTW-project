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
