package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import java.util.List;
import java.util.Map;

public interface ProductDao {

    List<Product> findAllWithVariants();

}
