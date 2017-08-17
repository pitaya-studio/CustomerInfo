package com.trekiz.admin.review.payment.visa.web;


import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.entity.ResponseJson;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.common.bean.CostPaymentReviewNewLog;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.payment.comment.exception.PaymentException;
import com.trekiz.admin.review.payment.comment.utils.PayMentUtils;
import com.trekiz.admin.review.payment.visa.service.IVisaPaymentReviewService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 签证产品付款审核的Controller
 * @author shijun.liu
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/review/visa/payment")
public class VisaPaymentReviewController {

	private static final Logger log = Logger.getLogger(VisaPaymentReviewController.class);

	@Autowired
	private IVisaPaymentReviewService visaPaymentReviewService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private DictService dictService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private ICommonReviewService commonReviewService;
	
	/**
	 * 签证产品付款申请，包括批量申请
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
				visaPaymentReviewService.apply(item);
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
	 * 签证产品，审核列表，操作里面的查看功能
	 * @param visaProductId		签证产品ID
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author shijun.liu
	 * @date 2015.11.26
	 */
	@RequestMapping(value="visaRead/{visaProductId}", method=RequestMethod.GET)
	public String visaRead(@PathVariable(value="visaProductId") Long visaProductId,
			HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("flag", "read");
		model.addAttribute("title", "成本付款-详情");
		readAndReview(visaProductId, model, request, response);
		return "review/payment/visaDetail";	
	}
	
	/**
	 * 签证产品，审核列表，操作里面的审批详情功能
	 * @param visaProductId		签证产品ID
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author shijun.liu
	 * @date 2015.11.26
	 */
	@RequestMapping(value="visaReview/{visaProductId}", method=RequestMethod.GET)
	public String visaReview(@PathVariable(value="visaProductId") Long visaProductId,
			HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("flag", "review");
		model.addAttribute("title", "成本付款-审批");
		readAndReview(visaProductId, model, request, response);
		return "review/payment/visaDetail";	
	}
	
	/**
	 * 付款审批列表的查看和审核功能
	 * @param visaProductId		签证产品ID
	 * @param model				Model对象
	 * @param request			request对象
	 * @param response			response对象
	 * @author shijun.liu
	 * @date 2015.11.26
	 */
	private void readAndReview(Long visaProductId, Model model, HttpServletRequest request, HttpServletResponse response){

	
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);		
		//model.addAttribute("costLog",PayMentUtils.getPaymentReviewLog(actualInList, actualOutList));
		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("visaCountryList", countryList);		
		
		VisaProducts visaProduct = this.visaProductsService.findByVisaProductsId(visaProductId);
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		model.addAttribute("visaProduct", visaProduct);
		
		List<Map<String, Object>> orderList = visaOrderService.queryVisaOrdersByProductId(visaProductId.toString()); 
		model.addAttribute("orderList", orderList);	
		
		List<Dict> ordertype = dictService.findByType("order_type");
		model.addAttribute("ordertype", ordertype);	
		//价格币种
		model.addAttribute("currencyMark", CurrencyUtils.getCurrencyInfo(visaProduct.getCurrencyId().toString(), 0, "mark"));
		if (visaProduct.getCurrencyId() != null) {
			Currency currency = this.currencyService.findCurrency(new Long(
					visaProduct.getCurrencyId()));
			model.addAttribute("currency", currency);
		}
		String costIdStr = request.getParameter("costId");
		Long costId = StringUtils.isBlank(costIdStr)?null:Long.parseLong(costIdStr);
		// 0381需求 只针对越柬行踪
		/*if(Context.SUPPLIER_UUID_YJXZ.equals(UserUtils.getUser().getCompany().getUuid())) {
			//实际成本
			List<Map<String,Object>> actualList = costManageService.getCostRecordsByAgentsAndSupplierss(Integer.parseInt(visaProductId.toString()), Context.ORDER_TYPE_QZ, new Integer(1));
			model.addAttribute("actualList", actualList);
//			costManageService.costInputList(model, visaProductId, Context.ORDER_TYPE_QZ);
		}else {*/
			List<CostRecord> actualInList = costManageService.findNewAndOldCostRecordList(visaProductId, 1,0,Context.ORDER_TYPE_QZ);
			List<CostRecord> actualOutList = costManageService.findNewAndOldCostRecordList(visaProductId, 1,1, Context.ORDER_TYPE_QZ);
			//0425需求，起航假期根传入的costUuid参数，找到指定的CostRecord对象，并放到List的首位。 yudong.xu 2016.6.1
			if (Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getCompanyUuid())){
				Integer overseas = PayMentUtils.preHandleList(actualInList,actualOutList, costId);
				model.addAttribute("overseas",overseas);
			}
			model.addAttribute("actualInList", actualInList);
			model.addAttribute("actualOutList", actualOutList);
			/*List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(visaProductId, Context.ORDER_TYPE_QZ);
			List<Map<String, Object>> actualCost=costManageService.getCost(visaProductId, Context.ORDER_TYPE_QZ, 1);
			model.addAttribute("incomeList", incomeList);
			model.addAttribute("actualCost",actualCost);*/
//		}

		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(visaProductId, Context.ORDER_TYPE_QZ);
		List<Map<String, Object>> actualCost=costManageService.getCost(visaProductId, Context.ORDER_TYPE_QZ, 1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("actualCost",actualCost);
		
		List<CostPaymentReviewNewLog> listLog = commonReviewService.getPaymentReviewLog(visaProductId, Context.ORDER_TYPE_QZ, costId);
		model.addAttribute("paymentReviewLog",listLog);
	}
}
