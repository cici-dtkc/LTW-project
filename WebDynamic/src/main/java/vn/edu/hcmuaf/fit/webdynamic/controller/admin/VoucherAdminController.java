package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdynamic.dao.VoucherAdminDAO;
import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;

import java.io.IOException;
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

        resp.sendRedirect(req.getContextPath() + "/admin/vouchers");
    }
}
