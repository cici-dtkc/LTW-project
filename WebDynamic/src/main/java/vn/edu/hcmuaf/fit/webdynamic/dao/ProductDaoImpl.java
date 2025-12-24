package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ProductDaoImpl implements ProductDao {
    private final Jdbi jdbi = DBConnect.getJdbi();

    @Override
    public List<Product> findAllWithVariants() {

        String sql = """
                    SELECT
                        p.id              AS p_id,
                        p.name            AS p_name,
                        p.img             AS p_img,
                        c.id              AS c_id,
                        c.name            AS c_name,
                        v.id              AS v_id,
                        v.name            AS v_name,
                        v.base_price,
                        v.status          AS v_status,
                        vc.id             AS vc_id,
                        vc.price          AS vc_price,
                        vc.quantity,
                        col.id            AS color_id,
                        col.name          AS color_name
                    FROM products p
                    JOIN categories c        ON p.category_id = c.id
                    JOIN product_variants v ON p.id = v.product_id
                    JOIN variant_colors vc  ON v.id = vc.variant_id
                    JOIN colors col          ON vc.color_id = col.id
                    ORDER BY p.id DESC
                """;

        return jdbi.withHandle(handle -> {

            Map<Integer, Product> productMap = new LinkedHashMap<>();

            handle.createQuery(sql)
                    .map((rs, ctx) -> {

                        int productId = rs.getInt("p_id");

                        // ===== PRODUCT =====
                        Product product = productMap.get(productId);
                        if (product == null) {
                            product = new Product();
                            product.setId(productId);
                            product.setName(rs.getString("p_name"));
                            product.setMainImage(rs.getString("p_img"));

                            Category category = new Category();
                            category.setId(rs.getInt("c_id"));
                            category.setName(rs.getString("c_name"));
                            product.setCategory(category);

                            product.setVariants(new ArrayList<>());
                            productMap.put(productId, product);
                        }

                        // ===== VARIANT =====
                        int variantId = rs.getInt("v_id");
                        ProductVariant variant = null;

                        for (ProductVariant v : product.getVariants()) {
                            if (v.getId() == variantId) {
                                variant = v;
                                break;
                            }
                        }

                        if (variant == null) {
                            variant = new ProductVariant();
                            variant.setId(variantId);
                            variant.setName(rs.getString("v_name"));
                            variant.setBasePrice(rs.getDouble("base_price"));
                            variant.setStatus(rs.getInt("v_status"));
                            variant.setColors(new ArrayList<>());

                            product.getVariants().add(variant);
                        }

                        // ===== VARIANT COLOR =====
                        VariantColor vc = new VariantColor();
                        vc.setId(rs.getInt("vc_id"));
                        vc.setPrice(rs.getDouble("vc_price"));
                        vc.setQuantity(rs.getInt("quantity"));

                        Color color = new Color();
                        color.setId(rs.getInt("color_id"));
                        color.setName(rs.getString("color_name"));
                        vc.setColor(color);

                        variant.getColors().add(vc);

                        return null;
                    }).list();

            return new ArrayList<>(productMap.values());
        });
    }

    @Override
    public void updateStatus(int productId, int status) {
        String sql = "UPDATE products SET status = ? WHERE id = ?";

        try (Connection conn = (Connection) DBConnect.getJdbi();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, status);
            ps.setInt(2, productId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = (Connection) DBConnect.getJdbi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // return mapResult(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> search(String keyword, Integer status, Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + keyword + "%");
        }
        if (status != null) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }
        sql.append(" ORDER BY id DESC");

        return jdbi.withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.bind(i, params.get(i));
            }
            return query.map((rs, ctx) -> {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setMainImage(rs.getString("img"));
                p.setDescription(rs.getString("description"));
                p.setDiscountPercentage(rs.getInt("discount_percentage"));
                p.setTotalSold(rs.getInt("total_sold"));
                p.setWarrantyPeriod(rs.getInt("warranty_period"));
                p.setStatus(rs.getInt("status"));
                return p;
            }).list();
        });
    }

    @Override
    public List<Map<String, Object>> getProductsByCategory(int categoryId) {
        String sql = """
                    SELECT
                        p.id,
                        p.name,
                        p.img AS image,
                        p.discount_percentage AS discount,
                        MIN(vc.price) AS priceNew,
                        MAX(vc.price) AS priceOld,
                        GROUP_CONCAT(DISTINCT v.name ORDER BY v.name) AS capacities,
                        0 AS rating,
                        p.total_sold AS soldCount
                    FROM products p
                    LEFT JOIN product_variants v ON p.id = v.product_id
                    LEFT JOIN variant_colors vc ON v.id = vc.variant_id
                    WHERE p.status = 1 AND p.category_id = ?
                    GROUP BY p.id
                    ORDER BY p.id DESC
                """;

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind(0, categoryId)
                .map((rs, ctx) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getInt("id"));
                    map.put("name", rs.getString("name"));
                    map.put("image", rs.getString("image"));
                    map.put("discount", rs.getInt("discount"));
                    map.put("priceNew", rs.getDouble("priceNew"));
                    map.put("priceOld", rs.getDouble("priceOld"));
                    String capacitiesStr = rs.getString("capacities");
                    if (capacitiesStr != null) {
                        map.put("capacities", Arrays.asList(capacitiesStr.split(",")));
                    } else {
                        map.put("capacities", new ArrayList<>());
                    }
                    map.put("rating", rs.getInt("rating"));
                    map.put("soldCount", rs.getInt("soldCount"));
                    return map;
                }).list());
    }

    @Override
    public List<Map<String, Object>> getProductsForList() {
        // Placeholder
        return List.of();
    }

    public List<Map<String, Object>> getProductsForListDisplay() {
        String sql = """
                    SELECT
                        p.id,
                        p.name,
                        p.img AS image,
                        p.discount_percentage AS discount,
                        MIN(vc.price) AS priceNew,
                        MAX(vc.price) AS priceOld,
                        GROUP_CONCAT(DISTINCT v.name ORDER BY v.name) AS capacities,
                        0 AS rating,
                        p.total_sold AS soldCount
                    FROM products p
                    LEFT JOIN product_variants v ON p.id = v.product_id
                    LEFT JOIN variant_colors vc ON v.id = vc.variant_id
                    WHERE p.status = 1
                    GROUP BY p.id
                    ORDER BY p.id DESC
                """;

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getInt("id"));
                    map.put("name", rs.getString("name"));
                    map.put("image", rs.getString("image"));
                    map.put("discount", rs.getInt("discount"));
                    map.put("priceNew", rs.getDouble("priceNew"));
                    map.put("priceOld", rs.getDouble("priceOld"));
                    String capacitiesStr = rs.getString("capacities");
                    if (capacitiesStr != null) {
                        map.put("capacities", Arrays.asList(capacitiesStr.split(",")));
                    } else {
                        map.put("capacities", new ArrayList<>());
                    }
                    map.put("rating", rs.getInt("rating"));
                    map.put("soldCount", rs.getInt("soldCount"));
                    return map;
                }).list());
    }

    // private Product mapResult(ResultSet rs) throws SQLException {
    // Product p = new Product();
    // p.setId(rs.getInt("id"));
    // p.setName(rs.getString("name"));
    // p.setMainImage(rs.getString("img"));
    // p.setDescription(rs.getString("description"));
    // p.setCategoryId(rs.getInt("category_id"));
    // p.setBrandId((Integer) rs.getObject("brand_id"));
    // p.setStatus(rs.getInt("status"));
    // return p;
    // }
}
