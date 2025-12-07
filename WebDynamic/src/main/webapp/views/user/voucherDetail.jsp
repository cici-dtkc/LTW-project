<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết voucher</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/voucherDetail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/accountSidebar.css">
</head>
<body>
<jsp:include page="../includes/header.jsp"/>

<div class="voucher-container page-wrapper">

    <jsp:include page="../includes/sideBarAdmin.jsp"/>
    <!-- Mẫu voucher -->
    <div class="voucher-list">
        <div class="voucher">
            <div class="voucher-left">
                <div class="icon"><img src="${pageContext.request.contextPath}/assert/img/logo.png" alt="logo"></div>
            </div>
            <div class="voucher-right">
                <div>
                    <h3>Giảm 20%</h3>
                    <p>Giảm tối đa 100kđ</p>
                    <p>Đơn tối thiểu 70kđ</p>
                </div>
                <div>
                    <button>Áp dụng</button>
                    <a href="#">Điều kiện</a>
                </div>
            </div>
            <div class="badge">x5</div>
        </div>

        <div class="voucher">
            <div class="voucher-left">
                <div class="icon"><img src="${pageContext.request.contextPath}/assert/img/logo.png" alt="logo"></div>
            </div>
            <div class="voucher-right">
                <div>
                    <h3>Giảm 20%</h3>
                    <p>Giảm tối đa 100kđ</p>
                    <p>Đơn tối thiểu 100kđ</p>
                </div>
                <div>
                    <button>Áp dụng</button>
                    <a href="#">Điều kiện</a>
                </div>
            </div>
            <div class="badge">x5</div>
        </div>

        <div class="voucher">
            <div class="voucher-left">
                <div class="icon"><img src="${pageContext.request.contextPath}/assert/img/logo.png" alt="logo"></div>
            </div>
            <div class="voucher-right">
                <div>
                    <h3>Giảm 20%</h3>
                    <p>Giảm tối đa 100kđ</p>
                    <p>Đơn tối thiểu 150kđ</p>
                </div>
                <div>
                    <button>Áp dụng</button>
                    <a href="#">Điều kiện</a>
                </div>
            </div>
            <div class="badge">x5</div>
        </div>

        <div class="voucher">
            <div class="voucher-left">
                <div class="icon"><img src="${pageContext.request.contextPath}/assert/img/logo.png" alt="logo"></div>
            </div>
            <div class="voucher-right">
                <div>
                    <h3>Giảm 20%</h3>
                    <p>Giảm tối đa 350kđ</p>
                    <p>Đơn tối thiểu 1trđ</p>
                </div>
                <div>
                    <button>Áp dụng</button>
                    <a href="#">Điều kiện</a>
                </div>
            </div>
            <div class="badge">x5</div>
        </div>
        <!-- Voucher hết lượt sử dụng -->
        <div class="voucher expired">
            <div class="voucher-left">
                <div class="icon"><img src="${pageContext.request.contextPath}/assert/img/logo.png" alt="logo"></div>
            </div>
            <div class="voucher-right">
                <h3>Giảm 22% Giảm tối đa 100kđ</h3>
                <p>Đơn tối thiểu 350kđ</p>
                <small>Có hiệu lực từ 04 Th11<a href="#">Điều Kiện</a></small>
            </div>
        </div>
        <div class="voucher">
            <div class="voucher-left">
                <div class="icon"><img src="${pageContext.request.contextPath}/assert/img/logo.png" alt="logo"></div>
            </div>
            <div class="voucher-right">
                <div>
                    <h3>Giảm 20%</h3>
                    <p>Giảm tối đa 350kđ</p>
                    <p>Đơn tối thiểu 1trđ</p>
                </div>
                <div>
                    <button>Áp dụng</button>
                    <a href="#">Điều kiện</a>
                </div>
            </div>
            <div class="badge">x5</div>
        </div>
        <!-- Voucher hết lượt sử dụng -->
        <div class="voucher expired">
            <div class="voucher-left">
                <div class="icon"><img src="${pageContext.request.contextPath}/assert/img/logo.png" alt="logo"></div>
            </div>
            <div class="voucher-right">
                <h3>Giảm 22% Giảm tối đa 100kđ</h3>
                <p>Đơn tối thiểu 350kđ</p>
                <small>Có hiệu lực từ 04 Th11<a href="#">Điều Kiện</a></small>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp"/>

<script src="${pageContext.request.contextPath}/js/header.js"></script>
<script>
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
</script>
</body>
</html>
