package vn.edu.hcmuaf.fit.webdynamic.dao;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.DBConnect;
import vn.edu.hcmuaf.fit.webdynamic.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

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
            col.id AS color_id, col.name AS color_name
        FROM products p
        JOIN categories c ON p.category_id = c.id
        JOIN product_variants v ON p.id = v.product_id
         JOIN variant_colors vc ON v.id = vc.variant_id
          JOIN colors col ON vc.color_id = col.id
        WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
          AND (:status IS NULL OR v.status = :status)
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
        SELECT COUNT(DISTINCT v.id)
        FROM product_variants v
        JOIN products p ON v.product_id = p.id
        WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
          AND (:status IS NULL OR v.status = :status)
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
    public boolean toggleStatus(int productId) {

        String sql = """
        UPDATE product_variants
        SET status = CASE WHEN status = 1 THEN 0 ELSE 1 END
        WHERE id = :id
    """;

        return jdbi.withHandle(h ->
                h.createUpdate(sql)
                        .bind("id", productId)
                        .execute() > 0
        );}

    @Override
    public int insertProduct(Product p) {
        String sql = """
        INSERT INTO products(name, img, category_id, description, created_at)
        VALUES (:name, :img, :categoryId, :description, :createdAt)
    """;

        return jdbi.withHandle(h ->
                h.createUpdate(sql)
                        .bind("name", p.getName())
                        .bind("img", p.getMainImage())
                        .bind("categoryId", p.getCategory().getId())
                        .bind("description", p.getDescription())
                        .bind("createdAt", LocalDateTime.now())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );

    }

    @Override
    public int insertVariant(int productId, ProductVariant v) {

        String sql = """
        INSERT INTO product_variants
        (product_id, name, base_price, status, created_at, updated_at)
        VALUES (:productId, :name, :basePrice, :status, :createdAt, :updatedAt)
    """;

        return jdbi.withHandle(h ->
                h.createUpdate(sql)
                        .bind("productId", productId)
                        .bind("name", v.getName())
                        .bind("basePrice", v.getBasePrice())
                        .bind("status", v.getStatus()) // thường = 1
                        .bind("createdAt", LocalDateTime.now())
                        .bind("updatedAt", LocalDateTime.now())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    @Override
    public void insertVariantColor(VariantColor c) {

        String sql = """
            INSERT INTO variant_colors
            (variant_id, color_id, price, quantity, created_at)
            VALUES (:variantId, :colorId, :price, :quantity, :createdAt)
        """;

        jdbi.useHandle(h -> {

            int variantColorId = h.createUpdate(sql)
                    .bind("variantId", c.getId())
                    .bind("colorId", c.getColor().getId())
                    .bind("price", c.getPrice())
                    .bind("quantity", c.getQuantity())
                    .bind("createdAt", LocalDateTime.now())
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();

            // NOTE insert từng image riêng
            if (c.getImages() != null) {
                for (Image img : c.getImages()) {
                    insertVariantColorImage(h, variantColorId, img);
                }
            }
        } );
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
    public void insertTechSpec(int productId, TechSpecs t) {
        String sql = """
        INSERT INTO  tech_specs
        (product_id, name, value, priority)
        VALUES (:productId, :name, :value, :priority)
    """;

        jdbi.useHandle(h ->
                h.createUpdate(sql)
                        .bind("productId", productId)
                        .bind("name", t.getName())
                        .bind("value", t.getValue())
                        .bind("priority", t.getPriority())
                        .execute()
        );
    }


}


