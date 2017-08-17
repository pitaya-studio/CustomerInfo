/**
 * 
 */
package com.trekiz.admin.review.rebates.visa.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.rebates.entity.RebatesNew;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.review.visaRebates.entity.MoreCurrencyComputePrice;
import com.trekiz.admin.modules.review.visaborrowmoney.service.IVisaBorrowMoneyService;
import com.trekiz.admin.modules.review.visaborrowmoney.web.VisaBorrowMoneyController;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.changePrice.airticket.bean.ReviewResultBean;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import com.trekiz.admin.review.rebates.singleGroup.repository.RebatesNewDao;
import com.trekiz.admin.review.rebates.visa.bean.VisaRebateInput;
import com.trekiz.admin.review.rebates.visa.service.NewVisaRebateService;


/**
 * 描述:签证返佣
 * 
 * @author zhanghao 2015-12-09
 */
@Service
@Transactional
public class NewVisaRebateServiceImpl extends BaseService implements NewVisaRebateService {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewMutexService reviewMutexService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private IVisaBorrowMoneyService visaBorrowMoneyService;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private RebatesNewDao rebatesNewDao;
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	/**
	 * 查询签证返佣申请记录
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-10 下午2:27:57
	 */
	public List<Map<String, Object>> findReviewListMap(String orderId){
		VisaOrder order = visaOrderService.findVisaOrder(Long.valueOf(orderId));
		//通过orderId获取产品的发布部门
		Long deptId = visaOrderService.getProductPept(Long.valueOf(orderId));
		List<Map<String, Object>> processList = reviewService.getReviewDetailMapListByOrderId(deptId, Context.ORDER_TYPE_QZ, Context.REBATES_FLOW_TYPE, orderId, OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
		
		
		if(CollectionUtils.isNotEmpty(processList)) {
			for(Map<String, Object> map : processList) {
				String travellerId = map.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString();//游客Id集合封装
				
				// 游客/团队 团队返佣/个人返佣（预计返佣金额）
				if (StringUtils.isBlank(travellerId)) {
					//团队预计返佣
					String groupRebatesUUID = order.getGroupRebate();
					if(StringUtils.isBlank(groupRebatesUUID)){
						map.put("yujiRebates", "—");
					}else{
						MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(groupRebatesUUID);
						if(moneyAmount != null){
							if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO) != 0){
								map.put("yujiRebates", OrderCommonUtil.getMoneyAmountBySerialNum(groupRebatesUUID, 2));
							}else{
								map.put("yujiRebates", "—");
							}
						}else{
							map.put("yujiRebates", "—");
						}
					}
				} else {
					String[] tavelerIds = travellerId.split(VisaBorrowMoneyController.SPLITMARK);
					
					List<String> rebatesMoneySerialNums = new ArrayList<String>();
					for(String tavelerId : tavelerIds) {
						Traveler traveler = travelerDao.findById(Long.parseLong(tavelerId));
						//个人预计返佣
						String travelerRebatesUUID = traveler.getRebatesMoneySerialNum();
						if(StringUtils.isBlank(travelerRebatesUUID)){
							
						}else{
							MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(travelerRebatesUUID);
							if(moneyAmount != null){
								if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO) != 0){
									rebatesMoneySerialNums.add(travelerRebatesUUID);
								}
							}
						}
						
						if(CollectionUtils.isEmpty(rebatesMoneySerialNums)) {
							map.put("yujiRebates", "-");
						} else {
							map.put("yujiRebates", moneyAmountService.getCurrMarkMoneyAmountStr(rebatesMoneySerialNums));
						}
					}
				}
				
