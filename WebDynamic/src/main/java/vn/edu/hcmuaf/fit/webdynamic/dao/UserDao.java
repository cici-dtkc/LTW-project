package vn.edu.hcmuaf.fit.webdynamic.dao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    private static UserDao instance;

    public static UserDao getInstance() {
        if (instance == null) instance = new UserDao();
        return instance;
    }

    //Kiểm tra đã có username tồn tại chưa
    public boolean checkExistUsername(String username) {
        Integer count = DBConnect.getJdbi()
                .withHandle(h -> h.createQuery("SELECT COUNT(*) FROM users WHERE username = :u")
                        .bind("u", username)
                        .mapTo(Integer.class)
                        .one());
        return count > 0;
    }

    //Kiểm tra đã có email tồn tại chưa
    public boolean checkExistEmail(String email) {
        Integer count = DBConnect.getJdbi()
                .withHandle(h -> h.createQuery("SELECT COUNT(*) FROM users WHERE email = :e")
                        .bind("e", email)
                        .mapTo(Integer.class)
                        .one());
        return count > 0;
    }

    public boolean register(User u) {
        int result = DBConnect.getJdbi().withHandle(h ->
                h.createUpdate("INSERT INTO users (first_name, last_name, username, password, email) " +
                                "VALUES (:fn, :ln, :un, :pw, :em)")
                        .bind("fn", u.getFirstName())
                        .bind("ln", u.getLastName())
                        .bind("un", u.getUsername())
                        .bind("pw", u.getPassword())
                        .bind("em", u.getEmail())
                        .execute()
        );
        return result > 0;
    }
    public User login(String input, String password) {
        String sql = """
        SELECT id, username, first_name, last_name, avatar, email,
               role, status, provider, provider_id, created_at, updated_at
        FROM users
        WHERE (email = ? OR username = ?)
          AND password = ?
          AND status = 1
        LIMIT 1
        """;

            return DBConnect.getJdbi().withHandle(handle ->
                    handle.createQuery(sql)
                            .bind(0, input)
                            .bind(1, input)
                            .bind(2, password)
                            .mapToBean(User.class)
                            .findOne()
                            .orElse(null)
            );
        }

        // Kiểm tra mật khẩu cũ
    public boolean checkOldPassword(int userId, String oldPassword) {
        String sql = """
        SELECT COUNT(*) 
        FROM users 
        WHERE id = ? AND password = ?
    """;

        return DBConnect.getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, userId)
                        .bind(1, oldPassword)
                        .mapTo(Integer.class)
                        .one() == 1
        );
    }

    // Cập nhật mật khẩu mới
    public boolean updatePassword(int userId, String newPassword) {
        String sql = """
        UPDATE users 
        SET password = ?, updated_at = NOW()
        WHERE id = ?
    """;

        return DBConnect.getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, newPassword)
                        .bind(1, userId)
                        .execute() > 0
        );
    }

    }
