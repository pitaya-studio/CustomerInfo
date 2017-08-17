package com.trekiz.admin.modules.review.visaborrowmoney.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.formBean.PrintParamBean;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.visaborrowmoney.repository.IVisaBorrowMoneyDao;
import com.trekiz.admin.modules.review.visaborrowmoney.web.VisaBorrowMoneyController;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewCompany;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewFlow;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewFlowDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.RoleDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SysBatchNoService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.web.VisaPreOrderController;

import freemarker.template.TemplateException;

@Service
public class VisaBorrowMoneyServiceImpl implements IVisaBorrowMoneyService {
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy 年 MM 月 dd 日");
	
	
	@Autowired
	private VisaOrderDao visaOrderDao;
	
	@Autowired
	private TravelerDao travelerDao;
	
	@Autowired
	private ReviewLogDao reviewLogDao;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private ReviewRoleLevelDao reviewRoleLevelDao;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private ReviewFlowDao reviewFlowDao;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private  IVisaBorrowMoneyDao visaBorrowMoneyDao;

	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
    private SysBatchNoService sysBatchNoService;
	
	@Autowired
	private VisaProductsService visaProductsService;
	
	@Autowired
	private ReviewDetailDao reviewDetailDao;

	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	
	@Autowired
	private VisaProductsDao visaProductsDao;
	
	@Autowired
	private RefundService refundService;

	@Autowired
	private CurrencyService currencyService;

	/**
	 * 签证借款审核列表
	 *  xinwei.wang added
	 *  2014年12月24日16:23:15
	 */
	@Override
	public Page<Map<String, Object>> queryVisaBorrowMoneyReviewInfo(HttpServletRequest request, HttpServletResponse response, String deFaultUserJobid) {
		
		String groupCode = request.getParameter("groupCode");
		if(groupCode == null || "".equals(groupCode.trim())){
			groupCode = null;
		}
		String startTime = request.getParameter("startTime");
		if(startTime == null || "".equals(startTime.trim())){
			startTime = null;
		}
		String endtime = request.getParameter("endTime");
		if(endtime == null || "".equals(endtime.trim())){
			endtime = null;
		}
		String agent = request.getParameter("channel");
		if(agent == null || "".equals(agent.trim())){
			agent = null;
		}
		
		//--------下单人   wxw 2015-08-20 added-----------
		String orderCreateBy = request.getParameter("orderCreateBy");
		if(orderCreateBy == null || "".equals(orderCreateBy.trim())){
			orderCreateBy = null;
		}
		
		String saler = request.getParameter("saler");
		if(saler == null || "".equals(saler.trim())){
			saler = null;
		}
		String jdsaler = request.getParameter("meter");
		if(jdsaler == null || "".equals(jdsaler.trim())){
			jdsaler = null;
		}
	
		String statusChoose = request.getParameter("statusChoose");
		if(statusChoose == null || ("all".equals(statusChoose.trim()))){
			statusChoose = null;
		}
		
		String visaType = request.getParameter("visaType");
		if(visaType == null || "".equals(visaType.trim())){
			visaType = null;
		}
		String userJobIdStr = request.getParameter("userJobId");
		UserJob userJob = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		//获取当前用户的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		if(userJobs == null || userJobs.size() == 0) {
			return null;
		}
		
        Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
		
		//获取第一层级部门id
		Long pDeptId;
		//子部门id
		List<Long> subIds = new ArrayList<Long>();
		//如果userJob为null则表明是第一次进入审核页面  默认取第一个userJob
		if(userJob == null){
			userJob = userJobs.get(0);
		}
		
		if(userJob.getDeptLevel() == 1){
			pDeptId = userJob.getDeptId();
			subIds = departmentDao.findSubidsByParentId(pDeptId);
		} else {
			pDeptId = userJob.getParentDept();
			subIds.add(userJob.getDeptId());
		}
		//获取reviewComppanyid
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		if(levels == null || levels.size() == 0){
			return null;
		}
		
		//-------added for adapt deptId  2015-2-6--------
		// 1调用dao查询审核列表
		Page<Map<String, Object>> reviewPage = visaBorrowMoneyDao.queryVisaBorrowMoneyReviewList(request, response, groupCode,
						startTime, endtime, agent, orderCreateBy,  saler, jdsaler, statusChoose, visaType,levels, userJob, reviewCompanyId,subIds);
		// 2 调用review接口查询审核信息
		//List<Map<String,String>> list = reviewService.findReviewCompanyListMap(Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY,false, subIds);
		// 3 整合信息
		List<Map<String, Object>> reviewList = reviewPage.getList();
		for(Map<String, Object> tMap : reviewList){
			tMap.put("revLevel", levels.get(0));
			//start 把游客的结算价的UUID转化为多币种的款项值
	/*		String payprice = tMap.get("payprice") == null ? null : tMap.get("payprice").toString();
			payprice = moneyAmountService.getMoney(payprice);
			tMap.remove("payprice");
			tMap.put("payprice", payprice);
			//end 把游客的结算价的UUID转化为多币种的款项值
			for(Map<String, String> tList: list){
				String revid = tMap.get("revid").toString();
				String id = tList.get("id");
				if(revid != null && !"".equals(revid.trim()) && id !=null && (revid.trim()).equals(id.trim())) {
					tMap.putAll(tList);
				}
			}*/
			
			String revid = tMap.get("revid").toString();
			Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
			tMap.put("createReason", reviewAndDetailInfoMap.get("createReason"));
			
		}
		
		Iterable<ReviewLog> all = reviewLogDao.findAll();
		Iterator<ReviewLog> iterator = all.iterator();
		List<ReviewLog> listReLog = new ArrayList<ReviewLog>();
		ReviewLog next;
		while(iterator.hasNext()){
			next = iterator.next();
			listReLog.add(next);
		}
	/*	for(Map<String, Object> tMap : reviewList){
			for(ReviewLog tempRevLog : listReLog){
				String revid = tMap.get("revid").toString();
				String id = tempRevLog.getReviewId().toString();
				if(revid != null && !"".equals(revid.trim()) && id !=null && (revid.trim()).equals(id.trim())) {
					
					//-------added for adapt deptId  2015-2-6--------
					if(!isInLevel(tempRevLog.getNowLevel(), levels) ){
						tMap.remove("revstatus");//审核状态
						tMap.put("revstatus", tempRevLog.getResult()); //状态转换为log中相应汉字
//						tMap.remove("lastoperator");//上一环节操作人
//						tMap.put("lastoperator", tempRevLog.getCreateBy());
					}
					if(levels.get(0) == 1){
						tMap.put("lastoperator", "");
					}
					
					if(tempRevLog.getNowLevel() == levels.get(0) - 1){
						tMap.put("lastoperator", tempRevLog.getCreateBy());
					}
				}
			}
		}*/
		
		reviewPage.setList(reviewList);
		return reviewPage;
	}

	
	/**
	 * 私有方法 判断这个level是否在level层级中
	 * @param nowLevel
	 * @param list2
	 * @return
	 */
