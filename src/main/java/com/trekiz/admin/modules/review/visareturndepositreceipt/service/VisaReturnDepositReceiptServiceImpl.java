package com.trekiz.admin.modules.review.visareturndepositreceipt.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.visareturndepositreceipt.repository.IVisaReturnDepositReceiptDao;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
public class VisaReturnDepositReceiptServiceImpl implements IVisaReturnDepositReceiptService {
	
	@Autowired
	private ReviewLogDao reviewLogDao;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	@Autowired
	private  IVisaReturnDepositReceiptDao visaReturnDepositReceiptDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private ReviewCommonService reviewCommonService;

	/**
	 * 还签证押金收据审核列表  页查询
     * xinwei.wang added
	 * 2014年12月28日12:12:00
	 */
	@Override
	public Page<Map<String, Object>> queryVisaReturnDepositReceiptReviewInfo(HttpServletRequest request, 
			HttpServletResponse response, String deFaultUserJobid) {
		
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
		
		//--------游客姓名  wxw 2015-07-28 added-----------
		String travlerName = request.getParameter("travlerName");
		if(travlerName == null || "".equals(travlerName.trim())){
			travlerName = null;
		}
		
		//--------订单编号  wxw 2015-07-28 added-----------
		String orderNum = request.getParameter("orderNum");
		if(orderNum == null || "".equals(orderNum.trim())){
			orderNum = null;
		}
		
		//--------押金金额  wxw 2015-07-28 added-----------
		String depositeAmount = request.getParameter("depositeAmount");
		if(depositeAmount == null || "".equals(depositeAmount.trim())){
			depositeAmount = null;
		}
		
		//--------押金金额  wxw 2015-08-19 added-----------
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
			//如果request中取不到roleID 就用系统默认的roleID
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		
		//获取当前用户的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT);
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
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT);
		if(levels == null || levels.size() == 0){
			return null;
		}
			
		// 1调用dao查询审核列表
		Page<Map<String, Object>> reviewPage = visaReturnDepositReceiptDao.queryVisaReturnDepositReceiptReviewList(request, response, groupCode,
						startTime, endtime, agent,travlerName,orderNum,depositeAmount,orderCreateBy, saler, jdsaler, statusChoose, visaType,levels,userJob,reviewCompanyId,subIds);
		// 2 调用review接口查询审核信息
		//-----wxw  2015-08-28 弃用耗时方法
		//List<Map<String,String>> list = reviewService.findReviewCompanyListMap(Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT,false, subIds);
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
		reviewPage.setList(reviewList);
		return reviewPage;
	}
	
}
