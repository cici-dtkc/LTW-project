package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDaoImpl;
import vn.edu.hcmuaf.fit.webdynamic.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public void addProduct(Product product,
                           String[] techNames, String[] techValues, String[] techPriorities,
                           String[] variantNames, String[] basePrices,
                           String[] quantities, String[] variantQuantities,
                           String[] skus, String[] colorVariantIndexes,
                           String[] colorIds, String[] customColors, String[] colorPrices) throws Exception {

        getJdbi().useTransaction(handle -> {
            // 1. Lưu sản phẩm chính (Dùng hàm của bạn đã có warranty_period và release_date)
            int productId = productDao.insertProduct(handle, product);

            // 2. Xử lý Thông số kỹ thuật (Bê nguyên từ Controller sang)
            if (techNames != null) {
                for (int i = 0; i < techNames.length; i++) {
                    if (techNames[i] == null || techNames[i].isBlank()) continue;
                    TechSpecs t = new TechSpecs();
                    t.setName(techNames[i]);
                    t.setValue(techValues[i]);
                    t.setPriority(parseInt(techPriorities[i]));
                    productDao.insertTechSpec(handle, productId, t);
                }
            }

            // 3. XỬ LÝ VARIANTS & COLORS (Bê nguyên từ Controller sang)
            if (variantNames != null) {
                for (int i = 0; i < variantNames.length; i++) {
                    ProductVariant v = new ProductVariant();
                    v.setName(variantNames[i]);
                    v.setBasePrice(parseDbl(basePrices != null ? basePrices[i] : "0"));
                    v.setStatus(1);
                    v.setCreatedAt(LocalDateTime.now());

                    int variantId = productDao.insertVariant(handle, productId, v);

                    // Lấy categoryId từ object product truyền vào
                    int categoryId = product.getCategory().getId();

                    if (categoryId == 1) {
                        // --- TRƯỜNG HỢP ĐIỆN THOẠI ---
                        if (colorIds != null && colorVariantIndexes != null) {
                            for (int j = 0; j < colorIds.length; j++) {
                                int belongsToVariant = parseInt(colorVariantIndexes[j]);

                                if (belongsToVariant == i) {
                                    VariantColor vc = new VariantColor();
                                    if ("custom".equals(colorIds[j])) {
                                        Color c = new Color();
                                        c.setName(customColors != null ? customColors[j] : "Màu mới");
                                        vc.setColor(c);
                                    } else {
                                        vc.setColor(new Color(parseInt(colorIds[j])));
                                    }
                                    vc.setPrice(parseDbl(colorPrices != null ? colorPrices[j] : "0"));
                                    vc.setQuantity(parseInt(quantities != null ? quantities[j] : "0"));
                                    vc.setCreatedAt(LocalDateTime.now());
                                    if (skus != null && j < skus.length) {
                                        vc.setSku(skus[j]);
                                    }
                                    productDao.insertVariantColor(handle, variantId, vc);
                                }
                            }
                        }
                    } else {
                        // --- TRƯỜNG HỢP LINH KIỆN ---
                        VariantColor vc = new VariantColor();
                        vc.setColor(new Color(1)); // Màu mặc định
                        vc.setPrice(0.0);
                        vc.setQuantity(parseInt(variantQuantities != null ? variantQuantities[i] : "0"));
                        vc.setCreatedAt(LocalDateTime.now());
                        productDao.insertVariantColor(handle, variantId, vc);
                    }
                }
            }
        });
    }

    // Giữ nguyên hàm helper của bạn
    private int parseInt(String s) {
        if (s == null || s.trim().isEmpty()) return 0;
        return Integer.parseInt(s.trim());
    }

    private double parseDbl(String s) {
        if (s == null || s.trim().isEmpty()) return 0.0;
        return Double.parseDouble(s.trim());
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
    public List<Map<String, Object>> getProductsForList() {
        return ((ProductDaoImpl) productDao).getProductsForListDisplay();
    }

    @Override
    public List<Map<String, Object>> getProductsByCategory(int categoryId) {
        return ((ProductDaoImpl) productDao).getProductsByCategory(categoryId);
    }

}



