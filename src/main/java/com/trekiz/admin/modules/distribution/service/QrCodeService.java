package com.trekiz.admin.modules.distribution.service;

import java.util.List;
import java.util.Map;

public interface QrCodeService {
	
	/**
	 * 生成二维码信息（生成url，存储待分销团期信息）
	 * @param isMulti false表示单一产品，跳转详情页; true 表示多产品，跳转列表页
	 * @return url = request path + uuid
	 * @author yang.wang
	 * */
	public String generateQrCodeInfo4Batch(List<Map<String, Object>> list, boolean isMulti);
}
