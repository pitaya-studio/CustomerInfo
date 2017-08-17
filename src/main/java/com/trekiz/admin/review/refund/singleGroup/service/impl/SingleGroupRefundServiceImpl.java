package com.trekiz.admin.review.refund.singleGroup.service.impl;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.refund.singleGroup.service.ISingleGroupRefundService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SingleGroupRefundServiceImpl implements ISingleGroupRefundService {
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private NewProcessMoneyAmountService newProcessMoneyAmountService;
	@Autowired
	private UserReviewPermissionChecker permissionChecker;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private ICommonReviewService commonReviewService;

	protected org.slf4j.Logger logger = LoggerFactory
			.getLogger(SingleGroupRefundServiceImpl.class);

	@Override
	@Transactional(readOnly=false)
	public void handleMoneyAmount(String reviewId) {
		// 改变金额状态
		NewProcessMoneyAmount newMoneyAmount = newProcessMoneyAmountService.findByReviewId(reviewId);
		if(newMoneyAmount != null) {
			newMoneyAmount.setDelFlag("1");
		}
		// 对成本录入进行更改
		ReviewNew review = reviewService.getReview(reviewId);
		commonReviewService.updateCostRecordStatus(review, 3);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> startGroupRefundAndHandleTravler(ProductOrderCommon productOrder,
			ActivityGroup productGroup, TravelActivity product, List<RefundBean> refundRecords, Long deptId,
			Integer productType) throws Exception {
		// 要返回的结果
		Map<String, Object> result = new HashMap<>();
		// 获取订单信息数据(下单人,销售,下单时间,订单编号,订单团号,订单总额,计调)
		Map<String, Object> orderInfo = OrderCommonUtil.getOrderInfo(productOrder, productGroup);
		// 获取产品信息(已：产品id,产品名称,出团日期,行程天数,目的地
		// 未：成人单价,儿童单价,特殊人群单价,成人出行人数,儿童出行人数,特殊人群出行人数,出发城市)
		Map<String, Object> productInfo = OrderCommonUtil.getProductInfo(product);
		boolean yubao_locked = false; //预报单是否锁定标识
		boolean jiesuan_locked = false; //结算单是否锁定标识
		//对预报单状态进行判断
		if ("10".equals(productGroup.getForcastStatus())) {
			yubao_locked = true;
		}
		//对结算单状态进行判断
		if (1 == productGroup.getLockStatus()) {
			yubao_locked = true;
			result.put("reply", "结算单已锁定，不能发起申请");
			result.put("msg", "faild");
			return result;
		}

		// 遍历refundRecords，将数据进行存储，同时发起审批
		try {
			for (RefundBean refundBean : refundRecords) {
                String travelerId = refundBean.getTravelerId(); // 退款游客 id
                String travelerName = refundBean.getTravelerName(); // 退款游客姓名
                String refundCurrencyId = refundBean.getCurrencyId(); // 退款币种id
                String refundName = refundBean.getRefundName(); // 退款款项
                String refundPrice = refundBean.getRefundPrice(); // 退款金额
                String remark = refundBean.getRemark(); // 退款备注
                String refundCurrencyMark = refundBean.getCurrencyMark(); // 退款币种标识
                String refundCurrencyName = refundBean.getCurrencyName();
                String createBy = refundBean.getCreateBy();
                Date applyDate = refundBean.getApplyDate() == null ? new Date() : refundBean.getApplyDate();

                String payPrice = "";

                if(StringUtils.isNotBlank(travelerId) && !"0".equals(travelerId)) {		// 游客退款
                    Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
                    payPrice = moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum()); 	// 游客应收金额
                } else if(StringUtils.isBlank(travelerId) || "0".equals(travelerId)) {	// 团队退款
                    payPrice = moneyAmountService.getMoneyStr(productOrder.getTotalMoney()); 		// 订单应收金额
                }

                // 组织 review 数据
                Map<String, Object> variables = new HashMap<String, Object>();
                variables.putAll(orderInfo);
                variables.putAll(productInfo);
                // 流程类型
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_REFUND);
                // 产品类型
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, productType);
                // 备注
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, remark);
                // 部门id
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, deptId);
                // 游客id
                variables.put("travellerId", travelerId);
                // 游客名称
                variables.put("travellerName", travelerName);
                // 退款款项名
                variables.put("refundName", refundName);
                // 申请时间
                variables.put("applyDate", applyDate);
                // 游客或团队应收金额（字符串形式）
                variables.put("payPrice", payPrice);
                // 退款金额（币种）
                variables.put("currencyId", refundCurrencyId);
                // 退款金额（数量）
                variables.put("refundPrice", refundPrice);
                // 退款金额（标识）
                variables.put("currencyMark", refundCurrencyMark);
                // 退款金额币种名称
                variables.put("currencyName", refundCurrencyName);
                // 申请人
                variables.put("createBy", createBy);

                String userId = UserUtils.getUser().getId().toString();
                String companyUuid = UserUtils.getUser().getCompany().getUuid();
                String companyId = UserUtils.getUser().getCompany().getId().toString();
                // 调用 start()
                ReviewResult reviewResult = reviewService.start(userId, companyUuid, permissionChecker, null, productType,
                        Context.REVIEW_FLOWTYPE_REFUND, deptId, "", variables);
                if (!reviewResult.getSuccess()) {
					throw new Exception(reviewResult.getMessage());
                } else {
                    // start 成功后 保存退款金额
                    NewProcessMoneyAmount refundMoneyAmount = new NewProcessMoneyAmount();

                    // 获取退款金额对应的汇率
                    BigDecimal exchangerate = null;
                    List<Currency> currencyList = currencyService.findCurrencyList(companyId);
                    for (Currency currency : currencyList) {
                        Long currencyId = currency.getId();
                        if (refundCurrencyId.equals(currencyId)) {
                            exchangerate = currency.getConvertLowest();
                            break;
                        }
                    }

                    refundMoneyAmount.setAmount(new BigDecimal(refundPrice));
                    refundMoneyAmount.setCompanyId(companyUuid);
                    refundMoneyAmount.setCreatedBy(Long.parseLong(userId));
                    refundMoneyAmount.setCreateTime(new Date());
                    refundMoneyAmount.setCurrencyId(Integer.parseInt(refundCurrencyId));
                    refundMoneyAmount.setDelFlag("0");
                    refundMoneyAmount.setExchangerate(exchangerate);
                    refundMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_REFUND);
                    refundMoneyAmount.setOrderType(productType);
                    refundMoneyAmount.setReviewId(reviewResult.getReviewId());
                    refundMoneyAmount.setId(UuidUtils.generUuid());
                    // 保存退款金额
                    newProcessMoneyAmountService.saveNewProcessMoneyAmount(refundMoneyAmount);

                    if(reviewResult.getReviewStatus() == 2) {
                        MoneyAmount moneyAmount = new MoneyAmount();
                        moneyAmount.setSerialNum(UUID.randomUUID().toString());
                        moneyAmount.setCurrencyId(Integer.parseInt(refundCurrencyId));
                        moneyAmount.setAmount(new BigDecimal(refundPrice));
                        moneyAmount.setUid(productOrder.getId());
                        moneyAmount.setMoneyType(12);
                        moneyAmount.setOrderType(product.getActivityTypeId());
                        moneyAmount.setBusindessType(1);
                        moneyAmount.setExchangerate(exchangerate);
                        moneyAmount.setReviewUuid(reviewResult.getReviewId());
                        moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
                    }
                    ReviewNew reviewInfo = reviewService.getReview(reviewResult.getReviewId());
    				// 返佣发起申请之后对成本进行同步插入
    				costManageService.saveRefundCostRecordNew(reviewInfo, refundBean, productOrder, yubao_locked, jiesuan_locked);
                }

                //无需审批
                if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
                    ReviewNew review = reviewService.getReview(reviewResult.getReviewId());
                    commonReviewService.updateCostRecordStatus(review, reviewResult.getReviewStatus());
                }
            }
		} catch (Exception e) {
			logger.error(e.getMessage());
            throw new Exception(e.getMessage());
		}
		result.put("msg", "success");
		return result;
	}

}
