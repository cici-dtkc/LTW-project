package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.ProductDaoImpl;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;

import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    public List<Product> getAllForAdmin() {
        return productDao.findAllWithVariants();
    }

    @Override
    public List<Map<String, Object>> getProductsForList() {
        return ((ProductDaoImpl) productDao).getProductsForListDisplay();
    }

    @Override
    public List<Map<String, Object>> getProductsByCategory(int categoryId) {
        return ((ProductDaoImpl) productDao).getProductsByCategory(categoryId);
    }

}
