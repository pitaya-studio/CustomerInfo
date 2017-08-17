package com.trekiz.admin.modules.visa.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.repository.OrderinvoiceDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;

import freemarker.template.TemplateException;

@Service
@Transactional(readOnly = true)
public class VisaOrderTravelerPayLogService extends BaseService {
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private OrderpayDao orderPayDao;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private MoneyAmountDao amountDao;
	@Autowired
	private VisaDao visaDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private ReviewDetailDao reviewDetailDao;
    @Autowired
    private VisaOrderService visaOrderService;
    @Autowired
    private VisaProductsService VisaProductsService;
    @Autowired
    private OrderpayDao orderpayDao;
    @Autowired
    private OrderinvoiceDao orderinvoiceDao;
    @Autowired
    private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;
    

	/**
	 * 签证产品签证订单支付记录查询
	 * 
	 * @param page
	 * @param map
	 * @param
	 * @return
	 * @throws Exception
	 */
	public Page<Map<Object, Object>> findVisaOrderList1(
			Page<Map<Object, Object>> page, Map<String, String> map) {
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT vp.id vpid,o.isAsAccount,oo.isAsAccount 'cancleFlag',o.id,o.payerName,o.toBankNname,")
		.append("o.accountDate,o.receiptConfirmationDate,o.orderNum,o.payTypeName,o.payType,o.orderPaySerialNum,")
		.append("su1.name jd_Name,o.createDate odate,o.printFlag,o.printTime,sys.label,sys.vv,sc.countryName_cn,")
		.append("vp.productName,vo.group_code groupCode,vp.groupCode vpGroupCode,su.name saleName,suu.name jdName,")
		.append("a.agentName,a.id agentId,vo.total_money,vo.create_date vodate,vo.salerId,vo.salerName sName,")
		.append("vo.create_by voCreate_by,suu.name suuName, vo.group_code,vo.id vorderid,vo.accounted_money,'order_money',")
		.append("'account_money','payed_money' FROM orderpay o ")
		.append(" LEFT JOIN  (SELECT  o1.orderNum,o1.isAsAccount FROM  orderpay o1  WHERE  o1.isAsAccount =1 ")
		.append(" GROUP  BY o1.orderNum)oo  ON o.orderNum=oo.orderNum")
		.append(" LEFT JOIN visa v ON v.traveler_id=o.traveler_id")
		.append(" LEFT JOIN  visa_order vo ON o.orderNum= vo.order_no")
		.append(" LEFT JOIN visa_products vp ON vo.visa_product_id=vp.id")
		.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ")
		.append(" LEFT JOIN sys_user su ON o.createBy = su.id ")
		.append(" LEFT JOIN sys_user suu ON vo.create_by = suu.id ")
		.append(" LEFT JOIN sys_user su1 ON vp.createBy = su1.id ")
		.append(" LEFT JOIN  (SELECT value vv, label FROM sys_dict WHERE delFlag = 0 AND type = 'new_visa_type') AS sys ")
		.append(" ON concat(vp.visaType) = sys.vv ")
		.append(" LEFT JOIN sys_country sc ON vp.sysCountryId = sc.id ");
		if(StringUtils.isNotBlank(map.get("payAmountStrat")) ||StringUtils.isNotBlank(map.get("payAmountEnd")))
		{ 
			sqlBuffer.append(" inner JOIN ");
			sqlBuffer.append(getTravellerMoneySql(map));
			sqlBuffer.append(" ON o.orderPaySerialNum =tmp.orderPaySerialNum");
		}
		sqlBuffer.append(" WHERE o.orderType = 6 AND o.payPriceType != 16 ");
		/*add by ang.gao */
		sqlBuffer.append(" AND vo.del_flag = '0' ");
		sqlBuffer.append(" AND o.delFlag = 0 AND su.companyId =" + userCompanyId);
		sqlBuffer.append(" AND suu.companyId = " + userCompanyId + " AND su1.companyId =" + userCompanyId);
		sqlBuffer.append(" AND vo.total_money IS NOT null");
		// 拼接游客姓名 查询条件
		if(StringUtils.isNotBlank(map.get("travellerName")))
		{ 
			sqlBuffer.append(" AND o.traveler_id IN (SELECT t.id  FROM traveler t WHERE t.name LIKE '%");
			sqlBuffer.append(map.get("travellerName").trim());
			sqlBuffer.append("%') ");
		}
		serarchCondtion(map, sqlBuffer);
		sqlBuffer.append(" GROUP BY o.orderPaySerialNum ,o.isAsAccount,o.payType");
		if(StringUtils.isBlank(page.getOrderBy())) {
			page.setOrderBy("o.updateDate desc");
		}
		Page<Map<Object, Object>> pageMap = visaOrderDao.findBySql(page,sqlBuffer.toString(), Map.class);
		// 对订单金额，达账 已付金额做单独处理通过减少关关联表的方式来增加查询效率
		dealWithResult(pageMap);
		return pageMap;
	}
	/**
	 * 对订单金额，达账 已付金额做单独处理通过减少关关联表的方式来增加查询效率
	 * @param pageMap
	 */
	private void dealWithResult(Page<Map<Object, Object>> pageMap) {
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		List<Map<Object, Object>> list= new ArrayList<Map<Object,Object>>();
		list =pageMap.getList();
		if(list.size() >0)
		{
			StringBuffer sqlString = getMoneybySerialNum(userCompanyId, list);
			//获取一页批次的游客已付金额总计
			StringBuffer buffer = getAccountMoneyBySeriNum(list);
			
			List<Map<Object, Object>> lists =  new ArrayList<Map<Object,Object>>();
			lists= visaOrderDao.findBySql(sqlString.toString(),Map.class);
			List<Map<Object, Object>> lists2 =  new ArrayList<Map<Object,Object>>();
			lists2 = visaOrderDao.findBySql(buffer.toString(),Map.class);
			StringBuffer noPayeMoney = getNoPayedMoney(list);
			//获取
			List<Map<Object, Object>> lists3 =  visaOrderDao.findBySql(noPayeMoney.toString(),Map.class);
			
			for(int i=0;i<list.size();i++)
			{
					//处理订单金额
				       for( int j=0;j<lists.size();j++)
				        {
				        	if(list.get(i).get("total_money").equals(lists.get(j).get("serialNum")))
				        	{
				        		list.get(i).put("total_money", lists.get(j).get("total_money"));
				        	}
				        }
	       //处理已付 达账金额			       
				       for( int j=0;j<lists2.size();j++)
				        {
				        	if(list.get(i).get("orderPaySerialNum").equals(lists2.get(j).get("orderPaySerialNum")))
				        	{
				        		list.get(i).put("payed_money", lists2.get(j).get("payed_money"));
				        	}
				        }
//				       //处理已付 达账金额			       
			       for( int j=0;j<lists3.size();j++)
			        {
			        	if(list.get(i).get("orderNum").equals(lists3.get(j).get("order_no")))
			        	{
			        		list.get(i).put("no_pay_money", lists3.get(j).get("no_pay_money"));
			        	}
			        }
//			       
				       
				       
			//处理订单累计达账金额
				 list.get(i).put("accounted_money", list.get(i).get("accounted_money") == null? "":getTotalAccountMoney(list.get(i).get("accounted_money").toString()));      
				       
			}
			
			
			lists =null;
			lists2 = null;
		}
		
		//订单
		
		pageMap.setList(list);
		list = null;
	}
	
	
	
	
	
	
	
	
	/**
	 * 对订单金额，达账 已付金额做单独处理通过减少关关联表的方式来增加查询效率
	 * @param list
	 */
	private void dealWithResult(List<Map<Object, Object>> list) {
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		if(list.size() >0)
		{
			StringBuffer sqlString = getMoneybySerialNum(userCompanyId, list);
			//获取一页批次的游客已付金额总计
			StringBuffer buffer = getAccountMoneyBySeriNum(list);
			
			List<Map<Object, Object>> lists =  new ArrayList<Map<Object,Object>>();
			lists= visaOrderDao.findBySql(sqlString.toString(),Map.class);
			List<Map<Object, Object>> lists2 =  new ArrayList<Map<Object,Object>>();
			lists2 = visaOrderDao.findBySql(buffer.toString(),Map.class);
			StringBuffer noPayeMoney = getNoPayedMoney(list);
			//获取
			List<Map<Object, Object>> lists3 =  visaOrderDao.findBySql(noPayeMoney.toString(),Map.class);
			
			for(int i=0;i<list.size();i++)
			{
					//处理订单金额
				       for( int j=0;j<lists.size();j++)
				        {
				        	if(list.get(i).get("total_money").equals(lists.get(j).get("serialNum")))
				        	{
				        		list.get(i).put("total_money", lists.get(j).get("total_money"));
				        	}
				        }
	       //处理已付 达账金额			       
				       for( int j=0;j<lists2.size();j++)
				        {
				        	if(list.get(i).get("orderPaySerialNum").equals(lists2.get(j).get("orderPaySerialNum")))
				        	{
				        		list.get(i).put("payed_money", lists2.get(j).get("payed_money"));
				        	}
				        }
//				       //处理已付 达账金额			       
			       for( int j=0;j<lists3.size();j++)
			        {
			        	if(list.get(i).get("orderNum").equals(lists3.get(j).get("order_no")))
			        	{
			        		list.get(i).put("no_pay_money", lists3.get(j).get("no_pay_money"));
			        	}
			        }
//			       
				       
				       
			//处理订单累计达账金额
				 list.get(i).put("accounted_money", list.get(i).get("accounted_money") == null? "":getTotalAccountMoney(list.get(i).get("accounted_money").toString()));      
				       
			}
			
			
			lists =null;
			lists2 = null;
		}
		
		//订单
		
	

	}
	
	
	
	
	
	
	
	
	/**
	 * 获取一页结果集中的未付款记录
	 * @param list
	 * @return
	 * @author hxd
	 */
	private StringBuffer getNoPayedMoney(List<Map<Object, Object>> list)
	{
		StringBuffer buffList  = new StringBuffer();
		for(int i=0;i<list.size();i++)
		{
			if(list.size() >i+1)
			{
				buffList.append("'");
				buffList.append(list.get(i).get("orderNum"));
				buffList.append("',");
			}
			else
			{
				buffList.append("'");
				buffList.append(list.get(i).get("orderNum"));
				buffList.append("'");
			}
		}
		StringBuffer buffer  = new StringBuffer();
		buffer.append("SELECT tmp3.nopaymoney,tmp3.currency_mark,tmp3.order_no,GROUP_CONCAT(tmp3.currency_mark,tmp3.nopaymoney) no_pay_money FROM  ( ");
		buffer.append("SELECT SUM(amount) nopaymoney,tmp2.currency_mark,tmp2.order_no FROM ( ");
		buffer.append("SELECT ma.currencyId, 	ma.amount amount, c.currency_mark, tmp.tm, tmp.order_no FROM money_amount ma ");
		buffer.append("RIGHT JOIN (SELECT	vo.total_money UUID,'total' tm,vo.order_no FROM visa_order vo WHERE vo.order_no IN ( ");
		buffer.append(buffList);
		buffer.append(")) tmp ON tmp.uuid = ma.serialNum ");
		buffer.append("LEFT JOIN currency c ON ma.currencyId = c.currency_id ");
		buffer.append("UNION SELECT ma.currencyId ,- ma.amount amount, c.currency_mark, tmp.tm, tmp.order_no 	FROM money_amount ma ");
		buffer.append("RIGHT JOIN ( SELECT vo.payed_money UUID, 'payed' tm, vo.order_no FROM visa_order vo WHERE 	vo.order_no IN ( ");
		buffer.append(buffList);
		buffer.append(")) tmp ON tmp.uuid = ma.serialNum ");
		buffer.append("LEFT JOIN currency c ON ma.currencyId = c.currency_id) tmp2 GROUP BY tmp2.currency_mark,tmp2.order_no) tmp3  GROUP BY tmp3.order_no ");
		
		return buffer;
	}
	
