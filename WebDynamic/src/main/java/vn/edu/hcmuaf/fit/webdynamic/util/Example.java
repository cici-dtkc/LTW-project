package vn.edu.hcmuaf.fit.webdynamic.util;

import org.jdbi.v3.core.Jdbi;


public class Example {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/phone_store?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String username = "root";
        String password = "LTWeb@12HK1";

        Jdbi jdbi = Jdbi.create(url, username, password);

        jdbi.useHandle(handle -> {
            var tables = handle.createQuery("SHOW TABLES")
                    .mapTo(String.class)
                    .list();

            System.out.println("Danh sách bảng trong database:");
            tables.forEach(System.out::println);
        });
    }

}


