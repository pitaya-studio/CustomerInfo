package com.trekiz.admin.modules.activity.service.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.trekiz.admin.agentToOffice.PricingStrategy.entity.AgentPriceStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PriceStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.PriceStrategyDao;
import com.trekiz.admin.agentToOffice.T2.utils.JudgeStringType;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityFile;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.LogProduct;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityFileDao;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.LogProductDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.activity.service.GroupControlBoardService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.utils.TravelActivityUtil;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockGroupRelDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockDetailService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockGroupRelService;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordService;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.order.service.OrderStockService;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.repository.ActivityGroupReserveDao;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DictDao;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.Activityvisafile;
import com.trekiz.admin.modules.visa.service.VisaService;

@Service
public class TravelActivitySyncService extends BaseService implements ITravelActivityService {
	
	@Autowired
	private ActivityGroupReserveDao activityGroupReserveDao;
	
	/**
	 * 日志对象
	 */
	private final static Log logger = LogFactory.getLog(OrderStockService.class);
	
	@Autowired
	@Qualifier("travelActivityService")
	private ITravelActivityService travelActivityService;
	
	@Autowired
	@Qualifier("activityGroupService")
	private IActivityGroupService activityGroupService;

	@Autowired
	private TravelActivityDao travelActivityDao;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private DictDao dictDao;
	
	@Autowired
	private ActivityGroupDao activityGroupDao;
	
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private ActivityFileDao activityFileDao;
	
	@Autowired
	private DocInfoDao docInfoDao;
	
	@Autowired
	private DocInfoService docInfoService;

    @Autowired
    private IActivityAirTicketService iActivityAirTicketService;
    
    @Autowired
    @Qualifier("estimatePriceRecordService")
    private EstimatePriceRecordService estimatePriceRecordService;
    
    @Autowired
    private CruiseshipStockDetailService  cruiseshipStockDetailService;
    
    @Autowired
    private CruiseshipStockGroupRelService  cruiseshipStockGroupRelService;
    
    @Autowired
    private CruiseshipStockGroupRelDao cruiseshipStockGroupRelDao;
    
    @Autowired
    private AreaService areaService;
    
    @Autowired
	private PriceStrategyDao priceStrategyDao;
    
    @Autowired
	private TouristLineService priceStrategyLineService;

	@Autowired
    private LogProductDao logProductDao;

	@Autowired
	private GroupControlBoardService groupControlBoardService;
    
	@Override
	public TravelActivity findById(Long id) {
		return travelActivityService.findById(id);
	}

	@Override
	public List<Map<String, Object>> findAreaIds(Long companyId) {
		return travelActivityService.findAreaIds(companyId);
	}

	//-----------t1t2需求-----------s--//
	@Override
	public List<Map<String, Object>> findAreaIds4T1(Long companyId) {
		return travelActivityService.findAreaIds4T1(companyId);
	}
	//-----------t1t2需求-----------e--//
	
	@Override
	public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page,
			TravelActivity travelActivity, String settlementAdultPriceStart,
			String settlementAdultPriceEnd, DepartmentCommon common) {
		return travelActivityService.findTravelActivity(page, travelActivity, settlementAdultPriceStart, settlementAdultPriceEnd, common);
	}

	@Override
	public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page,
												   TravelActivity travelActivity, String settlementAdultPriceStart,
												   String settlementAdultPriceEnd, DepartmentCommon common, String groundingStatus) {
		return travelActivityService.findTravelActivity(page, travelActivity, settlementAdultPriceStart, settlementAdultPriceEnd, common, groundingStatus);
	}

	@Override
	public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page,
			TravelActivity travelActivity, String settlementAdultPriceStart,
			String settlementAdultPriceEnd, Long agentId, DepartmentCommon common) {
		return travelActivityService.findTravelActivity(page, travelActivity,
				settlementAdultPriceStart, settlementAdultPriceEnd, agentId, common);
	}

	@Override
	public Long findTravelActivitysByCode(String groupCode) {
		return travelActivityService.findTravelActivitysByCode(groupCode);
	}


	@Override
	public List<TravelActivity> findActivity(String activitySerNum, Long proId) {
		return travelActivityService.findActivity(activitySerNum, proId);
	}

	@Override
	public List<TravelActivity> findActivityByCompany(Long companyId, boolean lazy) {
		return travelActivityService.findActivityByCompany(companyId, lazy);
	}
	
	@Override
	public Page<TravelActivity> findActivityByCompany(
			Page<TravelActivity> page, TravelActivity travelActivity) {
		return travelActivityService.findActivityByCompany(page, travelActivity);
	}

	@Override
    public List<TravelActivity> findActivityByCompanyIgnoreDeleteFlag(
            Long companyId) {
	    return travelActivityService.findActivityByCompanyIgnoreDeleteFlag(companyId);
    }
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public synchronized String save(TravelActivity travelActivity, String groupOpenDateBegin, String groupCloseDateEnd, HttpServletRequest request, 
			HttpServletResponse response, Model model, RedirectAttributes redirectAttributes, boolean is_after_supplement) {
		
		int groups = 0;
		
		/** 该产品的最早出团团编码 */
		String groupOpenCode = "";
		/** 该产品的最晚出团团编码 */
		String groupCloseCode = "";
		
		//产品状态
		String activityStatus = request.getParameter("activityStatus");
		//产品种类
		String activitykind = request.getParameter("activityKind");   
		//默认产品类型为单团种类
		if(StringUtils.isBlank(activitykind)) {
			activitykind = "1";
		}
		try {			
			//所有出团产品
			Set<ActivityGroup> activityGroups = new HashSet<ActivityGroup>();
			//产品上传文件
			Set<ActivityFile> activityFiles = new HashSet<ActivityFile>();
			//出团日期
			String[] groupOpenDates = request.getParameterValues("groupOpenDate");
			if(groupOpenDates != null)
				groups = groupOpenDates.length;
			//截团日期
			String[] groupCloseDates = request.getParameterValues("groupCloseDate");
			//应付账期
			String[] yingFuDate = request.getParameterValues("yingFuDate");
			//团号
			String[] groupCodes = request.getParameterValues("groupCode");
			//签证
			String[] visaCountrys = request.getParameterValues("visaCountry");
			//材料截止日期
			String[] visaDates = request.getParameterValues("visaDate");
			//游轮产品舱型
			String[] spaceTypes = request.getParameterValues("spaceType");
			//同业价成人价
			String[] settlementAdultPrices = request.getParameterValues("settlementAdultPrice");
			//同业价儿童
			String[] settlementcChildPrices = request.getParameterValues("settlementcChildPrice");
			//特殊人群最高人数：
			String[] maxPeopleCounts = request.getParameterValues("maxPeopleCount");
			//特殊人群最高人数：
			String[] maxChildrenCounts = request.getParameterValues("maxChildrenCount");
			//同业价特殊人群
			String[] settlementSpecialPrices = request.getParameterValues("settlementSpecialPrice");
//			//trekiz成人价
//			String[] trekizPrices = request.getParameterValues("trekizPrice");
//			//trekiz儿童价
//			String[] trekizChildPrices = request.getParameterValues("trekizChildPrice");
			//建议零售价成人
			String[] suggestAdultPrices = request.getParameterValues("suggestAdultPrice");
			//建议零售价儿童
			String[] suggestChildPrices = request.getParameterValues("suggestChildPrice");
			//建议零售价特殊人群
			String[] suggestSpecialPrices = request.getParameterValues("suggestSpecialPrice");
			//单房差
			String[] singleDiffs = request.getParameterValues("singleDiff");
			//需交订金
			String[] payDeposits = request.getParameterValues("payDeposit");
			//预收人数
			String[] planPositions = request.getParameterValues("planPosition");
			//空位数量
			String[] freePositions = request.getParameterValues("freePosition");
			//价格币种
			String[] currencyTypes = request.getParameterValues("groupCurrencyType");
			//出团通知文件
			String[] openDateFiles = request.getParameterValues("openDateFiles");
			//推荐
			String[] recommends = request.getParameterValues("recommend");
			//团期多价格json
			String[] priceJson = request.getParameterValues("priceJson");
			// 团期酒店
			String[] groupHotels = request.getParameterValues("groupHotelStr");
			// 团期房型
			String[] groupHouseTypes = request.getParameterValues("groupHouseTypeStr");
			
			//c463   添加团期备注信息
			String[] groupremarks = request.getParameterValues("groupRemark");
			
			//223   添加游轮团控
			String[] cruiseGroupControlIds = request.getParameterValues("cruiseGroupControlId");
			
			// 0258   团期添加   发票税
			String[] invoiceTaxds = request.getParameterValues("invoiceTax");
			
		
			//109团期优惠 只有散拼产品才有
			String[] adultDiscountPrices= request.getParameterValues("adultDiscountPrice");
			String[] childDiscountPrices= request.getParameterValues("childDiscountPrice");
			String[] specialDiscountPrices= request.getParameterValues("specialDiscountPrice");
			
			
			
			//同业价quauq成人
			/*String[] quauqAdultPrices = request.getParameterValues("quauqAdultPrice");
			//同业价quauq儿童
			String[] quauqChildPrices = request.getParameterValues("quauqChildPrice");
			//同业价quauq特殊人群
			String[] quauqSpecialPrices = request.getParameterValues("quauqSpecialPrice");*/
			
			
			String[] tempRecommend = new String[groups];
			if (recommends != null && recommends.length > 0) {
				//遍历0到length
				for (int i = 0; i < recommends.length; i++) {
					//如果对应i的值与i相等，存进去
					if (Integer.valueOf(recommends[i]) == i) {
//						tempRecommend[i] = String.valueOf(i+1);
						tempRecommend[i] = "1";
					}
					//如果不相等，把值作为索引存入，
					else {
//						tempRecommend[Integer.valueOf(recommends[i])] = String.valueOf(Integer.valueOf(recommends[i]) + 1);
						tempRecommend[Integer.valueOf(recommends[i])] = "1";
						if (tempRecommend[i] == null) {							
							tempRecommend[i] = "0";
						}
					}
				}
				for (int i = recommends.length; i < groups; i++) {
//					if (tempRecommend[i] == null || Integer.valueOf(tempRecommend[i]) != i+1) {
					if (tempRecommend[i] == null) {
						tempRecommend[i] = "0";
					}
				}
			}else {
				for (int i = 0; i < groups; i++) {
					tempRecommend[i] = "0";
				}
			}
			
			
			
//            //联运类型
//            String intermodalType = request.getParameter("intermodalType");
//            //联运价格
//            String intermodalAllPrice = request.getParameter("intermodalAllPrice");
//            //联运分区
//            String[] intermodalGroupPart = request.getParameterValues("intermodalGroupPart");
//            //联运分区价格
//            String[] intermodalGroupPrice = request.getParameterValues("intermodalGroupPrice");
//            //联运分区价格币种
//            String[] templateGroupCurrency = request.getParameterValues("templateCurrency");

			
			//关联机票产品
			if(StringUtils.isNotBlank(request.getParameter("airTicketId"))) {
				travelActivity.setActivityAirTicket(iActivityAirTicketService.getActivityAirTicketById(StringUtils.toLong(request.getParameter("airTicketId"))));
			}
			
			//根据询价项目设置产品种类
			String estimatePriceRecordId = request.getParameter("estimatePriceRecordId");
			String maxPeopleCount = request.getParameter("maxPeopleCount");
			if(StringUtils.isBlank(maxPeopleCount))
				maxPeopleCount= "0";
			travelActivity.setMaxPeopleCount(Integer.valueOf(maxPeopleCount));
			if(StringUtils.isNotBlank(estimatePriceRecordId)) {
				EstimatePriceRecord estimatePriceRecord = estimatePriceRecordService.findById(StringUtils.toLong(estimatePriceRecordId));
				travelActivity.setEstimatePriceRecord(estimatePriceRecord);
				activitykind = estimatePriceRecord.getType().toString();
				estimatePriceRecordService.releaseProduct(estimatePriceRecord.getId(),"1");
			}

			//如果is_after_supplement为true则表示是补单产品，其余为正常产品
			if(is_after_supplement) {
				travelActivity.setIsAfterSupplement(1);
			} else {
				travelActivity.setIsAfterSupplement(0);
			}
			
			//设置成人的产品最低结算价和建议成人最低零售价
			if(null != settlementAdultPrices || null != suggestAdultPrices) {

				BigDecimal minSettlementAdultPrice = TravelActivityUtil.getMinPrice(settlementAdultPrices,null);
				BigDecimal minSuggestAdultPrice = TravelActivityUtil.getMinPrice(suggestAdultPrices,null);
				travelActivity.setSettlementAdultPrice(minSettlementAdultPrice);
				travelActivity.setSuggestAdultPrice(minSuggestAdultPrice);
				int i = 0;
				int j = 0;
				for (int m = 0;m<settlementAdultPrices.length; m++){
					if (minSettlementAdultPrice.toString().equals(settlementAdultPrices[i])){
						i = m;
					}
				}
				
				// 17276 wangyang 2017.2.6
				if (suggestAdultPrices != null && suggestAdultPrices.length != 0) {
					for (int n = 0; n < suggestAdultPrices.length; n++){
						if (minSuggestAdultPrice.toString().equals(suggestAdultPrices[j])){
							j = n;
						}
					}
				}


				//设置成人的产品最低结算价和建议成人最低零售价币种(目前有bug)
				if(Context.ACTIVITY_KINDS_SP.equals(activitykind)) {
					travelActivity.setCurrencyType(currencyTypes[i].split(",")[0] + "," + currencyTypes[j].split(",")[3]);
				} else if (Context.ACTIVITY_KINDS_YL.equals(activitykind)) {

					travelActivity.setCurrencyType(currencyTypes[i].split(",")[0] + "," + currencyTypes[j].split(",")[2]);

				} else {
					travelActivity.setCurrencyType(currencyTypes[i].split(",")[0]);
				}
			}
			
			//设置产品的最早最晚出团日期
			if(StringUtil.isBlank(groupOpenDateBegin)) {
				if(null != groupOpenDates && groupOpenDates.length > 0 )
					groupOpenDateBegin = groupOpenDates[0];
			}
			travelActivity.setGroupOpenDate(DateUtils.parseDate(groupOpenDateBegin));
			if(StringUtil.isBlank(groupCloseDateEnd)) {
				if(null != groupOpenDates && groupOpenDates.length > 0 ) {
					groupCloseDateEnd = groupOpenDates[groups - 1];
				}
				if(groupCloseDateEnd.equals(groupOpenDateBegin)) {
					travelActivity.setGroupCloseDate(null);
				}else {
					travelActivity.setGroupCloseDate(DateUtils.parseDate(groupCloseDateEnd));
				}
			}else{
				travelActivity.setGroupCloseDate(DateUtils.parseDate(groupCloseDateEnd));
			}

			travelActivity.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
			travelActivity.setProCompany(UserUtils.getUser().getCompany().getId());
			travelActivity.setProCompanyName(UserUtils.getUser().getCompany().getName());
			//除了散拼种类的产品，其他不需要设置产品类型(产品类型已去掉)
//			if(Context.ACTIVITY_KINDS_SP.equals(activitykind))
//				travelActivity.setTravelTypeName(DictUtils.getDicMap(Context.TRAVEL_TYPE).get(travelActivity.getTravelTypeId().toString()));
			
			travelActivity.setActivityStatus(activityStatus==null?null:Integer.parseInt(activityStatus));//产品状态：上架
			//保存产品付款方式，如果付款方式被选择，则标识为1，否则为0
			String payMode[] = request.getParameterValues("payMode");
			travelActivity.setPayMode_deposit(0);
			travelActivity.setPayMode_advance(0);
			travelActivity.setPayMode_full(0);
			travelActivity.setPayMode_op(0);
			travelActivity.setPayMode_data(0);
			travelActivity.setPayMode_guarantee(0);
			travelActivity.setPayMode_express(0);
			travelActivity.setPayMode_cw(0);
			if(payMode != null && payMode.length > 0) {
				for(String mode : payMode) {
					//订金占位：1  预占位：2  全款支付：3 资料占位：4   担保占位:5   确认单占位:6
					if("1".equals(mode)) {
						travelActivity.setPayMode_deposit(1);
					} else if("2".equals(mode)) {
						travelActivity.setPayMode_advance(1);
					} else if("3".equals(mode)) {
						travelActivity.setPayMode_full(1);
					} else if("7".equals(mode)) {
						travelActivity.setPayMode_op(1);
					} else if("4".equals(mode)){
						travelActivity.setPayMode_data(1);
					} else if("5".equals(mode)){
						travelActivity.setPayMode_guarantee(1);
					} else if("6".equals(mode)){
						travelActivity.setPayMode_express(1);
					}
					else if("8".equals(mode)){
						travelActivity.setPayMode_cw(1);
					}
				}
			} else {
				throw new Exception("付款方式没有选择");
			}
			
			int mxVal = Integer.MIN_VALUE;
			List<Dict> dictList = Lists.newArrayList();
			
			//是否自定义产品等级
			String product_level = request.getParameter("product_level");
			if(StringUtils.isNotBlank(product_level) && travelActivity.getActivityLevelId()==null) {
				dictList = dictDao.findByType(Context.PRODUCT_LEVEL);
				for(Dict dict:dictList){
					if(mxVal<Integer.parseInt(dict.getValue()))
						mxVal = Integer.parseInt(dict.getValue());
				}
				Dict dict = dictList.get(0);
				Dict tmp = new Dict();
				tmp.setLabel(product_level);
				tmp.setValue(String.valueOf(mxVal+1));
				tmp.setType(Context.PRODUCT_LEVEL);
				tmp.setDescription(dict.getDescription());
				tmp.setSort(mxVal+1);
				tmp.setDelFlag("0");
				dictDao.save(tmp);
				travelActivity.setActivityLevelId(Integer.parseInt(tmp.getValue()));
				travelActivity.setActivityLevelName(tmp.getLabel());
			}else if(null != travelActivity.getActivityLevelId()){
				travelActivity.setActivityLevelName(DictUtils.getValueAndLabelMap(Context.PRODUCT_LEVEL,StringUtils.toLong(UserUtils.getUser().getCompany().getId())).get(travelActivity.getActivityLevelId().toString()));
			}
			
			mxVal = Integer.MIN_VALUE;
			//是否自定义产品类型
			String product_type = request.getParameter("product_type");
			if(StringUtils.isNotBlank(product_type) && travelActivity.getActivityTypeId()==null) {
				dictList = dictDao.findByType(Context.PRODUCT_TYPE);
				for(Dict dict:dictList){
					if(mxVal<Integer.parseInt(dict.getValue()))
						mxVal = Integer.parseInt(dict.getValue());
				}
				Dict dict = dictList.get(0);
				Dict tmp = new Dict();
				tmp.setLabel(product_type);
				tmp.setValue(String.valueOf(mxVal+1));
				tmp.setType(Context.PRODUCT_TYPE);
				tmp.setDescription(dict.getDescription());
				tmp.setSort(mxVal+1);
				tmp.setDelFlag("0");
				dictDao.save(tmp);
				travelActivity.setActivityTypeId(Integer.parseInt(tmp.getValue()));
				travelActivity.setActivityTypeName(tmp.getLabel());
			} else if(travelActivity.getActivityTypeId() != null){
				travelActivity.setActivityTypeName(DictUtils.getValueAndLabelMap(Context.PRODUCT_TYPE,StringUtils.toLong(UserUtils.getUser().getCompany().getId())).get(travelActivity.getActivityTypeId().toString()));
			}
			
			//旅游类型名称
			if(null != travelActivity.getTravelTypeId()) {
				travelActivity.setTravelTypeName(DictUtils.getValueAndLabelMap(Context.TRAVEL_TYPE,StringUtils.toLong(UserUtils.getUser().getCompany().getId())).get(travelActivity.getTravelTypeId().toString()));
			}
			
			boolean addSuggestPrice = false;
			boolean addSpecialPrice = false;
			if(activitykind != null) {
				travelActivity.setActivityKind(StringUtils.toInteger(activitykind));
				
				//如果发布的是散拼产品，则说明填写了直客价
				if(Context.ACTIVITY_KINDS_SP.equals(activitykind)) {
					addSuggestPrice = true;
				}
				if(Context.ACTIVITY_KINDS_YL.equals(activitykind)) {
					addSpecialPrice = true;
				}
			}
			
			for(int i=0;i<groups;i++){
					
				ActivityGroup group = new ActivityGroup();
				String groupOpenDate = groupOpenDates[i];
				
				String groupCode = groupCodes[i];
				
				String groupCodeBeforeChange = new String(groupCode);
				
				groupCode = filterCtrlChars(groupCode);
				groupCode = groupCode.replace("\\", "\\\\");
				if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")//青岛凯撒72
						|| UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")//大洋87
						|| UserUtils.getUser().getCompany().getUuid().contains("7a81a26b77a811e5bc1e000c29cf2586")//拉美图88
						|| UserUtils.getUser().getCompany().getUuid().contains("7a45838277a811e5bc1e000c29cf2586")//友创国际(大唐国旅)221
						|| UserUtils.getUser().getCompany().getUuid().contains("ed88f3507ba0422b859e6d7e62161b00")//诚品旅游311
						|| UserUtils.getUser().getCompany().getUuid().contains("f5c8969ee6b845bcbeb5c2b40bac3a23")//懿洋假期
						|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")//非常国际
						|| UserUtils.getUser().getCompany().getUuid().contains("7a81c5d777a811e5bc1e000c29cf2586")//优加国际
						|| UserUtils.getUser().getCompany().getUuid().contains("5c05dfc65cd24c239cd1528e03965021")//起航假期
						|| UserUtils.getUser().getCompany().getGroupCodeRuleDT()==0){ //对应需求号  c460 
					
					if(activityGroupService.groupNoCheck(groupCode)){
						return "modules/activity/activityDuplicatedGroupcode";
					}
					groupCode = groupCodeBeforeChange;
				}
				//处理团号重复问题，如果用户在同一时间进行了提交保存，但是必须保证团号不可重复
				//获取数据库中团号累加值的最大值
				Long sequence = activityGroupService.getMaxCountForSequence(Integer.valueOf(groupOpenDate.substring(0, 4)));
				if(null != sequence) {
					Integer trueValue = sequence.intValue();
					int groupCodeLength = groupCodes[0].split("-").length;
					String[] groupCodeArr = groupCode.split("-");
					//替换累加值
					//环球行带有TTS开头的团号
					if(5 == groupCodeLength) {
						groupCodeArr[3] = activityGroupService.getZeroCode(String.valueOf(trueValue), 4);
					//目前环球行正在使用的团号
					}else if(3 == groupCodeLength) {
						groupCodeArr[1] = activityGroupService.getZeroCode(String.valueOf(trueValue), 4);
					//新行者的团号
					}else if(4 == groupCodeLength) {
						groupCodeArr[3] = activityGroupService.getZeroCode(String.valueOf(trueValue), 4);
					}
					groupCode = StringUtils.join(groupCodeArr,"-");
				}
				
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
				//处理最早及最晚出团日期所在团团号
				if((groupOpenDateBegin!=null && groupOpenDateBegin.equals(groupOpenDate)) || groupOpenDate.equals(df.format(TravelActivityUtil.getMinDate(groupOpenDates,null)))){
					groupOpenCode = groupCode;
				}
				if(groupCloseDateEnd!=null && groupCloseDateEnd.equals(groupOpenDate)){
					groupCloseCode = groupCode;
				}
				String groupCloseDate = groupCloseDates[i];
				if (StringUtils.isBlank(groupCloseDate)) {
					groupCloseDate = groupOpenDate;
				}
				
				String yFuDate = yingFuDate[i];
				
				String visaCountry = visaCountrys[i];
				String visaDate = visaDates[i];
				if(addSpecialPrice) {
					//页面上有一个隐藏的模板select，需要跳过。
					String spaceType = spaceTypes[i + 1];
					group.setSpaceType(StringUtils.toLong(spaceType));
				}
				String settlementAdultPrice = settlementAdultPrices[i];
				String settlementcChildPrice = settlementcChildPrices[i];
				String maxPeopleCountsi="0";
				String maxChildrenCountsi="0";
				if(maxPeopleCounts != null)
					maxPeopleCountsi= maxPeopleCounts[i];
				if(maxChildrenCounts != null)
					maxChildrenCountsi= maxChildrenCounts[i];
				String settlementSpecialPrice = "";
				if(!addSpecialPrice) {
					settlementSpecialPrice = settlementSpecialPrices[i];
				}
				String currencyType = currencyTypes[i];
				String openDateFile = openDateFiles[i];
//				String trekizPrice = trekizPrices[i];
//				String trekizChildPrice = trekizChildPrices[i];
				String suggestAdultPrice = "";
				String suggestChildPrice = "";
				String suggestSpecialPrice = "";
				if(addSuggestPrice || addSpecialPrice) {
					suggestAdultPrice = suggestAdultPrices[i];
					suggestChildPrice = suggestChildPrices[i];
					if(!addSpecialPrice) {
						suggestSpecialPrice = suggestSpecialPrices[i];
					}
				}
				String singleDiff = singleDiffs[i];
				String payDeposit = payDeposits[i];
				String planPosition = planPositions[i];
				String freePosition = freePositions[i];

				String adultDiscountPrice= null;
				String childDiscountPrice= null;
				String specialDiscountPrice= null;
				if(adultDiscountPrices != null ||childDiscountPrices != null ||specialDiscountPrices != null  )
				{
					 adultDiscountPrice= adultDiscountPrices[i];
					 childDiscountPrice= childDiscountPrices[i];
					 specialDiscountPrice= specialDiscountPrices[i];
				}
				
				//c463  添加 备注信息  20160111
				String groupremark = groupremarks[i];
				group.setGroupRemark(groupremark);
				
				
				//0258  添加   发票税 
				if(null!=invoiceTaxds){
					String invoiceTax = invoiceTaxds[i];
					if (StringUtils.isNoneEmpty(invoiceTax)) {
						group.setInvoiceTax(new BigDecimal(invoiceTax));
					}else{
						group.setInvoiceTax(null);
					}
				}
				
				
				if (priceJson != null && priceJson.length > i) {
					String priceStr = priceJson[i];
					group.setPriceJson(priceStr);
				}
				
				// 团期酒店
				if (groupHotels != null && groupHotels.length > i) {
					String groupHotel = groupHotels[i];
					group.setGroupHotel(groupHotel);
				}
				// 团期房型
				if (groupHouseTypes != null && groupHouseTypes.length > i) {
					String groupHouseType = groupHouseTypes[i];
					group.setGroupHouseType(groupHouseType);
				}
				
				//223  添加游轮团控关系  20160307
//				System.out.println(null == cruiseGroupControlIds);
				if (null!=cruiseGroupControlIds) {
					String cruiseshipStockDetailId = cruiseGroupControlIds[i]; //cruiseship_stock_detail 表Id
					if (StringUtils.isNotBlank(cruiseshipStockDetailId)) {
						group.setCruiseshipStockDetailId(Integer.valueOf(cruiseshipStockDetailId));
					}
			    }
				
				
				group.setGroupOpenDate(DateUtils.parseDate(groupOpenDate));
				group.setGroupCloseDate(DateUtils.parseDate(groupCloseDate));
				group.setVisaCountry(visaCountry);
				group.setPayableDate(DateUtils.parseDate(yFuDate));
				group.setVisaDate(DateUtils.parseDate(visaDate));
				if(StringUtils.isNotBlank(settlementAdultPrice)) {
					group.setSettlementAdultPrice(getBigDecimal(settlementAdultPrice));
				}
				if(StringUtils.isNotBlank(settlementcChildPrice)) {
					
					group.setSettlementcChildPrice(getBigDecimal(settlementcChildPrice));
				}
				if(StringUtils.isNotBlank(maxPeopleCountsi)) {
									
									group.setMaxPeopleCount(Integer.valueOf(maxPeopleCountsi));
								}
				if(StringUtils.isNotBlank(maxChildrenCountsi)) {
					
					group.setMaxChildrenCount(Integer.valueOf(maxChildrenCountsi));
				}
				
				
				if(StringUtils.isNotBlank(settlementSpecialPrice)) {
					
					group.setSettlementSpecialPrice(getBigDecimal(settlementSpecialPrice));
				}
//				group.setTrekizPrice(StringNumFormat.getIntegerValue(trekizPrice));
//				group.setTrekizChildPrice(StringNumFormat.getIntegerValue(trekizChildPrice));
				if(addSuggestPrice || addSpecialPrice) {
					if(StringUtils.isNotBlank(suggestAdultPrice)) {
						group.setSuggestAdultPrice(getBigDecimal(suggestAdultPrice));
					}
					if(StringUtils.isNotBlank(suggestChildPrice)) {
						group.setSuggestChildPrice(getBigDecimal(suggestChildPrice));
					}
					if(StringUtils.isNotBlank(suggestSpecialPrice)) {
						group.setSuggestSpecialPrice(getBigDecimal(suggestSpecialPrice));
					}
				}
				
				// t1t2 打通   产品发布   对   新增quauq  价的保存  ------------ 开始 -------------
				
				/*
				 * 对应需求号  0426 t1t2 打通  
				 * 同业价quauq成人
				 */
				/*if(null!=quauqAdultPrices){
					String quauqAdultPrice = quauqAdultPrices[i];
					if(StringUtils.isNotBlank(quauqAdultPrice)) {
						group.setQuauqAdultPrice(getBigDecimal(quauqAdultPrice));
					}
					
				}*/
				
				/*
				 * 对应需求号  0426 t1t2 打通  
				 * 同业价quauq儿童
				 */
				/*if(null!=quauqChildPrices){
					String quauqChildPrice = quauqChildPrices[i];
					if(StringUtils.isNotBlank(quauqChildPrice)) {
						group.setQuauqChildPrice(getBigDecimal(quauqChildPrice));
					}
				}*/
				
				/*
				 * 对应需求号  0426 t1t2 打通  
				 * 同业价quauq特殊人群
				 */
				/*if(null!=quauqSpecialPrices){
					String quauqSpecialPrice = quauqSpecialPrices[i];
					if(StringUtils.isNotBlank(quauqSpecialPrice)) {
						group.setQuauqSpecialPrice(getBigDecimal(quauqSpecialPrice));
					}
				}*/
				// t1t2 打通  对修改时新增quauq  价的保存  ------------ 开始 -------------
				
				
				if(StringUtils.isNotBlank(singleDiff)) {
					group.setSingleDiff(getBigDecimal(singleDiff));
				}
				//单团产品才有此属性
				//if($("#companyUUID").val()=='7a81c5d777a811e5bc1e000c29cf2586' 
				// || $("#companyUUID").val()=='75895555346a4db9a96ba9237eae96a5')

				if("2".equals(travelActivity.getActivityKind().toString()) 
						|| UserUtils.getUser().getCompany().getUuid().equals("7a81c5d777a811e5bc1e000c29cf2586")
						|| UserUtils.getUser().getCompany().getUuid().equals("75895555346a4db9a96ba9237eae96a5")
						)
				{
					if(StringUtils.isNotBlank(adultDiscountPrice)) {
						group.setAdultDiscountPrice(getBigDecimal(adultDiscountPrice));
					}
					if(StringUtils.isNotBlank(childDiscountPrice)) {
						group.setChildDiscountPrice(getBigDecimal(childDiscountPrice));
					}
					if(StringUtils.isNotBlank(specialDiscountPrice)) {
						group.setSpecialDiscountPrice(getBigDecimal(specialDiscountPrice));
					}
				}


				if(StringUtils.isNotBlank(payDeposit)) {
					group.setPayDeposit(getBigDecimal(payDeposit));
				}
				group.setPlanPosition(StringNumFormat.getIntegerValue(planPosition));
				group.setFreePosition(StringNumFormat.getIntegerValue(freePosition));
				group.setCurrencyType(currencyType);
				if(StringUtils.isNotBlank(openDateFile)) {
					group.setOpenDateFile(StringUtils.toLong(openDateFile));
				}
				//设置推荐flag
				group.setRecommend(Integer.valueOf(tempRecommend[i]));
				
				group.setTravelActivity(travelActivity);
				group.setVersionNumber(StringUtils.getVersionNumber(request));
				group.setGroupCode(groupCode);
				activityGroups.add(group);
				
				// 添加日期
				logger.info("添加团期：groupCode=" + group.getGroupCode() + "；预收：" + group.getPlanPosition() + "；余位为：" + group.getFreePosition());
			}
		        
			//产品行程介绍
			ActivityFile activityFile;
		    String[] introductionArr = request.getParameterValues("introduction");
		    if(introductionArr != null) {
		    	String introduction_name = request.getParameter("introduction_name");
		    	for(int i = 0; i < introductionArr.length; i++) {
		    		activityFile = new ActivityFile();
		    		activityFile.setFileType(ActivityFile.INTRODUCTION_TYPE);
		    		activityFile.setFileName(introduction_name);
		    		activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(introductionArr[i])));
		    		activityFile.setTravelActivity(travelActivity);
		    		activityFiles.add(activityFile);
		    	}
		    }
		    
				
			//自费补充协议
		    String[] costagreementArr = request.getParameterValues("costagreement");
		    if(costagreementArr != null) {
		    	String costagreement_name = request.getParameter("costagreement_name");
		    	for(int i = 0; i < costagreementArr.length; i++) {
		    		activityFile = new ActivityFile();
		    		activityFile.setFileType(ActivityFile.COSTAGREEMENT_TYPE);
		    		activityFile.setFileName(costagreement_name);
		    		activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(costagreementArr[i])));
		    		activityFile.setTravelActivity(travelActivity);
		    		activityFiles.add(activityFile);
		    	}
		    }
				
			//其他补充协议
			String[] otheragreementArr = request.getParameterValues("otheragreement");
		    if(otheragreementArr != null) {
		    	String otheragreement_name = request.getParameter("otheragreement_name");
		    	for(int i = 0; i < otheragreementArr.length; i++) {
		    		activityFile = new ActivityFile();
		    		activityFile.setFileType(ActivityFile.OTHERAGREEMENT_TYPE);
		    		activityFile.setFileName(otheragreement_name);
		    		activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(otheragreementArr[i])));
		    		activityFile.setTravelActivity(travelActivity);
		    		activityFiles.add(activityFile);
		    	}
		    }
			
			//其他文件
			String[] otherFileArr = request.getParameterValues("otherfile");
			if(otherFileArr != null) {
				String otherfile_name = request.getParameter("otherfile_name");
		    	for(int i = 0; i < otherFileArr.length; i++) {
		    		activityFile = new ActivityFile();
		    		activityFile.setFileType(ActivityFile.OTHER_TYPE);
		    		activityFile.setFileName(otherfile_name);
		    		activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(otherFileArr[i])));
		    		activityFile.setTravelActivity(travelActivity);
		    		activityFiles.add(activityFile);
		    	}
			}
			
			/**
			  QU-SDP-微信分销模块 散拼产品追加上传图片按钮 start yang.gao 2017.01.06
			 */
			// 如果是批发商计调则返回true
			boolean requiredStraightPrice = SecurityUtils.getSubject().isPermitted("looseProduct:operation:requiredStraightPrice");
			// 只有拼机并且拥有批发商计调权限才有微信图片上传功能
			if (Context.ACTIVITY_KINDS_SP.equals(activitykind) && requiredStraightPrice) {
				String docId = request.getParameter("docId");
				DocInfo docinfo = docInfoDao.findOne(StringUtils.toLong(docId));
                int docIndex = docinfo.getDocName().lastIndexOf('.'); 
                String fileName = docinfo.getDocName().substring(0, docIndex); // 文件名称
	    		activityFile = new ActivityFile();
	    		activityFile.setFileType(ActivityFile.DISTRIBUTION_TYPE);
	    		activityFile.setFileName(fileName);
	    		activityFile.setDocInfo(docinfo);
	    		activityFile.setTravelActivity(travelActivity);
	    		activityFiles.add(activityFile);
				// 微信分销广告图片
//				String[] distributionAdImgArr = request.getParameterValues("distributionAdImg");
//				if(distributionAdImgArr != null) {
//					String distributionAdImg_name = request.getParameter("distributionAdImg_name");
//			    	for(int i = 0; i < distributionAdImgArr.length; i++) {
//			    		activityFile = new ActivityFile();
//			    		activityFile.setFileType(ActivityFile.DISTRIBUTION_TYPE);
//			    		activityFile.setFileName(distributionAdImg_name);
//			    		activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(distributionAdImgArr[i])));
//			    		activityFile.setTravelActivity(travelActivity);
//			    		activityFiles.add(activityFile);
//			    	}
//			    // 如果没有上传就上传默认图片
//				}
			}
			// QU-SDP-微信分销模块 散拼产品追加上传图片按钮 end yang.gao 2017.01.06
			
			travelActivity.setActivityGroups(activityGroups);
			travelActivity.setActivityFiles(activityFiles);
			
			//产品的最早及最晚出团团编码
			travelActivity.setGroupOpenCode(groupOpenCode);
			travelActivity.setGroupCloseCode(groupCloseCode);
			
			//新增
			travelActivity.setIsSyncSuccess(TravelActivity.IS_ADD_SYNC_SUCCESS);
			
			//名扬验证是否有重复团号
			if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
				//保存前查询数据库是否有重复团号，如重复则循环+1直到没有重复为止
				Iterator<ActivityGroup> iterator=travelActivity.getActivityGroups().iterator();
				while(iterator.hasNext()){
					ActivityGroup group = iterator.next();
					group.setGroupCode(activityGroupService.groupCodeCheck(group.getGroupCode()));
				}
			}
			travelActivity = travelActivityDao.save(travelActivity);
			
			/**
			 * QU-SDP-微信分销模块 散拼产品追加上传图片按钮 如果没有上传就默认上传 start yang.gao 2017.01.06
			 */
			// 如果没有上传分销广告图片就设置默认上传的广告图片
