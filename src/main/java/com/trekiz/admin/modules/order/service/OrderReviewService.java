package com.trekiz.admin.modules.order.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.GroupControlBoardService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.AirTicketOrderLendMoneyService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.service.IAirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.OrderExitGroupReviewVO;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;


/**
 * 
 * @author ruyi.chen 
 * update date 2014-11-19
 * describe   订单，补充  OrderCommonService
 *
 */
@Service
@Transactional(readOnly = true)
public class OrderReviewService extends BaseService{

	private static final Integer REVIEW_UNAUDITED=1;//审核中状态
	private static final Integer OPERATE_SUCCESS=3;//操作完成 
	@Autowired
    private ProductOrderCommonDao productorderDao;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	@Autowired
    private TravelerDao travelerDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private IAirticketPreOrderService airticketPreOrderService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private OrderContactsService orderContactsService;
	@Autowired
	private OrderCommonService orderCommonService;
	@Autowired
    private OrderServiceForSaveAndModify orderServiceForSaveAndModify;
	@Autowired
	private OrderStockService orderStockService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private SysIncreaseService sysIncreaseService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	@Autowired
    private OrderCommonService orderService;
	@Autowired
    TransferMoneyService transferMoneyService;
	@Autowired
	private OrderPayService orderPayService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
    private OrderStatisticsService orderStatusticsService;
	@Autowired
    private UserJobDao userJobDao;
	@Autowired
	private AirTicketOrderLendMoneyService airTicketOrderLendMoneyService;
	@Autowired
	private GroupControlBoardService groupControlBoardService;
	
	
	
	//add 
	@Autowired
	private IAirticketOrderDao  iAirticketOrderDao;
	/**
	 * 获取退团审核信息列表
	 * create by ruyi.chen
	 * create date 2014-12-09
	 * @return
	 */
	public Page<Map<Object, Object>> getExitGroupReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,List<UserJob> userJobs){
		return getReviewLists(page,vo,Context.REVIEW_FLOWTYPE_EXIT_GROUP,userJobs);
	}
	
	
	private Page<Map<Object, Object>> getReviewLists(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,Integer flowType,List<UserJob> userJobs){
		
		if(null != userJobs && 0 < userJobs.size()){
		//页面有部门职务查询时查询条件加载，没有部门职务查询时设默认值
		UserJob uj = new UserJob();
		if(vo.getRid() == 0){								
			vo.setRid(userJobs.get(0).getId());
			uj = userJobs.get(0);
		}else{
			boolean flag = false;
			for(UserJob r:userJobs){
				if(r.getId().longValue() == vo.getRid()){
					flag = true;
					uj = r;
					}
			}
			if(!flag){
				vo.setRid(userJobs.get(0).getId());
				uj = userJobs.get(0);
			}
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		// 获取review_company 实例
		List<Long> reviewCompanyIds = Lists.newArrayList();
		if(null != uj.getDeptLevel() && 1 == uj.getDeptLevel().intValue()){
			 reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getDeptId());
			 //获得当前职位负责的审核层级列表
			 List<Integer> jobs = reviewService.getJobLevel( uj.getDeptId(),uj.getJobId(),flowType);
			 vo.setUserLevel(jobs.get(0));
			 return getAllCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),1,uj.getOrderType());
		}else if(null != uj.getDeptLevel() && 2 == uj.getDeptLevel().intValue()){
			reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getParentDept());
			//获得当前职位负责的审核层级列表
			 List<Integer> jobs = reviewService.getJobLevel( uj.getParentDept(),uj.getJobId(),flowType);
			 vo.setUserLevel(jobs.get(0));
			 return getAllCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),2,uj.getOrderType());
		}else{
			return null;
		}					
					
	  }else {
		  return null;
	  }
	}

/**
 * 获取审核信息列表(查询审核信息基础方法，具体业务数据请关联业务表获取,暂适用于单团类基础审核查询)
 * create by ruyi.chen
 * create date 2015-03-18
 * @return
 */
