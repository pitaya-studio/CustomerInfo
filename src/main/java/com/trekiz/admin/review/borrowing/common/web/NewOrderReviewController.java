package com.trekiz.admin.review.borrowing.common.web;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderExitGroupReviewVO;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.formBean.PrintFormBean;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.service.PrintFormService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.order.service.TransferMoneyService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.service.ReviewLogService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.borrowing.airticket.service.NewOrderReviewService;

/**
 *
 * @author songyang 
 * 借款审批公用Controller
 */
@SuppressWarnings("unused")
@Controller
@RequestMapping(value = "${adminPath}/newOrderReview/manage")
public class NewOrderReviewController extends BaseController{

	@Autowired
    private OrderCommonService orderService;
	@Autowired
    private OrderReviewService orderReviewService;
	@Autowired
	private NewOrderReviewService newOrderReviewService;
	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
    @Autowired
    private ActivityGroupService activityGroupService;
    @Autowired
    TransferMoneyService transferMoneyService;
    @Autowired
	private MoneyAmountService moneyAmountService;
    @Autowired
	private AreaService areaServce;
    @Autowired
	private TravelerService travelerService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ReviewLogService reviewLogService;
    @Autowired
    private com.quauq.review.core.engine.ReviewService processReviewService;
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	@Autowired
	private PrintFormService printFormService;
	@Autowired
	private IAirTicketOrderService ariticketOrderService;
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	@Autowired
	private RefundService refundService;

    private static Logger logger = LoggerFactory.getLogger(NewOrderReviewController.class);

