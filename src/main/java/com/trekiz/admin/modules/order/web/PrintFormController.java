package com.trekiz.admin.modules.order.web;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.formBean.PrintFormBean;
import com.trekiz.admin.modules.order.formBean.PrintParamBean;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.PrintFormService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.review.visaborrowmoney.service.IVisaBorrowMoneyService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.borrowing.airticket.service.NewOrderReviewService;

/**
 * 
 *打印单操作控制类
 *打印单详情、下载等功能
 *add by zhanghao 20150507
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/printForm")
public class PrintFormController extends BaseController{

	@Autowired
	private PrintFormService printFormService;
	
	@Autowired
	private IAirTicketOrderService ariticketOrderService;
	@Autowired
    private OrderCommonService orderService;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private RefundService refundService;
	@Autowired
	private IVisaBorrowMoneyService visaBorrowMoneyService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private NewOrderReviewService newOrderReviewService;
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
    /**
	 * 借款单 zhanghao 20150506 
	 */
	@RequestMapping(value = "borrowMoneyForm")
	public String borrowMoneyForm(Model model,HttpServletRequest request, HttpServletResponse response) {
		String reviewId = request.getParameter("reviewId");
		String orderType = request.getParameter("orderType");
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		
		//根据审核表ID获取借款单数据
		PrintFormBean printFormBean = printFormService.buildPrintFormBean(Long.parseLong(reviewId));
		//根据订单类型，获取打印单名称
		printFormBean.setPrintFormName(getOrderTypeNameByOrderType(orderType)+"借款单");
		printFormBean.setBorrowDept(getOrderTypeNameByOrderType(orderType)+"部");
		printFormBean.setProductCreater(getProductCreaterByReviewId(printFormBean.getOrderId(), orderType));
		
		if("pay".equals(option)) {
			//45需求，以每次支付的金额作为借款金额
			String revBorrowAmount = "";
			String revBorrowAmountDx = "";
			if(StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, orderType);
				if(payDetail != null) {
					if(Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != BigDecimal.ZERO.doubleValue()) {
						revBorrowAmount = payDetail.getRefundRMBDispStyle();
						revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.valueOf(revBorrowAmount.replaceAll(",", "")));
					}
				}
			}
			printFormBean.setRevBorrowAmount(revBorrowAmount);
			printFormBean.setRevBorrowAmountDx(revBorrowAmountDx);
		}
								
		request.setAttribute("printFormBean", printFormBean);
		request.setAttribute("orderType", orderType);
		request.setAttribute("payId", payId);
		request.setAttribute("option", option);
		
		//----- majiancheng added 20151019 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
		//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		Review review = reviewService.findReviewInfo(Long.parseLong(reviewId));
		
		if(review != null) {
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				model.addAttribute("payStatus","0");
			}else {
				model.addAttribute("payStatus", review.getPayStatus());
			}
		} else {
			model.addAttribute("payStatus","0");
		}
				
		return "modules/borrowmoney/borrowMoneyFeePrint";
	}
	
	/**
	 * 打印申请单
		* 
		* @param 
		* @return ResponseEntity<byte[]>
		* @author majiancheng
		* @Time 2015-5-8
	 */
	@RequestMapping(value="downloadBorrowMoneySheet")
	public ResponseEntity<byte[]> downloadBorrowMoneySheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String reviewId = request.getParameter("reviewId");
		String orderType = request.getParameter("orderType");
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		
		PrintFormBean printFormBean = printFormService.buildPrintFormBean(Long.parseLong(reviewId));
		printFormBean.setPrintFormName(getOrderTypeNameByOrderType(orderType)+"借款单");
		printFormBean.setBorrowDept(getOrderTypeNameByOrderType(orderType)+"部");
		printFormBean.setProductCreater(getProductCreaterByReviewId(printFormBean.getOrderId(), orderType));
		
		//----- majiancheng added 20151019 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
		//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		Review review = reviewService.findReviewInfo(Long.parseLong(reviewId));
		
		if(review != null && (review.getPayStatus() == 1)) {
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				printFormBean.setPayDateStr("     年   月   日");
			}else {
				printFormBean.setPayDateStr(DateUtils.date2String(printFormBean.getPayDate(), "yyyy 年 MM 月 dd 日"));
			}
		} else {
			printFormBean.setPayDateStr("     年   月   日");
		}
		
		if("pay".equals(option)) {
			//45需求，以每次支付的金额作为借款金额
			String revBorrowAmount = "";
			String revBorrowAmountDx = "";
			if(StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, orderType);
				if(payDetail!=null && (Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != 
					BigDecimal.ZERO.doubleValue())) {
					revBorrowAmount = payDetail.getRefundRMBDispStyle();
					revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.valueOf(revBorrowAmount.replaceAll(",", "")));
				}			
			}
			
			printFormBean.setRevBorrowAmount(revBorrowAmount);
			printFormBean.setRevBorrowAmountDx(revBorrowAmountDx);
		}
			
		File file = printFormService.createBorrowMoneySheetDownloadFile(printFormBean);
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  printFormBean.getPrintFormName() + nowDate + ".doc";
		OutputStream os = null;
    	try {
			if(file != null && file.exists()){
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
		    	os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(file));
	            os.flush();
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
		return null;
	}
	
	private String getOrderTypeNameByOrderType(String orderType) {
		String printFormName = "";
		if(StringUtils.isEmpty(orderType)) {
			return "";
		}
		
		if(Context.ORDER_STATUS_SINGLE.equals(orderType)) {
			printFormName = "单团";
		} else if(Context.ORDER_STATUS_LOOSE.equals(orderType)) {
			printFormName = "散拼";
		} else if(Context.ORDER_STATUS_STUDY.equals(orderType)) {
			printFormName = "游学";
		} else if(Context.ORDER_STATUS_BIG_CUSTOMER.equals(orderType)) {
			printFormName = "大客户";
		} else if(Context.ORDER_STATUS_FREE.equals(orderType)) {
			printFormName = "自由行";
		} else if(Context.ORDER_STATUS_AIR_TICKET.equals(orderType)) {
			printFormName = "机票";
		} else if(Context.ORDER_STATUS_CRUISE.equals(orderType)) {
			printFormName = "游轮";
		}
		
		return printFormName;
	}
	
	private String getProductCreaterByReviewId(Long orderId, String orderType) {
		String productCreater = "";
		if(orderId == null) {
			return productCreater;
		}
		
		if(Context.ORDER_STATUS_AIR_TICKET.equals(orderType)) {
			AirticketOrder airticketOrder = ariticketOrderService.getAirticketorderById(orderId);
			productCreater = airticketOrder.getCreateBy().getName();
		} else {
			ProductOrderCommon pro = orderService.getProductorderById(orderId);
			productCreater = pro.getCreateBy().getName();
		}
		
		return productCreater;
	}
	
	@ResponseBody
	@RequestMapping(value = "updateReviewPrintInfo")
	public Object updateReviewPrintInfo(Long reviewId) {
		Review review =  reviewService.findReviewInfo(reviewId);
		if (null!=review && null == review.getPrintFlag()) {
			Date printDate = new Date();
			reviewService.updateReviewPrintInfoById(printDate, reviewId);
			
			return DateUtils.formatCustomDate(printDate, "yyyy/ MM /dd HH:mm");
		}
		return "";
	}

	/**
	 * 借款付款批量打印接口,根据参数分别调用不同的方法来获取数据。
	 * @param
	 * @return
     */
	@RequestMapping(value = "borrowMoneyBatchPrint")
	public String borrowMoneyBatchPrint(HttpServletRequest request, Model model) {
		List<Map<String,Object>> result = new ArrayList<>();

		String printInfo = request.getParameter("printInfo");
		List<PrintParamBean> paramBeanList = parseBatchPrintParam(printInfo);
		for (PrintParamBean paramBean : paramBeanList) {
			Map<String,Object> printMap = null;
			Integer orderType = paramBean.getOrderType();

			if (orderType == 11 || orderType == 12){
				Integer reviewId = Integer.parseInt(paramBean.getReviewId());
				printMap = costManageService.getJKRebates4HotelORIsland(reviewId, orderType);
			} else if (paramBean.getOrderType() == 6 && "2".equals(paramBean.getIsShowPrint())){
				printMap = visaBorrowMoneyService.getVisaBorrowMoney4XXZPrintMap(paramBean);
			} else if (orderType != 6 && paramBean.getReviewFlag() == 1){
				printMap = printFormService.getBorrowMoneyFormMap(paramBean);
			} else if (orderType != 6 && paramBean.getReviewFlag() == 2){
				printMap = newOrderReviewService.getBorrowMoneyFormMap(paramBean);
			}
			result.add(printMap);
		}
		model.addAttribute("printList",result);
		return "modules/borrowmoney/borrowMoneyBatchPrint";
	}

	private List<PrintParamBean> parseBatchPrintParam(String printInfo){
		List<PrintParamBean> paramBeanList = new ArrayList<>();
		if (StringUtils.isBlank(printInfo)){
			return paramBeanList;
		}

		String[] printArr = printInfo.split(","); // 每一个打印对象用逗号分隔
		for (int i = 0; i < printArr.length; i++) {
			String printItem = printArr[i];
			String[] itemParam = printItem.split("_"); // 打印参数之间用下划线分隔

			PrintParamBean paramBean = new PrintParamBean();
			paramBean.setReviewId(itemParam[0]); // 0 是reviewId
			paramBean.setOrderType(Integer.parseInt(itemParam[1])); // 1 是orderType
			paramBean.setReviewFlag(Integer.parseInt(itemParam[2])); // 2 是ReviewFlag，新老审批
			paramBean.setIsShowPrint(itemParam[3]); // 3 是是否显示打印
			paramBean.setOption("order");

			paramBeanList.add(paramBean);
		}
		return paramBeanList;
	}

	@ResponseBody
	@RequestMapping(value = "borrowMoneyBatchUpdatePrintInfo")
	public Map<String,Object> borrowMoneyBatchUpdatePrintInfo(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<>();
		String printInfo = request.getParameter("updateInfo");
		JSONArray printArr = JSONArray.parseArray(printInfo);
		if (printArr == null){
			result.put("success",0); // 0 表示未成功，1表示成功
			return result;
		}

		Date currentDate = new Date();
		String userId = UserUtils.getUser().getId().toString();
		for (int i = 0; i < printArr.size(); i++) {
			String itemStr = printArr.getString(i);
			String[] itemArr = itemStr.split("_");
			String orderType = itemArr[0];
			String reviewIdStr = itemArr[1];
			String reviewFlag = itemArr[2];
			String printDateStr = itemArr[3];

			if ("6".equals(orderType)){
				try {
					Date printDate = DateUtils.dateFormat(printDateStr,"yyyy/ MM /dd HH:mm");
					Long reviewId = Long.parseLong(reviewIdStr);
					visaBorrowMoneyService.updateReviewPrintInfoById(reviewId,printDate);
				} catch (Exception e) {
					e.printStackTrace();
					result.put("success",0); // 0 表示未成功，1表示成功
					return result;
				}
			}else if ("1".equals(reviewFlag)){
				Long reviewId = Long.parseLong(reviewIdStr);
				Review review =  reviewService.findReviewInfo(reviewId);
				if (null!=review && null == review.getPrintFlag()) {
					reviewService.updateReviewPrintInfoById(currentDate, reviewId);
				}
			}else if ("2".equals(reviewFlag)){
				ReviewNew reviewNew = processReviewService.getReview(reviewIdStr);
				if (null != reviewNew && 0 == reviewNew.getPrintStatus()) {
					processReviewService.updatePrintFlag(userId, "1", reviewIdStr, currentDate);
				}
			}
		}
		result.put("success",1); // 0 表示未成功，1表示成功
		result.put("printDate",DateUtils.formatCustomDate(currentDate, "yyyy/ MM /dd HH:mm")); // 当前日期为打印日期
		return result;
	}
	
}
