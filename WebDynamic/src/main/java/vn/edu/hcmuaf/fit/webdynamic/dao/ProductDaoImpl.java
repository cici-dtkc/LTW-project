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
                    v.id AS variant_id,
                    v.name AS variant_name,
                    vc.price AS variant_price,
                    (vc.price * (100 - p.discount_percentage) / 100) AS variant_price_new,
                    COALESCE(AVG(f.rating), 0) AS rating,
                    p.total_sold AS soldCount
                FROM products p
                LEFT JOIN product_variants v ON p.id = v.product_id
                LEFT JOIN variant_colors vc ON v.id = vc.variant_id
                LEFT JOIN feedback f ON p.id = f.product_id AND f.status = 1
                WHERE p.status = 1 AND p.category_id = ?
                GROUP BY p.id, p.name, p.img, p.discount_percentage, p.total_sold, 
                         v.id, v.name, vc.price
                ORDER BY p.id DESC, v.name ASC
            """;

        return jdbi.withHandle(handle -> {
            List<Map<String, Object>> rawResults = handle.createQuery(sql)
                    .bind(0, categoryId)
                    .map((rs, ctx) -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", rs.getInt("id"));
                        map.put("name", rs.getString("name"));
                        map.put("image", rs.getString("image"));
                        map.put("discount", rs.getInt("discount"));
                        map.put("variant_id", rs.getInt("variant_id"));
                        map.put("variant_name", rs.getString("variant_name"));
                        map.put("variant_price", rs.getDouble("variant_price"));
                        map.put("variant_price_new", rs.getDouble("variant_price_new"));
                        map.put("rating", Math.round(rs.getDouble("rating")));
                        map.put("soldCount", rs.getInt("soldCount"));
                        return map;
                    }).list();

            // Group by product
            Map<Integer, Map<String, Object>> productMap = new LinkedHashMap<>();

            for (Map<String, Object> row : rawResults) {
                int productId = (int) row.get("id");

                if (!productMap.containsKey(productId)) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("id", row.get("id"));
                    product.put("name", row.get("name"));
                    product.put("image", row.get("image"));
                    product.put("discount", row.get("discount"));
                    product.put("rating", row.get("rating"));
                    product.put("soldCount", row.get("soldCount"));
                    product.put("variants", new ArrayList<Map<String, Object>>());
                    productMap.put(productId, product);
                }

                // Add variant info
                Map<String, Object> variant = new HashMap<>();
                variant.put("id", row.get("variant_id"));
                variant.put("name", row.get("variant_name"));
                variant.put("priceOld", row.get("variant_price"));
                variant.put("priceNew", row.get("variant_price_new"));

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> variants =
                        (List<Map<String, Object>>) productMap.get(productId).get("variants");
                variants.add(variant);
            }

            // Set default price (first variant)
            for (Map<String, Object> product : productMap.values()) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> variants =
                        (List<Map<String, Object>>) product.get("variants");

                if (!variants.isEmpty()) {
                    product.put("priceNew", variants.get(0).get("priceNew"));
                    product.put("priceOld", variants.get(0).get("priceOld"));
                }
            }

            return new ArrayList<>(productMap.values());
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
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.id,
                    p.name,
                    p.img AS image,
                    p.discount_percentage AS discount,
                    p.brand_id,
                    p.release_date,
                    p.created_at,
                    v.id AS variant_id,
                    v.name AS variant_name,
                    vc.price AS variant_price,
                    (vc.price * (100 - p.discount_percentage) / 100) AS variant_price_new,
                    col.name AS color_name,
                    COALESCE(AVG(f.rating), 0) AS rating,
                    p.total_sold AS soldCount
                FROM products p
                LEFT JOIN product_variants v ON p.id = v.product_id
                LEFT JOIN variant_colors vc ON v.id = vc.variant_id
                LEFT JOIN colors col ON vc.color_id = col.id
                LEFT JOIN feedback f ON p.id = f.product_id AND f.status = 1
                WHERE p.status = 1 AND p.category_id = ?
            """);

        List<Object> params = new ArrayList<>();
        params.add(categoryId);

        // Lọc theo giá
        if (priceMin != null) {
            sql.append(" AND (vc.price * (100 - p.discount_percentage) / 100) >= ?");
            params.add(priceMin);
        }
        if (priceMax != null) {
            sql.append(" AND (vc.price * (100 - p.discount_percentage) / 100) <= ?");
            params.add(priceMax);
        }

        // Lọc theo bộ nhớ (memory) - cho điện thoại
        if (memory != null && !memory.isEmpty()) {
            sql.append(" AND v.name IN (");
            for (int i = 0; i < memory.size(); i++) {
                if (i > 0) sql.append(",");
                sql.append("?");
                params.add(memory.get(i));
            }
            sql.append(")");
        }

        // Lọc theo màu sắc
        if (colors != null && !colors.isEmpty()) {
            sql.append(" AND col.name IN (");
            for (int i = 0; i < colors.size(); i++) {
                if (i > 0) sql.append(",");
                sql.append("?");
                params.add(colors.get(i));
            }
            sql.append(")");
        }

        // Lọc theo năm ra mắt
        if (year != null) {
            sql.append(" AND YEAR(p.release_date) = ?");
            params.add(year);
        }

        // Lọc theo thương hiệu
        if (brandId != null) {
            sql.append(" AND p.brand_id = ?");
            params.add(brandId);
        }

        // Lọc theo loại linh kiện (types) - có thể cần thêm bảng hoặc trường
        // Tạm thời bỏ qua vì chưa rõ cấu trúc

        // Lọc theo tình trạng (condition) - có thể cần thêm trường
        // Tạm thời bỏ qua vì chưa rõ cấu trúc

        sql.append("""
                GROUP BY p.id, p.name, p.img, p.discount_percentage, p.total_sold, 
                         p.brand_id, p.release_date, p.created_at,
                         v.id, v.name, vc.price, col.name
            """);

        // Sắp xếp
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "banchay":
                case "Bán chạy":
                    sql.append(" ORDER BY p.total_sold DESC, v.name ASC");
                    break;
                case "giamgia":
                case "Giảm giá":
                    sql.append(" ORDER BY p.discount_percentage DESC, v.name ASC");
                    break;
                case "moi":
                case "Mới":
                    sql.append(" ORDER BY p.created_at DESC, v.name ASC");
                    break;
                case "giatang":
                case "Giá tăng":
                    sql.append(" ORDER BY variant_price_new ASC, v.name ASC");
                    break;
                case "giagiam":
                case "Giá giảm":
                    sql.append(" ORDER BY variant_price_new DESC, v.name ASC");
                    break;
                default: // Nổi bật
                    sql.append(" ORDER BY p.id DESC, v.name ASC");
                    break;
            }
        } else {
            sql.append(" ORDER BY p.id DESC, v.name ASC");
        }

        String finalSql = sql.toString();

        return jdbi.withHandle(handle -> {
            var query = handle.createQuery(finalSql);
            for (int i = 0; i < params.size(); i++) {
                query.bind(i, params.get(i));
            }

            List<Map<String, Object>> rawResults = query.map((rs, ctx) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                map.put("image", rs.getString("image"));
                map.put("discount", rs.getInt("discount"));
                map.put("variant_id", rs.getInt("variant_id"));
                map.put("variant_name", rs.getString("variant_name"));
                map.put("variant_price", rs.getDouble("variant_price"));
                map.put("variant_price_new", rs.getDouble("variant_price_new"));
                map.put("rating", Math.round(rs.getDouble("rating")));
                map.put("soldCount", rs.getInt("soldCount"));
                return map;
            }).list();

            // Group by product
            Map<Integer, Map<String, Object>> productMap = new LinkedHashMap<>();

            for (Map<String, Object> row : rawResults) {
                int productId = (int) row.get("id");

                if (!productMap.containsKey(productId)) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("id", row.get("id"));
                    product.put("name", row.get("name"));
                    product.put("image", row.get("image"));
                    product.put("discount", row.get("discount"));
                    product.put("rating", row.get("rating"));
                    product.put("soldCount", row.get("soldCount"));
                    product.put("variants", new ArrayList<Map<String, Object>>());
                    productMap.put(productId, product);
                }

                // Add variant info
                Map<String, Object> variant = new HashMap<>();
                variant.put("id", row.get("variant_id"));
                variant.put("name", row.get("variant_name"));
                variant.put("priceOld", row.get("variant_price"));
                variant.put("priceNew", row.get("variant_price_new"));

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> variants =
                        (List<Map<String, Object>>) productMap.get(productId).get("variants");
                variants.add(variant);
            }

            // Set default price (first variant)
            for (Map<String, Object> product : productMap.values()) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> variants =
                        (List<Map<String, Object>>) product.get("variants");

                if (!variants.isEmpty()) {
                    product.put("priceNew", variants.get(0).get("priceNew"));
                    product.put("priceOld", variants.get(0).get("priceOld"));
                }
            }

            return new ArrayList<>(productMap.values());
        });
    }

    @Override
    public List<Map<String, Object>> getAccessories() {
        String sql = """
                SELECT
                    p.id,
                    p.name,
                    p.img AS image,
                    p.discount_percentage AS discount,
                    v.id AS variant_id,
                    v.name AS variant_name,
                    vc.price AS variant_price,
                    (vc.price * (100 - p.discount_percentage) / 100) AS variant_price_new,
                    COALESCE(AVG(f.rating), 0) AS rating,
                    p.total_sold AS soldCount
                FROM products p
                LEFT JOIN product_variants v ON p.id = v.product_id
                LEFT JOIN variant_colors vc ON v.id = vc.variant_id
                LEFT JOIN feedback f ON p.id = f.product_id AND f.status = 1
                WHERE p.status = 1 AND p.category_id != 1
                GROUP BY p.id, p.name, p.img, p.discount_percentage, p.total_sold, 
                         v.id, v.name, vc.price
                ORDER BY p.id DESC, v.name ASC
            """;

        return jdbi.withHandle(handle -> {
            List<Map<String, Object>> rawResults = handle.createQuery(sql)
                    .map((rs, ctx) -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", rs.getInt("id"));
                        map.put("name", rs.getString("name"));
                        map.put("image", rs.getString("image"));
                        map.put("discount", rs.getInt("discount"));
                        map.put("variant_id", rs.getInt("variant_id"));
                        map.put("variant_name", rs.getString("variant_name"));
                        map.put("variant_price", rs.getDouble("variant_price"));
                        map.put("variant_price_new", rs.getDouble("variant_price_new"));
                        map.put("rating", Math.round(rs.getDouble("rating")));
                        map.put("soldCount", rs.getInt("soldCount"));
                        return map;
                    }).list();

            // Group by product
            Map<Integer, Map<String, Object>> productMap = new LinkedHashMap<>();

            for (Map<String, Object> row : rawResults) {
                int productId = (int) row.get("id");

                if (!productMap.containsKey(productId)) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("id", row.get("id"));
                    product.put("name", row.get("name"));
                    product.put("image", row.get("image"));
                    product.put("discount", row.get("discount"));
                    product.put("rating", row.get("rating"));
                    product.put("soldCount", row.get("soldCount"));
                    product.put("variants", new ArrayList<Map<String, Object>>());
                    productMap.put(productId, product);
                }

                // Add variant info
                Map<String, Object> variant = new HashMap<>();
                variant.put("id", row.get("variant_id"));
                variant.put("name", row.get("variant_name"));
                variant.put("priceOld", row.get("variant_price"));
                variant.put("priceNew", row.get("variant_price_new"));

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> variants =
                        (List<Map<String, Object>>) productMap.get(productId).get("variants");
                variants.add(variant);
            }

            // Set default price (first variant)
            for (Map<String, Object> product : productMap.values()) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> variants =
                        (List<Map<String, Object>>) product.get("variants");

                if (!variants.isEmpty()) {
                    product.put("priceNew", variants.get(0).get("priceNew"));
                    product.put("priceOld", variants.get(0).get("priceOld"));
                }
            }

            return new ArrayList<>(productMap.values());
        });
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
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.id,
                    p.name,
                    p.img AS image,
                    p.discount_percentage AS discount,
                    p.brand_id,
                    p.release_date,
                    p.created_at,
                    v.id AS variant_id,
                    v.name AS variant_name,
                    vc.price AS variant_price,
                    (vc.price * (100 - p.discount_percentage) / 100) AS variant_price_new,
                    col.name AS color_name,
                    COALESCE(AVG(f.rating), 0) AS rating,
                    p.total_sold AS soldCount
                FROM products p
                LEFT JOIN product_variants v ON p.id = v.product_id
                LEFT JOIN variant_colors vc ON v.id = vc.variant_id
                LEFT JOIN colors col ON vc.color_id = col.id
                LEFT JOIN feedback f ON p.id = f.product_id AND f.status = 1
                WHERE p.status = 1 AND p.category_id != 1
            """);

        List<Object> params = new ArrayList<>();

        // Lọc theo giá
        if (priceMin != null) {
            sql.append(" AND (vc.price * (100 - p.discount_percentage) / 100) >= ?");
            params.add(priceMin);
        }
        if (priceMax != null) {
            sql.append(" AND (vc.price * (100 - p.discount_percentage) / 100) <= ?");
            params.add(priceMax);
        }

        // Lọc theo thương hiệu
        if (brandId != null) {
            sql.append(" AND p.brand_id = ?");
            params.add(brandId);
        }

        sql.append("""
                GROUP BY p.id, p.name, p.img, p.discount_percentage, p.total_sold, 
                         p.brand_id, p.release_date, p.created_at,
                         v.id, v.name, vc.price, col.name
            """);

        // Sắp xếp
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "banchay":
                case "Bán chạy":
                    sql.append(" ORDER BY p.total_sold DESC, v.name ASC");
                    break;
                case "giamgia":
                case "Giảm giá":
                    sql.append(" ORDER BY p.discount_percentage DESC, v.name ASC");
                    break;
                case "moi":
                case "Mới":
                    sql.append(" ORDER BY p.created_at DESC, v.name ASC");
                    break;
                case "giatang":
                case "Giá tăng":
                    sql.append(" ORDER BY variant_price_new ASC, v.name ASC");
                    break;
                case "giagiam":
                case "Giá giảm":
                    sql.append(" ORDER BY variant_price_new DESC, v.name ASC");
                    break;
                default:
                    sql.append(" ORDER BY p.id DESC, v.name ASC");
                    break;
            }
        } else {
            sql.append(" ORDER BY p.id DESC, v.name ASC");
        }

        String finalSql = sql.toString();

        return jdbi.withHandle(handle -> {
            var query = handle.createQuery(finalSql);
            for (int i = 0; i < params.size(); i++) {
                query.bind(i, params.get(i));
            }

            List<Map<String, Object>> rawResults = query.map((rs, ctx) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                map.put("image", rs.getString("image"));
                map.put("discount", rs.getInt("discount"));
                map.put("variant_id", rs.getInt("variant_id"));
                map.put("variant_name", rs.getString("variant_name"));
                map.put("variant_price", rs.getDouble("variant_price"));
                map.put("variant_price_new", rs.getDouble("variant_price_new"));
                map.put("rating", Math.round(rs.getDouble("rating")));
                map.put("soldCount", rs.getInt("soldCount"));
                return map;
            }).list();

            // Group by product
            Map<Integer, Map<String, Object>> productMap = new LinkedHashMap<>();

            for (Map<String, Object> row : rawResults) {
                int productId = (int) row.get("id");

                if (!productMap.containsKey(productId)) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("id", row.get("id"));
                    product.put("name", row.get("name"));
                    product.put("image", row.get("image"));
                    product.put("discount", row.get("discount"));
                    product.put("rating", row.get("rating"));
                    product.put("soldCount", row.get("soldCount"));
                    product.put("variants", new ArrayList<Map<String, Object>>());
                    productMap.put(productId, product);
                }

                // Add variant info
                Map<String, Object> variant = new HashMap<>();
                variant.put("id", row.get("variant_id"));
                variant.put("name", row.get("variant_name"));
                variant.put("priceOld", row.get("variant_price"));
                variant.put("priceNew", row.get("variant_price_new"));

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> variants =
                        (List<Map<String, Object>>) productMap.get(productId).get("variants");
                variants.add(variant);
            }

            // Set default price (first variant)
            for (Map<String, Object> product : productMap.values()) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> variants =
                        (List<Map<String, Object>>) product.get("variants");

                if (!variants.isEmpty()) {
                    product.put("priceNew", variants.get(0).get("priceNew"));
                    product.put("priceOld", variants.get(0).get("priceOld"));
                }
            }

            return new ArrayList<>(productMap.values());
        });
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
                        MIN(vc.price) AS priceOld,
                        MIN(vc.price * (100 - p.discount_percentage) / 100) AS priceNew,
                        MAX(vc.price) AS maxPriceOld,
                        MAX(vc.price * (100 - p.discount_percentage) / 100) AS maxPriceNew,
                        GROUP_CONCAT(DISTINCT v.name ORDER BY v.name SEPARATOR ', ') AS capacities,
                        COALESCE(AVG(f.rating), 0) AS rating,
                        p.total_sold AS soldCount
                    FROM products p
                    LEFT JOIN product_variants v ON p.id = v.product_id
                    LEFT JOIN variant_colors vc ON v.id = vc.variant_id
                    LEFT JOIN feedback f ON p.id = f.product_id AND f.status = 1
                    WHERE p.status = 1
                    GROUP BY p.id, p.name, p.img, p.discount_percentage, p.total_sold
                    ORDER BY p.id DESC
                """;

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getInt("id"));
                    map.put("name", rs.getString("name"));
                    map.put("image", rs.getString("image"));
                    map.put("discount", rs.getInt("discount"));

                    // Giá cũ (giá gốc)
                    double priceOld = rs.getDouble("priceOld");
                    map.put("priceOld", priceOld);

                    // Giá mới (đã giảm giá)
                    double priceNew = rs.getDouble("priceNew");
                    map.put("priceNew", priceNew);

                    String capacitiesStr = rs.getString("capacities");
                    if (capacitiesStr != null && !capacitiesStr.isEmpty()) {
                        map.put("capacities", Arrays.asList(capacitiesStr.split(", ")));
                    } else {
                        map.put("capacities", new ArrayList<>());
                    }

                    map.put("rating", Math.round(rs.getDouble("rating")));
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
