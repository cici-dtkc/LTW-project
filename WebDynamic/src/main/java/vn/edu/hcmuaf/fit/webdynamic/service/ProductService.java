package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import java.util.List;

public interface ProductService {

    List<Product> getAll();

    List<Product> search(String keyword, String status, String category);

    void toggleStatus(int productId);
}
