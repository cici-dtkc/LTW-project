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

@WebServlet("/listproduct_accessory")
public class AccessoryListServlet extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    private static final int DEFAULT_PAGE_SIZE = 12;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Category ID 1 = Điện thoại, tất cả ID khác = Linh kiện

        // Lấy các tham số lọc
        Double priceMin = getDoubleParameter(request, "priceMin");
        Double priceMax = getDoubleParameter(request, "priceMax");
        List<String> types = getListParameter(request, "type");
        Integer brandId = getIntegerParameter(request, "brandId");
        String condition = request.getParameter("condition");
        List<String> models = getListParameter(request, "model");
        String sortBy = request.getParameter("sort");

        // Lấy tham số phân trang
        int page = getIntegerParameter(request, "page") != null ? getIntegerParameter(request, "page") : 1;
        int pageSize = DEFAULT_PAGE_SIZE;
        if (page < 1) page = 1;

        // Lấy danh sách linh kiện với bộ lọc (tất cả category_id != 1)
        List<Map<String, Object>> allAccessories;
        if (hasFilters(priceMin, priceMax, types, brandId, condition, models, sortBy)) {
            allAccessories = productService.getAccessoriesWithFilters(
                    priceMin, priceMax, brandId, types, condition, sortBy
            );
            
            // Lọc theo model sau khi lấy dữ liệu (nếu có)
            if (models != null && !models.isEmpty()) {
                allAccessories = allAccessories.stream()
                    .filter(product -> {
                        String productName = (String) product.get("name");
                        if (productName == null) return false;
                        return models.stream().anyMatch(model -> 
                            productName.toLowerCase().contains(model.toLowerCase())
                        );
                    })
                    .collect(Collectors.toList());
            }
        } else {
            allAccessories = productService.getAccessories();
        }

        // Tính toán phân trang đơn giản
        int totalItems = allAccessories.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (totalPages < 1) totalPages = 1;
        if (page > totalPages) page = totalPages;

        // Lấy danh sách cho trang hiện tại
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<Map<String, Object>> accessories = new ArrayList<>();
        if (startIndex < totalItems) {
            accessories = allAccessories.subList(startIndex, endIndex);
        }

        // Truyền dữ liệu vào JSP
        request.setAttribute("accessories", accessories);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/listproduct_accessory.jsp").forward(request, response);
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

    private boolean hasFilters(Double priceMin, Double priceMax, List<String> types,
                               Integer brandId, String condition, List<String> models, String sortBy) {
        return priceMin != null || priceMax != null || (types != null && !types.isEmpty()) ||
               brandId != null || (condition != null && !condition.trim().isEmpty()) ||
               (models != null && !models.isEmpty()) ||
               (sortBy != null && !sortBy.trim().isEmpty());
    }
}