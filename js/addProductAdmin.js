const categoryRadios = document.querySelectorAll('input[name="category"]');
const variantsSection = document.getElementById("variantsSection");
const attributesOnlySection = document.getElementById("attributesOnlySection");
const submitBtn = document.getElementById("submitBtn");

// Hiển thị sections theo danh mục
categoryRadios.forEach(radio => {
    radio.addEventListener("change", function() {
        attributesOnlySection.style.display = "block";
        variantsSection.style.display = "block"; // hiển thị variants cho tất cả danh mục
        submitBtn.style.display = "inline-block";
    });
});

// Thêm variant
document.getElementById("addVariantBtn").addEventListener("click",function(){
    const container=document.getElementById("variantsContainer");
    const div=document.createElement("div");
    div.className="variant";
    div.innerHTML=`
    <input type="text" name="variant_name[]" placeholder="Tên phiên bản (màu/dung lượng)" required>
    <input type="number" name="variant_price[]" placeholder="Giá" required>
    <input type="number" name="variant_quantity[]" placeholder="Số lượng" required>
    <button type="button" class="removeBtn" onclick="removeVariant(this)">Xóa</button>
  `;
    container.appendChild(div);
});

// Xóa variant
function removeVariant(button){
    const container=document.getElementById("variantsContainer");
    if(container.children.length>1) button.parentElement.remove();
    else alert("Cần ít nhất 1 phiên bản!");
}

// Thêm attribute
function addAttribute(button){
    const attrContainer=button.parentElement;
    const row=document.createElement("div");
    row.className="attributeRow";
    row.innerHTML=`
    <input type="text" placeholder="Tên thông số" required>
    <input type="text" placeholder="Giá trị" required>
    <button type="button" class="removeBtn" onclick="removeAttribute(this)">Xóa</button>
  `;
    attrContainer.appendChild(row);
}
function removeAttribute(button){button.parentElement.remove();}

// Submit
document.getElementById("productForm").addEventListener("submit",function(e){
    e.preventDefault();
    alert("Đã thêm sản phẩm (cần backend lưu vào DB)");
});