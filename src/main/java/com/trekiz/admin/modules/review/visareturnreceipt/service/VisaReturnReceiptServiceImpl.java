package com.trekiz.admin.modules.review.visareturnreceipt.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.visareturnreceipt.repository.IVisaReturnReceiptDao;
import com.trekiz.admin.modules.review.visareturnreceipt.web.VisaReturnReceiptController;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewFlowDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.RoleDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.SysBatchNoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;

import freemarker.template.TemplateException;

@Service
public class VisaReturnReceiptServiceImpl implements IVisaReturnReceiptService {
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy 年 MM 月 dd 日");

	@Autowired
	private IVisaReturnReceiptDao visaReturnReceiptDao;
	
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
	private ReviewFlowDao reviewFlowDao;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private SysBatchNoService sysBatchNoService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	
	@Autowired
	private ReviewDetailDao reviewDetailDao;
	

	/**
	 * 还签证收据审核列表 
	 * xinwei.wang added
	 * 2014年12月17日16:23:15
	 */
	@Override
	public Page<Map<String, Object>> queryVisaReturnReceiptReviewInfo(HttpServletRequest request, HttpServletResponse response, String deFaultUserJobid) {
		
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
		//审核层级level 考虑到需要根据用户身份去查找对应的level层级的数据  暂时先注掉  如启用 则 给如下的查询方法加上这个参数
		String userJobIdStr = request.getParameter("userJobId");
		UserJob userJob = null;
		if(userJobIdStr != null &&! "".equals(userJobIdStr)){
			//如果request中取不到roleID 就用系统默认的roleID
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		
		//获取当前用户的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);
		if(userJobs == null || userJobs.size() == 0) {
			return null;
		}
		
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
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);
		if(levels == null || levels.size() == 0){
			return null;
		}

		// 1调用dao查询审核列表
		Page<Map<String, Object>> reviewPage = visaReturnReceiptDao.queryVisaReturnReceiptReviewList(request, response, groupCode,
						startTime, endtime, agent,orderCreateBy,saler, jdsaler, statusChoose,levels,userJob,reviewCompanyId,subIds);
		// 2 调用review接口查询审核信息
		//List<Map<String,String>> list = reviewService.findReviewCompanyListMap(Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT,false, subIds);
		// 3 整合信息
		List<Map<String, Object>> reviewList = reviewPage.getList();
		for(Map<String, Object> tMap : reviewList){
			tMap.put("revLevel", levels.get(0));
			//start 把游客的结算价的UUID转化为多币种的款项值
			String payprice = tMap.get("payprice") == null ? null : tMap.get("payprice").toString();
			payprice = moneyAmountService.getMoney(payprice);
			tMap.remove("payprice");
			tMap.put("payprice", payprice);
			//end 把游客的结算价的UUID转化为多币种的款项值
			/*for(Map<String, String> tList: list){
				String revid = tMap.get("revid").toString();
				String id = tList.get("id");
				if(revid != null && !"".equals(revid.trim()) && id !=null && (revid.trim()).equals(id.trim())) {
					tMap.putAll(tList);
				}
			}*/
			
			String revid = tMap.get("revid").toString();
			Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid)); //
			tMap.put("createReason", reviewAndDetailInfoMap.get("createReason"));
			tMap.put("receiptAmount", reviewAndDetailInfoMap.get("receiptAmount"));
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
					if(!isInLevel(tempRevLog.getNowLevel(), levels)){
						tMap.remove("revstatus");//审核状态
						tMap.put("revstatus", tempRevLog.getResult());
						//tMap.remove("lastoperator");//上一环节操作人
						//tMap.put("lastoperator", tempRevLog.getCreateBy());
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
	private boolean isInLevel(Integer nowLevel, List<Integer> list2) {
		for(Integer tempNum : list2){
			if(nowLevel == tempNum){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 把 List<Integer> levelList 转成 (3,4,5)格式
	 * @param levelList
	 * @return
	 */
	public  String getRoleLevelStrByLevelList(List<Integer> levelList){
		StringBuffer sb=null;
		if (null!=levelList&&levelList.size()>0) {
			sb =  new StringBuffer("(");
			for (Integer level: levelList) {
				sb.append(level+",");
			}
			String temp =sb.toString();
			temp = temp.substring(0,temp.length()-1)+")";
			//System.out.println(temp);
			return temp;
		}else {
			return null;
		}
	}
	
	/**
	 * 签证批量还收据
	 * xinwei.wang  added
	 * @param visaIds：签证Ids 结构[]
	 * @param travellerIds：游客Ids
	 * @param returnReceiptJe：还收据金额，结构[56,78,0]
	 * @param returnReceiptName：还收据人
	 * @param returnReceiptTime：归还时间
	 * @param returnReceiptRemark：备注信息
	 * @return
	 */
	public Map<String, Object> visaBatchHsj(String visaIds,
			String travellerIds, String returnReceiptJe,
			String returnReceiptName, String returnReceiptTime,
			String returnReceiptRemark) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = null;
		String[] travelerIDS = travellerIds.split(",");
		String[] returnReceiptNames = returnReceiptName.split(",");
		String[] returnReceiptJes = returnReceiptJe.split(",");
		String[] returnReceiptTimes = returnReceiptTime.split(",");
		String[] returnReceiptRemarks = returnReceiptRemark.split(",");
		// String batchNo = sysBatchNoService.getVisaBorrowMoneyBatchNo();
		try {
			/*
			 * 同一批次的还收据用同一个批次号，作为业务数据保存在表review_detail中，
			 * mykey的值为visaReturnReceiptBatchNo
			 */
			String batchNo = sysBatchNoService.getVisaReturnReceiptBatchNo();
			// System.out.println(2/0);
			// 签证批量借款
			for (int i = 0; i < travelerIDS.length; i++) {
				// travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {

					// 申请时的addReview 要用添加dept的方法
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
					Long deptId = visaOrderService.getProductPept(traveler.getOrderId());

					List<Detail> listDetail = new ArrayList<Detail>();
					listDetail.add(new Detail("receiptAmount", returnReceiptJes[i]));
					listDetail.add(new Detail("currencyId", "1"));// 默认收据币种为RMB
					listDetail.add(new Detail("receiptor", returnReceiptNames[i]));
					listDetail.add(new Detail("returnTime", returnReceiptTimes[i]));
					listDetail.add(new Detail("returnReceiptRemark",returnReceiptRemarks[i]== null ? "": returnReceiptRemarks[i]));
					listDetail.add(new Detail("visaReturnReceiptBatchNo", batchNo));

					StringBuffer reply = new StringBuffer("还签证收据");

					long addresult = reviewService.addReview(
							VisaReturnReceiptController.VISA_PRODUCT_TYPE, // 产品类型
							VisaReturnReceiptController.VISA_HSJ_FLOW_TYPE,// 流程类型 flowtype
							traveler.getOrderId() + "",// 订单ID
							traveler.getId().longValue(),// 游客ID
							Long.parseLong("0"), // 新提交的审核请置 0.重新提交审核时,等于上次审核记录的主键
							returnReceiptRemarks[i]== null ? "": returnReceiptRemarks[i],// 创建原因
							reply, 
							listDetail, 
							deptId);
					if (addresult != 0) {// 1为申请成功 2为申请失败
						reply.append("申请成功");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (resultMap == null) {
				resultMap = new HashMap<String, Object>();
			}
			resultMap.put("msg", "批量还收据申请失败！");
		}
		return resultMap;
	}
	
	//------------签证费还款单 2015-04-19 added--------------
	/**
	 * 如果为第一次打印需要更新打印状态
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public void updateReviewPrintInfoById(Long revid,Date printdate) throws Exception{
		 Review review =  reviewDao.findOne((revid));
		 if (null!=review&&(null==review.getPrintFlag()||"0".equals(review.getPrintFlag().toString()))) { 
			 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			 Date date = sf.parse(sf.format(printdate));
			 reviewDao.updateReviewPrintInfoById(revid, date, 1, UserUtils.getUser().getId());
			
		 }
	}
	

	/**
	 * 生成签证费还款单下载文件
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public File createVisaReturnMoneySheetDownloadFile(Long revid) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(revid);
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate"))));// 填写日期
			String tmp = reviewAndDetailInfoMap.get("returnReceiptRemark");
			if (null!=tmp) {
				root.put("returnReceiptRemark",reviewAndDetailInfoMap.get("returnReceiptRemark"));// 申报原因(变动)
			}else {
				root.put("returnReceiptRemark","");// 申报原因(变动)
			}
			
			root.put("receiptAmount",reviewAndDetailInfoMap.get("receiptAmount"));// 申报原因(变动)
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			root.put("payDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"))));// 付款日期
			root.put("receiptAmount",fmtMicrometer(reviewAndDetailInfoMap.get("receiptAmount")));// 借款金额
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
		root.put("revReturnAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(reviewAndDetailInfoMap.get("receiptAmount"))));// 借款金额大写
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT,reviewLogs);
		
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
		
		
	   return FreeMarkerUtil.generateFile("visareturnmoney.ftl", "visareturnmoney.doc", root);
	}
		
		
	/**
	 * wxw added 2015-04-19
	 * 数字格式化: 
	 * @param text
	 * @return
	 */
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
	
	
	/**********************************批次审核开始**********************************/
	/**********************************批次审核开始**********************************/
	/**********************************批次审核开始**********************************/
	
	
	/**
	 * 签证批量还收据
	 * xinwei.wang  added
	 * @param visaIds：签证Ids 结构[]
	 * @param travellerIds：游客Ids
	 * @param returnReceiptJe：还收据金额，结构[56,78,0]
	 * @param returnReceiptName：还收据人
	 * @param returnReceiptTime：归还时间
	 * @param returnReceiptRemark：备注信息
	 * @return
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Map<String, Object> visaBatchHsj(String batchNo, String visaIds,
			String travellerIds, String returnReceiptJe,
			String returnReceiptName, String returnReceiptTime,
			String returnReceiptRemark) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = null;
		String[] travelerIDS = travellerIds.split(",");
		String[] returnReceiptNames = returnReceiptName.split(",");
		String[] returnReceiptJes = returnReceiptJe.split(",");
		String[] returnReceiptTimes = returnReceiptTime.split(",");
		String[] returnReceiptRemarks = returnReceiptRemark.split(",");
		// String batchNo = sysBatchNoService.getVisaBorrowMoneyBatchNo();
		try {
			/*
			 * 同一批次的还收据用同一个批次号，作为业务数据保存在表review_detail中，
			 * mykey的值为visaReturnReceiptBatchNo
			 */
			//String batchNo = sysBatchNoService.getVisaReturnReceiptBatchNo();
			// System.out.println(2/0);
			// 签证批量借款
			for (int i = 0; i < travelerIDS.length; i++) {
				// travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {

					// 申请时的addReview 要用添加dept的方法
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
					Long deptId = visaOrderService.getProductPept(traveler.getOrderId());

					List<Detail> listDetail = new ArrayList<Detail>();
					listDetail.add(new Detail("receiptAmount", returnReceiptJes[i]));
					listDetail.add(new Detail("currencyId", "1"));// 默认收据币种为RMB
					listDetail.add(new Detail("receiptor", returnReceiptNames[i]));
					listDetail.add(new Detail("returnTime", returnReceiptTimes[i]));
					listDetail.add(new Detail("returnReceiptRemark",returnReceiptRemarks[i]== null ? "": returnReceiptRemarks[i]));
					listDetail.add(new Detail("visaReturnReceiptBatchNo", batchNo));

					StringBuffer reply = new StringBuffer("还签证收据");

					long addresult = reviewService.addReview(
							VisaReturnReceiptController.VISA_PRODUCT_TYPE, // 产品类型
							VisaReturnReceiptController.VISA_HSJ_FLOW_TYPE,// 流程类型 flowtype
							traveler.getOrderId() + "",// 订单ID
							traveler.getId().longValue(),// 游客ID
							Long.parseLong("0"), // 新提交的审核请置 0.重新提交审核时,等于上次审核记录的主键
							returnReceiptRemarks[i]== null ? "": returnReceiptRemarks[i],// 创建原因
							reply, 
							listDetail, 
							deptId);
					if (addresult != 0) {// 1为申请成功 2为申请失败
						reply.append("申请成功");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (resultMap == null) {
				resultMap = new HashMap<String, Object>();
			}
			resultMap.put("msg", "批量还收据申请失败！");
		}
		return resultMap;
	}
	
	
	/**
	 * 还签证收据批次审核列表 
	 * xinwei.wang added
	 * 2014年12月17日16:23:15
	 */
	@Override
	public Page<Map<String, Object>> queryVisaReturnReceiptBatchReviewInfo(HttpServletRequest request, HttpServletResponse response, String deFaultUserJobid) {
		
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
		
		//----wxw 2015-07-29 added 打印状态----
		String printstatus = request.getParameter("printstatus");
		if(printstatus == null || "".equals(printstatus.trim())){
			printstatus = null;
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
		//审核层级level 考虑到需要根据用户身份去查找对应的level层级的数据  暂时先注掉  如启用 则 给如下的查询方法加上这个参数
		String userJobIdStr = request.getParameter("userJobId");
		UserJob userJob = null;
		if(userJobIdStr != null &&! "".equals(userJobIdStr)){
			//如果request中取不到roleID 就用系统默认的roleID
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		
		//获取当前用户的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);
		if(userJobs == null || userJobs.size() == 0) {
			return null;
		}
		
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
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);
		if(levels == null || levels.size() == 0){
			return null;
		}

		// 1调用dao查询审核列表
		Page<Map<String, Object>> reviewPage = visaReturnReceiptDao.queryVisaReturnReceiptBatchReviewList(request, response, groupCode,
						startTime, endtime, agent,printstatus, saler, jdsaler, statusChoose,levels,userJob,reviewCompanyId,subIds);
		// 2 调用review接口查询审核信息
		//List<Map<String,String>> list = reviewService.findReviewCompanyListMap(Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT,false, subIds);
		// 3 整合信息
		List<Map<String, Object>> reviewList = reviewPage.getList();
		for(Map<String, Object> tMap : reviewList){
			tMap.put("revLevel", levels.get(0));
			//start 把游客的结算价的UUID转化为多币种的款项值
			String payprice = tMap.get("payprice") == null ? null : tMap.get("payprice").toString();
			payprice = moneyAmountService.getMoney(payprice);
			tMap.remove("payprice");
			tMap.put("payprice", payprice);
			//end 把游客的结算价的UUID转化为多币种的款项值
			/*for(Map<String, String> tList: list){
				String revid = tMap.get("revid").toString();
				String id = tList.get("id");
				if(revid != null && !"".equals(revid.trim()) && id !=null && (revid.trim()).equals(id.trim())) {
					tMap.putAll(tList);
				}
			}*/
			
			String revid = tMap.get("revid").toString();
			Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid)); //
			tMap.put("createReason", reviewAndDetailInfoMap.get("createReason"));
			//tMap.put("receiptAmount", reviewAndDetailInfoMap.get("receiptAmount"));
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
					if(!isInLevel(tempRevLog.getNowLevel(), levels)){
						tMap.remove("revstatus");//审核状态
						tMap.put("revstatus", tempRevLog.getResult());
						//tMap.remove("lastoperator");//上一环节操作人
						//tMap.put("lastoperator", tempRevLog.getCreateBy());
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
	 * wxwadded 2015-06-01
	 * 生成签证费批次还款单下载文件
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public File createBatchVisaReturnMoneySheetDownloadFile(Long revid,String batchno) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// 签证还收据申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(revid);
		
		//获取批次借款金额
		VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"1");
		String batchborrowtotalMoney = visaFlowBatchOpration.getBatchTotalMoney();
		
		
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate"))));// 填写日期
			String tmp = reviewAndDetailInfoMap.get("returnReceiptRemark");
			if (null!=tmp) {
				root.put("returnReceiptRemark",reviewAndDetailInfoMap.get("returnReceiptRemark"));// 申报原因(变动)
			}else {
				root.put("returnReceiptRemark","");// 申报原因(变动)
			}
			
			root.put("receiptAmount",reviewAndDetailInfoMap.get("receiptAmount"));// 申报原因(变动)
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			root.put("payDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"))));// 付款日期
			root.put("receiptAmount",fmtMicrometer(batchborrowtotalMoney));// 借款金额    改为批次总金额
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
		root.put("revReturnAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(batchborrowtotalMoney)));// 借款金额大写   改为批次总金额
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT,reviewLogs);
		
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
		 * 需求变更2015-06-01：如果为环球行用户出纳为空
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
		
		
		/**
		 * wangxinwei 20151103 added
		 * 解决还签证收据审核打印（批次），还款理由只能显示一个游客备注的问题
		 */
		String remarkString = getReturnReCeiptBatchPrintAppReason(batchno);
		root.put("returnReceiptRemark",remarkString);// 申报原因
		
	   return FreeMarkerUtil.generateFile("visabatchreturnmoney.ftl", "visabatchreturnmoney.doc", root);
	}
	
	
	

	//--------------------审核通过后处理---------------------
	/**
	 * 还签证收据 审核通过后 处理：
	 * 1.更新批次审核状态
	 * @param review
	 */
	@Transactional
	@Override
	public void visaReturnReceiptPostReviewPassedTackle4HQX(Review review){
		Long revid = review.getId();
	    
		//1.获取收据批次号
		List<ReviewDetail> batchnoList =reviewDetailDao.findReviewDetailByMykey(revid, "visaReturnReceiptBatchNo");
		String batchno = null;
		if (null!=batchnoList&&batchnoList.size()>0) {
			batchno = batchnoList.get(0).getMyvalue();
		}
			
		//2.更新批次审核状态   如有批次记录就更新
	    VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno, "1");
	    if (null!=visaFlowBatchOpration) {
	    	visaFlowBatchOprationDao.updateVisaFlowBatchOprationStatusByBatchNo(batchno, "1");
		}
		
	}

	
	
	/**********************************批次审核结束**********************************/
	/**********************************批次审核结束**********************************/
	/**********************************批次审核结束**********************************/
	
	/**
	 * 环球行游客单个借款金额的字符串  否则返回null
	 * 
	 * @param orderId
	 * @return 游客借款金额的字符串
	 */
	@Override
	public String getHSJActiveReview(String orderId,Long trvelerId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId)) {
			listReview = reviewDao.findReviewActive4HQX(6, 4,orderId,trvelerId);
			if (listReview.size()>=1) {
				List<ReviewDetail> list =reviewDetailDao.findReviewDetailByMykey(listReview.get(0).getId(), "receiptAmount");
				return list.get(0).getMyvalue()+","+listReview.get(0).getStatus();
			}
		}
		return null;
	}
	
	
	//--------------批量批次审批   开始 ---------------
	/**
	 * 多批次审核：还签证收据  多批次审核
	 * added by wxw 20150812
	 */
	@Transactional
	public boolean multiBatchReviewvisaReturnReceiptbyBatchNo(String result,String remarks, String batchnons){
		boolean isSuccess = false;
		
	    String[] batchnosArray  = batchnons.split(",");
	    
	    for (int j = 0; j < batchnosArray.length; j++) {
	    	boolean batchreviewstaus = false;//如审核通过更新批次社和状态
	    	
	    	//根据批次号获取批次审核 revidsAndCurlevelsArray
	    	StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
			buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
			buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");
			buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaReturnReceiptBatchNo' AND revd.myValue= vfbo.batch_no ");
			buffer.append(" AND vfbo.busyness_type = 1 AND rev.travelerId = tr.id AND vfbo.batch_no=");
			buffer.append("'"+batchnosArray[j]+"'");
			List<Map<String, Object>> list = reviewDao.findBySql(buffer.toString(), Map.class);
			
			StringBuilder revidsAndCurlevels = new StringBuilder("");
			
			for (Map<String, Object> map : list) {
				//签证批次还收据  中 每个批次中游客 数据组织如下    curentlevel@revid,curentlevel@revid,
				revidsAndCurlevels.append(map.get("curentlevel")+"@").append(map.get("reviewid")+",");
			}
	    	String[] revidsAndCurlevelsArray = revidsAndCurlevels.toString().split(",");
			
			
			for (int i = 0; i < revidsAndCurlevelsArray.length; i++) {
				StringBuffer reply = new StringBuffer();
				if (result == null || "".equals(result)) {
					reply.append("审批结果不能为空");
				}
				
				String nowLevel = revidsAndCurlevelsArray[i].split("@")[0];
				String revid = revidsAndCurlevelsArray[i].split("@")[1];
				
				
				int num = reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),remarks);
				List<Review> _list = reviewDao.findReviewActive(Long.parseLong(revid));
				if(_list.get(0).getTopLevel() == Integer.parseInt(nowLevel)){//如果当前层级为最高层级  说明该流程已经审核结束
					if (1==num&&"1".equals(result)) {
						batchreviewstaus = true;
					}
				}
			}
		
			//审核通过更新批次审批状态
			if (batchreviewstaus) {
				visaFlowBatchOprationDao.updateVisaFlowBatchOprationStatusByBatchNo(batchnosArray[j], "1");
			}
			
			//如果多批次正常执行到最后，才确认批次操作成功
			if (j==(batchnosArray.length-1)) {
				isSuccess = true;
			}
			
		}
		
		return isSuccess;
	}
	
	//--------------批量批次审批   结束 ---------------
	
	
	//-----wxw added 2015-09-28  处理 批次审核菜单数量 与 列表查询数量不一致的问题-----
	/**
	 * 获取用户所有审核角色的待审核数量
	 */
	@Override
	public int getBatchReviewCount4Menu(){
		int count = 0;
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);
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
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return 0;
		}
		
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);
		if(levels == null || levels.size() == 0){
			return 0;
		}
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("SELECT COUNT(1) FROM")
		     .append(" (SELECT COUNT(1) FROM review rev, visa_order vorder,visa_products vp,traveler t,review_detail revd,visa_flow_batch_opration vfbo")
		     .append(" WHERE rev.orderId = vorder.id AND vorder.visa_product_id = vp.id AND rev.travelerId = t.id AND rev.productType = 6 ")
		     .append(" AND rev.flowType = 4 AND revd.review_id = rev.id AND revd.myKey = 'visaReturnReceiptBatchNo' AND revd.myValue = vfbo.batch_no ")
		     .append(" AND vfbo.busyness_type = 1 AND rev.review_company_id = "+reviewCompanyId+" AND rev.companyId = ")
		     .append(UserUtils.getUser().getCompany().getId().intValue()+""+subIdsSQLStr)
		     .append(" AND rev.`status` = '1' AND rev.nowLevel ="+ levels.get(0) +" GROUP BY vfbo.batch_no) aa");
		return ((BigInteger)reviewDao.findBySql(sbSql.toString()).get(0)).intValue();
	}
	
	
	/**
	 * 获取批次申请原因
	 * wangxinwei 20151103 added 
	 * 解决还签证收据审核打印（批次），还款理由只能显示一个游客备注的问题
	 */
	@Override
	public String getReturnReCeiptBatchPrintAppReason(String batchNo){
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("SELECT aa.review_id FROM ")
		.append("(SELECT rd.review_id FROM review_detail rd WHERE rd.myKey = 'visaReturnReceiptBatchNo'")
		.append("AND rd.myValue = '"+batchNo+"') aa ")
	    .append("LEFT JOIN (SELECT rv.id FROM review rv WHERE rv.flowType = 4) bb ON aa.review_id = bb.id");
		List<Object> revIdList = (List<Object>)reviewDao.findBySql(sbSql.toString());
		StringBuilder sb4remark = new StringBuilder("");
		for (Object object : revIdList) {
			Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(object.toString()));
			sb4remark.append(reviewAndDetailInfoMap.get("returnReceiptRemark")+" ");// 申报原因
		}
		return sb4remark.toString();
	}
	

}
