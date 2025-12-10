package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;
import vn.edu.hcmuaf.fit.webdynamic.service.AddressService;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AddressServlet", urlPatterns = { "/user/addresses" })
public class AddressServlet extends HttpServlet {
    private UserService userService;
    private AddressService addressService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
        this.addressService = new AddressService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy userId từ session giống cách các servlet khác xử lý
        // HttpSession session = request.getSession(false);
        // Integer userId = null;
        // if (session != null) {
        // Object idAttr = session.getAttribute("userId");
        // if (idAttr instanceof Integer)
        // userId = (Integer) idAttr;
        // else if (idAttr instanceof String) {
        // try {
        // userId = Integer.parseInt((String) idAttr);
        // } catch (NumberFormatException ignored) {
        // }
        // }
        // }
        //
        // if (userId == null) {
        // // chưa đăng nhập -> redirect tới trang login
        // response.sendRedirect(request.getContextPath() + "/login.jsp");
        // return;
        // }

        // List<Address> addresses = addressService.getAllAddressesByUserId(userId);
        // request.setAttribute("addresses", addresses);

        request.getRequestDispatcher("/views/user/addresses.jsp").forward(request, response);
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json;charset=UTF-8");
//
//        HttpSession session = request.getSession(false);
//        Integer userId = null;
//        if (session != null) {
//            Object idAttr = session.getAttribute("userId");
//            if (idAttr instanceof Integer)
//                userId = (Integer) idAttr;
//            else if (idAttr instanceof String) {
//                try {
//                    userId = Integer.parseInt((String) idAttr);
//                } catch (NumberFormatException ignored) {
//                }
//            }
//        }
//
//        if (userId == null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            try (PrintWriter out = response.getWriter()) {
//                out.print("{\"success\":false,\"message\":\"Unauthorized\"}");
//            }
//            return;
//        }
//
//        String action = request.getParameter("action");
//        if (action == null)
//            action = "";
//
//        try (PrintWriter out = response.getWriter()) {
//            switch (action) {
//                case "add": {
//                    // Accept either model-style params (name, fullAddress, status) or legacy form
//                    // parts
//                    String name = request.getParameter("name");
//                    String phone = request.getParameter("phoneNumber");
//                    String fullAddress = request.getParameter("fullAddress");
//                    String statusParam = request.getParameter("status");
//
//                    if ((name == null || name.isEmpty())) {
//                        // fallback to legacy field
//                        name = request.getParameter("fullName");
//                    }
//
//                    if (fullAddress == null || fullAddress.isEmpty()) {
//                        // fallback to legacy composition
//                        String specific = request.getParameter("specificAddress");
//                        String location = request.getParameter("location");
//                        fullAddress = (specific == null ? "" : specific.trim());
//                        if (location != null && !location.isEmpty()) {
//                            if (!fullAddress.isEmpty())
//                                fullAddress += ", ";
//                            fullAddress += location.trim();
//                        }
//                    }
//
//                    // Determine status (1 = default)
//                    int status = 0;
//                    if ("1".equals(statusParam) || "on".equalsIgnoreCase(statusParam)
//                            || "true".equalsIgnoreCase(statusParam)) {
//                        status = 1;
//                    } else {
//                        String setDefault = request.getParameter("setDefault");
//                        if ("1".equals(setDefault) || "on".equalsIgnoreCase(setDefault))
//                            status = 1;
//                    }
//
//                    Address addr = new Address();
//                    addr.setUserId(userId);
//                    addr.setName(name);
//                    addr.setPhoneNumber(phone);
//                    addr.setFullAddress(fullAddress);
//                    addr.setStatus(status);
//
//                    int newId = addressService.addAddress(addr);
//                    if (newId > 0) {
//                        out.print("{\"success\":true,\"id\":" + newId + "}");
//                    } else {
//                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                        out.print("{\"success\":false,\"message\":\"Failed to insert\"}");
//                    }
//                    break;
//                }
//                case "update": {
//                    String idStr = request.getParameter("id");
//                    if (idStr == null) {
//                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                        out.print("{\"success\":false}");
//                        break;
//                    }
//                    int id = Integer.parseInt(idStr);
//
//                    // Accept model-style params
//                    String name = request.getParameter("name");
//                    String phone = request.getParameter("phoneNumber");
//                    String fullAddress = request.getParameter("fullAddress");
//                    String statusParam = request.getParameter("status");
//
//                    if ((name == null || name.isEmpty())) {
//                        name = request.getParameter("fullName");
//                    }
//
//                    if (fullAddress == null || fullAddress.isEmpty()) {
//                        String specific = request.getParameter("specificAddress");
//                        String location = request.getParameter("location");
//                        fullAddress = (specific == null ? "" : specific.trim());
//                        if (location != null && !location.isEmpty()) {
//                            if (!fullAddress.isEmpty())
//                                fullAddress += ", ";
//                            fullAddress += location.trim();
//                        }
//                    }
//
//                    int status = 0;
//                    if ("1".equals(statusParam) || "on".equalsIgnoreCase(statusParam)
//                            || "true".equalsIgnoreCase(statusParam)) {
//                        status = 1;
//                    } else {
//                        String setDefault = request.getParameter("setDefault");
//                        if ("1".equals(setDefault) || "on".equalsIgnoreCase(setDefault))
//                            status = 1;
//                    }
//
//                    Address addr = new Address();
//                    addr.setId(id);
//                    addr.setUserId(userId);
//                    addr.setName(name);
//                    addr.setPhoneNumber(phone);
//                    addr.setFullAddress(fullAddress);
//                    addr.setStatus(status);
//
//                    boolean ok = addressService.updateAddress(addr);
//                    out.print("{\"success\":" + ok + "}");
//                    break;
//                }
//                case "delete": {
//                    String idStr = request.getParameter("id");
//                    if (idStr == null) {
//                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                        out.print("{\"success\":false}");
//                        break;
//                    }
//                    int id = Integer.parseInt(idStr);
//                    boolean ok = addressService.deleteAddress(id, userId);
//                    out.print("{\"success\":" + ok + "}");
//                    break;
//                }
//                case "set-default": {
//                    String idStr = request.getParameter("id");
//                    if (idStr == null) {
//                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                        out.print("{\"success\":false}");
//                        break;
//                    }
//                    int id = Integer.parseInt(idStr);
//                    boolean ok = addressService.setDefaultAddress(id, userId);
//                    out.print("{\"success\":" + ok + "}");
//                    break;
//                }
//                default: {
//                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    out.print("{\"success\":false,\"message\":\"Unknown action\"}");
//                }
//            }
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            try (PrintWriter out = response.getWriter()) {
//                out.print("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
//            }
//        }
//    }
}