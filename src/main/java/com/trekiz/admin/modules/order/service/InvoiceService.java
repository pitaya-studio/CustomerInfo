package com.trekiz.admin.modules.order.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.hotel.dao.HotelOrderDao;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.repository.OrderinvoiceDao;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderInvoiceVO;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.receipt.entity.OrderReceipt;
import com.trekiz.admin.modules.receipt.repository.OrderReceiptDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaOrderTravelerPayLogService;
/**
 * 
 * @author ruyi.chen 
 * update date 2014-11-19
 * describe   综合发票类业务以及订单类业务，补充  OrderCommonService
 *
 */
@Service
@Transactional(readOnly = true)
public class InvoiceService extends BaseService{

	@Autowired
	private OrderinvoiceDao orderinvoiceDao;
	@Autowired
    private ProductOrderCommonDao productorderDao;
    @Autowired
    private OrderinvoiceService orderinvoiceService;
    @Autowired
    private IAirticketOrderDao airticketOrderDao;
    @Autowired
	private VisaOrderDao visaOrderDao;
    @Autowired
    private VisaOrderTravelerPayLogService visaOrderTravelerPayLogService;
    @Autowired
	private OrderReceiptDao orderReceiptDao;
    @Autowired
    private MoneyAmountService moneyAmountService;
    @Autowired
    private IslandOrderService islandOrderService;
    @Autowired
    private IslandOrderDao islandOrderDao;
    @Autowired
    private HotelOrderDao hotelOrderDao;
	/**
	 * 获取当前订单相关信息,订单已开发票总额，订单已收金额
	 * add by  ruyi.chen
	 * add date 2014 11-17
	 * @param orderId 订单唯一标识
	 */
	private List<Map<Object,Object>> getInvoiceOrderInfoByNum(String orderNum,String orderType,boolean flag,String salerId){		
		String querySql = "";
		String orderTypeSql = "orderType='" + orderType + "' ";
		String payType = " and mac.moneyType =11 ";//款项类型，退款
		//改为多币种处理后sql语句
		String sql = "";
		String createStr = " ";
		switch(orderType){
			case Context.ORDER_STATUS_VISA:
				if(flag){
					createStr = createStr+" and p.payStatus<>'99' "+" and (p.salerId='" + salerId + "' or p.create_by ='"+salerId +"')";
				}
				querySql = " p.order_no='" + orderNum + "'"+createStr;
				sql = this.getVisaOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_AIR_TICKET:
				if(flag){
					createStr = createStr+" and p.order_state<>'111' and p.order_state<>'99' "+" and (p.salerId='" + salerId + "' or p.create_by ='"+salerId +"')";
				}
				querySql = " p.order_no='" + orderNum + "'"+createStr;
				sql = this.getAirOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_HOTEL:
				if(flag){
					createStr = createStr+" and p.orderStatus<>'3' "+" and (p.salerId='" + salerId + "' or p.createBy ='"+salerId +"')";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getHotelOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_ISLAND:
				if(flag){
					createStr = createStr+" and p.orderStatus<>'3' "+" and (p.salerId='" + salerId + "' or p.createBy ='"+salerId +"')";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getIslandOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			default:
				if(flag){
					createStr = createStr+" and p.payStatus<>'111' and p.payStatus<>'99' "+" and (p.salerId='" + salerId + "' or p.createBy ='"+salerId +"')";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
		}
		List<Map <Object,Object>>ls = orderinvoiceDao.findBySql(sql,Map.class);
		
		return ls;
				
	}/**
	 * 获取当前订单基本信息、已开发票信息、退款信息、支付信息等
	 * add by  ruyi.chen
	 * add date 2014 11-18
	 * @param orderId 订单唯一标识
	 */
	public Map<Object,Object>getApplyInvoiceInfo(String orderNum,String orderType){
		Map<Object,Object>m = new HashMap<Object,Object>();
		
		/**
		 * add by ruyi.chen
		 * 根据订单号获取相应全平台对应的订单类型，根据不同的订单类型加载相应的订单数据信息
		 */
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String, Object>>) visaOrderTravelerPayLogService.getOrderByorderCode(orderNum);
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		if(null != list && 0 < list.size()){
			
			Map<String,Object> rMap = null;
			String orderTypeAdd = null;
			for (Map<String,Object> map : list) {
				orderTypeAdd = map.get("productType").toString();
				if (orderType.equals(orderTypeAdd) && UserUtils.getUser(map.get("salerId").toString()).getCompany().getId() == companyId) {
					rMap = map;
					break;
				} else {
					continue;
				}
			}
			
			if (rMap == null) {
				rMap = list.get(0);
				orderTypeAdd = rMap.get("productType").toString();
			}
			
			String salerId = rMap.get("salerId").toString();
			//TODO 根据是否限制本人开发票来判断
			
			//是否限制本人   限制为true,不限制false
			boolean flag = true;
			List<Map<Object,Object>>ls = getInvoiceOrderInfoByNum(orderNum,orderTypeAdd,flag,salerId);
			if(null != ls && ls.size() > 0){
				m=ls.get(0);
				m.put("orderType", orderTypeAdd);
				m.put("orderTypeName", rMap.get("productTypeName"));
				String orderTotal = m.get("orderyTotal").toString();
				if(StringUtils.isNotBlank(orderTotal)){
					m.put("orderyTotal", getMoneyStr(orderTotal));
				}
				
				String alreadyPaid = m.get("alreadyPaid").toString();
				if(StringUtils.isNotBlank(alreadyPaid)){
					m.put("alreadyPaid", getMoneyStr(alreadyPaid));
				}
				
				String totalAsAcount = m.get("totalAsAcount").toString();
				if(StringUtils.isNotBlank(totalAsAcount)){
					m.put("totalAsAcount", getMoneyStr(totalAsAcount));
				}
				
				String refundableAmount = m.get("refundableAmount").toString();
				if(StringUtils.isNotBlank(refundableAmount)){
					m.put("refundableAmount", "￥"+refundableAmount);
				}
				String invoiceAmountStr = m.get("invoiceAmountStr").toString();
				if(StringUtils.isNotBlank(invoiceAmountStr)){
					m.put("invoiceAmount", "￥"+invoiceAmountStr);
				}
			}else{
				m = null;
			}
			
		}
		
		return m;		
		
	}
	
	/**
	 * 获取当前订单相关信息,订单已开收据总额，订单已收金额
	 * add by  ruyi.chen
	 * add date 2015 07-14
	 * @param orderId 订单唯一标识
	 */
	private List<Map<Object,Object>> getReceiptOrderInfoByNum(String orderNum,String orderType,boolean flag,String salerId){		
		String querySql = "";
		String orderTypeSql = "orderType='" + orderType + "' ";
		String payType = " and mac.moneyType =11 ";//款项类型，退款
		//改为多币种处理后sql语句
		String sql = "";
		String createStr = " ";
		switch(orderType){
			case Context.ORDER_STATUS_VISA:
				if(flag){
					createStr = createStr+" and p.payStatus<>'99' "+" and (p.salerId='" + salerId + "' or p.create_by ='"+salerId +"')";
				}
				querySql = " p.order_no='" + orderNum + "'"+createStr;
				sql = this.getVisaOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_AIR_TICKET:
				if(flag){
					createStr = createStr+" and p.order_state<>'111' and p.order_state<>'99' "+" and (p.salerId='" + salerId + "' or p.create_by ='"+salerId +"')";
				}
				querySql = " p.order_no='" + orderNum + "'"+createStr;
				sql = this.getAirOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_HOTEL:
				if(flag){
					createStr = createStr+" and p.orderStatus<>'3' "+" and (p.salerId='" + salerId + "' or p.createBy ='"+salerId +"')";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getHotelOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_ISLAND:
				if(flag){
					createStr = createStr+" and p.orderStatus<>'3' "+" and (p.salerId='" + salerId + "' or p.createBy ='"+salerId +"')";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getIslandOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
			default:
				if(flag){
					createStr = createStr+" and p.payStatus<>'111' and p.payStatus<>'99' "+" and (p.salerId='" + salerId + "' or p.createBy ='"+salerId +"')";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
		}
		List<Map <Object,Object>>ls = orderinvoiceDao.findBySql(sql,Map.class);
		
		return ls;
				
	}
	/**
	 * 获取当前订单基本信息、已开发票信息、退款信息、支付信息等
	 * add by  ruyi.chen
	 * add date 2014 11-18
	 * @param orderId 订单唯一标识
	 */
	public Map<Object,Object>getApplyReceiptInfo(String orderNum,String orderType){
		Map<Object,Object>m = new HashMap<Object,Object>();
		/**
		 * add by ruyi.chen
		 * 根据订单号获取相应全平台对应的订单类型，根据不同的订单类型加载相应的订单数据信息
		 */
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String, Object>>) visaOrderTravelerPayLogService.getOrderByorderCode(orderNum);
		if(null != list && 0 < list.size()){
			Map<String,Object> rMap = list.get(0);
			String orderTypeAdd = rMap.get("productType").toString();
			String salerId = rMap.get("salerId").toString();
			//TODO 根据是否限制本人开发票来判断
			
			//是否限制本人   限制为true,不限制false
			boolean flag = true;
			List<Map<Object,Object>>ls = getReceiptOrderInfoByNum(orderNum,orderTypeAdd,flag,salerId);
			if(null != ls && ls.size() > 0){
				m=ls.get(0);
				m.put("orderType", orderTypeAdd);
				m.put("orderTypeName", rMap.get("productTypeName"));
				String orderTotal = m.get("orderyTotal").toString();
				if(StringUtils.isNotBlank(orderTotal)){
					m.put("orderyTotal", getMoneyStr(orderTotal));
				}
				
				String alreadyPaid = m.get("alreadyPaid").toString();
				if(StringUtils.isNotBlank(alreadyPaid)){
					m.put("alreadyPaid", getMoneyStr(alreadyPaid));
				}
				
				String totalAsAcount = m.get("totalAsAcount").toString();
				if(StringUtils.isNotBlank(totalAsAcount)){
					m.put("totalAsAcount", getMoneyStr(totalAsAcount));
				}
				
				String refundableAmount = m.get("refundableAmount").toString();
				if(StringUtils.isNotBlank(refundableAmount)){
					m.put("refundableAmount", "￥"+refundableAmount);
				}
				String invoiceAmountStr = m.get("invoiceAmountStr").toString();
				if(StringUtils.isNotBlank(invoiceAmountStr)){
					m.put("invoiceAmount", "￥"+invoiceAmountStr);
				}
			}else{
				m = null;
			}
			
		}
		
		
		return m;		
		
	}
	
	/**
	 * 获取当前订单所在团 的订单信息(当前用户可见),订单已开发票总额,订单退款信息等
	 * add by  ruyi.chen
	 * add date 2014 11-17
	 * @param orderId 订单唯一标识
	 */
	@SuppressWarnings("unchecked")
	public List<Map<Object,Object>> getInvoiceOrderInfoByGroup(Long orderId,String orderType,String orderNum){
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		String query = "";
		List<Map<String,Object>> list = (List<Map<String, Object>>) visaOrderTravelerPayLogService.getOrderByorderCode(orderNum);
		if(null != list && 0 < list.size()){
			
			Map<String,Object> rMap = null;
			String orderTypeAdd = null;
			for (Map<String,Object> map : list) {
				orderTypeAdd = map.get("productType").toString();
				if (orderType.equals(orderTypeAdd) && UserUtils.getUser(map.get("salerId").toString()).getCompany().getId() == companyId) {
					rMap = map;
					break;
				} else {
					continue;
				}
			}
			
			if (rMap == null) {
				rMap = list.get(0);
				orderTypeAdd = rMap.get("productType").toString();
			}
			
			String salerId = rMap.get("salerId").toString();
			
			List<Map<String,Object>> ls = getGroupOrdersByOrderId(orderId,orderTypeAdd,salerId);
			
			if(null != ls && ls.size() > 0){
				for(Map<String,Object>m:ls){
					String ordersId = m.get("orderId").toString();
					if(StringUtils.isNotBlank(ordersId)){
						query = query+ordersId+",";
					}
				}
			}
			
			if(!"".equals(query)){
				query = query.substring(0, query.length()-1);
			}
			String querySql = "";
			String payType = " and mac.moneyType =11 ";//款项类型，退款
			String orderTypeSql = "orderType='"+orderType + "' ";
			String sql = "";
			switch(orderType){
			case Context.ORDER_STATUS_VISA:
				querySql = " p.id in("+query+") and p.salerId='"+salerId+ "'  and p.payStatus<>'99' ";
				sql = this.getVisaOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_AIR_TICKET:
				querySql = " p.id in("+query+") and p.salerId='"+salerId+"' and p.order_state<>'111' and p.order_state<>'99' ";
				sql = this.getAirOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_HOTEL:
				querySql = " p.id in("+query+") and p.salerId='"+salerId+"' and p.orderStatus<>'3' ";
				sql = this.getHotelOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_ISLAND:
				querySql = " p.id in("+query+") and p.salerId='"+salerId+"' and p.orderStatus<>'3' ";
				sql = this.getIslandOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			default:
				querySql = " p.id in("+query+") and p.salerId='"+salerId+"' and p.payStatus<>'111' and p.payStatus<>'99' ";
				sql = this.getOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			}
			
			List<Map <Object,Object>>l = orderinvoiceDao.findBySql(sql,Map.class);
			if(null != l && l.size()>0){
				for(Map<Object,Object>m:l){
					m.put("orderType", orderType);
					m.put("orderTypeName", OrderCommonUtil.getChineseOrderType(orderType));
					String orderTotal = m.get("orderyTotal").toString();
					if(StringUtils.isNotBlank(orderTotal)){
						m.put("orderyTotal", getMoneyStr(orderTotal));
					}
					
					String alreadyPaid = m.get("alreadyPaid").toString();
					if(StringUtils.isNotBlank(alreadyPaid)){
						m.put("alreadyPaid", getMoneyStr(alreadyPaid));
					}
					
					String totalAsAcount = m.get("totalAsAcount").toString();
					if(StringUtils.isNotBlank(totalAsAcount)){
						m.put("totalAsAcount", getMoneyStr(totalAsAcount));
					}
					String refundableAmount = m.get("refundableAmount").toString();
					if(StringUtils.isNotBlank(refundableAmount)){
						m.put("refundableAmount", "￥"+refundableAmount);
					}
					String invoiceAmountStr = m.get("invoiceAmountStr").toString();
					if(StringUtils.isNotBlank(invoiceAmountStr)){
						m.put("invoiceAmount", "￥"+invoiceAmountStr);
					}
				}
			}
			return l;
		}	
		
		return null;		
		
	}
	
	/**
	 * 获取当前订单所在团 的订单信息(当前用户可见),订单已开发票总额,订单退款信息等
	 * add by  ruyi.chen
	 * add date 2014 11-17
	 * @param orderId 订单唯一标识
	 */
	@SuppressWarnings("unchecked")
	public List<Map<Object,Object>> getReceiptOrderInfoByGroup(Long orderId,String orderType,String orderNum){
		
		String query = "";
		List<Map<String,Object>> list = (List<Map<String, Object>>) visaOrderTravelerPayLogService.getOrderByorderCode(orderNum);
		if(null != list && 0 < list.size()){
			Map<String,Object> rMap = list.get(0);
			String orderTypeAdd = rMap.get("productType").toString();
			String salerId = rMap.get("salerId").toString();
			
			List<Map<String,Object>> ls = getGroupOrdersByOrderId(orderId,orderTypeAdd,salerId);
			
			if(null != ls && ls.size() > 0){
				for(Map<String,Object>m:ls){
					String ordersId = m.get("orderId").toString();
					if(StringUtils.isNotBlank(ordersId)){
						query = query+ordersId+",";
					}
				}
			}
			if(!"".equals(query)){
				query = query.substring(0, query.length()-1);
			}
			String querySql = "";
			String payType = " and mac.moneyType =11 ";//款项类型，退款
			String orderTypeSql = "orderType='"+orderType + "' ";
			String sql = "";
			switch(orderType){
				case Context.ORDER_STATUS_VISA:
					querySql = " p.id in("+query+") and p.salerId='"+salerId+ "'  and p.payStatus<>'99' ";
					sql = this.getVisaOrderReceiptSelectSql(querySql,payType,orderTypeSql);
					
					break;
				case Context.ORDER_STATUS_AIR_TICKET:
					querySql = " p.id in("+query+") and p.salerId='"+salerId+"' and p.order_state<>'111' and p.order_state<>'99' ";
					sql = this.getAirOrderReceiptSelectSql(querySql,payType,orderTypeSql);
					
					break;
				case Context.ORDER_STATUS_HOTEL:
					querySql = " p.id in("+query+") and p.salerId='"+salerId+"' and p.orderStatus<>'3' ";
					sql = this.getHotelOrderReceiptSelectSql(querySql,payType,orderTypeSql);
					
					break;
				case Context.ORDER_STATUS_ISLAND:
					querySql = " p.id in("+query+") and p.salerId='"+salerId+"' and p.orderStatus<>'3' ";
					sql = this.getIslandOrderReceiptSelectSql(querySql,payType,orderTypeSql);
					
					break;
				default:
					querySql = " p.id in("+query+") and p.salerId='"+salerId+"' and p.payStatus<>'111' and p.payStatus<>'99' ";
					sql = this.getOrderReceiptSelectSql(querySql,payType,orderTypeSql);
					
					break;
			}
			
			List<Map <Object,Object>>l = orderinvoiceDao.findBySql(sql,Map.class);
			if(null != l && l.size()>0){
				for(Map<Object,Object>m:l){
					m.put("orderType", orderType);
					m.put("orderTypeName", OrderCommonUtil.getChineseOrderType(orderType));
					String orderTotal = m.get("orderyTotal").toString();
					if(StringUtils.isNotBlank(orderTotal)){
						m.put("orderyTotal", getMoneyStr(orderTotal));
					}
					
					String alreadyPaid = m.get("alreadyPaid").toString();
					if(StringUtils.isNotBlank(alreadyPaid)){
						m.put("alreadyPaid", getMoneyStr(alreadyPaid));
					}
					
					String totalAsAcount = m.get("totalAsAcount").toString();
					if(StringUtils.isNotBlank(totalAsAcount)){
						m.put("totalAsAcount", getMoneyStr(totalAsAcount));
					}
					// 未开收据金额
					String refundableAmount = m.get("refundableAmount").toString();
					if(StringUtils.isNotBlank(refundableAmount)){
						m.put("refundableAmount", "￥"+refundableAmount);
					}
					// 已开收据金额
					String invoiceAmountStr = m.get("invoiceAmountStr").toString();
					if(StringUtils.isNotBlank(invoiceAmountStr)){
						m.put("invoiceAmount", "￥"+invoiceAmountStr);
					}
				}
			}
			return l;
		}
				
		return null;
	}
	
	/**
	 * 保存申请发票信息
	 * add by  ruyi.chen
	 * add date 2014 11-12
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Integer saveApplyInvoiceInfos(OrderInvoiceVO orderInvoice,String orderType){
		Long[] orderIds = orderInvoice.getOrderId();
		BigDecimal[] orderInvoiceAmount = orderInvoice.getOrderInvoiceAmount();
		String[]orderNums = orderInvoice.getOrderNum();
		String[]groupCodes = orderInvoice.getGroupCode();
		Integer[]orderTypes = orderInvoice.getOrderTypes();
		String invoiceNum = orderinvoiceService.createVirtualInvoiceNum();
		StringBuffer sbf = new StringBuffer();
		String uuid = UuidUtils.generUuid();
		sbf.append("保存申请发票：");
		for(int i = 0;i < orderIds.length;i++){
			//组建保存发票信息实体
			Orderinvoice o = new Orderinvoice();
			o.setDelFlag(0+"");
			int orderId=(int)orderIds[i].longValue();
			o.setOrderId(orderId);
			o.setInvoiceAmount(orderInvoiceAmount[i]);
			o.setRemarks(orderInvoice.getRemarks());
			o.setInvoiceHead(orderInvoice.getInvoiceHead());
			o.setInvoiceMode(orderInvoice.getInvoiceMode());
			o.setInvoiceSubject(orderInvoice.getInvoiceSubject());
			o.setInvoiceType(orderInvoice.getInvoiceType());
			o.setInvoiceCustomer(orderInvoice.getInvoiceCustomer());
			//0414 新增 update by pengfei.shang
			o.setInvoiceComingUnit(orderInvoice.getInvoiceComingUnit());
			o.setInvoiceComeFromCompany(orderInvoice.getInvoiceComeFromCompany());
			o.setOrderNum(orderNums[i]);
			if(groupCodes!=null &&groupCodes.length!=0){
				o.setGroupCode(groupCodes[i]);
			}
			o.setInvoiceNum(invoiceNum);
			o.setInvoiceCompany(UserUtils.getUser().getCompany().getId());
			o.setInvoiceCompanyName(UserUtils.getUser().getCompany().getName());
			o.setCreateBy(UserUtils.getUser());
			o.setCheckAmount(orderInvoiceAmount[i]);
			o.setVerifyStatus(0);
			o.setCreateStatus(0);
			o.setCreateDate(new Date());
			o.setOrderType(orderTypes[i]);
			o.setUuid(uuid);
			o.setReceiveStatus(0);
			// 0444
			o.setApplyInvoiceWay(orderInvoice.getApplyInvoiceWay());
			o.setReceivedPayStatus(0);
			// 0444
			// 对接肖凯，准备相应数据
			orderinvoiceDao.save(o);
			sbf.append(" 订单号："+orderNums[i]+",订单类型："+orderType+",发票申请金额："+orderInvoiceAmount[i]);
		}
//		this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, sbf.toString(), Context.log_state_add);
		return 1;
	}
	
	/**
	 * 保存申请发票信息
	 * add by  ruyi.chen
	 * add date 2015 07-14
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Integer saveApplyReceiptInfos(OrderInvoiceVO orderInvoice,String orderType){
		Long[] orderIds = orderInvoice.getOrderId();
		BigDecimal[] orderInvoiceAmount = orderInvoice.getOrderInvoiceAmount();
		String[]orderNums = orderInvoice.getOrderNum();
		String[]groupCodes = orderInvoice.getGroupCode();
		Integer[]orderTypes = orderInvoice.getOrderTypes();
		String invoiceNum = orderinvoiceService.createVirtualInvoiceNum();
		String uuid = UuidUtils.generUuid();
		StringBuffer sbf = new StringBuffer();
		sbf.append("保存申请开收据：");
		for(int i = 0;i < orderIds.length;i++){
			//组建保存发票信息实体
			OrderReceipt o = new OrderReceipt();
			o.setDelFlag(0+"");
			int orderId=(int)orderIds[i].longValue();
			o.setOrderId(orderId);
			o.setInvoiceAmount(orderInvoiceAmount[i]);
			o.setRemarks(orderInvoice.getRemarks());
			o.setInvoiceHead(orderInvoice.getInvoiceHead());
			o.setInvoiceMode(orderInvoice.getInvoiceMode());
			o.setInvoiceSubject(orderInvoice.getInvoiceSubject());
			o.setInvoiceType(orderInvoice.getInvoiceType());
			o.setInvoiceCustomer(orderInvoice.getInvoiceCustomer());
			o.setOrderNum(orderNums[i]);
			o.setGroupCode(groupCodes[i]);
			o.setInvoiceNum(invoiceNum);
			o.setInvoiceCompany(UserUtils.getUser().getCompany().getId());
			o.setInvoiceCompanyName(UserUtils.getUser().getCompany().getName());
			o.setCreateBy(UserUtils.getUser());
			o.setCheckAmount(orderInvoiceAmount[i]);
			o.setVerifyStatus(0);
			o.setCreateStatus(0);
			o.setCreateDate(new Date());
			o.setOrderType(orderTypes[i]);
			o.setUuid(uuid);
			o.setReceiveStatus(0);
			// 对接肖凯，准备相应数据
			orderReceiptDao.save(o);
			sbf.append(" 订单号："+orderNums[i]+",订单类型："+orderType+",收据申请金额："+orderInvoiceAmount[i]);
		}
//		this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, sbf.toString(), Context.log_state_add);
		return 1;
	}

	
	/**获取机票订单信息、已开发票信息、退款信息、应付金额、实收金额、达帐金额信息等  
	 * create by ruyi.chen
	 * create date  2014 12-03
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getAirOrderInvoiceSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT AA.*, IFNULL(BB.refundTotalStr, '￥0.00') AS refundTotalStr,IFNULL(BB.refundTotal, '￥0.00') AS refundTotal,")
		.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(")
		.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,A.person_num,A.salerId,A.startTime,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,")
		.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))")
		.append("as invoiceAmount from(SELECT a.id,a.person_num,a.salerId,a.startTime,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal ")
		.append(" ,SUM(m.amount * c.currency_exchangerate) AS totalMoney from")
		.append("(select p.id,p.person_num,p.salerId,DATE_FORMAT(flight.startTime,'%Y-%m-%d %H:%i:%s') AS startTime,p.total_money from airticket_order p INNER JOIN activity_airticket air ")
		.append(" on p.airticket_id=air.id INNER JOIN activity_flight_info flight on air.id=flight.airticketId and flight.number=1 ")
		.append(" and  "+checkSql+")a LEFT JOIN money_amount m ")
		.append(" on a.total_money=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.total_money")
		.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ")
		.append("(select p.id,p.payed_money from airticket_order p where "+checkSql+")a LEFT JOIN money_amount m ")
		.append(" on a.payed_money=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.payed_money ")
		.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ")
		.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money ")
		.append(" from airticket_order p  where "+checkSql+")a LEFT JOIN money_amount m on a.accounted_money=m.serialNum ")
		.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.accounted_money)C on A.id=C.id LEFT JOIN(")
		//查询关联机票订单信息
		.append(" select p.id,p.order_no as orderNum,p.group_code as groupCode,DATE_FORMAT(p.create_date,'%Y-%m-%d %H:%i:%s')as createDate ")
		.append(" from airticket_order p where "+checkSql+")D")
		.append(" on A.id =D.id LEFT JOIN orderinvoice b on D.orderNum =b.orderNum and b.createStatus in (1, 2) group by D.id ")
		
		.append(")AA LEFT JOIN (SELECT A.orderId,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,")
		.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ")
		.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.orderId from (")
		.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,mac.orderType,mac.uid as orderId from money_amount mac,airticket_order p")
		.append("  where mac.uid = p.id AND mac.businessType=1  and mac."+orderType+"  and  "+checkSql+payType)
		.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.orderId ")
		.append(")BB on AA.id=BB.orderId order by AA.id asc ");
		return sbf.toString();
	}
	/**获取签证订单信息、已开发票信息、退款信息、应付金额、实收金额、达帐金额信息等  
	 * create by ruyi.chen
	 * create date  2014 12-03
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getVisaOrderInvoiceSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT AA.*, IFNULL(BB.refundTotalStr, '￥0.00') AS refundTotalStr,IFNULL(BB.refundTotal, '￥0.00') AS refundTotal,")
		.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(")
		.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,A.person_num,A.salerId,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,")
		.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))")
		.append("as invoiceAmount from(SELECT a.id,a.person_num,a.salerId,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal, ")
		.append("SUM(m.amount * c.currency_exchangerate) AS totalMoney from")
		.append("(select p.id,p.travel_num as person_num,p.total_money,p.salerId from visa_order p where "+checkSql+")a LEFT JOIN money_amount m ")
		.append(" on a.total_money=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.total_money")
		.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ")
		.append("(select p.id,p.payed_money from visa_order p where "+checkSql+")a LEFT JOIN money_amount m ")
		.append(" on a.payed_money=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.payed_money ")
		.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ")
		.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money ")
		.append(" from visa_order p  where "+checkSql+")a LEFT JOIN money_amount m on a.accounted_money=m.serialNum ")
		.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.accounted_money)C on A.id=C.id LEFT JOIN(")
		//查询关联机票订单信息
		.append(" select p.id,p.order_no as orderNum,vp.groupCode as groupCode,DATE_FORMAT(p.create_date,'%Y-%m-%d %H:%i:%s')as createDate ")
		.append(" from visa_order p, visa_products vp where "+checkSql+" AND p.visa_product_id = vp.id)D")
		.append(" on A.id =D.id LEFT JOIN orderinvoice b on D.orderNum =b.orderNum and b.createStatus in (1, 2) group by D.id ")
		
		.append(")AA LEFT JOIN (SELECT A.orderId,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,")
		.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ")
		.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.orderId from (")
		.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,mac.orderType,mac.uid as orderId from money_amount mac,visa_order p ")
		.append("  where mac.uid = p.id AND mac.businessType=1  and mac."+orderType+"  and  "+checkSql+payType)
		.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.orderId ")
		.append(")BB on AA.id=BB.orderId order by AA.id asc ");
		return sbf.toString();
	}
	/**获取参团订单信息、已开发票信息、退款信息、应付金额、实收金额、达帐金额信息等  
	 * create by ruyi.chen
	 * create date  2014 11-28
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getOrderInvoiceSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		//新增退款部分计算内容 开始
		sbf.append("SELECT AA.*,IFNULL(BB.refundTotalStr,'￥0.00') as refundTotalStr,IFNULL(BB.refundTotal,'￥0.00')as refundTotal,");
		sbf.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(");		
		//新增退款部分计算内容 结束
		sbf.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,A.salerId,");
		sbf.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))");
																																					//--- -IFNULL(ma.amount,0) 538需求增的 为了减去应收金额中的差额
		sbf.append("as invoiceAmount from(SELECT a.id,a.salerId,group_concat(c.currency_mark,format(IFNULL(m.amount,0)-IFNULL(ma.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal ");
		sbf.append(", SUM(m.amount * c.currency_exchangerate) AS totalMoney from ");
		                                                                                                    //---p.differenceMoney 538需求增的 为了减去应收金额中的差额
		sbf.append("(select p.id,p.salerId,p.total_money as orderyTotal,p.differenceMoney from productorder p where "+checkSql+")a LEFT JOIN money_amount m ");
																			//--- LEFT JOIN money_amount ma ON a.differenceMoney = ma.serialNum  538需求增的 为了减去应收金额中的差额
		sbf.append(" on a.orderyTotal=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id LEFT JOIN money_amount ma ON a.differenceMoney = ma.serialNum" +
							" GROUP BY a.orderyTotal");
		sbf.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ");
		sbf.append("(select p.id,p.payed_money as alreadyPaid from productorder p where "+checkSql+")a LEFT JOIN money_amount m ");
		sbf.append(" on a.alreadyPaid=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.alreadyPaid ");
		sbf.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ");
		sbf.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money as totalAsAcount ");
		sbf.append(" from productorder p  where "+checkSql+")a LEFT JOIN money_amount m on a.totalAsAcount=m.serialNum ");
		sbf.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.totalAsAcount)C on A.id=C.id LEFT JOIN(");
		sbf.append(" select p.productId,p.productGroupId,p.orderNum,DATE_FORMAT(p.orderTime,'%Y-%m-%d %H:%i:%s')as orderTime,");
		sbf.append(" p.orderPersonNum,p.orderStatus,p.id,ap.groupCode,DATE_FORMAT(ap.groupOpenDate,'%Y-%c-%d')as groupOpenDate ");
		sbf.append(" from productorder p,activitygroup ap where p.productGroupId=ap.id and "+checkSql+")D");
		sbf.append(" on A.id =D.id LEFT JOIN orderinvoice b on D.orderNum =b.orderNum and b.createStatus in (1, 2) and b."+orderType+" group by D.id ");
		
		sbf.append(")AA LEFT JOIN (SELECT A.orderId,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,");
		sbf.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ");
		sbf.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.orderId from (");
		sbf.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,mac.orderType,mac.uid as orderId from money_amount mac,productorder p");
		sbf.append("  where mac.uid = p.id AND mac.businessType=1  and mac."+orderType+"  and  "+checkSql+payType);
		sbf.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.orderId ");
		sbf.append(")BB on AA.id=BB.orderId order by AA.id asc ");
		return sbf.toString();
	}
	
	/**获取海岛游参团订单信息、已开发票信息、退款信息、应付金额、实收金额、达帐金额信息等  
	 * create by ruyi.chen
	 * create date  2015 07-14
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getIslandOrderInvoiceSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		//新增退款部分计算内容 开始
		sbf.append("SELECT AA.*,IFNULL(BB.refundTotalStr,'￥0.00') as refundTotalStr,IFNULL(BB.refundTotal,'￥0.00')as refundTotal,");
		sbf.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(");		
		//新增退款部分计算内容 结束
		sbf.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,A.salerId,");
		sbf.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))");
		sbf.append("as invoiceAmount from(SELECT a.id,a.salerId,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal ");
		sbf.append(", SUM(m.amount * c.currency_exchangerate) AS totalMoney from ");
		sbf.append("(select p.id,p.salerId,p.total_money as orderyTotal from island_order p where "+checkSql+")a LEFT JOIN island_money_amount m ");
		sbf.append(" on a.orderyTotal=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.orderyTotal");
		sbf.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ");
		sbf.append("(select p.id,p.payed_money as alreadyPaid from island_order p where "+checkSql+")a LEFT JOIN island_money_amount m ");
		sbf.append(" on a.alreadyPaid=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.alreadyPaid ");
		sbf.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ");
		sbf.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money as totalAsAcount ");
		sbf.append(" from island_order p  where "+checkSql+")a LEFT JOIN island_money_amount m on a.totalAsAcount=m.serialNum ");
		sbf.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.totalAsAcount)C on A.id=C.id LEFT JOIN(");
		sbf.append(" select p.productId,p.productGroupId,p.orderNum,DATE_FORMAT(p.orderTime,'%Y-%m-%d %H:%i:%s')as orderTime,");
		sbf.append(" p.orderPersonNum,p.orderStatus,p.id,ap.groupCode,DATE_FORMAT(ap.groupOpenDate,'%Y-%c-%d')as groupOpenDate ");
		sbf.append(" from island_order p,activity_island_group ap where p.productGroupId=ap.id and "+checkSql+")D");
		sbf.append(" on A.id =D.id LEFT JOIN orderinvoice b on D.orderNum =b.orderNum and b.createStatus in (1, 2) and b."+orderType+" group by D.id ");
		
		sbf.append(")AA LEFT JOIN (SELECT A.orderId,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,");
		sbf.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ");
		sbf.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.orderId from (");
		sbf.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,mac.orderType,mac.uid as orderId from island_money_amount mac,island_order p");
		sbf.append("  where mac.uid = p.id AND mac.businessType=1  and mac."+orderType+"  and  "+checkSql+payType);
		sbf.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.orderId ");
		sbf.append(")BB on AA.id=BB.orderId order by AA.id asc ");
		return sbf.toString();
	}
	/**获取酒店参团订单信息、已开发票信息、退款信息、应付金额、实收金额、达帐金额信息等  
	 * create by ruyi.chen
	 * create date  2015 07-14
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getHotelOrderInvoiceSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		//新增退款部分计算内容 开始
				sbf.append("SELECT AA.*,IFNULL(BB.refundTotalStr,'￥0.00') as refundTotalStr,IFNULL(BB.refundTotal,'￥0.00')as refundTotal,");
				sbf.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(");		
				//新增退款部分计算内容 结束
				sbf.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,A.salerId,");
				sbf.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))");
				sbf.append("as invoiceAmount from(SELECT a.id,a.salerId,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal ");
				sbf.append(", SUM(m.amount * c.currency_exchangerate) AS totalMoney from ");
				sbf.append("(select p.id,p.salerId,p.total_money as orderyTotal from hotel_order p where "+checkSql+")a LEFT JOIN hotel_money_amount m ");
				sbf.append(" on a.orderyTotal=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.orderyTotal");
				sbf.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ");
				sbf.append("(select p.id,p.payed_money as alreadyPaid from hotel_order p where "+checkSql+")a LEFT JOIN hotel_money_amount m ");
				sbf.append(" on a.alreadyPaid=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.alreadyPaid ");
				sbf.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ");
				sbf.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money as totalAsAcount ");
				sbf.append(" from hotel_order p  where "+checkSql+")a LEFT JOIN hotel_money_amount m on a.totalAsAcount=m.serialNum ");
				sbf.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.totalAsAcount)C on A.id=C.id LEFT JOIN(");
				sbf.append(" select p.uuid, p.activity_hotel_uuid,p.activity_hotel_group_uuid,p.orderNum,'11' as orderType,DATE_FORMAT(p.orderTime,'%Y-%m-%d %H:%i:%s')as orderTime,");
				sbf.append(" p.orderPersonNum,p.orderStatus,p.id,ap.groupCode,DATE_FORMAT(ap.groupOpenDate,'%Y-%c-%d')as groupOpenDate ");
				sbf.append(" from hotel_order p,activity_hotel_group ap where p.activity_hotel_group_uuid = ap.uuid and "+checkSql+")D");
				sbf.append(" on A.id =D.id LEFT JOIN orderinvoice b on D.orderNum =b.orderNum and b.createStatus in (1, 2) and b."+orderType+" group by D.uuid ");
				
				sbf.append(")AA LEFT JOIN (SELECT A.uuid,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,");
				sbf.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ");
				sbf.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.uuid from (");
				sbf.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,'11' as orderType,mac.business_uuid AS uuid from hotel_money_amount mac,hotel_order p");
				sbf.append("  where mac.business_uuid = p.uuid AND mac.businessType=1  and  "+checkSql+payType);
				sbf.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.uuid ");
				sbf.append(")BB on AA.uuid=BB.uuid order by AA.uuid asc ");
		return sbf.toString();
	}
	/**
	 * create by ruyi.chen 
	 * add date 2014-12-01
	 * 转换金额总数
	 * @param moneyStr
	 * @return
	 */
	private  static String getMoneyStr(String moneyStr){
		moneyStr = moneyStr.replaceAll("-", "del");
		moneyStr = moneyStr.replaceAll("money,", "add");
		moneyStr = moneyStr.replaceAll("adddel", "-");
		moneyStr = moneyStr.replaceAll("add", "+");
		moneyStr = moneyStr.replaceAll("money", "");
		moneyStr = moneyStr.replaceAll("del", "-");
		return moneyStr;
	}
	/**
	 * create by ruyi.chen 
	 * add date 2014-12-01
	 * 根据orderId获取同一団期下该销售符合条件的订单信息
	 * @param orderId
	 * @return
	 */
	private List<Map<String,Object>> getGroupOrdersByOrderId(Long orderId,String orderType,String salerId){
		
		StringBuffer sbf=new StringBuffer();
		switch(orderType){
			case Context.ORDER_STATUS_VISA:
				//签证产品时，按签证产品查找符合条件的签证订单
			sbf
			.append("select p.id as orderId from visa_order p ")
			.append(" where p.visa_product_id=(select visa_product_id from visa_order where id=?)and p.salerId=?");
			break;
			case Context.ORDER_STATUS_AIR_TICKET:
				//机票产品时，按机票产品查找符合条件的机票订单
			sbf
			.append("select p.id as orderId from airticket_order p ")
			.append(" where p.airticket_id=(select airticket_id from airticket_order where id=?)and p.salerId=?");
			break;
			case Context.ORDER_STATUS_HOTEL:
				//机票产品时，按机票产品查找符合条件的机票订单
			sbf
			.append("select p.id as orderId from hotel_order p ")
			.append(" where p.activity_hotel_group_uuid=(select activity_hotel_group_uuid from hotel_order where id=?)and p.salerId=?");
			break;
			case Context.ORDER_STATUS_ISLAND:
				//海岛游产品时，按海岛游产品查找符合条件的海岛游订单
			sbf
			.append("select p.id as orderId from island_order p ")
			.append(" where p.activity_island_group_uuid=(select activity_island_group_uuid from island_order where id=?)and p.salerId=?");
			break;
			default:
				//默认为订单类型，单团、大客户、自由行、游学等
			sbf
			.append("select p.id as orderId,p.orderNum,p.productGroupId  from productorder p ")
			.append(" where p.productGroupId=(select productGroupId from productorder where id=?)and p.salerId=?");
			break;			
		}
		return orderinvoiceDao.findBySql(sbf.toString(), Map.class,orderId,salerId);
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-01-16
	 * update by jianning.gao
	 * update date 2015-07-14
	 * 2015-0714 新增，只有已付金额大于0时，才允许开发票
	 * 发票列表部分判断是否有申请发票功能
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public Integer getApplyInvoiceCheck(Long orderId,String orderType){
		Integer sign = 0;
		switch(orderType){
		case Context.ORDER_STATUS_VISA:
			VisaOrder vOrder = visaOrderDao.findOne(orderId);
			if(null != vOrder&&vOrder.getId() > 0){
				if(UserUtils.getUser().getId().longValue()  == vOrder.getCreateBy().getId().longValue()|| UserUtils.getUser().getId().longValue()  == vOrder.getSalerId().longValue()){
//					// 获取订单已付金额
//					String payed =vOrder.getPayedMoney();
//					BigDecimal money = moneyAmountService.getMoneyMath(payed);
//					int r = money.compareTo(BigDecimal.ZERO);
//					if(r==1){
//						sign = 1;
//					}
					sign = 1;
				}
				
			}
			break;
		case Context.ORDER_STATUS_AIR_TICKET:
			AirticketOrder order = airticketOrderDao.getAirticketOrderById(orderId);
			if(null != order&&order.getId() > 0){
				if(UserUtils.getUser().getId().longValue()  == order.getCreateBy().getId().longValue()|| UserUtils.getUser().getId().longValue()  == order.getSalerId().longValue()){
					// 获取订单已付金额
//					String payed =order.getPayedMoney();
//					BigDecimal money = moneyAmountService.getMoneyMath(payed);
//					int r = money.compareTo(BigDecimal.ZERO);
//					if(r==1){
//						sign = 1;
//					}
					sign = 1;
				}
				
			}
			break;
		case Context.ORDER_STATUS_ISLAND:
			IslandOrder islandOrder = islandOrderDao.getById(orderId);
			if(null != islandOrder&&islandOrder.getId() > 0){
				if(UserUtils.getUser().getId().longValue()  == islandOrder.getCreateBy().longValue()){
					// 获取订单已付金额
//					String payed =islandOrder.getPayedMoney();
//					BigDecimal money = moneyAmountService.getMoneyMath(payed);
//					int r = money.compareTo(BigDecimal.ZERO);
//					if(r==1){
//						sign = 1;
//					}
					sign = 1;
				}
			}
			break;
			
		default:
			ProductOrderCommon porder = productorderDao.findOne(orderId);
			if(null != porder&&porder.getId() > 0){
				if(UserUtils.getUser().getId().longValue()  == porder.getCreateBy().getId().longValue()|| UserUtils.getUser().getId().longValue()  == porder.getSalerId().longValue()){
					// 获取订单已付金额
//					String payed =porder.getPayedMoney();
//					BigDecimal money = moneyAmountService.getMoneyMath(payed);
//					int r = money.compareTo(BigDecimal.ZERO);
//					if(r==1){
//						sign = 1;
//					}
					sign = 1;
				}
			}
			break;
		}
		
		return sign;
	}
	
	//合开收据相关sql装配
	
	
	/**获取机票订单信息、已开收据信息、退款信息、应付金额、实收金额、达帐金额信息等  (开收据)
	 * create by ruyi.chen
	 * create date  2014 12-03
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getAirOrderReceiptSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT AA.*, IFNULL(BB.refundTotalStr, '￥0.00') AS refundTotalStr,IFNULL(BB.refundTotal, '￥0.00') AS refundTotal,")
		.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(")
		.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,A.person_num,A.salerId,A.startTime,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,")
		.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))")
		.append("as invoiceAmount from(SELECT a.id,a.person_num,a.salerId,a.startTime,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal ")
		.append(" ,SUM(m.amount * c.currency_exchangerate) AS totalMoney from")
		.append("(select p.id,p.person_num,p.salerId,DATE_FORMAT(flight.startTime,'%Y-%m-%d %H:%i:%s') AS startTime,p.total_money from airticket_order p INNER JOIN activity_airticket air ")
		.append(" on p.airticket_id=air.id INNER JOIN activity_flight_info flight on air.id=flight.airticketId and flight.number=1 ")
		.append(" and  "+checkSql+")a LEFT JOIN money_amount m ")
		.append(" on a.total_money=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.total_money")
		.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ")
		.append("(select p.id,p.payed_money from airticket_order p where "+checkSql+")a LEFT JOIN money_amount m ")
		.append(" on a.payed_money=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.payed_money ")
		.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ")
		.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money ")
		.append(" from airticket_order p  where "+checkSql+")a LEFT JOIN money_amount m on a.accounted_money=m.serialNum ")
		.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.accounted_money)C on A.id=C.id LEFT JOIN(")
		//查询关联机票订单信息
		.append(" select p.id,p.order_no as orderNum,p.group_code as groupCode,DATE_FORMAT(p.create_date,'%Y-%m-%d %H:%i:%s')as createDate ")
		.append(" from airticket_order p where "+checkSql+")D")
		.append(" on A.id =D.id LEFT JOIN orderreceipt b on D.orderNum =b.orderNum and b.createStatus in (1, 2) group by D.id ")
		
		.append(")AA LEFT JOIN (SELECT A.orderId,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,")
		.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ")
		.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.orderId from (")
		.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,mac.orderType,mac.uid as orderId from money_amount mac,airticket_order p")
		.append("  where mac.uid = p.id AND mac.businessType=1  and mac."+orderType+"  and  "+checkSql+payType)
		.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.orderId ")
		.append(")BB on AA.id=BB.orderId order by AA.id asc ");
		return sbf.toString();
	}
	/**获取签证订单信息、已开收据信息、退款信息、应付金额、实收金额、达帐金额信息等  (开收据)
	 * create by ruyi.chen
	 * create date  2014 12-03
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getVisaOrderReceiptSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT AA.*, IFNULL(BB.refundTotalStr, '￥0.00') AS refundTotalStr,IFNULL(BB.refundTotal, '￥0.00') AS refundTotal,")
		.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(")
		.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,A.person_num,A.salerId,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,")
		.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))")
		.append("as invoiceAmount from(SELECT a.id,a.person_num,a.salerId,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal, ")
		.append("SUM(m.amount * c.currency_exchangerate) AS totalMoney from")
		.append("(select p.id,p.travel_num as person_num,p.salerId,p.total_money from visa_order p where "+checkSql+")a LEFT JOIN money_amount m ")
		.append(" on a.total_money=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.total_money")
		.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ")
		.append("(select p.id,p.payed_money from visa_order p where "+checkSql+")a LEFT JOIN money_amount m ")
		.append(" on a.payed_money=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.payed_money ")
		.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ")
		.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money ")
		.append(" from visa_order p  where "+checkSql+")a LEFT JOIN money_amount m on a.accounted_money=m.serialNum ")
		.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.accounted_money)C on A.id=C.id LEFT JOIN(")
		//查询关联机票订单信息
		.append(" select p.id,p.order_no as orderNum,p.group_code as groupCode,DATE_FORMAT(p.create_date,'%Y-%m-%d %H:%i:%s')as createDate ")
		.append(" from visa_order p where "+checkSql+")D")
		.append(" on A.id =D.id LEFT JOIN orderreceipt b on D.orderNum =b.orderNum and b.createStatus in (1, 2) group by D.id ")
		
		.append(")AA LEFT JOIN (SELECT A.orderId,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,")
		.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ")
		.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.orderId from (")
		.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,mac.orderType,mac.uid as orderId from money_amount mac,visa_order p ")
		.append("  where mac.uid = p.id AND mac.businessType=1  and mac."+orderType+"  and  "+checkSql+payType)
		.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.orderId ")
		.append(")BB on AA.id=BB.orderId order by AA.id asc ");
		return sbf.toString();
	}
	/**获取参团订单信息、已开收据信息、退款信息、应付金额、实收金额、达帐金额信息等  (开收据)
	 * create by ruyi.chen
	 * create date  2014 11-28
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getOrderReceiptSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		//新增退款部分计算内容 开始
		sbf.append("SELECT AA.*,IFNULL(BB.refundTotalStr,'￥0.00') as refundTotalStr,IFNULL(BB.refundTotal,'￥0.00')as refundTotal,");
		sbf.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(");		
		//新增退款部分计算内容 结束
		sbf.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,A.salerId,");
		sbf.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))");
		sbf.append("as invoiceAmount from(SELECT a.id,a.salerId,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal ");
		sbf.append(", SUM(m.amount * c.currency_exchangerate) AS totalMoney from ");
		sbf.append("(select p.id,p.salerId,p.total_money as orderyTotal from productorder p where "+checkSql+")a LEFT JOIN money_amount m ");
		sbf.append(" on a.orderyTotal=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.orderyTotal");
		sbf.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ");
		sbf.append("(select p.id,p.payed_money as alreadyPaid from productorder p where "+checkSql+")a LEFT JOIN money_amount m ");
		sbf.append(" on a.alreadyPaid=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.alreadyPaid ");
		sbf.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ");
		sbf.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money as totalAsAcount ");
		sbf.append(" from productorder p  where "+checkSql+")a LEFT JOIN money_amount m on a.totalAsAcount=m.serialNum ");
		sbf.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.totalAsAcount)C on A.id=C.id LEFT JOIN(");
		sbf.append(" select p.productId,p.productGroupId,p.orderNum,DATE_FORMAT(p.orderTime,'%Y-%m-%d %H:%i:%s')as orderTime,");
		sbf.append(" p.orderPersonNum,p.orderStatus,p.id,ap.groupCode,DATE_FORMAT(ap.groupOpenDate,'%Y-%c-%d')as groupOpenDate ");
		sbf.append(" from productorder p,activitygroup ap where p.productGroupId=ap.id and "+checkSql+")D");
		sbf.append(" on A.id =D.id LEFT JOIN orderreceipt b on D.orderNum =b.orderNum and b.createStatus in (1, 2) and b."+orderType+" group by D.id ");
		
		sbf.append(")AA LEFT JOIN (SELECT A.orderId,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,");
		sbf.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ");
		sbf.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.orderId from (");
		sbf.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,mac.orderType,mac.uid as orderId from money_amount mac,productorder p");
		sbf.append("  where mac.uid = p.id AND mac.businessType=1  and mac."+orderType+"  and  "+checkSql+payType);
		sbf.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.orderId ");
		sbf.append(")BB on AA.id=BB.orderId order by AA.id asc ");
		return sbf.toString();
	}
	
	/**获取海岛游参团订单信息、已开收据信息、退款信息、应付金额、实收金额、达帐金额信息等  (开收据)
	 * create by ruyi.chen
	 * create date  2015 07-14
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getIslandOrderReceiptSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		//新增退款部分计算内容 开始
		sbf.append("SELECT AA.*,IFNULL(BB.refundTotalStr,'￥0.00') as refundTotalStr,IFNULL(BB.refundTotal,'￥0.00')as refundTotal,");
		sbf.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(");		
		//新增退款部分计算内容 结束
		sbf.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,A.salerId,");
		sbf.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))");
		sbf.append("as invoiceAmount from(SELECT a.id,a.salerId,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal ");
		sbf.append(", SUM(m.amount * c.currency_exchangerate) AS totalMoney from ");
		sbf.append("(select p.id,p.salerId,p.total_money as orderyTotal from island_order p where "+checkSql+")a LEFT JOIN island_money_amount m ");
		sbf.append(" on a.orderyTotal=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.orderyTotal");
		sbf.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ");
		sbf.append("(select p.id,p.payed_money as alreadyPaid from island_order p where "+checkSql+")a LEFT JOIN island_money_amount m ");
		sbf.append(" on a.alreadyPaid=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.alreadyPaid ");
		sbf.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ");
		sbf.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money as totalAsAcount ");
		sbf.append(" from island_order p  where "+checkSql+")a LEFT JOIN island_money_amount m on a.totalAsAcount=m.serialNum ");
		sbf.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.totalAsAcount)C on A.id=C.id LEFT JOIN(");
		sbf.append(" select p.uuid, p.activity_island_uuid,p.activity_island_group_uuid,p.orderNum,'12' as orderType,DATE_FORMAT(p.orderTime,'%Y-%m-%d %H:%i:%s')as orderTime,");
		sbf.append(" p.orderPersonNum,p.orderStatus,p.id,ap.groupCode,DATE_FORMAT(ap.groupOpenDate,'%Y-%c-%d')as groupOpenDate ");
		sbf.append(" from island_order p,activity_island_group ap where p.activity_island_group_uuid = ap.uuid and "+checkSql+")D");
		sbf.append(" on A.id =D.id LEFT JOIN orderreceipt b on D.orderNum =b.orderNum and b.createStatus in (1, 2) and b."+orderType+" group by D.uuid ");
		
		sbf.append(")AA LEFT JOIN (SELECT A.uuid,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,");
		sbf.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ");
		sbf.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.uuid from (");
		sbf.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,'12' as orderType,mac.business_uuid AS uuid from island_money_amount mac,island_order p");
		sbf.append("  where mac.business_uuid = p.uuid AND mac.businessType=1  and  "+checkSql+payType);
		sbf.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.uuid ");
		sbf.append(")BB on AA.uuid=BB.uuid order by AA.uuid asc ");
		return sbf.toString();
	}
	/**获取酒店参团订单信息、已开收据信息、退款信息、应付金额、实收金额、达帐金额信息等  (开收据)
	 * create by ruyi.chen
	 * create date  2015 07-14
	 * @version 1.1
	 * @param checkSql,payType
	 * @return
	 */
	private String getHotelOrderReceiptSelectSql(String checkSql,String payType,String orderType){
		StringBuffer sbf = new StringBuffer();
		//新增退款部分计算内容 开始
				sbf.append("SELECT AA.*,IFNULL(BB.refundTotalStr,'￥0.00') as refundTotalStr,IFNULL(BB.refundTotal,'￥0.00')as refundTotal,");
				sbf.append("format(IFNULL(convert((IFNULL(AA.totalMoney,0)-IFNULL(AA.invoiceAmount,0)),DECIMAL(11,2)),'0.00'),2)as refundableAmount,format(invoiceAmount,2)as invoiceAmountStr from(");		
				//新增退款部分计算内容 结束
				sbf.append("SELECT D.*,IFNULL(A.orderyTotal,'￥0.00')as orderyTotal,IFNULL(B.alreadyPaid,'￥0.00')as alreadyPaid,A.salerId,");
				sbf.append(" IFNULL(C.totalAsAcount,'￥0.00')as totalAsAcount,IFNULL(convert(C.totalMoney,DECIMAL(11,2)),0)as totalMoney,sum(IFNULL(b.invoiceAmount,0))");
				sbf.append("as invoiceAmount from(SELECT a.id,a.salerId,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as orderyTotal ");
				sbf.append(", SUM(m.amount * c.currency_exchangerate) AS totalMoney from ");
				sbf.append("(select p.id,p.salerId,p.total_money as orderyTotal from hotel_order p where "+checkSql+")a LEFT JOIN hotel_money_amount m ");
				sbf.append(" on a.orderyTotal=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.orderyTotal");
				sbf.append(") A LEFT JOIN(SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY c.currency_id separator '+')as alreadyPaid from ");
				sbf.append("(select p.id,p.payed_money as alreadyPaid from hotel_order p where "+checkSql+")a LEFT JOIN hotel_money_amount m ");
				sbf.append(" on a.alreadyPaid=m.serialNum LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.alreadyPaid ");
				sbf.append(")B on A.id=B.id LEFT JOIN (SELECT a.id,group_concat(c.currency_mark,format(IFNULL(m.amount,0),2) ORDER BY ");
				sbf.append(" c.currency_id separator '+')as totalAsAcount,SUM(m.amount*c.currency_exchangerate)as totalMoney from (select p.id,p.accounted_money as totalAsAcount ");
				sbf.append(" from hotel_order p  where "+checkSql+")a LEFT JOIN hotel_money_amount m on a.totalAsAcount=m.serialNum ");
				sbf.append(" LEFT JOIN currency c on m.currencyId=c.currency_id GROUP BY a.totalAsAcount)C on A.id=C.id LEFT JOIN(");
				sbf.append(" select p.uuid, p.activity_hotel_uuid,p.activity_hotel_group_uuid,p.orderNum,'11' as orderType,DATE_FORMAT(p.orderTime,'%Y-%m-%d %H:%i:%s')as orderTime,");
				sbf.append(" p.orderPersonNum,p.orderStatus,p.id,ap.groupCode,DATE_FORMAT(ap.groupOpenDate,'%Y-%c-%d')as groupOpenDate ");
				sbf.append(" from hotel_order p,activity_hotel_group ap where p.activity_hotel_group_uuid = ap.uuid and "+checkSql+")D");
				sbf.append(" on A.id =D.id LEFT JOIN orderreceipt b on D.orderNum =b.orderNum and b.createStatus in (1, 2) and b."+orderType+" group by D.uuid ");
				
				sbf.append(")AA LEFT JOIN (SELECT A.uuid,group_concat(A.currency_mark,format(IFNULL(A.totalMoney,0),2) ORDER BY A.currencyId separator '+')as refundTotalStr,");
				sbf.append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0)as refundTotal  from ");
				sbf.append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_mark,c.currency_exchangerate,a.uuid from (");
				sbf.append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,'11' as orderType,mac.business_uuid AS uuid from hotel_money_amount mac,hotel_order p");
				sbf.append("  where mac.business_uuid = p.uuid AND mac.businessType=1  and  "+checkSql+payType);
				sbf.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.uuid ");
				sbf.append(")BB on AA.uuid=BB.uuid order by AA.uuid asc ");
		return sbf.toString();
	}
	/**
	 * 判断订单创建人和系统登录人是否一致
	 * @param orderNum
	 * @author xudong.he
	 * @return
	 */
	public boolean validateOrder(String orderNum, String orderType)
	{
		Long userId = UserUtils.getUser().getId();
		StringBuffer buffer  = new StringBuffer();
		buffer.append("SELECT svav.orderCode, svav.productType,svav.productTypeName,svav.salerId,svav.salerName FROM single_visa_air_view svav where svav.orderCode=?");
		List<Map<Object, Object>> list =  orderinvoiceDao.findBySql(buffer.toString(),Map.class,orderNum);
		
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		
		Iterator<Map<Object, Object>> it = list.iterator();
		while(it.hasNext()) {
			Map<Object, Object> map = it.next();
			String productType = map.get("productType").toString();
			if (!orderType.equals(productType)) {
				it.remove();
			}
		}
		
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		
		String productType = list.get(0).get("productType").toString();
		if("1".equals(productType)||"2".equals(productType)||"3".equals(productType)||"4".equals(productType)||"5".equals(productType)||"10".equals(productType))
		{
			
			List<?> listPro =  orderinvoiceDao.findBySql("SELECT p.createBy FROM productorder p WHERE p.orderNum=? AND p.createBy=?",Map.class,orderNum,userId);
			if(listPro.size()>0)
				return true;
			else
				return false;
		 }
		if("6".equals(productType))
		{
			List<?> listVisa =  orderinvoiceDao.findBySql("SELECT vo.create_by FROM visa_order vo WHERE vo.order_no=? AND vo.create_by=?",Map.class,orderNum,userId);
			if(listVisa.size()>0)
				return true;
			else
				return false;
		 }
		if("7".equals(productType))
		{
			List<?> listVisa =  orderinvoiceDao.findBySql("SELECT ao.create_by FROM airticket_order ao WHERE ao.order_no=? AND ao.create_by=?",Map.class,orderNum,userId);
			if(listVisa.size()>0)
				return true;
			else
				return false;
		 }
		if("11".equals(productType))
		{
			List<?> listVisa =  orderinvoiceDao.findBySql("SELECT ho.createBy FROM hotel_order ho WHERE ho.orderNum=? AND ho.createBy=?",Map.class,orderNum,userId);
			if(listVisa.size()>0)
				return true;
			else
				return false;
		 }
		if("12".equals(productType))
		{
			List<?> listVisa =  orderinvoiceDao.findBySql("SELECT oo.createBy FROM island_order oo WHERE oo.orderNum=? AND oo.createBy=?",Map.class,orderNum,userId);
			if(listVisa.size()>0)
				return true;
			else
				return false;
		 }
		return false;
	}
	/**
	 * 判断订单是否已删除或已取消
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public boolean checkOrder(String orderId,String orderType){
		if("1".equals(orderType)||"2".equals(orderType)||"3".equals(orderType)||"4".equals(orderType)||"5".equals(orderType)||"10".equals(orderType)){
			ProductOrderCommon productOrder = productorderDao.findOne(Long.parseLong(orderId));
			if(productOrder.getDelFlag().equals("1") || productOrder.getPayStatus()==99 || productOrder.getPayStatus()==111){
				return false;
			}else{
				return true;
			}
		}
		if("6".equals(orderType)){
			VisaOrder visaOrder = visaOrderDao.findByOrderId(Long.parseLong(orderId));
			if(visaOrder.getDelFlag().equals("1") || visaOrder.getPayStatus()==99){
				return false;
			}else{
				return true;
			}
		}
		if("7".equals(orderType)){
			AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
			if(airticketOrder.getDelFlag().equals("1") || airticketOrder.getOrderState()==99 || airticketOrder.getOrderState()==111){
				return false;
			}else{
				return true;
			}
		}
		if("11".equals(orderType)){
			HotelOrder hotelOrder = hotelOrderDao.getById(orderId);
			if(hotelOrder.getDelFlag().equals("1") || hotelOrder.getOrderStatus()==99 || hotelOrder.getOrderStatus()==111){
				return false;
			}else{
				return true;
			}
		}
		if("12".equals(orderType)){
			IslandOrder islandOrder = islandOrderDao.getById(orderId);
			if(islandOrder.getDelFlag().equals("1") || islandOrder.getOrderStatus()==99 || islandOrder.getOrderStatus()==111){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	/**
	 * 获取当前订单相关信息,订单已开发票总额，订单已收金额
	 * add by  ruyi.chen
	 * add date 2014 11-17
	 * @param orderId 订单唯一标识
	 */
	private List<Map<Object,Object>> getInvoiceOrderInfoByNumForAdd(String orderNum,String orderType,boolean flag,String salerId){		
		String querySql = "";
		String orderTypeSql = "orderType='" + orderType + "' ";
		String payType = " and mac.moneyType =11 ";//款项类型，退款
		//改为多币种处理后sql语句
		String sql = "";
		String createStr = " ";
		switch(orderType){
			case Context.ORDER_STATUS_VISA:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "'  and p.payStatus<>'99' ";
				}
				querySql = " p.order_no='" + orderNum + "'"+createStr;
				sql = this.getVisaOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_AIR_TICKET:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "' and p.order_state<>'111' and p.order_state<>'99' ";
				}
				querySql = " p.order_no='" + orderNum + "'"+createStr;
				sql = this.getAirOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_HOTEL:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "' and p.orderStatus<>'3' ";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getHotelOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_ISLAND:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "' and p.orderStatus<>'3' ";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getIslandOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
			default:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "' and p.payStatus<>'111' and p.payStatus<>'99' ";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getOrderInvoiceSelectSql(querySql,payType,orderTypeSql);
				
				break;
		}
		List<Map <Object,Object>>ls = orderinvoiceDao.findBySql(sql,Map.class);
		
		return ls;
				
	}/**
	 * 获取当前订单基本信息、已开发票信息、退款信息、支付信息等
	 * add by  ruyi.chen
	 * add date 2014 11-18
	 * @param orderId 订单唯一标识
	 */
	public Map<Object,Object>getApplyInvoiceInfoForAdd(String orderNum,String orderType,String salerId){
		Map<Object,Object>m = new HashMap<Object,Object>();
		
		/**
		 * add by ruyi.chen
		 * 根据订单号获取相应全平台对应的订单类型，根据不同的订单类型加载相应的订单数据信息
		 */
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String, Object>>) visaOrderTravelerPayLogService.getOrderByorderCode(orderNum);
		if(null != list && 0 < list.size()){
			Map<String,Object> rMap = list.get(0);
			String orderTypeAdd = rMap.get("productType").toString();
			//String salerId = rMap.get("salerId").toString();
			//TODO 根据是否限制本人开发票来判断
			/**
			 * update by ruyi.chen
			 * update date 2015-10-12
			 * 根据订单类型、对应的订单状况去除删除、取消的订单，并给予回复
			 */
			String payStatus = rMap.get("payStatus").toString();
			int myOrderType = Integer.parseInt(orderTypeAdd);
			if(myOrderType < 6 || myOrderType == 10){
				if("99".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开发票！");
					return m;
				}else if("111".equals(payStatus)){
					m.put("errorMessage", "已删除订单不能开发票！");
					return m;
				}
			}else if(myOrderType == 6){
				if("99".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开发票！");
					return m;
				}
			}else if(myOrderType == 7){
				if("99".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开发票！");
					return m;
				}else if("111".equals(payStatus)){
					m.put("errorMessage", "已删除订单不能开发票！");
					return m;
				}
			}else if(myOrderType == 11){
				if("3".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开发票！");
					return m;
				}
			}else if(myOrderType == 12){
				if("3".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开发票！");
					return m;
				}
			}
			//是否限制本人   限制为true,不限制false
			boolean flag = true;
			List<Map<Object,Object>>ls = getInvoiceOrderInfoByNumForAdd(orderNum,orderTypeAdd,flag,salerId);
			if(null != ls && ls.size() > 0){
				m=ls.get(0);
				m.put("orderType", orderTypeAdd);
				m.put("orderTypeName", rMap.get("productTypeName"));
				String orderTotal = m.get("orderyTotal").toString();
				if(StringUtils.isNotBlank(orderTotal)){
					m.put("orderyTotal", getMoneyStr(orderTotal));
				}
				
				String alreadyPaid = m.get("alreadyPaid").toString();
				if(StringUtils.isNotBlank(alreadyPaid)){
					m.put("alreadyPaid", getMoneyStr(alreadyPaid));
				}
				
				String totalAsAcount = m.get("totalAsAcount").toString();
				if(StringUtils.isNotBlank(totalAsAcount)){
					m.put("totalAsAcount", getMoneyStr(totalAsAcount));
				}
				
				String refundableAmount = m.get("refundableAmount").toString();
				if(StringUtils.isNotBlank(refundableAmount)){
					m.put("refundableAmount", "￥"+refundableAmount);
				}
				String invoiceAmountStr = m.get("invoiceAmountStr").toString();
				if(StringUtils.isNotBlank(invoiceAmountStr)){
					m.put("invoiceAmount", "￥"+invoiceAmountStr);
				}
			}else{
				m = null;
			}
			
		}else{
			m.put("errorMessage", "订单不存在！");
			return m;
		}
		
		return m;		
		
	}
	
	/**
	 * 获取当前订单相关信息,订单已开收据总额，订单已收金额
	 * add by  ruyi.chen
	 * add date 2015 07-14
	 * @param orderId 订单唯一标识
	 */
	private List<Map<Object,Object>> getReceiptOrderInfoByNumForAdd(String orderNum,String orderType,boolean flag,String salerId){		
		String querySql = "";
		String orderTypeSql = "orderType='" + orderType + "' ";
		String payType = " and mac.moneyType =11 ";//款项类型，退款
		//改为多币种处理后sql语句
		String sql = "";
		String createStr = " ";
		switch(orderType){
			case Context.ORDER_STATUS_VISA:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "'  and p.payStatus<>'99' ";
				}
				querySql = " p.order_no='" + orderNum + "'"+createStr;
				sql = this.getVisaOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_AIR_TICKET:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "' and p.order_state<>'111' and p.order_state<>'99' ";
				}
				querySql = " p.order_no='" + orderNum + "'"+createStr;
				sql = this.getAirOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_HOTEL:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "' and p.orderStatus<>'3' ";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getHotelOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
			case Context.ORDER_STATUS_ISLAND:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "' and p.orderStatus<>'3' ";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getIslandOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
			default:
				if(flag){
					createStr = createStr+" and p.salerId='" + salerId + "' and p.payStatus<>'111' and p.payStatus<>'99' ";
				}
				querySql = " p.orderNum='" + orderNum + "'"+createStr;
				sql = this.getOrderReceiptSelectSql(querySql,payType,orderTypeSql);
				
				break;
		}
		List<Map <Object,Object>>ls = orderinvoiceDao.findBySql(sql,Map.class);
		
		return ls;
				
	}
	/**
	 * 获取当前订单基本信息、已开发票信息、退款信息、支付信息等
	 * add by  ruyi.chen
	 * add date 2014 11-18
	 * @param orderId 订单唯一标识
	 */
	public Map<Object,Object>getApplyReceiptInfoForAdd(String orderNum,String orderType,String salerId){
		Map<Object,Object>m = new HashMap<Object,Object>();
		/**
		 * add by ruyi.chen
		 * 根据订单号获取相应全平台对应的订单类型，根据不同的订单类型加载相应的订单数据信息
		 */
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String, Object>>) visaOrderTravelerPayLogService.getOrderByorderCode(orderNum);
		if(null != list && 0 < list.size()){
			Map<String,Object> rMap = list.get(0);
			String orderTypeAdd = rMap.get("productType").toString();
			//String salerId = rMap.get("salerId").toString();
			//TODO 根据是否限制本人开发票来判断
			/**
			 * update by ruyi.chen
			 * update date 2015-10-12
			 * 根据订单类型、对应的订单状况去除删除、取消的订单，并给予回复
			 */
			String payStatus = rMap.get("payStatus").toString();
			int myOrderType = Integer.parseInt(orderTypeAdd);
			if(myOrderType < 6 || myOrderType == 10){
				if("99".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开收据！");
					return m;
				}else if("111".equals(payStatus)){
					m.put("errorMessage", "已删除订单不能开收据！");
					return m;
				}
			}else if(myOrderType == 6){
				if("99".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开收据！");
					return m;
				}
			}else if(myOrderType == 7){
				if("99".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开收据！");
					return m;
				}else if("111".equals(payStatus)){
					m.put("errorMessage", "已删除订单不能开收据！");
					return m;
				}
			}else if(myOrderType == 11){
				if("3".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开收据！");
					return m;
				}
			}else if(myOrderType == 12){
				if("3".equals(payStatus)){
					m.put("errorMessage", "已取消订单不能开收据！");
					return m;
				}
			}
			//是否限制本人   限制为true,不限制false
			boolean flag = true;
			List<Map<Object,Object>>ls = getReceiptOrderInfoByNumForAdd(orderNum,orderTypeAdd,flag,salerId);
			if(null != ls && ls.size() > 0){
				m=ls.get(0);
				m.put("orderType", orderTypeAdd);
				m.put("orderTypeName", rMap.get("productTypeName"));
				String orderTotal = m.get("orderyTotal").toString();
				if(StringUtils.isNotBlank(orderTotal)){
					m.put("orderyTotal", getMoneyStr(orderTotal));
				}
				
				String alreadyPaid = m.get("alreadyPaid").toString();
				if(StringUtils.isNotBlank(alreadyPaid)){
					m.put("alreadyPaid", getMoneyStr(alreadyPaid));
				}
				
				String totalAsAcount = m.get("totalAsAcount").toString();
				if(StringUtils.isNotBlank(totalAsAcount)){
					m.put("totalAsAcount", getMoneyStr(totalAsAcount));
				}
				
				String refundableAmount = m.get("refundableAmount").toString();
				if(StringUtils.isNotBlank(refundableAmount)){
					m.put("refundableAmount", "￥"+refundableAmount);
				}
				String invoiceAmountStr = m.get("invoiceAmountStr").toString();
				if(StringUtils.isNotBlank(invoiceAmountStr)){
					m.put("invoiceAmount", "￥"+invoiceAmountStr);
				}
			}else{
				m = null;
			}
			
		}else{
			m.put("errorMessage", "订单不存在！");
			return m;
		}
		
		
		return m;		
		
	}
}
