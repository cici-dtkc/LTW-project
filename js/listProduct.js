document.querySelectorAll("#sortList li").forEach(item => {
    item.addEventListener("click", () => {
        document.querySelectorAll("#sortList li").forEach(li => li.classList.remove("active"));
        item.classList.add("active");
    });
});
