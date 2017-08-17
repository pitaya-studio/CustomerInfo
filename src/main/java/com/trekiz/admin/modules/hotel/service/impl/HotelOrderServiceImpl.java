/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupPriceDao;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.dao.HotelMoneyAmountDao;
import com.trekiz.admin.modules.hotel.dao.HotelOrderDao;
import com.trekiz.admin.modules.hotel.dao.HotelOrderPriceDao;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerDao;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerPapersTypeDao;
import com.trekiz.admin.modules.hotel.dao.HotelTravelervisaDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelOrderPrice;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerPapersType;
import com.trekiz.admin.modules.hotel.entity.HotelTravelervisa;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.input.HotelOrderInput;
import com.trekiz.admin.modules.hotel.query.HotelOrderQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupPriceService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupRoomService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelControlService;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.hotel.service.HotelOrderPriceService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.pay.dao.PayHotelOrderDao;
import com.trekiz.admin.modules.pay.entity.PayHotelOrder;
import com.trekiz.admin.modules.pay.service.PayHotelOrderService;
import com.trekiz.admin.modules.pay.service.ProductMoneyAmountService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierContactsView;
import com.trekiz.admin.modules.supplier.service.SupplierContactsService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelOrderServiceImpl  extends BaseService implements HotelOrderService{
	@Autowired
    private SystemService systemService;
	@Autowired
	private PayHotelOrderService payHotelOrderService;
	@Autowired
    private ReviewService reviewService;
	@Autowired
	private HotelOrderDao hotelOrderDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private PayHotelOrderDao payHotelOrderDao;
	@Autowired
	private ProductMoneyAmountService productMoneyAmountService;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private ActivityHotelDao activityHotelDao;
	@Autowired
	private ActivityHotelGroupDao activityHotelGroupDao;
	@Autowired
	private ActivityHotelGroupPriceDao activityHotelGroupPriceDao;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private ActivityHotelGroupMealService activityHotelGroupMealService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private HotelTravelervisaDao hotelTravelervisaDao;
	@Autowired
	private HotelTravelerPapersTypeDao hotelTravelerPapersTypeDao;
	@Autowired
	private HotelAnnexDao hotelAnnexDao;
	@Autowired
	private OrderContactsDao orderContactsDao;
	@Autowired
	private HotelOrderPriceDao hotelOrderPriceDao;
	@Autowired
	private HotelTravelerDao hotelTravelerDao;
	@Autowired
	private HotelMoneyAmountDao hotelMoneyAmountDao;
	@Autowired
	private ActivityHotelGroupRoomService activityHotelGroupRoomService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupPriceService activityHotelGroupPriceService;
	@Autowired
	private HotelOrderPriceService hotelOrderPriceService;
	@Autowired
	private HotelStarService hotelStarService;
	@Autowired
	private ActivityHotelServiceImpl activityHotelServiceImpl;
	@Autowired
	private HotelControlService hotelControlService;
	@Autowired
    private SupplierContactsService supplierContactsService;
	@Autowired
	private OrderContactsService orderContactsService;
	
	public void save (HotelOrder hotelOrder){
		super.setOptInfo(hotelOrder, BaseService.OPERATION_ADD);
		hotelOrderDao.saveObj(hotelOrder);
	}
	
	public void update (HotelOrder hotelOrder){
		super.setOptInfo(hotelOrder, BaseService.OPERATION_UPDATE);
		hotelOrderDao.updateObj(hotelOrder);
	}
	
	public HotelOrder getById(java.lang.Integer value) {
		return hotelOrderDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelOrder obj = hotelOrderDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelOrder> find(Page<HotelOrder> page, HotelOrderQuery hotelOrderQuery) {
		DetachedCriteria dc = hotelOrderDao.createDetachedCriteria();
		
	   	if(hotelOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelOrderQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", hotelOrderQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", hotelOrderQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOrderNum())){
			dc.add(Restrictions.eq("orderNum", hotelOrderQuery.getOrderNum()));
		}
	   	if(hotelOrderQuery.getOrderStatus()!=null){
	   		dc.add(Restrictions.eq("orderStatus", hotelOrderQuery.getOrderStatus()));
	   	}
	   	if(hotelOrderQuery.getOrderCompany()!=null){
	   		dc.add(Restrictions.eq("orderCompany", hotelOrderQuery.getOrderCompany()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOrderCompanyName())){
			dc.add(Restrictions.eq("orderCompanyName", hotelOrderQuery.getOrderCompanyName()));
		}
	   	if(hotelOrderQuery.getOrderSalerId()!=null){
	   		dc.add(Restrictions.eq("orderSalerId", hotelOrderQuery.getOrderSalerId()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOrderPersonName())){
			dc.add(Restrictions.eq("orderPersonName", hotelOrderQuery.getOrderPersonName()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOrderPersonPhoneNum())){
			dc.add(Restrictions.eq("orderPersonPhoneNum", hotelOrderQuery.getOrderPersonPhoneNum()));
		}
		if(hotelOrderQuery.getOrderTime()!=null){
			dc.add(Restrictions.eq("orderTime", hotelOrderQuery.getOrderTime()));
		}
	   	if(hotelOrderQuery.getOrderPersonNum()!=null){
	   		dc.add(Restrictions.eq("orderPersonNum", hotelOrderQuery.getOrderPersonNum()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getFrontMoney())){
			dc.add(Restrictions.eq("frontMoney", hotelOrderQuery.getFrontMoney()));
		}
	   	if(hotelOrderQuery.getPayStatus()!=null){
	   		dc.add(Restrictions.eq("payStatus", hotelOrderQuery.getPayStatus()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getPayedMoney())){
			dc.add(Restrictions.eq("payedMoney", hotelOrderQuery.getPayedMoney()));
		}
	   	if(hotelOrderQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", hotelOrderQuery.getPayType()));
	   	}
	   	if(hotelOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelOrderQuery.getCreateBy()));
	   	}
		if(hotelOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelOrderQuery.getCreateDate()));
		}
	   	if(hotelOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelOrderQuery.getUpdateBy()));
	   	}
		if(hotelOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelOrderQuery.getDelFlag()));
		}
	   	if(hotelOrderQuery.getChangeGroupId()!=null){
	   		dc.add(Restrictions.eq("changeGroupId", hotelOrderQuery.getChangeGroupId()));
	   	}
	   	if(hotelOrderQuery.getGroupChangeType()!=null){
	   		dc.add(Restrictions.eq("groupChangeType", hotelOrderQuery.getGroupChangeType()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getCostMoney())){
			dc.add(Restrictions.eq("costMoney", hotelOrderQuery.getCostMoney()));
		}
	   	if(hotelOrderQuery.getAsAcountType()!=null){
	   		dc.add(Restrictions.eq("asAcountType", hotelOrderQuery.getAsAcountType()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getAccountedMoney())){
			dc.add(Restrictions.eq("accountedMoney", hotelOrderQuery.getAccountedMoney()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getPayDeposit())){
			dc.add(Restrictions.eq("payDeposit", hotelOrderQuery.getPayDeposit()));
		}
	   	if(hotelOrderQuery.getPlaceHolderType()!=null){
	   		dc.add(Restrictions.eq("placeHolderType", hotelOrderQuery.getPlaceHolderType()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getSingleDiff())){
			dc.add(Restrictions.eq("singleDiff", hotelOrderQuery.getSingleDiff()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getCancelDescription())){
			dc.add(Restrictions.eq("cancelDescription", hotelOrderQuery.getCancelDescription()));
		}
	   	if(hotelOrderQuery.getIsPayment()!=null){
	   		dc.add(Restrictions.eq("isPayment", hotelOrderQuery.getIsPayment()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getPayMode())){
			dc.add(Restrictions.eq("payMode", hotelOrderQuery.getPayMode()));
		}
	   	if(hotelOrderQuery.getRemainDays()!=null){
	   		dc.add(Restrictions.eq("remainDays", hotelOrderQuery.getRemainDays()));
	   	}
		if(hotelOrderQuery.getActivationDate()!=null){
			dc.add(Restrictions.eq("activationDate", hotelOrderQuery.getActivationDate()));
		}
	   	if(hotelOrderQuery.getLockStatus()!=null){
	   		dc.add(Restrictions.eq("lockStatus", hotelOrderQuery.getLockStatus()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getSpecialDemand())){
			dc.add(Restrictions.eq("specialDemand", hotelOrderQuery.getSpecialDemand()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getTotalMoney())){
			dc.add(Restrictions.eq("totalMoney", hotelOrderQuery.getTotalMoney()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getFileIds())){
			dc.add(Restrictions.eq("fileIds", hotelOrderQuery.getFileIds()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOriginalTotalMoney())){
			dc.add(Restrictions.eq("originalTotalMoney", hotelOrderQuery.getOriginalTotalMoney()));
		}
	   	if(hotelOrderQuery.getIsAfterSupplement()!=null){
	   		dc.add(Restrictions.eq("isAfterSupplement", hotelOrderQuery.getIsAfterSupplement()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOriginalFrontMoney())){
			dc.add(Restrictions.eq("originalFrontMoney", hotelOrderQuery.getOriginalFrontMoney()));
		}
	   	if(hotelOrderQuery.getPaymentType()!=null){
	   		dc.add(Restrictions.eq("paymentType", hotelOrderQuery.getPaymentType()));
	   	}
	   	if(hotelOrderQuery.getForecaseReportNum()!=null){
	   		dc.add(Restrictions.eq("forecaseReportNum", hotelOrderQuery.getForecaseReportNum()));
	   	}
	   	if(hotelOrderQuery.getSubControlNum()!=null){
	   		dc.add(Restrictions.eq("subControlNum", hotelOrderQuery.getSubControlNum()));
	   	}
	   	if(hotelOrderQuery.getSubUnControlNum()!=null){
	   		dc.add(Restrictions.eq("subUnControlNum", hotelOrderQuery.getSubUnControlNum()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getRemark())){
			dc.add(Restrictions.eq("remark", hotelOrderQuery.getRemark()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getBookingStatus())){
			dc.add(Restrictions.eq("bookingStatus", hotelOrderQuery.getBookingStatus()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelOrderDao.find(page, dc);
	}
	
	public List<HotelOrder> find( HotelOrderQuery hotelOrderQuery) {
		DetachedCriteria dc = hotelOrderDao.createDetachedCriteria();
		
	   	if(hotelOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelOrderQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", hotelOrderQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", hotelOrderQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOrderNum())){
			dc.add(Restrictions.eq("orderNum", hotelOrderQuery.getOrderNum()));
		}
	   	if(hotelOrderQuery.getOrderStatus()!=null){
	   		dc.add(Restrictions.eq("orderStatus", hotelOrderQuery.getOrderStatus()));
	   	}
	   	if(hotelOrderQuery.getOrderCompany()!=null){
	   		dc.add(Restrictions.eq("orderCompany", hotelOrderQuery.getOrderCompany()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOrderCompanyName())){
			dc.add(Restrictions.eq("orderCompanyName", hotelOrderQuery.getOrderCompanyName()));
		}
	   	if(hotelOrderQuery.getOrderSalerId()!=null){
	   		dc.add(Restrictions.eq("orderSalerId", hotelOrderQuery.getOrderSalerId()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOrderPersonName())){
			dc.add(Restrictions.eq("orderPersonName", hotelOrderQuery.getOrderPersonName()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOrderPersonPhoneNum())){
			dc.add(Restrictions.eq("orderPersonPhoneNum", hotelOrderQuery.getOrderPersonPhoneNum()));
		}
		if(hotelOrderQuery.getOrderTime()!=null){
			dc.add(Restrictions.eq("orderTime", hotelOrderQuery.getOrderTime()));
		}
	   	if(hotelOrderQuery.getOrderPersonNum()!=null){
	   		dc.add(Restrictions.eq("orderPersonNum", hotelOrderQuery.getOrderPersonNum()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getFrontMoney())){
			dc.add(Restrictions.eq("frontMoney", hotelOrderQuery.getFrontMoney()));
		}
	   	if(hotelOrderQuery.getPayStatus()!=null){
	   		dc.add(Restrictions.eq("payStatus", hotelOrderQuery.getPayStatus()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getPayedMoney())){
			dc.add(Restrictions.eq("payedMoney", hotelOrderQuery.getPayedMoney()));
		}
	   	if(hotelOrderQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", hotelOrderQuery.getPayType()));
	   	}
	   	if(hotelOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelOrderQuery.getCreateBy()));
	   	}
		if(hotelOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelOrderQuery.getCreateDate()));
		}
	   	if(hotelOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelOrderQuery.getUpdateBy()));
	   	}
		if(hotelOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelOrderQuery.getDelFlag()));
		}
	   	if(hotelOrderQuery.getChangeGroupId()!=null){
	   		dc.add(Restrictions.eq("changeGroupId", hotelOrderQuery.getChangeGroupId()));
	   	}
	   	if(hotelOrderQuery.getGroupChangeType()!=null){
	   		dc.add(Restrictions.eq("groupChangeType", hotelOrderQuery.getGroupChangeType()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getCostMoney())){
			dc.add(Restrictions.eq("costMoney", hotelOrderQuery.getCostMoney()));
		}
	   	if(hotelOrderQuery.getAsAcountType()!=null){
	   		dc.add(Restrictions.eq("asAcountType", hotelOrderQuery.getAsAcountType()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getAccountedMoney())){
			dc.add(Restrictions.eq("accountedMoney", hotelOrderQuery.getAccountedMoney()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getPayDeposit())){
			dc.add(Restrictions.eq("payDeposit", hotelOrderQuery.getPayDeposit()));
		}
	   	if(hotelOrderQuery.getPlaceHolderType()!=null){
	   		dc.add(Restrictions.eq("placeHolderType", hotelOrderQuery.getPlaceHolderType()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getSingleDiff())){
			dc.add(Restrictions.eq("singleDiff", hotelOrderQuery.getSingleDiff()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getCancelDescription())){
			dc.add(Restrictions.eq("cancelDescription", hotelOrderQuery.getCancelDescription()));
		}
	   	if(hotelOrderQuery.getIsPayment()!=null){
	   		dc.add(Restrictions.eq("isPayment", hotelOrderQuery.getIsPayment()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getPayMode())){
			dc.add(Restrictions.eq("payMode", hotelOrderQuery.getPayMode()));
		}
	   	if(hotelOrderQuery.getRemainDays()!=null){
	   		dc.add(Restrictions.eq("remainDays", hotelOrderQuery.getRemainDays()));
	   	}
		if(hotelOrderQuery.getActivationDate()!=null){
			dc.add(Restrictions.eq("activationDate", hotelOrderQuery.getActivationDate()));
		}
	   	if(hotelOrderQuery.getLockStatus()!=null){
	   		dc.add(Restrictions.eq("lockStatus", hotelOrderQuery.getLockStatus()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getSpecialDemand())){
			dc.add(Restrictions.eq("specialDemand", hotelOrderQuery.getSpecialDemand()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getTotalMoney())){
			dc.add(Restrictions.eq("totalMoney", hotelOrderQuery.getTotalMoney()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getFileIds())){
			dc.add(Restrictions.eq("fileIds", hotelOrderQuery.getFileIds()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOriginalTotalMoney())){
			dc.add(Restrictions.eq("originalTotalMoney", hotelOrderQuery.getOriginalTotalMoney()));
		}
	   	if(hotelOrderQuery.getIsAfterSupplement()!=null){
	   		dc.add(Restrictions.eq("isAfterSupplement", hotelOrderQuery.getIsAfterSupplement()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getOriginalFrontMoney())){
			dc.add(Restrictions.eq("originalFrontMoney", hotelOrderQuery.getOriginalFrontMoney()));
		}
	   	if(hotelOrderQuery.getPaymentType()!=null){
	   		dc.add(Restrictions.eq("paymentType", hotelOrderQuery.getPaymentType()));
	   	}
	   	if(hotelOrderQuery.getForecaseReportNum()!=null){
	   		dc.add(Restrictions.eq("forecaseReportNum", hotelOrderQuery.getForecaseReportNum()));
	   	}
	   	if(hotelOrderQuery.getSubControlNum()!=null){
	   		dc.add(Restrictions.eq("subControlNum", hotelOrderQuery.getSubControlNum()));
	   	}
	   	if(hotelOrderQuery.getSubUnControlNum()!=null){
	   		dc.add(Restrictions.eq("subUnControlNum", hotelOrderQuery.getSubUnControlNum()));
	   	}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getRemark())){
			dc.add(Restrictions.eq("remark", hotelOrderQuery.getRemark()));
		}
		if (StringUtils.isNotEmpty(hotelOrderQuery.getBookingStatus())){
			dc.add(Restrictions.eq("bookingStatus", hotelOrderQuery.getBookingStatus()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelOrderDao.find(dc);
	}
	
	public List<HotelOrder> getByHotelUuid(String hotelUuid) {
		String sql = "select DISTINCT o.createBy from hotel_order o where o.activity_hotel_uuid = ?";
		return hotelOrderDao.findBySql(sql, Map.class, hotelUuid);
	}
	
	public HotelOrder getByUuid(String uuid) {
		return hotelOrderDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelOrder hotelOrder = getByUuid(uuid);
		hotelOrder.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelOrder);
	}
	
	public boolean hotelOrderCancel(String payUuid, String orderUuid) {

		HotelOrder hotelOrder = hotelOrderDao.getByUuid(orderUuid);
		// 撤销：订单达账金额减去撤销金额
		this.DTcancelOrder(payUuid, hotelOrder);

		//修改金额表中的付款方式类型为财务取消状态
		productMoneyAmountService.updateMoneyType(Context.ORDER_TYPE_HOTEL, hotelOrder.getAccountedMoney(), Context.MONEY_TYPE_CANCEL);
		return true;
	}
	
	/**
	 * 酒店产品预报名
	*<p>Title: saveHotelOrder</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-6-18 上午10:18:34
	* @throws
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public Map<String, String> saveHotelOrder(HotelOrderInput hotelOrderInput) throws Exception {
		//重建前端传递数据
		rebuildInputData(hotelOrderInput);
		
		Map<String, String> datas = new HashMap<String, String>();
		//联系人信息集合
		List<OrderContacts> orderContactsList = new ArrayList<OrderContacts>();
		//游客类型费用、费用调整
		List<HotelOrderPrice> hotelOrderPriceList = new ArrayList<HotelOrderPrice>();
		//游客信息 集合
	  	List<HotelTraveler> hotelTravelerList = new ArrayList<HotelTraveler>();
	    //订单的金额信息，订单总额、结算总额、应收、已收
	  	List<HotelMoneyAmount> hotelMoneyAmountList = new ArrayList<HotelMoneyAmount>();
	  	
	  	//签证信息
		List<HotelTravelervisa> hotelTravelervisaList = new ArrayList<HotelTravelervisa>();
		//附件信息
		List<HotelAnnex> hotelTravelerFilesList = new ArrayList<HotelAnnex>();
		//游客的证件信息
		List<HotelTravelerPapersType> hotelTravelerPapersTypeList = new ArrayList<HotelTravelerPapersType>();
		HotelOrder hotelOrder = null;
		
		try{
			hotelOrder = hotelOrderInput.getHotelOrder();
			hotelOrder.setOrderNum(com.trekiz.admin.common.utils.DateUtils.date2String(new Date(), "yyyyMMddHHmmss"));
			hotelOrder.setOrderStatus(HotelOrder.ORDER_STATUS_TO_CONFIRM);
			hotelOrder.setOrderSalerId(UserUtils.getUser().getId().intValue());
			hotelOrder.setOrderPersonName(UserUtils.getUser().getName());
			hotelOrder.setOrderTime(new Date());
			hotelOrder.setCostMoney(UuidUtils.generUuid());
			hotelOrder.setLockStatus(HotelOrder.LOCK_STATUS_NORMAL);
			hotelOrder.setTotalMoney(UuidUtils.generUuid());
			hotelOrder.setOriginalTotalMoney(UuidUtils.generUuid());
			hotelOrder.setIsAfterSupplement(false);
			hotelOrder.setPaymentType(Context.PAYMENT_TYPE_JS);
			if(hotelOrder.getOrderCompany() == null) {
				hotelOrder.setOrderCompany(-1);
			}
			
			//保存酒店订单以及子订单数据
			this.save(hotelOrder);
			
			orderContactsList = hotelOrder.getOrderContactsList();
			hotelOrderPriceList = hotelOrder.getHotelOrderPriceList();
			hotelTravelerList = hotelOrder.getHotelTravelerList();
			hotelMoneyAmountList = hotelOrder.getHotelMoneyAmountList();
			
			//初始化联系人表数据
			if(CollectionUtils.isNotEmpty(orderContactsList)) {
				for(OrderContacts orderContacts : orderContactsList) {
					orderContacts.setOrderId(hotelOrder.getId().longValue());
					orderContacts.setOrderType(Context.ORDER_TYPE_HOTEL);
					orderContacts.setAgentId(hotelOrder.getOrderCompany()==null?null:hotelOrder.getOrderCompany().longValue());
				}
			}
			
			//初始化订单价格表数据
			if(CollectionUtils.isNotEmpty(hotelOrderPriceList)) {
				for(HotelOrderPrice hotelOrderPrice : hotelOrderPriceList) {
					hotelOrderPrice.setOrderUuid(hotelOrder.getUuid());
					super.setOptInfo(hotelOrderPrice, BaseService.OPERATION_ADD);
				}
			}
			
			//初始化酒店moneyAmount数据
			if(CollectionUtils.isNotEmpty(hotelMoneyAmountList)) {
				for(HotelMoneyAmount hotelMoneyAmount : hotelMoneyAmountList) {
					hotelMoneyAmount.setBusinessUuid(hotelOrder.getUuid());
					hotelMoneyAmount.setBusinessType(HotelMoneyAmount.BUSINESS_TYPE_ORDER);
					BigDecimal convertLowest = currencyDao.findConvertLowestById(hotelMoneyAmount.getCurrencyId().longValue());
					if(convertLowest != null) {
						hotelMoneyAmount.setExchangerate(convertLowest.doubleValue());
					} else {
						BigDecimal exchangerage = currencyDao.findExchangerageById(hotelMoneyAmount.getCurrencyId().longValue());
						if (exchangerage != null) {
							hotelMoneyAmount.setExchangerate(exchangerage.doubleValue());
						}
					}
					
					//设置订单流水号：
					if(hotelMoneyAmount.getMoneyType() == Context.MONEY_TYPE_CBJ.intValue()) {
						hotelMoneyAmount.setSerialNum(hotelOrder.getCostMoney());
					} else if(hotelMoneyAmount.getMoneyType() == Context.MONEY_TYPE_YSH.intValue()) {
						hotelMoneyAmount.setSerialNum(hotelOrder.getTotalMoney());
					} else if(hotelMoneyAmount.getMoneyType() == Context.MONEY_TYPE_YSYSH.intValue()) {
						hotelMoneyAmount.setSerialNum(hotelOrder.getOriginalTotalMoney());
					}
					
					super.setOptInfo(hotelMoneyAmount, BaseService.OPERATION_ADD);
				}
				
				//多币种的累加操作（将同样币种金额进行相加）
				countMoney(hotelMoneyAmountList);
			}
			
			//初始化游客表数据
			if(CollectionUtils.isNotEmpty(hotelTravelerList)) {
				for(HotelTraveler hotelTraveler : hotelTravelerList) {
					hotelTraveler.setOrderUuid(hotelOrder.getUuid());
					//根据团期信息获取唯一团期价格信息
					ActivityHotelGroupPrice groupPrice = activityHotelGroupPriceDao.getGroupPriceByGroupInfo(hotelOrder.getActivityHotelGroupUuid(), hotelTraveler.getPersonType());
					if(groupPrice != null) {
						hotelTraveler.setSrcPriceCurrency(groupPrice.getCurrencyId());
						hotelTraveler.setSrcPrice(groupPrice.getPrice());
					}
					//游客原始应收价
					hotelTraveler.setOriginalPayPriceSerialNum(UuidUtils.generUuid());
					//游客成本价
					hotelTraveler.setCostPriceSerialNum(UuidUtils.generUuid());
					//游客结算价
					hotelTraveler.setPayPriceSerialNum(UuidUtils.generUuid());
					//酒店游客信息将状态设置为正常
					hotelTraveler.setStatus("0");
					
					super.setOptInfo(hotelTraveler, BaseService.OPERATION_ADD);
					
					//加载游客表子表数据(签证信息)
					if(CollectionUtils.isNotEmpty(hotelTraveler.getHotelTravelervisaList())) {
						for(HotelTravelervisa hotelTravelervisa : hotelTraveler.getHotelTravelervisaList()) {
							hotelTravelervisa.setHotelOrderUuid(hotelOrder.getUuid());
							hotelTravelervisa.setHotelTravelerUuid(hotelTraveler.getUuid());
							super.setOptInfo(hotelTravelervisa, BaseService.OPERATION_ADD);
						}
						//添加游客子表数据(签证信息)
						hotelTravelervisaList.addAll(hotelTraveler.getHotelTravelervisaList());
					}
					
					//加载游客表子表数据(游客金额信息)
					if(CollectionUtils.isNotEmpty(hotelTraveler.getHotelMoneyAmountList())) {
						for(int i=0; i<3; i++) {
							//存储了三次金额数据,每次金额数据做一次币种合并累加
							List<HotelMoneyAmount> hotelMoneyAmountBaks = new ArrayList<HotelMoneyAmount>();
							
							for(HotelMoneyAmount hotelMoneyAmount : hotelTraveler.getHotelMoneyAmountList()) {
								HotelMoneyAmount hotelMoneyAmountBak = new HotelMoneyAmount();
								hotelMoneyAmountBak.setAmount(hotelMoneyAmount.getAmount());
								hotelMoneyAmountBak.setCurrencyId(hotelMoneyAmount.getCurrencyId());
								
								hotelMoneyAmountBak.setBusinessUuid(hotelTraveler.getUuid());
								hotelMoneyAmountBak.setBusinessType(HotelMoneyAmount.BUSINESS_TYPE_TRAVELER);

								//保存金额类型：原始结算价、结算价、成本价
								if (i == 0) {
									hotelMoneyAmountBak.setMoneyType(Context.MONEY_TYPE_YSJSJ);
								} else if (i == 1) {
									hotelMoneyAmountBak.setMoneyType(Context.MONEY_TYPE_JSJ);
								} else {
									hotelMoneyAmountBak.setMoneyType(Context.MONEY_TYPE_CBJ);
								}
								
								//设置游客金额表金额流水号
								String serialNum = "";
								if(i == 0) {
									serialNum = hotelTraveler.getOriginalPayPriceSerialNum();
								} else if(i == 1) {
									serialNum = hotelTraveler.getPayPriceSerialNum();
								} else if(i == 2) {
									serialNum = hotelTraveler.getCostPriceSerialNum();
								}
								hotelMoneyAmountBak.setSerialNum(serialNum);
								
								BigDecimal exchangerage = currencyDao.findConvertLowestById(hotelMoneyAmountBak.getCurrencyId().longValue());
								if(exchangerage != null) {
									hotelMoneyAmountBak.setExchangerate(exchangerage.doubleValue());
								}
								super.setOptInfo(hotelMoneyAmountBak, BaseService.OPERATION_ADD);
								
								hotelMoneyAmountBaks.add(hotelMoneyAmountBak);
							}

							//多币种的累加操作（将同样币种金额进行相加）
							countMoney(hotelMoneyAmountBaks);
							
							//添加游客子表数据(游客金额信息)
							hotelMoneyAmountList.addAll(hotelMoneyAmountBaks);
						}
					}
					
					//加载游客表子表数据(游客的证件信息)
					if(CollectionUtils.isNotEmpty(hotelTraveler.getHotelTravelerPapersTypeList())) {
						for(HotelTravelerPapersType hotelTravelerPapersType : hotelTraveler.getHotelTravelerPapersTypeList()) {
							hotelTravelerPapersType.setHotelTravelerUuid(hotelTraveler.getUuid());
							hotelTravelerPapersType.setOrderUuid(hotelOrder.getUuid());
							super.setOptInfo(hotelTravelerPapersType, BaseService.OPERATION_ADD);
						}
						//添加游客子表数据(游客的证件信息)
						hotelTravelerPapersTypeList.addAll(hotelTraveler.getHotelTravelerPapersTypeList());
					}
					
					//加载游客表子表数据(上传文件信息)
					if(CollectionUtils.isNotEmpty(hotelTraveler.getHotelTravelerFilesList())) {
						for(HotelAnnex hotelAnnex : hotelTraveler.getHotelTravelerFilesList()) {
							hotelAnnex.setType(HotelAnnex.ANNEX_TYPE_FOR_HOTEL_TRAVELER);
							hotelAnnex.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
							hotelAnnex.setMainUuid(hotelTraveler.getUuid());
							super.setOptInfo(hotelAnnex, BaseService.OPERATION_ADD);
						}
						
						hotelTravelerFilesList.addAll(hotelTraveler.getHotelTravelerFilesList());
					}
				}
			}
			
			//批量保存数据
			orderContactsDao.batchSave(orderContactsList);
			hotelOrderPriceDao.batchSave(hotelOrderPriceList);
			hotelTravelerDao.batchSave(hotelTravelerList);
			hotelMoneyAmountDao.batchSave(hotelMoneyAmountList);
			hotelTravelervisaDao.batchSave(hotelTravelervisaList);
			hotelTravelerPapersTypeDao.batchSave(hotelTravelerPapersTypeList);
			hotelAnnexDao.batchSave(hotelTravelerFilesList);
			
			
		} catch (Exception e) {
			datas.put("message", "3");
			datas.put("error", "保存异常,请重新操作!");
			throw e;
		}
		datas.put("orderUuid", hotelOrder.getUuid());
		
		return datas;
	}
	
	/**
	 * 重建表单传递数据
	*<p>Title: rebuildInputData</p>
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-6-18 上午10:19:34
	* @throws
	 */
	private void rebuildInputData(HotelOrderInput hotelOrderInput) {
		//处理结算总额、订单总额（涉及多币种）---------
		String[] hotelMoneyAmount_amountArr = hotelOrderInput.getHotelMoneyAmount_amount();
		String[] hotelMoneyAmount_currencyIdArr = hotelOrderInput.getHotelMoneyAmount_currencyId();
		String[] hotelMoneyAmount_moneyTypeArr = hotelOrderInput.getHotelMoneyAmount_moneyType();

		List<String> amountBak = new ArrayList<String>();
		List<String> currencyBak = new ArrayList<String>();
		List<String> moneyTypeBak = new ArrayList<String>();
		
		if(hotelMoneyAmount_amountArr != null && hotelMoneyAmount_amountArr.length > 0) {
			
			for(int i=0; i<hotelMoneyAmount_amountArr.length; i++) {
				String[] hotelMoneyAmount_amount = hotelMoneyAmount_amountArr[i].split(";");
				String[] hotelMoneyAmount_currencyId = hotelMoneyAmount_currencyIdArr[i].split(";");
				
				amountBak.addAll(Arrays.asList(hotelMoneyAmount_amount));
				currencyBak.addAll(Arrays.asList(hotelMoneyAmount_currencyId));
				
				for(int j=0; j<hotelMoneyAmount_amount.length; j++) {
					moneyTypeBak.add(hotelMoneyAmount_moneyTypeArr[i]);
				}
			}
		}
		
		hotelOrderInput.setHotelMoneyAmount_amount((String[])amountBak.toArray(new String[amountBak.size()]));
		hotelOrderInput.setHotelMoneyAmount_currencyId((String[])currencyBak.toArray(new String[currencyBak.size()]));
		hotelOrderInput.setHotelMoneyAmount_moneyType((String[])moneyTypeBak.toArray(new String[moneyTypeBak.size()]));
		//处理结算总额、订单总额---------
	}
	
	/**
	 * 多币种的累加操作（将同样币种金额进行相加）
	*<p>Title: countMoney</p>
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-6-15 下午10:15:32
	* @throws
	 */
	private void countMoney(List<HotelMoneyAmount> hotelMoneyAmountBak) {
		if(CollectionUtils.isEmpty(hotelMoneyAmountBak)) {
			return ;
		}

		Iterator<HotelMoneyAmount> hotelMoneyAmountBakIter = hotelMoneyAmountBak.iterator();
		List<HotelMoneyAmount> hotelMoneyAmountList = new ArrayList<HotelMoneyAmount>();
		
		flag:while(hotelMoneyAmountBakIter.hasNext()) {
			HotelMoneyAmount hotelMoneyAmount = hotelMoneyAmountBakIter.next();
			
			for(HotelMoneyAmount entity : hotelMoneyAmountList) {
				//当存储游客金额时：
				if(IslandMoneyAmount.BUSINESS_TYPE_TRAVELER == entity.getBusinessType()) {
					if(hotelMoneyAmount.getCurrencyId().intValue() == entity.getCurrencyId()) {
						entity.setAmount(entity.getAmount() + hotelMoneyAmount.getAmount());
						hotelMoneyAmountBakIter.remove();
						
						continue flag;
					}
				//存储其他金额时：
				} else {
					if(hotelMoneyAmount.getCurrencyId().intValue() == entity.getCurrencyId() && hotelMoneyAmount.getMoneyType().intValue() == entity.getMoneyType()) {
						entity.setAmount(entity.getAmount() + hotelMoneyAmount.getAmount());
						hotelMoneyAmountBakIter.remove();
						
						continue flag;
					}
				}
			}
			
			hotelMoneyAmountList.add(hotelMoneyAmount);
		}
		
		return ;
	}
	
	/**
	 * 酒店达账撤销
	*<p>Title: DTcancelOrder</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午12:21:55
	* @throws
	 */
	public boolean DTcancelOrder(String payUuid, HotelOrder hotelOrder) {
		
		//达账金额减去撤销金额
		List<HotelMoneyAmount> list = hotelMoneyAmountService.getMoneyAmonutBySerialNum(hotelOrder.getAccountedMoney());
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				hotelMoneyAmountService.addOrSubtractMoneyAmount(list.get(i), hotelOrder.getAccountedMoney(), false);
			}
		}
		return true;
	}
	
	public void initHotelOrderPageData(String activityHotelGroupUuid, Model model) {
		//加载酒店产品团期信息
		ActivityHotelGroup activityHotelGroup = activityHotelGroupDao.getByUuid(activityHotelGroupUuid);
		if(activityHotelGroup == null) {
			return ;
		}
		
		//加载酒店产品信息
		ActivityHotel activityHotel = activityHotelDao.getByUuid(activityHotelGroup.getActivityHotelUuid());

		//加载酒店产品团期价格表
		List<ActivityHotelGroupPrice> groupPrices = activityHotelGroupPriceService.getPriceFilterTravel(activityHotelGroupUuid, activityHotel.getHotelUuid());
		
		//加载渠道信息
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();// 渠道信息列表

		//签证类型
		List<SysDict> visaTypes = sysDictService.findByType(SysDict.TYPE_NEW_VISA_TYPE);
		

		//加载团期下所有的房型数据
		List<ActivityHotelGroupRoom> groupRooms = activityHotelGroupRoomService.getRoomListByGroupUuid(activityHotelGroupUuid);
		
		//读取公司配置币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("agentList", agentInfos);
		model.addAttribute("groupPrices", groupPrices);
		model.addAttribute("activityHotelGroup", activityHotelGroup);
		model.addAttribute("activityHotel", activityHotel);
		model.addAttribute("visaTypes", visaTypes);
		
		model.addAttribute("hotelStar", hotelService.getHotelStarValByHotelUuid(activityHotel.getHotelUuid()));
		model.addAttribute("groupRooms", groupRooms);
		model.addAttribute("groupRoomsOutHteml", generateGroupRoomsOutHtml(groupRooms));
		//add by hhx 把产品发布人  当前用户名显示出来
		model.addAttribute("productCreateBy", UserUtils.getUserNameById(activityHotel.getCreateBy()+0L));
		model.addAttribute("loginUserName", UserUtils.getUser().getName());
		
		model.addAttribute("bookingPersonNum", this.getBookingPersonNum(activityHotelGroup.getUuid()));
		
		//add start by yang.jiang 2016-01-17 20:21:45
		//获取默认的第一个渠道
		if (CollectionUtils.isEmpty(agentInfos)) {
			agentInfos.add(new Agentinfo());
		}
		Agentinfo defaultFirstAgentinfo = agentInfos.get(0);
		//组织渠道商对应的联系人（由于第一联系人跟其他联系人不在一起） 
		List<SupplierContacts> contacts = supplierContactsService.findAllContactsByAgentInfo(defaultFirstAgentinfo.getId());  //取出渠道商所有联系人（包括第一联系人）
//				List<SupplierContacts> otherContacts = supplierContactsService.findContactsByAgentInfo(Long.parseLong(agentId));  //此方法只能取出渠道商非第一联系人
//				model.addAttribute("otherContacts", otherContacts);
		//渠道商的联系地址
		String address = agentinfoService.getAddressStrById(defaultFirstAgentinfo.getId());
		model.addAttribute("agent", defaultFirstAgentinfo);
		model.addAttribute("address", address);
		//渠道商转换为json
		for (SupplierContacts supplierContacts : contacts) {
			supplierContacts.setAgentAddressFull(address);
		}
		//转换成view实体
		List<SupplierContactsView> contactsView = Lists.newArrayList();
		for (SupplierContacts supplierContacts : contacts) {			
			SupplierContactsView splContactsView = new SupplierContactsView();
			BeanUtils.copyProperties(splContactsView, supplierContacts);
			contactsView.add(splContactsView);
		}
		model.addAttribute("contacts", contacts);
		model.addAttribute("contactsView", contactsView);
		String contactsJsonStr = supplierContactsService.contacts2Json(contacts);
		org.json.JSONArray contactArray = supplierContactsService.contacts2JsonArray(contacts);
		org.json.JSONArray contactArrayView = supplierContactsService.contacts2JsonArray4View(contactsView);
//				List<String> contactList = supplierContactsService.contacts2JsonList(contacts);
		model.addAttribute("contactsJsonStr", contactsJsonStr);
		model.addAttribute("contactArray", contactArray);
		model.addAttribute("contactArrayView", contactArrayView);
//				model.addAttribute("contactList", contactList);
		//订单是否允许添加多个渠道联系人信息
		Integer allowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();
		model.addAttribute("allowAddAgentInfo", allowAddAgentInfo);
		//订单是否允许渠道联系人信息输入和修改
		Integer allowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();
		model.addAttribute("allowModifyAgentInfo", allowModifyAgentInfo);
		
		//end
		
	}
	
	private String generateGroupRoomsOutHtml(List<ActivityHotelGroupRoom> groupRooms) {
		StringBuffer sb = new StringBuffer();
		 /*<tr>
	         <td rowspan="3" class="tc"><p>水上屋 4</p></td>
	         <td rowspan="2" class="tc"><p>BB</p></td>
	         <td class="tc"><p>HB ￥200/人</p></td>
	     </tr>
	     <tr>
	         <td class="tc"><p>HB ￥200/人</p></td>
	     </tr>
	     <tr>
	         <td class="tc"><p>HB</p></td>
	         <td class="tc"><p>HB ￥200/人</p></td>
	     </tr>
	     <tr>
	         <td rowspan="3" class="tc"><p>水上屋 4</p></td>
	         <td class="tc">BB</td>
	         <td class="tc"><p>HB ￥200/人</p></td>
	     </tr>
	     <tr>
	         <td rowspan="2" class="tc">HB</td>
	         <td class="tc"><p>HB ￥200/人</p></td>
	     </tr>
	     <tr>
	         <td class="tc"><p>HB ￥200/人</p></td>
	     </tr>*/
		
		if(CollectionUtils.isNotEmpty(groupRooms)) {
			for(ActivityHotelGroupRoom groupRoom : groupRooms) {
				sb.append("<tr>");
				sb.append("");
				sb.append("</tr>");
			}
		}
		
		return sb.toString();
	}

	@Override
	public Integer getBookingPersonNum(String groupUuid) {
		
		return hotelOrderDao.getBookingPersonNum(groupUuid);
	}
	
	@Override
	public Integer  getForecaseReportNum(String groupUuid) {
		return hotelOrderDao.getForecaseReportNum(groupUuid);
	}
	
	public OrderPayInput buildOrderPayData(List<String> orderUuids, String[] resultCurrency, String[] resultAmount, String cancelPayUrl) {
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();

		if(StringUtils.isNotEmpty(cancelPayUrl)) {
			orderPayInput.setOrderListUrl(cancelPayUrl);
		}
		
		//订单的后置方法名
		orderPayInput.setServiceAfterMethodName("handleHotelOrderPay");
		// //订单的后置类名
		orderPayInput.setServiceClassName("com.trekiz.admin.modules.hotel.service.impl.HotelOrderServiceImpl");
		
		// 订单总额显示状态(true:显示;false:不显示)
		orderPayInput.setTotalCurrencyFlag(true);
		if(CollectionUtils.isNotEmpty(orderUuids)) {
			for(String orderUuid : orderUuids) {
				//单个订单的支付信息
				OrderPayDetail detail = new OrderPayDetail();
				HotelOrder hotelOrder = this.getByUuid(orderUuid);
				
				if(hotelOrder.getOrderCompany() != null) {
					orderPayInput.setAgentId(hotelOrder.getOrderCompany());
				} else {
					orderPayInput.setAgentId(-1);
				}
				detail.setPayPriceType(Context.MONEY_TYPE_QK);
				detail.setOrderUuid(orderUuid);
				detail.setOrderNum(hotelOrder.getOrderNum());
				detail.setBusindessType(1);
				detail.setOrderType(Integer.valueOf(Context.ORDER_TYPE_HOTEL));
				
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
		orderPayInput.setOrderType(Context.ORDER_TYPE_HOTEL);
		
		orderPayInput.setOrderPayDetailList(detailList);
		return orderPayInput;
	}
	
	/**
	 * 支付完成后订单的后续操作
	 * @param orderPayForm
	 * @return
	 */
	public Map<String, Object> handleHotelOrderPay(OrderPayForm orderPayForm) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
			if (orderPayInput != null && CollectionUtils.isNotEmpty(orderPayInput.getOrderPayDetailList())) {
				String orderUuid = orderPayInput.getOrderPayDetailList().get(0).getOrderUuid();
				if (StringUtils.isNotBlank(orderUuid)) {
					HotelOrder hotelOrder = hotelOrderDao.getByUuid(orderUuid);
					String payedMoney = hotelOrder.getPayedMoney();
					if (StringUtils.isBlank(payedMoney)) {
						payedMoney = UuidUtils.generUuid();
						hotelOrder.setPayedMoney(payedMoney);
						hotelOrderDao.updateObj(hotelOrder);
					}
					 
					List<HotelMoneyAmount> moneyAmountList = Lists.newArrayList();
					for (OrderPayDetail payDetail : orderPayInput.getOrderPayDetailList()) {
						List<HotelMoneyAmount> tempMoneyAmountList = hotelMoneyAmountService.getMoneyAmonutBySerialNum(payDetail.getMoneySerialNum());
						if (CollectionUtils.isNotEmpty(tempMoneyAmountList)) {
							for (HotelMoneyAmount money : tempMoneyAmountList) {
								HotelMoneyAmount moneyAmonnt = new HotelMoneyAmount();
								BeanUtils.copyProperties(moneyAmonnt, money);
								moneyAmonnt.setId(null);
								moneyAmonnt.setSerialNum(payedMoney);
								moneyAmonnt.setUuid(UuidUtils.generUuid());
								moneyAmonnt.setMoneyType(Context.MONEY_TYPE_YS);
								moneyAmonnt.setBusinessType(1);
								moneyAmonnt.setBusinessUuid(hotelOrder.getUuid());
								moneyAmonnt.setDelFlag(Context.DEL_FLAG_NORMAL);
								moneyAmountList.add(moneyAmonnt);
							}
						}
					}
					if (CollectionUtils.isNotEmpty(moneyAmountList)) {
						hotelMoneyAmountService.saveMoneyAmounts(moneyAmountList);
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
	 * 查询酒店订单列表
	 * @param page
	 * @param hotelOrderQuery 查询对象
	 * @param common 部门对象
	 * @return
	 */
	public Page<Map<Object, Object>> findOrderList(Page<Map<Object, Object>> page, HotelOrderQuery hotelOrderQuery, DepartmentCommon common) {
		
    	page.setOrderBy(hotelOrderQuery.getOrderBy());
    	
    	//sql语句where条件
        String where = this.getWhereSql(hotelOrderQuery, common);

        //获取订单查询sql语句
        String orderSql = getOrderSql(hotelOrderQuery.getOrderStatus(), where);
        hotelOrderQuery.setOrderSql(orderSql);
        
        //如果是订单查询，则直接返回结果，如果是团期查询则需要再处理sql语句
        if (hotelOrderQuery.getIsOrder()) {
        	return getOrderList(page, orderSql);
        } else {
        	return this.getGroupListByOrder(page, orderSql);
        }
    }
	
	/**
	 * 获取查询酒店where语句
	 * @param hotelOrderQuery 查询对象
	 * @param common 部门对象
	 * @return
	 */
    private String getWhereSql(HotelOrderQuery hotelOrderQuery, DepartmentCommon common) {
    	
        StringBuffer sqlWhere = new StringBuffer("");
        
        //用户类型，分为接待社内部用户和渠道用户，分别为3和1
        String userType = UserUtils.getUser().getUserType();
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        
        //团期出团日期
        String groupOpenDateBegin = hotelOrderQuery.getGroupOpenDateBegin();
        String groupOpenDateEnd = hotelOrderQuery.getGroupOpenDateEnd();
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
        	sqlWhere.append(" and agp.groupOpenDate >= '" + groupOpenDateBegin + "'");
        }
        if (StringUtils.isNotBlank(groupOpenDateEnd)){
            sqlWhere.append(" and agp.groupOpenDate  <= '" + groupOpenDateEnd + "'");
        }
        
        //下单人
        Integer createBy = hotelOrderQuery.getCreateBy();
        if (createBy != null) {
        	sqlWhere.append(" and pro.createBy = " + createBy + " ");
        }
        
        //国家
        String countryUuid = hotelOrderQuery.getCountryUuid();
        if (StringUtils.isNotBlank(countryUuid)) {
        	sqlWhere.append(" and island.country = '" + countryUuid + "' ");
        }
        
        //岛屿
        String islandUuid = hotelOrderQuery.getIslandUuid();
        if (StringUtils.isNotBlank(islandUuid)) {
        	sqlWhere.append(" and island.uuid = '" + islandUuid + "' ");
        }
        
        //酒店
        String hotelUuid = hotelOrderQuery.getHotelUuid();
        if (StringUtils.isNotBlank(hotelUuid)) {
        	sqlWhere.append(" and hotel.uuid = '" + hotelUuid + "' ");
        }
        
        //酒店星级
        String hotelStarUuid = hotelOrderQuery.getHotelStarUuid();
        if (StringUtils.isNotBlank(hotelStarUuid)) {
        	sqlWhere.append(" and hotel.star = '" + hotelStarUuid + "' ");
        }
        
        //房型
        String hotelRoomUuid = hotelOrderQuery.getHotelRoomUuid();
        if (StringUtils.isNotBlank(hotelRoomUuid)) {
        	sqlWhere.append(" and agp.uuid in (SELECT activity_hotel_group_uuid FROM activity_hotel_group_room WHERE hotel_room_uuid = '" + 
        			hotelRoomUuid + "')");
        }
        
        //产品创建者即计调
        Integer activityCreateBy = hotelOrderQuery.getActivityCreateBy();
        if (activityCreateBy != null) {
        	sqlWhere.append(" and users.id = " + activityCreateBy + " ");
        }
        
        //订单创建时间
        String orderTimeBegin = hotelOrderQuery.getOrderTimeBegin();
        String orderTimeEnd = hotelOrderQuery.getOrderTimeEnd();
        if (StringUtils.isNotBlank(orderTimeBegin)) {
            sqlWhere.append( " and pro.orderTime >= '" + orderTimeBegin + " 00:00:00" + "'");
        }
        if (StringUtils.isNotBlank(orderTimeEnd)) {
            sqlWhere.append( " and pro.orderTime <= '" + orderTimeEnd + " 23:59:59" + "'");
        }
        
        //区分渠道商用户或批发商用户
        Integer agentId = hotelOrderQuery.getOrderCompany();
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
        String orderCompanyName = hotelOrderQuery.getOrderCompanyName();
        if (StringUtils.isNotBlank(orderCompanyName)) {
        	if (agentId != null && agentId == -1) {
        		sqlWhere.append(" and pro.orderCompanyName = '" + agentId + "' ");
        	}
        }
        
        //产品名称
        if(StringUtils.isNotBlank(hotelOrderQuery.getActivityName())){
        	sqlWhere.append(" and activity.activityName like '%" + hotelOrderQuery.getActivityName() + "%' ");
        }
        
        //控房单号
        if(StringUtils.isNotBlank(hotelOrderQuery.getActivitySerNum())){
        	sqlWhere.append(" and activity.activitySerNum like '%" + hotelOrderQuery.getActivitySerNum() + "%' ");
        }
        
        //订单号或团号
        String orderNumOrGroupCode = hotelOrderQuery.getOrderNumOrGroupCode();
        if (StringUtils.isNotBlank(orderNumOrGroupCode)){
        	sqlWhere.append(" and (pro.orderNum like '%" + orderNumOrGroupCode + "%' ")
        			.append(" or agp.groupCode like '%" + orderNumOrGroupCode + "%') ");
        }
        
        //联系人
        String orderPersonName = hotelOrderQuery.getOrderPersonName();
        if (StringUtils.isNotBlank(orderPersonName)) {
        	sqlWhere.append(" and pro.orderPersonName like '%" + orderPersonName + "%' ");
        }
        
        //分部门显示
        systemService.getDepartmentSql("order", null, sqlWhere, common, Context.ORDER_TYPE_HOTEL);
        
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
    				.append("island.island_name islandName, hotel.uuid hotelUuid, hotel.star hotelStar, ")
    				.append("hotel.name_cn hotelName, ")
    				.append("agp.groupOpenDate, agp.groupEndDate, agp.island_way islandWay, agp.groupCode, ")
    				.append("agp.id gruopId, agp.uuid gruopUuid, agp.remNumber freePosition, agp.lockStatus settleLockStatus, ")
    				.append("pro.id,pro.uuid orderUuid, pro.orderTime, pro.createBy, pro.orderNum, pro.orderSalerId, pro.orderPersonPhoneNum, ")
    				.append("pro.orderStatus, pro.orderCompanyName, pro.orderPersonNum, pro.isPayment, pro.orderPersonName,pro.apply_time AS applyTime ,")
    				.append("ifnull(pro.forecase_report_num, 0) forecaseReportNum,pro.id orderById,pro.updateDate orderByUpdateDate,")
    				.append("pro.front_money, pro.total_money,  pro.payed_money, pro.accounted_money, pro.activity_hotel_group_uuid groupUuid, ")
    				.append("costOuter.moneyStr AS costMoney, totalOuter.moneyStr AS totalMoney, ")
    				.append("payedOuter.moneyStr AS payedMoney, accountedOuter.moneyStr AS accountedMoney, ")
    				.append("pro.placeHolderType, pro.activationDate, pro.orderCompany, pro.payMode proPayMode, pro.lockStatus, ")
    				.append("pro.remainDays proRemainDays, pro.cancel_description AS cancelDescription, pro.paymentType, ")
    				.append("limits.id invoiceid, limits.createStatus createStatus, limits.verifyStatus verifyStatus ")
    			.append("FROM activity_hotel activity, ")
    				.append("activity_hotel_group agp, ")
    				.append("island island, ")
    				.append("hotel hotel, ")
    				.append("sys_user users, ")
    				.append("hotel_order pro ")
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
	    			.append("FROM hotel_money_amount mao ")
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
	    			.append("FROM hotel_money_amount mao ")
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
	    			.append("FROM hotel_money_amount mao ")
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
	    			.append("FROM hotel_money_amount mao ")
	    			.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
	    			.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_DZ + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
	    			.append("GROUP BY mao.serialNum ")
	    			.append(") accountedOuter ON accountedOuter.serialNum = pro.accounted_money ")
	    			
    			.append("WHERE pro.delFlag = '" + ProductOrderCommon.DEL_FLAG_NORMAL + "' ")
    				.append("AND activity.uuid = pro.activity_hotel_uuid ")
    				.append("AND activity.createBy = users.id ")
    				.append("AND agp.uuid = pro.activity_hotel_group_uuid AND agp.activity_hotel_uuid = pro.activity_hotel_uuid ")
    				.append("AND activity.island_uuid = island.uuid AND activity.hotel_uuid = hotel.uuid ")
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
        Page<Map<Object, Object>> pageMap = hotelOrderDao.findPageBySql(page, sql, Map.class);
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
     * 根据订单查询条件查询涉及到团期，并根据团期uuid查询团期
     * @param page
     * @param orderSql 订单查询语句
     * @return
     */
    private Page<Map<Object, Object>> getGroupListByOrder(Page<Map<Object, Object>> page, String orderSql) {
    	
    	StringBuffer sql = new StringBuffer("");
    	//获取根据订单查询条件查询团期ids的sql语句
    	orderSql = "SELECT distinct agp.uuid groupUuid " + orderSql.substring(orderSql.indexOf("FROM"));
    	orderSql = orderSql.split("LEFT JOIN")[0] + orderSql.substring(orderSql.lastIndexOf("WHERE pro.delFlag"));
    	
    	//查询团期sql
    	sql.append("SELECT groups.* FROM(")
	    		.append("SELECT ")
					.append("agp.id orderById, agp.uuid groupUuid, agp.groupCode groupCode, ")
					.append("agp.groupOpenDate, agp.groupEndDate, agp.updateDate orderByUpdateDate, ")
					.append("activity.uuid activityUuid, activity.activityName, activity.activitySerNum, ")
					.append("island.island_name islandName, ")
					.append("hotel.uuid hotelUuid, hotel.name_cn hotelName, hotel.star hotelStar, ")
					.append("agp.island_way islandWay, agp.remNumber freePosition, agp.control_num controlNum, agp.uncontrol_num uncontrolNum, ")
					.append("agp.currency_id singleCurrencyId, agp.singlePrice singlePrice, ")
					.append("agp.front_money_currency_id frontCurrencyId, agp.front_money frontMoney, ")
					.append("NOW() nowDate, agp.status, activity.createBy createBy ")
				.append("FROM activity_hotel activity, activity_hotel_group agp,island island, hotel hotel ")
				.append("WHERE agp.activity_hotel_uuid = activity.uuid AND activity.island_uuid = island.uuid AND activity.hotel_uuid = hotel.uuid ")
				.append(") groups, (" + orderSql + ") orders ")
				.append("WHERE groups.groupUuid = orders.groupUuid");
        Page<Map<Object, Object>> pageMap = hotelOrderDao.findBySql(page, sql.toString(), Map.class);
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
    		.append("agp.id orderById,agp.updateDate orderByUpdateDate,agp.uuid groupUuid,")
    		.append("pro.createBy,pro.orderNum,pro.orderSalerId,pro.orderPersonName,pro.orderPersonPhoneNum,pro.orderStatus,")
    		.append("costOuter.moneyStr AS costMoney,totalOuter.moneyStr AS totalMoney,pro.orderPersonNum,payedOuter.moneyStr AS payedMoney,")
    		.append("pro.id,pro.uuid orderUuid,pro.orderTime,pro.activationDate,pro.placeHolderType,accountedOuter.moneyStr AS accountedMoney,")
    		.append("pro.orderCompany,pro.lockStatus,pro.orderCompanyName,ifnull(pro.forecase_report_num,0) forecaseReportNum,pro.apply_time AS applyTime,")
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
		sql.append(" and pro.activity_hotel_group_uuid in (" + groupIdSql + ")");
		//查询
		page.setPageSize(300);
		Page<Map<Object, Object>> pageMap = hotelOrderDao.findPageBySql(page, sql.toString(), Map.class);
    	return pageMap;
    }
    
    /**
	 * 清除session中Orderpay的缓存
	 * @param role
	 * @return
	 */
	public Object clearObject(Object object){
		hotelOrderDao.getSession().evict(object);
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
		
		List<PayHotelOrder> orderPayList = payHotelOrderService.findLastDateOrderPay(orderUuid);
		String prompt = "财务已确认";
		
		PayHotelOrder orderPay = null;
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
				prompt = prompt.replace("已确认", "驳回") + "收款";
			}
			
			prompt += hotelMoneyAmountService.getMoneyStr(orderPay.getMoneySerialNum(), false);
			
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
		HotelOrder hotelOrder = getByUuid(uuid);
		hotelOrder.setLockStatus(Context.LOCK_ORDER);
		update(hotelOrder);
        //添加操作日志
        this.saveLogOperate(Context.log_type_orderform,
        		Context.log_type_orderform_name, "订单" + hotelOrder.getId() + "锁死成功", "2", Context.ProductType.PRODUCT_HOTEL, hotelOrder.getId().longValue());
    }
	
	/**
	 * 订单解锁
	 * @param uuid 订单uuid
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void unLockOrder(String uuid) {
		HotelOrder hotelOrder = getByUuid(uuid);
		hotelOrder.setLockStatus(Context.UNLOCK_ORDER);
		update(hotelOrder);
		//添加操作日志
		this.saveLogOperate(Context.log_type_orderform,
				Context.log_type_orderform_name, "订单" + hotelOrder.getId() + "解锁成功", "2", Context.ProductType.PRODUCT_HOTEL, hotelOrder.getId().longValue());
	}
	
	/**
	 * 订单关联文件
	 * @param uuid 订单orderUuid
	 * @param uuid 文件ids
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void setOrderFiles(String orderUuid, String fileIds) {
		HotelOrder hotelOrder = getByUuid(orderUuid);
		if (StringUtils.isNotBlank(hotelOrder.getFileIds())) {
			hotelOrder.setFileIds(hotelOrder.getFileIds() + fileIds);
		} else {
			hotelOrder.setFileIds(fileIds);
		}
		update(hotelOrder);
	}
	
	/**
	 * 获取订单关联文件信息
	 * @param orderUuid 订单uuid
	 */
	public List<DocInfo> getFilesInfo(String orderUuid) {
		List<DocInfo> docInfoLists = Lists.newArrayList();
		HotelOrder hotelOrder = getByUuid(orderUuid);
		if (hotelOrder != null) {
			String docIds = hotelOrder.getFileIds();
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
	public List<HotelOrder> getByUuids(List<String> orderUuids) {
		DetachedCriteria dc = hotelOrderDao.createDetachedCriteria();
		dc.add(Restrictions.in("uuid", orderUuids));
		return hotelOrderDao.find(dc);
	}
	
	/**
     * 激活订单
     * @param uuid 订单uuid
     * @param request
     */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public String invokeOrder(String uuid, HttpServletRequest request) {
		HotelOrder hotelOrder = hotelOrderDao.getByUuid(uuid);
		Integer orderStatus = hotelOrder.getOrderStatus();
		if (orderStatus != null) {
			//如果是待确认报名订单，则直接激活订单
			if (Context.ISLAND_ORDER_STATUS_YQX == orderStatus) {
			   hotelOrder.setOrderStatus(Context.ISLAND_ORDER_STATUS_DQR);
			   hotelOrderDao.saveObj(hotelOrder);
			   //添加操作日志
			   this.saveLogOperate(Context.log_type_orderform,  Context.log_type_orderform_name,
					   "订单" + hotelOrder.getOrderNum() + "激活成功", "2", Context.ProductType.PRODUCT_HOTEL, hotelOrder.getId().longValue());
			}
		}
		return "success";
	}
	
	/**
     * 取消订单
     * @param hotelOrder 酒店订单
     * @param description 取消理由
     * @param request
     */
   @Transactional(readOnly=false, rollbackFor=Exception.class)
   public void cancelOrder(HotelOrder hotelOrder, String description, HttpServletRequest request) {
	   Integer orderStatus = hotelOrder.getOrderStatus();
	   if (orderStatus != null) {
		   //如果是待确认报名订单，则直接取消订单
		   if (Context.ISLAND_ORDER_STATUS_DQR == orderStatus) {
			   hotelOrder.setOrderStatus(Context.ISLAND_ORDER_STATUS_YQX);
			   hotelOrder.setCancelDescription(description);
			   hotelOrderDao.saveObj(hotelOrder);
			   //添加操作日志
		       this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, 
		    		   "订单" + hotelOrder.getOrderNum() + "取消成功", "2", Context.ProductType.PRODUCT_HOTEL, hotelOrder.getId().longValue());
		   }
	   }
   }
   
   /**
    * 修改凭证上传图片文件
    * @param docInfoList 文件列表
    * @param payHotelOrder 支付实体
    * @param orderUuid 订单UUID
    * @param mode
    * @param request
    * @return
    * @throws OptimisticLockHandleException
    * @throws PositionOutOfBoundException
    * @throws Exception
    */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> updatepayVoucherFile(ArrayList<DocInfo> docInfoList, PayHotelOrder payHotelOrder, String orderUuid, ModelMap mode, 
			HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException,Exception {

		List<Long> docIdList = Lists.newArrayList();
		if (StringUtils.isNotBlank(payHotelOrder.getPayVoucher())) {
			String ids [] = payHotelOrder.getPayVoucher().split(",");
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
		HotelOrder hotelOrder = getByUuid(orderUuid);

		if (docInfoList != null && docInfoList.size() > 0) {
			Iterator<DocInfo> iter = docInfoList.iterator();
			String docInfoIds = null;
			while (iter.hasNext()) {
				DocInfo docInfo = iter.next();
				docInfo.setPayOrderId(payHotelOrder.getId().longValue());
				docInfoDao.save(docInfo);
				// save 保存之后，docInfo对象在数据库生成的主键ID居然也加入了docInfo对象中。
				// save之前，getId()应该是null，save后getId()有值了。
				if (StringUtils.isNotBlank(docInfoIds)) {
					docInfoIds += "," + docInfo.getId();
				} else {
					docInfoIds =  docInfo.getId().toString();
				}
			}
			payHotelOrder.setPayVoucher(docInfoIds);
		}
		payHotelOrderDao.updateBySql("update pay_hotel_order set payVoucher = ?,remarks=? where id = ?", 
				payHotelOrder.getPayVoucher(),payHotelOrder.getRemarks(), payHotelOrder.getId());
		
		Map<String, Object> map = new HashMap<String, Object>();

		//支付订单金额千位符处理
	    if (StringUtils.isNotBlank(payHotelOrder.getMoneySerialNum())) {
	    	clearObject(payHotelOrder);
	    	payHotelOrder.setMoneySerialNum(hotelMoneyAmountService.getMoneyStr(payHotelOrder.getMoneySerialNum(), false));
	    }
		mode.addAttribute("orderPay", payHotelOrder);
		mode.addAttribute("hotelOrder", hotelOrder);
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
			for (Map<String,String>m:reviewMapList) {
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
						m.put("orderTime", travelerMap.get("orderTime") + "");
						m.put("payPrice", getMoneyStr(travelerMap.get("payPrice").toString()));
						
						m.put("spaceLevelName", travelerMap.get("spaceLevelName") + "");
						m.put("personTypeName", travelerMap.get("personTypeName") + "");
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
		.append("SELECT order.orderTime,spaceDict.label spaceLevelName,personTypeDict.label personTypeName, tra.id as travelerId,")
				.append("tra.order_uuid orderUuid, tt.name,tra.payPriceSerialNum, tra.personType, IFNULL(a.accounted_money, 0) as payPrice ")
		.append("FROM hotel_traveler tra ")
		.append("LEFT JOIN hotel_order order ON tra.order_uuid = order.uuid ")
		.append("LEFT JOIN sys_dict spaceDict ON tra.space_level = dict.value and type = 'spaceGrade_Type' ")
		.append("LEFT JOIN sys_dict personTypeDict ON tra.personType = dict.value and type = '" + Context.BaseInfo.TRAVELER_TYPE + "' ")
		.append("LEFT JOIN (")
					.append("SELECT t.id, t.order_uuid orderUuid, t.payPriceSerialNum, group_concat(format(IFNULL(ma.amount, 0),2), ")
						.append("c.currency_name, 'money' ORDER BY c.currency_id) AS accounted_money and t.id in " + checksql + " ")
					.append("FROM traveler t LEFT JOIN money_amount ma on t.payPriceSerialNum = ma.serialNum ")
					.append("INNER JOIN currency c on ma.currencyId= c.currency_id ")
					.append("GROUP BY t.id")
		.append(") a on tra.id= a.id  WHERE tra.id in " + checksql);
		return hotelOrderDao.findBySql(sbf.toString(), Map.class);
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
		if (CollectionUtils.isNotEmpty(list)){
			resultMap.put("reviewInfo", list.get(0));
			String checksql = "";
			checksql = "(" + list.get(0).getTravelerId() + ")";
			List<Map<Object,Object>> map = getTravelerInfoForReview(checksql);
			Map<Object,Object> mapResult = map.get(0);
			String payPrice = mapResult.get("payPrice").toString();
			mapResult.put("payPrice", getMoneyStr(payPrice));
			mapResult.put("spaceLevelName", mapResult.get("spaceLevelName") + "");
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
	public List<Map<Object, Object>> getTravelerByOrderId(String orderUuid) {
		StringBuffer sbf=new StringBuffer();
		sbf
		.append("SELECT p.orderTime,tt.id,tt.orderId,tt.name,tt.sex,tt.payPriceSerialNum,tt.personType,tt.delFlag,")
		.append("IFNULL(a.accounted_money,0) as payPrice from  traveler tt LEFT JOIN productorder p on tt.orderId= p.id ")
		.append(" LEFT JOIN(select t.id,")
		.append(" group_concat(IFNULL(ma.amount, 0),c.currency_name,'money' ORDER BY c.currency_id ) AS accounted_money")
		.append("FROM traveler tra LEFT  JOIN hotel_money_amount money on t.payPriceSerialNum = ma.serialNum")
		.append(" and t.orderId =? and t.status in(0,4) inner JOIN currency c on ma.currencyId= c.currency_id ")
		.append(" GROUP BY t.id)a on tt.id=a.id  WHERE tt.orderId=? and tt.status in(0,4) and tt.order_type=?  order by tt.id asc");
		List<Map<Object, Object>> ls = hotelOrderDao.findBySql(sbf.toString(), Map.class, orderUuid, orderUuid);
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
		HotelOrder order = getByUuid(orderUuid);
		if (null == order || order.getId() == 0) {
			map.put("sign", 0);
			map.put("result", "未找到订单！");
			return map;
			
		} 
		ActivityHotel activity = activityHotelDao.getByUuid(order.getActivityHotelUuid());
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
		for (int i = 0; i < travelerId.length; i++){
			if (sign == 1) {
				HotelTraveler traveler = hotelTravelerDao.getById(travelerId[i]);
				if (null != traveler && traveler.getId() > 0) {
						List<Detail> detailList = new ArrayList<Detail>();
						detailList.add(new Detail("travelerId",travelerId[i].toString()));
						detailList.add(new Detail("travelerName",travelerName[i]));
						detailList.add(new Detail("exitReason",exitReason[i]));
						travelerNames = travelerNames + travelerName[i]+" ";
						long rid = reviewService.addReview(
								productType, flowType, order.getId() + "", travelerId[i], (long)0, exitReason[i], sbf, detailList, activity.getDeptId().longValue());
						if (rid > 0 && sign == 1) {
							traveler.setDelFlag(Context.TRAVELER_DELFLAG_EXIT.toString());
							hotelTravelerDao.updateObj(traveler);
							order.setOrderPersonNum(order.getOrderPersonNum() - 1);
						} else {
							sign = 0;
						}
				}else{
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
		hotelOrderDao.saveObj(order);
		//添加操作日志项
		StringBuffer content = new StringBuffer();
		content.append("订单号为：" + order.getOrderNum() + " 的订单 发起退团申请流程,").append("具体退团游客有【"+travelerNames+"】");
		this.saveLogOperate(Context.log_type_orderform,
				Context.log_type_orderform_name, content.toString(), 1 + "", Context.ProductType.PRODUCT_HOTEL, order.getId().longValue());
		return map;
	}
	
	/**
	 * 修改酒店订单信息
	 * @param jsonData
	 * @return
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public int updateHotelOrder(String jsonData)throws Exception{
		logger.info("获取的json数据：" + jsonData);
		
		JSONObject json = JSONObject.fromObject(jsonData);
		long hotelOrderId = json.getLong("hotelOrderId");	//订单id
		String orderUuid = json.getString("hotelOrderUuid");//订单Uuid
		
		HotelOrder hotelOrder = hotelOrderDao.getByUuid(orderUuid);
		
		//联系人列表
		JSONArray contactsArr = (JSONArray)json.get("contactsArr");	
		saveOrUpdateContacts(contactsArr,hotelOrderId);
		
		JSONArray moneyAndPeopleArr = (JSONArray)json.get("moneyAndPeopleArr");	//费用及人数列表
		if (moneyAndPeopleArr != null && moneyAndPeopleArr.size() > 0) {
			int orderPersonNum = 0;
			//批量更新订单价格表人数
			for (int i = 0 ; i < moneyAndPeopleArr.size(); i++) {
				JSONObject orderPrice =  (JSONObject)moneyAndPeopleArr.get(i);
				String uuid = orderPrice.getString("groupPriceUuid");
				int num = orderPrice.getInt("orderPersonNum");
				orderPersonNum += num;
//				hotelOrderPriceDao.updateOrderPriceNum(num, orderUuid, groupPriceUuid);
				hotelOrderPriceDao.updateOrderPriceNumByUuid(num, uuid);
			}
			hotelOrder.setOrderPersonNum(orderPersonNum);
		}
		
		//订单价格表（费用调整操作）
		JSONArray moneyChangeArr = (JSONArray)json.get("moneyChangeArr");	//费用调整列表
		saveOrUpdateOrderPrice(moneyChangeArr,orderUuid);
		
		//依据订单uuid查询出原来所有的游客
		List<HotelTraveler> hotelTravelers = hotelTravelerDao.findTravelerByOrderUuid(orderUuid, false);
		
		//游客列表
		JSONArray travelerArr = (JSONArray)json.get("travelerArr");	
		if(travelerArr != null && travelerArr.size() > 0){
			List<HotelTraveler> saveTravelerList = new ArrayList<HotelTraveler>();	//批量保存游客
			List<HotelTraveler> updateTravelerList = new ArrayList<HotelTraveler>();	//批量更新游客
			List<HotelTraveler> deleteTravelerList = new ArrayList<HotelTraveler>();	//批量删除游客
			for(int i = 0 ; i < travelerArr.size(); i++){
				JSONObject traveler = (JSONObject)travelerArr.get(i);
				
				HotelTraveler hotelTraveler = new HotelTraveler();
				String travelerUuid = traveler.getString("travelerUuid");	//游客表uuid
				if(StringUtils.isNotEmpty(travelerUuid)){
					hotelTraveler = hotelTravelerDao.getByUuid(travelerUuid);
					hotelTraveler.setUuid(travelerUuid);
					hotelTraveler.setName(traveler.getString("travelerName"));	//游客姓名
					hotelTraveler.setNameSpell(traveler.getString("nameSpell"));	//游客名称拼音
					hotelTraveler.setPersonType(traveler.getString("personType"));	//游客类型
					hotelTraveler.setSex(traveler.getInt("sex"));	//性别
					hotelTraveler.setRemark(traveler.getString("travelerRemark"));	//备注
					
					super.setOptInfo(hotelTraveler, BaseService.OPERATION_UPDATE);
					updateTravelerList.add(hotelTraveler);
				}else{
					travelerUuid = UuidUtils.generUuid();
					hotelTraveler.setUuid(travelerUuid);	//uuid
					hotelTraveler.setOrderUuid(orderUuid);//订单uuid
					hotelTraveler.setName(traveler.getString("travelerName"));	//游客姓名
					hotelTraveler.setNameSpell(traveler.getString("nameSpell"));	//游客名称拼音
					hotelTraveler.setPersonType(traveler.getString("personType"));	//游客类型
					hotelTraveler.setSex(traveler.getInt("sex"));	//性别
					hotelTraveler.setStatus(Context.DEL_FLAG_NORMAL);
					hotelTraveler.setRemark(traveler.getString("travelerRemark"));	//备注
					
					hotelTraveler.setOriginalPayPriceSerialNum(UuidUtils.generUuid());
					hotelTraveler.setPayPriceSerialNum(UuidUtils.generUuid());
					hotelTraveler.setCostPriceSerialNum(UuidUtils.generUuid());
					
					super.setOptInfo(hotelTraveler, BaseService.OPERATION_ADD);
					saveTravelerList.add(hotelTraveler);
				}
				//签证信息
				JSONArray visaArr = (JSONArray)traveler.get("visaArr");
				saveOrUpdateTravelerVisa(visaArr,orderUuid,travelerUuid);
				
				//证件类型信息
				JSONArray papersTypeArr = (JSONArray)traveler.get("papersTypeArr");
				saveOrUpdateTravelerPapersType(papersTypeArr,orderUuid,travelerUuid);
				
				//酒店专用金额表
				JSONArray travelerMoneyArr = (JSONArray)traveler.get("travelerMoneyArr");
				saveOrUpdateTravelerMoneyAmount(travelerMoneyArr,orderUuid,travelerUuid, hotelTraveler);
				
				//附件信息
				JSONArray fileArr = (JSONArray)traveler.get("fileArr");
				saveFile(fileArr,travelerUuid);
				
			}
			//获取更新游客的补集
			if (hotelTravelers.removeAll(updateTravelerList)) {
				deleteTravelerList = hotelTravelers;
			}
			//对补集的delflag设置为0
			for (HotelTraveler hTraveler : deleteTravelerList) {
				hTraveler.setDelFlag(Context.DEL_FLAG_DELETE);
				hTraveler.setUpdateDate(new Date());
			}
			
			hotelTravelerDao.batchSave(saveTravelerList);	//批量保存
			hotelTravelerDao.batchUpdate(updateTravelerList); //批量修改游客
			hotelTravelerDao.batchUpdate(deleteTravelerList);//批量删除游客
		} else {
			//对补集的delflag设置为0
			for (HotelTraveler hTraveler : hotelTravelers) {
				hTraveler.setDelFlag(Context.DEL_FLAG_DELETE);
				hTraveler.setUpdateDate(new Date());
			}
			hotelTravelerDao.batchUpdate(hotelTravelers);//批量删除游客
		}
		
		//修改订单金额信息
		//订单总额
		JSONArray receiptedArr = (JSONArray)json.get("costMoneyArr");
		String costMoneySerialNum = UuidUtils.generUuid();	//流水号
		saveOrderMoney(receiptedArr, orderUuid, costMoneySerialNum, Context.MONEY_TYPE_CBJ, hotelOrder.getCostMoney());
				
		//订单结算总额
		JSONArray totalCostArr = (JSONArray)json.get("totalCostArr");
		String totalMoneySerialNum = UuidUtils.generUuid();	//流水号
		saveOrderMoney(totalCostArr, orderUuid, totalMoneySerialNum, Context.MONEY_TYPE_YSH, hotelOrder.getTotalMoney());
		
		hotelOrder.setCostMoney(costMoneySerialNum);//订单成本价总额流水号
		hotelOrder.setTotalMoney(totalMoneySerialNum);//订单结算总额流水号
		hotelOrder.setOrderCompany(json.getInt("orderCompany"));	//渠道
		hotelOrder.setOrderCompanyName(json.getString("orderCompanyName"));//渠道名称
		if(StringUtils.isNotEmpty(json.getString("subControlNum"))){
			hotelOrder.setSubControlNum(json.getInt("subControlNum"));	//控房间数
		}
		if(StringUtils.isNotEmpty(json.getString("subUnControlNum"))){
			hotelOrder.setSubUnControlNum(json.getInt("subUnControlNum"));//非控房
		}
		hotelOrder.setRemark(json.getString("hotelOrderRemark")); //备注
		Integer houseNum = hotelOrder.getForecaseReportNum();//房间总数先默认取预报名房间数（意义不大）
		//转报名房间数 = 控房 + 非控房
		int subControlNum = 0;
		int subUnControlNum = 0;
		if (hotelOrder.getSubControlNum() != null) {
			subControlNum = hotelOrder.getSubControlNum();
		}
		if (hotelOrder.getSubUnControlNum() != null) {
			subUnControlNum = hotelOrder.getSubUnControlNum();
		}
		houseNum = subControlNum + subUnControlNum;
		String isTransfer = json.getString("isTransfer");
		if (Boolean.parseBoolean(isTransfer)) {
			hotelOrder.setOrderStatus(Context.HOTEL_ORDER_STATUS_YQR);
			//扣减酒店余位数
			Map<String, String> deductResult = activityHotelServiceImpl.deductRemNumber(hotelOrder.getActivityHotelGroupUuid(), houseNum);
			String resultStr = deductResult.get("result");
			if ("fail".equals(resultStr)) {
				return 0;
			}
			
			hotelOrder.setApplyTime(new Date());//转报名时间
		}
		hotelOrderDao.updateObj(hotelOrder);
		
		//添加操作日志
		super.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, 
				json.getString("hotelOrderRemark"), Context.log_state_up, Context.ProductType.PRODUCT_HOTEL, hotelOrder.getId().longValue());

		return 1;
	}
	
	/**
	 * 批量保存金额
	 * @param moneyArr  	
	 * @param orderUuid	订单uuid	
	 * @param serialNum 流水号
	 */
	private  void saveOrderMoney(JSONArray moneyArr, String orderUuid, String serialNum, Integer moneyType, String oldUuid) {
		if(moneyArr != null && moneyArr.size() > 0){
			List<HotelMoneyAmount> saveMoneyAmountList = Lists.newArrayList();	//批量保存金额
			for(int i = 0 ; i < moneyArr.size(); i++){
				JSONObject moneyJson = (JSONObject)moneyArr.get(i);
				
				HotelMoneyAmount hotelMoneyAmount  = new HotelMoneyAmount();
				
				@SuppressWarnings("rawtypes")
				Iterator it = moneyJson.keys();
				while(it.hasNext()){
					String key = (String)it.next();
					Double amount = moneyJson.getDouble(String.valueOf(key));
					
					hotelMoneyAmount.setUuid(UuidUtils.generUuid());//uuid
					hotelMoneyAmount.setCurrencyId(Integer.valueOf(key));	//币种ID
					hotelMoneyAmount.setAmount(amount);	//金额
					hotelMoneyAmount.setBusinessUuid(orderUuid);//订单uuid
					hotelMoneyAmount.setSerialNum(serialNum);//流水号
					hotelMoneyAmount.setBusinessType(1);
					hotelMoneyAmount.setBusinessUuid(orderUuid);
					hotelMoneyAmount.setMoneyType(moneyType);
					hotelMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
					
					List<HotelMoneyAmount> exsitMaList = hotelMoneyAmountDao.findAmountBySerialNumAndCurrencyId(oldUuid, Integer.valueOf(key));
					if (exsitMaList != null && exsitMaList.size() > 0) {
						hotelMoneyAmount.setId(exsitMaList.get(0).getId());
						hotelMoneyAmount.setExchangerate(exsitMaList.get(0).getExchangerate());
					} else {
						Currency currency = currencyDao.findOne(Long.parseLong(hotelMoneyAmount.getCurrencyId().toString()));
						hotelMoneyAmount.setExchangerate(currency.getConvertLowest().doubleValue());
					}
					
					super.setOptInfo(hotelMoneyAmount, BaseService.OPERATION_ADD);
					saveMoneyAmountList.add(hotelMoneyAmount);
				}
			}
			hotelMoneyAmountDao.batchSave(saveMoneyAmountList);
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
			List<OrderContacts> allContactsList = orderContactsService.findOrderContactsByOrderIdAndOrderType(orderid, Context.ORDER_TYPE_HOTEL);
			for(int i = 0 ; i < contactsArr.size(); i++){
				JSONObject contacts = (JSONObject)contactsArr.get(i);
				
				OrderContacts orderContacts = new OrderContacts();
				String contactsId = contacts.getString("contactsId");
				if(StringUtils.isNotEmpty(contactsId)){
					orderContacts = orderContactsDao.getById(contacts.getLong("contactsId"));
				}
				
				orderContacts.setOrderId(orderid);	//订单id
				orderContacts.setOrderType(Context.ORDER_TYPE_HOTEL);	//订单类型
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
					super.setOptInfo(orderContacts, BaseService.OPERATION_UPDATE);
					updateContactsList.add(orderContacts);
				}else{
					super.setOptInfo(orderContacts, BaseService.OPERATION_ADD);
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
			List<HotelOrderPrice> saveOrderPriceList = new ArrayList<HotelOrderPrice>();	//批量保存订单价格
			List<HotelOrderPrice> updateOrderPriceList = new ArrayList<HotelOrderPrice>();//批量更新订单价格
			for(int i = 0 ; i < moneyChangeArr.size(); i++){
				JSONObject orderPrice = (JSONObject)moneyChangeArr.get(i);
				
				HotelOrderPrice hotelOrderPrice = new HotelOrderPrice();
				String orderPriceUuid = orderPrice.getString("orderPriceUuid");
				if(StringUtils.isNotEmpty(orderPriceUuid)){
					hotelOrderPrice = hotelOrderPriceDao.getByUuid(orderPriceUuid);
				}
				
				hotelOrderPrice.setUuid(orderPrice.getString("orderPriceUuid"));	//uuid
				hotelOrderPrice.setOrderUuid(orderUuid);   		 	//订单Uuid
				hotelOrderPrice.setPriceType(orderPrice.getInt("priceType"));	//价格类型
				hotelOrderPrice.setCurrencyId(orderPrice.getInt("currencyId"));	//币种
				hotelOrderPrice.setPrice(orderPrice.getDouble("orderPrice"));	//价格
				hotelOrderPrice.setRemark(orderPrice.getString("orderPriceRemark"));	//备注
				hotelOrderPrice.setPriceName(orderPrice.getString("priceName"));	//金额名称
				
				if(StringUtils.isNotEmpty(orderPriceUuid)){
					super.setOptInfo(hotelOrderPrice, BaseService.OPERATION_UPDATE);
					updateOrderPriceList.add(hotelOrderPrice);
				}else{
					hotelOrderPrice.setUuid(UuidUtils.generUuid());
					super.setOptInfo(hotelOrderPrice, BaseService.OPERATION_ADD);
					saveOrderPriceList.add(hotelOrderPrice);
				}
			}
			hotelOrderPriceDao.batchSave(saveOrderPriceList);
			hotelOrderPriceDao.batchUpdate(updateOrderPriceList);
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
			List<HotelTravelervisa> saveVisaList = new ArrayList<HotelTravelervisa>();	//批量保存签证信息
			List<HotelTravelervisa> updateVisaList = new ArrayList<HotelTravelervisa>();	//批量修改签证信息
			for(int k = 0 ; k < visaArr.size(); k++){
				JSONObject travelervisa = (JSONObject)visaArr.get(k);
				
				HotelTravelervisa hotelTravelervisa = new HotelTravelervisa();
				String travelerVisaUuid = travelervisa.getString("travelerVisaUuid");//酒店游客签证信息表uuid
				//修改列表
				if(StringUtils.isNotEmpty(travelerVisaUuid)){
					hotelTravelervisa = hotelTravelervisaDao.getByUuid(travelerVisaUuid);
					hotelTravelervisa.setCountry(travelervisa.getString("country"));	//签证国家
					hotelTravelervisa.setVisaTypeId(travelervisa.getInt("visaType"));	//签证类型
					
					super.setOptInfo(hotelTravelervisa, BaseService.OPERATION_UPDATE);
					updateVisaList.add(hotelTravelervisa);
				}else{
					hotelTravelervisa.setUuid(UuidUtils.generUuid());	//uuid
					hotelTravelervisa.setHotelOrderUuid(orderUuid);		//订单uuid
					hotelTravelervisa.setHotelTravelerUuid(travelerUuid);	//游客uuid
					hotelTravelervisa.setCountry(travelervisa.getString("country"));	//签证国家
					hotelTravelervisa.setVisaTypeId(travelervisa.getInt("visaType"));	//签证类型
					
					super.setOptInfo(hotelTravelervisa, BaseService.OPERATION_ADD);
					saveVisaList.add(hotelTravelervisa);
				}
			}
			//批量保存
			if(CollectionUtils.isNotEmpty(saveVisaList)){
				hotelTravelervisaDao.batchSave(saveVisaList);
			}
			//批量修改
			if(CollectionUtils.isNotEmpty(updateVisaList)){
				hotelTravelervisaDao.batchUpdate(updateVisaList);
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
			List<HotelTravelerPapersType> savetravelerPapersTypeList = new ArrayList<HotelTravelerPapersType>();	//批量保存证件类型信息
			List<HotelTravelerPapersType> updatetravelerPapersTypeList = new ArrayList<HotelTravelerPapersType>();	//批量修改证件类型信息
			for(int i = 0 ; i < papersTypeArr.size(); i++){
				JSONObject travelerPapersType = (JSONObject)papersTypeArr.get(i);
				
				HotelTravelerPapersType hotelTravelerPapersType = new HotelTravelerPapersType();
				String travelerPapersUuid = travelerPapersType.getString("travelerPapersUuid");	//uuid
				
				if(StringUtils.isNotEmpty(travelerPapersUuid)){
					hotelTravelerPapersType = hotelTravelerPapersTypeDao.getByUuid(travelerPapersUuid);
					hotelTravelerPapersType.setPapersType(travelerPapersType.getInt("papersType"));	//证件类型
					hotelTravelerPapersType.setIdCard(travelerPapersType.getString("idCard"));			//证件号码
					hotelTravelerPapersType.setValidityDateString(travelerPapersType.getString("validityDate"));//有效期
					
					super.setOptInfo(hotelTravelerPapersType, BaseService.OPERATION_UPDATE);
					updatetravelerPapersTypeList.add(hotelTravelerPapersType);
				}else{
					hotelTravelerPapersType.setUuid(UuidUtils.generUuid());//uuid
					hotelTravelerPapersType.setOrderUuid(orderUuid);	//订单uuid
					hotelTravelerPapersType.setHotelTravelerUuid(travelerUuid);//游客uuid
					hotelTravelerPapersType.setPapersType(travelerPapersType.getInt("papersType"));	//证件类型
					hotelTravelerPapersType.setIdCard(travelerPapersType.getString("idCard"));			//证件号码
					hotelTravelerPapersType.setValidityDateString(travelerPapersType.getString("validityDate"));//有效期
					
					super.setOptInfo(hotelTravelerPapersType, BaseService.OPERATION_ADD);
					savetravelerPapersTypeList.add(hotelTravelerPapersType);
				}
			}
			//批量保存
			if(CollectionUtils.isNotEmpty(savetravelerPapersTypeList)){
				hotelTravelerPapersTypeDao.batchSave(savetravelerPapersTypeList);
			}
			//批量修改
			if(CollectionUtils.isNotEmpty(updatetravelerPapersTypeList)){
				hotelTravelerPapersTypeDao.batchUpdate(updatetravelerPapersTypeList);
			}
		}
	}
	
	/**
	 * 批量保存或者修改游客金额信息
	 * @param travelerMoneyArr
	 * @param orderUuid
	 * @param travelerUuid
	 */
	private void saveOrUpdateTravelerMoneyAmount(JSONArray travelerMoneyArr,String orderUuid,String travelerUuid, HotelTraveler hotelTraveler){
		if(travelerMoneyArr != null && travelerMoneyArr.size() > 0){
			List<HotelMoneyAmount> saveMoneyAmountList = new ArrayList<HotelMoneyAmount>();	//批量保存金额信息
			List<HotelMoneyAmount> updateMoneyAmountList = new ArrayList<HotelMoneyAmount>();//批量修改金额信息
			for(int i = 0 ; i < travelerMoneyArr.size(); i++){
				JSONObject travelerMoney = (JSONObject)travelerMoneyArr.get(i);
				
				HotelMoneyAmount hotelMoneyAmount = new HotelMoneyAmount();
				String travelerMoneyUuid = travelerMoney.getString("travelerMoneyUuid");	//uuid
				
				if(StringUtils.isNotEmpty(travelerMoneyUuid)){
					hotelMoneyAmount = hotelMoneyAmountDao.getByUuid(travelerMoneyUuid);
					hotelMoneyAmount.setCurrencyId(travelerMoney.getInt("currencyId"));	//币种
					hotelMoneyAmount.setAmount(travelerMoney.getDouble("travelerMoney"));	//金额
					//这条moneyamount记录是游客的结算价，所以serialNum应该对应游客的payPriceSerialNum，况且它的moneyType=14
					hotelMoneyAmount.setSerialNum(hotelTraveler.getPayPriceSerialNum());
					super.setOptInfo(hotelMoneyAmount, BaseService.OPERATION_UPDATE);
					updateMoneyAmountList.add(hotelMoneyAmount);
				}else{
					
					for (int j=0; j<3; j++) {
						hotelMoneyAmount = new HotelMoneyAmount();
						hotelMoneyAmount.setUuid(UuidUtils.generUuid());
						hotelMoneyAmount.setCurrencyId(travelerMoney.getInt("currencyId"));	//币种
						hotelMoneyAmount.setAmount(travelerMoney.getDouble("travelerMoney"));	//金额
						hotelMoneyAmount.setBusinessUuid(travelerUuid);//游客uuid
						hotelMoneyAmount.setBusinessType(2);	//业务类型
						//保存金额类型：原始结算价、结算价、成本价
						if (j == 0) {
							hotelMoneyAmount.setMoneyType(Context.MONEY_TYPE_YSJSJ);
							hotelMoneyAmount.setSerialNum(hotelTraveler.getOriginalPayPriceSerialNum());
						} else if (j == 1) {
							hotelMoneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);
							hotelMoneyAmount.setSerialNum(hotelTraveler.getPayPriceSerialNum());
						} else {
							hotelMoneyAmount.setMoneyType(Context.MONEY_TYPE_CBJ);
							hotelMoneyAmount.setSerialNum(hotelTraveler.getCostPriceSerialNum());
						}
						
						super.setOptInfo(hotelMoneyAmount, BaseService.OPERATION_ADD);
						saveMoneyAmountList.add(hotelMoneyAmount);
					}
				}
			}
			//批量保存
			if(CollectionUtils.isNotEmpty(saveMoneyAmountList)){
				hotelMoneyAmountDao.batchSave(saveMoneyAmountList);
			}
			//批量修改
			if(CollectionUtils.isNotEmpty(updateMoneyAmountList)){
				hotelMoneyAmountDao.batchUpdate(updateMoneyAmountList);
			}
		}
	}
	
	/**
	 * 保存附件信息
	 * @param fileArr
	 * @param travelerUuid
	 */
	private void saveFile(JSONArray fileArr,String travelerUuid) {
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
	
	/**
	 * 获取订单基本信息
	 * @param orderUuid
	 * @param model
	 */
	public void getOrderBaseInfo(String orderUuid, Model model) {
		//获取订单信息
		HotelOrder hotelOrder = getByUuid(orderUuid);
		//获取产品信息
		ActivityHotel activityHotel = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
		//获取产品团期
		ActivityHotelGroup activityHotelGroup = activityHotelGroupService.getByUuid(hotelOrder.getActivityHotelGroupUuid());
		//获取酒店信息
		Hotel hotel = hotelService.getByUuid(activityHotel.getHotelUuid());
		Integer hotelLevel = null;
		if (hotel != null) {
			HotelStar hotelStar = hotelStarService.getByUuid(hotel.getStar());
			if (hotelStar != null) {
				hotelLevel = hotelStar.getValue();
			}
		}
		//加载团期下所有的基础餐型数据
		List<ActivityHotelGroupMeal> groupMeals = activityHotelGroupMealService.getMealListByGroupUuid(activityHotelGroup.getUuid());
		//加载团期下所有的房型数据
		List<ActivityHotelGroupRoom> groupRooms = activityHotelGroupRoomService.getByactivityHotelGroupUuid(activityHotelGroup.getUuid());
		//预报名
		activityHotelGroup.setOrderNum(getForecaseReportNum(activityHotelGroup.getUuid()));
		
		//获取费用及人数
		List<HotelOrderPrice> groupPrices = Lists.newArrayList();
		//获取订单价格列表
		List<HotelOrderPrice> hotelOrderPriceList = hotelOrderPriceService.getByOrderUuid(orderUuid);
		if (CollectionUtils.isNotEmpty(hotelOrderPriceList)) {
			for (HotelOrderPrice hotelOrderPrice : hotelOrderPriceList) {
				//获取团期价格
				if (hotelOrderPrice != null && "1".equals(hotelOrderPrice.getPriceType().toString())) {
					int num = hotelOrderPrice.getNum() == null ? 0:hotelOrderPrice.getNum();	//人数
					Double price = hotelOrderPrice.getPrice() == null ? 0 : hotelOrderPrice.getPrice();	//价钱
					Double total = price * num;		//小计
					String totalStr = new java.text.DecimalFormat("#.00").format(total);
					hotelOrderPrice.setSubTotal(new BigDecimal(totalStr));	//设置小计金额
					groupPrices.add(hotelOrderPrice);
				}
			}
		}
		
		//获取订单总额(成本价)
		String costMoneyStr =  hotelMoneyAmountService.getMoneyStr(hotelOrder.getCostMoney(), true);
		//获取订单结算总额(结算价,应收金额)
		String totalMoneyStr = hotelMoneyAmountService.getMoneyStr(hotelOrder.getTotalMoney(), true);
		//获取订单已付金额(已付金额)
		String payedMoneyStr = hotelMoneyAmountService.getMoneyStr(hotelOrder.getPayedMoney(), true);
		//订单未收金额( totalMoneyStr - payedMoneyStr  )
		String noPayMoneyStr = "";
		if(!StringUtils.isEmpty(totalMoneyStr) && !StringUtils.isEmpty(payedMoneyStr) ){
			 noPayMoneyStr =  hotelMoneyAmountService.addOrSubtract(totalMoneyStr.replace(",", "").trim(),
					 payedMoneyStr.replace(",", "").trim(), false);
			 if (StringUtils.isNotBlank(noPayMoneyStr) && noPayMoneyStr.split(" ").length == 2) {
				 DecimalFormat d = new DecimalFormat(",##0.00");
				 noPayMoneyStr = noPayMoneyStr.split(" ")[0] + " " + d.format(new BigDecimal(noPayMoneyStr.split(" ")[1]));
			 }
		}
		
		//订单基本信息
		model.addAttribute("hotelOrder", hotelOrder);
		model.addAttribute("activityHotel", activityHotel);
		model.addAttribute("activityHotelGroup", activityHotelGroup);
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
		List<String> returnList = hotelOrderDao.findBySql(sql);
		if (CollectionUtils.isNotEmpty(returnList)) {
			return returnList.get(0);
		}
		return "";
	}
}
