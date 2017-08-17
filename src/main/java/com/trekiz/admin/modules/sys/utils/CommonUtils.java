package com.trekiz.admin.modules.sys.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.hotel.dao.HotelStarDao;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;

public class CommonUtils {
	
	private static ReviewService reviewService = SpringContextHolder.getBean(ReviewService.class);
	
	private static  ProductOrderCommonDao productorderDao = SpringContextHolder.getBean(ProductOrderCommonDao.class);
	private static  HotelStarDao hotelStarDao = SpringContextHolder.getBean(HotelStarDao.class);
	private static ReviewCommonService reviewCommonService = SpringContextHolder.getBean(ReviewCommonService.class);
	private static DepartmentDao departmentDao = SpringContextHolder.getBean(DepartmentDao.class);
	private static IAirticketOrderDao airticketOrderDao = SpringContextHolder.getBean(IAirticketOrderDao.class);
	public static List<String> visaType = new LinkedList<String>();
	private static AgentinfoService agentinfoService = SpringContextHolder.getBean(AgentinfoService.class);
    private static CurrencyService currencyService = SpringContextHolder.getBean(CurrencyService.class);
	static{
		visaType.add("旅游签证");
		visaType.add("探亲签证");
	}
	
	/**
	 * add by ruyi.chen 
	 * add date 2015-01-05
	 * 获取退款审核详情
	 * @param rid
	 * @return
	 */
	public static Map<String,Object>getReviewRefundInfo(String ridStr){
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		
		Long rid = 0L;
		if(StringUtils.isNotBlank(ridStr)) {
			rid = Long.parseLong(ridStr);
		}
		
		Map<String,String> map=reviewService.findReview(rid);
		if(null!=map&&map.size()>0){
			resultMap.put("reviewInfo",map);
			String travelerId=map.get("travelerId");
			if(null!=travelerId&&Long.parseLong(travelerId)>0){
				String checksql="("+travelerId+")";
				List<Map<Object,Object>> m=getTravelerInfoForReview(checksql);
				Map<Object,Object>mapResult=m.get(0);
				String payPrice=mapResult.get("payPrice").toString();
				mapResult.put("payPrice", getMoneyStr(payPrice));
				resultMap.put("travelerInfo",mapResult);
			}else{
				resultMap.put("travelerInfo", null);
			}
			List<ReviewLog> rLog=reviewService.findReviewLog(rid);
			resultMap.put("reviewLogInfo", rLog);
		}
		return resultMap;
		
	}
	/**
	 * add by chy
	 * 2015-7-7 11:00:42
	 * 获取某流程某产品的审核数量
	 * @param Integer flowType, Integer prdType
	 * @return
	 */
	public static String getReviewNum(Integer flowType, Integer prdType){
		
		if(flowType == null || prdType == null){
			return "";
		}
		//声明返回结果
		Integer prdReviewNum = 0;//目前产品对应的审核条数
		List<Integer> flowTypes = new ArrayList<Integer>();
		if(1 == flowType){
			flowTypes.add(Context.REVIEW_FLOWTYPE_REFUND);
			flowTypes.add(Context.REVIEW_FLOWTYPE_OPER_REFUND);
		}else{
			flowTypes.add(flowType);
		}
		//查询用户的职位
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(flowTypes);
		if(userJobs == null || userJobs.size() == 0){
			return "";
		}
		//根据产品 过滤出对应职位 根据职位对应的level 查询审核记录的数量
		for(UserJob temp : userJobs){
			if(temp.getOrderType() == prdType){
				prdReviewNum += temp.getCount();
			}
		}
		//返回
		if(prdReviewNum == 0){
			return "";
		} else {
			return "(" + prdReviewNum.toString() + ")";
		}
	}
	public static List<Map<Object,Object>>getTravelerInfoForReview(String checksql){
		StringBuffer sbf=new StringBuffer();
		sbf
		.append("SELECT p.orderTime, tt.id as travelerId,tt.orderId,tt.name,tt.payPriceSerialNum,tt.personType,")
		.append("IFNULL(a.accounted_money,0) as payPrice from  traveler tt LEFT JOIN productorder p on tt.orderId= p.id ")
		.append(" LEFT JOIN(select t.id,t.orderId,t.payPriceSerialNum,group_concat(format(IFNULL(ma.amount, 0),2),c.currency_name,'money'")
		.append(" ORDER BY c.currency_id) AS accounted_money from traveler t LEFT JOIN money_amount ma on t.payPriceSerialNum=ma.serialNum ")
		.append(" and t.id in "+checksql+" inner JOIN currency c on ma.currencyId= c.currency_id ")
		.append("GROUP BY t.id)a on tt.id=a.id  WHERE tt.id in "+checksql);
		return productorderDao.findBySql(sbf.toString(), Map.class);
	}
	
