package com.trekiz.admin.modules.traveler.web;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

/**
 * 
 *  文件名: TravelerController.java
 *  功能:游客业务
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-10-28 上午11:35:20
 *  @version 1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/traveler/manage")
public class TravelerController extends BaseController {
    
    protected static final Logger logger = LoggerFactory.getLogger(TravelerController.class);
    
    @Autowired
    private TravelerService travelerService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private VisaProductsService visaProductsService;
    @Autowired
    private DictService dictService;
    @Autowired
    private OrderCommonService orderService;
    @Autowired
    private OrderPayService orderPayService;
	/**
	 * 游客添加
	 */
	@ResponseBody
	@RequestMapping(value="save")
	public Object save(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> paraMap = Maps.newHashMap();
			JSONObject jsonObject = JSONObject.fromObject(request.getParameter("travelerInfo"));
			JSONArray costArr = JSONArray.fromObject(request.getParameter("costs"));
			JSONArray payPriceArr = JSONArray.fromObject(request.getParameter("payPrice"));	//结算价
			JSONArray costPriceArr = JSONArray.fromObject(request.getParameter("costPrice")); //成本价
			JSONArray visasArr = JSONArray.fromObject(request.getParameter("visas"));
			JSONArray travelerCost = JSONArray.fromObject(request.getParameter("travelerCost"));
			Integer orderType = Integer.parseInt(request.getParameter("orderType"));
			
			paraMap.put("totalCharge", request.getParameter("totalCharge"));
			paraMap.put("orderModifyFlag", request.getParameter("orderModifyFlag"));
			paraMap.put("cbCurrencyId", request.getParameter("cbCurrencyId"));
			paraMap.put("cbCurrencyPrice", request.getParameter("cbCurrencyPrice"));
			paraMap.put("jsCurrencyId", request.getParameter("jsCurrencyId"));
			paraMap.put("jsCurrencyPrice", request.getParameter("jsCurrencyPrice"));
			paraMap.put("rebatesMoney", request.getParameter("rebatesMoney"));
			paraMap.put("rebatesCurrencyId", request.getParameter("rebatesCurrencyId"));
			paraMap.put("orderPersonelNum", request.getParameter("orderPersonelNum"));
			paraMap.put("roomNumber", request.getParameter("roomNumber"));
			paraMap.put("orderPersonNumChild", request.getParameter("orderPersonNumChild"));
			paraMap.put("orderPersonNumAdult", request.getParameter("orderPersonNumAdult"));
			paraMap.put("orderPersonNumSpecial", request.getParameter("orderPersonNumSpecial"));
			// 26需求，判断订单是否是新生成的
			paraMap.put("newOrderFlag", request.getParameter("newOrderFlag"));

			//获取游客基本信息
			Traveler traveler = getTraveler(jsonObject, orderType, false);
			travelerService.saveTraveler(orderService, jsonObject, traveler, orderType, costArr, payPriceArr, visasArr,costPriceArr, paraMap);
			if (traveler != null) {
				//保存签证
				updateVisaTraveler(traveler, orderType);
				//保存其他游客费用：因报价应用全部，所以需要重新保存其他游客费用
				useAllPrice(travelerCost);
				return traveler;
			} else {
				return "游客总数已与人数相同";
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 游客添加(订单修改)C147-C109
	 * @author yang.jiang
	 */
	@ResponseBody
	@RequestMapping(value="saveTraveler4YouJiaModify")
	public Map<String, Object> saveTraveler4YouJiaModify(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> returnResult = new HashMap<>();
		try {
			Map<String, String> paraMap = Maps.newHashMap();
			JSONObject travelerInfo = JSONObject.fromObject(request.getParameter("travelerInfo"));
			JSONArray costArr = JSONArray.fromObject(request.getParameter("costs"));
			JSONArray payPriceArr = JSONArray.fromObject(request.getParameter("payPrice"));	//结算价
			JSONArray costPriceArr = JSONArray.fromObject(request.getParameter("costPrice")); //成本价
			JSONArray visasArr = JSONArray.fromObject(request.getParameter("visas"));
			JSONArray travelerCost = JSONArray.fromObject(request.getParameter("travelerCost"));
			Integer orderType = Integer.parseInt(request.getParameter("orderType"));
			
			paraMap.put("orderModifyFlag", request.getParameter("orderModifyFlag"));
			paraMap.put("cbCurrencyId", request.getParameter("cbCurrencyId"));
			paraMap.put("cbCurrencyPrice", request.getParameter("cbCurrencyPrice"));
			paraMap.put("jsCurrencyId", request.getParameter("jsCurrencyId"));
			paraMap.put("jsCurrencyPrice", request.getParameter("jsCurrencyPrice"));
			paraMap.put("rebatesMoney", request.getParameter("rebatesMoney"));
			paraMap.put("rebatesCurrencyId", request.getParameter("rebatesCurrencyId"));
			paraMap.put("orderPersonelNum", request.getParameter("orderPersonelNum"));
			paraMap.put("roomNumber", request.getParameter("roomNumber"));
			paraMap.put("orderPersonNumChild", request.getParameter("orderPersonNumChild"));
			paraMap.put("orderPersonNumAdult", request.getParameter("orderPersonNumAdult"));
			paraMap.put("orderPersonNumSpecial", request.getParameter("orderPersonNumSpecial"));
			paraMap.put("groupHandleId", request.getParameter("groupHandleId"));  //团控记录id（与订单id属于一对一关系）
			paraMap.put("productId", request.getParameter("productId"));
			paraMap.put("productGroupId", request.getParameter("productGroupId"));
			paraMap.put("activityKind", request.getParameter("activityKind"));
			paraMap.put("agentId", request.getParameter("agentId"));
			paraMap.put("orderId", request.getParameter("orderId"));
			paraMap.put("orderNum", request.getParameter("orderNum"));
//			paraMap.put("isForYouJia", request.getParameter("isForYouJia"));  //是否是优佳
			// 26需求，判断订单是否是新生成的
			paraMap.put("newOrderFlag", request.getParameter("newOrderFlag"));

			//获取游客基本信息
			Traveler traveler = getTraveler4Youjia(travelerInfo, orderType, true);
			returnResult = travelerService.saveTravelerMainInfo4YouJiaModify(orderService, travelerInfo, traveler, orderType, costArr, payPriceArr, visasArr,costPriceArr, paraMap);
			if (returnResult.get("traveler") != null) {
				//C147 & C109 不产生、更新签证子订单
				//保存其他游客费用：因报价应用全部，所以需要重新保存其他游客费用
				useAllPrice(travelerCost);
			} else {
				returnResult.put("msg", "1");
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return returnResult;
	}	

	/**
	 * C147&C109 游客添加
	 * @author yunpeng.zhang
	 */
	@ResponseBody
	@RequestMapping(value="save4YouJia")
	public Map<String, Object> save4YouJia(HttpServletRequest request) {
		Map<String, Object> returnResult = new HashMap<>();
		try {
			JSONObject jsonObject = JSONObject.fromObject(request.getParameter("travelerInfo"));
			JSONArray costArr = JSONArray.fromObject(request.getParameter("costs"));
			JSONArray payPriceArr = JSONArray.fromObject(request.getParameter("payPrice"));		//结算价
			JSONArray costPriceArr = JSONArray.fromObject(request.getParameter("costPrice")); 	//成本价
			JSONArray visasArr = JSONArray.fromObject(request.getParameter("visas"));
			JSONArray travelerCost = JSONArray.fromObject(request.getParameter("travelerCost"));
			Integer orderType = Integer.parseInt(request.getParameter("orderType"));
			Map<String, String> params = Maps.newHashMap();

			params.put("rebatesMoney", request.getParameter("rebatesMoney"));
			params.put("rebatesCurrencyId", request.getParameter("rebatesCurrencyId"));
			params.put("orderPersonelNum", request.getParameter("orderPersonelNum"));
			params.put("productId", request.getParameter("productId"));
			params.put("productGroupId", request.getParameter("productGroupId"));
			params.put("activityKind", request.getParameter("activityKind"));
			params.put("agentId", request.getParameter("agentId"));
			params.put("groupHandleId", request.getParameter("groupHandleId"));
			params.put("discountPrice", request.getParameter("discountPrice"));								// 范围内的优惠金额
			params.put("activityDiscountPrice", request.getParameter("activityDiscountPrice"));				// 团期优惠金额

			//获取游客基本信息
			Traveler traveler = getTraveler4YouJia(jsonObject, orderType, true);
			Integer groupHandleId = travelerService.saveTraveler4YouJia(jsonObject,traveler, orderType, costArr,
					payPriceArr, visasArr, costPriceArr, params);
			returnResult.put("groupHandleId", groupHandleId);
			if (traveler != null) {
				//保存签证
				updateVisaTraveler(traveler, orderType);
				//保存其他游客费用：因报价应用全部，所以需要重新保存其他游客费用
				useAllPrice(travelerCost);
				returnResult.put("traveler", traveler);
			} else {
				returnResult.put("msg", "1");
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}

		return  returnResult;
	}

	/**
	 * 清除游客优惠信息
     */
	@ResponseBody
	@RequestMapping(value="updateApplyDiscount4YouJia")
	public Map<String, String> updateApplyDiscount4YouJia(HttpServletRequest request) {
		Map<String, String> resultMap = new HashMap<>();
		try {
			String oldApplyTravelerIds = request.getParameter("oldApplyTravelerIds");
			String applyInfos = request.getParameter("applyInfos");
			travelerService.handleTravelerApplyDiscountInfo(oldApplyTravelerIds, applyInfos);
		} catch(Exception e) {
			e.printStackTrace();
			resultMap.put("result", "2");
			resultMap.put("message", e.getMessage());
		}

		return resultMap;
	}
	
	/**
	 * 保存其他游客费用
	 * @param travelerCost
	 */
	private void useAllPrice(JSONArray travelerCost) {
		JSONArray jsonArray = new JSONArray();
		if (travelerCost != null && travelerCost.size() > 0) {
			String temp = "";
			for (int i=0;i<travelerCost.size();i++) {
				JSONObject object = travelerCost.getJSONObject(i);
				String travelerId = object.getString("travelerId");
				if (StringUtils.isNotBlank(travelerId)) {
					if (!"".equals(temp) && !temp.equals(travelerId)) {
						travelerService.saveCost(jsonArray, Long.parseLong(temp));
						jsonArray = new JSONArray();
					}
					if (object.size() > 1) {
						jsonArray.add(object);
					}
					if (i == travelerCost.size()-1) {
						travelerService.saveCost(jsonArray, Long.parseLong(travelerId));
					}
				}
				temp = object.getString("travelerId");
			}
			
		}
	}

	/**
	 *  功能: 保存游客基本信息
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-11-13 上午10:44:06
	 *  @param jsonObject
	 *  @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private Traveler getTraveler(JSONObject jsonObject,int orderType, Boolean isForYouJia) {
		/** 创建要保存的新游客实体 */ 
		Traveler traveler = new Traveler();
		String travelerId = jsonObject.getString("travelerId");
		if(StringUtils.isNotBlank(travelerId)){
			traveler.setId(Long.parseLong(travelerId));
		}
		/** 查找源游客信息，赋值到新实体中 */
		if (traveler.getId() != null) {
			Traveler srcTraveler = travelerService.findTravelerById(traveler.getId());
			BeanUtils.copyProperties(srcTraveler, traveler);
		}
		/** 新实体添加页面获取的属性 */
		traveler.setOrderType(orderType);
		String orderId = jsonObject.getString("travelerOrderId");
		traveler.setOrderId(Long.parseLong(orderId));
		//联运信息
		String intermodalType = "0";
		if(jsonObject.containsKey("travelerIntermodalType")){
			intermodalType = jsonObject.getString("travelerIntermodalType");
			if("1".equals(intermodalType)){
				String intermodalId = jsonObject.getString("intermodalId");
				traveler.setIntermodalId(Long.parseLong(intermodalId));
			}
		}
		traveler.setIntermodalType(Integer.parseInt(intermodalType));
		//基本信息
		String name = jsonObject.getString("travelerName");
		String nameSpell = jsonObject.getString("travelerPinyin");
		int sex = Integer.parseInt(jsonObject.getString("sex"));
		Integer nationality = null;
		String positionCn = null;
		String positionEn = null;
		if(isForYouJia) {
			positionCn = jsonObject.getString("positionCn");
			positionEn = jsonObject.getString("positionEn");
		} else {
			nationality = Integer.parseInt(jsonObject.getString("nationality"));
		}
		if(UserUtils.getUser().getCompany().getUuid().equals("980e4c74b7684136afd89df7f89b2bee")){
			if (jsonObject.containsKey("issuePlace1")) {
				String issuePlace1 = jsonObject.getString("issuePlace1");
				if(StringUtils.isNotBlank(issuePlace1)){
					traveler.setIssuePlace1(issuePlace1);
				}
			}
			if (jsonObject.containsKey("issueDate")) {
				String issueDate = jsonObject.getString("issueDate");
				if(StringUtils.isNotBlank(issueDate)){
					traveler.setIssueDate(DateUtils.parseDate(issueDate));
				}
			}
			if (jsonObject.containsKey("issueDate")) {
				String hometown = jsonObject.getString("hometown");
				if(StringUtils.isNotBlank(hometown)){
					traveler.setHometown(hometown);
				}
			}
		}
		String birthDay = jsonObject.getString("birthDay");
		String telephone = jsonObject.getString("telephone");
		String passportCode = jsonObject.getString("passportCode");
		String issuePlace = jsonObject.getString("issuePlace");
		String passportValidity = jsonObject.getString("passportValidity");
		String passportType = jsonObject.getString("passportType");
		String idCard = jsonObject.getString("idCard");
		String remark = jsonObject.getString("remark");
		// 需求299   价格备注
		String priceRemark = jsonObject.containsKey("priceRemark") ? jsonObject.getString("priceRemark") : "";
		String hotelDemand = jsonObject.containsKey("hotelDemand") ? jsonObject.getString("hotelDemand") : "2";
		String singleDiff = jsonObject.containsKey("singleDiff") ? jsonObject.getString("singleDiff") : "0";
		String singleDiffCurrency = jsonObject.containsKey("singleDiffCurrencyId") ? jsonObject.getString("singleDiffCurrencyId") : "";
		String singleDiffNight = jsonObject.containsKey("sumNight") ? jsonObject.getString("sumNight") : "";
		String srcPriceCurrency = jsonObject.getString("srcPriceCurrency");
		String srcPrice = jsonObject.getString("srcPrice");


		traveler.setName(name);
		if(StringUtils.isNotBlank(nameSpell)){
			traveler.setNameSpell(nameSpell);
		}
		String personType = jsonObject.getString("personType");
		if(StringUtils.isNotBlank(personType)){
			traveler.setPersonType(Integer.parseInt(personType));
		}
		traveler.setSex(sex);
		traveler.setNationality(nationality);
		if(StringUtils.isNotBlank(birthDay)){
			traveler.setBirthDay(DateUtils.parseDate(birthDay));
		}
		if(StringUtils.isNotBlank(positionCn)){
			traveler.setPositionCn(positionCn);
		}
		if(StringUtils.isNotBlank(positionEn)){
			traveler.setPositionEn(positionEn);
		}
		if(StringUtils.isNotBlank(telephone)){
			traveler.setTelephone(telephone);
		}
		traveler.setPassportCode(passportCode);
		traveler.setPassportType(passportType);
		if(StringUtils.isNotBlank(issuePlace)){
			traveler.setIssuePlace(DateUtils.parseDate(issuePlace));
		}
		traveler.setPassportValidity(DateUtils.parseDate(passportValidity));
		if(StringUtils.isNotBlank(idCard)){
			traveler.setIdCard(idCard);
		}
		if(StringUtils.isNotBlank(remark)){
			traveler.setRemark(remark);
		}
		// 需求299 价格备注
		if (StringUtils.isNotBlank(priceRemark)) {
			traveler.setPriceRemark(priceRemark);
		}
		
		traveler.setHotelDemand(Integer.parseInt(hotelDemand));
		if(StringUtils.isNotBlank(singleDiffCurrency)){
			traveler.setSingleDiffCurrency(currencyService.findCurrency(Long.parseLong(singleDiffCurrency)));
		}
		traveler.setSingleDiff(StringNumFormat.getBigDecimalForTow(singleDiff));
		if(StringUtils.isNotBlank(singleDiffNight)){
			traveler.setSingleDiffNight(Integer.parseInt(singleDiffNight));
		}
		traveler.setSrcPrice(StringNumFormat.getBigDecimalForTow(srcPrice));
		if(StringUtils.isNotBlank(srcPriceCurrency)){
			traveler.setSrcPriceCurrency(currencyService.findCurrency(Long.parseLong(srcPriceCurrency)));
		}

		return traveler;
	}
	
	/**
	 *  功能: 保存游客基本信息
	 *  @author yang.jiang
	 *  @DateTime 2016-2-20 17:41:11
	 *  @param jsonObject
	 *  @return
	 */
	private Traveler getTraveler4Youjia(JSONObject jsonObject,int orderType, Boolean isForYouJia){
		Traveler traveler = new Traveler();
		String travelerId = jsonObject.getString("travelerId");
		if(StringUtils.isNotBlank(travelerId)){
			traveler.setId(Long.parseLong(travelerId));
		}
		traveler.setOrderType(orderType);
		String orderId = jsonObject.getString("travelerOrderId");
		traveler.setOrderId(Long.parseLong(orderId));
		//联运信息
		String intermodalType = "0";
		if(jsonObject.containsKey("travelerIntermodalType")){
			intermodalType = jsonObject.getString("travelerIntermodalType");
			if("1".equals(intermodalType)){
				String intermodalId = jsonObject.getString("intermodalId");
				traveler.setIntermodalId(Long.parseLong(intermodalId));
			}
		}
		traveler.setIntermodalType(Integer.parseInt(intermodalType));
		//基本信息
		String name = jsonObject.getString("travelerName");  //姓名
		String nameSpell = jsonObject.getString("travelerPinyin");  // 姓名英文拼音
		int sex = Integer.parseInt(jsonObject.getString("sex"));  // 性别
		Integer nationality = null;
		String positionCn = null;
		String positionEn = null;
		String passportPlace = null;
		if(isForYouJia) {
			positionCn = jsonObject.getString("positionCn");  // 职位中文
			positionEn = jsonObject.getString("positionEn");  // 职位英文
			passportPlace = jsonObject.getString("passportPlace");  // 护照签发地
		} else {
			nationality = Integer.parseInt(jsonObject.getString("nationality"));
		}
		String birthDay = jsonObject.getString("birthDay");  // 出生日期
		String telephone = jsonObject.getString("telephone");  // 电话
		String passportCode = jsonObject.getString("passportCode");  // 护照号
		String issuePlace = jsonObject.getString("issuePlace");  // 发证日期
		String passportValidity = jsonObject.getString("passportValidity");  // 护照有效期
		String passportType = jsonObject.getString("passportType");  // 护照类型（因公护照、因私护照）
		String idCard = jsonObject.getString("idCard");  // 身份证号码
		String remark = jsonObject.getString("remark");  // 备注
		String hotelDemand = jsonObject.containsKey("hotelDemand") ? jsonObject.getString("hotelDemand") : "2";
		String singleDiff = jsonObject.containsKey("singleDiff") ? jsonObject.getString("singleDiff") : "0";
		String singleDiffCurrency = jsonObject.containsKey("singleDiffCurrencyId") ? jsonObject.getString("singleDiffCurrencyId") : "";
		String singleDiffNight = jsonObject.containsKey("sumNight") ? jsonObject.getString("sumNight") : "";
		String srcPriceCurrency = jsonObject.getString("srcPriceCurrency");
		String srcPrice = jsonObject.getString("srcPrice");
		String fixedDiscountPrice = BigDecimal.ZERO.toString();  // 填写的额内的优惠价
		String orgDiscountPrice = BigDecimal.ZERO.toString();  // 原始额定优惠：报名时的团期优惠
		if(isForYouJia) {
			if (jsonObject.containsKey("fixedDiscount")) {
				fixedDiscountPrice = jsonObject.getString("fixedDiscount");  // 手动输入的额内优惠金额
			}
			if (jsonObject.containsKey("orgDiscountPrice")) {				
				orgDiscountPrice = jsonObject.getString("orgDiscountPrice");  // 原始优惠金额（报名时团期下对应游客类型的优惠额度）
			}
		}
		
		traveler.setName(name);
		if(StringUtils.isNotBlank(nameSpell)){
			traveler.setNameSpell(nameSpell);
		}
		String personType = jsonObject.getString("personType");  // 游客类型
		if(StringUtils.isNotBlank(personType)){
			traveler.setPersonType(Integer.parseInt(personType));
		}
		traveler.setSex(sex);
		traveler.setNationality(nationality);
		if(StringUtils.isNotBlank(birthDay)){
			try {
				traveler.setBirthDay(new SimpleDateFormat("dd/MM/yyyy").parse(birthDay));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(positionCn)){
			traveler.setPositionCn(positionCn);
		}
		if(StringUtils.isNotBlank(positionEn)){
			traveler.setPositionEn(positionEn);
		}
		if(StringUtils.isNotBlank(passportPlace)) {
			traveler.setPassportPlace(passportPlace);
		}
		if(StringUtils.isNotBlank(telephone)){
			traveler.setTelephone(telephone);
		}
		traveler.setPassportCode(passportCode);
		traveler.setPassportType(passportType);
		if(StringUtils.isNotBlank(issuePlace)){
			try {
				traveler.setIssuePlace(new SimpleDateFormat("dd/MM/yyyy").parse(issuePlace));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isNotBlank(passportValidity)) {			
			try {
				traveler.setPassportValidity(new SimpleDateFormat("dd/MM/yyyy").parse(passportValidity));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(idCard)){
			traveler.setIdCard(idCard);
		}
		if(StringUtils.isNotBlank(remark)){
			traveler.setRemark(remark);
		}
		
		traveler.setHotelDemand(Integer.parseInt(hotelDemand));
		if(StringUtils.isNotBlank(singleDiffCurrency)){
			traveler.setSingleDiffCurrency(currencyService.findCurrency(Long.parseLong(singleDiffCurrency)));
		}
		traveler.setSingleDiff(StringNumFormat.getBigDecimalForTow(singleDiff));
		if(StringUtils.isNotBlank(singleDiffNight)){
			traveler.setSingleDiffNight(Integer.parseInt(singleDiffNight));
		}
		traveler.setSrcPrice(StringNumFormat.getBigDecimalForTow(srcPrice));
		if(StringUtils.isNotBlank(srcPriceCurrency)){
			traveler.setSrcPriceCurrency(currencyService.findCurrency(Long.parseLong(srcPriceCurrency)));
		}
		
		traveler.setFixedDiscountPrice(StringNumFormat.getBigDecimalForTow(fixedDiscountPrice));  // 填写的额内的优惠价
		traveler.setOrgDiscountPrice(StringNumFormat.getBigDecimalForTow(orgDiscountPrice));  // 填写的报名时或初次添加游客时的优惠定额
		
		return traveler;
	}

	/**
	 * 获取游客
     */
	private Traveler getTraveler4YouJia(JSONObject jsonObject,int orderType, Boolean isForYouJia){
		Traveler traveler = new Traveler();
		String travelerId = jsonObject.getString("travelerId");
		if(StringUtils.isNotBlank(travelerId)){
			traveler.setId(Long.parseLong(travelerId));
		}
		traveler.setOrderType(orderType);
		String orderId = jsonObject.getString("travelerOrderId");
		traveler.setOrderId(Long.parseLong(orderId));
		//联运信息
		String intermodalType = "0";
		if(jsonObject.containsKey("travelerIntermodalType")){
			intermodalType = jsonObject.getString("travelerIntermodalType");
			if("1".equals(intermodalType)){
				String intermodalId = jsonObject.getString("intermodalId");
				traveler.setIntermodalId(Long.parseLong(intermodalId));
			}
		}
		traveler.setIntermodalType(Integer.parseInt(intermodalType));
		//基本信息
		String name = jsonObject.getString("travelerName");
		String nameSpell = jsonObject.getString("travelerPinyin");
		int sex = Integer.parseInt(jsonObject.getString("sex"));
		Integer nationality = null;
		String positionCn = null;
		String positionEn = null;
		String passportPlace = null;
		if(isForYouJia) {
			positionCn = jsonObject.getString("positionCn");
			positionEn = jsonObject.getString("positionEn");
			passportPlace = jsonObject.getString("passportPlace");
		} else {
			nationality = Integer.parseInt(jsonObject.getString("nationality"));
		}
		String birthDay = jsonObject.getString("birthDay");
		String telephone = jsonObject.getString("telephone");
		String passportCode = jsonObject.getString("passportCode");
		String issuePlace = jsonObject.getString("issuePlace");
		String passportValidity = jsonObject.getString("passportValidity");
		String passportType = jsonObject.getString("passportType");
		String idCard = jsonObject.getString("idCard");
		String remark = jsonObject.getString("remark");
		String hotelDemand = jsonObject.containsKey("hotelDemand") ? jsonObject.getString("hotelDemand") : "2";
		String singleDiff = jsonObject.containsKey("singleDiff") ? jsonObject.getString("singleDiff") : "0";
		String singleDiffCurrency = jsonObject.containsKey("singleDiffCurrencyId") ? jsonObject.getString("singleDiffCurrencyId") : "";
		String singleDiffNight = jsonObject.containsKey("sumNight") ? jsonObject.getString("sumNight") : "";
		String srcPriceCurrency = jsonObject.getString("srcPriceCurrency");
		String srcPrice = jsonObject.getString("srcPrice");

		traveler.setName(name);
		if(StringUtils.isNotBlank(nameSpell)){
			traveler.setNameSpell(nameSpell);
		}
		String personType = jsonObject.getString("personType");
		if(StringUtils.isNotBlank(personType)){
			traveler.setPersonType(Integer.parseInt(personType));
		}
		traveler.setSex(sex);
		traveler.setNationality(nationality);
		if(StringUtils.isNotBlank(birthDay)){
			try {
				traveler.setBirthDay(new SimpleDateFormat("dd/MM/yyyy").parse(birthDay));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(positionCn)){
			traveler.setPositionCn(positionCn);
		}
		if(StringUtils.isNotBlank(positionEn)){
			traveler.setPositionEn(positionEn);
		}
		if(StringUtils.isNotBlank(passportPlace)) {
			traveler.setPassportPlace(passportPlace);
		}
		if(StringUtils.isNotBlank(telephone)){
			traveler.setTelephone(telephone);
		}
		traveler.setPassportCode(passportCode);
		traveler.setPassportType(passportType);
		if(StringUtils.isNotBlank(issuePlace)){
			try {
				traveler.setIssuePlace(new SimpleDateFormat("dd/MM/yyyy").parse(issuePlace));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(passportValidity)) {
			try {
				traveler.setPassportValidity(new SimpleDateFormat("dd/MM/yyyy").parse(passportValidity));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(idCard)){
			traveler.setIdCard(idCard);
		}
		if(StringUtils.isNotBlank(remark)){
			traveler.setRemark(remark);
		}

		traveler.setHotelDemand(Integer.parseInt(hotelDemand));
		if(StringUtils.isNotBlank(singleDiffCurrency)){
			traveler.setSingleDiffCurrency(currencyService.findCurrency(Long.parseLong(singleDiffCurrency)));
		}
		traveler.setSingleDiff(StringNumFormat.getBigDecimalForTow(singleDiff));
		if(StringUtils.isNotBlank(singleDiffNight)){
			traveler.setSingleDiffNight(Integer.parseInt(singleDiffNight));
		}
		traveler.setSrcPrice(StringNumFormat.getBigDecimalForTow(srcPrice));
		if(StringUtils.isNotBlank(srcPriceCurrency)){
			traveler.setSrcPriceCurrency(currencyService.findCurrency(Long.parseLong(srcPriceCurrency)));
		}
		return traveler;
	}
	
	/**
	 * 如果是单团订单且已生成子订单，则修改单团订单游客信息的时候，需同步修改签证游客信息
	 * @param traveler
	 * @param orderType
	 */
	private void updateVisaTraveler(Traveler traveler, Integer orderType) {
		
		//如果是修改游客信息且订单类型不为空
		if (traveler.getId() != null && orderType != null) {
			String order_type = orderType.toString();
			//判断是否是单团订单
			if (Context.ORDER_STATUS_SINGLE.equals(order_type) 
					|| Context.ORDER_STATUS_LOOSE.equals(order_type)
					|| Context.ORDER_STATUS_STUDY.equals(order_type)
					|| Context.ORDER_STATUS_BIG_CUSTOMER.equals(order_type)
					|| Context.ORDER_STATUS_FREE.equals(order_type)) {
				if (traveler.getOrderId() != null) {
					List<Orderpay> payList = orderPayService.findOrderpayByOrderId(traveler.getOrderId(), orderType);
					if (CollectionUtils.isNotEmpty(payList) && UserUtils.getUser().getCompany().getCreateSubOrder().contains("2")) {
						orderService.createSingleSubOrder(traveler.getOrderId());
					}
				}
			}
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value ="deleteTraveler")
	public String deleteTraveler(HttpServletRequest request, HttpServletResponse response){
	    String ids = request.getParameter("travelerId");
	    if(StringUtils.isNotBlank(ids)){
	    	travelerService.deleteTraveler(Long.parseLong(ids));
	    }
	    return "ok";
	}
	
	@ResponseBody
	@RequestMapping(value ="getVisaType")
	public String getVisaType(HttpServletRequest request, HttpServletResponse response){
	    String countryId = request.getParameter("countryId");
	    String manor =  request.getParameter("manor");
	    List<VisaProducts> vpList = visaProductsService.findVisaProductsByCountryIdAndManor(Integer.parseInt(countryId), manor);
	    JSONArray vpListJsonArray = new JSONArray();
	    for(VisaProducts vp: vpList){
	    	JSONObject visaType = new JSONObject();
	    	Dict visaTypeDict = dictService.findByValueAndType(vp.getVisaType().toString(), Context.DICT_TYPE_NEW_VISATYPE);
	    	visaType.put(vp.getVisaType(), visaTypeDict.getLabel());
	    	vpListJsonArray.add(visaType);
	    }
	    return vpListJsonArray.toString();
	}

}
