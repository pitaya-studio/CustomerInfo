package com.trekiz.admin.modules.island.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.airticketorder.service.AirTicketOrderLendMoneyService;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.island.dao.HotelRebatesDao;
import com.trekiz.admin.modules.island.dao.IslandRebatesDao;
import com.trekiz.admin.modules.island.dao.IslandTravelerDao;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.HotelRebates;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandRebates;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandReviewService;
import com.trekiz.admin.modules.order.entity.OrderExitGroupReviewVO;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
@Service
@Transactional(readOnly = true)
public class IslandReviewServiceImpl extends BaseService implements IslandReviewService{

	@Autowired
	private IslandTravelerDao islandTravelerDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private AirTicketOrderLendMoneyService airTicketOrderLendMoneyService;
	@Autowired
	private IslandRebatesDao islandRebatesDao;
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private HotelRebatesDao hotelRebatesDao;
	/**
	 * 获取审核信息列表(查询审核信息基础方法，具体业务数据请关联业务表获取,适用于海岛游类基础审核查询)
	 * create by ruyi.chen
	 * create date 2015-06-09
	 * @return
	 */
	public Page<Map<Object, Object>> getIslandCommonReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo,Long deptId,Long reviewCompanyId,Integer reviewType,Integer nowLevel,int deptLevel,Integer orderType){
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
		.append("select aa.*,a.orderNum,a.orderTime,a.groupCode,a.meter,a.saler,a.acitivityName,a.productId,a.island_order_uuid,IFNULL(rl.nowLevel, 0) ")
		.append("AS reviewLevel,IFNULL(rl.result, '未审核') AS result,su.name as beforeReviewName,rev.createBy,a.orderCompanyName,")
		.append("a.activity_island_group_uuid,a.activity_island_uuid from (SELECT r.id AS rid,r.orderId,r.productType as orderType,")
		.append(" r.createReason,r.createDate,r.updateDate, r.updateByName,r.topLevel,r.nowLevel,(r.status) as reviewStatus,r.printTime,r.printFlag,");
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
		.append(" )aa INNER JOIN (select s.* from(SELECT DISTINCT p.id AS orderId,p.order_no as orderNum,p.create_date as orderTime,p.uuid as island_order_uuid,")
		.append("ag.uuid as activity_island_group_uuid,ta.uuid as activity_island_uuid,")
		.append(" p.group_code as groupCode,ta.product_code as acitivityName,ta.createBy AS meter,ta.id as productId,p.create_by AS saler,ag.agentName as orderCompanyName from island_order p ")
		.append("INNER JOIN activity_island_group ag ON ag.uuid = p.activity_island_group_uuid INNER JOIN activity_island ta ON ta.uuid = p.activity_island_uuid ");
		// 添加页面条件查询控制(销售、计调、日期等)
		
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
		.append(")s where s.orderId is not null) a ON aa.orderId = a.orderId  ")
		.append(" LEFT JOIN review_log rl on aa.rid =rl.review_id and rl.nowLevel=? ")
		.append(" LEFT JOIN review_log rev on aa.rid=rev.review_id and rev.nowLevel=? ")
		.append(" LEFT JOIN sys_user su on rev.createBy=su.id ");
		ls.add(nowLevel);
		ls.add(nowLevel-1);
	    pageInfo= islandTravelerDao.findPageBySql(page,sbf.toString(), Map.class,ls.toArray());		
		List<Map<Object,Object>>list=pageInfo.getList();
		if(list.size()>0){
			for(Map<Object,Object> map:list){
				//String price=map.get("payPrice").toString();
				String result = map.get("result").toString();
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
				//判断前台返回针对于当前审核层级的审核状态
				if(StringUtils.isNotBlank(result)){
					switch(result){
						case "未审核":
							if(map.get("nowLevel").toString().equals(nowLevel.toString())){
								myStatus=1;
							}
							
							break;
						default:
							break;
					}
					map.put("myStatus", myStatus);
				}
			}
		}
		return pageInfo;
						
	}
	/**add by ruyi.chen
	 * update date 2015-06-09
	 * 不同审核状态  审核查询控制sql拼接(修改本例适合各种审核查询)
	 * 0：全部   1：待审核   2：未通过(驳回)  3:已通过     4:审核中  5:已取消
	 * 
	 */
	public String getNewReviewCheckSql(int reviewStatus,int userLevel){
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
	@Override
	public String saveBorrowing(HttpServletRequest request) {
		String orderId = request.getParameter("orderId");
		String orderUuid = request.getParameter("orderUuid");
		String reviewId = request.getParameter("reviewId");
//		String[] travelerIds = request.getParameterValues("travelerId");
		String travelerId="0";
		String orderType = request.getParameter("orderType");
		List<String> paramNamesList = new ArrayList<String>();
		paramNamesList.add("travelerId");
		paramNamesList.add("travelerName");
		paramNamesList.add("currencyId");
		paramNamesList.add("currencyName");
		paramNamesList.add("currencyMark");
		paramNamesList.add("currencyExchangerate");
		paramNamesList.add("payPrice");
		paramNamesList.add("lendPrice");
		paramNamesList.add("remark");
		paramNamesList.add("lendName");
		paramNamesList.add("borrowRemark");
		List<String> paramList = new ArrayList<String>();
		paramList.add("currencyIds");
		paramList.add("currencyNames");
		paramList.add("currencyMarks");
		paramList.add("borrowPrices");
		//paramList.add("currencyExchangerates");
				
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		if(null == islandOrder || islandOrder.getId().intValue() == 0){
			return "未找到订单！";
			
		}
		ActivityIsland activityIsland = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
		if(null == activityIsland || activityIsland.getId().intValue() == 0){
			return "未找到产品！";
		}
		
		if(null == activityIsland.getDeptId() || 0 >= activityIsland.getDeptId().intValue()){
			return "产品没有部门！";
		}
		
		List<Detail> listReview = BorrowingBean.exportDetail4Request(request, paramNamesList,paramList,"lendPrice");
		if(request.getParameter("borrowPrices") != null&& request.getParameter("currencyIds")!= null ){
			float  fc = currencyConverter(request.getParameter("borrowPrices"),request.getParameter("currencyIds"));
			Detail d = new Detail();
			d.setKey("currencyConverter");
			d.setValue(fc+"");
			listReview.add(d);
		}
		if(request.getParameter("currencyExchangerates") != null ){
			Detail d = new Detail();
			d.setKey("currencyExchangerates");
			d.setValue(request.getParameter("currencyExchangerates"));
			listReview.add(d);
		}
		if (StringUtils.isEmpty(reviewId)) {
			reviewId = "0";
		}
		StringBuffer reply = new StringBuffer();
		Long result=0L; //返回的是reviewId
		
		String[] travels =  listReview.get(0).getValue().split("#");
		if(travels!=null&&travels.length>0){
			if(travels.length>1){
				travelerId="0";
			}else if(travels.length==1 && ("0").equals(travels[0])){
				travelerId="0";
			}else{
				travelerId=travels[0];
			}
		}
		
//		if(travelerIds!=null&&travelerIds.length>0){
//			if(travelerIds.length>1 ){
//				travelerId="0";
//			}else{
//				travelerId=travelerIds[0];
//			}
//		}
		String borrowRemark = request.getParameter("borrowRemark");
		result = reviewService.addReview(Integer.parseInt(orderType), 19, orderId, Long.parseLong(travelerId), Long.parseLong(reviewId), borrowRemark, reply, listReview, activityIsland.getDeptId().longValue());
		
		//Map<String, Object> map = new HashMap<String, Object>();
		String msg="";
		if(result==0L){
			//map.put("error", reply.toString());
			msg=reply.toString();
		} else {
			//map.put("success", "success");
			msg="申请成功!";
		////////////////////////////////////////////////
			/**
			* add by ruyi.chen
			* add date 2015-06-14
			* describe 过滤海岛游直接通过审核
			*/
			Review r=reviewService.findReviewInfo(result);
			if(Context.ORDER_TYPE_ISLAND.toString().equals(r.getProductType().toString())){
				reviewService.UpdateReview(result, r.getTopLevel(), 1, "");
				boolean flag = false;
				try {
					 flag = airTicketOrderLendMoneyService.saveLendMoney2MoneyAmount(result, r.getOrderId(), r.getProductType());
				} catch (Exception e) {
					return "操作失败";
				}
				if(flag){
					return "操作成功";
				}else{
					return "操作失败";
				}
			}
			
			
			//////////////////////////////////////////////////
		}
		
		return msg;
	}
	/**
	 * 其他币种转换成人民币
	 */
	@ResponseBody
	public float currencyConverter(String count,String currencyId)
	{
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		String [] ct= count.split("#");
		String [] ci= currencyId.split("#");
		double totalMoney =0;
		for(int i=0;i<ct.length;i++)
		{
			if(StringUtils.isNotBlank(ct[i])){
				String ctStr = ct[i].replaceAll(",", "");
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=?");
				//buffer.append(ci[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = islandTravelerDao.findBySql(buffer.toString(), Map.class,ci[i]);
				Map<String, Object>  mp =  list.get(0);
				totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ctStr);
			}
			
		}
		 BigDecimal   b   =   new   BigDecimal(totalMoney); 
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
	}
	/**
	 * 根据海岛游订单uuid、产品类型查询符合借款条件游客信息
	 * @param uuid
	 * @param productType
	 * @author chenry
     * @DateTime 2015-06-14
	 */
	public List<Map<String, Object>> getBorrowingTravelerByOrderUuid(String orderUuid) {
		StringBuffer sbf=new StringBuffer();
		sbf
		.append("SELECT p.orderTime,p.uuid as orderUuid,tt.id,tt.uuid as travelerUuid,tt.name,tt.sex,tt.payPriceSerialNum,tt.personType,tt.delFlag,")
		.append("IFNULL(a.accounted_money,0) as payPrice from  island_traveler tt LEFT JOIN island_order p on tt.order_uuid= p.uuid ")
		.append(" LEFT JOIN(select t.id,")
		.append(" group_concat(FORMAT(IFNULL(ma.amount, 0),2),c.currency_name,'money' ORDER BY c.currency_id ) AS accounted_money")
		.append(" from island_traveler t LEFT  JOIN island_money_amount ma on t.payPriceSerialNum=ma.serialNum")
		.append(" and t.order_uuid=? and t.delFlag in(0,2,4) inner JOIN currency c on ma.currencyId= c.currency_id ")
		.append(" GROUP BY t.id)a on tt.id=a.id  WHERE tt.order_uuid =? and tt.delFlag in(0,2,4) order by tt.id asc");
		List<Map<String, Object>> ls=islandTravelerDao.findBySql(sbf.toString(), Map.class,orderUuid,orderUuid);
		for(Map<String, Object>m:ls){
			String payPrice=m.get("payPrice").toString();
			if(StringUtils.isNotBlank(payPrice)){
				m.put("payPrice", getMoneyStr(payPrice));
			}
			
		}
		return ls;
	}
	/**
	 * 根据海岛游订单uuid、产品类型查询符合借款条件游客信息
	 * @param uuid
	 * @param productType
	 * @author chenry
     * @DateTime 2015-06-19
	 */
	public List<Map<String, Object>> getHotelBorrowingTravelerByOrderUuid(String orderUuid) {
		StringBuffer sbf=new StringBuffer();
		sbf
		.append("SELECT p.orderTime,p.uuid as orderUuid,tt.id,tt.uuid as travelerUuid,tt.orderId,tt.name,tt.sex,tt.payPriceSerialNum,tt.personType,tt.delFlag,")
		.append("IFNULL(a.accounted_money,0) as payPrice from  hotel_traveler tt LEFT JOIN hotel_order p on tt.order_uuid= p.uuid ")
		.append(" LEFT JOIN(select t.id,")
		.append(" group_concat(FORMAT(IFNULL(ma.amount, 0),2),c.currency_name,'money' ORDER BY c.currency_id ) AS accounted_money")
		.append(" from hotel_traveler t LEFT  JOIN island_money_amount ma on t.payPriceSerialNum=ma.serialNum")
		.append(" and t.order_uuid=? and t.delFlag in(0,2,4) inner JOIN currency c on ma.currencyId= c.currency_id ")
		.append(" GROUP BY t.id)a on tt.id=a.id  WHERE tt.order_uuid =? and tt.delFlag in(0,2,4) order by tt.id asc");
		List<Map<String, Object>> ls=islandTravelerDao.findBySql(sbf.toString(), Map.class,orderUuid,orderUuid);
		for(Map<String, Object>m:ls){
			String payPrice=m.get("payPrice").toString();
			if(StringUtils.isNotBlank(payPrice)){
				m.put("payPrice", getMoneyStr(payPrice));
			}
			
		}
		return ls;
	}
	public  static String getMoneyStr(String moneyStr){
		moneyStr=moneyStr.replaceAll("-", "del");
		moneyStr=moneyStr.replaceAll("money,", "add");
		moneyStr=moneyStr.replaceAll("adddel", "-");
		moneyStr=moneyStr.replaceAll("add", "+");
		moneyStr=moneyStr.replaceAll("money", "");
		moneyStr=moneyStr.replaceAll("del", "-");
		return moneyStr;
	}
	@Override
	public List<IslandRebates> findRebatesList(Long orderId, Integer orderType) {
		
		return islandRebatesDao.findRebatesList(orderId, orderType);
	}
	@Override
	public List<IslandRebates> findRebatesByTravelerAndStatus(Long travelerId) {
		
		return islandRebatesDao.findRebatesByTravelerAndStatus(travelerId);
	}
	@Override
	public List<HotelRebates> findHotelRebatesList(Long orderId, Integer orderType) {
		
		return hotelRebatesDao.findHotelRebatesList(orderId, orderType);
	}
	@Override
	public List<HotelRebates> findHotelRebatesByTravelerAndStatus(Long travelerId) {
		
		return hotelRebatesDao.findHotelRebatesByTravelerAndStatus(travelerId);
	}
	@Override
	public String saveHotelBorrowing(HttpServletRequest request) {
		String orderId = request.getParameter("orderId");
		String orderUuid = request.getParameter("orderUuid");
		String reviewId = request.getParameter("reviewId");
//		String[] travelerIds = request.getParameterValues("travelerId");
		String travelerId="0";
		String orderType = request.getParameter("orderType");
		List<String> paramNamesList = new ArrayList<String>();
		paramNamesList.add("travelerId");
		paramNamesList.add("travelerName");
		paramNamesList.add("currencyId");
		paramNamesList.add("currencyName");
		paramNamesList.add("currencyMark");
		paramNamesList.add("currencyExchangerate");
		paramNamesList.add("payPrice");
		paramNamesList.add("lendPrice");
		paramNamesList.add("remark");
		paramNamesList.add("lendName");
		paramNamesList.add("borrowRemark");
		List<String> paramList = new ArrayList<String>();
		paramList.add("currencyIds");
		paramList.add("currencyNames");
		paramList.add("currencyMarks");
		paramList.add("borrowPrices");
		//paramList.add("currencyExchangerates");
				
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		if(null == hotelOrder || hotelOrder.getId().intValue() == 0){
			return "未找到订单！";
			
		}
		ActivityHotel activityHotel = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
		if(null == activityHotel || activityHotel.getId().intValue() == 0){
			return "未找到产品！";
		}
		
		if(null == activityHotel.getDeptId() || 0 >= activityHotel.getDeptId().intValue()){
			return "产品没有部门！";
		}
		
		List<Detail> listReview = BorrowingBean.exportDetail4Request(request, paramNamesList,paramList,"lendPrice");
		if(request.getParameter("borrowPrices") != null&& request.getParameter("currencyIds")!= null ){
			float  fc = currencyConverter(request.getParameter("borrowPrices"),request.getParameter("currencyIds"));
			Detail d = new Detail();
			d.setKey("currencyConverter");
			d.setValue(fc+"");
			listReview.add(d);
		}
		if(request.getParameter("currencyExchangerates") != null ){
			Detail d = new Detail();
			d.setKey("currencyExchangerates");
			d.setValue(request.getParameter("currencyExchangerates"));
			listReview.add(d);
		}
		if (StringUtils.isEmpty(reviewId)) {
			reviewId = "0";
		}
		StringBuffer reply = new StringBuffer();
		Long result=0L; //返回的是reviewId
		
		String[] travels =  listReview.get(0).getValue().split("#");
		if(travels!=null&&travels.length>0){
			if(travels.length>1){
				travelerId="0";
			}else if(travels.length==1 && ("0").equals(travels[0])){
				travelerId="0";
			}else{
				travelerId=travels[0];
			}
		}
		
//		if(travelerIds!=null&&travelerIds.length>0){
//			if(travelerIds.length>1 ){
//				travelerId="0";
//			}else{
//				travelerId=travelerIds[0];
//			}
//		}
		String borrowRemark = request.getParameter("borrowRemark");
		result = reviewService.addReview(Integer.parseInt(orderType), 19, orderId, Long.parseLong(travelerId), Long.parseLong(reviewId), borrowRemark, reply, listReview, activityHotel.getDeptId().longValue());
		
		//Map<String, Object> map = new HashMap<String, Object>();
		String msg="";
		if(result==0L){
			//map.put("error", reply.toString());
			msg=reply.toString();
		} else {
			//map.put("success", "success");
			msg="申请成功!";
		////////////////////////////////////////////////
			/**
			* add by ruyi.chen
			* add date 2015-06-14
			* describe 过滤酒店直接通过审核
			*/
			Review r=reviewService.findReviewInfo(result);
			if(Context.ORDER_TYPE_HOTEL.toString().equals(r.getProductType().toString())){
				reviewService.UpdateReview(result, r.getTopLevel(), 1, "");
				boolean flag = false;
				try {
					 flag = airTicketOrderLendMoneyService.saveLendMoney2MoneyAmount(result, r.getOrderId(), r.getProductType());
				} catch (Exception e) {
					return "操作失败";
				}
				if(flag){
					return "操作成功";
				}else{
					return "操作失败";
				}
			}
			
			
			//////////////////////////////////////////////////
		}
		
		return msg;
	}
	@Override
	public Map<String, Object> getIslandAllTypeReviewByOrderUuid(
			String orderUuid) {
		Map<String,Object> rMap = Maps.newHashMap();
		
		return rMap;
	}
}
