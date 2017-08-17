package com.trekiz.admin.modules.mtourOrder.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderDetail;
import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderMoney;
import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderParam;
import com.trekiz.admin.modules.mtourOrder.jsonbean.MtourOrderSearchJsonBean;

public interface AirticketOrderMtourDao extends BaseDao<MtourOrderDetail> {

	public Map<String,Object> getMtourOrderJsonBean(MtourOrderParam mtourOrderParam);
	
	/**
	 * 根据查询信息获取美途付款列表-订单列表数据集合
	 * @Description: 
	 * @param @param orderSearch
	 * @param @return   
	 * @return List<MtourOrderDetail>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	public Page<Map<String, Object>> getMtourOrderList(MtourOrderSearchJsonBean orderSearch);
	
	/**
	 * 根据订单id获取订单的应付金额
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return Map<String,Object>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	public List<MtourOrderMoney> getPayableAmountByOrderId(Long orderId);
	
	/**
	 * 根据订单id获取订单的已付金额
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return List<MtourOrderMoney>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	public List<MtourOrderMoney> getPaidAmountByOrderId(Long orderId);
	
	/**
	 * 根据查询信息获取美途财务中心-收款->订单列表数据集合
	 * @Description: 
	 * @param orderSearch
	 * @throws
	 * @author shijun.liu
	 * @date 2016-02-29
	 */
	public Page<Map<String, Object>> getMtourOrderListForReceive(MtourOrderSearchJsonBean orderSearch);
}