private Page<Map<Object, Object>> getAllCommonReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,Long deptId,Long reviewCompanyId,Integer reviewType,Integer nowLevel,int deptLevel,Integer orderType){
	if (StringUtils.isBlank(page.getOrderBy())){
		page.setOrderBy(" aa.rid DESC ");
	}
	StringBuffer sbf = new StringBuffer();
	List<Object> ls=new ArrayList<Object>();
	ls.add(UserUtils.getUser().getCompany().getId());
//	ls.add(vo.getOrderType());
	ls.add(reviewType);
	
	Page<Map<Object, Object>> pageInfo;
	sbf
	.append("select aa.*,a.orderNum,a.intermodalType,a.orderTime,a.groupCode,a.meter,a.saler,a.picker,a.acitivityName,a.productId, ")
	.append("a.orderCompanyName")
	.append(" from ( SELECT AA.* from  (SELECT  A.*,t.name  from (SELECT r.id AS rid,r.orderId,r.productType as orderType,r.updateBy,")
	.append(" r.createReason,r.createDate,r.updateDate, r.updateByName as beforeReviewName,r.topLevel,r.nowLevel,(r.status) as reviewStatus,r.printTime,r.printFlag,");
//	.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=? and r.productType=? and r.flowType =? ")
	//审核部分根据部门不同的审核层级，添加不同的查询条件
	
	if(reviewType.intValue() == 9){
		if(2 == deptLevel){
			sbf.append(" r.travelerId from review r,sys_user sys,order_rebates re where r.createby=sys.id and r.id=re.rid and sys.companyId=?  and r.flowType =? ")
			.append(" and r.deptId=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
			if(StringUtils.isNotBlank(vo.getCreateTimeBegin())){//报批时间   r.createDate
				sbf.append(" and r.createDate>=? ");
				ls.add(vo.getCreateTimeBegin()+" 00:00:00");
			}
			if(StringUtils.isNotBlank(vo.getCreateTimeEnd())){
				sbf.append(" and r.createDate<=? ");
				ls.add(vo.getCreateTimeEnd()+" 23:59:59");
			}
			if(StringUtils.isNotBlank(vo.getRebatesDiffBegin())){//返佣差额(返佣金额)
				BigDecimal rebatesDiffBegin = new BigDecimal(vo.getRebatesDiffBegin());
				sbf.append(" and re.rebatesDiff >=? ");
				ls.add(rebatesDiffBegin);
			}
			if(StringUtils.isNotBlank(vo.getRebatesDiffEnd())){//返佣差额(返佣金额)
				BigDecimal rebatesDiffEnd = new BigDecimal(vo.getRebatesDiffEnd());
				sbf.append(" and re.rebatesDiff <=?");
				ls.add(rebatesDiffEnd);
			}
			
			if(StringUtils.isNotBlank(vo.getPrintFlag())){//打印状态
				if("1".equals(vo.getPrintFlag().trim())){
					sbf.append(" and r.printFlag = '"+vo.getPrintFlag()+"' ");
				}else{
					sbf.append(" and (r.printFlag = '0' or r.printFlag is null) ");
				}
				
			}
		}else if(1 == deptLevel){
			sbf.append(" r.travelerId from review r,sys_user sys,department dep,order_rebates re  where r.createby=sys.id  and r.deptId=dep.id and r.id=re.rid and sys.companyId=?  and r.flowType =? ")
			.append(" and dep.parent_id=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
			if(StringUtils.isNotBlank(vo.getCreateTimeBegin())){//报批时间   r.createDate
				sbf.append(" and r.createDate>=? ");
				ls.add(vo.getCreateTimeBegin()+" 00:00:00");
			}
			if(StringUtils.isNotBlank(vo.getCreateTimeEnd())){
				sbf.append(" and r.createDate<=? ");
				ls.add(vo.getCreateTimeEnd()+" 23:59:59");
			}
			if(StringUtils.isNotBlank(vo.getRebatesDiffBegin())){//返佣差额(返佣金额)
				BigDecimal rebatesDiffBegin = new BigDecimal(vo.getRebatesDiffBegin());
				sbf.append(" and re.rebatesDiff >=? ");
				ls.add(rebatesDiffBegin);
			}
			if(StringUtils.isNotBlank(vo.getRebatesDiffEnd())){//返佣差额(返佣金额)
				BigDecimal rebatesDiffEnd = new BigDecimal(vo.getRebatesDiffEnd());
				sbf.append(" and re.rebatesDiff <=?");
				ls.add(rebatesDiffEnd);
			}
			
			if(StringUtils.isNotBlank(vo.getPrintFlag())){//打印状态
				if("1".equals(vo.getPrintFlag().trim())){
					sbf.append(" and r.printFlag = '"+vo.getPrintFlag()+"' ");
				}else{
					sbf.append(" and (r.printFlag = '0' or r.printFlag is null) ");
				}
				
			}
		}else {
			return null;
		}
	}else{
		if(2 == deptLevel){
			sbf.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=?  and r.flowType =? ")
			.append(" and r.deptId=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
		}else if(1 == deptLevel){
			sbf.append(" r.travelerId from review r,sys_user sys,department dep  where r.createby=sys.id  and r.deptId=dep.id and sys.companyId=?  and r.flowType =? ")
			.append(" and dep.parent_id=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
		}else {
			return null;
		}
	}
	
	if(StringUtils.isNotBlank(vo.getCreateTimeBegin())){
		sbf.append(" and r.createDate>=? ");
		ls.add(vo.getCreateTimeBegin()+" 00:00:00");
	}
	if(StringUtils.isNotBlank(vo.getCreateTimeEnd())){
		sbf.append(" and r.createDate<=? ");
		ls.add(vo.getCreateTimeEnd()+" 23:59:59");
	}
	
	
	// 添加审核查询控制(全部、未审核、已驳回、已通过)
	sbf
	.append(getNewReviewCheckSql(vo.getReviewStatus(),nowLevel))
	.append(")A LEFT JOIN traveler t on A.travelerId = t.id");
	if(StringUtils.isNotBlank(vo.getTravelerName())){
		sbf.append(" and t.name like'%"+vo.getTravelerName()+"%') AA where AA.name is not null ");
	}else{
		sbf.append(" ) AA");
	}
	sbf.append(" )aa INNER JOIN (select s.* from(SELECT DISTINCT p.id AS orderId,p.orderNum,p.orderTime,")
	.append(" ag.groupCode,ta.acitivityName,ta.createBy AS meter,ta.id as productId,p.createBy AS picker, p.salerId AS saler,p.orderCompanyName,p.intermodal_type as intermodalType from productorder p ")
	.append("INNER JOIN activitygroup ag ON ag.id = p.productGroupId INNER JOIN travelactivity ta ON ta.id = p.productId ");
	// 添加页面条件查询控制(销售、计调、日期等)
	if(StringUtils.isNotBlank(vo.getOrderNum())){
		sbf.append(" and p.orderNum like'%"+vo.getOrderNum()+"%' ");
	}
	if(StringUtils.isNotBlank(vo.getChannel())){
		sbf.append(" and p.orderCompany=? ");
		ls.add(vo.getChannel());
	}
	if(StringUtils.isNotBlank(vo.getGroupCode())){
		sbf.append(" and ag.groupCode like'%"+vo.getGroupCode()+"%' ");
//		ls.add(vo.getChannel());
	}
	if(StringUtils.isNotBlank(vo.getMeter())){
		sbf.append(" and ta.createBy=? ");
		ls.add(vo.getMeter());
	}
//	if(StringUtils.isNotBlank(vo.getCreateBy())){
//		sbf.append(" and p.createBy=? ");
//		ls.add(vo.getCreateBy());
//	}
	if(StringUtils.isNotBlank(vo.getSaler())){
		sbf.append(" and p.salerId=? ");
		ls.add(vo.getSaler());
	}
	if(StringUtils.isNotBlank(vo.getPicker())) {
		sbf.append(" and p.createBy=?");
		ls.add(vo.getPicker());
	}
	if(StringUtils.isNotBlank(vo.getOrderType())){
		sbf.append(" and p.orderStatus=? ");
		ls.add(orderType);
	}
	if(StringUtils.isNotBlank(vo.getOrderTimeBegin())){
		sbf.append(" and p.orderTime>=? ");
		ls.add(vo.getOrderTimeBegin()+" 00:00:00");
	}
	if(StringUtils.isNotBlank(vo.getOrderTimeEnd())){
		sbf.append(" and p.orderTime<=? ");
		ls.add(vo.getOrderTimeEnd()+" 23:59:59");
	}
	
	sbf
	.append(")s where s.orderId is not null) a ON aa.orderId = a.orderId  ");
    pageInfo= productorderDao.findPageBySql(page,sbf.toString(), Map.class,ls.toArray());		
	List<Map<Object,Object>>list=pageInfo.getList();
	if(list.size()>0){
		for(Map<Object,Object> map:list){
			//String price=map.get("payPrice").toString();
//			String result = map.get("result").toString();
			String travelerId ="";
			if(null != map.get("travelerId")){
				travelerId = map.get("travelerId").toString();
			}
			
			
			if(StringUtils.isNotBlank(travelerId)){
				Traveler t = travelerDao.findById(Long.parseLong(travelerId));
				if(null != t && 0 < t.getId().intValue()){
					String payPrice = OrderCommonUtil.getMoneyAmountBySerialNum(t.getPayPriceSerialNum(),2);
					map.put("payPrice",payPrice);
					map.put("travelerName", t.getName());
				}
				
			}else{
				String orderId = map.get("orderId").toString();
				if(StringUtils.isNotBlank(orderId)){
					ProductOrderCommon order = productorderDao.findOne(Long.parseLong(orderId));
					if(null != order && 0 < order.getId().intValue()){
						String payPrice = OrderCommonUtil.getMoneyAmountBySerialNum(order.getTotalMoney(),2);
						map.put("payPrice",payPrice);
						map.put("travelerName", "团队");
					}
				}
				
			}
			
			Integer myStatus = 0;
			
			if(map.get("nowLevel").toString().equals(nowLevel.toString())){
				myStatus=1;
			}
			map.put("myStatus", myStatus);
			//判断前台返回针对于当前审核层级的审核状态
//			if(StringUtils.isNotBlank(result)){
//				switch(result){
//					case "未审核":
//						if(map.get("nowLevel").toString().equals(nowLevel.toString())){
//							myStatus=1;
//						}
//						break;
//					default:
//						break;
//				}
//				map.put("myStatus", myStatus);
//			}
		}
	}
	return pageInfo;
	
	
	
}
	/**add by ruyi.chen
	 * update date 2014-12-28
	 * 不同审核状态  审核查询控制sql拼接(修改本例适合单团类各种审核查询)
	 * 0：全部   1：待审核   2：未通过(驳回)  3:已通过
	 * 
	 */
	private String getReviewCheckSql(int reviewStatus,int userLevel){
		StringBuffer sbf=new StringBuffer();
		switch(reviewStatus){
			case 0:
				sbf.append("  and( (r.active = 1 and r.status =1  and r.nowLevel="+userLevel+") or(");
				sbf.append("r.status =0 and r.nowLevel="+userLevel+") or(r.nowLevel>"+userLevel+"  or(r.nowLevel<=r.topLevel and r.status in(2,3))))");
				break;
			case 1:
				sbf.append(" AND r.active = 1 and r.status =1  and r.nowLevel="+userLevel);
				break;
			case 2:
				sbf.append(" and r.status =0 and r.nowLevel="+userLevel);
				break;
			case 3:
				sbf.append(" and (r.nowLevel>"+userLevel+" or (r.nowLevel<=r.topLevel and r.status in(2,3)))");;
				break;
			default:
				break;
		}
		
		return sbf.toString();
	}
	
	/**
	 * create by ruyi.chen
	 * add date 2014-12-19
	 * 退团审核流程通过后，进行具体退团操作业务，更新余位，更新游客状态，更新审核流程状态
	 * @param orderId
	 * @param rid
	 * @param travelerId
	 * @param request
	 * @throws Exception
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> exitGroup(Long orderId, Long rid, Long travelerId, HttpServletRequest request) throws Exception {
		
		/** 操作合法性校验 */
		Map<String,Object> map = Maps.newHashMap();
		if (orderId == null) {
			map.put("flag", 0);
			map.put("message", "订单ID不能为空！");
			return map;
		}
		ProductOrderCommon productOrder = productorderDao.findOne(orderId);
		if(null == productOrder || productOrder.getId() <=0){
			map.put("flag", 0);
			map.put("message", "未找到该订单！");
			return map;
		}
		Traveler traveler = travelerDao.findOne(travelerId);
		if(null == traveler || traveler.getId() <=0){
			map.put("flag", 0);
			map.put("message", "未找到该游客！");
			return map;
		}
		ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
        if(null == activityGroup || activityGroup.getId()<=0){
        	map.put("flag", 0);
			map.put("message", "未找到该订单所属団期！");
			return map;
        }
        
        //第一步，修改订单信息
        
        /** 归还余位 */
        if (productOrder.getPayStatus() != null 
				&& !Context.ORDER_PAYSTATUS_YQX.equals(productOrder.getPayStatus().toString())
				&& !Context.ORDER_PAYSTATUS_DEL.equals(productOrder.getPayStatus().toString())) {
        	Map<String,String> rMap = orderStatusticsService.getPlaceHolderInfo(productOrder.getId(), productOrder.getOrderStatus().toString());
        	if (null != rMap && null != rMap.get(Context.RESULT)) {
            	//当等于1时，占位
            	String resultP = rMap.get(Context.RESULT);
            	//如果订单占位，要归还余位
            	if(Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)){
            		//当订单占位时归还余位
            		Map<String,String> pMap = orderStatusticsService.saveActivityGroupPlaceHolderChange(
            				productOrder.getId(), productOrder.getOrderStatus().toString(), 1, request, 5);
            		// 0524需求 团期余位变化,记录在团控板中
            		Review reviewInfo = reviewService.findReviewInfo(rid);
            		groupControlBoardService.insertGroupControlBoard(5, 1, "订单号"+productOrder.getOrderNum()+",1人退团", activityGroup.getId(), reviewInfo.getCreateBy());
            		// 0524需求 团期余位变化,记录在团控板中
            		//余位处理失败
            		if(null != pMap && Context.ORDER_PLACEHOLDER_ERROR.equals( pMap.get(Context.RESULT))) {
            			throw new Exception(pMap.get(Context.MESSAGE));
            		}
            		//余位处理成功
            		else if (null != pMap && Context.ORDER_PLACEHOLDER_YES.toString().equals( pMap.get(Context.RESULT))) {
            			
            		}else{
            			throw new Exception("归还余位失败！");
            		}
            	}else if(Context.ORDER_PLACEHOLDER_ERROR.toString().equals(resultP)){
            		throw new Exception(rMap.get(Context.MESSAGE));
            	}
            } else {
            	throw new Exception("操作失败！");
            }
        }
        
        
        handleOrderAndTravelerInfo(productOrder, traveler);
        
        if (productOrder.getPayStatus() != null 
				&& !Context.ORDER_PAYSTATUS_YQX.equals(productOrder.getPayStatus().toString())
				&& !Context.ORDER_PAYSTATUS_DEL.equals(productOrder.getPayStatus().toString())) {
        	//第三步，更新机票余位，调用机票余位处理接口
            if(StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, productOrder.getPayStatus().toString())	
               || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, productOrder.getPayStatus().toString())
               || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZW, productOrder.getPayStatus().toString())
            		){
            	airticketPreOrderService.returnFreePosionByProductOrderId(orderId, 1);
            }
        }
        
        //第四步，更新审核状态，修改为操作完成
        reviewService.reviewOperationDone(rid, OPERATE_SUCCESS);
        map.put("flag", 1);
		map.put("message", "操作完成！");
		return map;
	}
	
	/**
	 * 订单金额与人数、游客金额修改（退团和转团共用）
	 * @param productOrder
	 * @param traveler
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private void handleOrderAndTravelerInfo(ProductOrderCommon productOrder, Traveler traveler) {
		 /** 扣减订单人数 */
        int personType = traveler.getPersonType();
        switch(personType){
        	case Context.PERSON_TYPE_ADULT:
        		productOrder.setOrderPersonNumAdult(productOrder.getOrderPersonNumAdult() - 1);
        	break;
        	case Context.PERSON_TYPE_CHILD:
        		productOrder.setOrderPersonNumChild(productOrder.getOrderPersonNumChild() - 1);
        	break;
        	case Context.PERSON_TYPE_SPECIAL:
        		productOrder.setOrderPersonNumSpecial(productOrder.getOrderPersonNumSpecial() - 1);
        	break;
        	default:
        		productOrder.setOrderPersonNumAdult(productOrder.getOrderPersonNumAdult() - 1);
            break;
        }
        productOrder.setOrderPersonNum(productOrder.getOrderPersonNum() - 1);
        productorderDao.save(productOrder);
        
        //第二步，更新游客状态，改为已退团，并扣减相应金额
        reducePrice(productOrder, traveler);
	}
	
	/**
	 * 修改游客成本、结算价；修改订单成本、结算价
	 * 订单成本价、结算价保留扣减金额；游客成本、结算价改成扣减金额
	 * @param productOrder
	 * @param traveler
	 * @author yunpeng.zhang
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private void reducePrice(ProductOrderCommon productOrder, Traveler traveler) {
		// 获取游客扣减金额
		List<MoneyAmount> subtractMoneyList = moneyAmountService.findAmountBySerialNum(traveler.getSubtractMoneySerialNum());
		// 获取游客的结算价
		List<MoneyAmount> payPriceList = moneyAmountService.findAmountBySerialNum(traveler.getPayPriceSerialNum());
		
		
		//修改订单成本价、结算价：先减去游客结算价，如果有扣减金额，再加上扣减金额
		if (CollectionUtils.isNotEmpty(payPriceList)) {
			//如果游客有扣减金额，则需要在订单成本价、结算价加上此金额再减去游客成本、结算价
			if (CollectionUtils.isNotEmpty(subtractMoneyList)) {
				MoneyAmount subtractMoneyAmount = subtractMoneyList.get(0);
				for (MoneyAmount moneyAmonut : payPriceList) {
					if (moneyAmonut.getCurrencyId().equals(subtractMoneyAmount.getCurrencyId())) {
						moneyAmonut.setAmount(moneyAmonut.getAmount().subtract(subtractMoneyAmount.getAmount()));
						break;
					}
				}
				//订单应收价要减去的价格：游客结算价
				handlePrice(payPriceList, productOrder.getTotalMoney(), false, Context.MONEY_TYPE_YSH, productOrder.getOrderStatus());
				//订单成本价要减去的价格：游客结算价
				handlePrice(payPriceList, productOrder.getCostMoney(), false, Context.MONEY_TYPE_CBJ, productOrder.getOrderStatus());
			} else {
				//订单应收价要减去的价格：游客结算价
				handlePrice(payPriceList, productOrder.getTotalMoney(), false, Context.MONEY_TYPE_YSH, productOrder.getOrderStatus());
				//订单成本价要减去的价格：游客结算价
				handlePrice(payPriceList, productOrder.getCostMoney(), false, Context.MONEY_TYPE_CBJ, productOrder.getOrderStatus());
			}
		}
		
		//修改游客结算价、成本价：如果有游客扣减金额，则改为扣减金额，如果没有则都置为0
		if (CollectionUtils.isNotEmpty(subtractMoneyList)) {
			//修改游客结算价
			moneyAmountService.delMoneyAmountBySerialNum(traveler.getPayPriceSerialNum());
			String payMoneyserialNum = UuidUtils.generUuid();
			traveler.setPayPriceSerialNum(payMoneyserialNum);
			MoneyAmount payMoneyAmount = new MoneyAmount();
			BeanUtils.copyProperties(subtractMoneyList.get(0), payMoneyAmount);
			payMoneyAmount.setId(null);
			payMoneyAmount.setSerialNum(payMoneyserialNum);
			payMoneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);
			moneyAmountService.saveOrUpdateMoneyAmount(payMoneyAmount);
			
			
			//修改游客成本价
			moneyAmountService.delMoneyAmountBySerialNum(traveler.getCostPriceSerialNum());
			String costMoneyserialNum = UuidUtils.generUuid();
			traveler.setCostPriceSerialNum(costMoneyserialNum);
			MoneyAmount costMoneyAmount = new MoneyAmount();
			BeanUtils.copyProperties(subtractMoneyList.get(0), costMoneyAmount);
			costMoneyAmount.setId(null);
			costMoneyAmount.setSerialNum(costMoneyserialNum);
			costMoneyAmount.setMoneyType(Context.MONEY_TYPE_CBJ);
			moneyAmountService.saveOrUpdateMoneyAmount(costMoneyAmount);
			
		}
		//保存游客
		traveler.setDelFlag(Context.TRAVELER_DELFLAG_EXITED);
		travelerDao.save(traveler);
	}
	
	/**
	 * 金额相加或相减并保存
	 * @param priceList 要进行计算的金额List
	 * @param serialNum 进行相加减的金额序列号
	 * @param isAdd 是否进行相加
	 * @param moneyType 金额种类
	 * @param orderType 订单类型
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private void handlePrice(List<MoneyAmount> priceList, String serialNum, boolean isAdd, Integer moneyType, Integer orderType) {
		for (MoneyAmount subtractMoneyAmount : priceList) {
			MoneyAmount moneyAmount = new MoneyAmount();
			BeanUtils.copyProperties(subtractMoneyAmount, moneyAmount);
			moneyAmount.setId(null);
			moneyAmount.setSerialNum(serialNum);
			moneyAmount.setMoneyType(moneyType);
			moneyAmount.setOrderType(orderType);
			moneyAmount.setBusindessType(1);
			if (!isAdd) {
				moneyAmount.setAmount(subtractMoneyAmount.getAmount().multiply(new BigDecimal(-1)));
			}
			moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount, serialNum, "add", moneyType);
		}
	}
	
	/**
	 * 获取转团审核信息列表
	 * create by ruyi.chen
	 * create date 2014-12-28
	 * @return
	 */
	public Page<Map<Object, Object>> getTransferGroupReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,List<UserJob> userJobs){
		// 根据需要添加转团审核列表需要的业务数据
		return getReviewLists(page,vo,Context.REVIEW_FLOWTYPE_TRANSFER_GROUP,userJobs);
	}
	/**
	 * 获取转团转款审核信息列表
	 * create by ruyi.chen
	 * create date 2014-12-28
	 * @return
	 */
	public Page<Map<Object, Object>> getTransferMoneyReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,List<UserJob> userJobs){
		//  根据需要获取转团转款的款项、金额、币种等信息
		Page <Map<Object, Object>>p = getReviewLists(page,vo,Context.REVIEW_FLOWTYPE_TRANSFER_MONEY,userJobs);
		List<Map<Object,Object>>ls = Lists.newArrayList();
		if( null != p){
			ls = p.getList();
		}
		if(null!=ls&&ls.size()>0){
			for(Map<Object,Object>map:ls){
				String rid=map.get("rid").toString();
				if(StringUtils.isNotBlank(rid)){
					Map<String,String>m=reviewService.findReview(Long.parseLong(rid));
					String money =m.get("refundMoney");
					if(StringUtils.isNotBlank(money)){
						String moneyStr=OrderCommonUtil.getMoneyAmountBySerialNum(money, 1);
						map.put("travelerMoney", moneyStr);
					}
				}
				//添加相关订单信息
				String orderId = map.get("orderId").toString();
				if(StringUtils.isNotBlank(rid)){
					ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
					map.put("totalMoney", OrderCommonUtil.getMoneyAmountBySerialNum(productOrder.getTotalMoney(),2));
					map.put("payedMoney", OrderCommonUtil.getMoneyAmountBySerialNum(productOrder.getPayedMoney(),2));
					map.put("accountedMoney", OrderCommonUtil.getMoneyAmountBySerialNum(productOrder.getAccountedMoney(),2));
				}
			}
		}
		return p;
	}
	/**
	 * 转团审核成功后调用接口
	 * @param oldOrderId  原订单ID
	 * @param travelerId  转团游客ID
	 * @param newGroupId  新团期ID
	 * @param rid  审核记录ID
	 * @param request
	 * @return
	 * 返回 Map<String,Object>, 键值对：res：0；message：“”
	 * res：0 操作转团成功，1余位不足，2生成新订单失败, 3 数据输入有误
	 * message：失败原因。
	 * @throws Exception 
	 */
	@Deprecated
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object>  changeGroupSuccess(Long oldOrderId, Long travelerId,Long newGroupId,Long rid,HttpServletRequest request) throws Exception{
		// 返回类
		Map<String,Object> backMap = new HashMap<String,Object>();
		// 审核记录
		Review review=reviewService.findReviewInfo(rid);
		if(null == review){
			backMap.put("res", 3);
			backMap.put("message", "根据审核记录ID:"+rid+", 找不到审核记录");
			return backMap;
		}
		// 原订单实体
		ProductOrderCommon oldOrder = productorderDao.findOne(oldOrderId);
		if(null == oldOrder){
			backMap.put("res", 3);
			backMap.put("message", "根据订单ID:"+oldOrderId+", 找不到原订单");
			return backMap;
		}
		// 原旅客实体
		Traveler traveler = travelerDao.findById(travelerId);
		if(null == traveler){
			backMap.put("res", 3);
			backMap.put("message", "根据游客ID:"+travelerId+", 找不到游客");
			return backMap;
		}
		// 原团期实体
		ActivityGroup oldGroup = activityGroupService.findById(oldOrder.getProductGroupId());
		if(null == oldGroup){
			backMap.put("res", 3);
			backMap.put("message", "根据订单ID:"+oldOrderId+", 找不到原团期");
			return backMap;
		}
		// 新团期实体
		ActivityGroup newGroup = activityGroupService.findById(newGroupId);
		if(null == newGroup){
			backMap.put("res", 3);
			backMap.put("message", "根据团期ID:"+newGroupId+", 找不到新团期");
			return backMap;
		}
		// 新订单实体
		ProductOrderCommon newOrder = null;
		// 新游客实体
		Traveler newTraveler = new Traveler();
		// 审核实体
		Map<String, Object> reviewMap = reviewService.findReviewObject(rid);
		if(reviewMap.isEmpty() || reviewMap.get("payType")==null){
			backMap.put("res", 3);
			backMap.put("message", "根据审核ID:"+rid+", 找不到审核实体");
			return backMap;
		}
		// 生成新订单
		backMap = this.getNewGroup(review,oldOrder, newGroup, traveler,reviewMap.get("payType").toString(),Double.valueOf(reviewMap.get("remainDays")!=null?reviewMap.get("remainDays").toString():"0"));
		if(null != backMap.get("res") && "2".equals(backMap.get("res").toString()) ){
			return backMap;
		}else{
			newOrder = (ProductOrderCommon)backMap.get("newOrder");
		}
		// 获取原订单联系人
		List<OrderContacts> list =  orderContactsService.findOrderContactsByOrderIdAndOrderType(oldOrder.getId(),oldOrder.getOrderStatus());
		
		// 保存新订单 
		ProductOrderCommon order = new ProductOrderCommon();
		try {
			Map<String,Object> backOrderMap = new HashMap<String,Object>();
			// 判断余位、保存订单，新团期余位增加
			backOrderMap = orderServiceForSaveAndModify.saveOrder(newOrder, list, request);
			if(null != backOrderMap.get("errorMsg")){
				// 余位不足
				backMap.put("res", 1);
				backMap.put("message", backOrderMap.get("errorMsg").toString());
				return backMap;
			}else{
				order = (ProductOrderCommon)backOrderMap.get("productOrder");
			}
			orderStockService.changeGroupFreeNum(newOrder, null, Context.StockOpType.TRANSFER_GROUP);
			
		} catch (OptimisticLockHandleException e) {
			e.printStackTrace();
			backMap.put("res", 2);
			backMap.put("message", e.getMessage());
			return backMap;
		} catch (PositionOutOfBoundException e) {
			e.printStackTrace();
			backMap.put("res", 2);
			backMap.put("message", e.getMessage());
			return backMap;
		} catch (Exception e) {
			e.printStackTrace();
			backMap.put("res", 2);
			backMap.put("message", e.getMessage());
			return backMap;
		}
		
		// 保存游客
		newTraveler = getNewTraveler( newGroup, traveler,  order);
		if(null == newTraveler){
			backMap.put("res", 3);
			backMap.put("message", "保存游客失败");
			return backMap;
		}
		/**
		 * 处理原游客类(将删除状态置为“已转团”)
		 */
		handleOrderAndTravelerInfo(oldOrder, traveler);
		traveler.setDelFlag(5);
		travelerDao.save(traveler);
		
		/**
		 * 处理原团期类，切位/占位 减一。
		 */
		//判断是否有余位
        Map<String,String>rMap = orderStatusticsService.getPlaceHolderInfo(oldOrder.getId(), oldOrder.getOrderStatus().toString());
        if(null != rMap && null != rMap.get(Context.RESULT)){
        	String resultP = rMap.get(Context.RESULT);
        	if(Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)){
        		//当订单占位时归还余位
        		Map<String,String> pMap = orderStatusticsService.saveActivityGroupPlaceHolderChange(oldOrder.getId(), oldOrder.getOrderStatus().toString(), 1, request, -1);
        		//余位处理失败
        		if(null != pMap && Context.ORDER_PLACEHOLDER_ERROR.toString().equals( pMap.get(Context.RESULT))){
        			throw new Exception(Context.MESSAGE);
        		}
        		//余位处理成功
        		else if (null != pMap && Context.ORDER_PLACEHOLDER_YES.toString().equals( pMap.get(Context.RESULT))){
        			
        		}else{
        			throw new Exception("归还余位失败！");
        		}
        	}else if(Context.ORDER_PLACEHOLDER_ERROR.toString().equals(resultP)){
        		throw new Exception(rMap.get(Context.MESSAGE));
        	}
        }else{
        	throw new Exception("取消订单失败！");
        }
		/////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * 为保证转团成功后必须占位，在转团生成订单后根据新订单状态，若为未支付定金或未支付全款状态，则模拟一次支付，确保余位扣减
		 * add by ruyi.chen
		 * add date 2015-02-05
		 */
        String newOrderPayStatus = order.getPayStatus().toString();
        if(StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_WZF, newOrderPayStatus)||
        	StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_DJWZF, newOrderPayStatus)){
        	//模拟支付
        	Integer payPriceType = 1;
        	if(StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_WZF, newOrderPayStatus)){
        		payPriceType = 1;
        	}
        	if(StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_DJWZF, newOrderPayStatus)){
        		payPriceType = 3;
        	}
        	List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
        	if(null != currencyList && 0<currencyList.size()){
        		Integer[] currencyIdPrice = new Integer[1];
        		currencyIdPrice[0] = currencyList.get(0).getId().intValue();
        		BigDecimal[]dqzfprice = new BigDecimal[1];
        		dqzfprice[0] = BigDecimal.ZERO;
        		try {
					 orderPayService.savePay("",order.getId(), order.getOrderNum(), 1, order.getOrderStatus(), 3, payPriceType,
							1, currencyIdPrice, dqzfprice, "", null, null, "", "", "", 
							"", "", null, null, "yes", null, request, new ModelMap());
   					 // 0524需求 团期余位变化,记录在团控板中
					 // 转入团
					 groupControlBoardService.insertGroupControlBoard(7, order.getOrderPersonNum(), "从"+oldGroup.getGroupCode() + "团转入" + order.getOrderPersonNum() + "人", newGroup.getId(), review.getCreateBy());
					 // 转出团
					 groupControlBoardService.insertGroupControlBoard(6, order.getOrderPersonNum(), "订单"+oldOrder.getOrderNum()+"从"+oldGroup.getGroupCode() + "团转入"+newGroup.getGroupCode()+"团,转团" + order.getOrderPersonNum() + "人", oldGroup.getId(), review.getCreateBy());
					 // 0524需求 团期余位变化,记录在团控板中
				} catch (Exception e) {
					
					e.printStackTrace();
					backMap.put("res", 2);
					backMap.put("message", "操作失败");
					return backMap;
				}
        		
        	}
        }
                                
       
		/////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * 修正审核记录状态为“已通过”
		 * 审核状态0: 已驳回 (审核失败);1: 待审核;2: 审核成功;3: 操作完成 (审核成功后，操作员完成退款退团等操作),4:取消申请
		 */
		review.setStatus(Context.REVIEW_STATUS_PASS);
		reviewService.updateRivew(review);
		
		backMap.put("res", 0);
		backMap.put("message", "转团成功");
		return backMap;
	}
	/**
	 * 创建新订单 
	 * @param oldOrder review 审核记录
	 * @param oldOrder  原订单
	 * @param newGroup		新团期
	 * @param traveler  转团旅客
	 * @param payType  付款方式
	 * @param remainDays  （占位）保留时间
	 * @return
	 */
	private Map<String,Object> getNewGroup(Review review,ProductOrderCommon oldOrder,ActivityGroup newGroup,Traveler traveler,String payType,Double remainDays){
		
		Map<String,Object> backMap = new HashMap<String,Object>();
		ProductOrderCommon newOrder = new ProductOrderCommon();
		// 获取新团期产品
		TravelActivity travelActivity = travelActivityService.findById(Long.valueOf(newGroup.getSrcActivityId()));
		// 新订单类填值
		if(oldOrder!=null && newGroup!=null && traveler!=null){
			
			orderCommonService.clearObject(oldOrder);
			newOrder.setId(null);
			newOrder.setProductId(Long.valueOf(newGroup.getSrcActivityId()));// 产品信息表ID
			newOrder.setProductGroupId(newGroup.getId());	// 团期id
			newOrder.setOrderTime(new Date());	// 订单预定时间
			newOrder.setOrderPersonNum(1); 	// 预定人数
			newOrder.setOrderCompany(oldOrder.getOrderCompany());	// 渠道ID（同原订单）
			newOrder.setOrderCompanyName(oldOrder.getOrderCompanyName()); // 渠道名称（同原订单）
			newOrder.setPlaceHolderType(0);	// 占位方式 1： 切位，0或空值： 占位。
			newOrder.setOrderSaler(oldOrder.getOrderSaler()); // 跟单销售（同原订单）
			newOrder.setPayMode(payType); 	// 付款方式
			newOrder.setRemainDays(remainDays); 	// 占位保留天数
			newOrder.setCreateBy(UserUtils.getUser(review.getCreateBy())); // 审核申请创建人
			newOrder.setSalerId(review.getCreateBy().intValue());
			newOrder.setSalerName(UserUtils.getUser(review.getCreateBy()).getName());
			newOrder.setOrderStatus(travelActivity.getActivityKind());  // 订单种类
			newOrder.setIsAfterSupplement(1);
			// 支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消
			// 支付方式：1定金占位，2预占位，3全款支付，4资料占位，5担保占位，6确认单占位
			if("1".equals(payType)){
				newOrder.setPayStatus(1);  // 未支付订金
			}else if("2".equals(payType)){
				newOrder.setPayStatus(3);  // 已占位
			}else if("3".equals(payType)){
				newOrder.setPayStatus(1);  // 未支付全款
			}else if("4".equals(payType)){
				newOrder.setPayStatus(3);  // 已占位
			}else if("5".equals(payType)){
				newOrder.setPayStatus(3);  // 已占位
			}else if("6".equals(payType)){
				newOrder.setPayStatus(3);  // 已占位
			}else if("7".equals(payType)){
				newOrder.setPayStatus(3);  // 已占位
			}else if ("8".equals(payType)) {
				newOrder.setPayStatus(8); //待财务确认
			}else {
				newOrder.setPayStatus(99);  //  已取消
			}
			
			String companyName = officeService.get(travelActivity.getProCompany()).getName();
			String orderNum = sysIncreaseService.updateSysIncrease(companyName.length() > 3 ? companyName.substring(0, 3) : companyName, travelActivity.getProCompany(),null, Context.ORDER_NUM_TYPE);
			newOrder.setOrderNum(orderNum);	// 订单号
			// 判断游客是1成人、2儿童、3特殊人群
			if(traveler.getPersonType()==1){
				newOrder.setOrderPersonNum(1);
				newOrder.setOrderPersonNumAdult(1);
				newOrder.setOrderPersonNumChild(0);
				newOrder.setOrderPersonNumSpecial(0);
			}else if(traveler.getPersonType()==2){
				newOrder.setOrderPersonNum(1);
				newOrder.setOrderPersonNumAdult(0);
				newOrder.setOrderPersonNumChild(1);
				newOrder.setOrderPersonNumSpecial(0);
			}else if(traveler.getPersonType()==3){
				newOrder.setOrderPersonNum(1);
				newOrder.setOrderPersonNumAdult(0);
				newOrder.setOrderPersonNumChild(0);
				newOrder.setOrderPersonNumSpecial(1);
			}
			backMap.put("newOrder", newOrder);
		}else{
			backMap.put("res", 2);
			backMap.put("message", "生成新订单失败；原订单、新团期、游客 中有缺失数据");
		}
		return backMap;
	}
	/**
	 * 创建新旅客
	 * @param newGroup 新团期
	 * @param oldTraveler 原游客数据
	 * @param newOrderId 新订单ID
	 * @return
	 */
	private Traveler  getNewTraveler(ActivityGroup newGroup,Traveler oldTraveler, ProductOrderCommon newOrder){
		// 创建新旅客
		Traveler newTraveler = new Traveler();
		
		BeanUtils.copyProperties(oldTraveler, newTraveler);
		
		newTraveler.setId(null);
		newTraveler.setVisa(null);	// 签证实体
		newTraveler.setDelFlag(0);	//  删除标记 0:正常 1：删除 2:退团审核中 3：已退团 4：转团审核中 5：已转团
		newTraveler.setOrderId(newOrder.getId()); // 新订单ID
		newTraveler.setPayPriceSerialNum(UUID.randomUUID().toString()); 	// 游客结算价流水号
		newTraveler.setIsAirticketFlag("0"); 	// 是否有机票标志 0 标示无机票 或已退票 1标示有机票
		newTraveler.setOriginalPayPriceSerialNum(UUID.randomUUID().toString()); // 游客原始应收价 一次生成 永不改变
		newTraveler.setCostPriceSerialNum(UUID.randomUUID().toString());
		newTraveler.setOrderType(newOrder.getOrderStatus());
		
		// 保存游客
		newTraveler = travelerDao.save(newTraveler);
		
		// 根据游客类型，计算游客原始应收价
		BigDecimal amount = new BigDecimal(0);
		String currency = "1";
		if (newTraveler.getPersonType().equals(1)) {
			amount = newGroup.getSettlementAdultPrice();
			currency = newGroup.getCurrencyType().split(",")[0];
		} else if(newTraveler.getPersonType().equals(2)) {
			amount = newGroup.getSettlementcChildPrice();
			currency = newGroup.getCurrencyType().split(",")[1];
		} else {
			amount = newGroup.getSettlementSpecialPrice();
			currency = newGroup.getCurrencyType().split(",")[2];
		}
		
		newTraveler.setSrcPrice(amount);
		
		// 添加游客原始应收价流水表记录
		this.MoneyAmountList(newGroup, newTraveler.getOriginalPayPriceSerialNum(), currency, amount, newTraveler.getId(),
				Context.MONEY_TYPE_21, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
		// 添加游客结算价流水表记录
		this.MoneyAmountList(newGroup, newTraveler.getPayPriceSerialNum(), currency, amount, newTraveler.getId(),
				Context.MONEY_TYPE_21, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
		// 添加游客成本价流水表记录
		this.MoneyAmountList(newGroup, newTraveler.getCostPriceSerialNum(), currency, amount, newTraveler.getId(),
				Context.MONEY_TYPE_21, newOrder.getOrderStatus(), Context.BUSINESS_TYPE_TRAVELER, UserUtils.getUser().getId());
		return newTraveler;
	}
	
	
	/**
	 * 生成流水表金额记录
	 * @param newGroup 新团期实体,
	 * @param serialNum UUID
	 * @param amount 金额币种id
	 * @param amount 金额
	 * @param uid 订单ID或游客ID
	 * @param moneyType 款项类型
	 * @param orderType 产品类型
	 * @param busindessType 业务类型
	 * @param createdBy 创建者
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private boolean MoneyAmountList(ActivityGroup newGroup, String serialNum, String currency, BigDecimal amount,
			Long uid, Integer moneyType, Integer orderType,
			Integer busindessType, Long createdBy) {
		List<MoneyAmount> moneyAmountList= Lists.newArrayList();
		if(null == newGroup || StringUtils.isBlank(newGroup.getCurrencyType())){
			return false;
		}
		MoneyAmount moneyAmount = null;
		moneyAmount = new MoneyAmount(serialNum, Integer.parseInt(currency), 
				amount, uid, moneyType, orderType, busindessType, UserUtils.getUser().getId());
		moneyAmountList.add(moneyAmount);
		return moneyAmountService.saveOrUpdateMoneyAmounts(serialNum, moneyAmountList);
	}
	
	
	/**
	 * 获取返佣审核信息列表
	 * add by zhangcl
	 * create date 2015年3月10日
	 * @return
	 */
	public Page<Map<Object, Object>> getRebatesReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,List<UserJob> userJobs){
		Page<Map<Object, Object>>  p= getReviewLists(page,vo,Context.REBATES_FLOW_TYPE,userJobs);
		return p;
	}
	/**
	 * 获取酒店返佣审核信息列表
	 * add by ruyi.chen
	 * create date 2015-06-28
	 * @return
	 */
	public Page<Map<Object, Object>> getHotelRebatesReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,List<UserJob> userJobs){
		Page<Map<Object, Object>>  p= getHotelReviewLists(page,vo,Context.REBATES_FLOW_TYPE,userJobs);
		return p;
	}
	/**
	 * 获取海岛游返佣审核信息列表
	 * add by ruyi.chen
	 * create date 2015-06-28
	 * @return
	 */
	public Page<Map<Object, Object>> getIslandRebatesReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,List<UserJob> userJobs){
		Page<Map<Object, Object>>  p= getIslandReviewLists(page,vo,Context.REBATES_FLOW_TYPE,userJobs);
		return p;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-01-21
	 * 审核退团操作
	 * @param rid
	 * @param roleId
	 * @param result
	 * @param denyReason
	 * @param userLevel
	 * @param request
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> reviewExitGroup(long rid,long roleId,Integer result,String denyReason,Integer userLevel,HttpServletRequest request) throws NumberFormatException, Exception{
		int backSign=0;
		Map<String,Object>map = new HashMap<String,Object>();
		Review r=reviewService.findReviewInfo(rid);
		if(null != r&&r.getId()>0 && REVIEW_UNAUDITED.intValue() == r.getStatus().intValue()){
			backSign = reviewService.UpdateReview(rid, userLevel, result, denyReason);
			Review review = reviewService.findReviewInfo(rid);
			if(1 != result){
				orderService.updateTravelerStatus(Context.TRAVELER_DELFLAG_NORMAL, r.getTravelerId());
			}
			
			if(1 == backSign){
				try{
						Map<String,Object> exitMap = exitGroup(Long.parseLong(review.getOrderId()), rid, review.getTravelerId(), request);
						if(!exitMap.get("flag").toString().equals("1")){
							throw new Exception("操作失败！");
							
						}
				}catch(Exception e){
					map.put("flag", "0");
					map.put("message", "操作失败!");
					return map;
				}
			}
			map.put("flag", "1");
			map.put("message", "操作成功！");
		}else{
			map.put("flag", "0");
			map.put("message", "操作失败!");
		}
		
		return map;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-01-21
	 * 审核转团操作
	 * @param rid
	 * @param roleId
	 * @param result
	 * @param denyReason
	 * @param userLevel
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> reviewChangeGroup(long rid,long roleId,Integer result,String denyReason,Integer userLevel,HttpServletRequest request) throws Exception{
		int backSign=0;
		Map<String,Object>resultMap = new HashMap<String,Object>();
		Review r= reviewService.findReviewInfo(rid);
		
		String errorStr = new String();
		if(null != r && r.getId() >0 && REVIEW_UNAUDITED.intValue() == r.getStatus().intValue()){
			backSign=reviewService.UpdateReview(rid, userLevel, result, denyReason);
			Map<String ,String>review=reviewService.findReview(rid);
			if(1 != result){
				orderService.updateTravelerStatus(Context.TRAVELER_DELFLAG_NORMAL, r.getTravelerId());
			}
			if(1 == backSign){
				
				String newGroupCode = review.get("newGroupCode");
				ActivityGroup newGroup =  activityGroupService.findByGroupCode(newGroupCode);
				try{
					if(newGroup==null || newGroup.getId()==null){
						errorStr="新团期不存在，请检查团期号 "+newGroupCode+" 是否有误。";
						throw new Exception(errorStr);
					}
					// 转团审核成功后调用
					Map<String,Object> map = changeGroupSuccess(Long.parseLong(review.get("oldOrderId")), Long.parseLong(review.get("travelerId")), newGroup.getId(),rid, request);
					if(map.get("res").toString().equals("0")){
						ProductOrderCommon order = (ProductOrderCommon)map.get("newOrder");
						if(null != order && 0 < order.getId()){
							List<Detail>ls = new ArrayList<Detail>();
							ls.add(new Detail("newOrderId",order.getId()+""));
							reviewService.addReviewDetail(rid,ls);
						}
						orderService.updateTravelerStatus(Context.TRAVELER_DELFLAG_TURNROUNDED, r.getTravelerId());
						reviewService.reviewOperationDone(rid, Context.REFUND_STATUS_DONE);
						resultMap.put("flag", 1);
						resultMap.put("message", map.get("message"));
						return resultMap;
					}else{	
						errorStr=map.get("message").toString();
						throw new Exception(errorStr);
					}				
				}catch(Exception e){
					e.printStackTrace();
					throw new Exception(errorStr);
				}
			}			
		}else{
			throw new Exception("审核失败!");
		}		
		resultMap.put("flag", 1);
		resultMap.put("message", "操作成功!");
		return resultMap;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-01-21
	 * 审核转团转款操作
	 * @param rid
	 * @param roleId
	 * @param result
	 * @param denyReason
	 * @param userLevel
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> reviewTransFerMoney(long rid,long roleId,Integer result,String denyReason,Integer userLevel) throws Exception{
		Map<String,Object>resultMap = new HashMap<String,Object>();
		int backSign=0;
		Review r=reviewService.findReviewInfo(rid);
		if(null != r && r.getId() >0 && REVIEW_UNAUDITED.intValue() == r.getStatus().intValue()){
			backSign = reviewService.UpdateReview(rid, userLevel, result, denyReason);
			Review review = reviewService.findReviewInfo(rid);
			if(1 == backSign){
				//操作失败回滚
				String rl = transferMoneyService.transferMoneyApplyDone(review.getId());
				if("success".equals(rl)){
//					reviewService.reviewOperationDone(rid, Context.REFUND_STATUS_DONE);
					resultMap.put("flag", 1);
					resultMap.put("message", "审核成功!");
					return resultMap;
				}else{
						throw new Exception(rl);
					
				}
					
			}
		}else{
			throw new Exception("操作失败!");
			
		}
		resultMap.put("flag", 1);
		resultMap.put("message", "审核成功!");
		return resultMap;
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-04-07
	 * describe 获取单团类互斥操作统一判断接口(包含订单互斥，游客互斥，团队互斥，团队类流程游客标识为 0)
	 * @param orderId
	 * @param orderType
	 * @param flowType
	 * @param travelerIds 当此处为null时视为判断订单操作 
	 * @return result:  
	 *         message:描述信息
	 *         mutex_code: 订单类：0游客、团队均无互斥 1游客无互斥,团队互斥 2团队无互斥，游客有互斥 3游客、团队均互斥或者不展示
	 *         mutex_result_list:订单互斥判断结果
	 */
	public Map<String,Object> getOrderReviewMutexInfo(Long orderId,String orderType,Integer flowType,List<Long>travelerIds){
		Map<String,Object> resultMap = Maps.newHashMap();
		String sql = "SELECT r.flowType, r.id,r.travelerId from review r where r.active =1 and r.status =1  and r.orderId =? and r.productType=? ";
		List <Map<String ,Object>> tMap = productorderDao.findBySql(sql, Map.class,orderId,orderType);
		//获取当前流程互斥情况
		String mutexStr = Context.getREVIEW_MUTEX().get(flowType.toString());
		//定义不受互斥影响的符合条件的游客ID集合
		List<Long> resultTravelerIds = Lists.newArrayList();
		//当为订单互斥判断的情况
		if(null == travelerIds || 0 == travelerIds.size()){
			
			List<Integer> delFlags = Lists.newArrayList();
			switch(flowType){
				case 1:
					delFlags.add(0);
					delFlags.add(2);
					delFlags.add(3);
					break;
				case 16:
					delFlags.add(0);
					delFlags.add(2);
					delFlags.add(3);
					break;
				case 8:
					delFlags.add(0);
					delFlags.add(4);
					break;
				case 9:
					//返佣  游客查询控制条件添加
					delFlags.add(0);
					break;
				case 10:
					//改价  游客查询控制条件添加
					delFlags.add(0);
					break;
				case 11:
					delFlags.add(0);
					break;
				case 12:
					delFlags.add(5);
					break;
					default:
					delFlags.add(0);
					break;
			}
			//判断团队操作(如团队退款、团队返佣、团队改价等)流程互斥情况   true 存在相同流程审核流程  false 不存在
			boolean flag=false;
			if(null != tMap && 0 < tMap.size()){
				for(Map<String,Object> m : tMap){
					Integer rFlowType = Integer.parseInt(m.get("flowType").toString());
					if(rFlowType.intValue() == flowType.intValue() && (null == m.get("travelerId") || "0".equals(m.get("travelerId").toString()))){
						flag = true;
					}
				}
			}
			List<Traveler> travelerList = travelerDao.findTravelerByOrderIdAndOrderType(orderId, Integer.parseInt(orderType), delFlags);
			//游客查询无符合结果
			if(null == travelerList || 0 == travelerList.size()){
				if(flag){
					resultMap.put(Context.MUTEX_CODE, 3);
					resultMap.put(Context.MESSAGE, "无符合条件游客流程申请，团队"+Context.getREVIEW_FLOW().get(flowType.toString())+"流程未结束！");
					
				}else{
					resultMap.put(Context.MUTEX_CODE, 2);
					resultMap.put(Context.MESSAGE, "无符合条件游客流程申请！");
				}
			}else{
				
				for(Traveler t : travelerList){
					boolean tFlag=true;
					if(null != mutexStr && 0 < mutexStr.length()){
						String [] mutexReviews = mutexStr.split(",");
						for(String mut : mutexReviews){
							
							if(null != tMap && 0 < tMap.size()){
								for(Map<String,Object> m : tMap){
									String rFlowType = mut.toString();
									if(null != m.get("travelerId")){
										if(rFlowType.equals(m.get("flowType").toString()) && t.getId().toString().equals(m.get("travelerId"))){
											tFlag = false;
										}
									}
									
								}
							}
							
						}
					}
					//只有当订单无互斥流程的时候游客才添加到可申请页面
					if(tFlag){
						resultTravelerIds.add(t.getId());
					}
				}
			//	
				if(flag && (null == resultTravelerIds || 0 == resultTravelerIds.size())){
					resultMap.put(Context.MUTEX_CODE, 3);
					resultMap.put(Context.MESSAGE, "无符合条件游客流程申请，团队"+Context.getREVIEW_FLOW().get(flowType.toString())+"流程未结束！");
					
				}else if( flag && resultTravelerIds.size() > 0){
					resultMap.put(Context.MUTEX_CODE, 1);
					resultMap.put(Context.MESSAGE, "可进行游客流程申请，团队"+Context.getREVIEW_FLOW().get(flowType.toString())+"流程未结束！");
					resultMap.put(Context.MUTEX_RESULT_lIST, resultTravelerIds);
				}else if(!flag && (null == resultTravelerIds || 0 == resultTravelerIds.size())){
					resultMap.put(Context.MUTEX_CODE, 2);
					resultMap.put(Context.MESSAGE, "可进行团队流程申请，无符合条件游客申请流程！");
				}else if(!flag && resultTravelerIds.size() > 0){
					resultMap.put(Context.MUTEX_CODE, 0);
					resultMap.put(Context.MESSAGE, "可进行游客流程申请、团队流程申请");
					resultMap.put(Context.MUTEX_RESULT_lIST, resultTravelerIds);
				}	
			}
		}else{
			//游客互斥操作判断情况
			Map<String,Object> returnMap = Maps.newHashMap();
			for(Long t : travelerIds){
				boolean tFlag=true;
				int flowSelf = 0;
				StringBuffer sf = new StringBuffer();
				if(null != mutexStr && 0 < mutexStr.length()){
					String [] mutexReviews = mutexStr.split(",");
					for(String mut : mutexReviews){
						
						if(null != tMap && 0 < tMap.size()){
							int sign = 0;
							for(Map<String,Object> m : tMap){
								if(sign == 0){
									String rFlowType = mut.toString();
									if(null != m.get("travelerId")){
										if(rFlowType.equals(m.get("flowType").toString()) && t.toString().equals(m.get("travelerId").toString())){
											tFlag = false;
											sign = 1;
											sf.append(Context.getREVIEW_FLOW().get(rFlowType)+"流程审核中");
											if(rFlowType.equals(flowType.toString())){
												flowSelf = 1;
											}
										}
									}
									
									//判断团队操作的情况
									if(0 == t.intValue()){
										if(rFlowType.equals(m.get("flowType").toString()) && ( null == m.get("travelerId")||t.toString().equals(m.get("travelerId").toString()))){
											tFlag = false;
											sign = 1;
											sf.append(Context.getREVIEW_FLOW().get(rFlowType)+"流程审核中");
											if(rFlowType.equals(flowType.toString())){
												flowSelf = 1;
											}
										}
										
									}
								}
								
							}
						}
						
					}
				}
				//只有当订单无互斥流程的时候游客才添加到可申请列表，针对每个游客进行互斥判断
				if(tFlag){
					// 是否互斥(0否，1互斥)/是否同一类型的流程互斥(0否，1是)/互斥信息描述       
					returnMap.put(t.toString(), "0/"+flowSelf+"/"+sf.toString());
				}else{
					returnMap.put(t.toString(), "1/"+flowSelf+"/"+sf.toString());
				}
			}
			resultMap.put(Context.MUTEX_RESULT_lIST, returnMap);
		}
		
		
		return resultMap;
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-04-09
	 * describe 取消订单所有的团队审核
	 * @param orderId
	 * @param orderType
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void removeOrderGroupReviewForExitGroup(Long orderId,Integer orderType){
		String sqlString = "SELECT r.flowType, r.id,r.productType,r.orderId,r.travelerId from review r where  r.active =1 and r.status =1  and r.orderId =? and r.productType=?  and  (travelerId is NULL or travelerId ='0')  ";
		List <Map<Object ,Object>> tMap = travelerDao.findBySql(sqlString, Map.class,orderId,orderType);
		if(null != tMap && 0 < tMap.size()){
			
			for(Map<Object,Object> m : tMap){
				
				reviewService.removeReview(Long.parseLong(m.get("id").toString()));
				this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, "取消审核流程：" + m.get("id").toString(),
						Context.log_state_del, m.get("productType") != null ? Integer.parseInt(m.get("productType").toString()) : null, null);
			}
		}
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-04-09
	 * describe 取消订单上某一游客的所有互斥流程
	 * @param orderId
	 * @param orderType
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void removeTravelerReviewForExitGroup(Long orderId,Integer orderType,Long travelerId){
		String sqlString = "SELECT r.flowType, r.id,r.productType,r.orderId,r.travelerId from review r where  r.active =1 and r.status =1  and r.orderId =? and r.productType=?  and travelerId=? and flowType in(1,16,9,10,11)  ";
		List <Map<Object ,Object>> tMap = travelerDao.findBySql(sqlString, Map.class,orderId,orderType,travelerId);
		if(null != tMap && 0 < tMap.size()){
			
			for(Map<Object,Object> m : tMap){
				if(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.equals(m.get("flowType").toString())){
					travelerDao.updateTravelerDelFlag(Context.TRAVELER_DELFLAG_NORMAL, Long.parseLong(m.get("travelerId").toString()));
					
				}
				reviewService.removeReview(Long.parseLong(m.get("id").toString()));
				this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, "取消审核流程：" + m.get("id").toString(),
						Context.log_state_del, m.get("productType") != null ? Integer.parseInt(m.get("productType").toString()) : null, null);
			}
		}
	}
	public Map<String,Object>checkExitGroupInfo(String orderType,Integer flowType,Long orderId,Long[]travelerId,String[]travelerName){
		Map<String, Object> result =new HashMap<String,Object>();
		Map<Long,String> travelerInfos = Maps.newHashMap();
		List<Long> travelerIds = Lists.newArrayList();
		for(int i = 0;i < travelerId.length;i++){
			travelerInfos.put(travelerId[i], travelerName[i]);
			travelerIds.add(travelerId[i]);
		}
		travelerIds.add((long)0);
		travelerInfos.put((long)0, "团队");
		Map<String, Object> rMap = this.getOrderReviewMutexInfo(orderId, orderType, flowType,travelerIds);
		
		@SuppressWarnings("unchecked")
		Map<String,Object> resultMap = (Map<String,Object>) rMap.get(Context.MUTEX_RESULT_lIST);
		boolean flag = false;
		boolean flowSelf = false;
		//普通互斥信息
		StringBuffer sf = new StringBuffer();
		//退团游客自己互斥信息
		StringBuffer sbf = new StringBuffer();
		for(Long tid : travelerIds){
			String[] tResult = resultMap.get(tid.toString()).toString().split("/");
			if("1".equals(tResult[0])){
				flag = true;
				sf.append(travelerInfos.get(tid)+" "+tResult[2]+" ");
			}
			if("1".equals(tResult[1])){
				flowSelf = true;
				sbf.append(travelerInfos.get(tid)+" 退团审核中！ ");
			}
		}
		//当有游客退团申请中的时候
		if(flowSelf){
			result.put("sign", 1);
			result.put(Context.MESSAGE, sbf);
			return result;
		}
		//当游客退团有互斥流程的时候
		if(flag){
			result.put("sign", 2);
			result.put(Context.MESSAGE, sf);
			return result;
		}
		//游客退团不存在互斥情况
		result.put("sign", 0);
		return result;
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2015-04-14
	 * describe 获取单团类互斥操作统一判断接口(包含订单互斥，游客互斥，团队互斥，团队类流程游客标识为 0),此处为统一判断普通流程游客互斥情况
	 * @param orderId
	 * @param orderType
	 * @param flowType
	 * @param travelerIds 当此处为null时视为判断订单操作 
	 * @return result:  
	 *         message:描述信息
	 *         mutex_code: 0 不互斥   1有互斥  
	 */
	public Map<String,Object> getTravelerReviewMutexInfo(Long orderId,String orderType,Integer flowType,Map<Long,String> travelerMap){
		Map<String,Object> rMap = Maps.newHashMap();
		List<Long> travelerIds = Lists.newArrayList();
		Iterator<Entry<Long, String>> it = travelerMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Long, String> entry = (Entry<Long, String>) it.next();
			travelerIds.add(entry.getKey());
		}
		Map<String,Object> mutexMap = this.getOrderReviewMutexInfo(orderId, orderType, flowType, travelerIds);
		if(null != travelerIds && 0 < travelerIds.size()){
			@SuppressWarnings("unchecked")
			Map<String,Object> resultMap = (Map<String,Object>) mutexMap.get(Context.MUTEX_RESULT_lIST);
			boolean flag = false;
			StringBuffer sf = new StringBuffer();
			for(Long tid : travelerIds){
				if("1".equals(resultMap.get(tid.toString()).toString().split("/")[0])){
					flag = true;
					sf.append(travelerMap.get(tid)+" "+resultMap.get(tid.toString()).toString().split("/")[2]+" ");
				}
			}
			if(flag){
				rMap.put(Context.MUTEX_CODE, 1);
				rMap.put(Context.MESSAGE, sf.toString());
			}else{
				rMap.put(Context.MUTEX_CODE, 0);
			}
						
		}else{
			rMap = mutexMap;
		}
		
		return rMap;
	}
	
	public int getReviewCountByType(Integer flowType,List<UserJob> userJobs,Integer reviewStatus){
		int count = 0;
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(null != userJobs && 0 < userJobs.size()){
			for(UserJob uj :userJobs){
				OrderExitGroupReviewVO vo = new OrderExitGroupReviewVO();
				vo.setReviewStatus(reviewStatus);
				// 获取review_company 实例
				List<Long> reviewCompanyIds = Lists.newArrayList();
				if(null != uj.getDeptLevel() && 1 == uj.getDeptLevel().intValue()){
					 reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getDeptId());
					 //获得当前职位负责的审核层级列表
					 List<Integer> jobs = reviewService.getJobLevel( uj.getDeptId(),uj.getJobId(),flowType);
					 vo.setUserLevel(jobs.get(0));
					 count = count + getAllCommonReviewCount(vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),1,uj.getOrderType());
				}else if(null != uj.getDeptLevel() && 2 == uj.getDeptLevel().intValue()){
					reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getParentDept());
					//获得当前职位负责的审核层级列表
					 List<Integer> jobs = reviewService.getJobLevel( uj.getParentDept(),uj.getJobId(),flowType);
					 vo.setUserLevel(jobs.get(0));
					 count = count + getAllCommonReviewCount(vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),2,uj.getOrderType());
				}
			}				
	  }else {
		  return 0;
	  }
	 return count ;
	}

	/**
	 * 获取审核信息列表(查询审核信息基础方法，具体业务数据请关联业务表获取,暂适用于单团类基础审核查询)
	 * create by ruyi.chen
	 * create date 2015-03-18
	 * @return
	 */
	private int getAllCommonReviewCount(OrderExitGroupReviewVO vo,Long deptId,Long reviewCompanyId,Integer reviewType,Integer nowLevel,int deptLevel,Integer orderType){
		int count = 0;
		StringBuffer sbf = new StringBuffer();
		List<Object> ls=new ArrayList<Object>();
		ls.add(UserUtils.getUser().getCompany().getId());
	//	ls.add(vo.getOrderType());
		ls.add(reviewType);
		
		sbf
		.append("SELECT r.id ");
	//	.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=? and r.productType=? and r.flowType =? ")
		//审核部分根据部门不同的审核层级，添加不同的查询条件
		if(2 == deptLevel){
			sbf.append(" from review r,sys_user sys  where r.createby=sys.id and sys.companyId=?  and r.flowType =? ")
			.append(" and r.deptId=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
		}else if(1 == deptLevel){
			sbf.append(" from review r,sys_user sys,department dep  where r.createby=sys.id  and r.deptId=dep.id and sys.companyId=?  and r.flowType =? ")
			.append(" and dep.parent_id=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
		}else {
			return count;
		}
		
		
		
		// 添加审核查询控制(全部、未审核、已驳回、已通过)
		sbf
		.append(getReviewCheckSql(vo.getReviewStatus(),nowLevel));
		List<Map<Object,Object>>list=productorderDao.findBySql(sbf.toString(),ls.toArray());
		count = list.size();
		return count;
			
	}
	
	
	/**
	 * add by ruyi.chen
	 * add date 2015-04-27
	 * describe 装配当前审核人所拥有审核职务的  审核状态  审核条数
	 * @param flowType
	 * @param userJobs
	 * @param reviewStatus  1：待审核
	 * @return
	 */
	public List<UserJob> getUserJobsReviewCountByType(Integer flowType,List<UserJob> userJobs,Integer reviewStatus){
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(null != userJobs && 0 < userJobs.size()){
			for(UserJob uj :userJobs){
				int count = 0;
				OrderExitGroupReviewVO vo = new OrderExitGroupReviewVO();
				vo.setReviewStatus(reviewStatus);
				// 获取review_company 实例
				List<Long> reviewCompanyIds = Lists.newArrayList();
				if(null != uj.getDeptLevel() && 1 == uj.getDeptLevel().intValue()){
					 reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getDeptId());
					 //获得当前职位负责的审核层级列表
					 List<Integer> jobs = reviewService.getJobLevel( uj.getDeptId(),uj.getJobId(),flowType);
					 vo.setUserLevel(jobs.get(0));
					 count = getAllCommonReviewCount(vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),1,uj.getOrderType());
					 uj.setCount(count);
				}else if(null != uj.getDeptLevel() && 2 == uj.getDeptLevel().intValue()){
					reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getParentDept());
					//获得当前职位负责的审核层级列表
					 List<Integer> jobs = reviewService.getJobLevel( uj.getParentDept(),uj.getJobId(),flowType);
					 vo.setUserLevel(jobs.get(0));
					 count = getAllCommonReviewCount(vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),2,uj.getOrderType());
					 uj.setCount(count);
				}
			}				
	  }
	 return userJobs ;
	}
	
	/**
	 * 获取审核信息列表(查询审核信息基础方法，具体业务数据请关联业务表获取,适用于机票基础审核查询)
	 * create by ruyi.chen
	 * create date 2015-03-18
	 * @return
	 */
	private Page<Map<Object, Object>> getPlaneCommonReviewList(Page<Map<Object, Object>>page, 
			OrderExitGroupReviewVO vo, Long deptId, Long reviewCompanyId, Integer reviewType, Integer nowLevel, int deptLevel, Integer orderType){
		if (StringUtils.isBlank(page.getOrderBy())){
			page.setOrderBy(" aa.rid DESC ");
		}
		StringBuffer sbf = new StringBuffer();
		List<Object> ls=new ArrayList<Object>();
		ls.add(UserUtils.getUser().getCompany().getId());
//		ls.add(vo.getOrderType());
		ls.add(reviewType);
		
		Page<Map<Object, Object>> pageInfo;
		sbf
		.append("select aa.*,a.orderNum,a.orderTime,a.groupCode,a.meter,a.saler, a.picker, a.acitivityName,a.productId, ")
		.append("a.orderCompanyName")
		.append(" from (SELECT r.id AS rid,r.orderId,r.productType as orderType,r.updateBy,")
		.append(" r.createReason,r.createDate,r.updateDate, r.updateByName as beforeReviewName,r.topLevel,r.nowLevel,(r.status) as reviewStatus,r.printTime,r.printFlag,");
//		.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=? and r.productType=? and r.flowType =? ")
		//审核部分根据部门不同的审核层级，添加不同的查询条件
		if(2 == deptLevel){
			sbf.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=?  and r.flowType =? ")
			.append(" and r.deptId=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
		}else if(1 == deptLevel){
			sbf.append(" r.travelerId from review r,sys_user sys,department dep  where r.createby=sys.id  and r.deptId=dep.id and sys.companyId=?  and r.flowType =? ")
			.append(" and dep.parent_id=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
		}else {
			return null;
		}
		
		
		
		// 添加审核查询控制(全部、未审核、已驳回、已通过)
		sbf
		.append(getNewReviewCheckSql(vo.getReviewStatus(),nowLevel))
		.append(" )aa INNER JOIN (select s.* from(SELECT DISTINCT p.id AS orderId,p.order_no as orderNum,p.create_date as orderTime,")
		.append(" p.group_code as groupCode,ta.product_code as acitivityName,ta.createBy AS meter,ta.id as productId,p.create_by AS picker, p.salerId AS saler,ag.agentName as orderCompanyName from airticket_order p ")
		.append("INNER JOIN agentinfo ag ON ag.id = p.agentinfo_id INNER JOIN activity_airticket ta ON ta.id = p.airticket_id ");
		// 添加页面条件查询控制(销售、计调、日期等)
		if(StringUtils.isNotBlank(vo.getOrderNum())){
			sbf.append(" and p.order_no like'%"+vo.getOrderNum()+"%' ");
		}
		if(StringUtils.isNotBlank(vo.getChannel())){
			sbf.append(" and p.agentinfo_id=? ");
			ls.add(vo.getChannel());
		}
		if(StringUtils.isNotBlank(vo.getGroupCode())){
			sbf.append(" and p.group_code like'%"+vo.getGroupCode()+"%' ");
//			ls.add(vo.getChannel());
		}
		if(StringUtils.isNotBlank(vo.getMeter())){
			sbf.append(" and ta.createBy=? ");
			ls.add(vo.getMeter());
		}
		if(StringUtils.isNotBlank(vo.getOrderNo())){
			sbf.append(" and p.order_no = ? ");
			ls.add(vo.getOrderNo());
		}
		if(StringUtils.isNotBlank(vo.getPicker())){
			sbf.append(" and p.create_by=? ");
			ls.add(vo.getPicker());
		}
		
		if(StringUtils.isNotBlank(vo.getSaler())){
			sbf.append(" and p.salerId=? ");
			ls.add(vo.getSaler());
		}
//		if(StringUtils.isNotBlank(vo.getCreateBy())){
//			sbf.append(" and p.create_by=? ");
//			ls.add(vo.getCreateBy());
//		}
//		if(StringUtils.isNotBlank(vo.getOrderType())){
//			sbf.append(" and p.orderStatus=? ");
//			ls.add(vo.getOrderType());
//		}
		if(StringUtils.isNotBlank(vo.getOrderTimeBegin())){
			sbf.append(" and p.create_date>=? ");
			ls.add(vo.getOrderTimeBegin()+" 00:00:00");
		}
		if(StringUtils.isNotBlank(vo.getOrderTimeEnd())){
			sbf.append(" and p.create_date<=? ");
			ls.add(vo.getOrderTimeEnd()+" 23:59:59");
		}
		
		sbf
		.append(")s where s.orderId is not null) a ON aa.orderId = a.orderId  ");
	    pageInfo= productorderDao.findPageBySql(page,sbf.toString(), Map.class,ls.toArray());		
		List<Map<Object,Object>>list=pageInfo.getList();
		if(list.size()>0){
			for(Map<Object,Object> map:list){
				String travelerId ="";
				if(null != map.get("travelerId")){
					travelerId = map.get("travelerId").toString();
				}
				
				
				if(StringUtils.isNotBlank(travelerId)){
					Traveler t = travelerDao.findById(Long.parseLong(travelerId));
					if(null != t && 0 < t.getId().intValue()){
						String payPrice = OrderCommonUtil.getMoneyAmountBySerialNum(t.getPayPriceSerialNum(),2);
						map.put("payPrice",payPrice);
						map.put("travelerName", t.getName());
					}
					
				}else{
					map.put("travelerName", "团队");
					
				}
				
				Integer myStatus = 0;
				if(map.get("nowLevel").toString().equals(nowLevel.toString())){
					myStatus=1;
				}
				map.put("myStatus", myStatus);
			}
		}
		return pageInfo;
						
	}
	/**
	 * 获取单团类借款审核信息列表
	 * create by ruyi.chen
	 * create date 2015-05-05
	 * @return
	 */
	public Page<Map<Object, Object>> getBorrowingReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,List<UserJob> userJobs){
		// TODO 添加借款审核业务数据
		Page<Map<Object,Object>> pageInfo = getReviewLists(page,vo,Context.REVIEW_FLOWTYPE_BORROWMONEY,userJobs);
		if(null != pageInfo && pageInfo.getList().size() > 0){
			List<Map<Object,Object>> list =pageInfo.getList();
			for(Map<Object,Object> map:list){
				String rid = map.get("rid").toString();
				if(StringUtils.isNotBlank(rid)){
					Map<String,String> rMap = reviewService.findReview(Long.parseLong(rid));
					BorrowingBean borr = new BorrowingBean(rMap);
					if(borr.getTravelerId().contains(BorrowingBean.REGEX)){
						borr.setTravelerName("团队");
					}
					if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(BorrowingBean.REGEX)){
						String compPrice = "";
						if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
							String[] cMarks = borr.getCurrencyMarks().split(BorrowingBean.REGEX);	
							String[] cPrices = borr.getBorrowPrices().split(BorrowingBean.REGEX);
							for(int i=0;i<cMarks.length;i++){
								compPrice+=cMarks[i]+cPrices[i]+"+";
							}
							borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
						}
						
					}else{
					    borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
					}
					map.put("payPrice", borr.getCurrencyIds());
					//map.put("travelerName", borr.getTravelerName());
				}
			}
		}
		return pageInfo;
	}
	/**
	 * 获取机票借款审核信息列表
	 * create by ruyi.chen
	 * create date 2015-05-05
	 * @return
	 */
	public Page<Map<Object, Object>> getPlaneBorrowingReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,List<UserJob> userJobs){
		// TODO 添加借款审核业务数据
		Page<Map<Object,Object>> pageInfo = getPlaneReviewLists(page,vo,Context.REVIEW_FLOWTYPE_BORROWMONEY,userJobs);
		if(null != pageInfo && pageInfo.getList().size() > 0){
			List<Map<Object,Object>> list =pageInfo.getList();
			for(Map<Object,Object> map:list){
				String rid = map.get("rid").toString();
				if(StringUtils.isNotBlank(rid)){
					Map<String,String> rMap = reviewService.findReview(Long.parseLong(rid));
					BorrowingBean borr = new BorrowingBean(rMap);
					if(borr.getTravelerId().contains(BorrowingBean.REGEX)){
						borr.setTravelerName("团队");
					}
					if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(BorrowingBean.REGEX)){
						String compPrice = "";
						if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
							String[] cMarks = borr.getCurrencyMarks().split(BorrowingBean.REGEX);	
							String[] cPrices = borr.getBorrowPrices().split(BorrowingBean.REGEX);
							for(int i=0;i<cMarks.length;i++){
								compPrice+=cMarks[i]+cPrices[i]+"+";
							}
							borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
						}
						
					}else{
					    borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
					}
					map.put("payPrice", borr.getCurrencyIds());
					//map.put("travelerName", borr.getTravelerName());
				}
			}
		}
		return pageInfo;
	}
	/**add by ruyi.chen
	 * update date 2014-12-28
	 * 不同审核状态  审核查询控制sql拼接(修改本例适合各种审核查询)
	 * 0：全部   1：待审核   2：未通过(驳回)  3:已通过     4:审核中  5:已取消
	 * 
	 */
	private String getNewReviewCheckSql(int reviewStatus,int userLevel){
		StringBuffer sbf=new StringBuffer();
		switch(reviewStatus){
			case 0:
				
				break;
			case 1:
				sbf.append(" AND r.active = 1 and r.status =1  and r.nowLevel="+userLevel);
				break;
			case 2:
				sbf.append(" and r.status =0 ");
				break;
			case 3:
				sbf.append(" and r.status in (2,3)");
				break;
			case 4:
				sbf.append(" and r.status=1 ");
				break;
			case 5:
				sbf.append(" and r.status=4 ");
			default:
				break;
		}
		
		return sbf.toString();
	}
