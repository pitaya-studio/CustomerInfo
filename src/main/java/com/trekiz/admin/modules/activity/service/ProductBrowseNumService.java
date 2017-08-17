package com.trekiz.admin.modules.activity.service;

import com.trekiz.admin.modules.activity.entity.ProductBrowseNum;

public interface ProductBrowseNumService {
	
	public void updateBrowseNum(com.trekiz.admin.modules.activity.entity.ProductBrowseNum browseNum);

	public ProductBrowseNum getProductBrowseNumById(Long productId);
	
	public void saveProductBrowseNum(ProductBrowseNum browseNum);
	
	public void updateBrowseNumByProductId(Long productId);
}
