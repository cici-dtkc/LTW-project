package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.Feedback;

import java.util.List;

public class FeedbackDao {
    /**
     * Lấy danh sách feedback theo productId (chỉ lấy feedback đang hiển thị)
     */
    public List<Feedback> getFeedbacksByProductId(int productId) {

        String sql = """
            SELECT 
                f.id,
                f.rating,
                f.comment,
                f.status,
                f.created_at,
                u.id AS user_id,
                u.username,
                u.avatar
            FROM feedbacks f
            JOIN users u ON f.user_id = u.id
            WHERE f.product_id = :productId
              AND f.status = 1
            ORDER BY f.created_at DESC
        """;

        return DBConnect.getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .mapToBean(Feedback.class)
                        .list()
        );
    }
}
