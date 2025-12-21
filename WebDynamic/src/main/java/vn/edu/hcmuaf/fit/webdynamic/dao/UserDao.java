package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private static UserDao instance;

    public static UserDao getInstance() {
        if (instance == null)
            instance = new UserDao();
        return instance;
    }

    // Kiểm tra đã có username tồn tại chưa
    public boolean checkExistUsername(String username) {
        Integer count = DBConnect.getJdbi()
                .withHandle(h -> h.createQuery("SELECT COUNT(*) FROM users WHERE username = :u")
                        .bind("u", username)
                        .mapTo(Integer.class)
                        .one());
        return count > 0;
    }

    // Kiểm tra đã có email tồn tại chưa
    public boolean checkExistEmail(String email) {
        Integer count = DBConnect.getJdbi()
                .withHandle(h -> h.createQuery("SELECT COUNT(*) FROM users WHERE email = :e")
                        .bind("e", email)
                        .mapTo(Integer.class)
                        .one());
        return count > 0;
    }

    // Kiểm tra email đã tồn tại cho user khác chưa
    public boolean checkExistEmailForOtherUsers(int id, String email) {
        Integer count = DBConnect.getJdbi()
                .withHandle(h -> h.createQuery("SELECT COUNT(*) FROM users WHERE email = :e AND id != :id")
                        .bind("e", email)
                        .bind("id", id)
                        .mapTo(Integer.class)
                        .one());
        return count > 0;
    }

    public boolean register(User u) {
        int result = DBConnect.getJdbi()
                .withHandle(h -> h
                        .createUpdate("INSERT INTO users (first_name, last_name, username, password, email) " +
                                "VALUES (:fn, :ln, :un, :pw, :em)")
                        .bind("fn", u.getFirstName())
                        .bind("ln", u.getLastName())
                        .bind("un", u.getUsername())
                        .bind("pw", u.getPassword())
                        .bind("em", u.getEmail())
                        .execute());
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

        return DBConnect.getJdbi().withHandle(handle -> handle.createQuery(sql)
                .bind(0, input)
                .bind(1, input)
                .bind(2, password)
                .mapToBean(User.class)
                .findOne()
                .orElse(null));
    }

    // lây thông tin qua tìm id người dùng
    public Optional<User> findById(int id) {
        return DBConnect.getJdbi().withHandle(handle -> handle
                .createQuery("SELECT id, username, first_name, last_name, email, avatar FROM users WHERE id = :id")
                .bind("id", id)
                .mapToBean(User.class)
                .findOne());
    }

    // lấy thông tin qua tìm tên đăng nhập
    public Optional<User> findByUsername(String username) {
        return DBConnect.getJdbi().withHandle(handle -> handle.createQuery(
                "SELECT id, username, first_name, last_name, email, avatar FROM users WHERE username = :username")
                .bind("username", username)
                .mapToBean(User.class)
                .findOne());
    }

    // Lấy danh sachs người dùng load từ database
    public List<User> getAllUsers() {
        String sql = """
                    SELECT id, username, first_name, last_name, avatar, email, role, status
                    FROM users
                """;

        return DBConnect.getJdbi().withHandle(handle -> handle.createQuery(sql)
                .mapToBean(User.class)
                .list());
    }

    // Cập nhật người dùng bên admin
    public boolean updateUser(int id, int role, int status) {
        String sql = """
                    UPDATE users
                    SET role = :role, status = :status
                    WHERE id = :id
                """;

        int rows = DBConnect.getJdbi().withHandle(handle -> handle.createUpdate(sql)
                .bind("role", role)
                .bind("status", status)
                .bind("id", id)
                .execute());

        return rows > 0;
    }

    // chỉnh sửa thông tin bên người dùng
    public boolean updateUserInfo(int id, String firstName, String lastName, String email) {
        String sql = """
                    UPDATE users SET first_name = :fn, last_name = :ln, email = :em
                    WHERE id = :id
                """;

        int rows = DBConnect.getJdbi().withHandle(h -> h.createUpdate(sql)
                .bind("fn", firstName)
                .bind("ln", lastName)
                .bind("em", email)
                .bind("id", id)
                .execute());
        return rows > 0;
    }

    // Cập nhật ảnh đại diện
    public boolean updateAvatar(int id, String avatarUrl) {
        String sql = "UPDATE users SET avatar = :av WHERE id = :id";

        int rows = DBConnect.getJdbi().withHandle(h -> h.createUpdate(sql)
                .bind("av", avatarUrl)
                .bind("id", id)
                .execute());
        return rows > 0;
    }

}
