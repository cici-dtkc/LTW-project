package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdynamic.dao.VoucherAdminDAO;
import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;

import java.io.IOException;
import java.util.List;

@WebServlet("/user/voucher-detail")
public class VoucherDetailController extends HttpServlet {
    private VoucherAdminDAO dao = new VoucherAdminDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy tất cả voucher còn hạn
        List<VoucherAdmin> listVoucher = dao.getActiveVouchers();
        req.setAttribute("listVoucher", listVoucher);
        req.getRequestDispatcher("/views/user/voucherDetail.jsp").forward(req, resp);

    }
}

