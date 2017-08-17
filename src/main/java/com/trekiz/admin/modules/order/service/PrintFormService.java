package com.trekiz.admin.modules.order.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.order.formBean.PrintFormBean;
import com.trekiz.admin.modules.order.formBean.PrintParamBean;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.util.OrderUtil;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.borrowing.airticket.service.NewOrderReviewService;


/**
 * 借款单Service，主要功能为封装借款单数据
 * @author majiancheng
 * @Time 2015-5-7
 */
@Service
@Transactional(readOnly = true)
public class PrintFormService extends BaseService {
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewLogDao reviewLogDao;
	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	private NewOrderReviewService newOrderReviewService;
	@Autowired
	private RefundService refundService;
	
    /**
     * 封装借款单数据
    	* 
    	* @return PrintFormBean
    	* @author majiancheng
    	* @Time 2015-5-7
     */
	public PrintFormBean buildPrintFormBean(Long reviewId) {
		PrintFormBean printFormBean = new PrintFormBean();
		
		// 借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(reviewId);
		if (reviewAndDetailInfoMap != null) {
			printFormBean.setRevCreateDate(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));// 填写日期
			printFormBean.setRevBorrowRemark(reviewAndDetailInfoMap.get("createReason"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			
			if (null != user) {
				printFormBean.setOperatorName(user.getName());// 经办人、领款人都为借款申请人
			} else {
				printFormBean.setOperatorName("未知");
			}
			printFormBean.setPayDate(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate")));// 付款日期
			
			
			List<ReviewDetail> rdlist = reviewService.queryReviewDetailList(String.valueOf(reviewId));
			if(rdlist != null && !rdlist.isEmpty()) {
				for(ReviewDetail reviewDetail : rdlist) {
					if("currencyConverter".equals(reviewDetail.getMykey())) {
						// 设置借款金额
						printFormBean.setRevBorrowAmount(MoneyNumberFormat.fmtMicrometer(reviewDetail.getMyvalue(), MoneyNumberFormat.FMT_MICROMETER));
						// 设置借款金额大写
						printFormBean.setRevBorrowAmountDx(MoneyNumberFormat.digitUppercase(Double.parseDouble(reviewDetail.getMyvalue())));
					}
				}
			}
			
		}
		
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(reviewId);		
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_BORROWMONEY,reviewLogs);
		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			printFormBean.setCw(jobtypeusernameMap.get(8));
		}
		
		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (null!=jobtypeusernameMap.get(6)) {//出纳
			if (68!=UserUtils.getUser().getCompany().getId()) {
				printFormBean.setCashier(jobtypeusernameMap.get(6));
			}
		}
		
		//model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
		if (null!=jobtypeusernameMap.get(10)) {//总经理
			printFormBean.setMajorCheckPerson(jobtypeusernameMap.get(10));
		}
		
		if (null!=jobtypeusernameMap.get(7)) {//部门经理
			printFormBean.setDeptmanager(jobtypeusernameMap.get(7));
		}
		
		Review review =  reviewService.findReviewInfo(reviewId);
		if (null!=review && null == review.getPrintFlag()) {
//			Date printDate = new Date();
//			reviewService.updateReviewPrintInfoById(printDate, reviewId);
//			
//			printFormBean.setPrintDate(printDate);
		}else {
			printFormBean.setPrintDate(review.getPrintTime());
		}
		
		if (null!=jobtypeusernameMap.get(9)) {//财务主管
			if (68!=UserUtils.getUser().getCompany().getId()) {
				printFormBean.setCwmanager(jobtypeusernameMap.get(9));
			}
		}
		
		printFormBean.setOrderId(Long.parseLong(reviewAndDetailInfoMap.get("orderId")));
		printFormBean.setReviewId(reviewId);
		
		return printFormBean;
	}
	
	public File createBorrowMoneySheetDownloadFile(PrintFormBean printFormBean) throws Exception {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("revCreateDate", DateUtils.date2String(printFormBean.getRevCreateDate(), "yyyy 年 MM 月 dd 日"));
		root.put("revBorrowRemark", printFormBean.getRevBorrowRemark());
		root.put("productCreater", printFormBean.getProductCreater());
		root.put("operatorName", printFormBean.getOperatorName());
		root.put("payDate", DateUtils.date2String(printFormBean.getPayDate(), "yyyy 年 MM 月 dd 日"));
		root.put("revBorrowAmount", printFormBean.getRevBorrowAmount());
		root.put("majorCheckPerson", printFormBean.getMajorCheckPerson());
		root.put("revBorrowAmountDx", printFormBean.getRevBorrowAmountDx());
		root.put("cwmanager", printFormBean.getCwmanager());
		root.put("cw", printFormBean.getCw());
		root.put("deptmanager", printFormBean.getDeptmanager());
		root.put("cashier", printFormBean.getCashier());
		root.put("printFormName", printFormBean.getPrintFormName());
		root.put("borrowDept", printFormBean.getBorrowDept());
		root.put("payDateStr", printFormBean.getPayDateStr());
		
		return FreeMarkerUtil.generateFile("borrowmoney.ftl", "borrowmoney.doc", root);
	}

	/**
	 * 由于需要批量打印借款单，所以把原先在Controller里面的单一打印方法提取出来到Service层。返回Map类型的打印信息数据，供
	 * 总控方法调用，该方法保持和Controller里面的业务一致。
	 * @param paramBean
	 * @return
     */
	public Map<String,Object> getBorrowMoneyFormMap(PrintParamBean paramBean) {

		Map<String,Object> result = new HashMap<>();

		String reviewId = paramBean.getReviewId();
		String orderType = paramBean.getOrderType().toString();
		String payId = paramBean.getPayId();
		String option = paramBean.getOption();

		//根据审核表ID获取借款单数据
		PrintFormBean printFormBean = buildPrintFormBean(Long.parseLong(reviewId));
		//根据订单类型，获取打印单名称
		printFormBean.setPrintFormName(OrderUtil.getOrderTypeName(orderType)+"借款单");
		printFormBean.setBorrowDept(OrderUtil.getOrderTypeName(orderType)+"部");
		String proCreator = newOrderReviewService.getProductCreaterByReviewId(printFormBean.getOrderId(), orderType);
		printFormBean.setProductCreater(proCreator);

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

		result.put("printFormBean", printFormBean);
		result.put("orderType", orderType);
		result.put("payId", payId);
		result.put("option", option);
		result.put("reviewFlag", 1); // 旧审核
		result.put("reviewId", reviewId); // reviewId

		//----- majiancheng added 20151019 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
		//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		Review review = reviewService.findReviewInfo(Long.parseLong(reviewId));

		if(review != null) {
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				printFormBean.setPayStatus("0");
			}else {
				printFormBean.setPayStatus(String.valueOf(review.getPayStatus()));
			}
		} else {
			printFormBean.setPayStatus("0");
		}
		return result;
	}

}
