package com.trekiz.admin.modules.cost.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.cost.entity.GroupManagerEntity;
import com.trekiz.admin.modules.cost.entity.ParamBean;
import com.trekiz.admin.modules.cost.repository.IReceivePayDao;
import com.trekiz.admin.modules.cost.service.IReceivePayService;
import com.trekiz.admin.modules.cost.utils.CostExcelUtils;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.message.entity.MsgEntity;
import com.trekiz.admin.modules.message.entity.MsgMarkEntity;
import com.trekiz.admin.modules.message.repository.MsgEntityDao;
import com.trekiz.admin.modules.message.repository.MsgUserMarkDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.statistics.utils.ExcelUtils;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Remind;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.IReviewDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.IRemindService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，应收，应付功能对应的Service实现类
 * @author shijun.liu
 * @date 2015年11月12日
 */
@Service("iReceivePayService")
public class ReceivePayServiceImpl implements IReceivePayService {

	
	private static final Logger log = Logger.getLogger(ReceivePayServiceImpl.class);
	
	@Autowired
	private IReceivePayDao receivePayDao;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private MsgEntityDao msgEntityDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private com.quauq.review.core.engine.ReviewService newReviewService;
    @Autowired
    IReviewDao iReviewDao;
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
    @Autowired
    private MsgUserMarkDao msgUserMarkDao;
    @Autowired
    private IRemindService remindService;
    @Autowired
    private com.quauq.review.core.engine.ReviewService reviewService;
    @Autowired
    private ReturnDifferenceService returnDifferenceService;
    @Autowired
    private CurrencyService currencyService;
	
	@Override
	public Page<Map<String, Object>> getReceive(Page<Map<String, Object>> page, ParamBean paramBean) {
		
		Page<Map<String, Object>> pageList = receivePayDao.getReceive(page, paramBean);
		moneyThousand(pageList.getList());
		return pageList;
	}

	@Override
	public Workbook downloadReceive(Page<Map<String, Object>> page, ParamBean paramBean) throws NullPointerException {
		page.setPageNo(0);
		page.setMaxSize(Integer.MAX_VALUE);//0069 需求  不限制导出的数据的条数  by chy 2016年1月7日14:06:42
		Page<Map<String, Object>> pageList = receivePayDao.getReceive(page, paramBean);
		if(null == pageList){
			log.error("账龄数据为空，请检查");
			throw new NullPointerException("账龄数据为空，请检查");
		}
		moneyThousand(pageList.getList());
		Workbook workbook = CostExcelUtils.createReceiveExcel(pageList.getList());
		return workbook;
	}
	
	/**
	 * 将应收账款账龄金额转化为千分位显示
	 * @param list
	 * @author shijun.liu
	 * @date 2015.11.16
	 */
	private void moneyThousand(List<Map<String, Object>> list){
		if(null == list){
			return;
		}
		int decimals = 2;	//保留两位小数
		for (Map<String, Object> map : list) {
			String totalMoney = MoneyNumberFormat.getThousandsByRegex(String.valueOf(map.get("totalMoney")), decimals);
			String accountedMoney = MoneyNumberFormat.getThousandsByRegex(String.valueOf(map.get("accountedMoney")), decimals);
			String unreceivedMoney =  MoneyNumberFormat.getThousandsByRegex(String.valueOf(map.get("unreceivedMoney")), decimals);
			String accountAge30 =  MoneyNumberFormat.getThousandsByRegex(String.valueOf(map.get("accountAge30")), decimals);
			String accountAge60 =  MoneyNumberFormat.getThousandsByRegex(String.valueOf(map.get("accountAge60")), decimals);
			String accountAge90 =  MoneyNumberFormat.getThousandsByRegex(String.valueOf(map.get("accountAge90")), decimals);
			String accountAge180 =  MoneyNumberFormat.getThousandsByRegex(String.valueOf(map.get("accountAge180")), decimals);
			String accountAge360 =  MoneyNumberFormat.getThousandsByRegex(String.valueOf(map.get("accountAge360")), decimals);
			map.put("totalMoney", totalMoney);
			map.put("accountedMoney", accountedMoney);
			map.put("unreceivedMoney", unreceivedMoney);
			map.put("accountAge30", accountAge30);
			map.put("accountAge60", accountAge60);
			map.put("accountAge90", accountAge90);
			map.put("accountAge180", accountAge180);
			map.put("accountAge360", accountAge360);
		}
	}

	@Override
	public Page<Map<Object, Object>> getPayList(Page<Map<Object, Object>> page,HttpServletRequest request,
			HttpServletResponse response, Model model) {
		// TODO Auto-generated method stub
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sql = new StringBuffer("select deptId, createBy, jd, groupCode, sum(total) total, GROUP_CONCAT(recordId) recordId, orderType, GROUP_CONCAT(payee) payee, paymentDay,SUM(payed) payed,(sum(total) -SUM(payed)) as sub from (");
        sql
        .append(genDTSql(companyId))
		   .append(" UNION ALL ")
		   .append(genJPSql(companyId))
		   .append(" UNION ALL ")
		   .append(genQZSql(companyId))
		   .append(" UNION ALL ")
		   .append(genHotelSql(companyId))
		   .append(" UNION ALL ")
		   .append(genIslandSql(companyId))
		   .append(" UNION ALL ")
		   .append(genTKsql(companyId))
		   .append(" UNION ALL ")
		   .append(genFYsql(companyId))
		   .append(" UNION ALL ")
		   .append(genJKsql())
		   .append(" ) list where 1=1 ")
		   .append(genWhereSqlForPayList(request, model)).append(" group by deptId,createBy,groupCode,orderType");
        String flag = request.getParameter("flag");
		if(StringUtils.isNotBlank(flag) && "1".equals(flag)){
			page.setMaxSize(Integer.MAX_VALUE);// 0069 需求  不限制导出条数 by chy 2016年1月7日14:08:02 
		}
		page = receivePayDao.findBySql(page, sql.toString(), Map.class);
		return page;
	}