//			if (Context.ACTIVITY_KINDS_SP.equals(activitykind) && requiredStraightPrice) {
//				// 微信分销广告图片
//				String[] distributionAdImgArr = request.getParameterValues("distributionAdImg");
//				if(distributionAdImgArr == null) {
//	                DocInfo doc = uploadDefaultFile(request, Context.DEFAULT_IMG_PATH);
//	                int docIndex = doc.getDocName().lastIndexOf('.'); 
//	                String fileName = doc.getDocName().substring(0, docIndex); // 文件名称
//		    		// 产品和文件中间表保存
//	                activityFile = new ActivityFile();
//		    		activityFile.setFileType(ActivityFile.DISTRIBUTION_TYPE);
//		    		activityFile.setFileName(fileName); // 文件名称
//		    		activityFile.setDocInfo(doc);
//		    		activityFile.setDelFlag("0");
//		    		activityFile.setTravelActivity(travelActivity);
//		    		activityFileDao.save(activityFile);
//				}
//			}
			// QU-SDP-微信分销模块 散拼产品追加上传图片按钮 如果没有上传就默认上传 end yang.gao 2017.01.06
			
			saveVisaFile4Save(travelActivity, request);
			
			/**
			 * ------------------ wxw added -----------------
			 * 
			 * 需求号    223 
			 * 
			 * 产品发布   保存后    可获取团期id
			 * 保存  团期的  团控关联表
			 * 
			 */
			if (null!=cruiseGroupControlIds) {
				Set<ActivityGroup> activityGroupset = travelActivity.getActivityGroups();
				for (ActivityGroup activityGroup : activityGroupset) {
					Integer cruiseshipStockDetailId = activityGroup.getCruiseshipStockDetailId();
					if (null!=cruiseshipStockDetailId) {
						CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailService.getById(cruiseshipStockDetailId);
						CruiseshipStockGroupRel  cruiseshipStockGroupRel = new CruiseshipStockGroupRel();
						cruiseshipStockGroupRel.setActivityId(travelActivity.getId().intValue());
						cruiseshipStockGroupRel.setActivitygroupId(Integer.valueOf(activityGroup.getId().toString()));
						cruiseshipStockGroupRel.setActivityType(1); //  1：activitygroup表；
						cruiseshipStockGroupRel.setCruiseshipStockUuid(cruiseshipStockDetail.getCruiseshipStockUuid());
						cruiseshipStockGroupRel.setStatus(0); //0:表示已经关联
						
						String companyId  = UserUtils.getUser().getCompany().getId().toString();
						cruiseshipStockGroupRel.setWholesalerId(Integer.valueOf(companyId));
						
						
						cruiseshipStockGroupRel.setCruiseshipStockDetailId(activityGroup.getCruiseshipStockDetailId());
						
						String userId = UserUtils.getUser().getId().toString();
						cruiseshipStockGroupRel.setCreateBy(Integer.valueOf(userId));				
						cruiseshipStockGroupRel.setCreateDate(new Date());
						
						cruiseshipStockGroupRel.setUpdateBy(Integer.valueOf(userId));
						cruiseshipStockGroupRel.setUpdateDate(new Date());
						
						cruiseshipStockGroupRelService.save(cruiseshipStockGroupRel);
						
					}				
				}
			}
			
			
			
