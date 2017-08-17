package com.trekiz.admin.review.payment.activity.web;


import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.entity.ResponseJson;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.service.sync.ActivityGroupSyncService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.common.bean.CostPaymentReviewNewLog;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.payment.activity.service.IActivityPaymentReviewService;
import com.trekiz.admin.review.payment.comment.exception.PaymentException;
import com.trekiz.admin.review.payment.comment.utils.PayMentUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 单团类产品付款审核的Controller
 * @author shijun.liu
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/review/activity/payment")
public class ActivityPaymentReviewController {

	private static final Logger log = Logger.getLogger(ActivityPaymentReviewController.class);

	@Autowired
	private IActivityPaymentReviewService activityPaymentReviewService;
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;	
	@Autowired	
	private ActivityGroupSyncService activityGroupService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private ICommonReviewService commonReviewService;
	
	/**
	 * 单团类产品付款申请，包括批量申请
	 * 		request获取的参数为items，items的值为：activityGroupId,orderType,costrecorduuid&activityGroupId,orderType,costrecorduuid
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
				activityPaymentReviewService.apply(item);
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
	 *  单团类产品，审核列表，操作里面的查看功能
	 * @param activityId
	 * @param groupId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author shijun.liu
	 */
	@RequestMapping(value="activityRead/{activityId}/{groupId}", method=RequestMethod.GET)
	public String activityRead(@PathVariable(value="activityId") Long activityId, 
			@PathVariable(value="groupId") Long groupId, Model model,
			HttpServletRequest request, HttpServletResponse response){
		model.addAttribute("flag", "read");
		model.addAttribute("title", "成本付款-详情");
		readAndReview(groupId, activityId, model, request, response);
		return "review/payment/activityDetail";
	}
	
	/**
	 * 单团类产品，审核列表，操作里面的审批功能
	 * @param activityId
	 * @param groupId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="activityReview/{activityId}/{groupId}", method=RequestMethod.GET)
	public String activityReview(@PathVariable(value="activityId") Long activityId, 
			@PathVariable(value="groupId") Long groupId, Model model,
			HttpServletRequest request, HttpServletResponse response){
		model.addAttribute("flag", "review");
		model.addAttribute("title", "成本付款-审批");
		readAndReview(groupId, activityId, model, request, response);
		return "review/payment/activityDetail";
	}
	
	/**
	 * 付款审批列表的查看和审核功能
	 * @param groupId		团期ID
	 * @param activityId	产品Id
	 * @param model			存储容器
	 * @param request		request对象
	 * @param response		response对象
	 * @author shijun.liu
	 * @date	2015.11.23
	 */
	private void readAndReview(Long groupId, Long activityId, Model model, 
			HttpServletRequest request, HttpServletResponse response){
		TravelActivity travelActivity = travelActivityService.findById(activityId);		
		ActivityGroup activityGroup = activityGroupService.findById(groupId);
		if(activityGroup != null && travelActivity != null && 
				activityGroup.getTravelActivity() != null && 
				activityGroup.getTravelActivity().getId().equals(travelActivity.getId())){
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);

			Integer typeId=travelActivity.getActivityKind();
			model.addAttribute("typeId",typeId);
			model.addAttribute("typename", OrderCommonUtil.getChineseOrderType(typeId.toString()));
			
			orderList = orderService.findOrderListByPayType(orderList, groupId);
			model.addAttribute("orderList", orderList.getList());			
			model.addAttribute("travelActivity", travelActivity);
			model.addAttribute("activityGroup", activityGroup);
			
			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);			
			model.addAttribute("curlist", currencylist);
			String costIdStr = request.getParameter("costId");
			Long costId = StringUtils.isBlank(costIdStr)?null:Long.parseLong(costIdStr);

			List<CostRecord> actualInList = costManageService.findNewAndOldCostRecordList(groupId, 1,0,typeId);
			List<CostRecord> actualOutList = costManageService.findNewAndOldCostRecordList(groupId, 1,1,typeId);

			//0425需求，起航假期根传入的costUuid参数，找到指定的CostRecord对象，并放到List的首位。 yudong.xu 2016.6.1
			if (Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getCompanyUuid())){
				Integer overseas = PayMentUtils.preHandleList(actualInList, actualOutList, costId);
				model.addAttribute("overseas",overseas);
			}
			model.addAttribute("actualInList", actualInList);
			model.addAttribute("actualOutList", actualOutList);

			
			List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(groupId,typeId);
			List<Map<String, Object>> actualCost=costManageService.getCost(groupId,typeId,1);
			model.addAttribute("incomeList", incomeList);
			model.addAttribute("actualCost", actualCost);
			
			List<CostPaymentReviewNewLog> listLog = commonReviewService.getPaymentReviewLog(groupId, typeId, costId);
			model.addAttribute("paymentReviewLog", listLog);
		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
	}

}
