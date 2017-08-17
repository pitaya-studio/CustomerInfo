package com.trekiz.admin.review.changePrice.airticket.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.airticket.service.AirTicketUpPricesService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.changePrice.airticket.bean.AirticketChangePriceInput;
import com.trekiz.admin.review.changePrice.airticket.bean.ReviewResultBean;
import com.trekiz.admin.review.changePrice.airticket.service.NewAirticketChangePriceService;
import com.trekiz.admin.review.mutex.ReviewMutexService;

/**
 * 
 * 描述:改价
 * 
 * @author hhx 2015-11-16
 */
@Controller
@RequestMapping(value = "${adminPath}/changeprice/airticket")
public class NewAirticketChangePriceController extends BaseController{
	
	private static String AIRTICKET_CHANGE_PRICE_LIST = "review/changePrice/airticket/changePriceList" ;//机票改价申请记录页面
	private static String AIRTICKET_CHANGE_PRICE_FORM = "review/changePrice/airticket/changePriceForm" ;//机票改价申请页面
	private static String AIRTICKET_CHANGE_PRICE_DETAIL = "review/changePrice/airticket/changePriceDetail";//机票改价申请详情页面
	
	@Autowired
	private AirTicketUpPricesService airTicketUpPricesService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private CurrencyService currencyService ;
	@Autowired
	private AreaService areaService;
	@Autowired
	private ReviewMutexService reviewMutexService;
	@Autowired
	private NewAirticketChangePriceService newAirticketChangePriceService;
	
	
	/**
	 * 查询机票改价记录
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="changePriceList")
	public String changePriceList(AirticketChangePriceInput input,Model model,HttpServletResponse response,HttpServletRequest request){
		
		/**流程互斥，后期统一加，当前默认true，即：没有互斥*/
		//ReviewResultBean result = newAirticketChangePriceService.checkReview(input);
//		boolean flag = result.getSuccess();
		boolean flag=true;
		/**流程互斥，后期统一加，当前默认true，即：没有互斥*/
		
		List<Map<String, Object>> list = newAirticketChangePriceService.findReviewListMap(input);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", input.getOrderId());
		map.put("productType",input.getProductType());
		map.put("flowType", input.getFlowType());
		model.addAttribute("airticketReturnList", list);
		model.addAttribute("flag", flag);
		model.addAllAttributes(map);
		model.addAttribute("loginUser", UserUtils.getUser().getId().toString());
		return AIRTICKET_CHANGE_PRICE_LIST;
	}
	
	/**
	 * 跳转到改价详情页面(申请改价页面)
	 * @param model
	 * @param response
	 * @param request
	 * @return 
	 */
	@RequestMapping(value="changePriceForm")
	public String changePriceForm(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = airTicketUpPricesService.queryTravelerList(orderId);
		model.addAttribute("travelerList", travelerList);
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAllAttributes(map);
		model.addAttribute("orderDetailInfoMap", airTicketOrderService.queryAirticketOrderDetailInfoById(orderId));
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		
		return AIRTICKET_CHANGE_PRICE_FORM;
	}
	
	/**
	 * 查询单个改价信息(改价详情页面)
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="changePriceDetail")
	public String changePriceDetail(AirticketChangePriceInput input,Model model,HttpServletResponse response,HttpServletRequest request){
		
		Map<String, Object> result = newAirticketChangePriceService.getReviewDetailMapByReviewId(input);
		
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", input.getOrderId());
		map.put("productType",input.getProductType());
		map.put("flowType", input.getFlowType());
		model.addAllAttributes(map);
		model.addAttribute("airticketReturnReview", result);
		//=========订单详情开始
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(input.getOrderId());
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		//=========订单详情结束
		
		model.addAttribute("reviewId", input.getReviewId());
		model.addAttribute("rid", input.getReviewId());
		return AIRTICKET_CHANGE_PRICE_DETAIL;
	}
	
	/**
	 * 申请改价
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="applyForUpAirPrices")
	@ResponseBody
	public Object applyForUpAirTicketPrices(AirticketChangePriceInput input,Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String,String> rMap = new HashMap<String, String>();
		if(StringUtils.isBlank(input.getOrderId())||StringUtils.isBlank(input.getProductType())||StringUtils.isBlank(input.getFlowType())){
			rMap.put("sbinfo", "改价输入参数异常！");
		}else{
			try {
				ReviewResultBean result = newAirticketChangePriceService.changePriceProStart(input);
				rMap.put("sbinfo", result.getMessage().toString());
				rMap.put("orderId",input.getOrderId());
				rMap.put("productType",input.getProductType());
				rMap.put("flowType",input.getFlowType());
			} catch (Exception e) {
				e.printStackTrace();
				rMap.put("sbinfo", e.getMessage());
			}
		}
		return rMap;
	}
	/**
	 * 取消申请
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="cancelTravlerReturnRequest")
	public Object cancelTravlerReturnRequest(AirticketChangePriceInput input,Model model,HttpServletResponse response,HttpServletRequest request){
		ReviewResultBean result = newAirticketChangePriceService.cancel(input);
		
		return result;
	}
	
	
	/**
	 * 
	 * checkMutex  检查改价流程是否存在互斥
	 * @param model
	 * @param response
	 * @param request
	 * @exception
	 * @since  1.0.0
	 */
	@RequestMapping("checkMutex")
	@ResponseBody
	public Object checkMutex(AirticketChangePriceInput input,Model model,HttpServletResponse response,HttpServletRequest request){
		ReviewResultBean result = newAirticketChangePriceService.checkReview(input);
		
		return result;
	}
	
}
