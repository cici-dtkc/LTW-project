package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.model.VariantColor;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Map<String, Object>> getForAdmin(String keyword, Integer status, Integer categoryId, int page, int limit);
    int countForAdmin(String keyword, Integer status, Integer categoryId);
      boolean toggleStatus(int productId) ;
      void addPhone(Product product);

}
