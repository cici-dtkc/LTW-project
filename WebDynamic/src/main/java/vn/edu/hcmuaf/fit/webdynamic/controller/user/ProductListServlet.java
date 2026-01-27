package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductService;
import vn.edu.hcmuaf.fit.webdynamic.service.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

@WebServlet("/listproduct")
public class ProductListServlet extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    private static final int DEFAULT_PAGE_SIZE = 12;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Assuming category 1 is for phones
        int categoryId = 1;

        // Lấy các tham số lọc
        Double priceMin = getDoubleParameter(request, "priceMin");
        Double priceMax = getDoubleParameter(request, "priceMax");
        List<String> memory = getListParameter(request, "memory");
        List<String> colors = getListParameter(request, "color");
        Integer year = getIntegerParameter(request, "year");
        String brandName = request.getParameter("brandName");
        String sortBy = request.getParameter("sort");
        String search = request.getParameter("search");

        // Lấy tham số phân trang
        int page = getIntegerParameter(request, "page") != null ? getIntegerParameter(request, "page") : 1;
        int pageSize = DEFAULT_PAGE_SIZE;
        if (page < 1)
            page = 1;

        // 🔧 TỐI ƯU HÓA: Lấy dữ liệu với pagination ở tầng database
        List<Map<String, Object>> products;
        int totalItems;

        // Kiểm tra có filter hay search không
        boolean hasFilter = hasFilters(priceMin, priceMax, memory, colors, year, brandName, sortBy)
                || (search != null && !search.trim().isEmpty());

        if (hasFilter) {
            // Có filter: Sử dụng getProductsByCategoryPaginated để load chỉ 1 page
            products = productService.getProductsByCategoryPaginated(
                    categoryId,
                    priceMin, priceMax,
                    memory, colors,
                    year, brandName,
                    sortBy,
                    page,
                    pageSize,
                    search); // Thêm search parameter

            // Đếm tổng số products thỏa filter
            totalItems = productService.countProductsByCategory(
                    categoryId,
                    priceMin, priceMax,
                    memory, colors,
                    year, brandName,
                    search); // Thêm search parameter
        } else {
            // Không filter: Load category mặc định, vẫn dùng pagination
            products = productService.getProductsByCategoryPaginated(
                    categoryId,
                    null, null,
                    null, null,
                    null, null,
                    null,
                    page,
                    pageSize,
                    null);

            // Đếm tổng số products
            totalItems = productService.countProductsByCategory(
                    categoryId,
                    null, null,
                    null, null,
                    null, null,
                    null);
        }

        // Tính toán phân trang
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (totalPages < 1)
            totalPages = 1;
        if (page > totalPages)
            page = totalPages;

        // Truyền dữ liệu vào JSP
        request.setAttribute("products", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);
        request.getRequestDispatcher("/listproduct.jsp").forward(request, response);
    }

    private Double getDoubleParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (value != null && !value.trim().isEmpty()) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Integer getIntegerParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (value != null && !value.trim().isEmpty()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private List<String> getListParameter(HttpServletRequest request, String paramName) {
        String[] values = request.getParameterValues(paramName);
        if (values != null && values.length > 0) {
            return new ArrayList<>(Arrays.asList(values));
        }
        return null;
    }

    private boolean hasFilters(Double priceMin, Double priceMax, List<String> memory,
            List<String> colors, Integer year, String brandName, String sortBy) {
        return priceMin != null || priceMax != null || (memory != null && !memory.isEmpty()) ||
                (colors != null && !colors.isEmpty()) || year != null
                || (brandName != null && !brandName.trim().isEmpty()) ||
                (sortBy != null && !sortBy.trim().isEmpty());
    }
}