/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.modules.order.service.OrderContactsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerTypeRelationDao;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.service.HotelControlService;
import com.trekiz.admin.modules.hotel.service.HotelOrderPriceService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.island.dao.ActivityIslandDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupPriceDao;
import com.trekiz.admin.modules.island.dao.IslandMoneyAmountDao;
import com.trekiz.admin.modules.island.dao.IslandOrderControlDetailDao;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.dao.IslandOrderPriceDao;
import com.trekiz.admin.modules.island.dao.IslandTravelerDao;
import com.trekiz.admin.modules.island.dao.IslandTravelerPapersTypeDao;
import com.trekiz.admin.modules.island.dao.IslandTravelervisaDao;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandOrderControlDetail;
import com.trekiz.admin.modules.island.entity.IslandOrderPrice;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.entity.IslandTravelerPapersType;
import com.trekiz.admin.modules.island.entity.IslandTravelervisa;
import com.trekiz.admin.modules.island.input.IslandOrderInput;
import com.trekiz.admin.modules.island.query.IslandOrderControlDetailQuery;
import com.trekiz.admin.modules.island.query.IslandOrderQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupMealRiseService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupMealService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupPriceService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupRoomService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderControlDetailService;
import com.trekiz.admin.modules.island.service.IslandOrderPriceService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.pay.dao.PayIslandOrderDao;
import com.trekiz.admin.modules.pay.entity.PayIslandOrder;
import com.trekiz.admin.modules.pay.service.PayIslandOrderService;
import com.trekiz.admin.modules.pay.service.PayProductOrderService;
import com.trekiz.admin.modules.pay.service.ProductMoneyAmountService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.AirlineInfoDao;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;
import com.trekiz.admin.modules.traveler.service.TravelerTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class IslandOrderServiceImpl  extends BaseService implements IslandOrderService {
	@Autowired
    private SystemService systemService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private PayIslandOrderService payIslandOrderService;
	@Autowired
	private IslandOrderDao islandOrderDao;
	@Autowired
	private IslandOrderPriceDao islandOrderPriceDao;
	@Autowired
	private AirlineInfoDao airlineInfoDao;
	@Autowired
	private IslandTravelerDao islandTravelerDao;
	@Autowired
	private IslandMoneyAmountDao islandMoneyAmountDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private IslandTravelervisaDao islandTravelervisaDao;
	@Autowired
	private IslandTravelerPapersTypeDao islandTravelerPapersTypeDao;
	@Autowired
	private HotelAnnexDao hotelAnnexDao;
	@Autowired
	private OrderContactsDao orderContactsDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private ActivityIslandGroupDao activityIslandGroupDao;
	@Autowired
	private ActivityIslandDao activityIslandDao;
	@Autowired
	private PayIslandOrderDao payIslandOrderDao;
	@Autowired
	private ActivityIslandGroupPriceService activityIslandGroupPriceService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityIslandGroupMealService activityIslandGroupMealService;
	@Autowired
	private ActivityIslandGroupMealRiseService activityIslandGroupMealRiseService;
	@Autowired
	private ActivityIslandGroupRoomService activityIslandGroupRoomService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private PayProductOrderService payProductOrderService;
	@Autowired
	private ProductMoneyAmountService productMoneyAmountService;
	@Autowired
    private ReviewService reviewService;
	@Autowired
	private TravelerTypeService travelerTypeService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private IslandOrderPriceService islandOrderPriceService;
	@Autowired
	private HotelStarService hotelStarService;
	@Autowired
	private HotelOrderPriceService hotelOrderPriceService;
	@Autowired
	private ActivityIslandGroupPriceDao activityIslandGroupPriceDao;
	@Autowired
	private HotelControlService hotelControlService;
	@Autowired
	private IslandOrderControlDetailService islandOrderControlDetailService;
	@Autowired
	private IslandOrderControlDetailDao islandOrderControlDetailDao;
	@Autowired
	private HotelTravelerTypeRelationDao hotelTravelerTypeRelationDao;
	@Autowired
	private OrderContactsService orderContactsService;
	//List<OrderContacts> orderContactsList = new ArrayList<OrderContacts>();

	public void save (IslandOrder islandOrder){
		super.setOptInfo(islandOrder, BaseService.OPERATION_ADD);
		islandOrderDao.saveObj(islandOrder);
	}
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void save (IslandOrderInput islandOrderInput) throws Exception{
		
		IslandOrder islandOrder = islandOrderInput.getIslandOrder();
		super.setOptInfo(islandOrder, BaseService.OPERATION_ADD);
		
		
		islandOrderDao.saveObj(islandOrder);
	}
	
	public void update (IslandOrder islandOrder){
		super.setOptInfo(islandOrder, BaseService.OPERATION_UPDATE);
		islandOrderDao.updateObj(islandOrder);
	}
	
	public IslandOrder getById(java.lang.Integer value) {
		return islandOrderDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		IslandOrder obj = islandOrderDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<IslandOrder> find(Page<IslandOrder> page, IslandOrderQuery islandOrderQuery) {
		DetachedCriteria dc = islandOrderDao.createDetachedCriteria();
		
	   	if(islandOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandOrderQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", islandOrderQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", islandOrderQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOrderNum())){
			dc.add(Restrictions.eq("orderNum", islandOrderQuery.getOrderNum()));
		}
	   	if(islandOrderQuery.getOrderStatus()!=null){
	   		dc.add(Restrictions.eq("orderStatus", islandOrderQuery.getOrderStatus()));
	   	}
	   	if(islandOrderQuery.getOrderCompany()!=null){
	   		dc.add(Restrictions.eq("orderCompany", islandOrderQuery.getOrderCompany()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOrderCompanyName())){
			dc.add(Restrictions.eq("orderCompanyName", islandOrderQuery.getOrderCompanyName()));
		}
	   	if(islandOrderQuery.getOrderSalerId()!=null){
	   		dc.add(Restrictions.eq("orderSalerId", islandOrderQuery.getOrderSalerId()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOrderPersonName())){
			dc.add(Restrictions.eq("orderPersonName", islandOrderQuery.getOrderPersonName()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOrderPersonPhoneNum())){
			dc.add(Restrictions.eq("orderPersonPhoneNum", islandOrderQuery.getOrderPersonPhoneNum()));
		}
		if(islandOrderQuery.getOrderTime()!=null){
			dc.add(Restrictions.eq("orderTime", islandOrderQuery.getOrderTime()));
		}
	   	if(islandOrderQuery.getOrderPersonNum()!=null){
	   		dc.add(Restrictions.eq("orderPersonNum", islandOrderQuery.getOrderPersonNum()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getFrontMoney())){
			dc.add(Restrictions.eq("frontMoney", islandOrderQuery.getFrontMoney()));
		}
	   	if(islandOrderQuery.getPayStatus()!=null){
	   		dc.add(Restrictions.eq("payStatus", islandOrderQuery.getPayStatus()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getPayedMoney())){
			dc.add(Restrictions.eq("payedMoney", islandOrderQuery.getPayedMoney()));
		}
	   	if(islandOrderQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", islandOrderQuery.getPayType()));
	   	}
	   	if(islandOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandOrderQuery.getCreateBy()));
	   	}
		if(islandOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandOrderQuery.getCreateDate()));
		}
	   	if(islandOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandOrderQuery.getUpdateBy()));
	   	}
		if(islandOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandOrderQuery.getDelFlag()));
		}
	   	if(islandOrderQuery.getChangeGroupId()!=null){
	   		dc.add(Restrictions.eq("changeGroupId", islandOrderQuery.getChangeGroupId()));
	   	}
	   	if(islandOrderQuery.getGroupChangeType()!=null){
	   		dc.add(Restrictions.eq("groupChangeType", islandOrderQuery.getGroupChangeType()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getCostMoney())){
			dc.add(Restrictions.eq("costMoney", islandOrderQuery.getCostMoney()));
		}
	   	if(islandOrderQuery.getAsAcountType()!=null){
	   		dc.add(Restrictions.eq("asAcountType", islandOrderQuery.getAsAcountType()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getAccountedMoney())){
			dc.add(Restrictions.eq("accountedMoney", islandOrderQuery.getAccountedMoney()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getPayDeposit())){
			dc.add(Restrictions.eq("payDeposit", islandOrderQuery.getPayDeposit()));
		}
	   	if(islandOrderQuery.getPlaceHolderType()!=null){
	   		dc.add(Restrictions.eq("placeHolderType", islandOrderQuery.getPlaceHolderType()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getSingleDiff())){
			dc.add(Restrictions.eq("singleDiff", islandOrderQuery.getSingleDiff()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getCancelDescription())){
			dc.add(Restrictions.eq("cancelDescription", islandOrderQuery.getCancelDescription()));
		}
	   	if(islandOrderQuery.getIsPayment()!=null){
	   		dc.add(Restrictions.eq("isPayment", islandOrderQuery.getIsPayment()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getPayMode())){
			dc.add(Restrictions.eq("payMode", islandOrderQuery.getPayMode()));
		}
	   	if(islandOrderQuery.getRemainDays()!=null){
	   		dc.add(Restrictions.eq("remainDays", islandOrderQuery.getRemainDays()));
	   	}
		if(islandOrderQuery.getActivationDate()!=null){
			dc.add(Restrictions.eq("activationDate", islandOrderQuery.getActivationDate()));
		}
	   	if(islandOrderQuery.getLockStatus()!=null){
	   		dc.add(Restrictions.eq("lockStatus", islandOrderQuery.getLockStatus()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getSpecialDemand())){
			dc.add(Restrictions.eq("specialDemand", islandOrderQuery.getSpecialDemand()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getTotalMoney())){
			dc.add(Restrictions.eq("totalMoney", islandOrderQuery.getTotalMoney()));
		}
	   	if(StringUtils.isNotEmpty(islandOrderQuery.getFileIds())){
	   		dc.add(Restrictions.eq("fileIds", islandOrderQuery.getFileIds()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOriginalTotalMoney())){
			dc.add(Restrictions.eq("originalTotalMoney", islandOrderQuery.getOriginalTotalMoney()));
		}
	   	if(islandOrderQuery.getIsAfterSupplement()!=null){
	   		dc.add(Restrictions.eq("isAfterSupplement", islandOrderQuery.getIsAfterSupplement()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOriginalFrontMoney())){
			dc.add(Restrictions.eq("originalFrontMoney", islandOrderQuery.getOriginalFrontMoney()));
		}
	   	if(islandOrderQuery.getPaymentType()!=null){
	   		dc.add(Restrictions.eq("paymentType", islandOrderQuery.getPaymentType()));
	   	}
	   	if(islandOrderQuery.getForecaseReportRoomNum() != null) {
	   		dc.add(Restrictions.eq("forecaseReportRoomNum", islandOrderQuery.getForecaseReportRoomNum()));
	   	}
	   	if(islandOrderQuery.getForecaseReportTicketNum() != null) {
	   		dc.add(Restrictions.eq("forecaseReportTicketNum", islandOrderQuery.getForecaseReportTicketNum()));
	   	}
	   	if(islandOrderQuery.getSubControlNum()!=null){
	   		dc.add(Restrictions.eq("subControlNum", islandOrderQuery.getSubControlNum()));
	   	}
	   	if(islandOrderQuery.getSubUnControlNum()!=null){
	   		dc.add(Restrictions.eq("subUnControlNum", islandOrderQuery.getSubUnControlNum()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getRemark())){
			dc.add(Restrictions.eq("remark", islandOrderQuery.getRemark()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getBookingStatus())){
			dc.add(Restrictions.eq("bookingStatus", islandOrderQuery.getBookingStatus()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandOrderDao.find(page, dc);
	}
	
	public List<IslandOrder> find( IslandOrderQuery islandOrderQuery) {
		DetachedCriteria dc = islandOrderDao.createDetachedCriteria();
		
	   	if(islandOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandOrderQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", islandOrderQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getActivityIslandGroupUuid())){
			dc.add(Restrictions.eq("activityIslandGroupUuid", islandOrderQuery.getActivityIslandGroupUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOrderNum())){
			dc.add(Restrictions.eq("orderNum", islandOrderQuery.getOrderNum()));
		}
	   	if(islandOrderQuery.getOrderStatus()!=null){
	   		dc.add(Restrictions.eq("orderStatus", islandOrderQuery.getOrderStatus()));
	   	}
	   	if(islandOrderQuery.getOrderCompany()!=null){
	   		dc.add(Restrictions.eq("orderCompany", islandOrderQuery.getOrderCompany()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOrderCompanyName())){
			dc.add(Restrictions.eq("orderCompanyName", islandOrderQuery.getOrderCompanyName()));
		}
	   	if(islandOrderQuery.getOrderSalerId()!=null){
	   		dc.add(Restrictions.eq("orderSalerId", islandOrderQuery.getOrderSalerId()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOrderPersonName())){
			dc.add(Restrictions.eq("orderPersonName", islandOrderQuery.getOrderPersonName()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOrderPersonPhoneNum())){
			dc.add(Restrictions.eq("orderPersonPhoneNum", islandOrderQuery.getOrderPersonPhoneNum()));
		}
		if(islandOrderQuery.getOrderTime()!=null){
			dc.add(Restrictions.eq("orderTime", islandOrderQuery.getOrderTime()));
		}
	   	if(islandOrderQuery.getOrderPersonNum()!=null){
	   		dc.add(Restrictions.eq("orderPersonNum", islandOrderQuery.getOrderPersonNum()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getFrontMoney())){
			dc.add(Restrictions.eq("frontMoney", islandOrderQuery.getFrontMoney()));
		}
	   	if(islandOrderQuery.getPayStatus()!=null){
	   		dc.add(Restrictions.eq("payStatus", islandOrderQuery.getPayStatus()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getPayedMoney())){
			dc.add(Restrictions.eq("payedMoney", islandOrderQuery.getPayedMoney()));
		}
	   	if(islandOrderQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", islandOrderQuery.getPayType()));
	   	}
	   	if(islandOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandOrderQuery.getCreateBy()));
	   	}
		if(islandOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandOrderQuery.getCreateDate()));
		}
	   	if(islandOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandOrderQuery.getUpdateBy()));
	   	}
		if(islandOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandOrderQuery.getDelFlag()));
		}
	   	if(islandOrderQuery.getChangeGroupId()!=null){
	   		dc.add(Restrictions.eq("changeGroupId", islandOrderQuery.getChangeGroupId()));
	   	}
	   	if(islandOrderQuery.getGroupChangeType()!=null){
	   		dc.add(Restrictions.eq("groupChangeType", islandOrderQuery.getGroupChangeType()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getCostMoney())){
			dc.add(Restrictions.eq("costMoney", islandOrderQuery.getCostMoney()));
		}
	   	if(islandOrderQuery.getAsAcountType()!=null){
	   		dc.add(Restrictions.eq("asAcountType", islandOrderQuery.getAsAcountType()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getAccountedMoney())){
			dc.add(Restrictions.eq("accountedMoney", islandOrderQuery.getAccountedMoney()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getPayDeposit())){
			dc.add(Restrictions.eq("payDeposit", islandOrderQuery.getPayDeposit()));
		}
	   	if(islandOrderQuery.getPlaceHolderType()!=null){
	   		dc.add(Restrictions.eq("placeHolderType", islandOrderQuery.getPlaceHolderType()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getSingleDiff())){
			dc.add(Restrictions.eq("singleDiff", islandOrderQuery.getSingleDiff()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getCancelDescription())){
			dc.add(Restrictions.eq("cancelDescription", islandOrderQuery.getCancelDescription()));
		}
	   	if(islandOrderQuery.getIsPayment()!=null){
	   		dc.add(Restrictions.eq("isPayment", islandOrderQuery.getIsPayment()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getPayMode())){
			dc.add(Restrictions.eq("payMode", islandOrderQuery.getPayMode()));
		}
	   	if(islandOrderQuery.getRemainDays()!=null){
	   		dc.add(Restrictions.eq("remainDays", islandOrderQuery.getRemainDays()));
	   	}
		if(islandOrderQuery.getActivationDate()!=null){
			dc.add(Restrictions.eq("activationDate", islandOrderQuery.getActivationDate()));
		}
	   	if(islandOrderQuery.getLockStatus()!=null){
	   		dc.add(Restrictions.eq("lockStatus", islandOrderQuery.getLockStatus()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getSpecialDemand())){
			dc.add(Restrictions.eq("specialDemand", islandOrderQuery.getSpecialDemand()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getTotalMoney())){
			dc.add(Restrictions.eq("totalMoney", islandOrderQuery.getTotalMoney()));
		}
		if(StringUtils.isNotEmpty(islandOrderQuery.getFileIds())){
	   		dc.add(Restrictions.eq("fileIds", islandOrderQuery.getFileIds()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOriginalTotalMoney())){
			dc.add(Restrictions.eq("originalTotalMoney", islandOrderQuery.getOriginalTotalMoney()));
		}
	   	if(islandOrderQuery.getIsAfterSupplement()!=null){
	   		dc.add(Restrictions.eq("isAfterSupplement", islandOrderQuery.getIsAfterSupplement()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getOriginalFrontMoney())){
			dc.add(Restrictions.eq("originalFrontMoney", islandOrderQuery.getOriginalFrontMoney()));
		}
	   	if(islandOrderQuery.getPaymentType()!=null){
	   		dc.add(Restrictions.eq("paymentType", islandOrderQuery.getPaymentType()));
	   	}
	   	if(islandOrderQuery.getForecaseReportRoomNum() != null) {
	   		dc.add(Restrictions.eq("forecaseReportRoomNum", islandOrderQuery.getForecaseReportRoomNum()));
	   	}
	   	if(islandOrderQuery.getForecaseReportTicketNum() != null) {
	   		dc.add(Restrictions.eq("forecaseReportTicketNum", islandOrderQuery.getForecaseReportTicketNum()));
	   	}
	   	if(islandOrderQuery.getSubControlNum()!=null){
	   		dc.add(Restrictions.eq("subControlNum", islandOrderQuery.getSubControlNum()));
	   	}
	   	if(islandOrderQuery.getSubUnControlNum()!=null){
	   		dc.add(Restrictions.eq("subUnControlNum", islandOrderQuery.getSubUnControlNum()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderQuery.getRemark())){
			dc.add(Restrictions.eq("remark", islandOrderQuery.getRemark()));
		}
		if (StringUtils.isNotEmpty(islandOrderQuery.getBookingStatus())){
			dc.add(Restrictions.eq("bookingStatus", islandOrderQuery.getBookingStatus()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandOrderDao.find(dc);
	}
	
	public IslandOrder getByUuid(String uuid) {
		return islandOrderDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		IslandOrder islandOrder = getByUuid(uuid);
		islandOrder.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(islandOrder);
	}
	
	public void initIslandOrderPageData(String activityIslandGroupUuid, Model model) {
		
		//加载海岛游团期产品信息
		ActivityIslandGroup activityIslandGroup = activityIslandGroupDao.getByUuid(activityIslandGroupUuid);
		if(activityIslandGroup == null) {
			return ;
		}
		//加载海岛游产品信息
		ActivityIsland activityIsland = activityIslandDao.getByUuid(activityIslandGroup.getActivityIslandUuid());

		//加载海岛游产品团期价格表
		List<ActivityIslandGroupPrice> groupPrices = activityIslandGroupPriceService.getPriceListByGroupUuid(activityIslandGroupUuid,activityIsland.getHotelUuid());
		
		//加载渠道信息
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();// 渠道信息列表
		//签证类型
		List<SysDict> visaTypes = sysDictService.findByType(SysDict.TYPE_NEW_VISA_TYPE);
		
		//加载团期下所有的参考航班数据
		List<ActivityIslandGroupAirline> groupAirlines = activityIslandGroupAirlineService.getByactivityIslandGroup(activityIslandGroupUuid);
		//初始化团期余位数
		activityIslandGroup.setRemNumber(activityIslandGroupService.getRemNumberByGroupAirlineList(groupAirlines));
		
		//加载舱位等级数据集(去重后的数据)
		List<ActivityIslandGroupAirline> groupAirlineSpaceLevels = new ArrayList<ActivityIslandGroupAirline>();
		//舱位等级去重操作
		List<String> spaceLevelKeys = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(groupAirlines)) {
			for(ActivityIslandGroupAirline groupAirline : groupAirlines) {
				if(!spaceLevelKeys.contains(groupAirline.getSpaceLevel())) {
					groupAirlineSpaceLevels.add(groupAirline);
					
					spaceLevelKeys.add(groupAirline.getSpaceLevel());
				}
			}
		}
		
		//团期游客信息集合
		List<TravelerType> groupTravelerTypes = hotelTravelerTypeRelationDao.getTravelerTypesByHotelUuid(activityIsland.getHotelUuid());
		List<TravelerType> travelerTypes = new ArrayList<TravelerType>();
		
		//获取所有的存在价格的游客uuid
		List<String> travelerTypeUuids = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(groupPrices)) {
			for(ActivityIslandGroupPrice groupPrice : groupPrices) {
				if((groupPrice.getPrice() != null) && (!travelerTypeUuids.contains(groupPrice.getType()))) {
					travelerTypeUuids.add(groupPrice.getType());
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(groupTravelerTypes)) {
			for(TravelerType travelerType : groupTravelerTypes) {
				if(travelerTypeUuids.contains(travelerType.getUuid())) {
					travelerTypes.add(travelerType);
				}
			}
		}
		
		//加载团期下所有的房型数据
		List<ActivityIslandGroupRoom> groupRooms = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityIslandGroupUuid);
		
		//读取公司配置币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("agentList", agentInfos);
		model.addAttribute("groupPrices", groupPrices);
		model.addAttribute("activityIslandGroup", activityIslandGroup);
		model.addAttribute("activityIsland", activityIsland);
		model.addAttribute("visaTypes", visaTypes);
		if(groupAirlines != null && groupAirlines.size() > 0) {
			//加载所有余位数
			model.addAttribute("groupAirline", groupAirlines.get(0));
		}
		model.addAttribute("groupRooms", groupRooms);
		model.addAttribute("hotelStar", hotelService.getHotelStarValByHotelUuid(activityIsland.getHotelUuid()));
		model.addAttribute("groupAirlineSpaceLevels", groupAirlineSpaceLevels);
		model.addAttribute("travelerTypes", travelerTypes);
		model.addAttribute("productCreateBy", UserUtils.getUserNameById(activityIsland.getCreateBy()+0L));
		model.addAttribute("loginUserName", UserUtils.getUser().getName());
		
		model.addAttribute("bookingPersonNum", this.getBookingPersonNum(activityIslandGroup.getUuid()));
	}
	
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public Map<String, String> saveIslandOrder(IslandOrderInput islandOrderInput) throws Exception {
		//重建前端传递数据
		rebuildInputData(islandOrderInput);
		
		Map<String, String> datas = new HashMap<String, String>();
		//联系人信息集合
		List<OrderContacts> orderContactsList = new ArrayList<OrderContacts>();
		//游客类型费用、费用调整
		List<IslandOrderPrice> islandOrderPriceList = new ArrayList<IslandOrderPrice>();
		//游客信息 集合
	  	List<IslandTraveler> islandTravelerList = new ArrayList<IslandTraveler>();
	    //订单的金额信息，订单总额、结算总额、应收、已收
	  	List<IslandMoneyAmount> islandMoneyAmountList = new ArrayList<IslandMoneyAmount>();
	  	
	  	//签证信息
		List<IslandTravelervisa> islandTravelervisaList = new ArrayList<IslandTravelervisa>();
		//附件信息
		List<HotelAnnex> islandTravelerFilesList = new ArrayList<HotelAnnex>();
		//游客的证件信息
		List<IslandTravelerPapersType> islandTravelerPapersTypeList = new ArrayList<IslandTravelerPapersType>();
		IslandOrder islandOrder = null;
		
		try{
			islandOrder = islandOrderInput.getIslandOrder();
			islandOrder.setOrderNum(com.trekiz.admin.common.utils.DateUtils.date2String(new Date(), "yyyyMMddHHmmss"));
			islandOrder.setOrderStatus(IslandOrder.ORDER_STATUS_TO_CONFIRM);
			islandOrder.setOrderSalerId(UserUtils.getUser().getId().intValue());
			islandOrder.setOrderPersonName(UserUtils.getUser().getName());
			islandOrder.setOrderTime(new Date());
			islandOrder.setCostMoney(UuidUtils.generUuid());
			islandOrder.setLockStatus(IslandOrder.LOCK_STATUS_NORMAL);
			islandOrder.setTotalMoney(UuidUtils.generUuid());
			islandOrder.setOriginalTotalMoney(UuidUtils.generUuid());
			islandOrder.setIsAfterSupplement(false);
			islandOrder.setPaymentType(Context.PAYMENT_TYPE_JS);
			if(islandOrder.getOrderCompany() == null) {
				islandOrder.setOrderCompany(-1);
			}
			
			//保存海岛游订单以及子订单数据
			this.save(islandOrder);
			
			orderContactsList = islandOrder.getOrderContactsList();
			islandOrderPriceList = islandOrder.getIslandOrderPriceList();
			islandTravelerList = islandOrder.getIslandTravelerList();
			islandMoneyAmountList = islandOrder.getIslandMoneyAmountList();
			
			//初始化联系人表数据
			if(CollectionUtils.isNotEmpty(orderContactsList)) {
				for(OrderContacts orderContacts : orderContactsList) {
					orderContacts.setOrderId(islandOrder.getId().longValue());
					orderContacts.setOrderType(Context.ORDER_TYPE_ISLAND);
					orderContacts.setAgentId(islandOrder.getOrderCompany().longValue());
				}
			}
			
			//初始化订单价格表数据
			if(CollectionUtils.isNotEmpty(islandOrderPriceList)) {
				for(IslandOrderPrice islandOrderPrice : islandOrderPriceList) {
					islandOrderPrice.setOrderUuid(islandOrder.getUuid());
					super.setOptInfo(islandOrderPrice, BaseService.OPERATION_ADD);
				}
			}
			
			//初始化海岛moneyAmount数据
			if(CollectionUtils.isNotEmpty(islandMoneyAmountList)) {
				for(IslandMoneyAmount islandMoneyAmount : islandMoneyAmountList) {
					islandMoneyAmount.setBusinessUuid(islandOrder.getUuid());
					islandMoneyAmount.setBusinessType(IslandMoneyAmount.BUSINESS_TYPE_ORDER);
					BigDecimal convertLowest = currencyDao.findConvertLowestById(islandMoneyAmount.getCurrencyId().longValue());
					if(convertLowest != null) {
						islandMoneyAmount.setExchangerate(convertLowest.doubleValue());
					} else {
						BigDecimal exchangerage = currencyDao.findExchangerageById(islandMoneyAmount.getCurrencyId().longValue());
						if (exchangerage != null) {
							islandMoneyAmount.setExchangerate(exchangerage.doubleValue());
						}
					}
					
					//设置订单流水号：
					if(islandMoneyAmount.getMoneyType() == Context.MONEY_TYPE_CBJ.intValue()) {
						islandMoneyAmount.setSerialNum(islandOrder.getCostMoney());
					} else if(islandMoneyAmount.getMoneyType() == Context.MONEY_TYPE_YSH.intValue()) {
						islandMoneyAmount.setSerialNum(islandOrder.getTotalMoney());
					} else if(islandMoneyAmount.getMoneyType() == Context.MONEY_TYPE_YSYSH.intValue()) {
						islandMoneyAmount.setSerialNum(islandOrder.getOriginalTotalMoney());
					}
					
					super.setOptInfo(islandMoneyAmount, BaseService.OPERATION_ADD);
				}
				
				//多币种的累加操作（将同样币种金额进行相加）
				countMoney(islandMoneyAmountList);
			}
			
			//初始化游客表数据
			if(CollectionUtils.isNotEmpty(islandTravelerList)) {
				for(IslandTraveler islandTraveler : islandTravelerList) {
					
					islandTraveler.setOrderUuid(islandOrder.getUuid());
					//根据团期信息获取唯一团期价格信息
					ActivityIslandGroupPrice groupPrice = activityIslandGroupPriceDao.getGroupPriceByGroupInfo(islandOrder.getActivityIslandGroupUuid(), islandTraveler.getPersonType(), islandTraveler.getSpaceLevel());
					if(groupPrice != null) {
						islandTraveler.setSrcPriceCurrency(groupPrice.getCurrencyId());
						islandTraveler.setSrcPrice(groupPrice.getPrice());
					}
					//游客原始应收价
					islandTraveler.setOriginalPayPriceSerialNum(UuidUtils.generUuid());
					//游客成本价
					islandTraveler.setCostPriceSerialNum(UuidUtils.generUuid());
					//游客结算价
					islandTraveler.setPayPriceSerialNum(UuidUtils.generUuid());
					//将游客设置为正常状态
					islandTraveler.setStatus("0");
					super.setOptInfo(islandTraveler, BaseService.OPERATION_ADD);
					
					//加载游客表子表数据(签证信息)
					if(CollectionUtils.isNotEmpty(islandTraveler.getIslandTravelervisaList())) {
						for(IslandTravelervisa islandTravelervisa : islandTraveler.getIslandTravelervisaList()) {
							islandTravelervisa.setIslandOrderUuid(islandOrder.getUuid());
							islandTravelervisa.setIslandTravelerUuid(islandTraveler.getUuid());
							super.setOptInfo(islandTravelervisa, BaseService.OPERATION_ADD);
						}
						//添加游客子表数据(签证信息)
						islandTravelervisaList.addAll(islandTraveler.getIslandTravelervisaList());
					}
					
					//加载游客表子表数据(游客金额信息)
					if(CollectionUtils.isNotEmpty(islandTraveler.getIslandMoneyAmountList())) {
						for(int i=0; i<3; i++) {
							//存储了三次金额数据,每次金额数据做一次币种合并累加
							List<IslandMoneyAmount> islandMoneyAmountBaks = new ArrayList<IslandMoneyAmount>();
							for(IslandMoneyAmount islandMoneyAmount : islandTraveler.getIslandMoneyAmountList()) {
								IslandMoneyAmount islandMoneyAmountBak = new IslandMoneyAmount();
								islandMoneyAmountBak.setAmount(islandMoneyAmount.getAmount());
								islandMoneyAmountBak.setCurrencyId(islandMoneyAmount.getCurrencyId());
								
								islandMoneyAmountBak.setBusinessUuid(islandTraveler.getUuid());
								islandMoneyAmountBak.setBusinessType(IslandMoneyAmount.BUSINESS_TYPE_TRAVELER);
								
								//保存金额类型：原始结算价、结算价、成本价
								if (i == 0) {
									islandMoneyAmountBak.setMoneyType(Context.MONEY_TYPE_YSJSJ);
								} else if (i == 1) {
									islandMoneyAmountBak.setMoneyType(Context.MONEY_TYPE_JSJ);
								} else {
									islandMoneyAmountBak.setMoneyType(Context.MONEY_TYPE_CBJ);
								}
								
								//设置游客金额表金额流水号
								String serialNum = "";
								if(i == 0) {
									serialNum = islandTraveler.getOriginalPayPriceSerialNum();
								} else if(i == 1) {
									serialNum = islandTraveler.getPayPriceSerialNum();
								} else if(i == 2) {
									serialNum = islandTraveler.getCostPriceSerialNum();
								}
								islandMoneyAmountBak.setSerialNum(serialNum);
								BigDecimal exchangerage = currencyDao.findConvertLowestById(islandMoneyAmountBak.getCurrencyId().longValue());
								if(exchangerage != null) {
									islandMoneyAmountBak.setExchangerate(exchangerage.doubleValue());
								}
								super.setOptInfo(islandMoneyAmountBak, BaseService.OPERATION_ADD);
								
								islandMoneyAmountBaks.add(islandMoneyAmountBak);
							}
							
							//多币种的累加操作（将同样币种金额进行相加）
							countMoney(islandMoneyAmountBaks);
							
							//添加游客子表数据(游客金额信息)
							islandMoneyAmountList.addAll(islandMoneyAmountBaks);
						}
					}
					
					//加载游客表子表数据(游客的证件信息)
					if(CollectionUtils.isNotEmpty(islandTraveler.getIslandTravelerPapersTypeList())) {
						for(IslandTravelerPapersType islandTravelerPapersType : islandTraveler.getIslandTravelerPapersTypeList()) {
							islandTravelerPapersType.setIslandTravelerUuid(islandTraveler.getUuid());
							islandTravelerPapersType.setOrderUuid(islandOrder.getUuid());
							super.setOptInfo(islandTravelerPapersType, BaseService.OPERATION_ADD);
						}
						//添加游客子表数据(游客的证件信息)
						islandTravelerPapersTypeList.addAll(islandTraveler.getIslandTravelerPapersTypeList());
					}
					
					//加载游客表子表数据(上传文件信息)
					if(CollectionUtils.isNotEmpty(islandTraveler.getIslandTravelerFilesList())) {
						for(HotelAnnex hotelAnnex : islandTraveler.getIslandTravelerFilesList()) {
							hotelAnnex.setType(HotelAnnex.ANNEX_TYPE_FOR_ISLAND_TRAVELER);
							hotelAnnex.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
							hotelAnnex.setMainUuid(islandTraveler.getUuid());
							super.setOptInfo(hotelAnnex, BaseService.OPERATION_ADD);
						}
						
						islandTravelerFilesList.addAll(islandTraveler.getIslandTravelerFilesList());
					}
				}
			}
			
			//批量保存数据
			orderContactsDao.batchSave(orderContactsList);
			islandOrderPriceDao.batchSave(islandOrderPriceList);
			islandTravelerDao.batchSave(islandTravelerList);
			islandMoneyAmountDao.batchSave(islandMoneyAmountList);
			islandTravelervisaDao.batchSave(islandTravelervisaList);
			islandTravelerPapersTypeDao.batchSave(islandTravelerPapersTypeList);
			hotelAnnexDao.batchSave(islandTravelerFilesList);
			
			
		} catch (Exception e) {
			datas.put("message", "3");
			datas.put("error", "保存异常,请重新操作!");
			throw e;
		}
		datas.put("orderUuid", islandOrder.getUuid());
		
		return datas;
	}
	
	/**
	 * 多币种的累加操作（将同样币种金额进行相加）
	*<p>Title: countMoney</p>
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-6-15 下午10:15:32
	* @throws
	 */
	private void countMoney(List<IslandMoneyAmount> islandMoneyAmountBak) {
		if(CollectionUtils.isEmpty(islandMoneyAmountBak)) {
			return ;
		}

		Iterator<IslandMoneyAmount> islandMoneyAmountBakIter = islandMoneyAmountBak.iterator();
		List<IslandMoneyAmount> islandMoneyAmountList = new ArrayList<IslandMoneyAmount>();
		
		flag:while(islandMoneyAmountBakIter.hasNext()) {
			IslandMoneyAmount islandMoneyAmount = islandMoneyAmountBakIter.next();
			
			for(IslandMoneyAmount entity : islandMoneyAmountList) {
				//当存储游客金额时：
				if(IslandMoneyAmount.BUSINESS_TYPE_TRAVELER == entity.getBusinessType()) {
					if(islandMoneyAmount.getCurrencyId().intValue() == entity.getCurrencyId()) {
						entity.setAmount(entity.getAmount() + islandMoneyAmount.getAmount());
						islandMoneyAmountBakIter.remove();
						
						continue flag;
					}
				//存储其他金额时：
				} else {
					if(islandMoneyAmount.getCurrencyId().intValue() == entity.getCurrencyId() && islandMoneyAmount.getMoneyType().intValue() == entity.getMoneyType()) {
						entity.setAmount(entity.getAmount() + islandMoneyAmount.getAmount());
						islandMoneyAmountBakIter.remove();
						
						continue flag;
					}
				}
			}
			
			islandMoneyAmountList.add(islandMoneyAmount);
		}
		
		return ;
	}
	
	/**
	 * 重建表单传递数据
	*<p>Title: rebuildInputData</p>
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-6-8 下午3:02:02
	* @throws
	 */
	private void rebuildInputData(IslandOrderInput islandOrderInput) {
		//处理结算总额、订单总额（涉及多币种）---------
		String[] islandMoneyAmount_amountArr = islandOrderInput.getIslandMoneyAmount_amount();
		String[] islandMoneyAmount_currencyIdArr = islandOrderInput.getIslandMoneyAmount_currencyId();
		String[] islandMoneyAmount_moneyTypeArr = islandOrderInput.getIslandMoneyAmount_moneyType();

		List<String> amountBak = new ArrayList<String>();
		List<String> currencyBak = new ArrayList<String>();
		List<String> moneyTypeBak = new ArrayList<String>();
		
		if(islandMoneyAmount_amountArr != null && islandMoneyAmount_amountArr.length > 0) {
			
			for(int i=0; i<islandMoneyAmount_amountArr.length; i++) {
				String[] islandMoneyAmount_amount = islandMoneyAmount_amountArr[i].split(";");
				String[] islandMoneyAmount_currencyId = islandMoneyAmount_currencyIdArr[i].split(";");
				
				amountBak.addAll(Arrays.asList(islandMoneyAmount_amount));
				currencyBak.addAll(Arrays.asList(islandMoneyAmount_currencyId));
				
				for(int j=0; j<islandMoneyAmount_amount.length; j++) {
					moneyTypeBak.add(islandMoneyAmount_moneyTypeArr[i]);
				}
			}
		}
		
		islandOrderInput.setIslandMoneyAmount_amount((String[])amountBak.toArray(new String[amountBak.size()]));
		islandOrderInput.setIslandMoneyAmount_currencyId((String[])currencyBak.toArray(new String[currencyBak.size()]));
		islandOrderInput.setIslandMoneyAmount_moneyType((String[])moneyTypeBak.toArray(new String[moneyTypeBak.size()]));
		//处理结算总额、订单总额---------
	}
	
	public OrderPayInput buildOrderPayData(List<String> orderUuids, String[] resultCurrency, String[] resultAmount, String cancelPayUrl) {
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();
		
		if(StringUtils.isNotEmpty(cancelPayUrl)) {
			orderPayInput.setOrderListUrl(cancelPayUrl);
		}
		
		//订单的后置方法名
		orderPayInput.setServiceAfterMethodName("handleIslandOrderPay");
		// //订单的后置类名
		orderPayInput.setServiceClassName("com.trekiz.admin.modules.island.service.impl.IslandOrderServiceImpl");
		
		// 订单总额显示状态(true:显示;false:不显示)
		orderPayInput.setTotalCurrencyFlag(true);
		if(CollectionUtils.isNotEmpty(orderUuids)) {
			for(String orderUuid : orderUuids) {
				//单个订单的支付信息
				OrderPayDetail detail = new OrderPayDetail();
				IslandOrder islandOrder = this.getByUuid(orderUuid);
				
				if(islandOrder.getOrderCompany() != null) {
					orderPayInput.setAgentId(islandOrder.getOrderCompany());
				} else {
					orderPayInput.setAgentId(-1);
				}
				
				detail.setPayPriceType(Context.MONEY_TYPE_QK);
				detail.setOrderUuid(orderUuid);
				detail.setOrderNum(islandOrder.getOrderNum());
				detail.setBusindessType(1);
				detail.setOrderType(Integer.valueOf(Context.ORDER_TYPE_ISLAND));
				
				String currencyId = MoneyAmountUtils.getFormatCurrency(resultCurrency);
				String price = MoneyAmountUtils.getFormatCurrency(resultAmount);
				
				detail.setPayCurrencyId(currencyId);
				detail.setPayCurrencyPrice(price);

				detail.setTotalCurrencyId(currencyId);
				detail.setTotalCurrencyPrice(price);
				
				detailList.add(detail);
			}
		}
		
		// input金额状态(0:不显示；1：显示、可操作；2：显示、只读)
		orderPayInput.setMoneyFlag(1);
		
		//设置订单类型
		orderPayInput.setOrderType(Context.ORDER_TYPE_ISLAND);
		
		orderPayInput.setOrderPayDetailList(detailList);
		return orderPayInput;
	}
	
	/**
	 * 支付完成后订单的后续操作
	 * @param orderPayForm
	 * @return
	 */
	public Map<String, Object> handleIslandOrderPay(OrderPayForm orderPayForm) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
			if (orderPayInput != null && CollectionUtils.isNotEmpty(orderPayInput.getOrderPayDetailList())) {
				String orderUuid = orderPayInput.getOrderPayDetailList().get(0).getOrderUuid();
				if (StringUtils.isNotBlank(orderUuid)) {
					IslandOrder islandOrder = islandOrderDao.getByUuid(orderUuid);
					String payedMoney = islandOrder.getPayedMoney();
					if (StringUtils.isBlank(payedMoney)) {
						payedMoney = UuidUtils.generUuid();
						islandOrder.setPayedMoney(payedMoney);
						islandOrderDao.updateObj(islandOrder);
					}
					 
					List<IslandMoneyAmount> moneyAmountList = Lists.newArrayList();
					for (OrderPayDetail payDetail : orderPayInput.getOrderPayDetailList()) {
						List<IslandMoneyAmount> tempMoneyAmountList = islandMoneyAmountService.getMoneyAmonutBySerialNum(payDetail.getMoneySerialNum());
						if (CollectionUtils.isNotEmpty(tempMoneyAmountList)) {
							for (IslandMoneyAmount money : tempMoneyAmountList) {
								IslandMoneyAmount moneyAmonnt = new IslandMoneyAmount();
								BeanUtils.copyProperties(moneyAmonnt, money);
								moneyAmonnt.setId(null);
								moneyAmonnt.setSerialNum(payedMoney);
								moneyAmonnt.setUuid(UuidUtils.generUuid());
								moneyAmonnt.setMoneyType(Context.MONEY_TYPE_YS);
								moneyAmonnt.setBusinessType(1);
								moneyAmonnt.setBusinessUuid(islandOrder.getUuid());
								moneyAmonnt.setDelFlag(Context.DEL_FLAG_NORMAL);
								moneyAmountList.add(moneyAmonnt);
							}
						}
					}
					if (CollectionUtils.isNotEmpty(moneyAmountList)) {
						islandMoneyAmountService.saveMoneyAmounts(moneyAmountList);
					}
				}
			}
			result.put("isSuccess", true);
		} catch(Exception e) {
			result.put("isSuccess", false);
			result.put("errorMsg", "系统出现异常，请重新操作！！！");
		}
		 
		return result;
	}
	
	/**
	 * 查询海岛游订单列表
	 * @param page
	 * @param islandOrderQuery 查询对象
	 * @param common 部门对象
	 * @return
	 */
	public Page<Map<Object, Object>> findOrderList(Page<Map<Object, Object>> page, IslandOrderQuery islandOrderQuery, DepartmentCommon common) {
		
    	
    	//如果是按团期查询则需要把排序值字段改成以团期表名开头字段
    	if (!islandOrderQuery.getIsOrder() && islandOrderQuery.getOrderBy().contains("pro")) {
    		islandOrderQuery.setOrderBy(islandOrderQuery.getOrderBy().replace("pro", "agp"));
    	} 
    	page.setOrderBy(islandOrderQuery.getOrderBy());
    	
    	//sql语句where条件
        String where = this.getWhereSql(islandOrderQuery, common);

        //获取订单查询sql语句
        String orderSql = getOrderSql(islandOrderQuery.getOrderStatus(), where);
        islandOrderQuery.setOrderSql(orderSql);
        
        //如果是订单查询，则直接返回结果，如果是团期查询则需要再处理sql语句
        if (islandOrderQuery.getIsOrder()) {
        	return getOrderList(page, orderSql);
        } else {
        	return this.getGroupListByOrder(page, orderSql);
        }
    }
	
	/**
	 * 获取查询海岛游where语句
	 * @param islandOrderQuery 查询对象
	 * @param common 部门对象
	 * @return
	 */
    private String getWhereSql(IslandOrderQuery islandOrderQuery, DepartmentCommon common) {
    	
        StringBuffer sqlWhere = new StringBuffer("");
        
        //用户类型，分为接待社内部用户和渠道用户，分别为3和1
        String userType = UserUtils.getUser().getUserType();
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        
        //团期出团日期
        String groupOpenDateBegin = islandOrderQuery.getGroupOpenDateBegin();
        String groupOpenDateEnd = islandOrderQuery.getGroupOpenDateEnd();
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
        	sqlWhere.append(" and agp.groupOpenDate >= '" + groupOpenDateBegin + "'");
        }
        if (StringUtils.isNotBlank(groupOpenDateEnd)){
            sqlWhere.append(" and agp.groupOpenDate  <= '" + groupOpenDateEnd + "'");
        }
        
        //产品创建者即计调
        Integer activityCreateBy = islandOrderQuery.getActivityCreateBy();
        if (activityCreateBy != null){
        	sqlWhere.append(" and users.id = " + activityCreateBy + " ");
        }
        
        //订单创建时间
        String orderTimeBegin = islandOrderQuery.getOrderTimeBegin();
        String orderTimeEnd = islandOrderQuery.getOrderTimeEnd();
        if (StringUtils.isNotBlank(orderTimeBegin)) {
            sqlWhere.append( " and pro.orderTime >= '" + orderTimeBegin + " 00:00:00" + "'");
        }
        if (StringUtils.isNotBlank(orderTimeEnd)) {
            sqlWhere.append( " and pro.orderTime <= '" + orderTimeEnd + " 23:59:59" + "'");
        }
        
        //区分渠道商用户或批发商用户
        Integer agentId = islandOrderQuery.getOrderCompany();
        if (Context.USER_TYPE_MAINOFFICE.equalsIgnoreCase(userType)) {
        	//渠道商用户
        	if (UserUtils.getUser().getAgentId() != null) {
        		sqlWhere.append(" and pro.orderCompany = " + UserUtils.getUser().getAgentId() + " ");
        	}
        } else if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)) {
            //批发商用户
            sqlWhere.append(" and activity.wholesaler_id = " + userCompanyId + " ");
            
            //渠道
            if(agentId != null) {
            	sqlWhere.append(" and pro.orderCompany = " + agentId);
        	}
        }
        
        //非签约渠道名称
        String orderCompanyName = islandOrderQuery.getOrderCompanyName();
        if (StringUtils.isNotBlank(orderCompanyName)) {
        	if (agentId != null && agentId == -1) {
        		sqlWhere.append(" and pro.orderCompanyName = '" + agentId + "' ");
        	}
        }
        
        //产品名称
        if(StringUtils.isNotBlank(islandOrderQuery.getActivityName())){
        	sqlWhere.append(" and activity.activityName like '%" + islandOrderQuery.getActivityName() + "%' ");
        }
        
        //订单号或团号
        String orderNumOrGroupCode = islandOrderQuery.getOrderNumOrGroupCode();
        if (StringUtils.isNotBlank(orderNumOrGroupCode)){
        	sqlWhere.append(" and (pro.orderNum like '%" + orderNumOrGroupCode + "%' ")
        			.append(" or agp.groupCode like '%" + orderNumOrGroupCode + "%') ");
        }
        
        //联系人
        String orderPersonName = islandOrderQuery.getOrderPersonName();
        if (StringUtils.isNotBlank(orderPersonName)) {
        	sqlWhere.append(" and pro.orderPersonName like '%" + orderPersonName + "%' ");
        }
        
        //分部门显示
        systemService.getDepartmentSql("order", null, sqlWhere, common, Context.ORDER_TYPE_ISLAND);
        
        return sqlWhere.toString();
    }
    
    /**
     * 设置查询订单sql语句
     * @param orderStatus 订单状态：0 全部订单；1 待确认报名；2 已确认报名；3 已取消；
     * @param where 订单查询where语句
     * @return
     */
    private String getOrderSql(Integer orderStatus, String where) {
    	
    	//订单查询sql语句
    	StringBuilder sql = new StringBuilder("");
    	sql.append("SELECT activity.activitySerNum, activity.activityName, activity.id activityId, ")
    				.append("activity.uuid activityUuid, activity.wholesaler_id companyId, users.name AS activityCreateUserName, ")
    				.append("agp.groupOpenDate, agp.groupEndDate, agp.groupCode, ")
    				.append("agp.id gruopId, agp.uuid gruopUuid, agp.advNumber, 10, agp.lockStatus settleLockStatus, ")
    				.append("pro.id,pro.uuid orderUuid, pro.orderTime, pro.createBy, pro.orderNum, pro.orderSalerId, pro.orderPersonPhoneNum, ")
    				.append("pro.orderStatus, pro.orderCompanyName, pro.orderPersonNum, pro.isPayment, pro.orderPersonName,pro.apply_time applyTime,")
    				.append("pro.front_money, pro.total_money,  pro.payed_money, pro.accounted_money, pro.activity_island_group_uuid groupUuid, ")
    				.append("costOuter.moneyStr AS costMoney, totalOuter.moneyStr AS totalMoney, ")
    				.append("payedOuter.moneyStr AS payedMoney, accountedOuter.moneyStr AS accountedMoney, ")
    				.append("pro.placeHolderType, pro.activationDate, pro.orderCompany, pro.payMode proPayMode, pro.lockStatus, ")
    				.append("pro.remainDays proRemainDays, pro.cancel_description AS cancelDescription, pro.paymentType, ")
    				.append("limits.id invoiceid, limits.createStatus createStatus, limits.verifyStatus verifyStatus ")
    			.append("FROM activity_island activity, ")
    				.append("activity_island_group agp, ")
    				.append("sys_user users, ")
    				.append("island_order pro ")
    			.append("LEFT JOIN orderinvoice limits ON limits.orderId = pro.id ")
    				.append("AND limits.id = ( ")
    					.append("SELECT MAX(id) ")
    					.append("FROM orderinvoice ")
    					.append("GROUP BY orderId ")
    					.append("HAVING pro.id = orderId ")
    					.append(") ")
    					
    			//订单成本金额多币种查询
    			.append("LEFT JOIN ( ")
    				.append("SELECT mao.serialNum ")
	    				.append(",GROUP_CONCAT(CONCAT ( ")
	    					.append("c.currency_mark, ' ', ")
	    					.append("mao.amount ")
	    					.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
	    			.append("FROM island_money_amount mao ")
	    			.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
	    			.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_CBJ + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
	    			.append("GROUP BY mao.serialNum ")
	    			.append(") costOuter ON costOuter.serialNum = pro.cost_money ")
    			
    			//订单应收金额多币种查询
    			.append("LEFT JOIN ( ")
    				.append("SELECT mao.serialNum ")
	    				.append(",GROUP_CONCAT(CONCAT ( ")
	    					.append("c.currency_mark, ' ', ")
	    					.append("mao.amount ")
	    					.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
	    			.append("FROM island_money_amount mao ")
	    			.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
	    			.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_YSH + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
	    			.append("GROUP BY mao.serialNum ")
	    			.append(") totalOuter ON totalOuter.serialNum = pro.total_money ")
	    			
	    		//订单实收金额多币种查询
    			.append("LEFT JOIN ( ")
    				.append("SELECT mao.serialNum ")
	    				.append(",GROUP_CONCAT(CONCAT ( ")
	    					.append("c.currency_mark, ' ', ")
	    					.append("mao.amount ")
	    					.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
	    			.append("FROM island_money_amount mao ")
	    			.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
	    			.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_YS + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
	    			.append("GROUP BY mao.serialNum ")
	    			.append(") payedOuter ON payedOuter.serialNum = pro.payed_money ")
	    			
	    		//订单已达账金额多币种查询
    			.append("LEFT JOIN ( ")
    				.append("SELECT mao.serialNum ")
	    				.append(",GROUP_CONCAT(CONCAT ( ")
	    					.append("c.currency_mark, ' ', ")
	    					.append("mao.amount ")
	    					.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
	    			.append("FROM island_money_amount mao ")
	    			.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
	    			.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_DZ + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
	    			.append("GROUP BY mao.serialNum ")
	    			.append(") accountedOuter ON accountedOuter.serialNum = pro.accounted_money ")
    			
    			.append("WHERE pro.delFlag = '" + ProductOrderCommon.DEL_FLAG_NORMAL + "' ")
    				.append("AND activity.uuid = pro.activity_island_uuid ")
    				.append("AND activity.createBy = users.id ")
    				.append("AND agp.uuid = pro.activity_island_group_uuid AND agp.activity_island_uuid = pro.activity_island_uuid ")
    				.append((orderStatus != null && orderStatus != 0) ? "AND pro.orderStatus = " + orderStatus + " " : "")
    				.append(where);
    	return sql.toString();
    }
    
    /**
     * 查询订单列表
     * @param page
     * @param sql
     * @return
     */
    private Page<Map<Object, Object>> getOrderList(Page<Map<Object, Object>> page, String sql) {
        Page<Map<Object, Object>> pageMap = islandOrderDao.findPageBySql(page, sql, Map.class);
        if(pageMap != null) {
        	for(Map<Object, Object> m : pageMap.getList()) {
        		Object orderSalerId = m.get("orderSalerId");
        		if (orderSalerId != null) {
	        		m.put("orderSalerId", UserUtils.getUser(StringUtils.toLong(orderSalerId)).getName());
        		} else {
        			m.put("orderSalerId","");
        		}
        	}
        }
        return pageMap;
    }
    
    /**
     * 根据订单查询条件查询涉及到团期，并根据团期id查询团期
     * @param page
     * @param orderSql 订单查询语句
     * @return
     */
    private Page<Map<Object, Object>> getGroupListByOrder(Page<Map<Object, Object>> page, String orderSql) {
    	
    	StringBuffer sql = new StringBuffer("");
    	//获取根据订单查询条件查询团期ids的sql语句
    	orderSql = "SELECT distinct agp.uuid groupUuid " + orderSql.substring(orderSql.indexOf("FROM"));
    	orderSql = orderSql.split("LEFT JOIN")[0] + orderSql.substring(orderSql.lastIndexOf("WHERE"));
    	
    	//查询团期sql
    	sql.append("SELECT ")
				.append("agp.id, agp.uuid groupUuid, agp.groupCode groupCode, activity.activityName, activity.uuid activityUuid, ")
				.append("agp.groupOpenDate, agp.groupEndDate, agp.advNumber planPosition, airlineGroupOuter.freePosition, ")
				.append("NOW() nowDate, agp.status, activity.createBy createBy ")
			.append("FROM activity_island activity, activity_island_group agp ")
				//获取团期余位
    			.append("LEFT JOIN ( ")
    				.append("SELECT activity_island_group_uuid, sum(airlineGroup.remNumber) freePosition ")
	    			.append("FROM activity_island_group_airline airlineGroup ")
	    			.append("GROUP BY activity_island_group_uuid ")
	    			.append(") airlineGroupOuter ON airlineGroupOuter.activity_island_group_uuid = agp.uuid ")
			.append("WHERE agp.activity_island_uuid = activity.uuid ")
			.append("AND agp.uuid in ")
			.append("(")
			.append(orderSql)
			.append(")");
        Page<Map<Object, Object>> pageMap = islandOrderDao.findBySql(page, sql.toString(), Map.class);
        return pageMap;
    }
    
    /**
     * 根据团期IDS查询订单
     * @param groupList
     * @param orderSql
     * @return 订单列表
     */
    public Page<Map<Object, Object>> findByGroupIds(Page<Map<Object, Object>> page, List<String> groupList, String orderSql) {
    	//给排序值加别名，防止查询过程中出现不认识情况
    	String orderBy = page.getOrderBy();
    	if(StringUtils.isNotBlank(orderBy) && orderBy.indexOf("group") == 0) {
    		orderBy = orderBy.replace("group", "agp.group");
    		page.setOrderBy(orderBy);
    	}
    	//构造sql语句
    	StringBuffer sql = new StringBuffer("");
    	sql.append("SELECT ")
    		.append("agp.uuid groupUuid,pro.createBy,pro.orderNum,pro.orderSalerId,pro.orderPersonName,pro.orderPersonPhoneNum,pro.orderStatus,")
    		.append("costOuter.moneyStr AS costMoney,totalOuter.moneyStr AS totalMoney,pro.orderPersonNum,payedOuter.moneyStr AS payedMoney,")
    		.append("pro.id,pro.uuid orderUuid,pro.orderTime,pro.activationDate,pro.placeHolderType,accountedOuter.moneyStr AS accountedMoney,")
    		.append("pro.orderCompany,pro.lockStatus,pro.orderCompanyName,pro.apply_time AS applyTime,")
    		.append("pro.payMode proPayMode,pro.remainDays proRemainDays,pro.cancel_description AS cancelDescription,pro.isPayment,")
    		.append("agp.lockStatus settleLockStatus,agp.groupOpenDate,activity.uuid activityUuid ")
    		.append(orderSql.substring(orderSql.indexOf("FROM")));
    	
    	//加入团期IDS限制条件
    	StringBuffer groupIdSql = new StringBuffer("");
		for(int i=0;i<groupList.size();i++) {
			if(i != groupList.size()-1) {
				groupIdSql.append("'" + groupList.get(i) + "',");
			} else {
				groupIdSql.append("'" + groupList.get(i) + "'");
			}
		}
		sql.append(" and pro.activity_island_group_uuid in (" + groupIdSql + ")");
		//查询
		page.setPageSize(300);
		Page<Map<Object, Object>> pageMap = islandOrderDao.findPageBySql(page, sql.toString(), Map.class);
    	return pageMap;
    }
    
    /**
	 * 清除session中Orderpay的缓存
	 * @param role
	 * @return
	 */
	public Object clearObject(Object object){
		islandOrderDao.getSession().evict(object);
		return object;
	}
	
	/**
	 * 订单财务收款提示信息： 确认达账例子：财务已确认订金/尾款/全款达账金额$100；
	 * 						撤销达账例子：财务已撤销订金/尾款/全款达账金额$100；
	 * 						驳回且保留占位例子：财务驳回订金/尾款/全款付款￥12500元，需重新发起付款；
	 * 						驳回且不保留占位例子：财务驳回订金/尾款/全款付款￥12500元，并取消占位，需重新发起预定；
	 * @param orderUuid 订单UUID
	 * @param isCanceledOrder 订单是否因驳回取消
	 * @return
	 */
	public String getOrderPrompt(String orderUuid, boolean isCanceledOrder) {
		
		List<PayIslandOrder> orderPayList = payIslandOrderService.findLastDateOrderPay(orderUuid);
		String prompt = "财务已确认收款";
		
		PayIslandOrder orderPay = null;
		if (CollectionUtils.isNotEmpty(orderPayList)) {
			orderPay = orderPayList.get(0);
		}
		
		//达账和撤销提示
        if (orderPay != null && orderPay.getPayPriceType() != null && orderPay.getIsAsAccount() != null) {
			Integer isAsAccount = orderPay.getIsAsAccount();
			
			if (Context.ORDERPAY_ACCOUNT_STATUS_YCX == isAsAccount) {
				prompt = prompt.replace("确认", "撤销") + "达账金额";
			} else if (Context.ORDERPAY_ACCOUNT_STATUS_YDZ.equals(isAsAccount)) {
				prompt += "达账金额";
			} else if (Context.ORDERPAY_ACCOUNT_STATUS_YBH.equals(isAsAccount)) {
				prompt = prompt.replace("已确认", "驳回") + "付款";
			}
			
			prompt += islandMoneyAmountService.getMoneyStr(orderPay.getMoneySerialNum(), true);
			
			if (Context.ORDERPAY_ACCOUNT_STATUS_YBH.equals(isAsAccount)) {
				if (isCanceledOrder) {
					prompt += "，并取消占位，需重新发起预定";
				} else {
					prompt += "，需重新发起付款";
				}
			}
			
			return prompt;
        }
		return null;
	}
	
	/**
	 * 订单锁定
	 * @param uuid 订单uuid
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void lockOrder(String uuid) {
		IslandOrder islandOrder = getByUuid(uuid);
		islandOrder.setLockStatus(Context.LOCK_ORDER);
		update(islandOrder);
        //添加操作日志
        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
        		"订单" + islandOrder.getId() + "锁死成功", "2", Context.ProductType.PRODUCT_ISLAND, islandOrder.getId().longValue());
    }
	
	/**
	 * 订单解锁
	 * @param uuid 订单uuid
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void unLockOrder(String uuid) {
		IslandOrder islandOrder = getByUuid(uuid);
		islandOrder.setLockStatus(Context.UNLOCK_ORDER);
		update(islandOrder);
		//添加操作日志
		this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
				"订单" + islandOrder.getId() + "解锁成功", "2", Context.ProductType.PRODUCT_ISLAND, islandOrder.getId().longValue());
	}
	
	/**
	 * 订单关联文件
	 * @param uuid 订单orderUuid
	 * @param uuid 文件ids
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void setOrderFiles(String orderUuid, String fileIds) {
		IslandOrder islandOrder = getByUuid(orderUuid);
		if (StringUtils.isNotBlank(islandOrder.getFileIds())) {
			islandOrder.setFileIds(islandOrder.getFileIds() + fileIds);
		} else {
			islandOrder.setFileIds(fileIds);
		}
		update(islandOrder);
	}
	
	/**
	 * 获取订单关联文件信息
	 * @param orderUuid 订单uuid
	 */
	public List<DocInfo> getFilesInfo(String orderUuid) {
		List<DocInfo> docInfoLists = Lists.newArrayList();
		IslandOrder islandOrder = getByUuid(orderUuid);
		if (islandOrder != null) {
			String docIds = islandOrder.getFileIds();
			List<Long> docIdList = Lists.newArrayList();
			if (StringUtils.isNotBlank(docIds)) {
				String ids [] = docIds.split(",");
				for (String id : ids) {
					if (StringUtils.isNotBlank(id)) {
						docIdList.add(Long.parseLong(id));
					}
				}
			}
			if (CollectionUtils.isNotEmpty(docIdList)) {
				docInfoLists = docInfoDao.findDocInfoByIds(docIdList);
			}
		}
		return docInfoLists;
	}
	
	/**
	 * 根据UUIDS查询订单列表
	 * @param orderUuids
	 * @return
	 */
	public List<IslandOrder> getByUuids(List<String> orderUuids) {
		DetachedCriteria dc = islandOrderDao.createDetachedCriteria();
		dc.add(Restrictions.in("uuid", orderUuids));
		return islandOrderDao.find(dc);
	}
	
	/**
     * 激活订单
     * @param uuid 订单uuid
     * @param request
     */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public String invokeOrder(String uuid, HttpServletRequest request) {
		IslandOrder islandOrder = islandOrderDao.getByUuid(uuid);
		Integer orderStatus = islandOrder.getOrderStatus();
		if (orderStatus != null) {
			//如果是待确认报名订单，则直接激活订单
			if (Context.ISLAND_ORDER_STATUS_YQX == orderStatus) {
			   islandOrder.setOrderStatus(Context.ISLAND_ORDER_STATUS_DQR);
			   islandOrderDao.saveObj(islandOrder);
			   //添加操作日志
			   this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
					   "订单" + islandOrder.getOrderNum() + "激活成功", "2", Context.ProductType.PRODUCT_ISLAND, islandOrder.getId().longValue());
			}
		}
		return "success";
	}
	
	/**
     * 取消订单
     * @param islandOrder 海岛游订单
     * @param description 取消理由
     * @param request
     */
   @Transactional(readOnly=false, rollbackFor=Exception.class)
   public void cancelOrder(IslandOrder islandOrder, String description, HttpServletRequest request) {
	   Integer orderStatus = islandOrder.getOrderStatus();
	   if (orderStatus != null) {
		   //如果是待确认报名订单，则直接取消订单
		   if (Context.ISLAND_ORDER_STATUS_DQR == orderStatus) {
			   islandOrder.setOrderStatus(Context.ISLAND_ORDER_STATUS_YQX);
			   islandOrder.setCancelDescription(description);
			   islandOrderDao.saveObj(islandOrder);
			   //添加操作日志
		       this.saveLogOperate(Context.log_type_orderform,  Context.log_type_orderform_name,
		    		   "订单" + islandOrder.getOrderNum() + "取消成功", "2", Context.ProductType.PRODUCT_ISLAND, islandOrder.getId().longValue());
		   }
	   }
   }
   
   /**
    * 修改凭证上传图片文件
    * @param docInfoList 文件列表
    * @param payIslandOrder 支付实体
    * @param orderUuid 订单UUID
    * @param mode
    * @param request
    * @return
    * @throws OptimisticLockHandleException
    * @throws PositionOutOfBoundException
    * @throws Exception
    */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> updatepayVoucherFile(ArrayList<DocInfo> docInfoList, PayIslandOrder payIslandOrder, String orderUuid, ModelMap mode, 
			HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException,Exception {

		List<Long> docIdList = Lists.newArrayList();
		if (StringUtils.isNotBlank(payIslandOrder.getPayVoucher())) {
			String ids [] = payIslandOrder.getPayVoucher().split(",");
			for (String id : ids) {
				if (StringUtils.isNotBlank(id)) {
					docIdList.add(Long.parseLong(id));
				}
			}
		}
		List<DocInfo> old_docInfoList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(docIdList)) {
			old_docInfoList = docInfoDao.findDocInfoByIds(docIdList);
			Iterator<DocInfo> iter = old_docInfoList.iterator();
			while (iter.hasNext()) {
				DocInfo old_docInfo = iter.next();
				docInfoDao.delete(old_docInfo);
			}
		}
		IslandOrder islandOrder = getByUuid(orderUuid);

		if (docInfoList != null && docInfoList.size() > 0) {
			Iterator<DocInfo> iter = docInfoList.iterator();
			String docInfoIds = null;
			while (iter.hasNext()) {
				DocInfo docInfo = iter.next();
				docInfo.setPayOrderId(payIslandOrder.getId().longValue());
				docInfoDao.save(docInfo);
				// save 保存之后，docInfo对象在数据库生成的主键ID居然也加入了docInfo对象中。
				// save之前，getId()应该是null，save后getId()有值了。
				if(StringUtils.isNotBlank(docInfoIds)){
					docInfoIds += "," + docInfo.getId();
				}else{
					docInfoIds =  docInfo.getId().toString();
				}
			}
			payIslandOrder.setPayVoucher(docInfoIds);
		}
		payIslandOrderDao.updateBySql("update pay_island_order set payVoucher = ?,remarks=? where id = ?", 
				payIslandOrder.getPayVoucher(),payIslandOrder.getRemarks(), payIslandOrder.getId());
		
		Map<String, Object> map = new HashMap<String, Object>();

		//支付订单金额千位符处理
	    if (StringUtils.isNotBlank(payIslandOrder.getMoneySerialNum())) {
	 	    clearObject(payIslandOrder);
	 	   payIslandOrder.setMoneySerialNum(islandMoneyAmountService.getMoneyStr(payIslandOrder.getMoneySerialNum(), true));
	    }
		mode.addAttribute("orderPay", payIslandOrder);
		mode.addAttribute("islandOrder", islandOrder);
		map.put("isSuccess", true);
		return map;
	}
	
	
	/**
	 * 获取退团审核信息列表
	 * @param flowType 流程类型
	 * @param orderUuid 订单UUID
	 * @return
	 */
	public List<Map<String, String>> getExitGroupReviewInfo(Integer flowType, String orderId) {
		
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(Context.ProductType.PRODUCT_ISLAND, flowType, orderId, false);
		String checksql = "";
		if (CollectionUtils.isNotEmpty(reviewMapList)) {
			for (Map<String,String> m : reviewMapList) {
				checksql += (m.get("travelerId") + ",");
			}
		}
		if (StringUtils.isNotBlank(checksql)) {
			checksql = checksql.substring(0, checksql.length()-1);
			checksql = "(" + checksql + ")";
			List<Map<Object,Object>> map = getTravelerInfoForReview(checksql);
			for (Map<String,String> m : reviewMapList) {
				for (Map<Object,Object> travelerMap : map) {
					if (m.get("travelerId").equals(travelerMap.get("travelerId").toString())) {
						m.put("travelerUuid", travelerMap.get("travelerUuid") + "");
						m.put("payPrice", getMoneyStr(travelerMap.get("payPrice").toString()));
						
						m.put("spaceLevelName", travelerMap.get("spaceLevelName") != null ? travelerMap.get("spaceLevelName").toString() : "");
						m.put("personTypeName", travelerMap.get("personTypeName") != null ? travelerMap.get("personTypeName").toString() : "");
					}
				}
			}
		}	
		return reviewMapList;
	}
	
	/**
	 * 获取审核部分所需游客信息(根据审核查询条件)
	 * @param checksql 查询sql
	 * @return
	 */
	private List<Map<Object,Object>> getTravelerInfoForReview(String checksql) {
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT orders.orderTime, spaceDict.label spaceLevelName, traType.name personTypeName, tra.id as travelerId, tra.uuid travelerUuid, ")
				.append("tra.order_uuid orderUuid, tra.name,tra.payPriceSerialNum, tra.personType, IFNULL(a.cost_money, 0) as payPrice ")
		.append("FROM island_traveler tra ")
		.append("LEFT JOIN island_order orders ON tra.order_uuid = orders.uuid ")
		.append("LEFT JOIN sys_dict spaceDict ON tra.space_level = spaceDict.value and spaceDict.type = 'spaceGrade_Type' ")
		.append("LEFT JOIN traveler_type traType ON tra.personType = traType.uuid ")
		.append("LEFT JOIN (")
					.append("SELECT t.id, t.order_uuid orderUuid, t.payPriceSerialNum, group_concat(format(IFNULL(ma.amount, 0),2), ")
						.append("c.currency_name, 'money' ORDER BY c.currency_id) AS cost_money ")
					.append("FROM island_traveler t ")
					.append("LEFT JOIN island_money_amount ma ON t.payPriceSerialNum = ma.serialNum AND t.id in " + checksql + " ")
					.append("INNER JOIN currency c on ma.currencyId= c.currency_id ")
					.append("GROUP BY t.id")
		.append(") a ON tra.id= a.id  WHERE tra.id in " + checksql);
		return islandOrderDao.findBySql(sbf.toString(), Map.class);
	}
	
	private String getMoneyStr(String moneyStr) {
		moneyStr=moneyStr.replaceAll("-", "del");
		moneyStr=moneyStr.replaceAll("money,", "add");
		moneyStr=moneyStr.replaceAll("adddel", "-");
		moneyStr=moneyStr.replaceAll("add", "+");
		moneyStr=moneyStr.replaceAll("money", "");
		moneyStr=moneyStr.replaceAll("del", "-");
		return moneyStr;
	}
	
	/**
	 * 获取退团审核详情
	 * @param rid 审核id
	 * @return
	 */
	public Map<String, Object> getExitGroupReviewInfoById(long rid) {		
		Map<String,Object> resultMap = Maps.newHashMap();
		List<Review> list = reviewService.findReview(rid, false);
		if (CollectionUtils.isNotEmpty(list)) {
			resultMap.put("reviewInfo", list.get(0));
			String checksql = "";
			checksql = "(" + list.get(0).getTravelerId() + ")";
			List<Map<Object,Object>> map = getTravelerInfoForReview(checksql);
			Map<Object,Object> mapResult = map.get(0);
			String payPrice = mapResult.get("payPrice").toString();
			mapResult.put("payPrice", getMoneyStr(payPrice));
			mapResult.put("spaceLevelName", mapResult.get("spaceLevelName") != null ? mapResult.get("spaceLevelName").toString() : "");
			mapResult.put("personTypeName", mapResult.get("personTypeName") + "");
			resultMap.put("travelerInfo", mapResult);
			List<ReviewLog> rLog = reviewService.findReviewLog(rid);
			resultMap.put("reviewLogInfo", rLog);
		}
		return resultMap;
	}
	
	/**
	 * 根据订单UUID查询游客列表
	 * @param orderUuid 订单UUID
	 * @return
	 */
	public List<Map<Object, Object>> getTravelerByOrderUuid(String orderUuid) {
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT o.orderTime, spaceDict.label spaceLevelName, traType.name personTypeName, t.id, t.order_uuid, t.name, t.payPriceSerialNum, ")
			.append("IFNULL(a.accounted_money, 0) as payPrice ")
		.append("FROM  island_traveler t ")
		.append("LEFT JOIN island_order o ON t.order_uuid = o.uuid ")
		.append("LEFT JOIN sys_dict spaceDict ON t.space_level = spaceDict.value and spaceDict.type = 'spaceGrade_Type' ")
		.append("LEFT JOIN traveler_type traType ON t.personType = traType.uuid ")
		.append("LEFT JOIN (")
			.append("SELECT tra.id, group_concat(IFNULL(m.amount, 0), c.currency_name, 'money' ORDER BY c.currency_id ) AS accounted_money ")
			.append("FROM island_traveler tra ")
			.append("LEFT JOIN island_money_amount m ON tra.payPriceSerialNum = m.serialNum ")
			.append("AND tra.order_uuid = ? AND tra.status in(0,4) INNER JOIN currency c ON m.currencyId = c.currency_id ")
			.append(" GROUP BY tra.id) a ")
		.append("ON t.id = a.id  WHERE t.delFlag = 0 AND t.order_uuid = ? AND t.status in(0,4) ORDER BY t.id ASC");
		List<Map<Object, Object>> ls = islandOrderDao.findBySql(sbf.toString(), Map.class, orderUuid, orderUuid);
		for (Map<Object, Object> m : ls) {
			String payPrice = m.get("payPrice").toString();
			if (StringUtils.isNotBlank(payPrice)) {
				m.put("payPrice", getMoneyStr(payPrice));
			}
		}
		return ls;
	}
	
	/**
	 * 保存退团申请
	 * @param productType
	 * @param flowType
	 * @param travelerId
	 * @param travelerName
	 * @param exitReason
	 * @param orderUuid
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> saveExitGroupReviewInfo(Integer productType, Integer flowType, 
			Long[] travelerId, String[] travelerName, String[] exitReason, String orderUuid) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		IslandOrder order = getByUuid(orderUuid);
		if (null == order || order.getId() == 0) {
			map.put("sign", 0);
			map.put("result", "未找到订单！");
			return map;
			
		} 
		ActivityIsland activity = activityIslandDao.getByUuid(order.getActivityIslandUuid());
		if (null == activity || activity.getId() == 0) {
			map.put("sign", 0);
			map.put("result", "未找到产品！");
			return map;
		}
		
		if (null == activity.getDeptId() || 0 == activity.getDeptId().intValue()) {
			map.put("sign", 0);
			map.put("result", "产品没有部门！");
			return map;
		}
		int sign = 1;
		StringBuffer sbf = new StringBuffer();
		String travelerNames = "";
		for (int i = 0; i < travelerId.length; i++) {
			if (sign == 1) {
				IslandTraveler traveler = islandTravelerDao.getById(travelerId[i].intValue());
				if (null != traveler && traveler.getId() > 0) {
					List<Detail> detailList = new ArrayList<Detail>();
					detailList.add(new Detail("travelerId",travelerId[i].toString()));
					detailList.add(new Detail("travelerName",travelerName[i]));
					detailList.add(new Detail("exitReason",exitReason[i]));
					travelerNames = travelerNames + travelerName[i]+" ";
					long rid = reviewService.addSuccessReview(
							productType, flowType, order.getId() + "", travelerId[i], (long)0, exitReason[i], sbf, detailList, activity.getDeptId().longValue());
					if (rid > 0 && sign == 1) {
						traveler.setStatus(Context.TRAVELER_DELFLAG_EXIT.toString());
						islandTravelerDao.updateObj(traveler);
						order.setOrderPersonNum(order.getOrderPersonNum() - 1);
					} else {
						sign = 0;
					}
				} else {
					sign = 0;
					throw new Exception("找不到游客信息！");
				}
			}
		}
		map.put("sign", sign);
		map.put("result", sbf.toString());
		if(sign == 0){
			throw new Exception(sbf.toString());
		}
		islandOrderDao.saveObj(order);
		//添加操作日志项
		StringBuffer content = new StringBuffer();
		content.append("订单号为：" + order.getOrderNum() + " 的订单 发起退团申请流程,").append("具体退团游客有【"+travelerNames+"】");
		this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
				content.toString(), 1 + "", Context.ProductType.PRODUCT_ISLAND, order.getId().longValue());
		return map;
	}
	
	/**
	 * 从数据库根据表名和条件获取指定的值
	 * @param table 表名
	 * @param queryName 查询字段名
	 * @param queryValue 查询字段名值
	 * @param getName 查询值
	 * @return
	 */
	public String getData(String tableName, String queryName, String queryValue, String getName) {
		String sql = "SELECT " + getName + " FROM " + tableName + " WHERE " + queryName + " = '" + queryValue + "'";
		List<String> returnList = islandOrderDao.findBySql(sql);
		if (CollectionUtils.isNotEmpty(returnList)) {
			return returnList.get(0);
		}
		return "";
	}
	public String getSpaceGradeTypeData(String type, String queryName, String queryValue, String getName) {
		String sql = "SELECT " + getName + " FROM sys_dict WHERE type = '"+ type + "' AND " + queryName + " = '" + queryValue + "'";
		List<String> returnList = islandOrderDao.findBySql(sql);
		if (CollectionUtils.isNotEmpty(returnList)) {
			return returnList.get(0);
		}
		return "";
	}
	public boolean islandOrderCancel(String payUuid, String orderUuid) {

		IslandOrder islandOrder = islandOrderDao.getByUuid(orderUuid);
		// 撤销：订单达账金额减去撤销金额
		this.DTcancelOrder(payUuid, islandOrder);

		//修改金额表中的付款方式类型为财务取消状态
		productMoneyAmountService.updateMoneyType(Context.ORDER_TYPE_ISLAND, islandOrder.getAccountedMoney(), Context.MONEY_TYPE_CANCEL);
		return true;
	}
	
	/**
	 * 海岛游达账撤销
	*<p>Title: DTcancelOrder</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午12:21:55
	* @throws
	 */
	public boolean DTcancelOrder(String payUuid, IslandOrder islandOrder) {
		
		//达账金额减去撤销金额
		List<IslandMoneyAmount> list = islandMoneyAmountService.getMoneyAmonutBySerialNum(islandOrder.getAccountedMoney());
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				islandMoneyAmountService.addOrSubtractMoneyAmount(list.get(i), islandOrder.getAccountedMoney(), false);
			}
		}
		return true;
	}
	@Override
	public Integer getBookingPersonNum(String groupUuid) {
		// TODO Auto-generated method stub
		return islandOrderDao.getBookingPersonNum(groupUuid);
	}
	public Integer getForecaseReportNum(String groupUuid) {
		// TODO Auto-generated method stub
		return islandOrderDao.getForecaseReportNum(groupUuid);
	}
	
	
	/**
	 * 修改海岛游订单信息
	 * @param jsonData
	 * @return
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public int updateIslandOrder(String jsonData) throws Exception {
		logger.info("获取的json数据：" + jsonData);
		
		JSONObject json = JSONObject.fromObject(jsonData);
		long islandOrderId = json.getLong("islandOrderId");	//订单id
		String orderUuid = json.getString("islandOrderUuid");//订单Uuid
		
		IslandOrder islandOrder = islandOrderDao.getByUuid(orderUuid);
		
		//联系人列表
		JSONArray contactsArr = (JSONArray)json.get("contactsArr");	
		saveOrUpdateContacts(contactsArr,islandOrderId);
		
		//海岛游订单控房明细表
		JSONArray controlDetailArr = (JSONArray)json.get("controlDetailArr");	
		saveOrUpdateOrderControlDetail(controlDetailArr,orderUuid);
		
		JSONArray moneyAndPeopleArr = (JSONArray)json.get("moneyAndPeopleArr");	//费用及人数列表
		if (moneyAndPeopleArr != null && moneyAndPeopleArr.size() > 0) {
			int orderPersonNum = 0;
			//批量更新订单价格表人数
			for (int i = 0 ; i < moneyAndPeopleArr.size(); i++) {
				JSONObject orderPrice =  (JSONObject)moneyAndPeopleArr.get(i);
				String uuid = orderPrice.getString("groupPriceUuid");
				int num = orderPrice.getInt("orderPersonNum");
				orderPersonNum += num;
//				islandOrderPriceDao.updateOrderPriceNum(num, orderUuid, groupPriceUuid);
				islandOrderPriceDao.updateOrderPriceNumByUuid(num, uuid);
			}
			islandOrder.setOrderPersonNum(orderPersonNum);
		}
		
		//订单价格表（费用调整操作）
		JSONArray moneyChangeArr = (JSONArray)json.get("moneyChangeArr");	//费用调整列表
		saveOrUpdateOrderPrice(moneyChangeArr,orderUuid);
		
		//依据订单uuid查询出原来所有的游客
		List<IslandTraveler> islandTravelers = islandTravelerDao.findTravelerByOrderUuid(orderUuid, false);
		
		//游客列表
		JSONArray travelerArr = (JSONArray)json.get("travelerArr");	
		if(travelerArr != null && travelerArr.size() > 0){
			List<IslandTraveler> saveTravelerList = new ArrayList<IslandTraveler>();	//批量保存游客
			List<IslandTraveler> updateTravelerList = new ArrayList<IslandTraveler>();	//批量更新游客
			List<IslandTraveler> deleteTravelerList = new ArrayList<IslandTraveler>();	//批量删除游客
			for(int i = 0 ; i < travelerArr.size(); i++){
				JSONObject traveler = (JSONObject)travelerArr.get(i);
				
				IslandTraveler islandTraveler = null;
				String travelerUuid = traveler.getString("travelerUuid");	//游客表uuid
				if(StringUtils.isNotEmpty(travelerUuid)){
					islandTraveler = islandTravelerDao.getByUuid(travelerUuid);
					islandTraveler.setUuid(travelerUuid);
					islandTraveler.setName(traveler.getString("travelerName"));	//游客姓名
					islandTraveler.setNameSpell(traveler.getString("nameSpell"));	//游客名称拼音
					islandTraveler.setPersonType(traveler.getString("personType"));	//游客类型
					islandTraveler.setSpaceLevel(traveler.getString("spaceLevel"));	//舱位等级
					islandTraveler.setSex(traveler.getInt("sex"));	//性别
					islandTraveler.setRemark(traveler.getString("travelerRemark"));	//备注
					
					super.setOptInfo(islandTraveler, BaseService.OPERATION_UPDATE);
					updateTravelerList.add(islandTraveler);
				}else{
					travelerUuid = UuidUtils.generUuid();
					islandTraveler = new IslandTraveler();
					
					islandTraveler.setUuid(travelerUuid);	//uuid
					islandTraveler.setOrderUuid(orderUuid);//订单uuid
					islandTraveler.setName(traveler.getString("travelerName"));	//游客姓名
					islandTraveler.setNameSpell(traveler.getString("nameSpell"));	//游客名称拼音
					islandTraveler.setPersonType(traveler.getString("personType"));	//游客类型
					islandTraveler.setSpaceLevel(traveler.getString("spaceLevel"));	//舱位等级
					islandTraveler.setSex(traveler.getInt("sex"));	//性别
					islandTraveler.setStatus(Context.DEL_FLAG_NORMAL);
					islandTraveler.setRemark(traveler.getString("travelerRemark"));	//备注
					
					islandTraveler.setOriginalPayPriceSerialNum(UuidUtils.generUuid());
					islandTraveler.setPayPriceSerialNum(UuidUtils.generUuid());
					islandTraveler.setCostPriceSerialNum(UuidUtils.generUuid());
					
					super.setOptInfo(islandTraveler, BaseService.OPERATION_ADD);
					saveTravelerList.add(islandTraveler);
				}
				//签证信息
				JSONArray visaArr = (JSONArray)traveler.get("visaArr");
				saveOrUpdateTravelerVisa(visaArr,orderUuid,travelerUuid);
				
				//证件类型信息
				JSONArray papersTypeArr = (JSONArray)traveler.get("papersTypeArr");
				saveOrUpdateTravelerPapersType(papersTypeArr,orderUuid,travelerUuid);
				
				//海岛游专用金额表
				JSONArray travelerMoneyArr = (JSONArray)traveler.get("travelerMoneyArr");
				saveOrUpdateTravelerMoneyAmount(travelerMoneyArr,orderUuid,travelerUuid, islandTraveler);
				
				//附件信息
				JSONArray fileArr = (JSONArray)traveler.get("fileArr");
				saveFile(fileArr,travelerUuid);
				
			}
			//获取更新游客的补集
			if (islandTravelers.removeAll(updateTravelerList)) {
				deleteTravelerList = islandTravelers;
			}
			//对补集的delflag设置为0
			for (IslandTraveler hTraveler : deleteTravelerList) {
				hTraveler.setDelFlag(Context.DEL_FLAG_DELETE);
				hTraveler.setUpdateDate(new Date());
			}
			
			islandTravelerDao.batchSave(saveTravelerList);	//批量保存
			islandTravelerDao.batchUpdate(updateTravelerList); //批量修改游客
			islandTravelerDao.batchUpdate(deleteTravelerList);//批量删除游客
			//添加修改记录（暂时没内容，只是记录谁修改）
			this.saveLogOperate(Context.log_type_orderform, 
					Context.log_type_orderform_name, "", Context.log_state_up, Context.ProductType.PRODUCT_ISLAND, null);
		} else {
			//对补集的delflag设置为0
			for (IslandTraveler hTraveler : islandTravelers) {
				hTraveler.setDelFlag(Context.DEL_FLAG_DELETE);
				hTraveler.setUpdateDate(new Date());
			}
			islandTravelerDao.batchUpdate(islandTravelers);//批量删除游客
		}
		
		//修改订单金额信息
		//订单总额
		JSONArray receiptedArr = (JSONArray)json.get("costMoneyArr");
		String costMoneySerialNum = UuidUtils.generUuid();	//流水号
		saveOrderMoney(receiptedArr, orderUuid, costMoneySerialNum, Context.MONEY_TYPE_CBJ, islandOrder.getCostMoney());
				
		//订单结算总额
		JSONArray totalCostArr = (JSONArray)json.get("totalCostArr");
		String totalMoneySerialNum = UuidUtils.generUuid();	//流水号
		saveOrderMoney(totalCostArr ,orderUuid, totalMoneySerialNum, Context.MONEY_TYPE_YSH, islandOrder.getTotalMoney());
		
		islandOrder.setCostMoney(costMoneySerialNum);//订单成本价总额流水号
		islandOrder.setTotalMoney(totalMoneySerialNum);//订单结算总额流水号
		islandOrder.setOrderCompany(json.getInt("orderCompany"));	//渠道
		islandOrder.setOrderCompanyName(json.getString("orderCompanyName"));//渠道名称
		String isTransfer = json.getString("isTransfer");
		if(StringUtils.isNotEmpty(json.getString("subControlNum"))){
			islandOrder.setSubControlNum(json.getInt("subControlNum"));	//控房间数
		}
		if(StringUtils.isNotEmpty(json.getString("subUnControlNum"))){
			islandOrder.setSubUnControlNum(json.getInt("subUnControlNum"));//非控房
		}
		if(StringUtils.isNotEmpty(json.getString("subControlTicketNum"))){
			islandOrder.setSubControlTicketNum(json.getInt("subControlTicketNum"));//控票
		}
		if(StringUtils.isNotEmpty(json.getString("subUnControlTicketNum"))){
			islandOrder.setSubUnControlTicketNum(json.getInt("subUnControlTicketNum"));//非控票
		}
		if (Boolean.parseBoolean(isTransfer)) {
			islandOrder.setOrderStatus(Context.HOTEL_ORDER_STATUS_YQR);
			//扣减酒店和机票余位
			IslandOrderControlDetailQuery islandOrderControlDetailQuery = new IslandOrderControlDetailQuery();
			islandOrderControlDetailQuery.setOrderUuid(orderUuid);
			List<IslandOrderControlDetail> detailList = islandOrderControlDetailService.find(islandOrderControlDetailQuery);
			//接收扣减余位结果，如果判断失败就返回
			Map<String, Object> deductResult = hotelControlService.deductRemNumber(islandOrder, detailList);
			if (deductResult == null || !(deductResult.size() > 0)) {
				return 0;
			}
			String resultStr = deductResult.get("ret").toString();
			if ("fail".equals(resultStr)) {
				return 0;
			}
			
			islandOrder.setApplyTime(new Date());//转报名时间
		}
		islandOrder.setRemark(json.getString("islandOrderRemark")); //备注
		
		
		super.setOptInfo(islandOrder, BaseService.OPERATION_UPDATE);
		islandOrderDao.updateObj(islandOrder);
		
		//添加操作日志
		super.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
				json.getString("islandOrderRemark"), Context.log_state_up, Context.ProductType.PRODUCT_ISLAND, islandOrder.getId().longValue());
		
		return 1;
	}
	
	
	/**
	 * 批量保存海岛游订单控房明细
	 * @param orderControlArr	海岛游订单控房明细jsonArr
	 * @param orderUuid			订单uuid
	 */
	private void saveOrUpdateOrderControlDetail(JSONArray orderControlArr,String orderUuid){
		//海岛游订单控房明细
		if(orderControlArr != null && orderControlArr.size() > 0){
			List<IslandOrderControlDetail> saveList = new ArrayList<IslandOrderControlDetail>();	//批量保存海岛游订单控房明细
			List<IslandOrderControlDetail> updateList = new ArrayList<IslandOrderControlDetail>();	//批量修改海岛游订单控房明细
			for(int k = 0 ; k < orderControlArr.size(); k++){
				JSONObject controlDetail = (JSONObject)orderControlArr.get(k);
				
				IslandOrderControlDetail islandOrderControlDetail = new IslandOrderControlDetail();
				String islandOrderControlDetailUuid = controlDetail.getString("islandOrderControlDetailUuid");//海岛游订单控房明细表uuid
				//修改列表
				if(StringUtils.isNotEmpty(islandOrderControlDetailUuid)){
					islandOrderControlDetail = islandOrderControlDetailDao.getByUuid(islandOrderControlDetailUuid);
					islandOrderControlDetail.setNum(controlDetail.getInt("number"));
					
					super.setOptInfo(islandOrderControlDetail, BaseService.OPERATION_UPDATE);
					updateList.add(islandOrderControlDetail);
				}else{
					islandOrderControlDetail.setUuid(UuidUtils.generUuid());	//uuid
					islandOrderControlDetail.setOrderUuid(orderUuid);		//订单uuid
					islandOrderControlDetail.setHotelControlDetailUuid(controlDetail.getString("hotelControlDetailUuid"));//酒店控房明细uuid
					islandOrderControlDetail.setNum(controlDetail.getInt("number"));
					
					super.setOptInfo(islandOrderControlDetail, BaseService.OPERATION_ADD);
					saveList.add(islandOrderControlDetail);
				}
			}
			//批量保存
			if(CollectionUtils.isNotEmpty(saveList)){
				islandOrderControlDetailDao.batchSave(saveList);
			}
			//批量修改
			if(CollectionUtils.isNotEmpty(updateList)){
				islandOrderControlDetailDao.batchUpdate(updateList);
			}
		}
	}
	
	
	/**
	 * 
	 * @param moneyArr  	
	 * @param orderUuid	订单uuid	
	 * @param serialNum 流水号
	 */
	private  void saveOrderMoney(JSONArray moneyArr, String orderUuid, String serialNum, Integer moneyType, String oldUuid) {
		if(moneyArr != null && moneyArr.size() > 0){
			List<IslandMoneyAmount> saveMoneyAmountList = new ArrayList<IslandMoneyAmount>();	//批量保存金额
			for(int i = 0 ; i < moneyArr.size(); i++){
				JSONObject moneyJson = (JSONObject)moneyArr.get(i);
				
				IslandMoneyAmount islandMoneyAmount  = new IslandMoneyAmount();
				
				@SuppressWarnings("rawtypes")
				Iterator it = moneyJson.keys();
				while(it.hasNext()){
					String key = (String)it.next();
					Double amount = moneyJson.getDouble(String.valueOf(key));
					
					islandMoneyAmount.setUuid(UuidUtils.generUuid());//uuid
					islandMoneyAmount.setCurrencyId(Integer.valueOf(key));	//币种ID
					islandMoneyAmount.setAmount(amount);	//金额
					islandMoneyAmount.setBusinessUuid(orderUuid);//订单uuid
					islandMoneyAmount.setSerialNum(serialNum);//流水号
					islandMoneyAmount.setBusinessType(1);
					islandMoneyAmount.setBusinessUuid(orderUuid);
					islandMoneyAmount.setMoneyType(moneyType);
					islandMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
					
					List<IslandMoneyAmount> exsitMaList = islandMoneyAmountDao.findAmountBySerialNumAndCurrencyId(oldUuid, Integer.valueOf(key));
					if (exsitMaList != null && exsitMaList.size() > 0) {
						islandMoneyAmount.setId(exsitMaList.get(0).getId());
						islandMoneyAmount.setExchangerate(exsitMaList.get(0).getExchangerate());
					} else {
						Currency currency = currencyDao.findOne(Long.parseLong(islandMoneyAmount.getCurrencyId().toString()));
						islandMoneyAmount.setExchangerate(currency.getConvertLowest().doubleValue());
					}
					
					super.setOptInfo(islandMoneyAmount, BaseService.OPERATION_ADD);
					saveMoneyAmountList.add(islandMoneyAmount);
				}
			}
			islandMoneyAmountDao.batchSave(saveMoneyAmountList);
		}
	}
			
			
	/**
	 * 保存或者修改订单联系人信息
	 * @param contactsArr
	 * @param orderid
	 */
	private  void saveOrUpdateContacts(JSONArray contactsArr,Long orderid){
		if(contactsArr != null && contactsArr.size() > 0){
			List<OrderContacts> saveContactsList = new ArrayList<OrderContacts>();	//批量保存联系人
			List<OrderContacts> updateContactsList = new ArrayList<OrderContacts>();//批量更新操作
			List<OrderContacts> deleteContactsList = new ArrayList<OrderContacts>();//批量删除操作
			List<OrderContacts> allContactsList = orderContactsService.findOrderContactsByOrderIdAndOrderType(orderid, Context.ORDER_TYPE_ISLAND);//批量删除操作

			for(int i = 0 ; i < contactsArr.size(); i++){
				JSONObject contacts = (JSONObject)contactsArr.get(i);
				
				OrderContacts orderContacts = new OrderContacts();
				String contactsId = contacts.getString("contactsId");
				if(StringUtils.isNotEmpty(contactsId)){
					orderContacts = orderContactsDao.getById(contacts.getLong("contactsId"));
				}
				
				orderContacts.setOrderId(orderid);	//订单id
				orderContacts.setOrderType(Context.ORDER_TYPE_ISLAND);	//订单类型
				orderContacts.setContactsName(contacts.getString("contactsName"));//联系人
				orderContacts.setContactsTel(contacts.getString("contactsTel")); //联系电话
				orderContacts.setContactsTixedTel(contacts.getString("contactsTixedTel"));//固定电话
				orderContacts.setContactsAddress(contacts.getString("contactsAddress"));	//渠道地址
				orderContacts.setContactsFax(contacts.getString("contactsFax"));	//传真
				orderContacts.setContactsQQ(contacts.getString("contactsQQ"));	//QQ
				orderContacts.setContactsEmail(contacts.getString("contactsEmail"));	//Email
				orderContacts.setContactsZipCode(contacts.getString("contactsZipCode"));	//渠道邮编
				orderContacts.setRemark(contacts.getString("remark"));	//其他
				
				//如果主键不为空,则是修改操作
				if(StringUtils.isNotEmpty(contactsId)){
					updateContactsList.add(orderContacts);
				}else{
					saveContactsList.add(orderContacts);
				}
			}

			//获取原订单联系人中不更新的记录，作为要删除的记录
			allContactsList.removeAll(updateContactsList);
			deleteContactsList = allContactsList;

			orderContactsDao.batchSave(saveContactsList);
			orderContactsDao.batchUpdate(updateContactsList);
			orderContactsDao.batchDelete(deleteContactsList);
		}
	}
	
	/**
	 * 保存或者修改订单价格(费用调整)
	 * @param moneyChangeArr
	 * @param orderUuid
	 */
	private void saveOrUpdateOrderPrice(JSONArray moneyChangeArr,String orderUuid){
		if(moneyChangeArr != null && moneyChangeArr.size() > 0){
			List<IslandOrderPrice> saveOrderPriceList = new ArrayList<IslandOrderPrice>();	//批量保存订单价格
			List<IslandOrderPrice> updateOrderPriceList = new ArrayList<IslandOrderPrice>();//批量更新订单价格
			for(int i = 0 ; i < moneyChangeArr.size(); i++){
				JSONObject orderPrice = (JSONObject)moneyChangeArr.get(i);
				
				IslandOrderPrice islandOrderPrice = new IslandOrderPrice();
				String orderPriceUuid = orderPrice.getString("orderPriceUuid");
				if(StringUtils.isNotEmpty(orderPriceUuid)){
					islandOrderPrice = islandOrderPriceDao.getByUuid(orderPriceUuid);
				}
				
				islandOrderPrice.setUuid(orderPrice.getString("orderPriceUuid"));	//uuid
				islandOrderPrice.setOrderUuid(orderUuid);   		 	//订单Uuid
				islandOrderPrice.setPriceType(orderPrice.getInt("priceType"));	//价格类型
				islandOrderPrice.setCurrencyId(orderPrice.getInt("currencyId"));	//币种
				islandOrderPrice.setPrice(orderPrice.getDouble("orderPrice"));	//价格
				islandOrderPrice.setRemark(orderPrice.getString("orderPriceRemark"));	//备注
				islandOrderPrice.setPriceName(orderPrice.getString("priceName"));	//金额名称
				
				if(StringUtils.isNotEmpty(orderPriceUuid)){
					super.setOptInfo(islandOrderPrice, BaseService.OPERATION_UPDATE);
					updateOrderPriceList.add(islandOrderPrice);
				}else{
					super.setOptInfo(islandOrderPrice, BaseService.OPERATION_ADD);
					islandOrderPrice.setUuid(UuidUtils.generUuid());
					saveOrderPriceList.add(islandOrderPrice);
				}
			}
			islandOrderPriceDao.batchSave(saveOrderPriceList);
			islandOrderPriceDao.batchUpdate(updateOrderPriceList);
		}
	}
	
	/**
	 * 批量保存游客签证信息
	 * @param visaArr		游客签证信息jsonArr
	 * @param orderUuid		订单uuid
	 * @param travelerUuid	游客uuid
	 */
	private void saveOrUpdateTravelerVisa(JSONArray visaArr,String orderUuid,String travelerUuid){
		//签证信息
		if(visaArr != null && visaArr.size() > 0){
			List<IslandTravelervisa> saveVisaList = new ArrayList<IslandTravelervisa>();	//批量保存签证信息
			List<IslandTravelervisa> updateVisaList = new ArrayList<IslandTravelervisa>();	//批量修改签证信息
			for(int k = 0 ; k < visaArr.size(); k++){
				JSONObject travelervisa = (JSONObject)visaArr.get(k);
				
				IslandTravelervisa islandTravelervisa = new IslandTravelervisa();
				String travelerVisaUuid = travelervisa.getString("travelerVisaUuid");//海岛游游客签证信息表uuid
				//修改列表
				if(StringUtils.isNotEmpty(travelerVisaUuid)){
					islandTravelervisa = islandTravelervisaDao.getByUuid(travelerVisaUuid);
					islandTravelervisa.setCountry(travelervisa.getString("country"));	//签证国家
					islandTravelervisa.setVisaTypeId(travelervisa.getInt("visaType"));	//签证类型
					
					super.setOptInfo(islandTravelervisa, BaseService.OPERATION_UPDATE);
					updateVisaList.add(islandTravelervisa);
				}else{
					islandTravelervisa.setUuid(UuidUtils.generUuid());	//uuid
					islandTravelervisa.setIslandOrderUuid(orderUuid);		//订单uuid
					islandTravelervisa.setIslandTravelerUuid(travelerUuid);	//游客uuid
					islandTravelervisa.setCountry(travelervisa.getString("country"));	//签证国家
					islandTravelervisa.setVisaTypeId(travelervisa.getInt("visaType"));	//签证类型
					
					super.setOptInfo(islandTravelervisa, BaseService.OPERATION_ADD);
					saveVisaList.add(islandTravelervisa);
				}
			}
			//批量保存
			if(CollectionUtils.isNotEmpty(saveVisaList)){
				islandTravelervisaDao.batchSave(saveVisaList);
			}
			//批量修改
			if(CollectionUtils.isNotEmpty(updateVisaList)){
				islandTravelervisaDao.batchUpdate(updateVisaList);
			}
		}
	}
	
	/**
	 * 批量添加或者修改游客证件类型
	 * @param visaArr
	 * @param orderUuid		订单uuid
	 * @param travelerUuid	游客uuid
	 */
	private void saveOrUpdateTravelerPapersType(JSONArray papersTypeArr,String orderUuid,String travelerUuid){
		if(papersTypeArr != null && papersTypeArr.size() > 0){
			List<IslandTravelerPapersType> savetravelerPapersTypeList = new ArrayList<IslandTravelerPapersType>();	//批量保存证件类型信息
			List<IslandTravelerPapersType> updatetravelerPapersTypeList = new ArrayList<IslandTravelerPapersType>();	//批量修改证件类型信息
			for(int i = 0 ; i < papersTypeArr.size(); i++){
				JSONObject travelerPapersType = (JSONObject)papersTypeArr.get(i);
				
				IslandTravelerPapersType islandTravelerPapersType = new IslandTravelerPapersType();
				String travelerPapersUuid = travelerPapersType.getString("travelerPapersUuid");	//uuid
				
				if(StringUtils.isNotEmpty(travelerPapersUuid)){
					islandTravelerPapersType = islandTravelerPapersTypeDao.getByUuid(travelerPapersUuid);
					islandTravelerPapersType.setPapersType(travelerPapersType.getInt("papersType"));	//证件类型
					islandTravelerPapersType.setIdCard(travelerPapersType.getString("idCard"));			//证件号码
					islandTravelerPapersType.setValidityDateString(travelerPapersType.getString("validityDate"));//有效期
					
					super.setOptInfo(islandTravelerPapersType, BaseService.OPERATION_UPDATE);
					updatetravelerPapersTypeList.add(islandTravelerPapersType);
				}else{
					islandTravelerPapersType.setUuid(UuidUtils.generUuid());//uuid
					islandTravelerPapersType.setOrderUuid(orderUuid);	//订单uuid
					islandTravelerPapersType.setIslandTravelerUuid(travelerUuid);//游客uuid
					islandTravelerPapersType.setPapersType(travelerPapersType.getInt("papersType"));	//证件类型
					islandTravelerPapersType.setIdCard(travelerPapersType.getString("idCard"));			//证件号码
					islandTravelerPapersType.setValidityDateString(travelerPapersType.getString("validityDate"));//有效期
					
					super.setOptInfo(islandTravelerPapersType, BaseService.OPERATION_ADD);
					savetravelerPapersTypeList.add(islandTravelerPapersType);
				}
			}
			//批量保存
			if(CollectionUtils.isNotEmpty(savetravelerPapersTypeList)){
				islandTravelerPapersTypeDao.batchSave(savetravelerPapersTypeList);
			}
			//批量修改
			if(CollectionUtils.isNotEmpty(updatetravelerPapersTypeList)){
				islandTravelerPapersTypeDao.batchUpdate(updatetravelerPapersTypeList);
			}
		}
	}
	
	/**
	 * 批量保存或者修改游客金额信息
	 * @param travelerMoneyArr
	 * @param orderUuid
	 * @param travelerUuid
	 */
	private void saveOrUpdateTravelerMoneyAmount(JSONArray travelerMoneyArr,String orderUuid,String travelerUuid, IslandTraveler islandTraveler){
		if(travelerMoneyArr != null && travelerMoneyArr.size() > 0){
			List<IslandMoneyAmount> saveMoneyAmountList = new ArrayList<IslandMoneyAmount>();	//批量保存金额信息
			List<IslandMoneyAmount> updateMoneyAmountList = new ArrayList<IslandMoneyAmount>();//批量修改金额信息
			for(int i = 0 ; i < travelerMoneyArr.size(); i++){
				JSONObject travelerMoney = (JSONObject)travelerMoneyArr.get(i);
				
				IslandMoneyAmount islandMoneyAmount = new IslandMoneyAmount();
				String travelerMoneyUuid = travelerMoney.getString("travelerMoneyUuid");	//uuid
				
				String money = travelerMoney.getString("travelerMoney");
				if (StringUtils.isBlank(money)) {
					money = "0.00";
				}
				
				if(StringUtils.isNotEmpty(travelerMoneyUuid)){
					islandMoneyAmount = islandMoneyAmountDao.getByUuid(travelerMoneyUuid);
					islandMoneyAmount.setCurrencyId(travelerMoney.getInt("currencyId"));	//币种
					islandMoneyAmount.setAmount(new Double(money));	//金额
					//这条moneyamount记录是游客的结算价，所以serialNum应该对应游客的payPriceSerialNum，况且它的moneyType=14
					islandMoneyAmount.setSerialNum(islandTraveler.getPayPriceSerialNum());
					super.setOptInfo(islandMoneyAmount, BaseService.OPERATION_UPDATE);
					updateMoneyAmountList.add(islandMoneyAmount);
				}else{
					
					for (int j=0; j<3; j++) {
						islandMoneyAmount = new IslandMoneyAmount();
						islandMoneyAmount.setUuid(UuidUtils.generUuid());
						islandMoneyAmount.setCurrencyId(travelerMoney.getInt("currencyId"));	//币种
						islandMoneyAmount.setAmount(new Double(money));	//金额
						islandMoneyAmount.setBusinessUuid(travelerUuid);//游客uuid
						islandMoneyAmount.setBusinessType(2);	//业务类型
						//保存金额类型：原始结算价、结算价、成本价
						if (j == 0) {
							islandMoneyAmount.setMoneyType(Context.MONEY_TYPE_YSJSJ);
							islandMoneyAmount.setSerialNum(islandTraveler.getOriginalPayPriceSerialNum());
						} else if (j == 1) {
							islandMoneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);
							islandMoneyAmount.setSerialNum(islandTraveler.getPayPriceSerialNum());
						} else {
							islandMoneyAmount.setMoneyType(Context.MONEY_TYPE_CBJ);
							islandMoneyAmount.setSerialNum(islandTraveler.getCostPriceSerialNum());
						}
						
						super.setOptInfo(islandMoneyAmount, BaseService.OPERATION_ADD);
						saveMoneyAmountList.add(islandMoneyAmount);
					}
				}
			}
			//批量保存
			if(CollectionUtils.isNotEmpty(saveMoneyAmountList)){
				islandMoneyAmountDao.batchSave(saveMoneyAmountList);
			}
			//批量修改
			if(CollectionUtils.isNotEmpty(updateMoneyAmountList)){
				islandMoneyAmountDao.batchUpdate(updateMoneyAmountList);
			}
			
			//保存游客
			islandTravelerDao.saveObj(islandTraveler);
		}
	}
	
	/**
	 * 保存附件信息
	 * @param fileArr
	 * @param travelerUuid
	 */
	private void saveFile(JSONArray fileArr,String travelerUuid){
		if(fileArr != null && fileArr.size() > 0){
			List<HotelAnnex> saveHotelAnnexList = new ArrayList<HotelAnnex>();	//批量保存附件
			for(int i = 0 ; i < fileArr.size(); i++){
				JSONObject hotelAnnexJson = (JSONObject)fileArr.get(i);
				
				HotelAnnex hotelAnnex = new HotelAnnex();
				
				hotelAnnex.setUuid(UuidUtils.generUuid());	//uuid
				hotelAnnex.setMainUuid(travelerUuid);	//业务主表ID
				hotelAnnex.setDocId(hotelAnnexJson.getInt("docId"));		//附件表ID
				hotelAnnex.setDocName(hotelAnnexJson.getString("docName"));	//文件名称
				hotelAnnex.setDocPath(hotelAnnexJson.getString("docPath")); //文件路径
				
				super.setOptInfo(hotelAnnex, BaseService.OPERATION_ADD);
				saveHotelAnnexList.add(hotelAnnex);
			}
			//批量保存
			if(CollectionUtils.isNotEmpty(saveHotelAnnexList)){
				hotelAnnexDao.batchSave(saveHotelAnnexList);
			}
		}
	}
	
	@Override
	public List<IslandOrder> getByIslandUuid(String islandUuid) {
		String sql = "select DISTINCT o.createBy from island_order o where o.activity_island_uuid = ?";
		return islandOrderDao.findBySql(sql, Map.class, islandUuid);
	}
	
	/**
	 * 获取订单基本信息
	 * @param orderUuid
	 * @param model
	 */
	public void getOrderBaseInfo(String orderUuid, Model model) {
		//获取订单信息
		IslandOrder islandOrder = getByUuid(orderUuid);
		//获取产品信息
		ActivityIsland activityIsland = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
		//获取产品团期
		ActivityIslandGroup activityIslandGroup = activityIslandGroupService.getByUuid(islandOrder.getActivityIslandGroupUuid());
		//获取酒店信息
		Hotel hotel = hotelService.getByUuid(activityIsland.getHotelUuid());
		Integer hotelLevel = null;
		if (hotel != null) {
			HotelStar hotelStar = hotelStarService.getByUuid(hotel.getStar());
			if (hotelStar != null) {
				hotelLevel = hotelStar.getValue();
			}
		}
		//加载团期下所有的基础餐型数据
		List<ActivityIslandGroupMeal> groupMeals = activityIslandGroupMealService.getMealListByGroupUuid(activityIslandGroup.getUuid());
		//加载团期下所有的房型数据
		List<ActivityIslandGroupRoom> groupRooms = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityIslandGroup.getUuid());
		
		//加载团期下所有的参考航班数据
		List<ActivityIslandGroupAirline> groupAirlines = activityIslandGroupAirlineService.getByactivityIslandGroup(activityIslandGroup.getUuid());
		//初始化团期余位数
		activityIslandGroup.setRemNumber(activityIslandGroupService.getRemNumberByGroupAirlineList(groupAirlines));
		//加载舱位等级数据集(去重后的数据)
		List<ActivityIslandGroupAirline> groupAirlineSpaceLevels = new ArrayList<ActivityIslandGroupAirline>();
		//舱位等级去重操作
		List<String> spaceLevelKeys = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(groupAirlines)) {
			for(ActivityIslandGroupAirline groupAirline : groupAirlines) {
				if(!spaceLevelKeys.contains(groupAirline.getSpaceLevel())) {
					groupAirlineSpaceLevels.add(groupAirline);
					spaceLevelKeys.add(groupAirline.getSpaceLevel());
				}
			}
		}
		if(groupAirlines != null && groupAirlines.size() > 0) {
			//加载所有余位数
			ActivityIslandGroupAirline activityIslandGroupAirline = groupAirlines.get(0);
			model.addAttribute("groupAirline", groupAirlines.get(0));
			//查询航班
			List<AirlineInfo> airlineInfoList = airlineInfoDao.findByCompanyIdAndAirlineCodeAndFiightNumber(UserUtils.getUser().getCompany().getId(), 
					activityIslandGroupAirline.getAirline(), activityIslandGroupAirline.getFlightNumber());
			if (CollectionUtils.isNotEmpty(airlineInfoList)) {
				model.addAttribute("airlineInfo", airlineInfoList.get(0));
			}
		}
		Integer orderNum = getBookingPersonNum(activityIslandGroup.getUuid());
		activityIslandGroup.setOrderNum(orderNum);
		
		//获取费用及人数
		List<IslandOrderPrice> groupPrices = Lists.newArrayList();
		
		//获取订单价格列表
		List<IslandOrderPrice> islandOrderPriceList = islandOrderPriceService.getByOrderUuid(orderUuid);
		if (CollectionUtils.isNotEmpty(islandOrderPriceList)) {
			for (IslandOrderPrice islandOrderPrice : islandOrderPriceList) {
				//获取团期价格
				if (islandOrderPrice != null && "1".equals(islandOrderPrice.getPriceType().toString())) {
					int num = islandOrderPrice.getNum() == null ? 0:islandOrderPrice.getNum();	//人数
					Double price = islandOrderPrice.getPrice() == null ? 0 : islandOrderPrice.getPrice();	//价钱
					Double total = price * num;		//小计
					islandOrderPrice.setSubTotal(new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP));	//设置小计金额
					islandOrderPrice.setSpaceLevel(DictUtils.getDictLabel(islandOrderPrice.getSpaceLevel(), "spaceGrade_Type", ""));
					groupPrices.add(islandOrderPrice);
				}
			}
		}
		
		//获取订单总额(成本价)
		String costMoneyStr =  islandMoneyAmountService.getMoneyStr(islandOrder.getCostMoney(), true);
		//获取订单结算总额(结算价,应收金额)
		String totalMoneyStr = islandMoneyAmountService.getMoneyStr(islandOrder.getTotalMoney(), true);
		//获取订单已付金额(已付金额)
		String payedMoneyStr = islandMoneyAmountService.getMoneyStr(islandOrder.getPayedMoney(), true);
		//订单未收金额( totalMoneyStr - payedMoneyStr  )
		String noPayMoneyStr = "";
		if(!StringUtils.isEmpty(totalMoneyStr) && !StringUtils.isEmpty(payedMoneyStr) ){
			 noPayMoneyStr =  islandMoneyAmountService.addOrSubtract(totalMoneyStr.replace(",", "").trim(),
					 payedMoneyStr.replace(",", "").trim(), false);
			 if (StringUtils.isNotBlank(noPayMoneyStr) && noPayMoneyStr.split(" ").length == 2) {
				 DecimalFormat d = new DecimalFormat(",##0.00");
				 noPayMoneyStr = noPayMoneyStr.split(" ")[0] + " " + d.format(new BigDecimal(noPayMoneyStr.split(" ")[1]));
			 }
		}
		
		//订单基本信息
		model.addAttribute("islandOrder", islandOrder);
		model.addAttribute("activityIsland", activityIsland);
		model.addAttribute("activityIslandGroup", activityIslandGroup);
		model.addAttribute("groupMeals", groupMeals);
		model.addAttribute("groupRooms", groupRooms);
		model.addAttribute("hotelLevel", hotelLevel);
		//费用及人数
		model.addAttribute("groupPrices", groupPrices);
		//费用结算信息
		model.addAttribute("costMoneyStr", costMoneyStr);
		model.addAttribute("totalMoneyStr", totalMoneyStr);
		model.addAttribute("payedMoneyStr", payedMoneyStr);
		model.addAttribute("noPayMoneyStr", noPayMoneyStr);
	}
}
