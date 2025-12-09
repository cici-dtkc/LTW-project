package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdynamic.dao.VoucherAdminDAO;
import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/admin/vouchers")
public class VoucherAdminController extends HttpServlet {
    private VoucherAdminDAO dao = new VoucherAdminDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        String keyword = req.getParameter("keyword") == null ? "" : req.getParameter("keyword");
        int status = req.getParameter("status") == null ? -1 : Integer.parseInt(req.getParameter("status"));

        int limit = 10;
        int offset = (page - 1) * limit;

        List<VoucherAdmin> list = dao.search(keyword, status, limit, offset);
        int total = dao.countSearch(keyword, status);
        int totalPage = (int) Math.ceil((double) total / limit);

        req.setAttribute("vouchers", list);
        req.setAttribute("page", page);
        req.setAttribute("totalVoucher", total);
        req.setAttribute("totalPage", totalPage);
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);

        req.getRequestDispatcher("/views/admin/vouchersAdmin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        if ("toggleStatus".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            int st = Integer.parseInt(req.getParameter("status"));
            dao.updateStatus(id, st == 1 ? 0 : 1);
        }
        else if ("addVoucher".equals(action)) {
            String code = req.getParameter("promoCode");
            int type = Integer.parseInt(req.getParameter("promoType"));
            int discount = Integer.parseInt(req.getParameter("discountValue"));
            int maxDiscount = Integer.parseInt(req.getParameter("maxDiscount"));
            int minOrder = Integer.parseInt(req.getParameter("minOrder"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");

            VoucherAdmin vc = new VoucherAdmin();
            vc.setVoucherCode(code);
            vc.setType(type);
            vc.setDiscountAmount(discount);
            vc.setMaxReduce(maxDiscount);
            vc.setMinOrderValue(minOrder);
            vc.setQuantity(quantity);
            vc.setStartDate(java.time.LocalDate.parse(startDate).atStartOfDay());
            vc.setEndDate(java.time.LocalDate.parse(endDate).atStartOfDay());
            vc.setStatus(1);

            dao.insert(vc);
        }
        else if ("updateVoucher".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            String code = req.getParameter("promoCode");
            int type = Integer.parseInt(req.getParameter("promoType"));
            int discount = Integer.parseInt(req.getParameter("discountValue"));
            int maxDiscount = Integer.parseInt(req.getParameter("maxDiscount"));
            int minOrder = Integer.parseInt(req.getParameter("minOrder"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");

            VoucherAdmin vc = new VoucherAdmin();
            vc.setId(id);
            vc.setVoucherCode(code);
            vc.setType(type);
            vc.setDiscountAmount(discount);
            vc.setMaxReduce(maxDiscount);
            vc.setMinOrderValue(minOrder);
            vc.setQuantity(quantity);
            vc.setStartDate(LocalDate.parse(startDate).atStartOfDay());
            vc.setEndDate(LocalDate.parse(endDate).atStartOfDay());

            dao.update(vc);
        }


        resp.sendRedirect(req.getContextPath() + "/admin/vouchers");
    }


}