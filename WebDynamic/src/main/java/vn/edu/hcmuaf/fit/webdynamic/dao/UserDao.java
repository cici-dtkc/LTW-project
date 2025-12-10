package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;

public class UserDao {

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

    /**
     * Update role and status for a user by id.
     * role: integer (e.g. 0=user,1=admin)
     * status: integer (1=active,0=locked)
     */
    public boolean updateRoleStatus(int id, int role, int status) {
        String sql = "UPDATE users SET role = :role, status = :status, updated_at = CURRENT_TIMESTAMP WHERE id = :id";
        return DBConnect.getJdbi().withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind("role", role)
                    .bind("status", status)
                    .bind("id", id)
                    .execute();
            return rows > 0;
        });
    }

    /**
     * Find user with role/status fields (for admin responses)
     */
    public User findByIdForAdmin(int id) {
        String sql = "SELECT id, username, first_name as firstName, last_name as lastName, email, avatar as avatar, role, status FROM users WHERE id = :id";
        return DBConnect.getJdbi().withHandle(handle -> handle.createQuery(sql)
                .bind("id", id)
                .mapToBean(User.class)
                .findOne()
                .orElse(null));
    }

}
