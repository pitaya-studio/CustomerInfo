package com.trekiz.admin.modules.reviewflow.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.mail.SendMailUtil;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.entity.CostRecordHotel;
import com.trekiz.admin.modules.cost.entity.CostRecordIsland;
import com.trekiz.admin.modules.cost.entity.CostRecordLog;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.repository.CostRecordHotelDao;
import com.trekiz.admin.modules.cost.repository.CostRecordIslandDao;
import com.trekiz.admin.modules.cost.repository.CostRecordLogDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupDao;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.island.dao.ActivityIslandDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewCompany;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewFlowDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.IReviewDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.repository.VisaProductDao;

@Service
@Transactional(readOnly = true)
public class ReviewService extends BaseService {

	@Autowired
	private ReviewLogDao reviewLogDao;
	@Autowired
	private ReviewDetailDao reviewDetailDao;
	@Autowired
	private ReviewFlowDao reviewFlowDao;
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private UserJobDao userJobDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private ReviewRoleLevelDao reviewRoleLevelDao;
	@Autowired
    private TravelerDao travelerDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private CostRecordHotelDao costRecordHotelDao;
	@Autowired
	private CostRecordIslandDao costRecordIslandDao;
	@Autowired
	private CostRecordLogDao costRecordLogDao;
	@Autowired
	private ActivityHotelGroupDao activityHotelGroupDao;
	@Autowired
	private ActivityHotelDao activityHotelDao;
	@Autowired
	private ActivityIslandGroupDao activityIslandGroupDao;
	@Autowired
	private ActivityIslandDao activityIslandDao;
	
	@Autowired
	private IReviewDao reviewSqlDao;	
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private VisaProductDao visaProductDao;
	@Autowired
	private TravelActivityDao travelActivityDao;

	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private ProductOrderCommonDao orderDao;

	@Autowired
	private IslandOrderDao islandOrderDao;
	
	@Autowired
	private HotelTravelerDao hotelTravelerDao;

	/**
	 * 统一处理审核部分时间问题，去掉后面显示多余的.0(如 2015-01-04 12:45:56.0)
	 * update date 2015-01-07
	 * update by ruyi.chen
	 */
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	//返回用户拥有计调职位的数目，新行者退款审核用
	public int getOperTotal(){		
		  List<Long> jobList=userJobDao.getUserOperList(UserUtils.getUser().getId());
		  return jobList.size();
	}
	
	//检查用户是否拥有某种类型的职务 职务类型： 1-销售 2-销售主管 3-计调 4-计调主管 5-操作 0-其他
	//返回值 1 有此类型的职位，0 没有此类型的职务
	public int checkJobType(Integer jobType){
		  List<Integer> jobList=userJobDao.checkJobType(UserUtils.getUser().getId(),jobType);
		  if (jobList.size()>0) return 1;
		  else return 0;	
	}
	
	//检查用户是否拥有某种类型的职务 职务类型： 1-销售 2-销售主管 3-计调 4-计调主管 5-操作 0-其他
	//返回值 1 有此类型的职位，0 没有此类型的职务
	public int checkJobType(Integer jobTypeA,Integer jobTypeB){
		  List<Integer> jobList=userJobDao.checkJobType(UserUtils.getUser().getId(),jobTypeA,jobTypeB);
		  if (jobList.size()>0) return 1;
		  else return 0;
		
		 
	}
	
	/*函数功能：获得用户对某一审核流程具有的审核层级 列表 ，jobId 职务
	 * 输入参数: deptId 部门Id；jobId 职务; flowId 流程类型(退款，改签..),     
	 * 系统从 review_role_level表中取出 用户对此订单流程具有的审核层级列表 
	 * 当产品类型为null时 认为是查询所有的产品 
	 * 输出参数:List<Integer> 审核层级 列表 */	 
	public List<Integer> getJobLevel(Long deptId,Long jobId,Integer flowId){		
		List<Long> cpidList= new ArrayList<Long>();	
		List<Integer> levelList= new ArrayList<Integer>();	
		Long companyId = UserUtils.getUser().getCompany().getId();		
		
		cpidList=reviewCompanyDao.findReviewCompanyListDept(companyId,flowId,deptId);	
			
	    if (cpidList.size()==0) { 
	    	return levelList; //对于此订单审核流程，reviewCompany表没有数据 
	    } else if (cpidList.size()>1 ) {
	    	return levelList; //对于此订单审核流程，同一部门 reviewCompany表有多条数据也是错误的
	    }	    
	    Long reviewCompanyId=cpidList.get(0); 
	    List<Integer> nowLevel= new ArrayList<Integer>();	  
	    nowLevel=reviewRoleLevelDao.findReviewJobLevel(jobId,reviewCompanyId); 
	    if (nowLevel.size() >=0){
	       levelList.addAll(nowLevel);
	    }
	    return levelList;		
	}	  
	
	
	public List<Integer> getRoleLevel(Long deptId,Integer roleId,Integer flowId){
		return null;
	}

	/*
	 * 提交审核申请 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...) Orderid=订单id
	 * CreateReason= 提交申请原因 travelerId=游客Id, 如果团体订单，travelerId 请置0 ReviewId:
	 * 新提交的审核请置 0. 重新提交审核时,等于上次审核记录的主键(review.id).用来把上次审核记录设置active=0
	 * StringBuffer reply:新增失败时的回复信息,供前台显示 订单是否可以多次提交审批(例如退款分2次进行)保存在
	 * review_company.redo 字段 (1:可以多次提交,0:不可以多次提交)
	 * 
	 * 返回值 0:添加失败;成功时返回添加成功记录的主键
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public long addReview(Integer productType, Integer flowType,
			String orderId, Long travelerId, Long reviewId,
			String createReason, StringBuffer reply, List<Detail> listDetail, Long deptId) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		if (companyId == null) {
			reply.append("用户所属经销商Id不存在");
			return 0;
		}
		if (orderId == null || orderId.trim().length() == 0) {
			reply.append("订单ID不能为空");
			return 0;
		}	
		if (flowType == null) {
			reply.append("流程类型 (退款,改签...)不能为空");
			return 0;
		}
		//判断结算单锁定状态 如果锁定 不能继续审核 add by chy 2015年6月8日17:21:56
		if(productType == 7) {//机票
			AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
			if(airticketOrder == null || airticketOrder.getAirticketId() == null){
				reply.append("错误的订单数据");
				return 0;
			}
			ActivityAirTicket airTicket = activityAirTicketDao.findOne(airticketOrder.getAirticketId());
			if(airTicket == null){
				reply.append("错误的产品数据");
				return 0;
			}
			if(airTicket.getLockStatus() != null && 1 == airTicket.getLockStatus()){
				reply.append("结算单已锁定，不能发起流程");
				return 0;
			}
		} else if(productType == 6) {//签证
			if (flowType != 4 && flowType != 5) { // 批量操作：借款和还收据不判断结算单（蔡）
				VisaOrder visaOrder = visaOrderDao.findByOrderId(Long.parseLong(orderId));
				if(visaOrder == null || visaOrder.getVisaProductId() == null){
					reply.append("错误的订单数据");
					return 0;
				}
				VisaProducts visaProducts = visaProductDao.findOne(visaOrder.getVisaProductId());
				if(visaProducts == null){
					reply.append("错误的产品数据");
					return 0;
				}
				if(visaProducts.getLockStatus() != null && 1 == visaProducts.getLockStatus()){
					reply.append("结算单已锁定，不能发起流程");
					return 0;
				}
			}
		} else if(productType == 11) {//酒店
			
		} else if(productType == 12) {//海岛游
			Integer id = Integer.valueOf(orderId);
			IslandOrder order = islandOrderDao.getById(id);
			if(order == null ||  StringUtils.isBlank(order.getActivityIslandGroupUuid())){
				reply.append("错误的订单数据");
				return 0;
			}
			ActivityIslandGroup activityGroup = activityIslandGroupDao.getByUuid(order.getActivityIslandGroupUuid());
			if(activityGroup == null){
				reply.append("错误的团期数据");
				return 0;
			}
			if(activityGroup.getLockStatus() != null && 1 == activityGroup.getLockStatus()){
				reply.append("结算单已锁定，不能发起流程");
				return 0;
			}
		} else {//参团 等、、、
			ProductOrderCommon order = orderDao.findOne(Long.parseLong(orderId));
			if(order == null ||  order.getProductGroupId() == null){
				reply.append("错误的订单数据");
				return 0;
			}
			ActivityGroup activityGroup = activityGroupDao.findOne(order.getProductGroupId());
			if(activityGroup == null){
				reply.append("错误的团期数据");
				return 0;
			}
			if(activityGroup.getLockStatus() != null && 1 == activityGroup.getLockStatus()){
				reply.append("结算单已锁定，不能发起流程");
				return 0;
			}
		}
		//添加流程互斥判断(无优先级互斥流程)
//		StringBuffer sf = new StringBuffer();
//		boolean flag = getOrderApplyReviewInfo(orderId,flowType.toString(),productType.toString(),sf);
//		if(!flag){
//			reply.append(sf.toString());
//			return 0;
//		}		
		if (deptId == null) {
			reply.append("所属部门不能为空.");
			return 0;
		}
		Department department= departmentDao.findOne(deptId);
		if(department==null || department.getLevel()!=2){
			reply.append("用户所在的部门不能提交审核");
			return 0;
		}
	
		List<ReviewCompany> listCompany = new ArrayList<ReviewCompany>();
		listCompany = reviewCompanyDao.findReviewCompanyDept(companyId,flowType,department.getParentId());
		if (listCompany.size() != 1){
			reply.append("未配置审核流程!");
			return 0;
		}
			
		int redo = listCompany.get(0).getRedo();
		if (redo == 0) {
			/* reviewCompany.redo==0 时,订单不可以多次提交审批 */
			List<Review> listReview = new ArrayList<Review>();
			if (travelerId == null || (long) travelerId == 0) {
				listReview = reviewDao.findReviewCheckDept(productType, flowType,
						orderId,deptId);
			} else {
				listReview = reviewDao.findReviewActiveDept(productType, flowType,
						orderId, travelerId,deptId);
			}
			if (listReview.size() >= 1) {
				reply.append("此审核申请已经提交过了，不能重复提交");
				return 0;
			}
		}
		long reviewCompanyId = listCompany.get(0).getId();
		int topLevel = listCompany.get(0).getTopLevel();
		int nowLevel = 1;
		long createBy = UserUtils.getUser().getId();
		Date createDate = new Date();
		Date updateDate = new Date();
		String updateByName = "";
		String denyReason = "";
		int status = 1;
		int active = 1;
		if (reviewId != null && (long) reviewId > 0) {
			reviewDao.removeReview(reviewId);
		}
		Review review = new Review(reviewCompanyId, topLevel, nowLevel, productType,
				orderId, flowType, companyId, travelerId, createReason,
				denyReason, createBy, createDate, updateDate, updateByName,
				status, active,deptId);
		reviewDao.save(review);
		Long rid = review.getId();
		if (rid == null) {
			reply.append("审核申请提交失败");
			return 0;
		}
		List<ReviewDetail> listReviewDetail = new ArrayList<ReviewDetail>();
		ReviewDetail reviewDetail;
		for (Detail detail : listDetail) {
			reviewDetail = new ReviewDetail(rid, detail.getKey(), detail.getValue());
			listReviewDetail.add(reviewDetail);
		}
		if (listReviewDetail.size() > 0) {
			reviewDetailDao.save(listReviewDetail);
		}
		//发邮件 add by chy 2015年5月5日17:51:00
		String flowName = reviewFlowDao.findOne(flowType).getFlowName();
		List<String> email = getNextReviewEmail(rid);
		String userName = UserUtils.getUser(review.getCreateBy()).getName();//发起人名称
		String productTypeName = DictUtils.getDictLabel(review.getProductType() + "", "order_type", "");//产品类型
		String[] emails = new String[email.size()];
		int i = 0;
		for(String tmp : email){
			emails[i] = tmp;
			i++;
		}
		if(emails.length != 0){
			SendMailUtil.sendSimpleMail(emails, "有" + userName + "发起" + productTypeName + "的" + flowName + "待审核", "您好，您有待处理的" + flowName + ",请及时处理");
		}
		return rid;
	}
	/**
	 * 
	 * addSuccessReview 增加成功标示的流程
	 * @param productType 产品类型
	 * @param flowType 流程类型
	 * @param orderId 订单编号
	 * @param travelerId 游客编号
	 * @param reviewId 
	 * @param createReason 申请人
	 * @param reply
	 * @param listDetail 业务参数
	 * @param deptId 部门编号
	 * @return
	 * @exception
	 * @since  1.0.0
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public long addSuccessReview(Integer productType, Integer flowType,
			String orderId, Long travelerId, Long reviewId,
			String createReason, StringBuffer reply, List<Detail> listDetail, Long deptId) {
		Long companyId = UserUtils.getUser().getCompany().getId();
//		if (companyId == null) {
//			reply.append("用户所属经销商Id不存在");
//			return 0;
//		}
//		if (orderId == null || orderId.trim().length() == 0) {
//			reply.append("订单ID不能为空");
//			return 0;
//		}	
//		if (flowType == null) {
//			reply.append("流程类型 (退款,改签...)不能为空");
//			return 0;
//		}
//		if (deptId == null) {
//			reply.append("所属部门不能为空.");
//			return 0;
//		}
		Department department= departmentDao.findOne(deptId);
//		if(department==null || department.getLevel()!=2){
//			reply.append("用户所在的部门不能提交审核");
//			return 0;
//		}
	
		List<ReviewCompany> listCompany = new ArrayList<ReviewCompany>();
		listCompany = reviewCompanyDao.findReviewCompanyDept(companyId,flowType,department.getParentId());
		if (listCompany.size() != 1){
			reply.append("未配置审核流程!");
			return 0;
		}
			
		int redo = listCompany.get(0).getRedo();
		if (redo == 0) {
			/* reviewCompany.redo==0 时,订单不可以多次提交审批 */