	/**
	 * 获取订单的达账金额汇总值
	 */
	public String getTotalAccountMoney(String uuid)
	{
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT  IFNULL(GROUP_CONCAT( tf.tm SEPARATOR '+') ,'') money FROM ( ");
		buffer.append(" SELECT  CONCAT(c.currency_mark, FORMAT(SUM(mma.amount), 2))   tm");
		buffer.append(" FROM currency c RIGHT JOIN (SELECT ma.currencyId, ma.amount FROM money_amount ma ");
		buffer.append(" WHERE ma.serialNum = '");
		buffer.append(uuid);
		buffer.append("') mma ON c.currency_id = mma.currencyId GROUP BY c.currency_mark) tf");
		List<Map<Object, Object>> ls =  visaOrderDao.findBySql(buffer.toString(),Map.class);
		if (ls.size()==0)
			return null;
		else
			 
			return ls.get(0).get("money").toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 获取一页批次的游客达账金额总计
	 * @param list
	 * @return
	 */
	private StringBuffer getAccountMoneyBySeriNum(List<Map<Object, Object>> list) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT GROUP_CONCAT(tm.tmoney SEPARATOR '+') payed_money,tm.orderPaySerialNum FROM (");
		buffer.append(" SELECT c.currency_mark,am.amount,am.orderPaySerialNum ,am.id,SUM(am.amount) money,CONCAT(c.currency_mark,FORMAT(SUM(am.amount),2)) tmoney  FROM currency c RIGHT  JOIN (");
		buffer.append(" SELECT ma.id, ma.serialNum,ma.currencyId,ma.amount,ma.orderPaySerialNum FROM money_amount ma WHERE ma.orderPaySerialNum IN (");
		String totalOrderString="";
		for(int i=0;i<list.size();i++)
		{
				if(i+1 == list.size())
				  totalOrderString= totalOrderString+"'"+list.get(i).get("orderPaySerialNum")+"'";
				else
				  totalOrderString= totalOrderString+"'"+list.get(i).get("orderPaySerialNum")+"',";	
		}
		totalOrderString= totalOrderString +" )) am ON c.currency_id = am.currencyId";
		buffer.append(totalOrderString);
		buffer.append(" GROUP BY am.orderPaySerialNum,c.currency_mark) tm  GROUP  BY tm.orderPaySerialNum");
		return buffer;
	}
	/**
	 * 计算每一页对应的订单金额金额
	 * @param userCompanyId
	 * @param list
	 * @return
	 */
	private StringBuffer getMoneybySerialNum(Long userCompanyId,
			List<Map<Object, Object>> list) {
		StringBuffer sqlString = new StringBuffer();
		sqlString.append("SELECT c.currency_mark,FORMAT(am.amount,2) orderMoney,am.serialNum,GROUP_CONCAT(c.currency_mark,FORMAT(am.amount, 2) SEPARATOR '+') total_money  FROM currency c RIGHT JOIN (");
		sqlString.append(" SELECT ma.amount,ma.currencyId, ma.serialNum FROM money_amount ma WHERE ma.serialNum IN");
		String totalOrderString="(";
		for(int i=0;i<list.size();i++)
		{
			if(i+1 == list.size())
			  totalOrderString= totalOrderString+"'"+list.get(i).get("total_money")+"'";
			else
			  totalOrderString= totalOrderString+"'"+list.get(i).get("total_money")+"',";	
		}
		totalOrderString = totalOrderString+")) am";
		sqlString.append(totalOrderString);
		sqlString.append(" ON c.currency_id=am.currencyId");
		sqlString.append(" WHERE c.create_company_id=");
		sqlString.append(userCompanyId);
		sqlString.append(" GROUP BY  am.serialNum");
		return sqlString;
	}
	/**
	 * 拼接查询条件
	 * @param map
	 * @param sqlBuffer
	 */
	private void serarchCondtion(Map<String, String> map, StringBuffer sqlBuffer) {
		if(StringUtils.isNotBlank(map.get("orderNum")))
		{
			sqlBuffer.append(" and  o.orderNum='");
			sqlBuffer.append(map.get("orderNum").trim());
			sqlBuffer.append( "' ");
		}
		if(StringUtils.isNotBlank(map.get("visaType")))
		{
			sqlBuffer.append(" and  vv = '");
			sqlBuffer.append(map.get("visaType").trim());
			sqlBuffer.append( "' ");
		}
		
		if(StringUtils.isNotBlank(map.get("jd")))
		{
			sqlBuffer.append(" and  vp.createBy = '");
			sqlBuffer.append(map.get("jd").trim());
			sqlBuffer.append( "' ");
		}
		
		if(StringUtils.isNotBlank(map.get("createByName")))  //下单人
		{
			sqlBuffer.append(" and  vo.create_by = '");
			sqlBuffer.append(map.get("createByName").trim());
			sqlBuffer.append( "' ");
		}
		if(StringUtils.isNotBlank(map.get("saler")))  //销售
		{
			sqlBuffer.append(" and  vo.salerId = '");
			sqlBuffer.append(map.get("saler").trim());
			sqlBuffer.append( "' ");
		}
		if(StringUtils.isNotBlank(map.get("visaCountry")))
		{
			sqlBuffer.append(" and  countryName_cn like '");
			sqlBuffer.append(map.get("visaCountry").trim());
			sqlBuffer.append( "' ");
		}
		if(StringUtils.isNotBlank(map.get("agentId")))
		{
			sqlBuffer.append(" and  a.id like '");
			sqlBuffer.append(map.get("agentId").trim());
			sqlBuffer.append( "' ");
		}
		
		if(StringUtils.isNotBlank(map.get("bankTimeBegin")))
		{
			sqlBuffer.append(" and o.accountDate" +">='");
			sqlBuffer.append(map.get("bankTimeBegin").trim()+" 00:00:00");
			sqlBuffer.append("' ");
		}
		if(StringUtils.isNotBlank(map.get("bankTimeEnd")))
		{
			sqlBuffer.append(" and o.accountDate" +"<='");
			sqlBuffer.append(map.get("bankTimeEnd").trim()+" 23:59:59");
			sqlBuffer.append("' ");
		}
		//0405 收款确认日期
		if(StringUtils.isNotBlank(map.get("confirmationDateStar")))
		{
			sqlBuffer.append(" and o.receiptConfirmationDate" +">='");
			sqlBuffer.append(map.get("confirmationDateStar").trim()+" 00:00:00");
			sqlBuffer.append("' ");
		}
		if(StringUtils.isNotBlank(map.get("confirmationDateEnd")))
		{
			sqlBuffer.append(" and o.receiptConfirmationDate" +"<='");
			sqlBuffer.append(map.get("confirmationDateEnd").trim()+" 23:59:59");
			sqlBuffer.append("' ");
		}
		
		//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
		if(StringUtils.isNotBlank(map.get("startOutBegin")))
		{
			sqlBuffer.append(" and v.start_out" +">='");
			sqlBuffer.append(map.get("startOutBegin").trim()+" 00:00:00");
			sqlBuffer.append("' ");
		}
		if(StringUtils.isNotBlank(map.get("startOutEnd")))
		{
			sqlBuffer.append(" and v.start_out" +"<='");
			sqlBuffer.append(map.get("startOutEnd").trim()+" 23:59:59");
			sqlBuffer.append("' ");
		}
		//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
		if(StringUtils.isNotBlank(map.get("payTimeBegin")))//付款时间
		{
			sqlBuffer.append(" and o.createDate" +">='");
			sqlBuffer.append(map.get("payTimeBegin").trim()+" 00:00:00");
			sqlBuffer.append("' ");
		}
		if(StringUtils.isNotBlank(map.get("payTimeEnd")))
		{
			sqlBuffer.append(" and o.createDate" +"<='");
			sqlBuffer.append(map.get("payTimeEnd").trim()+" 23:59:59");
			sqlBuffer.append("' ");
		}
		if(StringUtils.isNotBlank(map.get("payerName")))
		{
			sqlBuffer.append(" and  payerName like '");
			sqlBuffer.append(map.get("payerName").trim());
			sqlBuffer.append( "' ");
		}

		if(StringUtils.isNotBlank(map.get("payType")))
		{
			sqlBuffer.append(" and  o.payType =");
			sqlBuffer.append(map.get("payType").trim());
			sqlBuffer.append( " ");
		}

		//
		if(StringUtils.isNotBlank(map.get("toBankNname")))
		{
			sqlBuffer.append(" and  toBankNname like '");
			sqlBuffer.append(map.get("toBankNname").trim());
			sqlBuffer.append( "' ");
		}

		if(StringUtils.isNotBlank(map.get("isAccounted")))
		{
			if("Y".equals(map.get("isAccounted")))
				sqlBuffer.append(" and  o.isAsAccount =1");
			else if("D".equals(map.get("isAccounted")))
				sqlBuffer.append(" and  o.isAsAccount =2");
			else
				sqlBuffer.append(" and  o.isAsAccount !=1 and  o.isAsAccount !=2");
		}
		if(StringUtils.isNotBlank(map.get("printFlag")))
		{
			if("1".equals(map.get("printFlag")))
			sqlBuffer.append(" and  o.printFlag =1");
			else
				sqlBuffer.append(" and ( o.printFlag is null or o.printFlag=0) ");
		}
		if(StringUtils.isNotBlank(map.get("paymentType"))){
			sqlBuffer.append(" and a.id in (select id from agentinfo where paymentType =  " + map.get("paymentType") + " )");
		}

	}
/**
 * 拼接付款金额查询条件的sql
 * @param map
 * @return
 */
	private String  getTravellerMoneySql(Map<String, String> map) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(SELECT ma.orderPaySerialNum,ma.amount FROM money_amount ma WHERE ma.orderPaySerialNum <>''");
		if(StringUtils.isNotBlank(map.get("payAmountStrat")))
		{
			buffer.append(" AND ma.amount >=");
			buffer.append(map.get("payAmountStrat"));
		}
		if(StringUtils.isNotBlank(map.get("payAmountEnd")))
		{
			buffer.append(" AND  ma.amount <=");
			buffer.append(map.get("payAmountEnd"));
		}
		buffer.append(") tmp ");
		return buffer.toString();
	}

	/**
	 * 参数处理
	 *
	 * @param paraList
	 * @param mapRequest
	 * @param model
	 * @param request
	 */
	public void handlePara(List<String> paraList,
			Map<String, String> mapRequest, Model model,
			HttpServletRequest request) {
		String common = "";
		for (String para : paraList) {
			common = request.getParameter(para);
			if (common != null) {
				common = common.trim();
			}
			mapRequest.put(para, common);
			model.addAttribute(para, common);
		}
	}

	/**
	 * 获取签证类型
	 *
	 * @param
	 * @return
	 */
	public List<Dict> getVisaType() {
		return visaOrderDao.queryVisaType();
	}

	/**
	 * 根据Id 查询Orderpay
	 *
	 * @param orderpayId
	 * @return
	 */
	public Orderpay getOrderPay(Long orderpayId) {
		// TODO Auto-generated method stub
		return orderPayDao.getOrderPayById(orderpayId);
	}

	/**
	 * 更行Orderpay 打印时间以及打印状态
	 *
	 * @param time
	 * @param id
	 */
	public void updateOrderPay(Date time, Long id) {
		// TODO Auto-generated method stub
		orderPayDao.updateOrderPayById(time, id);
	}

	/**
	 * 校验游客是否有借款
	 *
	 * @param travleId
	 */
	public List<Review> validateBorrowMoney(String travleId) {
		// TODO Auto-generated method stub
		return reviewDao.validateBorrowMoney(Long.parseLong(travleId));
	}

	/**
	 * 游客收款单
	 *
	 */
	public List<?> inMoneyList(String id) // payeeAmount
	{
		// TODO Auto-generated method stub
		StringBuffer sqlBuffer = new StringBuffer();
		// sqlBuffer.append("SELECT t1.createDate startDate ,t1.remarks,su.name operator,vp.productName product ,t1.payerName fromPayerName ");
		// sqlBuffer.append(",t1.bankAccount fromBankAccount,t1.bankName fromBankName,t.name travllerName,FORMAT(ma.amount*c.currency_exchangerate,2) payeeAmount,ma.amount*c.currency_exchangerate payeeAmount3 ");
		// sqlBuffer.append(",t1.toBankAccount toBankAccount,t1.toBankNname toBankNname,vo.create_by paymentPerson ");
		// sqlBuffer.append(",t1.accountDate accountDate,su1.name payee,t1.updateDate confTime,t1.updateDate,t.accounted_money accountedMoney,a.groupCode groupCode,a.groupOpenDate groupOpenDate,t.accounted_money,ma1.amount payeeAmount2,FORMAT(ma1.amount*c.currency_exchangerate, 2) maCurrency    FROM traveler t ");
		// //sqlBuffer.append("LEFT JOIN (SELECT * FROM orderpay o  WHERE o.payTypeName LIKE '%汇款%') t1 ON t.id = t1.traveler_id ");
		// sqlBuffer.append("LEFT JOIN  orderpay  t1 ON t.id = t1.traveler_id ");
		// sqlBuffer.append("LEFT JOIN visa_order vo ON t1.orderId = vo.id ");
		// sqlBuffer.append("LEFT JOIN visa_products vp ON vo.visa_product_id = vp.id ");
		// sqlBuffer.append("LEFT JOIN money_amount ma ON t1.moneySerialNum = ma.serialNum ");
		// sqlBuffer.append("LEFT JOIN sys_user su1 ON t1.updateBy = su1.id ");
		// sqlBuffer.append("LEFT JOIN sys_user su  ON vo.create_by = su.id ");
		// sqlBuffer.append("LEFT JOIN productorder p  ON t.main_order_id = p.id ");
		// sqlBuffer.append("LEFT JOIN activitygroup a ON p.productGroupId = a.id ");
		// sqlBuffer.append("LEFT JOIN currency c ON ma.currencyId = c.currency_id ");
		// sqlBuffer.append(" LEFT JOIN money_amount ma1 ");
		// sqlBuffer.append(" ON t.accounted_money = ma1.serialNum ");
		// sqlBuffer.append("WHERE t.id="+id);
		// sqlBuffer.append(" order by t1.updateDate desc");
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		sqlBuffer
				.append("SELECT  a1.groupCode groupCode,vo.create_date startDate1,su.name  operator,vp.productName product,vo.create_date startDate,vo.group_code");
		sqlBuffer
				.append(" ,o.payerName,o.remarks remarks,ma.amount,o.toBankAccount,o.toBankNname,o.bankAccount fromBankAccount,o.bankName fromPayerName,o.isAsAccount");
		sqlBuffer
				.append(" ,o.updateDate,o.updateBy,o.accountDate accountDate ,o.createDate confTime,su1.name  payeee ,FORMAT(ma.amount * c.convert_lowest, 2) payeeAmount2  , ma.amount * c.convert_lowest  payeeAmount3,t.name travllerName,c.currency_name  currencyName, sc.countryName_cn,c.currency_mark mark,c.convert_lowest,a.agentName");
		sqlBuffer.append(" FROM orderpay o");
		sqlBuffer.append(" INNER JOIN visa_order vo ON o.orderId =vo.id");
		sqlBuffer
				.append(" INNER JOIN visa_products vp ON vo.visa_product_id = vp.id");
		sqlBuffer.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id");
		sqlBuffer.append(" LEFT JOIN traveler t ON o.traveler_id = t.id");
		sqlBuffer.append(" LEFT JOIN productorder p ON p.id =t.main_order_id");
		sqlBuffer.append(" LEFT JOIN sys_user su ON o.createBy = su.id");
		sqlBuffer.append(" LEFT JOIN sys_user su1 ON o.updateBy = su1.id");
		sqlBuffer
				.append(" LEFT JOIN money_amount ma ON o.moneySerialNum = ma.serialNum");
		sqlBuffer
				.append(" LEFT JOIN currency c ON ma.currencyId = c.currency_id");
		sqlBuffer
				.append(" LEFT JOIN productorder p1 ON t.main_order_id =p1.id");
		sqlBuffer
				.append(" LEFT JOIN activitygroup a1 ON p1.productGroupId = a1.id");
		sqlBuffer
				.append(" LEFT JOIN sys_country sc ON vp.sysCountryId = sc.id ");
		sqlBuffer.append(" WHERE  c.create_company_id=");
		sqlBuffer.append(userCompanyId);
		sqlBuffer.append(" and  o.id=");
		sqlBuffer.append(id);
		return visaOrderDao.findBySql(sqlBuffer.toString(), Map.class);
	}

	/**
	 * list<Object> 转 map
	 *
	 * @param ls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getJson(List<?> ls) {
		Map<String, Object> map1 = (Map<String, Object>) ls.get(0);
		return map1;
	}

	/**
	 * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零 要用到正则表达式
	 */
	public static String digitUppercase(double n) {
		String fraction[] = { "角", "分" };
		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };
		String head = n < 0 ? "负" : "";
		n = Math.abs(n);
		String s = "";
		for (int i = 0; i < fraction.length; i++) {
			s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i])
					.replaceAll("(零.)+", "");
		}
		if (s.length() < 1) {
			s = "整";
		}
		int integerPart = (int) Math.floor(n);

