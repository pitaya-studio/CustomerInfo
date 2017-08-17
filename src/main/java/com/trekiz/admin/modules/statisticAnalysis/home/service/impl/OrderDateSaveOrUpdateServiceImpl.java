/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.home.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.visa.entity.VisaOrder;

/**
 * @author junhao.zhao
 * 2017年1月9日  上午10:12:49
 */
@Service
public class OrderDateSaveOrUpdateServiceImpl implements OrderDateSaveOrUpdateService{
	
	@Autowired
	private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;
	
	/**为了向表order_data_statistics中添加数据，需要根据visaOrderId从四张表查询UUID()、product_name、company_uuid、amount
	 * @param visaOrder
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService#insertVisaOrder(com.trekiz.admin.modules.visa.entity.VisaOrder)
	 */
	public void insertVisaOrder(VisaOrder visaOrder){
		Long visaOrderId = visaOrder.getId();
		OrderDataStatistics ods = new OrderDataStatistics();
						
		ods.setUuid(UUID.randomUUID().toString());
		
		//数据库不能为空值，初始化数据
		
		if(visaOrder.getId()!=null && StringUtils.isNotBlank(visaOrder.getId().toString())){
			ods.setOrderId(visaOrder.getId());
		}else{
			ods.setOrderId(Long.valueOf(0));
		}
		
		if(visaOrder.getVisaProductId()!=null && StringUtils.isNotBlank(visaOrder.getVisaProductId().toString())){
			ods.setProductId(visaOrder.getVisaProductId());
		}else{
			ods.setProductId(Long.valueOf(0));
		}
		
		if(visaOrder.getProductTypeID()!=null && StringUtils.isNotBlank(visaOrder.getProductTypeID().toString())){
			ods.setOrderType(Integer.valueOf(visaOrder.getProductTypeID().toString()));
		}else{
			ods.setOrderType(0);
		}
		
		if(visaOrder.getTotalMoney()!=null && StringUtils.isNotBlank(visaOrder.getTotalMoney().toString())){
			ods.setAmountUuid(visaOrder.getTotalMoney());
		}else{
			ods.setAmountUuid("");
		}
		
		if(visaOrder.getCreateDate()!=null && StringUtils.isNotBlank(visaOrder.getCreateDate().toString())){
			ods.setOrderCreatetime(visaOrder.getCreateDate());
		}else{
			ods.setOrderCreatetime(new Date());
		}
		
		if(visaOrder.getAgentinfoId()!=null && StringUtils.isNotBlank(visaOrder.getAgentinfoId().toString())){
			ods.setAgentinfoId(visaOrder.getAgentinfoId());
		}else{
			ods.setAgentinfoId(Long.valueOf(0));
		}
		
		if(visaOrder.getTravelNum()!=null && StringUtils.isNotBlank(visaOrder.getTravelNum().toString())){
			ods.setOrderPersonNum(visaOrder.getTravelNum());
		}else{
			ods.setOrderPersonNum(0);
		}
		
		
		if(visaOrder.getAgentinfoName()!=null && StringUtils.isNotBlank(visaOrder.getAgentinfoName().toString())){
			ods.setAgentinfoName(visaOrder.getAgentinfoName());
		}else{
			ods.setAgentinfoName("");
		}
		
		if(visaOrder.getSalerId()!=null && StringUtils.isNotBlank(visaOrder.getSalerId().toString())){
			ods.setSalerId(visaOrder.getSalerId());
		}else{
			ods.setSalerId(0);
		}
		
		if(visaOrder.getSalerName()!=null && StringUtils.isNotBlank(visaOrder.getSalerName().toString())){
			ods.setSalerName(visaOrder.getSalerName());
		}else{
			ods.setSalerName("");
		}
		
		ods.setCreateDate(new Date());
		
		if(visaOrder.getVisaOrderStatus()!=null && StringUtils.isNotBlank(visaOrder.getVisaOrderStatus().toString())){
			ods.setOrderStatus(visaOrder.getVisaOrderStatus());
		}else{
			ods.setOrderStatus(0);
		}
		
		if(visaOrder.getDelFlag()!=null && StringUtils.isNotBlank(visaOrder.getDelFlag().toString())){
			ods.setDelFlag(visaOrder.getDelFlag());
		}else{
			ods.setDelFlag("");
		}
			
		//从四张表查询UUID()、product_name、company_uuid、amount
		List<Map<String, Object>> addDate = orderDateSaveOrUpdateDao.getVisaAddDate(visaOrderId);
		for(Map<String,Object> mapKey : addDate){
					
			if(mapKey.get("UUID()")!= null && StringUtils.isNotBlank(mapKey.get("UUID()").toString())){
				ods.setUuid(mapKey.get("UUID()").toString());
			}
			
			if(mapKey.get("product_name")!= null && StringUtils.isNotBlank(mapKey.get("product_name").toString())){
				ods.setProductName(mapKey.get("product_name").toString());
			}else{
				ods.setProductName("");
			}
			
			if(mapKey.get("company_uuid")!= null && StringUtils.isNotBlank(mapKey.get("company_uuid").toString())){
				ods.setCompanyUuid(mapKey.get("company_uuid").toString());
			}else{
				ods.setCompanyUuid("");
			}
			
			if(mapKey.get("amount")!= null && StringUtils.isNotBlank(mapKey.get("amount").toString())){
				ods.setAmount(new BigDecimal(mapKey.get("amount").toString()));
			}else{
				ods.setAmount(new BigDecimal("0"));
			}
		}
				
		orderDateSaveOrUpdateDao.saveObj(ods);
	}

