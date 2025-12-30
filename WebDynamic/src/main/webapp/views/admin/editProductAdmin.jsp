<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa sản phẩm - Quản trị hệ thống</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/editProduct.css">
</head>
<body>

<div class="container">
    <div class="header-section">
        <h2>Chỉnh sửa chi tiết phiên bản</h2>
        <span class="product-tag">Điện thoại</span>
    </div>

    <div class="product-switch">
        <button type="button" id="btnPhone" class="active">📱 Điện thoại</button>
        <button type="button" id="btnAccessory">🔌 Linh kiện</button>
    </div>

    <form id="editFormPhone" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/updatePhone">
        <input type="hidden" name="productId" value="${phone.id}">

        <div class="card">
            <h3>ℹ️ Thông tin điện thoại</h3>
            <div class="form-grid">
                <div class="form-group">
                    <label>Tên sản phẩm</label>
                    <input type="text" name="phoneName" value="${phone.name}" readonly class="readonly-input">
                </div>

                <div class="form-group">
                    <label>Hãng sản xuất</label>
                    <input type="text" name="phoneBrand" value="${phone.brand}" readonly class="readonly-input">
                </div>

                <div class="image-management">
                    <div class="form-group image-box">
                        <label>Ảnh hiện tại</label>
                        <div class="current-image-box">
                            <img src="${pageContext.request.contextPath}/${phone.image}" alt="Sản phẩm">
                        </div>
                    </div>

                    <div class="upload-action">
                        <label>Thay đổi hình ảnh</label>
                        <input type="file" name="imagePhone" accept="image/*" class="image-input">
                        <p class="image-note">* Chọn file mới nếu muốn thay đổi ảnh đại diện.</p>
                    </div>
                </div>

                <div class="form-group full">
                    <label>Mô tả tổng quát</label>
                    <textarea rows="4" name="description">${phone.description}</textarea>
                </div>
            </div>
        </div>

        <div class="action-buttons">
            <button type="button" class="btn-cancel" onclick="window.history.back()">HỦY BỎ</button>
            <button type="submit" class="btn-save">LƯU CẬP NHẬT</button>
        </div>
    </form>

    <form id="editFormAccessory" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/updateAccessory" style="display: none;">
        <input type="hidden" name="accessoryId" value="${accessory.id}">

        <div class="card">
            <h3>🔌 Thông tin linh kiện</h3>
            <div class="form-grid">
                <div class="form-group">
                    <label>Tên linh kiện</label>
                    <input type="text" name="accName" value="${accessory.name}" readonly class="readonly-input">
                </div>

                <div class="form-group">
                    <label>Hãng sản xuất</label>
                    <input type="text" name="accBrand" value="${accessory.brand}" readonly class="readonly-input">
                </div>

                <div class="image-management">
                    <div class="form-group image-box">
                        <label>Ảnh hiện tại</label>
                        <div class="current-image-box">
                            <img src="${pageContext.request.contextPath}/${accessory.image}" alt="Linh kiện">
                        </div>
                    </div>

                    <div class="upload-action">
                        <label>Thay đổi hình ảnh</label>
                        <input type="file" name="imageAccessory" accept="image/*" class="image-input">
                    </div>
                </div>

                <div class="form-group full">
                    <label>Mô tả linh kiện</label>
                    <textarea rows="4" name="descriptionAccessory">${accessory.description}</textarea>
                </div>
            </div>
        </div>

        <div class="action-buttons">
            <button type="button" class="btn-cancel" onclick="window.history.back()">HỦY BỎ</button>
            <button type="submit" class="btn-save">LƯU LINH KIỆN</button>
        </div>
    </form>
</div>

<script src="${pageContext.request.contextPath}/js/editProduct.js"></script>

</body>
</html>