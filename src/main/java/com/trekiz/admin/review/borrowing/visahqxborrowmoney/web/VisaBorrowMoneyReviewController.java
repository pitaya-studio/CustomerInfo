package com.trekiz.admin.review.borrowing.visahqxborrowmoney.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;
import com.trekiz.admin.review.borrowing.airticket.web.ActivityAirTicketOrderLendMoneyController;
import com.trekiz.admin.review.borrowing.visahqxborrowmoney.service.IVisaBorrowMoneyReviewService;

@Controller
@RequestMapping("${adminPath}/visa/borrowMoney/review")
public class VisaBorrowMoneyReviewController {

    private static final Logger log = Logger.getLogger(ActivityAirTicketOrderLendMoneyController.class);
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private IVisaBorrowMoneyReviewService visaBorrowMoneyReviewService;
    @Autowired
    private VisaProductsService visaProductsService;
    @Autowired
    private VisaOrderDao visaOrderDao;
    @Autowired
    private ReviewService processReviewService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private VisaService visaService;
	@Autowired
	private MoneyAmountService  moneyAmountService;
	@Autowired
	private DictService dictService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;

	
    @ResponseBody
    @RequestMapping("/batchVisaBorrowingMoneyReview")
    public Map<String ,Object> batchVisaBorrowingMoneyReview(String result, String batchNos, String remark) {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> canBeBatchNos = null;
        if(StringUtils.isNotBlank(batchNos)) {
            // 查找符合条件的batchNos
            canBeBatchNos = visaBorrowMoneyReviewService.findMatchConditionBatchNos(batchNos);
        }
        //批量审核
        try {
            resultMap = visaBorrowMoneyReviewService.batchReview(canBeBatchNos, result, remark);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            resultMap.put("result", "fail");
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/backReview")
    public Map<String, Object> backReview(String batchNo) {
        /*声明返回对象*/
        Map<String, Object> result = new HashMap<String, Object>();
        // 如果批次号不为空，则进行审核撤销
        if(StringUtil.isNotBlank(batchNo)) {
            try {
                result = visaBorrowMoneyReviewService.backReivew(batchNo);
            } catch(Exception e) {
                result.put("result", "fail");
                result.put("msg", "撤销失败!");
            }
        } else {
            result.put("result", "fail");
            result.put("msg", "批次号不能为空！");
        }
        return result;
    }

    /**
     * @param result     1审批通过 0 驳回
     * @param denyReason 审批通过备注或驳回原因
     * @param batchNo    批次号
     * @return
     * @author yunpeng.zhang
     * @createDate 2015年12月8日11:20:29
     */
    @ResponseBody
    @RequestMapping("/reviewVisaBorrowMoneybyBatchNo")
    public Map<String, Object> reviewVisaBorrowMoneybyBatchNo(String result, String denyReason, String batchNo, Map<String,
            Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtil.isNotBlank(result) && StringUtil.isNotBlank(batchNo)) {
            try {
                resultMap = visaBorrowMoneyReviewService.reviewVisaBorrowMoneybyBatchNo(result, batchNo, denyReason);
            } catch (Exception e) {
                if("1".equals(result)) {
                    resultMap.put("msg", "审批失败！");
                } else if("0".equals(result)) {
                    resultMap.put("msg", "驳回失败！");
                }
                resultMap.put("result", "fail");
            }
        } else {
            resultMap.put("msg", "批次号不存在！");
            resultMap.put("result", "fail");
        }

        return resultMap;
    }

    /**
     * 通过批次号查询该批次下游客的信息(针对签证借款)
     *
     * @param batchNo
     * @return List<Map<String, String>>
     * @author yunpeng.zhang
     * @DateTime 2015年12月4日17:00:49
     */
    @ResponseBody
    @RequestMapping("/getTravelerList")
    public List<Map<String, String>> getTravelerList(String batchNo, String businessType) {
        List<Map<String, String>> travelerList = null;
        if (StringUtils.isNotBlank(batchNo)) {
            travelerList = new ArrayList<Map<String, String>>();
            if ("2".equals(businessType)) {
                visaBorrowMoneyReviewService.getTravelerList(batchNo, travelerList);
            } else if ("1".equals(businessType)) {
                visaBorrowMoneyReviewService.getTravelerList4Receipt(batchNo, travelerList);
            }
        }
        return travelerList;
    }

    /**
     * @return
     * @author yunpeng.zhang
     * @createDate 2015年12月3日19:47:55
     */
    @RequestMapping("/visaBorrowMoneyReviewList")
    public String visaBorrowMoneyReviewList(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> params = prepareParams(request, response);
        Long userId = UserUtils.getUser().getId();
        Page<Map<String, Object>> page = visaBorrowMoneyReviewService.queryBorrowMoneyReviewList(params);
        model.addAttribute("page", page);
        model.addAttribute("conditionsMap", params);
        model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
        List<Object[]> countryInfoList = visaProductsService.findCountryInfoList();
        model.addAttribute("countryInfoList", countryInfoList);
        model.addAttribute("userId", userId);
        return "/review/borrowing/visahqx/review/newVisaBorrowMoneyReviewList";
    }

    /**
     * 组织操作参数
     *
     * @param request
     * @return
     */
    private Map<String, Object> prepareParams(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        /**获取参数 start*/
        //批次号
        String groupCode = request.getParameter("groupCode") == null ? null : request.getParameter("groupCode").toString();
        //游客名字
        String travellerName = request.getParameter("travellerName") == null ? null : request.getParameter("travellerName").toString();
        //下单人
        String createBy = request.getParameter("createBy") == null ? null : request.getParameter("createBy").toString();
        //申请日期（from）
        String applyDateFrom = request.getParameter("applyDateFrom") == null ? null : request.getParameter("applyDateFrom").toString();
        //申请日期（to）
        String applyDateTo = request.getParameter("applyDateTo") == null ? null : request.getParameter("applyDateTo").toString();
        //审批发起人(id)
        String applyPerson = request.getParameter("applyPerson") == null ? null : request.getParameter("applyPerson").toString();
        // 签证国家
        String sysCountryId = request.getParameter("sysCountryId") == null ? null : request.getParameter("sysCountryId").toString();
        // 签证类型
        String visaType = request.getParameter("visaType") == null ? null : request.getParameter("visaType").toString();
        //审批状态
        String reviewStatus = request.getParameter("reviewStatus") == null ? null : request.getParameter("reviewStatus").toString();
        //出纳确认
        String cashConfirm = request.getParameter("cashConfirm") == null ? null : request.getParameter("cashConfirm").toString();
        //打印状态
        String printStatus = request.getParameter("printStatus") == null ? null : request.getParameter("printStatus").toString();
        //页签选择状态
        String tabStatus = request.getParameter("tabStatus") == null ? "1" : request.getParameter("tabStatus").toString();
        // 创建日期排序标识
        String orderCreateDateSort = request.getParameter("orderCreateDateSort");
        // 更新日期排序标识
        String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");
        //订单创建日期排序标识
        String orderCreateDateCss = request.getParameter("orderCreateDateCss");
        //订单更新日期排序标识
        String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");
        //page对象
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
        /**获取参数 end*/
        /**组装参数 start*/
        result.put("groupCode", groupCode);
        result.put("travellerName", travellerName);
        result.put("createBy", createBy);
        result.put("applyDateFrom", applyDateFrom);
        result.put("applyDateTo", applyDateTo);
        result.put("applyPerson", applyPerson);
        result.put("sysCountryId", sysCountryId);
        result.put("visaType", visaType);
        result.put("reviewStatus", reviewStatus);
        result.put("cashConfirm", cashConfirm);
        result.put("printStatus", printStatus);
        result.put("tabStatus", tabStatus);
        result.put("orderCreateDateSort", orderCreateDateSort);
        result.put("orderUpdateDateSort", orderUpdateDateSort);
        result.put("orderCreateDateCss", orderCreateDateCss);
        result.put("orderUpdateDateCss", orderUpdateDateCss);
        result.put("pageP", page);
        /**组装参数 end*/
        return result;
    }


    /**
     * @param model
     * @param request
     * @param response
     * @return
     * @throws
     * @Description: 签证借款批量审批详情页
     * @author xinwei.wang
     * @date 2015年12月5日下午1:48:56
     */
    @RequestMapping(value = "visaBorrowMoneyBatchReviewDetail4Hqx")
    public String visaBorrowMoneyBatchReviewDetail4Hqx(Model model, HttpServletRequest request, HttpServletResponse response) {
        String orderId = request.getParameter("orderId");
        String travelerId = request.getParameter("travelerId");
        String reviewId = request.getParameter("revid");
        String flowType = request.getParameter("flowType");
        String flag = request.getParameter("flag");
        String batchno = request.getParameter("batchno");
        String fromflag = request.getParameter("fromflag");

		/*SELECT revn.batch_no AS batchno, revn.create_by AS createBy, revn.id AS reviewid, revn.order_id AS orderid,
          revn.traveller_id AS travelerid, revn.traveller_name AS travelername
		  FROM review_new revn,  visa_flow_batch_opration vfbo
		  WHERE  revn.batch_no = vfbo.batch_no AND vfbo.busyness_type = 2 
          AND  revn.batch_no='20151203-0001';*/


        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT revn.batch_no AS batchno, revn.create_by AS createBy, revn.id AS reviewid, revn.order_id AS orderid, ");
        buffer.append("revn.traveller_id AS travelerid, revn.traveller_name AS travelername ");
        buffer.append("FROM review_new revn,  visa_flow_batch_opration vfbo ");
        buffer.append("WHERE  revn.batch_no = vfbo.batch_no AND vfbo.busyness_type = 2 AND revn.process_type = '5' ");
        buffer.append("AND  revn.batch_no = '" + batchno + "'");

        List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
        //Map<String, Object>  mp =  list.get(0);
        //totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ct[i]);

        StringBuilder revids = new StringBuilder("");
        StringBuilder remarks = new StringBuilder("");
        for (Map<String, Object> map : list) {
            //签证借款申请相关信息
            //Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong((Integer)map.get("reviewid")
            // +""));
            //获取activiti审核信息
            Map<String, Object> reviewAndDetailInfoMap = null;
            try {
                if (reviewId != null) {
                    reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(map.get("reviewid").toString());
                }
            } catch (Exception e) {
                log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ", e);
            }

            map.put("borrowAmount", fmtMicrometer(reviewAndDetailInfoMap.get("borrowAmount").toString()));
            map.put("borrowRemark", reviewAndDetailInfoMap.get("borrowRemark"));
            revids.append(reviewAndDetailInfoMap.get("id")).append(",");
            remarks.append(reviewAndDetailInfoMap.get("borrowRemark")).append(",");
        }
        //Map<String, String> reviewAndDetailInfoMapforone = reviewService.findReview(Long.parseLong(revid));
        Map<String, Object> reviewAndDetailInfoMapforone = null;
        try {
            if (reviewId != null) {
                reviewAndDetailInfoMapforone = processReviewService.getReviewDetailMapByReviewId(reviewId);
            }
        } catch (Exception e) {
            log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ", e);
        }
        
       //270需求--审批签证借款审批和查看页面新增还款日期-s//
        VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno, "2");
          //System.out.println("---"+visaFlowBatchOpration.getRefundDate());
         if(null!=visaFlowBatchOpration){ //非空处理
        	 if(null!=visaFlowBatchOpration.getRefundDate()){ //还款日期不为空
        		 model.addAttribute("refund_date",DateUtils
                .date2String((Date) visaFlowBatchOpration.getRefundDate(), "yyyy-MM-dd"));
        	 }
         }else{
        	 model.addAttribute("refund_date","");
         }
          //放置还款日期是否为必填项的配置:1:必填 0:非必填
         model.addAttribute("refundDateOption",UserUtils.getUser().getCompany().getIsMustRefundDate());
       //270需求--审批签证借款审批和查看页面新增还款日期-e//
        model.addAttribute("revCreateDate", DateUtils
                .date2String((Date) reviewAndDetailInfoMapforone.get("createDate"), "yyyy-MM-dd hh:mm:ss"));//报批日期

        model.addAttribute("orderId", orderId);
        model.addAttribute("travelerId", travelerId);
        model.addAttribute("revid", reviewId);
        model.addAttribute("flowType", flowType);
        model.addAttribute("flag", flag);
        model.addAttribute("rid", reviewId);
        //批量审批用到
        model.addAttribute("borrowinfolist", list);
        model.addAttribute("batchno", batchno);
        model.addAttribute("revids", revids.toString());
        model.addAttribute("remarks", remarks.toString());

        model.addAttribute("fromflag", fromflag);

        //处理审核动态信息
        if (reviewId != null && !"".equals(reviewId)) {//显示动态审核的标志
            List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
            model.addAttribute("rLog", rLog);
        }

        //model.addAttribute("airticketReturnDetailInfoMap",airticketReturnDetailInfoMap);
        return "review/borrowing/visahqx/visaBorrowMoney4HQXBatchReviewDetail";
    }
    
    
	/**
	 * @Description: 数字千位符格式化
	 * @author xinwei.wang
	 * @date 2015年12月8日下午9:08:57
	 * @param text
	 * @return    
	 * @throws
	 */
	public String fmtMicrometer(String text){
/*		if (text.indexOf(".") > 0) {
			if (text.length() - text.indexOf(".") - 1 == 0) {
				df = new DecimalFormat("###,##0.");
			} else if (text.length() - text.indexOf(".") - 1 == 1){
				df = new DecimalFormat("###,##0.0");
			} else{
				df = new DecimalFormat("###,##0.00");
			}

		} else{
			df = new DecimalFormat("###,##0");
		}*/
		DecimalFormat df  = new DecimalFormat("###,##0.00");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}


