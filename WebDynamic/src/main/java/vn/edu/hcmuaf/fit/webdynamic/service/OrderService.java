package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.OrderDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.AddressDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.PaymentTypesDao;
import vn.edu.hcmuaf.fit.webdynamic.model.Order;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;
import vn.edu.hcmuaf.fit.webdynamic.model.PaymentTypes;

import java.util.*;

public class OrderService {
    private final OrderDao orderDao;
    private final AddressDao addressDao;
    private final PaymentTypesDao paymentTypesDao;

    public OrderService() {
        this.orderDao = new OrderDao();
        this.addressDao = new AddressDao();
        this.paymentTypesDao = new PaymentTypesDao();
    }

    /**
     * Lấy tất cả đơn hàng của user
     */
    public List<Order> getUserOrders(int userId) {
        return orderDao.getOrdersByUserId(userId);
    }

    /**
     * Lấy đơn hàng theo trạng thái
     * Status:
     * 1 - Đang lên đơn (prepare)
     * 2 - Đang giao (shipping)
     * 3 - Đã giao (delivered)
     * 4 - Hoàn thành
     * 5 - Đã hủy (cancelled)
     */
    public List<Order> getUserOrdersByStatus(int userId, int status) {
        return orderDao.getOrdersByUserIdAndStatus(userId, status);
    }

    /**
     * Lấy chi tiết đơn hàng
     */
    public Optional<Order> getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    /**
     * Tạo đơn hàng mới
     */
    public int createOrder(Order order) {
        return orderDao.createOrder(order);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public boolean updateOrderStatus(int orderId, int status) {
        return orderDao.updateOrderStatus(orderId, status);
    }

    /**
     * Hủy đơn hàng (chỉ được hủy khi đang ở trạng thái "Đang lên đơn")
     */
    public boolean cancelOrder(int orderId, int userId) {
        return orderDao.cancelOrder(orderId, userId);
    }

    /**
     * Lấy thông tin địa chỉ giao hàng
     */
    public Address getOrderAddress(int addressId) {
        return addressDao.findById(addressId).orElse(null);
    }

    /**
     * Lấy thông tin phương thức thanh toán
     */
    public PaymentTypes getPaymentType(int paymentTypeId) {
        return null;
    }

    /**
     * Kiểm tra đơn hàng có thuộc về user không
     */
    public boolean isOrderBelongToUser(int orderId, int userId) {
        Optional<Order> order = orderDao.getOrderById(orderId);
        return order.isPresent() && order.get().getUserId() == userId;
    }

    public static String getStatusName(int status) {
        switch (status) {
            case 1:
                return "Đang lên đơn";
            case 2:
                return "Đang giao";
            case 3:
                return "Đã giao";
            case 4:
                return "Hoàn thành";
            case 5:
                return "Đã hủy";
            default:
                return "Không xác định";
        }
    }

    /**
     * Lấy CSS class cho trạng thái
     */
    public static String getStatusClass(int status) {
        switch (status) {
            case 1:
                return "prepare";
            case 2:
                return "shipping";
            case 3:
                return "delivered";
            case 5:
                return "cancelled";
            default:
                return "";
        }
    }

    /**
     * Lấy icon cho trạng thái
     */
    public static String getStatusIcon(int status) {
        switch (status) {
            case 1:
                return "fa-solid fa-clock";
            case 2:
                return "fa-solid fa-truck";
            case 3:
                return "fa-solid fa-box";
            case 5:
                return "fa-solid fa-xmark";
            default:
                return "fa-solid fa-question";
        }
    }

    public List<Map<String, Object>> getAllForAdmin() {
        return orderDao.findAll();
    }

    public List<Map<String, Object>> searchForAdmin(String keyword, Integer status) {
        return orderDao.searchOrders(keyword, status);
    }


    public Map<String, Object> updateStatus(int orderId, int newStatus) {
        Map<String, Object> result = new HashMap<>();

        Optional<Order> opt = orderDao.getOrderById(orderId);
        if (opt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Đơn hàng không tồn tại");
            return result;
        }

        int current = opt.get().getStatus();

        // Không cho cập nhật nếu đã giao hoặc đã hủy
        if (current == 3) {
            result.put("success", false);
            result.put("message", "Đơn hàng đã giao, không thể thay đổi trạng thái");
            return result;
        }

        if (current == 4) {
            result.put("success", false);
            result.put("message", "Đơn hàng đã bị hủy");
            return result;
        }

        boolean isValidTransition =
                (current == 1 && (newStatus == 2 || newStatus == 4)) ||
                        (current == 2 && (newStatus == 3 || newStatus == 4));

        if (!isValidTransition) {
            result.put("success", false);
            result.put("message",
                    "Không thể chuyển trạng thái từ "
                            + getStatusName(current) + " sang "
                            + getStatusName(newStatus));
            return result;
        }

        if (!orderDao.updateStatus(orderId, newStatus)) {
            result.put("success", false);
            result.put("message", "Lỗi hệ thống, vui lòng thử lại");
            return result;
        }

        result.put("success", true);
        result.put("message", "Cập nhật trạng thái đơn hàng thành công");
        return result;
    }


}