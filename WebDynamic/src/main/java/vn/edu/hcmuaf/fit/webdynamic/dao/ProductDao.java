package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Handle;
import vn.edu.hcmuaf.fit.webdynamic.model.*;

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
    //edit
    Map<String, Object> findProductByVariantColorId(int vcId);
    List<Map<String, Object>> findVariantsByProductId(int productId);
    List<Map<String, Object>> findTechByProductId(int productId);
    List<Map<String, Object>> findColorsByVariantId(int variantId);

      Map<String, Object> findVariantColorDetailForEdit(int vcId);
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
    void updateProductBasic(Handle h, Product p);
    void deleteTechSpecsByProductId(Handle h, int productId);
     void updateVariant(Handle h, int variantId, String name, double basePrice);
    void updateVariantColor(Handle h, int vcId, int quantity, String sku, double price);

    // ===== PRODUCT DETAIL =====

    // Thông tin chính product (brand, category)
    Product findProductDetailById(int productId);

    // Variants của product
    List<ProductVariant> getVariantsByProduct(int productId);

    // Colors theo variant
    List <TechSpecs> getTechSpecsByProduct(int variantId);

     // VariantColor mặc định (load trang)
    VariantColor getDefaultVariantColor(int productId);


    // cart
     Map<String, Object> getCartItemDetail(int variantColorId);

     // Sản phẩm liên quan
     //int brandId,           : ID thương hiệu để lấy sản phẩm cùng hãng
     //int excludeProductId,  : ID sản phẩm hiện tại (loại trừ khỏi danh sách liên quan)
     //int limit              : Số lượng sản phẩm liên quan cần lấy (ví dụ: 4)
    List<Map<String, Object>> getRelatedProductsByBrand(int brandId, int excludeProductId, int limit);
}
