package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.OrderDetail;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailDao {
    private final Jdbi jdbi;

    public OrderDetailDao() {
        this.jdbi = DBConnect.getJdbi();
    }

    /**
     * Lấy tất cả chi tiết đơn hàng theo orderId
     */
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind(0, orderId)
                .mapToBean(OrderDetail.class)
                .list());
    }

    /**
     * Lấy chi tiết đơn hàng với thông tin sản phẩm đầy đủ
     */
    public List<OrderDetail> getOrderDetailsWithProduct(int orderId) {
        return getOrderDetailsByOrderId(orderId);
    }

    /**
     * Lấy thông tin sản phẩm dựa trên variantId
     */
    public Map<String, String> getProductInfoByVariantId(int variantId) {
        String sql = "SELECT p.name as product_name, pv.name as variant_name, " +
                "c.name as color_name, c.color_code, " +
                "(SELECT img_path FROM images WHERE product_id = p.id LIMIT 1) as image_path " +
                "FROM variant_colors vc " +
                "JOIN product_variants pv ON vc.variant_id = pv.id " +
                "JOIN products p ON pv.product_id = p.id " +
                "JOIN colors c ON vc.color_id = c.id " +
                "WHERE vc.id = ?";

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind(0, variantId)
                .map((rs, ctx) -> {
                    Map<String, String> info = new HashMap<>();
                    info.put("productName", rs.getString("product_name"));
                    info.put("variantName", rs.getString("variant_name"));
                    info.put("colorName", rs.getString("color_name"));
                    info.put("colorCode", rs.getString("color_code"));
                    info.put("imagePath", rs.getString("image_path"));
                    return info;
                })
                .findFirst()
                .orElse(new HashMap<>()));
    }

    /**
     * Thêm chi tiết đơn hàng
     */
    public boolean addOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO order_details (variant_id, order_id, price, quantity, " +
                "discount_amount, total_money) VALUES (?, ?, ?, ?, ?, ?)";

        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind(0, orderDetail.getVariantId())
                .bind(1, orderDetail.getOrderId())
                .bind(2, orderDetail.getPrice())
                .bind(3, orderDetail.getQuantity())
                .bind(4, orderDetail.getDiscountAmount())
                .bind(5, orderDetail.getTotalMoney())
                .execute()) > 0;
    }
}