/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.eprice.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.eprice.form.SpeedOrderInput;
import com.trekiz.admin.modules.eprice.service.SpeedGenOrderService;
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
import com.trekiz.admin.modules.hotel.entity.ActivityHotelShare;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelOrderPrice;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerPapersType;
import com.trekiz.admin.modules.hotel.entity.HotelTravelervisa;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupLowpriceService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupPriceService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupRoomService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelShareService;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SpeedGenOrderServiceImpl  extends BaseService implements SpeedGenOrderService{
	@Autowired
	private HotelOrderDao hotelOrderDao;
	@Autowired
	private ActivityHotelGroupDao activityHotelGroupDao;
	@Autowired
	private ActivityHotelGroupPriceDao activityHotelGroupPriceDao;
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
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private ActivityHotelGroupPriceService activityHotelGroupPriceService;
	@Autowired
    private	ActivityHotelGroupRoomService activityHotelGroupRoomService;
	@Autowired
	private ActivityHotelGroupMealService activityHotelGroupMealService;
	@Autowired
	private ActivityHotelShareService activityHotelShareService;
	@Autowired
	private ActivityHotelGroupLowpriceService activityHotelGroupLowpriceService; 
	
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
	public HotelOrder getByUuid(String uuid) {
		return hotelOrderDao.getByUuid(uuid);
	}
	
	/**
	 * 保存酒店产品信息，酒店预报名
	 * @author star
	 * @param speedOrderInput
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, String> saveHotelOrder(SpeedOrderInput speedOrderInput,HttpServletRequest request){
		if("1".equals(speedOrderInput.getSelNo())){
			saveActivityHotel(speedOrderInput, request);
		}
		
		//重建前端传递数据
		rebuildInputData(speedOrderInput);
		
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
			hotelOrder = speedOrderInput.getHotelOrder();
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
			//如果是非签约渠道，给一个默认值-1
			if(hotelOrder.getOrderCompany()==null){
				hotelOrder.setOrderCompany(-1);
			}
			
			//保存酒店订单以及子订单数据
			this.save(hotelOrder);
			//更新一下下单人
			hotelOrder.setCreateBy(speedOrderInput.getOrderMan());
			this.update(hotelOrder);
			
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
					if(hotelOrderPrice==null){
						continue;
					}
					hotelOrderPrice.setOrderUuid(hotelOrder.getUuid());
					super.setOptInfo(hotelOrderPrice, BaseService.OPERATION_ADD);
				}
			}
			
			//初始化酒店moneyAmount数据
			if(CollectionUtils.isNotEmpty(hotelMoneyAmountList)) {
				for(HotelMoneyAmount hotelMoneyAmount : hotelMoneyAmountList) {
					if(hotelMoneyAmount==null){
						continue;
					}
					hotelMoneyAmount.setBusinessUuid(hotelOrder.getUuid());
					hotelMoneyAmount.setBusinessType(HotelMoneyAmount.BUSINESS_TYPE_ORDER);
					if(hotelMoneyAmount.getCurrencyId()!=null){
						BigDecimal convertLowest = currencyDao.findConvertLowestById(hotelMoneyAmount.getCurrencyId().longValue());
						if(convertLowest != null) {
							hotelMoneyAmount.setExchangerate(convertLowest.doubleValue());
						} else {
							BigDecimal exchangerage = currencyDao.findExchangerageById(hotelMoneyAmount.getCurrencyId().longValue());
							if (exchangerage != null) {
								hotelMoneyAmount.setExchangerate(exchangerage.doubleValue());
							}
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
					if(hotelTraveler==null){
						continue;
					}
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
					hotelTraveler.setOrderUuid(hotelOrder.getUuid());
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
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
	private void rebuildInputData(SpeedOrderInput speedOrderInput) {
		//处理结算总额、订单总额（涉及多币种）---------
		String[] hotelMoneyAmount_amountArr = speedOrderInput.getHotelMoneyAmount_amount();
		String[] hotelMoneyAmount_currencyIdArr = speedOrderInput.getHotelMoneyAmount_currencyId();
		String[] hotelMoneyAmount_moneyTypeArr = speedOrderInput.getHotelMoneyAmount_moneyType();

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
		
		speedOrderInput.setHotelMoneyAmount_amount((String[])amountBak.toArray(new String[amountBak.size()]));
		speedOrderInput.setHotelMoneyAmount_currencyId((String[])currencyBak.toArray(new String[currencyBak.size()]));
		speedOrderInput.setHotelMoneyAmount_moneyType((String[])moneyTypeBak.toArray(new String[moneyTypeBak.size()]));
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
				if(entity==null){
					continue;
				}
				//当存储游客金额时：
				if(IslandMoneyAmount.BUSINESS_TYPE_TRAVELER == entity.getBusinessType()) {
					if((hotelMoneyAmount.getCurrencyId()!=null) && (hotelMoneyAmount.getCurrencyId().intValue() == entity.getCurrencyId())) {
						entity.setAmount(entity.getAmount() + hotelMoneyAmount.getAmount());
						hotelMoneyAmountBakIter.remove();
						
						continue flag;
					}
				//存储其他金额时：
				} else {
					if((hotelMoneyAmount.getCurrencyId()!=null && hotelMoneyAmount.getMoneyType()!=null) && (hotelMoneyAmount.getCurrencyId().intValue() == entity.getCurrencyId()) 
							&& hotelMoneyAmount.getMoneyType().intValue() == entity.getMoneyType().intValue()) {
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
	 * 清除session中Orderpay的缓存
	 * @param role
	 * @return
	 */
	public Object clearObject(Object object){
		hotelOrderDao.getSession().evict(object);
		return object;
	}
	
	/**
	 * 保存产品相关信息
	 * @param speedOrderInput
	 * @param request
	 */
	public void saveActivityHotel(SpeedOrderInput speedOrderInput,HttpServletRequest request) {
		if("1".equals(speedOrderInput.getSelNo())){
			//保存酒店产品信息
			ActivityHotel activityHotel = new ActivityHotel();
			activityHotel.setActivityName(speedOrderInput.getActivityName());
			activityHotel.setActivitySerNum(speedOrderInput.getActivitySerNum());
			activityHotel.setCountry("".equals(request.getParameter("country"))?speedOrderInput.getCountry():request.getParameter("country"));
			activityHotel.setIslandUuid(speedOrderInput.getIsland());
			activityHotel.setHotelUuid(speedOrderInput.getHotel());
			activityHotel.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
			Set<Department> userDeptList = UserUtils.getDepartmentByJob();
			if (CollectionUtils.isNotEmpty(userDeptList)) {
				activityHotel.setDeptId(Integer.parseInt(userDeptList.iterator().next().getId().toString()));
			}
			activityHotelService.save(activityHotel);
			speedOrderInput.setActivityHotelUuid(activityHotel.getUuid());
			
			//保存产品共享信息
			ActivityHotelShare share = new ActivityHotelShare();
			share.setActivityHotelUuid(activityHotel.getUuid());
			share.setDelFlag("0");
			share.setShareUser(UserUtils.getUser().getId());
			share.setAcceptShareUser(speedOrderInput.getOrderMan()+0L);
			activityHotelShareService.save(share);
			
			//保存酒店团期信息
			ActivityHotelGroup activityHotelGroup = new ActivityHotelGroup();
			activityHotelGroup.setActivityHotelUuid(activityHotel.getUuid());
			activityHotelGroup.setGroupCode(speedOrderInput.getGroupCode());
			if(StringUtils.isNotBlank(speedOrderInput.getIslandWay())){
				//数据库字段长度为200,页面选择的上岛方式可能超过这个范围
				if(speedOrderInput.getIslandWay().length()>200){
					activityHotelGroup.setIslandWay(speedOrderInput.getIslandWay().replaceAll(",", ";").substring(0, speedOrderInput.getIslandWay().length()));
				}else{
					activityHotelGroup.setIslandWay(speedOrderInput.getIslandWay().replaceAll(",", ";"));
				}
			}
			activityHotelGroup.setStatus(BaseEntity.DEL_FLAG_NORMAL);
			activityHotelGroup.setGroupOpenDate(DateUtils.string2Date(speedOrderInput.getGroupOpenDateString(), 
												DateUtils.DATE_PATTERN_YYYY_MM_DD));
			if(StringUtils.isNotBlank(speedOrderInput.getSinglePrice())){
				activityHotelGroup.setSinglePrice(Double.parseDouble(speedOrderInput.getSinglePrice()));
			}
			if(StringUtils.isNotBlank(speedOrderInput.getFrontMoney())){
				activityHotelGroup.setFrontMoney(Double.parseDouble(speedOrderInput.getFrontMoney()));
			}
			activityHotelGroup.setCurrencyId(Integer.parseInt(speedOrderInput.getSingleCurrency()));
			activityHotelGroup.setFrontMoneyCurrencyId(Integer.parseInt(speedOrderInput.getFrontCurrency()));
			activityHotelGroup.setStatus("1");
			activityHotelGroup.setRemNumber(speedOrderInput.getRemNumber());//设置一下余位，页面手动录入
			activityHotelGroupService.save(activityHotelGroup);
			speedOrderInput.setActivityHotelGroupUuid(activityHotelGroup.getUuid());
			
			//保存团期价格表
			String[] travelType = speedOrderInput.getHotelOrderPrice_activityHotelGroupPriceUuid();
			int length = travelType.length;
			
			String[] currencyId = speedOrderInput.getHotelOrderPrice_currencyId();
			String[] travelPrice = speedOrderInput.getHotelOrderPrice_price();
			String[] newPriceUuids = new String[length];
			for(int i=0;i<length;i++){
				if("_".equals(travelType[i])){
					newPriceUuids[i] = travelType[i];
					continue;
				}
				ActivityHotelGroupPrice price = new ActivityHotelGroupPrice();
				price.setActivityHotelUuid(activityHotel.getUuid());
				price.setActivityHotelGroupUuid(activityHotelGroup.getUuid());
				price.setType(travelType[i]);
				price.setCurrencyId(Integer.parseInt(currencyId[i]));
				price.setPrice(Double.valueOf(travelPrice[i]));
				activityHotelGroupPriceService.save(price);
				newPriceUuids[i] = price.getUuid();
			}
			speedOrderInput.setHotelOrderPrice_activityHotelGroupPriceUuid(newPriceUuids);
			
			//保存最低价
			activityHotelGroupLowpriceService.getPriceList(activityHotel.getUuid());
			
			//保存房型
			int roomlength = 0;
			if(ArrayUtils.isNotEmpty(speedOrderInput.getRoomtype())){
				roomlength = speedOrderInput.getRoomtype().length;
			}
			String allfood = request.getParameter("allfood");
			String[] foodArray = null;
			if(StringUtil.isNotBlank(allfood)){
				foodArray = allfood.split(";");
			}
			if(roomlength>0){
				for(int i=0;i<roomlength;i++){
					ActivityHotelGroupRoom room = new ActivityHotelGroupRoom();
					room.setActivityHotelUuid(activityHotel.getUuid());// 一级表uuid
					room.setActivityHotelGroupUuid(activityHotelGroup.getUuid());// 二级表uuid
					if(ArrayUtils.isNotEmpty(speedOrderInput.getNight()) && StringUtils.isNotBlank(speedOrderInput.getNight()[i])){
						room.setNights(Integer.parseInt(speedOrderInput.getNight()[i]));
					}
					room.setHotelRoomUuid(speedOrderInput.getRoomtype()[i]);
					super.setOptInfo(room,BaseService.OPERATION_ADD);// 添加默认信息
					activityHotelGroupRoomService.save(room);
					
					//如果allfood不为空   为新建团号使用，用户勾选房型餐型      否则，房型餐型由报价信息带出
					if(StringUtils.isNotBlank(allfood)){
						int meallength = 0;
						String mealArray[] = null;
						if(ArrayUtils.isNotEmpty(foodArray) && StringUtils.isNotBlank(foodArray[i])){
							mealArray = foodArray[i].split(",");
							meallength = mealArray.length;
						}
						if(meallength>0){
							for(int j=0;j<meallength;j++){
								ActivityHotelGroupMeal meal = new ActivityHotelGroupMeal();
								meal.setActivityHotelUuid(room.getActivityHotelUuid());
								meal.setActivityHotelGroupUuid(room.getActivityHotelGroupUuid());
								meal.setActivityHotelGroupRoomUuid(room.getUuid());
								meal.setHotelMealUuid(mealArray[j]);
								super.setOptInfo(meal,BaseService.OPERATION_ADD);// 添加默认信息
								activityHotelGroupMealService.save(meal);
							}
						}
					}else{
						//int meallength = 0;
						String[] mealArray = null;
						if(ArrayUtils.isNotEmpty(speedOrderInput.getFoodtype())){
							mealArray = speedOrderInput.getFoodtype();
							//meallength = mealArray.length;
						}
						if(StringUtils.isNotBlank(mealArray[i])){
							ActivityHotelGroupMeal meal = new ActivityHotelGroupMeal();
							meal.setActivityHotelUuid(room.getActivityHotelUuid());
							meal.setActivityHotelGroupUuid(room.getActivityHotelGroupUuid());
							meal.setActivityHotelGroupRoomUuid(room.getUuid());
							meal.setHotelMealUuid(mealArray[i]);
							super.setOptInfo(meal,BaseService.OPERATION_ADD);// 添加默认信息
							activityHotelGroupMealService.save(meal);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取当前批发商下所有已上架的团期
	 * @param saleId
	 * @return
	 */
	public List<ActivityHotelGroup> getListBySaleId(String saleId){
		String sql = "select id,uuid,activity_hotel_uuid,groupCode,groupOpenDate,groupEndDate,island_way,singlePrice,currency_id,singlePriceUnit,"+
                     "control_num,uncontrol_num,remNumber,airline,priority_deduction,front_money,front_money_currency_id,memo,status,"+
                     "lockStatus,forcastStatus,createBy,createDate,updateBy,updateDate,delFlag,iscommission from activity_hotel_group ahg " +
                     "where ahg.delFlag='0' and ahg.status='1' and exists "+
	                 "(select 1 from activity_hotel ah where ah.delFlag='0' and ah.uuid = ahg.activity_hotel_uuid and ah.wholesaler_id = ?)";
		return this.activityHotelGroupDao.findBySql(sql,ActivityHotelGroup.class,saleId);
	}
	
}
