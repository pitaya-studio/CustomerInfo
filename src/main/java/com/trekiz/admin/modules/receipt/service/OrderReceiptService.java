package com.trekiz.admin.modules.receipt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.receipt.entity.OrderReceipt;
import com.trekiz.admin.modules.receipt.repository.OrderReceiptDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class OrderReceiptService {

	@Autowired
	private OrderReceiptDao orderReceiptDao;

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void saveOrderReceipt(OrderReceipt invoice) {

		orderReceiptDao.save(invoice);
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
	public List<OrderReceipt> findApplyOrderReceipt(Integer orderid, String audited) {
		return orderReceiptDao.findApplyOrderReceipt(orderid, StringUtils.toInteger(audited));
	}

	public OrderReceipt findOrderReceiptById(Long id) {
		return orderReceiptDao.findOne(id);
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
       
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.invoiceNum,a.invoiceMode,a.invoiceType,a.invoiceSubject,")
	       .append(" a.invoiceCustomer,d.createDate,c.updateDate,a.createName,FORMAT(b.invoiceAmount, 2) invoiceAmount,")
	       .append(" a.verifyStatus,a.updateName,a.remarks,a.createStatus,a.receiveStatus,a.orderType,a.uuid FROM (SELECT DISTINCT ")//新加了uuid查询结果 by chy 2015年7月21日20:32:15
	       .append(" o.invoiceNum,o.invoiceMode,o.invoiceType,o.invoiceSubject,o.invoiceCustomer,")
	       .append(" (select name from sys_user su where su.id = o.createBy and su.delFlag='0') as createName,")
	       .append(" (select name from sys_user su where su.id = o.updateBy and su.delFlag='0') as updateName,")
	       .append(" o.verifyStatus,o.updateBy,o.remarks,o.createStatus,o.receiveStatus,o.orderType,max(o.updateDate) as disflag,o.uuid ")//新加了uuid查询结果 by chy 2015年7月21日20:32:15
	       .append("FROM orderreceipt o ")
	       .append("WHERE o.delFlag = '0' and o.invoiceCompany = ? ");
		
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
		}else{
			sql.append(" and o.updateDate  between '").append(Context.DEFAULT_BEGIN_TIME).append("' and ");
			if(StringUtils.isNotBlank(invoiceTimeEnd)){
				sql.append("'").append(invoiceTimeEnd).append(" 23:59:59'");
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
		
		if(StringUtils.isNotBlank(makeOrderBegin) || StringUtils.isNotBlank(makeOrderEnd)){
			sql.append(" and exists (SELECT DISTINCT orderNum,createDate,orderType FROM (")
			   .append(" SELECT orderNum,oo.createDate,oo.orderStatus AS orderType FROM")
			   .append(" productorder oo WHERE delFlag = '0' ")
			   .append(" UNION SELECT order_no AS orderNum,oo.create_date AS createDate,7 AS orderType FROM")
			   .append(" airticket_order oo WHERE oo.del_flag = '0' ")
			   .append(" UNION SELECT order_no AS orderNum,oo.create_date AS createDate,6 AS orderType FROM")
			   .append(" visa_order oo WHERE oo.del_flag = '0') t1 where t1.orderNum = o.orderNum and t1.orderType = o.orderType ");
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
		
	    sql.append(" group by o.uuid) a,(SELECT invoiceNum, uuid,sum(invoiceAmount) AS invoiceAmount FROM orderreceipt ")
	       .append(" where invoiceCompany = ").append(company).append(" GROUP BY uuid) b,")
	       .append("(SELECT invoiceNum, uuid,max(updateDate) AS updateDate FROM orderreceipt ")
	       .append(" where invoiceCompany = ").append(company).append(" GROUP BY uuid) c,")
	       .append("(SELECT invoiceNum,uuid, min(createDate) AS createDate FROM orderreceipt ")
	       .append(" where invoiceCompany = ").append(company).append(" GROUP BY uuid) d ")
	       .append(" WHERE a.uuid = b.uuid AND a.uuid = c.uuid")
	       .append(" AND a.uuid = d.uuid ");
	    if(StringUtils.isNotBlank(createName)){
			sql.append(" and createName like '%").append(createName).append("%'");
	    }
	    if(StringUtils.isNotBlank(invoiceMoneyBegin)){
	    	sql.append(" and invoiceAmount  between ").append(invoiceMoneyBegin).append(" and ");
	    	if(StringUtils.isNotBlank(invoiceMoneyEnd)){
	    		sql.append(invoiceMoneyEnd);
	    	}else{
	    		sql.append(Double.MAX_VALUE);
	    	}
	    }else{
	    	sql.append(" and invoiceAmount  between 0 and ");
	    	if(StringUtils.isNotBlank(invoiceMoneyEnd)){
	    		sql.append(invoiceMoneyEnd);
	    	}else{
	    		sql.append(Double.MAX_VALUE);
	    	}
	    }
	    if(StringUtils.isBlank(page.getOrderBy())){
	    	sql.append(" order by createDate desc");
	    }
		return orderReceiptDao.findBySql(page, sql.toString(), Map.class, company);
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
	public List<Map<Object, Object>> getSupplyinvoiceLists(Page<Map<Object, Object>> page, String verifyStatus,
			Map<String,String> mapRequest, Long company, TravelActivity travelActivity) {
		
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
        String orderNum = mapRequest.get("orderNum");//订单号 
		String groupCode = mapRequest.get("groupCode");//团号 
		String makeOrderBegin = mapRequest.get("orderTimeBegin");//下单日期开始 1
        String makeOrderEnd = mapRequest.get("orderTimeEnd");//下单日期结束 1
        String createStatus = mapRequest.get("createStatus");//开票状态 
		
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT a.invoiceNum,a.invoiceMode,a.invoiceType,a.invoiceSubject,")
	       .append("a.invoiceCustomer,d.createDate,c.updateDate,a.createName,FORMAT(b.invoiceAmount, 2) invoiceAmount,")
	       .append("a.verifyStatus,a.updateName,a.remarks,a.createStatus,a.receiveStatus,a.orderType,a.uuid FROM (SELECT DISTINCT ")//新加了uuid查询结果 by chy 2015年7月21日20:32:15
	       .append("o.invoiceNum,o.invoiceMode,o.invoiceType,o.invoiceSubject,o.invoiceCustomer,")
	       .append("(select name from sys_user su where su.id = o.createBy and su.delFlag='0') as createName,")
	       .append("(select name from sys_user su where su.id = o.updateBy and su.delFlag='0') as updateName,")
	       .append("o.verifyStatus,o.updateBy,o.remarks,o.createStatus,o.receiveStatus,o.orderType,o.uuid FROM orderreceipt o")//新加了uuid查询结果 by chy 2015年7月21日20:32:15
	       .append(" WHERE o.delFlag = '0' and o.invoiceCompany = ? ");
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
        }else{
        	sql.append(" and o.updateDate  between '").append(Context.DEFAULT_BEGIN_TIME).append("' and ");
        	if(StringUtils.isNotBlank(invoiceTimeEnd)){
        		sql.append("'").append(invoiceTimeEnd).append(" 23:59:59'");
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
        sql.append(" and o.invoiceAmount  between ").append(0).append(" and 10000000000 ");

        if(StringUtils.isNotBlank(makeOrderBegin) || StringUtils.isNotBlank(makeOrderEnd)){
        	sql.append(" and exists (SELECT DISTINCT orderNum,createDate,orderType FROM (")
			   .append(" SELECT orderNum,oo.createDate,oo.orderStatus AS orderType FROM")
			   .append(" productorder oo WHERE delFlag = '0' ")
			   .append(" UNION SELECT order_no AS orderNum,oo.create_date AS createDate,7 AS orderType FROM")
			   .append(" airticket_order oo WHERE oo.del_flag = '0' ")
			   .append(" UNION SELECT order_no AS orderNum,oo.create_date AS createDate,6 AS orderType FROM")
			   .append(" visa_order oo WHERE oo.del_flag = '0') t1 where t1.orderNum = o.orderNum and t1.orderType = o.orderType ");
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
	    sql.append(" ) a,(SELECT invoiceNum, sum(invoiceAmount) AS invoiceAmount FROM orderreceipt ")
	       .append(" where invoiceCompany = ").append(company).append(" GROUP BY invoiceNum) b,")
	       .append("(SELECT invoiceNum, max(updateDate) AS updateDate FROM orderreceipt ")
	       .append(" where invoiceCompany = ").append(company).append(" GROUP BY invoiceNum) c,")
	       .append("(SELECT invoiceNum, min(createDate) AS createDate FROM orderreceipt ")
	       .append(" where invoiceCompany = ").append(company).append(" GROUP BY invoiceNum) d ")
	       .append(" WHERE a.invoiceNum = b.invoiceNum AND a.invoiceNum = c.invoiceNum")
	       .append(" AND a.invoiceNum = d.invoiceNum ");
	    if(StringUtils.isNotBlank(createName)){
			sql.append(" and createName like '%").append(createName).append("%'");
	    }
	    if(StringUtils.isBlank(page.getOrderBy())){
			sql.append(" order by createDate desc");
		}
		return orderReceiptDao.findBySql(sql.toString(), Map.class, company);
	}

	
	public boolean invoiceNum(String inoviceNum, Long invoiceCompany) {
		return orderReceiptDao.findByInvoiceNumAndInvoiceCompany(inoviceNum,
				invoiceCompany).size() == 0;
	}
	
	/**
	 * update by ruyi.chen
	 * 2015-01-16
	 * 添加达帐金额查询字段
	* @Title: findSupplyLimitDetailList 
	* @Description: TODO(发票相关订单详情)
	* @param @param invoiceNum
	* @param @param company
	* @return List<Object[]>    返回类型 
	* @throws
	 */
	public List<String[]> findSupplyLimitDetailList(String uuid, Integer orderType, Long company) {
		StringBuffer sqlStringBuffer = new StringBuffer();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		
		// 机票
		sqlStringBuffer.append("select invoice.orderNum,ao.group_code groupCode,invoice.invoiceCustomer,sysuser.NAME AS createName,")
					   .append("date_format(ao.create_date,'%Y-%m-%d') as orderTime,ao.person_num orderPersonNum,CURDATE() groupOpenDate,")
					   .append("CURDATE() groupCloseDate,ao.total_money AS totalMoney,ao.payed_money,b.invoiceAmount,invoice.invoiceAmount amount, ")
					   .append("invoice.orderType,ao.accounted_money ")
					   .append("FROM sys_user sysuser,orderreceipt invoice,airticket_order ao,(select orderNum,sum(case createstatus when 0 then 0 when 1 then invoiceamount when 2 then invoiceamount end ) invoiceamount from ")
					   .append("orderreceipt group by orderNum) b where invoice.orderType = ao.product_type_id and invoice.orderId = ao.id and ao.create_by = sysuser.id and ")
					   .append("invoice.uuid = '" + uuid + "' and invoice.invoiceCompany = " + company + " AND invoice.orderNum = b.orderNum ");//ORDER BY ao.create_date
		
		// 签证
		//C460V3 签证取签证产品中的虚拟团号  modify by wangyang 2016.4.15
		sqlStringBuffer.append(" UNION SELECT invoice.orderNum,");
		if(Context.SUPPLIER_UUID_HQX.equals(companyUuid)){
			sqlStringBuffer.append(" vo.group_code groupCode, ");
		}else{
			sqlStringBuffer.append(" vp.groupCode, ");
		}
		sqlStringBuffer.append(" invoice.invoiceCustomer,sysuser.NAME AS createName, ")
		               .append(" date_format(vo.create_date,'%Y-%m-%d') as orderTime,vo.travel_num orderPersonNum,CURDATE() groupOpenDate, ")
		               .append(" CURDATE() groupCloseDate,vo.total_money AS totalMoney,vo.payed_money,b.invoiceAmount, ")
		               .append(" invoice.invoiceAmount amount,invoice.orderType,vo.accounted_money ")
		               .append(" FROM sys_user sysuser,orderreceipt invoice,visa_order vo,visa_products vp, ")
		               .append(" (select orderNum,sum(case createstatus when 0 then 0 when 1 then invoiceamount when 2 then invoiceamount end ) ")
		               .append(" invoiceamount FROM orderreceipt group by orderNum) b ")
		               .append(" WHERE invoice.orderType = vo.product_type_id and invoice.orderId = vo.id and vo.create_by = sysuser.id and ")
		               .append(" invoice.uuid = '" + uuid + "' and invoice.invoiceCompany = " + company + " AND invoice.orderNum = b.orderNum ")
		               .append(" AND vp.id = vo.visa_product_id ");
		
		// 单团
		sqlStringBuffer.append(" UNION SELECT invoice.orderNum,invoice.groupCode,invoice.invoiceCustomer,sysuser.NAME AS createName,")
					   .append("date_format(porder.orderTime,'%Y-%m-%d') as orderTime,porder.orderPersonNum,groups.groupOpenDate,groups.groupCloseDate,porder.total_money AS totalMoney,")
					   .append("porder.payed_money,b.invoiceAmount,invoice.invoiceAmount amount,invoice.orderType,porder.accounted_money from sys_user sysuser,orderreceipt invoice,productorder porder,")
					   .append("travelactivity activity,activitygroup groups,(select orderNum,sum(case createstatus when 0 then 0 when 1 then invoiceamount when 2 then invoiceamount end ) invoiceamount from ")
					   .append("orderreceipt group by orderNum) b where invoice.orderType = porder.orderStatus and invoice.orderId = porder.id ")
					   .append("and porder.createBy = sysuser.id and activity.id = groups.srcActivityId and ")
					   .append("porder.productGroupId = groups.id and invoice.uuid = '" + uuid + "'")
					   .append(" and activity.proCompany = " + company + " and invoice.orderNum = b.orderNum and invoice.invoiceCompany = " + company + " ");//order by porder.orderTime 
		
		return orderReceiptDao.findBySql(sqlStringBuffer.toString());
	}

	/**
	 * 
	* @Title: findSupplyLimitDetailList 
	* @Description: TODO(发票相关订单详情)
	* @param @param orderinvoceList
	* @return List<Object[]>    返回类型 
	* @throws
	 */
	public List<String[]> findSupplyLimitDetailList(List<OrderReceipt> orderinvoceList, String currencyId,
			String orderMoneyBegin, String orderMoneyEnd, String operator) {
		StringBuffer sql = new StringBuffer();
		OrderReceipt orderinvoice = orderinvoceList.get(0);
		String ordertypes = "";
		//遍历取出所有的产品类型（当前收据包含的产品类型）
		int n = 0;
		for(OrderReceipt temp : orderinvoceList){
			if(n == 0){
				ordertypes += temp.getOrderType();
				n++;
			} else {
				ordertypes += "," + temp.getOrderType();
			}
		}
		StringBuffer str = new StringBuffer(); 
		str.append(" and exists ( select 1 from money_amount ma where ma.businessType = 1 and ma.orderType in(")
		   .append(ordertypes).append(")");
		boolean b = false;
		int argsOperator = -1;
		if (StringUtils.isNotBlank(currencyId)) {
			b = true;
			str.append(" and ma.currencyId=").append(currencyId);
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
		sql.append("select t1.orderNum,t1.groupCode,t1.invoiceCustomer,t1.createName,t1.orderTime,t1.orderPersonNum,t1.groupOpenDate,")
		   .append("t1.groupCloseDate,t2.payed_money,t3.accounted_money,t1.invoiceAmount,t1.amount,t1.orderType,t1.id,t1.total_money,")
		   .append("t1.operatorName from (select 6 as orderType,vo.id,vo.total_money,invoice.orderNum,");
		//C460V3plus 环球行签证团号取订单团号  wangyang  2016.5.6
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
			sql.append(" vo.group_code AS groupCode, ");
		}else{
			sql.append(" tt.groupCode, ");
		}
		sql.append("invoice.invoiceCustomer,sysuser.NAME AS createName,date_format(vo.create_date,'%Y-%m-%d') orderTime,")
		   .append("vo.travel_num orderPersonNum,ag.groupOpenDate,ag.groupCloseDate,vo.payed_money payed_money,vo.accounted_money accounted_money,")
		   .append("FORMAT(b.invoiceAmount,2) invoiceAmount,FORMAT(invoice.invoiceAmount,2) amount,tt.name as operatorName FROM visa_order vo ")
		   .append(" LEFT JOIN orderreceipt invoice on invoice.orderId = vo.id and invoice.orderType = vo.product_type_id ")
		   .append(" LEFT JOIN sys_user sysuser on vo.create_by = sysuser.id ")
		   .append(" LEFT JOIN productorder po ON po.id = vo.product_type_id ")
		   .append(" LEFT JOIN activitygroup ag ON ag.id = po.productGroupId ")
		   .append(" LEFT JOIN (select uu.name,uu.id as userId,vp.id,vp.proCompanyId,vp.groupCode ")
		   .append(" FROM visa_products vp left join sys_user uu on vp.createBy = uu.id ) tt on tt.id = vo.visa_product_id, ")
		   .append(" (select orderNum,sum(case createstatus when 0 then 0 when 1 then invoiceamount end ) invoiceamount ")
		   .append(" FROM orderreceipt GROUP BY orderNum) b ")
		   .append(" WHERE invoice.orderNum = b.orderNum ");
		
		if(b){
			sql.append(str.toString()).append(" and ma.serialNum = vo.total_money )");
		}
		if(-1 != argsOperator){//添加计调条件
			sql.append(" and tt.userId = ").append(argsOperator);
		}
		Long invoiceCompany = orderinvoice.getInvoiceCompany();
		sql.append(" and invoice.uuid =  '" + orderinvoice.getUuid() + "' and tt.proCompanyId = "+ invoiceCompany +" order by vo.create_date) t1 ")
		   .append(" LEFT JOIN (select ma.serialNum, group_concat(concat(c.currency_name,ma.amount) separator '+') payed_money ")
		   .append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType =  " + Context.MONEY_TYPE_YS)
		   .append(" and ma.orderType = " + Context.ORDER_TYPE_QZ + " and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t2 on t1.payed_money = t2.serialNum ")
		   .append("left join (select ma.serialNum, group_concat(concat(c.currency_name,ma.amount) separator '+') accounted_money ")
		   .append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType =  " + Context.MONEY_TYPE_DZ)
		   .append(" and ma.orderType = " + Context.ORDER_TYPE_QZ + " and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t3 on t1.accounted_money = t3.serialNum");

		//机票
		sql.append(" union select t1.orderNum,t1.groupCode,t1.invoiceCustomer,t1.createName,t1.orderTime,t1.orderPersonNum,t1.groupOpenDate,")
		   .append("t1.groupCloseDate,t2.payed_money,t3.accounted_money,t1.invoiceAmount,t1.amount,t1.orderType,t1.id,t1.total_money,")
		   .append(" t1.operatorName from (select 7 as orderType,ao.id,ao.total_money,invoice.orderNum,")
		   .append("invoice.groupCode,invoice.invoiceCustomer,sysuser. NAME AS createName,date_format(ao.create_date,'%Y-%m-%d') orderTime, ")
		   .append("ag.groupCloseDate,ao.payed_money payed_money,ao.accounted_money accounted_money,FORMAT(b.invoiceAmount,2) invoiceAmount,")
		   .append("FORMAT(invoice.invoiceAmount,2) amount ,tt.name as operatorName,ao.person_num orderPersonNum,ag.groupOpenDate ")
		   .append(" FROM airticket_order ao LEFT JOIN orderreceipt invoice on invoice.orderId = ao.id and invoice.orderType = ao.product_type_id ")
		   .append(" LEFT JOIN sys_user sysuser on ao.create_by = sysuser.id ")
		   .append(" left join (select uu.name,uu.id as userId,aa.id,aa.proCompany from activity_airticket aa ")
		   .append(" left join sys_user uu on aa.createBy = uu.id ) tt on ao.airticket_id  = tt.id ")
		   .append(" LEFT JOIN activitygroup ag on ao.activitygroup_id = ag.id,(select orderNum,")
		   .append(" sum(case createstatus when 0 then 0 when 1 then invoiceamount end ) invoiceamount from orderreceipt ")
		   .append(" GROUP BY orderNum) b where invoice.orderNum = b.orderNum ");
		if(b){
			sql.append(str.toString()).append(" and ma.serialNum = ao.total_money )");
		}
		if(-1 != argsOperator){//添加计调条件
			sql.append(" and tt.userId = ").append(argsOperator);
		}
		sql.append(" and invoice.uuid =  '" + orderinvoice.getUuid() + "' and tt.proCompany = " + invoiceCompany + " ORDER BY ao.create_date) t1 ")
		   .append(" LEFT JOIN (select ma.serialNum, group_concat(concat(c.currency_name,ma.amount) separator '+') payed_money ")
		   .append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_YS)
		   .append(" and ma.orderType = " + Context.ORDER_TYPE_JP + " and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t2 on t1.payed_money = t2.serialNum ")
		   .append("left join (select ma.serialNum, group_concat(concat(c.currency_name,ma.amount) separator '+') accounted_money ")
		   .append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_DZ)
		   .append(" and ma.orderType = " + Context.ORDER_TYPE_JP + " and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t3 on t1.accounted_money = t3.serialNum");

		// 单团
		sql.append(" union select t1.orderNum,t1.groupCode,t1.invoiceCustomer,t1.createName,t1.orderTime,t1.orderPersonNum,t1.groupOpenDate,")
		   .append("t1.groupCloseDate,t2.payed_money,t3.accounted_money,t1.invoiceAmount,t1.amount,t1.orderStatus as orderType,t1.id,t1.total_money,t1.operatorName from (")
		   .append("select porder.orderStatus,porder.id,porder.total_money,invoice.orderNum,invoice.groupCode,invoice.invoiceCustomer,sysuser.NAME AS createName,")
		   .append("date_format(porder.orderTime,'%Y-%m-%d') as orderTime,porder.orderPersonNum,tt.groupOpenDate,tt.groupCloseDate,porder.accounted_money,")
		   .append("porder.payed_money,FORMAT(b.invoiceAmount,2) invoiceAmount,FORMAT(invoice.invoiceAmount,2) amount,invoice.orderType,tt.name as operatorName ")
		   .append(" from sys_user sysuser,orderreceipt invoice,productorder porder,")
		   .append("travelactivity activity,(select ag.id,uu.name,uu.id as userId,ag.groupOpenDate,")
		   .append("ag.groupCloseDate,ag.srcActivityId from activitygroup ag left join sys_user uu on ag.createBy = uu.id ) tt,")
		   .append(" (select orderNum,sum(case createstatus when 0 then 0 when 1 then invoiceamount end ) invoiceamount from ")
		   .append("orderreceipt group by orderNum) b where invoice.orderId = porder.id  and invoice.orderType = porder.orderStatus and porder.createBy = sysuser.id")
		   .append(" and activity.id = tt.srcActivityId and porder.productGroupId = tt.id ");
		if(b){
			sql.append(str.toString()).append(" and ma.serialNum = porder.total_money )");
		}
		if(-1 != argsOperator){//添加计调条件
			sql.append(" and tt.userId = ").append(argsOperator);
		}
		sql.append(" and invoice.uuid = '" + orderinvoice.getUuid() + "' and activity.proCompany = "+ invoiceCompany +" and invoice.orderNum = b.orderNum order by porder.orderTime ) t1 ")
		   .append("left join (select ma.serialNum, group_concat(concat(c.currency_name,ma.amount) separator '+') payed_money ")
		   .append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_YS)
		   .append(" and ma.orderType in(1,2,3,4,5,10) and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t2 on t1.payed_money = t2.serialNum ")
		   .append("left join (select ma.serialNum, group_concat(concat(c.currency_name,ma.amount) separator '+') accounted_money ")
		   .append("from money_amount ma left join currency c on ma.currencyId = c.currency_id where ma.moneyType = " + Context.MONEY_TYPE_DZ)
		   .append(" and ma.orderType in(1,2,3,4,5,10) and ma.businessType = 1 and c.create_company_id = " + invoiceCompany + " group by ma.serialNum) t3 on t1.accounted_money = t3.serialNum ");
		return orderReceiptDao.findBySql(sql.toString());
	}
	
	/**
	 * 根据订单id 和订单类型查询单个订单的已开收据（新）
	 * @author yang.jiang 2016-2-25 20:53:36
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<OrderReceipt> findCreatedReceiptByOrder(Integer orderId,Integer orderType){
		return orderReceiptDao.findCreatedReceiptByOrder(orderId, orderType);
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
	public List<OrderReceipt> findInvoiceDetails(String uuid,Long invoiceCompany){
		return orderReceiptDao.findByUuidAndInvoiceCompany(uuid, invoiceCompany);
	}
	/**
	 * 
	* @Title: updateOrderinvoice 
	* @Description: TODO(更新审核状态) 
	* @param @param invoiceNum    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void updateOrderinvoice(String invoiceNum,Integer verifyStatus){
		orderReceiptDao.update("update OrderReceipt set verifyStatus = ? where uuid = ?", verifyStatus,invoiceNum);
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
	public void updateOrderinvoiceCreateStatus(String uuid,Integer createStatus) {
		orderReceiptDao.update("update OrderReceipt set createStatus = ? where uuid = ?", createStatus,uuid);
	}
	public void updateOrderinvoiceCreateStatus(String uuid,String invoiceNumber,Integer createStatus) {
		orderReceiptDao.update("update OrderReceipt set createStatus = ?,invoiceNum = ?,updateDate = ? where uuid = ? and invoiceCompany  = ?", createStatus,invoiceNumber,new Date(),uuid, UserUtils.getUser().getCompany().getId());
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
	public void updateOrderinvoiceReceiveStatus(String uuid,Integer receiveStatus) {
		orderReceiptDao.update("update OrderReceipt set receiveStatus = ? where uuid = ?", receiveStatus,uuid);
	}

	/**
	 * 获取所有订单的退款数据
	 * @param orderId 订单id
	 * @param orderType 订单类型
	 * @return
	 * */
	public List<Map<String,Object>> getOrderAllRefund(String orderId, String orderType) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT A.orderId,group_concat(format(IFNULL(A.totalMoney,0),2),A.currency_name ORDER BY A.currencyId separator '+')as refundTotalStr,")
		   .append("IFNULL(convert(SUM(A.totalMoney*A.currency_exchangerate),DECIMAL(11,2)),0) as refundTotal FROM ")
		   .append("(select sum(a.amount) as totalMoney ,a.currencyId,c.currency_name,c.currency_exchangerate,a.orderId from (")
		   .append("select mac.currencyId, IFNULL(mac.amount,0)as amount,mac.moneyType,mac.orderType,p.id as orderId from money_amount mac,");
		
		switch(orderType){
			case Context.ORDER_STATUS_VISA:
				sbf.append(" visa_order p where mac.uid = p.id AND mac.businessType=1  and mac.moneyType =11 and mac.orderType=?  and p.id=? ");
				sbf.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.orderId ");
				break;
			case Context.ORDER_STATUS_AIR_TICKET:
				sbf.append(" airticket_order p where mac.uid = p.id AND mac.businessType=1  and mac.moneyType =11 and mac.orderType=? and p.id=? ");
				sbf.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId )A  GROUP BY A.orderId ");
				break;
			default:
				sbf.append(" productorder p where mac.uid = p.id AND mac.businessType=1 and mac.moneyType=11 and mac.orderType = p.orderStatus and p.orderStatus=? and p.id=? ");
				sbf.append(")a LEFT JOIN currency c on a.currencyId=c.currency_id GROUP BY a.currencyId ) A GROUP BY A.orderId ");
				break;
		}
		
		return orderReceiptDao.findBySql(sbf.toString(), Map.class, orderType, orderId);
	}
	
	/**
	 * 开票页面收据撤销功能，将已开票发票改成待开票
	 * @param    发票号
	 * @return 修改记录数
	 * @author shijun.liu
	 */
	public int revokeToUninvioce(String uuid, Integer createStatus){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String hsql = "update OrderReceipt set createStatus = ? where uuid = ? and invoiceCompany = ?";
		return orderReceiptDao.update(hsql, createStatus, uuid, companyId);
	}
	
	/**
	 * 已审核页面收据撤销功能，将已审核发票改成待审核
	 * @param  发票号
	 * @return 修改记录数
	 * @author shijun.liu
	 */
	public int revokeToUncheck(String uuid){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String hsql = "update OrderReceipt set verifyStatus = 0 where uuid = ? and invoiceCompany = ?";
		return orderReceiptDao.update(hsql, uuid, companyId);
	}
	
	/**
	 * 获取待审核的收据数据数量
	 */
	public int getToBeReviewReceiptNum(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String sql = "select count(*) from orderreceipt where delFlag = 0 and invoiceCompany = " + companyId + " and verifyStatus = 0 group by uuid";
		List<Object> list = orderReceiptDao.findBySql(sql);
		if(list == null || list.size() == 0){
			return 0;
		}
		return list.size();
	}
	
	/**
	 * 获取待开票的收据数据数量
	 */
	public int getReviewedReceiptNum(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String sql = "select count(*) from orderreceipt where delFlag = 0 and invoiceCompany = " + companyId + " and createStatus = 0 and verifyStatus = 1 group by uuid";
		List<Object> list = orderReceiptDao.findBySql(sql);
		if(list == null || list.size() == 0){
			return 0;
		}
		return list.size();
	}

}
