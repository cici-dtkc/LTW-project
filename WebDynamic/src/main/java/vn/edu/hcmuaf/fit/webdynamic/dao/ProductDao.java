package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import java.util.List;

public interface ProductDao {

    List<Product> findAll();

    Product getProductById(int id);

    List<Product> search(String keyword, Integer status, Integer categoryId);

    void updateStatus(int productId, int status);
}
