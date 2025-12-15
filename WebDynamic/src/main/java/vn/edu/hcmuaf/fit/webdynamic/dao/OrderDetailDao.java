package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.OrderDetail;

import java.util.List;

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
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, orderId)
                        .mapToBean(OrderDetail.class)
                        .list()
        );
    }

    /**
     * Lấy chi tiết đơn hàng với thông tin sản phẩm đầy đủ
     */
    public List<OrderDetailWithProduct> getOrderDetailsWithProduct(int orderId) {
        String sql = "SELECT " +
                "od.id, od.variant_id, od.order_id, od.price, od.quantity, " +
                "od.discount_amount, od.total_money, od.created_at, od.updated_at, " +
                "p.name as product_name, pv.name as variant_name, " +
                "c.name as color_name, c.color_code, " +
                "(SELECT img_path FROM images WHERE product_id = p.id LIMIT 1) as image_path " +
                "FROM order_details od " +
                "JOIN variant_colors vc ON od.variant_id = vc.id " +
                "JOIN product_variants pv ON vc.variant_id = pv.id " +
                "JOIN products p ON pv.product_id = p.id " +
                "JOIN colors c ON vc.color_id = c.id " +
                "WHERE od.order_id = ?";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, orderId)
                        .mapToBean(OrderDetailWithProduct.class)
                        .list()
        );
    }

    /**
     * Thêm chi tiết đơn hàng
     */
    public boolean addOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO order_details (variant_id, order_id, price, quantity, " +
                "discount_amount, total_money) VALUES (?, ?, ?, ?, ?, ?)";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, orderDetail.getVariantId())
                        .bind(1, orderDetail.getOrderId())
                        .bind(2, orderDetail.getPrice())
                        .bind(3, orderDetail.getQuantity())
                        .bind(4, orderDetail.getDiscountAmount())
                        .bind(5, orderDetail.getTotalMoney())
                        .execute()
        ) > 0;
    }

    /**
     * Lớp hỗ trợ chứa thông tin chi tiết đơn hàng + sản phẩm
     */
    public static class OrderDetailWithProduct extends OrderDetail {
        private String productName;
        private String variantName;
        private String colorName;
        private String colorCode;
        private String imagePath;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getVariantName() {
            return variantName;
        }

        public void setVariantName(String variantName) {
            this.variantName = variantName;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getColorCode() {
            return colorCode;
        }

        public void setColorCode(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    }
}