package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;
import vn.edu.hcmuaf.fit.webdynamic.service.AddressService;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AddressServlet", urlPatterns = {"/user/addresses"})
public class AddressServlet extends HttpServlet {
    private UserService userService;
    private AddressService addressService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
        this.addressService = new AddressService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy danh sách địa chỉ của user
        Integer userId = 1;
        List<Address> addresses = addressService.getAllAddressesByUserId(userId);
        request.setAttribute("addresses", addresses);

        request.getRequestDispatcher("/views/user/addresses.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}