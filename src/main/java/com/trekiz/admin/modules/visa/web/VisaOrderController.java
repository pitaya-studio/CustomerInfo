package com.trekiz.admin.modules.visa.web;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.avro.generic.GenericData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.utils.word.WordDownLoadUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.repository.OrderinvoiceDao;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.rebates.entity.Rebates;
import com.trekiz.admin.modules.order.rebates.service.RebatesService;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.service.CostchangeService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.pay.model.BatchPayModel;
import com.trekiz.admin.modules.pay.model.CurrencyAmount;
import com.trekiz.admin.modules.receipt.entity.OrderReceipt;
import com.trekiz.admin.modules.receipt.repository.OrderReceiptDao;
import com.trekiz.admin.modules.review.visaRebates.entity.MoreCurrencyComputePrice;
import com.trekiz.admin.modules.review.visaborrowmoney.service.IVisaBorrowMoneyService;
import com.trekiz.admin.modules.review.visaborrowmoney.web.VisaBorrowMoneyController;
import com.trekiz.admin.modules.review.visareturnreceipt.service.IVisaReturnReceiptService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.sys.entity.BatchTravelerRelation;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.BatchRecordDao;
import com.trekiz.admin.modules.sys.repository.BatchTravelerRelationDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.SysBatchNoService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.form.OriginalProjectForm;
import com.trekiz.admin.modules.visa.form.VisaOrderForm;
import com.trekiz.admin.modules.visa.form.VisaOrderTravelerResultForm;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaInterviewNoticeService;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;
import com.trekiz.admin.review.borrowing.visahqxborrowmoney.service.ActivityVisaHQXBorrowMoneyService;
import com.trekiz.admin.review.guarantee.service.GuaranteeService;
import com.trekiz.admin.review.returnvisareceipt.service.ActivityVisaReturnReceiptService;

