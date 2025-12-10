package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;

import java.util.List;

public interface VoucherAdminDao {
    List<VoucherAdmin> getAll(String keyword, Integer status, int offset, int limit);

    int countAll(String keyword, Integer status);

    VoucherAdmin getById(int id);

    boolean isCodeExist(String voucherCode, Integer excludeId);

    boolean insert(VoucherAdmin voucher);

    boolean update(VoucherAdmin voucher);

    boolean delete(int id);

    boolean toggleStatus(int id);
}
