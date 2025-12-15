package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.OrderDetailDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.OrderDetailDao.OrderDetailWithProduct;
import vn.edu.hcmuaf.fit.webdynamic.model.OrderDetail;

import java.util.List;

public class OrderDetailService {
    private final OrderDetailDao orderDetailDao;

    public OrderDetailService() {
        this.orderDetailDao = new OrderDetailDao();
    }

    /**
     * Lấy danh sách chi tiết đơn hàng
     */
    public List<OrderDetail> getOrderDetails(int orderId) {
        return orderDetailDao.getOrderDetailsByOrderId(orderId);
    }

    /**
     * Lấy chi tiết đơn hàng với thông tin sản phẩm đầy đủ
     */
    public List<OrderDetailWithProduct> getOrderDetailsWithProduct(int orderId) {
        return orderDetailDao.getOrderDetailsWithProduct(orderId);
    }

    /**
     * Thêm chi tiết đơn hàng
     */
    public boolean addOrderDetail(OrderDetail orderDetail) {
        return orderDetailDao.addOrderDetail(orderDetail);
    }

    /**
     * Tính tổng tiền của đơn hàng
     */
    public double calculateOrderTotal(int orderId) {
        List<OrderDetail> details = orderDetailDao.getOrderDetailsByOrderId(orderId);
        return details.stream()
                .mapToDouble(OrderDetail::getTotalMoney)
                .sum();
    }
}