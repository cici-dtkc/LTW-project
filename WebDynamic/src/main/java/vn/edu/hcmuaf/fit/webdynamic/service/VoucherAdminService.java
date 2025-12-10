package vn.edu.hcmuaf.fit.webdynamic.service;

import jakarta.servlet.http.HttpServletRequest;
import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;

import java.util.List;

public interface VoucherAdminService {
    List<VoucherAdmin> getAll(String keyword, Integer status, int page, int limit);

    int countAll(String keyword, Integer status);

    VoucherAdmin getById(int id);

    void createVoucher(VoucherAdmin voucher);

    void updateVoucher(VoucherAdmin voucher);

    void deleteVoucher(int id);

    void toggleStatus(int id);


}
