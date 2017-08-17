package com.trekiz.admin.modules.review.visareturndepositreceipt.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;

/**
 * 还签证押金收据 service
 * xinwei.wang 
 */
public interface IVisaReturnDepositReceiptService {

	Page<Map<String, Object>> queryVisaReturnDepositReceiptReviewInfo(HttpServletRequest request, HttpServletResponse response, String roleID);
	
}
