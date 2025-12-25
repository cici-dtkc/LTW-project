package vn.edu.hcmuaf.fit.webdynamic.dao;

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
    int insertProduct(Product product);

    int insertVariant(int productId,ProductVariant variant);

    void insertVariantColor(VariantColor color);

    void insertTechSpec(int productId, TechSpecs tech);;
}
