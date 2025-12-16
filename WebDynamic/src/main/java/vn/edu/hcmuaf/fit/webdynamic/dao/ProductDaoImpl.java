package vn.edu.hcmuaf.fit.webdynamic.dao;
import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private final Jdbi jdbi = DBConnect.getJdbi();


    @Override
    public List<Product> findAll(String keyword, String status, String category) {
        return List.of();
    }

    @Override
    public void toggleStatus(int productId) {

    }

    @Override
    public void updateStatus(int productId, int status) {

    }
}
