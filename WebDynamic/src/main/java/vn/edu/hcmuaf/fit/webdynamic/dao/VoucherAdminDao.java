package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;

import java.util.List;

public interface VoucherAdminDao {
    // Lấy danh sách voucher với bộ lọc và phân trang
    List<VoucherAdmin> getAll(String keyword, Integer status, int offset, int limit);
// Đếm tổng số voucher với bộ lọc
    int countAll(String keyword, Integer status);
// Lấy voucher theo ID
    VoucherAdmin getById(int id);
// Kiểm tra mã voucher đã tồn tại (ngoại trừ ID được chỉ định)
    boolean isCodeExist(String voucherCode, Integer excludeId);
// Thêm voucher mới
    boolean insert(VoucherAdmin voucher);
// Cập nhật thông tin voucher
    boolean update(VoucherAdmin voucher);
// Xóa voucher theo ID
    boolean delete(int id);
// Chuyển đổi trạng thái hoạt động của voucher
    boolean toggleStatus(int id);
// Lấy danh sách voucher đang hoạt động
    List<VoucherAdmin> getActiveVouchers();
    // Lấy voucher theo mã
    VoucherAdmin getByCode(String code);
}
