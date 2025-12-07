package vn.edu.hcmuaf.fit.webdynamic.dao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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



    }