	/**visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额、支付状态修改
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService#updatePeopleAndMoney(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public int updatePeopleAndMoney(Long orderId, Integer orderType) {
		
		int updatePeopleAndMoney = orderDateSaveOrUpdateDao.updatePeopleAndMoney(orderId,orderType);
		return updatePeopleAndMoney;
	}

	/**productorder订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService#updatePeopleAndMoneyPro(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public int updatePeopleAndMoneyPro(Long orderId, Integer orderType) {
		
		int updatePeopleAndMoneyPro = orderDateSaveOrUpdateDao.updatePeopleAndMoneyPro(orderId,orderType);
		return updatePeopleAndMoneyPro;
	}
	
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService#updateDelFlag(java.lang.Long, java.lang.Integer)
	 * OrderDateSaveOrUpdateService updateDelFlag
	 * 
	 * visa_order订单删除之后，修改表order_data_statistics中对应的订单状态
	 * 
	 * 2017年3月23日  下午3:27:12 
	 */
	@Override
	public void updateDelFlag(Long id, Integer orderTypeQz) {
		orderDateSaveOrUpdateDao.updateDelFlag(id, orderTypeQz);
	}
	
	/**根据order_id与order_type从表order_data_statistics中查询是否有对应的id，针对visa_order
	 * @param visaOrderId
	 * @param orderType
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService#getOrderDataStaIdForVisa(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> getOrderDataStaIdForVisa(Long visaOrderId, Integer orderType) {

		List<Map<String, Object>> orderDataStaIdForVisa = orderDateSaveOrUpdateDao.getOrderDataStaIdForVisa(visaOrderId, orderType);
		return orderDataStaIdForVisa;
	}

	/**通过传来的参数更改表order_data_statistics订单金额与金额uuid
	 * @param id
	 * @param orderTypeQz
	 * @param visaPay
	 * @param payPriceSerialNum
	 */
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService#updateMoneyAndUuid(java.lang.Long, java.lang.Integer, java.math.BigDecimal, java.lang.String)
	 */
	@Override
	public int updateMoneyAndUuid(Long id, Integer orderTypeQz, BigDecimal visaPay, String payPriceSerialNum) {

		int updateMoneyAndUuid = orderDateSaveOrUpdateDao.updateMoneyAndUuid(id, orderTypeQz, visaPay, payPriceSerialNum);
		return updateMoneyAndUuid;
	}
	