import freemarker.template.TemplateException;
/**
 * 文件名: VisaProductController.java 功能: 签务和销售身份对签证产品订单管理
 * 
 * @author wenjianye
 * @DateTime 2014-12-1 下午2:36:17
 * @version 1.5
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/visa/order")
public class VisaOrderController extends BaseController {
	
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	
	@Autowired
	private VisaProductsService visaProductsService;
	
	@Autowired
	private RebatesService rebatesService;

	@Autowired
	private AgentinfoService agentinfoService;

	@Autowired
	private VisaOrderService visaOrderService;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private OrderCommonService orderCommonService;

	@Autowired
	private VisaService visaService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private OrderContactsService orderContactsService;
	@Autowired
	private OrderPayService orderPayService;
	@Autowired
	private CostchangeService costchangeService;
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private VisaInterviewNoticeService visaInterviewNoticeService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
    private DepartmentService departmentService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private SysBatchNoService sysBatchNoService;
	@Autowired
	private IVisaBorrowMoneyService visaBorrowMoneyService;
	@Autowired
	private IVisaReturnReceiptService visaReturnReceiptService;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private BatchRecordDao batchRecordDao;
	@Autowired
	private BatchTravelerRelationDao batchTravelerRelationDao;
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private ReviewDetailDao reviewDetailDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private OrderpayDao orderpayDao;
	@Autowired
	private OrderReceiptDao orderReceiptDao;
	@Autowired
	private OrderinvoiceDao orderinvoiceDao;
	@Autowired
	private ActivityVisaReturnReceiptService activityVisaReturnReceiptService;
	@Autowired
	private ActivityVisaHQXBorrowMoneyService activityVisaHQXBorrowMoneyService;
	@Autowired
	private GuaranteeService guaranteeService;
	@Autowired
	private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;
	
	
	@RequestMapping(value={"batchPay"})
	public String batchPay(HttpServletRequest req,HttpServletResponse response  ){
		
		//支付json串格式[{"travelerid":"1289","currencyAmounts":[{"currencyid":"44","price":"2"},{"currencyid":"45","price":"2"},{"currencyid":"46","price":"2"}]},{"travelerid":"1290","currencyAmounts":[{"currencyid":"45","price":"2"}]},{"travelerid":"1291","currencyAmounts":[{"currencyid":"45","price":"2"}]}]
		String parString = req.getParameter("parString");
		List<BatchPayModel> batchPayModels = new ArrayList<BatchPayModel>();
		Boolean hasPreOpeninvoice = false;
		if(parString.contains("@"))
		{
			BatchPayModel batchPayModel  = new BatchPayModel();
			Integer orderId = Integer.parseInt(parString.split("@")[1]);
			Integer travellerId = Integer.parseInt(parString.split("@")[0]);
			List<CurrencyAmount> list = visaOrderService.findMoneyAmount(orderId,travellerId);
			batchPayModel.setCurrencyAmounts(list);
			batchPayModel.setOrderid(orderId);
			batchPayModel.setTravelerid(travellerId);
			batchPayModels.add(batchPayModel);
			// 0444需求 判断在订单中是否有预开发票
			hasPreOpeninvoice = orderinvoiceDao.findHasPreOpeninvoiceInOrder(orderId);
		}
		else{
			batchPayModels = JSON.parseArray(parString, BatchPayModel.class);
		}
		
		OrderPayInput orderPayInput  = visaOrderService.batchPay(batchPayModels);
		orderPayInput.setEntryOrderUrl("/visa/order/searchxs?orderNo="+orderPayInput.getOrderPayDetailList().get(0).getOrderNum());
		orderPayInput.setHasPreOpeninvoice(hasPreOpeninvoice);
		// 0444 暂时只对签证设置为"1"
		orderPayInput.setComingPay("1");
		req.setAttribute("pay", orderPayInput);
		return "forward:../../orderPayMore/pay";
	}
	
	/**
	 * 获取付款游客的信息
	 * @author jiachen
	 * @DateTime 2015年6月25日 下午5:26:13
	 * @param travlerIds
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="payTravelerInfo")
	@ResponseBody
	public Map<String, Object> payTravelerInfo(String travlerIds) {
		
		Map<String, Object> infoMap = null;
		List<Traveler> travelerList = null;
		
		if(StringUtils.isNotBlank(travlerIds)) {
			infoMap = new HashMap<String, Object>();
			String[] travelerIdArr = travlerIds.split(",");
			for(int i = 0; i < travelerIdArr.length; i++) {
				travelerIdArr[i] = travelerIdArr[i].split("@")[0];
			}
			//游客信息列表
			travelerList = travelerService.findByIds(travelerIdArr);
			infoMap.put("companyId", UserUtils.getUser().getCompany().getId());
			infoMap.put("travelerList", travelerList);
			//累计计算总额
			MoreCurrencyComputePrice mccp = new MoreCurrencyComputePrice();
			//游客应付金额的币种串
			String currencyIds = "";
			//游客应付金额串
			String prices = "";
			for(Traveler t : travelerList) {
				String payPriceStr = "";
				//将游客的结算价转换成页面显示的字符串
				Map<String, String> map = visaOrderService.getChajiaPub(t.getPayPriceSerialNum(), t.getPayedMoneySerialNum());
				String[] huobiArr = map.get("huobiId").split(",");
				String[] jineArr = map.get("jine").split(",");
				for(int i = 0; i < huobiArr.length; i++) {
					mccp.addPrice(huobiArr[i], jineArr[i]);
					currencyIds += huobiArr[i] + ",";
					prices += jineArr[i] + ",";
					payPriceStr += CurrencyUtils.getCurrencyInfo(huobiArr[i], 0, "mark") + jineArr[i] + ",";
				}
				t.setPayPriceSerialNum(payPriceStr);
			}
			//应付总额
			infoMap.put("totleMoney", mccp.toString());
			infoMap.put("currencyIds", currencyIds);
			infoMap.put("prices", prices);
		}
		return infoMap;
	}
	
	@RequestMapping(value="cancelOrder")
    @ResponseBody
    public String cancelOrder(HttpServletRequest request){
        String orderId = request.getParameter("orderId");
        return visaOrderService.cancelOrder(Long.valueOf(orderId));
    }
    
    @ResponseBody
    @RequestMapping(value ="invokeOrder")
    public String invokeOrder(HttpServletRequest request){
        String id = request.getParameter("orderId");
        return visaOrderService.invokeOrder(Long.parseLong(id));
    }
    @RequestMapping(value = "lockOrder")
    @ResponseBody
    public Object lockOrder(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String orderId = request.getParameter("orderId");
        this.visaOrderService.lockOrder(Long.valueOf(orderId));
        map.put("success", "success");
        return map;
    }
    
    @RequestMapping(value = "unLockOrder")
    @ResponseBody
    public Object unLockOrder(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String orderId = request.getParameter("orderId");
        this.visaOrderService.unLockOrder(Long.valueOf(orderId));
        map.put("success", "success");
        return map;
    }
	
	/**
	 * 签证订单导出游客信息
	 * @param visaOrderId
	 * @param agentId
	 * @param groupCode
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="downloadTravelerByGroupCode")
	public void downloadTravelerByGroupCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//参团的团号
		String groupCode = request.getParameter("groupCode");
		//渠道id
		String agentId = request.getParameter("agentId");
		//如果没有渠道id就给默认0
		if(null == agentId || "".equals(agentId)){
			agentId="0";
		}
		String fileName = groupCode + "-游客信息";
		//Excel各行名称
		String[] cellTitle =  {"序号","中文姓名","英文姓名","性别","出生日期","出生地","证件类型","证件号码","有效日期","签发地","电话号码","渠道名字","渠道跟进销售","备注"};
		//文件首行标题
		String firstTitle = groupCode;
		//订单团号列表
		String dingdantuanhao = request.getParameter("dingdantuanhao");
		//单个订单团号的数组,
		String[] dingdanArray = null;
		List<Object[]> travelerList = new ArrayList<Object[]>();
		if(null != dingdantuanhao && !"".equals(dingdantuanhao)){
			//将团号列表分隔成多个个体
			dingdanArray = dingdantuanhao.split("</br>");
			for(int i =0;i<dingdanArray.length;i++){
				String dingdan = dingdanArray[i];
				 List<Object[]> sonList = travelerService.findTravelerByOrderCode(dingdan, Long.valueOf(agentId));
				 for(int j=0;j<sonList.size();j++){
					 travelerList.add(sonList.get(j));
				 }
			}
		}
		if(travelerList != null && travelerList.size() > 0) {
			int i = 0;
			for(Object[] o : travelerList) {
				i++;
				o[0] = i;
				String temp = null;
				if(o[6] != null && o[6].toString().length() >= 1) {
					temp = o[6].toString();
					temp = temp.replace("1", "身份证").replace("2", "护照").
											replace("3", "警官证").replace("4", "军官证").
											replace("5", "其他").replace(",", "/");
					if(temp.lastIndexOf("/") == (temp.length()-1)) {
						temp = temp.substring(0,temp.length()-1);
					}
					o[6] = temp;
				}
				if(o[7] != null && o[7].toString().length() >= 1) {
					temp = o[7].toString();
					temp = temp.replace(",", "/");
					if(temp.lastIndexOf("/") == (temp.length()-1)) {
						temp = temp.substring(0,temp.length()-1);
					}
					o[7] = temp;
				}
			}
			ExportExcel.createExcle(fileName, travelerList, cellTitle, firstTitle, request, response);
		}
		ExportExcel.createExcle(fileName, travelerList, cellTitle, firstTitle, request, response);
	}
	
	/***
	 * 签务身份管理签证订单
	 * 查询是否有正在审核的押金转担保申请
	 * 
	 */   
	@ResponseBody
	@RequestMapping(value={"searchApply"})
	public Object searchApply(HttpServletRequest req){
		  
		Map<String, Object> map = new HashMap<String, Object>();
		//visa表的主键id
		String orderId = req.getParameter("orderId");
		//游客表的主键id
		String travelerId = req.getParameter("travelerId");
		if(null == travelerId || null == orderId){
			map.put("message", "参数错误,请重新请求");
			return map;
		}
		//查看是否有正在审批的 押金转担保的申请
		boolean shenqingFalg = visaOrderService.searchReview(orderId,travelerId);
		
		//存在正在审核的申请
		if(shenqingFalg){
			map.put("message", "show");
		}else{
			map.put("message", "no");
		}
		return map;
	}
	
	/***
	 * 签务身份管理签证订单
	 * 撤销押金转担保申请
	 * 
	 */   
	@ResponseBody
	@RequestMapping(value={"cancelApply"})
	public Object cancelApply(HttpServletRequest req){
		Map<String, Object> map = new HashMap<String, Object>();
		//visa表的主键id
		String orderId = req.getParameter("orderId");
		//游客表的主键id
		String travelerId = req.getParameter("travelerId");
		if(null == travelerId || null == orderId){
			map.put("message", "参数错误,请重新请求");
			return map;
		}
		//撤销押金转担保的申请
		int status = reviewService.removeReview(16,6,orderId,Long.valueOf(travelerId));
		
		if(1 == status ){
			map.put("message", "押金转担保取消成功");
		}else{
			map.put("message", "押金转担保取消失败");
		}
		return map;
	}
	

	/***
	 * 销售身份管理签证订单
	 * 查询页面的初始化页面
	 * visaId:visa表的主键
	 * visaProductId:签证产品表的主键id
	 */     
	@RequestMapping(value={"salespersonList"})
	public String initSalespersonOrederList(HttpServletRequest req,HttpServletResponse response){
		VisaOrderForm form = new VisaOrderForm();
		searchxs(req,response, form);
		//填充初始数据
		//initializationData(req);
		processMenuProcRight_xiaoShou(req);
		return "modules/visa/visaOrderXiaoshou";
	}
	
	
	/**
	 * 跳转到返佣列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="showRebatesList")
	public String showRebatesList(Model model, HttpServletRequest request){
		Long orderId = Long.parseLong(request.getParameter("orderId"));
				model.addAttribute("companyId",UserUtils.getUser().getCompany().getId());
				// 订单ID
				model.addAttribute("orderId", orderId);
				model.addAttribute("orderType", Context.ORDER_TYPE_QZ);
				// 返佣记录List
				model.addAttribute("borrowMoneyRecordList", visaOrderService.rebatesRecord(orderId.toString()));
				return "modules/order/rebates/visarebatesList";
	}
	
	

	
	
	
	
	/**
	 * 验证是否能再次返佣信息
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="validRebates",method = RequestMethod.POST)
	public String validRebates(HttpServletResponse response, HttpServletRequest request){
		String orderId = request.getParameter("orderId");
		Integer orderType = Integer.parseInt(request.getParameter("orderType"));
		@SuppressWarnings("rawtypes")
		List rebatesList = rebatesService.findVisaRebatesListByStatus(orderType, Context.REBATES_FLOW_TYPE, orderId);
		if(rebatesList != null && rebatesList.size() > 0){
			return "false1";
		}else{
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
			VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			//对结算单状态进行判断
			if (1 == visaProducts.getLockStatus()) {
				return "false2";
			}
		}
		return "true";
	}
	
	
	/**
	 * 跳转到返佣申请界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="addRebates/{orderId}/{orderStatus}")
	public String addRebates(@PathVariable Long orderId, @PathVariable int orderStatus, Model model, HttpServletRequest request){
			//签证订单
			VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
			model.addAttribute("visaOrder", visaOrder);
			model.addAttribute("visaOrderId", orderId);
			
			//团队预计返佣
			String groupRebatesUUID = visaOrder.getGroupRebate();
			if(StringUtils.isBlank(groupRebatesUUID)){
				model.addAttribute("groupRebates", "—");
			}else{
				MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(groupRebatesUUID);
				if(moneyAmount != null){
				if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO) != 0){
					model.addAttribute("groupRebates", OrderCommonUtil.getMoneyAmountBySerialNum(groupRebatesUUID, 2));
				}else{
					model.addAttribute("groupRebates", "—");
				}
				}else{
					model.addAttribute("groupRebates", "—");
				}
			}
			
			
			//签证产品
			Long proId = visaOrder.getVisaProductId();
			if(null != proId && 0 != proId) {
				VisaProducts visaProduct = visaProductsService.findByVisaProductsId(proId);
				model.addAttribute("visaProduct", visaProduct);
			}
			
			//游客列表
			List<Traveler> travelerList = visaBorrowMoneyService.getTravelerList(orderId.toString());
			model.addAttribute("travelerList", travelerList);
			
			//累计返佣金额
			//通过订单ID查询reviewId
			List<Review> reviewList = reviewDao.findReviewSortByCreateDate(
					Context.ORDER_TYPE_QZ, Context.REBATES_FLOW_TYPE, orderId.toString());
			StringBuilder sb_trvids = new StringBuilder("");
			StringBuilder sb_rebatestrvcurrents = new StringBuilder("");
			StringBuilder sb_trvamounts = new StringBuilder("");
			if (reviewList != null && reviewList.size() > 0) {
				for (Review review : reviewList) {
					if (review.getStatus() == 2) {
						List<ReviewDetail> trvids =reviewDetailDao.findReviewDetailByMykey(review.getId(), "trvids");
						List<ReviewDetail> rebatestrvcurrents =reviewDetailDao.findReviewDetailByMykey(review.getId(), "rebatestrvcurrents");
						List<ReviewDetail> trvamounts =reviewDetailDao.findReviewDetailByMykey(review.getId(), "trvamounts");
						if (trvids != null && trvids.size() > 0 && rebatestrvcurrents != null && rebatestrvcurrents.size() > 0 && trvamounts != null && trvamounts.size() > 0) {
							sb_trvids.append(trvids.get(0).getMyvalue());
							sb_rebatestrvcurrents.append(rebatestrvcurrents.get(0).getMyvalue());
							sb_trvamounts.append(trvamounts.get(0).getMyvalue());
						}
					}
				}
				//累计返佣金额隐藏域
				List<ReviewDetail> totalRebatesJe =reviewDetailDao.findReviewDetailByMykey(reviewList.get(0).getId(), "totalRebatesJe");
				if (totalRebatesJe != null && totalRebatesJe.size() > 0 && totalRebatesJe.get(0).getMyvalue().length() > 0) {
					model.addAttribute("totalRebatesJe", totalRebatesJe.get(0).getMyvalue());
				} else {
					model.addAttribute("totalRebatesJe", "0");
				}
			} else {
				model.addAttribute("totalRebatesJe", "0");
			}
			String[] trvids = sb_trvids.toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] rebatestrvcurrents = sb_rebatestrvcurrents.toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvamounts = sb_trvamounts.toString().split(VisaBorrowMoneyController.SPLITMARK);
			Map<String, BigDecimal> map = null;
			for (Traveler traveler : travelerList) {
				map = new LinkedHashMap<String, BigDecimal>();
				for (int i = 0; sb_trvids.length() > 0 && i < trvids.length; i++) {
					if (traveler.getId().toString().equals(trvids[i])) {
						String currencyId = rebatestrvcurrents[i];
						String currencyPrice = trvamounts[i];
						if (!map.containsKey(currencyId)) {
							map.put(currencyId, new BigDecimal(currencyPrice));
						} else {
							map.put(currencyId,map.get(currencyId).add(new BigDecimal(currencyPrice)));
						}
					}
				}
				if (map.size() > 0){
					StringBuilder totalRebateJe = new StringBuilder("");
					Iterator<Entry<String, BigDecimal>> iter = map.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map.Entry entry = (Map.Entry) iter.next();
						String key = entry.getKey().toString();
						DecimalFormat df = new DecimalFormat("#,##0.00");
						String val = df.format(new BigDecimal(entry.getValue().toString()));
						Currency currency = currencyService.findCurrency(Long.parseLong(key));
						totalRebateJe.append(currency.getCurrencyMark());
						totalRebateJe.append(val);
						if (iter.hasNext()) {
							totalRebateJe.append("</br>");
						}
					}
					traveler.setTotalRebateJe(totalRebateJe.toString());
					totalRebateJe.delete(0,totalRebateJe.length()-1);
				} else {
					traveler.setTotalRebateJe("￥0.00");
				}
			}
			
			//币种列表
			List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
			model.addAttribute("currencyList", currencyList);
			model.addAttribute("visaCostPriceFlag", UserUtils.getUser().getCompany().getVisaCostPrice());
		return "modules/visa/rebatesAddForm";
		//return "modules/order/rebates/rebatesAdd";
	}
	
	
	/**
	 * 跳转到返佣详情界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value="visaRebatesDetail")
	public String visaRebatesDetail(Model model, HttpServletRequest request){
		String orderId = request.getParameter("orderId");
		//String travelerId = request.getParameter("travelerId");
		//String flag = request.getParameter("flag");
		String reviewId = request.getParameter("reviewId");
		//String flowType = request.getParameter("flowType");
		//String nowLevel = request.getParameter("nowLevel");
		
		//签证订单
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("visaOrderId", orderId);
		
		//团队预计返佣
		String groupRebatesUUID = visaOrder.getGroupRebate();
		if(StringUtils.isBlank(groupRebatesUUID)){
			model.addAttribute("groupRebates", "—");
		}else{
			MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(groupRebatesUUID);
			if(moneyAmount != null){
				if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO) != 0){
					model.addAttribute("groupRebates", OrderCommonUtil.getMoneyAmountBySerialNum(groupRebatesUUID, 2));
				}else{
					model.addAttribute("groupRebates", "—");
				}
			}else{
				model.addAttribute("groupRebates", "—");
			}
		}
		
		//签证产品
		Long proId = visaOrder.getVisaProductId();
		if(null != proId && 0 != proId) {
			VisaProducts visaProduct = visaProductsService.findByVisaProductsId(proId);
			model.addAttribute("visaProduct", visaProduct);
		}
		
		//个人返佣
		List<ReviewDetail> travelerIds =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "trvids");
		List<Map<String,String>> trvList = new ArrayList<Map<String,String>>();
		Map<String, String> trvMap = null;
		Map<String, String> groupMap = null;
		if (travelerIds != null && travelerIds.size() > 0 && travelerIds.get(0).getMyvalue().length() > 0) {
			List<ReviewDetail> travelerNames =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "trvnames");
			List<ReviewDetail> trvcurrencyNames =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "trvcurrencyNames");
			List<ReviewDetail> trvcurrencyMarks =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "trvcurrencyMarks");
			List<ReviewDetail> trvborrownames =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "trvborrownames");
			List<ReviewDetail> trvsettlementprices =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "trvsettlementprices");
			List<ReviewDetail> trvamounts =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "trvamounts");
			List<ReviewDetail> trvrebatesnotes =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "trvrebatesnotes");
			
			String[] travelerIdS = travelerIds.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] travelerNameS = travelerNames.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvcurrencyNameS = trvcurrencyNames.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvcurrencyMarkS = trvcurrencyMarks.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvborrownameS = trvborrownames.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvsettlementpriceS = trvsettlementprices.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvamountS = trvamounts.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvrebatesnoteS = trvrebatesnotes.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			
			//游客累计返佣金额
			//通过订单ID查询reviewId
			List<Review> reviewList = reviewDao.findReviewSortByCreateDate(
					Context.ORDER_TYPE_QZ, Context.REBATES_FLOW_TYPE, orderId.toString());
			StringBuilder sb_trvids = new StringBuilder("");
			StringBuilder sb_rebatestrvcurrents = new StringBuilder("");
			StringBuilder sb_trvamounts = new StringBuilder("");
			if (reviewList != null && reviewList.size() > 0) {
				for (Review review : reviewList) {
					if (review.getStatus() == 2) {
						List<ReviewDetail> tids =reviewDetailDao.findReviewDetailByMykey(review.getId(), "trvids");
						List<ReviewDetail> rcurrents =reviewDetailDao.findReviewDetailByMykey(review.getId(), "rebatestrvcurrents");
						List<ReviewDetail> tamounts =reviewDetailDao.findReviewDetailByMykey(review.getId(), "trvamounts");
						if (tids != null && tids.size() > 0 && rcurrents != null && rcurrents.size() > 0 && tamounts != null && tamounts.size() > 0) {
							sb_trvids.append(tids.get(0).getMyvalue());
							sb_rebatestrvcurrents.append(rcurrents.get(0).getMyvalue());
							sb_trvamounts.append(tamounts.get(0).getMyvalue());
						}
					}
				}
			}
			
			for (int i = 0; i < travelerIdS.length; i++) {
				Map<String, BigDecimal> map = null;
				trvMap = new HashMap<String, String>();
				trvMap.put("travelerId", travelerIdS[i]);
				trvMap.put("travelerName", travelerNameS[i]);
				trvMap.put("trvcurrencyName", trvcurrencyNameS[i]);
				trvMap.put("trvcurrencyMark", trvcurrencyMarkS[i]);
				trvMap.put("trvborrowname", trvborrownameS[i]);
				trvMap.put("trvsettlementprice", trvsettlementpriceS[i]);
				trvMap.put("trvamount", trvamountS[i]);
				trvMap.put("trvrebatesnote", trvrebatesnoteS[i]);
				
				
				Traveler reTraveler = travelerService.findTravelerById(Long.parseLong(travelerIdS[i]));
				trvMap.put("rebatesMoneySerialNum", reTraveler.getRebatesMoneySerialNum());
				
				String[] tids = sb_trvids.toString().split(VisaBorrowMoneyController.SPLITMARK);
				String[] rcurrents = sb_rebatestrvcurrents.toString().split(VisaBorrowMoneyController.SPLITMARK);
				String[] tamounts = sb_trvamounts.toString().split(VisaBorrowMoneyController.SPLITMARK);
				map = new LinkedHashMap<String, BigDecimal>();
				for (int j = 0; sb_trvids.length() > 0 && j < tids.length; j++) {
					if (travelerIdS[i].toString().equals(tids[j])) {
						String currencyId = rcurrents[j];
						String currencyPrice = tamounts[j];
						if (!map.containsKey(currencyId)) {
							map.put(currencyId, new BigDecimal(currencyPrice));
						} else {
							map.put(currencyId,map.get(currencyId).add(new BigDecimal(currencyPrice)));
						}
					}
				}
				if (map.size() > 0){
					StringBuilder totalRebateJe = new StringBuilder("");
					Iterator<Entry<String, BigDecimal>> iter = map.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map.Entry entry = (Map.Entry) iter.next();
						String key = entry.getKey().toString();
						DecimalFormat df = new DecimalFormat("#,##0.00");
						String val = df.format(new BigDecimal(entry.getValue().toString()));
						Currency currency = currencyService.findCurrency(Long.parseLong(key));
						totalRebateJe.append(currency.getCurrencyMark());
						totalRebateJe.append(val);
						if (iter.hasNext()) {
							totalRebateJe.append("</br>");
						}
					}
					trvMap.put("totalRebateJe", totalRebateJe.toString());
					totalRebateJe.delete(0,totalRebateJe.length()-1);
				} else {
					trvMap.put("totalRebateJe", "￥0.00");
				}
				
				trvList.add(trvMap);
			}
		}
		model.addAttribute("travelerList", trvList);
		
		//团队返佣
		List<ReviewDetail> grouprebatesamounts =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "grouprebatesamounts");
		List<Map<String,String>> groupList = new ArrayList<Map<String,String>>();
		if (grouprebatesamounts != null && grouprebatesamounts.size() > 0 && grouprebatesamounts.get(0).getMyvalue().length() > 0) {
			List<ReviewDetail> grouprebatesnames =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "grouprebatesnames");
			List<ReviewDetail> groupcurrencyNames =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "groupcurrencyNames");
			List<ReviewDetail> groupcurrencyMarks =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "groupcurrencyMarks");
			List<ReviewDetail> grouprebatesnodes =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "grouprebatesnodes");
			
			String[] grouprebatesnameS = grouprebatesnames.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] groupcurrencyNameS = groupcurrencyNames.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] groupcurrencyMarkS = groupcurrencyMarks.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] grouprebatesamountS = grouprebatesamounts.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			String[] grouprebatesnodeS = grouprebatesnodes.get(0).getMyvalue().split(VisaBorrowMoneyController.SPLITMARK);
			
			for (int i = 0; i < grouprebatesamountS.length; i++) {
				groupMap = new HashMap<String, String>();
				groupMap.put("grouprebatesname", grouprebatesnameS[i]);
				groupMap.put("groupcurrencyName", groupcurrencyNameS[i]);
				groupMap.put("groupcurrencyMark", groupcurrencyMarkS[i]);
				groupMap.put("grouprebatesamount", grouprebatesamountS[i]);
				groupMap.put("grouprebatesnode", grouprebatesnodeS[i]);
				groupList.add(groupMap);
			}
		}
		model.addAttribute("groupList", groupList);
		List<ReviewDetail> totalrebatesamount =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(reviewId), "totalrebatesamount");
		model.addAttribute("totalrebatesamount", totalrebatesamount.get(0).getMyvalue());
		
		model.addAttribute("rid",reviewId);
		
		String shenfen = request.getParameter("shenfen");
		model.addAttribute("shenfen",shenfen);
		
		return "modules/visa/visaRebatesDetail";
	}
	
	/**
	 * 签证返佣取消
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "cancelVisaRebates")
	@Transactional
	public Map<String, Object> cancelVisaRebates(HttpServletRequest request,
		HttpServletResponse response) throws JSONException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try{
			Long revId = Long.parseLong(request.getParameter("revId"));
			Review review = reviewService.findReviewInfo(revId);
			if (null != review && 1 == review.getStatus().intValue()) {
				reviewService.removeReview(revId);
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", "取消失败！");
		}
		
		map.put("success", "取消成功！");
		
		return map;
	}
	
	/**
	 * 签证返佣申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "visaRebatesAppliy")
	public Map<String, Object> visaRebatesAppliy(HttpServletRequest request,
		HttpServletResponse response) throws JSONException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer reply = new StringBuffer("签证返佣");
		
		String orderid = request.getParameter("visaOrderId");//订单id
		
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
		Long agentId = visaOrder.getAgentinfoId();
		String agentName = "";
		if (agentId == -1) {
			agentName = visaOrder.getAgentinfoName();
		} else {
			agentName = agentinfoService.findOne(agentId).getAgentName();
		}
		
		boolean yubao_locked = false; //预报单是否锁定标识
		boolean jiesuan_locked = false; //结算单是否锁定标识
		VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		//对结算单状态进行判断
		if (1 == visaProducts.getLockStatus()) {//!=改为了== by chy 2015年6月8日17:34:52 1为锁 0 标示未锁
			jiesuan_locked = true;
			// 返回返佣记录列表页
			reply.append("结算单已锁定，不能申请返佣数据");
			map.put("visaJKreply",reply.toString());//1为申请成功 2为申请失败
			map.put("orderId", orderid);
			return map;
		}
		//对预报单状态进行判断
		if ("10".equals(visaProducts.getForcastStatus())) {//00改为了10 by chy 2015年6月8日17:35:24 00标示未锁 10标示锁定
			yubao_locked = true;
		}
		
		/**
		 * 
		 * 1.新行者字符串分割符号    #@!#!@#
		 * 
		 * 2.游客借款数据
		 *   游客id：trvids
		 *   游客姓名：trvnames
		 *   游客币种：trvcurrents
		 *   游客款项名称：trvborrownames
		 *   游客借款额：trvamounts
		 *   游客结算价：trvsettlementprices
		 *   游客借款备注：trvborrownotes
		 *   
		 * 3.团队借款数据
		 *   
		 *   订单id：orderid
		 *   团队款项名称：groupborrownames
		 *   团队款项币种：groupborrowcurrents
		 *   团队款项借款额：groupRebatesamounts
		 *   团队借款备注：groupborrownodes
		 *   
		 * 4.借款总额：totalborrowamount  币种，符号，额度 #@!#!@#币种，符号，额度
		 * 5.借款总备注：grouptotalborrownode
		 * 6.累计返佣金额：totalRebatesJe
		 * 
		 */
		List<Detail> listDetail = new ArrayList<Detail>();
		
		//游客借款信息
		String[] trvids = request.getParameterValues("trvids");//游客ID
		String[] trvnames = request.getParameterValues("trvnames");//游客姓名
		String[] rebatestrvcurrents = request.getParameterValues("refundCurrency");//游客币种
		String[] trvborrownames = request.getParameterValues("trvborrownames");//游客款项名称
		String[] trvamounts = request.getParameterValues("lendPrice");//借款金额  trvamounts
		String[] trvsettlementprices = request.getParameterValues("trvsettlementprices");//游客结算价
		String[] trvrebatesnotes = request.getParameterValues("trvborrownotes");//游客借款备注
		
		//团队借款信息
		String[] grouprebatesnames = request.getParameterValues("groupborrownames");//团队款项名称
		String[] grouprebatescurrents = request.getParameterValues("teamCurrency");//团队款项币种  groupborrowcurrents
		String[] grouprebatesamounts = request.getParameterValues("teamMoney");//团队款项借款额  groupborrowamounts
		String[] grouprebatesnodes = request.getParameterValues("groupborrownodes");//团队借款备注
		
		String grouptotalrebatesnode = request.getParameter("otherRemarks");//总团队借款备注
		//String totalborrowamount = request.getParameter("totalborrowamount");//团队借款总额
		
		//处理订单 游客的 借款信息-----------------------------
		StringBuilder sb_trvids = new StringBuilder("");//游客ID
		StringBuilder sb_trvnames = new StringBuilder("");//游客姓名
		StringBuilder sb_rebatestrvcurrents = new StringBuilder("");//游客币种
		StringBuilder sb_trvborrownames = new StringBuilder("");//游客款项名称
		StringBuilder sb_trvamounts = new StringBuilder("");//借款金额
		StringBuilder sb_trvsettlementprices = new StringBuilder("");//游客结算价
		StringBuilder sb_trvrebatesnotes = new StringBuilder("");//游客借款备注
		
		//保存游客借款汇率、币种名称、币种符号
		StringBuilder sb_trvrebatesexchangerates = new StringBuilder("");//游客借款备注
		StringBuilder sb_trvcurrencyNames = new StringBuilder("");//游客借款备注
		StringBuilder sb_trvcurrencyMarks = new StringBuilder("");//游客借款备注
		
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		
		for (int i = 0; i < trvamounts.length; i++) {
			if (null!=trvamounts[i]&&!"".equals(trvamounts[i].trim())) {
				sb_trvids.append(trvids[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客ID
				sb_trvnames.append(trvnames[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客姓名
				sb_rebatestrvcurrents.append(rebatestrvcurrents[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客币种
				String trvborrowname = "".equals(trvborrownames[i])?" ":trvborrownames[i];
				sb_trvborrownames.append(trvborrowname).append(VisaBorrowMoneyController.SPLITMARK);//游客款项名称
				sb_trvamounts.append(trvamounts[i]).append(VisaBorrowMoneyController.SPLITMARK);//借款金额
				sb_trvsettlementprices.append(trvsettlementprices[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客结算价
				String trvrebatesnote = "".equals(trvrebatesnotes[i])?" ":trvrebatesnotes[i];
				sb_trvrebatesnotes.append(trvrebatesnote).append(VisaBorrowMoneyController.SPLITMARK);//游客借款备注
				
				//获取游客借款汇率
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.currency_name,c.currency_mark,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
				buffer.append(rebatestrvcurrents[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
				Map<String, Object>  mp =  list.get(0);
				sb_trvrebatesexchangerates.append(mp.get("convert_lowest")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_trvcurrencyNames.append(mp.get("currency_name")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_trvcurrencyMarks.append(mp.get("currency_mark")).append(VisaBorrowMoneyController.SPLITMARK);
			}
		}
		listDetail.add(new Detail("trvids", sb_trvids.toString()));//游客ID
		listDetail.add(new Detail("trvnames", sb_trvnames.toString()));//游客姓名
		listDetail.add(new Detail("rebatestrvcurrents", sb_rebatestrvcurrents.toString()));//游客币种
		listDetail.add(new Detail("trvborrownames", sb_trvborrownames.toString()));//游客款项名称
		listDetail.add(new Detail("trvamounts", sb_trvamounts.toString()));//借款金额
		listDetail.add(new Detail("trvsettlementprices", sb_trvsettlementprices.toString()));//游客结算价
		listDetail.add(new Detail("trvrebatesnotes", sb_trvrebatesnotes.toString()));//游客借款备注
		//保存游客借款汇率、币种名称、币种符号
		listDetail.add(new Detail("trvrebatesexchangerates", sb_trvrebatesexchangerates.toString()));//游客借款汇率
		listDetail.add(new Detail("trvcurrencyNames", sb_trvcurrencyNames.toString()));//游客借款币种名称
		listDetail.add(new Detail("trvcurrencyMarks", sb_trvcurrencyMarks.toString()));//游客借款币种符号
		listDetail.add(new Detail("totalRebatesJe", request.getParameter("totalRebatesJe").toString()));//累计返佣金额
		
		//处理订单的  借款信息-------------------------
		StringBuilder sb_grouprebatesnames = new StringBuilder("");//团队款项名称
		StringBuilder sb_grouprebatescurrents = new StringBuilder("");//团队款项币种
		StringBuilder sb_grouprebatesamounts = new StringBuilder("");//团队款项借款额
		StringBuilder sb_grouprebatesnodes = new StringBuilder("");//团队借款备注
		
		//保存订单借款汇率、币种名称、币种符号
		StringBuilder sb_grouprebatesexchangerates = new StringBuilder("");//游客借款备注
		StringBuilder sb_groupcurrencyNames = new StringBuilder("");//游客借款备注
		StringBuilder sb_groupcurrencyMarks = new StringBuilder("");//游客借款备注
		
		for (int i = 0; i < grouprebatesamounts.length; i++) {
			if (null!=grouprebatesamounts[i]&&!"".equals(grouprebatesamounts[i].trim())) {
				String grouprebatesname = "".equals(grouprebatesnames[i])?" ":grouprebatesnames[i];
				sb_grouprebatesnames.append(grouprebatesname).append(VisaBorrowMoneyController.SPLITMARK);//团队款项名称
				sb_grouprebatescurrents.append(grouprebatescurrents[i]).append(VisaBorrowMoneyController.SPLITMARK);//团队款项币种
				sb_grouprebatesamounts.append(grouprebatesamounts[i]).append(VisaBorrowMoneyController.SPLITMARK);//团队款项借款额
				String grouprebatesnode = "".equals(grouprebatesnodes[i])?" ":grouprebatesnodes[i];
				sb_grouprebatesnodes.append(grouprebatesnode).append(VisaBorrowMoneyController.SPLITMARK);//团队借款备注
				
				//获取订单借款汇率
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.currency_name,c.currency_mark,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
				buffer.append(grouprebatescurrents[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
				Map<String, Object>  mp =  list.get(0);
				sb_grouprebatesexchangerates.append(mp.get("convert_lowest")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_groupcurrencyNames.append(mp.get("currency_name")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_groupcurrencyMarks.append(mp.get("currency_mark")).append(VisaBorrowMoneyController.SPLITMARK);
				
			}
		}
		listDetail.add(new Detail("grouprebatesnames", sb_grouprebatesnames.toString()));//团队款项名称
		listDetail.add(new Detail("grouprebatescurrents", sb_grouprebatescurrents.toString()));//团队款项币种
		listDetail.add(new Detail("grouprebatesamounts", sb_grouprebatesamounts.toString()));//团队款项借款额
		listDetail.add(new Detail("grouprebatesnodes", sb_grouprebatesnodes.toString()));//团队借款备注
		
		listDetail.add(new Detail("orderid", orderid));//订单ID
		listDetail.add(new Detail("grouptotalrebatesnode", grouptotalrebatesnode));//总团队借款备注
		
		//保存游客借款汇率、币种名称、币种符号
		listDetail.add(new Detail("grouprebatesexchangerates", sb_grouprebatesexchangerates.toString()));//游客借款备注
		listDetail.add(new Detail("groupcurrencyNames", sb_groupcurrencyNames.toString()));//游客借款币种名称
		listDetail.add(new Detail("groupcurrencyMarks", sb_groupcurrencyMarks.toString()));//游客借款币种符号
		
		//将借款多币种总和根据汇率转换成人民币
		String count = (sb_trvamounts.toString()+sb_grouprebatesamounts.toString()).replace(VisaBorrowMoneyController.SPLITMARK, ",");
		String currencyId = (sb_rebatestrvcurrents.toString()+sb_grouprebatescurrents.toString()).replace(VisaBorrowMoneyController.SPLITMARK, ",");
		float totalrebates4rmb = visaBorrowMoneyService.currencyConverter(count, currencyId);
		listDetail.add(new Detail("rebatesAmount", totalrebates4rmb+""));//按照汇率转为人民币的团队借款总额
		
		//获取人民币币种id
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
		buffer.append(" AND c.create_company_id=");
		buffer.append(UserUtils.getUser().getCompany().getId());
		List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
		Integer rebatestotalcurrencyId = 0;
		for (int i = 0; i < currencylist.size(); i++) {
			if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
				rebatestotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
				break;
			}
		}
		listDetail.add(new Detail("currencyId", rebatestotalcurrencyId+""));//汇总后各个渠道的人民币币种id
		
		//将币种和金额汇总后转换成如下格式  借款金额：￥800+$800
		StringBuilder sbcurentcy = new StringBuilder("");
		Map<String, BigDecimal> currentMap =  getTotalMoney(currencyId.split(","),count.split(","));
		Set<String> keys = currentMap.keySet();
		for (String key : keys) {
			Currency currency = currencyService.findCurrency(Long.parseLong(key));
			DecimalFormat df = new DecimalFormat("#,##0.00");
			sbcurentcy.append(currency.getCurrencyMark()).append(df.format(currentMap.get(key)).toString()).append(" ");
		}
		String totalrebatesamount = sbcurentcy.toString().replace(" ", "+");
		listDetail.add(new Detail("totalrebatesamount", totalrebatesamount.substring(0,totalrebatesamount.length()-1)));//团队借款总额str.subString(0,str.length()-1)
		
		/**
		 * 处理review表中是否需要保存游客id
		 * 规则如下：
		 * 1.如果有团队借款则不保存
		 * 2.如没有团队借款则看游客借款是否为一条，如为一条则保存
		 */
		String[] travelerIds = sb_trvids.toString().split(VisaBorrowMoneyController.SPLITMARK);
		String travelerId = "0";
		if (null==sb_grouprebatesamounts||sb_grouprebatesamounts.toString().length()==0) {
			travelerId = travelerIds.length==1?travelerIds[0]:"0";
		}
		
		//通过orderId获取产品的发布部门
		Long deptId = visaOrderService.getProductPept(orderid);
		
		long  addresult = reviewService.addReview(Context.ORDER_TYPE_QZ, //产品类型 
				Context.REBATES_FLOW_TYPE,//流程类型  flowtype
				orderid,//订单ID
				Long.parseLong(travelerId),//游客ID
				Long.parseLong("0"), //新提交的审核请置 0. 重新提交审核时,等于上次审核记录的主键
				"",//创建原因
				reply, 
				listDetail,
				deptId);
		if (addresult!=0) {
			
			//----------往预算单和结算单插入数据------------
			
			Rebates rebates = new Rebates();
			rebates.setCurrencyId(rebatestotalcurrencyId.longValue());
			rebates.setRebatesDiff(new BigDecimal(totalrebates4rmb+""));
			costManageService.saveRebatesCostRecord(Context.ORDER_TYPE_QZ, rebates, visaOrder, agentName, addresult, deptId, yubao_locked, jiesuan_locked);
			
			//----------------------
			
			reply.append("申请成功");
		}
		
		map.put("visaJKreply",reply.toString());//1为申请成功 2为申请失败
		map.put("orderId", orderid);
		return map;
	}
	
	/**
	 * 将币种和金额汇总后转换成如下格式  借款金额：￥800+$800
	 * 
	 * @param currencyIds:{"1,2,34,5","1,3,34,7"};
	 * @param currencyPrices:{"120.00,50.00,40.00,70.00","100.00,50.00,40.00,50.00"}
	 * @return
	 */
	private Map<String, BigDecimal> getTotalMoney(String[] currencyIds,String[] currencyPrices ){
		//合并所有游客各个币种的钱数
		Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
		for (int i = 0; i < currencyIds.length; i++) {
			String currencyId = currencyIds[i];
			String currencyPrice = currencyPrices[i];
			String[] currIds = currencyId.split(",");
			String[] currPrices = currencyPrice.split(",");
			for (int j = 0; j < currIds.length; j++) {
				String currId = currIds[j];
				String currPrice = currPrices[j];

				if (!map.containsKey(currId)) {
					map.put(currId, new BigDecimal(currPrice));
				} else {
					map.put(currId,map.get(currId).add(new BigDecimal(currPrice)));
				}
			}
		}
		return map;
	}
	
	
	
	
	
	
	
	
	

	/**
	 * 
	 * @author jiachen
	 * @DateTime 2015年5月6日 上午11:54:23
	 * @param visaOrderId
	 * @param model
	 * @return String
	 */
	@RequestMapping("applyForm")
	public String applyForm(@RequestParam(value="visaOrderId", required=true)String visaOrderId, Model model) {
		if(StringUtils.isNotBlank(visaOrderId)) {
			//签证订单
			VisaOrder visaOrder = visaOrderService.findVisaOrder(com.trekiz.admin.common.utils.StringUtils.toLong(visaOrderId));
			model.addAttribute("visaOrder", visaOrder);
			model.addAttribute("visaOrderId", visaOrderId);
			
			
			//签证产品
			Long proId = visaOrder.getVisaProductId();
			if(null != proId && 0 != proId) {
				VisaProducts visaProduct = visaProductsService.findByVisaProductsId(proId);
				model.addAttribute("visaProduct", visaProduct);
			}
			
			//游客列表
			List<Traveler> travelerList = visaBorrowMoneyService.getTravelerList(visaOrderId);
			model.addAttribute("travelerList", travelerList);
			
			//币种列表
			List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
			model.addAttribute("currencyList", currencyList);
		}
		return "modules/visa/batchBorrowMoneyForm";
	}
	
	
	
	
	
	
	public void processMenuProcRight_xiaoShou(HttpServletRequest req) {
		Long companyId = UserUtils.getUser().getCompany().getId();

		// 新行者的情况下
		if (companyId.intValue() == 71) {
			req.setAttribute("huanYaJinShouJuFlag", false);
			req.setAttribute("tuiYaJinFlag", false);
			req.setAttribute("jieKuanFlag", true);
		} else {
			req.setAttribute("huanYaJinShouJuFlag", true);
			req.setAttribute("tuiYaJinFlag", true);
			req.setAttribute("jieKuanFlag", false);
		}
	}
	
	public void processMenuProcRight_qianWu(HttpServletRequest req) {
		Long companyId = UserUtils.getUser().getCompany().getId();

		// 新行者的情况下
		if (companyId.intValue() == 71) {
			req.setAttribute("huanYaJinShouJuFlag", true);
			req.setAttribute("tuiYaJinFlag", true);
			req.setAttribute("jieKuanFlag", false);
		} else {
			req.setAttribute("huanYaJinShouJuFlag", false);
			req.setAttribute("tuiYaJinFlag", false);
			req.setAttribute("jieKuanFlag", true);
		}
	}
	
	/***
	 * 签务身份管理签证订单
	 * 查询需要签收的资料
	 * visaId:visa表的主键
	 * visaProductId:签证产品表的主键id
	 */     
	@ResponseBody
	@RequestMapping(value={"searchDatum"})
	public Object searchDatum(HttpServletRequest request){
		//签证产品表的id
		String visaProductId = request.getParameter("visaProductId");
		//visa表的id
		String visaId = request.getParameter("visaId");
		Map<String, Object> map = new HashMap<String, Object>();
		List<List<OriginalProjectForm>> originalProjectList = visaOrderService.searchDatum(visaProductId,visaId);
		if(null != originalProjectList && originalProjectList.size()>0){
			map.put("yuanjian", originalProjectList.get(0));
		}else{
			map.put("yuanjian", "");
		}
		if(null != originalProjectList && originalProjectList.size()>1){
			map.put("fuyinjian", originalProjectList.get(1));
		}else{
			map.put("fuyinjian", "");
		}
		return map;
	}
	
	/***
	 * 签务身份管理签证订单
	 * 为游客修改已签收资料
	 * visaId:visa表的主键id
	 */   
	@ResponseBody
	@RequestMapping(value={"signDatum"})
	public Object signDatum(HttpServletRequest request){
		//visa表的主键id
		String visaId = request.getParameter("visaId");
		//获取修改后的原件
		String yuanValue = request.getParameter("yuanValue");
		//获取修改后的复印件
		String fuyinValue = request.getParameter("fuyinValue");
		Map<String, Object> map = new HashMap<String, Object>();
		String result = visaOrderService.updateOrder(fuyinValue,yuanValue,visaId);
		if(null != result && !"".equals(result)){
			map.put("message", result);
		}else{
			map.put("message", "设置成功");
		}
		return map;
	}
	
	
	/**
	 * 根据主订单ID和订单类型获取游客签证信息  
	 * @param orderId
	 * @param orderType
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="searchTravelerByMainOrderId/{orderId}/{orderType}")
	public String searchTravelerByMainOrderId(@PathVariable String orderId, @PathVariable String orderType,
				Model model, HttpServletRequest request) {
		
		List<VisaOrderTravelerResultForm> list = Lists.newArrayList();
		
//		List<VisaOrder> visaOrderList = visaOrderService.findOrdersByMainId(Integer.parseInt(orderId));
//		if(CollectionUtils.isNotEmpty(visaOrderList)) {
//			for(VisaOrder order : visaOrderList) {
//				List<VisaOrderTravelerResultForm> tempList = visaOrderService.searchTravelerByOrder(order.getId().toString());
//				list.addAll(tempList);
//			}
//		}
		
		List<Dict> visaStatusList = visaOrderService.findDictByType("visa_status");	
		
		orderType = OrderCommonUtil.getChineseOrderType(orderType);
		model.addAttribute("mainOrderId",orderId);
	    model.addAttribute("orderType",orderType);
	    model.addAttribute("travelers",list);
	    model.addAttribute("visaStatusList",visaStatusList);
	    return "modules/order/visaInfo";
	}
	
	/***
	 * 签务身份管理签证订单
	 * 为游客修改应收押金
	 * visaId:visa表的主键id
	 */   
	@ResponseBody
	@RequestMapping(value={"searchDeposit"})
	public Object searchDeposit(HttpServletRequest request){
		//visa表的主键id
		String visaId = request.getParameter("visaId");
		Map<String, Object> map = new HashMap<String, Object>();
		map = visaOrderService.searchMoneyAmount(visaId);
		return map;
	}
	
	/***
	 * 签务身份管理签证订单
	 * 为游客修改应收押金
	 * totalDeposit:visa表的应收押金的uuid
	 */   
	@ResponseBody
	@RequestMapping(value={"updateDeposit"})
	public Object updateDeposit(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		//money表中的金额数
		String totalDeposit = request.getParameter("totalDeposit");
		String visaId = request.getParameter("visaId");
		//visa表的应收押金的uuid
		String uuid = request.getParameter("uuid");
		if(null  == totalDeposit || null == uuid){
			map.put("message", "押金修改失败,参数错误");
			return map;
		}
		String result = visaOrderService.updateMoneyAmount(totalDeposit.trim(),uuid.trim(),visaId.trim());
		if(null == result || "".equals(result)){
			map.put("message", "押金修改成功");
		}else{
			map.put("message", "押金修改失败");
		}
		return map;
	}
	/***
	 * 签务身份管理签证订单
	 * 为游客设置押金
	 * totalDeposit:应收押金的值
	 * visaid:visa表的主键
	 */   
	@ResponseBody
	@RequestMapping(value={"addDeposit"})
	public Object addDeposit(HttpServletRequest request){
		//visa表的主键
		String visaId = request.getParameter("visaId");
		//要保存到数据库内的值
		String  totalDeposit = request.getParameter("totalDeposit");
		//币种id
		String  currencyId = request.getParameter("currencyId");
		//游客表的主键
		String travelerId = request.getParameter("travelerId");
		//异常消息
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == visaId || null ==totalDeposit || "".equals(visaId) ||"".equals(totalDeposit) || 
		   null ==totalDeposit || "".equals(totalDeposit)){
			map.put("message", "参数错误,设置失败");
			return map;
		}else{
			List<String> result = visaOrderService.addDeposit(visaId,totalDeposit.trim(),currencyId,travelerId);
			if(null != result && "操作成功".equals(result.get(0))){
				map.put("value", totalDeposit);
				map.put("mark", result.get(1));
				map.put("message", "担保类型修改成功");
				return map;
			}else{
				map.put("message", result.get(0));
				return map;
			}
		}
	}

	@ResponseBody
	@RequestMapping(value={"check"})
	public Map<String, Object> check(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String objs = request.getParameter("objs");
		JSONArray jsonArray = JSONArray.fromObject(objs);

		List<String> list = new ArrayList<String>();
		List<String> list1 = new ArrayList<String>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String travelerId = jsonObject.get("travelerId").toString();
			String traAndOrderId = jsonObject.get("traAndOrderId").toString();
			String[] traordArr = traAndOrderId.split(",");
			String reviewing = guaranteeService.isReviewing(Long.valueOf(travelerId), traordArr[1]);
			if ("yes".equals(reviewing)) {
				list.add(travelerId);
				list1.add(traAndOrderId);
			}
		}
		if (list.size() > 0) {
			String[] str = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				String s = list.get(i);
				str[i] = s;
			}
			List<Traveler> travelerList = travelerService.findByIds(str);


			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			for (int i = 0; i < travelerList.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				Traveler t = travelerList.get(i);
				jsonObj.put("name", t.getName());
				jsonObj.put("traAndOrderId", list1.get(i));
				jsonList.add(jsonObj);
			}

			map.put("jsonList", jsonList);
			map.put("result", "reviewing");
//			、、	forUpdaeOrder.push({"travelerId":_attr[5]+_attr[7],"travelerName":_attr[0]})
			map.put("message", "批量设置担保类型失败");
			return map;
		}
		return map;
	}

	/**
	 * 批量设置担保类型
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"batchUpdateDeposit"})
	public Map<String, Object> batchUpdateDeposit(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		String objs = request.getParameter("objs");
		JSONArray jsonArray = JSONArray.fromObject(objs);

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String travelerId = jsonObject.get("travelerId").toString();
			String traAndOrderId = jsonObject.get("traAndOrderId").toString();
			String[] traordArr = traAndOrderId.split(",");
			String reviewing = guaranteeService.isReviewing(Long.valueOf(travelerId), traordArr[1]);
			if("yes".equals(reviewing)) {
				list.add(travelerId);
			}
		}
		if(list.size() > 0) {
			String[] str = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				String s = list.get(i);
				str[i] = s;
			}
			List<Traveler> travelerList = travelerService.findByIds(str);
			String names = "";
			for(int i = 0; i < travelerList.size(); i++) {
				Traveler t = travelerList.get(i);
				names += t.getName() + " ";
			}
//			model.addAttribute("names", names);
			map.put("names", names);
			map.put("result", "reviewing");
			map.put("message", "批量设置担保类型失败");
			return map;
		}

		String batchNo = sysBatchNoService.getVisaGuaranteeBatchNo();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String travelerId = jsonObject.get("travelerId").toString();
			String visaId = jsonObject.get("visaId").toString();
			String currencyId = jsonObject.get("currencyId").toString();
			String amount = jsonObject.get("amount").toString();
			String guarantee = jsonObject.get("guarantee").toString();

			if (StringUtils.isNotBlank(travelerId) && StringUtils.isNotBlank(visaId) && StringUtils.isNotBlank(guarantee)) {
				visaOrderService.addDeposit(visaId, amount, currencyId, travelerId);
				visaService.updateGuaranteeStatus(visaId, guarantee);

				visaOrderService.saveBatchInfo(i, travelerId, batchNo);

				map.put("result", "success");
				map.put("message", "批量设置担保类型成功");
			} else {
				map.put("result", "failure");
				map.put("message", "批量设置担保类型失败");
			}
		}
		return map;
	}


	/***
	 * 签务身份管理签证订单
	 * 更新游客信息
	 * type:aa         		AA码
			start      		实际出团时间
			end        		实际约签时间
			visa_status		签证状态
			pass_status		护照状态
			guarantee_type担保类型
	 * 
	 */   
	@ResponseBody
	@RequestMapping(value={"updateTraveler"})
	public Object updateTraveler(HttpServletRequest request){
		
		//要保存到数据库内的值
		//AA码
		//String aa = request.getParameter("aa");
		//签证状态
		String visaStatus = request.getParameter("visaStatus");
		//护照状态
		String passstatus = request.getParameter("passstatus");
		//担保状态
		String guaranteeStatus = request.getParameter("guaranteeStatus");
		//游客表的主键
		String travelerId = request.getParameter("travelerId");
		//visa表的主键
		String visaId = request.getParameter("visaId");
		//实际出团时间
		String startOut = request.getParameter("startOut");
		//实际约签时间
		String contract = request.getParameter("contract");
		//实际送签时间
		String deliveryTime = request.getParameter("deliveryTime");
        
		//异常消息
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == travelerId || null == visaId){
			map.put("message", "参数错误,更新失败");
			return map;
		}else{


			if(null == deliveryTime){
				String updateResult =  visaOrderService.updateTraveler1(visaStatus,passstatus,guaranteeStatus,travelerId,visaId,startOut,contract);
				map.put("message", updateResult);
			}else{
				String updateResult =  visaOrderService.updateTraveler(visaStatus,passstatus,guaranteeStatus,travelerId,visaId,startOut,contract,deliveryTime);
				map.put("message", updateResult);
			}
					

			return map;
		}
	}
	
	/***
	 * 签务身份管理签证订单
	 * 批量更新签证状态
	 * type:visa_status 签证状态
	 */
	@ResponseBody
	@RequestMapping(value={"batchUpdateVisaStatus"})
	public Object batchUpdateVisaStatus(HttpServletRequest request){

		//要保存到数据库内的值
		//签证状态
		String visaStatus = request.getParameter("visaStatus");
		//visa表的主键
		String visaIds = request.getParameter("visaIds");

		//异常消息
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == visaIds){
			map.put("message", "参数错误,更新失败");
			return map;
		}else{
			String updateResult =  visaOrderService.batchUpdateVisaStatus(visaIds, visaStatus);
			map.put("message", updateResult);
			return map;
		}
	}
	
	/**
	 * 批量操作记录-借护照-提交
	 */
	@ResponseBody
	@RequestMapping(value={"update4Submit"})
	public Object update4Submit(HttpServletRequest request){
		String batchNo = request.getParameter("batchNo");
		String visaIds = request.getParameter("visaIds");
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == visaIds){
			map.put("message", "参数错误,更新失败");
			return map;
		}else{
			String updateResult =  visaOrderService.submit4BorrowPassport(batchNo, visaIds);
			map.put("message", updateResult);
			return map;
		}
	}
	/**
	 * 批量操作记录-还护照-提交
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"update4SubmitReturn"})
	public Object update4SubmitReturn(HttpServletRequest request){
		String batchNo = request.getParameter("batchNo");
		String visaIds = request.getParameter("visaIds");
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == visaIds){
			map.put("message", "参数错误,更新失败");
			return map;
		}else{
			String updateResult =  visaOrderService.submit4ReturnPassport(batchNo, visaIds);
			map.put("message", updateResult);
			return map;
		}
	}
	
	
	/**
	 * 批量更新护照状态
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"batchUpdatePassportStatus"})
	public Object batchUpdatePassportStatus(String passportStatus,String travellerIds){
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == travellerIds || ""==travellerIds){
			map.put("message", "参数错误,更新失败");
			return map;
		}else{
			String updateResult =  visaOrderService.batchUpdatePassportStatus(passportStatus, travellerIds);
			map.put("message", updateResult);
			return map;
		}

	}

	/***
	 * 签务身份管理签证订单
	 * 签证类型  签证状态 签证国家  渠道选择 是从数据库中获取
	 * 付款状态 参团状态 固定写在jsp页面中
	 * 查询初始页面
	 * 
	 */
	@RequestMapping(value={"list"})
	public String initQianwuOrederList(HttpServletRequest req,HttpServletResponse response,Model model){
		VisaOrderForm form = new VisaOrderForm();
		searchqw( req,response, form,model);
		//填充初始数据
		//initializationData(req);
		User user = UserUtils.getUser();
		model.addAttribute("companyUUid", user.getCompany().getUuid().trim());
		model.addAttribute("dayangCompanyUuid", Context.DA_YANG_COMPANYUUID);
		model.addAttribute("userId", user.getCompany().getId());
		processMenuProcRight_qianWu(req);
		return "modules/visa/visaOrderQianwu";
	}
	
	/**
	 * 填充页面 各下拉列表数据和初始化数据的方法
	 * 
	 * */
	private void initializationData(HttpServletRequest req){
		//签证类型  
		//从sys_dict表中查找type是new_visa_type的字段,初始化到页面的签证类型下拉列表中
		/**
		 * 300&301根据不同供应商展示不同的签证类型
		 */
		String uuid = UserUtils.getUser().getCompany().getUuid();
		List<Dict> visaTypeLists = visaOrderService.findDictByType("new_visa_type");
		List<Dict> visaTypeList = new ArrayList<Dict>();
		for (Dict dict : visaTypeLists) {
			if(11>=Integer.parseInt(dict.getValue())){
				visaTypeList.add(dict);
			}
			//针对越柬行踪、起航假期 供应商的签证类型的不同显示--wenchao.lv
			//起航假期uuid：5c05dfc65cd24c239cd1528e03965021 12---23
			if("5c05dfc65cd24c239cd1528e03965021".equals(uuid)){
				if(12<=Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=23){
					visaTypeList.add(dict);
				}
			}
			//越柬行踪uuid:7a81b21a77a811e5bc1e000c29cf2586	24-28
			if("7a81b21a77a811e5bc1e000c29cf2586".equals(uuid)){
				if(23<Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=28){
					visaTypeList.add(dict);
				}
			}
			//百乐游uuid：4a39518f8de74baebe6b51efcdd57aa3  29---
			if("4a39518f8de74baebe6b51efcdd57aa3".equals(uuid)){
				if(28<Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=38){
					visaTypeList.add(dict);
				}
			}
			//骡子假期 39-61  0417需求  --djw
			//骡子假期uuid：980e4c74b7684136afd89df7f89b2bee
			if("980e4c74b7684136afd89df7f89b2bee".equals(uuid)){
				if(38<Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=61){
					visaTypeList.add(dict);
				}
			}
			//鼎鸿假期  0391需求 -djw
			//鼎鸿假期uuid：049984365af44db592d1cd529f3008c3  62-64
			if("049984365af44db592d1cd529f3008c3".equals(uuid)){
				if(61<Integer.parseInt(dict.getValue())&&Integer.parseInt(dict.getValue())<=64){
					visaTypeList.add(dict);
				}
			}
		}
		
		/**
		 * 300&301根据不同供应商展示不同的签证类型
		 */
		//签证状态
		//从sys_dict表中查找type是visa_status的字段,初始化到页面的签证状态下拉列表中
		List<Dict> visaStatusList = visaOrderService.findDictByType("visa_status");	
		
		//yun签证国家  智能匹配
		List<Object[]> countryObject = visaProductsService.findCountryInfoList();
		List<Country> countryList = new ArrayList<Country>();
		Country country = null;
		for (Object[] props : countryObject) {
			country = new Country();
			//签证国家id
			country.setId(Long.parseLong(props[0].toString())); 
			//签证国家名称
			country.setCountryName_cn(String.valueOf(props[1]));
			countryList.add(country);
		}
		
		//渠道选择  智能匹配
		List<Agentinfo> agentinfoList = agentinfoService.findAllAgentinfo();
		
		//所有的币种和币种id
		List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		 
		req.setAttribute("currencyList", currencyList);
		req.setAttribute("agentinfoList", agentinfoList);
		req.setAttribute("countryList", countryList);
		req.setAttribute("visaStatusList", visaStatusList);
		req.setAttribute("visaTypeList", visaTypeList);
		//本公司的所有的下过订单的人
		req.setAttribute("createByList", visaOrderService.findCreateBy());
	}
	
	/**
	 * 跳转到到签证修改页面（签务身份）
	 * @param req
	 * @param orderNo订单号
	 * @param orderType参团类型
	 * @return
	 */
	@RequestMapping(value="goUpdateVisaOrder")
	public String goUpdateVisaOrder(HttpServletRequest req,@RequestParam Long visaOrderId,String details ,Model model){
		
		//订单封装
		VisaOrder visaOrder = visaOrderService.findVisaOrder(visaOrderId);
		String totalMoney = moneyAmountService.getMoney(visaOrder.getTotalMoney());
		String payedMoney = moneyAmountService.getMoney(visaOrder.getPayedMoney());
		String accountedMoney = moneyAmountService.getMoney(visaOrder.getAccountedMoney());
		List<Map<String, Object>> invoiceList = visaOrderService.findInvoiceOrReceiptByOrderIdAndFlag(visaOrderId, 0);//发票号集合
		List<Map<String, Object>> receiptList = visaOrderService.findInvoiceOrReceiptByOrderIdAndFlag(visaOrderId, 1);//收据号集合
		
		model.addAttribute("visaCostPriceFlag", UserUtils.getUser().getCompany().getVisaCostPrice());
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("payedMoney", payedMoney);
		model.addAttribute("accountedMoney", accountedMoney);
		model.addAttribute("invoiceList", invoiceList);
		model.addAttribute("receiptList", receiptList);
		
		
		//产品封装
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		Currency currency = currencyService.findCurrency(visaOrder.getProOriginCurrencyId().longValue());
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
		
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("currency", currency);
		model.addAttribute("visaType", visaType);
		model.addAttribute("country", country);
		
		//渠道信息
		if(visaOrder.getAgentinfoId()!=null){
			Agentinfo agentInfo = agentinfoService.findOne(visaOrder.getAgentinfoId());
			if(agentInfo.getId() == -1 ){
				agentInfo.setAgentName(visaOrder.getAgentinfoName());
			}
			model.addAttribute("agentInfo", agentInfo);
		}
		List<OrderContacts> contacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(visaOrderId, 6);
		model.addAttribute("contacts", contacts);
		
		//国家信息
		List<Country> countrys = CountryUtils.getCountrys();
		model.addAttribute("countrys", countrys);
		
		
		
		//游客信息
		List<Traveler> travelers = travelerService.findTravelerByOrderIdAndOrderType(visaOrderId, 6);
		
//		String serialNums = "";
		if(travelers!=null && travelers.size()>0){
			for (int i = 0; i < travelers.size(); i++) {
				Visa visa = visaService.findVisaByTravlerId(travelers.get(i).getId());
				List<Costchange> list = costchangeService.findCostchangeByTravelerId(travelers.get(i).getId());
				if(visa!=null){
					
					if(!StringUtils.isEmpty(visa.getTotalDeposit())){
						visa.setTotalDepositMoney(moneyAmountService.getMoney(visa.getTotalDeposit()));
					}
					
					List<Long> ids = new ArrayList<Long>();
					if(visa.getPassportPhotoId()!=null){
						ids.add(visa.getPassportPhotoId());
					}
					if(visa.getIdentityFrontPhotoId()!=null){
						ids.add(visa.getIdentityFrontPhotoId());
					}
					if(visa.getIdentityBackPhotoId()!=null){
						ids.add(visa.getIdentityBackPhotoId());
					}
					if(visa.getTablePhotoId()!=null){
						ids.add(visa.getTablePhotoId());
					}
					if(visa.getPersonPhotoId()!=null){
						ids.add(visa.getPersonPhotoId());
					}
					if(visa.getOtherPhotoId()!=null){
						ids.add(visa.getOtherPhotoId());
					}
					if(visa.getFamilyRegisterPhotoId()!=null){
						ids.add(visa.getFamilyRegisterPhotoId());
					}
					if(visa.getHouseEvidencePhotoId()!=null){
						ids.add(visa.getHouseEvidencePhotoId());
					}
					if(StringUtils.isNotBlank(visa.getDocIds())){
						String[] array = visa.getDocIds().split(",");
						for(int j=0;j<array.length;j++){
							ids.add(Long.parseLong(array[j]));
						}
					}
					
					List<DocInfo> docs = docInfoService.getDocInfoByIds(ids);
					visa.setDocs(docs);
					if(docs != null && docs.size()>0){
						visa.setDocIds(ids.toString().substring(1,ids.toString().length()-1));
					}
					
//					if(visa.getTotalDeposit()!=null && !"".equals(visa.getTotalDeposit())){
//						serialNums = serialNums+"'"+visa.getTotalDeposit()+"',";
//					}
//					if(visa.getAccountedDeposit()!=null && !"".equals(visa.getAccountedDeposit())){
//						serialNums = serialNums+"'"+visa.getAccountedDeposit()+"',";
//					}
					
				}
				
				String payPriceSerialNumInfo = moneyAmountService.getMoney(travelers.get(i).getPayPriceSerialNum());
				travelers.get(i).setPayPriceSerialNumInfo(payPriceSerialNumInfo);
				
				//wxw added  --------处理是否可发起申请---------
				travelers.get(i).setVisa(visa);
				travelers.get(i).setCostChange(list);
				
				//综合考虑新旧审核情况   20151222 wangxinwei modified
				String borrowMoneyandCheckStatus = visaOrderService.getTravelerBorrowedMoney(travelers.get(i).getOrderId()+"", travelers.get(i).getId());//旧审核
				if(null==borrowMoneyandCheckStatus){
					borrowMoneyandCheckStatus = visaOrderService.getTravelerBorrowedMoney4Activiti(travelers.get(i).getOrderId()+"", travelers.get(i).getId());//新审核
				}
				
				
				if (borrowMoneyandCheckStatus!=null) {
					travelers.get(i).setBorrowMoney(borrowMoneyandCheckStatus.split(",")[0]);
					travelers.get(i).setBorrowMoneyCheckStatus(borrowMoneyandCheckStatus.split(",")[1]);
				}
				
			}
		} 
//		if(serialNums.length()>0){
//			String totalDeposit = moneyAmountService.getMoneys(serialNums.substring(0,serialNums.length()-1));
//			model.addAttribute("totalDeposit", totalDeposit);
//		}else{
//			model.addAttribute("totalDeposit", "¥ 0.00");
//		}
		String totalDeposit =moneyAmountService.getMoneys(visaOrderId.toString());
		model.addAttribute("totalDeposit", totalDeposit);
		model.addAttribute("travelers", travelers);
		
		//支付信息
		List<Orderpay> orderPays =  orderPayService.getOrderPayByOrderIdAndOrderType(visaOrderId, 6);
		if(orderPays!=null && orderPays.size()>0){
			for (int i = 0; i < orderPays.size(); i++) {
				if (StringUtils.isNotBlank(orderPays.get(i).getOrderPaySerialNum())) {
					orderPays.get(i).setMoneyAmount(moneyAmountService.getOrderMoney(orderPays.get(i).getOrderPaySerialNum()));
					// 使用orderpay的备注字段remarks来保存游客名称
					orderPays.get(i).setRemarks(orderPayService.getOrderPayTravelerNames(orderPays.get(i).getOrderPaySerialNum()));
					//break;//用break有问题,改成continue-tgy
//					continue;
				} else { // 收押金无批量号orderPaySerialNum，直接取其travelerId字段，用备注字段remarks存储游客名称
					orderPays.get(i).setMoneyAmount(moneyAmountService.getMoney(orderPays.get(i).getMoneySerialNum()));
					String travelerName = travelerDao.findOne(orderPays.get(i).getTravelerId()).getName();
					orderPays.get(i).setRemarks(travelerName);
				}
				
			}
		}
		model.addAttribute("orderPays", orderPays);
		//签务身份
		model.addAttribute("shenfen", "qianwu");
		
		//其他费用
		@SuppressWarnings("rawtypes")
        List costs = costchangeService.findCostChangeByOrderId(visaOrderId);
		model.addAttribute("costs", costs);
		
//		if(mainOrderCode!=null && mainOrderCode>0){
//			参团签证订单 暂时屏蔽
//			ProductOrderCommon productOrder = orderCommonService.getProductorderById(mainOrderCode);
//			ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
//			model.addAttribute("productOrder", productOrder);
//			model.addAttribute("activityGroup", activityGroup);
//			
//		}else{
//			//单办签证订单
//			
//		}
	//---------------------------为了获得预订渠道的信息,C346&C406-------------------tgy-start	
		//渠道商信息列表
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
        //从ordercontacts表中获取个订单下联系人的信息	
		
	//---------------------------为了获得预订渠道的信息,C346&C406-------------------tgy-end	
		
		if(!StringUtils.isEmpty(details) && "1".equals(details)){
			
			model.addAttribute("flag", "/visa/order/list");
			//订单详情页
			return "modules/visa/visaOrderDetails";
		}else{
			//订单修改页
			return "modules/visa/visaOrderUpdate";
		}
	}
	
	/**
	 * 跳转到到签证修改页面（销售身份）
	 * @param req
	 * @param orderNo订单号
	 * @param orderType参团类型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    @RequestMapping(value="goUpdateVisaOrderForSales")
	public String goUpdateVisaOrderForSales(HttpServletRequest req,@RequestParam Long visaOrderId,String details ,Model model){

		//订单封装
		VisaOrder visaOrder = visaOrderService.findVisaOrder(visaOrderId);
		String totalMoney = moneyAmountService.getMoney(visaOrder.getTotalMoney());
		String costTotalMoney = moneyAmountService.getMoney(visaOrder.getCostTotalMoney());//for c333
		String payedMoney = moneyAmountService.getMoney(visaOrder.getPayedMoney());
		String accountedMoney = moneyAmountService.getMoney(visaOrder.getAccountedMoney());
		List<Map<String, Object>> invoiceList = visaOrderService.findInvoiceOrReceiptByOrderIdAndFlag(visaOrderId, 0);//发票号集合
		List<Map<String, Object>> receiptList = visaOrderService.findInvoiceOrReceiptByOrderIdAndFlag(visaOrderId, 1);//收据号集合
		
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("costTotalMoney", costTotalMoney);//for c333
		model.addAttribute("payedMoney", payedMoney);
		model.addAttribute("accountedMoney", accountedMoney);
		model.addAttribute("visaCostPriceFlag", UserUtils.getUser().getCompany().getVisaCostPrice());
		model.addAttribute("invoiceList", invoiceList);
		model.addAttribute("receiptList", receiptList);
		
		
		//产品封装
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		Currency currency = currencyService.findCurrency(visaOrder.getProOriginCurrencyId().longValue());
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
		
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("currency", currency);
		model.addAttribute("visaType", visaType);
		model.addAttribute("country", country);
		
		//渠道信息
		Agentinfo agentInfo = agentinfoService.findOne(visaOrder.getAgentinfoId());
		if(visaOrder.getAgentinfoId()!=null){
			
			if(agentInfo.getId() == -1){
				agentInfo.setAgentName(visaOrder.getAgentinfoName());
			}
			model.addAttribute("agentInfo", agentInfo);
		}
		//此订单销售负责的渠道List
		model.addAttribute("agentList", agentinfoService.findAgentBySalerId(visaOrder.getSalerId().longValue()));
		
		List<OrderContacts> contacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(visaOrderId, 6);
		model.addAttribute("contacts", contacts);
		
		//国家信息
		List<Country> countrys = CountryUtils.getCountrys();
		model.addAttribute("countrys", countrys);
		
		//游客信息
		List<Traveler> travelers = travelerService.findTravelerByOrderIdAndOrderType(visaOrderId, 6);
		String serialNums = "";
		Map<String, Object> map = new HashMap<>();
		if(travelers!=null && travelers.size()>0){
			for (int i = 0; i < travelers.size(); i++) {
				Visa visa = visaService.findVisaByTravlerId(travelers.get(i).getId());
				List<Costchange> list = costchangeService.findCostchangeByTravelerId(travelers.get(i).getId());
				if(visa!=null){
					if(!StringUtils.isEmpty(visa.getTotalDeposit())){
						visa.setTotalDepositMoney(moneyAmountService.getMoney(visa.getTotalDeposit()));
					}
					if(visa.getPassportPhotoId()!=null){
						visa.setPassportPhoto(docInfoService.getDocInfo(visa.getPassportPhotoId()));
					}
					if(visa.getIdentityFrontPhotoId()!=null){
						visa.setIdentityFrontPhoto(docInfoService.getDocInfo(visa.getIdentityFrontPhotoId()));
					}
					if(visa.getIdentityBackPhotoId()!=null){
						visa.setIdentityBackPhoto(docInfoService.getDocInfo(visa.getIdentityBackPhotoId()));
					}
					if(visa.getTablePhotoId()!=null){
						visa.setTablePhoto(docInfoService.getDocInfo(visa.getTablePhotoId()));
					}
					if(visa.getPersonPhotoId()!=null){
						visa.setPersonPhoto(docInfoService.getDocInfo(visa.getPersonPhotoId()));
					}
					if(visa.getOtherPhotoId()!=null){
						visa.setOtherPhoto(docInfoService.getDocInfo(visa.getOtherPhotoId()));
					}
					//户口本ID
					if(visa.getFamilyRegisterPhotoId()!=null){
						visa.setFamilyRegisterPhoto(docInfoService.getDocInfo(visa.getFamilyRegisterPhotoId()));
					}
					//房产证ID
					if(visa.getHouseEvidencePhotoId()!=null) {
						visa.setHouseEvidencePhoto(docInfoService.getDocInfo(visa.getHouseEvidencePhotoId()));
					}
					//签证附件
					if(visa.getDocIds()!=null){
						visa.setDocs(docInfoService.getDocInfoBydocids(visa.getDocIds()));
					}
//					if(visa.getTotalDeposit()!=null && !"".equals(visa.getTotalDeposit())){
//						serialNums = serialNums+"'"+visa.getTotalDeposit()+"',";
//					}
					
					if(visa.getAccountedDeposit()!=null && !"".equals(visa.getAccountedDeposit())){
						serialNums = serialNums+"'"+visa.getAccountedDeposit()+"',";
					}
				}
				
				List<Currency> currencies = moneyAmountService.getMoneyNew(travelers.get(i).getPayPriceSerialNum());
				for(Currency currency2 : currencies)
				{
					if(map.get(currency2.getCurrencyMark()) == null)
					map.put(currency2.getCurrencyMark(), currency2.getConvertCash());
					else
					 map.put(currency2.getCurrencyMark(), currency2.getConvertCash().add((BigDecimal) map.get(currency2.getCurrencyMark())))	;
				}
				
				travelers.get(i).setCurrencies(currencies); // currencies
				//wxw added  --------处理是否可发起申请---------
				travelers.get(i).setVisa(visa);
				travelers.get(i).setCostChange(list);
				String borrowMoneyandCheckStatus = visaOrderService.getTravelerBorrowedMoney(travelers.get(i).getOrderId()+"", travelers.get(i).getId());
				if (borrowMoneyandCheckStatus!=null) {
					travelers.get(i).setBorrowMoney(borrowMoneyandCheckStatus.split(",")[0]);
					travelers.get(i).setBorrowMoneyCheckStatus(borrowMoneyandCheckStatus.split(",")[1]);
				}
				
				//wxw added 处理游客返佣显示
				String rebatesMoneySerialNum = travelers.get(i).getRebatesMoneySerialNum();
				MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(rebatesMoneySerialNum);
				if (null!=moneyAmount) {
					travelers.get(i).setRebatesCurrencyID(moneyAmount.getCurrencyId()+"");
					travelers.get(i).setRebatesAmount(moneyAmount.getAmount().toString());
				}
				
			}
		} 
		
		//wxw added 处理游客返佣显示
		List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("currencyList4Rebates", currencyList);
		
		//订单预计团队返佣
		if(StringUtils.isNotBlank(visaOrder.getGroupRebate())){
			MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(visaOrder.getGroupRebate());
			if (null!=moneyAmount) {
				if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO) != 0){
					model.addAttribute("groupRebatesCurrency", moneyAmount.getCurrencyId());
					model.addAttribute("groupRebatesMoney", moneyAmount.getAmount().toString());
				}
			}
		}
		
		model.addAttribute("totalFinalPirce", getString(map));
		String totalDeposit =moneyAmountService.getMoneys(visaOrderId.toString());
		model.addAttribute("totalDeposit", totalDeposit);
		model.addAttribute("travelers", travelers);
		
		//支付信息
		List<Orderpay> orderPays =  orderPayService.getOrderPayByOrderIdAndOrderType(visaOrderId, 6);
		
		if(orderPays!=null && orderPays.size()>0){
			for (int i = 0; i < orderPays.size(); i++) {
				String orderPaySerialNum = orderPays.get(i).getOrderPaySerialNum();
				if(StringUtils.isNotBlank(orderPaySerialNum)){
					orderPays.get(i).setMoneyAmount(moneyAmountService.getOrderMoney(orderPaySerialNum));
					// 使用orderpay的备注字段remarks来保存游客名称
					orderPays.get(i).setRemarks(orderPayService.getOrderPayTravelerNames(orderPaySerialNum));
					//break;//不应该用break,改成continue-tgy
					// continue;
				}else {
					// 收押金无批量号orderPaySerialNum，直接取其travelerId字段，用备注字段remarks存储游客名称
					orderPays.get(i).setMoneyAmount(moneyAmountService.getMoney(orderPays.get(i).getMoneySerialNum()));
					String travelerName = travelerDao.findOne(orderPays.get(i).getTravelerId()).getName();
					orderPays.get(i).setRemarks(travelerName);
				}

			}
		}
		model.addAttribute("orderPays", orderPays);
		
		//其他费用
		List costs = costchangeService.findCostChangeByOrderId(visaOrderId);
		model.addAttribute("costs", costs);
		setCurrencyModel(model);
		

		
				//订单是否允许添加多个渠道联系人信息
				Integer allowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();
				model.addAttribute("allowAddAgentInfo", allowAddAgentInfo);
				//订单是否允许渠道联系人信息输入和修改
				Integer allowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();
				model.addAttribute("allowModifyAgentInfo", allowModifyAgentInfo);
				// 是否允许订单预定渠道修改
				Integer isAllowModify = UserUtils.getUser().getCompany().getIsAllowModify();
				model.addAttribute("isAllowModify", isAllowModify);

		List<OrderContacts> orderContacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(visaOrder.getId(), 6);
		if (CollectionUtils.isEmpty(orderContacts)) {
			orderContacts.add(new OrderContacts());
		}
		model.addAttribute("orderContactsSrc", orderContacts);  //保存的是最原始的联系人---供非签约渠道订单修改使用
//		for (OrderContacts orderContact : orderContacts) {
//			orderContact.setContactsAddress(agentinfoService.getAddressStrById(agentInfo.getId()));
//		}
		if (CollectionUtils.isNotEmpty(orderContacts)) {
			model.addAttribute("orderContacts", orderContacts);
			net.sf.json.JSONArray currencyListJsonArray = net.sf.json.JSONArray.fromObject(orderContacts);
	        model.addAttribute("orderContactsListJsonArray", currencyListJsonArray.toString());
		}
		
		/**
		 * 获取护照类型信息
		 * wangxinwei added,2015-11-23,相应需求号c333
		 * 
		 */
        Map<String,String> passportTypeList = DictUtils.getVisaTypeMap(Context.PASSPORT_TYPE);
        model.addAttribute("passportTypeList", passportTypeList);
		
        //销售身份
      	model.addAttribute("shenfen", "xiaoshou");
		
		// 公司ID
		model.addAttribute("companyId", UserUtils.getUser().getCompany().getId());
		if(!StringUtils.isEmpty(details) && "1".equals(details)){
			//订单详情页
			model.addAttribute("sales", 1);
			return "modules/visa/visaOrderDetails";
		}else{
			
		   /**
		    * 20151221  wangxinwie   modified
		    * 修改   c333需求的   中  的业务  与  订单中游客参团冲突的问题
		    * 在 游客   列表中 添加： isjoingroup，以区别  参团游客  与  子订单游客  
		    */
           int issubject = visaOrderService.judgeOrderIdIsSubOrder(visaOrder);
           model.addAttribute("issubject", issubject);
         //0318新增是否允许修改销售签证订单下的游客信息--s//
           model.addAttribute("isAllowModifyXSVisaOrder",UserUtils.getUser().getCompany().getIsAllowModifyXSVisaOrder());
         //0318新增是否允许修改销售签证订单下的游客信息--e//
			//订单修改页
			return "modules/visa/visaOrderUpdateForSales";
		}
	}
	
	/**
	 * 修改签证信息
	 * @param req
	 * @param visa
	 * @param model
	 * @return
	 */
	@RequestMapping(value="doUpdateVisaOrder")
	@ResponseBody
	public String doUpdateVisaOrder(HttpServletRequest req,
			Visa visa,  Model model){
		String orderId = req.getParameter("orderId");
		//traveler 544 修改游客信息  签发地
		if(UserUtils.getUser().getCompany().getUuid().equals("980e4c74b7684136afd89df7f89b2bee")){
			String issuePlace1 = req.getParameter("issuePlace1");
			String travelerId = req.getParameter("travelerId");
			if(StringUtil.isNotBlank(travelerId)){
				Traveler traveler = travelerService.findTravelerById(new Long(travelerId));
				traveler.setIssuePlace1(issuePlace1);
				travelerService.saveTraveler(traveler);
			}
		}
		visaService.updateVisaOrder(visa, orderId);
		return "true";
	}
	
	/**
	 * 修改签证特殊备注信息
	 * @param req
	 * @return
	 */
	@RequestMapping(value="doUpdateVisaOrderRemark")
	@ResponseBody
	public String doUpdateVisaOrderRemark(HttpServletRequest req){
		String remark = req.getParameter("remark");
		String orderId = req.getParameter("orderId");
		visaOrderService.doUpdateVisaOrderRemark( orderId,remark);
		return "true";
	}
	
	
	/**
	 * 销售身份签证修改页面（销售身份），保存游客及签证
	 * @param req
	 * @param traveler
	 * @param model
	 * @return
	 */
	@RequestMapping(value="saveOrUpdateTraveler",method=RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateTraveler(HttpServletRequest req,
			Traveler traveler,Long srcPriceCur,Visa visa,Long[] currency,String[] cosName,BigDecimal[] sum,  Model model){
		if(srcPriceCur != null){
			traveler.setSrcPriceCurrency(currencyService.findCurrency(srcPriceCur));
		}
		
		//wxw added for 游客返佣修改
		String orderId = req.getParameter("orderId");
		String visaTravelerRebateCurrencyId =req.getParameter("rebatesCurrencyID");
		String visaTravelerRebateAmount  =req.getParameter("rebatesAmount");
		String inputClearPrice = req.getParameter("inputClearPrice");//结算价
		//---0211需求,新增预计回团时间,限定为批发商星徽四海-s--//
		if("0e19ac500f78483d8a9f4bb768608629".equals(UserUtils.getUser().getCompany().getUuid())){
			String travelerVisaId=req.getParameter("travelerVisaId") ;//获得该游客的签证id
			String forecastBackDate=req.getParameter("forecastBackDate");//获得该游客的预计归团时间 
			 //更新该游客的预计归团时间
			 String flag4update=visaService.updateForecastBackDateById(travelerVisaId,forecastBackDate);
			 if("false"==flag4update){ //如果更新预计归团时间失败,则返回"false"
				 return "false";
			 }
			//System.out.println("-----"+travelerVisaId);
		}
		//---0211需求,新增预计回团时间,限定为批发商星徽四海-e--//
		traveler.setRebatesAmount(visaTravelerRebateAmount);
		traveler.setRebatesCurrencyID(visaTravelerRebateCurrencyId);
		
		Long travelerId = travelerService.saveOrUpdateTravelerForSalers(traveler, visa, currency, cosName, sum, orderId, inputClearPrice);
		model.addAttribute("travelerId", travelerId);
		return "true";
	}
	
	/**
	 * 借护照，还护照修改相关数据
	 * @param req
	 * @param visa
	 * @param passportStatus
	 * @return
	 */
	@RequestMapping(value="updatePassportStatus")
	@ResponseBody
	public String updatePassportStatus(HttpServletRequest req, Visa visa,@RequestParam Integer passportStatus){
		if(passportStatus!=null){
			if(passportStatus==1){
				visa.setPassportOperateType(1);
			}else{
				visa.setPassportOperateType(2);
			}
			visaOrderService.updatePassportStatus(visa, passportStatus);
		}
		String travelerId=req.getParameter("travelerId");
		return travelerId;
	}
	
	
	
	/**
	 * 根据参团的团号或订单编号,下载面签通知
	 * @param id 参团的团号或是订单编号
	 * @param type: cantuan:代表的是根据参团的团号,下载面签通知
	 * 			    dingdan:代表的是根据签证订单编号,下载面签通知
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@RequestMapping(value="interviewNotice")
	public ResponseEntity<byte[]> downloadInterviewNotice(@RequestParam String id,
			@RequestParam String type,
			HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{
		//面签通知文件列表
		List<File> fileList = new ArrayList<File>();
		if(null == id || null == type || "".equals(id) || "".equals(type) ){
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter pw=response.getWriter();
			pw.println("没有面签通知!");
			return null;
		}
		List<Long> longArray = new ArrayList<Long>();
		if("cantuan".equals(type)){
			//根据参团团号获得所有签证订单编号数组
			longArray = visaOrderService.getOrderIdByGroupCode(id);
		}else{
			//根据订单id获得签证面签通知的文件列表
			longArray.add(Long.valueOf(id));
		}
		
		if(null != longArray && longArray.size()>0){
			//循环数组,获取每个签证订单下的面签通知
			//将单个订单的面签通知文件列表,汇总 到总的面签通知文件列表中,
			for(int i=0;i<longArray.size();i++){
				List<File> tempFileList = visaInterviewNoticeService.mianqiantongzhiByOrderId(longArray.get(i));
				if(null != tempFileList && tempFileList.size()>0){
					for(int j=0;j<tempFileList.size();j++){
						fileList.add(tempFileList.get(j));
					}
				}
			}
		}
		if(null != fileList && fileList.size()>0){
		try {
			//将文件列表变成压缩包,进行下载
			DownLoadController.downLoadFiles(fileList, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}else{
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter pw=response.getWriter();
			pw.println("没有面签通知!");
			
		}
		return null;
	}
	/**
	 * 下载面签通知
	 * @param orderId
	 * @param travelerId
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@RequestMapping(value="downloadInterviewNotice")
	public void downloadInterviewNotice(@RequestParam Long orderId, @RequestParam Long travelerId,HttpServletResponse response) throws IOException, TemplateException{
		File downFile = visaInterviewNoticeService.createInterviewNoticeFile(orderId, travelerId);
		
    	OutputStream os = null;
    	try {
			if(downFile != null && downFile.exists()){
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(downFile.getName().getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
		    	os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(downFile));
	            os.flush();
			}else{
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter pw=response.getWriter();
				pw.println("没有面签通知!");
			}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(os!=null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
	}
	
	/**
	 * 签证订单导出游客信息
	 * @param visaOrderId
	 * @param agentId
	 * @param groupCode
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="downloadTraveler")
	public void downloadTraveler(@RequestParam Long visaOrderId, @RequestParam Long agentId, @RequestParam String groupCode, HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<Object[]> travelerList = travelerService.findTravelerByVisaOrderId(visaOrderId, agentId);
		if(travelerList != null && travelerList.size() > 0) {
			int i = 0;
			for(Object[] o : travelerList) {
				i++;
				o[0] = i;
				String temp = null;
				if(o[6] != null && o[6].toString().length() >= 1) {
					temp = o[6].toString();
					temp = temp.replace("1", "身份证").replace("2", "护照").
											replace("3", "警官证").replace("4", "军官证").
											replace("5", "其他").replace(",", "/");
					if(temp.lastIndexOf("/") == (temp.length()-1)) {
						temp = temp.substring(0,temp.length()-1);
					}
					o[6] = temp;
				}
				if(o[7] != null && o[7].toString().length() >= 1) {
					temp = o[7].toString();
					temp = temp.replace(",", "/");
					if(temp.lastIndexOf("/") == (temp.length()-1)) {
						temp = temp.substring(0,temp.length()-1);
					}
					o[7] = temp;
				}
			}
			String fileName = groupCode + "-游客信息";
			//Excel各行名称
			String[] cellTitle =  {"序号","中文姓名","英文姓名","性别","出生日期","出生地","证件类型","证件号码","有效日期","签发地","电话号码","渠道名字","渠道跟进销售","备注"};
			//文件首行标题
			String firstTitle = groupCode;
			ExportExcel.createExcle(fileName, travelerList, cellTitle, firstTitle, request, response);
		}
	}
	
	/**
	 * 游客明细表下载
	 * @param visaOrderId
	 * @param serialNum
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="downloadTravelerDetails")
	public void downloadTravelerDetails(@RequestParam Long visaOrderId, @RequestParam String serialNum, HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<Map<String,Object>> travelerMap = travelerService.findTravelerdetails(visaOrderId,serialNum);
		
		if(travelerMap != null && travelerMap.size() > 0) {
			int i = 0;
			for(Map<String,Object> map : travelerMap) {
				i++;
				map.put("rowNum", i);
				if(map.get("id") != null && !"".equals(map.get("id"))){
					Traveler traveler = travelerDao.findById(Long.valueOf(map.get("id").toString()));
					//获取订单信息
					VisaOrder visaOrder = visaOrderDao.findOne(traveler.getOrderId());
					//下单人
					if(visaOrder.getCreateBy() != null)
					map.put("orderCreateBy", visaOrder.getCreateBy().getName());
				}		
			}
			List<Object[]> travelerInfoList = new ArrayList<Object[]>();
			for(int j = 0; j < travelerMap.size();  j++){
				Object[] o = new Object[5];
				//序号
				o[0] = travelerMap.get(j).get("rowNum");
				//游客姓名
				o[1] = travelerMap.get(j).get("name");
				//订单号
				o[2] = travelerMap.get(j).get("orderNum");
				//下单人
				o[3] = travelerMap.get(j).get("orderCreateBy");
				//付款金额
				o[4] = travelerMap.get(j).get("payedMoney");
				travelerInfoList.add(o);
			}
			String fileName ="游客明细";
			//Excel各行名称
			String[] cellTitle =  {"序号","游客姓名","订单号","下单人","收款金额"};
			//文件首行标题
			String firstTitle = travelerMap.get(0).get("orderNum").toString();
			try {
				ExportExcel.createExcle(fileName, travelerInfoList, cellTitle, firstTitle, request, response);
			} catch (Exception e) {
				new Exception("导出游客信息时发生错误");
			}
		}
	}
	
	/**
	 * 预约表
	 * @author jiachen
	 * @DateTime 2014-12-16 上午11:10:56
	 * @return String
	 */
	@RequestMapping("orderTable")
	public String orderTable(@RequestParam(value="orderId", required=true)String orderId, Model model) {
		model.addAttribute("orderTables", visaOrderService.findOrderTableByOrderId(orderId));
		return "modules/visa/orderTable";
	}
	
	
	/**
     *  获取币种相关信息
	 *
	 *  @author xinwei.wang
	 *  @DateTime 2014-11-15 上午10:33:00
	 *  @param model
     */
	private void setCurrencyModel(Model model){
	    //获取币种信息
        List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
        StringBuilder sbBuilder = new StringBuilder();
        
        for(Currency curr:currencyList){
        	sbBuilder.append("<option value=\"").append(curr.getId()).append("\">").append(curr.getCurrencyName()).append("</option>");
        }
        model.addAttribute("currencyList", sbBuilder.toString());
        JsonConfig currencyconfig = new JsonConfig();
        currencyconfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
				if (arg1.equals("id") || arg1.equals("currencyName")|| arg1.equals("currencyMark")) {
					return false;
				} else {
					return true;
				}
			}
		});
        net.sf.json.JSONArray currencyListJsonArray = net.sf.json.JSONArray.fromObject(currencyList, currencyconfig);
        model.addAttribute("currencyListJsonArray", currencyListJsonArray.toString());
	}

	
	
	 @RequestMapping(value = "batchUpdatePassport")
	 @ResponseBody
	 public Object batchUpdatePassport(HttpServletRequest request,Visa visa,@RequestParam Integer passportStatus) {
	        String visaIds = request.getParameter("visaIds")+"0";
	        String travellerIds = request.getParameter("travellerIds")+"0";
	        String others = request.getParameter("passportOperateRemark")+"0";
	        String dates = request.getParameter("passportOperateTime")+"0";
	        String pessons = request.getParameter("passportOperator")+"0";
	        String deflag = request.getParameter("deflag");
	                
	        Map<String, String> map  = visaOrderService.batchUpdatePassport(others,dates,pessons, passportStatus,visaIds,travellerIds,Integer.parseInt(deflag));
	        return map;
	    }
	 
	 @RequestMapping(value = "batchUpdatePassport4jhz")
	 @ResponseBody
	 public Object batchUpdatePassport4jhz(HttpServletRequest request,Visa visa,@RequestParam Integer passportStatus) {
		 String visaIds = request.getParameter("visaIds")+"0";
	        String travellerIds = request.getParameter("travellerIds")+"0";
	        String others = request.getParameter("passportOperateRemark")+"0";
	        String dates = request.getParameter("passportOperateTime")+"0";
	        String pessons = request.getParameter("passportOperator")+"0";
	        String batchNo = request.getParameter("batchNo");
	        Map<String, String> map  =visaOrderService.batchUpdatePassport4jhz(others, dates, pessons, passportStatus, visaIds, travellerIds,batchNo);
	        return map;
	 }
	 
	 
	 @RequestMapping(value = "checkBatchBorrowPassport")
	 @ResponseBody
	 public Object checkBatchBorrowPassport(HttpServletRequest request){
		  
		String visaIds = request.getParameter("visaIds")+"0";
        String travellerIds = request.getParameter("travellerIds")+"0";
        Map<String, Object> map  = visaOrderService.checkBatchBorrowPassport(visaIds,travellerIds);
        return map;
		 
	 }
	 
	 /**
	  * 检查游客是否满足删除条件
	  * @param request
	  * @author ang.gao 20150806
	  * @return
	  */
	 @RequestMapping(value = "check4DeleteTravelers")
	 @ResponseBody
	 public Object check4DeleteTravelers(HttpServletRequest request){
		  
        String travelerIds = request.getParameter("travelerIds")+"0";
        Map<String, Object> map  = visaOrderService.check4DeleteTravelers(travelerIds);
        return map;	 
	 }

	 
	 /**
	  * 取消正在审核中的流程并删除游客-提示框信息
	  * @param request
	  * @return
	  */
	 @RequestMapping(value = "cancelReviewAndDelete")
	 @ResponseBody
	 public Object cancelReviewAndDelete(HttpServletRequest request){
		  
        String travelerIds = request.getParameter("travelerIds")+"0";
        Map<String, Object> map  = visaOrderService.cancelReviewAndDelete(travelerIds);
        return map;	 
	 }
	 
	 /**
	  * 删除游客
	  * @param request
	  * @return
	  */
	 @RequestMapping(value = "doDelete")
	 @ResponseBody
	 public Object doDelete(HttpServletRequest request){
		  
        String travelerIds = request.getParameter("travelerIds")+"0";
        String tids = request.getParameter("tids");
        Map<String, Object> map  = visaOrderService.doDelete(travelerIds,tids);
        return map;	 
	 }
	 
	 @RequestMapping(value = "checkBatchHsj")
	 @ResponseBody
	 public Object checkBatchHsj(HttpServletRequest request){
//		 String orderId = request.getParameter("orderId") ;
		    String visaIds = request.getParameter("visaIds")+"0";
	        String travellerIds = request.getParameter("travellerIds")+"0";
	        Map<String, Object> map  = visaOrderService.checkBatchHsj(visaIds,travellerIds);
	        return map;
	 }
	 
	@RequestMapping(value = "checkBatchHsjHqx")
	@ResponseBody
	public Object checkBatchHsjHqx(HttpServletRequest request){
		String visaIds = request.getParameter("visaIds")+"0";
		String travellerIds = request.getParameter("travelerIds")+"0";
		Map<String, Object> map  = visaOrderService.checkBatchHsjHqx(visaIds,travellerIds);
		return map;
	}
	 
	/**
	 * 批量还收据列表（环球行）
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "batchHsj")
	@ResponseBody
	public Object batchHsj(HttpServletRequest req){
		Map<String, Object> resultMap = null;
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 环球行的情况下
		if (companyId.intValue() == 68) {
			String type = req.getParameter("type");
			String visaIds = req.getParameter("visaIds")+"0";
			String orderIds = req.getParameter("orderIds")+"0";
			String travelerIds = req.getParameter("travellerIds")+"0";
			String returnReceiptJes = req.getParameter("returnReceiptJe")+"0";
			String returnReceiptNames = req.getParameter("returnReceiptName")+"0";
			String returnReceiptTimes = req.getParameter("returnReceiptTime")+"0";
			String returnReceiptRemarks = req.getParameter("returnReceiptRemark")+"0";
			
			String[] travelerIDS = travelerIds.split(",");
			String[] visaIDs = visaIds.split(",");
			String[] orderIDs = orderIds.split(",");
			String[] jes = returnReceiptJes.split(",");
			String[] remarks = returnReceiptRemarks.split(",");
			
			// 生成批次号
			String batchNo = sysBatchNoService.getVisaReturnReceiptBatchNo();
			// 批次总人数
			int batchPersonCount = travelerIDS.length - 1;
			// 批次总金额
			BigDecimal batchtotalMoney = BigDecimal.ZERO;
			for (int i = 0; i < travelerIDS.length - 1; i++) {
				//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {
					batchtotalMoney = batchtotalMoney.add(new BigDecimal(jes[i]));
				}
			}
			//获取人民币币种id
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
			buffer.append(" AND c.create_company_id=");
			buffer.append(UserUtils.getUser().getCompany().getId());
			List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
			Integer borrowtotalcurrencyId = 0;
			String borrowtotalcurrencyName="人民币";
			for (int i = 0; i < currencylist.size(); i++) {
				if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
					borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
					borrowtotalcurrencyName = (String)currencylist.get(i).get("currency_name");
					break;
				}
			}
			//生成uuid
			String uuid = UUID.randomUUID().toString();
	
			if ("2".equals(type)) {
//				try{
					resultMap=visaReturnReceiptService.visaBatchHsj(batchNo,visaIds,travelerIds,returnReceiptJes,returnReceiptNames,returnReceiptTimes,returnReceiptRemarks);
//				}catch(Exception e){
//					e.printStackTrace();
//					if(resultMap==null){
//						resultMap = new HashMap<String,Object>();
//					}
//					resultMap.put("msg", "签证批量还收据申请失败！");
//					return resultMap;
//				}
			}
			
			VisaFlowBatchOpration record = new VisaFlowBatchOpration();
			record.setUuid(uuid);
			record.setBatchNo(batchNo);
			record.setBusynessType("1");
			record.setBatchPersonCount(batchPersonCount);
			record.setBatchTotalMoney(batchtotalMoney.toString());
			record.setCreateUserId(UserUtils.getUser().getId());
			record.setCreateUserName(UserUtils.getUser().getName());
			record.setCreateTime(new Date());
			record.setPrintStatus("0");
			record.setCurrencyId(borrowtotalcurrencyId);
			record.setCurrencyName(borrowtotalcurrencyName);
			if ("1".equals(type)) {
				record.setReviewStatus("99");
				record.setIsSubmit("1");
			} else if ("2".equals(type)) {
				record.setReviewStatus("1");
				record.setIsSubmit("2");
			}
	
			batchRecordDao.getSession().save(record);
	
			for(int i = 0; i < travelerIDS.length - 1; i++){
				//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {
					uuid = UUID.randomUUID().toString();
					BatchTravelerRelation relation = new BatchTravelerRelation();
					relation.setUuid(uuid);
					relation.setBatchUuid(record.getUuid());
					relation.setBatchRecordNo(batchNo);
					relation.setTravelerId(Long.parseLong(travelerIDS[i]));
					relation.setVisaId(Long.parseLong(visaIDs[i]));
					relation.setOrderId(Long.parseLong(orderIDs[i]));
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
					relation.setTravelerName(traveler.getName());
					relation.setBusinessType(2);//业务类型 1:借款 2：还收据  3：借护照 4：还护照
					relation.setTravellerBorrowMoney(new BigDecimal(jes[i]));
					relation.setRemark(remarks[i]);
					relation.setCreatebyId(UserUtils.getUser().getId());
					relation.setCreatebyName(UserUtils.getUser().getName());
					relation.setIsSubmit("1");
					relation.setSaveTime(new Date());
					if ("2".equals(type)) {
						relation.setSubmitbyId(UserUtils.getUser().getId());
						relation.setSubmitbyName(UserUtils.getUser().getName());
						relation.setIsSubmit("2");
						relation.setSubmitTime(new Date());
					}
	
					batchTravelerRelationDao.getSession().save(relation);
				}
			}
		}
		return resultMap;
	}
	 
	 @RequestMapping(value = "checkBatchReturnPassport")
	 @ResponseBody
	 public Object checkBatchReturnPassport(HttpServletRequest req){
		 String visaIds = req.getParameter("visaIds")+"0";
		 String travellerIds = req.getParameter("travellerIds")+"0";
		 Map<String, Object> map=visaOrderService.checkBatchReturnPassport(visaIds,travellerIds);
		 return map;
	 }
	 
	 @RequestMapping(value = "checkBatchJk")
	 @ResponseBody
	 public Object checkBatchJk(HttpServletRequest request){
		    String visaIds = request.getParameter("visaIds")+"0";
	        String travellerIds = request.getParameter("travellerIds")+"0";
	        Map<String, Object> resultMap = null;
//	        try{
	        	 resultMap  = visaOrderService.checkBatchJk(visaIds,travellerIds);
//			 }catch(Exception e){
//				 e.printStackTrace();
//				 if(resultMap==null){
//					 resultMap = new HashMap<String,Object>();
//				 }
//				 resultMap.put("msg", e.getMessage());
//				 
//			 }
	        return resultMap;
	 }
	 
	@RequestMapping(value = "checkBatchJkHqx")
	@ResponseBody
	public Object checkBatchJkHqx(HttpServletRequest request){
		String visaIds = request.getParameter("visaIds")+"0";
		String travelerIds = request.getParameter("travelerIds")+"0";
		Map<String, Object> resultMap = null;
//		try{
			resultMap  = visaOrderService.checkBatchJkHqx(visaIds,travelerIds);
//		}catch(Exception e){
//			e.printStackTrace();
//			if(resultMap==null){
//				resultMap = new HashMap<String,Object>();
//			}
//			resultMap.put("msg", e.getMessage());
//		}
		return resultMap;
	}
	 
	@RequestMapping(value = "batchJk")
	@ResponseBody
	public Object batchJk(HttpServletRequest req){
		Map<String, Object> resultMap = null;
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 环球行的情况下
		if (companyId.intValue() == 68) {
			String type = req.getParameter("type");
			String visaIds = req.getParameter("visaIds")+"0";
			String orderIds = req.getParameter("orderIds")+"0";
			String travelerIds = req.getParameter("travelerIds")+"0";
			String persons = req.getParameter("passportOperator")+"0";
			String dates = req.getParameter("passportOperateTime")+"0";
			String moneys = req.getParameter("moneys")+"0";
			String others = req.getParameter("passportOperateRemark")+"0";
	
			String[] travelerIDS = travelerIds.split(",");
			String[] visaIDs = visaIds.split(",");
			String[] orderIDs = orderIds.split(",");
			String[] borrowAmounts = moneys.split(",");
			String[] borrowRemarks = others.split(",");
	
			// 生成批次号
			String batchNo = sysBatchNoService.getVisaBorrowMoneyBatchNo();
			// 批次总人数
			int batchPersonCount = travelerIDS.length - 1;
			// 批次总金额
			BigDecimal batchtotalMoney = BigDecimal.ZERO;
			for (int i = 0; i < travelerIDS.length - 1; i++) {
				//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {
					batchtotalMoney = batchtotalMoney.add(new BigDecimal(borrowAmounts[i]));
				}
			}
			//获取人民币币种id
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
			buffer.append(" AND c.create_company_id=");
			buffer.append(UserUtils.getUser().getCompany().getId());
			List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
			Integer borrowtotalcurrencyId = 0;
			String borrowtotalcurrencyName="人民币";
			for (int i = 0; i < currencylist.size(); i++) {
				if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
					borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
					borrowtotalcurrencyName = (String)currencylist.get(i).get("currency_name");
					break;
				}
			}
			//生成uuid
			String uuid = UUID.randomUUID().toString();
	
			if ("2".equals(type)) {
//				try{
					resultMap=visaBorrowMoneyService.visaBatchJk(batchNo,visaIds,travelerIds,persons,dates,moneys,others);
//				}catch(Exception e){
//					e.printStackTrace();
//					if(resultMap==null){
//						resultMap = new HashMap<String,Object>();
//					}
//					resultMap.put("msg", "签证批量借款申请失败！");
//					return resultMap;
//				}
			}
			
			VisaFlowBatchOpration record = new VisaFlowBatchOpration();
			record.setUuid(uuid);
			record.setBatchNo(batchNo);
			record.setBusynessType("2");
			record.setBatchPersonCount(batchPersonCount);
			record.setBatchTotalMoney(batchtotalMoney.toString());
			record.setCreateUserId(UserUtils.getUser().getId());
			record.setCreateUserName(UserUtils.getUser().getName());
			record.setCreateTime(new Date());
			record.setPrintStatus("0");
			record.setCurrencyId(borrowtotalcurrencyId);
			record.setCurrencyName(borrowtotalcurrencyName);
			if ("1".equals(type)) {
				record.setReviewStatus("99");
				record.setIsSubmit("1");
			} else if ("2".equals(type)) {
				record.setReviewStatus("1");
				record.setIsSubmit("2");
			}
	
			batchRecordDao.getSession().save(record);
	
			for(int i = 0; i < travelerIDS.length - 1; i++){
				//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {
					uuid = UUID.randomUUID().toString();
					BatchTravelerRelation relation = new BatchTravelerRelation();
					relation.setUuid(uuid);
					relation.setBatchUuid(record.getUuid());
					relation.setBatchRecordNo(batchNo);
					relation.setTravelerId(Long.parseLong(travelerIDS[i]));
					relation.setVisaId(Long.parseLong(visaIDs[i]));
					relation.setOrderId(Long.parseLong(orderIDs[i]));
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
					relation.setTravelerName(traveler.getName());
					relation.setBusinessType(1);//业务类型 1:借款 2：还收据  3：借护照 4：还护照
					relation.setTravellerBorrowMoney(new BigDecimal(borrowAmounts[i]));
					relation.setRemark(borrowRemarks[i]);
					relation.setCreatebyId(UserUtils.getUser().getId());
					relation.setCreatebyName(UserUtils.getUser().getName());
					relation.setIsSubmit("1");
					relation.setSaveTime(new Date());
					if ("2".equals(type)) {
						relation.setSubmitbyId(UserUtils.getUser().getId());
						relation.setSubmitbyName(UserUtils.getUser().getName());
						relation.setIsSubmit("2");
						relation.setSubmitTime(new Date());
					}
	
					batchTravelerRelationDao.getSession().save(relation);
				}
			}
		}
		return resultMap;
	}

	@RequestMapping(value = "batchJkSubmit")
	@ResponseBody
	@Transactional
	public Object batchJkSubmit(HttpServletRequest req,String batchNo){
		
		Map<String, Object> resultMap = null;
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 环球行的情况下
		if (companyId.intValue() == 68) {
			
			// 调新伟接口
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT GROUP_CONCAT(bt.visa_id) visaIds,GROUP_CONCAT(bt.traveler_id) travelerIds,GROUP_CONCAT(bt.createby_name) persons,GROUP_CONCAT(date_format(bt.save_time,'%Y-%m-%d')) dates,GROUP_CONCAT(bt.traveller_borrow_money) moneys,GROUP_CONCAT(bt.remark) others");
			buffer.append(" FROM batch_traveler_relation bt WHERE bt.batch_record_no = '"+batchNo+"' AND bt.business_type = '1';");
			List<Map<String, Object>> batchTravelerRelationList = batchTravelerRelationDao.findBySql(buffer.toString(), Map.class);
			if (batchTravelerRelationList != null && batchTravelerRelationList.size() > 0) {
				for (int i = 0; i < batchTravelerRelationList.size(); i++) {
					String visaIds = batchTravelerRelationList.get(i).get("visaIds").toString();
					String travelerIds = batchTravelerRelationList.get(i).get("travelerIds").toString();
					String persons = batchTravelerRelationList.get(i).get("persons").toString();
					String dates = batchTravelerRelationList.get(i).get("dates").toString();
					String moneys = batchTravelerRelationList.get(i).get("moneys").toString();
					String others = batchTravelerRelationList.get(i).get("others").toString();
					try{
						resultMap=visaBorrowMoneyService.visaBatchJk(batchNo,visaIds,travelerIds,persons,dates,moneys,others);
					}catch(Exception e){
						e.printStackTrace();
						if(resultMap==null){
							resultMap = new HashMap<String,Object>();
						}
						resultMap.put("msg", "签证批量借款提交失败！");
					}
				}
			}
			
			// 更新批次游客关系表表状态
			List<BatchTravelerRelation> relationList = batchTravelerRelationDao.findByBatchNo(batchNo, 1);
			if (relationList != null && relationList.size() > 0) {
				for (int i = 0; i < relationList.size(); i++) {
					BatchTravelerRelation relation = relationList.get(i);
					relation.setSubmitbyId(UserUtils.getUser().getId());
					relation.setSubmitbyName(UserUtils.getUser().getName());
					relation.setIsSubmit("2");
					relation.setSubmitTime(new Date());
					try{
						batchTravelerRelationDao.getSession().save(relation);
					}catch(Exception e){
						e.printStackTrace();
						if(resultMap==null){
							resultMap = new HashMap<String,Object>();
						}
						resultMap.put("msg", "签证批量借款提交失败！");
					}
				}
			}
			
			// 更新签证批量操作表状态
			VisaFlowBatchOpration record = visaFlowBatchOprationDao.findByBatchNo(batchNo,"2");
			if (record != null) {
				record.setReviewStatus("1");
				record.setIsSubmit("2");
				try{
					visaFlowBatchOprationDao.getSession().save(record);
				}catch(Exception e){
					e.printStackTrace();
					if(resultMap==null){
						resultMap = new HashMap<String,Object>();
					}
					resultMap.put("msg", "签证批量借款提交失败！");
				}
			}
		}
		return resultMap;
	}
	
	
	/**
	 * 
	 * ----------------综合处理新旧审核的  借款状态问题----------------
	 * 
	 * 
	 * 处理bug  13178   放开  @Transactional
	 * 
	 * @Description: 借款新接口
	 * @author xinwei.wang
	 * @date 2015年12月25日下午7:56:36
	 * @param req
	 * @param batchNo
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "batchJkSubmit4Activiti")
	@ResponseBody
	@Transactional
	public Object batchJkSubmit4Activiti(HttpServletRequest req,String batchNo){
		
		Map<String, Object> resultMap = null;
		
//		Long companyId = UserUtils.getUser().getCompany().getId();
		// 环球行的情况下
//		if (companyId.intValue() == 68||companyId.intValue() == 69) {
			
			// 调新伟接口
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT GROUP_CONCAT(bt.visa_id) visaIds,GROUP_CONCAT(bt.traveler_id) travelerIds,GROUP_CONCAT(bt.createby_name) persons,GROUP_CONCAT(date_format(bt.save_time,'%Y-%m-%d')) dates,GROUP_CONCAT(bt.traveller_borrow_money) moneys,GROUP_CONCAT(bt.remark) others");
			buffer.append(" FROM batch_traveler_relation bt WHERE bt.batch_record_no = '"+batchNo+"' AND bt.business_type = '1';");
			List<Map<String, Object>> batchTravelerRelationList = batchTravelerRelationDao.findBySql(buffer.toString(), Map.class);
			if (batchTravelerRelationList != null && batchTravelerRelationList.size() > 0) {
				for (int i = 0; i < batchTravelerRelationList.size(); i++) {
					String visaIds = batchTravelerRelationList.get(i).get("visaIds").toString();
					String travelerIds = batchTravelerRelationList.get(i).get("travelerIds").toString();
					String persons = batchTravelerRelationList.get(i).get("persons").toString();
					String dates = batchTravelerRelationList.get(i).get("dates").toString();
					String moneys = batchTravelerRelationList.get(i).get("moneys").toString();
					String others = batchTravelerRelationList.get(i).get("others").toString();
					try{
						//resultMap=visaBorrowMoneyService.visaBatchJk(batchNo,visaIds,travelerIds,persons,dates,moneys,others);
						resultMap=activityVisaHQXBorrowMoneyService.visaBatchJk4activiti(batchNo,visaIds,travelerIds,persons,dates,moneys,others);
						
					}catch(Exception e){
						e.printStackTrace();
						if(resultMap==null){
							resultMap = new HashMap<String,Object>();
						}
						 resultMap.put("msg", "流程启动失败！");
					}
				}
			}
			
			// 更新批次游客关系表表状态
			List<BatchTravelerRelation> relationList = batchTravelerRelationDao.findByBatchNo(batchNo, 1);
			if (relationList != null && relationList.size() > 0) {
				for (int i = 0; i < relationList.size(); i++) {
					BatchTravelerRelation relation = relationList.get(i);
					relation.setSubmitbyId(UserUtils.getUser().getId());
					relation.setSubmitbyName(UserUtils.getUser().getName());
					relation.setIsSubmit("2");
					relation.setSubmitTime(new Date());
					try{
						batchTravelerRelationDao.getSession().save(relation);
					}catch(Exception e){
						e.printStackTrace();
						if(resultMap==null){
							resultMap = new HashMap<String,Object>();
						}
						resultMap.put("msg", "签证批量借款提交失败！");
					}
				}
			}
			
			// 更新签证批量操作表状态
			VisaFlowBatchOpration record = visaFlowBatchOprationDao.findByBatchNo(batchNo,"2");
			if (record != null) {
				record.setReviewStatus("1");
				record.setIsSubmit("2");
				try{
					visaFlowBatchOprationDao.getSession().save(record);
				}catch(Exception e){
					e.printStackTrace();
					if(resultMap==null){
						resultMap = new HashMap<String,Object>();
					}
					resultMap.put("msg", "签证批量借款提交失败！");
				}
			}
//		}else {
//			if(resultMap==null){
//				resultMap = new HashMap<String,Object>();
//			}
//			resultMap.put("msg", "签证批量借款提交失败,该功能没有开启！");
//		}
		
		return resultMap;
	}
	
	
	@RequestMapping(value = "batchHsjSubmit")
	@ResponseBody
	@Transactional
	public Object batchHsjSubmit(HttpServletRequest req,String batchNo){
		
		Map<String, Object> resultMap = null;
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 环球行的情况下
		if (companyId.intValue() == 68) {
			
			// 调新伟接口
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT GROUP_CONCAT(bt.visa_id) visaIds,GROUP_CONCAT(bt.traveler_id) travelerIds,GROUP_CONCAT(bt.createby_name) persons,GROUP_CONCAT(date_format(bt.save_time,'%Y-%m-%d')) dates,GROUP_CONCAT(bt.traveller_borrow_money) moneys,GROUP_CONCAT(bt.remark) others");
			buffer.append(" FROM batch_traveler_relation bt WHERE bt.batch_record_no = '"+batchNo+"' AND bt.business_type = '2';");
			List<Map<String, Object>> batchTravelerRelationList = batchTravelerRelationDao.findBySql(buffer.toString(), Map.class);
			if (batchTravelerRelationList != null && batchTravelerRelationList.size() > 0) {
				for (int i = 0; i < batchTravelerRelationList.size(); i++) {
					String visaIds = batchTravelerRelationList.get(i).get("visaIds").toString();
					String travelerIds = batchTravelerRelationList.get(i).get("travelerIds").toString();
					String returnReceiptNames = batchTravelerRelationList.get(i).get("persons").toString();
					String returnReceiptTimes = batchTravelerRelationList.get(i).get("dates").toString();
					String returnReceiptJes = batchTravelerRelationList.get(i).get("moneys").toString();
					String returnReceiptRemarks = batchTravelerRelationList.get(i).get("others").toString();
					try{
						resultMap=visaReturnReceiptService.visaBatchHsj(batchNo,visaIds,travelerIds,returnReceiptJes,returnReceiptNames,returnReceiptTimes,returnReceiptRemarks);
					}catch(Exception e){
						e.printStackTrace();
						if(resultMap==null){
							resultMap = new HashMap<String,Object>();
						}
						resultMap.put("msg", "签证批量还收据提交失败！");
					}
				}
			}
	
			// 更新批次游客关系表表状态
			List<BatchTravelerRelation> relationList = batchTravelerRelationDao.findByBatchNo(batchNo, 2);
			if (relationList != null && relationList.size() > 0) {
				for (int i = 0; i < relationList.size(); i++) {
					BatchTravelerRelation relation = relationList.get(i);
					relation.setSubmitbyId(UserUtils.getUser().getId());
					relation.setSubmitbyName(UserUtils.getUser().getName());
					relation.setIsSubmit("2");
					relation.setSubmitTime(new Date());
					try{
						batchTravelerRelationDao.getSession().save(relation);
					}catch(Exception e){
						e.printStackTrace();
						if(resultMap==null){
							resultMap = new HashMap<String,Object>();
						}
						resultMap.put("msg", "签证批量还收据提交失败！");
					}
				}
			}
			
			// 更新签证批量操作表状态
			VisaFlowBatchOpration record = visaFlowBatchOprationDao.findByBatchNo(batchNo,"1");
			if (record != null) {
				record.setReviewStatus("1");
				record.setIsSubmit("2");
				try{
					visaFlowBatchOprationDao.getSession().save(record);
				}catch(Exception e){
					e.printStackTrace();
					if(resultMap==null){
						resultMap = new HashMap<String,Object>();
					}
					resultMap.put("msg", "签证批量还收据提交失败！");
				}
			}
		}
		return resultMap;
	}
	
	
	/**
	 * 
	 *  ----------------综合处理新旧审核的  借款状态问题----------------
	 * 
	 * @Description: 新审核接口
	 * @author xinwei.wang
	 * @date 2015年12月25日下午6:05:12
	 * @param req
	 * @param batchNo
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "batchHsjSubmit4Activiti")
	@ResponseBody
	@Transactional
	public Object batchHsjSubmit4Activiti(HttpServletRequest req,String batchNo){
		
		Map<String, Object> resultMap = null;
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 环球行的情况下
		if (companyId.intValue() == 68) {
			
			// 调新伟接口
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT GROUP_CONCAT(bt.visa_id) visaIds,GROUP_CONCAT(bt.traveler_id) travelerIds,GROUP_CONCAT(bt.createby_name) persons,GROUP_CONCAT(date_format(bt.save_time,'%Y-%m-%d')) dates,GROUP_CONCAT(bt.traveller_borrow_money) moneys,GROUP_CONCAT(bt.remark) others");
			buffer.append(" FROM batch_traveler_relation bt WHERE bt.batch_record_no = '"+batchNo+"' AND bt.business_type = '2';");
			List<Map<String, Object>> batchTravelerRelationList = batchTravelerRelationDao.findBySql(buffer.toString(), Map.class);
			if (batchTravelerRelationList != null && batchTravelerRelationList.size() > 0) {
				for (int i = 0; i < batchTravelerRelationList.size(); i++) {
					String visaIds = batchTravelerRelationList.get(i).get("visaIds").toString();
					String travelerIds = batchTravelerRelationList.get(i).get("travelerIds").toString();
					String returnReceiptNames = batchTravelerRelationList.get(i).get("persons").toString();
					String returnReceiptTimes = batchTravelerRelationList.get(i).get("dates").toString();
					String returnReceiptJes = batchTravelerRelationList.get(i).get("moneys").toString();
					String returnReceiptRemarks = batchTravelerRelationList.get(i).get("others").toString();
					
					//----------------综合处理新旧审核的  借款状态问题-----------------
					String borrowamounts = null;
					String[] travelerIdArray = travelerIds.split(",");
			        
					StringBuilder borrowamountSb = new StringBuilder();
					List<Traveler> travelers = travelerService.findByIds(travelerIdArray);
					for (Traveler traveler : travelers) {
						
						String borrowAmountAndMoney = visaOrderService.getTravelerBorrowedMoney4Activiti(traveler.getOrderId().toString(),traveler.getId());
						if (null!=borrowAmountAndMoney) {
							borrowamountSb.append(borrowAmountAndMoney.split(",")[0]).append(",");
						}
						
					}
					borrowamounts = borrowamountSb.toString().replaceAll(",$", "");
					
					try{
						//visaBatchHsj4activiti(String batchNo, String visaIds,String travellerIds, String returnReceiptJe,String returnReceiptName, String returnReceiptTime,String returnReceiptRemark,String borrowamount);
						               //resultMap=visaReturnReceiptService.visaBatchHsj(batchNo,visaIds,travelerIds,returnReceiptJes,returnReceiptNames,returnReceiptTimes,returnReceiptRemarks);
						resultMap=activityVisaReturnReceiptService.visaBatchHsj4activiti(batchNo,visaIds,travelerIds,returnReceiptJes,returnReceiptNames,returnReceiptTimes,returnReceiptRemarks,borrowamounts);
						
					}catch(Exception e){
						e.printStackTrace();
						if(resultMap==null){
							resultMap = new HashMap<String,Object>();
						}
						resultMap.put("msg", "签证批量还收据提交失败！");
					}
				}
			}
	
			// 更新批次游客关系表表状态
			List<BatchTravelerRelation> relationList = batchTravelerRelationDao.findByBatchNo(batchNo, 2);
			if (relationList != null && relationList.size() > 0) {
				for (int i = 0; i < relationList.size(); i++) {
					BatchTravelerRelation relation = relationList.get(i);
					relation.setSubmitbyId(UserUtils.getUser().getId());
					relation.setSubmitbyName(UserUtils.getUser().getName());
					relation.setIsSubmit("2");
					relation.setSubmitTime(new Date());
					try{
						batchTravelerRelationDao.getSession().save(relation);
					}catch(Exception e){
						e.printStackTrace();
						if(resultMap==null){
							resultMap = new HashMap<String,Object>();
						}
						resultMap.put("msg", "签证批量还收据提交失败！");
					}
				}
			}
			
			// 更新签证批量操作表状态
			VisaFlowBatchOpration record = visaFlowBatchOprationDao.findByBatchNo(batchNo,"1");
			if (record != null) {
				record.setReviewStatus("1");
				record.setIsSubmit("2");
				try{
					visaFlowBatchOprationDao.getSession().save(record);
				}catch(Exception e){
					e.printStackTrace();
					if(resultMap==null){
						resultMap = new HashMap<String,Object>();
					}
					resultMap.put("msg", "签证批量还收据提交失败！");
				}
			}
		}
		return resultMap;
	}
	
	
	
	
	/**
	 * 
	 * @param req
	 * @param batchNo
	 * @param tid
	 * @return
	 */
	@RequestMapping(value = "travelerDelete")
	@ResponseBody
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Object travelerDelete(HttpServletRequest req,String batchNo,Long tid){
		Map<String, Object> resultMap = new HashMap<String,Object>();
		try{
			batchTravelerRelationDao.deleteTraveler(batchNo, tid);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("msg", "游客删除失败！");
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "jkTravelerDelete")
	@ResponseBody
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Object jkTravelerDelete(HttpServletRequest req,String batchNo,Long tid){
		
		Map<String, Object> resultMap = null;
		
		// 更新签证批量操作表 游客人数和借款总额两个字段
		// 根据batchNo,tid查询删除游客借款金额
		BatchTravelerRelation relation = batchTravelerRelationDao.findByBatchNoAndTid(batchNo,tid,1);
		VisaFlowBatchOpration record = visaFlowBatchOprationDao.findByBatchNo(batchNo,"2");
		if (record != null) {
			// 人数-1
			int batchPersonCount = record.getBatchPersonCount()-1;
			// 重新计算借款总额
			BigDecimal batchTotalMoney = new BigDecimal(record.getBatchTotalMoney());
			BigDecimal travellerBorrowMoney = relation.getTravellerBorrowMoney();
			record.setBatchPersonCount(batchPersonCount);
			record.setUpdateTime(new Date());
			record.setBatchTotalMoney(batchTotalMoney.subtract(travellerBorrowMoney).toString());
//			try{
				visaFlowBatchOprationDao.getSession().save(record);
//			}catch(Exception e){
//				e.printStackTrace();
//				if(resultMap==null){
//					resultMap = new HashMap<String,Object>();
//				}
//				resultMap.put("msg", "游客删除失败！");
//			}
		}
//		try{
			batchTravelerRelationDao.deleteByBatchNoAndTid(batchNo,tid,1);
//		}catch(Exception e){
//			e.printStackTrace();
//			if(resultMap==null){
//				resultMap = new HashMap<String,Object>();
//			}
//			resultMap.put("msg", "游客删除失败！");
//		}
			//-----订单->签务签证订单->批量操作记录->游客列表->删除---更新批次表visa_flow_batch_opration的更新时间-s//
			 //根据批次号和业务类型索引表visa_flow_batch_opration
			/*visaFlowBatchOprationDao.clear(); //消除缓存影响 
			int tempCount=record.getBatchPersonCount()-1;//更新人数,由于上述的人数-1未知原因不生效,所以这里更新,bug#13766
			StringBuffer sb=new StringBuffer();
              sb.append(" UPDATE visa_flow_batch_opration vfbo ")
                .append(" SET vfbo.update_time=SYSDATE(), ")//更新时间去系统当前时间
                .append(" vfbo.batch_person_count="+tempCount+" ") 
                .append(" WHERE vfbo.batch_no = '"+batchNo+"' AND vfbo.busyness_type = 2 ");
				visaFlowBatchOprationDao.updateBySql(sb.toString());*/
			//-----订单->签务签证订单->批量操作记录->游客列表->删除---更新批次表visa_flow_batch_opration的更新时间-e//	
		return resultMap;
	}
	
	@RequestMapping(value = "hsjTravelerDelete")
	@ResponseBody
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Object hsjTravelerDelete(HttpServletRequest req,String batchNo,Long tid){
		
		Map<String, Object> resultMap = null;
		
		// 更新签证批量操作表 游客人数和借款总额两个字段
		// 根据batchNo,tid查询删除游客借款金额
		BatchTravelerRelation relation = batchTravelerRelationDao.findByBatchNoAndTid(batchNo,tid,2);
		VisaFlowBatchOpration record = visaFlowBatchOprationDao.findByBatchNo(batchNo,"1");
		if (record != null) {
			// 人数-1
			int batchPersonCount = record.getBatchPersonCount()-1;
			// 重新计算借款总额
			BigDecimal batchTotalMoney = new BigDecimal(record.getBatchTotalMoney());
			BigDecimal travellerBorrowMoney = relation.getTravellerBorrowMoney();
			record.setBatchPersonCount(batchPersonCount);
			record.setBatchTotalMoney(batchTotalMoney.subtract(travellerBorrowMoney).toString());
//			try{
				visaFlowBatchOprationDao.getSession().save(record);
//			}catch(Exception e){
//				e.printStackTrace();
//				if(resultMap==null){
//					resultMap = new HashMap<String,Object>();
//				}
//				resultMap.put("msg", "游客删除失败！");
//			}
		}
//		try{
			batchTravelerRelationDao.deleteByBatchNoAndTid(batchNo,tid,2);
//		}catch(Exception e){
//			e.printStackTrace();
//			if(resultMap==null){
//				resultMap = new HashMap<String,Object>();
//			}
//			resultMap.put("msg", "游客删除失败！");
//		}
		return resultMap;
	}
	
	/**
	 * 环球行批量借款游客信息
	 * @param batchNo
	 * @return
	 */
	@RequestMapping(value="getBatchJkTravelerList")
	@ResponseBody
	public List<Map<String,Object>> getBatchJkTravelerList(HttpServletRequest request,HttpServletResponse response){
		String batchNo = request.getParameter("batchNo");
		Map<String,String> condition = new HashMap<String,String>();
		condition.put("batchNo", batchNo);
		List<Map<String,Object>> traveler = visaOrderService.findBatchJkTraveler(condition);
		return traveler;
	}

	/**
	 * 批量担保--游客信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getBatchGuaTravelerList")
	@ResponseBody
	public List<Map<String,Object>> getBatchGuaTravelerList(HttpServletRequest request,HttpServletResponse response){
		String batchNo = request.getParameter("batchNo");
		Map<String,String> condition = new HashMap<String,String>();
		condition.put("batchNo", batchNo);
		List<Map<String,Object>> traveler = visaOrderService.findBatchGuaTraveler(condition);
		return traveler;
	}
	
	/**
	 * 环球行批量还收据游客信息
	 * @param batchNo
	 * @return
	 */
	@RequestMapping(value="getBatchHsjTravelerList")
	@ResponseBody
	public List<Map<String,Object>> getBatchHsjTravelerList(HttpServletRequest request,HttpServletResponse response){
		String batchNo = request.getParameter("batchNo");
		Map<String,String> condition = new HashMap<String,String>();
		condition.put("batchNo", batchNo);
		List<Map<String,Object>> traveler = visaOrderService.findBatchHsjTraveler(condition);
		return traveler;
	}
	
	 /**
	  * 批量借护照 展示
	  * @param req
	  * @param res
	  * @return
	  */
	 @RequestMapping(value="batchReturnPassportList")
	 public String batchReturnPassportList(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, String> condition = new HashMap<String, String>();
		String  batchNo         = request.getParameter("batchNo");
		String  txnPerson       = request.getParameter("txnPerson");
		String  createTimeStart = request.getParameter("createTimeStart");
		String  createTimeEnt   = request.getParameter("createTimeEnt");
		String  travellerName   = request.getParameter("travellerName");
		//订单列表排序
		String orderBy = request.getParameter("orderBy");
		condition.put("batchNo", batchNo        );
		condition.put("txnPerson", txnPerson      );
		condition.put("createTimeStart", createTimeStart);
		condition.put("createTimeEnt", createTimeEnt  );
		condition.put("travellerName", travellerName  );
		condition.put("orderBy", orderBy  );
		Page<Map<String,Object>> page = visaOrderService.batchReturnPassportList(request,response,condition);
		String pageStr = page.toString();
		pageStr = pageStr.replace("<div style=\"clear:both;\"></div>", "");
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("page", page);
		model.addAttribute("count",page.getCount());		
		model.addAllAttributes(condition);
		return "modules/visa/batchReturnPassportList";
	 }
	 
	 
	 /**
	  * 输出借护照页面游客信息
	  * @param batchNo
	  * @return
	  */
	 @RequestMapping(value="getBorrowPassPortTravelerList")
	 @ResponseBody
	 public List<Map<String,Object>> getTravelerList(HttpServletRequest request,HttpServletResponse response){
		 String batchNo = request.getParameter("batchNo");
		 String visaIds = request.getParameter("visaIds");
		 Map<String,String> condition = new HashMap<String,String>();
		 condition.put("batchNo", batchNo);
		 condition.put("visaIds", visaIds);
		 List<Map<String,Object>> traveler = visaOrderService.findBatchReturnPassportTraveler(condition);
		 //req.setAttribute("travelers", traveler);
		 return traveler;
	 }
	 @RequestMapping(value = "totalcheckBatchReturnPassport")
	 @ResponseBody
	 public Object totalcheckBatchReturnPassport(HttpServletRequest req){
		 String batchnos = req.getParameter("batchnos");
		 String[] batchnosArray = batchnos.split(",");
		 StringBuilder totalbatchnos = new StringBuilder();
		 for(int i=0;i<batchnosArray.length;i++){
			 totalbatchnos.append("'"+batchnosArray[i]+"'").append(",");
		 }
		 String totalbatchno= totalbatchnos.toString();
		 totalbatchno= totalbatchno.substring(0, totalbatchno.length()-1);
		 List<Map<String,Object>> list=visaOrderService.totalcheckBatchReturnPassport(totalbatchno);
		 StringBuilder visaIds = new StringBuilder();
		 for(Map<String,Object> map:list){
			 visaIds.append(map.get("visaIds")).append(",");
		 }
		 String totalvisaIds=visaIds.toString();
		 //totalvisaIds=totalvisaIds.substring(0, totalvisaIds.length()-1);
		 return totalvisaIds;
	 }

	/**
	 * 查询借款记录列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "batchBorrowMoneyList")
	public String batchBorrowMoneyList(Model model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> condition = new HashMap<String, String>();
		String batchNo = request.getParameter("batchNo");
		String txnPerson = request.getParameter("txnPerson");
		String createDateStart = request.getParameter("createDateStart");
		String createDateEnd = request.getParameter("createDateEnd");
		String travellerName = request.getParameter("travellerName");
		//订单列表排序
		String orderBy = request.getParameter("orderBy");
		condition.put("batchNo", batchNo);
		condition.put("txnPerson", txnPerson);
		condition.put("createDateStart", createDateStart);
		condition.put("createDateEnd", createDateEnd);
		condition.put("travellerName", travellerName);
		condition.put("orderBy", orderBy);
		Page<Map<String,Object>> page = visaOrderService.batchBorrowMoneyList(request,response,condition);
		String pageStr = page.toString();
		pageStr = pageStr.replace("<div style=\"clear:both;\"></div>", "");
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("page", page);
		model.addAttribute("count",page.getCount());
		model.addAllAttributes(condition);
		return "modules/visa/batchBorrowMoneyList";
	}

	/**
	 * 输出借款游客信息
	 * @param batchNo
	 * @return
	 */
	@RequestMapping(value="getBorrowMoneyTravelerList")
	@ResponseBody
	public List<Map<String,Object>> getBorrowMoneyTravelerList(HttpServletRequest request,HttpServletResponse response){
		String batchNo = request.getParameter("batchNo");
		String visaIds = request.getParameter("visaIds");
		Map<String,String> condition = new HashMap<String,String>();
		condition.put("batchNo", batchNo);
		condition.put("visaIds", visaIds);
		List<Map<String,Object>> traveler = visaOrderService.findBatchBorrowMoneyTraveler(condition);
		return traveler;
	}
	
	/**
	 * 签务签证订单-借款记录
	 * @param orderId 订单ID
	 * @return
	 */
	@RequestMapping("borrowMoneyRecord")
	public String borrowMoneyRecord(@RequestParam(value="orderId", required=true)String orderId, Model model) {
		// 公司ID
		model.addAttribute("companyId",UserUtils.getUser().getCompany().getId());
		// 订单ID
		model.addAttribute("orderId", orderId);
		// 借款记录List
		model.addAttribute("borrowMoneyRecordList", visaOrderService.borrowMoneyRecord(orderId));

		return "modules/visa/borrowMoneyRecord";
	}
	
	/**
	 * 签务签证订单-支付信息
	 * @param orderId
	 * return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("paidRecords")
	@ResponseBody
	public List<Object> getPaidRecordInfo(String orderId){
		List<Object > resultList = visaOrderService.findPaidRecords(orderId, 6);
		List<Object> result = new ArrayList<Object>();
		for(int i = 0;i<resultList.size();i++){
			List list = (List) resultList.get(i);
			Date date = (Date) list.get(3);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			list.set(3, sdf.format(date));
			String money = (String) list.get(2);
			list.set(2, money.replaceAll(",", "+"));
			result.add(list);
		}
		
		return result;
	}
	
	/**
	 * 根据订单id和订单支付id查询支付凭证
	 * @param payId
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="modifypayVoucher/{payId}/{orderId}")
	public String modifypayVoucher(@PathVariable String payId,@PathVariable String orderId, Model model,HttpServletRequest request){
		
		Orderpay orderpay = orderService.findOrderpayById(new Long(payId));
		VisaOrder order = visaOrderService.findVisaOrder(Long.parseLong(orderId));
		
		//支付订单金额千位符处理
		if (StringUtils.isNotBlank(orderpay.getMoneySerialNum())) {
			visaOrderService.clearObject(orderpay);
			orderpay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderpay.getMoneySerialNum()));
		}

		//订单金额千位符处理
		if (StringUtils.isNotBlank(order.getTotalMoney())) {
			visaOrderService.clearObject(order);
			order.setTotalMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
		}

		model.addAttribute("orderpay",orderpay);
		model.addAttribute("orderId",orderId);
		model.addAttribute("productorder",order);
		return "modules/visa/modifypayVoucher";
	}
	
	@RequestMapping(value ="modifypayVoucherFile")
	public String modifypayVoucherFile(@RequestParam(value = "payVoucher", required = false) MultipartFile[] files, HttpServletRequest request, ModelMap model) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		ArrayList<DocInfo> infoList = new ArrayList<DocInfo>();
		if(files!=null && files.length>0){
			for(int i=0;i<files.length;i++){
				MultipartFile file = files[i];
				if(file!=null){
					DocInfo docInfo =null;
					String fileName = file.getOriginalFilename();
					docInfo = new DocInfo();
					//保存
					try {
						String path = FileUtils.uploadFile(file.getInputStream(),fileName);
						docInfo.setDocName(fileName);
						docInfo.setDocPath(path);
						infoList.add(docInfo);
					//保存附件表数据
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		String remarks = request.getParameter("remarks");
		String payId = request.getParameter("payId");
		String orderId = request.getParameter("orderId");
		Orderpay orderpay=this.orderService.findOrderpayById(new Long(payId));
		if(StringUtils.isNotBlank(remarks)){
			orderpay.setRemarks(remarks);
		}
		visaOrderService.updatepayVoucherFile(infoList, orderpay,orderId,model, request);
		
		return "modules/visa/uploadVoSuccess";
	}
	
	/**
	 * 批量批次展示列表
	 * @param 
	 * @return
	 */
	@RequestMapping("visaBatchEditRecordList")
	public String visaBatchEditRecordList(HttpServletRequest request,HttpServletResponse response,String jumpFlag,Model model) {
		//搜索条件
		Map<String, String> condition = new HashMap<String, String>();
		String  batchNo         = request.getParameter("batchNo");
		String  txnPerson       = request.getParameter("txnPerson");
		String  createTimeStart = request.getParameter("createTimeStart");
		String  createTimeEnt   = request.getParameter("createTimeEnt");
		String  travellerName   = request.getParameter("travellerName");
		String  signTimeStart   = request.getParameter("signTimeStart");//实际约签时间-开始时间
		String  signTimeEnd     = request.getParameter("signTimeEnd");  //实际约签时间-结束时间
		String  country         = request.getParameter("country");  //签证国家
		String  area            = request.getParameter("area");
		String  guaranteeType   = request.getParameter("guaranteeType");
		//列表排序
		String orderBy = request.getParameter("orderBy");
		//展示哪个列表页
		String showList = request.getParameter("showList");
		String saveOrSubmit = request.getParameter("saveOrSubmit");
		
		condition.put("batchNo", batchNo);
		condition.put("txnPerson", txnPerson);
		condition.put("createTimeStart", createTimeStart);
		condition.put("createTimeEnt", createTimeEnt);
		condition.put("travellerName", travellerName);
		condition.put("signTimeStart", signTimeStart);
		condition.put("signTimeEnd", signTimeEnd);
		condition.put("signTimeEnd", signTimeEnd);
		condition.put("signTimeEnd", signTimeEnd);
		condition.put("guaranteeType", guaranteeType);
		condition.put("orderBy", orderBy);
		condition.put("country", country);
		condition.put("area", area);
		model.addAllAttributes(condition);
		
		if(null == showList || "jiekuan".equals(showList)){
			//借款列表
			Page<Map<String, Object>> pageJk = visaOrderService.batchBorrowMoneyHqxList(request, response, condition);
			String pageStrJk = pageJk.toString();
			
			//----------------综合处理新旧审核的  借款状态问题-----------------
			if (pageJk.getList().size()>0){
				List<Map<String, Object>> result = pageJk.getList();
				for(Map<String, Object> map:result){
					if (null==map.get("reviewId")) {
						//{travelerIds=26927,26928,26929, batchNo=20151225-0010, txnPerson=田颖, reviewStatus=1,
						//orderIds=7588,7588,7588, reviewId=null, isnew=true, createDate=2015-12-25 19:42:53.0, 
						//batchTotalMoney=18000.00, visaIds=10147,10148,10149}
						String orderid = map.get("orderIds").toString().split(",")[0];
						String travellerid = map.get("travelerIds").toString().split(",")[0];
						String borrowamountAndstatus = visaOrderService.getTravelerBorrowedMoney4Activiti(orderid,Long.parseLong(travellerid));
						if (null!=borrowamountAndstatus) {
							String status = borrowamountAndstatus.split(",")[1];
							map.put("reviewStatus", status);
							String reveiwId  = visaOrderService.getReViewIdByBatchNumMainly(map.get("batchNo").toString(),Integer.valueOf(map.get("travelerIds").toString().split(",")[0]),Integer.valueOf(map.get("orderIds").toString().split(",")[0]));
							map.put("reviewId", reveiwId);
							//map.put("reviewId", borrowamountAndstatus.split(",")[2]);
							if (map.get("isnew") != null) {
								if ("1".equals(map.get("isnew").toString())) {
									map.put("isnew", "1");
								}else if("2".equals(map.get("isnew").toString())){
									map.put("isnew", "2");
								}
							}
						}
					}
				}				
				pageJk.setList(result);
			}
			
			pageStrJk = pageStrJk.replace("<div style=\"clear:both;\"></div>", "");
			model.addAttribute("pageStrJk", pageStrJk);
			model.addAttribute("pageJk", pageJk);
			model.addAttribute("countJk",pageJk.getCount());

			model.addAttribute("showList","jiekuan");
			if (null == saveOrSubmit || "save".equals(saveOrSubmit)) {
				model.addAttribute("saveOrSubmit","save");
			} else if ("submit".equals(saveOrSubmit)) {
				model.addAttribute("saveOrSubmit","submit");
			}
		} else if("huanshouju".equals(showList)){
			//还收据列表
			Page<Map<String, Object>> pageHsj = visaOrderService.batchReturnReceiptandHqxList(request, response, condition);
			String pageStrHsj = pageHsj.toString();
			
			
			//----------------综合处理新旧审核的  借款状态问题-----------------
			if (pageHsj.getList().size()>0){
				List<Map<String, Object>> result = pageHsj.getList();
				for(Map<String, Object> map:result){
					if (null==map.get("reviewId")) {
						//{travelerIds=26927,26928,26929, batchNo=20151225-0010, txnPerson=田颖, reviewStatus=1,
						//orderIds=7588,7588,7588, reviewId=null, isnew=true, createDate=2015-12-25 19:42:53.0, 
						//batchTotalMoney=18000.00, visaIds=10147,10148,10149}
						String orderid = map.get("orderIds").toString().split(",")[0];
						String travellerid = map.get("travelerIds").toString().split(",")[0];
						String returnReciptamountAndstatus = visaOrderService.getTravelerRetrunReceipt4Activiti(orderid,Long.parseLong(travellerid));
						if (null!=returnReciptamountAndstatus) {
							String status = returnReciptamountAndstatus.split(",")[1];
							map.put("reviewStatus", status);
							map.put("reviewId", returnReciptamountAndstatus.split(",")[2]);
							if (map.get("isnew") != null) {
								if ("1".equals(map.get("isnew").toString())) {
									map.put("isnew", "1");
								}else if("2".equals(map.get("isnew").toString())){
									map.put("isnew", "2");
								}
							}
						}
					}
				}				
				pageHsj.setList(result);
			}

			
			pageStrHsj = pageStrHsj.replace("<div style=\"clear:both;\"></div>", "");
			model.addAttribute("pageStrHsj", pageStrHsj);
			model.addAttribute("pageHsj", pageHsj);
			model.addAttribute("countHsj",pageHsj.getCount());

			model.addAttribute("showList","huanshouju");
			if (null == saveOrSubmit || "save".equals(saveOrSubmit)) {
				model.addAttribute("saveOrSubmit","save");
			} else if ("submit".equals(saveOrSubmit)) {
				model.addAttribute("saveOrSubmit","submit");
			}
		} else if(showList.equals("jiehuzhao")){
			//借护照-未提交
			Page<Map<String, Object>> page1 = visaOrderService.borrowPassportWait4Submit(request, response, condition);
			String pageStr1 = page1.toString();
			pageStr1 = pageStr1.replace("<div style=\"clear:both;\"></div>", "");
			model.addAttribute("pageStr1", pageStr1);
			model.addAttribute("page1", page1);
			model.addAttribute("count1",page1.getCount());		
			//借护照-已提交
			Page<Map<String, Object>> page2 = visaOrderService.borrowPassportAlreadySubmit(request, response, condition);
			String pageStr2 = page2.toString();
			pageStr2 = pageStr2.replace("<div style=\"clear:both;\"></div>", "");
			model.addAttribute("pageStr2", pageStr2);
			model.addAttribute("page2", page2);
			model.addAttribute("count2",page2.getCount());
			
			model.addAttribute("showList","jiehuzhao");
			if(null == saveOrSubmit || "save".equals(saveOrSubmit)){
				model.addAttribute("saveOrSubmit","save");
			}
			else if ("submit".equals(saveOrSubmit)) {
				model.addAttribute("saveOrSubmit","submit");
			}
		}else if(showList.equals("huanhuzhao")){
			//还护照-待提交
			Page<Map<String,Object>> page3 = visaOrderService.returnPassportWait4Submit(request, response, condition);
			String pageStr3 = page3.toString();
			pageStr3 = pageStr3.replace("<div style=\"clear:both;\"></div>", "");
			model.addAttribute("pageStr3", pageStr3);
			model.addAttribute("page3", page3);
			model.addAttribute("count3",page3.getCount());
			//还护照-已提交
			Page<Map<String, Object>> page4 = visaOrderService.returnPassportAlreadySubmit(request, response, condition);
			String pageStr4 = page4.toString();
			pageStr4 = pageStr4.replace("<div style=\"clear:both;\"></div>", "");
			model.addAttribute("pageStr4", pageStr4);
			model.addAttribute("page4", page4);
			model.addAttribute("count4",page4.getCount());
			
			model.addAttribute("showList","huanhuzhao");
			if(null == saveOrSubmit || "save".equals(saveOrSubmit)){
				model.addAttribute("saveOrSubmit","save");
			}
			else if ("submit".equals(saveOrSubmit)) {
				model.addAttribute("saveOrSubmit","submit");
			}
			
		}else if(showList.equals("mianqiantongzhi")){
	
			Page<Map<String, Object>> pageMqtz = visaOrderService.batchEditInterviewNotice(request, response, condition);
			String pageMqtzStr = pageMqtz.toString();
			List<Country> countrys = CountryUtils.getCountrys();//签证国家
			model.addAttribute("countrys", countrys);
			List<Map<String,Object>> fromAreaMapList = visaOrderService.findDictByType4Map("from_area");//签证领区
			model.addAttribute("fromAreaMapList", fromAreaMapList);
			model.addAttribute("pageMqtzStr", pageMqtzStr);
			model.addAttribute("pageMqtz", pageMqtz);
			model.addAttribute("countMqtz",pageMqtz.getCount());
			model.addAttribute("showList","mianqiantongzhi");
		}else if(showList.equals("guarantee")) {
			Page<Map<String, Object>> page4Gua = visaOrderService.getBatchOpt4Gua(request, response, condition);
			model.addAttribute("page4Gua", page4Gua);
			model.addAttribute("showList","guarantee");
			List<Country> countrys = CountryUtils.getCountrys();//签证国家
			model.addAttribute("countrys", countrys);
			List<Map<String,Object>> fromAreaMapList = visaOrderService.findDictByType4Map("from_area");//签证领区
			model.addAttribute("fromAreaMapList", fromAreaMapList);
		}

		
		//销售签证订单-批量借款记录(角色权限配置是否显示)
		if(StringUtils.isNotEmpty(jumpFlag) && "1".equals(jumpFlag)){
			return "modules/visa/visaBatchEditRecordList4XS";
		}else{//批量操作记录页
			return "modules/visa/visaBatchEditRecordList";
		}
		
	}

	
	/**
	 * 解析map返回字符串
	 */
	public String getString(Map<String, Object> mp)
	{
		String tmp= "";
		DecimalFormat df = new DecimalFormat("#,##0.00");
		int flag= 0;
		for(String entry:mp.keySet())
		{   flag++;
			if(flag == mp.keySet().size())
			tmp = entry+df.format(mp.get(entry))+tmp;
			else
			tmp = tmp+"+"+entry+df.format(mp.get(entry));
		}     
		return tmp;
	}

	/**
	 * 修改订单的预定渠道
	 * @author jiachen
	 * @DateTime 2015年7月16日 下午4:44:33
	 * @param orderId
	 * @param agentId
	 * @return void
	 */
	@ResponseBody
	@RequestMapping("updateOrderAgent")
	public void updateOrderAgent(String orderId, String agentId, String[] dataArr) {
		visaOrderService.updateOrderAgent(orderId, agentId, dataArr);
	}
	
	/**
	 * 修改订单预计团队返佣
	 * @author nan
	 * @date 2015年8月17日 下午7:44:06
	 * @param orderId 订单ID
	 * @param groupRebatesCurrency 币种ID
	 * @param groupRebatesMoney 金额
	 */
	@ResponseBody
	@RequestMapping("updateGroupRebates")
	public void updateGroupRebates(String orderId, String groupRebatesCurrency, String groupRebatesMoney) {
		visaOrderService.updateGroupRebates(orderId, groupRebatesCurrency, groupRebatesMoney);
	}
	
	@RequestMapping("exportExcelUserList")
	public void exportExcelUserList(HttpServletRequest request,HttpServletResponse response,VisaOrderForm form,Model model) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException{
		DepartmentCommon common = departmentService.setDepartmentPara();
		Page<Map<String,Object>> pageList = visaOrderService.searchVisaOrder(request,response,form,"qianwu",common,true);
		  //  文件名称str   文件表头列表Map   表头对应的属性  Map   class信息 class
        String fileName = "游客信息";
        String columnTitlesStr = "序号,销售,人数,单号,姓名,英文,性别,出生年月,护照号,发行地,职业,关系,押金,担保函,小费,自费,保险,备注,手机";
        String[] columnTitlesArray =  columnTitlesStr.split(",");
        
        String rowMsgsStr = "salerName,travelerCount,orderCode,name,nameSpell,sex,birthDay,passportCode,passportPlace,,,,,,,,remark,telephone";
        String[] rowMsgsArray = rowMsgsStr.split(",");
        for(Map<String,Object> entity:pageList.getList()){
    		//获取游客信息并过滤已改签游客
        	List<Traveler> travelers = travelerService.findTravelerByOrderIdAndOrderType(new Long(entity.get("orderId").toString()), 6);
        	entity.put("userList", travelers);
        }
        try {
			ExportExcel.exportExcel(fileName,columnTitlesArray,rowMsgsArray,pageList.getList(),request,response,"orderCommon");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//填充结果集--订单列表
		request.setAttribute("resultFormList", pageList.getList());
	}
	
	/***
	 * 签务签证订单(新)
	 */
	@RequestMapping(value={"searchqw"})
	public String searchqw(HttpServletRequest request,HttpServletResponse response,VisaOrderForm visaOrderForm ,Model model){
		//部门ID及角色查询
		DepartmentCommon common = departmentService.setDepartmentPara();
		
		// 通过搜索条件判断返回的展示标签
		String showType = Context.ORDER_PAYSTATUS_ALL;
		if (StringUtils.isNotBlank(visaOrderForm.getOrderPayStatus()) && Context.ORDER_PAYSTATUS_YQX.equals(visaOrderForm.getOrderPayStatus())) {
			showType = Context.ORDER_PAYSTATUS_YQX;
			visaOrderForm.setShowType(Context.ORDER_PAYSTATUS_YQX);
			// 如果 没有已取消权限，则展示“全部页签”
			Object cancelOrderPermission = request.getParameter("cancelOrderPermission");
			if (!(cancelOrderPermission != null && StringUtils.isNotBlank(cancelOrderPermission.toString()) && "1".equals(cancelOrderPermission.toString()))) {
				showType = Context.ORDER_PAYSTATUS_ALL;
				visaOrderForm.setShowType(Context.ORDER_PAYSTATUS_ALL);
			}
		} else {
			visaOrderForm.setShowType(Context.ORDER_PAYSTATUS_ALL);
		}
		request.setAttribute("showType", showType);
		
		if("youke".equals(visaOrderForm.getShowList())){
			//游客列表
			Page<Map<String,Object>> travelList = visaOrderService.searchTravelList(request,response,visaOrderForm,"qianwu",common);
			
			//填充游客列表结果集
			request.setAttribute("travelList", travelList.getList());
			request.setAttribute("travelPage", travelList);
		}else{
			//订单列表
			Page<Map<String,Object>> pageList = visaOrderService.searchVisaOrder(request,response,visaOrderForm,"qianwu",common,false);
			
			//填充结果集--订单列表
			request.setAttribute("resultFormList", pageList.getList());
			request.setAttribute("page", pageList);
		}
		
		//填充初始数据
		initializationData(request);
		//填充查询条件
		request.setAttribute("visaOrderForm", visaOrderForm);
		//查询条件是否展开
		request.setAttribute("flag", request.getParameter("showFlag"));
		//展示哪个列表页
		request.setAttribute("showList",visaOrderForm.getShowList());
		//根据公司ID判断按钮是否显示
		processMenuProcRight_qianWu(request);
		
		model.addAttribute("companyUUid", UserUtils.getUser().getCompany().getUuid().trim());
		model.addAttribute("dayangCompanyUuid", Context.DA_YANG_COMPANYUUID);
		//添加是否多返佣对象标识（0：否；1：是）
		model.addAttribute("isAllowMultiRebateObject", UserUtils.getUser().getCompany().getIsAllowMultiRebateObject());	
//		System.out.println(UserUtils.getUser().getCompany().getIsAllowMultiRebateObject());
		return "modules/visa/visaOrderQianwu";
	}
	
	/***
	 * 销售签证订单(新)
	 */
	@RequestMapping(value={"searchxs"})
	public String searchxs(HttpServletRequest request,HttpServletResponse response,VisaOrderForm visaOrderForm){
		//部门ID及角色查询
		DepartmentCommon common = departmentService.setDepartmentPara();
		
		// 通过搜索条件判断返回的展示标签
		String showType = Context.ORDER_PAYSTATUS_ALL;
		if (StringUtils.isNotBlank(visaOrderForm.getOrderPayStatus()) && Context.ORDER_PAYSTATUS_YQX.equals(visaOrderForm.getOrderPayStatus())) {
			showType = Context.ORDER_PAYSTATUS_YQX;
			visaOrderForm.setShowType(Context.ORDER_PAYSTATUS_YQX);
			// 如果 没有已取消权限，则展示“全部页签”
			Object cancelOrderPermission = request.getParameter("cancelOrderPermission");
			if (!(cancelOrderPermission != null && StringUtils.isNotBlank(cancelOrderPermission.toString()) && "1".equals(cancelOrderPermission.toString()))) {
				showType = Context.ORDER_PAYSTATUS_ALL;
				visaOrderForm.setShowType(Context.ORDER_PAYSTATUS_ALL);
			}
		} else {
			visaOrderForm.setShowType(Context.ORDER_PAYSTATUS_ALL);
		}
		request.setAttribute("showType", showType);
			
		if("youke".equals(visaOrderForm.getShowList())){
			//游客数据列表
			Page<Map<String,Object>> travelList = visaOrderService.searchTravelList(request,response,visaOrderForm,"xiaoshou",common);
			//填充游客列表结果集
			request.setAttribute("travelList", travelList.getList());
			request.setAttribute("travelPage", travelList);
		}else{
			//订单数据列表
			Page<Map<String,Object>> dingdanList = visaOrderService.searchVisaOrder(request,response,visaOrderForm,"xiaoshou",common,false);
			//达帐和撤销提示
			if (dingdanList != null) {
				List<Map<String,Object>> list = dingdanList.getList();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> resultList = list.get(i);
						boolean isCanceledOrder = false;
						if (resultList.get("payStatus") != null) {
							isCanceledOrder = Context.ORDER_PAYSTATUS_YQX.equals(resultList.get("payStatus").toString());
						}
						resultList.put("promptStr",orderCommonService.getOrderPrompt("6", Long.parseLong(resultList.get("orderId").toString()), isCanceledOrder));
					}
				}
			}
			//填充结果集--订单列表
			request.setAttribute("resultFormList", dingdanList.getList());
			request.setAttribute("page", dingdanList);
			request.setAttribute("mainOrderId", request.getParameter("mainOrderId"));
			
		}
		
		//填充初始数据
		initializationData(request);
		
		//填充查询条件
		request.setAttribute("visaOrderForm", visaOrderForm);
		request.setAttribute("flag", request.getParameter("showFlag"));
		request.setAttribute("showList",visaOrderForm.getShowList());
		processMenuProcRight_xiaoShou(request);
		request.setAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
		request.setAttribute("dayangCompanyUuid", Context.DA_YANG_COMPANYUUID);
		return "modules/visa/visaOrderXiaoshou";
	}
	
	/**
	 * 订单列表展开游客，根据订单ID查询游客信息
	 * @return traveler 游客信息List
	 */
	@RequestMapping(value="searchTravelerByOrderId")
	@ResponseBody
	public List<Map<String,Object>> searchTravelerByOrderId(HttpServletRequest request,HttpServletResponse response){
		//订单ID
		String orderId = request.getParameter("orderId");
		//身份
		String shenfen = request.getParameter("shenfen");
		
		//根据订单ID查询游客信息
		List<Map<String,Object>> traveler = visaOrderService.searchTravelerByOrderId(orderId, shenfen);
		
		return traveler;
	}
	
	/**
	 * 签证批量操作页(新)
	 * @param visaOrderForm 页面数据
	 * @return
	 */
	@RequestMapping("visaBatchEditListNew")
	public String visaBatchEditListNew(HttpServletRequest request,HttpServletResponse response,String details,VisaOrderForm visaOrderForm) {
		if(visaOrderForm == null){
			visaOrderForm = new VisaOrderForm();
		}
		//部门ID及角色查询
		DepartmentCommon common = departmentService.setDepartmentPara();
		
		//批量操作-游客列表
		Page<Map<String,Object>> travelList = visaOrderService.searchTravelList(request ,response, visaOrderForm,"qianwu",common);
		
		//填充游客列表结果集
		request.setAttribute("travelList", travelList.getList());
		request.setAttribute("page", travelList);
		
		//填充初始数据
		initializationData(request);
		//填充查询条件
		request.setAttribute("visaOrderForm", visaOrderForm);
		//查询条件是否展开
		request.setAttribute("flag", request.getParameter("showFlag"));
		//展示哪个列表页
		request.setAttribute("showList","youke");
		//根据公司ID判断按钮是否显示
		processMenuProcRight_qianWu(request);
		
		//销售签证订单-批量操作(懿洋假期-批量借款)
		if(StringUtils.isNotEmpty(details) && "1".equals(details)){
			return "modules/visa/visaBatchEditList4XS";
		}else{//签务签证订单-批量操作
			return "modules/visa/visaBatchEditListNew";
		}
		
	}
	
	/**
	 * 
	* @Title: print
	* @Description: TODO(签证押金收款单打印20150824)
	* @param @param payid 支付记录id
	* @param @param model
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping(value ="visaCashPrint/{payid}")
	public String print(@PathVariable Long payid,Model model){
		if(payid == null) {
			return null;
		}
		else {
			model.addAttribute("payid", payid);
			List<Map<Object, Object>> payList = new ArrayList<Map<Object, Object>>();
			payList = visaOrderService.getVisaCashPayPrint(payid);
			if(payList != null && payList.size() > 0) {
				 model.addAttribute("pay", payList.get(0));
				 model.addAttribute("groupCode", payList.get(0).get("groupCode") == null ? "" : payList.get(0).get("groupCode").toString());
				 model.addAttribute("groupName", payList.get(0).get("acitivityName") == null ? "" : payList.get(0).get("acitivityName"));
				 //根据游客id获取游客姓名
				 String travelerId = payList.get(0).get("travelerId").toString();
				 if(StringUtils.isNotBlank(travelerId)) {
					 String travelerName = travelerDao.findById(Long.parseLong(travelerId)).getName();
					 model.addAttribute("traveler", travelerName); 
				 }else {
					 model.addAttribute("traveler", ""); 
				 }
				 String capitalMoney = "";
				   String payPriceType = payList.get(0).get("payPriceType").toString();
				   if(StringUtils.isNotBlank(payPriceType)){
					   if(payPriceType.equals("16")){
						  String payedMoney = payList.get(0).get("payedMoney").toString();
						  //当前批发商的美元、加元汇率（目前环球行）
						  List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
						    //美元汇率
							BigDecimal currencyExchangerateUSA = null;
							//加元汇率
							BigDecimal currencyExchangerateCAN = null;
							//人民币计算
							BigDecimal amountCHN = new BigDecimal("0");
							//美元计算
							BigDecimal amountUSA = new BigDecimal("0");
							//加元计算
							BigDecimal amountCAN = new BigDecimal("0");
						  for (Currency currency : currencylist) {
							if(currency.getCurrencyName().startsWith("美元")) {
								currencyExchangerateUSA = currency.getConvertLowest();
							}else if(currency.getCurrencyName().startsWith("加")) {
								currencyExchangerateCAN = currency.getConvertLowest();
							}
						}
						  //增加多币种金额判断
						  if(payedMoney.contains("+")) {
							  String [] moneys = payedMoney.split("\\+");
							  if(moneys.length > 0) {
								  for(int i = 0 ; i < moneys.length ; i++) {
									  if(moneys[i].contains("人民币")) {
										  moneys[i] = moneys[i].replaceAll("人民币", "").replaceAll(",", "");
										  amountCHN = amountCHN.add(BigDecimal.valueOf(Double.parseDouble(moneys[i])));
									  }else if(moneys[i].contains("美元")) {
										  moneys[i] = moneys[i].replaceAll("美元", "").replaceAll(",", "");
										  amountUSA = amountUSA.add(BigDecimal.valueOf(Double.parseDouble(moneys[i])));
										  amountCHN = amountCHN.add(amountUSA.multiply(currencyExchangerateUSA));
									  }else {
										  moneys[i] = moneys[i].replaceAll("加币", "").replaceAll("加拿大", "").replaceAll(",", "");
										  amountCAN = amountCAN.add(BigDecimal.valueOf(Double.parseDouble(moneys[i])));
										  amountCHN = amountCHN.add(amountCAN.multiply(currencyExchangerateCAN));
									  }
								  }
								  if(amountCHN.doubleValue() < 0) {
									  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
									  model.addAttribute("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								  }else {
									  capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
									  model.addAttribute("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								  }
								  model.addAttribute("capitalMoney", capitalMoney);
							  }
						  }else {
							  if(payedMoney.contains("人民币")) {
								  payedMoney = payedMoney.replaceAll("人民币", "");
								  if(payedMoney.contains("-")) {
									  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(payedMoney.replaceAll("-", "").replace(",", ""));
								  }else {
									  capitalMoney = "人民币" + StringNumFormat.changeAmount(payedMoney.replaceAll(",", ""));
								  }
								  model.addAttribute("payedMoney", payedMoney);
								  model.addAttribute("capitalMoney", capitalMoney);
							  }else if(payedMoney.contains("美元")) {
								  payedMoney = payedMoney.replaceAll("美元", "").replace(",", "");
								  amountUSA = amountUSA.add(BigDecimal.valueOf(Double.parseDouble(payedMoney)));
								  amountCHN = amountCHN.add(amountUSA.multiply(currencyExchangerateUSA));
								  if(amountCHN.doubleValue() < 0) {
									  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
									  model.addAttribute("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								  }else {
									  capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
									  model.addAttribute("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								  }
								  model.addAttribute("capitalMoney", capitalMoney);
							  }else if(payedMoney.contains("加币") || payedMoney.contains("加拿大")) {
								  payedMoney = payedMoney.replaceAll("加币", "").replaceAll("加拿大", "").replace(",", "");
								  amountCAN = amountCAN.add(BigDecimal.valueOf(Double.parseDouble(payedMoney)));
								  amountCHN = amountCHN.add(amountCAN.multiply(currencyExchangerateCAN));
								  if(amountCHN.doubleValue() < 0) {
									  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
									  model.addAttribute("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								  }else {
									  capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
									  model.addAttribute("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								  }
								  model.addAttribute("capitalMoney", capitalMoney);
							  }
							  else {
								  model.addAttribute("payedMoney", payedMoney);
								  model.addAttribute("capitalMoney", capitalMoney);
							  }
						  }
					   }
				   }
					model.addAttribute("groupOpenDate", payList.get(0).get("conDate"));
					Long companyId = UserUtils.getUser().getCompany().getId();
					if(companyId != 68) {
						model.addAttribute("pay.conDate", payList.get(0).get("updateDate"));
					}else {
						model.addAttribute("pay.conDate", "");
					}
					model.addAttribute("item", "签证押金"); 
					Date printTime = null;
					Orderpay orderpay = orderpayDao.findOne(payid);
					printTime = orderpay.getPrintTime();
					//Integer printFlag=orderpay.getPrintFlag();
					if(null != printTime){
						model.addAttribute("firstPrintTime", printTime);
					}else{
						model.addAttribute("firstPrintTime", new Date());//处理已打印，但是无打印时间的情况（特殊情况）
					}
					model.addAttribute("orderType", Context.ORDER_TYPE_QZ);
					model.addAttribute("printType", "visaCashPrint");
					//20151016环球行、拉美途客户确认到账时间为空
					String companyName = UserUtils.getUser().getCompany().getCompanyName();
					model.addAttribute("companyName", companyName);
					String companyUuid = UserUtils.getUser().getCompany().getUuid();
					model.addAttribute("isHQX", Context.SUPPLIER_UUID_HQX.equals(companyUuid));
					model.addAttribute("isLMT", Context.SUPPLIER_UUID_LMT.equals(companyUuid));
				/*if (printFlag==null || printFlag==0){
					orderpay.setPrintFlag(1);
					orderpay.setPrintTime(tempDateTime);
					model.addAttribute("firstPrintTime", tempDateTime);
					orderpayDao.save(orderpay);
				}*/
			}
		}
		return "modules/order/print";
	}
	
	/**
	 * @Title: batch print
	 * @Description: 签证押金收款单批量打印
	 * @param payids 批量打印的支付记录id
	 * @author yang.wang@quauq.com
	 * @date 2016.10.25
	 * */
	@RequestMapping(value = "visaCashBatchPrint")
	public String batchPrint(String payids, Model model) {
		
		if (payids == null) {
			return null;
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		//20151016环球行、拉美途客户确认到账时间为空
		String companyName = UserUtils.getUser().getCompany().getCompanyName();
		model.addAttribute("companyName", companyName);
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("isHQX", Context.SUPPLIER_UUID_HQX.equals(companyUuid));
		model.addAttribute("isLMT", Context.SUPPLIER_UUID_LMT.equals(companyUuid));
		
		List<Map<Object, Object>> printList = new ArrayList<>();
		String[] payidArr = payids.split(",");
		for (String payid : payidArr) {
			Map<Object, Object> print = new HashMap<>();
			print.put("payid", payid);
			
			List<Map<Object, Object>> payList = visaOrderService.getVisaCashPayPrint(Long.parseLong(payid));
			if (payList != null && payList.size() > 0) {
				print.put("pay", payList.get(0));
				print.put("groupCode", payList.get(0).get("groupCode") == null ? "" : payList.get(0).get("groupCode").toString());
				print.put("groupName", payList.get(0).get("acitivityName") == null ? "" : payList.get(0).get("acitivityName"));
				
				//根据游客id获取游客姓名
				String travelerId = payList.get(0).get("travelerId").toString();
				if(StringUtils.isNotBlank(travelerId)) {
					String travelerName = travelerDao.findById(Long.parseLong(travelerId)).getName();
					print.put("traveler", travelerName); 
				}else {
					print.put("traveler", ""); 
				}
				
				String capitalMoney = "";
				String payPriceType = payList.get(0).get("payPriceType").toString();
				if (StringUtils.isNotBlank(payPriceType)) {
					if (payPriceType.equals("16")) {
						String payedMoney = payList.get(0).get("payedMoney").toString();
						//当前批发商的美元、加元汇率（目前环球行）
						List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
						//美元汇率
						BigDecimal currencyExchangerateUSA = null;
						//加元汇率
						BigDecimal currencyExchangerateCAN = null;
						//人民币计算
						BigDecimal amountCHN = new BigDecimal("0");
						//美元计算
						BigDecimal amountUSA = new BigDecimal("0");
						//加元计算
						BigDecimal amountCAN = new BigDecimal("0");
						for (Currency currency : currencylist) {
							if (currency.getCurrencyName().startsWith("美元")) {
								currencyExchangerateUSA = currency.getConvertLowest();
							}else if (currency.getCurrencyName().startsWith("加")) {
								currencyExchangerateCAN = currency.getConvertLowest();
							}
						}
						//增加多币种金额判断
						if (payedMoney.contains("+")) {
							String [] moneys = payedMoney.split("\\+");
							if (moneys.length > 0) {
								for(int i = 0 ; i < moneys.length ; i++) {
									if(moneys[i].contains("人民币")) {
										moneys[i] = moneys[i].replaceAll("人民币", "").replaceAll(",", "");
										amountCHN = amountCHN.add(BigDecimal.valueOf(Double.parseDouble(moneys[i])));
									} else if (moneys[i].contains("美元")) {
										moneys[i] = moneys[i].replaceAll("美元", "").replaceAll(",", "");
										amountUSA = amountUSA.add(BigDecimal.valueOf(Double.parseDouble(moneys[i])));
										amountCHN = amountCHN.add(amountUSA.multiply(currencyExchangerateUSA));
									} else {
										moneys[i] = moneys[i].replaceAll("加币", "").replaceAll("加拿大", "").replaceAll(",", "");
										amountCAN = amountCAN.add(BigDecimal.valueOf(Double.parseDouble(moneys[i])));
										amountCHN = amountCHN.add(amountCAN.multiply(currencyExchangerateCAN));
									}
								}
								if (amountCHN.doubleValue() < 0) {
									capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
									print.put("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}else {
									capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
									print.put("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								print.put("capitalMoney", capitalMoney);
							}
						} else {
							if (payedMoney.contains("人民币")) {
								payedMoney = payedMoney.replaceAll("人民币", "");
								if (payedMoney.contains("-")) {
									capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(payedMoney.replaceAll("-", "").replace(",", ""));
								} else {
									capitalMoney = "人民币" + StringNumFormat.changeAmount(payedMoney.replaceAll(",", ""));
								}
								print.put("payedMoney", payedMoney);
								print.put("capitalMoney", capitalMoney);
							} else if (payedMoney.contains("美元")) {
								payedMoney = payedMoney.replaceAll("美元", "").replace(",", "");
								amountUSA = amountUSA.add(BigDecimal.valueOf(Double.parseDouble(payedMoney)));
								amountCHN = amountCHN.add(amountUSA.multiply(currencyExchangerateUSA));
								if (amountCHN.doubleValue() < 0) {
									capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
									print.put("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								} else {
									capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
									print.put("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								print.put("capitalMoney", capitalMoney);
							} else if (payedMoney.contains("加币") || payedMoney.contains("加拿大")) {
								payedMoney = payedMoney.replaceAll("加币", "").replaceAll("加拿大", "").replace(",", "");
								amountCAN = amountCAN.add(BigDecimal.valueOf(Double.parseDouble(payedMoney)));
								amountCHN = amountCHN.add(amountCAN.multiply(currencyExchangerateCAN));
								if (amountCHN.doubleValue() < 0) {
									capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
									print.put("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								} else {
									capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
									print.put("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								print.put("capitalMoney", capitalMoney);
							} else {
								print.put("payedMoney", payedMoney);
								print.put("capitalMoney", capitalMoney);
							}
						}
					}
				}
				
				print.put("groupOpenDate", payList.get(0).get("conDate"));
				if(companyId != 68) {
					print.put("pay.conDate", payList.get(0).get("updateDate"));
				}else {
					print.put("pay.conDate", "");
				}
				
				print.put("item", "签证押金");
				Date printTime = null;
				Orderpay orderpay = orderpayDao.findOne(Long.parseLong(payid));
				printTime = orderpay.getPrintTime();
				if(null != printTime){
					print.put("firstPrintTime", printTime);
				}else{
					print.put("firstPrintTime", new Date());//处理已打印，但是无打印时间的情况（特殊情况）
				}
				
				print.put("orderType", Context.ORDER_TYPE_QZ);
				print.put("printType", "visaCashPrint");
				
				printList.add(print);
			}
			
		}
		
		model.addAttribute("list", printList);
		return "modules/order/batchPrint";
	}
	
	
	/**
	 * 
	* @Title: downloadVisaCash
	* @Description: TODO(下载签证押金单20150824)
	* @param @param payid
	* @param @param request
	* @param @param response
	* @param @return
	* @param @throws IOException
	* @param @throws TemplateException
	* @return ResponseEntity<byte[]>    返回类型
	* @throws
	 */
	
	@RequestMapping(value ="downloadVisaCash/{payid}")
	public ResponseEntity<byte[]> downloadVisaCash(@PathVariable String payid,
			HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{
		if(StringUtils.isBlank(payid)){
			return null;
		}
		File file = visaOrderService.createVisaCashFile(Long.parseLong(payid));
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName = new StringBuilder().append("客人报名收款单").append(nowDate).append(".doc").toString();
		updateOrderPayPrinted(Long.parseLong(payid));
		WordDownLoadUtils.downLoadWordByAttachment(file, fileName, response);
		return null;
	}
	
	/**
	 * 
	* @Title: updateOrderPayPrinted
	* @Description: TODO(更新打印状态以及打印时间，如果是已打印状态则不进行更新操作20150824)
	* @param @param payId
	* @return Date    返回类型
	* @throws
	 */
	private Date updateOrderPayPrinted(Long payId){
		Date nowDate = null;
		try {
			Orderpay orderpay = orderpayDao.findOne(payId);
			Integer printFlag = orderpay.getPrintFlag();
			if (printFlag == null || printFlag == 0){
				orderpay.setPrintFlag(1);
				nowDate = new Date();
				orderpay.setPrintTime(nowDate);
				orderpayDao.save(orderpay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowDate;
	}
	
	/**
	 * 判断订单是否已开发票或收据
	* @param orderId 订单ID
	* @return status 状态
	 */
	@RequestMapping(value="checkOrderStatus")
	@ResponseBody
	public String checkOrderStatus(String orderId){
		if(StringUtil.isNotBlank(orderId)){
			//List不为空说明已开发票
			List<Orderinvoice> orderinvoiceList = orderinvoiceDao.findOrderInvoiceByOrderId(Integer.parseInt(orderId),6);
			if(orderinvoiceList!=null && orderinvoiceList.size()>0){
				return "已开发票";
			}
			//List不为空说明已开收据，status=2说明收据已还，收据已还时订单可取消
			List<OrderReceipt> orderReceiptList = orderReceiptDao.findOrderReceiptByOrderId(Integer.parseInt(orderId),6);
			if(orderReceiptList != null && orderReceiptList.size()>0){
				for(int i=0;i<orderReceiptList.size();i++){
					if(orderReceiptList.get(i).getCreateStatus()!=2){
						return "已开收据";
					}
				}
			}
		}
		return "";
	}
	
	//-------签证修改，添加游客  begin,对应需求号c333---------
	
	/**
	 * 测试订单编号:BJH151119066
	 * @Description: 签证修改，添加游客;对应需求号C333
	 * @author xinwei.wang
	 * @date 2015年11月23日上午9:13:18
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException    
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "addTravelers4update")
	@Transactional
	public Map<String, Object> addTravelers4update(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		
		String travelerJSON = request.getParameter("travelerJSON");
		//String activityId = request.getParameter("activityId");//产品ID
		String orderid = request.getParameter("orderid");//订单ID
		//String specialremark = request.getParameter("specialremark");//订单特殊需求
		//[{"id":"","name":"my","sum":"2","currency":"34"},{"id":"","name":"jnd","sum":"3","currency":"35"}]
		JSONArray costObject = JSONArray.fromObject(request.getParameter("costs"));
			                                                                                                                           
		//payPrice
		//1.保存游客信息
		List<Traveler> travelerList = jsonToTravelerBean(travelerJSON);
		Traveler saveTraveler = visaOrderService.saveTravelers(Long.parseLong(orderid),travelerList,true);
		String rebatesPayPrice =request.getParameter("rebatesPayPrice");
//		saveTraveler.getId();
		

		//0820上，游客返佣费用
		visaOrderService.saveMoneyAmount(rebatesPayPrice,saveTraveler);

		//2. 创建签证信息
		Visa backVisa = saveVisa(saveTraveler.getId(),request);
		
		//3.保存签证订单特殊需求
		VisaOrder  visaOrder  = visaOrderService.findVisaOrder(Long.parseLong(orderid));
		if (null!=visaOrder) {
			//visaOrder.setRemark(specialremark);
			//订单人数要加1
			visaOrder.setTravelNum(visaOrder.getTravelNum()+1);
			visaOrderService.saveOrderInfo(visaOrder, null);
		}
		
		//4.保存游客其它费用信息 [{"currencyId":"33","currencyName":"人民币","price":"100"},{"currencyId":"34","currencyName":"美元","price":"4"},{"currencyId":"35","currencyName":"加拿大","price":"12"}]
		if(costObject.size() > 0){
			saveCost(costObject, saveTraveler.getId());
		}
		
		//5.保存游客结算价 和  游客成本价 ----modified wxw 2015-03-04 游客成本价 ----
		savePayPrice(saveTraveler, request);
		
		//6.在订单中增加   所添加游客的相应游客各项费用：订单总应收价、订单总结算价、订单应收金额
		String order_costTotalMoney = visaOrder.getCostTotalMoney();//成本价
		String order_totalMoney = visaOrder.getTotalMoney();//结算价
		//String serialsnum_originalTotalMoney = visaOrder.getOriginalTotalMoney();//原始应收价
		
		String travle_costTotalMoney = saveTraveler.getCostPriceSerialNum();//游客结算价
		String travle_totalMoney = saveTraveler.getPayPriceSerialNum();//游客结算价
		
		List<MoneyAmount> moneyAmounts4costTotalMoney = visaOrderService.moneyAmountsAdd(order_costTotalMoney, travle_costTotalMoney,"add");
		moneyAmountService.saveOrUpdateMoneyAmounts(order_costTotalMoney, moneyAmounts4costTotalMoney);
		
		List<MoneyAmount> moneyAmounts4totalMoney = visaOrderService.moneyAmountsAdd(order_totalMoney, travle_totalMoney,"add");
		moneyAmountService.saveOrUpdateMoneyAmounts(order_totalMoney, moneyAmounts4totalMoney);
		
	    //-------by------junhao.zhao-----2017-01-10-----------visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改---开始---
		if (null!=visaOrder && visaOrder.getId() != null && StringUtils.isNotBlank(visaOrder.getId().toString())) {
			orderDateSaveOrUpdateService.updatePeopleAndMoney(visaOrder.getId(),Context.ORDER_TYPE_QZ);
		}
		//-------by------junhao.zhao-----2017-01-10-----------visa_order订单修改之后，将表order_data_statistics中对应的收客人数与订单金额修改---结束---
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("travelerId", saveTraveler.getId());
		map.put("visaId", backVisa.getId());
		
		return map;
	}
	
	/**
	 * 把traveler  JSON字符串转换为  List<Traveler>
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	private List<Traveler> jsonToTravelerBean(String jsonStr)
			throws JSONException {
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		List<Traveler> result = new ArrayList<Traveler>();
        
		for(int i = 0; i < jsonArray.size(); i++){
		       JSONObject jsonTravler = jsonArray.getJSONObject(i);
	           Traveler traveler  =  new Traveler();
	           if (com.trekiz.admin.common.utils.StringUtils.isNumeric(jsonTravler.getString("id"))) {
	        	   traveler.setId(jsonTravler.getLong("id"));
			   }
	           traveler.setName(jsonTravler.getString("name"));
	           traveler.setNameSpell(jsonTravler.getString("nameSpell"));
	           traveler.setSex(jsonTravler.getInt("sex"));
	           traveler.setPassportType(jsonTravler.getString("passportType"));
	           traveler.setBirthDay(DateUtils.dateFormat(jsonTravler.getString("birthDay"),"yyyy-MM-dd"));
	           traveler.setTelephone(jsonTravler.getString("telephone"));
	           traveler.setPassportCode(jsonTravler.getString("passportCode"));
	           traveler.setIssuePlace(DateUtils.dateFormat(jsonTravler.getString("issuePlace"),"yyyy-MM-dd"));
	           traveler.setPassportValidity(DateUtils.dateFormat(jsonTravler.getString("passportValidity"),"yyyy-MM-dd"));
	           traveler.setRemark(jsonTravler.getString("remark"));
	          // traveler.setIdCard(jsonTravler.getString("idCard"));
	           traveler.setPassportStatus(jsonTravler.getInt("passportStatus"));
	           if(UserUtils.getUser().getCompany().getUuid().equals("980e4c74b7684136afd89df7f89b2bee")){
	        	   traveler.setIssuePlace1(jsonTravler.getString("issuePlace1"));
	           }
	           result.add(traveler);
		}
		//List<Traveler> result = JSONArray.toList(jsonArray, Traveler.class);
		return result;
	}
	
	/**
	 * 保存签证基本信息 
	 * @param travelerId
	 * @param request
	 * @return
	 */
	private Visa saveVisa(Long travelerId, HttpServletRequest request){
		
		//保存游客前需要 删除游客签证信息
		visaService.delVisaByTravelerId(travelerId);
		
		String passportdocID = request.getParameter("passportdocID");
		String idcardfrontdocID = request.getParameter("idcardfrontdocID");
		String entry_formdocID = request.getParameter("entry_formdocID");
		String photodocID = request.getParameter("photodocID");
		String idcardbackdocID = request.getParameter("idcardbackdocID");
		String otherdocID = request.getParameter("otherdocID");
		String familyRegisterdocID = request.getParameter("familyRegisterdocID");//户口本ID
		String houseEvidencedocID = request.getParameter("houseEvidencedocID");//房产证ID
		//签证附件
		String visa_annexdocID = request.getParameter("visa_annexdocID");
		
		String forecastStartOut = request.getParameter("forecastStartOut");//预计出团时间
		String forecastContract = request.getParameter("forecastContract");//预计签约时间
		
		//---0211需求,新增预计回团时间,限定为批发商星徽四海-s--//
		String forecastBackDate = request.getParameter("forecastBackDate");//预计回团时间
		//---0211需求,新增预计回团时间,限定为批发商星徽四海-e--//
		
		String remark = request.getParameter("remark"); //签证备注信息
		
		Visa visa = new Visa();
		visa.setTravelerId(travelerId);
		//签证预订后默认状态为 请选择 -  2015-04-15--
		visa.setVisaStauts(-1);
		visa.setForecastStartOut(DateUtils.dateFormat(forecastStartOut,"yyyy-MM-dd"));
		visa.setForecastContract(DateUtils.dateFormat(forecastContract,"yyyy-MM-dd"));
		//---0211需求,新增预计回团时间,限定为批发商星徽四海-s--//
		  //需要处理空值情况:虽然星徽四海
		if(null==forecastBackDate){
			visa.setForecastBackDate(null);
		}else{
			visa.setForecastBackDate(DateUtils.dateFormat(forecastBackDate, "yyyy-MM-dd"));
		}
		//---0211需求,新增预计回团时间,限定为批发商星徽四海-e--//
		visa.setRemark(remark);
		if (null!=passportdocID&&!"".equals(passportdocID)) {
			visa.setPassportPhotoId(Long.parseLong(passportdocID));
		}
		if (null!=idcardfrontdocID&&!"".equals(idcardfrontdocID)) {
			visa.setIdentityFrontPhotoId(Long.parseLong(idcardfrontdocID));
		}
		if (null!=entry_formdocID&&!"".equals(entry_formdocID)) {
			visa.setTablePhotoId(Long.parseLong(entry_formdocID));
		}
		if (null!=photodocID&&!"".equals(photodocID)) {
			visa.setPersonPhotoId(Long.parseLong(photodocID));
		}
		if (null!=idcardbackdocID&&!"".equals(idcardbackdocID)) {
			visa.setIdentityBackPhotoId(Long.parseLong(idcardbackdocID));
		}
		if (null!=otherdocID&&!"".equals(otherdocID)) {
			visa.setOtherPhotoId(Long.parseLong(otherdocID));
		}
		//户口本
		if (null!=familyRegisterdocID && !"".equals(familyRegisterdocID)) {
			visa.setFamilyRegisterPhotoId(Long.parseLong(familyRegisterdocID));
		}
		//房产证
		if (null!=houseEvidencedocID && !"".equals(houseEvidencedocID)) {
			visa.setHouseEvidencePhotoId(Long.parseLong(houseEvidencedocID));
		}
		//签证附件
		if(null!=visa_annexdocID&&!"".equals(visa_annexdocID)){
					visa.setDocIds(visa_annexdocID);
					List<DocInfo> docs = docInfoService.getDocInfoBydocids(visa_annexdocID);
					visa.setDocs(docs);
					
		}
		// 0615 需求  限定大洋国旅新增是否需要押金和是否需要上传文件
//		int depositValue=Integer.valueOf(request.getParameter("depositValue")); //是否需要押金
//		int datumValue=Integer.valueOf(request.getParameter("datumValue")); //是否需要上传文件
//		visa.setDepositValue(depositValue);
//		visa.setDatumValue(datumValue);
		return  visaService.saveVisa(visa);
	}
	
	
	/**
     * 保存游客的其它费用
     * 
     * @param jsonCost : 其它费用的JSON数组
     * @param travelerId： 游客ID
     */
	public void saveCost(JSONArray jsonCost,Long travelerId){
		//清除费用相关信息
		costchangeService.delete(travelerId);
		for(int i = 0; i < jsonCost.size(); i++){
			JSONObject costJson = jsonCost.getJSONObject(i);
			Costchange costchange = new Costchange();
			//费用名称
	        String name = costJson.getString("cosName");
	        if(name != null && StringUtils.isNotBlank(name)){
	            costchange.setCostName(name.toString());
	        }
	        //费用币种
	        String curency = costJson.getString("currency");
	        if(curency != null && StringUtils.isNotBlank(curency)){
	            costchange.setPriceCurrency(currencyService.findCurrency(Long.parseLong(curency)));
	        }
	        //费用金额
	        String sum = costJson.getString("sum");
	        if(sum != null && StringUtils.isNotBlank(sum)){
	            costchange.setCostSum(StringNumFormat.getBigDecimalForTow(sum));
	        }
	        costchange.setTravelerId(travelerId);
	        costchangeService.save(costchange);
		}
	}
	
	/**
	 * 保存游客结算价  和  游客成本价
	 * @param travelerId 游客Id
	 * @param request
	 */
	private void savePayPrice(Traveler traveler, HttpServletRequest request){
		JSONArray payPriceObject = JSONArray.fromObject(request.getParameter("payPrice"));
		List<MoneyAmount> moneyAmountList= Lists.newArrayList();
		MoneyAmount moneyAmount = null;
		List<MoneyAmount> moneyAmountListOrigin= Lists.newArrayList();
		MoneyAmount moneyAmountOrigin = null;
		
		int orderType =VisaPreOrderController.VISA_ORDER_TYPE;
		int busindessType=VisaPreOrderController.BUSINDESS_TYPE_TRAVELER;
		
		//保存游客结算价
		/**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		String payPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
		String payPriceSerialNumOrigin = UUID.randomUUID().toString().replace("-", "");
		for(int i = 0; i < payPriceObject.size(); i++){
		       JSONObject payPrice = payPriceObject.getJSONObject(i);
		       
		        /**
		         * -------wxw added--2015-03-14----
		         * 处理某币种结算价为""是产生的问题
		         */
		        String price = payPrice.getString("price");
		        Double priceDouble=null;
		        if (null==price||"".equals(price)) {
		        	priceDouble = new Double("0");
				}else {
					priceDouble= new Double(price);
				}
		        
		        //构造游客结算价
		        /*moneyAmount = new MoneyAmount(traveler.getPayPriceSerialNum()==null?payPriceSerialNum:traveler.getPayPriceSerialNum(), //款项UUID
				payPrice.getInt("currencyId"),//币种ID
				new BigDecimal(priceDouble),//相应币种的金额
				traveler.getId(), //订单或游客ID
				Context.MONEY_TYPE_JSJ, //款项类型: 游客结算 为 14
				orderType,//订单类型
				busindessType,//
				UserUtils.getUser().getId());*/
		        
		        /**
                 * wangxinwei added 2015-10-13
                 * 签证产品预定评审后修改：重写上面一段代码，代码中方法参数个数删减
		         */
		        moneyAmount = new MoneyAmount();
		        moneyAmount.setSerialNum(traveler.getPayPriceSerialNum()==null?payPriceSerialNum:traveler.getPayPriceSerialNum());//款项UUID
		        moneyAmount.setCurrencyId(payPrice.getInt("currencyId"));//币种ID
		        moneyAmount.setAmount(new BigDecimal(priceDouble));//相应币种的金额
		        moneyAmount.setUid(traveler.getId());//订单或游客ID
		        moneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);//款项类型: 游客结算 为 14
		        moneyAmount.setOrderType(orderType);//订单类型
		        moneyAmount.setBusindessType(busindessType);//业务类型
		        moneyAmount.setCreatedBy(UserUtils.getUser().getId());//创建者
		        moneyAmountList.add(moneyAmount);
		        
		        //构造游客原始结算价
		    	/*moneyAmountOrigin = new MoneyAmount(traveler.getPayPriceSerialNum()==null?payPriceSerialNumOrigin:traveler.getOriginalPayPriceSerialNum(), //款项UUID
				payPrice.getInt("currencyId"),//币种ID
				new BigDecimal(priceDouble),//相应币种的金额
				traveler.getId(), //订单或游客ID
				Context.MONEY_TYPE_YSJSJ, //款项类型: 原始游客结算价 为21
				orderType,//订单类型
				busindessType,//
				UserUtils.getUser().getId());*/
		        /**
                 * wangxinwei added 2015-10-13
                 * 签证产品预定评审后修改：重写上面一段代码，代码中方法参数个数删减
		         */
		        moneyAmountOrigin = new MoneyAmount();
		        moneyAmountOrigin.setSerialNum(traveler.getPayPriceSerialNum()==null?payPriceSerialNumOrigin:traveler.getOriginalPayPriceSerialNum());//款项UUID
		        moneyAmountOrigin.setCurrencyId(payPrice.getInt("currencyId"));//币种ID
		        moneyAmountOrigin.setAmount(new BigDecimal(priceDouble));//相应币种的金额
		        moneyAmountOrigin.setUid(traveler.getId());//订单或游客ID
		        moneyAmountOrigin.setMoneyType(Context.MONEY_TYPE_YSJSJ);//款项类型: 原始游客结算价 为21
		        moneyAmountOrigin.setOrderType(orderType);//订单类型
		        moneyAmountOrigin.setBusindessType(busindessType);//业务类型
		        moneyAmountOrigin.setCreatedBy(UserUtils.getUser().getId());//创建者
		    	moneyAmountListOrigin.add(moneyAmountOrigin);
		}
		

		boolean flagpaysave =moneyAmountService.saveOrUpdateMoneyAmounts(traveler.getPayPriceSerialNum()==null?payPriceSerialNum:traveler.getPayPriceSerialNum(), moneyAmountList);
		boolean flagpaysaveorigin =moneyAmountService.saveOrUpdateMoneyAmounts(traveler.getPayPriceSerialNum()==null?payPriceSerialNumOrigin:traveler.getOriginalPayPriceSerialNum(), moneyAmountListOrigin);
		
		if (flagpaysave&&traveler.getPayPriceSerialNum()==null) {
			traveler.setPayPriceSerialNum(payPriceSerialNum);
			travelerService.updateSerialNumByTravelerId(payPriceSerialNum, traveler.getId());
		}
		if (flagpaysaveorigin&&traveler.getOriginalPayPriceSerialNum()==null) {
			traveler.setOriginalPayPriceSerialNum(payPriceSerialNumOrigin);
			travelerService.updateOriginalPayPriceSerialNumByTravelerId(payPriceSerialNumOrigin,traveler.getId());
		}
		
		//保存游客成本价2014-03-04
		JSONArray costPriceObject = JSONArray.fromObject(request.getParameter("costPrice"));
		List<MoneyAmount> costMoneyAmountList= Lists.newArrayList();
		MoneyAmount costMoneyAmount = null;
		
		/**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		String costPriceSerialNum = UUID.randomUUID().toString().replace("-", "");	//成本价序列号
		JSONObject costPrice = null;	
		for(int i =0 ; i < costPriceObject.size(); i++){
			costPrice = costPriceObject.getJSONObject(i);
			int currencyId = costPrice.getInt("currencyId");
			BigDecimal price = new BigDecimal(costPrice.getDouble("price"));
			Long userId = UserUtils.getUser().getId();
			costMoneyAmount = new MoneyAmount(costPriceSerialNum, currencyId, price, traveler.getId(), Context.MONEY_TYPE_CBJ, orderType, busindessType, userId);
			costMoneyAmountList.add(costMoneyAmount);
		}
		boolean flagpaysavecost =moneyAmountService.saveOrUpdateMoneyAmounts(traveler.getCostPriceSerialNum()==null?costPriceSerialNum:traveler.getCostPriceSerialNum(), costMoneyAmountList);
		if (flagpaysavecost&&traveler.getCostPriceSerialNum()==null) {
			traveler.setCostPriceSerialNum(costPriceSerialNum);
			travelerService.updateCostSerialNumByTravelerId(costPriceSerialNum,traveler.getId());
		}
	}
	
	//-------签证修改，添加游客  end,对应需求号c333---------
	
	
	
	/**
	 * 上传确认单：查询订单
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="visaUploadConfirmationHref")
	public String visaUploadConfirmationHref(@RequestParam(value="orderId", required=true)String orderId, Model model, HttpServletRequest request) {
		
	    VisaOrder pro = visaOrderService.findVisaOrder(Long.parseLong(orderId));
		model.addAttribute("orderId",orderId);
		model.addAttribute("productorder",pro);
	    return "modules/order/visaUploadConfirmation";
	}
	
	/**
	 * 上传确认单
	 * @param orderId
	 * @param orderType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="visaUploadConfirmation")	
	@ResponseBody
	public Object visaUploadConfirmation(HttpServletRequest request)throws Exception {
		net.sf.json.JSONObject resobj = new net.sf.json.JSONObject();
		String orderId = request.getParameter("orderId");
		String docFileIds = request.getParameter("docFileIds");
		if( !StringUtils.isBlank(docFileIds)&& !StringUtils.isBlank(orderId)) {
    		VisaOrder order = visaOrderService.findVisaOrder(Long.parseLong(orderId));
	        try {
	        	//保存文件
	        	String[] docFileArray = ((order.getConfirmationFileId()==null?"":order.getConfirmationFileId())+docFileIds)==null?null:((order.getConfirmationFileId()==null?"":order.getConfirmationFileId())+docFileIds).replace(",", " ").trim().split(" ");
	        	List<String> docFileList = Arrays.asList(docFileArray);
	        	Collections.sort(docFileList);
	        	docFileIds ="";
	    		for(String docId:docFileList){
	    			docFileIds = docFileIds +docId+",";
	    		}
	    		order.setConfirmationFileId(docFileIds);
	    		visaOrderService.saveVisaOrder(order);
	    		//添加操作日志
	    		visaOrderService.saveLogOperate(Context.log_type_orderform,
	    				Context.log_type_orderform_name, "订单" + orderId + "上传确认单成功", "2", 6, order.getId());
	    		resobj.put("success", "上传确认单成功");
	        } catch (Exception e) {
	        	//添加操作日志
	        	visaOrderService.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
	    				"订单" + request.getParameter("orderId") + "上传确认单失败", "2", 6, order.getId());
	            e.printStackTrace();  
	            resobj.put("error", "上传确认单失败");
	        }
		}
		 return resobj;
	}
	
	@RequestMapping(value ="downloadConfirmFiles")
	public String downloadConfirmFiles(HttpServletRequest request,Model model){
		String orderId = request.getParameter("orderId");
		if(!StringUtils.isBlank(orderId)) {
			VisaOrder order = visaOrderService.findVisaOrder(Long.parseLong(orderId));
    		String docFileIds = order.getConfirmationFileId();
    		if(!StringUtil.isBlank(docFileIds)){
    			String[] fileIds = docFileIds.replace(",", " ").trim().split(" ");
    			List<DocInfo> fileList = new ArrayList<DocInfo>();
    			for(String fileId:fileIds){
    				fileList.add(docInfoService.getDocInfo(new Long(fileId)));
    			}
    			model.addAttribute("docList", fileList);
    			model.addAttribute("orderId", orderId);
    			model.addAttribute("downloadUrl", "visa/order/userDownloadConfirmFile");
    			model.addAttribute("delUrl", "visa/order/deleteDoc");
    		}else{
    			//文件不存在
    			return "";
    		}
		}
	    return "include/downloadFiles";
	}
	
	@RequestMapping(value ="deleteDoc")
	@ResponseBody
	public Object deleteDoc(HttpServletRequest request) throws Exception {
		net.sf.json.JSONObject resobj = new net.sf.json.JSONObject();
		String orderId = request.getParameter("orderId");
		String docId = request.getParameter("docId");
		if( !StringUtils.isBlank(docId)&& !StringUtils.isBlank(orderId)) {
			VisaOrder order = visaOrderService.findVisaOrder(Long.parseLong(orderId));
	        try {
	        	DocInfo doc = docInfoService.getDocInfo(new Long(docId));
	        	if(doc.getCreateBy().getId().longValue() == UserUtils.getUser().getId().longValue()){
	        		String docIds = order.getConfirmationFileId();
	        		String downloadIds = order.getDownloadFileIds();
	        		docIds = docIds.replace(docId+",", "");
	        		order.setConfirmationFileId(docIds);
	        		if(!StringUtils.isBlank(downloadIds)){
	        			downloadIds = downloadIds.replace(docId+",", "");
	        			order.setDownloadFileIds(downloadIds);
	        		}
	        		visaOrderService.saveVisaOrder(order);
	        		
	        		docInfoService.delDocInfoById(new Long(docId));
	        		//添加操作日志
	        		orderService.saveLogOperate(Context.log_type_orderform,
		    				Context.log_type_orderform_name, "订单" + orderId + "上传确认单成功", "2", Context.ORDER_TYPE_JP, order.getId());
	        		resobj.put("success", "ok");
	        	}else{
	        		resobj.put("error", "删除确认单失败");
	        	}
	        	//保存文件
	        } catch (Exception e) {
	        	//添加操作日志
	        	orderService.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
	    				"订单" + request.getParameter("orderId") + "上传确认单失败", "2", Context.ORDER_TYPE_JP, order.getId());
	            e.printStackTrace();  
	    		resobj.put("error", "删除确认单失败");
	        }
		}
	    return resobj;
	}
	
	@RequestMapping(value ="zipconfirmdownload/{orderIds}")
	public String zipconfirmdownload(@PathVariable("orderIds")String orderIds,HttpServletResponse response){
		String[] orderIdArray = orderIds.replace(",", " ").trim().split(" ");
		String docid = "";
		for(String str:orderIdArray){
			VisaOrder order = visaOrderService.findVisaOrder(Long.parseLong(str));
			docid = docid + (order.getConfirmationFileId()==null?"":order.getConfirmationFileId());
			order.setDownloadFileIds(order.getConfirmationFileId());
			visaOrderService.saveVisaOrder(order);
		}
	    return "redirect:" + Global.getAdminPath() + "/sys/docinfo/zipdownload/" + docid+"/confirm";
	}
	
	@RequestMapping(value ="userDownloadConfirmFile/{orderId}/{docid}")
	public String userDownloadConfirmFile(@PathVariable("docid") Long docid,@PathVariable("orderId") Long orderId,HttpServletResponse response) {
		//
		VisaOrder order = visaOrderService.findVisaOrder(orderId);
		String downloadIds = order.getDownloadFileIds();
		if(!StringUtils.isBlank(downloadIds)){
			downloadIds = downloadIds.replace(docid+",", "");
			String[] docIdArray = (downloadIds+docid+",").replace(",", " ").trim().split(" ");
			List<String> docIdList =Arrays.asList(docIdArray);
			Collections.sort(docIdList);
			String downloadIdsString = "";
			for(String downLoadId:docIdList){
				downloadIdsString = downloadIdsString+downLoadId+",";
			}
			order.setDownloadFileIds(downloadIdsString);
		}else{
			order.setDownloadFileIds(docid+",");
		}
		visaOrderService.saveVisaOrder(order);
		return "redirect:" + Global.getAdminPath() + "/sys/docinfo/download/" + docid;
	}
	
	
	
	/**
     * 根据 orderPaySerialNum 更新打印状态
     * @param response
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value ="updateOrderContacts")
	@ResponseBody
	@Transactional
    public Object updateOrderContacts(HttpServletResponse response,
       	    Model model, HttpServletRequest request) throws Exception { 
    	Map<String, Object> map = new HashMap<String, Object>();
    	String data = request.getParameter("data");
    	String agent_name  =request.getParameter("agent_name");
    	String agentId = request.getParameter("agentId");
		data = java.net.URLDecoder.decode(data, "utf-8");
    	String orderId =  request.getParameter("orderId");
    	visaOrderService.updateOrderContacts(orderId,data,agentId,agent_name);
    	
    	return map;
	   
    
    }
    /**
     * 0214需求,面签通知下载,既包括原先的面签资料,还包括上传的附件
     * @param id
     * @param type
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    @RequestMapping(value="interviewNoticeAll")
	public ResponseEntity<byte[]> downloadInterviewNoticeAll(@RequestParam String id,
			@RequestParam String type,
			HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{
		//面签通知文件列表
		List<File> fileList = new ArrayList<File>();
		if(null == id || null == type || "".equals(id) || "".equals(type) ){
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter pw=response.getWriter();
			pw.println("没有面签通知!");
			return null;
		}
		List<Long> longArray = new ArrayList<Long>();
		if("cantuan".equals(type)){
			//根据参团团号获得所有签证订单编号数组
			longArray = visaOrderService.getOrderIdByGroupCode(id);
		}else{
			//根据订单id获得签证面签通知的文件列表
			longArray.add(Long.valueOf(id));
		}
		
		if(null != longArray && longArray.size()>0){
			//循环数组,获取每个签证订单下的面签通知
			//将单个订单的面签通知文件列表,汇总 到总的面签通知文件列表中,
			for(int i=0;i<longArray.size();i++){
				List<File> tempFileList = visaInterviewNoticeService.mianqiantongzhiByOrderId(longArray.get(i));
				if(null != tempFileList && tempFileList.size()>0){
					for(int j=0;j<tempFileList.size();j++){
						fileList.add(tempFileList.get(j));
					}
				}
			}
			//0214需求:面签通知新增上传附件-s//
			//根据订单id从表visa_order_file表中获得该订单所有的未删除的docinfo_id
			List<Map<String,Object>> listVOF=visaInterviewNoticeService.findDocInfoListByOrderId(id);
			for (Map<String, Object> tempMap : listVOF) {
				try {
		    		DocInfo docInfo = docInfoService.getDocInfo(Long.parseLong(tempMap.get("id").toString()));
		    		if(docInfo!=null){
		    			/*int pos=docInfo.getDocPath().lastIndexOf("\\");
		    			String docPathReadable=docInfo.getDocPath().substring(0, pos)+"\\"+docInfo.getDocName();*/
		    			 //考虑到操作系统对路径的表示不同
		    			int pos=docInfo.getDocPath().lastIndexOf(File.separator);
		    			String docPathReadable=docInfo.getDocPath().substring(0, pos)+File.separator+docInfo.getDocName();
		    			/*File fileAttached = new File(Global.getBasePath() +File.separator+docPathReadable);*/
		    			File fileAttached = new File(Global.getBasePath() +File.separator+docInfo.getDocPath());//获得上传附件的文件
		    			File fileTemp=new File(Global.getBasePath() +File.separator+docPathReadable);//建立临时文件,用于上传的原文件的重命名使用
		    			fileAttached.renameTo(fileTemp);//将名字命名成正确名字的文件
		    			/*System.out.println("path: "+docInfo.getDocPath());
		    			int pos=docInfo.getDocPath().lastIndexOf("\\");
		    			System.out.println(docInfo.getDocPath().substring(0, pos)+"\\"+docInfo.getDocName());
		    			System.out.println("---------------------------------------");*/
		    			
//		    			response.reset();
//	    				response.setHeader("Content-Disposition", "attachment; filename="+new String(docInfo.getDocName().getBytes("gb2312"), "ISO-8859-1"));
//	    				response.setContentType("application/octet-stream; charset=utf-8");
		    			fileList.add(fileTemp);	
		    			
		    		}
		    		
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//0214需求:面签通知新增上传附件-e//
		}
		if(null != fileList && fileList.size()>0){
		try {
			//将文件列表变成压缩包,进行下载
			DownLoadController.downLoadFiles(fileList, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}else{
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter pw=response.getWriter();
			pw.println("没有面签通知!");
			
		}
		return null;
	}
	
	
	/**
	 * 0064需求   djw
	 * 游客批量加入批次
	 * @return
	 */
	@RequestMapping(value ="visaBatchAddVisitors")
	public String visaBatchAddVisitors(Model model, @RequestParam String batchNo,HttpServletRequest request,HttpServletResponse response,VisaOrderForm visaOrderForm){
		if(visaOrderForm == null){
			visaOrderForm = new VisaOrderForm();
		}
		//部门ID及角色查询
		DepartmentCommon common = departmentService.setDepartmentPara();
		
		//批量操作-游客列表
		Page<Map<String,Object>> travelList = visaOrderService.searchTravelList(request ,response, visaOrderForm,"qianwu",common);
		
		//填充游客列表结果集
		request.setAttribute("travelList", travelList.getList());
		request.setAttribute("page", travelList);
		//-----0064需求--start//
		String refundDate=request.getParameter("refundDate");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("batchNo",batchNo);
		model.addAttribute("refundDate",(StringUtils.isNotEmpty(refundDate))?sdf.format(DateUtils.string2Date(refundDate)):null);
		//System.out.println("------"+((StringUtils.isNotEmpty(refundDate))?sdf.format(DateUtils.string2Date(refundDate)):null));
		//-----0064需求--end//
		//填充初始数据
		initializationData(request);
		//填充查询条件
		request.setAttribute("visaOrderForm", visaOrderForm);
		//查询条件是否展开
		request.setAttribute("flag", request.getParameter("showFlag"));
		//展示哪个列表页
		request.setAttribute("showList","youke");
		//根据公司ID判断按钮是否显示
		processMenuProcRight_qianWu(request);
		
		return "modules/visa/visaBatchAddVisitors";
	}
    /**
     * 0064需求,根据批次号,获得该批次已有的traveler_id和visa_id
     * @param request
     * @return
     * @throws Exception
     */
	@RequestMapping(value="getInfoByBatchNo")
	@ResponseBody
	public List<Map<String,Object>>  getInfoByBatchNo(HttpServletRequest request) throws Exception{
		String joinBatchNo=request.getParameter("joinBatchNo");
		List<Map<String,Object>>  infoMap=null;
		//System.out.println("--------"+joinBatchNo);
		if(StringUtils.isNotEmpty(joinBatchNo)){ //批次号不为空时进行查询
			infoMap=visaOrderService.getTravelerIdsVisaIds(joinBatchNo);
		}
		return infoMap;
	}
	
	/**
	 * 0064需求:校验游客是否可加入该批次
	 * @param request
	 * @return
	 */
	@RequestMapping(value="checkTravelerJoinBatch")
	@ResponseBody
	public Map<String, Object> checkTravelerJoinBatch(HttpServletRequest request){
		String travelerIds=request.getParameter("travelerIds")+"0";
		String visaIds=request.getParameter("visaIds")+"0";
		String batchNo=request.getParameter("batchNo");
		//校验游客状态是否发生变化,成为不满足条件的用户
		Map<String, Object> resultMap  = visaOrderService.checkBatchJkHqx4activiti(visaIds,travelerIds);
		//查询该批次的是否是已提交,同一批次下,所有游客的批次提交状态是相同的
		List<Map<String, Object>> submitList=visaOrderService.checkSubmit(batchNo);
		resultMap.put("submitFlag", submitList);
		return resultMap;
	}
	/**
	 * 0064需求批量借款加入批次
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "batchJk4activiti")
	@ResponseBody
	public Object batchJk4activiti(HttpServletRequest req){
		Map<String, Object> resultMap = null;
		
//		Long companyId = UserUtils.getUser().getCompany().getId();
		// 环球行的情况下


			String type = req.getParameter("type");
			String visaIds = req.getParameter("visaIds")+"0";
			String orderIds = req.getParameter("orderIds")+"0";
			String travelerIds = req.getParameter("travelerIds")+"0";
			String persons = req.getParameter("passportOperator")+"0";
			String dates = req.getParameter("passportOperateTime")+"0";
			String moneys = req.getParameter("moneys")+"0";
			String others = req.getParameter("passportOperateRemark")+"0";
			//***270需求--添加"还款日期"项-s****//
			//TODO-270需求临时性屏蔽以后上线用
			String refundDate=req.getParameter("refundDate");//获得选择的还款日期
			//***270需求--添加"还款日期"项-e****//
			String[] travelerIDS = travelerIds.split(",");
			String[] visaIDs = visaIds.split(",");
			String[] orderIDs = orderIds.split(",");
			String[] borrowAmounts = moneys.split(",");
			String[] borrowRemarks = others.split(",");
			// 生成批次号
			String batchNo=req.getParameter("batchNo");
			// 批次总人数
			int batchPersonCount = travelerIDS.length - 1;
			// 批次总金额
			BigDecimal batchtotalMoney = BigDecimal.ZERO;
			for (int i = 0; i < travelerIDS.length - 1; i++) {
				//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {
					batchtotalMoney = batchtotalMoney.add(new BigDecimal(borrowAmounts[i]));
				}
			}
			//获取人民币币种id
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
			buffer.append(" And c.del_flag = 0 AND c.create_company_id=");
			buffer.append(UserUtils.getUser().getCompany().getId());
			List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
			Integer borrowtotalcurrencyId = 0;
			String borrowtotalcurrencyName="人民币";
			for (int i = 0; i < currencylist.size(); i++) {
				if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
					borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
					borrowtotalcurrencyName = (String)currencylist.get(i).get("currency_name");
					break;
				}
			}
			//生成uuid
			//String uuid = UUID.randomUUID().toString();//随机算法生成的uuid
			String uuid=null;
			//----根据批次号和已有批次查询表visa_flow_batch_opration的uuid,因为对应关联表batch_traveler_relation的batch_uuid,故这里不能用随机生成的uuid-s//
			 StringBuffer sb=new StringBuffer();
			 sb.append(" SELECT  ")
			   .append(" vfbo.uuid AS 'BTR_batch_uuid'   ")
			   .append(" FROM visa_flow_batch_opration vfbo  ")
			   .append(" WHERE vfbo.batch_no = '"+batchNo+"' AND vfbo.busyness_type = 2;  "); //business_type=2表示借款
			List <Map<String,String>>tempList= visaFlowBatchOprationDao.findBySql(sb.toString(), Map.class);//这里应该只能查到1条记录
			for (Map<String, String> tempMap : tempList) {
				uuid=tempMap.get("BTR_batch_uuid");
			}
			//------------------------用随机生成的uuid会造成批量操作记录里删除原有游客批次消失的情况--需求0064-----------------------------------------e//
			//1.创建审核记录
			if ("2".equals(type)) {
				
				try {
					resultMap=activityVisaHQXBorrowMoneyService.visaBatchJk4activiti(batchNo,visaIds,travelerIds,persons,dates,moneys,others);
				} catch (Exception e) {
					if(resultMap==null){
						resultMap = new HashMap<String,Object>();
					}
					resultMap.put("msg", "流程启动失败!");
					return resultMap;
				}
			}
			
			VisaFlowBatchOpration record = new VisaFlowBatchOpration();
			record.setUuid(uuid);
			record.setBatchNo(batchNo);
			record.setBusynessType("2");
			record.setBatchPersonCount(batchPersonCount);
			record.setBatchTotalMoney(batchtotalMoney.toString());
			record.setCreateUserId(UserUtils.getUser().getId());
			record.setCreateUserName(UserUtils.getUser().getName());
			record.setCreateTime(new Date());
			record.setPrintStatus("0");
			//270-qyl-begin把还款日期保存到后台
			try {
				//--0064需求处理还款日期为空的情况.
				if(StringUtils.isNoneEmpty(refundDate)){
					record.setRefundDate(new SimpleDateFormat("yyyy-MM-dd").parse(refundDate));
				}else{
					record.setRefundDate(null);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//270-qyl-end
			record.setCurrencyId(borrowtotalcurrencyId);
			record.setCurrencyName(borrowtotalcurrencyName);
			record.setIsNewReview(2);//用于表示是新审核
			if ("1".equals(type)) {
				record.setReviewStatus("99");
				record.setIsSubmit("1");
			} else if ("2".equals(type)) {
				record.setReviewStatus("1");
				record.setIsSubmit("2");
			}
	        
			//2.创建批次借款记录
			//travelerIDS.length - 1
			String updatetime = DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
			//String updateSql= "UPDATE visa_flow_batch_opration vfbo SET vfbo.batch_person_count = vfbo.batch_person_count +"+(travelerIDS.length - 1)+",vfbo.update_time = '"+ updatetime+"' WHERE vfbo.batch_no = '"+batchNo+"' AND vfbo.busyness_type = 2";
			//batchRecordDao.updateBySql(updateSql);
			String updateSql= "UPDATE visa_flow_batch_opration vfbo SET vfbo.batch_person_count = vfbo.batch_person_count + ?,vfbo.update_time = ?,vfbo.batch_total_money=vfbo.batch_total_money+? WHERE vfbo.batch_no = ? AND vfbo.busyness_type = 2";
			batchRecordDao.updateBySql(updateSql,travelerIDS.length - 1,updatetime,batchtotalMoney,batchNo);
	
			//3.保存游客与批次的关系
			for(int i = 0; i < travelerIDS.length - 1; i++){
				//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {
					uuid = UUID.randomUUID().toString();
					BatchTravelerRelation relation = new BatchTravelerRelation();
					relation.setUuid(uuid);
					relation.setBatchUuid(record.getUuid());
					relation.setBatchRecordNo(batchNo);
					relation.setTravelerId(Long.parseLong(travelerIDS[i]));
					relation.setVisaId(Long.parseLong(visaIDs[i]));
					relation.setOrderId(Long.parseLong(orderIDs[i]));
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
					relation.setTravelerName(traveler.getName());
					relation.setBusinessType(1);//业务类型 1:借款 2：还收据  3：借护照 4：还护照
					relation.setTravellerBorrowMoney(new BigDecimal(borrowAmounts[i]));
					relation.setRemark(borrowRemarks[i]);
					relation.setCreatebyId(UserUtils.getUser().getId());
					relation.setCreatebyName(UserUtils.getUser().getName());
					relation.setIsSubmit("1");
					relation.setSaveTime(new Date());
					if ("2".equals(type)) {
						relation.setSubmitbyId(UserUtils.getUser().getId());
						relation.setSubmitbyName(UserUtils.getUser().getName());
						relation.setIsSubmit("2");
						relation.setSubmitTime(new Date());
					}
					batchTravelerRelationDao.getSession().save(relation);
				}
			}
		
		return resultMap;
	}
	/**
	 * 0318需求
	 * 获得要修改的游客的基本信息,尤其是游客的签证状态和借款状态
	 * @param request
	 * @param response
	 * @param visaOrderForm
	 * @return
	 */
	@RequestMapping(value="checkTraveler")
	@ResponseBody
	public Object checkTraveler(HttpServletRequest request,HttpServletResponse response,VisaOrderForm visaOrderForm){
		//System.out.println("---------"+request.getParameter("travelerId"));
		String travelerId=request.getParameter("travelerId");//游客的id
		//部门ID及角色查询
	    DepartmentCommon common = departmentService.setDepartmentPara();
	    Page<Map<String,Object>> travelerList = visaOrderService.searchTravelerListByTravelerId(request,response,visaOrderForm,"xiaoshou",common,travelerId);//必须是查到唯一一条记录
		return travelerList.getList();
	}
	
	
	/**
	 * @author jiawei.du  需求0065&0099
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "batchSetingIN")
	@Transactional
	public Map<String, Object> batchSetingIN(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		
		//String orderid = request.getParameter("orderid");//订单ID
			                                                                                                                           
		String visaIds = request.getParameter("visaIds")+"0";
		String travellerIds = request.getParameter("travelerIds")+"0";
		
				
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map = visaOrderService.checkInterviewNotice(visaIds,travellerIds);
		
		return map;
	}
	
	
	/**
	 * 面签通知游客信息
	 * @param batchNo
	 * @return
	 */
	@RequestMapping(value="getBatchMqtzTravelerList")
	@ResponseBody
	public List<Map<String,Object>> getBatchMqtzTravelerList(HttpServletRequest request,HttpServletResponse response){
		String batchNo = request.getParameter("batchNo");
		String areaId = request.getParameter("areaId");
		String countryId = request.getParameter("countryId");
		String travelerName = request.getParameter("travelerName");
		Map<String,String> condition = new HashMap<String,String>();
		condition.put("batchNo", batchNo);
		condition.put("areaId", areaId);
		condition.put("countryId", countryId);
		condition.put("travelerName", travelerName);
		//List<Map<String,Object>> traveler = visaOrderService.findBatchHsjTraveler(condition);
		List<Map<String,Object>> traveler = visaOrderService.findBatchMqtzTraveler(condition);
		return traveler;
	}
	
}
