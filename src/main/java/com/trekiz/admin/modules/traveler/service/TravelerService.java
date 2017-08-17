package com.trekiz.admin.modules.traveler.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.grouphandle.dao.GroupHandleDao;
import com.trekiz.admin.modules.grouphandle.dao.GroupHandleVisaDao;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandle;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandleVisa;
import com.trekiz.admin.modules.grouphandle.service.GroupHandleService;
import com.trekiz.admin.modules.grouphandle.service.GroupHandleVisaService;
import com.trekiz.admin.modules.log.service.LogOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.CostchangeDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.CostchangeService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderStockService;
import com.trekiz.admin.modules.order.util.OrderUtil;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.entity.TravelerFile;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.entity.TravelerVisaNew;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.repository.TravelerFileDao;
import com.trekiz.admin.modules.traveler.repository.TravelerVisaDao;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.repository.VisaDao;


/**
 * 游客信息Service
 * 
 * @author xiaoyang.tao
 */
@Service
@Transactional(readOnly = true)
public class TravelerService extends BaseService{

	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private TravelerFileDao travelerFileDao;
	@Autowired
	private TravelerVisaDao travelerVisaDao;
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	private CostchangeDao costchangeDao;
	@Autowired
	private VisaDao visaDao;
	@Autowired
	private OrderpayDao orderPayDao;
	@Autowired
    private CurrencyDao currencyDao;
	@Autowired
    private CurrencyService currencyService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
    private TravelerVisaService travelerVisaService;
	@Autowired
    private TravelerVisaNewService travelerVisaNewService;
    @Autowired
    private CostchangeService costchangeService;
    @Autowired
    private AreaService areaService;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
    @Autowired
    private OrderStockService orderStockService;     
	@Autowired
	private GroupHandleDao groupHandleDao;
	@Autowired
	private GroupHandleVisaDao groupHandleVisaDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
    @Autowired
	private TravelActivityDao travelActivityDao;
	@Autowired
	private AgentinfoDao agentinfoDao;
	@Autowired
	private GroupHandleService groupHandleService;
	@Autowired
	private GroupHandleVisaService groupHandleVisaService;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private ProductOrderCommonDao orderCommonDao;
	@Autowired
	private LogOrderService logOrderService;

	/**
     * 
     * @param jsonObject
     * @param traveler
     * @param orderType
     * @param costArr
     * @param payPriceArr
     * @param visasArr
     * @param costPriceArr 成本价
     * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
	public Traveler saveTraveler(OrderCommonService orderService, JSONObject jsonObject, Traveler traveler, 
			int orderType, JSONArray costArr, JSONArray payPriceArr, JSONArray visasArr,JSONArray costPriceArr, Map<String, String> paraMap) {
		Traveler srcTraveler = new Traveler();
		// 26需求
		Map<String, Object> map = new HashMap<>();
		Traveler preTraveler = new Traveler();
		List<TravelerFile> preTravelerFileList = new ArrayList<>();

		boolean isAdd = false;
		if (traveler.getId() == null) {
			isAdd = true;
		} else {
			srcTraveler = travelerDao.getTravleById(traveler.getId());
			List<TravelerFile> srcList = travelerFileDao.findFileListByPid(traveler.getId());
			BeanUtils.copyProperties(srcTraveler, preTraveler);
			preTravelerFileList.addAll(srcList);
		}

		ProductOrderCommon order = orderService.getProductorderById(traveler.getOrderId());
		String oldPayPriceString = moneyAmountService.getMoneyStr(srcTraveler.getPayPriceSerialNum());
		String prePayPriceString = moneyAmountService.getMoneyCurrenName(srcTraveler.getPayPriceSerialNum());
		if (oldPayPriceString.equals("¥ 0.00")) {
			oldPayPriceString = traveler.getSrcPriceCurrency().getCurrencyMark() + "0.00";
		}
		map.put("preInputClearPrice", prePayPriceString);
		//判断添加游客数会不会超过总人数
		String orderPersonelNum = paraMap.get("orderPersonelNum");
		boolean flag = isCanSave(isAdd, traveler, orderPersonelNum);
		//当游客可以添加或不是添加的时候保存
		if (flag || !isAdd) {
			// 保存游客
			traveler = travelerDao.save(traveler);
			// 获取游客Id
			Long travelerId = traveler.getId();
			// 添加日志
			addOpLog(isAdd, srcTraveler, traveler, costchangeService.findCostchangeByTravelerId(traveler.getId()), costArr);
			// 保存游客结算价,成本价和返佣费用
			String rebatesMoney = paraMap.get("rebatesMoney");
			String rebatesCurrencyId = paraMap.get("rebatesCurrencyId");
			savePayPrice(traveler, payPriceArr, orderType,costPriceArr,rebatesMoney,rebatesCurrencyId);
			// 记录结算价变动 for quauq
			String newPayPriceString = moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum());
			String afterPayPriceString = moneyAmountService.getMoneyCurrenName(traveler.getPayPriceSerialNum());
			List<TravelerVisa> list = travelerVisaDao.findVisaListByPid(travelerId);
//			TravelerVisa preTravelerVisa = new TravelerVisa();
//			if(list!=null && list.size()>0){
//				preTravelerVisa = list.get(0);
//			}
			map.put("afterInputClearPrice", afterPayPriceString);
			map.put("afterRebatesMoney", rebatesMoney);
			map.put("afterRebatesCurrencyId", rebatesCurrencyId);
			map.put("preTravelerVisa", list);
			if (Context.PRICE_TYPE_QUJ == order.getPriceType()) {
				// 记录结算价变动日志
				StringBuffer logContent = new StringBuffer();
				logContent.append("QUAUQ订单");
				logContent.append("###");
				if (Context.DEL_FLAG_NORMAL.equals(order.getDelFlag())) {					
					logContent.append("订单修改");
				} else {
					logContent.append("预定");
				}
				logContent.append("###");
				logContent.append(traveler.getName() + "游客结算价从" + oldPayPriceString + "修改为" + newPayPriceString);
				this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, logContent.toString(), Context.log_state_add, traveler.getOrderType(), traveler.getOrderId());
			}
			// 保存游客签证信息
			saveVisa(travelerId, visasArr);
			// 保存费用信息
			saveCost(costArr, travelerId);
			List<TravelerFile>  travelerFileList = OrderUtil.getTravelerFileList(jsonObject, travelerId);
//			travelerFileList.addAll(OrderUtil.getTravelerFileListNew(jsonObject, travelerId));
			saveTravelerFile(travelerFileList);
			if(isAdd){ // 添加游客
				if(!"newOrder".equals(paraMap.get("newOrderFlag"))){// 新生成的订单不做记录
					logOrderService.saveSingleOrderLog("", traveler.getOrderId(), "添加了新游客："+traveler.getName(), 1, UuidUtils.generUuid());
				}
			}else{ // 修改游客
				// 26需求
				if(!"newOrder".equals(paraMap.get("newOrderFlag"))){// 新生成的订单时修改也不做记录
					logOrderService.saveLogSingleGroupOrder4Traveler(preTraveler, visasArr, preTravelerFileList, map);
				}
			}
			// 保存订单成本价、结算价（仅在游客修改时，避免用户修改了游客价格而没保存订单产生错误）
			String orderModifyFlag = paraMap.get("orderModifyFlag");
			if (StringUtils.isNoneBlank(orderModifyFlag) && Boolean.parseBoolean(orderModifyFlag)) {
				saveOrderMoney(order, paraMap);
				changeFreePositionAndSoldPosition(order, paraMap);
			}
		} else {
			traveler = null;
		}
		return traveler;
	}
    
    /**
     * C147&C109优佳 (订单修改保存游客)
     * @author yang.jiang 2016-2-3 12:01:13
     * @param jsonObject
     * @param traveler
     * @param orderType
     * @param costArr
     * @param payPriceArr
     * @param visasArr
     * @param costPriceArr 成本价
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> saveTravelerMainInfo4YouJiaModify(OrderCommonService orderService, JSONObject jsonObject, Traveler traveler, 
			int orderType, JSONArray costArr, JSONArray payPriceArr, JSONArray visasArr,JSONArray costPriceArr, Map<String, String> paraMap) throws IllegalAccessException, InvocationTargetException{
    	Map<String, Object> returnMap = new HashMap<>();
    	Traveler srcTraveler = new Traveler();
		Traveler targetTraveler = new Traveler();		
		boolean isAdd = false;
		
		// 26需求
		List<Map<String,Object>> preTravelerFileList = new ArrayList<Map<String,Object>>();
		Traveler preTraveler = new Traveler();
		
		if (traveler.getId() == null) {
			isAdd = true;
			BeanUtils.copyProperties(traveler, targetTraveler);
		} else {
			srcTraveler = travelerDao.getTravleById(traveler.getId());
			BeanUtils.copyProperties(srcTraveler, preTraveler);
			// 由于bean工具拷贝功能的限制，traveler中确实要置为空的属性不能正常拷贝，故先把src中涉及到要修改的属性现行置为空，在后面操作中由traveler的值填充。
			remakeSrcTraveler(srcTraveler);
			traveler.setDelFlag(srcTraveler.getDelFlag());
			BeanUtils.copyProperties(srcTraveler, targetTraveler);
			BeanUtils.copyProperties(traveler, targetTraveler);
			
			preTravelerFileList = travelerFileDao.findFilesByPid(traveler.getId());
		}
		ProductOrderCommon order = orderService.getProductorderById(targetTraveler.getOrderId());
		//判断添加游客数会不会超过总人数
		String orderPersonelNum = paraMap.get("orderPersonelNum");
		boolean flag = isCanSave(isAdd, targetTraveler, orderPersonelNum);
		//当游客可以添加或不是添加的时候保存		
		if (flag || !isAdd) {
			// 保存游客
			targetTraveler = travelerDao.save(targetTraveler);
			// 获取游客Id
			Long travelerId = targetTraveler.getId();
			List<Map<String, Object>> list = groupHandleVisaDao.findListByOrderIdAndTravelerId(Integer.parseInt(targetTraveler.getOrderId().toString()), Integer.parseInt(travelerId.toString()));
			// 添加日志
			addOpLog(isAdd, srcTraveler, targetTraveler, costchangeService.findCostchangeByTravelerId(targetTraveler.getId()), costArr);
			// 保存游客结算价,成本价和返佣费用
			String rebatesMoney = paraMap.get("rebatesMoney");
			String rebatesCurrencyId = paraMap.get("rebatesCurrencyId");
			savePayPrice(targetTraveler, payPriceArr, orderType,costPriceArr,rebatesMoney,rebatesCurrencyId);
			// 保存游客签证信息
			returnMap = saveVisa2GroupHandle4Modify(targetTraveler, visasArr, paraMap);
			// 保存费用信息
			saveCost(costArr, travelerId);
//			List<TravelerFile>  travelerFileList = OrderUtil.getTravelerFileList(jsonObject, travelerId);
//			saveTravelerFile(travelerFileList);
			
			List<TravelerFile> travelerFileList = OrderUtil.getTravelerFileList4YoujiaMod(jsonObject, travelerId);  // 获取待添加文件
			returnMap.putAll(saveTravelerFile4YouJiaMod(jsonObject, travelerFileList));  // 删除、保存
			
			if(isAdd){ // 添加游客
				if(!"newOrder".equals(paraMap.get("newOrderFlag"))){// 新生成的订单不做记录
					logOrderService.saveSingleOrderLog("", traveler.getOrderId(), "添加了新游客："+traveler.getName(), 1, UuidUtils.generUuid());
				}
			}else{ // 修改游客
				// 26需求
				if(!"newOrder".equals(paraMap.get("newOrderFlag"))){// 新生成的订单时修改也不做记录
					logOrderService.saveYouJiaLogOrder4Traveler(preTraveler, visasArr, preTravelerFileList, list);
				}
			}
			
			// 保存订单成本价、结算价（仅在游客修改时，避免用户修改了游客价格而没保存订单产生错误）
			String orderModifyFlag = paraMap.get("orderModifyFlag");
			if (StringUtils.isNoneBlank(orderModifyFlag) && Boolean.parseBoolean(orderModifyFlag)) {
				saveOrderMoney(order, paraMap);
				changeFreePositionAndSoldPosition(order, paraMap);
			}
		} else {
			targetTraveler = null;
		}
		returnMap.put("traveler", targetTraveler);
		return returnMap;
	}
    
    /**
     * 订单修改时，游客基本信息项的值是待赋状态，应置为空。其余项的值使用src的不变。
     * @param targetTraveler
     * @param srcTraveler
     */
    private void remakeSrcTraveler(Traveler srcTraveler){
    	srcTraveler.setOrderType(null);  // 订单类型
    	srcTraveler.setOrderId(null);  // 订单id
    	srcTraveler.setIntermodalId(null);  // 产品联运信息表主键 关联intermodal_strategy表
    	srcTraveler.setIntermodalType(null);  // 是否需要联运 0：不需要，1：需要
    	srcTraveler.setName(null);  //姓名
    	srcTraveler.setNameSpell(null);  // 姓名英文拼音
    	srcTraveler.setSex(null);  // 性别
    	srcTraveler.setBirthDay(null);  // 出生日期
    	srcTraveler.setPositionCn(null);  // 职位中文
    	srcTraveler.setPositionEn(null);  // 职位英文
    	srcTraveler.setPassportPlace(null);  // 护照签发地
    	srcTraveler.setNationality(null);  // 国籍
    	srcTraveler.setTelephone(null);  // 电话
    	srcTraveler.setPassportCode(null);  // 护照号
    	srcTraveler.setIssuePlace(null);  // 发证日期
    	srcTraveler.setPassportValidity(null);  // 护照有效期
    	srcTraveler.setPassportType(null);  // 护照类型（因公护照、因私护照）
    	srcTraveler.setIdCard(null);  // 身份证号码
    	srcTraveler.setRemark(null);  // 备注
    	srcTraveler.setHotelDemand(null);  // 房屋类型 1-单人间 2-双人间
    	srcTraveler.setSingleDiff(null);  // 单房差
    	srcTraveler.setSingleDiffCurrency(null);  // 单房差币种
    	srcTraveler.setSingleDiffNight(null);  // 单房差几晚
    	srcTraveler.setSrcPriceCurrency(null);  // 同行价币种
    	srcTraveler.setSrcPrice(null);  // 同行价
    	srcTraveler.setFixedDiscountPrice(null);  // 手动输入的额内优惠金额
    	srcTraveler.setOrgDiscountPrice(null);  // 原始优惠金额（报名时团期下对应游客类型的优惠额度）
    	srcTraveler.setPersonType(null);  // 游客类型
    }
    