	public  static String getMoneyStr(String moneyStr){
		moneyStr=moneyStr.replaceAll("-", "del");
		moneyStr=moneyStr.replaceAll("money,", "add");
		moneyStr=moneyStr.replaceAll("adddel", "-");
		moneyStr=moneyStr.replaceAll("add", "+");
		moneyStr=moneyStr.replaceAll("money", "");
		return moneyStr;
	}
	
	public static String getValueByStar(String star){
		String sql = "SELECT hs.value FROM hotel_star hs,hotel h where hs.uuid = h.star and h.delFlag = '0' and hs.delFlag = '0'"
				+ " and h.star = ?";
		List<?> list = hotelStarDao.createSqlQuery(sql, star).list();
		if(list != null && list.size() > 0) {
			String s = list.get(0).toString();
			return s;
		}
		return "0";
		
	}
	
	public static String getAirlineByGroupUuid(String uuid){
		
		String sql = "SELECT info.airline_name FROM activity_island_group_airline air join sys_airline_info info on air.airline=info.airline_code where air.activity_island_group_uuid=? and air.delFlag = '0'";
		List<?> list =	hotelStarDao.createSqlQuery(sql, uuid).list();
		if(list != null && list.size() > 0) {
			String s = list.get(0).toString();
			return s;
		}
		return "0";
		
	}
	//海岛团期的机票余位
    public static String getAirRemNumber(String uuid){
		
		String sql = "SELECT remNumber FROM activity_island_group_airline air where air.activity_island_group_uuid=? and air.delFlag = '0'";
		List<?> list =	hotelStarDao.createSqlQuery(sql, uuid).list();
		if(list != null && list.size() > 0) {
			String s = list.get(0).toString();
			return s;
		}
		return "0";		
	}
    
	public static String  getIslandRoom(String groupUuid){
		String sql="select room.room_name as roomName, g.nights from  activity_island_group_room g join hotel_room room on g.hotel_room_uuid=room.uuid and g.activity_island_group_uuid='"+groupUuid+"' and g.delFlag='0'";
		List<Map<Object,Object>> list= hotelStarDao.findBySql(sql, Map.class);
		String roomStr="";
		for(int i = 0 ; i< list.size();i++){  
			if(i==0) roomStr=list.get(0).get("roomName").toString() +" x"+list.get(0).get("nights").toString();
			else roomStr += ","+list.get(0).get("roomName").toString() +" x"+list.get(0).get("nights").toString();
		}
		return roomStr;
	}
    
	public static String  getHotelRoom(String groupUuid){
		String sql="select room.room_name as roomName, g.nights from  activity_hotel_group_room g join hotel_room room on g.hotel_room_uuid=room.uuid and g.activity_hotel_group_uuid='"+groupUuid+"' and g.delFlag='0'";
		List<Map<Object,Object>> list= hotelStarDao.findBySql(sql, Map.class);
		String roomStr="";
		for(int i = 0 ; i< list.size();i++){  
			if(i==0) roomStr=list.get(0).get("roomName").toString() +" x"+list.get(0).get("nights").toString();
			else roomStr += ","+list.get(0).get("roomName").toString() +" x"+list.get(0).get("nights").toString();
		}
		return roomStr;
	}
	
	public static String getDeptNameById(Long id){
		String deptName="";
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(id != null){
			Department department = departmentDao.getDeptNameById(id, companyId);
			if(department != null){
				deptName = department.getName();
			}
		}
		return deptName;
	}

	/**
	 * 根据团期id查询该团期下所有的已确认占位的人数
     */
	public static Integer getConfirmedNums(Long activityGroupId) {
		int confirmedNums = 0;
		if(activityGroupId != null) {
			List<ProductOrderCommon> productOrderCommons = productorderDao.findByActivityGroupId(activityGroupId);
			if(productOrderCommons != null && productOrderCommons.size() > 0) {
				for(ProductOrderCommon productOrderCommon : productOrderCommons) {
					Integer seizedConfirmationStatus = productOrderCommon.getSeizedConfirmationStatus();
					Integer payStatus = productOrderCommon.getPayStatus();
					if(seizedConfirmationStatus != null && seizedConfirmationStatus == 1 && payStatus != 99) {
						confirmedNums += productOrderCommon.getOrderPersonNum();
					}
				}
			}
		}

		return confirmedNums;
	}