	// 向表order_data_statistics中添加数据,或修改数据
	@Override
    public void addOrderDataStatistics(ProductOrderCommon productOrder){

		Long pOrderId = productOrder.getId();
		OrderDataStatistics ods = new OrderDataStatistics();
						
		ods.setUuid(UUID.randomUUID().toString());
		//数据库不能为空值，初始化数据
		
		if(productOrder.getId()!=null && StringUtils.isNotBlank(productOrder.getId().toString())){
			ods.setOrderId(productOrder.getId());
		}else{
			ods.setOrderId(Long.valueOf(0));
		}
		
		if(productOrder.getProductId()!=null && StringUtils.isNotBlank(productOrder.getProductId().toString())){
			ods.setProductId(productOrder.getProductId());
		}else{
			ods.setProductId(Long.valueOf(0));
		}
		
		if(productOrder.getOrderStatus()!=null && StringUtils.isNotBlank(productOrder.getOrderStatus().toString())){
			ods.setOrderType(productOrder.getOrderStatus());
		}else{
			ods.setOrderType(0);
		}
		
		
		if(productOrder.getTotalMoney()!=null && StringUtils.isNotBlank(productOrder.getTotalMoney().toString())){
			ods.setAmountUuid(productOrder.getTotalMoney());
		}else{
			ods.setAmountUuid("");
		}
		
		
		if(productOrder.getOrderPersonNum()!=null && StringUtils.isNotBlank(productOrder.getOrderPersonNum().toString())){
			ods.setOrderPersonNum(productOrder.getOrderPersonNum());
		}else{
			ods.setOrderPersonNum(0);
		}
		
		
		if(productOrder.getOrderTime()!=null && StringUtils.isNotBlank(productOrder.getOrderTime().toString())){
			ods.setOrderCreatetime(productOrder.getOrderTime());
		}else{
			ods.setOrderCreatetime(new Date());
		}
		
		if(productOrder.getOrderCompany()!=null && StringUtils.isNotBlank(productOrder.getOrderCompany().toString())){
			ods.setAgentinfoId(productOrder.getOrderCompany());
		}else{
			ods.setAgentinfoId(Long.valueOf(0));
		}
		
		if(productOrder.getOrderCompanyName()!=null && StringUtils.isNotBlank(productOrder.getOrderCompanyName().toString())){
			ods.setAgentinfoName(productOrder.getOrderCompanyName());
		}else{
			ods.setAgentinfoName("");
		}
		
		if(productOrder.getSalerId()!=null && StringUtils.isNotBlank(productOrder.getSalerId().toString())){
			ods.setSalerId(productOrder.getSalerId());
		}else{
			ods.setSalerId(0);
		}
		
		if(productOrder.getSalerName()!=null && StringUtils.isNotBlank(productOrder.getSalerName().toString())){
			ods.setSalerName(productOrder.getSalerName());
		}else{
			ods.setSalerName("");
		}
		
		ods.setCreateDate(new Date());
		
		if(productOrder.getPayStatus()!=null && StringUtils.isNotBlank(productOrder.getPayStatus().toString())){
			ods.setOrderStatus(productOrder.getPayStatus());
		}else{
			ods.setOrderStatus(0);
		}
		
		if(productOrder.getDelFlag()!=null && StringUtils.isNotBlank(productOrder.getDelFlag().toString())){
			ods.setDelFlag(productOrder.getDelFlag());
		}else{
			ods.setDelFlag("");
		}
							
		//为了向表order_data_statistics中添加数据，需要从四张表查询UUID()、product_name、company_uuid、amount.	
		List<Map<String, Object>> addDate = orderDateSaveOrUpdateDao.getAddDate(pOrderId);
		for(Map<String,Object> mapKey : addDate){
					
			if(mapKey.get("UUID()")!= null && StringUtils.isNotBlank(mapKey.get("UUID()").toString())){
				ods.setUuid(mapKey.get("UUID()").toString());
			}
			if(mapKey.get("product_name")!= null && StringUtils.isNotBlank(mapKey.get("product_name").toString())){
				ods.setProductName(mapKey.get("product_name").toString());
			}else{
				ods.setProductName("");
			}
			
			if(mapKey.get("company_uuid")!= null && StringUtils.isNotBlank(mapKey.get("company_uuid").toString())){
				ods.setCompanyUuid(mapKey.get("company_uuid").toString());
			}else{
				ods.setCompanyUuid("");
			}
			
			if(mapKey.get("amount")!= null && StringUtils.isNotBlank(mapKey.get("amount").toString())){
				ods.setAmount(new BigDecimal(mapKey.get("amount").toString()));
			}else{
				ods.setAmount(new BigDecimal("0"));
			}
		}
		List<Map<String, Object>> orderDataStatisticsId = orderDateSaveOrUpdateDao.getOrderDataStatisticsId(pOrderId);
		//根据order_data_statistics中是否存在相应的值，进行修改或删除
		if(orderDataStatisticsId.size()>0 && orderDataStatisticsId.get(0)!= null && 
				orderDataStatisticsId.get(0).get("id")!= null && 
				StringUtils.isNotBlank(orderDataStatisticsId.get(0).get("id").toString())){
			for(Map<String,Object> mapKeyId : orderDataStatisticsId){
				if(mapKeyId.get("id")!= null && StringUtils.isNotBlank(mapKeyId.get("id").toString())){
					ods.setId(Long.parseLong(mapKeyId.get("id").toString()));
				}
			}
			//修改
			orderDateSaveOrUpdateDao.updateForProductorder(ods.getId(), ods.getOrderPersonNum(), ods.getAmount(), ods.getOrderStatus());
		}else{
			//保存
			orderDateSaveOrUpdateDao.saveObj(ods);
		}
    }

	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService#whetherAddOrUpdate(com.trekiz.admin.modules.order.entity.ProductOrderCommon)
	 * OrderDateSaveOrUpdateService whetherAddOrUpdate
	 * 
	 * 判断是否向表order_data_statistics中加数据，或修改数据
	 * 
	 * 2017年3月24日  上午11:49:16 
	 */
	@Override
	public boolean whetherAddOrUpdate(ProductOrderCommon order) {
		return null!=order.getId() 
				&& (order.getPayStatus()==3 || order.getPayStatus()==4 || order.getPayStatus()==5)
				&& StringUtils.isNotBlank(order.getId().toString());
	}

}
