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
    public List<Product> getForAdmin(
            String keyword,
            Integer status,
            Integer categoryId,
            int page,
            int limit) {

        int offset = (page - 1) * limit;

        List<Map<String, Object>> rows =
                productDao.findForAdmin(keyword, status, categoryId, offset, limit);

        Map<Integer, Product> productMap = new LinkedHashMap<>();
        Map<Integer, ProductVariant> variantMap = new HashMap<>();

        for (Map<String, Object> row : rows) {

            int productId = (int) row.get("p_id");
            Product product = productMap.computeIfAbsent(productId, id -> {
                Product p = new Product();
                p.setId(id);
                p.setName((String) row.get("p_name"));
                p.setMainImage((String) row.get("p_img"));

                Category c = new Category();
                c.setId((int) row.get("c_id"));
                c.setName((String) row.get("c_name"));
                p.setCategory(c);

                p.setVariants(new ArrayList<>());
                return p;
            });

            int variantId = (int) row.get("v_id");
            ProductVariant variant = variantMap.computeIfAbsent(variantId, id -> {
                ProductVariant v = new ProductVariant();
                v.setId(id);
                v.setName((String) row.get("v_name"));
                v.setBasePrice(((BigDecimal) row.get("base_price")).doubleValue());
                v.setStatus((int) row.get("v_status"));
                v.setColors(new ArrayList<>());

                product.getVariants().add(v);
                return v;
            });

            VariantColor vc = new VariantColor();
            vc.setId((int) row.get("vc_id"));
            vc.setPrice(((BigDecimal) row.get("vc_price")).doubleValue());
            vc.setQuantity((int) row.get("quantity"));

            Color color = new Color();
            color.setId((int) row.get("color_id"));
            color.setName((String) row.get("color_name"));

            vc.setColor(color);
            variant.getColors().add(vc);
        }

        return new ArrayList<>(productMap.values());
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

        getJdbi().useTransaction(handle -> {

            int productId = productDao.insertProduct(product);

            if (product.getTechSpecs() != null) {
                for (TechSpecs t : product.getTechSpecs()) {
                    productDao.insertTechSpec(productId, t);
                }
            }

            for (ProductVariant v : product.getVariants()) {
                int variantId = productDao.insertVariant(productId, v);

                for (VariantColor c : v.getColors()) {
                    c.setId(variantId);
                    productDao.insertVariantColor(c);
                }
            }
        });
    }


}
