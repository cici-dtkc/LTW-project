package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.AddressDao;
import vn.edu.hcmuaf.fit.webdynamic.model.Address;

import java.util.List;
import java.util.Optional;

public class AddressService {
    private final AddressDao addressDao;
    public AddressService() {
        this.addressDao = new AddressDao();
    }
    // Lấy địa chỉ theo ID
    public List<Address> getAllAddressesByUserId(int userId) {
        return addressDao.findAllByUserId(userId);
    }
    // Lấy địa chỉ mặc định của user
    public Optional<Address> getAddressDefaultByUserId(int userId) {
        return addressDao.findDefaultByUserId(userId);
    }
    //Thêm địa chỉ mới.
    public int addAddress(Address address) {
        return addressDao.insert(address);
    }
    // Cập nhật địa chỉ
    public boolean updateAddress(Address address) {
        // Kiểm tra địa chỉ có tồn tại không
        Optional<Address> existing = addressDao.findById(address.getId());
        if (existing.isEmpty()) {
            return false;
        }

        // Kiểm tra địa chỉ thuộc về user này
        if (existing.get().getUserId() != address.getUserId()) {
            return false;
        }

        return addressDao.update(address);
    }
     // Xóa địa chỉ.
    public boolean deleteAddress(int id, int userId) {
        // Kiểm tra địa chỉ có tồn tại và thuộc về user này không
        Optional<Address> address = addressDao.findById(id);
        if (address.isEmpty() || address.get().getUserId() != userId) {
            return false;
        }

        return addressDao.delete(id);
    }
     //Đặt địa chỉ làm mặc định.
    public boolean setDefaultAddress(int id, int userId) {
        // Kiểm tra địa chỉ có tồn tại và thuộc về user này không
        Optional<Address> address = addressDao.findById(id);
        if (address.isEmpty() || address.get().getUserId() != userId) {
            return false;
        }

        return addressDao.setAsDefault(id, userId);
    }
}
