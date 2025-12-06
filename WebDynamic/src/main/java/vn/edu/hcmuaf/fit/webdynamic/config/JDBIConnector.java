package vn.edu.hcmuaf.fit.webdynamic.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;

public class JDBIConnector {
    private static HikariDataSource ds;
    private static Jdbi jdbi;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/phone_store?useSSL=false&characterEncoding=utf8");
        config.setUsername("root");
        config.setPassword("");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        ds = new HikariDataSource(config);
        jdbi = Jdbi.create(ds);
    }

    public static Jdbi get() {
        return jdbi;
    }
}