	@Override
	public Workbook downloadPayList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		
		Page<Map<Object,Object>> page = getPayList(new Page<Map<Object,Object>>(request, response), request, response, model);
		// 创建新的Excel 工作簿
		Workbook workbook = ExcelUtils.createPayListExcel(page.getList());
		return workbook;
	}
	
	/**
	 * 拼装应付账款查询条件
	 * @param request
	 * @param model
	 * @return String
	 * @author zhaohaiming
	 * */
	private String genWhereSqlForPayList(HttpServletRequest request,Model model){
		StringBuffer sql = new StringBuffer();
		//部门Id
		String deptId = request.getParameter("deptId");
		//计调
		String jd = request.getParameter("jd");
		//收款单位
		String payee = request.getParameter("payee");
		String groupCode = request.getParameter("groupCode");
		if(StringUtils.isNotBlank(deptId)){
			model.addAttribute("deptId", deptId);
			sql.append(" AND list.deptId=").append(deptId);
		}
		if(StringUtils.isNotBlank(jd)){
			model.addAttribute("jds", jd);
			sql.append(" AND list.createBy=").append(jd);
		}
		if(StringUtils.isNotBlank(payee)){
			model.addAttribute("payee", payee);
			sql.append(" AND list.payee like '%").append(payee.trim()).append("%'");
		}
		if(StringUtils.isNotBlank(groupCode)){
			model.addAttribute("groupCode", groupCode);
			sql.append(" AND list.groupCode like '%").append(groupCode.trim()).append("%'");
		}
		return sql.toString();
	}
	
	
	/**
	 * 拼装借款付款sql
	 * @author 赵海明
	 * */
	private String genJKsql(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String uuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer jk = new StringBuffer();
		if(uuid.equals(Context.SUPPLIER_UUID_HQX)){ //环球行只有签证借款
			jk.append("select jk.deptId,jk.createBy,(select su.name from sys_user su where su.id=jk.createBy) jd,")
			  .append(" jk.groupCode,ROUND(sum(jk.batch_total_money),2) total,GROUP_CONCAT(jk.reviewId) recordId,6 orderType,4 moneyType,")
			  .append(" (select GROUP_CONCAT(distinct payee) from refund r where r.record_id in (GROUP_CONCAT(jk.reviewId)) and ")
			  .append(" r.orderType=orderType and r.moneyType=moneyType  and status is null and r.del_flag = '0') payee,jk.payableDate paymentDay,")
			  .append(" ROUND((SELECT sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT sum(m.amount * IFNULL(m.exchangerate, 1)) FROM money_amount m WHERE m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT sum(m.amount) FROM money_amount m WHERE m.serialNum = r.merge_money_serial_num ) ELSE '' END )) amount FROM refund r WHERE r.moneyType = 4  AND r.`status` IS NULL AND r.record_id in ( GROUP_CONCAT(jk.reviewId))),2) payed from (SELECT")
              .append(" p.deptId,")
              .append(" p.createBy,")
              .append(" p.groupCode,")
              .append(" batch.batch_total_money ,")
              .append(" r.id AS reviewId,")
              .append(" p.payableDate")
              .append(" FROM")
              .append(" review r,")
              .append(" review_detail d,")
              .append(" visa_order o,")
              .append(" visa_products p,")
              .append(" visa_flow_batch_opration batch")
              .append(" WHERE")
              .append(" r.id = d.review_id")
              .append(" AND d.myKey = 'visaBorrowMoneyBatchNo'")
              .append(" AND r.orderId = o.id")
              .append(" AND p.id = o.visa_product_id")
              .append(" AND batch.batch_no = d.myValue")
              .append(" AND p.proCompanyId = ").append(companyId)
              .append(" AND r.companyId = ").append(companyId)
              .append(" AND batch.busyness_type = 2")
              .append(" AND r.flowType = 5")
              .append(" AND r. STATUS IN (2, 3)")
              .append(" GROUP BY")
              .append(" batch.batch_no) jk group by jk.deptId,jk.createBy,jk.groupCode");
		}else{
			jk.append(" select deptId,createBy,jd,groupCode,total,recordId,orderType,moneyType,payee,paymentDay,")
              .append(" ROUND((SELECT sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT sum(m.amount * IFNULL(m.exchangerate, 1)) FROM money_amount m WHERE m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT sum(m.amount) FROM money_amount m WHERE m.serialNum = r.merge_money_serial_num ) ELSE '' END )) amount FROM refund r WHERE r.moneyType = 4 AND r.orderType = orderType AND r.`status` IS NULL AND r.record_id in(recordId)),2) payed")
              .append(" FROM (SELECT")
			  .append(" p.deptId,g.createBy,(select su.name from sys_user su where su.id=g.createBy) jd,g.groupCode,review.total,review.recordId,p.activity_kind orderType,4 moneyType,")
			  .append(" (select GROUP_CONCAT(distinct payee) from refund r where r.record_id in (review.recordId) and r.orderType=orderType and r.moneyType=moneyType  and status is null and r.del_flag = '0') payee,g.payableDate paymentDay") 
			  .append(" FROM")
			  .append(" (")
			  .append(" SELECT")
			  .append(" GROUP_CONCAT(r.id) recordId,")
			  .append(" o.productGroupId groupId,")
			  .append(" ROUND(sum( m.amount * IFNULL(m.exchangerate, 1)),2) total")
			  .append(" FROM")
			  .append(" review r,")
			  .append(" money_amount m,")
			  .append(" productorder o")
			  .append(" WHERE")
			  .append(" r.id = m.reviewId")
			  .append(" AND r.orderId = o.id")
			  .append(" AND r. STATUS IN (2, 3)")
			  .append(" AND r.productType IN (1, 2, 3, 4, 5, 10)")
			  .append(" AND r.flowtype = 19")
			  .append(" AND r.companyId = ").append(companyId)
			  .append(" GROUP BY") 
			  .append(" o.productGroupId")
			  .append(" ) review,activitygroup g,")
			  .append(" travelactivity p where review.groupId=g.id AND g.srcActivityId=p.id")
			  .append(" union ALL")
			  .append(" select p.deptId,p.createBy,(select su.name from sys_user su where su.id=p.createBy) jd,p.group_code groupCode,review.total,review.recordId,7 orderType,4 moneyType,")
			  .append(" ( SELECT GROUP_CONCAT(DISTINCT payee) FROM refund r WHERE r.record_id IN (review.recordId) AND r.orderType = orderType AND r.moneyType = moneyType AND STATUS IS NULL AND r.del_flag = '0' ) payee, p.payableDate paymentDay from (")
			  .append(" SELECT")
			  .append(" GROUP_CONCAT(r.id) recordId,")
			  .append(" o.airticket_id groupId,")
			  .append(" ROUND(sum( m.amount * IFNULL(m.exchangerate, 1)),2) total")
			  .append(" FROM")
			  .append(" review r,")
			  .append(" money_amount m,")
			  .append(" airticket_order o")
			  .append(" WHERE")
			  .append(" r.id = m.reviewId")
			  .append(" AND r.orderId = o.id")
			  .append(" AND r. STATUS IN (2, 3)")
			  .append(" AND r.productType =7")
			  .append(" AND r.flowtype = 19")
			  .append(" AND r.companyId = ").append(companyId)
			  .append(" GROUP BY") 
			  .append(" o.airticket_id) review,activity_airticket p where review.groupId=p.id")
			  .append(" UNION ALL")
			  .append(" select p.deptId,p.createBy,(select su.name from sys_user su where su.id=p.createBy) jd,p.groupCode,review.total,review.recordId,p.product_type_id orderType,4 moneyType,")
			  .append(" ( SELECT GROUP_CONCAT(DISTINCT payee) FROM refund r WHERE r.record_id IN (review.recordId) AND r.orderType = orderType AND r.moneyType = moneyType AND STATUS IS NULL AND r.del_flag = '0' ) payee, p.payableDate paymentDay from (")
			  .append(" SELECT")
			  .append(" GROUP_CONCAT(r.id) recordId,")
			  .append(" o.visa_product_id groupId,")
			  .append(" ROUND(sum( m.amount * IFNULL(m.exchangerate, 1)),2) total")
			  .append(" FROM")
			  .append(" review r,")
			  .append(" money_amount m,")
			  .append(" visa_order o")
			  .append(" WHERE")
			  .append(" r.id = m.reviewId")
			  .append(" AND r.orderId = o.id")
			  .append(" AND r. STATUS IN (2, 3)")
			  .append(" AND r.productType =6")
			  .append(" AND r.flowtype = 19")
			  .append(" AND r.companyId = ").append(companyId)
			  .append(" GROUP BY") 
			  .append(" o.visa_product_id) review,visa_products p where review.groupId=p.id")
			  .append(" UNION ALL")
			  .append(" select p.deptId,p.createBy,(select su.name from sys_user su where su.id=p.createBy) jd,g.groupCode,review.total,review.recordId,11 orderType,4 moneyType,")
			  .append(" ( SELECT GROUP_CONCAT(DISTINCT payee) FROM refund r WHERE r.record_id IN (review.recordId) AND r.orderType = orderType AND r.moneyType = moneyType AND STATUS IS NULL AND r.del_flag = '0' ) payee, NULL paymentDay from (")
			  .append(" SELECT")
			  .append(" GROUP_CONCAT(r.id) recordId,")
			  .append(" o.activity_hotel_group_uuid groupId,")
			  .append(" ROUND(sum( m.amount * IFNULL(m.exchangerate, 1)),2) total")
			  .append(" FROM")
			  .append(" review r,")
			  .append(" hotel_money_amount m,")
			  .append(" hotel_order o")
			  .append(" WHERE")
			  .append(" r.id = m.reviewId")
			  .append(" AND r.orderId = o.id")
			  .append(" AND r. STATUS IN (2, 3)")
			  .append(" AND r.productType =11")
			  .append(" AND r.flowtype = 19")
			  .append(" AND r.companyId = ").append(companyId)
			  .append(" GROUP BY") 
			  .append(" o.activity_hotel_group_uuid) review,activity_hotel_group g,activity_hotel p where review.groupId= g.uuid and g.activity_hotel_uuid=p.uuid")
			  .append(" union ALL")
			  .append(" select p.deptId,p.createBy,(select su.name from sys_user su where su.id=p.createBy) jd,g.groupCode,review.total,review.recordId,12 orderType,4 moneyType,")
			  .append(" ( SELECT GROUP_CONCAT(DISTINCT payee) FROM refund r WHERE r.record_id IN (review.recordId) AND r.orderType = orderType AND r.moneyType = moneyType AND STATUS IS NULL AND r.del_flag = '0' ) payee, NULL paymentDay from (")
			  .append(" SELECT")
			  .append(" GROUP_CONCAT(r.id) recordId,")
			  .append(" o.activity_island_group_uuid groupId,")
			  .append(" ROUND(sum( m.amount * IFNULL(m.exchangerate, 1)),2) total")
			  .append(" FROM")
			  .append(" review r,")
			  .append(" island_money_amount m,")
			  .append(" island_order o")
			  .append(" WHERE")
			  .append(" r.id = m.reviewId")
			  .append(" AND r.orderId = o.id")
			  .append(" AND r. STATUS IN (2, 3)")
			  .append(" AND r.productType =12")
			  .append(" AND r.flowtype = 19")
			  .append(" AND r.companyId = ").append(companyId)
			  .append(" GROUP BY") 
			  .append(" o.activity_island_group_uuid) review,activity_island_group g,activity_island p where review.groupId= g.uuid and g.activity_island_uuid=p.uuid) list");
		}
		return jk.toString();
	}
	/**
	 * 拼装返佣付款sql
	 * @author zhaohaiming
	 * */
	private String genFYsql(Long companyId){
		StringBuffer fy = new StringBuffer();
		fy.append("select fy.deptId,fy.jidcreateby createBy,(select su.name from sys_user su where su.id=fy.jidcreateby) jd,fy.groupcode, sum(total) total,GROUP_CONCAT(fy.revid) recordId,fy.prdtype orderType,3 moneyType,")
		  .append("(select GROUP_CONCAT(distinct payee) from refund r where r.record_id in (GROUP_CONCAT(fy.revid)) and r.orderType=orderType and r.moneyType=moneyType")
		  .append("  and status is null and r.del_flag = '0') payee, CASE fy.prdtype WHEN '6' THEN ( SELECT vp.payableDate FROM visa_products vp WHERE")
		  .append(" vp.id = fy.groupid ) WHEN '7' THEN ( SELECT aa.payableDate FROM activity_airticket aa WHERE aa.id = fy.groupid ) WHEN '11' THEN NULL WHEN '12'")
		  .append(" THEN NULL ELSE ( SELECT agp.payableDate FROM activitygroup agp WHERE agp.id = fy.groupid ) END AS paymentDay,")
		  .append(" ROUND((SELECT sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT sum(m.amount * IFNULL(m.exchangerate, 1)) FROM money_amount m WHERE m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT sum(m.amount) FROM money_amount m WHERE m.serialNum = r.merge_money_serial_num ) ELSE '' END )) amount FROM refund r WHERE r.moneyType = 3  AND r.`status` IS NULL AND r.record_id in (GROUP_CONCAT(fy.revid))),2) payed from (")
          .append(" SELECT")
          .append(" rv.groupcode,")
          .append(" rv.prdtype,")
          .append(" rv.jidcreateby,")
          .append(" rv.revid,")
          .append(" rv.deptId,")
          .append(" ROUND(o.rebatesDiff*IFNULL(m.exchangerate,1),2) total,")
          .append(" if(groupid is not null and groupid <> '',groupid,chanpid) groupid")
          .append(" FROM")
          .append(" refundreview_view rv,")
          .append(" order_rebates o,")
          .append(" money_amount m")
          .append(" WHERE")
          .append(" rv.revid = o.rid")
          .append(" AND o.new_rebates = m.serialNum")
          .append(" AND rv.revstatus IN (2, 3)")
          .append(" AND rv.flowtype = 9")
          .append(" AND rv.revcom = ").append(companyId)
          .append(" UNION")
          .append(" SELECT")
          .append(" rv.groupcode,")
          .append(" rv.prdtype,")
          .append(" rv.jidcreateby,")
          .append(" rv.revid,")
          .append(" rv.deptId,")
          .append(" ROUND(m.amount*IFNULL(m.exchangerate,1),2) total,")
          .append(" if(groupid is not null and groupid <> '',groupid,chanpid) groupid")
          .append(" FROM")
          .append(" refundreview_view rv,")
          .append(" money_amount m")
          .append(" WHERE")
          .append(" rv.revid = m.reviewId")
          .append(" AND rv.revstatus IN (2, 3)")
          .append(" AND rv.flowtype = 9")
          .append(" AND rv.revcom = ").append(companyId)
          .append(" ) fy group by groupcode,prdType,jidcreateby,deptId");
		return fy.toString();
	}
	/***
	 * 拼装退款付款sql
	 * @author 赵海明
	 * 
	 * */
	private String genTKsql(Long companyId){
		StringBuffer tk = new StringBuffer();
		tk.append("select tk.deptId,tk.jidcreateby createBy,(select su.name from sys_user su where su.id=tk.jidcreateby) jd,tk.groupCode,sum(tk.total) total,GROUP_CONCAT(tk.revid) recordId,tk.prdtype orderType,2 moneyType,")
		  .append(" (select GROUP_CONCAT(distinct payee) from refund r where r.record_id in (GROUP_CONCAT(tk.revid)) and r.orderType=orderType and r.moneyType=moneyType  and status is null and r.del_flag = '0') payee,")
		  .append(" CASE tk.prdtype WHEN '6' THEN ( SELECT vp.payableDate FROM visa_products vp WHERE vp.id = tk.groupid ) WHEN '7' THEN ( SELECT aa.payableDate FROM activity_airticket aa WHERE aa.id = tk.groupid ) WHEN '11' ")
		  .append(" THEN NULL WHEN '12' THEN NULL ELSE ( SELECT agp.payableDate FROM activitygroup agp WHERE agp.id = tk.groupid ) END AS paymentDay,")
		  .append(" ROUND((SELECT sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT sum(m.amount * IFNULL(m.exchangerate, 1)) FROM money_amount m WHERE m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT sum(m.amount) FROM money_amount m WHERE m.serialNum = r.merge_money_serial_num ) ELSE '' END )) amount FROM refund r WHERE r.moneyType = 2  AND r.`status` IS NULL AND r.record_id in( GROUP_CONCAT(tk.revid))),2) payed from (")
		  .append(" SELECT")
		  .append(" a.groupcode,")
		  .append(" a.prdtype,")
		  .append(" a.jidcreateby,")
		  .append(" a.revid,")
		  .append(" a.deptId,")
		  .append(" ROUND(c.amount * IFNULL(c.exchangerate, 1),2) total,")
		  .append(" if(groupid is not null and groupid <> '',groupid,chanpid) groupid")
		  .append(" FROM")
		  .append(" refundreview_view a,")
		  .append(" money_amount c")
		  .append(" WHERE")
		  .append(" a.revid = c.reviewId")
		  .append(" AND a.revstatus IN (2, 3)")
		  .append(" AND a.flowtype = 1")
		  .append(" AND a.revcom = ").append(companyId)
		  .append(" UNION")
		  .append(" SELECT")
		  .append(" a.groupcode,")
		  .append(" a.prdtype,")
		  .append(" a.jidcreateby,")
		  .append(" a.revid,")
		  .append(" a.deptId,")
		  .append(" ROUND(c.amount * IFNULL(c.exchangerate, 1),2) total,")
		  .append(" if(groupid is not null and groupid <> '',groupid,chanpid) groupid")
		  .append(" FROM")
		  .append(" refundreview_view a,")
		  .append(" hotel_money_amount c")
		  .append(" WHERE")
		  .append(" a.revid = c.reviewId")
		  .append(" AND a.revstatus IN (2, 3)")
		  .append(" AND a.flowtype = 1")
		  .append(" AND a.revcom = ").append(companyId)
		  .append(" UNION")
		  .append(" SELECT")
		  .append(" a.groupcode,")
		  .append(" a.prdtype,")
		  .append(" a.jidcreateby,")
		  .append(" a.revid,")
		  .append(" a.deptId,")
		  .append(" ROUND(c.amount * IFNULL(c.exchangerate, 1),2) total,")
		  .append(" if(groupid is not null and groupid <> '',groupid,chanpid) groupid")
		  .append(" FROM")
		  .append(" refundreview_view a,")
		  .append(" island_money_amount c")
		  .append(" WHERE")
		  .append(" a.revid = c.reviewId")
		  .append(" AND a.revstatus IN (2, 3)")
		  .append(" AND a.flowtype = 1")
		  .append(" AND a.revcom = ").append(companyId)
		  .append(" ) tk group by prdType,groupcode,jidcreateby,deptId");
		return tk.toString();
	}
	/**
	 * 拼装海岛游产品成本付款sql
	 * @author zhaohaiming
	 * 
	 * */
	private String genIslandSql(Long companyId){
		StringBuffer island = new StringBuffer();
		island.append("select p.deptId,g.createBy,(select su.name from sys_user su where su.id=g.createBy) jd,g.groupCode,costrecord.total,costrecord.recordId,12 orderType,1 moneyType,")
		 .append(" (select GROUP_CONCAT(distinct payee) from refund r where r.record_id in (costrecord.recordId) and r.orderType=orderType and r.moneyType=moneyType  and status is null and r.del_flag = '0') payee,null paymentDay,")
		 .append(" ROUND((SELECT sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT sum(m.amount * IFNULL(m.exchangerate, 1)) FROM money_amount m WHERE m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT sum(m.amount) FROM money_amount m WHERE m.serialNum = r.merge_money_serial_num ) ELSE '' END )) amount FROM refund r WHERE r.moneyType = 1 AND r.orderType = 12 AND r.`status` IS NULL AND r.record_id in (costrecord.recordId)),2) payed from (")
		 .append(" SELECT")
		 .append(" cr.activity_uuid,")
		 .append(" cr.orderType,")
		 .append(" GROUP_CONCAT(cr.id) recordId,")
		 .append(" ROUND(sum(cr.priceAfter),2) total")
		 .append(" FROM")
		 .append(" cost_record_island cr")
		 .append(" WHERE")
		 .append(" cr.payReview = 2")
		 .append(" AND cr.reviewType <> 1")
		 .append(" AND cr.reviewType <> 2")
		 .append(" AND cr.delFlag = 0")
		 .append(" AND cr.budgetType = 1")
		 .append(" GROUP BY")
		 .append(" cr.activity_uuid,")
		 .append(" cr.orderType) costrecord,")
		 .append(" activity_island_group g,")
		 .append(" activity_island p")
		 .append(" where 	costrecord.activity_uuid = g.uuid and g.activity_island_uuid=p.uuid")
		 .append(" AND g.delFlag = 0")
		 .append(" AND p.wholesaler_id = ").append(companyId)
		 .append(" AND p.delFlag=0");
		return island.toString();
	}
	
	/**
	 * 拼装酒店产品成本付款sql
	 * @author zhaohaiming
	 * 
	 * */
	private String genHotelSql(Long companyId){
		StringBuffer hotel = new StringBuffer();
		hotel.append("select p.deptId,g.createBy,(select su.name from sys_user su where su.id=g.createBy) jd,g.groupCode,costrecord.total,costrecord.recordId,11 orderType,1 moneyType," )
			 .append(" (select GROUP_CONCAT(distinct payee) from refund r where r.record_id in (costrecord.recordId) and r.orderType=orderType and r.moneyType=moneyType  and status is null and r.del_flag = '0') payee,null paymentDay,")
			 .append(" ROUND((SELECT sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT sum(m.amount * IFNULL(m.exchangerate, 1)) FROM money_amount m WHERE m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT sum(m.amount) FROM money_amount m WHERE m.serialNum = r.merge_money_serial_num ) ELSE '' END )) amount FROM refund r WHERE r.moneyType = 1 AND r.orderType = 11 AND r.`status` IS NULL AND r.record_id in (costrecord.recordId)),2) payed from (")
			 .append(" SELECT")
			 .append(" cr.activity_uuid,")
			 .append(" cr.orderType,")
			 .append(" GROUP_CONCAT(cr.id) recordId,")
			 .append(" ROUND(sum(cr.priceAfter),2) total")
			 .append(" FROM")
			 .append(" cost_record_hotel cr")
			 .append(" WHERE")
			 .append(" cr.payReview = 2")
			 .append(" AND cr.reviewType <> 1")
			 .append(" AND cr.reviewType <> 2")
			 .append(" AND cr.delFlag = 0")
			 .append(" AND cr.budgetType = 1")
			 .append(" GROUP BY")
			 .append(" cr.activity_uuid,")
			 .append(" cr.orderType) costrecord,")
			 .append(" activity_hotel_group g,")
			 .append(" activity_hotel p")
			 .append(" where 	costrecord.activity_uuid = g.uuid and g.activity_hotel_uuid=p.uuid")
			 .append(" AND g.delFlag = 0")
			 .append(" AND p.wholesaler_id = ").append(companyId)
			 .append(" AND p.delFlag=0");
		return hotel.toString();
	}
	
	/**
	 * 拼装签证产品成本付款sql
	 * @author zhaohaiming
	 * 
	 * */
	private String genQZSql(Long companyId){
		StringBuffer qz = new StringBuffer();
		qz.append("select g.deptId,g.createBy,(select su.name from sys_user su where su.id=g.createBy) jd,g.groupCode,costrecord.total,costrecord.recordId,6 orderType,1 moneyType," )
		  .append(" (select GROUP_CONCAT(distinct payee) from refund r where r.record_id in (costrecord.recordId) and r.orderType=orderType and r.moneyType=moneyType  and status is null and r.del_flag = '0') payee,g.payableDate paymentDay,")
		  .append(" ROUND((SELECT sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT sum(m.amount * IFNULL(m.exchangerate, 1)) FROM money_amount m WHERE m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT sum(m.amount) FROM money_amount m WHERE m.serialNum = r.merge_money_serial_num ) ELSE '' END )) amount FROM refund r WHERE r.moneyType = 1 AND r.orderType = 6 AND r.`status` IS NULL AND r.record_id in (costrecord.recordId)),2) payed from (")
		  .append(" SELECT")
		  .append(" cr.activityId,")
		  .append(" cr.orderType,")
		  .append(" GROUP_CONCAT(cr.id) recordId,")
		  .append(" ROUND(sum(cr.priceAfter),2) total")
		  .append(" FROM")
		  .append(" cost_record cr")
		  .append(" WHERE")
		  .append(" cr.payReview = 2")
		  .append(" AND cr.reviewType <> 1")
		  .append(" AND cr.reviewType <> 2")
		  .append(" AND cr.delFlag = 0")
		  .append(" AND cr.budgetType = 1")
		  .append(" AND cr.orderType =6")
		  .append(" GROUP BY")
		  .append(" cr.activityId,")
		  .append(" cr.orderType) costrecord,")
		  .append(" visa_products g")
		  .append(" where 	costrecord.activityId = g.id")
		  .append(" AND g.delFlag = 0")
		  .append(" AND g.proCompanyId = ").append(companyId);
		return qz.toString();
	}
	/**
	 *  拼装机票产品的成本付款sql
	 *  @author zhaohaiming
	 * */
	private String genJPSql(Long companyId){
		StringBuffer jp = new StringBuffer();
		jp.append("select g.deptId,g.createBy,(select su.name from sys_user su where su.id=g.createBy) jd,g.group_code groupCode,costrecord.total,costrecord.recordId,7 orderType,1 moneyType,")
				.append(" (select GROUP_CONCAT(distinct payee) from refund r where r.record_id in (costrecord.recordId) and r.orderType=orderType and r.moneyType=moneyType  and status is null and r.del_flag = '0') payee,g.payableDate paymentDay,")
				.append(" ROUND((SELECT sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT sum(m.amount * IFNULL(m.exchangerate, 1)) FROM money_amount m WHERE m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT sum(m.amount) FROM money_amount m WHERE m.serialNum = r.merge_money_serial_num ) ELSE '' END )) amount FROM refund r WHERE r.moneyType = 1 AND r.orderType = 7 AND r.`status` IS NULL AND r.record_id in (costrecord.recordId)),2) payed from (")
		  .append(" SELECT")
		  .append(" cr.activityId,")
		  .append(" cr.orderType,")
		  .append(" GROUP_CONCAT(cr.id) recordId,")
			.append(" ROUND(sum(cr.priceAfter),2) total")
			.append(" FROM")
			.append(" cost_record cr")
			.append(" WHERE")
			.append(" cr.payReview = 2")
			.append(" AND cr.reviewType <> 1")
			.append(" AND cr.reviewType <> 2")
			.append(" AND cr.delFlag = 0")
			.append(" AND cr.budgetType = 1")
			.append(" AND cr.orderType =7")
			.append(" GROUP BY")
			.append(" cr.activityId,")
			.append(" cr.orderType) costrecord,")
			.append(" activity_airticket g")
			.append(" where 	costrecord.activityId = g.id")
			.append(" AND g.delFlag = 0")
			.append(" AND g.proCompany = ").append(companyId);
            return jp.toString();
	}
	
	/**
	 * 拼装单团类产品的成本付款sql
	 * @zhaohaiming
	 * */
	private String genDTSql(Long companyId){
		StringBuffer dt = new StringBuffer();
        dt.append("select p.deptId,g.createBy,(select su.name from sys_user su where su.id=g.createBy) jd,g.groupCode,costrecord.total,costrecord.recordId,p.activity_kind orderType,1 moneyType,")
        		.append(" (select GROUP_CONCAT(distinct payee) from refund r where r.record_id in (costrecord.recordId) and r.orderType=orderType and r.moneyType=moneyType  and status is null and r.del_flag = '0') payee,g.payableDate paymentDay,")
        		.append(" ROUND(( SELECT sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT sum(m.amount * IFNULL(m.exchangerate, 1)) FROM money_amount m WHERE m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT sum(m.amount) FROM money_amount m WHERE m.serialNum = r.merge_money_serial_num ) ELSE '' END )) amount FROM refund r WHERE r.moneyType = 1 AND r.orderType = p.activity_kind AND r.`status` IS NULL AND r.record_id in ( costrecord.recordId)),2) payed from (")
          .append(" SELECT")
          .append(" cr.activityId,")
          .append(" cr.orderType,")
          .append(" GROUP_CONCAT(cr.id) recordId,")
          .append(" ROUND(sum(cr.priceAfter),2) total")
          .append(" FROM")
          .append(" cost_record cr")
          .append(" WHERE")
          .append(" cr.payReview = 2")
          .append(" AND cr.reviewType <> 1")
		  .append(" AND cr.reviewType <> 2")
          .append(" AND cr.delFlag = 0")
          .append(" AND cr.budgetType = 1")
          .append(" AND cr.orderType IN (1, 2, 3, 4, 5, 10)")
          .append(" GROUP BY")
          .append(" cr.activityId,")
          .append(" cr.orderType) costrecord,")
          .append(" activitygroup g,")
          .append(" travelactivity p")
          .append(" where costrecord.activityId = g.id")
          .append(" AND g.srcActivityId = p.id")
          .append(" AND g.delFlag = 0")
          .append(" AND p.proCompany = ").append(companyId);
		return dt.toString();
		
	}

	/**
	 * 
	* @Title: getPayListByPayDate
	* @Description: TODO(获取应收账款到期数据)
	* @param @return    设定文件
	* @return List<Map<Object,Object>>    返回类型
	* @throws
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public void getPayListByPayDate() {
		// TODO Auto-generated method stub
		List<Map<Object, Object>> payList = receivePayDao.getPayListByPayDate();
		Session session = receivePayDao.getSession();
		if(payList != null && payList.size() > 0) {
			for (Map<Object, Object> map : payList) {
				//订单id
				String id = map.get("id") == null ? "" : map.get("id").toString();
				//订单编号
				String orderNum = map.get("orderNum") == null ? "" : map.get("orderNum").toString();
				//订单类型
				String orderType = map.get("orderType") == null ? "0" : map.get("orderType").toString();
				//应收账期
				String payableDate = map.get("payableDate") == null ? "" : map.get("payableDate").toString();
				//下单人id
				String createBy = map.get("createBy") == null ? "0" : map.get("createBy").toString();
				//消息(公告)类
				MsgEntity msgAnnouncement = new MsgEntity();
				//标题
				msgAnnouncement.setTitle(Context.MSG_FINANCE_ORDER + "#" + id + "#" + orderNum + "#" + Context.MSG_RECEIVE_PAY + payableDate);
				//消息状态
				msgAnnouncement.setStatus(Context.MESSAGE_STATUS_ISSUE);
				//消息类型
				msgAnnouncement.setMsgType(Context.MSG_TYPE_FINANCE);
				//公告类型
				msgAnnouncement.setMsgNoticeType(Integer.parseInt(orderType));
				//获取当前登录人批发商ID
				Long companyId = 0L;
				if(StringUtils.isNotBlank(createBy)) {
					if(!createBy.equals("0")) {
						User user = userDao.findById(Long.parseLong(createBy));
						if(user != null) {
							Office office = user.getCompany();
							if(office != null) {
								companyId = office.getId();
							}
						}
					}
				}
				msgAnnouncement.setCompanyId(companyId);
				msgAnnouncement.setCreateDate(new Date());
				msgAnnouncement.setCreateBy(Long.parseLong(createBy));
				msgAnnouncement.setDelFlag("0");
				List<Long> userIdList = receivePayDao.findUserIdByReceivePay(companyId);
				//保存此消息，可查看人员包括：财务、销售、计调、总经理
				if(userIdList != null && userIdList.size() > 0) {
					if(msgAnnouncement != null) {
						//对信息进行保存
						session.save(msgAnnouncement);
						session.flush();
					}
					//对信息进行保存
					try{
						//循环接收信息的人的列表,逐条保存
						for(int i = 0;i < userIdList.size();i++){
							//保存用户标示表
							MsgMarkEntity msgUserMark = new MsgMarkEntity();
							msgUserMark.setIfRead(0);
							msgUserMark.setMsgAnnouncementId(msgAnnouncement.getId());
							msgUserMark.setUserId(userIdList.get(i));
							msgUserMark.setCreateDate(new Date());
							msgUserMark.setDelFlag("0");
							session.save(msgUserMark);
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public Map<String, BigDecimal> getRealReceiveSumMoney(GroupManagerEntity entity, Integer orderType) {
		if(Context.ORDER_TYPE_ALL == orderType){
			//团期类订单收款达帐总额
			List<Map<String, Object>> activityOrder = receivePayDao.getActivityOrderRealReceiveSumMoney(entity, orderType);
			//机票订单收款达帐总额
			List<Map<String, Object>> airticketOrder = receivePayDao.getAirticketOrderRealReceiveSumMoney(entity);
			//签证订单收款达帐总额               全部不包括签证产品 Bug 11652
			//List<Map<String, Object>> visaOrder = receivePayDao.getVisaOrderRealReceiveSumMoney(entity);
			
			//散拼产品切位收款达帐总额
			List<Map<String, Object>> reserveSp = receivePayDao.getSPReserveReceiveSumMoney(entity);
			//机票产品切位收款达帐总额
			List<Map<String, Object>> reserveAirticket = receivePayDao.getAirticketReserveReceiveSumMoney(entity);
			
			//团期类其他收入收款达帐总额
			List<Map<String, Object>> activityOtherincome = receivePayDao.getActivityOtherIncomeRecevieSumMoney(entity, orderType);
			//机票其他收入收款达帐总额
			List<Map<String, Object>> airticketOtherincome = receivePayDao.getAirticketOtherIncomeRecevieSumMoney(entity);
			//签证其他收入收款达帐总额            全部不包括签证产品 Bug 11652
			//List<Map<String, Object>> visaOtherincome = receivePayDao.getVisaOtherIncomeRecevieSumMoney(entity);
			
			//团期类订单收款达帐总额 + 机票订单收款达帐总额
			Map<String,BigDecimal> map1 = MoneyAmountUtils.multiplyCurrencyPlus(activityOrder, airticketOrder);
			//机票产品切位收款达帐总额+ 散拼产品切位收款达帐总额
			Map<String,BigDecimal> map2 = MoneyAmountUtils.multiplyCurrencyPlus(reserveAirticket, reserveSp);
			//机票其他收入收款达帐总额 + 团期类其他收入收款达帐总额
			Map<String,BigDecimal> map3 = MoneyAmountUtils.multiplyCurrencyPlus(airticketOtherincome, activityOtherincome);
			
			Map<String,BigDecimal> map4 = MoneyAmountUtils.multiplyCurrencyPlus(map1, map2);
			Map<String,BigDecimal> map5 = MoneyAmountUtils.multiplyCurrencyPlus(map3, map4);
			return map5;
		}else if(Context.ORDER_TYPE_JP == orderType){
			//机票订单收款达帐总额
			List<Map<String, Object>> airticketOrder = receivePayDao.getAirticketOrderRealReceiveSumMoney(entity);
			
			//机票产品切位收款达帐总额
			List<Map<String, Object>> reserveAirticket = receivePayDao.getAirticketReserveReceiveSumMoney(entity);
			
			//机票其他收入收款达帐总额
			List<Map<String, Object>> airticketOtherincome = receivePayDao.getAirticketOtherIncomeRecevieSumMoney(entity);
			
			//机票订单收款达帐总额 + 机票产品切位收款达帐总额
			Map<String,BigDecimal> map1 = MoneyAmountUtils.multiplyCurrencyPlus(airticketOrder, reserveAirticket);
			//机票其他收入收款达帐总额 + 空
			Map<String,BigDecimal> map2 = MoneyAmountUtils.multiplyCurrencyPlus(airticketOtherincome, new ArrayList<Map<String, Object>>());
			return MoneyAmountUtils.multiplyCurrencyPlus(map1, map2);
		}else if(Context.ORDER_TYPE_QZ == orderType){
			//签证订单收款达帐总额
			List<Map<String, Object>> visaOrder = receivePayDao.getVisaOrderRealReceiveSumMoney(entity);
			
			//签证其他收入收款达帐总额
			List<Map<String, Object>> visaOtherincome = receivePayDao.getVisaOtherIncomeRecevieSumMoney(entity);
			
			//签证订单收款达帐总额 + 签证其他收入收款达帐总额
			Map<String,BigDecimal> map1 = MoneyAmountUtils.multiplyCurrencyPlus(visaOrder, visaOtherincome);
			return map1;
		}else{
			//团期类订单收款达帐总额
			List<Map<String, Object>> activityOrder = receivePayDao.getActivityOrderRealReceiveSumMoney(entity, orderType);
			//团期类其他收入收款达帐总额
			List<Map<String, Object>> activityOtherincome = receivePayDao.getActivityOtherIncomeRecevieSumMoney(entity, orderType);
			//团期类订单收款达帐总额 + 团期类其他收入收款达帐总额
			Map<String,BigDecimal> map1 = MoneyAmountUtils.multiplyCurrencyPlus(activityOrder, activityOtherincome);
			//散拼产品切位收款达帐总额
			List<Map<String, Object>> reserveSp = new ArrayList<Map<String, Object>>();
			if(Context.ORDER_TYPE_SP == orderType){
				//散拼产品切位收款达帐总额
				reserveSp = receivePayDao.getSPReserveReceiveSumMoney(entity);
			}
			//签证订单收款达帐总额 + 散拼产品切位收款达帐总额
			Map<String,BigDecimal> map2 = MoneyAmountUtils.multiplyCurrencyPlus(reserveSp, new ArrayList<Map<String, Object>>());
			
			Map<String,BigDecimal> map3 = MoneyAmountUtils.multiplyCurrencyPlus(map1, map2);
			return map3;
		}
	}

	@Override
	public Map<String, BigDecimal> getRealPayedSumMoney(GroupManagerEntity entity, Integer orderType) {
		if(Context.ORDER_TYPE_ALL == orderType){
			//成本付款团期类产品达帐总额
			List<Map<String, Object>> activityCostList = receivePayDao.getActivityCostPayedSumMoney(entity, orderType);
			//机票成本付款达帐总额
			List<Map<String, Object>> airticketCostList = receivePayDao.getAirticketCostPayedSumMoney(entity);
			//签证产品成本付款达帐总额         全部不包括签证产品 Bug 11652
			//List<Map<String, Object>> visaCostList = receivePayDao.getVisaCostPayedSumMoney(entity);
			//团期类退款付款达帐总金额
			List<Map<String, Object>> activityRefundList = receivePayDao.getActivityRefundPayedSumMoney(entity, orderType);
			//机票退款付款达帐总金额
			List<Map<String, Object>> airticketRefundList = receivePayDao.getAirticketRefundPayedSumMoney(entity);
			
			//新行者计调退款
			List<Map<String, Object>> xxzRefundList = new ArrayList<Map<String, Object>>();
			if(Context.SUPPLIER_UUID_XXZ.equals(UserUtils.getUser().getCompany().getUuid())){
				xxzRefundList = receivePayDao.getAirticketRefundPayedSumMoneyForXXZ(entity);
			}
			//团期类反佣付款达帐总金额
			List<Map<String, Object>> activityRebateList = receivePayDao.getActivityRebatePayedSumMoney(entity, orderType);
			//机票反佣付款达帐总金额
			List<Map<String, Object>> airticketRebateList = receivePayDao.getAirticketRebatePayedSumMoney(entity);
			
			//成本付款团期类产品达帐总额 + 机票成本付款达帐总额
			Map<String, BigDecimal> map1 = MoneyAmountUtils.multiplyCurrencyPlus(activityCostList, airticketCostList);
			// 团期类退款付款达帐总金额 + 机票产品退款付款达帐总金额 
			Map<String, BigDecimal> map2 = MoneyAmountUtils.multiplyCurrencyPlus(activityRefundList, airticketRefundList);
			//团期类反佣付款达帐总金额 + 机票反佣付款达帐总金额
			Map<String, BigDecimal> map3 = MoneyAmountUtils.multiplyCurrencyPlus(activityRebateList, airticketRebateList);
			//新行者计调退款 + 空
			Map<String, BigDecimal> map4 = MoneyAmountUtils.multiplyCurrencyPlus(xxzRefundList, new ArrayList<Map<String, Object>>());
			
			
			Map<String, BigDecimal> map5 = MoneyAmountUtils.multiplyCurrencyPlus(map1, map2);
			Map<String, BigDecimal> map6 = MoneyAmountUtils.multiplyCurrencyPlus(map3, map4);
			Map<String, BigDecimal> map7 = MoneyAmountUtils.multiplyCurrencyPlus(map5, map6);
			return map7;
		}else if(Context.ORDER_TYPE_JP == orderType){
			//机票成本付款达帐总额
			List<Map<String, Object>> airticketCostList = receivePayDao.getAirticketCostPayedSumMoney(entity);
			//机票退款付款达帐总金额
			List<Map<String, Object>> airticketRefundList = receivePayDao.getAirticketRefundPayedSumMoney(entity);
			//新行者计调退款
			List<Map<String, Object>> xxzRefundList = new ArrayList<Map<String, Object>>();
			if(Context.SUPPLIER_UUID_XXZ.equals(UserUtils.getUser().getCompany().getUuid())){
				xxzRefundList = receivePayDao.getAirticketRefundPayedSumMoneyForXXZ(entity);
			}
			//机票反佣付款达帐总金额
			List<Map<String, Object>> airticketRebateList = receivePayDao.getAirticketRebatePayedSumMoney(entity);
			
			//机票成本付款达帐总额 + 机票退款付款达帐总金额
			Map<String, BigDecimal> map1 = MoneyAmountUtils.multiplyCurrencyPlus(airticketCostList, airticketRefundList);
			//机票反佣付款达帐总金额 + 新行者计调退款
			Map<String, BigDecimal> map2 = MoneyAmountUtils.multiplyCurrencyPlus(airticketRebateList, xxzRefundList);
			
			Map<String, BigDecimal> map3 = MoneyAmountUtils.multiplyCurrencyPlus(map1, map2);
			return map3;
		}else if(Context.ORDER_TYPE_QZ == orderType){
			//签证成本付款达帐总额
			List<Map<String, Object>> visaCostList = receivePayDao.getVisaCostPayedSumMoney(entity);
			//签证退款付款达帐总金额
			List<Map<String, Object>> visaRefundList = receivePayDao.getVisaRefundPayedSumMoney(entity);
			//签证反佣付款达帐总金额
			List<Map<String, Object>> visaRebateList = receivePayDao.getVisaRebatePayedSumMoney(entity);
			
			//签证成本付款达帐总额 + 签证退款付款达帐总金额
			Map<String, BigDecimal> map1 = MoneyAmountUtils.multiplyCurrencyPlus(visaCostList, visaRefundList);
			//签证反佣付款达帐总金额 + 空
			Map<String, BigDecimal> map2 = MoneyAmountUtils.multiplyCurrencyPlus(visaRebateList, new ArrayList<Map<String, Object>>());
			
			Map<String, BigDecimal> map3 = MoneyAmountUtils.multiplyCurrencyPlus(map1, map2);
			return map3;
		}else{
			//成本付款团期类产品达帐总额
			List<Map<String, Object>> activityCostList = receivePayDao.getActivityCostPayedSumMoney(entity, orderType);
			//团期类退款付款达帐总金额
			List<Map<String, Object>> activityRefundList = receivePayDao.getActivityRefundPayedSumMoney(entity, orderType);
			//团期类反佣付款达帐总金额
			List<Map<String, Object>> activityRebateList = receivePayDao.getActivityRebatePayedSumMoney(entity, orderType);
			
			//成本付款团期类产品达帐总额 + 团期类退款付款达帐总金额
			Map<String, BigDecimal> map1 = MoneyAmountUtils.multiplyCurrencyPlus(activityCostList, activityRefundList);
			//团期类反佣付款达帐总金额 + 空
			Map<String, BigDecimal> map2 = MoneyAmountUtils.multiplyCurrencyPlus(activityRebateList, new ArrayList<Map<String, Object>>());
			
			Map<String, BigDecimal> map3 = MoneyAmountUtils.multiplyCurrencyPlus(map1, map2);
			return map3;
		}
	}

	//查询应收总额
	@Override
	public BigDecimal getReceiveSumMoneyCNY(Integer orderType, String activityId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(SUM(ma.amount*IFNULL(ma.exchangerate,0)),2) AS money FROM money_amount ma,(");
		//根据订单类型的不同，子查询使用不同的表，把最终查询的结果字段起名为total。
		if (Context.ORDER_TYPE_JP == orderType){
			sql.append("SELECT o.total_money FROM airticket_order o WHERE o.del_flag = 0 AND o.order_state NOT IN (99,111) AND o.airticket_id = ").append(activityId);
		}else if (Context.ORDER_TYPE_QZ == orderType){
			sql.append("SELECT o.total_money FROM visa_order o WHERE")
			   .append(" o.del_flag = 0 AND o.payStatus NOT IN (99,111) AND o.visa_order_status <> 100 AND o.visa_product_id = ").append(activityId);
		}else if (Context.ORDER_TYPE_ISLAND == orderType){
			sql.append("SELECT o.total_money FROM island_order o WHERE o.delFlag = 0 AND o.orderStatus <> 3 AND o.activity_island_group_uuid = '")
			   .append(activityId).append("'");
		}else if (Context.ORDER_TYPE_HOTEL == orderType){
			sql.append("SELECT o.total_money FROM hotel_order o WHERE o.delFlag = 0 AND o.orderStatus <> 3 AND o.activity_hotel_group_uuid = '")
			   .append(activityId).append("'");
		} else {
			sql.append("SELECT o.total_money FROM productorder o WHERE o.delFlag = 0 AND o.payStatus NOT IN (99, 111) AND o.productGroupId = ").append(activityId);
		}
		sql.append(") total WHERE total.total_money = ma.serialNum");
		List<Object> total = receivePayDao.findBySql(sql.toString());
		if (total.get(0) != null){
			//查看该订单有无差额，如果有
			StringBuffer sbf = new StringBuffer();
			sbf.append("SELECT * FROM productorder WHERE productGroupId =").append(activityId).append(" AND payStatus NOT IN (99, 111)  and delFlag = 0 ");
			List<ProductOrderCommon> orderCommons = receivePayDao.findBySql(sbf.toString(), ProductOrderCommon.class);
			if(orderCommons.size() > 0){
				ProductOrderCommon orderCommon = orderCommons.get(0);
				if(orderCommon.getDifferenceFlag() == 1 && StringUtils.isNotBlank(orderCommon.getDifferenceMoney())){
					MoneyAmount moneyAmount = MoneyAmountUtils.getMoneyAmountByUUID(orderCommon.getDifferenceMoney());
					BigDecimal decimal = moneyAmount.getAmount().multiply(moneyAmount.getExchangerate());
					BigDecimal bigDecimal = new BigDecimal(total.get(0).toString()).subtract(decimal);
					total.remove(0);
					total.add(bigDecimal);
				}
			}
			return new BigDecimal(total.get(0).toString());
		}
		return new BigDecimal(0);
	}

	@Override
	public BigDecimal getRealReceiveSumMoneyCNY(Integer orderType, String activityId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(SUM(ma.amount*IFNULL(ma.exchangerate,0)),2) AS money FROM (");
		if (Context.ORDER_TYPE_JP == orderType){
			sql.append("SELECT o.accounted_money FROM airticket_order o WHERE o.del_flag = 0 AND o.order_state NOT IN (99,111) AND o.airticket_id = ").append(activityId);
		}else if (Context.ORDER_TYPE_QZ == orderType){
			sql.append("SELECT o.accounted_money FROM visa_order o WHERE")
			   .append(" o.del_flag = 0 AND o.payStatus NOT IN (99,111) AND o.visa_order_status <> 100 AND o.visa_product_id = ").append(activityId);
		}else if (Context.ORDER_TYPE_ISLAND == orderType){
			sql.append("SELECT o.accounted_money FROM island_order o WHERE o.delFlag = 0 AND o.orderStatus <> 3 AND o.activity_island_group_uuid = '")
			   .append(activityId).append("'");
		}else if (Context.ORDER_TYPE_HOTEL == orderType){
			sql.append("SELECT o.accounted_money FROM hotel_order o WHERE o.delFlag = 0 AND o.orderStatus <> 3 AND o.activity_hotel_group_uuid = '")
			   .append(activityId).append("'");
		} else {
			sql.append("SELECT o.accounted_money FROM productorder o WHERE o.delFlag = 0 AND o.payStatus NOT IN (99, 111) AND o.productGroupId = ").append(activityId);
		}
		sql.append(") accounted,money_amount ma WHERE accounted.accounted_money = ma.serialNum");
		List<Object> accounted = receivePayDao.findBySql(sql.toString());
		if (accounted.get(0) != null){
			//查看该订单有无差额，如果有
			return new BigDecimal(accounted.get(0).toString());
		}
		return new BigDecimal(0);

	}

	@Override
	public BigDecimal getOtherIncomeSumMoneyCNY(Integer orderType, String activityId,Boolean isPayed) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(SUM(cr.price*cr.quantity*IFNULL(cr.rate,0)),2) AS other FROM ");
		if (Context.ORDER_TYPE_HOTEL == orderType){
			sql.append("cost_record_hotel cr WHERE cr.activity_uuid = '").append(activityId).append("'");
		}else if (Context.ORDER_TYPE_ISLAND == orderType){
			sql.append("cost_record_island cr WHERE cr.activity_uuid = '").append(activityId).append("'");
		}else {
			sql.append("cost_record cr WHERE cr.activityId = ").append(activityId);
		}
		sql.append(" AND cr.delFlag = 0 AND cr.budgetType = 2 AND cr.orderType = ").append(orderType);//cr.budgetType = 2表示其它收入
		if (isPayed){//已确认付款。
			sql.append(" AND EXISTS (SELECT 1 FROM pay_group pg WHERE pg.cost_record_id = cr.id AND pg.isAsAccount = 1)");
		}

		List<Object> other = receivePayDao.findBySql(sql.toString());
		if (other.get(0) != null){
			return new BigDecimal(other.get(0).toString());
		}
		return new BigDecimal(0);
	}

	@Override
	public BigDecimal getCostSumMoneyCNY(Integer orderType, String activityId, Boolean isPayed) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(SUM(cr.price*cr.quantity*IFNULL(cr.rate,0)),2) AS cost FROM ");
		if (Context.ORDER_TYPE_HOTEL == orderType){
			sql.append("cost_record_hotel cr WHERE cr.activity_uuid = '").append(activityId).append("'");
		}else if (Context.ORDER_TYPE_ISLAND == orderType){
			sql.append("cost_record_island cr WHERE cr.activity_uuid = '").append(activityId).append("'");
		}else {
			sql.append("cost_record cr WHERE cr.activityId = ").append(activityId);
		}
		sql.append(" AND cr.delFlag = 0 AND cr.reviewType = 0 AND cr.review = 2 AND cr.orderType = ").append(orderType); //reviewType=0,表示成本；review = 2表示审核通过
		//取review_new表中的对应支付状态为已支付。且该成本变为实际成本。
		if (isPayed){
			sql.append(" AND cr.budgetType = 1 AND cr.payStatus = 1");
		}else {
			//未支付只取预算成本：budgetType = 0。
			sql.append(" AND cr.budgetType = 0");
		}

		List<Object> cost = receivePayDao.findBySql(sql.toString());
		if (cost.get(0) != null){
			return new BigDecimal(cost.get(0).toString());
		}
		return new BigDecimal(0);
	}

	@Override
	public BigDecimal getRefundSumMoneyCNY(Integer orderType, String activityId, Boolean isPayed) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(SUM(cr.price*cr.quantity*IFNULL(cr.rate,0)),2) AS refund FROM ");
		if (Context.ORDER_TYPE_HOTEL == orderType){
			sql.append("cost_record_hotel cr WHERE cr.activity_uuid = '").append(activityId).append("'");
		}else if (Context.ORDER_TYPE_ISLAND == orderType){
			sql.append("cost_record_island cr WHERE cr.activity_uuid = '").append(activityId).append("'");
		}else {
			sql.append("cost_record cr WHERE cr.activityId = ").append(activityId);
		}
		sql.append(" AND cr.delFlag = 0 AND cr.reviewType = 1 AND cr.orderType = ").append(orderType)
		   .append(" AND cr.reviewStatus IN ('审批通过','审核通过') "); //reviewType=1,表示退款；reviewStatus表示审核通过
		//查询已确认付款的数据
		if(isPayed){
			sql.append(" AND cr.budgetType = 1 ");
			if (Context.ORDER_TYPE_ISLAND != orderType && Context.ORDER_TYPE_HOTEL != orderType){
				sql.append(" AND EXISTS (SELECT 1 FROM review_new r WHERE r.id = cr.reviewUuid AND r.pay_status =1) ");
			}
		}else {
			sql.append(" AND cr.budgetType = 0");//取预算价，审批通过的话，会生成一条新的记录，使用cr.budgetType = 0进行区别。
		}
		List<Object> refund = receivePayDao.findBySql(sql.toString());
		if (refund.get(0) != null){
			return new BigDecimal(refund.get(0).toString());
		}
		return new BigDecimal(0);
	}

	@Override
	public BigDecimal getRebateSumMoneyCNY(Integer orderType, String activityId, Boolean isPayed) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(SUM(cr.price*cr.quantity*IFNULL(cr.rate,0)),2) AS refund FROM ");
		if (Context.ORDER_TYPE_HOTEL == orderType){
			sql.append("cost_record_hotel cr WHERE cr.activity_uuid = '").append(activityId).append("'");
		}else if (Context.ORDER_TYPE_ISLAND == orderType){
			sql.append("cost_record_island cr WHERE cr.activity_uuid = '").append(activityId).append("'");
		}else {
			sql.append("cost_record cr WHERE cr.activityId = ").append(activityId);
		}
		sql.append(" AND cr.delFlag = 0 AND cr.reviewType = 2 AND cr.orderType = ").append(orderType)//reviewType=2,表示返佣；reviewStatus表示审核通过
		   .append(" AND cr.reviewStatus IN ('审批通过','审核通过') ");
		if (isPayed){
			sql.append(" AND cr.budgetType = 1 ");
			if (Context.ORDER_TYPE_ISLAND != orderType && Context.ORDER_TYPE_HOTEL != orderType){
				sql.append(" AND EXISTS (SELECT 1 FROM review_new r WHERE r.id = cr.reviewUuid AND r.pay_status =1) ");
			}
		}else {
			sql.append(" AND cr.budgetType = 0");//取预算价，审批通过的话，会生成一条新的记录，使用cr.budgetType = 0进行区别。
		}
		List<Object> rebate = receivePayDao.findBySql(sql.toString());
		if (rebate.get(0) != null){
			return new BigDecimal(rebate.get(0).toString());
		}
		return new BigDecimal(0);
	}

	/**
	 * 查询拉美图的团期实际总收款
	 * 针对于拉美图的用户，其实收金额是以结算单为准，所以需要重新写sql进行查询。
	 * @author yudong.xu --2016/4/14--10:27
	 */
	public BigDecimal getRealReceiveMoneyForLMT(Integer orderType,String activityId){
		StringBuilder sql = new StringBuilder();
		if (orderType == Context.ORDER_TYPE_JP){
			sql.append("SELECT IFNULL(SUM(m.amount * m.exchangerate),0) FROM money_amount m,");
			sql.append("airticket_order o WHERE o.accounted_money = m.serialNum AND o.del_flag = '0' AND o.airticket_id =").append(activityId)
			   .append(" AND o.order_state NOT IN (7, 99, 111) AND o.settle_locked_in is null")	;
		}else if (orderType == Context.ORDER_TYPE_QZ){
			sql.append("SELECT IFNULL(SUM(m.amount * m.exchangerate),0) FROM money_amount m,");
			sql.append("visa_order o WHERE o.accounted_money = m.serialNum AND o.del_flag = '0' AND o.visa_product_id =").append(activityId)
			   .append(" AND o.payStatus NOT IN (7, 99, 111) AND o.visa_order_status <> 100 AND o.settle_locked_in is null");
		}else if (orderType == Context.ORDER_TYPE_ISLAND){
			sql.append("SELECT IFNULL(SUM(m.amount * m.exchangerate),0) FROM island_money_amount m,");
			sql.append("island_order o WHERE o.accounted_money = m.serialNum AND o.delFlag ='0' AND o.activity_island_group_uuid = '")
			   .append(activityId).append("'");
		}else if (orderType == Context.ORDER_TYPE_HOTEL){
			sql.append("SELECT IFNULL(SUM(m.amount * m.exchangerate),0) FROM hotel_money_amount m,");
			sql.append("hotel_order o WHERE o.accounted_money = m.serialNum AND o.delFlag ='0' AND o.activity_hotel_group_uuid = '")
			   .append(activityId).append("'");
		}else {
			sql.append("SELECT IFNULL(SUM(m.amount * m.exchangerate),0) FROM money_amount m,");
			sql.append("productorder o WHERE o.accounted_money = m.serialNum AND o.delFlag = '0' AND o.productGroupId = ").append(activityId)
			   .append(" AND o.payStatus NOT IN (7, 99, 111) AND o.settle_locked_in is null AND o.orderStatus = ").append(orderType);
		}
		List<Object> total = receivePayDao.findBySql(sql.toString());
		if (total.get(0) != null){
			return new BigDecimal(total.get(0).toString());
		}
		return new BigDecimal(0);
	}

	/**
	 * 查询拉美图实际其他收入收款之和。
	 * @author yudong.xu --2016/4/14--20:01
	 */
	public BigDecimal getOtherIncomeForLMT(Integer orderType,String activityId){
		StringBuilder sql = new StringBuilder();
		if (orderType == Context.ORDER_TYPE_HOTEL){
			sql.append("SELECT ROUND(SUM(m.amount * m.exchangerate),2) AS otherIncome FROM pay_group p");
			sql.append(" LEFT JOIN money_amount m ON m.serialNum = p.payPrice")
			   .append(" LEFT JOIN cost_record_hotel c ON p.cost_record_id=c.id AND p.orderType=c.orderType")
			   .append(" WHERE p.isAsAccount = 1 AND c.delFlag='0' AND c.budgetType=2")
			   .append(" AND c.orderType =").append(orderType)
			   .append(" AND c.activity_uuid = '").append(activityId).append("'");
		} else if (orderType == Context.ORDER_TYPE_ISLAND){
			sql.append("SELECT ROUND(SUM(m.amount * m.exchangerate),2) AS otherIncome FROM pay_group p");
			sql.append(" LEFT JOIN money_amount m ON m.serialNum = p.payPrice")
			   .append(" LEFT JOIN cost_record_island c ON p.cost_record_id=c.id AND p.orderType=c.orderType")
			   .append(" WHERE p.isAsAccount = 1 AND c.delFlag='0' AND c.budgetType=2")
			   .append(" AND c.orderType =").append(orderType)
			   .append(" AND c.activity_uuid = '").append(activityId).append("'");
		} else {
			sql.append("SELECT ROUND(SUM(m.amount * m.exchangerate),2) AS otherIncome FROM pay_group p");
			sql.append(" LEFT JOIN money_amount m ON m.serialNum = p.payPrice")
			   .append(" LEFT JOIN cost_record c ON p.cost_record_id=c.id AND p.orderType=c.orderType")
			   .append(" WHERE p.isAsAccount = 1 AND c.delFlag='0' AND c.budgetType=2")
			   .append(" AND c.orderType =").append(orderType)
			   .append(" AND c.activityId = ").append(activityId)
			   .append(" AND c.settle_locked_in is null");
		}
		List<Object> total = receivePayDao.findBySql(sql.toString());
		if (total.get(0) != null){
			return new BigDecimal(total.get(0).toString());
		}
		return new BigDecimal(0);
	}


	/**
	 * 还款日期到期时
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public void getRemaindListByRefundDate() {
		Session session = receivePayDao.getSession();

		// 获取所有 借款审批、签证借款审批的数据。 针对每一条数据生成message
		List<ReviewNew> allBorrowingList = new ArrayList<>();
		// 获取 单团、散拼、游学、自由行、大客户、游轮、签证、机票  的借款审批，已审批通过。
		allBorrowingList.addAll(newReviewService.getReviewList(Integer.parseInt(Context.ACTIVITY_KINDS_DT), Context.REVIEW_FLOWTYPE_BORROWMONEY, ReviewConstant.REVIEW_STATUS_PASSED));  // 单团
		allBorrowingList.addAll(newReviewService.getReviewList(Integer.parseInt(Context.ACTIVITY_KINDS_SP), Context.REVIEW_FLOWTYPE_BORROWMONEY, ReviewConstant.REVIEW_STATUS_PASSED));  // 散拼
		allBorrowingList.addAll(newReviewService.getReviewList(Integer.parseInt(Context.ACTIVITY_KINDS_YX), Context.REVIEW_FLOWTYPE_BORROWMONEY, ReviewConstant.REVIEW_STATUS_PASSED));  // 游学
		allBorrowingList.addAll(newReviewService.getReviewList(Integer.parseInt(Context.ACTIVITY_KINDS_ZYX), Context.REVIEW_FLOWTYPE_BORROWMONEY, ReviewConstant.REVIEW_STATUS_PASSED));  // 自由行
		allBorrowingList.addAll(newReviewService.getReviewList(Integer.parseInt(Context.ACTIVITY_KINDS_DKH), Context.REVIEW_FLOWTYPE_BORROWMONEY, ReviewConstant.REVIEW_STATUS_PASSED));  // 大客户
		allBorrowingList.addAll(newReviewService.getReviewList(Integer.parseInt(Context.ACTIVITY_KINDS_QZ), Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY, ReviewConstant.REVIEW_STATUS_PASSED));  // 签证
		allBorrowingList.addAll(newReviewService.getReviewList(Integer.parseInt(Context.ACTIVITY_KINDS_QZ), Context.REVIEW_FLOWTYPE_BORROWMONEY, ReviewConstant.REVIEW_STATUS_PASSED));  // 新行者签证
		allBorrowingList.addAll(newReviewService.getReviewList(Integer.parseInt(Context.ACTIVITY_KINDS_JP), Context.REVIEW_FLOWTYPE_BORROWMONEY, ReviewConstant.REVIEW_STATUS_PASSED));  // 机票
		allBorrowingList.addAll(newReviewService.getReviewList(Integer.parseInt(Context.ACTIVITY_KINDS_YL), Context.REVIEW_FLOWTYPE_BORROWMONEY, ReviewConstant.REVIEW_STATUS_PASSED));  // 游轮

		if (CollectionUtils.isNotEmpty(allBorrowingList)) {
			// 判断该批次是否已经执行了操作
			Map<String, Boolean> batchOperatedMap = new HashMap<>();
			for (ReviewNew reviewNew : allBorrowingList) {

				try {

					// 借款审批类型
					Integer borrowType = 1;
					if (Context.ACTIVITY_KINDS_DT.equals(reviewNew.getProductType())
							|| Context.ACTIVITY_KINDS_SP.equals(reviewNew.getProductType())
							|| Context.ACTIVITY_KINDS_YX.equals(reviewNew.getProductType())
							|| Context.ACTIVITY_KINDS_ZYX.equals(reviewNew.getProductType())
							|| Context.ACTIVITY_KINDS_DKH.equals(reviewNew.getProductType())
							|| Context.ACTIVITY_KINDS_YL.equals(reviewNew.getProductType())) {
						borrowType = 1;  // 1.单团借款
					} else if (Context.ACTIVITY_KINDS_JP.equals(reviewNew.getProductType())) {
						borrowType = 2;  // 2.机票借款
					} else if (Context.ACTIVITY_KINDS_QZ.equals(reviewNew.getProductType()) && Context.REVIEW_FLOWTYPE_BORROWMONEY.toString().equals(reviewNew.getProcessType())) {
						borrowType = 3;  // 3.新行者借款                                 业务特点： 1.按单团借款规则进行借款审批 2.审批相关页面跟单团、跟签证不同
					} else if (Context.ACTIVITY_KINDS_QZ.equals(reviewNew.getProductType()) && Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY.toString().equals(reviewNew.getProcessType())) {
						borrowType = 4;  // 4.签证借款(新行者除外)   业务特点：1.按批次进行借款审批 2.refundDate存储在批次表中，不在review及map中
					} else {
						continue;
					}
					// 获取签证借款的批次信息
					String batchNo = reviewNew.getBatchNo();
					if (StringUtils.isNotBlank(batchNo)) {
						// 先判断该批次是否在批次操作map中，不在则初始化为false，在则直接跳过。
						if (batchOperatedMap.containsKey(batchNo) && batchOperatedMap.get(batchNo) == true) {
							continue;
						}
						batchOperatedMap.put(batchNo, false);
					}
					// 判断该批次是否已经执行了操作
					// 获取 业务dataMap
					Map<String, Object> borrowingmMap = newReviewService.getReviewDetailMapByReviewId(reviewNew.getId());
					if (borrowingmMap != null && borrowingmMap.size() > 0) {

						// 订单所属批发商
						String companyUuid = borrowingmMap.get("companyId").toString();
						if (StringUtils.isBlank(companyUuid)) {
							continue;
						}
						// 审批创建人
						String reviewCreateBy = borrowingmMap.get("createBy").toString();
						if (StringUtils.isBlank(reviewCreateBy)) {
							continue;
						}
						Office office = officeService.findWholeOfficeByUuid(companyUuid);
						if (office == null) {
							continue;
						}
						// 订单编号
						String orderNum = borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO) == null ? null : borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO).toString();
						// 订单id
						String orderId = borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID) == null ? null : borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID).toString();
						// 团期id
						String groupIdStr = borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID) == null ? null : borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID).toString();
						// 产品类型
						String productType = borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE) == null ? null : borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE).toString();
						// 产品id
						String productId = borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID) == null ? null : borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID).toString();
						// 产品名称
						String productName = borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME) == null ? null : borrowingmMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME).toString();
						if (StringUtils.isBlank(productName)) {
							productName = "机票";
						}
						//机票可以没有团
						if (((borrowType == 1 || borrowType == 3) && StringUtils.isBlank(orderId)) || (borrowType == 1 && StringUtils.isBlank(groupIdStr)) || StringUtils.isBlank(productId) || StringUtils.isBlank(productType)) {
							continue;
						}

						// 还款日期
						String refundDateStr = "";
						if (borrowType != 4) {
							refundDateStr = borrowingmMap.get(Context.REVIEW_VARIABLE_KEY_REFUND_DATE) == null ? "" : borrowingmMap.get(Context.REVIEW_VARIABLE_KEY_REFUND_DATE).toString();
						} else {
							// 获取批次号对应的 还款日期
							VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchNo, "2");
							if (visaFlowBatchOpration != null && visaFlowBatchOpration.getRefundDate() != null) {
								refundDateStr = DateUtils.formatCustomDate(visaFlowBatchOpration.getRefundDate(), DateUtils.DATE_PATTERN_YYYY_MM_DD);
							}
						}
						if (StringUtils.isBlank(refundDateStr)) {  // 无还款日期：不生成提醒
							continue;
						}
						// 其他字段 TODO

						// 判断是否生成提醒
						// 根据批发商id，提醒类型，获取 批发商下所有正常的remindRules（提醒规则）
						List<Remind> remindRules = remindService.getRemindRulesByCompany(office.getUuid(), Context.REMIND_TYPE_REFUND);
						if (CollectionUtils.isEmpty(remindRules)) {  // 无规则：不生成提醒
							continue;
						}
						// 根据批发商id，提醒类型，获取批发商下，某中类型的提醒规则涉及到的所有产品类型
						Set<Integer> productTypes = remindService.getProTypesByCompany(office.getUuid(), Context.REMIND_TYPE_REFUND);
						if (CollectionUtils.isEmpty(productTypes) || !productTypes.contains(Integer.parseInt(productType))) {  // 无产品线集合，或者本审批数据不在产品线集合中：不生成提醒
							continue;
						}
						// 遍历提醒规则
						for (Remind remind : remindRules) {
							boolean isContainGroup = false;  // 是否包含本团期
							// 单团：根据提醒规则id、产品类型，获取该规则下所有团期
							if (Context.ACTIVITY_KINDS_DT.equals(reviewNew.getProductType())
									|| Context.ACTIVITY_KINDS_SP.equals(reviewNew.getProductType())
									|| Context.ACTIVITY_KINDS_YX.equals(reviewNew.getProductType())
									|| Context.ACTIVITY_KINDS_ZYX.equals(reviewNew.getProductType())
									|| Context.ACTIVITY_KINDS_DKH.equals(reviewNew.getProductType())
									|| Context.ACTIVITY_KINDS_YL.equals(reviewNew.getProductType())) {

								List<ActivityGroup> groups = remindService.getGroupsByRemindRule(remind.getId());
								if (CollectionUtils.isEmpty(groups)) {  // 无团期集合：不生成提醒
									continue;
								}
								for (ActivityGroup activityGroup : groups) {
									if (groupIdStr.equals(activityGroup.getId().toString())) {
										isContainGroup = true;
										break;
									}
								}
							}
							// 签证、机票：直接取产品
							else if (Context.ACTIVITY_KINDS_JP.equals(reviewNew.getProductType())
									|| Context.ACTIVITY_KINDS_QZ.equals(reviewNew.getProductType())) {
								// 根据提醒规则获取产品ids，并组成list
								List<String> productIds = remindService.getProductIdListByRemindRule(remind.getId());
								if (productType.equals(remind.getSelectedRemindOrderType()) && CollectionUtils.isNotEmpty(productIds)) {  // 对应产品类型
									if (productIds.contains(productId)) {
										isContainGroup = true;
									}
								}
							} else {
								// TOOD 其他产品线，暂时未知
							}
							if (!isContainGroup) {  // 本审批数据不在团期集合中：不生成提醒
								continue;
							}

							//时间判断 (应还款日期 +- 提醒起始天数 <= 当前日期 <= 应还款日期 +- 提醒过期天数)
							try {
								Date refundDate = DateUtils.parseDate(refundDateStr);  // 还款日期
								Date nowDate = new Date();  // 当前日期
								// 计算提醒起始日期
								Calendar calendarS = Calendar.getInstance();
								calendarS.setTime(refundDate);
								if (remind.getStartRemindStatus() == 1) {
									calendarS.add(Calendar.DATE, remind.getStartRemindDays());
								} else if (remind.getStartRemindStatus() == -1) {
									calendarS.add(Calendar.DATE, 0 - remind.getStartRemindDays());
								} else {
									continue;
								}
								Date startDate = calendarS.getTime();  // 提醒起始日期
								if (nowDate.before(startDate)) {  // 未到提醒起始日期：不生成提醒
									continue;
								}
								// 计算提醒过期日期
								Calendar calendarE = Calendar.getInstance();
								calendarE.setTime(refundDate);
								if (remind.getEndRemindStatus() == 1) {
									calendarE.add(Calendar.DATE, remind.getEndRemindDays());
								} else if (remind.getEndRemindStatus() == -1) {
									calendarE.add(Calendar.DATE, 0 - remind.getEndRemindDays());
								} else {
									continue;
								}
								Date endDate = calendarE.getTime();  // 提醒过期日期
								if (nowDate.after(endDate)) {  // 已过提醒结束日期：不生成提醒，且要删除原有提醒
									List<MsgEntity> msgEntityList = msgEntityDao.findMsgEntity(reviewNew.getId(), remind.getId());  // 根据审批id，规则id，查找msg。然后更新, 如果不存在则新增
									if (CollectionUtils.isNotEmpty(msgEntityList)) {
										for (MsgEntity msgEntity : msgEntityList) {											
											msgEntity = msgEntityList.get(0);
											msgEntity.setDelFlag(Context.DEL_FLAG_DELETE);
											session.save(msgEntity);  // 日期已过，取消提醒
											session.flush();
										}
									}
									continue;
								}
								// 当前时间在 [提醒起始时间, 提醒到期时间]之间 。 正常生成或更新提醒。
								if ((nowDate.getTime() == startDate.getTime() || nowDate.after(startDate)) && nowDate.before(endDate)) {
									List<MsgEntity> msgEntityList = msgEntityDao.findMsgEntity(reviewNew.getId(), remind.getId());  // 根据审批id，规则id，查找msg。然后更新, 如果不存在则新增
									// 新增 / 更新 所需要处理的业务不一致，需要区分
									boolean isAlreadyExist = false;
									MsgEntity msgEntity = null;
									if (CollectionUtils.isNotEmpty(msgEntityList)) {
										msgEntity = msgEntityList.get(0);
									}
									if (msgEntity == null) {
										msgEntity = new MsgEntity();
									} else {
										isAlreadyExist = true;
									}
									// msg的其他主要信息
									String title = "";
									String ordreOrBatch = borrowType == 4 ? "批次" : "订单";  // 订单 or 批次
									String ordreNoOrBatchNo = borrowType == 4 ? batchNo : orderNum;  // 订单编号 or 批次号
									String beforeOrAfter = refundDate.after(nowDate) ? "还剩" : "已过";  // 还剩、已过
									// 计算还款日期与当前日期的差额
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(nowDate);
									int nowDay = calendar.get(Calendar.DAY_OF_YEAR);
									calendar.setTime(refundDate);
									int refundDay = calendar.get(Calendar.DAY_OF_YEAR);
									int diffDay = nowDay - refundDay;  // 天数差值
									title = productName + "借款" + ordreOrBatch + "#" + ordreNoOrBatchNo + "#" + "未还款，距还款日期" + beforeOrAfter + Math.abs(diffDay) + "天" + "#" + borrowType + "#" + orderId + "#" + reviewNew.getId();
									msgEntity.setTitle(title);  // 标题
									msgEntity.setStatus(Context.MESSAGE_STATUS_ISSUE);  //消息状态
									msgEntity.setMsgType(Context.MSG_TYPE_REFUND);  //消息类型
									msgEntity.setMsgNoticeType(Integer.parseInt(productType));  //公告产品线类型
									msgEntity.setCompanyId(office.getId());  // 批发商
									if (!isAlreadyExist) {										
										msgEntity.setCreateDate(nowDate);  // 创建
										msgEntity.setCreateBy(Long.parseLong(reviewCreateBy));
									}
									msgEntity.setUpdateDate(nowDate);  // 更新
									msgEntity.setUpdateBy(Long.parseLong(reviewCreateBy));
									msgEntity.setDelFlag(Context.DEL_FLAG_NORMAL);
									msgEntity.setSysRemindId(remind.getId());
									msgEntity.setBusinessData(reviewNew.getId());
									msgEntity.setBusinessType(Context.MSG_TYPE_REFUND);
									
									// 获取提醒可见人集合。并把msg与msg_user_mark一起保存
									Set<User> usersSet = new HashSet<>();
									usersSet.addAll(remind.getUserList());  // 选择人
									// 借款审批申请人（必有）、审批人（选填）
									usersSet.add(userDao.findById(Long.parseLong(reviewCreateBy)));  //  审批申请人
									if (remind.getIsVisible4Reviewer() == 1) {
										List<ReviewLogNew> reviewLogNews = reviewService.getReviewLogByReviewId(reviewNew.getId());
										if (CollectionUtils.isNotEmpty(reviewLogNews)) {
											for (ReviewLogNew reviewLogNew : reviewLogNews) {
												usersSet.add(userDao.findById(Long.parseLong(reviewLogNew.getCreateBy())));  //  审批者
											}
										}
									}
									List<User> remindUsers = new ArrayList<>(usersSet);
									if (CollectionUtils.isNotEmpty(remindUsers)) {
										// 保存消息
										session.save(msgEntity);
										session.flush();
										// 只有新增msg才需要新增 userMark; 如果已经存在提醒，只是做了更新并未新生成msg，不需要更新 userMark。
										if (!isAlreadyExist) {
											// 循环保存可见人对应表
											try{
												for (User user : remindUsers) {
													//保存用户标示表
													MsgMarkEntity msgUserMark = new MsgMarkEntity();
													msgUserMark.setIfRead(0);  // 未读、已读
													msgUserMark.setIsShow(0);  // 展示
													msgUserMark.setMsgAnnouncementId(msgEntity.getId());
													msgUserMark.setUserId(user.getId());
													msgUserMark.setCreateDate(nowDate);
													msgUserMark.setDelFlag(Context.DEL_FLAG_NORMAL);
													session.save(msgUserMark);
												}
												// 把该条批次对应的map值置为 已操作true
												batchOperatedMap.put(batchNo, true);
											}catch (Exception e) {
												continue;
											}
										}
									}
								}
							} catch (Exception e) {
								continue;
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}
	}

	/**
	 * 针对拉美图的该团下实际返佣总额。
	 * @author yudong.xu --2016/4/14--17:09
	 */
	public BigDecimal getRealRebatesForLMT(Integer orderType,String activityId){
		StringBuilder sql = new StringBuilder();
		if (orderType == Context.ORDER_TYPE_ISLAND){
			sql.append("SELECT SUM(c.price) AS price FROM cost_record_island c WHERE c.activity_uuid ='").append(activityId).append("'")
			   .append(" AND c.orderType=").append(orderType);
		} else if (orderType == Context.ORDER_TYPE_HOTEL){
			sql.append("SELECT SUM(c.price) AS price FROM cost_record_hotel c WHERE c.activity_uuid ='").append(activityId).append("'")
			   .append(" AND c.orderType=").append(orderType);
		}else {
			sql.append("SELECT SUM(c.price) AS price FROM cost_record c WHERE c.activityId =").append(activityId)
			   .append(" AND c.orderType=").append(orderType)
			   .append(" AND c.settle_locked_in is null ");
		}
		sql.append(" AND c.delFlag ='0' AND c.reviewType=2 AND c.budgetType=1 AND c.reviewStatus in ('审批通过', '审核通过')");
		List<Object> total = receivePayDao.findBySql(sql.toString());
		if (total.get(0) != null){
			return new BigDecimal(total.get(0).toString());
		}
		return new BigDecimal(0);
	}

	/**
	 * 针对拉美图，查询其对应团号下的所有境、境外实际支付之和。
	 * @author yudong.xu --2016/4/14--17:32
	 */
	public BigDecimal getRealPayForLMT(Integer orderType,String activityId){
		StringBuilder sql = new StringBuilder();
		Integer whenToSheet = UserUtils.getUser().getCompany().getActualCostWhenUpdate();//当配置为1时，把待审核的成本过滤掉
		sql.append("SELECT SUM(c.priceAfter) AS cost FROM cost_record c WHERE c.activityId =").append(activityId)
		   .append(" AND c.orderType=").append(orderType)
		   .append(" AND c.delFlag ='0' AND c.reviewType=0 AND c.budgetType=1 AND c.settle_locked_in is null");
		if (whenToSheet == 1){
			sql.append(" AND c.review not in(0,4,5)");
		}else if (whenToSheet == 2){
			sql.append(" AND c.payReview in(1,2) AND c.review <> 0");
		}
		List<Object> total = receivePayDao.findBySql(sql.toString());
		if (total.get(0) != null){
			return new BigDecimal(total.get(0).toString());
		}
		return new BigDecimal(0);
	}

	/**
	 * 针对拉美图的团期实际总退款
	 * @author yudong.xu --2016/4/14--19:59
	 */
	public BigDecimal getRefundForLMT(Integer orderType,String activityId){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUM(c.price) AS refund FROM ");
		if (orderType == Context.ORDER_TYPE_ISLAND){
			sql.append("cost_record_island c")
			   .append(" WHERE c.activity_uuid ='").append(activityId).append("'")
			   .append(" AND c.orderType=").append(orderType);
		}else if (orderType == Context.ORDER_TYPE_HOTEL){
			sql.append("cost_record_hotel c")
			   .append(" WHERE c.activity_uuid ='").append(activityId).append("'")
			   .append(" AND c.orderType=").append(orderType);
		}else {
			sql.append("cost_record c")
			   .append(" WHERE c.activityId =").append(activityId)
			   .append(" AND c.orderType=").append(orderType);
		}
		sql.append(" AND c.reviewType=1 AND c.delFlag='0' AND c.budgetType=1 AND c.reviewStatus in ('审批通过', '审核通过') AND c.settle_locked_in is null ");
		List<Object> total = receivePayDao.findBySql(sql.toString());
		if (total.get(0) != null){
			return new BigDecimal(total.get(0).toString());
		}
		return new BigDecimal(0);
	}


}
