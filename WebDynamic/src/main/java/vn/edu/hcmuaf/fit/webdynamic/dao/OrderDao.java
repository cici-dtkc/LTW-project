package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.Order;

import java.util.List;
import java.util.Optional;

public class OrderDao {
    private final Jdbi jdbi;

    public OrderDao() {
        this.jdbi = DBConnect.getJdbi();
    }

    /**
     * Lấy tất cả đơn hàng của user
     */
    public List<Order> getOrdersByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, userId)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    /**
     * Lấy đơn hàng theo trạng thái
     */
    public List<Order> getOrdersByUserIdAndStatus(int userId, int status) {
        String sql = "SELECT * FROM orders WHERE user_id = ? AND status = ? ORDER BY created_at DESC";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, userId)
                        .bind(1, status)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    /**
     * Lấy chi tiết một đơn hàng
     */
    public Optional<Order> getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, orderId)
                        .mapToBean(Order.class)
                        .findFirst()
        );
    }

    /**
     * Tạo đơn hàng mới
     */
    public int createOrder(Order order) {
        String sql = "INSERT INTO orders (status, voucher_id, payment_type_id, fee_shipping, " +
                "total_amount, discount_amount, user_id, address_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, order.getStatus())
                        .bind(1, order.getVoucherId())
                        .bind(2, order.getPaymentTypeId())
                        .bind(3, order.getFeeShipping())
                        .bind(4, order.getTotalAmount())
                        .bind(5, order.getDiscountAmount())
                        .bind(6, order.getUserId())
                        .bind(7, order.getAddressId())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public boolean updateOrderStatus(int orderId, int status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, status)
                        .bind(1, orderId)
                        .execute()
        ) > 0;
    }

    /**
     * Hủy đơn hàng
     */
    public boolean cancelOrder(int orderId, int userId) {
        String sql = "UPDATE orders SET status = 5 WHERE id = ? AND user_id = ? AND status = 1";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, orderId)
                        .bind(1, userId)
                        .execute()
        ) > 0;
    }

    // Lấy tất cả đơn hàng
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    // Tìm kiếm + lọc trạng thái
    public List<Order> search(String keyword, Integer status) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.* FROM orders o " +
                        "JOIN users u ON o.user_id = u.id WHERE 1=1 "
        );

        if (keyword != null && !keyword.isEmpty()) {
            sql.append("AND (o.id LIKE :kw OR u.fullname LIKE :kw) ");
        }
        if (status != null) {
            sql.append("AND o.status = :status ");
        }

        return jdbi.withHandle(handle -> {
            var query = handle.createQuery(sql.toString());

            if (keyword != null && !keyword.isEmpty()) {
                query.bind("kw", "%" + keyword + "%");
            }
            if (status != null) {
                query.bind("status", status);
            }

            return query.mapToBean(Order.class).list();
        });
    }

    // Cập nhật trạng thái đơn hàng
    public boolean updateStatus(int orderId, int status) {
        String sql = """
                UPDATE orders
                SET status = :status,
                    updated_at = NOW()
                WHERE id = :id
                """;

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("id", orderId)
                        .execute()
        ) > 0;
    }
}