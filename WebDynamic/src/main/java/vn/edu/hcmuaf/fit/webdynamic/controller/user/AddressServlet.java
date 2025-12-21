package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.service.AddressService;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/user/addresses")
public class AddressServlet extends HttpServlet {
    private AddressService service;

    @Override
    public void init() {
        service = new AddressService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int userId = user.getId();

        req.setAttribute("addresses", service.getAll(userId));
        req.getRequestDispatcher("/views/user/addresses.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json;charset=UTF-8");
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.getWriter().print("{\"success\":false}");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int userId = user.getId();

        String action = req.getParameter("action");

        boolean ok = false;

        if ("add".equals(action)) {
            Address a = buildAddress(req, userId);
            ok = service.add(a) > 0;
        }
        if ("update".equals(action)) {
            Address a = buildAddress(req, userId);
            a.setId(Integer.parseInt(req.getParameter("id")));
            ok = service.update(a);
        }
        if ("delete".equals(action)) {
            ok = service.delete(
                    Integer.parseInt(req.getParameter("id")), userId);
        }
        if ("set-default".equals(action)) {
            ok = service.setDefault(
                    Integer.parseInt(req.getParameter("id")), userId);
        }

        resp.getWriter().print("{\"success\":" + ok + "}");
    }

    private Address buildAddress(HttpServletRequest r, int userId) {
        Address a = new Address();
        a.setUserId(userId);
        a.setName(r.getParameter("name"));
        a.setPhoneNumber(r.getParameter("phoneNumber"));
        a.setAddress(r.getParameter("fullAddress"));
        a.setStatus("1".equals(r.getParameter("status")) ? 1 : 0);
        return a;
    }

}