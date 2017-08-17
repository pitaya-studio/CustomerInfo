package com.trekiz.admin.modules.visa.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.functions.Functions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.log.service.LogOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.order.util.ReviewCommonUtil;
import com.trekiz.admin.modules.pay.model.BatchPayModel;
import com.trekiz.admin.modules.pay.model.CurrencyAmount;
import com.trekiz.admin.modules.review.visaborrowmoney.web.VisaBorrowMoneyController;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.sys.entity.BatchRecord;
import com.trekiz.admin.modules.sys.entity.BatchTravelerRelation;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.BatchRecordDao;
import com.trekiz.admin.modules.sys.repository.BatchTravelerRelationDao;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.DictDao;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.repository.MenuDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SysBatchNoService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.repository.OrderPayRelationDao;
import com.trekiz.admin.modules.traveler.repository.OrderTravelerPayRelationDao;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.OrderPayRelation;
import com.trekiz.admin.modules.visa.entity.OrderTravelerPayRelation;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.form.OriginalProjectForm;
import com.trekiz.admin.modules.visa.form.SqlConstant;
import com.trekiz.admin.modules.visa.form.VisaOrderForm;
import com.trekiz.admin.modules.visa.form.VisaOrderResultForm;
import com.trekiz.admin.modules.visa.form.VisaOrderTravelerResultForm;
import com.trekiz.admin.modules.visa.repository.IVisaOrderListDao;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import com.trekiz.admin.modules.visa.web.VisaPreOrderController;
import com.trekiz.admin.review.borrowing.airticket.web.ActivityAirTicketOrderLendMoneyController;
import com.trekiz.admin.review.common.utils.ReviewUtils;

import freemarker.template.TemplateException;
@Service
@Transactional(readOnly = true)
public class VisaOrderService extends BaseService{
	
	private static final Logger log = Logger.getLogger(ActivityAirTicketOrderLendMoneyController.class);
	
	@Autowired
	private VisaOrderDao visaOrderDao;
	
	@Autowired
	private IVisaOrderListDao visaOrderListDao;
	
	@Autowired
	private VisaDao visaDao;
	
	@Autowired
	private DictDao dictDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private VisaProductsDao visaProductsDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	//-------wxw added -----------
	@Autowired
	private OrderContactsDao orderContactsDao;
	
	@Autowired
	private TravelerDao travelerDao;
	
	@Autowired
	private OrderPayRelationDao orderPayRelationDao;
	
	@Autowired
	private OrderTravelerPayRelationDao orderTravelerPayRelationDao;
	

	
	@Autowired
	private SysIncreaseService sysIncreaseService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
    private TravelerService travelerService;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
    private ReviewDao reviewDao;
	
	@Autowired
	private ReviewDetailDao reviewDetailDao;

	@Autowired
    private SysBatchNoService sysBatchNoService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private BatchRecordDao batchRecordDao;
	
	@Autowired
	private BatchTravelerRelationDao batchTravelerRelationDao;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private DocInfoDao docInfoDao;
	
	@Autowired
	private OrderpayDao orderpayDao;
	
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private MoneyAmountService amountService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	
	@Autowired
	private  OrderContactsDao contactsDao;
	
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
    private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;
	
	
	@Autowired
	private LogOrderService logOrderService;

	
	@Autowired
	private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;
	
	
	
	
	
	/**
	 * 通过签证订单ID查询签证订单
	 * @param visaOrderId
	 * @return
	 */
	public VisaOrder findVisaOrder(Long visaOrderId){
		VisaOrder visaOrder = null;
		if(visaOrderId!=null && visaOrderId>0){
			visaOrder = visaOrderDao.findOne(visaOrderId);
		}
		return visaOrder;
	}
	
	/**
	 * 通过签证订单ID和查询标志查询该订单下的已开发票号、已开收据号
	 * @param orderId
	 * @param flag:0 发票号;1 收据号
	 * @return
	 */
	public List<Map<String, Object>> findInvoiceOrReceiptByOrderIdAndFlag(Long orderId,Integer flag){
		List<Map<String,Object>> resultList = null;
		if(flag==0){//发票号
			String sql = "SELECT o.invoiceNum, o.uuid FROM orderinvoice o LEFT JOIN visa_order vo ON o.orderId = vo.id WHERE vo.id = ? AND o.createStatus = 1 AND o.orderType=6 AND o.delFlag = '0' ORDER BY o.updateDate DESC";
			resultList = visaOrderDao.findBySql(sql, Map.class, orderId);
		}else{//收据号
			String sql = "SELECT o.invoiceNum, o.uuid,o.orderType FROM orderreceipt o LEFT JOIN visa_order vo ON o.orderId = vo.id WHERE vo.id = ? AND o.createStatus = 1 AND o.orderType=6 AND o.delFlag = '0' ORDER BY o.updateDate DESC";
			resultList = visaOrderDao.findBySql(sql, Map.class, orderId);
		}
		return resultList;
	}
	
	/**
	 * 保存签证订单
	 * @param visaOrder
	 */
	@Transactional
	public VisaOrder saveVisaOrder(VisaOrder visaOrder){
		VisaOrder order = new VisaOrder();
		if(visaOrder!=null){
			order = visaOrderDao.save(visaOrder);
		}
		return order;
	}
	
	/**
	 * 修改签证订单
	 * @param visaOrder
	 */
	public VisaOrder updateVisaOrder(VisaOrder visaOrder){
		VisaOrder order = new VisaOrder();
		if(visaOrder!=null){
			order = visaOrderDao.save(visaOrder);
		}
		return order;
	}
	/**
	 * 
	* @Title: findvisaOrderList 
	* @Description: TODO(签证订单押金列表) 
	* @param @param type
	* @param @param page
	* @param @param orderBy
	* @param @param orderShowType
	* @param @param map
	* @param @param common
	* @return Page<Map<Object,Object>>    返回类型 
	* @throws
	 */
	public Page<Map<Object, Object>> findVisaOrderList(String type, Page<Map<Object, Object>> page,
	    		String orderBy, String orderShowType, Map<String,String> map, DepartmentCommon common) {
		StringBuffer sqlBuffer = new StringBuffer();
		if(StringUtils.isBlank(page.getOrderBy())) {
			page.setOrderBy(orderBy);
		}
		String payType = map.get("payType");
		/*增加计调查询条件*/
		String jd = map.get("jdUserId");
		String jdConditions = "";
		if(StringUtils.isNotBlank(jd)){
			jdConditions = "and vo.visa_product_id in(select vsp.id from visa_products vsp where vsp.createBy = " +jd +")";
		}
		
		
		sqlBuffer.append("select b.payType,b.accountDate,b.createDate orderpaycreatedate, b.toBankNname, b.receiptConfirmationDate, ")
				 .append(" b.payerName,a.salerId, a.saler,a.creator,a.createBy,a.id,a.visaId,b.id payid,a.agentid,a.travelerId, ")
				 .append(" a.orderNum,a.groupCode,a.productId,a.productName,a.travlerName,a.agentName,")
				 .append("a.createDate,b.updateDate,b.isAsAccount is_accounted,c.total_money,d.payed_money,d.serialNum,b.printTime, ")
				 .append(" b.printFlag,DATE_FORMAT(a.start_out,'%Y-%m-%d') start_out")
				 .append(" from (select vo.salerId, vo.salerName saler,vo.create_by creator,vp.createBy,vo.id,v.id visaId,t.id travelerId, ")
				 .append(" vo.order_no orderNum,vp.id productId,vp.productName,t.name as travlerName,")
				 .append(" ai.agentName ,ai.id agentid,vo.create_date createDate,vo.update_date updateDate,v.total_deposit,v.start_out, ");
		//C460V3plus 环球行签证团号取订单团号  wangyang 2016.5.6
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
			sqlBuffer.append(" vo.group_code AS groupCode ");
		}else{
			sqlBuffer.append(" vp.groupCode AS groupCode ");
		}
		sqlBuffer.append(" FROM visa v,visa_order vo,visa_products vp,traveler t,agentinfo ai WHERE v.traveler_id = t.id")
				 .append(" and vo.visa_product_id = vp.id " + jdConditions + " and t.orderId = vo.id ") //增加计调查询条件jdConditions
				 .append(" and t.order_type = " + Context.ORDER_TYPE_QZ + " and ai.id = vo.agentinfo_id ")
				 .append(" and vp.proCompanyId = " + UserUtils.getUser().getCompany().getId());
		String orderTimeBegin = map.get("orderTimeBegin");
	    String orderTimeEnd = map.get("orderTimeEnd");
       if(StringUtils.isNotBlank(orderTimeBegin)) {
    	   sqlBuffer.append( " and vo.create_date >= '" + orderTimeBegin + " 00:00:00' " );
       }
       if(StringUtils.isNotBlank(orderTimeEnd)){
    	   sqlBuffer.append(" and vo.create_date<='"+orderTimeEnd+" 23:59:59' ");
       }
       //订单号
       String orderNum = map.get("orderNum");
       if(StringUtils.isNotBlank(orderNum)){
    	   sqlBuffer.append(" and (vo.order_no like '%" + orderNum + "%') ");
       }
       //团号
       String groupCode = map.get("groupCode");
       if(StringUtils.isNotBlank(groupCode)){
    	   sqlBuffer.append(" and (vp.groupCode like '%" + groupCode + "%') ");
       }
       //销售
       String saler = map.get("saler");
       if(StringUtils.isNotBlank(saler)){ 
    	   sqlBuffer.append(" and vo.salerId =" + saler + " ");
       }
       String creator = map.get("creator");
       if(StringUtils.isNotBlank(creator)){
    	   sqlBuffer.append(" and vo.create_by =" + creator + " ");
       }
       //渠道
       String agentId = map.get("agentId");
       if(StringUtils.isNotBlank(agentId)){ 
    	   sqlBuffer.append(" and (ai.id =" + agentId + ")  ");
       }
		sqlBuffer.append(" )  a,orderpay b," )
			//将currency_name改为currency_mark
			.append(" (select ma.serialNum, group_concat(concat(c.currency_mark,ma.amount) separator '+') total_money")
			.append(" from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_SYJ)
			.append(" and ma.orderType = " + Context.ORDER_TYPE_QZ + " and ma.businessType = 2 group by ma.serialNum) c, ")
			//将currency_name改为currency_mark
			.append(" (select ma.serialNum,ma.uid, group_concat(concat(c.currency_mark,ma.amount) separator '+') payed_money ")
			//去掉了撤销状态
			.append(" from money_amount ma,currency c,orderpay op where ma.currencyId = c.currency_id and ma.moneyType in( " + Context.MONEY_TYPE_YSYJ +","+Context.MONEY_TYPE_CANCEL+")")
			.append(" and ma.orderType = " + Context.ORDER_TYPE_QZ + " and ma.businessType = 2 and op.moneySerialNum = ma.serialNum group by ma.serialNum,ma.uid) d")
			.append(" where a.orderNum = b.orderNum and b.moneySerialNum = d.serialNum and a.travelerId = d.uid and a.total_deposit=c.serialNum");
       	//是否已到款
        if("Y".equalsIgnoreCase(map.get("isAccounted"))){
     	   sqlBuffer.append(" and b.isAsAccount =").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
        }else if("N".equalsIgnoreCase(map.get("isAccounted"))){
	    	sqlBuffer.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
	    	  .append(" or isAsAccount is null)");
	    }else if("C".equalsIgnoreCase(map.get("isAccounted"))){
	    	sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
	    }
        
        if(StringUtils.isNotBlank(payType)){
        	if("3".equals(payType)){
        		sqlBuffer.append(" and (payType=3 or payType=5)");
        	}else{
        		sqlBuffer.append(" and payType =").append(payType);
        	}
        }
        //来款单位
        String payerName = map.get("payerName");
        if(StringUtils.isNotBlank(payerName)){
        	sqlBuffer.append(" and b.payerName like '%").append(payerName).append("%'");
        }
        //收款行
        String toBankNname = map.get("toBankNname");
        if(StringUtils.isNotBlank(toBankNname)){
        	sqlBuffer.append(" and b.toBankNname like '%").append(toBankNname).append("%'");
        }
        //确认收款日期
        String receiptConfirmationDateBegin = map.get("receiptConfirmationDateBegin");
        String receiptConfirmationDateEnd = map.get("receiptConfirmationDateEnd");
        if(StringUtils.isNotBlank(receiptConfirmationDateBegin)){
        	sqlBuffer.append(" and b.receiptConfirmationDate >='").append(receiptConfirmationDateBegin.trim()).append(" 00:00:00' ");
		}
		if(StringUtils.isNotBlank(receiptConfirmationDateEnd)){
			sqlBuffer.append(" and b.receiptConfirmationDate <='").append(receiptConfirmationDateEnd.trim()).append(" 23:59:59' ");
		}
        //付款日期
        String createDateBegin= map.get("createDateBegin");
        String createDateEnd = map.get("createDateEnd");
        if(StringUtils.isNotBlank(createDateBegin)){
			sqlBuffer.append(" and b.createDate >='").append(createDateBegin).append(" 00:00:00' ");
		}
		if(StringUtils.isNotBlank(createDateEnd)){
			sqlBuffer.append(" and b.createDate <='").append(createDateEnd).append(" 23:59:59' ");
		}
		//达账时间
		String accountDateBegin = map.get("accountDateBegin");
		String accountDateEnd = map.get("accountDateEnd");
		if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
        	sqlBuffer.append(" and b.isAsAccount=1");
        	if(StringUtils.isNotBlank(accountDateBegin)){
        		sqlBuffer.append(" and b.accountDate >= '"+accountDateBegin+" 00:00:00" + "' ");
        	}
        	if(StringUtils.isNotBlank(accountDateEnd)){
        		sqlBuffer.append(" and b.accountDate <= '"+accountDateEnd+" 23:59:59" + "' ");
        	}
        }
		String groupDateBegin = map.get("groupDateBegin");
		String groupDateEnd = map.get("groupDateEnd");
		if(StringUtils.isNotBlank(groupDateBegin)) {
			sqlBuffer.append(" and start_out >= '" + groupDateBegin + " 00:00:00'");
		}
		if(StringUtils.isNotBlank(groupDateEnd)) {
			sqlBuffer.append(" and start_out <= '" + groupDateEnd + " 23:59:59'");
		}
		//渠道结款方式
		String paymentType = map.get("paymentType");
		if(StringUtils.isNotBlank(paymentType)){
			sqlBuffer.append(" and a.agentid in ( select id from agentinfo where paymentType = "+ paymentType +" ) ");
		}
       //增加打印筛选查询条件20150824
	   String printFlag = map.get("printFlag");
	   if(StringUtils.isNotBlank(printFlag)) {
	    	   sqlBuffer.append( " and b.printFlag = " + printFlag );
	   }
	    Page<Map<Object, Object>> pageMap = visaOrderDao.findBySql(page, sqlBuffer.toString(),Map.class);
		return pageMap;
	}

	
	
	/**
	 * 
	* @Title: findvisaOrderList 
	* @Description: TODO(签证押金收款导出excel) 
	* @param @param orderBy
	* @param @param map
	* @return List<Map<Object,Object>>    返回类型 
	* @throws
	 */
	public List<Map<Object, Object>> findVisaOrderList(String orderBy, Map<String,String> map) {
		StringBuffer sqlBuffer = new StringBuffer();
		
		String payType = map.get("payType");
		/*增加计调查询条件*/
		String jd = map.get("jdUserId");
		String jdConditions = "";
		if(StringUtils.isNotBlank(jd)){
			jdConditions = "and vo.visa_product_id in(select vsp.id from visa_products vsp where vsp.createBy = " +jd +")";
		}
		
		
		sqlBuffer.append("select b.payType,b.accountDate,b.createDate orderpaycreatedate, b.toBankNname, b.payerName,a.salerId, a.saler,a.creator,a.createBy,a.id,a.visaId,b.id payid,a.agentid,a.travelerId,a.orderNum,a.groupCode,a.productId,a.productName,a.travlerName,a.agentName,")
					.append("a.createDate,b.updateDate,b.isAsAccount is_accounted,c.total_money,d.payed_money,d.serialNum,b.printTime,b.printFlag")
					.append(" from (select vo.salerId, vo.salerName saler,vo.create_by creator,vp.createBy,vo.id,v.id visaId,t.id travelerId,vo.order_no orderNum,vo.group_code groupCode,vp.id productId,vp.productName,t.name as travlerName,")
					.append(" ai.agentName ,ai.id agentid,vo.create_date createDate,vo.update_date updateDate,")
					.append(" v.total_deposit from visa v,visa_order vo,visa_products vp,")
					.append(" traveler t,agentinfo ai where v.traveler_id = t.id")
					.append(" and vo.visa_product_id = vp.id " + jdConditions + " and t.orderId = vo.id ") //增加计调查询条件jdConditions
					.append(" and t.order_type = " + Context.ORDER_TYPE_QZ + " and ai.id = vo.agentinfo_id ")
					.append(" and vp.proCompanyId = " + UserUtils.getUser().getCompany().getId());
		String orderTimeBegin = map.get("orderTimeBegin");
	    String orderTimeEnd = map.get("orderTimeEnd");
       if(StringUtils.isNotBlank(orderTimeBegin)) {
    	   sqlBuffer.append( " and vo.create_date >= '" + orderTimeBegin + " 00:00:00' " );
       }
       if(StringUtils.isNotBlank(orderTimeEnd)){
    	   sqlBuffer.append(" and vo.create_date<='"+orderTimeEnd+" 23:59:59' ");
       }
       //订单号
       String orderNum = map.get("orderNum");
       if(StringUtils.isNotBlank(orderNum)){
    	   sqlBuffer.append(" and (vo.order_no like '%" + orderNum + "%') ");
       }
       //团号
       String groupCode = map.get("groupCode");
       if(StringUtils.isNotBlank(groupCode)){
    	   sqlBuffer.append(" and (vo.group_code like '%" + groupCode + "%') ");
       }
       //销售
       String saler = map.get("saler");
       if(StringUtils.isNotBlank(saler)){ 
    	   sqlBuffer.append(" and vo.salerId =" + saler + " ");
       }
       String creator = map.get("creator");
       if(StringUtils.isNotBlank(creator)){
    	   sqlBuffer.append(" and vo.create_by =" + creator + " ");
       }
       //渠道
       String agentId = map.get("agentId");
       if(StringUtils.isNotBlank(agentId)){ 
    	   sqlBuffer.append(" and (ai.id =" + agentId + ")  ");
       }
       		sqlBuffer.append(" )  a,orderpay b," )
       				//将currency_name改为currency_mark
					.append(" (select ma.serialNum, group_concat(concat(c.currency_mark,FORMAT(ma.amount,2)) separator '+') total_money")
					.append(" from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_SYJ)
					.append(" and ma.orderType = " + Context.ORDER_TYPE_QZ + " and ma.businessType = 2 group by ma.serialNum) c, ")
					//将currency_name改为currency_mark
					.append(" (select ma.serialNum,ma.uid, group_concat(concat(c.currency_mark,FORMAT(ma.amount,2)) separator '+') payed_money ")
					.append(" from money_amount ma,currency c,orderpay op where ma.currencyId = c.currency_id and ma.moneyType in( " + Context.MONEY_TYPE_YSYJ +","+Context.MONEY_TYPE_CANCEL+")")
					.append(" and ma.orderType = " + Context.ORDER_TYPE_QZ + " and ma.businessType = 2 and op.moneySerialNum = ma.serialNum group by ma.serialNum,ma.uid) d")
					.append(" where a.orderNum = b.orderNum and b.moneySerialNum = d.serialNum and a.travelerId = d.uid and a.total_deposit=c.serialNum");
       	//是否已到款
        if("Y".equalsIgnoreCase(map.get("isAccounted"))){
     	   sqlBuffer.append(" and b.isAsAccount =").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
        }else if("N".equalsIgnoreCase(map.get("isAccounted"))){
	    	sqlBuffer.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
	    	  .append(" or isAsAccount is null)");
	    }else if("C".equalsIgnoreCase(map.get("isAccounted"))){
	    	sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
	    }
        
        if(StringUtils.isNotBlank(payType)){
        	if("3".equals(payType)){
        		sqlBuffer.append(" and (payType=3 or payType=5)");
        	}else{
        		sqlBuffer.append(" and payType =").append(payType);
        	}
        }
        //来款单位
        String payerName = map.get("payerName");
        if(StringUtils.isNotBlank(payerName)){
        	sqlBuffer.append(" and b.payerName like '%").append(payerName).append("%'");
        }
        //收款行
        String toBankNname = map.get("toBankNname");
        if(StringUtils.isNotBlank(toBankNname)){
        	sqlBuffer.append(" and b.toBankNname like '%").append(toBankNname).append("%'");
        }
        //确认收款日期
        String receiptConfirmationDateBegin = map.get("receiptConfirmationDateBegin");
        String receiptConfirmationDateEnd = map.get("receiptConfirmationDateEnd");
        if(StringUtils.isNotBlank(receiptConfirmationDateBegin)){
        	sqlBuffer.append(" and b.receiptConfirmationDate >='").append(receiptConfirmationDateBegin.trim()).append(" 00:00:00' ");
		}
		if(StringUtils.isNotBlank(receiptConfirmationDateEnd)){
			sqlBuffer.append(" and b.receiptConfirmationDate <='").append(receiptConfirmationDateEnd.trim()).append(" 23:59:59' ");
		}
        //付款日期
        String createDateBegin= map.get("createDateBegin");
        String createDateEnd = map.get("createDateEnd");
        if(StringUtils.isNotBlank(createDateBegin)){
			sqlBuffer.append(" and b.createDate >='").append(createDateBegin).append(" 00:00:00' ");
		}
		if(StringUtils.isNotBlank(createDateEnd)){
			sqlBuffer.append(" and b.createDate <='").append(createDateEnd).append(" 23:59:59' ");
		}
		//达账时间
		String accountDateBegin = map.get("accountDateBegin");
		String accountDateEnd = map.get("accountDateEnd");
		if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
        	sqlBuffer.append(" and b.isAsAccount=1");
        	if(StringUtils.isNotBlank(accountDateBegin)){
        		sqlBuffer.append(" and b.accountDate >= '"+accountDateBegin+" 00:00:00" + "' ");
        	}
        	if(StringUtils.isNotBlank(accountDateEnd)){
        		sqlBuffer.append(" and b.accountDate <= '"+accountDateEnd+" 23:59:59" + "' ");
        	}
        }
       //增加打印筛选查询条件20150824
	   String printFlag = map.get("printFlag");
	   if(StringUtils.isNotBlank(printFlag)) {
	    	   sqlBuffer.append( " and b.printFlag = " + printFlag );
	   }
	   if(StringUtils.isBlank(orderBy)){
			orderBy = "updateDate DESC";
	    }
	   sqlBuffer.append(" order by ").append(orderBy);
		List<Map<Object, Object>> list = visaOrderDao.findBySql(sqlBuffer.toString(),Map.class);
		return list;
	}

	
	/**
	 * 签证订单收款
	 * @param type
	 * @param page
	 * @param orderBy
	 * @param orderShowType
	 * @param map
	 * @param common
	 * @return
	 */
	public Page<Map<Object, Object>> findVisaOrderList1(String type, Page<Map<Object, Object>> page,
    		String orderBy, String orderShowType, Map<String,String> map, DepartmentCommon common) {
	StringBuffer sqlBuffer = new StringBuffer();
	sqlBuffer.append("select  cc.id, cc.serialNum,cc.amount,ee.orderId,cc.uid ,dd.name,ee.createDate from ");
	sqlBuffer.append(" (SELECT aa.id, aa.serialNum, GROUP_CONCAT(bb.currency_mark,aa.amount) amount, aa.uid FROM money_amount aa LEFT JOIN currency bb ON aa.currencyId = bb.currency_id");
	sqlBuffer.append(" WHERE aa.businessType = '2' AND aa.serialNum IS NOT NULL GROUP BY aa.serialNum");
	sqlBuffer.append(" )cc left join traveler dd ");
	sqlBuffer.append(" on cc.uid= dd.id left join orderpay ee on cc.serialNum = ee.moneySerialNum order by uid");
	Page<Map<Object, Object>> pageMap = visaOrderDao.findBySql(page, sqlBuffer.toString(),Map.class);
	return pageMap;
}
	
	
	/**
	 * 根据团期号,获得所有子订单id
	 * */
	public List<Long> getOrderIdByGroupCode(String groupCode){
		List<Object> par = new ArrayList<Object>();
		par.add(groupCode);
		List<Map<String, Object>> resultList = visaDao.findBySql(SqlConstant.SEARCH_VISAORDER_BY_MAINORDER,Map.class, par.toArray());
		if(null != resultList && resultList.size()>0){
			List<Long> idList = new ArrayList<Long>();
			for(int i=0;i<resultList.size();i++) {
				Map<String, Object> listin = resultList.get(i);
				if(null != listin.get("id") && !"".equals(listin.get("id"))){
					idList.add(Long.valueOf(listin.get("id").toString()));
				}
			}
			return idList;
		}else{
			return null;
		}
	}
	
	
	/**
	 * 查找是否有正在进行的押金转担保的申请
	 * @param orderId visa表的id
	 * @param agentId 公司id
	 * @param travelerId 游客id
	 * @return
	 */
	public boolean searchReview(String orderId,String travelerId ){
		List<Object> par = new ArrayList<Object>();
		par.add(orderId);
		par.add(travelerId);
		par.add(UserUtils.getUser().getCompany().getId());
		List<Map<String, Object>> resultList = visaDao.findBySql(SqlConstant.SEARCH_REVIEW,Map.class, par.toArray());
		
		for(Map<String, Object> listin : resultList) {
			if(null != listin.get("status") && !"".equals(listin.get("status"))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 更新已签收的资料
	 * @param visaId visa表达id
	 * @param fuyinValue 复印资料的值
	 * @param yuanValue 原件的值
	 * @return
	 */
	public String updateOrder (String fuyinValue,String yuanValue,String visaId){
		try{
		if(null == fuyinValue  || null == yuanValue || null == visaId ){
			return "参数错误,更新失败";
		}
		List<Object> par = new ArrayList<Object>();
		par.add(yuanValue);
		par.add(fuyinValue);
		par.add(visaId);
		visaDao.updateBySql(SqlConstant.UPDATE_VISA_BY_ID, par.toArray());
		}catch (Exception e) {
			e.printStackTrace();
			return "系统错误,更新失败";
		}
		return null;
		
	}
	
	/***
	 * 签务身份管理签证订单
	 * 查询需要签收的资料
	 * visaId:visa表的主键
	 * visaProductId 签证产品表的主键id
	 */   
	public List<List<OriginalProjectForm>>  searchDatum(String visaProductId,String visaId){
		if(null == visaId || "".equals(visaId)){
			return null;
		}
		Visa visa = visaDao.findOne(Long.valueOf(visaId));
		//已签收原件列表
		String[] yuanjianQianshouTypeArray = null;
		//已签收复印件列表
		String[] fuyinjianQianshouTypeArray = null;
		
		if(null != visa){
			String qianshouYuanjian = visa.getSignOriginalProjectType();
			String qianshouFuyinjian = visa.getSignCopyProjectType();
			//表示是否签收资料原件
			if(null != qianshouYuanjian && !"".equals(qianshouYuanjian) && null != qianshouYuanjian.split(",") && qianshouYuanjian.split(",").length>0 ){
				yuanjianQianshouTypeArray = qianshouYuanjian.split(",");
			}
			//表示是否签收资料复印件
			if(null != qianshouFuyinjian && !"".equals(qianshouFuyinjian) && null != qianshouFuyinjian.split(",") && qianshouFuyinjian.split(",").length>0 ){
				fuyinjianQianshouTypeArray = qianshouFuyinjian.split(",");
			}
		}
		//根据id得到产品应收资料
		VisaProducts visaProducts  = visaProductsDao.findOne(Long.valueOf(visaProductId));
		//原件资料
		String yuanjian = visaProducts.getOriginal_Project_Type();
		String yuanjian_qita = visaProducts.getOriginal_Project_Name();
		//复印件资料
		String fuyinjian = visaProducts.getCopy_Project_Type();
		String fuyinjian_qita = visaProducts.getCopy_Project_Name();
		
		//所有的资料列表
		List<List<OriginalProjectForm>> ziliaoList = new ArrayList<List<OriginalProjectForm>>();
		
		//应收资料不是空,并且资料字段内存在以","分隔的字符串
		if(null != yuanjian && !"".equals(yuanjian) && null != yuanjian.split(",") && yuanjian.split(",").length>0 ){
			
			ziliaoList.add(getOriginalProjectForm(yuanjian.split(","),yuanjianQianshouTypeArray,yuanjian_qita,"0"));
			
		}
		
		if(null != fuyinjian && !"".equals(fuyinjian) && null != fuyinjian.split(",") && fuyinjian.split(",").length>0 ){
			
			ziliaoList.add(getOriginalProjectForm(fuyinjian.split(","),fuyinjianQianshouTypeArray,fuyinjian_qita,"1"));
			
		}
		return ziliaoList;
	}
	/**
	 * 更新应收押金
	 * @param typeArray 需要签收的资料列表
	 * @param qianshouTypeArray 已经签收的资料列表
	 * @param qita 设置资料时,在"其他"选项的文本框内输入的汉字
	 * @param type 代表这次的调用时对 原件操作还是对 复印件操作 0:原件;1:复印件
	 * */
	private List<OriginalProjectForm> getOriginalProjectForm(String[] typeArray,String[] qianshouTypeArray,String qita,String type){
		OriginalProjectForm originalProject = null;
		List<OriginalProjectForm> originalProjectList = new ArrayList<OriginalProjectForm>();
		List<Dict> dictList = new ArrayList<Dict>();
		//获取原件的名字
		if("0".equals(type)){
			dictList = findDictByType("credentials_type");
		//获取复印件的名字
		}else{
			dictList = findDictByType("copy_credentials_type");
		}
			//已签收的列表为空,所有的资料都设置为,未签收的状态
			if(null == qianshouTypeArray ||  qianshouTypeArray.length == 0){
				for(int i=0;i<typeArray.length;i++){
					originalProject = new OriginalProjectForm();
					originalProject.setId(typeArray[i]);
					if(typeArray[i].equals("2")){
						originalProject.setName("其他:"+qita);
					}else{
						for(int k=0;k<dictList.size();k++){
							if(typeArray[i].equals(dictList.get(k).getValue())){
								originalProject.setName(dictList.get(k).getLabel());
								break;
							}
						}
					}
					originalProject.setSignFlag("0");
					originalProjectList.add(originalProject);
				}
			}else{
				//循环签证资料和已签收资料的列表,解析出已经签收的列表
				for(int i=0;i<typeArray.length;i++){
					originalProject = new OriginalProjectForm();
					originalProject.setId(typeArray[i]);
					if(typeArray[i].equals("2")){
						originalProject.setName("其他:"+qita);
					}else{
						originalProject.setSignFlag("0");
						for(int k=0;k<dictList.size();k++){
							if(typeArray[i].equals(dictList.get(k).getValue())){
								originalProject.setName(dictList.get(k).getLabel());
							//	originalProject.setSignFlag("1");//签收资料的勾选项应该是和签收列表数组关联的,不应该是和报名时的勾选的列表数组进行关联设置的.-tgy
								break;
							}
						}
					}
					//如果已签收列表中包含签收列表的id,设置为已签收状态
					for(int j=0;j<qianshouTypeArray.length;j++){
						if(qianshouTypeArray[j].equals(typeArray[i])){
							originalProject.setSignFlag("1");
							break;
						}
					}
					originalProjectList.add(originalProject);
				}
			}
			return originalProjectList;
	}
	
	
	/**
	 * 更新应收押金
	 * @param totalDeposit money表的金额
	 * @return uuid  money表的uuid
	 * */
	public String updateMoneyAmount(String totalDeposit,String uuid,String visaId ){
		if(null == visaId || "".equals(visaId) || null == totalDeposit ||"".equals(totalDeposit)){
			return "参数错误,更新失败";
		}
		List<Object> par = new ArrayList<Object>();
		par.add(totalDeposit);
		par.add(visaId);
		visaDao.updateBySql(SqlConstant.UPDATE_MONEY_AMOUNT_BY_UUID, par.toArray());
		return "";
	}
	
	/**
	 * 查询应收押金
	 * @param visaId visa表的主键id
	 * @return Map<String, Object>
	 * 		name 币种中文名称
	 * 		amount 金额数
	 * */
	public Map<String, Object> searchMoneyAmount(String visaId){
		if(null == visaId || "".equals(visaId)){
			return null;	
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> par = new ArrayList<Object>();
		par.add(visaId);
		List<Object> objectList = visaDao.findBySql(SqlConstant.SEARCH_MONEY_AMOUNT_BY_UUID, par.toArray());
		for (Object obj : objectList) {
    		Object[] array=(Object[])obj;
    		if(null != array[0] && !"".equals(array[0])){
    			map.put("currency_name", array[0]);
    			map.put("amount", array[1]);
    			map.put("currency_mark", array[2]);
    		}
    		if(null == map.get("amount") || null == map.get("currency_mark")){
    			map.put("data", "false");
    		}else{
    			map.put("data", "true");
    		}
		}
		return map;
		
	}
	
	/**
	 * 设置应收押金
	 * @param visaId visa表主键
	 * @param totalDeposit 应收押金
	 * @param currencyId 币种id
	 * @param travelerId 游客id
	 * */
	public List<String> addDeposit(String visaId,String totalDeposit,String currencyId,String travelerId){
		List<String> list = new ArrayList<String>();
		if(null == visaId || null ==totalDeposit || "".equals(visaId) ||"".equals(totalDeposit) || 
				   null ==totalDeposit || "".equals(totalDeposit)){
			list.add("参数错误,设置失败");
			return list;
		}else{
			//生成uuid
			String payPriceSerialNum = UUID.randomUUID().toString();

			MoneyAmount moneyAmount=new MoneyAmount();
			BigDecimal bd=new BigDecimal(totalDeposit);
	    	moneyAmount.setAmount(bd);
	    	moneyAmount.setCurrencyId(Integer.valueOf(currencyId));
	    	moneyAmount.setUid(Long.valueOf(travelerId));
	    	//代表是签证订单
	    	moneyAmount.setOrderType(6);
	    	moneyAmount.setSerialNum(payPriceSerialNum);
	    	//应收款的 类型
	    	moneyAmount.setMoneyType(Context.MONEY_TYPE_SYJ);
	    	moneyAmount.setCreatedBy(UserUtils.getUser().getId());
	    	moneyAmount.setBusindessType(2);
	    	Currency currency = currencyDao.findOne(Long.valueOf(currencyId));
	    	
	    	boolean flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
	    	if(flag){
	    		List<Object> par = new ArrayList<Object>();
	    		par.add(payPriceSerialNum);
	    		par.add(visaId);
	    		visaDao.updateBySql(SqlConstant.UPDATE_VISA_TOTAL_DEPOSIT, par.toArray());
	    	}else{
	    		list.add("添加UUid失败");
				return list;
	    	}
	    	list.add("操作成功");
	    	list.add(currency.getCurrencyMark());
			return list;
		}
	}
	/**
	 * 签务身份
	 * 更新游客的信息
	 * value 需要更新到数据库的数据
	 * type 更新游客的属性类型
	 * visaId visa表的主键id
	 * travelerId 游客表的主键id
	 * @return 空(null) 正常结束
	 * 		   "更新错误" 表示更新异常
	 * */
	
	public String updateTraveler (
			//String aa,//AA码
			String visaStatus,//签证状态
			String passstatus,//护照状态
			String guaranteeStatus,//担保状态
			String travelerId,//游客id
			String visaId,//签证id
			String startOut,//实际出团时间
			String contract, //实际约签时间
			String deliveryTime//实际送签时间
			) {
		
		SimpleDateFormat df ;
		try{
		List<Object> par = new ArrayList<Object>();
		//par.add(aa);
		
		if(null == startOut || "".equals(startOut)){
    		par.add(null);
    	}else{
    		if(startOut.contains(":"))
    		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
    		else
    			df = new SimpleDateFormat("yyyy-MM-dd"); 
    		Date date = df.parse(startOut);  
    		par.add(date);
    		
    	}
		if(null == contract || "".equals(contract)){
			par.add(null);
		}else{
			if(contract.contains(":"))
        		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        		else
        			df = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = df.parse(contract);  
			par.add(date);
			
		}
		par.add(visaStatus);
		//担保状态
		par.add(guaranteeStatus);
		//实际送签时间
		if(null == deliveryTime || "".equals(deliveryTime)){
    		par.add(null);
    	}else{
    		df = new SimpleDateFormat("yyyy-MM-dd"); 
    		Date date = df.parse(deliveryTime);  
    		par.add(date);
    	}
		par.add(visaId);
		Visa visa  = visaDao.findOne(Long.valueOf(visaId));
//		如果更新成押金担保,但是没有设置应收押金,提示错误
		if("3".equals(guaranteeStatus) && (null == visa || null == visa.getTotalDeposit() || "".equals(visa.getTotalDeposit()))){
			return "保存失败:担保状态为押金担保时,必须设置押金";
		}

		visaOrderDao.updateBySql(SqlConstant.UPDATE_VISA, par.toArray());
		
		par.clear();
		par.add(passstatus);
		par.add(travelerId);
		visaOrderDao.updateBySql(SqlConstant.UPDATE_PASSPORTSTATUS, par.toArray());
		
		return "游客信息保存成功";
		}catch (Exception e) {
			e.printStackTrace();
			return "保存失败";
		}
	}


	
	public String updateTraveler1 (
			//String aa,//AA码
			String visaStatus,//签证状态
			String passstatus,//护照状态
			String guaranteeStatus,//担保状态
			String travelerId,//游客id
			String visaId,//签证id
			String startOut,//实际出团时间
			String contract //实际约签时间
			) {
		
		SimpleDateFormat df ;
		try{
		List<Object> par = new ArrayList<Object>();
		//par.add(aa);
		
		if(null == startOut || "".equals(startOut)){
    		par.add(null);
    	}else{
    		if(startOut.contains(":"))
    		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
    		else
    			df = new SimpleDateFormat("yyyy-MM-dd"); 
    		Date date = df.parse(startOut);  
    		par.add(date);
    		
    	}
		if(null == contract || "".equals(contract)){
			par.add(null);
		}else{
			if(contract.contains(":"))
        		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        		else
        			df = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = df.parse(contract);  
			par.add(date);
			
		}
		par.add(visaStatus);
		//担保状态
		par.add(guaranteeStatus);
		par.add(visaId);
		Visa visa  = visaDao.findOne(Long.valueOf(visaId));
//		如果更新成押金担保,但是没有设置应收押金,提示错误
		if("3".equals(guaranteeStatus) && (null == visa || null == visa.getTotalDeposit() || "".equals(visa.getTotalDeposit()))){
			return "保存失败:担保状态为押金担保时,必须设置押金";
		}

		visaOrderDao.updateBySql(SqlConstant.UPDATE_VISA_ONE, par.toArray());
		
		par.clear();
		par.add(passstatus);
		par.add(travelerId);
		visaOrderDao.updateBySql(SqlConstant.UPDATE_PASSPORTSTATUS, par.toArray());
		
		return "游客信息保存成功";
		}catch (Exception e) {
			e.printStackTrace();
			return "保存失败";
		}
	}
	
	
	
	

	/**
	 * 签务身份
	 * 批量更新签证状态
	 * visaStatus 签证状态
	 * visaId visa表的主键id
	 * @return 空(null) 正常结束
	 *         "更新错误" 表示更新异常
	 * */
	public String batchUpdateVisaStatus(String visaIds, String visaStatus) {
		boolean status = visaOrderDao.batchUpdateVisaStatus(visaIds, visaStatus);
		return status==true?"签证状态保存成功":"签证状态保存失败";
	}
	
	
	public String batchUpdatePassportStatus(String passportStatus, String travellerIds) {
		boolean status = visaOrderDao.batchUpdatePassportStatus(passportStatus, travellerIds);
		return status==true?"护照状态保存成功":"护照状态保存失败";
	}
	
	/**
	 * 借护照-提交
	 * @param batchNo
	 * @param visaIds
	 * @return
	 */
	public String submit4BorrowPassport(String batchNo,String visaIds){
		String sql = "select t.id from traveler t join visa v on t.id=v.traveler_id where v.id in("+visaIds+")";
		List<Map<String,Integer>> list = visaOrderDao.findBySql(sql,Map.class);
		for(int i=0;i<list.size();i++){
			Integer id = list.get(i).get("id");
			String sql1 = "update Traveler set passportStatus=1 where id="+id;
			travelerDao.getSession().createQuery(sql1).executeUpdate();
		}
		String sql2 = "update BatchRecord set passportStatus=1,isSubmit=1,createUserId=?,createUserName=? where batchNo='"+batchNo+"'";
		batchRecordDao.getSession().createQuery(sql2)
		.setParameter(0, UserUtils.getUser().getId())
		.setParameter(1, UserUtils.getUser().getName())
		.executeUpdate();
		return "操作成功";
	}
	
	/**
	 * 还护照-提交
	 * @param batchNo
	 * @param visaIds
	 * @return
	 */
	public String submit4ReturnPassport(String batchNo,String visaIds){
		String sql = "select t.id from traveler t join visa v on t.id=v.traveler_id where v.id in("+visaIds+")";
		List<Map<String,Integer>> list = visaOrderDao.findBySql(sql,Map.class);
		for(int i=0;i<list.size();i++){
			Integer id = list.get(i).get("id");
			String sql1 = "update Traveler set passportStatus=4 where id="+id;
			travelerDao.getSession().createQuery(sql1).executeUpdate();
		}
		String sql2 = "update BatchRecord set passportStatus=3,isSubmit=1,createUserId=?,createUserName=? where batchNo='"+batchNo+"'";
		batchRecordDao.getSession().createQuery(sql2)
		.setParameter(0, UserUtils.getUser().getId())
		.setParameter(1, UserUtils.getUser().getName())
		.executeUpdate();
		return "操作成功";
	}
	
	

	/**
	 * 根据主订单ID查询签证订单游客
	 * @param mainOrderId
	 * @return
	 */
	public List<Traveler> findTravelersByMainOrderId(Long mainOrderId) {
		List<Traveler>  travelerList = travelerDao.findTravelersByMainOrderId(mainOrderId);
		return travelerList;
	}

	public static  String par(List<Long> levelList){
		StringBuffer sb=null;
		if (null!=levelList&&levelList.size()>0) {
			sb =  new StringBuffer("(");
			for (Long level: levelList) {
				sb.append(level+",");
			}
			String temp =sb.toString();
			temp = temp.substring(0,temp.length()-1)+",-1 )";
			return temp;
			
		}else {
			return null;
		}
	}
	
	/**
	 * 组装OrderPayInput参数
	 */
	public OrderPayInput createPayParameter(String visaOrderId,String visaOrderDetaiUrl) {

		List<Traveler> travelerList = travelerDao.findTravelerByOrderIdAndOrderType(Long.parseLong(visaOrderId),6);

		// 构造入参对象
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();

		// 订单详情的页面
		orderPayInput.setOrderDetailUrl(visaOrderDetaiUrl);
		// //订单的后置方法名
		orderPayInput.setServiceAfterMethodName("visaOrderPayByTravler");
		// //订单的后置类名
		orderPayInput.setServiceClassName("com.trekiz.admin.modules.visa.service.VisaOrderService");
		//订单列表的页面
		orderPayInput.setOrderListUrl("/visa/order/searchxs?_m=417&_mc=580");

		for (int i = 0; i < travelerList.size(); i++) {
			Traveler traveler = travelerList.get(i);
			Long trivateId = traveler.getId();

			// 单个游客的支付信息
			OrderPayDetail detail = new OrderPayDetail();
			// 根据订单id和游客id获得游客信息
			VisaOrderTravelerResultForm form = searchTravelerByOrder(trivateId.toString(), visaOrderId);
			// 设置渠道id
			orderPayInput.setAgentId(Integer.valueOf(form.getAgentinfoId()));
			// 订单id
			detail.setOrderId(Long.valueOf(form.getVisaorderId()));
			// 订单编号
			detail.setOrderNum(form.getVisaorderNo());
			// 订单类型
			detail.setOrderType(Integer.valueOf(Context.ORDER_STATUS_VISA));
			// 付款方式类型
			detail.setPayPriceType(Integer.parseInt(Context.ORDER_ORDERTYPE_ZFQK));
			
			//游客类型2，订单为1
			detail.setBusindessType(2);
			
			//--------处理单个游客的结算价 wxw added 2013-03-06----------
			List<String> serialNumList = new ArrayList<String>();
			serialNumList.add(traveler.getPayPriceSerialNum());
			List<String> moneystrList = moneyAmountService.getMoneyIdAndPrice(serialNumList);//返回的数据格式为//33 100.00
			StringBuffer sbcurrencyId = new StringBuffer("");
			StringBuffer sbcurrencyPrice = new StringBuffer("");
			for (String currencyIdAndCurrencyPrice:moneystrList) {
				String[] _temp  = currencyIdAndCurrencyPrice.split(" ");
				sbcurrencyId.append(_temp[0]).append(",");
				sbcurrencyPrice.append(_temp[1]).append(",");
			}
			String currencyIdsStr = sbcurrencyId.toString();
			currencyIdsStr = currencyIdsStr.substring(0,currencyIdsStr.length()-1);
			
			String currencyPricesStr = sbcurrencyPrice.toString();
			currencyPricesStr = currencyPricesStr.substring(0,currencyPricesStr.length()-1);
			
			// 订单货币id列表
			detail.setPayCurrencyId(currencyIdsStr);
			// 订单金额列表
			detail.setPayCurrencyPrice(currencyPricesStr);
			detail.setTotalCurrencyId(currencyIdsStr);
			detail.setTotalCurrencyPrice(currencyPricesStr);
			//----------------------------------------

			detail.setTravelerId(traveler.getId());
			detailList.add(detail);
		}

		if (detailList.size() == 1) {
			// input金额状态(0:不显示；1：显示、可操作；2：显示、只读)
			orderPayInput.setMoneyFlag(1);
		}
		
		orderPayInput.setOrderPayDetailList(detailList);
		return orderPayInput;
	}
	
	/**
	 *批量付款
	 * 
	 */
	public OrderPayInput batchPay(List<BatchPayModel> batchPayModels){
		
		//构造入参对象
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();
		
		//订单列表的页面
		orderPayInput.setOrderListUrl("/visa/order/searchxs");
//		//订单的后置方法名
		orderPayInput.setServiceAfterMethodName("visaOrderPayByTravler");
//		//订单的后置类名
		orderPayInput.setServiceClassName("com.trekiz.admin.modules.visa.service.VisaOrderService");
		// 订单总额显示状态(true:显示;false:不显示)
		orderPayInput.setTotalCurrencyFlag(false);

		if(CollectionUtils.isNotEmpty(batchPayModels)) {
			for(BatchPayModel batchPayModel : batchPayModels) {
				String trivateId = batchPayModel.getTravelerid().toString();
				String orderId = batchPayModel.getOrderid().toString();
				StringBuffer payCurrencyId = new StringBuffer();
				StringBuffer payCurrencyPrice = new StringBuffer();
				//单个游客的支付信息
				OrderPayDetail detail = new OrderPayDetail();
				//根据订单id和游客id获得游客信息
				VisaOrderTravelerResultForm form = searchTravelerByOrder(trivateId,orderId);
				//设置渠道id
				orderPayInput.setAgentId(Integer.valueOf(form.getAgentinfoId()));
				
				detail.setPayPriceType(Context.MONEY_TYPE_QK);
				//查询付款游客信息
				Traveler traveler = travelerDao.findById(Long.valueOf(form.getId()));
				
				/*Map<String, String> map = getChajia(traveler.getPayPriceSerialNum(), traveler.getPayedMoneySerialNum());*/
				//订单id
				detail.setOrderId(Long.valueOf(form.getVisaorderId()));
				//订单编号
				detail.setOrderNum(form.getVisaorderNo());
				detail.setBusindessType(2);
				//订单类型
				detail.setOrderType(Integer.valueOf(Context.ORDER_STATUS_VISA));
				
				if(CollectionUtils.isNotEmpty(batchPayModel.getCurrencyAmounts())) {
					for(CurrencyAmount currencyAmount : batchPayModel.getCurrencyAmounts()) {
						payCurrencyId.append(currencyAmount.getCurrencyid());
						payCurrencyId.append(",");
						
						payCurrencyPrice.append(currencyAmount.getPrice());
						payCurrencyPrice.append(",");
					}
					payCurrencyId.deleteCharAt(payCurrencyId.length()-1);
					payCurrencyPrice.deleteCharAt(payCurrencyPrice.length()-1);
				}
				
				//订单货币id列表
				detail.setPayCurrencyId(payCurrencyId.toString());
				//订单金额列表
				detail.setPayCurrencyPrice(payCurrencyPrice.toString());
				
				//差价不是0的时候,返回列表
				/*if(null != map){
					//订单货币id列表
					detail.setPayCurrencyId(map.get("huobiId"));
					//订单金额列表
					detail.setPayCurrencyPrice(map.get("jine"));
				}else{
					continue;
					
//					String huobi = getTotalMoney(traveler.getPayPriceSerialNum()).get("huobi");
//					//订单货币id列表
//					detail.setPayCurrencyId(huobi);
//					String jineArray ="";
//					if(null != huobi && !"".equals(huobi)){
//						String[] huobiArray = huobi.split(",");
//						for(int j=0;j<huobiArray.length;j++){
//							jineArray = jineArray+"0";
//							if(huobiArray.length -1 !=j){
//								jineArray=jineArray+",";
//							}
//						}
//					}
//					//订单金额列表
//					detail.setPayCurrencyPrice(jineArray);
				}*/
				
				VisaOrder visaOrder = visaOrderDao.findOne(Long.valueOf(orderId));
				detail.setTotalCurrencyId(getTotalMoney(visaOrder.getTotalMoney()).get("huobi"));
				
				detail.setTotalCurrencyPrice(getTotalMoney(visaOrder.getTotalMoney()).get("jine"));
				
				detail.setTravelerId(traveler.getId());
				detailList.add(detail);
			}
		}

		if (detailList.size() == 1) {
			// input金额状态(0:不显示；1：显示、可操作；2：显示、只读)
			orderPayInput.setMoneyFlag(1);
		}
		
		orderPayInput.setOrderPayDetailList(detailList);
		return orderPayInput;
//			//应收金额为空,不显示付款按钮
//        	if(null == traveler.getPayPriceSerialNum() || "".equals(traveler.getPayPriceSerialNum()) {
//        		travelerForm.setPayButtonFlag("false");
//        	//应付金额不为空,已付总金额为空,显示付款按钮
//        	}else if(null == travelerArray[20] || "".equals(travelerArray[20])){
//        		travelerForm.setPayButtonFlag("true");
//        		//从应付金额中获取,货币列表和货币金额,作为参数
//        		travelerForm.setJine(getMoneyId(travelerArray[12].toString()).get("jine"));
//        		travelerForm.setHuobiId(getMoneyId(travelerArray[12].toString()).get("huobiId"));
//        	}else{
//        		Map<String, String> map = getChajia_yajin(travelerArray[12].toString(),travelerArray[20].toString());
//        		//通过计算后,没有任何结果,则不显示付款按钮
//        		if(null == map ){
//        			travelerForm.setPayButtonFlag("fasle");
//        		}else{
//        			travelerForm.setPayButtonFlag("true");
//        			travelerForm.setJine(map.get("jine"));
//        			travelerForm.setHuobiId(map.get("huobiId"));
//        		}
			//渠道id
//			orderPayInput.setAgentId(Integer.valueOf(form.getAgentinfoId()));
			//返回页面
//			orderPayInput.setOrderListUrl("/visa/ordersalespersonSearch");
			//后置方法
			//orderPayInput.setServiceAfterMethodName();
			//回调类
//			orderPayInput.setServiceClassName();
//			
//			detail.setOrderId(Long.valueOf(form.getVisaorderId()));
//			detail.setOrderNum(form.getOrderCode());
//			detail.setOrderType(Context.ORDER_STATUS_VISA);
//			detail.setPayCurrencyId();
//			detail.setPayCurrencyPrice();
//			detail.setTravelerId();
//		}
	}
	
	/**
	 * 根据uuid获得金额和货币列表
	 * uuid uuid
	 * 
	 * 
	 * */
	public Map<String, String> getTotalMoney(String uuid ){
		String yingshouSQL = "SELECT ma.amount ,c.currency_id  from  money_amount ma INNER JOIN currency c ON c.currency_id = ma.currencyId  where ma.serialNum = '"+uuid+"'";
		List<Map<String, Object>> resultList = visaDao.findBySql(yingshouSQL,Map.class);
		Map<String, String> map = new HashMap<String, String>();
		String jine ="";
		String huobi = "";
		for(Map<String, Object> listin : resultList) {
			//货币
			if(null != listin.get("amount")  && !"".equals(listin.get("amount"))){
					jine = jine+listin.get("amount").toString()+",";
			}
			//金额
			if(null != listin.get("currency_id")  && !"".equals(listin.get("currency_id"))){
				huobi = huobi+listin.get("currency_id").toString()+",";
			}
		}
		if(null != jine && !"".equals(jine) && null != huobi && !"".equals(huobi)){
			map.put("jine", jine.substring(0,jine.length() -1));
			map.put("huobi", huobi.substring(0,huobi.length() -1));
		}
		return map;
	}
	
	public VisaOrderTravelerResultForm loadTraveler(Object[] travelerArray,VisaOrderTravelerResultForm travelerForm){
		//姓名jilu
		if(null != travelerArray[0] && !"".equals(String.valueOf(travelerArray[0]))){
			travelerForm.setTravelerName(travelerArray[0]+ "");
		}
		//护照编号
		if(null != travelerArray[1] && !"".equals(String.valueOf(travelerArray[1]))){
			travelerForm.setPassportId(travelerArray[1]+ "");
		}
		//AA码
		if(null != travelerArray[2] && !"".equals(String.valueOf(travelerArray[2]))){
			travelerForm.setAACode(travelerArray[2]+ "");
		}
		//签证类型
		if(null != travelerArray[3] && !"".equals(String.valueOf(travelerArray[3]))){
			List<Dict> dictList = findDictByType("new_visa_type");
			if(null != dictList && dictList.size()>0){
    			for(int i=0;i<dictList.size();i++){
    				Dict dict =dictList.get(i);
    				if(String.valueOf(travelerArray[3]).equals(dict.getValue())){
    					travelerForm.setVisaType(dict.getLabel());
    				}
    			}
			}
		}
		
		//国家
		if(null != travelerArray[4] && !"".equals(String.valueOf(travelerArray[4]))){
			travelerForm.setVisaCountry(travelerArray[4]+ "");
		}
		//领区
		if(null != travelerArray[34] && !"".equals(String.valueOf(travelerArray[34]))){
			travelerForm.setCollarZoning(travelerArray[34]+"");
		}
		//预定出团时间
		if(null != travelerArray[5] && !"".equals(travelerArray[5])){
			String customDate = DateUtils.formatCustomDate((Date)travelerArray[5],"yyyy-MM-dd");
			travelerForm.setForecastStartOut(customDate);
			
		}
		//预计约签时间
		if(null != travelerArray[6] && !"".equals(travelerArray[6])){
			String customDate = DateUtils.formatCustomDate((Date)travelerArray[6],"yyyy-MM-dd");
			travelerForm.setForecastContract(customDate);
			
		}
		//实际出团时间
		if(null != travelerArray[7] && !"".equals(travelerArray[7])){
			String customDate = DateUtils.formatCustomDate((Date)travelerArray[7],"yyyy-MM-dd" );
			travelerForm.setStartOut(customDate);
			
		}
		//实际约签时间
		if(null != travelerArray[8] && !"".equals(travelerArray[8])){
			String customDate = DateUtils.formatCustomDate((Date)travelerArray[8],"yyyy-MM-dd HH:mm");
			travelerForm.setContract(customDate);
		}
		//实际送签时间	
		if(null != travelerArray[32] && !"".equals(travelerArray[32])){
			String customDate = DateUtils.formatCustomDate((Date)travelerArray[32],"yyyy-MM-dd");
			travelerForm.setDeliveryTime(customDate);
	
		}
		//签证状态
		if(null != travelerArray[9] && !"".equals(String.valueOf(travelerArray[9]))){
			travelerForm.setVisaStatus(travelerArray[9]+"");
		}
		//护照状态
		if(null != travelerArray[10] && !"".equals(String.valueOf(travelerArray[10]))){
			travelerForm.setPassportStatus(travelerArray[10]+"");
		}
		//担保状态
		if(null != travelerArray[11] && !"".equals(String.valueOf(travelerArray[11]))){
			travelerForm.setGuaranteeStatus(travelerArray[11]+"");
		}
		//付款状态
		if(null != travelerArray[26] && !"".equals(String.valueOf(travelerArray[26]))){
			int count = 0;
			List<String> yflist = getMoneyAndId(travelerArray[26].toString());
			for(int i=0;i<yflist.size();i++){
				if(yflist.get(i).contains(" 0.00")){
					count++;
				}
			}
			if(null != yflist && count != yflist.size()){
				travelerForm.setFukuanStatus("已付");
			}else{
				travelerForm.setFukuanStatus("未付");
			}
		}else{
			travelerForm.setFukuanStatus("未付");
		}
		//应收押金
		if(null != travelerArray[12] && !"".equals(String.valueOf(travelerArray[12]))){
			travelerForm.setTotalDeposit(getMoney(travelerArray[12]+"",null));
			travelerForm.setTotalDepositUUID(String.valueOf(travelerArray[12]));
			//总押金数 用,相隔
			travelerForm.setTotalYajinJine(getTotalMoney(String.valueOf(travelerArray[12])).get("jine"));
			//总押金的货币id 用,相隔
			travelerForm.setTotalYajinHuobi(getTotalMoney(String.valueOf(travelerArray[12])).get("huobi"));
		}
		//=======================交押金按钮的显示和隐藏===================================
		//应收金额为空,不显示付款按钮
    	if(null == travelerArray[12] || "".equals(travelerArray[12])) {
    		travelerForm.setPayButtonFlag("false");
    	//应付金额不为空,已付总金额为空,显示付款按钮
    	}else if(null == travelerArray[20] || "".equals(travelerArray[20])){
    		travelerForm.setPayButtonFlag("true");
    		//从应付金额中获取,货币列表和货币金额,作为参数
    		travelerForm.setJine(getMoneyId(travelerArray[12].toString()).get("jine"));
    		travelerForm.setHuobiId(getMoneyId(travelerArray[12].toString()).get("huobiId"));
    		
    		//游客应收总金额
    		travelerForm.setTotalOrderJine(getMoneyId(travelerArray[12].toString()).get("jine"));
    		//游客应收总金额的货币列表
    		travelerForm.setTotalOrderHuobi(getMoneyId(travelerArray[12].toString()).get("huobiId"));
    		
    	}else{
    		Map<String, String> map = getChajia_yajin(travelerArray[12].toString(),travelerArray[20].toString());
    		//通过计算后,没有任何结果,则不显示付款按钮
    		if(null == map ){
    			travelerForm.setPayButtonFlag("fasle");
    		}else{
    			travelerForm.setPayButtonFlag("true");
    			//游客剩余没有支付的金额
    			travelerForm.setJine(map.get("jine"));
    			//游客剩余没有支付的金额货币列表
    			travelerForm.setHuobiId(map.get("huobiId"));
    			
    			//游客应收总金额
        		travelerForm.setTotalOrderJine(getMoneyId(travelerArray[12].toString()).get("jine"));
        		//游客应收总金额的货币列表
        		travelerForm.setTotalOrderHuobi(getMoneyId(travelerArray[12].toString()).get("huobiId"));
    		}
    	}
    	
    	//=========================付款按钮的显示和隐藏========================================
    	//订单应收金额为空,不显示付款按钮
    	if(null == travelerArray[25] || "".equals(travelerArray[25])) {
    		travelerForm.setFukuanButtonFlag("false");
    		//输出游客的应收金额、已付金额
//    		travelerForm.setYingshouMoney("");
//    		travelerForm.setYingshouCurrencyId("");
//    		travelerForm.setYifuMoney("");
//    		travelerForm.setYifuCurrencyId("");

    	//应付金额不为空,已付总金额为空,显示付款按钮
    	}else if(null == travelerArray[26] || "".equals(travelerArray[26])){
    		travelerForm.setFukuanButtonFlag("true");
    		//从应付金额中获取,货币列表和货币金额,作为参数
    		travelerForm.setFukuanjine(getMoneyId(travelerArray[25].toString()).get("jine"));
    		travelerForm.setFukuanhuobiId(getMoneyId(travelerArray[25].toString()).get("huobiId"));
    		
    		//输出游客的应收金额、已付金额
//    		travelerForm.setYingshouMoney(getMoneyId(travelerArray[25].toString()).get("jine"));
//    		String[] yingshouCurrencyId =  getMoneyId(travelerArray[25].toString()).get("huobiId").split(",");
//    		String mark = "";
//    		for(int i=0;i<yingshouCurrencyId.length;i++){
//    			if(i<yingshouCurrencyId.length){
//    				mark += CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(yingshouCurrencyId[i]), "0")+",";
//    			}else{
//    				mark += CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(yingshouCurrencyId[i]), "0");
//    			}	
//    		}
//    		travelerForm.setYingshouCurrencyId(mark);
    		List<String> list = getMoneyAndId(travelerArray[25].toString());
    		travelerForm.setMarkAndMoney(list);
    		
//    		travelerForm.setYifuMoney("");
//    		travelerForm.setYifuCurrencyId("");
    	}else{
    		Map<String, String> map = getChajia_yajin(travelerArray[25].toString(),travelerArray[26].toString());
    		//通过计算后,没有任何结果,则不显示付款按钮
    		if(null == map ){
    			travelerForm.setFukuanButtonFlag("fasle");
    		}else{
    			travelerForm.setFukuanButtonFlag("true");
    			travelerForm.setFukuanjine(map.get("jine"));
    			travelerForm.setFukuanhuobiId(map.get("huobiId"));
    		}
    		//输出游客的应收金额、已付金额
//    		travelerForm.setYingshouMoney(getMoneyId(travelerArray[25].toString()).get("jine"));
//    		String[] yingshouCurrencyId =  getMoneyId(travelerArray[25].toString()).get("huobiId").split(",");
//    		String mark = "";
//    		for(int i=0;i<yingshouCurrencyId.length;i++){
//    			if(i<yingshouCurrencyId.length-1){
//    				mark += CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(yingshouCurrencyId[i]), "0")+",";
//    			}else{
//    				mark += CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(yingshouCurrencyId[i]), "0");
//    			}	
//    		}
//    		travelerForm.setYingshouCurrencyId(mark);
    		List<String> list = getMoneyAndId(travelerArray[25].toString());
    		travelerForm.setMarkAndMoney(list);
    		    		
//    		travelerForm.setYifuMoney(getMoneyId(travelerArray[26].toString()).get("jine"));
//    		String[] yifuCurrencyId = getMoneyId(travelerArray[26].toString()).get("huobiId").split(",");
//    		String yfMark = "";
//    		for(int i=0;i<yifuCurrencyId.length;i++){
//    			if(i<yifuCurrencyId.length-1){
//    				yfMark += CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(yifuCurrencyId[i]), "0")+",";
//    			}else{
//    				yfMark += CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(yifuCurrencyId[i]), "0");
//    			}
//    		}
//    		travelerForm.setYifuCurrencyId(yfMark);
    		List<String> yflist = getMoneyAndId(travelerArray[26].toString());
    		travelerForm.setYfmarkAndMoney(yflist);
    	}
    	//达账金额
    	if(null == travelerArray[30] || "".equals(travelerArray[30])) {
//    		travelerForm.setDazhangMoney("");
//    		travelerForm.setDazhangCurrencyId("");
    	}else{
//    		travelerForm.setDazhangMoney(getMoneyId(travelerArray[30].toString()).get("jine"));
//    		String[] dazhangCurrencyId = getMoneyId(travelerArray[30].toString()).get("huobiId").split(",");
//    		String dzMark = "";
//    		for(int i=0;i<dazhangCurrencyId.length;i++){
//    			if(i<dazhangCurrencyId.length-1){
//    				dzMark += CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(dazhangCurrencyId[i]), "0")+",";
//    			}else{
//    				dzMark += CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(dazhangCurrencyId[i]), "0");
//    			}
//    		}
//    		travelerForm.setDazhangCurrencyId(dzMark);
    		List<String> dzlist = getMoneyAndId(travelerArray[30].toString());
    		travelerForm.setDzmarkAndMoney(dzlist);
    	}
		//达账押金
		if(null != travelerArray[13] && !"".equals(String.valueOf(travelerArray[13]))){
			travelerForm.setAccountedDeposit(getMoney(travelerArray[13]+"",null));
		}
		//游客主键id
		if(null != travelerArray[14] && !"".equals(String.valueOf(travelerArray[14]))){
			travelerForm.setId(String.valueOf(travelerArray[14]));
			List<Object> parmter = new ArrayList<Object>();
    		parmter.add(travelerArray[17]);
    		//parmter.add(UserUtils.getUser().getAgent().getId());
    		parmter.add(travelerArray[14]);
    		List<Map<String, Object>> resultList = visaDao.findBySql(SqlConstant.SEARCH_JIEKUAN,Map.class, parmter.toArray());
    		for(Map<String, Object> listin : resultList) {
    			//借款金额
    			if("borrowAmount".equals(listin.get("myKey"))){
    				if(null != listin.get("myValue") ){
    					travelerForm.setJiekuanAmount(listin.get("myValue").toString());
    				}
    			}
    			//借款备注
    			else if("borrowRemark".equals(listin.get("myKey"))){
    				if(null != listin.get("myValue") ){
    					travelerForm.setJiekuanRemarks(listin.get("myValue").toString());
    				}
    			}
    			//借款币种
    			else if("currencyId".equals(listin.get("myKey"))){
    				if(null != listin.get("myValue") ){
    					String fuhao = currencyService.findCurrency(Long.valueOf(listin.get("myValue").toString())).getCurrencyMark();
    					travelerForm.setJiekuanBizhong(fuhao);
    				}
    			}
    			//借款创建者
    			travelerForm.setJiekuanCreateUser(listin.get("name").toString());
    			}
		}
		//visa表id
		if(null != travelerArray[15] && !"".equals(String.valueOf(travelerArray[15]))){
			travelerForm.setVisaId(String.valueOf(travelerArray[15]));
		}
		//签证产品表的主键id
		if(null != travelerArray[16] && !"".equals(String.valueOf(travelerArray[16]))){
			travelerForm.setVisaProductId(String.valueOf(travelerArray[16]));
		}
		
		//17 visa_order表id
		if(null != travelerArray[17] && !"".equals(String.valueOf(travelerArray[17]))){
			travelerForm.setVisaorderId(String.valueOf(travelerArray[17]));
		}
		//18 visa_order表的订单编号
		if(null != travelerArray[18] && !"".equals(String.valueOf(travelerArray[18]))){
			travelerForm.setVisaorderNo(String.valueOf(travelerArray[18]));
		}
		//19 visa_order表agentId
		if(null != travelerArray[19] && !"".equals(String.valueOf(travelerArray[19]))){
			travelerForm.setAgentinfoId(String.valueOf(travelerArray[19]));
		}
		
		// 订单的锁定状态
		if(null != travelerArray[24] && !"".equals(String.valueOf(travelerArray[24]))){
    		travelerForm.setOrderStatus(travelerArray[24]+"");
    	}
		
		//结算方式
    	if(null != travelerArray[31] &&  !"".equals(travelerArray[31].toString()) && !"1".equals(travelerArray[31].toString())){
    		List<Dict> dictList = findDictByType("payment_type");
    		for(int i=0;i<dictList.size();i++){
    			Dict dict = dictList.get(i);
    			//
    			if(dict.getValue().equals(travelerArray[31].toString())){
    				travelerForm.setPaymentType(dict.getLabel());
    				travelerForm.setPaymentTypeFlag(dict.getLabel());
    			}
    		}
    	}
    	
    	//参团类型
    	if(null != travelerArray[23] && !"".equals(travelerArray[23])){
    		String groupType = travelerArray[23].toString();
    		travelerForm.setGroupType(OrderCommonUtil.getChineseOrderType(groupType));
    		travelerForm.setGroupTypeId(groupType);
    	}else{
    		travelerForm.setGroupType("单办签");
    		travelerForm.setGroupTypeId("6");
    	}
    	
    	//创建者
    	if(null != travelerArray[21] && !"".equals(travelerArray[21])) {
    		travelerForm.setCreatUser(String.valueOf(travelerArray[21]));
    		
    	}
    	//创建时间
    	if(null != travelerArray[22] && !"".equals(travelerArray[22])) {
    		String test = DateUtils.formatCustomDate((Date)travelerArray[22],"yyyy-MM-dd HH:mm:ss");
    		travelerForm.setCreateTime(test);
    	}
    	//更新时间
    	if(null != travelerArray[35] && !"".equals(travelerArray[35])) {
    		String test = DateUtils.formatCustomDate((Date)travelerArray[35],"yyyy-MM-dd HH:mm:ss");
    		travelerForm.setUpdateDate(test);
    	}
    	
    	//游客的主订单id
    	if(null != travelerArray[27] && !"".equals(travelerArray[27])) {
    		travelerForm.setMainOrderId(travelerArray[27].toString());
    	}
    	//游客的主订单编号
    	if(null != travelerArray[28] && !"".equals(travelerArray[28])) {
    		travelerForm.setMainOrderNum(travelerArray[28].toString());
    	}
    	//游客的参团团号
    	if(null != travelerArray[29] && !"".equals(travelerArray[29])) {
    		travelerForm.setCantuantuanhao(travelerArray[29].toString());
    	}
    	//游客借款状态
    	//先用游客的应收金额和达账金额来判断 
    	//相等就是 已还
    	//不相等
    		//查看是否有借款审核成功的记录,
    			//存在的话 已结款
    			//不存在的话 未借款
    	
    	//用travelerArray[26]代替达账金额的uuid,以后直接替换掉
    	//达账金额不是空,进行差价对比,否则直接进行是否有借款申请的判断
    	
    	
    	if(null != travelerArray[30] &&  !"".equals(travelerArray[30].toString()) && null == getChajia(travelerArray[25].toString(), travelerArray[30].toString())){
    		if(StringUtils.isBlank(getTravelerBorrowedMoney2(travelerForm.getVisaorderId(), Long.valueOf(travelerForm.getId())))){
    			//设置为 未借款	
    			travelerForm.setJiekuanStatus("未借");	
    		}else{	
    			travelerForm.setJiekuanStatus("已还");//借款状态(1:未借2:已借3:已还)
    		}
    	}else{
    	
    	String borrowMoneyandCheckStatus = getTravelerBorrowedMoney2(travelerForm.getVisaorderId(), Long.valueOf(travelerForm.getId()));
    	if (null != borrowMoneyandCheckStatus) {
    		//借款
    		String jiekuanStatus = borrowMoneyandCheckStatus.split(",")[1];
    		if("1".equals(jiekuanStatus)){
    			//设置为 审批中	
    			//travelerForm.setJiekuanStatus("审批中");
    			Review review = getTravelerBorrowedStatusAndTime2(travelerForm.getVisaorderId(), Long.valueOf(travelerForm.getId()));
    			travelerForm.setJiekuanStatus(ReviewCommonUtil.getNextReview(review.getId()));
    			if(null != review ){
    				
    				String customDate = DateUtils.formatCustomDate((Date)review.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
    				
    				travelerForm.setJiekuanTime(customDate);
    			}
    		//2 表示审核成功 设置已 借款	
    		}else if("2".equals(jiekuanStatus)){
    			//travelerForm.setJiekuanStatus("已借");//借款状态(1:未借2:已借3:已还)
    			Review review = getTravelerBorrowedStatusAndTime2(travelerForm.getVisaorderId(), Long.valueOf(travelerForm.getId()));
    			travelerForm.setJiekuanStatus(ReviewCommonUtil.getNextReview(review.getId()));
    			if(null != review ){
    				
    				String customDate = DateUtils.formatCustomDate((Date)review.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
    				
    				travelerForm.setJiekuanTime(customDate);
    			}
    		}else if("3".equals(jiekuanStatus)){
    			//设置为 已还	
    			//travelerForm.setJiekuanStatus("已还");
    			Review review = getTravelerBorrowedStatusAndTime2(travelerForm.getVisaorderId(), Long.valueOf(travelerForm.getId()));
    			travelerForm.setJiekuanStatus(ReviewCommonUtil.getNextReview(review.getId()));
    			if(null != review ){
    				
    				String customDate = DateUtils.formatCustomDate((Date)review.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
    				
    				travelerForm.setJiekuanTime(customDate);
    			}
    		}else{
    			//设置为 未借款	
    			travelerForm.setJiekuanStatus("未借");
    		}
		}else{
    		//设置为 未借款	
			travelerForm.setJiekuanStatus("未借");
		}
		
    	}

		// 订单的锁定状态
		if(null != travelerArray[33] && !"".equals(String.valueOf(travelerArray[33]))){
			travelerForm.setActivityLockStatus(travelerArray[33]+"");
		}
		
		//游客预计返佣
		if(null != travelerArray[37] && !"".equals(String.valueOf(travelerArray[37]))){
			travelerForm.setYujiRebates(travelerArray[37]+"");
		}
		
    	return travelerForm;
	}
	/**
	 * 根据订单编号,查询游客信息
	 * @param page
	 * @param orderNo 订单编号
	 * @return List<VisaOrderTravelerResultForm> 游客信息
	 */
	public List<Object> searchTravelerByOrderIds(VisaOrderForm visaOrderForm ,Page<Map<Object, Object>> page,List<Long> orderNoList,String orderBy,String shenfen){
		List<Object> objectList = new ArrayList<Object>(); 
		List<VisaOrderTravelerResultForm> travelerResultForm = new ArrayList<VisaOrderTravelerResultForm>();
		if(null == orderNoList  || orderNoList.size()==0){
			objectList.add(travelerResultForm);
			page.setCount(0);
			objectList.add(page);
			return objectList;
		}
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy("t.id DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		
		
		//签务身份管理订单,根据订单号的列表查询游客信息
		String sql = travlelerSQL(visaOrderForm,SqlConstant.SEARCH_TRAVELER + " and  t.orderId in "+par(orderNoList),shenfen);
		
		
		List<Map<Object,Object>> travelerList =  visaOrderDao.findBySql(page,sql).getList();

		VisaOrderTravelerResultForm travelerForm = null;
		
		
		for (Object obj : travelerList) {
    		Object[] travelerArray=(Object[])obj;
    		travelerForm = new VisaOrderTravelerResultForm();
    		//loadTraveler() 加载数据
    		travelerResultForm.add(loadTraveler(travelerArray,travelerForm));
		}
		
		objectList.add(travelerResultForm);
		objectList.add(page);
		return objectList;
		
	}
	
	/**
	 * 将页面传出的查询条件,拼装成sql,查出符合条件的游客信息
	 * 
	 * */
	private String  travlelerSQL (VisaOrderForm visaOrderForm,String sql,String shenfen){
		//验证参数是否合法
		if(null  == visaOrderForm){
			return null;
		}
		StringBuffer sqlBuffer = new StringBuffer(sql);
		//参数列表
		User user = UserUtils.getUser();
		if(null != user.getRoleList() && user.getRoleList().size()>0){
			for(int i=0;i<user.getRoleList().size();i++){
				if("9".equals(user.getRoleList().get(i).getRoleType())){
					break;
				}
			}
		}
		//是系统管理员,可以看到本公司的订单
//		if(adminFlag == false){
//			sqlBuffer.append("and t.orderId in(select vo.id from visa_order vo where vo.create_by in ( select id from sys_user where companyId in ( select companyId from sys_user where companyId = ?  )))");
//			par.add(UserUtils.getUser().getCompany().getId());
//		}else{
//			//销售员
//			if("xiaoshou".equals(shenfen)){
//					boolean createFlag = true;
//					for(int i=0;i<permissionList.size();i++){
//						//查看本公司的
//						if ("visaOrderForSale:operation:searchAllCompany".equals(permissionList.get(i).trim())){
//							sqlBuffer.append("and t.orderId in (select vo.id from visa_order vo where vo.create_by in ( select id from sys_user where companyId in ( select companyId from sys_user where companyId = ?  )))");
//							par.add(UserUtils.getUser().getCompany().getId());
//							createFlag = false;
//							break;
//						}
//					}
//					if(createFlag){
//						sqlBuffer.append("and t.orderId in (select id from visa_order vo where vo.create_by = ? ) ");
//						par.add(UserUtils.getUser().getId());
//					}
//			}else{
//					boolean createFlag = true;
//					for(int i=0;i<permissionList.size();i++){
//						//查看本公司的
//						if ("visaOrderForSale:operation:searchAllCompanyForQW".equals(permissionList.get(i).trim())){
//							sqlBuffer.append("and  t.orderId in (select vo.id from visa_order vo where vo.create_by vo.create_by in ( select id from sys_user where companyId in ( select companyId from sys_user where companyId = ?  )))");
//							//sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_products vp INNER JOIN visa_order vo  on vo.visa_product_id = vp.id  and vp.createBy in ( (select u.id from sys_user u LEFT JOIN sys_user_role ur on ur.userId = u.id LEFT JOIN sys_role sr on sr.id = ur.roleId where sr.deptId in "+getRoleLevelStrByLevelList(depIdList)+" ) )"     );
//							par.add(UserUtils.getUser().getCompany().getId());
//							createFlag = false;
//							break;
//						}
//					}
//					if(createFlag){
//						sqlBuffer.append(" and t.orderId in ( SELECT vo.id  from visa_products vp INNER JOIN visa_order vo  on vo.visa_product_id = vp.id  and vp.createBy  = ? ) ");
//						par.add(UserUtils.getUser().getId());
//				}
//			}
//		}
//		
		//游客名称
		if(null != visaOrderForm.getTravelName() && !"".equals(visaOrderForm.getTravelName().trim())){
			//sqlBuffer.append(" and t.orderId in (SELECT orderId from traveler where name like '%"+visaOrderForm.getTravelName().trim()+"%' ) ");
			sqlBuffer.append(" and  t.name like '%"+visaOrderForm.getTravelName().trim()+"%' ");
		}
		
//		//批次编号
//		if(null != visaOrderForm.getBatchNo() && !"".equals(visaOrderForm.getBatchNo().trim()) ){
//			sqlBuffer.append(" and  br.batch_no =\""+visaOrderForm.getBatchNo()+"\"");
//		} 
//		
//		//操作人
//		if(null != visaOrderForm.getTxnPerson() && !"".equals(visaOrderForm.getTxnPerson().trim()) ){
//			sqlBuffer.append(" and  br.create_user_name like '%"+visaOrderForm.getTxnPerson().trim()+"%' ");
//		} 
//		
//		//记录时间
//		if(null != visaOrderForm.getCreateTimeStart() && !"".equals(visaOrderForm.getCreateTimeStart()) ){
//			sqlBuffer.append(" and  br.create_time >='"+visaOrderForm.getCreateTimeStart()+"'");
//		} 
//		if(null != visaOrderForm.getCreateTimeEnd() && !"".equals(visaOrderForm.getCreateTimeEnd()) ){
//			sqlBuffer.append(" and  br.create_time <='"+visaOrderForm.getCreateTimeEnd()+" 23:59:59.0'");
//		} 
		
		
//		//公共编号的查询条件
//		if(null != visaOrderForm.getCommonCode() && !"".equals(visaOrderForm.getCommonCode().trim())){
//			sqlBuffer.append(" and t.orderId in ( select o.id from visa_order o LEFT JOIN visa_products p on o.visa_product_id = p.id  where o.order_no = ? or p.productCode = ? )");
//			par.add(visaOrderForm.getCommonCode().trim());
//			par.add(visaOrderForm.getCommonCode().trim());
//		}
//		//添加AA码的查询条件
//		if(null != visaOrderForm.getAACode() && !"".equals(visaOrderForm.getAACode().trim())){
//			sqlBuffer.append(" and t.orderId in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.AA_code = ?) ");
//			par.add(visaOrderForm.getAACode().trim());
//		}
//		//渠道ID
////		if(null != visaOrderForm.getAgentinfoId() && !"".equals(visaOrderForm.getAgentinfoId().trim())){
////			sqlBuffer.append(" and vo.agentinfo_id = ? ");
////			par.add(visaOrderForm.getAgentinfoId());
////		}else{
////			//销售身份选择渠道id的时候
////			if("xiaoshou".equals(shenfen)){
////					List<Long> idList = new ArrayList<Long>();
////					List<Agentinfo> agentList = agentinfoService.findAllAgentinfo();
////					if(null != agentList && agentList.size()>0){
////						for(int i=0;i<agentList.size();i++){
////							idList.add(agentList.get(i).getId());
////						}
////						sqlBuffer.append(" and vo.agentinfo_id in "+getRoleLevelStrByLevelList(idList)+" ");
////					}else{
////						sqlBuffer.append(" and vo.agentinfo_id = -1 ");
////					}
////				}else{
////					List<Long> idList = new ArrayList<Long>();
////					List<Agentinfo> agentList = agentinfoDao.findAllAgentinfo(UserUtils.getUser().getCompany().getId());
////					if(null != agentList && agentList.size()>0){
////						for(int i=0;i<agentList.size();i++){
////							idList.add(agentList.get(i).getId());
////						}
////						sqlBuffer.append(" and vo.agentinfo_id in "+getRoleLevelStrByLevelList(idList)+" ");
////					}else{
////						sqlBuffer.append(" and vo.agentinfo_id = -1 ");
////					}
////				}
////			}
//		//创建者编号
//		if(null != visaOrderForm.getCreateBy() && !"".equals(visaOrderForm.getCreateBy().trim())){
//			sqlBuffer.append(" and vo.create_by in (select id  from sys_user where name = ? ) ");
//			par.add(visaOrderForm.getCreateBy().trim());
//		}
//		//订单编号
//		if(null != visaOrderForm.getOrderNo() && !"".equals(visaOrderForm.getOrderNo().trim())){
//			sqlBuffer.append(" and vo.order_no = ? ");
//			par.add(visaOrderForm.getOrderNo());
//		}
//		//支付状态
//		if(null != visaOrderForm.getOrderPayStatus() && !"".equals(visaOrderForm.getOrderPayStatus().trim())){
//			sqlBuffer.append(" and vo.payStatus = ? ");
//			par.add(visaOrderForm.getOrderPayStatus());
//		}else{
//			sqlBuffer.append(" and vo.payStatus in ('1','3','5','99') ");
//		}
//		//参团类型
//		if(null != visaOrderForm.getOrderType() && !"".equals(visaOrderForm.getOrderType().trim())){
//			if (Context.ORDER_STATUS_VISA.equals(visaOrderForm.getOrderType().trim())){
//				sqlBuffer.append(" and vo.id in (SELECT orderId from traveler where main_order_id is null )  ");
//			}else{
//				sqlBuffer.append(" and vo.id in (SELECT orderId from traveler where main_order_id in (SELECT p.id FROM productorder p WHERE p.orderStatus = ? )) ");
//				par.add(visaOrderForm.getOrderType());
//			}
//		}
//		//签证国家编号
//		if(null != visaOrderForm.getSysCountryId() && !"".equals(visaOrderForm.getSysCountryId().trim())){
//			sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_products vp INNER JOIN visa_order vo  on vo.visa_product_id = vp.id  and vp.sysCountryId = ? )");
//			par.add(visaOrderForm.getSysCountryId());
//		}
//		//产品编号
//		if(null != visaOrderForm.getVisaProductId() && !"".equals(visaOrderForm.getVisaProductId().trim())){
//			sqlBuffer.append(" and vo.visa_product_id in ( select vp.id from visa_products vp where vp.productCode = ?  ) ");
//			par.add(visaOrderForm.getVisaProductId());
//		}
		//签证状态
		if(null != visaOrderForm.getVisaStatus() && !"".equals(visaOrderForm.getVisaStatus().trim())){
			sqlBuffer.append(" and v.visa_stauts = "+visaOrderForm.getVisaStatus()+" ");
		}
//		//签证状态
//		if(null != visaOrderForm.getVisaStatus() && !"".equals(visaOrderForm.getVisaStatus().trim())){
//			sqlBuffer.append("and vo.id in ( SELECT vo1.id  from visa_order vo1 INNER JOIN traveler t1 on  vo1.id = t1.orderId inner JOIN visa v1  on  t1.id = v1.traveler_id and v1.visa_stauts = ?)");
//			par.add(visaOrderForm.getVisaStatus());
//		}
//		//签证类型
//		if(null != visaOrderForm.getVisaType() && !"".equals(visaOrderForm.getVisaType().trim())){
//			sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_products vp INNER JOIN visa_order vo  on vo.visa_product_id = vp.id  and vp.visaType = ? ) ");
//			par.add(visaOrderForm.getVisaType());
//		}
		

		//借款状态
		if(null != visaOrderForm.getJiekuanStatus() && !"".equals(visaOrderForm.getJiekuanStatus().trim())){
			//1 审核中
			if("1".equals(visaOrderForm.getJiekuanStatus().trim())){
				sqlBuffer.append(" and re.status = 1 ");
				
				//2 已借
			}if("2".equals(visaOrderForm.getJiekuanStatus().trim())){
				sqlBuffer.append(" and (re.status = 2 or re.status = 3) ");
				//3 未借
			}if("3".equals(visaOrderForm.getJiekuanStatus().trim())){
				sqlBuffer.append(" and (re.status =0 or re.status=4 or re.status is null) ");
			}
		}


		
		//借款时间
		if((null != visaOrderForm.getJiekuanTimeStart() && !"".equals(visaOrderForm.getJiekuanTimeStart())) || 
		   (null != visaOrderForm.getJiekuanTimeEnd() && !"".equals(visaOrderForm.getJiekuanTimeEnd()))){
			//预计约签时间-开始时间
			if(null != visaOrderForm.getJiekuanTimeStart() && !"".equals(visaOrderForm.getJiekuanTimeStart().trim())){
				sqlBuffer.append(" and t.id in ( select travelerId  from review where productType = 6 and  flowType = 5 and createDate BETWEEN '"+visaOrderForm.getJiekuanTimeStart() + " 00:00:00"+"' ");
			}else{
				sqlBuffer.append(" and t.id in ( select travelerId from review where productType = 6 and  flowType = 5 and createDate BETWEEN '1987-09-12 00:00:00' ");
			}
			//预计约签时间-结束时间
			if(null != visaOrderForm.getJiekuanTimeEnd() && !"".equals(visaOrderForm.getJiekuanTimeEnd().trim())){
				sqlBuffer.append(" and '"+visaOrderForm.getJiekuanTimeEnd() + " 23:59:59"+"') ");
				
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59')  ");
			}
		}
		
		//预计出团时间-开始时间
		//预计出团时间-结束时间
		if((null != visaOrderForm.getForecastStartOutStart() && !"".equals(visaOrderForm.getForecastStartOutStart())) ||(
				   null != visaOrderForm.getForecastStartOutEnd () && !"".equals(visaOrderForm.getForecastStartOutEnd()))){
					//预计出团时间-开始时间
					if(null != visaOrderForm.getForecastStartOutStart() && !"".equals(visaOrderForm.getForecastStartOutStart().trim())){
						sqlBuffer.append(" and v.forecast_start_out BETWEEN '"+visaOrderForm.getForecastStartOutStart()+" 00:00:00"+"' ");
					}else{
						sqlBuffer.append(" and v.forecast_start_out BETWEEN '1987-09-12 00:00:00' ");
					}
					//预计出团时间-结束时间
					if(null != visaOrderForm.getForecastStartOutEnd () && !"".equals(visaOrderForm.getForecastStartOutEnd().trim())){
						sqlBuffer.append(" and '"+visaOrderForm.getForecastStartOutEnd()+" 23:59:59"+"' ");
					}else{
						sqlBuffer.append(" and '2099-12-12 23:59:59'  ");
					}
				}
		
		//预计约签时间-开始时间
		//预计约签时间-结束时间
		if((null != visaOrderForm.getForecastContractStart() && !"".equals(visaOrderForm.getForecastContractStart())) || 
		   (null != visaOrderForm.getForecastContractEnd() && !"".equals(visaOrderForm.getForecastContractEnd()))){
			//预计约签时间-开始时间
			if(null != visaOrderForm.getForecastContractStart() && !"".equals(visaOrderForm.getForecastContractStart().trim())){
				sqlBuffer.append(" and v.forecast_contract BETWEEN '"+visaOrderForm.getForecastContractStart()+" 00:00:00"+"' ");
			}else{
				sqlBuffer.append(" and v.forecast_contract BETWEEN '1987-09-12 00:00:00' ");
			}
			//预计约签时间-结束时间
			if(null != visaOrderForm.getForecastContractEnd() && !"".equals(visaOrderForm.getForecastContractEnd().trim())){
				sqlBuffer.append(" and '"+visaOrderForm.getForecastContractEnd()+" 23:59:59"+"' ");
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59'  ");
			}
		}
		
		
		//实际出团时间-开始时间
		//实际出团时间-结束时间
		if((null != visaOrderForm.getStartOutStart() && !"".equals(visaOrderForm.getStartOutStart()) || 
		   (null != visaOrderForm.getStartOutEnd() && !"".equals(visaOrderForm.getStartOutEnd())))){
			//实际出团时间-开始时间
			if(null != visaOrderForm.getStartOutStart() && !"".equals(visaOrderForm.getStartOutStart().trim())){
				//sqlBuffer.append(" and v.start_out  BETWEEN ?  ");
				sqlBuffer.append(" and v.start_out BETWEEN '"+visaOrderForm.getStartOutStart()+" 00:00:00"+"' ");
			}else{
				//sqlBuffer.append(" and  v.start_out BETWEEN '2014-09-12 00:00:00' ");
				sqlBuffer.append(" and v.start_out BETWEEN '1987-09-12 00:00:00' ");
			}
			//实际出团时间-结束时间
			if(null != visaOrderForm.getStartOutEnd() && !"".equals(visaOrderForm.getStartOutEnd().trim())){
				sqlBuffer.append(" and '"+visaOrderForm.getStartOutEnd()+" 23:59:59"+ "' ");
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59'  ");
			}
		}
		
		//实际签约时间-开始时间
		//实际签约时间-结束时间
		if((null != visaOrderForm.getContractStart() && !"".equals(visaOrderForm.getContractStart())) || 
		   (null != visaOrderForm.getContractEnd() && !"".equals(visaOrderForm.getContractEnd()))){
			//实际签约时间-开始时间
			if(null != visaOrderForm.getContractStart() && !"".equals(visaOrderForm.getContractStart().trim())){
				//sqlBuffer.append(" and v.contract  BETWEEN ?  ");
				sqlBuffer.append(" and v.contract BETWEEN '"+visaOrderForm.getContractStart()+" 00:00:00"+"' ");
			}else{
				//sqlBuffer.append(" and  v.contract BETWEEN '2014-09-12 00:00:00' ");
				sqlBuffer.append(" and v.contract BETWEEN '1987-09-12 00:00:00' ");
			}
			//实际签约时间-结束时间
			if(null != visaOrderForm.getContractEnd() && !"".equals(visaOrderForm.getContractEnd().trim())){
				sqlBuffer.append(" and  '"+visaOrderForm.getContractEnd()+" 23:59:59"+"' ");
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59'  ");
			}
		}

		
		//下单时间-开始时间
		//下单时间-结束时间
		if((null != visaOrderForm.getCreateDateStart() && !"".equals(visaOrderForm.getCreateDateStart())) || 
		   (null != visaOrderForm.getCreateDateEnd() && !"".equals(visaOrderForm.getCreateDateEnd()))){
			//下单时间-开始时间
			if(null != visaOrderForm.getCreateDateStart() && !"".equals(visaOrderForm.getCreateDateStart().trim())){
				sqlBuffer.append(" and vo.create_date  BETWEEN '"+visaOrderForm.getCreateDateStart()+" 00:00:00"+"' ");
			}else{
				sqlBuffer.append(" and  vo.create_date BETWEEN '2014-09-12 00:00:00' ");
			}
			//下单时间-结束时间
			if(null != visaOrderForm.getCreateDateEnd() && !"".equals(visaOrderForm.getCreateDateEnd().trim())){
				sqlBuffer.append(" and  '"+visaOrderForm.getCreateDateEnd()+" 23:59:59"+"' ");
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59'  ");
			}
		}
		return sqlBuffer.toString();
	}
	
	/**
	 * 根据订单编号,查询游客信息
	 * @param page
	 * @param orderNo 订单编号
	 * @return List<VisaOrderTravelerResultForm> 游客信息
	 */
	public VisaOrderTravelerResultForm searchTravelerByOrder(String trivateId,String orderNo){
		List<VisaOrderTravelerResultForm> travelerResultForm = new ArrayList<VisaOrderTravelerResultForm>();
		if(null == orderNo || "".equals(orderNo)){
			return null;
		}
		Object[] par  = new Object[] {orderNo,trivateId};
		
		String sql = SqlConstant.SEARCH_TRAVELER + " and t.orderId = ? and t.id= ?   ";
		List<Object> travelerList =  visaOrderDao.findBySql(sql, par);
		VisaOrderTravelerResultForm travelerForm = null;
		for (Object obj : travelerList) {
    		Object[] travelerArray=(Object[])obj;
    		travelerForm = new VisaOrderTravelerResultForm();
    		travelerResultForm.add(loadTraveler(travelerArray,travelerForm));
		}
		return travelerResultForm.get(0);
		
	}
	/**
	 * 根据订单编号,查询游客信息
	 * @param page
	 * @param orderNo 订单编号
	 * @return List<VisaOrderTravelerResultForm> 游客信息
	 */
	public List<VisaOrderTravelerResultForm> searchTravelerByOrder(String orderNo,String orderCode,List<?> list){
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list2 = (List<Map<String, Object>>) list;
		List<VisaOrderTravelerResultForm> travelerResultForm = new ArrayList<VisaOrderTravelerResultForm>();
		if(null == orderNo || "".equals(orderNo)){
			return null;
		}
		Object[] par  = new Object[] {orderNo};
		String sql = SqlConstant.SEARCH_TRAVELER + " and t.orderId = ? ";
		List<Object> travelerList =  visaOrderDao.findBySql(sql, par);
		VisaOrderTravelerResultForm travelerForm = null;
		for (Object obj : travelerList) {
    		Object[] travelerArray=(Object[])obj;
    		travelerForm = new VisaOrderTravelerResultForm();
    		VisaOrderTravelerResultForm form = loadTraveler(travelerArray,travelerForm);
    		for(int i=0;i<list2.size();i++)
    		{
    			if(orderCode.equals(list2.get(i).get("orderNum")) && form.getId().equals(list2.get(i).get("id").toString()))
    				form.setShowFlag("F");// 标示付款按钮不需要显示	
    		}
    		
    		travelerResultForm.add(form);
    		
		}
		return travelerResultForm;
		
	}
	
	
	/**
	 * 查询币种信息
	 */
	public List<Currency> findAllCurrency(){
		List<Currency> currencyList =new ArrayList<Currency>();
		Iterable<Currency> currencyIterable = currencyDao.findAll();
		Iterator<Currency> currencyIterator = null;
		if(currencyIterable != null) {
			currencyIterator = currencyIterable.iterator();
			while(currencyIterator.hasNext()) {
				Currency currency = currencyIterator.next();
				currencyList.add(currency);
			}
		}
		return currencyList;
	}
	/**
	 * 查询字典表中的数据
	 * @param type 字典表中的 type 字段
	 */
	public List<Dict> findDictByType(String type){
		List<Dict> dictList = dictDao.findByType(type);
		return dictList;
	}
	
	/**
	 * 查询字典表中的数据
	 * @param type 字典表中的 type 字段
	 */
	public List<Map<String,Object>> findDictByType4Map(String type){
		StringBuffer sql=new StringBuffer();
		sql.append(" SELECT sd.value,sd.label ")
		   .append(" FROM sys_dict sd  ")
		   .append(" WHERE sd.type='"+type+"'  ")
		   .append(" AND sd.delFlag=0  ")
		   .append(" ORDER BY sd.sort ");
		List<Map<String,Object>> dictMapList = dictDao.findBySql(sql.toString(),Map.class);
		
		return dictMapList;
	}
	
	/**
	 * 根据登陆者所在公司,找到所有的下过订单的人
	 * */
	public List<String> findCreateBy(){
		
		List<String>  createByList = new ArrayList<String>();
		String sql = "SELECT DISTINCT u.name from visa_order v inner JOIN sys_user u on v.create_by = u.id and u.companyId = "+UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> resultList = dictDao.findBySql(sql,Map.class);
		
		for(Map<String, Object> listin : resultList) {
			//借款金额
			if(null != listin.get("name") && !"".equals(listin.get("name").toString())){
				createByList.add(listin.get("name").toString());
			}
		}
		return createByList;
	}
	
	
	/**
	 * 根据登陆者所在公司,找到所有的下过订单的人,根据Id进行分组，忽略重名
	 */
	public List<String> findVisaOrderCreateBy(){
		
		List<String>  createByList = new ArrayList<String>();
		String sql = "SELECT u.id, u.name from visa_order v inner JOIN sys_user u on v.create_by = u.id and u.companyId = "
		+UserUtils.getUser().getCompany().getId() +" group by u.id";
		
		// System.out.println(sql);
		
		List<Map<String, Object>> resultList = dictDao.findBySql(sql,Map.class);
		
		for(Map<String, Object> listin : resultList) {
			//借款金额
			if(null != listin.get("name") && !"".equals(listin.get("name").toString())){
				createByList.add(listin.get("id").toString()+","+listin.get("name").toString());
			}
		}
		return createByList;
	}
	
	
	
	/**
	 * 根据登陆者所在公司,找到所有的下过订单的人,根据Id进行分组，忽略重名
	 */
	public List<Map<String, Object>> findVisaOrderCreateBy1(){
		
		
		String sql = "SELECT u.id, u.name from visa_order v inner JOIN sys_user u on v.create_by = u.id and u.companyId = "
		+UserUtils.getUser().getCompany().getId() +" group by u.id";
		
		// System.out.println(sql);
		
		List<Map<String, Object>> resultList = dictDao.findBySql(sql,Map.class);
		
		return resultList;
	}
	
	/**
	 * 签务身份管理订单
	 * 查询游客列表
	 * @param visaOrder
	 */
	@SuppressWarnings("unchecked")
	public List<Object> QWSearchTravelList(Page<Map<Object, Object>> page, VisaOrderForm visaOrderForm, 
    		String orderBy, String shenfen, DepartmentCommon common) {
		
		//签务身份-根据条件查询订单信息
		StringBuffer sqlBuffer = new StringBuffer(SqlConstant.QW_SEARCH_ORDER);
		List<Map<Object, Object>> resultList = (List<Map<Object, Object>>)qianwuSearchcreateSQL(visaOrderForm, sqlBuffer, shenfen, null, common);
		List<Long> ids = new ArrayList<Long>();
		for(Map<Object, Object> listin : resultList) {
			
			ids.add(Long.valueOf(listin.get("orderId").toString()));
		}
		return searchTravelerByOrderIds(visaOrderForm,page,ids,orderBy,shenfen);
	}
	
	/**
	 * 签证订单
	 * 查询订单列表
	 * @param VisaOrderForm 查询条件类
	 * @param orderBy 排序字段
	 * @param shenfen 身份
	 * @param visaOrder
	 */
	@SuppressWarnings("unchecked")
	public List<Object> QWsearchVisaOrder(Page<Map<Object, Object>> page, VisaOrderForm visaOrderForm, 
    		String orderBy, String shenfen, DepartmentCommon common) {
		
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy("vo.create_date DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		//根据条件查询订单信息
		StringBuffer sqlBuffer = new StringBuffer(SqlConstant.QW_SEARCH_ORDER);
		//订单列表信息
		Page<Map<Object, Object>> result = (Page<Map<Object, Object>>)qianwuSearchcreateSQL(visaOrderForm, sqlBuffer, shenfen, page, common);
		
		List<Map<Object, Object>> resultList = result.getList();
		//结果集
		List<VisaOrderResultForm> resultFormList = new ArrayList<VisaOrderResultForm>();
		VisaOrderResultForm visaOrderResultForm = null;
		List<?> list1 = getOldVisaData();
		
		for(Map<Object, Object> listin : resultList) {
			visaOrderResultForm = new VisaOrderResultForm();
			//付款状态
			visaOrderResultForm.setPayStatus(listin.get("payStatus") == null ? 1 : Integer.valueOf(String.valueOf(listin.get("payStatus"))));
			
			//锁定状态
			visaOrderResultForm.setLockStatus(listin.get("lockStatus") == null ? 0 : Integer.valueOf(String.valueOf(listin.get("lockStatus"))));
			//产品锁定状态
			visaOrderResultForm.setActivityLockStatus(listin.get("activityLockStatus") == null ? 0 : Integer.valueOf(String.valueOf(listin.get("activityLockStatus"))));
			//visa表的主键
        	if(null != listin.get("orderId") && !"".equals(listin.get("orderId"))) {
        		//预计返佣List
        		List<String> yujiRebatesSerialNumList = new ArrayList<String>();
        		//订单预计团队返佣
        		if(null != listin.get("groupRebate") && !"".equals(listin.get("groupRebate"))) {
        			MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(listin.get("groupRebate").toString());
        			if(moneyAmount != null){
	        			BigDecimal groupRebate = moneyAmount.getAmount();
	        			if(groupRebate.compareTo(BigDecimal.ZERO)!=0){
	        				yujiRebatesSerialNumList.add(listin.get("groupRebate").toString());
	        			}
        			}
        		}
        		visaOrderResultForm.setOrderId(String.valueOf(listin.get("orderId")));
        		visaOrderResultForm.setOrderCode(String.valueOf(listin.get("order_no")));
        		//获取办签人的信息
        		List<VisaOrderTravelerResultForm>  traveList = searchTravelerByOrder(visaOrderResultForm.getOrderId(),visaOrderResultForm.getOrderCode(),list1);
    			if(null != traveList && traveList.size()>0){
	        		for(int i=0;i<traveList.size();i++){
	    				if(null != traveList.get(i).getPaymentTypeFlag() &&  !"".equals(traveList.get(i).getPaymentTypeFlag())){
	    					visaOrderResultForm.setPaymentType(traveList.get(i).getPaymentTypeFlag());
	    					break;
	    				}
	    			}
					//每个游客的预计返佣
					for(int i=0;i<traveList.size();i++){
						if(null != traveList.get(i).getYujiRebates() && !"".equals(traveList.get(i).getYujiRebates())){
							MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(traveList.get(i).getYujiRebates());
							if(moneyAmount != null){
								BigDecimal travelerRebate = moneyAmount.getAmount();
								if(travelerRebate.compareTo(BigDecimal.ZERO)!=0){
									yujiRebatesSerialNumList.add(traveList.get(i).getYujiRebates());
								}
							}
						}
					}
					//实际返佣
					//根据订单ID查询签证返佣审核通过的记录
					List<Review> reviewList = reviewDao.findReviewIdByOrderId(6, 9, 2, String.valueOf(listin.get("orderId")));
					if(null != reviewList && reviewList.size()>0){
						//最近一次通过的审核记录
						Map<String,String> reviewMap = reviewService.findReview(reviewList.get(0).getId());
						//实际返佣json串
						String shijiRebatesJson = reviewMap.get("totalRebatesJe");
						//累计返佣!=0
						if(!"0".equals(shijiRebatesJson)) {
							JSONObject jsonObject = JSONObject.fromObject(shijiRebatesJson);
							Iterator<String> it = jsonObject.keys();
							StringBuffer shijiRebatesBuf = new StringBuffer();
							while (it.hasNext()) {
								//币种符号
								String key = String.valueOf(it.next());
								Currency currency = currencyDao.findOne(Long.valueOf(key));
								shijiRebatesBuf.append(currency.getCurrencyMark());
								//金额
								String value = (String) jsonObject.get(key);
								DecimalFormat df = new DecimalFormat("#,##0.00");
								String money = df.format(new BigDecimal(value));
								shijiRebatesBuf.append(money);
								//多个币种相加
								shijiRebatesBuf.append("+");
							}
							//去掉最后一个+
							String shijiRebates = shijiRebatesBuf.substring(0, shijiRebatesBuf.length()-1);
							visaOrderResultForm.setShijiRebates(shijiRebates);
						}else{
							visaOrderResultForm.setShijiRebates("¥ 0");
						}
					}else{
						visaOrderResultForm.setShijiRebates("¥ 0");
					}
    			}
				//多个uuid相加
				String yujiRebates = OrderCommonUtil.getMoneyAmountBySerialNum(yujiRebatesSerialNumList, 2);
				visaOrderResultForm.setYujiRebates(yujiRebates);
        		visaOrderResultForm.setVisaOrderTravelerResultForm(traveList);
        	}
        	//渠道Id
        	if(null != listin.get("agentId") && !"".equals(listin.get("agentId"))) {
        		visaOrderResultForm.setAgentId(String.valueOf(listin.get("agentId")));
        	}
			//预定渠道
        	if(null != listin.get("agentName") && !"".equals(listin.get("agentName"))) {
        		visaOrderResultForm.setAgentinfoName(String.valueOf(listin.get("agentName")));
        	}else{
        		visaOrderResultForm.setAgentinfoName("非签约渠道");
        	}
        	//订单编号
        	if(null != listin.get("order_no") && !"".equals(listin.get("order_no"))) {
        		visaOrderResultForm.setOrderCode(String.valueOf(listin.get("order_no")));
        	}
        	
        	//订单团号
        	if(null != listin.get("orderTuanhao") && !"".equals(listin.get("orderTuanhao"))){
        		visaOrderResultForm.setOrderTuanhao(String.valueOf(listin.get("orderTuanhao")));
        	}
        	
        	//领区联系人
        	if(null != listin.get("contactPerson") && !"".equals(listin.get("contactPerson"))){
        		visaOrderResultForm.setContactPerson(String.valueOf(listin.get("contactPerson")));
        	}
        	
        	//产品编号
        	if(null != listin.get("visa_product_id") && !"".equals(listin.get("visa_product_id"))) {
        		visaOrderResultForm.setProductCode(String.valueOf(listin.get("visa_product_id")));
        	}
        	//参团类型
        	if(null != listin.get("group_type") && !"".equals(listin.get("group_type"))){
        		visaOrderResultForm.setGroupTypeId(listin.get("group_type").toString());
        		String groupType = listin.get("group_type").toString();
        		visaOrderResultForm.setGroupType(OrderCommonUtil.getChineseOrderType(groupType));
        	}else{
        		visaOrderResultForm.setGroupType("单办签");
        		visaOrderResultForm.setGroupTypeId("6");
        	}
        	//创建者
        	if(null != listin.get("create_by") && !"".equals(listin.get("create_by"))) {
        		visaOrderResultForm.setCreatUser(String.valueOf(listin.get("create_by")));
        	}
        	//创建时间
        	if(null != listin.get("create_date") && !"".equals(listin.get("create_date"))) {
        		String test = DateUtils.formatCustomDate((Date)listin.get("create_date"),"yyyy-MM-dd HH:mm:ss");
        		visaOrderResultForm.setCreateTime(test);
        	}
        	//游客个数
        	if(null != listin.get("travel_num") && !"".equals(listin.get("travel_num"))) {
        		visaOrderResultForm.setTravelerCount(String.valueOf(listin.get("travel_num")));
        	}
        	//应收金额
        	if(null != listin.get("visaPay") && !"".equals(listin.get("visaPay"))) {
        		visaOrderResultForm.setVisaPay(getMoney(String.valueOf(listin.get("visaPay")),null));
        	}
        	// 已付总金额
        	if(null != listin.get("payed_money") && !"".equals(listin.get("payed_money"))) {
        		
        		//参数列表
//        		List<Object> par = new ArrayList<Object>();
//        		par.add();
//        		par.add();
//        		//查询此游客有没有被驳回的记录
//        		List<Map<Object, Object>> bohuiList = visaOrderDao.findBySql(SqlConstant.SEARCH_BOHUI,Map.class);
//        		//有的话,应收金额就是
//        		if(null != bohuiList){
//        			
//        		}
        		String payedMoney = getMoney(String.valueOf(listin.get("payed_money")),null);
        		visaOrderResultForm.setPayedMoney(payedMoney);
        	}
        	//应收金额为空,不显示付款按钮
        	if(null == listin.get("visaPay") || "".equals(listin.get("visaPay"))) {
        		visaOrderResultForm.setPayButtonFlag("false");
        	//应付金额部位空,已付总金额为空,显示付款按钮
        	}else if(null == listin.get("payed_money") || "".equals(listin.get("payed_money"))){
        		visaOrderResultForm.setPayButtonFlag("true");
        		visaOrderResultForm.setJine(getMoneyId(listin.get("visaPay").toString()).get("jine"));
        		visaOrderResultForm.setHuobiId(getMoneyId(listin.get("visaPay").toString()).get("huobiId"));
        		
        	}else{
        		Map<String, String> map = getChajia(listin.get("visaPay").toString(),listin.get("payed_money").toString());
        		//通过计算后,没有任何结果,则不显示付款按钮
        		if(null == map ){
        			visaOrderResultForm.setPayButtonFlag("fasle");
        		}else{
        			visaOrderResultForm.setPayButtonFlag("true");
        			visaOrderResultForm.setJine(map.get("jine"));
        			visaOrderResultForm.setHuobiId(map.get("huobiId"));
        		}
        	}
        	// 到账总金额
        	if(null != listin.get("accounted_money") && !"".equals(listin.get("accounted_money"))) {
        		visaOrderResultForm.setAccountedMoney(getMoney(String.valueOf(listin.get("accounted_money")),null));
        	}
//        	// 主订单id
//        	if(null != listin.get("mainOrderCode") && !"".equals(listin.get("mainOrderCode"))) {
//        		visaOrderResultForm.setMainOrderCode(listin.get("mainOrderCode")+"");
//        	}
//        	// 主订单编号
//            if(null != listin.get("orderNum") && !"".equals(listin.get("orderNum"))) {
//            		visaOrderResultForm.setMainOrderNum(listin.get("orderNum")+"");
//            }
        	// 订单团号
        	if(null != listin.get("orderTuanhao") && !"".equals(listin.get("orderTuanhao"))) {
        		visaOrderResultForm.setOrderTuanhao(listin.get("orderTuanhao")+"");
        	}
        	// 参团团号
        	if(null != listin.get("cantuanTuanhao") && !"".equals(listin.get("cantuanTuanhao"))) {
        		visaOrderResultForm.setCantuanTuanhao(listin.get("cantuanTuanhao")+"");
        	}
        	// 计调员姓名
        	if(null != listin.get("productCreateUser") && !"".equals(listin.get("productCreateUser"))) {
        		visaOrderResultForm.setProductCreateUser(listin.get("productCreateUser")+"");
        	}
        	// 开团时间
        	if(null != listin.get("groupOpenDate") && !"".equals(listin.get("groupOpenDate"))) {
        		visaOrderResultForm.setGroupOpenDate(listin.get("groupOpenDate")+"");
        	}
        	//截团时间
        	if(null != listin.get("groupCloseDate") && !"".equals(listin.get("groupCloseDate"))) {
        		visaOrderResultForm.setGroupCloseDate(listin.get("groupCloseDate")+"");
        	}
            // 订单的锁定状态
            if(null != listin.get("lockStatus") && !"".equals(listin.get("lockStatus"))) {
            		visaOrderResultForm.setOrderStatus(listin.get("lockStatus")+"");
            }
            
            //签证订单状态
            if(null != listin.get("payStatus") && !"".equals(listin.get("payStatus"))) {
        		visaOrderResultForm.setVisaOrderStatus(listin.get("payStatus").toString());
            }
            //签证产品名称
            if(null != listin.get("productName") && !"".equals(listin.get("productName"))) {
        		visaOrderResultForm.setProductName(listin.get("productName").toString());
            }
            //
            //签证产品名称
            if(null != listin.get("isRead") && !"".equals(listin.get("isRead"))) {
        		visaOrderResultForm.setIsRead(listin.get("isRead").toString());
            }
            //发票状态
            if(null != listin.get("invoiceStatus") && !"".equals(listin.get("invoiceStatus"))) {
            		visaOrderResultForm.setInvoiceStatus("已开发票");
            }else{
            	visaOrderResultForm.setInvoiceStatus("未开发票");
            }
            //收据状态
            if(null != listin.get("receiptStatus") && !"".equals(listin.get("receiptStatus"))) {
        		visaOrderResultForm.setReceiptStatus("已开收据");
            }else{
            	visaOrderResultForm.setReceiptStatus("未开收据");
            }
			//销售名称
			if(null != listin.get("salerName") && !"".equals(listin.get("salerName"))) {
				visaOrderResultForm.setSalerName(String.valueOf(listin.get("salerName")));
			}
            
            
        	resultFormList.add(visaOrderResultForm);
        }
		
		List<Object> list = new ArrayList<Object>();
		
		list.add(resultFormList);
		list.add(page);
		return list;
	}

	public List<?> getOldVisaData() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT t.id,t.name,o.orderNum,vo.create_date FROM orderpay o ");
		sql.append(" LEFT JOIN traveler t on o.traveler_id=t.id");
		sql.append(" LEFT  JOIN money_amount ma  ON o.moneySerialNum = ma.serialNum");
		sql.append(" LEFT JOIN visa_order vo ON o.orderId = vo.id");
		sql.append(" where o.moneySerialNum=t.payed_moneySerialNum");
		sql.append(" AND t.order_type =6 AND t.id IS NOT NULL");
		sql.append(" AND vo.id IS NOT NULL   AND vo.create_date IS NOT NULL  AND o.createDate <='2015-04-16 23:59:59'");
		List<?> list1 = visaOrderDao.findBySql(sql.toString(), Map.class);
		return list1;
	}
	/**
	 * yingshouUUID 应收金额的uuid
	 * yishouUUID 已收金额的uuid
	 * @return null 计算后 应收和已收押金相等;map:不相等的差价
	 * 
	 * */
	private Map<String,String> getChajia_yajin(String yingshouUUID,String yishouUUID){
			
		String yishouSQL = "select amount,currencyId from money_amount where serialNum = '"+yishouUUID+"'";
		String yingshouSQL = "select amount,currencyId from money_amount where serialNum = '"+yingshouUUID+"'";
		
		//查询到多次已收到的钱数
		List<Map<Object, Object>> yishouList = visaOrderDao.findBySql(yishouSQL,Map.class);
		//查询到应收到的钱数
		List<Map<Object, Object>> yingshouList = visaOrderDao.findBySql(yingshouSQL,Map.class);
		
		BigDecimal amount = BigDecimal.ZERO;
		if(null == yingshouList || yingshouList.size()==0){
			return null;
		}
		//没有已收金额的时候,将应收作为结果返回
		else if(null == yishouList || yishouList.size() == 0){
			Map<String,String> map = new HashMap<String, String>();
			map.put("huobiId", yingshouList.get(0).get("currencyId").toString());
			map.put("jine", yingshouList.get(0).get("amount").toString());
			return map;
		}else{
			for(Map<Object, Object> listin : yishouList) {
				//货币id
				 amount = amount.add((BigDecimal)listin.get("amount"));
			}
			Map<String,String> map = new HashMap<String, String>();
			map.put("huobiId", yingshouList.get(0).get("currencyId").toString());
			
			double result = ((BigDecimal)yingshouList.get(0).get("amount")).subtract(amount).doubleValue();
			if(0L == result){
				/**
				 * 签证订单付款除了应收金额必须外，不做其他条件限制
				 * update by jiachen
				 */
				map.put("jine","0.00");
			}else{
				map.put("jine",String.valueOf(result));
			}
			
			return map;
		}
	}
	
	/**
	 * yingshouUUID 应收金额的uuid
	 * yishouUUID 已收金额的uuid
	 * 
	 * */
	private Map<String,String> getChajia(String yingshouUUID,String yishouUUID){
			
		//参数列表
		List<Object> par = new ArrayList<Object>();
		
		par.add(yingshouUUID);
		par.add(yishouUUID);
		List<Map<Object, Object>> resultList = visaOrderDao.findBySql(SqlConstant.JISUAN_CHAJIA,Map.class, par.toArray());
		
		String huobiId="";
		String jine ="";
		int i = 0;
		for(Map<Object, Object> listin : resultList) {
			//货币id
			Object currencyId = listin.get("currencyId");
			//应收金额
			Object big = listin.get("big");
			//计算后的金额
			Object result = listin.get("result");
			//应收金额为空,进行下一次循环 计算结果为0 的时候,直接进行下一次循环,不再操作
			if(null == big || "".equals(big)) {
				continue;
			//应收不为空,已支付为空,则记录货币id和应收金额
			}else if (null == result || "".equals(result)) {
				huobiId = huobiId + currencyId.toString();
				jine = jine+big;
			//应收和已收都不为空,获取计算后的结果
			}else{
				//如果以"-"开头,就不显示付款按钮
				if(result.toString().startsWith("-")){
					/**
					 * 对于应收金额减已付金额小于零的情况，不做付款限制
					 * update by jiachen
					 */
//					return null;
				}
				huobiId = huobiId + currencyId.toString();
				jine = jine+result;
			}
			
			if(i< resultList.size()-1){
				huobiId = huobiId +",";
				jine =  jine+",";
			}
			i++;
		}
		//如果货币id和金额都不为空的话,返回map集合,
		if(null != jine && !"".equals(jine)){
			Map<String,String> map = new HashMap<String, String>();
			map.put("huobiId", huobiId);
			map.put("jine", jine);
			return map;
		}
		return null;
	}
	
	/**
	 * yingshouUUID 应收金额的uuid
	 * yishouUUID 已收金额的uuid
	 * 
	 * */
	public Map<String,String> getChajiaPub(String yingshouUUID,String yishouUUID){
			
		//参数列表
		List<Object> par = new ArrayList<Object>();
		
		par.add(yingshouUUID);
		par.add(yishouUUID);
		List<Map<Object, Object>> resultList = visaOrderDao.findBySql(SqlConstant.JISUAN_CHAJIA,Map.class, par.toArray());
		
		String huobiId="";
		String jine ="";
		int i = 0;
		for(Map<Object, Object> listin : resultList) {
			//货币id
			Object currencyId = listin.get("currencyId");
			//应收金额
			Object big = listin.get("big");
			//计算后的金额
			Object result = listin.get("result");
			//应收金额为空,进行下一次循环 计算结果为0 的时候,直接进行下一次循环,不再操作
			if(null == big || "".equals(big)) {
				continue;
			//应收不为空,已支付为空,则记录货币id和应收金额
			}else if (null == result || "".equals(result)) {
				huobiId = huobiId + currencyId.toString();
				jine = jine+big;
			//应收和已收都不为空,获取计算后的结果
			}else{
				//如果以"-"开头,就不显示付款按钮
				if(result.toString().startsWith("-")){
					/**
					 * 对于应收金额减已付金额小于零的情况，不做付款限制
					 * update by jiachen
					 */
//					return null;
				}
				huobiId = huobiId + currencyId.toString();
				jine = jine+result;
			}
			
			if(i< resultList.size()-1){
				huobiId = huobiId +",";
				jine =  jine+",";
			}
			i++;
		}
		//如果货币id和金额都不为空的话,返回map集合,
		if(null != jine && !"".equals(jine)){
			Map<String,String> map = new HashMap<String, String>();
			map.put("huobiId", huobiId);
			map.put("jine", jine);
			return map;
		}
		return null;
	}
	/**
	 * 公用接口，根据流水号取金额，对多币种进行拼接
	 * @param serialNum 流水号UUID
	 * @param flag 是否添加</br> 的标示位,flag为空,返回的字符串中,带有</br> 返回的格式是  货币符号 货币金额</br>
	 * ;否则,返回值中没有</br> ,返回的格式是  货币符号 货币金额
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public Map<String,String> getMoneyId(String serialNum){
		if(StringUtils.isBlank(serialNum)){
			return null;
		}
		Map<String,String> map = new HashMap<String,String>();
		String sql = "SELECT currencyId,amount from money_amount  where serialNum = '"+ serialNum +"'" ;
		List<Object[]> results = visaOrderDao.getSession().createSQLQuery(sql).list();
		
		//货币的id列表
		String huobiId ="";
		//金额
		String jine ="";
		if(results!=null && results.size()>0){
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				
				huobiId = huobiId + amount[0] ;
				jine = jine + amount[1] ;
				//货币id 以,隔开
				if(i!= results.size() -1){
					huobiId = huobiId + ",";
					jine = jine + ",";
				}
			}
		}
		map.put("huobiId", huobiId);
		map.put("jine", jine);
		return map;
	}
	
	/**
	 * 销售签证订单-游客列表 金额获取
	 * @param serialNum
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public List<String> getMoneyAndId(String serialNum){
		if(StringUtils.isBlank(serialNum)){
			return null;
		}
		List<String> list = new ArrayList<String>();
		String sql = "SELECT currencyId,amount from money_amount  where serialNum = '"+ serialNum +"'" ;
		List<Object[]> results = visaOrderDao.getSession().createSQLQuery(sql).list();
	
		if(results!=null && results.size()>0){
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				String mark = CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(amount[0].toString()), "0");
				String money = amount[1].toString();
				String huobiId = mark+" "+money+"</br>";
				list.add(huobiId);
			}
		}
		return list;
	}
	
	
	/**
	 * 公用接口，根据流水号取金额，对多币种进行拼接
	 * @param serialNum 流水号UUID
	 * @param flag 是否添加</br> 的标示位,flag为空,返回的字符串中,带有</br> 返回的格式是  货币符号 货币金额</br>
	 * ;否则,返回值中没有</br> ,返回的格式是  货币符号 货币金额
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoney(String serialNum,String flag){
		if(StringUtils.isBlank(serialNum)){
			return null;
		}
		String sql = "SELECT m.currencyId,c.currency_name,sum(m.amount),c.currency_mark,m.amount from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum +"' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = visaOrderDao.getSession().createSQLQuery(sql).list();
		
		String money = "";
		
		if(results!=null && results.size()>0){
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				
				DecimalFormat d= new DecimalFormat(",##0.00");
				if(null == flag){
					if(i==0){
					    if(amount[2] != null) {
                            money = money + amount[3] + " " + d.format(new BigDecimal(amount[2].toString()));
                        }
					}else{
						money = money +"</br>"+ amount[3] + " " + d.format(new BigDecimal(amount[2].toString()));
					}
				}else{
					money = money + amount[3] + " " + d.format(new BigDecimal(amount[2].toString()));
				}
			}
		}else{
			if(null == flag){
				return null;
			}
			money = "0";
		}
		return money;
	}
	
	public static  String getRoleLevelStrByLevelList(List<Long> levelList){
		StringBuffer sb=null;
		if (null!=levelList&&levelList.size()>0) {
			sb =  new StringBuffer("(");
			for (Long level: levelList) {
				sb.append(level+",");
			}
			String temp =sb.toString();
			temp = temp.substring(0,temp.length()-1)+",-1 )";
			//System.out.println(temp);
			return temp;
			
		}else {
			return null;
		}
	}

	
	/**
	 * 根据查询条件对象,拼装出SQL和参数列表
	 * @param visaOrderForm 查询条件对象
	 * resultMap sql语句和参数列表的结果集
	 * 
	 * */
	private Object  qianwuSearchcreateSQL (VisaOrderForm visaOrderForm, StringBuffer sqlBuffer, String shenfen, 
			Page<Map<Object, Object>> page, DepartmentCommon common) {
		//验证参数是否合法
		if(null  == visaOrderForm){
			return null;
		}
		//参数列表
		List<Object> par = new ArrayList<Object>();
		
		//游客名称
		if(null != visaOrderForm.getTravelName() && !"".equals(visaOrderForm.getTravelName().trim())){
			String travelName = visaOrderForm.getTravelName().replace("'", "''");
			sqlBuffer.append(" and vo.id in (SELECT orderId from traveler where order_type='6' and delFlag='0' and name like '%"+travelName.trim()+"%' ) ");
		}
		//公共编号的查询条件
		if(null != visaOrderForm.getCommonCode() && !"".equals(visaOrderForm.getCommonCode().trim())){
			sqlBuffer.append(" and ( vo.order_no = ? or vp.productCode = ? )");
			par.add(visaOrderForm.getCommonCode().trim());
			par.add(visaOrderForm.getCommonCode().trim());
		}
		//添加AA码的查询条件
		if(null != visaOrderForm.getAACode() && !"".equals(visaOrderForm.getAACode().trim())){
			sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.AA_code = ?) ");
			par.add(visaOrderForm.getAACode().trim());
		}
		//渠道ID
		if(null != visaOrderForm.getAgentinfoId() && !"".equals(visaOrderForm.getAgentinfoId().trim())){
			sqlBuffer.append(" and vo.agentinfo_id = ? ");
			par.add(visaOrderForm.getAgentinfoId());
		}else{
			//销售身份选择渠道id的时候
			/* C182 当渠道被删除时，历史数据不变  alter by 20150826 */
//			if("xiaoshou".equals(shenfen)){
//					List<Long> idList = new ArrayList<Long>();
//					List<Agentinfo> agentList = agentinfoService.findAllAgentinfo();
//					if(null != agentList && agentList.size()>0){
//						for(int i=0;i<agentList.size();i++){
//							idList.add(agentList.get(i).getId());
//						}
//						sqlBuffer.append(" and vo.agentinfo_id in "+getRoleLevelStrByLevelList(idList)+" ");
//					}else{
//						sqlBuffer.append(" and vo.agentinfo_id = -1 ");
//					}
//				}else{
//					List<Long> idList = new ArrayList<Long>();
//					List<Agentinfo> agentList = agentinfoDao.findAllAgentinfo(UserUtils.getUser().getCompany().getId());
//					if(null != agentList && agentList.size()>0){
//						for(int i=0;i<agentList.size();i++){
//							idList.add(agentList.get(i).getId());
//						}
//						sqlBuffer.append(" and vo.agentinfo_id in "+getRoleLevelStrByLevelList(idList)+" ");
//					}else{
//						sqlBuffer.append(" and vo.agentinfo_id = -1 ");
//					}
//				}
			}
		//创建者编号
		if(null != visaOrderForm.getCreateBy() && !"".equals(visaOrderForm.getCreateBy().trim())){
			sqlBuffer.append(" and vo.create_by in (select id  from sys_user where name = ? ) ");
			par.add(visaOrderForm.getCreateBy().trim());
		}
		//订单编号
		if(null != visaOrderForm.getOrderNo() && !"".equals(visaOrderForm.getOrderNo().trim())){
			sqlBuffer.append(" and vo.order_no = ? ");
			par.add(visaOrderForm.getOrderNo());
		}
		//支付状态
		if(null != visaOrderForm.getOrderPayStatus() && !"".equals(visaOrderForm.getOrderPayStatus().trim())){
			sqlBuffer.append(" and vo.payStatus = ? ");
			par.add(visaOrderForm.getOrderPayStatus());
		}else{
			sqlBuffer.append(" and vo.payStatus in ('1','3','5','99') ");
		}
		//参团类型
		if(null != visaOrderForm.getOrderType() && !"".equals(visaOrderForm.getOrderType().trim())){
			if (Context.ORDER_STATUS_VISA.equals(visaOrderForm.getOrderType().trim())){
				sqlBuffer.append(" and vo.id in (SELECT orderId from traveler where main_order_id is null )  ");
			}else{
				sqlBuffer.append(" and vo.id in (SELECT orderId from traveler where main_order_id in (SELECT p.id FROM productorder p WHERE p.orderStatus = ? )) ");
				par.add(visaOrderForm.getOrderType());
			}
		}
		//签证国家编号
		if(null != visaOrderForm.getSysCountryId() && !"".equals(visaOrderForm.getSysCountryId().trim())){
			sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_products vp INNER JOIN visa_order vo  on vo.visa_product_id = vp.id  and vp.sysCountryId = ? )");
			par.add(visaOrderForm.getSysCountryId());
		}
		//产品编号
		if(null != visaOrderForm.getVisaProductId() && !"".equals(visaOrderForm.getVisaProductId().trim())){
			sqlBuffer.append(" and vo.visa_product_id in ( select vp.id from visa_products vp where vp.productCode = ?  ) ");
			par.add(visaOrderForm.getVisaProductId());
		}
		//签证状态
		if(null != visaOrderForm.getVisaStatus() && !"".equals(visaOrderForm.getVisaStatus().trim())){
			sqlBuffer.append("and vo.id in ( SELECT vo1.id  from visa_order vo1 INNER JOIN traveler t1 on  vo1.id = t1.orderId inner JOIN visa v1  on  t1.id = v1.traveler_id and v1.visa_stauts = ?)");
			par.add(visaOrderForm.getVisaStatus());
		}
		//签证类型
		if(null != visaOrderForm.getVisaType() && !"".equals(visaOrderForm.getVisaType().trim())){
			sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_products vp INNER JOIN visa_order vo  on vo.visa_product_id = vp.id  and vp.visaType = ? ) ");
			par.add(visaOrderForm.getVisaType());
		}
		
		if("dingdan".equals(visaOrderForm.getShowList())){
			//借款状态
			if(null != visaOrderForm.getJiekuanStatus() && !"".equals(visaOrderForm.getJiekuanStatus().trim())){
				//1 审核中
				if("1".equals(visaOrderForm.getJiekuanStatus().trim())){
					sqlBuffer.append(" and re.status = 1 ");
					//2 已借
				}if("2".equals(visaOrderForm.getJiekuanStatus().trim())){
					sqlBuffer.append(" and (re.status = 2 or re.status = 3 )");
					//3 未借
				}if("3".equals(visaOrderForm.getJiekuanStatus().trim())){
					sqlBuffer.append(" and (re.status = 0 or re.status = 4 or re.status is null) ");
				}
			}
		}  	
		
		
		//借款时间
		if((null != visaOrderForm.getJiekuanTimeStart() && !"".equals(visaOrderForm.getJiekuanTimeStart())) || 
		   (null != visaOrderForm.getJiekuanTimeEnd() && !"".equals(visaOrderForm.getJiekuanTimeEnd()))){
			//预计约签时间-开始时间
			if(null != visaOrderForm.getJiekuanTimeStart() && !"".equals(visaOrderForm.getJiekuanTimeStart().trim())){
				sqlBuffer.append(" and vo.id in ( select DISTINCT orderid from review where productType = 6 and  flowType = 5 and createDate BETWEEN ? ");
				par.add(visaOrderForm.getJiekuanTimeStart() + " 00:00:00");
			}else{
				sqlBuffer.append(" and vo.id in ( select id from review where productType = 6 and  flowType = 5 and createDate BETWEEN '1987-09-12 00:00:00' ");
			}
			//预计约签时间-结束时间
			if(null != visaOrderForm.getJiekuanTimeEnd() && !"".equals(visaOrderForm.getJiekuanTimeEnd().trim())){
				sqlBuffer.append(" and ?) ");
				par.add(visaOrderForm.getJiekuanTimeEnd() + " 23:59:59");
				
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59')  ");
			}
		}
		
		
		//预计出团时间-开始时间
		//预计出团时间-结束时间
		if((null != visaOrderForm.getForecastStartOutStart() && !"".equals(visaOrderForm.getForecastStartOutStart())) ||(
		   null != visaOrderForm.getForecastStartOutEnd () && !"".equals(visaOrderForm.getForecastStartOutEnd()))){
			//预计出团时间-开始时间
			if(null != visaOrderForm.getForecastStartOutStart() && !"".equals(visaOrderForm.getForecastStartOutStart().trim())){
				sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.forecast_start_out BETWEEN ? ");
				par.add(visaOrderForm.getForecastStartOutStart()+" 00:00:00");
						
			}else{
				sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.forecast_start_out BETWEEN '1987-09-12 00:00:00' ");
			}
			//预计出团时间-结束时间
			if(null != visaOrderForm.getForecastStartOutEnd () && !"".equals(visaOrderForm.getForecastStartOutEnd().trim())){
				sqlBuffer.append(" and ?) ");
				par.add(visaOrderForm.getForecastStartOutEnd()+" 23:59:59");
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59')  ");
			}
		}
		
		//预计约签时间-开始时间
		//预计约签时间-结束时间
		if((null != visaOrderForm.getForecastContractStart() && !"".equals(visaOrderForm.getForecastContractStart())) || 
		   (null != visaOrderForm.getForecastContractEnd() && !"".equals(visaOrderForm.getForecastContractEnd()))){
			//预计约签时间-开始时间
			if(null != visaOrderForm.getForecastContractStart() && !"".equals(visaOrderForm.getForecastContractStart().trim())){
				sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.forecast_contract BETWEEN ? ");
				par.add(visaOrderForm.getForecastContractStart()+" 00:00:00");
			}else{
				sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.forecast_contract BETWEEN '1987-09-12 00:00:00' ");
			}
			//预计约签时间-结束时间
			if(null != visaOrderForm.getForecastContractEnd() && !"".equals(visaOrderForm.getForecastContractEnd().trim())){
				sqlBuffer.append(" and ?) ");
				par.add(visaOrderForm.getForecastContractEnd()+" 23:59:59");
				
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59')  ");
			}
		}
		
		
		//实际出团时间-开始时间
		//实际出团时间-结束时间
		if((null != visaOrderForm.getStartOutStart() && !"".equals(visaOrderForm.getStartOutStart()) || 
		   (null != visaOrderForm.getStartOutEnd() && !"".equals(visaOrderForm.getStartOutEnd())))){
			//实际出团时间-开始时间
			if(null != visaOrderForm.getStartOutStart() && !"".equals(visaOrderForm.getStartOutStart().trim())){
				//sqlBuffer.append(" and v.start_out  BETWEEN ?  ");
				sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.start_out BETWEEN ? ");
				par.add(visaOrderForm.getStartOutStart()+" 00:00:00");
						
			}else{
				//sqlBuffer.append(" and  v.start_out BETWEEN '2014-09-12 00:00:00' ");
				sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.start_out BETWEEN '1987-09-12 00:00:00' ");
			}
			//实际出团时间-结束时间
			if(null != visaOrderForm.getStartOutEnd() && !"".equals(visaOrderForm.getStartOutEnd().trim())){
				sqlBuffer.append(" and ? ) ");
				par.add(visaOrderForm.getStartOutEnd()+" 23:59:59");
								
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59')  ");
			}
		}
		
		//实际签约时间-开始时间
		//实际签约时间-结束时间
		if((null != visaOrderForm.getContractStart() && !"".equals(visaOrderForm.getContractStart())) || 
		   (null != visaOrderForm.getContractEnd() && !"".equals(visaOrderForm.getContractEnd()))){
			//实际签约时间-开始时间
			if(null != visaOrderForm.getContractStart() && !"".equals(visaOrderForm.getContractStart().trim())){
				//sqlBuffer.append(" and v.contract  BETWEEN ?  ");
				sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.contract BETWEEN ? ");
				par.add(visaOrderForm.getContractStart()+" 00:00:00");
						
			}else{
				//sqlBuffer.append(" and  v.contract BETWEEN '2014-09-12 00:00:00' ");
				sqlBuffer.append(" and vo.id in ( SELECT vo.id  from visa_order vo INNER JOIN traveler t on  vo.id = t.orderId inner JOIN visa v  on  t.id = v.traveler_id and v.contract BETWEEN '1987-09-12 00:00:00' ");
			}
			//实际签约时间-结束时间
			if(null != visaOrderForm.getContractEnd() && !"".equals(visaOrderForm.getContractEnd().trim())){
				sqlBuffer.append(" and  ?) ");
				par.add(visaOrderForm.getContractEnd()+" 23:59:59");
										
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59')  ");
			}
		}

		
		//下单时间-开始时间
		//下单时间-结束时间
		if((null != visaOrderForm.getCreateDateStart() && !"".equals(visaOrderForm.getCreateDateStart())) || 
		   (null != visaOrderForm.getCreateDateEnd() && !"".equals(visaOrderForm.getCreateDateEnd()))){
			//下单时间-开始时间
			if(null != visaOrderForm.getCreateDateStart() && !"".equals(visaOrderForm.getCreateDateStart().trim())){
				sqlBuffer.append(" and vo.create_date  BETWEEN ?  ");
				par.add(visaOrderForm.getCreateDateStart()+" 00:00:00");
								
			}else{
				sqlBuffer.append(" and  vo.create_date BETWEEN '2014-09-12 00:00:00' ");
			}
			//下单时间-结束时间
			if(null != visaOrderForm.getCreateDateEnd() && !"".equals(visaOrderForm.getCreateDateEnd().trim())){
				sqlBuffer.append(" and  ? ");
				par.add(visaOrderForm.getCreateDateEnd()+" 23:59:59");
										
			}else{
				sqlBuffer.append(" and '2099-12-12 23:59:59'  ");
			}
		}
		
		if("dingdan".equals(visaOrderForm.getShowList())){
			//发票状态
			if(null != visaOrderForm.getInvoiceStatus() && !"".equals(visaOrderForm.getInvoiceStatus().trim())){
				//1已开发票2未开发票
				if("1".equals(visaOrderForm.getInvoiceStatus().trim())){
					sqlBuffer.append(" and oin.verifyStatus is not null ");
				}if("2".equals(visaOrderForm.getInvoiceStatus().trim())){
					sqlBuffer.append(" and oin.verifyStatus is null ");
				}
			}
			//收据状态
			if(null != visaOrderForm.getReceiptStatus() && !"".equals(visaOrderForm.getReceiptStatus().trim())){
				//1已开收据2未开收据
				if("1".equals(visaOrderForm.getReceiptStatus().trim())){
					sqlBuffer.append(" and ore.verifyStatus is not null ");
				}if("2".equals(visaOrderForm.getReceiptStatus().trim())){
					sqlBuffer.append(" and ore.verifyStatus is null ");
				}
			}
			//销售ID
			if(null != visaOrderForm.getSalerId() && !"".equals(visaOrderForm.getSalerId().trim())){
				sqlBuffer.append(" and vo.salerId = ? ");
				par.add(visaOrderForm.getSalerId().trim());
			}
		}
		
		//只能看本供应商下的签证订单
		sqlBuffer.append(" and vo.create_by in ( select id from sys_user where companyId  = ?) ");
		par.add(UserUtils.getUser().getCompany().getId());
		
		//分部门显示
        systemService.getDepartmentSql("order", null, sqlBuffer, common, Context.ORDER_TYPE_QZ);
		
		sqlBuffer.append(" GROUP BY vo.id ");
		
		if(null == page){
			return  visaDao.findBySql(sqlBuffer.toString(),Map.class, par.toArray());
		}else{
			return visaDao.findBySql(page,sqlBuffer.toString(),Map.class, par.toArray());
		} 
	}
	
	

	//-------------------王新伟  添加 ---------------------
	
	/**
	 * 保存联系人信息
	 * 
	 * @param order
	 * @param contactsList
	 */
	private void saveContactsList(VisaOrder order,List<OrderContacts> contactsList) {
		
		/* 避免反复保存，首先进行删除操作 */
		//orderContactsDao.deleteOrderContactsByOrderIdAndOrderType(order.getId(),(Long)VisaPreOrderController.VISA_ORDER_TYPE);
		orderContactsDao.deleteOrderContactsByOrderIdAndOrderType(order.getId(), VisaPreOrderController.VISA_ORDER_TYPE);
		if (null == contactsList || contactsList.size() == 0) {
			return;
		}
		Long id = order.getId();
		List<OrderContacts> temp = new ArrayList<OrderContacts>();
		for (OrderContacts o : contactsList) {
			if (null!=o.getContactsName()&&null!=o.getContactsTel()) {
				o.setOrderId(id);
				o.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE); 
				temp.add(o);
			}
		}
		orderContactsDao.save(temp);
	}
	
	/**
	 * 1.保存签证订单基础信息   
	 * 2.联系人信息
	 * 
	 * @param activity
	 * @param order
	 * @param contactsList
	 * @param errorMap
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public VisaOrder saveOrderInfo(VisaOrder order, List<OrderContacts> contactsList) {
		
		VisaOrder returnOrder = new VisaOrder();
		if (order!=null) {
			returnOrder = visaOrderDao.save(order);
		}
		
		// 保存联系人信息
		if (null!=contactsList) {
			saveContactsList(returnOrder, contactsList);
		}
		
		return returnOrder;
	}
	
	/**
	 * 
	 * @param orderId
	 * @param travelerList
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Traveler saveTravelers(Long orderId, List<Traveler> travelerList, boolean isLog) {
		VisaOrder  visaOrder  = findVisaOrder(orderId);
		VisaProducts visaProducts = visaProductsDao.findOne(visaOrder.getVisaProductId());
		//保存游客信息
		if (null != orderId) {//
			travelerDao.findTravelerByOrderIdAndOrderType(orderId,VisaPreOrderController.VISA_ORDER_TYPE);
		}
		Traveler rTraveler = null;
		for (Traveler traveler : travelerList) {
			
			if (traveler.getId() == null) {
				rTraveler = traveler;
				if (null == traveler.getDelFlag()) {
					traveler.setDelFlag(0);
				}
			} else {
				rTraveler = travelerDao.findOne(traveler.getId());
				if (null == traveler.getDelFlag()) {
					traveler.setDelFlag(0);
				}
				updateTravelerAttr(traveler, rTraveler);
			}
			rTraveler.setOrderId(orderId);
			rTraveler.setSrcPrice(visaProducts.getVisaPay());
			rTraveler.setSrcPriceCurrency(currencyDao.findOne(visaProducts.getCurrencyId().longValue()));
			rTraveler.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
			
			/*
			 * 2015-04-07王新伟添加
			 * 添加借款UUID：如果有游客借款成功后用此UUID与MoneyAmount表建立关联关系
			 */
			String jkSerialNum = UUID.randomUUID().toString();
			rTraveler.setJkSerialNum(jkSerialNum);
			rTraveler.setRebatesMoneySerialNum(UUID.randomUUID().toString());
			
			rTraveler = travelerDao.save(rTraveler);
			if (isLog) {
				logOrderService.saveObj("", orderId, "添加游客：" + traveler.getName(), 1);
			}
		}
		return rTraveler;
	}
	
	/**
	 * 更新游客属性
	 * 
	 * @param traveler
	 * @param targetTraveler
	 */
	private void updateTravelerAttr(Traveler traveler, Traveler targetTraveler) {
		targetTraveler.setBirthDay(traveler.getBirthDay());
		targetTraveler.setIdCard(traveler.getIdCard());
		targetTraveler.setIntermodalId(traveler.getIntermodalId());
		targetTraveler.setName(traveler.getName());
		targetTraveler.setNameSpell(traveler.getNameSpell());
		targetTraveler.setNationality(traveler.getNationality());
		targetTraveler.setOrderId(traveler.getOrderId());
		targetTraveler.setPassportCode(traveler.getPassportCode());
		targetTraveler.setPassportType(traveler.getPassportType());
		targetTraveler.setIssuePlace(traveler.getIssuePlace());
		targetTraveler.setPassportValidity(traveler.getPassportValidity());
		targetTraveler.setPersonType(traveler.getPersonType());
		targetTraveler.setSex(traveler.getSex());
		targetTraveler.setRemark(traveler.getRemark());
		targetTraveler.setTelephone(traveler.getTelephone());
		targetTraveler.setValidityDate(traveler.getValidityDate());
		targetTraveler.setPassportStatus(traveler.getPassportStatus());
	}
	
	

	/**
	 * -------------wxw创建签证子订单接口开始--------------
	 */
	
	/**
	 * 签证子订单接口说明：
	 * 1. 参数 countryId， visaType，collarZoning用来匹配查询签证产品
	 * 2. 参数 productOrderCommony : 主订单对象
	 * 3. 参数 traveler 办签证的游客
	 * 4. 参数 travelerDocInfoIds 办签证游客的附件IDS
	 * 
	 * 
	 * ------------wxw added 2015-03-14-------------
	 * 子订单修改：
	 * 1.生成的虚拟团号部分要与预定时保持一致
	 * 2.去掉订单中main_order_code字段造成的所有影响进行修改
	 * 3.为了解决签证订单价钱单独结算的问题，子订单游客还必须单独结算，游客还必须复制一份团期游客的信息
	 * 4.为了使团期游客与签证中游客的信息修改后同步修改信息的便利，在签证游客的信息中增加：main_order_travelerid，指向团期游客的id
	 *   如签证游客为非团期游客；
	 * 5.同时在游客表中增加main_order_id，指向团期订单的ID
	 * 6.由于签证订单在新增时增加了可变的结算价， 和 成本价，作为子订单也要进行相应的处理；
	 *   6.1：保存订单的成本价
	 *   6.2：保存游客的成本价
	 * 7.生成的签证默认的状态改为：默认状态为续补资料
	 * 
	 * 
	 * 
	 * @param productOrderCommon
	 * @param traveler
	 * @param travelerDocInfoIds: 
	 *  key: passport_photo_id护照首页ID
     *       identity_front_photo_id身份证正面ID
     *       identity_back_photo_id身份证背面ID
     *       table_photo_id报名表ID
     *       person_photo_id照片ID
     *       other_photo_id其他图片附件ID（多个附件ID，用&分离）
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void subVisaOrderCreate(VisaProducts visaProducts, VisaOrder visaOrder, TravelerVisa travelerVisa,
			ProductOrderCommon productOrderCommon, Traveler traveler, Map<String, Long> travelerDocInfoIds) {
		
		Traveler travelerback = null;
		Visa visa = null;
		
		if (visaOrder == null) {
			//保存签证子订单基本信息
			visaOrder = createSubVisaOrderBasicOrder(visaProducts,productOrderCommon);
		} else {
			travelerback = travelerDao.findTravelerByOrderIdAndOrderType(visaOrder.getId(), Context.ORDER_TYPE_QZ).get(0);
			visa = visaDao.findByTravelerId(travelerback.getId());
		}
		
		//保存或更新签证子订单游客信息
		travelerback = createOrUpdateSubVisaTraveler(visaProducts, traveler, travelerback, visaOrder, productOrderCommon.getId());
		
		//保存或更改游客的结算价记录
	    createSubVisaTravelerMoneyAmount(moneyAmountService,travelerback,visaProducts,visaOrder);
	    
	    //保存或更新子订单的 Visa基本信息
	    //0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅
	    createSubVisaBasicInfo(visa,travelerVisa,travelerback, travelerDocInfoIds);
	    
	    //处理子订单的结算价记录
	    createSubVisaOrderMoneyAmount(moneyAmountService,visaProducts,visaOrder);
	}
	
	/**
	 * 保存签证子订单基本信息
	 * @param visaProducts
	 * @param productOrderCommon
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	private VisaOrder createSubVisaOrderBasicOrder(VisaProducts visaProducts,ProductOrderCommon productOrderCommon){
		User user  = productOrderCommon.getCreateBy();
		String companyName = user.getCompany().getName();
		Long companyId = user.getCompany().getId();
		VisaOrder visaOrder = new VisaOrder();
		String orderNum = sysIncreaseService.updateSysIncrease(companyName
				.length() > 3 ? companyName.substring(0, 3) : companyName,
						companyId, null, Context.ORDER_NUM_TYPE);

		
		
		String GroupNo = null;
		if (UserUtils.getUser().getCompany().getId()==68) {
			  GroupNo=activityGroupService.getGroupNumForTTS(visaProducts.getDeptId()+"",null);
		}else {
			 GroupNo = sysIncreaseService.updateSysIncrease(companyName
						.length() > 3 ? companyName.substring(0, 3) : companyName,
								companyId, null, Context.GROUP_NUM_TYPE);
		}
		
		visaOrder.setActivityCode(productOrderCommon.getProductGroupId());
		visaOrder.setOrderNo(orderNum);
		visaOrder.setGroupCode(GroupNo);
		visaOrder.setPayStatus(1);	//支付状态1-未支付全款 2-未支付订金  默认为未支付
		visaOrder.setVisaProductId(visaProducts.getId());
		
		//在签证订单中保存签证预定时相关签证产品的应收价
		visaOrder.setProOriginCurrencyId(visaProducts.getCurrencyId());
		visaOrder.setProOriginVisaPay(visaProducts.getVisaPay());
		
		visaOrder.setProductTypeID(VisaPreOrderController.VISA_ORDER_TYPE.longValue());
		visaOrder.setTravelNum(1);//默认只生成一个游客的订单
		visaOrder.setAgentinfoId(Long.valueOf(productOrderCommon.getOrderCompany()));
		
		// --- wxw added 20150908 生成子订单 保存 非签渠道的名称 ---
		visaOrder.setAgentinfoName(productOrderCommon.getOrderCompanyName());
		
	    //  0：未支付;1:已支付;2:已取消;100:订单创建中（创建未完成，不能使用）
		visaOrder.setVisaOrderStatus(Integer.valueOf(Context.VISA_ORDER_PAYSTATUS_DOING));// 订单状态(正在进行中)   确定点单结算后要修改状态
		visaOrder.setCreateBy(user);
		visaOrder.setSalerId(productOrderCommon.getSalerId());
		visaOrder.setSalerName(productOrderCommon.getSalerName());
		/**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		visaOrder.setPayedMoney(UUID.randomUUID().toString().replace("-", ""));
		visaOrder.setAccountedMoney(UUID.randomUUID().toString().replace("-", ""));
		//2015-03-02
		//visaOrder.setMainOrderCode(productOrderCommon.getId().intValue());
	    VisaOrder visaOrderBack = visaOrderDao.save(visaOrder);
	    return visaOrderBack;
	}

	/**
	 * 保存或修改签证子订单游客信息
	 * @param visaProducts
	 * @param travelerback
	 * @param visaOrderBack
	 * @param createFlag
	 * @return
	 */
	private Traveler createOrUpdateSubVisaTraveler(VisaProducts visaProducts, Traveler travelerback, Traveler visatraveler,
			VisaOrder visaOrderBack, Long mainOrderId) {
		
		if (visatraveler == null) {
			visatraveler = new Traveler();
		}
	    visatraveler.setOrderId(visaOrderBack.getId());
	    visatraveler.setName(travelerback.getName());//游客姓名
	    visatraveler.setNameSpell(travelerback.getNameSpell());
	    visatraveler.setIdCard(travelerback.getIdCard());
	    visatraveler.setNationality(travelerback.getNationality());
	    visatraveler.setSex(travelerback.getSex());
	    visatraveler.setBirthDay(travelerback.getBirthDay());
	    visatraveler.setIssuePlace(travelerback.getIssuePlace());
	    visatraveler.setValidityDate(travelerback.getValidityDate());
	    visatraveler.setTelephone(travelerback.getTelephone());
	    visatraveler.setRemark(travelerback.getRemark());
	    visatraveler.setSrcPrice(visaProducts.getVisaPay());//签证产品的应收价格
	    visatraveler.setPersonType(travelerback.getPersonType());
	    visatraveler.setPassportCode(travelerback.getPassportCode());
	    visatraveler.setIssuePlace(travelerback.getIssuePlace());
	    visatraveler.setPassportValidity(travelerback.getPassportValidity());
	    visatraveler.setPassportType(travelerback.getPassportType());
	    Currency currency =  currencyService.findCurrency(Long.parseLong(visaProducts.getCurrencyId()+""));
	    visatraveler.setSrcPriceCurrency(currency);//签证产品的应收价格币种
	    visatraveler.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
	    visatraveler.setDelFlag(travelerback.getDelFlag());
	    
	    /**
	     * wxw modified 游客中添加主订单Id 2015-03-02
	     * 1.在创建子订单是需在游客中添加主订单的ID，以标识该游客为参团游客
	     * 2.在创建游客信息时为了同步主子订单的游客信息在签证游客的订单中记录主订单游客的ID
	     */
	    visatraveler.setMainOrderId(mainOrderId);
	    visatraveler.setMainOrderTravelerId(travelerback.getId());
	    
	    Traveler travelertemp =  travelerDao.save(visatraveler);
	    return travelertemp;
	}
	
	/**
	 * 
	 * ---2015-03-14 wxwadded---
	 * 增加游客的成本价；
	 * 
	 * 生成游客的结算价记录
	 * @param moneyAmountService
	 * @param traveler
	 * @param visaProducts
	 * @param visaOrderBack
	 * @return
	 */
	private boolean  createSubVisaTravelerMoneyAmount(MoneyAmountService moneyAmountService,Traveler traveler,VisaProducts visaProducts,VisaOrder visaOrderBack){
		    boolean flag = false;
		    //结算价，此处与原始价保持一致，即为签证产品的应收价
		    /**
			 * wangxinwei 2015-10-18  签证产品预定评审后修改
			 * 1.uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 * 2.代码中方法参数个数删减
			 */
		    String payPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
		    /*MoneyAmount moneyAmount = new MoneyAmount(payPriceSerialNum, //款项UUID
		    		visaProducts.getCurrencyId(),//币种ID
		    		visaProducts.getVisaPay(),//相应币种的金额
		    		traveler.getId(), //订单或游客ID
		    		Context.MONEY_TYPE_JSJ, //款项类型: 游客结算价
		    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
		    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1.游客  2.订单
		    		visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID*/  
		    MoneyAmount moneyAmount = new MoneyAmount();
		    moneyAmount.setSerialNum(payPriceSerialNum);
		    moneyAmount.setCurrencyId(visaProducts.getCurrencyId());
		    moneyAmount.setAmount(visaProducts.getVisaPay());
		    moneyAmount.setUid(traveler.getId());
		    moneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);
		    moneyAmount.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
		    moneyAmount.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_TRAVELER);
		    moneyAmount.setCreatedBy(visaOrderBack.getCreateBy().getId());
		    
		    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
            if (flag) {
            	travelerService.updateSerialNumByTravelerId(payPriceSerialNum, traveler.getId());
            	flag = false;
			}
		    //保存原始应收价
            /**
             * wangxinwei 2015-10-18  签证产品预定评审后修改
			 * 1.uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 * 2.代码中方法参数个数删减
			 */
            String payPriceSerialNumOrigin = UUID.randomUUID().toString().replace("-", "");
		    /*MoneyAmount moneyAmountOrigin = new MoneyAmount(payPriceSerialNumOrigin, //款项UUID
		    		visaProducts.getCurrencyId(),//币种ID
		    		visaProducts.getVisaPay(),//相应币种的金额
		    		traveler.getId(), //订单或游客ID
		    		Context.MONEY_TYPE_YSJSJ, //款项类型: 原始（游客）结算价
		    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
		    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1.游客  2.订单
		    		visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID*/
            
            MoneyAmount moneyAmountOrigin = new MoneyAmount();
            moneyAmountOrigin.setSerialNum(payPriceSerialNumOrigin);
            moneyAmountOrigin.setCurrencyId(visaProducts.getCurrencyId());
            moneyAmountOrigin.setAmount(visaProducts.getVisaPay());
            moneyAmountOrigin.setUid(traveler.getId());
            moneyAmountOrigin.setMoneyType(Context.MONEY_TYPE_YSJSJ);
            moneyAmountOrigin.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
            moneyAmountOrigin.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_TRAVELER);
            moneyAmountOrigin.setCreatedBy(visaOrderBack.getCreateBy().getId());
            
		    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountOrigin);
		    if (flag) {//游客结算记录成功后要 更新 游客的payPriceSerialNum字段绑定结算记录
		    	travelerService.updateOriginalPayPriceSerialNumByTravelerId(payPriceSerialNumOrigin, traveler.getId());
			}
		    
		    //保存游客成本价------wxw added 2015-03-14-------
		    /**
			 * wangxinwei 2015-10-18  签证产品预定评审后修改
			 * 1.uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 * 2.代码中方法参数个数删减
			 */
            String costPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
		    /*MoneyAmount moneyAmountCost = new MoneyAmount(costPriceSerialNum, //款项UUID
		    		visaProducts.getCurrencyId(),//币种ID
		    		visaProducts.getVisaPay(),//相应币种的金额
		    		traveler.getId(), //订单或游客ID
		    		Context.MONEY_TYPE_CBJ, //款项类型: 游客成本价
		    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
		    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1.游客  2.订单
		    		visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID*/
            
            MoneyAmount moneyAmountCost = new MoneyAmount();
            moneyAmountCost.setSerialNum(costPriceSerialNum);
            moneyAmountCost.setCurrencyId(visaProducts.getCurrencyId());
            moneyAmountCost.setAmount(visaProducts.getVisaPay());
            moneyAmountCost.setUid(traveler.getId());
            moneyAmountCost.setMoneyType(Context.MONEY_TYPE_CBJ);
            moneyAmountCost.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
            moneyAmountCost.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_TRAVELER);
            moneyAmountCost.setCreatedBy(visaOrderBack.getCreateBy().getId());
		    
		    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountCost);
		    if (flag) {//游客结算记录成功后要 更新 游客的payPriceSerialNum字段绑定结算记录
		    	travelerService.updateCostSerialNumByTravelerId(costPriceSerialNum, traveler.getId());
			}
		    
		    
		    return flag;
	}
	
	/**
	 * 创建签证基本信息
	 * 
	 * @param forecastStartOut:预计出发时间
	 * @param forecastContract；预计签约时间
	 * @param traveler
	 * @param travelerDocInfoIds
	 *key: passport_photo_id护照首页ID
     *       identity_front_photo_id身份证正面ID
     *       identity_back_photo_id身份证背面ID
     *       table_photo_id报名表ID
     *       person_photo_id照片ID
     *       other_photo_id其他图片附件ID（多个附件ID，用&分离）
	 * @return
	 */
	private Visa createSubVisaBasicInfo(Visa visa, TravelerVisa travelerVisa, Traveler traveler, Map<String,Long> travelerDocInfoIds) {
		if (visa == null) {
			visa = new Visa();
		}
		visa.setTravelerId(traveler.getId());
		visa.setPassportOperateRemark(traveler.getRemark());
		visa.setRemark(traveler.getRemark());
		visa.setForecastStartOut(travelerVisa.getGroupOpenDate());//预计出发时间
		visa.setForecastContract(travelerVisa.getContractDate());//预计签约时间
		
		//0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅
//		visa.setDatumValue(travelerVisa.getDatumValue());//是否需要上传资料
//		visa.setDepositValue(travelerVisa.getDepositValue());//是否需要押金
		//visa.setVisaStauts(Integer.valueOf(Context.VISA_STATUTS_TO));
		//作为子订单与签证预订时保持一致：默认状态为续补资料 -  2015-03-14--
		visa.setVisaStauts(Integer.valueOf(Context.VISA_STATUTS_RESOURCENEEDED));
		Long passportdocID = travelerDocInfoIds.get("passport_photo_id");
		Long idcardfrontdocID = travelerDocInfoIds.get("identity_front_photo_id");
		Long idcardbackdocID = travelerDocInfoIds.get("identity_back_photo_id");
	
		Long entry_formdocID = travelerDocInfoIds.get("table_photo_id");
		Long photodocID = travelerDocInfoIds.get("person_photo_id");
		Long housedocID = travelerDocInfoIds.get("house_photo_id");
		Long residencedocID = travelerDocInfoIds.get("residence_photo_id");
		Long otherdocID = travelerDocInfoIds.get("other_photo_id");
		
		if (null!=passportdocID) {
			visa.setPassportPhotoId(passportdocID);
		}
		if (null!=idcardfrontdocID) {
			visa.setIdentityFrontPhotoId(idcardfrontdocID);
		}
		if (null!=idcardbackdocID) {
			visa.setIdentityBackPhotoId(idcardbackdocID);
		}
		if (null!=entry_formdocID) {
			visa.setTablePhotoId(entry_formdocID);
		}
		if (null!=photodocID) {
			visa.setPersonPhotoId(photodocID);
		}
		if (null!=housedocID) {
			visa.setHouseEvidencePhotoId(housedocID);
		}
		if (null!=residencedocID) {
			visa.setFamilyRegisterPhotoId(residencedocID);
		}
		if (null!=otherdocID) {
			visa.setOtherPhotoId(otherdocID);
		}
		visa = visaService.saveVisa(visa);
		return visa;
	}
	
	/**
	 * 处理子订单的结算价记录
	 * @param moneyAmountService
	 * @param visaProducts
	 * @param visaOrderBack
	 * @return
	 */
	private boolean  createSubVisaOrderMoneyAmount(MoneyAmountService moneyAmountService,VisaProducts visaProducts,VisaOrder visaOrderBack){
		    boolean flag = false;
		    
		  //保存订单应收价
		    /**
			 * wangxinwei 2015-10-14  签证产品预定评审后修改
			 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 * 代码中方法参数个数删减
			 */
		    String payPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
		    /*MoneyAmount moneyAmount = new MoneyAmount(payPriceSerialNum, 
		    		visaProducts.getCurrencyId(),//币种ID
		    		visaProducts.getVisaPay(),//相应币种的金额
		    		visaOrderBack.getId(), //订单或游客ID
		    		Context.MONEY_TYPE_YSH, //款项类型: 订单应收
		    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
		    		VisaPreOrderController.BUSINDESS_TYPE_ORDER,//1表示订单，2表示游客
		    		visaOrderBack.getCreateBy().getId());*///记录创建人ID, 这里用订单的ID
		    MoneyAmount moneyAmount = new MoneyAmount();
		    moneyAmount.setSerialNum(payPriceSerialNum);//款项UUID
		    moneyAmount.setCurrencyId(visaProducts.getCurrencyId());//币种ID
		    moneyAmount.setAmount(visaProducts.getVisaPay());//相应币种的金额
		    moneyAmount.setUid(visaOrderBack.getId());//订单或游客ID
		    moneyAmount.setMoneyType(Context.MONEY_TYPE_YSH); //款项类型: 订单应收
		    moneyAmount.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);//订单类型
		    moneyAmount.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_ORDER);//1表示订单，2表示游客
		    moneyAmount.setCreatedBy(visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID
		    
		    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
		    
		    //保存原始应收价
		    /**
			 * wangxinwei 2015-10-14  签证产品预定评审后修改
			 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 * 代码中方法参数个数删减
			 */
		    String payPriceSerialNumOrigin = UUID.randomUUID().toString().replace("-", "");
		    /*MoneyAmount moneyAmountOrigin = new MoneyAmount(payPriceSerialNumOrigin, //款项UUID
		    		visaProducts.getCurrencyId(),//币种ID
		    		visaProducts.getVisaPay(),//相应币种的金额
		    		visaOrderBack.getId(), //订单或游客ID
		    		Context.MONEY_TYPE_YSYSH, //款项类型: 订单原始应收
		    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
		    		VisaPreOrderController.BUSINDESS_TYPE_ORDER,//1表示订单，2表示游客
		    		visaOrderBack.getCreateBy().getId());*///记录创建人ID, 这里用订单的ID
		    MoneyAmount moneyAmountOrigin = new MoneyAmount(); //款项UUID
		    moneyAmountOrigin.setSerialNum(payPriceSerialNumOrigin);//款项UUID
		    moneyAmountOrigin.setCurrencyId(visaProducts.getCurrencyId());//币种ID
		    moneyAmountOrigin.setAmount(visaProducts.getVisaPay());//相应币种的金额
		    moneyAmountOrigin.setUid(visaOrderBack.getId()); //订单或游客ID
		    moneyAmountOrigin.setMoneyType(Context.MONEY_TYPE_YSYSH); //款项类型: 订单原始应收
		    moneyAmountOrigin.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);//订单类型
		    moneyAmountOrigin.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_ORDER);//1表示订单，2表示游客
		    moneyAmountOrigin.setCreatedBy(visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID
		    
		    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountOrigin);
		    
		/*    if (flag) {//游客结算记录成功后要 更新 游客的payPriceSerialNum字段绑定结算记录
		    	visaOrderBack.setOriginalTotalMoney(payPriceSerialNumOrigin); //设置原始应收总额的UUID
		    	visaOrderBack.setTotalMoney(payPriceSerialNum); //设置应收总额的UUID
		    	visaOrderBack.setVisaOrderStatus(0);//设置订单的支付状态改为未支付
				saveOrderInfo(visaOrderBack, null);
			}*/
		    
		    //保存订单的成本价
		    /**
			 * wangxinwei 2015-10-14  签证产品预定评审后修改
			 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 * 代码中方法参数个数删减
			 */
		    String costPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
		    MoneyAmount costMoneyAmount = new MoneyAmount();
		    costMoneyAmount.setSerialNum(costPriceSerialNum); //款项UUID
		    costMoneyAmount.setCurrencyId(visaProducts.getCurrencyId());//币种ID
		    costMoneyAmount.setAmount(visaProducts.getVisaPay());//相应币种的金额
		    costMoneyAmount.setUid(visaOrderBack.getId()); //订单或游客ID
		    costMoneyAmount.setMoneyType(Context.MONEY_TYPE_CBJ); //款项类型: 成本价
		    costMoneyAmount.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);//订单类型
		    costMoneyAmount.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_ORDER);//1表示订单，2表示游客
		    costMoneyAmount.setCreatedBy(visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID
		    flag = moneyAmountService.saveOrUpdateMoneyAmount(costMoneyAmount);
		    
		    if (flag) {//游客结算记录成功后要 更新 游客的payPriceSerialNum字段绑定结算记录
		    	visaOrderBack.setOriginalTotalMoney(payPriceSerialNumOrigin); //设置原始应收总额的UUID
		    	visaOrderBack.setTotalMoney(payPriceSerialNum); //设置应收总额的UUID
		    	visaOrderBack.setCostTotalMoney(costPriceSerialNum);
		    	visaOrderBack.setVisaOrderStatus(0);//设置订单的支付状态改为未支付
				saveOrderInfo(visaOrderBack, null);
				
				//-------by------junhao.zhao-----2017-01-19-----主要通过visa_order向表order_data_statistics中添加数据+通过参数更改表order_data_statistics订单金额与金额uuid---开始-----
			    List<Map<String, Object>> orderDataStatisticsId = orderDateSaveOrUpdateService.getOrderDataStaIdForVisa(visaOrderBack.getId(), Context.ORDER_TYPE_QZ);
				//根据order_data_statistics中是否存在相应的值，进行修改或添加
				if(orderDataStatisticsId.size()==0 || orderDataStatisticsId.get(0) == null){
					// 向表order_data_statistics中添加数据
			  		orderDateSaveOrUpdateService.insertVisaOrder(visaOrderBack);
				}
				BigDecimal amount = new BigDecimal(0);
				if(moneyAmount.getAmount()!= null && moneyAmount.getExchangerate()!= null){
					amount = moneyAmount.getAmount().multiply(moneyAmount.getExchangerate());
				}
				// 更改表order_data_statistics中的订单金额与金额uuid
				orderDateSaveOrUpdateService.updateMoneyAndUuid(visaOrderBack.getId(), Context.ORDER_TYPE_QZ, amount, payPriceSerialNum);
			    //-------by------junhao.zhao-----2017-01-19-----主要通过visa_order向表order_data_statistics中添加数据+通过参数更改表order_data_statistics订单金额与金额uuid---结束------
		    
		    }
		    
		    return flag;
	}
	/**
	 * -------------wxw 子订单结束--------------
	 */
	
		
	/**
	 * 20150520 借、还护照操作流程更改
	 * 借、还护照时，产生批次记录，并更新visa表和traveler表,向batch_record表和batch_traveler_relation表插入记录
	 * @param visa
	 * @param passportStatus
	 */
	@Transactional
	public void updatePassportStatus(Visa visa, Integer passportStatus){
		String hql = "update Visa set passportOperator=?, passportOperateTime=?, passportOperateRemark=?,passportOperateType=? where id=?";
		visaDao.getSession().createQuery(hql)
			.setParameter(0, visa.getPassportOperator())
			.setParameter(1, visa.getPassportOperateTime())
			.setParameter(2, visa.getPassportOperateRemark())
			.setParameter(3, visa.getPassportOperateType())
			.setParameter(4, visa.getId())
			.executeUpdate();

		String batchNo =   sysBatchNoService.getBorrowPassPortBatchNo();
		String hql2 = "update Traveler set passportStatus=?,borrowPassportBatchNo=? where id=?";
		travelerDao.getSession().createQuery(hql2)
		.setParameter(0, passportStatus)
		.setParameter(1, batchNo)
		.setParameter(2, visa.getTravelerId())
		.executeUpdate();
	
		String uid = UUID.randomUUID().toString();
		BatchRecord record = new BatchRecord();
		record.setUuid(uid);
		record.setBatchNo(batchNo);
		record.setType(BatchRecord.TYPE_1);
		record.setCreateTime(new Date());
		record.setCreateUserId(UserUtils.getUser().getId());
		record.setCreateUserName(UserUtils.getUser().getName());
		if(passportStatus==1){//借护照操作
			record.setPassportStatus(1);// 0:未借  1:已借  2:未还 3:已还
			record.setIsSubmit(1);// 是否提交 0:未提交  1：已提交
		}else{//还护照操作
			record.setPassportStatus(3);
			record.setIsSubmit(1);
		}
		batchRecordDao.getSession().save(record);
		
		String uuid = UUID.randomUUID().toString();
		
		BatchTravelerRelation relation = new BatchTravelerRelation();
		relation.setUuid(uuid);
		relation.setBatchUuid(uid);
		relation.setBatchRecordNo(batchNo);
		relation.setTravelerId(visa.getTravelerId());
		Traveler traveler = travelerService.findTravelerById(visa.getTravelerId());
		relation.setTravelerName(traveler.getName());
		relation.setOrderId(traveler.getOrderId());
		relation.setVisaId(visa.getId());
		if(passportStatus==1){//借护照
			relation.setBusinessType(3);//业务类型 1:借款 2：还收据  3：借护照 4：还护照
		}else{  //还护照
			relation.setBusinessType(4);
		}
		batchTravelerRelationDao.getSession().save(relation);
		
	}
	
	/**
	 * 获取游客借款金额的字符串  否则返回null
	 * 
	 * @param orderId
	 * @param travelerId
	 * @return 游客借款金额的字符串
	 */
	public String getTravelerBorrowedMoney(String orderId, Long travelerId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId)&&travelerId!=null) {
			listReview = reviewDao.findReviewActive(6, 5,orderId, travelerId);
			if (listReview.size()>=1) {
				List<ReviewDetail> list =reviewDetailDao.findReviewDetailByMykey(listReview.get(0).getId(), "borrowAmount");
				return list.get(0).getMyvalue()+","+listReview.get(0).getStatus();
			}
		}
		return null;
	}
	
	
	
	/**
	 * ----------------综合处理新旧审核的  借款状态问题-----------------
	 * 
	 * @Description: 获取游客借款金额的字符串  否则返回null,流程互斥判断用
	 *               规则如下：已经申请  或 审核 通过的 游客 不能发起借款，
	 *                      取消  和  驳回  的  可再次发起
	 * @author xinwei.wang
	 * @date 2015年12月12日下午3:25:47
	 * @param orderId
	 * @param travellerId
	 * @return   游客借款金额的字符串 
	 * @throws
	 */
	public String getTravelerBorrowedMoney4Activiti(String orderId, Long travellerId){
		List<ReviewNew> listReview = new ArrayList<ReviewNew>();
		if (StringUtils.isNotEmpty(orderId)&&travellerId!=null) {
			listReview = processReviewService.getReviewList(orderId, travellerId.toString(),"6","5");
			if (listReview.size()>=1) {
				for (ReviewNew reviewNew : listReview) {
					if (1==reviewNew.getStatus()||2==reviewNew.getStatus()||0==reviewNew.getStatus()) {//1,审批中
						Map<String,Object>  reviewAndDetailInfoMap = null;
						String review_new_id = reviewNew.getId();
						try{
							reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewNew.getId());
						}catch(Exception e){
							log.error("根据reviewid： " + review_new_id + " 查询出来reviewDetail明细报错 ",e);
						}
						return reviewAndDetailInfoMap.get("borrowAmount").toString()+","+reviewNew.getStatus().toString()+","+reviewNew.getId();
					}
				}
			}
		}
		return null;
	}
	
	
	
	
	/**
	 * ----------------综合处理新旧审核的  借款状态问题-----------------
	 * 
	 * @Description: 根据
	 * @author xinwei.wang
	 * @date 2016年03月14日下午19:25:47
	 * @param orderId
	 * @param travellerId
	 * @return   游客借款金额的字符串 
	 * @throws
	 */
	public String getReViewIdByBatchNumMainly(String batchNum,Integer travelerId,Integer orderId){
		String hql  = " SELECT rn.id ,rn.batch_no from review_new rn WHERE  rn.process_type = '5' AND rn.product_type = '6' AND rn.batch_no = '"+batchNum+"' AND rn.traveller_id ="+travelerId+" and order_id = "+orderId;
//		System.out.println(hql);
		List<Map<String, Object>> resultList = reviewDao.findBySql(hql,Map.class);
		String reviewId = null;
		if(null!=resultList && resultList.size()>0){
			reviewId = resultList.get(0).get("id").toString();
		}
		
		return reviewId;
	}
	
	
	
    /**
     * @Description: 处理签务签证订单  的  借款明细
     * @author xinwei.wang
     * @date 2015年12月23日下午2:00:45
     * @param orderId
     * @param travellerId
     * @return    
     * @throws
     */
	public Map<String,Object>   getReviewNew4HQXBorrowMoney(String orderId, Long travellerId){
		List<ReviewNew> listReview = new ArrayList<ReviewNew>();
		Map<String,Object>  reviewAndDetailInfoMap = null;
		if (StringUtils.isNotEmpty(orderId)&&travellerId!=null) {
			listReview = processReviewService.getReviewList(orderId, travellerId.toString(),"6","5");
			if (listReview.size()>=1) {
				for (ReviewNew reviewNew : listReview) {
					if (1==reviewNew.getStatus()||2==reviewNew.getStatus()) {//1,审批中  
						reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewNew.getId());
						return reviewAndDetailInfoMap;
					}
				}
			}
		}
		return reviewAndDetailInfoMap;
	}
	
	
	/**
	 * 
	 * ----------------综合处理新旧审核的  借款状态问题----------------
	 * 
	 * @Description: 获取游客还收据金额的字符串   否则返回null,流程互斥判断用
	 *               规则如下：已经申请  或 审核 通过的 游客 不能发起还收据，
	 *                      取消  和  驳回  的  可再次发起
	 * @author xinwei.wang
	 * @date 2015年12月12日下午3:25:47
	 * @param orderId
	 * @param travellerId
	 * @return   游客借款金额的字符串 
	 * @throws
	 */
	public String getTravelerRetrunReceipt4Activiti(String orderId, Long travellerId){
		List<ReviewNew> listReviewReturn = new ArrayList<ReviewNew>();
		if (StringUtils.isNotEmpty(orderId)&&travellerId!=null) {
			listReviewReturn = processReviewService.getReviewList(orderId, travellerId.toString(),"6","4");
			if (listReviewReturn.size()>=1) {
				for (ReviewNew reviewNew : listReviewReturn) {
					if (1==reviewNew.getStatus()||2==reviewNew.getStatus()) {//1,审批中  
						Map<String,Object>  reviewAndDetailInfoMap = null;
						String review_new_id = reviewNew.getId();
						try{
							reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewNew.getId());
						}catch(Exception e){
							log.error("根据reviewid： " + review_new_id + " 查询出来reviewDetail明细报错 ",e);
						}
						return reviewAndDetailInfoMap.get("receiptAmount").toString()+","+reviewNew.getStatus().toString()+","+reviewNew.getId();
					}
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * 获取游客借款金额的字符串  否则返回null
	 * 
	 * @param orderId
	 * @param travelerId
	 * @return 游客借款金额的字符串
	 */
	public Review getTravelerBorrowedStatusAndTime(String orderId, Long travelerId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId)&&travelerId!=null) {
			listReview = reviewDao.findReviewActive(6, 5,orderId, travelerId);
		}
		if(null == listReview || listReview.size() == 0){
			return null;
		}else{
			return listReview.get(0);
		}
	}
	// ---------------------------------------------
	/**
	 * 获取游客借款金额的字符串  否则返回null
	 * 
	 * @param orderId
	 * @param travelerId
	 * @return 游客借款金额的字符串
	 */
	public String getTravelerBorrowedMoney2(String orderId, Long travelerId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId)&&travelerId!=null) {
			listReview = reviewDao.findReviewActive(6, 5,orderId, travelerId);
			if (listReview.size()>=1) {
				List<ReviewDetail> list =reviewDetailDao.findReviewDetailByMykey(listReview.get(listReview.size()-1).getId(), "borrowAmount");
				return list.get(list.size()-1).getMyvalue()+","+listReview.get(listReview.size()-1).getStatus();
			}
		}
		return null;
	}
	
	/**
	 * 获取游客借款金额的字符串  否则返回null
	 * 
	 * @param orderId
	 * @param travelerId
	 * @return 游客借款金额的字符串
	 */
	public Review getTravelerBorrowedStatusAndTime2(String orderId, Long travelerId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId)&&travelerId!=null) {
			listReview = reviewDao.findReviewActive(6, 5,orderId, travelerId);
		}
		if(null == listReview || listReview.size() == 0){
			return null;
		}else{
			return listReview.get(listReview.size()-1);
		}
	}
	
	
	/**
	 * 
	 * ----------------综合处理新旧审核的  借款状态问题-----------------
	 * 
	 * @Description: 根据   orderId、travelerId 查询新审核  记录
	 * @author xinwei.wang
	 * @date 2015年12月21日下午3:33:20
	 * @param orderId
	 * @param travelerId
	 * @return    
	 * @throws
	 */
	public ReviewNew getTravelerBorrowedStatusAndTime24Activiti(String orderId, Long travellerId){
		List<ReviewNew> listReview = new ArrayList<ReviewNew>();
		if (StringUtils.isNotEmpty(orderId)&&travellerId!=null) {
			//listReview = reviewDao.findReviewActive(6, 5,orderId, travelerId);
			listReview = processReviewService.getReviewList(orderId, travellerId.toString(),"6","5");
		}
		if(null == listReview || listReview.size() == 0){
			return null;
		}else{
			return listReview.get(0);
		}
	}
	
	// ---------------------------------------------
	/**
	 * 查看预约表
	 * @author jiachen
	 * @DateTime 2014-12-16 下午12:26:54
	 * @return List<Object[]>
	 */
	public List<Object[]> findOrderTableByOrderId(String orderId) {
		List<Object[]> list = Lists.newArrayList();
		if(StringUtils.isNotBlank(orderId)) {
			String ids [] = orderId.split(",");
			for (String id : ids) {
				list.addAll(visaOrderDao.findOrderTableByOrderId(StringUtils.toLong(id)));
			}
			//查找关联的数据的信息
			for(Object[] o : list) {
				//约签日期和时间分开显示
				if(null != o[3]) {
					String contract = o[3].toString();
					o[3] = contract.split(" ");
				}
				//约签国家
				if(null != o[4]) {
					o[4] = CountryUtils.getCountryName(StringUtils.toLong(o[4]));
				}
				//签证类型
				if(null != o[5]) {
					o[5] = DictUtils.getDictLabel(o[5].toString(), "new_visa_type", "");
				}
			}
		}
		return list;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void deleteTraveler(Long travelerId) {
		this.travelerDao.delete(travelerId);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
    public String cancelOrder(Long orderId) {
	    VisaOrder order = visaOrderDao.findOne(orderId);
        if(order == null){
            return "fail";
        }
//        String payStatus = String.valueOf(order.getPayStatus());
//        if(!Context.ORDER_PAYSTATUS_WZF.equals(payStatus) 
//              && !Context.ORDER_PAYSTATUS_DJWZF.equals(payStatus) 
//              && !Context.ORDER_PAYSTATUS_YZW.equals(payStatus)){
//          return "订单已支付不能取消";
//        }
        //保存取消前订单支付状态
        order.setOldPayStatus(order.getPayStatus());
        order.setPayStatus(Integer.valueOf(Context.ORDER_PAYSTATUS_YQX));
//        order.setVisaOrderStatus(2);
        visaOrderDao.save(order);
        
      //-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为2（取消）---开始---
        orderDateSaveOrUpdateDao.updateOrderStatus(order.getId(), Integer.parseInt(Context.VISA_ORDER_PAYSTATUS_CANCEL), Context.ORDER_TYPE_QZ);
        
      //-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为2（取消）---结束--- 
        
        return "ok";
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public String invokeOrder(Long orderId) {
        VisaOrder order = visaOrderDao.findOne(orderId);
        if(order == null){
            return "订单不存在";
        }
//        Integer payStatus = order.getPayStatus();
//        //激活条件：支付方式为订金占位或预占位的订单：包括已取消和没有取消的
//        Set<Integer> payStatusSet = new HashSet<Integer>();
//        payStatusSet.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YQX));//已取消
//        payStatusSet.add(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF));//订金未占位
//        payStatusSet.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));//已占位 非订金占位
//        if(payStatusSet.contains(payStatus)){
        if(order.getOldPayStatus() != null){
            Date currentTime = new Date(); 
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
//            order.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
            order.setPayStatus(order.getOldPayStatus());
            order.setActivationDate(DateUtils.dateFormat(dateString));
            visaOrderDao.invokeOrder(order.getId(), order.getPayStatus(), order.getActivationDate());
            
          //-------by------junhao.zhao-----2017-01-09-----表order_data_statistics中order_status改为激活---开始---
            orderDateSaveOrUpdateDao.updateOrderStatus(order.getId(), order.getVisaOrderStatus(), Context.ORDER_TYPE_QZ);
            
          //-------by------junhao.zhao-----2017-01-09-----表order_data_statistics中order_status改为激活 ---结束--- 
        }
//        }else {
//            return "只有订单状态为'取消'或'订金未支付'或'预占位'的订单才能激活";
//        }
        return "success";
    }

    public void lockOrder(Long orderId) {
        this.visaOrderDao.updateOrderLockStatus(orderId, Context.LOCK_ORDER);
        
    }

    public void unLockOrder(Long orderId) {
        this.visaOrderDao.updateOrderLockStatus(orderId, Context.UNLOCK_ORDER);
    }
    
	public List<Map<String, Object>> queryVisaOrdersByProductId(String productId) {
		return visaOrderListDao.queryVisaOrdersByProductId(productId);
	}
	
	/**
	 * 获取订单关联的产品的部门id
	 * @author jiachen
	 * @DateTime 2015年2月5日 下午3:43:05
	 * @return Long
	 */
	public Long getProductPept(Object orderId) {
		Long deptId = null;
		if(null != orderId) {
			VisaOrder visaOrder = findVisaOrder(StringUtils.toLong(orderId.toString()));
			if(null != visaOrder) {
				deptId = visaProductsDao.findOne(visaOrder.getVisaProductId()).getDeptId();
				return deptId == null ? 0L : deptId;
			}
		}
		return deptId;
	}
	

	/**
	 * 游客支付签证订单
	 * @param opf
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public Map<String, Object> visaOrderPayByTravler(OrderPayForm opf){
		Map<String, Object> restV = new HashMap<String, Object>();
		
		OrderPayRelation op = null;
		OrderTravelerPayRelation otp = null;
		String errorMsg="";
		Map updateOrderStatusMap = new HashMap();
		if (opf.getOrderPayInput().getOrderPayDetailList().size() > 1) {
			for(OrderPayDetail detail:opf.getOrderPayInput().getOrderPayDetailList()){

				// 结算方式为即时结算时
				if (opf.getPaymentStatus() == 1) {
					Traveler traveler = travelerDao.findById(detail.getTravelerId());
					// 判断PayedMoneySerialNum是否为null
					// 如果为空,新生成一个UUID,将传过来的UUID对应的金额保存到MoneyAmount
					if (StringUtils.isBlank(traveler.getPayedMoneySerialNum())) {
						String travlerPayedMoneySerialNum = this.getUUID();
						List<MoneyAmount> travlerPayedMoneyList = moneyAmountDao.findAmountBySerialNum(detail.getMoneySerialNum());
						MoneyAmount ma = null;
						for (int i = 0; i < travlerPayedMoneyList.size(); i++) {
							ma = new MoneyAmount();
							ma.setAmount(travlerPayedMoneyList.get(i).getAmount());
							ma.setCurrencyId(travlerPayedMoneyList.get(i).getCurrencyId());
							ma.setSerialNum(travlerPayedMoneySerialNum);
							ma.setMoneyType(Context.MONEY_TYPE_WK);
							ma.setOrderType(Context.ORDER_TYPE_QZ);
							ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_YK);
							ma.setUid(detail.getTravelerId());
							ma.setCreatedBy(UserUtils.getUser().getId());
							ma.setCreateTime(new Date());
							
							moneyAmountService.saveOrUpdateMoneyAmount(ma);
						}
						// 将新生成的UUID保存到游客表payed_moneySerialNum(游客已付款流水号)
						traveler.setPayedMoneySerialNum(travlerPayedMoneySerialNum);
						traveler.setPaymentType(opf.getPaymentStatus());
						travelerDao.save(traveler);
					// 如果不为空,从游客表中取得UUID，并与传过来的UUID相回，并保存到MoneyAmount
					} else {
						List<MoneyAmount> moneyAmountList = moneyAmountService.addMoney(traveler.getPayedMoneySerialNum(), detail.getMoneySerialNum());
						
						moneyAmountService.updateMoneyAmount(traveler.getPayedMoneySerialNum(), moneyAmountList);
					}

					// 根据游客更新订单信息
					// 当前游客订单ID
					Long orderId = detail.getOrderId();
					// 根据订单ID查询信息
					VisaOrder visaOrder = visaOrderDao.findOne(orderId);
					// 当前订单已付金额UUID
					String payedMoney = visaOrder.getPayedMoney();

					String[] currIds = detail.getPayCurrencyId().split(",");
					String[] currPrices = detail.getPayCurrencyPrice().split(",");
					List<MoneyAmount> payedMoneyList = moneyAmountDao.findAmountBySerialNum(payedMoney);

					// 订单付款
					boolean flag;
					MoneyAmount ma = null;
					if (payedMoneyList != null && payedMoneyList.size() > 0) {
						for (int i = 0; i < currIds.length; i++) {
							flag = false;
							String currId = currIds[i];
							String currPrice = currPrices[i];
							// 如果该UUID包含数组中的币种
							for (int j = 0; j < payedMoneyList.size(); j++) {
								if(currId.equals(payedMoneyList.get(j).getCurrencyId().toString())){
									List<MoneyAmount> ma1 = moneyAmountDao.findAmountBySerialNumAndCurrencyId(payedMoney,Integer.parseInt(currId));
									ma1.get(0).setAmount(payedMoneyList.get(j).getAmount().add(new BigDecimal(currPrice)));
									moneyAmountService.saveOrUpdateMoneyAmounts(payedMoney, ma1);
									flag = true;
								}
							}
							// 如果该UUID不包含数组中的币种
							if (!flag) {
								ma = new MoneyAmount();
								ma.setAmount(new BigDecimal(currPrice));
								ma.setCurrencyId(Integer.parseInt(currId));
								ma.setSerialNum(payedMoney);
								ma.setMoneyType(Context.MONEY_TYPE_WK);
								ma.setOrderType(Context.ORDER_TYPE_QZ);
								ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_DD);
								ma.setUid(orderId);
								ma.setCreatedBy(UserUtils.getUser().getId());
								ma.setCreateTime(new Date());
								moneyAmountService.saveOrUpdateMoneyAmount(ma);
							}
						}
					// 该订单第一次付款
					} else {
						for (int i = 0; i < currIds.length; i++) {
							ma = new MoneyAmount();
							String currId = currIds[i];
							String currPrice = currPrices[i];
							ma.setAmount(new BigDecimal(currPrice));
							ma.setCurrencyId(Integer.parseInt(currId));
							ma.setSerialNum(payedMoney);
							ma.setMoneyType(Context.MONEY_TYPE_WK);
							ma.setOrderType(Context.ORDER_TYPE_QZ);
							ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_DD);
							ma.setUid(orderId);
							ma.setCreatedBy(UserUtils.getUser().getId());
							ma.setCreateTime(new Date());
							moneyAmountService.saveOrUpdateMoneyAmount(ma);
						}
					}

					// 更新订单状态
					updateOrderStatusMap = updateOrderStatus(orderId);
					if(updateOrderStatusMap.size()>0){
						errorMsg +=updateOrderStatusMap.get("errorMsg");
					}
				}

				op = new OrderPayRelation();
				String orderPayRelationId = this.getUUID();
				op.setId(orderPayRelationId);
				op.setOrderId(detail.getOrderId().toString());
				op.setPayId(detail.getPayId());
				op.setPaymentStatus(opf.getPaymentStatus());
				orderPayRelationDao.save(op);
				
				otp = new OrderTravelerPayRelation();
				String orderTravelerPayRelationId = this.getUUID();
				otp.setId(orderTravelerPayRelationId);
				otp.setOrderPayRelationId(orderPayRelationId);
				otp.setTravelerId(detail.getTravelerId().toString());
				orderTravelerPayRelationDao.save(otp);
			}
		} else {
			for(OrderPayDetail detail:opf.getOrderPayInput().getOrderPayDetailList()){

				// 结算方式为即时结算时
				if (opf.getPaymentStatus() == 1) {
					Traveler traveler = travelerDao.findById(detail.getTravelerId());
					// 判断PayedMoneySerialNum是否为null
					// 如果为空,新生成一个UUID,将传过来的UUID对应的金额保存到MoneyAmount
					if (StringUtils.isBlank(traveler.getPayedMoneySerialNum())) {
						String travlerPayedMoneySerialNum = this.getUUID();
						List<MoneyAmount> travlerPayedMoneyList = moneyAmountDao.findAmountBySerialNum(detail.getMoneySerialNum());
						MoneyAmount ma = null;
						for (int i = 0; i < travlerPayedMoneyList.size(); i++) {
							ma = new MoneyAmount();
							ma.setAmount(travlerPayedMoneyList.get(i).getAmount());
							ma.setCurrencyId(travlerPayedMoneyList.get(i).getCurrencyId());
							ma.setSerialNum(travlerPayedMoneySerialNum);
							ma.setMoneyType(Context.MONEY_TYPE_WK);
							ma.setOrderType(Context.ORDER_TYPE_QZ);
							ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_YK);
							ma.setUid(detail.getTravelerId());
							ma.setCreatedBy(UserUtils.getUser().getId());
							ma.setCreateTime(new Date());
							
							moneyAmountService.saveOrUpdateMoneyAmount(ma);
						}
						// 将新生成的UUID保存到游客表payed_moneySerialNum(游客已付款流水号)
						traveler.setPayedMoneySerialNum(travlerPayedMoneySerialNum);
						traveler.setPaymentType(opf.getPaymentStatus());
						travelerDao.save(traveler);
					// 如果不为空,从游客表中取得UUID，并与传过来的UUID相回，并保存到MoneyAmount
					} else {
						List<MoneyAmount> moneyAmountList = moneyAmountService.addMoney(traveler.getPayedMoneySerialNum(), detail.getMoneySerialNum());
						
						moneyAmountService.updateMoneyAmount(traveler.getPayedMoneySerialNum(), moneyAmountList);
						
					}

					// 根据游客更新订单信息
					// 当前游客订单ID
					Long orderId = detail.getOrderId();
					// 根据订单ID查询信息
					VisaOrder visaOrder = visaOrderDao.findOne(orderId);
					// 当前订单已付金额UUID
					String payedMoney = visaOrder.getPayedMoney();

					String[] currIds = opf.getCurrencyIdPrice();
					String[] currPrices = opf.getDqzfprice();
					List<MoneyAmount> payedMoneyList = moneyAmountDao.findAmountBySerialNum(payedMoney);

					// 订单付款
					boolean flag;
					MoneyAmount ma = null;
					if (payedMoneyList != null && payedMoneyList.size() > 0) {
						for (int i = 0; i < currIds.length; i++) {
							flag = false;
							String currId = currIds[i];
							String currPrice = currPrices[i];
							// 如果该UUID包含数组中的币种
							for (int j = 0; j < payedMoneyList.size(); j++) {
								if(currId.equals(payedMoneyList.get(j).getCurrencyId().toString())){
									List<MoneyAmount> ma1 = moneyAmountDao.findAmountBySerialNumAndCurrencyId(payedMoney,Integer.parseInt(currId));
									ma1.get(0).setAmount(payedMoneyList.get(j).getAmount().add(new BigDecimal(currPrice)));
									moneyAmountService.saveOrUpdateMoneyAmounts(payedMoney, ma1);
									flag = true;
								}
							}
							// 如果该UUID不包含数组中的币种
							if (!flag) {
								ma = new MoneyAmount();
								ma.setAmount(new BigDecimal(currPrice));
								ma.setCurrencyId(Integer.parseInt(currId));
								ma.setSerialNum(payedMoney);
								ma.setMoneyType(Context.MONEY_TYPE_WK);
								ma.setOrderType(Context.ORDER_TYPE_QZ);
								ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_DD);
								ma.setUid(orderId);
								ma.setCreatedBy(UserUtils.getUser().getId());
								ma.setCreateTime(new Date());
								
								moneyAmountService.saveOrUpdateMoneyAmount(ma);
							}
						}
					// 该订单第一次付款
					} else {
						for (int i = 0; i < currIds.length; i++) {
							ma = new MoneyAmount();
							String currId = currIds[i];
							String currPrice = currPrices[i];
							ma.setAmount(new BigDecimal(currPrice));
							ma.setCurrencyId(Integer.parseInt(currId));
							ma.setSerialNum(payedMoney);
							ma.setMoneyType(Context.MONEY_TYPE_WK);
							ma.setOrderType(Context.ORDER_TYPE_QZ);
							ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_DD);
							ma.setUid(orderId);
							ma.setCreatedBy(UserUtils.getUser().getId());
							ma.setCreateTime(new Date());
							
							moneyAmountService.saveOrUpdateMoneyAmount(ma);
						}
					}

					// 更新订单状态
					updateOrderStatusMap = updateOrderStatus(orderId);
					if(updateOrderStatusMap.size()>0){
						errorMsg +=updateOrderStatusMap.get("errorMsg");
					}
				}

				op = new OrderPayRelation();
				String orderPayRelationId = this.getUUID();
				op.setId(orderPayRelationId);
				op.setOrderId(detail.getOrderId().toString());
				op.setPayId(detail.getPayId());
				op.setPaymentStatus(opf.getPaymentStatus());
				orderPayRelationDao.save(op);
				
				otp = new OrderTravelerPayRelation();
				String orderTravelerPayRelationId = this.getUUID();
				otp.setId(orderTravelerPayRelationId);
				otp.setOrderPayRelationId(orderPayRelationId);
				otp.setTravelerId(detail.getTravelerId().toString());
				orderTravelerPayRelationDao.save(otp);
			}
		}
		

		// --------------------------------

		if(StringUtils.isBlank(errorMsg)){
			restV.put("isSuccess", true);
		}else{
			restV.put("isSuccess", false);
			restV.put("errorMsg", errorMsg);
		}
		
		return restV;
	}
	
	/**
	 * 查询支付记录
	 * @param orderId,orderType
	 * @return
	 */
	public List<Object> findPaidRecords(String orderId ,Integer orderType ){
		//String sql = "SELECT o.payType ,o.payTypeName,ac.amount,o.createDate,o.isAsAccount,o.payPriceType,o.id,o.orderId,o.payVoucher,o.reject_reason FROM orderpay o LEFT JOIN amount_currency ac ON o.moneySerialNum=ac.serialNum WHERE o.orderId="+orderId+" and o.orderType="+orderType ;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT o.payType, o.payTypeName, ac.amount, o.createDate, o.isAsAccount, o.payPriceType, o.id, o.orderId, ")
		   .append(" o.payVoucher, o.reject_reason, t.`name` AS travelerName ")
		   .append(" FROM orderpay o ")
		   .append(" LEFT JOIN amount_currency ac ON o.moneySerialNum = ac.serialNum ")
		   .append(" LEFT JOIN traveler t ON o.traveler_id = t.id ")
		   .append(" WHERE o.orderId = '").append(orderId).append("' and o.orderType = '").append(orderType).append("' ");
		List<Object> list = visaOrderDao.findBySql(sql.toString(), List.class);
		return list;
	}
	
	
	/**
	 * 更新订单状态 ：改为已支付
	 * @param orderId
	 */
	private Map<String, Object> updateOrderStatus(Long orderId){
		
			Map<String, Object> map = new HashMap<String, Object>();
			
				VisaOrder pra = visaOrderDao.findOne(orderId);
				//
				@SuppressWarnings("unused")
                String payStatus = pra.getPayStatus() != null ? pra.getPayStatus()
						.toString() : "";

				// 如果订单已经被取消或被删除则不能支付订单
				if (pra.getPayStatus() != null
						&& Context.ORDER_PAYSTATUS_YQX.equals(pra.getPayStatus()
								.toString())) {
					map.put("isSuccess", false);
					map.put("errorMsg", "订单已取消，不能支付");
					return map;
				}
				if (pra.getPayStatus() != null
						&& Context.ORDER_PAYSTATUS_DEL.equals(pra.getPayStatus()
								.toString())) {
					map.put("isSuccess", false);
					map.put("errorMsg", "订单已删除，不能支付");
					return map;
				}
				pra.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
				pra.setVisaOrderStatus(1);// 该字段1表示已支付，见数据库字段注释说明
				visaOrderDao.save(pra);
				return map;
	}
	
	
	
	
	public String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 批量还/借护照
	 * @author yue.wang
	 * @param visa 签证实例
	 * @param passportStatus 操作类型 1：借 4：还
	 * @param visaIds 签证id 以','分割
	 * @param travellerIds 游客id 以','分割
	 * @return map
	 */
	public Map<String, String> batchUpdatePassport(String others,String dates,String pessons,
			Integer passportStatus, String visaIds, String travellerIds,Integer deflag) {
		// TODO Auto-generated method stub
		Map<String,String> resultMap = new HashMap<String,String>();
		//passportStatus:1借
		String[] travellerArr = travellerIds.split(",");
		String[] visaArr = visaIds.split(",");
		String[] otherArr = others.split(",");
		String[] dateArr = dates.split(",");
		String[] pessonArr = pessons.split(",");
		java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		Date date = null;
		
		for(int i =0;i<visaArr.length;i++){
			if(visaArr[i].equals("0")){
				continue;
			}
			try {
				date = format1.parse(dateArr[i]);
			} catch (ParseException e) {
				e.printStackTrace();
				date = new Date();
			}
				String hql = "update Visa set passportOperator=?, passportOperateTime=?, passportOperateRemark=?,passportOperateType=? where id = "+visaArr[i];
				visaDao.getSession().createQuery(hql)
					.setParameter(0, pessonArr[i])
					.setParameter(1, date)
					.setParameter(2, otherArr[i])
					.setParameter(3, 1)
					.executeUpdate();
		}
		String batchNo =   sysBatchNoService.getBorrowPassPortBatchNo();
		if(deflag==1){
			String hql2 = "update Traveler set borrowPassportBatchNo=? where id in ("+travellerIds+")";
			travelerDao.getSession().createQuery(hql2)
			.setParameter(0, batchNo)
			.executeUpdate();
		}else{
			String hql2 = "update Traveler set borrowPassportBatchNo=?,passportStatus=? where id in ("+travellerIds+")";
			travelerDao.getSession().createQuery(hql2)
			.setParameter(0, batchNo)
			.setParameter(1, passportStatus)
			.executeUpdate();
		}
			
			String uid = UUID.randomUUID().toString();
			BatchRecord record = new BatchRecord();
			record.setUuid(uid);
			record.setBatchNo(batchNo);
			record.setType(BatchRecord.TYPE_1);
			record.setCreateTime(new Date());
			record.setCreateUserId(UserUtils.getUser().getId());
			record.setCreateUserName(UserUtils.getUser().getName());
			if(passportStatus==1){  //批量借护照    
				if(deflag==1){
					record.setPassportStatus(0);//0:未借  1:已借  2:未还 3:已还
					record.setIsSubmit(0); //是否提交 0:未提交  1：已提交
				}else{
					record.setPassportStatus(1);
					record.setIsSubmit(1); 
				}
				
			}else{  //批量还护照
				if(deflag==1){
					record.setPassportStatus(2);
					record.setIsSubmit(0);
				}else{
					record.setPassportStatus(3);
					record.setIsSubmit(1);
				}
						
			}	
			batchRecordDao.getSession().save(record);
			
			for(int i =0;i<travellerArr.length-1;i++){
				String uuid = UUID.randomUUID().toString();
				BatchTravelerRelation relation = new BatchTravelerRelation();
				relation.setUuid(uuid);
				relation.setBatchUuid(uid);
				relation.setBatchRecordNo(batchNo);
				relation.setTravelerId(Long.parseLong(travellerArr[i]));
				Traveler traveler = travelerService.findTravelerById(Long.parseLong(travellerArr[i]));
				relation.setTravelerName(traveler.getName());
				relation.setOrderId(traveler.getOrderId());
				Visa visa = visaService.findVisaByTravlerId(Long.parseLong(travellerArr[i]));
				relation.setVisaId(visa.getId());
				if(passportStatus==1){//批量借护照
					relation.setBusinessType(3);//业务类型 1:借款 2：还收据  3：借护照 4：还护照
				}else{  //批量还护照
					relation.setBusinessType(4);
				}
				batchTravelerRelationDao.getSession().save(relation);
			}
		return resultMap;
	}
	
	/**
	 * 批量借护照-已提交-还护照操作
	 * @param others
	 * @param dates
	 * @param pessons
	 * @param passportStatus
	 * @param visaIds
	 * @param travellerIds
	 * @param batchNo
	 * @return
	 */
	public Map<String, String> batchUpdatePassport4jhz(String others,String dates,String pessons,
			Integer passportStatus, String visaIds, String travellerIds,String batchNo) {
		Map<String,String> resultMap = new HashMap<String,String>();
		//passportStatus:1借
		String[] visaArr = visaIds.split(",");
		String[] otherArr = others.split(",");
		String[] dateArr = dates.split(",");
		String[] pessonArr = pessons.split(",");
		String[] travelerArr = travellerIds.split(",");
		java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		Date date = null;
		
		for(int i =0;i<visaArr.length;i++){
			if(visaArr[i].equals("0")){
				continue;
			}
			try {
				date = format1.parse(dateArr[i]);
			} catch (ParseException e) {
				e.printStackTrace();
				date = new Date();
			}
				String hql = "update Visa set passportOperator=?, passportOperateTime=?, passportOperateRemark=?,passportOperateType=? where id = "+visaArr[i];
				visaDao.getSession().createQuery(hql)
					.setParameter(0, pessonArr[i])
					.setParameter(1, date)
					.setParameter(2, otherArr[i])
					.setParameter(3, 1)
					.executeUpdate();
		}
		//产生一个新批次号
		String newBatchNo =   sysBatchNoService.getBorrowPassPortBatchNo();
	
		List<BatchTravelerRelation> list = batchTravelerRelationDao.findTravelerIdByNo(batchNo);
		for(BatchTravelerRelation relation : list){
			Long tid = relation.getTravelerId();
			//游客只保存最新的批次号，护照状态改为4：已还
			String sql = "update Traveler set borrowPassportBatchNo=?, passportStatus=4 where id="+tid;
			travelerDao.getSession().createQuery(sql).setParameter(0, newBatchNo).executeUpdate(); 
		}
		//旧批次记录仍在借护照-已提交列表保留
		String sql1 = "update BatchRecord set passportStatus=3 where passportStatus=1 and isSubmit=1 and batchNo='"+batchNo+"'";
		batchRecordDao.getSession().createQuery(sql1).executeUpdate();
		
		//向batch_record表插入新纪录
		String uid = UUID.randomUUID().toString();
		BatchRecord record = new BatchRecord();
		record.setUuid(uid);
		record.setBatchNo(newBatchNo);
		record.setType(BatchRecord.TYPE_1);
		record.setCreateTime(new Date());
		record.setCreateUserId(UserUtils.getUser().getId());
		record.setCreateUserName(UserUtils.getUser().getName());
		record.setPassportStatus(3);//已还
		record.setIsSubmit(1);//已提交
		batchRecordDao.getSession().save(record);
		
		//有多少游客就向batch_traveler_relation表插入多少条记录
		for(int i =0;i<travelerArr.length-1;i++){
			String uuid = UUID.randomUUID().toString();
			BatchTravelerRelation relation = new BatchTravelerRelation();
			relation.setUuid(uuid);
			relation.setBatchUuid(uid);//batch_record表uuid
			relation.setBatchRecordNo(newBatchNo);
			relation.setTravelerId(Long.parseLong(travelerArr[i]));
			
			Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerArr[i]));
			relation.setTravelerName(traveler.getName());
			relation.setOrderId(traveler.getOrderId());
			
			Visa visa = visaService.findVisaByTravlerId(Long.parseLong(travelerArr[i]));
			relation.setVisaId(visa.getId());
			
			relation.setBusinessType(4);//批量还护照
			batchTravelerRelationDao.getSession().save(relation);
		}
		
//      批量借护照-已提交-还护照时  批次记录转入还护照-待提交列表
//		String sql = "update BatchRecord set passportStatus=2,isSubmit=0 where batchNo='"+batchNo+"'";
//		batchRecordDao.getSession().createQuery(sql).executeUpdate();
//		
//		String sql1 = "update BatchTravelerRelation set businessType=4 where businessType=3 and batchRecordNo in('"+batchNo+"')";
//		batchTravelerRelationDao.getSession().createQuery(sql1).executeUpdate();
		
		return resultMap;
	}
	

	public Map<String,Object> checkBatchBorrowPassport(String visaIds,
			String travellerIds) {
		// TODO Auto-generated method stub
		Map<Integer ,String> statusMap = new HashMap<Integer ,String>();
		statusMap.put(1,"借出");
		statusMap.put(2,"销售已领取");
//		statusMap.put(3,"未签收");
//		statusMap.put(4,"已签收");
		statusMap.put(4,"已还");
		statusMap.put(5,"已取出");
		statusMap.put(6,"未取出");
//		statusMap.put(7,"走团");
		statusMap.put(8,"计调领取");
		
		
		String hql  = " select visa.id as visaId , t.id as tid , t.name as tname  ,t.passportStatus as passportStatus ,t.passportCode as passportCode from visa  visa , traveler t where t.id  = visa.traveler_id and visa.id in ("+visaIds+")";
		List<Map<String, Object>> resultList = visaDao.findBySql(hql,Map.class);
		List<Map<String,Object>> errList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
		String curDate = DateUtils.getDate();
		String personName = UserUtils.getUser().getName();
		for(Map<String,Object> map:resultList){
			Integer passpoatStatus = (Integer)map.get("passportStatus");
			map.put("passportStatusDes", statusMap.get(passpoatStatus)==null?"无":statusMap.get(passpoatStatus));
//			if(passpoatStatus==null||passpoatStatus!=4){
			if(passpoatStatus !=null && passpoatStatus==1){
				map.put("errMsg", "护照状态不符");
				errList.add(map);
			}else{
				map.put("curDate", curDate);
				map.put("borrowPerson", personName);
				rightList.add(map);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rightList", rightList);
		result.put("errList", errList);
		return result;
	}
	

	/**
	 * 取消正在审核中的流程并删除游客提示框信息显示
	 * @author ang.gao
	 * @param travelerIds
	 * @return
	 */
	public Map<String,Object> cancelReviewAndDelete(String travelerIds) {
		String[] strArray = travelerIds.split(",");
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer massage = new StringBuffer();
		String tId = "";//需要取消审核流程并删除的游客Id
		for(int i = 0;i<strArray.length-1;i++){
			boolean JKFlag = true;//借款标志
			boolean FYFlag = true;//返佣标志
			boolean GJFlag = true;//改价标志
			Traveler traveler = travelerDao.findById(Long.parseLong(strArray[i]));
			List<Review> reviewList = reviewDao.getReviewingRecordBytId(Long.parseLong(strArray[i]));
			
			/* 查询订单id部门id*/
			String str = "SELECT  vo.id as orderId,vp.deptId FROM traveler t LEFT JOIN visa_order vo ON t.orderId=vo.id LEFT JOIN visa_products vp ON vo.visa_product_id=vp.id WHERE t.id=? ";
			List<Map<String,Object>> orderAndDeptIDList = travelerDao.findBySql(str, Map.class, strArray[i]);
			String orderId =  orderAndDeptIDList.get(0).get("orderId").toString();
			String deptId = orderAndDeptIDList.get(0).get("deptId").toString();
			
			/*从新审核表review_new中查找该游客审批中的记录,有团队返佣情况不会进入这一步,此时查出的记录均为单游客审批记录*/
			String sql = " SELECT rn.process_type AS flowType FROM review_new rn ";
			       sql += " WHERE rn.del_flag = 0 AND rn.product_type = 6 ";
			       sql += " AND rn.status = 1 ";
			       sql += " AND rn.process_type IN ('5', '19', '9', '10')";
			       sql += " AND rn.traveller_id LIKE '%"+strArray[i]+"%' ";
			       sql += " AND rn.order_id=? ";
			       sql += " AND rn.dept_id=? ";
			 List<Map<String,Object>> resultList = reviewDao.findBySql(sql, Map.class, orderId,deptId);
			
			if(!reviewList.toString().equals("[]") || CollectionUtils.isNotEmpty(resultList)){
				tId += traveler.getId()+",";
				massage.append("游客<span style='color:orange;'>"+traveler.getName()+" </span>所在订单有");	
				for(Review review : reviewList){
					if(review.getFlowType()==5){
						if(JKFlag){
							massage.append(" 借款");
							JKFlag = false;
						}
					}
					if(review.getFlowType()==9){
						if(FYFlag){
							massage.append(" 返佣");
							FYFlag = false;
						}
					}
					if(review.getFlowType()==10){
						if(GJFlag){
							massage.append(" 改价");
							GJFlag = false;
						}
					}
				}
				/*拼接新审批记录*/
				for(Map<String,Object> maps : resultList){
					if(maps.get("flowType").toString().equals("5") || maps.get("flowType").toString().equals("19")){
						if(JKFlag){
							massage.append(" 借款");
							JKFlag = false;
						}
					}
					if(maps.get("flowType").toString().equals("9")){
						if(FYFlag){
							massage.append(" 返佣");
							FYFlag = false;
						}
					}
					if(maps.get("flowType").toString().equals("10")){
						if(GJFlag){
							massage.append(" 改价");
							GJFlag = false;
						}
					}
				}
				
				massage.append(" 流程正在审核中</br></br>");
			}
		}
		if(tId==""){
			massage.append("您确定要删除此<span style='color:red;'> ").append(strArray.length-1).append(" </span>项游客信息？");
		}else{
			massage.append("您确定取消这些审核流程并删除此<span style='color:red;'> ").append(strArray.length-1).append(" </span>项游客信息？");
		}
		
		map.put("waringMsg", massage);
		map.put("tid", tId);
		return map;
	}
	
	/**
	 * 取消正在审核中的流程并删除游客
	 * @author ang.gao
	 * @param travelerIds
	 * @param tids
	 * @return
	 */
	public Map<String,Object> doDelete(String travelerIds,String tids) {
		Map<String,Object> map = new HashMap<String,Object>();
		String[] travelerArray = travelerIds.split(",");
		if(tids!=""){//取消审核流程
			String[] tArray = (tids+"0").split(",");
			for(int i = 0;i<tArray.length-1;i++){
				/* 查询订单id部门id*/
				String str = "SELECT  vo.id as orderId,vp.deptId FROM traveler t LEFT JOIN visa_order vo ON t.orderId=vo.id LEFT JOIN visa_products vp ON vo.visa_product_id=vp.id WHERE t.id=? ";
				List<Map<String,Object>> orderAndDeptIDList = travelerDao.findBySql(str, Map.class, tArray[i]);
				String orderId =  orderAndDeptIDList.get(0).get("orderId").toString();
				String deptId = orderAndDeptIDList.get(0).get("deptId").toString();
				
				List<Review> reviewList = reviewDao.getReviewingRecordBytId(Long.parseLong(tArray[i]));
				for(Review review : reviewList){
					reviewDao.removeMyReview(review.getId());
				}
				/*取消新审批流程*/
				String sql = " SELECT rn.id AS reviewUuid FROM review_new rn ";
			       sql += " WHERE rn.del_flag = 0 AND rn.product_type = 6 ";
			       sql += " AND rn.status = 1 ";
			       sql += " AND rn.process_type IN ('5', '19', '9', '10')";
			       sql += " AND rn.traveller_id LIKE '%"+tArray[i]+"%' ";
			       sql += " AND rn.order_id=? ";
			       sql += " AND rn.dept_id=? ";
			    List<Map<String,Object>> resultList = reviewDao.findBySql(sql, Map.class, orderId,deptId);
			    if(CollectionUtils.isNotEmpty(resultList)){
			    	for(Map<String,Object> maps : resultList){
			    		String sql1 = "UPDATE review_new SET status=3 where id='"+maps.get("reviewUuid")+"'";
			    		reviewDao.updateBySql(sql1);
			    	}
			    }
			}
		}
		//更新订单应收金额，删除游客	
		for(int i = 0;i<travelerArray.length-1;i++){
			Traveler traveler = travelerDao.findById(Long.parseLong(travelerArray[i]));
			VisaOrder visaOrder = visaOrderDao.findByOrderId(traveler.getOrderId());
			String orderUUID = visaOrder.getTotalMoney();//订单总价UUID
			String travelerUUID = traveler.getPayPriceSerialNum();//游客结算价UUID
			moneyAmountDao.clear();	
			//查询金额信息，考虑多币种情况
			List<MoneyAmount> orderAmountList = moneyAmountDao.findAmountListBySerialNum(orderUUID);
			List<MoneyAmount> travelerAmountList = moneyAmountDao.findAmountListBySerialNum(travelerUUID);
			for(MoneyAmount orderAmount : orderAmountList){
				for(MoneyAmount travelerAmount : travelerAmountList){
					if(orderAmount.getCurrencyId().equals(travelerAmount.getCurrencyId())){
						//订单应收金额-被删除游客的结算价
						BigDecimal value = new BigDecimal(VisaOrderTravelerPayLogService.sub1(orderAmount.getAmount().toString(), travelerAmount.getAmount().toString()));
						String hql = "update MoneyAmount set amount=? where id="+orderAmount.getId();
						moneyAmountDao.createQuery(hql)
						.setParameter(0, value)
						.executeUpdate();
					}
				}
			}		
			//删除游客 deflag=1
			travelerDao.updateTravelerDelFlag(1,Long.parseLong(travelerArray[i]));
			//更新订单游客人数
			visaOrderDao.clear();
			String hql2 = "update VisaOrder set travelNum="+(visaOrder.getTravelNum()-1)+" where id="+visaOrder.getId();
			visaOrderDao.createQuery(hql2).executeUpdate();
			
			//-------by------junhao.zhao-----2017-03-23--------visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额、支付状态修改---开始---
			orderDateSaveOrUpdateService.updatePeopleAndMoney(visaOrder.getId(),Context.ORDER_TYPE_QZ);
			//-------by------junhao.zhao-----2017-03-23--------visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额、支付状态修改---结束---
			
			//订单下所有游客被删除时，删除订单
			if(visaOrder.getTravelNum()-1 == 0){
				visaOrderDao.updateOrder4Delete(visaOrder.getId());
				
				//-------by------junhao.zhao-----2017-03-23-------visa_order订单删除之后，修改表order_data_statistics中对应的订单状态---------------开始---
				orderDateSaveOrUpdateService.updateDelFlag(visaOrder.getId(),Context.ORDER_TYPE_QZ);
				//-------by------junhao.zhao-----2017-03-23-------visa_order订单删除之后，修改表order_data_statistics中对应的订单状态---------------结束---
				
			}
			
		}
		return map;
	}
	
	/**
	 * 检查游客是否满足删除条件
	 * @author ang.gao
	 * @param travellerIds
	 * @return
	 */
	public Map<String,Object> check4DeleteTravelers(String travelerIds) {
		String[] strArray = travelerIds.split(",");
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer massage = new StringBuffer();
		
		for(int i = 0;i<strArray.length-1;i++){
			
			boolean yjk = true;//已借款
			boolean yfy = true;//已返佣
			boolean ygj = true;//已改价
			boolean tdfyshz = true;//团队返佣审核中
			boolean ytdfy = true;//已团队返佣
			boolean yfk = true;//已付款
			boolean printFlag = false;
			/* 查询订单id部门id*/
			String str = "SELECT  vo.id as orderId,vp.deptId FROM traveler t LEFT JOIN visa_order vo ON t.orderId=vo.id LEFT JOIN visa_products vp ON vo.visa_product_id=vp.id WHERE t.id=? ";
			List<Map<String,Object>> orderAndDeptIDList = travelerDao.findBySql(str, Map.class, strArray[i]);
			String orderId =  orderAndDeptIDList.get(0).get("orderId").toString();
			String deptId = orderAndDeptIDList.get(0).get("deptId").toString();
			
			/* 查询新审批表review_new 该订单下所有借款审批记录  流程类型为5或19*/
			List<Map<String, Object>> JKlist = processReviewService.getReviewDetailMapListByOrderId(Long.parseLong(deptId), 6, 
					Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
			List<Map<String, Object>> XXZ_JKlist = processReviewService.getReviewDetailMapListByOrderId(Long.parseLong(deptId), 6, 
					Context.REVIEW_FLOWTYPE_BORROWMONEY, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
			
			/* 查询新审批表review_new 该订单下所有改价审批记录  流程类型为10*/
			List<Map<String, Object>> GJlist = processReviewService.getReviewDetailMapListByOrderId(Long.parseLong(deptId), 6, 
					Context.REVIEW_FLOWTYPE_CHANGE_PRICE, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
			
			/* 查询新审批表review_new 该订单下所有返佣审批记录  流程类型为9*/
			List<Map<String, Object>> FYlist = processReviewService.getReviewDetailMapListByOrderId(Long.parseLong(deptId), 6, 
					Context.REBATES_FLOW_TYPE, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
			
			/*将所有需要的审批记录放入一个List中   begin*/
			List<Map<String,Object>> reviewResultList = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> maps : JKlist){
				if(maps.get("status").toString().equals("2")){//已通过
					reviewResultList.add(maps);
				}
			}
			for(Map<String,Object> maps : XXZ_JKlist){
				if(maps.get("status").toString().equals("2")){//已通过
					reviewResultList.add(maps);
				}
			}
			for(Map<String,Object> maps : GJlist){
				if(maps.get("status").toString().equals("2")){//已通过
					reviewResultList.add(maps);
				}
			}
			for(Map<String,Object> maps : FYlist){
				if(maps.get("status").toString().equals("2") || maps.get("status").toString().equals("1")){//已通过、审批中
					//判断是团队还是个人返佣
					String showName = "";
					String travellerId = maps.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString();//游客Id集合封装
					String travellerName = maps.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME).toString();//游客名称集合封装
					String grouprebatesnames = maps.get("grouprebatesnames").toString();
					if(StringUtils.isNotEmpty(grouprebatesnames)) {
						showName = "团队";
					} else if(StringUtils.isNotEmpty(travellerId)) {
						String[] travellerIdArr = travellerId.split(VisaBorrowMoneyController.SPLITMARK);
						if(travellerIdArr.length > 1) {
							showName = "团队";
						} else if(StringUtils.isNotEmpty(grouprebatesnames)) {
							showName = "团队";
						} else {
							showName = travellerName.split(VisaBorrowMoneyController.SPLITMARK)[0];
						}
					}
					maps.put("showName", showName);
					
					reviewResultList.add(maps);
				}
			}
			/*------------end---------------*/
			
			Traveler traveler = travelerDao.findById(Long.parseLong(strArray[i]));
			List<Review> reviewList = reviewDao.getSuccessedBytId(Long.parseLong(strArray[i]));	//该游客所有审核通过的记录
			
			// 再次循环取出属于该游客的审批记录,不包括团队返佣的记录,放入travellerReviewList,订单的团队返佣记录放入groupReviewList
			List<Map<String,Object>> travellerReviewList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> groupReviewList = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> maps : reviewResultList){
				if(maps.get("showName")==null || !maps.get("showName").toString().equals("团队")){//非团队返佣
					if(maps.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString().contains(strArray[i])){//为该游客的返佣记录
						if(maps.get("status").toString().equals("2")){//审批状态为2,已通过
							travellerReviewList.add(maps); 
						}
					}
				}
				if(maps.get("showName")!=null && maps.get("showName").toString().equals("团队")){//团队返佣记录
					groupReviewList.add(maps);
				}
			}
			
			//先判断是否有审核通过的改价、借款、个人返佣，若有记录不可删
			if(!reviewList.toString().equals("[]") || CollectionUtils.isNotEmpty(travellerReviewList)){
				massage.append(traveler.getName()+"");
				for(Review review : reviewList){	
					if(review.getFlowType() == 5){
						if(yjk)
						massage.append(" 已借款");
						yjk = false;
					}
					if(review.getFlowType() == 9){
						if(yfy)
						massage.append(" 已返佣");
						yfy = false;
					}
					if(review.getFlowType() == 10){
						if(ygj)
						massage.append(" 已改价");
						ygj = false;
					}	
				}
				/*拼接新审批表数据*/
				for(Map<String,Object> reviewResultMap : travellerReviewList){
					if(reviewResultMap.get("processType").toString().equals("5") || reviewResultMap.get("processType").toString().equals("19")){//type=19 新行者签证借款
						if(yjk)
						massage.append(" 已借款");
						yjk = false;
					}
					if(reviewResultMap.get("processType").toString().equals("9")){
						if(yfy)
						massage.append(" 已返佣");
						yfy = false;
					}
					if(reviewResultMap.get("processType").toString().equals("10")){
						if(ygj)
						massage.append(" 已改价");
						ygj = false;
					}
				}
				
			}
			//再判断是否有团队返佣通过和审核中的情况
			List<Review> RebatesList = reviewDao.getSuccessedBatchBytId(traveler.getOrderId().toString());//某订单下的团队返佣通过和审核中记录
			if(reviewList.toString().equals("[]") && CollectionUtils.isEmpty(travellerReviewList) && RebatesList.toString().equals("[]") && CollectionUtils.isEmpty(groupReviewList)){
				
			}else if((reviewList.toString().equals("[]") && CollectionUtils.isEmpty(travellerReviewList)) && (!RebatesList.toString().equals("[]") || CollectionUtils.isNotEmpty(groupReviewList))){
				massage.append(traveler.getName()+"");
				for(Review r : RebatesList){
					//两个人以上的返佣也属于团队返佣，只要是团队返佣，该订单下所有游客都不能删除！
					if( r.getStatus()==1){//团队返佣审核中
						if(tdfyshz)
						massage.append(" 团队返佣审核中");
						tdfyshz = false;
							
					}
					if( r.getStatus()==2 || r.getStatus()==3){//团队返佣通过
						if(ytdfy){
							massage.append(" 已团队返佣");
							ytdfy = false;
						}													
					}
				}
				/*拼接新审批表数据*/
				for(Map<String,Object> maps : groupReviewList){
					if(maps.get("status").toString().equals("1")){
						if(tdfyshz)
						massage.append(" 团队返佣审核中");
						tdfyshz = false;
					}
					if(maps.get("status").toString().equals("2")){
						if(ytdfy){
							massage.append(" 已团队返佣");
							ytdfy = false;
						}	
					}
				}
			}else if((!reviewList.toString().equals("[]") || CollectionUtils.isNotEmpty(travellerReviewList)) && (RebatesList.toString().equals("[]") && CollectionUtils.isEmpty(groupReviewList))){
				
			}else if((!reviewList.toString().equals("[]") || CollectionUtils.isNotEmpty(travellerReviewList)) && (!RebatesList.toString().equals("[]") || CollectionUtils.isNotEmpty(groupReviewList))){
				for(Review r : RebatesList){
					if( r.getStatus()==1){//团队返佣审核中
						if(tdfyshz)
						massage.append(" 团队返佣审核中");
						tdfyshz = false;
					}
					if(r.getStatus()==2 || r.getStatus()==3){//团队返佣通过
						if(ytdfy)
						massage.append(" 已团队返佣");
						ytdfy = false;
					}
				}
				/*拼接新审批表数据*/
				for(Map<String,Object> maps : groupReviewList){
					if(maps.get("status").toString().equals("1")){
						if(tdfyshz)
						massage.append(" 团队返佣审核中");
						tdfyshz = false;
					}
					if(maps.get("status").toString().equals("2")){
						if(ytdfy){
							massage.append(" 已团队返佣");
							ytdfy = false;
						}	
					}
				}
			}
			//判断是否有付款、付押金、参团情况
			String hql = "select t.id,t.name,t.payed_moneySerialNum,v.payed_deposit,a.groupCode ";
			hql += " from traveler t LEFT JOIN visa v on t.id = v.traveler_id ";
			hql += " LEFT JOIN productorder po on po.id = t.main_order_id ";
			hql += " LEFT JOIN activitygroup a on a.id = po.productGroupId where t.id ="+traveler.getId();
			List<Map<String, Object>> resultList = visaDao.findBySql(hql,Map.class);
			Map<String, Object> resultMap = resultList.get(0);
			if(reviewList.toString().equals("[]")  && RebatesList.toString().equals("[]") && CollectionUtils.isEmpty(travellerReviewList) && CollectionUtils.isEmpty(groupReviewList)){//前两个集合为空，要拼游客名称
				if(resultMap.get("groupCode") != null){//游客参团团号
					massage.append(resultMap.get("name")+" 已参团");
					if(resultMap.get("payed_moneySerialNum") != null){//已付金额uuid
						String hql1 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_moneySerialNum")+"'";
						Object o = visaDao.findBySql(hql1);
						if(!o.toString().contains("[0.00]")){//金额未驳回
							massage.append(" 已付款");
						}
					}
					if(resultMap.get("payed_deposit") != null){//已付押金UUID
						String hql2 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_deposit")+"'";
						Object o1 = visaDao.findBySql(hql2);
						if(!o1.toString().contains("[0.00]")){
							massage.append(" 已付押金");
						}
					}
					massage.append(",您不能删除此游客信息!</br></br>");
				}else if(resultMap.get("payed_moneySerialNum") != null){
					String hql1 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_moneySerialNum")+"'";
					Object o = visaDao.findBySql(hql1);
					if(!o.toString().contains("[0.00]")){//金额未驳回
						massage.append(resultMap.get("name")+" 已付款");
						yfk = false;
						printFlag = true;
					}
					if(resultMap.get("payed_deposit") != null){
						String hql2 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_deposit")+"'";
						Object o1 = visaDao.findBySql(hql2);
						if(!o1.toString().contains("[0.00]") && !yfk){
							massage.append(" 已付押金");
						}else if(!o1.toString().contains("[0.00]") && yfk){
							massage.append(resultMap.get("name")+" 已付押金");
							printFlag = true;
						}else{
							massage.append("");
						}
					}
					if(printFlag){
						massage.append(",您不能删除此游客信息!</br></br>");
					}			
				}else if(resultMap.get("payed_deposit") != null){
					String hql1 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_deposit")+"'";
					Object o1 = visaDao.findBySql(hql1);
					if(!o1.toString().contains("[0.00]")){
						massage.append(resultMap.get("name")+" 已付押金,您不能删除此游客信息!</br></br>");
					}
				}
			}else{//massage已经有内容，不需要拼游客名了
				if(resultMap.get("groupCode") != null){//游客参团团号
					massage.append(" 已参团");
					if(resultMap.get("payed_moneySerialNum") != null){//已付金额uuid
						String hql1 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_moneySerialNum")+"'";
						Object o = visaDao.findBySql(hql1);
						if(!o.toString().contains("[0.00]")){//金额未驳回
							massage.append(" 已付款");
						}
					}
					if(resultMap.get("payed_deposit") != null){//已付押金UUID
						String hql2 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_deposit")+"'";
						Object o1 = visaDao.findBySql(hql2);
						if(!o1.toString().contains("[0.00]")){
							massage.append(" 已付押金");
						}
					}
					massage.append(",您不能删除此游客信息!</br></br>");
				}else if(resultMap.get("payed_moneySerialNum") != null){
					String hql1 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_moneySerialNum")+"'";
					Object o = visaDao.findBySql(hql1);
					if(!o.toString().contains("[0.00]")){//金额未驳回
						massage.append(" 已付款");
					}
					if(resultMap.get("payed_deposit") != null){
						String hql2 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_deposit")+"'";
						Object o1 = visaDao.findBySql(hql2);
						if(!o1.toString().contains("[0.00]") ){
							massage.append(" 已付押金");
						}
					}
					massage.append(",您不能删除此游客信息!</br></br>");		
				}else if(resultMap.get("payed_deposit") != null){
					String hql1 = "select ma.amount from money_amount ma where ma.serialNum='"+resultMap.get("payed_deposit")+"'";
					Object o1 = visaDao.findBySql(hql1);
					if(!o1.toString().contains("[0.00]")){
						massage.append(" 已付押金");
					}
					massage.append(",您不能删除此游客信息!</br></br>");
				}else{//都不满足，直接拼结束语
					massage.append(",您不能删除此游客信息!</br></br>");
				}
				
			}
		}
		
		map.put("errMsg", massage);
		return map;
	}
	
	
	public Map<String,Object> checkBatchReturnPassport(String visaIds,String travellerIds) {
		Map<Integer ,String> statusMap = new HashMap<Integer ,String>();
		statusMap.put(1,"借出");
		statusMap.put(2,"销售已领取");
//		statusMap.put(3,"未签收");
//		statusMap.put(4,"已签收");
		statusMap.put(4,"已还");
		statusMap.put(5,"已取出");
		statusMap.put(6,"未取出");
//		statusMap.put(7,"走团");
		statusMap.put(8,"计调领取");
		
		//String hql  = " select visa.id as visaId , t.id as tid , t.name as tname  ,t.passportStatus as passportStatus ,t.passportCode as passportCode from visa  visa , traveler t where t.id  = visa.traveler_id and visa.id in ("+visaIds+")";
		String hql= "select visa.id as visaId , t.id as tid , t.name as tname  ,t.passportStatus as passportStatus ,t.passportCode as passportCode, t.borrow_passport_batch_no as batchno from visa  visa , traveler t where t.id  = visa.traveler_id and visa.id in ("+visaIds+")";
		List<Map<String, Object>> resultList = visaDao.findBySql(hql,Map.class);
		List<Map<String,Object>> errList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
		String curDate = DateUtils.getDate();
		String personName = UserUtils.getUser().getName();
		for(Map<String,Object> map:resultList){
			Integer passpoatStatus = (Integer)map.get("passportStatus");
			map.put("passportStatusDes", statusMap.get(passpoatStatus)==null?"无":statusMap.get(passpoatStatus));
//			if(passpoatStatus==null||passpoatStatus!=1){
//				map.put("errMsg", "护照状态不符");
//				errList.add(map);
//			}else{
				map.put("curDate", curDate);
				map.put("borrowPerson", personName);
				rightList.add(map);
//			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rightList", rightList);
		result.put("errList", errList);
		return result;
	}

	/**
	 * 校验批量还收据数据
	 * @author yue.wang
	 * @param visaIds 签证id 以','分割
	 * @param travellerIds 游客id 以','分割
	 * @return map
	 */
	public Map<String, Object> checkBatchHsj(String visaIds, String travellerIds) {
		String hql  = " select visa.id as visaId , t.id as tid , t.name as tname  ,t.passportStatus as passportStatus ,t.passportCode as passportCode," +
				"  t.payPriceSerialNum as payPriceSerialNum ,  t.accounted_money as accounted_money,t.orderId as orderId  " +
				" from visa  visa , traveler t where t.id  = visa.traveler_id and visa.id in ("+visaIds+")";
		List<Map<String, Object>> resultList = visaDao.findBySql(hql,Map.class);
		List<Map<String,Object>> errList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
		String curDate = DateUtils.getDate();
		String personName = UserUtils.getUser().getName();
		for(Map<String,Object> map:resultList){
			String jiekuanStatus = "";
			String jiekuanJe = "";
//			if(null != map.get("accounted_money") &&  !"".equals(map.get("accounted_money").toString())){
//	    		Map<String,String> checkMap = getChajia(map.get("payPriceSerialNum").toString(), map.get("accounted_money").toString());
//	    		if(null == checkMap ){
//	    			map.put("jiekuanStatus", "已还");//借款状态(1:未借2:已借3:已还)
//	    		}
//	    	}else{
	    	
	    		String returnReceiptandCheckStatus = getTravelerBorrowedMoney(map.get("orderId").toString(),(long)(Integer)map.get("tid"));
	    		List<Review> listReview = reviewDao.findReviewActive(6,4,map.get("orderId").toString(),(long)(Integer)map.get("tid"));
		    	if (null != returnReceiptandCheckStatus) {
		    		//借款
		    		jiekuanStatus = returnReceiptandCheckStatus.split(",")[1];
		    		//2 表示审核成功 设置已 未借款	
		    		if("2".equals(jiekuanStatus)){
		    			if(listReview!=null && listReview.size()>0){
		    				map.put("jiekuanStatus", "已借");
		    			} else {
				    		//借款状态(1:未借2:已借3:已还)
			    			map.put("jiekuanStatus", "已借");
		    			}
		    		}else if("1".equals(jiekuanStatus)){
		    			//设置为 借款审批中	
		    			map.put("jiekuanStatus", "审批中");
		    		}else if("3".equals(jiekuanStatus)){
		    			//设置为 已还	
		    			map.put("jiekuanStatus", "已还");
		    		}
	    			jiekuanJe = returnReceiptandCheckStatus.split(",")[0];
	    			map.put("jiekuanJe", jiekuanJe);
				}else{
		    		//设置为 未借款	
					jiekuanStatus="1";
					map.put("jiekuanStatus", "未借");
				}
//	    	}
			
			map.put("visaFee", moneyAmountService.getMoney(map.get("payPriceSerialNum").toString()));
			
			if(jiekuanStatus.equals("2") && listReview!=null && listReview.size()>0){
				map.put("errMsg", "已提交还收据申请");
				errList.add(map);
			} else if(jiekuanStatus.equals("1") && null != returnReceiptandCheckStatus){
				map.put("errMsg", "借款审批中");
				errList.add(map);
			} else if(jiekuanStatus.equals("1") && null == returnReceiptandCheckStatus){
				map.put("errMsg", "尚无借款");
				errList.add(map);
			}else if(jiekuanStatus.equals("3")){
				map.put("errMsg", "还收据审批中");
				errList.add(map);
			}else{
				map.put("curDate", curDate);
				map.put("returnReceiptPerson", personName);
				rightList.add(map);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rightList", rightList);
		result.put("errList", errList);
		return result;
	}

	
	/**
	 * 校验批量借款
	 * @author yue.wang
	 * @param visaIds 签证id 以','分割
	 * @param travellerIds 游客id 以','分割
	 * @return map
	 */
	public Map<String, Object> checkBatchJk(String visaIds,
			String travellerIds) {
		// TODO Auto-generated method stub

		String hql  = " select visa.id as visaId , t.id as tid , t.name as tname  ,t.passportStatus as passportStatus ,t.passportCode as passportCode," +
				"  t.payPriceSerialNum as payPriceSerialNum ,  t.accounted_money as accounted_money ,t.orderId as orderId  " +
				" from visa  visa , traveler t where t.id  = visa.traveler_id and visa.id in ("+visaIds+")";
		List<Map<String, Object>> resultList = visaDao.findBySql(hql,Map.class);
		List<Map<String,Object>> errList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
		String curDate = DateUtils.getDate();
		String personName = UserUtils.getUser().getName();
		for(Map<String,Object> map:resultList){
			String errMsg = "";
			
			if(null != map.get("accounted_money") &&  !"".equals(map.get("accounted_money").toString()) && 
			   null == getChajia(map.get("payPriceSerialNum").toString(), map.get("accounted_money").toString())){
	    			map.put("jiekuanStatus", "已还");//借款状态(1:未借2:已借3:已还)
	    			errMsg= "已达账";
	    	}else{
	    	
	    		String borrowMoneyandCheckStatus = getTravelerBorrowedMoney( map.get("orderId").toString(),(long)(Integer)map.get("tid"));
		    	if (null != borrowMoneyandCheckStatus) {
		    		//借款
		    		String jiekuanStatus   = borrowMoneyandCheckStatus.split(",")[1];
		    		//2 表示审核成功 设置已 未借款	
		    		if("2".equals(jiekuanStatus)){
		    		   //借款状态(1:未借2:已借3:已还)
		    			map.put("jiekuanStatus", "已借");
		    			errMsg= "借款状态不符";
		    		}else if("1".equals(jiekuanStatus)){
		    			//设置为 未借款	虽然是未借款的状态，但是要算作是不符合条件的游客， 不符合的原因 固定显示：借款审核中
		    			map.put("jiekuanStatus", "审批中");
		    			errMsg= "借款审批中";
		    		}else if("3".equals(jiekuanStatus)){
		    			map.put("jiekuanStatus", "已还");
		    			errMsg= "已达账";
		    		}
				}else{
		    		//设置为 未借款	
					map.put("jiekuanStatus", "未借");
				}
	    	}
		    	
			map.put("visaFee", moneyAmountService.getMoney(map.get("payPriceSerialNum").toString()));
			
			if(!StringUtils.isBlank(errMsg)){
				map.put("errMsg", errMsg);
				errList.add(map);
			}else{
				map.put("curDate", curDate);
				map.put("borrowPerson", personName);
				rightList.add(map);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rightList", rightList);
		result.put("errList", errList);
		return result;
	
	}

	/**
	 * 校验批量借款
	 * @author yue.wang
	 * @param visaIds 签证id 以','分割
	 * @param travellerIds 游客id 以','分割
	 * @return map
	 */
	public Map<String, Object> checkBatchJkHqx(String visaIds,
			String travelerIds) {
		// TODO Auto-generated method stub

		String hql  = " select visa.id as visaId , t.id as tid , t.name as tname  ,t.passportStatus as passportStatus ,t.passportCode as passportCode," +
				"  t.payPriceSerialNum as payPriceSerialNum ,  t.accounted_money as accounted_money ,t.orderId as orderId  " +
				" from visa  visa , traveler t where t.id  = visa.traveler_id and visa.id in ("+visaIds+")";
		List<Map<String, Object>> resultList = visaDao.findBySql(hql,Map.class);
		List<Map<String,Object>> errList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
		String curDate = DateUtils.getDate();
		String personName = UserUtils.getUser().getName();
		for(Map<String,Object> map:resultList){
			String errMsg = "";
			
			boolean isSubmit = false;
			List<BatchTravelerRelation> batchTravelerRelationList = batchTravelerRelationDao.findBatchNo((long)(Integer)map.get("tid"), 1);
			if (batchTravelerRelationList != null && batchTravelerRelationList.size() > 0) {
				for(BatchTravelerRelation list : batchTravelerRelationList){
					if("1".equals(list.getIsSubmit())){
						isSubmit=true;
						break;
					}
				}
			}
			
			if(null != map.get("accounted_money") &&  !"".equals(map.get("accounted_money").toString()) && 
			   null == getChajia(map.get("payPriceSerialNum").toString(), map.get("accounted_money").toString())){
	    			map.put("jiekuanStatus", "已还");//借款状态(1:未借2:已借3:已还)
	    			errMsg= "已达账";
	    	}else if(isSubmit){
	    		map.put("jiekuanStatus", "已保存");
    			errMsg= "借款状态不符";
	    	}else{
	    	
	    		String borrowMoneyandCheckStatus = getTravelerBorrowedMoney( map.get("orderId").toString(),(long)(Integer)map.get("tid"));
		    	if (null != borrowMoneyandCheckStatus) {
		    		//借款
		    		String jiekuanStatus   = borrowMoneyandCheckStatus.split(",")[1];
		    		//2 表示审核成功 设置已 未借款	
		    		if("2".equals(jiekuanStatus)){
		    		   //借款状态(1:未借2:已借3:已还)
		    			map.put("jiekuanStatus", "已借");
		    			errMsg= "借款状态不符";
		    		}else if("1".equals(jiekuanStatus)){
		    			//设置为 未借款	虽然是未借款的状态，但是要算作是不符合条件的游客， 不符合的原因 固定显示：借款审核中
		    			map.put("jiekuanStatus", "审批中");
		    			errMsg= "借款审批中";
		    		}else if("3".equals(jiekuanStatus)){
		    			map.put("jiekuanStatus", "已还");
		    			errMsg= "已达账";
		    		}
				}else{
		    		//设置为 未借款	
					map.put("jiekuanStatus", "未借");
				}
	    	}
		    	
			map.put("visaFee", moneyAmountService.getMoney(map.get("payPriceSerialNum").toString()));
			
			if(!StringUtils.isBlank(errMsg)){
				map.put("errMsg", errMsg);
				errList.add(map);
			}else{
				map.put("curDate", curDate);
				map.put("borrowPerson", personName);
				rightList.add(map);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rightList", rightList);
		result.put("errList", errList);
		return result;
	
	}
	

	/**
	 * xinwei.wang modified 2015-11-19 
	 * 校验activiti批量借款
	 * @author yue.wang
	 * @param visaIds 签证id 以','分割
	 * @param travellerIds 游客id 以','分割
	 * @return map
	 */
	public Map<String, Object> checkBatchJkHqx4activiti(String visaIds,
			String travelerIds) {
		// TODO Auto-generated method stub

		String hql  = " select visa.id as visaId , t.id as tid , t.name as tname  ,t.passportStatus as passportStatus ,t.passportCode as passportCode," +
				"  t.payPriceSerialNum as payPriceSerialNum ,  t.accounted_money as accounted_money ,t.orderId as orderId  " +
				" from visa  visa , traveler t where t.id  = visa.traveler_id and visa.id in ("+visaIds+")";
		List<Map<String, Object>> resultList = visaDao.findBySql(hql,Map.class);
		List<Map<String,Object>> errList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
		String curDate = DateUtils.getDate();
		String personName = UserUtils.getUser().getName();
		for(Map<String,Object> map:resultList){
			String errMsg = "";
			
			boolean isSubmit = false;
			List<BatchTravelerRelation> batchTravelerRelationList = batchTravelerRelationDao.findBatchNo((long)(Integer)map.get("tid"), 1);
			if (batchTravelerRelationList != null && batchTravelerRelationList.size() > 0) {
				for(BatchTravelerRelation list : batchTravelerRelationList){
					if("1".equals(list.getIsSubmit())){
						isSubmit=true;
						break;
					}
				}
			}
			
			if(null != map.get("accounted_money") &&  !"".equals(map.get("accounted_money").toString()) && 
			   null == getChajia(map.get("payPriceSerialNum").toString(), map.get("accounted_money").toString())){  //1.判断达账状态
	    			map.put("jiekuanStatus", "已还");//借款状态(1:未借2:已借3:已还)
	    			errMsg= "已达账";
	    	}else if(isSubmit){  //2.判断是否已经保存到要提交的批次
	    		map.put("jiekuanStatus", "已保存"); //批次里已经保存还未提交
    			errMsg= "借款状态不符";
	    	}else{   //3.判断是否已提交 借款申请  及  借款申请的相关状况 
	    	
	    		//String borrowMoneyandCheckStatus = getTravelerBorrowedMoney4Activiti( map.get("orderId").toString(),(long)(Integer)map.get("tid"));
	    		
	    		//----------------综合考虑 新旧审核时状态的显示 ----------------- 
	    		
	    		String borrowMoneyandCheckStatus = getTravelerBorrowedMoney( map.get("orderId").toString(),(long)(Integer)map.get("tid"));
				if(null==borrowMoneyandCheckStatus){
					borrowMoneyandCheckStatus =getTravelerBorrowedMoney4Activiti( map.get("orderId").toString(),(long)(Integer)map.get("tid"));//新审核
				}
	    		
		    	if (null != borrowMoneyandCheckStatus) {
		    		//借款
		    		String jiekuanStatus   = borrowMoneyandCheckStatus.split(",")[1];
		    		//2 表示审核成功 设置已 未借款	
		    		if("2".equals(jiekuanStatus)){
		    		   //借款状态(1:未借2:已借3:已还)
		    			map.put("jiekuanStatus", "已借");
		    			errMsg= "借款状态不符";
		    		}else if("1".equals(jiekuanStatus)){
		    			//设置为 未借款	虽然是未借款的状态，但是要算作是不符合条件的游客， 不符合的原因 固定显示：借款审核中
		    			map.put("jiekuanStatus", "审批中");
		    			errMsg= "借款审批中";
		    		}else if("3".equals(jiekuanStatus)){
		    			map.put("jiekuanStatus", "已还");
		    			errMsg= "已达账";
		    		}else if("0".equals(jiekuanStatus)) {
						map.put("jiekuanStatus", "审批驳回");
//						errMsg= "审批驳回";
					}
				}else{
		    		//设置为 未借款	
					map.put("jiekuanStatus", "未借");
				}
	    	}
		    	
			map.put("visaFee", moneyAmountService.getMoney(map.get("payPriceSerialNum").toString()));
			
			if(!StringUtils.isBlank(errMsg)){
				map.put("errMsg", errMsg);
				errList.add(map);
			}else{
				map.put("curDate", curDate);
				map.put("borrowPerson", personName);
				rightList.add(map);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rightList", rightList);
		result.put("errList", errList);
		return result;
	
	}
	
	/**
	 * 校验批量还收据数据
	 * @author yue.wang
	 * @param visaIds 签证id 以','分割
	 * @param travellerIds 游客id 以','分割
	 * @return map
	 */
	public Map<String, Object> checkBatchHsjHqx(String visaIds, String travellerIds) {
		String hql  = " select visa.id as visaId , t.id as tid , t.name as tname  ,t.passportStatus as passportStatus ,t.passportCode as passportCode," +
				"  t.payPriceSerialNum as payPriceSerialNum ,  t.accounted_money as accounted_money,t.orderId as orderId  " +
				" from visa  visa , traveler t where t.id  = visa.traveler_id and visa.id in ("+visaIds+")";
		List<Map<String, Object>> resultList = visaDao.findBySql(hql,Map.class);
		List<Map<String,Object>> errList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
		String curDate = DateUtils.getDate();
		String personName = UserUtils.getUser().getName();
		for(Map<String,Object> map:resultList){
			String jiekuanStatus = "";
			String jiekuanJe = "";
			
			boolean isSubmit = false;
			List<BatchTravelerRelation> batchTravelerRelationList = batchTravelerRelationDao.findBatchNo((long)(Integer)map.get("tid"), 2);
			if (batchTravelerRelationList != null && batchTravelerRelationList.size() > 0) {
				for(BatchTravelerRelation list : batchTravelerRelationList){
					if("1".equals(list.getIsSubmit())){
						isSubmit=true;
						break;
					}
				}
			}
	    	
    		String returnReceiptandCheckStatus = getTravelerBorrowedMoney(map.get("orderId").toString(),(long)(Integer)map.get("tid"));
    		List<Review> listReview = reviewDao.findReviewActive(6,4,map.get("orderId").toString(),(long)(Integer)map.get("tid"));
    		if(isSubmit){
	    		map.put("jiekuanStatus", "已保存");
	    	}else if (null != returnReceiptandCheckStatus) {
	    		//借款
	    		jiekuanStatus = returnReceiptandCheckStatus.split(",")[1];
	    		//2 表示审核成功 设置已 未借款	
	    		if("2".equals(jiekuanStatus)){
	    			if(listReview!=null && listReview.size()>0){
	    				map.put("jiekuanStatus", "已借");
	    			} else {
			    		//借款状态(1:未借2:已借3:已还)
		    			map.put("jiekuanStatus", "已借");
	    			}
	    		}else if("1".equals(jiekuanStatus)){
	    			//设置为 借款审批中	
	    			map.put("jiekuanStatus", "审批中");
	    		}else if("3".equals(jiekuanStatus)){
	    			//设置为 已还	
	    			map.put("jiekuanStatus", "已还");
	    		}
    			jiekuanJe = returnReceiptandCheckStatus.split(",")[0];
    			map.put("jiekuanJe", jiekuanJe);
			}else{
	    		//设置为 未借款	
				jiekuanStatus="1";
				map.put("jiekuanStatus", "未借");
			}
			
			map.put("visaFee", moneyAmountService.getMoney(map.get("payPriceSerialNum").toString()));
			
			if(isSubmit){
				map.put("errMsg", "已保存还收据申请");
				errList.add(map);
	    	}else if(jiekuanStatus.equals("2") && listReview!=null && listReview.size()>0){
				map.put("errMsg", "已提交还收据申请");
				errList.add(map);
			} else if(jiekuanStatus.equals("1") && null != returnReceiptandCheckStatus){
				map.put("errMsg", "借款审批中");
				errList.add(map);
			} else if(jiekuanStatus.equals("1") && null == returnReceiptandCheckStatus){
				map.put("errMsg", "尚无借款");
				errList.add(map);
			}else if(jiekuanStatus.equals("3")){
				map.put("errMsg", "还收据审批中");
				errList.add(map);
			}else{
				map.put("curDate", curDate);
				map.put("returnReceiptPerson", personName);
				rightList.add(map);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rightList", rightList);
		result.put("errList", errList);
		return result;
	}
	
	
	/**
	 * @Description: 获取符合还签证收据条件的游客列表
	 * @author xinwei.wang
	 * @date 2015年12月3日下午2:21:47
	 * @param visaIds
	 * @param travellerIds
	 * @return    
	 * @throws
	 */
	public Map<String, Object> checkBatchHsjHqx4activiti(String visaIds, String travellerIds) {
		String hql  = " select visa.id as visaId , t.id as tid , t.name as tname  ,t.passportStatus as passportStatus ,t.passportCode as passportCode," +
				"  t.payPriceSerialNum as payPriceSerialNum ,  t.accounted_money as accounted_money,t.orderId as orderId  " +
				" from visa  visa , traveler t where t.id  = visa.traveler_id and visa.id in ("+visaIds+")";
		List<Map<String, Object>> resultList = visaDao.findBySql(hql,Map.class);
		List<Map<String,Object>> errList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
		String curDate = DateUtils.getDate();
		String personName = UserUtils.getUser().getName();
		for(Map<String,Object> map:resultList){
			String jiekuanStatus = "";
			String jiekuanJe = "";
			
			boolean isSubmit = false;
			List<BatchTravelerRelation> batchTravelerRelationList = batchTravelerRelationDao.findBatchNo((long)(Integer)map.get("tid"), 2);
			if (batchTravelerRelationList != null && batchTravelerRelationList.size() > 0) {
				for(BatchTravelerRelation list : batchTravelerRelationList){
					if("1".equals(list.getIsSubmit())){
						isSubmit=true;
						break;
					}
				}
			}
	    	
			
			//----------------综合处理新旧审核的  借款状态问题----------------- 
			String returnReceiptandCheckStatus = getTravelerBorrowedMoney(map.get("orderId").toString(),(long)(Integer)map.get("tid"));
			if(null==returnReceiptandCheckStatus){
				returnReceiptandCheckStatus = getTravelerBorrowedMoney4Activiti(map.get("orderId").toString(),(long)(Integer)map.get("tid"));	
			}
    		
    		//List<Review> listReview = reviewDao.findReviewActive(6,4,map.get("orderId").toString(),(long)(Integer)map.get("tid"));
    		String returnReceiptStatus = getTravelerRetrunReceipt4Activiti(map.get("orderId").toString(),(long)(Integer)map.get("tid"));
    		if(isSubmit){
	    		map.put("jiekuanStatus", "已借");
	    	}else if (null != returnReceiptandCheckStatus) {
	    		//借款
	    		jiekuanStatus = returnReceiptandCheckStatus.split(",")[1];
	    		//2 表示审核成功 设置已 未借款	
	    		if("2".equals(jiekuanStatus)){
	    			/*if(listReview!=null && listReview.size()>0){
	    				map.put("jiekuanStatus", "已借");
	    			} else {
			    		//借款状态(1:未借2:已借3:已还)
		    			map.put("jiekuanStatus", "已借");
	    			}*/
	    			
	    			//新审批逻辑
	    			if(returnReceiptStatus!=null){
	    				map.put("jiekuanStatus", "已借");
	    			} else {
			    		//借款状态(1:未借2:已借3:已还)
		    			map.put("jiekuanStatus", "已借");
	    			}
	    		}else if("1".equals(jiekuanStatus)){
	    			//设置为 借款审批中	
	    			map.put("jiekuanStatus", "审批中");
	    		}else if("3".equals(jiekuanStatus)){
	    			//设置为 已还	
	    			map.put("jiekuanStatus", "已还");
	    		}
    			jiekuanJe = returnReceiptandCheckStatus.split(",")[0];
    			map.put("jiekuanJe", jiekuanJe);
			}else{
	    		//设置为 未借款	
				jiekuanStatus="1";
				map.put("jiekuanStatus", "未借");
			}
			
			map.put("visaFee", moneyAmountService.getMoney(map.get("payPriceSerialNum").toString()));
			
			if(isSubmit){
				map.put("errMsg", "已保存还收据申请");
				errList.add(map);
	    	}else if(jiekuanStatus.equals("2") && returnReceiptStatus!=null){
	    		/*map.put("errMsg", "已提交还收据申请");
				errList.add(map);*/
	    		//新审批逻辑
	    		if ("1".equals(returnReceiptStatus.split(",")[1])) {
	    			map.put("errMsg", "已提交还收据申请");
					errList.add(map);
				}else if("2".equals(returnReceiptStatus.split(",")[1])){
					map.put("errMsg", "已还");
					errList.add(map);
				}
			} else if(jiekuanStatus.equals("1") && null != returnReceiptandCheckStatus){
				map.put("errMsg", "借款审批中");
				errList.add(map);
			} else if(jiekuanStatus.equals("1") && null == returnReceiptandCheckStatus){
				map.put("errMsg", "尚无借款");
				errList.add(map);
			}else if(jiekuanStatus.equals("3")){//新审核没有该状态
				map.put("errMsg", "还收据审批中");
				errList.add(map);
			}else{
				map.put("curDate", curDate);
				map.put("returnReceiptPerson", personName);
				rightList.add(map);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rightList", rightList);
		result.put("errList", errList);
		return result;
	}
	
	
	
	
	/**
	 * 批量借款-待提交/已提交 分页查询
	 * @return
	 */
	public Page<Map<String, Object>> batchBorrowMoneyHqxList(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> condition){
		String saveOrSubmit = request.getParameter("saveOrSubmit");
		String batchNo = condition.get("batchNo");
		String txnPerson = condition.get("txnPerson");
		String createDateStart = condition.get("createTimeStart");
		String createDateEnd = condition.get("createTimeEnt");
		String travellerName = condition.get("travellerName");
		String signTimeStart = condition.get("signTimeStart");//实际约签时间-开始时间
		String signTimeEnd = condition.get("signTimeEnd");    //实际约签时间-结束时间
		String orderBy = condition.get("orderBy");
		
		//----------------综合处理新旧审核的  借款状态问题-----------------

		StringBuffer sb = new StringBuffer("SELECT vb.batch_no batchNo,vb.is_new isNew,vb.create_user_name txnPerson,");
		if (saveOrSubmit == null || "save".equals(saveOrSubmit)) {
			//sb.append(" bt.save_time createDate,");
			//---0064需求,游客加入批次-s--//
			sb.append(" vb.create_time createDate, ");
			//---0064需求,游客加入批次-e--//
		} else if("submit".equals(saveOrSubmit)) {
			sb.append(" bt.submit_time createDate,");
		}
		//---0064需求,游客加入批次-s-更新时间--//
		sb.append(" vb.update_time updateDate, ");
		//---0064需求,游客加入批次-e--//
		//--0064需求--批量添加游客到批次--djw--start-
		sb.append(" vb.refund_date refundDate,");
		//--0064需求--批量添加游客到批次--djw--over-
		sb.append(" vb.batch_total_money batchTotalMoney,vb.review_status reviewStatus,GROUP_CONCAT(bt.visa_id) visaIds,GROUP_CONCAT(bt.traveler_id) travelerIds,GROUP_CONCAT(bt.order_id) orderIds,rd.review_id reviewId FROM visa_flow_batch_opration vb");
		sb.append(" LEFT JOIN batch_traveler_relation bt ON bt.batch_uuid = vb.uuid ");
		sb.append(" LEFT JOIN review_detail rd on rd.myValue=vb.batch_no and rd.myKey='visaBorrowMoneyBatchNo' ");
		sb.append(" LEFT JOIN sys_user su ON su.id=vb.create_user_id ");
		//关联visa表 查询实际约签时间
		sb.append(" LEFT JOIN visa v ON v.traveler_id=bt.traveler_id ");
		sb.append(" WHERE bt.business_type=1 and vb.busyness_type=2 and su.companyId="+UserUtils.getUser().getCompany().getId());
		if(!StringUtils.isBlank(batchNo)){
			sb.append(" and vb.batch_no = '"+batchNo+"' ");
		}
		if(!StringUtils.isBlank(txnPerson)){
			sb.append(" and vb.create_user_name = '"+txnPerson+"' ");
		}
		if (saveOrSubmit == null || "save".equals(saveOrSubmit)) {
			//实际约签时间-开始时间
			if(StringUtils.isNotBlank(signTimeStart)){
				sb.append(" and v.contract >='"+signTimeStart+":00'");
			}
			//实际约签时间-结束时间
			if(StringUtils.isNotBlank(signTimeEnd)){
				sb.append(" and v.contract <='"+signTimeEnd+":59'");
			}
			if(!StringUtils.isBlank(createDateStart)){
				sb.append(" and bt.save_time >='"+createDateStart+" 00:00:00.0'");
			}
			if(!StringUtils.isBlank(createDateEnd)){
				sb.append(" and bt.save_time <='"+createDateEnd+" 23:59:59.0'");
			}
			sb.append(" and vb.is_submit = '1'");
		} else if("submit".equals(saveOrSubmit)) {
			//实际约签时间-开始时间
			if(StringUtils.isNotBlank(signTimeStart)){
				sb.append(" and v.contract >='"+signTimeStart+":00'");
			}
			//实际约签时间-结束时间
			if(StringUtils.isNotBlank(signTimeEnd)){
				sb.append(" and v.contract <='"+signTimeEnd+":59'");
			}
			if(!StringUtils.isBlank(createDateStart)){
				sb.append(" and bt.submit_time >='"+createDateStart+" 00:00:00.0'");
			}
			if(!StringUtils.isBlank(createDateEnd)){
				sb.append(" and bt.submit_time <='"+createDateEnd+" 23:59:59.0'");
			}
			sb.append(" and vb.is_submit = '2'");
		}
		if(!StringUtils.isBlank(travellerName)){
			sb.append(" and bt.traveler_name = '"+travellerName+"' ");
		}
		sb.append(" group by vb.batch_no");

		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" u.createDate DESC");
		} else {
			page.setOrderBy(orderBy);
		}
     	String sql =" SELECT batchNo,refundDate,txnPerson,createDate,updateDate,batchTotalMoney,reviewStatus,visaIds,travelerIds,orderIds,isnew,reviewId from ( "+sb.toString()+" ) u";
		return visaFlowBatchOprationDao.findBySql(page,sql,Map.class);
	}

	/**
	 * ----------------综合处理新旧审核的  借款状态问题-----------------
	 * 
	 * 批量还收据-待提交/已提交 分页查询
	 * @return
	 */
	public Page<Map<String, Object>> batchReturnReceiptandHqxList(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> condition){
		String saveOrSubmit = request.getParameter("saveOrSubmit");
		String batchNo = condition.get("batchNo");
		String txnPerson = condition.get("txnPerson");
		String createDateStart = condition.get("createTimeStart");
		String createDateEnd = condition.get("createTimeEnt");
		String travellerName = condition.get("travellerName");
		String signTimeStart = condition.get("signTimeStart");//实际约签时间-开始时间
		String signTimeEnd = condition.get("signTimeEnd");    //实际约签时间-结束时间
		String orderBy = condition.get("orderBy");

		StringBuffer sb = new StringBuffer("SELECT vb.batch_no batchNo,vb.is_new isNew,vb.create_user_name txnPerson,");
		if (saveOrSubmit == null || "save".equals(saveOrSubmit)) {
			sb.append(" bt.save_time createDate,");
		} else if("submit".equals(saveOrSubmit)) {
			sb.append(" bt.submit_time createDate,");
		}
		sb.append(" vb.batch_total_money batchTotalMoney,vb.review_status reviewStatus,GROUP_CONCAT(bt.visa_id) visaIds,GROUP_CONCAT(bt.traveler_id) travelerIds,GROUP_CONCAT(bt.order_id) orderIds,rd.review_id reviewId FROM visa_flow_batch_opration vb");
		sb.append(" LEFT JOIN batch_traveler_relation bt ON bt.batch_uuid = vb.uuid ");
		sb.append(" LEFT JOIN review_detail rd on rd.myValue=vb.batch_no and rd.myKey='visaReturnReceiptBatchNo' ");
		sb.append(" LEFT JOIN sys_user su ON su.id=vb.create_user_id ");
		//关联visa表 查询实际约签时间
		sb.append(" LEFT JOIN visa v ON v.traveler_id=bt.traveler_id ");
		sb.append(" WHERE bt.business_type=2 and vb.busyness_type=1 and su.companyId="+UserUtils.getUser().getCompany().getId());
		if(!StringUtils.isBlank(batchNo)){
			sb.append(" and vb.batch_no = '"+batchNo+"' ");
		}
		if(!StringUtils.isBlank(txnPerson)){
			sb.append(" and vb.create_user_name = '"+txnPerson+"' ");
		}
		if (saveOrSubmit == null || "save".equals(saveOrSubmit)) {
			//实际约签时间-开始时间
			if(StringUtils.isNotBlank(signTimeStart)){
				sb.append(" and v.contract >='"+signTimeStart+":00'");
			}
			//实际约签时间-结束时间
			if(StringUtils.isNotBlank(signTimeEnd)){
				sb.append(" and v.contract <='"+signTimeEnd+":59'");
			}
			if(!StringUtils.isBlank(createDateStart)){
				sb.append(" and bt.save_time >='"+createDateStart+" 00:00:00.0'");
			}
			if(!StringUtils.isBlank(createDateEnd)){
				sb.append(" and bt.save_time <='"+createDateEnd+" 23:59:59.0'");
			}
			sb.append(" and vb.is_submit = '1'");
		} else if("submit".equals(saveOrSubmit)) {
			//实际约签时间-开始时间
			if(StringUtils.isNotBlank(signTimeStart)){
				sb.append(" and v.contract >='"+signTimeStart+":00'");
			}
			//实际约签时间-结束时间
			if(StringUtils.isNotBlank(signTimeEnd)){
				sb.append(" and v.contract <='"+signTimeEnd+":59'");
			}
			if(!StringUtils.isBlank(createDateStart)){
				sb.append(" and bt.submit_time >='"+createDateStart+" 00:00:00.0'");
			}
			if(!StringUtils.isBlank(createDateEnd)){
				sb.append(" and bt.submit_time <='"+createDateEnd+" 23:59:59.0'");
			}
			sb.append(" and vb.is_submit = '2'");
		}
		if(!StringUtils.isBlank(travellerName)){
			sb.append(" and bt.traveler_name = '"+travellerName+"' ");
		}
		sb.append(" group by vb.batch_no");

		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" u.createDate DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		String sql =" select  batchNo,txnPerson,createDate,batchTotalMoney,reviewStatus,visaIds,travelerIds,orderIds,isnew,reviewId from ( "+sb.toString()+" ) u";
		return visaFlowBatchOprationDao.findBySql(page,sql,Map.class);
	}
	
	public Page<Map<String, Object>> batchReturnPassportList(
			HttpServletRequest request, HttpServletResponse response,
			Map<String, String> condition){
		String  batchNo         = condition.get("batchNo");
		String  txnPerson       = condition.get("txnPerson");
		String  createTimeStart = condition.get("createTimeStart");
		String  createTimeEnt   = condition.get("createTimeEnt");
		String  travellerName   = condition.get("travellerName");
		String orderBy = condition.get("orderBy");
		
		StringBuffer  sb  = new StringBuffer("select record.batch_no as batchNo , record.create_user_name as createUserName, record.create_time  as createTime,GROUP_CONCAT(visa.id) as visaIds ") ;
		sb.append(" from batch_record record , traveler t ,visa visa ,sys_user user ");
		sb.append(" where visa.traveler_id=t.id and  record.batch_no = t.borrow_passport_batch_no and record.type='1' and record.create_user_id = user.id  and  record.batch_no !='单借'  ");
		sb.append(" and  user.companyId ="+UserUtils.getUser().getCompany().getId());
		if(!StringUtils.isBlank(batchNo)){
			sb.append(" and  record.batch_no ='"+batchNo+"'");
		}
		if(!StringUtils.isBlank(txnPerson)){
			sb.append(" and  record.create_user_name ='"+txnPerson+"'");
		}
		if(!StringUtils.isBlank(createTimeStart)){
			sb.append(" and  record.create_time >='"+createTimeStart+"'");
		}
		
		if(!StringUtils.isBlank(createTimeEnt)){
			sb.append(" and  record.create_time <='"+createTimeEnt+" 23:59:59.0'");
		}
		if(!StringUtils.isBlank(travellerName)){
			sb.append(" and  t.name ='"+travellerName+"'");
		}
		
		sb.append(" group by record.batch_no, record.create_user_name,record.create_time");
	
		sb.append(" UNION  SELECT  '单借'  AS batchNo,visa.passport_operator AS createUserName, visa.passport_operate_time AS createTime,GROUP_CONCAT(visa.id) AS visaIds   "+
				"  FROM   traveler traveler  INNER JOIN   visa  visa  ON traveler.id=visa.traveler_id  INNER JOIN visa_order vo ON vo.id= traveler.orderId INNER JOIN visa_products vp ON vo.visa_product_id = vp.id   "+ 
				"  WHERE traveler.passportStatus=1 AND traveler.borrow_passport_batch_no IS NULL  ");
		sb.append("  AND vp.proCompanyId="+UserUtils.getUser().getCompany().getId());
		if(!StringUtils.isBlank(travellerName)){
			sb.append(" and  traveler.name ='"+travellerName+"'");
		}
		if(!StringUtils.isBlank(txnPerson)){
			sb.append(" and  visa.passport_operator ='"+txnPerson+"'");
		}
		sb.append("  having  visaIds is not null ");
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" u.createTime DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		//System.out.println(sb.toString());
		String sql =" select batchNo, createUserName, createTime, visaIds from ( "+sb.toString()+" ) u";	
		return reviewDao.findBySql(page,sql,Map.class);
	}
	
	/**
	 * 批量借护照-待提交   分页查询
	 * @param request,response,condition
	 * @return
	 */
	public Page<Map<String, Object>> borrowPassportWait4Submit(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> condition){
		String  batchNo         = condition.get("batchNo");
		String  txnPerson       = condition.get("txnPerson");
		String  createTimeStart = condition.get("createTimeStart");
		String  createTimeEnt   = condition.get("createTimeEnt");
		String  travellerName   = condition.get("travellerName");
		String  signTimeStart   = condition.get("signTimeStart");  //实际约签时间-开始时间
		String  signTimeEnd     = condition.get("signTimeEnd");    //实际约签时间-结束时间
		String orderBy = condition.get("orderBy");
		StringBuffer  sb  = new StringBuffer("select record.batch_no as batchNo , record.create_user_name as createUserName, record.create_time  as createTime,GROUP_CONCAT(visa.id) as visaIds ");
		sb.append(" FROM batch_traveler_relation btr,batch_record record,traveler t,visa visa,sys_user USER ");
		sb.append(" WHERE visa.traveler_id = t.id AND record.batch_no = btr.batch_record_no AND btr.traveler_id=t.id AND record.type = '1' AND record.passport_status=0 AND record.is_submit=0 AND btr.business_type=3 AND record.create_user_id = USER.id AND record.batch_no != '单借' ");
		sb.append(" and  USER.companyId ="+UserUtils.getUser().getCompany().getId());
		if(!StringUtils.isBlank(batchNo)){
			sb.append(" and  record.batch_no ='"+batchNo+"'");
		}
		if(!StringUtils.isBlank(txnPerson)){
			sb.append(" and  record.create_user_name like '%"+txnPerson.trim()+"%'");
		}
		if(!StringUtils.isBlank(createTimeStart)){
			sb.append(" and  record.create_time >='"+createTimeStart+"'");
		}
		
		if(!StringUtils.isBlank(createTimeEnt)){
			sb.append(" and  record.create_time <='"+createTimeEnt+" 23:59:59.0'");
		}
		if(!StringUtils.isBlank(travellerName)){
			sb.append(" and  t.name like '%"+travellerName.trim()+"%'");
		}
		//实际约签时间-开始时间
		if(StringUtils.isNotBlank(signTimeStart)){
			sb.append(" and visa.contract >='"+signTimeStart+":00'");
		}
		//实际约签时间-结束时间
		if(StringUtils.isNotBlank(signTimeEnd)){
			sb.append(" and visa.contract <='"+signTimeEnd+":59'");
		}
		sb.append(" group by record.batch_no, record.create_user_name,record.create_time");
		
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" u.createTime DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		String sql =" select batchNo,createUserName,createTime,visaIds from ( "+sb.toString()+" ) u";	
		
		return reviewDao.findBySql(page,sql,Map.class);		
	}
	
	/**
	 * 批量借护照-已提交   分页查询
	 * @param request,response,condition
	 * @return
	 */
	public Page<Map<String, Object>> borrowPassportAlreadySubmit(HttpServletRequest request, HttpServletResponse response,Map<String, String> condition){
		String  batchNo         = condition.get("batchNo");
		String  txnPerson       = condition.get("txnPerson");
		String  createTimeStart = condition.get("createTimeStart");
		String  createTimeEnt   = condition.get("createTimeEnt");
		String  travellerName   = condition.get("travellerName");
		String  signTimeStart   = condition.get("signTimeStart");  //实际约签时间-开始时间
		String  signTimeEnd     = condition.get("signTimeEnd");    //实际约签时间-结束时间
		String orderBy = condition.get("orderBy");
		StringBuffer  sb  = new StringBuffer("select record.batch_no as batchNo , record.create_user_name as createUserName, record.create_time  as createTime, record.passport_status as passportStatus, GROUP_CONCAT(visa.id) as visaIds ");
		sb.append(" FROM batch_traveler_relation btr,batch_record record,traveler t,visa visa,sys_user USER ");
		sb.append(" WHERE visa.traveler_id = t.id AND record.batch_no = btr.batch_record_no AND btr.traveler_id=t.id AND record.type = '1' AND (record.passport_status = 1  or record.passport_status = 3)  AND record.is_submit=1 AND btr.business_type=3 AND record.create_user_id = USER.id AND record.batch_no != '单借' ");
		sb.append(" and  USER.companyId ="+UserUtils.getUser().getCompany().getId());
		if(!StringUtils.isBlank(batchNo)){
			sb.append(" and  record.batch_no ='"+batchNo+"'");
		}
		if(!StringUtils.isBlank(txnPerson)){
			sb.append(" and  record.create_user_name like '%"+txnPerson.trim()+"%'");
		}
		if(!StringUtils.isBlank(createTimeStart)){
			sb.append(" and  record.create_time >='"+createTimeStart+"'");
		}
		
		if(!StringUtils.isBlank(createTimeEnt)){
			sb.append(" and  record.create_time <='"+createTimeEnt+" 23:59:59.0'");
		}
		if(!StringUtils.isBlank(travellerName)){
			sb.append(" and  t.name like '%"+travellerName.trim()+"%'");
		}
		//实际约签时间-开始时间
		if(StringUtils.isNotBlank(signTimeStart)){
			sb.append(" and visa.contract >='"+signTimeStart+":00'");
		}
		//实际约签时间-结束时间
		if(StringUtils.isNotBlank(signTimeEnd)){
			sb.append(" and visa.contract <='"+signTimeEnd+":59'");
		}
		sb.append(" group by record.batch_no, record.create_user_name,record.create_time,record.passport_status ");
		
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" u.createTime DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		String sql =" select batchNo,createUserName, createTime,passportStatus,visaIds from ( "+sb.toString()+" ) u";	
		
		return reviewDao.findBySql(page,sql,Map.class);	
	}
	
	/**
	 * 批量还护照-待提交   分页查询
	 * @param request,response,condition
	 * @return
	 */
	public Page<Map<String, Object>> returnPassportWait4Submit(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> condition){
		String  batchNo         = condition.get("batchNo");
		String  txnPerson       = condition.get("txnPerson");
		String  createTimeStart = condition.get("createTimeStart");
		String  createTimeEnt   = condition.get("createTimeEnt");
		String  travellerName   = condition.get("travellerName");
		String  signTimeStart   = condition.get("signTimeStart");  //实际约签时间-开始时间
		String  signTimeEnd     = condition.get("signTimeEnd");    //实际约签时间-结束时间
		String orderBy = condition.get("orderBy");
		StringBuffer  sb  = new StringBuffer("select record.batch_no as batchNo , record.create_user_name as createUserName, record.create_time  as createTime,GROUP_CONCAT(visa.id) as visaIds ");
		sb.append(" FROM batch_traveler_relation btr,batch_record record,traveler t,visa visa,sys_user USER ");
		sb.append(" WHERE visa.traveler_id = t.id AND record.batch_no = btr.batch_record_no AND btr.traveler_id=t.id AND record.type = '1' AND record.passport_status=2 AND record.is_submit=0 AND btr.business_type=4 AND record.create_user_id = USER.id AND record.batch_no != '单借' ");
		sb.append(" and  USER.companyId ="+UserUtils.getUser().getCompany().getId());
		if(!StringUtils.isBlank(batchNo)){
			sb.append(" and  record.batch_no ='"+batchNo+"'");
		}
		if(!StringUtils.isBlank(txnPerson)){
			sb.append(" and  record.create_user_name like '%"+txnPerson.trim()+"%'");
		}
		if(!StringUtils.isBlank(createTimeStart)){
			sb.append(" and  record.create_time >='"+createTimeStart+"'");
		}
		
		if(!StringUtils.isBlank(createTimeEnt)){
			sb.append(" and  record.create_time <='"+createTimeEnt+" 23:59:59.0'");
		}
		if(!StringUtils.isBlank(travellerName)){
			sb.append(" and  t.name like '%"+travellerName.trim()+"%'");
		}
		//实际约签时间-开始时间
		if(StringUtils.isNotBlank(signTimeStart)){
			sb.append(" and visa.contract >='"+signTimeStart+":00'");
		}
		//实际约签时间-结束时间
		if(StringUtils.isNotBlank(signTimeEnd)){
			sb.append(" and visa.contract <='"+signTimeEnd+":59'");
		}
		sb.append(" group by record.batch_no, record.create_user_name,record.create_time");
		
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" u.createTime DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		String sql =" select batchNo,createUserName, createTime,visaIds from ( "+sb.toString()+" ) u";	
		
		return reviewDao.findBySql(page,sql,Map.class);		
	}
	/**
	 * 批量还护照-已提交   分页查询
	 * @param request,response,condition
	 * @return
	 */
	public Page<Map<String, Object>> returnPassportAlreadySubmit(HttpServletRequest request, HttpServletResponse response,Map<String, String> condition){
		String  batchNo         = condition.get("batchNo");
		String  txnPerson       = condition.get("txnPerson");
		String  createTimeStart = condition.get("createTimeStart");
		String  createTimeEnt   = condition.get("createTimeEnt");
		String  travellerName   = condition.get("travellerName");
		String  signTimeStart   = condition.get("signTimeStart");  //实际约签时间-开始时间
		String  signTimeEnd     = condition.get("signTimeEnd");    //实际约签时间-结束时间
		String orderBy = condition.get("orderBy");
		StringBuffer  sb  = new StringBuffer("select record.batch_no as batchNo , record.create_user_name as createUserName, record.create_time  as createTime,GROUP_CONCAT(visa.id) as visaIds ");
		sb.append(" FROM batch_traveler_relation btr,batch_record record,traveler t,visa visa,sys_user USER ");
		sb.append(" WHERE visa.traveler_id = t.id AND record.batch_no = btr.batch_record_no AND btr.traveler_id=t.id AND record.type = '1' AND record.passport_status=3 AND record.is_submit=1 AND btr.business_type=4 AND record.create_user_id = USER.id AND record.batch_no != '单借' ");
		sb.append(" and  USER.companyId ="+UserUtils.getUser().getCompany().getId());
		if(!StringUtils.isBlank(batchNo)){
			sb.append(" and  record.batch_no ='"+batchNo+"'");
		}
		if(!StringUtils.isBlank(txnPerson)){
			sb.append(" and  record.create_user_name like '%"+txnPerson.trim()+"%'");
		}
		if(!StringUtils.isBlank(createTimeStart)){
			sb.append(" and  record.create_time >='"+createTimeStart+"'");
		}
		
		if(!StringUtils.isBlank(createTimeEnt)){
			sb.append(" and  record.create_time <='"+createTimeEnt+" 23:59:59.0'");
		}
		if(!StringUtils.isBlank(travellerName)){
			sb.append(" and  t.name like '%"+travellerName.trim()+"%'");
		}
		//实际约签时间-开始时间
		if(StringUtils.isNotBlank(signTimeStart)){
			sb.append(" and visa.contract >='"+signTimeStart+":00'");
		}
		//实际约签时间-结束时间
		if(StringUtils.isNotBlank(signTimeEnd)){
			sb.append(" and visa.contract <='"+signTimeEnd+":59'");
		}
		sb.append(" group by record.batch_no, record.create_user_name,record.create_time");
		
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" u.createTime DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		String sql =" select batchNo, createUserName, createTime, visaIds from ( "+sb.toString()+" ) u";	
		
		return reviewDao.findBySql(page,sql,Map.class);	
	}
	
	
	/**
	 * 查找游客信息
	 * @param conditon
	 * @return
	 */
	public List<Map<String, Object>> findBatchReturnPassportTraveler(Map<String,String> conditon){
		Map<Integer ,String> passPoartStatusMap = new HashMap<Integer ,String>();
			passPoartStatusMap.put(1,"借出");
			passPoartStatusMap.put(2,"销售已领取");
			//passPoartStatusMap.put(3,"未签收");
			//passPoartStatusMap.put(4,"已签收");
			passPoartStatusMap.put(4,"已还");
			passPoartStatusMap.put(5,"已取出");
			passPoartStatusMap.put(6,"未取出");
			//passPoartStatusMap.put(7,"走团");
			passPoartStatusMap.put(8,"计调领取");
		List<Dict> visaStatusList = findDictByType("visa_status");	
		 String visaIds = conditon.get("visaIds");
		String hql  = " select t.borrow_passport_batch_no as batchNo," +
				" t.name as tname, " +
				" t.id as tid , " +
				" t.passportCode as passportCode," +
				" t.passportType as passportType," +
				" t.passportStatus as passportStatus ," +
				" t.orderId as orderId, "+ 
				" sc.countryName_cn as visaCountry,  "
				+" v.visa_stauts as visaStatus,  "
				//7实际出团时间
				+" v.start_out as startOut, "
				//8实际签约时间
				+" v.contract  as contract,"
				+" vi.id  as visaId,"
				+" vi.passport_operator  as passportoperator,"
				+" v.passport_operate_time  as passportoperatetime,"
				//团号
				+" vo.group_code as orderTuanhaoFromVOTable, "//C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s//
				+" vp.groupCode as orderTuanhao "  //对应需求号  C460V3 签证订单团号统一取  订单所关联的产品团号
				+" from traveler t " 
				+" LEFT JOIN visa_order vo on vo.id = t.orderId  "
				+" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id "
				+" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId "
				+" LEFT JOIN sys_user su on su.id = vo.create_by "
				+" LEFT JOIN productorder po on po.id = t.main_order_id" 
				+" LEFT JOIN visa vi on t.id = vi.traveler_id "+ 
				",visa v  "   
				+" where v.traveler_id = t.id and v.id in ("+visaIds+")";
		List<Map<String, Object>> list = visaDao.findBySql(hql,Map.class);
		List<Dict> dictList = findDictByType("new_visa_type");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map:list){
			//签证类别 
			map.put("passportTypeDesc", "");
			if(null != dictList && dictList.size()>0){
    			for(int i=0;i<dictList.size();i++){
    				Dict dict =dictList.get(i);
    				if(String.valueOf(map.get("passportType")).equals(dict.getValue())){
    					map.put("passportTypeDesc", dict.getLabel());
    					break;
    				}
    			}
			}
			//实际出团时间
			if(null != map.get("startOut") && !"".equals(map.get("startOut"))){
				String customDate = DateUtils.formatCustomDate((Date)map.get("startOut"),"yyyy-MM-dd");
				map.put("startOut", customDate);
			}else{
				map.put("startOut", "");
			}
			
			//实际签约时间
			if(null != map.get("contract") && !"".equals(map.get("contract"))){
				String customDate = DateUtils.formatCustomDate((Date)map.get("contract"),"yyyy-MM-dd HH:mm");
				map.put("contract", customDate);
			}	else{
				map.put("contract", "");
			}
			//护照状态
			Object passportStatus = map.get("passportStatus");
			map.put("passportStatusDesc","");
			if(null != passportStatus && !"".equals(passportStatus)){
				map.put("passportStatusDesc", passPoartStatusMap.get(passportStatus)==null?"": passPoartStatusMap.get(passportStatus));
			}	
			//签证状态
			Object visaStatus = map.get("visaStatus"); 
			map.put("visaStatusDesc", "");
			if(null != visaStatus && !"".equals(visaStatus)){
				if("3".equals(String.valueOf(visaStatus))){
					map.put("visaStatusDesc", "通过");
				} else if (!"2".equals(String.valueOf(visaStatus))) {
					for(Dict dict:visaStatusList){
						if(String.valueOf(visaStatus).equals(dict.getValue())){
							map.put("visaStatusDesc", dict.getLabel());
							break;
						}
					}
				}
			}	
			
			//还护照时间
			java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm");
			try{	
				if(map.get("passportoperatetime")!=null&&map.get("passportoperatetime")!=""){
				map.put("passportoperatetime", format1.format(format1.parse(map.get("passportoperatetime").toString())));}
			}catch(Exception e){
				e.printStackTrace();
			}
			resultList.add(map);
		}
			
			
		return resultList;
	}
	
	public List<Map<String, Object>> totalcheckBatchReturnPassport(String totalbatchno) {
		 String hql="";
		if ("'单借'".equals(totalbatchno)){
		 /* hql  = "select GROUP_CONCAT(visa.id) as visaIds   from visa  visa , traveler t where t.id  = visa.traveler_id and t.borrow_passport_batch_no is null "+ 
                       " and t.passportStatus=1  group by visa.id ";*/
			 hql  =  " SELECT  GROUP_CONCAT(visa.id) AS visaIds   "+
		      "  FROM   traveler traveler  INNER JOIN   visa  visa  ON traveler.id=visa.traveler_id  INNER JOIN visa_order vo ON vo.id= traveler.orderId INNER JOIN visa_products vp ON vo.visa_product_id = vp.id   "+ 
		      "  WHERE traveler.passportStatus=1 AND traveler.borrow_passport_batch_no IS NULL  "+ 
		      "  AND vp.proCompanyId="+UserUtils.getUser().getCompany().getId();
		}else {
		 hql  = "select GROUP_CONCAT(visa.id) as visaIds   from visa  visa , traveler t where t.id  = visa.traveler_id and t.borrow_passport_batch_no in ("+totalbatchno+")  group by visa.id";}
		List<Map<String, Object>> list = visaDao.findBySql(hql,Map.class);
		return list;
	} 

	/**
	 * 查找批次记录
	 * @return
	 */
	public Page<Map<String, Object>> batchBorrowMoneyList(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> condition){
		String batchNo = condition.get("batchNo");
		String txnPerson = condition.get("txnPerson");
		String createDateStart = condition.get("createDateStart");
		String createDateEnd = condition.get("createDateEnd");
		String travellerName = condition.get("travellerName");
		String orderBy = condition.get("orderBy");

		StringBuffer sb1 = new StringBuffer("SELECT GROUP_CONCAT(visa.id) visaIds,sum(rd1.myValue) borrowAmount,rd.myValue batchNo,user.name txnPerson,max(re.createDate) createDate FROM review_detail rd");
		sb1.append(" LEFT JOIN (SELECT rd.id,rd.review_id,rd.myKey,rd.myValue FROM review_detail rd WHERE rd.myKey = 'borrowAmount') rd1");
		sb1.append(" ON rd.review_id = rd1.review_id");
		sb1.append(" LEFT JOIN review re ON re.id = rd.review_id");
		sb1.append(" LEFT JOIN traveler t ON re.travelerId = t.id");
		sb1.append(" LEFT JOIN visa visa ON visa.traveler_id = t.id");
		sb1.append(" LEFT JOIN sys_user user ON user.id = re.createBy");
		sb1.append(" WHERE rd.myKey = 'visaBorrowMoneyBatchNo' and re.productType='6' and re.flowType='5' and re.status !='0' and re.status ='2' and user.companyId ='"+UserUtils.getUser().getCompany().getId()+"'");

		if(!StringUtils.isBlank(batchNo)){
			sb1.append(" and rd.myValue like '%"+batchNo+"%'");
		}
		if(!StringUtils.isBlank(txnPerson)){
			sb1.append(" and user.name like '%"+txnPerson+"%'");
		}
		if(!StringUtils.isBlank(createDateStart)){
			sb1.append(" and re.createDate >='"+createDateStart+" 00:00:00.0'");
		}
		if(!StringUtils.isBlank(createDateEnd)){
			sb1.append(" and re.createDate <='"+createDateEnd+" 23:59:59.0'");
		}
		if(!StringUtils.isBlank(travellerName)){
			sb1.append(" and t.name like '%"+travellerName+"%'");
		}
		sb1.append(" group by rd.myValue");

		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" u.createDate DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		String sql =" select visaIds, borrowAmount, batchNo, txnPerson,createDate from ( "+sb1.toString()+" ) u";
		return reviewDao.findBySql(page,sql,Map.class);
	}

	/**
	 * 查找借款游客信息
	 * @param conditon
	 * @return
	 */
	public List<Map<String, Object>> findBatchBorrowMoneyTraveler(Map<String,String> conditon){
		Map<Integer ,String> passPoartStatusMap = new HashMap<Integer ,String>();
			passPoartStatusMap.put(1,"借出");
			passPoartStatusMap.put(2,"销售已领取");
			//passPoartStatusMap.put(3,"未签收");
			//passPoartStatusMap.put(4,"已签收");
			passPoartStatusMap.put(4,"已还");
			passPoartStatusMap.put(5,"已取出");
			passPoartStatusMap.put(6,"未取出");
			//passPoartStatusMap.put(7,"走团");
			passPoartStatusMap.put(8,"计调领取");
		List<Dict> visaStatusList = findDictByType("visa_status");
		 String visaIds = conditon.get("visaIds");
		String hql  = "select t.name tname, " +
				" t.id tid, " +
				" t.passportCode passportCode, " +
				" t.passportType passportType, " +
				" t.passportStatus passportStatus, " +
				" sc.countryName_cn visaCountry, " +
				" v.visa_stauts visaStatus, " +
				//7实际出团时间
				" v.start_out startOut, " +
				//8实际签约时间
				" v.contract contract, " +
				" vi.id visaId, " +
				" re.createBy createBy, " +
				" user.name createByName, " +
				" v.passport_operate_time passportoperatetime" +
				" from traveler t " +
				" LEFT JOIN visa_order vo on vo.id = t.orderId " +
				" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id " +
				" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId " +
				" LEFT JOIN sys_user su on su.id = vo.create_by " +
				" LEFT JOIN productorder po on po.id = t.main_order_id" +
				" LEFT JOIN visa vi on t.id = vi.traveler_id " +
				" LEFT JOIN review re on t.id=re.travelerId and re.productType='6' and re.flowType='5' and re.status !='0' and re.status ='2' " +
				" LEFT JOIN sys_user user on re.createBy=user.id, " +
				" visa v " +
				" where v.traveler_id = t.id and v.id in ("+visaIds+")";
		List<Map<String, Object>> list = visaDao.findBySql(hql,Map.class);
		List<Dict> dictList = findDictByType("new_visa_type");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map:list){
			//签证类别 
			map.put("passportTypeDesc", "");
			if(null != dictList && dictList.size()>0){
    			for(int i=0;i<dictList.size();i++){
    				Dict dict =dictList.get(i);
    				if(String.valueOf(map.get("passportType")).equals(dict.getValue())){
    					map.put("passportTypeDesc", dict.getLabel());
    					break;
    				}
    			}
			}
			//实际出团时间
			if(null != map.get("startOut") && !"".equals(map.get("startOut"))){
				String customDate = DateUtils.formatCustomDate((Date)map.get("startOut"),"yyyy-MM-dd");
				map.put("startOut", customDate);
			}else{
				map.put("startOut", "");
			}
			//实际签约时间
			if(null != map.get("contract") && !"".equals(map.get("contract"))){
				String customDate = DateUtils.formatCustomDate((Date)map.get("contract"),"yyyy-MM-dd");
				map.put("contract", customDate);
			} else {
				map.put("contract", "");
			}
			//护照状态
			Object passportStatus = map.get("passportStatus");
			map.put("passportStatusDesc","");
			if(null != passportStatus && !"".equals(passportStatus)){
				map.put("passportStatusDesc", passPoartStatusMap.get(passportStatus)==null?"": passPoartStatusMap.get(passportStatus));
			}
			//签证状态
			Object visaStatus = map.get("visaStatus"); 
			map.put("visaStatusDesc", "");
			if(null != visaStatus && !"".equals(visaStatus)){
				if("3".equals(String.valueOf(visaStatus))){
					map.put("visaStatusDesc", "通过");
				} else if (!"2".equals(String.valueOf(visaStatus))) {
					for(Dict dict:visaStatusList){
						if(String.valueOf(visaStatus).equals(dict.getValue())){
							map.put("visaStatusDesc", dict.getLabel());
							break;
						}
					}
				}
			}
			//还护照时间
			if(null != map.get("passportoperatetime") && !"".equals(map.get("passportoperatetime"))){
				java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm");
				try{
					map.put("passportoperatetime", dateFormat.format(map.get("passportoperatetime")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				map.put("passportoperatetime", "");
			}
			resultList.add(map);
		}
		return resultList;
	}
	/**
	 * 验证批量退押金游客
	 * @author jiachen
	 * @DateTime 2015年3月19日 下午3:23:45
	 * @return Map<String,Object>
	 */
	public Map<String,Object> checkDepositRefund(String travelerIds) {
		Map<String,Object> resultMap = null;
		List<Object[]> travelerInfoList = null;
		List<Map<String, Object>> rightList = null;
		List<Map<String, Object>> errList = null;
		travelerInfoList = new ArrayList<Object[]>();
		//获得游客信息
		if(StringUtils.isNotBlank(travelerIds)) {
			List<Long> travelerIdsList = new ArrayList<Long>();
			for(String s : travelerIds.split(",")) {
				travelerIdsList.add(StringUtils.toLong(s));
			}
			travelerInfoList = visaDao.findTravelerInfoByIdsToRefund(travelerIdsList);
		}
		//封装游客信息，并把符合与不符合条件的分类
		if(!travelerInfoList.isEmpty()) {
			resultMap = new HashMap<String,Object>();
			rightList = new  ArrayList<Map<String, Object>>();
			errList = new  ArrayList<Map<String, Object>>();
			for(Object[] o : travelerInfoList) {
				Map<String, Object> innerInfoMap = new HashMap<String, Object>();
				innerInfoMap.put("id", o[0]);
				innerInfoMap.put("name", o[1]);
				innerInfoMap.put("code", o[2]);
				innerInfoMap.put("visaId", o[6]);
				if(null != o[3] && !"".equals(o[3])) {
					String totalCurrency = "";
					//获取押金金额的币种中文名称
					List<MoneyAmount> moneyAmountList = moneyAmountDao.findAmountBySerialNum(o[3].toString());
					for(MoneyAmount money : moneyAmountList) {
						totalCurrency += CurrencyUtils.getCurrencyInfo(money.getCurrencyId().toString(), 0, "name") + "、";
					}
					totalCurrency = totalCurrency.toString().substring(0, totalCurrency.toString().length() - 1);
					innerInfoMap.put("totalCurrency", totalCurrency);
					o[3] = OrderCommonUtil.getMoneyAmountBySerialNum(o[3].toString(), 2);
					//由于签证游客交押金时无多币种，所以币种id取list第一个值
					String totalCurrencyId = moneyAmountList.get(0).getCurrencyId().toString();
					innerInfoMap.put("totalCurrencyId", totalCurrencyId);
				}else{
					innerInfoMap.put("totalCurrency", "-");
				}
				innerInfoMap.put("total", o[3]);
//				//过滤游客，并写明原因
				//订金已达账(有达账UUID并且金额大于0)
				if(null != o[4] && 1 == moneyAmountDao.findAmountBySerialNum(o[4].toString()).get(0).getAmount().compareTo(new BigDecimal(0))) {
					//是否已经退过押金
					Long deptId = this.getProductPept(o[6]);
					Long travelerId = StringUtils.toLong(o[0]);
					List<Review> appliedList = reviewService.findReview(StringUtils.toInteger(Context.PRODUCT_TYPE_QIAN_ZHENG), 7, o[6].toString(), travelerId, true, deptId);
					if(null == appliedList || appliedList.isEmpty()) {
						o[4] = OrderCommonUtil.getMoneyAmountBySerialNum(o[4].toString(), 2);
						innerInfoMap.put("accountedMoney", o[4]);
						rightList.add(innerInfoMap);
					}else{
						innerInfoMap.put("moneyStatus", "已申请");
						innerInfoMap.put("message", "已申请退押金");
						errList.add(innerInfoMap);
					}
				}else{
					if(null == o[3] || "".equals(o[3].toString())) {
						innerInfoMap.put("total", "-");
						innerInfoMap.put("moneyStatus", "无押金");
						innerInfoMap.put("message", "没有交押金");
					}else{
						innerInfoMap.put("moneyStatus", "未达账");
						innerInfoMap.put("message", "押金未达账");
					}
					errList.add(innerInfoMap);
				}
			}
			resultMap.put("errList", errList);
			resultMap.put("rightList", rightList);
		}
		return resultMap;
	}

	/**
	 * 根据订单编号 查询游客信息
	 * @param orderNo
	 * @return
	 */
	public List<?> getTravellInfoByOrderNo(String orderNo)
	{
		StringBuffer  sqlString = new StringBuffer();
		return visaOrderDao.findBySql(sqlString.toString(), Map.class);
	}

	/**
	 * 借款记录
	 * @param orderId 订单ID
	 * @return list
	 */
	public List<Map<String, String>> borrowMoneyRecord(String orderId) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		// 环球行
		if (UserUtils.getUser().getCompany().getId()==68) {
			// 通过订单ID查询reviewId
			List<Review> reviewList = reviewDao.findReviewSortByCreateDate(
					Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY, orderId);
			if (reviewList != null && reviewList.size() > 0) {
				for (Review review : reviewList) {
					Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(review.getId());
					// 报批日期格式化
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("createDate"))
							&& reviewAndDetailInfoMap.get("createDate").length() > 10) {
						String createDate = reviewAndDetailInfoMap.get("createDate").substring(0, 10);
						reviewAndDetailInfoMap.put("createDate", createDate);
					}
					// 游客/团队
					if (StringUtils.isBlank(reviewAndDetailInfoMap.get("travelerId"))) {
						reviewAndDetailInfoMap.put("travelerName", "团队");
					} else {
						Traveler traveler = travelerDao.findById(Long.parseLong(reviewAndDetailInfoMap.get("travelerId")));
						reviewAndDetailInfoMap.put("travelerName", traveler.getName());
					}
					// 币种
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("currencyId"))) {
						Currency currency = currencyService.findCurrency(Long.parseLong(reviewAndDetailInfoMap.get("currencyId")));
						reviewAndDetailInfoMap.put("currencyName", currency.getCurrencyName());
					}
					// 借款金额
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("borrowAmount"))) {
						DecimalFormat df = new DecimalFormat("#,##0.00");
						String borrowAmount = df.format(new BigDecimal(reviewAndDetailInfoMap.get("borrowAmount")));
						reviewAndDetailInfoMap.put("borrowAmount", borrowAmount);
					}
					list.add(reviewAndDetailInfoMap);
				}
			}
		// 新行者
		} else if (UserUtils.getUser().getCompany().getId()==71) {
			// 通过订单ID查询reviewId
			List<Review> reviewList = reviewDao.findReviewSortByCreateDate(Context.ORDER_TYPE_QZ, 20, orderId);
			if (reviewList != null && reviewList.size() > 0) {
				for (Review review : reviewList) {
					Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(review.getId());
					// 报批日期格式化
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("createDate"))
							&& reviewAndDetailInfoMap.get("createDate").length() > 10) {
						String createDate = reviewAndDetailInfoMap.get("createDate").substring(0, 10);
						reviewAndDetailInfoMap.put("createDate", createDate);
					}
					// 游客/团队
					if (StringUtils.isBlank(reviewAndDetailInfoMap.get("travelerId"))) {
						reviewAndDetailInfoMap.put("travelerName", "团队");
					} else {
						Traveler traveler = travelerDao.findById(Long.parseLong(reviewAndDetailInfoMap.get("travelerId")));
						reviewAndDetailInfoMap.put("travelerName", traveler.getName());
					}
					// 币种
					reviewAndDetailInfoMap.put("currencyName", "人民币");
					// 借款金额
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("borrowAmount"))) {
						DecimalFormat df = new DecimalFormat("#,##0.00");
						String borrowAmount = df.format(new BigDecimal(reviewAndDetailInfoMap.get("borrowAmount")));
						reviewAndDetailInfoMap.put("borrowAmount", borrowAmount);
					}
					list.add(reviewAndDetailInfoMap);
				}
			}
		//其他
		} else {
			// 通过订单ID查询reviewId
			List<Review> reviewList = reviewDao.findReviewSortByCreateDate(
					Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY, orderId);
			if (reviewList != null && reviewList.size() > 0) {
				for (Review review : reviewList) {
					Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(review.getId());
					// 报批日期格式化
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("createDate"))
							&& reviewAndDetailInfoMap.get("createDate").length() > 10) {
						String createDate = reviewAndDetailInfoMap.get("createDate").substring(0, 10);
						reviewAndDetailInfoMap.put("createDate", createDate);
					}
					// 游客/团队
					if (StringUtils.isBlank(reviewAndDetailInfoMap.get("travelerId"))) {
						reviewAndDetailInfoMap.put("travelerName", "团队");
					} else {
						Traveler traveler = travelerDao.findById(Long.parseLong(reviewAndDetailInfoMap.get("travelerId")));
						reviewAndDetailInfoMap.put("travelerName", traveler.getName());
					}
					// 币种
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("currencyId"))) {
						Currency currency = currencyService.findCurrency(Long.parseLong(reviewAndDetailInfoMap.get("currencyId")));
						reviewAndDetailInfoMap.put("currencyName", currency.getCurrencyName());
					}
					// 借款金额
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("borrowAmount"))) {
						DecimalFormat df = new DecimalFormat("#,##0.00");
						String borrowAmount = df.format(new BigDecimal(reviewAndDetailInfoMap.get("borrowAmount")));
						reviewAndDetailInfoMap.put("borrowAmount", borrowAmount);
					}
					list.add(reviewAndDetailInfoMap);
				}
			}
		}
		return list;
	}
	
	
	
	
	
	
	
	
	/**
	 * 返佣记录
	 * @param orderId 订单ID
	 * @return list
	 */
	public List<Map<String, String>> rebatesRecord(String orderId) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			// 通过订单ID查询reviewId
			List<Review> reviewList = reviewDao.findReviewSortByCreateDate(
					Context.ORDER_TYPE_QZ, Context.REBATES_FLOW_TYPE, orderId);
			if (reviewList != null && reviewList.size() > 0) {
				for (Review review : reviewList) {
					Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(review.getId());
					// 返佣申请日期格式化
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("createDate"))
							&& reviewAndDetailInfoMap.get("createDate").length() > 10) {
						String createDate = reviewAndDetailInfoMap.get("createDate").substring(0, 10);
						reviewAndDetailInfoMap.put("createDate", createDate);
					}
					// 游客/团队 团队返佣/个人返佣
					if (StringUtils.isBlank(reviewAndDetailInfoMap.get("travelerId"))) {
						reviewAndDetailInfoMap.put("travelerName", "团队");
						
						//团队预计返佣
						VisaOrder visaOrder = findVisaOrder(Long.parseLong(orderId));
						String groupRebatesUUID = visaOrder.getGroupRebate();
						if(StringUtils.isBlank(groupRebatesUUID)){
							reviewAndDetailInfoMap.put("yujiRebates", "—");
						}else{
							MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(groupRebatesUUID);
							if(moneyAmount != null){
								if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO) != 0){
									reviewAndDetailInfoMap.put("yujiRebates", OrderCommonUtil.getMoneyAmountBySerialNum(groupRebatesUUID, 2));
								}else{
									reviewAndDetailInfoMap.put("yujiRebates", "—");
								}
							}else{
								reviewAndDetailInfoMap.put("yujiRebates", "—");
							}
						}
					} else {
						Traveler traveler = travelerDao.findById(Long.parseLong(reviewAndDetailInfoMap.get("travelerId")));
						reviewAndDetailInfoMap.put("travelerName", traveler.getName());
						
						//个人预计返佣
						String travelerRebatesUUID = traveler.getRebatesMoneySerialNum();
						if(StringUtils.isBlank(travelerRebatesUUID)){
							reviewAndDetailInfoMap.put("yujiRebates", "—");
						}else{
							MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(travelerRebatesUUID);
							if(moneyAmount != null){
								if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO) != 0){
									reviewAndDetailInfoMap.put("yujiRebates", OrderCommonUtil.getMoneyAmountBySerialNum(travelerRebatesUUID, 2));
								}else{
									reviewAndDetailInfoMap.put("yujiRebates", "—");
								}
							}else{
								reviewAndDetailInfoMap.put("yujiRebates", "—");
							}
						}
					}
					// 币种
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("currencyId"))) {
						Currency currency = currencyService.findCurrency(Long.parseLong(reviewAndDetailInfoMap.get("currencyId")));
						reviewAndDetailInfoMap.put("currencyName", currency.getCurrencyName());
					}
					// 应收金额
					if (StringUtils.isBlank(reviewAndDetailInfoMap.get("travelerId"))) {
						VisaOrder visaOrder = visaOrderDao.findByOrderId(Long.parseLong(orderId));
						reviewAndDetailInfoMap.put("yingshouJe", visaOrder.getTotalMoney());
					} else {
						Traveler traveler = travelerDao.findById(Long.parseLong(reviewAndDetailInfoMap.get("travelerId")));
						reviewAndDetailInfoMap.put("yingshouJe", traveler.getPayPriceSerialNum());
					}
					// 订单累计返佣金额
					List<ReviewDetail> totalRebatesJe =reviewDetailDao.findReviewDetailByMykey(review.getId(), "totalRebatesJe");
					if (totalRebatesJe != null && totalRebatesJe.size() > 0) {
						reviewAndDetailInfoMap.put("totalRebatesJe", totalRebatesJe.get(0).getMyvalue());
					}
					// 返佣金额
					if (StringUtils.isNotBlank(reviewAndDetailInfoMap.get("totalrebatesamount"))) {
						reviewAndDetailInfoMap.put("totalrebatesamount", reviewAndDetailInfoMap.get("totalrebatesamount"));
					}
					// 备注
					String rebatesnodes = "";
					List<ReviewDetail> trvrebatesnotes =reviewDetailDao.findReviewDetailByMykey(review.getId(), "trvrebatesnotes");
					List<ReviewDetail> grouprebatesnodes =reviewDetailDao.findReviewDetailByMykey(review.getId(), "grouprebatesnodes");
					if (trvrebatesnotes.size() >0 && StringUtils.isNotBlank(trvrebatesnotes.get(0).getMyvalue())) {
						rebatesnodes += trvrebatesnotes.get(0).getMyvalue();
					}
					if (grouprebatesnodes.size() > 0 && StringUtils.isNotBlank(grouprebatesnodes.get(0).getMyvalue())) {
						rebatesnodes += grouprebatesnodes.get(0).getMyvalue();
					}
					if (rebatesnodes.length() > 0) {
						rebatesnodes = rebatesnodes.replace(VisaBorrowMoneyController.SPLITMARK, " ");
						rebatesnodes = rebatesnodes.substring(0, rebatesnodes.length()-1);
					}
					reviewAndDetailInfoMap.put("rebatesnodes", rebatesnodes);
					
					list.add(reviewAndDetailInfoMap);
				}
			}
		return list;
	}
	
	
	
	
	
	
	/**
	 * 清除session中Orderpay的缓存
	 * @param role
	 * @return
	 */
	public Object clearObject(Object object){
		orderpayDao.getSession().evict(object);
		return object;
	}
	
	/**
    * 修改  凭证 上传图片文件
    * @param docInfoList
    * @param payId
    * @param orderId
    * @param mode
    * @param request
    * @return
    * @throws OptimisticLockHandleException
    * @throws PositionOutOfBoundException
    * @throws Exception
    */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> updatepayVoucherFile(
			ArrayList<DocInfo> docInfoList, Orderpay orderPay, String orderId,
			ModelMap mode, HttpServletRequest request)
			throws OptimisticLockHandleException, PositionOutOfBoundException,
			Exception {

		ArrayList<DocInfo> old_docInfoList=(ArrayList<DocInfo>) docInfoDao.findDocInfoByPayOrderId(orderPay.getId());
		if (old_docInfoList != null && old_docInfoList.size() > 0) {
			Iterator<DocInfo> iter = old_docInfoList.iterator();
			while (iter.hasNext()) {
				DocInfo old_docInfo = iter.next();
				docInfoDao.delete(old_docInfo);
			}
		}
		VisaOrder pra = visaOrderDao.findOne(new Long(orderId));

		if (docInfoList != null && docInfoList.size() > 0) {
			Iterator<DocInfo> iter = docInfoList.iterator();
			String docInfoIds = null;
			while (iter.hasNext()) {
				DocInfo docInfo = iter.next();
				docInfo.setPayOrderId(orderPay.getId());
				docInfoDao.save(docInfo);
				// save 保存之后，docInfo对象在数据库生成的主键ID居然也加入了docInfo对象中。
				// save之前，getId()应该是null，save后getId()有值了。
				if(StringUtils.isNotBlank(docInfoIds)){
					docInfoIds += "," + docInfo.getId();
				}else{
					docInfoIds =  docInfo.getId().toString();
				}
			}
			orderPay.setPayVoucher(docInfoIds);
		}
		orderpayDao.updateBySql(
				"update orderpay set payVoucher = ?,remarks=? where id = ?", orderPay
						.getPayVoucher(),orderPay.getRemarks(), orderPay.getId());
		
		Map<String, Object> map = new HashMap<String, Object>();

		//支付订单金额千位符处理
		if (StringUtils.isNotBlank(orderPay.getMoneySerialNum())) {
			clearObject(orderPay);
			orderPay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderPay.getMoneySerialNum()));
		}
		mode.addAttribute("orderPay", orderPay);
		mode.addAttribute("pra", pra);
		map.put("isSuccess", true);
		return map;
	}
	
	/**
	 * 根据订单id订单的借款uuid 
	 * @param jkSerialNum 游客结算价流水号
	 * @param travelerId 游客Id
	 */
	public void updateOrderJkSerialnum(String jkSerialNum, Long orderId){
		visaOrderDao.updateOrderJkSerialnum(orderId, jkSerialNum);
	}

	/**
	 * 查找借款游客信息
	 * @param conditon
	 * @return
	 */
	public List<Map<String, Object>> findBatchJkTraveler(Map<String,String> conditon){
		List<Dict> visaStatusList = findDictByType("visa_status");
		String batchNo = conditon.get("batchNo");
		String hql  = "select " +
				" bt.traveler_id tid, " +
				" bt.traveler_name tname, " +
				//" vo.group_code groupCode, " +  // 对应需求C460v3  订单团号  统一取订单所关联产品团号
				" vo.group_code orderTuanhaoFromVOTable, "+//C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s//
				" vp.groupCode groupCode, " + 
				" vo.order_no orderNo, " +
				" vp.visaType visaType, " +
				" sc.countryName_cn visaCountry, " +
				" v.start_out startOut, " +
				" v.contract contract, " +
				" v.visa_stauts visaStatus, " +
				" bt.traveller_borrow_money travelerBorrowMoney, " +
				" bt.createby_name createByName, " +
				" bt.order_id orderId, " +
				" bt.is_submit isSubmit " +
				" from batch_traveler_relation bt " +
				" LEFT JOIN visa_order vo ON vo.id = bt.order_id " +
				" LEFT JOIN visa_products vp ON vp.id = vo.visa_product_id " +
				" LEFT JOIN sys_country sc ON sc.id = vp.sysCountryId " +
				" LEFT JOIN visa v ON v.id = bt.visa_id " +
				" WHERE bt.batch_record_no = '"+batchNo+"' AND bt.business_type = '1'";
		List<Map<String, Object>> list = batchTravelerRelationDao.findBySql(hql,Map.class);
		List<Dict> dictList = findDictByType("new_visa_type");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map:list){
			//签证类别 
			map.put("visaTypeDesc", "");
			if(null != dictList && dictList.size()>0){
				for(int i=0;i<dictList.size();i++){
					Dict dict =dictList.get(i);
					if(String.valueOf(map.get("visaType")).equals(dict.getValue())){
						map.put("visaTypeDesc", dict.getLabel());
						break;
					}
				}
			}
			//实际出团时间
			if(null != map.get("startOut") && !"".equals(map.get("startOut"))){
				String customDate = DateUtils.formatCustomDate((Date)map.get("startOut"),"yyyy-MM-dd");
				map.put("startOut", customDate);
			}else{
				map.put("startOut", "");
			}
			//实际签约时间
			if(null != map.get("contract") && !"".equals(map.get("contract"))){
				String customDate = DateUtils.formatCustomDate((Date)map.get("contract"),"yyyy-MM-dd HH:mm");
				map.put("contract", customDate);
			} else {
				map.put("contract", "");
			}
			// 借款金额
			if(null != map.get("travelerBorrowMoney") && !"".equals(map.get("travelerBorrowMoney"))){
				DecimalFormat df = new DecimalFormat("#,##0.00");
				String travelerBorrowMoney = df.format(new BigDecimal(map.get("travelerBorrowMoney").toString()));
				map.put("travelerBorrowMoney", travelerBorrowMoney);
			} else {
				map.put("travelerBorrowMoney", "0.00");
			}
			//签证状态
			Object visaStatus = map.get("visaStatus"); 
			map.put("visaStatusDesc", "");
			if(null != visaStatus && !"".equals(visaStatus)){
				if("3".equals(String.valueOf(visaStatus))){
					map.put("visaStatusDesc", "通过");
				} else if (!"2".equals(String.valueOf(visaStatus))) {
					for(Dict dict:visaStatusList){
						if(String.valueOf(visaStatus).equals(dict.getValue())){
							map.put("visaStatusDesc", dict.getLabel());
							break;
						}
					}
				}
			}
			resultList.add(map);
		}
		return resultList;
	}

	public List<Map<String, Object>> findBatchGuaTraveler(Map<String,String> conditon){
//		List<Dict> visaStatusList = findDictByType("visa_status");
		Long companyId = UserUtils.getUser().getCompany().getId();
		String batchNo = conditon.get("batchNo");

		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" SELECT ");
		sqlBuffer.append(" t.name travelerName, ");//0游客名称
		sqlBuffer.append(" t.passportCode passportId, ");//1护照编号
		sqlBuffer.append(" v.AA_code aACode, ");//2AA码
		sqlBuffer.append(" vp.visaType visaType, ");//3签证类型
		sqlBuffer.append(" sc.countryName_cn visaCountry, ");//4签证国家
		sqlBuffer.append(" v.forecast_start_out forecastStartOut, ");//5预定出团时间
		sqlBuffer.append(" v.forecast_contract forecastContract, ");//6预计签约时间
		sqlBuffer.append(" v.start_out startOut, ");//7实际出团时间
		sqlBuffer.append(" v.contract contract, ");//8实际签约时间
		sqlBuffer.append(" v.visa_stauts visaStatus, ");//9签证状态
		sqlBuffer.append(" t.passportStatus passportStatus, ");//10护照状态
		sqlBuffer.append(" v.guarantee_status guaranteeStatus, ");//11担保类型
		sqlBuffer.append(" v.total_deposit totalDepositUUID, ");//12应收押金UUID
		sqlBuffer.append(" v.payed_deposit payedDepositUUID, ");//已收押金
		sqlBuffer.append(" v.accounted_deposit accountedDeposit, ");//13达账押金UUID
		sqlBuffer.append(" t.id id, ");//14游客主键id
		sqlBuffer.append(" v.id visaId, ");//15 visa表的id
		sqlBuffer.append(" vp.id visaProductId, ");//16 签证产品id
		sqlBuffer.append(" vo.id visaorderId, ");//17 visa_order表id
		sqlBuffer.append(" vo.order_no visaorderNo,vo.mainOrderId main_orderId, ");//18 visa_order表的订单编号
		sqlBuffer.append(" vo.agentinfo_id agentinfoId, ");//19 visa_order表渠道IDagentId
		sqlBuffer.append(" vo.salerName, ");
		sqlBuffer.append(" v.payed_deposit payedDeposit, ");//20  已付押金UUID
		sqlBuffer.append(" su.name creatUser, ");//21下单人
		sqlBuffer.append(" vo.create_date createTime, ");//22下单时间
		sqlBuffer.append(" po.orderStatus groupType, ");//23参团类型
		sqlBuffer.append(" vo.lockStatus lockStatus, ");//24订单锁死状态
		sqlBuffer.append(" t.payPriceSerialNum payPriceSerialNum, ");//25游客的应收uuid
		sqlBuffer.append(" t.payed_moneySerialNum payedMoneySerialNum, ");//26游客的已付uuid
		sqlBuffer.append(" t.main_order_id mainOrderId, ");//27游客的主订单id
		sqlBuffer.append(" po.orderNum mainOrderNum, ");//28游客的主订单编号
		sqlBuffer.append(" ag.groupCode cantuantuanhao, ");//29游客的参团团号
		sqlBuffer.append(" t.accounted_money accountedMoney, ");//30游客的达账uuid
		sqlBuffer.append(" t.payment_type paymentType, ");//31游客的结算方式
		sqlBuffer.append(" v.actual_delivery_time deliveryTime, ");//32 实际送签时间
		sqlBuffer.append(" vp.lockStatus activityLockStatus, ");//33 visa_products锁死状态
		sqlBuffer.append(" vp.collarZoning collarZoning, ");//34 领区
		sqlBuffer.append(" vo.update_date updateDate ");//35更新时间
		sqlBuffer.append(" FROM traveler t ");
		sqlBuffer.append(" LEFT JOIN batch_traveler_relation  btr on btr.traveler_id = t.id ");
		sqlBuffer.append(" LEFT JOIN visa v on t.id = v.traveler_id ");
		sqlBuffer.append(" LEFT JOIN visa_order vo on vo.id = t.orderId ");
		sqlBuffer.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ");
		sqlBuffer.append(" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id ");
		sqlBuffer.append(" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId ");
		sqlBuffer.append(" LEFT JOIN sys_user su on su.id = vo.create_by ");
		sqlBuffer.append(" LEFT JOIN productorder po ");
		sqlBuffer.append(" on po.id = t.main_order_id ");
		sqlBuffer.append(" LEFT JOIN activitygroup ag ");
		sqlBuffer.append(" on ag.id = po.productGroupId ");
//		if(companyId == 68){
//			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
//		}else if(companyId == 71) {
//			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
//		}else{
//			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
//		}
		//查询新建面签通知时填写的说明会时间
//		sqlBuffer.append("LEFT JOIN visa_interview_notice_traveler vint on t.id = vint.travaler_id ");
//		sqlBuffer.append("LEFT JOIN visa_interview_notice vin on vint.interview_id = vin.id and vin.del_flag='0' ");

		sqlBuffer.append(" where btr.batch_record_no = '"+batchNo+"' and btr.business_type = 6 and t.order_type = 6 and t.companyId = " + companyId + " and t.delFlag=0 and vo.visa_order_status != 100 and vo.del_flag = 0 and vo.create_by IS NOT NULL ");;
		sqlBuffer.append(" and a.status = 1 ");

		//只能看本供应商下的签证订单
		sqlBuffer.append(" and su.companyId = "+ companyId +" ");
//		sqlBuffer.append(" and t.orderId = "+orderId);
		//依游客id分组以便查询该游客最大说明会时间
		sqlBuffer.append(" GROUP BY t.id");

		List<Map<String,Object>> travelerList = visaOrderDao.findBySql(sqlBuffer.toString(),Map.class);

		//游客数据处理
		travelerData(travelerList,"xiaoshou");

		return travelerList;
	}

	/**
	 * 查找还收据游客信息
	 * @param conditon
	 * @return
	 */
	public List<Map<String, Object>> findBatchHsjTraveler(Map<String,String> conditon){
		List<Dict> visaStatusList = findDictByType("visa_status");
		String batchNo = conditon.get("batchNo");
		String hql  = "select " +
				" bt.traveler_id tid, " +
				" bt.traveler_name tname, " +
				" vo.group_code groupCode, " +  //C460V3  签证订单团号 统一取   订单所关联的产品团号
				" vp.groupCode vpGroupCode, " +   //C460V3  plus 签证订单团号 统一取   订单所关联的产品团号
				" vo.order_no orderNo, " +
				" vp.visaType visaType, " +
				" sc.countryName_cn visaCountry, " +
				" v.start_out startOut, " +
				" v.contract contract, " +
				" v.visa_stauts visaStatus, " +
				" bt.traveller_borrow_money travelerBorrowMoney, " +
				" bt.createby_name createByName, " +
				" bt.order_id orderId, " +
				" bt.is_submit isSubmit " +
				" from batch_traveler_relation bt " +
				" LEFT JOIN visa_order vo ON vo.id = bt.order_id " +
				" LEFT JOIN visa_products vp ON vp.id = vo.visa_product_id " +
				" LEFT JOIN sys_country sc ON sc.id = vp.sysCountryId " +
				" LEFT JOIN visa v ON v.id = bt.visa_id " +
				" WHERE bt.batch_record_no = '"+batchNo+"' AND bt.business_type = '2'";
		List<Map<String, Object>> list = batchTravelerRelationDao.findBySql(hql,Map.class);
		List<Dict> dictList = findDictByType("new_visa_type");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map:list){
			//签证类别 
			map.put("visaTypeDesc", "");
			if(null != dictList && dictList.size()>0){
				for(int i=0;i<dictList.size();i++){
					Dict dict =dictList.get(i);
					if(String.valueOf(map.get("visaType")).equals(dict.getValue())){
						map.put("visaTypeDesc", dict.getLabel());
						break;
					}
				}
			}
			
			
			String companeyUUID = UserUtils.getUser().getCompany().getUuid();
			if ("7a816f5077a811e5bc1e000c29cf2586".equals(companeyUUID)) {
				map.put("groupCode", map.get("groupCode").toString());
			}else{
				map.put("groupCode", map.get("vpGroupCode").toString());
			}
			
			
			//实际出团时间
			if(null != map.get("startOut") && !"".equals(map.get("startOut"))){
				String customDate = DateUtils.formatCustomDate((Date)map.get("startOut"),"yyyy-MM-dd");
				map.put("startOut", customDate);
			}else{
				map.put("startOut", "");
			}
			//实际签约时间
			if(null != map.get("contract") && !"".equals(map.get("contract"))){
				String customDate = DateUtils.formatCustomDate((Date)map.get("contract"),"yyyy-MM-dd HH:mm");
				map.put("contract", customDate);
			} else {
				map.put("contract", "");
			}
			// 还收据金额
			if(null != map.get("travelerBorrowMoney") && !"".equals(map.get("travelerBorrowMoney"))){
				DecimalFormat df = new DecimalFormat("#,##0.00");
				String travelerBorrowMoney = df.format(new BigDecimal(map.get("travelerBorrowMoney").toString()));
				map.put("travelerBorrowMoney", travelerBorrowMoney);
			} else {
				map.put("travelerBorrowMoney", "0.00");
			}
			//签证状态
			Object visaStatus = map.get("visaStatus"); 
			map.put("visaStatusDesc", "");
			if(null != visaStatus && !"".equals(visaStatus)){
				if("3".equals(String.valueOf(visaStatus))){
					map.put("visaStatusDesc", "通过");
				} else if (!"2".equals(String.valueOf(visaStatus))) {
					for(Dict dict:visaStatusList){
						if(String.valueOf(visaStatus).equals(dict.getValue())){
							map.put("visaStatusDesc", dict.getLabel());
							break;
						}
					}
				}
			}
			resultList.add(map);
		}
		return resultList;
	}
	
	/**
	 * 修改订单的预定渠道
	 * @author jiachen
	 * @DateTime 2015年7月16日 下午4:02:50
	 * @return void
	 */
	@Transactional
	public void updateOrderAgent(String orderId, String agentId, String[] dataArr) {
		if(StringUtils.isNotBlank(orderId)) {
			//获取订单
			VisaOrder visaOrder = visaOrderDao.findByOrderId(StringUtils.toLong(orderId));
			//修改订单预定渠道
			visaOrder.setAgentinfoId(StringUtils.toLong(agentId));
			visaOrderDao.save(visaOrder);
			//获取订单联系人
			List<OrderContacts> contactsList = orderContactsDao.findOrderContactsByOrderIdAndOrderType(visaOrder.getId(),6);
			if(!contactsList.isEmpty()) {
				//默认第一个联系人是从渠道信息中读取的
				OrderContacts contacts = contactsList.get(0);
				contacts.setContactsName(dataArr[0]);
				contacts.setContactsTel(dataArr[1]);
				contacts.setContactsTixedTel(dataArr[2]);
				contacts.setContactsAddress(dataArr[3]);
				contacts.setContactsFax(dataArr[4]);
				contacts.setContactsQQ(dataArr[5]);
				contacts.setContactsEmail(dataArr[6]);
				orderContactsDao.save(contacts);
			}
		}
	}
	
	/**
	 * 修改订单预计团队返佣
	 * @author nan
	 */
	@Transactional
	public void updateGroupRebates(String orderId, String groupRebatesCurrency, String groupRebatesMoney) {
		if(StringUtils.isNotBlank(orderId)) {
			//获取订单
			VisaOrder visaOrder = visaOrderDao.findByOrderId(StringUtils.toLong(orderId));
			//订单预计团队返佣UUID
			String groupRebatesUUID = visaOrder.getGroupRebate();
			if(StringUtils.isNotBlank(groupRebatesUUID)){
				MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(groupRebatesUUID);
				if(!groupRebatesCurrency.equals(moneyAmount.getCurrencyId().toString())) {
					logOrderService.saveObj("currencyId", Long.valueOf(orderId), logOrderService.content("团队返佣币种", CurrencyUtils.getCurrencyNameOrFlag(moneyAmount.getCurrencyId().longValue(), "1"),
							CurrencyUtils.getCurrencyNameOrFlag(Long.valueOf(groupRebatesCurrency), "1")), 2);
				}
				if(!groupRebatesMoney.equals(moneyAmount.getAmount().toString())) {
					logOrderService.saveObj("amount", Long.valueOf(orderId), logOrderService.content("团队返佣金额", moneyAmount.getAmount().toString(), groupRebatesMoney), 2);
				}
				moneyAmount.setCurrencyId(Integer.parseInt(groupRebatesCurrency));
				if(StringUtils.isNotBlank(groupRebatesMoney)){
					moneyAmount.setAmount(new BigDecimal(groupRebatesMoney));
				}else{
					moneyAmount.setAmount(BigDecimal.ZERO);
				}
				moneyAmountDao.save(moneyAmount);
			}else{

				logOrderService.saveObj("currencyId", Long.valueOf(orderId), logOrderService.content("团队返佣币种", "", CurrencyUtils.getCurrencyNameOrFlag(Long.valueOf(groupRebatesCurrency), "1")), 2);
				logOrderService.saveObj("amount", Long.valueOf(orderId), logOrderService.content("团队返佣金额", "", groupRebatesMoney), 2);

				//新生成团队返佣UUID
				String groupRebatesSerialNum = UUID.randomUUID().toString();
				MoneyAmount amount = new MoneyAmount();
				amount.setSerialNum(groupRebatesSerialNum);
				amount.setCurrencyId(Integer.valueOf(groupRebatesCurrency));
				if (StringUtils.isNotBlank(groupRebatesMoney)) {
					amount.setAmount(new BigDecimal(groupRebatesMoney));
				}else{
					amount.setAmount(BigDecimal.ZERO);
				}
				amount.setUid(Long.parseLong(orderId));
				amount.setMoneyType(23);
				amount.setOrderType(6);
				amount.setBusindessType(1);
				amount.setCreatedBy(UserUtils.getUser().getId());
				amount.setCreateTime(new Date());
				moneyAmountService.saveOrUpdateMoneyAmount(amount);
				visaOrder.setGroupRebate(groupRebatesSerialNum);
				visaOrderDao.save(visaOrder);
			}
		}
	}
	
	/**
	 * 新行者获取订单借款金额的字符串  否则返回null
	 * 
	 * @param orderId
	 * @param travelerId
	 * @return 游客借款金额的字符串
	 */
	public String getVisaOrderBorrowedMoney(String orderId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId)) {
			listReview = reviewDao.findReviewActive(6, 20,orderId);
			if (listReview.size()>=1) {
				return listReview.get(listReview.size()-1).getStatus().toString();
			}
		}
		return null;
	}
	
	
	/**
	 * ----------处理新审核 -----------
	 * 
	 * @Description: 新行者获取订单借款金额的字符串  否则返回null
	 * @author xinwei.wang
	 * @date 2015年12月23日下午3:20:41
	 * @param orderId
	 * @return    
	 * @throws
	 */
	public String getVisaOrderBorrowedMoney4Activiti(String orderId){
		List<ReviewNew> listReview = new ArrayList<ReviewNew>();
		if (StringUtils.isNotEmpty(orderId)) {
			listReview = processReviewService.getOrderReviewList(orderId, "6", "19");
			for (ReviewNew reviewNew : listReview) {
				if ("1".equals(reviewNew.getStatus().toString())||"2".equals(reviewNew.getStatus().toString())) {
					return reviewNew.getStatus().toString();
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * 新行者获取订单借款金额的字符串  否则返回null
	 * 
	 * @param orderId
	 * @param travelerId
	 * @return 游客借款金额的字符串
	 */
	public Review getVisaOrderBorrowedStatusAndTime(String orderId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId)) {
			listReview = reviewDao.findReviewActive(6, 20, orderId);
		}
		if(null == listReview || listReview.size() == 0){
			return null;
		}else{
			return listReview.get(listReview.size()-1);
		}
	}
	
	
	
	/**
	 * 
	 * ----------------综合处理新旧审核的  借款状态问题-----------------
	 * 
	 * @Description: 根据 order id  查询  新行者  类  签证  借款  记录
	 * @author xinwei.wang
	 * @date 2015年12月21日下午3:56:17
	 * @param orderId
	 * @return    
	 * @throws
	 */
	public ReviewNew getVisaOrderBorrowedStatusAndTime4Activiti(String orderId){
		List<ReviewNew> listReview = new ArrayList<ReviewNew>();
		if (StringUtils.isNotEmpty(orderId)) {
			listReview = processReviewService.getOrderReviewList(orderId, "6", "19");
			for (ReviewNew reviewNew : listReview) {
				if ("1".equals(reviewNew.getStatus().toString())||"2".equals(reviewNew.getStatus().toString())) {
					return reviewNew;
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * 签证订单列表(新)
	 * @param VisaOrderForm 页面查询条件
	 * @param shenfen 身份
	 * @param common 部门相关信息
	 */
	public Page<Map<String,Object>> searchVisaOrder(HttpServletRequest request,HttpServletResponse response,
			VisaOrderForm visaOrderForm, String shenfen, DepartmentCommon common,boolean isAll) {

		//根据条件查询订单信息
		StringBuffer sqlBuffer = new StringBuffer();
		
		// sql优化：当有查询条件时才左关联对应表，map中储存是否需要关联对应表
		Map<String, Boolean> whereMap = Maps.newHashMap();
		// 获取查询条件
		StringBuffer whereSql = addWhereSql(whereMap, common, visaOrderForm, shenfen, request);
		
		// 获取查询主sql
		getMainSql(whereMap, sqlBuffer, visaOrderForm);
		sqlBuffer.append(whereSql);
		
		//列表排序
		String orderBy = request.getParameter("orderBy");
		Page<Map<String,Object>> page ;
		if(isAll){
			page = new Page<Map<String,Object>>(request, response,-1);
		}else{
			page = new Page<Map<String,Object>>(request, response);
		}
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy("vo.create_date DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		
		//分页查询订单数据
		Page<Map<String,Object>> result = visaDao.findBySql(page, sqlBuffer.toString(), Map.class);
		
		List<Map<String,Object>> pageList = result.getList();

		//200 通过visa_order表中的id, 关联orderContacts表 查询预订联系人数据
		for(int i=0;i<pageList.size();i++) {
			Object visaOrderId = pageList.get(i).get("orderId");
			List<OrderContacts> orderContactsList = visaOrderDao.findContactsNameByOrderId(StringUtils.toLong(visaOrderId));
			StringBuffer contactsName = new StringBuffer();
			String contactsNameStr = "";
			if(CollectionUtils.isNotEmpty(orderContactsList)) {
				for (int j = 0; j < orderContactsList.size(); j++) {
					contactsName.append(orderContactsList.get(j).getContactsName()).append(",");
				}
				contactsNameStr = contactsName.substring(0, contactsName.length()-1);
			}
			pageList.get(i).put("contactsName", contactsNameStr);
		}


		//历史数据(不可多次支付)
		//List<?> list1 = getOldVisaData();
		for(int i=0;i<pageList.size();i++){
			if("qianwu".equals(shenfen)){
				//订单提醒
				if(null != pageList.get(i).get("isRead") && !"".equals(pageList.get(i).get("isRead"))) {
					pageList.get(i).put("isRead", pageList.get(i).get("isRead").toString());
				}
			}
			if("xiaoshou".equals(shenfen)){
				//订单提醒
				if(null != pageList.get(i).get("isRead") && !"".equals(pageList.get(i).get("isRead"))) {
					pageList.get(i).put("isRead", pageList.get(i).get("isRead").toString());
				}
				//发票状态
				if(null != pageList.get(i).get("invoiceStatus") && !"".equals(pageList.get(i).get("invoiceStatus"))) {
					pageList.get(i).put("invoiceStatus","已开发票");
				}else{
					pageList.get(i).put("invoiceStatus","未开发票");
				}
				//收据状态
				if(null != pageList.get(i).get("receiptStatus") && !"".equals(pageList.get(i).get("receiptStatus"))) {
					pageList.get(i).put("receiptStatus","已开收据");
				}else{
					pageList.get(i).put("receiptStatus","未开收据");
				}
				//返佣金额
//				if(null != pageList.get(i).get("orderId") && !"".equals(pageList.get(i).get("orderId"))) {
//					//预计返佣List
//					List<String> yujiRebatesSerialNumList = new ArrayList<String>();
//					//订单预计团队返佣
//					if(null != pageList.get(i).get("groupRebate") && !"".equals(pageList.get(i).get("groupRebate"))) {
//						MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(pageList.get(i).get("groupRebate").toString());
//						if(moneyAmount != null){
//							BigDecimal groupRebate = moneyAmount.getAmount();
//							if(groupRebate.compareTo(BigDecimal.ZERO)!=0){
//								yujiRebatesSerialNumList.add(pageList.get(i).get("groupRebate").toString());
//							}
//						}
//					}
//					//获取办签人的信息
//					List<VisaOrderTravelerResultForm>  traveList = searchTravelerByOrder(pageList.get(i).get("orderId").toString(),pageList.get(i).get("orderCode").toString(),list1);
//					if(null != traveList && traveList.size()>0){
//						//每个游客的预计返佣
//						for(int j=0;j<traveList.size();j++){
//							if(null != traveList.get(j).getYujiRebates() && !"".equals(traveList.get(j).getYujiRebates())){
//								MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(traveList.get(j).getYujiRebates());
//								if(moneyAmount != null){
//									BigDecimal travelerRebate = moneyAmount.getAmount();
//									if(travelerRebate.compareTo(BigDecimal.ZERO)!=0){
//										yujiRebatesSerialNumList.add(traveList.get(j).getYujiRebates());
//									}
//								}
//							}
//						}
//						//实际返佣
//						//根据订单ID查询签证返佣审核通过的记录
//						List<Review> reviewList = reviewDao.findReviewIdByOrderId(6, 9, 2, String.valueOf(pageList.get(i).get("orderId")));
//						if(null != reviewList && reviewList.size()>0){
//							//最近一次通过的审核记录
//							Map<String,String> reviewMap = reviewService.findReview(reviewList.get(0).getId());
//							//实际返佣json串
//							String shijiRebatesJson = reviewMap.get("totalRebatesJe");
//							//累计返佣!=0
//							if(!"0".equals(shijiRebatesJson)) {
//								JSONObject jsonObject = JSONObject.fromObject(shijiRebatesJson);
//								@SuppressWarnings("unchecked")
//								Iterator<String> it = jsonObject.keys();
//								StringBuffer shijiRebatesBuf = new StringBuffer();
//								while (it.hasNext()) {
//									//币种符号
//									String key = String.valueOf(it.next());
//									Currency currency = currencyDao.findOne(Long.valueOf(key));
//									shijiRebatesBuf.append(currency.getCurrencyMark());
//									//金额
//									String value = (String) jsonObject.get(key);
//									DecimalFormat df = new DecimalFormat("#,##0.00");
//									String money = df.format(new BigDecimal(value));
//									shijiRebatesBuf.append(money);
//									//多个币种相加
//									shijiRebatesBuf.append("+");
//								}
//								//去掉最后一个+
//								String shijiRebates = shijiRebatesBuf.substring(0, shijiRebatesBuf.length()-1);
//								pageList.get(i).put("shijiRebates",shijiRebates);
//							}else{
//								pageList.get(i).put("shijiRebates","¥ 0");
//							}
//						}else{
//							pageList.get(i).put("shijiRebates","¥ 0");
//						}
//					}
//					//多个uuid相加
//					String yujiRebates = OrderCommonUtil.getMoneyAmountBySerialNum(yujiRebatesSerialNumList, 2);
//					pageList.get(i).put("yujiRebates",yujiRebates);
//				}
				if(null != pageList.get(i).get("orderId") && !"".equals(pageList.get(i).get("orderId"))) {
					//List<Map<String, Object>> ls =  moneyAmountService.getYuJiFanYong(pageList.get(i).get("orderId").toString());
					//String yujiRebates="";
					//if(ls.size()>0)
					//yujiRebates = ls.get(0).toString();
					List<?>  ls  =moneyAmountService.getYuJiFanYong(pageList.get(i).get("orderId").toString());
					if(ls.get(0) == null)
					pageList.get(i).put("yujiRebates","");
					else
					pageList.get(i).put("yujiRebates",moneyAmountService.getYuJiFanYong(pageList.get(i).get("orderId").toString()).get(0));
					String shijiRebates = moneyAmountService.getShiJiFanYong(pageList.get(i).get("orderId").toString());
					pageList.get(i).put("shijiRebates",shijiRebates);

					
				}
				
			}
		}
		return result;
	}
	
	/**
	 * 获取查询主sql 
	 * @author yakun.bai
	 * @Date 2016-6-2
	 */
	public StringBuffer getMainSql(Map<String, Boolean> whereMap, StringBuffer sqlBuffer, VisaOrderForm visaOrderForm) {
		sqlBuffer.append("SELECT a.agentName agentinfoName, ");
		sqlBuffer.append("vo.id orderId, vo.mainOrderId main_orderId,");
		sqlBuffer.append("vo.order_no orderCode, ");
		sqlBuffer.append("vp.id productId, ");
		sqlBuffer.append("vp.productCode productCode, ");
		sqlBuffer.append("vp.productName productName, ");
		//C460V3 所有批发商团号取产品团号
		sqlBuffer.append("vp.groupCode orderTuanhao, ");
		//C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s//
        sqlBuffer.append("vo.group_code orderTuanhaoFromVOTable, "); 
		//C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-e//
		sqlBuffer.append("vo.payStatus payStatus, ");
		sqlBuffer.append("vo.visa_order_status visaOrderStatus, ");
		sqlBuffer.append("su.name creatUser, ");
		sqlBuffer.append("vo.create_date createTime, ");
		sqlBuffer.append("vo.travel_num travelerCount, ");
		sqlBuffer.append("vo.total_money visaPay, ");
		sqlBuffer.append("vo.payed_money payedMoney, ");
		sqlBuffer.append("vo.accounted_money accountedMoney, ");
		if(StringUtils.isNotBlank(visaOrderForm.getJiekuanStatus())
				|| StringUtils.isNotBlank(visaOrderForm.getJiekuanTimeStart())
				|| StringUtils.isNotBlank(visaOrderForm.getJiekuanTimeEnd())){
			sqlBuffer.append("re.status status, ");
			sqlBuffer.append("re.createDate createDate, ");
		}
		sqlBuffer.append("op.isAsAccount isAsAccount, ");
		sqlBuffer.append("vo.lockStatus lockStatus, ");
		sqlBuffer.append("vp.lockStatus activityLockStatus, ");
		sqlBuffer.append("vp.visaType visaType, ");
		sqlBuffer.append("sc.countryName_cn visaCountry, ");
		sqlBuffer.append("vo.isRead isRead, ");
		//发票状态
		sqlBuffer.append("oin.verifyStatus invoiceStatus, ");
		//发票申请方式  0444
		sqlBuffer.append("MAX(oin.applyInvoiceWay) applyInvoiceWay, ");
		//收据状态 
		sqlBuffer.append("ore.verifyStatus receiptStatus, ");
		//预计团队返佣
		sqlBuffer.append("vo.groupRebate groupRebate, ");
		//销售名称
		sqlBuffer.append("vo.salerName salerName, ");
		//确认单Id
		sqlBuffer.append("vo.confirmationFileId confirmationFileId ,vo.confirmFlag,vo.downloadFileIds ");
		
		sqlBuffer.append("FROM visa_order vo ");
		sqlBuffer.append("LEFT JOIN sys_user su ON vo.create_by = su.id ");
		sqlBuffer.append("LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ");
		sqlBuffer.append("LEFT JOIN visa_products vp ON vo.visa_product_id=vp.id ");
		sqlBuffer.append("LEFT JOIN orderpay op ON vo.order_no=op.orderNum AND vo.id=op.orderId AND op.isAsAccount=1 ");
		//if (whereMap != null && whereMap.get("traveler") != null && whereMap.get("traveler")) {
			//bug14983
			sqlBuffer.append("LEFT JOIN traveler t on vo.id = t.orderId and t.order_type=6 and t.delFlag=0 ");
		//}
		if (whereMap != null && whereMap.get("visa") != null && whereMap.get("visa")) {
			sqlBuffer.append("LEFT JOIN visa v on t.id = v.traveler_id ");
		}
		if (whereMap != null && whereMap.get("productorder") != null && whereMap.get("productorder")) {
			sqlBuffer.append("LEFT JOIN productorder po on t.main_order_id=po.id ");
		}
		sqlBuffer.append("LEFT JOIN sys_country sc on sc.id = vp.sysCountryId ");
		sqlBuffer.append("LEFT JOIN orderinvoice oin ON oin.orderType='6' and oin.orderNum=vo.order_no and oin.verifyStatus != 2 ");
		sqlBuffer.append("LEFT JOIN orderreceipt ore ON ore.orderType='6' and ore.orderNum=vo.order_no and ore.verifyStatus != 2 ");
		if(StringUtils.isNotBlank(visaOrderForm.getJiekuanStatus())
				|| StringUtils.isNotBlank(visaOrderForm.getJiekuanTimeStart())
				|| StringUtils.isNotBlank(visaOrderForm.getJiekuanTimeEnd())){
			//bug14983
			//sqlBuffer.append("LEFT JOIN traveler t on vo.id = t.orderId and t.order_type=6 and t.delFlag=0 ");
			if(UserUtils.getUser().getCompany().getId()==68) {
				sqlBuffer.append("LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
			}else if(UserUtils.getUser().getCompany().getId()==71) {
				sqlBuffer.append("LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
			}else{
				sqlBuffer.append(" LEFT JOIN ((SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) union (SELECT rr.order_id orderId,rr.status status,rr.traveller_id travelerId,rr.create_date createDate FROM review_new rr RIGHT JOIN (select id,id_long from review_new re1 where not exists (select 1 from review_new where traveller_id = re1.traveller_id and id_long > re1.id_long) and re1.process_type = '5' AND re1.product_type = '6') r ON rr.id = r.id) ) re ON concat(t.id) = re.travelerId ");
			}
		}
		//关联说明会时间
		if (whereMap != null && whereMap.get("vv") != null && whereMap.get("vv")) {
			sqlBuffer.append(" LEFT JOIN ( SELECT MAX(vin.explanation_time) explanationTime, tt.orderId FROM ");
			sqlBuffer.append("traveler tt, visa_interview_notice_traveler vint, visa_interview_notice vin ");
			sqlBuffer.append("WHERE tt.id = vint.travaler_id AND vint.interview_id = vin.id AND vin.del_flag = '0' " +
								"AND tt.order_type = 6 and tt.companyId = " + UserUtils.getUser().getCompany().getId() + " GROUP BY tt.id ");
			sqlBuffer.append(")vv ON vo.id = vv.orderId ");
		}
		return sqlBuffer;
	}

	/**
	 * 添加查询where条件
	 * @author yakun.bai
	 * @Date 2016-6-2
	 */
	public StringBuffer addWhereSql(Map<String, Boolean> whereMap, DepartmentCommon common, 
			VisaOrderForm visaOrderForm, String shenfen, HttpServletRequest request) {
		
		StringBuffer sqlBuffer = new StringBuffer();
		
		//部门ID不为空时拼接部门SQL
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
			departmentSQL(common,sqlBuffer,1);
		}
		
		sqlBuffer.append("WHERE vo.visa_order_status != 100 ");
		sqlBuffer.append("AND vo.del_flag = 0 ");
		sqlBuffer.append("AND vo.create_by IS NOT NULL ");
		sqlBuffer.append("AND a.status = 1 ");
		
		//部门ID不为空时拼接部门SQL的where条件
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
			departmentSQL(common,sqlBuffer,2);
		}
		
		boolean flag=false;
		/** 部门之间人员是否能相互查看，默认不能  add by 2016/01/18 for requirement ID 113*/
		boolean LisenceFlag = whetherSelectAllDeptDate(common);
		//销售签证订单过滤渠道 true:只能看自己渠道 false:看所有渠道
		if("xiaoshou".equals(shenfen)){
			String userType = UserUtils.getUser().getUserType();
			//如果不是销售经理或管理员，则用户只能查看自己负责的渠道
			if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)){
				boolean isManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_MANAGER);
				boolean isSaleManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES_EXECUTIVE);
				boolean isFinance = false;
				List<UserJob> list = userJobDao.getUserJobList(UserUtils.getUser().getId());
				for (UserJob userJob:list) {
					String jobName = userJob.getJobName();
					if(jobName.indexOf("财务") != -1){
						isFinance = true;
						break;
					}
				}
				if(isFinance){
					flag=false;
				}else if(!isManager && !isSaleManager) {
					flag=true;
				}else{
					flag=false;
				}
				
				//如果配置相互查看权限，则拥有销售经理权限
				if(LisenceFlag){
					flag=false;
				}
			}
		}
		if(flag){
			sqlBuffer.append("AND (FIND_IN_SET("+UserUtils.getUser().getId()+",a.agentSalerId) or a.id=-1) ");
		}else{
			sqlBuffer.append("AND (a.supplyId="+UserUtils.getUser().getCompany().getId()+" OR a.id=-1) ");
		}
		
		//拼接查询条件SQL
		String mainOrderId = null;
		if (request != null) {
			mainOrderId = request.getParameter("mainOrderId");
		}
		searchVisaSQL(whereMap, visaOrderForm, sqlBuffer, shenfen,mainOrderId);
		
		//只能看本供应商下的签证订单
		sqlBuffer.append(" and su.companyId = "+UserUtils.getUser().getCompany().getId()+" ");
		sqlBuffer.append(" GROUP BY vo.id ");
		return sqlBuffer;
	}
	
	/**
	 * 签证订单列表拼接查询条件SQL
	 * */
	private StringBuffer searchVisaSQL(Map<String, Boolean> whereMap, VisaOrderForm visaOrderForm, StringBuffer sqlBuffer, String shenfen,String mainOrderId) {
		
		//验证参数是否合法
		if(null == visaOrderForm){
			return null;
		}
		
		//游客名称
		if(null != visaOrderForm.getTravelName() && !"".equals(visaOrderForm.getTravelName().trim())){
			String travelName = visaOrderForm.getTravelName().replace("'", "''");
			sqlBuffer.append(" and t.order_type='6' and t.delFlag='0' and t.name like '%"+travelName.trim()+"%' ");
			whereMap.put("traveler", true);
		}
		//订单号，产品编号的查询条件 签务
		if(null != visaOrderForm.getCommonCode() && !"".equals(visaOrderForm.getCommonCode().trim())){
			String commonCode = visaOrderForm.getCommonCode().replace("'", "''");
			sqlBuffer.append(" and ( vo.order_no like '%"+commonCode.trim()+"%' or vp.productCode like '%"+commonCode.trim()+"%' )");
		}
		//订单号的查询条件
		if(null != visaOrderForm.getOrderNo() && !"".equals(visaOrderForm.getOrderNo().trim())){
			String orderNo = visaOrderForm.getOrderNo().replace("'", "''");
			sqlBuffer.append(" and vo.order_no like '%"+orderNo.trim()+"%' ");
		}
		//bug16053，签证参团后该团订单签证信息显示错误
		if(StringUtils.isNotBlank(mainOrderId)) {
			List<Traveler> travelerList = travelerDao.findTravelersByMainOrderId(Long.parseLong(mainOrderId));
			String orderIds = "(";
			if(null != travelerList && travelerList.size() > 0) {
				int i = 0;
				for (Traveler traveler : travelerList) {
					i++;
					Long orderId = traveler.getOrderId();
					if (travelerList.size() == i) {
						orderIds += orderId;
					} else {
						orderIds += orderId + ",";
					}
				}
//			sqlBuffer.append(" and vo.mainOrderId like '%" + mainOrderId.trim() + "%' ");
				sqlBuffer.append("and vo.id in " + orderIds + ")");
			}
		}
		
		
		//产品编号的查询条件
		if(null != visaOrderForm.getVisaProductId() && !"".equals(visaOrderForm.getVisaProductId().trim())){
			String visaProductId = visaOrderForm.getVisaProductId().replace("'", "''");
			sqlBuffer.append(" and vp.productCode like '%"+visaProductId.trim()+"%' ");
		}
		//渠道ID
		if(null != visaOrderForm.getAgentinfoId() && !"".equals(visaOrderForm.getAgentinfoId().trim())){
			sqlBuffer.append(" and vo.agentinfo_id = '"+visaOrderForm.getAgentinfoId()+"' ");
		}
		//创建者编号
		if(null != visaOrderForm.getCreateBy() && !"".equals(visaOrderForm.getCreateBy().trim())){
			sqlBuffer.append(" and su.name = '"+visaOrderForm.getCreateBy().trim()+"' ");
		}
		//支付状态
		if(null != visaOrderForm.getOrderPayStatus() && !"".equals(visaOrderForm.getOrderPayStatus().trim())){
			sqlBuffer.append(" and vo.payStatus = '"+visaOrderForm.getOrderPayStatus()+"' ");
		}
		// 当显示三级菜单 Tab 页面是“全部订单”的时候， 判断批发商权限，是否在其中禁止显示“已取消”的订单
		if (StringUtils.isBlank(visaOrderForm.getShowType()) || visaOrderForm.getShowType().equals(Context.ORDER_PAYSTATUS_ALL)) {
			String banedOrderType = UserUtils.getUser().getCompany().getBanedVisaOrderOfAllTab();  // 获取批发商权限
			if (Functions.contains(banedOrderType, Context.ORDER_PAYSTATUS_YQX)) {
				sqlBuffer.append(" and vo.payStatus != '"+ Context.ORDER_PAYSTATUS_YQX + "' ");				
			}
		}
		//参团类型
		if(null != visaOrderForm.getOrderType() && !"".equals(visaOrderForm.getOrderType().trim())){
			if (Context.ORDER_STATUS_VISA.equals(visaOrderForm.getOrderType().trim())){
				sqlBuffer.append(" and t.main_order_id is null ");
			}else{
				sqlBuffer.append(" and po.orderStatus = '"+visaOrderForm.getOrderType()+"' ");
			}
			whereMap.put("traveler", true);
			whereMap.put("productorder", true);
		}
		//签证国家编号
		if(null != visaOrderForm.getSysCountryId() && !"".equals(visaOrderForm.getSysCountryId().trim())){
			sqlBuffer.append(" and vp.sysCountryId = '"+visaOrderForm.getSysCountryId()+"' ");
		}
		//签证状态
		if(null != visaOrderForm.getVisaStatus() && !"".equals(visaOrderForm.getVisaStatus().trim())){
			sqlBuffer.append("and v.visa_stauts = '"+visaOrderForm.getVisaStatus()+"' ");
			whereMap.put("traveler", true);
			whereMap.put("visa", true);
		}
		//签证类型
		if(null != visaOrderForm.getVisaType() && !"".equals(visaOrderForm.getVisaType().trim())){
			sqlBuffer.append(" and vp.visaType = '"+visaOrderForm.getVisaType()+"' ");
		}
		//护照状态
		if(StringUtils.isNotBlank(visaOrderForm.getPassportStatus())){
			sqlBuffer.append("and t.passportStatus = '"+visaOrderForm.getPassportStatus()+"' ");
			whereMap.put("traveler", true);
		}
		//借款状态
		if(null != visaOrderForm.getJiekuanStatus() && !"".equals(visaOrderForm.getJiekuanStatus().trim())){
			//1 审核中
			if("1".equals(visaOrderForm.getJiekuanStatus().trim())){
				sqlBuffer.append(" and re.status = 1 ");
				//2 已借
			}if("2".equals(visaOrderForm.getJiekuanStatus().trim())){
				sqlBuffer.append(" and (re.status = 2 or re.status = 3 )");
				//3 未借
			}if("3".equals(visaOrderForm.getJiekuanStatus().trim())){
				sqlBuffer.append(" and (re.status = 0 or re.status = 4 or re.status is null) ");
			}
		}
		//借款时间-开始时间
		if(null != visaOrderForm.getJiekuanTimeStart() && !"".equals(visaOrderForm.getJiekuanTimeStart().trim())){
			sqlBuffer.append(" and re.createDate >= '"+visaOrderForm.getJiekuanTimeStart()+" 00:00:00"+"' ");
		}
		//借款时间-结束时间
		if(null != visaOrderForm.getJiekuanTimeEnd() && !"".equals(visaOrderForm.getJiekuanTimeEnd().trim())){
			sqlBuffer.append(" and re.createDate <= '"+visaOrderForm.getJiekuanTimeEnd()+" 23:59:59"+"' ");
		}
		//预计出团时间-开始时间
		if(null != visaOrderForm.getForecastStartOutStart() && !"".equals(visaOrderForm.getForecastStartOutStart().trim())){
			sqlBuffer.append(" and v.forecast_start_out >= '"+visaOrderForm.getForecastStartOutStart()+" 00:00:00"+"' ");
			whereMap.put("traveler", true);
			whereMap.put("visa", true);
		}
		//预计出团时间-结束时间
		if(null != visaOrderForm.getForecastStartOutEnd () && !"".equals(visaOrderForm.getForecastStartOutEnd().trim())){
			sqlBuffer.append(" and v.forecast_start_out <= '"+visaOrderForm.getForecastStartOutEnd()+" 23:59:59"+"' ");
			whereMap.put("traveler", true);
			whereMap.put("visa", true);
		}
		//预计约签时间-开始时间
		if(null != visaOrderForm.getForecastContractStart() && !"".equals(visaOrderForm.getForecastContractStart().trim())){
			sqlBuffer.append(" and v.forecast_contract >= '"+visaOrderForm.getForecastContractStart()+" 00:00:00"+"' ");
			whereMap.put("traveler", true);
			whereMap.put("visa", true);
		}
		//预计约签时间-结束时间
		if(null != visaOrderForm.getForecastContractEnd() && !"".equals(visaOrderForm.getForecastContractEnd().trim())){
			sqlBuffer.append(" and v.forecast_contract <= '"+visaOrderForm.getForecastContractEnd()+" 23:59:59"+"' ");
			whereMap.put("traveler", true);
			whereMap.put("visa", true);
		}
		//实际出团时间-开始时间
		if(null != visaOrderForm.getStartOutStart() && !"".equals(visaOrderForm.getStartOutStart().trim())){
			sqlBuffer.append(" and v.start_out >= '"+visaOrderForm.getStartOutStart()+" 00:00:00"+"' ");
			whereMap.put("traveler", true);
			whereMap.put("visa", true);
		}
		//实际出团时间-结束时间
		if(null != visaOrderForm.getStartOutEnd() && !"".equals(visaOrderForm.getStartOutEnd().trim())){
			sqlBuffer.append(" and v.start_out <= '"+visaOrderForm.getStartOutEnd()+" 23:59:59"+"' ");
			whereMap.put("traveler", true);
			whereMap.put("visa", true);
		}
		//实际签约时间-开始时间
		if(null != visaOrderForm.getContractStart() && !"".equals(visaOrderForm.getContractStart().trim())){
			sqlBuffer.append(" and v.contract >= '"+visaOrderForm.getContractStart()+" 00:00:00"+"' ");
			whereMap.put("traveler", true);
			whereMap.put("visa", true);
		}
		//实际签约时间-结束时间
		if(null != visaOrderForm.getContractEnd() && !"".equals(visaOrderForm.getContractEnd().trim())){
			sqlBuffer.append(" and v.contract <= '"+visaOrderForm.getContractEnd()+" 23:59:59"+"' ");
			whereMap.put("traveler", true);
			whereMap.put("visa", true);
		}
		
		//说明会时间-开始时间
		if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingStart())){
			sqlBuffer.append(" and vv.explanationTime >= '"+visaOrderForm.getExplanationMeetingStart()+":00"+"' ");
			whereMap.put("vv", true);
		}
		//说明会时间-结束时间
		if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingEnd())){
			sqlBuffer.append(" and vv.explanationTime <= '"+visaOrderForm.getExplanationMeetingEnd()+":59"+"' ");
			whereMap.put("vv", true);
		}
		
		//下单时间-开始时间
		if(null != visaOrderForm.getCreateDateStart() && !"".equals(visaOrderForm.getCreateDateStart().trim())){
			sqlBuffer.append(" and vo.create_date >= '"+visaOrderForm.getCreateDateStart()+" 00:00:00"+"' ");
		}
		//下单时间-结束时间
		if(null != visaOrderForm.getCreateDateEnd() && !"".equals(visaOrderForm.getCreateDateEnd().trim())){
			sqlBuffer.append(" and vo.create_date <= '"+visaOrderForm.getCreateDateEnd()+" 23:59:59"+"' ");
		}
		//销售ID
		if(null != visaOrderForm.getSalerId() && !"".equals(visaOrderForm.getSalerId().trim())){
			sqlBuffer.append(" and vo.salerId = '"+visaOrderForm.getSalerId()+"' ");
		}
		//发票状态
		if(null != visaOrderForm.getInvoiceStatus() && !"".equals(visaOrderForm.getInvoiceStatus().trim())){
			//1已开发票2未开发票
			if("1".equals(visaOrderForm.getInvoiceStatus().trim())){
				sqlBuffer.append(" and oin.verifyStatus is not null ");
			}if("2".equals(visaOrderForm.getInvoiceStatus().trim())){
				sqlBuffer.append(" and oin.verifyStatus is null ");
			}
		}
		//收据状态
		if(null != visaOrderForm.getReceiptStatus() && !"".equals(visaOrderForm.getReceiptStatus().trim())){
			//1已开收据2未开收据
			if("1".equals(visaOrderForm.getReceiptStatus().trim())){
				sqlBuffer.append(" and ore.verifyStatus is not null ");
			}if("2".equals(visaOrderForm.getReceiptStatus().trim())){
				sqlBuffer.append(" and ore.verifyStatus is null ");
			}
		}
		//渠道结算方式
		if(null != visaOrderForm.getPaymentType() && !"".equals(visaOrderForm.getPaymentType().trim())){
			sqlBuffer.append(" and vo.agentinfo_id in (select id from agentinfo where paymentType = "+visaOrderForm.getPaymentType()+") ");
		}
		
		return sqlBuffer;
	}
	
	/**
	 * 签证订单列表展开游客，根据订单ID查询游客信息(新)
	 * @param orderNo 订单编号
	 * @return List<VisaOrderTravelerResultForm> 游客信息
	 */
	public List<Map<String,Object>> searchTravelerByOrderId(String orderId,String shenfen){
		
		//签务身份管理订单,根据订单号的列表查询游客信息
		StringBuffer sqlBuffer = new StringBuffer();
		
		sqlBuffer.append(" SELECT ");
		sqlBuffer.append(" t.name travelerName, ");//0游客名称
		sqlBuffer.append(" t.passportCode passportId, ");//1护照编号
		sqlBuffer.append(" v.AA_code aACode, ");//2AA码
		sqlBuffer.append(" vp.visaType visaType, ");//3签证类型
		sqlBuffer.append(" sc.countryName_cn visaCountry, ");//4签证国家
		sqlBuffer.append(" v.forecast_start_out forecastStartOut, ");//5预定出团时间
		sqlBuffer.append(" v.forecast_contract forecastContract, ");//6预计签约时间
		sqlBuffer.append(" v.start_out startOut, ");//7实际出团时间
		sqlBuffer.append(" v.contract contract, ");//8实际签约时间
		sqlBuffer.append(" v.visa_stauts visaStatus, ");//9签证状态
		sqlBuffer.append(" t.passportStatus passportStatus, ");//10护照状态
		sqlBuffer.append(" v.guarantee_status guaranteeStatus, ");//11担保类型
		sqlBuffer.append(" v.total_deposit totalDepositUUID, ");//12应收押金UUID
		sqlBuffer.append(" v.payed_deposit payedDepositUUID, ");//已收押金
		sqlBuffer.append(" v.accounted_deposit accountedDeposit, ");//13达账押金UUID
		sqlBuffer.append(" t.id id, ");//14游客主键id
		sqlBuffer.append(" v.id visaId, ");//15 visa表的id
		sqlBuffer.append(" vp.id visaProductId, ");//16 签证产品id
		sqlBuffer.append(" vo.id visaorderId, ");//17 visa_order表id
		sqlBuffer.append(" vo.order_no visaorderNo,vo.mainOrderId main_orderId, ");//18 visa_order表的订单编号
		sqlBuffer.append(" vo.agentinfo_id agentinfoId, ");//19 visa_order表渠道IDagentId
		sqlBuffer.append(" v.payed_deposit payedDeposit, ");//20  已付押金UUID
		sqlBuffer.append(" su.name creatUser, ");//21下单人
		sqlBuffer.append(" vo.create_date createTime, ");//22下单时间
		sqlBuffer.append(" po.orderStatus groupType, ");//23参团类型
		sqlBuffer.append(" vo.lockStatus lockStatus, ");//24订单锁死状态
		sqlBuffer.append(" t.payPriceSerialNum payPriceSerialNum, ");//25游客的应收uuid
		sqlBuffer.append(" t.payed_moneySerialNum payedMoneySerialNum, ");//26游客的已付uuid
		sqlBuffer.append(" t.main_order_id mainOrderId, ");//27游客的主订单id
		sqlBuffer.append(" po.orderNum mainOrderNum, ");//28游客的主订单编号
		sqlBuffer.append(" ag.groupCode cantuantuanhao, ");//29游客的参团团号
		sqlBuffer.append(" t.accounted_money accountedMoney, ");//30游客的达账uuid
		sqlBuffer.append(" t.payment_type paymentType, ");//31游客的结算方式
		sqlBuffer.append(" v.actual_delivery_time deliveryTime, ");//32 实际送签时间
		sqlBuffer.append(" vp.lockStatus activityLockStatus, ");//33 visa_products锁死状态
		sqlBuffer.append(" vp.collarZoning collarZoning, ");//34 领区
		sqlBuffer.append(" vo.update_date updateDate, ");//35更新时间
		sqlBuffer.append(" re.status status, ");//36review状态
		sqlBuffer.append(" MAX(vin.explanation_time) explanationTime ");//37 （最大）说明会时间
		sqlBuffer.append(" FROM traveler t ");
		sqlBuffer.append(" LEFT JOIN visa v on t.id = v.traveler_id ");
		sqlBuffer.append(" LEFT JOIN visa_order vo on vo.id = t.orderId ");
		sqlBuffer.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ");
		sqlBuffer.append(" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id ");
		sqlBuffer.append(" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId ");
		sqlBuffer.append(" LEFT JOIN sys_user su on su.id = vo.create_by ");
		sqlBuffer.append(" LEFT JOIN productorder po ");
		sqlBuffer.append(" on po.id = t.main_order_id ");
		sqlBuffer.append(" LEFT JOIN activitygroup ag ");
		sqlBuffer.append(" on ag.id = po.productGroupId ");
		if(UserUtils.getUser().getCompany().getId()==68){
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}else if(UserUtils.getUser().getCompany().getId()==71) {
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
		}else{
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}
		//查询新建面签通知时填写的说明会时间
		sqlBuffer.append("LEFT JOIN visa_interview_notice_traveler vint on t.id = vint.travaler_id ");
		sqlBuffer.append("LEFT JOIN visa_interview_notice vin on vint.interview_id = vin.id and vin.del_flag='0' ");
		
		sqlBuffer.append(" where t.order_type = 6 and t.companyId = " + UserUtils.getUser().getCompany().getId() + " and t.delFlag=0 and vo.visa_order_status != 100 and vo.del_flag = 0 and vo.create_by IS NOT NULL ");;
		sqlBuffer.append(" and a.status = 1 ");
		
		//只能看本供应商下的签证订单
		sqlBuffer.append(" and su.companyId = "+UserUtils.getUser().getCompany().getId()+" ");
		sqlBuffer.append(" and t.orderId = "+orderId);
		//依游客id分组以便查询该游客最大说明会时间
		sqlBuffer.append(" GROUP BY t.id");
		
		List<Map<String,Object>> travelerList = visaOrderDao.findBySql(sqlBuffer.toString(),Map.class);
		
		//游客数据处理
		travelerData(travelerList,shenfen);
		
		return travelerList;
	}

	/**
	 * 担保变更记录列表
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> getTravelerByOrderId(Long orderId){

		String shenfen = "xiaoshou";
		Long companyId = UserUtils.getUser().getCompany().getId();

		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" SELECT ");
		sqlBuffer.append(" t.name travelerName, ");//0游客名称
		sqlBuffer.append(" t.passportCode passportId, ");//1护照编号
		sqlBuffer.append(" v.AA_code aACode, ");//2AA码
		sqlBuffer.append(" vp.visaType visaType, ");//3签证类型
		sqlBuffer.append(" sc.countryName_cn visaCountry, ");//4签证国家
		sqlBuffer.append(" v.forecast_start_out forecastStartOut, ");//5预定出团时间
		sqlBuffer.append(" v.forecast_contract forecastContract, ");//6预计签约时间
		sqlBuffer.append(" v.start_out startOut, ");//7实际出团时间
		sqlBuffer.append(" v.contract contract, ");//8实际签约时间
		sqlBuffer.append(" v.visa_stauts visaStatus, ");//9签证状态
		sqlBuffer.append(" t.passportStatus passportStatus, ");//10护照状态
		sqlBuffer.append(" v.guarantee_status guaranteeStatus, ");//11担保类型
		sqlBuffer.append(" v.total_deposit totalDepositUUID, ");//12应收押金UUID
		sqlBuffer.append(" v.accounted_deposit accountedDeposit, ");//13达账押金UUID
		sqlBuffer.append(" t.id id, ");//14游客主键id
		sqlBuffer.append(" v.id visaId, ");//15 visa表的id
		sqlBuffer.append(" vp.id visaProductId, ");//16 签证产品id
		sqlBuffer.append(" vo.id visaorderId, ");//17 visa_order表id
		sqlBuffer.append(" vo.order_no visaorderNo,vo.mainOrderId main_orderId, ");//18 visa_order表的订单编号
		sqlBuffer.append(" vo.agentinfo_id agentinfoId, ");//19 visa_order表渠道IDagentId
		sqlBuffer.append(" vo.salerId, ");
		sqlBuffer.append(" vo.salerName, ");
		sqlBuffer.append(" v.payed_deposit payedDeposit, ");//20  已付押金UUID
		sqlBuffer.append(" su.name creatUser, ");//21下单人
		sqlBuffer.append(" vo.create_date createTime, ");//22下单时间
		sqlBuffer.append(" po.orderStatus groupType, ");//23参团类型
		sqlBuffer.append(" vo.lockStatus lockStatus, ");//24订单锁死状态
		sqlBuffer.append(" t.payPriceSerialNum payPriceSerialNum, ");//25游客的应收uuid
		sqlBuffer.append(" t.payed_moneySerialNum payedMoneySerialNum, ");//26游客的已付uuid
		sqlBuffer.append(" t.main_order_id mainOrderId, ");//27游客的主订单id
		sqlBuffer.append(" po.orderNum mainOrderNum, ");//28游客的主订单编号
		sqlBuffer.append(" ag.groupCode cantuantuanhao, ");//29游客的参团团号
		sqlBuffer.append(" t.accounted_money accountedMoney, ");//30游客的达账uuid
		sqlBuffer.append(" t.payment_type paymentType, ");//31游客的结算方式
		sqlBuffer.append(" v.actual_delivery_time deliveryTime, ");//32 实际送签时间
		sqlBuffer.append(" vp.lockStatus activityLockStatus, ");//33 visa_products锁死状态
		sqlBuffer.append(" vp.collarZoning collarZoning, ");//34 领区
		sqlBuffer.append(" vo.update_date updateDate, ");//35更新时间
		sqlBuffer.append(" re.status status, ");//36review状态
//		sqlBuffer.append(" MAX(vin.explanation_time) explanationTime, ");//37 （最大）说明会时间
		sqlBuffer.append(" rn.id reviewUuid, ");
		sqlBuffer.append(" rn.status reviewStatus, ");
		sqlBuffer.append(" rn.extend_1 newGuaranteeType, ");
		sqlBuffer.append(" rn.current_reviewer, ");
		sqlBuffer.append(" rn.create_date createDate, ");
		sqlBuffer.append(" rn.remark, ");
		sqlBuffer.append(" m.currencyId, ");
		sqlBuffer.append(" m.amount ");
		sqlBuffer.append(" FROM traveler t ");
		sqlBuffer.append(" LEFT JOIN visa v on t.id = v.traveler_id ");
		sqlBuffer.append(" LEFT JOIN visa_order vo on vo.id = t.orderId ");
		sqlBuffer.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ");
		sqlBuffer.append(" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id ");
		sqlBuffer.append(" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId ");
		sqlBuffer.append(" LEFT JOIN sys_user su on su.id = vo.create_by ");
		sqlBuffer.append(" LEFT JOIN productorder po ");
		sqlBuffer.append(" on po.id = t.main_order_id ");
		sqlBuffer.append(" LEFT JOIN activitygroup ag ");
		sqlBuffer.append(" on ag.id = po.productGroupId ");
		if(companyId == 68){
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}else if(companyId == 71) {
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
		}else{
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}
		//查询新建面签通知时填写的说明会时间
//		sqlBuffer.append("LEFT JOIN visa_interview_notice_traveler vint on t.id = vint.travaler_id ");
//		sqlBuffer.append("LEFT JOIN visa_interview_notice vin on vint.interview_id = vin.id and vin.del_flag='0' ");
		sqlBuffer.append("LEFT JOIN review_new rn on t.id = rn.traveller_id ");
		sqlBuffer.append("LEFT JOIN money_amount m on m.review_uuid = rn.id ");
		sqlBuffer.append(" where ");
//		if ("list".equals(list)) {
			sqlBuffer.append("t.id = rn.traveller_id and ");
			sqlBuffer.append("rn.process_type = 22 and ");
//		}
//		if ("approval".equals(flag)) {
//			sqlBuffer.append("t.id = rn.traveller_id and ");
//			if (StringUtils.isNotBlank(list)) {
//				sqlBuffer.append("t.id = " + list + " and ");
//			}
//		}
		sqlBuffer.append("t.order_type = 6 and t.companyId = " + companyId + " and t.delFlag=0 and vo.visa_order_status != 100 and vo.del_flag = 0 and vo.create_by IS NOT NULL ");;
		sqlBuffer.append(" and a.status = 1 ");

		//只能看本供应商下的签证订单
		sqlBuffer.append(" and su.companyId = " + companyId +" ");
		sqlBuffer.append(" and t.orderId = " + orderId);
		//依游客id分组以便查询该游客最大说明会时间
//		sqlBuffer.append(" GROUP BY t.id");
		sqlBuffer.append(" order by rn.create_date desc");

		List<Map<String,Object>> travelerList = visaOrderDao.findBySql(sqlBuffer.toString(),Map.class);

		//游客数据处理
		travelerData(travelerList,shenfen);

		return travelerList;
	}

	/**
	 * 担保变更申请--游客信息
	 * @param orderId
	 * @param list "list"表示担保变更申请记录列表，只查询该订单下申请了担保变更的游客
	 * @param flag approval表示审批页面，只查询选择的游客信息，此时list传值为travelerId
	 * @return
	 */
	public List<Map<String,Object>> getTravelerByOrderId(Long orderId, String travelerId, String flag, String review){

		String shenfen = "xiaoshou";
		Long companyId = UserUtils.getUser().getCompany().getId();

		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" SELECT ");
		sqlBuffer.append(" t.name travelerName, ");//0游客名称
		sqlBuffer.append(" t.passportCode passportId, ");//1护照编号
		sqlBuffer.append(" v.AA_code aACode, ");//2AA码
		sqlBuffer.append(" vp.visaType visaType, ");//3签证类型
		sqlBuffer.append(" sc.countryName_cn visaCountry, ");//4签证国家
		sqlBuffer.append(" v.forecast_start_out forecastStartOut, ");//5预定出团时间
		sqlBuffer.append(" v.forecast_contract forecastContract, ");//6预计签约时间
		sqlBuffer.append(" v.start_out startOut, ");//7实际出团时间
		sqlBuffer.append(" v.contract contract, ");//8实际签约时间
		sqlBuffer.append(" v.visa_stauts visaStatus, ");//9签证状态
		sqlBuffer.append(" t.passportStatus passportStatus, ");//10护照状态
		sqlBuffer.append(" v.guarantee_status guaranteeStatus, ");//11担保类型
		sqlBuffer.append(" v.total_deposit totalDepositUUID, ");//12应收押金UUID
		sqlBuffer.append(" v.accounted_deposit accountedDeposit, ");//13达账押金UUID
		sqlBuffer.append(" t.id id, ");//14游客主键id
		sqlBuffer.append(" v.id visaId, ");//15 visa表的id
		sqlBuffer.append(" vp.id visaProductId, ");//16 签证产品id
		sqlBuffer.append(" vo.id visaorderId, ");//17 visa_order表id
		sqlBuffer.append(" vo.order_no visaorderNo,vo.mainOrderId main_orderId, ");//18 visa_order表的订单编号
		sqlBuffer.append(" vo.agentinfo_id agentinfoId, ");//19 visa_order表渠道IDagentId
		sqlBuffer.append(" vo.salerId, ");
		sqlBuffer.append(" vo.salerName, ");
		sqlBuffer.append(" v.payed_deposit payedDeposit, ");//20  已付押金UUID
		sqlBuffer.append(" su.name creatUser, ");//21下单人
		sqlBuffer.append(" vo.create_date createTime, ");//22下单时间
		sqlBuffer.append(" po.orderStatus groupType, ");//23参团类型
		sqlBuffer.append(" vo.lockStatus lockStatus, ");//24订单锁死状态
		sqlBuffer.append(" t.payPriceSerialNum payPriceSerialNum, ");//25游客的应收uuid
		sqlBuffer.append(" t.payed_moneySerialNum payedMoneySerialNum, ");//26游客的已付uuid
		sqlBuffer.append(" t.main_order_id mainOrderId, ");//27游客的主订单id
		sqlBuffer.append(" po.orderNum mainOrderNum, ");//28游客的主订单编号
		sqlBuffer.append(" ag.groupCode cantuantuanhao, ");//29游客的参团团号
		sqlBuffer.append(" t.accounted_money accountedMoney, ");//30游客的达账uuid
		sqlBuffer.append(" t.payment_type paymentType, ");//31游客的结算方式
		sqlBuffer.append(" v.actual_delivery_time deliveryTime, ");//32 实际送签时间
		sqlBuffer.append(" vp.lockStatus activityLockStatus, ");//33 visa_products锁死状态
		sqlBuffer.append(" vp.collarZoning collarZoning, ");//34 领区
		sqlBuffer.append(" vo.update_date updateDate, ");//35更新时间
		sqlBuffer.append(" re.status status, ");//36review状态
//		sqlBuffer.append(" MAX(vin.explanation_time) explanationTime, ");//37 （最大）说明会时间
		sqlBuffer.append(" rn.id reviewUuid, ");
		sqlBuffer.append(" rn.status reviewStatus, ");
		sqlBuffer.append(" rn.extend_1 newGuaranteeType, ");
		sqlBuffer.append(" rn.current_reviewer, ");
		sqlBuffer.append(" rn.create_date createDate, ");
		sqlBuffer.append(" rn.remark, ");
		sqlBuffer.append(" m.currencyId, ");
		sqlBuffer.append(" m.amount ");
		sqlBuffer.append(" FROM traveler t ");
		sqlBuffer.append(" LEFT JOIN visa v on t.id = v.traveler_id ");
		sqlBuffer.append(" LEFT JOIN visa_order vo on vo.id = t.orderId ");
		sqlBuffer.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ");
		sqlBuffer.append(" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id ");
		sqlBuffer.append(" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId ");
		sqlBuffer.append(" LEFT JOIN sys_user su on su.id = vo.create_by ");
		sqlBuffer.append(" LEFT JOIN productorder po ");
		sqlBuffer.append(" on po.id = t.main_order_id ");
		sqlBuffer.append(" LEFT JOIN activitygroup ag ");
		sqlBuffer.append(" on ag.id = po.productGroupId ");
		if(companyId == 68){
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}else if(companyId == 71) {
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
		}else{
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}
		//查询新建面签通知时填写的说明会时间
//		sqlBuffer.append("LEFT JOIN visa_interview_notice_traveler vint on t.id = vint.travaler_id ");
//		sqlBuffer.append("LEFT JOIN visa_interview_notice vin on vint.interview_id = vin.id and vin.del_flag='0' ");
		sqlBuffer.append("LEFT JOIN review_new rn on t.id = rn.traveller_id ");

		sqlBuffer.append("LEFT JOIN money_amount m on m.review_uuid = rn.id ");
		sqlBuffer.append(" where ");
//		if ("list".equals(list)) {
//			sqlBuffer.append("t.id = rn.traveller_id and ");
//			sqlBuffer.append("rn.process_type = 22 and ");
//		}
		if ("1".equals(review)) {
			sqlBuffer.append(" t.review_uuid = rn.id and ");
            sqlBuffer.append(" t.id = " + travelerId + " and ");
		}
		if ("approval".equals(flag)) {
			sqlBuffer.append("t.id = rn.traveller_id and ");
			if (StringUtils.isNotBlank(travelerId)) {
				sqlBuffer.append("t.id = " + travelerId + " and ");
			}
		}
        if ("detail".equals(flag) || "approval".equals(flag)) {
            sqlBuffer.append(" t.review_uuid = rn.id and ");
        }
		sqlBuffer.append("t.order_type = 6 and t.companyId = " + companyId + " and t.delFlag=0 and vo.visa_order_status != 100 and vo.del_flag = 0 and vo.create_by IS NOT NULL ");;
		sqlBuffer.append(" and a.status = 1 ");

		//只能看本供应商下的签证订单
		sqlBuffer.append(" and su.companyId = " + companyId +" ");
		sqlBuffer.append(" and t.orderId = " + orderId);
		//依游客id分组以便查询该游客最大说明会时间
		sqlBuffer.append(" GROUP BY t.id");

		List<Map<String,Object>> travelerList = visaOrderDao.findBySql(sqlBuffer.toString(),Map.class);

		//游客数据处理
		travelerData(travelerList,shenfen);

		return travelerList;
	}

	public Page<Map<String, Object>> getGuaranteeReviewList(Page<Map<String, Object>> page, Map<String, String> paramsMap){

		String shenfen = "xiaoshou";
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		Long userId = user.getId();

		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" SELECT ")
			.append(" t.name travelerName, ")//0游客名称
			.append(" t.passportCode passportId, ")//1护照编号
			.append(" v.AA_code aACode, ")//2AA码
			.append(" vp.visaType visaType, ")//3签证类型
			.append(" sc.countryName_cn visaCountry, ")//4签证国家
			.append(" v.forecast_start_out forecastStartOut, ")//5预定出团时间
			.append(" v.forecast_contract forecastContract, ")//6预计签约时间
			.append(" v.start_out startOut, ")//7实际出团时间
			.append(" v.contract contract, ")//8实际签约时间
			.append(" v.visa_stauts visaStatus, ")//9签证状态
			.append(" t.passportStatus passportStatus, ")//10护照状态
			.append(" v.guarantee_status guaranteeStatus, ")//11担保类型
			.append(" v.total_deposit totalDepositUUID, ")//12应收押金UUID
			.append(" v.accounted_deposit accountedDeposit, ")//13达账押金UUID
			.append(" t.id id, ")//14游客主键id
			.append(" v.id visaId, ")//15 visa表的id
			.append(" vp.id visaProductId, ")//16 签证产品id
				.append(" vp.productName, ")
				.append(" vp.groupCode, ")
				.append(" vo.id visaorderId, ")//17 visa_order表id
				.append(" vo.order_no visaorderNo,vo.mainOrderId main_orderId, ")//18 visa_order表的订单编号
				.append(" vo.agentinfo_id agentinfoId, ")//19 visa_order表渠道IDagentId
				.append(" vo.salerId, ")
				.append(" vo.salerName, ")
				.append(" v.payed_deposit payedDeposit, ")//20  已付押金UUID
				.append(" su.name creatUser, ")//21下单人
				.append(" vo.create_date createTime, ")//22下单时间
				.append(" po.orderStatus groupType, ")//23参团类型
				.append(" vo.lockStatus lockStatus, ")//24订单锁死状态
				.append(" t.payPriceSerialNum payPriceSerialNum, ")//25游客的应收uuid
				.append(" t.payed_moneySerialNum payedMoneySerialNum, ")//26游客的已付uuid
				.append(" t.main_order_id mainOrderId, ")//27游客的主订单id
				.append(" po.orderNum mainOrderNum, ")//28游客的主订单编号
				.append(" ag.groupCode cantuantuanhao, ")//29游客的参团团号
				.append(" t.accounted_money accountedMoney, ")//30游客的达账uuid
				.append(" t.payment_type paymentType, ")//31游客的结算方式
				.append(" v.actual_delivery_time deliveryTime, ")//32 实际送签时间
				.append(" vp.lockStatus activityLockStatus, ")//33 visa_products锁死状态
				.append(" vp.collarZoning collarZoning, ")//34 领区
				.append(" rn.update_date updateDate, ")//35更新时间
				.append(" re.status status, ")//36review状态
//				.append(" MAX(vin.explanation_time) explanationTime, ")//37 （最大）说明会时间
				.append(" rn.id reviewUuid, ")
				.append(" rn.status reviewStatus, ")
				.append(" rn.extend_1 newGuaranteeType, ")
				.append(" rn.last_reviewer, ")
				.append(" rn.current_reviewer, ")
				.append(" rn.create_by createBy, ")
				.append(" rn.create_date createDate, ")
				.append(" rn.remark, ")
				.append(" m.currencyId, ")
				.append(" m.amount ")
				.append(" FROM traveler t ")
				.append(" LEFT JOIN visa v on t.id = v.traveler_id ")
				.append(" LEFT JOIN visa_order vo on vo.id = t.orderId ")
				.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ")
				.append(" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id ")
				.append(" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId ")
				.append(" LEFT JOIN sys_user su on su.id = vo.create_by ")
				.append(" LEFT JOIN productorder po ")
				.append(" on po.id = t.main_order_id ")
				.append(" LEFT JOIN activitygroup ag ")
				.append(" on ag.id = po.productGroupId ");
		if(companyId == 68){
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}else if(companyId == 71) {
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
		}else{
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}
		//查询新建面签通知时填写的说明会时间
//		sqlBuffer.append("LEFT JOIN visa_interview_notice_traveler vint on t.id = vint.travaler_id ")
//			.append("LEFT JOIN visa_interview_notice vin on vint.interview_id = vin.id and vin.del_flag='0' ")
		sqlBuffer.append("LEFT JOIN review_new rn on t.id = rn.traveller_id ")
			.append("LEFT JOIN money_amount m on m.review_uuid = rn.id ")
			.append(" where t.id = rn.traveller_id and ")
			.append("t.order_type = 6 and t.companyId = " + companyId + " and t.delFlag=0 and vo.visa_order_status != 100 and vo.del_flag = 0 and vo.create_by IS NOT NULL ")
			.append(" and a.status = 1 ")
			//只能看本供应商下的签证订单
			.append(" and su.companyId = " + companyId + " ")
//			.append(" and t.orderId = " + orderId)
			.append(" and rn.process_type = " + Context.REVIEW_FLOWTYPE_GUARANTEE);
		String wholeSalerKey = paramsMap.get("wholeSalerKey");
		if (StringUtils.isNotBlank(wholeSalerKey)) {
			sqlBuffer.append(" and (vp.groupCode like '%" + wholeSalerKey + "%' or vo.order_no like '%" + wholeSalerKey + "%') ");
		}
		String createBy = paramsMap.get("createBy");
		if (StringUtils.isNotBlank(createBy)) {
			sqlBuffer.append(" and rn.create_by = " + createBy + " ");
		}
		String agentInfo = paramsMap.get("agentInfo");
		if (StringUtils.isNotBlank(agentInfo)) {
			sqlBuffer.append(" and vo.agentinfo_id = " + agentInfo + " ");
		}
		String travelerName = paramsMap.get("travelerName");
		if (StringUtils.isNotBlank(travelerName)) {
			sqlBuffer.append(" and t.name like '%" + travelerName + "%' ");
		}
		String reviewStatus = paramsMap.get("reviewStatus");
		if (StringUtils.isNotBlank(reviewStatus)) {
			sqlBuffer.append(" and rn.status = " + reviewStatus + " ");
		}
		String createDateStart = paramsMap.get("createDateStart");
		if (StringUtils.isNotBlank(createDateStart)) {
			sqlBuffer.append(" and rn.create_date >= '" + createDateStart + "' ");
		}
		String createDateEnd = paramsMap.get("createDateEnd");
		if (StringUtils.isNotBlank(createDateEnd)) {
			sqlBuffer.append(" and rn.create_date <= '" + createDateEnd + "' ");
		}
		String reviewer = paramsMap.get("reviewer");
		if(reviewer != null) {
			if(Context.REVIEW_TAB_ALL.equals(reviewer)) {//全部
				sqlBuffer.append(" and FIND_IN_SET ('" + userId + "', all_reviewer)");
			}else if(Context.REVIEW_TAB_TO_BE_REVIEWED.equals(reviewer)) {//待本人审批
				sqlBuffer.append(" and rn.status = 1 and FIND_IN_SET ('" + userId + "', current_reviewer) ");
			}else if(Context.REVIEW_TAB_REVIEWED.equals(reviewer)) {//本人已审批
				sqlBuffer.append(" and rn.id in (select log.review_id from review_log_new log where (log.operation = 1 or log.operation=2) and active_flag = 1 and create_by = " + userId + ")");
			}else if(Context.REVIEW_TAB_OTHER_REVIEWED.equals(reviewer)) {//非本人审批
				sqlBuffer.append(" and not FIND_IN_SET ('" + userId + "', all_reviewer) ");
			}
		}
			//依游客id分组以便查询该游客最大说明会时间
//		sqlBuffer.append(" GROUP BY t.id");

		if(StringUtils.isBlank(page.getOrderBy())) {
			page.setOrderBy("updateDate desc");
		}

		Page<Map<String,Object>> travelerList = visaOrderDao.findBySql(page, sqlBuffer.toString(),Map.class);

		//游客数据处理
		travelerData(travelerList.getList(),shenfen);

		return travelerList;
	}
	
	/**
	 * 签证游客列表(新)
	 * @param visaOrder
	 */
	public Page<Map<String,Object>> searchTravelList(HttpServletRequest request,HttpServletResponse response,
			VisaOrderForm visaOrderForm, String shenfen, DepartmentCommon common) {
		
		//签证查询订单信息
		StringBuffer sqlBuffer = new StringBuffer();
		
		sqlBuffer.append(" SELECT ");
		sqlBuffer.append(" t.name travelerName, ");//0游客名称
		sqlBuffer.append(" t.passportCode passportId, ");//1护照编号
		sqlBuffer.append(" v.AA_code aACode, ");//2AA码
		sqlBuffer.append(" vp.visaType visaType, ");//3签证类型
		sqlBuffer.append(" sc.countryName_cn visaCountry, ");//4签证国家
		sqlBuffer.append(" v.forecast_start_out forecastStartOut, ");//5预定出团时间
		sqlBuffer.append(" v.forecast_contract forecastContract, ");//6预计签约时间
		sqlBuffer.append(" v.start_out startOut, ");//7实际出团时间
		sqlBuffer.append(" v.contract contract, ");//8实际签约时间
		sqlBuffer.append(" v.visa_stauts visaStatus, ");//9签证状态
		sqlBuffer.append(" t.passportStatus passportStatus, ");//10护照状态
		sqlBuffer.append(" v.guarantee_status guaranteeStatus, ");//11担保类型
		sqlBuffer.append(" v.total_deposit totalDepositUUID, ");//12应收押金UUID
        sqlBuffer.append(" v.payed_deposit payedDepositUUID, ");
		sqlBuffer.append(" v.accounted_deposit accountedDeposit, ");//13达账押金UUID
		sqlBuffer.append(" t.id id, ");//14游客主键id
		sqlBuffer.append(" v.id visaId, ");//15 visa表的id
		sqlBuffer.append(" vp.id visaProductId, ");//16 签证产品id
		sqlBuffer.append(" vo.id visaorderId, ");//17 visa_order表id
		sqlBuffer.append(" vo.order_no visaorderNo,vo.mainOrderId main_orderId, ");//18 visa_order表的订单编号
		sqlBuffer.append(" vo.agentinfo_id agentinfoId, ");//19 visa_order表渠道IDagentId
		sqlBuffer.append(" v.payed_deposit payedDeposit, ");//20  已付押金UUID
		sqlBuffer.append(" su.name creatUser, ");//21下单人
		sqlBuffer.append(" vo.create_date createTime, ");//22下单时间
		sqlBuffer.append(" po.orderStatus groupType, ");//23参团类型
		sqlBuffer.append(" vo.lockStatus lockStatus, ");//24订单锁死状态
		sqlBuffer.append(" t.payPriceSerialNum payPriceSerialNum, ");//25游客的应收uuid
		sqlBuffer.append(" t.payed_moneySerialNum payedMoneySerialNum, ");//26游客的已付uuid
		sqlBuffer.append(" t.main_order_id mainOrderId, ");//27游客的主订单id
		sqlBuffer.append(" po.orderNum mainOrderNum, ");//28游客的主订单编号
		sqlBuffer.append(" ag.groupCode cantuantuanhao, ");//29游客的参团团号
		sqlBuffer.append(" t.accounted_money accountedMoney, ");//30游客的达账uuid
		sqlBuffer.append(" t.payment_type paymentType, ");//31游客的结算方式
		sqlBuffer.append(" v.actual_delivery_time deliveryTime, ");//32 实际送签时间
		sqlBuffer.append(" vp.lockStatus activityLockStatus, ");//33 visa_products锁死状态
		sqlBuffer.append(" vp.collarZoning collarZoning, ");//34 领区
		sqlBuffer.append(" vo.update_date updateDate, ");//35更新时间
		
		// 0615 需求  限定大洋国旅新增是否需要押金和是否需要上传文件
//		sqlBuffer.append(" v.visa_deposit visaDeposit, ");//36是否需要押金
//		sqlBuffer.append(" v.visa_datum visaDatum, ");//37是否上交资料
		if(UserUtils.getUser().getCompany().getId()==68){
			sqlBuffer.append(" IFNULL(re.STATUS ,re2.STATUS) STATUS, ");//36review状态
		} else {
			sqlBuffer.append(" re.STATUS STATUS, ");//36review状态
		}
		
		//37 （最大）说明会时间
		sqlBuffer.append(" MAX(vin.explanation_time) explanationTime ");
		
		sqlBuffer.append(" FROM traveler t ");
		sqlBuffer.append(" LEFT JOIN visa v on t.id = v.traveler_id ");
		sqlBuffer.append(" LEFT JOIN visa_order vo on vo.id = t.orderId ");
		sqlBuffer.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ");
		sqlBuffer.append(" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id ");
		sqlBuffer.append(" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId ");
		sqlBuffer.append(" LEFT JOIN sys_user su on su.id = vo.create_by ");
		sqlBuffer.append(" LEFT JOIN productorder po ");
		sqlBuffer.append(" on po.id = t.main_order_id ");
		sqlBuffer.append(" LEFT JOIN activitygroup ag ");
		sqlBuffer.append(" on ag.id = po.productGroupId ");
		
		if(UserUtils.getUser().getCompany().getId()==68){
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr, (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 AND re1.companyId = 68 GROUP BY re1.travelerId) r WHERE rr.id = r.id) re ON t.id = re.travelerId ");
			sqlBuffer.append(" LEFT JOIN (SELECT rr.order_id orderId,rr.status status,rr.traveller_id travelerId,rr.create_date createDate FROM review_new rr, (SELECT MAX(re1.id_long) id FROM review_new re1 WHERE re1.process_type = '5' AND re1.product_type = '6' AND re1.company_id = '" + Context.SUPPLIER_UUID_HQX + "' GROUP BY re1.traveller_id) r WHERE rr.id_long = r.id) re2 ON concat(t.id) = re2.travelerId ");
			//解決bug 13826
			//sqlBuffer.append(" LEFT JOIN ((SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) union (SELECT rr.order_id orderId,rr.status status,rr.traveller_id travelerId,rr.create_date createDate FROM review_new rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review_new re1 WHERE re1.process_type = '5' AND re1.product_type = '6' GROUP BY re1.traveller_id) r ON rr.id = r.id)) re ON t.id = re.travelerId ");

		}else if(UserUtils.getUser().getCompany().getId()==71) {
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
		}else{
			//sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
			//解決bug 13098
			sqlBuffer.append(" LEFT JOIN ((SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) union (SELECT rr.order_id orderId,rr.status status,rr.traveller_id travelerId,rr.create_date createDate FROM review_new rr RIGHT JOIN (SELECT re1.id id,MAX(re1.id_long) FROM review_new re1 WHERE re1.process_type = '5' AND re1.product_type = '6' GROUP BY re1.traveller_id) r ON rr.id = r.id) ) re ON concat(t.id) = re.travelerId ");
		}
		//查询新建面签通知时填写的说明会时间
		sqlBuffer.append("LEFT JOIN visa_interview_notice_traveler vint on t.id = vint.travaler_id ");
		sqlBuffer.append("LEFT JOIN visa_interview_notice vin on vint.interview_id = vin.id and vin.del_flag='0' ");
		
		//部门ID不为空时拼接部门SQL
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
			departmentSQL(common,sqlBuffer,1);
		}
		
		sqlBuffer.append(" where t.order_type = 6 and t.companyId = " + UserUtils.getUser().getCompany().getId() + " and t.delFlag=0 and vo.visa_order_status != 100 and vo.del_flag = 0 and vo.create_by IS NOT NULL ");;
		sqlBuffer.append(" and a.status = 1 ");
		
		//部门ID不为空时拼接部门SQL的where条件
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
			departmentSQL(common,sqlBuffer,2);
		}
		
		boolean flag=false;
		/** 部门之间人员是否能相互查看，默认不能  add by 2016/01/18 for requirement ID 113*/
		boolean LisenceFlag = whetherSelectAllDeptDate(common);
		//销售签证订单过滤渠道 true:只能看自己渠道 false:看所有渠道
		if("xiaoshou".equals(shenfen)){
			String userType = UserUtils.getUser().getUserType();
			//如果不是销售经理或管理员，则用户只能查看自己负责的渠道
			if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)){
				boolean isManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_MANAGER);
				boolean isSaleManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES_EXECUTIVE);
				boolean isFinance = false;
				List<UserJob> list = userJobDao.getUserJobList(UserUtils.getUser().getId());
				for (UserJob userJob:list) {
					String jobName = userJob.getJobName();
					if(jobName.indexOf("财务") != -1){
						isFinance = true;
						break;
					}
				}
				if(isFinance){
					flag=false;
				}else if(!isManager && !isSaleManager) {
					flag=true;
				}else{
					flag=false;
				}
				
				//如果配置相互查看权限，则拥有销售经理权限
				if(LisenceFlag){
					flag=false;
				}
			}
		}
		if(flag){
			sqlBuffer.append("AND (a.agentSalerId="+UserUtils.getUser().getId()+" OR a.id=-1) ");
		}else{
			sqlBuffer.append("AND (a.supplyId="+UserUtils.getUser().getCompany().getId()+" OR a.id=-1) ");
		}
		
		//拼接筛选条件
		String mainOrderId = request.getParameter("mainOrderIdYouKe");
		searchTravlelerSQL(visaOrderForm,sqlBuffer,shenfen,mainOrderId);
		
		//只能看本供应商下的签证订单
		sqlBuffer.append(" and su.companyId = "+UserUtils.getUser().getCompany().getId()+" ");
		
		//依游客id分组以便查询该游客最大说明会时间
		sqlBuffer.append(" GROUP BY t.id ");
		//筛选说明会时间
		if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingStart()) || StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingEnd())){
			//说明会时间-开始时间
			if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingStart())){
				sqlBuffer.append(" having MAX(vin.explanation_time) >= '"+visaOrderForm.getExplanationMeetingStart()+":00"+"' ");
				//说明会时间-结束时间
				if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingEnd())){
					sqlBuffer.append(" and MAX(vin.explanation_time) <= '"+visaOrderForm.getExplanationMeetingEnd()+":59"+"' ");
				}
			}else{
				//说明会时间-结束时间
				if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingEnd())){
					sqlBuffer.append(" having MAX(vin.explanation_time) <= '"+visaOrderForm.getExplanationMeetingEnd()+":59"+"' ");
				}
			}
		}
		if(StringUtils.isNotBlank(visaOrderForm.getPaymentType())){
			sqlBuffer.append(" and t.payment_type = "+visaOrderForm.getPaymentType()+" ");
		}
		
		//列表排序
		String orderBy = request.getParameter("youkeOrderBy");
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy("vo.create_date DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		
		//分页查询游客数据
		Page<Map<String,Object>> travelerList = visaOrderDao.findBySql(page, sqlBuffer.toString(),Map.class);
		
		//游客查询结果，页面展示数据处理
		travelerData(travelerList.getList(),shenfen);
		
		return travelerList;
	}
	
	//游客数据处理
	private List<Map<String,Object>> travelerData(List<Map<String,Object>> travelerList,String shenfen){
		
		if (travelerList != null && travelerList.size() > 0) {
			//历史数据（这些数据已支付不能再次支付）
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> oldVisaDatalist =  (List<Map<String,Object>>)getOldVisaData();
			//结算方式
			//List<Dict> paymentList = findDictByType("payment_type");
			//速度优化
			List<Map<String,Object>> paymentMapList = findDictByType4Map("payment_type");
			
			//签证类别
			//List<Dict> newVisaList = findDictByType("new_visa_type");
			//速度优化
			List<Map<String,Object>> newVisaMapList = findDictByType4Map("new_visa_type");
			
			//领区
			//List<Dict> fromAreaList = findDictByType("from_area");
			//速度优化
			List<Map<String,Object>> fromAreaMapList = findDictByType4Map("from_area");
			
			//游客数量循环
			for(int i = 0; i< travelerList.size();i++){
				//结算方式
				if(null != travelerList.get(i).get("paymentType") && !"".equals(travelerList.get(i).get("paymentType").toString()) && !"1".equals(travelerList.get(i).get("paymentType").toString())){
					if(null != paymentMapList && paymentMapList.size()>0){
						for(int j=0;j<paymentMapList.size();j++){
							Map<String,Object> dictmap = paymentMapList.get(j);
							if(dictmap.get("value").toString().equals(travelerList.get(i).get("paymentType").toString())){
								travelerList.get(i).put("paymentType", dictmap.get("label"));
								travelerList.get(i).put("paymentTypeFlag", dictmap.get("label"));
								break;
							}
						}
					}
				}else{
					travelerList.get(i).put("paymentType", "");
					travelerList.get(i).put("paymentTypeFlag", "");
				}
				//签证类别
				if(null != travelerList.get(i).get("visaType") && !"".equals(String.valueOf(travelerList.get(i).get("visaType")))){
					if(null != newVisaMapList && newVisaMapList.size()>0){
						for(int j=0;j<newVisaMapList.size();j++){
							Map<String,Object> dictmap =newVisaMapList.get(j);
							if(String.valueOf(travelerList.get(i).get("visaType")).equals(dictmap.get("value").toString())){
								travelerList.get(i).put("visaType", dictmap.get("label"));
								break;
							}
						}
					}
				}else{
					travelerList.get(i).put("visaType", "");
				}
				//领区
				if(null != travelerList.get(i).get("collarZoning") && !"".equals(String.valueOf(travelerList.get(i).get("collarZoning")))){
					if(null != fromAreaMapList && fromAreaMapList.size()>0){
						for(int j=0;j<fromAreaMapList.size();j++){
							Map<String,Object> dictmap =fromAreaMapList.get(j);
							if(String.valueOf(travelerList.get(i).get("collarZoning")).equals(dictmap.get("value").toString())){
								travelerList.get(i).put("collarZoning", dictmap.get("label"));
								break;
							}
						}
					}
				}else{
					travelerList.get(i).put("collarZoning", "");
				}
				//参团类型
				if(null != travelerList.get(i).get("groupType") && !"".equals(String.valueOf(travelerList.get(i).get("groupType")))){
					String groupType = travelerList.get(i).get("groupType").toString();
					travelerList.get(i).put("groupType", OrderCommonUtil.getChineseOrderType(groupType));
					travelerList.get(i).put("groupTypeId", groupType);
				}else{
					travelerList.get(i).put("groupType", "单办签");
					travelerList.get(i).put("groupTypeId", "6");
				}
				//预定出团时间
				if(null != travelerList.get(i).get("forecastStartOut") && !"".equals(String.valueOf(travelerList.get(i).get("forecastStartOut")))){
					String customDate = DateUtils.formatCustomDate((Date)travelerList.get(i).get("forecastStartOut"),"yyyy-MM-dd");
					travelerList.get(i).put("forecastStartOut", customDate);
				}else{
					travelerList.get(i).put("forecastStartOut", "");
				}
				//预计约签时间
				if(null != travelerList.get(i).get("forecastContract") && !"".equals(String.valueOf(travelerList.get(i).get("forecastContract")))){
					String customDate = DateUtils.formatCustomDate((Date)travelerList.get(i).get("forecastContract"),"yyyy-MM-dd");
					travelerList.get(i).put("forecastContract", customDate);
				}else{
					travelerList.get(i).put("forecastContract", "");
				}
				//实际出团时间
				if(null != travelerList.get(i).get("startOut") && !"".equals(String.valueOf(travelerList.get(i).get("startOut")))){
					String customDate = DateUtils.formatCustomDate((Date)travelerList.get(i).get("startOut"),"yyyy-MM-dd" );
					travelerList.get(i).put("startOut", customDate);
				}else{
					travelerList.get(i).put("startOut", "");
				}
				//实际约签时间
				if(null != travelerList.get(i).get("contract") && !"".equals(String.valueOf(travelerList.get(i).get("contract")))){
					String customDate = DateUtils.formatCustomDate((Date)travelerList.get(i).get("contract"),"yyyy-MM-dd HH:mm");
					travelerList.get(i).put("contract", customDate);
				}else{
					travelerList.get(i).put("contract", "");
				}
				//说明会时间
				if(null != travelerList.get(i).get("explanationTime") && !"".equals(String.valueOf(travelerList.get(i).get("explanationTime")))){
					String explanationTime = DateUtils.formatCustomDate((Date)travelerList.get(i).get("explanationTime"),"yyyy-MM-dd HH:mm");
					travelerList.get(i).put("explanationTime", explanationTime);
				}else{
					travelerList.get(i).put("explanationTime", "");
				}
				//实际送签时间	
				if(null != travelerList.get(i).get("deliveryTime") && !"".equals(String.valueOf(travelerList.get(i).get("deliveryTime")))){
					String customDate = DateUtils.formatCustomDate((Date)travelerList.get(i).get("deliveryTime"),"yyyy-MM-dd");
					travelerList.get(i).put("deliveryTime", customDate);
				}else{
					travelerList.get(i).put("deliveryTime", "");
				}
				//应收押金
				if(null != travelerList.get(i).get("totalDepositUUID") && !"".equals(String.valueOf(travelerList.get(i).get("totalDepositUUID")))){
					travelerList.get(i).put("totalDeposit", getMoney(travelerList.get(i).get("totalDepositUUID").toString(), null));
					travelerList.get(i).put("totalDepositUUID", String.valueOf(travelerList.get(i).get("totalDepositUUID")));
					//总押金数 用,相隔
					travelerList.get(i).put("totalYajinJine", getTotalMoney(String.valueOf(travelerList.get(i).get("totalDepositUUID"))).get("jine"));
					//总押金的货币id 用,相隔
					travelerList.get(i).put("totalYajinHuobi", getTotalMoney(String.valueOf(travelerList.get(i).get("totalDepositUUID"))).get("huobi"));
				}else{
					travelerList.get(i).put("totalDeposit", "");
					travelerList.get(i).put("totalDepositUUID", "");
					travelerList.get(i).put("totalYajinJine", "");
					travelerList.get(i).put("totalYajinHuobi", "");
				}
				//已收押金
				if(null != travelerList.get(i).get("payedDepositUUID") && !"".equals(String.valueOf(travelerList.get(i).get("payedDepositUUID")))){
					travelerList.get(i).put("payedDeposit", getMoney(travelerList.get(i).get("payedDepositUUID").toString(), null));
					travelerList.get(i).put("payedDepositUUID", String.valueOf(travelerList.get(i).get("payedDepositUUID")));
					//总押金数 用,相隔
					travelerList.get(i).put("payedYajinJine", getTotalMoney(String.valueOf(travelerList.get(i).get("payedDepositUUID"))).get("jine"));
					//总押金的货币id 用,相隔
					travelerList.get(i).put("payedYajinHuobi", getTotalMoney(String.valueOf(travelerList.get(i).get("payedDepositUUID"))).get("huobi"));
				}else{
					travelerList.get(i).put("payedDeposit", "");
					travelerList.get(i).put("payedDepositUUID", "");
					travelerList.get(i).put("payedYajinJine", "");
					travelerList.get(i).put("payedYajinHuobi", "");
				}
				//达账押金
				if(null != travelerList.get(i).get("accountedDeposit") && !"".equals(String.valueOf(travelerList.get(i).get("accountedDeposit")))){
					travelerList.get(i).put("accountedDeposit", getMoney(travelerList.get(i).get("accountedDeposit").toString(), null));
				}else{
					travelerList.get(i).put("accountedDeposit", "");
				}
				if(UserUtils.getUser().getCompany().getId()==71) {
					//游客借款状态
					//先用游客的应收金额和达账金额来判断 
					//相等就是 已还
					//不相等
						//查看是否有借款审核成功的记录,
							//存在的话 已结款
							//不存在的话 未借款
					//用travelerArray[26]代替达账金额的uuid,以后直接替换掉
					//达账金额不是空,进行差价对比,否则直接进行是否有借款申请的判断
					if(null != travelerList.get(i).get("accountedMoney") && !"".equals(travelerList.get(i).get("accountedMoney").toString())
							&& null == getChajia(travelerList.get(i).get("payPriceSerialNum").toString(), travelerList.get(i).get("accountedMoney").toString())){
						if(StringUtils.isBlank(getTravelerBorrowedMoney2(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString())))){
							//设置为 未借款	
							travelerList.get(i).put("jiekuanStatus", "未借");
						}else{
							travelerList.get(i).put("jiekuanStatus", "已还");//借款状态(1:未借2:已借3:已还)
						}
					}else{
						
						
						//----------------综合处理新旧审核的  借款状态问题-----------------
						boolean isNewReview  = false;
						String borrowMoneyandCheckStatus = getVisaOrderBorrowedMoney(travelerList.get(i).get("visaorderId").toString());
						if (null == borrowMoneyandCheckStatus) {
							borrowMoneyandCheckStatus  = getVisaOrderBorrowedMoney4Activiti(travelerList.get(i).get("visaorderId").toString());
							isNewReview  = true;
						}
						
						if (null != borrowMoneyandCheckStatus) {
							//借款
							String jiekuanStatus = borrowMoneyandCheckStatus;
							if("1".equals(jiekuanStatus)){
								//设置为 审批中	
								//travelerForm.setJiekuanStatus("审批中");
								
								//----------------综合处理新旧审核的  借款状态问题-----------------
								//Review review = getVisaOrderBorrowedStatusAndTime(travelerList.get(i).get("visaorderId").toString());
								//travelerList.get(i).put("jiekuanStatus", ReviewCommonUtil.getNextReview(review.getId()));
								Review review = null;
								ReviewNew reviewnew = null;
								if (!isNewReview) {
									review = getVisaOrderBorrowedStatusAndTime(travelerList.get(i).get("visaorderId").toString());
									travelerList.get(i).put("jiekuanStatus", ReviewCommonUtil.getNextReview(review.getId()));
								}else{
									reviewnew = getVisaOrderBorrowedStatusAndTime4Activiti(travelerList.get(i).get("visaorderId").toString());
									travelerList.get(i).put("jiekuanStatus", ReviewUtils.getChineseReviewStatusByUuid(reviewnew.getId()));
								}
								
								
								if(null != review ){
									String customDate = DateUtils.formatCustomDate((Date)review.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								if(null != reviewnew ){
									String customDate = DateUtils.formatCustomDate((Date)reviewnew.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
							//2 表示审核成功 设置已 借款	
							}else if("2".equals(jiekuanStatus)){
								//travelerForm.setJiekuanStatus("已借");//借款状态(1:未借2:已借3:已还)
								
								//----------------综合处理新旧审核的  借款状态问题-----------------
								//Review review = getVisaOrderBorrowedStatusAndTime(travelerList.get(i).get("visaorderId").toString());
								//travelerList.get(i).put("jiekuanStatus", ReviewCommonUtil.getNextReview(review.getId()));
								
								Review review = null;
								ReviewNew reviewnew = null;
								
								if (!isNewReview) {
									 review = getVisaOrderBorrowedStatusAndTime(travelerList.get(i).get("visaorderId").toString());
									 travelerList.get(i).put("jiekuanStatus", ReviewCommonUtil.getNextReview(review.getId()));
								}else{
									reviewnew = getVisaOrderBorrowedStatusAndTime4Activiti(travelerList.get(i).get("visaorderId").toString());
									travelerList.get(i).put("jiekuanStatus", ReviewUtils.getChineseReviewStatusByUuid(reviewnew.getId()));
								}
								
								if(null != review ){
									String customDate = DateUtils.formatCustomDate((Date)review.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
								if(null != reviewnew ){
									String customDate = DateUtils.formatCustomDate((Date)reviewnew.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
							}else if("3".equals(jiekuanStatus)){
								//设置为 已还	
								//travelerForm.setJiekuanStatus("已还");
								
								//----------------综合处理新旧审核的  借款状态问题-----------------
								//Review review = getVisaOrderBorrowedStatusAndTime(travelerList.get(i).get("visaorderId").toString());
								//travelerList.get(i).put("jiekuanStatus", ReviewCommonUtil.getNextReview(review.getId()));
								Review review = null;
								ReviewNew reviewnew = null;
								if (!isNewReview) {
									review = getVisaOrderBorrowedStatusAndTime(travelerList.get(i).get("visaorderId").toString());
									travelerList.get(i).put("jiekuanStatus", ReviewCommonUtil.getNextReview(review.getId()));
								}else{
									reviewnew = getVisaOrderBorrowedStatusAndTime4Activiti(travelerList.get(i).get("visaorderId").toString());
									travelerList.get(i).put("jiekuanStatus", ReviewUtils.getChineseReviewStatusByUuid(reviewnew.getId()));
								}
								
								if(null != review ){
									String customDate = DateUtils.formatCustomDate((Date)review.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
								if(null != reviewnew ){
									String customDate = DateUtils.formatCustomDate((Date)reviewnew.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
								
							}else{
								//设置为 未借款
								travelerList.get(i).put("jiekuanStatus", "未借");
							}
						}else{
							//设置为 未借款
							travelerList.get(i).put("jiekuanStatus", "未借");
						}
					}
				}else{
					//游客借款状态
					//先用游客的应收金额和达账金额来判断 
					//相等就是 已还
					//不相等
						//查看是否有借款审核成功的记录,
							//存在的话 已结款
							//不存在的话 未借款
					//用travelerArray[26]代替达账金额的uuid,以后直接替换掉
					//达账金额不是空,进行差价对比,否则直接进行是否有借款申请的判断
					if(null != travelerList.get(i).get("accountedMoney") && !"".equals(travelerList.get(i).get("accountedMoney").toString())
							&& null == getChajia(travelerList.get(i).get("payPriceSerialNum").toString(), travelerList.get(i).get("accountedMoney").toString())){
						if(StringUtils.isBlank(getTravelerBorrowedMoney2(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString())))){
							//设置为 未借款	
							travelerList.get(i).put("jiekuanStatus", "未借");
						}else{
							travelerList.get(i).put("jiekuanStatus", "已还");//借款状态(1:未借2:已借3:已还)
						}
					}else{
						
						//----------------综合处理新旧审核的  借款状态问题-----------------
						
						boolean isNewReview  = false;
						
						String borrowMoneyandCheckStatus = getTravelerBorrowedMoney2(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
						//String borrowMoneyandCheckStatus = getTravelerBorrowedMoney4Activiti(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
						if (null==borrowMoneyandCheckStatus) {
							borrowMoneyandCheckStatus = getTravelerBorrowedMoney4Activiti(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
							isNewReview = true;
						}
						
						if (null != borrowMoneyandCheckStatus) {
							//借款
							String jiekuanStatus = borrowMoneyandCheckStatus.split(",")[1];
							if("0".equals(jiekuanStatus) || "1".equals(jiekuanStatus)){
								//设置为 审批中	
								//travelerForm.setJiekuanStatus("审批中");
								
								//----------------综合处理新旧审核的  借款状态问题-----------------
								
								Review review = null;
								ReviewNew reviewnew = null;
								if (!isNewReview) {
									review = getTravelerBorrowedStatusAndTime2(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
									travelerList.get(i).put("jiekuanStatus", ReviewCommonUtil.getNextReview(review.getId()));
								}else{
									reviewnew = getTravelerBorrowedStatusAndTime24Activiti(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
									travelerList.get(i).put("jiekuanStatus", ReviewUtils.getChineseReviewStatusByUuid(reviewnew.getId()));
								}
								
								//ReviewNew review = getTravelerBorrowedStatusAndTime24Activiti(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
								//travelerList.get(i).put("jiekuanStatus", ReviewUtils.getChineseReviewStatusByUuid(review.getId()));
								
								if(null != review ){
									String customDate = DateUtils.formatCustomDate((Date)review.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
								if(null != reviewnew ){
									String customDate = DateUtils.formatCustomDate((Date)reviewnew.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
							//2 表示审核成功 设置已 借款	
							}else if("2".equals(jiekuanStatus)){
								//travelerForm.setJiekuanStatus("已借");//借款状态(1:未借2:已借3:已还)
								
								
								//----------------综合处理新旧审核的  借款状态问题-----------------
								
								Review review = null;
								ReviewNew reviewnew = null;
								if (!isNewReview) {
									review =  getTravelerBorrowedStatusAndTime2(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
									travelerList.get(i).put("jiekuanStatus", ReviewCommonUtil.getNextReview(review.getId()));
								}else{
									reviewnew = getTravelerBorrowedStatusAndTime24Activiti(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
									travelerList.get(i).put("jiekuanStatus", ReviewUtils.getChineseReviewStatusByUuid(reviewnew.getId()));
								}
								
								if(null != review ){
									String customDate = DateUtils.formatCustomDate((Date)review.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
								if(null != reviewnew ){
									String customDate = DateUtils.formatCustomDate((Date)reviewnew.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
							}else if("3".equals(jiekuanStatus)){
								//设置为 已还	
								//travelerForm.setJiekuanStatus("已还");
								
								//----------------综合处理新旧审核的  借款状态问题-----------------
								
								Review review = null;
								ReviewNew reviewnew = null;
								if (!isNewReview) {
									review = getTravelerBorrowedStatusAndTime2(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
									travelerList.get(i).put("jiekuanStatus", ReviewCommonUtil.getNextReview(review.getId()));
								}else{
									reviewnew = getTravelerBorrowedStatusAndTime24Activiti(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
									travelerList.get(i).put("jiekuanStatus", ReviewUtils.getChineseReviewStatusByUuid(reviewnew.getId()));
								}
								
								if(null != review ){
									String customDate = DateUtils.formatCustomDate((Date)review.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
								if(null != reviewnew ){
									String customDate = DateUtils.formatCustomDate((Date)reviewnew.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
									travelerList.get(i).put("jiekuanTime", customDate);
								}
								
								
							}else{
								//设置为 未借款
								travelerList.get(i).put("jiekuanStatus", "未借");
							}
						}else{
							//设置为 未借款
							travelerList.get(i).put("jiekuanStatus", "未借");
						}
					}
				}
				//付款状态
				if(null != travelerList.get(i).get("payedMoneySerialNum") && !"".equals(String.valueOf(travelerList.get(i).get("payedMoneySerialNum")))){
					int count = 0;
					List<String> yflist = getMoneyAndId(travelerList.get(i).get("payedMoneySerialNum").toString());
					for(int j=0;j<yflist.size();j++){
						if(yflist.get(j).contains(" 0.00")){
							count++;
						}
					}
					if(null != yflist && count != yflist.size()){
						travelerList.get(i).put("fukuanStatus", "已收");
					}else{
						travelerList.get(i).put("fukuanStatus", "未收");
					}
				}else{
					travelerList.get(i).put("fukuanStatus", "未收");
				}
				//护照编号
				if(null != travelerList.get(i).get("passportId") && !"".equals(String.valueOf(travelerList.get(i).get("passportId")))){
					travelerList.get(i).put("passportId", travelerList.get(i).get("passportId").toString());
				}else{
					travelerList.get(i).put("passportId", "");
				}
				
				
				
				//借款明细       ---  需要综合考虑   新旧借款情况
				if(null != travelerList.get(i).get("id") && !"".equals(String.valueOf(travelerList.get(i).get("id")))){
					List<Object> parmter = new ArrayList<Object>();
					parmter.add(travelerList.get(i).get("visaorderId").toString());
					parmter.add(travelerList.get(i).get("id").toString());
					
					//----------------综合处理新旧审核的  借款状态问题-----------------
					List<Map<String, Object>> resultList = visaDao.findBySql(SqlConstant.SEARCH_JIEKUAN,Map.class, parmter.toArray());
					
					if(resultList != null && resultList.size() > 0){
						
						for(Map<String, Object> listin : resultList) {
							//借款金额
							if("borrowAmount".equals(listin.get("myKey"))){
								if(null != listin.get("myValue") ){
									travelerList.get(i).put("jiekuanAmount", listin.get("myValue").toString());
								}else{
									travelerList.get(i).put("jiekuanAmount", "");
								}
							}
							//借款备注
							else if("borrowRemark".equals(listin.get("myKey"))){
								if(null != listin.get("myValue") ){
									travelerList.get(i).put("jiekuanRemarks", listin.get("myValue").toString());
								}else{
									travelerList.get(i).put("jiekuanRemarks", "");
								}
							}
							//借款币种
							else if("currencyId".equals(listin.get("myKey"))){
								if(null != listin.get("myValue") ){
									String fuhao = currencyService.findCurrency(Long.valueOf(listin.get("myValue").toString())).getCurrencyMark();
									travelerList.get(i).put("jiekuanBizhong", fuhao);
								}else{
									travelerList.get(i).put("jiekuanBizhong", "");
								}
							}
							//借款创建者
							travelerList.get(i).put("jiekuanCreateUser", listin.get("name").toString());
						}
						
					}else{
						
						//----------------综合处理新旧审核的  借款状态问题-----------------
						Map<String,Object>  reviewAndDetailInfoMap = getReviewNew4HQXBorrowMoney(travelerList.get(i).get("visaorderId").toString(), Long.valueOf(travelerList.get(i).get("id").toString()));
						if (null!=reviewAndDetailInfoMap) {
							String fuhao = currencyService.findCurrency(Long.valueOf(reviewAndDetailInfoMap.get("currencyId").toString())).getCurrencyMark();
							travelerList.get(i).put("jiekuanAmount", reviewAndDetailInfoMap.get("borrowAmount").toString());
							travelerList.get(i).put("jiekuanRemarks", reviewAndDetailInfoMap.get("remark").toString());
							travelerList.get(i).put("jiekuanBizhong", fuhao);
							travelerList.get(i).put("jiekuanCreateUser", reviewAndDetailInfoMap.get("operatorName").toString());
						}else{
							travelerList.get(i).put("jiekuanAmount", "");
							travelerList.get(i).put("jiekuanRemarks", "");
							travelerList.get(i).put("jiekuanBizhong", "");
							travelerList.get(i).put("jiekuanCreateUser", "");
						}
						
					}
				}else{
					travelerList.get(i).put("jiekuanAmount", "");
					travelerList.get(i).put("jiekuanRemarks", "");
					travelerList.get(i).put("jiekuanBizhong", "");
					travelerList.get(i).put("jiekuanCreateUser", "");
				}
				
				
				//下单时间
				if(null != travelerList.get(i).get("createTime") && !"".equals(String.valueOf(travelerList.get(i).get("createTime")))) {
					String date = DateUtils.formatCustomDate((Date)travelerList.get(i).get("createTime"),"yyyy-MM-dd HH:mm:ss");
					travelerList.get(i).put("createTime", date);
				}else{
					travelerList.get(i).put("createTime", "");
				}
				
				//以下为销售在签证基础上后添加
				if("xiaoshou".equals(shenfen)){
					//参团订单编号
					if(null != travelerList.get(i).get("mainOrderNum") && !"".equals(String.valueOf(travelerList.get(i).get("mainOrderNum")))) {
						travelerList.get(i).put("mainOrderNum", travelerList.get(i).get("mainOrderNum").toString());
					}else{
						travelerList.get(i).put("mainOrderNum", "");
					}
					//参团团号
					if(null != travelerList.get(i).get("cantuantuanhao") && !"".equals(String.valueOf(travelerList.get(i).get("cantuantuanhao")))) {
						travelerList.get(i).put("cantuantuanhao", travelerList.get(i).get("cantuantuanhao").toString());
					}else{
						travelerList.get(i).put("cantuantuanhao", "");
					}
					//付款按钮的显示和隐藏
					//订单应收金额为空,不显示付款按钮
					if(null == travelerList.get(i).get("payPriceSerialNum") || "".equals(travelerList.get(i).get("payPriceSerialNum"))) {
						travelerList.get(i).put("fukuanButtonFlag", "false");
					//应付金额不为空,已付总金额为空,显示付款按钮
					}else if(null == travelerList.get(i).get("payedMoneySerialNum") || "".equals(travelerList.get(i).get("payedMoneySerialNum"))){
						travelerList.get(i).put("fukuanButtonFlag", "true");
					}else{
						Map<String, String> map = getChajia_yajin(travelerList.get(i).get("payPriceSerialNum").toString(),travelerList.get(i).get("payedMoneySerialNum").toString());
						//通过计算后,没有任何结果,则不显示付款按钮
						if(null == map ){
							travelerList.get(i).put("fukuanButtonFlag", "false");
						}else{
							travelerList.get(i).put("fukuanButtonFlag", "true");
						}
					}
					//历史数据（这些数据已支付不能再次支付）
					for(int j=0;j<oldVisaDatalist.size();j++){
						if(travelerList.get(i).get("visaorderNo").toString().equals(oldVisaDatalist.get(j).get("orderNum").toString())
								&& travelerList.get(i).get("visaorderId").toString().equals(oldVisaDatalist.get(j).get("id").toString())){
							travelerList.get(i).put("showFlag", "F");// 标示付款按钮不需要显示
						}
					}
					//交押金按钮的显示和隐藏
					//应收金额为空,不显示付款按钮
					if(null == travelerList.get(i).get("totalDepositUUID") || "".equals(travelerList.get(i).get("totalDepositUUID"))) {
						travelerList.get(i).put("payButtonFlag", "false");
						travelerList.get(i).put("yajinJine", "");
						travelerList.get(i).put("yajinHuobiId", "");
					//应付金额不为空,已付总金额为空,显示付款按钮
					}else if(null == travelerList.get(i).get("payedDeposit") || "".equals(travelerList.get(i).get("payedDeposit"))){
						travelerList.get(i).put("payButtonFlag", "true");
						//从应付金额中获取,货币列表和货币金额,作为参数
						travelerList.get(i).put("yajinJine", getMoneyId(travelerList.get(i).get("totalDepositUUID").toString()).get("jine"));
						travelerList.get(i).put("yajinHuobiId", getMoneyId(travelerList.get(i).get("totalDepositUUID").toString()).get("huobiId"));
					}else{
						Map<String, String> map = getChajia_yajin(travelerList.get(i).get("totalDepositUUID").toString(),travelerList.get(i).get("payedDeposit").toString());
						//通过计算后,没有任何结果,则不显示付款按钮
						if(null == map ){
							travelerList.get(i).put("payButtonFlag", "false");
							travelerList.get(i).put("yajinJine", "");
							travelerList.get(i).put("yajinHuobiId", "");
						}else{
							travelerList.get(i).put("payButtonFlag", "true");
							//游客剩余没有支付的金额
							travelerList.get(i).put("yajinJine", map.get("jine"));
							//游客剩余没有支付的金额货币列表
							travelerList.get(i).put("yajinHuobiId", map.get("huobiId"));
						}
					}
					//应收金额
					if(null != travelerList.get(i).get("payPriceSerialNum") && !"".equals(String.valueOf(travelerList.get(i).get("payPriceSerialNum")))){
						travelerList.get(i).put("payPriceSerialNum", getMoney(travelerList.get(i).get("payPriceSerialNum").toString(), null)!=null?getMoney(travelerList.get(i).get("payPriceSerialNum").toString(), null):"-");
					}else{
						travelerList.get(i).put("payPriceSerialNum", "");
					}
					//已付金额
					if(null != travelerList.get(i).get("payedMoneySerialNum") && !"".equals(String.valueOf(travelerList.get(i).get("payedMoneySerialNum")))){
						travelerList.get(i).put("payedMoneySerialNum", getMoney(travelerList.get(i).get("payedMoneySerialNum").toString(), null));
					}else{
						travelerList.get(i).put("payedMoneySerialNum", "");
					}
					//达账金额
					if(null != travelerList.get(i).get("accountedMoney") && !"".equals(String.valueOf(travelerList.get(i).get("accountedMoney")))){
						travelerList.get(i).put("accountedMoney", getMoney(travelerList.get(i).get("accountedMoney").toString(), null));
					}else{
						travelerList.get(i).put("accountedMoney", "");
					}
					//游客的主订单id
					if(null != travelerList.get(i).get("mainOrderId") && !"".equals(String.valueOf(travelerList.get(i).get("mainOrderId")))){
						travelerList.get(i).put("mainOrderId", travelerList.get(i).get("mainOrderId").toString());
					}else{
						travelerList.get(i).put("mainOrderId", "");
					}
				}
			}
		}
		
		return travelerList;
	}
	
	/**
	 * 签证游客列表拼接查询条件SQL
	 * */
	private StringBuffer searchTravlelerSQL(VisaOrderForm visaOrderForm,StringBuffer sqlBuffer,String shenfen,String mainOrderId){
		
		//验证参数是否合法
		if(null == visaOrderForm){
			return sqlBuffer;
		}
		
		//游客名称
		if(null != visaOrderForm.getTravelName() && !"".equals(visaOrderForm.getTravelName().trim())){
			String travelName = visaOrderForm.getTravelName().replace("'", "''");
			sqlBuffer.append(" and t.name like '%"+travelName.trim()+"%' ");
		}
		//公共编号(订单号,产品编号)的查询条件
		if(null != visaOrderForm.getCommonCode() && !"".equals(visaOrderForm.getCommonCode().trim())){
			String commonCode = visaOrderForm.getCommonCode().replace("'", "''");
			sqlBuffer.append(" and ( vo.order_no like '%"+commonCode.trim()+"%' or vp.productCode like '%"+commonCode.trim()+"%' )");
		}
		//订单号的查询条件
		if(null != visaOrderForm.getOrderNo() && !"".equals(visaOrderForm.getOrderNo().trim())){
			String orderNo = visaOrderForm.getOrderNo().replace("'", "''");
			sqlBuffer.append(" and vo.order_no like '%"+orderNo.trim()+"%' ");
		}
	
		if(StringUtils.isNotBlank(mainOrderId) && null != mainOrderId && !"null".equals(mainOrderId))
			sqlBuffer.append(" and vo.mainOrderId = '"+mainOrderId.trim()+"' ");
		
		//产品编号的查询条件
		if(null != visaOrderForm.getVisaProductId() && !"".equals(visaOrderForm.getVisaProductId().trim())){
			String visaProductId = visaOrderForm.getVisaProductId().replace("'", "''");
			sqlBuffer.append(" and vp.productCode like '%"+visaProductId.trim()+"%' ");
		}
		//渠道ID
		if(null != visaOrderForm.getAgentinfoId() && !"".equals(visaOrderForm.getAgentinfoId().trim())){
			sqlBuffer.append(" and vo.agentinfo_id = '"+visaOrderForm.getAgentinfoId()+"' ");
		}
		//创建者编号
		if(null != visaOrderForm.getCreateBy() && !"".equals(visaOrderForm.getCreateBy().trim())){
			sqlBuffer.append(" and su.name = '"+visaOrderForm.getCreateBy().trim()+"' ");
		}
		//支付状态
		if(null != visaOrderForm.getOrderPayStatus() && !"".equals(visaOrderForm.getOrderPayStatus().trim())){
			sqlBuffer.append(" and vo.payStatus = '"+visaOrderForm.getOrderPayStatus()+"' ");
		}
		// 当显示三级菜单 Tab 页面是“全部订单”的时候， 判断批发商权限，是否于其中显示“已取消”的订单
		if (StringUtils.isBlank(visaOrderForm.getShowType()) || visaOrderForm.getShowType().equals(Context.ORDER_PAYSTATUS_ALL)) {
			String banedOrderType = UserUtils.getUser().getCompany().getBanedVisaOrderOfAllTab();  // 获取批发商权限
			if (Functions.contains(banedOrderType, Context.ORDER_PAYSTATUS_YQX)) {
				sqlBuffer.append(" and vo.payStatus != '"+ Context.ORDER_PAYSTATUS_YQX + "' ");				
			}
		}
		//参团类型
		if(null != visaOrderForm.getOrderType() && !"".equals(visaOrderForm.getOrderType().trim())){
			if (Context.ORDER_STATUS_VISA.equals(visaOrderForm.getOrderType().trim())){
				sqlBuffer.append(" and t.main_order_id is null ");
			}else{
				sqlBuffer.append(" and po.orderStatus = '"+visaOrderForm.getOrderType()+"' ");
			}
		}
		//签证国家编号
		if(null != visaOrderForm.getSysCountryId() && !"".equals(visaOrderForm.getSysCountryId().trim())){
			sqlBuffer.append(" and vp.sysCountryId = '"+visaOrderForm.getSysCountryId()+"' ");
		}
		//签证状态
		if(null != visaOrderForm.getVisaStatus() && !"".equals(visaOrderForm.getVisaStatus().trim())){
			sqlBuffer.append(" and v.visa_stauts = '"+visaOrderForm.getVisaStatus()+"' ");
		}
		//签证类型
		if(null != visaOrderForm.getVisaType() && !"".equals(visaOrderForm.getVisaType().trim())){
			sqlBuffer.append(" and vp.visaType = '"+visaOrderForm.getVisaType()+"' ");
		}
		
		//护照状态
		if(StringUtils.isNotBlank(visaOrderForm.getPassportStatus())){
			sqlBuffer.append(" and t.passportStatus = '"+visaOrderForm.getPassportStatus()+"' ");
		}
		
		//借款状态
		if(null != visaOrderForm.getJiekuanStatus() && !"".equals(visaOrderForm.getJiekuanStatus().trim())){
			//1 审核中
			if("1".equals(visaOrderForm.getJiekuanStatus().trim())){
				if (UserUtils.getUser().getCompany().getId() == 68) {
					sqlBuffer.append(" and (IFNULL(re.STATUS ,re2.STATUS)) = 1 ");
				} else {
					sqlBuffer.append(" and re.status = 1 ");
				}
				//2 已借
			}if("2".equals(visaOrderForm.getJiekuanStatus().trim())){
				if (UserUtils.getUser().getCompany().getId() == 68) {
					sqlBuffer.append(" and (IFNULL(re.STATUS ,re2.STATUS) = 2 or (IFNULL(re.STATUS ,re2.STATUS) = 3 ))");
				} else {
					sqlBuffer.append(" and (re.status = 2 or re.status = 3 )");
				}
				//3 未借
			}if("3".equals(visaOrderForm.getJiekuanStatus().trim())){
				if (UserUtils.getUser().getCompany().getId() == 68) {
					sqlBuffer.append(" and (IFNULL(re.STATUS ,re2.STATUS) = 0 or IFNULL(re.STATUS ,re2.STATUS) = 4 or IFNULL(re.STATUS ,re2.STATUS) is null) ");
				} else {
					sqlBuffer.append(" and (re.status = 0 or re.status = 4 or re.status is null) ");
				}
				
			}
		}
		//借款时间-开始时间
		if(null != visaOrderForm.getJiekuanTimeStart() && !"".equals(visaOrderForm.getJiekuanTimeStart().trim())){
			sqlBuffer.append(" and re.createDate >= '"+visaOrderForm.getJiekuanTimeStart()+" 00:00:00' ");
		}
		//借款时间-结束时间
		if(null != visaOrderForm.getJiekuanTimeEnd() && !"".equals(visaOrderForm.getJiekuanTimeEnd().trim())){
			sqlBuffer.append(" and re.createDate <= '"+visaOrderForm.getJiekuanTimeEnd()+" 23:59:59' ");
		}
		//预计出团时间-开始时间
		if(null != visaOrderForm.getForecastStartOutStart() && !"".equals(visaOrderForm.getForecastStartOutStart().trim())){
			sqlBuffer.append(" and v.forecast_start_out >= '"+visaOrderForm.getForecastStartOutStart()+" 00:00:00' ");
		}
		//预计出团时间-结束时间
		if(null != visaOrderForm.getForecastStartOutEnd () && !"".equals(visaOrderForm.getForecastStartOutEnd().trim())){
			sqlBuffer.append(" and v.forecast_start_out <= '"+visaOrderForm.getForecastStartOutEnd()+" 23:59:59' ");
		}
		//预计约签时间-开始时间
		if(null != visaOrderForm.getForecastContractStart() && !"".equals(visaOrderForm.getForecastContractStart().trim())){
			sqlBuffer.append(" and v.forecast_contract >= '"+visaOrderForm.getForecastContractStart()+" 00:00:00' ");
		}
		//预计约签时间-结束时间
		if(null != visaOrderForm.getForecastContractEnd() && !"".equals(visaOrderForm.getForecastContractEnd().trim())){
			sqlBuffer.append(" and v.forecast_contract <= '"+visaOrderForm.getForecastContractEnd()+" 23:59:59' ");
		}
		//实际出团时间-开始时间
		if(null != visaOrderForm.getStartOutStart() && !"".equals(visaOrderForm.getStartOutStart().trim())){
			sqlBuffer.append(" and v.start_out >= '"+visaOrderForm.getStartOutStart()+" 00:00:00' ");
		}
		//实际出团时间-结束时间
		if(null != visaOrderForm.getStartOutEnd() && !"".equals(visaOrderForm.getStartOutEnd().trim())){
			sqlBuffer.append(" and v.start_out <= '"+visaOrderForm.getStartOutEnd()+" 23:59:59' ");
		}
		//实际签约时间-开始时间
		if(null != visaOrderForm.getContractStart() && !"".equals(visaOrderForm.getContractStart().trim())){
			sqlBuffer.append(" and v.contract >= '"+visaOrderForm.getContractStart()+" 00:00:00' ");
		}
		//实际签约时间-结束时间
		if(null != visaOrderForm.getContractEnd() && !"".equals(visaOrderForm.getContractEnd().trim())){
			sqlBuffer.append(" and v.contract <= '"+visaOrderForm.getContractEnd()+" 23:59:59' ");
		}
		//下单时间-开始时间
		if(null != visaOrderForm.getCreateDateStart() && !"".equals(visaOrderForm.getCreateDateStart().trim())){
			sqlBuffer.append(" and vo.create_date >= '"+visaOrderForm.getCreateDateStart()+" 00:00:00' ");
		}
		//下单时间-结束时间
		if(null != visaOrderForm.getCreateDateEnd() && !"".equals(visaOrderForm.getCreateDateEnd().trim())){
			sqlBuffer.append(" and vo.create_date <= '"+visaOrderForm.getCreateDateEnd()+" 23:59:59' ");
		}
		
		return sqlBuffer;
	}
	
	//拼接部门SQL type=1:拼接left join type=2：拼接where条件
	private StringBuffer departmentSQL(DepartmentCommon common, StringBuffer sqlBuffer, int type){
		
		/** 要查询部门ID */
		String departmentId = common.getDepartmentId();
		/** 是否是管理员 */
		boolean isManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_MANAGER);
		/** 是否是计调经理 */
		boolean isOPManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_OP_EXECUTIVE);
		/** 是否是计调 */
		boolean isOP = common.getRoleTypeList().contains(Context.ROLE_TYPE_OP);
		/** 是否是销售经理 */
		boolean isSaleManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES_EXECUTIVE);
		/** 是否是销售 */
		boolean isSale = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES);
		
		/** 部门内部人员是否能相互查看，默认不能  add by 2016/01/18 for requirement ID 113*/
		boolean flag = whetherSelectAllDeptDate(common);
		
		if (type==1) {
			/*用in语句替代left join ,该处左链接查询有问题*/
//			if (!isManager) {
//				if(flag){//同部门下的不同角色订单可见,相当于同时拥有计调经理和销售经理角色
//					sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
//					sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
//				}else{
//					if (isOPManager) {
//						sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
//					}
//					if (isSaleManager) {
//						sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
//					}
//					if(!isSaleManager && !isOPManager && !isOP && !isSale){
//						sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
//						sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
//					}
//				}
//			}
		}else if(type==2){
			if (!isManager) {
				if(flag){
					sqlBuffer.append(" AND (vp.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
					sqlBuffer.append(" OR vo.create_by in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
					sqlBuffer.append(" OR vo.salerId in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ) ");
				}else{//未勾选权限 
					if (isOPManager && isSaleManager) {
						sqlBuffer.append(" AND (vp.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						sqlBuffer.append(" OR vo.create_by in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						sqlBuffer.append(" OR vo.salerId in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ) ");
					}else if(isOPManager){
						sqlBuffer.append(" AND ( vp.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						/*下单人是本部门或子部门的计调或基调主管*/
						sqlBuffer.append(" OR vo.create_by in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') ) ");
					}else if(isSaleManager){
						sqlBuffer.append(" AND (vo.create_by in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						sqlBuffer.append(" OR vo.salerId in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						/*本部门或子部门的销售或销售主管 发布产品所属订单*/
						sqlBuffer.append(" OR vp.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ) ");
					}
					if(!isSaleManager && !isOPManager && (isOP || isSale)){
						sqlBuffer.append(" AND (vp.createBy='"+UserUtils.getUser().getId()+"' OR (vo.create_by='"+UserUtils.getUser().getId()+"' OR vo.salerId='"+UserUtils.getUser().getId()+"')) ");
					}
					if(!isSaleManager && !isOPManager && !isOP && !isSale){
						//sqlBuffer.append("AND (vp.createBy=opdept.id OR (vo.create_by=saledept.id OR vo.salerId=saledept.id)) ");
						sqlBuffer.append(" AND (vp.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						sqlBuffer.append(" OR vo.create_by in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						sqlBuffer.append(" OR vo.salerId in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ) ");
					}
				}
			}
		}
		
		return sqlBuffer;
	}
	
	
	/**
	 * 判断此部门是否允许人员相互查看数据：
	 * 					获取有此权限标示的菜单；然后查看此部门可以相互查看的菜单列表；
	 * 					如果此菜单列表中包含这个菜单则表示允许相互查看
	 * 					特殊情况：签务签证订单 和 批量操作页要权限同步
	 * @param common
	 * @return
	 */
	private boolean whetherSelectAllDeptDate(DepartmentCommon common) {
		
		if (common == null || StringUtils.isBlank(common.getDepartmentId())) {
			return false;
		}
		
		/** 判断是否是部门之间人员可以相互查看请求 */
		List<Menu> menuList = menuDao.findByPermission(common.getPermission());//查询请求的菜单
		Department dept = departmentDao.findOne(Long.parseLong(common.getDepartmentId()));//查询用于查询的部门
		if (CollectionUtils.isNotEmpty(menuList)) {
    		List<Integer> list = departmentDao.findSelectIdsByDeptId(dept.getId());
    		if (CollectionUtils.isNotEmpty(list)) {
    			//签务签证订单-批量操作，若签务签证订单勾选权限，则批量操作页也拥有权限
    			boolean flag = false;//是否选中签务签证订单权限，默认否
    			for(Integer menuId : list){
    				Menu menu = menuDao.findOne(menuId.longValue());
    				if("签务签证订单".equals(menu.getName())){
    					flag = true;
    					break;
    				}
    			}
    			if(flag && "visaOrderForOp:batch:operation".equals(common.getPermission())){//隐藏的菜单-批量操作页
    				return true;
    			}
    			
    			return list.contains(menuList.get(0).getId().intValue());
    		}
    	}
		return false;
	}
	

	public void saveMoneyAmount(String rebatesPayPrice, Traveler saveTraveler)
	{
		Long userId = UserUtils.getUser().getId();
		MoneyAmount amount = new MoneyAmount();
		amount.setBusindessType(2);
		amount.setMoneyType(23);
		if(rebatesPayPrice.replace("\"", "").split(",").length==1)
		amount.setAmount(new BigDecimal(0));
		else
		amount.setAmount(new BigDecimal(rebatesPayPrice.replace("\"", "").split(",")[1]));
		amount.setCurrencyId(Integer.valueOf(rebatesPayPrice.replace("\"", "").split(",")[0]));
		amount.setSerialNum(saveTraveler.getRebatesMoneySerialNum());
		amount.setCreatedBy(userId);
		amount.setOrderType(6);
		amount.setCreateTime(new Date());
		amount.setUid(saveTraveler.getId());
		amountService.saveOrUpdateMoneyAmount(amount);
		
		
	}

	public void updateVisaOrderByCode(String orderCode)
	{
		visaOrderDao.updateBySql("UPDATE visa_order vo SET vo.isRead=1 WHERE vo.order_no='"+orderCode+"'");
		
	}
	/**
	 * 
	* @Title: getVisaCashPayPrint
	* @Description: TODO(获取签证押金打印单内容20150824)
	* @param @param payid 支付记录id
	* @param @return    设定文件
	* @return List<Map<Object,Object>>    返回类型
	* @throws
	 */
	public  List<Map<Object, Object>> getVisaCashPayPrint(Long payid) {
		StringBuffer sqlBuffer = new StringBuffer();
		//C460V3 签证团号取签证产品的虚拟团号  modify by wangyang 2016.4.15
		sqlBuffer.append("select pay.id,pro.id AS orderid,")
				.append("us.name,activity.productName acitivityName,pay.orderNum,users. NAME createName,")
				.append("pay.isAsAccount,pay.toBankAccount,pay.toBankNname,pay.bankName,pay.bankAccount,")
				.append("pay.payerName,pay.remarks,pay.traveler_id travelerId,pay.payPriceType,")
				// sql执行速度优化  王洋 2016.10.26 --- 修改后新增代码S ---
				.append("( SELECT GROUP_CONCAT( CONCAT( c.currency_name, FORMAT(ma.amount, 2)) SEPARATOR '+' ) total_money ")
				.append(" FROM money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id ")
				.append(" WHERE ma.businessType = 2 AND ma.serialNum = pay.moneySerialNum GROUP BY ma.serialNum ) AS payedMoney, ")
				// sql执行速度优化  王洋 2016.10.26 --- 修改后新增代码E ---
				// bug 16689 将签证押金收款的 出发/签证时间改为签证下单时间  modify by wangyang 2016.11.3
				.append(" pay.createDate,pay.accountDate,pro.create_date conDate,pay.createDate payDate, ")
				.append(" pay.to_alipay_account AS toAlipayAccount,pay.to_alipay_name AS toAlipayName, ");//224因公支付宝
		//C460V3plus 环球行签证团号取订单团号    wangyang 2016.5.6
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
			sqlBuffer.append(" pro.group_code AS groupCode ");
		}else{
			sqlBuffer.append(" activity.groupCode ");
		}
		sqlBuffer.append(" FROM orderpay pay ")
				.append("LEFT JOIN visa_order pro ON pay.orderNum = pro.order_no ")
				.append("LEFT JOIN visa_products activity ON activity.id = pro.visa_product_id ")
				.append("LEFT JOIN sys_user us ON us.id = pro.create_by ")
				.append("LEFT JOIN sys_user users ON pay.updateBy = users.id ")
				// sql执行速度优化  王洋 2016.10.26 --- 原代码（已注释）
//				.append("LEFT JOIN (select ma.serialNum,GROUP_CONCAT(CONCAT(c.currency_name,FORMAT(ma.amount, 2)) ")
//				.append("SEPARATOR '+' ) total_money from money_amount ma ")
//				.append("LEFT JOIN currency c ON ma.currencyId = c.currency_id where ma.businessType = 2 ")
//				.append("GROUP BY ma.serialNum) t2 ON pay.moneySerialNum = t2.serialNum ")
				.append("where pay.id = ").append(payid);
		return  visaOrderDao.findBySql(sqlBuffer.toString(),Map.class);
	    
	}
	
	  /**
		 * 
		* @Title: createVisaCashFile
		* @Description: TODO(生成签证押金单word文档)
		* @param @param id
		* @param @return
		* @param @throws IOException
		* @param @throws TemplateException
		* @return File    返回类型
		* @throws
		 */
		@Transactional(readOnly=false,rollbackFor=Exception.class)
		public File createVisaCashFile(Long payid) throws IOException, TemplateException {
			//word文档数据集合
			Map<String, Object> root = new HashMap<String, Object>();
			if(payid == null) {
				return null;
			}else {
				List<Map<Object, Object>> payList = new ArrayList<Map<Object, Object>>();
				payList = getVisaCashPayPrint(payid);
				if(payList != null && payList.size() > 0) {
					 root.put("pay", payList.get(0));
					 root.put("groupCode", payList.get(0).get("groupCode") == null ? "" : payList.get(0).get("groupCode").toString());
					 root.put("groupName", payList.get(0).get("acitivityName") == null ? "" : payList.get(0).get("acitivityName"));
					 //根据游客id获取游客姓名
					 String travelerId = payList.get(0).get("travelerId").toString();
					 if(StringUtils.isNotBlank(travelerId)) {
						 String travelerName = travelerDao.findById(Long.parseLong(travelerId)).getName();
						 root.put("traveler", travelerName); 
					 }else {
						 root.put("traveler", ""); 
					 }
					 String capitalMoney = "";
					   String payPriceType = payList.get(0).get("payPriceType").toString();
					   if(StringUtils.isNotBlank(payPriceType)){
						   if(payPriceType.equals("16")){
							  String payedMoney = payList.get(0).get("payedMoney").toString();
							  //当前批发商的美元、加元汇率（目前环球行）
							  List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
							    //美元汇率
								BigDecimal currencyExchangerateUSA = null;
								//加元汇率
								BigDecimal currencyExchangerateCAN = null;
								//人民币计算
								BigDecimal amountCHN = new BigDecimal("0");
								//美元计算
								BigDecimal amountUSA = new BigDecimal("0");
								//加元计算
								BigDecimal amountCAN = new BigDecimal("0");
							  for (Currency currency : currencylist) {
								if(currency.getCurrencyName().startsWith("美元")) {
									currencyExchangerateUSA = currency.getConvertLowest();
								}else if(currency.getCurrencyName().startsWith("加")) {
									currencyExchangerateCAN = currency.getConvertLowest();
								}
							}
							  //增加多币种金额判断
							  if(payedMoney.contains("+")) {
								  String [] moneys = payedMoney.split("\\+");
								  if(moneys.length > 0) {
									  for(int i = 0 ; i < moneys.length ; i++) {
										  if(moneys[i].contains("人民币")) {
											  moneys[i] = moneys[i].replaceAll("人民币", "").replaceAll(",", "");
											  amountCHN = amountCHN.add(BigDecimal.valueOf(Double.parseDouble(moneys[i])));
										  }else if(moneys[i].contains("美元")) {
											  moneys[i] = moneys[i].replaceAll("美元", "").replaceAll(",", "");
											  amountUSA = amountUSA.add(BigDecimal.valueOf(Double.parseDouble(moneys[i])));
											  amountCHN = amountCHN.add(amountUSA.multiply(currencyExchangerateUSA));
										  }else {
											  moneys[i] = moneys[i].replaceAll("加币", "").replaceAll("加拿大", "").replaceAll(",", "");
											  amountCAN = amountCAN.add(BigDecimal.valueOf(Double.parseDouble(moneys[i])));
											  amountCHN = amountCHN.add(amountCAN.multiply(currencyExchangerateCAN));
										  }
									  }
									  if(amountCHN.doubleValue() < 0) {
										  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
										  root.put("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
									  }else {
										  capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
										  root.put("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
									  }
									  root.put("capitalMoney", capitalMoney);
								  }
							  }else {
								  if(payedMoney.contains("人民币")) {
									  payedMoney = payedMoney.replaceAll("人民币", "");
									  if(payedMoney.contains("-")) {
										  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(payedMoney.replaceAll("-", "").replace(",", ""));
									  }else {
										  capitalMoney = "人民币" + StringNumFormat.changeAmount(payedMoney.replaceAll(",", ""));
									  }
									  root.put("payedMoney", payedMoney);
									  root.put("capitalMoney", capitalMoney);
								  }else if(payedMoney.contains("美元")) {
									  payedMoney = payedMoney.replaceAll("美元", "").replace(",", "");
									  amountUSA = amountUSA.add(BigDecimal.valueOf(Double.parseDouble(payedMoney)));
									  amountCHN = amountCHN.add(amountUSA.multiply(currencyExchangerateUSA));
									  if(amountCHN.doubleValue() < 0) {
										  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
										  root.put("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
									  }else {
										  capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
										  root.put("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
									  }
									  root.put("capitalMoney", capitalMoney);
								  }else if(payedMoney.contains("加币") || payedMoney.contains("加拿大")) {
									  payedMoney = payedMoney.replaceAll("加币", "").replaceAll("加拿大", "").replace(",", "");
									  amountCAN = amountCAN.add(BigDecimal.valueOf(Double.parseDouble(payedMoney)));
									  amountCHN = amountCHN.add(amountCAN.multiply(currencyExchangerateCAN));
									  if(amountCHN.doubleValue() < 0) {
										  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
										  root.put("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
									  }else {
										  capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
										  root.put("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
									  }
									  root.put("capitalMoney", capitalMoney);
								  }
								  else {
									  root.put("payedMoney", payedMoney);
									  root.put("capitalMoney", capitalMoney);
								  }
							  }
						   }
					   }
					   String companyName = UserUtils.getUser().getCompany().getCompanyName();
					   // 填写日期
						String payCreate = payList.get(0).get("createDate") == null ? "" : payList.get(0).get("createDate").toString();
						if (StringUtils.isNotBlank(payCreate)) {
							Date groupDate = DateUtils.dateFormat(payCreate);
							payCreate = DateUtils.formatCustomDate(groupDate, "yyyy年MM月dd日");
						} else {
							payCreate = "年       月       日";
						}
						root.put("payCreate", payCreate);
						// 出发/签证日期
						Date groupOpenDate = (Date) payList.get(0).get("createDate");
						if (groupOpenDate != null) {
							String groupDate = DateUtils.formatCustomDate(groupOpenDate,
									"MM月dd日");
							root.put("groupOpenDate", groupDate);
						} else {
							String groupDate = "年       月       日";
							root.put("groupOpenDate", groupDate);
						}
						// 银行到账日期
						String accountDate = "";
						if(companyName.contains("环球行")) {
							accountDate = payList.get(0).get("accountDate") == null ? "" : payList.get(0).get("accountDate").toString();
							if (StringUtils.isNotBlank(accountDate)) {
								Date groupDate = DateUtils.dateFormat(accountDate);
								accountDate = DateUtils
										.formatCustomDate(groupDate, "yyyy年M月d日");
								String isAsAccount = payList.get(0).get("isAsAccount") == null ? "" : payList.get(0).get("isAsAccount").toString();
								if(!isAsAccount.equals("1")) {
									accountDate = "年       月       日";
								}
							} else {
								accountDate = "年       月       日";
							}
							
						}else {
							accountDate = payList.get(0).get("accountDate") == null ? "" : payList.get(0).get("accountDate").toString();
							if (StringUtils.isNotBlank(accountDate)) {
								Date groupDate = DateUtils.dateFormat(accountDate);
								accountDate = DateUtils
										.formatCustomDate(groupDate, "yyyy年M月d日");
							} else {
								accountDate = "年       月       日";
							}
						}
						root.put("accountDate", accountDate);
						// 确认收款日期(20151011 环球行、拉美途客户确认到账时间为空，其它客户当财务撤销确认后，确认收款日期消失)
						Integer revoke = payList.get(0).get("isAsAccount") == null ? -1 :Integer.parseInt(payList.get(0).get("isAsAccount").toString());
						String conDate = payList.get(0).get("conDate") == null ? "" : payList.get(0).get("conDate").toString();
						if(companyName.contains("环球行") || companyName.contains("拉美途")) {
							conDate = "年       月       日";
						}else {
							if(revoke == 0 || revoke == -1 || revoke == 2) {
								conDate = "年       月       日";
							}else {
								if (StringUtils.isNotBlank(conDate)) {
									Long compayId = UserUtils.getUser().getCompany().getId();
									if(compayId == 68) {
										conDate = "年       月       日";
									}else {
										Date groupDate = DateUtils.dateFormat(conDate);
										conDate = DateUtils.formatCustomDate(groupDate, "yyyy年M月d日");
									}
								} else {
									conDate = "年       月       日";
								}
							}
						}
						root.put("conDate", conDate);
						//来款单位
						root.put("payerName", payList.get(0).get("payerName") == null ? "" : payList.get(0).get("payerName").toString());
						root.put("bankName", payList.get(0).get("bankName") == null ? "" : payList.get(0).get("bankName").toString());
						root.put("bankAccount", payList.get(0).get("bankAccount") == null ? "" : payList.get(0).get("bankAccount").toString());
						//收款账户
						root.put("tobankName", payList.get(0).get("toBankNname") == null ? "" : payList.get(0).get("toBankNname").toString());
						root.put("tobankAccount", payList.get(0).get("toBankAccount") == null ? "" : payList.get(0).get("toBankAccount").toString());
						//备注
						root.put("remarks", payList.get(0).get("remarks") == null ? "" : payList.get(0).get("remarks").toString());
						//收款人
						String isAsAccount = payList.get(0).get("isAsAccount") == null ? "" : payList.get(0).get("isAsAccount").toString();
						if(StringUtils.isNotBlank(isAsAccount)) {
							if(Integer.parseInt(isAsAccount) == Context.ORDERPAY_ACCOUNT_STATUS_YDZ) {
								root.put("createName", payList.get(0).get("createName") == null ? "" : payList.get(0).get("createName").toString());
							}else {
								root.put("createName", "");
							}
						}else {
							root.put("createName", "");
						}
						//款项
						root.put("moneyType", "签证押金"); 
				}
				return FreeMarkerUtil.generateFile("receiptList.ftl",
						"receiptList.doc", root);
			}
		}
	
	/**
	 * 单游客付款，查询币种和金额
	 * @param orderId
	 * @param travellerId
	 * @return
	 */
	public List<CurrencyAmount> findMoneyAmount(Integer orderId, Integer travellerId) {
		// TODO Auto-generated method stub
		List<CurrencyAmount> resultList = new ArrayList<CurrencyAmount>();
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travellerId.toString()));	
		Map<String, String> map = getChajia(traveler.getPayPriceSerialNum(), traveler.getPayedMoneySerialNum());
		if(map.get("huobiId") != null && map.get("jine") != null){
			String[] huobiId = map.get("huobiId").split(",");
			String[] jine = map.get("jine").split(",");
			for(int i = 0;i<huobiId.length;i++){
				CurrencyAmount currencyAmount = new CurrencyAmount();
				currencyAmount.setCurrencyid(Integer.parseInt(huobiId[i]));
				currencyAmount.setPrice(Double.parseDouble(jine[i]));
				resultList.add(currencyAmount);
			}
		}
		
		return resultList;			
	}
	
	
	

	
	// -------------wangxinwei 拉美途创建签证子订单接口开始--------------
	/**
	 * 签证子订单接口说明：
	 * 1. 参数 countryId， visaType，collarZoning用来匹配查询签证产品
	 * 2. 参数 productOrderCommony : 主订单对象
	 * 3. 参数 traveler 办签证的游客
	 * 4. 参数 travelerDocInfoIds 办签证游客的附件IDS
	 * 
	 * 
	 * ------------wxw added 2015-03-14-------------
	 * 子订单修改：
	 * 1.生成的虚拟团号部分要与预定时保持一致
	 * 2.去掉订单中main_order_code字段造成的所有影响进行修改
	 * 3.为了解决签证订单价钱单独结算的问题，子订单游客还必须单独结算，游客还必须复制一份团期游客的信息
	 * 4.为了使团期游客与签证中游客的信息修改后同步修改信息的便利，在签证游客的信息中增加：main_order_travelerid，指向团期游客的id
	 *   如签证游客为非团期游客；
	 * 5.同时在游客表中增加main_order_id，指向团期订单的ID
	 * 6.由于签证订单在新增时增加了可变的结算价， 和 成本价，作为子订单也要进行相应的处理；
	 *   6.1：保持订单的成本价
	 *   6.2：保存游客的成本价
	 * 7.生成的签证默认的状态改为：默认状态为续补资料
	 * 
	 * 
	 * -------wangxinwei  added 2015-03-19  拉美途接口变动-----------
	 * -------对应需求号  C310 ---------
	 * 1. 签证订单增加 mainOrderId 字段 关联    productorder.orderNum
	 * 2. 订单的成本价改为： 订单的“应收金额”取签证产品成本
	 * 3. 游客的“应收金额”：取签证产品成本价
	 * 4. 通过签证子订单的 mainOrderId 即  主订单的orderNum获取 全部子订单
	 * 
	 * 
	 * @param productOrderCommon
	 * @param traveler
	 * @param travelerDocInfoIds: 
	 *key: passport_photo_id护照首页ID
     *       identity_front_photo_id身份证正面ID
     *       identity_back_photo_id身份证背面ID
     *       table_photo_id报名表ID
     *       person_photo_id照片ID
     *       other_photo_id其他图片附件ID（多个附件ID，用&分离）
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void subVisaOrderCreate4LMT(VisaProducts visaProducts, VisaOrder visaOrder, TravelerVisa travelerVisa,
			ProductOrderCommon productOrderCommon,
			Traveler traveler,
			Map<String, Long> travelerDocInfoIds) {
		
		Date forecastStartOut = travelerVisa.getGroupOpenDate(); //预计出发时间
		Date forecastContract = travelerVisa.getContractDate();  //预计签约时间
		
		Traveler travelerback = null;
		Visa visa = null;
		
		if (visaOrder == null) {
			//保存签证子订单基本信息
			visaOrder = createSubVisaOrderBasicOrder4LMT(visaProducts,productOrderCommon);
		} else {
			travelerback = travelerDao.findTravelerByOrderIdAndOrderType(visaOrder.getId(), Context.ORDER_TYPE_QZ).get(0);
			visa = visaDao.findByTravelerId(travelerback.getId());
		}
		
		//保存或更新签证子订单游客信息
		travelerback = createOrUpdateSubVisaTraveler4LMT(visaProducts, traveler, travelerback, visaOrder, productOrderCommon.getId());
		
		//保存或更改游客的结算价记录
	    createSubVisaTravelerMoneyAmount4LMT(moneyAmountService,travelerback,visaProducts,visaOrder);
	    
	    //保存或更新子订单的 Visa基本信息
	    createSubVisaBasicInfo4LMT(visa, forecastStartOut, forecastContract, travelerback, travelerDocInfoIds);
	    
	    //处理子订单的结算价记录
	    createSubVisaOrderMoneyAmount4LMT(moneyAmountService,visaProducts,visaOrder);
	    
	}
	
	/**
	 * 保存签证子订单基本信息
	 * @param visaProducts
	 * @param productOrderCommon
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	private VisaOrder createSubVisaOrderBasicOrder4LMT(VisaProducts visaProducts,ProductOrderCommon productOrderCommon){
		User user  = productOrderCommon.getCreateBy();
		String companyName = user.getCompany().getName();
		Long companyId = user.getCompany().getId();
		VisaOrder visaOrder = new VisaOrder();
		String orderNum = sysIncreaseService.updateSysIncrease(companyName
				.length() > 3 ? companyName.substring(0, 3) : companyName,
						companyId, null, Context.ORDER_NUM_TYPE);

		
		
		String GroupNo = null;
		if (UserUtils.getUser().getCompany().getId()==68) {
			  GroupNo=activityGroupService.getGroupNumForTTS(visaProducts.getDeptId()+"",null);
		}else {
			 GroupNo = sysIncreaseService.updateSysIncrease(companyName
						.length() > 3 ? companyName.substring(0, 3) : companyName,
								companyId, null, Context.GROUP_NUM_TYPE);
		}
		
		visaOrder.setActivityCode(productOrderCommon.getProductGroupId());
		visaOrder.setOrderNo(orderNum);
		visaOrder.setGroupCode(GroupNo);
		visaOrder.setPayStatus(1);	//支付状态1-未支付全款 2-未支付订金  默认为未支付
		visaOrder.setVisaProductId(visaProducts.getId());
		
		/**
		 * wangxinwei 2015-10-21 modified
		 * 签证订单增加 mainOrderId 字段 关联    productorder.orderNum
		 */
		visaOrder.setMainOrderId(productOrderCommon.getOrderNum());
		
		//在签证订单中保存签证预定时相关签证产品的应收价
		visaOrder.setProOriginCurrencyId(visaProducts.getCurrencyId());
		visaOrder.setProOriginVisaPay(visaProducts.getVisaPay());
		
		visaOrder.setProductTypeID(VisaPreOrderController.VISA_ORDER_TYPE.longValue());
		visaOrder.setTravelNum(1);//默认只生成一个游客的订单
		visaOrder.setAgentinfoId(Long.valueOf(productOrderCommon.getOrderCompany()));
		
		// --- wxw added 20150908 生成子订单 保存 非签渠道的名称 ---
		visaOrder.setAgentinfoName(productOrderCommon.getOrderCompanyName());
		
	    //  0：未支付;1:已支付;2:已取消;100:订单创建中（创建未完成，不能使用）
		visaOrder.setVisaOrderStatus(Integer.valueOf(Context.VISA_ORDER_PAYSTATUS_DOING));// 订单状态(正在进行中)   确定点单结算后要修改状态
		visaOrder.setCreateBy(user);
		visaOrder.setSalerId(productOrderCommon.getSalerId());
		visaOrder.setSalerName(productOrderCommon.getSalerName());
		/**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		visaOrder.setPayedMoney(UUID.randomUUID().toString().replace("-", ""));
		visaOrder.setAccountedMoney(UUID.randomUUID().toString().replace("-", ""));
		//2015-03-02
		//visaOrder.setMainOrderCode(productOrderCommon.getId().intValue());
		
		/**
		 * wxw added 2016-01-04   对应需求号c457,c486
		 * 预定时处理产品预报单锁定情况--0：未锁定（默认），1：锁定
		 * (目前只针对拉美途)
		 */
		if ("10".equals(visaProducts.getForcastStatus())) {
			visaOrder.setForecastLockedIn('1');
		}
		
		/**
		 * wxw added 2016-01-04   对应需求号c457,c486
		 * 预定时处理产品预报单锁定情况--0：未锁定（默认），1：锁定
		 * (目前只针对拉美途)
		 */
		if ("1".equals(visaProducts.getLockStatus())) {
			visaOrder.setSettleLockedIn('1');
		}
		
		
	    VisaOrder visaOrderBack = visaOrderDao.save(visaOrder);
	    return visaOrderBack;
	}

	/**
	 * 保存或修改签证子订单游客信息
	 * @param visaProducts
	 * @param travelerback
	 * @param visaOrderBack
	 * @param createFlag
	 * @return
	 */
	private Traveler createOrUpdateSubVisaTraveler4LMT(VisaProducts visaProducts, Traveler travelerback, Traveler visatraveler,
			VisaOrder visaOrderBack, Long mainOrderId) {
		
		if (visatraveler == null) {
			visatraveler = new Traveler();
		}
	    visatraveler.setOrderId(visaOrderBack.getId());
	    visatraveler.setName(travelerback.getName());//游客姓名
	    visatraveler.setNameSpell(travelerback.getNameSpell());
	    visatraveler.setIdCard(travelerback.getIdCard());
	    visatraveler.setNationality(travelerback.getNationality());
	    visatraveler.setSex(travelerback.getSex());
	    visatraveler.setBirthDay(travelerback.getBirthDay());
	    visatraveler.setIssuePlace(travelerback.getIssuePlace());
	    visatraveler.setValidityDate(travelerback.getValidityDate());
	    visatraveler.setTelephone(travelerback.getTelephone());
	    visatraveler.setRemark(travelerback.getRemark());
	    visatraveler.setSrcPrice(visaProducts.getVisaPay());//签证产品的应收价格
	    visatraveler.setPersonType(travelerback.getPersonType());
	    visatraveler.setPassportCode(travelerback.getPassportCode());
	    visatraveler.setIssuePlace(travelerback.getIssuePlace());
	    visatraveler.setPassportValidity(travelerback.getPassportValidity());
	    visatraveler.setPassportType(travelerback.getPassportType());
	    Currency currency =  currencyService.findCurrency(Long.parseLong(visaProducts.getCurrencyId()+""));
	    visatraveler.setSrcPriceCurrency(currency);//签证产品的应收价格币种
	    visatraveler.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
	    visatraveler.setDelFlag(travelerback.getDelFlag());
	    
	    /**
	     * wxw modified 游客中添加主订单Id 2015-03-02
	     * 1.在创建子订单是需在游客中添加主订单的ID，以标识该游客为参团游客
	     * 2.在创建游客信息时为了同步主子订单的游客信息在签证游客的订单中记录主订单游客的ID
	     */
	    visatraveler.setMainOrderId(mainOrderId);
	    visatraveler.setMainOrderTravelerId(travelerback.getId());
	    
	    Traveler travelertemp =  travelerDao.save(visatraveler);
	    return travelertemp;
	}
	
	/**
	 * 
	 * ---2015-03-14 wxwadded---
	 * 增加游客的成本价；
	 * 
	 * 生成游客的结算价记录
	 * @param moneyAmountService
	 * @param traveler
	 * @param visaProducts
	 * @param visaOrderBack
	 * @return
	 */
	private boolean  createSubVisaTravelerMoneyAmount4LMT(MoneyAmountService moneyAmountService,Traveler traveler,VisaProducts visaProducts,VisaOrder visaOrderBack){
		    boolean flag = false;
		    //结算价，此处与原始价保持一致，即为签证产品的应收价
		    /**
			 * wangxinwei 2015-10-18  签证产品预定评审后修改
			 * 1.uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 * 2.代码中方法参数个数删减
			 */
		    String payPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
		    /*MoneyAmount moneyAmount = new MoneyAmount(payPriceSerialNum, //款项UUID
		    		visaProducts.getCurrencyId(),//币种ID
		    		visaProducts.getVisaPay(),//相应币种的金额
		    		traveler.getId(), //订单或游客ID
		    		Context.MONEY_TYPE_JSJ, //款项类型: 游客结算价
		    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
		    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1.游客  2.订单
		    		visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID*/  
		    MoneyAmount moneyAmount = new MoneyAmount();
		    moneyAmount.setSerialNum(payPriceSerialNum);
		    moneyAmount.setCurrencyId(visaProducts.getCurrencyId());
		    /**
    	     * ---需求号C310---
    	     * ---wangxinwei modified 20151020---
    	     * ---游客的成本价改为取产品成本价，不在用应收价  
    	     * visaProducts.getVisaPay() --> visaProducts.getVisaPrice()
    	     */
		    //moneyAmount.setAmount(visaProducts.getVisaPay());
		    moneyAmount.setAmount(visaProducts.getVisaPrice());
		    moneyAmount.setUid(traveler.getId());
		    moneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);
		    moneyAmount.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
		    moneyAmount.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_TRAVELER);
		    moneyAmount.setCreatedBy(visaOrderBack.getCreateBy().getId());
		    
		    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
            if (flag) {
            	travelerService.updateSerialNumByTravelerId(payPriceSerialNum, traveler.getId());
            	flag = false;
			}
		    //保存原始应收价
            /**
             * wangxinwei 2015-10-18  签证产品预定评审后修改
			 * 1.uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 * 2.代码中方法参数个数删减
			 */
            String payPriceSerialNumOrigin = UUID.randomUUID().toString().replace("-", "");
		    /*MoneyAmount moneyAmountOrigin = new MoneyAmount(payPriceSerialNumOrigin, //款项UUID
		    		visaProducts.getCurrencyId(),//币种ID
		    		visaProducts.getVisaPay(),//相应币种的金额
		    		traveler.getId(), //订单或游客ID
		    		Context.MONEY_TYPE_YSJSJ, //款项类型: 原始（游客）结算价
		    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
		    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1.游客  2.订单
		    		visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID*/
            
            MoneyAmount moneyAmountOrigin = new MoneyAmount();
            moneyAmountOrigin.setSerialNum(payPriceSerialNumOrigin);
            moneyAmountOrigin.setCurrencyId(visaProducts.getCurrencyId());
            /**
    	     * ---需求号C310---
    	     * ---wangxinwei modified 20151020---
    	     * ---游客的成本价改为取产品成本价，不在用应收价  
    	     * visaProducts.getVisaPay() --> visaProducts.getVisaPrice()
    	     */
            //moneyAmountOrigin.setAmount(visaProducts.getVisaPay());
            moneyAmountOrigin.setAmount(visaProducts.getVisaPrice());
            moneyAmountOrigin.setUid(traveler.getId());
            moneyAmountOrigin.setMoneyType(Context.MONEY_TYPE_YSJSJ);
            moneyAmountOrigin.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
            moneyAmountOrigin.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_TRAVELER);
            moneyAmountOrigin.setCreatedBy(visaOrderBack.getCreateBy().getId());
            
		    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountOrigin);
		    if (flag) {//游客结算记录成功后要 更新 游客的payPriceSerialNum字段绑定结算记录
		    	travelerService.updateOriginalPayPriceSerialNumByTravelerId(payPriceSerialNumOrigin, traveler.getId());
			}
		    
		    //保存游客成本价------wxw added 2015-03-14-------
		    /**
			 * wangxinwei 2015-10-18  签证产品预定评审后修改
			 * 1.uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 * 2.代码中方法参数个数删减
			 */
            String costPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
		    /*MoneyAmount moneyAmountCost = new MoneyAmount(costPriceSerialNum, //款项UUID
		    		visaProducts.getCurrencyId(),//币种ID
		    		visaProducts.getVisaPay(),//相应币种的金额
		    		traveler.getId(), //订单或游客ID
		    		Context.MONEY_TYPE_CBJ, //款项类型: 游客成本价
		    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
		    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1.游客  2.订单
		    		visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID*/
            
            MoneyAmount moneyAmountCost = new MoneyAmount();
            moneyAmountCost.setSerialNum(costPriceSerialNum);
            moneyAmountCost.setCurrencyId(visaProducts.getCurrencyId());
            /**
    	     * ---需求号C310---
    	     * ---wangxinwei modified 20151020---
    	     * ---游客的成本价改为取产品成本价，不在用应收价  
    	     * visaProducts.getVisaPay() --> visaProducts.getVisaPrice()
    	     */
            //moneyAmountCost.setAmount(visaProducts.getVisaPay());
            moneyAmountCost.setAmount(visaProducts.getVisaPrice());
            moneyAmountCost.setUid(traveler.getId());
            moneyAmountCost.setMoneyType(Context.MONEY_TYPE_CBJ);
            moneyAmountCost.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
            moneyAmountCost.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_TRAVELER);
            moneyAmountCost.setCreatedBy(visaOrderBack.getCreateBy().getId());
		    
		    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountCost);
		    if (flag) {//游客结算记录成功后要 更新 游客的payPriceSerialNum字段绑定结算记录
		    	travelerService.updateCostSerialNumByTravelerId(costPriceSerialNum, traveler.getId());
			}
		    
		    
		    return flag;
	}
	
	/**
	 * 创建签证基本信息
	 * 
	 * @param forecastStartOut:预计出发时间
	 * @param forecastContract；预计签约时间
	 * @param traveler
	 * @param travelerDocInfoIds
	 *key: passport_photo_id护照首页ID
     *       identity_front_photo_id身份证正面ID
     *       identity_back_photo_id身份证背面ID
     *       table_photo_id报名表ID
     *       person_photo_id照片ID
     *       other_photo_id其他图片附件ID（多个附件ID，用&分离）
	 * @return
	 */
	private Visa createSubVisaBasicInfo4LMT(Visa visa, Date forecastStartOut, Date forecastContract, Traveler traveler, Map<String,Long> travelerDocInfoIds) {
		if (visa == null) {
			visa = new Visa();
		}
		visa.setTravelerId(traveler.getId());
		visa.setPassportOperateRemark(traveler.getRemark());
		visa.setRemark(traveler.getRemark());
		visa.setForecastStartOut(forecastStartOut);
		visa.setForecastContract(forecastContract);
		//visa.setVisaStauts(Integer.valueOf(Context.VISA_STATUTS_TO));
		//作为子订单与签证预订时保持一致：默认状态为续补资料 -  2015-03-14--
		visa.setVisaStauts(Integer.valueOf(Context.VISA_STATUTS_RESOURCENEEDED));
		Long passportdocID = travelerDocInfoIds.get("passport_photo_id");
		Long idcardfrontdocID = travelerDocInfoIds.get("identity_front_photo_id");
		Long idcardbackdocID = travelerDocInfoIds.get("identity_back_photo_id");
		
		Long entry_formdocID = travelerDocInfoIds.get("table_photo_id");
		Long photodocID = travelerDocInfoIds.get("person_photo_id");
		Long otherdocID = travelerDocInfoIds.get("other_photo_id");
		
		if (null!=passportdocID) {
			visa.setPassportPhotoId(passportdocID);
		}
		if (null!=idcardfrontdocID) {
			visa.setIdentityFrontPhotoId(idcardfrontdocID);
		}
		if (null!=idcardbackdocID) {
			visa.setIdentityBackPhotoId(idcardbackdocID);
		}
		if (null!=entry_formdocID) {
			visa.setTablePhotoId(entry_formdocID);
		}
		if (null!=photodocID) {
			visa.setPersonPhotoId(photodocID);
		}
		if (null!=otherdocID) {
			visa.setOtherPhotoId(otherdocID);
		}
		visa = visaService.saveVisa(visa);
		return visa;
	}
	
	/**
	 * 处理子订单的结算价记录
	 * @param moneyAmountService
	 * @param visaProducts
	 * @param visaOrderBack
	 * @return
	 */
	private boolean  createSubVisaOrderMoneyAmount4LMT(MoneyAmountService moneyAmountService,VisaProducts visaProducts,VisaOrder visaOrderBack){
	    boolean flag = false;
	    
	  //保存订单应收价
	    /**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 * 代码中方法参数个数删减
		 */
	    String payPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
	    /*MoneyAmount moneyAmount = new MoneyAmount(payPriceSerialNum, 
	    		visaProducts.getCurrencyId(),//币种ID
	    		visaProducts.getVisaPay(),//相应币种的金额
	    		visaOrderBack.getId(), //订单或游客ID
	    		Context.MONEY_TYPE_YSH, //款项类型: 订单应收
	    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
	    		VisaPreOrderController.BUSINDESS_TYPE_ORDER,//1表示订单，2表示游客
	    		visaOrderBack.getCreateBy().getId());*///记录创建人ID, 这里用订单的ID
	    MoneyAmount moneyAmount = new MoneyAmount();
	    moneyAmount.setSerialNum(payPriceSerialNum);//款项UUID
	    moneyAmount.setCurrencyId(visaProducts.getCurrencyId());//币种ID
	    /**
	     * ---需求号C310---
	     * ---wangxinwei modified 20151020---
	     * ---订单的成本价改为取产品成本价，不在用应收价  
	     * visaProducts.getVisaPay() --> visaProducts.getVisaPrice()
	     */
	    //moneyAmount.setAmount(visaProducts.getVisaPay());//相应币种的金额
	    moneyAmount.setAmount(visaProducts.getVisaPrice());//相应币种的金额
	    moneyAmount.setUid(visaOrderBack.getId());//订单或游客ID
	    moneyAmount.setMoneyType(Context.MONEY_TYPE_YSH); //款项类型: 订单应收
	    moneyAmount.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);//订单类型
	    moneyAmount.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_ORDER);//1表示订单，2表示游客
	    moneyAmount.setCreatedBy(visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID
	    
	    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
		
	    //保存原始应收价
	    /**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 * 代码中方法参数个数删减
		 */
	    String payPriceSerialNumOrigin = UUID.randomUUID().toString().replace("-", "");
	    /*MoneyAmount moneyAmountOrigin = new MoneyAmount(payPriceSerialNumOrigin, //款项UUID
	    		visaProducts.getCurrencyId(),//币种ID
	    		visaProducts.getVisaPay(),//相应币种的金额
	    		visaOrderBack.getId(), //订单或游客ID
	    		Context.MONEY_TYPE_YSYSH, //款项类型: 订单原始应收
	    		VisaPreOrderController.VISA_ORDER_TYPE,//订单类型
	    		VisaPreOrderController.BUSINDESS_TYPE_ORDER,//1表示订单，2表示游客
	    		visaOrderBack.getCreateBy().getId());*///记录创建人ID, 这里用订单的ID
	    MoneyAmount moneyAmountOrigin = new MoneyAmount(); //款项UUID
	    moneyAmountOrigin.setSerialNum(payPriceSerialNumOrigin);//款项UUID
	    moneyAmountOrigin.setCurrencyId(visaProducts.getCurrencyId());//币种ID
	    /**
	     * ---需求号C310---
	     * ---wangxinwei modified 20151020---
	     * ---订单的成本价改为取产品成本价，不在用应收价  
	     * visaProducts.getVisaPay() --> visaProducts.getVisaPrice()
	     */
	    // moneyAmountOrigin.setAmount(visaProducts.getVisaPay());//相应币种的金额
	    moneyAmountOrigin.setAmount(visaProducts.getVisaPrice());//相应币种的金额
	    moneyAmountOrigin.setUid(visaOrderBack.getId()); //订单或游客ID
	    moneyAmountOrigin.setMoneyType(Context.MONEY_TYPE_YSYSH); //款项类型: 订单原始应收
	    moneyAmountOrigin.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);//订单类型
	    moneyAmountOrigin.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_ORDER);//1表示订单，2表示游客
	    moneyAmountOrigin.setCreatedBy(visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID
	    
	    flag = moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountOrigin);
	    
	/*    if (flag) {//游客结算记录成功后要 更新 游客的payPriceSerialNum字段绑定结算记录
	    	visaOrderBack.setOriginalTotalMoney(payPriceSerialNumOrigin); //设置原始应收总额的UUID
	    	visaOrderBack.setTotalMoney(payPriceSerialNum); //设置应收总额的UUID
	    	visaOrderBack.setVisaOrderStatus(0);//设置订单的支付状态改为未支付
			saveOrderInfo(visaOrderBack, null);
		}*/
	    
	    //保存订单的成本价
	    /**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 * 代码中方法参数个数删减
		 */
	    String costPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
	    MoneyAmount costMoneyAmount = new MoneyAmount();
	    costMoneyAmount.setSerialNum(costPriceSerialNum); //款项UUID
	    costMoneyAmount.setCurrencyId(visaProducts.getCurrencyId());//币种ID
	    /**
	     * ---需求号C310---
	     * ---wangxinwei modified 20151020---
	     * ---订单的成本价改为取产品成本价，不在用应收价  
	     * visaProducts.getVisaPay() --> visaProducts.getVisaPrice()
	     */
	    //costMoneyAmount.setAmount(visaProducts.getVisaPay());//相应币种的金额
	    costMoneyAmount.setAmount(visaProducts.getVisaPrice());//相应币种的金额
	    costMoneyAmount.setUid(visaOrderBack.getId()); //订单或游客ID
	    costMoneyAmount.setMoneyType(Context.MONEY_TYPE_CBJ); //款项类型: 成本价
	    costMoneyAmount.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);//订单类型
	    costMoneyAmount.setBusindessType(VisaPreOrderController.BUSINDESS_TYPE_ORDER);//1表示订单，2表示游客
	    costMoneyAmount.setCreatedBy(visaOrderBack.getCreateBy().getId());//记录创建人ID, 这里用订单的ID
	    flag = moneyAmountService.saveOrUpdateMoneyAmount(costMoneyAmount);
	    
	    if (flag) {//游客结算记录成功后要 更新 游客的payPriceSerialNum字段绑定结算记录
	    	visaOrderBack.setOriginalTotalMoney(payPriceSerialNumOrigin); //设置原始应收总额的UUID
	    	visaOrderBack.setTotalMoney(payPriceSerialNum); //设置应收总额的UUID
	    	visaOrderBack.setCostTotalMoney(costPriceSerialNum);
	    	visaOrderBack.setVisaOrderStatus(0);//设置订单的支付状态改为未支付
			saveOrderInfo(visaOrderBack, null);
			
			//-------by------junhao.zhao-----2017-01-19-----主要通过visa_order向表order_data_statistics中添加数据+通过参数更改表order_data_statistics订单金额与金额uuid---开始-----
		    List<Map<String, Object>> orderDataStatisticsId = orderDateSaveOrUpdateService.getOrderDataStaIdForVisa(visaOrderBack.getId(), Context.ORDER_TYPE_QZ);
			//根据order_data_statistics中是否存在相应的值，进行修改或添加
			if(orderDataStatisticsId.size()==0 || orderDataStatisticsId.get(0) == null){
				// 向表order_data_statistics中添加数据
		  		orderDateSaveOrUpdateService.insertVisaOrder(visaOrderBack);
			}
			BigDecimal amount = new BigDecimal(0);
			if(moneyAmount.getAmount()!= null && moneyAmount.getExchangerate()!= null){
				amount = moneyAmount.getAmount().multiply(moneyAmount.getExchangerate());
			}
			// 更改表order_data_statistics中的订单金额与金额uuid
			orderDateSaveOrUpdateService.updateMoneyAndUuid(visaOrderBack.getId(), Context.ORDER_TYPE_QZ, amount, payPriceSerialNum);
		    //-------by------junhao.zhao-----2017-01-19-----主要通过visa_order向表order_data_statistics中添加数据+通过参数更改表order_data_statistics订单金额与金额uuid---结束------
	    }
	    
	    return flag;
    }
/**
 * 通过订单ID获取产品所在的部门ID
 * @param orderId
 * @return
 * @author hxd
 */
	public Long getDeptId(String orderId)
	{
		List<Map<Object, Object>> list=visaOrderDao.findBySql("SELECT vp.deptId FROM visa_order vo  INNER JOIN visa_products vp ON vo.visa_product_id= vp.id WHERE vo.id="+orderId, Map.class);
		if(list.size() >0)
		{
			return Long.parseLong(list.get(0).get("deptId").toString());
		}
		else
		return 0L;
	
	}
/**
 * 根据订单编号查询该订单所属的游客记录。
 * @param orderId
 * @return
 */
public List<Map<String, Object>> getTravellerInfoByOrderId(String orderId)
{
	StringBuffer buffer = new StringBuffer();
	buffer.append("SELECT t.name,t.id tid,v.total_deposit,v.visa_stauts,v.guarantee_status,v.passport_operate_remark,t.orderId,'price','currencyMark','currencyId'   ");
	buffer.append("FROM traveler t INNER JOIN visa_order vo  ON t.orderId = vo.id ");
	buffer.append("INNER JOIN visa v ON t.id =v.traveler_id WHERE vo.id =");
	buffer.append(orderId);
	buffer.append(" AND t.delFlag=0");
	return travelerDao.findBySql(buffer.toString(), Map.class);
}


	
	// -------------wangxinwei 拉美途创建签证子订单接口结束--------------

	
	
	/**
	 * @Description: 签证预定列表，查询产品的已有有效订单；
	 * 对应需求编号C361:
	 * 1.在“报名”模块的“签证”菜单列表中，增加查看已收明细的功能，可查看该团下已收明细数据。  
	 * 2.在列表的“操作”栏中增加“已收明细”功能，点击可查看该团下已收明细数据，再点击收起；
	 * @author xinwei.wang
	 * @date 2015年11月13日上午11:18:40
	 * @param visaproductid：产品id
	 * @param visaOrderList    
	 * @throws
	 */
    public void getOrderListByVisaProductId(String visaproductid, List<Map<String, String>> visaOrderList) {
		/**
		 * 获取产品下的有效订单
		 * 条件：vo.del_flag = 0 AND vo.visa_order_status <> 100
		 */
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("vo.id vorderid,vo.order_no,vo.salerName,vo.create_by,DATE_FORMAT(vo.create_date,'%Y-%m-%d %H:%i:%s') create_date,vo.travel_num,vo.payStatus, ");
		buffer.append("vo.total_money,vo.payed_money,vo.accounted_money ");
		buffer.append("FROM visa_order vo ");
		buffer.append("LEFT JOIN visa_products vp ON vo.visa_product_id = vp.id ");
		buffer.append("WHERE vo.del_flag = 0 AND vo.payStatus != 99 and vo.payStatus != 111 AND vo.visa_order_status <> 100 AND vp.id =");
		buffer.append("'"+visaproductid+"'");
		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		Integer num = 0;
		for(Map<String, Object> map : list) {
			Map<String, String> visaOrderMap = new HashMap<String, String>();
			//序号
			num++;
			visaOrderMap.put("num", num.toString());
			//订单编号
			visaOrderMap.put("order_no", map.get("order_no").toString());
			//销售
			visaOrderMap.put("salerName", map.get("salerName").toString());
			//下单人
			Long userid = Long.parseLong(map.get("create_by").toString());
			visaOrderMap.put("create_by", UserUtils.getUserNameById(userid));
			//预定时间
			visaOrderMap.put("create_date", map.get("create_date").toString());
			//订单人数
			visaOrderMap.put("travel_num", map.get("travel_num").toString());
			//订单状态   1-未支付 3-预定   5-已支付 99-已取消',
			String payStatus = map.get("payStatus").toString();
			if ("1".equals(payStatus)) {
				visaOrderMap.put("payStatus","未支付");
			}else if ("3".equals(payStatus)) {
				visaOrderMap.put("payStatus","预定");
			}else if ("5".equals(payStatus)) {
				visaOrderMap.put("payStatus","已支付");
			}else if ("99".equals(payStatus)) {
				visaOrderMap.put("payStatus","已取消");
			}else {
				visaOrderMap.put("payStatus","");
			}
			//订单总额
			String total_money = map.get("total_money").toString();
			String total_money_str = moneyAmountService.getMoneyStr(total_money);
			visaOrderMap.put("total_money", total_money_str);
			//已付金额
			String payed_money = map.get("payed_money").toString();
			String payed_money_str = moneyAmountService.getMoneyStr(payed_money);
			visaOrderMap.put("payed_money", payed_money_str);
			//到账金额
			String accounted_money = map.get("accounted_money").toString();
			String accounted_money_str = moneyAmountService.getMoneyStr(accounted_money);
			visaOrderMap.put("accounted_money", accounted_money_str);
			visaOrderList.add(visaOrderMap);
		}
	}

	
	
	/**
	 * 修改订单时新增游客：对应需求号c333
	 * @param orderId
	 * @param travelerList
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Traveler saveTravelers4orderupdate(Long orderId, List<Traveler> travelerList) {
		VisaOrder  visaOrder  = findVisaOrder(orderId);
		VisaProducts visaProducts = visaProductsDao.findOne(visaOrder.getVisaProductId());
		//保存游客信息
		if (null != orderId) {//
			travelerDao.findTravelerByOrderIdAndOrderType(orderId,VisaPreOrderController.VISA_ORDER_TYPE);
		}
		Traveler rTraveler = null;
		for (Traveler traveler : travelerList) {
			
			if (traveler.getId() == null) {
				rTraveler = traveler;
				if (null == traveler.getDelFlag()) {
					traveler.setDelFlag(0);
				}
			} else {
				rTraveler = travelerDao.findOne(traveler.getId());
				if (null == traveler.getDelFlag()) {
					traveler.setDelFlag(0);
				}
				updateTravelerAttr(traveler, rTraveler);
			}
			rTraveler.setOrderId(orderId);
			rTraveler.setSrcPrice(visaProducts.getVisaPay());
			rTraveler.setSrcPriceCurrency(currencyDao.findOne(visaProducts.getCurrencyId().longValue()));
			rTraveler.setOrderType(VisaPreOrderController.VISA_ORDER_TYPE);
			
			/*
			 * 2015-04-07王新伟添加
			 * 添加借款UUID：如果有游客借款成功后用此UUID与MoneyAmount表建立关联关系
			 */
			String jkSerialNum = UUID.randomUUID().toString();
			rTraveler.setJkSerialNum(jkSerialNum);
			rTraveler.setRebatesMoneySerialNum(UUID.randomUUID().toString());
			
			
			/*
			 * 2015-04-07王新伟添加
			 * 添加借款UUID：如果有游客借款成功后用此UUID与MoneyAmount表建立关联关系
			 */
			String payPriceSerialNum = UUID.randomUUID().toString();
			rTraveler.setPayPriceSerialNum(payPriceSerialNum);
			
			String originalPayPriceSerialNum = UUID.randomUUID().toString();
			rTraveler.setOriginalPayPriceSerialNum(originalPayPriceSerialNum);
			
			String costPriceSerialNum = UUID.randomUUID().toString();
			rTraveler.setCostPriceSerialNum(costPriceSerialNum);
			
			rTraveler = travelerDao.save(rTraveler);
		}
		return rTraveler;
	}
	
	
	/**
	 * @Description: 根据币种相加， 对应需求号c333
	 * @author xinwei.wang
	 * @date 2015年11月23日下午7:37:10
	 * @param moneyAmounts
	 * @param type：0：成本价；1：结算价
	 * @return    
	 * @throws
	 */
	public List<MoneyAmount> moneyAmountsAdd(String uuid1, String uuid2,
			String flag){

		List<MoneyAmount> result = new ArrayList<MoneyAmount>();
		List<MoneyAmount> resultfinal = new ArrayList<MoneyAmount>();

		List<MoneyAmount> list1 = moneyAmountDao.findAmountBySerialNum(uuid1);
		List<MoneyAmount> list2 = moneyAmountDao.findAmountBySerialNum(uuid2);
		
		Integer busindessType = list1.get(0).getBusindessType();
		Long uid = list1.get(0).getUid();
		String serialNumOrigin = list1.get(0).getSerialNum();
		Integer moneytype = list1.get(0).getMoneyType();

		Map<String, MoneyAmount> map1 = new LinkedHashMap<>();
		if (list1 != null) {
			for (MoneyAmount ma1 : list1) {
				map1.put(ma1.getCurrencyId().toString(), ma1);
			}
		}

		if (list2 != null) {
			for (MoneyAmount ma2 : list2) {
				String key = ma2.getCurrencyId().toString();
				if (map1.containsKey(key)) {
					if ("add".equals(flag)) {
						MoneyAmount moneyAmount = map1.get(key);
						moneyAmount.setAmount(moneyAmount.getAmount().add(ma2.getAmount()));
						map1.put(key, moneyAmount);
					} else {
						MoneyAmount moneyAmount = map1.get(key);
						moneyAmount.setAmount(moneyAmount.getAmount().subtract(ma2.getAmount()));
						map1.put(key, moneyAmount);
					}
				} else {
					if ("add".equals(flag)) {
						MoneyAmount moneyAmount = ma2;
						map1.put(ma2.getCurrencyId().toString(), moneyAmount);
					} else {
						MoneyAmount moneyAmount =ma2;
						moneyAmount.setAmount(new BigDecimal(0).subtract(ma2.getAmount()));
						map1.put(key,moneyAmount);
					}
				}
			}
		}

		for (String key : map1.keySet()) {
			result.add(map1.get(key));
		}
		
		for (MoneyAmount  moneyAmountOrigin: result) {
			MoneyAmount moneyAmount = new MoneyAmount();
			moneyAmount.setSerialNum(serialNumOrigin);
			moneyAmount.setCurrencyId(moneyAmountOrigin.getCurrencyId());
			moneyAmount.setAmount(moneyAmountOrigin.getAmount());
			moneyAmount.setUid(uid);
			moneyAmount.setMoneyType(moneytype);
			moneyAmount.setOrderType(6);
			moneyAmount.setBusindessType(busindessType);
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			resultfinal.add(moneyAmount);
		}
		
		return resultfinal;
	}
	
	/**
	 * @Description: 根据订单Id判断该订单是否为子订单，  对应需求号c333
	 * @author xinwei.wang
	 * @date 2015年11月24日下午7:44:42
	 * @param orderId
	 * @return  0:不是子订单； 1：子订单   
	 * @throws
	 * 
	 * -------------------------------------------------
	 * 20151221  modified by wangxinwei
	 * 游客表中添加 isjoingroup 字段，用来  区别 参团 订单  与  签证子订单
	 * 
	 */
	public int judgeOrderIdIsSubOrder(VisaOrder visaOrder){
		
		int temp = 0;
		//拉美途的的子订单有 MainOrderId，不用再通过游客进行判断
		if (null!=visaOrder.getMainOrderId()) {
			temp = 1;
			return temp;
		}
		//处理老数据中没有MainOrderId
		List<Traveler> travelerList = travelerDao.findTravelerByOrderIdAndOrderType(visaOrder.getId(),6); 
		if (null!=travelerList&&travelerList.size()>0) {
			for (Traveler traveler : travelerList) {
				Long maiorderid = traveler.getMainOrderId();
				Long maiordertravelerid = traveler.getMainOrderTravelerId();
				Integer isjoingroup = traveler.getIsjoingroup();
				if (isjoingroup==1) { // 参团订单一定不是子订单
					temp = 0;
					break;
				}else{
					if (null!=maiorderid||null!=maiordertravelerid) {
						temp = 1;
						break;
					}
				}
				
			}
		}else {
			temp = 1;
		}
		return temp;
	}

	public void updateOrderContacts(String orderId, String data,String agentId,String agent_name) {
		//添加新的记录入库
		String tmp[] = data.split("contactsName");
		String newArray [] = new String[tmp.length-1];
		for (int i=1 ;i< tmp.length ;i++) {
			newArray[i-1] = "contactsName"+tmp[i];
		}
		List<Map<String, String>> maps =  new ArrayList<>();
		for(int j=0;j<newArray.length;j++) {
			Map<String,String> map = new HashMap<>();
			String [] a = newArray[j].split("&");
			for(int k=0;k<a.length;k++) {
				String b[] = a[k].split(",");
				for(int m = 0;m<b.length;m++) {
					String c[] = b[m].split("=");
					if(c.length>1)
						map.put(c[0],c[1]);
					else
						map.put(c[0], "");
				}
			}
			maps.add(map);
		}

		//日志
		logOrderService.saveLogOrder4Agent(orderId, maps, agent_name);

		//删除旧的联系人记录数据
		String sql = "DELETE FROM ordercontacts  WHERE orderId ="+ orderId+ " AND orderType=6";
		contactsDao.updateBySql(sql);

		String sq = "UPDATE visa_order vo SET vo.agentinfo_id="
				+ agentId+ " , vo.agentinfo_name ='"+ agent_name+ "' WHERE vo.id="
				+ Long.parseLong(orderId);
		contactsDao.updateBySql(sq);
	     
		for(int i=0;i<maps.size();i++) {
			 OrderContacts contacts  = new OrderContacts();
			 contacts.setAgentId(Long.parseLong(agentId));
			 contacts.setContactsAddress(maps.get(i).get("contactsAddress"));
			 contacts.setContactsEmail(maps.get(i).get("contactsEmail"));
			 contacts.setContactsFax(maps.get(i).get("contactsFax"));
			 contacts.setContactsName(maps.get(i).get("contactsName"));
			 contacts.setContactsQQ(maps.get(i).get("contactsQQ"));
			 contacts.setContactsTel(maps.get(i).get("contactsTel"));
			 contacts.setContactsTixedTel(maps.get(i).get("contactsTixedTel"));
			 contacts.setContactsZipCode(maps.get(i).get("contactsZipCode"));
			 contacts.setOrderId(Long.parseLong(orderId));
			 contacts.setOrderType(6);
			 contacts.setRemark(maps.get(i).get("remark"));
			 contactsDao.saveObj(contacts);
		}
	}


	
	/**
	 * 修改页保存特殊需求备注
	 * @param remark
	 */
	public void doUpdateVisaOrderRemark(String orderId,String remark){
		VisaOrder visaOrder = visaOrderDao.findByOrderId(Long.valueOf(orderId));
		if(!remark.equals(visaOrder.getRemark())) {
			String remarkTemp = visaOrder.getRemark();
			if(remarkTemp == null) {
				remarkTemp = "";
			}
			logOrderService.saveObj("remark", Long.valueOf(orderId), logOrderService.content("特殊需求", remarkTemp, remark), 2);
		}
		String sql = "UPDATE visa_order SET remark=? where id=? ";
		visaOrderDao.updateBySql(sql, remark,orderId);
	}

	/**
	 * 根据渠道id查找签证订单信息，并且排除payStatus 99：已取消，delFlag 1：已删除的订单
	 */
	public List<VisaOrder> findByAgentId(Long agentId){
		return visaOrderDao.findByAgentId(agentId);
	}
	
	/*
	 * 根据产品id获取获取订单id和订单No
	 * @param productId 产品id
	 * @return 订单id和No的map列表
	 * @author xianglei.dong 
	 */
	public List<Map<String, Object>> findOrderIdAndNoByProductId(String productId){
		return visaOrderDao.findOrderIdAndNoByProductId(productId);
	}
   /**
    * 0064需求,根据批次号,获得该批次已有的traveler_id和visa_id
    * 根据batchNo和business_type=1为借款查询batch_traveler_relation表
    * @param joinBatchNo
    * @return
    */
	public List<Map<String,Object>>  getTravelerIdsVisaIds(String joinBatchNo) {
		StringBuffer sql=new StringBuffer();
		List<Map<String,Object>>  infoMap=new ArrayList<>();
		sql.append(" SELECT btr.traveler_id AS traveler_id,btr.visa_id AS visa_id ")
		   .append(" FROM batch_traveler_relation btr ")
		   .append(" LEFT JOIN traveler t ")
		   .append(" ON btr.traveler_id=t.id ")
		   .append(" WHERE btr.batch_record_no='"+joinBatchNo+"' AND btr.business_type='1'  ");
		try {
			infoMap=visaOrderDao.findBySql(sql.toString(),Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoMap;
	}
   /**
    * 0064需求,游客加入批次-s-批发商环球行,查看该批次的提交情况
    * @param batchNo
    * @return
    */
   public  List<Map<String, Object>> checkSubmit(String batchNo) {
	StringBuffer sql=new StringBuffer();
	sql.append(" SELECT DISTINCT(btr.is_submit)AS submit  ")
	   .append(" FROM batch_traveler_relation btr ")
	   .append(" WHERE btr.batch_record_no='"+batchNo+"' AND btr.business_type=1; ");
	  List<Map<String, Object>> tempList=visaOrderDao.findBySql(sql.toString(),List.class);
	return tempList;
}
/**
 * 0318需求
 * 获得要修改的游客的基本信息,尤其是游客的签证状态和借款状态 
 * @param request
 * @param response
 * @param visaOrderForm
 * @param shenfen:身份
 * @param common
 * @param travelerId
 * @return
 */
public Page<Map<String, Object>> searchTravelerListByTravelerId(HttpServletRequest request,
		HttpServletResponse response, VisaOrderForm visaOrderForm, String shenfen, DepartmentCommon common,
		String travelerId) {

	
	//签证查询订单信息
	StringBuffer sqlBuffer = new StringBuffer();
	
	sqlBuffer.append(" SELECT ");
	sqlBuffer.append(" t.name travelerName, ");//0游客名称
	sqlBuffer.append(" t.passportCode passportId, ");//1护照编号
	sqlBuffer.append(" v.AA_code aACode, ");//2AA码
	sqlBuffer.append(" vp.visaType visaType, ");//3签证类型
	sqlBuffer.append(" sc.countryName_cn visaCountry, ");//4签证国家
	sqlBuffer.append(" v.forecast_start_out forecastStartOut, ");//5预定出团时间
	sqlBuffer.append(" v.forecast_contract forecastContract, ");//6预计签约时间
	sqlBuffer.append(" v.start_out startOut, ");//7实际出团时间
	sqlBuffer.append(" v.contract contract, ");//8实际签约时间
	sqlBuffer.append(" v.visa_stauts visaStatus, ");//9签证状态
	sqlBuffer.append(" t.passportStatus passportStatus, ");//10护照状态
	sqlBuffer.append(" v.guarantee_status guaranteeStatus, ");//11担保类型
	sqlBuffer.append(" v.total_deposit totalDepositUUID, ");//12应收押金UUID
	sqlBuffer.append(" v.accounted_deposit accountedDeposit, ");//13达账押金UUID
	sqlBuffer.append(" t.id id, ");//14游客主键id
	sqlBuffer.append(" v.id visaId, ");//15 visa表的id
	sqlBuffer.append(" vp.id visaProductId, ");//16 签证产品id
	sqlBuffer.append(" vo.id visaorderId, ");//17 visa_order表id
	sqlBuffer.append(" vo.order_no visaorderNo,vo.mainOrderId main_orderId, ");//18 visa_order表的订单编号
	sqlBuffer.append(" vo.agentinfo_id agentinfoId, ");//19 visa_order表渠道IDagentId
	sqlBuffer.append(" v.payed_deposit payedDeposit, ");//20  已付押金UUID
	sqlBuffer.append(" su.name creatUser, ");//21下单人
	sqlBuffer.append(" vo.create_date createTime, ");//22下单时间
	sqlBuffer.append(" po.orderStatus groupType, ");//23参团类型
	sqlBuffer.append(" vo.lockStatus lockStatus, ");//24订单锁死状态
	sqlBuffer.append(" t.payPriceSerialNum payPriceSerialNum, ");//25游客的应收uuid
	sqlBuffer.append(" t.payed_moneySerialNum payedMoneySerialNum, ");//26游客的已付uuid
	sqlBuffer.append(" t.main_order_id mainOrderId, ");//27游客的主订单id
	sqlBuffer.append(" po.orderNum mainOrderNum, ");//28游客的主订单编号
	sqlBuffer.append(" ag.groupCode cantuantuanhao, ");//29游客的参团团号
	sqlBuffer.append(" t.accounted_money accountedMoney, ");//30游客的达账uuid
	sqlBuffer.append(" t.payment_type paymentType, ");//31游客的结算方式
	sqlBuffer.append(" v.actual_delivery_time deliveryTime, ");//32 实际送签时间
	sqlBuffer.append(" vp.lockStatus activityLockStatus, ");//33 visa_products锁死状态
	sqlBuffer.append(" vp.collarZoning collarZoning, ");//34 领区
	sqlBuffer.append(" vo.update_date updateDate, ");//35更新时间
	if(UserUtils.getUser().getCompany().getId()==68){
		sqlBuffer.append(" IFNULL(re.STATUS ,re2.STATUS) STATUS, ");//36review状态
	} else {
		sqlBuffer.append(" re.STATUS STATUS, ");//36review状态
	}
	
	//37 （最大）说明会时间
	sqlBuffer.append(" MAX(vin.explanation_time) explanationTime ");
	
	sqlBuffer.append(" FROM traveler t ");
	sqlBuffer.append(" LEFT JOIN visa v on t.id = v.traveler_id ");
	sqlBuffer.append(" LEFT JOIN visa_order vo on vo.id = t.orderId ");
	sqlBuffer.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ");
	sqlBuffer.append(" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id ");
	sqlBuffer.append(" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId ");
	sqlBuffer.append(" LEFT JOIN sys_user su on su.id = vo.create_by ");
	sqlBuffer.append(" LEFT JOIN productorder po ");
	sqlBuffer.append(" on po.id = t.main_order_id ");
	sqlBuffer.append(" LEFT JOIN activitygroup ag ");
	sqlBuffer.append(" on ag.id = po.productGroupId ");
	
	if(UserUtils.getUser().getCompany().getId()==68){
		sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr, (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 AND re1.companyId = 68 GROUP BY re1.travelerId) r WHERE rr.id = r.id) re ON t.id = re.travelerId ");
		sqlBuffer.append(" LEFT JOIN (SELECT rr.order_id orderId,rr.status status,rr.traveller_id travelerId,rr.create_date createDate FROM review_new rr, (SELECT MAX(re1.id_long) id FROM review_new re1 WHERE re1.process_type = '5' AND re1.product_type = '6' AND re1.company_id = '" + Context.SUPPLIER_UUID_HQX + "' GROUP BY re1.traveller_id) r WHERE rr.id_long = r.id) re2 ON concat(t.id) = re2.travelerId ");
		//解決bug 13826
		//sqlBuffer.append(" LEFT JOIN ((SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) union (SELECT rr.order_id orderId,rr.status status,rr.traveller_id travelerId,rr.create_date createDate FROM review_new rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review_new re1 WHERE re1.process_type = '5' AND re1.product_type = '6' GROUP BY re1.traveller_id) r ON rr.id = r.id)) re ON t.id = re.travelerId ");
		
	}else if(UserUtils.getUser().getCompany().getId()==71) {
		sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
	}else{
		//sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		//解決bug 13098
		sqlBuffer.append(" LEFT JOIN ((SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) union (SELECT rr.order_id orderId,rr.status status,rr.traveller_id travelerId,rr.create_date createDate FROM review_new rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review_new re1 WHERE re1.process_type = '5' AND re1.product_type = '6' GROUP BY re1.traveller_id) r ON rr.id = r.id) ) re ON concat(t.id) = re.travelerId ");
	}
	//查询新建面签通知时填写的说明会时间
	sqlBuffer.append("LEFT JOIN visa_interview_notice_traveler vint on t.id = vint.travaler_id ");
	sqlBuffer.append("LEFT JOIN visa_interview_notice vin on vint.interview_id = vin.id and vin.del_flag='0' ");
	
	//部门ID不为空时拼接部门SQL
	if (StringUtils.isNotBlank(common.getDepartmentId())) {
		departmentSQL(common,sqlBuffer,1);
	}
	
	sqlBuffer.append(" where t.order_type = 6 and t.companyId = " + UserUtils.getUser().getCompany().getId() + " and t.delFlag=0 and vo.visa_order_status != 100 and vo.del_flag = 0 and vo.create_by IS NOT NULL ");;
	sqlBuffer.append(" and a.status = 1 and t.id="+travelerId+" ");
	
	//部门ID不为空时拼接部门SQL的where条件
	if (StringUtils.isNotBlank(common.getDepartmentId())) {
		departmentSQL(common,sqlBuffer,2);
	}
	
	boolean flag=false;
	/** 部门之间人员是否能相互查看，默认不能  add by 2016/01/18 for requirement ID 113*/
	boolean LisenceFlag = whetherSelectAllDeptDate(common);
	//销售签证订单过滤渠道 true:只能看自己渠道 false:看所有渠道
	if("xiaoshou".equals(shenfen)){
		String userType = UserUtils.getUser().getUserType();
		//如果不是销售经理或管理员，则用户只能查看自己负责的渠道
		if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)){
			boolean isManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_MANAGER);
			boolean isSaleManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES_EXECUTIVE);
			boolean isFinance = false;
			List<UserJob> list = userJobDao.getUserJobList(UserUtils.getUser().getId());
			for (UserJob userJob:list) {
				String jobName = userJob.getJobName();
				if(jobName.indexOf("财务") != -1){
					isFinance = true;
					break;
				}
			}
			if(isFinance){
				flag=false;
			}else if(!isManager && !isSaleManager) {
				flag=true;
			}else{
				flag=false;
			}
			
			//如果配置相互查看权限，则拥有销售经理权限
			if(LisenceFlag){
				flag=false;
			}
		}
	}
	if(flag){
		sqlBuffer.append("AND (a.agentSalerId="+UserUtils.getUser().getId()+" OR a.id=-1) ");
	}else{
		sqlBuffer.append("AND (a.supplyId="+UserUtils.getUser().getCompany().getId()+" OR a.id=-1) ");
	}
	
	//拼接筛选条件
	String mainOrderId = request.getParameter("mainOrderIdYouKe");
	searchTravlelerSQL(visaOrderForm,sqlBuffer,shenfen,mainOrderId);
	
	//只能看本供应商下的签证订单
	sqlBuffer.append(" and su.companyId = "+UserUtils.getUser().getCompany().getId()+" ");
	
	//依游客id分组以便查询该游客最大说明会时间
	sqlBuffer.append(" GROUP BY t.id ");
	//筛选说明会时间
	if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingStart()) || StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingEnd())){
		//说明会时间-开始时间
		if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingStart())){
			sqlBuffer.append(" having MAX(vin.explanation_time) >= '"+visaOrderForm.getExplanationMeetingStart()+":00"+"' ");
			//说明会时间-结束时间
			if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingEnd())){
				sqlBuffer.append(" and MAX(vin.explanation_time) <= '"+visaOrderForm.getExplanationMeetingEnd()+":59"+"' ");
			}
		}else{
			//说明会时间-结束时间
			if(StringUtils.isNotBlank(visaOrderForm.getExplanationMeetingEnd())){
				sqlBuffer.append(" having MAX(vin.explanation_time) <= '"+visaOrderForm.getExplanationMeetingEnd()+":59"+"' ");
			}
		}
	}
	
	//列表排序
	String orderBy = request.getParameter("youkeOrderBy");
	Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
	if (StringUtils.isBlank(orderBy)) {
		page.setOrderBy("t.id DESC");
	} else {
		page.setOrderBy(orderBy);
	}
	
	//分页查询游客数据
	Page<Map<String,Object>> travelerList = visaOrderDao.findBySql(page, sqlBuffer.toString(),Map.class);
	
	//游客查询结果，页面展示数据处理
	travelerData(travelerList.getList(),shenfen);
	
	return travelerList;
	
}



/**
 * 校验批量设置面签通知
 * @author jiawei.du  需求0065&0099
 * @param visaIds 签证id 以','分割
 * @param travellerIds 游客id 以','分割
 * @return map
 */
public Map<String, Object> checkInterviewNotice(String visaIds, String travellerIds) {
	String hql  = " select vo.salerName as salerName ,vo.travel_num as travelerNum , t.id as tid , t.name as tname , t.orderId as orderId , t.id as travellerId, vp.sysCountryId as visaCountryId, vp.collarZoning as collarZoning, visa.id as visaId " +
			" from visa_order vo LEFT JOIN traveler t on t.orderId  = vo.id ";
	hql += "LEFT JOIN visa  visa on visa.traveler_id = t.id and visa.id in (" +visaIds+ ")";
	hql += "LEFT JOIN visa_products vp on vp.id = vo.visa_product_id where t.id in ("+travellerIds+")";
	List<Map<String, Object>> rightList = visaOrderDao.findBySql(hql,Map.class);
//	List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
//	String curDate = DateUtils.getDate();
//	String personName = UserUtils.getUser().getName();
//	for(Map<String,Object> map:resultList){	
//		rightList.add(map);
//	}
	Map<String,Object> result = new HashMap<String,Object>();
	result.put("rightList", rightList);
	return result;
}


/**
 * 批量设置面签通知批次查询     需求号:0064&0099   addby:djw
 * @return
 */
public Page<Map<String, Object>> batchEditInterviewNotice(
		HttpServletRequest request, HttpServletResponse response,
		Map<String, String> condition) {
	
	String  batchNo = condition.get("batchNo");
	String  txnPerson = condition.get("txnPerson");
	String  createTimeStart = condition.get("createTimeStart");
	String  createTimeEnt = condition.get("createTimeEnt");
	String  travellerName = condition.get("travellerName");
	String  country = condition.get("country");  //签证国家
	String  area = condition.get("area");    //签证领区
	String  orderBy = condition.get("orderBy");
	
	StringBuffer sqlBuffer = new StringBuffer();
//	sqlBuffer.append(" select");
//	sqlBuffer.append(" batchNo,");
//	sqlBuffer.append(" txnPerson,");
//	sqlBuffer.append(" createDate,");
//	sqlBuffer.append(" visaIds,");
//	sqlBuffer.append(" travelerIds,");
//	sqlBuffer.append(" orderIds");
//	sqlBuffer.append(" FROM");
//	sqlBuffer.append(" (");
	sqlBuffer.append(" SELECT ");
	sqlBuffer.append(" vb.batch_no batchNo, ");
	sqlBuffer.append(" vb.create_user_name txnPerson, ");
	sqlBuffer.append(" vb.create_time createTime, ");
	sqlBuffer.append(" GROUP_CONCAT(bt.visa_id) visaIds, ");
	sqlBuffer.append(" GROUP_CONCAT(bt.traveler_id) travelerIds, ");
	sqlBuffer.append(" GROUP_CONCAT(bt.order_id) orderIds, ");
	sqlBuffer.append(" GROUP_CONCAT(vi.country) countrys, ");
	sqlBuffer.append(" GROUP_CONCAT(vi.area) areas ");
	sqlBuffer.append(" FROM");
	sqlBuffer.append(" visa_interview_notice vi ");
	sqlBuffer.append(" LEFT JOIN visa_interview_notice_traveler vint ON vint.interview_id = vi.id ");
	sqlBuffer.append(" LEFT JOIN traveler t ON t.id = vint.travaler_id ");
	sqlBuffer.append(" LEFT JOIN visa_flow_batch_opration vb ON vb.batch_no = vi.batch_no ");
	sqlBuffer.append(" LEFT JOIN visa v ON v.traveler_id = t.id ");
	sqlBuffer.append(" LEFT JOIN batch_traveler_relation bt ON bt.traveler_id = t.id AND bt.batch_record_no = vi.batch_no ");
	sqlBuffer.append(" LEFT JOIN sys_user su ON su.id = vb.create_user_id ");
	
	sqlBuffer.append(" WHERE bt.business_type=5 and vi.del_flag = 0 and vb.busyness_type=3 and su.companyId="+UserUtils.getUser().getCompany().getId());
	if(!StringUtils.isBlank(batchNo)){
		sqlBuffer.append(" and vb.batch_no = '"+batchNo+"' ");
	}
	if(!StringUtils.isBlank(txnPerson)){
		sqlBuffer.append(" and vb.create_user_name like '%"+txnPerson+"%' ");
	}
	

	if(!StringUtils.isBlank(createTimeStart)){
		sqlBuffer.append(" and bt.save_time >='"+createTimeStart+" 00:00:00.0'");
	}
	if(!StringUtils.isBlank(createTimeEnt)){
		sqlBuffer.append(" and bt.save_time <='"+createTimeEnt+" 23:59:59.0'");
	}
	//sb.append(" and vb.is_submit = '1'");
	
	if(!StringUtils.isBlank(travellerName)){
		sqlBuffer.append(" and bt.traveler_name = '"+travellerName+"' ");
	}
//	if(!StringUtils.isBlank(area)){
//		sqlBuffer.append(" and bt.traveler_name = '"+travellerName+"' ");
//	}
//	if(!StringUtils.isBlank(country)){
//		sqlBuffer.append(" and bt.traveler_name = '"+travellerName+"' ");
//	}
	sqlBuffer.append(" group by vb.batch_no");
	
	//sqlBuffer.append(" ) u");
	
	Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
	if (StringUtils.isBlank(orderBy)) {
		page.setOrderBy(" u.createTime DESC");
	} else {
		page.setOrderBy(orderBy);
	}
	//System.out.println(sb.toString());
	String sql =" select batchNo, txnPerson, createTime, visaIds, travelerIds, orderIds, countrys, areas from ( "+sqlBuffer.toString()+" ) u";	
	sql += " where 1 = 1 ";
	if (!StringUtils.isBlank(country)) {
		String countryName = CountryUtils.getCountry(Long.parseLong(country)).getCountryName_cn();
		sql += " and countrys like '%"+countryName+"%' ";
	}
	
	if (!StringUtils.isBlank(area)) {
		Dict dict = DictUtils.getDict(area, "from_area");
		String areaName = dict.getLabel();
		sql += " and areas like '%"+areaName+"%' ";
	}
	
	return visaFlowBatchOprationDao.findBySql(page,sql,Map.class);
}

/**
 * 需求号:0065&0099  addby:djw
 * 查找面签通知游客信息
 * @param conditon
 * @return
 */
public List<Map<String, Object>> findBatchMqtzTraveler(Map<String,String> conditon){
	String batchNo = conditon.get("batchNo");
	String areaId = conditon.get("areaId");
	String countryId = conditon.get("countryId");
	String travelerName = conditon.get("travelerName");
	String hql  = "select " +
			" t.id tid, " +
			" t.`name` tname, " +
			" vo.salerName salerName, " +   
			" vo.id orderId, " +   
			" vin.country country, " +
			" vin.area area, " +
			" vin.address address, " +
			" vin.interview_time interviewTime, " +
			" vin.explanation_time explanationTime, " +
			" vin.contact_man contactMan, " +
			" vin.contact_way contactWay " +
			" from visa_interview_notice vin " +
			" LEFT JOIN visa_interview_notice_traveler vint ON vint.interview_id = vin.id "+
			" LEFT JOIN traveler t ON t.id = vint.travaler_id " +
			" LEFT JOIN visa v ON v.traveler_id = t.id " +
			" LEFT JOIN visa_order vo ON vo.id = t.orderId " +
			" WHERE vin.batch_no = '"+batchNo+"' ";
	if(StringUtils.isNotBlank(countryId)){
		String countryName = CountryUtils.getCountryName(Long.parseLong(countryId));
		hql += " AND vin.country = '" + countryName + "'";
	}
	
	if(StringUtils.isNotBlank(areaId)){
		String areaName = DictUtils.getDict(areaId, "from_area").getLabel();
		areaName = "\"" + areaName +"\"";
		hql += " AND vin.area like'%" + areaName + "%'" ;
	}
	if(StringUtils.isNotBlank(travelerName)){
		hql += " AND t.`name` = '" + travelerName + "'" ;
	}
	
	List<Map<String, Object>> list = batchTravelerRelationDao.findBySql(hql,Map.class);
	//List<Dict> dictList = findDictByType("new_visa_type");
	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	for(Map<String, Object> map:list){
		//约签时间
		if(null != map.get("interviewTime") && !"".equals(map.get("interviewTime"))){
			String interviewTime = DateUtils.formatCustomDate((Date)map.get("interviewTime"),"yyyy-MM-dd hh:mm");
			map.put("interviewTime", interviewTime);
		}else{
			map.put("interviewTime", "");
		}
		//说明会时间
		if(null != map.get("explanationTime") && !"".equals(map.get("explanationTime"))){
			String explanationTime = DateUtils.formatCustomDate((Date)map.get("explanationTime"),"yyyy-MM-dd hh:mm");
			map.put("explanationTime", explanationTime);
		}else{
			map.put("explanationTime", "");
		}
		//领区处理
		if(null != map.get("area") && !"".equals(map.get("area"))){
			JSONArray json = JSONArray.fromObject(map.get("area"));
			String str = "";
			for(int i=0;i<json.size();i++){
				if(i==json.size()-1){
					str+=((Map<String,String>)(json.get(i))).get("areaName");
				}else{
					str+=((Map<String,String>)(json.get(i))).get("areaName")+",";
				}
			}
			//String explanationTime = DateUtils.formatCustomDate((Date)map.get("explanationTime"),"yyyy-MM-dd");
			map.put("area", str);
		}else{
			map.put("area", "");
		}
		resultList.add(map);
	}
	return resultList;
}

	public Page<Map<String, Object>> getBatchOpt4Gua(HttpServletRequest request, HttpServletResponse response, Map<String, String> condition) {

		Long companyId = UserUtils.getUser().getCompany().getId();

		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select batch_no,userId,userName,createTime,travelerIds,travelerNames,countryIds,countryNames,collarZonings,guaranteeStatus ")
			.append(" from( ")
			.append("select vfbo.batch_no, create_user_id userId, create_user_name userName, vfbo.create_time createTime, ")
			.append("GROUP_CONCAT(t.id) travelerIds, GROUP_CONCAT(t.name) travelerNames, GROUP_CONCAT(sc.id) countryIds, ")
			.append("GROUP_CONCAT(sc.countryName_cn) countryNames, GROUP_CONCAT(vp.collarZoning) collarZonings, GROUP_CONCAT(v.guarantee_status) guaranteeStatus ")
			.append("from visa_flow_batch_opration vfbo ")
			.append("LEFT JOIN batch_traveler_relation btr on vfbo.batch_no = btr.batch_record_no ")
			.append("LEFT JOIN	traveler t on btr.traveler_id = t.id ")
			.append(" LEFT JOIN visa v on t.id = v.traveler_id ")
			.append(" LEFT JOIN visa_order vo on vo.id = t.orderId ")
			.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ")
			.append(" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id ")
			.append(" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId ")
			.append(" LEFT JOIN sys_user su on su.id = vo.create_by ")
			.append(" LEFT JOIN productorder po ")
			.append(" on po.id = t.main_order_id ")
			.append(" LEFT JOIN activitygroup ag ")
			.append(" on ag.id = po.productGroupId ");
		if(companyId == 68){
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}else if(companyId == 71) {
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
		}else{
			sqlBuffer.append(" LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}
		//查询新建面签通知时填写的说明会时间
		sqlBuffer.append("LEFT JOIN visa_interview_notice_traveler vint on t.id = vint.travaler_id ")
			.append("LEFT JOIN visa_interview_notice vin on vint.interview_id = vin.id and vin.del_flag='0' ")
			.append(" where vfbo.busyness_type = 6 and ")
			.append(" t.order_type = 6 and t.companyId = " + companyId + " and t.delFlag=0 and vo.visa_order_status != 100 and vo.del_flag = 0 and vo.create_by IS NOT NULL ")
			.append(" and a.status = 1 ")
			//只能看本供应商下的签证订单
			.append(" and su.companyId = " + companyId + " ");
//		sqlBuffer.append(" and t.orderId = "+orderId);



		sqlBuffer.append(" GROUP BY batch_no) t")	.append(" where 1=1 ");
		//查询条件
		String batchNo = condition.get("batchNo");
		if (StringUtil.isNotBlank(batchNo)) {
			sqlBuffer.append(" and batch_no like '%" + batchNo + "%' ");
		}
		String txnPerson = condition.get("txnPerson");
		if (StringUtil.isNotBlank(txnPerson)) {
			sqlBuffer.append(" and userName like '%" + txnPerson + "%' ");
		}
		String createTimeStart = condition.get("createTimeStart");
		if (StringUtil.isNotBlank(createTimeStart)) {
			sqlBuffer.append(" and createTime >= '" + createTimeStart + " 00:00:00' ");
		}
		String createTimeEnt = condition.get("createTimeEnt");
		if (StringUtil.isNotBlank(createTimeEnt)) {
			sqlBuffer.append(" and createTime <= '" + createTimeEnt + " 23:59:59' ");
		}
		String travellerName = condition.get("travellerName");
		if (StringUtil.isNotBlank(travellerName)) {
			sqlBuffer.append(" and travelerNames like '%" + travellerName + "%' ");
		}
		String country = condition.get("country");
		if (StringUtil.isNotBlank(country)) {
			sqlBuffer.append(" and FIND_IN_SET('"+country+"',countryIds) ");
		}
		String area = condition.get("area");
		if (StringUtil.isNotBlank(area)) {
			sqlBuffer.append(" and FIND_IN_SET('"+area+"',collarZonings) ");
		}
		String guaranteeType = condition.get("guaranteeType");
		if (StringUtil.isNotBlank(guaranteeType)) {
			sqlBuffer.append(" and FIND_IN_SET('"+guaranteeType+"',guaranteeStatus) ");
		}

		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(page.getOrderBy())) {
			page.setOrderBy("createTime DESC");
		}
		Page<Map<String, Object>> page4Gua = visaFlowBatchOprationDao.findBySql(page, sqlBuffer.toString(), Map.class);

		return page4Gua;

	}

	public void saveBatchInfo(int i, String travelerId, String batchNo) {

		String uuid = UUID.randomUUID().toString();
		if (i == 0) {
//			VisaFlowBatchOpration visaFlowBatchOpration = new VisaFlowBatchOpration();
//			visaFlowBatchOpration.setUuid(uuid);
//			visaFlowBatchOpration.setBatchNo(batchNo);
//			visaFlowBatchOpration.setBusynessType("6");
			User user = UserUtils.getUser();
//			visaFlowBatchOpration.setCreateUserId(user.getId());
//			visaFlowBatchOpration.setCreateUserName(user.getName());
//			visaFlowBatchOpration.setCreateTime(new Date());
//			visaFlowBatchOprationDao.updateObj(visaFlowBatchOpration);
			String sql = "INSERT INTO visa_flow_batch_opration (uuid, batch_no,busyness_type,create_user_id,create_user_name,create_time) " +
					"VALUES ('"+uuid+"','"+batchNo+"','6','"+user.getId()+"','"+user.getName()+"','"+new DateTime().toString("yyyy-MM-dd HH:mm:ss")+"')";
			visaFlowBatchOprationDao.updateBySql(sql);
		}

		BatchTravelerRelation batchTravelerRelation = new BatchTravelerRelation();
		batchTravelerRelation.setBatchUuid(uuid);
		batchTravelerRelation.setBatchRecordNo(batchNo);
		batchTravelerRelation.setTravelerId(Long.valueOf(travelerId));
		batchTravelerRelation.setBusinessType(6);

		batchTravelerRelationDao.saveObj(batchTravelerRelation);

	}
}
