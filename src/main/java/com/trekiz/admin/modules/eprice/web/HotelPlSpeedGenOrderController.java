package com.trekiz.admin.modules.eprice.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.eprice.form.SpeedOrderInput;
import com.trekiz.admin.modules.eprice.service.SpeedGenOrderService;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandway;
import com.trekiz.admin.modules.hotelPl.module.bean.GuestPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceQuery;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceRoomQuery;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteCondition;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResult;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResultDetail;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteConditionService;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteResultDetailService;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteResultService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/hotelPlSpeedGenOrder")
public class HotelPlSpeedGenOrderController {
	protected static final String GEN_ORDER_PAGE = "modules/eprice/hotelPlSpeedGenOrder";
	@Autowired
	private SystemService systemService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	protected LogOperateService logOpeService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private SpeedGenOrderService speedGenOrderService;
	@Autowired
	private HotelQuoteResultService hotelQuoteResultService;
	@Autowired
	private HotelQuoteConditionService hotelQuoteConditionService;
	@Autowired
	private HotelQuoteResultDetailService hotelQuoteResultDetailService;
	
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
		User user = UserUtils.getUser();
		mav.addObject("user",user);
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		//加载报价信息
		String uuid = request.getParameter("uuid");
		String json = request.getParameter("guestPriceJson");
		if(StringUtils.isNotBlank(uuid)){
			//报价列表跳转
			HotelQuoteCondition condition = hotelQuoteConditionService.getByUuid(uuid);
			if(condition!=null){
				speedOrderInput.setCountry(condition.getCountry());
				speedOrderInput.setIsland(condition.getIslandUuid());
				speedOrderInput.setHotel(condition.getHotelUuid());
				if(StringUtils.isNotBlank(condition.getHotelUuid())){
					mav.addObject("hotelGroup", hotelService.getHotelGroupByUuid(condition.getHotelUuid()));
				}
			}
			
			List<HotelQuoteResult> resultList = hotelQuoteResultService.findByHotelQuoteConditionUuid(uuid,condition.getHotelUuid());
			mav.addObject("travellerList", resultList);
			
			List<HotelQuoteResultDetail> hotelQuoteResultDetail = hotelQuoteResultDetailService.findByHotelQuoteConditionUuid(condition.getUuid());
			List<QuotedPriceRoomQuery> quotedPriceRoomList = new ArrayList<QuotedPriceRoomQuery>();
			Map<String,String> map = new TreeMap<String,String>(); 
			for(HotelQuoteResultDetail detail:hotelQuoteResultDetail){
				String keyWord = detail.getInDateString()+detail.getHotelRoomUuid()+detail.getHotelMealUuid();
				if(detail.getPriceType()!=1 || map.containsKey(keyWord)){
					continue;
				}
				map.put(keyWord, keyWord);
				QuotedPriceRoomQuery que = new QuotedPriceRoomQuery();
				que.setHotelRoomUuid(detail.getHotelRoomUuid());
				que.setNights(1);
				que.setHotelMealUuid(detail.getHotelMealUuid());
				quotedPriceRoomList.add(que);
			}
			mav.addObject("groupRoomList", quotedPriceRoomList);
			mav.addObject("groupMealList", quotedPriceRoomList);
			
			List<HotelPlIslandway> islandWayList = new ArrayList<HotelPlIslandway>();
			HotelPlIslandway way1 = new HotelPlIslandway();
			way1.setIslandWay(condition.getDepartureIslandWay());
			if(condition.getDepartureIslandWay().equals(condition.getArrivalIslandWay())){
				speedOrderInput.setIslandWay(condition.getDepartureIslandWay());
				islandWayList.add(way1);
			}else{
				speedOrderInput.setIslandWay(condition.getDepartureIslandWay()+";"+condition.getArrivalIslandWay());
				islandWayList.add(way1);
				HotelPlIslandway way2 = new HotelPlIslandway();
				way2.setIslandWay(condition.getArrivalIslandWay());
				islandWayList.add(way2);
			}
			mav.addObject("islandWayList", islandWayList);
			
			mav.addObject("typeFrom", "2");
			mav.addObject("typeFromTravel", "1");
		}else if(StringUtils.isNotBlank(json)){
			//自动报价跳转
			@SuppressWarnings("unchecked")
			List<GuestPriceJsonBean> guestPricelist = (List<GuestPriceJsonBean>) JSON.parse(json);
			mav.addObject("guestPricelist", guestPricelist);
			
			QuotedPriceQuery quote = JSON.parseObject(request.getParameter("quotedPriceQueryJsonStr"),QuotedPriceQuery.class);
			speedOrderInput.setCountry(quote.getCountry());
			speedOrderInput.setIsland(quote.getIslandUuid());
			speedOrderInput.setHotel(quote.getHotelUuid());
			mav.addObject("hotelGroup", hotelService.getHotelGroupByUuid(quote.getHotelUuid()==null?"":quote.getHotelUuid()));
			
			//上岛方式    过滤重复数据
			List<HotelPlIslandway> islandWayList = new ArrayList<HotelPlIslandway>();
			HotelPlIslandway way1 = new HotelPlIslandway();
			way1.setIslandWay(quote.getDepartureIslandWay());
			HotelPlIslandway way2 = new HotelPlIslandway();
			way2.setIslandWay(quote.getArrivalIslandWay());
			islandWayList.add(way1);
			islandWayList.add(way2);
			speedOrderInput.setIslandWay(quote.getDepartureIslandWay()+";"+quote.getArrivalIslandWay());
			mav.addObject("islandWayList", islandWayList);
			//房型    过滤重复数据
			List<QuotedPriceRoomQuery> quotedPriceRoomList = quote.getQuotedPriceRoomList();
			mav.addObject("groupRoomList", quotedPriceRoomList);
			mav.addObject("groupMealList", quotedPriceRoomList);
			mav.addObject("typeFrom", "2");
		}
		//加载渠道信息
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();
		mav.addObject("agentList", agentInfos);
		//签证类型
		List<SysDict> visaTypes = sysDictService.findByType(SysDict.TYPE_NEW_VISA_TYPE);
		mav.addObject("visaTypes", visaTypes);
		//加载币种信息
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		mav.addObject("currencyList", currencyList);
		//保存一个人名币id
		Currency currency = currencyService.getRMBCurrencyId();
		mav.addObject("currencyRMB",currency.getId());
		//查询批发商下的所用用户
		List<User> userList = systemService.getUserByCompanyId(companyId);
		mav.addObject("userList", userList);
		mav.setViewName(GEN_ORDER_PAGE);
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
}