/*	private boolean isInLevel(Integer nowLevel, List<Integer> list2) {
		for(Integer tempNum : list2){
			if(nowLevel == tempNum){
				return false;
			}
		}
		return true;
	}*/
	
	/**
     * @param productType:产品类型
	 * @param flowType: 流程类型
	 * @param level:审核层级
	 * @return 审核层级对应的角色
	 */
	@Override
	public Role getWorkFlowRoleByFlowTypeAndLevel(Integer productType,Integer flowType,Integer level) {
		
		//1.根据flowType 和 产品类型找出  review_flow 的ID
		List<ReviewFlow> reviewFlows = reviewFlowDao.findReviewFlow(productType, flowType);
		long flowId=0;
		if (null!=reviewFlows&&reviewFlows.size()>0) {
			flowId = reviewFlows.get(0).getId();
		}else {
			return null;
		}
		//2.根据reviewID 和 companyID 查找  review_company的ID
		long companyId = UserUtils.getUser().getCompany().getId();
		
		long reviewCompanyId = 0;
		List<ReviewCompany> reviewCompanies = reviewCompanyDao.findReviewCompany(companyId, flowId);
		if (null!=reviewCompanies&&reviewCompanies.size()>0) {
			reviewCompanyId= reviewCompanies.get(0).getId();
		}else {
			return null;
		}
		//3.过滤流程相关的角色		
		Long reviewRoleID = reviewRoleLevelDao.findRoleIdsByreviewCompanyIdAndLevel(reviewCompanyId,level);
		Role role = roleDao.findOne(reviewRoleID);
		return role;
	}
	
	
	/**
	 * 生成签证借款单文件
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public File createVisaBorrowMoneySheetDownloadFile(Long revid) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(revid);
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate"))));// 填写日期
			
			// --- wangxinwei 20151008 added：需求C221，处理确认付款日期
			root.put("revUpdateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"))));// 更新日期
			
			root.put("revBorrowRemark",reviewAndDetailInfoMap.get("borrowRemark"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			root.put("productCreater", productCreater);
			
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			root.put("payDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"))));// 付款日期
			root.put("revBorrowAmount", MoneyNumberFormat.getThousandsMoney(Double.valueOf(reviewAndDetailInfoMap.get("borrowAmount")), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 借款金额
		}
		//出纳以外的最后一个审批人：对签证借款流程来说level为3
		//Long userCompanyId = UserUtils.getUser().getCompany().getId();
		//int toplivelLong = reviewCompanyDao.findTopLevel(userCompanyId, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);//
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(revid);
		
		if (null!=reviewLogs&&reviewLogs.size()>0) {
			User user = UserUtils.getUser(reviewLogs.get(reviewLogs.size()-1).getCreateBy());
			if (null!=user) {
				root.put("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			}
		}
		root.put("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(reviewAndDetailInfoMap.get("borrowAmount"))));// 借款金额大写
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY,reviewLogs);
		
		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户财务主管为空
		 */
		if (null!=jobtypeusernameMap.get(9)) {//财务主管
			if (68!=UserUtils.getUser().getCompany().getId()) {
				root.put("cwmanager", jobtypeusernameMap.get(9));
			}else {
				root.put("cwmanager", "");
			}
		}else {
			root.put("cwmanager", "");
		}
		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			root.put("cw", jobtypeusernameMap.get(8));
		}else {
			root.put("cw", "");
		}
		
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (null!=jobtypeusernameMap.get(6)) {//出纳
			if (68!=UserUtils.getUser().getCompany().getId()) {
				root.put("cashier", jobtypeusernameMap.get(6));
			}else {
				root.put("cashier", "");
			}
		}else {
			root.put("cashier", "");
		}
		
		//model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
		if (null!=jobtypeusernameMap.get(10)) {//总经理
			root.put("majorCheckPerson", jobtypeusernameMap.get(10));
		}else {
			root.put("majorCheckPerson", "");
		}
		
		if (null!=jobtypeusernameMap.get(7)) {//部门经理
			root.put("deptmanager", jobtypeusernameMap.get(7));
		}else {
			root.put("deptmanager", "");
		}
		
		//----- wxw added 20151008 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
		//----- C221 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		Long companyId = UserUtils.getUser().getCompany().getId();	
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT rev.payStatus FROM review rev WHERE rev.id =");
		buffer.append("'"+revid+"'");
		String payStatus = null;
		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		if (null!=list&&list.size()>0) {
			payStatus = list.get(0).get("payStatus").toString();
			if (companyId==88||companyId==68) {
				root.put("payStatus","0");
			}else {
				root.put("payStatus",payStatus);
			}
			
		}else {
			root.put("payStatus","0");
		}
		
		
	   return FreeMarkerUtil.generateFile("visaborrowmoney.ftl", "visaborrowmoney.doc", root);
	}	
	
	/**
	 * 如果为第一次打印需要更新打印状态
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public void updateReviewPrintInfoById(Long revid,Date printdate) throws Exception{
		 Review review =  reviewDao.findOne((revid));
		 if (null!=review&&null==review.getPrintFlag()) {
			 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			 Date date = sf.parse(sf.format(printdate));
			 reviewDao.updateReviewPrintInfoById(revid, date, 1, UserUtils.getUser().getId());
			
		 }
	}
	
	/**
	 * 签证批量借款
	 * @author wxw
	 * @param visaIds 签证id 以','分割
	 * @param travellerIds 游客id 以','分割
	 * @param persons 借款人 以','分割
	 * @param dates 日期 以','分割
	 * @param moneys 金额 以','分割
	 * @param others 备注 以','分割
	 * @return map
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Map<String, Object> visaBatchJk(String visaIds, String travellerIds,
			String persons, String dates, String moneys, String others) {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap= null;
		
		String[] travelerIDS = travellerIds.split(",");
		String[] borrowAmounts = moneys.split(",");
		String[] borrowRemarks = others.split(",");
		String[] borrowTimes = dates.split(",");
		
		//获取人民币币种id
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
		buffer.append(" AND c.create_company_id=");
		buffer.append(UserUtils.getUser().getCompany().getId());
		List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
		Integer borrowtotalcurrencyId = 0;
		for (int i = 0; i < currencylist.size(); i++) {
			if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
				borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
				break;
			}
		}
		
		//String batchNo =   sysBatchNoService.getVisaBorrowMoneyBatchNo();
		 try{
			    /*
			     * 同一批次的借款用同一个批次号，作为业务数据保存在表review_detail中，mykey的值为visaBorrowMoneyBatchNo
			     */
			    String batchNo =   sysBatchNoService.getVisaBorrowMoneyBatchNo();
			    //System.out.println(2/0);
				//签证批量借款
		        for (int i = 0; i < travelerIDS.length; i++) {
		        	
		        	//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
		        	if (!"0".equals(travelerIDS[i])) { 
		        		//申请时的addReview 要用添加dept
		        		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
		        		Long deptId = visaOrderService.getProductPept(traveler.getOrderId());
		        		
		        		List<Detail> listDetail = new ArrayList<Detail>();
		        		listDetail.add(new Detail("borrowAmount", borrowAmounts[i]));
		        		listDetail.add(new Detail("currencyId", borrowtotalcurrencyId+""));//默认借款币种为RMB
		        		listDetail.add(new Detail("borrowRemark", borrowRemarks[i]));
		        		listDetail.add(new Detail("borrowTime", borrowTimes[i]));//新增借款时间
		        		listDetail.add(new Detail("visaBorrowMoneyBatchNo", batchNo));
		        		
		        		StringBuffer reply = new StringBuffer("签证批量借款");
		        		long  addresult = reviewService.addReview(VisaBorrowMoneyController.VISA_PRODUCT_TYPE, //产品类型 
		        				VisaBorrowMoneyController.VISA_JK_FLOW_TYPE,//流程类型  flowtype
		        				traveler.getOrderId()+"",//订单ID
		        				traveler.getId().longValue(),//游客ID
		        				Long.parseLong("0"), //新提交的审核请置 0. 重新提交审核时,等于上次审核记录的主键
		        				borrowRemarks[i],//创建原因
		        				reply, 
		        				listDetail,
		        				deptId);
		        		if (addresult!=0) {
		        			reply.append("申请成功");
		        		}
					}
				}
		        
		 }catch(Exception e){
			 e.printStackTrace();
			 if(resultMap==null){
				 resultMap = new HashMap<String,Object>();
			 }
			 resultMap.put("msg", "批量借款申请失败！");
		 }
		return resultMap;
	}
    
    /*********************   新行者借款相关     开始      *********************/
	/*********************   新行者借款相关     开始      *********************/
	/*********************   新行者借款相关     开始      *********************/
	
	
	/**
	 * 签证借款审核列表
	 *  xinwei.wang added
	 *  2015年05月02日
	 */
	@Override
	public Page<Map<String, Object>> queryVisaBorrowMoney4XXZReviewInfo(HttpServletRequest request, HttpServletResponse response, String deFaultUserJobid) {
		
		String groupCode = request.getParameter("groupCode");
		if(groupCode == null || "".equals(groupCode.trim())){
			groupCode = null;
		}
		String startTime = request.getParameter("startTime");
		if(startTime == null || "".equals(startTime.trim())){
			startTime = null;
		}
		String endtime = request.getParameter("endTime");
		if(endtime == null || "".equals(endtime.trim())){
			endtime = null;
		}
		String agent = request.getParameter("channel");
		if(agent == null || "".equals(agent.trim())){
			agent = null;
		}
		
		//--------下单人   wxw 2015-08-20 added-----------
		String orderCreateBy = request.getParameter("orderCreateBy");
		if(orderCreateBy == null || "".equals(orderCreateBy.trim())){
			orderCreateBy = null;
		}
		
		String saler = request.getParameter("saler");
		if(saler == null || "".equals(saler.trim())){
			saler = null;
		}
		
		String jdsaler = request.getParameter("meter");
		if(jdsaler == null || "".equals(jdsaler.trim())){
			jdsaler = null;
		}
	
		String statusChoose = request.getParameter("statusChoose");
		if(statusChoose == null || ("all".equals(statusChoose.trim()))){
			statusChoose = null;
		}
		
		String visaType = request.getParameter("visaType");
		if(visaType == null || "".equals(visaType.trim())){
			visaType = null;
		}
		String userJobIdStr = request.getParameter("userJobId");
		UserJob userJob = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		//获取当前用户的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY);
		if(userJobs == null || userJobs.size() == 0) {
			return null;
		}
		
        Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
		
		//获取第一层级部门id
		Long pDeptId;
		//子部门id
		List<Long> subIds = new ArrayList<Long>();
		//如果userJob为null则表明是第一次进入审核页面  默认取第一个userJob
		if(userJob == null){
			userJob = userJobs.get(userJobs.size()-1);
		}
		
		if(userJob.getDeptLevel() == 1){
			pDeptId = userJob.getDeptId();
			subIds = departmentDao.findSubidsByParentId(pDeptId);
		} else {
			pDeptId = userJob.getParentDept();
			subIds.add(userJob.getDeptId());
		}
		//获取reviewComppanyid
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY);
		if(levels == null || levels.size() == 0){
			return null;
		}
		
		//-------added for adapt deptId  2015-2-6--------
		// 1调用dao查询审核列表
		Page<Map<String, Object>> reviewPage = visaBorrowMoneyDao.queryVisaBorrowMoney4XXZReviewList(request, response, groupCode,
						startTime, endtime, agent,orderCreateBy, saler, jdsaler, statusChoose, visaType,levels, userJob, reviewCompanyId,subIds);
		// 2 调用review接口查询审核信息
		//List<Map<String,String>> list = reviewService.findReviewCompanyListMap(Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY,false, subIds);
		// 3 整合信息
		List<Map<String, Object>> reviewList = reviewPage.getList();
		for(Map<String, Object> tMap : reviewList){
			tMap.put("revLevel", levels.get(0));
			//start 把游客的结算价的UUID转化为多币种的款项值
	/*		String payprice = tMap.get("payprice") == null ? null : tMap.get("payprice").toString();
			payprice = moneyAmountService.getMoney(payprice);
			tMap.remove("payprice");
			tMap.put("payprice", payprice);
			//end 把游客的结算价的UUID转化为多币种的款项值
			for(Map<String, String> tList: list){
				String revid = tMap.get("revid").toString();
				String id = tList.get("id");
				if(revid != null && !"".equals(revid.trim()) && id !=null && (revid.trim()).equals(id.trim())) {
					tMap.putAll(tList);
				}
			}*/
			
			String revid = tMap.get("revid").toString();
			Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
			tMap.put("createReason", reviewAndDetailInfoMap.get("createReason"));
		}
		
		Iterable<ReviewLog> all = reviewLogDao.findAll();
		Iterator<ReviewLog> iterator = all.iterator();
		List<ReviewLog> listReLog = new ArrayList<ReviewLog>();
		ReviewLog next;
		while(iterator.hasNext()){
			next = iterator.next();
			listReLog.add(next);
		}
	/*	for(Map<String, Object> tMap : reviewList){
			for(ReviewLog tempRevLog : listReLog){
				String revid = tMap.get("revid").toString();
				String id = tempRevLog.getReviewId().toString();
				if(revid != null && !"".equals(revid.trim()) && id !=null && (revid.trim()).equals(id.trim())) {
					
					//-------added for adapt deptId  2015-2-6--------
					if(!isInLevel(tempRevLog.getNowLevel(), levels) ){
						tMap.remove("revstatus");//审核状态
						tMap.put("revstatus", tempRevLog.getResult()); //状态转换为log中相应汉字
//						tMap.remove("lastoperator");//上一环节操作人
//						tMap.put("lastoperator", tempRevLog.getCreateBy());
					}
					if(levels.get(0) == 1){
						tMap.put("lastoperator", "");
					}
					
					if(tempRevLog.getNowLevel() == levels.get(0) - 1){
						tMap.put("lastoperator", tempRevLog.getCreateBy());
					}
				}
			}
		}*/
		
		reviewPage.setList(reviewList);
		return reviewPage;
	}
	
	/**
	 * 获取新行者订单借款金额的字符串  否则返回null
	 * 
	 * @param orderId
	 * @return 游客借款金额的字符串
	 */
	@Override
	public String getXinXingZheActiveReview(String orderId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId)) {
			listReview = reviewDao.findReviewActive4XXZ(6, 20,orderId);
			if (listReview.size()>=1) {
				List<ReviewDetail> list =reviewDetailDao.findReviewDetailByMykey(listReview.get(0).getId(), "borrowAmount");
				return list.get(0).getMyvalue()+","+listReview.get(0).getStatus();
			}
		}
		return null;
	}
	
	
	/**
	 * 环球行游客单个借款金额的字符串  否则返回null
	 * 
	 * @param orderId
	 * @return 游客借款金额的字符串
	 */
	@Override
	public String getHQXActiveReview(String orderId,Long trvelerId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId)) {
			listReview = reviewDao.findReviewActive4HQX(6, 5,orderId,trvelerId);
			if (listReview.size()>=1) {
				List<ReviewDetail> list =reviewDetailDao.findReviewDetailByMykey(listReview.get(0).getId(), "borrowAmount");
				return list.get(0).getMyvalue()+","+listReview.get(0).getStatus();
			}
		}
		return null;
	}
	
	/**
	 * 生成签证借款单文件
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public File createVisaBorrowMoney4XXZSheetDownloadFile(Long revid, String payId, String option) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(revid);
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate"))));// 填写日期
			
			// --- wangxinwei 20151008 added：处理确认付款日期
			root.put("revUpdateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"))));// 更新日期
			
			root.put("grouptotalborrownode",reviewAndDetailInfoMap.get("grouptotalborrownode"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			root.put("productCreater", productCreater);
			
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			
			if("order".equals(option)) {
				Double borrowAmount = Double.valueOf(reviewAndDetailInfoMap.get("borrowAmount"));
				root.put("revBorrowAmount", MoneyNumberFormat.getThousandsMoney(borrowAmount, MoneyNumberFormat.THOUSANDST_POINT_TWO));// 借款金额
				root.put("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(borrowAmount, MoneyNumberFormat.POINT_TWO))));// 借款金额大写
			}	
			
			//----- wxw added 20151008 ----- 处理付款确认时间，payStatus：1 显示update时间，0不显示
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();	
			String	payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				root.put("payStatus","0");
			}else {
				if(StringUtils.isNotBlank(payStatus)) {
					root.put("payStatus",payStatus);
				}else {
					root.put("payStatus", "0");
				}
			}
		}
		//出纳以外的最后一个审批人：对签证借款流程来说level为3
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(revid);
		
		if (null!=reviewLogs&&reviewLogs.size()>0) {
			User user = UserUtils.getUser(reviewLogs.get(reviewLogs.size()-1).getCreateBy());
			if (null!=user) {
				root.put("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			}
		}		
		
		if("pay".equals(option)) {
			//45需求，借款金额以每次支出金额为准
			String revBorrowAmount = "";
			String revBorrowAmountDx = "";
			PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, Context.ORDER_STATUS_VISA);
			if(payDetail != null) {
				if(Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != BigDecimal.ZERO.doubleValue()) {
					revBorrowAmount = payDetail.getRefundRMBDispStyle();
					revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.parseDouble(revBorrowAmount.replaceAll(",", "")));// 借款金额大写
				}
			}
			
			root.put("revBorrowAmount", revBorrowAmount);
			root.put("revBorrowAmountDx", revBorrowAmountDx);
		}
			
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY,reviewLogs);
		
		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户财务主管为空
		 */
		if (null!=jobtypeusernameMap.get(9)) {//财务主管
			if (68!=UserUtils.getUser().getCompany().getId()) {
				root.put("cwmanager", jobtypeusernameMap.get(9));
			}else {
				root.put("cwmanager", "");
			}
		}else {
			root.put("cwmanager", "");
		}
		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			root.put("cw", jobtypeusernameMap.get(8));
		}else {
			root.put("cw", "");
		}
		
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (null!=jobtypeusernameMap.get(6)) {//出纳
			if (68!=UserUtils.getUser().getCompany().getId()) {
				root.put("cashier", jobtypeusernameMap.get(6));
			}else {
				root.put("cashier", "");
			}
		}else {
			root.put("cashier", "");
		}
		
		//model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
		if (null!=jobtypeusernameMap.get(10)) {//总经理
			root.put("majorCheckPerson", jobtypeusernameMap.get(10));
		}else {
			root.put("majorCheckPerson", "");
		}
		
		if (null!=jobtypeusernameMap.get(7)) {//部门经理
			root.put("deptmanager", jobtypeusernameMap.get(7));
		}else {
			root.put("deptmanager", "");
		}
		
	   return FreeMarkerUtil.generateFile("visaborrowmoney4xxz.ftl", "visaborrowmoney4xxz.doc", root);
	}
	
	/*********************   新行者借款相关     结束      *********************/
	/*********************   新行者借款相关     结束      *********************/
	/*********************   新行者借款相关     结束      *********************/

	/**
	 * 通过签证订单ID查找游客列表
	 * @author jiachen
	 * @DateTime 2015年5月6日 上午14:23:23
	 */
	@Override
	public List<Traveler> getTravelerList(String orderId) {
		List<Traveler> travelerList = null;
		if(StringUtils.isNotBlank(orderId)) {
			travelerList = travelerDao.findTravelerByOrderIdAndOrderType(Long.valueOf(orderId), Integer.valueOf(Context.PRODUCT_TYPE_QIAN_ZHENG));
		}
		return travelerList;
	}

	/**
	 * 其他币种转换成人民币
	 */
	@Override
	public float currencyConverter(String count,String currencyId)
	{
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		String [] ct= count.split(",");
		String [] ci= currencyId.split(",");
		double totalMoney =0;
		for(int i=0;i<ct.length;i++)
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT c.currency_id,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
			buffer.append(ci[i]);
			buffer.append(" AND c.create_company_id=");
			buffer.append(userCompanyId);
			List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
			Map<String, Object>  mp =  list.get(0);
			totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ct[i]);
		}
		 BigDecimal   b   =   new   BigDecimal(totalMoney); 
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	
	
	
	/**********************************批次审核开始**********************************/
	/**********************************批次审核开始**********************************/
	/**********************************批次审核开始**********************************/
	
	/**
	 * 签证批量借款
	 * @author wxw
	 * @param visaIds 签证id 以','分割
	 * @param travellerIds 游客id 以','分割
	 * @param persons 借款人 以','分割
	 * @param dates 日期 以','分割
	 * @param moneys 金额 以','分割
	 * @param others 备注 以','分割
	 * @return map
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Map<String, Object> visaBatchJk(String batchNo,String visaIds, String travellerIds,
			String persons, String dates, String moneys, String others) {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap= null;
		
		String[] travelerIDS = travellerIds.split(",");
		String[] borrowAmounts = moneys.split(",");
		String[] borrowRemarks = others.split(",");
		String[] borrowTimes = dates.split(",");
		
		//获取人民币币种id
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
		buffer.append(" AND c.create_company_id=");
		buffer.append(UserUtils.getUser().getCompany().getId());
		List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
		Integer borrowtotalcurrencyId = 0;
		for (int i = 0; i < currencylist.size(); i++) {
			if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
				borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
				break;
			}
		}
		
		//String batchNo =   sysBatchNoService.getVisaBorrowMoneyBatchNo();
		 try{
			    /*
			     * 同一批次的借款用同一个批次号，作为业务数据保存在表review_detail中，mykey的值为visaBorrowMoneyBatchNo
			     */
			    //System.out.println(2/0);
				//签证批量借款
		        for (int i = 0; i < travelerIDS.length; i++) {
		        	
		        	//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
		        	if (!"0".equals(travelerIDS[i])) { 
		        		//申请时的addReview 要用添加dept
		        		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
		        		Long deptId = visaOrderService.getProductPept(traveler.getOrderId());
		        		
		        		List<Detail> listDetail = new ArrayList<Detail>();
		        		listDetail.add(new Detail("borrowAmount", borrowAmounts[i]));
		        		/**
		        		 * 默认取借款申请者所在渠道的人民币为借款币种
		        		 */
		        		listDetail.add(new Detail("currencyId", borrowtotalcurrencyId+""));//默认借款币种为RMB
		        		listDetail.add(new Detail("borrowRemark", borrowRemarks[i]));
		        		listDetail.add(new Detail("borrowTime", borrowTimes[i]));//新增借款时间
		        		listDetail.add(new Detail("visaBorrowMoneyBatchNo", batchNo));
		        		
		        		StringBuffer reply = new StringBuffer("签证批量借款");
		        		long  addresult = reviewService.addReview(VisaBorrowMoneyController.VISA_PRODUCT_TYPE, //产品类型 
		        				VisaBorrowMoneyController.VISA_JK_FLOW_TYPE,//流程类型  flowtype
		        				traveler.getOrderId()+"",//订单ID
		        				traveler.getId().longValue(),//游客ID
		        				Long.parseLong("0"), //新提交的审核请置 0. 重新提交审核时,等于上次审核记录的主键
		        				borrowRemarks[i],//创建原因
		        				reply, 
		        				listDetail,
		        				deptId);
		        		if (addresult!=0) {
		        			reply.append("申请成功");
		        		}
					}
				}
		        
		 }catch(Exception e){
			 e.printStackTrace();
			 if(resultMap==null){
				 resultMap = new HashMap<String,Object>();
			 }
			 resultMap.put("msg", "批量借款申请失败！");
		 }
		return resultMap;
	}
	
	
	
	/**
	 *  签证借款批次审核列表
	 *  xinwei.wang added
	 *  2015年05月26日11:33:30
	 */
	@Override
	public Page<Map<String, Object>> queryVisaBorrowMoneyBatchReviewInfo(HttpServletRequest request, HttpServletResponse response, String deFaultUserJobid) {
		
		String groupCode = request.getParameter("groupCode");
		if(groupCode == null || "".equals(groupCode.trim())){
			groupCode = null;
		}
		String startTime = request.getParameter("startTime");
		if(startTime == null || "".equals(startTime.trim())){
			startTime = null;
		}
		String endtime = request.getParameter("endTime");
		if(endtime == null || "".equals(endtime.trim())){
			endtime = null;
		}
		String agent = request.getParameter("channel");
		if(agent == null || "".equals(agent.trim())){
			agent = null;
		}
		
		//--------打印状态  wxw 2015-07-28 added-----------
		String printstatus = request.getParameter("printstatus");
		if(printstatus == null || "".equals(printstatus.trim())){
			printstatus = null;
		}
		
		//--------游客姓名  wxw 2015-07-28 added-----------
		String travlerName = request.getParameter("travlerName");
		if(travlerName == null || "".equals(travlerName.trim())){
			travlerName = null;
		}
		
		//--------借款金额  wxw 2015-07-28 added-----------
		String batchBorrowAmountStart = request.getParameter("batchBorrowAmountStart");
		if(batchBorrowAmountStart == null || "".equals(batchBorrowAmountStart.trim())){
			batchBorrowAmountStart = null;
		}
		String batchBorrowAmountEnd = request.getParameter("batchBorrowAmountEnd");
		if(batchBorrowAmountEnd == null || "".equals(batchBorrowAmountEnd.trim())){
			batchBorrowAmountEnd = null;
		}
		
		String saler = request.getParameter("saler");
		if(saler == null || "".equals(saler.trim())){
			saler = null;
		}
		
		
		//签证国家
		String sysCountryId = request.getParameter("sysCountryId");
		if(sysCountryId == null || "".equals(sysCountryId.trim())){
			sysCountryId = null;
		}
		
		
		String jdsaler = request.getParameter("meter");
		if(jdsaler == null || "".equals(jdsaler.trim())){
			jdsaler = null;
		}
	
		String statusChoose = request.getParameter("statusChoose");
		if(statusChoose == null || ("all".equals(statusChoose.trim()))){
			statusChoose = null;
		}
		
		String visaType = request.getParameter("visaType");
		if(visaType == null || "".equals(visaType.trim())){
			visaType = null;
		}
		String userJobIdStr = request.getParameter("userJobId");
		UserJob userJob = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		//获取当前用户的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		if(userJobs == null || userJobs.size() == 0) {
			return null;
		}
		
        Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
		
		//获取第一层级部门id
		Long pDeptId;
		//子部门id
		List<Long> subIds = new ArrayList<Long>();
		//如果userJob为null则表明是第一次进入审核页面  默认取第一个userJob
		if(userJob == null){
			userJob = userJobs.get(0);
		}
		
		if(userJob.getDeptLevel() == 1){
			pDeptId = userJob.getDeptId();
			subIds = departmentDao.findSubidsByParentId(pDeptId);
		} else {
			pDeptId = userJob.getParentDept();
			subIds.add(userJob.getDeptId());
		}
		//获取reviewComppanyid
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		if(levels == null || levels.size() == 0){
			return null;
		}
		
		//-------added for adapt deptId  2015-2-6--------
		// 1调用dao查询审核列表
		Page<Map<String, Object>> reviewPage = visaBorrowMoneyDao.queryVisaBorrowMoneyBatchReviewList(request, response, groupCode,
						startTime, endtime, agent,printstatus,travlerName,batchBorrowAmountStart,batchBorrowAmountEnd, saler,sysCountryId, jdsaler, statusChoose, visaType,levels, userJob, reviewCompanyId,subIds);

		// 2 调用review接口查询审核信息
		//List<Map<String,String>> list = reviewService.findReviewCompanyListMap(Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY,false, subIds);
		// 3 整合信息
		List<Map<String, Object>> reviewList = reviewPage.getList();
		for(Map<String, Object> tMap : reviewList){
			tMap.put("revLevel", levels.get(0));
			//start 把游客的结算价的UUID转化为多币种的款项值
		/*	String payprice = tMap.get("payprice") == null ? null : tMap.get("payprice").toString();
			payprice = moneyAmountService.getMoney(payprice);
			tMap.remove("payprice");
			tMap.put("payprice", payprice);
			//end 把游客的结算价的UUID转化为多币种的款项值
			for(Map<String, String> tList: list){
				String revid = tMap.get("revid").toString();
				String id = tList.get("id");
				if(revid != null && !"".equals(revid.trim()) && id !=null && (revid.trim()).equals(id.trim())) {
					tMap.putAll(tList);
				}
			}*/
			
			String revid = tMap.get("revid").toString();
			Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
			tMap.put("createReason", reviewAndDetailInfoMap.get("createReason"));
			tMap.put("borrowAmount", reviewAndDetailInfoMap.get("borrowAmount"));
			
		}
		
		Iterable<ReviewLog> all = reviewLogDao.findAll();
		Iterator<ReviewLog> iterator = all.iterator();
		List<ReviewLog> listReLog = new ArrayList<ReviewLog>();
		ReviewLog next;
		while(iterator.hasNext()){
			next = iterator.next();
			listReLog.add(next);
		}
	/*	for(Map<String, Object> tMap : reviewList){
			for(ReviewLog tempRevLog : listReLog){
				String revid = tMap.get("revid").toString();
				String id = tempRevLog.getReviewId().toString();
				if(revid != null && !"".equals(revid.trim()) && id !=null && (revid.trim()).equals(id.trim())) {
					
					//-------added for adapt deptId  2015-2-6--------
					if(!isInLevel(tempRevLog.getNowLevel(), levels) ){
						tMap.remove("revstatus");//审核状态
						tMap.put("revstatus", tempRevLog.getResult()); //状态转换为log中相应汉字
//						tMap.remove("lastoperator");//上一环节操作人
//						tMap.put("lastoperator", tempRevLog.getCreateBy());
					}
					if(levels.get(0) == 1){
						tMap.put("lastoperator", "");
					}
					
					if(tempRevLog.getNowLevel() == levels.get(0) - 1){
						tMap.put("lastoperator", tempRevLog.getCreateBy());
					}
				}
			}
		}*/
		
		reviewPage.setList(reviewList);
		return reviewPage;
	}
	

	
	
	
	/**
	 * 生成签证借款单文件
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public File createBatchVisaBorrowMoneySheetDownloadFile(Long revid,String batchno, String payId, String option) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(revid);		
		
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate"))));// 填写日期
			// --- wangxinwei 20151008 added：处理确认付款日期
			root.put("revUpdateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"))));// 更新日期
			root.put("revBorrowRemark",reviewAndDetailInfoMap.get("borrowRemark"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			root.put("productCreater", productCreater);
			
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			
			if("order".equals(option)) {
				//获取批次借款金额
				VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"2");
				String batchborrowtotalMoney = visaFlowBatchOpration.getBatchTotalMoney();
				
				root.put("revBorrowAmount",MoneyNumberFormat.getThousandsMoney(Double.parseDouble(batchborrowtotalMoney), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 借款金额   改为批次的借款金额
				root.put("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(Double.parseDouble(batchborrowtotalMoney), MoneyNumberFormat.POINT_TWO))));// 借款金额大写   改为批次借款金额
			}
			
			//----- wxw added 20151008 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			String payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid)||Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				root.put("payStatus","0");
			}else {
				if(StringUtils.isNotBlank(payStatus)) {
					root.put("payStatus",payStatus);
				}else {
					root.put("payStatus","0");
				}
			}	
		}
		
		if("pay".equals(option)) {
			//45需求，借款金额以每次的支付金额为标准
			String revBorrowAmount = "";
			String revBorrowAmountDx = "";
			if(StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, Context.ORDER_STATUS_VISA);
				if (payDetail != null) {
					if (Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != BigDecimal.ZERO.doubleValue()) {
						revBorrowAmount = payDetail.getRefundRMBDispStyle();
						revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.parseDouble(revBorrowAmount.replaceAll(",", "")));// 借款金额大写
					}
				}
			}
			root.put("revBorrowAmount", revBorrowAmount);
			root.put("revBorrowAmountDx", revBorrowAmountDx);			
		}
		
				
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(revid);
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY,reviewLogs);
		
		if (null!=reviewLogs&&reviewLogs.size()>0) {
			User user = UserUtils.getUser(reviewLogs.get(reviewLogs.size()-1).getCreateBy());
			if (null!=user) {
				root.put("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			}
		}		
		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户财务主管为空
		 */
		if (null!=jobtypeusernameMap.get(9)) {//财务主管
			if (68!=UserUtils.getUser().getCompany().getId()) {
				root.put("cwmanager", jobtypeusernameMap.get(9));
			}else {
				root.put("cwmanager", "");
			}
		}else {
			root.put("cwmanager", "");
		}
		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			root.put("cw", jobtypeusernameMap.get(8));
		}else {
			root.put("cw", "");
		}
		
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (null!=jobtypeusernameMap.get(6)) {//出纳
			if (68!=UserUtils.getUser().getCompany().getId()) {
				root.put("cashier", jobtypeusernameMap.get(6));
			}else {
				root.put("cashier", "");
			}
		}else {
			root.put("cashier", "");
		}
		
		if (null!=jobtypeusernameMap.get(10)) {//总经理
			root.put("majorCheckPerson", jobtypeusernameMap.get(10));
		}else {
			root.put("majorCheckPerson", "");
		}
		
		if (null!=jobtypeusernameMap.get(7)) {//部门经理
			root.put("deptmanager", jobtypeusernameMap.get(7));
		}else {
			root.put("deptmanager", "");
		}		
		
		/**
		 * wangxinwei 20151102 added
		 * 解决签证借款打印（批次）申请原因只能显示一个游客申请原因的问题
		 */
		String borrowReMark4DownLoad = getVisaBorrowBatchPrintAppReason(batchno);
		root.put("revBorrowRemark",borrowReMark4DownLoad);// 申报原因
		
		
	   return FreeMarkerUtil.generateFile("visabatchborrowmoney.ftl", "visabatchborrowmoney.doc", root);
	}
	

	/**
	 * 通过批次号查询该批次下游客的信息(针对签证借款)
	 * @author jiachen
	 * @DateTime 2015年5月28日 上午10:07:17
	 * @param batchNo
	 * @return Map<String,String>
	 */
	public void getTravelerList(String batchNo, List<Map<String, String>> travelerList) {
		
		//获取批次下的游客
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
		buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
		buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");
		buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaBorrowMoneyBatchNo' AND revd.myValue= vfbo.batch_no ");
		buffer.append(" AND vfbo.busyness_type = 2 AND rev.travelerId = tr.id AND vfbo.batch_no=");
		buffer.append("'"+batchNo+"'");
		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		
		Integer i = 0;
		for(Map<String, Object> map : list) {
			if(null != map.get("travelerid")) {
				Map<String, String> travelerMap = new HashMap<String, String>();
				//审批流信息
				travelerMap.put("reviewId", map.get("reviewid").toString());
				//获取游客信息---------------
				Traveler traveler = travelerDao.findById(Long.valueOf(map.get("travelerid").toString()));
				//游客id
				Long travelerId = traveler.getId();
				if(null != travelerId) {
					travelerMap.put("tid", travelerId.toString());
				}
				//序号
				i++;
				travelerMap.put("num", i.toString());
				//游客姓名
				travelerMap.put("tname", traveler.getName());
				//获取订单信息---------------
				VisaOrder visaOrder = visaOrderDao.findOne(traveler.getOrderId());
				
				VisaProducts visaProduct = visaProductsDao.findOne(visaOrder.getVisaProductId());
				//订单id
				travelerMap.put("orderId", visaOrder.getId().toString());
				//订单编号
				travelerMap.put("orderNo", visaOrder.getOrderNo());
				
				//订单团号
				//travelerMap.put("groupCode", visaOrder.getGroupCode());
				String companeyUUID = UserUtils.getUser().getCompany().getUuid();
				if ("7a816f5077a811e5bc1e000c29cf2586".equals(companeyUUID)) {
					travelerMap.put("groupCode", visaOrder.getGroupCode());
				}else{
					travelerMap.put("groupCode", visaProduct.getGroupCode());// 对应需求号C460V3  签证订单团号  统一显示订单所关联产品团号
				}
				
				//下单人
				travelerMap.put("orderCreateBy", visaOrder.getCreateBy().getName());
				//获取产品信息---------------
				VisaProducts visaProducts = visaProductsDao.findOne(visaOrder.getVisaProductId());
				//签证类型
				Integer visaType = visaProducts.getVisaType();
				if(null != visaType) {
					travelerMap.put("visaType", DictUtils.getDictLabel(visaType.toString(), "new_visa_type", ""));
				}
				//签证国家
				travelerMap.put("visaCountry", CountryUtils.getCountryName(visaProducts.getSysCountryId().longValue()));
				//签证借款申请相关信息
				Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong((Integer)map.get("reviewid")+""));
				String borrowCurrency = CurrencyUtils.getCurrencyInfo(reviewAndDetailInfoMap.get("currencyId"), 0, "mark");
				travelerMap.put("borrowCurrency", borrowCurrency);
				travelerMap.put("borrowAmount", reviewAndDetailInfoMap.get("borrowAmount"));
				travelerMap.put("borrowRemark", reviewAndDetailInfoMap.get("borrowRemark"));
				travelerList.add(travelerMap);
			}
		}
	}

	/**
	 * 通过批次号查询该批次下游客的信息(针对还押金收据)
	 * @author jiachen
	 * @DateTime 2015年5月28日 上午10:07:17
	 * @param batchNo
	 * @return Map<String,String>
	 */
	public void getTravelerListForReturnReceipt(String batchNo, List<Map<String, String>> travelerList) {
		
		//获取批次下的游客
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
		buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
		buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");
		buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaReturnReceiptBatchNo' AND revd.myValue= vfbo.batch_no ");
		buffer.append(" AND vfbo.busyness_type = 1 AND rev.travelerId = tr.id AND vfbo.batch_no=");
		buffer.append("'"+batchNo+"'");
		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		
		Integer i = 0;
		for(Map<String, Object> map : list) {
			if(null != map.get("travelerid")) {
				Map<String, String> travelerMap = new HashMap<String, String>();
				//审批流信息
				travelerMap.put("reviewId", map.get("reviewid").toString());
				//获取游客信息---------------
				Traveler traveler = travelerDao.findById(Long.valueOf(map.get("travelerid").toString()));
				//游客id
				Long travelerId = traveler.getId();
				if(null != travelerId) {
					travelerMap.put("tid", travelerId.toString());
				}
				//序号
				i++;
				travelerMap.put("num", i.toString());
				//游客姓名
				travelerMap.put("tname", traveler.getName());
				//获取订单信息---------------
				VisaOrder visaOrder = visaOrderDao.findOne(traveler.getOrderId());
				//订单id
				travelerMap.put("orderId", visaOrder.getId().toString());
				//订单编号
				travelerMap.put("orderNo", visaOrder.getOrderNo());
				//订单团号
				travelerMap.put("groupCode", visaOrder.getGroupCode());
				//下单人
				travelerMap.put("orderCreateBy", visaOrder.getCreateBy().getName());
				//获取产品信息---------------
				VisaProducts visaProducts = visaProductsDao.findOne(visaOrder.getVisaProductId());
				//签证类型
				Integer visaType = visaProducts.getVisaType();
				if(null != visaType) {
					travelerMap.put("visaType", DictUtils.getDictLabel(visaType.toString(), "new_visa_type", ""));
				}
				//签证国家
				travelerMap.put("visaCountry", CountryUtils.getCountryName(visaProducts.getSysCountryId().longValue()));
				//签证借款申请相关信息
				Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong((Integer)map.get("reviewid")+""));
				String receiptCurrency = CurrencyUtils.getCurrencyInfo(reviewAndDetailInfoMap.get("currencyId"), 0, "mark");
				travelerMap.put("receiptCurrency", receiptCurrency);
				travelerMap.put("receiptAmount", reviewAndDetailInfoMap.get("receiptAmount"));
				travelerMap.put("returnReceiptRemark", reviewAndDetailInfoMap.get("returnReceiptRemark"));
				travelerList.add(travelerMap);
			}
		}
	}
	/**
	 * 签证借款批次审批导出游客信息
	 * @author jiachen
	 * @DateTime 2015年5月29日 上午11:03:54
	 * @param batchNo
	 * @return void
	 */
	public void exportTravelerInfo(String batchNo, HttpServletRequest request, HttpServletResponse response) {
		//获取数据
		List<Map<String, String>> travelerList = new ArrayList<Map<String, String>>();
		List<VisaFlowBatchOpration> busynessTypeList = visaFlowBatchOprationDao.findByBatchNo(batchNo);
		//String busynessType = busynessTypeList.get(0).getBusynessType();
		String busynessType = request.getParameter("busynessType");
		//根据批次号类型判断是哪种批量操作
		if(!busynessTypeList.isEmpty()) {//??????????
			//批量还收据
			if("1".equals(busynessType)) {
				getTravelerListForReturnReceipt(batchNo, travelerList);
			}
			//批量借款
			else if("2".equals(busynessType)){
				getTravelerList(batchNo, travelerList);
			}
		}
		
		if(!travelerList.isEmpty()) {
			List<Object[]> travelerInfoList = new ArrayList<Object[]>();
			for(int i = 0; i < travelerList.size();  i++) {
				Object[] o = new Object[6];
				//序号
				o[0] = i + 1;
				Map<String, String> travelerInfoMap = travelerList.get(i);
				//游客姓名
				o[1] = travelerInfoMap.get("tname");
				//订单团号
				o[2] = travelerInfoMap.get("groupCode");
				//下单人
				o[3] = travelerInfoMap.get("orderCreateBy");
				//批量还收据
				if("1".equals(busynessType)) {
					//借款金额
					o[4] = travelerInfoMap.get("receiptCurrency") + travelerInfoMap.get("receiptAmount");
					//借款原因
					o[5] = travelerInfoMap.get("returnReceiptRemark");
				}
				//批量借款
				else if("2".equals(busynessType)){
					o[4] = travelerInfoMap.get("borrowCurrency") + travelerInfoMap.get("borrowAmount");
					o[5] = travelerInfoMap.get("borrowRemark");
				}
				travelerInfoList.add(o);
			}
			String fileName = batchNo + "-游客信息";
			//Excel各行名称
			
			String[] cellTitle =  new String[6];
			cellTitle[0]="序号";
			cellTitle[1]="游客姓名";
			cellTitle[2]="团号";
			cellTitle[3]="下单人";
			if("1".equals(busynessType)) {//还收据
				cellTitle[4]="收据金额";
				cellTitle[5]="还收据原因";
			}else if("2".equals(busynessType)){//批量借款
				cellTitle[4]="借款金额";
				cellTitle[5]="借款原因";
			}
			//文件首行标题
			String firstTitle = batchNo;
			try {
				ExportExcel.createExcle(fileName, travelerInfoList, cellTitle, firstTitle, request, response);
			} catch (Exception e) {
				new Exception("导出游客信息时发生错误");
			}
		}
	}
	
	
	//--------------------审核通过后处理---------------------
	/**
	 * 
	 * 环球行签证借款（包括批次）  审核通过后 处理：
	 * 1.首先补充老数据游客的JkSerialNum
	 * 2.保存签证借款记录
	 * 3.更新批次审核状态
	 * @param review
	 */
	@Transactional
	@Override
	public void visaBorrowMoneyPostReviewPassedTackle4HQX(Review review){
		Long revid = review.getId();
		Traveler traveler = travelerService.findTravelerById(revid);
		String jkSerialNum = UUID.randomUUID().toString();
		//1.如果游客没有添加借款序列号，则生成，否则取已有序列号
		if (null==traveler.getJkSerialNum()||"".equals(traveler.getJkSerialNum())) {
			travelerService.updateJkSerialNumByTravelerId(jkSerialNum,traveler.getId());
			traveler.setJkSerialNum(jkSerialNum);
		}
		
		//2.保存签证借款记录
		//获取借款币种
		List<ReviewDetail> currencyId =reviewDetailDao.findReviewDetailByMykey(revid, "currencyId");
		//获取借款金额
		List<ReviewDetail> borrowAmount =reviewDetailDao.findReviewDetailByMykey(revid, "borrowAmount");
		//获取借款金额
		List<ReviewDetail> batchnoList =reviewDetailDao.findReviewDetailByMykey(revid, "visaBorrowMoneyBatchNo");
		String batchno = null;
		if (null!=batchnoList&&batchnoList.size()>0) {
			batchno = batchnoList.get(0).getMyvalue();
		}
		
	    BigDecimal price = new BigDecimal(borrowAmount.get(0).getMyvalue());
		MoneyAmount costMoneyAmount = new MoneyAmount(traveler.getJkSerialNum(), //款项UUID
				Integer.parseInt(currencyId.get(0).getMyvalue()),//币种ID
				price,//相应币种的金额
	    		traveler.getId(), //订单或游客ID
	    		Context.MONEY_TYPE_JK, //款项类型: 借款
	    		Context.ORDER_TYPE_QZ,//订单类型
	    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1表示订单，2表示游客
	    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
		costMoneyAmount.setReviewId(revid);
	    moneyAmountService.addMoneyAmount(costMoneyAmount);
			
		//3.更新批次审核状态
	    VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno, "2");
	    if (null!=visaFlowBatchOpration) {
	    	visaFlowBatchOprationDao.updateVisaFlowBatchOprationStatusByBatchNo(batchno, "2");
		}
		
	}
	
	
	/**
	 * 
	 * 新行者 审核通过后 处理：
	 * 1.保存签证借款记录
	 * 2.处理币种获取失败的情况
	 * @param review
	 */
	@Transactional
	@Override
	public void visaBorrowMoneyPostReviewPassedTackle4XXZ(Review review){
		Long revid = review.getId();
		List<ReviewDetail> currencyId =reviewDetailDao.findReviewDetailByMykey(revid, "currencyId");
		
		//获取借款金额
		List<ReviewDetail> borrowAmount =reviewDetailDao.findReviewDetailByMykey(revid, "borrowAmount");
	    BigDecimal price = new BigDecimal(borrowAmount.get(0).getMyvalue());
	    
	    //获取订单借款币种
	    Integer borrowtotalcurrencyId = 0;
	    if (null!=currencyId&&currencyId.size()>0) {
	    	borrowtotalcurrencyId = Integer.parseInt(currencyId.get(0).getMyvalue());
		}else{
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
			buffer.append(" AND c.create_company_id=");
			buffer.append(UserUtils.getUser().getCompany().getId());
			List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
			
			for (int i = 0; i < currencylist.size(); i++) {
				if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
					borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
					break;
				}
			}
		}
	    VisaOrder visaOrder = visaOrderDao.findOne(Long.parseLong(review.getOrderId()));
		
		MoneyAmount costMoneyAmount = new MoneyAmount(visaOrder.getJkSerialnum(), //款项UUID
				borrowtotalcurrencyId,//币种ID
				price,//相应币种的金额
				Long.parseLong(review.getOrderId()), //借款订单或游客ID
	    		Context.MONEY_TYPE_JK, //款项类型: 借款
	    		Context.ORDER_TYPE_QZ,//订单类型
	    		VisaPreOrderController.BUSINDESS_TYPE_ORDER,//1表示订单，2表示游客
	    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
		costMoneyAmount.setReviewId(review.getId());
	    moneyAmountService.addMoneyAmount(costMoneyAmount);
		
	}
	
	
	//--------------------审核通过后处理---------------------
	
	/**********************************批次审核结束**********************************/
	/**********************************批次审核结束**********************************/
	/**********************************批次审核结束**********************************/
	
	
	
	//--------------批量批次审批   开始 ---------------
	/**
	 * 多批次审核：签证借款  多批次审核
	 * added by wxw 20150812
	 */
	@Transactional
	public boolean multiBatchReviewVisaBorrowMoneybyBatchNo(String result,String remarks, String batchnons){
		boolean isSuccess = false;
		
	    String[] batchnosArray  = batchnons.split(",");
	    
	    for (int j = 0; j < batchnosArray.length; j++) {
	    	boolean batchreviewstaus = false;//如审核通过更新批次社和状态
	    	
	    	//根据批次号获取批次审核 revids
	    	StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
			buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
			buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");
			buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaBorrowMoneyBatchNo' AND revd.myValue= vfbo.batch_no ");
			buffer.append(" AND vfbo.busyness_type = 2 AND rev.travelerId = tr.id AND vfbo.batch_no=");
			buffer.append("'"+batchnosArray[j]+"'");
			List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
			
			StringBuilder revidsAndCurlevels = new StringBuilder("");
			
			for (Map<String, Object> map : list) {
				//签证批次借款  中 每个批次中游客借款记录的 数据组织如下    当前审核层级@revid,当前审核层级@revid
				revidsAndCurlevels.append(map.get("curentlevel")+"@").append(map.get("reviewid")+",");
			}
	    	String[] revidsAndCurlevelsArray = revidsAndCurlevels.toString().split(",");
			
			/**
			 * 2015-08-12王新伟添加
			 * 签证借款批量审核：
			 * 
			 */
			for (int i = 0; i < revidsAndCurlevelsArray.length; i++) {
				StringBuffer reply = new StringBuffer();
				if (result == null || "".equals(result)) {
					reply.append("审批结果不能为空");
				}
				
				String nowLevel = revidsAndCurlevelsArray[i].split("@")[0];
				String revid = revidsAndCurlevelsArray[i].split("@")[1];
				
				
				/**
				 * 2015-08-12 wxw added
				 * 1.审核通过后在MoneyAmount中保存借款信息
				 * 2.处理与老数据兼容的相关问题：如果traveler的jkSerialNum为空，生成新的UUID并进行更新
				 * uuid取游客的jkSerialNum
				 */
				//UpdateReview返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
				int num = reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),remarks);
				List<Review> _list = reviewDao.findReviewActive(Long.parseLong(revid));
				Traveler traveler = travelerService.findTravelerById(_list.get(0).getTravelerId());
				String jkSerialNum = UUID.randomUUID().toString();
				//如果游客没有添加借款序列号，则生成，否则取已有序列号
				if (null==traveler.getJkSerialNum()||"".equals(traveler.getJkSerialNum())) {
					travelerService.updateJkSerialNumByTravelerId(jkSerialNum,traveler.getId());
					traveler.setJkSerialNum(jkSerialNum);
				}
				
				//if(list.get(0).getTopLevel() == Integer.parseInt(nowLevel)){//如果当前层级为最高层级  说明该流程已经审核结束
					if (null!=traveler&&1==num&&"1".equals(result)) {
						//获取借款币种
						List<ReviewDetail> currencyId =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "currencyId");
						//获取借款金额
						List<ReviewDetail> borrowAmount =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "borrowAmount");
					    BigDecimal price = new BigDecimal(borrowAmount.get(0).getMyvalue());
					    
						MoneyAmount costMoneyAmount = new MoneyAmount(traveler.getJkSerialNum(), //款项UUID
								Integer.parseInt(currencyId.get(0).getMyvalue()),//币种ID
								price,//相应币种的金额
					    		traveler.getId(), //订单或游客ID
					    		Context.MONEY_TYPE_JK, //款项类型: 借款
					    		Context.ORDER_TYPE_QZ,//订单类型
					    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1表示订单，2表示游客
					    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
						
						costMoneyAmount.setReviewId(Long.parseLong(revid));
					    moneyAmountService.addMoneyAmount(costMoneyAmount);
					    batchreviewstaus = true;
					}
				//}
			}
			
			//更新批次审批状态
			if (batchreviewstaus) {
				visaFlowBatchOprationDao.updateVisaFlowBatchOprationStatusByBatchNo(batchnosArray[j], "2");
			}
			
			//如果多批次正常执行到最后，才确认批次操作成功
			if (j==(batchnosArray.length-1)) {
				isSuccess = true;
			}
			
		}
		
		return isSuccess;
	}
	
	//--------------批量批次审批   结束 ---------------
	
	//-----wxw added 2015-09-18  处理 批次审核菜单数量 与 列表查询数量不一致的问题  开始-----
	/**
	 * 获取用户所有审核角色的待审核数量
	 */
	@Override
	public int getBatchReviewCount4Menu(){
		int count = 0;
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		if (null==userJobs||userJobs.size()<1) {
			return 0;
		}else {
			for (UserJob userJob : userJobs) {
				count=count+getJobReviewCountbyUserJob(userJob);
			}
		}
		return count;
	}
	
	/**
	 * 获取某个审核角色的审核数量
	 */
	@Override
	public int getJobReviewCountbyUserJob(UserJob userJob){
		//获取第一层级部门id
		Long pDeptId;
		//子部门id
		List<Long> subIds = new ArrayList<Long>();
		if(userJob.getDeptLevel() == 1){
			pDeptId = userJob.getDeptId();
			subIds = departmentDao.findSubidsByParentId(pDeptId);
		} else {
			pDeptId = userJob.getParentDept();
			subIds.add(userJob.getDeptId());
		}
		
		//处理子部门的id
		String subIdsStr = "";
		int nFlag = 0;
		for(Long subId : subIds){
			if(nFlag == 0) {
				subIdsStr += subId;
				nFlag++;
			} else {
				subIdsStr += "," + subId;
			}
		}
		String subIdsSQLStr = null;
		if (subIdsStr != null && !"".equals(subIdsStr.trim())) {
			subIdsSQLStr = " and rev.deptId in(" + subIdsStr + ") ";
		}
		
		//获取reviewComppanyid
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return 0;
		}
		
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		if(levels == null || levels.size() == 0){
			return 0;
		}
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("SELECT COUNT(1) FROM")
		     .append(" (SELECT count(1) FROM review rev,visa_order vorder,visa_products vp,traveler t,review_detail revd,visa_flow_batch_opration vfbo")
		     .append(" WHERE rev.orderId = vorder.id AND vorder.visa_product_id = vp.id AND rev.travelerId = t.id AND rev.productType = 6 ")
		     .append(" AND rev.flowType = 5 AND revd.review_id = rev.id AND revd.myKey = 'visaBorrowMoneyBatchNo' AND revd.myValue = vfbo.batch_no ")
		     .append(" AND vfbo.busyness_type = 2 AND rev.review_company_id = "+reviewCompanyId+" AND rev.companyId = ")
		     .append(UserUtils.getUser().getCompany().getId().intValue()+""+subIdsSQLStr)
		     .append(" AND rev.`status` = '1' AND rev.nowLevel ="+ levels.get(0) +" GROUP BY vfbo.batch_no) aa");
		return ((BigInteger)reviewDao.findBySql(sbSql.toString()).get(0)).intValue();
	}
	
	//-----wxw added 2015-09-18  处理 批次审核菜单数量 与 列表查询数量不一致的问题  结束-----
	
	/**
	 * 获取批次申请原因
	 * wangxinwei 20151102 added 
	 * 解决签证借款审核打印（批次），借款理由只能显示一个游客借款理由的问题
	 */
	@Override
	public String getVisaBorrowBatchPrintAppReason(String batchNo){
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("SELECT aa.review_id FROM ")
		.append("(SELECT rd.review_id FROM review_detail rd WHERE rd.myKey = 'visaBorrowMoneyBatchNo'")
		.append("AND rd.myValue = '"+batchNo+"') aa ")
	    .append("LEFT JOIN (SELECT rv.id FROM review rv WHERE rv.flowType = 5) bb ON aa.review_id = bb.id");
		List<Object> revIdList = (List<Object>)reviewDao.findBySql(sbSql.toString());
		StringBuilder sb4remark = new StringBuilder("");
		for (Object object : revIdList) {
			Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(object.toString()));
			sb4remark.append(reviewAndDetailInfoMap.get("borrowRemark")+" ");// 申报原因
		}
		return sb4remark.toString();
	}

	/**
	 * 把原有的对应的打印方法内容复制过来，保持原有的业务不变，接收返回的Map数据。该方法用于批量打印借款付款单，需要
	 * 把Controller里面的单个打印方法内容提取到Service层。
	 * @param paramBean
	 * @return
     */
	public Map<String,Object> getVisaBorrowMoney4XXZPrintMap(PrintParamBean paramBean) {

		Map<String,Object> result = new HashMap<>(32,0.75f);

		String revid = paramBean.getReviewId();
		String payId = paramBean.getPayId();
		String option = paramBean.getOption();
		String orderType = paramBean.getOrderType().toString();
		String payStatus = null; //签证借款审核付款状态

		result.put("revid", revid); // 把revid 传到模板模板的html页面，下载模板时使用
		result.put("payId", payId);
		result.put("orderType", orderType);
		result.put("option", option);

		// 签证借款申请相关信息
		String revCreateDate = "";
		String revUpdateDate = "";
		String groupTotalBorrowNode = "";
		String productCreater = "";
		String operaterName = "未知";
		String payDate = "";
		String revBorrowAmount = "";
		String revBorrowAmountDx = "";
		Currency revCurrency = null;

		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		if (reviewAndDetailInfoMap != null) {
			revCreateDate = reviewAndDetailInfoMap.get("createDate");
			revUpdateDate = reviewAndDetailInfoMap.get("updateDate");
			groupTotalBorrowNode = reviewAndDetailInfoMap.get("grouptotalborrownode");

			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			productCreater = visaProducts.getCreateBy().getName();

			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			if (null != user) {
				operaterName = user.getName();
			}

			payDate = reviewAndDetailInfoMap.get("updateDate");

			if ("order".equals(option)) {
				Double borrowAmount = Double.valueOf(reviewAndDetailInfoMap.get("borrowAmount"));
				String formatMoney = MoneyNumberFormat.getThousandsMoney(borrowAmount, MoneyNumberFormat.THOUSANDST_POINT_TWO);
				result.put("revBorrowAmount", formatMoney); // 借款金额
				result.put("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(formatMoney))); // 借款金额大写
			}

			String currencyId = reviewAndDetailInfoMap.get("currencyId");
			if (null != currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				revCurrency = currency;
			}

			//----- wxw added 20151008 -----需求C221， 新行者签证借款付款状态，payStatus：1 显示upDateDate时间，0不显示
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				result.put("payStatus", "0");
			} else {
				if (com.trekiz.admin.common.utils.StringUtils.isNotBlank(payStatus)) {
					result.put("payStatus", payStatus);
				} else {
					result.put("payStatus", "0");
				}
			}
		}

		result.put("revCreateDate", DateUtils.dateFormat(revCreateDate));// 填写日期
		//----- wxw added 20151008 -----单需求C221 ， 处理付款确认时间
		result.put("revUpdateDate", DateUtils.dateFormat(revUpdateDate));// 更新日期
		result.put("grouptotalborrownode", groupTotalBorrowNode);        // 申报原因（对新行者来说取总的备注信息）
		result.put("productCreater", productCreater);
		result.put("operatorName", operaterName);                        // 经办人、领款人都为借款申请人
		result.put("payDate", DateUtils.dateFormat(payDate));            // 付款日期
		result.put("revCurrency", revCurrency);

		if ("pay".equals(option)) {
			if (com.trekiz.admin.common.utils.StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, Context.ORDER_STATUS_VISA);
				if (payDetail != null) {
					if (Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != BigDecimal.ZERO.doubleValue()) {
						revBorrowAmount = payDetail.getRefundRMBDispStyle();
						revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.parseDouble(revBorrowAmount.replaceAll(",", "")));// 借款金额大写
					}
				}
			}
			result.put("revBorrowAmount", revBorrowAmount);
			result.put("revBorrowAmountDx", revBorrowAmountDx);
		}

		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));

		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap = reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY, reviewLogs);

		if (null != jobtypeusernameMap.get(8)) {//财务
			result.put("cw", jobtypeusernameMap.get(8));
		} else {
			result.put("cw", "");
		}

		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (null != jobtypeusernameMap.get(6)) {//出纳
			if (68 != UserUtils.getUser().getCompany().getId()) {
				result.put("cashier", jobtypeusernameMap.get(6));
			} else {
				result.put("cashier", "");
			}
		} else {
			result.put("cashier", "");
		}

		if (null != jobtypeusernameMap.get(10)) {//总经理
			result.put("majorCheckPerson", jobtypeusernameMap.get(10));
		} else {
			result.put("majorCheckPerson", "");
		}

		if (null != jobtypeusernameMap.get(7)) {//部门经理
			result.put("deptmanager", jobtypeusernameMap.get(7));
		} else {
			result.put("deptmanager", "");
		}

		Review review = reviewDao.findOne(Long.parseLong(revid));
		if (null != review && null == review.getPrintFlag()) {
			Date printDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(printDate);
			result.put("printDate", printDateStr);
		} else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(review.getPrintTime());
			result.put("printDate", printDateStr);
		}
		return result;
	}


	/**
	 * 把原有的Controller里面对应的打印方法内容，提取到Service层的该方法中，用于批量打印，方法内的业务逻辑不变。
	 * option=order是固定的。
	 * @param paramBean
	 * @return
     */
	public Map<String,Object> getVisaBorrowMoneyBatchFeePrint(PrintParamBean paramBean) {

		Map<String,Object> result = new HashMap<>(32,0.75f); // 用于返回数据的map

		String revid = paramBean.getReviewId();
		String batchno = paramBean.getBatchNo();
		String isPrintFlag = paramBean.getIsPrintFlag();
		String payId = paramBean.getPayId();
		String option = paramBean.getOption();
		String payStatus = null;

		result.put("revid", revid); // 把revid 传到模板模板的html页面，下载模板时使用
		result.put("batchno", batchno);
		result.put("isPrintFlag", isPrintFlag);
		result.put("payId", payId);
		result.put("option", option);
		result.put("reviewFlag",1);

		// 签证借款申请相关信息
		Map<String, String> reviewDetail = reviewService.findReview(Long.parseLong(revid));
		if (reviewDetail != null) {
			result.put("revCreateDate",DateUtils.dateFormat(reviewDetail.get("createDate"))); // 填写日期
			result.put("revUpdateDate",DateUtils.dateFormat(reviewDetail.get("updateDate"))); // 更新日期
			result.put("revBorrowRemark",reviewDetail.get("borrowRemark")); // 申报原因

			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewDetail.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			result.put("productCreater", productCreater);

			User user = UserUtils.getUser(reviewDetail.get("createBy"));
			if (null != user) {
				result.put("operatorName", user.getName()); // 经办人、领款人都为借款申请人
			} else {
				result.put("operatorName", "未知");
			}
			result.put("payDate", DateUtils.dateFormat(reviewDetail.get("updateDate"))); // 付款日期
			String currencyId = reviewDetail.get("currencyId");
			if (null != currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				result.put("revCurrency", currency);
			}

			if("order".equals(option)) {
				//获取批次借款金额
				VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"2");
				String batchborrowtotalMoney = visaFlowBatchOpration.getBatchTotalMoney();

				result.put("revBorrowAmount", MoneyNumberFormat.getThousandsMoney(Double.parseDouble(batchborrowtotalMoney), MoneyNumberFormat.THOUSANDST_POINT_TWO)); // 借款金额   改为批次总金额
				result.put("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(
						MoneyNumberFormat.getThousandsMoney(Double.parseDouble(batchborrowtotalMoney), MoneyNumberFormat.POINT_TWO))));// 借款金额大写  改为批次总金额
			}


			//----- wxw added 20151008 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			payStatus = reviewDetail.get("payStatus");
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid)||Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				result.put("payStatus","0");
			}else {
				if(com.trekiz.admin.common.utils.StringUtils.isNotBlank(payStatus)) {
					result.put("payStatus",payStatus);
				}else {
					result.put("payStatus","0");
				}
			}
		}

		if("pay".equals(option)) {
			//45需求，借款金额以每次的支付金额为标准
			String revBorrowAmount = "";
			String revBorrowAmountDx = "";
			if(com.trekiz.admin.common.utils.StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, Context.ORDER_STATUS_VISA);
				if (payDetail != null) {
					if (Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != BigDecimal.ZERO.doubleValue()) {
						revBorrowAmount = payDetail.getRefundRMBDispStyle();
						revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.parseDouble(revBorrowAmount.replaceAll(",", "")));// 借款金额大写
					}
				}
			}
			result.put("revBorrowAmount", revBorrowAmount);
			result.put("revBorrowAmountDx", revBorrowAmountDx);
		}

		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY,reviewLogs);

		if (null!=jobtypeusernameMap.get(8)) {//财务
			result.put("cw", jobtypeusernameMap.get(8));
		}else {
			result.put("cw", "");
		}

		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (null!=jobtypeusernameMap.get(6)) {//出纳
			if (68!=UserUtils.getUser().getCompany().getId()) {
				result.put("cashier", jobtypeusernameMap.get(6));
			}else {
				result.put("cashier", "");
			}
		}else {
			result.put("cashier", "");
		}

		if (null!=jobtypeusernameMap.get(10)) {//总经理
			result.put("majorCheckPerson", jobtypeusernameMap.get(10));
		}else {
			result.put("majorCheckPerson", "");
		}

		if (null!=jobtypeusernameMap.get(7)) {//部门经理
			result.put("deptmanager", jobtypeusernameMap.get(7));
		}else {
			result.put("deptmanager", "");
		}

		Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&(null==review.getPrintFlag()||0==review.getPrintFlag())) {
			Date printDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(printDate);
			result.put("printDate",printDateStr);
		}else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = null;
			try {
				printDateStr = simpleDateFormat.format(review.getPrintTime());
			} catch (Exception e) {
				e.printStackTrace();
				printDateStr = simpleDateFormat.format(new Date());
			}
			result.put("printDate",printDateStr);
		}

		/**
		 * wangxinwei 20151102 added
		 * 解决签证借款打印（批次）申请原因只能显示一个游客申请原因的问题
		 */
		String remarkString = this.getVisaBorrowBatchPrintAppReason(batchno);
		result.put("revBorrowRemark",remarkString);

		return result;
	}

	/**
	 * 更新签证借款付款单的打印状态，打印时间等，把原有的更新方法提取到Service层。
	 * @param batchNo
	 * @param printDateStr
     */
	@Transactional(readOnly = false)
	public boolean updatePrintStatus(String batchNo,String printDateStr){
		Date printDate = DateUtils.dateFormat(printDateStr,"yyyy/ MM /dd HH:mm");
		if (printDate == null || StringUtils.isBlank(batchNo)){ // 验证
			return false;
		}

		VisaFlowBatchOpration batchOperation = visaFlowBatchOprationDao.findByBatchNo(batchNo,"2");
		visaFlowBatchOprationDao.updateVisaFlowBatchUpdateTime(batchNo, "2",new Date(), UserUtils.getUser().getId());

		if (batchOperation != null && !"1".equals(batchOperation.getPrintStatus())){

			List<Map<String,Object>> reviewInfo = visaFlowBatchOprationDao.getVisaFlowReviewInfo(batchNo);
			if (CollectionUtils.isNotEmpty(reviewInfo)){
				try {
					for (Map<String, Object> map : reviewInfo) {
                        updateReviewPrintInfoById(Long.parseLong(map.get("reviewid")+""),printDate);
                    }
					visaFlowBatchOprationDao.updateVisaFlowBatchPrintTimeAndPrintStatus(batchNo, "2", printDate);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

}