		for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
			String p = "";
			for (int j = 0; j < unit[1].length && n > 0; j++) {
				p = digit[integerPart % 10] + unit[1][j] + p;
				integerPart = integerPart / 10;
			}
			s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i]
					+ s;
		}
		return head
				+ s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "")
						.replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
	}
	
	
	/**
	 * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零 要用到正则表达式
	 */
	public static String digitUppercaseRedWord(double n) {
		String fraction[] = { "角", "分" };
		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };
		String head = n < 0 ? "红字" : "";
		n = Math.abs(n);
		String s = "";
		for (int i = 0; i < fraction.length; i++) {
			s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i])
					.replaceAll("(零.)+", "");
		}
		if (s.length() < 1) {
			s = "整";
		}
		int integerPart = (int) Math.floor(n);

		for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
			String p = "";
			for (int j = 0; j < unit[1].length && n > 0; j++) {
				p = digit[integerPart % 10] + unit[1][j] + p;
				integerPart = integerPart / 10;
			}
			s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i]
					+ s;
		}
		return head
				+ s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "")
						.replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
	}

	/**
	 * 创建word文档
	 * 
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public File createReceiptFile(String orderPaySerialNum,String vorderid,String vpid) throws IOException,
			TemplateException {
		
		List<Orderpay> orderpayList = orderpayDao.findOrderpaByOrderPaySerialNum(orderPaySerialNum);
		Orderpay orderpay = orderpayList.get(0);
		
		VisaOrder visaOrder= visaOrderService.findVisaOrder(Long.parseLong(vorderid));
		VisaProducts visaProducts = VisaProductsService.findByVisaProductsId(Long.parseLong(vpid));
		
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		
		Map<String, Object> root = new HashMap<String, Object>();
		
		//填写日期 
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = null;
		if (null!=visaOrder.getCreateDate()) {
			startDate = sFormat.format(visaOrder.getCreateDate());
			root.put("startDate", startDate);
		}else {
			root.put("startDate", "");
		}
		
		//团号
		//if (null!=visaOrder.getGroupCode()) {
		//对应需求号    C460V3  签证订单团号   统一取  签证订单所关联产品的   团号
		if (null!=visaProducts.getGroupCode()) {
			//root.put("groupCode", visaOrder.getGroupCode());
			String companyUUID = UserUtils.getUser().getCompany().getUuid();
			if ("7a816f5077a811e5bc1e000c29cf2586".equals(companyUUID)) {
				root.put("groupCode", visaOrder.getGroupCode());
			 }else{
				root.put("groupCode", visaProducts.getGroupCode());// 对应需求C460V3  签证订单团号  统一取签证订单所关联的产品团号
			 }
		}else {
			root.put("groupCode", "");
		}
		
		//出发/签证日期
		SimpleDateFormat sFormat4startDate1 = new SimpleDateFormat("MM 月 dd 日");
		String startDate1 = null;
		if (null!=visaOrder.getCreateDate()) {
			startDate1 = sFormat4startDate1.format(visaOrder.getCreateDate());
			root.put("startDate1", startDate1);
		}else {
			root.put("startDate1", "");
		}
		
		//经办人   交款人  operator
		if (null!=orderpay.getCreateBy().getName()) {
			root.put("operator", orderpay.getCreateBy().getName());
		}else {
			root.put("operator", "");
		}
		
		//线路/产品
		if (null!=visaProducts.getProductName()) {
			root.put("product", visaProducts.getProductName());
		}else {
			root.put("product", "");
		}
		
		//来款单位信息  payerName  fromPayerName  fromBankAccount
		root.put("payerName", StringUtils.blankReturnEmpty(orderpay.getPayerName()));
		root.put("fromPayerName", StringUtils.blankReturnEmpty(orderpay.getBankName()));
		root.put("fromBankAccount", StringUtils.blankReturnEmpty(orderpay.getBankAccount()));
				
		//款项
		String countryName_cn =  null;
		if (null!=visaProducts.getSysCountryId()) {
			countryName_cn =  CountryUtils.getCountryName(Long.parseLong(visaProducts.getSysCountryId()+""));
			root.put("countryName_cn", countryName_cn);
		}else {
			root.put("countryName_cn", "");
		}
		
		//客人名单  travllerName
		String travllerName =  getOrderTravellersPayedNames(orderPaySerialNum);
		if (null!=visaProducts.getSysCountryId()) {
			root.put("travllerName", travllerName);
		}else {
			root.put("travllerName", "");
		}
		
		 //----------备注-----------
	/*	 StringBuilder remarkstemp = new StringBuilder("");
		 for (Orderpay orderpaytemp : orderpayList) {
			if (null!=orderpaytemp.getRemarks()&&!"".equals(orderpaytemp.getRemarks())) {
				if (!remarkstemp.toString().contains(orderpaytemp.getRemarks())) {
					remarkstemp.append(orderpaytemp.getRemarks()).append(",");
				}
				
			}
		 }
		String remarks = remarkstemp.toString().replaceAll(",$", "");*/
		
		//处理付款备注无法显示的问题
		root.put("remarks", StringUtils.blankReturnEmpty(orderpay.getRemarks()));
		
		//
		String moneyStr = getOrderTravellersPayed(orderPaySerialNum);
		//收款金额
		root.put("money",fmtMicrometer(moneyStr));
		//大写
		root.put("SAYTOTAL",digitUppercaseRedWord(Double.parseDouble(moneyStr)));
		
		
		//收款账户  toBankNname  toBankAccount
		root.put("toBankNname", StringUtils.blankReturnEmpty(orderpay.getToBankNname()));
		root.put("toBankAccount", StringUtils.blankReturnEmpty(orderpay.getToBankAccount()));
		
		//收款人
		if (null!=orderpay.getUpdateBy().getName()) {
			if(null == orderpayList.get(0).getIsAsAccount()) 
			{
				root.put("payeee",""); 
				
			}else{
				root.put("payeee",orderpay.getUpdateBy().getName());
			}
		}else {
			root.put("payeee",orderpay.getUpdateBy().getName()); 
		}
		
		//确认收款日期  confTime （accountDate）
		if (null!=orderpay.getAccountDate()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
			if (userCompanyId==68) {
				
				if ("1".equals(orderpay.getIsAsAccount().toString())) {
					root.put("accountDate",simpleDateFormat.format(orderpay.getAccountDate()));
				}else{
					root.put("accountDate",""); 
				}
				
			}else{
				root.put("accountDate",simpleDateFormat.format(orderpay.getAccountDate()));
			}
			
			root.put("tmp", "");//用于调整word中的空格
		}else {
			root.put("accountDate",""); 
			root.put("tmp", "              ");//用于调整word中的空格
		}
		
		//确认收款日期
		root.put("confTime", "    年  月   日");
		
		/*
   	     * wangxinwei  added 20151012， 客人报名收款单  和打印  如如果 已经确认收款要显示收款日期
   	     * 需求C221
		 */
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年 MM月 dd日");
		
		root.put("isAsAccount", orderpay.getIsAsAccount());
		 if (userCompanyId==88||userCompanyId==68) {
			 //修改  银行确认收款  0041
			 if (userCompanyId==68) {
				 root.put("isAsAccount", orderpay.getIsAsAccount());
			 }else{
				 root.put("isAsAccount", 1000); 
			 }
			 
		 }else {
			 root.put("isAsAccount", orderpay.getIsAsAccount());
		 }
		
		 String compName = UserUtils.getUser().getCompany().getCompanyName();
		if (null!=orderpay.getUpdateDate()) {
			
				//"    年   月    日"
			if("拉美途".equals(compName)){
				root.put("payConfirmDate",  "    年   月    日");
			}else{
				root.put("payConfirmDate",  sdf.format(orderpay.getUpdateDate()));
			}
		}else {
			root.put("payConfirmDate",  "              ");
		}
		
		 //对应需求号   0002，增加  开票状态   和  开票金额
         List<Orderinvoice> orderinvoices  = orderinvoiceDao.findOrderinvoiceByOrderIdOrderTypeStatusINOneTwo(Integer.parseInt(vorderid),6);
		 if (null!=orderinvoices&&orderinvoices.size()>0) {
			 
			 String invoiceCount =  OrderCommonUtil.getOrderInvoiceMoney("6",vorderid);
			 if (!"0.00".equals(invoiceCount)) {
				 root.put("invoiceStatus", "已开票");
				 root.put("invoiceCount", Context.CURRENCY_MARK_RMB+invoiceCount);
			 }else{
				 root.put("invoiceStatus", "待开票");
				 root.put("invoiceCount", "¥0.00");
			 }
			 
		 }else{
			 root.put("invoiceStatus", "");
			 root.put("invoiceCount", "");
		 }
		 
		//7a816f5077a811e5bc1e000c29cf2586  环球行
		 String companyUUID = UserUtils.getUser().getCompany().getUuid();
		 root.put("companyUUID", companyUUID);
		return rest(root);

	}
	
	public String fmtMicrometer(String text){
		DecimalFormat df = null;
		df = new DecimalFormat("###,##0.00");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	public File rest(Map<String, Object> root)
			throws IOException, TemplateException {
		return FreeMarkerUtil.generateFile("inMoney.ftl", "inMoney.doc", root);
	}

	/**
	 * 创建word文档
	 * 
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public File createReceiptFile2(String travleId, String uid, int size)
			throws IOException, TemplateException {
		// word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// String travleId = request.getParameter("travelerid");
		// String uid ="";
		List<Review> reviews = validateBorrowMoney(travleId);
		if (reviews.size() > 0) {
			List<Review> review = getReview(Long.parseLong(travleId));
			List<ReviewDetail> details = getReviewDetail(review.get(0).getId());
			for (int i = 0; i < details.size(); i++) {
				if ("borrowAmount".equals(details.get(i).getMykey()))
					root.put("borrowAmount", details.get(i).getMyvalue());
				if ("borrowRemark".equals(details.get(i).getMykey()))
					root.put("borrowRemark", details.get(i).getMyvalue());
			}
			root.put("SAYTOTAL", StringNumFormat.changeAmount(root.get(
					"borrowAmount").toString()));
			root.put(
					"fillDate",
					DateUtils.formatCustomDate(
							DateUtils.dateFormat(review.get(0).getCreateDate()
									.toString()), "yyyy年MM月dd日"));// 填写日期

			/**
			 * List<?> lsts =logService.getAppPerson(travleId); if(lsts.size()
			 * ==0) mp.put("returnPerson", ""); else {
			 * 
			 * @SuppressWarnings("unchecked") List<Map<String, Object>> lstS
			 *                                =(List<Map<String, Object>>) lsts;
			 *                                Map<String, Object> map =
			 *                                lstS.get(0);
			 *                                mp.put("returnPerson",
			 *                                map.get("name"));//还款人 }
			 */
			List<?> lsts = getAppPerson(travleId);
			if (lsts.size() == 0)
				root.put("returnPerson", "");
			else {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> lstS = (List<Map<String, Object>>) lsts;
				Map<String, Object> map = lstS.get(0);
				root.put("returnPerson", map.get("name"));// 还款人
			}

			// root.put("returnPerson", review.get(0).getUpdateByName());//还款人
			List<?> list = getFinanceName(uid, travleId);
			if (list.size() == 0)
				root.put("chargeApproval", "");// 主管审批
			else {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> lst = (List<Map<String, Object>>) list;
				Map<String, Object> map = lst.get(0);
				root.put("chargeApproval", map.get("suname")); // 确认的财务名称
				root.put("createTime", map.get("createTime"));
				root.put("createTime1", DateUtils.formatCustomDate(
						DateUtils.dateFormat(map.get("createTime").toString()),
						"yyyy年M月d日"));
			}
		}
		return FreeMarkerUtil.generateFile("returnMoney.ftl",
				"returnMoney.doc", root);
	}

	public List<?> borrowMoneyList(String id) {
		// TODO Auto-generated method stub
		StringBuffer sqlBuffer = new StringBuffer();
		return visaOrderDao.findBySql(sqlBuffer.toString(), Map.class);
	}

	public List<Visa> getVisaByTid(String travelId) {
		// TODO Auto-generated method stub
		return visaDao.findByTravelerId1(Long.parseLong(travelId));
	}

	public void updateMoneyAmount(List<MoneyAmount> amounts, String uuid,
			Long orderId) {
		for (int i = 0; i < amounts.size(); i++) {
			amountDao.updateMoneyAmount(uuid, amounts.get(i).getAmount(),
					amounts.get(i).getCurrencyId());
		}
		orderPayDao.updateOrderPayByOrderId(orderId);
	}

	public VisaOrder getVisaOrderByOrderNo(String orderno) {
		// TODO Auto-generated method stub
		return visaOrderDao.getVisaInfoByOrderNo(orderno);
	}

