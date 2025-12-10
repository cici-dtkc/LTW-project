package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;

import java.util.List;
import java.util.Optional;

public class AddressDao {
    private final Jdbi jdbi;

    public AddressDao() {
        this.jdbi = DBConnect.getJdbi();
    }
    // tất cả địa chỉ của người dùng bang userId
    public List<Address> findAllByUserId(int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, phone_number, address, name,status, user_id FROM addresses WHERE user_id = :userId ORDER BY status DESC, id DESC")
                        .bind("userId", userId)
                        .mapToBean(Address.class).list()
        );
    }
    //Lấy địa chỉ theo id
    public Optional<Address> findById(int id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, phone_number, address, name,status, user_id FROM addresses WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Address.class).findOne()
        );
    }
    public Optional<Address> findDefaultByUserId(int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, phone_number, address, name,status, user_id FROM addresses WHERE user_id = :userId AND status = 1 LIMIT 1")
                        .bind("userId", userId)
                        .mapToBean(Address.class).findOne()
        );
    }

     //Thêm địa chỉ mới.
    public int insert(Address address) {
        return jdbi.withHandle(handle -> {
            // Nếu địa chỉ này được đặt làm mặc định, bỏ mặc định của các địa chỉ khác
            if (address.getStatus() == 1) {
                handle.createUpdate("UPDATE addresses SET status = 0 WHERE user_id = :userId")
                        .bind("userId", address.getUserId())
                        .execute();
            }
            return handle.createUpdate("INSERT INTO addresses (phone_number, address, name, status, user_id) VALUES (:phoneNumber, :address, :name, :status, :userId)")
                    .bind("phoneNumber", address.getPhoneNumber())
                    .bind("address", address.getFullAddress())
                    .bind("name", address.getName())
                    .bind("status", address.getStatus())
                    .bind("userId", address.getUserId())
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();
        });
    }

    /**
     * Cập nhật địa chỉ.
     */
    public boolean update(Address address) {
        return jdbi.withHandle(handle -> {
                // Nếu địa chỉ này được đặt làm mặc định, bỏ mặc định của các địa chỉ khác
                if (address.getStatus() == 1) {
                handle.createUpdate("UPDATE addresses SET status = 0 WHERE user_id = :userId AND id != :id")
                    .bind("userId", address.getUserId())
                    .bind("id", address.getId())
                    .execute();
                }

                int rowsAffected = handle.createUpdate("UPDATE addresses SET phone_number = :phoneNumber, address = :address, name = :name, status = :status WHERE id = :id")
                    .bind("id", address.getId())
                    .bind("phoneNumber", address.getPhoneNumber())
                    .bind("address", address.getFullAddress())
                    .bind("name", address.getName())
                    .bind("status", address.getStatus())
                    .execute();

                return rowsAffected > 0;
        });
    }

    /**
     * Xóa địa chỉ.
     */
    public boolean delete(int id) {
        return jdbi.withHandle(handle -> {
            int rowsAffected = handle.createUpdate("DELETE FROM addresses WHERE id = :id")
                    .bind("id", id)
                    .execute();
            return rowsAffected > 0;
        });
    }

    /**
     * Đặt địa chỉ làm mặc định.
     */
    public boolean setAsDefault(int id, int userId) {
        return jdbi.withHandle(handle -> {
            // Bỏ mặc định của tất cả địa chỉ khác của user
            handle.createUpdate("UPDATE addresses SET status = 0 WHERE user_id = :userId")
                .bind("userId", userId)
                .execute();

            // Đặt địa chỉ này làm mặc định
            int rowsAffected = handle.createUpdate("UPDATE addresses SET status = 1 WHERE id = :id AND user_id = :userId")
                .bind("id", id)
                .bind("userId", userId)
                .execute();

            return rowsAffected > 0;
        });
    }
}
