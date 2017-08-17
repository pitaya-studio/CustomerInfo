package com.trekiz.admin.review.payment.airticket.web;


import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.entity.ResponseJson;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.*;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.common.bean.CostPaymentReviewNewLog;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.payment.airticket.service.IAirticketPaymentReviewService;
import com.trekiz.admin.review.payment.comment.exception.PaymentException;
import com.trekiz.admin.review.payment.comment.utils.PayMentUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 机票产品付款审核的Controller
 * @author shijun.liu
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/review/airticket/payment")
public class AirticketPaymentReviewController {

	private static final Logger log = Logger.getLogger(AirticketPaymentReviewController.class);

	@Autowired
	private IAirticketPaymentReviewService airticketPaymentReviewService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private AreaService areaService;
	@Autowired
	AirlineInfoService airlineInfoService;
	@Autowired
	AirportInfoService airportInfoService; 
	@Autowired
	private AirportService airportService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	IAirTicketOrderService  iAirTicketOrderService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private ICommonReviewService commonReviewService;
	
	/**
	 * 机票产品付款申请，包括批量申请
	 * 		request获取的参数为items，items的值为：productId,orderType,costrecorduuid&productId,orderType,costrecorduuid
	 * @param request		request对象
	 * @param response		response对象
	 * @param model			模型对象
	 * @return
	 * @author shijun.liu
	 */
	@ResponseBody
	@RequestMapping(value="apply")
	public String apply(HttpServletRequest request, HttpServletResponse response,Model model){
		ResponseJson json = new ResponseJson();
		json.setFlag(true);//默认是正常执行，无异常
		StringBuffer str = new StringBuffer();
		String items = request.getParameter("items");
		if(StringUtils.isBlank(items)){
			json.setFlag(false);
			log.error("参数不能为空");
			json.setMsg("items参数不能为空");
			return JSONObject.toJSONString(json);
		}
		String[] itemArray = items.split(",");
		for (String item:itemArray) {
			try {
				airticketPaymentReviewService.apply(item);
			} catch (PaymentException e) {
				e.printStackTrace();
				str.append(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				str.append(e.getMessage());
			}
		}
		if(!"".equals(str.toString())){
			json.setFlag(false);
			log.error(str.toString());
			json.setMsg(str.toString());
		}
		return JSONObject.toJSONString(json);
	}
	
	/**
	 * 机票产品，审核列表，操作里面的查看功能
	 * @param model
	 * @param airTicketId
	 * @param request
	 * @param response
	 * @return
	 * @author shijun.liu
	 * @date 2015.11.25
	 */
	@RequestMapping(value="airTicketRead/{airTicketId}")
	public String airTicketRead(Model model,@PathVariable Long airTicketId,
			HttpServletRequest request, HttpServletResponse response){
		model.addAttribute("flag", "read");
		model.addAttribute("title", "成本付款-详情");
		readAndReview(airTicketId, model, request, response);
		return "review/payment/airTicketDetail";
	}
	
	/**
	 * 机票产品，审核列表，操作里面的审批功能
	 * @param model
	 * @param airTicketId
	 * @param request
	 * @param response
	 * @return
	 * @author shijun.liu
	 * @date 2015.11.25
	 */
	@RequestMapping(value="airTicketReview/{airTicketId}")
	public String airTicketReview(Model model,@PathVariable Long airTicketId,
			HttpServletRequest request, HttpServletResponse response){
		model.addAttribute("flag", "review");
		model.addAttribute("title", "成本付款-审批");
		readAndReview(airTicketId, model, request, response);
		return "review/payment/airTicketDetail";
	}
	
	/**
	 * 付款审批列表的查看和审核功能
	 * @param airTicketId	机票产品ID
	 * @param model
	 * @param request
	 * @param response
	 * @author shijun.liu
	 * @date 2015.11.25
	 */
	private void readAndReview(Long airTicketId, Model model, 
			HttpServletRequest request, HttpServletResponse response){
		ActivityAirTicket airTicket=activityAirTicketService.getActivityAirTicketById(airTicketId);
		model.addAttribute("areas", areaService.findAirportCityList(""));
		model.addAttribute("activityAirTicket",airTicket);		
		model.addAttribute("airportlist", airportService.queryAirportInfos());
	    List<Map<String,Object>> airticketOrders =iAirTicketOrderService.queryAirticketOrdersByProductId(airTicketId.toString());
        //计算机票剩余时间
		for(Map<String, Object> temp : airticketOrders){
			com.trekiz.admin.modules.airticketorder.web.AirTicketOrderController.getOrderLeftTime(temp);			
		} 
		model.addAttribute("airTicketOrderList", airticketOrders);
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		List<CostRecord> actualOutList = new ArrayList<CostRecord>();
		List<CostRecord> actualInList = new ArrayList<CostRecord>();
		String costIdStr = request.getParameter("costId");
		Long costId = StringUtils.isBlank(costIdStr)?null:Long.parseLong(costIdStr);
		// 0381需求 只针对越柬行踪
		/*if(Context.SUPPLIER_UUID_YJXZ.equals(UserUtils.getUser().getCompany().getUuid())) {
			//实际成本
			List<Map<String,Object>> actualList = costManageService.getCostRecordsByAgentsAndSupplierss(Integer.parseInt(airTicketId.toString()), Context.ORDER_TYPE_JP, new Integer(1));
			model.addAttribute("actualList", actualList);
//			costManageService.costInputList(model, airTicketId, Context.ORDER_TYPE_JP);
		}else {*/
			actualInList = costManageService.findNewAndOldCostRecordList(airTicketId, 1,0,Context.ORDER_TYPE_JP);
			actualOutList = costManageService.findNewAndOldCostRecordList(airTicketId, 1,1,Context.ORDER_TYPE_JP);
			//0425需求，起航假期根传入的costUuid参数，找到指定的CostRecord对象，并放到List的首位。 yudong.xu 2016.6.1
			if (Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getCompanyUuid())){
				Integer overseas = PayMentUtils.preHandleList(actualInList,actualOutList, costId);
				model.addAttribute("overseas",overseas);
			}
			model.addAttribute("actualInList", actualInList);
			model.addAttribute("actualOutList", actualOutList);
			//model.addAttribute("costLog",PayMentUtils.getPaymentReviewLog(actualInList, actualOutList));
			/*List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(airTicketId,Context.ORDER_TYPE_JP);
			List<Map<String, Object>> actualCost=costManageService.getCost(airTicketId,Context.ORDER_TYPE_JP,1);
			model.addAttribute("incomeList", incomeList);
			model.addAttribute("actualCost",actualCost);*/
//		}
		
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(airTicketId,Context.ORDER_TYPE_JP);
		List<Map<String, Object>> actualCost=costManageService.getCost(airTicketId,Context.ORDER_TYPE_JP,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("actualCost",actualCost);
		
		List<CostPaymentReviewNewLog> listLog = commonReviewService.getPaymentReviewLog(airTicketId, Context.ORDER_TYPE_JP, costId);
		model.addAttribute("paymentReviewLog",listLog);
	}

}