    /**
     * 签证借款批次审批导出游客信息
     *
     * @return void
     * @author Bin
     * @DateTime 2015年12月5日
     */
    @RequestMapping("exportTravelerInfo")
    public void exportTravelerInfo(String batchNo, HttpServletRequest request, HttpServletResponse response) {
        visaBorrowMoneyReviewService.exportTravelerInfo(batchNo, request, response);
    }
    
    
   /**
    * 还签证审批详情页 
    * @param model
    * @param request
    * @param response
    * @return
    */
	@RequestMapping("/visaReturnReceiptReviewDetail4Activiti")
	public String visaReturnReceiptReviewDetail4Activiti(Model model, HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		
		//订单相关信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));

		String totalMoney = moneyAmountService.getMoney(visaOrder.getTotalMoney());
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("visaOrder", visaOrder);
		
		//产品相关信息
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Dict collarZoning = dictService.findByValueAndType(visaProduct.getCollarZoning().toString(), "from_area");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("visaType", visaType);
		model.addAttribute("collarZoning", collarZoning);
		model.addAttribute("country", country);
		
		//游客相关信息
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
		model.addAttribute("traveler", traveler);
		
		//签证相关信息
		Visa visa = visaService.findVisaByTravlerId(Long.parseLong(travelerId));
		Dict visaStauts = dictService.findByValueAndType(visa.getVisaStauts()+"", "visa_status");
		model.addAttribute("visa", visa);
		model.addAttribute("visaStauts", visaStauts);
		
		//还签证收据申请相关信息
		//Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		
		//获取activiti审核信息
		Map<String,Object>  reviewAndDetailInfoMap = null;
		try{
			if(revid!=null){
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + revid + " 查询出来reviewDetail明细报错 ",e);
		}
		
		if (reviewAndDetailInfoMap!=null) {
			model.addAttribute("revCreateDate", reviewAndDetailInfoMap.get("createDate"));//报批日期
//			model.addAttribute("revCreateReason", reviewAndDetailInfoMap.get("createReason"));//申报原因
			model.addAttribute("revBorrowRemark", reviewAndDetailInfoMap.get("borrowRemark"));//申报原因
//			model.addAttribute("revReceiptAmount", reviewAndDetailInfoMap.get("receiptAmount"));//收据金额
			model.addAttribute("revBorrowAmount", reviewAndDetailInfoMap.get("borrowAmount"));//收据金额
			model.addAttribute("revReceiptor", reviewAndDetailInfoMap.get("receiptor"));//还收据人
			String currencyId = reviewAndDetailInfoMap.get("currencyId").toString();
			if (null!=currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);//还收据人
			}
		}
		model.addAttribute("nowLevel",nowLevel);
		model.addAttribute("orderId",orderId);
		model.addAttribute("travelerId",travelerId);
		model.addAttribute("revid",revid);
		model.addAttribute("flowType",flowType);
		model.addAttribute("flag", flag);
		model.addAttribute("rid",revid);
		
		return "review/borrowing/visahqx/visaBorrowMoney4HQXBatchReviewDetail4Activiti";
	}

}
