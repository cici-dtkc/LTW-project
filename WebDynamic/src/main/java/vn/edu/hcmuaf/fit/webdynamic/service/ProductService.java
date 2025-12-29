package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.model.VariantColor;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Map<String, Object>> getForAdmin(String keyword, Integer status, Integer categoryId, int page, int limit);
    int countForAdmin(String keyword, Integer status, Integer categoryId);
      boolean toggleStatus(int productId) ;
     Map<String, Object> getProductForEditByVariantColorId(int vcId);
    List<Map<String, Object>> getProductsForList();

    List<Map<String, Object>> getProductsByCategory(int categoryId);
    void addProduct(Product product,
                    String[] techNames, String[] techValues, String[] techPriorities,
                    String[] variantNames, String[] basePrices,
                    String[] quantities, String[] variantQuantities,
                    String[] skus, String[] colorVariantIndexes,
                    String[] colorIds, String[] customColors, String[] colorPrices) throws Exception;

}
