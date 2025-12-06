document.addEventListener("DOMContentLoaded", function () {
  const btnEdit = document.getElementById("btn-edit");
  const btnSave = document.getElementById("btn-save");
  const btnLogout = document.getElementById("btn-logout");

  const firstNameInput = document.getElementById("firstName");
  const lastNameInput = document.getElementById("lastName");
  const emailInput = document.getElementById("emailInput");
  const emailError = document.getElementById("emailError");

  const avatarInput = document.getElementById("avatarInput");
  const avatarPreview = document.getElementById("user-avatar");
  const chooseAvatarBtn = document.querySelector(".btn.small.outline");

  let isEditing = false;

  // ==== Khi nhấn "Chỉnh sửa" ====
  btnEdit.addEventListener("click", function () {
    isEditing = true;

    // Bật input để chỉnh sửa
    firstNameInput.disabled = false;
    lastNameInput.disabled = false;
    emailInput.disabled = false;

    // Chuyển nút
    btnEdit.classList.add("hidden");
    btnLogout.classList.add("hidden");
    btnSave.classList.remove("hidden");
  });

  // ==== Khi nhấn "Lưu thay đổi" ====
  btnSave.addEventListener("click", function () {

    // Khoá lại input sau khi lưu
    firstNameInput.disabled = true;
    lastNameInput.disabled = true;
    emailInput.disabled = true;

    // Hiển thị lại nút ban đầu
    btnEdit.classList.remove("hidden");
    btnLogout.classList.remove("hidden");
    btnSave.classList.add("hidden");

    isEditing = false;

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
