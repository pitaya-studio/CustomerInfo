package com.trekiz.admin.review.cost.common;

import java.util.List;
import java.util.Map;

public interface ICostCommonService {

	public String getLockStatus(Integer productType, Long groupId);
	
	public void updateCostVoucher(String docIds, Long costId);

	public String getCostVouchers(Long costId);

	public void deleteCostVoucher(String docId, Long costId);


	/**
	 * 查询对应团期或者产品的Quauq服务费
	 * @param orderType			产品类型
	 * @param activityId		产品或者团期ID
	 * @return
	 * @author	shijun.liu
	 * @date	2016.08.11
	 */
	public List<Map<String, String>> getQuauqServiceAmount(Integer orderType, Long activityId);

    /**
     * 查询产品或者团期的渠道服务费
     * @param orderType		产品类型
     * @param activityId	产品或者团期ID
     * @return
     * @author	shijun.liu
     * @date	2016.08.11
     */
    public List<Map<String, String>> getAgentServiceAmount(Integer orderType, Long activityId);
    
    /**
     * 查询代收服务费总额
     * @param productType
     * @param groupId
     * @return
     */
    public List<Map<String,Object>> getTotalPrice(Integer productType,Long groupId);
    
	/**
	 * 查询对应团期或者产品的Quauq服务费
	 * @param orderType			产品类型
	 * @param activityId		产品或者团期ID
	 * @return
	 * @author	chao.zhang
	 * @date	2016.09.13
	 */
	public List<Map<String, String>> getQuauqServiceAmount1(Integer orderType, Long activityId);

    /**
     * 查询产品或者团期的渠道服务费
     * @param orderType		产品类型
     * @param activityId	产品或者团期ID
     * @return
     * @author	chao.zhang
     * @date	2016.09.13
     */
    public List<Map<String, String>> getAgentServiceAmount1(Integer orderType, Long activityId);
    
}
