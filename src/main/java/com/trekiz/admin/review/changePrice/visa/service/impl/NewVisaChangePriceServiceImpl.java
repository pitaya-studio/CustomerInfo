package com.trekiz.admin.review.changePrice.visa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.review.changePrice.visa.service.INewVisaChangePriceService;

@Service
public class NewVisaChangePriceServiceImpl implements INewVisaChangePriceService{
	@Autowired
	private ReviewDao reviewDao;
	
	@Override
	public List<Object> getDeptIdByOrderId(String orderId) {
		String sql = "SELECT vp.deptId from visa_order vo LEFT JOIN visa_products vp ON vo.visa_product_id=vp.id WHERE vo.id=?";
		return reviewDao.findBySql(sql, orderId);
	}

}