				//游客/团队名称封装
				String showName = "";
				String travellerName = map.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME).toString();//游客名称集合封装
				String grouprebatesnames = map.get("grouprebatesnames").toString();
				if(StringUtils.isNotEmpty(grouprebatesnames)) {
					showName = "团队";
				} else if(StringUtils.isNotEmpty(travellerId)) {
					String[] travellerIdArr = travellerId.split(VisaBorrowMoneyController.SPLITMARK);
					if(travellerIdArr.length > 1) {
						showName = "团队";
					} else if(StringUtils.isNotEmpty(grouprebatesnames)) {
						showName = "团队";
					} else {
						showName = travellerName.split(VisaBorrowMoneyController.SPLITMARK)[0];
					}
				}
				map.put("showName", showName);
				
				if("团队".equals(showName)) {
					VisaOrder visaOrder = visaOrderDao.findByOrderId(Long.parseLong(orderId));
					map.put("yingshouJe", visaOrder.getTotalMoney());
				} else {
					Traveler traveler = travelerDao.findById(Long.parseLong(travellerId.split(VisaBorrowMoneyController.SPLITMARK)[0]));
					map.put("yingshouJe", traveler.getPayPriceSerialNum());
				}
				
				//备注信息拼装
				String trvrebatesnotes = map.get("trvrebatesnotes").toString();
				String grouprebatesnodes = map.get("grouprebatesnodes").toString();
				StringBuffer allNotes = new StringBuffer();
				if(StringUtils.isNotEmpty(trvrebatesnotes)) {
					for(String trvrebatesnote : trvrebatesnotes.split(VisaBorrowMoneyController.SPLITMARK)) {
						if(StringUtils.isNotEmpty(trvrebatesnote)) {
							allNotes.append(trvrebatesnote);
							allNotes.append(" ");
						}
					}
				}
				if(StringUtils.isNotEmpty(grouprebatesnodes)) {
					for(String grouprebatesnode : grouprebatesnodes.split(VisaBorrowMoneyController.SPLITMARK)) {
						if(StringUtils.isNotEmpty(grouprebatesnode)) {
							allNotes.append(grouprebatesnode);
							allNotes.append(" ");
						}
					}
				}
				map.put("allNotes", allNotes.toString());
			}
		}
		return processList;
	}
	
	/**
	 * 验证是否存在互斥流程
	 * @param input
	 * @return
	 */
	public ReviewResultBean checkReview(VisaRebateInput input){
		ReviewResultBean reviewResultBean = new ReviewResultBean();
		reviewResultBean.setSuccess(true);
		
		String orderId = input.getVisaOrderId();
		String[] travellerIds = input.getTrvids();
		String[] rebatePrice = input.getLendPrice();
		
		int applyCount = 0;//发起申请的个数，即返佣金额不为0的个数
		if(ArrayUtils.isNotEmpty(rebatePrice)){
			for(String price:rebatePrice){
				if(StringUtils.isNotBlank(price)){
					applyCount++;
				}
			}
		}
		if(applyCount==0){//没有申请的个数，不进行互斥验证
			return reviewResultBean;
		}
		
		CommonResult result;
		try {
			result =reviewMutexService.check(orderId, Arrays.asList(travellerIds), String.valueOf(Context.ORDER_TYPE_QZ), String.valueOf(Context.REBATES_FLOW_TYPE));
			reviewResultBean.setSuccess(result.getSuccess());
			reviewResultBean.putMessage(result.getMessage()+"请重新选择游客");
		} catch (Exception e) {
			reviewResultBean.setSuccess(false);
			reviewResultBean.putMessage("调用流程互斥验证接口异常！");
		}
		
		return reviewResultBean;
	}
	
	/**
	 * 机票改价申请发起
	 * @param input
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public ReviewResultBean applyStart(VisaRebateInput input) throws Exception{
		ReviewResultBean result = new ReviewResultBean();
		result.getMessage().append("签证返佣");
		String orderId = input.getVisaOrderId();//订单id
		
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
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
			result.getMessage().append("结算单已锁定，不能申请返佣数据");
			return result;
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
		
		//游客借款信息
		String[] trvids = input.getTrvids();//游客ID
		String[] trvnames = input.getTrvnames();//游客姓名
		String[] rebatestrvcurrents = input.getRefundCurrency();//游客币种
		String[] trvborrownames = input.getTrvborrownames();//游客款项名称
		String[] trvamounts = input.getLendPrice();//借款金额  trvamounts
		String[] trvsettlementprices = input.getTrvsettlementprices();//游客结算价
		String[] trvrebatesnotes = input.getTrvborrownotes();//游客借款备注
		
		//团队借款信息
		String[] grouprebatesnames = input.getGroupborrownames();//团队款项名称
		String[] grouprebatescurrents = input.getTeamCurrency();//团队款项币种  groupborrowcurrents
		String[] grouprebatesamounts = input.getTeamMoney();//团队款项借款额  groupborrowamounts
		String[] grouprebatesnodes = input.getGroupborrownodes();//团队借款备注
		
		String grouptotalrebatesnode = input.getOtherRemarks();//总团队借款备注
		//String totalborrowamount = request.getParameter("totalborrowamount");//团队借款总额
		
		//处理订单 游客的 借款信息-----------------------------
		StringBuilder sb_trvids = new StringBuilder("");//游客ID
		
		StringBuilder sb_trvids4Ext = new StringBuilder("");//游客ID ,流程互斥用的多个游客用“，”分隔（统一用“，”分隔，之前的游客字符串不能通用）
		
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
		
		/*** 返佣公用业务表数据  */
		List<RebatesNew> rebatesList = new ArrayList<RebatesNew>();
		
		
		for (int i = 0; i < trvamounts.length; i++) {
			if (null!=trvamounts[i]&&!"".equals(trvamounts[i].trim())) {
				sb_trvids.append(trvids[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客ID
				sb_trvids4Ext.append(trvids[i]).append(",");//游客ID
				sb_trvnames.append(trvnames[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客姓名
				sb_rebatestrvcurrents.append(rebatestrvcurrents[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客币种
				String trvborrowname = ArrayUtils.isEmpty(trvborrownames)||"".equals(trvborrownames[i])?" ":trvborrownames[i];
				sb_trvborrownames.append(trvborrowname).append(VisaBorrowMoneyController.SPLITMARK);//游客款项名称
				sb_trvamounts.append(trvamounts[i]).append(VisaBorrowMoneyController.SPLITMARK);//借款金额
				sb_trvsettlementprices.append(trvsettlementprices[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客结算价
				String trvrebatesnote = ArrayUtils.isEmpty(trvrebatesnotes)||"".equals(trvrebatesnotes[i])?" ":trvrebatesnotes[i];
				sb_trvrebatesnotes.append(trvrebatesnote).append(VisaBorrowMoneyController.SPLITMARK);//游客借款备注
				
				//获取游客借款汇率
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.currency_name,c.currency_mark,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
				buffer.append(rebatestrvcurrents[i]);
				buffer.append(" AND c.del_flag = 0 AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
				Map<String, Object>  mp =  list.get(0);
				sb_trvrebatesexchangerates.append(mp.get("convert_lowest")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_trvcurrencyNames.append(mp.get("currency_name")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_trvcurrencyMarks.append(mp.get("currency_mark")).append(VisaBorrowMoneyController.SPLITMARK);
				
				
				RebatesNew rebates = new RebatesNew();
				rebates.setOrderId(visaOrder.getId());
				Traveler traveler = travelerDao.findById(Long.parseLong(trvids[i]));
				rebates.setTraveler(traveler);
				Currency currency = currencyService.findCurrency(Long.parseLong(rebatestrvcurrents[i]));
				rebates.setCurrency(currency);
				rebates.setCurrencyId(currency.getId());
				rebates.setCurrencyExchangerate(currency.getCurrencyExchangerate());
				rebates.setTotalMoney(trvsettlementprices[i]);
				rebates.setCostname(trvborrowname);
				rebates.setRebatesDiff(new BigDecimal(trvamounts[i]));
				rebates.setRemark(trvrebatesnote);
				rebates.setOrderType(Context.ORDER_TYPE_QZ);
				rebatesList.add(rebates);
			}
		}
		
		Map<String, Object> variables = new HashMap<String, Object>();
		
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, visaOrder.getSalerId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, visaOrder.getSalerName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, agentId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, agentName);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, Context.ORDER_TYPE_QZ);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,visaOrder.getVisaProductId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME,visaProducts.getProductName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, visaOrder.getOrderNo());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaProducts.getGroupCode());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, visaProducts.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, visaOrder.getCreateBy().getId());
		
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID,sb_trvids.toString());// 游客id
		
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1,sb_trvids4Ext.toString());//游客ID ,流程互斥用的多个游客用“，”分隔（统一用“，”分隔，之前的游客字符串不能通用）
		
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME,sb_trvnames.toString());	// 游客名称
		
		variables.put("rebatestrvcurrents", sb_rebatestrvcurrents.toString());//游客币种
		variables.put("trvborrownames", sb_trvborrownames.toString());//游客款项名称
		variables.put("trvamounts", sb_trvamounts.toString());//借款金额
		variables.put("trvsettlementprices", sb_trvsettlementprices.toString());//游客结算价
		variables.put("trvrebatesnotes", sb_trvrebatesnotes.toString());//游客借款备注
		//保存游客借款汇率、币种名称、币种符号
		variables.put("trvrebatesexchangerates", sb_trvrebatesexchangerates.toString());//游客借款汇率
		variables.put("trvcurrencyNames", sb_trvcurrencyNames.toString());//游客借款币种名称
		variables.put("trvcurrencyMarks", sb_trvcurrencyMarks.toString());//游客借款币种符号
		variables.put("totalRebatesJe", input.getTotalRebatesJe());//累计返佣金额
		
		//累计返佣金额 审批通过后会更新此字段，每个申请汇总之前的返佣金额 update by zhanghao 20151230 修复bug11479
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_3, input.getTotalRebatesJe4update());//累计返佣金额
		//累计返佣金额 审批通过后会更新此字段，每个申请汇总之前的返佣金额 update by zhanghao 20151230 修复bug11479
		
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
				String grouprebatesname = ArrayUtils.isEmpty(grouprebatesnames)||"".equals(grouprebatesnames[i])?" ":grouprebatesnames[i];
				sb_grouprebatesnames.append(grouprebatesname).append(VisaBorrowMoneyController.SPLITMARK);//团队款项名称
				sb_grouprebatescurrents.append(grouprebatescurrents[i]).append(VisaBorrowMoneyController.SPLITMARK);//团队款项币种
				sb_grouprebatesamounts.append(grouprebatesamounts[i]).append(VisaBorrowMoneyController.SPLITMARK);//团队款项借款额
				String grouprebatesnode = ArrayUtils.isEmpty(grouprebatesnodes)||"".equals(grouprebatesnodes[i])?" ":grouprebatesnodes[i];
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
				
				
				RebatesNew rebates = new RebatesNew();
				rebates.setOrderId(visaOrder.getId());
				
				Currency currency = currencyService.findCurrency(Long.parseLong(grouprebatescurrents[i]));
				rebates.setCurrency(currency);
				rebates.setCurrencyId(currency.getId());
				rebates.setCurrencyExchangerate(currency.getCurrencyExchangerate());
//				rebates.setTotalMoney(trvsettlementprices[i]);
				rebates.setCostname(grouprebatesname);
				
				rebates.setRebatesDiff(new BigDecimal(grouprebatesamounts[i]));
				rebates.setRemark(grouprebatesnode);
				rebates.setOrderType(Context.ORDER_TYPE_QZ);
				rebatesList.add(rebates);
				
			}
		}
		
		variables.put("grouprebatesnames", sb_grouprebatesnames.toString());//团队款项名称
		variables.put("grouprebatescurrents", sb_grouprebatescurrents.toString());//团队款项币种
		variables.put("grouprebatesamounts", sb_grouprebatesamounts.toString());//团队款项借款额
		variables.put("grouprebatesnodes", sb_grouprebatesnodes.toString());//团队借款备注
		
		variables.put("grouptotalrebatesnode", grouptotalrebatesnode);//总团队借款备注
		
		//保存游客借款汇率、币种名称、币种符号
		variables.put("grouprebatesexchangerates", sb_grouprebatesexchangerates.toString());//游客借款备注
		variables.put("groupcurrencyNames", sb_groupcurrencyNames.toString());//游客借款币种名称
		variables.put("groupcurrencyMarks", sb_groupcurrencyMarks.toString());//游客借款币种符号
		
		//将借款多币种总和根据汇率转换成人民币
		String count = (sb_trvamounts.toString()+sb_grouprebatesamounts.toString()).replace(VisaBorrowMoneyController.SPLITMARK, ",");
		String currencyId = (sb_rebatestrvcurrents.toString()+sb_grouprebatescurrents.toString()).replace(VisaBorrowMoneyController.SPLITMARK, ",");
		float totalrebates4rmb = visaBorrowMoneyService.currencyConverter(count, currencyId);
		variables.put("rebatesAmount", totalrebates4rmb+"");//按照汇率转为人民币的团队借款总额
		
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
		variables.put("currencyId", rebatestotalcurrencyId+"");//汇总后各个渠道的人民币币种id
		//返佣对象相关的数据
		if(UserUtils.getUser().getCompany().getIsAllowMultiRebateObject()==1){
			if(StringUtils.isNotBlank(input.getSupplyInfo())&&!"请选择".equals(input.getSupplyInfo())){
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT, input.getSupplyInfo());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_NAME, input.getSupplyName());
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_TYPE, "2");//审批相关对象类型(1渠道；2供应商)
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_2, input.getAccountNo());// 账号id
			}else{
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT, agentId);//返佣对象为渠道
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_NAME, agentName);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_TYPE, "1");//审批相关对象类型(1渠道；2供应）
			}
		}
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
		variables.put("totalrebatesamount", totalrebatesamount.substring(0,totalrebatesamount.length()-1));//团队借款总额str.subString(0,str.length()-1)
		
		variables.put(Context.REVIEW_VARIABLE_KEY_REBATES_MARK_TOTAL_MONEY, totalrebatesamount.substring(0,totalrebatesamount.length()-1));//返佣差额
		
		
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
		Long deptId = visaOrderService.getProductPept(orderId);
		
		//验证是否存在互斥流程
		result = this.checkReview(input);
		if(!result.getSuccess()){//存在互斥返回，不进行发起申请
			return result;
		}else{
			result.clearMessage();
		}
		
		if(commonReviewService.checkApplyStart(orderId, Context.ORDER_TYPE_QZ, Context.REBATES_FLOW_TYPE)){
			ReviewResult rr = new ReviewResult();;
			try {
				rr = reviewService.start(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), userReviewPermissionChecker, null,
						Context.ORDER_TYPE_QZ, Context.REBATES_FLOW_TYPE, deptId, null, variables);
				
				if(rr.getCode() == 200) {
					result.setCode(rr.getCode());
					result.setMessage(new StringBuffer(rr.getMessage()));
					return result;
				}
			} catch (Exception e) {
				throw new Exception("调用审批申请接口异常！");
			}
			
			if(rr.getSuccess()){
				//针对鼎鸿假，将返佣字段改为宣传费
		        String uuid = UserUtils.getUser().getCompany().getUuid();
		        if(uuid.equals("049984365af44db592d1cd529f3008c3"))
		          result.putMessage("发起宣传费申请成功！");
		        else
		          result.putMessage("发起签证返佣申请成功！");
			}
			result.setSuccess(rr.getSuccess());
			result.setCode(rr.getCode());
			if(!rr.getSuccess()){
				String message = "调用申请接口异常，错误代码："+rr.getCode()+"!";//返回的提示信息
				if(ReviewErrorCode.ERROR_CODE_MSG_CN.containsKey(rr.getCode())){
					message = ReviewErrorCode.ERROR_CODE_MSG_CN.get(rr.getCode());
				}
				throw new Exception(message);
			}
			
			//保存业务数据 
			ReviewNew review = reviewService.getReview(rr.getReviewId());
			rebatesNewDao.batchSave(rebatesList);
			if(CollectionUtils.isNotEmpty(rebatesList)){
				for(RebatesNew rn:rebatesList){
					rn.setReview(review);
					
					//----------往预算单和结算单插入数据------------
					// 返佣发起申请之后对成本进行同步插入
					costManageService.saveRebatesCostRecordNew(review, rn, visaOrder, yubao_locked, jiesuan_locked);
					//----------------------
				}
			}
			//保存业务数据
			
		}else{
			result.putMessage("结算单已锁定，不能申请返佣数据");
		}
		
		return result;
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
	 * 新 签证返佣申请 审批通过后 同步更新 扩展字段3（当前申请的累计返佣金额，申请列表显示使用）
	 * add by zhanghao 20151230
	 * @param reviewMap 当前申请的业务数据MAP
	 * @param reviewId 当前申请的ID
	 */
	public void updateExtend3ByReviewSuccess(Map<String,Object> reviewMap,String reviewId) {
		
		MoreCurrencyComputePrice moreCur = null;
		//返佣累计，如果不是第一次申请的话，就把之前的值累加起来
		if(!reviewMap.containsKey(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_3)||reviewMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_3)==null){
			return ;
		}
		String totalRebatesJe = reviewMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_3).toString();
		if(!"0".equals(totalRebatesJe)) {
			Map<Object, Object> dataMap = new HashMap<Object, Object>();
			JSONObject jsonObject = JSONObject.fromObject(totalRebatesJe);
			Iterator it = jsonObject.keys();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());  
				String value = (String) jsonObject.get(key);
				dataMap.put(key, value);
			}
			moreCur = new MoreCurrencyComputePrice(dataMap);
		}else{
			moreCur = new MoreCurrencyComputePrice();
		}
		
		//游客返佣
		//金额
		String trvaMounts = reviewMap.get("trvamounts").toString();
		if(null != trvaMounts) {
			//币种
			String rebatestrvcurrents = reviewMap.get("rebatestrvcurrents").toString();
			String[] trvaMountsArr = trvaMounts.split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvaCurrenciesArr = rebatestrvcurrents.split(VisaBorrowMoneyController.SPLITMARK);
			for(int i = 0; i < trvaMountsArr.length; i++) {
				if(StringUtils.isNotBlank(trvaMountsArr[i])) {
					moreCur.addPrice(trvaCurrenciesArr[i], trvaMountsArr[i]);
				}
			}
		}
		
		//团队返佣
		//金额
		String groupRebatesaMounts = reviewMap.get("grouprebatesamounts").toString();
		if(null != groupRebatesaMounts) {
			//币种
			String groupRebatesCurrencies = reviewMap.get("grouprebatescurrents").toString();
			String[] groupMountsArr = groupRebatesaMounts.split(VisaBorrowMoneyController.SPLITMARK);
			String[] groupCurrenciesArr = groupRebatesCurrencies.split(VisaBorrowMoneyController.SPLITMARK);
			for(int i = 0; i < groupMountsArr.length; i++) {
				if(StringUtils.isNotBlank(groupMountsArr[i])) {
					moreCur.addPrice(groupCurrenciesArr[i], groupMountsArr[i]);
				}
			}
		}
		
        JSONObject object = JSONObject.fromObject(moreCur.getPriceMap());
        
        Map<String, String> extendValues = new HashMap<String, String>();
        extendValues.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_3, object.toString().replace("\"", "'"));
        reviewService.updateExtendValues(reviewId, extendValues);
       
	}

}
