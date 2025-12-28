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

    List<Integer> findVariantIdsForAdmin(
            String keyword,
            Integer status,
            Integer categoryId,
            int offset,
            int limit
    );

}
