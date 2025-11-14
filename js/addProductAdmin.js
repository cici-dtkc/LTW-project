// ===== Hiển thị form theo category =====
const phoneRadio = document.getElementById("phone");
const partRadio = document.getElementById("part");
const phoneForm = document.getElementById("phoneForm");
const partForm = document.getElementById("partForm");

phoneRadio.addEventListener("change", () => {
    phoneForm.style.display = "block";
    partForm.style.display = "none";
});

partRadio.addEventListener("change", () => {
    phoneForm.style.display = "none";
    partForm.style.display = "block";
});

// ===== Function thêm Variant =====
function addVariant(containerId){
    const container = document.getElementById(containerId);
    const template = document.getElementById("variantTemplate");
    const clone = template.content.cloneNode(true);
    clone.querySelector(".removeBtn").addEventListener("click", e => {
        e.target.parentElement.remove();
    });
    container.appendChild(clone);
}

// ===== Function thêm Attribute =====
function addAttribute(buttonId){
    const container = document.getElementById(buttonId);
    const template = document.getElementById("attributeTemplate");
    const clone = template.content.cloneNode(true);
    clone.querySelector(".removeBtn").addEventListener("click", e => {
        e.target.parentElement.remove();
    });
    container.appendChild(clone);
}

// ===== EVENTS =====
// Phone
document.getElementById("addPhoneVariant").addEventListener("click", ()=> addVariant("phoneVariantsContainer"));
document.getElementById("addPhoneAttribute").addEventListener("click", ()=> addAttribute("phoneAttributesContainer"));

// Part
document.getElementById("addPartVariant").addEventListener("click", ()=> addVariant("partVariantsContainer"));
document.getElementById("addPartAttribute").addEventListener("click", ()=> addAttribute("partAttributesContainer"));

// ===== Optional: demo submit =====
phoneForm.addEventListener("submit", e=>{ e.preventDefault(); alert("Đã thêm điện thoại (demo)")});
partForm.addEventListener("submit", e=>{ e.preventDefault(); alert("Đã thêm linh kiện (demo)")});