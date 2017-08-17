package com.trekiz.admin.modules.review.changeprice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.hotel.dao.HotelMoneyAmountDao;
import com.trekiz.admin.modules.hotel.dao.HotelOrderDao;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.island.dao.IslandMoneyAmountDao;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.dao.IslandTravelerDao;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.review.changeprice.entity.ChangePriceBean;
import com.trekiz.admin.modules.review.changeprice.repository.IChangePriceReviewDao;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;

@Service
@Transactional(readOnly = true)
public class ChangePriceReviewNewServiceImpl implements
		IChangePriceReviewService {

	@Autowired
	private IChangePriceReviewDao changePriceReviewDao;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private HotelMoneyAmountDao hotelMoneyAmountDao;
	@Autowired
	private HotelTravelerDao hotelTravelerDao;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private IslandMoneyAmountDao islandMoneyAmountDao;
	@Autowired
	private IslandTravelerDao islandTravelerDao;
	@Autowired
	private HotelOrderDao hotelOrderDao;
	@Autowired
	private IslandOrderDao islandOrderDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private OrderCommonService orderCommonService;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;
	/**
	 * 根据条件查询改价审批列表 现在进入这个改价审批的产品有机票、签证、团期、机票切位、散拼切位 由于业务表不同。需提供不同的SQL去单独查询
	 * 现在的思路是 写多个DAO方法 每个DAO查询一个产品的产品信息 再有一个DAO去查询审批表(为了翻页) 查询出所有的改价审批信息
	 * 然后再去关联产品信息 这样做的前提理解是：有一条审批记录 必然会有对应的产品订单信息
	 */
    @Override
	public Page<Map<String, Object>> queryRefundReviewList(
			HttpServletRequest request, HttpServletResponse response) {

		// 1 获取参数start
		String groupCode = request.getParameter("groupCode");// 团号
		String statusChoose = request.getParameter("statusChoose");// 状态过滤 默认为全部
		String productType = request.getParameter("orderType");// 团队类型 即产品类型
		String channel = request.getParameter("channel");// 渠道
		String saler = request.getParameter("saler");// 销售
		String meter = request.getParameter("meter");// 计调
		if(statusChoose == null){
			statusChoose = "1";
		}
		if (statusChoose != null && ("all".equals(statusChoose.trim()) || "".equals(statusChoose.trim()))) {
			statusChoose = null;// null即全部0 已驳回 1 待审核 2 审核成功 3 操作完成
		}
		if (productType != null && "-99999".equals(productType.trim())) {
			productType = null;
		}
		String startTime = null;
		if (request.getParameter("startTime") != null
				&& !"".equals(request.getParameter("startTime").trim())) {
			startTime = request.getParameter("startTime");
		}
		String endTime = null;
		if (request.getParameter("endTime") != null
				&& !"".equals(request.getParameter("endTime").trim())) {
			endTime = request.getParameter("endTime");
		}
		if (channel != null && "-99999".equals(channel.trim())) {
			channel = null;
		}
		if (saler != null && "-99999".equals(saler.trim())) {
			saler = null;
		}
		if (meter != null && "-99999".equals(meter.trim())) {
			meter = null;
		}
		String orderBy = request.getParameter("orderBy");//
		if (orderBy == null || "".equals(orderBy.trim())) {
			orderBy = null;
		}
		/*新的审核 start*/
		//获取参数
		//选中的userJob的id
		String cOrderBy  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String uOrderBy = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		String userJobIdStr = request.getParameter("userJobId");
		UserJob userJob = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		//获取当前用户的职位信息
		List<UserJob> userJobsAll = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_CHANGE_PRICE);
		List<UserJob> userJobs = new ArrayList<UserJob>();
		String headPrd = request.getParameter("headPrd");
		if(headPrd == null){
			headPrd = "1";
		}
		for(UserJob temp : userJobsAll){
			if(temp.getOrderType() == Integer.parseInt(headPrd)){
				userJobs.add(temp);
			}
		}
		if(userJobs == null || userJobs.size() == 0) {
			return null;
		}
		//获取第一层级部门id
		Long pDeptId;
		//子部门id
		List<Long> subIds = new ArrayList<Long>();
		//如果userJob为null则表明是第一次进入审核页面  默认取第一个userJob
		if(userJob == null){
			userJob = userJobs.get(userJobs.size() - 1);
		}
		
		if(userJob.getDeptLevel() == 1){
			pDeptId = userJob.getDeptId();
			subIds = departmentDao.findSubidsByParentId(pDeptId);
		} else {
			pDeptId = userJob.getParentDept();
			subIds.add(userJob.getDeptId());
		}
		//获取reviewComppanyid
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_CHANGE_PRICE, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_CHANGE_PRICE);
		if(levels == null || levels.size() == 0){
			return null;
		}
		Page<Map<String, Object>> refundReviewPageData = changePriceReviewDao
				.findRefundReviewList(request, response, groupCode,
						startTime, endTime, channel, saler, meter,
						statusChoose, levels, Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString(), cOrderBy, uOrderBy, userJob, reviewCompanyId, subIds);
		/*新的审核 end*/
		// 2.1查询审核表数据 由于这是针对改价的 所以查出审核表中所有的改价数据 end
		// 查询改价款项信息 start
		List<Map<String, String>> reviewCompanyListMap = reviewService.
				findReviewCompanyListMap(userJob.getOrderType().intValue(), Context.REVIEW_FLOWTYPE_CHANGE_PRICE, false, subIds);
		// 查询改价款项信息 end
		// 3.组织审批信息和款项信息 返回页面展示 start
		List<Map<String, Object>> refundReviewList = refundReviewPageData
				.getList();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 3.组织审批信息和款项信息 返回页面展示 end
		// 整合信息数据返回
		for (Map<String, Object> temp : refundReviewList) {
			temp.put("revLevel", levels.get(0));
			for (Map<String, String> tempReview : reviewCompanyListMap) {
				if (temp.get("revid") == null
						|| "".equals(temp.get("revid").toString().trim())
						|| tempReview.get("id") == null
						|| "".equals(tempReview.get("id").toString().trim())) {
					continue;
				}
				if (temp.get("revid").toString()
						.equals(tempReview.get("id").toString().trim())) {
					Map<String, Object> tempMap = new HashMap<String, Object>();
					String curtotal = tempReview.get(ChangePriceBean.CUR_TOTAL_MONEY);
					tempReview.remove(ChangePriceBean.CUR_TOTAL_MONEY);
					tempReview.put(ChangePriceBean.CUR_TOTAL_MONEY, curtotal);
					String changetotal = tempReview.get(ChangePriceBean.CHANGED_TOTAL_MONEY);
					tempReview.remove(ChangePriceBean.CHANGED_TOTAL_MONEY);
					tempReview.put(ChangePriceBean.CHANGED_TOTAL_MONEY, changetotal);
					tempMap.putAll(tempReview);
					tempMap.putAll(temp);
					list.add(tempMap);
				}
			}
		}
		refundReviewPageData.setList(list);
		return refundReviewPageData;
	}
	
	/**
	 * 根据条件查询参团订单详情
	 */
	@Override
	public Map<String, Object> queryGrouporderDeatail(String prdOrderId) {
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return changePriceReviewDao.queryGroupReviewOrderDetail(prdOrderId);
	}

	/**
	 * 根据条件查询签证订单详情 
	 */
	@Override
	public Map<String, Object> queryVisaorderDeatail(String prdOrderId) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return changePriceReviewDao.queryVisaReviewOrderDetail(prdOrderId);
	}
	
	/**
	 * 根据条件查询机票订单详情
	 */
	@Override
	public Map<String, Object> queryAirticketorderDeatail(String prdOrderId, String prdType) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return changePriceReviewDao.queryAirticketReviewOrderDetail(prdOrderId, prdType);
	}

	@Override
	public Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return changePriceReviewDao.querySanPinReviewOrderDetail(prdOrderId);
	}
	
	@Override
	public boolean doChangePrice(Review review) {
	
		Map<String, String> reviewDetail = reviewService.findReview(review.getId());
		Map<String, String> params = new HashMap<String, String>();
		params.put("travelerId", reviewDetail.get("travelerid"));
		params.put("orderId", review.getOrderId());
		params.put("orderType", review.getProductType().toString());
		params.put("currencyId", reviewDetail.get("currencyid"));
		params.put("amount", reviewDetail.get("changedprice"));
		return doChangePrice(params);
	}

	/**
	 * 进行改价业务数据处理
	 * 成功true 失败false
	 */
	@Override
	@Transactional
	public boolean doChangePrice(Map<String, String> params) {
		
		String travelerId = params.get("travelerId");
		String orderId = params.get("orderId");
		String orderType = params.get("orderType");
		String currencyId = params.get("currencyId");
		String amount = params.get("amount");

		if("0".equals(travelerId)) {//团队
			List<MoneyAmount> amount2 = moneyAmountService.findAmount(Long.parseLong(orderId), Context.MONEY_TYPE_YSH, Integer.parseInt(orderType));
			boolean curFlag = false;//是否有这个币种标志
			if(amount2 == null || amount2.size() == 0){//判空保护
				return false;
			}
			for(MoneyAmount tempMoney : amount2){
				if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的结算价
					curFlag = true;
					tempMoney.setAmount(tempMoney.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount))));
					Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
					tempMoney.setExchangerate(currency.getConvertLowest());
					moneyAmountDao.updateAmountById(tempMoney.getAmount(), tempMoney.getExchangerate(), tempMoney.getId());
					break;
				}
			}
			if(curFlag == false){
				MoneyAmount ma = new MoneyAmount();
				ma.setAmount(BigDecimal.valueOf(Double.parseDouble(amount)));
				ma.setCurrencyId(Integer.parseInt(currencyId));
				ma.setSerialNum(amount2.get(0).getSerialNum());
				ma.setBusindessType(1);
				ma.setOrderType(Integer.parseInt(orderType));
				ma.setMoneyType(Context.MONEY_TYPE_YSH);
				ma.setUid(Long.parseLong(orderId));
				moneyAmountService.saveOrUpdateMoneyAmount(ma);
			}
		} else if ("-1".equals(travelerId)){//订金
			List<MoneyAmount> amount2 = moneyAmountService.findAmount(Long.parseLong(orderId), Context.MONEY_TYPE_DJ, Integer.parseInt(orderType));
			for(MoneyAmount tempMoney : amount2){
				if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的结算价
					tempMoney.setAmount(tempMoney.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount))));
					Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
					tempMoney.setExchangerate(currency.getConvertLowest());
					moneyAmountDao.updateAmountById(tempMoney.getAmount(), tempMoney.getExchangerate(), tempMoney.getId());
					break;
				}
			}
		} else {//既不是团队改价 也不是订金改价 一定是游客改价
			//注释掉了 更改游客的结算价  如果是签证 应做对应处理 等签证功能完善
//			if(Context.ORDER_STATUS_VISA.equals(orderType)){//如果是签证的话 则更改游客的应收价
//				List<MoneyAmount> amount2 = moneyAmountService.findAmount(Long.parseLong(travelerId), Context.MONEY_TYPE_JSJ, Integer.parseInt(orderType));
			//游客结算价改价
			Traveler traveler = travelerDao.findById(Long.parseLong(travelerId));
			if (traveler != null) {
				String payPriceSeria = traveler.getPayPriceSerialNum();
				List<MoneyAmount> tempMoneyList = moneyAmountDao.findAmountBySerialNum(payPriceSeria);
				if (CollectionUtils.isNotEmpty(tempMoneyList)) {
					for (MoneyAmount tempMoney : tempMoneyList) {
						if (tempMoney != null && tempMoney.getCurrencyId() == Integer.parseInt(currencyId)) {
							tempMoney.setAmount(tempMoney.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount))));
							Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
							tempMoney.setExchangerate(currency.getConvertLowest());
							moneyAmountDao.updateAmountById(tempMoney.getAmount(), tempMoney.getExchangerate(), tempMoney.getId());
						}
					}
				}				
			}
			
