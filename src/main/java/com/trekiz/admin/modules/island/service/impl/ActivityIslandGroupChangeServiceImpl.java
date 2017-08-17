package com.trekiz.admin.modules.island.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.geography.dao.SysGeographyDao;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandOrderPrice;
import com.trekiz.admin.modules.island.entity.IslandTransFerGroup;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.entity.IslandTravelerPapersType;
import com.trekiz.admin.modules.island.entity.IslandTravelervisa;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupPriceQuery;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupQuery;
import com.trekiz.admin.modules.island.query.IslandTravelerPapersTypeQuery;
import com.trekiz.admin.modules.island.query.IslandTravelervisaQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupChangeService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupPriceService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderControlDetailService;
import com.trekiz.admin.modules.island.service.IslandOrderPriceService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandTravelerPapersTypeService;
import com.trekiz.admin.modules.island.service.IslandTravelerService;
import com.trekiz.admin.modules.island.service.IslandTravelervisaService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupChangeServiceImpl extends BaseService implements ActivityIslandGroupChangeService {


	@Autowired
	private ReviewService reviewService;

	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private IslandTravelerService islandTravelerService;
	@Autowired
	private IslandTravelervisaService islandTravelervisaService;
	@Autowired
	private IslandTravelerPapersTypeService islandTravelerPapersTypeService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private ActivityIslandGroupPriceService activityIslandGroupPriceService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private IslandOrderPriceService islandOrderPriceService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private IslandOrderControlDetailService islandOrderControlDetailService;
	@Autowired
	private SysGeographyDao sysGeographyDao;
	@Autowired
	private OrderContactsService orderContactsService;
	
	/**
	 * 转团申请操作，为一个或多个游客进行转团申请，如果申请失败，需要整体回滚。
	 * @author gao
	 * @param travelList 全部要转团的游客
	 * @param oldOrder 原订单实体
	 * @param  String[] remark 转团理由
	 * @param  String groupCode
	 * @param  TravelActivity ta 原产品实体
	 * @param  String remainDays 保留天数
	 * @param listDetail
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Map<String, String> addReviewList(
			List<com.trekiz.admin.modules.island.entity.IslandTraveler> travelList,
			com.trekiz.admin.modules.island.entity.IslandOrder oldOrder,
			String[] remark, ActivityIsland ta, String groupCode, String newRoom,
			String newTicket,String oldRoomControl,String oldRoomNoControl,
			String oldTicketControl,String oldTicketNoControl,
			StringBuffer reply,Map<String, String> map) throws Exception {
		for(int n=0;n<travelList.size();n++){
			IslandTraveler t = travelList.get(n);
			// 转团实体
			IslandTransFerGroup tran = getIslandTransFerGroup(t, oldOrder, n, remark, groupCode);
			List<Detail>  listDetail = getDetailList(tran);	// 申请细节
			// 提交申请(包含原产品部门)
			//Long back= reviewService.addReview(Context.ProductType.PRODUCT_ISLAND, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, oldOrder.getId().toString(), Long.valueOf(t.getId()), Long.valueOf(n), remark[n], reply, listDetail,Long.valueOf(ta.getDeptId()));
			// 提交申请直接通过
			Long back = reviewService.addSuccessReview(Context.ProductType.PRODUCT_ISLAND, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, oldOrder.getId().toString(), Long.valueOf(t.getId()), Long.valueOf(n), remark[n], reply, listDetail,Long.valueOf(ta.getDeptId()));
			if(back==0){
				throw new Exception(reply.toString());
			}
			// 游客生成新订单加入新团期
			try{
				newTravelGroup(t,groupCode,oldOrder, newRoom, newTicket);
			}catch(Exception e){
				reply.append("转团时生成新订单失败，请检查输入参数");
				throw new Exception(reply.toString());
			}
			// 游客在原订单转团
			try{
				refundGroup(t,oldOrder, oldRoomControl, oldRoomNoControl, oldTicketControl, oldTicketNoControl);
			}catch(Exception e){
				reply.append("修改原订单失败，请检查输入参数");
				throw new Exception(reply.toString());
			}
//			// 获得审核实体
//			Review re = reviewService.findReviewInfo(back);
//			// 审核自动通过
//			reviewService.UpdateReview(back, re.getNowLevel(), 1, null);
		}
		map.put("res", "success");
		map.put("message", "申请成功，请等待审核");
		return map;
	}
	/**
	 * 将转团游客状态改为”已转团“
	 * @author gao
	 * @param travel
	 */
	private void refundGroup(IslandTraveler travel,com.trekiz.admin.modules.island.entity.IslandOrder oldOrder,
			String oldRoomControl,String oldRoomNoControl,String oldTicketControl,String oldTicketNoControl){
		if(travel!=null){
			travel.setStatus(Context.TRAVELER_DELFLAG_TURNROUNDED.toString()); // 将状态置为已转团
		}
		if(oldOrder!=null){
			Integer num = oldOrder.getOrderPersonNum(); // 获取原订单人数
			oldOrder.setOrderPersonNum(num-1); // 将原订单人数减一
			// 归还控票机票和非控票机票
			if(StringUtils.isNotBlank(oldTicketControl) && StringUtils.isNotBlank(oldTicketNoControl) 
					&& Integer.valueOf(oldTicketControl)>0 && Integer.valueOf(oldTicketNoControl)>0 ){
				Integer sum = oldOrder.getSubControlTicketNum();
				Integer sumUn = oldOrder.getSubUnControlTicketNum();
				oldOrder.setSubControlTicketNum(sum-Integer.valueOf(oldTicketControl));
				oldOrder.setSubUnControlTicketNum(sumUn-Integer.valueOf(oldTicketNoControl));
			}
			// 归还控房和非控房
			if(StringUtils.isNotBlank(oldRoomControl) && Integer.valueOf(oldRoomControl)>0
					&& StringUtils.isNotBlank(oldRoomNoControl) && Integer.valueOf(oldRoomNoControl)>0){
				Integer sum = oldOrder.getSubControlNum();
				Integer sumUn = oldOrder.getSubUnControlNum();
				oldOrder.setSubControlNum(sum-Integer.valueOf(oldRoomControl));
				oldOrder.setSubUnControlNum(sumUn-Integer.valueOf(oldRoomNoControl));
			}
			islandOrderService.save(oldOrder); // 保存原订单
			
			
		}
		
		// 暂时只在订单中归还，不必归还其他
//		// 归还旧团期 酒店控房
//		if(StringUtils.isNotBlank(oldRoomIn) && Integer.valueOf(oldRoomIn)>0){
//			islandOrderControlDetailService.find();
//		}
//		if(StringUtils.isNotBlank(oldRoomOut) && Integer.valueOf(oldRoomOut)>0){
//			
//		}
//		// 归还旧团期 机票控票
//		if(StringUtils.isNotBlank(oldTicket) && Integer.valueOf(oldTicket)>0){
//			
//		}
	}
	/**
	 * 将转团游客加入新生成订单，转入新团期
	 * @author gao
	 * @param travel 转团游客
	 * @param groupCode 新团期code
	 * @param oldOrder 原订单
	 */
	private void newTravelGroup(IslandTraveler travel,String groupCode,IslandOrder oldOrder,String newRoom,String newTicket)  throws  Exception{
		// 获取新团期
		ActivityIslandGroupQuery query = new ActivityIslandGroupQuery();
		query.setGroupCode(groupCode); // 团期编号
		query.setStatus("1"); // 团期状态（已上架）
		List<ActivityIslandGroup> groupList =activityIslandGroupService.find(query);
		if(groupList!=null && !groupList.isEmpty()){
			newIslandOrder(travel,groupList.get(0),oldOrder,newRoom, newTicket);
		}
	}
	
	/**
	 * 根据转团游客和新团期，生成新订单
	 * @author gao
	 * @param travel 转团游客
	 * @param isLandGroup 新团期
	 * @param oldOrder 原订单
	 * @return
	 * @throws Exception 
	 */
	private void  newIslandOrder(IslandTraveler travel,ActivityIslandGroup isLandGroup,IslandOrder oldOrder,String newRoom,String newTicket) throws  Exception{
		//Map<String, String> map = new HashMap<String, String>();
		
		if(travel!=null && isLandGroup!=null && oldOrder!=null){
			// 获取产品
			ActivityIsland activity = activityIslandService.getByUuid(isLandGroup.getActivityIslandUuid());
			
			
			// 创建新订单实体
			IslandOrder newOrder = new IslandOrder();
			// 原订单数据导入新订单
//			BeanUtil.copySimpleProperties(newOrder, oldOrder);
			// 增加新订单数据
			newOrder.setUuid(UuidUtils.generUuid());
			newOrder.setActivityIslandUuid(activity.getUuid());
			newOrder.setActivityIslandGroupUuid(isLandGroup.getUuid());
			newOrder.setOrderNum(com.trekiz.admin.common.utils.DateUtils.date2String(new Date(), "yyyyMMddHHmmss")); // 订单单号
			newOrder.setOrderStatus(IslandOrder.ORDER_STATUS_TO_CONFIRM);
			newOrder.setOrderCompany(oldOrder.getOrderCompany());// 预定单位（渠道）
			newOrder.setOrderCompanyName(oldOrder.getOrderCompanyName());
			newOrder.setOrderSalerId(UserUtils.getUser().getId().intValue()); // 预定人ID
			newOrder.setOrderPersonName(UserUtils.getUser().getName());
			newOrder.setOrderPersonPhoneNum(UserUtils.getUser().getPhone());
			newOrder.setOrderTime(new Date());
			newOrder.setOrderPersonNum(1); // 预定人数
			newOrder.setForecaseReportRoomNum(StringUtils.isNotBlank(newRoom)?Integer.valueOf(newRoom):0); // 预报名间数
			newOrder.setForecaseReportTicketNum(StringUtils.isNotBlank(newTicket)?Integer.valueOf(newTicket):0); // 预报名票数
			newOrder.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
			newOrder.setCreateDate(new Date());
			newOrder.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
			newOrder.setUpdateDate(new Date());
			newOrder.setDelFlag(IslandOrder.DEL_FLAG_NORMAL);
			newOrder.setCostMoney(UuidUtils.generUuid()); //订单成本金额
			newOrder.setLockStatus(IslandOrder.LOCK_STATUS_NORMAL); // 订单锁定状态：0:正常  1：锁定(订单锁定状态不允许操作订单)
			newOrder.setTotalMoney(UuidUtils.generUuid());//订单总价UUID
			newOrder.setOriginalTotalMoney(UuidUtils.generUuid());//原始应收价 一次生成永不改变
			newOrder.setIsAfterSupplement(false);//是否是补单产品，0：否，1：是
			newOrder.setPaymentType(Context.PAYMENT_TYPE_JS);//结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4
			// 保存新订单
			islandOrderService.save(newOrder);
			
			// 订单联系人
			List<OrderContacts> oldContacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(Long.parseLong(oldOrder.getId().toString()), Context.ORDER_TYPE_ISLAND);
			List<OrderContacts> newContacts = new ArrayList<>();
			for (OrderContacts orderContacts : oldContacts) {
				OrderContacts tempContact = new OrderContacts();
				BeanUtils.copyProperties(orderContacts, tempContact);
				tempContact.setId(null);
				tempContact.setOrderId(Long.parseLong(newOrder.getId().toString()));
				newContacts.add(tempContact);
			}
			orderContactsService.batchSave(newContacts);
			
           // 游客实体
			IslandTraveler newTraveler = new IslandTraveler();
		//	BeanUtil.copySimpleProperties(newTraveler, travel, false);
			// 导入原数据
			newTraveler.setName(travel.getName());
			newTraveler.setNameSpell(travel.getNameSpell());
			newTraveler.setSpaceLevel(travel.getSpaceLevel());
			newTraveler.setPersonType(travel.getPersonType());
			newTraveler.setSex(travel.getSex());
			newTraveler.setNationality(travel.getNationality());
			newTraveler.setBirthDay(travel.getBirthDay());
			newTraveler.setTelephone(travel.getTelephone());
			newTraveler.setRemark(travel.getRemark());
			newTraveler.setSrcPrice(travel.getSrcPrice());
			newTraveler.setSrcPriceCurrency(travel.getSrcPriceCurrency());
			newTraveler.setIslandTravelervisaList(travel.getIslandTravelervisaList());
			newTraveler.setIslandTravelerFilesList(travel.getIslandTravelerFilesList());
			newTraveler.setIslandMoneyAmountList(travel.getIslandMoneyAmountList());
			newTraveler.setIslandTravelerPapersTypeList(travel.getIslandTravelerPapersTypeList());
			
			// 导入新游客数据
			newTraveler.setUuid(UuidUtils.generUuid());
			newTraveler.setOrderUuid(newOrder.getUuid());
			newTraveler.setOriginalPayPriceSerialNum(UuidUtils.generUuid());//"游客原始应收价 一次生成 永不改变";
			newTraveler.setCostPriceSerialNum(UuidUtils.generUuid());//"游客成本价UUID";
			newTraveler.setPayPriceSerialNum(UuidUtils.generUuid());// "游客结算价UUID";
			newTraveler.setRebatesMoneySerialNum(UuidUtils.generUuid());//"游客返佣UUID";
			newTraveler.setJkSerialNum(UuidUtils.generUuid()); // 游客借款UUID"
			newTraveler.setStatus(Context.TRAVELER_DELFLAG_NORMAL.toString());// 游客状态正常
			
			newTraveler.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
			newTraveler.setCreateDate(new Date());
			newTraveler.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
			newTraveler.setUpdateDate(new Date());
			newTraveler.setDelFlag(IslandTraveler.DEL_FLAG_NORMAL);
			
			//原海岛游游客签证信息查询条件
			IslandTravelervisaQuery islandTravelervisaQuery = new IslandTravelervisaQuery();	
			islandTravelervisaQuery.setIslandTravelerUuid(travel.getUuid());
			List<IslandTravelervisa> visaList = islandTravelervisaService.find(islandTravelervisaQuery);
			//List<IslandTravelervisa> newvisaList = new ArrayList<IslandTravelervisa>(); // 新签证list
			if(visaList!=null && !visaList.isEmpty()){
				Iterator<IslandTravelervisa> iter = visaList.iterator();
				while(iter.hasNext()){
					IslandTravelervisa visa = iter.next();
					IslandTravelervisa newvisa = new IslandTravelervisa();
					//BeanUtil.copySimpleProperties(newvisa, visa, false);
					// 导入原数据
					newvisa.setCountry(visa.getCountry());
					newvisa.setVisaTypeId(visa.getVisaTypeId());
					// 更新数据
					newvisa.setUuid(UuidUtils.generUuid());
					newvisa.setIslandOrderUuid(newOrder.getUuid());
					newvisa.setIslandTravelerUuid(newTraveler.getUuid());
					newvisa.setCreateBy(UserUtils.getUser().getId());
					newvisa.setCreateDate(new Date());
					newvisa.setUpdateBy(UserUtils.getUser().getId());
					newvisa.setUpdateDate(new Date());
					// 保存新签证
					islandTravelervisaService.save(newvisa);
				}
			}
			//原海岛游游客证件类型查询条件
			IslandTravelerPapersTypeQuery islandTravelerPapersTypeQuery = new  IslandTravelerPapersTypeQuery();
			islandTravelerPapersTypeQuery.setIslandTravelerUuid(travel.getUuid());
			List<IslandTravelerPapersType> paperList = islandTravelerPapersTypeService.find(islandTravelerPapersTypeQuery);
			if(paperList!=null && !paperList.isEmpty()){
				Iterator<IslandTravelerPapersType> iter = paperList.iterator();
				while(iter.hasNext()){
					IslandTravelerPapersType paper = iter.next();
					IslandTravelerPapersType newpapaer = new IslandTravelerPapersType();
					//BeanUtil.copySimpleProperties(newpapaer, paper, false);
					// 导入原数据
					newpapaer.setPapersType(paper.getPapersType());
					newpapaer.setValidityDate(paper.getValidityDate());
					newpapaer.setIdCard(paper.getIdCard());
					newpapaer.setIssueDate(paper.getIssueDate());
					newpapaer.setIssuePlace(paper.getIssuePlace());
					// 更新数据
					newpapaer.setUuid(UuidUtils.generUuid());
					newpapaer.setOrderUuid(newOrder.getUuid());
					newpapaer.setIslandTravelerUuid(newTraveler.getUuid());
					newpapaer.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
					newpapaer.setCreateDate(new Date());
					newpapaer.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
					newpapaer.setUpdateDate(new Date());
					islandTravelerPapersTypeService.save(newpapaer);
				}
			}
			//原附件信息查询条件
			HotelAnnex hotelAnnex = new HotelAnnex();
			hotelAnnex.setMainUuid(travel.getUuid());
			List<HotelAnnex> annexList = hotelAnnexService.find(hotelAnnex);
			if(annexList!=null && !annexList.isEmpty()){
				Iterator<HotelAnnex> iter = annexList.iterator();
				while(iter.hasNext()){
					HotelAnnex annex = iter.next();
					HotelAnnex newannex = new HotelAnnex();
					BeanUtil.copySimpleProperties(newannex, annex, false);
					// 更新数据
					newannex.setId(null);
					newannex.setUuid(UuidUtils.generUuid());
					newannex.setMainUuid(newTraveler.getUuid());
					newannex.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
					newannex.setCreateDate(new Date());
					newannex.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
					newannex.setUpdateDate(new Date());
					hotelAnnexService.save(newannex);
				}
			}
			// 获取新团期价格
			ActivityIslandGroupPriceQuery groupPriceQuery = new ActivityIslandGroupPriceQuery();
			groupPriceQuery.setActivityIslandUuid(activity.getUuid());
			groupPriceQuery.setActivityIslandGroupUuid(isLandGroup.getUuid());
			List<ActivityIslandGroupPrice> isgroupPriceList = activityIslandGroupPriceService.find(groupPriceQuery);
			if(isgroupPriceList!=null && !isgroupPriceList.isEmpty()){
				Iterator<ActivityIslandGroupPrice> iter = isgroupPriceList.iterator();
				while(iter.hasNext()){
					ActivityIslandGroupPrice gprice = iter.next();
					// 写入订单价格
					IslandOrderPrice orderPrice = new IslandOrderPrice();
					orderPrice.setUuid(UuidUtils.generUuid());
					orderPrice.setOrderUuid(newOrder.getUuid());
					orderPrice.setPriceType(IslandOrderPrice.PRICE_TYPE_GROUP); // 价格类型：1 团期价格类型
					orderPrice.setActivityIslandGroupPriceUuid(gprice.getUuid());// 团期UUid
					orderPrice.setCurrencyId(gprice.getCurrencyId()); // 币种
					orderPrice.setPrice(gprice.getPrice()); // 价格
					// 判断转团游客分类
					if(gprice.getType().equals(newTraveler.getPersonType())){
						// 获取游客定价
						newTraveler.setSrcPrice(gprice.getPrice()); // 游客单价（发布产品时的定价）
						newTraveler.setSrcPriceCurrency(gprice.getCurrencyId());// "单价币种";
						islandTravelerService.save(newTraveler);// 保存游客实体
						
						orderPrice.setNum(1); // 分类人数
						// 写入订单成本金额
						IslandMoneyAmount amount = new IslandMoneyAmount();
						amount.setUuid(UuidUtils.generUuid());
						amount.setCurrencyId(gprice.getCurrencyId());
						// 通过currencyID 获取汇率
						Currency currency = currencyService.findCurrency(Long.valueOf(gprice.getCurrencyId()));
						amount.setExchangerate(currency.getCurrencyExchangerate().doubleValue());
						amount.setAmount(gprice.getPrice());// 游客应付金额
						amount.setBusinessType(Context.MONEY_BUSINESSTYPE_ORDER); // 业务类型(订单)
						amount.setMoneyType(Context.MONEY_TYPE_CBJ); // 付款方式（成本）
						amount.setBusinessUuid(newOrder.getUuid()); // 订单uuid
						amount.setSerialNum(newOrder.getCostMoney());
						amount.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
						amount.setCreateDate(new Date());
						amount.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
						amount.setUpdateDate(new Date());
						amount.setDelFlag(Context.DEL_FLAG_NORMAL);
						// 保存订单原始应收价 一次生成永不改变
						IslandMoneyAmount amountStatic = new IslandMoneyAmount();
						BeanUtil.copySimpleProperties(amountStatic, amount, false);
						amountStatic.setUuid(UuidUtils.generUuid());
						amountStatic.setMoneyType(Context.MONEY_TYPE_YSYSH); // 原始应收
						amountStatic.setSerialNum(newOrder.getOriginalTotalMoney());
						amountStatic.setCreateDate(new Date());
						amountStatic.setUpdateDate(new Date());
						// 保存订单订单总价（应收）
						IslandMoneyAmount amountSum = new IslandMoneyAmount();
						BeanUtil.copySimpleProperties(amountSum, amount, false);
						amountSum.setUuid(UuidUtils.generUuid());
						amountSum.setMoneyType(Context.MONEY_TYPE_YSH); // 订单总价(应收)
						amountSum.setSerialNum(newOrder.getTotalMoney());
						amountSum.setCreateDate(new Date());
						amountSum.setUpdateDate(new Date());
						
						// 写入成人成本金额
						IslandMoneyAmount travelAmount = new IslandMoneyAmount();
						BeanUtil.copySimpleProperties(travelAmount, amount, false);
						travelAmount.setUuid(UuidUtils.generUuid());
						travelAmount.setBusinessUuid(newTraveler.getUuid()); // 游客uuid
						travelAmount.setBusinessType(Context.MONEY_BUSINESSTYPE_TRAVELER); // 业务类型(游客)
						travelAmount.setMoneyType(Context.MONEY_TYPE_CBJ); // 付款方式 成本
						travelAmount.setSerialNum(newTraveler.getCostPriceSerialNum());//"游客成本价UUID";
						travelAmount.setCreateDate(new Date());
						travelAmount.setUpdateDate(new Date());
						
						// 保存成人原始应收价 一次生成永不改变
						IslandMoneyAmount travelAmountStatic = new IslandMoneyAmount();
						BeanUtil.copySimpleProperties(travelAmountStatic, amount, false);
						travelAmountStatic.setUuid(UuidUtils.generUuid());
						travelAmountStatic.setBusinessUuid(newTraveler.getUuid()); // 游客uuid
						travelAmountStatic.setBusinessType(Context.MONEY_BUSINESSTYPE_TRAVELER); // 业务类型(游客)
						travelAmountStatic.setMoneyType(Context.MONEY_TYPE_YSJSJ);  //付款方式类型：游客原始结算价 
						travelAmountStatic.setSerialNum(newTraveler.getOriginalPayPriceSerialNum());//"游客原始应收价 一次生成 永不改变";
						travelAmountStatic.setCreateDate(new Date());
						travelAmountStatic.setUpdateDate(new Date());
						
						// 保存成人订单总价
						IslandMoneyAmount travelAmountSum = new IslandMoneyAmount();
						BeanUtil.copySimpleProperties(travelAmountSum, amount, false);
						travelAmountSum.setUuid(UuidUtils.generUuid());
						travelAmountSum.setBusinessUuid(newTraveler.getUuid()); // 游客uuid
						travelAmountSum.setBusinessType(Context.MONEY_BUSINESSTYPE_TRAVELER); // 业务类型(游客)
						travelAmountSum.setMoneyType(Context.MONEY_TYPE_YSH); // 付款方式类型（应收）
						travelAmountSum.setSerialNum(newTraveler.getPayPriceSerialNum());//"游客结算价UUID";
						travelAmountSum.setCreateDate(new Date());
						travelAmountSum.setUpdateDate(new Date());
						
						islandMoneyAmountService.save(travelAmount); // 保存游客成本
						islandMoneyAmountService.save(travelAmountStatic); // 保存游客原始应收价
						islandMoneyAmountService.save(travelAmountSum); // 保存游客应收

						islandMoneyAmountService.save(amount); // 保存订单成本
						islandMoneyAmountService.save(amountStatic); // 保存订单原始应收价
						islandMoneyAmountService.save(amountSum); // 保存订单应收
					}
					orderPrice.setCreateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
					orderPrice.setCreateDate(new Date());
					orderPrice.setUpdateBy(Integer.valueOf(UserUtils.getUser().getId().toString()));
					orderPrice.setUpdateDate(new Date());
					islandOrderPriceService.save(orderPrice);
				}
				//  注：2 返佣类型；3 优惠类型; 4 其他类型;5退款"; 这四种价格转团后不处理，放在转款中处理
			}
		}
	}
	
	/**
	 * 获取申请细节
	 * @return
	 */
	private List<Detail> getDetailList(IslandTransFerGroup bean){
		List<Detail> detailList = new ArrayList<Detail>();

		Map<String, String> map = bean.getReviewDetailMap();
		for (Entry<String, String> entry : map.entrySet()) {
			detailList.add(new Detail(entry.getKey(), entry.getValue()));
		}
		
		return detailList;
	}
	
	/**
	 * 创建申请实体类TransFerGroup
	 * @param traveler		游客实体
	 * @param order			原订单实体
	 * @param index			数组序号
	 * @param remarks		转团说明数组
	 * @param groupCode	新团期号
	 * @return
	 */
	private  IslandTransFerGroup getIslandTransFerGroup(IslandTraveler traveler,IslandOrder order,int index,String[] remarks,String groupCode){
		IslandTransFerGroup bean = new IslandTransFerGroup();
		
		// 游客ID
		if(traveler.getId()!=null){
			bean.setTravelerId(traveler.getId().toString());
		}
		// 游客姓名
		if(StringUtils.isNotBlank(traveler.getName())){
			bean.setTravelerName(traveler.getName());
		}	
		// 游客类型 （1-成人 2-儿童 3-特殊人群）
		if(StringUtils.isNotBlank(traveler.getPersonType())){
			bean.setTravelerType(traveler.getPersonType());
		}	
		// 游客舱位等级
		if(StringUtils.isNotBlank(traveler.getSpaceLevel())){
			bean.setTravelerLevel(traveler.getSpaceLevel());
		}
		//  游客转团申请时间
		bean.setApplyDate(new Date());
		// 游客签证国家和签证类型
		if(StringUtils.isNotBlank(traveler.getUuid()) && StringUtils.isNotBlank(order.getUuid())){
			StringBuffer buffer = new StringBuffer();
			IslandTravelervisaQuery query = new IslandTravelervisaQuery();
			query.setIslandTravelerUuid(traveler.getUuid());
			query.setIslandOrderUuid(order.getUuid());
			List<IslandTravelervisa> list = islandTravelervisaService.find(query);
			Iterator<IslandTravelervisa> iter = list.iterator();
			int listIndex = 0;
			while(iter.hasNext()){
				IslandTravelervisa visa = iter.next();
				SysGeography geo = sysGeographyDao.getByUuid(visa.getCountry());
				String citystr = new String();
				if(geo!=null){
					citystr = geo.getNameCn();
				}else{
					citystr = "待补充";
				}
				buffer.append(citystr+"/");
				if(visa.getVisaTypeId()!=null){
					buffer.append(DictUtils.getDictById(Long.valueOf(visa.getVisaTypeId()), "new_visa_type").getLabel()); // 获取签证类型
				}
				if(listIndex<=(list.size()-1)){
					buffer.append(",");
				}
			}
			bean.setVisaCountryType(buffer.toString());
		}	
		// 游客证件类型/证件号/有效期
		if(StringUtils.isNotBlank(traveler.getUuid()) && StringUtils.isNotBlank(order.getUuid())){
			StringBuffer buffertr = new StringBuffer();
			IslandTravelerPapersTypeQuery query = new IslandTravelerPapersTypeQuery();
			query.setIslandTravelerUuid(traveler.getUuid());
			query.setOrderUuid(order.getUuid());
			List<IslandTravelerPapersType> list = islandTravelerPapersTypeService.find(query);
			Iterator<IslandTravelerPapersType> iter = list.iterator();
			int listIndex = 0;
			while(iter.hasNext()){
				IslandTravelerPapersType paper = iter.next();
				String label = new String();
				if(paper.getPapersType()==1){
					label = "身份证";
				}else if(paper.getPapersType()==2){
					label = "护照";
				}else if(paper.getPapersType()==3){
					label = "警官证";
				}else if(paper.getPapersType()==4){
					label = "军官证";
				}else if(paper.getPapersType()==5){
					label = "其他";
				}
				
				if(StringUtils.isNotBlank(label)){
					buffertr.append(label);
				}else{
					buffertr.append("-");
				}
				buffertr.append("/");
				if(StringUtils.isNotBlank(paper.getIdCard())){
					buffertr.append(paper.getIdCard());
				}else{
					buffertr.append("-");
				}
				buffertr.append("/");
				if(paper.getValidityDate()!=null){
					buffertr.append(DateUtils.formatCustomDate(paper.getValidityDate(), "yyyy-MM-dd"));
				}else{
					buffertr.append("-");
				}
				if(listIndex<=(list.size()-1)){
					buffertr.append(",");
				}
			}
			bean.setPaperTypeCodeDate(buffertr.toString());
		}	
		//  备注（转团原因）
		if(StringUtils.isNotBlank(remarks[index])){
			bean.setRemark(remarks[index]);
		}	
		// 转入团期号
		if(StringUtils.isNotBlank(groupCode)){
			bean.setNewGroupCode(groupCode);
		}
		// 游客uuid
		if(StringUtils.isNotBlank(traveler.getUuid())){
			bean.setTravelerUuid(traveler.getUuid());
		}
		return bean;
	}
}
