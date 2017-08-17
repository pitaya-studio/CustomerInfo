/**
 * 
 */
package com.trekiz.admin.review.changePrice.airticket.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.Arith;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.AirTicketUpPricesService;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.review.changeprice.entity.ChangePriceBean;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.changePrice.airticket.bean.AirticketChangePriceInput;
import com.trekiz.admin.review.changePrice.airticket.bean.ReviewResultBean;
import com.trekiz.admin.review.changePrice.airticket.service.NewAirticketChangePriceService;
import com.trekiz.admin.review.changePrice.common.service.IChangePriceReviewService;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.mutex.ReviewMutexService;


/**
 * 描述:改价
 * 
 * @author zhanghao 2015-12-03 
 */
@Service
@Transactional
public class NewAirticketChangePriceServiceImpl extends BaseService implements NewAirticketChangePriceService {
	@Autowired
	private IChangePriceReviewService changePriceReviewService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewMutexService reviewMutexService;
	@Autowired
	private AirTicketUpPricesService airTicketUpPricesService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	/**
	 * 查询机票改价申请记录
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> findReviewListMap(AirticketChangePriceInput input){
		String orderId = input.getOrderId() ; // 订单编号
		
		AirticketOrder airOrder = airTicketOrderService.getAirticketorderById(StringUtils.toLong(orderId));
		Long airTicketId = airOrder.getAirticketId();
		ActivityAirTicket activityAirTicket = activityAirTicketService.getActivityAirTicketById(airTicketId);
		Long deptId = activityAirTicket.getDeptId();
		
		List<Map<String, Object>> list = reviewService.getReviewDetailMapListByOrderId(deptId, Context.PRODUCT_TYPE_AIRTICKET, Context.REVIEW_FLOWTYPE_CHANGE_PRICE, orderId, null, null);
		
		return list;
	}
	
	/**
	 * 改价申请取消
	 * @param input
	 * @return
	 */
	public ReviewResultBean cancel(AirticketChangePriceInput input){
		 ReviewResult rr = reviewService.cancel(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), null, input.getReviewId(), null, null);
		 ReviewResultBean result = new ReviewResultBean(rr);
		 return result;
	}
	
	/**
	 * 改价申请详情
	 * @param input
	 * @return
	 */
	public Map<String, Object> getReviewDetailMapByReviewId(AirticketChangePriceInput input){
		Map<String, Object> result = reviewService.getReviewDetailMapByReviewId(input.getReviewId());
		return result;
	}
	
	/**
	 * 验证是否存在互斥流程
	 * @param input
	 * @return
	 */
	public ReviewResultBean checkReview(AirticketChangePriceInput input){
		ReviewResultBean reviewResultBean = new ReviewResultBean();
		
		String orderId = input.getOrderId();
		String[] travellerIds = input.getTravelerId();
		
		List<String> travelList = Arrays.asList(travellerIds);
		CommonResult result;
		try {
			result = reviewMutexService.check(orderId, travelList, String.valueOf(Context.PRODUCT_TYPE_AIRTICKET), String.valueOf(Context.REVIEW_FLOWTYPE_CHANGE_PRICE));
			reviewResultBean.setSuccess(result.getSuccess());
			reviewResultBean.putMessage(result.getMessage()+"请重新选择游客");
		} catch (Exception e) {
			reviewResultBean.setSuccess(false);
			reviewResultBean.putMessage("调用流程互斥验证接口异常！");
		}
		return reviewResultBean;
	}
	
	/**
	 * 改价申请业务数据封装 
	 * @param input
	 * @param variables
	 * @param i
	 * @param airOrder
	 * @param activityAirTicket
	 */
	private void putMap4ReviewMap(AirticketChangePriceInput input,Map<String, Object> variables,int i,AirticketOrder airOrder,ActivityAirTicket activityAirTicket){
		if(variables==null)variables = new HashMap<String, Object>();
		
		String orderId = input.getOrderId();// 订单编号
		String[] plusystrue = input.getPlusysTrue();// 游客差价
		String[] travelerids = input.getTravelerids();// 游客编号
		String[] gaijiacurency = input.getGaijiaCurency();// 游客币种
		String[] travelerremark = input.getTravelerremark();// 游客备注
		
		Long airTicketId = airOrder.getAirticketId();
		Long agentId = airOrder.getAgentinfoId();
		String agentName = "";
		if (agentId == -1) {
			agentName = airOrder.getNagentName();
		} else {
			agentName = agentinfoService.findOne(agentId).getAgentName();
		}
		
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, airOrder.getSalerId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, airOrder.getSalerName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, agentId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, agentName);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, Context.PRODUCT_TYPE_AIRTICKET);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,airTicketId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME,activityAirTicket.getActivityName());
		
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, airOrder.getOrderNo());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, airOrder.getGroupCode());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, activityAirTicket.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, airOrder.getCreateBy().getId());
		
		if(ArrayUtils.isNotEmpty(travelerids)&&travelerids.length>i){
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID,travelerids[i]);// 游客id
		}
		
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME,"");	// 游客名称
		
		if(ArrayUtils.isNotEmpty(gaijiacurency)&&gaijiacurency.length>i){
			variables.put(ChangePriceBean.CURRENCY_ID,gaijiacurency[i]); // 游客币种id
		}
		
		String currName = CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(String.valueOf(gaijiacurency[i])), "1");
		variables.put(ChangePriceBean.CURRENCY_NAME,currName); // 币种名称
		variables.put(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
		variables.put(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
		
		if(ArrayUtils.isNotEmpty(plusystrue)&&plusystrue.length>i){
			variables.put(ChangePriceBean.CHANGED_PRICE,plusystrue[i]); // 改价差额
			variables.put("changePrice",plusystrue[i]); // 改价差额 -- 审核流程高改或低改判断使用
		}
		
		if(ArrayUtils.isNotEmpty(travelerremark)&&travelerremark.length>i){
			variables.put(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
		}
		
		variables.put(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
		variables.put(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
		
	}
	
	/**
	 * 机票改价申请发起
	 * @param input
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public ReviewResultBean changePriceProStart(AirticketChangePriceInput input) throws Exception{
		ReviewResultBean result = new ReviewResultBean();
		
		String orderId = input.getOrderId();// 订单编号
		String[] plusystrue = input.getPlusysTrue();// 游客差价
		String[] gaijiacurency = input.getGaijiaCurency();// 游客币种
		
		
		if(loopCheckArrs(plusystrue)){ 
			result.getMessage().append(" 游客差价列表为空！ ");
			return result;
		}
		
		for(int i=0;i<plusystrue.length;i++){
			if(Double.parseDouble(plusystrue[i]) == 0){  // 改差价为默认的 ，就不启动工作流申请改价！
				plusystrue[i] = "0.00";
			}
		}
		
		//edit by majiancheng 2015-12-25,添加改价差额验证
		//如果选中的游客币种对应金额都没改变，则返回
		if (loopCheckArrs(plusystrue)) { 
			result.getMessage().append("改价差额不能为零");
			return result;
		}
		
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = airTicketUpPricesService.queryTravelerList(orderId);
		
		
		//验证是否存在互斥流程
		result = this.checkReview(input);
		if(!result.getSuccess()){//存在互斥返回，不进行发起申请
			return result;
		}else{
			result.clearMessage();
		}
		
		for(int i=0;i<plusystrue.length;i++){
			if(plusystrue[i].equals("0.00")){  // 改差价为默认的 ，就不启动工作流申请改价！
				continue;
			}
			
			
			
			
			// 流程属性数据
			
			Map<String, Object> variables = new HashMap<String, Object>();
			AirticketOrder airOrder = airTicketOrderService.getAirticketorderById(StringUtils.toLong(orderId));
			Long airTicketId = airOrder.getAirticketId();
			ActivityAirTicket activityAirTicket = activityAirTicketService.getActivityAirTicketById(airTicketId);
			
			putMap4ReviewMap(input, variables, i, airOrder, activityAirTicket);
			Long deptId = activityAirTicket.getDeptId();
			// 获取游客的 原始应收价 和 当前应收价 
			for(int j =0;j<travelerList.size();j++){
				Map raw = travelerList.get(j);
				
				if(variables.containsKey(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID)&&variables.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).equals(raw.get("travelerid").toString())){ //  同一个游客的信息
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME,String.valueOf(raw.get("travelername")));
					
					List<Map<String,Object>>  moneys = (List<Map<String,Object>>) raw.get("travelers") ; // 游客币种
					
					// 遍历游客的币种信息
					for(int mi=0;mi<moneys.size();mi++){
						Map temp = moneys.get(mi); // 游客的币种信息
						if(gaijiacurency[i].equals(temp.get("currency_id").toString())){   // 币种确认
							variables.put(ChangePriceBean.OLD_TOTAL_MONEY, String.valueOf(temp.get("oldtotalmoney"))); // 赋值游客的原始应收价
							
							variables.put(ChangePriceBean.CUR_TOTAL_MONEY, String.valueOf(temp.get("amount"))); // 赋值游客的当前应收价
							
							double f = Arith.add(Double.parseDouble(String.valueOf(temp.get("amount"))),Double.parseDouble(plusystrue[i]));// 计算 改后应收价
							variables.put(ChangePriceBean.CHANGED_TOTAL_MONEY,String.valueOf(f)); // 赋值改后应收价
							
							break;
						}else{
							double f = Arith.add(Double.parseDouble("0.00"),Double.parseDouble(plusystrue[i]));// 计算 改后应收价
							variables.put(ChangePriceBean.CHANGED_TOTAL_MONEY,String.valueOf(f));  //赋值 改后应收价
							
							continue;
						}
						
					}
				}
				continue;
			}
			if(commonReviewService.checkApplyStart(orderId, Context.PRODUCT_TYPE_AIRTICKET, Context.REVIEW_FLOWTYPE_CHANGE_PRICE)){
				ReviewResult rr = new ReviewResult();;
				try {
					rr = reviewService.start(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), userReviewPermissionChecker, null,
							Context.PRODUCT_TYPE_AIRTICKET, Context.REVIEW_FLOWTYPE_CHANGE_PRICE, deptId, null, variables);
				} catch (Exception e) {
					throw new Exception("调用审批申请接口异常！");
				}
				if(rr.getMessage()!=null){
					result.getMessage().append(rr.getMessage());
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
				
				if (ReviewConstant.REVIEW_STATUS_PASSED == rr.getReviewStatus()) {//默认审批通过进行业务数据修改
					Map<String, Object> review = reviewService.getReviewDetailMapByReviewId(rr.getReviewId());
					
					Map<String, String> params = new HashMap<String, String>();
					params.put("travelerId", review.get("travellerId") == null ? "": review.get("travellerId").toString());
					params.put("orderId", review.get("orderId") == null ? "": review.get("orderId").toString());
					params.put("orderType", review.get("productType") == null ? "": review.get("productType").toString());
					params.put("currencyId", review.get("currencyid") == null ? "": review.get("currencyid").toString());
					params.put("amount", review.get("changedprice") == null ? "": review.get("changedprice").toString());
					boolean back = changePriceReviewService.doChangePrice(params);
					if (!back) {
						throw new Exception("机票改价默认审批通过后修改业务数据失败！");
					}
				}
				
			}else{
				result.getMessage().append("结算单已锁定，不能申请返佣数据");
				break;
			}
			
		}
		
		return result;
	}
	
	
	/**
	 * 检查数组(验证改差价数组是否为空或者默认值)
	 * @param arrs
	 * @return
	 */
	private boolean loopCheckArrs(String [] arrs){
		if(arrs == null ) return true;
		int count = 0;
		int len = arrs.length;
		for(int i=0;i<len;i++){
			if(arrs[i].equals("0.00")){
				count ++;
			}
		}
		if(count == len){
			return true;
		}
		return false;
	}
	
}
