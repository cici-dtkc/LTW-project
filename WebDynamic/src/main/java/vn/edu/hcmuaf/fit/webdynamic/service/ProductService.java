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
     Map<String, Object> getProductForEditByVariantColorId(int vcId);
    List<Product> getAllForAdmin();

    List<Map<String, Object>> getProductsForList();

    List<Map<String, Object>> getProductsByCategory(int categoryId);

    List<Map<String, Object>> getProductsByCategoryWithFilters(
            int categoryId,
            Double priceMin,
            Double priceMax,
            List<String> memory,
            List<String> colors,
            Integer year,
            Integer brandId,
            List<String> types,
            String condition,
            String sortBy
    );

    List<Map<String, Object>> getAccessories();

    List<Map<String, Object>> getAccessoriesWithFilters(
            Double priceMin,
            Double priceMax,
            Integer brandId,
            List<String> types,
            String condition,
            String sortBy
    );
}
