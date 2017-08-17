/**
 * 
 */
package com.trekiz.admin.review.changePrice.airticket.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.review.changePrice.airticket.bean.AirticketChangePriceInput;
import com.trekiz.admin.review.changePrice.airticket.bean.ReviewResultBean;


/**
 * 描述:改价
 * 
 * @author zhanghao 2015-12-03 
 */

public interface NewAirticketChangePriceService {
	
	/**
	 * 查询机票改价申请记录
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> findReviewListMap(AirticketChangePriceInput input);
	
	/**
	 * 机票改价申请发起
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public ReviewResultBean changePriceProStart(AirticketChangePriceInput input) throws Exception;
	
	/**
	 * 改价申请取消
	 * @param input
	 * @return
	 */
	public ReviewResultBean cancel(AirticketChangePriceInput input); 
	
	/**
	 * 改价申请详情
	 * @param input
	 * @return
	 */
	public Map<String, Object> getReviewDetailMapByReviewId(AirticketChangePriceInput input);
	
	/**
	 * 验证是否存在互斥流程
	 * @param input
	 * @return
	 */
	public ReviewResultBean checkReview(AirticketChangePriceInput input);
	
}
