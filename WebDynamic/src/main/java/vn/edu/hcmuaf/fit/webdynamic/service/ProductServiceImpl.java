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

    @Override
    public List<Map<String, Object>> getProductsByCategoryWithFilters(
            int categoryId,
            Double priceMin,
            Double priceMax,
            List<String> memory,
            List<String> colors,
            Integer year,
            Integer brandId,
            List<String> types,
            String condition,
            String sortBy
    ) {
        return ((ProductDaoImpl) productDao).getProductsByCategoryWithFilters(
                categoryId, priceMin, priceMax, memory, colors, year, brandId, types, condition, sortBy
        );
    }

}
