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
            // 1. Lưu sản phẩm chính
            int productId = productDao.insertProduct(handle, product);

            // 2. Xử lý Thông số kỹ thuật
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

            // 3. XỬ LÝ VARIANTS & COLORS
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
        // 1. Lấy thông tin cơ bản sản phẩm
        Map<String, Object> product = productDao.findProductByVariantColorId(vcId);
        int productId = (int) product.get("product_id");

        // 2. Lấy chi tiết phiên bản và màu sắc đang edit
        Map<String, Object> detail = (  productDao).findVariantColorDetailForEdit(vcId);

        // Đưa toàn bộ chi tiết ra ngoài Map cha để JSP gọi trực tiếp ${product.variant_name}
        product.putAll(detail);

        // 3. Lấy danh sách thông số kỹ thuật
        product.put("techs", productDao.findTechByProductId(productId));

        // 4. (Tùy chọn) Nếu form Accessory cần danh sách variants
        List<Map<String, Object>> variants = productDao.findVariantsByProductId(productId);
        for (Map<String, Object> v : variants) {
            v.put("colors", productDao.findColorsByVariantId((int) v.get("variant_id")));
        }
        product.put("variants", variants);

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


    @Override
    public void updateProduct(Product product, String[] techNames, String[] techValues, String[] techPriorities,
                              String[] vNames, String[] bPrices, String[] vIds, String[] cIds,
                              String[] skus, String[] qtys, String[] cPrices) throws Exception {

        getJdbi().useTransaction(handle -> {
            productDao.updateProductBasic(handle, product);

            productDao.deleteTechSpecsByProductId(handle, product.getId());
            if (techNames != null) {
                for (int i = 0; i < techNames.length; i++) {
                    // Kiểm tra nếu tên thông số trống thì bỏ qua không lưu
                    if (techNames[i] == null || techNames[i].isBlank()) continue;
                    TechSpecs t = new TechSpecs();
                    t.setName(techNames[i]);
                    t.setValue(techValues[i]);
                    t.setPriority(Integer.parseInt(techPriorities[i]));
                    productDao.insertTechSpec(handle, product.getId(), t);
                }
            }

            int categoryId = product.getCategory().getId();

            if (categoryId == 1) {
                int vId = Integer.parseInt(vIds[0]);
                int vcId = Integer.parseInt(cIds[0]);
                double baseP = Double.parseDouble(bPrices[0]);
                double colorP = Double.parseDouble(cPrices[0]);
                int quantity = Integer.parseInt(qtys[0]);

                productDao.updateVariant(handle, vId, vNames[0], baseP);
                productDao.updateVariantColor(handle, vcId, quantity, skus[0], colorP);
            } else {
                if (vIds != null) {
                    for (int i = 0; i < vIds.length; i++) {
                        int vId = Integer.parseInt(vIds[i]);
                        int vcId = Integer.parseInt(cIds[i]);
                        double price = Double.parseDouble(cPrices[i]);
                        int quantity = Integer.parseInt(qtys[i]);

                        productDao.updateVariant(handle, vId, "Default", price);
                        productDao.updateVariantColor(handle, vcId, quantity, "", price);
                    }
                }
            }
        });
    }


    @Override
    public List<Map<String, Object>> getProductsByCategoryWithFilters(
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
    ) {
        return ((ProductDaoImpl) productDao).getProductsByCategoryWithFilters(
                categoryId, priceMin, priceMax, memory, colors, year, brandId, types, condition, sortBy
        );
    }

    @Override
    public List<Map<String, Object>> getAccessories() {
        return ((ProductDaoImpl) productDao).getAccessories();
    }

    @Override
    public List<Map<String, Object>> getAccessoriesWithFilters(
            Double priceMin,
            Double priceMax,
            Integer brandId,
            List<String> types,
            String condition,
            String sortBy
    ) {
        return ((ProductDaoImpl) productDao).getAccessoriesWithFilters(
                priceMin, priceMax, brandId, types, condition, sortBy
        );
    }



    // cart

    @Override
        public Map<String, Object> getProductForCart(int variantColorId) {
            Map<String, Object> detail = productDao.getCartItemDetail(variantColorId);
            if (detail != null) {
                // Lấy giá gốc và % giảm giá từ DB
                double unitPrice = Double.parseDouble(detail.get("unit_price").toString());
                int discount = Integer.parseInt(detail.get("discount_percentage").toString());

                // Tính giá bán thực tế
                double finalPrice = unitPrice * (100 - discount) / 100;
                detail.put("price_final", finalPrice);
            }
            return detail;
        }

    @Override
    public List<Map<String, Object>> getRelatedProducts(
            int brandId,
            int excludeProductId, int limit
    ) {


        // 1. Ưu tiên cùng brand
        List<Map<String, Object>> products =
                productDao.findRelatedBySameBrand(brandId, excludeProductId, limit);

        // 2. Nếu chưa đủ → lấy bù
        if (products.size() < limit) {
            int remain = limit - products.size();

            List<Integer> existedIds = products.stream()
                    .map(p -> (Integer) p.get("id"))
                    .toList();

            List<Map<String, Object>> fallback =
                    productDao.findFallbackRelatedProducts(
                            excludeProductId,
                            existedIds,
                            remain
                    );

            products.addAll(fallback);
        }

        return products;
    }

}




