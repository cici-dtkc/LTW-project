package vn.edu.hcmuaf.fit.webdynamic.config;

import org.jdbi.v3.core.Jdbi;

import java.io.InputStream;
import java.util.Properties;

public class DBConnect {

    private static Jdbi jdbi;

    static {
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

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

            // Init Jdbi
            jdbi = Jdbi.create(url, username, password);
            System.out.println("✔ JDBI initialized!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Lỗi khởi tạo DBConnect", e);
        }
    }


    public static Jdbi getJdbi() {
        return jdbi;
    }
}
