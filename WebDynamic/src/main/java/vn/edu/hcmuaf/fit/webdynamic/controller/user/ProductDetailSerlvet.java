package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdynamic.dao.FeedbackDao;
import vn.edu.hcmuaf.fit.webdynamic.model.Feedback;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", value = "/product-detail")
public class ProductDetailSerlvet extends HttpServlet {

    @Override
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int productId = Integer.parseInt(request.getParameter("productId"));
        int productId = 3;
        FeedbackDao feedbackDao = new FeedbackDao();
        List<Feedback> feedbacks = feedbackDao.getFeedbacksByProductId(productId);
        int totalFeedbacks = feedbackDao.countByProductId(productId);
        request.setAttribute("feedbacks", feedbacks);
        request.setAttribute("totalFeedbacks", totalFeedbacks);
        request.getRequestDispatcher("product-detail.jsp").forward(request, response);

    }

}
