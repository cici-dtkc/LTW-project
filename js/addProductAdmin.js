const categoryRadios = document.querySelectorAll('input[name="category"]');
const productInfo = document.getElementById("productInfo");
const variantsSection = document.getElementById("variantsSection");
const attributesOnlySection = document.getElementById("attributesOnlySection");
const submitBtn = document.getElementById("submitBtn");

// Khi click chọn danh mục
categoryRadios.forEach(radio => {
    radio.addEventListener("change", function() {
        const value = this.value;

        // Hiển thị phần chung
        productInfo.style.display = "block";
        submitBtn.style.display = "inline-block";

        if (value === "Điện thoại" || value === "Laptop") {
            variantsSection.style.display = "block";
            attributesOnlySection.style.display = "none";
        } else { // Linh kiện, phụ kiện
            variantsSection.style.display = "none";
            attributesOnlySection.style.display = "block";
        }
    });
});

// Thêm phiên bản mới
document.getElementById("addVariantBtn").addEventListener("click", function() {
    const container = document.getElementById("variantsContainer");

    const variantDiv = document.createElement("div");
    variantDiv.className = "variant";
    variantDiv.innerHTML = `
      <input type="text" name="version[]" placeholder="Tên phiên bản" required>
      <input type="number" name="price[]" placeholder="Giá" required>
      <button type="button" class="removeVariantBtn" onclick="removeVariant(this)">Xóa phiên bản</button>
      <div class="attributesContainer">
          <h4>Thông số kỹ thuật</h4>
          <button type="button" class="addAttrBtn" onclick="addAttribute(this)">Thêm thông số</button>
      </div>
  `;
    container.appendChild(variantDiv);
});

// Xóa phiên bản
function removeVariant(button) {
    const container = document.getElementById("variantsContainer");
    if (container.children.length > 1) {
        button.parentElement.remove();
    } else {
        alert("Cần ít nhất 1 phiên bản!");
    }
}

// Thêm attribute key-value
function addAttribute(button) {
    const attrContainer = button.parentElement;
    const row = document.createElement("div");
    row.className = "attributeRow";
    row.innerHTML = `
      <input type="text" placeholder="Tên thông số" required>
      <input type="text" placeholder="Giá trị" required>
      <button type="button" onclick="removeAttribute(this)">Xóa</button>
  `;
    attrContainer.appendChild(row);
}

function removeAttribute(button) {
    button.parentElement.remove();
}

// Submit form
document.getElementById("productForm").addEventListener("submit", function(e) {
    e.preventDefault();
    alert("Đã thêm sản phẩm");
});
