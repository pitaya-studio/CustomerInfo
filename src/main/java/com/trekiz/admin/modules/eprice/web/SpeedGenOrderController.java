package com.trekiz.admin.modules.eprice.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.eprice.form.SpeedOrderInput;
import com.trekiz.admin.modules.eprice.service.SpeedGenOrderService;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupPriceService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupRoomService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerTypeRelationService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

@Controller
@RequestMapping(value = "${adminPath}/speedGenOrder")
public class SpeedGenOrderController {
	protected static final String GEN_ORDER_PAGE = "modules/eprice/speedGenOrder";
	@Autowired
	private SystemService systemService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	protected LogOperateService logOpeService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private ActivityHotelGroupPriceService activityHotelGroupPriceService;
	@Autowired
    private	ActivityHotelGroupRoomService activityHotelGroupRoomService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private SpeedGenOrderService speedGenOrderService;
	@Autowired
	private ActivityHotelGroupMealService activityHotelGroupMealService;
	@Autowired
	private HotelTravelerTypeRelationService hotelTravelerTypeRelationService;
	
	/**
	 * 快速生成订单页面
	 * @author star
	 * @param speedOrderInput
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "page")
	public ModelAndView speedGenOrderPage(@ModelAttribute SpeedOrderInput speedOrderInput,HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		//当前登录用户
		User user = UserUtils.getUser();
		mav.addObject("user",user);
		// 构造酒店团号下拉列表
		Long companyId = user.getCompany().getId();
		List<ActivityHotelGroup> activityHotelGroupList = speedGenOrderService.getListBySaleId(String.valueOf(companyId));
		mav.addObject("activityHotelGroupList", activityHotelGroupList);
		//加载渠道信息
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();
		mav.addObject("agentList", agentInfos);
		//签证类型
		List<SysDict> visaTypes = sysDictService.findByType(SysDict.TYPE_NEW_VISA_TYPE);
		mav.addObject("visaTypes", visaTypes);
		//加载币种信息
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		mav.addObject("currencyList", currencyList);
		//下单人列表
		List<User> userList = systemService.getUserByCompanyId(companyId);
		mav.addObject("userList", userList);
		//返回视图
		mav.setViewName(GEN_ORDER_PAGE);
		return mav;
	}

	/**
	 * 刷新页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "speedGenOrderRefreshPage")
	public ModelAndView speedGenOrderRefreshPage(@ModelAttribute SpeedOrderInput speedOrderInput,HttpServletRequest request,
			HttpServletResponse response) {
		//已有团号时返回视图
		ModelAndView mav = new ModelAndView();
		mav.setViewName(GEN_ORDER_PAGE);
		//selNo=1新建团号    selNo=2已有团号
		String selNo = request.getParameter("selNo");
		mav.addObject("selNo", selNo);
		//已有团号为空处理
		String groupUuid = request.getParameter("uuid");
		if(StringUtils.isBlank(groupUuid) || "0".equals(groupUuid)){
			return mav;
		}
		//当前登录用户即下单人
		User user = UserUtils.getUser();
		mav.addObject("user",user);
		Long companyId = user.getCompany().getId();
		// 构造酒店团号下拉列表
		List<ActivityHotelGroup> activityHotelGroupList = speedGenOrderService.getListBySaleId(String.valueOf(companyId));
		mav.addObject("activityHotelGroupList", activityHotelGroupList);
		//构造团期信息
		ActivityHotelGroup activityHotelGroup = activityHotelGroupService.getByUuid(groupUuid);
		mav.addObject("activityHotelGroup",activityHotelGroup);
		speedOrderInput.setGroupCode(activityHotelGroup.getGroupCode());
		//房型
		List<ActivityHotelGroupRoom> groupRoomList = activityHotelGroupRoomService.getRoomListByGroupUuid(groupUuid);
		mav.addObject("groupRoomList", groupRoomList);
		//餐型
		for(ActivityHotelGroupRoom room:groupRoomList){
			List<ActivityHotelGroupMeal> groupMeallist = activityHotelGroupMealService.getMealListByRoomUuid(room.getUuid());
			room.setActivityHotelGroupMealList(groupMeallist);
		}
		//构造产品信息
		ActivityHotel activityHotel = activityHotelService.getByUuid(activityHotelGroup.getActivityHotelUuid());
		mav.addObject("activityHotel", activityHotel);
		mav.addObject("hotelGroup", hotelService.getHotelGroupByUuid(activityHotel.getHotelUuid()==null?"":activityHotel.getHotelUuid()));
		//加载渠道信息
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();
		mav.addObject("agentList", agentInfos);
		//签证类型
		List<SysDict> visaTypes = sysDictService.findByType(SysDict.TYPE_NEW_VISA_TYPE);
		mav.addObject("visaTypes", visaTypes);
		//加载团期价格
		List<ActivityHotelGroupPrice> priceList = activityHotelGroupPriceService.getPriceFilterTravel(groupUuid, activityHotel.getHotelUuid());
		mav.addObject("priceList", priceList);
		//加载币种信息
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		mav.addObject("currencyList", currencyList);
		//查询批发商下的所用用户
		List<User> userList = systemService.getUserByCompanyId(companyId);
		mav.addObject("userList", userList);
		return mav;
	}
	 
	/**
	 * 保存订单信息
	 * @param speedOrderInput
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object saveOrder(SpeedOrderInput speedOrderInput,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		try{
	        //保存酒店订单信息
			result = speedGenOrderService.saveHotelOrder(speedOrderInput, request);
	        result.put("message", "1");
		} catch(Exception e){
			e.printStackTrace();
			result.put("message", "3");
			result.put("error", "系统异常，请重新操作!");
		}
		return result;
	}
	
	/**
	 * 查询酒店绑定的游客类型
	 * @param hotelUuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getTravelersByHotelUuid")
	public List<TravelerType> getTravelersByHotelUuid(String hotelUuid){
		return hotelTravelerTypeRelationService.getTravelerTypesByHotelUuid(hotelUuid);
	}
}