//            int partSize = intermodalGroupPart.length;
//            int priceSize = intermodalGroupPrice.length;
//            if(partSize == priceSize && partSize > 0 && "2".equals(intermodalType)){
//                for(int i = 0; i < partSize; i++){
//                    IntermodalStrategy intermodalStrategy = new IntermodalStrategy();
//                    intermodalStrategy.setTravelActivity(travelActivity);
//                    intermodalStrategy.setType(Integer.valueOf(intermodalType));
//                    intermodalStrategy.setGroupPart(intermodalGroupPart[i]);
//                    intermodalStrategy.setPrice(new BigDecimal(StringUtils.isNotEmpty(intermodalGroupPrice[i]) ? Long.valueOf(intermodalGroupPrice[i]) : 0L));
//                    intermodalStrategy.setPriceCurrency(currencyService.findCurrency(Long.valueOf(templateGroupCurrency[i])));
//                    intermodalStrategyService.save(intermodalStrategy);
//                }
//            }else if("1".equals(intermodalType)){
//                IntermodalStrategy intermodalStrategy = new IntermodalStrategy();
//                intermodalStrategy.setTravelActivity(travelActivity);
//                intermodalStrategy.setType(Integer.valueOf(intermodalType));
//                intermodalStrategy.setGroupPart("全国");
//                intermodalStrategy.setPrice(new BigDecimal(StringUtils.isNotEmpty(intermodalAllPrice) ? Long.valueOf(intermodalAllPrice) : 0L));
//                intermodalStrategy.setPriceCurrency(currencyService.findCurrency(Long.valueOf(templateGroupCurrency[0])));
//                intermodalStrategyService.save(intermodalStrategy);
//            }

		} catch (ServiceException serviceException) {
//			model.addAttribute("content", serviceException.getMessage());
//			model.addAttribute("type", "error");
			//throw new RuntimeException(serviceException.getMessage());
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "出现异常" + serviceException.getMessage());
			return Context.ERROR_PAGE;
		} catch (Exception exception){
//			model.addAttribute("content", exception.getMessage());
//			model.addAttribute("type", "error");
			//throw new RuntimeException(exception.getMessage());
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "出现异常" + exception.getMessage());
			return Context.ERROR_PAGE;
		} 
		if(StringUtils.isNotBlank(activityStatus)){
			return "redirect:" + Global.getAdminPath() + "/activity/manager/list/" + activityStatus + "/" + activitykind;
		}else			
			return "redirect:" + Global.getAdminPath() + "/activity/manager";
	}
	
	/**
	 * 上传默认图片
	 * @author gaoyang
	 * @DateTime 2017年1月9日 下午20:16:12
	 * @return BigDecimal
	 */
	private DocInfo uploadDefaultFile(HttpServletRequest request, String imgPath) {
		// 项目根目录
		String realPath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fileInputStream = null;
		File file = new File(realPath + imgPath);
		byte[] bFile = null;
		DocInfo doc = new DocInfo();
		try {
			bFile = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
			String fileNameFull = file.getName();
			int docIndex = fileNameFull.lastIndexOf('.'); 
			String pfix= fileNameFull.substring(docIndex+1); // 文件后缀
	        // 使用唯一标识码生成文件名
	        String newName = UUID.randomUUID().toString() + "." + pfix;
	        File uploadFile = null;
			StringBuilder sb = new StringBuilder();
			sb.append(FileUtils.getUploadFilePath().get(1));
			uploadFile = new File(sb.toString());
			if (!uploadFile.exists()) {
				uploadFile.mkdirs();
			}
			uploadFile = new File(sb.toString(), newName);
			FileCopyUtils.copy(bFile, uploadFile);
	        // 保存到DocInfo
	        String docPath = FileUtils.getUploadFilePath().get(0) + newName;
	        doc.setDocName(fileNameFull);
	        doc.setDocPath(docPath);
	        Long docId = docInfoService.saveDocInfo(doc).getId();
	        doc.setId(docId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
        return doc;
	}

	/**
	 * Object转BigDecimal
	 * @author jiachen
	 * @DateTime 2015年1月6日 下午9:16:12
	 * @return BigDecimal
	 */
	private BigDecimal getBigDecimal(Object o) {
		if(null == o) {
			return null;
		}else if(StringUtils.isNotBlank(o.toString())){
			return new BigDecimal(o.toString());
		}else{
			return new BigDecimal(0);
		}
	}
	
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delActivity(TravelActivity activity){
		activity.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		travelActivityDao.save(activity);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void batchDelActivity(List<Long> ids) {
		if(ids == null || ids.isEmpty()){
			throw new IllegalArgumentException("产品ids为空");
		}
		for(Long id : ids){
			TravelActivity activity = findById(id);
			try {
				this.delActivity(activity);
			} catch (Exception e) {
				throw new RuntimeException("删除产品失败");
			}
		}
//		travelActivityService.batchDelActivity(ids);
	}


	@Override
	@Transactional(rollbackFor=Exception.class)
	public void batchOnOrOffActivity(List<Long> ids, Integer satus) {
		if(ids == null || ids.isEmpty()){
			throw new IllegalArgumentException("产品ids为空");
		}
		String statusStr = "";
		//正向平台产品状态：草稿为1；上架为2；下架为3
		//同步接口上架为1；下架为2
		if(satus == Integer.parseInt(Context.PRODUCT_ONLINE_STATUS)) {
			statusStr = "上架";
		} else {
			statusStr = "下架";
		}
		Set<TravelActivity> activitSet = new HashSet<TravelActivity>();
		for(Long id : ids) {
			TravelActivity activity = this.findById(id);
			activitSet.add(activity);
		}
		
		//批量保存到数据库和同步
		for(TravelActivity activity : activitSet) {
			try {
				activity.setActivityStatus(satus);
				this.save(activity);
			} catch (Exception e) {
				throw new RuntimeException("批量" + statusStr + "产品失败");
			}
		}
	}


	@Override
    public Boolean updateTravelActivityWithSyncStatus(Long activityId,
            Integer status) {
		return travelActivityService.updateTravelActivityWithSyncStatus(activityId, status);
    }

	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public TravelActivity save(TravelActivity travelActivity) {
		return travelActivityService.save(travelActivity);
	}

	/**
	 * 产品修改
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public synchronized String modSave(String groupdata, TravelActivity travelActivity,
			HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws Exception {
//        //联运类型
//        String intermodalType = request.getParameter("intermodalType");
//        //联运价格
//        String intermodalAllPrice = request.getParameter("intermodalAllPrice");
//        //联运分区
//        String[] intermodalGroupPart = request.getParameterValues("intermodalGroupPart");
//        //联运分区价格
//        String[] intermodalGroupPrice = request.getParameterValues("intermodalGroupPrice");

		//如果旅游类型、交通方式、产品系列、产品类型为空，则抛出异常
        //由于存在单团产品，不能保证下述全部必填项，所以取消验证
//		if(travelActivity.getTravelTypeId() == null || travelActivity.getTrafficMode() == null ||
//		        travelActivity.getActivityLevelId() == null || travelActivity.getActivityTypeId() == null) {
//		    throw new RuntimeException("存在必填信息为空");
//		}
		//根据产品ID查询产品并赋值
	    String proId = request.getParameter("proId"); 
		//出团通知文件，目前只能满足产品只有一个团期的操作，需后期修改！！！
		String openDateFiles = request.getParameter("openDateFile");
	    TravelActivity activity = travelActivityService.findById(Long.parseLong(proId));
	    //把数据库产品对象放入新的产品对象用于产品同步
	    TravelActivity source = new TravelActivity();
	    //把数据库对象赋值给一个新建产品对象用于正反向产品同步时对比
	    BeanUtils.copyProperties(activity, source);
	    Set<ActivityGroup> set = new HashSet<ActivityGroup>();
	    if(activity.getActivityGroups() != null) {
	    	source.getActivityGroups().clear();
	    	for(ActivityGroup group : activity.getActivityGroups()) {
	    		//设置单个团期的出团通知
	    		if(StringUtils.isNotBlank(openDateFiles)) {
	    			group.setOpenDateFile(StringUtils.toLong(openDateFiles));
	    		}
	    		ActivityGroup tempGroup = new ActivityGroup();
	    		BeanUtils.copyProperties(group, tempGroup);
		    	set.add(tempGroup);
		    }
	    } 
	    source.setActivityGroups(set);
	    Set<ActivityFile> sourceFileSet = new HashSet<ActivityFile>();
	    if(activity.getActivityFiles() != null) {
	    	for(ActivityFile file : activity.getActivityFiles()) {
	    		Hibernate.initialize(file);
	    		ActivityFile tempFile = new ActivityFile();
	    		BeanUtils.copyProperties(file, tempFile);
	    		sourceFileSet.add(tempFile);
	    	}
	    } 
	    source.setActivityFiles(sourceFileSet);
	    
	    //给数据库产品对象赋值
		//setActivity(activity, travelActivity, request);
		
		//自定义产品等级、产品类型
		setCustomer(activity, travelActivity, request);
		
		
		/**
		 * 对应需求号 223  只能在这里写，移到下面会有问题
		 * 获取产品 原有  团期的 IDS
		 */
		List<Long> activityGroupIds = new ArrayList<>();
		Set<ActivityGroup> activityGroupsetOld = activity.getActivityGroups();
		for (ActivityGroup activityGroup : activityGroupsetOld) {
			if (null!=activityGroup.getId()) {
				activityGroupIds.add(activityGroup.getId());
			}
		}
		
		
		//处理产品团期：添加
		String url = addGroup(activity, request,model);
		//给数据库产品对象赋值
		setActivity(activity, travelActivity, request);
		 List<ActivityGroup> list  = new ArrayList<ActivityGroup>(activity.getActivityGroups());
		 if(list.size()>0)
		 {
			 getSortList(list);
			 activity.setGroupOpenCode(list.get(list.size()-1).getGroupCode());
			 activity.setGroupCloseCode(list.get(0).getGroupCode());
			 travelActivityDao.updateObj(activity);
		 }
		
		//同时提交时，如团号重复，则后提交的跳到重复提示界面
		if(StringUtils.isNotBlank(url)){
			return url;
		}
		
		//保存修改文件
		saveFile(activity,request);
		
		// 需求号    223 
		/**
		 * 获取产品 原有  团期的 IDS
		 */
		/*List<Long> activityGroupIds = new ArrayList<>();
		Set<ActivityGroup> activityGroupsetOld = activity.getActivityGroups();
		for (ActivityGroup activityGroup : activityGroupsetOld) {
			if (null!=activityGroup.getId()) {
				activityGroupIds.add(activityGroup.getId());
			}
		}*/
		
		//产品保存并同步到正向平台
		travelActivity = travelActivityDao.save(activity);

		//t1t2-v4 0518如果修改了产品团期的同行价或直客价后，设置定价策略状态
		setPricingStrategyStatus(source, activity, request);
		
		//如果团期余位发生改变，则记录到日志中，增加t1t2-v2记录同行价、直客价修改记录，保存到log_product表中
	    setFreeLog(source, activity);
		

		/**
		 * 需求号    223 
		 * 此处只保存   修改时新增的  团期， 通过原有团期  已有Id 来进行判断
		 * 
		 * 原有团期在   addGroup 方法中保存
		 * 
		 */
		Set<ActivityGroup> activityGroupsetNEW = travelActivity.getActivityGroups();
		for (ActivityGroup activityGroup : activityGroupsetNEW) {
			if (!activityGroupIds.contains(activityGroup.getId())) {
				
				Integer cruiseshipStockDetailId = activityGroup.getCruiseshipStockDetailId();
				if (null!=cruiseshipStockDetailId) {
					CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailService.getById(cruiseshipStockDetailId);
					CruiseshipStockGroupRel  cruiseshipStockGroupRel = new CruiseshipStockGroupRel();
					cruiseshipStockGroupRel.setActivityId(travelActivity.getId().intValue());
					cruiseshipStockGroupRel.setActivitygroupId(Integer.valueOf(activityGroup.getId().toString()));
					cruiseshipStockGroupRel.setActivityType(1); //  1：activitygroup表；
					cruiseshipStockGroupRel.setCruiseshipStockUuid(cruiseshipStockDetail.getCruiseshipStockUuid());
					cruiseshipStockGroupRel.setStatus(0); //0:表示已经关联
					
					String companyId  = UserUtils.getUser().getCompany().getId().toString();
					cruiseshipStockGroupRel.setWholesalerId(Integer.valueOf(companyId));
					
					
					cruiseshipStockGroupRel.setCruiseshipStockDetailId(activityGroup.getCruiseshipStockDetailId());
					
					String userId = UserUtils.getUser().getId().toString();
					cruiseshipStockGroupRel.setCreateBy(Integer.valueOf(userId));				
					cruiseshipStockGroupRel.setCreateDate(new Date());
					
					cruiseshipStockGroupRel.setUpdateBy(Integer.valueOf(userId));
					cruiseshipStockGroupRel.setUpdateDate(new Date());
					
					cruiseshipStockGroupRelService.save(cruiseshipStockGroupRel);
					
				}
			}
		}
		
		
		
		//删除其他文件和团期
		deleteFileAndGroup(activity, request);
		
		//修改签证信息
		saveVisaFile(travelActivity, request);
			
		if (travelActivity.getActivityStatus() != null) {
			//t1t2-v2修改产品提交保存不跳转到产品列表页面
//			if(travelActivity.getActivityKind().intValue() == Integer.parseInt(Context.PRODUCT_TYPE_SAN_PIN)) {
//				return "redirect:" + Global.getAdminPath() + "/activity/manager/mod/" + travelActivity.getId() + "/1";
//			}
//			else {
				return "redirect:" + Global.getAdminPath() + "/activity/manager/list/" + travelActivity.getActivityStatus() + "/" + request.getParameter("kind");
//			}
		} else {
			return "redirect:"+Global.getAdminPath()+"/activity/manager" + "/" + request.getParameter("kind");
		}		
	}

	/**
	 * 如果修改了产品团期的同行价或直客价后，设置定价策略状态
	 * @param source 修改前产品对象
	 * @param activity 修改后产品对象
	 * @param request
     */
	public void setPricingStrategyStatus(TravelActivity source, TravelActivity activity,HttpServletRequest request) {
		Set<ActivityGroup> sourceGroupSet = source.getActivityGroups();
		Set<ActivityGroup> groupSet = activity.getActivityGroups();
		if (CollectionUtils.isNotEmpty(sourceGroupSet) && CollectionUtils.isNotEmpty(groupSet)) {
			for (ActivityGroup sourceGroup : sourceGroupSet) {
				for (ActivityGroup group : groupSet) {
					if (sourceGroup.getId() == group.getId()) {
						//t1t2-v4 0518成人同行价修改
						BigDecimal sourceSettlementAdultPrice = sourceGroup.getSettlementAdultPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSettlementAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSettlementAdultPrice = group.getSettlementAdultPrice() == null ? new BigDecimal("0.00") : group.getSettlementAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//t1t2-v4 0518儿童同行价修改
						BigDecimal sourceSettlementcChildPrice = sourceGroup.getSettlementcChildPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSettlementcChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSettlementcChildPrice = group.getSettlementcChildPrice() == null ? new BigDecimal("0.00") : group.getSettlementcChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//t1t2-v4 0518特殊人群同行价修改
						BigDecimal sourceSettlementSpecialPrice = sourceGroup.getSettlementSpecialPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSettlementSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSettlementSpecialPrice = group.getSettlementSpecialPrice() == null ? new BigDecimal("0.00") : group.getSettlementSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//t1t2-v4 0518成人直客价修改
						BigDecimal sourceSuggestAdultPrice = sourceGroup.getSuggestAdultPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSuggestAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSuggestAdultPrice = group.getSuggestAdultPrice() == null ? new BigDecimal("0.00") : group.getSuggestAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//t1t2-v4 0518儿童直客价修改
						BigDecimal sourceSuggestChildPrice = sourceGroup.getSuggestChildPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSuggestChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSuggestChildPrice = group.getSuggestChildPrice() == null ? new BigDecimal("0.00") : group.getSuggestChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//t1t2-v4 0518特殊人群直客价修改
						BigDecimal sourceSuggestSpecialPrice = sourceGroup.getSuggestSpecialPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSuggestSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSuggestSpecialPrice = group.getSuggestSpecialPrice() == null ? new BigDecimal("0.00") : group.getSuggestSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						if(!sourceSettlementAdultPrice.equals(targetSettlementAdultPrice) || !sourceSettlementcChildPrice.equals(
								targetSettlementcChildPrice) || !sourceSettlementSpecialPrice.equals(targetSettlementSpecialPrice)
								|| !sourceSuggestAdultPrice.equals(targetSuggestAdultPrice) || !sourceSuggestChildPrice.equals(
								targetSuggestChildPrice) || !sourceSuggestSpecialPrice.equals(targetSuggestSpecialPrice)) {
							group.setPricingStrategyStatus(Context.PRICING_NEED_RESET_STATUS);
							activityGroupDao.updateByOptLock(group, StringUtils.getVersionNumber(request));
						}
					}
				}
			}
		}
	}

	/**
	 * 记录产品修改同行价或者直客价日志
	 * @param source 修改前产品对象
	 * @param activity 修改后产品对象
     */
	public void setFreeLog(TravelActivity source, TravelActivity activity) {
		Set<ActivityGroup> sourceGroupSet = source.getActivityGroups();
		Set<ActivityGroup> groupSet = activity.getActivityGroups();
		if (CollectionUtils.isNotEmpty(sourceGroupSet) && CollectionUtils.isNotEmpty(groupSet)) {
			for (ActivityGroup sourceGroup : sourceGroupSet) {
				for (ActivityGroup group : groupSet) {
					if (sourceGroup.getId() == group.getId()) {
						if (sourceGroup.getFreePosition() != group.getFreePosition()) {
							StringBuffer message = new StringBuffer("");
							message.append(DateUtils.getDate(DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS))
							.append(" 团期id："+ sourceGroup.getId() + " ")
							.append(" 团期编号："+ sourceGroup.getGroupCode() + " ")
							.append("  用户：" + UserUtils.getUser().getName())
							.append("在产品修改时把团期余位从：" + sourceGroup.getFreePosition() + "个")
							.append("修改为：" + group.getFreePosition() + "个");
//						logger.info(message);
						saveLogOperate(Context.log_type_product, Context.log_type_product_name, message.toString(), Context.log_state_up,
								activity.getActivityKind(), group.getId());
						}
						
						// 0524需求 团期余位变化,记录在团控板中
						Integer amount = group.getFreePosition() - sourceGroup.getFreePosition();
						if(0 != amount){
							String remarks = "余位从" + sourceGroup.getFreePosition() + "调整为" + group.getFreePosition();
							groupControlBoardService.insertGroupControlBoard(3, Math.abs(amount), remarks, group.getId(), -1);
						}
						// 0524需求 团期余位变化,记录在团控板中
						
						//t1t2-v2成人同行价修改
						BigDecimal sourceSettlementAdultPrice = sourceGroup.getSettlementAdultPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSettlementAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSettlementAdultPrice = group.getSettlementAdultPrice() == null ? new BigDecimal("0.00") : group.getSettlementAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						if(!sourceSettlementAdultPrice.equals(targetSettlementAdultPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),0,"mark");
							String targetCurrencyInfo = CurrencyUtils.getCurrencyInfo(group.getCurrencyType(),0,"mark");
							StringBuffer sb = new StringBuffer();
							String sourceSettlementAdultPriceString = "",targetSettlementAdultPriceString = "";
							if(sourceSettlementAdultPrice.equals(new BigDecimal("0.00"))) {
								sourceSettlementAdultPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSettlementAdultPriceString = sourceSettlementAdultPrice.toString();
							}
							if(targetSettlementAdultPrice.equals(new BigDecimal("0.00"))) {
								targetSettlementAdultPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSettlementAdultPriceString = targetSettlementAdultPrice.toString();
							}
							sb.append("成人同行价：").append(sourceCurrencyInfo).append(sourceSettlementAdultPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSettlementAdultPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"settlementAdultPrice",sb.toString(),group);
						}
						//t1t2-v2儿童同行价修改
						BigDecimal sourceSettlementcChildPrice = sourceGroup.getSettlementcChildPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSettlementcChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSettlementcChildPrice = group.getSettlementcChildPrice() == null ? new BigDecimal("0.00") : group.getSettlementcChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						if(!sourceSettlementcChildPrice.equals(targetSettlementcChildPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),1,"mark");
							String targetCurrencyInfo = CurrencyUtils.getCurrencyInfo(group.getCurrencyType(),1,"mark");
							StringBuffer sb = new StringBuffer();
							String sourceSettlementcChildPriceString = "",targetSettlementcChildPriceString = "";
							if(sourceSettlementcChildPrice.equals(new BigDecimal("0.00"))) {
								sourceSettlementcChildPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSettlementcChildPriceString = sourceSettlementcChildPrice.toString();
							}
							if(targetSettlementcChildPrice.equals(new BigDecimal("0.00"))) {
								targetSettlementcChildPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSettlementcChildPriceString = targetSettlementcChildPrice.toString();
							}
							sb.append("儿童同行价：").append(sourceCurrencyInfo).append(sourceSettlementcChildPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSettlementcChildPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"settlementcChildPrice",sb.toString(),group);
						}
						//t1t2-v2特殊人群同行价修改
						BigDecimal sourceSettlementSpecialPrice = sourceGroup.getSettlementSpecialPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSettlementSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSettlementSpecialPrice = group.getSettlementSpecialPrice() == null ? new BigDecimal("0.00") : group.getSettlementSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						if(!sourceSettlementSpecialPrice.equals(targetSettlementSpecialPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),2,"mark");
							String targetCurrencyInfo = CurrencyUtils.getCurrencyInfo(group.getCurrencyType(),2,"mark");
							StringBuffer sb = new StringBuffer();
							String sourceSettlementSpecialPriceString = "",targetSettlementSpecialPriceString = "";
							if(sourceSettlementSpecialPrice.equals(new BigDecimal("0.00"))) {
								sourceSettlementSpecialPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSettlementSpecialPriceString = sourceSettlementSpecialPrice.toString();
							}
							if(targetSettlementSpecialPrice.equals(new BigDecimal("0.00"))) {
								targetSettlementSpecialPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSettlementSpecialPriceString = targetSettlementSpecialPrice.toString();
							}
							sb.append("特殊人群同行价：").append(sourceCurrencyInfo).append(sourceSettlementSpecialPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSettlementSpecialPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"settlementSpecialPrice",sb.toString(),group);
						}
						//t1t2-v2成人直客价修改
						BigDecimal sourceSuggestAdultPrice = sourceGroup.getSuggestAdultPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSuggestAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSuggestAdultPrice = group.getSuggestAdultPrice() == null ? new BigDecimal("0.00") : group.getSuggestAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						if(!sourceSuggestAdultPrice.equals(targetSuggestAdultPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),3,"mark");
							String targetCurrencyInfo = CurrencyUtils.getCurrencyInfo(group.getCurrencyType(),3,"mark");
							StringBuffer sb = new StringBuffer();
							String sourceSuggestAdultPriceString = "",targetSuggestAdultPriceString = "";
							if(sourceSuggestAdultPrice.equals(new BigDecimal("0.00"))) {
								sourceSuggestAdultPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSuggestAdultPriceString = sourceSuggestAdultPrice.toString();
							}
							if(targetSuggestAdultPrice.equals(new BigDecimal("0.00"))) {
								targetSuggestAdultPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSuggestAdultPriceString = targetSuggestAdultPrice.toString();
							}
							sb.append("成人直客价：").append(sourceCurrencyInfo).append(sourceSuggestAdultPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSuggestAdultPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"suggestAdultPrice",sb.toString(),group);
						}
						//t1t2-v2儿童直客价修改
						BigDecimal sourceSuggestChildPrice = sourceGroup.getSuggestChildPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSuggestChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSuggestChildPrice = group.getSuggestChildPrice() == null ? new BigDecimal("0.00") : group.getSuggestChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						if(!sourceSuggestChildPrice.equals(targetSuggestChildPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),4,"mark");
							String targetCurrencyInfo = CurrencyUtils.getCurrencyInfo(group.getCurrencyType(),4,"mark");
							StringBuffer sb = new StringBuffer();
							String sourceSuggestChildPriceString = "",targetSuggestChildPriceString = "";
							if(sourceSuggestChildPrice.equals(new BigDecimal("0.00"))) {
								sourceSuggestChildPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSuggestChildPriceString = sourceSuggestChildPrice.toString();
							}
							if(targetSuggestChildPrice.equals(new BigDecimal("0.00"))) {
								targetSuggestChildPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSuggestChildPriceString = targetSuggestChildPrice.toString();
							}
							sb.append("儿童直客价：").append(sourceCurrencyInfo).append(sourceSuggestChildPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSuggestChildPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"suggestChildPrice",sb.toString(),group);
						}
						//t1t2-v2特殊人群直客价修改
						BigDecimal sourceSuggestSpecialPrice = sourceGroup.getSuggestSpecialPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSuggestSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSuggestSpecialPrice = group.getSuggestSpecialPrice() == null ? new BigDecimal("0.00") : group.getSuggestSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						if(!sourceSuggestSpecialPrice.equals(targetSuggestSpecialPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),5,"mark");
							String targetCurrencyInfo = CurrencyUtils.getCurrencyInfo(group.getCurrencyType(),5,"mark");
							StringBuffer sb = new StringBuffer();
							String sourceSuggestSpecialPriceString = "",targetSuggestSpecialPriceString = "";
							if(sourceSuggestSpecialPrice.equals(new BigDecimal("0.00"))) {
								sourceSuggestSpecialPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSuggestSpecialPriceString = sourceSuggestSpecialPrice.toString();
							}
							if(targetSuggestSpecialPrice.equals(new BigDecimal("0.00"))) {
								targetSuggestSpecialPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSuggestSpecialPriceString = targetSuggestSpecialPrice.toString();
							}
							sb.append("特殊人群直客价：").append(sourceCurrencyInfo).append(sourceSuggestSpecialPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSuggestSpecialPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"suggestSpecialPrice",sb.toString(),group);
						}
					}
				}
			}
		}
	}

	public void setFreeLog(TravelActivity activity, ActivityGroup sourceGroup, Map<String,String> map) {
		//ActivityGroup group = activityGroupService.findById(groupId);
		long id = Long.parseLong(map.get("groupid"));
		int freePosition = Integer.parseInt(map.get("freePosition"));
		BigDecimal settlementAdultPrice = new BigDecimal("0.00");
		if (StringUtils.isNotBlank(map.get("settlementAdultPrice"))){
			settlementAdultPrice = new BigDecimal(map.get("settlementAdultPrice")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		}
		BigDecimal settlementcChildPrice =  new BigDecimal("0.00");
		if(StringUtils.isNotBlank(map.get("settlementcChildPrice"))){
			settlementcChildPrice = new BigDecimal(map.get("settlementcChildPrice")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		}
		BigDecimal settlementSpecialPrice = new BigDecimal("0.00");
		if(StringUtils.isNotBlank(map.get("settlementSpecialPrice"))){
			settlementSpecialPrice = new BigDecimal(map.get("settlementSpecialPrice")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		}
		BigDecimal suggestAdultPrice = new BigDecimal("0.00");
		if(StringUtils.isNotBlank(map.get("suggestAdultPrice"))){
			suggestAdultPrice = new BigDecimal(map.get("suggestAdultPrice")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		}
		//BigDecimal suggestAdultPrice = map.get("suggestAdultPrice") == null ? new BigDecimal("0.00") : new BigDecimal(map.get("suggestAdultPrice")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		BigDecimal suggestChildPrice = new BigDecimal("0.00");
		if(StringUtils.isNotBlank(map.get("suggestChildPrice"))){
			suggestChildPrice = new BigDecimal(map.get("suggestChildPrice")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		}
		//BigDecimal suggestChildPrice = map.get("suggestChildPrice") == null ? new BigDecimal("0.00") : new BigDecimal(map.get("suggestChildPrice")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		BigDecimal suggestSpecialPrice = new BigDecimal("0.00");
		if(StringUtils.isNotBlank(map.get("suggestSpecialPrice"))){
			suggestSpecialPrice = new BigDecimal(map.get("suggestSpecialPrice")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		}
		//BigDecimal suggestSpecialPrice = map.get("suggestSpecialPrice") == null ? new BigDecimal("0.00") : new BigDecimal(map.get("suggestSpecialPrice")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		Set<ActivityGroup> groupSet = activity.getActivityGroups();
					if (sourceGroup.getId() == id) {
						if (sourceGroup.getFreePosition() != freePosition) {
							StringBuffer message = new StringBuffer("");
							message.append(DateUtils.getDate(DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS))
									.append(" 团期id："+ sourceGroup.getId() + " ")
									.append(" 团期编号："+ sourceGroup.getGroupCode() + " ")
									.append("  用户：" + UserUtils.getUser().getName())
									.append("在产品修改时把团期余位从：" + sourceGroup.getFreePosition() + "个")
									.append("修改为：" + freePosition + "个");
//						logger.info(message);
							saveLogOperate(Context.log_type_product, Context.log_type_product_name, message.toString(), Context.log_state_up,
									activity.getActivityKind(), id);
						}
						//t1t2-v2成人同行价修改
						BigDecimal sourceSettlementAdultPrice = sourceGroup.getSettlementAdultPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSettlementAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//BigDecimal targetSettlementAdultPrice = settlementAdultPrice == null ? new BigDecimal("0.00") : settlementAdultPrice.setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSettlementAdultPrice = settlementAdultPrice;
						if(!sourceSettlementAdultPrice.equals(targetSettlementAdultPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),0,"mark");
							String targetCurrencyInfo = sourceCurrencyInfo;
							StringBuffer sb = new StringBuffer();
							String sourceSettlementAdultPriceString = "",targetSettlementAdultPriceString = "";
							if(sourceSettlementAdultPrice.equals(new BigDecimal("0.00"))) {
								sourceSettlementAdultPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSettlementAdultPriceString = sourceSettlementAdultPrice.toString();
							}
							if(targetSettlementAdultPrice.equals(new BigDecimal("0.00"))) {
								targetSettlementAdultPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSettlementAdultPriceString = targetSettlementAdultPrice.toString();
							}
							sb.append("成人同行价：").append(sourceCurrencyInfo).append(sourceSettlementAdultPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSettlementAdultPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"settlementAdultPrice",sb.toString(),sourceGroup);
						}
						//t1t2-v2儿童同行价修改
						BigDecimal sourceSettlementcChildPrice = sourceGroup.getSettlementcChildPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSettlementcChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//BigDecimal targetSettlementcChildPrice = settlementcChildPrice == null ? new BigDecimal("0.00") : settlementcChildPrice.setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSettlementcChildPrice = settlementcChildPrice;
						if(!sourceSettlementcChildPrice.equals(targetSettlementcChildPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),1,"mark");
							String targetCurrencyInfo = sourceCurrencyInfo;
							StringBuffer sb = new StringBuffer();
							String sourceSettlementcChildPriceString = "",targetSettlementcChildPriceString = "";
							if(sourceSettlementcChildPrice.equals(new BigDecimal("0.00"))) {
								sourceSettlementcChildPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSettlementcChildPriceString = sourceSettlementcChildPrice.toString();
							}
							if(targetSettlementcChildPrice.equals(new BigDecimal("0.00"))) {
								targetSettlementcChildPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSettlementcChildPriceString = targetSettlementcChildPrice.toString();
							}
							sb.append("儿童同行价：").append(sourceCurrencyInfo).append(sourceSettlementcChildPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSettlementcChildPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"settlementcChildPrice",sb.toString(),sourceGroup);
						}
						//t1t2-v2特殊人群同行价修改
						BigDecimal sourceSettlementSpecialPrice = sourceGroup.getSettlementSpecialPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSettlementSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//BigDecimal targetSettlementSpecialPrice = settlementSpecialPrice == null ? new BigDecimal("0.00") : settlementSpecialPrice.setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSettlementSpecialPrice = settlementSpecialPrice;
						if(!sourceSettlementSpecialPrice.equals(targetSettlementSpecialPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),2,"mark");
							String targetCurrencyInfo = sourceCurrencyInfo;
							StringBuffer sb = new StringBuffer();
							String sourceSettlementSpecialPriceString = "",targetSettlementSpecialPriceString = "";
							if(sourceSettlementSpecialPrice.equals(new BigDecimal("0.00"))) {
								sourceSettlementSpecialPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSettlementSpecialPriceString = sourceSettlementSpecialPrice.toString();
							}
							if(targetSettlementSpecialPrice.equals(new BigDecimal("0.00"))) {
								targetSettlementSpecialPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSettlementSpecialPriceString = targetSettlementSpecialPrice.toString();
							}
							sb.append("特殊人群同行价：").append(sourceCurrencyInfo).append(sourceSettlementSpecialPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSettlementSpecialPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"settlementSpecialPrice",sb.toString(),sourceGroup);
						}
						//t1t2-v2成人直客价修改
						BigDecimal sourceSuggestAdultPrice = sourceGroup.getSuggestAdultPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSuggestAdultPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//BigDecimal targetSuggestAdultPrice = suggestAdultPrice == null ? new BigDecimal("0.00") : suggestAdultPrice.setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSuggestAdultPrice = suggestAdultPrice;
						if(!sourceSuggestAdultPrice.equals(targetSuggestAdultPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),3,"mark");
							String targetCurrencyInfo = sourceCurrencyInfo;
							StringBuffer sb = new StringBuffer();
							String sourceSuggestAdultPriceString = "",targetSuggestAdultPriceString = "";
							if(sourceSuggestAdultPrice.equals(new BigDecimal("0.00"))) {
								sourceSuggestAdultPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSuggestAdultPriceString = sourceSuggestAdultPrice.toString();
							}
							if(targetSuggestAdultPrice.equals(new BigDecimal("0.00"))) {
								targetSuggestAdultPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSuggestAdultPriceString = targetSuggestAdultPrice.toString();
							}
							sb.append("成人直客价：").append(sourceCurrencyInfo).append(sourceSuggestAdultPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSuggestAdultPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"suggestAdultPrice",sb.toString(),sourceGroup);
						}
						//t1t2-v2儿童直客价修改
						BigDecimal sourceSuggestChildPrice = sourceGroup.getSuggestChildPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSuggestChildPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//BigDecimal targetSuggestChildPrice = suggestChildPrice == null ? new BigDecimal("0.00") : suggestChildPrice.setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSuggestChildPrice = suggestChildPrice;
						if(!sourceSuggestChildPrice.equals(targetSuggestChildPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),4,"mark");
							String targetCurrencyInfo = sourceCurrencyInfo;
							StringBuffer sb = new StringBuffer();
							String sourceSuggestChildPriceString = "",targetSuggestChildPriceString = "";
							if(sourceSuggestChildPrice.equals(new BigDecimal("0.00"))) {
								sourceSuggestChildPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSuggestChildPriceString = sourceSuggestChildPrice.toString();
							}
							if(targetSuggestChildPrice.equals(new BigDecimal("0.00"))) {
								targetSuggestChildPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSuggestChildPriceString = targetSuggestChildPrice.toString();
							}
							sb.append("儿童直客价：").append(sourceCurrencyInfo).append(sourceSuggestChildPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSuggestChildPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"suggestChildPrice",sb.toString(),sourceGroup);
						}
						//t1t2-v2特殊人群直客价修改
						BigDecimal sourceSuggestSpecialPrice = sourceGroup.getSuggestSpecialPrice() == null ? new BigDecimal("0.00") : sourceGroup.getSuggestSpecialPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
						//BigDecimal targetSuggestSpecialPrice = suggestSpecialPrice == null ? new BigDecimal("0.00") : suggestSpecialPrice.setScale(2,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal targetSuggestSpecialPrice = suggestSpecialPrice;
						if(!sourceSuggestSpecialPrice.equals(targetSuggestSpecialPrice)) {
							String sourceCurrencyInfo = CurrencyUtils.getCurrencyInfo(sourceGroup.getCurrencyType(),5,"mark");
							String targetCurrencyInfo = sourceCurrencyInfo;
							StringBuffer sb = new StringBuffer();
							String sourceSuggestSpecialPriceString = "",targetSuggestSpecialPriceString = "";
							if(sourceSuggestSpecialPrice.equals(new BigDecimal("0.00"))) {
								sourceSuggestSpecialPriceString = "";
								sourceCurrencyInfo = "";
							}
							else {
								sourceSuggestSpecialPriceString = sourceSuggestSpecialPrice.toString();
							}
							if(targetSuggestSpecialPrice.equals(new BigDecimal("0.00"))) {
								targetSuggestSpecialPriceString = "";
								targetCurrencyInfo = "";
							}
							else {
								targetSuggestSpecialPriceString = targetSuggestSpecialPrice.toString();
							}
							sb.append("特殊人群直客价：").append(sourceCurrencyInfo).append(sourceSuggestSpecialPriceString)
									.append("修改为").append(targetCurrencyInfo).append(targetSuggestSpecialPriceString);
							saveLogProduct(Context.ProductType.PRODUCT_LOOSE,Context.log_state_up,"suggestSpecialPrice",sb.toString(),sourceGroup);
						}
					}
	}


/*——————————————————————————————————————————————————————————————————————————————————————————*/
	/**
	 * 对list按照日期顺序排序
	 * @param list
	 */
	private void getSortList(List<ActivityGroup> list) {
		Collections.sort(list, new Comparator<ActivityGroup>() {  
		        public int compare(ActivityGroup arg0, ActivityGroup arg1) {  
		            long hits0 = arg0.getGroupOpenDate().getTime();  
		            long hits1 = arg1.getGroupOpenDate().getTime();  
		            if (hits1 > hits0) {  
		                return 1;  
		            } else if (hits1 == hits0) {  
		                return 0;  
		            } else {  
		                return -1;  
		            }  
		        }  
		    });
	}
	
	private void setActivity(TravelActivity activity, TravelActivity travelActivity, HttpServletRequest request) {
		//出团日期
		String[] groupOpenDates = request.getParameterValues("groupOpenDate");
		//同业价人价
		String[] settlementAdultPrices = request.getParameterValues("settlementAdultPrice");
		//建议零售价成人
		String[] suggestAdultPrices = request.getParameterValues("suggestAdultPrice");
		//因为只有一个团期，所以最早团期的团号可以用唯一的团号
		String[] groupCodes = request.getParameterValues("groupCode");
		
		String[] currencyTypes = request.getParameterValues("groupCurrencyType");
		
		//处理   产品  同行价  和  直客 价显示  后    币种符号显示不正确的问题
				BigDecimal settlementAdultMinPrice = TravelActivityUtil.getMinPrice(settlementAdultPrices, null);
				BigDecimal suggestAdultMinPrice = TravelActivityUtil.getMinPrice(suggestAdultPrices, null);
				List<ActivityGroup> list = new ArrayList<ActivityGroup>(activity.getActivityGroups());

				String suggestAdultCurrencyType="";
		for (ActivityGroup activityGroup : list) {
			if(activityGroup != null && activityGroup.getSettlementAdultPrice()!=null){
				//String[] str = activity.getCurrencyType().split(",");
				String[] str = null;
				if(null != currencyTypes && currencyTypes.length > 0) {
					str = currencyTypes;
					//str = activity.getCurrencyType().split(",");
				}
				//当没有填写价格时，值为null 调用compareTo时报错，针对该情况加个判断
				if(activityGroup.getSettlementAdultPrice()!=null){
					if(settlementAdultMinPrice.compareTo(activityGroup.getSettlementAdultPrice())==0){
						String settlementAdultCurrencyType = activityGroup.getCurrencyType();
						if(StringUtil.isNotBlank(settlementAdultCurrencyType)) {
							str[0] = settlementAdultCurrencyType.split(",")[0];
						}
						if(activityGroup.getSuggestAdultPrice()==null){
							suggestAdultCurrencyType=activity.getCurrencyType();
							if(null != str && str.length > 0) {
								//518需求，当直客价为空时，产品修改后只有一个币种
								if(str.length == 1){
									activity.setCurrencyType(str[0] + "," + str[0]);
								}else {
									activity.setCurrencyType(str[0] + "," + str[1]);
								}
							}
						}else if(suggestAdultMinPrice.compareTo(activityGroup.getSuggestAdultPrice())==0){
							suggestAdultCurrencyType = activityGroup.getCurrencyType();
							//设置成人的产品最低结算价和建议成人最低零售价币种
							if(Context.ACTIVITY_KINDS_SP.equals(activity.getActivityKind().toString())) {
								if(currencyTypes != null){
									activity.setCurrencyType(str[0] + "," + suggestAdultCurrencyType.split(",")[3]);
								}
								break;
							}
						}
					}
				}

			}
		}
		//设置成人的产品最低结算价和建议成人最低零售价币种
//		if(Context.ACTIVITY_KINDS_SP.equals(activity.getActivityKind().toString())) {
//			if(currencyTypes != null){
//				activity.setCurrencyType(currencyTypes[0].split(",")[0] + "," + currencyTypes[0].split(",")[3]);
//			}
//		} else if (Context.ACTIVITY_KINDS_YL.equals(activity.getActivityKind().toString())) {
//			if(currencyTypes != null){
//				activity.setCurrencyType(currencyTypes[0].split(",")[0] + "," + currencyTypes[0].split(",")[2]);
//			}
//		}else{
//			if(currencyTypes != null){
//				activity.setCurrencyType(currencyTypes[0].split(",")[0]);
//			}
//		}
		
		if(null != groupOpenDates) {
			Date groupOpenDate = TravelActivityUtil.getMinDate(groupOpenDates,null);
			Date groupCloseDate = TravelActivityUtil.getMaxDate(groupOpenDates,null);
			activity.setGroupOpenDate(groupOpenDate);
			activity.setGroupCloseDate(groupCloseDate);
			activity.setSettlementAdultPrice(TravelActivityUtil.getMinPrice(settlementAdultPrices,null));
			activity.setSuggestAdultPrice(TravelActivityUtil.getMinPrice(suggestAdultPrices,null));
//			String minGroupCode = activityGroupDao.findByGroupOpenDate(activity.getGroupOpenDate()).getGroupCode();
			for(int i = 0; i < groupOpenDates.length; i++) {
				//最早出团团号
				if(groupOpenDates[i].equals(groupOpenDate) && !groupCodes[i].equals(travelActivity.getGroupOpenCode())) {
					activity.setGroupOpenCode(groupCodes[i]);
				}
				//最晚出团团号
				if(groupOpenDates[i].equals(groupOpenDate) && !groupCodes[i].equals(travelActivity.getGroupCloseCode())) {
					activity.setGroupCloseCode(groupCodes[i]);
				}
			}
		}else{
			activity.setGroupOpenDate(null);
			activity.setGroupCloseDate(null);
			activity.setSettlementAdultPrice(null);
			activity.setSuggestAdultPrice(null);
			activity.setGroupOpenCode(null);
		}
		
		activity.setActivitySerNum(travelActivity.getActivitySerNum());
		activity.setAcitivityName(travelActivity.getAcitivityName());
		activity.setActivityDuration(travelActivity.getActivityDuration());
		activity.setTargetAreaList(travelActivity.getTargetAreaList());
		activity.setOpUserList(travelActivity.getOpUserList());
		activity.setGroupLead(travelActivity.getGroupLead());
		activity.setOpUserId(travelActivity.getOpUserId());
		if(travelActivity.getActivityAirTicket().getId() == null) {
			activity.setActivityAirTicket(null);
		}else{
			ActivityAirTicket activityAirTicket = iActivityAirTicketService.getActivityAirTicketById(travelActivity.getActivityAirTicket().getId());
			activity.setActivityAirTicket(activityAirTicket);
		}
		
		activity.setOverseasFlag(travelActivity.getOverseasFlag());
		
		activity.setTravelTypeId(travelActivity.getTravelTypeId());
		if(travelActivity.getTravelTypeId() != null) {
			activity.setTravelTypeName(DictUtils.getDicMap(Context.TRAVEL_TYPE).get(travelActivity.getTravelTypeId().toString()));			
		}else{
			activity.setTravelTypeName(null);
		}
		if(travelActivity.getActivityLevelId() != null) {
			activity.setActivityLevelId(travelActivity.getActivityLevelId());	
		}else{
			activity.setActivityLevelId(null);	
		}
		if(travelActivity.getActivityTypeId() != null) {
			activity.setActivityTypeId(travelActivity.getActivityTypeId());
		}else{
			activity.setActivityTypeId(null);
		}
		//525线路玩法
		if(travelActivity.getTouristLineId() != null) {
			activity.setTouristLineId(travelActivity.getTouristLineId());
		}else {
			activity.setTouristLineId(null);
		}
		activity.setFromArea(travelActivity.getFromArea());
		activity.setBackArea(travelActivity.getBackArea());
		if(null != travelActivity.getTrafficMode()) {
			activity.setTrafficMode(travelActivity.getTrafficMode());
			if(DictUtils.getRelevanceFlag(StringUtils.toLong(UserUtils.getUser().getCompany().getId())).indexOf(travelActivity.getTrafficMode().toString())>=0) {
				activity.setTrafficName(travelActivity.getTrafficName());
			}
		}
		activity.setPayMode(travelActivity.getPayMode());
		activity.setRemainDays(travelActivity.getRemainDays());					
		activity.setActivityDuration(travelActivity.getActivityDuration());
		activity.setRemainDays_deposit(travelActivity.getRemainDays_deposit());
		activity.setRemainDays_deposit_hour(travelActivity.getRemainDays_deposit_hour());
		activity.setRemainDays_deposit_fen(travelActivity.getRemainDays_deposit_fen());
		activity.setRemainDays_advance(travelActivity.getRemainDays_advance());
		activity.setRemainDays_advance_hour(travelActivity.getRemainDays_advance_hour());
		activity.setRemainDays_advance_fen(travelActivity.getRemainDays_advance_fen());
		activity.setRemainDays_data(travelActivity.getRemainDays_data());
		activity.setRemainDays_data_hour(travelActivity.getRemainDays_data_hour());
		activity.setRemainDays_data_fen(travelActivity.getRemainDays_data_fen());
		activity.setRemainDays_guarantee(travelActivity.getRemainDays_guarantee());
		activity.setRemainDays_guarantee_hour(travelActivity.getRemainDays_guarantee_hour());
		activity.setRemainDays_guarantee_fen(travelActivity.getRemainDays_guarantee_fen());
		activity.setRemainDays_express(travelActivity.getRemainDays_express());
		activity.setRemainDays_express_hour(travelActivity.getRemainDays_express_hour());
		activity.setRemainDays_express_fen(travelActivity.getRemainDays_express_fen());
		//保存产品付款方式，如果付款方式被选择，则标识为1，否则为0
		String payMode[] = request.getParameterValues("payMode");
		activity.setPayMode_deposit(0);
		activity.setPayMode_advance(0);
		activity.setPayMode_full(0);
		activity.setPayMode_op(0);
		activity.setPayMode_data(0);
		activity.setPayMode_guarantee(0);
		activity.setPayMode_express(0);
		activity.setPayMode_cw(0);//wangxinwei added 20151111,添加，财务确认占位，对应需求号 C362
		//activity.setpaymoe_
		if(payMode != null && payMode.length > 0) {
			for(String mode : payMode) {
				//订金占位：1  预占位：2  全款支付：3 资料占位：4   担保占位:5   确认单占位:6
				if("1".equals(mode)) {
					activity.setPayMode_deposit(1);
				} else if("2".equals(mode)) {
					activity.setPayMode_advance(1);
				} else if("3".equals(mode)) {
					activity.setPayMode_full(1);
				} else if("7".equals(mode)) {
					activity.setPayMode_op(1);
				} else if("4".equals(mode)){
					activity.setPayMode_data(1);
				} else if("5".equals(mode)){
					activity.setPayMode_guarantee(1);
				} else if("6".equals(mode)){
					activity.setPayMode_express(1);
				} else if("8".equals(mode)){//wangxinwei added 20151111,添加，财务确认占位，对应需求号 C362
					activity.setPayMode_cw(1);
				}
			}
		} 
		if(activity.getPayMode_deposit() != null && activity.getPayMode_deposit() == 0) {
			activity.setRemainDays_deposit(null);
		}
		if(activity.getPayMode_advance() != null && activity.getPayMode_advance() == 0) {
			activity.setRemainDays_advance(null);
		}
        activity.setOutArea(travelActivity.getOutArea());
	}
	
	/**
	 * 自定义产品等级、产品类型
	 * @param activity 数据库查询产品对象
	 * @param travelActivity 页面传递对象
	 * @param request
	 */
	private void setCustomer(TravelActivity activity, TravelActivity travelActivity, HttpServletRequest request) {
		/**如果有自定义产品等级，保存等级信息并赋值给产品等级属性*/
		int mxVal = Integer.MIN_VALUE;
		List<Dict> dictList = Lists.newArrayList();
		//是否自定义产品等级
		String product_level = request.getParameter("product_level");
		if(StringUtils.isNotBlank(product_level) && activity.getActivityLevelId() == null) {
			dictList = dictDao.findByType(Context.PRODUCT_LEVEL);
			//循环求出已有产品等级的最大值，新保存产品等级加1
			for(Dict dict : dictList) {
				if(mxVal < Integer.parseInt(dict.getValue()))
					mxVal = Integer.parseInt(dict.getValue());
			}
			Dict dict = dictList.get(0);
			Dict tmp = new Dict();
			tmp.setLabel(product_level);
			tmp.setValue(String.valueOf(mxVal+1));
			tmp.setType(Context.PRODUCT_LEVEL);
			tmp.setDescription(dict.getDescription());
			tmp.setSort(mxVal+1);
			tmp.setDelFlag("0");
			dictDao.save(tmp);
			activity.setActivityLevelId(Integer.parseInt(tmp.getValue()));
			activity.setActivityLevelName(tmp.getLabel());
		} else if(travelActivity.getActivityLevelId() != null){
			activity.setActivityLevelName(DictUtils.getValueAndLabelMap(Context.PRODUCT_LEVEL,StringUtils.toLong(UserUtils.getUser().getCompany().getId())).get(travelActivity.getActivityLevelId().toString()));
		}
		
		/** 如果有自定义产品类型，保存类型信息并赋值给产品类型属性  */
		mxVal = Integer.MIN_VALUE;
		//是否自定义产品类型
		String product_type = request.getParameter("product_type");
		if(StringUtils.isNotBlank(product_type) && activity.getActivityTypeId()==null) {
			dictList = dictDao.findByType(Context.PRODUCT_TYPE);
			for(Dict dict:dictList){
				if(mxVal<Integer.parseInt(dict.getValue()))
					mxVal = Integer.parseInt(dict.getValue());
			}
			Dict dict = dictList.get(0);
			Dict tmp = new Dict();
			tmp.setLabel(product_type);
			tmp.setValue(String.valueOf(mxVal+1));
			tmp.setType(Context.PRODUCT_TYPE);
			tmp.setDescription(dict.getDescription());
			tmp.setSort(mxVal+1);
			tmp.setDelFlag("0");
			dictDao.save(tmp);
			activity.setActivityTypeId(Integer.parseInt(tmp.getValue()));
			activity.setActivityTypeName(tmp.getLabel());
		} else if(travelActivity.getActivityTypeId() != null){
			activity.setActivityTypeName(DictUtils.getValueAndLabelMap(Context.PRODUCT_TYPE,StringUtils.toLong(UserUtils.getUser().getCompany().getId())).get(travelActivity.getActivityTypeId().toString()));
		}
	}
	
	private  String filterCtrlChars(String source){
	      StringBuffer sf = new StringBuffer();
	      for (char c : source.toCharArray()){
	          if (Character.isISOControl(c)){
	              sf.append("\\").append(Integer.toOctalString(c));       
	          }else{
	              sf.append(c);
	          }
	      }
	      return sf.toString();
	  }
	
	/**
	 * 产品添加团期
	 * @param activity 数据库查询产品对象
	 * @param request
	 * @throws Exception 
	 */
	private String addGroup(TravelActivity activity, HttpServletRequest request,Model model) throws Exception {
		//所有出团日期的数目
		int groups = 0;
		//现有出团日期的数目
		int len = 0;
		//出团日期
		String[] groupOpenDates = request.getParameterValues("groupOpenDate");
		if(groupOpenDates != null)
			groups = groupOpenDates.length;
		else
			groups = 0;
		//截团日期
		String[] groupCloseDates = request.getParameterValues("groupCloseDate");
		//团号
		String[] groupCodes = request.getParameterValues("groupCode");
		//签证
		String[] visaCountrys = request.getParameterValues("visaCountry");
		//材料截止日期
		String[] visaDates = request.getParameterValues("visaDate");
		//同业价人价
		String[] settlementAdultPrices = request.getParameterValues("settlementAdultPrice");
		//同业价儿童
		String[] settlementcChildPrices = request.getParameterValues("settlementcChildPrice");
		//同业价特殊人群
		String[] settlementSpecialPrices = request.getParameterValues("settlementSpecialPrice");
//		//trekiz成人价
//		String[] trekizPrices = request.getParameterValues("trekizPrice");
//		//trekiz儿童价
//		String[] trekizChildPrices = request.getParameterValues("trekizChildPrice");
		//建议零售价成人
		String[] suggestAdultPrices = request.getParameterValues("suggestAdultPrice");
		//应付账期
		String[] yingFuDate = request.getParameterValues("yingFuDate");
		//建议零售价儿童
		String[] suggestChildPrices = request.getParameterValues("suggestChildPrice");
		//建议零售价特殊人群
		String[] suggestSpecialPrices = request.getParameterValues("suggestSpecialPrice");
		//单房差
		String[] singleDiffs = request.getParameterValues("singleDiff");
		//儿童最高人数
		String[] maxChildrenCounts  = request.getParameterValues("maxChildrenCount");
		//特殊人群最高人数
		String[] maxPeopleCounts  = request.getParameterValues("maxPeopleCount");
		//需交订金
		String[] payDeposits = request.getParameterValues("payDeposit");
		//预收人数
		String[] planPositions = request.getParameterValues("planPosition");
		//空位数量
		String[] freePositions = request.getParameterValues("freePosition");
		//所有出团产品
		Set<ActivityGroup> activityGroups = new HashSet<ActivityGroup>();
		//价格币种
		String[] currencyTypes = request.getParameterValues("groupCurrencyType");		
		String groupids[] = request.getParameterValues("groupid");
		
		//C463団期备注
		String[] groupRemarks = request.getParameterValues("groupRemark");
		
		//223   添加游轮团控
		String[] cruiseGroupControlIds = request.getParameterValues("cruiseGroupControlId");
		//0258 懿洋假期 发票税
		String[] invoiceTaxs = request.getParameterValues("invoiceTax");
		
		//109团期优惠 只有散拼产品才有
		String[] adultDiscountPrices= request.getParameterValues("adultDiscountPrice");
		String[] childDiscountPrices= request.getParameterValues("childDiscountPrice");
		String[] specialDiscountPrices= request.getParameterValues("specialDiscountPrice");
		
		// t1t2     打通
		//同业价quauq成人
		String[] quauqAdultPrices = request.getParameterValues("quauqAdultPrice");
		//同业价quauq儿童
		String[] quauqChildPrices = request.getParameterValues("quauqChildPrice");
		//同业价quauq特殊人群
		String[] quauqSpecialPrices = request.getParameterValues("quauqSpecialPrice");
		
		
		
		//团期多价格json
		String[] priceJson= request.getParameterValues("priceJson");
		
		// 团期酒店
		String[] groupHotelStr= request.getParameterValues("groupHotelStr");
		// 团期房型		
		String[] groupHouseTypeStr= request.getParameterValues("groupHouseTypeStr");
		
		if(groupids != null)
			len = groupids.length;
		else
			len = 0;
		
		//推荐
		String[] recommends = request.getParameterValues("recommend");
		String[] tempRecommend = new String[groups];
		if (recommends != null && recommends.length > 0) {
			//遍历0到length
			for (int i = 0; i < recommends.length; i++) {
				//如果对应i的值与i相等，存进去
				if (Integer.valueOf(recommends[i]) == i - len) {
					tempRecommend[i] = "1";
				}
				//如果不相等，把值作为索引存入，
				else {
					tempRecommend[Integer.valueOf(recommends[i]) + len] = "1";
					if (tempRecommend[i] == null) {							
						tempRecommend[i] = "0";
					}
				}
			}
			for (int i = recommends.length; i < groups; i++) {
				if (tempRecommend[i] == null) {
					tempRecommend[i] = "0";
				}
			}
		}else {
			for (int i = 0; i < groups; i++) {
				tempRecommend[i] = "0";
			}
		}
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		
		String groupOpenDate = "";
		String groupCloseDate = "";	
		
		String activityKind = request.getParameter("kind");
		if(groups != 0) {
			//现有修改后的团期
			if(len != 0) {
				activityGroups = activity.getActivityGroups();
				Iterator<ActivityGroup> it = activityGroups.iterator();				
				while(it.hasNext()) {
					ActivityGroup group = (ActivityGroup) it.next();
					for(int k=0;k<groupids.length;k++){
						Long groupid = Long.parseLong(groupids[k]);
						if(group.getId().equals(groupid)){
							groupOpenDate = groupOpenDates[k];
							groupCloseDate = groupCloseDates[k];
							String visaCountry = visaCountrys[k];
							String visaDate = visaDates[k];
							String groupCode =groupCodes[k];
							String paydeAbleDate = yingFuDate[k];
							String settlementAdultPrice = settlementAdultPrices[k];
							String settlementcChildPrice = settlementcChildPrices[k];
							String settlementSpecialPrice = "";
							if(!Context.ACTIVITY_KINDS_YL.equals(activityKind)) {
								settlementSpecialPrice = settlementSpecialPrices[k];
							}else{
								String spaceType = request.getParameterValues("spaceType")[k + 1];
								group.setSpaceType(StringUtils.toLong(spaceType));
							}
//							String trekizPrice = trekizPrices[k];
//							String trekizChildPrice = trekizChildPrices[k];
							String suggestAdultPrice = "";
							String suggestChildPrice = "";
							String suggestSpecialPrice = "";



							String adultDiscountPrice = "";
							String childDiscountPrice = "";
							String specialDiscountPrice = "";

							if (Context.ACTIVITY_KINDS_SP.equals(activityKind) 
									&& (Context.SUPPLIER_UUID_YJ.equals(companyUuid) || Context.SUPPLIER_UUID_SHZL.equals(companyUuid))) {
								 adultDiscountPrice = adultDiscountPrices[k];
								 childDiscountPrice = childDiscountPrices[k];
								 specialDiscountPrice = specialDiscountPrices[k];
							}



							if(Context.ACTIVITY_KINDS_SP.equals(activityKind) || Context.ACTIVITY_KINDS_YL.equals(activityKind)) {
								suggestAdultPrice = suggestAdultPrices[k];
								suggestChildPrice = suggestChildPrices[k];
								if(Context.ACTIVITY_KINDS_SP.equals(activityKind)) {
									suggestSpecialPrice = suggestSpecialPrices[k];
								}
							}
							String maxPeopleCount = "";
							String maxChildrenCount = "";
							if(!activityKind.equals("10"))
							{
								maxChildrenCount = maxChildrenCounts[k];
								maxPeopleCount = maxPeopleCounts[k];
							}
							
							//String maxPeopleCount = maxPeopleCounts[k];
							String singleDiff = singleDiffs[k];
							String payDeposit = payDeposits[k];
							String planPosition = planPositions[k];
							String freePosition = freePositions[k];
							if (freePosition == null || Integer.parseInt(freePosition) < 0) {
								throw new Exception("余位不能为空或0");
							}
							//0258 懿洋假期 发票税
							String invoiceTax = "";
							if(invoiceTaxs!=null){
								invoiceTax = invoiceTaxs[k];
							}
							
							//C463団期备注
							String groupRemark = groupRemarks[k];
							group.setGroupRemark(groupRemark);
							
							

							// t1t2 打通  对修改时新增quauq  价的保存  ------------ 开始 -------------
							/*
							 * 对应需求号  0426 t1t2 打通  
							 * 同业价quauq成人
							 */
							if(null!=quauqAdultPrices){
								String quauqAdultPrice = quauqAdultPrices[k];
								if(StringUtils.isNotBlank(quauqAdultPrice)) {
								   group.setQuauqAdultPrice(getBigDecimal(quauqAdultPrice));
								}else{
									group.setQuauqAdultPrice(null);
								}
							}
							
							/*
							 * 对应需求号  0426 t1t2 打通  
							 * 同业价quauq儿童
							 */
							if(null!=quauqChildPrices){
								String quauqChildPrice = quauqChildPrices[k];
								if(StringUtils.isNotBlank(quauqChildPrice)) {
								    group.setQuauqChildPrice(getBigDecimal(quauqChildPrice));
								}else{
									group.setQuauqChildPrice(null);
								}
							}
							
							/*
							 * 对应需求号  0426 t1t2 打通  
							 * 同业价quauq特殊人群
							 */
							if(null!=quauqSpecialPrices){
								String quauqSpecialPrice = quauqSpecialPrices[k];
								if(StringUtils.isNotBlank(quauqSpecialPrice)) {
								   group.setQuauqSpecialPrice(getBigDecimal(quauqSpecialPrice));
								}else{
								   group.setQuauqSpecialPrice(null);
								}
							}
							
							// t1t2 打通  对新增quauq  价的保存  ------------- 结束 --------------
							
							
							//团期多价格json
//							String priceStr = priceJson[k];
							
							// 团期酒店
							String groupHotel = null;
							if(groupHotelStr != null && groupHotelStr.length > k){
								groupHotel = groupHotelStr[k];
							}
							// 团期房型
							String groupHouseType = null;
							if (groupHouseTypeStr != null && groupHouseTypeStr.length > k) {
								groupHouseType = groupHouseTypeStr[k];
							}
							
							/**
							 * -----------------  start wxwadded 2016-03-11   -------------------------------              
							 * 
							 * 对应需求号   223 ，  原有游轮团控关联修改的各种情况
							 * 
							 * 1.已有旧关联
							 *    1.1  进行过修改
							 *    1.2 进行过取消
							 *    1.3 没有进行过改变
							 *    
							 * 2.没有旧关联   
							 *    2.1 没有添加新关联
							 *    2.2添加新关联
							 * 
							 */
							
							if (null!=cruiseGroupControlIds) {
								String cruiseGroupControlId  = cruiseGroupControlIds[k];
								Integer cruiseGroupControlIdOld =  null;
								if (null!=group.getCruiseshipStockDetailId()) {
									cruiseGroupControlIdOld =  new Integer(group.getCruiseshipStockDetailId());
								}
								
								
								if (null!=cruiseGroupControlIdOld && cruiseGroupControlIdOld>0) { //已经有旧关联
									if (!cruiseGroupControlIdOld.toString().equals(cruiseGroupControlId)) {//有旧关联  且发生变化
										
										//1.更新游轮团期关联   且添加新游轮团控
										if (StringUtils.isNoneBlank(cruiseGroupControlId)) {
											
											//1.1更新关联
											group.setCruiseshipStockDetailId(Integer.valueOf(cruiseGroupControlId));
											
											//1.2更新  团控关联记录
											CruiseshipStockGroupRel cruiseshipStockGroupRelOld = cruiseshipStockGroupRelDao.getCruiseShipRelByActivityGroupId(group.getId().intValue());
											cruiseshipStockGroupRelOld.setStatus(1);
											cruiseshipStockGroupRelService.save(cruiseshipStockGroupRelOld);
											
											//1.3生成新关联记录
											CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailService.getById(Integer.valueOf(cruiseGroupControlId));
							
											CruiseshipStockGroupRel  cruiseshipStockGroupRel = new CruiseshipStockGroupRel();
											cruiseshipStockGroupRel.setActivityId(group.getSrcActivityId());
											cruiseshipStockGroupRel.setActivitygroupId(Integer.valueOf(group.getId().toString()));
											cruiseshipStockGroupRel.setActivityType(1); //  1：activitygroup表；
											cruiseshipStockGroupRel.setCruiseshipStockUuid(cruiseshipStockDetail.getCruiseshipStockUuid());
											cruiseshipStockGroupRel.setStatus(0); //0:表示已经关联
											
											String companyId  = UserUtils.getUser().getCompany().getId().toString();
											cruiseshipStockGroupRel.setWholesalerId(Integer.valueOf(companyId));
											
											cruiseshipStockGroupRel.setCruiseshipStockDetailId(group.getCruiseshipStockDetailId());
											
											String userId = UserUtils.getUser().getId().toString();
											cruiseshipStockGroupRel.setCreateBy(Integer.valueOf(userId));				
											cruiseshipStockGroupRel.setCreateDate(new Date());
											
											cruiseshipStockGroupRel.setUpdateBy(Integer.valueOf(userId));
											cruiseshipStockGroupRel.setUpdateDate(new Date());
											
											cruiseshipStockGroupRelService.save(cruiseshipStockGroupRel);
											
										//2.进行过取消操作的情况
										}else {
											
											//1.1更新关联
											group.setCruiseshipStockDetailId(null);
											
											//1.2更新  团控关联记录
											CruiseshipStockGroupRel cruiseshipStockGroupRelOld = cruiseshipStockGroupRelDao.getCruiseShipRelByActivityGroupId(group.getId().intValue());
											cruiseshipStockGroupRelOld.setStatus(1);
											cruiseshipStockGroupRelService.save(cruiseshipStockGroupRelOld);
										}
										
									}
									
								}else {//无旧关联
									if (StringUtils.isNotBlank(cruiseGroupControlId)) {
										
										//1.1更新关联
										group.setCruiseshipStockDetailId(Integer.valueOf(cruiseGroupControlId));
										
										//2.2添加新关联
										CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailService.getById(Integer.valueOf(cruiseGroupControlId));
										CruiseshipStockGroupRel  cruiseshipStockGroupRel = new CruiseshipStockGroupRel();
										cruiseshipStockGroupRel.setActivityId(group.getSrcActivityId());
										cruiseshipStockGroupRel.setActivitygroupId(Integer.valueOf(group.getId().toString()));
										cruiseshipStockGroupRel.setActivityType(1); //  1：activitygroup表；
										cruiseshipStockGroupRel.setCruiseshipStockUuid(cruiseshipStockDetail.getCruiseshipStockUuid());
										cruiseshipStockGroupRel.setStatus(0); //0:表示已经关联
										
										String companyId  = UserUtils.getUser().getCompany().getId().toString();
										cruiseshipStockGroupRel.setWholesalerId(Integer.valueOf(companyId));
										
										cruiseshipStockGroupRel.setCruiseshipStockDetailId(group.getCruiseshipStockDetailId());
										
										String userId = UserUtils.getUser().getId().toString();
										cruiseshipStockGroupRel.setCreateBy(Integer.valueOf(userId));				
										cruiseshipStockGroupRel.setCreateDate(new Date());
										
										cruiseshipStockGroupRel.setUpdateBy(Integer.valueOf(userId));
										cruiseshipStockGroupRel.setUpdateDate(new Date());
										
										cruiseshipStockGroupRelService.save(cruiseshipStockGroupRel);
									}
									
								}
							}

							
							/**
							 * -----------------  end    --------------------             
							 */
							
							
							group.setGroupOpenDate(DateUtils.parseDate(groupOpenDate));
							group.setGroupCloseDate(DateUtils.parseDate(groupCloseDate));
							group.setVisaCountry(visaCountry);
							group.setVisaDate(DateUtils.parseDate(visaDate));
							
							//修改团号同时更新审批表review_new对应的团号记录
							if(!groupCode.equals(group.getGroupCode())){
								String sql = "update review_new set group_code=? where group_code=? and group_id=? and company_id='"+UserUtils.getUser().getCompany().getUuid()+"'";
								activityGroupDao.updateBySql(sql, groupCode,group.getGroupCode(),group.getId());
								activityGroupDao.clear();
							}
							long  compayid = UserUtils.getUser().getCompany().getId();
							activityGroupDao.updateBySql("UPDATE travelactivity t SET t.groupOpenCode='"+groupCode+"' WHERE t.id="+activity.getId());
							//更新发票团号
							activityGroupDao.updateBySql("update   orderinvoice o LEFT JOIN sys_user su  ON o.createBy = su.id  " +
									"SET o.groupCode='"+groupCode+"' WHERE o.groupCode='"+group.getGroupCode()+"' AND su.companyId="+compayid);
							//更新收据团号
							activityGroupDao.updateBySql("update   orderreceipt o LEFT JOIN sys_user su  ON o.createBy = su.id  " +
									"SET o.groupCode='"+groupCode+"' WHERE o.groupCode='"+group.getGroupCode()+"' AND su.companyId="+compayid);


							group.setGroupCode(groupCode);
							group.setPayableDate(DateUtils.parseDate(paydeAbleDate));
							if(StringUtils.isNotBlank(settlementAdultPrice)) {
								group.setSettlementAdultPrice(getBigDecimal(settlementAdultPrice));
							}else{
								group.setSettlementAdultPrice(null);
							}
							if(StringUtils.isNotBlank(settlementcChildPrice)) {
								group.setSettlementcChildPrice(getBigDecimal(settlementcChildPrice));
							}else{
								group.setSettlementcChildPrice(null);
							}
							if(StringUtils.isNotBlank(settlementSpecialPrice)) {
								group.setSettlementSpecialPrice(getBigDecimal(settlementSpecialPrice));
							}else{
								group.setSettlementSpecialPrice(null);
							}
							//0258 懿洋假期 发票税
							if(StringUtils.isNotBlank(invoiceTax)){
								group.setInvoiceTax(getBigDecimal(invoiceTax));
							}else{
								group.setInvoiceTax(null);
							}
//							group.setTrekizPrice(StringNumFormat.getIntegerValue(trekizPrice));
//							group.setTrekizChildPrice(StringNumFormat.getIntegerValue(trekizChildPrice));
							if (Context.ACTIVITY_KINDS_SP.equals(activityKind) 
									&& (Context.SUPPLIER_UUID_YJ.equals(companyUuid) || Context.SUPPLIER_UUID_SHZL.equals(companyUuid))) {

								if(StringUtils.isNotBlank(adultDiscountPrice)) {
									group.setAdultDiscountPrice(getBigDecimal(adultDiscountPrice.trim()));
								}else{
									group.setAdultDiscountPrice(null);
								}
								if(StringUtils.isNotBlank(childDiscountPrice)) {
									group.setChildDiscountPrice(getBigDecimal(childDiscountPrice.trim()));
								}else{
									group.setChildDiscountPrice(null);
								}
								if(StringUtils.isNotBlank(specialDiscountPrice)) {
									group.setSpecialDiscountPrice(getBigDecimal(specialDiscountPrice.trim()));
								}else{
									group.setSpecialDiscountPrice(null);
								}

							}
							if(Context.ACTIVITY_KINDS_SP.equals(activityKind) || Context.ACTIVITY_KINDS_YL.equals(activityKind)) {
								if(StringUtils.isNotBlank(suggestAdultPrice)) {
									group.setSuggestAdultPrice(getBigDecimal(suggestAdultPrice));
								}else{
									group.setSuggestAdultPrice(null);
								}
								if(StringUtils.isNotBlank(suggestChildPrice)) {
									group.setSuggestChildPrice(getBigDecimal(suggestChildPrice));
								}else{
									group.setSuggestChildPrice(null);
								}
								if(StringUtils.isNotBlank(suggestSpecialPrice)) {
									group.setSuggestSpecialPrice(getBigDecimal(suggestSpecialPrice));
								}else{
									group.setSuggestSpecialPrice(null);
								}
							}
							Map<String, Object>  counts = activityGroupService.countOrderChildAndSpecialNum(group.getId(),null);
							int orderPersonNumChild = 0;
							if(counts != null){
								orderPersonNumChild = counts.get("orderPersonNumChild")==null?0:new Integer(counts.get("orderPersonNumChild").toString());
							}
							if(StringUtils.isNotBlank(maxChildrenCount)) {
								int maxChildrenCountTemp = new Integer(maxChildrenCount);
								if(orderPersonNumChild>maxChildrenCountTemp){
									model.addAttribute("errorMsg","团号为："+group.getGroupCode()+"的团期，儿童最高人数不能小于团期已报名儿童数");
									return "modules/activity/peopleMaxError";
								}else{
									group.setMaxChildrenCount(Integer.valueOf(maxChildrenCount));
								}
							}else{
								group.setMaxChildrenCount(null);
							}
							int orderPersonNumSpecial = 0;
							if(counts != null){
								orderPersonNumSpecial = counts.get("orderPersonNumSpecial")==null?0:new Integer(counts.get("orderPersonNumSpecial").toString());
							}
							if(StringUtils.isNotBlank(maxPeopleCount)) {
								int maxPeopleCountTemp = new Integer(maxPeopleCount);
								if(orderPersonNumSpecial > maxPeopleCountTemp){
									model.addAttribute("errorMsg","团号为："+group.getGroupCode()+"的团期，特殊人群最高数不能小于团期已报名特殊人群人数");
									return "modules/activity/peopleMaxError";
								}else{
									group.setMaxPeopleCount(Integer.valueOf(maxPeopleCount));
								}
							}else{
								group.setMaxPeopleCount(null);
							}
							if(StringUtils.isNotBlank(singleDiff)) {
								group.setSingleDiff(getBigDecimal(singleDiff));
							}else{
								group.setSingleDiff(null);
							}
							if(StringUtils.isNotBlank(payDeposit)) {
								group.setPayDeposit(getBigDecimal(payDeposit));
							}else{
								group.setPayDeposit(null);
							}
							group.setPlanPosition(StringNumFormat.getIntegerValue(planPosition));
							group.setFreePosition(StringNumFormat.getIntegerValue(freePosition));
							/////////////////////////////////
							group.setPlusFreePosition(StringNumFormat.getIntegerValue(freePosition) - group.getFreePosition());
							group.setPlusPlanPosition(StringNumFormat.getIntegerValue(planPosition) - group.getPlanPosition());
							group.setRecommend(Integer.valueOf(tempRecommend[k]));
//							group.setPriceJson(priceStr);
							group.setGroupHotel(groupHotel);
							group.setGroupHouseType(groupHouseType);
							
							//////////////////////////////////
							group.setTravelActivity(activity);
							activityGroupDao.updateByOptLock(group, StringUtils.getVersionNumber(request));
							
							break;
						}
					}						
				}
			}				
			//对新增团期的保存
			for(int l=len;l<groups;l++){
				ActivityGroup group = new ActivityGroup();
				groupOpenDate = groupOpenDates[l];
				groupCloseDate = groupCloseDates[l];
				if(groupCloseDate.equals(""))
					groupCloseDate = groupOpenDate;
				String groupCode = groupCodes[l];
				String payedableDate = yingFuDate[l];
				String visaCountry = visaCountrys[l];
				String visaDate = visaDates[l];
				String settlementAdultPrice = settlementAdultPrices[l];
				String settlementcChildPrice = settlementcChildPrices[l];
				String settlementSpecialPrice = "";
				
				//C463団期备注
				String groupRemark = groupRemarks[l];
				group.setGroupRemark(groupRemark);
				
				
				// t1t2 打通  对修改时新增quauq  价的保存  ------------ 开始 -------------
				/*
				 * 对应需求号  0426 t1t2 打通  
				 * 同业价quauq成人
				 */
				if(null!=quauqAdultPrices){
					String quauqAdultPrice = quauqAdultPrices[l];
					if(StringUtils.isNotBlank(quauqAdultPrice)) {
					    group.setQuauqAdultPrice(getBigDecimal(quauqAdultPrice));
					}
				}
				
				/*
				 * 对应需求号  0426 t1t2 打通  
				 * 同业价quauq儿童
				 */
				if(null!=quauqChildPrices){
					String quauqChildPrice = quauqChildPrices[l];
					if(StringUtils.isNotBlank(quauqChildPrice)) {
					   group.setQuauqChildPrice(getBigDecimal(quauqChildPrice));
					}
				}
				
				/*
				 * 对应需求号  0426 t1t2 打通  
				 * 同业价quauq特殊人群
				 */
				if(null!=quauqSpecialPrices){
					String quauqSpecialPrice = quauqSpecialPrices[l];
					if(StringUtils.isNotBlank(quauqSpecialPrice)) {
					   group.setQuauqSpecialPrice(getBigDecimal(quauqSpecialPrice));
					}
				}
				
				// t1t2 打通  对新增quauq  价的保存  ------------- 结束 --------------
				
				
				
				//0258 懿洋假期 发票税
				String invoiceTax = "";
				if(invoiceTaxs!=null){
					invoiceTax = invoiceTaxs[l];
				}
				if(StringUtils.isNotBlank(invoiceTax)){
					group.setInvoiceTax(getBigDecimal(invoiceTax));
				}else{
					group.setInvoiceTax(null);
				}
				
				String priceStr = null;
				if (priceJson==null || priceJson.length <= l) {
					priceStr = "";
				} else {
					priceStr = priceJson[l];
				}
				group.setPriceJson(priceStr);
				// 团期酒店
				String groupHotel = null;
				if (groupHotelStr != null && groupHotelStr.length > l) {
					groupHotel = groupHotelStr[l];
				}
				group.setGroupHotel(groupHotel);
				// 团期房型
				String groupHouseType = null;
				if (groupHouseTypeStr != null && groupHouseTypeStr.length > l) {
					groupHouseType = groupHouseTypeStr[l];
				}
				group.setGroupHouseType(groupHouseType);
				
				//223 为修改时新增的团期设值
				if (null!=cruiseGroupControlIds) {
					String cruiseGroupControlId  = cruiseGroupControlIds[l];
					if (StringUtils.isNotBlank(cruiseGroupControlId)) {
						group.setCruiseshipStockDetailId(Integer.valueOf(cruiseGroupControlId));
					}
				}
				
				
				
				if(!Context.ACTIVITY_KINDS_YL.equals(activityKind)) {
					settlementSpecialPrice = settlementSpecialPrices[l];
				}else{
					String spaceType = request.getParameterValues("spaceType")[l + 1];
					group.setSpaceType(StringUtils.toLong(spaceType));
				}
//				String trekizPrice = trekizPrices[l];
//				String trekizChildPrice = trekizChildPrices[l];
				String suggestAdultPrice = "";
				String suggestChildPrice = "";
				String suggestSpecialPrice = "";

				String adultDiscountPrice = "";
				String childDiscountPrice = "";
				String specialDiscountPrice = "";

				if (Context.ACTIVITY_KINDS_SP.equals(activityKind) 
						&& (Context.SUPPLIER_UUID_YJ.equals(companyUuid) || Context.SUPPLIER_UUID_SHZL.equals(companyUuid))) {
					adultDiscountPrice = adultDiscountPrices[l];
					childDiscountPrice = childDiscountPrices[l];
					specialDiscountPrice = specialDiscountPrices[l];
				}
				if(Context.ACTIVITY_KINDS_SP.equals(activity.getActivityKind().toString()) || Context.ACTIVITY_KINDS_YL.equals(activityKind)) {
					suggestAdultPrice = suggestAdultPrices[l];
					suggestChildPrice = suggestChildPrices[l];
					if(Context.ACTIVITY_KINDS_SP.equals(activity.getActivityKind().toString())) {
						suggestSpecialPrice = suggestSpecialPrices[l];
					}
				}
				String maxPeopleCount = "";
				String maxChildrenCount = "";
				
				if(!activityKind.equals("10"))
				{
					if(maxPeopleCounts[l].trim().equals(""))
						// forbug 最大特殊人数变为0了
						maxPeopleCount="";
					else
						maxPeopleCount = maxPeopleCounts[l];
					if( maxChildrenCounts[l].trim().equals(""))
						// forbug 最大儿童人数变为0了
						maxChildrenCount="";
					else
						maxChildrenCount = maxChildrenCounts[l];
				}
				
				
				String singleDiff = singleDiffs[l];
				String payDeposit = payDeposits[l];
				String planPosition = planPositions[l];
				String freePosition = freePositions[l];
				String currencyType = "";
				if(null != currencyTypes && currencyTypes.length > 0)
					currencyType = currencyTypes[l];
				group.setGroupOpenDate(DateUtils.parseDate(groupOpenDate));
				group.setPayableDate(DateUtils.parseDate(payedableDate));
				group.setGroupCloseDate(DateUtils.parseDate(groupCloseDate));
				group.setVisaCountry(visaCountry);
				group.setVisaDate(DateUtils.parseDate(visaDate));
				
				if(StringUtils.isNotBlank(settlementAdultPrice)) {
					group.setSettlementAdultPrice(getBigDecimal(settlementAdultPrice));
				}
				if(StringUtils.isNotBlank(settlementcChildPrice)) {
					
					group.setSettlementcChildPrice(getBigDecimal(settlementcChildPrice));
				}
				if(StringUtils.isNotBlank(settlementSpecialPrice)) {
					
					group.setSettlementSpecialPrice(getBigDecimal(settlementSpecialPrice));
				}
//				group.setTrekizPrice(StringNumFormat.getIntegerValue(trekizPrice));
//				group.setTrekizChildPrice(StringNumFormat.getIntegerValue(trekizChildPrice));
				if(Context.ACTIVITY_KINDS_SP.equals(activityKind))
				{
					if(StringUtils.isNotBlank(adultDiscountPrice)) {
						group.setAdultDiscountPrice(getBigDecimal(adultDiscountPrice));
					}
					if(StringUtils.isNotBlank(childDiscountPrice)) {
						group.setChildDiscountPrice(getBigDecimal(childDiscountPrice));
					}
					if(StringUtils.isNotBlank(specialDiscountPrice)) {
						group.setSpecialDiscountPrice(getBigDecimal(specialDiscountPrice));
					}
				}

				if(Context.ACTIVITY_KINDS_SP.equals(activityKind) || Context.ACTIVITY_KINDS_YL.equals(activityKind)) {
					if(StringUtils.isNotBlank(suggestAdultPrice)) {
						group.setSuggestAdultPrice(getBigDecimal(suggestAdultPrice));
					}
					if(StringUtils.isNotBlank(suggestChildPrice)) {
						group.setSuggestChildPrice(getBigDecimal(suggestChildPrice));
					}
					if(StringUtils.isNotBlank(suggestSpecialPrice)) {
						group.setSuggestSpecialPrice(getBigDecimal(suggestSpecialPrice));
					}
				}
				if(StringUtils.isNotBlank(maxChildrenCount)) {
					group.setMaxChildrenCount(Integer.valueOf(maxChildrenCount));
				}
				if(StringUtils.isNotBlank(maxPeopleCount)) {
					group.setMaxPeopleCount(Integer.valueOf(maxPeopleCount));
				}
				if(StringUtils.isNotBlank(singleDiff)) {
					group.setSingleDiff(getBigDecimal(singleDiff));
				}
				if(StringUtils.isNotBlank(payDeposit)) {
					group.setPayDeposit(getBigDecimal(payDeposit));
				}
				group.setPlanPosition(StringNumFormat.getIntegerValue(planPosition));
				group.setFreePosition(StringNumFormat.getIntegerValue(freePosition));
				group.setCurrencyType(currencyType);
				group.setVersionNumber(StringUtils.getVersionNumber(request));
				group.setRecommend(Integer.valueOf(tempRecommend[l]));
				group.setTravelActivity(activity);
				//名扬验证团号
				if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
					groupCode = filterCtrlChars(groupCode);
					groupCode = groupCode.replace("\\", "\\\\");
					
					groupCode=activityGroupService.groupCodeCheck(groupCode);
				}
				//青岛凯撒，大洋，拉美图，友创国际 验证团号， 
				if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")//青岛凯撒72
						|| UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")//大洋87
						|| UserUtils.getUser().getCompany().getUuid().contains("7a81a26b77a811e5bc1e000c29cf2586")//拉美图88
						|| UserUtils.getUser().getCompany().getUuid().contains("7a45838277a811e5bc1e000c29cf2586")//友创国际(大唐国旅)
						|| UserUtils.getUser().getCompany().getUuid().contains("ed88f3507ba0422b859e6d7e62161b00")//诚品旅游311
						|| UserUtils.getUser().getCompany().getUuid().contains("f5c8969ee6b845bcbeb5c2b40bac3a23")//懿洋假期
						|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")//非常国际
						|| UserUtils.getUser().getCompany().getUuid().contains("7a81c5d777a811e5bc1e000c29cf2586")//优加国际
						|| UserUtils.getUser().getCompany().getUuid().contains("5c05dfc65cd24c239cd1528e03965021")//起航假期
						|| UserUtils.getUser().getCompany().getGroupCodeRuleDT()==0){ //对应需求号  c460
					if(activityGroupService.groupNoCheck(groupCode)){
						return "modules/activity/activityDuplicatedGroupcode";
					}
				}
				group.setGroupCode(groupCode);
				
				activity.getActivityGroups().add(group);
			}
		
		}
		return "";
	}
	
	/**
	 * 保存页面传递文件
	 * @param activity
	 * @param request
	 * @throws IOException
	 */
	private void saveFile(TravelActivity activity, HttpServletRequest request) throws Exception {

		Set<ActivityFile> files = activity.getActivityFiles();
		//旧文件Id
		List<String> inrtoductionOldFiles = new ArrayList<String>();
		List<String> costagreementOldFiles = new ArrayList<String>();
		List<String> otheragreementOldFiles = new ArrayList<String>();
		List<String> otherFilesOldFiles = new ArrayList<String>();
		// QU-SDP-微信分销模块start 微信分销广告图片保存 yang.gao 2017-01-06
		List<String> distributionAdOldFiles = new ArrayList<String>();
		// QU-SDP-微信分销模块end
		for(ActivityFile file : files) {
			if(ActivityFile.INTRODUCTION_TYPE.equals(file.getFileType()))
				inrtoductionOldFiles.add(file.getSrcDocId().toString());
			else if(ActivityFile.COSTAGREEMENT_TYPE.equals(file.getFileType()))
				costagreementOldFiles.add(file.getSrcDocId().toString());
			else if(ActivityFile.OTHERAGREEMENT_TYPE.equals(file.getFileType()))
				otheragreementOldFiles.add(file.getSrcDocId().toString());
			else if(ActivityFile.OTHER_TYPE.equals(file.getFileType()))
				otherFilesOldFiles.add(file.getSrcDocId().toString());
			// QU-SDP-微信分销模块start 微信分销广告图片保存 yang.gao 2017-01-06
			else if(ActivityFile.DISTRIBUTION_TYPE.equals(file.getFileType()))
				distributionAdOldFiles.add(file.getSrcDocId().toString());
			// QU-SDP-微信分销模块end
		}
		
		//行程介绍
		String[] inrtoductionFilesArr = request.getParameterValues("introduction");
		List<String> introductionFiles = new ArrayList<String>();
		if(inrtoductionFilesArr != null) {
			introductionFiles = Arrays.asList(inrtoductionFilesArr);
		}
		
		//自费补充协议
		String[] costagreementFilesArr = request.getParameterValues("costagreement");
		List<String> costagreementFiles = new ArrayList<String>();
		if(costagreementFilesArr != null) {
			costagreementFiles = Arrays.asList(costagreementFilesArr);
		}
		
		//其他补充协议
		String[] otheragreementFilesArr = request.getParameterValues("otheragreement");
		List<String> otheragreementFiles = new ArrayList<String>();
		if(otheragreementFilesArr != null) {
			otheragreementFiles = Arrays.asList(otheragreementFilesArr);
		}
		
		//其他文件
		String[] otherFilesArr = request.getParameterValues("otherFiles");
		List<String> otherFiles = new ArrayList<String>();
		if(otherFilesArr != null) {
			otherFiles = Arrays.asList(otherFilesArr);
		}
		
		// QU-SDP-微信分销模块start 微信分销广告图片保存 yang.gao 2017-01-06
		// 微信分销广告图片
//		String[] distributionAdImgsArr = request.getParameterValues("distributionAdImg");
//		List<String> distributionAdImgs = new ArrayList<String>();
//		if(distributionAdImgsArr != null) {
//			distributionAdImgs = Arrays.asList(distributionAdImgsArr);
//		}
		// QU-SDP-微信分销模块end
		
		ActivityFile activityFile;
		
		//行程介绍
		//如果新组中有不包含旧组的值，说明是删除的
		for(String oldFiles : inrtoductionOldFiles) {
			if(!introductionFiles.contains(oldFiles)) {
				ActivityFile tmp = activityFileDao.findBySrcActivityIdAndSrcDocId(Integer.valueOf(activity.getId().toString()), Integer.valueOf(oldFiles));
				activity.getActivityFiles().remove(tmp);
				activityFileDao.delActivityFileById(tmp.getId());
				if (tmp.getDocInfo() != null) {
					docInfoDao.delDocInfoById(tmp.getDocInfo().getId());
				}
			}
		}
		//如果旧组中有不包含新组的值，说明是新增的
		for(String newFiles : introductionFiles) {
			if(!inrtoductionOldFiles.contains(newFiles)) {
				activityFile = new ActivityFile();
				activityFile.setFileType(ActivityFile.INTRODUCTION_TYPE);
				activityFile.setFileName(request.getParameter("introduction_name"));
				activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(newFiles)));
				activityFile.setTravelActivity(activity);
				activity.getActivityFiles().add(activityFile);
			}
		}
			
		    
		//自费补充协议
		//如果新组中有不包含旧组的值，说明是删除的
		for(String oldFiles : costagreementOldFiles) {
			if(!costagreementFiles.contains(oldFiles)) {
				ActivityFile tmp = activityFileDao.findBySrcActivityIdAndSrcDocId(Integer.valueOf(activity.getId().toString()), Integer.valueOf(oldFiles));
				activity.getActivityFiles().remove(tmp);
				activityFileDao.delActivityFileById(tmp.getId());
				docInfoDao.delDocInfoById(tmp.getDocInfo().getId());
			}
		}
		//如果旧组中有不包含新组的值，说明是新增的
		for(String newFiles : costagreementFiles) {
			if(!costagreementOldFiles.contains(newFiles)) {
				activityFile = new ActivityFile();
				activityFile.setFileType(ActivityFile.COSTAGREEMENT_TYPE);
				activityFile.setFileName(request.getParameter("costagreement_name"));
				activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(newFiles)));
				activityFile.setTravelActivity(activity);
				activity.getActivityFiles().add(activityFile);
			}
		}
						
		//其他补充协议
		//如果新组中有不包含旧组的值，说明是删除的
		for(String oldFiles : otheragreementOldFiles) {
			if(!otheragreementFiles.contains(oldFiles)) {
				ActivityFile tmp = activityFileDao.findBySrcActivityIdAndSrcDocId(Integer.valueOf(activity.getId().toString()), Integer.valueOf(oldFiles));
				activity.getActivityFiles().remove(tmp);
				activityFileDao.delActivityFileById(tmp.getId());
				docInfoDao.delDocInfoById(tmp.getDocInfo().getId());
			}
		}
		//如果旧组中有不包含新组的值，说明是新增的
		for(String newFiles : otheragreementFiles) {
			if(!otheragreementOldFiles.contains(newFiles)) {
				activityFile = new ActivityFile();
				activityFile.setFileType(ActivityFile.OTHERAGREEMENT_TYPE);
				activityFile.setFileName(request.getParameter("otheragreement_name"));
				activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(newFiles)));
				activityFile.setTravelActivity(activity);
				activity.getActivityFiles().add(activityFile);
			}
		}
	
		//其他文件
		//如果新组中有不包含旧组的值，说明是删除的
		for(String oldFiles : otherFilesOldFiles) {
			if(!otherFiles.contains(oldFiles)) {
				ActivityFile tmp = activityFileDao.findBySrcActivityIdAndSrcDocId(Integer.valueOf(activity.getId().toString()), Integer.valueOf(oldFiles));
				activity.getActivityFiles().remove(tmp);
				activityFileDao.delActivityFileById(tmp.getId());
				docInfoDao.delDocInfoById(tmp.getDocInfo().getId());
			}
		}
		
		//如果旧组中有不包含新组的值，说明是新增的
		for(String newFiles : otherFiles) {
			if(!otherFilesOldFiles.contains(newFiles)) {
				activityFile = new ActivityFile();
				activityFile.setFileType(ActivityFile.OTHER_TYPE);
				activityFile.setFileName(request.getParameter("otherFile_name"));
				activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(newFiles)));
				activityFile.setTravelActivity(activity);
				activity.getActivityFiles().add(activityFile);
			}
		}
		
		// QU-SDP-微信分销模块start 微信分销广告图片保存 yang.gao 2017-01-06
		// 散拼并且拥有批发商计调才做此操作
		// 如果是批发商计调则返回true
		boolean requiredStraightPrice = SecurityUtils.getSubject().isPermitted("looseProduct:operation:requiredStraightPrice");
		if (Context.ACTIVITY_KINDS_SP.equals(activity.getActivityKind().toString()) && requiredStraightPrice) {
			// 微信分销广告图片
			// 如果新组中有不包含旧组的值，说明是删除的
			for(String oldFiles : distributionAdOldFiles) {
				ActivityFile tmp = activityFileDao.findBySrcActivityIdAndSrcDocId(Integer.valueOf(activity.getId().toString()), Integer.valueOf(oldFiles));
				activity.getActivityFiles().remove(tmp);
				activityFileDao.delActivityFileById(tmp.getId());
				docInfoDao.delDocInfoById(tmp.getDocInfo().getId());
			}
			
			// 如果旧组中有不包含新组的值，说明是新增的
//			for(String newFiles : distributionAdImgs) {
//				if(!otherFilesOldFiles.contains(newFiles)) {
//					activityFile = new ActivityFile();
//					activityFile.setFileType(ActivityFile.DISTRIBUTION_TYPE);
//					activityFile.setFileName(request.getParameter("distributionAdImg_name"));
//					activityFile.setDocInfo(docInfoDao.findOne(StringUtils.toLong(newFiles)));
//					activityFile.setTravelActivity(activity);
//					activity.getActivityFiles().add(activityFile);
//				}
//			}
			
//			if (distributionAdImgsArr == null) {
//				DocInfo doc = uploadDefaultFile(request, Context.DEFAULT_IMG_PATH);
//              int docIndex = doc.getDocName().lastIndexOf('.');
//              String fileName = doc.getDocName().substring(0, docIndex); // 文件名称
	    		// 产品和文件中间表保存
//              activityFile = new ActivityFile();
//	    		activityFile.setFileType(ActivityFile.DISTRIBUTION_TYPE);
//	    		activityFile.setFileName(fileName); // 文件名称
//	    		activityFile.setDocInfo(doc);
//	    		activityFile.setDelFlag("0");
//	    		activityFile.setTravelActivity(activity);
//	    		activityFileDao.save(activityFile);
	    		String docId = request.getParameter("docId");
				DocInfo docinfo = docInfoDao.findOne(StringUtils.toLong(docId));
                int docIndex = docinfo.getDocName().lastIndexOf('.'); 
                String fileName = docinfo.getDocName().substring(0, docIndex); // 文件名称
	    		activityFile = new ActivityFile();
	    		activityFile.setFileType(ActivityFile.DISTRIBUTION_TYPE);
	    		activityFile.setFileName(fileName);
	    		activityFile.setDocInfo(docinfo);
	    		activityFile.setDelFlag("0");
	    		activityFile.setTravelActivity(activity);
	    		activityFileDao.save(activityFile);
//			}
		}
		// QU-SDP-微信分销模块end
	}
	
	/**
	 * 修改签证信息
	 * @param activity
	 * @param request
	 */
	@SuppressWarnings("unused")
	private void modVise(TravelActivity activity, HttpServletRequest request) {
		//获得之前上传的签证文件
		List<Activityvisafile> visFileList = visaService.findVisaFileByProid(activity.getId());
		//签证资料
		String countrys[] = request.getParameterValues("country");
		String visaTypes[] = request.getParameterValues("visaType");
		String fileGroups[] = request.getParameterValues("fileGroup");
		String docIds[] = request.getParameterValues("docId");
		Map<String,Integer> visaMap = new HashMap<String, Integer>();
		if(countrys != null) {
			for(int k=0;k<countrys.length;k++) {
				String country = countrys[k];
				String visaType = visaTypes[k];
				String docInfoId = docIds[k];
				if(StringUtils.isNotBlank(country) && StringUtils.isNotBlank(visaType))
					visaMap.put(country + "_" + visaType + "_" + docInfoId + "_" + k, k);
			}
			if(visFileList != null && visFileList.size() != 0) {
				Set<Long> idSet = new HashSet<Long>();
				for(Activityvisafile visa : visFileList) {					
					idSet.add(visa.getId());
				}
				visaService.delVisaFileByIds(idSet);
			}
			//根据国家ID在国家签证表中查询签证文档名称和路径保存到文档表中(docInfo)
			if(countrys.length != 0) {
				for(int i=0;i<countrys.length;i++) {
					if(!StringUtils.isBlank(countrys[i])) {
						String countryId = countrys[i];
						String visaType = visaTypes[i];
						String groupCount = fileGroups[i];
						String[] signmaterialArr = request.getParameterValues("signmaterial" + groupCount);
						visaService.uploadActivityVisaData(countryId, visaType, signmaterialArr, Long.parseLong(activity.getId().toString()));
					}
				}
			}
		} else {
			//如果修改时签证全部删除 则数据表的签证资料也全部删除
			visaService.delVisaFileByProid(activity.getId());
		}
	}
	
	private void saveVisaFile(TravelActivity activity, HttpServletRequest request) throws Exception {
		
		//上传的签证资料
		String countrys[] = request.getParameterValues("country");
		String visaTypes[] = request.getParameterValues("visaType");
		String fileGroups[] = request.getParameterValues("fileGroup");
		
		//旧的签证文件关联全部删除
		visaService.delVisaFileByProid(activity.getId());
		
		Map<String,Integer> visaMap = new HashMap<String, Integer>();
		int len = 0;
		if(countrys != null){
			len = countrys.length;
			for(int k=0;k<len;k++){
				String country = countrys[k];
				String visaType = visaTypes[k];
				
				if(StringUtils.isNotBlank(country) && StringUtils.isNotBlank(visaType)){
					visaMap.put(country+"_"+visaType + "_" + k, k);
				}
			}
			Iterator<Entry<String, Integer>> ite = visaMap.entrySet().iterator();
			while(ite.hasNext()){
				Entry<String, Integer> entry = ite.next();
				String key = entry.getKey().toString();
				int flag = Integer.parseInt(entry.getValue().toString());
				String groupCount = fileGroups[flag];
				String[] signmaterialArr = request.getParameterValues("signmaterial" + groupCount);
				if(signmaterialArr != null){
					String countryId = key.split("_")[0];
					String visaType = key.split("_")[1];
					visaService.uploadActivityVisaData(countryId, visaType, signmaterialArr, Long.parseLong(activity.getId().toString()));
				}
			}
		}
	}
	