//			List<Review> listReview = new ArrayList<Review>();
//			if (travelerId == null || (long) travelerId == 0) {
//				listReview = reviewDao.findReviewCheckDept(productType, flowType,
//						orderId,deptId);
//			} else {
//				listReview = reviewDao.findReviewActiveDept(productType, flowType,
//						orderId, travelerId,deptId);
//			}
//			if (listReview.size() >= 1) {
//				reply.append("此审核申请已经提交过了，不能重复提交");
//				return 0;
//			}
		}
		long reviewCompanyId = listCompany.get(0).getId();
		int topLevel = listCompany.get(0).getTopLevel();
		int nowLevel = topLevel;
		long createBy = UserUtils.getUser().getId();
		Date createDate = new Date();
		Date updateDate = new Date();
		String updateByName = "";
		String denyReason = "";
		int status = 2;
		int active = 1;
		if (reviewId != null && (long) reviewId > 0) {
			reviewDao.removeReview(reviewId);
		}
		Review review = new Review(reviewCompanyId, topLevel, nowLevel, productType,
				orderId, flowType, companyId, travelerId, createReason,
				denyReason, createBy, createDate, updateDate, updateByName,
				status, active,deptId);
		reviewDao.save(review);
		Long rid = review.getId();
		if (rid == null) {
			reply.append("审核申请提交失败");
			return 0;
		}
		List<ReviewDetail> listReviewDetail = new ArrayList<ReviewDetail>();
		ReviewDetail reviewDetail;
		for (Detail detail : listDetail) {
			reviewDetail = new ReviewDetail(rid, detail.getKey(),
					detail.getValue());
			listReviewDetail.add(reviewDetail);
		}
		if (listReviewDetail.size() > 0) {
			reviewDetailDao.save(listReviewDetail);
		}
		return rid;
	}
	/**
	 * 不加申请限制的申请审核方法 为了特殊需求而生 by chy2014年12月30日11:06:58
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @param active
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public long addReviewNoLimit(Integer productType, Integer flowType,
			String orderId, Long travelerId, Long reviewId,
			String createReason, StringBuffer reply, List<Detail> listDetail,Long deptId) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		if (companyId == null) {
			reply.append("用户所属经销商Id不存在");
			return 0;
		}
		if (orderId == null || orderId.trim().length() == 0) {
			reply.append("订单ID不能为空");
			return 0;
		}
		if (productType == null) {
			reply.append("业务类型 (机票,签证...)不能为空");
			return 0;
		}
		if (flowType == null) {
			reply.append("流程类型 (退款,改签...)不能为空");
			return 0;
		}
		if (deptId == null) {
			reply.append("所属部门不能为空.");
			return 0;
		}		
		Department department= departmentDao.findOne(deptId);
		if(department==null || department.getLevel()!=2){
			reply.append("用户所在的部门不能提交审核");
			return 0;
		}	
	
		List<ReviewCompany> listCompany = new ArrayList<ReviewCompany>();
		listCompany = reviewCompanyDao.findReviewCompanyDept(companyId, flowType,department.getParentId());
		if (listCompany.size() != 1){
			return 0;
		}
		long reviewCompanyId = listCompany.get(0).getId();
		int topLevel = listCompany.get(0).getTopLevel();
		int nowLevel = 1;
		long createBy = UserUtils.getUser().getId();
		Date createDate = new Date();
		Date updateDate = new Date();
		String updateByName = "";
		String denyReason = "";
		int status = 1;
		int active = 1;
		if (reviewId != null && (long) reviewId > 0) {
			reviewDao.removeReview(reviewId);
		}
		Review review = new Review(reviewCompanyId, topLevel, nowLevel, productType,
				orderId, flowType, companyId, travelerId, createReason,
				denyReason, createBy, createDate, updateDate, updateByName,
				status, active,deptId);
		reviewDao.save(review);
		Long rid = review.getId();
		if (rid == null) {
			reply.append("审核申请提交失败");
			return 0;
		}
		List<ReviewDetail> listReviewDetail = new ArrayList<ReviewDetail>();
		ReviewDetail reviewDetail;
		for (Detail detail : listDetail) {
			reviewDetail = new ReviewDetail(rid, detail.getKey(),
					detail.getValue());
			listReviewDetail.add(reviewDetail);
		}
		if (listReviewDetail.size() > 0) {
			reviewDetailDao.save(listReviewDetail);
		}
		return rid;
	}
	
	/*
	 * 获取类订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id active： 是否只查询 active=1的记录
	 * deptId 是产品所属部门Id
	 */
	public List<Map<String, String>> findReviewListMap(Integer productType,
			Integer flowType, String orderId, boolean active,Long deptId) {

		List<Map<String, String>> listMap = Lists.newArrayList();
		List<Review> list = Lists.newArrayList();
		if (active == true)
			list = reviewDao.findReviewActiveDept(productType, flowType, orderId, deptId);
		else
			list = reviewDao.findReviewDept(productType, flowType, orderId, deptId);

		for (Review review : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", orderId.toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			map.put("deptId", review.getDeptId().toString());
			
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = review.getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}

			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			
			map.put("updateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(review.getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					//表review_detail数据错误修正
					String myValue;
					if (detail.getMyvalue()!=null){
						myValue = detail.getMyvalue();
						if(myValue.contains(",")){
							myValue = myValue.substring(0, myValue.length()-1);
						}
					}else{
						myValue = "";
					}
					map.put(detail.getMykey(), myValue);
//					map.put(detail.getMykey(), detail.getMyvalue() != null ? detail.getMyvalue(): "");
				}
			}
			map.put("createBy", review.getCreateBy().toString());
			listMap.add(map);
		}
		return listMap;
	}
	
	/*
	 * 获取类订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id 
	 * deptId 是产品所属部门Id
	 * 查询单个退款详情（根据Review的id）
	 */
	public List<Map<String, String>> findReviewListMapSingle(Integer productType,
			Integer flowType, String orderId, Long id) {

		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		List<Review> list = new ArrayList<Review>();
		list = reviewDao.findReviewById(productType, flowType, orderId,id);

		for (Review review : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", flowType.toString());
			
			map.put("deptId", review.getDeptId().toString());
			
			map.put("orderId", orderId.toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = review.getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			
			map.put("updateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(review.getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			map.put("createBy", review.getCreateBy().toString());
			listMap.add(map);
		}
		return listMap;
	}
	
	/*
	 * 获取类订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id  上面方法的重载 by chy 适应退款特殊需求的
	 * deptId 是产品所属部门Id
	 * 查询单个退款详情（根据Review的id）
	 */
	public List<Map<String, String>> findReviewListMapSingle(Integer productType,
			List<Integer> flowTypes, String orderId, Long id) {

		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		List<Review> list = new ArrayList<Review>();
		for(Integer flowType : flowTypes){
			list.addAll(reviewDao.findReviewById(productType, flowType, orderId,id));
		}
		for (Review review : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", review.getFlowType().toString());
			
			map.put("deptId", review.getDeptId().toString());
			
			map.put("orderId", orderId.toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = review.getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			
			map.put("updateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(review.getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			map.put("createBy", review.getCreateBy().toString());
			listMap.add(map);
		}
		return listMap;
	}
	
	/**
	 * 获取类订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 *  deptId 是产品所属部门Id
	 * Orderid=订单id active： 是否只查询 active=1的记录 
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @param active
	 * @param t
	 * @author HPT
	 * @return 重载方法，返回List<Map<String, Object>>类型的数据
	 */
	public List<Map<String, Object>> findReviewListMap(Integer productType,
			Integer flowType, String orderId, boolean active,String t,Long deptId) {

		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActiveDept(productType, flowType, orderId,deptId);
		else
			list = reviewDao.findReviewDept(productType, flowType, orderId,deptId);

		for (Review review : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("deptId", review.getDeptId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", orderId.toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = review.getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
//			map.put("createDate", review.getCreateDate());
//			map.put("updateDate", review.getUpdateDate());
			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			map.put("updateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(review.getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			map.put("createBy", review.getCreateBy().toString());
//			if (map.get("currencyname") == null && map.get("currencyid") != null) {				
//				map.remove("currencyname");
//				map.put("currencyname", currencyDao.findById((Long)map.get("currencyid")).getCurrencyName());
//			}
			listMap.add(map);
		}
		return listMap;
	}
	
	/**
	 * 通过增加Review的id属性查询，查询单个改价信息,因为Id是主键，不需要deptId 部门入参
	 * @param id
	 */
	public List<Map<String, Object>> findReviewListMapById(Integer productType,
			Integer flowType, String orderId,Long id) {

		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		List<Review> list = new ArrayList<Review>();
		
		list = reviewDao.findReviewById(productType, flowType, orderId,id);

		for (Review review : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("deptId", review.getDeptId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", orderId.toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = review.getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
//			map.put("createDate", review.getCreateDate());
//			map.put("updateDate", review.getUpdateDate());
			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			map.put("updateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(review.getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			map.put("createBy", review.getCreateBy().toString());
			listMap.add(map);
		}
		return listMap;
	}
	
	//废弃函数
	public List<Map<String, String>> findReviewCompanyListMap(
			Integer productType, Integer flowType, boolean active) {
		return null;
	}
	
	/*
	 * 获取类订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * CompanyId=经销商Id active： 是否只查询 active=1的记录
	 * 
	 * 如果当前用户使用的部门Level=2, 则入参List<Long> deptId 只有一条数据，就是这个部门Id
	 * 如果当前用户使用的部门Level=1, 则入参List<Long> deptId 是此部门的所有直接下级部门 
	 * */
	public List<Map<String, String>> findReviewCompanyListMap(
			Integer productType, Integer flowType, boolean active, List<Long> deptId) {
		long companyId = UserUtils.getUser().getCompany().getId();
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		List<Review> list = new ArrayList<Review>();
		
		 
		if (active == true) {
			// 如果产品类型为null 则调用这个没有产品限制的查询方法 modify by chy 2014年11月26日10:57:56
			if (productType == null) {
				list = reviewDao.findReviewCompanyAllPrdActiveDept(flowType,
						companyId,deptId);
			} else if (productType == 0){ //查所有团期产品, 即： prdouctType <6
				list = reviewDao.findReviewCompanyActiveDept(flowType,
						companyId,deptId);
			} else {
				list = reviewDao.findReviewCompanyActiveDept(productType, flowType,
						companyId,deptId);
			}
		} else {
			// 如果产品类型为null 则调用这个没有产品限制的查询方法 modify by chy 2014年11月26日10:57:56
			if (productType == null) {
				list = reviewDao.findReviewCompanyAllPrdDept(flowType, companyId,deptId);
			} else if (productType == 0){ //查所有团期产品, 即： prdouctType <6
				list = reviewDao.findReviewCompanyDept(flowType,
						companyId,deptId);
			} else {
				list = reviewDao.findReviewCompanyDept(productType, flowType,
						companyId,deptId);
			}
		}

		for (Review review : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("deptId", review.getDeptId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			// map.put("productType", productType.toString()); 注释掉了这句 改为了下面这一行
			// 从查询结果中取数据
			map.put("productType", review.getProductType().toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", review.getOrderId().toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = review.getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			
			map.put("updateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(review.getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			map.put("createBy", review.getCreateBy().toString());
			listMap.add(map);
		}
		return listMap;
	}

/*
	 * 获取类订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * CompanyId=经销商Id active： 是否只查询 active=1的记录
	 * 
	 * 如果当前用户使用的部门Level=2, 则入参List<Long> deptId 只有一条数据，就是这个部门Id
	 * 如果当前用户使用的部门Level=1, 则入参List<Long> deptId 是此部门的所有直接下级部门 
	 * */
	public List<Map<String, String>> findReviewCompanyNoDetail(
			Integer productType, Integer flowType, boolean active, List<Long> deptId) {
		long companyId = UserUtils.getUser().getCompany().getId();
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		List<Review> list = new ArrayList<Review>();
		
		 
		if (active == true) {
			// 如果产品类型为null 则调用这个没有产品限制的查询方法 modify by chy 2014年11月26日10:57:56
			if (productType == null) {
				list = reviewDao.findReviewCompanyAllPrdActiveDept(flowType,
						companyId,deptId);
			} else if (productType == 0){ //查所有团期产品, 即： prdouctType <6
				list = reviewDao.findReviewCompanyActiveDept(flowType,
						companyId,deptId);
			} else {
				list = reviewDao.findReviewCompanyActiveDept(productType, flowType,
						companyId,deptId);
			}
		} else {
			// 如果产品类型为null 则调用这个没有产品限制的查询方法 modify by chy 2014年11月26日10:57:56
			if (productType == null) {
				list = reviewDao.findReviewCompanyAllPrdDept(flowType, companyId,deptId);
			} else if (productType == 0){ //查所有团期产品, 即： prdouctType <6
				list = reviewDao.findReviewCompanyDept(flowType,
						companyId,deptId);
			} else {
				list = reviewDao.findReviewCompanyDept(productType, flowType,
						companyId,deptId);
			}
		}

		for (Review review : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("deptId", review.getDeptId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			// map.put("productType", productType.toString()); 注释掉了这句 改为了下面这一行
			// 从查询结果中取数据
			map.put("productType", review.getProductType().toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", review.getOrderId().toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			map.put("createBy", review.getCreateBy().toString());
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = review.getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			
			map.put("updateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());
		
			listMap.add(map);
		}
		return listMap;
	}


	/*
	 * 通过 ReviewId 获得审核详细信息 Reviewid = 审核记录主键= Review表.Id
	 */
	public Map<String, String> findReview(Long reviewId) {
		Map<String, String> map = new HashMap<String, String>();
		List<Review> list = new ArrayList<Review>();
		list = reviewDao.findReview(reviewId);
		if (list.size() == 1) {
			map.put("id", list.get(0).getId().toString());
			map.put("cpid", list.get(0).getReviewCompanyId().toString());
			map.put("deptId", list.get(0).getDeptId().toString());
			map.put("topLevel", list.get(0).getTopLevel().toString());
			map.put("curLevel", list.get(0).getNowLevel().toString());//新加查询当前审核层级 key value by chy2015年8月24日10:46:24
			map.put("productType", list.get(0).getProductType().toString());
			map.put("flowType", list.get(0).getFlowType().toString());
			map.put("orderId", list.get(0).getOrderId().toString());
			map.put("createReason", list.get(0).getCreateReason());
			map.put("denyReason", list.get(0).getDenyReason());
			map.put("createBy", list.get(0).getCreateBy().toString());
			map.put("payStatus",list.get(0).getPayStatus() == null ? "0" : list.get(0).getPayStatus().toString());
			Long updateBy = list.get(0).getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = list.get(0).getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}

			map.put("createDate", sdf.format(list.get(0).getCreateDate()));
			map.put("updateDate", sdf.format(list.get(0).getUpdateDate()));
			map.put("updateByName", list.get(0).getUpdateByName());
			map.put("status", list.get(0).getStatus().toString());
			map.put("active", list.get(0).getActive().toString());
			/*新加打印时间的查询 start by chy 2015年4月13日19:57:14*/
			if (list.get(0).getPrintTime()!=null) {
				map.put("printTime", sdf.format(list.get(0).getPrintTime()));
			}else {
				map.put("printTime", null);
			}
			
			/*新加打印时间的查询  end*/
			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(list.get(0).getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					if("createBy".equals(detail.getMykey())){//新加过滤掉createBy by chy 2015年7月30日19:44:41
						continue;
					}
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
		}
		return map;
	}
	
	/*
	 * 通过 ReviewId 获得审核详细信息 Reviewid = 审核记录主键= Review表.Id
	 */
	public Map<String, Object> findReviewObject(Long reviewId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Review> list = new ArrayList<Review>();
		list = reviewDao.findReview(reviewId);
		if (list.size() == 1) {
			map.put("id", list.get(0).getId().toString());
			map.put("cpid", list.get(0).getReviewCompanyId().toString());
			map.put("deptId", list.get(0).getDeptId().toString());
			map.put("topLevel", list.get(0).getTopLevel().toString());
			map.put("productType", list.get(0).getProductType().toString());
			map.put("flowType", list.get(0).getFlowType().toString());
			map.put("orderId", list.get(0).getOrderId().toString());
			map.put("createReason", list.get(0).getCreateReason());
			map.put("denyReason", list.get(0).getDenyReason());
			
			Long updateBy = list.get(0).getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = list.get(0).getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
//			map.put("createDate", list.get(0).getCreateDate().toString());
//			map.put("updateDate", list.get(0).getUpdateDate().toString());
			map.put("createDate", list.get(0).getCreateDate());
			map.put("updateDate", list.get(0).getUpdateDate());
			map.put("updateByName", list.get(0).getUpdateByName());
			map.put("status", list.get(0).getStatus().toString());
			map.put("active", list.get(0).getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(list.get(0).getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			map.put("createBy", list.get(0).getCreateBy().toString());
		}
		return map;
	}

	/*
	 * 获取某条订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id
	 */
	public Map<String, String> findReviewActive(Integer productType,
			Integer flowType, String orderId,Long deptId) {
		Map<String, String> map = new HashMap<String, String>();
		List<Review> list = new ArrayList<Review>();
		list = reviewDao.findReviewActive(productType, flowType, orderId);
		if (list.size() == 1) {
			map.put("id", list.get(0).getId().toString());
			map.put("cpid", list.get(0).getReviewCompanyId().toString());
			map.put("deptId", list.get(0).getDeptId().toString());
			map.put("topLevel", list.get(0).getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", orderId.toString());
			map.put("createReason", list.get(0).getCreateReason());
			map.put("denyReason", list.get(0).getDenyReason());
			
			Long updateBy = list.get(0).getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = list.get(0).getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
//			map.put("createDate", list.get(0).getCreateDate().toString());
//			map.put("updateDate", list.get(0).getUpdateDate().toString());
			map.put("createDate", sdf.format(list.get(0).getCreateDate()));
			map.put("updateDate", sdf.format(list.get(0).getUpdateDate()));
			map.put("updateByName", list.get(0).getUpdateByName());
			map.put("status", list.get(0).getStatus().toString());
			map.put("active", list.get(0).getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(list.get(0).getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			map.put("createBy", list.get(0).getCreateBy().toString());
		}
		return map;
	}

	/*
	 * 获取某条订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id travelerId=游客Id
	 */
	public Map<String, String> findReviewActive(Integer productType,
			Integer flowType, String orderId, Long travelerId,Long deptId) {
		Map<String, String> map = new HashMap<String, String>();
		List<Review> list = new ArrayList<Review>();
		list = reviewDao.findReviewActive(productType, flowType, orderId,
				travelerId);
		if (list.size() == 1) {
			map.put("id", list.get(0).getId().toString());
			map.put("cpid", list.get(0).getReviewCompanyId().toString());
			map.put("deptId", list.get(0).getDeptId().toString());
			map.put("topLevel", list.get(0).getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", orderId.toString());
			map.put("createReason", list.get(0).getCreateReason());
			map.put("denyReason", list.get(0).getDenyReason());
			
			Long updateBy = list.get(0).getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			map.put("travelerId", travelerId.toString());
//			map.put("createDate", list.get(0).getCreateDate().toString());
//			map.put("updateDate", list.get(0).getUpdateDate().toString());
			map.put("createDate", sdf.format(list.get(0).getCreateDate()));
			map.put("updateDate", sdf.format(list.get(0).getUpdateDate()));
			map.put("updateDateByName", list.get(0).getUpdateByName());
			map.put("status", list.get(0).getStatus().toString());
			map.put("active", list.get(0).getActive().toString());

			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(list.get(0).getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			map.put("createBy", list.get(0).getCreateBy().toString());
		}
		return map;
	}
	
	/*
	 * 获取某条订单变更 的审核信息详情 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id travelerId=游客Id
	 */
	public List<Map<String, String>> findReviewInfoActive(Integer productType,
			Integer flowType, String orderId, Long travelerId) {
		List<Review> list = new ArrayList<Review>();
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		list = reviewDao.findReviewActive(productType, flowType, orderId,
				travelerId);
		for (Review review : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("deptId", review.getDeptId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", orderId.toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			map.put("travelerId", travelerId.toString());
			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			map.put("updateDateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());

			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(review.getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			map.put("createBy", review.getCreateBy().toString());
			listMap.add(map);
		}
		return listMap;
	}

	/* 直接增加 ReviewDetail 记录 */
	public int addReviewDetail(Long reviewId, List<Detail> listDetail) {
		List<ReviewDetail> listReviewDetail = new ArrayList<ReviewDetail>();
		ReviewDetail reviewDetail;
		for (Detail detail : listDetail) {
			reviewDetail = new ReviewDetail(reviewId, detail.getKey(),
					detail.getValue());
			listReviewDetail.add(reviewDetail);
		}
		if (listReviewDetail.size() > 0) {
			reviewDetailDao.save(listReviewDetail);
			return 0;
		} else {
			return 1;
		}
	}

	/*
	 * 查看订单变更 审核列表,active=1 不包括驳回的记录 productType=业务类型 (机票，签证...) flowType=流程类型
	 * (退款，改签...) active： 是否只查询 active=1的记录 companyId: 经销商Id
	 * 
	 * 废弃函数*/
	public List<Review> findReviewCompany(Integer productType,
			Integer flowType, Long companyId, boolean active,List<Long> deptId) {
		
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewCompanyActiveDept(productType, flowType,
					companyId,deptId);
		else
			reviewDao.findReviewCompanyDept(productType, flowType, companyId,deptId);
		return list;
	}

	
	/*
	 * 查看订单变更 审核列表,active=1 不包括驳回的记录 productType=业务类型 (机票，签证...) flowType=流程类型
	 * (退款，改签...) active： 是否只查询 active=1的记录 companyId: 经销商Id
	 */
	public List<Review> findReviewCompany(Integer productType,
			Integer flowType, Long companyId, boolean active,Long deptId) {
		
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewCompanyActiveDept(productType, flowType,
					companyId,deptId);
		else
			reviewDao.findReviewCompanyDept(productType, flowType, companyId,deptId);
		return list;
	}
	/*
	 * 查看某条订单变更 审核列表，包括驳回的记录 productType=业务类型 (机票，签证...) flowType=流程类型
	 * (退款，改签...) Orderid=订单id active： 是否只查询 active=1的记录
	 */
	public List<Review> findReview(Integer productType, Integer flowType,
			String orderId, boolean active,Long deptId) {
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActiveDept(productType, flowType, orderId,deptId);
		else
			list = reviewDao.findReviewDept(productType, flowType, orderId,deptId);
		return list;
	}
	
	/*
	 * //新行者账号申请签证退款时，会根据不同职务，生成不同类型审核流程 addBy jiachen
	 * 查看某条订单变更 审核列表，包括驳回的记录 productType=业务类型 (机票，签证...) flowType=流程类型
	 * (退款，改签...) Orderid=订单id active： 是否只查询 active=1的记录
	 */
	public List<Review> findReview(Integer productType, List<Integer> flowType,
			String orderId, boolean active,Long deptId) {
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActiveDept(productType, flowType, orderId,deptId);
		else
			list = reviewDao.findReviewDept(productType, flowType, orderId,deptId);
		return list;
	}

	/*
	 * 查看订单中某游客的变更审核列表 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id travlerId=游客Id active： 是否只查询 active=1的记录
	 */
	public List<Review> findReview(Integer productType, Integer flowType,
			String orderId, Long travelerId, boolean active,Long deptId) {
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActiveDept(productType, flowType, orderId,
					travelerId,deptId);
		else
			list = reviewDao.findReviewDept(productType, flowType, orderId,
					travelerId,deptId);
		return list;
	}

	/*
	 * 出纳确认方法 add by chy 2014年11月17日15:12:28
	 */
	public void cashConfirm(long reviewId, String key, String value) {
		// 1查询是否已存在记录
		List<ReviewDetail> reviewDetail = reviewDetailDao.findReviewDetail(reviewId);
		boolean exist = false;
		for (ReviewDetail temp : reviewDetail) {
			if (key.equals(temp.getMykey())) {
				exist = true;
			}
		}
		// 2存在则update不存在insert
		if (exist) {
			reviewDetailDao.updateReviewDetail(reviewId, key);
		} else {
			ReviewDetail rDetail = new ReviewDetail();
			rDetail.setReviewId(reviewId);
			rDetail.setMykey(key);
			rDetail.setMyvalue(value);
			reviewDetailDao.save(rDetail);
		}
	}

	/*
	 * 查看订单中某游客的变更审核列表 id=review 表主键 active： 是否只查询 active=1的记录
	 */
	public List<Review> findReview(long id, boolean active) {
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActive(id);
		else
			list = reviewDao.findReview(id);
		return list;
	}

	/* 取消某条审核,设置 status=4,active=0 */
	public int removeReview(Integer productType, Integer flowType,
			String orderId,Long deptId) {
		reviewDao.removeReviewDept(productType, flowType, orderId,deptId);
		return 1;
	}

	/* 取消某条审核,设置 status=4,active=0 */
	public int removeReview(Integer productType, Integer flowType,
			String orderId, Long travelerId,Long deptId) {
		reviewDao.removeReviewDept(productType, flowType, orderId, travelerId,deptId);
		return 1;
	}

	/* 取消某条审核,设置 active=0 */
	@Transactional
	public int removeReview(Long id) {
		Review review = reviewDao.findOne(id);
		//审批日志
		if (review != null) {
			String result = "已取消";
			String mark = "主动取消";
			Long updateBy = UserUtils.getUser().getId();
			Date updateDate = new Date();
			ReviewLog reviewLog = new ReviewLog(review.getId(), review.getNowLevel(), updateBy, updateDate, result, mark);
			reviewLogDao.save(reviewLog);
			
			//设置 status=4, active=0,表示取消审核			
			reviewDao.removeMyReview(id);
		}
		review.setStatus(4);
		review.setActive(0);
		updateCostRecordStatus(review, "已取消");
		
		return 1;
	}
	
	
	/**
	 * 如果取消或驳回申请为退款或返佣，则更新costrecord表对应记录的状态
	 * @param review 审核实体
	 * @param reviewStatus 成本状态
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void updateCostRecordStatus(Review review, String reviewStatus) {

		String orderId = review.getOrderId();
		Integer orderType = review.getProductType();
		
//		boolean yubao_locked = false; //预报单是否锁定标识
		boolean jiesuan_locked = false; //结算单是否锁定标识
		
		if (StringUtils.isNotBlank(orderId) && orderType != null) {
			//机票
			if (Context.ORDER_TYPE_JP == orderType) {
				AirticketOrder airOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
				ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(airOrder.getAirticketId());
				
				//对预报单状态进行判断
				if ("10".equals(activityAirTicket.getForcastStatus())) {
//					yubao_locked = true;
				}
				//对结算单状态进行判断
				if (1 == activityAirTicket.getLockStatus()) {
					jiesuan_locked = true;
				}
				
			} 
			//签证
			else if(Context.ORDER_TYPE_QZ == orderType) {
				VisaOrder visaOrder = visaOrderDao.findOne(Long.parseLong(orderId));
				VisaProducts visaProducts = visaProductDao.findOne(visaOrder.getVisaProductId());
				//对预报单状态进行判断
				if ("10".equals(visaProducts.getForcastStatus())) {
//					yubao_locked = true;
				}
				//对结算单状态进行判断
				if (1 == visaProducts.getLockStatus()) {
					jiesuan_locked = true;
				}
			} 
			//单团
			else {
				ProductOrderCommon order = orderDao.findOne(Long.parseLong(orderId));
				ActivityGroup group = activityGroupDao.findOne(order.getProductGroupId());
				//对预报单状态进行判断
				if ("10".equals(group.getForcastStatus())) {
//					yubao_locked = true;
				}
				//对结算单状态进行判断
				if (1 == group.getLockStatus()) {
					jiesuan_locked = true;
				}
			}
		}
		//如果取消或驳回申请为退款或返佣，则更新costrecord表对应记录的状态
		if (review.getFlowType() != null && 
				(review.getFlowType() == Context.REVIEW_FLOWTYPE_REFUND 
				|| review.getFlowType() == Context.REBATES_FLOW_TYPE
				|| review.getFlowType() == Context.REVIEW_FLOWTYPE_OPER_REFUND)) {
//			if (!yubao_locked) {
				List<CostRecord> costRecordList = costRecordDao.findCostRecordList(review.getId(), 0);
				if (CollectionUtils.isNotEmpty(costRecordList)) {
					for (CostRecord record : costRecordList) {
						if ("审核通过".equals(reviewStatus)) {
							record.setReview(2);
						}
						record.setReviewStatus(reviewStatus);
						costRecordDao.updateCostRecord(record.getId(), record.getReviewStatus(), record.getReview());
					}
				}
//			}
			
			if (!jiesuan_locked) {
				List<CostRecord> costList = costRecordDao.findCostRecordList(review.getId(), 1);
				if (CollectionUtils.isNotEmpty(costList)) {
					for (CostRecord record : costList) {
						if ("审核通过".equals(reviewStatus)) {
							record.setReview(2);
//							record.setReviewStatus("待提交");
//							costRecordDao.updateCostRecord(record.getId(), record.getReviewStatus(), record.getReview());
//							continue;
						}
						record.setReviewStatus(reviewStatus);
						costRecordDao.updateCostRecord(record.getId(), record.getReviewStatus(), record.getReview());
					}
				}
			}
		}
	}

	/* 获得审核日志列表 */
	public List<ReviewLog> findReviewLog(Integer productType, Integer flowType,
			String orderId,Long deptId) {
		List<Review> listReview = new ArrayList<Review>();
		listReview = reviewDao.findReviewDept(productType, flowType, orderId,deptId);
		if (listReview.size() == 1) {
			long rid = listReview.get(0).getId();
			List<ReviewLog> listLog = new ArrayList<ReviewLog>();
			listLog = reviewLogDao.findReviewLog(rid);
			return listLog;
		} else {
			return null;
		}
	}

	/* 获得审核日志列表 */
	public List<ReviewLog> findReviewLog(long rid) {
		List<ReviewLog> list = new ArrayList<ReviewLog>();
		list = reviewLogDao.findReviewLog(rid);
		return list;
	}
	
	/* 获得审核日志列表 */
	public List<ReviewLog> findReviewLogByOderedId(long rid) {
		List<ReviewLog> list = new ArrayList<ReviewLog>();
		list = reviewLogDao.findReviewLogByOderedId(rid);
		return list;
	}

	/*
	 * 经理审核模块 id=记录主键 nowLevel=当前的审核级别 result=1,0 审核结果 denyReason 驳回原因
	 * 返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
	 * 返回 -1：不是待审核的记录，不需要审核
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public int UpdateReview(Long id, Integer nowLevel, Integer result,
			String denyReason) {
		long userId = UserUtils.getUser().getId();
		Review review= reviewDao.findOne(id);
		//状态不是审核中,直接返回
		if(review.getStatus()!=1){
			return -1;
		}
		Long reviewCompanyId= review.getReviewCompanyId();
		Integer myLevel = review.getNowLevel();
		if(myLevel != nowLevel){
			return 1;
		}
		
		Integer orderType = review.getProductType();
		List<Integer> reviewRoleLevel= new ArrayList<Integer>();
		Long deptId= review.getDeptId();
		if(deptId != null){
			List<Long> parentId= departmentDao.findParentId(deptId);
			List<Long> jobList=userJobDao.getUserJobDeptList(userId, orderType, deptId, parentId.get(0));
			if(jobList.size() > 0) {
				reviewRoleLevel = reviewRoleLevelDao.findReviewJobList(reviewCompanyId,nowLevel, jobList);
			}
		}
		int isLastLevel=0;
		int logLevel = nowLevel;
		String logResult = "";
		if ((int) result == 1) {
			logResult = "审核通过";
		} else {
			logResult = "驳回";
		}

		ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getReviewCompanyId());
		int topLevel = reviewCompany.getTopLevel();
		if(result == 1){
			if(nowLevel < topLevel && reviewRoleLevel.size()==0) {
				nowLevel++;
			}else{
			  /**
			   * 最后审核人审核驳回时处理情况修改
			   * update by ruyi.chen
			   * update date 2015-01-09
			   */
			  if(result !=0){
				  nowLevel = topLevel;
				  //logLevel = topLevel;
				  isLastLevel=1;
				  if (result == 1){
					  result = 2;
				  }
			  }
			
		  }
		}
		Date updateDate = new Date();
		String updateByName = UserUtils.getUser().getName();
		if (updateByName == null){
			updateByName = "";
		}
		updateByName = updateByName.trim();
		//当是退款 退票 改价 退签证押金时 调用实体保存方法 add by chy 2015年6月2日14:40:57 其它 的还是以前的流程
		if(review.getFlowType() == 1 || review.getFlowType() == 3 || review.getFlowType() == 7 || review.getFlowType() == 10 || review.getFlowType() == 16){
			Review review2 = reviewDao.findOne(id);
			review2.setUpdateBy(userId);
			review2.setNowLevel(nowLevel);
			review2.setDenyReason(denyReason);
			review2.setUpdateByName(updateByName);
			review2.setStatus(result);
			reviewDao.save(review2);
		} else {
			reviewDao.UpdateReview(id, nowLevel, result, userId, updateByName,
					updateDate, denyReason);
			review.setUpdateBy(userId);
			review.setUpdateByName(updateByName);
			review.setUpdateDate(updateDate);
			review.setDenyReason(denyReason);
		}
		ReviewLog reviewLog = new ReviewLog(id, logLevel, userId, updateDate,
				logResult, denyReason);
		review.setNowLevel(nowLevel);
		review.setStatus(result.intValue());
		String reviewStatus = "";
		if(result.intValue() == 2){//审核通过时
			reviewStatus = "审核通过";
		} else if(result.intValue() == 0){//驳回时
			reviewStatus = "已驳回";
		} else {//待审核时
			reviewStatus = getNextReviewPerson(review);
		}
		updateCostRecordStatus(review, reviewStatus);
		reviewLogDao.save(reviewLog);
		return isLastLevel;
	}
	/**
	 * 审核撤销操作，只有流程未结束，且符合审核撤销标准的流程才可以撤销
	 * add by ruyi.chen 
	 * add date 2015-07-10
	 * @param id
	 * @param nowLevel
	 * @param result
	 * @param denyReason
	 * @param sbf
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public int backOutReview(Long id, Integer nowLevel,
			String denyReason,StringBuffer sbf) {
		
		Review review= reviewDao.findOne(id);
		//状态不是审核中,直接返回
		if(review.getStatus()!= 1 ){
			sbf.append("流程已审核结束！不能撤销审核！");
			return 0;
		} 
//		Long reviewCompanyId= review.getReviewCompanyId();
		Integer myLevel = review.getNowLevel();
		if(myLevel != nowLevel+1){
			sbf.append("操作错误！您不能撤销审核！");
			return 0;
		}
		if(review.getUpdateBy().longValue() != UserUtils.getUser().getId().longValue()){
			sbf.append("操作错误！撤销审核只能本人进行！");
			return 0;
		}
		if(review.getTopLevel().intValue() == 1){
			sbf.append("操作错误！不能撤销审核！");
			return 0;
		}
		long userId = UserUtils.getUser().getId();
		Integer orderType=review.getProductType();
//		List<Integer> reviewRoleLevel= new ArrayList<Integer>();
		Long deptId= review.getDeptId();		
		if (deptId != null){
		  List<Long> parentId= departmentDao.findParentId(deptId);
		  List<Long> jobList=userJobDao.getUserJobDeptList(userId, orderType, deptId, parentId.get(0));
		  if(jobList.size() > 0) {
//		     reviewRoleLevel = reviewRoleLevelDao.findReviewJobList(reviewCompanyId,nowLevel, jobList);
		  }
		}
		int logLevel = nowLevel;
		String logResult = "撤消审核";
		Date updateDate = new Date();
		long updateBy = UserUtils.getUser().getId();
		String updateByName = UserUtils.getUser().getName();
		if (updateByName == null)
			updateByName = "";
		updateByName = updateByName.trim();
		nowLevel = review.getNowLevel().intValue() -1;
		review.setNowLevel(nowLevel);
		Long backUpdateBy = null;
		String backUpdateByName = null;
		//审核撤销恢复相关
		List<ReviewLog> reviewLogs = this.findReviewLog(review.getId());
		List<ReviewLog> logs = Lists.newArrayList();
		if(null != reviewLogs && 0 < reviewLogs.size()){
			for(ReviewLog log : reviewLogs){
				if(nowLevel.intValue() != log.getNowLevel().intValue()){
					logs.add(log);
				}
			}
		}
		if(null != logs && 0 < logs.size()){
			Collections.sort(logs,  new Comparator<ReviewLog>(){
				@Override
				public int compare(ReviewLog o1, ReviewLog o2) {
					if (o1.getId().longValue() > o2.getId().longValue()) {
						return -1;
					} else if (o1.getId().longValue() < o2.getId().longValue()) {
						return 1;
					} else {
						return 0;
					}
				}
			});
			
			ReviewLog log = logs.get(0);
			if(log.getResult().trim().equals("审核通过")){
				backUpdateBy = log.getCreateBy();
				backUpdateByName = UserUtils.getUserNameById(backUpdateBy);
			}
		}				
		

		//当是退款 退票 改价 退签证押金时 调用实体保存方法 add by chy 2015年6月2日14:40:57 其它 的还是以前的流程
		if(review.getFlowType() == 1 || review.getFlowType() == 3 || review.getFlowType() == 7 || review.getFlowType() == 10 || review.getFlowType() == 16){
			Review review2 = reviewDao.findOne(id);	
			review2.setUpdateBy(backUpdateBy);
			review2.setNowLevel(nowLevel);
			review2.setDenyReason(denyReason);
			review2.setUpdateByName(backUpdateByName);
			review2.setStatus(1);
			reviewDao.save(review2);
		} else {
			reviewDao.UpdateReview(id, nowLevel, 1, backUpdateBy, backUpdateByName,
					updateDate, denyReason);
			review.setUpdateBy(backUpdateBy);
			review.setUpdateByName(backUpdateByName);
			review.setUpdateDate(updateDate);
			review.setDenyReason(denyReason);
		}
		ReviewLog reviewLog = new ReviewLog(id, logLevel, updateBy, updateDate,
				logResult, denyReason);
		review.setNowLevel(nowLevel);
		review.setStatus(1);
		String reviewStatus = "";
		reviewStatus = getNextReviewPerson(review);
//		if(result.intValue() == 0 || result.intValue() == 2){
		updateCostRecordStatus(review, reviewStatus);
//		}
		reviewLogDao.save(reviewLog);
//		String flowName = reviewFlowDao.findOne(review.getFlowType()).getFlowName();
//		String userName = UserUtils.getUser(review.getCreateBy()).getName();//发起人名称
//		String productTypeName = DictUtils.getDictLabel(review.getProductType() + "", "order_type", "");//产品类型
//		String[] emails;
//		if(result.intValue() == 0){
//			String createByemail = UserUtils.getUser(review.getCreateBy()).getEmail();
//			emails = new String[1];
//			emails[0] = createByemail;
//			SendMailUtil.sendSimpleMail(emails, "有" + userName + "发起" + productTypeName + "的" + flowName + "驳回", "您好，" + flowName + "已被驳回,请您知晓");
//		} else if(result.intValue() == 1){//已通过，切有下一环节审核人
//			//发邮件 add by chy 2015年5月5日17:51:00
//			List<String> email = getNextReviewEmail(review.getId());
//			emails = new String[email.size()];
//			int i = 0;
//			for(String tmp : email){
//				emails[i] = tmp;
//				i++;
//			}
//			if(emails.length != 0){
//				SendMailUtil.sendSimpleMail(emails, "有" + flowName + "待审核", "您好，您有待处理的" + flowName + ",请及时处理");
//			}
//		}
		return 1;
	}
	
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void revokePayReview(Long id, Integer nowLevel) {
		CostRecord costRecord = costRecordDao.findOne(id);
		
		Integer orderType = costRecord.getOrderType();
		
		Date updateDate = new Date();
		String sql = "select crl.createBy from cost_record_log crl where crl.result = 1 and crl.costId = ? and crl.orderType = ? and crl.nowLevel = ? and crl.logType = ?"; 
		List<Map<String, Integer>> costRecordLogs = costRecordLogDao.findBySql(sql, Map.class, id, orderType, nowLevel - 2, 1);
		int size = costRecordLogs.size();
		Integer updateBy;
		if(size > 0) {
			updateBy = costRecordLogs.get(size - 1).get("createBy");
		}else{
			updateBy = costRecord.getCreateBy().getId().intValue();
		}
		
		costRecord.setPayUpdateBy(updateBy);
		costRecord.setPayUpdateDate(updateDate);
		nowLevel = costRecord.getPayNowLevel() - 1;
		costRecord.setPayNowLevel(nowLevel);
		costRecordDao.updateObj(costRecord);
		
		CostRecordLog crl = new CostRecordLog();
		crl.setRid(costRecord.getActivityId());
		crl.setCostId(id);
		crl.setCostName(costRecord.getName()+"_付款审核");
		crl.setNowLevel(nowLevel);
		crl.setResult(-1);//付款审核撤销
		crl.setOrderType(orderType); 	
		crl.setRemark("");
		crl.setCreateDate(new Date());
		crl.setCreateBy(UserUtils.getUser().getId());
		crl.setLogType(1);
		costRecordLogDao.save(crl);
	}
	
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void revokePayReviewForHotel(Long id, Integer nowLevel) {
		CostRecordHotel costRecordHotel = costRecordHotelDao.findOne(id);
		
		Integer orderType = costRecordHotel.getOrderType();
		
		Date updateDate = new Date();
		String sql = "select crl.createBy from cost_record_log crl where crl.result = 1 and crl.costId = ? and crl.orderType = ? and crl.nowLevel = ? and crl.logType = ?"; 
		List<Map<String, Integer>> costRecordLogs = costRecordLogDao.findBySql(sql, Map.class, id, orderType, nowLevel - 2, 1);
		int size = costRecordLogs.size();
		Integer updateBy;
		if(size > 0) {
			updateBy = costRecordLogs.get(size - 1).get("createBy");
		}else{
			updateBy = Integer.parseInt(costRecordHotel.getCreateBy().getId() + "");
		}
		
		costRecordHotel.setPayUpdateBy(updateBy);
		costRecordHotel.setPayUpdateDate(updateDate);
		nowLevel = costRecordHotel.getPayNowLevel() - 1;
		costRecordHotel.setPayNowLevel(nowLevel);
		costRecordHotelDao.updateObj(costRecordHotel);
		
		CostRecordLog crl = new CostRecordLog();
		crl.setActivityUuid(costRecordHotel.getActivityUuid());
		crl.setCostId(id);
		crl.setCostName(costRecordHotel.getName()+"_付款审核");
		crl.setNowLevel(nowLevel);
		crl.setResult(-1);//付款审核撤销
		crl.setOrderType(orderType); 	
		crl.setRemark("");
		crl.setCreateDate(new Date());
		crl.setCreateBy(UserUtils.getUser().getId());
		crl.setLogType(1);
		costRecordLogDao.save(crl);
	}
	
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void revokePayReviewForIsland(Long id, Integer nowLevel) {
		CostRecordIsland costRecordIsland = costRecordIslandDao.findOne(id);
		
		Integer orderType = costRecordIsland.getOrderType();
		
		Date updateDate = new Date();
		String sql = "select crl.createBy from cost_record_log crl where crl.result = 1 and crl.costId = ? and crl.orderType = ? and crl.nowLevel = ? and crl.logType = ?"; 
		List<Map<String, Integer>> costRecordLogs = costRecordLogDao.findBySql(sql, Map.class, id, orderType, nowLevel - 2, 1);
		int size = costRecordLogs.size();
		Integer updateBy;
		if(size > 0) {
			updateBy = costRecordLogs.get(size - 1).get("createBy");
		}else{
			updateBy = Integer.parseInt(costRecordIsland.getCreateBy().getId() + "");
		}
		
		costRecordIsland.setPayUpdateBy(updateBy);
		costRecordIsland.setPayUpdateDate(updateDate);
		nowLevel = costRecordIsland.getPayNowLevel() - 1;
		costRecordIsland.setPayNowLevel(nowLevel);
		costRecordIslandDao.updateObj(costRecordIsland);
		
		CostRecordLog crl = new CostRecordLog();
		crl.setActivityUuid(costRecordIsland.getActivityUuid());
		crl.setCostId(id);
		crl.setCostName(costRecordIsland.getName()+"_付款审核");
		crl.setNowLevel(nowLevel);
		crl.setResult(-1);//付款审核撤销
		crl.setOrderType(orderType); 	
		crl.setRemark("");
		crl.setCreateDate(new Date());
		crl.setCreateBy(UserUtils.getUser().getId());
		crl.setLogType(1);
		costRecordLogDao.save(crl);
	}
	
	/*
	 * add by ruyi.chen
	 * 用于特殊审核直接通过，执行相应其他操作
	 * 经理审核模块 id=记录主键 nowLevel=当前的审核级别 result=1,0 审核结果 denyReason 驳回原因
	 * 返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
	 * 返回 -1：不是待审核的记录，不需要审核
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public int UpdateReviewLast(Long id, Integer nowLevel, Integer result,
			String denyReason) {
		
		Review review= reviewDao.findOne(id);
		//状态不是审核中,直接返回
		if(review.getStatus()!=1) return -1;
		Long reviewCompanyId= review.getReviewCompanyId();
//		Integer myLevel = review.getNowLevel();
//		if(myLevel != nowLevel) return 1;
		
		long userId = UserUtils.getUser().getId();
		Integer orderType=review.getProductType();
		List<Integer> reviewRoleLevel= new ArrayList<Integer>();
		Long deptId= review.getDeptId();		
		if (deptId != null){
		  List<Long> parentId= departmentDao.findParentId(deptId);
		  List<Long> jobList=userJobDao.getUserJobDeptList(userId, orderType, deptId, parentId.get(0));
		  if(jobList.size() > 0) {
		     reviewRoleLevel = reviewRoleLevelDao.findReviewJobList(reviewCompanyId,nowLevel, jobList);
		  }
		}
		int isLastLevel=0;
		int logLevel = nowLevel;
		String logResult = "";
		if ((int) result == 1) {
			logResult = "审核通过";
		} else {
			logResult = "驳回";
		}
		/*
		List<Review> list = new ArrayList<Review>();
		list = reviewDao.findReviewActive(id);
		if (list.size() != 1)
			return 0; */
		ReviewCompany reviewCompany=reviewCompanyDao.findOne(review.getReviewCompanyId());
		int topLevel = reviewCompany.getTopLevel();
		if ((int) result == 1) {
		  if (nowLevel < topLevel && reviewRoleLevel.size()==0) {
			nowLevel++;
		  } else {
			  /**
			   * 最后审核人审核驳回时处理情况修改
			   * update by ruyi.chen
			   * update date 2015-01-09
			   */
			  if(result !=0){
				  nowLevel = topLevel;
				  //logLevel = topLevel;
				  isLastLevel=1;
				  if (result == 1)
						result = 2;
			  }
			
		  }
		}
		Date updateDate = new Date();
		long updateBy = UserUtils.getUser().getId();
		String updateByName = UserUtils.getUser().getName();
		if (updateByName == null)
			updateByName = "";
		updateByName = updateByName.trim();
		//当是退款 退票 改价 退签证押金时 调用实体保存方法 add by chy 2015年6月2日14:40:57 其它 的还是以前的流程
		if(review.getFlowType() == 1 || review.getFlowType() == 3 || review.getFlowType() == 7 || review.getFlowType() == 10 || review.getFlowType() == 16){
			Review review2 = reviewDao.findOne(id);	
			review2.setUpdateBy(updateBy);
			review2.setNowLevel(nowLevel);
			review2.setDenyReason(denyReason);
			review2.setUpdateByName(updateByName);
			review2.setStatus(result);
			reviewDao.save(review2);
		} else {
			reviewDao.UpdateReview(id, nowLevel, result, updateBy, updateByName,
					updateDate, denyReason);
		}
		ReviewLog reviewLog = new ReviewLog(id, logLevel, updateBy, updateDate,
				logResult, denyReason);
		review.setNowLevel(nowLevel);
		review.setStatus(result.intValue());
		String reviewStatus = "";
		if(result.intValue() == 2){//审核通过时
			reviewStatus = "审核通过";
		} else if(result.intValue() == 0){//驳回时
			reviewStatus = "已驳回";
		} else {//待审核时
			reviewStatus = getNextReviewPerson(review);
		}
//		if(result.intValue() == 0 || result.intValue() == 2){
		updateCostRecordStatus(review, reviewStatus);
//		}
		reviewLogDao.save(reviewLog);
		String flowName = reviewFlowDao.findOne(review.getFlowType()).getFlowName();
		String userName = UserUtils.getUser(review.getCreateBy()).getName();//发起人名称
		String productTypeName = DictUtils.getDictLabel(review.getProductType() + "", "order_type", "");//产品类型
		String[] emails;
		if(result.intValue() == 0){
			String createByemail = UserUtils.getUser(review.getCreateBy()).getEmail();
			emails = new String[1];
			emails[0] = createByemail;
			SendMailUtil.sendSimpleMail(emails, "有" + userName + "发起" + productTypeName + "的" + flowName + "驳回", "您好，" + flowName + "已被驳回,请您知晓");
		} else if(result.intValue() == 1){//已通过，切有下一环节审核人
			//发邮件 add by chy 2015年5月5日17:51:00
			List<String> email = getNextReviewEmail(review.getId());
			emails = new String[email.size()];
			int i = 0;
			for(String tmp : email){
				emails[i] = tmp;
				i++;
			}
			if(emails.length != 0){
				SendMailUtil.sendSimpleMail(emails, "有" + flowName + "待审核", "您好，您有待处理的" + flowName + ",请及时处理");
			}
		}
		return isLastLevel;
	}

	/**
	 * 在业务上没有后续操作：整个流程审批及后续操作完毕
	 * @param id
	 * @param status
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void reviewOperationDone(Long id, Integer status){
		reviewDao.reviewOperationDone(id, status);
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2014-12-24
	 * 根据审核流程实例主键获取审核流程实例信息
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public Review findReviewInfo(Long id){
		return reviewDao.findOne(id);
	}

	/**
	 * add by yue.wang
	 * add date 2014-12-30
	 * 查询提交转团转款review 
	 * @param request
	 * @param response
	 * @param user
	 * @param condition
	 * @return
	 */
	public Page<Map<String, Object>> getTransferMoneyAppList(
			HttpServletRequest request, HttpServletResponse response,
			User user, Map<String, String> condition) {
		String sqlString= "select id,review_company_id,topLevel,nowLevel,productType,orderId,flowType,companyId,travelerId,createReason,";
		sqlString = sqlString + "denyReason,createDate,updateDate,createBy,updateBy,updateByName,status,active,printTime,printFlag,deptId,payStatus";
		sqlString = sqlString + " from review where flowType = '"+Context.REVIEW_FLOWTYPE_TRANSFER_MONEY+"' and orderId = "+condition.get("orderId")	+ " order by id desc ";
		return reviewDao.findBySql(new Page<Map<String,Object>>(request, response),sqlString,Review.class);
	}

	/**
	 * add by yue.wang
	 * add date 2014-12-30
	 * 修改review实例
	 * @param review
	 * @return
	 */
	public void updateRivew(Review review) {
		// TODO Auto-generated method stub
		reviewDao.getSession().update(review);
		reviewDao.getSession().flush();
	}

	/**
	 * add by yue.wang
	 * 查看某条订单变更 审核列表，包括驳回的记录 productType=业务类型 (机票，签证...) flowType=流程类型
	 * (退款，改签...) Orderid=订单id active： 是否只查询 active=1的记录
	 */
	public List<Review> findReview(Integer productType, Integer flowType,String orderId,
			Integer status, boolean active,Long deptId) {
		// TODO Auto-generated method stub
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActiveStatus(productType, flowType, orderId,status,deptId);
		else
			list = reviewDao.findReviewDept(productType, flowType, orderId,status,deptId);
		return list;
	}
	

	public boolean verifyWorkFlowStatus(String orderId,Integer productType,Integer flowType,Long deptId){
		List<Review> flows = reviewDao.queryWorkFlowByStatus(Integer.valueOf(flowType), Integer.valueOf(productType),orderId,Integer.valueOf("1"));
		
		if(flows.size()> 0){
			return Boolean.TRUE; 
		}
		return Boolean.FALSE;
	}
	

	
	/***************以下函数为不支持  部门业务   的历史函数*************************/
	
	
  
	/*
	 * 获取类订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id active： 是否只查询 active=1的记录
	 */
	public List<Map<String, String>> findReviewListMap(Integer productType,
			Integer flowType, String orderId, boolean active) {

		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActive(productType, flowType, orderId);
		else
			list = reviewDao.findReview(productType, flowType, orderId);

		for (Review review : list) {
			Map<String, String> map = Maps.newHashMap();
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", orderId.toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			map.put("createBy", review.getCreateBy().toString());
			map.put("createByName", UserUtils.getUser(review.getCreateBy()).getName());
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = review.getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
//			map.put("createDate", review.getCreateDate().toString());
//			map.put("updateDate", review.getUpdateDate().toString());
			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			
			map.put("updateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(review.getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			listMap.add(map);
		}
		return listMap;
	}
	
	
	
	/**
	 * 获取类订单变更 的审核信息 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id active： 是否只查询 active=1的记录 
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @param active
	 * @param t
	 * @author HPT
	 * @return 重载方法，返回List<Map<String, Object>>类型的数据
	 */
	public List<Map<String, Object>> findReviewListMap(Integer productType,
			Integer flowType, String orderId, boolean active,String t) {

		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActive(productType, flowType, orderId);
		else
			list = reviewDao.findReview(productType, flowType, orderId);

		
		
		for (Review review : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			HotelTraveler traveler = hotelTravelerDao.getById(Integer.valueOf(review.getTravelerId().toString()));
			if (traveler != null) {				
				map.put("personType", traveler.getPersonType());			
			}
			map.put("id", review.getId().toString());
			map.put("cpid", review.getReviewCompanyId().toString());
			map.put("topLevel", review.getTopLevel().toString());
			map.put("productType", productType.toString());
			map.put("flowType", flowType.toString());
			map.put("orderId", orderId.toString());
			map.put("createReason", review.getCreateReason());
			map.put("denyReason", review.getDenyReason());
			map.put("createBy", review.getCreateBy().toString());
			Long updateBy = review.getUpdateBy();
			if (updateBy != null) {
				map.put("updateBy", updateBy.toString());
			} else {
				map.put("updateBy", "");
			}
			Long travelerId = review.getTravelerId();
			if (travelerId != null) {
				map.put("travelerId", travelerId.toString());
			} else {
				map.put("travelerId", "");
			}
//			map.put("createDate", review.getCreateDate());
//			map.put("updateDate", review.getUpdateDate());
			map.put("createDate", sdf.format(review.getCreateDate()));
			map.put("updateDate", sdf.format(review.getUpdateDate()));
			map.put("updateByName", review.getUpdateByName());
			map.put("status", review.getStatus().toString());
			map.put("active", review.getActive().toString());

			/* 获得 review_detail 表的 key,value */
			List<ReviewDetail> listDetail = new ArrayList<ReviewDetail>();
			listDetail = reviewDetailDao.findReviewDetail(review.getId());
			if (listDetail.size() > 0) {
				for (ReviewDetail detail : listDetail) {
					map.put(detail.getMykey(), detail.getMyvalue());
				}
			}
			listMap.add(map);
		}
		return listMap;
	}
	
	
	

	
	


	/*
	 * 查看订单变更 审核列表,active=1 不包括驳回的记录 productType=业务类型 (机票，签证...) flowType=流程类型
	 * (退款，改签...) active： 是否只查询 active=1的记录 companyId: 经销商Id
	 */
	public List<Review> findReviewCompany(Integer productType,
			Integer flowType, Long companyId, boolean active) {
		// companyId=UserUtils.getUser().getCompany().getId();
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewCompanyActive(productType, flowType,
					companyId);
		else
			reviewDao.findReviewCompany(productType, flowType, companyId);
		return list;
	}

	/*
	 * 查看某条订单变更 审核列表，包括驳回的记录 productType=业务类型 (机票，签证...) flowType=流程类型
	 * (退款，改签...) Orderid=订单id active： 是否只查询 active=1的记录
	 */
	public List<Review> findReview(Integer productType, Integer flowType,
			String orderId, boolean active) {
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActive(productType, flowType, orderId);
		else
			list = reviewDao.findReview(productType, flowType, orderId);
		return list;
	}

	/*
	 * 查看订单中某游客的变更审核列表 productType=业务类型 (机票，签证...) flowType=流程类型 (退款，改签...)
	 * Orderid=订单id travlerId=游客Id active： 是否只查询 active=1的记录
	 */
	public List<Review> findReview(Integer productType, Integer flowType,
			String orderId, Long travelerId, boolean active) {
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActive(productType, flowType, orderId,
					travelerId);
		else
			list = reviewDao.findReview(productType, flowType, orderId,
					travelerId);
		return list;
	}

	


	/* 获得审核日志列表 */
	public List<ReviewLog> findReviewLog(Integer productType, Integer flowType,
			String orderId) {
		List<Review> listReview = new ArrayList<Review>();
		listReview = reviewDao.findReview(productType, flowType, orderId);
		if (listReview.size() == 1) {
			long rid = listReview.get(0).getId();
			List<ReviewLog> listLog = new ArrayList<ReviewLog>();
			listLog = reviewLogDao.findReviewLog(rid);
			return listLog;
		} else {
			return null;
		}
	}

	
	public List<CostRecordLog> findCostRecordLog(Long id, Integer orderType) {
		List<CostRecord> costRecords = new ArrayList<CostRecord>();
		costRecords = costRecordDao.findCostRecordList(id);
		if (costRecords.size() == 1) {
			long rid = costRecords.get(0).getId();
			List<CostRecordLog> listLog = new ArrayList<CostRecordLog>();
			listLog = costRecordLogDao.findCostRecordLogList(rid, orderType);
			return listLog;
		} else {
			return null;
		}
	}

	

	/**
	 * add by yue.wang
	 * 查看某条订单变更 审核列表，包括驳回的记录 productType=业务类型 (机票，签证...) flowType=流程类型
	 * (退款，改签...) Orderid=订单id active： 是否只查询 active=1的记录
	 */
	public List<Review> findReview(Integer productType, Integer flowType,String orderId,
			Integer status, boolean active) {
		// TODO Auto-generated method stub
		List<Review> list = new ArrayList<Review>();
		if (active == true)
			list = reviewDao.findReviewActive(productType, flowType, orderId,status);
		else
			list = reviewDao.findReview(productType, flowType, orderId,status);
		return list;
	}
	

	public boolean verifyWorkFlowStatus(String orderId,Integer productType,Integer flowType){
		List<Review> flows = reviewDao.queryWorkFlowByStatus(Integer.valueOf(flowType), Integer.valueOf(productType),orderId,Integer.valueOf("1"));
		
		if(flows.size()> 0){
			return Boolean.TRUE; 
		}
		return Boolean.FALSE;
	}


	/**
	 * add by ruyi.chen 
	 * add date 2015-03-16
	 * 此处判断订单流程申请互斥情况
	 * @param orderId
	 * @param flowType
	 * @return  true 可以正常申请    false 流程互斥
	 */
	public boolean getOrderApplyReviewInfo(String orderId,String flowType,String orderType,StringBuffer sf){
		
		String sqlString = "SELECT r.flowType, count(r.id)as number from review r where  r.active =1 and r.status =1  and r.orderId =? and r.productType=?  GROUP BY r.flowType ";
		List <Map<Object ,Object>> tMap = reviewDao.findBySql(sqlString, Map.class,orderId,orderType);
		if(null == tMap || 0 == tMap.size()){
			return Boolean.TRUE;
		}else{
			int number = getReviewMutexCount(tMap,flowType,sf);
			if(number > 0){
				return Boolean.FALSE;
			}else{
				return Boolean.TRUE;
			}
			
		}
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-03-17
	 * 获取与当下流程互斥的流程个数
	 * @param tMap
	 * @param flowType
	 * @return  流程互斥个数
	 */
	public int getReviewMutexCount(List <Map<Object ,Object>> tMap,String flowType,StringBuffer sf){
		//装配当前进行中各类别审核流程个数
		Map<String,String> rMap = Maps.newHashMap();
		for(Map<Object ,Object>map : tMap){
			rMap.put(map.get("flowType").toString(), map.get("number").toString());
		}
		//流程互斥计数
		int num = 0;
		//根据申请流程类别获取流程互斥情况
		String mutexStr = Context.getREVIEW_MUTEX().get(flowType);
		if(null != mutexStr && 0 < mutexStr.length()){
			String [] mutexReviews = mutexStr.split(",");
			for(String mut : mutexReviews){
				String number = rMap.get(mut);
				if(null != number){
					num = num +Integer.parseInt(number);
					sf.append(Context.getREVIEW_FLOW().get(mut)+"流程未结束！");
				}
			}
		}
		return num;
		
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-03-26
	 * describe 获取优先级别高的审核流程互斥情况(优先级别高的流程互斥条件为当前订单所有的审核流程)
	 * @param orderId
	 * @param orderType
	 * @param flowType
	 * @return result :0  无互斥(可以正常申请) 1 有相同流程审核中  2 有其他互斥流程审核中      message:描述信息
	 */
	public Map<String,Object>getApplyReviewMutexInfo(Long orderId,Integer orderType,String flowType){
		Map<String,Object> resultMap = Maps.newHashMap();
		
		resultMap.put(Context.RESULT, "0");
		resultMap.put(Context.MESSAGE, "流程可正常申请！");
		return resultMap;
//		StringBuffer sf = new StringBuffer();
//		String sqlString = "SELECT r.flowType, count(r.id)as number from review r where  r.active =1 and r.status =1  and r.orderId =? and r.productType=?  GROUP BY r.flowType ";
//		List <Map<Object ,Object>> tMap = reviewDao.findBySql(sqlString, Map.class,orderId,orderType);
//		//装配当前进行中各类别审核流程个数
//		Map<String,String> rMap = Maps.newHashMap();
//		for(Map<Object ,Object>map : tMap){
//			rMap.put(map.get("flowType").toString(), map.get("number").toString());
//		}
//		String flowSelf =rMap.get(flowType);
//		if(null != flowSelf){
//			resultMap.put(Context.RESULT, "1");
//			resultMap.put(Context.MESSAGE, Context.getREVIEW_FLOW().get(flowType)+"流程未结束！");
//			return resultMap;
//		}
//		int num = 0;
//		//根据申请流程类别获取流程互斥情况
//		String mutexStr = Context.getREVIEW_MUTEX().get(flowType);
//		if(null != mutexStr && 0 < mutexStr.length()){
//			String [] mutexReviews = mutexStr.split(",");
//			for(String mut : mutexReviews){
//				String number = rMap.get(mut);
//				if(null != number){
//					num = num +Integer.parseInt(number);
//					sf.append(Context.getREVIEW_FLOW().get(mut)+"流程未结束！");
//				}
//			}
//		}		
//		if(num > 0){
//			resultMap.put(Context.RESULT, "2");
//			resultMap.put(Context.MESSAGE, sf.toString());
//			return resultMap;
//		}else{
//			resultMap.put(Context.RESULT, "0");
//			resultMap.put(Context.MESSAGE, "流程可正常申请！");
//			return resultMap;
//		}
//		
	}

	public void removeOrderAllReview(Long orderId,Integer orderType){
		String sqlString = "SELECT r.flowType, r.id,r.productType,r.orderId,r.travelerId from review r where  r.active =1 and r.status =1  and r.orderId =? and r.productType=?  ";
		List <Map<Object ,Object>> tMap = reviewDao.findBySql(sqlString, Map.class,orderId,orderType);
		if(null != tMap && 0 < tMap.size()){
			
			for(Map<Object,Object> m : tMap){
				if(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.equals(m.get("flowType").toString())){
					travelerDao.updateTravelerDelFlag(Context.TRAVELER_DELFLAG_NORMAL, Long.parseLong(m.get("travelerId").toString()));
					
				}
				this.removeReview(Long.parseLong(m.get("id").toString()));
//				this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, "取消审核流程："+m.get("id").toString(), Context.log_state_del);
			}
		}
	}
	
	/**
	 * 获得将要审核的审核人的邮件信息
	 * @param reviewId
	 * @return
	 */
	public List<String>  getNextReviewEmail(Long reviewId){
		List<String> emails = new ArrayList<String>();
		Review review= reviewDao.findOne(reviewId);
		if(review.getStatus()==2) return emails;
		else if(review.getStatus()==0) return emails;
		else if(review.getStatus()==4) return emails;
		else if(review.getStatus()==3) return emails;
		ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getReviewCompanyId());
		List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewEmail(reviewCompany.getId(),review.getNowLevel(),review.getProductType(),review.getDeptId(),reviewCompany.getDeptId());
		if(jobList.size() >0){
		  for(Map<String, Object>  job : jobList){			
			  emails.add(job.get("email") == null ? "" : job.get("email").toString());
		  }		 
		 return emails;
		}
		return emails;
	}
	
	/**
	 * 获得成本审核审核 的审核人的邮件信息
	 * @param reviewId
	 * @return
	 */
	public List<String> getNextPayReviewEmail(Long reviewId){
		List<String> emails = new ArrayList<String>();				
		CostRecord review= costRecordDao.findOne(reviewId);
		if(review==null || review.getPayReview()==null) return emails;
		if(review.getPayReview()!=1) return  emails;
				
		Long activityId= review.getActivityId();
		Integer orderType= review.getOrderType();
		Long dept;
		if (orderType<6 || orderType==10){
			long srcActivityId=(long)activityGroupDao.findOne(activityId).getSrcActivityId();
			dept=travelActivityDao.findOne(srcActivityId).getDeptId();
		}else if(orderType==6){
			dept=visaProductDao.findOne(activityId).getDeptId();
		}else {
			dept=activityAirTicketDao.findOne(activityId).getDeptId();
		}
		if(dept==null){
			dept=(long)-1;
		}
		ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getPayReviewCompanyId());
		if(reviewCompany!=null){
			List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewEmail(reviewCompany.getId(),review.getPayNowLevel(),review.getOrderType(),dept,reviewCompany.getDeptId());
			if(jobList.size() >0){
					
				for(Map<String, Object>  job : jobList){			
					emails.add(job.get("email") == null ? "" : job.get("email").toString());
				}		 	 
				return emails;
			} else{
				return  emails;
			}
		}else{
			return emails;
		}
	} 
	
	//获得将要审核的审核人信息
	public String getNextReviewJob(Long reviewId){
		Review review = reviewDao.findOne(reviewId);	
		if(review.getStatus()==2) return "<font color=\"green\">审批通过</font>";
		else if(review.getStatus()==0) return "<font color=\"red\">审批失败(驳回)</font>";
		else if(review.getStatus()==4) return "取消申请";
		else if(review.getStatus()==3) return "操作完成";
		
		ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getReviewCompanyId());
		List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewJob(
				reviewCompany.getId(), review.getNowLevel(), review.getProductType(), 
				review.getDeptId(),reviewCompany.getDeptId());
		if (jobList.size() > 0) {
			String tmp="待审核人:";
			for (Map<String, Object> job : jobList) {			
				tmp += job.get("name") + " ";
			}		 
			return tmp;
		} else {
			return "没分配审核人";
		}	 
		
	} 
	
	//获得将要审核的审核人信息
	public String  getNextReviewPerson(Review review){
//		Review review= reviewDao.findOne(reviewId);	
		if(review.getStatus()==2) return  "审批通过";
		else if(review.getStatus()==0) return "已驳回";
		else if(review.getStatus()==4) return "已取消";
		else if(review.getStatus()==3) return "操作完成";
		
		ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getReviewCompanyId());
		List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewJob(reviewCompany.getId(),review.getNowLevel(),review.getProductType(),review.getDeptId(),reviewCompany.getDeptId());
		if(jobList.size() >0){
			String tmp="待审核人:";
		  for(Map<String, Object>  job : jobList){			
			 tmp+=job.get("name")+" ";
		  }		 
		 return tmp;
		}
		else{
			return "没分配审核人";
		}	 
		
	} 
	
	//获得成本审核 将要审核的审核人信息
		public String  getNextCostReviewJob(Long reviewId){
			CostRecord review= costRecordDao.findOne(reviewId);	
			if(review.getReview()==null) return "";
			if(review.getReview()==2) return "<font color=\"green\">审批通过</font>";
			else if(review.getReview()==0) return "<font color=\"red\">审核失败(驳回)</font>";
			else if(review.getReview()==4||review.getReview()==5) return "待提交审核";
			else if(review.getReview()==3) return "操作完成";
			Long activityId= review.getActivityId();
			Integer orderType= review.getOrderType();
			Long dept;
			if (orderType<6 || orderType==10){
				long srcActivityId=(long)activityGroupDao.findOne(activityId).getSrcActivityId();
				dept=travelActivityDao.findOne(srcActivityId).getDeptId();
			}else if(orderType==6){
				dept=visaProductDao.findOne(activityId).getDeptId();
			}else {
				dept=activityAirTicketDao.findOne(activityId).getDeptId();
			}
			if(dept==null){
				dept=(long)-1;
			}
			ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getReviewCompanyId());
			List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewJob(reviewCompany.getId(),review.getNowLevel(),review.getOrderType(),dept,reviewCompany.getDeptId());
			if(jobList.size() >0){
				String tmp="待审核人:";
			  for(Map<String, Object>  job : jobList){			
				 tmp+=job.get("name")+" ";
			  }		 
			 return tmp;
			}
			else{
				return "没分配审核人";
			}	
		} 
		
		//获得成本审核 酒店 将要审核的审核人信息
		public String  getNextCostHotelReviewJob(Long reviewId){
					CostRecordHotel review= costRecordHotelDao.findOne(reviewId);	
					if(review.getReview()==null) return "";
					if(review.getReview()==2) return "<font color=\"green\">审批通过</font>";
					else if(review.getReview()==0) return "<font color=\"red\">审核失败(驳回)</font>";
					else if(review.getReview()==4||review.getReview()==5) return "待提交审核";
					else if(review.getReview()==3) return "操作完成";
					
					ActivityHotelGroup activityHotelGroup= activityHotelGroupDao.getByUuid(review.getActivityUuid());
					long dept;
					if (activityHotelGroup!=null) {
						Integer deptId=activityHotelDao.getByUuid(activityHotelGroup.getActivityHotelUuid()).getDeptId();
						if(deptId !=null) {
							dept=deptId.longValue();						
						}
						else return "没有审核人";
					} else{
						return "没有审核人";
					}	
					
					ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getReviewCompanyId());
					List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewJob(reviewCompany.getId(),review.getNowLevel(),review.getOrderType(),dept,reviewCompany.getDeptId());
					if(jobList.size() >0){
						String tmp="待审核人:";
					  for(Map<String, Object>  job : jobList){			
						 tmp+=job.get("name")+" ";
					  }		 
					 return tmp;
					}
					else{
						return "没分配审核人";
					}	
				} 
		

		//获得成本审核 酒店 将要审核的审核人信息
		public String  getNextCostIslandReviewJob(Long reviewId){
					CostRecordIsland review= costRecordIslandDao.findOne(reviewId);	
					if(review.getReview()==null) return "";
					if(review.getReview()==2) return "<font color=\"green\">审批通过</font>";
					else if(review.getReview()==0) return "<font color=\"red\">审核失败(驳回)</font>";
					else if(review.getReview()==4||review.getReview()==5) return "待提交审核";
					else if(review.getReview()==3) return "操作完成";					
					ActivityIslandGroup activityIslandGroup= activityIslandGroupDao.getByUuid(review.getActivityUuid());
					long dept;
					if (activityIslandGroup!=null) {
						Integer deptId=activityIslandDao.getByUuid(activityIslandGroup.getActivityIslandUuid()).getDeptId();
						if(deptId !=null) {
							dept=deptId.longValue();						
						}
						else return "没有审核人";
					} else{
						return "没有审核人";
					}						
				    ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getReviewCompanyId());
					List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewJob(reviewCompany.getId(),review.getNowLevel(),review.getOrderType(),dept,reviewCompany.getDeptId());
					if(jobList.size() >0){
						String tmp="待审核人:";
					  for(Map<String, Object>  job : jobList){			
						 tmp+=job.get("name")+" ";
					  }		 
					 return tmp;
					}
					else{
						return "没分配审核人";
					}	
				} 
		
		//获得付款审核 将要审核的审核人信息
		public String  getNextPayReviewJob(Long reviewId){
			CostRecord review= costRecordDao.findOne(reviewId);
			if(review==null || review.getPayReview()==null) return "";
			if(review.getPayReview()==2) return  "<font color=green>审批通过</font>";
			else if(review.getPayReview()==0) return "<font color=red>审核失败(驳回)</font>";
			else if(review.getPayReview()==4||review.getPayReview()==5) return "待提交审核";
			else if(review.getPayReview()==3) return "操作完成";
			Long activityId= review.getActivityId();
			Integer orderType= review.getOrderType();
			Long dept;
			if (orderType<6 || orderType==10){
				long srcActivityId=(long)activityGroupDao.findOne(activityId).getSrcActivityId();
				dept=travelActivityDao.findOne(srcActivityId).getDeptId();
			}else if(orderType==6){
				dept=visaProductDao.findOne(activityId).getDeptId();
			}else {
				dept=activityAirTicketDao.findOne(activityId).getDeptId();
			}
			if(dept==null){
				dept=(long)-1;
			}
			ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getPayReviewCompanyId());
			if(reviewCompany!=null){
			  List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewJob(reviewCompany.getId(),review.getPayNowLevel(),review.getOrderType(),dept,reviewCompany.getDeptId());
			  if(jobList.size() >0){
				String tmp="待审核人:";
			   for(Map<String, Object>  job : jobList){			
				 tmp+=job.get("name")+" ";
			   }		 
			   return tmp;
			   } else{
				return "没分配审核人";
			 }
			}else{
				return "没分配审核人";
			}
		} 
		
		//获得付款审核  酒店 海岛 将要审核的审核人信息
		public String  getNextPayHotelReviewJob(Long reviewId){
			CostRecordHotel review= costRecordHotelDao.findOne(reviewId);		
			if(review==null || review.getPayReview()==null) return "";
			if(review.getPayReview()==2) return  "<font color=green>审批通过</font>";
			else if(review.getPayReview()==0) return "<font color=red>审核失败(驳回)</font>";
			else if(review.getPayReview()==4||review.getPayReview()==5) return "待提交审核";
			else if(review.getPayReview()==3) return "操作完成";
			ActivityHotelGroup activityHotelGroup= activityHotelGroupDao.getByUuid(review.getActivityUuid());
			long dept;
			if (activityHotelGroup!=null) {
				Integer deptId=activityHotelDao.getByUuid(activityHotelGroup.getActivityHotelUuid()).getDeptId();
				if(deptId !=null) {
					dept=deptId.longValue();						
				}
				else return "没有审核人";
			} else{
				return "没有审核人";
			}				
			ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getPayReviewCompanyId());
			if(reviewCompany!=null){
			  List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewJob(reviewCompany.getId(),review.getPayNowLevel(),review.getOrderType(),dept,reviewCompany.getDeptId());
			  if(jobList.size() >0){
				String tmp="待审核人:";
			   for(Map<String, Object>  job : jobList){			
				 tmp+=job.get("name")+" ";
			   }		 
			   return tmp;
			   } else{
				return "没分配审核人";
			 }
			}else{
				return "没分配审核人";
			}
		} 
	
		//获得付款审核  酒店 海岛 将要审核的审核人信息
		public String  getNextPayIslandReviewJob(Long reviewId){
			CostRecordIsland review= costRecordIslandDao.findOne(reviewId);			
			if(review==null || review.getPayReview()==null) return "";
			if(review.getPayReview()==2) return  "<font color=green>审批通过</font>";
			else if(review.getPayReview()==0) return "<font color=red>审核失败(驳回)</font>";
			else if(review.getPayReview()==4||review.getPayReview()==5) return "待提交审核";
			else if(review.getPayReview()==3) return "操作完成";
			ActivityIslandGroup activityIslandGroup= activityIslandGroupDao.getByUuid(review.getActivityUuid());
			long dept;
			if (activityIslandGroup!=null) {
				Integer deptId=activityIslandDao.getByUuid(activityIslandGroup.getActivityIslandUuid()).getDeptId();
				if(deptId !=null) {
					dept=deptId.longValue();						
				}
				else return "没有审核人";
			} else{
				return "没有审核人";
			}				
			ReviewCompany reviewCompany = reviewCompanyDao.findOne(review.getPayReviewCompanyId());
			if(reviewCompany!=null){
			  List<Map<String, Object>> jobList = reviewSqlDao.getNextReviewJob(reviewCompany.getId(),review.getPayNowLevel(),review.getOrderType(),dept,reviewCompany.getDeptId());
			  if(jobList.size() >0){
				String tmp="待审核人:";
			   for(Map<String, Object>  job : jobList){			
				 tmp+=job.get("name")+" ";
			   }		 
			   return tmp;
			   } else{
				return "没分配审核人";
			 }
			}else{
				return "没分配审核人";
			}
		} 
	//获得付款审核的日志信息
	public String  getPayReviewPerson(Long costId,Integer jobType){
		CostRecord costRecord= costRecordDao.findOne(costId);
		Long reviewCompanyId=costRecord.getPayReviewCompanyId();
		if (reviewCompanyId==null) return "";
		List<Map<String, Object>> jobList = reviewSqlDao.getPayReviewPerson(costId,reviewCompanyId,jobType,costRecord.getOrderType());
		if(jobList.size() >0){		
		  return  ""+jobList.get(jobList.size()-1).get("name");		 
		}
		else{
		  return "";
		}	 		
	}
	
	//通过职务类型 获得成本审核预算单中具体的 审核人信息
	public String getCostReviewJob(Long costId, Integer jobType)
	{ 
		return "";
		
	}
	public List<ReviewDetail> queryReviewDetailList(String reviewId){
		
		return reviewDetailDao.findReviewDetail(Long.valueOf(reviewId));
	}
	
	/**
	 * 根据审核Id更新打印时间和打印状态
		* 
		* @param 
		* @return void
		* @author majiancheng
		* @Time 2015-5-7
	 */
	public void updateReviewPrintInfoById(Date printDate, Long reviewId) {
		reviewDao.updateReviewPrintInfoById(reviewId, printDate, 1, UserUtils.getUser().getId());
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 *  1:审核成功
	 *  0：不符合撤销条件
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public int CancelReview(Long id) {
		Review review= reviewDao.findOne(id);
		
		//1.只能对本人的审核进行撤销(暂时先不限制)
		if(review.getUpdateBy().longValue() != UserUtils.getUser().getId().longValue()){
			//return 0;
		}
		
		//2.恢复审核review 表的 updateby 和 updatebyname
		List<ReviewLog> reviewLogs = findReviewLogByOderedId(review.getId());
		Long updateby = null;
		String updatebyname = null;
		for (ReviewLog reviewLog : reviewLogs) {
			if ("审核通过".endsWith(reviewLog.getResult())&&((review.getNowLevel()-1)==reviewLog.getNowLevel())) {
				updateby=reviewLog.getCreateBy();
				updatebyname = UserUtils.getUserNameById(reviewLog.getCreateBy());
			}
		}
		
		//3.更新review ，  并创建驳回日志
		int nowLevel=review.getNowLevel();
		if(review.getStatus()==1 && nowLevel >1){
			review.setNowLevel(nowLevel-1);
			review.setUpdateBy(updateby);
			review.setUpdateByName(updatebyname);
			reviewDao.save(review);
			Date updateDate = new Date();
			long updateBy = UserUtils.getUser().getId();			

			ReviewLog reviewLog = new ReviewLog(id, nowLevel, updateBy, updateDate,
					"回退审核", "审核撤销到上一级(第"+(nowLevel-1)+"级)");
			reviewLogDao.save(reviewLog);
			
			return 1;
		} else{
			return 0;
		}
		
	}
	
	/**
	 * 新增成本付款总数
	 * @return
	 */
//	public Integer getOrderPayCount() {
//		Long companyId = UserUtils.getUser().getCompany().getId();
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT payment.id,payment.orderType FROM")
//			.append(" (SELECT	pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,")
//			.append(" cr.supplyName,cr.amount,cr.currencyId,pro.createBy,pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
//			.append(" FROM (	SELECT costrecord.activityId,costrecord.`name`,costrecord.review,costrecord.orderType,costrecord.supplyName,costrecord.id,costrecord.payStatus,")
//			.append(" costrecord.printFlag,costrecord.printTime,costrecord.supplyId,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
//			.append(" FROM cost_record costrecord	WHERE	costrecord.payReview = 2 AND reviewType <> 1 AND reviewType <> 2 AND costrecord.delFlag = 0	AND costrecord.budgetType = 1	AND costrecord.orderType = 6")
//			.append(" ) cr,	(SELECT	visaproducts.createBy,' ' groupid,visaproducts.id proid,visaproducts.productCode groupCode,visaproducts.createDate,visaproducts.updateDate,visaproducts.productName acitivityName")
//			.append(" FROM visa_products visaproducts	WHERE	visaproducts.delFlag = 0 AND visaproducts.proCompanyId = ").append(companyId)
//			.append(" ) pro	WHERE	cr.activityId = pro.proid")
//			.append(" UNION")
//			.append(" SELECT pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,cr.supplyName,cr.amount,cr.currencyId,pro.createBy,")
//			.append(" pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
//			.append(" FROM (SELECT	costrecord.activityId,costrecord.`name`,costrecord.review,costrecord.supplyId,costrecord.orderType,costrecord.supplyName,costrecord.id,")
//			.append(" costrecord.payStatus,costrecord.printFlag,costrecord.printTime,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
//			.append(" FROM cost_record costrecord	WHERE	costrecord.payReview = 2 AND reviewType <> 1 AND reviewType <> 2 AND costrecord.delFlag = 0	AND costrecord.budgetType = 1")
//			.append(" AND costrecord.orderType <> 6	AND costrecord.orderType <> 7) cr,")
//			.append(" (SELECT	activitygroup.createBy,activitygroup.id groupid,travelactivity.id proid,activitygroup.groupCode groupCode,activitygroup.createDate,activitygroup.updateDate,travelactivity.acitivityName acitivityName")
//			.append(" FROM activitygroup activitygroup,(SELECT t.acitivityName,t.id FROM travelactivity t	WHERE	t.proCompany = ").append(companyId).append(") travelactivity	WHERE	activitygroup.delFlag = 0	AND activitygroup.srcActivityId = travelactivity.id")
//			.append(" ) pro	WHERE	cr.activityId = pro.groupid")
//			.append(" UNION")
//			.append(" SELECT pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,")
//			.append(" cr.supplyName,cr.amount,cr.currencyId,pro.createBy,pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
//			.append(" FROM (SELECT	costrecord.activityId,costrecord.`name`,costrecord.review,costrecord.orderType,costrecord.supplyName,costrecord.id,")
//			.append(" costrecord.payStatus,costrecord.printFlag,costrecord.printTime,costrecord.supplyId,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
//			.append(" FROM cost_record costrecord	WHERE costrecord.payReview = 2 AND reviewType <> 1 AND reviewType <> 2 AND costrecord.delFlag = 0	AND costrecord.budgetType = 1	AND costrecord.orderType = 7")
//			.append(" ) cr,(SELECT activityairticket.createBy,' ' groupid,activityairticket.id proid,activityairticket.product_code groupCode,activityairticket.createDate,activityairticket.updateDate,' ' acitivityName")
//			.append(" FROM activity_airticket activityairticket	WHERE	activityairticket.delflag = 0	AND activityairticket.proCompany = ").append(companyId).append(") pro WHERE	cr.activityId = pro.proid")
//			.append(" UNION")
//			.append(" SELECT pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,cr.supplyName,")
//			.append(" cr.amount,cr.currencyId,pro.createBy,pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
//			.append(" FROM (SELECT costrecord.activity_uuid activityId,costrecord.`name`,costrecord.review,costrecord.orderType,costrecord.supplyName,costrecord.id,")
//			.append(" costrecord.payStatus,costrecord.printFlag,costrecord.printTime,costrecord.supplyId,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
//			.append(" FROM cost_record_hotel costrecord	WHERE	costrecord.payReview = 2 AND costrecord.reviewType = 0 AND costrecord.delFlag = 0	AND costrecord.budgetType = 1	AND costrecord.orderType = 11")
//			.append(" ) cr,(SELECT activitygroup.createBy,activitygroup.uuid groupid,activity_hotel.uuid proid,activitygroup.groupCode,activitygroup.createDate,activitygroup.updateDate,activity_hotel.activityName acitivityName")
//			.append(" FROM activity_hotel_group activitygroup,(SELECT t.activityName,t.id,t.uuid FROM activity_hotel t WHERE t.wholesaler_id = ").append(companyId).append(") activity_hotel")
//			.append(" WHERE activitygroup.delFlag = 0	AND activitygroup.activity_hotel_uuid = activity_hotel.uuid) pro WHERE cr.activityId = pro.groupid")
//			.append(" UNION")
//			.append(" SELECT pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,cr.supplyName,cr.amount,")
//			.append(" cr.currencyId,pro.createBy,pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
//			.append(" FROM (SELECT costrecord.activity_uuid activityId,costrecord.`name`,costrecord.review,costrecord.orderType,costrecord.supplyName,costrecord.id,costrecord.payStatus,")
//			.append(" costrecord.printFlag,costrecord.printTime,costrecord.supplyId,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
//			.append(" FROM cost_record_island costrecord WHERE costrecord.payReview = 2	AND costrecord.reviewType = 0	AND costrecord.delFlag = 0 AND costrecord.budgetType = 1 AND costrecord.orderType = 12")
//			.append(" ) cr,(SELECT activitygroup.createBy,activitygroup.uuid groupid,activity_island.uuid proid,activitygroup.groupCode,activitygroup.createDate,activitygroup.updateDate,activity_island.activityName acitivityName")
//			.append(" FROM activity_island_group activitygroup,	(SELECT	t.activityName,t.id,t.uuid FROM	activity_island t	WHERE	t.wholesaler_id = ").append(companyId).append(") activity_island")
//			.append(" WHERE	activitygroup.delFlag = 0	AND activitygroup.activity_island_uuid = activity_island.uuid) pro WHERE cr.activityId = pro.groupid")
//			.append(" ) payment WHERE 1 = 1 ");
//		List<Map<String, Object>> list = reviewDao.findBySql(sql.toString(), Map.class);
//		int unpayedCount = 0;
//		for(int i = 0; i < list.size();i++) {
//			Map<String, Object> m = list.get(i);
//			String id = m.get("id").toString();
//			String orderType = m.get("orderType").toString();
//			String money = moneyAmountService.getRefundPayedMoney2(id,"1",orderType);
//			if("".equals(money)) {
//				unpayedCount++;
//			}
//		}
//		return unpayedCount;
//	}
	public Integer getOrderPayCount() {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) FROM")
			.append(" (SELECT	pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,")
			.append(" cr.supplyName,cr.amount,cr.currencyId,pro.createBy,pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
			.append(" FROM (	SELECT costrecord.activityId,costrecord.`name`,costrecord.review,costrecord.orderType,costrecord.supplyName,costrecord.id,costrecord.payStatus,")
			.append(" costrecord.printFlag,costrecord.printTime,costrecord.supplyId,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
			.append(" FROM cost_record costrecord	WHERE	costrecord.payReview = 2 AND reviewType <> 1 AND reviewType <> 2 AND costrecord.delFlag = 0	AND costrecord.budgetType = 1	AND costrecord.orderType = 6")
			.append(" ) cr,	(SELECT	visaproducts.createBy,' ' groupid,visaproducts.id proid,visaproducts.productCode groupCode,visaproducts.createDate,visaproducts.updateDate,visaproducts.productName acitivityName")
			.append(" FROM visa_products visaproducts	WHERE	visaproducts.delFlag = 0 AND visaproducts.proCompanyId = ").append(companyId)
			.append(" ) pro	WHERE	cr.activityId = pro.proid")
			.append(" UNION")
			.append(" SELECT pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,cr.supplyName,cr.amount,cr.currencyId,pro.createBy,")
			.append(" pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
			.append(" FROM (SELECT	costrecord.activityId,costrecord.`name`,costrecord.review,costrecord.supplyId,costrecord.orderType,costrecord.supplyName,costrecord.id,")
			.append(" costrecord.payStatus,costrecord.printFlag,costrecord.printTime,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
			.append(" FROM cost_record costrecord	WHERE	costrecord.payReview = 2 AND reviewType <> 1 AND reviewType <> 2 AND costrecord.delFlag = 0	AND costrecord.budgetType = 1")
			.append(" AND costrecord.orderType <> 6	AND costrecord.orderType <> 7) cr,")
			.append(" (SELECT	activitygroup.createBy,activitygroup.id groupid,travelactivity.id proid,activitygroup.groupCode groupCode,activitygroup.createDate,activitygroup.updateDate,travelactivity.acitivityName acitivityName")
			.append(" FROM activitygroup activitygroup,(SELECT t.acitivityName,t.id FROM travelactivity t	WHERE	t.proCompany = ").append(companyId).append(") travelactivity	WHERE	activitygroup.delFlag = 0	AND activitygroup.srcActivityId = travelactivity.id")
			.append(" ) pro	WHERE	cr.activityId = pro.groupid")
			.append(" UNION")
			.append(" SELECT pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,")
			.append(" cr.supplyName,cr.amount,cr.currencyId,pro.createBy,pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
			.append(" FROM (SELECT	costrecord.activityId,costrecord.`name`,costrecord.review,costrecord.orderType,costrecord.supplyName,costrecord.id,")
			.append(" costrecord.payStatus,costrecord.printFlag,costrecord.printTime,costrecord.supplyId,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
			.append(" FROM cost_record costrecord	WHERE costrecord.payReview = 2 AND reviewType <> 1 AND reviewType <> 2 AND costrecord.delFlag = 0	AND costrecord.budgetType = 1	AND costrecord.orderType = 7")
			.append(" ) cr,(SELECT activityairticket.createBy,' ' groupid,activityairticket.id proid,activityairticket.product_code groupCode,activityairticket.createDate,activityairticket.updateDate,' ' acitivityName")
			.append(" FROM activity_airticket activityairticket	WHERE	activityairticket.delflag = 0	AND activityairticket.proCompany = ").append(companyId).append(") pro WHERE	cr.activityId = pro.proid")
			.append(" UNION")
			.append(" SELECT pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,cr.supplyName,")
			.append(" cr.amount,cr.currencyId,pro.createBy,pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
			.append(" FROM (SELECT costrecord.activity_uuid activityId,costrecord.`name`,costrecord.review,costrecord.orderType,costrecord.supplyName,costrecord.id,")
			.append(" costrecord.payStatus,costrecord.printFlag,costrecord.printTime,costrecord.supplyId,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
			.append(" FROM cost_record_hotel costrecord	WHERE	costrecord.payReview = 2 AND costrecord.reviewType = 0 AND costrecord.delFlag = 0	AND costrecord.budgetType = 1	AND costrecord.orderType = 11")
			.append(" ) cr,(SELECT activitygroup.createBy,activitygroup.uuid groupid,activity_hotel.uuid proid,activitygroup.groupCode,activitygroup.createDate,activitygroup.updateDate,activity_hotel.activityName acitivityName")
			.append(" FROM activity_hotel_group activitygroup,(SELECT t.activityName,t.id,t.uuid FROM activity_hotel t WHERE t.wholesaler_id = ").append(companyId).append(") activity_hotel")
			.append(" WHERE activitygroup.delFlag = 0	AND activitygroup.activity_hotel_uuid = activity_hotel.uuid) pro WHERE cr.activityId = pro.groupid")
			.append(" UNION")
			.append(" SELECT pro.groupCode,cr.activityId,cr.`name`,cr.id,cr.payStatus payFlag,cr.review,cr.orderType activityTypeId,cr.supplyId,cr.supplyType,cr.supplyName,cr.amount,")
			.append(" cr.currencyId,pro.createBy,pro.createDate,pro.updateDate,pro.acitivityName,cr.printFlag,cr.printTime,cr.orderType,pro.groupid,pro.proid")
			.append(" FROM (SELECT costrecord.activity_uuid activityId,costrecord.`name`,costrecord.review,costrecord.orderType,costrecord.supplyName,costrecord.id,costrecord.payStatus,")
			.append(" costrecord.printFlag,costrecord.printTime,costrecord.supplyId,costrecord.supplyType,costrecord.price * costrecord.quantity amount,costrecord.currencyId")
			.append(" FROM cost_record_island costrecord WHERE costrecord.payReview = 2	AND costrecord.reviewType = 0	AND costrecord.delFlag = 0 AND costrecord.budgetType = 1 AND costrecord.orderType = 12")
			.append(" ) cr,(SELECT activitygroup.createBy,activitygroup.uuid groupid,activity_island.uuid proid,activitygroup.groupCode,activitygroup.createDate,activitygroup.updateDate,activity_island.activityName acitivityName")
			.append(" FROM activity_island_group activitygroup,	(SELECT	t.activityName,t.id,t.uuid FROM	activity_island t	WHERE	t.wholesaler_id = ").append(companyId).append(") activity_island")
			.append(" WHERE	activitygroup.delFlag = 0	AND activitygroup.activity_island_uuid = activity_island.uuid) pro WHERE cr.activityId = pro.groupid")
			.append(" ) payment WHERE 1 = 1 and payment.payFlag = 0");
		List<Object> list = reviewDao.findBySql(sql.toString());
		return Integer.parseInt(list.get(0).toString());
	}
	
	/**
	 * 新增退款付款总数
	 * @return
	 */
//	public Integer getRefundCount() {
//		Long companyId = UserUtils.getUser().getCompany().getId();
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT s.revid,s.prdtype FROM")
//			.append(" (SELECT	a.*, b.myValue refundname, c.currencyId mcurid,	c.amount mamount FROM	refundreview_view a, review_detail b,	money_amount c")
//			.append(" WHERE	a.revid = b.review_id	AND a.revid = c.reviewId AND b.myKey = 'refundName'	AND a.revstatus IN (2, 3)	AND a.flowtype = 1 AND a.revcom = ").append(companyId)
//			.append(" UNION")
//			.append(" SELECT a.*, b.myValue refundname,	c.currencyId mcurid, c.amount mamount	FROM refundreview_view a,	review_detail b, hotel_money_amount c")
//			.append(" WHERE	a.revid = b.review_id	AND a.revid = c.reviewId AND b.myKey = 'refundName'	AND a.revstatus IN (2, 3)	AND a.flowtype = 1 AND a.revcom = ").append(companyId)
//			.append(" UNION")
//			.append(" SELECT a.*, b.myValue refundname,	c.currencyId mcurid, c.amount mamount FROM refundreview_view a, review_detail b, island_money_amount c")
//			.append(" WHERE	a.revid = b.review_id	AND a.revid = c.reviewId AND b.myKey = 'refundName'	AND a.revstatus IN (2, 3)	AND a.flowtype = 1 AND a.revcom = ").append(companyId)
//			.append(" ) s WHERE	1 = 1");
//		List<Map<String, Object>> list = reviewDao.findBySql(sql.toString(), Map.class);
//		int unpayedCount = 0;
//		for(int i = 0; i < list.size();i++) {
//			Map<String, Object> m = list.get(i);
//			String revid = m.get("revid").toString();
//			String prdtype = m.get("prdtype").toString();
//			String money = moneyAmountService.getRefundPayedMoney2(revid,"2",prdtype);
//			if("".equals(money)) {
//				unpayedCount++;
//			}
//		}
//		return unpayedCount;
//	}
	public Integer getRefundCount() {
		Long companyId = UserUtils.getUser().getCompany().getId();
		String uuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) count FROM")
		//注释掉了 by chy 2015年12月9日16:01:53 目前的查询只支持退款的查询 现在需要查询退款和退签证押金的总数
//			.append(" (SELECT b.myValue refundname, c.currencyId mcurid,	c.amount mamount FROM	refundreview_view a, review_detail b,	money_amount c")
//			.append(" WHERE	a.revid = b.review_id	AND a.revid = c.reviewId AND b.myKey = 'refundName'	AND a.revstatus IN (2, 3)	AND a.flowtype in (1,7) AND a.payStatus = 0 AND a.revcom = ").append(companyId)
//			.append(" UNION")
//			.append(" SELECT b.myValue refundname,	c.currencyId mcurid, c.amount mamount	FROM refundreview_view a,	review_detail b, hotel_money_amount c")
//			.append(" WHERE	a.revid = b.review_id	AND a.revid = c.reviewId AND b.myKey = 'refundName'	AND a.revstatus IN (2, 3)	AND a.flowtype in (1,7) AND a.payStatus = 0 AND a.revcom = ").append(companyId)
//			.append(" UNION")
//			.append(" SELECT b.myValue refundname,	c.currencyId mcurid, c.amount mamount FROM refundreview_view a, review_detail b, island_money_amount c")
//			.append(" WHERE	a.revid = b.review_id	AND a.revid = c.reviewId AND b.myKey = 'refundName'	AND a.revstatus IN (2, 3)	AND a.flowtype in (1,7) AND a.payStatus = 0 AND a.revcom = ").append(companyId)
			/*新增查询新审核的退款审核记录条数 by chy 2015年12月9日15:16:35 start*/
				// 注释 by shijun.liu  2016.04.27
			/*.append(" (SELECT a.revid FROM refundreview_view a ")
			.append(" WHERE	a.revstatus IN (2, 3) AND a.flowtype in (1,7) AND a.payStatus = 0 AND a.revcom = ").append(companyId)
			.append(" UNION")
			.append(" SELECT r.id from review_new r where  r.pay_status = 0 and r.process_type in ('1','7') and r.status = ")
			.append(ReviewConstant.REVIEW_STATUS_PASSED).append(" and r.company_id = '").append(uuid).append("' ) s ");*/
			/*新增查询新审核的退款审核记录条数 by chy 2015年12月9日15:16:35 end*/
			.append(" (SELECT a.id FROM review a ")
			.append(" WHERE	a.status IN (2, 3) AND a.flowType in (1,7) AND a.payStatus = 0 AND a.companyId = ").append(companyId)
			.append(" UNION")
			.append(" SELECT r.id from review_new r where  r.pay_status = 0 and r.process_type in ('1','7') and r.status = ")
			.append(ReviewConstant.REVIEW_STATUS_PASSED).append(" and r.company_id = '").append(uuid).append("' ) s ");
		List<Object> list = reviewDao.findBySql(sql.toString());
		return Integer.parseInt(list.get(0).toString());
	}

	/**
	 * 新增返佣付款总数
	 * @return
	 */
//	public Integer getRebateCount() {
//		Long companyId = UserUtils.getUser().getCompany().getId();
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT rv.revid,rv.prdtype FROM")
//			.append(" (SELECT	rv.*, o.costname,	o.currencyId,	o.rebatesDiff	FROM refundreview_view rv, order_rebates o")
//			.append(" WHERE	rv.revid = o.rid AND rv.revstatus IN (2, 3)	AND rv.flowtype = ? AND rv.revcom = ? ")
//			.append(" UNION")
//			.append(" SELECT rv.*, '', m.currencyId, m.amount rebatesDiff	FROM refundreview_view rv, money_amount m")
//			.append(" WHERE	rv.revid = m.reviewId	AND rv.revstatus IN (2, 3) AND rv.flowtype = ? AND rv.revcom = ? ")
//			.append(" ) rv WHERE 1 = 1 ");
//		List<Map<String, Object>> list = reviewDao.findBySql(sql.toString(), Map.class, Context.REBATES_FLOW_TYPE, companyId, Context.REBATES_FLOW_TYPE, companyId);
//		int unpayedCount = 0;
//		for(int i = 0; i < list.size();i++) {
//			Map<String, Object> m = list.get(i);
//			String revid = m.get("revid").toString();
//			String prdtype = m.get("prdtype").toString();
//			String money = moneyAmountService.getRefundPayedMoney2(revid,"3",prdtype);
//			if("".equals(money)) {
//				unpayedCount++;
//			}
//		}
//		return unpayedCount;
//	}
	public Integer getRebateCount() {
		Long companyId = UserUtils.getUser().getCompany().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer sql = new StringBuffer();
		/**sql.append("SELECT count(*) count FROM")
			.append(" (SELECT rv.cpid id FROM refundreview_view rv, order_rebates o")
			.append(" WHERE rv.revid = o.rid AND rv.revstatus IN (2, 3) AND rv.payStatus = 0 ")
			.append(" AND rv.flowtype = ").append(Context.REBATES_FLOW_TYPE).append(" AND rv.revcom = ").append(companyId)
			.append(" UNION ALL ")
			.append(" SELECT rv.cpid id FROM refundreview_view rv, money_amount m")
			.append(" WHERE rv.revid = m.reviewId AND rv.revstatus IN (2, 3) AND rv.payStatus = 0 ")
			.append(" AND rv.flowtype = ").append(Context.REBATES_FLOW_TYPE).append(" AND rv.revcom = ").append(companyId)
			.append(" UNION ALL ")
			.append(" SELECT r.id from review_new r where  r.pay_status = 0 and r.process_type = '9' and r.status = ")
			.append(ReviewConstant.REVIEW_STATUS_PASSED).append(" and r.company_id = '").append(companyUuid).append("' ")
			.append(" ) rv ");*/
		sql.append("SELECT count(*) count FROM")
			.append(" (SELECT rv.id FROM review rv ")
			.append(" 	WHERE rv.status IN (2, 3) AND rv.payStatus = 0 ")
			.append(" 	AND rv.flowType = ").append(Context.REBATES_FLOW_TYPE).append(" AND rv.companyId = ").append(companyId)
			.append(" UNION ALL ")
			.append(" SELECT r.id from review_new r where  r.pay_status = 0 and r.process_type = '").append(Context.REBATES_FLOW_TYPE + "' ")
			.append(" AND r.status = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
			.append(" and r.company_id = '").append(companyUuid).append("' ) rv");
		List<Object> list = reviewDao.findBySql(sql.toString());
		return Integer.parseInt(list.get(0).toString());
	}
	
	/**
	 * 根据review ID, 获取对应机票的预计返佣总额（包括个人和团队，仅限申请了的）
	 * @param rid
	 * @return
	 */
	public String getPreRebatesApplied(Long rid) {
		String sql = "" + 
				
" select GROUP_CONCAT(yy.prebt SEPARATOR '+') as prebt " + 
" from (select CONCAT(xx.currency_mark, sum(xx.amount)) as prebt " +
" from (select c.currency_mark, ma.amount " +
" from order_rebates orb " +
" 	left join traveler t on orb.travelerId = t.id " +
" 	left join money_amount ma on t.rebates_moneySerialNum = ma.serialNum  " +
" 	left join currency c on ma.currencyId = c.currency_id " +
" where orb.rid = '" + rid + "' and orb.travelerId <> '' " +
" UNION " +
" select c.currency_mark, ma.amount " +
" from order_rebates orb " +
" 	left join airticket_order ao on orb.orderId = ao.id " +
" 	left join money_amount ma on ao.schedule_back_uuid = ma.serialNum  " +
" 	left join currency c on ma.currencyId = c.currency_id " +
" where orb.rid = '" + rid + "' and ISNULL(orb.travelerId)) as xx group by xx.currency_mark) as yy ";
		
		List<String> list = reviewDao.findBySql(sql.toString());
		if(CollectionUtils.isNotEmpty(list)){
			 String result = list.get(0);
			 return result;
		}
		return null;
	}
	
	
	public String getPreRebatesAppliedNew(String rid) {
		String sql = "" + 
		" select GROUP_CONCAT(yy.prebt SEPARATOR '+') as prebt " + 
		" from (select CONCAT(xx.currency_mark, sum(xx.amount)) as prebt " +
		" from (select c.currency_mark, ma.amount " +
		" from rebates orb " +
		" 	left join traveler t on orb.travelerId = t.id " +
		" 	left join money_amount ma on t.rebates_moneySerialNum = ma.serialNum  " +
		" 	left join currency c on ma.currencyId = c.currency_id " +
		" where orb.rid = '" + rid + "' and orb.travelerId <> '' " +
		" UNION " +
		" select c.currency_mark, ma.amount " +
		" from rebates orb " +
		" 	left join airticket_order ao on orb.orderId = ao.id " +
		" 	left join money_amount ma on ao.schedule_back_uuid = ma.serialNum  " +
		" 	left join currency c on ma.currencyId = c.currency_id " +
		" where orb.rid = '" + rid + "' and ISNULL(orb.travelerId)) as xx group by xx.currency_mark) as yy ";
		List<String> list = reviewDao.findBySql(sql.toString());
		if(CollectionUtils.isNotEmpty(list)){
			 String result = list.get(0);
			 return result;
		}
		return null;
	}
/**
 * 
 * @param revid
 * @return
 */
public String findRemark1(String revid)
{
	StringBuffer buffer = new StringBuffer();
	buffer.append("SELECT r.remarks FROM refund r WHERE r.record_id=");
	buffer.append(revid);
	List<Map<String,Object>> list = reviewDao.findBySql(buffer.toString(), Map.class);
	if(list.size()>0)
		return list.get(0).get("remarks").toString();
	else
		return "";
}


}