	/**
	 * 打印借款单
	 * @author yunpeng.zhang
	 * @createDate 2015年12月10日15:54:24
	 * @param request
	 * @param response
	 * @param reviewId
	 * @param orderType
	 * @param map
	 * @return
     * @throws Exception
     */
	@RequestMapping(value="downloadBorrowMoneySheet")
	public ResponseEntity<byte[]> downloadBorrowMoneySheet(HttpServletRequest request, HttpServletResponse response,
														   String reviewId, String orderType, Map<String, Object> map)
			throws Exception {
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		MultiValueMap<Integer, User> resultMap =
				reviewReceiptService.obtainReviewer4Receipt(companyUuid, ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY, reviewId);
		PrintFormBean printFormBean = newOrderReviewService.buildPrintFormBean(reviewId, resultMap, map);
		printFormBean.setPrintFormName(getOrderTypeNameByOrderType(orderType)+"借款单");
		printFormBean.setBorrowDept(getOrderTypeNameByOrderType(orderType)+"部");
		printFormBean.setProductCreater(getProductCreaterByReviewId(printFormBean.getOrderId(), orderType));

		//----- majiancheng added 20151019 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
		//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		Long companyId = UserUtils.getUser().getCompany().getId();
		ReviewNew reviewNew = processReviewService.getReview(reviewId);

		if(reviewNew != null && (reviewNew.getPayStatus() == 1)) {
			if (companyId==88||companyId==68) {
				printFormBean.setPayDateStr("     年   月   日");
			}else {
				printFormBean.setPayDateStr(DateUtils.date2String(printFormBean.getPayDate(), "yyyy 年 MM 月 dd 日"));
			}
		} else {
			printFormBean.setPayDateStr("     年   月   日");
		}
		
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		if("pay".equals(option)) {
			//45需求，以每次借款的金额为打印凭单的借款金额
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
			
		File file = newOrderReviewService.createBorrowMoneySheetDownloadFile(printFormBean);

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

	/**
	 * 在点击打印的时候，更新打印状态
	 * @author yunpeng.zhang
	 * @createDate 2015年12月10日15:53:22
	 * @param reviewId 审批id
	 * @return
     */
	@ResponseBody
	@RequestMapping(value = "/updateReviewPrintInfo")
	public Object updateReviewPrintInfo(String reviewId) {
		ReviewNew reviewNew = processReviewService.getReview(reviewId);
		if (null != reviewNew && 0 == reviewNew.getPrintStatus()) {
			Date printDate = new Date();
			processReviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", reviewId, printDate);
			return DateUtils.formatCustomDate(printDate, "yyyy/ MM /dd HH:mm");
		}
		return "";
	}

	/**
	 * 显示借款单
	 * @author yunpeng.zhang
	 * @createDate 2015年12月10日15:52:22
	 * @param reviewId  审批id
	 * @param orderType 订单类型
	 * @param map		放入模型中的 map
     * @return
     */
	@RequestMapping("/showBorrowMoneyForm")
	public String showBorrowMoneyForm(String reviewId, String orderType, Map<String, Object> map, HttpServletRequest request) {
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		MultiValueMap<Integer, User> resultMap = reviewReceiptService.obtainReviewer4Receipt(companyUuid, ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY, reviewId);

		//根据审批表ID获取借款单数据
		PrintFormBean printFormBean = newOrderReviewService.buildPrintFormBean(reviewId, resultMap, map);
		
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		if("pay".equals(option)) {
			//45需求，以每次借款的金额为打印凭单的借款金额
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

		//根据订单类型，获取打印单名称
		printFormBean.setPrintFormName(getOrderTypeNameByOrderType(orderType)+"借款单");
		printFormBean.setBorrowDept(getOrderTypeNameByOrderType(orderType)+"部");
		printFormBean.setProductCreater(getProductCreaterByReviewId(printFormBean.getOrderId(), orderType));						//经办人
		map.put("reviewId", reviewId);
        map.put("payId", payId);
        map.put("option", option);
		map.put("printFormBean", printFormBean);
		map.put("orderType", orderType);
		return "/review/borrowing/common/newBorrowMoneyForm";
	}

	private String getOrderTypeNameByOrderType(String orderType) {
		String printFormName = "";
		if(com.trekiz.admin.common.utils.StringUtils.isEmpty(orderType)) {
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

	/**
	 * add by songyang 2015年11月4日13:47:11
	 * 获取借款审批列表信息
	 * @param vo
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="getBorrowingList")
	public String getBorrowingList( @ModelAttribute OrderExitGroupReviewVO vo, HttpServletResponse response,
	        Model model, HttpServletRequest request){
		
		//初始化排序方式
		if(StringUtils.isEmpty(vo.getOrderBy())) {
			vo.setOrderBy("create_date");
			vo.setAscOrDesc("desc");
		}
		
		Page<Map<Object, Object>> pageInfo=newOrderReviewService
				.getPlaneBorrowingReviewList(new Page<Map<Object, Object>>(request,response), vo);
		String returnStr = "review/borrowing/common/newPlaneBorrowingList";
		Long companyId = UserUtils.getUser().getCompany().getId();
		model.addAttribute("companyId", companyId.toString());
		model.addAttribute("conditionsMap", vo);
		model.addAttribute("page", pageInfo);
		model.addAttribute("reviewVO", vo);
		model.addAttribute("userList", systemService.getUserByCompanyId(companyId));
		return returnStr;
	}
	/**
	 * add by sy  2015年11月4日14:17:30
	 * 审批借款申请信息
	 */
	@ResponseBody
	@RequestMapping(value ="reviewBorrowing")
	public Map<String, Object> reviewBorrowing(HttpServletResponse response,
	        Model model, HttpServletRequest request,String rid,Integer result){

		Map<String, Object> map =new HashMap<String,Object>();
		String denyReason = request.getParameter("denyReason");
		ReviewResult r = null;
		try {
//			map = orderReviewService.reviewBorrowing(rid, roleId, result, denyReason, userLevel, request);
			String reviewId = "";
			// result 等于  1 审批通过  ， 等于 0  驳回
			if(result==1){
				 r=processReviewService.approve(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(),"", userReviewPermissionChecker, rid,denyReason, null);

				 //审批通过时候的业务数据操作
				 if(r.getReviewStatus()==2){
					 map = newOrderReviewService.reviewBorrowing(r.getReviewId(), result, request);
				 }

				 if(r.getSuccess()){
					 map.put("flag", 1);
				 }
			}else if(result==0){
				 r=processReviewService.reject(UserUtils.getUser().getId().toString(),UserUtils.getUser().getCompany().getUuid().toString(), "", rid, denyReason, null);
				 if(r.getSuccess()){
					 map.put("flag", 1);
				 }
			}
		} catch (Exception e) {
			map.put("flag", 0);
			map.put("message", e.getMessage());
			return map;
		}
		return map;
	}
	/**
	 * 批量审批借款申请信息
	 * @author sy  2015年11月5日17:01:01
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="batchReviewBorrowing")
	public Map<String,Object> batchReviewBorrowing(HttpServletResponse response,
	        Model model, HttpServletRequest request){
		String result =  request.getParameter("result"); //1审批通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审批驳回原因
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");
		int num = 0;
		Map<String, Object> back =new HashMap<String,Object>();
		if (StringUtils.isBlank(result)) {
			back.put("res", "error");
			back.put("msg", "审批结果不能为空");
			return back;
		}
		for (int i = 0; i < levelandrevids.length; i++) {

			try {
				if("1".equals(result)){
					//审批通过
					ReviewResult r = processReviewService.approve(UserUtils.getUser().getId().toString(),UserUtils.getUser().getCompany().getUuid().toString(), "", userReviewPermissionChecker, levelandrevids[i], remarks, null);
					//审批通过时候的业务数据操作
					if(r.getReviewStatus()==2){
						back = newOrderReviewService.reviewBorrowing(r.getReviewId(), Integer.parseInt(result), request);
					}
				}else{
					//驳回
					processReviewService.reject(UserUtils.getUser().getId().toString(),UserUtils.getUser().getCompany().getUuid().toString(), "", levelandrevids[i], remarks, null, null);
				}
				num++;
			} catch (Exception e) {
				logger.error("批量审批借款申请: 审批ID为："+levelandrevids[i]+",  报错原因："+e.getMessage());
			}
		}
		back.put("res", "success");
		back.put("msg", "共有"+num+"条记录操作成功。");
		return back;
	}

}
