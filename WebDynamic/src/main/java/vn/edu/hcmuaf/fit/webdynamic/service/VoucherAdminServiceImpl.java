package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;
import vn.edu.hcmuaf.fit.webdynamic.dao.VoucherAdminDao;
import vn.edu.hcmuaf.fit.webdynamic.validation.VoucherAdminValidator;

import java.time.LocalDateTime;
import java.util.List;

public class VoucherAdminServiceImpl implements VoucherAdminService {

    private final VoucherAdminDao dao;

    public VoucherAdminServiceImpl(VoucherAdminDao dao) {
        this.dao = dao;
    }

    @Override
    public List<VoucherAdmin> getAll(String keyword, Integer status, int page, int limit) {
        int offset = (page - 1) * limit;
        return dao.getAll(keyword, status, offset, limit);
    }

    @Override
    public int countAll(String keyword, Integer status) {
        return dao.countAll(keyword, status);
    }

    @Override
    public VoucherAdmin getById(int id) {
        return dao.getById(id);
    }

    @Override
    public void createVoucher(VoucherAdmin v) {
        VoucherAdminValidator.validate(v, dao, null);
        dao.insert(v);
    }

    @Override
    public void updateVoucher(VoucherAdmin v) {
        VoucherAdminValidator.validate(v, dao, v.getId());
        dao.update(v);
    }
    @Override
    public void deleteVoucher(int id) {
        dao.delete(id);
    }

    @Override
    public void toggleStatus(int id) {
        dao.toggleStatus(id);
    }


}