	/**
	 * C147&C109 保存游客
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveTraveler4YouJia(JSONObject jsonObject,Traveler traveler, int orderType, JSONArray costArr,
			JSONArray payPriceArr, JSONArray visasArr,JSONArray costPriceArr, Map<String, String> params) throws Exception {
		Integer returnData = null;
		Traveler targetTraveler = new Traveler();
		boolean isAdd = false;
		if (traveler.getId() == null) {
			isAdd = true;
		} else {
			Traveler srcTraveler = travelerDao.getTravleById(traveler.getId());
			traveler.setDelFlag(srcTraveler.getDelFlag());
			BeanUtils.copyProperties(srcTraveler, targetTraveler);
		}
		//判断添加游客数会不会超过总人数
		boolean flag = isCanSave(isAdd, traveler, params.get("orderPersonelNum"));
		//当游客可以添加或不是添加的时候保存
		if (flag || !isAdd) {
			//保存游客
			traveler = travelerDao.save(traveler);
			//获取游客Id
			Long travelerId = traveler.getId();
			//添加日志
			addOpLog(isAdd, targetTraveler, traveler, costchangeService.findCostchangeByTravelerId(traveler.getId()), costArr);
			// 处理游客优惠信息
			handleDiscountPrice(traveler, params);
			//保存游客结算价,成本价和返佣费用
			savePayPrice(traveler, payPriceArr, orderType,costPriceArr,params.get("rebatesMoney"),params.get("rebatesCurrencyId"));
			// 保存签证信息到团控列表
			returnData = saveVisa2GroupHandle(traveler, visasArr, params.get("productId"), params.get("productGroupId"),
					params.get("activityKind"), params.get("agentId"), params.get("groupHandleId"));
			//保存费用信息
			saveCost(costArr, travelerId);
			List<TravelerFile>  travelerFileList = OrderUtil.getTravelerFileList(jsonObject, travelerId);
			clearTravelerFileByTravelerId(travelerId);
			saveTravelerFile4YouJia(travelerFileList);
		} else {
			// 处理游客优惠信息
			handleDiscountPrice(traveler, params);
			returnData = saveVisa2GroupHandle(traveler, visasArr, params.get("productId"), params.get("productGroupId"),
					params.get("activityKind"), params.get("agentId"), params.get("groupHandleId"));
			traveler = null;
		}
		return returnData;
	}
	
	/**
	 * 批量保存游客附件信息，同时删除之前同一类型附件
	 * mod by yang.jiang 2016-2-20 21:11:22
	 * @param travelerFileList 附件信息对象组装集合
	 */
	private Map<String, Object> saveTravelerFile4YouJiaMod(net.sf.json.JSONObject jsonObject, List<TravelerFile> travelerFileList) {
		Map<String, Object> resultMap = new HashMap<>();
		//删除
		String  tobeDelFiles = null;
		if (jsonObject.containsKey("passportdocID")) {
			//要待删除的文件
			tobeDelFiles = jsonObject.getString("tobeDelFiles");
			if (StringUtils.isNotBlank(tobeDelFiles)) {				
				if (tobeDelFiles.startsWith(",")) {
					tobeDelFiles = tobeDelFiles.substring(1, tobeDelFiles.length());
				}
				String[] fileIds = tobeDelFiles.split(",");
				for(int i = 0; i < fileIds.length; i++) {
					travelerFileDao.delTravelerFileBySrcDocId(Long.parseLong(fileIds[i]));
				}
			}
		}
		//获取游客下所有正常状态签证文件
		List<TravelerFile> allFiles = new ArrayList<>();
		if (StringUtils.isNotBlank(jsonObject.getString("travelerId"))) {
			allFiles = travelerFileDao.findBySrcTravelerIdAndDelFlag(Long.parseLong(jsonObject.getString("travelerId")), "0");
		}
		//剔除已存在的
		List<TravelerFile> tempDel = new ArrayList<>(); 
		for (TravelerFile travelerFile : travelerFileList) {
			for (TravelerFile allFile : allFiles) {
				if (allFile.getSrcDocId().compareTo(travelerFile.getSrcDocId()) == 0) {
					tempDel.add(travelerFile);
				}
			}
		}
		travelerFileList.removeAll(tempDel);
		//保存
		List<TravelerFile> newVisafiles = new ArrayList<>();
		if (travelerFileList != null && travelerFileList.size() > 0) {
			// 在添加之前把存在的文件关系删除
			for (TravelerFile travelerFile : travelerFileList) {
				travelerFile.setTraveler(travelerDao.findOne(travelerFile.getSrcTravelerId()));
				travelerFile.setDocInfo(docInfoService.getDocInfo(travelerFile.getSrcDocId()));
				newVisafiles.add(travelerFileDao.save(travelerFile));
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 保存游客文件前，先将原来的文件进行删除
     */
	private void clearTravelerFileByTravelerId(Long travelerId) throws Exception {
		try {
			travelerFileDao.delTravelerFileByTravelerId(travelerId);
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("删除游客文件失败!");
		}
	}

	/**
	 * 处理游客优惠信息(范围内优惠，团期优惠信息)
     */
	private void handleDiscountPrice(Traveler traveler, Map<String, String> params) {
		String discountPrice = params.get("discountPrice");
		String activityDiscountPrice = params.get("activityDiscountPrice");

		// 保存游客额内优惠
		if(StringUtils.isNotBlank(discountPrice)) {
			traveler.setFixedDiscountPrice(new BigDecimal(discountPrice));
		}
		// 保存游客报名时的团期优惠
		if(StringUtils.isNotBlank(activityDiscountPrice)) {
			traveler.setOrgDiscountPrice(new BigDecimal(activityDiscountPrice));
		}

		travelerDao.save(traveler);
	}


	/**
     * 判断是否可以添加此游客（bugfree 5469）
     * @param isAdd
     * @return
     */
    private boolean isCanSave(boolean isAdd, Traveler traveler, String orderPersonelNum) {
    	boolean flag = true;
    	if (isAdd) {
    		Long orderId = traveler.getOrderId();
    		Integer orderType = traveler.getOrderType();
    		
    		List<Traveler> travelerList = findTravelerByOrderIdAndOrderType(orderId, orderType);
    		if (CollectionUtils.isNotEmpty(travelerList) && Integer.parseInt(orderPersonelNum) == travelerList.size()) {
    			flag = false;
    		}
    	}
    	return flag;
    }
	
	/**
	 * 游客日志
	 * @param isAdd 是否添加
	 * @param srcTraveler 原游客信息
	 * @param newTraveler 添加或修改后游客信息
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	private void addOpLog(boolean isAdd, Traveler srcTraveler, Traveler newTraveler, List<Costchange> srcCostChange, JSONArray jsonCost) {
		 //日志内容
		StringBuffer opLog = new StringBuffer("");
		
		if (isAdd) {
			opLog.append("在订单（id=" + newTraveler.getOrderId() + "）" + "下添加游客，id为：" + newTraveler.getId());
			this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
					opLog.toString(), Context.log_state_add, srcTraveler.getOrderType(), null);
		} else {
			opLog.append("在订单（id=" + newTraveler.getOrderId() + "）" + "下修改游客（id=" + newTraveler.getId()  + "）信息：");
			//姓名
			if (!srcTraveler.getName().equals(newTraveler.getName())) {
				opLog.append("游客姓名由" + srcTraveler.getName() + "改为" + newTraveler.getName() + "；");
			}
			//性别
			if (srcTraveler.getSex() != newTraveler.getSex()) {
				if (srcTraveler.getSex() == 1) {
					opLog.append("游客性别由男改为女；");
				} else {
					opLog.append("游客性别由女改为男；");
				}
				
			}
			//护照
			if (srcTraveler.getPassportCode() != null && newTraveler.getPassportCode() != null) {
				if (!srcTraveler.getPassportCode().equals(newTraveler.getPassportCode())) {
					opLog.append("游客护照号由" + srcTraveler.getPassportCode() + "改为" + newTraveler.getPassportCode() + "；");
				}
			}
			//发证日期
			if (srcTraveler.getIssuePlace() != null && newTraveler.getIssuePlace() != null) {
				if (srcTraveler.getIssuePlace().compareTo(newTraveler.getIssuePlace()) != 0) {
					opLog.append("游客发证日期由" + DateUtils.formatCustomDate(srcTraveler.getIssuePlace(), "yyyy-MM-dd") 
							+ "改为" + DateUtils.formatCustomDate(newTraveler.getIssuePlace(), "yyyy-MM-dd") + "；");
				}
			}
			//护照有效期
			if (srcTraveler.getPassportValidity() != null && newTraveler.getPassportValidity() != null) {
				if (srcTraveler.getPassportValidity().compareTo(newTraveler.getPassportValidity()) != 0) {
					opLog.append("游客护照有效期由" + DateUtils.formatCustomDate(srcTraveler.getPassportValidity(), "yyyy-MM-dd") 
							+ "改为" + DateUtils.formatCustomDate(newTraveler.getPassportValidity(), "yyyy-MM-dd") + "；");
				}
			}
			//住房要求
			if (srcTraveler.getHotelDemand() != null && srcTraveler.getHotelDemand() != newTraveler.getHotelDemand()) {
				if (srcTraveler.getHotelDemand() == 1) {
					opLog.append("住房要求从单人间改为双人间；");
				} else {
					opLog.append("住房要求从双人间改为单人间；");
				}
				
			}
			//单人房差
			if (srcTraveler.getSingleDiffNight() != newTraveler.getSingleDiffNight()) {
				opLog.append("游客单人房差由" + (srcTraveler.getSingleDiffNight() != null ? srcTraveler.getSingleDiffNight() : 0) 
						+ "晚改为" + (newTraveler.getSingleDiffNight() != null ? newTraveler.getSingleDiffNight() : 0) + "晚；");
			}
			//单人房差
			if (srcTraveler.getSingleDiffCurrency() != null && (srcTraveler.getSingleDiffCurrency().getId() != newTraveler.getSingleDiffCurrency().getId()
					|| srcTraveler.getSingleDiff().compareTo(newTraveler.getSingleDiff()) != 0)) {
				opLog.append("游客单房差小费由" + srcTraveler.getSingleDiffCurrency().getCurrencyMark() + srcTraveler.getSingleDiff() 
						+ "改为" + newTraveler.getSingleDiffCurrency().getCurrencyMark() + newTraveler.getSingleDiff() + "；");
			}
			
			
			if (CollectionUtils.isNotEmpty(srcCostChange)) {
				//如果提交其他费用为空，则表示数据库中其他费用被全部删除
				if (jsonCost == null || jsonCost.size() == 0) {
					for (Costchange costChange : srcCostChange) {
						opLog.append("删除其他费用" + costChange.getCostName() + "：" + costChange.getPriceCurrency().getCurrencyMark() + costChange.getCostSum() + "；");
					}
				} else {
					//如果提交其他费用不为空，则循环这些其他费用并和数据库其他费用对比：如果费用名称相同则表示修改或没改动，如果不同则表示是新增加其他费用
					if (jsonCost != null) {
						for(int i = 0; i < jsonCost.size(); i++) {
							JSONObject costJson = jsonCost.getJSONObject(i);
							if (!costJson.containsKey("name") || !costJson.containsKey("currency")) {
								continue;
							}
							//费用名称
					        String name = costJson.getString("name");
					        //费用币种
					        String curency = costJson.getString("currency");
					        //费用金额
					        String sum = costJson.getString("sum");
					        if(name != null && StringUtils.isNotBlank(name) 
					        		&& curency != null && StringUtils.isNotBlank(curency)
					        		&& sum != null && StringUtils.isNotBlank(sum)) {
					        	Iterator<Costchange> costIt = srcCostChange.iterator();
					        	boolean flag = true;//数据库中其他费用是否有和此提交其他费用相同一项
					        	while(costIt.hasNext()) {
					        		Costchange cost = costIt.next();
					        		if (name.equals(cost.getCostName())) {
					        			//如果其他费用名称和币种与金额都相同则表示没有改变
					        			if (curency.equals(cost.getPriceCurrency().getId().toString()) 
					        					&& StringNumFormat.getBigDecimalForTow(sum).compareTo(cost.getCostSum()) == 0) {
					        				costIt.remove();
					        			} 
					        			//如果其他费用名称相同但其余两项有不同地方则表示其他费用被修改
					        			else {
					        				opLog.append("修改其他费用" + name.toString() + "：金额由" + cost.getPriceCurrency().getCurrencyMark() 
					        						+ cost.getCostSum().toString() + "改为"  + currencyService.findCurrency(
					        						Long.parseLong(curency)).getCurrencyMark() + sum + "；");
					        				costIt.remove();
					        			}
					        			flag = false;
					        		}
					        	}
					        	//如果没有相同其他费用名称则表示是新增加其他费用项
					        	if (flag) {
					        		opLog.append("添加其他费用" + name.toString() + "：" + currencyService.findCurrency(Long.parseLong(curency)).getCurrencyMark() + sum + "；");
					        	}
					        }
						}
						//如果数据库中经过和提交其他费用对比后没有相同一项则表示被删除
						if (CollectionUtils.isNotEmpty(srcCostChange)) {
							for (Costchange costChange : srcCostChange) {
								opLog.append("删除其他费用" + costChange.getCostName() + "：" + costChange.getPriceCurrency().getCurrencyMark() + costChange.getCostSum() + "；");
							}
						}
					}
				}
			} else {
				//如果数据库中其他费用为空则表示提交其他费用都为新增
				if (CollectionUtils.isNotEmpty(jsonCost)) {
					for(int i = 0; i < jsonCost.size(); i++){
						JSONObject costJson = jsonCost.getJSONObject(i);
						if (!costJson.isEmpty() && costJson.get("name") != null 
								&& costJson.get("currency") != null && costJson.get("sum") != null) {							
							//费用名称
							String name = costJson.getString("name");
							//费用币种
							String curency = costJson.getString("currency");
							//费用金额
							String sum = costJson.getString("sum");
							if(name != null && StringUtils.isNotBlank(name) 
									&& curency != null && StringUtils.isNotBlank(curency)
									&& sum != null && StringUtils.isNotBlank(sum)){
								opLog.append("添加其他费用" + name.toString() + "：" + currencyService.findCurrency(Long.parseLong(curency)).getCurrencyMark() + sum + "；");
							}
						}
					}
				}
			}
			
			//如果最后一个字符是冒号则表示没有日志，如果不是则删除最后一个分号
			if (opLog.lastIndexOf("：") != opLog.length()-1) {
				this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
						opLog.substring(0, opLog.length()-1), Context.log_state_up, srcTraveler.getOrderType(), null);
			}
		}
	}
	
	/**
	 * 保存游客结算价及原始结算价
	 * @param travelerId 游客Id
	 * @param request
	 */
	private void savePayPrice(Traveler traveler, JSONArray payPriceObject,int orderType,JSONArray costPriceObject,String rebatesMoney,String rebatesCurrencyId){
		// 登录用户
		Long userId = UserUtils.getUser().getId();
		// 获取游客
		if (traveler == null) {
			return;
		}
		Long travelerId = traveler.getId();
		// 游客相关金额流水号
		String payPriceSerialNum = traveler.getPayPriceSerialNum();  // 游客结算价
		String originalPayPriceSerialNum = traveler.getOriginalPayPriceSerialNum();  // 游客原始结算价
		String rebatesMoneySerialNum = traveler.getRebatesMoneySerialNum();	//返佣费用序列号
		String costPriceSerialNum = traveler.getCostPriceSerialNum();  //成本价序列号		
		// 结算价、原始结算价
		if (StringUtils.isBlank(originalPayPriceSerialNum)) {
			originalPayPriceSerialNum = UuidUtils.generUuid();
		}
		if (StringUtils.isBlank(payPriceSerialNum)) {
			payPriceSerialNum = UuidUtils.generUuid();
		}
		List<MoneyAmount> moneyAmountList= Lists.newArrayList();
		List<MoneyAmount> oldMoneyAmountList= Lists.newArrayList();
		JSONObject payPrice = null;
		List<MoneyAmount> payPrices = moneyAmountService.findAmountBySerialNum(payPriceSerialNum);  // 获取原有的结算价
		// 如果原有结算价，删除.(由于每次结算价的变动不确定：或有同币种、或增币种、或减币种，区分进行增删改较为繁琐，此处直接全删除)
		if (CollectionUtils.isNotEmpty(payPrices)) {
			for (MoneyAmount oldPayPrice : payPrices) {
				moneyAmountService.deleteById(oldPayPrice.getId());
			}
		}
		List<MoneyAmount> orgPayPrices = moneyAmountService.findAmountBySerialNum(originalPayPriceSerialNum);  // 获取原有的原始结算价
		// TODO_DF 汇率要存对

		for(int i = 0; i < payPriceObject.size(); i++){
			payPrice = payPriceObject.getJSONObject(i);
			int currencyId = payPrice.getInt("currencyId");
			BigDecimal price = new BigDecimal(payPrice.getDouble("price"));
			// 保存结算价
			MoneyAmount payPriceAmount = new MoneyAmount(payPriceSerialNum, currencyId, price, travelerId, Context.MONEY_TYPE_JSJ, orderType, 2, userId);
			moneyAmountList.add(payPriceAmount);
			
			// 原始结算价。当原始结算价流水号不存在时，认为没有存过原始结算价，本次结算价就是原始结算价
			if (CollectionUtils.isEmpty(orgPayPrices)) {				
				MoneyAmount oldMoneyAmount = new MoneyAmount(originalPayPriceSerialNum, currencyId, price, travelerId, Context.MONEY_TYPE_YSJSJ, orderType, 2, userId);
				oldMoneyAmountList.add(oldMoneyAmount);
			}
		}
		
		//保存返佣费用 
		List<MoneyAmount> rebatesMoneyAmountList= Lists.newArrayList();
		if (StringUtils.isBlank(rebatesMoneySerialNum)) {
			rebatesMoneySerialNum = UuidUtils.generUuid();
		}
		List<MoneyAmount> rebatesMoneyAmounts = moneyAmountService.findAmountBySerialNum(rebatesMoneySerialNum);  // 获取原有的moneyAmount
		if(StringUtils.isNotBlank(rebatesMoney) && StringUtils.isNotBlank(rebatesCurrencyId)){
			int rebatesCurrency_Id = Integer.parseInt(rebatesCurrencyId);  // 返佣币种
			BigDecimal rebates_Money = new BigDecimal(rebatesMoney);  // 返佣金额
			// 若存在，则对应币种的money重新设置主要字段
			if (CollectionUtils.isNotEmpty(rebatesMoneyAmounts)) {
				MoneyAmount rebatesMoneyAmount = rebatesMoneyAmounts.get(0);
				rebatesMoneyAmount.setCurrencyId(rebatesCurrency_Id);
				rebatesMoneyAmount.setAmount(rebates_Money);
				rebatesMoneyAmount.setUid(travelerId);
				rebatesMoneyAmount.setMoneyType(Context.MONEY_TYPE_FYFY);
				rebatesMoneyAmount.setOrderType(orderType);
				rebatesMoneyAmount.setBusindessType(2);
				rebatesMoneyAmountList.add(rebatesMoneyAmount);
			} else {  // 不存在，则存储一条
				MoneyAmount rebatesMoneyAmount  = new MoneyAmount(rebatesMoneySerialNum, rebatesCurrency_Id, rebates_Money, travelerId, Context.MONEY_TYPE_FYFY, orderType, 2, userId);
				rebatesMoneyAmountList.add(rebatesMoneyAmount);
			}
		}
		
		//保存游客成本价
		List<MoneyAmount> costMoneyAmountList= Lists.newArrayList();
		JSONObject costPrice = null;
		List<MoneyAmount> costMoneyAmounts = moneyAmountService.findAmountBySerialNum(costPriceSerialNum);  // 获取原有的moneyAmount
		if (StringUtils.isBlank(costPriceSerialNum)) {
			costPriceSerialNum = UuidUtils.generUuid();
		}
		for(int i =0 ; i < costPriceObject.size(); i++){
			costPrice = costPriceObject.getJSONObject(i);
			if (costPrice.get("currencyId") == null || "".equals(costPrice.get("currencyId").toString())
					|| costPrice.get("price") == null || "".equals(costPrice.get("price").toString())) {
				continue;
			}
			int currencyId = costPrice.getInt("currencyId");
			BigDecimal price = new BigDecimal(costPrice.getDouble("price"));
			// 若存在，则对应币种的money重新设置主要字段
			if (CollectionUtils.isNotEmpty(costMoneyAmounts)) {
				for (MoneyAmount costMoneyAmount : costMoneyAmounts) {
					if (costMoneyAmount.getCurrencyId() == currencyId) {
						costMoneyAmount.setAmount(price);
						costMoneyAmount.setUid(travelerId);
						costMoneyAmount.setMoneyType(Context.MONEY_TYPE_CBJ);
						costMoneyAmount.setOrderType(orderType);
						costMoneyAmount.setBusindessType(2);
						costMoneyAmountList.add(costMoneyAmount);
					}
					continue;
				}
			} else {  // 不存在，则存储一条
				MoneyAmount costMoneyAmount = new MoneyAmount(costPriceSerialNum, currencyId, price, travelerId, Context.MONEY_TYPE_CBJ, orderType, 2, userId);
				costMoneyAmountList.add(costMoneyAmount);
			}
			
		}
		
		moneyAmountService.saveMoneyAmounts(moneyAmountList);		//保存游客结算价
		moneyAmountService.saveMoneyAmounts(oldMoneyAmountList);	//保存游客原始结算价
		moneyAmountService.saveMoneyAmounts(costMoneyAmountList);	//保存成本价
		moneyAmountService.saveMoneyAmounts(rebatesMoneyAmountList);		//保存返佣费用
		
		updateSerialNumByTravelerId(payPriceSerialNum, travelerId);
		updateOriginalPayPriceSerialNumByTravelerId(originalPayPriceSerialNum,travelerId);
		updateCostSerialNumByTravelerId(costPriceSerialNum, travelerId); //根据游客Id修改游客成本价流水号
		updateRebatesMoneySerialNumByTravelerId(rebatesMoneySerialNum,travelerId);	//根据游客Id修改游客返佣费用流水号
		
		traveler.setPayPriceSerialNum(payPriceSerialNum);
		traveler.setOriginalPayPriceSerialNum(originalPayPriceSerialNum);
		traveler.setCostPriceSerialNum(costPriceSerialNum);
		traveler.setRebatesMoneySerialNum(rebatesMoneySerialNum);
	}
	
	/**
	 * 
	 *  功能:保存游客签证信息
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-10-28 下午5:50:21
	 *  @param groupOpenDate 预计出团时间
	 *  @param travelerId 游客ID
	 *  @param request
	 */
	private void saveVisa(Long travelerId, JSONArray visaArr){
		//清除相关签证信息
		travelerVisaService.delete(travelerId);
		for(int i = 0; i < visaArr.size(); i++){
			TravelerVisa travelerVisa = new TravelerVisa();
			JSONObject jsonVisa = visaArr.getJSONObject(i);
			Area applyCountry = areaService.get(jsonVisa.getLong("applyCountryId"));
			travelerVisa.setApplyCountry(applyCountry);
			if(!"null".equals(jsonVisa.getString("manorId")) && StringUtils.isNotBlank(jsonVisa.getString("manorId"))){
				travelerVisa.setManorId(jsonVisa.getInt("manorId"));
			}
			if(!"null".equals(jsonVisa.getString("visaTypeId")) && StringUtils.isNotBlank(jsonVisa.getString("visaTypeId"))){
				travelerVisa.setVisaTypeId(jsonVisa.getInt("visaTypeId"));
			}
			if(jsonVisa.get("groupOpenDate") != null){
				travelerVisa.setGroupOpenDate(DateUtils.parseDate(jsonVisa.getString("groupOpenDate")));
			}
			if(jsonVisa.get("contractDate") != null){
				travelerVisa.setContractDate(DateUtils.parseDate(jsonVisa.getString("contractDate")));
			}

			if(jsonVisa.get("visaDate") != null){
				travelerVisa.setVisaDate(DateUtils.parseDate(jsonVisa.getString("visaDate")));
			}
			travelerVisa.setZbqType(jsonVisa.getInt("zbqType"));
			travelerVisa.setTraveler(findTravelerById(travelerId));
			travelerVisa = travelerVisaService.save(travelerVisa);
		}
	}
	
	/**
	 * 
	 *  功能:保存游客签证信息 (订单修改时，保存到团控列表中)
	 *	C147 & C109
	 *  @author yang.jiang
	 *  @DateTime 2016-2-3 12:08:42
	 *  @param jsonObject 
	 *  @param groupOpenDate 预计出团时间
	 *  @param travelerId 游客ID
	 *  @param request
	 */
	private Map<String, Object> saveVisa2GroupHandle4Modify(Traveler traveler, JSONArray visaArr, Map<String, String> paraMap){
		Map<String, Object> resultMap = new HashMap<>();
		//
		String orderId = paraMap.get("orderId");
		String orderNum = paraMap.get("orderNum");
		String productId = paraMap.get("productId");
		String productGroupId = paraMap.get("productGroupId"); 
		String activityKind = paraMap.get("activityKind"); 
		String agentId = paraMap.get("agentId");
		String groupHandleIdStr = paraMap.get("groupHandleId");
//		清除相关签证信息
//		travelerVisaService.delete(travelerId);
		//依据团控id获取或组织团控实体
		GroupHandle groupHandle = null;
		if (StringUtils.isBlank(groupHandleIdStr)) {			
			groupHandle = new GroupHandle();
		} else {
			groupHandle = groupHandleDao.getById(Integer.parseInt(groupHandleIdStr));
		}
		//如果数据库中无对应团控记录，则组织实体存入
		ActivityGroup activityGroup = activityGroupDao.getById(Long.parseLong(productGroupId));
		if (groupHandle == null || groupHandle.getId() == null) {
			groupHandle.setOrderId(Integer.parseInt(orderId));
			groupHandle.setOrderNum(orderNum);
			groupHandle.setSalerId(UserUtils.getUser().getId().intValue());
			groupHandle.setSalerName(UserUtils.getUser().getName());
			TravelActivity travelActivity = travelActivityDao.getById(Long.parseLong(productId));
			groupHandle.setActivityGroupCode(activityGroup.getGroupCode()+"-V");
			groupHandle.setActivityGroupId(Integer.parseInt(productGroupId));
			groupHandle.setActivityProductId(Integer.parseInt(productId));
			groupHandle.setActivityProductKind(Integer.parseInt(activityKind));
			groupHandle.setActivityProductName(travelActivity.getAcitivityName());
			groupHandle.setAgentinfoId(Integer.parseInt(agentId));
			String agentName = agentinfoDao.getAgentNameById(Long.parseLong(agentId));
			groupHandle.setAgentinfoName(agentName);
			groupHandle.setOpId(travelActivity.getCreateBy().getId().intValue());
			groupHandle.setOpName(travelActivity.getCreateBy().getName());
			groupHandle.setDelFlag("0");
			groupHandle = groupHandleDao.save(groupHandle);
		}
		
		List<GroupHandleVisa> tobeUpdateVisas = new ArrayList<>();  //待更新的签证
		List<GroupHandleVisa> tobeSaveVisas = new ArrayList<>();  //待新增的签证
		List<GroupHandleVisa> tobeDeleteVisas = new ArrayList<>();  //待删除的签证
		
		List<TravelerVisaNew> tobeUpdateZBVisas = new ArrayList<>();  //待更新的自备签
		List<TravelerVisaNew> tobeSaveZBVisas = new ArrayList<>();  //待新增的自备签
		List<TravelerVisaNew> tobeDeleteZBVisas = new ArrayList<>();  //待删除的自备签
		//组织签证数据
		for(int i = 0; i < visaArr.size(); i++) {
			JSONObject jsonVisa = visaArr.getJSONObject(i);
			String countrySelectStr = jsonVisa.getString("applyCountryId");
			if("-1".equals(countrySelectStr)) {
				continue;
			}
			String visaTypeId = jsonVisa.getString("visaTypeId");
			// 自备签证
			if(StringUtils.isNotBlank(visaTypeId) && "0".equals(visaTypeId)) {
				// 原自备签id
				String orgVisaId = jsonVisa.getString("orgVisaId");
				// 原签证类型 orgVisaType
				String orgVisaType = jsonVisa.getString("orgVisaType");
				// 判断操作类型, 填充批量操作集合
				if (StringUtils.isBlank(orgVisaId) || !"0".equals(orgVisaType)) {
					tobeSaveZBVisas.add(transferVisaJson2Entity4ZB(jsonVisa, groupHandle, activityGroup, traveler, "insert", paraMap));  //没有旧的id则添加
				} else {
					tobeUpdateZBVisas.add(transferVisaJson2Entity4ZB(jsonVisa, groupHandle, activityGroup, traveler, "update", paraMap));  //有旧的id则更新
				}							
//				travelerVisa = travelerVisaService.save(travelerVisa);
		    // 除自备签证外的
			} else {
				// 原签证id
				String orgVisaId = jsonVisa.getString("orgVisaId");
				// 原签证类型 orgVisaType
				String orgVisaType = jsonVisa.getString("orgVisaType");
				// 判断操作类型, 填充批量操作集合
				if (StringUtils.isBlank(orgVisaId) || StringUtils.isBlank(orgVisaType) || "0".equals(orgVisaType)) {
					tobeSaveVisas.add(transferVisaJson2Entity(jsonVisa, groupHandle, activityGroup, traveler, "insert", paraMap));  //没有旧的id则添加
				} else {
					tobeUpdateVisas.add(transferVisaJson2Entity(jsonVisa, groupHandle, activityGroup, traveler, "update", paraMap));  //有旧的id则更新
				}
			}
		}
		// 获取游客对应的所有正常记录
		List<GroupHandleVisa> allNormalVisas = groupHandleVisaService.getByHandleAndTraveler(groupHandle.getId(), traveler.getId(), Context.DEL_FLAG_NORMAL);
		List<TravelerVisaNew> allNormalZBVisas = travelerVisaNewService.findVisaListByPid(traveler.getId());
		// 填充批量删除集合
		allNormalVisas.removeAll(tobeUpdateVisas);
		tobeDeleteVisas = allNormalVisas;
		allNormalZBVisas.removeAll(tobeUpdateZBVisas);
		tobeDeleteZBVisas = allNormalZBVisas;
		for (GroupHandleVisa deleteVisa : tobeDeleteVisas) {
			// 设为草稿--待删除。订单保存，删除成1。订单不保存，再次加载修改详情时，恢复成0。
			deleteVisa.setDelFlag("1");
		}
		for (TravelerVisaNew deleteVisa : tobeDeleteZBVisas) {
			// 设为草稿--待删除。订单保存，删除成1。订单不保存，再次加载修改详情时，恢复成0。
			deleteVisa.setDelFlag("1");
		}		
		// 批量操作（游客保存只做预保存，订单保存时才真正保存）
		List<GroupHandleVisa> savedVisas = groupHandleVisaService.batchSaveReturn(tobeSaveVisas);  //保存
		List<TravelerVisaNew> savedZBVisas = travelerVisaNewService.batchSaveReturn(tobeSaveZBVisas);  //保存
		groupHandleVisaService.batchUpdate(tobeUpdateVisas);  //更新
		travelerVisaNewService.batchUpdate(tobeUpdateZBVisas);  //更新
		groupHandleVisaService.batchUpdate(tobeDeleteVisas);  //删除
		travelerVisaNewService.batchUpdate(tobeDeleteZBVisas);  //删除
		//组织返回值
		resultMap.put("groupHandleId", groupHandle.getId());  //团控id，一条
		String groupHandleVisaIdStr = ""; 
		String orgVisaTypeStr = ""; 
		for (GroupHandleVisa groupHandleVisa : savedVisas) {
			groupHandleVisaIdStr += groupHandleVisa.getId() + ",";
			orgVisaTypeStr += groupHandleVisa.getVisaTypeId() + ",";
		}
		for (TravelerVisaNew travelerVisa : savedZBVisas) {
			groupHandleVisaIdStr += travelerVisa.getId() + ",";
			orgVisaTypeStr += "0" + ",";
		}
		resultMap.put("groupHandleVisaIdStr", groupHandleVisaIdStr);  //团控签证诸id， 或多条
		resultMap.put("orgVisaTypeStr", orgVisaTypeStr);  // 签证类型，自备签是0
		return resultMap;
	}
	
	/**
	 * 把从前台传入的json形式的visa信息 组织为group_control_visa对应的实体
	 * @author yang.jiang 2016-2-3 16:02:31
	 * @param jsonVisa
	 * @return
	 */
	private GroupHandleVisa transferVisaJson2Entity(JSONObject jsonVisa, GroupHandle groupHandle, ActivityGroup activityGroup, Traveler traveler, String operationType, Map<String, String> paraMap){
		GroupHandleVisa newVisa = new GroupHandleVisa();
		if (jsonVisa == null) {
			return newVisa;
		}

		//新增
		if ("insert".equals(operationType)) {
			newVisa = new GroupHandleVisa();			
		}
		//更新
		if ("update".equals(operationType)) {
			// 原签证id
			String orgVisaId = jsonVisa.getString("orgVisaId");
			newVisa = groupHandleVisaService.findById(Integer.parseInt(orgVisaId));
		}
		String orderId = paraMap.get("orderId");
		String orderNum = paraMap.get("orderNum");
		// 团控id
		newVisa.setGroupHandleId(groupHandle.getId());
		// 订单id
		newVisa.setOrderId(Integer.parseInt(orderId));
		// 订单num
		newVisa.setOrderNum(orderNum);
		// 游客id
		newVisa.setTravelerId(traveler.getId().intValue());
		// 游客name
		newVisa.setTravelerName(traveler.getName());
		// 护照号
		newVisa.setPassportNum(traveler.getPassportCode());
		String countrySelectStr = jsonVisa.getString("applyCountryId");
		// 签证国家id
		if(StringUtils.isNotBlank(countrySelectStr)) {
			newVisa.setVisaCountryId(Integer.parseInt(countrySelectStr));
		}
		// 签证国家name
		if(StringUtils.isNotBlank(countrySelectStr)) {
			Country country = CountryUtils.getCountry(Long.parseLong(countrySelectStr));
			newVisa.setVisaCountryName(country.getCountryName_cn());
		}
		// 领区
		if(!"null".equals(jsonVisa.getString("manorId")) && StringUtils.isNotBlank(jsonVisa.getString("manorId"))){
			newVisa.setVisaConsularDistricId(Integer.parseInt(jsonVisa.getString("manorId")));
			Dict dict = DictUtils.getDict(jsonVisa.getString("manorId"), "from_area");
			newVisa.setVisaConsularDistricName(dict.getLabel());
		}
		// 签证类型
		if(!"null".equals(jsonVisa.getString("visaTypeId")) && StringUtils.isNotBlank(jsonVisa.getString("visaTypeId"))){
			newVisa.setVisaTypeId(Integer.parseInt(jsonVisa.getString("visaTypeId")));
			Dict dict = DictUtils.getDict(jsonVisa.getString("visaTypeId"), "new_visa_type");
			newVisa.setVisaTypeName(dict.getLabel());
		}
		// 预计签约时间
		if(!"null".equals(jsonVisa.getString("contractDate")) && StringUtils.isNotBlank(jsonVisa.getString("contractDate"))) {
			try {
				newVisa.setAboutSigningTime(new SimpleDateFormat("dd/MM/yyyy").parse(jsonVisa.getString("contractDate")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 团号
		newVisa.setActivityGroupCode(groupHandle.getActivityGroupCode());
		// 签证状态
		newVisa.setVisaStauts(-1);			
		newVisa.setUpdateDate(new Date());
		newVisa.setUpdateBy(UserUtils.getUser());
		newVisa.setDelFlag("0");
		
		return newVisa;
	}
	
	/**
	 * 把从前台传入的json形式的visa信息 组织为travelervisa对应的实体, 自备签
	 * @author yang.jiang 2016-2-3 16:02:31
	 * @param jsonVisa
	 * @return
	 */
	private TravelerVisaNew transferVisaJson2Entity4ZB(JSONObject jsonVisa, GroupHandle groupHandle, ActivityGroup activityGroup, Traveler traveler, String operationType, Map<String, String> paraMap){
		TravelerVisaNew travelerVisa = new TravelerVisaNew();
		if (jsonVisa == null) {
			return travelerVisa;
		}

		//新增
		if ("insert".equals(operationType)) {
			travelerVisa = new TravelerVisaNew();			
		}
		//更新
		if ("update".equals(operationType)) {
			// 原签证id
			String orgVisaId = jsonVisa.getString("orgVisaId");
			travelerVisa = travelerVisaNewService.getById(Long.parseLong(orgVisaId));
		}
		String countrySelectStr = jsonVisa.getString("applyCountryId");
		// 签证国家id
		if(StringUtils.isNotBlank(countrySelectStr)) {  // TODO
			//获取国籍信息
//		    List<Country> countryList = CountryUtils.getCountrys();
//			Area applyCountry = areaService.get(Long.parseLong(countrySelectStr));
//			travelerVisa.setApplyCountry(applyCountry);
//			travelerVisa.setApplyCountry(CountryUtils.getCountry(Long.parseLong(countrySelectStr)));
//			travelerVisa.setApplyCountryId(Integer.parseInt(countrySelectStr));

			Country country = CountryUtils.getCountry(Long.parseLong(countrySelectStr));
//					travelerVisa.setApplyCountry(applyCountry);
			travelerVisa.setApplyCountryNew(country);
		}
		// 领区
		if(!"null".equals(jsonVisa.getString("manorId")) && StringUtils.isNotBlank(jsonVisa.getString("manorId"))){
			travelerVisa.setManorId(jsonVisa.getInt("manorId"));
			Dict dict = DictUtils.getDict(jsonVisa.getString("manorId"), "from_area");
			travelerVisa.setManorName(dict.getLabel());  // 领区名称
		}
		// 签证类型
		if(!"null".equals(jsonVisa.getString("visaTypeId")) && StringUtils.isNotBlank(jsonVisa.getString("visaTypeId"))){
			travelerVisa.setVisaTypeId(jsonVisa.getInt("visaTypeId"));
		}
		// 预计出团时间
		travelerVisa.setGroupOpenDate(DateUtils.parseDate(jsonVisa.getString("groupOpenDate").trim()));
		if(jsonVisa.get("contractDate") != null){
			travelerVisa.setContractDate(DateUtils.parseDate(jsonVisa.getString("contractDate")));
		}
		// 自备签有效期
		if(jsonVisa.get("visaDate") != null){
			travelerVisa.setVisaDate(DateUtils.parseDate(jsonVisa.getString("visaDate")));
		}
		travelerVisa.setZbqType(1);  //是否自备签（办签：0 自备签：1）
		travelerVisa.setTraveler(traveler);
		travelerVisa.setDelFlag("0");
		travelerVisa.setUpdateDate(new Date());
		travelerVisa.setUpdateBy(UserUtils.getUser());
		
		return travelerVisa;
	}

	/**
	 * 保存游客信息到团控列表
     */
	private Integer saveVisa2GroupHandle(Traveler traveler, JSONArray visaArr, String productId, String productGroupId, String activityKind,
			String agentId, String groupHandleIdStr) {
		// 保存 GroupHandle 对象
		GroupHandle groupHandle = new GroupHandle();
		if(StringUtils.isNotBlank(groupHandleIdStr)) {
			groupHandle = groupHandleDao.getById(Integer.parseInt(groupHandleIdStr));
		}
		groupHandle.setSalerId(UserUtils.getUser().getId().intValue());
		groupHandle.setSalerName(UserUtils.getUser().getName());
		ActivityGroup activityGroup = activityGroupDao.getById(Long.parseLong(productGroupId));
		TravelActivity travelActivity = travelActivityDao.getById(Long.parseLong(productId));
		groupHandle.setActivityGroupCode(activityGroup.getGroupCode()+"-V");
		groupHandle.setActivityGroupId(Integer.parseInt(productGroupId));
		groupHandle.setActivityProductId(Integer.parseInt(productId));
		groupHandle.setActivityProductKind(Integer.parseInt(activityKind));
		groupHandle.setActivityProductName(travelActivity.getAcitivityName());
		groupHandle.setAgentinfoId(Integer.parseInt(agentId));
		String agentName = agentinfoDao.getAgentNameById(Long.parseLong(agentId));
		groupHandle.setAgentinfoName(agentName);
		groupHandle.setOpId(travelActivity.getCreateBy().getId().intValue());
		groupHandle.setOpName(travelActivity.getCreateBy().getName());
		groupHandle.setDelFlag("2");// 草稿状态
		GroupHandle returnGroupHandle = groupHandleDao.save(groupHandle);

		List<GroupHandleVisa> groupHandleVisaList =
				groupHandleVisaDao.findByGroupHandleIdAndTravelerId(groupHandle.getId(), traveler.getId().intValue());
		if(groupHandleVisaList != null && groupHandleVisaList.size() > 0) {
			for(GroupHandleVisa groupHandleVisa : groupHandleVisaList) {
				groupHandleVisa.setDelFlag("1");
				groupHandleVisaDao.save(groupHandleVisa);
			}
		}

		List<TravelerVisa> travelerVisaList = travelerVisaDao.findByTravelerId(traveler.getId());
		if(travelerVisaList != null && travelerVisaList.size() > 0) {
			for(TravelerVisa travelerVisa : travelerVisaList) {
				travelerVisa.setDelFlag("1");
				travelerVisaDao.save(travelerVisa);
			}
		}


		for(int i = 0; i < visaArr.size(); i++) {
			JSONObject jsonVisa = visaArr.getJSONObject(i);
			String countrySelectStr = jsonVisa.getString("applyCountryId");
			if("-1".equals(countrySelectStr)) {
				continue;
			}
			String visaTypeId = jsonVisa.getString("visaTypeId");
			// 自备签证
			if(StringUtils.isNotBlank(visaTypeId) && "0".equals(visaTypeId)) {
				TravelerVisa travelerVisa = new TravelerVisa();
				// 签证国家id
				if(StringUtils.isNotBlank(countrySelectStr)) {
//					Area applyCountry = areaService.get(Long.parseLong(countrySelectStr));
					Country country = CountryUtils.getCountry(Long.parseLong(countrySelectStr));
//					travelerVisa.setApplyCountry(applyCountry);
					travelerVisa.setApplyCountryNew(country);
				}
				if(!"null".equals(jsonVisa.getString("manorId")) && StringUtils.isNotBlank(jsonVisa.getString("manorId"))){
					travelerVisa.setManorId(jsonVisa.getInt("manorId"));
					Dict dict = DictUtils.getDict(jsonVisa.getString("manorId"), "from_area");
					travelerVisa.setManorName(dict.getLabel());
				}
				if(!"null".equals(jsonVisa.getString("visaTypeId")) && StringUtils.isNotBlank(jsonVisa.getString("visaTypeId"))){
					travelerVisa.setVisaTypeId(jsonVisa.getInt("visaTypeId"));
				}
				travelerVisa.setGroupOpenDate(DateUtils.parseDate(jsonVisa.getString("groupOpenDate")));
				if(jsonVisa.get("contractDate") != null){
					travelerVisa.setContractDate(DateUtils.parseDate(jsonVisa.getString("contractDate")));
				}
				if(jsonVisa.get("visaDate") != null){
					travelerVisa.setVisaDate(DateUtils.parseDate(jsonVisa.getString("visaDate")));
				}
				travelerVisa.setZbqType(1);
				travelerVisa.setTraveler(traveler);
				travelerVisa.setDelFlag("2");			// 设为草稿状态
				travelerVisa = travelerVisaService.save(travelerVisa);
		    // 除自备签证外的
			} else {
				GroupHandleVisa groupHandleVisa = new GroupHandleVisa();
				groupHandleVisa.setGroupHandleId(returnGroupHandle.getId());
				// 游客id
				groupHandleVisa.setTravelerId(traveler.getId().intValue());
				// 游客name
				groupHandleVisa.setTravelerName(traveler.getName());
				// 护照号
				groupHandleVisa.setPassportNum(traveler.getPassportCode());
				// 签证国家id
				if(StringUtils.isNotBlank(countrySelectStr)) {
					groupHandleVisa.setVisaCountryId(Integer.parseInt(countrySelectStr));
				}
				// 签证国家name
				if(StringUtils.isNotBlank(countrySelectStr)) {
					Country country = CountryUtils.getCountry(Long.parseLong(countrySelectStr));
					groupHandleVisa.setVisaCountryName(country.getCountryName_cn());
				}
				// 领区
				if(!"null".equals(jsonVisa.getString("manorId")) && StringUtils.isNotBlank(jsonVisa.getString("manorId"))){
					groupHandleVisa.setVisaConsularDistricId(Integer.parseInt(jsonVisa.getString("manorId")));
					Dict dict = DictUtils.getDict(jsonVisa.getString("manorId"), "from_area");
					groupHandleVisa.setVisaConsularDistricName(dict.getLabel());
				}
				// 签证类型
				if(!"null".equals(jsonVisa.getString("visaTypeId")) && StringUtils.isNotBlank(jsonVisa.getString("visaTypeId"))){
					groupHandleVisa.setVisaTypeId(Integer.parseInt(jsonVisa.getString("visaTypeId")));
					Dict dict = DictUtils.getDict(jsonVisa.getString("visaTypeId"), "new_visa_type");
					groupHandleVisa.setVisaTypeName(dict.getLabel());
				}
				// 预计签约时间
				if(!"null".equals(jsonVisa.getString("contractDate")) && StringUtils.isNotBlank(jsonVisa.getString("contractDate"))) {
					try {
						groupHandleVisa.setAboutSigningTime(new SimpleDateFormat("dd/MM/yyyy").parse(jsonVisa.getString("contractDate")));
					} catch(ParseException e) {
						e.printStackTrace();
					}
				}
				// 团号
				groupHandleVisa.setActivityGroupCode(activityGroup.getGroupCode() + "-V");

				groupHandleVisa.setVisaStauts(-1);
				groupHandleVisa.setDelFlag("2");	// 设为草稿状态
				groupHandleVisaDao.save(groupHandleVisa);
			}
		}

		return returnGroupHandle.getId();

	}
	
	/**
	 * 
	 *  功能:保存其他费用
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-11-5 下午12:12:37
	 *  @param jsonCost
	 *  @param travelerId
	 */
	public void saveCost(JSONArray jsonCost,Long travelerId){
		//清除费用相关信息
		costchangeService.delete(travelerId);
		if (jsonCost != null && jsonCost.size() > 0) {
			for(int i = 0; i < jsonCost.size(); i++){
				JSONObject costJson = jsonCost.getJSONObject(i);
				if (!costJson.isEmpty() && costJson.get("name") != null 
						&& costJson.get("currency") != null && costJson.get("sum") != null && costJson.get("businessType") != null) {
					
					// 如果是审核过的其他费用则不保存
					if ("1".equals(costJson.get("businessType").toString())) {
						continue;
					}
					
					
					Costchange costchange = new Costchange();
					// 费用名称
			        String name = costJson.getString("name");
			        if (StringUtils.isNotBlank(name)) {
			            costchange.setCostName(name.toString());
			        }
			        // 单价
			        String price = costJson.getString("price");
			        if (StringUtils.isNotBlank(price)) {
			        	costchange.setCostPrice(new BigDecimal(price));
			        }
			        // 数量
			        String num = costJson.getString("num");
			        if(StringUtils.isNotBlank(num)){
			        	costchange.setCostNum(new BigDecimal(num));
			        }
			        // 费用币种
			        String curency = costJson.getString("currency");
			        if(StringUtils.isNotBlank(curency)){
			            costchange.setPriceCurrency(currencyService.findCurrency(Long.parseLong(curency)));
			        }
			        // 费用金额
			        String sum = costJson.getString("sum");
			        if (StringUtils.isNotBlank(sum)) {
			            costchange.setCostSum(StringNumFormat.getBigDecimalForTow(sum));
			        }
			        costchange.setTravelerId(travelerId);
			        costchangeService.save(costchange);
				}
			}
		}
	}
	
	
	/**
	 * 删除游客
	 * @param id 游客ID
	 */
	@Transactional
    public void deleteTraveler(Long id){
		// 优佳奢华 C147&C109 删除团控签证信息
		groupHandleVisaDao.deleteByTraveler(Integer.parseInt(id.toString()));
		travelerVisaDao.delTravelerVisaByTravelerId(id);
		
        costchangeDao.deleteCostchangeByTravelerId(id);
        travelerFileDao.delTravelerFileByTravelerId(id);
        travelerVisaDao.delTravelerVisaByTravelerId(id);
        travelerDao.delete(id);
    }
	
	/**
	 * 根据订单ID和订单类型查询对应游客
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<Traveler> findTravelerByOrderIdAndOrderType(Long orderId, int orderType, List<Integer> delFlag){
		return travelerDao.findTravelerByOrderIdAndOrderType(orderId, orderType, delFlag);
	}
	
	/**
	 * 根据订单ID和订单类型查询对应游客
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<Traveler> findTravelerByOrderIdAndOrderType(Long orderId, int orderType){
		return travelerDao.findTravelerByOrderIdAndOrderType(orderId, orderType);
	}
	
	/**
	 * 根据游客Id修改游客结算价流水号
	 * @param payPriceSerialNum 游客结算价流水号
	 * @param travelerId 游客Id
	 */
	public void updateSerialNumByTravelerId(String payPriceSerialNum, Long travelerId){
		travelerDao.updateSerialNumByTravelerId(payPriceSerialNum, travelerId);
	}
	
	
	/**
	 * 根据游客Id修改游客返佣费用流水号
	 * @param payPriceSerialNum 游客结算价流水号
	 * @param travelerId 游客Id
	 */
	private void updateRebatesMoneySerialNumByTravelerId(String rebatesMoneySerialNum, Long travelerId){
		travelerDao.updateRebatesMoneySerialNumByTravelerId(rebatesMoneySerialNum, travelerId);
	}
	
	
	/**
	 * 根据游客Id修改游客成本价流水号
	 * @param payPriceSerialNum 游客结算价流水号
	 * @param travelerId 游客Id
	 */
	public void updateCostSerialNumByTravelerId(String costPriceSerialNum, Long travelerId){
		travelerDao.updateCostSerialNumByTravelerId(costPriceSerialNum, travelerId);
	}
	
	/**
	 * 根据游客Id更新游客的借款uuid 
	 * @param jkSerialNum 游客结算价流水号
	 * @param travelerId 游客Id
	 */
	public void updateJkSerialNumByTravelerId(String jkSerialNum, Long travelerId){
		travelerDao.updateJkSerialNumByTravelerId(jkSerialNum, travelerId);
	}
	
	/**
	 * 根据游客id找到游客信息 add by chy 2014年11月11日18:13:39
	 */
	public Traveler findTravelerById(Long travelerId) {
		return travelerDao.findOne(travelerId);
	}
	
	/**
	 * 根据主订单游客id查询签证对应游客
	 * @param orderId
	 * @return
	 */
	public List<Traveler> findTravelersByMainTravelerId(Long travelerId) {
		List<Traveler> travelerList = travelerDao.findTravelersByMainTravelerId(travelerId);
		return travelerList;
	}
	
	/**
	 * 根据游客ID查询上传文件
	 * @param travelerId 游客ID
	 * @return 上传文件List
	 */
	public List<TravelerFile> findFileListByPid(Long travelerId) {
		return travelerFileDao.findFileListByPid(travelerId);
	}

	/**
	 * 批量保存游客附件信息，同事删除之前同一类型附件
	 * 非自备签由于没有删除功能只有替换功能，且只有单个文件，这里只需要对同类型的删除原有即可；自备签存在删除功能，此处暴力删除。
	 * TODO 查询出原有，跟现有对比，区分增删改。
	 * @param travelerFileList
	 *            附件信息对象组装集合
	 */
	private void saveTravelerFile(List<TravelerFile> travelerFileList) {
		if (travelerFileList != null && travelerFileList.size() > 0) {
			// 在添加之前把存在的文件关系删除
			for (int i = 0; i < travelerFileList.size(); i++) {
				if (travelerFileList.get(i).getFileType() != null) {
					travelerFileDao.delTravelerFileByUninId( travelerFileList.get(i).getFileType(), travelerFileList.get(i).getSrcTravelerId());
				}
			}
			for (TravelerFile travelerFile : travelerFileList) {
				travelerFile.setTraveler(travelerDao.findOne(travelerFile.getSrcTravelerId()));
				travelerFile.setDocInfo(docInfoService.getDocInfo(travelerFile.getSrcDocId()));
				travelerFileDao.save(travelerFile);
			}
		}
	}
//	public void saveTravelerFile(List<TravelerFile> travelerFileList) {
//		if (CollectionUtils.isNotEmpty(travelerFileList)) {
//			
//			// 在添加之前把存在的文件关系删除（自备签全删，非自备签删除对应类型）
//			Set<Integer> fileTypeSet = new HashSet<>(); 
//			for (TravelerFile travelerFile : travelerFileList) {
//				fileTypeSet.add(travelerFile.getFileType());  //发生变动的非自备签（无删除、只可能是单文件替换）
//			}
//			// 自备签 文件类型
//			fileTypeSet.add(9);
//			fileTypeSet.add(10);
//			fileTypeSet.add(11);
//			travelerFileDao.delByTypesAndTrvl(new ArrayList<>(fileTypeSet), travelerFileList.get(0).getSrcTravelerId());
//			// 添加新的
//			for (TravelerFile travelerFile : travelerFileList) {
//				travelerFile.setTraveler(travelerDao.findOne(travelerFile.getSrcTravelerId()));
//				travelerFile.setDocInfo(docInfoService.getDocInfo(travelerFile.getSrcDocId()));
//				travelerFileDao.save(travelerFile);
//			}
//		}
//	}
	
	/**
	 * 批量保存游客附件信息，同事删除之前同一类型附件
	 *
	 * @param travelerFileList
	 *            附件信息对象组装集合
	 */
	private void saveTravelerFile4YouJia(List<TravelerFile> travelerFileList) {
		if (travelerFileList != null && travelerFileList.size() > 0) {
			// 在添加之前把存在的文件关系删除
			for (TravelerFile travelerFile : travelerFileList) {
				travelerFile.setTraveler(travelerDao.findOne(travelerFile.getSrcTravelerId()));
				travelerFile.setDocInfo(docInfoService.getDocInfo(travelerFile.getSrcDocId()));
				travelerFileDao.save(travelerFile);
			}
		}
	}
	
	/**
	 * @Description 游客修改时保存订单成本价和结算价
	 * @author yakun.bai
	 * @Date 2016-1-18
	 */
	private void saveOrderMoney(ProductOrderCommon order, Map<String, String> paraMap) {
		String costCurrencyId = paraMap.get("cbCurrencyId");
		String costCurrencyPrice = paraMap.get("cbCurrencyPrice");
		String currencyId = paraMap.get("jsCurrencyId");
		String currencyPrice = paraMap.get("jsCurrencyPrice");
//		String totalCharge = paraMap.get("totalCharge");  //(这里不使用前台传入的金额，使用总结算价与服务费率进行计算)
		//结算价信息
		Map<String,BigDecimal> totalMoneyMap = new HashMap<String, BigDecimal>();
		if(StringUtils.isNotBlank(currencyId) && StringUtils.isNotBlank(currencyPrice)){
			String[] currencyIdArr = currencyId.trim().split(",");
			String[] currencyPrcieArr = currencyPrice.trim().split(",");
			for(int i = 0; i < currencyIdArr.length; i++) {
				totalMoneyMap.put(currencyIdArr[i], new BigDecimal(currencyPrcieArr[i]));
			}
		}
		
		// 散拼产品使用quauq价报名生成的订单： 保存服务费、订单总额添加服务费
		if (order.getOrderStatus() == Context.ORDER_TYPE_SP && order.getPriceType() == Context.PRICE_TYPE_QUJ) {			
//			String RMB_currencyId = currencyService.getRMBCurrencyId().getId().toString();
			// 总结算价---> RMB
			BigDecimal clearRMB = BigDecimal.ZERO;
			if(totalMoneyMap != null && !totalMoneyMap.isEmpty()){
				for (String key : totalMoneyMap.keySet()) {
					Currency currency = currencyDao.findOne(Long.parseLong(key));
					BigDecimal money = totalMoneyMap.get(key) == null ? BigDecimal.ZERO : totalMoneyMap.get(key);
					clearRMB = clearRMB.add(money.multiply(currency.getConvertLowest()));  // 转成RMB 累加
				}
			}
//			// 总结算价RMB * 服务费率  = 订单总服务费 
//			BigDecimal totalCharge = clearRMB.multiply(order.getOrderChargeRate());
//			if (totalCharge != null && totalCharge.compareTo(BigDecimal.ZERO) > 0) {
//				List<MoneyAmount> orgTotalCharge = moneyAmountDao.getAmountByUid(order.getQuauqServiceCharge());
//				if (CollectionUtils.isNotEmpty(orgTotalCharge)) {
//					MoneyAmount chargeMoneyAmount = orgTotalCharge.get(0);
//					chargeMoneyAmount.setAmount(totalCharge);
//					chargeMoneyAmount.setExchangerate(currencyService.getRMBCurrencyId().getConvertLowest());
//					chargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
//					moneyAmountService.saveOrUpdateMoneyAmount(chargeMoneyAmount);
//				}
//			}
//			// 把服务费添加到总结算价map中
//			totalMoneyMap.put(RMB_currencyId, totalCharge.add(totalMoneyMap.get(RMB_currencyId)));
		}
		
		//成本价信息
		Map<String,BigDecimal> costTotalMoneyMap = new HashMap<String, BigDecimal>();
		if(StringUtils.isNotBlank(costCurrencyId) && StringUtils.isNotBlank(costCurrencyPrice)){
			String[] costCurrencyIdArr = costCurrencyId.split(",");
			String[] costCurrencyPriceArr = costCurrencyPrice.split(",");
			for(int i = 0; i < costCurrencyIdArr.length; i++) {
				costTotalMoneyMap.put(costCurrencyIdArr[i], new BigDecimal(costCurrencyPriceArr[i]));
			}
		}
		
		List<MoneyAmount> moneyAmountList= Lists.newArrayList();
		MoneyAmount moneyAmount = null;
		List<MoneyAmount> oldMoneyAmountList= Lists.newArrayList();
		MoneyAmount oldMoneyAmount = null;
		String totalMoneySerialNum = order.getTotalMoney();
		String oldTotalMoneySerialNum = order.getOriginalTotalMoney();
		Long orderId = order.getId();
		Long userId = UserUtils.getUser().getId();
		int orderStatus = order.getOrderStatus();
		
		//保存订单结算价
		if(totalMoneyMap != null && !totalMoneyMap.isEmpty()){
			for (String key : totalMoneyMap.keySet()) {
				int cId = Integer.parseInt(key);
				BigDecimal money = totalMoneyMap.get(key);
				moneyAmount = new MoneyAmount(totalMoneySerialNum, cId, money, orderId, Context.MONEY_TYPE_YSH, orderStatus, 1, userId);
				moneyAmountList.add(moneyAmount);
				oldMoneyAmount = new MoneyAmount(oldTotalMoneySerialNum, cId, money, orderId, Context.MONEY_TYPE_YSYSH, orderStatus, 1, userId);
				oldMoneyAmountList.add(oldMoneyAmount);
			}
			moneyAmountService.saveOrUpdateMoneyAmounts(totalMoneySerialNum, moneyAmountList);
			moneyAmountService.saveOrUpdateMoneyAmounts(oldTotalMoneySerialNum, oldMoneyAmountList);
		}
		
		//保存订单成本价
		if(costTotalMoneyMap != null && !costTotalMoneyMap.isEmpty()){
			List<MoneyAmount> costMoneyAmountList= Lists.newArrayList();
			String costTotalMoneySerialNum = order.getCostMoney();	//成本价UUID
			for (String key : costTotalMoneyMap.keySet()) {
				int cId = Integer.parseInt(key);
				BigDecimal money = costTotalMoneyMap.get(key);
				moneyAmount = new MoneyAmount(costTotalMoneySerialNum, cId, money, orderId, Context.MONEY_TYPE_CBJ, orderStatus, 1, userId);
				costMoneyAmountList.add(moneyAmount);
			}
			moneyAmountService.saveOrUpdateMoneyAmounts(costTotalMoneySerialNum, costMoneyAmountList);
		}
	}
	
	/**
	 * 订单修改情况下，对售出占位和余位进行修改。
	 * add by yunpeng.zhang
	 * @param productOrder
	 * @param orgOrderPersonNum
	 */
	private Map<String, Object> changeFreePositionAndSoldPosition(ProductOrderCommon productOrder, Map<String, String> paraMap) {
		
		String orderPersonNumChilds = paraMap.get("orderPersonNumChild");
		String orderPersonNumAdults = paraMap.get("orderPersonNumAdult");
		String orderPersonNumSpecials = paraMap.get("orderPersonNumSpecial");
		String orderPersonelNum = paraMap.get("orderPersonelNum");
		String roomNumber = paraMap.get("roomNumber");
		Integer NewRoomNumber = null;
    	if( productOrder.getOrderStatus() == Context.ORDER_TYPE_CRUISE) {
    		NewRoomNumber = roomNumber != null ? Integer.parseInt(roomNumber) : productOrder.getRoomNumber();
    	}
    	Integer newTotalPersonNum = Integer.parseInt(orderPersonelNum);
	    
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("Code", "0");
		resultMap.put("Message", "Success!");
		int balancePersonNum = 0;
		//订单修改前人数
		Integer orderPersonNum = productOrder.getOrderPersonNum();
		if(StringUtils.isNotBlank(orderPersonNumChilds)) {
	        productOrder.setOrderPersonNumChild(Integer.parseInt(orderPersonNumChilds));
	    }
	    if(StringUtils.isNotBlank(orderPersonNumAdults)) {
	        productOrder.setOrderPersonNumAdult(Integer.parseInt(orderPersonNumAdults));
	    }
	    if(StringUtils.isNotBlank(orderPersonNumSpecials)) {
	    	productOrder.setOrderPersonNumSpecial(Integer.parseInt(orderPersonNumSpecials));
	    }
		
		// 邮轮扣减房间差
		if (Integer.valueOf(Context.ORDER_STATUS_CRUISE) == productOrder.getOrderStatus()) {
			if(NewRoomNumber != null && productOrder.getRoomNumber() != null) {
				balancePersonNum = NewRoomNumber - productOrder.getRoomNumber();
			}
		// 单团、散拼、大客户、自由行、游学，扣减人数差
		} else if (Integer.valueOf(Context.ORDER_STATUS_SINGLE) == productOrder.getOrderStatus() ||
				Integer.valueOf(Context.ORDER_STATUS_LOOSE) == productOrder.getOrderStatus() ||
				Integer.valueOf(Context.ORDER_STATUS_BIG_CUSTOMER) == productOrder.getOrderStatus() ||
				Integer.valueOf(Context.ORDER_STATUS_FREE) == productOrder.getOrderStatus() ||
				Integer.valueOf(Context.ORDER_STATUS_STUDY) == productOrder.getOrderStatus()) {
			balancePersonNum = newTotalPersonNum - orderPersonNum;
		}
		//把订单人数、房间数设置为新值
		productOrder.setOrderPersonNum(newTotalPersonNum);
		productOrder.setRoomNumber(NewRoomNumber);
		
		if (balancePersonNum > 0) {
			try {
				orderStockService.changeGroupFreeNum(productOrder, balancePersonNum, Context.StockOpType.MODIFY);
			} catch (Exception e) {
				resultMap.put("Code", "101");
				resultMap.put("Message", e.getMessage());
				return resultMap;
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 保存或修改游客资料
	 * @param fileList
	 * @param traveler
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveOrUpdateTravelerFile(Map<Integer, MultipartFile> fileList, Traveler traveler) {
		if(fileList != null) {
			for(Integer type : fileList.keySet()) {
				MultipartFile file = fileList.get(type);
				//如果资料没有更新则不做处理
				if(file != null && StringUtils.isNotBlank(file.getOriginalFilename())) {
					DocInfo docInfo = null;
			        String fileName = file.getOriginalFilename();
			        docInfo = new DocInfo();
			        try {
			            String path = FileUtils.uploadFile(file.getInputStream(),fileName);
			            docInfo.setDocName(fileName);
			            docInfo.setDocPath(path);
			            docInfoService.saveDocInfo(docInfo);
			            
			            TravelerFile travelerFile = null;
			            List<TravelerFile> files = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), type, Context.DEL_FLAG_NORMAL);
			            if(CollectionUtils.isNotEmpty(files)) {
			            	travelerFile = files.get(0);
			            	docInfoService.delDocInfoById(travelerFile.getSrcDocId());
			            } else {
			            	travelerFile = new TravelerFile();
			            }
			            travelerFile.setSrcTravelerId(traveler.getId());
			            travelerFile.setTraveler(traveler);
			            travelerFile.setSrcDocId(docInfo.getId());
			    		travelerFile.setDocInfo(docInfo);
			    		travelerFile.setFileName(docInfo.getDocName());
			    		travelerFile.setFileType(type);
			    		travelerFileDao.save(travelerFile);
			    		//添加操作日志
	        	        this.saveLogOperate(Context.log_type_orderform,
	        	        		Context.log_type_orderform_name, "游客"+ traveler.getName()+"上传资料成功", "2", traveler.getOrderType(), null);
			        } catch (Exception e) {  
			        	//添加操作日志
	        	        this.saveLogOperate(Context.log_type_orderform, 
	        	        		Context.log_type_orderform_name, "游客"+ traveler.getName()+"上传资料失败", "2", traveler.getOrderType(), null);
			            e.printStackTrace();  
			        }
				}
			}
		}
	}
	
	/**
	 * 根据签证产品id查询游客信息
	 * @param productGroupId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findTravelerByVisaOrderId(Long visaOrderId, Long agentId) {
		if(visaOrderId != null){
			if(agentId == null){
				agentId = 0L;
			}
			String sql = "SELECT '' as rowNum,t.name,t.nameSpell,IF(t.sex=1,'男','女') as sex,date_format(t.birthDay,'%Y/%c/%d'),c.countryName_cn,t.personType,";
					sql = sql + "t.idCard,date_format(t.validityDate,'%Y/%c/%d'),'' as visaCity,t.telephone ,a.agentName,a.agentSaler,ordercontacts.remark";
					sql = sql + " from traveler t LEFT JOIN sys_country  c on t.nationality=c.id ";
					sql = sql + "LEFT JOIN (SELECT agentName,agentSaler from agentinfo WHERE agentinfo.id=? ) as a ON 1=1 LEFT JOIN (select min(id) id,orderId orderId,remark remark from ordercontacts where orderId="+visaOrderId+" and orderType=6) ordercontacts ON t.orderId = ordercontacts.orderId where t.delFlag <> 1 AND t.orderId=? and t.order_type=6";
					
					return travelerDao.createSqlQuery(sql, agentId,visaOrderId).list();
		}else{
			return null;
		}
	}
	
	/**
	 * 查询游客明细表信息
	 */
	public List<Map<String,Object>> findTravelerdetails(Long visaOrderId, String serialNum) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT '' as rowNum,t.name,t.id,o.orderNum,o.moneySerialNum,vo.create_by, t.payPriceSerialNum");
		buffer.append(" FROM orderpay o");
		buffer.append(" LEFT JOIN traveler t ON o.traveler_id = t.id");
		buffer.append(" LEFT JOIN visa_order vo ON t.orderId=vo.id ");
		buffer.append(" WHERE t.delFlag <> 1 AND o.orderPaySerialNum='");
		buffer.append(serialNum);
		buffer.append("'");
		//buffer.append(" and o.orderId="+visaOrderId);
		List<Map<String,Object>> list =travelerDao.findBySql(buffer.toString(), Map.class);
		
		for (int i = 0; i < list.size(); i++) {
			// 已付
			List<Map<String,Object>> payed_moneyList = orderPayDao
					.findBySql(
							"SELECT ma.amount,c.currency_mark FROM money_amount ma LEFT JOIN currency c  "
									+ "ON ma.currencyId = c.currency_id WHERE ma.serialNum='"
									+ list.get(i).get("moneySerialNum") + "'",
							Map.class);
			String total_payed = ""; // 已付
			for (int j = 0; j < payed_moneyList.size(); j++) {	
				if(j<payed_moneyList.size()-1){
					total_payed = total_payed
							+ payed_moneyList.get(j).get("currency_mark")
							+ payed_moneyList.get(j).get("amount")+" + ";
				}else{
					total_payed = total_payed
							+ payed_moneyList.get(j).get("currency_mark")
							+ payed_moneyList.get(j).get("amount");
				}
			}
			list.get(i).put("payedMoney", total_payed);		
		}
		return list;
	}
	
	/**
	 * 销售身份单办签 签证修改页面保存游客信息
	 * @param traveler
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Long saveOrUpdateTravelerForSalers(Traveler traveler,Visa visa,Long[] currency,String[] cosName,BigDecimal[] sum,String orderId, String inputClearPrice) {
		if(traveler.getId() == null){
			//添加游客日志
//			logOrderService.saveObj("", Long.valueOf(orderId), "添加游客：" + traveler.getName(), 1);

			//保存游客信息
//			String payPriceSerialNum = UUID.randomUUID().toString();
//			traveler.setPayPriceSerialNum(payPriceSerialNum);
			travelerDao.getSession().save(traveler);
			
			//保存签证信息
			visa.setId(null);
			visa.setTravelerId(traveler.getId());
			visaDao.getSession().save(visa);
			
		}else{
			//修改游客日志
			logOrderService.saveLogOrder4Traveler(traveler, orderId, visa, inputClearPrice);

			//更新游客及签证信息
			String hql = "update Traveler set personType=?,name=?,nameSpell=?,sex=?,nationality=?,birthDay=?,telephone=?,passportCode=?,passportValidity=?,idCard=?,passportType=?,remark=? ,issuePlace=?, issuePlace1=? where id=?";
			travelerDao.getSession().createQuery(hql)
				.setParameter(0, traveler.getPapersType())
				.setParameter(1, traveler.getName())
				.setParameter(2, traveler.getNameSpell())
				.setParameter(3, traveler.getSex())
				.setParameter(4, traveler.getNationality())
				.setParameter(5, traveler.getBirthDay())
				.setParameter(6, traveler.getTelephone())
				.setParameter(7, traveler.getPassportCode())
				.setParameter(8, traveler.getPassportValidity())
				.setParameter(9, traveler.getIdCard())
				.setParameter(10, traveler.getPassportType())
				.setParameter(11, traveler.getRemark())
				.setParameter(12, traveler.getIssuePlace())
				.setParameter(13, traveler.getIssuePlace1())
				.setParameter(14, traveler.getId())
				.executeUpdate();
			
			//游客信息修改时,主/子订单同时更新
			if(traveler.getMainOrderTravelerId()!=null && traveler.getMainOrderTravelerId()>0){
				travelerDao.getSession().createQuery(hql)
				.setParameter(0, traveler.getPapersType())
				.setParameter(1, traveler.getName())
				.setParameter(2, traveler.getNameSpell())
				.setParameter(3, traveler.getSex())
				.setParameter(4, traveler.getNationality())
				.setParameter(5, traveler.getBirthDay())
				.setParameter(6, traveler.getTelephone())
				.setParameter(7, traveler.getPassportCode())
				.setParameter(8, traveler.getPassportValidity())
				.setParameter(9, traveler.getIdCard())
				.setParameter(10, traveler.getPassportType())
				.setParameter(11, traveler.getRemark())
				.setParameter(12, traveler.getIssuePlace())
				.setParameter(13, traveler.getMainOrderTravelerId())
				.executeUpdate();
			}
			
			
			String hql2 = "update Visa set passportPhotoId=?,identityFrontPhotoId=?,tablePhotoId=?,personPhotoId=?,identityBackPhotoId=?,otherPhotoId=?,docIds=?,passportOperateRemark=?,familyRegisterPhotoId=?,houseEvidencePhotoId=? where travelerId=?";//,depositValue=?,datumValue=?
			visaDao.getSession().createQuery(hql2)
				.setParameter(0, visa.getPassportPhotoId())
				.setParameter(1, visa.getIdentityFrontPhotoId())
				.setParameter(2, visa.getTablePhotoId())
				.setParameter(3, visa.getPersonPhotoId())
				.setParameter(4, visa.getIdentityBackPhotoId())
				.setParameter(5, visa.getOtherPhotoId())
				.setParameter(6, visa.getDocIds())
				.setParameter(7, traveler.getRemark())
				.setParameter(8, visa.getFamilyRegisterPhotoId())//户口本id
				.setParameter(9, visa.getHouseEvidencePhotoId())//房产证id
				
				// -- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅
//				.setParameter(10, visa.getDepositValue())//是否需要资料
//				.setParameter(11, visa.getDatumValue())//是否上传资料
	
				.setParameter(10, visa.getId())
				.executeUpdate();
			travelerDao.updateBySql("UPDATE visa_order vo SET vo.update_date=? WHERE vo.id=?",new Date(), traveler.getOrderId());
		}
		
		//保存其他费用信息
		//saveOrUpdateCostchangeAndMoneyAmount(traveler, currency, cosName, sum);
		
		//-----wxw added 20150817 游客返佣修改 f7f9c410-a057-4469-a7f0-3f8acfa31152-----
		//System.out.println(traveler.getRebatesAmount()+";"+traveler.getRebatesCurrencyID()+";"+traveler.getRebatesMoneySerialNum());
		if (null!=traveler.getRebatesMoneySerialNum()&&!"".equals(traveler.getRebatesMoneySerialNum())) {
			MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(traveler.getRebatesMoneySerialNum());
			if (null!=moneyAmount) {
				moneyAmount.setAmount(new BigDecimal(traveler.getRebatesAmount().equals("")==true? "0":traveler.getRebatesAmount()));
				moneyAmount.setCurrencyId(Integer.parseInt(traveler.getRebatesCurrencyID()));
				moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
			} else {
				
				MoneyAmount amount = new MoneyAmount();
				amount.setSerialNum(traveler.getRebatesMoneySerialNum());
				amount.setCurrencyId(Integer.parseInt(traveler.getRebatesCurrencyID()));
				if (StringUtils.isNotBlank(traveler.getRebatesAmount())) {
					amount.setAmount(new BigDecimal(traveler.getRebatesAmount()));
				}else{
					amount.setAmount(BigDecimal.ZERO);
				}
				amount.setUid(traveler.getId());
				amount.setMoneyType(23);
				amount.setOrderType(6);
				amount.setBusindessType(2);
				amount.setCreatedBy(UserUtils.getUser().getId());
				amount.setCreateTime(new Date());
				moneyAmountService.saveOrUpdateMoneyAmount(amount);
				
			}
			
		}else{
			Traveler _traveler = findTravelerById(traveler.getId());
			String rebatesMoneySerialNum = UUID.randomUUID().toString();
			_traveler.setRebatesMoneySerialNum(rebatesMoneySerialNum);
			saveTraveler(_traveler);
			
		
			MoneyAmount amount = new MoneyAmount();
			amount.setSerialNum(rebatesMoneySerialNum);
			amount.setCurrencyId(Integer.parseInt(traveler.getRebatesCurrencyID()));
			if (StringUtils.isNotBlank(traveler.getRebatesAmount())) {
				amount.setAmount(new BigDecimal(traveler.getRebatesAmount()));
			}else{
				amount.setAmount(BigDecimal.ZERO);
			}
			amount.setUid(traveler.getId());
			amount.setMoneyType(23);
			amount.setOrderType(6);
			amount.setBusindessType(2);
			amount.setCreatedBy(UserUtils.getUser().getId());
			amount.setCreateTime(new Date());
			moneyAmountService.saveOrUpdateMoneyAmount(amount);
			
		}
		
		
		
		return traveler.getId();
	}
	
	/**
	 * 根据订单团号查询游客信息
	 * @param dingdantuanhao 订单团号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findTravelerByOrderCode(String orderCode, Long agentId) {
		if(null != orderCode && !"".equals(orderCode)){
			if(agentId == null){
				agentId = 0L;
			}
			String sql = "SELECT '' as rowNum,t.name,t.nameSpell,IF(t.sex=1,'男','女') as sex,date_format(t.birthDay,'%Y/%c/%d'),c.countryName_cn,t.personType,";
					sql = sql + "t.idCard,date_format(t.validityDate,'%Y/%c/%d'),'' as visaCity,t.telephone ,a.agentName,a.agentSaler,contacts.remark";
					sql = sql + " from traveler t LEFT JOIN sys_country  c on t.nationality=c.id ";
					sql = sql + "LEFT JOIN (SELECT agentName,agentSaler from agentinfo WHERE agentinfo.id=? ) as a ON 1=1 LEFT JOIN contacts on t.orderId=contacts.orderId left join visa_order vo on vo.id = t.orderId where vo.group_code=? and t.order_type=6";
					
					return travelerDao.createSqlQuery(sql, agentId,orderCode).list();
		}else{
			return null;
		}
	}
	
	/**
	 * 根据游客Id更新游客原始结算价
	 * xinwei.wang added
	 * @param originalPayPriceSerialNum 游客结算价流水号
	 * @param travelerId 游客Id
	 */
	public void updateOriginalPayPriceSerialNumByTravelerId(String originalPayPriceSerialNum, Long travelerId){
		travelerDao.updateOriginalPayPriceSerialNumByTravelerId(originalPayPriceSerialNum, travelerId);
	}
	/**
	 *  保存游客
	 * @param traveler
	 * @return
	 */
	@Transactional(readOnly=false)
	public Traveler  saveTraveler(Traveler traveler){
		Traveler backTraveler = travelerDao.save(traveler);
		return backTraveler;
	}
	
	/**
	 * 通过传入的游客id串(*,*)查询游客
	 * @author jiachen
	 * @DateTime 2015年3月12日 下午2:26:16
	 * @return List<Traveler>
	 */
	public List<Traveler> findByIds(String[] ids) {
		List<Traveler> travelerList= null;
		if(null != ids) {
			List<Long> idList = new ArrayList<Long>(); 
			travelerList = new ArrayList<Traveler>();
			for(String id : ids) {
				idList.add(StringUtils.toLong(id));
			}
			travelerList = travelerDao.findByIds(idList);
		}
		return travelerList;
	}

	/**
	 * 处理游客申请优惠信息
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void handleTravelerApplyDiscountInfo(String oldApplyTravelerIds, String applyInfos) throws Exception {
		try {
			// 清空旧的游客优惠信息
			if(StringUtils.isNotBlank(oldApplyTravelerIds)) {
                String[] oldApplyTravelerIdsArr = oldApplyTravelerIds.split(",");
                List<Traveler> travelers = findByIds(oldApplyTravelerIdsArr);
                for(Traveler traveler : travelers) {
                    traveler.setAppliedDiscountPrice(null);
                }
            }
			// 保存新的游客优惠信息
			if(StringUtils.isNotBlank(applyInfos)) {
                org.json.JSONArray applyInfosJsonArray = new org.json.JSONArray(applyInfos);
                int len = applyInfosJsonArray.length();
                if(len > 0) {
                    for(int i = 0; i < len; i++) {
                        org.json.JSONObject applyInfo = applyInfosJsonArray.getJSONObject(i);
                        String travelerId= applyInfo.getString("travelerId");
                        String applyDiscountPrice = applyInfo.getString("applyDiscountPrice");
                        if(StringUtils.isNotBlank(travelerId) && StringUtils.isNotBlank(applyDiscountPrice)) {
                            Traveler traveler = travelerDao.findById(Long.parseLong(travelerId));
                            traveler.setAppliedDiscountPrice(new BigDecimal(applyDiscountPrice));
                        }
                    }
                }
            }
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("保存游客信息出错!");
		}

	}

	/**
	 * 依据游客id，获取游客对应的结算价 moneyAmountList
	 * @param parseLong
	 * @return
	 */
	public List<MoneyAmount> findTravelerPayPrice(long travelerId) {
		Traveler traveler = travelerDao.findById(travelerId);
		if (traveler != null) {
			String payPriceSerial = traveler.getPayPriceSerialNum();
			if (StringUtils.isNotBlank(payPriceSerial)) {
				List<MoneyAmount> payAmounts = moneyAmountService.getMoneyAmonutListIgnoreDelflag(payPriceSerial);
				return payAmounts;
			}
		}
		return null;
	}
	
	/**
	 * 获取游客总金额（游客结算价 + 服务费），依据游客ID, 批发商ID
	 * 说明：结果可能为空
	 * @param travelerId
	 * @param officeId
	 * @return
	 */
	public List<MoneyAmount> getTravelerTotalWithCharge(Long travelerId, Long officeId) {
		List<MoneyAmount> resultAmounts = new ArrayList<>();
		if (travelerId == null || officeId == null) {			
			return resultAmounts;
		}
		// 游客
		Traveler traveler = travelerDao.findById(travelerId);
		if (traveler == null) {
			return resultAmounts;
		}
		// 游客的结算价
		List<MoneyAmount> travelerPayPriceAmounts = moneyAmountService.findAmountBySerialNum(traveler.getPayPriceSerialNum());
//		List<MoneyAmount> travelerChargeAmounts = getTravelerCharge(travelerId, officeId);
		
//		resultAmounts.addAll(moneyAmountService.calculation4MoneyAmountList(Context.ADD, travelerPayPriceAmounts, travelerChargeAmounts));
		return travelerPayPriceAmounts;
	}
	
}
