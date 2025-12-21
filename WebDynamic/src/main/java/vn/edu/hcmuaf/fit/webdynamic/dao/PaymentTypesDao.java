package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;
import vn.edu.hcmuaf.fit.webdynamic.model.PaymentTypes;

import java.util.List;

public class PaymentTypesDao {
    private final Jdbi jdbi = DBConnect.getJdbi();

    public List<PaymentTypes> findAll() {
        return jdbi.withHandle(h ->
                h.createQuery("SELECT id, name FROM payment_types")
                        .mapToBean(PaymentTypes.class)
                        .list()
        );
    }
//Thêm đơn hàng (orders)
//public int insertOrder(Order order) {
//    return jdbi.withHandle(h ->
//            h.createUpdate("""
//                INSERT INTO orders
//                (status, voucher_id, payment_type_id, fee_shipping,
//                 discount_amount, total_amount, created_at, user_id, address_id)
//                VALUES (:status, :voucherId, :paymentTypeId,
//                        :feeShipping, :discountAmount, :totalAmount,
//                        NOW(), :userId, :addressId)
//            """)
//                    .bindBean(order)
//                    .executeAndReturnGeneratedKeys("id")
//                    .mapTo(int.class)
//                    .one()
//    );
//}
}
