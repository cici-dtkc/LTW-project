package vn.edu.hcmuaf.fit.webdynamic.dao;

import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<Map<String, Object>> findForAdmin(
            String keyword,
            Integer status,
            Integer categoryId,
            int offset,
            int limit
    );

    int countForAdmin(String keyword, Integer status, Integer categoryId);

    boolean toggleStatus(int productId);

}
