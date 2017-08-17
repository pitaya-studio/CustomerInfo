package com.trekiz.admin.modules.activity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.activity.repository.IActivityUpPricesDao;
import com.trekiz.admin.modules.review.changeprice.entity.ChangePriceBean;

@SuppressWarnings({ "rawtypes"})
@Service
@Transactional(readOnly = true)
public class ActivityUpPricesServiceImpl extends BaseService implements IActivityUpPricesService {
	
	private static String DEF_MT_YSDJ = "20",DEF_MT_JSJ = "3" ;   // 原始定金结算价 ，当前结算价 的 moneyType
	@Autowired
	private IActivityUpPricesDao activityUpPricesDao;
	
	/**
	 * 查询该订单下的游客列表
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryTravelerList(String orderId) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		// 查询该订单游客列表
		List<Map<String,Object>> traveler = activityUpPricesDao.queryTravelerUpPrices(orderId);
		// 查询该订单游客币种信息
		List<Map<String,Object>> travCurr =activityUpPricesDao.queryTravelerCurrency(orderId); 
		
		if(null != traveler ){
			for(Map<String,Object> map:traveler){
				Map<String,Object> rawMap = new HashMap<>(); 
				rawMap.put(ChangePriceBean.TRVALER_ID, map.get("id")); 				// 游客id
				rawMap.put(ChangePriceBean.TRAVALER_NAME, map.get("name")); 		// 游客名称
				List<Map<String,Object>> travelers = new ArrayList<Map<String,Object>>(); 
				int k = 0 ;
				String original_ = null;
				for(int i=0;i<travCurr.size(); i++){
					Map<String,Object> map1 = travCurr.get(i);
					// 判断币种是属于哪位游客的
					if(map.get("id").toString().equals(map1.get("id").toString())){
						if(original_ == null){
							original_ = String.valueOf(map1.get("original_payPriceSerialNum"));
						}
						// 获取原始应收价
						List<Map<String,Object>> originals = activityUpPricesDao.queryOriginalMoney(orderId, original_);
						map1.put(ChangePriceBean.OLD_TOTAL_MONEY, getOriginals2Obj(originals,k));
						travelers.add(map1);
						if(original_ .equals(map1.get("original_payPriceSerialNum"))){
							k ++ ;
						}else{
							 k = 0 ;
							 original_ = String.valueOf(map1.get("original_payPriceSerialNum"));
						}
//						rawMap.put(ChangePriceBean.CURRENCY_ID, map1.get("currency_id"));   // 币种id
//						rawMap.put(ChangePriceBean.CURRENCY_NAME, map1.get("currency_name"));  // 币种名称
//						rawMap.put(ChangePriceBean.OLD_TOTAL_MONEY, getOriginals2Obj(originals)); // 原始应收价
//						rawMap.put(ChangePriceBean.CUR_TOTAL_MONEY, map1.get("amount"));	// 当前应收价
						
						
					}
					
				}
				
				
				rawMap.put("travelers", travelers);
				resultList.add(rawMap);
			}
		}  
		return resultList;
	}
	
	/**
	 * 查询该订单下的游客列表
	 * update by ruyi.chen
	 * add date 2015-06-30
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryHotelTravelerList(String orderUuid) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		// 查询该订单游客列表
		List<Map<String,Object>> traveler = activityUpPricesDao.queryHotelTravelerUpPrices(orderUuid);
		// 查询该订单游客币种信息
		List<Map<String,Object>> travCurr =activityUpPricesDao.queryHotelTravelerCurrency(orderUuid);
		
		if(null != traveler ){
			for(Map<String,Object> map:traveler){
				Map<String,Object> rawMap = new HashMap<>(); 
				rawMap.put(ChangePriceBean.TRVALER_ID, map.get("id")); 				// 游客id
				rawMap.put(ChangePriceBean.TRAVALER_NAME, map.get("name")); 		// 游客名称
				List<Map<String,Object>> travelers = new ArrayList<Map<String,Object>>(); 
				int k = 0 ;
				String original_ = null;
				if(CollectionUtils.isEmpty(travCurr)) {
					travelers.add(map);
				}
				for(int i=0;i<travCurr.size(); i++){
					Map<String,Object> map1 = travCurr.get(i);
					// 判断币种是属于哪位游客的
					if(map.get("id").toString().equals(map1.get("id").toString())){
						if(original_ == null){
							original_ = String.valueOf(map1.get("original_payPriceSerialNum"));
						}
						// 获取原始应收价
						List<Map<String,Object>> originals = activityUpPricesDao.queryHotelOriginalMoney(orderUuid, original_);
						map1.put(ChangePriceBean.OLD_TOTAL_MONEY, getOriginals2Obj(originals,k));
						travelers.add(map1);
						if(original_ .equals(map1.get("original_payPriceSerialNum"))){
							k ++ ;
						}else{
							 k = 0 ;
							 original_ = String.valueOf(map1.get("original_payPriceSerialNum"));
						}
//						rawMap.put(ChangePriceBean.CURRENCY_ID, map1.get("currency_id"));   // 币种id
//						rawMap.put(ChangePriceBean.CURRENCY_NAME, map1.get("currency_name"));  // 币种名称
//						rawMap.put(ChangePriceBean.OLD_TOTAL_MONEY, getOriginals2Obj(originals)); // 原始应收价
//						rawMap.put(ChangePriceBean.CUR_TOTAL_MONEY, map1.get("amount"));	// 当前应收价
						
						
					}
					
				}
				
				
				rawMap.put("travelers", travelers);
				resultList.add(rawMap);
			}
		}  
		return resultList;
	}
	/**
	 * 查询该订单下的游客列表
	 * update by ruyi.chen
	 * add date 2015-06-30
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryIslandTravelerList(String orderUuid) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		// 查询该订单游客列表
		List<Map<String,Object>> traveler = activityUpPricesDao.queryIslandTravelerUpPrices(orderUuid);
		// 查询该订单游客币种信息
		List<Map<String,Object>> travCurr =activityUpPricesDao.queryIslandTravelerCurrency(orderUuid); 
		
		if(null != traveler ){
			for(Map<String,Object> map:traveler){
				Map<String,Object> rawMap = new HashMap<>(); 
				rawMap.put(ChangePriceBean.TRVALER_ID, map.get("id")); 				// 游客id
				rawMap.put(ChangePriceBean.TRAVALER_NAME, map.get("name")); 		// 游客名称
				List<Map<String,Object>> travelers = new ArrayList<Map<String,Object>>(); 
				int k = 0 ;
				String original_ = null;
				if(CollectionUtils.isEmpty(travCurr)) {
					travelers.add(map);
				}
				for(int i=0;i<travCurr.size(); i++){
					Map<String,Object> map1 = travCurr.get(i);
					// 判断币种是属于哪位游客的
					if(map.get("id").toString().equals(map1.get("id").toString())){
						if(original_ == null){
							original_ = String.valueOf(map1.get("original_payPriceSerialNum"));
						}
						// 获取原始应收价
						List<Map<String,Object>> originals = activityUpPricesDao.queryIslandOriginalMoney(orderUuid, original_);
						map1.put(ChangePriceBean.OLD_TOTAL_MONEY, getOriginals2Obj(originals,k));
						travelers.add(map1);
						if(original_ .equals(map1.get("original_payPriceSerialNum"))){
							k ++ ;
						}else{
							 k = 0 ;
							 original_ = String.valueOf(map1.get("original_payPriceSerialNum"));
						}
//						rawMap.put(ChangePriceBean.CURRENCY_ID, map1.get("currency_id"));   // 币种id
//						rawMap.put(ChangePriceBean.CURRENCY_NAME, map1.get("currency_name"));  // 币种名称
//						rawMap.put(ChangePriceBean.OLD_TOTAL_MONEY, getOriginals2Obj(originals)); // 原始应收价
//						rawMap.put(ChangePriceBean.CUR_TOTAL_MONEY, map1.get("amount"));	// 当前应收价
						
						
					}
					
				}
				
				
				rawMap.put("travelers", travelers);
				resultList.add(rawMap);
			}
		}  
		return resultList;
	}
	/**
	 * 查询订单定金
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryFrontMoneyList(String orderId) {
		return activityUpPricesDao.queryFrontMoney(orderId);
	}
	/*
	 * 获取‘原始应收价’列表里的数据
	 */
	public Object getOriginals2Obj(List<Map<String,Object>> originals,int idex){
		if(null == originals){
			return "0.00";
		}else if(originals.size() == 0){
			return "0.00";
		}
		
		return originals.get(idex).get("amount"); 
	}
	@Override
	public List<Map<String, Object>> queryOrderReceivable(String orderId) {
		return activityUpPricesDao.queryOrderReceivable(orderId) ;
	}
	
