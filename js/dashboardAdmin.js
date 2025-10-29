window.addEventListener('scroll', function () {
    const topbar = document.querySelector('.topbar');

    if (window.scrollY > 50) {
        topbar.classList.add('transparent');
    } else {
        topbar.classList.remove('transparent');
    }
});
// Line Chart - Doanh thu theo ngày
const ctxLine = document.getElementById('salesLine').getContext('2d');
new Chart(ctxLine, {
    type: 'line',
    data: {
        labels: Array.from({ length: 30 }, (_, i) => `Ngày ${i + 1}`),
        datasets: [{
            label: 'Doanh thu (₫)',
            data: Array.from({ length: 30 }, () => Math.floor(Math.random() * 10000000) + 2000000),
            borderWidth: 2,
            fill: true
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false
    }
});

// Pie Chart - Doanh thu theo danh mục
const ctxPie = document.getElementById('categoryPie').getContext('2d');
new Chart(ctxPie, {
    type: 'pie',
    data: {
        labels: ['Iphone', 'Apple', 'Pin dự phòng', 'Dây sạc'],
        datasets: [{
            data: [45, 25, 18, 12],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false
    }
});

// Bar Chart - Sản phẩm bán chạy
const ctxBar = document.getElementById('topProductsBar').getContext('2d');
new Chart(ctxBar, {
    type: 'bar',
    data: {
        labels: ['iPhone 15', 'Samsung galaxy', 'AirPods Pro', 'iPad 10', 'Tai nghe Bluetooth'],
        datasets: [{
            label: 'Sản phẩm bán chạy',
            data: [150, 110, 95, 80, 60],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        indexAxis: 'y'
    }
});
