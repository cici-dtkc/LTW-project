package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.*;
import vn.edu.hcmuaf.fit.webdynamic.model.Order;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;
import vn.edu.hcmuaf.fit.webdynamic.model.PaymentTypes;
import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;

import java.math.BigDecimal;
import java.util.*;

public class OrderService {
    private final OrderDao orderDao;
    private final AddressDao addressDao;
    private final PaymentTypesDao paymentTypesDao;
    private final VoucherAdminDao voucherDao = new VoucherAdminDaoImpl();

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
     * 4 - Đã hủy (cancelled)
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
        return paymentTypesDao.findById(paymentTypeId);
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
            case 4:
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
            case 4:
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

        boolean isValidTransition = (current == 1 && (newStatus == 2 || newStatus == 4)) ||
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

    public int processOrder(int userId, int addressId, String paymentMethod, String voucherCode,
            Map<Integer, Integer> cart, int paymentStatus) {
        // 1. Tính tổng tiền hàng (Subtotal) sử dụng OrderDao
        double subtotal = 0;
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Map<String, Object> product = orderDao.getProductForCart(entry.getKey());
            if (product != null) {
                // Jdbi có thể trả về BigDecimal cho kiểu dữ liệu DECIMAL/DOUBLE trong MySQL
                Object priceObj = product.get("unit_price");
                double price = (priceObj instanceof BigDecimal) ? ((BigDecimal) priceObj).doubleValue()
                        : (double) priceObj;

                subtotal += price * entry.getValue();
            }
        }

        // 2. Xử lý Voucher (Giữ nguyên logic kiểm tra an toàn)
        double discount = 0;
        Integer appliedVoucherId = null;

        if (voucherCode != null && !voucherCode.isEmpty()) {
            VoucherAdmin voucher = voucherDao.getByCode(voucherCode);
            if (voucher != null && voucher.getStatus() == 1 && subtotal >= voucher.getMinOrderValue()) {
                appliedVoucherId = voucher.getId();

                // Kiểm tra type: '1' là %, '0' là tiền mặt
                if ("percentage".equals(voucher.getType()) || "1".equals(voucher.getType())) {
                    discount = subtotal * (voucher.getDiscountAmount() / 100);
                    if (voucher.getMaxReduce() > 0 && discount > voucher.getMaxReduce()) {
                        discount = voucher.getMaxReduce();
                    }
                } else {
                    discount = voucher.getDiscountAmount();
                }
            }
        }

        // 3. Đóng gói đối tượng Order
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setPaymentTypeId("bank".equals(paymentMethod) ? 2 : 1);
        order.setStatus(1);
        int paymentTypeId = "bank".equalsIgnoreCase(paymentMethod) ? 2 : 1;
        order.setPaymentTypeId(paymentTypeId);// Trạng thái Chờ xác nhận
        order.setFeeShipping(30000.0);
        order.setVoucherId(appliedVoucherId);
        order.setDiscountAmount(discount);
        order.setTotalAmount(subtotal + 30000.0 - discount);

        // 4. Thực hiện lưu toàn bộ thông tin qua OrderDao
        return orderDao.insertOrderWithDetails(order, cart);
    }

    public Address getDefaultAddress(int userId) {
        List<Address> addresses = orderDao.getDefaultAddressByUserId(userId);
        // Nếu list không trống, lấy phần tử đầu tiên (địa chỉ mặc định)
        if (!addresses.isEmpty()) {
            return addresses.get(0);
        }
        return null; // Trả về null để JSP hiển thị phần "Chưa có địa chỉ"
    }

    public List<VoucherAdmin> getActiveVouchers() {
        return voucherDao.getActiveVouchers();
    }

}