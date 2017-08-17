package com.trekiz.admin.review.privilege.singlegroup.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Context.ProductType;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import com.trekiz.admin.review.privilege.singlegroup.service.ISingleGroupPrivilegeService;

@Service
@Transactional(readOnly = true)
public class SingleGroupPrivilegeServiceImpl implements ISingleGroupPrivilegeService{
	
	@Autowired
	private OrderCommonService orderService;
	
	@Autowired
	private ReviewMutexService reviewMutexService;
	
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	
	
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
	
	/**
	 * @author songyang
	 * add by  2016-01-21 14:40:32
	 */
	@SuppressWarnings("unused")
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public Map<String, String> ApplyPrivilege(HttpServletRequest request,HttpServletResponse response){
		//流程变量表储存数据
		Map<String, Object> variables = new HashMap<String, Object>();
		//返回结果
		Map<String,String> result = new HashMap<String, String>();
		//所有多选框元素
		String[] allChecked = request.getParameter("ids").split(",");
		//所有的游客ID
		String travelerId = "";
		String[] travelerIds = request.getParameterValues("travelerId");
		//所有的游客Name
		String travelerName = "";
		String[] travelerNames = request.getParameterValues("travelerName");
		//所有游客类型
		String travelerType = "";
		String[] travelerTypes =request.getParameterValues("travelerType");
		//所有币种
		String currencyId = "";
		String[] currencyIds =request.getParameterValues("currencyIds");
		//所有同行价
		String thPrice ="";
		String[] thPrices = request.getParameterValues("thPrice");
		//所有优惠额度
		String inpuryhprice =""; //yhPrice
		String[] inpuryhprices =request.getParameterValues("inpuryhprice");
		//所有的同行结算价
		String thjsjinputprice = "";
		String[] thjsjinputprices = request.getParameterValues("thjsjinputprice");
		//所有的申请优惠金额
		String sqyhPrice = "";
		String[] sqyhPrices = request.getParameterValues("dicount");
		//所有的备注信息
		String privilegeReason="";
		String[] privilegeReasons = request.getParameterValues("privilegereason");
		//同行总价
		String inputtxtotalprice = request.getParameter("inputtxtotalprice");
		//优惠总价
		String inputyhtotalprice = request.getParameter("inputyhtotalprice");
		//同行结算总价
		String inputtxjstotalprice = request.getParameter("inputtxjstotalprice");
		//申请优惠总价
		String inputsqyhtotalprice = request.getParameter("inputsqyhtotalprice");
		//没有币种的优惠总额
		String inputsqyhtotalpriceNoMark = request.getParameter("inputsqyhtotalpriceNoMark");
		//表单重复提交
		String token = request.getParameter("token");
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		String orderId = request.getParameter("orderId");
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		if("".equals(product.getDeptId())){
			result.put("result", "部门为空！");
			return result;
		}
		int orderPersonNum =0;
		//计算申请优惠人数
		for (int i = 0; i < allChecked.length; i++) {
			if("1".equals(allChecked[i])){
				orderPersonNum++;
				//游客ID拼接
				travelerId = travelerId + travelerIds[i]+",";
				//游客Name拼接
				travelerName =travelerName + travelerNames[i]+",";
				//游客类型
				travelerType = travelerType+travelerTypes[i]+",";
				//币种Id
				currencyId = currencyId +currencyIds[i]+",";
				//同行价
				thPrice = thPrice+thPrices[i]+",";
				//优惠金额
				inpuryhprice = inpuryhprice+inpuryhprices[i]+",";
				//同行结算价
				thjsjinputprice =thjsjinputprice+thjsjinputprices[i]+",";
				//申请优惠金额
				sqyhPrice = sqyhPrice +sqyhPrices[i]+",";
				//备注信息
				privilegeReason = privilegeReason +travelerIds[i]+"@_@#_="+ privilegeReasons[i]+",";
				continue;
			}
		}
		//优惠金额
		String yhNum = UUID.randomUUID().toString();
		//同行价
		String thNum = UUID.randomUUID().toString();
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1, yhNum);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_2, thNum);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, product.getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, product.getAcitivityName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID,orderId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, productOrder.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, productOrder.getOrderCompany());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, productOrder.getProductGroupId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, product.getGroupOpenCode());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, productOrder.getOrderNum());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, product.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, productOrder.getOrderCompanyName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, ProductType.PRODUCT_LOOSE);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, userId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, UserUtils.getUser().getName());
		variables.put("applyPrivilegePersonNum",orderPersonNum);
		variables.put("orderPersonNum", productOrder.getOrderPersonNum());
		/**
		 * 详情需要展示的数据
		 */
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, travelerName);
		variables.put("travelerType", travelerType);
		variables.put("currencyId", currencyId);
		variables.put("thPrice", thPrice);
		variables.put("inpuryhprice", inpuryhprice);
		variables.put("thjsjinputprice", thjsjinputprice);
		variables.put("sqyhPrice", sqyhPrice);
		variables.put("privilegeReason", privilegeReason);
		variables.put("thTotalPrice", inputtxtotalprice);
		variables.put("inputtxjstotalprice", inputtxjstotalprice);
		variables.put("inputsqyhtotalpriceNoMark", inputsqyhtotalpriceNoMark);
		variables.put("inputyhtotalprice", inputyhtotalprice);
		if(StringUtils.isBlank(inputsqyhtotalprice)){
			variables.put("inputsqyhtotalprice", "0");
		}else{
			variables.put("inputsqyhtotalprice", inputsqyhtotalprice);
		}
		/**
		 * 详情需要展示的数据
		 */
		CommonResult commonResult = null;
		
		try {
			commonResult = reviewMutexService.check(orderId,"0", ProductType.PRODUCT_LOOSE.toString(), Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString(), false);
			if(commonResult.getSuccess()){
				//发送流程申请
				ReviewResult rResult = processReviewService.start(UserUtils.getUser().getId().toString(),UserUtils.getUser().getCompany().getUuid().toString(),userReviewPermissionChecker,null,Context.ORDER_TYPE_SP,Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE,product.getDeptId(),"",variables);
				//流程ID
				String  rid = rResult.getReviewId();
				Boolean brResult =  rResult.getSuccess(); 
				if(brResult){
					for (int i = 0; i < allChecked.length; i++) {
						NewProcessMoneyAmount yhMoneyAmount = new NewProcessMoneyAmount();
						if("1".equals(allChecked[i])){
							yhMoneyAmount.setCurrencyId(Integer.parseInt(currencyIds[i]));
							yhMoneyAmount.setAmount(new BigDecimal(sqyhPrices[i]));
							yhMoneyAmount.setUid(Long.parseLong(orderId));
							yhMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE);
							yhMoneyAmount.setOrderType(ProductType.PRODUCT_LOOSE);
							yhMoneyAmount.setSerialNum(yhNum);
							yhMoneyAmount.setBusindessType(1);
							yhMoneyAmount.setCreateTime(new Date());
							yhMoneyAmount.setCreatedBy(userId);
							yhMoneyAmount.setDelFlag("0");
							yhMoneyAmount.setCompanyId(companyUuid);
							yhMoneyAmount.setReviewId(rResult.getReviewId());
							processMoneyAmountService.saveNewProcessMoneyAmount(yhMoneyAmount);
						}
					}
					for (int i = 0; i < allChecked.length; i++) {
						NewProcessMoneyAmount thMoneyAmount = new NewProcessMoneyAmount();
						if("1".equals(allChecked[i])){
							thMoneyAmount.setCurrencyId(Integer.parseInt(currencyIds[i]));
							thMoneyAmount.setAmount(new BigDecimal(thjsjinputprices[i]));
							thMoneyAmount.setUid(Long.parseLong(orderId));
							thMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE);
							thMoneyAmount.setOrderType(ProductType.PRODUCT_LOOSE);
							thMoneyAmount.setSerialNum(thNum);
							thMoneyAmount.setBusindessType(1);
							thMoneyAmount.setCreateTime(new Date());
							thMoneyAmount.setCreatedBy(userId);
							thMoneyAmount.setDelFlag("0");
							thMoneyAmount.setCompanyId(companyUuid);
							thMoneyAmount.setReviewId(rResult.getReviewId());
							processMoneyAmountService.saveNewProcessMoneyAmount(thMoneyAmount);
						}
					}
					result.put("result", "申请成功！");
				}else{
					result.put("result", rResult.getMessage());
				}
			}else{
				result.put("result", commonResult.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	//计算总同行价
	@SuppressWarnings("unused")
	private String thTatalPrice(String orderId){
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		//计算总同行价start
		BigDecimal  orderPersonNumAdult = new BigDecimal(productOrder.getOrderPersonNumAdult()) ;
		BigDecimal  orderPersonNumChild = new BigDecimal(productOrder.getOrderPersonNumChild());
		BigDecimal  orderPersonNumSpecial = new BigDecimal(productOrder.getOrderPersonNumSpecial());
		//同行价币种
		String[] currency_type = productGroup.getCurrencyType().split(",");
		String  orderPersonNumAdultcurrency = currency_type[0];
		String  orderPersonNumChildcurrency = currency_type[1];
		String  orderPersonNumSpecialcurrency = currency_type[2];
		BigDecimal settlementAdultPrice = productGroup.getSettlementAdultPrice();
		BigDecimal settlementcChildPrice = productGroup.getSettlementcChildPrice();
		BigDecimal settlementSpecialPrice = productGroup.getSettlementSpecialPrice();
		BigDecimal AdultTotalPrice = null;
		BigDecimal ChildTotalPrice = null;
		BigDecimal SpecialTotalPrice = null;
		String AdultPrice = "";
		String ChildPrice = "";
		String SpecialPrice = "";
		String totalPrice ="";
		if(Integer.parseInt(orderPersonNumAdult.toString())>0){
			AdultTotalPrice = orderPersonNumAdult.multiply(settlementAdultPrice);
			AdultPrice = orderPersonNumAdultcurrency +":"+AdultTotalPrice.toString();
		}
		if(Integer.parseInt(orderPersonNumChild.toString())>0){
			ChildTotalPrice = orderPersonNumChild.multiply(settlementcChildPrice);
			ChildPrice = orderPersonNumChildcurrency +":"+ChildTotalPrice.toString();
		}
		if(Integer.parseInt(orderPersonNumSpecial.toString())>0){
			SpecialTotalPrice = orderPersonNumSpecial.multiply(settlementSpecialPrice);
			SpecialPrice = orderPersonNumSpecialcurrency+":"+SpecialTotalPrice.toString();
		}
		totalPrice = AdultPrice+"+"+ChildPrice+"+"+SpecialPrice;
		String[] totalPrices = totalPrice.split("\\+");
		Map<String, Double> priceMap = new HashMap<String, Double>();
		Map<String, String> priceMap2 = new HashMap<String, String>();
		for (int i = 0; i < totalPrices.length; i++) {
			String[] s = totalPrices[i].split(":");
			if(priceMap.containsKey(s[0])){
				priceMap.put(s[0], Double.parseDouble(priceMap.get(s[0]).toString())+Double.parseDouble(s[1]));
			}else{
				priceMap.put(s[0], Double.parseDouble(s[1]));
			}
		}
		Double val;
	    String name;
	    Set<String> keySet = priceMap.keySet();
	    DecimalFormat myformat = new DecimalFormat();
		myformat.applyPattern("##,###.00");
	    for (String  key : keySet) {
			val = priceMap.get(key);
			name = currencyService.findCurrency(Long.parseLong(key)).getCurrencyMark();
			priceMap2.put(name, myformat.format(val));
		}
	    String mt = priceMap2.toString();
	    mt=mt.replaceAll("\\=", " ");
	    mt=mt.substring(1, mt.length()-1);
		//计算总同行价end
	    return mt;
	}

}
