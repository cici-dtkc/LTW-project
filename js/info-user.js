document.addEventListener("DOMContentLoaded", function () {
  const btnEdit = document.getElementById("btn-edit");
  const btnSave = document.getElementById("btn-save");
  const btnLogout = document.getElementById("btn-logout");

  const fullNameInput = document.getElementById("fullName");
  const emailInput = document.getElementById("emailInput");
  const emailError = document.getElementById("emailError");

  const avatarInput = document.getElementById("avatarInput");
  const avatarPreview = document.getElementById("user-avatar");
  const chooseAvatarBtn = document.querySelector(".btn.small.outline");

  let isEditing = false;

  // ==== Nút "Chọn ảnh" mở input file ====
  chooseAvatarBtn.addEventListener("click", () => {
    avatarInput.click();
  });

  // ==== Xem trước ảnh đại diện ====
  avatarInput.addEventListener("change", function () {
    const file = this.files[0];
    if (file) {
      if (!file.type.startsWith("image/")) {
        return;
      }
      const reader = new FileReader();
      reader.onload = function (e) {
        avatarPreview.src = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  });

  // ==== Khi nhấn "Chỉnh sửa" ====
  btnEdit.addEventListener("click", function () {
    isEditing = true;

    // Bật input để chỉnh sửa
    fullNameInput.disabled = false;
    emailInput.disabled = false;

    // Chuyển nút
    btnEdit.classList.add("hidden");
    btnLogout.classList.add("hidden");
    btnSave.classList.remove("hidden");
  });

  // ==== Khi nhấn "Lưu thay đổi" ====
  btnSave.addEventListener("click", function () {
    const fullName = fullNameInput.value.trim();
    const email = emailInput.value.trim();

    // Kiểm tra hợp lệ
    if (fullName === "") {
      alert("Vui lòng nhập họ tên!");
      fullNameInput.focus();
      return;
    }

    if (!validateEmail(email)) {
      emailError.textContent = "Email không hợp lệ!";
      emailInput.focus();
      return;
    } else {
      emailError.textContent = "";
    }

    // Giả lập lưu dữ liệu (API hoặc localStorage tuỳ bạn)
    console.log("Dữ liệu mới:", { fullName, email });

    // Khoá lại input sau khi lưu
    fullNameInput.disabled = true;
    emailInput.disabled = true;

    // Hiển thị lại nút ban đầu
    btnEdit.classList.remove("hidden");
    btnLogout.classList.remove("hidden");
    btnSave.classList.add("hidden");

    isEditing = false;

    alert("Lưu thay đổi thành công!");
  });

  // ==== Khi nhấn "Đăng xuất" ====
  btnLogout.addEventListener("click", function () {
       window.location.href = "login.html";
  });

  // ==== Hàm kiểm tra định dạng email ====
  function validateEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }
});
// ===========================
// SIDEBAR submenu toggle
// ===========================
const menuAccountMain = document.getElementById("menuAccountMain");
const accountSubmenu = document.getElementById("accountSubmenu");

if (menuAccountMain && accountSubmenu) {
  accountSubmenu.classList.add("open");
  menuAccountMain.addEventListener("click", (e) => {
    e.preventDefault();
    accountSubmenu.classList.toggle("open");
  });
}
