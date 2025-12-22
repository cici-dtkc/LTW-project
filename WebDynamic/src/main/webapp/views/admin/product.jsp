<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý sản phẩm - Admin</title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/sidebarAdmin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/productAdmin.css">
</head>

<body>
<div class="app">

    <%@ include file="/views/includes/sideBarAdmin.jsp" %>

    <div class="container">

        <!-- TOP BAR -->
        <div class="topbar">
            <div>
                <h2>Sản phẩm</h2>
                <div class="muted">Quản lý sản phẩm</div>
            </div>
            <div class="muted">Xin chào, Admin</div>
        </div>

        <!-- TOOLBAR -->
        <div class="toolbar-wrapper">

            <form class="toolbar" method="get"
                  action="${pageContext.request.contextPath}/admin/products">

                <input type="text" name="keyword"
                       value="${keyword}"
                       placeholder="Tìm kiếm sản phẩm...">

                <select name="status">
                    <option value="">Tất cả trạng thái</option>
                    <option value="1" ${status == 1 ? "selected" : ""}>Đang hiển thị</option>
                    <option value="0" ${status == 0 ? "selected" : ""}>Đang ẩn</option>
                </select>

                <select name="categoryId">
                    <option value="">Tất cả danh mục</option>
                    <option value="1" ${categoryId == 1 ? "selected" : ""}>Điện thoại</option>
                    <option value="2" ${categoryId == 2 ? "selected" : ""}>Linh kiện</option>
                </select>

                <button class="btn-filter">Tìm kiếm</button>
            </form>

            <a class="btn-add"
               href="${pageContext.request.contextPath}/admin/products/add">
                + Thêm sản phẩm
            </a>

        </div>

        <!-- TABLE -->
        <table class="table-view">
            <thead>
            <tr>
                <th>Hình ảnh</th>
                <th>Tên</th>
                <th>Giá</th>
                <th>Danh mục</th>
                <th>Phiên bản</th>
                <th>Tồn kho</th>
                <th>Hành động</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${products}" var="p">
                <c:forEach items="${p.variants}" var="v">

                    <tr>
                        <td>
                            <img src="${pageContext.request.contextPath}/assert/img/product/${p.mainImage}">
                        </td>

                        <td class="text-left">${p.name}</td>

                        <td>
                            <fmt:formatNumber value="${v.basePrice}" groupingUsed="true"/>₫
                        </td>

                        <td>${p.category.name}</td>

                        <td>${v.name}</td>

                        <td>
                            <c:set var="totalQty" value="0"/>
                            <c:forEach items="${v.colors}" var="c">
                                <c:set var="totalQty" value="${totalQty + c.quantity}"/>
                            </c:forEach>
                                ${totalQty}
                        </td>

                        <td class="actionsProduct">
                            <a class="btn-edit"
                               href="${pageContext.request.contextPath}/admin/products/edit?id=${p.id}">
                                <i class="fa-solid fa-pencil"></i>
                            </a>


                            <form action="${pageContext.request.contextPath}/admin/products"
                                  method="post"
                                  style="display:inline">

                                <input type="hidden" name="action" value="toggle"/>
                                <input type="hidden" name="id" value="${v.id}"/>

                                <button type="submit"
                                        class="btn-toggle"
                                        title="${v.status == 1 ? 'Ẩn phiên bản' : 'Hiển thị phiên bản'}">

                                    <i class="fa-solid ${v.status == 1 ? 'fa-eye' : 'fa-eye-slash'}"></i>
                                </button>
                            </form>



                        </td>
                    </tr>

                </c:forEach>
            </c:forEach>
            </tbody>
        </table>


        <div class="footer">

            <div class="pagination">

                <c:if test="${page > 1}">
                    <a href="${pageContext.request.contextPath}/admin/products?page=${page-1}&keyword=${keyword}&status=${status}&categoryId=${categoryId}">
                        ‹
                    </a>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == page}">
                            <span class="active">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/admin/products?page=${i}&keyword=${keyword}&status=${status}&categoryId=${categoryId}">
                                    ${i}
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${page < totalPages}">
                    <a href="${pageContext.request.contextPath}/admin/products?page=${page+1}&keyword=${keyword}&status=${status}&categoryId=${categoryId}">
                        ›
                    </a>
                </c:if>

            </div>
        </div>


    </div>
</div>

<script src="${pageContext.request.contextPath}/js/sidebarAdmin.js"></script>
<script src="${pageContext.request.contextPath}/js/productAdmin.js"></script>
</body>
</html>
