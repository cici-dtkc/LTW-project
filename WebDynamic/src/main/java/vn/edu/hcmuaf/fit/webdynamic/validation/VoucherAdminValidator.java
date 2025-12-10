package vn.edu.hcmuaf.fit.webdynamic.validation;

import vn.edu.hcmuaf.fit.webdynamic.dao.VoucherAdminDao;
import vn.edu.hcmuaf.fit.webdynamic.model.VoucherAdmin;

import java.time.LocalDateTime;

public class VoucherAdminValidator {
    private VoucherAdminValidator() {}

    public static void validate(VoucherAdmin v, VoucherAdminDao dao, Integer excludeId) {

        //   Voucher code
        if (v.getVoucherCode() == null || v.getVoucherCode().isBlank()) {
            throw new ValidationException("Voucher code không được rỗng");
        }

        if (dao.isCodeExist(v.getVoucherCode(), excludeId)) {
            throw new ValidationException("Voucher code đã tồn tại");
        }

        //   Discount
        if (v.getDiscountAmount() <= 0) {
            throw new ValidationException("Discount amount phải > 0");
        }

        //   Date
        LocalDateTime start = v.getStartDate();
        LocalDateTime end = v.getEndDate();

        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc");
        }

        //   Quantity
        if (v.getQuantity() < 0) {
            throw new ValidationException("Quantity không được âm");
        }
    }
}