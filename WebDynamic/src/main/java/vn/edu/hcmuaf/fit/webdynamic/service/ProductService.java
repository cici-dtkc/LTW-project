package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Product> getForAdmin(
            String keyword,
            Integer status,
            Integer categoryId,
            int page,
            int limit
    );

    int countForAdmin(String keyword, Integer status, Integer categoryId);
      boolean toggleStatus(int productId) ;
      void addPhone(Product product);
}