	/**
	 * 根据产品id查询该产品下的所有机票订单
     */
	public static Integer getAirticketConfirmedNums(Long activityAirticketId) {
		int confirmedNums = 0;
		if(activityAirticketId != null) {
			List<AirticketOrder> airticketOrders= airticketOrderDao.findByAirticketActivityCode(activityAirticketId);
			if(airticketOrders != null && airticketOrders.size() > 0) {
				for(AirticketOrder airticketOrder : airticketOrders) {
					Integer seizedConfirmationStatus = airticketOrder.getSeizedConfirmationStatus();
					Integer orderState = airticketOrder.getOrderState();
					if(seizedConfirmationStatus != null && seizedConfirmationStatus == 1 && orderState != 99) {
						confirmedNums += airticketOrder.getPersonNum();
					}
				}
			}
		}

		return confirmedNums;
	}
	
	/**
	 * @Description 判断是否是新审核
	 * @author yakun.bai
	 * @Date 2016-4-5
	 */
	public static boolean isNewReview(Date date) throws ParseException {
		boolean flag = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date onlineDate = dateFormat.parse("2015-12-31 00:00:00");
		if (date != null && date.after(onlineDate)) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * @Description 判断是否是新审核
	 * @author yakun.bai
	 * @Date 2016-4-5
	 */
	public static boolean isNewReview(String date) throws ParseException {
		boolean flag = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date onlineDate = dateFormat.parse("2015-12-31 00:00:00");
		if (date != null && dateFormat.parse(date).after(onlineDate)) {
			flag = true;
		}
		return flag;
	}
	
	public static List<Object[]> findAllPaymentType(){
		return  agentinfoService.findAllPaymentType();
	}

	public static String getOrderTypeName(Object orderType) {

		String type = "";
		switch (orderType.toString()) {
			case Context.ORDER_STATUS_SINGLE:
				type = "单团"; break;
			case Context.ORDER_STATUS_LOOSE:
				type = "散拼"; break;
			case Context.ORDER_STATUS_STUDY:
				type = "游学"; break;
			case Context.ORDER_STATUS_BIG_CUSTOMER:
				type = "大客户"; break;
			case Context.ORDER_STATUS_FREE:
				type = "自由行"; break;
			case Context.ORDER_STATUS_VISA:
				type = "签证"; break;
			case Context.ORDER_STATUS_AIR_TICKET:
				type = "机票"; break;
			case Context.ORDER_STATUS_CRUISE:
				type = "游轮"; break;
			case Context.ORDER_STATUS_HOTEL:
				type = "酒店"; break;
			case Context.ORDER_STATUS_ISLAND:
				type = "海岛游"; break;
			default:
				break;
		}

		return type;
	}
	
	public static Integer getOrderTypeByName(String orderTypeName) {

		Integer type = 0;
		switch (orderTypeName) {
			case "单团":
				type = Integer.parseInt(Context.ORDER_STATUS_SINGLE); break;
			case "散拼":
				type = Integer.parseInt(Context.ORDER_STATUS_LOOSE); break;
			case "游学":
				type = Integer.parseInt(Context.ORDER_STATUS_STUDY); break;
			case "大客户":
				type = Integer.parseInt(Context.ORDER_STATUS_BIG_CUSTOMER); break;
			case "自由行":
				type = Integer.parseInt(Context.ORDER_STATUS_FREE); break;
			case "签证":
				type = Integer.parseInt(Context.ORDER_STATUS_VISA); break;
			case "机票":
				type = Integer.parseInt(Context.ORDER_STATUS_AIR_TICKET); break;
			case "游轮":
				type = Integer.parseInt(Context.ORDER_STATUS_CRUISE); break;
			case "酒店":
				type = Integer.parseInt(Context.ORDER_STATUS_HOTEL); break;
			case "海岛游":
				type = Integer.parseInt(Context.ORDER_STATUS_ISLAND); break;
			default:
				break;
		}

		return type;
	}
}
