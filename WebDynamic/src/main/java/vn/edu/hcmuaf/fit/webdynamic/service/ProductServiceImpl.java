package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDaoImpl;
import vn.edu.hcmuaf.fit.webdynamic.dto.ProductAdminDTO;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao = new ProductDaoImpl();


    @Override
    public List<ProductAdminDTO> search(String keyword, String status, String category) {

        Integer st = (status == null || status.isEmpty())
                ? null : Integer.parseInt(status);

        Integer cate = null;
        if (category != null && !category.isEmpty()) {
            cate = category.equals("PHONE") ? 1 : 2;
        }

        return productDao.search(keyword, st, cate);
    }

    @Override
    public void toggleStatus(int productId) {
        // Lấy 1 record để đảo trạng thái
        List<ProductAdminDTO> list = productDao.search(null, null, null);
        for (ProductAdminDTO dto : list) {
            if (dto.getProductId() == productId) {
                productDao.updateStatus(productId, dto.isActive() ? 0 : 1);
                break;
            }
        }
    }

}