	/**
	 *  验证游客的改价申请流程是否有申请记录
	 * @param orderId
	 * @param productType
	 * @param flowType
	 * @param currencyid
	 * @return 是/否 (false/true)
	 */
	public boolean verifyTravelerFlowsign(String orderId,String productType,String flowType,String currencyid,String travelerId){
		List<Map<String,Object>> list = formatReviewDetials(activityUpPricesDao.queryReviewDetials(orderId, productType, flowType));
		int  k = 0 ; 
		for(Map rawMap : list){
			if(rawMap.get(ChangePriceBean.TRVALER_ID).toString().equals(travelerId)){  // 该游客有改价申请属性记录
				if(rawMap.get(ChangePriceBean.CURRENCY_ID).toString().equals(currencyid)){  // 该游客存在改价申请属性记录
					k ++; 
					break;
				}
			}
		}
		
		if(k == 0){
			return true;
		}
		return false;
	}
	/**
	 *  验证定金的改价申请流程是否有申请记录
	 * @param orderId
	 * @param productType
	 * @param flowType
	 * @param currencyid 
	 * @return 是/否 (false/true)
	 */
	@Override
	public boolean verifyfrontMoneyFlowsign(String orderId, String productType,
			String flowType, String currencyid) {
		List<Map<String,Object>> list = formatReviewDetials(activityUpPricesDao.queryReviewDetials(orderId, productType, flowType));
		int  k = 0 ; 
		for(Map rawMap : list){
			if("订金".equals(rawMap.get(ChangePriceBean.CHANGED_FUND).toString()) || "定金".equals(rawMap.get(ChangePriceBean.CHANGED_FUND).toString())  ){  // 订金
				if(currencyid.equals(rawMap.get(ChangePriceBean.CURRENCY_ID).toString())){ // 订金的币种相同
					k ++;
					break;
				}
			}
		}
		
		if(k == 0){
			return true;
		}
		return false;
	}
	/**
	 * 格式化流程属性数据
	 * @param listDetals
	 * @return
	 */
	public List<Map<String,Object>> formatReviewDetials(List<Map> listDetals){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> rawMap = new HashMap<String, Object>();
		for(Map tempMap : listDetals){
			if(null == rawMap.get("id")){ 
				rawMap = new HashMap<String, Object>();
				rawMap.put("id", tempMap.get("id").toString());
				resultList.add(rawMap);
			}
			if(rawMap.get("id").equals(tempMap.get("id").toString())){
				switch(tempMap.get("mykey").toString()){ 
				case ChangePriceBean.TRVALER_ID :
					rawMap.put(ChangePriceBean.TRVALER_ID, String.valueOf(tempMap.get("myvalue")));  
					break;
				case ChangePriceBean.TRAVALER_NAME:
					rawMap.put(ChangePriceBean.TRAVALER_NAME, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CURRENCY_ID:
					rawMap.put(ChangePriceBean.CURRENCY_ID, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CURRENCY_NAME:
					rawMap.put(ChangePriceBean.CURRENCY_NAME, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.OLD_TOTAL_MONEY:
					rawMap.put(ChangePriceBean.OLD_TOTAL_MONEY, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CUR_TOTAL_MONEY:
					rawMap.put(ChangePriceBean.CUR_TOTAL_MONEY, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CHANGED_TOTAL_MONEY:
					rawMap.put(ChangePriceBean.CHANGED_TOTAL_MONEY, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CHANGED_PRICE:
					rawMap.put(ChangePriceBean.CHANGED_PRICE, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CHANGED_FUND:
					rawMap.put(ChangePriceBean.CHANGED_FUND, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CHANGEDREMARK:
					rawMap.put(ChangePriceBean.CHANGEDREMARK, String.valueOf(tempMap.get("myvalue")));
					break;
				}
			}else{
				rawMap = new HashMap<String, Object>();
				rawMap.put("id", tempMap.get("id").toString());
				switch(tempMap.get("mykey").toString()){ 
				case ChangePriceBean.TRVALER_ID :
					rawMap.put(ChangePriceBean.TRVALER_ID, String.valueOf(tempMap.get("myvalue")));  
					break;
				case ChangePriceBean.TRAVALER_NAME:
					rawMap.put(ChangePriceBean.TRAVALER_NAME, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CURRENCY_ID:
					rawMap.put(ChangePriceBean.CURRENCY_ID, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CURRENCY_NAME:
					rawMap.put(ChangePriceBean.CURRENCY_NAME, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.OLD_TOTAL_MONEY:
					rawMap.put(ChangePriceBean.OLD_TOTAL_MONEY, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CUR_TOTAL_MONEY:
					rawMap.put(ChangePriceBean.CUR_TOTAL_MONEY, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CHANGED_TOTAL_MONEY:
					rawMap.put(ChangePriceBean.CHANGED_TOTAL_MONEY, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CHANGED_PRICE:
					rawMap.put(ChangePriceBean.CHANGED_PRICE, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CHANGED_FUND:
					rawMap.put(ChangePriceBean.CHANGED_FUND, String.valueOf(tempMap.get("myvalue")));
					break;
				case ChangePriceBean.CHANGEDREMARK:
					rawMap.put(ChangePriceBean.CHANGEDREMARK, String.valueOf(tempMap.get("myvalue")));
					break;
				}
				
				
				resultList.add(rawMap);
			}
			
		}
		
		return resultList;
	}
	@Override
	public List<Map<String, Object>> queryTravelerUpPrices(String orderId) {
		return activityUpPricesDao.queryTravelerUpPrices(orderId);
	}
}
