package com.trekiz.admin.review.guarantee.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;
import com.trekiz.admin.review.guarantee.service.GuaranteeService;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zong on 2016/6/12.
 * 担保变更Controller
 */
@Controller
@RequestMapping(value="${adminPath}/guaranteeMod")
public class GuaranteeModController {

    @Autowired
    private VisaOrderService visaOrderService;
    @Autowired
    private VisaProductsService visaProductsService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserReviewPermissionChecker permissionChecker;
    @Autowired
    private MoneyAmountService moneyAmountService;
    @Autowired
    private AgentinfoService agentinfoService;
    @Autowired
    private TravelerService travelerService;
    @Autowired
    private ReviewMutexService reviewMutexService;
    @Autowired
    private GuaranteeService guaranteeService;
    @Autowired
    private VisaService visaService;

    /**
     * 担保变更列表
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping(value = "list/{orderId}")
    public String list(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        List<Map<String, Object>> travelerList = visaOrderService.getTravelerByOrderId(orderId);
        model.addAttribute("travelerList", travelerList);
        return "/review/guarantee/guaranteeModList";
    }

    /**
     * 担保变更申请
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping(value = "guaranteeApply")
    public String guaranteeApply(@RequestParam Long orderId, Model model, HttpServletRequest request) {
        //区分担保变更申请或详情或审批页面-"apply"：申请，"detail"：详情，"approval"：审批
        String flag = request.getParameter("flag");
        model.addAttribute("flag", flag);
        String travelerId = request.getParameter("travelerId");
        model.addAttribute("travelerId", travelerId);
        String review = request.getParameter("review");
        model.addAttribute("review", review);
        String nav = request.getParameter("nav");
        model.addAttribute("nav", nav);
        String title = request.getParameter("title");
        model.addAttribute("title", title);

        Long companyId = UserUtils.getUser().getCompany().getId();
        //订单信息
        VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
        model.addAttribute("visaOrder", visaOrder);
        //产品信息
        VisaProducts visaProduct = visaProductsService.getVisaProductById(visaOrder.getVisaProductId());
        model.addAttribute("visaProduct", visaProduct);
        //签证国家
        Country country = CountryUtils.getCountry(Long.valueOf(visaProduct.getSysCountryId()));
        model.addAttribute("country", country);
        //游客列表
        List<Map<String, Object>> travelerList = visaOrderService.getTravelerByOrderId(orderId, travelerId, flag, review);
        model.addAttribute("travelerList", travelerList);
        if (travelerList != null && travelerList.size() > 0) {
            Object createDate = travelerList.get(0).get("createDate");
            model.addAttribute("createDate", createDate);
        }
        //币种
        List<Currency> currencyList = currencyService.findCurrencyList(companyId);
        model.addAttribute("currencyList", currencyList);
        return "/review/guarantee/guaranteeApply";
    }

    /**
     * 发起申请
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "apply")
    public Map<String, Object> apply(HttpServletRequest request) throws Exception {
        String objs = request.getParameter("objs");
        String deptId = request.getParameter("deptId");
        String orderId = request.getParameter("orderId");

        VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.valueOf(orderId));
        VisaProducts visaProduct = visaProductsService.getVisaProductById(visaOrder.getVisaProductId());

        User user = UserUtils.getUser();
        String userId = user.getId().toString();
        String companyUuid = user.getCompany().getUuid();

        ReviewResult reviewResult = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 是否已经经过流程互斥校验
        List<String> checkList = Lists.newArrayList();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, Context.PRODUCT_TYPE_QIAN_ZHENG);
        variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, visaProduct.getId());
        variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, visaProduct.getProductName());
        variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, visaOrder.getId());
        variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, visaOrder.getOrderNo());
        JSONArray jsonArray = JSONArray.fromObject(objs);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String travelerId = jsonObject.get("id").toString();
            String guaranteeType = jsonObject.get("guaranteeType").toString();
            String currencyId = jsonObject.get("currencyId").toString();
            String amount = jsonObject.get("amount").toString();
            String remark = jsonObject.get("remark").toString();

            variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId);
            Traveler traveler = travelerService.findTravelerById(Long.valueOf(travelerId));
            variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, traveler.getName());
            variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, remark);
            variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1, guaranteeType);
            try {
                if (!checkList.contains(travelerId)) {
                    // 流程互斥校验
//                if (travelerIds != null && travelerIds.length > 0) {
                    CommonResult result = reviewMutexService.check(orderId, travelerId, Context.PRODUCT_TYPE_QIAN_ZHENG,
                            Context.REVIEW_FLOWTYPE_GUARANTEE.toString(), false);
                    if (!result.getSuccess()) {
                        throw new Exception(result.getMessage() + "</br>" + "请重新选择游客");
                    }
//                }
                    checkList.add(travelerId);
                }
                reviewResult = reviewService.start(userId, companyUuid, permissionChecker, null, 6, Context.REVIEW_FLOWTYPE_GUARANTEE, Long.valueOf(deptId), null, variables);
                if(reviewResult.getSuccess()) {
                    traveler.setReviewUuid(reviewResult.getReviewId());
                    travelerService.saveTraveler(traveler);

                    //                if ("2".equals(guaranteeType) || "3".equals(guaranteeType)) {
                    MoneyAmount moneyAmount = new MoneyAmount();
                    moneyAmount.setSerialNum(UuidUtils.generUuid());
                    moneyAmount.setCurrencyId(Integer.valueOf(currencyId));
                    if(!"".equals(amount)) {
                        moneyAmount.setAmount(new BigDecimal(amount));
                    }
                    moneyAmount.setReviewUuid(reviewResult.getReviewId());
                    moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);

                    if (reviewResult.getReviewStatus() == 2) {
                        ReviewNew reviewNew = reviewService.getReview(reviewResult.getReviewId());
                        Visa visa = visaService.findVisaByTravlerId(Long.valueOf(reviewNew.getTravellerId()));
                        List<MoneyAmount> moneyAmountList = moneyAmountService.findAmountsByReviewUuId(reviewNew.getId());
                        visa.setTotalDeposit(moneyAmountList.get(0).getSerialNum());
                        visa.setGuaranteeStatus(Integer.valueOf(reviewNew.getExtend1()));
                        visaService.updateVisa(visa);
                    }

                    //                }
                    resultMap.put("result", "success");
                    resultMap.put("msg", reviewResult.getMessage());
                } else {
                    resultMap.put("result", "error");
                    resultMap.put("msg", reviewResult.getMessage());
                }

            } catch(Exception e) {
                resultMap.put("result", "error");
                resultMap.put("msg", e.getMessage());
            }

        }

        return resultMap;
    }

    @RequestMapping(value = "guaranteeReviewList")
    public String guaranteeReviewList(Model model, HttpServletRequest request, HttpServletResponse response) {

        Long companyId = UserUtils.getUser().getCompany().getId();

        Map<String, String> paramsMap = new HashMap<String, String>();
        setParams(model, request, paramsMap);

        //渠道商
        model.addAttribute("agentList", agentinfoService.findAllAgentinfo(companyId));

        Page<Map<String, Object>> page = visaOrderService.getGuaranteeReviewList(new Page<Map<String, Object>>(request, response), paramsMap);
        model.addAttribute("page", page);

        return "/review/guarantee/guaranteeReviewList";
    }

    private void setParams(Model model, HttpServletRequest request, Map<String, String> paramsMap) {
        String wholeSalerKey = request.getParameter("wholeSalerKey");
        paramsMap.put("wholeSalerKey", wholeSalerKey);
        model.addAttribute("wholeSalerKey", wholeSalerKey);
        String createBy = request.getParameter("createBy");
        paramsMap.put("createBy", createBy);
        model.addAttribute("createBy", createBy);
        String agentInfo = request.getParameter("agentInfo");
        paramsMap.put("agentInfo", agentInfo);
        model.addAttribute("agentInfo", agentInfo);
        String travelerName = request.getParameter("travelerName");
        paramsMap.put("travelerName", travelerName);
        model.addAttribute("travelerName", travelerName);
        String reviewStatus = request.getParameter("reviewStatus");
        paramsMap.put("reviewStatus", reviewStatus);
        model.addAttribute("reviewStatus", reviewStatus);
        String createDateStart = request.getParameter("createDateStart");
        paramsMap.put("createDateStart", createDateStart);
        model.addAttribute("createDateStart", createDateStart);
        String createDateEnd = request.getParameter("createDateEnd");
        paramsMap.put("createDateEnd", createDateEnd);
        model.addAttribute("createDateEnd", createDateEnd);
        String reviewer = request.getParameter("reviewer");
        if(reviewer == null) {
            reviewer = "1";
        }
        paramsMap.put("reviewer", reviewer);
        model.addAttribute("reviewer", reviewer);
    }

    /**
     * 取消申请
     * @param reviewUuid
     * @return
     */
    @ResponseBody
    @RequestMapping("/cancelApply")
    public Map<String, Object> cancelApply(String reviewUuid) {
        User user = UserUtils.getUser();
        String companyUuid = user.getCompany().getUuid();
        String userId = user.getId().toString();
        Map<String, Object> result = Maps.newHashMap();

        //取消申请
        ReviewResult reviewResult = reviewService.cancel(userId, companyUuid, "", reviewUuid, "", null);

        if (reviewResult.getSuccess()) {
            result.put("result", "success");
            result.put("msg", reviewResult.getMessage());
        } else {
            result.put("result", "failure");
            result.put("msg", reviewResult.getMessage());
        }

        return result;
    }

