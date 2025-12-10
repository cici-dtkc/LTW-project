<%--
  Created by IntelliJ IDEA.
  User: mtri2
  Date: 12/6/2025
  Time: 9:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/accountSidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/info-user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assert/css/paymentForm.css">
</head>
<body>
<div id="pageWrapper">
    <jsp:include page="/views/includes/sidebarUser.jsp"/>
    <div id="contentArea">
        <div class="bank-form" id="bankFormContainer">
            <h2>Tài Khoản Ngân Hàng</h2>

            <form id="bankForm">

                <!-- TÊN NGÂN HÀNG -->
                <div class="field" id="fieldBank">
                    <label for="bankSelect">Tên ngân hàng</label>
                    <select id="bankSelect">
                        <option value="Vietcombank">Vietcombank</option>
                        <option value="VietinBank">VietinBank</option>
                        <option value="Techcombank">Techcombank</option>
                        <option value="BIDV">BIDV</option>
                    </select>
                </div>


                <!-- SỐ TÀI KHOẢN -->
                <div class="field" id="fieldAccountNumber">
                    <label for="accountNumber">Số tài khoản</label>
                    <input type="text" id="accountNumber" placeholder="012345678">
                </div>

                <!-- TÊN CHỦ TÀI KHOẢN -->
                <div class="field" id="fieldOwnerName">
                    <label for="ownerName">Tên đầy đủ (VIẾT IN HOA, KHÔNG DẤU)</label>
                    <input type="text" id="ownerName" placeholder="NGUYEN VAN A">
                </div>

                <!-- BUTTON -->
                <div class="btn-group" id="btnGroup">
                    <button type="button" class="back" id="btnBack">Sửa</button>
                    <button type="submit" class="submit" id="btnSubmit">Hoàn Thành</button>
                </div>

            </form>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/paymentForm.js"></script>
</body>
</html>
