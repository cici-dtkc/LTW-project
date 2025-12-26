<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm sản phẩm</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/addProductAdmin.css">
</head>
<body>

<div class="add-product">
    <div class="container">

        <h2>Thêm sản phẩm</h2>

        <!-- CHỌN LOẠI -->
        <div class="type-select">
            <button type="button" onclick="showPhone()">Điện thoại</button>
            <button type="button" onclick="showPart()">Linh kiện</button>
        </div>

        <!-- ===== FORM ĐIỆN THOẠI ===== -->
        <form id="phoneForm"
              class="form hidden"
              method="post"
              action="${pageContext.request.contextPath}/admin/product/add"
              enctype="multipart/form-data">
            <!-- NOTE: BỔ SUNG categoryId -->
            <input type="hidden" name="categoryId" value="1">

        <h3>Điện thoại</h3>

            <label>Ảnh đại diện</label>
            <input type="file" name="productImage" accept="image/*" required>

            <input name="productName" placeholder="Tên sản phẩm" required>

            <div class="brand-row">
                <select name="brandId" onchange="toggleBrand(this)" required>
                    <option value="">-- Chọn hãng --</option>
                    <option value="1">Apple</option>
                    <option value="2">Samsung</option>
                    <option value="3">Xiaomi</option>
                    <option value="custom">Khác...</option>
                </select>

                <input type="text"
                       name="customBrand"
                       placeholder="Nhập hãng mới"
                       style="display:none">

            </div>

            <textarea name="description" placeholder="Mô tả"></textarea>

            <!-- THÔNG SỐ -->
            <h4>Thông số</h4>
            <div id="phoneTech"></div>
            <button type="button" onclick="addTech('phoneTech')">Thêm thông số</button>

            <!-- PHIÊN BẢN -->
            <h4>Phiên bản</h4>
            <div id="phoneVariant"></div>
            <button type="button" onclick="addVariant('phoneVariant')">Thêm phiên bản</button>

            <button type="submit">Lưu điện thoại</button>
        </form>

        <!-- ===== FORM LINH KIỆN ===== -->
        <form id="partForm"
              class="form hidden"
              method="post"
              action="${pageContext.request.contextPath}/admin/product/add"
              enctype="multipart/form-data">
            <input type="hidden" name="categoryId" value="2">

        <h3>Linh kiện</h3>

            <label>Ảnh đại diện</label>
            <input type="file" name="productImage" accept="image/*" required>

            <input name="productName" placeholder="Tên linh kiện" required>
            <div class="brand-row">
                <select name="brandId" onchange="toggleBrand(this)" required>
                    <option value="">-- Chọn hãng --</option>
                    <option value="1">Apple</option>
                    <option value="2">Samsung</option>
                    <option value="3">Xiaomi</option>
                    <option value="custom">Khác...</option>
                </select>

                <input type="text"
                       name="customBrand"
                       placeholder="Nhập hãng mới"
                       style="display:none">

            </div>

            <select name="subcategory">
                <option>Pin</option>
                <option>Màn hình</option>
            </select>

            <!-- THÔNG SỐ -->
            <h4>Thông số</h4>
            <div id="partTech"></div>
            <button type="button" onclick="addTech('partTech')">Thêm thông số</button>

            <!-- PHIÊN BẢN -->
            <h4>Phiên bản</h4>
            <div id="partVariant"></div>
            <button type="button" onclick="addVariant('partVariant')">Thêm phiên bản</button>

            <button type="submit">Lưu linh kiện</button>
        </form>

    </div>
</div>

<!-- ===== TEMPLATE ===== -->

<template id="techTpl">
    <div class="tech">
        <!-- NOTE: thêm [] -->
        <input name="techName[]" placeholder="Tên thông số">
        <input name="techValue[]" placeholder="Giá trị">
        <input name="techPriority[]" value="5">
        <button type="button" onclick="removeBlock(this)">✖</button>
    </div>
</template>

<template id="variantTpl">
    <div class="variant">
        <!-- NOTE: thêm [] -->
        <input name="variantName[]" placeholder="Tên phiên bản">
        <input name="basePrice[]" placeholder="Giá gốc">

        <div class="colors"></div>

        <button type="button" onclick="addColor(this)">Thêm màu</button>
        <button type="button" class="danger" onclick="removeBlock(this)">Xóa phiên bản</button>
    </div>
</template>

<template id="colorTpl">
    <div class="color">
        <!-- NOTE: đổi name -->
        <div class="color-row">
            <select name="colorId[]" onchange="toggleColor(this)">
            <option value="">-- Chọn màu --</option>
                <option value="1">Đen</option>
                <option value="2">Trắng</option>
                <option value="3">Xanh</option>
                <option value="4">Tím</option>
                <option value="5">Đỏ Lựu</option>
                <option value="6">Xám</option>
                <option value="custom">Khác...</option>
            </select>

            <input type="text"
                   name="customColor[]"
                   placeholder="Nhập màu mới"
                   style="display:none">

        </div>
        <input name="colorPrice[]" placeholder="Giá màu">
        <input name="quantity[]" placeholder="Số lượng">
        <input name="sku[]" placeholder="SKU">
        <input type="file" name="colorImage[]">


        <button type="button" onclick="removeBlock(this)">✖</button>
    </div>
</template>

<script src="${pageContext.request.contextPath}/js/addProductAdmin.js"></script>

</body>
</html>