//				List<MoneyAmount> amount2 = moneyAmountService.findAmount(Long.parseLong(travelerId), Context.MONEY_TYPE_JSJ, Integer.parseInt(orderType), Context.BUSINESS_TYPE_TRAVELER);
//				if(amount2 != null){
//					for(MoneyAmount tempMoney : amount2){
//						if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的结算价
//							tempMoney.setAmount(tempMoney.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount))));
//							Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
//							tempMoney.setExchangerate(currency.getConvertLowest());
//							moneyAmountDao.updateAmountById(tempMoney.getAmount(), tempMoney.getExchangerate(), tempMoney.getId());
//							break;
//						}
//					}
//				}
//			}
			//游客改价 同时更改订单的价格
//			List<MoneyAmount> amount3 = moneyAmountService.findAmount(Long.parseLong(orderId), Context.MONEY_TYPE_YSH, Integer.parseInt(orderType), Context.BUSINESS_TYPE_ORDER);
			ProductOrderCommon orderCommon = null;
			AirticketOrder airticketOrder = null;
			String totalMoneySerial = "";
			//团期类
			if (Context.ORDER_STATUS_SINGLE.equals(orderType) || Context.ORDER_STATUS_LOOSE.equals(orderType) ||
					Context.ORDER_STATUS_BIG_CUSTOMER.equals(orderType) || Context.ORDER_STATUS_FREE.equals(orderType) ||
					Context.ORDER_STATUS_STUDY.equals(orderType) || Context.ORDER_STATUS_CRUISE.equals(orderType)) {				
				orderCommon = orderCommonService.getProductorderById(Long.parseLong(orderId));
				if (orderCommon != null) {
					totalMoneySerial = orderCommon.getTotalMoney();					
				}
			//机票
			} else if (Context.ORDER_STATUS_AIR_TICKET.equals(orderType)) {			
				airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
				if (airticketOrder != null) {
					totalMoneySerial = airticketOrder.getTotalMoney();
				}
			} else {
				String sql = "UPDATE  visa_order vo LEFT JOIN money_amount ma  ON vo.total_money =ma.serialNum "
						+" SET ma.amount = ma.amount+"+amount+" WHERE vo.id="+orderId+" AND ma.currencyId="+currencyId;
				
				moneyAmountDao.updateBySql(sql);
			}
			if (StringUtils.isNotBlank(totalMoneySerial)) {
				List<MoneyAmount> amount3 = moneyAmountService.findAmountBySerialNumAndCurrencyId(totalMoneySerial, Integer.parseInt(currencyId));
				for(int j=0; j<amount3.size(); j++){
					MoneyAmount tempMoney = amount3.get(j);
					if (tempMoney != null) {
						if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的订单应收价
							tempMoney.setAmount(tempMoney.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount))));							
							Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
							tempMoney.setExchangerate(currency.getConvertLowest());
							moneyAmountDao.updateAmountById(tempMoney.getAmount(), tempMoney.getExchangerate(), tempMoney.getId());
							break;
						} else if (j == amount3.size() - 1) {
							MoneyAmount tempMoneyNew = new MoneyAmount();
							BeanUtils.copyProperties(tempMoney, tempMoneyNew);
							tempMoneyNew.setAmount(BigDecimal.valueOf(Double.parseDouble(amount)));//金额
							tempMoneyNew.setCurrencyId(Integer.parseInt(currencyId));//币种
							Currency currency = currencyDao.findOne(Long.parseLong(currencyId));
							tempMoneyNew.setExchangerate(BigDecimal.valueOf(currency.getConvertLowest().doubleValue()));//汇率
							tempMoneyNew.setSerialNum(totalMoneySerial);//uuid	
							tempMoneyNew.setCreateTime(new Date());
							moneyAmountDao.saveObj(tempMoneyNew);
						}
					}
				}
			}
		}
		
		//-------by------junhao.zhao-----2017-03-24-----------改价之后，签证与团类要将表order_data_statistics中对应的收客人数与订单金额修改---开始---
		if(null != orderId && null != orderType){
			switch (orderType) {
			case Context.ORDER_STATUS_VISA:
				orderDateSaveOrUpdateDao.updatePeopleAndMoney(Long.valueOf(orderId), Integer.valueOf(orderType));
				break;
			default:
				orderDateSaveOrUpdateDao.updatePeopleAndMoneyPro(Long.valueOf(orderId), Integer.valueOf(orderType));
			}
		}
		//-------by------junhao.zhao-----2017-03-24-----------改价之后，签证与团类要将表order_data_statistics中对应的收客人数与订单金额修改---结束---
		return true;
	}
	/**
	 * 进行改价业务数据处理
	 * 成功true 失败false
	 */
	@Override
	public boolean doHotelChangePrice(Map<String, String> params) {
		
		String travelerId = params.get("travelerId");
		String currencyId = params.get("currencyId");
		String amount = params.get("amount");
		String orderUuid = params.get("orderUuid");
		HotelTraveler t = hotelTravelerDao.getById(Integer.parseInt(travelerId));
		HotelOrder hotelOrder = hotelOrderDao.getByUuid(orderUuid);
		List<HotelMoneyAmount> amount2 = hotelMoneyAmountService.findAmount(t.getPayPriceSerialNum());
		if(amount2 != null){
			for(HotelMoneyAmount tempMoney : amount2){
				if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的结算价
					tempMoney.setAmount(tempMoney.getAmount()+Double.parseDouble(amount));
					Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
					tempMoney.setExchangerate(currency.getConvertLowest().doubleValue());
					hotelMoneyAmountDao.updateAmount(tempMoney.getUuid(), tempMoney.getAmount());
					break;
				}
			}
		}

		//游客改价 同时更改订单的价格
		List<HotelMoneyAmount> amount3 = hotelMoneyAmountService.findAmount(hotelOrder.getTotalMoney());
		for(int j=0; j<amount3.size(); j++){
			HotelMoneyAmount tempMoney = amount3.get(j);
			if (tempMoney != null) {
				if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的订单应收价
					tempMoney.setAmount(tempMoney.getAmount().doubleValue()+Double.parseDouble(amount));
					
					Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
					tempMoney.setExchangerate(currency.getConvertLowest().doubleValue());
					hotelMoneyAmountDao.updateAmount(tempMoney.getUuid(),tempMoney.getAmount());
					break;
				} else if (j == amount3.size() - 1) {
					HotelMoneyAmount tempMoneyNew = new HotelMoneyAmount();
					BeanUtils.copyProperties(tempMoney, tempMoneyNew);
					tempMoneyNew.setAmount(Double.parseDouble(amount));//金额
					tempMoneyNew.setCurrencyId(Integer.parseInt(currencyId));//币种
					Currency currency = currencyDao.findOne(Long.parseLong(currencyId));
					tempMoneyNew.setExchangerate(currency.getConvertLowest().doubleValue());//汇率
					tempMoneyNew.setUuid(hotelOrder.getTotalMoney());//uuid
					tempMoneyNew.setCreateDate(new Date());
					tempMoneyNew.setUpdateDate(new Date());
					hotelMoneyAmountDao.saveObj(tempMoneyNew);
				}
			}
		}
		return true;
	}
	/**
	 * 进行改价业务数据处理
	 * 成功true 失败false
	 */
	@Override
	public boolean doIslandChangePrice(Map<String, String> params) {
		
		String travelerId = params.get("travelerId");
		String currencyId = params.get("currencyId");
		String amount = params.get("amount");
		String orderUuid= params.get("orderUuid");
		IslandTraveler t = islandTravelerDao.getById(Integer.parseInt(travelerId));
		IslandOrder islandOrder = islandOrderDao.getByUuid(orderUuid);
		
		List<IslandMoneyAmount> amount2 = islandMoneyAmountService.findAmount(t.getPayPriceSerialNum());
		if(amount2 != null){
			for(IslandMoneyAmount tempMoney : amount2){
				if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的结算价
					tempMoney.setAmount(tempMoney.getAmount()+Double.parseDouble(amount));
					Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
					tempMoney.setExchangerate(currency.getConvertLowest().doubleValue());
					islandMoneyAmountDao.updateAmount(tempMoney.getUuid(), tempMoney.getAmount());
					break;
				}
			}
		}

		//游客改价 同时更改订单的价格
		List<IslandMoneyAmount> amount3 = islandMoneyAmountService.findAmount(islandOrder.getTotalMoney());
		for(int j=0; j<amount3.size(); j++){
			IslandMoneyAmount tempMoney = amount3.get(j);
			if (tempMoney != null) {
				if(tempMoney.getCurrencyId() == Integer.parseInt(currencyId)){//更改对应币种的订单应收价
					tempMoney.setAmount(tempMoney.getAmount().doubleValue()+Double.parseDouble(amount));
					
					Currency currency = currencyDao.findOne(Long.parseLong(tempMoney.getCurrencyId().toString()));
					tempMoney.setExchangerate(currency.getConvertLowest().doubleValue());
					islandMoneyAmountDao.updateAmount(tempMoney.getUuid(),tempMoney.getAmount());
					break;
				} else if (j == amount3.size() - 1) {
					IslandMoneyAmount tempMoneyNew = new IslandMoneyAmount();
					BeanUtils.copyProperties(tempMoney, tempMoneyNew);
					tempMoneyNew.setAmount(Double.parseDouble(amount));//金额
					tempMoneyNew.setCurrencyId(Integer.parseInt(currencyId));//币种
					Currency currency = currencyDao.findOne(Long.parseLong(currencyId));
					tempMoneyNew.setExchangerate(currency.getConvertLowest().doubleValue());//汇率
					tempMoneyNew.setUuid(islandOrder.getTotalMoney());//uuid				
					tempMoneyNew.setCreateDate(new Date());
					tempMoneyNew.setUpdateDate(new Date());
					islandMoneyAmountDao.saveObj(tempMoneyNew);
				}
			}
		}
		return true;
	}
}
