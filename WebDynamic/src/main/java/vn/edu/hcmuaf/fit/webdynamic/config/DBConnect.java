package vn.edu.hcmuaf.fit.webdynamic.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;

import java.io.InputStream;
import java.util.Properties;

public class DBConnect {

    private static Jdbi jdbi;
    private static HikariDataSource dataSource;

    static {
        try {
            // Load db.properties
            Properties props = new Properties();
            InputStream input = DBConnect.class.getClassLoader()
                    .getResourceAsStream("db.properties");

            if (input == null) {
                throw new RuntimeException("Không tìm thấy file db.properties");
            }

            props.load(input);

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");
            int poolSize = Integer.parseInt(props.getProperty("db.poolsize", "10"));

            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);

            // Connection pool settings
            config.setMaximumPoolSize(poolSize);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);      // 30 seconds
            config.setIdleTimeout(600000);           // 10 minutes
            config.setMaxLifetime(1800000);          // 30 minutes

            // Performance settings
            config.setAutoCommit(true);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("useLocalSessionState", "true");
            config.addDataSourceProperty("rewriteBatchedStatements", "true");
            config.addDataSourceProperty("cacheResultSetMetadata", "true");
            config.addDataSourceProperty("cacheServerConfiguration", "true");
            config.addDataSourceProperty("elideSetAutoCommits", "true");
            config.addDataSourceProperty("maintainTimeStats", "false");

            // Create DataSource
            dataSource = new HikariDataSource(config);

            // Init Jdbi with HikariCP
            jdbi = Jdbi.create(dataSource);

            System.out.println("✔ JDBI initialized with HikariCP!");
            System.out.println("✔ Connection Pool Size: " + poolSize);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Lỗi khởi tạo DBConnect", e);
        }
    }

    public static Jdbi getJdbi() {
        return jdbi;
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    // Shutdown hook để đóng connection pool khi app tắt
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("✔ HikariCP connection pool closed");
        }
    }
}
