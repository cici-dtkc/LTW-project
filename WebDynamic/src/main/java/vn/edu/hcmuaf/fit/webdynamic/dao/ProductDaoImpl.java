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
                    return mapResult(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> search(String keyword, Integer status, Integer categoryId) {
        return List.of();
    }

    private Product mapResult(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setImg(rs.getString("img"));
        p.setDescription(rs.getString("description"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setBrandId((Integer) rs.getObject("brand_id"));
        p.setStatus(rs.getInt("status"));
        return p;
    }
}


