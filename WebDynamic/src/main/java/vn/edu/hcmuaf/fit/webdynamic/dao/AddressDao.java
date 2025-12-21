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
    public List<Address> findAllByUserId(int userId) {
        return jdbi.withHandle(h ->
                h.createQuery("""
            SELECT id,
                   user_id AS userId,
                   phone_number AS phoneNumber,
                   address,
                   name,
                   status
            FROM addresses
            WHERE user_id = :userId
            ORDER BY status DESC, id DESC
        """)
                        .bind("userId", userId)
                        .mapToBean(Address.class)
                        .list()
        );
    }

    public Optional<Address> findById(int id) {
        return jdbi.withHandle(h ->
                h.createQuery("""
            SELECT id,
                   user_id AS userId,
                   phone_number AS phoneNumber,
                   address,
                   name,
                   status
            FROM addresses
            WHERE id = :id
        """)
                        .bind("id", id)
                        .mapToBean(Address.class)
                        .findOne()
        );
    }

    public int insert(Address a) {
        return jdbi.inTransaction(h -> {
            if (a.getStatus() == 1) {
                h.createUpdate("UPDATE addresses SET status = 0 WHERE user_id = :uid")
                        .bind("uid", a.getUserId()).execute();
            }
            return h.createUpdate("""
            INSERT INTO addresses(user_id, name, phone_number, address, status)
            VALUES (:uid, :name, :phone, :addr, :status)
        """)
                    .bind("uid", a.getUserId())
                    .bind("name", a.getName())
                    .bind("phone", a.getPhoneNumber())
                    .bind("addr", a.getAddress())
                    .bind("status", a.getStatus())
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class).one();
        });
    }

    public boolean update(Address a) {
        return jdbi.inTransaction(h -> {
            if (a.getStatus() == 1) {
                h.createUpdate("UPDATE addresses SET status = 0 WHERE user_id = :uid AND id != :id")
                        .bind("uid", a.getUserId()).bind("id", a.getId()).execute();
            }
            return h.createUpdate("""
            UPDATE addresses
            SET name=:name, phone_number=:phone, address=:addr, status=:status
            WHERE id=:id
        """)
                    .bind("id", a.getId())
                    .bind("name", a.getName())
                    .bind("phone", a.getPhoneNumber())
                    .bind("addr", a.getAddress())
                    .bind("status", a.getStatus())
                    .execute() > 0;
        });
    }

    public boolean delete(int id) {
        return jdbi.withHandle(h ->
                h.createUpdate("DELETE FROM addresses WHERE id=:id")
                        .bind("id", id).execute() > 0
        );
    }

    public boolean setDefault(int id, int userId) {
        return jdbi.inTransaction(h -> {
            h.createUpdate("UPDATE addresses SET status = 0 WHERE user_id=:uid")
                    .bind("uid", userId).execute();
            return h.createUpdate("UPDATE addresses SET status=1 WHERE id=:id AND user_id=:uid")
                    .bind("id", id).bind("uid", userId).execute() > 0;
        });
    }

}
