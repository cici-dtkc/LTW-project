package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import java.util.List;
import java.util.Map;

public interface ProductDao {

    List<Product> findAllWithVariants();

    Product getProductById(int id);

    List<Product> search(String keyword, Integer status, Integer categoryId);

    void updateStatus(int productId, int status);

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

    List<Map<String, Object>> getProductsForList();
}
