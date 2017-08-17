package com.trekiz.admin.modules.visa.repository;

import java.util.List;
import java.util.Map;

public interface IVisaOrderListDao {

	List<Map<String, Object>> queryVisaOrdersByProductId(String productId);
}