private void saveVisaFile4Save(TravelActivity activity, HttpServletRequest request) throws Exception {
		
		//上传的签证资料
		String countrys[] = request.getParameterValues("country");
		String visaTypes[] = request.getParameterValues("visaType");
		String fileGroups[] = request.getParameterValues("fileGroup");
		if(fileGroups!=null){
			String signmaterialArr1[] = request.getParameterValues("signmaterial" + fileGroups[0]);
			//旧的签证文件关联全部删除
			visaService.delVisaFileByProid(activity.getId());
			
			Map<String,Integer> visaMap = new HashMap<String, Integer>();
			int len = 0;
			if(countrys != null){
				len = countrys.length;
				for(int k=0;k<len;k++){
					String country = countrys[k];
					String visaType = visaTypes[k];
					
					if(StringUtils.isNotBlank(country) && StringUtils.isNotBlank(visaType)){
						if(null != signmaterialArr1) {
							String docid = signmaterialArr1[k];
							visaMap.put(country + "_" + visaType + "_" + docid + "_" + k, k);
						}
					}
				}
				Iterator<Entry<String, Integer>> ite = visaMap.entrySet().iterator();
				while(ite.hasNext()){
					Entry<String, Integer> entry = ite.next();
					String key = entry.getKey().toString();
					int flag = Integer.parseInt(entry.getValue().toString());
					String groupCount = fileGroups[flag];
					String[] signmaterialArr = request.getParameterValues("signmaterial" + groupCount);
					if(signmaterialArr != null){
						String countryId = key.split("_")[0];
						String visaType = key.split("_")[1];
						String docId = key.split("_")[2];
						visaService.uploadActivityVisaData4Save(countryId, visaType,docId, signmaterialArr, Long.parseLong(activity.getId().toString()));
					}
				}
			}
		}

	}
	
	/**
	 * 删除文件和产品团期
	 * @param activity
	 * @param request
	 * @throws Exception
	 */
	private void deleteFileAndGroup(TravelActivity activity, HttpServletRequest request) throws Exception {
		
		//删除其他文件
		String delotherfile_ids = request.getParameter("delOtherFileIds");
		travelActivityService.save(activity);
		List<Long> ids = Lists.newArrayList();
		if(StringUtils.isNotBlank(delotherfile_ids)){
			String fileids[] = delotherfile_ids.split(",");
			for(String id:fileids){
				if(StringUtils.isNotEmpty(id)){
					ids.add(Long.parseLong(id));
				}
			}
		}
		if(ids.size()!=0)
			activityFileDao.delActivityFileByIds(ids);
		
		//删除现有的团期
		String delGroupIds = request.getParameter("delGroupIds");
		ids = Lists.newArrayList();
		ids = Lists.newArrayList();
		if(StringUtils.isNotBlank(delGroupIds)){
			String delIds[] = delGroupIds.split(",");
			for(String id:delIds){
				if(StringUtils.isNotBlank(id))
					ids.add(Long.parseLong(id));
			}
		}
		for(Long id : ids) {
			Iterator<ActivityGroup> it = activity.getActivityGroups().iterator();
			while(it.hasNext()) {
				ActivityGroup group = it.next();
				if(group.getId().longValue() == id.longValue()) {
					it.remove();
					break;
				}
			}
		}
		if(ids.size() != 0){
			activityGroupService.delGroupsByIds(ids);
			
			/**
			 *  对应需求号223      
			 *  删除的订单  要  把期绑定的  团控表设置为取消状态
			 * 
			 */
			for (Long long1 : ids) {
				//1.2更新  团控关联记录
				CruiseshipStockGroupRel cruiseshipStockGroupRelOld = cruiseshipStockGroupRelDao.getCruiseShipRelByActivityGroupId(long1.intValue());
				if (null!=cruiseshipStockGroupRelOld) {
					cruiseshipStockGroupRelOld.setStatus(1);
					cruiseshipStockGroupRelOld.setDelFlag("1");
					cruiseshipStockGroupRelService.save(cruiseshipStockGroupRelOld);
				}
			}
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.ITravelActivityService#batchOnActivityTmp(java.util.List)
	 */
	@Override
    public void batchOnActivityTmp(List<Long> ids) {
		if(ids == null || ids.isEmpty()){
			throw new IllegalArgumentException("产品ids为空");
		}
		String statusStr = "";
		//正向平台产品状态：草稿为1；上架为2；下架为3
		//同步接口上架为1；下架为2
		statusStr = "上架";
		Set<TravelActivity> activitSet = new HashSet<TravelActivity>();
		for(Long id : ids) {
			TravelActivity activity = this.findById(id);
			activitSet.add(activity);
		}
		
		//批量保存到数据库和同步
		for(TravelActivity activity : activitSet) {
			try {
				activity.setActivityStatus(Integer.valueOf(Context.PRODUCT_ONLINE_STATUS));
				this.save(activity);
			} catch (Exception e) {
				throw new RuntimeException("批量" + statusStr + "产品失败");
			}
		}
	}

	@Override
    public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page,
            TravelActivity travelActivity, String settlementAdultPriceStart,
            String settlementAdultPriceEnd, List<Integer> state) {
	    return travelActivityService.findTravelActivity(page, travelActivity, 
	    		settlementAdultPriceStart, settlementAdultPriceEnd, state);
    }

	@Override
	public Page<TravelActivity> findTravelActivity(Page<TravelActivity> page, TravelActivity travelActivity,
			DepartmentCommon common, Map<String,String> mapRequest) {
		return travelActivityService.findTravelActivity(page, travelActivity, common, mapRequest);
	}
	
	@Override
	public Page<ActivityGroup> findActivityGroup(Page<ActivityGroup> page, TravelActivity travelActivity,
			DepartmentCommon common, Map<String,String> mapRequest) {
		return travelActivityService.findActivityGroup(page, travelActivity, common, mapRequest);
	}
	
	public List<ActivityGroup> findGroupsByActivityId(TravelActivity travelActivity, Map<String,String> map) {
		return travelActivityService.findGroupsByActivityId(travelActivity, map);
	}

	@Override
	public List<Map<String, Object>> findCountryAreaIds(Long companyId) {
		// TODO Auto-generated method stub
		String sql = "select sa.id from sys_area sa inner join userdefinedict u  on sa.id = u.dictId where sa.type='2' and   u.companyId='"+companyId+"' and u.type='area'";
		return travelActivityDao.findBySql(sql, Map.class);
	}
	
	/**
	 * 查找团号生成规则(新行者需求)
	 * @author jiachen
	 * @DateTime 2015年3月5日 下午12:02:37
	 * @return List<Object[]>
	 */
	@Override
	public List<Object[]> findGroupCodeRule() {
		Long companyId = UserUtils.getUser().getCompany().getId();
		String sql = "select id, code from group_code_rule where companyId = " + companyId +"";
		return travelActivityDao.findBySql(sql);
	}
	
	public List<ActivityGroup> getYwByGroupIds(List<Long> groupIdList) {
		return travelActivityDao.findYwByGroupIds(groupIdList);
	}

	@Override
	public Integer getAllLeftpayReservePosition(Long activityGroupId,
			Long activityId) {
		List<ActivityGroupReserve> activityGroupReserveList = activityGroupReserveDao.findByActivityGroupIdAndSrcActivityId(activityGroupId, activityId);
		Integer totalNum = 0;
		for (ActivityGroupReserve activityGroupReserve : activityGroupReserveList) {
			Integer leftpayReservePosition = activityGroupReserve.getLeftpayReservePosition();
			totalNum += leftpayReservePosition;
		}
		return totalNum;
	}


	@Override
	public void delCruiseshipStockGrooupRelByActivityIds(List<Long> idlist) {
		for (Long id : idlist) {
			try {
				travelActivityDao.updateBySql("update cruiseship_stock_group_rel set STATUS=1 where activity_id=?", id);
				//System.out.println("----"+result+"----"+id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

    /**
     * c460--下架取消关联状态
     * 通过团期id更新表cruiseship_stock_group_rel的关联状态
     */
	@Override
	public void updateCruiseshipRelStatusByActivityId(List<Long> idlist) {
		for (Long id : idlist) {
			try {
				travelActivityDao.updateBySql("update cruiseship_stock_group_rel csgr set csgr.STATUS=1 where csgr.activity_id= "+id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Map<String, Object>> findAreaIdsEndCountry(Long companyId) {
		return travelActivityService.findAreaIdsEndCountry(companyId);
	}
	
	@Override
	public Map<String, Object> getDetail(Long activityId, String groupCode) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ")
				.append("t.id activityId, ")
				.append("g.groupOpenDate, ")
				.append("g.freePosition, ")
				.append("t.acitivityName activityName, ")
				.append("g.id groupId, ")
				.append("g.groupCode, ")
				.append("t.fromArea, ")
				.append("t.activityDuration, ")
				.append("t.trafficMode, ")
				.append("g.currency_type currencyIds, ")
				.append("g.settlementAdultPrice settlementAdultPrice, ")
				.append("g.settlementcChildPrice settlementcChildPrice, ")
				.append("g.settlementSpecialPrice settlementSpecialPrice, ")
				.append("g.suggestAdultPrice suggestAdultPrice, ")
				.append("g.suggestChildPrice suggestChildPrice, ")
				.append("g.suggestSpecialPrice suggestSpecialPrice, ")
				.append("g.quauqAdultPrice quauqAdultPrice, ")
				.append("g.quauqChildPrice quauqChildPrice, ")
				.append("g.quauqSpecialPrice quauqSpecialPrice ")
				.append("FROM ")
				.append("travelactivity t, ")
				.append("activitygroup g ")
				.append("WHERE ")
				.append("t.id = g.srcActivityId ")
				.append("AND t.id = ? AND g.groupCode = ?");
		List<Map<String, Object>> list = travelActivityDao.findBySql(sb.toString(), Map.class, activityId, groupCode);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}


	/**
	 * 产品 quauq 价格策略匹配
	 * @author xinwei.wang@quauq.com
	 * @param bigareaList: (境内外跨省，跨国大区ID(策略路线))
	 * @param fromArea : 出发城市
	 * @param travelTypeId :旅游类型
	 * @param activityTypeId :产品类型
	 * @param activityLevelId :产品系列
	 * @return
	 */
	private String getCheckActivityPriceStrategy(List<String> bigareaList,String fromArea,String travelTypeId,String activityTypeId,String activityLevelId){
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<PriceStrategy> priceStrategyList = priceStrategyDao.getAllPriceStrategyByWholeSalerId(companyId);
		Set<AgentPriceStrategy> agentPriceStrategies = new HashSet<AgentPriceStrategy>();
		
		for (PriceStrategy priceStrategy : priceStrategyList) {
			//出发城市
			String fromAreaIds  = priceStrategy.getFromAreaIds();
			boolean isfromAreaMatched = matchSingleStrategy(fromAreaIds,fromArea);
			if (!isfromAreaMatched) {
				continue;
			}
			
			//目的地
			String targetAreaIds  = priceStrategy.getTargetAreaIds();//线路iD
			boolean istargetAreaIdsMatched = matchSingleStrategy4targetAreaNew(targetAreaIds,bigareaList);
			if (!istargetAreaIdsMatched) {
				continue;
			}
			
			//旅游类型
			String travelTypeIds  = priceStrategy.getTravelTypeIds();
			boolean istravelTypeIdMatched = matchSingleStrategy(travelTypeIds,travelTypeId);
			if (!istravelTypeIdMatched) {
				continue;
			}
			
			//产品类型
			String activityTypeIds  = priceStrategy.getActivityTypeIds();
			boolean isactivityTypeIdMatched = matchSingleStrategy(activityTypeIds,activityTypeId);
			if (!isactivityTypeIdMatched) {
				continue;
			}
			
			//产品系列
			String activityLevelIds  = priceStrategy.getProductLevelIds();
			boolean isfactivityLevelIdMatched = matchSingleStrategy(activityLevelIds,activityLevelId);
			if (!isfactivityLevelIdMatched) {
				continue;
			}
			
			//匹配成功  则  获取具体的  价格策略
			if (isfromAreaMatched && istargetAreaIdsMatched && istravelTypeIdMatched && isactivityTypeIdMatched && isfactivityLevelIdMatched) {
				
				// TODO  获取具体的  价格策略  ????
				//Set<AgentPriceStrategy> agentPriceStrategies = priceStrategy.getAgentPriceStrategySet();
				
				Set<AgentPriceStrategy> agentPriceStrategiesTemp = priceStrategy.getAgentPriceStrategySet();
				agentPriceStrategies.addAll(agentPriceStrategiesTemp);
				
				//String priceStrategyStr = getPriceStrategyByAgentPriceStategySet(agentPriceStrategies);
				//return priceStrategyStr;
				
			}
			
		}
		
		if (agentPriceStrategies.size()>0) {
			
			//String priceStrategyStr = getPriceStrategyByAgentPriceStategySet(agentPriceStrategies);
			String priceStrategyStr = getPriceStrategyByAgentPriceStategySetS(agentPriceStrategies);
			
			return priceStrategyStr;
		}
		
		return "0";
		
	}
	
	
	
	/**
	 * 根据价格策略获取  最低价格策略
	 * 
	 * 数据组织说明：
	 * 1.一个产品可能匹配到 price_strategy 表中的多条记录，即多条价格策略：只要价格策略中有一条线路与产品的选择一致  就视为匹配成功。
	 * 2.一条价格策略对应多条价格方案，每个方案中有  成人、儿童 和 特殊人群
	 * 3.页面中要根据所有的价格方案  计算价格策略，最后  的  成人、儿童 和 特殊人群 最低价  即为 产品的相应  quauq 价
	 * 4.数据的具体组织格式  策略适用对象（ 成人、儿童 和 特殊人群）与具体的价格方案用  ## 间隔，价格方案之间用  # 间隔， 成人、儿童 和 特殊人群 间用   ### 间隔，具体如下：
	 *   
	 *  quauqPrice4Adult##2:80,1:20#3:80,2:20#2:80,1:20
	 *  ###quauqPrice4Child##2:80,1:20#3:80,2:20#2:80,1:20
	 *  ###quauqPrice4SpicalPerson##2:80,1:20#3:80,2:20#2:80,1:20
	 * 
	 * @author xinwei.wang@quauq.com
	 * @param agentPriceStrategies
	 * @return
	 */
	private String getPriceStrategyByAgentPriceStategySetS(Set<AgentPriceStrategy> agentPriceStrategies){
	
		StringBuffer adultPriceStrategyVal = new StringBuffer("quauqPrice4Adult##");
		StringBuffer childrenPriceStrategyVal = new StringBuffer("quauqPrice4Child##");
		StringBuffer specialPriceStrategyVal = new StringBuffer("quauqPrice4SpicalPerson##");
		
		if (null!=agentPriceStrategies && agentPriceStrategies.size()>0) {
			
			/**
			 * 拼接  成人、儿童、特殊人群 的具体价格策略
			 */
			for (AgentPriceStrategy agentPriceStrategy : agentPriceStrategies) {
				
				//成人
				String quauqPrice4Adult = "";
				if(StringUtils.isNotBlank(agentPriceStrategy.getAdultPriceStrategy())){
					quauqPrice4Adult = agentPriceStrategy.getAdultPriceStrategy().replaceAll(",$", "").replaceFirst(",", "");
				}
				if (!"".equals(quauqPrice4Adult)) {
					adultPriceStrategyVal.append(quauqPrice4Adult).append("#");
				}
				
				//儿童	
				String quauqPrice4Child = "";
				if(StringUtils.isNotBlank(agentPriceStrategy.getChildrenPriceStrategy())){
					quauqPrice4Child = agentPriceStrategy.getChildrenPriceStrategy().replaceAll(",$", "").replaceFirst(",", "");
				}
				if (!"".equals(quauqPrice4Child)) {
					childrenPriceStrategyVal.append(quauqPrice4Child).append("#");
				}
					
				//特殊人群
				String quauqPrice4SpicalPerson = "";
				if(StringUtils.isNotBlank(agentPriceStrategy.getSpecialPriceStrategy())){
					quauqPrice4SpicalPerson = agentPriceStrategy.getSpecialPriceStrategy().replaceAll(",$", "").replaceFirst(",", "");
				}
				if (!"".equals(quauqPrice4SpicalPerson)) {
					specialPriceStrategyVal.append(quauqPrice4SpicalPerson).append("#");
				}
				
			}
			
			StringBuffer resultStrBuffer = new StringBuffer("");
			if (!"quauqPrice4Adult##".equals(adultPriceStrategyVal.toString())) {
				resultStrBuffer.append(adultPriceStrategyVal.toString().replaceAll("#$",""));
			}else{
				resultStrBuffer.append(adultPriceStrategyVal.toString());
			}
			
			if (!"quauqPrice4Child##".equals(childrenPriceStrategyVal.toString())) {
				resultStrBuffer.append("#Q#").append(childrenPriceStrategyVal.toString().replaceAll("#$",""));
			}else{
				resultStrBuffer.append("#Q#").append(childrenPriceStrategyVal.toString());
			}
			
			if (!"quauqPrice4SpicalPerson##".equals(specialPriceStrategyVal.toString())) {
				resultStrBuffer.append("#Q#").append(specialPriceStrategyVal.toString().replaceAll("#$",""));
			}else{
				resultStrBuffer.append("#Q#").append(specialPriceStrategyVal.toString());
			}
			
			String resultStr = resultStrBuffer.toString();
			return resultStr;
			
		}else{
			return "0";
		}
	}
	
	
	/**
	 * check策略单项匹配:
	 * 适用于：除路线(目的地)外的其它四项（出发城市、旅游类型，产品类型，产品系列）
	 * 业务逻辑：
	 * 1.如果srcmatch为空 直接匹配失败
	 * 2.如果singleStrategyId为空   则 看  srcmatch 中是否存在 ‘-1’，如果有就算匹配到
	 * @param srcmatch: 价格策略中的ID
	 * @param singleStrategyId:产品的基本匹配条件ID
	 * @return
	 */
	private boolean matchSingleStrategy(String srcmatch,String singleStrategyId){
		boolean flag = false;
		if(StringUtils.isNoneBlank(srcmatch)){
			String[] srcmatchArray = srcmatch.split(",");
			List<String> srcmatchList = Arrays.asList(srcmatchArray);
			if(StringUtils.isNotBlank(singleStrategyId)){
				if (srcmatchList.contains(singleStrategyId)) {
					flag = true;
				}
			}else{
				if (srcmatchList.contains("-1")) {
					flag = true;
				}
			}
		}
		return flag;
	}
	
	
	/**
	 * check策略单项匹配4目的地（即策略中的线路）:
	 * 适用于：路线(产品中的目的地)
	 * 业务逻辑：
	 * 1.如果srcmatch为空 直接匹配失败
	 * 2.如果bigareaList为空就算  单项匹配  成功
	 * @param srcmatch: 价格策略中的IDS(price_strategy中的字段targetAreaIds 关联线路表price_strategy_line 的id)
	 * @param bigareaList:产品的基本匹配条件IDS(目的地)
	 * @return
	 */
	private boolean matchSingleStrategy4targetAreaNew(String srcmatch,List<String> bigareaList){
		boolean flag = false;
		//如果产品没有选择目的地，  视为匹配成功
		if(bigareaList==null||bigareaList.size()<0){
			return flag;
		}
		
		if(StringUtils.isNoneBlank(srcmatch)){
			String[] srcmatchArray = srcmatch.replaceAll(",$", "").replaceFirst(",", "").split(",");
			for (int i = 0; i < srcmatchArray.length; i++) {
				TouristLine priceStrategyLine =  priceStrategyLineService.getById(Long.parseLong(srcmatchArray[i])); 
				if (null!=priceStrategyLine) {
					String[] areasinLine = priceStrategyLine.getAreaIds().replaceAll(",$", "").split(",");
					List<String> areasinLineMatchList = Arrays.asList(areasinLine);
					int tempcount = bigareaList.size();
					for (String targetAreaID : bigareaList) {
						if (areasinLineMatchList.contains(targetAreaID)) {
							tempcount = tempcount-1;
							if (0==tempcount) {
								break;
							}
						}
					}
					/**
					 * 产品选择的目的地所对应的区域id，如果与某条线路 一样 则 视为 匹配成功
					 */
					if (0==tempcount&&bigareaList.size()==areasinLineMatchList.size()) {
						flag = true;
						break;
					}
				}
				 
			}
			
		}
		return flag;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * check策略单项匹配4目的地（即策略中的线路）:
	 * 适用于：路线(产品中的目的地)
	 * 业务逻辑：
	 * 1.如果srcmatch为空 直接匹配失败
	 * 2.如果bigareaList为空就算  单项匹配  成功
	 * @param srcmatch: 价格策略中的ID(线路)
	 * @param bigareaList:产品的基本匹配条件IDS(目的地)
	 * @return
	 */
	private boolean matchSingleStrategy4targetArea(String srcmatch,List<String> bigareaList){
		boolean flag = false;
		if(StringUtils.isNoneBlank(srcmatch)){
			String[] srcmatchArray = srcmatch.split(",");
			List<String> srcmatchList = Arrays.asList(srcmatchArray);
			if(bigareaList!=null&&bigareaList.size()>0){
				int tempcount = bigareaList.size();
				for (String targetAreaID : bigareaList) {
					if (srcmatchList.contains(targetAreaID)) {
						tempcount = tempcount-1;
						if (0==tempcount) {
							break;
						}
					}
				}
				if (0==tempcount) {
					flag = true;
				}
			}else{
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 根据价格策略获取  最低价格策略
	 * @author xinwei.wang@quauq.com
	 * @param agentPriceStrategies
	 * @return
	 */
	private String getPriceStrategyByAgentPriceStategySet(Set<AgentPriceStrategy> agentPriceStrategies){
		Long adultPriceStrategyCurrentId = null;
		Long childrenPriceStrategyCurrentId = null;
		Long specialPriceStrategyCurrentId = null;
		
		Double adultPriceStrategyCurrentVal = null;
		Double childrenPriceStrategyCurrentVal = null;
		Double specialPriceStrategyCurrentVal = null;
		
		if (null!=agentPriceStrategies && agentPriceStrategies.size()>0) {
			for (AgentPriceStrategy agentPriceStrategy : agentPriceStrategies) {
				
				//成人
				String adultPriceStrategy  = agentPriceStrategy.getAdultPriceStrategy();
				Double afterUsedPriceStrategy4adult = calcPriceStrategy(adultPriceStrategy);
				if (null==adultPriceStrategyCurrentVal) {
					adultPriceStrategyCurrentVal = afterUsedPriceStrategy4adult;
					adultPriceStrategyCurrentId = agentPriceStrategy.getId();
				}else {
					if (afterUsedPriceStrategy4adult<adultPriceStrategyCurrentVal) {
						adultPriceStrategyCurrentVal = afterUsedPriceStrategy4adult;
						adultPriceStrategyCurrentId = agentPriceStrategy.getId();
					}
				}
				
				//儿童
				String childrenPriceStrategy  = agentPriceStrategy.getChildrenPriceStrategy();
				Double afterUsedPriceStrategy4children = calcPriceStrategy(childrenPriceStrategy);
				if (null==childrenPriceStrategyCurrentVal) {
					childrenPriceStrategyCurrentVal = afterUsedPriceStrategy4children;
					childrenPriceStrategyCurrentId = agentPriceStrategy.getId();
				}else {
					if (afterUsedPriceStrategy4children<childrenPriceStrategyCurrentVal) {
						childrenPriceStrategyCurrentVal = afterUsedPriceStrategy4children;
						childrenPriceStrategyCurrentId = agentPriceStrategy.getId();
					}
				}
				
				//特殊人群
				String specialPriceStrategy  = agentPriceStrategy.getSpecialPriceStrategy();
				Double afterUsedPriceStrategy4special = calcPriceStrategy(specialPriceStrategy);
				if (null==specialPriceStrategyCurrentVal) {
					specialPriceStrategyCurrentVal = afterUsedPriceStrategy4special;
					specialPriceStrategyCurrentId = agentPriceStrategy.getId();
				}else {
					if (afterUsedPriceStrategy4special<specialPriceStrategyCurrentVal) {
						specialPriceStrategyCurrentVal = afterUsedPriceStrategy4special;
						specialPriceStrategyCurrentId = agentPriceStrategy.getId();
					}
				}
			}
			
			/**
			 * 拼接  成人、儿童、特殊人群 的具体价格策略
			 * 
			 */
			StringBuffer priceStrategyBuffer = new StringBuffer("");
			for (AgentPriceStrategy agentPriceStrategy : agentPriceStrategies) {
				if (agentPriceStrategy.getId()==adultPriceStrategyCurrentId) {
					String quauqPrice4Adult = "";
					if(StringUtils.isNotBlank(agentPriceStrategy.getAdultPriceStrategy())){
						quauqPrice4Adult = agentPriceStrategy.getAdultPriceStrategy().replaceAll(",$", "").replaceFirst(",", "");
					}
					priceStrategyBuffer.append("quauqPrice4Adult#"+quauqPrice4Adult).append("##");
					adultPriceStrategyCurrentId = 0l;
				}
				
				if (agentPriceStrategy.getId()==childrenPriceStrategyCurrentId) {
					String quauqPrice4Child = "";
					if(StringUtils.isNotBlank(agentPriceStrategy.getChildrenPriceStrategy())){
						quauqPrice4Child = agentPriceStrategy.getChildrenPriceStrategy().replaceAll(",$", "").replaceFirst(",", "");
					}
					priceStrategyBuffer.append("quauqPrice4Child#"+quauqPrice4Child).append("##");
					childrenPriceStrategyCurrentId = 0l;
				}
				
				if (agentPriceStrategy.getId()==specialPriceStrategyCurrentId) {
					String quauqPrice4SpicalPerson = "";
					if(StringUtils.isNotBlank(agentPriceStrategy.getSpecialPriceStrategy())){
						quauqPrice4SpicalPerson = agentPriceStrategy.getSpecialPriceStrategy().replaceAll(",$", "").replaceFirst(",", "");
					}
					priceStrategyBuffer.append("quauqPrice4SpicalPerson#"+quauqPrice4SpicalPerson).append("##");
					specialPriceStrategyCurrentId = 0l;
				}
			}
			return priceStrategyBuffer.toString().replaceAll("##$", "");
			
		}else{
			return "0";
		}
	}
	
	/**
	 * 以500000作为价格基数
	 * 计算单项   成人、儿童 或 特殊人群的  策略基本值： 以1为基准，应用价格策略后的值  
	 * @param priceStrategy
	 * @return
	 */
	private double calcPriceStrategy(String priceStrategy){
		
		if (StringUtils.isNotBlank(priceStrategy)) {
			
			String[] priceStrategyArray = priceStrategy.replaceAll(",$", "").replaceFirst(",", "").split(",");
			BigDecimal basicDecimalOne = new BigDecimal("500000");
			for (int i = 0; i < priceStrategyArray.length; i++) {
				String[] priceStrategyItem = priceStrategyArray[i].split(":");
				if (Context.ADD == Integer.parseInt(priceStrategyItem[0])) {
					basicDecimalOne = basicDecimalOne.add(new BigDecimal(priceStrategyItem[1]));
				}else if (Context.SUBTRACT == Integer.parseInt(priceStrategyItem[0])) {
					basicDecimalOne = basicDecimalOne.subtract(new BigDecimal(priceStrategyItem[1]));
				}else if(Context.MULTIPLY == Integer.parseInt(priceStrategyItem[0])) {
					basicDecimalOne = basicDecimalOne.multiply(new BigDecimal(Double.parseDouble(priceStrategyItem[1]))).divide(new BigDecimal("100"));
				}/*else if (Context.DIVIDE == Integer.parseInt(priceStrategyItem[0])) {//暂时先不考虑除的情况
					basicDecimalOne = basicDecimalOne.divide(new BigDecimal(priceStrategyItem[1]));
				}*/
			}
			return basicDecimalOne.doubleValue();
			
		}else{
			return 500000;
		}
	}
	
	//-----------t1t2需求-----------s--//
	@Override
	public Page<TravelActivity> findActivityGroupInfos(Page<TravelActivity> page, TravelActivity travelActivity,
			String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7,
			String price, String arg8, String arg9,String countryPara,String pageNo,String pageSize,String orderBy, String type) {
		return travelActivityService.findActivityGroupInfos(page, travelActivity, arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, price, arg8, arg9,countryPara,pageNo,pageSize,orderBy,type);
	}
	
	@Override
	public List<Map<String, Object>> findAreaIdsEndCountry4T1(Long companyId) {
		return travelActivityService.findAreaIdsEndCountry4T1(companyId);
	}
	//-----------t1t2需求-----------e--//

	@Override
	public List<Map<String, String>> getFromAreas(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select fromArea, label, count(fromArea) sumArea from activitygroup c LEFT JOIN travelactivity a ON c.srcActivityId = a.id LEFT JOIN(select id, label, value from sys_dict where type = 'from_area' and delFlag = 0) b on a.fromArea = b.`value` where a.activity_kind = "+activityKind+" and a.proCompany= "+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and c.delFlag = 0 GROUP BY fromArea ORDER BY sumArea DESC";
		}else{
			sql = "select fromArea, label, count(fromArea) sumArea from activitygroup c LEFT JOIN travelactivity a ON c.srcActivityId = a.id LEFT JOIN(select id, label, value from sys_dict where type = 'from_area' and delFlag = 0) b on a.fromArea = b.`value` where a.activity_kind = "+activityKind+" and a.delFlag = 0 and a.activityStatus = 2 and c.delFlag = 0 GROUP BY fromArea ORDER BY sumArea DESC";
		}
		return travelActivityDao.findBySql(sql, Map.class);
	}

	@Override
	public Object getActivityKind(String activityKind, String companyId) {
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select DISTINCT a.activity_kind activityKind from travelactivity a where a.proCompany ="+companyId+"  and a.delFlag = 0";
		}else{
			sql = "select DISTINCT a.activity_kind activityKind from travelactivity a where  a.delFlag = 0";;
		}
		return travelActivityDao.findBySql(sql, Map.class);
	}

	@Override
	public List<Map<String, String>> getTargetAreas(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select targetAreaId,c.name, count(targetAreaId) sumArea  FROM activitygroup d LEFT JOIN  travelactivity a ON d.srcActivityId = a.id, activitytargetarea b, sys_area c where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.proCompany= "+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and d.delFlag = 0 GROUP BY targetAreaId ORDER BY sumArea DESC";
		}else{
			sql = "select targetAreaId,c.name, count(targetAreaId) sumArea  FROM activitygroup d LEFT JOIN  travelactivity a ON d.srcActivityId = a.id, activitytargetarea b, sys_area c where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and  a.delFlag = 0 and a.activityStatus = 2 and d.delFlag = 0 GROUP BY targetAreaId ORDER BY sumArea DESC";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}

	@Override
	public List<Map<String, String>> getTravelTypes(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select traveltypeid, traveltypename FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where traveltypeid is not null and traveltypename is not null and activity_kind = "+activityKind+" and proCompany="+companyId+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 GROUP BY traveltypeid";
		}else{
			sql =  "select traveltypeid, traveltypename FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where traveltypeid is not null and traveltypename is not null and activity_kind = "+activityKind+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 GROUP BY traveltypeid";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}

	@Override
	public List<Map<String, String>> getActivityTypes(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select activitytypeid,activitytypename  FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where activitytypeid is not null and activitytypename is not null  and activity_kind = "+activityKind+" and proCompany="+companyId+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 group by activitytypeid";
		}else{
			sql = "select activitytypeid,activitytypename  FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where activitytypeid is not null and activitytypename is not null  and activity_kind = "+activityKind+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 group by activitytypeid";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}

	@Override
	public List<Map<String, String>> getActivityLevels(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = "select activityLevelId,activityLevelName FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where activityLevelId is not null and activityLevelName is not null and activity_kind = "+activityKind+" and proCompany="+companyId+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 group by activityLevelId";
		}else{
			sql = "select activityLevelId,activityLevelName FROM activitygroup  b LEFT JOIN travelactivity a ON b.srcActivityId = a.id where activityLevelId is not null and activityLevelName is not null and activity_kind = "+activityKind+" and a.delFlag = 0 and b.delFlag = 0 and activityStatus = 2 group by activityLevelId";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}

	@Override
	public Object getDestinations(String activityKind,String companyId) {
		if(StringUtil.isBlank(activityKind)||StringUtil.isBlank(activityKind.trim())){
			activityKind = "2";
		}
		String sql = "";
		if(!StringUtil.isBlank(companyId)&&!StringUtil.isBlank(companyId.trim())){
			sql = 	"select id,name, sum(sumArea) sumArea1 from( "+
					"select d.id,d.name, count(targetAreaId) sumArea  FROM activitygroup  e LEFT JOIN travelactivity a ON e.srcActivityId = a.id, activitytargetarea b, sys_area c, sys_area d where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.proCompany="+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and c.parentId = d.id and d.type = 2 and e.delFlag = 0 GROUP BY targetAreaId "+
					"UNION all select c.id,c.name, count(targetAreaId) sumArea  FROM activitygroup  e LEFT JOIN travelactivity a ON e.srcActivityId = a.id, activitytargetarea b, sys_area c where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.proCompany="+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and c.type = 2 and e.delFlag = 0 GROUP BY targetAreaId "+
					"UNION all select e.id,e.name, count(targetAreaId) sumArea  FROM activitygroup  f LEFT JOIN travelactivity a ON f.srcActivityId = a.id, activitytargetarea b, sys_area c, sys_area d, sys_area e where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.proCompany="+companyId+" and a.delFlag = 0 and a.activityStatus = 2 and c.parentId = d.id and d.type = 3 and d.parentId = e.id and e. type = 2 and f.delFlag = 0 GROUP BY targetAreaId ) e group by id ORDER BY sumArea1 DESC";
		}else{
			sql = "select id,name, sum(sumArea) sumArea1 from( "+
					"select d.id,d.name, count(targetAreaId) sumArea  FROM activitygroup  e LEFT JOIN travelactivity a ON e.srcActivityId = a.id, activitytargetarea b, sys_area c, sys_area d where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.delFlag = 0 and a.activityStatus = 2 and c.parentId = d.id and d.type = 2 and e.delFlag = 0 GROUP BY targetAreaId "+
					"UNION all select c.id,c.name, count(targetAreaId) sumArea  FROM activitygroup  e LEFT JOIN travelactivity a ON e.srcActivityId = a.id, activitytargetarea b, sys_area c where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.delFlag = 0 and a.activityStatus = 2 and c.type = 2 and e.delFlag = 0 GROUP BY targetAreaId "+
					"UNION all select e.id,e.name, count(targetAreaId) sumArea  FROM activitygroup  f LEFT JOIN travelactivity a ON f.srcActivityId = a.id, activitytargetarea b, sys_area c, sys_area d, sys_area e where a.id = b.srcactivityid and c.id=b.targetAreaId and a.activity_kind = "+activityKind+" and a.delFlag = 0 and a.activityStatus = 2 and c.parentId = d.id and d.type = 3 and d.parentId = e.id and e. type = 2 and f.delFlag = 0 GROUP BY targetAreaId ) e group by id ORDER BY sumArea1 DESC";
		}
		return travelActivityDao.findBySql(sql,Map.class);
	}

	@Override
	public Page<Map<Object, Object>> searchSPActivityList(Page<Map<Object, Object>> page, HttpServletRequest request,Model model, String companyId,String requestType) {
		//获取搜索参数
		String activityKind = request.getParameter("activityKind");//产品名称  或 团号
		String activityName = request.getParameter("nameCode");//产品名称  或 团号
		String fromAreas = request.getParameter("contentWrap1");// 出发城市
		String targetAreas = request.getParameter("contentWrap2");// 到达城市
		String destination = request.getParameter("contentWrap3");// 到达城市所在国家
		String dateBucket = request.getParameter("departureDate");//出团开始日期
		String groupOpenDate = request.getParameter("startDate");//出团开始日期
		String groupEndDate = request.getParameter("endDate");//出团结束日期
		String travelTypes = request.getParameter("travelType");//旅游类型
		String activityTypes = request.getParameter("activityType");//产品类型
		String activityLevels = request.getParameter("activityLevel");//产品系列
		String onlineState = request.getParameter("onlineState");//交易系统上架状态
		String orderByQuauqPrice =  request.getParameter("orderByQuauqPrice");//quauq 价排序
		String orderByIndustryPrice =  request.getParameter("orderByIndustryPrice");// 厂价排序
		String orderByDate =  request.getParameter("orderByDate");//团期日期排序
		String allActivity = request.getParameter("allActivity");//是否查询未设置
		
		String sql = "";
		if(StringUtil.isBlank(activityKind)){
			activityKind = "2";
		}
		if(StringUtil.isNotBlank(requestType) && requestType.equals("pricingStrategyRequest") &&  UserUtils.getUser().getCompany().getShelfRightsStatus() == 0){
			sql = "select a.srcActivityId,a.id,a.is_t1 as isT1,a.currency_type, a.groupCode,b.acitivityName, a.groupOpenDate, a.settlementAdultPrice, a.settlementcChildPrice, a.settlementSpecialPrice, a.quauqAdultPrice, a.quauqChildPrice, a.quauqSpecialPrice,lp1.is_read,b.activity_kind, ifnull(a.quauqAdultPrice,'')+ifnull(a.quauqChildPrice,'')+ifnull(a.quauqSpecialPrice,'') as isT12 from activitygroup a LEFT JOIN travelactivity b on a.srcActivityId = b.id left join (select count(is_read) is_read,lp.group_id from log_product lp where lp.business_type = 2  and is_read = 0 and lp.field_name like '%Price' GROUP BY lp.group_id )lp1 on a.id = lp1.group_id where b.activity_kind = "+activityKind.trim();
		}else{
			sql = "select a.srcActivityId,a.id,a.is_t1 as isT1,a.currency_type, a.groupCode, b.acitivityName, a.groupOpenDate, a.settlementAdultPrice, a.settlementcChildPrice, a.settlementSpecialPrice, a.quauqAdultPrice, a.quauqChildPrice, a.quauqSpecialPrice,lp1.is_read,b.activity_kind from activitygroup a LEFT JOIN travelactivity b on a.srcActivityId = b.id left join (select count(is_read) is_read,lp.group_id from log_product lp where lp.business_type = 2  and is_read = 0 and lp.field_name like '%Price' GROUP BY lp.group_id )lp1 on a.id = lp1.group_id where b.activity_kind = "+activityKind.trim();
		}
		
		if(!StringUtil.isBlank(companyId) && !StringUtil.isBlank(companyId.trim())){
			sql = sql + " and b.proCompany="+companyId+" and b.delFlag = 0 and a.delFlag = 0 and b.activityStatus = 2";
		}else{
			sql = sql + " and b.delFlag = 0 and a.delFlag = 0 and b.activityStatus = 2";
		}
		
		if(!StringUtil.isBlank(activityName)){
			activityName = activityName.replaceAll(" ", "");
			sql = sql + " and ( a.groupCode like '%"+activityName.trim()+"%' or replace(b.acitivityName,' ','') like '%"+activityName.trim()+"%') ";
	    }
		if(!StringUtil.isBlank(fromAreas) && !StringUtil.isBlank(fromAreas.trim())){
//			sql = sql +" and b.fromArea in ("+fromAreas+") ";
			if(!JudgeStringType.isPositiveInteger(fromAreas)){
				throw new RuntimeException("参数数据格式错误");
			}
			sql = sql +" and b.fromArea = "+fromAreas+" ";
			model.addAttribute("contentWrap1", new Long(fromAreas.trim()));
		}else{
			model.addAttribute("contentWrap1", fromAreas);
		}
		if(!StringUtil.isBlank(groupOpenDate)){
			if(!JudgeStringType.isDate(groupOpenDate)){
				throw new RuntimeException("参数数据格式错误");
			}
			sql = sql +" and a.groupOpenDate >= '" +groupOpenDate+"' ";
		}
		if(!StringUtil.isBlank(groupEndDate)){
			if(!JudgeStringType.isDate(groupEndDate)){
				throw new RuntimeException("参数数据格式错误");
			}
			sql = sql +" and a.groupOpenDate <= '" +groupEndDate+"' ";		
		}
		if(!StringUtil.isBlank(dateBucket)){
			//获取当前时间 
			Calendar a=Calendar.getInstance();
			sql = sql +" and a.groupOpenDate >= '" +a.get(Calendar.YEAR)+"-"+dateBucket+"-01' and a.groupOpenDate <= '";	
			a.set(Calendar.MONTH,7);
			sql = sql +a.get(Calendar.YEAR)+"-"+dateBucket+"-"+a.getActualMaximum(Calendar.DATE)+"' ";
		}
		if(!StringUtil.isBlank(travelTypes)){
//			sql = sql +" and b.travelTypeId in("+travelTypes+")";
			if(travelTypes.trim().equals("-1")){
				sql = sql +" and b.travelTypeId is null ";
			}else{
				sql = sql +" and b.travelTypeId ="+travelTypes+" ";
			}
		}
		if(!StringUtil.isBlank(activityTypes)){
//			sql = sql +" and b.activityTypeId in("+travelTypes+")";
			if(activityTypes.trim().equals("-1")){
				sql = sql +" and b.activityTypeId is null ";
			}else{
				sql = sql +" and b.activityTypeId = "+activityTypes+" ";
			}
		}
		if(!StringUtil.isBlank(activityLevels)){
//			sql = sql +" and b.activityLevelId in("+travelTypes+")";
			if(activityLevels.trim().equals("-1")){
				sql = sql +" and b.activityLevelId is null ";
			}else{
				sql = sql +" and b.activityLevelId = "+activityLevels+" ";
				
			}
		}
		
		if(!StringUtil.isBlank(onlineState)){
			if(onlineState.equals("1")){
				sql = sql +" and a.quauqAdultPrice is null and a.quauqChildPrice is null and a.quauqSpecialPrice is null and a.is_t1 = 0 " ;
			}else if(onlineState.equals("2")){
				sql = sql + " and a.is_t1 = 1 ";
			}else if(onlineState.equals("3")){
				sql = sql +" and (ifnull(a.quauqAdultPrice,'')+ifnull(a.quauqChildPrice,'')+ifnull(a.quauqSpecialPrice,'')) != '' and a.is_t1 = 0 " ;
			}
		}
		if(!StringUtil.isBlank(targetAreas)&& !StringUtil.isBlank(targetAreas.trim())){
//			sql = sql +" and  a.srcActivityId in(select srcActivityId from activitytargetarea where targetAreaId in ("+targetAreas+"))";
			sql = sql +" and  a.srcActivityId in (select srcActivityId from activitytargetarea where targetAreaId = "+targetAreas+")";
			model.addAttribute("contentWrap2", new Long(targetAreas.trim()));
		}else{
			model.addAttribute("contentWrap2", targetAreas);
		}
		if(!StringUtil.isBlank(destination)&& !StringUtil.isBlank(destination.trim())){
			//获取国家所包含的城市
			sql = sql +" and  a.srcActivityId in (select DISTINCT srcActivityId from activitytargetarea where targetAreaId in (select id from sys_area where parentIds like '%,"+destination+",%' union all select "+destination+"))";
			model.addAttribute("contentWrap3", new Long(destination.trim()));
		}else{
			model.addAttribute("contentWrap3", destination);
		}
		if(!StringUtil.isBlank(allActivity)){
			model.addAttribute("allActivity", allActivity);
			sql = sql +" and a.id not IN (select DISTINCT activitygroupId from activity_pricingStrategy where usageState = 0)";
		}
		
		if(!StringUtil.isBlank(orderByQuauqPrice)){
			sql = sql +" order by lp1.is_read desc,  a.quauqAdultPrice " + orderByQuauqPrice;
		}else if(!StringUtil.isBlank(orderByIndustryPrice)){
			sql = sql +" order by lp1.is_read desc,  a.settlementAdultPrice " + orderByIndustryPrice;
		}else if(!StringUtil.isBlank(orderByDate)){
			sql = sql +" order by lp1.is_read desc, a.groupOpenDate " + orderByDate;
		}else{
			orderByDate = "desc";
			sql = sql + " order by lp1.is_read desc,  a.groupOpenDate desc";
		}
		
		model.addAttribute("activityKind", activityKind);
		model.addAttribute("activityName", activityName);
		model.addAttribute("departureDate", dateBucket);
		model.addAttribute("startDate", groupOpenDate);
		model.addAttribute("endDate", groupEndDate);
		model.addAttribute("travelType", travelTypes);
		model.addAttribute("activityType", activityTypes);
		model.addAttribute("activityLevel", activityLevels);
		model.addAttribute("onlineState", onlineState);
		model.addAttribute("orderByQuauqPrice", orderByQuauqPrice);
		model.addAttribute("orderByIndustryPrice", orderByIndustryPrice);
		model.addAttribute("orderByDate", orderByDate);
		return travelActivityDao.findBySql(page,sql,Map.class);
	}

	@Override
	public List<Object> searchChangedList(String groupId, String srcId) {
		String Sql = "select * from log_product where business_type = 2 and activity_id ="+srcId+" and group_id = "+groupId +" and field_name like '%Price'";
		return travelActivityDao.findBySql(Sql,Map.class, null);
	}

	@Override
	public int getCount(boolean b) {
		String sql = "select a.srcActivityId,a.id,a.currency_type, a.groupCode, b.acitivityName, a.groupOpenDate, a.settlementAdultPrice, a.settlementcChildPrice, a.settlementSpecialPrice, a.quauqAdultPrice, a.quauqChildPrice, a.quauqSpecialPrice from activitygroup a LEFT JOIN travelactivity b on a.srcActivityId = b.id where b.activity_kind = 2 and b.proCompany="+UserUtils.getUser().getCompany().getId()+" and b.delFlag = 0 and a.delFlag = 0 and b.activityStatus = 2";
		if(!b){
			sql = sql +" and a.id not IN (select DISTINCT activitygroupId from activity_pricingStrategy where usageState = 0)";
			return travelActivityDao.findBySql(sql, null).size();
		}else{
			return travelActivityDao.findBySql(sql, null).size();
		}
	}
	
	@Override
	public boolean getChangedCount() {
		String sql = "select a.srcActivityId from activitygroup a LEFT JOIN travelactivity b on a.srcActivityId = b.id left join log_product lp on a.srcActivityId= lp.activity_id and a.id= lp.group_id where b.activity_kind = 2 and b.proCompany="+UserUtils.getUser().getCompany().getId()+" and b.delFlag = 0 and a.delFlag = 0 and b.activityStatus = 2";
		sql = sql + " and a.is_t1 = 0 and lp.business_type = 2 and lp.is_read = 0 and lp.field_name like '%Price'";
		return (travelActivityDao.findBySql(sql, null).size() > 0);
	}

	@Override
	public int hasChanged(String groupId, String srcId) {
		String sql = "select * from log_product where is_read = 0 and business_type = 2 and activity_id ="+srcId+" and group_id = "+groupId+" and field_name like 'settlement%Price'";
		int settlementPrice = travelActivityDao.findBySql(sql, null).size();
		sql = "select * from log_product where is_read = 0 and business_type = 2 and activity_id ="+srcId+" and group_id = "+groupId+" and field_name like 'suggest%Price'";
		int suggestPrice = travelActivityDao.findBySql(sql, null).size();
		if(settlementPrice >0 && suggestPrice > 0 ){
			return 1;
		}else if(settlementPrice >0){
			return 2;
		}else if(suggestPrice > 0){
			return 3;
		}
		return 0;
	}

	@Override
	public List<Map<Object, Object>> exportData() {
		
		String sql = "select office.name officeName, a.srcActivityId,a.id,a.currency_type, a.groupCode, b.acitivityName, a.groupOpenDate, a.freePosition, " +
				"a.settlementAdultPrice,a.settlementcChildPrice, a.settlementSpecialPrice, a.quauqAdultPrice, a.quauqChildPrice, a.quauqSpecialPrice, " +
				"FORMAT(a.quauqAdultPrice * (office.charge_rate + 1), 2) tgAdultPrice, " +
				"FORMAT(a.quauqChildPrice * (office.charge_rate + 1), 2) tgChildPrice, " +
				"FORMAT(a.quauqSpecialPrice * (office.charge_rate + 1), 2) tgSpecialPrice " +
				"from activitygroup a, travelactivity b, sys_office office " +
				"where a.srcActivityId = b.id and b.proCompany = office.id and b.activity_kind = 2 and b.delFlag = 0 " +
				"and a.delFlag = 0 and b.activityStatus = 2 and office.id != 369 " +
				"and (a.quauqAdultPrice is not null or a.quauqChildPrice is not null or a.quauqSpecialPrice is not null) " +
				"order by office.id asc, a.groupOpenDate desc";
		
		return travelActivityDao.findBySql(sql, Map.class);
	}

	/**
	 * 保存产品日志记录
	 * @param productType
	 * @param opState
	 * @param fieldName
	 * @param context
	 * @param group
     * @return
     */
	public String saveLogProduct(Integer productType,String opState,String fieldName,String context,ActivityGroup group) {
		User user = UserUtils.getUser();
		// 未登录，则跳转到登录页
		if(user.getId() == null){
			return "redirect:"+Global.getAdminPath()+"/login";
		}
		LogProduct logProduct = new LogProduct();
		logProduct.setUuid(UuidUtils.generUuid());
		logProduct.setProductType(productType);
		logProduct.setActivityId(Long.parseLong(group.getSrcActivityId().toString()));
		logProduct.setGroupId(Integer.parseInt(group.getId().toString()));
		logProduct.setBusinessType(Integer.parseInt(opState));
		logProduct.setFieldName(fieldName);
		logProduct.setContent(context);
		logProduct.setCreateBy(user.getId());
		logProduct.setCreateDate(new Date());
		logProduct.setCompanyId(user.getCompany().getId());
		logProductDao.save(logProduct);
		return null;
	}


	@Override
	public Page<Map<Object, Object>> searchProductAndActivityList(
			Page<Map<Object, Object>> page, HttpServletRequest request,
			Model model) {
		// TODO Auto-generated method stub
		StringBuffer sb =new StringBuffer();
		/*sb.append("SELECT ta.id taid, ag.id agid, ta.proCompanyName, ta.activityTypeName, ta.activityTypeId, ag.settlementAdultPrice, ag.settlementcChildPrice, ag.settlementSpecialPrice  ");
		sb.append("FROM travelactivity ta LEFT JOIN activitygroup ag ON ag.srcActivityId = ta.id LEFT JOIN sys_office_product_type so ON so.id = ta.activityTypeId");
		*/
		sb.append("SELECT sy.id ,sy.`name`, date_format(sy.export_time, '%Y/%c/%d %T') as export_time from sys_office sy where sy.delFlag != 1");
		return travelActivityDao.findBySql(page,sb.toString(),Map.class);
		//return null;
	}

	@Override
	public List<Map<String, Object>> findProductAndActivityList(
			String officeId, HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub
		String sql ="SELECT ta.id taid, ag.id agid,ta.activity_kind,ag.currency_type, ag.settlementAdultPrice, ag.settlementcChildPrice, ag.settlementSpecialPrice,ag.quauqAdultPrice,ag.quauqChildPrice,ag.quauqSpecialPrice " +
				"FROM travelactivity ta LEFT JOIN activitygroup ag ON ag.srcActivityId = ta.id WHERE  ta.delFlag = 0 and ag.delFlag = 0 " +
				/*"and ag.is_t1 = 1 and ag.pricingStrategyStatus != 0 " +*/
				" and ta.activityStatus = 2 and ta.activity_kind = 2 " +
				/*"and ag.hasEyelessAgents != 0 " +*/
				"and ta.proCompany = "+ Long.parseLong(officeId);
		return travelActivityDao.findBySql(sql, Map.class);
		//return null;
	}

	@Override
	public List<Map<String, String>> getGroupAndOfficeT1PermissionStatus(String groupId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT agp.is_t1 agpT1 ");
		sql.append(" FROM activitygroup agp ");
//		sql.append(" LEFT JOIN travelactivity activity ON agp.srcActivityId = activity.id ");
//		sql.append(" LEFT JOIN sys_office office ON activity.proCompany = office.id ");
		sql.append(" WHERE agp.id = " + groupId);
		return travelActivityDao.findBySql(sql.toString(), Map.class);
	}

	/**
	 * 根据产品类型获取所有的产品的目的地,做数据初始化用。
	 * @param orderType
	 * @return
	 * @author yudong.xu 2016.10.20
	 */
	@Override
	public List<Map<String,Object>> getAllActivityAreaIdsByType(Integer orderType){
		String sql = "SELECT GROUP_CONCAT(m.targetAreaId) AS areaIds,p.id AS activityId,p.acitivityName AS activityName " +
				"FROM travelactivity p,activitytargetarea m WHERE p.id=m.srcActivityId AND p.activity_kind=? GROUP BY p.id";
		List<Map<String,Object>> list =  travelActivityDao.findBySql(sql,Map.class,orderType);
		return list;
	}

	/**
	 * 更新产品中的线路玩法
	 * @param touristLineId
	 * @param activityId
	 * @author yudong.xu 2016.10.20
	 */
	@Transactional(readOnly = false)
	@Override
	public void updateTouristLine(Long touristLineId, Long activityId){
		travelActivityDao.updateTouristLine(touristLineId,activityId);
	}

}
