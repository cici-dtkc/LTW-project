package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDaoImpl;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    public List<Product> getAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> search(String keyword, String status, String category) {

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
        // Lấy sản phẩm  đảo status
        List<Product> list = productDao.search(null, null, null);
        for (Product p : list) {
            if (p.getId() == productId) {
                int newStatus = (p.getStatus() == 1) ? 0 : 1;
                productDao.updateStatus(productId, newStatus);
                break;
            }
        }
    }
}
