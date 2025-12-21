package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.AddressDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.PaymentTypesDao;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;
import vn.edu.hcmuaf.fit.webdynamic.model.PaymentTypes;

import java.util.List;

public class paymentTypesService {
    public final AddressDao addressDao = new AddressDao();
    private final PaymentTypesDao paymentTypeDao = new PaymentTypesDao();

    public List<Address> getUserAddresses(int userId) {
        return addressDao.findAllByUserId(userId);
    }
    public List<PaymentTypes> getPaymentTypes() {
        return paymentTypeDao.findAll();
    }
}
