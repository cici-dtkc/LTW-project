package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Handle;
import vn.edu.hcmuaf.fit.webdynamic.model.*;

import java.util.List;
import java.util.Map;

public interface ProductDao {
        // ADMIN - PRODUCT MANAGEMENT
        List<Map<String, Object>> findForAdmin(
                        String keyword,
                        Integer status,
                        Integer categoryId,
                        int offset,
                        int limit);

        // Count products for admin with filters
        int countForAdmin(String keyword, Integer status, Integer categoryId);

        // Toggle product status
        boolean toggleStatus(int productId);

        /* ADD PRODUCT */
        // find brand by name
        Brand findBrandByName(Handle h, String name);

        // insert brand and return its ID
        int insertBrand(Handle h, Brand b);

        // insert product
        int insertProduct(Handle h, Product p);

        // insert variant and return its ID
        int insertVariant(Handle h, int productId, ProductVariant v);

        // insert variant color and return its ID
        int insertVariantColor(Handle h, int variantId, VariantColor c);

        // insert variant color image
        void insertVariantColorImage(Handle h, int variantColorId, Image img);

        // insert tech spec
        void insertTechSpec(Handle h, int productId, TechSpecs t);

        // tìm kiếm sản phẩm theo danh sách variant IDs
        List<Map<String, Object>> findForAdminByVariantIds(List<Integer> variantIds);

        // tìm kiếm variant IDs theo từ khóa, trạng thái, danh mục
        List<Integer> findVariantIdsForAdmin(String keyword, Integer status, Integer categoryId, int offset, int limit);

        // edit
        // tìm kiếm sản phẩm theo vcId
        Map<String, Object> findProductByVariantColorId(int vcId);

        // tìm kiếm variant IDs theo productId
        List<Map<String, Object>> findVariantsByProductId(int productId);

        // tìm kiếm tech specs theo productId
        List<Map<String, Object>> findTechByProductId(int productId);

        // tìm kiếm colors theo variantId
        List<Map<String, Object>> findColorsByVariantId(int variantId);

        // tìm kiếm variant color detail theo vcId
        Map<String, Object> findVariantColorDetailForEdit(int vcId);

        Product getProductById(int id);

        // tìm kiếm products theo từ khóa, trạng thái, danh mục
        List<Product> search(String keyword, Integer status, Integer categoryId);

        // cập nhật trạng thái product
        void updateStatus(int productId, int status);

        List<Map<String, Object>> getProductsByCategory(int categoryId);

        List<Map<String, Object>> getProductsByCategoryWithFilters(
                        int categoryId,
                        Double priceMin,
                        Double priceMax,
                        List<String> memory,
                        List<String> colors,
                        Integer year,
                        String brandName,
                        List<String> types,
                        String condition,
                        String sortBy);

        /**
         * Lấy products với pagination (TỐI ƯU HÓA - load 1 page)
         */
        List<Map<String, Object>> getProductsByCategoryPaginated(
                        int categoryId,
                        Double priceMin,
                        Double priceMax,
                        List<String> memory,
                        List<String> colors,
                        Integer year,
                        String brandName,
                        String sortBy,
                        int page,
                        int pageSize,
                        String search);

        /**
         * Đếm tổng số products thỏa filter
         */
        int countProductsByCategory(
                        int categoryId,
                        Double priceMin,
                        Double priceMax,
                        List<String> memory,
                        List<String> colors,
                        Integer year,
                        String brandName,
                        String search);

        List<Map<String, Object>> getAccessories();

        List<Map<String, Object>> getAccessoriesWithFilters(
                        Double priceMin,
                        Double priceMax,
                        Integer brandId,
                        List<String> types,
                        String condition,
                        String sortBy);

        List<Map<String, Object>> getAccessoriesWithFilters(
                        Double priceMin,
                        Double priceMax,
                        String brandName,
                        List<String> types,
                        String condition,
                        String sortBy);

        List<Map<String, Object>> getProductsForList();

        // ===== UPDATE PRODUCT =====
        void updateProductBasic(Handle h, Product p);

        // Xóa tech specs cũ của product
        void deleteTechSpecsByProductId(Handle h, int productId);

        // Cập nhật variant
        void updateVariant(Handle h, int variantId, String name, double basePrice);

        // cập nhật variant color
        void updateVariantColor(Handle h, int vcId, int quantity, String sku, double price);

        // ===== PRODUCT DETAIL =====

        // Thông tin chính product (brand, category)
        Product findProductDetailById(int productId);

        // Variants của product
        List<ProductVariant> getVariantsByProduct(int productId);

        // Colors theo variant
        List<TechSpecs> getTechSpecsByProduct(int variantId);

        // VariantColor mặc định (load trang)
        VariantColor getDefaultVariantColor(int productId);

        // cart
        Map<String, Object> getCartItemDetail(int variantColorId);

        // Sản phẩm liên quan
        // int brandId, : ID thương hiệu để lấy sản phẩm cùng hãng
        // int excludeProductId, : ID sản phẩm hiện tại (loại trừ khỏi danh sách liên
        // quan)
        // int limit : Số lượng sản phẩm liên quan cần lấy (ví dụ: 4)
        // Lấy sản phẩm liên quan cùng brand (ưu tiên)
        List<Map<String, Object>> findRelatedBySameBrand(
                        int brandId,
                        int excludeProductId,
                        int limit);

        // Lấy sản phẩm bán chạy / mới nhất để bù khi chưa đủ
        List<Map<String, Object>> findFallbackRelatedProducts(
                        int excludeProductId,
                        List<Integer> excludeIds,
                        int limit);

        /**
         * Lấy danh sách tất cả categories có category_id khác 1 (dành cho linh kiện)
         */
        List<Map<String, Object>> getAccessoryCategories();
}