//	/**
//	 * 根据serialNum 获取money_amount List
//	 * 
//	 * @param payedMoney
//	 * @return
//	 */
//	public List<MoneyAmount> getAmountByUid(String payedMoney) {
//		// TODO Auto-generated method stub
//		return amountDao.getAmountByUid(payedMoney);
//	}

	/**
	 * 签证游客支付记录驳回 订单已付金额总价 减去 驳回金额
	 * 
	 * @param amount
	 *            订单支付总记录总价
	 * @param amount2
	 *            驳回的金额
	 * @param amounts
	 *            游客支付金额总计
	 * 
	 */
	public void updateAmountByUid(List<Map<Object, Object>> amount,
			List<Map<Object, Object>> amount2, List<Map<Object, Object>> amounts,
			String travelerId) {
		// String payedMoneySerialNum= null;
		// travelerDao.updateUUid(payedMoneySerialNum,Long.parseLong(travelerId));
		// for(int j= 0;j<amount.size();j++)
		// {
		double big = sub(Double.parseDouble(amount.get(0).get("amount").toString()),Double.parseDouble(amount2.get(0).get("amount").toString()));
				//.);
		amountDao.updateBySql("UPDATE money_amount ma SET ma.amount="
				+ big
				+ " WHERE ma.id="
				+ amount.get(0).get("id")
				+ ""); //updateOrderForAmount(amount.get(0).getId(), big);
		double travelerBig = sub(Double.parseDouble(amounts.get(0).get("amount").toString()),Double.parseDouble(amount2.get(0).get("amount").toString()));
		//amounts.get(0).getAmount().subtract(amount2.get(0).getAmount());
		// amountDao.updateOrderForAmount(amounts.get(0).getId(), travelerBig);
		
		amountDao.updateBySql("UPDATE money_amount ma SET ma.amount="
				+ travelerBig
				+ " WHERE ma.id="
				+ amounts.get(0).get("id")+ "");
		// }
	}
	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	public Traveler getTravleById(String travelerId) {
		// TODO Auto-generated method stub
		return travelerDao.getTravleById(Long.parseLong(travelerId));
	}

	/**
	 * 签证押金驳回
	 * 
	 * @param travelId
	 *
	 * @param orderPayId
	 *            orderpay 表的主键
	 */
	public boolean depositReject(String travelId, String orderPayId, String reason) {
		// TODO Auto-generated method stub
		List<Visa> list = visaDao.findByTravelerId1(Long.parseLong(travelId));
		Orderpay orderPay = orderPayDao.findOne(Long.valueOf(orderPayId));
		if (list.size() == 0)
			return false;
		else {
			Visa visa = list.get(0);
			List<MoneyAmount> amounts = addOrDecreaseMoney(
					visa.getPayedDeposit(), orderPay.getMoneySerialNum(),
					"decrease");
			for (int i = 0; i < amounts.size(); i++) {
				amountDao.updateMoneyAmount(visa.getPayedDeposit(), amounts
						.get(i).getAmount(), amounts.get(i).getCurrencyId());
			}
//			orderPayDao.updateOrderPayByOrderId(Long.parseLong(orderPayId));
			orderPay.setIsAsAccount(2);
			orderPay.setRejectReason(reason);
			orderpayDao.updateObj(orderPay);
			return true;
		}

	}

	/**
	 * 由指定的flag对两个UUID对应的金额相加或相减
	 * 
	 * @param uuid1
	 * @param uuid2
	 * @param flag
	 * @return
	 */
	List<MoneyAmount> addOrDecreaseMoney(String uuid1, String uuid2, String flag) {

		List<MoneyAmount> result = new ArrayList<MoneyAmount>();

		List<MoneyAmount> list1 = amountDao.findAmountBySerialNum(uuid1);
		List<MoneyAmount> list2 = amountDao.findAmountBySerialNum(uuid2);

		Map<String, BigDecimal> map1 = new LinkedHashMap<>();
		if (list1 != null) {
			for (MoneyAmount ma1 : list1) {
				map1.put(ma1.getCurrencyId().toString(), ma1.getAmount());
			}
		}

		if (list2 != null) {
			for (MoneyAmount ma2 : list2) {
				String key = ma2.getCurrencyId().toString();
				if (map1.containsKey(key)) {
					if ("add".equals(flag)) {
						map1.put(key, map1.get(key).add(ma2.getAmount()));
					} else {
						map1.put(key, map1.get(key).subtract(ma2.getAmount()));
					}
				} else {
					if ("add".equals(flag)) {
						map1.put(key, ma2.getAmount());
					} else {
						map1.put(key,
								new BigDecimal(0).subtract(ma2.getAmount()));
					}
				}
			}
		}

		for (String key : map1.keySet()) {
			MoneyAmount ma = new MoneyAmount();
			ma.setCurrencyId(Integer.parseInt(key));
			ma.setAmount(map1.get(key));
			result.add(ma);
		}

		return result;
	}

	/**
	 * 获取流程信息
	 * 
	 * @param travleId
	 * @return
	 */
	public List<Review> getReview(Long travleId) {
		// TODO Auto-generated method stub
		return reviewDao.getReview(travleId);
	}

	public List<ReviewDetail> getReviewDetail(Long id) {
		// TODO Auto-generated method stub
		return reviewDetailDao.getReviewDetail(id);
	}

	public List<?> getFinanceName(String serialNum, String travleId) {
		// TODO Auto-generated method stub
		StringBuffer buString = new StringBuffer();
		buString.append("SELECT su.name suname,ma.createTime  createTime FROM money_amount ma LEFT JOIN sys_user su ON ma.createdBy = su.id WHERE  ma.uid='");
		buString.append(travleId + "' AND ma.serialNum='" + serialNum + "'");
		return visaOrderDao.findBySql(buString.toString(), Map.class);
	}

	public List<?> findAllAgentinfo() {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT DISTINCT a.agentName FROM visa_order vo LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ORDER BY agentFirstLetter ");
		return visaOrderDao.findBySql(buffer.toString(), Map.class);
	}

	/**
	 * 根据游客ID查询 发起借款的签务人员的信息
	 * 
	 * @param travleId
	 * @return
	 */
	public List<?> getAppPerson(String travleId) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT su.name,r.* FROM review r LEFT JOIN sys_user su on r.createBy= su.id WHERE r.flowType=5 AND r.productType =6 AND r.travelerId=1994 AND r.status!=0");
		return visaOrderDao.findBySql(buffer.toString(), Map.class);
	}

	public Map<String, Object> getTotalMoney(List<Map<String, Object>> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		float money = 0;
		for (int i = 0; i < list.size(); i++) {
			money = money
					+ Float.parseFloat(list.get(i).get("amount").toString())
					* Float.parseFloat(list.get(i).get("convert_lowest")
							.toString());
		}
		NumberFormat nf = new DecimalFormat(",##0.00");
		map.put("toatalMoney", nf.format(money));
		if (money < 0)
			map.put("toatalMoneyBig", "红字");
		else
			map.put("toatalMoneyBig",
					StringNumFormat.changeAmount(String.valueOf(money)));
		return map;
	}

	@SuppressWarnings("rawtypes")
	public List<?> findAmountMap(List ls) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		String sql = "";
		buffer.append("SELECT  ac.serialNum, ac.amount, ac.moneyType FROM amount_currency ac WHERE ac.moneyType <> 16 AND ac.serialNum IN(");
		for (int i = 0; i < ls.size(); i++) {
			if (i == ls.size() - 1)
				sql = sql + "'" + ls.get(i) + "'";
			else
				sql = sql + "'" + ls.get(i) + "',";
		}
		buffer.append(sql);
		buffer.append(")");
		return visaOrderDao.findBySql(buffer.toString(), Map.class);
	}

	public List<?> getOrderTotalMoney(String orderno) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT GROUP_CONCAT(CONCAT(c.currency_mark,ma.amount)SEPARATOR '+') amountt FROM money_amount ma  JOIN  visa_order vo ON ma.serialNum = vo.total_money ");
		buffer.append("JOIN  currency c ON ma.currencyId = c.currency_id WHERE vo.order_no='");
		buffer.append(orderno);
		buffer.append("' GROUP BY ma.serialNum");
		return visaOrderDao.findBySql(buffer.toString(), Map.class);

	}

	/**
	 * 修改订单、游客结算价格
	 * 
	 * @param data2
	 * @param payPriceSerialNum
	 */
	public void updateMoney(String data2, String payPriceSerialNum,
			String orderId) {
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		if (StringUtils.isNotBlank(data2)) {
			String[] str = data2.split("&");
			List<String> list1 = new ArrayList<>();
			List<String> list2 = new ArrayList<>();
			for (int i = 0; i < str.length; i++) {
				if (i % 2 == 0)
					list1.add(str[i].split("=")[1]);// 币种ID
				else
					list2.add(str[i].split("=")[1]); // 钱数 amount
			}
			// 游客已经支付的币种记录
			String sql = "SELECT ma.id,ma.amount,c.currency_mark,c.currency_id FROM money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id   WHERE ma.serialNum='"
					+ payPriceSerialNum + "'";
			List<Map<String, Object>> list = orderPayDao.findBySql(sql,
					Map.class);
			// 修改订单 结算价
			for (int i = 0; i < list1.size(); i++) {
				for (int j = 0; j < list.size(); j++) {
					if (list1.get(i).toString()
							.equals(list.get(j).get("currency_id").toString())) {
						// String
						// qsql="select  ma.amount from money_amount ma  WHERE ma.serialNum='"
						// + payPriceSerialNum+ "' AND ma.currencyId=" +
						// list1.get(i);
						String qsql = "SELECT ma.amount FROM money_amount ma LEFT JOIN visa_order vo  ON ma.serialNum=vo.total_money  WHERE vo.id="
								+ orderId
								+ " AND ma.currencyId="
								+ list1.get(i);

						String amount = orderPayDao.findBySql(qsql).get(0)
								.toString();
						String upsql = "UPDATE  money_amount ma LEFT JOIN  visa_order vo  ON ma.serialNum = vo.total_money SET ma.amount="
								+ add(amount,
										String.valueOf(sub1(list2.get(i), list
												.get(j).get("amount")
												.toString())))
								+ " WHERE vo.id="
								+ orderId
								+ " AND ma.currencyId=" + list1.get(i);
						orderPayDao.updateBySql(upsql);
						
						//-------by------junhao.zhao-----2017-01-18------------visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改---开始---
						if(StringUtils.isNotBlank(orderId)){
							orderDateSaveOrUpdateDao.updatePeopleAndMoney(Long.parseLong(orderId), Context.ORDER_TYPE_QZ);
						}
						//-------by------junhao.zhao-----2017-01-18------------visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改---结束---
					}
				}
				// 修改 游客 结算价
				orderPayDao
						.updateBySql("UPDATE money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id SET ma.amount = "
								+ list2.get(i)
								+ " WHERE ma.serialNum ='"
								+ payPriceSerialNum
								+ "' AND c.currency_id = "
								+ list1.get(i)
								+ " AND c.create_company_id = "
								+ userCompanyId + "");
			}
		}
	}

	/**
	 * 提供精确减法运算的sub方法
	 * 
	 * @param value1
	 *            被减数
	 * @param value2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub1(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确加法计算的add方法
	 * 
	 * @param value1
	 *            被加数
	 * @param value2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 查询批次游客信息
	 * 
	 * @param btno
	 * @return
	 */
	public String getTravellerInfo(String btno,HttpServletRequest request) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT t.name,o.moneySerialNum, t.payPriceSerialNum, v.start_out startOut");
		buffer.append(" FROM orderpay o");
		buffer.append(" LEFT JOIN traveler t ON o.traveler_id = t.id");
		buffer.append(" LEFT JOIN visa v ON t.id=v.traveler_id");
		buffer.append(" WHERE  o.orderPaySerialNum='");
		buffer.append(btno);
		buffer.append("'");
		//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
		String companyUuid=UserUtils.getUser().getCompany().getUuid();
		if("58a27feeab3944378b266aff05b627d2".equals(companyUuid)){
			if(StringUtils.isNotBlank(request.getParameter("startOutBegin")))
			{
				buffer.append(" AND v.start_out" +">='");
				buffer.append(request.getParameter("startOutBegin").trim()+" 00:00:00");
				buffer.append("' ");
			}
			if(StringUtils.isNotBlank(request.getParameter("startOutEnd")))
			{
				buffer.append(" AND v.start_out" +"<='");
				buffer.append(request.getParameter("startOutEnd").trim()+" 23:59:59");
				buffer.append("' ");
			}
		}
		//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv

		// 查询出素有的已付 应付 UUID
		List<Map<String, Object>> list = orderPayDao.findBySql(
				buffer.toString(), Map.class);
		// 汇总值 保存游客的支付信息
		List<Map<String, Object>> list_total = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 已付
			List<Map<String, Object>> payed_moneyList = orderPayDao
					.findBySql(
							"SELECT ma.amount,c.currency_mark FROM money_amount ma LEFT JOIN currency c  "
									+ "ON ma.currencyId = c.currency_id WHERE ma.serialNum='"
									+ list.get(i).get("moneySerialNum") + "'",
							Map.class);
			// 应付
			List<Map<String, Object>> pay_moneyList = orderPayDao
					.findBySql(
							"SELECT ma.amount,c.currency_mark FROM money_amount ma LEFT JOIN currency c  "
									+ "ON ma.currencyId = c.currency_id WHERE ma.serialNum='"
									+ list.get(i).get("payPriceSerialNum")
									+ "'", Map.class);
			map.put("tname", list.get(i).get("name"));
			map.put("v_startOut",list.get(i).get("startOut"));
			map.put("total_payed", getTotalPrice(payed_moneyList));
			map.put("total_paye", getTotalPrice(pay_moneyList));
			list_total.add(map);
		}
		return getHtml(list_total,btno);
	}

	/**
	 * 单游客支付金额汇总
	 * 
	 * @param payed_moneyList
	 * @return
	 */
	private String getTotalPrice(List<Map<String, Object>> payed_moneyList) {
		String total_payed = ""; // 已付
		for (int j = 0; j < payed_moneyList.size(); j++) {
			if (j+1 == payed_moneyList.size())
				total_payed = total_payed
						+ payed_moneyList.get(j).get("currency_mark")
						+ payed_moneyList.get(j).get("amount");
			else
				total_payed = total_payed
						+ payed_moneyList.get(j).get("currency_mark")
						+ payed_moneyList.get(j).get("amount") + "+";
		}
		return total_payed;
	}

	/**
	 * 拼接html
	 * 
	 * @param list_total
	 * @return
	 */
	public String getHtml(List<Map<String, Object>> list_total,String btno) {
		StringBuffer buffer = new StringBuffer("");
		if (list_total.size() > 0) {
			buffer.append("<tr id=\"");
			buffer.append(btno);
			buffer.append("\" class=\"activity_team_top1\"  style=\"background-color: #d1e5f5;\">");
			buffer.append("<td colspan=\"19\" class=\"team_top\">");
			buffer.append("<table id=\"teamTable\" class=\"table activitylist_bodyer_table\" style=\"margin:0 auto;\">");
			buffer.append("<thead>");
			buffer.append("<tr>");
			buffer.append("<th width=\"20%\">序号</th>");
			buffer.append("<th width=\"20%\">姓名</th>");
			//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
			if("58a27feeab3944378b266aff05b627d2".equals(UserUtils.getUser().getCompany().getUuid())){
				buffer.append("<th width=\"20%\">出团日期</th>");
			}
			//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
			buffer.append("<th width=\"20%\">应收金额</th>");
			buffer.append("<th width=\"20%\">收款金额</th>");
			buffer.append("</tr>");
			buffer.append("</thead>");
			buffer.append("<tbody>");
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < list_total.size(); i++) {
				buffer.append("<tr>");
				buffer.append("	<td>");
				buffer.append(i + 1);
				buffer.append("</td>");
				buffer.append("	<td>");
				buffer.append(list_total.get(i).get("tname"));
				buffer.append("</td>");
				//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
				if("58a27feeab3944378b266aff05b627d2".equals(UserUtils.getUser().getCompany().getUuid())){
					buffer.append("	<td>");
					//排除空值影響
					if(list_total.get(i).get("v_startOut")==null||"".equals(list_total.get(i).get("v_startOut"))){
						buffer.append(" ");
					}else{
						String format = date.format(list_total.get(i).get("v_startOut"));
						buffer.append(format);
					}
					buffer.append("</td>");
				}
				//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
				buffer.append("	<td>");
				buffer.append(list_total.get(i).get("total_paye"));
				buffer.append("</td>");
				buffer.append("	<td>");
				buffer.append(list_total.get(i).get("total_payed"));
				buffer.append("</td>");
				buffer.append("</tr>");
			}

			buffer.append("</tbody>");
			buffer.append("</table>");
			buffer.append("</td>");
			buffer.append("</tr>");
		}else{
			buffer.append("<tr ><td colspan='14' ><center>收款游客已被删除</center></td></tr>");
		}
		return buffer.toString();
	}




	/**
	 * 查询批次游客付款总额
	 * @param btno
	 * @return
	 */
	public String getOrderTravellersPayed(String btno) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT t.name,o.moneySerialNum, t.payPriceSerialNum");
		buffer.append(" FROM orderpay o");
		buffer.append(" LEFT JOIN traveler t ON o.traveler_id = t.id");
		buffer.append(" WHERE o.orderPaySerialNum='");
		buffer.append(btno);
		buffer.append("'");
		//查询出素有的已付 应付 UUID 
		List<Map<String,Object>> list = orderPayDao.findBySql(buffer.toString(), Map.class);
		
		List<Map<String,Object>> payed_moneyList = null;
		BigDecimal serialNumpayedAmountToRMB = new BigDecimal("0");
		for(int i=0;i<list.size();i++)
		{
			//已付
			payed_moneyList = orderPayDao.findBySql("SELECT ma.amount,c.currency_mark,c.convert_lowest FROM money_amount ma LEFT JOIN currency c  "
					+ "ON ma.currencyId = c.currency_id WHERE ma.serialNum='"
					+ list.get(i).get("moneySerialNum")
					+ "'", Map.class);
			for (Map<String,Object> map : payed_moneyList) {
				serialNumpayedAmountToRMB = serialNumpayedAmountToRMB.add(((BigDecimal)map.get("amount")).multiply((BigDecimal)map.get("convert_lowest")));
			}
			
		}

		return serialNumpayedAmountToRMB.toString();
	}
	
	
	/**
	 * 查询批次付款游客名称：小于十个的全部显示，>=10个的只显示 10个有个名称
	 * @param btno
	 * @return
	 */
	public String getOrderTravellersPayedNames(String btno) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT t.id,t.name,o.orderPaySerialNum");  //???? 这样查不到应付的吧
		buffer.append(" FROM orderpay o");
		buffer.append(" LEFT JOIN traveler t ON o.traveler_id = t.id");
		buffer.append(" WHERE o.orderPaySerialNum='");
		buffer.append(btno);
		buffer.append("'");
		//查询出素有的已付 应付 UUID 
		List<Map<String,Object>> list = orderPayDao.findBySql(buffer.toString(), Map.class);
		
		StringBuilder sbtnames  = new StringBuilder("");
		for(int i=0;i<list.size();i++)
		{   
			if (null!=list.get(i).get("name")) {
				String nameTemp = (String)list.get(i).get("name");
				//if (!sbtnames.toString().contains(nameTemp)) {
					sbtnames.append(list.get(i).get("name")).append(",");
				//}
			}
		}
		String tnames = sbtnames.toString().replaceAll(",$", "");
		return tnames;
	}

	//---------------批量打印相关-----------------
	/**
	 * 更行Orderpay 打印时间以及打印状态
	 * @param time
	 * @param
	 */
	public void updateOrderPayByOrderPaySerialNum(Date time,String orderPaySerialNum) {
		// TODO Auto-generated method stub
//		orderPayDao.updateOrderPayByOrderPaySerialNum(time, OrderPaySerialNum);
		List<Orderpay> orderpays = orderpayDao.findOrderpaByOrderPaySerialNum(orderPaySerialNum);
		for (Orderpay orderpay : orderpays) {
			orderpay.setPrintTime(new Date());
			orderpay.setPrintFlag(1);
			orderPayDao.updateObj(orderpay);
		}
		
	}

	
	
	/**
	 * 根据serialNum 获取money_amount List
	 * 
	 * @param payedMoney
	 * @return
	 */
	public List<Map<Object, Object>> getAmountByUid(String payedMoney) {
		// TODO Auto-generated method stub
		String sql = "SELECT ma.id ,ma.serialNum,ma.currencyId,ma.amount,ma.uid,ma.moneyType,ma.orderType,ma.businessType,ma.createdBy,ma.createTime,ma.delFlag,ma.exchangerate,ma.reviewId,ma.orderPaySerialNum FROM money_amount ma WHERE ma.serialNum='"
				+ payedMoney
				+ "' ORDER BY ma.currencyId";
		
		
		return amountDao.findBySql(sql, Map.class);
	}
	
	/**
	 * 通过订单编号查询所有订单视图
	 * @param orderCode
	 * @return
	 */
	public List<?> getOrderByorderCode(String orderCode)
	{
		StringBuffer buffer  = new StringBuffer();
		buffer.append("SELECT svav.orderCode, svav.productType,svav.productTypeName,svav.salerId,svav.salerName,svav.payStatus FROM single_visa_air_view svav where svav.orderCode=?");
	 	return orderPayDao.findBySql(buffer.toString(),Map.class,orderCode);
	}

	/**
	 * 签证产品签证订单支付记录查询
	 * 
	 * @param
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	public List<Map<Object, Object>> findVisaOrderList1(Map<String, String> map) {
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT vp.id vpid,IFNULL(o.isAsAccount,'') isAsAccount,IFNULL(oo.isAsAccount,'') 'cancleFlag',o.id,IFNULL(o.payerName,'') payerName,IFNULL(o.toBankNname,'') toBankNname,IFNULL(o.accountDate,'') accountDate,IFNULL(o.orderNum,'') orderNum,IFNULL(o.payTypeName,'') payTypeName,IFNULL(o.payType,'') payType,IFNULL(o.orderPaySerialNum,'') orderPaySerialNum,IFNULL(su1.name,'') jd_Name,");
		sqlBuffer.append(" IFNULL(o.createDate,'') odate,IFNULL(o.printFlag,'') printFlag,IFNULL(o.printTime,'') printTime,IFNULL(sys.label,'') label,IFNULL(sys.vv,'') vv,IFNULL(sc.countryName_cn,'') countryName_cn,IFNULL(vp.productName,'') productName,IFNULL(vo.group_code,'') groupCode,IFNULL(su.name,'') saleName,IFNULL(suu.name,'') jdName,IFNULL(a.agentName,'') agentName,");
		sqlBuffer.append(" IFNULL(a.id,'') agentId,IFNULL(vo.total_money,'') total_money,IFNULL(vo.create_date,'') vodate,IFNULL(vo.salerId,'') salerId,IFNULL(vo.salerName,'') sName,IFNULL(vo.create_by,'') voCreate_by,IFNULL(suu.name,'') suuName, IFNULL(vo.group_code,'') group_code,vo.id vorderid,IFNULL(vo.accounted_money,'') accounted_money,'order_money','account_money','payed_money'");
		sqlBuffer.append(" FROM orderpay o");
		sqlBuffer.append(" LEFT JOIN  (SELECT  o1.orderNum,o1.isAsAccount FROM  orderpay o1  WHERE  o1.isAsAccount =1 GROUP  BY o1.orderNum)oo  ON o.orderNum=oo.orderNum ");
		sqlBuffer.append(" LEFT JOIN  visa_order vo ON o.orderNum= vo.order_no");
		sqlBuffer.append(" LEFT JOIN visa_products vp ON vo.visa_product_id=vp.id");
		sqlBuffer.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id");
		sqlBuffer.append(" LEFT JOIN sys_user su ON o.createBy = su.id");
		sqlBuffer.append(" LEFT JOIN sys_user suu ON vo.create_by = suu.id");
		sqlBuffer.append(" LEFT JOIN sys_user su1 ON vp.createBy = su1.id");
		sqlBuffer.append(" LEFT JOIN  (SELECT value vv, label FROM sys_dict WHERE delFlag = 0 AND type = 'new_visa_type') AS sys ON concat(vp.visaType) = sys.vv");
		sqlBuffer.append(" LEFT JOIN sys_country sc ON vp.sysCountryId = sc.id");
		if(StringUtils.isNotBlank(map.get("payAmountStrat")) ||StringUtils.isNotBlank(map.get("payAmountEnd")))
		{ 
			sqlBuffer.append(" inner JOIN ");
			sqlBuffer.append(getTravellerMoneySql(map));
			sqlBuffer.append(" ON o.orderPaySerialNum =tmp.orderPaySerialNum");
		}
		sqlBuffer.append(" WHERE o.orderType = 6 AND o.payPriceType != 16 ");
		/*add by ang.gao */
		sqlBuffer.append(" AND vo.del_flag = '0' ");
		sqlBuffer.append(" AND o.delFlag = 0 AND su.companyId =" + userCompanyId);
		sqlBuffer.append(" AND suu.companyId = " + userCompanyId + " AND su1.companyId =" + userCompanyId);
		sqlBuffer.append(" AND vo.total_money IS NOT null");
		// 拼接游客姓名 查询条件
		if(StringUtils.isNotBlank(map.get("travellerName")))
		{ 
			sqlBuffer.append(" AND o.traveler_id IN (SELECT t.id  FROM traveler t WHERE t.name LIKE '%");
			sqlBuffer.append(map.get("travellerName").trim());
			sqlBuffer.append("%') ");
		}
		serarchCondtion(map, sqlBuffer);
		sqlBuffer.append(" GROUP BY o.orderPaySerialNum ,o.isAsAccount,o.payType");
		if(StringUtils.isNotBlank(map.get("orderBy").trim())) {
			sqlBuffer.append(" order by ");
			sqlBuffer.append(map.get("orderBy").trim());
			sqlBuffer.append(" ");
		}
		List<Map<Object, Object>> ls= visaOrderDao.findBySql(sqlBuffer.toString(), Map.class);
		dealWithResult(ls);
		return ls;
	}

}

