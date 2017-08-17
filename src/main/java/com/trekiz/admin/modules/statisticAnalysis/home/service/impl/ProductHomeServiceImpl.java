/**
 * 
 */
package com.trekiz.admin.modules.statisticAnalysis.home.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.ProductHomeDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.ProductHomeService;

/**
 * @author junhao.zhao
 * 2016年12月28日  下午3:36:12
 */
@Service
public class ProductHomeServiceImpl implements ProductHomeService{

	@Autowired
	private ProductHomeDao productHomeDao;
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.home.service.ProductHomeService#getListForProductHome(java.util.Map)
	 */
	@Override
	public List<Map<String,Object>> getListForProductHome(Map<String, Object> map) {

		//根据条件查询前五条数据
		List<Map<String,Object>> allList = productHomeDao.getListForOrderType(map);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		//为前台返回限定的数据
		list.addAll(allList);
		//根据分析类型（1：订单数，2：收客人数，3：订单金额）返回结果
		if(map.get("analysisType") != null && StringUtils.isNotBlank(map.get("analysisType").toString())){
			//格式化数字
			//NumberFormat nf = new DecimalFormat("#,###");
			NumberFormat nfm = new DecimalFormat("#,###.00");
			String analysisType = map.get("analysisType").toString();
			for(Map<String,Object> mapKey : list){
				if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){
					if(mapKey.get("orderNum")!= null && StringUtils.isNotBlank(mapKey.get("orderNum").toString())){
						Integer dOrderNum = Integer.parseInt(mapKey.get("orderNum").toString());
						mapKey.put("num", dOrderNum);
					}else{
						mapKey.put("num", "0");
					}
				    
				}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
					if(mapKey.get("orderPersonNum")!= null && StringUtils.isNotBlank(mapKey.get("orderPersonNum").toString())){
						Integer dOrderPersonNum = Integer.parseInt(mapKey.get("orderPersonNum").toString());
						mapKey.put("num", dOrderPersonNum);
					}else{
						mapKey.put("num", "0");
					}
				}else{
					if(mapKey.get("orderMoney")!= null && StringUtils.isNotBlank(mapKey.get("orderMoney").toString())){
						Double dOrderMoney = Double.parseDouble(mapKey.get("orderMoney").toString());
					    String strOrderMoney = nfm.format(dOrderMoney);
						mapKey.put("num",strOrderMoney);
					}else{
						mapKey.put("num","0.00");
					}
				}
		}
		int size = list.size();
		//大于0小于5，只有1-4条，剩下的从团类产品表travelactivity中按产品名称降序排序，查出名称来展示;为0，不展示。团类产品表不足，从签证产品表visa_products中查，仍不够，不管。
		if(size>0 && size<5){
			List<Map<String, Object>> listForProductId = new ArrayList<Map<String,Object>>();
			//拼接从order_data_statistics表中查询到的product_id
			String productId = "";
			String visaId = "";
			String orderType = "";
			for(Map<String,Object> mapList : list){
				if(mapList.get("orderType")!= null && StringUtils.isNotBlank(mapList.get("orderType").toString())){
					orderType = mapList.get("orderType").toString();
					if(!Context.ORDER_STATUS_VISA.equals(orderType) && !Context.ORDER_STATUS_AIR_TICKET.equals(orderType)){
						if(mapList.get("productId")!= null && StringUtils.isNotBlank(mapList.get("productId").toString())){
							productId += "'"+mapList.get("productId").toString()+"',";
						}
					}
					if(Context.ORDER_STATUS_VISA.equals(orderType)){
						if(mapList.get("productId")!= null && StringUtils.isNotBlank(mapList.get("productId").toString())){
							visaId += mapList.get("productId").toString()+",";
						}
					}
				}
			}
			//去掉最后的","
			if(StringUtils.isNotBlank(productId)){
				productId = productId.substring(0, productId.length()-1);
			}
			if(StringUtils.isNotBlank(visaId)){
				visaId = visaId.substring(0, visaId.length()-1);
			}
			//从travelactivity表与visa_products表中查询产品名称
			listForProductId = productHomeDao.getListForProductId(map, productId,visaId,5-size);
			//根据分析类型（1：订单数，2：收客人数，3：订单金额）拼值		
			for(Map<String,Object> mapPro : listForProductId){
				if(mapPro.get("productName")!= null && StringUtils.isNotBlank(mapPro.get("productName").toString())){
					if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){
						mapPro.put("num", "0");
					}
					else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
						mapPro.put("num", "0");
					}
					else{
						mapPro.put("num","0.00");
					}
				}
			}
			list.addAll(listForProductId);
		}
		}
		for(Map<String,Object> maplt : list){
				maplt.remove("orderType");
				maplt.remove("productId");
		}
		return list;
	}
}		
		
		
		
		
		
		
