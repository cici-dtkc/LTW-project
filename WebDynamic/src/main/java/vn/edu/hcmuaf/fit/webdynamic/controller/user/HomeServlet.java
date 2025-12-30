package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import vn.edu.hcmuaf.fit.webdynamic.service.HomeService;
import vn.edu.hcmuaf.fit.webdynamic.service.HomeServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private final HomeService homeService = new HomeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Load featured products
        List<Map<String, Object>> featuredProducts = homeService.getFeaturedProducts();
        request.setAttribute("featuredProducts", featuredProducts);

        // Load featured accessories
        List<Map<String, Object>> featuredAccessories = homeService.getFeaturedAccessories();
        request.setAttribute("featuredAccessories", featuredAccessories);

        // Load active vouchers
        List<Map<String, Object>> activeVouchers = homeService.getActiveVouchers();
        request.setAttribute("activeVouchers", activeVouchers);

        // Forward to home.jsp
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}