    /**
     * 批量审批通过或驳回
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value="batchApproveOrReject")
    public String batchApproveOrReject(HttpServletRequest request, HttpServletResponse response) {
        String reason = request.getParameter("reason"); //理由
        String typeStr = request.getParameter("type"); //通过或驳回标志
        Integer type = null;
        if(StringUtils.isNotBlank(typeStr)) {
            type = Integer.parseInt(typeStr);
        }

        String msg = "";

        String reviewUuidStr = request.getParameter("reviewUuids");
        String[] reviewUuids = reviewUuidStr.split(",");
        for (int i = 0; i < reviewUuids.length; i++) {
            String reviewUuid = reviewUuids[i];
            if(!"0".equals(reviewUuid)) {
                msg = approveOrReject(reviewUuid, reason, type, request, response);
            }
        }

        return msg;
    }

    /**
     * 审批通过或驳回
     * @param reviewUuid 审批Uuid
     * @param reason 理由
     * @param type 通过驳回标志
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="approveOrReject")
    public String approveOrReject(@RequestParam String reviewUuid, @RequestParam String reason, @RequestParam Integer type, HttpServletRequest request, HttpServletResponse response) {
        User user = UserUtils.getUser();
        Long userId = user.getId();
        String companyUuid = user.getCompany().getUuid();

        ReviewResult result = null;
        String msg = "";

        if(type == 2) { //通过
            result = reviewService.approve(userId.toString(), companyUuid, null, permissionChecker, reviewUuid, reason, null);
            if (result.getReviewStatus() == 2) {
                ReviewNew reviewNew = reviewService.getReview(reviewUuid);
                Visa visa = visaService.findVisaByTravlerId(Long.valueOf(reviewNew.getTravellerId()));
                List<MoneyAmount> moneyAmountList = moneyAmountService.findAmountsByReviewUuId(reviewNew.getId());
                visa.setTotalDeposit(moneyAmountList.get(0).getSerialNum());
                visa.setGuaranteeStatus(Integer.valueOf(reviewNew.getExtend1()));
                visaService.updateVisa(visa);
            }
        }else if(type == 1) { //驳回
            result = reviewService.reject(userId.toString(), companyUuid, null, reviewUuid, reason, null);
        }

        if(result.getSuccess()) {
            msg = "审批成功！";
        }else{
            msg = "审批失败！";
        }
        return msg;
    }

    /**
     * 流程撤销
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value="revoke")
    public String revoke(HttpServletRequest request, HttpServletResponse response) {
        String reviewUuid = request.getParameter("reviewUuid");
        ReviewResult result = null;
        String msg = "";

        User user = UserUtils.getUser();
        Long userId = user.getId();
        String companyUuid = user.getCompany().getUuid();
        result = reviewService.back(userId.toString(), companyUuid, null, reviewUuid, null, null);

        if(result.getSuccess()) {
            msg = "撤销成功！";
        }else{
            msg = "撤销失败！";
        }
        return msg;
    }

    /**
     * 检查游客是否在审批中
     * @param travelerId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "isReviewing")
    public String isReviewing(Long travelerId, String orderId) {
        return guaranteeService.isReviewing(travelerId, orderId);
    }
}
