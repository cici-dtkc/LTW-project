const dashAddPromo = document.getElementById("dashAddPromo");
if (dashAddPromo) {
    dashAddPromo.addEventListener("click", function () {
        window.location.href = "vouchersAdmin.jsp?addPromo=true";
    });
}

window.addEventListener('scroll', function () {
    const topbar = document.querySelector('.topbar');
    if (!topbar) return;

    if (window.scrollY > 50) {
        topbar.classList.add('transparent');
    } else {
        topbar.classList.remove('transparent');
    }
});
