package vn.edu.hcmuaf.fit.webdynamic.dao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    public User login(String input, String password) {
        User user = null;
        String sql = """
            SELECT id, username, first_name, last_name, avatar, email, 
                   role, status, provider, provider_id, created_at, updated_at
            FROM users
            WHERE (email = ? OR username = ?)
              AND password = ?
              AND status = 1
            LIMIT 1
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, input);   // email
            ps.setString(2, input);   // username
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setAvatar(rs.getString("avatar"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getInt("role"));
                user.setStatus(rs.getInt("status"));
                user.setProvider(rs.getString("provider"));
                user.setProviderId(rs.getString("provider_id"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

}