package com.trekiz.admin.modules.cost.service;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

import com.google.common.collect.Lists;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.engine.repository.ReviewNewDao;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.cost.entity.AbstractSpecificCost;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.entity.CostRecordHotel;
import com.trekiz.admin.modules.cost.entity.CostRecordIsland;
import com.trekiz.admin.modules.cost.entity.CostRecordLog;
import com.trekiz.admin.modules.cost.entity.CostRecordVO;
import com.trekiz.admin.modules.cost.repository.CostManagerDao;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.repository.CostRecordHotelDao;
import com.trekiz.admin.modules.cost.repository.CostRecordIslandDao;
import com.trekiz.admin.modules.cost.repository.CostRecordLogDao;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.island.dao.ActivityIslandDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.HotelRebates;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandRebates;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.PayRemittance;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.rebates.entity.Rebates;
import com.trekiz.admin.modules.order.rebates.entity.RebatesNew;
import com.trekiz.admin.modules.order.repository.PayRemittanceDao;
import com.trekiz.admin.modules.order.repository.PlatBankInfoDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.pay.dao.PayBanktransferDao;
import com.trekiz.admin.modules.pay.dao.PayDraftDao;
import com.trekiz.admin.modules.pay.dao.PayGroupDao;
import com.trekiz.admin.modules.pay.dao.PayPosDao;
import com.trekiz.admin.modules.pay.entity.PayBanktransfer;
import com.trekiz.admin.modules.pay.entity.PayDraft;
import com.trekiz.admin.modules.pay.entity.PayGroup;
import com.trekiz.admin.modules.pay.entity.PayPos;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.refundreview.service.IAirTicketRefundReviewService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewCompany;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.statemachine.context.StateMachineContext;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.MenuDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import com.trekiz.admin.modules.visa.service.IVisaProductsService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.cost.common.ICostCommonService;

import freemarker.template.TemplateException;

@Service
public class CostManageService extends BaseService {

	//预算成本
	public static final int BUDGET_TYPE = 0;
	//实际成本
	public static final int ACTUAL_TYPE = 1;
	@Autowired
	private CostManagerDao costManagerDao;
	@Autowired
	private CostRecordDao costRecordDao;

	@Autowired
	private DictService dictService;

	@Autowired
	private CostRecordIslandDao costRecordIslandDao;

	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private VisaProductsDao visaProductsDao;
	@Autowired
	private IAirTicketRefundReviewService airTicketRefundReviewService;
	@Autowired
	private CostRecordLogDao costRecordLogDao;

	@Autowired
	private ReviewRoleLevelDao reviewRoleLevelDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;

	@Autowired
	private MoneyAmountDao moneyAmountDao;

	@Autowired
	private ReviewDao reviewDao;

	@Autowired
	private PayRemittanceDao payRemittanceDao;
	@Autowired
	private PayBanktransferDao payBanktransferDao;
	@Autowired
	private PayDraftDao payDraftDao;
	@Autowired
	private PayPosDao payPosDao;

	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private ReviewService reviewService;
	@Resource
	private ActivityGroupService activityGroupService;

	@Autowired
	private IActivityAirTicketService iActivityAirTicketService;

	@Autowired
	private IVisaProductsService iVisaProductsService;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private TravelActivityService travelActivityService;

	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;

	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;

	@Autowired
	private RefundService refundService;

	@Autowired
	private RefundDao refundDao;

	@Autowired
	private ProductOrderCommonDao productorderDao;

	@Autowired
	private UserJobDao userJobDao;

	@Autowired
	private ReviewCompanyDao reviewCompanyDao;

	@Autowired
	private ReviewCommonService reviewCommonService;

	@Autowired
	private PayGroupDao payGroupDao;

	@Autowired
	private ReviewLogDao reviewLogDao;

	@Autowired
	private CostRecordHotelDao costRecordHotelDao;

	@Autowired
	private ActivityHotelGroupDao activityHotelGroupDao;

	@Autowired
	private ActivityIslandGroupDao activityIslandGroupDao;
	@Autowired
	private ActivityIslandDao activityIslandDao;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private ActivityHotelDao activityHotelDao;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private AgentinfoDao agentinfoDao;
	@Autowired
	private ReviewNewDao reviewNewDao;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	@Autowired
	private com.quauq.review.core.engine.ReviewService reviewNewService;
	@Autowired
	private PlatBankInfoDao bankInfoDao;
	@Autowired
	private ICostCommonService costCommonService;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private ReturnDifferenceService returnDifferenceService;
	@Autowired
	private IStockDao stockDao;

	

	public long findReviewCompanyId(Long companyId, Integer flowId,
			List<Long> deptList) {
		List<Long> listCompany = new ArrayList<Long>();
		listCompany = reviewCompanyDao.findReviewCompanyIds(companyId, flowId,
				deptList);
		if (listCompany.size() > 0) {
			return listCompany.get(0);
		} else {
			// 部门没有配置审核时，使用默认审核配置
			List<ReviewCompany> list = reviewCompanyDao.findReviewCompanyDept(
					(long) 0, flowId, (long) 0);
			if (list.size() > 0)
				return list.get(0).getId();
			else
				return 0;
		}
	}

	public CostRecord copyCost(CostRecord cd) {
		CostRecord cr = new CostRecord();
		cr.setActivityId(cd.getActivityId());
		cr.setOrderType(cd.getOrderType());
		cr.setName(cd.getName());
		cr.setPrice(cd.getPrice());
		cr.setQuantity(cd.getQuantity());
		cr.setComment(cd.getComment());
		cr.setSupplierType(cd.getSupplierType());
		cr.setCurrencyId(cd.getCurrencyId());
		cr.setOverseas(cd.getOverseas());
		cr.setSupplyType(cd.getSupplyType());

		cr.setSupplyId(cd.getSupplyId());
		cr.setSupplyName(cd.getSupplyName());
		cr.setCreateBy(cd.getCreateBy());
		cr.setBudgetType(cd.getBudgetType());

		cr.setReview(cd.getReview());
		cr.setPayStatus(cd.getPayStatus());
		cr.setDelFlag(cd.getDelFlag());
		cr.setCurrencyAfter(cd.getCurrencyAfter());

		cr.setRate(cd.getRate());
		cr.setPriceAfter(cd.getPriceAfter());
		cr.setNowLevel(cd.getNowLevel());
		cr.setReviewCompanyId(cd.getReviewCompanyId());

		cr.setBankName(cd.getBankName());
		cr.setBankAccount(cd.getBankAccount());
		cr.setReviewType(cd.getReviewType());
		cr.setReviewCompanyId(cd.getReviewCompanyId());

		cr.setUpdateBy(cd.getUpdateBy());
		cr.setCreateDate(cd.getCreateDate());
		cr.setUpdateDate(cd.getUpdateDate());
		cr.setPayApplyDate(cd.getPayApplyDate());
		cr.setPayUpdateBy(cd.getPayUpdateBy());
		cr.setPayUpdateDate(cd.getPayUpdateDate());

		return cr;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public CostRecord saveCostRecord(CostRecord cost) {
		return costRecordDao.save(cost);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public CostRecordIsland saveCostRecordIsland(
			CostRecordIsland costRecordIsland) {
		return costRecordIslandDao.save(costRecordIsland);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public CostRecordHotel saveCostRecordHotel(CostRecordHotel costRecordHotel) {
		return costRecordHotelDao.save(costRecordHotel);
	}

	public List<Map<String, Object>> getRefunifoForCastList(Long productId,
			Integer orderType) {
		List<Map<String, Object>> costList = iActivityAirTicketService
				.getRefunifoForCastList(productId, orderType);
		return costList;
	}

	public List<Map<String, Object>> getCost(Long activityId,
			Integer orderType, Integer budgetType) {
		List<Map<String, Object>> costList = iActivityAirTicketService.getCost(
				activityId, orderType, budgetType);
		return costList;
	}

	public List<Map<String, Object>> getHotelCost(String activityUuid,
			Integer budgetType) {
		List<Map<String, Object>> costList = iActivityAirTicketService
				.getHotelCost(activityUuid, budgetType);
		return costList;
	}

	public List<Map<String, Object>> getIslandCost(String activityUuid,
			Integer budgetType) {
		List<Map<String, Object>> costList = iActivityAirTicketService
				.getIslandCost(activityUuid, budgetType);
		return costList;
	}

	/* 提交成本审核,获得用户所在部门deptLevel=1 的部门列表 */
	public List<Long> getDeptList(long userId) {
		List<UserJob> userJobList = new ArrayList<UserJob>();
		userJobList = userJobDao.getUserJobList(userId);
		List<Long> deptList = new ArrayList<Long>();
		for (UserJob userjob : userJobList) {
			{
				if (userjob.getDeptLevel() == 1) {
					deptList.add(userjob.getDeptId());
				} else if (userjob.getDeptLevel() == 2) {
					deptList.add(userjob.getParentDept());
				}
			}
		}
		return deptList;
	}

	

	/**
	 * 查询预报单的数据信息 
	 * @param activityId	团期或者产品ID
	 * @param orderType     订单类型
	 * @param groupUUID		海岛游或者酒店产品的团期UUID
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	public CostRecordVO getForecastDataInfo(Long activityId, Integer orderType, String groupUUID){
		CostRecordVO cRecordVO = new CostRecordVO();
		// 产品信息
		Map<String, Object> productInfo = getForecastProductInfo(orderType, activityId, groupUUID);
		// 境内付款(成本价)
		List<CostRecord> budgetInList = findCostRecordForcast(activityId, groupUUID, 0, orderType);
		// 境外付款(成本价)
		List<CostRecord> budgetOutList = findCostRecordForcast(activityId, groupUUID, 1, orderType);

		// 获取返佣金额
		List<Map<String, Object>> fyList = getFYForForecast(activityId, orderType, groupUUID);
		
		// 收款明细
		List<Map<String, Object>> refundList =  getOrderAndRefundInfoForForecast(activityId, orderType, groupUUID);
		//减去差额 538
		for (int i = 0; i < refundList.size() - 1; i++) {
			Map<String,Object> map = refundList.get(i);
			Map<String, Object> moneyMap = returnDifferenceService.getOrderMoneyByOrderId(map);
			if(null != moneyMap){
				map.put("totalMoney", moneyMap.get("totalMoney"));
				map.put("accountedMoney", moneyMap.get("accountedMoney"));
				map.put("notAccountedMoney",moneyMap.get("notAccountedMoney"));
			}
		}
		//其他收入
		List<Map<String, Object>> otherIncome = getOtherIncomeForForecast(activityId, orderType, groupUUID);
		if(! Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
			if (null != refundList) {
				refundList.addAll(refundList.size()-1,otherIncome);
			} else {
				refundList = otherIncome;
			}
		}
		// 添加其他收入收款数据信息
		cRecordVO.setOtherRecordList(otherIncome);
		//操作人的抓取
		if (null != productInfo.get("createById")) {
			Map<String,String> leader = queryLeader(
					Integer.parseInt(productInfo.get("createById").toString()),
					orderType, Context.JOB_OPEATOR_Manager);
			productInfo.put("createByLeader", leader.get("shortLeader"));
			productInfo.put("createByLeaderFull", leader.get("leader"));
		} else {
			productInfo.put("createByLeader", "");
		}
		//0581 需求 将拉美图的操作人写死为陈卡卡       add by chao.zhang 20170321
		if(UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_LAMEITOUR)){
			productInfo.put("createByLeader", "陈卡卡");
			productInfo.put("createByLeaderFull", "陈卡卡");
		}
		//如果是拉美图的用户，在团期产品信息中加上单价，预计收款=单价*预收人数*汇率。需求0396. yudong.xu
		//判断是否是拉美图的团期。
		if (ActivityGroupService.isLMTTuanQi(orderType)){
			Map<String,Object> LMTGroupInfo = activityGroupService.getGroupInfo(activityId);//获取拉美图的成人同行价，和预计总价。
			productInfo.putAll(LMTGroupInfo);//把查询到的单价和预计收款放入产品信息对象中。
		}
		//骡子假期 团队退款总额
		if (Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())) {
			if(orderType < 6){
				BigDecimal groupRefundSum = getGroupRefundSum(activityId, orderType);
				if(! groupRefundSum.toString().equals("0.00")){
					cRecordVO.setGroupRefundSum(groupRefundSum);
				}
			}
		}
		cRecordVO.setBaseinfo(productInfo);
		cRecordVO.setRefundInfo(refundList);
		cRecordVO.setActualInList(budgetInList);
		cRecordVO.setActualoutList(budgetOutList);
		cRecordVO.setInTotal(getTotalCB(budgetInList, "1"));
		cRecordVO.setOutTotal(getTotalCB(budgetOutList, "2"));
		cRecordVO.setfYlistList(fyList);
		return cRecordVO;
	}
	
	/**
	 * 查询结算单的数据信息 
	 * @param activityId	团期或者产品ID
	 * @param orderType     订单类型
	 * @param groupUUID		海岛游或者酒店产品的团期UUID
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	public CostRecordVO getSettleDataInfo(Long activityId, Integer orderType, String groupUUID){
		CostRecordVO cRecordVO = new CostRecordVO();
		// 产品信息
		Map<String, Object> productInfo = getSettleProductInfo(orderType, activityId, groupUUID);
		// 境内付款(成本价)
		List<CostRecord> actualInList =  findCostRecordSettle(activityId, groupUUID, 0, orderType);
		// 境外付款(成本价)
		List<CostRecord> actualOutList = findCostRecordSettle(activityId, groupUUID, 1, orderType);
		List<CostRecord> needRemoveIn = new ArrayList<CostRecord>();
		List<CostRecord> needRemoveOut = new ArrayList<CostRecord>();
		// 实际成本删除驳回的数据
		for (CostRecord cost : actualInList) {
			Integer review = cost.getReview();
			if (review == 0) {
				needRemoveIn.add(cost);
			}
		}

		for (CostRecord cost : actualOutList) {
			Integer review = cost.getReview();
			if (review == 0) {
				needRemoveOut.add(cost);
			}
		}
		actualInList.removeAll(needRemoveIn);
		actualOutList.removeAll(needRemoveOut);
		// 获取返佣金额
		List<Map<String, Object>> fyList = getFYForSettle(activityId, orderType, groupUUID);
		// 收款明细
		List<Map<String, Object>> refundList = getOrderAndRefundInfoForSettle(activityId, orderType, groupUUID);
		//减去差额 538
		for(Map<String,Object> map : refundList){
			Map<String, Object> moneyMap = returnDifferenceService.getOrderMoneyByOrderId(map);
			if(null != moneyMap){
				map.put("totalMoney", moneyMap.get("totalMoney"));
				map.put("accountedMoney", moneyMap.get("accountedMoney"));
				map.put("notAccountedMoney",moneyMap.get("notAccountedMoney"));
			}
		}
		//其他收入
		List<Map<String, Object>> otherIncome = getOtherIncomeForSettle(activityId, orderType, groupUUID);
		// 添加其他收入收款数据信息
		if(! Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
			if (null != refundList) {
				refundList.addAll(otherIncome);
			} else {
				refundList = otherIncome;
			}
		}
		if (null != productInfo.get("createById")) {
			Map<String,String> leader = queryLeader(
					Integer.parseInt(productInfo.get("createById").toString()),
					orderType, Context.JOB_OPEATOR_Manager);
			productInfo.put("createByLeader", leader.get("shortLeader"));
			productInfo.put("createByLeaderFull", leader.get("leader"));
		} else {
			productInfo.put("createByLeader", "");
		}
		//0581 需求 将拉美图的操作人写死为陈卡卡       add by chao.zhang 20170321
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())){
			productInfo.put("createByLeader", "陈卡卡");
			productInfo.put("createByLeaderFull", "陈卡卡");
		}
		//骡子假期 团队退款总额
		if (Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())) {
			if(orderType < 6){
				BigDecimal groupRefundSum = getGroupRefundSum(activityId, orderType);
				if(groupRefundSum != null && ! groupRefundSum.toString().equals("0.00")){
					cRecordVO.setGroupRefundSum(groupRefundSum);
				}
			}
		}
		//其他收入
		cRecordVO.setOtherRecordList(otherIncome);
		cRecordVO.setBaseinfo(productInfo);
		cRecordVO.setRefundInfo(refundList);
		cRecordVO.setActualInList(actualInList);
		cRecordVO.setActualoutList(actualOutList);
		cRecordVO.setInTotal(getTotalCB(actualInList, "1"));
		cRecordVO.setOutTotal(getTotalCB(actualOutList, "2"));
		cRecordVO.setfYlistList(fyList);
		return cRecordVO;
	}

	/**
	 * 预报单预计收款和退款
	 * @param activityId        团期或者产品ID
	 * @param orderType         订单类型
	 * @param groupUUID         团期UUID
	 * @author shijun.liu
	 * @return
	 */
	private List<Map<String, Object>> getOrderAndRefundInfoForForecast(Long activityId, Integer orderType, String groupUUID) {
		List<Map<String, Object>> refundList = null;
		if (orderType == Context.ORDER_TYPE_JP) {
			refundList = iActivityAirTicketService.getOrderAndRefundInfoForcast(activityId, orderType);
		} else if (orderType == Context.ORDER_TYPE_QZ) {
			refundList = iVisaProductsService.getOrderAndRefundInfoForcast(activityId, orderType);
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			refundList = activityIslandGroupService
					.getRefundInfoForcastAndSettle(BUDGET_TYPE, groupUUID, orderType);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			refundList = activityHotelGroupService
					.getRefundInfoForcastAndSettle(BUDGET_TYPE, groupUUID, orderType);
		} else {
			refundList = activityGroupService.getOrderAndRefundInfoForcast(activityId, orderType);
		}
		return refundList;
	}
	
	/**
	 * 结算单预计收款和退款
	 * @param activityId        团期或者产品ID
	 * @param orderType         订单类型
	 * @param groupUUID         团期UUID
	 * @author shijun.liu
	 * @return
	 */
	private List<Map<String, Object>> getOrderAndRefundInfoForSettle(Long activityId, Integer orderType, String groupUUID) {
		List<Map<String, Object>> refundList = null;
		if (orderType == Context.ORDER_TYPE_JP) {
			refundList = iActivityAirTicketService.getOrderAndRefundInfoSettle(activityId, orderType);
		} else if (orderType == Context.ORDER_TYPE_QZ) {
			refundList = iVisaProductsService.getOrderAndRefundInfoSettle(activityId, orderType);
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			refundList = activityIslandGroupService
					.getRefundInfoForcastAndSettle(ACTUAL_TYPE, groupUUID, orderType);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			refundList = activityHotelGroupService
					.getRefundInfoForcastAndSettle(ACTUAL_TYPE, groupUUID, orderType);
		} else {
			refundList = activityGroupService.getOrderAndRefundInfoSettle(activityId, orderType);
		}
		return refundList;
	}

	/**
	 * 查询预报单对应订单类型的产品信息
	 * 
	 * @param orderType          订单类型
	 * @param activityId         产品Id或者团期ID
	 * @param groupUUID          团期UUID
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	private Map<String, Object> getForecastProductInfo(Integer orderType, Long activityId, String groupUUID) {
		Map<String, Object> productInfo = null;
		if (orderType == Context.ORDER_TYPE_JP) {
			productInfo = iActivityAirTicketService.getProductInfoForForcast(activityId).get(0);
		} else if (orderType == Context.ORDER_TYPE_QZ) {
			productInfo = iVisaProductsService.getProductInfoForForcast(activityId).get(0);
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			productInfo = activityIslandGroupService
					.getProductInfoForSettleForcast(groupUUID).get(0);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			productInfo = activityHotelGroupService
					.getProductInfoForSettleForcast(groupUUID).get(0);
		} else {
			productInfo = activityGroupService.getProductInfoForForecast(activityId).get(0);
		}
		return productInfo;
	}
	
	/**
	 * 查询结算单对应订单类型的产品信息
	 * 
	 * @param orderType          订单类型
	 * @param activityId         产品Id或者团期ID
	 * @param groupUUID          团期UUID
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	private Map<String, Object> getSettleProductInfo(Integer orderType, Long activityId, String groupUUID) {
		Map<String, Object> productInfo = null;
		if (orderType == Context.ORDER_TYPE_JP) {
			productInfo = iActivityAirTicketService.getProductInfoForSettle(activityId).get(0);
		} else if (orderType == Context.ORDER_TYPE_QZ) {
			productInfo = iVisaProductsService.getProductInfoForSettle(activityId).get(0);
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			productInfo = activityIslandGroupService
					.getProductInfoForSettleForcast(groupUUID).get(0);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			productInfo = activityHotelGroupService
					.getProductInfoForSettleForcast(groupUUID).get(0);
		} else {
			productInfo = activityGroupService.getProductInfoForSettle(activityId).get(0);
		}
		return productInfo;
	}

	/**
	 * 查询预报单境内/外成本数据
	 * 
	 * @param activityId    团期Id，机票产品Id,签证产品Id
	 * @param groupUUID     团期UUID
	 * @param overseas      0表示境内，1表示境外
	 * @param orderType     订单类型
	 * @return list
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	private List<CostRecord> findCostRecordForcast(Long activityId, String groupUUID,
					Integer overseas, Integer orderType) {
		List<CostRecord> list = new ArrayList<CostRecord>();
		// 成本录入时根据批发商的配置查询相应的数据，0：保存时，1：审核时。具体见C395需求
		// 当配置为1时，把待审核的成本过滤掉
		Integer whenToSheet = UserUtils.getUser().getCompany().getBudgetCostWhenUpdate();//改为了获取新的字段 针对预算成本的 by chy 2016年1月26日13:53:40
		if (orderType < 11) {
            if(0 == overseas.intValue()) {
				//QUAUQ费率
				List<Map<String, String>> quauqAmount = costCommonService.getQuauqServiceAmount1(orderType, activityId);
				//渠道费率
				List<Map<String, String>> agentAmount = costCommonService.getAgentServiceAmount1(orderType, activityId);
				if (!Collections3.isEmpty(quauqAmount)) {
					for (Map<String, String> map : quauqAmount) {
						CostRecord costRecord = map2Object(map);
						list.add(costRecord);
					}
				}

				if (!Collections3.isEmpty(agentAmount)) {
					for (Map<String, String> map : agentAmount) {
						CostRecord costRecord = map2Object(map);
						list.add(costRecord);
					}
				}
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("activityId", activityId);
			params.put("overseas", overseas);
			params.put("orderType", orderType);
			List<Map<String, Object>> mapList = getCostRecordForForecastList(params);
			if (null != mapList && mapList.size() > 0) {
				for (Map<String, Object> map : mapList) {
					CostRecord costRecord = new CostRecord();
				
					Object name = map.get("name") == null ? "" : map.get("name");
					Object supplyName = map.get("supplyName") == null ? "" : map.get("supplyName");
					Object price = map.get("price") == null ? "0.00" : map.get("price");
					Object rate = map.get("rate") == null ? "1" : map.get("rate");
					Object priceAfter = map.get("priceAfter") == null ? "0.00" : map.get("priceAfter");
					Object quantity = map.get("quantity") == null ? "0" : map.get("quantity");
					Object currencyId = map.get("currencyId") == null ? "-1" : map.get("currencyId");
					Object review = map.get("review") == null ? "-1" : map.get("review");
					Object comment = map.get("comment") == null ? "" : map.get("comment");
					
					costRecord.setName(String.valueOf(name));
					costRecord.setSupplyName(String.valueOf(supplyName));
					costRecord.setPrice(new BigDecimal(String.valueOf(price)));
					costRecord.setRate(new BigDecimal(String.valueOf(rate)));
					costRecord.setPriceAfter(new BigDecimal(String.valueOf(priceAfter)));
					costRecord.setQuantity(Integer.parseInt(String.valueOf(quantity)));
					costRecord.setCurrencyId(Integer.parseInt(String.valueOf(currencyId)));
					costRecord.setReview(Integer.parseInt(String.valueOf(review)));
					costRecord.setComment(comment.toString());
					
					list.add(costRecord);
				}
			}
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			List<CostRecordIsland> isLandList = new ArrayList<CostRecordIsland>();
			if (whenToSheet == 1) {
				isLandList = costRecordIslandDao
						.findReviewCostRecordIslandListpass(groupUUID,
								BUDGET_TYPE, overseas, orderType);
			} else {
				isLandList = costRecordIslandDao.findCostRecordIslandListpass(
						groupUUID, BUDGET_TYPE, overseas, orderType);
			}
			for (CostRecordIsland isLangCostRecord : isLandList) {
				CostRecord costRecord = new CostRecord();
				costRecord.setName(isLangCostRecord.getName());
				costRecord.setSupplyName(isLangCostRecord.getSupplyName());
				costRecord.setPrice(isLangCostRecord.getPrice());
				costRecord.setRate(isLangCostRecord.getRate());
				costRecord.setPriceAfter(isLangCostRecord.getPriceAfter());
				costRecord.setQuantity(isLangCostRecord.getQuantity());
				costRecord.setCurrencyId(isLangCostRecord.getCurrencyId());
				costRecord.setReview(isLangCostRecord.getReview());
				list.add(costRecord);
			}
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			List<CostRecordHotel> hotelList = new ArrayList<CostRecordHotel>();
			if (whenToSheet == 1) {
				hotelList = costRecordHotelDao
						.findReviewCostRecordHotelListpass(groupUUID,
								BUDGET_TYPE, overseas, orderType);
			} else {
				hotelList = costRecordHotelDao.findCostRecordHotelListpass(
						groupUUID, BUDGET_TYPE, overseas, orderType);
			}
			for (CostRecordHotel hotelCostRecord : hotelList) {
				CostRecord costRecord = new CostRecord();
				costRecord.setName(hotelCostRecord.getName());
				costRecord.setSupplyName(hotelCostRecord.getSupplyName());
				costRecord.setPrice(hotelCostRecord.getPrice());
				costRecord.setRate(hotelCostRecord.getRate());
				costRecord.setPriceAfter(hotelCostRecord.getPriceAfter());
				costRecord.setQuantity(hotelCostRecord.getQuantity());
				costRecord.setCurrencyId(hotelCostRecord.getCurrencyId());
				costRecord.setReview(hotelCostRecord.getReview());
				list.add(costRecord);
			}
		}
		return list;
	}
	
	/**
	 * 查询结算单境内/外成本数据
	 * 
	 * @param activityId    团期Id，机票产品Id,签证产品Id
	 * @param groupUUID     团期UUID
	 * @param overseas      0表示境内，1表示境外
	 * @param orderType     订单类型
	 * @return list
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	private List<CostRecord> findCostRecordSettle(Long activityId, String groupUUID,
			Integer overseas, Integer orderType) {
		List<CostRecord> list = new ArrayList<CostRecord>();
		// 成本录入时根据批发商的配置查询相应的数据，0：保存时，1：审核时。具体见C395需求
		// 当配置为1时，把待审核的成本过滤掉
		//改为了获取新的字段 针对实际成本的 by chy 2016年1月26日13:53:40
		Integer whenToSheet = UserUtils.getUser().getCompany().getActualCostWhenUpdate();
		if (orderType < 11) {
			//境内
			if(0 == overseas.intValue()) {
				List<Map<String, String>> quauqAmount = costCommonService.getQuauqServiceAmount1(orderType, activityId);
				List<Map<String, String>> agentAmount = costCommonService.getAgentServiceAmount1(orderType, activityId);
				if (!Collections3.isEmpty(quauqAmount)) {
					for (Map<String, String> map : quauqAmount) {
						CostRecord costRecord = map2Object(map);
						list.add(costRecord);
					}
				}

				if (!Collections3.isEmpty(agentAmount)) {
					for (Map<String, String> map : agentAmount) {
						CostRecord costRecord = map2Object(map);
						list.add(costRecord);
					}
				}
			}

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("activityId", activityId);
			params.put("overseas", overseas);
			params.put("orderType", orderType);
			List<Map<String, Object>> mapList = getCostRecordForSettleList(params);
			if (null != mapList && mapList.size() > 0) {
				for (Map<String, Object> map : mapList) {
					CostRecord costRecord = new CostRecord();
					Object name = StringUtils.blankReturnEmpty(map.get("name"));
					Object supplyName = StringUtils.blankReturnEmpty(map.get("supplyName"));
					Object price = map.get("price") == null ? "0.00" : map.get("price");
					Object rate = map.get("rate") == null ? "1" : map.get("rate");
					Object priceAfter = map.get("priceAfter") == null ? "0.00": map.get("priceAfter");
					Object quantity = map.get("quantity") == null ? "0" : map.get("quantity");
					Object currencyId = map.get("currencyId") == null ? "-1" : map.get("currencyId");
					Object review = map.get("review") == null ? "-1" : map.get("review");
					Object payStatus = map.get("payStatus") == null ? "-1" : map.get("payStatus");
					Object comment = map.get("comment") == null ? "" : map.get("comment");
					costRecord.setName(name.toString());
					costRecord.setSupplyName(supplyName.toString());
					costRecord.setPrice(new BigDecimal(price.toString()));
					costRecord.setRate(new BigDecimal(rate.toString()));
					costRecord.setPriceAfter(new BigDecimal(priceAfter.toString()));
					costRecord.setQuantity(Integer.parseInt(quantity.toString()));
					costRecord.setCurrencyId(Integer.parseInt(currencyId.toString()));
					costRecord.setReview(Integer.parseInt(review.toString()));
					costRecord.setPayStatus(Integer.parseInt(payStatus.toString()));
					costRecord.setComment(comment.toString());
					list.add(costRecord);
				}
			}
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			List<CostRecordIsland> isLandList = new ArrayList<CostRecordIsland>();
			if (whenToSheet == 1) {
				isLandList = costRecordIslandDao.findReviewCostRecordIslandListpass(groupUUID,ACTUAL_TYPE, overseas, orderType);
			} else {
				isLandList = costRecordIslandDao.findCostRecordIslandListpass(groupUUID, ACTUAL_TYPE, overseas, orderType);
			}
			for (CostRecordIsland isLangCostRecord : isLandList) {
				CostRecord costRecord = new CostRecord();
				costRecord.setName(isLangCostRecord.getName());
				costRecord.setSupplyName(isLangCostRecord.getSupplyName());
				costRecord.setPrice(isLangCostRecord.getPrice());
				costRecord.setRate(isLangCostRecord.getRate());
				costRecord.setPriceAfter(isLangCostRecord.getPriceAfter());
				costRecord.setQuantity(isLangCostRecord.getQuantity());
				costRecord.setCurrencyId(isLangCostRecord.getCurrencyId());
				costRecord.setReview(isLangCostRecord.getReview());
				list.add(costRecord);
			}
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			List<CostRecordHotel> hotelList = new ArrayList<CostRecordHotel>();
			if (whenToSheet == 1) {
				hotelList = costRecordHotelDao.findReviewCostRecordHotelListpass(groupUUID, ACTUAL_TYPE, overseas, orderType);
			} else {
				hotelList = costRecordHotelDao.findCostRecordHotelListpass(groupUUID, ACTUAL_TYPE, overseas, orderType);
			}
			for (CostRecordHotel hotelCostRecord : hotelList) {
				CostRecord costRecord = new CostRecord();
				costRecord.setName(hotelCostRecord.getName());
				costRecord.setSupplyName(hotelCostRecord.getSupplyName());
				costRecord.setPrice(hotelCostRecord.getPrice());
				costRecord.setRate(hotelCostRecord.getRate());
				costRecord.setPriceAfter(hotelCostRecord.getPriceAfter());
				costRecord.setQuantity(hotelCostRecord.getQuantity());
				costRecord.setCurrencyId(hotelCostRecord.getCurrencyId());
				costRecord.setReview(hotelCostRecord.getReview());
				list.add(costRecord);
			}
		}
		return list;
	}

	/**
	 * 将Map中的数据转换到CostRecord对象中
	 * @param map
	 * @return
	 * @author	shijun.liu
	 * @date	2016.08.15
     */
	private CostRecord map2Object(Map<String, String> map){
		CostRecord costRecord = new CostRecord();
		costRecord.setName(map.get("itemName"));
		costRecord.setSupplyName(map.get("settleName") + "（订单号:"+map.get("orderNum")+"）");
		costRecord.setQuantity(1);
		if(StringUtils.isNotBlank(map.get("amount"))){
			costRecord.setPriceAfter(new BigDecimal(map.get("amount").replace(",","").replace(Context.CURRENCY_MARK_RMB, "")));
			costRecord.setPrice(new BigDecimal(map.get("amount").replace(",","").replace(Context.CURRENCY_MARK_RMB, "")));
		}
		costRecord.setComment("");
		//以下属于额外设置，请忽略
		costRecord.setRate(new BigDecimal(1));
		costRecord.setCurrencyId(-1);
		costRecord.setReview(-1);
		costRecord.setPayStatus(-1);
		return costRecord;
	}


	/**
	 * 查询实际成本的成本数据
	 * @param params
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	private List<Map<String, Object>> getCostRecordForSettleList(Map<String, Object> params) {
		StringBuffer str = new StringBuffer();
		// 成本录入时根据批发商的配置查询相应的数据，0：保存时，1：审核时。具体见C395需求
		// 当配置为1时，把待审核的成本过滤掉
		Integer whenToSheet = UserUtils.getUser().getCompany().getActualCostWhenUpdate();//改为了获取新的字段 针对实际成本的 by chy 2016年1月26日13:53:40
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		str.append(" SELECT ").append("  name,").append("  supplyName,")
			.append("  price,").append("  rate,").append("  priceAfter,")
			.append("  quantity,")
			.append("  currencyId,")
			.append("  review,")
			.append("  payStatus, ")
			.append("  comment ")
			.append(" FROM ")
			.append("  cost_record c ")
			.append(" WHERE ")
			.append("  reviewType = 0 ")// 0表示是成本数据，1：退款，2：反佣
			.append(" AND activityId = ").append(params.get("activityId"))
			.append(" AND budgetType = ").append(ACTUAL_TYPE)
			.append(" AND overseas = ").append(params.get("overseas"))
			.append(" AND orderType = ").append(params.get("orderType"))
			.append(" AND delFlag = ").append(Context.DEL_FLAG_NORMAL)
			.append(" AND c.visaId IS NULL ");
		if (whenToSheet == 1) {// 查询除待提交之外的成本数据，C395 2015.12.21 update by shijun.liu
			str.append(" and review not in(4,5) ");
		} else if (whenToSheet == 2) {// 查询提交了付款申请状态的成本数据， by chy 2016年1月26日13:56:39
			str.append(" and payReview in(1,2) ");//1 审批中 2 审批通过
		}
		// 拉美途批发商，结算单锁定之后录入的预算成本，实际成本，其他收入，退款，反佣不再统计 C457, C486 add by shijun.liu
		// 2015.12.21
		// settle_locked_in == 1 表示是结算单锁定之后录入的数据
		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)) {
			str.append(" and c.settle_locked_in is null ");
		}
		return costRecordDao.findBySql(str.toString(), Map.class);
	}
	
	/**
	 * 查询预算成本的成本数据
	 * @param params
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	private List<Map<String, Object>> getCostRecordForForecastList(Map<String, Object> params) {
		StringBuffer str = new StringBuffer();
		// 成本录入时根据批发商的配置查询相应的数据，0：保存时，1：审核时。具体见C395需求
		// 当配置为1时，把待审核的成本过滤掉
		Integer whenToSheet = UserUtils.getUser().getCompany().getBudgetCostWhenUpdate();//改为了获取新的字段 针对预算成本的 by chy 2016年1月26日13:53:40
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		str.append(" SELECT ").append("  name,").append("  supplyName,")
			.append("  price,").append("  rate,").append("  priceAfter,")
			.append("  quantity,")
			.append("  currencyId,")
			.append("  review, ")
			.append("  comment ")
			.append(" FROM ")
			.append("  cost_record c ")
			.append(" WHERE ")
			.append("  reviewType = 0 ") // 0表示是成本数据，1：退款，2：反佣
			.append(" AND activityId = ").append(params.get("activityId"))
			.append(" AND budgetType = ").append(BUDGET_TYPE)
			.append(" AND overseas = ").append(params.get("overseas"))
			.append(" AND orderType = ").append(params.get("orderType"))
			.append(" AND delFlag = ").append(Context.DEL_FLAG_NORMAL)
			.append(" AND c.visaId IS NULL ");
		if (whenToSheet == 1) {// 查询除待提交之外的成本数据，C395 2015.12.21 update by shijun.liu
			str.append(" and review not in(4,5) ");
		}
		// 拉美途批发商，预报单锁定之后录入的预算成本，实际成本，其他收入，退款，反佣不再统计 C457, C486 add by shijun.liu
		// 2015.12.21
		// forecast_locked_in == 1 表示是预报单锁定之后录入的数据
		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)) {
			str.append(" and c.forecast_locked_in is null ");
		}
		return costRecordDao.findBySql(str.toString(), Map.class);
	}

	/**
	 * 查询预报单，反佣数据
	 * @param activityId	团期ID或者机票产品ID或者签证产品ID
	 * @param orderType		订单类型
	 * @param groupUUID		海岛游或者酒店产品的团期uuid
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	private List<Map<String, Object>> getFYForForecast(Long activityId, Integer orderType, String groupUUID) {
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer str = new StringBuffer();
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append("select supplyName, sum(price) price, count(*) as count from cost_record_island cr ")
					.append(" WHERE cr.delFlag = '0' AND cr.reviewType=2 AND cr.budgetType= ")
					.append(BUDGET_TYPE).append(" AND cr.activity_uuid = '")
					.append(groupUUID).append("' AND cr.orderType=")
					.append(orderType);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append("select supplyName, sum(price) price, count(*) as count from cost_record_hotel cr ")
					.append(" WHERE cr.delFlag = '0' AND cr.reviewType=2 AND cr.budgetType= ")
					.append(BUDGET_TYPE).append(" AND cr.activity_uuid = '")
					.append(groupUUID).append("' AND cr.orderType=")
					.append(orderType);
		} else {
			str.append("select supplyName, sum(price) price, count(*) as count from cost_record cr ")
					.append(" WHERE cr.delFlag = '0' AND cr.reviewType=2 AND cr.budgetType= ")
					.append(BUDGET_TYPE).append(" AND cr.activityId = ")
					.append(activityId).append(" AND cr.orderType=")
					.append(orderType);
			// 拉美途 过滤预报单锁定之后生成的反佣数据 C457  add by shijun.liu 2015.12.21
			// forecast_locked_in = 1 表示是预报单锁定之后生成的数据
			if (Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)) {
				str.append(" AND cr.forecast_locked_in is null ");
			}
		}
		str.append(" group by supplyName ");
		return costRecordDao.findCostRecordBySql(str.toString());
	}
	
	/**
	 * 查询结算单，反佣数据
	 * @param activityId	团期ID或者机票产品ID或者签证产品ID
	 * @param orderType		订单类型
	 * @param groupUUID		海岛游或者酒店产品的团期uuid
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.25
	 */
	private List<Map<String, Object>> getFYForSettle(Long activityId, Integer orderType, String groupUUID) {
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer str = new StringBuffer();
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append("select supplyName, sum(price) price, count(*) as count from cost_record_island cr ")
					.append(" WHERE cr.delFlag = '0' AND cr.reviewType=2 AND cr.budgetType= ")
					.append(ACTUAL_TYPE).append(" AND cr.activity_uuid = '")
					.append(groupUUID).append("' AND cr.orderType=")
					.append(orderType);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append("select supplyName, sum(price) price, count(*) as count from cost_record_hotel cr ")
					.append(" WHERE cr.delFlag = '0' AND cr.reviewType=2 AND cr.budgetType= ")
					.append(ACTUAL_TYPE).append(" AND cr.activity_uuid = '")
					.append(groupUUID).append("' AND cr.orderType=")
					.append(orderType);
		} else {
			str.append("select supplyName, sum(price) price, count(*) as count from cost_record cr ")
					.append(" WHERE cr.delFlag = '0' AND cr.reviewType=2 AND cr.budgetType= ")
					.append(ACTUAL_TYPE).append(" AND cr.activityId = ")
					.append(activityId).append(" AND cr.orderType=")
					.append(orderType);
			// 拉美途 过滤结算单锁定之后生成的反佣数据 C486 add by shijun.liu 2015.12.21
			// settle_locked_in = 1 表示是结算单锁定之后生成的数据
			if (Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)) {
				str.append(" AND cr.settle_locked_in is null ");
			}
		}
		//start 136 & 135 需求 拉美途要求返佣、退款审批通过后再计入结算单； by chy 2016年1月22日20:28:45
		String uuid = UserUtils.getUser().getCompany().getUuid();
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(uuid)){//如果是拉美途 只有审批通过的才计入结算单
			str.append(" AND cr.reviewStatus in ('审批通过', '审核通过') group by supplyName ");
		} else {
			str.append(" AND cr.reviewStatus not in ('已取消','已驳回') group by supplyName ");
		}
		//end
		return costRecordDao.findCostRecordBySql(str.toString());
	}

	/**
	 * 查询每个团期或者产品其他收入数据信息
	 * 
	 * @param activityId     产品或者团期ID
	 * @param orderType      订单类型
	 * @param groupUUID      团期ID
	 * @author shijun.liu
	 * @return
	 */
	private List<Map<String, Object>> getOtherIncomeForForecast(Long activityId, Integer orderType, String groupUUID) {
		StringBuffer str = new StringBuffer();
		String lockedIn = "";
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		// 拉美途 过滤预报单锁定之后生成的其他收入数据 C457 add by shijun.liu 2015.12.21
		// forecast_locked_in = 1 表示是预报单锁定之后生成的数据
		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)) {
			lockedIn = " AND c.forecast_locked_in is null ";
		}
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append("SELECT (SELECT NAME FROM sys_user WHERE id = c.createBy) AS saler,'其他' as agentName,")
					.append(" priceAfter as totalMoney, IFNULL(pay.accountedMoney, '0') as accountedMoney ")
					.append(" FROM cost_record_hotel c LEFT JOIN ( SELECT g.cost_record_id, g.orderType, ")
					.append(" ROUND(SUM(m.amount * m.exchangerate),2) AS accountedMoney FROM pay_group g,")
					.append(" money_amount m WHERE g.isAsAccount = 1 AND m.serialNum = g.payPrice ")
					.append(" GROUP BY g.cost_record_id, g.orderType ) pay ")
					.append(" ON pay.cost_record_id = c.id AND c.orderType = pay.orderType ")
					.append(" WHERE c.delFlag = ")
					.append(Context.DEL_FLAG_NORMAL).append(" AND ")
					.append(" c.budgetType = 2 AND c.orderType = ")
					.append(orderType).append(" AND c.activity_uuid = '")
					.append(groupUUID).append("'");
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append("SELECT (SELECT NAME FROM sys_user WHERE id = c.createBy) AS saler,'其他' as agentName,")
					.append(" priceAfter as totalMoney, IFNULL(pay.accountedMoney, '0') as accountedMoney ")
					.append(" FROM cost_record_island c LEFT JOIN ( SELECT g.cost_record_id, g.orderType, ")
					.append(" ROUND(SUM(m.amount * m.exchangerate),2) AS accountedMoney FROM pay_group g,")
					.append(" money_amount m WHERE g.isAsAccount = 1 AND m.serialNum = g.payPrice ")
					.append(" GROUP BY g.cost_record_id, g.orderType ) pay")
					.append(" ON pay.cost_record_id = c.id AND c.orderType = pay.orderType ")
					.append(" WHERE c.delFlag = ")
					.append(Context.DEL_FLAG_NORMAL).append(" AND ")
					.append(" c.budgetType = 2 AND c.orderType = ")
					.append(orderType).append(" AND c.activity_uuid = '")
					.append(groupUUID).append("'");
		} else {
			str.append("SELECT (SELECT NAME FROM sys_user WHERE id = c.createBy) AS saler, ");
			if (Context.SUPPLIER_UUID_LZJQ.equals(currentCompanyUuid)) {
				str.append(" c.supplyName as agentName, ");
			} else {
				str.append(" '其他' as agentName, ");
			}
			str.append(" priceAfter as totalMoney, IFNULL(pay.accountedMoney, '0') as accountedMoney,c.name,c.comment ")
			   .append(" FROM cost_record c LEFT JOIN ( SELECT g.cost_record_id, g.orderType, ")
			   .append(" ROUND(SUM(m.amount * m.exchangerate),2) AS accountedMoney FROM pay_group g,cost_record c,")
			   .append(" money_amount m WHERE g.isAsAccount = 1 AND m.serialNum = g.payPrice AND c.id = g.cost_record_id ")
			   .append(" GROUP BY g.cost_record_id, g.orderType ) pay")
			   .append(" ON pay.cost_record_id = c.id AND c.orderType = pay.orderType ")
			   .append(" WHERE c.delFlag = ")
			   .append(Context.DEL_FLAG_NORMAL).append(" AND ")
			   .append(" c.budgetType = 2 AND c.orderType = ")
			   .append(orderType).append(" AND c.activityId = ")
			   .append(activityId).append(lockedIn);
		}
		List<Map<String, Object>> accountEdMoney = costRecordDao.findBySql(str.toString(), Map.class);
		return accountEdMoney;
	}
	
	/**
	 * 查询结算单中每个团期或者产品其他收入数据信息 
	 * 
	 * @param activityId     产品或者团期ID
	 * @param orderType      订单类型
	 * @param groupUUID      团期ID
	 * @author shijun.liu
	 * @return
	 */
	private List<Map<String, Object>> getOtherIncomeForSettle(Long activityId, Integer orderType, String groupUUID) {
		StringBuffer str = new StringBuffer();
		String lockedIn = "";
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		// 拉美途 过滤结算单锁定之后生成的其他收入数据 C486 add by shijun.liu 2015.12.21
		// settle_locked_in = 1 表示是结算单锁定之后生成的数据
		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)) {
			lockedIn = " AND c.settle_locked_in is null ";
		}
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append("SELECT (SELECT NAME FROM sys_user WHERE id = c.createBy) AS saler,'其他' as agentName,")
					.append(" priceAfter as totalMoney, IFNULL(pay.accountedMoney, '0') as accountedMoney ")
					.append(" FROM cost_record_hotel c LEFT JOIN ( SELECT g.cost_record_id, g.orderType, ")
					.append(" ROUND(SUM(m.amount * m.exchangerate),2) AS accountedMoney FROM pay_group g,")
					.append(" money_amount m WHERE g.isAsAccount = 1 AND m.serialNum = g.payPrice ")
					.append(" GROUP BY g.cost_record_id, g.orderType ) pay ")
					.append(" ON pay.cost_record_id = c.id AND c.orderType = pay.orderType ")
					.append(" WHERE c.delFlag = ")
					.append(Context.DEL_FLAG_NORMAL).append(" AND ")
					.append(" c.budgetType = 2 AND c.orderType = ")
					.append(orderType).append(" AND c.activity_uuid = '")
					.append(groupUUID).append("'");
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append("SELECT (SELECT NAME FROM sys_user WHERE id = c.createBy) AS saler,'其他' as agentName,")
					.append(" priceAfter as totalMoney, IFNULL(pay.accountedMoney, '0') as accountedMoney ")
					.append(" FROM cost_record_island c LEFT JOIN ( SELECT g.cost_record_id, g.orderType, ")
					.append(" ROUND(SUM(m.amount * m.exchangerate),2) AS accountedMoney FROM pay_group g,")
					.append(" money_amount m WHERE g.isAsAccount = 1 AND m.serialNum = g.payPrice ")
					.append(" GROUP BY g.cost_record_id, g.orderType ) pay")
					.append(" ON pay.cost_record_id = c.id AND c.orderType = pay.orderType ")
					.append(" WHERE c.delFlag = ")
					.append(Context.DEL_FLAG_NORMAL).append(" AND ")
					.append(" c.budgetType = 2 AND c.orderType = ")
					.append(orderType).append(" AND c.activity_uuid = '")
					.append(groupUUID).append("'");
		} else {
			str.append("SELECT (SELECT NAME FROM sys_user WHERE id = c.createBy) AS saler,'其他' as agentName,")
					.append("supplyName,c.name,c.comment,")
					.append(" priceAfter as totalMoney, IFNULL(pay.accountedMoney, '0') as accountedMoney ")
					.append(" FROM cost_record c LEFT JOIN ( SELECT g.cost_record_id, g.orderType, ")
					.append(" ROUND(SUM(m.amount * m.exchangerate),2) AS accountedMoney FROM pay_group g,cost_record c,")
					.append(" money_amount m WHERE g.isAsAccount = 1 AND m.serialNum = g.payPrice AND g.cost_record_id = c.id ")
					.append(" GROUP BY g.cost_record_id, g.orderType ) pay")
					.append(" ON pay.cost_record_id = c.id AND c.orderType = pay.orderType ")
					.append(" WHERE c.delFlag = ")
					.append(Context.DEL_FLAG_NORMAL).append(" AND ")
					.append(" c.budgetType = 2 AND c.orderType = ")
					.append(orderType).append(" AND c.activityId = ")
					.append(activityId).append(lockedIn);
		}
		List<Map<String, Object>> accountEdMoney = costRecordDao.findBySql(str.toString(), Map.class);
		return accountEdMoney;
	}

	/**
	 * 根据用户所在部门查询其主管,如果多个主管则最多只显示两个，其他为省略号
	 * 
	 * @param userId
	 *            用户ID
	 * @param orderType
	 *            产品类型
	 * @param jobType
	 *            job类型
	 * @author shijun.liu
	 * @return
	 */
	private Map<String,String> queryLeader(Integer userId, Integer orderType,
			Integer jobType) {
		StringBuffer str = new StringBuffer();
		StringBuffer leader = new StringBuffer();
		StringBuffer shortLeader = new StringBuffer();
		Map<String,String> map = new HashMap<>();//map存放全写和简写leaderStr。
		Long companyId = UserUtils.getUser().getCompany().getId();
		str.append("SELECT su.* FROM sys_user su WHERE EXISTS ")
				.append(" ( SELECT user_id FROM sys_user_dept_job job WHERE")
				.append(" job_id IN (SELECT id AS jobId FROM sys_job WHERE jobType = ")
				.append(jobType)
				.append(" AND orderType = ")
				.append(orderType)
				.append(")")
				.append(" AND job.user_id = su.id ")
				.append(" AND EXISTS (SELECT t.dept_id FROM sys_user_dept_job t WHERE user_id = ")
				.append(userId).append(" AND job.dept_id = t.dept_id))")
				.append(" AND su.companyId = ").append(companyId)
				.append(" AND su.delFlag = '0' ");
		List<User> userLeader = costRecordDao.findBySql(str.toString(),User.class);
		for (User user : userLeader) {
			leader.append(user.getName()).append(",");
		}
		if (leader.length() > 1) {
			leader.delete(leader.length() - 1, leader.length());
			String[] array = leader.toString().split(",");
			int len = array.length;
			if (len > 2) {
				shortLeader.append(array[0]).append(",").append(array[1])
						.append("...");
			} else {
				shortLeader.append(leader.toString());
			}
		}
		map.put("leader",leader.toString());
		map.put("shortLeader",shortLeader.toString());
		return map;
	}

	/**
	 * 成本总合计
	 * 
	 * @param list
	 * @param flag
	 *            1表示境内预计成本和实际成本的总和 2表示境外预计成本和实际成本转换成人民币后的总和
	 * */
	private String getTotalCB(List<CostRecord> list, String flag) {
		BigDecimal bigDecimal = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(list)) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				if ("1".equals(flag)) {
					if (list.get(i).getPriceAfter() != null) {
						bigDecimal = bigDecimal
								.add(list.get(i).getPriceAfter());
					}
				} else if ("2".equals(flag)) {
					if (list.get(i).getPriceAfter() != null) {
						bigDecimal = bigDecimal
								.add(list.get(i).getPriceAfter());
					}
				}
			}
		}
		return bigDecimal.toString();
	}

	

	/**
	 * 退款申请添加成本记录
	 * 
	 * @param orderType
	 *            订单类型：1 单团；2 散拼；6 签证；7 机票
	 * @param bean
	 *            退款实体
	 *            订单实体
	 * @param result
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveRefundCostRecord(Integer orderType, RefundBean bean,
			Object genericsOrder, String agentName, Long result, Long deptId,
			boolean yubao_locked, boolean jiesuan_locked) {

		if (!(yubao_locked && jiesuan_locked)) {
			Long orderId = 0L; // 订单ID
			Long activityId = 0L; // 产品ID
			Long agentId = 0L; // 渠道ID
			String totalMoneySer = ""; // 应收金额UUID

			// 机票
			if (Context.ORDER_TYPE_JP == orderType) {
				AirticketOrder airOrder = (AirticketOrder) genericsOrder;
				orderId = airOrder.getId();
				activityId = airOrder.getAirticketId();
				agentId = airOrder.getAgentinfoId();
				totalMoneySer = airOrder.getTotalMoney();
			}
			// 汇率查询：订单应付金额中如有此币种则获取对应汇率，否则查询基础信息表中汇率
			BigDecimal exchangeRate = new BigDecimal(0);
			// 退款金额
			BigDecimal price = new BigDecimal(0);
			// 退款人民币币种
			Long rmbCurrencyId = 0L;
			if (rmbCurrencyId == 0) {
				Currency rmbCurrency = currencyService.getRMBCurrencyId();
				if (rmbCurrency != null) {
					rmbCurrencyId = rmbCurrency.getId();
				} else {
					throw new IllegalArgumentException("此供应商没有人民币币种");
				}
			}

			// 如果退款金额为空则回滚
			if (StringUtils.isNotBlank(bean.getRefundPrice())) {
				price = new BigDecimal(bean.getRefundPrice());
			} else {
				throw new IllegalArgumentException("退款金额异常");
			}

			// 如果汇率为空则回滚
			if (StringUtils.isNotBlank(bean.getCurrencyId())) {
				exchangeRate = moneyAmountService.getExchangerateByUuid(
						totalMoneySer, Integer.valueOf(bean.getCurrencyId()));
				if (exchangeRate != null) {
					price = price.multiply(exchangeRate);
				} else {
					throw new IllegalArgumentException("汇率异常");
				}
			} else {
				throw new IllegalArgumentException("币种异常");
			}

			CostRecord yubao_cost = new CostRecord();
			yubao_cost.setActivityId(activityId);
			yubao_cost.setOrderType(orderType); // 订单类型：6 签证；7机票；2散拼；1单团
			yubao_cost.setBudgetType(0); // 0为预算价；1为成本价；2财务成本预算
			yubao_cost.setCurrencyId(rmbCurrencyId.intValue()); // 退款人民币币种id
			yubao_cost.setPrice(price); // 成本单价=退款金额*币种对人民币的汇率
			yubao_cost.setQuantity(1); // 数量默认1
			yubao_cost.setSupplyType(1); // 渠道商
			yubao_cost.setSupplyName(agentName);// 渠道商名称
			yubao_cost.setOverseas(0); // 1:国外,0:国内
			yubao_cost.setNowLevel(1); // 当前审核层级
			yubao_cost.setReview(5); // 审核状态:
										// 0未通过,1待审核,2已通过,3：操作完成，4：待提交，5：不需要审核
			yubao_cost.setSupplyId(agentId.intValue()); // 渠道商ID
			yubao_cost.setName("退款");
			yubao_cost.setCreateBy(UserUtils.getUser());
			yubao_cost.setCreateDate(new Date());
			yubao_cost.setReviewType(1); // 1： 退款流程 2：返佣
			yubao_cost.setOrderId(orderId);// 订单Id
			yubao_cost.setComment(bean.getRemark());// 备注
			yubao_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			yubao_cost.setReviewId(result);
			yubao_cost.setReviewCompanyId(reviewDao.findOne(result)
					.getReviewCompanyId());
			yubao_cost.setDelFlag("0");
			yubao_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			BigDecimal bd = new BigDecimal(1);
			yubao_cost.setRate(bd);

			CostRecord jiesuan_cost = new CostRecord();
			BeanUtils.copyProperties(yubao_cost, jiesuan_cost);
			jiesuan_cost.setId(null);
			jiesuan_cost.setBudgetType(1);
			jiesuan_cost.setReview(2);
			jiesuan_cost.setNowLevel(4);
			jiesuan_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			jiesuan_cost.setPayNowLevel(1);
			jiesuan_cost.setPayReview(4);
			jiesuan_cost.setDelFlag("0");
			jiesuan_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			jiesuan_cost.setRate(bd);

//			if (!yubao_locked) {
//				saveCostRecord(yubao_cost);
//			}
//			if (!jiesuan_locked) {
//				saveCostRecord(jiesuan_cost);
//			}
			saveCostRecord(yubao_cost);
			saveCostRecord(jiesuan_cost);
		}
	}

	/**
	 * 海岛游退款申请添加成本记录 add by ruyi.chen add date 2015-06-18
	 * 
	 * @param orderType
	 *            订单类型：12 海岛游
	 * @param bean
	 *            退款实体
	 *            订单实体
	 * @param result
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveIslandRefundCostRecord(Integer orderType, RefundBean bean,
			Object genericsOrder, String agentName, Long result, Long deptId,
			boolean yubao_locked, boolean jiesuan_locked) {

		if (!(yubao_locked && jiesuan_locked)) {
			// Long orderId = 0L; //订单ID
			String activityUuid = ""; // 产品ID
			Long agentId = 0L; // 渠道ID
			String totalMoneySer = ""; // 应收金额UUID

			IslandOrder islandOrder = (IslandOrder) genericsOrder;
			// orderId = islandOrder.getId().longValue();
			agentId = islandOrder.getOrderCompany().longValue();
			totalMoneySer = islandOrder.getTotalMoney();
			activityUuid = activityIslandDao.getByUuid(
					islandOrder.getActivityIslandUuid()).getUuid();

			// 汇率查询：订单应付金额中如有此币种则获取对应汇率，否则查询基础信息表中汇率
			// BigDecimal exchangeRate = new BigDecimal(0);
			// 退款金额
			BigDecimal price = new BigDecimal(0);
			// 退款人民币币种
			Long rmbCurrencyId = 0L;
			if (rmbCurrencyId == 0) {
				Currency rmbCurrency = currencyService.getRMBCurrencyId();
				if (rmbCurrency != null) {
					rmbCurrencyId = rmbCurrency.getId();
				} else {
					throw new IllegalArgumentException("此供应商没有人民币币种");
				}
			}

			// 如果退款金额为空则回滚
			if (StringUtils.isNotBlank(bean.getRefundPrice())) {
				price = new BigDecimal(bean.getRefundPrice());
			} else {
				throw new IllegalArgumentException("退款金额异常");
			}

			// 如果汇率为空则回滚
			if (StringUtils.isNotBlank(bean.getCurrencyId())) {
				Double islandExchangeRete = islandMoneyAmountService
						.getExchangerateByUuid(totalMoneySer,
								Integer.valueOf(bean.getCurrencyId()));
				if (islandExchangeRete != null) {
					price = price.multiply(BigDecimal
							.valueOf(islandExchangeRete));
				} else {
					throw new IllegalArgumentException("汇率异常");
				}
			} else {
				throw new IllegalArgumentException("币种异常");
			}

			CostRecordIsland yubao_cost = new CostRecordIsland();
			yubao_cost.setUuid(UuidUtils.generUuid());
			yubao_cost.setActivityUuid(activityUuid);
			yubao_cost.setOrderType(orderType); // 订单类型：6 签证；7机票；2散拼；1单团
			yubao_cost.setBudgetType(0); // 0为预算价；1为成本价；2财务成本预算
			yubao_cost.setCurrencyId(rmbCurrencyId.intValue()); // 退款人民币币种id
			yubao_cost.setPrice(price); // 成本单价=退款金额*币种对人民币的汇率
			yubao_cost.setQuantity(1); // 数量默认1
			yubao_cost.setSupplyType(1); // 渠道商
			yubao_cost.setSupplyName(agentName);// 渠道商名称
			yubao_cost.setOverseas(0); // 1:国外,0:国内
			yubao_cost.setNowLevel(1); // 当前审核层级
			yubao_cost.setReview(5); // 审核状态:
										// 0未通过,1待审核,2已通过,3：操作完成，4：待提交，5：不需要审核
			yubao_cost.setSupplyId(agentId.intValue()); // 渠道商ID
			// yubao_cost.setSupplyName(bean.get);
			yubao_cost.setName("退款");
			yubao_cost.setCreateBy(UserUtils.getUser());
			yubao_cost.setCreateDate(new Date());
			yubao_cost.setReviewType(1); // 1： 退款流程 2：返佣
			// yubao_cost.setOrderId(orderId);//订单Id
			yubao_cost.setComment(bean.getRemark());// 备注
			yubao_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			yubao_cost.setReviewId(result);
			yubao_cost.setReviewCompanyId(reviewDao.findOne(result)
					.getReviewCompanyId());
			yubao_cost.setDelFlag("0");
			yubao_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			BigDecimal bd = new BigDecimal(1);
			yubao_cost.setRate(bd);

			CostRecordIsland jiesuan_cost = new CostRecordIsland();
			BeanUtils.copyProperties(yubao_cost, jiesuan_cost);
			jiesuan_cost.setId(null);
			jiesuan_cost.setUuid(UuidUtils.generUuid());
			jiesuan_cost.setBudgetType(1);
			jiesuan_cost.setReview(2);
			jiesuan_cost.setNowLevel(4);
			jiesuan_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			jiesuan_cost.setPayNowLevel(1);
			jiesuan_cost.setPayReview(4);
			jiesuan_cost.setDelFlag("0");
			jiesuan_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			jiesuan_cost.setRate(bd);

			if (!yubao_locked) {
				saveCostRecordIsland(yubao_cost);
			}
			if (!jiesuan_locked) {
				saveCostRecordIsland(jiesuan_cost);
			}
		}
	}

	/**
	 * 酒店退款申请添加成本记录 add by ruyi.chen add date 2015-06-18
	 * 
	 * @param orderType
	 *            订单类型：12 海岛游
	 * @param bean
	 *            退款实体
	 *            订单实体
	 * @param result
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveHotelRefundCostRecord(Integer orderType, RefundBean bean,
			Object genericsOrder, String agentName, Long result, Long deptId,
			boolean yubao_locked, boolean jiesuan_locked) {

		if (!(yubao_locked && jiesuan_locked)) {
			Long orderId = 0L; // 订单ID
			Long activityId = 0L; // 产品ID
			Long agentId = 0L; // 渠道ID
			String totalMoneySer = ""; // 应收金额UUID
			HotelOrder order = (HotelOrder) genericsOrder;
			orderId = order.getId().longValue();
			agentId = order.getOrderCompany().longValue();
			totalMoneySer = order.getTotalMoney();
			activityId = activityHotelDao
					.getByUuid(order.getActivityHotelUuid()).getId()
					.longValue();

			// 汇率查询：订单应付金额中如有此币种则获取对应汇率，否则查询基础信息表中汇率
			// BigDecimal exchangeRate = new BigDecimal(0);
			// 退款金额
			BigDecimal price = new BigDecimal(0);
			// 退款人民币币种
			Long rmbCurrencyId = 0L;
			if (rmbCurrencyId == 0) {
				Currency rmbCurrency = currencyService.getRMBCurrencyId();
				if (rmbCurrency != null) {
					rmbCurrencyId = rmbCurrency.getId();
				} else {
					throw new IllegalArgumentException("此供应商没有人民币币种");
				}
			}

			// 如果退款金额为空则回滚
			if (StringUtils.isNotBlank(bean.getRefundPrice())) {
				price = new BigDecimal(bean.getRefundPrice());
			} else {
				throw new IllegalArgumentException("退款金额异常");
			}

			// 如果汇率为空则回滚
			if (StringUtils.isNotBlank(bean.getCurrencyId())) {
				Double islandExchangeRete = hotelMoneyAmountService
						.getExchangerateByUuid(totalMoneySer,
								Integer.valueOf(bean.getCurrencyId()));
				if (islandExchangeRete != null) {
					price = price.multiply(BigDecimal
							.valueOf(islandExchangeRete));
				} else {
					throw new IllegalArgumentException("汇率异常");
				}
			} else {
				throw new IllegalArgumentException("币种异常");
			}

			CostRecord yubao_cost = new CostRecord();
			yubao_cost.setActivityId(activityId);
			yubao_cost.setOrderType(orderType); // 订单类型：6 签证；7机票；2散拼；1单团
			yubao_cost.setBudgetType(0); // 0为预算价；1为成本价；2财务成本预算
			yubao_cost.setCurrencyId(rmbCurrencyId.intValue()); // 退款人民币币种id
			yubao_cost.setPrice(price); // 成本单价=退款金额*币种对人民币的汇率
			yubao_cost.setQuantity(1); // 数量默认1
			yubao_cost.setSupplyType(1); // 渠道商
			yubao_cost.setSupplyName(agentName);// 渠道商名称
			yubao_cost.setOverseas(0); // 1:国外,0:国内
			yubao_cost.setNowLevel(1); // 当前审核层级
			yubao_cost.setReview(5); // 审核状态:
										// 0未通过,1待审核,2已通过,3：操作完成，4：待提交，5：不需要审核
			yubao_cost.setSupplyId(agentId.intValue()); // 渠道商ID
			// yubao_cost.setSupplyName(bean.get);
			yubao_cost.setName("退款");
			yubao_cost.setCreateBy(UserUtils.getUser());
			yubao_cost.setCreateDate(new Date());
			yubao_cost.setReviewType(1); // 1： 退款流程 2：返佣
			yubao_cost.setOrderId(orderId);// 订单Id
			yubao_cost.setComment(bean.getRemark());// 备注
			yubao_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			yubao_cost.setReviewId(result);
			yubao_cost.setReviewCompanyId(reviewDao.findOne(result)
					.getReviewCompanyId());
			yubao_cost.setDelFlag("0");
			yubao_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			BigDecimal bd = new BigDecimal(1);
			yubao_cost.setRate(bd);

			CostRecord jiesuan_cost = new CostRecord();
			BeanUtils.copyProperties(yubao_cost, jiesuan_cost);
			jiesuan_cost.setId(null);
			jiesuan_cost.setBudgetType(1);
			jiesuan_cost.setReview(2);
			jiesuan_cost.setNowLevel(4);
			jiesuan_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			jiesuan_cost.setPayNowLevel(1);
			jiesuan_cost.setPayReview(4);
			jiesuan_cost.setDelFlag("0");
			jiesuan_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			jiesuan_cost.setRate(bd);

//			if (!yubao_locked) {
//				saveCostRecord(yubao_cost);
//			}
//			if (!jiesuan_locked) {
//				saveCostRecord(jiesuan_cost);
//			}

			saveCostRecord(yubao_cost);
			saveCostRecord(jiesuan_cost);
		}
	}

	/**
	 * 退款申请添加成本记录（对应新的审核流程）
	 * 
	 *            返佣实体
	 * @param genericsOrder
	 *            订单实体
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveRefundCostRecordNew(ReviewNew reviewInfo, RefundBean bean,
			Object genericsOrder, boolean yubao_locked, boolean jiesuan_locked) {

		Integer orderType = Integer.parseInt(reviewInfo.getProductType());
		Long agentId = Long.parseLong(reviewInfo.getAgent());
		String agentName = reviewInfo.getAgentName();
		Long companyId = UserUtils.getUser().getCompany().getId();

		Long orderId = 0L; // 订单ID
		Long activityId = 0L; // 产品ID
		String totalMoneySer = ""; // 应收金额UUID
	
		// 机票
		if (Context.ORDER_TYPE_JP == orderType) {
			AirticketOrder airOrder = (AirticketOrder) genericsOrder;
			orderId = airOrder.getId();
			activityId = airOrder.getAirticketId();
			agentId = airOrder.getAgentinfoId();
			totalMoneySer = airOrder.getTotalMoney();
		} 
		//签证  合并时分支无此代码
		else if(Context.ORDER_TYPE_QZ == orderType) {
			VisaOrder visaOrder = (VisaOrder) genericsOrder;
			orderId = visaOrder.getId();
			activityId = visaOrder.getVisaProductId();
			agentId = visaOrder.getAgentinfoId();
			totalMoneySer = visaOrder.getTotalMoney();
		} 
		//单团   合并时分支无此代码
		else {
			ProductOrderCommon order = (ProductOrderCommon) genericsOrder; 
			orderId = order.getId();
			activityId = order.getProductGroupId();
			agentId = order.getOrderCompany();
			totalMoneySer = order.getTotalMoney();
		}
		//汇率查询：订单应付金额中如有此币种则获取对应汇率，否则查询基础信息表中汇率
		BigDecimal exchangeRate = new BigDecimal(0);
		// 退款金额
		BigDecimal price = new BigDecimal(0);
		// 退款人民币币种
		Long rmbCurrencyId = 0L;
		if (rmbCurrencyId == 0) {
			Currency rmbCurrency = currencyService.getRMBCurrencyId();
			if (rmbCurrency != null) {
				rmbCurrencyId = rmbCurrency.getId();
			} else {
				throw new IllegalArgumentException("此供应商没有人民币币种");
			}
		}
	
		// 如果退款金额为空则回滚
		if (StringUtils.isNotBlank(bean.getRefundPrice())) {
			price = new BigDecimal(bean.getRefundPrice());
		} else {
			throw new IllegalArgumentException("退款金额异常");
		}
	
		// 如果汇率为空则回滚
		if (StringUtils.isNotBlank(bean.getCurrencyId())) {
			exchangeRate = moneyAmountService.getExchangerateByUuid(
					totalMoneySer, Integer.valueOf(bean.getCurrencyId()));
			if (exchangeRate != null) {
				price = price.multiply(exchangeRate);
			} else {
				throw new IllegalArgumentException("汇率异常");
			}
		} else {
			throw new IllegalArgumentException("币种异常");
		}
	
		CostRecord yubao_cost = new CostRecord();
		yubao_cost.setActivityId(activityId);
		yubao_cost.setOrderType(orderType); // 订单类型：6 签证；7机票；2散拼；1单团
		yubao_cost.setBudgetType(0); // 0为预算价；1为成本价；2财务成本预算
		yubao_cost.setCurrencyId(rmbCurrencyId.intValue()); // 退款人民币币种id
		yubao_cost.setPrice(price); // 成本单价=退款金额*币种对人民币的汇率
		yubao_cost.setQuantity(1); // 数量默认1
		yubao_cost.setSupplyType(1); // 渠道商
		yubao_cost.setSupplyName(agentName);// 渠道商名称
		yubao_cost.setOverseas(0); // 1:国外,0:国内
		yubao_cost.setNowLevel(1); // 当前审核层级
		yubao_cost.setReview(5); // 审核状态:
									// 0未通过,1待审核,2已通过,3：操作完成，4：待提交，5：不需要审核
		yubao_cost.setSupplyId(agentId.intValue()); // 渠道商ID
		yubao_cost.setName("退款");
		yubao_cost.setCreateBy(UserUtils.getUser());
		yubao_cost.setCreateDate(new Date());
		yubao_cost.setReviewType(1); // 1： 退款流程 2：返佣
		yubao_cost.setOrderId(orderId);// 订单Id
		yubao_cost.setComment(bean.getRemark());// 备注
		yubao_cost.setReviewStatus(ReviewUtils
				.getChineseReviewStatusByUuid(reviewInfo.getId()));
		yubao_cost.setReviewUuid(reviewInfo.getId());
		yubao_cost.setReviewCompanyId(companyId);
		yubao_cost.setDelFlag("0");
		yubao_cost.setCurrencyAfter(rmbCurrencyId.intValue());
		yubao_cost.setIsNew(2);
		yubao_cost.setUuid(UuidUtils.generUuid());
		BigDecimal bd = new BigDecimal(1);
		yubao_cost.setRate(bd);
	
		CostRecord jiesuan_cost = new CostRecord();
		BeanUtils.copyProperties(yubao_cost, jiesuan_cost);
		jiesuan_cost.setId(null);
		jiesuan_cost.setBudgetType(1);
		jiesuan_cost.setReview(2);
		jiesuan_cost.setNowLevel(4);
		jiesuan_cost.setReviewStatus(ReviewUtils
				.getChineseReviewStatusByUuid(reviewInfo.getId()));
		jiesuan_cost.setPayNowLevel(1);
		jiesuan_cost.setPayReview(4);
		jiesuan_cost.setDelFlag("0");
		jiesuan_cost.setCurrencyAfter(rmbCurrencyId.intValue());
		jiesuan_cost.setRate(bd);
		jiesuan_cost.setIsNew(2);
		jiesuan_cost.setUuid(UuidUtils.generUuid());
		
//		String companyUuid = UserUtils.getUser().getCompany().getUuid();
//		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
//			if (yubao_locked) {
//				jiesuan_cost.setForecastLockedIn("1");
//			}
//			saveCostRecord(yubao_cost);
//			if (jiesuan_locked) {
//				yubao_cost.setSettleLockedIn("1");
//			}
//			saveCostRecord(jiesuan_cost);
//		} else {
//			if (!yubao_locked) {
//				saveCostRecord(yubao_cost);
//			}
//			if (!jiesuan_locked) {
//				saveCostRecord(jiesuan_cost);
//			}
//		}
		saveCostRecord(yubao_cost);
		saveCostRecord(jiesuan_cost);
	}

	/**
	 * 返佣申请添加成本记录（对应新的审核流程）
	 *
	 *            订单类型：1 单团；2 散拼；6 签证；7 机票
	 * @param rebates
	 *            返佣实体
	 * @param genericsOrder
	 *            订单实体
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveRebatesCostRecordNew(ReviewNew reviewInfo,
			RebatesNew rebates, Object genericsOrder, boolean yubao_locked,
			boolean jiesuan_locked) {

		Integer orderType = Integer.parseInt(reviewInfo.getProductType());
		Long agentId = Long.parseLong(reviewInfo.getAgent());
		String agentName = reviewInfo.getAgentName();
		Long companyId = UserUtils.getUser().getCompany().getId();

		Long orderId = 0L; // 订单ID
		Long activityId = 0L; // 产品ID
		String totalMoneySer = ""; // 应收金额UUID

		// 机票
		if (Context.ORDER_TYPE_JP == orderType) {
			AirticketOrder airOrder = (AirticketOrder) genericsOrder;
			orderId = airOrder.getId();
			activityId = airOrder.getAirticketId();
			agentId = airOrder.getAgentinfoId();
			totalMoneySer = airOrder.getTotalMoney();
		}
		// 签证
		else if (Context.ORDER_TYPE_QZ == orderType) {
			VisaOrder visaOrder = (VisaOrder) genericsOrder;
			orderId = visaOrder.getId();
			activityId = visaOrder.getVisaProductId();
			agentId = visaOrder.getAgentinfoId();
			totalMoneySer = visaOrder.getTotalMoney();
		}
		// 单团
		else {
			ProductOrderCommon order = (ProductOrderCommon) genericsOrder;
			orderId = order.getId();
			activityId = order.getProductGroupId();
			agentId = order.getOrderCompany();
			totalMoneySer = order.getTotalMoney();
		}

		// 退款人民币币种
		Long rmbCurrencyId = 0L;
		if (rmbCurrencyId == 0) {
			Currency rmbCurrency = currencyService.getRMBCurrencyId();
			if (rmbCurrency != null) {
				rmbCurrencyId = rmbCurrency.getId();
			} else {
				throw new IllegalArgumentException("此供应商没有人民币币种");
			}
		}
		// 汇率查询：订单应付金额中如有此币种则获取对应汇率，否则查询基础信息表中汇率
		BigDecimal exchangeRate = new BigDecimal(0);

		if (rebates.getCurrencyId() == null
				|| rebates.getRebatesDiff() == null) {
			throw new IllegalArgumentException("币种或金额异常");
		}

		exchangeRate = moneyAmountService.getExchangerateByUuid(
				totalMoneySer, rebates.getCurrencyId().intValue());
		if (exchangeRate == null) {
			throw new IllegalArgumentException("汇率异常");
		}

		CostRecord yubao_cost = new CostRecord();
		yubao_cost.setActivityId(activityId);
		yubao_cost.setOrderType(orderType); // 订单类型：6 签证；7机票；2散拼；1单团
		yubao_cost.setBudgetType(0); // 0为预算价；1为成本价；2财务成本预算
		yubao_cost.setCurrencyId(rmbCurrencyId.intValue());
		yubao_cost
				.setPrice(rebates.getRebatesDiff().multiply(exchangeRate)); // 成本单价=返佣金额*币种对人民币的汇率
		yubao_cost.setQuantity(1); // 数量默认1
		if(reviewInfo.getRelatedObjectType()!=null&&reviewInfo.getRelatedObjectType()==2){
			yubao_cost.setSupplyType(0); //供应商
			yubao_cost.setSupplyName(reviewInfo.getRelatedObjectName());// 供应商名称
			yubao_cost.setSupplyId(Integer.valueOf(reviewInfo.getRelatedObject())); // 供应商ID
		}else{
			yubao_cost.setSupplyType(1); // 渠道商
			yubao_cost.setSupplyName(agentName);// 渠道商名称
			yubao_cost.setSupplyId(agentId.intValue()); // 渠道商ID
		}
		yubao_cost.setOverseas(0); // 1:国外,0:国内
		yubao_cost.setNowLevel(1); // 当前审核层级
		yubao_cost.setReview(5); // 审核状态:
									// 0未通过,1待审核,2已通过,3：操作完成，4：待提交，5：不需要审核
		yubao_cost.setName("其他");
		yubao_cost.setCreateBy(UserUtils.getUser());
		yubao_cost.setCreateDate(new Date());
		yubao_cost.setReviewType(2); // 1： 退款流程 2：返佣
		yubao_cost.setOrderId(orderId);// 订单Id
		yubao_cost.setComment(rebates.getRemark());// 备注
		// yubao_cost.setComment("返佣");
		yubao_cost.setReviewStatus(ReviewUtils
				.getChineseReviewStatusByUuid(reviewInfo.getId()));
		yubao_cost.setReviewUuid(reviewInfo.getId());
		yubao_cost.setReviewCompanyId(companyId);
		yubao_cost.setPriceAfter(rebates.getRebatesDiff().multiply(
				exchangeRate));
		yubao_cost.setDelFlag("0");
		yubao_cost.setCurrencyAfter(rmbCurrencyId.intValue());
		yubao_cost.setIsNew(2);
		yubao_cost.setUuid(UuidUtils.generUuid());
		BigDecimal bd = new BigDecimal(1);
		yubao_cost.setRate(bd);
		yubao_cost.setRebatesId(rebates.getId());
		
		CostRecord jiesuan_cost = new CostRecord();
		BeanUtils.copyProperties(yubao_cost, jiesuan_cost);
		jiesuan_cost.setId(null);
		jiesuan_cost.setBudgetType(1);
		jiesuan_cost.setReview(2);
		jiesuan_cost.setNowLevel(4);
		jiesuan_cost.setReviewStatus(ReviewUtils
				.getChineseReviewStatusByUuid(reviewInfo.getId()));
		jiesuan_cost.setPayNowLevel(1);
		jiesuan_cost.setPayReview(4);
		jiesuan_cost.setPriceAfter(rebates.getRebatesDiff().multiply(
				exchangeRate));
		jiesuan_cost.setDelFlag("0");
		jiesuan_cost.setCurrencyAfter(rmbCurrencyId.intValue());
		jiesuan_cost.setRate(bd);
		jiesuan_cost.setIsNew(2);
		jiesuan_cost.setUuid(UuidUtils.generUuid());
		jiesuan_cost.setRebatesId(rebates.getId());
//		String companyUuid = UserUtils.getUser().getCompany().getUuid();
//		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
//			if (yubao_locked) {
//				jiesuan_cost.setForecastLockedIn("1");
//			}
//			saveCostRecord(yubao_cost);
//			if (jiesuan_locked) {
//				yubao_cost.setSettleLockedIn("1");
//			}
//			saveCostRecord(jiesuan_cost);
//		} else {
//			if (!yubao_locked) {
//				saveCostRecord(yubao_cost);
//			}
//			if (!jiesuan_locked) {
//				saveCostRecord(jiesuan_cost);
//			}
//		}
		
		saveCostRecord(yubao_cost);
		saveCostRecord(jiesuan_cost);
	}

	/**
	 * 返佣申请添加成本记录
	 * 
	 * @param orderType
	 *            订单类型：1 单团；2 散拼；6 签证；7 机票
	 * @param rebates
	 *            返佣实体
	 * @param genericsOrder
	 *            订单实体
	 * @param agentName
	 *            渠道商名称
	 * @param result
	 * @param deptId
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveRebatesCostRecord(Integer orderType, Rebates rebates,
			Object genericsOrder, String agentName, Long result, Long deptId,
			boolean yubao_locked, boolean jiesuan_locked) {

		if (!(yubao_locked && jiesuan_locked)) {
			Long orderId = 0L; // 订单ID
			Long activityId = 0L; // 产品ID
			Long agentId = 0L; // 渠道ID
			String totalMoneySer = ""; // 应收金额UUID

			// 机票
			if (Context.ORDER_TYPE_JP == orderType) {
				AirticketOrder airOrder = (AirticketOrder) genericsOrder;
				orderId = airOrder.getId();
				activityId = airOrder.getAirticketId();
				agentId = airOrder.getAgentinfoId();
				totalMoneySer = airOrder.getTotalMoney();
			}
			// 签证
			else if (Context.ORDER_TYPE_QZ == orderType) {
				VisaOrder visaOrder = (VisaOrder) genericsOrder;
				orderId = visaOrder.getId();
				activityId = visaOrder.getVisaProductId();
				agentId = visaOrder.getAgentinfoId();
				totalMoneySer = visaOrder.getTotalMoney();
			}
			// 单团
			else {
				ProductOrderCommon order = (ProductOrderCommon) genericsOrder;
				orderId = order.getId();
				activityId = order.getProductGroupId();
				agentId = order.getOrderCompany();
				totalMoneySer = order.getTotalMoney();
			}

			// 退款人民币币种
			Long rmbCurrencyId = 0L;
			if (rmbCurrencyId == 0) {
				Currency rmbCurrency = currencyService.getRMBCurrencyId();
				if (rmbCurrency != null) {
					rmbCurrencyId = rmbCurrency.getId();
				} else {
					throw new IllegalArgumentException("此供应商没有人民币币种");
				}
			}
			// 汇率查询：订单应付金额中如有此币种则获取对应汇率，否则查询基础信息表中汇率
			BigDecimal exchangeRate = new BigDecimal(0);

			if (rebates.getCurrencyId() == null
					|| rebates.getRebatesDiff() == null) {
				throw new IllegalArgumentException("币种或金额异常");
			}

			exchangeRate = moneyAmountService.getExchangerateByUuid(
					totalMoneySer, rebates.getCurrencyId().intValue());
			if (exchangeRate == null) {
				throw new IllegalArgumentException("汇率异常");
			}

			CostRecord yubao_cost = new CostRecord();
			yubao_cost.setActivityId(activityId);
			yubao_cost.setOrderType(orderType); // 订单类型：6 签证；7机票；2散拼；1单团
			yubao_cost.setBudgetType(0); // 0为预算价；1为成本价；2财务成本预算
			yubao_cost.setCurrencyId(rmbCurrencyId.intValue());
			yubao_cost
					.setPrice(rebates.getRebatesDiff().multiply(exchangeRate)); // 成本单价=返佣金额*币种对人民币的汇率
			yubao_cost.setQuantity(1); // 数量默认1
			yubao_cost.setSupplyType(1); // 渠道商
			yubao_cost.setSupplyName(agentName);// 渠道商名称
			yubao_cost.setOverseas(0); // 1:国外,0:国内
			yubao_cost.setNowLevel(1); // 当前审核层级
			yubao_cost.setReview(5); // 审核状态:
										// 0未通过,1待审核,2已通过,3：操作完成，4：待提交，5：不需要审核
			yubao_cost.setSupplyId(agentId.intValue()); // 渠道商ID
			yubao_cost.setName("其他");
			yubao_cost.setCreateBy(UserUtils.getUser());
			yubao_cost.setCreateDate(new Date());
			yubao_cost.setReviewType(2); // 1： 退款流程 2：返佣
			yubao_cost.setOrderId(orderId);// 订单Id
			yubao_cost.setComment(rebates.getRemark());// 备注
			// yubao_cost.setComment("返佣");
			yubao_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			yubao_cost.setReviewId(result);
			yubao_cost.setReviewCompanyId(reviewDao.findOne(result)
					.getReviewCompanyId());
			yubao_cost.setPriceAfter(rebates.getRebatesDiff().multiply(
					exchangeRate));
			yubao_cost.setDelFlag("0");
			yubao_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			BigDecimal bd = new BigDecimal(1);
			yubao_cost.setRate(bd);

			CostRecord jiesuan_cost = new CostRecord();
			BeanUtils.copyProperties(yubao_cost, jiesuan_cost);
			jiesuan_cost.setId(null);
			jiesuan_cost.setBudgetType(1);
			jiesuan_cost.setReview(2);
			jiesuan_cost.setNowLevel(4);
			jiesuan_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			jiesuan_cost.setPayNowLevel(1);
			jiesuan_cost.setPayReview(4);
			jiesuan_cost.setPriceAfter(rebates.getRebatesDiff().multiply(
					exchangeRate));
			jiesuan_cost.setDelFlag("0");
			jiesuan_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			jiesuan_cost.setRate(bd);

//			if (!yubao_locked) {
//				saveCostRecord(yubao_cost);
//			}
//			if (!jiesuan_locked) {
//				saveCostRecord(jiesuan_cost);
//			}
			saveCostRecord(yubao_cost);
			saveCostRecord(jiesuan_cost);
		}
	}

	/**
	 * 海岛游返佣申请添加成本记录
	 * 
	 * @param orderType
	 *            订单类型：12海岛游
	 * @param rebates
	 *            返佣实体
	 * @param genericsOrder
	 *            订单实体
	 * @param agentName
	 *            渠道商名称
	 * @param result
	 * @param deptId
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveRebatesCostRecord(Integer orderType, IslandRebates rebates,
			Object genericsOrder, String agentName, Long result, Long deptId,
			boolean yubao_locked, boolean jiesuan_locked) {

		if (!(yubao_locked && jiesuan_locked)) {
			Long orderId = 0L; // 订单ID
			Long activityId = 0L; // 产品ID
			Long agentId = 0L; // 渠道ID
			String totalMoneySer = ""; // 应收金额UUID

			IslandOrder islandOrder = (IslandOrder) genericsOrder;
			orderId = islandOrder.getId().longValue();
			agentId = islandOrder.getOrderCompany().longValue();
			totalMoneySer = islandOrder.getTotalMoney();
			activityId = activityIslandDao
					.getByUuid(islandOrder.getActivityIslandUuid()).getId()
					.longValue();

			// 退款人民币币种
			Long rmbCurrencyId = 0L;
			if (rmbCurrencyId == 0) {
				Currency rmbCurrency = currencyService.getRMBCurrencyId();
				if (rmbCurrency != null) {
					rmbCurrencyId = rmbCurrency.getId();
				} else {
					throw new IllegalArgumentException("此供应商没有人民币币种");
				}
			}
			// 汇率查询：订单应付金额中如有此币种则获取对应汇率，否则查询基础信息表中汇率
			BigDecimal exchangeRate = new BigDecimal(0);

			if (rebates.getCurrencyId() == null
					|| rebates.getRebatesDiff() == null) {
				throw new IllegalArgumentException("币种或金额异常");
			}

			Double islandExchangeRete = islandMoneyAmountService
					.getExchangerateByUuid(totalMoneySer, rebates
							.getCurrencyId().intValue());
			exchangeRate = BigDecimal.valueOf(islandExchangeRete);
			if (exchangeRate == null) {
				throw new IllegalArgumentException("汇率异常");
			}

			CostRecord yubao_cost = new CostRecord();
			yubao_cost.setActivityId(activityId);
			yubao_cost.setOrderType(orderType); // 订单类型：6 签证；7机票；2散拼；1单团
			yubao_cost.setBudgetType(0); // 0为预算价；1为成本价；2财务成本预算
			yubao_cost.setCurrencyId(rmbCurrencyId.intValue());
			yubao_cost
					.setPrice(rebates.getRebatesDiff().multiply(exchangeRate)); // 成本单价=返佣金额*币种对人民币的汇率
			yubao_cost.setQuantity(1); // 数量默认1
			yubao_cost.setSupplyType(1); // 渠道商
			yubao_cost.setSupplyName(agentName);// 渠道商名称
			yubao_cost.setOverseas(0); // 1:国外,0:国内
			yubao_cost.setNowLevel(1); // 当前审核层级
			yubao_cost.setReview(5); // 审核状态:
										// 0未通过,1待审核,2已通过,3：操作完成，4：待提交，5：不需要审核
			yubao_cost.setSupplyId(agentId.intValue()); // 渠道商ID
			yubao_cost.setName("其他");
			yubao_cost.setCreateBy(UserUtils.getUser());
			yubao_cost.setCreateDate(new Date());
			yubao_cost.setReviewType(2); // 1： 退款流程 2：返佣
			yubao_cost.setOrderId(orderId);// 订单Id
			yubao_cost.setComment(rebates.getRemark());// 备注
			// yubao_cost.setComment("返佣");
			yubao_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			yubao_cost.setReviewId(result);
			yubao_cost.setReviewCompanyId(reviewDao.findOne(result)
					.getReviewCompanyId());
			yubao_cost.setPriceAfter(rebates.getRebatesDiff().multiply(
					exchangeRate));
			yubao_cost.setPrintFlag(0);
			yubao_cost.setDelFlag("0");
			yubao_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			BigDecimal bd = new BigDecimal(1);
			yubao_cost.setRate(bd);

			CostRecord jiesuan_cost = new CostRecord();
			BeanUtils.copyProperties(yubao_cost, jiesuan_cost);
			jiesuan_cost.setId(null);
			jiesuan_cost.setBudgetType(1);
			jiesuan_cost.setReview(2);
			jiesuan_cost.setNowLevel(4);
			jiesuan_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			jiesuan_cost.setPayNowLevel(1);
			jiesuan_cost.setPayReview(4);
			jiesuan_cost.setPriceAfter(rebates.getRebatesDiff().multiply(
					exchangeRate));
			jiesuan_cost.setDelFlag("0");
			jiesuan_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			jiesuan_cost.setRate(bd);

//			if (!yubao_locked) {
//				saveCostRecord(yubao_cost);
//			}
//			if (!jiesuan_locked) {
//				saveCostRecord(jiesuan_cost);
//			}

			saveCostRecord(yubao_cost);
			saveCostRecord(jiesuan_cost);
		}
	}

	/**
	 * 海岛游返佣申请添加成本记录
	 * 
	 * @param orderType
	 *            订单类型：12海岛游
	 * @param rebates
	 *            返佣实体
	 * @param genericsOrder
	 *            订单实体
	 * @param agentName
	 *            渠道商名称
	 * @param result
	 * @param deptId
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveHotelRebatesCostRecord(Integer orderType,
			HotelRebates rebates, Object genericsOrder, String agentName,
			Long result, Long deptId, boolean yubao_locked,
			boolean jiesuan_locked) {

		if (!(yubao_locked && jiesuan_locked)) {
			Long orderId = 0L; // 订单ID
			Long activityId = 0L; // 产品ID
			Long agentId = 0L; // 渠道ID
			String totalMoneySer = ""; // 应收金额UUID

			HotelOrder hotelOrder = (HotelOrder) genericsOrder;
			orderId = hotelOrder.getId().longValue();
			agentId = hotelOrder.getOrderCompany().longValue();
			totalMoneySer = hotelOrder.getTotalMoney();
			activityId = activityHotelDao
					.getByUuid(hotelOrder.getActivityHotelUuid()).getId()
					.longValue();

			// 退款人民币币种
			Long rmbCurrencyId = 0L;
			if (rmbCurrencyId == 0) {
				Currency rmbCurrency = currencyService.getRMBCurrencyId();
				if (rmbCurrency != null) {
					rmbCurrencyId = rmbCurrency.getId();
				} else {
					throw new IllegalArgumentException("此供应商没有人民币币种");
				}
			}
			// 汇率查询：订单应付金额中如有此币种则获取对应汇率，否则查询基础信息表中汇率
			BigDecimal exchangeRate = new BigDecimal(0);

			if (rebates.getCurrencyId() == null
					|| rebates.getRebatesDiff() == null) {
				throw new IllegalArgumentException("币种或金额异常");
			}

			Double islandExchangeRete = islandMoneyAmountService
					.getExchangerateByUuid(totalMoneySer, rebates
							.getCurrencyId().intValue());
			exchangeRate = BigDecimal.valueOf(islandExchangeRete);
			if (exchangeRate == null) {
				throw new IllegalArgumentException("汇率异常");
			}

			CostRecord yubao_cost = new CostRecord();
			yubao_cost.setActivityId(activityId);
			yubao_cost.setOrderType(orderType); // 订单类型：6 签证；7机票；2散拼；1单团
			yubao_cost.setBudgetType(0); // 0为预算价；1为成本价；2财务成本预算
			yubao_cost.setCurrencyId(rmbCurrencyId.intValue());
			yubao_cost
					.setPrice(rebates.getRebatesDiff().multiply(exchangeRate)); // 成本单价=返佣金额*币种对人民币的汇率
			yubao_cost.setQuantity(1); // 数量默认1
			yubao_cost.setSupplyType(1); // 渠道商
			yubao_cost.setSupplyName(agentName);// 渠道商名称
			yubao_cost.setOverseas(0); // 1:国外,0:国内
			yubao_cost.setNowLevel(1); // 当前审核层级
			yubao_cost.setReview(5); // 审核状态:
										// 0未通过,1待审核,2已通过,3：操作完成，4：待提交，5：不需要审核
			yubao_cost.setSupplyId(agentId.intValue()); // 渠道商ID
			yubao_cost.setName("其他");
			yubao_cost.setCreateBy(UserUtils.getUser());
			yubao_cost.setCreateDate(new Date());
			yubao_cost.setReviewType(2); // 1： 退款流程 2：返佣
			yubao_cost.setOrderId(orderId);// 订单Id
			yubao_cost.setComment(rebates.getRemark());// 备注
			// yubao_cost.setComment("返佣");
			yubao_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			yubao_cost.setReviewId(result);
			yubao_cost.setReviewCompanyId(reviewDao.findOne(result)
					.getReviewCompanyId());
			yubao_cost.setPriceAfter(rebates.getRebatesDiff().multiply(
					exchangeRate));
			yubao_cost.setDelFlag("0");
			yubao_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			BigDecimal bd = new BigDecimal(1);
			yubao_cost.setRate(bd);

			CostRecord jiesuan_cost = new CostRecord();
			BeanUtils.copyProperties(yubao_cost, jiesuan_cost);
			jiesuan_cost.setId(null);
			jiesuan_cost.setBudgetType(1);
			jiesuan_cost.setReview(2);
			jiesuan_cost.setNowLevel(4);
			jiesuan_cost.setReviewStatus(reviewService
					.getNextReviewPerson(reviewDao.findOne(result)));
			jiesuan_cost.setPayNowLevel(1);
			jiesuan_cost.setPayReview(4);
			jiesuan_cost.setPriceAfter(rebates.getRebatesDiff().multiply(
					exchangeRate));
			jiesuan_cost.setDelFlag("0");
			jiesuan_cost.setCurrencyAfter(rmbCurrencyId.intValue());
			jiesuan_cost.setRate(bd);

//			if (!yubao_locked) { //不需要进行判断了，都进行写入。yudong.xu 2016.5.19
//				saveCostRecord(yubao_cost);
//			}
//			if (!jiesuan_locked) {
//				saveCostRecord(jiesuan_cost);
//			}

			saveCostRecord(yubao_cost);
			saveCostRecord(jiesuan_cost);
		}
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void insertMoney(String activityId, Long groupId, Long typeId,
			int costTotal, int costId[], BigDecimal cost[]) {
		String uuid = UUID.randomUUID().toString();
		String costSerial = "";
		if (typeId == 7) {
			ActivityAirTicket activityAirTicket = activityAirTicketDao
					.findOne(groupId);
			costSerial = activityAirTicket.getCost();
			if (costSerial == null || costSerial.equals("")) {
				activityAirTicket.setCost(uuid);
				activityAirTicketDao.save(activityAirTicket);
			}
		} else if (typeId == 6) {
			VisaProducts visaProduct = this.visaProductsDao.findOne(groupId);
			costSerial = visaProduct.getCost();
			if (costSerial == null || costSerial.equals("")) {
				visaProduct.setCost(uuid);
				visaProductsDao.save(visaProduct);
			}
		} else {
			ActivityGroup activityGroup = activityGroupDao.findOne(groupId);
			costSerial = activityGroup.getCost();
			if (costSerial == null || costSerial.equals("")) {
				activityGroup.setCost(uuid);
				activityGroupDao.save(activityGroup);
			}
		}

		if (costSerial == null || costSerial.equals("")) {
			for (int i = 1; i <= costTotal; i++) {
				MoneyAmount moneyAmount = new MoneyAmount();
				moneyAmount.setSerialNum(uuid);
				moneyAmount.setCurrencyId(costId[i - 1]);
				moneyAmount.setAmount(cost[i - 1]);
				moneyAmount.setDelFlag("0");
				moneyAmount.setCreateTime(new Date());
				if (!cost[i - 1].equals(BigDecimal.ZERO)) {
					moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
				}
			}
		} else {
			for (int i = 1; i <= costTotal; i++) {
				List<MoneyAmount> amount = moneyAmountDao
						.findAmountBySerialNumAndCurrencyId(costSerial,
								costId[i - 1]);
				if (amount.size() == 0) {
					MoneyAmount moneyAmount = new MoneyAmount();
					moneyAmount.setSerialNum(costSerial);
					moneyAmount.setCurrencyId(costId[i - 1]);
					moneyAmount.setAmount(cost[i - 1]);
					moneyAmount.setCreateTime(new Date());
					moneyAmount.setDelFlag("0");
					if (!cost[i - 1].equals(BigDecimal.ZERO)) {
						moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
					}
				} else {
					if (!cost[i - 1].equals(BigDecimal.ZERO)) {
						moneyAmountDao.updateOrderForAmount(costSerial,
								costId[i - 1], cost[i - 1]);
					} else {
						moneyAmountDao.deleteOrderForAmount(costSerial,
								costId[i - 1]);
					}
				}
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void insertIncome(String activityId, Long groupId, Long typeId,
			int incomeTotal, int incomeId[], BigDecimal income[]) {

		String uuid = UUID.randomUUID().toString();
		String incomeSerial = "";
		if (typeId == 7) {
			ActivityAirTicket activityAirTicket = activityAirTicketDao
					.findOne(groupId);
			incomeSerial = activityAirTicket.getIncome();
			if (incomeSerial == null || incomeSerial.equals("")) {
				activityAirTicket.setIncome(uuid);
				activityAirTicketDao.save(activityAirTicket);
			}
		} else if (typeId == 6) {
			VisaProducts visaProduct = this.visaProductsDao.findOne(groupId);
			incomeSerial = visaProduct.getIncome();
			if (incomeSerial == null || incomeSerial.equals("")) {
				visaProduct.setIncome(uuid);
				visaProductsDao.save(visaProduct);
			}
		} else {
			ActivityGroup activityGroup = activityGroupDao.findOne(groupId);
			incomeSerial = activityGroup.getIncome();
			if (incomeSerial == null || incomeSerial.equals("")) {
				activityGroup.setIncome(uuid);
				activityGroupDao.save(activityGroup);
			}
		}

		if (incomeSerial == null || incomeSerial.equals("")) {
			for (int i = 1; i <= incomeTotal; i++) {
				MoneyAmount moneyAmount = new MoneyAmount();
				moneyAmount.setSerialNum(uuid);
				moneyAmount.setCurrencyId(incomeId[i - 1]);
				moneyAmount.setAmount(income[i - 1]);
				moneyAmount.setCreateTime(new Date());
				moneyAmount.setDelFlag("0");
				if (!income[i - 1].equals(BigDecimal.ZERO)) {
					moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
				}
			}
		} else {
			for (int i = 1; i <= incomeTotal; i++) {
				List<MoneyAmount> amount = moneyAmountDao
						.findAmountBySerialNumAndCurrencyId(incomeSerial,
								incomeId[i - 1]);
				if (amount.size() == 0) {
					MoneyAmount moneyAmount = new MoneyAmount();
					moneyAmount.setSerialNum(incomeSerial);
					moneyAmount.setCurrencyId(incomeId[i - 1]);
					moneyAmount.setAmount(income[i - 1]);
					moneyAmount.setCreateTime(new Date());
					moneyAmount.setDelFlag("0");
					if (!income[i - 1].equals(BigDecimal.ZERO)) {
						moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
					}
				} else {
					if (!income[i - 1].equals(BigDecimal.ZERO)) {
						moneyAmountDao.updateOrderForAmount(incomeSerial,
								incomeId[i - 1], income[i - 1]);
					} else {
						moneyAmountDao.deleteOrderForAmount(incomeSerial,
								incomeId[i - 1]);
					}
				}
			}
		}
	}

	public CostRecord findOne(Long id) {
		return costRecordDao.findOne(id);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteCostRecord(Long id) {
		costRecordDao.deleteById(id);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void cancelCostRecord(Long id) {
		costRecordDao.cancelById(id);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void cancelCostIsland(Long id) {
		costRecordIslandDao.cancelById(id);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void cancelCostHotel(Long id) {
		costRecordHotelDao.cancelById(id);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Deprecated
	public void cancelPayCostRecord(Long id) {
		costRecordDao.cancelPayById(id);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void cancelPayCostIsland(Long id) {
		costRecordIslandDao.cancelPayById(id);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void cancelPayCostHotel(Long id) {
		costRecordHotelDao.cancelPayById(id);
	}

	public List<CostRecord> findCostRecordList(Long activityId,
			Integer budgetType, Integer overseas, Integer orderType) {

		// String sql =
		// "select id,activityId,orderType,name,price,comment,quantity,currencyId,overseas,supplierType,supplyType,supplyId,supplyName,"
		// +
		// "createBy,updateBy,createDate,updateDate,budgetType,review,reviewComment,  payStatus,delFlag,serialNum,printTime,printFlag,lockStatus,"
		// +
		// "currencyAfter,rate,priceAfter,nowLevel,reviewCompanyId,bankName,bankAccount,reviewType,orderId,reviewStatus,reviewId,isJoin,payNowLevel,"
		// +
		// "payReviewCompanyId,payReview,pay_apply_date,payReviewComment,payUpdateBy,payUpdateDate,bigCode,visaId "
		// + " from cost_record where activityId=" + activityId +
		// " and budgetType=" + budgetType + " and overseas=" + overseas
		// + " and orderType=" + orderType +
		// " and (reviewType=0 or (reviewType<>0 and name<>'退款')) and delFlag='0' and visaId is null";
		//
		// return costRecordDao.findBySql(sql, CostRecord.class);
		return costRecordDao.findCostRecordList(activityId, budgetType,
				overseas, orderType);

		/*
		 * String sql =
		 * "select id,uuid, activityId,orderType,name,price,comment,quantity,currencyId,overseas,supplierType,supplyType,supplyId,supplyName,"
		 * +
		 * "createBy,updateBy,createDate,updateDate,budgetType,review,reviewComment,  payStatus,delFlag,serialNum,printTime,printFlag,lockStatus,"
		 * +
		 * "currencyAfter,rate,priceAfter,nowLevel,reviewCompanyId,bankName,bankAccount,reviewType,orderId,reviewStatus,reviewId,reviewUuid,pay_review_uuid,isJoin,payNowLevel,"
		 * +
		 * "payReviewCompanyId,payReview,pay_apply_date,payReviewComment,payUpdateBy,payUpdateDate,bigCode,visaId "
		 * + " from cost_record where activityId=" + activityId +
		 * " and budgetType=" + budgetType + " and overseas=" + overseas +
		 * " and orderType=" + orderType +
		 * " and (reviewType=0 or (reviewType<>0 and name<>'退款')) and delFlag='0' and visaId is null"
		 * ;
		 * 
		 * return costRecordDao.findBySql(sql, CostRecord.class);
		 */

	}
	
	/**
	 * 成本付款审批的审批和查看功能，查询的数据即包括新审批的数据也要包括旧审批的数据
	 * @param activityId		产品或者团期ID
	 * @param budgetType		成本类型
	 * @param overseas			境内境外
	 * @param orderType			订单类型
	 * @author shijun.liu
	 * @return
	 */
	public List<CostRecord> findNewAndOldCostRecordList(Long activityId,
			Integer budgetType, Integer overseas, Integer orderType) {

		return costRecordDao.findNewAndOldCostRecordList(activityId, budgetType,
				overseas, orderType);
	}

	// 签证子订单
	private List<CostRecord> findCostRecordList(Long activityId,

	Integer budgetType, Integer overseas, Integer orderType, Integer reviewType) {
		// String sql =
		// "select id,activityId,orderType,name,price,comment,quantity,currencyId,overseas,supplierType,supplyType,supplyId,supplyName,"
		// +
		// "createBy,updateBy,createDate,updateDate,budgetType,review,reviewComment,  payStatus,delFlag,serialNum,printTime,printFlag,lockStatus,"
		// +
		// "currencyAfter,rate,priceAfter,nowLevel,reviewCompanyId,bankName,bankAccount,reviewType,orderId,reviewStatus,reviewId,isJoin,payNowLevel,"
		// +
		// "payReviewCompanyId,payReview,pay_apply_date,payReviewComment,payUpdateBy,payUpdateDate,bigCode,visaId "
		// + " from cost_record where activityId=" + activityId +
		// " and budgetType=" + budgetType + " and overseas=" + overseas
		// + " and orderType=" + orderType +
		// " and (reviewType=0 or (reviewType<>0 and name<>'退款')) and visaId is not null";
		//
		// return costRecordDao.findBySql(sql, CostRecord.class);
		return costRecordDao.findCostRecordList(activityId, budgetType,
				overseas, orderType, reviewType);

		/*
		 * Integer budgetType, Integer overseas, Integer orderType, Integer
		 * visaId) { String sql =
		 * "select id,uuid,activityId,orderType,name,price,comment,quantity,currencyId,overseas,supplierType,supplyType,supplyId,supplyName,"
		 * +
		 * "createBy,updateBy,createDate,updateDate,budgetType,review,reviewComment,  payStatus,delFlag,serialNum,printTime,printFlag,lockStatus,"
		 * +
		 * "currencyAfter,rate,priceAfter,nowLevel,reviewCompanyId,bankName,bankAccount,reviewType,orderId,reviewStatus,reviewId,isJoin,payNowLevel,"
		 * +
		 * "payReviewCompanyId,payReview,pay_apply_date,payReviewComment,payUpdateBy,payUpdateDate,bigCode,visaId "
		 * + " from cost_record where activityId=" + activityId +
		 * " and budgetType=" + budgetType + " and overseas=" + overseas +
		 * " and orderType=" + orderType +
		 * " and (reviewType=0 or (reviewType<>0 and name<>'退款')) and visaId is not null"
		 * ;
		 * 
		 * return costRecordDao.findBySql(sql, CostRecord.class);
		 */

	}

	

	/**
	 * 新成本审批-审批列表
	 *
	 * @param activityId
	 * @param orderType
	 * @return
	 */
	private List<Map<String, Object>> getCostRecordList(Long activityId, Integer orderType) {
		String sql = "SELECT cost.id as costId,cost.name,cost.kb,cost.quantity,cost.supplyName,cost.currencyId,cost.price,cost.rate,cost.currencyAfter,cost.priceAfter,cost.`comment`,"
				+ "cost.createBy,cost.reviewType,cost.id,cost.reviewId,cost.budgetType,cost.overseas,review.id reviewUuid,CONCAT(review.status, '') status,review.current_reviewer,"
				+ "cost.reviewStatus,cost.delFlag,cost.visaId,cost.uuid,cost.activityId,cost.orderType,cost.pay_review_uuid,cost.payReview,"
				+ "(select n.status from review_new n where n.id = cost.pay_review_uuid) as pay_status,"
				+ "(select n.current_reviewer from review_new n where n.id = cost.pay_review_uuid) as pay_current_reviewer,"
				+ "CONCAT(cost.is_new,'') isNew,cost.review,CONCAT(review.need_no_review_flag,'') noReview,CONCAT(r.need_no_review_flag,'') noPayReview,"
				+ "cost.costVoucher"
				+ " from cost_record cost"
				+ " LEFT JOIN review_new review"
				+ " on cost.reviewUuid = review.id"
				+ " LEFT JOIN review_new r on cost.pay_review_uuid = r.id"
				+ " WHERE activityId = " + activityId
//				+ " and budgetType = " + budgetType
//				+ " and overseas = " + overseas
				+ " and orderType = " + orderType
				+ " and (reviewType = 0 or (reviewType<>0 and name<>'退款')) and delFlag='0' and visaId is NULL";
		return costRecordDao.findBySql(sql, Map.class);
	}

	/**
	 * 拉美图--签证子订单
	 * 
	 * @param activityId
	 * @param budgetType
	 * @param overseas
	 * @param orderType
	 * @param visaId
	 * @return
	 */
	private List<Map<String, Object>> getCostRecordList(Long activityId,
			Integer budgetType, Integer overseas, Integer orderType,
			Integer visaId) {
		String sql = "SELECT cost.name,cost.kb,cost.quantity,cost.supplyName,cost.currencyId,cost.price,cost.rate,cost.currencyAfter,cost.priceAfter,cost.`comment`,"
				+ "cost.createBy,cost.reviewType,cost.id,cost.reviewId,cost.budgetType,review.id reviewUuid,CONCAT(review.status, '') status,review.current_reviewer,"
				+ "cost.reviewStatus,CONCAT(cost.delFlag, '') delFlag,cost.visaId,cost.uuid,cost.activityId,cost.orderType,cost.pay_review_uuid,cost.payReview,"
				+ "(select n.status from review_new n where n.id = cost.pay_review_uuid) as pay_status,"
				+ "(select n.current_reviewer from review_new n where n.id = cost.pay_review_uuid) as pay_current_reviewer,"
				+ "CONCAT(cost.is_new,'') isNew,cost.review,CONCAT(review.need_no_review_flag,'') noReview,CONCAT(r.need_no_review_flag,'') noPayReview,"
				+ "cost.costVoucher"
				+ " from cost_record cost"
				+ " LEFT JOIN review_new review"
				+ " on cost.reviewUuid = review.id"
				+ " LEFT JOIN review_new r on cost.pay_review_uuid = r.id"
				+ " WHERE activityId = " + activityId
				+ " and budgetType = " + budgetType
				+ " and overseas = " + overseas
				+ " and orderType = " + orderType
				+ " and (reviewType = 0 or (reviewType<>0 and name<>'退款')) and visaId is not NULL";
		return costRecordDao.findBySql(sql, Map.class);
	}

	/**
	 * 拉美途--获取签证费信息
	 * 
	 * @param groupId
	 * @return
	 */
	public List<CostRecord> getVisaCost(Long groupId) {
		StringBuffer sb = new StringBuffer();
		// sb.append("SELECT visa.id id,'' bankAccount,'' bankName,'0' budgetType,'' createDate,'' comment,'0' delFlag,'' isJoin,visa.id visaId,'' nowLevel,'' orderId,'' orderType,'' pay_apply_date,'' payNowLevel,'' payReview, '' payReviewComment,'' payReviewCompanyId,'' payStatus,'' payUpdateBy,'' payUpdateDate,'' printFlag,'' printTime,'' reviewComment,'' reviewCompanyId,'' reviewId,'' reviewStatus,'0' reviewType,'' serialNum,(SELECT	supplyType FROM suppliername_view sv WHERE sv.supplierName = '拉美途') supplierType,(SELECT	supplierId FROM suppliername_view sv WHERE sv.supplierName = '拉美途') supplyId,'1' supplyType,'' updateBy,'' updateDate,visa.id activityId,'0' overseas,visa.productName name,visa.quantity,(SELECT supplierName from suppliername_view sv where sv.supplierName='拉美途') supplyName, ")
		// .append(" visa.currencyId,visa.visaPrice price,(SELECT c.currency_exchangerate from currency c where c.currency_id = visa.currencyId) rate, ")
		// .append(" (SELECT c.currency_id from currency c where c.currency_id = visa.currencyId and c.currency_name='人民币') currencyAfter,")
		// .append(" visa.quantity*visa.visaPrice*(SELECT c.currency_exchangerate from currency c where c.currency_id = visa.currencyId) priceAfter, '4' review, g.createBy")
		// .append(" from (SELECT visa.id,visa.productName,visa.currencyId,visa.visaPrice, vo.activity_code, vo.mainOrderId, count(*) as quantity ")
		// .append(" from visa_products visa, visa_order vo where visa.id = vo.visa_product_id group by visa.productName) visa left JOIN activitygroup g ON g.id = visa.activity_code and g.id = ?")
		// .append(" left JOIN productorder porder ON g.id = porder.productGroupId and porder.orderNum = visa.mainOrderId");
		/*
		 * sb.append("SELECT") .append(" visa.id id,") .append(
		 * " (select bank.bankName from plat_bank_info bank, agentinfo agent where bank.beLongPlatId = agent.id and bank.platType = 2 and bank.delFlag = 0 and agent.agentName = '拉美途') bankName,"
		 * ) .append(
		 * " (select bank.bankAccountCode from plat_bank_info bank, agentinfo agent where bank.beLongPlatId = agent.id and bank.platType = 2 and bank.delFlag = 0 and agent.agentName = '拉美途') bankAccount,"
		 * ) .append(" '0' budgetType,") .append(" '' createDate,")
		 * .append(" '' COMMENT,") .append(" '0' delFlag,")
		 * .append(" '' isJoin,") .append(" visa.id visaId,")
		 * .append(" '' nowLevel,") .append(" '' orderId,")
		 * .append(" '' orderType,") .append(" '' pay_apply_date,")
		 * .append(" '' payNowLevel,") .append(" '' payReview,")
		 * .append(" '' payReviewComment,") .append(" '' payReviewCompanyId,")
		 * .append(" '' payStatus,") .append(" '' payUpdateBy,")
		 * .append(" '' payUpdateDate,") .append(" '' printFlag,")
		 * .append(" '' printTime,") .append(" '' reviewComment,")
		 * .append(" '' reviewCompanyId,") .append(" '' reviewId,")
		 * .append(" '' reviewStatus,") .append(" '0' reviewType,")
		 * .append(" '' serialNum,") .append(" '' supplierType,") .append(" (")
		 * .append(" SELECT") .append(" agent.id") .append(" FROM")
		 * .append(" agentinfo agent") .append(" WHERE")
		 * .append(" agent.agentName = '拉美途'") .append(" ) supplyId,")
		 * .append(" '1' supplyType,") .append(" '' updateBy,")
		 * .append(" '' updateDate,") .append(" visa.id activityId,")
		 * .append(" '0' overseas,") .append(" visa.productName NAME,")
		 * .append(" visa.quantity,") .append(" '拉美途' supplyName,")
		 * .append(" visa.currencyId,") .append(" visa.visaPrice price,")
		 * .append(" (") .append(" SELECT") .append(" c.currency_exchangerate")
		 * .append(" FROM") .append(" currency c") .append(" WHERE")
		 * .append(" c.currency_id = visa.currencyId") .append(" ) rate,")
		 * .append(" (") .append(" SELECT") .append(" c.currency_id")
		 * .append(" FROM") .append(" currency c") .append(" WHERE")
		 * .append(" c.currency_id = visa.currencyId")
		 * .append(" AND c.currency_name = '人民币'") .append(" ) currencyAfter,")
		 * .append(" visa.quantity * visa.visaPrice * (") .append(" SELECT")
		 * .append(" c.currency_exchangerate") .append(" FROM")
		 * .append(" currency c") .append(" WHERE")
		 * .append(" c.currency_id = visa.currencyId") .append(" ) priceAfter,")
		 * .append(" '4' review,") .append(" g.createBy") .append(" FROM")
		 * .append(" (") .append(" SELECT") .append(" visa.id,")
		 * .append(" visa.productName,") .append(" visa.currencyId,")
		 * .append(" visa.visaPrice,") .append(" vo.activity_code,")
		 * .append(" vo.mainOrderId,") .append(" count(*) AS quantity")
		 * .append(" FROM") .append(" visa_products visa,")
		 * .append(" visa_order vo") .append(" WHERE")
		 * .append(" visa.id = vo.visa_product_id and vo.mainOrderId is not NULL"
		 * ) .append(" GROUP BY") .append(" visa.productName") .append(
		 * " ) visa inner JOIN productorder porder ON porder.orderNum = visa.mainOrderId"
		 * ) .append(
		 * " inner JOIN activitygroup g on g.id = porder.productGroupId and g.id = ?"
		 * );
		 */
		sb.append("SELECT")
				.append("	   vp.productName,")
				.append("	   vp.id,")
				.append("    vp.visaPrice,")
				.append("    count(*) as count,")
				.append("    po.operator,")
				.append("    vp.currencyId, ")
				.append("    po.id activityId ")
				.append("FROM ")
				.append("   visa_order vo ")
				.append(" LEFT JOIN visa_products vp ON vo.visa_product_id = vp.id ")
				.append(" LEFT JOIN ( ")
				.append(" 	SELECT ")
				.append(" 		(SELECT NAME FROM sys_user su WHERE su.id = g.createBy) AS operator,")
				.append("			o.orderNum,")
				.append("			g.id ")
				.append(" 	FROM ")
				.append("			productorder o ")
				.append(" 	LEFT JOIN activitygroup g ON g.id = o.productGroupId ")
				.append("  ) po ON vo.mainOrderId = po.orderNum ")
				.append(" WHERE ").append(" 	vo.del_flag = '0' ")
				.append(" AND vo.mainOrderId IS NOT NULL ")
				.append(" AND po.id = ? ").append(" GROUP BY vp.productName ");
		List<Map<String, Object>> visaList = costRecordDao.findBySql(
				sb.toString(), Map.class, groupId);
		List<CostRecord> list = new ArrayList<CostRecord>();
		if (null != visaList && visaList.size() > 0) {
			for (Map<String, Object> map : visaList) {
				CostRecord costRecord = new CostRecord();
				Integer currencyId = (Integer) map.get("currencyId");
				Integer visaProductId = (Integer) map.get("id");
				BigInteger count = (BigInteger) map.get("count");
				String productName = (String) map.get("productName");
				BigDecimal price = (BigDecimal) map.get("visaPrice");
				String operator = (String) map.get("operator");
				String activityId = map.get("activityId").toString();
				Currency currency = currencyService.findCurrency(Long
						.valueOf(String.valueOf(currencyId)));
				List<Currency> currencyList = currencyService
						.findCurrencyList(UserUtils.getUser().getCompany()
								.getId());
				Integer cnyID = null;
				for (Currency cny : currencyList) {
					if ("人民币".equals(cny.getCurrencyName())) {
						cnyID = Integer.parseInt(String.valueOf(cny.getId()));
					}
				}
				costRecord.setActivityId(Long.parseLong(activityId));
				costRecord.setName(productName);
				costRecord.setQuantity(count.intValue());
				String sql = "select id, agentName from agentinfo where agentName = '拉美途' and delFlag = 0";
				List<Map<String, Object>> agents = agentinfoDao.findBySql(sql,
						Map.class);
				if (agents != null && agents.size() > 0) {
					costRecord.setSupplyId(Integer.parseInt(agents.get(0)
							.get("id").toString()));
					costRecord.setSupplyName(agents.get(0).get("agentName")
							.toString());
				}

				costRecord.setCurrencyId(Integer.parseInt(String
						.valueOf(currencyId)));
				costRecord.setPrice(price);
				costRecord.setRate(currency.getCurrencyExchangerate());
				costRecord.setCurrencyAfter(cnyID);
				BigDecimal priceAfter = price.multiply(new BigDecimal(count))
						.multiply(currency.getCurrencyExchangerate());
				costRecord.setPriceAfter(priceAfter);
				costRecord.setReviewStatus("待提交审核");
				User u = new User();
				u.setName(operator);
				costRecord.setCreateBy(u);
				costRecord.setReviewType(0);
				costRecord.setBudgetType(0);
				costRecord.setOverseas(0);
				costRecord.setDelFlag("0");
				costRecord.setId(Long.valueOf(String.valueOf(visaProductId)));
				costRecord.setReview(4);
				costRecord.setReviewId(-1L);
				list.add(costRecord);
			}
		}
		return list;
	}

	/**
	 * 已收金额
	 * 
	 * @param id
	 * @return
	 */
	public Object getPayedMoney(Long id) {
		String sqlStr = "SELECT sum(m.amount) from pay_group p, money_amount m, currency c "
				+ "where m.currencyId=c.currency_id and p.cost_record_id = "
				+ id
				+ " and p.orderType not in (11, 12)"
				+ " and (p.isAsAccount is null or p.isAsAccount != 102)"
				+ " and p.payPrice = m.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
		return payGroupDao.getSession().createSQLQuery(sqlStr).uniqueResult();
	}

	/**
	 * 达帐金额
	 * 
	 * @param id
	 * @return
	 */
	public Object getConfirmMoney(Long id) {
		String sqlStr = "SELECT sum(m.amount) from pay_group p, money_amount m, currency c "
				+ "where m.currencyId=c.currency_id and p.cost_record_id = "
				+ id
				+ " and p.isAsAccount = 1 "
				+ " and p.orderType not in (11, 12)"
				+ " and p.payPrice = m.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
		return payGroupDao.getSession().createSQLQuery(sqlStr).uniqueResult();
	}

	public List<AbstractSpecificCost> getCostList(Long activityId, String type,
			String states) {
		List<Integer> stateList = new ArrayList<Integer>();
		if (StringUtils.isNotEmpty(states)) {
			String[] stateArray = states.split(",");
			for (String state : stateArray) {
				if (StringUtils.isNotBlank(state)) {
					stateList.add(StateMachineContext.getStateValue(state));
				}
			}
		}
		return costManagerDao.getSpecificCost(activityId,
				StateMachineContext.getMutableBeanType(type), stateList);
	}

	public Page<Map<Object, Object>> findCostRecordListForOtherIncome(
			String orderType, Page<Map<Object, Object>> page,
			Map<String, String> map) {
		String sql = "";
		if (StringUtils.isNotBlank(orderType)) {
			Integer type = Integer.parseInt(orderType);
			if (type == Context.ORDER_TYPE_ALL) {
				sql = findCostRecordListForAllOtherIncome(orderType, map);
			} else if (type == Context.ORDER_TYPE_JP) {
				sql = findCostRecordListForJPOtherIncome(orderType, map);
			} else if (type == Context.ORDER_TYPE_QZ) {
				sql = findCostRecordListForQZOtherIncome(orderType, map);
			} else if (type == Context.ORDER_TYPE_ISLAND) {
				sql = findCostRecordListForISLANDOtherIncome(orderType, map);
			} else if (type == Context.ORDER_TYPE_HOTEL) {
				sql = findCostRecordListForHOTELOtherIncome(orderType, map);
			} else {// 单团、散拼、游学、大客户、自由行、游轮
				sql = findCostRecordListForDTOtherIncome(orderType, map);
			}
		} else {// 默认全部
			sql = findCostRecordListForAllOtherIncome(orderType, map);
		}
		if (StringUtils.isBlank(page.getOrderBy())) {
			page.setOrderBy("updateDate DESC");
		}
		if ("1".equals(map.get("flag"))) {// add by chy 2015年12月23日15:45:46
											// 导出Excel时查询2000条数据
			page.setPageNo(0);
			page.setMaxSize(Integer.MAX_VALUE);
		}
		return costRecordDao.findBySql(page, sql, Map.class);
	}

	/**
	 * 查询其他收入收款未达账的总数其他收入收款全部查询
	 * 
	 * @return 未达账的总数
	 * @author shijun.liu
	 */
	public long findNotAccountedCountOtherIncome() {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("SELECT sum(count) FROM ")
				.append("(SELECT count(*) as count FROM")
				.append(" cost_record cost, pay_group pay, activitygroup g, travelactivity p ")
				.append(" WHERE pay.cost_record_id = cost.id AND pay.orderType = cost.orderType")
				.append(" AND p.id = g.srcActivityId AND g.id = cost.activityId AND cost.budgetType = 2")
				.append(" AND cost.orderType in (1,2,3,4,5,10) AND (pay.isAsAccount is null ")
				.append(" OR pay.isAsAccount = 101) AND p.proCompany = ")
				.append(companyId)
				.append(" UNION ")
				.append(" SELECT count(*) as count FROM cost_record cost, pay_group pay, ")
				.append(" activity_airticket p WHERE pay.cost_record_id = cost.id AND")
				.append(" pay.orderType = cost.orderType AND p.id = cost.activityId ")
				.append(" AND cost.budgetType = 2 AND cost.orderType = ")
				.append(Context.ORDER_TYPE_JP)
				.append(" AND (pay.isAsAccount is null OR pay.isAsAccount = 101)")
				.append(" AND p.proCompany = ")
				.append(companyId)
				.append(" UNION ")
				.append(" SELECT count(*) as count FROM ")
				.append(" cost_record cost, pay_group pay, visa_products p WHERE ")
				.append(" pay.cost_record_id = cost.id AND pay.orderType = cost.orderType ")
				.append(" AND p.id = cost.activityId  AND cost.budgetType = 2 AND cost.orderType = ")
				.append(Context.ORDER_TYPE_QZ)
				.append(" AND (pay.isAsAccount is null ")
				.append(" OR pay.isAsAccount = 101) AND p.proCompanyId = ")
				.append(companyId)
				.append(" UNION ")
				.append(" SELECT count(*) as count FROM ")
				.append(" cost_record_island cost, pay_group pay, activity_island_group g,")
				.append(" activity_island p WHERE pay.cost_record_id = cost.id AND ")
				.append(" pay.orderType = cost.orderType AND g.uuid = cost.activity_uuid ")
				.append(" AND p.uuid = g.activity_island_uuid AND cost.budgetType = 2 AND (pay.isAsAccount is null ")
				.append(" OR pay.isAsAccount = 101) and p.wholesaler_id = ")
				.append(companyId)
				.append(" UNION ")
				.append(" SELECT count(*) as count FROM ")
				.append(" cost_record_hotel cost, pay_group pay, activity_hotel_group g,")
				.append(" activity_hotel p WHERE pay.cost_record_id = cost.id AND ")
				.append(" pay.orderType = cost.orderType AND g.uuid = cost.activity_uuid ")
				.append(" AND p.uuid = g.activity_hotel_uuid AND cost.budgetType = 2 AND (pay.isAsAccount is null ")
				.append(" OR pay.isAsAccount = 101) AND p.wholesaler_id = ")
				.append(companyId).append(" ) T ");
		return costRecordDao.getCountBySQL(str.toString());
	}

	/**
	 * 其他收入收款查询条件
	 * 
	 * @param orderType
	 *            订单类型
	 * @param map
	 *            参数
	 * @return
	 */
	private String getWhereForOtherIncome(String orderType,
			Map<String, String> map) {
		StringBuffer str = new StringBuffer();
		String groupCode = map.get("groupCode"); // 团号
		String startCreateDate = map.get("startCreateDate"); // 收款日期开始
		String endCreateDate = map.get("endCreateDate"); // 收款日期结束
		String operatorId = map.get("jd"); // 计调
		String supplier = map.get("supplierInfo"); // 地接社
		String agentId = map.get("agentId"); // 渠道
		String currency = map.get("currency"); // 币种
		String startMoney = map.get("startMoney"); // 付款金额开始
		String endMoney = map.get("endMoney"); // 付款金额结束
		String isAsAccount = map.get("isAsAccount"); // 是否到账
		String printFlag = map.get("printFlag"); // 打印状态
		String payerName = map.get("payerName"); // 来款单位
		String toBankNname = map.get("toBankNname"); // 收款银行
		String accountDateBegin = map.get("accountDateBegin"); // 达账时间
		String accountDateEnd = map.get("accountDateEnd"); // 达账时间
		String payType = map.get("payType");// 支付方式
		String isKb = map.get("isKb");// 是否KB款
		String groupDateBegin = map.get("groupDateBegin"); //出团日期--开始
		String groupDateEnd = map.get("groupDateEnd"); //出团日期--结束
		String receiveConfirmDateBegin = map.get("receiveConfirmDateBegin");//收款确认日期-开始
		String receiveConfirmDateEnd = map.get("receiveConfirmDateEnd");//收款确认日期-结束
		String paymentType = map.get("paymentType");
		//新增是否KB款查询条件
		if (StringUtils.isNotBlank(isKb)) {
			if(Context.NumberDef.NUMER_ONE.toString().equals(isKb)){//1代表是KB款
				str.append(" and isKb = 1 ");
			} else {
				str.append(" and isKb <> 1 ");
			}
		}
		if (StringUtils.isNotBlank(payerName)) {
			str.append(" and payerName like '%").append(payerName).append("%'");
		}
		if (StringUtils.isNotBlank(toBankNname)) {
			str.append(" and toBankNname like '%").append(toBankNname).append("%'");
		}
		if (StringUtils.isNotBlank(groupCode)) {
			str.append(" and groupCode like '%").append(groupCode).append("%'");
		}
		if (StringUtils.isNotBlank(accountDateBegin)
				|| StringUtils.isNotBlank(accountDateEnd)) {
			str.append(" and isAsAccount=1");
			if (StringUtils.isNotBlank(accountDateBegin)) {
				str.append(" and accountDate >= '").append(accountDateBegin).append(" 00:00:00' ");
			}
			if (StringUtils.isNotBlank(accountDateEnd)) {
				str.append(" and accountDate <= '").append(accountDateEnd).append(" 23:59:59' ");
			}
		}
		if (StringUtils.isNotBlank(startCreateDate)) {
			str.append(" and createDate >= '").append(startCreateDate)
					.append(" 00:00:00' ");
		}
		if (StringUtils.isNotBlank(endCreateDate)) {
			str.append(" and createDate <= '").append(endCreateDate)
					.append(" 23:59:59' ");
		}
		if (StringUtils.isNotBlank(operatorId)) {
			str.append(" and operatorId=").append(Integer.parseInt(operatorId));
		}
		if (StringUtils.isNotBlank(currency)) {
			str.append(" and currencyId = ").append(Integer.parseInt(currency));
		}
		if (StringUtils.isNotBlank(startMoney)) {
			str.append(" and amount >= ").append(startMoney);
		}
		if (StringUtils.isNotBlank(endMoney)) {
			str.append(" and amount <= ").append(endMoney);
		}
		if (StringUtils.isNotBlank(agentId)) {
			str.append(" and supplyType=1 and supplyId = ").append(
					Integer.parseInt(agentId));
		}
		if (StringUtils.isNotBlank(supplier)) {
			str.append(" and supplyType=0 and supplyId = ").append(
					Integer.parseInt(supplier));
		}
		if ("Y".equals(isAsAccount)) {
			str.append(" and isAsAccount = 1");
		} else if ("N".equals(isAsAccount)) {
			str.append(" and (isAsAccount = 101 or isAsAccount is null)");
		} else if ("C".equals(isAsAccount)) {
			str.append(" and isAsAccount = 102");
		}
		if (StringUtils.isNotBlank(printFlag)) {
			if ("0".equals(printFlag)) {
				str.append(" and (printFlag = ")
						.append(Integer.parseInt(printFlag))
						.append(" or printFlag is null)");
			} else {
				str.append(" and printFlag = ").append(
						Integer.parseInt(printFlag));
			}

		}
		if (StringUtils.isNotBlank(payType)) {
			if ("3".equals(payType)) {
				str.append(" and payType in (3,5)");
			} else {
				str.append(" and payType =").append(payType);
			}
		}
		if (StringUtils.isNotBlank(groupDateBegin)) {
			str.append(" and groupOpenDate >= '").append(groupDateBegin).append("'");
		}
		if (StringUtils.isNotBlank(groupDateEnd)) {
			str.append(" and groupOpenDate <= '").append(groupDateEnd).append("'");
		}
		//
		if (StringUtils.isNotBlank(receiveConfirmDateBegin)) {
			str.append(" and receiptConfirmationDate >= '").append(receiveConfirmDateBegin).append(" 00:00:00'");
		}
		if (StringUtils.isNotBlank(receiveConfirmDateEnd)) {
			str.append(" and receiptConfirmationDate <= '").append(receiveConfirmDateEnd).append(" 23:59:59'");
		}
		if(StringUtils.isNotBlank(paymentType)){
			str.append(" AND supplyId in (select id from agentinfo where paymentType = "+paymentType+") ");
			
		}
		return str.toString();
	}

	/**
	 * 其他收入收款全部查询
	 * 
	 * @param orderType
	 *            订单类型
	 * @param map
	 *            参数
	 * @author shijun.liu
	 * @return
	 */
	private String findCostRecordListForAllOtherIncome(String orderType,
			Map<String, String> map) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = getWhereForOtherIncome(orderType, map);
		str.append("SELECT isKb,payType,accountDate,payerName, toBankNname,groupCode, productName, orderType, operatorId, supplyName,")
			.append("supplyType, supplyId, amount, isAsAccount, printFlag, printTime, currencyId,")
			.append("groupId, createDate, updateDate, productId, payGroupId, groupUUID, productUUID,name,groupOpenDate, ")
			.append("receiptConfirmationDate FROM (SELECT cost.kb as isKb,pay.payType,pay.accountDate,pay.payerName, ")
			.append(" pay.toBankNname,g.groupCode, p.acitivityName as productName, cost.orderType,")
			.append("cost.createBy AS operatorId, cost.supplyName, cost.supplyType, cost.supplyId, ma.amount,")
			.append("pay.isAsAccount, pay.printFlag, pay.printTime, ma.currencyId, g.id AS groupId, ")
			.append(" pay.createDate, pay.updateDate,g.srcActivityId AS productId,pay.id as payGroupId, ")
			.append(" ' ' as groupUUID,' ' as productUUID, cost.name,g.groupOpenDate, pay.receiptConfirmationDate ")
			.append(" FROM cost_record cost, pay_group pay, money_amount ma, activitygroup g, travelactivity p ")
			.append(" WHERE pay.cost_record_id = cost.id AND pay.orderType = cost.orderType")
			.append(" AND pay.payPrice = ma.serialNum AND p.id = g.srcActivityId AND g.id = cost.activityId ")
			.append(" AND cost.budgetType = 2 AND cost.orderType in (1,2,3,4,5,10) ")
			.append(" AND p.proCompany = ").append(companyId)
			.append(" UNION ")
			.append(" SELECT cost.kb as isKb,pay.payType,pay.accountDate,pay.payerName, pay.toBankNname, ")
			.append(" p.group_code AS groupCode, ' ' as productName, cost.orderType, ")
			.append(" cost.createBy AS operatorId, cost.supplyName, cost.supplyType, cost.supplyId, ma.amount, ")
			.append(" pay.isAsAccount, pay.printFlag, pay.printTime, ma.currencyId, '-1' AS groupId, pay.createDate,")
			.append(" pay.updateDate, p.id AS productId,pay.id as payGroupId, ' ' as groupUUID,' ' as productUUID, ")
			.append(" cost.name,p.outTicketTime groupOpenDate, pay.receiptConfirmationDate FROM cost_record cost, pay_group pay, ")
			.append(" money_amount ma, activity_airticket p WHERE pay.cost_record_id = cost.id AND")
			.append(" pay.orderType = cost.orderType AND pay.payPrice = ma.serialNum AND p.id = cost.activityId ")
			.append(" AND cost.budgetType = 2 AND cost.orderType =")
			.append(Context.ORDER_TYPE_JP).append(" AND p.proCompany = ").append(companyId)
			.append(" UNION ")
			.append(" SELECT cost.kb as isKb,pay.payType,pay.accountDate,pay.payerName, pay.toBankNname,")
			.append(" p.groupCode, p.productName, cost.orderType, cost.createBy AS operatorId, cost.supplyName,")
			.append(" cost.supplyType, cost.supplyId, ma.amount, pay.isAsAccount, pay.printFlag, pay.printTime, ma.currencyId,")
			.append(" '-1' AS groupId, pay.createDate, pay.updateDate,p.id AS productId,pay.id as payGroupId, ")
			.append(" ' ' as groupUUID,' ' as productUUID, cost.name,'' groupOpenDate, pay.receiptConfirmationDate ")
			.append(" FROM cost_record cost, pay_group pay, money_amount ma, visa_products p WHERE ")
			.append(" pay.cost_record_id = cost.id AND pay.orderType = cost.orderType AND pay.payPrice = ma.serialNum ")
			.append(" AND p.id = cost.activityId  AND cost.budgetType = 2 AND cost.orderType =")
			.append(Context.ORDER_TYPE_QZ).append(" AND p.proCompanyId = ").append(companyId)
			.append(" UNION ")
			.append(" SELECT '' as isKb,pay.payType,pay.accountDate,pay.payerName, pay.toBankNname,g.groupCode, ")
			.append(" p.activityName as productName, cost.orderType, cost.createBy AS operatorId, ")
			.append(" cost.supplyName, cost.supplyType, cost.supplyId, ma.amount, pay.isAsAccount,pay.printFlag, pay.printTime,")
			.append(" ma.currencyId, g.id AS groupId, pay.createDate, pay.updateDate,g.uuid AS productId, pay.id as payGroupId, ")
			.append(" g.uuid as groupUUID, p.uuid as productUUID, cost.name,g.groupOpenDate, pay.receiptConfirmationDate ")
			.append(" FROM cost_record_island cost, pay_group pay, island_money_amount ma, ")
			.append(" activity_island_group g, activity_island p WHERE pay.cost_record_id = cost.id AND ")
			.append(" pay.orderType = cost.orderType AND pay.payPrice = ma.serialNum AND g.uuid = cost.activity_uuid ")
			.append(" AND p.uuid = g.activity_island_uuid AND cost.budgetType = 2 AND cost.orderType =")
			.append(Context.ORDER_TYPE_ISLAND).append(" and p.wholesaler_id = ").append(companyId)
			.append(" UNION ")
			.append(" SELECT '' as isKb,pay.payType,pay.accountDate,pay.payerName, pay.toBankNname,g.groupCode, ")
			.append(" p.activityName as productName, cost.orderType, cost.createBy AS operatorId,")
			.append(" cost.supplyName, cost.supplyType, cost.supplyId, ma.amount, pay.isAsAccount,pay.printFlag, pay.printTime,")
			.append(" ma.currencyId, g.id AS groupId, pay.createDate, pay.updateDate,g.uuid AS productId, pay.id as payGroupId, ")
			.append(" g.uuid as groupUUID, p.uuid as productUUID, cost.name,g.groupOpenDate, pay.receiptConfirmationDate ")
			.append(" FROM cost_record_hotel cost, pay_group pay, hotel_money_amount ma, ")
			.append(" activity_hotel_group g, activity_hotel p WHERE pay.cost_record_id = cost.id AND ")
			.append(" pay.orderType = cost.orderType AND pay.payPrice = ma.serialNum AND g.uuid = cost.activity_uuid ")
			.append(" AND p.uuid = g.activity_hotel_uuid AND cost.budgetType = 2  AND cost.orderType =")
			.append(Context.ORDER_TYPE_HOTEL).append(" and p.wholesaler_id = ").append(companyId)
			.append(" ) T WHERE 1=1 ").append(where);
		return str.toString();
	}

	/**
	 * 其他收入收款机票查询
	 * 
	 * @param orderType
	 *            订单类型
	 * @param map
	 *            参数
	 * @author shijun.liu
	 * @return
	 */
	private String findCostRecordListForJPOtherIncome(String orderType,
			Map<String, String> map) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = getWhereForOtherIncome(orderType, map);
		str.append("SELECT isKb,payType,accountDate,payerName, toBankNname,groupCode, productName, orderType, operatorId, supplyName,")
			.append("supplyType, supplyId, amount, isAsAccount, printFlag, printTime, currencyId,")
			.append("groupId, createDate, updateDate, productId, payGroupId, groupUUID, productUUID, name, groupOpenDate ")
			.append("receiptConfirmationDate FROM (SELECT cost.kb as isKb,pay.payType,pay.accountDate,pay.payerName, ")
			.append(" pay.toBankNname, p.group_code AS groupCode, ' ' as productName, cost.orderType,")
			.append(" cost.createBy AS operatorId, cost.supplyName, cost.supplyType, cost.supplyId, ma.amount, ")
			.append(" pay.isAsAccount, pay.printFlag, pay.printTime, ma.currencyId, '-1' AS groupId, pay.createDate,")
			.append(" pay.updateDate, p.id AS productId,pay.id as payGroupId, ' ' as groupUUID, ' ' as productUUID, ")
			.append(" cost.name,p.outTicketTime groupOpenDate, pay.receiptConfirmationDate FROM cost_record cost, pay_group pay, ")
			.append(" money_amount ma, activity_airticket p WHERE pay.cost_record_id = cost.id AND")
			.append(" pay.orderType = cost.orderType AND pay.payPrice = ma.serialNum AND p.id = cost.activityId ")
			.append(" AND cost.budgetType = 2 AND cost.orderType = ")
			.append(Context.ORDER_TYPE_JP).append(" AND p.proCompany = ")
			.append(companyId).append(" ) T WHERE 1=1 ").append(where);
		return str.toString();
	}

	/**
	 * 其他收入收款签证查询
	 * 
	 * @param orderType
	 *            订单类型
	 * @param map
	 *            参数
	 * @author shijun.liu
	 * @return
	 */
	private String findCostRecordListForQZOtherIncome(String orderType,
			Map<String, String> map) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = getWhereForOtherIncome(orderType, map);
		str.append("SELECT isKb,payType,accountDate,payerName, toBankNname,groupCode, productName, orderType, operatorId, supplyName,")
			.append("supplyType, supplyId, amount, isAsAccount, printFlag, printTime, currencyId,")
			.append("groupId, createDate, updateDate, productId, payGroupId, groupUUID, productUUID, name, groupOpenDate, ")
			.append(" receiptConfirmationDate FROM (SELECT cost.kb as isKb,pay.payType,pay.accountDate,pay.payerName, pay.toBankNname, ")
			.append(" p.groupCode, p.productName, cost.orderType, cost.createBy AS operatorId, cost.supplyName, ")
			.append(" cost.supplyType, cost.supplyId, ma.amount, pay.isAsAccount, pay.printFlag, pay.printTime, ma.currencyId,")
			.append(" '-1' AS groupId, pay.createDate, pay.updateDate,p.id AS productId,pay.id as payGroupId, ")
			.append(" ' ' as groupUUID, ' ' as productUUID,cost.name,'' groupOpenDate, pay.receiptConfirmationDate ")
			.append(" FROM cost_record cost, pay_group pay, money_amount ma, visa_products p WHERE ")
			.append(" pay.cost_record_id = cost.id AND pay.orderType = cost.orderType AND pay.payPrice = ma.serialNum ")
			.append(" AND p.id = cost.activityId  AND cost.budgetType = 2 AND cost.orderType = ")
			.append(Context.ORDER_TYPE_QZ).append(" AND p.proCompanyId = ")
			.append(companyId).append(" ) T WHERE 1=1 ").append(where);
		return str.toString();
	}

	/**
	 * 其他收入收款酒店查询
	 * 
	 * @param orderType
	 *            订单类型
	 * @param map
	 *            参数
	 * @author shijun.liu
	 * @return
	 */
	private String findCostRecordListForHOTELOtherIncome(String orderType,
			Map<String, String> map) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = getWhereForOtherIncome(orderType, map);
		str.append("SELECT '' as isKb,payType, accountDate,payerName, toBankNname,groupCode, productName, orderType, operatorId, supplyName,")
			.append("supplyType, supplyId, amount, isAsAccount, printFlag, printTime, currencyId,")
			.append("groupId, createDate, updateDate, productId, payGroupId, groupUUID, productUUID, name, groupOpenDate, ")
			.append(" receiptConfirmationDate FROM (SELECT pay.payType,pay.accountDate,pay.payerName, ")
			.append(" pay.toBankNname,g.groupCode, p.activityName as productName, cost.orderType, cost.createBy AS operatorId, ")
			.append(" cost.supplyName, cost.supplyType, cost.supplyId, ma.amount, pay.isAsAccount,pay.printFlag, pay.printTime,")
			.append(" ma.currencyId, g.id AS groupId, pay.createDate, pay.updateDate,g.uuid AS productId, pay.id as payGroupId, ")
			.append(" g.uuid as groupUUID, p.uuid as productUUID,cost.name,g.groupOpenDate, pay.receiptConfirmationDate ")
			.append(" FROM cost_record_hotel cost, pay_group pay, hotel_money_amount ma, ")
			.append(" activity_hotel_group g, activity_hotel p WHERE pay.cost_record_id = cost.id AND ")
			.append(" pay.orderType = cost.orderType AND pay.payPrice = ma.serialNum AND g.uuid = cost.activity_uuid ")
			.append(" AND p.uuid = g.activity_hotel_uuid AND cost.budgetType = 2 and cost.orderType = ")
			.append(Context.ORDER_TYPE_HOTEL)
			.append(" and p.wholesaler_id = ").append(companyId)
			.append(" ) T WHERE 1=1 ").append(where);
		return str.toString();
	}

	/**
	 * 其他收入收款海岛查询
	 * 
	 * @param orderType
	 *            订单类型
	 * @param map
	 *            参数
	 * @author shijun.liu
	 * @return
	 */
	private String findCostRecordListForISLANDOtherIncome(String orderType,
			Map<String, String> map) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = getWhereForOtherIncome(orderType, map);
		str.append("SELECT '' as isKb,payType, accountDate,payerName, toBankNname,groupCode, productName, orderType, operatorId, supplyName,")
			.append("supplyType, supplyId, amount, isAsAccount, printFlag, printTime, currencyId,")
			.append("groupId, createDate, updateDate, productId, payGroupId, groupUUID, productUUID,name,groupOpenDate, ")
			.append("receiptConfirmationDate FROM (SELECT pay.payType,pay.accountDate,pay.payerName, pay.toBankNname, ")
			.append(" g.groupCode, p.activityName as productName, cost.orderType, cost.createBy AS operatorId,")
			.append(" cost.supplyName, cost.supplyType, cost.supplyId, ma.amount, pay.isAsAccount,pay.printFlag, pay.printTime,")
			.append(" ma.currencyId, g.id AS groupId, pay.createDate, pay.updateDate,g.uuid AS productId, pay.id as payGroupId, ")
			.append(" g.uuid as groupUUID, p.uuid as productUUID,cost.name,g.groupOpenDate, pay.receiptConfirmationDate ")
			.append(" FROM cost_record_island cost, pay_group pay, island_money_amount ma, ")
			.append(" activity_island_group g, activity_island p WHERE pay.cost_record_id = cost.id AND ")
			.append(" pay.orderType = cost.orderType AND pay.payPrice = ma.serialNum AND g.uuid = cost.activity_uuid ")
			.append(" AND p.uuid = g.activity_island_uuid AND cost.budgetType = 2 AND cost.orderType = ")
			.append(Context.ORDER_TYPE_ISLAND)
			.append(" and p.wholesaler_id = ").append(companyId)
			.append(" ) T WHERE 1=1 ").append(where);
		return str.toString();
	}

	/**
	 * 其他收入收款单团类查询
	 * 
	 * @param orderType
	 *            订单类型
	 * @param map
	 *            参数
	 * @author shijun.liu
	 * @return
	 */
	private String findCostRecordListForDTOtherIncome(String orderType,
			Map<String, String> map) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = getWhereForOtherIncome(orderType, map);
		str.append("SELECT payType,accountDate,payerName,toBankNname,groupCode, productName, orderType, operatorId, supplyName,")
			.append("supplyType, supplyId, amount, isAsAccount, printFlag, printTime, currencyId,")
			.append("groupId, createDate, updateDate, productId, payGroupId, groupUUID, productUUID,name,groupOpenDate, ")
			.append(" receiptConfirmationDate FROM (SELECT pay.payType,pay.accountDate,pay.payerName, pay.toBankNname,")
			.append(" g.groupCode, p.acitivityName as productName, cost.orderType,")
			.append(" cost.createBy AS operatorId, cost.supplyName, cost.supplyType, cost.supplyId, ma.amount,")
			.append(" pay.isAsAccount, pay.printFlag, pay.printTime, ma.currencyId, g.id AS groupId, ")
			.append(" pay.createDate, pay.updateDate,g.srcActivityId AS productId,pay.id as payGroupId, ")
			.append(" ' ' as groupUUID, ' ' as productUUID,cost.name,g.groupOpenDate, pay.receiptConfirmationDate ")
			.append(" FROM cost_record cost, pay_group pay, money_amount ma, activitygroup g, travelactivity p ")
			.append(" WHERE pay.cost_record_id = cost.id AND pay.orderType = cost.orderType")
			.append(" AND pay.payPrice = ma.serialNum AND p.id = g.srcActivityId AND g.id = cost.activityId ")
			.append(" AND cost.budgetType = 2 AND cost.orderType = ").append(orderType)
			.append(" AND p.proCompany = ").append(companyId)
			.append(" ) T WHERE 1=1 ").append(where);
		return str.toString();
	}

	public Page<Map<Object, Object>> findCostRecordList(String orderType,
			Page<Map<Object, Object>> page, String orderBy,
			Map<String, String> map, DepartmentCommon common) {
		String companyId = UserUtils.getUser().getCompany().getId().toString();

		Integer orderS = null;
		if (StringUtils.isNotBlank(orderType))
			orderS = Integer.parseInt(orderType);
		else
			orderS = 6;
		StringBuffer sqlBuffer =  new StringBuffer();
		sqlBuffer.append("SELECT payment.isNew,payment.rate,payment.budgetType,payment.payCreateDate,payment.payUpdateDate,payment.groupCode,")
				.append(" payment.activityId, payment.`name`, payment.id, payment.payFlag, ")
				.append("payment.review, payment.activityTypeId,payment.supplyId,payment.supplyType, payment.supplyName, payment.amount amount, ")
				.append("payment.currencyId, payment.createBy, payment.createDate, payment.updateDate, payment.acitivityName,")
				.append("payment.printFlag,payment.invoiceStatus, payment.printTime, payment.orderType,payment.groupid,payment.proid, ")
				.append("payment.f,payment.confirmCashierDate,payment.costVoucher,payment.payReviewUuid ")
				.append(" FROM ( SELECT cr.isNew,cr.rate,cr.budgetType,cr.payCreateDate,cr.payUpdateDate,pro.groupCode, cr.activityId,")
		.append("cr.`name`, cr.id, cr.payStatus payFlag, cr.review, cr.orderType activityTypeId,cr.supplyId,cr.supplyType, cr.supplyName, ")
		.append(" cr.amount, cr.currencyId, pro.createBy, cr.createDate, cr.updateDate, pro.acitivityName, cr.printFlag,cr.invoiceStatus, ")
		.append("cr.printTime, cr.orderType,pro.groupid,pro.proid,cr.f,cr.confirmCashierDate,cr.costVoucher,cr.payReviewUuid ");
		sqlBuffer.append(" FROM (SELECT costrecord.pay_apply_date payCreateDate,costrecord.payUpdateDate,costrecord.rate,");
		//添加海岛游、酒店业务20150614
		if(orderS > Context.ORDER_TYPE_CRUISE) {
			sqlBuffer.append("costrecord.activity_uuid activityId,");
		} else {
			sqlBuffer.append("costrecord.activityId,");
		}
		sqlBuffer.append("costrecord.`name`, costrecord.review,costrecord.orderType, costrecord.supplyName, ")
			.append(" costrecord.id, costrecord.payStatus, costrecord.printFlag, costrecord.invoiceStatus,")
			.append("costrecord.printTime,costrecord.supplyId, costrecord.supplyType, ")
			.append("costrecord.price * costrecord.quantity amount,costrecord.currencyId,costrecord.createDate,")
			.append("costrecord.updateDate,(select count(r.id) from refund r where r.moneyType=1 and r.record_id=costrecord.id")
		    .append(" and r.status is null and r.del_flag=0) f,costrecord.confirmCashierDate, ");
		if(orderS == Context.ORDER_TYPE_ALL){
			sqlBuffer.append(" costrecord.is_new isNew,costrecord.budgetType,costrecord.costVoucher,  ")
					.append(" costrecord.pay_review_uuid AS payReviewUuid FROM cost_record costrecord")
					.append(" WHERE costrecord.payReview =2 AND reviewType = 0 AND ")
					.append("costrecord.delFlag = 0 and costrecord.budgetType = 1 and costrecord.orderType=")
					.append(Context.ORDER_TYPE_QZ)
					.append(" ) cr,(SELECT p.createBy, ' ' groupid, p.id proid, p.groupCode, ")
			        .append(" p.productName acitivityName FROM visa_products p ")
					.append(" WHERE p.delFlag = 0 AND p.proCompanyId = ").append(companyId)
					.append(") pro WHERE cr.activityId = pro.proid ");
		if(map.get("payType") != null && !"".equals(map.get("payType"))) {
			sqlBuffer.append(" and EXISTS (select * from refund r where r.record_id = cr.id  and pay_type = ")
					.append(map.get("payType"))
					.append(" and moneyType in(1,7) and orderType = 6) ");
		}
		if(map.get("bankName") != null && !"".equals(map.get("bankName"))) {
			sqlBuffer.append(" and EXISTS (select * from pay_remittance pay,refund re where re.record_id = cr.id AND re.pay_type_Id = pay.id ")
					.append(" AND re.del_flag = 0 AND pay.del_flag = 0 AND re.pay_type = 4 AND pay.bank_name = '")
					.append(map.get("bankName"))
					.append("' and moneyType in(1,7) and cr.orderType = 6) ");
		}
			sqlBuffer.append(" union")
					.append(" SELECT cr.isNew,cr.rate,cr.budgetType,cr.payCreateDate,cr.payUpdateDate,pro.groupCode, cr.activityId,")
					.append(" cr.`name`, cr.id, cr.payStatus payFlag, cr.review, cr.orderType activityTypeId,")
					.append(" cr.supplyId,cr.supplyType, cr.supplyName, cr.amount, cr.currencyId, pro.createBy, ")
					.append(" cr.createDate, cr.updateDate, pro.acitivityName,")
					.append(" cr.printFlag,cr.invoiceStatus, cr.printTime, cr.orderType,pro.groupid,pro.proid,cr.f,cr.confirmCashierDate,cr.costVoucher,")
					.append(" cr.payReviewUuid ");

			sqlBuffer.append(" FROM (SELECT costrecord.pay_apply_date payCreateDate,costrecord.payUpdateDate,")
					.append(" costrecord.rate,costrecord.activityId, costrecord.`name`, costrecord.review,")
					.append(" costrecord.supplyId,costrecord.orderType, costrecord.supplyName, costrecord.id,")
					.append(" costrecord.payStatus, costrecord.printFlag,costrecord.invoiceStatus, costrecord.printTime,")
					.append(" costrecord.supplyType, costrecord.price * costrecord.quantity amount, costrecord.currencyId, ")
					.append(" costrecord.createDate,costrecord.updateDate,costrecord.is_new isNew,  ")
					.append(" costrecord.budgetType,costrecord.costVoucher,costrecord.pay_review_uuid AS payReviewUuid,  ")
					.append(" (select count(r.id) from refund r where r.moneyType=1")
				 	.append(" and r.record_id=costrecord.id and r.status is null and r.del_flag=0) f, costrecord.confirmCashierDate ")
					.append(" FROM cost_record costrecord WHERE ")
					.append(" costrecord.payReview =2 AND reviewType = 0 AND costrecord.delFlag = 0 and costrecord.budgetType = 1 and ")
					.append("costrecord.orderType IN (1,2,3,4,5,10) ) cr,")
					.append("(SELECT activitygroup.createBy, activitygroup.id groupid, travelactivity.id proid,activitygroup.groupCode groupCode,")
					.append(" travelactivity.acitivityName acitivityName FROM activitygroup activitygroup,")
					.append(" ( SELECT t.acitivityName, t.id FROM travelactivity t WHERE t.proCompany= ")
					.append(companyId)
					.append(") travelactivity where ")
					.append("activitygroup.delFlag = 0 AND activitygroup.srcActivityId = travelactivity.id) pro WHERE cr.activityId = pro.groupid ");
		if(map.get("payType") != null && !"".equals(map.get("payType"))) {
			sqlBuffer.append(" and EXISTS (select * from refund r where r.record_id = cr.id and pay_type = ")
					.append(map.get("payType"))
					.append(" and moneyType in(1,7) and orderType in (1,2,3,4,5,10)) ");
		}
		if(map.get("bankName") != null && !"".equals(map.get("bankName"))) {
			sqlBuffer.append(" and EXISTS (select * from pay_remittance pay,refund re where re.record_id = cr.id AND re.pay_type_Id = pay.id ")
					.append(" AND re.del_flag = 0 AND pay.del_flag = 0 AND re.pay_type = 4 AND pay.bank_name = '")
					.append(map.get("bankName"))
					.append("' and moneyType in(1,7) and cr.orderType in (1,2,3,4,5,10)) ");
		}
			sqlBuffer.append(" UNION")
					.append(" SELECT cr.isNew,cr.rate,cr.budgetType,cr.payCreateDate,cr.payUpdateDate,pro.groupCode, cr.activityId, cr.`name`,")
					.append(" cr.id, cr.payStatus payFlag, cr.review, cr.orderType activityTypeId,")
					.append(" cr.supplyId,cr.supplyType, cr.supplyName, cr.amount, cr.currencyId, pro.createBy,")
					.append(" cr.createDate, cr.updateDate,pro.acitivityName,")
					.append(" cr.printFlag,cr.invoiceStatus, cr.printTime, cr.orderType,pro.groupid,pro.proid,cr.f,cr.confirmCashierDate,cr.costVoucher,")
					.append(" cr.payReviewUuid  ");

			sqlBuffer.append("FROM ( SELECT costrecord.pay_apply_date payCreateDate,costrecord.payUpdateDate,costrecord.rate,")
					.append(" costrecord.activityId, costrecord.`name`, costrecord.review,")
					.append(" costrecord.orderType, costrecord.supplyName, costrecord.id, costrecord.payStatus,")
					.append(" costrecord.printFlag,costrecord.invoiceStatus, costrecord.printTime,")
					.append(" costrecord.supplyId,costrecord.supplyType, costrecord.price * costrecord.quantity amount, costrecord.currencyId, ")
					.append(" costrecord.createDate,costrecord.updateDate,costrecord.is_new isNew, ")
					.append(" costrecord.budgetType,costrecord.costVoucher,costrecord.pay_review_uuid AS payReviewUuid, ")
					.append(" (select count(r.id) from refund r ")
					.append(" where r.moneyType=1 and r.record_id=costrecord.id and r.status is null and r.del_flag=0) f,costrecord.confirmCashierDate ");

			sqlBuffer.append(" FROM cost_record costrecord WHERE ")
					.append("costrecord.payReview =2 AND reviewType = 0 AND costrecord.delFlag = 0 and costrecord.budgetType = 1 AND ")
					.append("costrecord.orderType = ").append(Context.ORDER_TYPE_JP)
					.append(") cr, ( SELECT activityairticket.createBy, ' ' groupid, activityairticket.id proid, activityairticket.group_code groupCode,")
					.append(" ' ' acitivityName FROM activity_airticket activityairticket ")
					.append(" WHERE ")
					.append(" activityairticket.delflag = 0 AND activityairticket.proCompany =")
					.append(companyId)
					.append(" ) pro WHERE cr.activityId = pro.proid ");
		if(map.get("payType") != null && !"".equals(map.get("payType"))) {
			sqlBuffer.append(" and EXISTS (select * from refund r where r.record_id = cr.id and pay_type = ")
					.append(map.get("payType"))
					.append(" and moneyType in(1,7) and orderType =7) ");
		}
		if(map.get("bankName") != null && !"".equals(map.get("bankName"))) {
			sqlBuffer.append(" and EXISTS (select * from pay_remittance pay,refund re where re.record_id = cr.id AND re.pay_type_Id = pay.id ")
					.append(" AND re.del_flag = 0 AND pay.del_flag = 0 AND re.pay_type = 4 AND pay.bank_name = '")
					.append(map.get("bankName"))
					.append("' and moneyType in(1,7) and cr.orderType =7) ");
		}
			sqlBuffer.append(" union ")
					.append(" SELECT cr.isNew,cr.rate,cr.budgetType,cr.payCreateDate,cr.payUpdateDate,pro.groupCode, cr.activityId,")
					.append("  cr.`name`, cr.id, cr.payStatus payFlag, cr.review, cr.orderType activityTypeId,")
					.append(" cr.supplyId,cr.supplyType, cr.supplyName, cr.amount, cr.currencyId, pro.createBy, ")
					.append(" cr.createDate, cr.updateDate,pro.acitivityName,cr.printFlag,cr.invoiceStatus, cr.printTime,")
					.append(" cr.orderType,pro.groupid,pro.proid,cr.f,cr.confirmCashierDate,cr.costVoucher,'' AS payReviewUuid")
					.append(" FROM (SELECT costrecord.pay_apply_date payCreateDate,costrecord.payUpdateDate,costrecord.rate,costrecord.activity_uuid activityId,")
					.append("costrecord.`name`, costrecord.review, ")
					.append("costrecord.orderType, costrecord.supplyName, costrecord.id, costrecord.payStatus, costrecord.printFlag,costrecord.invoiceStatus, ")
					.append("costrecord.printTime,costrecord.supplyId, costrecord.supplyType, ")
					.append("costrecord.price * costrecord.quantity amount,")
					.append("costrecord.currencyId, costrecord.createDate, costrecord.updateDate,0 isNew,costrecord.budgetType,null costVoucher,")
					.append("(select count(r.id) from refund r where r.moneyType=1 and r.record_id=costrecord.id and r.status is null and r.del_flag=0) f, ")
					.append("costrecord.confirmCashierDate FROM cost_record_hotel costrecord WHERE costrecord.payReview = 2 ")
					.append(" and costrecord.reviewType = 0 AND costrecord.delFlag = 0 and costrecord.budgetType = 1 and costrecord.orderType=")
					.append(Context.ORDER_TYPE_HOTEL)
					.append(") cr,(select activitygroup.createBy,")
					.append("activitygroup.uuid groupid,activity_hotel.uuid proid,activitygroup.groupCode,")
					.append("activity_hotel.activityName acitivityName from ")
					.append("activity_hotel_group activitygroup,(select t.activityName,t.id,t.uuid from activity_hotel t ")
					.append("WHERE t.wholesaler_id = ")
					.append(companyId)
					.append(" ) activity_hotel where activitygroup.delFlag = 0 AND activitygroup.activity_hotel_uuid = activity_hotel.uuid ")
					.append(") pro where cr.activityId = pro.groupid ")
					.append(" union ")
					.append(" SELECT cr.isNew,cr.rate,cr.budgetType,cr.payCreateDate,cr.payUpdateDate,pro.groupCode, cr.activityId,")
					.append(" cr.`name`, cr.id, cr.payStatus payFlag, cr.review, cr.orderType activityTypeId,")
					.append(" cr.supplyId,cr.supplyType, cr.supplyName, cr.amount, cr.currencyId, pro.createBy,")
					.append(" cr.createDate, cr.updateDate,pro.acitivityName,cr.printFlag,cr.invoiceStatus, cr.printTime, cr.orderType,")
					.append(" pro.groupid,pro.proid,cr.f,cr.confirmCashierDate,cr.costVoucher,'' AS payReviewUuid FROM (")
					.append("SELECT costrecord.pay_apply_date payCreateDate,costrecord.payUpdateDate,costrecord.rate,costrecord.activity_uuid activityId,")
					.append("costrecord.`name`, costrecord.review, ")
					.append("costrecord.orderType, costrecord.supplyName, costrecord.id, costrecord.payStatus, costrecord.printFlag,costrecord.invoiceStatus, ")
					.append("costrecord.printTime,costrecord.supplyId, costrecord.supplyType, ")
					.append("costrecord.price * costrecord.quantity amount,")
					.append("costrecord.currencyId,costrecord.createDate, costrecord.updateDate,0 isNew,costrecord.budgetType,null costVoucher,")
					.append("(select count(r.id) from refund r where r.moneyType=1 and r.record_id=costrecord.id and r.status is null and r.del_flag=0) f,")
					.append("costrecord.confirmCashierDate FROM cost_record_island costrecord WHERE costrecord.payReview = 2 ")
					.append(" and costrecord.reviewType = 0 AND costrecord.delFlag = 0 and costrecord.budgetType = 1 and costrecord.orderType=")
					.append(Context.ORDER_TYPE_ISLAND)
					.append(") cr,(select activitygroup.createBy,")
					.append("activitygroup.uuid groupid,activity_island.uuid proid,activitygroup.groupCode,")
					.append("activity_island.activityName acitivityName from ")
					.append("activity_island_group activitygroup,(select t.activityName,t.id,t.uuid from activity_island t ")
					.append("WHERE t.wholesaler_id = ")
					.append(companyId)
					.append(" ) activity_island where activitygroup.delFlag = 0 AND activitygroup.activity_island_uuid = activity_island.uuid ")
					.append(") pro where cr.activityId = pro.groupid ")
					.append(" ) payment where 1=1  ");

		}
		else if(orderS == Context.ORDER_TYPE_JP) {
				sqlBuffer.append(" costrecord.is_new isNew,costrecord.budgetType,costrecord.costVoucher,")
				.append(" costrecord.pay_review_uuid AS payReviewUuid FROM cost_record costrecord ")
				.append(" WHERE costrecord.payReview =2 AND reviewType<>1  AND reviewType<>2  AND costrecord.delFlag = 0 and ")
				.append("costrecord.budgetType = 1 and costrecord.orderType=").append(orderS).append(") cr,")
				.append("( SELECT activityairticket.createBy, ' ' groupid, activityairticket.id proid, activityairticket.group_code groupCode, ")
				.append(" ' ' acitivityName FROM activity_airticket activityairticket WHERE")
				.append(" activityairticket.delflag = 0  AND activityairticket.proCompany =")
				.append(companyId)
				.append(" ) pro WHERE cr.activityId = pro.proid ) payment WHERE 1 = 1");
		}
		else if (orderS == Context.ORDER_TYPE_QZ) {
			sqlBuffer.append(" costrecord.is_new isNew,costrecord.budgetType,costrecord.costVoucher,")
					.append(" costrecord.pay_review_uuid AS payReviewUuid From cost_record costrecord  ")
					.append(" WHERE costrecord.payReview =2 AND reviewType<>1  AND reviewType<>2 AND costrecord.delFlag = 0 and ")
					.append("costrecord.budgetType = 1 and costrecord.orderType=").append(orderS).append(") cr,")
					.append("( SELECT p.createBy, ' 'groupid, p.id proid, p.groupCode groupCode, ")
					.append(" p.productName acitivityName ")
					.append(" FROM visa_products p WHERE p.delFlag=0 and p.proCompanyId=").append(companyId)
					.append(" ) pro where cr.activityId = pro.proid")
					.append(") payment  where 1=1");
		}
		// 添加海岛游、酒店业务20150614
		else if (orderS == Context.ORDER_TYPE_HOTEL) {
			sqlBuffer.append(" 0 isNew,costrecord.budgetType,null costVoucher,null payReviewUuid From cost_record_hotel costrecord ")
					.append(" WHERE costrecord.payReview = 2 and costrecord.reviewType = 0 AND costrecord.delFlag = 0 and ")
					.append("costrecord.budgetType = 1 and costrecord.orderType=").append(orderS).append(") cr,(select activitygroup.createBy,")
					.append("activitygroup.uuid groupid,activity_hotel.uuid proid,activitygroup.groupCode,")
					.append("activity_hotel.activityName acitivityName from ")
					.append("activity_hotel_group activitygroup,(select t.activityName,t.id,t.uuid from activity_hotel t ")
					.append("WHERE t.wholesaler_id = ").append(companyId)
					.append(" ) activity_hotel where activitygroup.delFlag = 0 AND activitygroup.activity_hotel_uuid = activity_hotel.uuid ")
					.append(") pro where cr.activityId = pro.groupid ) payment where 1 = 1");
		} 
		else if (orderS == Context.ORDER_TYPE_ISLAND) {
			
			sqlBuffer.append(" 0 isNew,costrecord.budgetType,null costVoucher,null payReviewUuid From cost_record_island costrecord ")
			.append(" WHERE costrecord.payReview = 2 and costrecord.reviewType = 0 AND costrecord.delFlag = 0 and ")
			.append("costrecord.budgetType = 1 and costrecord.orderType=").append(orderS).append(") cr,(select activitygroup.createBy,")
			.append("activitygroup.uuid groupid,activity_island.uuid proid,activitygroup.groupCode,")
			.append("activity_island.activityName acitivityName from ")
			.append("activity_island_group activitygroup,(select t.activityName,t.id,t.uuid from activity_island t ")
			.append("WHERE t.wholesaler_id = ").append(companyId)
			.append(" ) activity_island where activitygroup.delFlag = 0 AND activitygroup.activity_island_uuid = activity_island.uuid ")
			.append(") pro where cr.activityId = pro.groupid ) payment where 1 = 1");
		}
		else {
			sqlBuffer.append(" costrecord.is_new isNew,costrecord.budgetType,costrecord.costVoucher,costrecord.pay_review_uuid AS payReviewUuid" +
					" FROM cost_record costrecord WHERE costrecord.payReview =2 AND reviewType<>1  AND reviewType<>2" +
					" AND costrecord.delFlag = 0 and costrecord.budgetType = 1 " +
					"and costrecord.orderType=").append(orderS).append(" ) cr,(SELECT activitygroup.createBy, " +
					"activitygroup.id groupid, travelactivity.id proid, activitygroup.groupCode groupCode, " +
					"travelactivity.acitivityName acitivityName FROM activitygroup activitygroup, " +
					"( SELECT t.acitivityName, t.id FROM travelactivity t WHERE t.proCompany = ")
			.append(companyId).append(" ) travelactivity WHERE activitygroup.delFlag = 0 AND activitygroup.srcActivityId = travelactivity.id " +
							") pro WHERE cr.activityId = pro.groupid) payment where 1=1");
		}
		
		//团号
		String groupCode = map.get("groupCode");
		if (StringUtils.isNotBlank(groupCode)) {
			sqlBuffer.append(" and payment.groupCode like '%" + groupCode
					+ "%'");
		}
		// 申请日期开始
		if (StringUtils.isNotBlank(map.get("createTimeMin"))) {
			sqlBuffer.append(" and payment.payCreateDate >= '").append(map.get("createTimeMin").trim()).append(" 00:00:00'");
		}
		// 申请日期结束
		if (StringUtils.isNotBlank(map.get("createTimeMax"))) {
			sqlBuffer.append(" and payment.payCreateDate <= '").append(map.get("createTimeMax").trim()).append(" 23:59:59'");
		}
		//出纳确认时间开始
		if (StringUtils.isNotBlank(map.get("confirmCashierDateBegin"))) {
			sqlBuffer.append(" and payment.confirmCashierDate >= '").append(map.get("confirmCashierDateBegin").trim()).append(" 00:00:00'");
		}
		//出纳确认时间结束
		if (StringUtils.isNotBlank(map.get("confirmCashierDateEnd"))) {
			sqlBuffer.append(" and payment.confirmCashierDate <= '").append(map.get("confirmCashierDateEnd").trim()).append(" 23:59:59'");
		}
		// 批发商编号
		String supplier = map.get("supplier");
		String agentId = map.get("agentId");
		if (StringUtils.isNotBlank(agentId) && StringUtils.isNotBlank(supplier)) {
			sqlBuffer
					.append(" and (payment.supplyType=0 and  payment.supplyId = ")
					.append(supplier)
					.append(" or payment.supplyType=1 and payment.supplyId=")
					.append(agentId).append(") ");
		} else if (StringUtils.isBlank(agentId)
				&& StringUtils.isNotBlank(supplier)) {
			sqlBuffer
					.append(" and payment.supplyType=0 and  payment.supplyId = "
							+ supplier);
		} else if (StringUtils.isBlank(supplier)
				&& StringUtils.isNotBlank(agentId)) {
			sqlBuffer
					.append(" and payment.supplyType=1 and  payment.supplyId = "
							+ agentId);
		}
		// 付款状态
		String payState = map.get("payState");
		if (StringUtils.isNotBlank(payState)) {
			if ("N".equals(payState)) {
				sqlBuffer.append(" and payment.payFlag = 0");
			}
			if ("Y".equals(payState)) {
				sqlBuffer.append(" and payment.payFlag = 1");
			}

		}

		// 计调条件
		if (StringUtils.isNotBlank(map.get("jds"))) {
			sqlBuffer.append(" and payment.createBy=").append(map.get("jds"));
		}
		// 币种
		String currency = map.get("currency");
		if (StringUtils.isNotBlank(currency)) {
			sqlBuffer.append(" and payment.currencyId = " + currency);

		}
		String printFlag = map.get("printFlag");
		if (StringUtils.isNotBlank(printFlag)) {
			if ("1".equals(printFlag)) {
				sqlBuffer.append(" and payment.printFlag=" + printFlag);
			} else {
				sqlBuffer.append(
						" and (payment.printFlag is null or payment.printFlag="
								+ printFlag).append(")");
			}
		}
		String startMoney = map.get("startMoney");
		String endMoney = map.get("endMoney");

		if (StringUtils.isNotBlank(startMoney)) {
			sqlBuffer.append(" and payment.amount >= " + startMoney);
		}
		if (StringUtils.isNotBlank(endMoney)) {
			sqlBuffer.append(" and payment.amount <= " + endMoney);
		}
		String moneyNum = map.get("moneyNum");
		if (StringUtils.isNotBlank(moneyNum) && "no".equals(moneyNum)) {
			sqlBuffer.append(" and payment.f = 0");

		}
		String paymentType = map.get("paymentType");
		if(StringUtils.isNotBlank(paymentType)){
			sqlBuffer.append(" and payment.supplyId in( select id from agentinfo where paymentType = " + paymentType + " ) ");
		}
		String order = page.getOrderBy();
		if (StringUtils.isBlank(order)) {
			page.setOrderBy("updateDate DESC ");
		}
		return costRecordDao.findBySql(page, sqlBuffer.toString(), Map.class);
	}

	/**
	 * 俄风行成本付款列表页（俄风行成本付款列表页-团队类型只有酒店、海岛游、全部三种类型）
	 * **/
	public Page<Map<Object, Object>> findCostRecordListForEFX(String orderType,
			Page<Map<Object, Object>> page, String orderBy,
			Map<String, String> map, DepartmentCommon common) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		Integer orderS = null;
		if (StringUtils.isNotBlank(orderType))
			orderS = Integer.parseInt(orderType);
		else
			orderS = Context.ORDER_TYPE_ALL;
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer
				.append("SELECT  payment.payCreateDate,payment.groupCode, payment.activityId, payment.`name`, payment.id, payment.payFlag, ")
				.append("payment.review, payment.activityTypeId,payment.supplyId,payment.supplyType, payment.supplyName, payment.amount amount, ")
				.append("payment.currencyId, payment.createBy, payment.createDate, payment.updateDate, payment.acitivityName,")
				.append("payment.printFlag, payment.printTime, payment.orderType,payment.groupid,payment.proid FROM ( SELECT cr.payCreateDate,pro.groupCode, cr.activityId,")
				.append("cr.`name`, cr.id, cr.payStatus payFlag, cr.review, cr.orderType activityTypeId,cr.supplyId,cr.supplyType, cr.supplyName, ")
				.append(" cr.amount, cr.currencyId, pro.createBy, cr.createDate, cr.updateDate, pro.acitivityName, cr.printFlag, ")
				.append("cr.printTime, cr.orderType,pro.groupid,pro.proid FROM (SELECT costrecord.pay_apply_date payCreateDate,costrecord.activity_uuid activityId,")
				.append("costrecord.`name`, costrecord.review, ")
				.append("costrecord.orderType, costrecord.supplyName, costrecord.id, costrecord.payStatus, costrecord.printFlag, ")
				.append("costrecord.printTime,costrecord.supplyId, costrecord.supplyType, ")
				.append("costrecord.price * costrecord.quantity amount,")
				.append("costrecord.currencyId,costrecord.createDate,costrecord.updateDate FROM ");
		if (orderS == Context.ORDER_TYPE_ALL) {
			sqlBuffer
					.append("cost_record_hotel costrecord WHERE costrecord.payReview = 2 and costrecord.reviewType = 0 AND costrecord.delFlag = 0 and ")
					.append("costrecord.budgetType = 1 and costrecord.orderType=")
					.append(Context.ORDER_TYPE_HOTEL)
					.append(") cr,(select activitygroup.createBy,")
					.append("activitygroup.uuid groupid,activity_hotel.uuid proid,activitygroup.groupCode,")
					.append("activity_hotel.activityName acitivityName from ")
					.append("activity_hotel_group activitygroup,(select t.activityName,t.id,t.uuid from activity_hotel t ")
					.append("WHERE t.wholesaler_id = ")
					.append(companyId)
					.append(" ) activity_hotel where activitygroup.delFlag = 0 AND activitygroup.activity_hotel_uuid = activity_hotel.uuid ")
					.append(") pro where cr.activityId = pro.groupid ")
					.append(" union ")
					.append(" SELECT cr.payCreateDate,pro.groupCode, cr.activityId, cr.`name`, cr.id, cr.payStatus payFlag, cr.review, cr.orderType activityTypeId,")
					.append(" cr.supplyId,cr.supplyType, cr.supplyName, cr.amount, cr.currencyId, pro.createBy,")
					.append(" cr.createDate, cr.updateDate,pro.acitivityName,")
					.append(" cr.printFlag, cr.printTime, cr.orderType,pro.groupid,pro.proid FROM (")
					.append("SELECT costrecord.pay_apply_date payCreateDate,costrecord.activity_uuid activityId,")
					.append("costrecord.`name`, costrecord.review, ")
					.append("costrecord.orderType, costrecord.supplyName, costrecord.id, costrecord.payStatus, costrecord.printFlag, ")
					.append("costrecord.printTime,costrecord.supplyId, costrecord.supplyType, ")
					.append("costrecord.price * costrecord.quantity amount,")
					.append("costrecord.currencyId,costrecord.createDate, costrecord.updateDate FROM ")
					.append(" cost_record_island costrecord WHERE costrecord.payReview = 2 and costrecord.reviewType = 0 AND costrecord.delFlag = 0 and ")
					.append("costrecord.budgetType = 1 and costrecord.orderType=")
					.append(Context.ORDER_TYPE_ISLAND)
					.append(") cr,(select activitygroup.createBy,")
					.append("activitygroup.uuid groupid,activity_island.uuid proid,activitygroup.groupCode,")
					.append("activity_island.activityName acitivityName from ")
					.append("activity_island_group activitygroup,(select t.activityName,t.id,t.uuid from activity_island t ")
					.append("WHERE t.wholesaler_id = ")
					.append(companyId)
					.append(" ) activity_island where activitygroup.delFlag = 0 AND activitygroup.activity_island_uuid = activity_island.uuid ")
					.append(") pro where cr.activityId = pro.groupid ")
					.append(" ) payment where 1=1  ");
		}

		// 添加海岛游、酒店业务20150614
		else if (orderS == Context.ORDER_TYPE_HOTEL) {
			sqlBuffer
					.append(" cost_record_hotel costrecord WHERE costrecord.payReview = 2 and costrecord.reviewType = 0 AND costrecord.delFlag = 0 and ")
					.append("costrecord.budgetType = 1 and costrecord.orderType=")
					.append(orderS)
					.append(") cr,(select activitygroup.createBy,")
					.append("activitygroup.uuid groupid,activity_hotel.uuid proid,activitygroup.groupCode,")
					.append("activity_hotel.activityName acitivityName from ")
					.append("activity_hotel_group activitygroup,(select t.activityName,t.id,t.uuid from activity_hotel t ")
					.append("WHERE t.wholesaler_id = ")
					.append(companyId)
					.append(" ) activity_hotel where activitygroup.delFlag = 0 AND activitygroup.activity_hotel_uuid = activity_hotel.uuid ")
					.append(") pro where cr.activityId = pro.groupid ) payment where 1 = 1");
		} else if (orderS == Context.ORDER_TYPE_ISLAND) {
			sqlBuffer
					.append(" cost_record_island costrecord WHERE costrecord.payReview = 2 and costrecord.reviewType = 0 AND costrecord.delFlag = 0 and ")
					.append("costrecord.budgetType = 1 and costrecord.orderType=")
					.append(orderS)
					.append(") cr,(select activitygroup.createBy,")
					.append("activitygroup.uuid groupid,activity_island.uuid proid,activitygroup.groupCode,")
					.append("activity_island.activityName acitivityName from ")
					.append("activity_island_group activitygroup,(select t.activityName,t.id,t.uuid from activity_island t ")
					.append("WHERE t.wholesaler_id = ")
					.append(companyId)
					.append(" ) activity_island where activitygroup.delFlag = 0 AND activitygroup.activity_island_uuid = activity_island.uuid ")
					.append(") pro where cr.activityId = pro.groupid ) payment where 1 = 1");
		}

		// 团号
		String groupCode = map.get("groupCode");
		if (StringUtils.isNotBlank(groupCode)) {
			sqlBuffer.append(" and payment.groupCode like '%" + groupCode
					+ "%'");
		}
		// 批发商编号
		String supplier = map.get("supplier");
		String agentId = map.get("agentId");
		if (StringUtils.isNotBlank(agentId) && StringUtils.isNotBlank(supplier)) {
			sqlBuffer
					.append(" and (payment.supplyType=0 and  payment.supplyId = ")
					.append(supplier)
					.append(" or payment.supplyType=1 and payment.supplyId=")
					.append(agentId).append(") ");
		} else if (StringUtils.isBlank(agentId)
				&& StringUtils.isNotBlank(supplier)) {
			sqlBuffer
					.append(" and payment.supplyType=0 and  payment.supplyId = "
							+ supplier);
		} else if (StringUtils.isBlank(supplier)
				&& StringUtils.isNotBlank(agentId)) {
			sqlBuffer
					.append(" and payment.supplyType=1 and  payment.supplyId = "
							+ agentId);
		}
		// 付款状态
		String payState = map.get("payState");
		if (StringUtils.isNotBlank(payState)) {
			sqlBuffer.append(" and payment.payFlag = " + payState);
		}

		// 计调条件
		if (StringUtils.isNotBlank(map.get("jds"))) {
			sqlBuffer.append(" and payment.createBy=").append(map.get("jds"));
		}
		// 币种
		String currency = map.get("currency");
		if (StringUtils.isNotBlank(currency)) {
			sqlBuffer.append(" and payment.currencyId = " + currency);

		}
		String printFlag = map.get("printFlag");
		if (StringUtils.isNotBlank(printFlag)) {
			if ("1".equals(printFlag)) {
				sqlBuffer.append(" and payment.printFlag=" + printFlag);
			} else {
				sqlBuffer.append(
						" and (payment.printFlag is null or payment.printFlag="
								+ printFlag).append(")");
			}
		}
		String startMoney = map.get("startMoney");
		String endMoney = map.get("endMoney");

		if (StringUtils.isNotBlank(startMoney)) {
			sqlBuffer.append(" and payment.amount >= " + startMoney);
		}
		if (StringUtils.isNotBlank(endMoney)) {
			sqlBuffer.append(" and payment.amount <= " + endMoney);
		}
		String order = page.getOrderBy();
		if (StringUtils.isBlank(order)) {
			page.setOrderBy("updateDate DESC ");
		}
		return costRecordDao.findBySql(page, sqlBuffer.toString(), Map.class);
	}

	/**
	 * yingshouUUID 应收金额的uuid yishouUUID 已收金额的uuid
	 * 
	 * */
	public List<Map<String, Object>> getChajia(String incomeUUID,
			String costUUID, Long companyId) {
		String Sql = "SELECT  *,amount-myamount  AS amountok FROM "
				+ "(SELECT cc.currency_name,cc.currency_id, aa.amount FROM currency cc  "
				+ " LEFT JOIN ( SELECT currencyId,amount FROM money_amount WHERE serialNum='"
				+ incomeUUID
				+ "' and delFlag='0') aa ON aa.currencyId=cc.currency_id "
				+ "WHERE cc.del_flag=0 AND cc.create_company_id="
				+ companyId
				+ ")  bb JOIN  "
				+ "(SELECT cc.currency_id AS currencyid, aa.amount myamount FROM currency cc  "
				+ " LEFT JOIN ( SELECT currencyId,amount FROM money_amount WHERE serialNum='"
				+ costUUID
				+ "' and delFlag='0') aa ON aa.currencyId=cc.currency_id "
				+ "WHERE cc.del_flag=0 AND cc.create_company_id="
				+ companyId
				+ ") dd ON  bb.currency_id=dd.currencyid  WHERE (amount IS NOT NULL OR myamount IS NOT NULL)";

		List<Map<String, Object>> ls = costRecordDao.findBySql(Sql, Map.class);

		for (Map<String, Object> map : ls) {
			if (map.get("amount") == null) {
				map.put("amountok", "-" + map.get("myamount").toString());
			} else if (map.get("myamount") == null) {
				map.put("amountok", map.get("amount").toString());
			}
		}
		return ls;
	}

	/**
	 * 成本审核撤销操作
	 * 
	 * @author haiming.zhao
	 * @param costRecordId
	 * @param orderType
	 */
	/**
	 * 成本审核撤销操作
	 * 
	 * @author haiming.zhao
	 * @param orderType
	 */

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void cancelOp(Long costId, String orderType, Integer nowLevel) {
		String sql = "select crl.createBy,crl.rid from cost_record_log crl where crl.costId="
				+ costId
				+ " AND crl.orderType="
				+ orderType
				+ " and logType=0 and crl.nowLevel="
				+ nowLevel
				+ " AND crl.result=1 ORDER BY crl.id desc LIMIT 1";
		List<Map<String, Long>> crl = costRecordLogDao
				.findBySql(sql, Map.class);
		int size = crl.size();
		Long updateBy;
		if (StringUtils.isNotBlank(orderType)
				&& orderType.equals(Context.ORDER_TYPE_HOTEL)) {
			CostRecordHotel cr = costRecordHotelDao.findOne(Long
					.valueOf(costId));
			if (size > 0) {
				updateBy = crl.get(size - 1).get("createBy");
			} else {
				updateBy = cr.getCreateBy().getId();
			}
			cr.setUpdateBy(updateBy);
			cr.setNowLevel(nowLevel - 1);
			CostRecordLog crll = new CostRecordLog();
			crll.setResult(-1);// -1表示撤销
			crll.setActivityUuid(cr.getActivityUuid());
			crll.setOrderType(11); // 酒店 orderType=11
			crll.setNowLevel(nowLevel);
			crll.setCreateBy(UserUtils.getUser().getId());
			crll.setCreateDate(new Date());
			crll.setCostName(cr.getName() + "_成本审核");
			crll.setLogType(0); // logType=0 成本审核
			crll.setCostId(costId);
			costRecordLogDao.save(crll);
		} else if (StringUtils.isNotBlank(orderType)
				&& orderType.equals(Context.ORDER_TYPE_ISLAND)) {

			CostRecordIsland cr = costRecordIslandDao.findOne(Long
					.valueOf(costId));
			if (size > 0) {
				updateBy = crl.get(size - 1).get("createBy");
			} else {
				updateBy = cr.getCreateBy().getId();
			}
			cr.setUpdateBy(updateBy);
			cr.setNowLevel(nowLevel - 1);
			CostRecordLog crll = new CostRecordLog();
			crll.setResult(-1);// -1表示撤销
			crll.setActivityUuid(cr.getActivityUuid());
			crll.setOrderType(12);// 海岛 orderType=12
			crll.setNowLevel(nowLevel);
			crll.setLogType(0); // logType=0 成本审核
			crll.setCreateBy(UserUtils.getUser().getId());
			crll.setCreateDate(new Date());
			crll.setLogType(0);
			crll.setCostName(cr.getName() + "_成本审核");
			crll.setCostId(costId);
			costRecordLogDao.save(crll);
		} else {

			CostRecord cr = costRecordDao.findOne(Long.valueOf(costId));
			if (size > 0) {
				updateBy = crl.get(size - 1).get("createBy");
			} else {
				updateBy = cr.getCreateBy().getId();
			}
			cr.setUpdateBy(updateBy);
			cr.setNowLevel(Integer.valueOf(nowLevel) - 1);
			CostRecordLog crll = new CostRecordLog();
			crll.setResult(-1);// -1表示撤销
			crll.setRid(cr.getActivityId());
			crll.setOrderType(cr.getOrderType());
			crll.setNowLevel(nowLevel);
			crll.setCreateBy(UserUtils.getUser().getId());
			crll.setCreateDate(new Date());
			crll.setLogType(0); // logType=0 成本审核
			crll.setCostName(cr.getName() + "_成本审核");
			crll.setCostId(costId);
			costRecordLogDao.save(crll);
		}
	}

	public List<UserJob> getReviewByFlowType(Integer flowType, Integer orderType) {

		// 获得审核业务需要的的 userJob 类
		List<UserJob> userJobList = new ArrayList<UserJob>();
		long companyId = UserUtils.getUser().getCompany().getId();
		long userId = UserUtils.getUser().getId();
		List<Long> deptList = getDeptList(userId);

		if (deptList.size() == 0) {
			return userJobList;
		}

		List<UserJob> myUserJobList = new ArrayList<UserJob>();
		userJobList = userJobDao.getUserJobList(userId);
		List<Long> listCompany = new ArrayList<Long>();
		listCompany = reviewCompanyDao.findReviewCompanyIds(companyId,
				flowType, deptList);
		for (UserJob userjob : userJobList) {
			if (userjob.getOrderType() == orderType) {
				Long deptId;
				if (userjob.getDeptLevel() == 1) {
					deptId = userjob.getDeptId();
				} else if (userjob.getDeptLevel() == 2) {
					deptId = userjob.getParentDept();
				} else {
					continue;
				}
				List<Long> reviewCompanyId = new ArrayList<Long>();
				if (listCompany.size() == 0) {
					/* 如果部门没有审核配置，则使用默认审核配置 */
					reviewCompanyId = reviewCompanyDao.findReviewCompanyList(
							(long) 0, flowType, (long) 0);
				} else {
					reviewCompanyId = reviewCompanyDao.findReviewCompanyList(
							companyId, flowType, deptId);
				}
				if (reviewCompanyId.size() > 0) {
					List<Integer> reviewList;
					reviewList = reviewRoleLevelDao.findReviewJobLevel(
							userjob.getJobId(), (long) reviewCompanyId.get(0));
					if (reviewList.size() > 0) {
						myUserJobList.add(userjob);
					}
				}
			}
		}
		getCostUserJobsReviewCountByType(myUserJobList, flowType);
		return myUserJobList;
	}

	/* 获得当前用户有多少条待审核的 成本审核记录，供菜单加载 */
	public int getReviewCountByType(int flowType) {
		int count = 0;
		// Integer reviewStatus=1;//待审核
		Long companyId = UserUtils.getUser().getCompany().getId();

		List<UserJob> userJobs = reviewCommonService
				.getWorkFlowJobByFlowType(flowType);

		if (null != userJobs && 0 < userJobs.size()) {
			for (UserJob uj : userJobs) {
				// 获取review_company 实例
				if (flowType == 18) {
					List<Long> reviewCompanyIds = Lists.newArrayList();
					if (null != uj.getDeptLevel()
							&& 1 == uj.getDeptLevel().intValue()) {
						reviewCompanyIds = reviewCompanyDao
								.findReviewCompanyList(companyId, flowType,
										uj.getDeptId());
						// 获得当前职位负责的审核层级列表
						List<Integer> jobs = reviewService.getJobLevel(
								uj.getDeptId(), uj.getJobId(), flowType);
						count = count
								+ getAllPayReviewCount(uj.getDeptId(),
										reviewCompanyIds.get(0), flowType,
										jobs.get(0), 1, uj.getOrderType());
					} else if (null != uj.getDeptLevel()
							&& 2 == uj.getDeptLevel().intValue()) {
						reviewCompanyIds = reviewCompanyDao
								.findReviewCompanyList(companyId, flowType,
										uj.getParentDept());
						// 获得当前职位负责的审核层级列表
						List<Integer> jobs = reviewService.getJobLevel(
								uj.getParentDept(), uj.getJobId(), flowType);
						count = count
								+ getAllPayReviewCount(uj.getDeptId(),
										reviewCompanyIds.get(0), flowType,
										jobs.get(0), 2, uj.getOrderType());
					}
				} else {
					List<Long> reviewCompanyIds = Lists.newArrayList();
					if (null != uj.getDeptLevel()
							&& 1 == uj.getDeptLevel().intValue()) {
						reviewCompanyIds = reviewCompanyDao
								.findReviewCompanyList(companyId, flowType,
										uj.getDeptId());
						// 获得当前职位负责的审核层级列表
						List<Integer> jobs = reviewService.getJobLevel(
								uj.getDeptId(), uj.getJobId(), flowType);
						count = count
								+ getAllCommonReviewCount(uj.getDeptId(),
										reviewCompanyIds.get(0), flowType,
										jobs.get(0), 1, uj.getOrderType());
					} else if (null != uj.getDeptLevel()
							&& 2 == uj.getDeptLevel().intValue()) {
						reviewCompanyIds = reviewCompanyDao
								.findReviewCompanyList(companyId, flowType,
										uj.getParentDept());
						// 获得当前职位负责的审核层级列表
						List<Integer> jobs = reviewService.getJobLevel(
								uj.getParentDept(), uj.getJobId(), flowType);
						count = count
								+ getAllCommonReviewCount(uj.getDeptId(),
										reviewCompanyIds.get(0), flowType,
										jobs.get(0), 2, uj.getOrderType());
					}
				}

			}
		} else {
			return 0;
		}
		return count;
	}

	/* 获得当前用户有多少条待审核的 成本审核记录，供菜单加载 */
	private List<UserJob> getCostUserJobsReviewCountByType(
			List<UserJob> userJobs, Integer flowType) {
		// Integer reviewStatus=1;//待审核
		Long companyId = UserUtils.getUser().getCompany().getId();
		if (null != userJobs && 0 < userJobs.size()) {
			for (UserJob uj : userJobs) {
				int count = 0;
				// 获取review_company 实例
				List<Long> reviewCompanyIds = Lists.newArrayList();
				if (null != uj.getDeptLevel()
						&& 1 == uj.getDeptLevel().intValue()) {
					reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(
							companyId, flowType, uj.getDeptId());
					// 获得当前职位负责的审核层级列表

					List<Integer> jobs = reviewService.getJobLevel(
							uj.getDeptId(), uj.getJobId(), flowType);
					if (jobs.size() > 0) {
						if (flowType == 18) {
							count = getAllPayReviewCount(uj.getDeptId(),
									reviewCompanyIds.get(0), flowType,
									jobs.get(0), 1, uj.getOrderType());
						} else {
							count = getAllCommonReviewCount(uj.getDeptId(),
									reviewCompanyIds.get(0), flowType,
									jobs.get(0), 1, uj.getOrderType());
						}
					}
					uj.setCount(count);
				} else if (null != uj.getDeptLevel()
						&& 2 == uj.getDeptLevel().intValue()) {
					reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(
							companyId, flowType, uj.getParentDept());
					// 获得当前职位负责的审核层级列表
					List<Integer> jobs = reviewService.getJobLevel(
							uj.getParentDept(), uj.getJobId(), flowType);
					if (jobs.size() > 0) {
						if (flowType == 18) {
							count = getAllPayReviewCount(uj.getDeptId(),
									reviewCompanyIds.get(0), flowType,
									jobs.get(0), 2, uj.getOrderType());
						} else {
							count = getAllCommonReviewCount(uj.getDeptId(),
									reviewCompanyIds.get(0), flowType,
									jobs.get(0), 2, uj.getOrderType());
						}
					}
					uj.setCount(count);
				}
			}
		}
		return userJobs;
	}

	/* 获得当前用户有多少条待审核的 成本审核记录，供菜单加载 */
	public List<UserJob> getCostUserJobsReviewCountByType(List<UserJob> userJobs) {
		// Integer reviewStatus=1;//待审核
		Long companyId = UserUtils.getUser().getCompany().getId();

		Integer flowType = 15;
		if (null != userJobs && 0 < userJobs.size()) {
			for (UserJob uj : userJobs) {
				int count = 0;
				// 获取review_company 实例
				List<Long> reviewCompanyIds = Lists.newArrayList();
				if (null != uj.getDeptLevel()
						&& 1 == uj.getDeptLevel().intValue()) {
					reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(
							companyId, flowType, uj.getDeptId());
					// 获得当前职位负责的审核层级列表

					List<Integer> jobs = reviewService.getJobLevel(
							uj.getDeptId(), uj.getJobId(), flowType);
					if (jobs.size() > 0) {
						count = getAllCommonReviewCount(uj.getDeptId(),
								reviewCompanyIds.get(0), flowType, jobs.get(0),
								1, uj.getOrderType());
					}
					uj.setCount(count);
				} else if (null != uj.getDeptLevel()
						&& 2 == uj.getDeptLevel().intValue()) {
					reviewCompanyIds = reviewCompanyDao.findReviewCompanyList(
							companyId, flowType, uj.getParentDept());
					// 获得当前职位负责的审核层级列表
					List<Integer> jobs = reviewService.getJobLevel(
							uj.getParentDept(), uj.getJobId(), flowType);
					if (jobs.size() > 0) {
						count = getAllCommonReviewCount(uj.getDeptId(),
								reviewCompanyIds.get(0), flowType, jobs.get(0),
								2, uj.getOrderType());
					}
					uj.setCount(count);
				}
			}
		}
		return userJobs;
	}

	/**
	 * 获取审核信息列表(查询审核信息基础方法，具体业务数据请关联业务表获取,暂适用于单团类基础审核查询) create by ruyi.chen
	 * create date 2015-03-18
	 * 
	 * @return
	 */
	private int getAllCommonReviewCount(Long deptId, Long reviewCompanyId,
			Integer reviewType, Integer nowLevel, int deptLevel,
			Integer orderType) {
		int count = 0;
		StringBuffer sbf = new StringBuffer();
		List<Object> ls = new ArrayList<Object>();
		// ls.add(reviewType);
		if (orderType == 7)
			sbf.append("SELECT r.id  from cost_record r  join activity_airticket air on r.activityId=air.id and air.delFlag='0' and r.delFlag='0' and r.orderType=7 and  r.review=1 and  r.reviewCompanyId=?  and r.nowLevel="
					+ nowLevel);
		else if (orderType == 6)
			sbf.append("SELECT r.id  from cost_record r join visa_products visa on  r.activityId=visa.id and visa.delFlag='0' and r.delFlag='0' and r.orderType=6 and  r.review=1 and  r.reviewCompanyId=?  and r.nowLevel="
					+ nowLevel);
		else if (orderType < 6 || orderType == 10)
			sbf.append("SELECT r.id  from cost_record r  join activitygroup activity on  r.activityId=activity.id and activity.delFlag='0' and r.delFlag='0' and r.orderType="
					+ orderType
					+ " and  r.review=1 and  r.reviewCompanyId=?  and r.nowLevel="
					+ nowLevel
					+ "  JOIN  travelactivity t ON  activity.srcActivityId = t.id AND t.delFlag=0");
		else if (orderType == 11)
			sbf.append("SELECT r.id  from cost_record_hotel r  join activity_hotel_group activity on   r.activity_uuid=activity.uuid and activity.delFlag='0' and r.delFlag='0'  and  r.review=1 and  r.reviewCompanyId=?  and r.nowLevel="
					+ nowLevel
					+ "  JOIN  activity_hotel t ON  activity.activity_hotel_uuid = t.uuid AND t.delFlag=0");
		else if (orderType == 12)
			sbf.append("SELECT r.id  from cost_record_island r  join activity_island_group activity on   r.activity_uuid=activity.uuid and activity.delFlag='0' and r.delFlag='0'  and  r.review=1 and  r.reviewCompanyId=?  and r.nowLevel="
					+ nowLevel
					+ "  JOIN  activity_island t ON  activity.activity_island_uuid = t.uuid AND t.delFlag=0");

		ls.add(reviewCompanyId);
		// ls.add(orderType);
		/*
		 * if(2 == deptLevel){ sbf.append(
		 * " from cost_record r  where  r.reviewCompanyId=? and r.orderType =? and r.nowLevel="
		 * +nowLevel); ls.add(reviewCompanyId); ls.add(orderType); } /*else if(1
		 * == deptLevel){ sbf.append(
		 * " from cost_record r,department dep  where r.createby=sys.id  and r.deptId=dep.id   and r.flowType =? "
		 * ) .append(
		 * " and dep.parent_id=? and r.review_company_id=? and r.productType =? "
		 * ); ls.add(deptId); ls.add(reviewCompanyId); ls.add(orderType); }else
		 * { return count; }
		 */
		// sbf.append(" AND r.active = 1 and r.status =1  and r.nowLevel="+nowLevel);
		// 添加审核查询控制(全部、未审核、已驳回、已通过)

		List<Map<Object, Object>> list = costRecordDao.findBySql(
				sbf.toString(), ls.toArray());
		count = list.size();
		return count;

	}

	/**
	 * 获取审核信息列表(查询审核信息基础方法，具体业务数据请关联业务表获取,暂适用于单团类基础审核查询) create by ruyi.chen
	 * create date 2015-03-18
	 * 
	 * @return
	 */
	private int getAllPayReviewCount(Long deptId, Long reviewCompanyId,
			Integer reviewType, Integer nowLevel, int deptLevel,
			Integer orderType) {
		int count = 0;
		StringBuffer sbf = new StringBuffer();
		List<Object> ls = new ArrayList<Object>();
		// ls.add(reviewType);
		if (orderType == 7)
			sbf.append("SELECT r.id  from cost_record r  join activity_airticket air on r.activityId=air.id and air.delFlag='0' and r.delFlag='0' and r.orderType=7 and  r.payReview=1 and  r.payReviewCompanyId=?  and r.payNowLevel="
					+ nowLevel);
		else if (orderType == 6)
			sbf.append("SELECT r.id  from cost_record r join visa_products visa on  r.activityId=visa.id and visa.delFlag='0' and r.delFlag='0' and r.orderType=6 and  r.payReview=1 and  r.payReviewCompanyId=?  and r.payNowLevel="
					+ nowLevel);
		else if (orderType < 6 || orderType == 10)
			sbf.append("SELECT r.id  from cost_record r  join activitygroup activity on  r.activityId=activity.id and activity.delFlag='0' and r.delFlag='0' and r.orderType="
					+ orderType
					+ " and  r.payReview=1 and  r.payReviewCompanyId=?  and r.payNowLevel="
					+ nowLevel
					+ "  JOIN  travelactivity t ON  activity.srcActivityId = t.id AND t.delFlag=0");
		else if (orderType == 11)
			sbf.append("SELECT r.id  from cost_record_hotel r  join activity_hotel_group activity on  r.activity_uuid=activity.uuid and activity.delFlag='0' and r.delFlag='0' and  r.payReview=1 and  r.payReviewCompanyId=?  and r.payNowLevel="
					+ nowLevel
					+ "  JOIN  activity_hotel t ON  activity.activity_hotel_uuid = t.uuid AND t.delFlag=0");
		else if (orderType == 12)
			sbf.append("SELECT r.id  from cost_record_island r  join activity_island_group activity on  r.activity_uuid=activity.uuid and activity.delFlag='0' and r.delFlag='0'  and  r.payReview=1 and  r.payReviewCompanyId=?  and r.payNowLevel="
					+ nowLevel
					+ "  JOIN  activity_island t ON  activity.activity_island_uuid = t.uuid AND t.delFlag=0");

		ls.add(reviewCompanyId);
		// ls.add(orderType);
		/*
		 * if(2 == deptLevel){ sbf.append(
		 * " from cost_record r  where  r.reviewCompanyId=? and r.orderType =? and r.nowLevel="
		 * +nowLevel); ls.add(reviewCompanyId); ls.add(orderType); } /*else if(1
		 * == deptLevel){ sbf.append(
		 * " from cost_record r,department dep  where r.createby=sys.id  and r.deptId=dep.id   and r.flowType =? "
		 * ) .append(
		 * " and dep.parent_id=? and r.review_company_id=? and r.productType =? "
		 * ); ls.add(deptId); ls.add(reviewCompanyId); ls.add(orderType); }else
		 * { return count; }
		 */
		// sbf.append(" AND r.active = 1 and r.status =1  and r.nowLevel="+nowLevel);
		// 添加审核查询控制(全部、未审核、已驳回、已通过)

		List<Map<Object, Object>> list = costRecordDao.findBySql(
				sbf.toString(), ls.toArray());
		count = list.size();
		return count;

	}

	/* 预算录入页面--成本与收入数据 */
	public List<Map<String, Object>> getCurrenySum(String serialNum,
			Long companyId) {
		String Sql = "SELECT cc.currency_mark,cc.currency_name,cc.currency_id, aa.amount FROM currency cc "
				+ "   JOIN ( SELECT currencyId,amount FROM money_amount WHERE serialNum=? and delFlag='0') aa ON aa.currencyId=cc.currency_id "
				+ " WHERE  cc.create_company_id=?  and cc.del_flag=0";
		List<Map<String, Object>> ls = costRecordDao.findBySql(Sql, Map.class,
				serialNum, companyId);
		return ls;
	}

	/* 预算录入页面--成本与收入数据 */

	public List<Map<String, Object>> getRefundSum(Long groupId,
			Integer budgetType, Integer orderType) {
		String sql = "select sum(c.price) totalRefund from cost_record c where c.activityId = ?"
				+ " and c.reviewType = 1 and c.budgetType = ? and c.orderType = ? and delFlag=0 ";
		if (budgetType == 1) {
			sql += " and c.reviewStatus not in ('已取消', '已驳回')";
		}
		List<Map<String, Object>> list = costRecordDao.findBySql(sql,
				Map.class, groupId, budgetType, orderType);
		return list;
	}

	/*
	 * public List<Map<String, Object>> getCurrenyCount(String serialNum) {
	 * String Sql=
	 * "SELECT currency_id FROM currency WHERE  create_company_id=2 AND display_flag=1 AND del_flag=0 "
	 * ; List<Map<String, Object>> ls=costRecordDao.findBySql(Sql, Map.class);
	 * return ls; }
	 */

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateCostRecord(Long activityId, Integer orderType,
			Integer review) {
		costRecordDao.updateCostRecord(activityId, orderType, review);
	}

	/**
	 * 
	 * @Title: createPaymentFile 针对海岛游
	 * @Description: TODO(生成支付凭证word文档)
	 * @param @param id
	 * @param @param orderType
	 * @param @return
	 * @param @throws IOException
	 * @param @throws TemplateException 设定文件
	 * @return File 返回类型
	 * @throws
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public File createPaymentFile(Long id, Integer orderType, Integer isNew)
			throws IOException, TemplateException {
		CostRecord costRecord = this.findOne(id);
		if (costRecord == null){
			return null;
		}
		// word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		PayInfoDetail payInfoDetail = refundService.getPayInfoByPayId(costRecord.getSerialNum(),costRecord.getOrderType()+"");

		if (orderType == Context.ORDER_TYPE_JP) {
			ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(costRecord.getActivityId());
			root.put("groupCode", activityAirTicket.getGroupCode() == null ? "": activityAirTicket.getGroupCode());
		} else if (orderType == Context.ORDER_TYPE_QZ) {
			VisaProducts visaPorduct = visaProductsDao.findOne(costRecord.getActivityId());
			root.put("groupCode", visaPorduct.getGroupCode() == null ? "": visaPorduct.getGroupCode());
		} else {
			ActivityGroup activityGroup = activityGroupDao.findOne(costRecord.getActivityId());
			root.put("groupCode", activityGroup.getGroupCode() == null ? "": activityGroup.getGroupCode());
		}
		root.put("nows", DateUtils.formatCustomDate(costRecord.getCreateDate(),"yyyy年MM月dd日"));
		// 20151012增加付款状态，判断财务撤销确认付款后不显示确认付款时间
		Integer payStatus = costRecord.getPayStatus();
		if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) ||
				Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
			root.put("conDate", "年    月     日");
		} else {
			if (payStatus == 0) {
				root.put("conDate", "年    月     日");
			} else {
				root.put("conDate",costRecord.getUpdateDate() == null ? "年    月     日" : DateUtils.formatCustomDate(costRecord.getUpdateDate(),"yyyy年MM月dd日"));
			}
		}
		root.put("money", costRecord.getName());				//款项
		root.put("person", costRecord.getCreateBy().getName());
		// 新审核数据单据人员信息抓取20151205
		String deptmanager = "", manager = "", finance = "", cashier = "", reviewer = "";
		if (isNew == 2) {
			String reviewId = costRecord.getPayReviewUuid();
			// 获取单据审批人员Map
			MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUuid,
							ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, reviewId);
			List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);// 主管
			List<User> managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);// 总经理
			List<User> finances = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);// 财务主管
			List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);// 出纳
			List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);// 审核
			deptmanager = getNames(executives);// 主管
			manager = getNames(managers);// 总经理
			finance = getNames(finances);// 财务主管
			cashier = getNames(cashiers);// 出纳
			reviewer = getNames(reviewers);// 审核
		} else {
			// 支出凭单获取人员信息20150505，根据付款审核流程
			reviewer = reviewService.getPayReviewPerson(costRecord.getId(), 9) == null ? "": reviewService.getPayReviewPerson(costRecord.getId(), 9);
			manager = reviewService.getPayReviewPerson(costRecord.getId(), 10) == null ? "": reviewService.getPayReviewPerson(costRecord.getId(), 10);
			deptmanager = reviewService.getPayReviewPerson(costRecord.getId(),4) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 4);
			// 青岛凯撒需要在单据上显示出纳，20150710
			if (Context.SUPPLIER_UUID_QDKS.equals(companyUuid)) {
				cashier = reviewService.getPayReviewPerson(costRecord.getId(),6);
			}
		}
		root.put("deptmanager", deptmanager);
		root.put("reviewer", reviewer);
		root.put("manager", manager);
		root.put("cashier", cashier);
		root.put("finance", finance);
		// 当前批发商的美元、加元汇率（目前环球行）
		List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		Integer currencyId = costRecord.getCurrencyId();
		// 币种名称
		String currencyName = "";
		// 汇率
		BigDecimal currencyExchangerate = new BigDecimal("1");
		// 美元汇率
		BigDecimal currencyExchangerateUSA = null;
		// 加元汇率
		BigDecimal currencyExchangerateCAN = null;
		// 人民币计算
		BigDecimal amountCHN = null;
		// 美元计算
		BigDecimal amountUSA = null;
		// 加元计算
		BigDecimal amountCAN = null;
		if (!Collections3.isEmpty(currencylist)) {
			for (Currency currency : currencylist) {
				if (currencyId == Integer.parseInt(currency.getId() + "")) {
					currencyName = currency.getCurrencyName();
					// 20151023汇率取值规则更改为成本录入时的汇率值
					currencyExchangerate = costRecord.getRate();
				}
			}
		}
		// 根据币种汇率计算人民币金额，取实际付款金额（单价*数量）20150420

		BigDecimal price = costRecord.getPrice();
		BigDecimal quantity = BigDecimal.valueOf(costRecord.getQuantity());
		
		BigDecimal amount = costRecord.getPriceAfter();
		BigDecimal currentAmount = price.multiply(quantity);
		
		if (StringUtils.isNotBlank(currencyName)) {
			if (currencyName.startsWith("美元")) {
				currencyExchangerateUSA = currencyExchangerate;
				amountUSA = currentAmount;
			} else if (currencyName.startsWith("加")) {
				currencyExchangerateCAN = currencyExchangerate;
				amountCAN = currentAmount;
			} else if (currencyName.startsWith("人民")) {
				amountCHN = price.multiply(quantity).multiply(
						currencyExchangerate);
			}
		}
		
		root.put("amountCHN",amountCHN == null ? "" : MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));
		root.put("currencyExchangerateUSA",currencyExchangerateUSA == null ? "" : MoneyNumberFormat.fmtMicrometer(currencyExchangerateUSA.toString(), "#,##0.0000"));
		root.put("amountUSA",amountUSA == null ? "" : MoneyNumberFormat.getThousandsMoney(
						Double.parseDouble(amountUSA.toString()),MoneyNumberFormat.THOUSANDST_POINT_TWO));
		root.put("currencyExchangerateCAN",currencyExchangerateCAN == null ? "" : MoneyNumberFormat.fmtMicrometer(currencyExchangerateCAN.toString(), "#,##0.0000"));
		root.put("amountCAN",amountCAN == null ? "" : MoneyNumberFormat.getThousandsMoney(
						Double.parseDouble(amountCAN.toString()),MoneyNumberFormat.THOUSANDST_POINT_TWO));
		// 人民币金额汉字
		String amountChinese = amount.toString();
		if (amountChinese.contains("-")) {
			amountChinese = "红字"+ StringNumFormat.changeAmount(amountChinese.replaceAll("-", ""));
		} else {
			amountChinese = StringNumFormat.changeAmount(amountChinese);
		}

		amount.setScale(2, BigDecimal.ROUND_DOWN);
		root.put("amount",amount == null ? "" : MoneyNumberFormat.getThousandsMoney(
						Double.parseDouble(amount.toString()),MoneyNumberFormat.THOUSANDST_POINT_TWO));
		root.put("amountChinese", amountChinese == null ? "" : amountChinese);
		// 20150720修改支出凭单摘要
		// 20151123项目备注：xxxxxx;付款备注：xxxxxx。
		String remarks = costRecord.getComment() == null ? "" : "项目备注："+ costRecord.getComment();
		// 因bug ： 12350,12355,12358 现将该代码注释去掉，update by shijun.liu 2016.02.17
		String payRemarks = StringUtils.isBlank(payInfoDetail.getRemarks()) ? "" : "付款备注：" + payInfoDetail.getRemarks();
		root.put("remarks", remarks + "  " + payRemarks);
		root.put("supplyName", costRecord.getSupplyName() == null ? "": costRecord.getSupplyName());
		root.put("tobankName", costRecord.getBankName() == null ? "": costRecord.getBankName());
		//银行账户名
		root.put("accountName", "");
		
		root.put("tobankAccount", costRecord.getBankAccount() == null ? ""
				: costRecord.getBankAccount());
		
		return FreeMarkerUtil.generateFile("payment.ftl", "payment.doc", root);
	}

	/**
	 * 
	 * @Title: createPaymentFile
	 * @Description: TODO(生成支付凭证word文档)
	 * @param @param id
	 * @param @param orderType
	 * @param @return
	 * @param @throws IOException
	 * @param @throws TemplateException 设定文件
	 * @return File 返回类型
	 * @throws
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public File createPaymentFile(Long id, Integer orderType, Integer isNew, String payId) throws IOException, TemplateException {
		if (id == null)
			return null;
		
		CostRecord costRecord = this.findOne(id);
		if (costRecord == null)
			return null;
		
		// word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();	
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		PayInfoDetail payInfoDetail = refundService.getPayInfoByPayId(payId,costRecord.getOrderType()+"");
		
		if (orderType == Context.ORDER_TYPE_JP) {
			ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(costRecord.getActivityId());
			root.put("groupCode", activityAirTicket.getGroupCode() == null ? "": activityAirTicket.getGroupCode());
		} else if (orderType == Context.ORDER_TYPE_QZ) {
			VisaProducts visaPorduct = visaProductsDao.findOne(costRecord.getActivityId());
			root.put("groupCode", visaPorduct.getGroupCode() == null ? "": visaPorduct.getGroupCode());
		} else {
			ActivityGroup activityGroup = activityGroupDao.findOne(costRecord.getActivityId());
			root.put("groupCode", activityGroup.getGroupCode() == null ? "": activityGroup.getGroupCode());
		}
		
		root.put("nows", DateUtils.formatCustomDate(costRecord.getCreateDate(),"yyyy年MM月dd日"));
		// 20151012增加付款状态，判断财务撤销确认付款后不显示确认付款时间
		Integer payStatus = costRecord.getPayStatus();
		String companyName = UserUtils.getUser().getCompany().getCompanyName();
		if (companyName.contains("环球行") || companyName.contains("拉美途")) {
			root.put("conDate", "年    月     日");
		} else {
			if (payStatus == 0) {
				root.put("conDate", "年    月     日");
			} else {
				root.put("conDate",costRecord.getUpdateDate() == null ? "年    月     日": 
					DateUtils.formatCustomDate(costRecord.getUpdateDate(),"yyyy年MM月dd日"));
			}
		}
		
		root.put("money", costRecord.getName());				//款项
		root.put("person", costRecord.getCreateBy().getName());

		// 新审核数据单据人员信息抓取20151205
		String deptmanager = "";
		String manager = "";
		String finance = "";
		String cashier = "";
		String reviewer = "";
		if (isNew == 2) {
			String reviewId = costRecord.getPayReviewUuid();
			// 获取单据审批人员Map
			MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUuid,ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, reviewId);
			List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);// 主管
			List<User> managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);// 总经理
			List<User> finances = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);// 财务主管
			List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);// 出纳
			List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);// 审核
			deptmanager = getNames(executives);// 主管
			manager = getNames(managers);// 总经理
			finance = getNames(finances);// 财务主管
			cashier = getNames(cashiers);// 出纳
			reviewer = getNames(reviewers);// 审核
		} else {
			// 支出凭单获取人员信息20150505，根据付款审核流程
			reviewer = reviewService.getPayReviewPerson(costRecord.getId(), 9) == null ? "": reviewService.getPayReviewPerson(costRecord.getId(), 9);
			manager = reviewService.getPayReviewPerson(costRecord.getId(), 10) == null ? "": reviewService.getPayReviewPerson(costRecord.getId(), 10);
			deptmanager = reviewService.getPayReviewPerson(costRecord.getId(),4) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 4);
			// 青岛凯撒需要在单据上显示出纳，20150710
			if (Context.SUPPLIER_UUID_QDKS.equals(companyUuid)) {
				cashier = reviewService.getPayReviewPerson(costRecord.getId(),6);
			}
		}
		root.put("deptmanager", deptmanager);
		root.put("reviewer", reviewer);
		root.put("manager", manager);
		root.put("cashier", cashier);
		root.put("finance", finance);
		
		// 当前批发商的美元、加元汇率（目前环球行）
		List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		Integer currencyId = costRecord.getCurrencyId();

		// 币种名称
		String currencyName = "";
		// 汇率
		BigDecimal currencyExchangerate = new BigDecimal("1");
		// 美元汇率
		BigDecimal currencyExchangerateUSA = null;
		// 加元汇率
		BigDecimal currencyExchangerateCAN = null;
		// 人民币计算
		BigDecimal amountCHN = null;
		// 美元计算
		BigDecimal amountUSA = null;
		// 加元计算
		BigDecimal amountCAN = null;
		if (!Collections3.isEmpty(currencylist)) {
			for (Currency currency : currencylist) {
				if (currencyId == Integer.parseInt(currency.getId() + "")) {
					currencyName = currency.getCurrencyName();
					// 20151023汇率取值规则更改为成本录入时的汇率值
					currencyExchangerate = costRecord.getRate();
				}
			}
		}
		
		//转化为支付的人民币金额进行显示
		BigDecimal currentAmount = new BigDecimal(0);
		String moneyDispStyle = payInfoDetail.getMoneyDispStyle();
		List<Object[]> moneys = MoneyNumberFormat.getMoneyFromString(moneyDispStyle, "\\+");
		if (CollectionUtils.isNotEmpty(moneys)) {
			currentAmount = new BigDecimal(Double.valueOf(moneys.get(0)[1].toString()));			
		}
		
		if (StringUtils.isNotBlank(currencyName)) {
			if (currencyName.startsWith("美元")) {
				currencyExchangerateUSA = currencyExchangerate;
				amountUSA = currentAmount;
			} else if (currencyName.startsWith("加")) {
				currencyExchangerateCAN = currencyExchangerate;
				amountCAN = currentAmount;
			} else if (currencyName.startsWith("人民")) {
				amountCHN = currentAmount;
			}
		}
		root.put("amountCHN",amountCHN == null ? "" : MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));
		
		root.put("currencyExchangerateUSA",currencyExchangerateUSA == null ? "" : MoneyNumberFormat.fmtMicrometer(currencyExchangerateUSA.toString(), "#,##0.0000"));
		
		root.put("amountUSA",amountUSA == null ? "" : MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountUSA.toString()),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));
		
		root.put("currencyExchangerateCAN",currencyExchangerateCAN == null ? "" : MoneyNumberFormat.fmtMicrometer(currencyExchangerateCAN.toString(), "#,##0.0000"));
		
		root.put("amountCAN",amountCAN == null ? "" : MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCAN.toString()),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));
		
		// 人民币金额汉字
		String amountChinese = currentAmount.multiply(currencyExchangerate).toString();
		if (amountChinese.contains("-")) {
			amountChinese = "红字"+ StringNumFormat.changeAmount(amountChinese.replaceAll("-", ""));
		} else {
			amountChinese = StringNumFormat.changeAmount(amountChinese);
		}
		
		BigDecimal amount = currentAmount.multiply(currencyExchangerate);
		amount.setScale(2, BigDecimal.ROUND_DOWN);
		root.put("amount",amount == null ? "" : MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amount.toString()),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));
		root.put("amountChinese", amountChinese == null ? "" : amountChinese);

		// 20151123项目备注：xxxxxx;付款备注：xxxxxx。
		String remarks = costRecord.getComment() == null ? "" : "项目备注："+ costRecord.getComment();

		String payRemarks = StringUtils.isBlank(payInfoDetail.getRemarks()) ? "" : "付款备注：" + payInfoDetail.getRemarks();
		root.put("remarks", remarks + "  " + payRemarks);
		root.put("supplyName", costRecord.getSupplyName() == null ? "": costRecord.getSupplyName());
		root.put("tobankName", costRecord.getBankName() == null ? "": costRecord.getBankName());
		//获取银行账户名
		if(costRecord.getSupplyId() == -1) {    //非签约渠道
			root.put("accountName", "");
		}else {
			String accountName = bankInfoDao.getAccountName(Long.valueOf(costRecord.getSupplyId()), costRecord.getSupplyType()+1, 
					costRecord.getBankName(), costRecord.getBankAccount(),"");
			root.put("accountName", accountName);
		}
		
		root.put("tobankAccount", costRecord.getBankAccount() == null ? "": costRecord.getBankAccount());
		//越柬行踪，支票和现金付款时，实际领款人，取收款单位名称 0419  update by shijun.liu 2016.04.25
		if(Context.SUPPLIER_UUID_YJXZ.equals(companyUuid)){
			Integer payType = payInfoDetail.getPayType();
			if(1 == payType || 3 == payType){
				root.put("payee", payInfoDetail.getPayerName());
			}
		}
		return FreeMarkerUtil.generateFile("payment.ftl", "payment.doc", root);
	}

	@SuppressWarnings("unchecked")
	public OrderPayInput payforisland(String id) {

		// 构造入参对象
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();
		// 订单详情的页面
		// orderPayInput.setOrderDetailUrl();
		// 后置方法名(更新实际成本付款出纳确认状态)
		orderPayInput.setServiceAfterMethodName("payConfirmStatusForIsland");
		// //后置类名
		orderPayInput
				.setServiceClassName("com.trekiz.admin.modules.cost.service.CostManageService");
		// 付款信息
		OrderPayDetail orderPayDetail = new OrderPayDetail();
		CostRecordIsland costRecord = costRecordIslandDao.findOne(Long
				.parseLong(id));

		orderPayDetail.setPayCurrencyId(costRecord.getCurrencyId() + "");
		BigDecimal price = costRecord.getPrice();
		BigDecimal quantity = BigDecimal.valueOf(costRecord.getQuantity());
		BigDecimal amount = price.multiply(quantity);
		orderPayDetail.setOrderType(costRecord.getOrderType());
		orderPayDetail.setRefundMoneyType(Context.REFUNDMONEYTYPE);
		orderPayDetail.setTotalCurrencyId(costRecord.getCurrencyId() + "");
		orderPayDetail.setTotalCurrencyPrice(amount.toString());
		// 根据recordid获取成本的已付金额
		List<Object[]> paydmoney = new ArrayList<Object[]>();
		List<Refund> list = refundDao.findRefund(Long.parseLong(id),
				costRecord.getOrderType());
		StringBuffer buffer = new StringBuffer();
		if (CollectionUtils.isNotEmpty(list)) {
			int num = list.size();
			for (int i = 0; i < num; i++) {
				if (i == num - 1) {
					buffer.append("'").append(list.get(i).getMoneySerialNum())
							.append("'");
				} else {
					buffer.append("'").append(list.get(i).getMoneySerialNum())
							.append("',");
				}
			}
		}
		if (StringUtils.isNotBlank(buffer.toString())) {
			String serialsql = "SELECT c.currency_id id, c.currency_mark, sum(m.amount) from island_money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in ("
					+ buffer.toString()
					+ ") GROUP BY m.currencyId ORDER BY m.currencyId";
			paydmoney = refundDao.getSession().createSQLQuery(serialsql).list();
		}
		if (CollectionUtils.isNotEmpty(paydmoney)) {
			// 成本付款是单币种，如果不是第一次支付，支付页面的显示的总金额=应付总金额-已付金额
			BigDecimal payeDecimal = (BigDecimal) paydmoney.get(0)[2];
			BigDecimal value = amount.subtract(payeDecimal);
			orderPayDetail.setPayCurrencyPrice(value.toString());
			// int r = value.compareTo(BigDecimal.ZERO);
			// if(r==-1){
			// orderPayDetail.setPayCurrencyPrice(BigDecimal.ZERO.toString());
			// }else{
			// orderPayDetail.setPayCurrencyPrice(amount.subtract(payeDecimal).toString());
			// }
		} else {
			orderPayDetail.setPayCurrencyPrice(amount.toString());
		}
		orderPayDetail.setProjectId(Long.parseLong(id));
		detailList.add(orderPayDetail);
		orderPayInput.setSupplyType(costRecord.getSupplyType());
		orderPayInput.setAgentId(costRecord.getSupplyId());
		orderPayInput.setPayType("2");
		orderPayInput.setRefundMoneyTypeDesc(costRecord.getName());
		orderPayInput.setOrderPayDetailList(detailList);
		orderPayInput.setMoneyFlag(1);
		return orderPayInput;

	}

	@SuppressWarnings("unchecked")
	public OrderPayInput payForHotel(String id) {

		// 构造入参对象
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();
		// 订单详情的页面
		// orderPayInput.setOrderDetailUrl();
		// 后置方法名(更新实际成本付款出纳确认状态)
		orderPayInput.setServiceAfterMethodName("payConfirmStatusForHotel");
		// //后置类名
		orderPayInput
				.setServiceClassName("com.trekiz.admin.modules.cost.service.CostManageService");
		// 付款信息
		OrderPayDetail orderPayDetail = new OrderPayDetail();
		CostRecordHotel costRecord = costRecordHotelDao.findOne(Long
				.parseLong(id));

		orderPayDetail.setPayCurrencyId(costRecord.getCurrencyId() + "");
		BigDecimal price = costRecord.getPrice();
		BigDecimal quantity = BigDecimal.valueOf(costRecord.getQuantity());
		BigDecimal amount = price.multiply(quantity);
		orderPayDetail.setOrderType(costRecord.getOrderType());
		orderPayDetail.setRefundMoneyType(Context.REFUNDMONEYTYPE);
		orderPayDetail.setTotalCurrencyId(costRecord.getCurrencyId() + "");
		orderPayDetail.setTotalCurrencyPrice(amount.toString());
		// 根据recordid获取成本的已付金额
		List<Object[]> paydmoney = new ArrayList<Object[]>();
		List<Refund> list = refundDao.findRefund(Long.parseLong(id),
				costRecord.getOrderType());
		StringBuffer buffer = new StringBuffer();
		if (CollectionUtils.isNotEmpty(list)) {
			int num = list.size();
			for (int i = 0; i < num; i++) {
				if (i == num - 1) {
					buffer.append("'").append(list.get(i).getMoneySerialNum())
							.append("'");
				} else {
					buffer.append("'").append(list.get(i).getMoneySerialNum())
							.append("',");
				}
			}
		}
		if (StringUtils.isNotBlank(buffer.toString())) {
			String serialsql = "SELECT c.currency_id id, c.currency_mark, sum(m.amount) from hotel_money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in ("
					+ buffer.toString()
					+ ") GROUP BY m.currencyId ORDER BY m.currencyId";
			paydmoney = refundDao.getSession().createSQLQuery(serialsql).list();
		}
		if (CollectionUtils.isNotEmpty(paydmoney)) {
			// 成本付款是单币种，如果不是第一次支付，支付页面的显示的总金额=应付总金额-已付金额
			BigDecimal payeDecimal = (BigDecimal) paydmoney.get(0)[2];
			BigDecimal value = amount.subtract(payeDecimal);
			orderPayDetail.setPayCurrencyPrice(value.toString());
			// int r = value.compareTo(BigDecimal.ZERO);
			// if(r==-1){
			// orderPayDetail.setPayCurrencyPrice(BigDecimal.ZERO.toString());
			// }else{
			// orderPayDetail.setPayCurrencyPrice(amount.subtract(payeDecimal).toString());
			// }
		} else {
			orderPayDetail.setPayCurrencyPrice(amount.toString());
		}
		orderPayDetail.setProjectId(Long.parseLong(id));
		detailList.add(orderPayDetail);
		orderPayInput.setSupplyType(costRecord.getSupplyType());
		orderPayInput.setAgentId(costRecord.getSupplyId());
		orderPayInput.setPayType("2");
		orderPayInput.setRefundMoneyTypeDesc(costRecord.getName());
		orderPayInput.setOrderPayDetailList(detailList);
		orderPayInput.setMoneyFlag(1);
		return orderPayInput;

	}

	/**
	 * 
	 * @Title: pay
	 * @Description: TODO(实际成本付款，批发商或渠道商)
	 * @param @param id
	 * @return OrderPayInput 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public OrderPayInput pay(String id) {
		// 构造入参对象
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();
		// 订单详情的页面
		// orderPayInput.setOrderDetailUrl();
		// 后置方法名(更新实际成本付款出纳确认状态)
		orderPayInput.setServiceAfterMethodName("payConfirmStatus");
		// //后置类名
		orderPayInput
				.setServiceClassName("com.trekiz.admin.modules.cost.service.CostManageService");
		// 付款信息
		OrderPayDetail orderPayDetail = new OrderPayDetail();
		CostRecord costRecord = this.findOne(Long.parseLong(id));

		orderPayDetail.setPayCurrencyId(costRecord.getCurrencyId() + "");
		BigDecimal price = costRecord.getPrice();
		BigDecimal quantity = BigDecimal.valueOf(costRecord.getQuantity());
		BigDecimal amount = price.multiply(quantity);
		orderPayDetail.setOrderType(costRecord.getOrderType());
		orderPayDetail.setRefundMoneyType(Context.REFUNDMONEYTYPE);
		orderPayDetail.setTotalCurrencyId(costRecord.getCurrencyId() + "");
		orderPayDetail.setTotalCurrencyPrice(amount.toString());
		// 根据recordid获取成本的已付金额
		List<Object[]> paydmoney = new ArrayList<Object[]>();
		List<Refund> list = refundDao.findRefund(Long.parseLong(id),
				costRecord.getOrderType());
		StringBuffer buffer = new StringBuffer();
		if (CollectionUtils.isNotEmpty(list)) {
			int num = list.size();
			for (int i = 0; i < num; i++) {
				if (i == num - 1) {
					buffer.append("'").append(list.get(i).getMoneySerialNum())
							.append("'");
				} else {
					buffer.append("'").append(list.get(i).getMoneySerialNum())
							.append("',");
				}
			}
		}
		if (StringUtils.isNotBlank(buffer.toString())) {
			String serialsql = "SELECT c.currency_id id, c.currency_mark, sum(m.amount) from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in ("
					+ buffer.toString()
					+ ") GROUP BY m.currencyId ORDER BY m.currencyId";
			paydmoney = refundDao.getSession().createSQLQuery(serialsql).list();
		}
		if (CollectionUtils.isNotEmpty(paydmoney)) {
			// 成本付款是单币种，如果不是第一次支付，支付页面的显示的总金额=应付总金额-已付金额
			BigDecimal payeDecimal = (BigDecimal) paydmoney.get(0)[2];
			BigDecimal value = amount.subtract(payeDecimal);
			orderPayDetail.setPayCurrencyPrice(value.toString());
			// int r = value.compareTo(BigDecimal.ZERO);
			// if(r==-1){
			// orderPayDetail.setPayCurrencyPrice(BigDecimal.ZERO.toString());
			// }else{
			// orderPayDetail.setPayCurrencyPrice(amount.subtract(payeDecimal).toString());
			// }
		} else {
			orderPayDetail.setPayCurrencyPrice(amount.toString());
		}
		orderPayDetail.setProjectId(Long.parseLong(id));
		detailList.add(orderPayDetail);
		orderPayInput.setSupplyType(costRecord.getSupplyType());
		orderPayInput.setAgentId(costRecord.getSupplyId());
		orderPayInput.setPayType("2");
		orderPayInput.setRefundMoneyTypeDesc(costRecord.getName());
		orderPayInput.setOrderPayDetailList(detailList);
		orderPayInput.setMoneyFlag(1);
		return orderPayInput;
	}

	/**
	 * 
	 * @Title: pay
	 * @Description: TODO(实际成本付款，批发商或渠道商)
	 * @param @param id
	 * @return OrderPayInput 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public OrderPayInput pay(String id, String payType, String groupId,
			String orderType) {
		// 构造入参对象
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();
		// 订单详情的页面
		// orderPayInput.setOrderDetailUrl();
		// 后置方法名(更新实际成本付款出纳确认状态)
		orderPayInput.setServiceAfterMethodName("payConfirmStatus");
		// //后置类名
		orderPayInput
				.setServiceClassName("com.trekiz.admin.modules.cost.service.CostManageService");
		// 付款信息
		OrderPayDetail orderPayDetail = new OrderPayDetail();
		CostRecord costRecord = this.findOne(Long.parseLong(id));

		orderPayDetail.setPayCurrencyId(costRecord.getCurrencyId() + "");
		BigDecimal price = costRecord.getPrice();
		BigDecimal quantity = BigDecimal.valueOf(costRecord.getQuantity());
		BigDecimal amount = price.multiply(quantity);
		orderPayDetail.setOrderType(costRecord.getOrderType());
		orderPayDetail.setRefundMoneyType(Context.REFUNDMONEYTYPE);
		orderPayDetail.setTotalCurrencyId(costRecord.getCurrencyId() + "");
		orderPayDetail.setTotalCurrencyPrice(amount.toString());
		orderPayDetail.setGroupId(Integer.parseInt(groupId));
		orderPayDetail.setOrderType(Integer.parseInt(orderType));
		orderPayDetail.setCostRecordId(Integer.parseInt(id));
		// 根据recordid获取成本的已付金额
		List<Object[]> paydmoney = new ArrayList<Object[]>();
		List<PayGroup> list = payGroupDao.findById(Integer.parseInt(id));
		StringBuffer buffer = new StringBuffer();
		if (CollectionUtils.isNotEmpty(list)) {
			int num = list.size();
			for (int i = 0; i < num; i++) {
				if (i == num - 1) {
					buffer.append("'").append(list.get(i).getPayPrice())
							.append("'");
				} else {
					buffer.append("'").append(list.get(i).getPayPrice())
							.append("',");
				}
			}
		}

		if (StringUtils.isNotBlank(buffer.toString())) {
			String serialsql = "SELECT c.currency_id id, c.currency_mark, sum(m.amount) from money_amount m,currency c, pay_group p where m.currencyId=c.currency_id and "
					+ "(p.isAsAccount is null or p.isAsAccount != 102) and p.payPrice = m.serialNum "
					+ " and m.serialNum in ("
					+ buffer.toString()
					+ ") GROUP BY m.currencyId ORDER BY m.currencyId";
			paydmoney = payGroupDao.getSession().createSQLQuery(serialsql)
					.list();
		}

		if (CollectionUtils.isNotEmpty(paydmoney)) {
			// 成本付款是单币种，如果不是第一次支付，支付页面的显示的总金额=应付总金额-已付金额
			BigDecimal payeDecimal = (BigDecimal) paydmoney.get(0)[2];
			BigDecimal value = amount.subtract(payeDecimal);
			orderPayDetail.setPayCurrencyPrice(value.toString());
			// int r = value.compareTo(BigDecimal.ZERO);
			// if(r==-1){
			// orderPayDetail.setPayCurrencyPrice(BigDecimal.ZERO.toString());
			// }else{
			// orderPayDetail.setPayCurrencyPrice(amount.subtract(payeDecimal).toString());
			// }
		} else {
			orderPayDetail.setPayCurrencyPrice(amount.toString());
		}
		orderPayDetail.setProjectId(Long.parseLong(id));
		detailList.add(orderPayDetail);
		orderPayInput.setSupplyType(costRecord.getSupplyType());
		orderPayInput.setAgentId(costRecord.getSupplyId());
		orderPayInput.setPayType("3");
		orderPayInput.setRefundMoneyTypeDesc(costRecord.getName());
		orderPayInput.setOrderPayDetailList(detailList);
		orderPayInput.setMoneyFlag(1);
		return orderPayInput;
	}

	/**
	 * 
	 * @Title: payConfirmStatus
	 * @Description: TODO(更新出纳确认状态和UUID)
	 * @param @param orderPayForm 设定文件
	 * @throws
	 */
	public void payConfirmStatus(OrderPayForm orderPayForm) {
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput
				.getOrderPayDetailList();
		OrderPayDetail orderPayDetail = orderPayDetailList.get(0);
		Long id = orderPayDetail.getProjectId();
		if (id != null) {
			CostRecord costRecord = findOne(id);
			// costRecord.setPayStatus(1);
			costRecord.setSerialNum(orderPayDetail.getRefundId());
			// add by shijun.liu 更新最后修改时间，对应需求C162
			costRecord.setUpdateBy(UserUtils.getUser().getId());
//			costRecord.setUpdateDate(new Date());
			saveCostRecord(costRecord);
		}
	}

	public void payConfirmStatusForIsland(OrderPayForm orderPayForm) {
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput
				.getOrderPayDetailList();
		OrderPayDetail orderPayDetail = orderPayDetailList.get(0);
		Long id = orderPayDetail.getProjectId();
		if (id != null) {
			CostRecordIsland costRecord = costRecordIslandDao.findOne(id);
			// costRecord.setPayStatus(1);
			costRecord.setSerialNum(orderPayDetail.getRefundId());
			// add by shijun.liu 更新最后修改时间，对应需求C162
			costRecord.setUpdateBy(UserUtils.getUser().getId());
			costRecord.setUpdateDate(new Date());
			costRecordIslandDao.save(costRecord);
		}
	}

	public void payConfirmStatusForHotel(OrderPayForm orderPayForm) {
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput
				.getOrderPayDetailList();
		OrderPayDetail orderPayDetail = orderPayDetailList.get(0);
		Long id = orderPayDetail.getProjectId();
		if (id != null) {
			CostRecordHotel costRecord = costRecordHotelDao.findOne(id);
			// costRecord.setPayStatus(1);
			costRecord.setSerialNum(orderPayDetail.getRefundId());
			// add by shijun.liu 更新最后修改时间，对应需求C162
			costRecord.setUpdateBy(UserUtils.getUser().getId());
			costRecord.setUpdateDate(new Date());
			costRecordHotelDao.save(costRecord);
		}
	}
	
	public List<CostRecord> findCostRecordList(Long reviewId) {
		return costRecordDao.findCostRecordList(reviewId);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void lock(Integer budgetType, Long activityId, Integer orderType,
			String lockStatus, String groupUUID) {
		// 结算单锁定和解锁处理
		if (1 == budgetType) {
			settleLockProduct(orderType, activityId,
					Integer.parseInt(lockStatus), groupUUID, budgetType);
		}
		// 预报单锁定和解锁处理
		if (0 == budgetType) {
			forcastLockOrUnlockProduct(orderType, activityId, lockStatus,
					groupUUID, budgetType);
		}
	}

	/**
	 * 查询是否有在审核中的退款、返佣数据
	 * 
	 * @param orderType
	 *            订单类型
	 * @param productId
	 *            产品或者团期ID
	 * @param groupUUID
	 *            团期UUID
	 * @author shijun.liu
	 * @return
	 */
	public boolean isAuditing(Integer orderType, Long productId,
			String groupUUID) {
		boolean b = false;
		StringBuffer str = new StringBuffer();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		List<Object> list = null;
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append("select count(*) as count from review r where EXISTS (")
					.append(" select reviewId from cost_record_island where delFlag = '0' and budgetType = 1")
					.append(" and reviewType in (1,2)  and orderType = ?")
					.append(" and activity_uuid = ?")
					.append(" and r.id = reviewId) and `status` = 1");
			list = reviewDao.findBySql(str.toString(), orderType, groupUUID);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append("select count(*) as count from review r where EXISTS (")
					.append(" select reviewId from cost_record_hotel where delFlag = '0' and budgetType = 1")
					.append(" and reviewType in (1,2)  and orderType = ?")
					.append(" and activity_uuid = ?")
					.append(" and r.id = reviewId) and `status` = 1");
			list = reviewDao.findBySql(str.toString(), orderType, groupUUID);
		} else {
			str.append("select count(*) as count from review r where EXISTS (")
					.append(" select reviewId from cost_record where delFlag = '0' and budgetType = 1")
					.append(" and reviewType in (1,2)  and orderType = ?")
					.append(" and activityId = ?")
					.append(" and r.id = reviewId) and `status` = 1");
			list = reviewDao.findBySql(str.toString(), orderType, productId);
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
				//1、老审批
				StringBuffer costReview = new StringBuffer();
				StringBuffer paymentReview = new StringBuffer();
				//成本审批
				costReview.append("select count(*) as count from cost_record c where reviewType = 0 and delFlag = '0'")
				          .append(" and activityId=").append(productId).append(" and c.review = 1");
				//付款审批
				paymentReview.append("select count(*) as count from cost_record c where reviewType = 0 and delFlag = '0'")
				             .append(" and activityId=").append(productId).append(" and c.payReview = 1");
				List<Object> costList = costRecordDao.findBySql(costReview.toString());
				int costSize = Integer.parseInt(String.valueOf(costList.get(0)));
				List<Object> paymentList = costRecordDao.findBySql(paymentReview.toString());
				int paymentSize = Integer.parseInt(String.valueOf(paymentList.get(0)));
				StringBuffer reviewSQL = new StringBuffer();
				reviewSQL.append(" select count(*) as count from review r where ")
				         .append(" r.status = 1 and r.active = 1 and exists ( select id from ");
				if(Context.ORDER_TYPE_JP == orderType){
					reviewSQL.append(" airticket_order o where o.id = r.orderId and del_flag = '0' ")
					         .append(" and o.airticket_id = ").append(productId)
					         .append(" AND o.order_state NOT IN (7, 99, 111) )");
				}else if(Context.ORDER_TYPE_QZ == orderType){
					reviewSQL.append(" visa_order o where o.id = r.orderId and del_flag = '0' ")
					         .append(" and o.visa_product_id = ").append(productId)
					         .append(" AND o.payStatus NOT IN (7, 99, 111)")
					         .append(" AND o.visa_order_status <> 100 )");
				}else{
					reviewSQL.append(" productorder o where o.id = r.orderId and delFlag = '0' ")
					         .append(" and o.productGroupId = ").append(productId)
					         .append(" and o.payStatus NOT IN (7, 99, 111)) ");
				}
				List<Object> reviewList = reviewDao.findBySql(reviewSQL.toString());
				int reviewSize = Integer.parseInt(String.valueOf(reviewList.get(0)));
				//2、新审批
				List<ReviewNew> newReviewList = null;
				if(Context.ORDER_TYPE_JP == orderType){
					newReviewList =  reviewNewService.getReviewListByGroupIdOrProductId(currentCompanyUuid, null, String.valueOf(productId), String.valueOf(orderType), null);
				}else if(Context.ORDER_TYPE_QZ == orderType){
					newReviewList = reviewNewService.getReviewListByGroupIdOrProductId(currentCompanyUuid, null, String.valueOf(productId), String.valueOf(orderType), null);
				}else{
					newReviewList = reviewNewService.getReviewListByGroupIdOrProductId(currentCompanyUuid, String.valueOf(productId), null, String.valueOf(orderType), null);
				}
				int newReviewSize = 0;
				if(null != newReviewList){
					newReviewSize = newReviewList.size();
				}
				//老审批和新审批正在审批中的总数
				int size = costSize + paymentSize + reviewSize + newReviewSize;
				int index = Integer.parseInt(String.valueOf(list.get(0))) + size;
				int sumCount = Integer.parseInt(String.valueOf(list.get(0))) + index;
				list.add(0, sumCount);
			}
		}
		Object obj = list.get(0);
		try {
			int index = Integer.parseInt(String.valueOf(obj));
			if (index != 0) {
				b = true;
			}
		} catch (NumberFormatException e) {
			b = false;
		}
		return b;
	}

	/**
	 * 预报单锁定解锁功能
	 * 
	 * @param orderType
	 *            订单类型
	 * @param activityId
	 *            产品ID或者团期ID
	 * @param lockStatus
	 *            锁定状态
	 * @param groupUUID
	 *            团期UUID
	 * @param budgetType
	 *            成本类型
	 * @author shijun.liu
	 */
	private void forcastLockOrUnlockProduct(Integer orderType, Long activityId,
			String lockStatus, String groupUUID, Integer budgetType) {
		User user = UserUtils.getUser();
		String currentCompanyUuid = user.getCompany().getUuid();
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			ActivityIslandGroup activityIslandGroup = activityIslandGroupService
					.getByUuid(groupUUID);
			activityIslandGroup.setForcastStatus(lockStatus);
			activityIslandGroup.setUpdateBy(user.getId());
			activityIslandGroup.setUpdateDate(new Date());
			activityIslandGroupService.save(activityIslandGroup);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			ActivityHotelGroup activityHotelGroup = activityHotelGroupService
					.getByUuid(groupUUID);
			activityHotelGroup.setForcastStatus(lockStatus);
			activityHotelGroup.setUpdateBy(user.getId());
			activityHotelGroup.setUpdateDate(new Date());
			activityHotelGroupService.save(activityHotelGroup);
		} else if (orderType == Context.ORDER_TYPE_JP) {
			ActivityAirTicket activityAirTicket = activityAirTicketDao
					.findOne(activityId);
			activityAirTicket.setForcastStatus(lockStatus);
			activityAirTicket.setUpdateBy(user);
			activityAirTicket.setUpdateDate(new Date());
			activityAirTicketDao.save(activityAirTicket);
		} else if (orderType == Context.ORDER_TYPE_QZ) {
			VisaProducts visaProducts = visaProductsDao.findOne(activityId);
			visaProducts.setForcastStatus(lockStatus);
			visaProducts.setUpdateBy(user);
			visaProducts.setUpdateDate(new Date());
			visaProductsDao.save(visaProducts);
		} else {
			ActivityGroup activityGroup = activityGroupDao.findOne(activityId);
			activityGroup.setForcastStatus(lockStatus);
			activityGroup.setUpdateBy(user);
			activityGroup.setUpdateDate(new Date());
			activityGroupDao.save(activityGroup);
		}

		if ("10".equals(lockStatus)) {
			lockOrUnlockRecord(orderType, activityId, budgetType, 1, groupUUID);
		} else {
			//预报单解锁，订单数据，预算成本数据，退款数据写入预报单（拉美途）C457 2015.12.25 add by shijun.liu
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
				if(Context.ORDER_TYPE_JP == orderType){
					String orderSQL = "update airticket_order set forecast_locked_in = null where del_flag = '0' and forecast_locked_in = '1' and airticket_id = " + activityId;
					activityGroupDao.updateBySql(orderSQL);
				}else if(Context.ORDER_TYPE_QZ == orderType){
					String orderSQL = "update visa_order set forecast_locked_in = null where del_flag = '0' and forecast_locked_in = '1' and visa_product_id = " + activityId;
					activityGroupDao.updateBySql(orderSQL);
				}else if(orderType < Context.ORDER_TYPE_QZ || orderType == Context.ORDER_TYPE_CRUISE){
					String orderSQL = "update productorder set forecast_locked_in = null where delFlag = '0' and forecast_locked_in = '1' and productGroupId = " + activityId;
					activityGroupDao.updateBySql(orderSQL);
				}
				StringBuffer costSQL = new StringBuffer();
				StringBuffer refundSQL = new StringBuffer();
				costSQL.append("update cost_record set forecast_locked_in = null where forecast_locked_in = '1' ")
				       .append(" and budgetType = 0 and reviewType = 0 and activityId =").append(activityId);
				refundSQL.append("update cost_record set forecast_locked_in = null where forecast_locked_in = '1' ")
				         .append(" and reviewType = 1 and activityId =").append(activityId);
				costRecordDao.updateBySql(costSQL.toString());
				costRecordDao.updateBySql(refundSQL.toString());
			}
			lockOrUnlockRecord(orderType, activityId, budgetType, 0, groupUUID);
		}
	}

	/**
	 * 结算单锁定或者解锁功能
	 * 
	 * @param orderType
	 *            订单类型
	 * @param activityId
	 *            产品或者团期ID
	 * @param lockStatus
	 *            解锁或者锁定标识
	 * @param groupUUID
	 *            团期UUID
	 * @param budgetType
	 *            成本类型
	 * @author shijun.liu
	 */
	private void settleLockProduct(Integer orderType, Long activityId,
			Integer lockStatus, String groupUUID, Integer budgetType) {
		// 1、将产品对应的结算单锁字段改为lockStatus
		User user = UserUtils.getUser();
		String currentCompanyUuid = user.getCompany().getUuid();
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			ActivityIslandGroup activityIslandGroup = activityIslandGroupService
					.getByUuid(groupUUID);
			activityIslandGroup.setLockStatus(lockStatus);
			activityIslandGroup.setUpdateBy(user.getId());
			activityIslandGroup.setUpdateDate(new Date());
			activityIslandGroupService.save(activityIslandGroup);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			ActivityHotelGroup activityHotelGroup = activityHotelGroupService
					.getByUuid(groupUUID);
			activityHotelGroup.setLockStatus(lockStatus);
			activityHotelGroup.setUpdateBy(user.getId());
			activityHotelGroup.setUpdateDate(new Date());
			activityHotelGroupService.save(activityHotelGroup);
		} else if (orderType == Context.ORDER_TYPE_JP) {
			ActivityAirTicket activityAirTicket = activityAirTicketDao
					.findOne(activityId);
			activityAirTicket.setLockStatus(lockStatus);
			activityAirTicket.setUpdateBy(user);
			activityAirTicket.setUpdateDate(new Date());
			activityAirTicketDao.save(activityAirTicket);
		} else if (orderType == Context.ORDER_TYPE_QZ) {
			VisaProducts visaProducts = visaProductsDao.findOne(activityId);
			visaProducts.setLockStatus(lockStatus);
			visaProducts.setUpdateBy(user);
			visaProducts.setUpdateDate(new Date());
			visaProductsDao.save(visaProducts);
		} else {
			ActivityGroup activityGroup = activityGroupDao.findOne(activityId);
			activityGroup.setLockStatus(lockStatus);
			activityGroup.setUpdateBy(user);
			activityGroup.setUpdateDate(new Date());
			activityGroupDao.save(activityGroup);
		}
		if (1 == lockStatus) {
			// 2、将退款、返佣的审核流程驳回
			// 3、将退款、返佣的记录reviewStatus改成已锁定
			// 临时需求，对应Bug号：5307，结算单锁定之后，将审核中的预算成本改成已驳回
			// 4、添加驳回记录
			// 5、将该产品下的所有记录锁定
			rejectRefundAndFyFlow(orderType, activityId, groupUUID);
			lockRefundAndFyRecord(orderType, activityId, groupUUID);
			rejectRefundAndFyRecord(orderType, activityId, groupUUID);
			addReviewLog(orderType, activityId, groupUUID);
			lockOrUnlockRecord(orderType, activityId, budgetType, 1, groupUUID);
			lockOrUnlockRecord(orderType, activityId, budgetType, 1);
			//针对拉美途批发商，锁定之后，所有在审批的流程都要驳回不仅仅是退款，反佣。
			//结算单锁定该流程自动驳回 C486 add by shijun.liu  2015.12.25
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
				//新审批的处理
				String productType = String.valueOf(orderType);
				List<ReviewNew> list = null;
				if(Context.ORDER_TYPE_JP == orderType){
					//查询机票所有处理中的审批流程
					list = reviewNewService.getReviewListByGroupIdOrProductId(currentCompanyUuid, null, String.valueOf(activityId), productType, null);
				}else if(Context.ORDER_TYPE_QZ == orderType){
					//查询签证所有处理中的审批流程
					list = reviewNewService.getReviewListByGroupIdOrProductId(currentCompanyUuid, null, String.valueOf(activityId), productType, null);
				}else if(orderType < Context.ORDER_TYPE_QZ || orderType == Context.ORDER_TYPE_CRUISE){
					//查询团期类产品所有处理中的审批流程
					list = reviewNewService.getReviewListByGroupIdOrProductId(currentCompanyUuid, String.valueOf(activityId), null, productType, null);
				}
				if(null != list){
					for(ReviewNew reviewNew:list){
						reviewNewService.rejectWithoutPermission(String.valueOf(user.getId()),
								currentCompanyUuid, reviewNew.getId(), "结算单锁定该流程自动驳回", null);
					}
				}
				//老审批的处理
				StringBuffer str = new StringBuffer();
				str.append("update review set status = 0, denyReason = '").append("结算单锁定该流程自动驳回").append("'")
				   .append(" where active = 1 and status = 1").append("  and companyId = ")
				   .append(UserUtils.getUser().getCompany().getId());
				reviewDao.updateBySql(str.toString());
				StringBuffer costSQL = new StringBuffer();
				StringBuffer paymentSQL = new StringBuffer();
				costSQL.append("update cost_record set review = 0, reviewComment = '").append("结算单锁定该流程自动驳回").append("'")
				       .append(" where review = 1 and reviewType = 0 and activityId = ").append(activityId);
				paymentSQL.append("update cost_record set payReview = 0, payReviewComment = '").append("结算单锁定该流程自动驳回").append("'")
			       		  .append(" where payReview = 1 and reviewType = 0 and budgetType = 1 ")
			       		  .append(" and activityId = ").append(activityId);
				costRecordDao.updateBySql(costSQL.toString());
				costRecordDao.updateBySql(paymentSQL.toString());
			}
		} else {
			unLockRefundAndFyRecord(orderType, activityId, groupUUID);
			lockOrUnlockRecord(orderType, activityId, budgetType, 0, groupUUID);
			lockOrUnlockRecord(orderType, activityId, budgetType, 0);
			//拉美途批发商，锁定结算单时生成的订单在解锁后进入结算单。C486需求. add by shijun.liu 2015.12.25
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
				if(Context.ORDER_TYPE_JP == orderType){
					String orderSQL = "update airticket_order set settle_locked_in = null where del_flag = '0' and settle_locked_in = '1' and airticket_id = " + activityId;
					activityGroupDao.updateBySql(orderSQL);
				}else if(Context.ORDER_TYPE_QZ == orderType){
					String orderSQL = "update visa_order set settle_locked_in = null where del_flag = '0' and settle_locked_in = '1' and visa_product_id = " + activityId;
					activityGroupDao.updateBySql(orderSQL);
				}else if(orderType < Context.ORDER_TYPE_QZ || orderType == Context.ORDER_TYPE_CRUISE){
					String orderSQL = "update productorder set settle_locked_in = null where delFlag = '0' and settle_locked_in = '1' and productGroupId = " + activityId;
					activityGroupDao.updateBySql(orderSQL);
				}
			}
		}
	}

	/**
	 * 预报单，结算单锁定或者解锁时，将其记录改成锁定或者解锁
	 * 
	 * @param orderType
	 *            订单类型
	 * @param activityId
	 *            产品或者团期ID
	 * @param budgetType
	 *            成本类型，预算成本或者结算成本
	 * @param lockStatus
	 *            锁定或者解锁状态，0-->表示解锁，1-->表示锁定(cost_record表中的锁定字段)
	 * @param groupUUID
	 *            团期UUID
	 * @author shijun.liu
	 */
	private void lockOrUnlockRecord(Integer orderType, Long activityId,
			Integer budgetType, Integer lockStatus, String groupUUID) {
		Long userId = UserUtils.getUser().getId();
		String updateDate = DateUtils.formatCustomDate(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		StringBuffer str = new StringBuffer();
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append("UPDATE cost_record_island set lockStatus= ")
					.append(lockStatus).append(",updateBy=").append(userId)
					.append(", updateDate = '").append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND budgetType = ").append(budgetType)
					.append(" AND orderType = ?")
					.append(" AND activity_uuid = ? ");
			costRecordIslandDao.updateBySql(str.toString(), orderType, groupUUID);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append("UPDATE cost_record_hotel set lockStatus= ")
					.append(lockStatus).append(",updateBy=").append(userId)
					.append(", updateDate = '").append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND budgetType = ").append(budgetType)
					.append(" AND orderType = ?")
					.append(" AND activity_uuid = ? ");
			costRecordHotelDao.updateBySql(str.toString(), orderType, groupUUID);
		} else {
			str.append("UPDATE cost_record set lockStatus= ")
					.append(lockStatus).append(",updateBy=").append(userId)
					.append(", updateDate = '").append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND budgetType = ").append(budgetType)
					.append(" AND orderType = ?")
					.append(" AND activityId = ? ");
			activityGroupDao.updateBySql(str.toString(), orderType, activityId);
		}
	}

	/**
	 * 结算单锁定时，驳回退款返佣的审核流程
	 * 
	 * @param orderType
	 *            订单类型
	 * @param activityId
	 *            产品或者团期ID
	 * @param groupUUID
	 *            团期UUID
	 * @author shijun.liu
	 */
	private void rejectRefundAndFyFlow(Integer orderType, Long activityId,
			String groupUUID) {
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		String updateDate = DateUtils.formatCustomDate(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		StringBuffer str = new StringBuffer();
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append(
					"update review r set `status` = 0, denyReason='结算单锁定该流程被自动驳回',")
					.append("updateBy=")
					.append(userId)
					.append(",updateDate = '")
					.append(updateDate)
					.append("' where exists (")
					.append(" SELECT reviewId FROM cost_record_island WHERE ")
					.append(" delFlag = '0' AND reviewType IN (1, 2) AND budgetType = 1 ")
					.append(" AND reviewStatus NOT IN ('已取消', '已驳回', '操作完成', '审核通过','审批通过','待提交')")
					.append(" and r.id = reviewId  AND orderType = ? AND activity_uuid = ? )");
			activityIslandGroupDao.updateBySql(str.toString(), orderType,
					groupUUID);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append(
					"update review r set `status` = 0, denyReason='结算单锁定该流程被自动驳回',")
					.append("updateBy=")
					.append(userId)
					.append(",updateDate = '")
					.append(updateDate)
					.append("' where exists (")
					.append(" SELECT reviewId FROM cost_record_hotel WHERE ")
					.append(" delFlag = '0' AND reviewType IN (1, 2) AND budgetType = 1 ")
					.append(" AND reviewStatus NOT IN ('已取消', '已驳回', '操作完成', '审核通过','审批通过','待提交')")
					.append(" and r.id = reviewId  AND orderType = ? AND activity_uuid = ? )");
			activityHotelGroupDao.updateBySql(str.toString(), orderType,
					groupUUID);
		} else {
			List<CostRecord> list = costRecordDao.getByOrderTypeAndActivityId(
					orderType, activityId);
			if (null != list && list.size() > 0) {
				for (CostRecord costRecord : list) {
					String reviewUuid = costRecord.getReviewUuid();
					if(StringUtils.isNotBlank(reviewUuid) && costRecord.getReviewType() != 0 
							&& costRecord.getBudgetType() == 1){
						reviewNewService.rejectWithoutPermission(String.valueOf(userId), companyUuid, reviewUuid, 
								"结算单锁定该流程自动驳回", null);
						//reviewNewService.reject(String.valueOf(userId),companyUuid, null, reviewUuid, 
						//      "结算单锁定该流程自动驳回", null);
					}else if(StringUtils.isBlank(reviewUuid) && costRecord.getReviewType() != 0 
							&& costRecord.getBudgetType() == 1){
						StringBuffer sql = new StringBuffer();
						sql.append("update review r set `status` = 0, denyReason='结算单锁定该流程被自动驳回',")
						   .append("updateBy=").append(userId).append(",updateDate = '").append(updateDate)
						   .append("' where exists (").append(" SELECT reviewId FROM cost_record WHERE ")
						   .append(" delFlag = '0' AND reviewType IN (1, 2) AND budgetType = 1 ")
						   .append(" AND reviewStatus NOT IN ('已取消', '已驳回', '操作完成', '审核通过','审批通过','待提交')")
						   .append(" and r.id = reviewId AND reviewUuid is null AND orderType = ? AND activityId = ? )");
						activityGroupDao.updateBySql(sql.toString(), orderType, activityId);	
					}
				}
			}
		}
	}

	/**
	 * 预报单，结算单锁定或者解锁时，将其记录改成锁定或者解锁
	 * 
	 * @param orderType
	 *            订单类型
	 * @param activityId
	 *            产品或者团期ID
	 * @param budgetType
	 *            成本类型，预算成本或者结算成本
	 * @param lockStatus
	 *            锁定或者解锁状态，0-->表示解锁，1-->表示锁定(cost_record表中的锁定字段)
	 * @author shijun.liu
	 */
	private void lockOrUnlockRecord(Integer orderType, Long activityId,
			Integer budgetType, Integer lockStatus) {
		Long userId = UserUtils.getUser().getId();
		String updateDate = DateUtils.formatCustomDate(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		StringBuffer str = new StringBuffer();
		str.append("UPDATE cost_record set lockStatus= ").append(lockStatus)
				.append(",updateBy=").append(userId).append(", updateDate = '")
				.append(updateDate).append("' WHERE delFlag = '0'")
				.append(" AND budgetType = ").append(budgetType)
				.append(" AND orderType = ?").append(" AND activityId = ? ");
		activityGroupDao.updateBySql(str.toString(), orderType, activityId);
	}

	/**
	 * 将退款返佣的记录改成已锁定状态
	 * 
	 * @param orderType
	 *            订单类型
	 * @param activityId
	 *            产品或者团期ID
	 * @param groupUUID
	 *            团期UUID
	 * @author shijun.liu
	 */
	private void lockRefundAndFyRecord(Integer orderType, Long activityId,
			String groupUUID) {
		Long userId = UserUtils.getUser().getId();
		String updateDate = DateUtils.formatCustomDate(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		StringBuffer str = new StringBuffer();
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append(
					"UPDATE cost_record_island set reviewStatus = '已锁定',updateBy=")
					.append(userId)
					.append(", updateDate = '")
					.append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND reviewType IN (1, 2) AND reviewStatus NOT IN ('已取消', '已驳回','操作完成','审核通过','审批通过','待提交')")
					.append(" AND budgetType = 1 AND orderType = ?")
					.append(" AND activity_uuid = ? ");
			activityIslandGroupDao.updateBySql(str.toString(), orderType,
					groupUUID);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append(
					"UPDATE cost_record_hotel set reviewStatus = '已锁定',updateBy=")
					.append(userId)
					.append(", updateDate = '")
					.append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND reviewType IN (1, 2) AND reviewStatus NOT IN ('已取消', '已驳回','操作完成','审核通过','审批通过','待提交')")
					.append(" AND budgetType = 1 AND orderType = ?")
					.append(" AND activity_uuid = ? ");
			activityHotelGroupDao.updateBySql(str.toString(), orderType,
					groupUUID);
		} else {
			str.append("UPDATE cost_record set reviewStatus = '已锁定',updateBy=")
					.append(userId)
					.append(", updateDate = '")
					.append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND reviewType IN (1, 2) AND reviewStatus NOT IN ('已取消', '已驳回','操作完成','审核通过','审批通过','待提交')")
					.append(" AND budgetType = 1 AND orderType = ?")
					.append(" AND activityId = ? ");
			activityGroupDao.updateBySql(str.toString(), orderType, activityId);
		}
	}

	/**
	 * 结算单锁定之后，将预报单的状态改为已驳回
	 * 
	 * @param orderType
	 *            订单类型
	 * @param activityId
	 *            产品或者团期ID
	 * @param groupUUID
	 *            团期UUID
	 * @author shijun.liu
	 */
	private void rejectRefundAndFyRecord(Integer orderType, Long activityId,
			String groupUUID) {
		Long userId = UserUtils.getUser().getId();
		String updateDate = DateUtils.formatCustomDate(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		StringBuffer str = new StringBuffer();
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append(
					"UPDATE cost_record_island set reviewStatus = '已驳回',updateBy=")
					.append(userId)
					.append(", updateDate = '")
					.append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND reviewType IN (1, 2) AND reviewStatus NOT IN ('已取消', '已驳回','操作完成','审核通过','审批通过','待提交')")
					.append(" AND budgetType = 0 AND orderType = ?")
					.append(" AND activity_uuid = ? ");
			activityIslandGroupDao.updateBySql(str.toString(), orderType,
					groupUUID);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append(
					"UPDATE cost_record_hotel set reviewStatus = '已驳回',updateBy=")
					.append(userId)
					.append(", updateDate = '")
					.append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND reviewType IN (1, 2) AND reviewStatus NOT IN ('已取消', '已驳回','操作完成','审核通过','审批通过','待提交')")
					.append(" AND budgetType = 0 AND orderType = ?")
					.append(" AND activity_uuid = ? ");
			activityHotelGroupDao.updateBySql(str.toString(), orderType,
					groupUUID);
		} else {
			str.append("UPDATE cost_record set reviewStatus = '已驳回',updateBy=")
					.append(userId)
					.append(", updateDate = '")
					.append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND reviewType IN (1, 2) AND reviewStatus NOT IN ('已取消', '已驳回','操作完成','审核通过','审批通过','待提交')")
					.append(" AND budgetType = 0 AND orderType = ?")
					.append(" AND activityId = ? ");
			activityGroupDao.updateBySql(str.toString(), orderType, activityId);
		}
	}

	/**
	 * 结算单解锁，将退款返佣的记录改成已解锁状态
	 * 
	 * @param orderType
	 *            订单类型
	 * @param activityId
	 *            团期或者产品ID
	 * @param groupUUID
	 *            团期UUID
	 * @author shijun.liu
	 */
	private void unLockRefundAndFyRecord(Integer orderType, Long activityId,
			String groupUUID) {
		Long userId = UserUtils.getUser().getId();
		String updateDate = DateUtils.formatCustomDate(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		StringBuffer str = new StringBuffer();
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append(
					"UPDATE cost_record_island set reviewStatus = '已解锁',updateBy=")
					.append(userId)
					.append(", updateDate = '")
					.append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND reviewType IN (1, 2) AND reviewStatus = '已锁定' ")
					.append(" AND budgetType = 1 AND orderType = ?")
					.append(" AND activity_uuid = ?");
			activityIslandGroupDao.updateBySql(str.toString(), orderType,
					groupUUID);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append(
					"UPDATE cost_record_hotel set reviewStatus = '已解锁',updateBy=")
					.append(userId)
					.append(", updateDate = '")
					.append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND reviewType IN (1, 2) AND reviewStatus = '已锁定' ")
					.append(" AND budgetType = 1 AND orderType = ?")
					.append(" AND activity_uuid = ?");
			activityHotelGroupDao.updateBySql(str.toString(), orderType,
					groupUUID);
		} else {
			str.append("UPDATE cost_record set reviewStatus = '已解锁',updateBy=")
					.append(userId)
					.append(", updateDate = '")
					.append(updateDate)
					.append("' WHERE delFlag = '0'")
					.append(" AND reviewType IN (1, 2) AND reviewStatus = '已锁定' ")
					.append(" AND budgetType = 1 AND orderType = ?")
					.append(" AND activityId = ?");
			activityGroupDao.updateBySql(str.toString(), orderType, activityId);
		}
	}

	/**
	 * 结算单锁定，将驳回退款、返佣的审核流程的日志保存到review_log表
	 * 
	 * @param orderType
	 *            产品类型
	 * @param activityId
	 *            产品Id
	 * @param groupUUID
	 *            团期UUID
	 * @author shijun.liu
	 * */
	private void addReviewLog(Integer orderType, Long activityId,
			String groupUUID) {
		Long userId = UserUtils.getUser().getId();
		StringBuffer str = new StringBuffer();
		List<Object[]> reviewList = null;
		if (orderType == Context.ORDER_TYPE_ISLAND) {
			str.append("SELECT id,nowLevel FROM review r WHERE exists (")
					.append(" SELECT reviewId FROM cost_record_island WHERE delFlag = '0' AND reviewType IN (1, 2)")
					.append(" AND reviewStatus NOT IN ('已取消', '已驳回', '操作完成', '审核通过','待提交')")
					.append(" AND budgetType = 1 and r.id = reviewId  AND orderType = ? AND activity_uuid = ?)");
			reviewList = reviewDao.findBySql(str.toString(), orderType,
					groupUUID);
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			str.append("SELECT id,nowLevel FROM review r WHERE exists (")
					.append(" SELECT reviewId FROM cost_record_hotel WHERE delFlag = '0' AND reviewType IN (1, 2)")
					.append(" AND reviewStatus NOT IN ('已取消', '已驳回', '操作完成', '审核通过','待提交')")
					.append(" AND budgetType = 1 and r.id = reviewId  AND orderType = ? AND activity_uuid = ?)");
			reviewList = reviewDao.findBySql(str.toString(), orderType,
					groupUUID);
		} else {
			str.append("SELECT id,nowLevel FROM review r WHERE exists (")
					.append(" SELECT reviewId FROM cost_record WHERE delFlag = '0' AND reviewType IN (1, 2)")
					.append(" AND reviewStatus NOT IN ('已取消', '已驳回', '操作完成', '审核通过','待提交')")
					.append(" AND budgetType = 1 and r.id = reviewId AND reviewUuid is null AND orderType = ? AND activityId = ?)");
			reviewList = reviewDao.findBySql(str.toString(), orderType,
					activityId);
		}
		for (Object[] array : reviewList) {
			ReviewLog reviewLog = new ReviewLog(Long.valueOf(String
					.valueOf(array[0])), Integer.parseInt(String
					.valueOf(array[1])), userId, new Date(), "已驳回", "结算单锁定流程驳回");
			reviewLogDao.save(reviewLog);
		}
	}

	/*
	 * @Transactional(readOnly=false,rollbackFor=Exception.class) public
	 * List<AbstractSpecificCost> initCosts(List<AbstractSpecificCost> costs,
	 * String type){ stateMachine.prepareStart(new
	 * StateOwner<AbstractSpecificCost>(costs,
	 * StateMachineContext.getMutableBeanStartState(type))); return costs; }
	 * 
	 * @Transactional(readOnly=false,rollbackFor=Exception.class) public
	 * List<CostRecord> initCostRecord(List<CostRecord> costs, String type){
	 * stateMachine.prepareStart(new StateOwner<CostRecord>(costs,
	 * StateMachineContext.getMutableBeanStartState(type))); return costs; }
	 */

	public List<Map<String, String>> findPayedRecordById(String id) {
		String sqlStr = "SELECT p.payTypeName, c.currency_mark, m.amount, p.createDate, p.isAsAccount, p.payVoucher, p.reject_reason rejectReason"
				+ " from money_amount m,currency c, pay_group p "
				+ "where m.currencyId = c.currency_id and m.serialNum = p.payPrice and p.cost_record_id = "
				+ id;
		return payGroupDao.findBySql(sqlStr, Map.class);
	}

	// @Transactional(readOnly=false,rollbackFor=Exception.class)
	// public void initOperatorCost(List<OperatorSpecificCost>
	// activitySpecificCostList){
	// List<MutableStateBean> newList = new ArrayList<MutableStateBean>();
	// newList.addAll(activitySpecificCostList);
	// stateMachine.prepareStart(new StateOwner(newList, "start"));
	// }
	//
	// @Transactional(readOnly=false,rollbackFor=Exception.class)
	// public void initFinanceCost(List<FinanceSpecificCost>
	// financeSpecificCostList){
	// List<MutableStateBean> newList = new ArrayList<MutableStateBean>();
	// newList.addAll(financeSpecificCostList);
	// stateMachine.prepareStart(new StateOwner(newList, "financeAccepted"));
	// }
	/*
	 * @Transactional(readOnly=false,rollbackFor=Exception.class) public
	 * List<AbstractSpecificCost> initCosts(List<AbstractSpecificCost> costs,
	 * String type){ stateMachine.prepareStart(new
	 * StateOwner<AbstractSpecificCost>(costs,
	 * StateMachineContext.getMutableBeanStartState(type))); return costs; }
	 * 
	 * @Transactional(readOnly=false,rollbackFor=Exception.class) public
	 * List<CostRecord> initCostRecord(List<CostRecord> costs, String type){
	 * stateMachine.prepareStart(new StateOwner<CostRecord>(costs,
	 * StateMachineContext.getMutableBeanStartState(type))); return costs; }
	 */

	// @Transactional(readOnly=false,rollbackFor=Exception.class)
	// public void initOperatorCost(List<OperatorSpecificCost>
	// activitySpecificCostList){
	// List<MutableStateBean> newList = new ArrayList<MutableStateBean>();
	// newList.addAll(activitySpecificCostList);
	// stateMachine.prepareStart(new StateOwner(newList, "start"));
	// }
	//
	// @Transactional(readOnly=false,rollbackFor=Exception.class)
	// public void initFinanceCost(List<FinanceSpecificCost>
	// financeSpecificCostList){
	// List<MutableStateBean> newList = new ArrayList<MutableStateBean>();
	// newList.addAll(financeSpecificCostList);
	// stateMachine.prepareStart(new StateOwner(newList, "financeAccepted"));
	// }

	/*
	 * @Transactional(readOnly=false,rollbackFor=Exception.class) public void
	 * nextCostFlow(List<AbstractSpecificCost> costs, String currentState,
	 * FlowEvent e){ stateMachine.transition(new
	 * StateOwner<AbstractSpecificCost>(costs, currentState), e); }
	 * 
	 * @Transactional(readOnly=false,rollbackFor=Exception.class) public void
	 * nextGroupFlow(ActivityGroup activityGroup, String currentState, FlowEvent
	 * e){ stateMachine.transition(new
	 * StateOwner<ActivityGroup>(Collections.singletonList(activityGroup),
	 * currentState), e); }
	 */
	/**
	 * 查询收款单数据 add by chy 2015年5月25日21:02:13
	 */
	public Map<String, Object> getAccountData(Integer payId, Integer prdType) {
		Map<String, Object> result = new HashMap<String, Object>();
		String query = null;
		if (prdType == 7) {// 机票
			query = "select a.id, a.uuid, a.payType,a.payTypeName ,"
					+ "a.payerName,a.checkNumber,a.invoiceDate,a.payVoucher,"
					+ "a.remarks,a.posNo,a.posTagEendNo,a.posBank,a.bankName,"
					+ "a.toBankNname,a.bankAccount,a.toBankAccount,a.payPrice,"
					+ "a.payPriceType,a.groupId,a.createBy,a.createDate,a.updateBy,"
					+ "a.updateDate conDate,a.payPriceBack,a.isAsAccount,a.orderType,"
					+ " a.fastPayType,a.printTime,a.printFlag,a.paymentStatus,"
					+ " a.accountDate ,a.cost_record_id," + "c.group_code, "
					+ "c.startingDate, " + "c.arrivedCity, "
					+ "c.departureCity," + "b.createBy as costCreateBy,"+" a.to_alipay_account AS toAlipayAccount, "
					+ " c.airType " + "FROM " + "pay_group a, "
					+ "cost_record b, " + "activity_airticket c " + " WHERE "
					+ "a.groupId = c.id " + "AND a.cost_record_id = b.id "
					+ "AND a.id = " + payId;
		} else if (prdType == 6) {// 签证
			query = "select a.id, a.uuid, a.payType,a.payTypeName ,"
					+ "a.payerName,a.checkNumber,a.invoiceDate,a.payVoucher,"
					+ "a.remarks,a.posNo,a.posTagEendNo,a.posBank,a.bankName,"
					+ "a.toBankNname,a.bankAccount,a.toBankAccount,a.payPrice,"
					+ "a.payPriceType,a.groupId,a.createBy,a.createDate,a.updateBy,"
					+ "a.updateDate conDate,a.payPriceBack,a.isAsAccount,a.orderType,"
					+ " a.fastPayType,a.printTime,a.printFlag,a.paymentStatus,"
					+ " a.accountDate ,a.cost_record_id,"
					+ "b.createBy as costCreateBy,"
					+ "c.groupCode,"
					+ " c.productName, "
					+ " c.sysCountryId, "
					+ " a.to_alipay_account AS toAlipayAccount "
					+ "FROM "
					+ "pay_group a,cost_record b,visa_products c "
					+ "WHERE "
					+ "a.groupId = c.id AND a.cost_record_id = b.id AND a.id = "
					+ payId;
		} else {// 单团类
			query = "select a.id, a.uuid, a.payType,a.payTypeName ,"
					+ "a.payerName,a.checkNumber,a.invoiceDate,a.payVoucher,"
					+ "a.remarks,a.posNo,a.posTagEendNo,a.posBank,a.bankName,"
					+ "a.toBankNname,a.bankAccount,a.toBankAccount,a.payPrice,"
					+ "a.payPriceType,a.groupId,a.createBy,a.createDate,a.updateBy,"
					+ "a.updateDate conDate,a.payPriceBack,a.isAsAccount,a.orderType,"
					+ " a.fastPayType,a.printTime,a.printFlag,a.paymentStatus,"
					+ " a.accountDate ,a.cost_record_id," + "b.groupCode, "
					+ "d.createBy as costCreateBy," + "c.groupOpenDate,"
					+ "c.acitivityName, "
					+ " a.to_alipay_account AS toAlipayAccount " 
					+ "FROM " + "pay_group a, "
					+ "activitygroup b, cost_record d, " + "travelactivity c "
					+ " WHERE " + "a.groupId = b.id "
					+ "AND b.srcActivityId = c.id AND a.cost_record_id = d.id "
					+ "AND a.id = " + payId;
		}
		List<Map<String, Object>> list = costRecordDao.findBySql(query,
				Map.class);
		if (list == null || list.size() == 0) {
			return result;
		}
		result = list.get(0);

		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("payId", payId);
		datas.put("prdType", prdType);
		datas.put(
				"printTime",
				result.get("printTime") == null ? "" : DateUtils
						.formatCustomDate(DateUtils.dateFormat(result.get(
								"printTime").toString()), "yyyy/MM/dd HH:mm"));// 去掉了秒:ss
		// 收款金额
		String serialNum = result.get("payPrice") == null ? "" : result.get(
				"payPrice").toString();// UUID
		StringBuffer money = new StringBuffer("");
		StringBuffer moneyUpCase = new StringBuffer("");
		buildMoneyData(serialNum, money, moneyUpCase);

		String shouKuanRen = "";
		String confirmDate = "";
		String isAccount = result.get("isAsAccount") == null ? "" : result.get(
				"isAsAccount").toString();
		if ("1".equals(isAccount)) {
			shouKuanRen = result.get("updateBy") == null
					|| "".equals(result.get("updateBy").toString()) ? ""
					: UserUtils.getUserNameById(Long.parseLong(result.get(
							"updateBy").toString()));
			// confirmDate = result.get("updateDate") == null ? "" :
			// result.get("updateDate").toString();
		}
		String pName = result.get("createBy") == null
				|| "".equals(result.get("createBy").toString()) ? ""
				: UserUtils.getUserNameById(Long.parseLong(result.get(
						"createBy").toString()));
		String toBankAccount = (result.get("toBankNname") == null ? "" : result
				.get("toBankNname").toString())
				+ (result.get("toBankAccount") == null ? "" : result.get(
						"toBankAccount").toString());

		// String payerName = (result.get("payerName") == null ? "" :
		// result.get("payerName").toString()) + " " + (result.get("bankName")
		// == null ? "" : result.get("bankName").toString()) + " " +
		// (result.get("bankAccount") == null ? "" :
		// result.get("bankAccount").toString());
		// 来款单位信息只显示付款单位信息，其它不显示
		String payerName = (result.get("payerName") == null ? "" : result.get(
				"payerName").toString());
		//支付宝账号
		String toAlipayAccount = StringUtils.blankReturnEmpty(result.get("toAlipayAccount"));
		// TODO 根据结果组织map数据 以适应退款单
		String companyName = UserUtils.getUser().getCompany().getCompanyName();
		if (prdType == 7) {// 机票
			String startCity = result.get("departureCity") == null ? ""
					: result.get("departureCity").toString();
			String arrivedCity = result.get("arrivedCity") == null ? ""
					: result.get("arrivedCity").toString();
			String airTypeName = result.get("airType") == null
					|| "".equals(result.get("airType")) ? "" : DictUtils
					.getDictLabel(result.get("airType").toString(), "air_Type",
							"");
			String fromCity = dictService.findByValueAndType(startCity,
					"from_area").getLabel();// 出发城市
			String arriveCity = "";
			if (arrivedCity != null) {
				arriveCity = AreaUtil.findAreaNameById(Long
						.parseLong(arrivedCity));// 到达城市
			}
			String productName = fromCity + "-->" + arriveCity + " "
					+ airTypeName;// 产品名称

			datas.put(
					"payDate",
					result.get("createDate") == null ? "" : DateUtils
							.formatCustomDate(
									DateUtils.dateFormat(result.get(
											"createDate").toString()),
									"yyyy年MM月dd日"));// 填写日期
			datas.put("groupCode", result.get("group_code") == null ? ""
					: result.get("group_code"));// 团号
			datas.put(
					"startDate",
					result.get("startingDate") == null ? "" : DateUtils
							.formatCustomDate(DateUtils.dateFormat(
									result.get("startingDate").toString(),
									"yyyy-MM-dd"), "MM月dd日"));// 出发/签证日期
			datas.put("applyPerson", pName);// 经办人--团期表的创建人
			datas.put("productName", productName);// 线路/产品
			datas.put("payerName",
					FreeMarkerUtil.StringFilter(payerName.trim()));// 来款单位信息
			datas.put("airRefund", "机票款");// 款项
			datas.put("remarks", result.get("remarks") == null ? "" : result
					.get("remarks").toString());// 备注
			datas.put("payPrice", money);// 收款金额
			datas.put("payPriceUpCase", moneyUpCase);// 大写收款金额
			datas.put("toBankAccount", toBankAccount);// 收款账户
			datas.put("payPerson", pName);// 交款人
			datas.put("shouKuanRen", shouKuanRen);// 收款人 --》财务
			// 银行到账日期
			String accountDate = "";
			if (companyName.contains("环球行")) {
				accountDate = result.get("accountDate") == null ? "" : result
						.get("accountDate").toString();
				if (StringUtils.isNotBlank(accountDate)) {
					Date groupDate = DateUtils.dateFormat(accountDate);
					accountDate = DateUtils.formatCustomDate(groupDate,
							"yyyy年MM月dd日");
					if(!isAccount.equals("1")) {
						accountDate = "年       月       日";
					}
				} else {
					accountDate = "年       月       日";
				}

			} else {
				accountDate = result.get("accountDate") == null ? "" : result
						.get("accountDate").toString();
				if (StringUtils.isNotBlank(accountDate)) {
					Date groupDate = DateUtils.dateFormat(accountDate);
					accountDate = DateUtils.formatCustomDate(groupDate,
							"yyyy年MM月dd日");
				} else {
					accountDate = "年       月       日";
				}
			}
			datas.put("bankGetDate", accountDate);// 银行到账日期
			datas.put(
					"confirmDate",
					confirmDate == "" ? "" : DateUtils.formatCustomDate(
							DateUtils.dateFormat(confirmDate), "yyyy年MM月dd日"));// 确认收款日期

		} else if (prdType == 6) {// 签证

			datas.put(
					"payDate",
					result.get("createDate") == null ? "" : DateUtils
							.formatCustomDate(
									DateUtils.dateFormat(result.get(
											"createDate").toString()),
									"yyyy年MM月dd日"));// 填写日期
			datas.put("groupCode", result.get("groupCode") == null ? ""
					: result.get("groupCode"));// 团号
			datas.put(
					"startDate",
					result.get("createDate") == null ? "" : DateUtils
							.formatCustomDate(DateUtils.dateFormat(
									result.get("createDate").toString(),
									"yyyy-MM-dd"), "MM月dd日"));// 出发/签证日期
			datas.put("applyPerson", pName);// 经办人--团期表的创建人
			datas.put("productName", result.get("productName") == null ? ""
					: result.get("productName").toString());// 线路/产品
			datas.put("payerName",
					FreeMarkerUtil.StringFilter(payerName.trim()));// 来款单位信息
			datas.put(
					"airRefund",
					result.get("sysCountryId") == null ? "" : CountryUtils
							.getCountryName(Long.parseLong(result.get(
									"sysCountryId").toString()))
							+ "签证费");// 款项
			datas.put("remarks", result.get("remarks") == null ? "" : result
					.get("remarks").toString());// 备注
			datas.put("payPrice", money);// 收款金额
			datas.put("payPriceUpCase", moneyUpCase);// 大写收款金额
			datas.put("toBankAccount", toBankAccount);// 收款账户
			datas.put("payPerson", pName);// 交款人
			datas.put("shouKuanRen", shouKuanRen);// 收款人 --》财务
			// 银行到账日期
			String accountDate = "";
			if (companyName.contains("环球行")) {
				accountDate = result.get("accountDate") == null ? "" : result
						.get("accountDate").toString();
				if (StringUtils.isNotBlank(accountDate)) {
					Date groupDate = DateUtils.dateFormat(accountDate);
					accountDate = DateUtils.formatCustomDate(groupDate,
							"yyyy年MM月dd日");
					if(!isAccount.equals("1")) {
						accountDate = "年       月       日";
					}
				} else {
					accountDate = "年       月       日";
				}

			} else {
				accountDate = result.get("accountDate") == null ? "" : result
						.get("accountDate").toString();
				if (StringUtils.isNotBlank(accountDate)) {
					Date groupDate = DateUtils.dateFormat(accountDate);
					accountDate = DateUtils.formatCustomDate(groupDate,
							"yyyy年MM月dd日");
				} else {
					accountDate = "年       月       日";
				}
			}
			datas.put("bankGetDate", accountDate);// 银行到账日期
			datas.put(
					"confirmDate",
					confirmDate == "" ? "" : DateUtils.formatCustomDate(
							DateUtils.dateFormat(confirmDate), "yyyy年MM月dd日"));// 确认收款日期

		} else {// 单团类
			datas.put(
					"payDate",
					result.get("createDate") == null ? "" : DateUtils
							.formatCustomDate(
									DateUtils.dateFormat(result.get(
											"createDate").toString()),
									"yyyy年MM月dd日"));// 填写日期
			datas.put("groupCode", result.get("groupCode") == null ? ""
					: result.get("groupCode"));// 团号
			datas.put(
					"startDate",
					result.get("groupOpenDate") == null ? "" : DateUtils
							.formatCustomDate(DateUtils.dateFormat(
									result.get("groupOpenDate").toString(),
									"yyyy-MM-dd"), "MM月dd日"));// 出发/签证日期
			datas.put("applyPerson", pName);// 经办人--团期表的创建人
			datas.put("productName", result.get("acitivityName") == null ? ""
					: result.get("acitivityName").toString());// 线路/产品
			datas.put("payerName",
					FreeMarkerUtil.StringFilter(payerName.trim()));// 来款单位信息
			datas.put("airRefund", "团费");// 款项
			datas.put("remarks", result.get("remarks") == null ? "" : result
					.get("remarks").toString());// 备注
			datas.put("payPrice", money);// 收款金额
			datas.put("payPriceUpCase", moneyUpCase);// 大写收款金额
			datas.put("toBankAccount", toBankAccount);// 收款账户
			datas.put("payPerson", pName);// 交款人
			datas.put("shouKuanRen", shouKuanRen);// 收款人 --》财务
			// 银行到账日期
			String accountDate = "";
			if (companyName.contains("环球行")) {
				accountDate = result.get("accountDate") == null ? "" : result
						.get("accountDate").toString();
				if (StringUtils.isNotBlank(accountDate)) {
					Date groupDate = DateUtils.dateFormat(accountDate);
					accountDate = DateUtils.formatCustomDate(groupDate,
							"yyyy年MM月dd日");
					if(!isAccount.equals("1")) {
						accountDate = "年       月       日";
					}
				} else {
					accountDate = "年       月       日";
				}

			} else {
				accountDate = result.get("accountDate") == null ? "" : result
						.get("accountDate").toString();
				if (StringUtils.isNotBlank(accountDate)) {
					Date groupDate = DateUtils.dateFormat(accountDate);
					accountDate = DateUtils.formatCustomDate(groupDate,
							"yyyy年MM月dd日");
				} else {
					accountDate = "年       月       日";
				}
			}
			datas.put("bankGetDate", accountDate);// 银行到账日期
			datas.put(
					"confirmDate",
					confirmDate == "" ? "" : DateUtils.formatCustomDate(
							DateUtils.dateFormat(confirmDate), "yyyy年MM月dd日"));// 确认收款日期
		}
		// 确认收款日期(20151011 环球行、拉美途客户确认到账时间为空，其它客户当财务撤销确认后，确认收款日期消失)
		Integer revoke = result.get("isAsAccount") == null ? -1 : Integer
				.parseInt(result.get("isAsAccount").toString());
		String conDate = result.get("conDate") == null ? "" : result.get(
				"conDate").toString();
		//if (companyName.contains("环球行") || companyName.contains("拉美途")) {
		if (UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_HQX) 
				|| UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_LMT)) {
			conDate = "年       月       日";
		} else {
			if (revoke == 101 || revoke == -1 || revoke == 102) {
				conDate = "年       月       日";
			} else {
				if (StringUtils.isNotBlank(conDate)) {
					Date groupDate = DateUtils.dateFormat(conDate);
					conDate = DateUtils
							.formatCustomDate(groupDate, "yyyy年M月d日");
				} else {
					conDate = "年       月       日";
				}
			}
		}
		datas.put("toAlipayAccount", toAlipayAccount);
		datas.put("conDate", conDate);
		return datas;
	}

	/**
	 * 根据序列号组织付款数据 和 大写
	 */
	private void buildMoneyData(String serialNum, StringBuffer money,
			StringBuffer moneyUpCase) {
		DecimalFormat df = new DecimalFormat("#.00");// 取两位小数点
		money = money.append("¥ ");
		BigDecimal moneyNum = new BigDecimal(0);
		BigDecimal tempNum = new BigDecimal(0);
		moneyUpCase = moneyUpCase.append("人民币");
		List<MoneyAmount> list2 = moneyAmountDao.getAmountByUid(serialNum);
		if (list2 != null && list2.size() != 0) {
			for (MoneyAmount temp : list2) {
				if (temp.getExchangerate() != null) {
					tempNum = temp.getAmount().multiply(temp.getExchangerate());
				} else {
					tempNum = temp.getAmount();
				}
				moneyNum = moneyNum.add(tempNum);
			}
		}

		money = money
				.append(MoneyNumberFormat.getThousandsMoney(Double
						.parseDouble(df.format(Double.parseDouble(moneyNum
								.toString()))),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));
		StringBuffer tempMoneyBuffer = new StringBuffer(money);
		char[] charArray = tempMoneyBuffer.toString().toCharArray();
		String tMoney2 = "";
		for(char c : charArray){
			if(org.apache.commons.lang.math.NumberUtils.isNumber(c + "") || ".".equals(c + "")){
				tMoney2 += c;
			}
		}
		moneyUpCase = moneyUpCase
				.append(MoneyNumberFormat.digitUppercase(Double.parseDouble(tMoney2)));
	}

	/**
	 * @Title: createfankuanFile
	 * @Description: TODO(生成返款单word文档)
	 * @param id
	 * @param orderType
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public File createfankuanFile(Integer id, Integer orderType, Integer type)
			throws IOException, TemplateException {
		if (id == null)
			return null;
		// word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		if (type == 1) {// 返佣付款打印
			root = getRebates4HotelIsland(id, orderType);
		} else {// 借款付款打印
			root = getJKRebates4HotelORIsland(id, orderType);
		}
		// 日期
		String createDate = root.get("createDate") == null ? "" : root.get(
				"createDate").toString();
		if (StringUtils.isNotBlank(createDate)) {
			// Date groupDate = DateUtils.dateFormat(accountDate);
			// accountDate = DateUtils.formatCustomDate(groupDate, "yyyy年M月d日");
		} else {
			createDate = "年       月       日";
		}
		root.put("createDate", createDate);
		return FreeMarkerUtil.generateFile("hotel_island_rebates.ftl",
				"hotel_island_rebates.doc", root);
	}

	/**
	 * 
	 * @Title: createPaymentFile
	 * @Description: TODO(生成收款单word文档)
	 * @param @param id
	 * @param @param orderType
	 * @param @return
	 * @param @throws IOException
	 * @param @throws TemplateException 设定文件
	 * @return File 返回类型
	 * @throws
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public File createReceiveFile(Integer id, Integer orderType)
			throws IOException, TemplateException {
		if (id == null)
			return null;
		// word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		if ("11".equals(orderType.toString())
				|| "12".equals(orderType.toString()))
			root = getAccountDataHotelIsland(id, orderType);
		else
			root = getAccountData(id, orderType);

		// 填写日期
		String payCreate = root.get("payDate") == null ? "" : root.get(
				"payDate").toString();
		if (StringUtils.isNotBlank(payCreate)) {
			// Date groupDate = DateUtils.dateFormat(payCreate);
			// payCreate = DateUtils.formatCustomDate(groupDate, "yyyy年MM月dd日");
		} else {
			payCreate = "年       月       日";
		}
		root.put("payDate", payCreate);
		// 出发/签证日期
		String groupOpenDate = root.get("startDate") == null ? "" : root.get(
				"startDate").toString();
		if (groupOpenDate != null) {
			// String groupDate = DateUtils.formatCustomDate(groupOpenDate,
			// "MM月dd日");
			// root.put("startDate", groupDate);
		} else {
			String groupDate = "年       月       日";
			root.put("startDate", groupDate);
		}
		// 银行到账日期
		String accountDate = root.get("bankGetDate") == null ? "" : root.get(
				"bankGetDate").toString();
		if (StringUtils.isNotBlank(accountDate)) {
			// Date groupDate = DateUtils.dateFormat(accountDate);
			// accountDate = DateUtils
			// .formatCustomDate(groupDate, "yyyy年M月d日");
		} else {
			accountDate = "年       月       日";
		}
		root.put("bankGetDate", accountDate);
		// 确认收款日期
		String conDate = root.get("conDate") == null ? "" : root.get("conDate")
				.toString();
		if (StringUtils.isNotBlank(conDate)) {
			// Date groupDate = DateUtils.dateFormat(conDate);
			// conDate = DateUtils.formatCustomDate(groupDate, "yyyy年M月d日");
		} else {
			conDate = "年       月       日";
		}
		root.put("confirmDate", conDate);
		return FreeMarkerUtil.generateFile("receive.ftl", "receive.doc", root);
	}

	/**
	 * 更新打印 add by chy2015年5月28日11:33:54
	 * 
	 * @return
	 */
	public String updatePrintFlag(String payId, String prdtype) {
		PayGroup payGroup = payGroupDao.getById(Integer.parseInt(payId));
		if (payGroup == null) {
			return "错误的业务数据payId。请确认收款的数据的准确性";
		}
		if (payGroup.getPrintFlag() != null && 1 == payGroup.getPrintFlag()) {// 如果不是第一次打印
																				// 则直接返回
			return "success";
		}
		payGroup.setPrintFlag(1);
		payGroup.setPrintTime(new Date());
		payGroup.setUpdateBy(Integer.parseInt(UserUtils.getUser().getId()
				.toString()));
		payGroup.setUpdateDate(new Date());
		payGroupDao.save(payGroup);
		return "success";
	}

	public String getReviewTotal(Integer flowType, Integer orderType) {
		List<UserJob> userJobs = getReviewByFlowType(flowType, orderType);
		int total = 0;
		for (UserJob userJob : userJobs) {
			total += userJob.getCount();
		}
		if (total == 0)
			return "";
		else
			return "(" + total + ")";

	}

	/**
	 * 更新打印 返款单
	 */
	public String updatePrintFlag4fkd(String pid, String prdtype, String rid) {
		// if("11".equals(prdtype)){//酒店
		// PayHotelOrder payHotelOrder =
		// payHotelOrderDao.getByPid(Integer.parseInt(pid));
		// if(payHotelOrder == null){
		// return "错误的业务数据payId。请确认收款的数据的准确性";
		// }
		// if(payHotelOrder.getPrintFlag() != null && 1 ==
		// payHotelOrder.getPrintFlag()){//如果不是第一次打印 则直接返回
		// return "success";
		// }
		// payHotelOrder.setPrintFlag(1);
		// payHotelOrder.setPrintTime(new Date());
		// payHotelOrderDao.saveObj(payHotelOrder);
		Review review = reviewDao.findOne(Long.parseLong(rid));
		if (review.getPrintFlag() != null && 1 == review.getPrintFlag()) {// 如果不是第一次打印
																			// 则直接返回
			return "success";
		}
		Date nowDate = new Date();
		review.setPrintFlag(1);
		review.setPrintTime(nowDate);
		review.setUpdateDate(nowDate);
		review.setUpdateBy(UserUtils.getUser().getId());
		reviewDao.saveObj(review);
		return "success";
		// }else{//海岛游
		// PayIslandOrder payIslandOrder =
		// payIslandOrderDao.getByPid(Integer.parseInt(pid));
		// if(payIslandOrder == null){
		// return "错误的业务数据payId。请确认收款的数据的准确性";
		// }
		// if(payIslandOrder.getPrintFlag() != null && 1 ==
		// payIslandOrder.getPrintFlag()){//如果不是第一次打印 则直接返回
		// return "success";
		// }
		// payIslandOrder.setPrintFlag(1);
		// payIslandOrder.setPrintTime(new Date());
		// payIslandOrderDao.saveObj(payIslandOrder);
		// return "success";
		// }
	}

	// ---------------------wangxinwei added 酒店（11） 和 海岛游（12）
	// 打印开始-----------------------
	// ---------------------wangxinwei added 酒店（11） 和 海岛游（12）
	// 打印开始-----------------------

	/**
	 * 
	 * @Title: createPaymentFile
	 * @Description: TODO(生成支付凭证word文档)
	 * @param @param id
	 * @param @param orderType
	 * @param @return
	 * @param @throws IOException
	 * @param @throws TemplateException 设定文件
	 * @return File 返回类型
	 * @throws
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public File createPaymentFileForHotelAndIsland(Long id, Integer orderType)
			throws IOException, TemplateException {
		if (id == null)
			return null;
		// word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();

		CostRecordHotel costRecordHotel = null;
		CostRecordIsland costRecordIsland = null;
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			costRecordHotel = costRecordHotelDao.findOne(id);
			if (null == costRecordHotel) {
				return null;
			} else {
				ActivityHotelGroup activityHotelGroup = activityHotelGroupDao
						.getByUuid(costRecordHotel.getActivityUuid());
				root.put("groupCode",
						activityHotelGroup.getGroupCode() == null ? ""
								: activityHotelGroup.getGroupCode());
			}
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			costRecordIsland = costRecordIslandDao.findOne(id);
			if (null == costRecordIsland) {
				return null;
			} else {
				ActivityIslandGroup activityIslandGroup = activityIslandGroupDao
						.getByUuid(costRecordIsland.getActivityUuid());
				root.put("groupCode",
						activityIslandGroup.getGroupCode() == null ? ""
								: activityIslandGroup.getGroupCode());
			}
		}

		PayInfoDetail payInfoDetail = null;
		// payInfoDetail =
		// refundService.getPayInfoByPayId(costRecord.getSerialNum());
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			payInfoDetail = refundService.getPayInfoByPayId(
					costRecordHotel.getSerialNum(),
					costRecordHotel.getOrderType() + "");
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			payInfoDetail = refundService.getPayInfoByPayId(
					costRecordIsland.getSerialNum(),
					costRecordIsland.getOrderType() + "");
		}

		root.put(
				"conDate",
				payInfoDetail.getCreateDate() == null ? "" : DateUtils
						.formatCustomDate(payInfoDetail.getCreateDate(),
								"yyyy年MM月dd日"));

		if (orderType == Context.ORDER_TYPE_HOTEL) {
			String dateStr = DateUtils.formatCustomDate(
					costRecordHotel.getCreateDate(), "yyyy年MM月dd日");
			root.put("nows", dateStr == null ? "" : dateStr);
			root.put("money", costRecordHotel.getName());
			root.put("person", costRecordHotel.getCreateBy().getName());
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			String dateStr = DateUtils.formatCustomDate(
					costRecordIsland.getCreateDate(), "yyyy年MM月dd日");
			root.put("nows", dateStr == null ? "" : dateStr);
			root.put("money", costRecordIsland.getName());
			if (null != costRecordIsland.getCreateBy()) {
				root.put("person", costRecordIsland.getCreateBy().getName());
			} else {
				root.put("person", "");
			}

		}

		// 暂时没有审核相关的人都置为空
		root.put("deptmanager", "");
		root.put("reviewer", "");
		root.put("manager", "");

		// 当前批发商的美元、加元汇率（目前环球行）
		List<Currency> currencylist = currencyService
				.findCurrencyList(UserUtils.getUser().getCompany().getId());
		// Integer currencyId = costRecord.getCurrencyId();
		Integer currencyId = null;
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			currencyId = costRecordHotel.getCurrencyId();
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			currencyId = costRecordIsland.getCurrencyId();
		}

		// 获取一次或多次付款金额20150409

		// List<Object[]> paydmoney =
		// moneyAmountService.getRefundPaydMoneyList(id + "");
		// String currencyID = null ;
		// String currencyMoney = null ;
		// if(paydmoney != null && paydmoney.size() > 0) {
		// currencyID = paydmoney.get(0)[0].toString();
		// currencyMoney = paydmoney.get(0)[2].toString();
		// }
		// Integer currencyId = -1;
		// if(currencyID != null && !"".equals(currencyID)){
		// currencyId = Integer.parseInt(currencyID);
		// }
		// 币种名称
		String currencyName = "";
		// 汇率
		BigDecimal currencyExchangerate = new BigDecimal("1");
		// 美元汇率
		BigDecimal currencyExchangerateUSA = null;
		// 加元汇率
		BigDecimal currencyExchangerateCAN = null;
		// 人民币计算
		BigDecimal amountCHN = null;
		// 美元计算
		BigDecimal amountUSA = null;
		// 加元计算
		BigDecimal amountCAN = null;
		if (!Collections3.isEmpty(currencylist)) {
			for (Currency currency : currencylist) {
				if (currencyId == Integer.parseInt(currency.getId() + "")) {
					currencyName = currency.getCurrencyName();
					currencyExchangerate = currency.getConvertLowest();
				}
			}
		}

		// 根据币种汇率计算人民币金额，取实际付款金额（单价*数量）20150420
		BigDecimal price = null;
		BigDecimal quantity = null;
		BigDecimal amount = new BigDecimal("0");
		// BigDecimal currentAmount = null;
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			price = costRecordHotel.getPrice();
			quantity = BigDecimal.valueOf(costRecordHotel.getQuantity());
			// amount = price.multiply(quantity).multiply(currencyExchangerate);
			amount = costRecordHotel.getPriceAfter();
			// currentAmount = price.multiply(quantity);

		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			price = costRecordIsland.getPrice();
			quantity = BigDecimal.valueOf(costRecordIsland.getQuantity());
			// amount = price.multiply(quantity).multiply(currencyExchangerate);
			amount = costRecordIsland.getPriceAfter();
			// currentAmount = price.multiply(quantity);

		}

		// if(null != currencyMoney && !"".equals(currencyMoney)){
		// currMoney = BigDecimal.valueOf(Double.parseDouble(currencyMoney));
		// }
		// BigDecimal amount = currMoney.multiply(currencyExchangerate);
		// BigDecimal currentAmount = currMoney;
		if (StringUtils.isNotBlank(currencyName)) {

			if (orderType == Context.ORDER_TYPE_HOTEL) {
				if (currencyName.startsWith("美元")) {
					currencyExchangerateUSA = currencyExchangerate;
					amountUSA = price.multiply(quantity);
				} else if (currencyName.startsWith("加")) {
					currencyExchangerateCAN = currencyExchangerate;
					amountCAN = price.multiply(quantity);
				} else {
					amountCHN = price.multiply(quantity);
				}
			} else if (orderType == Context.ORDER_TYPE_ISLAND) {
				if (currencyName.startsWith("美元")) {
					currencyExchangerateUSA = currencyExchangerate;
					amountUSA = price.multiply(quantity);
				} else if (currencyName.startsWith("加")) {
					currencyExchangerateCAN = currencyExchangerate;
					amountCAN = price.multiply(quantity);
				} else {
					amountCHN = price.multiply(quantity);
				}
			}

		}

		root.put(
				"amountCHN",
				amountCHN == null ? "" : MoneyNumberFormat.getThousandsMoney(
						Double.parseDouble(amountCHN.toString()),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));

		root.put(
				"currencyExchangerateUSA",
				currencyExchangerateUSA == null ? "" : MoneyNumberFormat
						.getThousandsMoney(
								Double.parseDouble(currencyExchangerateUSA
										.toString()),
								MoneyNumberFormat.POINT_THREE));

		root.put(
				"amountUSA",
				amountUSA == null ? "" : MoneyNumberFormat.getThousandsMoney(
						Double.parseDouble(amountUSA.toString()),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));

		root.put(
				"currencyExchangerateCAN",
				currencyExchangerateCAN == null ? "" : MoneyNumberFormat
						.getThousandsMoney(
								Double.parseDouble(currencyExchangerateCAN
										.toString()),
								MoneyNumberFormat.POINT_THREE));

		root.put(
				"amountCAN",
				amountCAN == null ? "" : MoneyNumberFormat.getThousandsMoney(
						Double.parseDouble(amountCAN.toString()),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));

		// 人民币金额汉字
		String amountChinese = amount.toString();
		if (amountChinese.contains("-")) {
			amountChinese = "红字"
					+ StringNumFormat.changeAmount(amountChinese.replaceAll(
							"-", ""));
		} else {
			amountChinese = StringNumFormat.changeAmount(amountChinese);
		}
		// String amountChinese = StringNumFormat.changeAmount(amount + "");
		root.put("currencyExchangerate", currencyExchangerate == null ? ""
				: currencyExchangerate);
		amount.setScale(2, BigDecimal.ROUND_DOWN);

		root.put(
				"amount",
				amount == null ? "" : MoneyNumberFormat.getThousandsMoney(
						Double.parseDouble(amount.toString()),
						MoneyNumberFormat.THOUSANDST_POINT_TWO));

		root.put("amountChinese", amountChinese == null ? "" : amountChinese);
		root.put("remarks", payInfoDetail.getRemarks() == null ? ""
				: payInfoDetail.getRemarks());

		if (orderType == Context.ORDER_TYPE_HOTEL) {
			root.put("supplyName", costRecordHotel.getSupplyName() == null ? ""
					: costRecordHotel.getSupplyName());
			root.put("tobankName", costRecordHotel.getBankName() == null ? ""
					: costRecordHotel.getBankName());
			root.put("tobankAccount",
					costRecordHotel.getBankAccount() == null ? ""
							: costRecordHotel.getBankAccount());
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			root.put("supplyName",
					costRecordIsland.getSupplyName() == null ? ""
							: costRecordIsland.getSupplyName());
			root.put("tobankName", costRecordIsland.getBankName() == null ? ""
					: costRecordIsland.getBankName());
			root.put("tobankAccount",
					costRecordIsland.getBankAccount() == null ? ""
							: costRecordIsland.getBankAccount());
		}

		return FreeMarkerUtil.generateFile("paymentforhotelandisland.ftl",
				"paymentforhotelandisland.doc", root);
	}

	// ---------------------酒店（11） 和 海岛游（12） 打印结束-----------------------
	// ---------------------酒店（11） 和 海岛游（12） 打印结束-----------------------
	/**
	 * 酒店、海岛游 返佣审核-返款单打印数据
	 */
	public Map<String, Object> getRebates4HotelIsland(Integer rid,
			Integer prdType) {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer buffer = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if ("11".equals(prdType.toString())) {// 酒店
			buffer.append(" SELECT or1.createDate,or1.rebatesDiff,or1.currencyId,ho.orderCompanyName ,ahg.groupCode,ah.activityName,pho.payerName,pho.toBankNname  as receive_bank_name,pho.toBankAccount as receive_account, ");
			buffer.append(" or1.orderId as pid,pho.printTime ");
			buffer.append(" FROM review r LEFT JOIN order_rebates or1 ON r.id = or1.rid ");
			buffer.append(" LEFT JOIN hotel_order ho ON or1.orderId = ho.id LEFT JOIN pay_hotel_order pho ON ho.uuid = pho.order_uuid ");
			buffer.append(" LEFT JOIN activity_hotel ah ON ho.activity_hotel_uuid = ah.uuid  LEFT JOIN activity_hotel_group ahg ON ho.activity_hotel_group_uuid = ahg.uuid ");
			buffer.append(" WHERE r.id =" + rid);
			list = costRecordDao.findBySql(buffer.toString(), Map.class);
		}
		if ("12".equals(prdType.toString())) {// 海岛游
			buffer.append(" SELECT or1.createDate,or1.rebatesDiff,or1.currencyId,ho.orderCompanyName ,ahg.groupCode,ah.activityName,pho.payerName,pho.toBankNname  as receive_bank_name,pho.toBankAccount as receive_account, ");
			buffer.append(" or1.orderId as pid,pho.printTime");
			buffer.append(" FROM review r LEFT JOIN order_rebates or1 ON r.id = or1.rid ");
			buffer.append(" LEFT JOIN island_order ho ON or1.orderId = ho.id  LEFT JOIN pay_island_order pho ON ho.uuid = pho.order_uuid ");
			buffer.append(" LEFT JOIN activity_island ah ON ho.activity_island_uuid = ah.uuid LEFT JOIN activity_island_group ahg ON ho.activity_island_group_uuid = ahg.uuid ");
			buffer.append(" WHERE r.id =" + rid);
			list = costRecordDao.findBySql(buffer.toString(), Map.class);
		}
		result = list.get(0);
		result.put("payId", result.get("pid") == null ? "" : result.get("pid")
				.toString());
		result.put("prdType", prdType);
		result.put(
				"printTime",
				result.get("printTime") == null ? "" : DateUtils
						.formatCustomDate(DateUtils.dateFormat(result.get(
								"printTime").toString()), "yyyy/MM/dd HH:mm"));// 去掉了秒:ss

		result.put("rid", rid);
		result.put("type", 1);// 返佣付款
		result.put(
				"createDate",
				result.get("createDate") == null ? "" : DateUtils
						.formatCustomDate(DateUtils.dateFormat(result.get(
								"createDate").toString()), "yyyy年 MM月 dd日"));// 日期
		String cMark = CurrencyUtils.getCurrencyNameOrFlag(
				Long.parseLong(result.get("currencyId").toString()), "0");// 币种符号
		result.put("rebatesDiff", result.get("rebatesDiff") == null ? ""
				: cMark + result.get("rebatesDiff").toString());// 金额
		result.put(
				"orderCompanyName",
				result.get("orderCompanyName") == null ? "" : result.get(
						"orderCompanyName").toString());// 组团社
		result.put("groupCode", result.get("groupCode") == null ? "" : result
				.get("groupCode").toString());// 团编号
		result.put("activityName", result.get("activityName") == null ? ""
				: result.get("activityName").toString());// 团号
		// result.put("payerName", result.get("payerName") == null ? "" :
		// result.get("payerName").toString());//户名
		// result.put("receive_bank_name", result.get("receive_bank_name") ==
		// null ? "" : result.get("receive_bank_name").toString());//开户行
		// result.put("receive_account", result.get("receive_account") == null ?
		// "" : result.get("receive_account").toString());//账号

		List<Refund> redundlist = refundDao.findLastPayByRecordId(
				Long.parseLong(rid.toString()), 3, prdType);
		if (null != redundlist && redundlist.size() > 0) {
			result.put("payerName", redundlist.get(0).getPayee());
			// 支票和现金支付
			if (redundlist.get(0).getPayType() == 1
					|| redundlist.get(0).getPayType() == 3) {
				result.put("receive_bank_name", "");
				result.put("receive_account", "");
				// 汇款
			} else if (redundlist.get(0).getPayType() == 4) {
				PayRemittance payRemittance = payRemittanceDao
						.findPayRemittanceInfoById(redundlist.get(0)
								.getPayTypeId());
				result.put("receive_bank_name", payRemittance.getTobankName());
				result.put("receive_account", payRemittance.getTobankAccount());
				// 银行转账
			} else if (redundlist.get(0).getPayType() == 6) {
				PayBanktransfer payBanktransfer = payBanktransferDao
						.getByUuid(redundlist.get(0).getPayTypeId());
				result.put("receive_bank_name",
						payBanktransfer.getReceiveBankName());
				result.put("receive_account",
						payBanktransfer.getReceiveAccount());
				// 汇票
			} else if (redundlist.get(0).getPayType() == 7) {
				PayDraft payDraft = payDraftDao.getByUuid(redundlist.get(0)
						.getPayTypeId());
				result.put("receive_bank_name", payDraft.getReceiveBankName());
				result.put("receive_account", payDraft.getReceiveAccount());
				// POS机刷卡
			} else if (redundlist.get(0).getPayType() == 8) {
				PayPos payPos = payPosDao.getByUuid(redundlist.get(0)
						.getPayTypeId());
				result.put("receive_bank_name", payPos.getReceiveBankName());
				result.put("receive_account", payPos.getReceiveAccount());
			}
		} else {
			result.put("payerName", "");
			result.put("receive_bank_name", "");
			result.put("receive_account", "");
		}

		return result;
	}

	/**
	 * 借款付款-打印（请款单）
	 * 
	 * @param rid
	 * @param prdType
	 * @return
	 */
	public Map<String, Object> getJKRebates4HotelORIsland(Integer rid,
			Integer prdType) {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer buffer = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if ("11".equals(prdType.toString())) {// 酒店
			buffer.append(" SELECT or1.createDate, or1.amount AS rebatesDiff,or1.currencyId, ho.orderCompanyName, ahg.groupCode, ah.activityName, pho.payerName, pho.toBankNname AS receive_bank_name, pho.toBankAccount AS receive_account, r.orderId AS pid, pho.printTime ");
			buffer.append(" FROM review r LEFT JOIN hotel_money_amount or1 ON or1.reviewId = r.id LEFT JOIN hotel_order ho ON ho.id = r.orderId LEFT JOIN pay_hotel_order pho ON ho.uuid = pho.order_uuid  ");
			buffer.append(" LEFT JOIN activity_hotel ah ON ho.activity_hotel_uuid = ah.uuid LEFT JOIN activity_hotel_group ahg ON ho.activity_hotel_group_uuid = ahg.uuid  ");
			buffer.append(" WHERE r.id =" + rid);
			list = costRecordDao.findBySql(buffer.toString(), Map.class);
		}
		if ("12".equals(prdType.toString())) {// 海岛游
			buffer.append(" SELECT or1.createDate, or1.amount AS rebatesDiff,or1.currencyId, ho.orderCompanyName, ahg.groupCode, ah.activityName, pho.payerName, pho.toBankNname AS receive_bank_name, pho.toBankAccount AS receive_account, r.orderId AS pid, pho.printTime ");
			buffer.append(" FROM review r LEFT JOIN island_money_amount or1 ON or1.reviewId = r.id ");
			buffer.append(" LEFT JOIN island_order ho ON ho.id = r.orderId  LEFT JOIN pay_island_order pho ON ho.uuid = pho.order_uuid LEFT JOIN activity_island ah ON ho.activity_island_uuid = ah.uuid ");
			buffer.append(" LEFT JOIN activity_island_group ahg ON ho.activity_island_group_uuid = ahg.uuid ");
			buffer.append(" WHERE r.id =" + rid);
			list = costRecordDao.findBySql(buffer.toString(), Map.class);
		}
		result = list.get(0);
		result.put("payId", result.get("pid") == null ? "" : result.get("pid")
				.toString());
		result.put("prdType", prdType);
		result.put(
				"printTime",
				result.get("printTime") == null ? "" : DateUtils
						.formatCustomDate(DateUtils.dateFormat(result.get(
								"printTime").toString()), "yyyy/MM/dd HH:mm"));// 去掉了秒:ss

		result.put("rid", rid);
		result.put("type", 2);// 借款付款
		result.put(
				"createDate",
				result.get("createDate") == null ? "" : DateUtils
						.formatCustomDate(DateUtils.dateFormat(result.get(
								"createDate").toString()), "yyyy年 MM月 dd日"));// 日期
		String cMark = CurrencyUtils.getCurrencyNameOrFlag(
				Long.parseLong(result.get("currencyId").toString()), "0");// 币种符号
		result.put("rebatesDiff", result.get("rebatesDiff") == null ? ""
				: cMark + result.get("rebatesDiff").toString());// 金额
		result.put(
				"orderCompanyName",
				result.get("orderCompanyName") == null ? "" : result.get(
						"orderCompanyName").toString());// 组团社
		result.put("groupCode", result.get("groupCode") == null ? "" : result
				.get("groupCode").toString());// 团编号
		result.put("activityName", result.get("activityName") == null ? ""
				: result.get("activityName").toString());// 团号
		// result.put("payerName", result.get("payerName") == null ? "" :
		// result.get("payerName").toString());//户名
		// result.put("receive_bank_name", result.get("receive_bank_name") ==
		// null ? "" : result.get("receive_bank_name").toString());//开户行
		// result.put("receive_account", result.get("receive_account") == null ?
		// "" : result.get("receive_account").toString());//账号

		List<Refund> redundlist = refundDao.findLastPayByRecordId(
				Long.parseLong(rid.toString()), 4, prdType);
		if (null != redundlist && redundlist.size() > 0) {
			result.put("payerName", redundlist.get(0).getPayee());
			// 支票和现金支付
			if (redundlist.get(0).getPayType() == 1
					|| redundlist.get(0).getPayType() == 3) {
				result.put("receive_bank_name", "");
				result.put("receive_account", "");
				// 汇款
			} else if (redundlist.get(0).getPayType() == 4) {
				PayRemittance payRemittance = payRemittanceDao
						.findPayRemittanceInfoById(redundlist.get(0)
								.getPayTypeId());
				result.put("receive_bank_name", payRemittance.getTobankName());
				result.put("receive_account", payRemittance.getTobankAccount());
				// 银行转账
			} else if (redundlist.get(0).getPayType() == 6) {
				PayBanktransfer payBanktransfer = payBanktransferDao
						.getByUuid(redundlist.get(0).getPayTypeId());
				result.put("receive_bank_name",
						payBanktransfer.getReceiveBankName());
				result.put("receive_account",
						payBanktransfer.getReceiveAccount());
				// 汇票
			} else if (redundlist.get(0).getPayType() == 7) {
				PayDraft payDraft = payDraftDao.getByUuid(redundlist.get(0)
						.getPayTypeId());
				result.put("receive_bank_name", payDraft.getReceiveBankName());
				result.put("receive_account", payDraft.getReceiveAccount());
				// POS机刷卡
			} else if (redundlist.get(0).getPayType() == 8) {
				PayPos payPos = payPosDao.getByUuid(redundlist.get(0)
						.getPayTypeId());
				result.put("receive_bank_name", payPos.getReceiveBankName());
				result.put("receive_account", payPos.getReceiveAccount());
			}
		} else {
			result.put("payerName", "");
			result.put("receive_bank_name", "");
			result.put("receive_account", "");
		}

		return result;
	}

	/**
	 * 查询海岛酒店 打印数据 11 酒店 12 海岛
	 */
	public Map<String, Object> getAccountDataHotelIsland(Integer payId,
			Integer prdType) {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer buffer = new StringBuffer();
		String confirmDate = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if ("11".equals(prdType.toString())) {
			buffer.append("SELECT pg.isAsAccount,pg.accountDate,pg.payType,pg.payTypeName,pg.payerName,pg.remarks,pg.posBank,pg.bankName,pg.toBankNname,pg.bankAccount,pg.toBankAccount,");
			buffer.append(" pg.payPrice,pg.payPriceType,pg.createBy,pg.createDate,pg.updateBy,pg.payPriceBack,pg.orderType,pg.printTime,pg.printFlag,");
			buffer.append(" pg.cost_record_id,ahg.groupCode,ahg.groupOpenDate,ah.activityName,hma.amount*hma.exchangerate amount,FORMAT(hma.amount*hma.exchangerate,2) money");
			buffer.append(" FROM pay_group pg");
			buffer.append(" LEFT JOIN  activity_hotel_group ahg ON  pg.groupId = ahg.id");
			buffer.append(" LEFT JOIN  activity_hotel ah   ON ahg.activity_hotel_uuid = ah.uuid");
			buffer.append(" LEFT JOIN  hotel_money_amount hma ON pg.payPrice = hma.serialNum");
			buffer.append(" WHERE pg.id=");
			buffer.append(payId);
			list = costRecordDao.findBySql(buffer.toString(), Map.class);
		}
		if ("12".equals(prdType.toString())) {
			buffer.append("SELECT pg.isAsAccount,pg.accountDate,pg.payType,pg.payTypeName,pg.payerName,pg.remarks,pg.posBank,pg.bankName,pg.toBankNname,pg.bankAccount,pg.toBankAccount,");
			buffer.append(" pg.payPrice,pg.payPriceType,pg.createBy,pg.createDate,pg.updateBy,pg.payPriceBack,pg.orderType,pg.printTime,pg.printFlag,");
			buffer.append(" pg.cost_record_id,ahg.groupCode,ahg.groupOpenDate,ah.activityName,hma.amount*hma.exchangerate amount,FORMAT(hma.amount*hma.exchangerate,2) money ");
			buffer.append(" FROM pay_group pg");
			buffer.append(" LEFT JOIN  activity_island_group  ahg ON  pg.groupId = ahg.id");
			buffer.append(" LEFT JOIN  activity_island  ah   ON ahg.activity_island_uuid = ah.uuid");
			buffer.append(" LEFT JOIN  island_money_amount  hma ON pg.payPrice = hma.serialNum");
			buffer.append(" WHERE pg.id=");
			buffer.append(payId);
			list = costRecordDao.findBySql(buffer.toString(), Map.class);
		}
		result = list.get(0);
		String pName = result.get("createBy") == null
				|| "".equals(result.get("createBy").toString()) ? ""
				: UserUtils.getUserNameById(Long.parseLong(result.get(
						"createBy").toString()));
		String payerName = (result.get("payerName") == null ? "" : result.get(
				"payerName").toString())
				+ " "
				+ (result.get("bankName") == null ? "" : result.get("bankName")
						.toString())
				+ " "
				+ (result.get("bankAccount") == null ? "" : result.get(
						"bankAccount").toString());
		String toBankAccount = (result.get("toBankNname") == null ? "" : result
				.get("toBankNname").toString())
				+ (result.get("toBankAccount") == null ? "" : result.get(
						"toBankAccount").toString());
		String shouKuanRen = "";
		String isAccount = result.get("isAsAccount") == null ? "" : result.get(
				"isAsAccount").toString();
		if ("1".equals(isAccount)) {
			shouKuanRen = result.get("updateBy") == null
					|| "".equals(result.get("updateBy").toString()) ? ""
					: UserUtils.getUserNameById(Long.parseLong(result.get(
							"updateBy").toString()));
		}
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("payId", payId);
		datas.put("prdType", prdType);
		datas.put(
				"printTime",
				result.get("printTime") == null ? "" : DateUtils
						.formatCustomDate(DateUtils.dateFormat(result.get(
								"printTime").toString()), "yyyy/MM/dd HH:mm"));// 去掉了秒:ss
		datas.put(
				"payDate",
				result.get("createDate") == null ? "" : DateUtils
						.formatCustomDate(DateUtils.dateFormat(result.get(
								"createDate").toString()), "yyyy年MM月dd日"));// 填写日期
		datas.put("groupCode",
				result.get("groupCode") == null ? "" : result.get("groupCode"));// 团号
		String date = DateUtils.formatCustomDate(DateUtils.dateFormat(result
				.get("groupOpenDate").toString(), "yyyy-MM-dd"), "MM月dd日");
		datas.put("startDate", result.get("groupOpenDate") == null ? "" : date);// 出发/签证日期
		datas.put("applyPerson", pName);// 经办人--团期表的创建人
		datas.put("productName", result.get("activityName") == null ? ""
				: result.get("activityName").toString());// 线路/产品
		datas.put("payerName", payerName);// 来款单位信息
		datas.put("airRefund", "团费");// 款项
		datas.put("remarks",
				result.get("remarks") == null ? "" : result.get("remarks")
						.toString());// 备注
		if (null == result.get("amount")) {
			datas.put("payPrice", "");// 收款金额
			datas.put("payPriceUpCase", "");// 大写收款金额
		} else {
			datas.put("payPrice", "￥" + result.get("money"));// 收款金额
			datas.put(
					"payPriceUpCase",
					"人民币"
							+ StringNumFormat.changeAmount(result.get("amount")
									.toString()));// 大写收款金额
		}
		datas.put("toBankAccount", toBankAccount);// 收款账户
		datas.put("payPerson", pName);// 交款人
		datas.put("shouKuanRen", shouKuanRen);// 收款人 --》财务
		datas.put(
				"bankGetDate",
				result.get("accountDate") == null ? "" : DateUtils
						.formatCustomDate(DateUtils.dateFormat(result.get(
								"accountDate").toString()), "yyyy年MM月dd日"));// 银行到账日期
		datas.put(
				"confirmDate",
				confirmDate == "" ? "" : DateUtils.formatCustomDate(
						DateUtils.dateFormat(confirmDate), "yyyy年MM月dd日"));// 确认收款日期
		return datas;
	}

	// 成本项已付款金额
	public String getPayCost(String costId, String costType, String moneyType) {
		String sqlStr = "SELECT SUM(amount) as amount,currencyid,currency_mark  FROM  refund  JOIN money_amount m ON m.serialNum=refund.money_serial_num"
				+ "  JOIN currency ON currency.currency_id= m.currencyId  WHERE refund.record_id=? AND refund.ordertype=? AND refund.moneytype=? and"
				+ " refund.STATUS IS NULL and refund.pay_type IN (1,3,4)  GROUP BY currencyid";
		List<Map<String, Object>> list = costRecordIslandDao.findBySql(sqlStr,
				Map.class, costId, costType, moneyType);
		if (list.size() == 0) {
			return "";
		} else {
			String tmp = "";
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("currencyid") != null) {
					tmp += list.get(i).get("currency_mark").toString()
							+ list.get(i).get("amount").toString() + " ";
				}
			}
			return tmp;
		}
	}

	// 成本项已付款金额
	public String getRefundTotal(String reviewId) {
		String sqlStr = "SELECT d.mykey,d.myvalue   FROM review JOIN review_detail d  ON d.review_id=review.id WHERE review.id=? ORDER BY d.mykey";
		List<Map<String, String>> list = costRecordIslandDao.findBySql(sqlStr,
				Map.class, reviewId);
		if (list.size() == 0) {
			return "";
		} else {
			String tmp = "";
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("mykey").equals("currencyMark")
						|| list.get(i).get("mykey").equals("refundPrice")) {
					tmp += list.get(i).get("myvalue");
				}
			}
			return tmp;
		}
	}

	// 获得订单总额
	public String getOrderTotal(String orderId, String prdType) {
		String totalMoney = "";
		if ("7".equals(prdType.trim()) || "8".equals(prdType.trim())) {
			// 由于这个内容和申请退款一致 所以调用了申请退款的这个查询
			Map<String, Object> orderDetail = airTicketRefundReviewService
					.queryAirticketorderDeatail(orderId, prdType);
			// 处理多币种信息 start
			totalMoney = orderDetail.get("totalmoney") == null ? null
					: orderDetail.get("totalmoney").toString();
			return moneyAmountService.getMoney(totalMoney);
		} else if ("6".equals(prdType.trim())) {// 6代表签证 查询签证信息
			Map<String, Object> orderDetail = airTicketRefundReviewService
					.queryVisaorderDeatail(orderId);
			totalMoney = orderDetail.get("totalmoney") == null ? null
					: orderDetail.get("totalmoney").toString();
			return moneyAmountService.getMoney(totalMoney);
		} else if ("2".equals(prdType.trim())) {// 2代表 散拼
			Map<String, Object> orderDetail = airTicketRefundReviewService
					.querySanPinReviewOrderDetail(orderId);
			// 处理多币种信息 start
			totalMoney = orderDetail.get("totalmoney") == null ? null
					: orderDetail.get("totalmoney").toString();
			return moneyAmountService.getMoney(totalMoney);
		} else {// 查询参团信息 1、3、4、5
			Map<String, Object> grouporderDeatail = airTicketRefundReviewService
					.queryGrouporderDeatail(orderId);
			// 处理多币种信息
			totalMoney = grouporderDeatail.get("totalmoney") == null ? null
					: grouporderDeatail.get("totalmoney").toString();
			return moneyAmountService.getMoney(totalMoney);
		}
	}

	/**
	 * 获取签证未读订单数量
	 * 
	 * @author xudong.he
	 * @return
	 */
	public int getVisaCount() {
		User user = UserUtils.getUser();
		// 按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("visaOrder", null);
		int count = 0;
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT COUNT(vo.isRead) ct,vo.id FROM visa_order vo");
		buffer.append(" LEFT JOIN sys_user su  ON su.id= vo.create_by LEFT JOIN visa_products vp  ON vo.visa_product_id = vp.id");
		buffer.append(" LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ");
		buffer.append("   ");
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
			departmentSQL(common, buffer);
		}
		buffer.append(" WHERE vo.visa_order_status != 100 AND vo.del_flag = 0 and vo.isRead=0 AND vo.create_by IS NOT NULL");
		buffer.append(" AND su.companyId=");
		buffer.append(user.getCompany().getId());
		buffer.append(" AND a.status = 1 AND a.delFlag=0 ");
		buffer.append("   ");

		// 部门ID不为空时拼接部门SQL的where条件
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
			departmentWhereSQL(common, buffer);
		}

		boolean flag = false;
		// 销售签证订单过滤渠道 true:只能看自己渠道 false:看所有渠道
		String userType = UserUtils.getUser().getUserType();
		// 如果不是销售经理或管理员，则用户只能查看自己负责的渠道
		if (Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)) {
			boolean isSaleManager = false;
			boolean isManager = false;
			boolean isFinance = false;
			for (Role role : UserUtils.getUser().getRoleList()) {
				if (Context.ROLE_TYPE_MANAGER.equals(role.getRoleType())) {
					isManager = true;
				}
				if (Context.ROLE_TYPE_SALES_EXECUTIVE
						.equals(role.getRoleType())) {
					isSaleManager = true;
				}
			}
			List<UserJob> list = userJobDao.getUserJobList(UserUtils.getUser()
					.getId());
			for (UserJob userJob : list) {
				String jobName = userJob.getJobName();
				if (jobName.indexOf("财务") != -1) {
					isFinance = true;
					break;
				}
			}
			if (isFinance) {
				flag = false;
			} else if (!isManager && !isSaleManager) {
				flag = true;
			} else {
				flag = false;
			}
		}

		if (flag) {
			buffer.append("AND (a.agentSalerId=" + UserUtils.getUser().getId()
					+ " or a.id=-1) ");
		} else {
			buffer.append("AND (a.supplyId="
					+ UserUtils.getUser().getCompany().getId()
					+ " OR a.id=-1) ");
		}

		List<Map<Object, Object>> list = costRecordDao.findBySql(
				buffer.toString(), Map.class);
		count = Integer.parseInt(list.get(0).get("ct") + "");
		return count;
	}

	// 拼接部门SQL
	private StringBuffer departmentSQL(DepartmentCommon common,
			StringBuffer sqlBuffer) {

		/** 要查询部门ID */
		String departmentId = common.getDepartmentId();
		/** 部门之间人员是否能相互查看，默认不能 */
		boolean flag = whetherSelectAllDeptDate(common);
		/** 是否是管理员 */
		boolean isManager = common.getRoleTypeList().contains(
				Context.ROLE_TYPE_MANAGER);
		/** 是否是计调经理 */
		boolean isOPManager = common.getRoleTypeList().contains(
				Context.ROLE_TYPE_OP_EXECUTIVE);
		/** 是否是计调 */
		boolean isOP = common.getRoleTypeList().contains(Context.ROLE_TYPE_OP);
		/** 是否是销售经理 */
		boolean isSaleManager = common.getRoleTypeList().contains(
				Context.ROLE_TYPE_SALES_EXECUTIVE);
		/** 是否是销售 */
		boolean isSale = common.getRoleTypeList().contains(
				Context.ROLE_TYPE_SALES);

		if (!isManager) {
			if (isOPManager) {
				if (flag) {
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND (dept.id = '"
									+ departmentId
									+ "' OR dept.parent_ids LIKE '%,"
									+ departmentId
									+ ",%' ) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
				} else {
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND (dept.id = '"
									+ departmentId
									+ "' OR dept.parent_ids LIKE '%,"
									+ departmentId
									+ ",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
				}
			}
			if (isSaleManager) {
				if (flag) {
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND (dept.id = '"
									+ departmentId
									+ "' OR dept.parent_ids LIKE '%,"
									+ departmentId
									+ ",%' ) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
				} else {
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND (dept.id = '"
									+ departmentId
									+ "' OR dept.parent_ids LIKE '%,"
									+ departmentId
									+ ",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
				}
			}
			if (!isSaleManager && !isOPManager && (isOP || isSale)) {
				if (flag) {
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND sr.deptId = '"
									+ departmentId
									+ "' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND sr.deptId = '"
									+ departmentId
									+ "' AND su.delFlag = '0') saledept ON vo.create_by=opdept.id ");
				}
			}
			if (!isSaleManager && !isOPManager && !isOP && !isSale) {
				if (flag) {
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND (dept.id = '"
									+ departmentId
									+ "' OR dept.parent_ids LIKE '%,"
									+ departmentId
									+ ",%' ) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND (dept.id = '"
									+ departmentId
									+ "' OR dept.parent_ids LIKE '%,"
									+ departmentId
									+ ",%' ) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
				} else {
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND (dept.id = '"
									+ departmentId
									+ "' OR dept.parent_ids LIKE '%,"
									+ departmentId
									+ ",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
					sqlBuffer
							.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "
									+ UserUtils.getUser().getCompany().getId()
									+ " AND (dept.id = '"
									+ departmentId
									+ "' OR dept.parent_ids LIKE '%,"
									+ departmentId
									+ ",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
				}
			}
		}

		return sqlBuffer;
	}

	/**
	 * 判断此部门是否允许人员相互查看数据： 获取有此权限标示的菜单；然后查看此部门可以相互查看的菜单列表；
	 * 如果此菜单列表中包含这个菜单则表示允许相互查看
	 * 
	 * @param common
	 * @return
	 */
	private boolean whetherSelectAllDeptDate(DepartmentCommon common) {

		/** 判断是否是部门之间人员可以相互查看请求 */
		List<Menu> menuList = menuDao.findByPermission(common.getPermission());// 查询请求的菜单
		Department dept = departmentDao.findOne(Long.parseLong(common
				.getDepartmentId()));// 查询用于查询的部门
		if (CollectionUtils.isNotEmpty(menuList)) {
			List<Integer> list = departmentDao.findSelectIdsByDeptId(dept
					.getId());
			if (CollectionUtils.isNotEmpty(list)) {
				return list.contains(menuList.get(0).getId().intValue());
			}
		}
		return false;
	}

	// 拼接部门SQL的WHERE条件
	private StringBuffer departmentWhereSQL(DepartmentCommon common,
			StringBuffer sqlBuffer) {

		/** 部门之间人员是否能相互查看，默认不能 */
		boolean flag = whetherSelectAllDeptDate(common);
		/** 是否是管理员 */
		boolean isManager = common.getRoleTypeList().contains(
				Context.ROLE_TYPE_MANAGER);
		/** 是否是计调经理 */
		boolean isOPManager = common.getRoleTypeList().contains(
				Context.ROLE_TYPE_OP_EXECUTIVE);
		/** 是否是计调 */
		boolean isOP = common.getRoleTypeList().contains(Context.ROLE_TYPE_OP);
		/** 是否是销售经理 */
		boolean isSaleManager = common.getRoleTypeList().contains(
				Context.ROLE_TYPE_SALES_EXECUTIVE);
		/** 是否是销售 */
		boolean isSale = common.getRoleTypeList().contains(
				Context.ROLE_TYPE_SALES);

		if (!isManager) {
			if (isOPManager && isSaleManager) {
				sqlBuffer
						.append("and (vp.createBy=opdept.id or vo.create_by=saledept.id) ");
			} else if (isOPManager) {
				sqlBuffer.append("and vp.createBy=opdept.id ");
			} else if (isSaleManager) {
				sqlBuffer.append("and vo.create_by=saledept.id ");
			}
			if (!isSaleManager && !isOPManager && (isOP || isSale)) {
				if (flag) {
					sqlBuffer
							.append("and (vp.createBy=opdept.id or vo.create_by=saledept.id) ");
				} else {
					sqlBuffer.append("and (vp.createBy='"
							+ UserUtils.getUser().getId()
							+ "' or vo.create_by='"
							+ UserUtils.getUser().getId() + "') ");
				}
			}
			if (!isSaleManager && !isOPManager && !isOP && !isSale) {
				sqlBuffer
						.append("and (vp.createBy=opdept.id or vo.create_by=saledept.id) ");
			}
		}

		return sqlBuffer;
	}

	public List<SysCompanyDictView> supplierList(Long companyId) {
		String value = "";
		if (companyId == 68) {
			value = "1,5,8";
		} else {
			value = "11";
		}
		String sql = "select * from sys_company_dict_view where type = 'travel_agency_type' and (companyId=-1 or companyId="
				+ companyId
				+ ") and delFlag='0' and  value not in ("
				+ value
				+ ") order by sort";
		return menuDao.findBySql(sql, Map.class);

	}

	/**
	 * 拉美途--预算成本中添加签证费信息
	 * 
	 * @author zzk
	 * @param budgetInList
	 * @param groupId
	 * @param typeId
	 * @return
	 */
	public List<CostRecord> getBudgetInList(List<CostRecord> budgetInList,
			Long groupId, Integer typeId) {
		List<CostRecord> visaList = new ArrayList<CostRecord>();
		visaList = this.getVisaCost(groupId); // 通过统计获取的签证费信息
		List<CostRecord> visaListFromCost = this.findCostRecordList(groupId, 0,
				0, typeId, 0); // 从cost_record表中获取的签证费信息
		List<CostRecord> notDelList = new ArrayList<CostRecord>();
		List<CostRecord> delList = new ArrayList<CostRecord>();
		for (CostRecord c : visaListFromCost) {
			if ("0".equals(c.getDelFlag())) {
				notDelList.add(c);
			}
			if ("1".equals(c.getDelFlag())) {
				delList.add(c);
			}
		}

		// List<CostRecord> visaTempList = new ArrayList<CostRecord>();
		// for(CostRecord c : visaList) {
		// visaTempList.add(c);
		// }

		// 删除的签证费不显示
		for (int i = 0; i < visaList.size(); i++) {
			for (int j = 0; j < delList.size(); j++) {
				if (visaList.get(i).getId().equals(delList.get(j).getVisaId())) {
					visaList.remove(visaList.get(i));
				}
			}
		}

		// 审核通过的签证费显示
		for (int i = 0; i < visaList.size(); i++) {
			for (int j = 0; j < notDelList.size(); j++) {
				if (visaList.get(i).getId()
						.equals(notDelList.get(j).getVisaId())) {
					visaList.remove(visaList.get(i));
					visaList.add(notDelList.get(j));
				}
			}
		}

		if (null != budgetInList) {
			budgetInList.addAll(visaList);
		} else {
			budgetInList = visaList;
		}
		return budgetInList;
	}

	/**
	 * 拉美途--预算成本中添加签证费信息--新审批
	 * 
	 * @author zzk
	 * @param budgetInList
	 * @param groupId
	 * @param typeId
	 * @return
	 */
	private List<Map<String, Object>> getBudgetInListNew(
			List<Map<String, Object>> budgetInList, Long groupId, Integer typeId) {
		List<CostRecord> visaList = new ArrayList<CostRecord>();
		visaList = this.getVisaCost(groupId); // 通过统计获取的签证费信息
		List<Map<String, Object>> visaMap = new ArrayList<Map<String, Object>>();
		for (CostRecord cr : visaList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", cr.getName());
			map.put("quantity", cr.getQuantity());
			map.put("supplyName", cr.getSupplyName());
			map.put("currencyId", cr.getCurrencyId());
			map.put("price", cr.getPrice());
			map.put("rate", cr.getRate());
			map.put("currencyAfter", cr.getCurrencyAfter());
			map.put("priceAfter", cr.getPriceAfter());
			map.put("comment", cr.getComment());
			map.put("createBy", cr.getCreateBy().getId());
			map.put("reviewType", cr.getReviewType());
			map.put("id", cr.getId());
			map.put("reviewId", cr.getReviewId());
			map.put("reviewUuid", null);
			map.put("status", null);
			map.put("current_reviewer", null);
			map.put("delFlag", cr.getDelFlag());
			map.put("visaId", cr.getVisaId());
			map.put("uuid", cr.getUuid());
			map.put("activityId", cr.getActivityId());
			map.put("orderType", cr.getOrderType());
			map.put("pay_review_uuid", null);
			map.put("budgetType", cr.getBudgetType());
			map.put("overseas", cr.getOverseas());
			map.put("review", 4);
			visaMap.add(map);
		}
		List<Map<String, Object>> visaListFromCost = this.getCostRecordList(
				groupId, 0, 0, typeId, 1); // 从cost_record表中获取的签证费信息
		List<Map<String, Object>> notDelList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> delList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> c : visaListFromCost) {
			if ("0".equals(c.get("delFlag"))) {
				notDelList.add(c);
			}
			if ("1".equals(c.get("delFlag"))) {
				delList.add(c);
			}
		}

		// List<CostRecord> visaTempList = new ArrayList<CostRecord>();
		// for(CostRecord c : visaList) {
		// visaTempList.add(c);
		// }

		// 删除的签证费不显示
		for (int i = 0; i < visaMap.size(); i++) {
			for (int j = 0; j < delList.size(); j++) {
				if (visaMap.get(i).get("id").toString()
						.equals(delList.get(j).get("visaId").toString())) {
					visaMap.remove(visaMap.get(i));
				}
			}
		}

		// 审核通过的签证费显示
		for (int i = 0; i < visaMap.size(); i++) {
			for (int j = 0; j < notDelList.size(); j++) {
				if (visaMap.get(i).get("id").toString()
						.equals(notDelList.get(j).get("visaId").toString())) {
					visaMap.remove(visaMap.get(i));
					visaMap.add(notDelList.get(j));
				}
			}
		}

		if (null != budgetInList) {
			budgetInList.addAll(visaMap);
		} else {
			budgetInList = visaMap;
		}
		return budgetInList;
	}

	/**
	 * 获取user的名称
	 * 
	 * @param users
	 * @return
	 */
	private String getNames(List<User> users) {
		String res = " ";
		int n = 0;
		if (users == null || users.size() == 0) {
			return res;
		}
		for (User user : users) {
			if (n == 0) {
				res = res.trim();
				res += user.getName();
				n++;
			} else {
				res += "," + user.getName();
			}
		}
		return res;
	}

	/**
	 * 0004需求 导出Excel by chy 2015年12月23日14:30:22 生成Excel
	 * 
	 * @param list
	 * @return
	 */
	public Workbook makeExcel(List<Map<Object, Object>> list) {
		String fontStyle = "Courier New";
		// 表头
		String[] heads = { "序号", "收款日期","银行到账日期", "团号", "产品名称", "团队类型", "计调",
				"渠道商/地接社", "来款单位", "收款银行", "款项", "已收金额","到账金额", "收款方式", "打印状态" };
		// 创建Excel
		HSSFWorkbook workBook = new HSSFWorkbook();

		// 表头字体和样式
		HSSFFont headFont = workBook.createFont();
		HSSFCellStyle headStyle = workBook.createCellStyle();
		headFont.setFontHeightInPoints((short) 10);
		headFont.setFontName(fontStyle);
		headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headStyle.setFont(headFont);
		headStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headStyle.setBorderTop(CellStyle.BORDER_THIN);
		headStyle.setBorderRight(CellStyle.BORDER_THIN);

		// 订单数据字体和样式
		HSSFFont dataFont = workBook.createFont();
		HSSFCellStyle dataStyle = workBook.createCellStyle();
		dataFont.setFontHeightInPoints((short) 10);
		dataFont.setFontName(fontStyle);
		dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		dataStyle.setFont(dataFont);
		dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
		dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
		dataStyle.setBorderTop(CellStyle.BORDER_THIN);
		dataStyle.setBorderRight(CellStyle.BORDER_THIN);
		
		HSSFCellStyle dataStyle2 = workBook.createCellStyle();
		dataStyle2.setAlignment(CellStyle.ALIGN_CENTER);
		dataStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		dataStyle2.setFont(dataFont);
		dataStyle2.setBorderBottom(CellStyle.BORDER_THIN);
		dataStyle2.setBorderLeft(CellStyle.BORDER_THIN);
		dataStyle2.setBorderTop(CellStyle.BORDER_THIN);
		dataStyle2.setBorderRight(CellStyle.BORDER_THIN);
		HSSFDataFormat format = workBook.createDataFormat();
		dataStyle2.setDataFormat(format.getFormat("#,##0.00"));
		

		HSSFCellStyle moneyStyle = workBook.createCellStyle();
		moneyStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		moneyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		moneyStyle.setFont(dataFont);
		moneyStyle.setBorderBottom(CellStyle.BORDER_THIN);
		moneyStyle.setBorderLeft(CellStyle.BORDER_THIN);
		moneyStyle.setBorderTop(CellStyle.BORDER_THIN);
		moneyStyle.setBorderRight(CellStyle.BORDER_THIN);

		HSSFCellStyle mergeStyle = workBook.createCellStyle();
		mergeStyle.setWrapText(true);
		mergeStyle.setAlignment(CellStyle.ALIGN_CENTER);
		mergeStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		mergeStyle.setFont(dataFont);
		mergeStyle.setBorderBottom(CellStyle.BORDER_THIN);
		mergeStyle.setBorderLeft(CellStyle.BORDER_THIN);
		mergeStyle.setBorderTop(CellStyle.BORDER_THIN);
		mergeStyle.setBorderRight(CellStyle.BORDER_THIN);

		HSSFSheet sheet = workBook.createSheet();
		sheet.setDefaultColumnWidth(20);
		int columnIndex = 0;
		HSSFRow headRow = sheet.createRow(0);
		headRow.setHeightInPoints((short) 30);
		for (int i = 0; i < heads.length; i++) {
			HSSFCell cell = headRow.createCell(columnIndex);
			cell.setCellValue(heads[i]);
			cell.setCellStyle(headStyle);
			 if("已收金额".equals(heads[i]) || "到账金额".equals(heads[i]) ){
				 sheet.setColumnWidth(columnIndex,  5*256);
				 sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex+1));
				 columnIndex = columnIndex +2;
			} else {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex));
				columnIndex = columnIndex +1;
			}
		}
		if (null == list) {
			return workBook;
		}
		for (int i = 0; i < list.size(); i++) {
			HSSFRow dataRow = sheet.createRow(i + 1);
			dataRow.setHeightInPoints((short) 25);
			createCell(dataRow, 0, i + 1, dataStyle);// "序号"
			String createDate = list.get(i).get("createDate") == null ? ""
					: DateUtils.formatCustomDate(
							DateUtils.dateFormat(list.get(i).get("createDate")
									.toString()), "yyyy-MM-dd");
			String accountDate = list.get(i).get("accountDate") == null ? ""
					: DateUtils.formatCustomDate(
							DateUtils.dateFormat(list.get(i).get("accountDate")
									.toString()), "yyyy-MM-dd");
//			createMergedCell(dataRow, 1, createDate, accountDate, mergeStyle);// "付款日期/银行到账日期"
			createCell(dataRow, 1, createDate, dataStyle);// "付款日期"
			String isAsAccount = list.get(i).get("isAsAccount") == null ? "": list.get(i).get("isAsAccount").toString();
			if(StringUtils.isNotBlank(isAsAccount) && isAsAccount.equals("1")) {
				createCell(dataRow, 2, accountDate, dataStyle);// "银行到账日期"
			}else {
				createCell(dataRow, 2, "", dataStyle);// "银行到账日期"
			}
			
			String groupCode = list.get(i).get("groupCode") == null ? "" : list
					.get(i).get("groupCode").toString();
			String productName = list.get(i).get("productName") == null ? ""
					: list.get(i).get("productName").toString();
			String orderType = list.get(i).get("orderType").toString();
			String productId = list.get(i).get("productId").toString();
			if(Context.ProductType.PRODUCT_AIR_TICKET.toString().equals(orderType)){
				productName = OrderCommonUtil.getProductName(productId, orderType);
			}
			createCell(dataRow, 3, groupCode, dataStyle);// "团号"
			createCell(dataRow, 4, productName, dataStyle);// "产品名称"
			String orderTypeName = DictUtils.getDictLabel(orderType,
					"order_type", "");
			createCell(dataRow, 5, orderTypeName, dataStyle);// "团队类型"
			String operatorId = list.get(i).get("operatorId") == null ? ""
					: list.get(i).get("operatorId").toString();
			String operator = "";
			if (operatorId != null
					&& org.apache.commons.lang.math.NumberUtils
							.isNumber(operatorId)) {
				operator = UserUtils
						.getUserNameById(Long.parseLong(operatorId));
			}
			createCell(dataRow, 6, operator, dataStyle);// "计调"
			String supplyName = list.get(i).get("supplyName") == null ? ""
					: list.get(i).get("supplyName").toString();
			createCell(dataRow, 7, supplyName, dataStyle);// "渠道商/地接社"
			String payerName = list.get(i).get("payerName") == null ? "" : list
					.get(i).get("payerName").toString();
			createCell(dataRow, 8, payerName, dataStyle);// "来款单位"
			String toBankNname = list.get(i).get("toBankNname") == null ? ""
					: list.get(i).get("toBankNname").toString();
			createCell(dataRow, 9, toBankNname, dataStyle);// "收款银行"
			String name = list.get(i).get("name") == null ? "" : list.get(i)
					.get("name").toString();
			createCell(dataRow, 10, name, dataStyle);// "款项"
			String currencyId = list.get(i).get("currencyId") == null ? ""
					: list.get(i).get("currencyId").toString();
			String currencyFlag = "";
			if (StringUtils.isNotBlank(currencyId)
					&& org.apache.commons.lang.math.NumberUtils
							.isNumber(currencyId)) {
				currencyFlag = CurrencyUtils.getCurrencyNameOrFlag(
						Long.parseLong(currencyId), "0");
			}
			String amount = list.get(i).get("amount") == null ? "" : list
					.get(i).get("amount").toString();
			if (!"".equals(amount)
					&& org.apache.commons.lang.math.NumberUtils
							.isDigits(amount)) {
				amount = MoneyNumberFormat.getThousandsByRegex(amount, 2);
			}
			
//			String tempMoney = currencyFlag + amount;
//			String tempMoney2 = "";
			String amount2 = "";
			if ("1".equals(isAsAccount)) {
//				tempMoney2 = tempMoney;
				amount2 = amount;
			}
			createCell(dataRow, 11, currencyFlag, dataStyle);// "款项"
			createCell2(dataRow, 12, amount, dataStyle2);// "款项"
			createCell(dataRow, 13, currencyFlag, dataStyle);// "款项"
			createCell2(dataRow, 14, amount2, dataStyle2);// "款项"
//			createMergedCell(dataRow, 10, tempMoney, tempMoney2, mergeStyle);// "已收金额/到账金额"
			String payType = list.get(i).get("payType") == null ? "" : list
					.get(i).get("payType").toString();
			createCell(dataRow, 15, DictUtils.getDictLabel(payType, "offlineorder_pay_type", ""), dataStyle);// "支付方式"
			String printFlag = list.get(i).get("printFlag") == null ? "" : list
					.get(i).get("printFlag").toString();
			if ("1".equals(printFlag)) {
				printFlag = "已打印";
			} else {
				printFlag = "未打印";
			}
			createCell(dataRow, 16, printFlag, dataStyle);// "打印状态"
		}
		return workBook;
	}


	/**
     * 创建单元格
     * @param row          row对象
     * @param index        列索引
     * @param value        单元格值
     * @param cellStyle    单元格样式
     * @author shijun.liu
     */
    private static void createCell(HSSFRow row, int index, Object value, CellStyle cellStyle){
    	if(null == value){
    		value = "";
    	}
    	HSSFCell cell = row.createCell(index);
    	cell.setCellValue(String.valueOf(value));
    	cell.setCellStyle(cellStyle);
    }
    
    /**
     * 创建单元格
     * @param row          row对象
     * @param index        列索引
     * @param value        单元格值
     * @param cellStyle    单元格样式
     * @author shijun.liu
     */
    private static void createCell2(HSSFRow row, int index, String value, CellStyle cellStyle){
    	if(null == value || "" == value.trim()){
    		HSSFCell cell = row.createCell(index);
        	cell.setCellValue("");
        	cell.setCellStyle(cellStyle);
    	}else{
    		HSSFCell cell = row.createCell(index);
    		cell.setCellValue(Double.valueOf(value.replaceAll(",", "")));
    		cell.setCellStyle(cellStyle);
    	}
    }
	    
	/**
	 * 获取成本录入详情页其他收入、预算、实际成本录入列表
	 * @author zzk
	 * @param model
	 * @param groupId
	 * @param typeId
	 */
	public void costInputList(Model model, Long groupId, Integer typeId) {
		List<Map<String, Object>> budgetInList = new ArrayList<Map<String, Object>>(); //境内预算
		List<Map<String, Object>> budgetOutList = new ArrayList<Map<String, Object>>(); //境外预算
		List<Map<String, Object>> actualInList = new ArrayList<Map<String, Object>>(); //境内实际
		List<Map<String, Object>> actualOutList = new ArrayList<Map<String, Object>>(); //境外实际
		List<Map<String, Object>> otherCostList = new ArrayList<Map<String, Object>>(); //其他收入

		//所有成本录入
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		allList = this.getCostRecordList(groupId, typeId);
		//对成本录入进行分组
		for (Map<String, Object> costRecord : allList) {
			if("0".equals(costRecord.get("budgetType").toString()) && "0".equals(costRecord.get("overseas").toString())) {
				budgetInList.add(costRecord);
			}else if("0".equals(costRecord.get("budgetType").toString())  && "1".equals(costRecord.get("overseas").toString())) {
				budgetOutList.add(costRecord);
			}else if("1".equals(costRecord.get("budgetType").toString()) && "0".equals(costRecord.get("overseas").toString())) {
				actualInList.add(costRecord);
			}else if("1".equals(costRecord.get("budgetType").toString())  && "1".equals(costRecord.get("overseas").toString())) {
				actualOutList.add(costRecord);
			}else if("2".equals(costRecord.get("budgetType").toString()) && "0".equals(costRecord.get("overseas").toString())) {
				otherCostList.add(costRecord);
			}
		}

		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())) {
			budgetInList = this.getBudgetInListNew(budgetInList, groupId, typeId);
		}
		model.addAttribute("budgetInList", budgetInList);	//境内预算
		model.addAttribute("budgetOutList", budgetOutList);	//境外预算
		model.addAttribute("actualInList", actualInList);	//境内实际
		model.addAttribute("actualOutList", actualOutList);	//境外实际

		// 其他收入已收金额、达帐金额
		DecimalFormat df = new DecimalFormat("#####0.00");
		for (int i = 0; i < otherCostList.size(); i++) {
			Object obj1 = this.getPayedMoney(Long.parseLong(otherCostList.get(i).get("id").toString()));
			String s1 = df.format(Double.parseDouble(obj1==null?"0":obj1.toString()));
			otherCostList.get(i).put("payedMoney", s1);
			Object obj2 = this.getConfirmMoney(Long.parseLong(otherCostList.get(i).get("id").toString()));
			String s2 = df.format(Double.parseDouble(obj2==null?"0":obj2.toString()));
			otherCostList.get(i).put("confirmMoney", s2);
		}
		model.addAttribute("otherCostList", otherCostList);

		List<Map<String, Object>> incomeList=this.getRefunifoForCastList(groupId,typeId);	//总收入
		List<Map<String, Object>> budgetCost=this.getCost(groupId,typeId,0);	//预计总成本
		List<Map<String, Object>> actualCost=this.getCost(groupId,typeId,1);	//实际总成本
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);

		Object budgetRefund = this.getRefundSum(groupId, 0, typeId).get(0).get("totalRefund");
		Object budgetrefund = budgetRefund == null ? "0" : budgetRefund;
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualRefund = this.getRefundSum(groupId, 1, typeId).get(0).get("totalRefund");
		Object actualrefund = actualRefund == null ? "0" : actualRefund;
		model.addAttribute("actualrefund", actualrefund);
	}
	
	public List<Map<String, String>> getBankList(){
		StringBuilder sbl=new StringBuilder();
		sbl.append("select bank.bankName, bank.bankAccountCode from plat_bank_info bank, agentinfo agent  ");
		sbl.append("where bank.beLongPlatId = agent.id and bank.platType = 2 and bank.delFlag = 0 and agent.agentName = '拉美途' ");
		List<Map<String, String>> bankList = costRecordDao.findBySql(sbl.toString(),Map.class);
		return bankList;
	}
	
	/**
	 * 获得团期下的团队退款的和(团期类)
	 * @param productId
	 * @param orderType
	 * @return
	 */
	public BigDecimal getGroupRefundSum(Long productId, Integer orderType){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ")
			.append("SUM(IFNULL(groupRefundSum, 0)) groupRefundSum ")
			.append("FROM ")
			.append("( ")
			.append("SELECT ")
			.append("sum( ")
			.append("	CASE ")
			.append("	WHEN trv.id IS NULL ")
			.append("	OR trv.id = '' THEN ")
			.append("		cr.price ")
			.append("	END ")
			.append(") groupRefundSum ")
			.append("FROM ")
		    .append("	productorder o ")
		    .append("LEFT JOIN review_new rn ON o.id = rn.order_id ")
		    .append("LEFT JOIN cost_record cr ON cr.reviewUuid = rn.id ")
		    .append("LEFT JOIN traveler trv ON trv.id = rn.traveller_id ")
		    .append("WHERE ")
		    .append("	o.delFlag = '0' ")
		    .append("AND o.payStatus NOT IN (7, 99, 111) ")
		    .append("AND o.orderStatus = ").append(orderType).append(" ")
		    .append("AND o.productGroupId = ").append(productId).append(" ")	
		    .append("AND rn.`status`= 2 ")
		    .append("AND	cr.reviewType = 1 ")
		    .append("AND cr.delFlag = '0' ")
		    .append("AND cr.orderType = ").append(orderType).append(" ")
		    .append("AND cr.budgetType = 0 ")
		    .append("GROUP BY ")
		    .append("	o.id ")
		    .append(") t1 ");
		List<Map<String,Object>> list = costRecordDao.findBySql(sbf.toString(), Map.class);
		BigDecimal big = new BigDecimal("0.00");
		if(list.size() > 0){
			if(list.get(0).get("groupRefundSum")!=null){
				big = new BigDecimal(list.get(0).get("groupRefundSum").toString());
			}
		}
		return big;
	}
}
