package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;

public class PaymentFormDao {
    private final Jdbi jdbi = DBConnect.getJdbi();


    public List<Address> findByUserId(int userId) {
        return jdbi.withHandle(h ->
                h.createQuery("""
                SELECT id, name, phone_number, address, status
                FROM addresses
                WHERE user_id = :userId AND status <> 0
                ORDER BY status DESC
            """)
                        .bind("userId", userId)
                        .mapToBean(Address.class)
                        .list()
        );
    }
}
