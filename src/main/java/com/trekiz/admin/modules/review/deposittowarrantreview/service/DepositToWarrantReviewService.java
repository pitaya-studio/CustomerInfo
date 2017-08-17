package com.trekiz.admin.modules.review.deposittowarrantreview.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.deposittowarrantreview.repository.DepositToWarrantReviewDao;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.RoleDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.repository.VisaDao;

@Service
public class DepositToWarrantReviewService extends BaseService {

	@Autowired
	private DepositToWarrantReviewDao depositToWarrantReviewDao;
	@Autowired
	private ReviewDetailDao reviewDetailDao;
	@Autowired
	private ReviewLogDao reviewLogDao;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private ReviewRoleLevelDao reviewRoleLevelDao;
	@Autowired
	private VisaDao visaDao;
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
	private ReviewService reviewService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private TravelerDao travelerDao;
	
	
	
	/**
	 * 查询指定审批流程的审核记录
	 * @author jiachen
	 * @DateTime 2014-12-22 下午05:21:43
	 * @return Page<Map<String,Object>>
	 */
	public Page<Map<String, Object>> findReviewInfoPage(
			Page<Map<String, Object>> page, String jobId, String status, Map<String, String> paramMap, Integer reveiwFlow) {
		
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		
		String reviewStatus = ">=";
		String reviewStatusAnd = "";
		//审核状态
		if(StringUtils.isNotBlank(status)) {
			//如果查已通过的
			if("2".equals(status)) {
				reviewStatus = ">";
				reviewStatusAnd = " AND (r.status=2 or r.status=3)";
			}else if("0".equals(status)) {
				//如果查未通过的
				reviewStatus = "=";
				reviewStatusAnd = " AND r.status=0";
			}else if("4".equals(status)) {
				//已取消
				reviewStatusAnd = " AND r.status=4";
			}else if("1".equals(status) || "666".equals(status)){
				//如果查待审核的
				reviewStatus = "=";
				reviewStatusAnd = " AND r.status=1";
			}
		}else{
			//默认查询待审核的
			reviewStatus = "=";
			reviewStatusAnd = " AND r.status=1";
		}
		
		//用户选择了职位
		UserJob userJob = null;
		if(jobId != null && !"".equals(jobId)){
			userJob = userJobDao.findOne(StringUtils.toLong(jobId));
		}
		
		
		//获取第一层级部门id
		Long pDeptId;
		//判断职位等级
		if(null != userJob && userJob.getDeptLevel() == 1){
			pDeptId = userJob.getDeptId();
		} else if(null != userJob && userJob.getDeptLevel() != 1) {
			pDeptId = userJob.getParentDept();
		}else{
			pDeptId = -1L;
		}
		
		//获取当前批发商的签证订单的审批配置
		List<Long> cpIdList = reviewCompanyDao.findReviewCompanyList(StringUtils.toLong(companyId),  reveiwFlow, pDeptId);
		String cpId = "";
		if(!cpIdList.isEmpty()) {
			cpId = cpIdList.get(0).toString();
		}else{
			return null;
		}
		String sql = "";
		if(Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT==reveiwFlow){
			sql += "SELECT o.order_no, o.total_money, o.payed_money, o.accounted_money, p.groupCode, p.productName, p.id as productId,"
				+ " p.createBy as OPId, o.create_by as salerId,o.salerName,"
				+ " r.id as reviewId, r.travelerId, o.create_date, r.createDate as reviewDate, r.createReason, r.nowLevel,"
				+ " o.agentinfo_id as agentId"
				+ " FROM review r, visa_order o, visa_products p, traveler t"
				+ " WHERE r.productType=6 AND r.orderId=o.id AND r.review_company_id=" + cpId;
				if(StringUtils.isBlank(status) || "1".equals(status)) {
					sql += " AND r.nowLevel" + reviewStatus + "(SELECT reviewLevel FROM review_role_level WHERE review_company_id=" + cpId + " AND sys_job_id=" + userJob.getJobId() + " AND delFlag = 0)";
				}
				sql += " AND r.travelerId=t.id AND o.visa_product_id = p.id" + reviewStatusAnd;
		}else if(Context.REBATES_FLOW_TYPE==reveiwFlow){
			sql += "SELECT o.order_no,o.groupRebate,o.id as visaorderid, o.total_money, o.payed_money, o.accounted_money, p.groupCode, p.productName, p.id as productId,"
				+ " p.createBy as OPId, o.create_by as salerId,o.salerName,"
				+ " r.id as reviewId, r.travelerId, o.create_date, r.createDate as reviewDate, r.createReason, r.nowLevel,"
				+ " o.agentinfo_id as agentId"
				+ " FROM review r, visa_order o, visa_products p, review_detail rd"
				+ " WHERE r.productType=6 AND r.orderId=o.id AND r.review_company_id=" + cpId;
				if(StringUtils.isBlank(status) || "1".equals(status)) {
					sql += " AND r.nowLevel" + reviewStatus + "(SELECT reviewLevel FROM review_role_level WHERE review_company_id=" + cpId + " AND sys_job_id=" + userJob.getJobId() + " AND delFlag = 0)";
				}
				sql += " AND r.id=rd.review_id AND rd.myKey='rebatesAmount' AND o.visa_product_id = p.id" + reviewStatusAnd;
		}
		//筛选条件
		if(!paramMap.isEmpty()) {
			for(String key : paramMap.keySet()) {
				String value = paramMap.get(key);
				if(StringUtils.isNotBlank(value)) {
					//如果是根据下单日期搜索
					if("orderCreateDateStart".equals(key)) {
						sql += " AND o.create_date >='" + value + " 00:00:00'";
					}else if("orderCreateDateEnd".equals(key)) {
						sql += " AND o.create_date <='" + value + " 23:59:59'";
					//如果是根据报批日期搜索
					}else if("reviewCreateDateStart".equals(key)) {
						sql += " AND r.createDate >='" + value + " 00:00:00'";
					}else if("reviewCreateDateEnd".equals(key)) {
						sql += " AND r.createDate <='" + value + " 23:59:59'";
					}else if("travelerName".equals(key)) {
						String travelerName = value.replace("'", "''");
						sql += " AND t.name like '%" + travelerName + "%'";
					}else if("rebatesAmountStrat".equals(key)) {
						sql += " AND rd.myValue>=" + value + " ";
					}else if("rebatesAmountEnd".equals(key)) {
						sql += " AND rd.myValue<=" + value + " ";
					}
					
					else if("createByName".equals(key)) {
						sql += " AND o.create_by=" + value + " ";
					}
					else if("o.create_by".equals(key)) {
						sql += " AND o.salerId=" + value + " ";
					}else if("paymentType".equals(key)){
						sql += " and o.agentinfo_id in (select id from agentinfo where paymentType = " + value+")";
					}
					 //o.create_by=345,   xiaoshou   o.saleId
					 //createByName=334,   xiadanren   p.createBy
					
					
					else if("r.printFlag".equals(key)){
						if("0".equals(value)){
							sql += " AND (r.printFlag = '" + value + "' or r.printFlag is null)";
						}else{
							sql += " AND " + key + "='" + value + "'";
						}
					}else{
						sql += " AND " + key + "='" + value + "'";
					}
				}
			}
		}
		//默认按更新时间排序
		if(StringUtils.isBlank(page.getOrderBy())) {
			sql += " order by r.updateDate desc";
		}
		
		Page<Map<String, Object>> returnMap = depositToWarrantReviewDao.findBySql(page, sql, Map.class);
		
		//拼装review的detail
		for(Map<String, Object> map : returnMap.getList()) {
			Review review = reviewDao.findOne(StringUtils.toLong(map.get("reviewId").toString()));
			Long reviewId = review.getId();
			List<ReviewDetail> detailList = reviewDetailDao.findReviewDetail(reviewId);
			for(ReviewDetail detail : detailList) {
				map.put(detail.getMykey(), detail.getMyvalue());
			}
			
			//当前用户在流程中的审核层级
			List<Integer> userLevelList = reviewRoleLevelDao.findReviewJobLevel(userJob.getJobId(), StringUtils.toLong(cpId));
			Integer userLevel = 0;
			if(! userLevelList.isEmpty()) {
				userLevel = userLevelList.get(0);
			}
			//上一环节审批
			List<ReviewLog> reviewLogList = reviewLogDao. findByReviewIdAndNowLevel(reviewId, userLevel - 1);
			//当前环节审批
			List<ReviewLog> nowReviewLogList = reviewLogDao. findByReviewIdAndNowLevel(reviewId, userLevel);
			String reviewPersonName = "";
			String remark = "";
			if(!reviewLogList.isEmpty()) {
				reviewPersonName = UserUtils.getUserNameById(reviewLogList.get(0).getCreateBy());
			}
			if(!nowReviewLogList.isEmpty()) {
				remark = nowReviewLogList.get(0).getRemark();
			}
			List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), reveiwFlow);
			if(null != map.get("agentId")) {
				map.put("agentName", agentinfoService.findOne(StringUtils.toLong(map.get("agentId").toString())).getAgentName());
			}
			map.put("jobLevel", levels.get(0));
			map.put("nowLevel", review.getNowLevel());
			map.put("reviewPersonName", reviewPersonName);
			map.put("result", review.getStatus());
			map.put("remark", remark);
			map.put("isPrintFlag", review.getPrintFlag());
			if(null != review.getPrintFlag()) {
				map.put("printTime", review.getPrintTime());
			}
			
			
			//处理预计返佣金额
			if(Context.REBATES_FLOW_TYPE==reveiwFlow){
				List<String> yujiRebatesSerialNumList = new ArrayList<String>();
				String groupRebateSerianum=null;
				if (null!=map.get("groupRebate")) {
					groupRebateSerianum= map.get("groupRebate").toString();
				}
				
				//订单预计团队返佣
        		if(null != groupRebateSerianum && !"".equals(groupRebateSerianum)) {
        			MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(groupRebateSerianum);
        			if(moneyAmount != null){
	        			BigDecimal groupRebate = moneyAmount.getAmount();
	        			if(groupRebate.compareTo(BigDecimal.ZERO)!=0){
	        				yujiRebatesSerialNumList.add(groupRebateSerianum);
	        			}
        			}
        		}
        		List<Traveler> traveList = travelerDao.findTravelerByOrderIdAndOrderType(Long.parseLong(map.get("visaorderid").toString()), 6);
        		//每个游客的预计返佣
				for(int i=0;i<traveList.size();i++){
					if(null != traveList.get(i).getRebatesMoneySerialNum() && !"".equals(traveList.get(i).getRebatesMoneySerialNum())){
						MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(traveList.get(i).getRebatesMoneySerialNum());
						if(moneyAmount != null){
							BigDecimal travelerRebate = moneyAmount.getAmount();
							if(travelerRebate.compareTo(BigDecimal.ZERO)!=0){
								yujiRebatesSerialNumList.add(traveList.get(i).getRebatesMoneySerialNum());
							}
						}
					}
				}
				
				//多个uuid相加
				String yujiRebates = OrderCommonUtil.getMoneyAmountBySerialNum(yujiRebatesSerialNumList, 2);
				map.put("yujiRebates", yujiRebates);
				
        		
			}
			
		}
		return returnMap;
	}
	
	/**
	 * 押金转担保审核通过后，将担保状态改为申请时的状态，返回修改结果
	 * @author jiachen
	 * @DateTime 2014-12-26 下午12:21:01
	 * @return boolean
	 */
	@Transactional
	public boolean updateWarrantType(String travelerId, String warrantType) {
		boolean result = false;
		
		/**
		 *  押金转担保申请时  有 两种状态：
		 *  1.担保(1)，担保+ 押金 (2) 
		 *  2.审核通过后的状态  与  申请时的选择保持一致
		 */
		if(StringUtils.isNotBlank(travelerId)) {
			Visa visa = visaDao.findByTravelerId(StringUtils.toLong(travelerId));
			visa.setGuaranteeStatus(StringUtils.toInteger(warrantType));
			visaDao.save(visa);
			if(visa.getGuaranteeStatus().toString().equals(warrantType)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * *供审核统一接口调用
	 * 押金转担保审核通过后，将担保状态改为申请时的状态，返回修改结果
	 * @author jiachen
	 * @DateTime 2015年6月2日 下午2:37:06
	 * @return void
	 */
	public void updateWarrantTypeReview(Review review) {
		if(null != review) {
			String travelerId = review.getTravelerId().toString();
			List<ReviewDetail> detailList = reviewDetailDao.findReviewDetail(review.getId());
			String warrantType = "";
			for(ReviewDetail d : detailList) {
				if("warrantType".equals(d.getMykey())) {
					warrantType = d.getMyvalue();
				}
			}
			updateWarrantType(travelerId, warrantType);
		}
	}
}
