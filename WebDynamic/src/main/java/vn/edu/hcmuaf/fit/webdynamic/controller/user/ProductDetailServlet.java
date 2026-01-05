package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.webdynamic.dao.*;
import vn.edu.hcmuaf.fit.webdynamic.model.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", value = "/product-detail")
public class ProductDetailServlet extends HttpServlet {

    private ProductDao productDao;
    private VariantDao variantDao;
    private FeedbackDao feedbackDao;

    @Override
    public void init() {
        productDao = new ProductDaoImpl();
        variantDao = new VariantDao();
        feedbackDao = new FeedbackDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy productId
        int productId = Integer.parseInt(request.getParameter("id"));
        // test tạm
        // int productId = 3;

        // Product chính
        Product product = productDao.findProductDetailById(productId);

        // Variants
        List<ProductVariant> variants = productDao.getVariantsByProduct(productId);

        // Tech specs
        List<TechSpecs> techSpecs = productDao.getTechSpecsByProduct(productId);

        //  Default VariantColor
        VariantColor defaultVC = productDao.getDefaultVariantColor(productId);

        // Colors + Images
        List<VariantColor> colors = null;
        List<Image> images = null;

        if (defaultVC != null) {
            colors = variantDao.getColorsByVariant(defaultVC.getId());
            images = variantDao.getImagesByVariantColor(defaultVC.getId());
        }

        // 7️⃣ Feedback
        List<Feedback> feedbacks = feedbackDao.getFeedbacksByProductId(productId);
        int totalFeedbacks = feedbackDao.countByProductId(productId);

        // 8️⃣ Set attribute
        request.setAttribute("product", product);
        request.setAttribute("variants", variants);
        request.setAttribute("colors", colors);
        request.setAttribute("images", images);
        request.setAttribute("techSpecs", techSpecs);
        request.setAttribute("defaultVariantColor", defaultVC);

        request.setAttribute("feedbacks", feedbacks);
        request.setAttribute("totalFeedbacks", totalFeedbacks);

        // 9️⃣ Forward
        request.getRequestDispatcher("/views/user/product-detail.jsp")
                .forward(request, response);
    }
}
