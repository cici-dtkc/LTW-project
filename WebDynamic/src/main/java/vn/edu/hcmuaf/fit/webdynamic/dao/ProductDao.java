package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Handle;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import vn.edu.hcmuaf.fit.webdynamic.model.ProductVariant;
import vn.edu.hcmuaf.fit.webdynamic.model.TechSpecs;
import vn.edu.hcmuaf.fit.webdynamic.model.VariantColor;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<Map<String, Object>> findForAdmin(
            String keyword,
            Integer status,
            Integer categoryId,
            int offset,
            int limit
    );
    int countForAdmin(String keyword, Integer status, Integer categoryId);
    boolean toggleStatus(int productId);
    /*  ADD PRODUCT  */
    int insertProduct(Handle h, Product p);
    int insertVariant(Handle h, int productId, ProductVariant v);
    int insertVariantColor(Handle h, int variantId, VariantColor c);
    void insertTechSpec(Handle h, int productId, TechSpecs t);
    List<Map<String, Object>> findForAdminByVariantIds(List<Integer> variantIds);

    List<Integer> findVariantIdsForAdmin( String keyword, Integer status, Integer categoryId, int offset, int limit);
    // EDIT
    Map<String, Object> findProductByVariantColorId(int vcId);
    List<Map<String, Object>> findVariantsByProductId(int productId);
    List<Map<String, Object>> findTechByProductId(int productId);
    List<Map<String, Object>> findColorsByVariantId(int variantId);

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
