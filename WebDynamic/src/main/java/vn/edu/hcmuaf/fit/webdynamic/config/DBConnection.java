package vn.edu.hcmuaf.fit.webdynamic.config;

import org.jdbi.v3.core.Jdbi;

public class DBConnection {

    // Thông tin kết nối MySQL – chỉnh lại cho đúng với cấu hình thực tế của bạn
    private static final String URL = "jdbc:mysql://localhost:3306/phone_store";
    private static final String USER = "root";
    private static final String PASSWORD = "LTWeb@12HK1";

    // Singleton Jdbi
    private static Jdbi jdbiInstance;

    private DBConnection() {
        // private constructor để tránh khởi tạo từ bên ngoài
    }

    /**
     * Lấy instance Jdbi dùng chung cho toàn bộ ứng dụng.
     */
    public static synchronized Jdbi getJdbi() {
        if (jdbiInstance == null) {
            jdbiInstance = Jdbi.create(URL, USER, PASSWORD);
        }
        return jdbiInstance;
    }
}
