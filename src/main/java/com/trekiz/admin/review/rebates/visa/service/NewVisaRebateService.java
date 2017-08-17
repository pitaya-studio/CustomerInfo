/**
 * 
 */
package com.trekiz.admin.review.rebates.visa.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.review.changePrice.airticket.bean.ReviewResultBean;
import com.trekiz.admin.review.rebates.visa.bean.VisaRebateInput;


/**
 * 描述:签证返佣
 * 
 * @author zhanghao 2015-12-09
 */

public interface NewVisaRebateService {
	
	/**
	 * 查询签证返佣申请记录
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-10 下午2:27:57
	 */
	public List<Map<String, Object>> findReviewListMap(String orderId);
	
	/**
	 * 签证返佣申请发起 zhanghao
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public ReviewResultBean applyStart(VisaRebateInput input) throws Exception;
	/**
	 * 验证是否存在互斥流程 zhanghao
	 * @param input
	 * @return
	 */
	public ReviewResultBean checkReview(VisaRebateInput input);

	/**
	 * 新 签证返佣申请 审批通过后 同步更新 扩展字段3（当前申请的累计返佣金额，申请列表显示使用）
	 * add by zhanghao 20151230
	 * @param reviewMap 当前申请的业务数据MAP
	 * @param reviewId 当前申请的ID
	 */
	public void updateExtend3ByReviewSuccess(Map<String,Object> reviewMap,String reviewId) ;
	
}
