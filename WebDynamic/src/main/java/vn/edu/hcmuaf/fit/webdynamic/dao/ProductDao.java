package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import java.util.List;
import java.util.Map;

public interface ProductDao {

    List<Product> findAllWithVariants();

    Product getProductById(int id);

    List<Product> search(String keyword, Integer status, Integer categoryId);

    void updateStatus(int productId, int status);
}