private Page<Map<Object, Object>> getPlaneReviewLists(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,Integer flowType,List<UserJob> userJobs){
		
		if(null != userJobs && 0 < userJobs.size()){
		//页面有部门职务查询时查询条件加载，没有部门职务查询时设默认值
		UserJob uj = new UserJob();
		if(vo.getRid() == 0){								
			vo.setRid(userJobs.get(0).getId());
			uj = userJobs.get(0);
		}else{
			boolean flag = false;
			for(UserJob r:userJobs){
				if(r.getId().longValue() == vo.getRid()){
					flag = true;
					uj = r;
					}
			}
			if(!flag){
				vo.setRid(userJobs.get(0).getId());
				uj = userJobs.get(0);
			}
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		// 获取review_company 实例
		List<Long> reviewCompanyIds = Lists.newArrayList();
		if(null != uj.getDeptLevel() && 1 == uj.getDeptLevel().intValue()){
			 reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getDeptId());
			 //获得当前职位负责的审核层级列表
			 List<Integer> jobs = reviewService.getJobLevel( uj.getDeptId(),uj.getJobId(),flowType);
			 vo.setUserLevel(jobs.get(0));
			 return getPlaneCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),1,uj.getOrderType());
		}else if(null != uj.getDeptLevel() && 2 == uj.getDeptLevel().intValue()){
			reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getParentDept());
			//获得当前职位负责的审核层级列表
			 List<Integer> jobs = reviewService.getJobLevel( uj.getParentDept(),uj.getJobId(),flowType);
			 vo.setUserLevel(jobs.get(0));
			 return getPlaneCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),2,uj.getOrderType());
		}else{
			return null;
		}					
					
	  }else {
		  return null;
	  }
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-05-12
	 * describe 借款审核
	 * @param rid
	 * @param roleId
	 * @param result
	 * @param denyReason
	 * @param userLevel
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public  Map<String,Object> reviewBorrowing(long rid,long roleId,Integer result,String denyReason,Integer userLevel,HttpServletRequest request){
		int backSign=0;
		Map<String,Object>map = new HashMap<String,Object>();
		Review r=reviewService.findReviewInfo(rid);
		if(null != r&&r.getId()>0 && REVIEW_UNAUDITED.intValue() == r.getStatus().intValue()){
			backSign = reviewService.UpdateReview(rid, userLevel, result, denyReason);
			boolean flag = false;
			if(backSign == 1){
				try {
					 flag = airTicketOrderLendMoneyService.saveLendMoney2MoneyAmount(rid, r.getOrderId(), r.getProductType());
//					 reviewService.reviewOperationDone(rid, 3);
				} catch (Exception e) {
					map.put("flag", "0");
					map.put("message", "操作失败!");
					return map;
				}
				if(flag){
					map.put("flag", "1");
					map.put("message", "操作成功！");	
				}else{
					map.put("flag", "0");
					map.put("message", "操作失败!");
				}
			}else{
				map.put("flag", "1");
				map.put("message", "操作成功！");	
			}
					
		}else{
			map.put("flag", "0");
			map.put("message", "操作失败!");
		}
		
		return map;
		
	}
	
    /**
	 * 财务审核 获取机票返佣审核信息列表
	 * add by wangXK
	 * @return
	 */
	public Page<Map<Object, Object>> queryActivityRebatesList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,List<UserJob> userJobs){
		Page<Map<Object, Object>>  p= queryActivityReviewLists(page,vo,Context.REBATES_FLOW_TYPE,userJobs);
		return p;
	}
	
    /**
	 * 财务审核 获取机票返佣审核信息列表
	 * add by wangXK
	 * @return
	 */
    private Page<Map<Object, Object>> queryActivityReviewLists(Page<Map<Object, Object>>page, 
    		OrderExitGroupReviewVO vo, Integer flowType, List<UserJob> userJobs) {
		if(null != userJobs && 0 < userJobs.size()){
		//页面有部门职务查询时查询条件加载，没有部门职务查询时设默认值,设置默认值，查询的是机票返佣的业务 oderType = 7 机票业务。
		UserJob uj = new UserJob();
		if(vo.getRid() == 0){//查询的是机票返佣的业务 oderType = 7 机票业务。								
			for(int i=0;i<userJobs.size();i++){
				if(userJobs.get(i).getOrderType()==7){
					uj = userJobs.get(i);
					vo.setRid(userJobs.get(i).getId());
					break;
				}
			}
			
		}else{
			boolean flag = false;
			for(UserJob r:userJobs){
				if(r.getId().longValue() == vo.getRid()){
					flag = true;
					uj = r;
					}
			}
			if(!flag){//查询的是机票返佣的业务 oderType = 7 机票业务。
				for(int i=0;i<userJobs.size();i++){
					if(userJobs.get(i).getOrderType()==7){
						uj = userJobs.get(i);
						vo.setRid(userJobs.get(i).getId());
						break;
					}
				}
			}
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		// 获取review_company 实例
		List<Long> reviewCompanyIds = Lists.newArrayList();
		if(null != uj.getDeptLevel() && 1 == uj.getDeptLevel().intValue()){
			 reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getDeptId());
			 //获得当前职位负责的审核层级列表
			 List<Integer> jobs = reviewService.getJobLevel( uj.getDeptId(),uj.getJobId(),flowType);
			 vo.setUserLevel(jobs.get(0));
			 return queryPlaneCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),1,uj.getOrderType());
		}else if(null != uj.getDeptLevel() && 2 == uj.getDeptLevel().intValue()){
			reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getParentDept());
			//获得当前职位负责的审核层级列表
			 List<Integer> jobs = reviewService.getJobLevel( uj.getParentDept(),uj.getJobId(),flowType);
			 vo.setUserLevel(jobs.get(0));
			 return queryPlaneCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),2,uj.getOrderType());
		}else{
			return null;
		}					
					
	  }else {
		  return null;
	  }
	}
	/**
	 *财务审核  获取审核信息列表(查询审核信息基础方法，具体业务数据请关联业务表获取,适用于机票基础审核查询)
	 * create by wangXK
	 * create date 2015-08-04
	 * @return
	 */
	private Page<Map<Object, Object>> queryPlaneCommonReviewList(Page<Map<Object, Object>>page, 
			OrderExitGroupReviewVO vo, Long deptId, Long reviewCompanyId, Integer reviewType, Integer nowLevel, int deptLevel, Integer orderType){
		if (StringUtils.isBlank(page.getOrderBy())){
			page.setOrderBy(" aa.rid DESC ");
		}
		StringBuffer sbf = new StringBuffer();
		List<Object> ls=new ArrayList<Object>();
		ls.add(UserUtils.getUser().getCompany().getId());
//		ls.add(vo.getOrderType());
		ls.add(reviewType);
		
		Page<Map<Object, Object>> pageInfo;
		sbf
		.append("select DISTINCT   aa.*,a.orderNum,a.orderTime,a.groupCode,a.meter,a.saler,a.picker,a.acitivityName,a.productId, ")
		.append("a.orderCompanyName")
		.append(" from (SELECT r.id AS rid,r.orderId,r.productType as orderType,r.updateBy,")
		.append(" r.createReason,r.createDate,r.updateDate, r.updateByName as beforeReviewName,r.topLevel,r.nowLevel,(r.status) as reviewStatus,r.printTime,r.printFlag,");
		//审核部分根据部门不同的审核层级，添加不同的查询条件
		if(2 == deptLevel){
			sbf.append(" r.travelerId from review r,sys_user sys,order_rebates re where r.createby=sys.id and r.id=re.rid and sys.companyId=?  and r.flowType =? ")
			.append(" and r.deptId=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
			if(StringUtils.isNotBlank(vo.getCreateTimeBegin())){//报批时间   r.createDate
				sbf.append(" and r.createDate>=? ");
				ls.add(vo.getCreateTimeBegin()+" 00:00:00");
			}
			if(StringUtils.isNotBlank(vo.getCreateTimeEnd())){
				sbf.append(" and r.createDate<=? ");
				ls.add(vo.getCreateTimeEnd()+" 23:59:59");
			}
			if(StringUtils.isNotBlank(vo.getRebatesDiffBegin())){//返佣差额(返佣金额)
				BigDecimal rebatesDiffBegin = new BigDecimal(vo.getRebatesDiffBegin());
				sbf.append(" and re.rebatesDiff >=? ");
				ls.add(rebatesDiffBegin);
			}
			if(StringUtils.isNotBlank(vo.getRebatesDiffEnd())){//返佣差额(返佣金额)
				BigDecimal rebatesDiffEnd = new BigDecimal(vo.getRebatesDiffEnd());
				sbf.append(" and re.rebatesDiff <=?");
				ls.add(rebatesDiffEnd);
			}
			
			if(StringUtils.isNotBlank(vo.getPrintFlag())){//打印状态
				if("1".equals(vo.getPrintFlag().trim())){
					sbf.append(" and r.printFlag = '"+vo.getPrintFlag()+"' ");
				}else{
					sbf.append(" and (r.printFlag = '0' or r.printFlag is null) ");
				}
				
			}
		}else if(1 == deptLevel){
			sbf.append(" r.travelerId from review r,sys_user sys,department dep,order_rebates re  where r.createby=sys.id  and r.deptId=dep.id and r.id=re.rid and sys.companyId=?  and r.flowType =? ")
			.append(" and dep.parent_id=? and r.review_company_id=? and r.productType =? ");
			ls.add(deptId);
			ls.add(reviewCompanyId);
			ls.add(orderType);
			if(StringUtils.isNotBlank(vo.getCreateTimeBegin())){//报批时间   r.createDate
				sbf.append(" and r.createDate>=? ");
				ls.add(vo.getCreateTimeBegin()+" 00:00:00");
			}
			if(StringUtils.isNotBlank(vo.getCreateTimeEnd())){
				sbf.append(" and r.createDate<=? ");
				ls.add(vo.getCreateTimeEnd()+" 23:59:59");
			}
			if(StringUtils.isNotBlank(vo.getRebatesDiffBegin())){//返佣差额(返佣金额)
				BigDecimal rebatesDiffBegin = new BigDecimal(vo.getRebatesDiffBegin());
				sbf.append(" and re.rebatesDiff >=? ");
				ls.add(rebatesDiffBegin);
			}
			if(StringUtils.isNotBlank(vo.getRebatesDiffEnd())){//返佣差额(返佣金额)
				BigDecimal rebatesDiffEnd = new BigDecimal(vo.getRebatesDiffEnd());
				sbf.append(" and re.rebatesDiff <=?");
				ls.add(rebatesDiffEnd);
			}
			
			if(StringUtils.isNotBlank(vo.getPrintFlag())){//打印状态
				if("1".equals(vo.getPrintFlag().trim())){
					sbf.append(" and r.printFlag = '"+vo.getPrintFlag()+"' ");
				}else{
					sbf.append(" and (r.printFlag = '0' or r.printFlag is null) ");
				}
				
			}
		}else {
			return null;
		}
		// 添加审核查询控制(全部、未审核、已驳回、已通过)
		sbf.append(getNewReviewCheckSql(vo.getReviewStatus(),nowLevel))
		.append(" )aa INNER JOIN (select s.* from(SELECT DISTINCT p.id AS orderId,p.order_no as orderNum,p.create_date as orderTime,")
		.append(" p.group_code as groupCode,ta.product_code as acitivityName,ta.createBy AS meter,ta.id as productId,p.create_by AS picker, p.salerId AS saler,ag.agentName as orderCompanyName from airticket_order p ")
		.append("INNER JOIN agentinfo ag ON ag.id = p.agentinfo_id INNER JOIN activity_airticket ta ON ta.id = p.airticket_id ");
		// 添加页面条件查询控制(销售、计调、日期等)
		if(StringUtils.isNotBlank(vo.getGroupCode())){//团号
			sbf.append(" and p.group_code like'%"+vo.getGroupCode()+"%' ");
			//ls.add(vo.getGroupCode());
		}
		
		if(StringUtils.isNotBlank(vo.getOrderNo())){//订单号
			sbf.append(" and p.order_no like '%"+vo.getOrderNo()+"%' ");
		}
		if(StringUtils.isNotBlank(vo.getChannel())){//渠道
			sbf.append(" and p.agentinfo_id=? ");
			ls.add(vo.getChannel());
		}
		if(StringUtils.isNotBlank(vo.getSaler())){//销售
			sbf.append(" and p.salerId=? ");
			ls.add(vo.getSaler());
		}
		if(StringUtils.isNotBlank(vo.getPicker())){//下单人
			sbf.append(" and p.create_by=? ");
			ls.add(vo.getPicker());
		}
		if(StringUtils.isNotBlank(vo.getMeter())){//计调
			sbf.append(" and ta.createBy=? ");
			ls.add(vo.getMeter());
		}
		
		sbf.append(")s where s.orderId is not null) a ON aa.orderId = a.orderId  ");
	    pageInfo= productorderDao.findPageBySql(page,sbf.toString(), Map.class,ls.toArray());		
		List<Map<Object,Object>>list=pageInfo.getList();
		if(list.size()>0){
			for(Map<Object,Object> map:list){
				String travelerId ="";
				if(null != map.get("travelerId")){
					travelerId = map.get("travelerId").toString();
				}
				
				
				if(StringUtils.isNotBlank(travelerId)){
					Traveler t = travelerDao.findById(Long.parseLong(travelerId));
					if(null != t && 0 < t.getId().intValue()){
						String payPrice = OrderCommonUtil.getMoneyAmountBySerialNum(t.getPayPriceSerialNum(),2);
						map.put("payPrice",payPrice);
						map.put("travelerName", t.getName());
					}
					
				}else{
					map.put("travelerName", "团队");
					
				}
				
				Integer myStatus = 0;
				if(map.get("nowLevel").toString().equals(nowLevel.toString())){
					myStatus=1;
				}
				map.put("myStatus", myStatus);
			}
		}
		return pageInfo;
						
	}
    /**
	 * 获取审核信息列表(查询审核信息基础方法，具体业务数据请关联业务表获取,适用于海岛游类基础审核查询)
	 * create by ruyi.chen
	 * create date 2015-06-09
	 * @return
	 */
	private Page<Map<Object, Object>> getIslandCommonReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,Long deptId,Long reviewCompanyId,Integer reviewType,Integer nowLevel,int deptLevel,Integer orderType){
		if (StringUtils.isBlank(page.getOrderBy())){
			page.setOrderBy(" aa.rid DESC ");
		}
		StringBuffer sbf = new StringBuffer();
		List<Object> ls=new ArrayList<Object>();
		ls.add(UserUtils.getUser().getCompany().getId());
//		ls.add(vo.getOrderType());
		ls.add(reviewType);
		
		Page<Map<Object, Object>> pageInfo;
		sbf
		.append("select aa.*,a.orderNum,a.orderTime,a.groupCode,a.meter,a.saler,a.acitivityName,a.productId,a.island_order_uuid,")
		.append("a.orderCompanyName,")
		.append("a.activity_island_group_uuid,a.activity_island_uuid from (SELECT r.id AS rid,r.orderId,r.productType as orderType,r.updateBy,")
		.append(" r.createReason,r.createDate,r.updateDate, r.updateByName as beforeReviewName,r.topLevel,r.nowLevel,(r.status) as reviewStatus,r.printTime,r.printFlag,");
//		.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=? and r.productType=? and r.flowType =? ")
		//审核部分根据部门不同的审核层级，添加不同的查询条件
		
//		if(2 == deptLevel){
//			sbf.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=?  and r.flowType =? ")
//			.append(" and r.deptId=? and r.review_company_id=? and r.productType =? ");
//			ls.add(deptId);
//			ls.add(reviewCompanyId);
//			ls.add(orderType);
//		}else if(1 == deptLevel){
//			sbf.append(" r.travelerId from review r,sys_user sys,department dep  where r.createby=sys.id  and r.deptId=dep.id and sys.companyId=?  and r.flowType =? ")
//			.append(" and dep.parent_id=? and r.review_company_id=? and r.productType =? ");
//			ls.add(deptId);
//			ls.add(reviewCompanyId);
//			ls.add(orderType);
//		}else {
//			return null;
//		}
		//暂时先屏蔽职位限制
		sbf.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=?  and r.flowType =? and r.productType = ? ");
		ls.add(orderType);
		
		// 添加审核查询控制(全部、未审核、已驳回、已通过)
		sbf
		.append(getNewReviewCheckSql(vo.getReviewStatus(),nowLevel))
		.append(" )aa INNER JOIN (select s.* from(SELECT DISTINCT p.id AS orderId,p.orderNum,p.createDate as orderTime,p.uuid as island_order_uuid,")
		.append("ag.uuid as activity_island_group_uuid,ta.uuid as activity_island_uuid,")
		.append(" p.group_code as groupCode,ta.product_code as acitivityName,ta.createBy AS meter,ta.id as productId,p.create_by AS saler,ag.agentName as orderCompanyName from island_order p ")
		.append("INNER JOIN activity_island_group ag ON ag.uuid = p.activity_island_group_uuid INNER JOIN activity_island ta ON ta.uuid = p.activity_island_uuid ");
		// 添加页面条件查询控制(销售、计调、日期等)
		if(StringUtils.isNotBlank(vo.getOrderNum())){
			sbf.append(" and p.orderNum like'%"+vo.getOrderNum()+"%' ");
		}
		if(StringUtils.isNotBlank(vo.getChannel())){
			sbf.append(" and p.orderCompany=? ");
			ls.add(vo.getChannel());
		}
		if(StringUtils.isNotBlank(vo.getGroupCode())){
			sbf.append(" and ag.groupCode like'%"+vo.getGroupCode()+"%' ");
//			ls.add(vo.getChannel());
		}
		if(StringUtils.isNotBlank(vo.getMeter())){
			sbf.append(" and ta.createBy=? ");
			ls.add(vo.getMeter());
		}
		if(StringUtils.isNotBlank(vo.getSaler())){
			sbf.append(" and p.createBy=? ");
			ls.add(vo.getSaler());
		}
//		if(StringUtils.isNotBlank(vo.getOrderType())){
//			sbf.append(" and p.orderStatus=? ");
//			ls.add(vo.getOrderType());
//		}
		if(StringUtils.isNotBlank(vo.getOrderTimeBegin())){
			sbf.append(" and p.createDate>=? ");
			ls.add(vo.getOrderTimeBegin()+" 00:00:00");
		}
		if(StringUtils.isNotBlank(vo.getOrderTimeEnd())){
			sbf.append(" and p.createDate<=? ");
			ls.add(vo.getOrderTimeEnd()+" 23:59:59");
		}
		
		sbf
		.append(")s where s.orderId is not null) a ON aa.orderId = a.orderId  ");
	    pageInfo= productorderDao.findPageBySql(page,sbf.toString(), Map.class,ls.toArray());		
		List<Map<Object,Object>>list=pageInfo.getList();
		if(list.size()>0){
			for(Map<Object,Object> map:list){
				//String price=map.get("payPrice").toString();
				String travelerId ="";
				if(null != map.get("travelerId")){
					travelerId = map.get("travelerId").toString();
				}
				
				
				if(StringUtils.isNotBlank(travelerId)){
//					IslandTraveler t = islandTravelerDao.getById(travelerId);
//					if(null != t && 0 < t.getId().intValue()){
//						String payPrice = OrderCommonUtil.getMoneyAmountBySerialNum(t.getPayPriceSerialNum(),2);
//						map.put("payPrice",payPrice);
//						map.put("travelerName", t.getName());
//					}
					
				}else{
					map.put("travelerName", "团队");
					
				}
				
				Integer myStatus = 0;
				if(map.get("nowLevel").toString().equals(nowLevel.toString())){
					myStatus=1;
				}
				map.put("myStatus", myStatus);
			}
		}
		return pageInfo;
						
	}
	
	/**
	 * 获取审核信息列表(查询审核信息基础方法，具体业务数据请关联业务表获取,适用于酒店类基础审核查询)
	 * create by ruyi.chen
	 * create date 2015-06-09
	 * @return
	 */
	private Page<Map<Object, Object>> getHotelCommonReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,Long deptId,Long reviewCompanyId,Integer reviewType,Integer nowLevel,int deptLevel,Integer orderType){
		if (StringUtils.isBlank(page.getOrderBy())){
			page.setOrderBy(" aa.rid DESC ");
		}
		StringBuffer sbf = new StringBuffer();
		List<Object> ls=new ArrayList<Object>();
		ls.add(UserUtils.getUser().getCompany().getId());
//		ls.add(vo.getOrderType());
		ls.add(reviewType);
		
		Page<Map<Object, Object>> pageInfo;
		sbf
		.append("select aa.*,a.orderNum,a.orderTime,a.groupCode,a.meter,a.saler,a.acitivityName,a.productId,a.hotel_order_uuid, ")
		.append("a.orderCompanyName,")
		.append("a.activity_hotel_group_uuid,a.activity_hotel_uuid from (SELECT r.id AS rid,r.orderId,r.productType as orderType,r.updateBy,")
		.append(" r.createReason,r.createDate,r.updateDate, r.updateByName as beforeReviewName,r.topLevel,r.nowLevel,(r.status) as reviewStatus,r.printTime,r.printFlag,");
//		.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=? and r.productType=? and r.flowType =? ")
		//审核部分根据部门不同的审核层级，添加不同的查询条件
		
//		if(2 == deptLevel){
//			sbf.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=?  and r.flowType =? ")
//			.append(" and r.deptId=? and r.review_company_id=? and r.productType =? ");
//			ls.add(deptId);
//			ls.add(reviewCompanyId);
//			ls.add(orderType);
//		}else if(1 == deptLevel){
//			sbf.append(" r.travelerId from review r,sys_user sys,department dep  where r.createby=sys.id  and r.deptId=dep.id and sys.companyId=?  and r.flowType =? ")
//			.append(" and dep.parent_id=? and r.review_company_id=? and r.productType =? ");
//			ls.add(deptId);
//			ls.add(reviewCompanyId);
//			ls.add(orderType);
//		}else {
//			return null;
//		}
		//暂时先屏蔽职位限制
		sbf.append(" r.travelerId from review r,sys_user sys  where r.createby=sys.id and sys.companyId=?  and r.flowType =? and r.productType = ? ");
		ls.add(orderType);
		
		// 添加审核查询控制(全部、未审核、已驳回、已通过)
		sbf
		.append(getNewReviewCheckSql(vo.getReviewStatus(),nowLevel))
		.append(" )aa INNER JOIN (select s.* from(SELECT DISTINCT p.id AS orderId,p.orderNum,p.createDate as orderTime,p.uuid as hotel_order_uuid,")
		.append("ag.uuid as activity_hotel_group_uuid,ta.uuid as activity_hotel_uuid,")
		.append(" p.group_code as groupCode,ta.product_code as acitivityName,ta.createBy AS meter,ta.id as productId,p.create_by AS saler,ag.agentName as orderCompanyName from hotel_order p ")
		.append("INNER JOIN activity_hotel_group ag ON ag.uuid = p.activity_hotel_group_uuid INNER JOIN activity_hotel ta ON ta.uuid = p.activity_hotel_uuid ");
		// 添加页面条件查询控制(销售、计调、日期等)
		if(StringUtils.isNotBlank(vo.getOrderNum())){
			sbf.append(" and p.orderNum like'%"+vo.getOrderNum()+"%' ");
		}
		if(StringUtils.isNotBlank(vo.getChannel())){
			sbf.append(" and p.orderCompany=? ");
			ls.add(vo.getChannel());
		}
		if(StringUtils.isNotBlank(vo.getGroupCode())){
			sbf.append(" and ag.groupCode like'%"+vo.getGroupCode()+"%' ");
//			ls.add(vo.getChannel());
		}
		if(StringUtils.isNotBlank(vo.getMeter())){
			sbf.append(" and ta.createBy=? ");
			ls.add(vo.getMeter());
		}
		if(StringUtils.isNotBlank(vo.getSaler())){
			sbf.append(" and p.createBy=? ");
			ls.add(vo.getSaler());
		}
//		if(StringUtils.isNotBlank(vo.getOrderType())){
//			sbf.append(" and p.orderStatus=? ");
//			ls.add(vo.getOrderType());
//		}
		if(StringUtils.isNotBlank(vo.getOrderTimeBegin())){
			sbf.append(" and p.createDate>=? ");
			ls.add(vo.getOrderTimeBegin()+" 00:00:00");
		}
		if(StringUtils.isNotBlank(vo.getOrderTimeEnd())){
			sbf.append(" and p.createDate<=? ");
			ls.add(vo.getOrderTimeEnd()+" 23:59:59");
		}
		
		sbf
		.append(")s where s.orderId is not null) a ON aa.orderId = a.orderId  ");
	    pageInfo= productorderDao.findPageBySql(page,sbf.toString(), Map.class,ls.toArray());		
		List<Map<Object,Object>>list=pageInfo.getList();
		if(list.size()>0){
			for(Map<Object,Object> map:list){
				//String price=map.get("payPrice").toString();
				String travelerId ="";
				if(null != map.get("travelerId")){
					travelerId = map.get("travelerId").toString();
				}
				
				
				if(StringUtils.isNotBlank(travelerId)){
//					IslandTraveler t = islandTravelerDao.getById(travelerId);
//					if(null != t && 0 < t.getId().intValue()){
//						String payPrice = OrderCommonUtil.getMoneyAmountBySerialNum(t.getPayPriceSerialNum(),2);
//						map.put("payPrice",payPrice);
//						map.put("travelerName", t.getName());
//					}
					
				}else{
					map.put("travelerName", "团队");
					
				}
				
				Integer myStatus = 0;
				if(map.get("nowLevel").toString().equals(nowLevel.toString())){
					myStatus=1;
				}
				map.put("myStatus", myStatus);
			}
		}
		return pageInfo;
						
	}
	
	private Page<Map<Object, Object>> getHotelReviewLists(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,Integer flowType,List<UserJob> userJobs){
			
//			if(null != userJobs && 0 < userJobs.size()){
//			//页面有部门职务查询时查询条件加载，没有部门职务查询时设默认值
//			UserJob uj = new UserJob();
//			if(vo.getRid() == 0){								
//				vo.setRid(userJobs.get(0).getId());
//				uj = userJobs.get(0);
//			}else{
//				boolean flag = false;
//				for(UserJob r:userJobs){
//					if(r.getId().longValue() == vo.getRid()){
//						flag = true;
//						uj = r;
//						}
//				}
//				if(!flag){
//					vo.setRid(userJobs.get(0).getId());
//					uj = userJobs.get(0);
//				}
//			}
//			Long companyId = UserUtils.getUser().getCompany().getId();
//			
//			// 获取review_company 实例
//			List<Long> reviewCompanyIds = Lists.newArrayList();
//			if(null != uj.getDeptLevel() && 1 == uj.getDeptLevel().intValue()){
//				 reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getDeptId());
//				 //获得当前职位负责的审核层级列表
//				 List<Integer> jobs = reviewService.getJobLevel( uj.getDeptId(),uj.getJobId(),flowType);
//				 vo.setUserLevel(jobs.get(0));
//				 return getAllCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),1,uj.getOrderType());
//			}else if(null != uj.getDeptLevel() && 2 == uj.getDeptLevel().intValue()){
//				reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getParentDept());
//				//获得当前职位负责的审核层级列表
//				 List<Integer> jobs = reviewService.getJobLevel( uj.getParentDept(),uj.getJobId(),flowType);
//				 vo.setUserLevel(jobs.get(0));
//				 return getAllCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),2,uj.getOrderType());
//			}else{
//				return null;
//			}					
//						
//		  }else {
//			  return null;
//		  }
		
		return getHotelCommonReviewList(page,vo,0l,0l,flowType,0,2,12);	
	}

	private Page<Map<Object, Object>> getIslandReviewLists(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,Integer flowType,List<UserJob> userJobs){
		
//		if(null != userJobs && 0 < userJobs.size()){
//		//页面有部门职务查询时查询条件加载，没有部门职务查询时设默认值
//		UserJob uj = new UserJob();
//		if(vo.getRid() == 0){								
//			vo.setRid(userJobs.get(0).getId());
//			uj = userJobs.get(0);
//		}else{
//			boolean flag = false;
//			for(UserJob r:userJobs){
//				if(r.getId().longValue() == vo.getRid()){
//					flag = true;
//					uj = r;
//					}
//			}
//			if(!flag){
//				vo.setRid(userJobs.get(0).getId());
//				uj = userJobs.get(0);
//			}
//		}
//		Long companyId = UserUtils.getUser().getCompany().getId();
//		
//		// 获取review_company 实例
//		List<Long> reviewCompanyIds = Lists.newArrayList();
//		if(null != uj.getDeptLevel() && 1 == uj.getDeptLevel().intValue()){
//			 reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getDeptId());
//			 //获得当前职位负责的审核层级列表
//			 List<Integer> jobs = reviewService.getJobLevel( uj.getDeptId(),uj.getJobId(),flowType);
//			 vo.setUserLevel(jobs.get(0));
//			 return getAllCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),1,uj.getOrderType());
//		}else if(null != uj.getDeptLevel() && 2 == uj.getDeptLevel().intValue()){
//			reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(companyId,flowType,uj.getParentDept());
//			//获得当前职位负责的审核层级列表
//			 List<Integer> jobs = reviewService.getJobLevel( uj.getParentDept(),uj.getJobId(),flowType);
//			 vo.setUserLevel(jobs.get(0));
//			 return getAllCommonReviewList(page,vo,uj.getDeptId(),reviewCompanyIds.get(0),flowType,jobs.get(0),2,uj.getOrderType());
//		}else{
//			return null;
//		}					
//					
//	  }else {
//		  return null;
//	  }
		return getIslandCommonReviewList(page,vo,0l,0l,flowType,0,2,12);
	}
}
