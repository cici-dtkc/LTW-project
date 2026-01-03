package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.model.*;

import java.util.List;

public class VariantDao {

    private final Jdbi jdbi;

    public VariantDao(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    // 5️⃣ Màu theo Variant (nút chọn màu)
    public List<VariantColor> getColorsByVariant(int variantId) {
        String sql = """
            SELECT vc.*,
                   c.id AS c_id, c.name AS c_name, c.color_code
            FROM variant_colors vc
            JOIN colors c ON vc.color_id = c.id
            WHERE vc.variant_id = ?
        """;

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind(0, variantId)
                        .map((rs, ctx) -> {
                            VariantColor vc = new VariantColor();
                            vc.setId(rs.getInt("id"));
                            vc.setPrice(rs.getDouble("price"));
                            vc.setQuantity(rs.getInt("quantity"));
                            vc.setSku(rs.getString("sku"));
                            vc.setStatus(rs.getInt("status"));

                            Color c = new Color();
                            c.setId(rs.getInt("c_id"));
                            c.setName(rs.getString("c_name"));
                            c.setColorCode(rs.getString("color_code"));
                            vc.setColor(c);

                            return vc;
                        })
                        .list()
        );
    }

    // 6️⃣ Chi tiết Variant + Color (giá, tồn kho)
    public VariantColor getVariantColorDetail(int variantId, int colorId) {
        String sql = """
            SELECT vc.*,
                   c.id AS c_id, c.name AS c_name, c.color_code
            FROM variant_colors vc
            JOIN colors c ON vc.color_id = c.id
            WHERE vc.variant_id = ? AND vc.color_id = ?
        """;

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind(0, variantId)
                        .bind(1, colorId)
                        .map((rs, ctx) -> {
                            VariantColor vc = new VariantColor();
                            vc.setId(rs.getInt("id"));
                            vc.setPrice(rs.getDouble("price"));
                            vc.setQuantity(rs.getInt("quantity"));
                            vc.setSku(rs.getString("sku"));

                            Color c = new Color();
                            c.setId(rs.getInt("c_id"));
                            c.setName(rs.getString("c_name"));
                            c.setColorCode(rs.getString("color_code"));
                            vc.setColor(c);

                            return vc;
                        })
                        .findOne()
                        .orElse(null)
        );
    }

    // 7️⃣ Ảnh theo VariantColor (gallery)
    public List<Image> getImagesByVariantColor(int variantColorId) {
        String sql = """
            SELECT *
            FROM images
            WHERE variant_color_id = ?
            ORDER BY is_main DESC
        """;

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind(0, variantColorId)
                        .mapToBean(Image.class)
                        .list()
        );
    }
}
