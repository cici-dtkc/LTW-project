package vn.edu.hcmuaf.fit.webdynamic.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.webdynamic.config.JDBIConnector;
import vn.edu.hcmuaf.fit.webdynamic.model.Voucher ;

import java.util.List;

public class VoucherDAO {
    private final Jdbi jdbi = JDBIConnector.get();

    // 1️ Lấy tất cả khuyến mãi
    public List<Voucher> getAll() {
        String sql = """
            SELECT voucher_code, discount_amount, type, max_reduce, 
                   min_order_value, quantity, start_date, end_date, status 
            FROM vouchers
            ORDER BY start_date DESC
        """;
        return jdbi.withHandle(h -> h.createQuery(sql).mapToBean(Voucher.class).list());
    }

    // 2️ Tìm kiếm theo code hoặc discount_amount
    public List<Voucher> search(String keyword) {
        String sql = """
            SELECT * FROM vouchers
            WHERE voucher_code LIKE CONCAT('%',:kw,'%')
               OR discount_amount LIKE CONCAT('%',:kw,'%')
        """;

        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind("kw", keyword)
                        .mapToBean(Voucher.class)
                        .list()
        );
    }

    // 3️ Lấy voucher đang áp dụng
    public List<Voucher> getActiveNow() {
        String sql = """
            SELECT * FROM vouchers
            WHERE status = 1 
              AND start_date <= NOW() 
              AND end_date >= NOW()
        """;
        return jdbi.withHandle(h -> h.createQuery(sql).mapToBean(Voucher.class).list());
    }

    // 4️ Lấy voucher chưa áp dụng hoặc hết hạn
    public List<Voucher> getInactiveOrExpired() {
        String sql = """
            SELECT * FROM vouchers
            WHERE status = 0 
               OR end_date < NOW() 
               OR start_date > NOW()
        """;
        return jdbi.withHandle(h -> h.createQuery(sql).mapToBean(Voucher.class).list());
    }

    // 5️ Thêm voucher mới
    public void insert(Voucher v) {
        String sql = """
            INSERT INTO vouchers (voucher_code, discount_amount, type, status,
                min_order_value, max_reduce, quantity, start_date, end_date, created_at)
            VALUES (:code, :discount, :type, :status, :min, :max, :qty, :start, :end, NOW())
        """;
        jdbi.useHandle(h ->
                h.createUpdate(sql)
                        .bind("code", v.getVoucherCode())
                        .bind("discount", v.getDiscountAmount())
                        .bind("type", v.getType())
                        .bind("status", v.getStatus())
                        .bind("min", v.getMinOrderValue())
                        .bind("max", v.getMaxReduce())
                        .bind("qty", v.getQuantity())
                        .bind("start", v.getStartDate())
                        .bind("end", v.getEndDate())
                        .execute()
        );
    }

    // 6️ Chỉnh sửa voucher theo ID
    public void update(Voucher v) {
        String sql = """
            UPDATE vouchers 
            SET discount_amount = :discount,
                type = :type,
                max_reduce = :max,
                min_order_value = :min,
                quantity = :qty,
                start_date = :start,
                end_date = :end,
                updated_at = NOW()
            WHERE id = :id
        """;
        jdbi.useHandle(h ->
                h.createUpdate(sql)
                        .bind("discount", v.getDiscountAmount())
                        .bind("type", v.getType())
                        .bind("max", v.getMaxReduce())
                        .bind("min", v.getMinOrderValue())
                        .bind("qty", v.getQuantity())
                        .bind("start", v.getStartDate())
                        .bind("end", v.getEndDate())
                        .bind("id", v.getId())
                        .execute()
        );
    }

    // 7️ Cập nhật trạng thái
    public void updateStatus(int id, int newStatus) {
        String sql = """
            UPDATE vouchers
            SET status = :status, updated_at = NOW()
            WHERE id = :id
        """;
        jdbi.useHandle(h ->
                h.createUpdate(sql)
                        .bind("status", newStatus)
                        .bind("id", id)
                        .execute()
        );
    }

    // 8 Lấy dữ liệu có phân trang
    public List<Voucher> getPage(int limit, int offset) {
        String sql = """
            SELECT * FROM vouchers
            ORDER BY created_at DESC
            LIMIT :limit OFFSET :offset
        """;
        return jdbi.withHandle(h ->
                h.createQuery(sql)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Voucher.class)
                        .list()
        );
    }

    // Đếm tổng số voucher (dùng cho pagination)
    public int countAll() {
        return jdbi.withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM vouchers")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<Voucher> search(String keyword, int status, int limit, int offset) {
        StringBuilder sql = new StringBuilder("SELECT * FROM vouchers WHERE 1=1 ");
        boolean hasKeyword = keyword != null && !keyword.isEmpty();

        if (hasKeyword) {
            sql.append("AND voucher_code LIKE CONCAT('%', :keyword, '%') ");
        }
        if (status != -1) {
            sql.append("AND status = :status ");
        }

        sql.append("ORDER BY id DESC LIMIT :limit OFFSET :offset");

        return jdbi.withHandle(h -> {
            var q = h.createQuery(sql.toString());

            if (hasKeyword) q.bind("keyword", keyword);
            if (status != -1) q.bind("status", status);

            q.bind("limit", limit);
            q.bind("offset", offset);

            return q.mapToBean(Voucher.class).list();
        });
    }

    public int countSearch(String keyword, int status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM vouchers WHERE 1=1 ");
        boolean hasKeyword = keyword != null && !keyword.isEmpty();

        if (hasKeyword) {
            sql.append("AND voucher_code LIKE CONCAT('%', :keyword, '%') ");
        }
        if (status != -1) {
            sql.append("AND status = :status ");
        }

        return jdbi.withHandle(h -> {
            var q = h.createQuery(sql.toString());

            if (hasKeyword) q.bind("keyword", keyword);
            if (status != -1) q.bind("status", status);

            return q.mapTo(Integer.class).one();
        });
    }

}
