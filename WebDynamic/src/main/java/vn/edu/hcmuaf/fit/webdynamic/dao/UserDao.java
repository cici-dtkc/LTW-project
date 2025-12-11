package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;

public class UserDao {

    private static UserDao instance;

    public static UserDao getInstance() {
        if (instance == null) instance = new UserDao();
        return instance;
    }

    // Kiểm tra username tồn tại
    public boolean checkExistUsername(String username) {
        Integer count = DBConnect.getJdbi()
                .withHandle(h -> h.createQuery("SELECT COUNT(*) FROM users WHERE username = :u")
                        .bind("u", username)
                        .mapTo(Integer.class)
                        .one());
        return count > 0;
    }

    // Kiểm tra email tồn tại
    public boolean checkExistEmail(String email) {
        Integer count = DBConnect.getJdbi()
                .withHandle(h -> h.createQuery("SELECT COUNT(*) FROM users WHERE email = :e")
                        .bind("e", email)
                        .mapTo(Integer.class)
                        .one());
        return count > 0;
    }

    // Đăng ký (mật khẩu đã được mã hoá ở Service)
    public boolean register(User u) {
        int result = DBConnect.getJdbi().withHandle(h ->
                h.createUpdate("""
                        INSERT INTO users 
                        (first_name, last_name, username, password, email, role, status) 
                        VALUES (:fn, :ln, :un, :pw, :em, 0, 1)
                        """)
                        .bind("fn", u.getFirstName())
                        .bind("ln", u.getLastName())
                        .bind("un", u.getUsername())
                        .bind("pw", u.getPassword())   // Đã hashed ở Service
                        .bind("em", u.getEmail())
                        .execute()
        );
        return result > 0;
    }

    // Tìm user để login
    public User findByInput(String input) {
        String sql = """
                SELECT id, username, first_name, last_name, avatar, email,
                       role, status, provider, provider_id, password, created_at, updated_at
                FROM users
                WHERE (email = :input OR username = :input)
                  AND status = 1
                LIMIT 1
                """;

        return DBConnect.getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("input", input)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }

    // Cập nhật mật khẩu
    public boolean updatePassword(int userId, String newPassword) {
        String sql = """
                UPDATE users 
                SET password = :pw, updated_at = NOW()
                WHERE id = :id
                """;

        return DBConnect.getJdbi().withHandle(h ->
                h.createUpdate(sql)
                        .bind("pw", newPassword) // hashed password
                        .bind("id", userId)
                        .execute() > 0
        );
    }
}
