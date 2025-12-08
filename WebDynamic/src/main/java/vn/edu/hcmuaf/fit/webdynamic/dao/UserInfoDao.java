package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.User;

import java.util.Optional;

public class UserInfoDao {
    private final Jdbi jdbi;
    public UserInfoDao() {
        this.jdbi = DBConnect.getJdbi();
    }
    public Optional<User> findById(int id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, username, first_name, last_name, email, avatar_url FROM users WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(User.class)
                        .findOne()
        );
    }

    public Optional<User> findByUsername(String username) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, username, first_name, last_name, email, avatar_url FROM users WHERE username = :username")
                        .bind("username", username)
                        .mapToBean(User.class)
                        .findOne()
        );
    }

}
