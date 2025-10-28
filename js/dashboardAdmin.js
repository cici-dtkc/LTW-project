window.addEventListener('scroll', function () {
    const topbar = document.querySelector('.topbar');

    if (window.scrollY > 50) {
        topbar.classList.add('transparent');
    } else {
        topbar.classList.remove('transparent');
    }
});
