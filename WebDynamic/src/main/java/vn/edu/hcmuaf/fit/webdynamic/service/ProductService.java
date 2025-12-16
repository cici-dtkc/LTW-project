package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dto.ProductAdminDTO;
import vn.edu.hcmuaf.fit.webdynamic.model.Product;
import java.util.List;

public interface ProductService {


    List<ProductAdminDTO> search(String keyword, String status, String category);

    void toggleStatus(int productId);
}
