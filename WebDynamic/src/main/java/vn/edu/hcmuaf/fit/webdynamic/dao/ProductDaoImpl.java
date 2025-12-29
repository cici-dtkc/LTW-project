package vn.edu.hcmuaf.fit.webdynamic.dao;
import org.jdbi.v3.core.Handle;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalTime.now;

public class ProductDaoImpl implements ProductDao {
    private final Jdbi jdbi = DBConnect.getJdbi();

    @Override
    public List<Map<String, Object>> findForAdmin(
            String keyword,
            Integer status,
            Integer categoryId,
            int offset,
            int limit) {

        String sql = """
   SELECT
   p.id AS p_id, p.name AS p_name, p.img AS p_img,
    c.id AS c_id, c.name AS c_name,
   v.id AS v_id, v.name AS v_name, v.base_price, v.status AS v_status,
    vc.id AS vc_id, vc.price AS vc_price, vc.quantity,
   vc.status AS vc_status,
   col.id AS color_id, col.name AS color_name,
     vc.sku AS vc_sku
     FROM products p
    JOIN categories c ON p.category_id = c.id
    JOIN product_variants v ON p.id = v.product_id
    JOIN variant_colors vc ON v.id = vc.variant_id
    JOIN colors col ON vc.color_id = col.id
    WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
      AND (:status IS NULL OR vc.status = :status)
      AND (:categoryId IS NULL OR p.category_id = :categoryId)
    ORDER BY p.id DESC
    LIMIT :limit OFFSET :offset
""";

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind("keyword", keyword)
                        .bind("status", status)
                        .bind("categoryId", categoryId)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToMap()
                        .list()
        );
    }

    @Override
    public int countForAdmin(String keyword, Integer status, Integer categoryId) {

        String sql = """
            SELECT COUNT(vc.id)
            FROM variant_colors vc
            JOIN product_variants v ON vc.variant_id = v.id
            JOIN products p ON v.product_id = p.id
            WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
              AND (:status IS NULL OR vc.status = :status)
              AND (:categoryId IS NULL OR p.category_id = :categoryId)
        """;

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind("keyword", keyword)
                        .bind("status", status)
                        .bind("categoryId", categoryId)
                        .mapTo(Integer.class)
                        .one()
        );
    }
    @Override
    public boolean toggleStatus(int variantColorId) {
        String sql = """
        UPDATE variant_colors
        SET status = CASE WHEN status = 1 THEN 0 ELSE 1 END
        WHERE id = :id
    """;

        return jdbi.withHandle(h -> {
            int rows = h.createUpdate(sql)
                    .bind("id", variantColorId)
                    .execute();
            return rows > 0;
        });
    }

    @Override
    public int insertProduct(Handle h, Product p) {
        String sql = """
        INSERT INTO products(name, img, category_id, description,warranty_period, release_date, created_at)
        VALUES (:name, :img, :categoryId, :description, :warranty, :releaseDate,:createdAt)
    """;

        return
                h.createUpdate(sql)
                        .bind("name", p.getName())
                        .bind("img", p.getMainImage())
                        .bind("categoryId", p.getCategory().getId())
                        .bind("description", p.getDescription())
                        .bind("warranty", 12)           // Thiết lập bảo hành cứng 12 tháng (1 năm)
                        .bind("releaseDate", LocalDateTime.now())       // Thiết lập ngày ra mắt là lúc thêm sản phẩm
                        .bind("createdAt", LocalDateTime.now())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one();


    }

    @Override
    public int insertVariant(Handle h, int productId, ProductVariant v) {

        String sql = """
        INSERT INTO product_variants
        (product_id, name, base_price, status, created_at, updated_at)
        VALUES (:productId, :name, :basePrice, :status, :createdAt, :updatedAt)
    """;
            return    h.createUpdate(sql)
                        .bind("productId", productId)
                        .bind("name", v.getName())
                        .bind("basePrice", v.getBasePrice())
                        .bind("status", v.getStatus()) // thường = 1
                        .bind("createdAt", LocalDateTime.now())
                        .bind("updatedAt", LocalDateTime.now())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one();
    }
    @Override
    public int insertVariantColor(Handle h, int variantId, VariantColor c) {

        String sql = """
        INSERT INTO variant_colors (variant_id, color_id, price, quantity, status, sku, created_at)
        VALUES (:variantId, :colorId, :price, :quantity, 1, :sku, :createdAt)
    """;

        int variantColorId = h.createUpdate(sql)
                .bind("variantId", variantId)
                .bind("colorId", c.getColor().getId())
                .bind("price", c.getPrice())
                .bind("quantity", c.getQuantity())
                .bind("sku", c.getSku())
                .bind("createdAt", LocalDateTime.now())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Integer.class)
                .one();

        // insert images (nếu có)
        if (c.getImages() != null) {
            for (Image img : c.getImages()) {
                insertVariantColorImage(h, variantColorId, img);
            }
        }

        return variantColorId;
    }


    //  IMAGE
    private void insertVariantColorImage(Handle h, int variantColorId, Image img) {

        String sql = """
            INSERT INTO Images
            (variant_color_id, img_path, created_at)
            VALUES (:vcId, :path, :createdAt)
        """;

        h.createUpdate(sql)
                .bind("vcId", variantColorId)
                .bind("path", img.getImgPath())
                .bind("createdAt", LocalDateTime.now())
                .execute();
    }




    @Override
    public void insertTechSpec(Handle h, int productId, TechSpecs t) {
        String sql = """
        INSERT INTO  tech_specs
        (product_id, name, value, priority)
        VALUES (:productId, :name, :value, :priority)
    """;


                h.createUpdate(sql)
                        .bind("productId", productId)
                        .bind("name", t.getName())
                        .bind("value", t.getValue())
                        .bind("priority", t.getPriority())
                        .execute();
    }

    @Override
    public List<Map<String, Object>> findForAdminByVariantIds(List<Integer> variantIds) {

        if (variantIds.isEmpty()) return List.of();
        String sql = """
            SELECT
                p.id   AS p_id,   p.name AS p_name, p.img AS p_img,p.category_id  AS category_id,
                c.id   AS c_id,   c.name AS c_name,
                v.id   AS v_id,   v.name AS v_name,
                v.base_price,    vc.status AS vc_status,
                vc.id  AS vc_id,  vc.price AS vc_price, vc.quantity,
                col.id AS color_id, col.name AS color_name
            FROM variant_colors vc
            JOIN product_variants v ON vc.variant_id = v.id
            JOIN products p ON v.product_id = p.id
            JOIN categories c ON p.category_id = c.id
            JOIN colors col ON vc.color_id = col.id
            WHERE vc.id IN (<ids>)
            ORDER BY vc.id DESC
        """;

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bindList("ids", variantIds)
                        .mapToMap()
                        .list()
        );
    }

    @Override
    public List<Integer> findVariantIdsForAdmin(String keyword, Integer status, Integer categoryId, int offset, int limit) {


            String sql = """
            SELECT vc.id
            FROM variant_colors vc
            JOIN product_variants v ON vc.variant_id = v.id
            JOIN products p ON v.product_id = p.id
            WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
              AND (:status IS NULL OR vc.status = :status)
              AND (:categoryId IS NULL OR p.category_id = :categoryId)
            ORDER BY vc.id DESC
            LIMIT :limit OFFSET :offset
        """;

            return jdbi.withHandle(h ->
                    h.createQuery(sql)
                            .bind("keyword", keyword)
                            .bind("status", status)
                            .bind("categoryId", categoryId)
                            .bind("limit", limit)
                            .bind("offset", offset)
                            .mapTo(Integer.class)
                            .list()
            );
        }


        // edit
        public Map<String, Object> findProductByVariantColorId(int vcId) {

            String sql = """
        SELECT
            p.id           AS product_id,
            p.name         AS product_name,
            p.img          AS product_img,
            p.category_id  AS category_id,
            p.description  AS description
        FROM variant_colors vc
        JOIN product_variants v ON vc.variant_id = v.id
        JOIN products p ON v.product_id = p.id
        WHERE vc.id = :vcId
    """;

            return jdbi.withHandle(h ->
                    h.createQuery(sql)
                            .bind("vcId", vcId)
                            .mapToMap()
                            .one()
            );
        }
    public List<Map<String, Object>> findVariantsByProductId(int productId) {
        String sql = """
        SELECT
            v.id          AS variant_id,
            v.product_id  AS product_id,
            v.name        AS variant_name,
            v.base_price  AS base_price,
            v.status      AS variant_status
        FROM product_variants v
        WHERE v.product_id = :pid
        ORDER BY v.id
    """;

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind("pid", productId)
                        .mapToMap()
                        .list()
        );
    }
    public List<Map<String, Object>> findColorsByVariantId(int variantId) {

        String sql = """
        SELECT
            vc.id        AS vc_id,
            vc.variant_id,
            vc.color_id,
            col.name     AS color_name,
            vc.price     AS color_price,
            vc.quantity,
            vc.status    AS vc_status,
            vc.sku
        FROM variant_colors vc
        JOIN colors col ON vc.color_id = col.id
        WHERE vc.variant_id = :vid
        ORDER BY vc.id
    """;

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind("vid", variantId)
                        .mapToMap()
                        .list()
        );
    }
    public List<Map<String, Object>> findTechByProductId(int productId) {
        String sql = """
        SELECT
            t.id          AS tech_id,
            t.product_id,
            t.name        AS tech_name,
            t.value       AS tech_value,
            t.priority
        FROM tech_specs t
        WHERE t.product_id = :pid
        ORDER BY t.priority
    """;

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind("pid", productId)
                        .mapToMap()
                        .list()
        );
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
