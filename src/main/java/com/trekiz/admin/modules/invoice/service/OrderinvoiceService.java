package com.trekiz.admin.modules.invoice.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.repository.OrderinvoiceDao;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class OrderinvoiceService {

	@Autowired
	private OrderinvoiceDao orderinvoiceDao;
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void saveOrderInvoice(Orderinvoice invoice) {

		orderinvoiceDao.save(invoice);
	}

	/**
	 * 查询发票信息表中申请的开发票信息
	 * 
	 * 创建人：liangjingming 创建时间：2014-1-27 下午1:44:46 修改人：liangjingming
	 * 修改时间：2014-1-27 下午1:44:46
	 * 
	 * @version
	 *
	 */
	public List<Orderinvoice> findApplyOrderinvoice(Integer orderid, String audited) {
		return orderinvoiceDao.findApplyOrderinvoice(orderid, StringUtils.toInteger(audited));
	}

	public Orderinvoice findOrderinvoiceById(Long id) {
		return orderinvoiceDao.findOne(id);
	}

	/**
	 * 发票信息查询
	 * update by shijun.liu 2015.03.26
	 * @param page
	 * @param verifyStatus
	 * @param mapRequest
	 * @param company
	 * @return
	 */
	public Page<Map<Object, Object>> getSupplyinvoiceList(
			Page<Map<Object, Object>> page, String verifyStatus,
			Map<String,String> mapRequest,Long company, TravelActivity travelActivity) {
		StringBuffer sql = new StringBuffer();
		String invoiceNum = mapRequest.get("invoiceNum");//发票号 
		String invoiceType = mapRequest.get("invoiceType");//开票类型 
		String selectVerifyStatus = mapRequest.get("selectVerifyStatus");//审核状态 
		String invoiceSubject = mapRequest.get("invoiceSubject");//开票项目 
		String invoiceTimeBegin = mapRequest.get("invoiceTimeBegin");//开票日期开始 
        String invoiceTimeEnd = mapRequest.get("invoiceTimeEnd");    //开票日期结束 
        String invoiceHead = mapRequest.get("invoiceHead");//发票台头
        String invoiceComingUnit = mapRequest.get("invoiceComingUnit");//来款单位   0414 新增   update by pengfei.shang
		String createName = mapRequest.get("createName");//申请人 
		String invoiceCustomer = mapRequest.get("invoiceCustomer");//开票客户 
        String applyInvoiceBegin = mapRequest.get("applyInvoiceBegin");//申请日期开始
        String applyInvoiceEnd = mapRequest.get("applyInvoiceEnd");    //申请日期结束 
        String invoiceMoneyBegin = mapRequest.get("invoiceMoneyBegin");//发票金额开始 
        String invoiceMoneyEnd = mapRequest.get("invoiceMoneyEnd");//发票金额结束 
        String orderNum = mapRequest.get("orderNum");//订单号 
		String groupCode = mapRequest.get("groupCode");//团号 
		String makeOrderBegin = mapRequest.get("orderTimeBegin");//下单日期开始 1
        String makeOrderEnd = mapRequest.get("orderTimeEnd");//下单日期结束 1
        String createStatus = mapRequest.get("createStatus");//开票状态 
        //开票方式查询条件20150728
        String applyInvoiceWay = mapRequest.get("applyInvoiceWay");// 0444 发票申请方式
        String invoiceMode = mapRequest.get("invoiceMode");      
        
		sql.append("SELECT a.invoiceHead,a.invoiceComingUnit,a.invoiceNum,a.uuid,a.invoiceMode,a.invoiceType,a.invoiceSubject, ")
	       .append(" a.invoiceCustomer,a.createDate,a.updateDate,a.createName,FORMAT(b.invoiceAmount, 2) AS invoiceAmount,a.verifyStatus, ")
	       .append(" a.updateName,a.remarks,a.createStatus,a.receiveStatus,a.reviewRemark,a.invoiceRemark,a.applyInvoiceWay,a.receivedPayStatus ")
	       .append(" FROM (SELECT DISTINCT o.invoiceNum,o.uuid,o.invoiceMode,o.invoiceType,o.invoiceSubject,o.invoiceCustomer,o.invoiceHead, ")
	       .append(" o.invoiceComingUnit,o.updateDate,o.createDate,o.applyInvoiceWay,o.receivedPayStatus, ")
	       .append(" (select name from sys_user su where su.id = o.createBy and su.delFlag='0') as createName, ")
	       .append(" (select name from sys_user su where su.id = o.updateBy and su.delFlag='0') as updateName, ")
	       .append(" o.verifyStatus,o.updateBy,o.remarks,o.createStatus,o.receiveStatus,o.reviewRemark,o.invoiceRemark FROM orderinvoice o ")
	       .append(" WHERE o.delFlag = '0' and o.invoiceCompany = ? ");
		
		if(StringUtils.isNotBlank(invoiceMode)){
			sql.append(" and o.invoiceMode = ").append(invoiceMode);
		}
		if(StringUtils.isNotBlank(orderNum)){
			sql.append(" and o.orderNum like '%").append(orderNum).append("%'");
		}
		if(StringUtils.isNotBlank(groupCode)){
			sql.append(" and o.groupCode like '%").append(groupCode).append("%'");
		}
		if(StringUtils.isNotBlank(invoiceNum)){
			sql.append(" and o.invoiceNum like '%").append(invoiceNum).append("%'");
			sql.append(" and o.invoiceNum not like 'TTS%'");//不显示虚拟发票号
		}
		if(StringUtils.isNotBlank(invoiceType)){
			sql.append(" and o.invoiceType = ").append(invoiceType);
		}
		if(StringUtils.isNotBlank(createStatus)){
			sql.append(" and o.createStatus = ").append(createStatus);
		}
		if(StringUtils.isNotBlank(applyInvoiceWay)){// 0444 发票申请方式
			sql.append(" and o.applyInvoiceWay = ").append(applyInvoiceWay);
		}
		if(StringUtils.isNotBlank(selectVerifyStatus)){
			sql.append(" and o.verifyStatus = ").append(selectVerifyStatus);
		}
		if(!StringUtils.isBlank(verifyStatus)){
			if("ne0".equals(verifyStatus)){
				sql.append(" and o.verifyStatus in (1,2)");
			}else{
				sql.append(" and o.verifyStatus = ").append(verifyStatus);
			}
		}
		if(StringUtils.isNotBlank(invoiceSubject)){
			sql.append(" and o.invoiceSubject = ").append(invoiceSubject);
		}
		if(StringUtils.isNotBlank(invoiceTimeBegin)){
			sql.append(" and o.updateDate  between '").append(invoiceTimeBegin).append(" 00:00:00' and ");
			if(StringUtils.isNotBlank(invoiceTimeEnd)){
				sql.append("'").append(invoiceTimeEnd).append(" 23:59:59'");
			}else{
				sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
			}
			sql.append(" and o.createStatus = 1 ");
		}else{
			sql.append(" and o.updateDate  between '").append(Context.DEFAULT_BEGIN_TIME).append("' and ");
			if(StringUtils.isNotBlank(invoiceTimeEnd)){
				sql.append("'").append(invoiceTimeEnd).append(" 23:59:59'")
				   .append(" and o.createStatus = 1 ");
			}else{
				sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
			}
		}
		if(StringUtils.isNotBlank(invoiceHead)){
			sql.append(" and o.invoiceHead like '%").append(invoiceHead).append("%'");
		}
		//0414 新增来款单位 update by pengfei.shang
		if(StringUtils.isNotBlank(invoiceComingUnit)){
			sql.append(" and o.invoiceComingUnit like '%").append(invoiceComingUnit).append("%'");
		}
		if(StringUtils.isNotBlank(invoiceCustomer)){
			sql.append(" and o.invoiceCustomer like '%").append(invoiceCustomer).append("%'");
		}
		if(StringUtils.isNotBlank(applyInvoiceBegin)){
			sql.append(" and o.createDate  between '").append(applyInvoiceBegin).append(" 00:00:00' and ");
			if(StringUtils.isNotBlank(applyInvoiceEnd)){
				sql.append("'").append(applyInvoiceEnd).append(" 23:59:59'");
			}else{
				sql.append("'").append(Context.DEFAULT_END_TIME).append("'");;
			}
		}else{
			sql.append(" and o.createDate  between '").append(Context.DEFAULT_BEGIN_TIME).append("' and ");
			if(StringUtils.isNotBlank(applyInvoiceEnd)){
				sql.append("'").append(applyInvoiceEnd).append(" 23:59:59'");
			}else{
				sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
			}
		}
		
		// 0539-所有产品线申请发票可输入负值 modify by wangyang 2016.10.20
		// 屏蔽显示的金额限制 0 到  2147483647 之间
		if(StringUtils.isNotBlank(invoiceMoneyBegin)){
			sql.append(" and o.invoiceAmount  between ").append(invoiceMoneyBegin).append(" and ");
			if(StringUtils.isNotBlank(invoiceMoneyEnd)){
				sql.append(invoiceMoneyEnd);
			}else{
				sql.append(Double.MAX_VALUE);
			}
		}else{
			sql.append(" and o.invoiceAmount between ").append(-Double.MAX_VALUE).append(" and ");
			if(StringUtils.isNotBlank(invoiceMoneyEnd)) {
				sql.append(invoiceMoneyEnd);
			}else{
				sql.append(Integer.MAX_VALUE);
			}
		}
		
		if (StringUtils.isNotBlank(makeOrderBegin) || StringUtils.isNotBlank(makeOrderEnd)) {
			sql.append(" and exists (SELECT DISTINCT orderNum,createDate,orderType FROM (")
			   .append(" SELECT orderNum,oo.createDate,oo.orderStatus AS orderType FROM")
			   .append(" productorder oo WHERE delFlag = '0' ")
			   .append(" UNION SELECT order_no AS orderNum,oo.create_date AS createDate,7 AS orderType FROM")
			   .append(" airticket_order oo WHERE oo.del_flag = '0' ")
			   .append(" UNION SELECT order_no AS orderNum,oo.create_date AS createDate,6 AS orderType FROM")
			   .append(" visa_order oo WHERE oo.del_flag = '0') t1 where t1.orderNum = o.orderNum and t1.orderType = o.orderType ");
			if (StringUtils.isNotBlank(makeOrderBegin)) {
				sql.append(" and t1.createDate  between '").append(makeOrderBegin).append(" 00:00:00' and ");
				if(StringUtils.isNotBlank(makeOrderEnd)){
					sql.append("'").append(makeOrderEnd).append(" 23:59:59'");
				}else{
					sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
				}
			} else {
				sql.append(" and t1.createDate  between '").append(Context.DEFAULT_BEGIN_TIME).append("' and ");
				if(StringUtils.isNotBlank(makeOrderEnd)){
					sql.append("'").append(makeOrderEnd).append(" 23:59:59'");
				}else{
					sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
				}
			}
			sql.append(")");
		}
	    sql.append(" group by o.uuid) a,(SELECT uuid,invoiceNum, sum(invoiceAmount) AS invoiceAmount FROM orderinvoice ")
	       .append(" where invoiceCompany = ").append(company).append(" GROUP BY uuid) b,")
	       .append("(SELECT uuid,invoiceNum, max(updateDate) AS updateDate FROM orderinvoice ")
	       .append(" where invoiceCompany = ").append(company).append(" GROUP BY uuid) c,")
	       .append("(SELECT uuid,invoiceNum, min(createDate) AS createDate FROM orderinvoice ")
	       .append(" where invoiceCompany = ").append(company).append(" GROUP BY uuid) d ")
	       .append(" WHERE a.uuid = b.uuid AND a.uuid = c.uuid")
	       .append(" AND a.uuid = d.uuid ");
	    if(StringUtils.isNotBlank(createName)){
			sql.append(" and createName like '%").append(createName).append("%'");
	    }
	    if(StringUtils.isBlank(page.getOrderBy())){
			sql.append(" order by createDate desc");
		}
		return orderinvoiceDao.findBySql(page, sql.toString(), Map.class, company);
	}
	

	public boolean invoiceNum(String inoviceNum, Long invoiceCompany) {
		return orderinvoiceDao.findByInvoiceNumAndInvoiceCompany2(inoviceNum, invoiceCompany).size() == 0;
	}
	
	/**
	 * update by ruyi.chen
	 * 2015-01-16
	 * 添加达帐金额查询字段
	 * @param invoiceNum
	 * @param company
	 * @return List<Object[]>    返回类型 
	 * @throws
	 * */
	public List<String[]> findSupplyLimitDetailList(String uuid, Long company) {
		StringBuffer sql = new StringBuffer();
		//增加多产品线合开发票20150720
		// 机票产品线
		sql.append("SELECT invoice.orderNum, invoice.groupCode, invoice.invoiceCustomer, sysuser.NAME AS createName, ")
		   .append(" date_format(ao.create_date,'%Y-%m-%d') AS orderTime, ao.person_num AS orderPersonNum, CURDATE() AS groupOpenDate, ")
		   .append(" CURDATE() AS groupCloseDate, ao.total_money AS totalMoney, ao.payed_money, b.invoiceAmount, ")
		   .append(" invoice.invoiceAmount AS amount, invoice.orderType, ao.accounted_money, b.orderId ")
		   .append("FROM sys_user sysuser, orderinvoice invoice, airticket_order ao, ")
		   .append(" (SELECT orderNum,orderId,sum(case createstatus when 0 then 0 when 1 then invoiceamount end ) invoiceamount ")
		   .append(" FROM orderinvoice group by orderNum,orderId) b ")
		   .append("WHERE invoice.orderId = ao.id AND invoice.createBy = sysuser.id AND invoice.uuid = '").append(uuid).append("' ")
		   .append(" AND invoice.invoiceCompany = ").append(company)
		   .append(" AND invoice.orderNum = b.orderNum ")
		   .append(" AND invoice.orderType = ").append(Context.ORDER_TYPE_JP)
		   
		   // 签证产品线
		   // C460V3 签证团号取签证产品的虚拟团号  modify by wangyang  2016.4.15
		   .append(" UNION ")
		   .append("SELECT invoice.orderNum,invoice.groupCode,invoice.invoiceCustomer,sysuser.NAME AS createName, ")
		   .append(" date_format(vo.create_date,'%Y-%m-%d') as orderTime,vo.travel_num orderPersonNum,CURDATE() groupOpenDate, ")
		   .append(" CURDATE() groupCloseDate,vo.total_money AS totalMoney,vo.payed_money,b.invoiceAmount,")
		   .append(" invoice.invoiceAmount amount, invoice.orderType, vo.accounted_money, b.orderId ")
		   .append("FROM sys_user sysuser,orderinvoice invoice,visa_order vo, ")
		   .append(" (select orderNum,orderId,sum(case createstatus when 0 then 0 when 1 then invoiceamount end ) invoiceamount ")
		   .append(" FROM orderinvoice group by orderNum,orderId) b ")
		   .append("WHERE invoice.orderId = vo.id AND invoice.createBy = sysuser.id ")
		   .append(" AND invoice.uuid = '").append(uuid).append("' AND invoice.invoiceCompany = ").append(company)
		   .append(" AND invoice.orderNum = b.orderNum ")
		   .append(" AND invoice.orderType = ").append(Context.ORDER_TYPE_QZ)
		   
		   // 团期类产品线
		   .append(" UNION ")
		   .append("SELECT invoice.orderNum,invoice.groupCode,invoice.invoiceCustomer,sysuser.NAME AS createName, ")
		   .append(" date_format(porder.orderTime,'%Y-%m-%d') as orderTime,porder.orderPersonNum,groups.groupOpenDate,groups.groupCloseDate, ")
		   .append(" porder.total_money AS totalMoney,porder.payed_money,b.invoiceAmount,invoice.invoiceAmount amount,invoice.orderType, ")
		   .append(" porder.accounted_money,b.orderId ")
		   .append("FROM sys_user sysuser,orderinvoice invoice,productorder porder,travelactivity activity,activitygroup groups, ")
		   .append(" (SELECT orderNum,orderId,sum(case createstatus when 0 then 0 when 1 then invoiceamount end ) invoiceamount ")
		   .append(" FROM orderinvoice group by orderNum,orderId) b ")
		   .append("WHERE invoice.orderId = porder.id AND invoice.createBy = sysuser.id AND activity.id = groups.srcActivityId ")
		   .append(" AND porder.productGroupId = groups.id AND invoice.uuid = '").append(uuid).append("' ")
		   .append(" AND activity.proCompany = ").append(company)
		   .append(" AND invoice.invoiceCompany = ").append(company)
		   .append(" AND invoice.orderType in (1,2,3,4,5,10) AND invoice.orderNum = b.orderNum");
		
		return orderinvoiceDao.findBySql(sql.toString());
	}
	/**
	 * 
	* @Title: findSupplyLimitDetailList 
	* @Description: TODO(发票相关订单详情)
	* @param @param orderinvoceList
	* @return List<Object[]>    返回类型 
	* @throws
	 */
	public List<String[]> findSupplyLimitDetailList(List<Orderinvoice> orderinvoceList,String currencyId,
											String orderMoneyBegin,String orderMoneyEnd, String operator) {
		StringBuffer sql = new StringBuffer();
		//支持多产品线合开发票20150716
		if(orderinvoceList != null && orderinvoceList.size() > 0) {
			List<String> selectParaSet = Lists.newArrayList();
			for (Orderinvoice orderinvoice : orderinvoceList) {
				Integer orderType = orderinvoice.getOrderType();
				String uuid = orderinvoice.getUuid();
				Long invoiceCompany = orderinvoice.getInvoiceCompany();
				String selectPara = orderType + uuid + invoiceCompany;
				// 如果sql已经组装过一遍，则相同sql需要过滤
				if (selectParaSet.contains(selectPara)) {
					continue;
				} else {
					selectParaSet.add(selectPara);
				}
				StringBuffer str = new StringBuffer();
				str.append(" AND EXISTS ( select 1 from money_amount ma where ma.businessType = 1 and ma.orderType = ")
				   .append(orderType);
				boolean b = false;
				int argsOperator = -1;
				if (StringUtils.isNotBlank(currencyId)) {
					b = true;
					str.append(" and ma.currencyId = ").append(currencyId);
				}
				if (StringUtils.isNotBlank(orderMoneyBegin)) {
					b = true;
					str.append(" and ma.amount between ").append(orderMoneyBegin);
				}
				if (StringUtils.isNotBlank(orderMoneyEnd)) {
					b = true;
					str.append(" and ").append(orderMoneyEnd);
				}
				if (StringUtils.isNotBlank(operator)) {
					argsOperator = Integer.parseInt(operator);
				}
				//签证
				if(null != orderType && orderType == Context.ORDER_TYPE_QZ) {
					if(sql.indexOf("select") != -1) {
						sql.append(" union ");
					}
					sql.append("select t1.orderNum,t1.groupCode,t1.invoiceCustomer,t1.createName,t1.orderTime,t1.orderPersonNum,t1.groupOpenDate,")
					.append("t1.groupCloseDate,t2.payed_money,t3.accounted_money,t1.invoiceAmount,t1.amount,t1.orderType,t1.id,t1.total_money,")
					.append("t1.operatorName FROM (select 6 as orderType,vo.id,vo.total_money,invoice.orderNum,");
					//C460V3plus 环球行签证团号取订单团号  wangyang 2016.5.6
					if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
						sql.append(" vo.group_code AS groupCode, ");
					}else{
						sql.append(" vp.groupCode, ");
					}
					sql.append("invoice.invoiceCustomer,sysuser.NAME AS createName,date_format(vo.create_date,'%Y-%m-%d') orderTime,")
					.append("vo.travel_num orderPersonNum,' ' groupOpenDate,' ' groupCloseDate,vo.payed_money payed_money,vo.accounted_money accounted_money,")
					.append("FORMAT(b.invoiceAmount,2) invoiceAmount,FORMAT(invoice.invoiceAmount,2) amount,tt.name as operatorName from visa_order vo ")
					.append(" LEFT JOIN orderinvoice invoice on invoice.orderId = vo.id LEFT JOIN sys_user sysuser on vo.create_by = sysuser.id")
					.append(" left join (select uu.name,uu.id as userId,vp.id,vp.proCompanyId ")
					.append(" from visa_products vp left join sys_user uu on vp.createBy = uu.id ) tt on tt.id = vo.visa_product_id ,")
					.append("(select orderNum,sum(case createstatus when 0 then 0 when 1 then invoiceamount end ) invoiceamount from orderinvoice GROUP BY orderNum) b, ")
					.append(" visa_products vp ")
					.append(" where invoice.orderNum = b.orderNum ")
					.append(" AND vp.id = vo.visa_product_id ");
					if(b){
						sql.append(str.toString()).append(" and ma.serialNum = vo.total_money )");
					}
					if(-1 != argsOperator){//添加计调条件
						sql.append(" and tt.userId = ").append(argsOperator);
					}
					sql.append(" and invoice.orderType = ").append(orderType)
					   .append(" and invoice.uuid = '").append(uuid).append("'")
					   .append(" and tt.proCompanyId = ").append(invoiceCompany).append(" order by vo.create_date) t1 ")
					   .append(" left join (select ma.serialNum, group_concat(concat(c.currency_mark,ma.amount) separator '+') payed_money ")
					   .append(" from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType =  ").append(Context.MONEY_TYPE_YS)
					   .append(" and ma.orderType = ").append(orderType)
					   .append(" and ma.businessType = 1 and c.create_company_id = ").append(invoiceCompany)
					   .append(" group by ma.serialNum) t2 on t1.payed_money = t2.serialNum ")
					   .append(" left join (select ma.serialNum, group_concat(concat(c.currency_mark,ma.amount) separator '+') accounted_money ")
					   .append(" from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = ").append(Context.MONEY_TYPE_DZ)
					   .append(" and ma.orderType = ").append(orderType)
					   .append(" and ma.businessType = 1 and c.create_company_id = ").append(invoiceCompany)
					   .append(" group by ma.serialNum) t3 on t1.accounted_money = t3.serialNum");
				}
				//机票
				else if(null != orderType && orderType == Context.ORDER_TYPE_JP) {
					if(sql.indexOf("select") != -1) {
						sql.append(" union ");
					}
					sql.append("select t1.orderNum,t1.groupCode,t1.invoiceCustomer,t1.createName,t1.orderTime,t1.orderPersonNum,t1.groupOpenDate,")
					.append("t1.groupCloseDate,t2.payed_money,t3.accounted_money,t1.invoiceAmount,t1.amount,t1.orderType,t1.id,t1.total_money,")
					.append(" t1.operatorName from (select invoice.orderType,ao.id,ao.total_money,invoice.orderNum,")
					.append("invoice.groupCode,invoice.invoiceCustomer,sysuser. NAME AS createName,date_format(ao.create_date,'%Y-%m-%d') orderTime,ao.person_num orderPersonNum,' ' groupOpenDate,")
					.append("' ' groupCloseDate,ao.payed_money payed_money,ao.accounted_money accounted_money,FORMAT(b.invoiceAmount,2) invoiceAmount,")
					.append("FORMAT(invoice.invoiceAmount,2) amount ,tt.name as operatorName ")
					.append(" from airticket_order ao LEFT JOIN orderinvoice invoice on invoice.orderId = ao.id ")
					.append(" LEFT JOIN sys_user sysuser on ao.create_by = sysuser.id ")
					.append(" left join (select uu.name,uu.id as userId,aa.id,aa.proCompany proCompanyId from activity_airticket aa ")
					.append(" left join sys_user uu on aa.createBy = uu.id ) tt on ao.airticket_id  = tt.id,(select orderNum,")
					.append(" sum(case createstatus when 0 then 0 when 1 then invoiceamount end ) invoiceamount from orderinvoice ")
					.append(" GROUP BY orderNum) b where invoice.orderNum = b.orderNum ");
					if(b){
						sql.append(str.toString()).append(" and ma.serialNum = ao.total_money )");
					}
					if(-1 != argsOperator){//添加计调条件
						sql.append(" and tt.userId = ").append(argsOperator);
					}
					sql.append(" and invoice.orderType = " + orderType + " and invoice.uuid = '" + uuid + "' and tt.proCompanyId = " + invoiceCompany + " ORDER BY ao.create_date) t1 ")
					.append(" left join (select ma.serialNum, group_concat(concat(c.currency_mark,ma.amount) separator '+') payed_money ")
					.append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_YS)
					.append(" and ma.orderType = " + orderType + " and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t2 on t1.payed_money = t2.serialNum ")
					.append("left join (select ma.serialNum, group_concat(concat(c.currency_mark,ma.amount) separator '+') accounted_money ")
					.append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_DZ)
					.append(" and ma.orderType = " + orderType + " and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t3 on t1.accounted_money = t3.serialNum");
				}
				//单团
				else {
					if(sql.indexOf("select") != -1) {
						sql.append(" union ");
					}
					sql.append("select t1.orderNum,t1.groupCode,t1.invoiceCustomer,t1.createName,t1.orderTime,t1.orderPersonNum,t1.groupOpenDate,")
					.append("t1.groupCloseDate,t2.payed_money,t3.accounted_money,t1.invoiceAmount,t1.amount,t1.orderStatus as orderType,t1.id,t1.total_money,t1.operatorName from (")
					.append("select porder.orderStatus,porder.id,porder.total_money,invoice.orderNum,invoice.groupCode,invoice.invoiceCustomer,sysuser.NAME AS createName,")
					.append("date_format(porder.orderTime,'%Y-%m-%d') as orderTime,porder.orderPersonNum,tt.groupOpenDate,tt.groupCloseDate,porder.accounted_money,")
					.append("porder.payed_money,FORMAT(b.invoiceAmount,2) invoiceAmount,FORMAT(invoice.invoiceAmount,2) amount,invoice.orderType,tt.name as operatorName ")
					.append(" from sys_user sysuser,orderinvoice invoice,productorder porder,")
					.append("travelactivity activity,(select ag.id,uu.name,uu.id as userId,ag.groupOpenDate,")
					.append("ag.groupCloseDate,ag.srcActivityId from activitygroup ag left join sys_user uu on ag.createBy = uu.id ) tt,")
					.append(" (select orderNum,sum(case createstatus when 0 then 0 when 1 then invoiceamount end ) invoiceamount from ")
					.append("orderinvoice group by orderNum) b where invoice.orderId = porder.id and porder.createBy = sysuser.id")
					.append(" and activity.id = tt.srcActivityId and porder.productGroupId = tt.id ");
					if(b){
						sql.append(str.toString()).append(" and ma.serialNum = porder.total_money )");
					}
					if(-1 != argsOperator){//添加计调条件
						sql.append(" and tt.userId = ").append(argsOperator);
					}
					sql.append("and invoice.orderType = " + orderType + " and invoice.uuid = '" + uuid + "' and activity.proCompany = " + invoiceCompany + " and invoice.orderNum = b.orderNum order by porder.orderTime ) t1 ")
					.append("left join (select ma.serialNum, group_concat(concat(c.currency_mark,ma.amount) separator '+') payed_money ")
					.append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_YS)
					.append(" and ma.orderType = " + orderType + " and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t2 on t1.payed_money = t2.serialNum ")
					.append("left join (select ma.serialNum, group_concat(concat(c.currency_mark,ma.amount) separator '+') accounted_money ")
					.append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_DZ)
					.append(" and ma.orderType = " + orderType + " and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t3 on t1.accounted_money = t3.serialNum ");
				}
			}
		}
		return orderinvoiceDao.findBySql(sql.toString());
	}
	/**
	 * 
	* @Title: findInvoiceDetails 
	* @Description: TODO(发票详情) 
	* @param @param invoiceNum
	* @param @return    设定文件 
	* @return List<Object[]>    返回类型 
	* @throws
	 */
	public List<Orderinvoice> findInvoiceDetails(String uuid,Long invoiceCompany){
		return orderinvoiceDao.findByInvoiceNumAndInvoiceCompany(uuid, invoiceCompany);
	}
	/**
	 * 
	* @Title: updateOrderinvoice 
	* @Description: TODO(更新审核状态) 
	* @param @param invoiceNum    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void updateOrderinvoice(String uuid, String reviewRemark, Integer verifyStatus){
		String sql = "update Orderinvoice set verifyStatus = ?,reviewRemark = ? where uuid = ?";
		orderinvoiceDao.update(sql, verifyStatus, reviewRemark, uuid);
	}
	/**
	 * 
	* @Title: updateOrderinvoiceCreateStatus 
	* @Description: TODO(更新开票状态) 
	* @param @param invoiceNum
	* @param @param createStatus    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void updateOrderinvoiceCreateStatus(String uuid, String invoiceNumber, String invoiceRemark, 
			Integer createStatus, Date invoiceTime) {
		String sql = "update Orderinvoice set createStatus = ?,invoiceNum = ?,invoiceRemark = ?,updateDate=? where uuid = ?";
		orderinvoiceDao.update(sql, createStatus, invoiceNumber, invoiceRemark, invoiceTime, uuid);
	}
	/**
	 * 
	* @Title: updateOrderinvoiceReceiveStatus 
	* @Description: TODO(更新领取状态) 
	* @param @param invoiceNum
	* @param @param receiveStatus    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void updateOrderinvoiceReceiveStatus(String uuid, Integer receiveStatus) {
		String sql = "update Orderinvoice set receiveStatus = ?,updateDate=? where uuid = ?";
		orderinvoiceDao.update(sql, receiveStatus, new Date(), uuid);
	}
	/**
	 * 
	* @Title: createVirtualInvoiceNum 
	* @Description: TODO(创建虚拟发票号，规则TTS+系统时间戳) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String createVirtualInvoiceNum() {
		return "TTS" + System.currentTimeMillis() + new Random().nextInt(100000); 
	}
	
	/**
	 * 获取订单的所有退款数据
	 * @param orderId 订单id
	 * @param orderType 订单类型
	 * @return
	 * */
	public List<Map<String,Object>> getOrderAllRefund(String orderId, String orderType) {
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT A.orderId,group_concat(format(IFNULL(A.totalMoney,0),2),A.currency_name ORDER BY A.currencyId separator '+') as refundTotalStr, ")
		   .append(" IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0) as refundTotal ")
		   .append("FROM (select sum(a.amount) as totalMoney, a.currencyId, c.currency_name, c.currency_exchangerate, a.orderId ")
		   .append(" FROM (select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,mac.orderType,p.id as orderId ")
		   .append(" FROM money_amount mac, ");
		
		switch(orderType){
			case Context.ORDER_STATUS_VISA://签证
				sbf.append(" visa_order p WHERE mac.uid = p.id AND mac.businessType=1  AND mac.moneyType =11 AND mac.orderType=? AND p.id=? ");
				sbf.append(") a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId ) A GROUP BY A.orderId ");
				break;
			case Context.ORDER_STATUS_AIR_TICKET://机票
				sbf.append(" airticket_order p WHERE mac.uid = p.id AND mac.businessType=1 AND mac.moneyType =11 AND mac.orderType=? AND p.id=? ");
				sbf.append(") a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId ) A GROUP BY A.orderId ");
				break;
			default://团期类
				sbf.append(" productorder p WHERE mac.uid = p.id AND mac.businessType=1 AND mac.moneyType =11 AND mac.orderType = p.orderStatus ")
				   .append(" AND p.orderStatus=? AND p.id=? ")
				   .append(") a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId ) A GROUP BY A.orderId ");
				break;
		}
		return orderinvoiceDao.findBySql(sbf.toString(), Map.class, orderType, orderId);
		
	}
	
	/**
	 * 开票页面发票撤销功能，将已开票发票改成待开票
	 * @param invoiceNum   发票号
	 * @return 修改记录数
	 * @author shijun.liu
	 */
	public int revokeToUninvioce(String uuid){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String hsql = "update Orderinvoice set createStatus = 0 ,receiveStatus = 0,invoiceNum = null, updateDate = ? where uuid = ? and invoiceCompany = ?";
		return orderinvoiceDao.update(hsql, new Date(), uuid, companyId);
	}
	
	/**
	 * 已审核页面发票撤销功能，将已审核发票改成待审核
	 * @param invoiceNum 发票号
	 * @return 修改记录数
	 * @author shijun.liu
	 */
	public int revokeToUncheck(String uuid){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String hsql = "update Orderinvoice set verifyStatus = 0 where uuid = ? and invoiceCompany = ?";
		return orderinvoiceDao.update(hsql, uuid, companyId);
	}
	
	/**
	 * 根据订单id 和订单类型查询单个订单的已开发票 记录 by chy 2015年7月14日16:46:14
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<Orderinvoice> findOrderinvoiceByOrderIdOrderType(Integer orderId,Integer orderType){
		return orderinvoiceDao.findOrderinvoiceByOrderIdOrderType(orderId, orderType);
	}
	
	/**
	 * 根据订单id 和订单类型查询单个订单的已开发票（新）
	 * @author yang.jiang 2016-2-25 20:54:33
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<Orderinvoice> findCreatedInvoiceByOrder(Integer orderId,Integer orderType){
		return orderinvoiceDao.findCreatedInvoiceByOrder(orderId, orderType);
	}
	
	/**
	 * 根据订单id 和订单类型查询单个订单的待开发票 记录 by chy 2016年1月5日20:13:01
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<Orderinvoice> findOrderinvoiceByOrderIdOrderType2(Integer orderId,Integer orderType){
		return orderinvoiceDao.findOrderinvoiceByOrderIdOrderType2(orderId, orderType);
	}
	
	/**
	 * 更新发票表 把所有UUID为空的记录加上新的UUID
	 */
	public void updateInvoiceUUID(){
		String sql = "SELECT max(invoiceNum) FROM orderinvoice WHERE uuid is null group by invoicenum";
		List<String> invoiceNumList = orderinvoiceDao.findBySql(sql);
		
		String updateSql = null;
		for(String invoiceNum : invoiceNumList){
			if(invoiceNum == null || "".equals(invoiceNum)){
				continue;
			}
			updateSql = "update Orderinvoice set uuid = '" + UUID.randomUUID().toString() + "' where invoiceNum = '" + invoiceNum + "'";
			orderinvoiceDao.update(updateSql);
		}
		
	}
	
	/**
	 * 获取待审核的发票数据数量
	 */
	public int getToBeReviewInvoiceNum(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String sql = "select count(*) from orderinvoice where delFlag = 0 and invoiceCompany = " + companyId + 
				" and verifyStatus = 0 group by uuid";
		List<Object> list = orderinvoiceDao.findBySql(sql);
		if(list == null || list.size() == 0){
			return 0;
		}
		return list.size();
	}
	
	/**
	 * 获取待开票的发票数据数量
	 */
	public int getReviewedInvoiceNum(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String sql = "select count(*) from orderinvoice where delFlag = 0 and invoiceCompany = " + companyId + 
				" and createStatus = 0 and verifyStatus = 1 group by uuid";
		List<Object> list = orderinvoiceDao.findBySql(sql);
		if(list == null || list.size() == 0){
			return 0;
		}
		return list.size();
	}
	
	/**
	 * 根据ids查询发票信息 by chy 2016-1-18 16:37:44
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> getInvoiceListByIds(String ids) {
		if(StringUtils.isBlank(ids)){
			return null;
		}
		//						                开票项目		    开票类型             开票方式             发票抬头		   开票客户  	      开票原因                                                      本次开票金额             uuid
		String sqlString = "select invoiceSubject,invoiceType,invoiceMode,invoiceHead,invoiceCustomer,remarks,sum(invoiceAmount) invoiceAmount, uuid " +
				" from orderinvoice where delFlag = 0 and uuid in (" + ids + ") group by uuid";
		List<Map<String, Object>> list = orderinvoiceDao.findBySql(sqlString, Map.class);
		for (Map<String, Object> m : list) {
			Object invoiceSubject = m.get("invoiceSubject");
			Object invoiceType = m.get("invoiceType");
			Object invoiceMode = m.get("invoiceMode");
			String invoiceSubjectLabel = "";
			String invoiceTypeLabel = "";
			String invoiceModeLabel = "";
			if(invoiceSubject != null){
				invoiceSubjectLabel = DictUtils.getDictLabel(invoiceSubject.toString(), "invoice_subject", invoiceSubject.toString());
			}
			if(invoiceSubject != null){
				invoiceTypeLabel = DictUtils.getDictLabel(invoiceType.toString(), "invoice_type", invoiceType.toString());
			}
			if(invoiceSubject != null){
				invoiceModeLabel = DictUtils.getDictLabel(invoiceMode.toString(), "invoice_mode", invoiceMode.toString());
			}
			m.put("invoiceSubjectLabel", invoiceSubjectLabel);
			m.put("invoiceTypeLabel", invoiceTypeLabel);
			m.put("invoiceModeLabel", invoiceModeLabel);
		}
		return list;
	}
	
	/**
	 * add by chy 2016年1月22日15:06:28 根据uuid查询已开票的数据
	 */
	public List<Orderinvoice> findByInvoiceNumAndCreateStatus(String uuid,Integer createStatus){
		return orderinvoiceDao.findByInvoiceNumAndCreateStatus(uuid, createStatus);
	}
	
	/**
	 * 根据参数map中的查询条件，来查询数据库，结果返回到List里。
	 * @param mapRequest
	 * @return
	 * createdBy yudong.xu --2016年3月28日--上午9:28:00
	 */
	public List<Map<Object,Object>> getSupplyinvoiceListForExcel(Map<String,String> mapRequest,Long company) {
			
		String invoiceNum = mapRequest.get("invoiceNum");//发票号 
		String invoiceType = mapRequest.get("invoiceType");//开票类型 
		String selectVerifyStatus = mapRequest.get("selectVerifyStatus");//审核状态 
		String invoiceSubject = mapRequest.get("invoiceSubject");//开票项目 
		String invoiceTimeBegin = mapRequest.get("invoiceTimeBegin");//开票日期开始 
        String invoiceTimeEnd = mapRequest.get("invoiceTimeEnd");    //开票日期结束 
        String invoiceHead = mapRequest.get("invoiceHead");//发票台头 
		String createName = mapRequest.get("createName");//申请人 
		String invoiceCustomer = mapRequest.get("invoiceCustomer");//开票客户 
        String applyInvoiceBegin = mapRequest.get("applyInvoiceBegin");//申请日期开始
        String applyInvoiceEnd = mapRequest.get("applyInvoiceEnd");    //申请日期结束 
        String invoiceMoneyBegin = mapRequest.get("invoiceMoneyBegin");//发票金额开始 
        String invoiceMoneyEnd = mapRequest.get("invoiceMoneyEnd");//发票金额结束 
        String orderNum = mapRequest.get("orderNum");//订单号 
		String groupCode = mapRequest.get("groupCode");//团号 
		String makeOrderBegin = mapRequest.get("orderTimeBegin");//下单日期开始 1
        String makeOrderEnd = mapRequest.get("orderTimeEnd");//下单日期结束 1
        String createStatus = mapRequest.get("createStatus");//开票状态 
        String verifyStatus = mapRequest.get("verifyStatus");//审核状态 
        //开票方式查询条件20150728
        String invoiceMode = mapRequest.get("invoiceMode");
        String invoiceComingUnit = mapRequest.get("invoiceComingUnit");  //来款单位
        // 0444需求 申请方式
        String applyInvoiceWay = mapRequest.get("applyInvoiceWay");
        //
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT a.createDate, a.createName, a.groupCode, a.orderNum, a.invoiceSubject, a.invoiceCustomer, FORMAT(b.invoiceAmount, 2) invoiceAmount,")
           .append("a.invoiceNum, a.verifyStatus, a.createStatus, a.receiveStatus, a.remarks, updateDate  FROM (SELECT DISTINCT o.createDate, o.uuid, o.updateDate, ")
           .append("(select name from sys_user su where su.id = o.createBy and su.delFlag='0') as createName, o.groupCode, o.orderNum, o.invoiceSubject,")
           .append("o.invoiceCustomer, o.createStatus,o.invoiceNum, o.verifyStatus, o.receiveStatus, o.remarks, o.updateBy FROM orderinvoice o WHERE o.delFlag = '0'")
           .append("and o.invoiceCompany = ? ");

		   if(StringUtils.isNotBlank(invoiceMode)){
	    	   sql.append(" and o.invoiceMode = ").append(invoiceMode);
	       }
		 //0414 新增来款单位 update by xianglei.dong
		   if(StringUtils.isNotBlank(invoiceComingUnit)){
			   sql.append(" and o.invoiceComingUnit like '%").append(invoiceComingUnit).append("%'");
		   }		   
		   if(StringUtils.isNotBlank(applyInvoiceWay)){
			   sql.append(" and o.applyInvoiceWay = ").append(applyInvoiceWay);
		   }		   
	       if(StringUtils.isNotBlank(orderNum)){
	    	   sql.append(" and o.orderNum like '%").append(orderNum).append("%'");
	       }
	       if(StringUtils.isNotBlank(groupCode)){
	    	   sql.append(" and o.groupCode like '%").append(groupCode).append("%'");
	       }
	       if(StringUtils.isNotBlank(invoiceNum)){
	    	   sql.append(" and o.invoiceNum like '%").append(invoiceNum).append("%'");
	    	   sql.append(" and o.invoiceNum not like 'TTS%'");//不显示虚拟发票号
	       }
	       if(StringUtils.isNotBlank(invoiceType)){
	    	   sql.append(" and o.invoiceType = ").append(invoiceType);
	       }
	       if(StringUtils.isNotBlank(createStatus)){
	    	   sql.append(" and o.createStatus = ").append(createStatus);
	       }
	       if(StringUtils.isNotBlank(selectVerifyStatus)){
	    	   sql.append(" and o.verifyStatus = ").append(selectVerifyStatus);
	       }
	       if(!StringUtils.isBlank(verifyStatus)){
	    	   if("ne0".equals(verifyStatus)){
	    		   sql.append(" and o.verifyStatus in (1,2)");
	    	   }else{
	    		   sql.append(" and o.verifyStatus = ").append(verifyStatus);
	    	   }
	       }
	       if(StringUtils.isNotBlank(invoiceSubject)){
	    	   sql.append(" and o.invoiceSubject = ").append(invoiceSubject);
	       }
	       if(StringUtils.isNotBlank(invoiceTimeBegin)){
	    	   sql.append(" and o.updateDate  between '").append(invoiceTimeBegin).append(" 00:00:00' and ");
	    	   if(StringUtils.isNotBlank(invoiceTimeEnd)){
	    		   sql.append("'").append(invoiceTimeEnd).append(" 23:59:59'");
	    	   }else{
	    		   sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
	    	   }
	    	   sql.append(" and o.createStatus = 1 ");
	       }else{
	    	   sql.append(" and o.updateDate  between '").append(Context.DEFAULT_BEGIN_TIME).append("' and ");
	    	   if(StringUtils.isNotBlank(invoiceTimeEnd)){
	    		   sql.append("'").append(invoiceTimeEnd).append(" 23:59:59'")
		    		  .append(" and o.createStatus = 1 ");
	    	   }else{
	    		   sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
	    	   }
	       }
	       
		   if(StringUtils.isNotBlank(invoiceHead)){
			   sql.append(" and o.invoiceHead like '%").append(invoiceHead).append("%'");
		   }
		   if(StringUtils.isNotBlank(invoiceCustomer)){
			   sql.append(" and o.invoiceCustomer like '%").append(invoiceCustomer).append("%'");
		   }
		   if(StringUtils.isNotBlank(applyInvoiceBegin)){
	    	   sql.append(" and o.createDate  between '").append(applyInvoiceBegin).append(" 00:00:00' and ");
	    	   if(StringUtils.isNotBlank(applyInvoiceEnd)){
	    		   sql.append("'").append(applyInvoiceEnd).append(" 23:59:59'");
	    	   }else{
	    		   sql.append("'").append(Context.DEFAULT_END_TIME).append("'");;
	    	   }
	       }else{
	    	   sql.append(" and o.createDate  between '").append(Context.DEFAULT_BEGIN_TIME).append("' and ");
	    	   if(StringUtils.isNotBlank(applyInvoiceEnd)){
	    		   sql.append("'").append(applyInvoiceEnd).append(" 23:59:59'");
	    	   }else{
	    		   sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
	    	   }
	       }
		   if(StringUtils.isNotBlank(invoiceMoneyBegin)){
	    	   sql.append(" and o.invoiceAmount  between ").append(invoiceMoneyBegin).append(" and ");
	    	   if(StringUtils.isNotBlank(invoiceMoneyEnd)){
	    		   sql.append(invoiceMoneyEnd);
	    	   }else{
	    		   sql.append(Double.MAX_VALUE);
	    	   }
	       }else{
	    	   sql.append(" and o.invoiceAmount  between ").append(-Double.MAX_VALUE).append(" and ");
	    	   if(StringUtils.isNotBlank(invoiceMoneyEnd)){
	    		   sql.append(invoiceMoneyEnd);
	    	   }else{
	    		   sql.append(Integer.MAX_VALUE);
	    	   }
	       }
		//下单时间条件，拼接exists 关键字开始。
		if(StringUtils.isNotBlank(makeOrderBegin) || StringUtils.isNotBlank(makeOrderEnd)){
			sql.append(" and exists (SELECT  'x' FROM (")
			   .append(" SELECT po.orderNum,po.createDate,po.orderStatus AS orderType")
			   .append(" FROM productorder po WHERE po.delFlag = '0' ")
			   .append(" UNION SELECT ao.order_no AS orderNum,ao.create_date AS createDate,7 AS orderType")
			   .append(" FROM airticket_order ao WHERE ao.del_flag = '0' ")
			   .append(" UNION SELECT vo.order_no AS orderNum,vo.create_date AS createDate,6 AS orderType")
			   .append(" FROM visa_order vo WHERE vo.del_flag = '0') t1")
			   .append(" where t1.orderNum = o.orderNum and t1.orderType = o.orderType ");
			   		
			   if(StringUtils.isNotBlank(makeOrderBegin)){
		    	   sql.append(" and t1.createDate  between '").append(makeOrderBegin).append(" 00:00:00' and ");
		    	   if(StringUtils.isNotBlank(makeOrderEnd)){
		    		   sql.append("'").append(makeOrderEnd).append(" 23:59:59'");
		    	   }else{
		    		   sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
		    	   }
		       }else{
		    	   sql.append(" and t1.createDate  between '").append(Context.DEFAULT_BEGIN_TIME).append("' and ");
		    	   if(StringUtils.isNotBlank(makeOrderEnd)){
		    		   sql.append("'").append(makeOrderEnd).append(" 23:59:59'");
		    	   }else{
		    		   sql.append("'").append(Context.DEFAULT_END_TIME).append("'");
		    	   }
		       }
			sql.append(")");
		}
		//下单时间条件，拼接exists 关键字结束。
		
		sql.append(" group by o.uuid) a,(SELECT uuid,invoiceNum, sum(invoiceAmount) AS invoiceAmount FROM orderinvoice where invoiceCompany =")
		   .append(company).append(" GROUP BY uuid) b,(SELECT uuid,invoiceNum, min(createDate) AS createDate FROM orderinvoice where invoiceCompany =")
		   .append(company).append(" GROUP BY uuid) d WHERE a.uuid = b.uuid AND a.uuid = d.uuid");
		
	    if(StringUtils.isNotBlank(createName)){
			sql.append(" and createName like '%").append(createName).append("%'");
	    }
	    
			sql.append(" order by createDate desc");
		
		return orderinvoiceDao.findBySql(sql.toString(),Map.class,company);
	}
	
	/**
	 * 关联发票成功，改变发票回款状态
	 * @param invoiceIds
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Object changeInvoiceReceivedPayStatus(String invoiceIds){
		Map<Object, Object> map = new HashMap<>();
		if(StringUtils.isNoneBlank(invoiceIds)){
			String[] ids = invoiceIds.split(",");
			for (String id : ids) {
				Orderinvoice invoice = this.findOrderinvoiceById(Long.parseLong(id));
				invoice.setReceivedPayStatus(1);
				this.saveOrderInvoice(invoice);
			}	
			map.put("flag", "success");
		}
		return map;
	}
}
