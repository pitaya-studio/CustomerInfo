package com.trekiz.admin.modules.order.rebates.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.hotel.dao.HotelOrderDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.island.dao.HotelRebatesDao;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.dao.IslandRebatesDao;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.HotelRebates;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandRebates;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.rebates.entity.Rebates;
import com.trekiz.admin.modules.order.rebates.repository.RebatesDao;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;

/**
 * 
 *  文件名: RebatesService.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-12-16 上午11:44:21 
 *  @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class RebatesService extends BaseService {
	@Autowired
	private RebatesDao rebatesDao;
	@Autowired
	private ReviewLogDao reviewLogDao;
	@Autowired
    private AgentinfoDao agentinfoDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
    private OrderCommonService orderService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	@Autowired
	private IslandRebatesDao islandRebatesDao;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private HotelRebatesDao hotelRebatesDao;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private VisaProductsDao visaProductsDao;
	@Autowired
	private HotelOrderDao hotelOrderDao;
	@Autowired
	private IslandOrderDao IslandOrderDao;
	@Autowired
	private RefundService refundService;
	@Autowired
	private PlatBankInfoService bankInfoService;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Transactional
	public String save(List<Rebates> rebatesList){
		for(Rebates rebates : rebatesList){
			//获取改后返佣金额
			if(StringUtils.isNotBlank(rebates.getNewRebates())){
				JSONArray newRebatesArr = JSONArray.fromObject(rebates.getNewRebates());
				List<MoneyAmount> moneyList = Lists.newArrayList();
				MoneyAmount moneyAmount = null;
				String newRebatesSerialNum = UUID.randomUUID().toString();
				for(int i = 0; i < newRebatesArr.size(); i++){
					JSONObject rebatesJSONObj = newRebatesArr.getJSONObject(i);
					Integer currencyId = rebatesJSONObj.getInt("currencyId");
					BigDecimal amount = new BigDecimal(rebatesJSONObj.getString("amount"));
					moneyAmount = new MoneyAmount(newRebatesSerialNum,currencyId,amount,rebates.getTraveler() != null ? rebates.getTraveler().getId() : rebates.getOrderId(), Context.MONEY_TYPE_FY, null, 2, UserUtils.getUser().getId());
					moneyList.add(moneyAmount);
				}
				moneyAmountService.saveMoneyAmounts(moneyList);
				rebates.setNewRebates(newRebatesSerialNum);
			}
			//获取团队返佣原返佣金额 每次获取都是前一次团队返佣的金额合计
			if(rebates.getTraveler() == null){
				List<Object[]> teamList = getTeamRebates(rebates.getOrderId());
				List<MoneyAmount> moneyList = Lists.newArrayList();
				MoneyAmount moneyAmount = null;
				String oldRebatesSerialNum = UUID.randomUUID().toString();
				for(Object[] money: teamList){
					moneyAmount = new MoneyAmount(oldRebatesSerialNum, Integer.parseInt(money[0].toString()), new BigDecimal(money[2].toString()), rebates.getOrderId(), Context.MONEY_TYPE_FY, null, 1, UserUtils.getUser().getId());
					moneyList.add(moneyAmount);
				}
				moneyAmountService.saveMoneyAmounts(moneyList);
				rebates.setOldRebates(oldRebatesSerialNum);
			}
			//获取个人返佣原返佣金额 每次获取都是之前个人返佣的金额合计
			else{ 
				// 计算原返佣金额
				Long orderId = rebates.getOrderId();
				Long travelerId = rebates.getTraveler().getId();
				// 查询到指定订单指定游客的全部历史返佣记录
				List<Rebates> list = rebatesDao.findRebatesByTravelerAndStatus(travelerId,orderId);
				List<MoneyAmount> moneyList = Lists.newArrayList();
				MoneyAmount moneyAmount = null;
				String oldRebatesSerialNum = UUID.randomUUID().toString();
				// 将该游客本次返佣之前的返佣差额全部查出，进行累加，其值便是当前返佣操作的原返佣金额
				for(Rebates rebateTemp : list){
					moneyAmount = new MoneyAmount(oldRebatesSerialNum, Integer.valueOf(rebateTemp.getCurrencyId().toString()), rebateTemp.getRebatesDiff(), rebates.getTravelerId(), Context.MONEY_TYPE_FY, null, 1, UserUtils.getUser().getId());
					moneyList.add(moneyAmount);
				}
				
				//moneyAmountService.saveMoneyAmounts(moneyList);
				// 原返佣值变为累计值
				moneyAmountService.saveNewMoneyAmounts(moneyList);
				rebates.setOldRebates(oldRebatesSerialNum);
			}
			rebates.setCurrency(currencyService.findCurrency(rebates.getCurrencyId()));
			
			//新建审核记录的 deptId 来自产品的部门编号
			ProductOrderCommon productOrder = orderService.getProductorderById(rebates.getOrderId());
			Long ProductId = productOrder.getProductId();
			Long deptId = travelActivityService.findById(ProductId).getDeptId();
			
			Long agentId = productOrder.getOrderCompany();
			String agentName = "";
			if (agentId == -1) {
				agentName = productOrder.getOrderCompanyName();
			} else {
				agentName = agentinfoDao.findOne(agentId).getAgentName();
			}
			
			boolean yubao_locked = false; //预报单是否锁定标识
			boolean jiesuan_locked = false; //结算单是否锁定标识
			ActivityGroup group = activityGroupService.findById(productOrder.getProductGroupId());
			//对预报单状态进行判断
			if ("10".equals(group.getForcastStatus())) {//更改了这里的判断 原先写反了 by chy 2015年6月11日15:22:10
				yubao_locked = true;
			}
			//对结算单状态进行判断
			if (1 == group.getLockStatus()) {
				yubao_locked = true;
				return "结算单已锁定，不能发起申请";
			} 
			
			StringBuffer sbmsg = new StringBuffer();
			Long rid = reviewService.addReview(rebates.getOrderType(), 9, 
					rebates.getOrderId().toString(), 
					rebates.getTraveler() == null? 0L : rebates.getTraveler().getId(), 0L, 
							rebates.getRemark(), sbmsg, new ArrayList<Detail>(),deptId);
			//rid返回值 0:添加失败;返回记录的主键:添加成功
			if(rid == 0){
				return sbmsg.toString();
			}
			costManageService.saveRebatesCostRecord(rebates.getOrderType(), rebates, productOrder, agentName, rid, deptId, yubao_locked, jiesuan_locked);
			Review review = reviewService.findReview(rid, false).get(0);
			rebates.setReview(review);
			rebatesDao.save(rebates);
			
			////////////////////////////////////////////////
			/**
			* add by ruyi.chen
			* add date 2015-05-28
			* describe 过滤低空游轮直接通过审核
			*/

			
		}
		return "success";
	}
	@Transactional
	public String saveIslandRebates(List<IslandRebates> rebatesList) throws Exception{
		for(IslandRebates rebates : rebatesList){
			
			//新建审核记录的 deptId 来自产品的部门编号
			Long deptId = 0l;
			
			IslandOrder islandOrder = islandOrderService.getById(rebates.getOrderId().intValue());
			if(StringUtils.isNotBlank(islandOrder.getActivityIslandUuid())){
				ActivityIsland activityIsland = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
				deptId = activityIsland.getDeptId().longValue();
			}
			StringBuffer sbmsg = new StringBuffer();
			Long rid = reviewService.addSuccessReview(rebates.getOrderType(), 9, 
					rebates.getOrderId().toString(), 
					rebates.getTraveler() == null? 0L : rebates.getTraveler().getId(), 0L, 
							rebates.getRemark(), sbmsg, new ArrayList<Detail>(),deptId);
			//rid返回值 0:添加失败;返回记录的主键:添加成功
			if(rid == 0){
				return sbmsg.toString();
			}
			
			//获取改后返佣金额
			if(StringUtils.isNotBlank(rebates.getNewRebates())){
				JSONArray newRebatesArr = JSONArray.fromObject(rebates.getNewRebates());
				List<IslandMoneyAmount> moneyList = Lists.newArrayList();
				IslandMoneyAmount moneyAmount = null;
				String newRebatesSerialNum = UUID.randomUUID().toString();
				for(int i = 0; i < newRebatesArr.size(); i++){
					JSONObject rebatesJSONObj = newRebatesArr.getJSONObject(i);
					Integer currencyId = rebatesJSONObj.getInt("currencyId");
					BigDecimal amount = new BigDecimal(rebatesJSONObj.getString("amount"));
					moneyAmount = new IslandMoneyAmount();
					moneyAmount.setSerialNum(newRebatesSerialNum);
					moneyAmount.setMoneyType(Context.MONEY_TYPE_FY);
					moneyAmount.setAmount(amount.doubleValue());
					moneyAmount.setBusinessType(2);
					moneyAmount.setCurrencyId(currencyId);
					moneyAmount.setBusinessUuid(rebates.getTraveler() != null ? rebates.getTraveler().getUuid() : islandOrderService.getById(rebates.getOrderId().intValue()).getUuid());
					moneyAmount.setExchangerate(currencyService.findCurrency(currencyId.longValue()).getCurrencyExchangerate().doubleValue());
					moneyAmount.setReviewId(rid.intValue());
					islandMoneyAmountService.save(moneyAmount);
					moneyList.add(moneyAmount);
				}
				islandMoneyAmountService.saveOrUpdateMoneyAmounts(newRebatesSerialNum, moneyList);
				rebates.setNewRebates(newRebatesSerialNum);
			}
			//获取团队返佣原返佣金额 每次获取都是前一次团队返佣的金额合计
			if(rebates.getTraveler() == null){
				List<Object[]> teamList = getIslandTeamRebates(rebates.getOrderId());
				List<IslandMoneyAmount> moneyList = Lists.newArrayList();
				IslandMoneyAmount moneyAmount = null;
				String oldRebatesSerialNum = UUID.randomUUID().toString();
				for(Object[] money: teamList){
					moneyAmount = new IslandMoneyAmount();
					moneyAmount.setSerialNum(oldRebatesSerialNum);
					moneyAmount.setMoneyType(Context.MONEY_TYPE_FY);
					moneyAmount.setAmount(new BigDecimal(money[2].toString()).doubleValue());
					moneyAmount.setBusinessType(2);
					moneyAmount.setCurrencyId(Integer.parseInt(money[0].toString()));
					moneyAmount.setBusinessUuid(rebates.getTraveler() != null ? rebates.getTraveler().getUuid() : islandOrderService.getById(rebates.getOrderId().intValue()).getUuid());
					moneyAmount.setExchangerate(currencyService.findCurrency(Long.parseLong(money[0].toString())).getCurrencyExchangerate().doubleValue());
					moneyAmount.setReviewId(rid.intValue());
					moneyList.add(moneyAmount);
				}
				islandMoneyAmountService.saveOrUpdateMoneyAmounts(oldRebatesSerialNum, moneyList);
				rebates.setOldRebates(oldRebatesSerialNum);
			}
			rebates.setCurrency(currencyService.findCurrency(rebates.getCurrencyId()));
			
			
			
			Long agentId = islandOrder.getOrderCompany().longValue();
			String agentName = "";
			if (agentId == -1) {
				agentName = islandOrder.getOrderCompanyName();
			} else {
//				agentName = agentinfoDao.findOne(agentId).getAgentName();
				agentName = islandOrder.getOrderCompanyName();
			}
			
			boolean yubao_locked = false; //预报单是否锁定标识
			boolean jiesuan_locked = false; //结算单是否锁定标识
			
			if(StringUtils.isNotBlank(islandOrder.getActivityIslandGroupUuid())){
				ActivityIslandGroup group = activityIslandGroupService.getByUuid(islandOrder.getActivityIslandGroupUuid());
				//对预报单状态进行判断
				if ("10".equals(group.getForcastStatus())) {//更改了这里的判断 原先写反了 by chy 2015年6月11日15:22:10
					yubao_locked = true;
				}
				//对结算单状态进行判断
				if (1 == group.getLockStatus()) {
					yubao_locked = true;
					return "结算单已锁定，不能发起申请";
				} 
			};
			
			
			
			costManageService.saveRebatesCostRecord(rebates.getOrderType(), rebates, islandOrder, agentName, rid, deptId, yubao_locked, jiesuan_locked);
			Review review = reviewService.findReview(rid, false).get(0);
			rebates.setReview(review);
			islandRebatesDao.saveObj(rebates);
			
			////////////////////////////////////////////////
			/**
			* add by ruyi.chen
			* add date 2015-05-28
			* describe 过滤海岛游返佣审核直接通过
			*/
			if(Context.ORDER_TYPE_ISLAND.toString().equals(review.getProductType().toString())){
//				reviewService.UpdateReview(rid, review.getTopLevel(), 1, "");
			}
			
		}
		return "success";
	}
	
	@Transactional
	public String saveHotelRebates(List<HotelRebates> rebatesList) throws Exception{
		for(HotelRebates rebates : rebatesList){
			
			//新建审核记录的 deptId 来自产品的部门编号
			Long deptId = 0l;
			
			HotelOrder hotelOrder = hotelOrderService.getById(rebates.getOrderId().intValue());
			if(StringUtils.isNotBlank(hotelOrder.getActivityHotelUuid())){
				ActivityHotel activityHotel = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
				deptId = activityHotel.getDeptId().longValue();
			}
			StringBuffer sbmsg = new StringBuffer();
			Long rid = reviewService.addSuccessReview(rebates.getOrderType(), 9, 
					rebates.getOrderId().toString(), 
					rebates.getTraveler() == null? 0L : rebates.getTraveler().getId(), 0L, 
							rebates.getRemark(), sbmsg, new ArrayList<Detail>(),deptId);
			//rid返回值 0:添加失败;返回记录的主键:添加成功
			if(rid == 0){
				return sbmsg.toString();
			}
			
			//获取改后返佣金额
			if(StringUtils.isNotBlank(rebates.getNewRebates())){
				JSONArray newRebatesArr = JSONArray.fromObject(rebates.getNewRebates());
				List<HotelMoneyAmount> moneyList = Lists.newArrayList();
				HotelMoneyAmount moneyAmount = null;
				String newRebatesSerialNum = UUID.randomUUID().toString();
				for(int i = 0; i < newRebatesArr.size(); i++){
					JSONObject rebatesJSONObj = newRebatesArr.getJSONObject(i);
					Integer currencyId = rebatesJSONObj.getInt("currencyId");
					BigDecimal amount = new BigDecimal(rebatesJSONObj.getString("amount"));
					moneyAmount = new HotelMoneyAmount();
					moneyAmount.setSerialNum(newRebatesSerialNum);
					moneyAmount.setMoneyType(Context.MONEY_TYPE_FY);
					moneyAmount.setAmount(amount.doubleValue());
					moneyAmount.setBusinessType(2);
					moneyAmount.setCurrencyId(currencyId);
					moneyAmount.setBusinessUuid(rebates.getTraveler() != null ? rebates.getTraveler().getUuid() : islandOrderService.getById(rebates.getOrderId().intValue()).getUuid());
					moneyAmount.setExchangerate(currencyService.findCurrency(currencyId.longValue()).getCurrencyExchangerate().doubleValue());
					moneyAmount.setReviewId(rid.intValue());
					hotelMoneyAmountService.save(moneyAmount);
					moneyList.add(moneyAmount);
				}
				hotelMoneyAmountService.saveOrUpdateMoneyAmounts(newRebatesSerialNum, moneyList);
				rebates.setNewRebates(newRebatesSerialNum);
			}
			//获取团队返佣原返佣金额 每次获取都是前一次团队返佣的金额合计
			if(rebates.getTraveler() == null){
				List<Object[]> teamList = getIslandTeamRebates(rebates.getOrderId());
				List<HotelMoneyAmount> moneyList = Lists.newArrayList();
				HotelMoneyAmount moneyAmount = null;
				String oldRebatesSerialNum = UUID.randomUUID().toString();
				for(Object[] money: teamList){
					moneyAmount = new HotelMoneyAmount();
					moneyAmount.setSerialNum(oldRebatesSerialNum);
					moneyAmount.setMoneyType(Context.MONEY_TYPE_FY);
					moneyAmount.setAmount(new BigDecimal(money[2].toString()).doubleValue());
					moneyAmount.setBusinessType(2);
					moneyAmount.setCurrencyId(Integer.parseInt(money[0].toString()));
					moneyAmount.setBusinessUuid(rebates.getTraveler() != null ? rebates.getTraveler().getUuid() : islandOrderService.getById(rebates.getOrderId().intValue()).getUuid());
					moneyAmount.setExchangerate(currencyService.findCurrency(Long.parseLong(money[0].toString())).getCurrencyExchangerate().doubleValue());
					moneyAmount.setReviewId(rid.intValue());
					moneyList.add(moneyAmount);
				}
				hotelMoneyAmountService.saveOrUpdateMoneyAmounts(oldRebatesSerialNum, moneyList);
				rebates.setOldRebates(oldRebatesSerialNum);
			}
			rebates.setCurrency(currencyService.findCurrency(rebates.getCurrencyId()));
			
			
			
			Long agentId = hotelOrder.getOrderCompany().longValue();
			String agentName = "";
			if (agentId == -1) {
				agentName = hotelOrder.getOrderCompanyName();
			} else {
				agentName = agentinfoDao.findOne(agentId).getAgentName();
			}
			
			boolean yubao_locked = false; //预报单是否锁定标识
			boolean jiesuan_locked = false; //结算单是否锁定标识
			
			if(StringUtils.isNotBlank(hotelOrder.getActivityHotelGroupUuid())){
				ActivityHotelGroup group = activityHotelGroupService.getByUuid(hotelOrder.getActivityHotelGroupUuid());
				//对预报单状态进行判断
				if ("10".equals(group.getForcastStatus())) {//更改了这里的判断 原先写反了 by chy 2015年6月11日15:22:10
					yubao_locked = true;
				}
				//对结算单状态进行判断
				if (1 == group.getLockStatus()) {
					yubao_locked = true;
					return "结算单已锁定，不能发起申请";
				} 
			};
			
			
			
			costManageService.saveHotelRebatesCostRecord(rebates.getOrderType(), rebates, hotelOrder, agentName, rid, deptId, yubao_locked, jiesuan_locked);
			Review review = reviewService.findReview(rid, false).get(0);
			rebates.setReview(review);
			hotelRebatesDao.saveObj(rebates);
			
			////////////////////////////////////////////////
			/**
			* add by ruyi.chen
			* add date 2015-05-28
			* describe 过滤酒店返佣审核直接通过
			*/
			if(Context.ORDER_TYPE_HOTEL.toString().equals(review.getProductType().toString())){
//				reviewService.UpdateReview(rid, review.getTopLevel(), 1, "");
			}
			
		}
		return "success";
	}
	
	/**
	 * 返回最后通过的累计总金额
	 * @param orderId
	 * @return
	 */
	public List<String> getNotStatus2AllCumulative(String orderId){
		String sql = "select order_rebates.all_cumulative from review as review,order_rebates as order_rebates  where review.STATUS=2 and review.productType = 7  and review.flowType = 9 and review.id= order_rebates.rid  AND review.orderId = order_rebates.orderId AND order_rebates.orderId =? limit 1";
		
		return rebatesDao.findBySql(sql,orderId);
	} 
	

	/**
	 * 返佣审核通过操作
	 */
	@Transient
	public void reviewSuccess(Map<String,String> reviewMap) {
		
		//now_cumulative 
		//all_cumulative
		
		//获取订单最新已通过流程的总差额
		List<Object[]> newtotalList = getnewTeamRebatesList(Long.parseLong(reviewMap.get("orderId").toString()));
		List<Object[]> oldtotalList = getoldTeamRebatesList(Long.parseLong(reviewMap.get("orderId").toString()));
		List<Object[]> onetotalList = getoneTeamRebatesList(Long.parseLong(reviewMap.get("orderId").toString()));
		
		String newtotal = "";
		String oldtotal = "";
		
		if(oldtotalList!= null && oldtotalList.size()>0 && null!=oldtotalList.get(0)[1]){
			for(Object[] rebates : oldtotalList){
				if(("").equals(rebates[1].toString()) ){
					for(Object[] o1 : onetotalList){
						oldtotal = o1[0].toString();
					}
				}else{
					//oldtotal = rebates[1].toString();
					oldtotal = rebates[1].toString();
				}
			}
		}
		
		for (Object[] rebates : newtotalList ) {
			
			if(!("").equals(oldtotal)){
				
				newtotal = oldtotal + "+" + rebates[1].toString();
			
			}else{
				
				newtotal =  rebates[1].toString();
				
			}
			
		}
		
		List<Rebates> rebatesList =  rebatesDao.findOneByRidList(Long.parseLong(reviewMap.get("id")));
	
		for (Rebates rebates : rebatesList) {
			//更新所有订单累计的总金额
			String sql = "update   order_rebates   set all_cumulative = ? where orderId = ? and order_type = 7";
			rebatesDao.updateBySql(sql, newtotal,rebates.getOrderId());
			
		}
		
	}
	
	public Rebates findRebatesById(Long id){
		return rebatesDao.findOne(id);
	}
	
	private Rebates findRebatesByRid(Long id){
		return rebatesDao.findOneByRid(id);
	}
	
	public List<Rebates> findRebatesListByRid(Long id){
		return rebatesDao.findListByRid(id);
	}
	
	public List<Rebates> findRebatesList(Long orderId, int orderType){
		return rebatesDao.findRebatesList(orderId, orderType);
	}
	public List<Rebates> findRebatesListAir(Long orderId, int orderType){
		return rebatesDao.findRebatesListAir(orderId, orderType);
	}
	
	public List<Rebates> findRebatesListByStatus(Integer productType, Integer flowType, String orderId){
		return rebatesDao.findRebatesListByStatus(productType, flowType, orderId);
	}
	
	public List<?> findVisaRebatesListByStatus(Integer productType, Integer flowType, String orderId)
	{
		StringBuffer sbf = new StringBuffer();
		sbf.append("select id from review where productType = ? and flowType=? and orderId=? and active=1 and status = 1");
		return rebatesDao.findBySql(sbf.toString(), Map.class, productType,flowType,orderId);
	}
	
	public void reviewRebates(long rid, Integer result, String denyReason, Integer userLevel){
		reviewService.UpdateReview(rid, userLevel, result, denyReason);
	}
	
	public List<ReviewLog> validReviewRebates(long rid, Integer userLevel){
		return reviewLogDao.findReviewLogByNowLevel(rid, userLevel);
	}
	
	public List<Rebates> findRebatesByTravelerAndStatus(Long travelerId){
		return rebatesDao.findRebatesByTravelerAndStatus(travelerId);
	}
	
	/**
	 * 根据订单获取原团队返佣费用
	 * @param orderId 订单主键
	 * @return 
	 */
	public List<Object[]> getTeamRebates(Long orderId){
		String sql = "SELECT m.currencyId, c.currency_name, sum(m.amount), c.currency_mark " +
					 "FROM money_amount m,currency c,(" +
					 	"SELECT a.new_rebates AS serialNum " +
					 	"FROM order_rebates a " +
					 	"LEFT JOIN review b " +
					 	"ON a.rid = b.id " +
					 	"WHERE a.orderId = " + orderId + " AND b.status = 2 AND a.delFlag="+Context.DEL_FLAG_NORMAL+")tmp " +
					 "WHERE m.currencyId = c.currency_id AND m.serialNum = tmp.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
		return rebatesDao.findBySql(sql);
	}
	/**
	 * 根据订单获取原团队返佣费用
	 * @param orderId 订单主键
	 * @return 
	 */
	public List<Object[]> getIslandTeamRebates(Long orderId){
		String sql = "SELECT m.currencyId, c.currency_name, sum(m.amount), c.currency_mark " +
				 "FROM island_money_amount m,currency c,(" +
				 	"SELECT a.new_rebates AS serialNum " +
				 	"FROM order_rebates a " +
				 	"LEFT JOIN review b " +
				 	"ON a.rid = b.id " +
				 	"WHERE a.orderId = " + orderId + " AND a.travelerId is null  AND b.status = 2 AND a.delFlag="+Context.DEL_FLAG_TEMP+")tmp " +
				 "WHERE m.currencyId = c.currency_id AND m.serialNum = tmp.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
	return rebatesDao.findBySql(sql);
	}
	
	/**
	 * 机票返佣获取返佣费用
	 * @param orderId
	 * @return
	 */
	public List<Rebates> getRebates(Long orderId){
//		String sql = "SELECT o.currencyId as currencyId,o.old_rebates as old_rebates,o.rebatesDiff as rebatesDiff,o.new_rebates as new_rebates,o.travelerId as travelerId  FROM order_rebates o,airticket_order a WHERE  " +
//				" a.id = o.orderId AND a.id = ? AND o.travelerId in " +
//				" (select t.travelerId from (select max(o.id),o.travelerId from order_rebates o,review r " +
//				" where o.rid = r.id and r.`status` = 2 AND r.flowType = 9 AND r.productType = 7 GROUP BY o.travelerId) t) ";
		
		String sql = "SELECT	w.currencyId AS currencyId, 	w.old_rebates AS old_rebates, 	w.rebatesDiff AS rebatesDiff, 	w.new_rebates AS new_rebates, 	w.travelerId AS travelerId FROM 	( 		SELECT 			o.currencyId AS currencyId, 			o.old_rebates AS old_rebates, 			o.rebatesDiff AS rebatesDiff, 			o.new_rebates AS new_rebates, 			o.travelerId AS travelerId 		FROM 			order_rebates o 				LEFT JOIN review r ON o.rid = r.id 				WHERE 			o.orderId = ?                         AND r.status = 2                         AND r.flowType = 9                         AND r.productType = 7 	AND o.delFlag="+Context.DEL_FLAG_TEMP+"	 ORDER BY 			o.id DESC 	) w GROUP BY 	w.travelerId";
		
		List<Map<String,Object>> mapList = rebatesDao.findBySql(sql,Map.class,orderId);
		List<Rebates> result = new ArrayList<Rebates>();
		for(Map<String,Object> map:mapList){
			Rebates rb = new Rebates();
			rb.setCurrencyId(Long.parseLong(map.get("currencyId").toString()));
			rb.setOldRebates(map.get("old_rebates").toString());
			rb.setRebatesDiff(new BigDecimal(map.get("rebatesDiff").toString()));
			rb.setNewRebates(map.get("new_rebates").toString());
			if(map.get("travelerId")==null){
				rb.setTravelerId(0l);
			}else{
				rb.setTravelerId(Long.parseLong(map.get("travelerId").toString()));
			}
			
			result.add(rb);
		}
		return result;
	} 
	
	
	/**
	 * 机票返佣根据订单获取最新已通过流程的总差额
	 * @param orderId
	 * @return
	 */
	private List<Object[]> getnewTeamRebatesList(Long orderId){
//		String sql = "select order_rebates.id,order_rebates.now_cumulative,order_rebates.all_cumulative from order_rebates as order_rebates where rid in (select max(r.id) from order_rebates as o,  review as r  where o.orderId = r.orderId  and r.`status`=2 and o.orderId = ? and o.delFlag="+Context.DEL_FLAG_TEMP+") and order_rebates.delFlag="+Context.DEL_FLAG_TEMP;
		String sql = "select order_rebates.id,order_rebates.now_cumulative,order_rebates.all_cumulative from order_rebates as order_rebates where rid in (select max(r.id) from order_rebates as o,  review as r  where o.orderId = r.orderId  and r.`status`=2 and o.orderId = ? )";
		return rebatesDao.findBySql(sql,orderId);
	} 
	
	/**
	 * 机票返佣根据订单获取最新流程的总差额
	 * @param orderId
	 * @return
	 */
	private List<Object[]> getoldTeamRebatesList(Long orderId){
//		String sql = "SELECT order_rebates.now_cumulative, 	order_rebates.all_cumulative,review.id FROM 	order_rebates AS order_rebates ,review as review WHERE review.id = order_rebates.rid and 	rid IN ( 		SELECT 			r.id 		FROM 			order_rebates AS o, 			review AS r 		WHERE 			o.orderId = r.orderId 		AND r.`status` = 2 		AND o.orderId = ? 	AND o.delFlag="+Context.DEL_FLAG_TEMP+") order_rebates.delFlag="+Context.DEL_FLAG_TEMP+" GROUP BY review.id  ORDER BY review.id DESC limit 1,1 ";
		String sql = "SELECT order_rebates.now_cumulative, 	order_rebates.all_cumulative,review.id FROM 	order_rebates AS order_rebates ,review as review WHERE review.id = order_rebates.rid and 	rid IN ( 		SELECT 			r.id 		FROM 			order_rebates AS o, 			review AS r 		WHERE 			o.orderId = r.orderId 		AND r.status = 2 		AND o.orderId = ? 	)  GROUP BY review.id  ORDER BY review.id DESC limit 1,1 ";
		return rebatesDao.findBySql(sql,orderId);
	} 
	
	
	/**
	 * 机票返佣根据订单获取最新流程的总差额
	 * @param orderId
	 * @return
	 */
	private List<Object[]> getoneTeamRebatesList(Long orderId){
//		String sql  ="SELECT 	order_rebates.now_cumulative, 	order_rebates.all_cumulative, 	review.id FROM 	order_rebates AS order_rebates, 	review AS review WHERE 	review.id = order_rebates.rid AND rid IN ( 	SELECT 		r.id 	FROM 		order_rebates AS o, 		review AS r 	WHERE 		o.orderId = r.orderId 	AND r.`status` = 2 	AND o.orderId = ? o.delFlag="+Context.DEL_FLAG_TEMP+" ORDER BY r.id DESC ) order_rebates.delFlag="+Context.DEL_FLAG_TEMP+" GROUP BY 	review.id limit 1";
		String sql  ="SELECT 	order_rebates.now_cumulative, 	order_rebates.all_cumulative, 	review.id FROM 	order_rebates AS order_rebates, 	review AS review WHERE 	review.id = order_rebates.rid AND rid IN ( 	SELECT 		r.id 	FROM 		order_rebates AS o, 		review AS r 	WHERE 		o.orderId = r.orderId 	AND r.`status` = 2 	AND o.orderId = ?  ORDER BY r.id DESC ) GROUP BY 	review.id limit 1";
		return rebatesDao.findBySql(sql,orderId);
	} 
	
	/**
	 * 封装打印数据
	 * @param reviewId
	 * @return
	 */
	public Map<String,Object> buildPrintData(String reviewId){
		Map<String,Object> map = new HashMap<String,Object>();
		
		//返佣申请审核相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(reviewId));
		
		if (reviewAndDetailInfoMap != null) {
			map.put("createDate", DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));	//填写日期
			String printTime = reviewAndDetailInfoMap.get("printTime");
			if(!StringUtils.isEmpty(printTime)  ){
				map.put("firstPrintTime",  DateUtils.dateFormat(reviewAndDetailInfoMap.get("printTime")));	//首次打印时间
			}else{
				map.put("firstPrintTime",  new Date());	//首次打印时间
			}
			
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			if (null != user) {
				map.put("operatorName", user.getName());	//经办人(返佣申请人)
				map.put("payee", user.getName());	//领款人(返佣申请人)
			} else {
				map.put("operatorName", "");//经办人(返佣申请人)
				map.put("payee", "");	//领款人(返佣申请人)
			}

			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			String payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (!Context.SUPPLIER_UUID_HQX.equals(companyUuid) && !Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid) && "1".equals(payStatus)) {
				Date confirmPayDate = DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"));
				map.put("confirmPayDate", DateUtils.formatDate(confirmPayDate, "yyyy 年  M 月 dd 日"));
			} else {
				map.put("confirmPayDate", "   年   月   日");
			}
		}
		
		//返佣申请相关信息
		Rebates rebates = this.findRebatesByRid(Long.parseLong(reviewId));
		if(rebates != null ){
			handleRemark(reviewId, rebates, map);			
			String currencyName = rebates.getCurrency().getCurrencyName().trim();	//币种名称			
			List<MoneyAmount> moneyList = moneyAmountService.findAmountBySerialNum(rebates.getNewRebates());
			MoneyAmount money = new MoneyAmount();
			if(moneyList != null && moneyList.size() > 0){
				for(MoneyAmount moneyAmount : moneyList){
					if(rebates.getCurrencyId().longValue() == moneyAmount.getCurrencyId().longValue() ){
						money = moneyAmount;
						break;
					}
				}
			}
			
			BigDecimal newRebates = new BigDecimal(0);
			newRebates = money.getAmount() == null?newRebates:money.getAmount();	//金额
			BigDecimal  totalRMBMoney = new BigDecimal(0);	//合计人民币金额
			
			//如果是非人民币
			if(!currencyName.contains("人民币")){
				BigDecimal currencyExchangerate = rebates.getRate();		
				//转换成人民币金额 = 金额 * 汇率
				BigDecimal RMBMoney = newRebates.multiply(currencyExchangerate);				
				totalRMBMoney = RMBMoney;				
				map.put("currencyExchangerate", MoneyNumberFormat.fmtMicrometer(currencyExchangerate.toString(), "#,##0.0000"));//汇率
			}else{
				totalRMBMoney = newRebates;
				map.put("currencyExchangerate", "1.0000");//汇率
			}
			
			map.put("currencyName", currencyName);	//币种名称
			map.put("money", MoneyNumberFormat.getThousandsMoney(newRebates.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));	//币种金额
			map.put("totalRMBMoney", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO)); //合计人民币金额
			
			int result = totalRMBMoney.compareTo(new BigDecimal(0));
			if(result == 1 || result == 0 ){
				map.put("totalRMBMoneyName", StringNumFormat.changeAmount(totalRMBMoney.toString())); //合计人民币金额大写
			}else{
				map.put("totalRMBMoneyName", "红字" + StringNumFormat.changeAmount(totalRMBMoney.toString().replaceAll("-",""))); //合计人民币金额大写
			}			
		}else{
			map.put("currencyName", "");	//币种名称
			map.put("money", "");	//币种金额
			map.put("currencyExchangerate", "");//汇率
			map.put("totalRMBMoney", ""); //合计人民币金额
			map.put("remark",  "");//备注
			map.put("totalRMBMoneyName", ""); //合计人民币金额大写
		}
		map.put("costname",  "报销");//款项
				
		if( rebates != null &&  rebates.getOrderId() != null){
			long orderId = rebates.getOrderId();
			
			//这里需根据订单类型处理 相应订单的产品名称
			//wxw added ---------2015-07-24----------------
			int ordertype = rebates.getOrderType();
			String orderCompanyName = "";
			if (11==ordertype) {//酒店
				HotelOrder hotelOrder = hotelOrderDao.getById(new Long(orderId).intValue());
				ActivityHotel activityHotel = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
				orderCompanyName = activityHotel.getActivityName();
			}else if (12==ordertype) {//海岛游
				IslandOrder islandOrder = IslandOrderDao.getById(new Long(orderId).intValue());
				ActivityIsland activityIsland = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
				orderCompanyName = activityIsland.getActivityName();
			}else if (6==ordertype) {
				VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
				VisaProducts visaProducts = visaProductsDao.findOne(visaOrder.getVisaProductId());
				orderCompanyName = visaProducts.getProductName();
			}else {
				ProductOrderCommon productOrderCommon = orderService.getProductorderById(orderId);
				orderCompanyName = productOrderCommon.getOrderCompanyName();
			}
			
			map.put("orderCompanyName", orderCompanyName);	//渠道名称
		}else{
			map.put("orderCompanyName", "");
		}
		map.put("accountName", "");
		
		List<ReviewLog> reviewLogs = reviewService.findReviewLog(Long.parseLong(reviewId));		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳  7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REBATES_FLOW_TYPE,reviewLogs);		
		map.put("deptmanager", jobtypeusernameMap.get(4) == null ?"":jobtypeusernameMap.get(4));	//主管审批
		map.put("cashier", jobtypeusernameMap.get(6) == null ? "" : jobtypeusernameMap.get(6));	//出纳
		map.put("finance", jobtypeusernameMap.get(8) == null ? "" :jobtypeusernameMap.get(8));		//财务
		map.put("financeManage", jobtypeusernameMap.get(9) == null ? "" : jobtypeusernameMap.get(9));	//财务主管
		map.put("majorCheckPerson", jobtypeusernameMap.get(10) == null ? "" : jobtypeusernameMap.get(10));	//总经理
		//如果财务主管不为空，审核取财务主管值；如果财务主管位空，则审核取财务值
		if(null != jobtypeusernameMap.get(9)){
			map.put("auditor", jobtypeusernameMap.get(9) == null ? "" : jobtypeusernameMap.get(9));	//审核
		}else {
			map.put("auditor", jobtypeusernameMap.get(8) == null ? "" : jobtypeusernameMap.get(8));	//审核
		}
		
		//环球行用户将团号改为订单团号
		if (Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			map.put("groupCodeName", "订单团号");
		} else {
			map.put("groupCodeName", "团号");
		}
				
		return map;
	}
	
	/**
	 * 封装打印数据
	 * @param reviewId
	 * @param payId
	 * @return
	 */
	public Map<String,Object> buildPrintData(String reviewId,String payId){
		Map<String,Object> map = new HashMap<String,Object>();
		
		//返佣申请审核相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(reviewId));
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if (reviewAndDetailInfoMap != null) {
			map.put("createDate", DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));	//填写日期
			String printTime = reviewAndDetailInfoMap.get("printTime");
			if(!StringUtils.isEmpty(printTime)  ){
				map.put("firstPrintTime",  DateUtils.dateFormat(reviewAndDetailInfoMap.get("printTime")));	//首次打印时间
			}else{
				map.put("firstPrintTime",  new Date());	//首次打印时间
			}
			
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			if (null != user) {
				map.put("operatorName", user.getName());	//经办人(返佣申请人)
				map.put("payee", user.getName());	//领款人(返佣申请人)
			} else {
				map.put("operatorName", "");//经办人(返佣申请人)
				map.put("payee", "");	//领款人(返佣申请人)
			}
			
			String payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (!Context.SUPPLIER_UUID_HQX.equals(companyUuid) && !Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid) && "1".equals(payStatus)) {
				Date confirmPayDate = DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"));
				map.put("confirmPayDate", DateUtils.formatDate(confirmPayDate, "yyyy 年  MM 月 dd 日"));
			} else {
				map.put("confirmPayDate", "   年   月   日");
			}
		}
		
		//返佣申请相关信息
		BigDecimal currencyExchangerate = new BigDecimal(1);
		Rebates rebates = this.findRebatesByRid(Long.parseLong(reviewId));
		if(rebates != null ){			
			handleRemark(reviewId, rebates, map);			
			String currencyName = rebates.getCurrency().getCurrencyName().trim();	//币种名称			
			
			//如果是非人民币
			if(!currencyName.contains("人民币")){
				currencyExchangerate = rebates.getRate();				
				map.put("currencyExchangerate", MoneyNumberFormat.fmtMicrometer(currencyExchangerate.toString(), "#,##0,0000"));//汇率
			}else{
				map.put("currencyExchangerate", "1.0000");//汇率
			}
			
			map.put("currencyName", currencyName);	//币种名称		
		}else{
			map.put("currencyName", "");	//币种名称
			map.put("currencyExchangerate", "");//汇率
			map.put("remark",  "");//备注
		}
		
		//45需求，凭单中的金额以每次支付的金额为准 ---单币种---
		List<Object[]> moneys = null;
		PayInfoDetail payDetail = null;
		if (StringUtils.isNotBlank(payId)) {
			payDetail = refundService.getPayInfoByPayId(payId, rebates.getOrderType().toString());
			if (payDetail != null && StringUtils.isNotBlank(payDetail.getMoneyDispStyle())) {
				moneys = MoneyNumberFormat.getMoneyFromString(payDetail.getMoneyDispStyle(), "\\+");
				Integer payType = payDetail.getPayType();
				if(Context.SUPPLIER_UUID_YJXZ.equals(companyUuid) && (1 == payType || 3 == payType)){
					map.put("payee", payDetail.getPayerName());
					map.put("isYJXZ","YJXZ");
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(moneys)) {
			BigDecimal totalMoney = new BigDecimal(Double.valueOf(moneys.get(0)[1].toString()));	
			map.put("money", MoneyNumberFormat.getThousandsMoney(totalMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));
			
			BigDecimal totalRMBMoney = totalMoney.multiply(currencyExchangerate);
			map.put("totalRMBMoney", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO)); //合计人民币金额			
			int result = totalRMBMoney.compareTo(new BigDecimal(0));
			if(result == 1 || result == 0 ){
				map.put("totalRMBMoneyName", StringNumFormat.changeAmount(totalRMBMoney.toString())); //合计人民币金额大写
			}else{
				map.put("totalRMBMoneyName", "红字" + StringNumFormat.changeAmount(totalRMBMoney.toString().replaceAll("-",""))); //合计人民币金额大写
			}	
		}else {
			map.put("money","");	//人民币金额
			map.put("totalRMBMoney", "");
			map.put("totalRMBMoneyName", "");
		}
				
		map.put("costname",  "报销");//款项
				
		if( rebates != null &&  rebates.getOrderId() != null){
			long orderId = rebates.getOrderId();			
			//这里需根据订单类型处理 相应订单的产品名称
			//wxw added ---------2015-07-24----------------
			int ordertype = rebates.getOrderType();
			String orderCompanyName = "";
			if (11==ordertype) {//酒店
				HotelOrder hotelOrder = hotelOrderDao.getById(new Long(orderId).intValue());
				ActivityHotel activityHotel = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
				orderCompanyName = activityHotel.getActivityName();
			}else if (12==ordertype) {//海岛游
				IslandOrder islandOrder = IslandOrderDao.getById(new Long(orderId).intValue());
				ActivityIsland activityIsland = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
				orderCompanyName = activityIsland.getActivityName();
			}else if (6==ordertype) {
				VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
				VisaProducts visaProducts = visaProductsDao.findOne(visaOrder.getVisaProductId());
				orderCompanyName = visaProducts.getProductName();
			}else {
				ProductOrderCommon productOrderCommon = orderService.getProductorderById(orderId);
				orderCompanyName = productOrderCommon.getOrderCompanyName();
			}			
			map.put("orderCompanyName", orderCompanyName);	//渠道名称
		}else{
			map.put("orderCompanyName", "");
		}
		
		List<ReviewLog> reviewLogs = reviewService.findReviewLog(Long.parseLong(reviewId));		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳  7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REBATES_FLOW_TYPE,reviewLogs);		
		map.put("deptmanager", jobtypeusernameMap.get(4) == null ?"":jobtypeusernameMap.get(4));	//主管审批
		map.put("cashier", jobtypeusernameMap.get(6) == null ? "" : jobtypeusernameMap.get(6));	//出纳
		map.put("finance", jobtypeusernameMap.get(8) == null ? "" :jobtypeusernameMap.get(8));		//财务
		map.put("financeManage", jobtypeusernameMap.get(9) == null ? "" : jobtypeusernameMap.get(9));	//财务主管
		map.put("majorCheckPerson", jobtypeusernameMap.get(10) == null ? "" : jobtypeusernameMap.get(10));	//总经理
		//如果财务主管不为空，审核取财务主管值；如果财务主管位空，则审核取财务值
		if(null != jobtypeusernameMap.get(9)){
			map.put("auditor", jobtypeusernameMap.get(9) == null ? "" : jobtypeusernameMap.get(9));	//审核
		}else {
			map.put("auditor", jobtypeusernameMap.get(8) == null ? "" : jobtypeusernameMap.get(8));	//审核
		}
		
		//环球行用户将团号改为订单团号
		if (Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			map.put("groupCodeName", "订单团号");
		} else {
			map.put("groupCodeName", "团号");
		}
		
		return map;
	}
		
	public File createRebatesSheetDownloadFile(Map<String,Object> map) throws Exception {		
		return FreeMarkerUtil.generateFile("rebatesPay.ftl", "rebatesPay.doc", map);
	}
		
	/**
	 * 机票返佣打印单
	 * add by wangxu
	 * @param reviewId
	 * @param rebatesId
	 * @return
	 */
	public Map<String,Object> airticketRebatesPrintData(String reviewId, String payId){
		Map<String,Object> map = new HashMap<String,Object>();
		
		//返佣申请审核相关信息review表
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(reviewId));
		map.put("createDate", DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));	//填写日期
		String printTime = reviewAndDetailInfoMap.get("printTime");
		if(!StringUtils.isEmpty(printTime)  ){
			map.put("firstPrintTime",  DateUtils.dateFormat(reviewAndDetailInfoMap.get("printTime")));	//首次打印时间
		}else{
			map.put("firstPrintTime",  new Date());	//首次打印时间
		}
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		String payStatus = reviewAndDetailInfoMap.get("payStatus");
		if(!Context.SUPPLIER_UUID_HQX.equals(companyUuid) && !Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid) && "1".equals(payStatus)) {
			Date confirmPayDate = DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"));
			map.put("confirmPayDate", DateUtils.formatDate(confirmPayDate, "yyyy 年  MM 月 dd 日"));
		} else {
			map.put("confirmPayDate", "   年   月   日");
		}
		
		String currencyName = "";    //币种名称
		String moneyStr= "";		//币种金额
		String currencyExchangerate = "";  //汇率
		String totalRMBMoneyStr = ""; //合计人民币金额
		String totalRMBMoneyName = ""; //合计人民币金额大写
		
		//返佣申请相关信息rebates表
		Map<String, BigDecimal> rateMap = Maps.newHashMap();   //币种对应的汇率表
		List<Rebates> rebatesList = findRebatesListByRid(Long.parseLong(reviewId));
		Rebates rebate = rebatesList.get(0);
		if(rebate != null ){
			handleRemark(reviewId, rebate, map);
			//合计人民币金额
			for(Rebates rebates :rebatesList){		
				List<MoneyAmount> moneyList = moneyAmountService.findAmountBySerialNum(rebates.getNewRebates());
				MoneyAmount money = new MoneyAmount();
				if(moneyList != null && moneyList.size() > 0){
					for(MoneyAmount moneyAmount : moneyList){
						if(rebates.getCurrencyId().longValue() == moneyAmount.getCurrencyId().longValue() ){
							money = moneyAmount;
							rateMap.put(rebates.getCurrency().getCurrencyMark(), money.getExchangerate());
							break;
						}
					}
				}
			}
		}

		User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
		if (null != user) {
			map.put("operatorName", user.getName());	//经办人(返佣申请人)
			map.put("payee", user.getName());	//领款人(返佣申请人)
		} else {
			map.put("operatorName", "");//经办人(返佣申请人)
			map.put("payee", "");	//领款人(返佣申请人)
		}
		//45需求，凭单中的金额以每次支付的金额为准 ---多币种---
		List<Object[]> moneys = null;
		PayInfoDetail payDetail = null;
		if (StringUtils.isNotBlank(payId)) {
			payDetail = refundService.getPayInfoByPayId(payId, Context.PRODUCT_TYPE_AIRTICKET.toString());
			if (payDetail != null && StringUtils.isNotBlank(payDetail.getMoneyDispStyle())) {
				moneys = MoneyNumberFormat.getMoneyFromString(payDetail.getMoneyDispStyle(), "\\+");
				//0419 越柬行踪 领款人改成实际领款人
				if(Context.SUPPLIER_UUID_YJXZ.equals(companyUuid)){
					Integer payType = payDetail.getPayType();
					if(1 == payType || 3 == payType){
						map.put("isYJXZ", "isYJXZ");
						map.put("payee", payDetail.getPayerName());	//收款单位
					}
				}
			}
		}
		if(CollectionUtils.isNotEmpty(moneys)) {
			BigDecimal  totalRMBMoney = new BigDecimal(0);	//合计人民币金额			
			for(Object[] money : moneys) {
				String currencyMark = money[0].toString();
				BigDecimal newRebates = new BigDecimal(Double.valueOf(money[1].toString()));
				if(rateMap.containsKey(currencyMark)) {					
					BigDecimal RMBMoney = newRebates.multiply(rateMap.get(currencyMark));
					totalRMBMoney = totalRMBMoney.add(RMBMoney);
				}else {
					Currency currency = currencyService.findCurrencyByCurrencyMark(currencyMark,UserUtils.getUser().getCompany().getId());
					BigDecimal rate = null==currency? new BigDecimal(0):currency.getCurrencyExchangerate();
					BigDecimal RMBMoney = newRebates.multiply(rate);
					totalRMBMoney = totalRMBMoney.add(RMBMoney);
				}
			}
				
			currencyName = "人民币";
			moneyStr = MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO);	//币种金额
			currencyExchangerate = "1.0000";  //汇率
			
			totalRMBMoneyStr = MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO);	//合计人民币
			int result = totalRMBMoney.compareTo(new BigDecimal(0));
			if(result == 1 || result == 0 ){
				totalRMBMoneyName = StringNumFormat.changeAmount(totalRMBMoney.toString()); //合计人民币金额大写
			}else{
				totalRMBMoneyName = "红字" + StringNumFormat.changeAmount(totalRMBMoney.toString().replaceAll("-","")); //合计人民币金额大写
			}
		}
		
		map.put("currencyName", currencyName);
		map.put("money", moneyStr);
		map.put("currencyExchangerate", currencyExchangerate);
		map.put("costname",  "报销");//款项
		map.put("totalRMBMoney", totalRMBMoneyStr);
		map.put("totalRMBMoneyName", totalRMBMoneyName);
		
		Agentinfo agentInfo = null;
		if( rebate != null &&  rebate.getOrderId() != null){
			long orderId = rebate.getOrderId();
			AirticketOrder productOrder = airticketOrderDao.getAirticketOrderById(orderId);
			agentInfo = agentinfoDao.findOne(productOrder.getAgentinfoId());
			String orderCompanyName = agentInfo.getAgentName();
			map.put("orderCompanyName", orderCompanyName);	//渠道名称
		}else{
			map.put("orderCompanyName", "");
		}
		
		//银行账户名
		String accountName="";
		if(agentInfo != null) {
			accountName = bankInfoService.getAccountName(agentInfo.getId(), Context.PLAT_TYPE_QD, payDetail==null? "":payDetail.getTobankName(), payDetail==null? "":payDetail.getTobankAccount(),"");
		}
		map.put("accountName", accountName);
		
		List<ReviewLog> reviewLogs = reviewService.findReviewLog(Long.parseLong(reviewId));

		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳  7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REBATES_FLOW_TYPE,reviewLogs);
		
		map.put("deptmanager", jobtypeusernameMap.get(4) == null ?"":jobtypeusernameMap.get(4));	//主管审批
		map.put("cashier", jobtypeusernameMap.get(6) == null ? "" : jobtypeusernameMap.get(6));	//出纳
		map.put("finance", jobtypeusernameMap.get(8) == null ? "" :jobtypeusernameMap.get(8));		//财务
		map.put("financeManage", jobtypeusernameMap.get(9) == null ? "" : jobtypeusernameMap.get(9));	//财务主管
		map.put("majorCheckPerson", jobtypeusernameMap.get(10) == null ? "" : jobtypeusernameMap.get(10));	//总经理
		//如果财务主管不为空，审核取财务主管值；如果财务主管位空，则审核取财务值
		if(null != jobtypeusernameMap.get(9)){
			map.put("auditor", jobtypeusernameMap.get(9) == null ? "" : jobtypeusernameMap.get(9));	//审核
		}else {
			map.put("auditor", jobtypeusernameMap.get(8) == null ? "" : jobtypeusernameMap.get(8));	//审核
		}
		
		//环球行用户将团号改为订单团号
		if (Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			map.put("groupCodeName", "订单团号");
		} else {
			map.put("groupCodeName", "团号");
		}
				
		return map;
	}
	
	/**
	 * 机票返佣打印单 , 仅供返佣列表的打印按钮使用
	 * add by wangxu
	 * @param reviewId
	 * @param rebatesId
	 * @return
	 */
	public Map<String,Object> airticketRebatesPrintData(String reviewId) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		//返佣申请审核相关信息review表
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(reviewId));
			map.put("createDate", DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));	//填写日期
			String printTime = reviewAndDetailInfoMap.get("printTime");
			if(!StringUtils.isEmpty(printTime)  ){
				map.put("firstPrintTime",  DateUtils.dateFormat(reviewAndDetailInfoMap.get("printTime")));	//首次打印时间
			}else{
				map.put("firstPrintTime",  new Date());	//首次打印时间
			}
			
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			if (null != user) {
				map.put("operatorName", user.getName());	//经办人(返佣申请人)
				map.put("payee", user.getName());	//领款人(返佣申请人)
			} else {
				map.put("operatorName", "");//经办人(返佣申请人)
				map.put("payee", "");	//领款人(返佣申请人)
			}
			
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			String payStatus = reviewAndDetailInfoMap.get("payStatus");
			if(!Context.SUPPLIER_UUID_HQX.equals(companyUuid) && !Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid) && "1".equals(payStatus)) {
				Date confirmPayDate = DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"));
				map.put("confirmPayDate", DateUtils.formatDate(confirmPayDate, "yyyy 年  M 月 dd 日"));
			} else {
				map.put("confirmPayDate", "   年   月   日");
			}
		
		//返佣申请相关信息rebates表
		List<Rebates> rebatesList = findRebatesListByRid(Long.parseLong(reviewId));
		Rebates rebate = rebatesList.get(0);
		if(rebate != null ){
			handleRemark(reviewId, rebate, map);
			
			BigDecimal  totalRMBMoney = new BigDecimal(0);	//合计人民币金额
			for(Rebates rebates :rebatesList){
				String currencyName = rebates.getCurrency().getCurrencyName().trim();	//币种名称
			
				List<MoneyAmount> moneyList = moneyAmountService.findAmountBySerialNum(rebates.getNewRebates());
				MoneyAmount money = new MoneyAmount();
				if(moneyList != null && moneyList.size() > 0){
					for(MoneyAmount moneyAmount : moneyList){
						if(rebates.getCurrencyId().longValue() == moneyAmount.getCurrencyId().longValue() ){
							money = moneyAmount;
							break;
						}
					}
				}
				
				BigDecimal newRebates = new BigDecimal(0);
				newRebates = money.getAmount() == null?newRebates:money.getAmount();	//金额			
			
				//如果是非人民币
				if(!currencyName.contains("人民币")){
					BigDecimal currencyExchangerate = new BigDecimal(1);
					currencyExchangerate = money.getExchangerate() == null ? rebates.getCurrency().getCurrencyExchangerate() : money.getExchangerate();	
					//转换成人民币金额 = 金额 * 汇率
					BigDecimal RMBMoney = newRebates.multiply(currencyExchangerate);
				
					totalRMBMoney = totalRMBMoney.add(RMBMoney);
				
					map.put("currencyExchangerate", "1.0000");//汇率默认为1
				}else{
					totalRMBMoney = totalRMBMoney.add(newRebates);
					map.put("currencyExchangerate", "1.0000");//汇率默认为1
				}
			}
			
			map.put("currencyName", "人民币");	//币种名称
			map.put("money",MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));	//币种金额
			map.put("totalRMBMoney", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO)); //合计人民币金额
			
			int result = totalRMBMoney.compareTo(new BigDecimal(0));
			if(result == 1 || result == 0 ){
				map.put("totalRMBMoneyName", StringNumFormat.changeAmount(totalRMBMoney.toString())); //合计人民币金额大写
			}else{
				map.put("totalRMBMoneyName", "红字" + StringNumFormat.changeAmount(totalRMBMoney.toString().replaceAll("-",""))); //合计人民币金额大写
			}			
		}else{
			map.put("currencyName", "");	//币种名称
			map.put("money", "");	//币种金额
			map.put("currencyExchangerate", "");//汇率
			map.put("totalRMBMoney", ""); //合计人民币金额
			map.put("remark",  "");//备注
			map.put("totalRMBMoneyName", ""); //合计人民币金额大写
		}
		map.put("costname",  "报销");//款项
		
		Agentinfo agentInfo = null;
		if( rebate != null &&  rebate.getOrderId() != null){
			long orderId = rebate.getOrderId();
			AirticketOrder productOrder = airticketOrderDao.getAirticketOrderById(orderId);
			agentInfo = agentinfoDao.findOne(productOrder.getAgentinfoId());
			String orderCompanyName = agentInfo.getAgentName();
			
			map.put("orderCompanyName", orderCompanyName);	//渠道名称
		}else{
			map.put("orderCompanyName", "");
		}
		
		//银行账户名
		map.put("accountName", "");
		
		List<ReviewLog> reviewLogs = reviewService.findReviewLog(Long.parseLong(reviewId));

		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳  7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REBATES_FLOW_TYPE,reviewLogs);
		
		map.put("deptmanager", jobtypeusernameMap.get(4) == null ?"":jobtypeusernameMap.get(4));	//主管审批
		map.put("cashier", jobtypeusernameMap.get(6) == null ? "" : jobtypeusernameMap.get(6));	//出纳
		map.put("finance", jobtypeusernameMap.get(8) == null ? "" :jobtypeusernameMap.get(8));		//财务
		map.put("financeManage", jobtypeusernameMap.get(9) == null ? "" : jobtypeusernameMap.get(9));	//财务主管
		map.put("majorCheckPerson", jobtypeusernameMap.get(10) == null ? "" : jobtypeusernameMap.get(10));	//总经理
		//如果财务主管不为空，审核取财务主管值；如果财务主管位空，则审核取财务值
		if(null != jobtypeusernameMap.get(9)){
			map.put("auditor", jobtypeusernameMap.get(9) == null ? "" : jobtypeusernameMap.get(9));	//审核
		}else {
			map.put("auditor", jobtypeusernameMap.get(8) == null ? "" : jobtypeusernameMap.get(8));	//审核
		}
		
		//环球行用户将团号改为订单团号
		if (Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			map.put("groupCodeName", "订单团号");
		} else {
			map.put("groupCodeName", "团号");
		}
				
		return map;
	}
	
	/**
	 * 修改支出凭单摘要 C437
	 * @author yunpeng.zhang
	 * @createTime 2015年11月23日
	 * @param reviewId
	 * @param rebate
	 * @param map
	 */
	private void handleRemark(String reviewId, Rebates rebate, Map<String, Object> map) {
		List<Refund> refundList = refundService.findLastPayByRecordId(Long.parseLong(reviewId), 3);
		Refund refund = null;
		if(refundList != null && refundList.size() > 0) {
			refund = refundList.get(0);
		}
		if (refund != null) {
			String remarks = "";
			if(StringUtils.isNotBlank(rebate.getRemark()) && StringUtils.isNotBlank(refund.getRemarks())) {
				remarks = "项目备注：" + rebate.getRemark() + " 付款备注：" + refund.getRemarks();
			} else if( StringUtils.isNotBlank(refund.getRemarks())){
				remarks = "付款备注：" + refund.getRemarks();
			}
			map.put("remark", remarks);
		} else {
			if (StringUtils.isNotBlank(rebate.getRemark())) {
				map.put("remark","项目备注：" + rebate.getRemark());
			} else {
				map.put("remark", "");
			}
		}
	}
	public IslandRebates findIslandRebatesById(Long id){
		return islandRebatesDao.getById(id);
	} 
	/**
	 * 20150716 获取报名时填写的团队返佣参考值
	 * @author gao
	 * @param orderId
	 * @return
	 */
	public List<Rebates> getGroupReList(Long orderId){
		List<Rebates> reList = rebatesDao.findListByOrderId(orderId);
		return reList;
	}
}
