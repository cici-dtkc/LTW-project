package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDaoImpl;
import vn.edu.hcmuaf.fit.webdynamic.model.*;

import java.math.BigDecimal;
import java.util.*;

import static vn.edu.hcmuaf.fit.webdynamic.config.DBConnect.getJdbi;

public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    public List<Map<String, Object>> getForAdmin(
            String keyword,
            Integer status,
            Integer categoryId,
            int page,
            int limit) {

        int offset = (page - 1) * limit;

        List<Integer> vcIds = productDao.findVariantIdsForAdmin(
                keyword, status, categoryId, offset, limit
        );

        if (vcIds.isEmpty()) return List.of();

        return productDao.findForAdminByVariantIds(vcIds);

    }

    @Override
    public int countForAdmin(String keyword, Integer status, Integer categoryId) {
        return productDao.countForAdmin(keyword, status, categoryId);
    }

    @Override
    public boolean toggleStatus(int productId) {
        return productDao.toggleStatus(productId);
    }

    @Override
    public void addPhone(Product product) {

        getJdbi().useTransaction(handle -> { //  SỬA: transaction ở SERVICE

            int productId = productDao.insertProduct(handle, product);

            // TECH SPECS
            if (product.getTechSpecs() != null) {
                for (TechSpecs t : product.getTechSpecs()) {
                    productDao.insertTechSpec(handle, productId, t);
                }
            }

            if (product.getVariants() == null) return;

            for (ProductVariant v : product.getVariants()) {

                int variantId = productDao.insertVariant(handle, productId, v);

                // linh kiện không có màu → bỏ qua
                if (v.getColors() == null) continue;

                for (VariantColor c : v.getColors()) {

                    if (c.getColor() == null) {
                        throw new RuntimeException("Color is NULL");
                    }

                    productDao.insertVariantColor(handle, variantId, c);
                }
            }
        });
    }
    @Override
    public Map<String, Object> getProductForEditByVariantColorId(int vcId) {

        Map<String, Object> product =
                productDao.findProductByVariantColorId(vcId);

        if (product == null) {
            throw new RuntimeException("Product not found for vcId=" + vcId);
        }

        Integer productId = (Integer) product.get("product_id");
        if (productId == null) {
            throw new RuntimeException("product_id missing: " + product);
        }

        List<Map<String, Object>> variants =
                productDao.findVariantsByProductId(productId);

        for (Map<String, Object> v : variants) {
            Integer variantId = (Integer) v.get("variant_id");
            if (variantId == null) continue;

            v.put("colors",
                    productDao.findColorsByVariantId(variantId));
        }

        product.put("variants", variants);
        product.put("techs",
                productDao.findTechByProductId(productId));

        return product;
    }

    @Override
    public List<Product> getAllForAdmin() {
        return productDao.findAllWithVariants();
    }

    @Override
    public List<Map<String, Object>> getProductsForList() {
        return ((ProductDaoImpl) productDao).getProductsForListDisplay();
    }

    @Override
    public List<Map<String, Object>> getProductsByCategory(int categoryId) {
        return ((ProductDaoImpl) productDao).getProductsByCategory(categoryId);
    }

}



