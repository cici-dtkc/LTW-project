package vn.edu.hcmuaf.fit.webdynamic.dao;

import java.util.List;
import java.util.Map;

public interface HomeDao {
    List<Map<String, Object>> getFeaturedProducts();

    List<Map<String, Object>> getFeaturedAccessories();

    List<Map<String, Object>> getActiveVouchers();
}