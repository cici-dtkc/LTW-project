package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import java.util.List;

public interface ProductDao {

    List<Product> findAll(String keyword, String status, String category);
    void toggleStatus(int productId);

    void updateStatus(int productId, int status);
}
