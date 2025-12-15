package vn.edu.hcmuaf.fit.webdynamic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.edu.hcmuaf.fit.webdynamic.dao.VoucherAdminDaoImpl;
import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;
import vn.edu.hcmuaf.fit.webdynamic.service.VoucherAdminService;
import vn.edu.hcmuaf.fit.webdynamic.service.VoucherAdminServiceImpl;
import vn.edu.hcmuaf.fit.webdynamic.validation.ValidationException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name ="VoucherAdminController",urlPatterns = {"/admin/vouchers"})
public class VoucherAdminServlet extends HttpServlet {

    private VoucherAdminService service;

    @Override
    public void init() {
        service = new VoucherAdminServiceImpl(new VoucherAdminDaoImpl());
    }

    // ================== GET ==================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        String statusParam = req.getParameter("status");
        String pageParam = req.getParameter("page");

        Integer status = null;
        if (statusParam != null && !statusParam.isBlank() && !statusParam.equals("-1")) {
            status = Integer.parseInt(statusParam);
        }

        int page = (pageParam == null) ? 1 : Integer.parseInt(pageParam);
        int limit = 10;

        int totalVoucher = service.countAll(keyword, status);
        int totalPage = (int) Math.ceil((double) totalVoucher / limit);

        List<VoucherAdmin> vouchers = service.getAll(keyword, status, page, limit);

        req.setAttribute("vouchers", vouchers);
        req.setAttribute("totalVoucher", totalVoucher);
        req.setAttribute("totalPage", totalPage);
        req.setAttribute("page", page);
        req.setAttribute("limit", limit);
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);

        req.getRequestDispatcher("/views/admin/vouchersAdmin.jsp")
                .forward(req, resp);
    }

    // ================== POST ==================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null || action.isBlank()) {
            req.setAttribute("error", "Action không được xác định");
            doGet(req, resp);
            return;
        }

        try {
            switch (action) {

                case "addVoucher" -> {
                    VoucherAdmin v = buildVoucher(req);
                    v.setStatus(1);
                    service.createVoucher(v);
                }
                case "update" -> {
                    int id = parseInt(req, "id");
                    VoucherAdmin old = service.getById(id);
                    if (old == null) {
                        throw new ValidationException("Không tìm thấy voucher với ID: " + id);
                    }
                    VoucherAdmin v = buildVoucher(req);
                    v.setId(old.getId());
                    v.setStatus(old.getStatus());
                    service.updateVoucher(v);
                }

                case "toggle" -> {
                    service.toggleStatus(parseInt(req, "id"));
                }

                case "delete" -> {
                    service.deleteVoucher(parseInt(req, "id"));
                }

                default -> {
                    throw new ValidationException("Action không hợp lệ: " + action);
                }
            }

            resp.sendRedirect(req.getContextPath() + "/admin/vouchers");

        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }

    }

    private VoucherAdmin buildVoucher(HttpServletRequest req) {
        VoucherAdmin v = new VoucherAdmin();

        v.setVoucherCode(req.getParameter("voucherCode"));
        v.setDiscountAmount(parseInt(req, "discountAmount"));
        v.setType(parseInt(req, "type"));
        v.setQuantity(parseInt(req, "quantity"));
        v.setMinOrderValue(parseInt(req, "minOrderValue"));
        v.setMaxReduce(parseInt(req, "maxReduce"));
        v.setStartDate(parseDate(req, "startDate"));
        v.setEndDate(parseDate(req, "endDate"));

        return v;
    }

    // ================== PARSE ==================
    private int parseInt(HttpServletRequest req, String name) {
        try {
            return Integer.parseInt(req.getParameter(name));
        } catch (Exception e) {
            throw new ValidationException(name + " không hợp lệ");
        }
    }

    private double parseDouble(HttpServletRequest req, String name) {
        try {
            return Double.parseDouble(req.getParameter(name));
        } catch (Exception e) {
            throw new ValidationException(name + " không hợp lệ");
        }
    }

    private LocalDateTime parseDate(HttpServletRequest req, String name) {
        try {
            String value = req.getParameter(name);
            if (value == null || value.isBlank()) return null;

            LocalDate date = LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
            return date.atStartOfDay();

        } catch (Exception e) {
            throw new ValidationException(name + " không hợp lệ");
        }
    }
}