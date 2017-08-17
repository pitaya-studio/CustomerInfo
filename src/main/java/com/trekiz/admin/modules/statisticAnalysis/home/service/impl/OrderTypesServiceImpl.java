package com.trekiz.admin.modules.statisticAnalysis.home.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderTypesDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderTypesService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author junhao.zhao
 * 2016年12月27日  下午7:01:40
 */
@Service
@Transactional(readOnly = true)
public class OrderTypesServiceImpl extends BaseService implements OrderTypesService{

	/**
	 *  判断顶栏——单团、散拼、游学、大客户、自由行、签证、游轮--是否展示
	 */
	@Autowired
	private OrderTypesDao orderTypesDao;
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.OrderTypesService#getOrderTypes()
	 */
	@Override
	public List<Map<String,Object>> getOrderTypes() {
		List<Map<String,Object>> arrayList = new ArrayList<Map<String,Object>>();
		Long companyId = UserUtils.getUser().getCompany().getId();
			//1：单团 
			String SINGLE = orderTypesDao.getOrderTypes(Context.ORDER_STATUS_SINGLE, companyId);
			if(StringUtils.isNotBlank(SINGLE)){
				String chineseOrderType = OrderCommonUtil.getChineseOrderType(SINGLE);
				Map<String,Object> orderTypeMap = new HashMap<String, Object>();
				orderTypeMap.put("typeValue", Context.ORDER_STATUS_SINGLE);
				orderTypeMap.put("typeName", chineseOrderType);
				arrayList.add(orderTypeMap);
			}
			//2：散拼  
			String LOOSE = orderTypesDao.getOrderTypes(Context.ORDER_STATUS_LOOSE, companyId);
			if(StringUtils.isNotBlank(LOOSE)){
				String chineseOrderType = OrderCommonUtil.getChineseOrderType(LOOSE);
				Map<String,Object> orderTypeMap = new HashMap<String, Object>();
				orderTypeMap.put("typeValue", Context.ORDER_STATUS_LOOSE);
				orderTypeMap.put("typeName", chineseOrderType);
				arrayList.add(orderTypeMap);
			}
			//3：游学 
			String STUDY = orderTypesDao.getOrderTypes(Context.ORDER_STATUS_STUDY, companyId);
			if(StringUtils.isNotBlank(STUDY)){
				String chineseOrderType = OrderCommonUtil.getChineseOrderType(STUDY);
				Map<String,Object> orderTypeMap = new HashMap<String, Object>();
				orderTypeMap.put("typeValue", Context.ORDER_STATUS_STUDY);
				orderTypeMap.put("typeName", chineseOrderType);
				arrayList.add(orderTypeMap);
			}
			//4：大客户 
			String BIG_CUSTOMER = orderTypesDao.getOrderTypes(Context.ORDER_STATUS_BIG_CUSTOMER, companyId);
			if(StringUtils.isNotBlank(BIG_CUSTOMER)){
				String chineseOrderType = OrderCommonUtil.getChineseOrderType(BIG_CUSTOMER);
				Map<String,Object> orderTypeMap = new HashMap<String, Object>();
				orderTypeMap.put("typeValue", Context.ORDER_STATUS_BIG_CUSTOMER);
				orderTypeMap.put("typeName", chineseOrderType);
				arrayList.add(orderTypeMap);
			}
			//5：自由行 
			String FREE = orderTypesDao.getOrderTypes(Context.ORDER_STATUS_FREE, companyId);
			if(StringUtils.isNotBlank(FREE)){
				String chineseOrderType = OrderCommonUtil.getChineseOrderType(FREE);
				Map<String,Object> orderTypeMap = new HashMap<String, Object>();
				orderTypeMap.put("typeValue", Context.ORDER_STATUS_FREE);
				orderTypeMap.put("typeName", chineseOrderType);
				arrayList.add(orderTypeMap);
			}
			//6：签证 
			String VISA = orderTypesDao.getOrderTypesForVisa(companyId);
			if(StringUtils.isNotBlank(VISA)){
				String chineseOrderType = OrderCommonUtil.getChineseOrderType(VISA);
				Map<String,Object> orderTypeMap = new HashMap<String, Object>();
				orderTypeMap.put("typeValue", Context.ORDER_STATUS_VISA);
				orderTypeMap.put("typeName", chineseOrderType);
				arrayList.add(orderTypeMap);
			}
			/*//7：机票
			String AIR_TICKET = orderTypesDao.getOrderTypes(Context.ORDER_STATUS_AIR_TICKET);
			if(StringUtils.isNotBlank(AIR_TICKET)){
				String chineseOrderType = OrderCommonUtil.getChineseOrderType(AIR_TICKET);
				arrayList.add(chineseOrderType);
			}*/
			//10：游轮
			String CRUISE = orderTypesDao.getOrderTypes(Context.ORDER_STATUS_CRUISE, companyId);
			if(StringUtils.isNotBlank(CRUISE)){
				String chineseOrderType = OrderCommonUtil.getChineseOrderType(CRUISE);
				Map<String,Object> orderTypeMap = new HashMap<String, Object>();
				orderTypeMap.put("typeValue", Context.ORDER_STATUS_CRUISE);
				orderTypeMap.put("typeName", chineseOrderType);
				arrayList.add(orderTypeMap);
			}
		return arrayList;
	}
}
