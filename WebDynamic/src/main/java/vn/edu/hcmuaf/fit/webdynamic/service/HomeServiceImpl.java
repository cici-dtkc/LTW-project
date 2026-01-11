package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.HomeDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.HomeDaoImpl;

import java.util.*;

public class HomeServiceImpl implements HomeService {
    private final HomeDao homeDao = new HomeDaoImpl();

    @Override
    public List<Map<String, Object>> getFeaturedProducts() {
        List<Map<String, Object>> rawList = homeDao.getFeaturedProducts();
        return groupByProduct(rawList);
    }

    @Override
    public List<Map<String, Object>> getFeaturedAccessories() {
        List<Map<String, Object>> rawList = homeDao.getFeaturedAccessories();
        return groupByProduct(rawList);
    }

    @Override
    public List<Map<String, Object>> getActiveVouchers() {
        return homeDao.getActiveVouchers();
    }

    private List<Map<String, Object>> groupByProduct(List<Map<String, Object>> rawList) {
        Map<Integer, Map<String, Object>> productMap = new LinkedHashMap<>();
        for (Map<String, Object> row : rawList) {
            Integer productId = (Integer) row.get("id");
            if (!productMap.containsKey(productId)) {
                // Create product map
                Map<String, Object> product = new HashMap<>();
                product.put("id", row.get("id"));
                product.put("name", row.get("name"));
                product.put("description", row.get("description"));
                product.put("discount_percentage", row.get("discount_percentage"));
                product.put("total_sold", row.get("total_sold"));
                product.put("warranty_period", row.get("warranty_period"));
                product.put("status", row.get("status"));
                product.put("main_image", row.get("main_image"));
                product.put("category_name", row.get("category_name"));
                product.put("brand_name", row.get("brand_name"));
                product.put("variants", new ArrayList<Map<String, Object>>());
                productMap.put(productId, product);
            }
            // Add variant
            Map<String, Object> variant = new HashMap<>();
            variant.put("variant_name", row.get("variant_name"));
            variant.put("base_price", row.get("base_price"));
            variant.put("variant_color_price", row.get("variant_color_price"));
            variant.put("variant_color_id", row.get("variant_color_id")); // THÊM DÒNG NÀY
            variant.put("quantity", row.get("quantity"));
            variant.put("sku", row.get("sku"));
            variant.put("color_name", row.get("color_name"));
            ((List<Map<String, Object>>) productMap.get(productId).get("variants")).add(variant);
        }
        return new ArrayList<>(productMap.values());
    }
}