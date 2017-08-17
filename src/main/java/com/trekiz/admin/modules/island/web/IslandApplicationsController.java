package com.trekiz.admin.modules.island.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.model.HotelControlDetailModel;
import com.trekiz.admin.modules.hotel.service.HotelControlService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupAirlineQuery;
import com.trekiz.admin.modules.island.query.IslandTravelerQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupChangeService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupPriceService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderControlDetailService;
import com.trekiz.admin.modules.island.service.IslandOrderPriceService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.island.service.IslandTravelerService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;

/**
 * 海岛游转团相关
 * @author gao
 *  2015年6月11日
 */
@Controller
@RequestMapping(value = "${adminPath}/islandapplications")
public class IslandApplicationsController extends BaseController {
	
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private IslandTravelerService islandTravelerService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private IslandService islandService;
	@Autowired
	private IslandOrderPriceService islandOrderPriceService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private ActivityIslandGroupPriceService activityIslandGroupPriceService;
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ActivityIslandGroupChangeService activityIslandGroupChangeService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private HotelControlService hotelControlService;
	@Autowired
	private IslandOrderControlDetailService islandOrderControlDetailService;
	/**
	 * 跳转到转团记录
	 * @author gao
	 * @param uuid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "goToIslandOrderList/{uuid}")
	public String goToIslandOrderList(@PathVariable String uuid,HttpServletRequest request, HttpServletResponse response, Model model){
		IslandOrder order = new IslandOrder();
		ActivityIsland activityIsland = new ActivityIsland();
		if(StringUtils.isNotBlank(uuid)){
			// 海岛游转团申请记录
			order = islandOrderService.getByUuid(uuid);
			activityIsland = activityIslandService.getByUuid(order.getActivityIslandUuid());
			List<Map<String, String>> reviewList= new ArrayList<Map<String, String>>();
			reviewList = reviewService.findReviewListMap(Context.ProductType.PRODUCT_ISLAND, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, order.getId().toString(), true,Long.valueOf(activityIsland.getDeptId()));
		
			model.addAttribute("reviewList", reviewList); // 审核记录
			model.addAttribute("order", order); // 订单
		}
		return "modules/island/islandChangeGroup/list";
	}
	
	/**
	 * 跳转到转团详情
	 * @author gao
	 * @param uuid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "goToIslandOrderInfo/{orderUuid}/{travelUuid}")
	public String goToIslandOrderInfo(@PathVariable String orderUuid,@PathVariable String travelUuid,HttpServletRequest request, HttpServletResponse response, Model model){
		IslandOrder order = new IslandOrder();
		ActivityIsland activityIsland = new ActivityIsland();
		if(StringUtils.isNotBlank(orderUuid)){
			order = islandOrderService.getByUuid(orderUuid);
			if(order!=null){
				// 订单基本信息
				islandOrderService.getOrderBaseInfo(orderUuid, model);
				// 海岛游转团申请记录
				List<Map<String, String>> reviewList= new ArrayList<Map<String, String>>();
				activityIsland = activityIslandService.getByUuid(order.getActivityIslandUuid());
				reviewList = reviewService.findReviewListMap(Context.ProductType.PRODUCT_ISLAND, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, order.getId().toString(), true,Long.valueOf(activityIsland.getDeptId()));
				
				List<Map<String, String>> reviewbackList= new ArrayList<Map<String, String>>();
				for(Map<String, String> map : reviewList){
					// 判断是否有指定travelUuid的游客，并将其放入返回的map中。
					if(map.get("KEY_TRAVELERUUID").equals(travelUuid)){
						reviewbackList.add(map);
					}
				}
				model.addAttribute("reviewList", reviewbackList); // 审核记录,只记录指定uuid的游客审核信息
				model.addAttribute("order", order); // 订单
				model.addAttribute("travelUuid", travelUuid); // 指定游客UUID
			}
		}
		return "modules/island/islandChangeGroup/info";
	}
	
	/**
	 * 跳转到转团操作
	 * @author gao
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "goToIslandOrderUuid/{uuid}")
	public String goToIslandOrderUuid(@PathVariable String uuid,HttpServletRequest request, HttpServletResponse response, Model model){
		
		IslandOrder order = new IslandOrder();
		ActivityIsland activityIsland = new ActivityIsland();
		List<IslandTraveler> travelList = new ArrayList<IslandTraveler>();
		if(StringUtils.isNotBlank(uuid)){
			order = islandOrderService.getByUuid(uuid);
			if(order!=null){
				// 订单基本信息
				islandOrderService.getOrderBaseInfo(uuid, model);
				IslandTravelerQuery query = new IslandTravelerQuery();
				query.setOrderUuid(order.getUuid());// 载入订单uuid
				query.setStatus("0"); // 游客状态为正常
				query.setDelFlag("0");
				activityIsland = activityIslandService.getByUuid(order.getActivityIslandUuid());
				travelList = islandTravelerService.find(query);
//				//酒店控房详情数据 
//				IslandOrderControlDetailQuery detailquery = new IslandOrderControlDetailQuery();
//				detailquery.setOrderUuid(order.getUuid());
//				List<IslandOrderControlDetail> islandOrderControlDetailList = islandOrderControlDetailService.find(detailquery);
				//酒店控房详情数据
				List<HotelControlDetailModel> hotelControlDetailList = hotelControlService.getControlDetailsByHotelUuid(activityIsland.getHotelUuid());
				
				// 海岛游转团申请记录
				List<Map<String, String>> reviewList= new ArrayList<Map<String, String>>();
				//reviewList = reviewService.findReview(Context.ProductType.PRODUCT_ISLAND, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, order.getId().toString(), true);
				reviewList = reviewService.findReviewListMap(Context.ProductType.PRODUCT_ISLAND, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, order.getId().toString(), true,Long.valueOf(activityIsland.getDeptId()));
				model.addAttribute("travelList", travelList); // 	游客列表
				model.addAttribute("reviewList", reviewList); // 审核记录
				model.addAttribute("order", order); // 订单
				model.addAttribute("hotelControlDetailList", hotelControlDetailList); // 酒店控房详情数据
			}
		}
		return "modules/island/islandChangeGroup/applications";
	}
	
	/**
	 * 提交海岛游转团审核
	 * @author gao
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="addIsLandReview", method=RequestMethod.POST)
	public Map<String,String> addIsLandReview(HttpServletRequest request, HttpServletResponse response){
		String groupCode = request.getParameter("groupCode"); // 获取输入的团期号
		String orderUuid = request.getParameter("orderUuid"); //获取原订单uuid
		String[] travelUuids = request.getParameterValues("travelUuid"); // 获取需转团的游客uuid组
		Integer number = (travelUuids!=null)?travelUuids.length:0; //获取需转团游客的数量
		String[] remarks = request.getParameterValues("remark"); // 获取转团理由
		
		
		String newRoom = request.getParameter("newRoom");// 新团预报名间数
		String newTicket = request.getParameter("newTicket"); // 新团预报名票数
		String oldRoomControl =request.getParameter("oldRoomControl"); // 原订单控房数量
		String oldRoomNoControl = request.getParameter("oldRoomNoControl"); // 原订单非控房数量
		String oldTicketControl = request.getParameter("oldTicketControl"); // 原订单控票数量
		String oldTicketNoControl = request.getParameter("oldTicketNoControl"); // 原订单非控票数量
		
		Map<String,String> map = new HashMap<String,String>();
		List<ActivityIslandGroup> groupList = activityIslandGroupService.find(groupCode);
		// 获取原订单实体
		IslandOrder oldOrder = islandOrderService.getByUuid(orderUuid); 
		// 获取原产品实体
		ActivityIsland island = activityIslandService.getByUuid(oldOrder.getActivityIslandUuid());
		// 需转团的游客数组
		List<IslandTraveler> travelList = new ArrayList<IslandTraveler>();
		// add by WangXK 添加空指针的判断
		if(travelUuids!=null && travelUuids.length > 0){
			for(String uuid : travelUuids){
				IslandTraveler tra = islandTravelerService.getByUuid(uuid);
				// 游客不为null，且状态为“正常”
				if(tra!=null && "0".equals(tra.getStatus())){
					travelList.add(tra);
				}
			}
		}
		
		if(travelList==null || travelList.size()<1){
			map.put("res", "error");
			map.put("mes","请检查游客信息");
			return map;
		}
		
		// 判断团期是否存在
		if(groupList!=null && !groupList.isEmpty()){
			ActivityIslandGroup group = groupList.get(0);
			ActivityIslandGroupAirlineQuery query = new ActivityIslandGroupAirlineQuery();
			query.setActivityIslandGroupUuid(group.getUuid());
			query.setActivityIslandUuid(group.getActivityIslandUuid());
			List<ActivityIslandGroupAirline> airLineList = activityIslandGroupAirlineService.find(query);
			// 判断是否还有余位
			if(airLineList!=null && !airLineList.isEmpty()){
				int remNumber = 0;
				Iterator<ActivityIslandGroupAirline> iter = airLineList.iterator();
				while(iter.hasNext()){
					ActivityIslandGroupAirline line = iter.next();
					remNumber+=line.getRemNumber();
				}
				// 判断余位是否充足,并且查询到的游客数量和前台传入游客UUID数量一致
				if(remNumber>=number){
					StringBuffer reply = new StringBuffer();
					try{
						map = activityIslandGroupChangeService.addReviewList(travelList, oldOrder, remarks, 
								island, groupCode,   newRoom, newTicket, oldRoomControl, oldRoomNoControl,
								 oldTicketControl, oldTicketNoControl, reply,map);
					}catch(Exception e){
						map.put("res", "error");
						map.put("mes",e.getMessage());
					}
				}else{
					map.put("res", "error");
					map.put("mes", "余位不足");
				}
				
			}else{
				map.put("res", "error");
				map.put("mes", "余位为0");
			}
		}else{
			map.put("res", "error");
			map.put("mes", "该团期不存在");
		}
		return map;
	}
	
	/**
	 * 根据输入的团期号，查询相关团期
	 * @author gao
	 * @param groupCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getAjaxGroupCodeList", method=RequestMethod.GET)
	public Map<String,Object> getAjaxGroupCodeList(HttpServletRequest request, HttpServletResponse response){
		String groupCode = request.getParameter("groupCode"); // 获取输入的团期号
		Map<String,Object> map = new HashMap<String,Object>();
		List<ActivityIslandGroup> groupList = activityIslandGroupService.find(groupCode);
		if(groupList!=null && !groupList.isEmpty()){
			map.put("groupList", groupList);
			// 获取产品
			ActivityIsland activityIsland = activityIslandService.getByUuid(groupList.get(0).getActivityIslandUuid());
			// 获取余位
			ActivityIslandGroupAirlineQuery query = new ActivityIslandGroupAirlineQuery();
			query.setActivityIslandGroupUuid(groupList.get(0).getUuid());
			query.setActivityIslandUuid(groupList.get(0).getActivityIslandUuid());

			List<ActivityIslandGroupAirline> airLineList = activityIslandGroupAirlineService.find(query);
			if(airLineList!=null && !airLineList.isEmpty()){
				int remNumber = 0;
				Iterator<ActivityIslandGroupAirline> iter = airLineList.iterator();
				while(iter.hasNext()){
					ActivityIslandGroupAirline line = iter.next();
					remNumber+=line.getRemNumber();
				}
				map.put("res", "success");
				map.put("activityIsland", activityIsland);
				map.put("remNumber", remNumber);
				return map;
			}
		}
		map.put("res", "error");
		return map;
	}
	
}
