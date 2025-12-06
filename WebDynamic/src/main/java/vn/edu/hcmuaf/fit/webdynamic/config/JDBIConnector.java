package vn.edu.hcmuaf.fit.webdynamic.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;

public class JDBIConnector {
    private static HikariDataSource ds;
    private static Jdbi jdbi;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/localhost?useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("123456");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(120000);
        config.setMaxLifetime(1800000);

        ds = new HikariDataSource(config);
        jdbi = Jdbi.create(ds);
    }

    public static Jdbi get() {
        return jdbi;
    }


}
