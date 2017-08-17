/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelGuestType;
import com.trekiz.admin.modules.hotel.entity.HotelRoom;
import com.trekiz.admin.modules.hotel.entity.HotelRoomOccuRate;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.SysGuestTravelerTypeRel;
import com.trekiz.admin.modules.hotel.entity.SysGuestType;
import com.trekiz.admin.modules.hotel.query.SysGuestTravelerTypeRelQuery;
import com.trekiz.admin.modules.hotel.service.HotelGuestTypeService;
import com.trekiz.admin.modules.hotel.service.HotelRoomOccuRateService;
import com.trekiz.admin.modules.hotel.service.HotelRoomService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerTypeRelationService;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.hotel.service.SysGuestTravelerTypeRelService;
import com.trekiz.admin.modules.hotel.service.SysGuestTypeService;
import com.trekiz.admin.modules.hotelPl.entity.HotelPl;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlHolidayMeal;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandway;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlMealrise;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatter;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatterValue;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRel;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequire;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRoom;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialTax;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPrice;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxException;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxPrice;
import com.trekiz.admin.modules.hotelPl.module.bean.GuestPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.PreferentialJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.QuotedPriceDetailJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.QuotedPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.QuotedPriceResultJsonBean;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceQuery;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceRoomQuery;
import com.trekiz.admin.modules.hotelPl.module.util.AutoQuotedPriceDataUtil;
import com.trekiz.admin.modules.hotelPl.query.HotelPlHolidayMealQuery;
import com.trekiz.admin.modules.hotelPl.query.HotelPlMealriseQuery;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPreferentialQuery;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPriceQuery;
import com.trekiz.admin.modules.hotelPl.query.HotelPlQuery;
import com.trekiz.admin.modules.hotelPl.query.HotelPlTaxExceptionQuery;
import com.trekiz.admin.modules.hotelPl.query.HotelPlTaxPriceQuery;
import com.trekiz.admin.modules.hotelPl.service.AutoQuotedPriceService;
import com.trekiz.admin.modules.hotelPl.service.HotelPlHolidayMealService;
import com.trekiz.admin.modules.hotelPl.service.HotelPlIslandwayService;
import com.trekiz.admin.modules.hotelPl.service.HotelPlMealriseService;
import com.trekiz.admin.modules.hotelPl.service.HotelPlPreferentialService;
import com.trekiz.admin.modules.hotelPl.service.HotelPlPriceService;
import com.trekiz.admin.modules.hotelPl.service.HotelPlService;
import com.trekiz.admin.modules.hotelPl.service.HotelPlTaxExceptionService;
import com.trekiz.admin.modules.hotelPl.service.HotelPlTaxPriceService;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.preferential.entity.PreferentialTemplates;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
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
public class AutoQuotedPricelServiceImpl  extends BaseService implements AutoQuotedPriceService{
	private final static Log logger = LogFactory.getLog(AutoQuotedPricelServiceImpl.class);
	@Autowired
	private HotelPlService hotelPlService;
	@Autowired
	private TravelerTypeService travelerTypeService;
	@Autowired
	private HotelGuestTypeService hotelGuestTypeService;
	@Autowired
	private SysGuestTypeService sysGuestTypeService;
	@Autowired
	private HotelPlIslandwayService hotelPlIslandwayService;
	@Autowired
	private HotelPlTaxExceptionService hotelPlTaxExceptionService;
	@Autowired
	private HotelPlTaxPriceService hotelPlTaxPriceService;
	@Autowired
	private HotelPlMealriseService hotelPlMealriseService;
	@Autowired
	private HotelPlHolidayMealService hotelPlHolidayMealService;
	@Autowired
	private HotelPlPriceService hotelPlPriceService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private HotelRoomService hotelRoomService;
	
	@Autowired
	private HotelPlPreferentialService hotelPlPreferentialService;
	@Autowired
	private SysGeographyService sysGeographyService;
	@Autowired
	private IslandService islandService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	@Autowired
	private SupplierInfoService supplierInfoService;
	@Autowired
	private HotelTravelerTypeRelationService hotelTravelerTypeRelationService;
	@Autowired
	private SysGuestTravelerTypeRelService sysGuestTravelerTypeRelService;
	@Autowired
	private HotelRoomOccuRateService hotelRoomOccuRateService;
	/**
	 * 验证是否超出选择房型的容住率，add by zhanghao
	 * @param query 报价条件
	 */
	public Map<String,String> checkRoomCapacity(QuotedPriceQuery query){
		
		Map<String,String> result = new HashMap<String,String>();
		result.put("result", "success");
		result.put("message", "");
		
		AutoQuotedPriceDataUtil dataUtil = new AutoQuotedPriceDataUtil();
		groupTravelerTypePerNum(query, dataUtil);
		
		List<QuotedPriceRoomQuery> roomQueryList = query.getQuotedPriceRoomList();
		List<List<Map<String,Integer>>> list = new ArrayList<List<Map<String,Integer>>>();//不同房型的容积率集合
		if(CollectionUtils.isNotEmpty(roomQueryList)){
			for(QuotedPriceRoomQuery rquery:roomQueryList){
				String hotelCapacity = rquery.getHotelCapacity();
				String[] arrayCapacity = null;
				if(hotelCapacity.indexOf("/")>-1){
					arrayCapacity = hotelCapacity.split("/");
				}else{
					arrayCapacity=new String[]{hotelCapacity};
				}
				
				if(ArrayUtils.isNotEmpty(arrayCapacity)){
					List<Map<String,Integer>> mapList = new ArrayList<Map<String,Integer>>();//每个房型的容住率集合
					for(String capacity:arrayCapacity){
						String[] arrayPersonNum = null;
						if(capacity.indexOf("+")>-1){
							arrayPersonNum = capacity.split("[+]");
						}else{
							arrayPersonNum = new String[]{capacity};
						}
						if(ArrayUtils.isNotEmpty(arrayPersonNum)){
							Map<String,Integer> map = new HashMap<String,Integer>();//容住率的key-value；key：shortName，value：数量
							
							for(String personNum:arrayPersonNum){
								String[] nums = personNum.split("[a-zA-Z]");
								if(ArrayUtils.isNotEmpty(nums)&&nums.length>0){
									String shortName = personNum.replaceFirst(nums[0], "");
									map.put(shortName, Integer.parseInt(nums[0]));
								}
								
							}
							mapList.add(map);
						}
					}
					list.add(mapList);
				}
			}
		}
		
		if(MapUtils.isNotEmpty(dataUtil.getPerNumMap())){
			Set<String> shortNameKey = dataUtil.getPerNumMap().keySet();
			boolean bo = false;//标示是否还需要继续验证下一个入住日期中的房型容住率（如有一个超出容住率就会跳出循环）
			if(CollectionUtils.isNotEmpty(list)){//循环每个房型的容住率，进行验证
				for(List<Map<String,Integer>> detailList : list){
							
					if(CollectionUtils.isNotEmpty(detailList)){
						boolean b = true;//每个房型中是否有满足需要的容住率，如果有一个满足即会跳出循环
						for(int i=0;i<detailList.size();i++){
							Map<String,Integer> detailMap = detailList.get(i);//一个房型的多个容住率 MAP ，如：A:2，C:2
									
							if(CollectionUtils.isNotEmpty(shortNameKey)){
								for(String shortName:shortNameKey){
									Integer perNum = dataUtil.getPerNumMap().get(shortName);
									if(perNum==0){//报价条件中的0数量游客类型过滤
										continue;
									}
									if(detailMap.containsKey(shortName)){//如果房型中的容住率 可以 住当前的报价条件中的游客类型 则继续验证数量是否超标
										
										if(detailMap.get(shortName)>=perNum){//数量满足，则可以入住，标示b=true，跳出循环，进行下个入住日期的验证。
											b=true;
										}else{//数量不满足，进行下一个该房型的容住率验证
											b=false;
											break;
										}
									}else{//如果当前的容住率不满足，则跳出进行下一个该房型的容住率验证
										b=false;
										break;
									}
									
								}
									
							}
							if(b){//如果有满足的容住率，则跳出循环，进行下一个入住日期的验证
								bo=true;//标示是否还需要继续验证下一个入住日期中的房型容住率，当前满足容住率，继续下一个入住日期的验证
								result.put("result", "success");
								result.put("message", "");
								break;
							}else{
								bo=false;//标示是否还需要继续验证下一个入住日期中的房型容住率，当前不满足容住率，跳出循环
								result.put("result", "fail");
								result.put("message", "");
							}
						}
					}
					if(!bo){
						break;
					}
				}
			}
		}else{
			result.put("result", "fail");
			result.put("message", "游客类型匹配不成功，请刷新页面重新操作！");
		}
		
		
		
		return result;
	}
	
	
	/**
	 * 把报价的条件 做分组合并，KEY是房型UUID，value是晚数的和 add by zhanghao
	 * @param roomQueryList
	 * @return
	 */
	private Map<String,Integer> getRoomAndNightsMap4Query(List<QuotedPriceRoomQuery> roomQueryList){
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(QuotedPriceRoomQuery roomQuery:roomQueryList){
			if(map.containsKey(roomQuery.getHotelRoomUuid())){
				map.put(roomQuery.getHotelRoomUuid(), map.get(roomQuery.getHotelRoomUuid())+1);
			}else{
				map.put(roomQuery.getHotelRoomUuid(), 1);
			}
		}
		return map;
	}
	/**
	 * 把优惠的房型晚数信息 做分组合并，KEY是房型UUID，value是晚数的和 add by zhanghao
	 * @param roomQueryList
	 * @return
	 */
	private Map<String,Integer> getRoomAndNightsMap4Room(List<HotelPlPreferentialRoom> roomList){
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(HotelPlPreferentialRoom room:roomList){
			if(map.containsKey(room.getHotelRoomUuid())){
				map.put(room.getHotelRoomUuid(), map.get(room.getHotelRoomUuid())+room.getNights());
			}else{
				map.put(room.getHotelRoomUuid(), room.getNights());
			}
		}
		return map;
	}
	
	
	
	/**
	 * 得到游客类型和对应的游客人数 add by zhanghao
	 * key 的排序和数据库中的查询排序相同，保证页面传入的人员数数组对应
	 * KEY是游客类型的简写 ：A成人 B婴儿 C儿童 O老人 S特殊人群
	 * VALUE 是对应的游客人数
	 * @param query
	 * @return
	 */
	private void groupTravelerTypePerNum(QuotedPriceQuery query,AutoQuotedPriceDataUtil dataUtil){
		Map<String,Integer> map = new LinkedHashMap<String,Integer>();
		List<TravelerType> list = hotelTravelerTypeRelationService.getTravelerTypesByHotelUuid(query.getHotelUuid());
		try {
			if(CollectionUtils.isNotEmpty(list)&&ArrayUtils.isNotEmpty(query.getPersonNum())&&list.size()==query.getPersonNum().length){
				for(int i = 0;i<list.size();i++){
					if(StringUtils.isBlank(query.getPersonNum()[i])){
						map.put(list.get(i).getShortName(), 0);
					}else{
						if(StringUtils.isBlank(list.get(i).getShortName())){
							dataUtil.putMessage("请先维护游客类型的简写在进行报价！");
							break;
						}
						map.put(list.get(i).getShortName(), Integer.parseInt(query.getPersonNum()[i]));
						if(list.get(i) != null && list.get(i).getPersonType()!=null&&list.get(i).getPersonType().intValue() == 0){//第一个人数不为0的成人类型游客类型
							dataUtil.setSortPreferObj(list.get(i));//优惠排序用的游客类型对象，基于此对象进行优惠后的价格排序。默认是第一个人数不为0的成人类型的游客类型
						}
					}
					
				}
				if(MapUtils.isNotEmpty(map)&&map.keySet().size()!=list.size()){
					dataUtil.putMessage("公司的游客类型绑定系统的游客类型时出现重复绑定！");
				}
				
				
				//成人为1时对儿童数量和成人数量做增减操作，旅游行业潜规则，不允许1A1C或1A多C出现
				if(CollectionUtils.isNotEmpty(dataUtil.getAdultList())){
					int tempPerNum = 0;
					String tempSn = "";
					//获得所有的成人类型游客类型的入住人数总和
					for(String sn:dataUtil.getAdultList()){
						if(map.containsKey(sn)){
							if(map.get(sn)==1){//临时保存成人数是1的简写
								tempSn=sn;
							}
							tempPerNum +=map.get(sn);
						}
					}
					if(tempPerNum==1){//所有成人数==1时 ，如果当前存在儿童的入住数量，则进行成人+1，儿童-1的操作。
						for(String sn:dataUtil.getChildList()){
							if(map.containsKey(sn)&&map.get(sn)>0){
								map.put(tempSn, map.get(tempSn)+1);
								map.put(sn, map.get(sn)-1);
								break;
							}
						}
					}
				}
				
				dataUtil.setPerNumMap(map);
			}
		} catch (NumberFormatException e) {
			logger.error("报价器根据条件报价时出现异常(输入的人数有非法字符或者超出长度)！",e);
			dataUtil.putMessage("报价器根据条件报价时出现异常(输入的人数有非法字符或者超出长度)！");
		}
	}

	/**
	 * 必填的验证 add by zhanghao
	 * @param query
	 * @return
	 */
	private boolean validataFormInput(QuotedPriceQuery query){
		boolean result = true;
		try {
			if(StringUtils.isBlank(query.getHotelUuid())){
				result=false;
			}else if(StringUtils.isBlank(query.getCountry())){
				result=false;
			}else if(StringUtils.isBlank(query.getIslandUuid())){
				result=false;
			}else if(StringUtils.isBlank(query.getDepartureIslandWay())){
				result=false;
			}else if(StringUtils.isBlank(query.getArrivalIslandWay())){
				result=false;
			}else if(ArrayUtils.isEmpty(query.getPersonNum())){
			//else if(ArrayUtils.isEmpty(query.getPersonNum())||StringUtils.isBlank(query.getPersonNum()[0])||Integer.parseInt(query.getPersonNum()[0])==0){
				//必须有成人数量才可以报价
				result=false;
			}else if(CollectionUtils.isEmpty(query.getQuotedPriceRoomList())){
				result=false;
			}
		} catch (NumberFormatException e) {
			result = false;
			logger.error("报价器成人数输入错误！",e);
		}
		return result;
	}
	
	/**
	 * 根据国家、酒店、地接社供应商、采购类型或得批发商下指定的价单
	 * @param query
	 * @return
	 */
	private List<HotelPl> getHotelPlList(QuotedPriceQuery query){
		HotelPlQuery hotelPlQuery = new HotelPlQuery();
		hotelPlQuery.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
		hotelPlQuery.setCountry(query.getCountry());
		hotelPlQuery.setHotelUuid(query.getHotelUuid());
		hotelPlQuery.setPurchaseType(query.getPurchaseType());
		hotelPlQuery.setSupplierInfoId(query.getSupplierInfoId());
		hotelPlQuery.setDelFlag("0");
		List<HotelPl> plList = hotelPlService.find(hotelPlQuery);
		return plList;
	}
	
	/**
	 * 初始化价单的数据 add by zhanghao
	 * @param query
	 * @param dataUtil
	 */
	private void initData(QuotedPriceQuery query,AutoQuotedPriceDataUtil dataUtil){
		
		/**当前批发商下 游客类型 的简写和TravelerType map*/
		if(dataUtil.getTravelerTypeMap()==null){
			Map<String,TravelerType> travelerTypeMap = new HashMap<String,TravelerType>();
			dataUtil.setTravelerTypeMap(travelerTypeMap);
//			TravelerType travelerType = new TravelerType();
//			travelerType.setDelFlag("0");
//			travelerType.setStatus("1");
//			travelerType.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
//			List<TravelerType> list = travelerTypeService.find(travelerType);
			
			List<TravelerType> list = hotelTravelerTypeRelationService.getTravelerTypesByHotelUuid(query.getHotelUuid());
			for(TravelerType type:list){
				if(StringUtils.isNotBlank(type.getShortName())){
					travelerTypeMap.put(type.getShortName(), type);
				}
				if(type.getPersonType()!=null) {
					if(type.getPersonType().intValue() == 0) {//成人的游客类型简写
						dataUtil.getAdultList().add(type.getShortName());
					} else if(type.getPersonType().intValue() == 2) {//儿童的游客类型简写
						dataUtil.getChildList().add(type.getShortName());
					}
				}
			}
		}
		
		/**游客类型简写和入住人数的映射关系*/
		groupTravelerTypePerNum(query,dataUtil);
		
		/**系统内所有游客类型和住客类型的映射关系 KEY:系统游客类型UUID，VALUE:系统游客类型和住客类型关联关系的数据集合*/
		SysGuestTravelerTypeRelQuery sysGuestTravelerTypeRelQuery = new SysGuestTravelerTypeRelQuery();
		sysGuestTravelerTypeRelQuery.setDelFlag("0");
		List<SysGuestTravelerTypeRel> guestAndTravelerTypeRelList = sysGuestTravelerTypeRelService.find(sysGuestTravelerTypeRelQuery);
		if(CollectionUtils.isNotEmpty(guestAndTravelerTypeRelList)){
			
			Map<String, List<String>> guestTypeMap4TravelerType = new HashMap<String, List<String>>();
			for(SysGuestTravelerTypeRel sgtr:guestAndTravelerTypeRelList){
				if(guestTypeMap4TravelerType.containsKey(sgtr.getSysTravelerTypeUuid())){
					List<String> list = guestTypeMap4TravelerType.get(sgtr.getSysTravelerTypeUuid());
					list.add(sgtr.getSysGuestTypeUuid());
					guestTypeMap4TravelerType.put(sgtr.getSysTravelerTypeUuid(), list);
				}else{
					List<String> list = new ArrayList<String>();
					list.add(sgtr.getSysGuestTypeUuid());
					guestTypeMap4TravelerType.put(sgtr.getSysTravelerTypeUuid(), list);
				}
			}
			if(MapUtils.isEmpty(guestTypeMap4TravelerType)){
				dataUtil.putMessage("系统的游客类型和住客类型没有绑定，请绑定后报价！");
			}
			dataUtil.setGuestTypeMap4TravelerType(guestTypeMap4TravelerType);
		}else{
			dataUtil.putMessage("系统的游客类型和住客类型没有绑定，请绑定后报价！");
		}
		
		
		
		/**第N人的MAP KEY：游客类型简写，value：住客类型UUID*/
		if(MapUtils.isNotEmpty(dataUtil.getPerNumMap())){
			
			/**报价人数大于0的游客类型会动态抓取对应的存在的住客类型 第N人价格*/
			
			//大于0的游客类型参数组装
			Set<Entry<String, Integer>> perNumSet = dataUtil.getPerNumMap().entrySet();
			List<String> shortNameParamList = new ArrayList<String>();
			for(Entry<String, Integer> entry:perNumSet){
				if(entry.getValue()>0){
					shortNameParamList.add(entry.getKey());
				}
			}
			
			List<HotelGuestType> shortNameList = hotelGuestTypeService.findShortNameAndGuestTypeUuidList(UserUtils.getCompanyIdForData().intValue(),shortNameParamList);
			Map<Object, List<HotelGuestType>> shortNameMap = BeanUtil.groupByKeyString("shortName", shortNameList);
			if(MapUtils.isNotEmpty(shortNameMap)){
				Set<Entry<Object, List<HotelGuestType>>> entrySet = shortNameMap.entrySet();
				boolean b = true;
				for(Entry<Object, List<HotelGuestType>> entry:entrySet){
					if(CollectionUtils.isEmpty(entry.getValue())){
						dataUtil.putMessage("第N人配置错误，出现未绑定住客类型的游客类型，请检查并重新配置");
						b=false;
						break;
					}
				}
				if(b){//第N人的 简写和批发商住客类型映射关系
					dataUtil.setShortNameAndGuestTypeMap(shortNameMap);
				}
			}
		}
		
		if(MapUtils.isEmpty(dataUtil.getGuestTypeMap4HotelRoom())){
//			Map<Object, List<SysGuestType>> guestTypeMap=new HashMap<Object, List<SysGuestType>>();
			//查询批发商下的所有绑定住客类型 返回SysGuestType实例（实例中的uuid是hotel_guest_type中的uuid）
			List<SysGuestType> list = sysGuestTypeService.findAllListByCompanyIdAndHotelUuid(UserUtils.getCompanyIdForData().intValue(),query.getHotelUuid());
			
			//根据房型进行分组
			Map<Object, List<SysGuestType>> hotelRoomUuidMap = BeanUtil.groupByKeyString("hotelRoomUuid", list);
			
			dataUtil.setGuestTypeMap4HotelRoom(hotelRoomUuidMap);
		}
		
		/**分组当前批发商下的所有币种信息*/
		if(dataUtil.getCurrencyMap()==null){
			List<Currency> list = currencyService.findCurrencyList(UserUtils.getCompanyIdForData());
			Map<Object, List<Currency>> currencyMap = BeanUtil.groupByKeyString("id", list);
			dataUtil.setCurrencyMap(currencyMap);
		}
		
		/**设置当前批发商的RMB id*/
		if(dataUtil.getRmbCurrencyId()==null||dataUtil.getRmbCurrencyId()==0){
			Currency currency = currencyService.getRMBCurrencyId();
			dataUtil.setRmbCurrencyId(currency.getId());
		}
		
		
		
		/**设置报价条件中的所选 基础餐型或者升级餐型 和 交通*/
		Set<String> mealSet = new HashSet<String>();
		Set<String> islandSet = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(query.getQuotedPriceRoomList())){
			for(QuotedPriceRoomQuery rquery:query.getQuotedPriceRoomList()){
				if(StringUtils.isNotBlank(rquery.getHotelMealRiseUuid())){
					mealSet.add(rquery.getHotelMealRiseUuid());
				}else{
					mealSet.add(rquery.getHotelMealUuid());
				}
			}
		}
		islandSet.add(query.getDepartureIslandWay());
		islandSet.add(query.getArrivalIslandWay());
		dataUtil.setMealSet(mealSet);
		dataUtil.setIslandSet(islandSet);
		
		
		/**-------------------前端需要展示的查询条件属性设置---------------------*/
		query.setCountryText(sysGeographyService.getNameCnByUuid(query.getCountry()));
		query.setIslandText(islandService.getByUuid(query.getIslandUuid()).getIslandName());
		Hotel hotel = hotelService.getByUuid(query.getHotelUuid());
		query.setHotelText(hotel.getNameCn());
		SysCompanyDictView sysCompanyDictView = sysCompanyDictViewService.getByUuId(hotel.getHotelGroup());
		if(sysCompanyDictView != null) {
			query.setHotelGroupText(sysCompanyDictView.getLabel());
			
		}
		query.setDepartureIslandWayText(sysCompanyDictViewService.getByUuId(query.getDepartureIslandWay()).getLabel());
		query.setArrivalIslandWayText(sysCompanyDictViewService.getByUuId(query.getArrivalIslandWay()).getLabel());
		
		
	}
	
	/**
	 * 自动报价计算 add by zhanghao
	 * @param query
	 * @return
	 */
	public QuotedPriceResultJsonBean autoQuotedPrice(QuotedPriceQuery query){
		QuotedPriceResultJsonBean result = new QuotedPriceResultJsonBean();
		result.setStatus("");
		result.setMessage("");
		if(query ==null ){
			result.setStatus("fail");
			result.setMessage("输入报价器条件异常，请重新操作！");
		}else{
			if(validataFormInput(query)){
				AutoQuotedPriceDataUtil dataUtil = new AutoQuotedPriceDataUtil();
				//初始化数据
				try {
					initData(query, dataUtil);
				} catch (Exception e) {
					logger.error("报价初始化数据报错。",e);
					result.setStatus("fail");
					result.setMessage("报价器初始化数据报错，请联系管理员！");
				}
				
				if(dataUtil.getPerNumMap()==null){
					result.setStatus("fail");
					result.setMessage("报价器条件中输入的人数异常，请重新操作！");
				}else if(dataUtil.getRmbCurrencyId()==null){
					result.setStatus("fail");
					result.setMessage("请维护人民币后在进行报价！");
				}else{
				
					//查询价单集合并循环生成价单报价结果
					List<HotelPl> hotelPlList = this.getHotelPlList(query);
					if(CollectionUtils.isNotEmpty(hotelPlList)){
						List<QuotedPriceJsonBean> quotedPriceJsonList = new ArrayList<QuotedPriceJsonBean>();
						
						//循环生成价单报价结果
						for(HotelPl hp:hotelPlList){
							if(dataUtil.getTaxPriceMap()==null || !dataUtil.getTaxPriceMap().containsKey(hp.getUuid())){
								dataUtil.getTaxPriceMap().put(hp.getUuid(), hotelPlTaxPriceService.findHotelPlTaxPricesByHotelPlUuid(hp.getUuid()));
							}
							if(dataUtil.getHotelPlMap()==null || !dataUtil.getHotelPlMap().containsKey(hp.getUuid())){
								dataUtil.getHotelPlMap().put(hp.getUuid(), hp);
							}
							QuotedPriceJsonBean jsonBean=null;
							try {
								jsonBean = this.autoQuotedPriceBySimpleHotelPl(query, hp,dataUtil);
								
							} catch (Exception e) {
								dataUtil.putMessage("当前价单报价异常：价单名称+"+hp.getName());
								logger.error("当前价单报价异常：价单名称+"+hp.getName(),e);
								e.printStackTrace();
							}
							
							if(jsonBean!=null){
								quotedPriceJsonList.add(jsonBean);
							}
						}
						if(StringUtils.isNotBlank(dataUtil.getMessage().toString())){
							result.setMessage(dataUtil.getMessage().toString());
						}
						//有报价结果时，返回success 和报价结果
						if(CollectionUtils.isNotEmpty(quotedPriceJsonList)){
							result.setQuotedPriceJsonList(quotedPriceJsonList);
							result.setStatus("success");
						}else{
							result.setStatus("fail");
							result.setMessage("没有符合条件的报价结果，请重新输入报价条件！");
						}
						
					}else{
						result.setStatus("fail");
						result.setMessage("查询不到价单信息，请您先维护价单再进行报价！");
					}
				}
			}else{
				result.setStatus("fail");
				result.setMessage("报价信息输入不全，请填写！");
			}
		}
		
		return result;
	}
	
	/**
	 * 查询条件的重新设置
	 * @param bean
	 * @param query
	 */
	private void setQueryParam(QuotedPriceJsonBean bean,QuotedPriceQuery query,HotelPl hotelPl){
		
		//根据价单把当前价单的地接供应商和采购类型set到query中，供报价结果展示使用
		query.setSupplierInfoId(hotelPl.getSupplierInfoId());
		SupplierInfo supplier = supplierInfoService.findSupplierInfoById(Long.parseLong(query.getSupplierInfoId().toString()));
		if(supplier!=null){
			query.setSupplierInfoText(supplier.getSupplierName());
		}
		query.setPurchaseType(hotelPl.getPurchaseType());
		if(query.getPurchaseType()==0){
			query.setPurchaseTypeText("内采");
		}else if(query.getPurchaseType()==1){
			query.setPurchaseTypeText("外采");
		}
		
		QuotedPriceQuery querynew = new QuotedPriceQuery();
		BeanUtil.copySimpleProperties(querynew, query);
		querynew.setPersonNum(query.getPersonNum());
		List<QuotedPriceRoomQuery> list = new ArrayList<QuotedPriceRoomQuery>();
		if(CollectionUtils.isNotEmpty(query.getQuotedPriceRoomList())){
			for(QuotedPriceRoomQuery rQuery:query.getQuotedPriceRoomList()){
				QuotedPriceRoomQuery nquery = new QuotedPriceRoomQuery();
				BeanUtil.copySimpleProperties(nquery, rQuery);
				list.add(nquery);
			}
			querynew.setQuotedPriceRoomList(list);
		}
		
		bean.setQuotedPriceQuery(querynew);
	}
	
	/**
	 * 单个价单的报价结果 add by zhanghao
	 * 
	 * 
	 * @param query 报价器条件
	 * @param hotelPl 根据报价器条件查询出的价单信息
	 * @param perNumMap 游客类型和对应的游客人数 KEY是游客类型的简写 ：A成人 B婴儿 C儿童 O老人 S特殊人群 VALUE 是对应的游客人数
	 * @return
	 */
	private QuotedPriceJsonBean autoQuotedPriceBySimpleHotelPl(QuotedPriceQuery query,HotelPl hotelPl ,AutoQuotedPriceDataUtil dataUtil){
		QuotedPriceJsonBean bean = new QuotedPriceJsonBean();
		
		//设置查询条件
		setQueryParam(bean, query,hotelPl);
		
		//设置混住费用 
		setMixlivePrice(bean, query, hotelPl, dataUtil);
		bean.setMemo(hotelPl.getMemo()==null?"":hotelPl.getMemo());
		bean.setHotelPlUuid(hotelPl.getUuid());
		
		List<QuotedPriceDetailJsonBean> detailList = new ArrayList<QuotedPriceDetailJsonBean>();
		
		try {
			List<QuotedPriceRoomQuery> quotedPriceRoomList = query.getQuotedPriceRoomList();
			
			for(int i=0;i<quotedPriceRoomList.size();i++){
				QuotedPriceRoomQuery roomQuery=quotedPriceRoomList.get(i);
				QuotedPriceDetailJsonBean detailBean=new QuotedPriceDetailJsonBean();
				detailList.add(detailBean);
				
				detailBean.setInDate(DateUtils.date2String(roomQuery.getInDate()));
				detailBean.setHotelRoomUuid(roomQuery.getHotelRoomUuid());
				detailBean.setHotelRoomName(roomQuery.getHotelRoomText());
				
				//容住率设置
				if(StringUtils.isBlank(dataUtil.getHotelRoomOccupancyRateString(roomQuery.getHotelRoomUuid()))){
					HotelRoom room = hotelRoomService.getByUuid(roomQuery.getHotelRoomUuid());
					dataUtil.getRoomOccupancyRateMap().put(roomQuery.getHotelRoomUuid(), room.getOccupancyRate());
				}
				detailBean.setHotelRoomOccupancyRate(dataUtil.getRoomOccupancyRateMap().get(roomQuery.getHotelRoomUuid()));
				
				
				//容住率备注设置
				if(StringUtils.isBlank(dataUtil.getHotelRoomOccupancyRateNoteString(roomQuery.getHotelRoomUuid()))){
					HotelRoomOccuRate hotelRoomOccuRate = new HotelRoomOccuRate();
					hotelRoomOccuRate.setHotelRoomUuid(roomQuery.getHotelRoomUuid());
					hotelRoomOccuRate.setDelFlag("0");
					List<HotelRoomOccuRate> roccurateList = hotelRoomOccuRateService.find(hotelRoomOccuRate);
					if(CollectionUtils.isNotEmpty(roccurateList)){
						StringBuffer sb = new StringBuffer();
						for(HotelRoomOccuRate rocc:roccurateList){
							if(StringUtils.isNotBlank(rocc.getRemark())){
								sb.append(rocc.getRemark());
								sb.append(HotelRoomOccuRate.SPLIT_FLAG);
							}
						}
						if(sb.length()>0){
							sb.deleteCharAt(sb.lastIndexOf(HotelRoomOccuRate.SPLIT_FLAG));
						}
						dataUtil.getRoomOccupancyRateNoteMap().put(roomQuery.getHotelRoomUuid(), sb.toString());
					}else{
						dataUtil.getRoomOccupancyRateNoteMap().put(roomQuery.getHotelRoomUuid(), "");
					}
				}
				detailBean.setMemo(dataUtil.getRoomOccupancyRateNoteMap().get(roomQuery.getHotelRoomUuid()));
				
				if(StringUtils.isNotBlank(roomQuery.getHotelMealRiseUuid())){
					detailBean.setHotelMealText(roomQuery.getHotelMealRiseText());
					detailBean.setHotelMealUuid(roomQuery.getHotelMealRiseUuid());
				}else{
					detailBean.setHotelMealText(roomQuery.getHotelMealText());
					detailBean.setHotelMealUuid(roomQuery.getHotelMealUuid());
				}
				
				
				//动态或得 交通方式
				String islandwayUuid="";
				if(i==0){
					islandwayUuid=query.getDepartureIslandWay();
				}else if(i==quotedPriceRoomList.size()-1){
					islandwayUuid=query.getArrivalIslandWay();
				}
				if(quotedPriceRoomList.size()==1){
					islandwayUuid=query.getDepartureIslandWay()+";"+query.getArrivalIslandWay();
				}

				//根据住客类型 对 不同的游客类型进行报价
				setGuestTypePrice(query, roomQuery, detailBean, bean, islandwayUuid, hotelPl, dataUtil);
				
				//第三人报价
				setGuestTypePrice4ThirdPerson(query, roomQuery, detailBean, bean, islandwayUuid, hotelPl, dataUtil);
				
				
			}
			bean.setDetailList(detailList);
			
			//同步明细价格和合计价格
			this.synQuotedData(bean);
			
			if(checkPrefer(bean)){//判断是否有价格报出，如果没有价格报出则不会进行优惠的相关筛选和计算
				//设置可以使用的优惠信息
				setPreferentialList(bean,hotelPl,dataUtil);
			}
		} catch (Exception e) {
			logger.error("报价器根据条件报价时出现异常！",e);
			e.printStackTrace();
			return new QuotedPriceJsonBean();
		}
		
		return bean;
	}	
	
	/**
	 * 判断是否当前报价结果中是否有可用价格（即：不为0的价格记录）add by zhanghao
	 * 如果有则返回true，否则false；
	 * @param bean
	 * @return
	 */
	private boolean checkPrefer(QuotedPriceJsonBean bean ){
		boolean b = false;
		double moneyAmount=0d;
		List<GuestPriceJsonBean> list = bean.getGuestPriceList();
		if(CollectionUtils.isNotEmpty(list)){//循环累计报价金额，如果大于0证明报出价格，反之没有报出价格
			for(GuestPriceJsonBean jsonBean:list){
				moneyAmount+=jsonBean.getAmount();
			}
		}
		if(moneyAmount>0){
			b=true;
		}
		return b;
	}
	/**
	 * 为了保证数据的一致性，报价结束后需要进行统一的校验
	 * 多条明细结果中，假设有一条数据中的一列价格为空即清空所有明细中的价格，同时清空该列对应的合计价格
	 * add by zhanghao
	 * @param bean
	 */
	private void synQuotedData(QuotedPriceJsonBean bean){
		
		Map<Integer, Map<String, List<GuestPriceJsonBean>>> groupMap = this.groupDetailGuestList(bean);
		
		//根据游客类型或得每天的价格集合
		Map<String, List<GuestPriceJsonBean>> groupMap0 = groupMap.get(0);
		if(CollectionUtils.isNotEmpty(bean.getGuestPriceList())){
			for(GuestPriceJsonBean totalBean:bean.getGuestPriceList()){
				if(totalBean.getIsThirdPerson()!=0){//非正常游客，即：第N人 跳过
					continue;
				}
				//当前游客类型没有报价结果，则清空所有明细的价格
				if(totalBean.getAmount()==null || totalBean.getAmount()==0){
					if(CollectionUtils.isNotEmpty(groupMap0.get(totalBean.getTravelerType()))){
						for(GuestPriceJsonBean gpj:groupMap0.get(totalBean.getTravelerType())){
							gpj.setAmount(0d);
							gpj.setRoomAmount(0d);
							gpj.setRoomAmountTotal(0d);
						}
					}
				}else{//有报价结果，判断明细中是否有没有报价结果的记录
					if(CollectionUtils.isNotEmpty(groupMap0.get(totalBean.getTravelerType()))){
						boolean b = false;
						for(GuestPriceJsonBean gpj:groupMap0.get(totalBean.getTravelerType())){
							if(gpj.getAmount()==null||gpj.getAmount()==0){
								b=true;
								break;
							}
						}
						if(b){//存在没有价格的记录，则清空所有明细价格，同时清空合计价格
							for(GuestPriceJsonBean gpj:groupMap0.get(totalBean.getTravelerType())){
								gpj.setAmount(0d);
								gpj.setRoomAmount(0d);
								gpj.setRoomAmountTotal(0d);
							}
							totalBean.setAmount(0d);
							totalBean.setRoomAmount(0d);
							totalBean.setRoomAmountTotal(0d);
						}
					}
				}
			}
		}
				
		//第N人的价格统一处理
		Map<String, List<GuestPriceJsonBean>> groupMap1 = groupMap.get(1);
		if(CollectionUtils.isNotEmpty(bean.getGuestPriceList())){
			for(GuestPriceJsonBean totalBean:bean.getGuestPriceList()){
				if(totalBean.getIsThirdPerson()==0){//正常游客 跳过
					continue;
				}
				//当前游客类型没有报价结果，则清空所有明细的价格
				if(totalBean.getAmount()==null || totalBean.getAmount()==0){
					if(CollectionUtils.isNotEmpty(groupMap1.get(totalBean.getTravelerType()))){
						for(GuestPriceJsonBean gpj:groupMap1.get(totalBean.getTravelerType())){
							gpj.setAmount(0d);
							gpj.setRoomAmount(0d);
							gpj.setRoomAmountTotal(0d);
						}
					}
				}else{//有报价结果，判断明细中是否有没有报价结果的记录
					if(CollectionUtils.isNotEmpty(groupMap1.get(totalBean.getTravelerType()))){
						boolean b = false;
						for(GuestPriceJsonBean gpj:groupMap1.get(totalBean.getTravelerType())){
							if(gpj.getAmount()==null||gpj.getAmount()==0){
								b=true;
								break;
							}
						}
						if(b){//存在没有价格的记录，则清空所有明细价格，同时清空合计价格
							for(GuestPriceJsonBean gpj:groupMap1.get(totalBean.getTravelerType())){
								gpj.setAmount(0d);
								gpj.setRoomAmount(0d);
								gpj.setRoomAmountTotal(0d);
							}
							totalBean.setAmount(0d);
							totalBean.setRoomAmount(0d);
							totalBean.setRoomAmountTotal(0d);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 设置混住费用 add by zhanghao
	 * @param bean
	 * @param query
	 * @param hotelPl
	 */
	private void setMixlivePrice(QuotedPriceJsonBean bean,QuotedPriceQuery query,HotelPl hotelPl ,AutoQuotedPriceDataUtil dataUtil){
		bean.setMixlivePrice(0d);
		bean.setMixlivePriceCurrencyId(dataUtil.getRmbCurrencyId().toString());
		bean.setMixlivePriceCurrencyText("-");
		if(hotelPl.getMixliveAmount()!=null){
			Double mixlivePrice = hotelPl.getMixliveAmount()*query.getMixliveNum();
			Map<String, Object> rmap = this.tranfer2RMB4exchangeRate(mixlivePrice, hotelPl.getMixliveCurrencyId().toString(), dataUtil.getCurrencyObject(Long.valueOf(hotelPl.getMixliveCurrencyId())).getCurrencyMark(),dataUtil);
			bean.setMixlivePrice(Double.parseDouble(rmap.get("amount").toString()));
			bean.setMixlivePriceCurrencyId(rmap.get("currencyId").toString());
			bean.setMixlivePriceCurrencyText(rmap.get("currencyText").toString());
		}
	}
	
	/**
	 * 根据住客类型 对 不同的游客类型进行报价 add by zhanghao
	 * @param query
	 * @param roomQuery
	 * @param detailBean
	 * @param bean
	 * @param islandwayUuid
	 * @param hotelPl
	 */
	private void setGuestTypePrice(QuotedPriceQuery query,QuotedPriceRoomQuery roomQuery,QuotedPriceDetailJsonBean detailBean,QuotedPriceJsonBean bean,String islandwayUuid,HotelPl hotelPl,AutoQuotedPriceDataUtil dataUtil ){
		
		
		//如果没有报价 的 游客类型 动态 价格 则新建
		List<GuestPriceJsonBean> allGuestPriceList = bean.getGuestPriceList();
		if(CollectionUtils.isEmpty(allGuestPriceList)){
			allGuestPriceList=new ArrayList<GuestPriceJsonBean>();
			bean.setGuestPriceList(allGuestPriceList);
		}
		//按游客类型分组的 报价结果
		Map<Object, List<GuestPriceJsonBean>> priceListGroup4TravelerType = BeanUtil.groupByKeyString("travelerType", allGuestPriceList);
		
		
		//报价明细 价格设置
		List<GuestPriceJsonBean> guestPriceList = new ArrayList<GuestPriceJsonBean>();
		Set<String> keySet = dataUtil.getPerNumMap().keySet();
		for(String shortName:keySet){
			GuestPriceJsonBean priceBean = new GuestPriceJsonBean();
			TravelerType travelerType = dataUtil.getTravelerTypeMap().get(shortName);
			priceBean.setTravelerType(travelerType.getUuid());
			priceBean.setTravelerTypeText(travelerType.getName());
			priceBean.setInDate(detailBean.getInDate());
			priceBean.setHotelPlUuid(hotelPl.getUuid());
			
			Map<String, Object> rmbMap = null;
			
			//如果报价人数是0，则不进行报价默认返回0
			if(dataUtil.getPerNumMap().get(shortName)==null || dataUtil.getPerNumMap().get(shortName)==0){
				
				String currencyId=hotelPl.getCurrencyId().toString();//币种id
				String currencyText="";
				if(dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString()))!=null){
					currencyText=dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString())).getCurrencyMark();//币种输出符号
				}
				
				rmbMap = this.tranfer2RMB4exchangeRate(0d, currencyId, currencyText,dataUtil);
			}else if(travelerType.getPersonType()==null){
				String currencyId=hotelPl.getCurrencyId().toString();//币种id
				String currencyText="";
				if(dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString()))!=null){
					currencyText=dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString())).getCurrencyMark();//币种输出符号
				}
				String message = "住客类型“"+travelerType.getName()+"”未绑定人员类型！";
				if(dataUtil.getMessage().indexOf(message)==-1){
					dataUtil.putMessage(message);
				}
				rmbMap = this.tranfer2RMB4exchangeRate(0d, currencyId, currencyText,dataUtil);
			}else{ 
				
				if(travelerType.getPersonType()!=null&&travelerType.getPersonType()==0){//成人
					rmbMap = setAdultPrice(query, roomQuery, islandwayUuid, hotelPl,priceBean,shortName, dataUtil);
				}else if(travelerType.getPersonType()!=null&&travelerType.getPersonType()==1){//婴儿
//					婴儿的特殊规则去掉（原来不计算交通费），现在也计算（原方法保留）
//					rmbMap = setBabyPrice(query, roomQuery,  hotelPl,priceBean, dataUtil);
					rmbMap = setBabyPrice( roomQuery, islandwayUuid, hotelPl, priceBean,shortName, dataUtil);
				}else if(travelerType.getPersonType()!=null&&travelerType.getPersonType()==2){//儿童
					rmbMap = setChildPrice( roomQuery, islandwayUuid, hotelPl,priceBean,shortName, dataUtil);
				}else{
//					dataUtil.putMessage("不存在游客类型简写：“"+dataUtil.getTravelerTypeMap().get(shortName).getName()+"”的报价规则，当前按成人游客的报价规则进行报价。");
					//其他类型按 成人报价
					rmbMap = setAdultPrice(query, roomQuery, islandwayUuid, hotelPl,priceBean,shortName, dataUtil);
				}
			}
			if(MapUtils.isEmpty(rmbMap)){
				rmbMap =new HashMap<String, Object>(); 
				priceBean.setCurrencyId(dataUtil.getRmbCurrencyId().toString());
				priceBean.setCurrencyText("-");
				priceBean.setAmount(0d);
			}else{
				priceBean.setCurrencyId((String)rmbMap.get("currencyId"));
				priceBean.setCurrencyText((String)rmbMap.get("currencyText"));
				priceBean.setAmount((double)rmbMap.get("amount"));
			}
			
			
			guestPriceList.add(priceBean);
			
			if(priceListGroup4TravelerType!=null&&priceListGroup4TravelerType.containsKey(dataUtil.getTravelerTypeMap().get(shortName).getUuid())){
				List<GuestPriceJsonBean> groupList = priceListGroup4TravelerType.get(dataUtil.getTravelerTypeMap().get(shortName).getUuid());
				if(CollectionUtils.isNotEmpty(groupList)){
					GuestPriceJsonBean groupBean = groupList.get(0);
					groupBean.setAmount(formatDouble(groupBean.getAmount()+priceBean.getAmount()));
					
				}else{
					GuestPriceJsonBean priceBean1 = new GuestPriceJsonBean();
					BeanUtil.copySimpleProperties(priceBean1, priceBean);
					allGuestPriceList.add(priceBean1);
				}
			}else{
				GuestPriceJsonBean priceBean1 = new GuestPriceJsonBean();
				BeanUtil.copySimpleProperties(priceBean1, priceBean);
				allGuestPriceList.add(priceBean1);
			}
		}
		
		detailBean.setGuestPriceList(guestPriceList);
	}
	/**
	 * 金额的格式化处理
	 * 默认保留2位小数，四舍五入 add by zhanghao
	 * @param value
	 * @return
	 */
	private double formatDouble (double value){
		if(value==0)return value;
		DecimalFormat df = new DecimalFormat("#.00");   
		double result = Double.valueOf(df.format(value));
		return result;
	}
	
	/**
	 * 设置 报价明细 中的 成人 价格 add by zhanghao
	 * 
	 * @param query 报价条件
	 * @param roomQuery 报价日期条件集合中的单条记录
	 * @param islandwayUuid 岛屿UUID
	 * @param hotelPl 当前价单信息
	 * @return
	 */
	private Map<String, Object>  setAdultPrice(QuotedPriceQuery query,QuotedPriceRoomQuery roomQuery,String islandwayUuid,HotelPl hotelPl ,GuestPriceJsonBean priceBean,String shortName,AutoQuotedPriceDataUtil dataUtil){


		String currencyId=hotelPl.getCurrencyId().toString();//币种id
		String currencyText="";
		if(dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString()))!=null){
			currencyText=dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString())).getCurrencyMark();//币种输出符号
		}
		Double amount=0d;//价格

		Double islandwayPriceAmount = 0d;
		Double mealPriceAmount = 0d;
		Double roomPriceAmount = 0d;
		
		//是否进行整体报价标示，true时会进行报价
		boolean flag=false;
		
		/**交通费*/
		Map<String, Object> computeIslandwayPriceMap = computeIslandwayPrice(islandwayUuid, roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ISLAND,priceBean,dataUtil);
		flag=(boolean)computeIslandwayPriceMap.get("flag");
		if(flag){
			islandwayPriceAmount=(double)computeIslandwayPriceMap.get("amount");
		}
		
		//判断是否继续进行下面报价，如果可以则初始化 flag标示为false
		if(!flag){
			return null;
		}else{
			flag=false;
		}
		
		/**计算升餐费*/
		
		Map<String, Object> computeMealPriceMap = computeMealPrice( roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_MEAL,priceBean,dataUtil);
		flag=(boolean)computeMealPriceMap.get("flag");
		if(flag){
			mealPriceAmount=(double)computeMealPriceMap.get("amount");
		}
		
		//判断是否继续进行下面报价，如果可以则初始化 flag标示为false
		if(!flag){
			return null;
		}else{
			flag=false;
		}
		
		
		/**计算房费*/
		
		
		Integer perNum = dataUtil.getPerNumMap().get(shortName);
		Integer thirdNum = 0;
		
		//得到 降序后升序 后的住客类型UUID的map
		List<Map<String, Object>> guestTypeUuidMapList = this.getSequenceHotelGuestTypeUuidList(shortName, perNum, dataUtil,roomQuery.getHotelRoomUuid(),roomQuery.getHotelRoomText(),hotelPl);
		if(CollectionUtils.isEmpty(guestTypeUuidMapList)){
			return null;
		}
		
		//需要补交的第三人价格设置
		GuestPriceJsonBean priceBeanTemp = new GuestPriceJsonBean();
		String thirdGuestTypeUuid = this.getGuestTypeUuid(shortName,3,SysGuestType.SYS_GUEST_TYPE_TYPE_1,dataUtil,roomQuery.getHotelRoomUuid(),roomQuery.getHotelRoomText());
		Map<String, Object> thirdComputeRoomPriceMap = computeRoomPrice(roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ROOM,thirdGuestTypeUuid,priceBeanTemp,dataUtil);
		
		
		Map<String, Object> computeRoomPriceMap = null;
		for(Map<String, Object> guestTypeUuidMap:guestTypeUuidMapList){
			String guestTypeUuid = (String)guestTypeUuidMap.get("guestTypeUuid");
			int bedTaxNum = 1;
			if(guestTypeUuidMap.containsKey("thirdNum")&&StringUtils.isNotBlank(guestTypeUuidMap.get("thirdNum").toString())){
				bedTaxNum = perNum-Integer.parseInt(guestTypeUuidMap.get("thirdNum").toString());
			}else{
				bedTaxNum=perNum;
			}
			computeRoomPriceMap = computeRoomPrice(roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ROOM,guestTypeUuid,priceBean,dataUtil,bedTaxNum);
			if(Boolean.parseBoolean(computeRoomPriceMap.get("flag").toString())){
				//需要取第三人价格的时候如果没有第三人价格则跳过，此处不能break，防止有不需要第三人价格就可以报价的情况
				if(guestTypeUuidMap.containsKey("thirdNum")&&!guestTypeUuidMap.get("thirdNum").toString().equals("0")&&!(boolean)thirdComputeRoomPriceMap.get("flag")){
					//update by zhanghao 20151028 修复 bug9063
					thirdNum = Integer.parseInt(guestTypeUuidMap.get("thirdNum").toString());
					continue;
				}else if(!guestTypeUuidMap.containsKey("thirdNum")){//不需要取第三人价的时候直接按新价格跳出
					break;
				}else{//需要取第三人价格，并且存在第三人价格，则得到需要计算的第三人数量并跳出
					thirdNum = Integer.parseInt(guestTypeUuidMap.get("thirdNum").toString());
					break;
				}
			}
		}
		
		
		flag=(boolean)computeRoomPriceMap.get("flag");
		if(flag){
			roomPriceAmount=(double)computeRoomPriceMap.get("amount");
			
			if(thirdNum>0){//大于0时补交第三人差价后 在平均 得出每个人的房费
				
				flag=(boolean)thirdComputeRoomPriceMap.get("flag");
				if(flag){
					//补交第三人差价
					double thirdRoomPriceAmount=(double)thirdComputeRoomPriceMap.get("amount");
					roomPriceAmount=roomPriceAmount+thirdRoomPriceAmount*thirdNum;
					roomPriceAmount=roomPriceAmount/perNum;
					priceBean.setRoomAmount((priceBean.getRoomAmount()+priceBeanTemp.getRoomAmount()*thirdNum)/perNum);
					priceBean.setRoomAmountTotal((priceBean.getRoomAmountTotal()+priceBeanTemp.getRoomAmountTotal()*thirdNum)/perNum);
				}
				
			}else{//平均得出没人房费
				roomPriceAmount=roomPriceAmount/perNum;
				priceBean.setRoomAmount((priceBean.getRoomAmount())/perNum);
				priceBean.setRoomAmountTotal((priceBean.getRoomAmountTotal())/perNum);
			}
			
		}
		
		if(flag){
			//价格
			amount=islandwayPriceAmount+mealPriceAmount+roomPriceAmount;
			//转换成人民币--
			Map<String, Object> rmbMap = tranfer2RMB4exchangeRate(amount, currencyId, currencyText,dataUtil);
			return rmbMap;
			
		}
		
		return null;
		
	}
	
	/**
	 * 根据成人 数量 返回【有序的】 住客类型的UUID 遵循先降后升的原则 add by zhanghao
	 * @param travelerTypeShortName 游客类型简写 A成人 B婴儿 C儿童 O老人 S特殊人群
	 * @param  perNum 游客数量
	 * @param dataUtil
	 * @param hotelRoomUuid
	 * @param hotelRoomName
	 * @param hotelPl
	 * @return List<Map<String,Object>> Map KEY(guestTypeUuid,thirdNum):住客类型uuid value:第三人成人价的个数
	 */
	private List<Map<String,Object>> getSequenceHotelGuestTypeUuidList(String travelerTypeShortName,Integer perNum ,AutoQuotedPriceDataUtil dataUtil,String hotelRoomUuid,String hotelRoomName,HotelPl hotelPl){
		Integer maxTypeValue = 0;
		
		List<SysGuestType> guestTypeList = dataUtil.getGuestTypeMap4HotelRoom().get(hotelRoomUuid);
		if(CollectionUtils.isNotEmpty(guestTypeList)){
			for(SysGuestType type:guestTypeList){
				if(maxTypeValue<type.getValue()){
					maxTypeValue=type.getValue();
				}
			}
		}
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=perNum;i>0;i--){
			String guestTypeUuid = getGuestTypeUuid(travelerTypeShortName, i, SysGuestType.SYS_GUEST_TYPE_TYPE_0,dataUtil,hotelRoomUuid,hotelRoomName);
			if(guestTypeUuid==null)continue;
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("guestTypeUuid", guestTypeUuid);
			resultMap.put("thirdNum", perNum-i);
			list.add(resultMap);
		}
		
		for(int i=perNum;i<=maxTypeValue;i++){
			String guestTypeUuid = getGuestTypeUuid(travelerTypeShortName, i, SysGuestType.SYS_GUEST_TYPE_TYPE_0,dataUtil,hotelRoomUuid,hotelRoomName);
			if(guestTypeUuid==null)continue;
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("guestTypeUuid", guestTypeUuid);
			list.add(resultMap);
		}
		if(CollectionUtils.isEmpty(list)){
			String message = "当前价单\""+hotelPl.getName()+"\"游客类型和住客类型没有绑定！";
			if(dataUtil.getMessage().indexOf(message)==-1){
				dataUtil.putMessage(message);
			}
		}
		return list;
	}
	
	/**
	 * 设置 报价明细 中的 婴儿 价格 add by zhanghao
	 * 婴儿不需要计算交通费用
	 * 
	 * @param query 报价条件
	 * @param roomQuery 报价日期条件集合中的单条记录
	 * @param hotelPl 当前价单信息
	 */
	private Map<String, Object> setBabyPrice(QuotedPriceRoomQuery roomQuery,HotelPl hotelPl ,GuestPriceJsonBean priceBean,String shortName,AutoQuotedPriceDataUtil dataUtil){
		String currencyText="";
		if(dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString()))!=null){
			currencyText=dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString())).getCurrencyMark();//币种输出符号
		}
		Double amount=0d;//价格

		Double islandwayPriceAmount = 0d;
		Double mealPriceAmount = 0d;
		Double roomPriceAmount = 0d;
		
		//是否进行整体报价标示，true时会进行报价
		boolean flag=false;
		
		/**计算升餐费*/
		Map<String, Object> computeMealPriceMap = computeMealPrice( roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_MEAL,priceBean,dataUtil);
		flag=(boolean)computeMealPriceMap.get("flag");
		if(flag){
			mealPriceAmount=(double)computeMealPriceMap.get("amount");
		}
		
		//判断是否继续进行下面报价，如果可以则初始化 flag标示为false
		if(!flag){
			return null;
		}else{
			flag=false;
		}
		
		
		/**计算房费*/
		String guestTypeUuid = this.getGuestTypeUuid(shortName,1,SysGuestType.SYS_GUEST_TYPE_TYPE_0,dataUtil,roomQuery.getHotelRoomUuid(),roomQuery.getHotelRoomText());
		Map<String, Object> computeRoomPriceMap = computeRoomPrice(roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ROOM,guestTypeUuid,priceBean, dataUtil);
		
		flag=(boolean)computeRoomPriceMap.get("flag");
		if(flag){
			roomPriceAmount=(double)computeRoomPriceMap.get("amount");
		}
		
		if(flag){
			//价格
			amount=islandwayPriceAmount+mealPriceAmount+roomPriceAmount;
			
			String currencyId = hotelPl.getCurrencyId().toString();//币种id
			
			//转换成人民币--
			Map<String, Object> rmbMap = tranfer2RMB4exchangeRate(amount, currencyId, currencyText,dataUtil);
			return rmbMap;
			
		}
		return null;
		
	}
	/**
	 * 重载婴儿的计算价格方法，在原方法基础上增加了
	 * 
	 * @param query 报价条件
	 * @param roomQuery 报价日期条件集合中的单条记录
	 * @param hotelPl 当前价单信息
	 */
	private Map<String, Object> setBabyPrice(QuotedPriceRoomQuery roomQuery,String islandwayUuid,HotelPl hotelPl ,GuestPriceJsonBean priceBean,String shortName,AutoQuotedPriceDataUtil dataUtil){
		String currencyId=hotelPl.getCurrencyId().toString();//币种id
		String currencyText="";
		if(dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString()))!=null){
			currencyText=dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString())).getCurrencyMark();//币种输出符号
		}
		Double amount=0d;//价格

		Double islandwayPriceAmount = 0d;
		Double mealPriceAmount = 0d;
		Double roomPriceAmount = 0d;
		
		//是否进行整体报价标示，true时会进行报价
		boolean flag=false;
		
		/**交通费*/
		Map<String, Object> computeIslandwayPriceMap = computeIslandwayPrice(islandwayUuid, roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ISLAND,priceBean,dataUtil);
		flag=(boolean)computeIslandwayPriceMap.get("flag");
		if(flag){
			islandwayPriceAmount=(double)computeIslandwayPriceMap.get("amount");
		}
		
		//判断是否继续进行下面报价，如果可以则初始化 flag标示为false
		if(!flag){
			return null;
		}else{
			flag=false;
		}
		
		
		/**计算升餐费*/
		Map<String, Object> computeMealPriceMap = computeMealPrice(roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_MEAL,priceBean,dataUtil);
		flag=(boolean)computeMealPriceMap.get("flag");
		if(flag){
			mealPriceAmount=(double)computeMealPriceMap.get("amount");
		}
		
		//判断是否继续进行下面报价，如果可以则初始化 flag标示为false
		if(!flag){
			return null;
		}else{
			flag=false;
		}
		
		
		/**计算房费*/
		String guestTypeUuid = this.getGuestTypeUuid(shortName,1,SysGuestType.SYS_GUEST_TYPE_TYPE_0,dataUtil,roomQuery.getHotelRoomUuid(),roomQuery.getHotelRoomText());
		Map<String, Object> computeRoomPriceMap = computeRoomPrice(roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ROOM,guestTypeUuid,priceBean, dataUtil);
		
		flag=(boolean)computeRoomPriceMap.get("flag");
		if(flag){
			roomPriceAmount=(double)computeRoomPriceMap.get("amount");
		}
		
		if(flag){
			//价格
			amount=islandwayPriceAmount+mealPriceAmount+roomPriceAmount;
			
			
			//转换成人民币--
			Map<String, Object> rmbMap = tranfer2RMB4exchangeRate(amount, currencyId, currencyText,dataUtil);
			return rmbMap;
			
		}
		return null;
		
	}
	/**
	 * 设置 报价明细 中的 儿童 价格 add by zhanghao
	 * 
	 * @param roomQuery 报价详情条件
	 * @param islandwayUuid 往返的交通方式
	 * @param hotelPl 价单信息
	 * @param priceBean 当前设置详细价格的对象
	 * @param shortName 游客类型简写
	 * @param dataUtil
	 */
	private Map<String, Object> setChildPrice(QuotedPriceRoomQuery roomQuery,String islandwayUuid,HotelPl hotelPl ,GuestPriceJsonBean priceBean,String shortName,AutoQuotedPriceDataUtil dataUtil){

		String currencyId=hotelPl.getCurrencyId().toString();//币种id
		String currencyText="";
		if(dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString()))!=null){
			currencyText=dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString())).getCurrencyMark();//币种输出符号
		}
		Double amount=0d;//价格

		Double islandwayPriceAmount = 0d;
		Double mealPriceAmount = 0d;
		Double roomPriceAmount = 0d;
		
		//是否进行整体报价标示，true时会进行报价
		boolean flag=false;
		
		/**交通费*/
		Map<String, Object> computeIslandwayPriceMap = computeIslandwayPrice(islandwayUuid, roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ISLAND,priceBean,dataUtil);
		flag=(boolean)computeIslandwayPriceMap.get("flag");
		if(flag){
			islandwayPriceAmount=(double)computeIslandwayPriceMap.get("amount");
		}
		
		//判断是否继续进行下面报价，如果可以则初始化 flag标示为false
		if(!flag){
			return null;
		}else{
			flag=false;
		}
		
		/**计算升餐费*/
		Map<String, Object> computeMealPriceMap = computeMealPrice( roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_MEAL,priceBean,dataUtil);
		flag=(boolean)computeMealPriceMap.get("flag");
		if(flag){
			mealPriceAmount=(double)computeMealPriceMap.get("amount");
		}
		
		//判断是否继续进行下面报价，如果可以则初始化 flag标示为false
		if(!flag){
			return null;
		}else{
			flag=false;
		}
		
		
		/**计算房费*/
		String guestTypeUuid = this.getGuestTypeUuid(shortName,1,SysGuestType.SYS_GUEST_TYPE_TYPE_0,dataUtil,roomQuery.getHotelRoomUuid(),roomQuery.getHotelRoomText());
		Map<String, Object> computeRoomPriceMap = computeRoomPrice(roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ROOM,guestTypeUuid,priceBean,dataUtil);
		
		flag=(boolean)computeRoomPriceMap.get("flag");
		if(flag){
			roomPriceAmount=(double)computeRoomPriceMap.get("amount");
		}
		
		if(flag){
			//价格
			amount=islandwayPriceAmount+mealPriceAmount+roomPriceAmount;
			
			
			//转换成人民币--
			Map<String, Object> rmbMap = tranfer2RMB4exchangeRate(amount, currencyId, currencyText,dataUtil);
			return rmbMap;
			
		}
		return null;
		
	}
	
	
	/**
	 * 根据住客类型 对 不同的游客类型进行报价
	 * 针对第N人的报价计算  add by zhanghao
	 * @param query
	 * @param roomQuery
	 * @param detailBean
	 * @param bean
	 * @param islandwayUuid
	 * @param hotelPl
	 */
	private void setGuestTypePrice4ThirdPerson(QuotedPriceQuery query,QuotedPriceRoomQuery roomQuery,QuotedPriceDetailJsonBean detailBean,QuotedPriceJsonBean bean,String islandwayUuid,HotelPl hotelPl,AutoQuotedPriceDataUtil dataUtil ){
		
		if(MapUtils.isEmpty(dataUtil.getShortNameAndGuestTypeMap())){
			return ;
		}
		
		//如果没有报价 的 游客类型 动态 价格 则新建
		List<GuestPriceJsonBean> allGuestPriceList = bean.getGuestPriceList();
		if(CollectionUtils.isEmpty(allGuestPriceList)){
			allGuestPriceList=new ArrayList<GuestPriceJsonBean>();
			bean.setGuestPriceList(allGuestPriceList);
		}
		//按游客类型分组的 报价结果
		Map<Object, List<GuestPriceJsonBean>> priceListGroup4TravelerType = null;
		//按是否是第三人状态分组
		Map<Object, List<GuestPriceJsonBean>> priceListGroup4TravelerTypeMap = BeanUtil.groupByKeyString("isThirdPerson", allGuestPriceList);
		if(MapUtils.isNotEmpty(priceListGroup4TravelerTypeMap)){
			if(CollectionUtils.isNotEmpty(priceListGroup4TravelerTypeMap.get(1))){//第N人集合按住客类型再进行分组
				priceListGroup4TravelerType = BeanUtil.groupByKeyString("guestType", priceListGroup4TravelerTypeMap.get(1));
			}
		}
		
		//报价明细 价格设置
		List<GuestPriceJsonBean> guestPriceList = detailBean.getGuestPriceList();
		if(CollectionUtils.isEmpty(guestPriceList)){
			guestPriceList = new ArrayList<GuestPriceJsonBean>();
		}
		Set<Object> keySet = dataUtil.getShortNameAndGuestTypeMap().keySet();
		for(Object obj:keySet){
			//没有匹配的第N人住客类型UUID 跳过循环
			if(dataUtil.getShortNameAndGuestTypeMap().get(obj)==null ){
				continue;
			}
			String shortName = obj.toString();
			List<HotelGuestType> hgtypeList = dataUtil.getShortNameAndGuestTypeMap().get(obj);
			
			for(HotelGuestType hgtype:hgtypeList){
				GuestPriceJsonBean priceBean = new GuestPriceJsonBean();
				TravelerType traveler = dataUtil.getTravelerTypeMap().get(shortName);
				if(traveler==null)continue;//报价条件中的游客类型和价单中的游客类型不匹配时跳过
				priceBean.setTravelerType(traveler.getUuid());
				
				//住客类型的UUID和NAME存放，第几人的情况下这样处理
				priceBean.setGuestType(hgtype.getUuid());
				priceBean.setTravelerTypeText(hgtype.getName());
				
				priceBean.setInDate(detailBean.getInDate());
				priceBean.setHotelPlUuid(hotelPl.getUuid());
				priceBean.setIsThirdPerson(1);
				
				Map<String, Object> rmbMap = this.setThirdPersonPrice(roomQuery, islandwayUuid, hotelPl, priceBean, shortName,hgtype.getUuid(), dataUtil);
			
				if(MapUtils.isEmpty(rmbMap)){
					priceBean.setCurrencyId(dataUtil.getRmbCurrencyId().toString());
					priceBean.setCurrencyText("-");
					priceBean.setAmount(0d);
				}else{
					priceBean.setCurrencyId((String)rmbMap.get("currencyId"));
					priceBean.setCurrencyText((String)rmbMap.get("currencyText"));
					priceBean.setAmount((double)rmbMap.get("amount"));
				}
				
				
				guestPriceList.add(priceBean);
				
				//通过住客类型查找对应的价格对象
				if(priceListGroup4TravelerType!=null&&priceListGroup4TravelerType.containsKey(hgtype.getUuid())){
					List<GuestPriceJsonBean> groupList = priceListGroup4TravelerType.get(hgtype.getUuid());
					if(CollectionUtils.isNotEmpty(groupList)){
						GuestPriceJsonBean groupBean = groupList.get(0);
						//第N人的合计处理 -- 当有不为空的明细报价则会进行合计的统计，覆盖之前为空的币种信息。
						if(priceBean.getAmount()!=null && priceBean.getAmount()>0){
							groupBean.setCurrencyId(priceBean.getCurrencyId());
							groupBean.setCurrencyText(priceBean.getCurrencyText());
						}
						groupBean.setAmount(formatDouble(groupBean.getAmount()+priceBean.getAmount()));
						
					}else{
						GuestPriceJsonBean priceBean1 = new GuestPriceJsonBean();
						BeanUtil.copySimpleProperties(priceBean1, priceBean);
						allGuestPriceList.add(priceBean1);
					}
				}else{
					GuestPriceJsonBean priceBean1 = new GuestPriceJsonBean();
					BeanUtil.copySimpleProperties(priceBean1, priceBean);
					allGuestPriceList.add(priceBean1);
				}
			}
		}
		
		detailBean.setGuestPriceList(guestPriceList);
	}
	
	
	/**
	 * 第N人的报价计算 add by zhanghao
	 * 
	 * @param roomQuery 报价详情条件
	 * @param islandwayUuid 往返的交通方式
	 * @param hotelPl 价单信息
	 * @param priceBean 当前设置详细价格的对象
	 * @param shortName 游客类型简写
	 * @param dataUtil
	 */
	private Map<String, Object> setThirdPersonPrice(QuotedPriceRoomQuery roomQuery,String islandwayUuid,HotelPl hotelPl ,GuestPriceJsonBean priceBean,String shortName,String guestTypeUuid,AutoQuotedPriceDataUtil dataUtil){

		String currencyId=hotelPl.getCurrencyId().toString();//币种id
		String currencyText="";
		if(dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString()))!=null){
			currencyText=dataUtil.getCurrencyObject(Long.parseLong(hotelPl.getCurrencyId().toString())).getCurrencyMark();//币种输出符号
		}
		Double amount=0d;//价格

		Double islandwayPriceAmount = 0d;
		Double mealPriceAmount = 0d;
		Double roomPriceAmount = 0d;
		
		//是否进行整体报价标示，true时会进行报价
		boolean flag=false;
		
		/**交通费*/
		Map<String, Object> computeIslandwayPriceMap = computeIslandwayPrice(islandwayUuid, roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ISLAND,priceBean,dataUtil);
		flag=(boolean)computeIslandwayPriceMap.get("flag");
		if(flag){
			islandwayPriceAmount=(double)computeIslandwayPriceMap.get("amount");
		}
		
		//判断是否继续进行下面报价，如果可以则初始化 flag标示为false
		if(!flag){
			return null;
		}else{
			flag=false;
		}
		
		/**计算升餐费*/
		Map<String, Object> computeMealPriceMap = computeMealPrice(roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_MEAL,priceBean,dataUtil);
		flag=(boolean)computeMealPriceMap.get("flag");
		if(flag){
			mealPriceAmount=(double)computeMealPriceMap.get("amount");
		}
		
		//判断是否继续进行下面报价，如果可以则初始化 flag标示为false
		if(!flag){
			return null;
		}else{
			flag=false;
		}
		
		
		/**计算房费*/
		Map<String, Object> computeRoomPriceMap = computeRoomPrice(roomQuery, hotelPl, shortName, HotelPlTaxException.EXCEPTION_TYPE_ROOM,guestTypeUuid,priceBean,dataUtil);
		
		flag=(boolean)computeRoomPriceMap.get("flag");
		if(flag){
			roomPriceAmount=(double)computeRoomPriceMap.get("amount");
		}
		
		if(flag){
			//价格
			amount=islandwayPriceAmount+mealPriceAmount+roomPriceAmount;
			
			
			//转换成人民币--
			Map<String, Object> rmbMap = tranfer2RMB4exchangeRate(amount, currencyId, currencyText,dataUtil);
			return rmbMap;
			
		}
		return null;
		
	}
	

	/**
	 * 计算房费 add by zhanghao
	 * @param roomQuery 报价明细条件
	 * @param hotelPl 价单信息
	 * @param travelerTypeShortNam 游客类型简写 游客类型简写 A成人 B婴儿 C儿童 O老人 S特殊人群
	 * @param exceptionType 税金 例外类型(1、房型；2、餐型；3、交通)
	 * @param perNum 人员数量
	 * @param type 住客类型 的 类型（0、1 共或者增）
	 * @return
	 */
	private Map<String,Object> computeRoomPrice(QuotedPriceRoomQuery roomQuery,HotelPl hotelPl,String travelerTypeShortName,Integer exceptionType,String guestTypeUuid,GuestPriceJsonBean priceBean,AutoQuotedPriceDataUtil dataUtil){
		return computeRoomPrice(roomQuery, hotelPl, travelerTypeShortName, exceptionType, guestTypeUuid, priceBean, dataUtil, 1);
	}
		
	private Map<String,Object> computeRoomPrice(QuotedPriceRoomQuery roomQuery,HotelPl hotelPl,String travelerTypeShortName,Integer exceptionType,String guestTypeUuid,GuestPriceJsonBean priceBean,AutoQuotedPriceDataUtil dataUtil,Integer bedTaxNum){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("flag", false);
		map.put("amount", 0d);
		
		//转换 住客类型 失败 则直接返回，不进行报价
		if(StringUtils.isBlank(guestTypeUuid)){
			return map;
		}
		
		/**房费*/
		
		HotelPlPriceQuery hotelPlPriceQuery = new HotelPlPriceQuery();
		hotelPlPriceQuery.setStartDate(roomQuery.getInDate());
		hotelPlPriceQuery.setHotelPlUuid(hotelPl.getUuid());
		
		//设置 住客类型 的UUID
		hotelPlPriceQuery.setHotelGuestTypeUuid(guestTypeUuid);
		
		hotelPlPriceQuery.setHotelRoomUuid(roomQuery.getHotelRoomUuid());
		hotelPlPriceQuery.setHotelMealUuids(roomQuery.getHotelMealUuid());
		hotelPlPriceQuery.setPriceType(HotelPlPrice.PRICE_TYPE_PEER);//房费 默认按 同行价报价
		
		List<HotelPlPrice> priceList = hotelPlPriceService.getHotelPlPriceQuery4AutoQuotedPrice(hotelPlPriceQuery);
		//有且仅有1条升餐费用记录时 才会进行升餐报价
		if(CollectionUtils.isNotEmpty(priceList)&&priceList.size()>=1){
			HotelPlPrice roomPrice = priceList.get(0);
			if(roomPrice.getAmount()!=null){
				HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery=new HotelPlTaxExceptionQuery();
				hotelPlTaxExceptionQuery.setStartDate(roomQuery.getInDate());
				hotelPlTaxExceptionQuery.setHotelPlUuid(hotelPl.getUuid());
				hotelPlTaxExceptionQuery.setTravelType(dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getUuid());
				hotelPlTaxExceptionQuery.setExceptionType(exceptionType);
				
				if(roomPrice.getAmount()==null){
					map.put("flag", false);
					map.put("amount", 0d);
					dataUtil.putMessage(DateUtils.date2String(roomQuery.getInDate())+"缺失房费！");
				}else{
					//税金处理--默认按同币种处理，多币种问题V1版本不考虑
					double roomPriceAmount = this.priceTaxex(roomPrice.getAmount(), hotelPlTaxExceptionQuery,bedTaxNum,dataUtil) ;
					
					//房费和含税的房费进行保存--用于后面的优惠计算
					if(priceBean!=null){
						priceBean.setRoomAmount(roomPrice.getAmount());
						priceBean.setRoomAmountTotal(roomPriceAmount);
					}
					
					
					map.put("flag", true);
					map.put("amount", roomPriceAmount);
				}
				
				
			}else{
				map.put("flag", false);
				map.put("amount", 0d);
			}
			
		}
		return map;
	}
	
	/**
	 * 计算交通费用 add by zhanghao
	 * @param islandwayUuid 交通方式
	 * @param roomQuery 报价明细条件
	 * @param hotelPl 价单信息
	 * @param travelerTypeShortNam 游客类型简写 游客类型简写 A成人 B婴儿 C儿童 O老人 S特殊人群
	 * @param exceptionType 税金 例外类型(1、房型；2、餐型；3、交通)
	 * @return
	 */
	private Map<String,Object> computeIslandwayPrice(String islandwayUuids,QuotedPriceRoomQuery roomQuery,HotelPl hotelPl,String travelerTypeShortName,Integer exceptionType,GuestPriceJsonBean priceBean,AutoQuotedPriceDataUtil dataUtil){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("flag", false);
		map.put("amount", 0d);
		
		double totalPrice = 0;
		
		//行程的起止时间，即存在交通费用
		if(StringUtils.isNotBlank(islandwayUuids)){
			
			for(String islandwayUuid:islandwayUuids.split(";")){
				/**交通费*/
				//查询出当前第三人的交通费用 根据上岛方式、日期、游客类型ID（成人）、价单UUID
				List<HotelPlIslandway> islandwayList = hotelPlIslandwayService.getIslandWay4AutoQuotedPrice(islandwayUuid, roomQuery.getInDate(), dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getUuid(), hotelPl.getUuid());
				//有且仅有1条交通费记录时 才会进行交通费报价
				if(CollectionUtils.isNotEmpty(islandwayList)&&islandwayList.size()>0){
					HotelPlIslandway f = islandwayList.get(0);
					
					HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery=new HotelPlTaxExceptionQuery();
					hotelPlTaxExceptionQuery.setStartDate(roomQuery.getInDate());
					hotelPlTaxExceptionQuery.setHotelPlUuid(hotelPl.getUuid());
					hotelPlTaxExceptionQuery.setTravelType(dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getUuid());
					hotelPlTaxExceptionQuery.setExceptionType(exceptionType);
					
					if(f.getAmount()==null){
						map.put("flag", false);
						if(dataUtil.getMessage().indexOf(DateUtils.date2String(roomQuery.getInDate())+dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getName()+"缺失交通费！")==-1){
							dataUtil.putMessage(DateUtils.date2String(roomQuery.getInDate())+dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getName()+"缺失交通费！");
						}
						break;
					}else{
						//税金处理--默认按同币种处理，多币种问题V1版本不考虑
						double islandwayPriceAmount =this.priceTaxex(f.getAmount(), hotelPlTaxExceptionQuery,dataUtil) ;
						
						//交通费和含税的交通费进行保存--用于后面的优惠计算
						if(priceBean!=null){
							if(priceBean.getIslandwayAmount()!=null && priceBean.getIslandwayAmountTotal()!=null){
								priceBean.setIslandwayAmount(priceBean.getIslandwayAmount()+f.getAmount());
								priceBean.setIslandwayAmountTotal(priceBean.getIslandwayAmountTotal()+islandwayPriceAmount);
							}else{
								priceBean.setIslandwayAmount(f.getAmount());
								priceBean.setIslandwayAmountTotal(islandwayPriceAmount);
							}
						}
						totalPrice+=islandwayPriceAmount;
						
						//有交通费的报价结果，允许继续报价
						map.put("flag", true);
						map.put("amount", totalPrice);
					}
				}else{
					map.put("flag", false);
					
					if(dataUtil.getMessage().indexOf(DateUtils.date2String(roomQuery.getInDate())+dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getName()+"缺失交通费！")==-1){
						dataUtil.putMessage(DateUtils.date2String(roomQuery.getInDate())+dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getName()+"缺失交通费！");
					}
				}
			}
			
			
		}else{
			map.put("flag", true);
		}
		
		return map;
	}
	
	/**
	 * 计算升餐费用 add by zhanghao
	 * 如果有强制餐费时则不计算升餐费
	 * @param query 报价条件
	 * @param roomQuery 报价明细条件
	 * @param hotelPl 价单信息
	 * @param travelerTypeShortNam 游客类型简写 游客类型简写 A成人 B婴儿 C儿童 O老人 S特殊人群
	 * @param exceptionType 税金 例外类型(1、房型；2、餐型；3、交通)
	 * @return
	 */
	private Map<String,Object> computeMealPrice(QuotedPriceRoomQuery roomQuery,HotelPl hotelPl,String travelerTypeShortName,Integer exceptionType,GuestPriceJsonBean priceBean,AutoQuotedPriceDataUtil dataUtil){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("flag", false);
		map.put("amount", 0d);
		
		
		
		HotelPlHolidayMealQuery hotelPlHolidayMealQuery = new HotelPlHolidayMealQuery();
		hotelPlHolidayMealQuery.setStartDate(roomQuery.getInDate());
		hotelPlHolidayMealQuery.setHotelPlUuid(hotelPl.getUuid());
		hotelPlHolidayMealQuery.setTravelerTypeUuid(dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getUuid());
		List<HotelPlHolidayMeal> holidayMealList = hotelPlHolidayMealService.getHotelPlHolidayMeal4AutoQuotedPrice(hotelPlHolidayMealQuery);
		
		if(CollectionUtils.isNotEmpty(holidayMealList)){
			HotelPlHolidayMeal meal = holidayMealList.get(0);	
			
			HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery=new HotelPlTaxExceptionQuery();
			hotelPlTaxExceptionQuery.setStartDate(roomQuery.getInDate());
			hotelPlTaxExceptionQuery.setHotelPlUuid(hotelPl.getUuid());
			hotelPlTaxExceptionQuery.setTravelType(dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getUuid());
			hotelPlTaxExceptionQuery.setExceptionType(exceptionType);
			
			if(meal.getAmount()==null){
				map.put("flag", false);
				dataUtil.putMessage(DateUtils.date2String(roomQuery.getInDate())+"游客类型“"+dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getName()+"”缺失强制餐费！");
			}else{
				//税金处理--默认按同币种处理，多币种问题V1版本不考虑
				double mealPriceAmount = this.priceTaxex(meal.getAmount(), hotelPlTaxExceptionQuery,dataUtil) ;
				
				//餐费和含税的餐费进行保存--用于后面的优惠计算
				if(priceBean!=null){
					priceBean.setMealAmount(meal.getAmount());
					priceBean.setMealAmountTotal(mealPriceAmount);
				}
				
				
				map.put("flag", true);
				map.put("amount", mealPriceAmount );
			}
			
		}else{
			//如果没有选择升餐则不会进行 升餐的费用计算
			if(StringUtils.isNotBlank(roomQuery.getHotelMealRiseUuid())){
				HotelPlMealriseQuery hotelPlMealriseQuery = new HotelPlMealriseQuery ();
				hotelPlMealriseQuery.setStartDate(roomQuery.getInDate());
				hotelPlMealriseQuery.setHotelPlUuid(hotelPl.getUuid());
				hotelPlMealriseQuery.setTravelerTypeUuid(dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getUuid());
				hotelPlMealriseQuery.setHotelMealUuid(roomQuery.getHotelMealUuid());
				hotelPlMealriseQuery.setHotelMealRiseUuid(roomQuery.getHotelMealRiseUuid());
				List<HotelPlMealrise> mealList = hotelPlMealriseService.getHotelPlMealrise4AutoQuotedPrice(hotelPlMealriseQuery);
				
				
				//有且仅有1条升餐费用记录时 才会进行升餐报价
				if(CollectionUtils.isNotEmpty(mealList)&&mealList.size()>0){
					HotelPlMealrise meal = mealList.get(0);	
					
					HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery=new HotelPlTaxExceptionQuery();
					hotelPlTaxExceptionQuery.setStartDate(roomQuery.getInDate());
					hotelPlTaxExceptionQuery.setHotelPlUuid(hotelPl.getUuid());
					hotelPlTaxExceptionQuery.setTravelType(dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getUuid());
					hotelPlTaxExceptionQuery.setExceptionType(exceptionType);
					
					if(meal.getAmount()==null){
						map.put("flag", false);
						dataUtil.putMessage(DateUtils.date2String(roomQuery.getInDate())+"缺失升餐费！");
					}else{
						//税金处理--默认按同币种处理，多币种问题V1版本不考虑
						double mealPriceAmount = this.priceTaxex(meal.getAmount(), hotelPlTaxExceptionQuery,dataUtil) ;
						
						//餐费和含税的餐费进行保存--用于后面的优惠计算
						if(priceBean!=null){
							priceBean.setMealAmount(meal.getAmount());
							priceBean.setMealAmountTotal(mealPriceAmount);
						}
						
						
						map.put("flag", true);
						map.put("amount", mealPriceAmount );
					}
				}
			}else{
				map.put("flag", true);
			}
			
		}
		
		
		return map;
	}
	/**
	 * 根据汇率转换成人民币 add by zhanghao
	 * 默认是 美元转换成 人民币
	 * @param amount
	 * @return
	 */
	private Map<String,Object> tranfer2RMB4exchangeRate(double amount,String currencyId,String currencyText,AutoQuotedPriceDataUtil dataUtil){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			amount = CurrencyUtils.currencyConverter(currencyId, amount+"");
		} catch (Exception e) {
			String message = "当前价单维护的币种不存在，请重新维护当前价单的币种！";
			if(dataUtil.getMessage().indexOf(message)==-1){
				dataUtil.putMessage(message);
			}
			throw e;
		}
		map.put("amount", amount);
//		BigDecimal   result   =   new   BigDecimal(amount); 
//		map.put("amount", result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//		map.put("currencyId", currencyId);
//		map.put("currencyText", currencyText);
		map.put("currencyId", dataUtil.getRmbCurrencyId().toString());
		map.put("currencyText", "￥");
		return map;
	}
	/**
	 * 根据币种ID和金额转成RMB;
	 * @param amount
	 * @param currencyId
	 * @return
	 */
	private double tranfer2RMB4exchangeRate(double amount,String currencyId,AutoQuotedPriceDataUtil dataUtil){
		double result = 0d;
		try {
			result = CurrencyUtils.currencyConverter(currencyId, amount+"");
		} catch (Exception e) {
			String message = "当前价单维护的币种不存在，请重新维护当前价单的币种！";
			if(dataUtil.getMessage().indexOf(message)==-1){
				dataUtil.putMessage(message);
			}
			throw e;
		}
		return result;
	}
	/**
	 * 根据游客类型简写、当前游客类型的人数 和 住客类型的类型（共或增） 得到 住客类型的UUID
	 * add by zhanghao
	 * @param travelerTypeShortName 游客类型简写
	 * @param perNum 当前游客类型的人数
	 * @param type 类型（0：共；1：增。即0共几人，1第几人）
	 * @param dataUtil
	 * @param hotelRoomUuid 当前房型UUID
	 * @param hotelRoomName	当前房型名称
	 * @return
	 */
	private String getGuestTypeUuid(String travelerTypeShortName,Integer perNum,Integer type,AutoQuotedPriceDataUtil dataUtil,String hotelRoomUuid,String hotelRoomName){
		//根据游客类型的简写 转换成 住客类型的人员类型 
		TravelerType travelerObj = dataUtil.getTravelerTypeMap().get(travelerTypeShortName);
		//当前游客类型 所 绑定的 系统游客类型 对应的所有绑定的系统住客类型UUID集合
		List<String> guestTypeMap4TravelerType = dataUtil.getGuestTypeMap4TravelerType().get(travelerObj.getSysTravelerType());
		
		//或得该房型下的 住客类型集合
		List<SysGuestType> guestTypeList = dataUtil.getGuestTypeMap4HotelRoom().get(hotelRoomUuid);
		
		if(CollectionUtils.isNotEmpty(guestTypeList)){
			for(SysGuestType sysGuestType:guestTypeList){
				//如果系统游客类型绑定的住客类型集合中存在 当前的 住客类型则进行下一步的判断
				if(CollectionUtils.isNotEmpty(guestTypeMap4TravelerType)&&guestTypeMap4TravelerType.contains(sysGuestType.getUuid())){
					//住客类型的类型、住客类型的value值，匹配出唯一的住客类型，返回批发商住客类型UUID（hotel_guest_type）
					if(sysGuestType.getType().intValue()==type&&sysGuestType.getValue().intValue()==perNum){
						return sysGuestType.getHotelGuestTypeUuid();
					}
				}
			}
		}
		//dataUtil.putMessage("输入的游客类型“"+dataUtil.getTravelerTypeMap().get(travelerTypeShortName).getName()+"”和房型"+hotelRoomName+"绑定的系统住客类型不匹配");
		return null;
	}
	
	
	
	
	/**
	 * 设置可以使用的优惠信息 add by zhanghao
	 * @param bean
	 */
	private void setPreferentialList(QuotedPriceJsonBean bean,HotelPl hotelPl,AutoQuotedPriceDataUtil dataUtil){
		//查询入住日期 、 预订日期 内的 所有优惠信息
		List<HotelPlPreferential> preferentialList =this.getPreferentialList(bean,hotelPl);
		//过滤后的优惠信息
		List<HotelPlPreferential> filterList = this.filterPreferentialList(preferentialList, bean,dataUtil);
		
		//增加优惠集合的非空校验
		if(CollectionUtils.isNotEmpty(filterList)){
			//设置所有的可用优惠信息，包括关联的优惠信息和普通的优惠信息
			//A分别和B,C关联，则返回的信息集合包括 AB,AC,A,B,C
			//此处不做多级的关联，只做一次关联
			List<PreferentialJsonBean> preferentialTotal = this.groupPreferentialList(filterList);
			
			//分组排序报价明细结果价格
			dataUtil.setGroupDetailPriceList4TravelTypeMap(sortDetailGuestList(bean));
			//计算优惠后的价格并数据封装
			this.computePrice4preferential(preferentialTotal, bean,dataUtil);
			
			//优惠后的价格排序
			sortPrefer4TotalPrice(preferentialTotal, dataUtil);
			
			
			bean.setPreferentialTotal(preferentialTotal);
			
		}
		//查询相关优惠信息
		bean.setPreferentialList4hotelPl(getRelatedPreferentialList4hotelPl(hotelPl,bean.getPreferentialTotal()));
		
	}
	
	/**
	 * 查询相关优惠信息 add by zhanghao
	 * @param hotelPl
	 * @return
	 */
	private List<HotelPlPreferential> getRelatedPreferentialList4hotelPl(HotelPl hotelPl,List<PreferentialJsonBean> preferentialTotal){
		HotelPlPreferentialQuery hotelPlPreferentialQuery=new HotelPlPreferentialQuery();
		hotelPlPreferentialQuery.setDelFlag("0");
		hotelPlPreferentialQuery.setHotelPlUuid(hotelPl.getUuid());
		List<HotelPlPreferential> list = hotelPlPreferentialService.find(hotelPlPreferentialQuery);
		//子表信息的设置，目前前端优惠信息不需要设置详细信息
		//hotelPlPreferentialService.setPlPreferentialsParam(list);
		
		
		//过滤可以使用的优惠信息 
		Map<Object, HotelPlPreferential> map = BeanUtil.groupByKeyString2Obj("uuid", list);
		List<HotelPlPreferential> result = new ArrayList<HotelPlPreferential>();
		if(MapUtils.isNotEmpty(map)&&CollectionUtils.isNotEmpty(preferentialTotal)){
			for(PreferentialJsonBean pjb:preferentialTotal){
				List<HotelPlPreferential> preferList = pjb.getPreferentialList();
				if(CollectionUtils.isNotEmpty(preferList)){
					for(HotelPlPreferential pre:preferList){
						if(map.containsKey(pre.getUuid())){//如果不在可使用集合中，可以返回
							map.remove(pre.getUuid());
						}
					}
				}
			}
		}
		if(MapUtils.isNotEmpty(map)){
			Set<Entry<Object, HotelPlPreferential>> entrySet = map.entrySet();
			if(CollectionUtils.isNotEmpty(entrySet)){
				for(Entry<Object, HotelPlPreferential> entry:entrySet){
					result.add(entry.getValue());
				}
			}
		}
		
		return result;
	}
	
	
	/**
	 * 根据成人价格进行排序 add by zhanghao
	 * 默认按第一个成人的优惠后价格排序（第一个成人：即填写人数的第一个成人）
	 * @param preferentialTotal
	 * @param dataUtil
	 */
	private void sortPrefer4TotalPrice(List<PreferentialJsonBean> preferentialTotal,final AutoQuotedPriceDataUtil dataUtil){
		
		if(CollectionUtils.isNotEmpty(preferentialTotal)&&dataUtil.getSortPreferObj()!=null){
			Collections.sort(preferentialTotal, new Comparator<PreferentialJsonBean>() {  
	            @Override  
	            public int compare(PreferentialJsonBean arg0, PreferentialJsonBean arg1) {  
	            	if(CollectionUtils.isNotEmpty(arg0.getGuestPriceList())&&CollectionUtils.isNotEmpty(arg1.getGuestPriceList())){
	            		double arg0Amount = 0d;
	            		for(GuestPriceJsonBean g1:arg0.getGuestPriceList()){
	            			if(g1.getTravelerType().equals(dataUtil.getSortPreferObj().getUuid())){
	            				arg0Amount=g1.getAmount();
	            			}
	            		}
	            		double arg1Amount = 0d;
	            		for(GuestPriceJsonBean g1:arg1.getGuestPriceList()){
	            			if(g1.getTravelerType().equals(dataUtil.getSortPreferObj().getUuid())){
	            				arg1Amount=g1.getAmount();
	            			}
	            		}
	            		if((arg0.getTotalPrice()!=null && arg0.getTotalPrice()>0)||(arg1.getTotalPrice()!=null && arg1.getTotalPrice()>0)){
	            			return -1 ;
	            		}
	            		if (arg0Amount - arg1Amount>0) {  
	                        return 1;  
	                    } else {  
	                        return -1;  
	                    }
	            		
	            	}else{
	            		return -1;
	            	}
	            }  
	        });  
		}
	}
	
	/**
	 * 根据报价结果 查询 符合日期条件的 优惠信息集合
	 * 包括优惠信息的详细信息，即子表数据
	 * @param bean
	 * @param hotelPl
	 * @return
	 */
	private List<HotelPlPreferential> getPreferentialList(QuotedPriceJsonBean bean,HotelPl hotelPl){
		HotelPlPreferentialQuery hotelPlPreferentialQuery =  new HotelPlPreferentialQuery();
		hotelPlPreferentialQuery.setHotelPlUuid(hotelPl.getUuid());
		QuotedPriceQuery query = bean.getQuotedPriceQuery();
		if(CollectionUtils.isNotEmpty(query.getQuotedPriceRoomList())){
			hotelPlPreferentialQuery.setInDate(query.getQuotedPriceRoomList().get(0).getInDate());
			hotelPlPreferentialQuery.setOutDate(query.getQuotedPriceRoomList().get(query.getQuotedPriceRoomList().size()-1).getInDate());
			hotelPlPreferentialQuery.setBookingStartDate(new Date());
			hotelPlPreferentialQuery.setBookingEndDate(new Date());
			List<HotelPlPreferential> list = hotelPlPreferentialService.getHotelPlPreferentials4AutoQuotedPrice(hotelPlPreferentialQuery);
			return list;
		}
		return null;
	}
	
	/**
	 * 优惠信息的过滤函数，过滤掉不符合要求的优惠信息，返回可以适用的优惠信息 add by zhanghao
	 * @param preferentialList
	 * @param bean
	 * @param dataUtil
	 * @return
	 */
	private List<HotelPlPreferential> filterPreferentialList(List<HotelPlPreferential> preferentialList,QuotedPriceJsonBean bean,AutoQuotedPriceDataUtil dataUtil){
		List<HotelPlPreferential> result = null;
		
		if(CollectionUtils.isNotEmpty(preferentialList)&&bean!=null){
			QuotedPriceQuery query = bean.getQuotedPriceQuery();
			if(query!=null){
				List<QuotedPriceRoomQuery> roomQueryList = query.getQuotedPriceRoomList();
				//自动报价条件异常
				if(CollectionUtils.isEmpty(roomQueryList)){
					return result;
				}
				
				
				//按房型分组后的 前端参数的map 房型UUID和晚数的映射
				Map<String, Integer> roomQueryMap = getRoomAndNightsMap4Query(roomQueryList);
				Set<String> keySet = roomQueryMap.keySet();
				
				//按hotelRoomUuid做分组
				Map<Object, List<QuotedPriceRoomQuery>> hotelMealMap = BeanUtil.groupByKeyString("hotelRoomUuid", roomQueryList);
				
				for(HotelPlPreferential preferential:preferentialList){
					
					//优惠要求中的过滤
					HotelPlPreferentialRequire require = preferential.getRequire();
					if(require!=null){
						
						//起订晚数 条件筛选
						if(require.getBookingNights()!=null&&require.getBookingNights()>roomQueryList.size()){
							continue;
						}
						//起订间数 条件筛选
						if(require.getBookingNumbers()!=null&&require.getBookingNumbers()>query.getRoomNum()){
							continue;
						}
						//不适用日期
						if(StringUtils.isNotBlank(require.getNotApplicableDate())){
							boolean b = false;
							for(QuotedPriceRoomQuery roomQuery:roomQueryList){
								//不使用日期的过滤，20151024 E243需求修改--不适用日期修改成日期段，当前存储方式是 2015-10-01~2015-10-10;2015-11-01~2015-11-10
								if(StringUtils.isNotBlank(require.getNotApplicableDate())){
									boolean bool = false;
									String[] dateArray = require.getNotApplicableDate().split(";");//截取单独的日期段
									if(ArrayUtils.isNotEmpty(dateArray)){
										for(String date:dateArray){
											if(StringUtils.isNotBlank(date)){
												String[] beginAndEndArray = date.split("~");
												Date beginDate = DateUtils.string2Date(beginAndEndArray[0]);
												Date endDate = beginDate;
												if(beginAndEndArray.length>1){
													endDate=DateUtils.string2Date(beginAndEndArray[1]);
												}
												if((beginDate.before(roomQuery.getInDate())||beginDate.equals(roomQuery.getInDate()))&&
														(endDate.after(roomQuery.getInDate())||endDate.equals(roomQuery.getInDate()))){
													bool=true;
													break;
												}
											}
										}
									}
									if(bool){
										b=true;
										break;
									}
								}
								
								//注释于20151024 update by zhanghao - E243需求修改日期段存储
								/*if(require.getNotApplicableDate().indexOf(DateUtils.date2String(roomQuery.getInDate()))>-1){
									b=true;
									break;
								}*/
							}
							if(b)continue;
						}
						
					}
					
					List<HotelPlPreferentialRoom> roomListAll = preferential.getPreferentialRoomList();
					List<HotelPlPreferentialRoom> roomList = new ArrayList<HotelPlPreferentialRoom>();
					//过滤 关联酒店的房型 update by zhanghao 20151118 修复关联酒店后不能筛选出优惠的bug
					if(CollectionUtils.isNotEmpty(roomListAll)){
						for(HotelPlPreferentialRoom hr:roomListAll){
							if(hr!=null){
								HotelPl pl = dataUtil.getHotelPlMap().get(bean.getHotelPlUuid());
								//关联酒店的房型过滤，只返回适用房型的集合
								if(pl!=null&&hr.getHotelUuid().equals(pl.getHotelUuid())){
									roomList.add(hr);
								}
							}
						}
					}
					
					Map<String, Integer> roomMap = getRoomAndNightsMap4Room(roomList);
					Map<Object, List<HotelPlPreferentialRoom>> roomMealMap = BeanUtil.groupByKeyString("hotelRoomUuid", roomList);
					if(MapUtils.isEmpty(roomMealMap)){
						continue;
					}
					boolean rbo = false;
					
					//优惠信息中的房型和间数映射
					//判断报价条件中的房型和晚数是否完全匹配价单中优惠信息中的房型和大于房型的晚数
					Set<Object> keys = roomMealMap.keySet();
					
					for(Object key:keys){
						if(roomQueryMap.containsKey(key.toString())&& roomMap.get(key.toString())<=roomQueryMap.get(key.toString())){
							
						}else{//报价条件中不存在该房型 或者 晚数小于优惠中该房型的适用晚数 不能使用该优惠
							rbo= true;
							break;
						}
					}
					if(rbo) 
						continue;
					
					
					//判断报价条件中的基础餐型是否匹配
					boolean mealbool = false;
					for(Object key:keys){
						List<QuotedPriceRoomQuery> queryMealList = hotelMealMap.get(key);//报价条件
						List<HotelPlPreferentialRoom> roomMealList = roomMealMap.get(key);//优惠信息
						
						boolean bo=false;
						
						for(HotelPlPreferentialRoom plroom:roomMealList){//循环优惠中维护的房型基础餐型信息
							if(StringUtils.isBlank(plroom.getHotelMealUuids())){
								break;
							}
							for(QuotedPriceRoomQuery pquery:queryMealList){//报价条件中的房型基础餐型信息
								
								if(StringUtils.isNotBlank(pquery.getHotelMealRiseUuid())){//如果存在升餐信息，则匹配升餐信息
									if(plroom.getHotelMealUuids().indexOf(pquery.getHotelMealRiseUuid())>-1){
										bo=true;
										break;
									}
								}else{
									if(plroom.getHotelMealUuids().indexOf(pquery.getHotelMealUuid())>-1){
										bo=true;
										break;
									}
									
								}
							}
							if(bo){//如果报价条件中一个房型的餐型匹配上优惠信息中的餐型即可通过
								mealbool = true;
								break;
							}
						}
						if(bo){//如果报价条件中一个房型的餐型匹配上优惠信息中的餐型即可通过
							break;
						}
					}
					if(!mealbool){//没有匹配的餐型则跳过
						continue;
					}
					
					
					if(!checkPlPreferential(bean, preferential)){;
						continue;
					}
					
					//符合匹配要求的优惠信息存储在result中并返回
					if(result==null){
						result = new ArrayList<HotelPlPreferential>();
					}
					result.add(preferential);
				}
			}
		}
		return result;
	}
	
	/**
	 * 优惠信息根据选择的优惠类型 进行判断，符合优惠信息中优惠类型设置的属性值的优惠返回true，否则false
	 * add by zhanghao
	 * @param bean
	 * @param pre
	 * @return
	 */
	private boolean checkPlPreferential(QuotedPriceJsonBean bean,HotelPlPreferential pre){
		boolean result = false;
		
		try {
			QuotedPriceQuery query = bean.getQuotedPriceQuery();
			List<QuotedPriceRoomQuery> roomQuery = query.getQuotedPriceRoomList();
			
			HotelPlPreferentialMatter matter = pre.getMatter();
			
			//根据模板生成的输入框中的输入值集合
			List<HotelPlPreferentialMatterValue> valueList = pre.getValueList();
			
			
			//按不同的KEY值分组
			Map<Object, HotelPlPreferentialMatterValue> myMap = BeanUtil.groupByKeyString2Obj("myKey", valueList);
			
			if(PreferentialTemplates.type2Uuid.get(PreferentialTemplates.PREFERENTIAL_TEMPLATE_TYPE_0).equals(matter.getPreferentialTemplatesUuid())){//住付优惠
				if(CollectionUtils.isEmpty(valueList)){
					return false;
				}
				HotelPlPreferentialMatterValue value = myMap.get(HotelPlPreferentialMatterValue.MYKEY_LIVE);
				if(roomQuery.size()>=Integer.parseInt(value.getMyValue())){//入住的时间大于 住付优惠的 条件时
					result = true;
				}
				StringBuffer preferentialTemplatesDetailText  = new StringBuffer();
				preferentialTemplatesDetailText.append("住");
				preferentialTemplatesDetailText.append(value.getMyValue());
				preferentialTemplatesDetailText.append("免");
				preferentialTemplatesDetailText.append(myMap.get(HotelPlPreferentialMatterValue.MYKEY_EXEMPT).getMyValue());
				matter.setPreferentialTemplatesDetailText(preferentialTemplatesDetailText.toString());
			}else if(PreferentialTemplates.type2Uuid.get(PreferentialTemplates.PREFERENTIAL_TEMPLATE_TYPE_1).equals(matter.getPreferentialTemplatesUuid())){//提前预订优惠
				if(CollectionUtils.isEmpty(valueList)){
					return false;
				}
				HotelPlPreferentialMatterValue value = myMap.get(HotelPlPreferentialMatterValue.MYKEY_BEFORE);
				Integer dayNum = Integer.parseInt(value.getMyValue());
				
				QuotedPriceRoomQuery roomDetail = roomQuery.get(0);
				Calendar ca1=Calendar.getInstance();
				ca1.setTime(roomDetail.getInDate());
				ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH)-dayNum);
				if(ca1.getTime().after(DateUtils.string2Date(DateUtils.date2String(new Date())))){//当前日期小于入住日期减去提前预订天数 
					result=true;
				}
				StringBuffer preferentialTemplatesDetailText  = new StringBuffer();
				preferentialTemplatesDetailText.append("提前");
				preferentialTemplatesDetailText.append(value.getMyValue());
				preferentialTemplatesDetailText.append("天预订");
				matter.setPreferentialTemplatesDetailText(preferentialTemplatesDetailText.toString());
			}else if(PreferentialTemplates.type2Uuid.get(PreferentialTemplates.PREFERENTIAL_TEMPLATE_TYPE_2).equals(matter.getPreferentialTemplatesUuid())){//打包优惠
				if(CollectionUtils.isEmpty(valueList)){
					return false;
				}
				result = true;
				StringBuffer preferentialTemplatesDetailText  = new StringBuffer();
				preferentialTemplatesDetailText.append("");
				matter.setPreferentialTemplatesDetailText(preferentialTemplatesDetailText.toString());
			}else if(PreferentialTemplates.type2Uuid.get(PreferentialTemplates.PREFERENTIAL_TEMPLATE_TYPE_3).equals(matter.getPreferentialTemplatesUuid())){//房餐交通优惠
				result = true;
				StringBuffer preferentialTemplatesDetailText  = new StringBuffer();
				preferentialTemplatesDetailText.append("");
				matter.setPreferentialTemplatesDetailText(preferentialTemplatesDetailText.toString());
			}else{
				//V1只有4种优惠，多余的不做处理
				if(CollectionUtils.isEmpty(valueList)){
					return false;
				}
			}
		} catch (NumberFormatException e) {
			logger.error("优惠信息中的模板输入信息格式不对！",e);
			return false;
		}
		return result;
	}
	
	/**
	 * 分组可用的优惠组合，包括关联后的集合 add by zhanghao
	 * A分别和B,C关联，则返回的信息集合包括 AB,AC,A,B,C
	 * 关联的优惠需要根据 优惠模板定义的常量排序，方便后面做关联优惠计算
	 * @param filterList
	 * @return
	 */
	private List<PreferentialJsonBean> groupPreferentialList(List<HotelPlPreferential> filterList){
		
		List<PreferentialJsonBean> preBeanList = null;
		if(CollectionUtils.isNotEmpty(filterList)){
			preBeanList = new ArrayList<PreferentialJsonBean>();
			Map<Object, HotelPlPreferential> plpreMap = BeanUtil.groupByKeyString2Obj("uuid", filterList);
			
			for(HotelPlPreferential pre:filterList){
				
				//单个的已匹配的优惠信息 如：A、B、C
				PreferentialJsonBean preBeanOne = new PreferentialJsonBean();
				List<HotelPlPreferential> preferentialListOne = new ArrayList<HotelPlPreferential>();
				preferentialListOne.add(pre);
				preBeanOne.setPreferentialList(preferentialListOne);
				preBeanList.add(preBeanOne);
				
				
				//关联的优惠信息 如 AB AC
				List<HotelPlPreferentialRel> relPreList = pre.getHotelPlPreferentialRels();
				for(HotelPlPreferentialRel rel:relPreList){
					if(plpreMap.containsKey(rel.getRelHotelPlPreferentialUuid())){
						
						PreferentialJsonBean preBeanMore = new PreferentialJsonBean();
						List<HotelPlPreferential> preferentialListMore = new ArrayList<HotelPlPreferential>();
						
						
						//关联的优惠 的 优惠类型 根据定义的常量 排序，后面计算关联的优惠时可以直接按此处排序后的顺序作关联计算
						if(PreferentialTemplates.uuid2Type.get(pre.getMatter().getPreferentialTemplatesUuid())
								<PreferentialTemplates.uuid2Type.get(plpreMap.get(rel.getRelHotelPlPreferentialUuid()).getMatter().getPreferentialTemplatesUuid())){
							HotelPlPreferential hpf1 = new HotelPlPreferential(pre);
							HotelPlPreferential hpf2 = new HotelPlPreferential(plpreMap.get(rel.getRelHotelPlPreferentialUuid()));
							preferentialListMore.add(hpf1);//当前的优惠
							preferentialListMore.add(hpf2);//关联的优惠
						}else{
							HotelPlPreferential hpf1 = new HotelPlPreferential(pre);
							HotelPlPreferential hpf2 = new HotelPlPreferential(plpreMap.get(rel.getRelHotelPlPreferentialUuid()));
							preferentialListMore.add(hpf2);//关联的优惠
							preferentialListMore.add(hpf1);//当前的优惠
						}

						preBeanMore.setPreferentialList(preferentialListMore);
						preBeanList.add(preBeanMore);
						
					}
				}
			}
		}
		return preBeanList;
	}
	
	/**
	 * 根据报价的明细结果 做分组排序 add by zhanghao
	 * 按游客类型分组 每天的价格集合
	 * 按价格的顺序排序
	 * @param bean
	 * @return
	 */
	private Map<Integer, Map<String, List<GuestPriceJsonBean>>> sortDetailGuestList(QuotedPriceJsonBean bean){
		Map<Integer, Map<String, List<GuestPriceJsonBean>>> map = groupDetailGuestList(bean);
		//普通游客类型对应的价格
		Map<String, List<GuestPriceJsonBean>> map0 = map.get(0);
		//按每天的价格 做 顺序的排序，
		Set<Entry<String, List<GuestPriceJsonBean>>> set0 = map0.entrySet();
		for(Entry<String, List<GuestPriceJsonBean>> entry:set0){
			List<GuestPriceJsonBean> value = entry.getValue();
			Collections.sort(value, new Comparator<GuestPriceJsonBean>() {  
	            @Override  
	            public int compare(GuestPriceJsonBean arg0, GuestPriceJsonBean arg1) {  
	            	if (arg0.getAmount() - arg1.getAmount()>0) {  
	                    return 1;  
	                } else {  
	                    return -1;  
	                }
	            }  
	        });  
		}
				
		//第N人对应的游客类型 价格
		Map<String, List<GuestPriceJsonBean>> map1 = map.get(1);
		//按每天的价格 做 顺序的排序，
		Set<Entry<String, List<GuestPriceJsonBean>>> set1 = map1.entrySet();
		for(Entry<String, List<GuestPriceJsonBean>> entry:set1){
			List<GuestPriceJsonBean> value = entry.getValue();
			Collections.sort(value, new Comparator<GuestPriceJsonBean>() {  
	            @Override  
	            public int compare(GuestPriceJsonBean arg0, GuestPriceJsonBean arg1) {  
	            	if (arg0.getAmount() - arg1.getAmount()>0) {  
	                    return 1;  
	                } else {  
	                    return -1;  
	                }
	            }  
	        });  
		}
		return map;
	}
	/**
	 * 根据报价的明细结果 做分组排序 add by zhanghao
	 * 按游客类型分组 每天的价格集合
	 * KEY:0 value:正常的游客类型对象
	 * KEY:1 value:第N人代表的对应游客类型对象
	 * @param bean
	 * @return
	 */
	private Map<Integer,Map<String,List<GuestPriceJsonBean>>> groupDetailGuestList(QuotedPriceJsonBean bean){
		Map<Integer,Map<String,List<GuestPriceJsonBean>>> map = new HashMap<Integer,Map<String,List<GuestPriceJsonBean>>>();
		
		Map<String,List<GuestPriceJsonBean>> map0  = new HashMap<String,List<GuestPriceJsonBean>>();
		Map<String,List<GuestPriceJsonBean>> map1  = new HashMap<String,List<GuestPriceJsonBean>>();
		
		map.put(0, map0);
		map.put(1, map1);
		
		//根据游客类型分组 详细报价，即：游客类型对应每天的报价集合
		List<QuotedPriceDetailJsonBean> detailList = bean.getDetailList();
		for(QuotedPriceDetailJsonBean deatilBean:detailList){
			List<GuestPriceJsonBean> guestList = deatilBean.getGuestPriceList();
			for(GuestPriceJsonBean priceBean:guestList){
				
				if(priceBean.getIsThirdPerson()==0){//正常的游客类型对象
					if(map0.containsKey(priceBean.getTravelerType())){
						map0.get(priceBean.getTravelerType()).add(priceBean);
					}else{
						List<GuestPriceJsonBean> list = new ArrayList<GuestPriceJsonBean>();
						list.add(priceBean);
						map0.put(priceBean.getTravelerType(), list);
					}
				}else if(priceBean.getIsThirdPerson()==1){//第N人代表的对应游客类型对象
					if(map1.containsKey(priceBean.getTravelerType())){
						map1.get(priceBean.getGuestType()).add(priceBean);
					}else{
						List<GuestPriceJsonBean> list = new ArrayList<GuestPriceJsonBean>();
						list.add(priceBean);
						map1.put(priceBean.getGuestType(), list);
					}
				}
				
			}
		}
		
		return map;
	}
	
	
	/**
	 * 对多个关联优惠做级别的排序（先计算哪个优惠），V1只有默认的四种优惠，所以此处按自然顺序进行排序
	 * V2时可能增加优惠类型优先级的属性进行计算
	 * add by zhanghao
	 * @param preferentialList
	 */
	private List<HotelPlPreferential> sortList(List<HotelPlPreferential> preferentialList){
		Map<Integer,HotelPlPreferential> map = new TreeMap<Integer,HotelPlPreferential>();
		for(HotelPlPreferential plpre:preferentialList){
			HotelPlPreferentialMatter matter = plpre.getMatter();
			Integer type = PreferentialTemplates.uuid2Type.get(matter.getPreferentialTemplatesUuid());
			map.put(type, plpre);
		}
		Collection<HotelPlPreferential> coll = map.values();
		List<HotelPlPreferential> result=new ArrayList<HotelPlPreferential>();
		for(HotelPlPreferential c:coll){
			preferentialList.add(c);
		}
		return result;
	}
	/**
	 * 根据过滤后分组的优惠信息做优惠的价格计算
	 * add by zhanghao
	 * @param preferentialTotal
	 * @param bean
	 */
	private void computePrice4preferential(List<PreferentialJsonBean> preferentialTotal ,QuotedPriceJsonBean bean,AutoQuotedPriceDataUtil dataUtil){
		
		if(CollectionUtils.isNotEmpty(preferentialTotal)){
			for(PreferentialJsonBean jsonBean:preferentialTotal){
				//初始化优惠价格，默认第一次是报价的结果
				initPrice(jsonBean, bean);
				
				List<HotelPlPreferential> preferentialList = jsonBean.getPreferentialList();
				
//				preferentialList=sortList(preferentialList);分组优惠时已经做了排序的处理，此处可以省略排序
				
				//临时的价格bean 存放当前优惠的打折力度。即:各项的优惠金额（原金额-打折后的金额）
				QuotedPriceJsonBean tempBean = new QuotedPriceJsonBean();
				BeanUtil.copySimpleProperties(tempBean, bean);
				int i=0;
				for(HotelPlPreferential pre:preferentialList){
					
					//每个优惠的临时价格存储对象
					PreferentialJsonBean tempJsonBean = new PreferentialJsonBean();
					initPrice(tempJsonBean, bean);
					
					//优惠价格计算
					this.computePrice4GuestTypeAndThirdPerson(pre,tempJsonBean,bean,dataUtil);
					
					//设置每次的打折力度，即每次的打折优惠金额保存到临时对象中 
					setTempBean(tempBean, tempJsonBean, bean,i++);
					
					//重置打包价格
					jsonBean.setTotalPrice(tempJsonBean.getTotalPrice());
					jsonBean.setTotalPriceCurrencyId(tempJsonBean.getTotalPriceCurrencyId());
					jsonBean.setTotalPriceCurrencyText(tempJsonBean.getTotalPriceCurrencyText());
				}
				computePreferPrice(tempBean, jsonBean, bean);
			}
		}
	}
	
	
	/**
	 * 设置每次的打折力度，即每次的打折优惠金额保存到临时对象中
	 * add by zhanghao
	 * @param tempBean
	 * @param jsonBean
	 * @param bean
	 */
	private void setTempBean(QuotedPriceJsonBean tempBean ,PreferentialJsonBean jsonBean,QuotedPriceJsonBean bean,int i){
		if(i==0){//第一次循环
			Double tempMixlivePrice = bean.getMixlivePrice()-jsonBean.getMixlivePrice();
			tempBean.setMixlivePrice(tempMixlivePrice);
			
			List<GuestPriceJsonBean> beanGuestPriceList = bean.getGuestPriceList();
			List<GuestPriceJsonBean> jsonBeanGuestPriceList = jsonBean.getGuestPriceList();
			List<GuestPriceJsonBean> tempGuestPriceList = new ArrayList<GuestPriceJsonBean>();
			tempBean.setGuestPriceList(tempGuestPriceList);
			
			if(CollectionUtils.isNotEmpty(beanGuestPriceList)&&CollectionUtils.isNotEmpty(jsonBeanGuestPriceList)){
				Map<Object, List<GuestPriceJsonBean>> isThirdBeanMap = BeanUtil.groupByKeyString("isThirdPerson", beanGuestPriceList);
				Map<Object, List<GuestPriceJsonBean>> isThirdJsonBeanMap = BeanUtil.groupByKeyString("isThirdPerson", jsonBeanGuestPriceList);
				
				Set<Object> isThirdBeanKeySet = isThirdBeanMap.keySet();
				for(Object key:isThirdBeanKeySet){
					String proKey="travelerType";
					if(key.toString().equals("1")){
						proKey="guestType";
					}
					List<GuestPriceJsonBean> keyIsThirdBeanList = isThirdBeanMap.get(key);
					Map<Object, GuestPriceJsonBean> beanMap = BeanUtil.groupByKeyString2Obj(proKey, keyIsThirdBeanList);
					
					List<GuestPriceJsonBean> keyIsThirdJsonBeanList = isThirdJsonBeanMap.get(key);
					Map<Object, GuestPriceJsonBean> jsonBeanMap = BeanUtil.groupByKeyString2Obj(proKey, keyIsThirdJsonBeanList);
					
					Set<Object> beanMapKeySet = beanMap.keySet();
					for(Object beanKey:beanMapKeySet){
						GuestPriceJsonBean beanGuestPrice = beanMap.get(beanKey);
						GuestPriceJsonBean jsonBeanGuestPrice = jsonBeanMap.get(beanKey);
						double tempAmount = beanGuestPrice.getAmount()-jsonBeanGuestPrice.getAmount();
						
						GuestPriceJsonBean tempGuestPrice = new GuestPriceJsonBean();
						BeanUtil.copySimpleProperties(tempGuestPrice, beanGuestPrice);
						tempGuestPrice.setAmount(tempAmount);
						tempGuestPriceList.add(tempGuestPrice);
					}
					
				}
				
			}
		}else{//第二次循环开始
			Double tempMixlivePrice = bean.getMixlivePrice()-jsonBean.getMixlivePrice();
			tempBean.setMixlivePrice(tempBean.getMixlivePrice()+tempMixlivePrice);
			
			List<GuestPriceJsonBean> beanGuestPriceList = bean.getGuestPriceList();
			List<GuestPriceJsonBean> jsonBeanGuestPriceList = jsonBean.getGuestPriceList();
			List<GuestPriceJsonBean> tempGuestPriceList = tempBean.getGuestPriceList();
			
			if(CollectionUtils.isNotEmpty(beanGuestPriceList)&&CollectionUtils.isNotEmpty(jsonBeanGuestPriceList)){
				
				Map<Object, List<GuestPriceJsonBean>> isThirdBeanMap = BeanUtil.groupByKeyString("isThirdPerson", beanGuestPriceList);
				Map<Object, List<GuestPriceJsonBean>> isThirdJsonBeanMap = BeanUtil.groupByKeyString("isThirdPerson", jsonBeanGuestPriceList);
				Map<Object, List<GuestPriceJsonBean>> isThirdTempBeanMap = BeanUtil.groupByKeyString("isThirdPerson", tempGuestPriceList);
				
				Set<Object> isThirdBeanKeySet = isThirdBeanMap.keySet();
				for(Object key:isThirdBeanKeySet){
					String proKey="travelerType";
					if(key.toString().equals("1")){
						proKey="guestType";
					}
					
					List<GuestPriceJsonBean> keyIsThirdBeanList = isThirdBeanMap.get(key);
					Map<Object, GuestPriceJsonBean> beanMap = BeanUtil.groupByKeyString2Obj(proKey, keyIsThirdBeanList);
					
					List<GuestPriceJsonBean> keyIsThirdJsonBeanList = isThirdJsonBeanMap.get(key);
					Map<Object, GuestPriceJsonBean> jsonBeanMap = BeanUtil.groupByKeyString2Obj(proKey, keyIsThirdJsonBeanList);
					
					List<GuestPriceJsonBean> keyIsThirdTempBeanList = isThirdTempBeanMap.get(key);
					Map<Object, GuestPriceJsonBean> tempBeanMap = BeanUtil.groupByKeyString2Obj(proKey, keyIsThirdTempBeanList);
					
					Set<Object> beanMapKeySet = beanMap.keySet();
					for(Object beanKey:beanMapKeySet){
						GuestPriceJsonBean beanGuestPrice = beanMap.get(beanKey);
						GuestPriceJsonBean jsonBeanGuestPrice = jsonBeanMap.get(beanKey);
						double tempAmount = beanGuestPrice.getAmount()-jsonBeanGuestPrice.getAmount();
						GuestPriceJsonBean tempGuestPrice = tempBeanMap.get(beanKey);
						tempGuestPrice.setAmount(tempGuestPrice.getAmount()+tempAmount);
					}
				}
			}
		}
		
	}
	
	/**
	 * 根据每次的打折力度进行总体的优惠价格计算，用于叠加优惠时使用
	 * add by zhanghao
	 * @param tempBean
	 * @param jsonBean
	 * @param bean
	 */
	private void computePreferPrice(QuotedPriceJsonBean tempBean ,PreferentialJsonBean jsonBean,QuotedPriceJsonBean bean){
		if(tempBean!=null){
			Double preferMixlivePrice = bean.getMixlivePrice()-tempBean.getMixlivePrice();
			
			jsonBean.setMixlivePrice(formatDouble(preferMixlivePrice));
			
			List<GuestPriceJsonBean> beanGuestPriceList = bean.getGuestPriceList();
			List<GuestPriceJsonBean> jsonBeanGuestPriceList = jsonBean.getGuestPriceList();
			List<GuestPriceJsonBean> tempGuestPriceList = tempBean.getGuestPriceList();
			
			if(CollectionUtils.isNotEmpty(beanGuestPriceList)&&CollectionUtils.isNotEmpty(jsonBeanGuestPriceList)&&CollectionUtils.isNotEmpty(tempGuestPriceList)){
				
				Map<Object, List<GuestPriceJsonBean>> isThirdBeanMap = BeanUtil.groupByKeyString("isThirdPerson", beanGuestPriceList);
				Map<Object, List<GuestPriceJsonBean>> isThirdJsonBeanMap = BeanUtil.groupByKeyString("isThirdPerson", jsonBeanGuestPriceList);
				Map<Object, List<GuestPriceJsonBean>> isThirdTempBeanMap = BeanUtil.groupByKeyString("isThirdPerson", tempGuestPriceList);
				
				Set<Object> isThirdBeanKeySet = isThirdBeanMap.keySet();
				for(Object key:isThirdBeanKeySet){
					
					String proKey="travelerType";
					if(key.toString().equals("1")){
						proKey="guestType";
					}
					
					List<GuestPriceJsonBean> keyIsThirdBeanList = isThirdBeanMap.get(key);
					Map<Object, GuestPriceJsonBean> beanMap = BeanUtil.groupByKeyString2Obj(proKey, keyIsThirdBeanList);
					
					List<GuestPriceJsonBean> keyIsThirdJsonBeanList = isThirdJsonBeanMap.get(key);
					Map<Object, GuestPriceJsonBean> jsonBeanMap = BeanUtil.groupByKeyString2Obj(proKey, keyIsThirdJsonBeanList);
					
					List<GuestPriceJsonBean> keyIsThirdTempBeanList = isThirdTempBeanMap.get(key);
					Map<Object, GuestPriceJsonBean> tempBeanMap = BeanUtil.groupByKeyString2Obj(proKey, keyIsThirdTempBeanList);
					
					Set<Object> beanMapKeySet = beanMap.keySet();
					for(Object beanKey:beanMapKeySet){
						GuestPriceJsonBean beanGuestPrice = beanMap.get(beanKey);
						GuestPriceJsonBean jsonBeanGuestPrice = jsonBeanMap.get(beanKey);
						GuestPriceJsonBean tempGuestPrice = tempBeanMap.get(beanKey);
						double preferAmount = beanGuestPrice.getAmount()-tempGuestPrice.getAmount();
						jsonBeanGuestPrice.setAmount(formatDouble(preferAmount));
						jsonBeanGuestPrice.setPreferAmount(formatDouble(tempGuestPrice.getAmount()));
						if(tempGuestPrice.getAmount()!=null && tempGuestPrice.getAmount()!=0){
							jsonBeanGuestPrice.setPreferAmount(formatDouble(0-tempGuestPrice.getAmount()));
						}else{
							jsonBeanGuestPrice.setPreferAmount(formatDouble(tempGuestPrice.getAmount()));
						}
					}
				}
				
			}
		}
	}
	
	
	
	
	/**
	 * 优惠前的价格初始化，默认是优惠前的合计价格（包括：房、餐、交通和例外的各项税金）add by zhanghao
	 * @param jsonBean
	 * @param bean
	 */
	private void initPrice(PreferentialJsonBean jsonBean,QuotedPriceJsonBean bean){
		List<GuestPriceJsonBean> guestPriceList = new ArrayList<GuestPriceJsonBean>();
		if(CollectionUtils.isNotEmpty(bean.getGuestPriceList())){
			for(GuestPriceJsonBean guest:bean.getGuestPriceList()){
				GuestPriceJsonBean g1 = new GuestPriceJsonBean();
				BeanUtil.copySimpleProperties(g1, guest);
				guestPriceList.add(g1);
			}
		}
		jsonBean.setGuestPriceList(guestPriceList);
		jsonBean.setMixlivePrice(bean.getMixlivePrice());
		jsonBean.setMixlivePriceCurrencyId(bean.getMixlivePriceCurrencyId());
		jsonBean.setMixlivePriceCurrencyText(bean.getMixlivePriceCurrencyText());
		jsonBean.setTotalPrice(0d);
		jsonBean.setTotalPriceCurrencyId("");
		jsonBean.setTotalPriceCurrencyText("");
	}
	
	/**
	 * 验证是否可以使用优惠
	 * 当前游客类型的房（餐或者交通）是否可以使用当前优惠
	 * @param pre 当前优惠信息
	 * @param taxType  税费类型（1、房费；2、餐费；3、交通费）
	 * @param travelerTypeUuid 游客类型UUID
	 * @return 返回true可以使用，false不可以使用
	 */
	private boolean checkUsePrefer (HotelPlPreferential pre,String taxType,String travelerTypeUuid){
		boolean result = false;
		//当前优惠信息的 税金设置
		Map<String, List<HotelPlPreferentialTax>> taxs = pre.getMatter().getPreferentialTaxMap();
		if(MapUtils.isNotEmpty(taxs)){
			if(taxs.containsKey(taxType)){
				for(HotelPlPreferentialTax tax:taxs.get(taxType)){
					//筛选出 同税费类型（1、房费；2、餐费；3、交通费） 、同游客类型的 税费 设置
					if(tax.getType()==Integer.parseInt(taxType) &&tax.getTravelerTypeUuid().equals(travelerTypeUuid)){
						//验证是否 可以 使用 优惠
						if(tax.getPreferentialType()==HotelPlPreferentialTax.PREFERENTIAL_TYPE_0){
							result=false;
							break;
						}else{
							result = true;
						}
					}
				}	
			}		
		}
		return result;
	}
	
	/**
	 * 根据不同的优惠类型 进行 分别的 优惠计算
	 * 住付：合计后价格-免费晚（含税）价格+免费晚（不含税）价格的税金（优惠中的税金设置）+合计餐费+原交通费（含税）*打折-原交通费（或者+合计交通费-原交通费（含税）或者 -减金额）
	 * 提前：原房费（不含税）*打折（或者-减金额）*（1+税率）+合计餐费+合计交通费（或者原来交通费*打折 或者原交通费-减金额）
	 * add by zhanghao
	 * @param pre
	 * @param jsonBean
	 */
	private void computePrice4GuestTypeAndThirdPerson(HotelPlPreferential pre,PreferentialJsonBean jsonBean,QuotedPriceJsonBean bean,AutoQuotedPriceDataUtil dataUtil){
		
		try {
			HotelPlPreferentialMatter matter = pre.getMatter();
			
			//根据模板生成的输入框中的输入值集合
			List<HotelPlPreferentialMatterValue> valueList = pre.getValueList();
			//按不同的KEY值分组
			Map<Object, HotelPlPreferentialMatterValue> myMap = BeanUtil.groupByKeyString2Obj("myKey", valueList);
			
			if(PreferentialTemplates.type2Uuid.get(PreferentialTemplates.PREFERENTIAL_TEMPLATE_TYPE_0).equals(matter.getPreferentialTemplatesUuid())){//住付优惠
				HotelPlPreferentialMatterValue value = myMap.get(HotelPlPreferentialMatterValue.MYKEY_EXEMPT);
				Integer dayNum = Integer.parseInt(value.getMyValue());//免费的晚数
				
				//报价明细的记录数
				Integer totalDayNum = 0;
				
				//游客类型的价格计算 （每个游客类型合计后的优惠价格）
				List<GuestPriceJsonBean> jsonBeanGuestPriceList = jsonBean.getGuestPriceList();
				for(GuestPriceJsonBean preGuestPriceJsonBean:jsonBeanGuestPriceList){
					Map<String, List<GuestPriceJsonBean>> tempMap = new HashMap<String, List<GuestPriceJsonBean>>();
					
					String keyUuid="";
					if(preGuestPriceJsonBean.getIsThirdPerson()==0){//当前价格是游客类型价格
						tempMap = dataUtil.getGroupDetailPriceList4TravelTypeMap().get(0);
						keyUuid=preGuestPriceJsonBean.getTravelerType();
					}else{//当前价格是第N人价格
						tempMap = dataUtil.getGroupDetailPriceList4TravelTypeMap().get(1);
						if(pre.getRequire().getApplicableThirdPerson()!=0){//如果当前优惠不支持第三人则会跳过循环
							continue;
						}
						keyUuid=preGuestPriceJsonBean.getGuestType();
					}
					
					/**报价后的明细价格集合 按 游客类型 分组排序，KEY是游客类型，VALUE是对应的每天价格集合（按价格顺序排序） 。*/
					if(tempMap.containsKey(keyUuid)){
						if(preGuestPriceJsonBean.getAmount()==null||preGuestPriceJsonBean.getAmount()==0
								||preGuestPriceJsonBean.getRoomAmount()==null||preGuestPriceJsonBean.getRoomAmount()==0){//没有报价结果的游客类型跳过
							continue;
						}
						
						//报价合计价格 减去 最低的免费晚明细价格（包括房费、餐费、交通费并各自含税）-----------start
						List<GuestPriceJsonBean> groupList = tempMap.get(keyUuid);
						List<GuestPriceJsonBean> deductPriceList=new ArrayList<GuestPriceJsonBean>();
						
						totalDayNum=groupList.size();
						//交通费的税后 合计包括往返
						double islandwayPriceTotal = 0;
						
						
						for(GuestPriceJsonBean temp:groupList){
							if(temp.getIslandwayAmountTotal()!=null){
								islandwayPriceTotal+=temp.getIslandwayAmountTotal();
							}
						}
						
						//减去免费晚的 合计明细价格
						for(int i=0;i<dayNum;i++){
							if(groupList.size()>i){
								GuestPriceJsonBean gpjb = groupList.get(i);
								
								//可以使用优惠--当前的减最低优惠
								if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_1+"", preGuestPriceJsonBean.getTravelerType())){
									//减去免费晚的房费
									if(gpjb.getRoomAmountTotal()!=null){//减去免费晚的含税房费
										double rmbRoomAmountTotal = tranfer2RMB4exchangeRate(gpjb.getRoomAmountTotal(), dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
										preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()-rmbRoomAmountTotal));
									}else{
										dataUtil.putMessage("缺失房价，计算住付优惠异常！");
									}
									
								}
								deductPriceList.add(gpjb);
								
								
							}
						}
						
						/**计算免费晚 的 房费税金和餐费、交通费等。 注：此处只会补交 房费的税金-根据价单中维护的价格进行计算*/
						if(CollectionUtils.isNotEmpty(deductPriceList)){
							
							//免费晚的房税 循环 补交
							for(GuestPriceJsonBean deductPrice:deductPriceList){
								
								//可以使用优惠进行计算
								if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_1+"", preGuestPriceJsonBean.getTravelerType())){
									//房税补交
									double roomTax = priceTaxex4PreType0(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_1+"", deductPrice,0d,dataUtil);
//									roomTax=roomTax-deductPrice.getRoomAmount();//当前房税是包含房价的，需要减去当前的房价。剩余的值就是房税
									
									preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()+roomTax));
								}
								
								
							}
							
							
							//可以使用优惠进行计算
							if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_2+"", preGuestPriceJsonBean.getTravelerType())){
								//餐费补交
								double mealTax = priceTaxex4PreType0(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_2+"", deductPriceList.get(0),0d,dataUtil);
								preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()+mealTax));
							}
							
							//可以使用优惠进行计算
							if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_3+"", preGuestPriceJsonBean.getTravelerType())){
								//交通费补交
								double islandwayTax = priceTaxex4PreType0(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_3+"", deductPriceList.get(0),islandwayPriceTotal,dataUtil);
								preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()+islandwayTax));
							}
							
						}
						
					}
				}
				
			}else if(PreferentialTemplates.type2Uuid.get(PreferentialTemplates.PREFERENTIAL_TEMPLATE_TYPE_1).equals(matter.getPreferentialTemplatesUuid())){//提前预订优惠
				//游客类型的价格计算 （每个游客类型合计后的优惠价格）
				List<GuestPriceJsonBean> jsonBeanGuestPriceList = jsonBean.getGuestPriceList();
				for(GuestPriceJsonBean preGuestPriceJsonBean:jsonBeanGuestPriceList){
					
					Map<String, List<GuestPriceJsonBean>> tempMap = new HashMap<String, List<GuestPriceJsonBean>>();
					
					String keyUuid="";
					if(preGuestPriceJsonBean.getIsThirdPerson()==0){//当前价格是游客类型价格
						tempMap = dataUtil.getGroupDetailPriceList4TravelTypeMap().get(0);
						keyUuid=preGuestPriceJsonBean.getTravelerType();
					}else{//当前价格是第N人价格
						tempMap = dataUtil.getGroupDetailPriceList4TravelTypeMap().get(1);
						if(pre.getRequire().getApplicableThirdPerson()!=0){//如果当前优惠不支持第三人则会跳过循环
							continue;
						}
						keyUuid=preGuestPriceJsonBean.getGuestType();
					}
					
					
					if(preGuestPriceJsonBean.getAmount()==null||preGuestPriceJsonBean.getAmount()==0
							||preGuestPriceJsonBean.getRoomAmount()==null||preGuestPriceJsonBean.getRoomAmount()==0){//没有报价结果的游客类型跳过
						continue;
					}
					
					//报价后的明细价格集合 按 游客类型 分组排序，KEY是游客类型，VALUE是对应的每天价格集合（按价格顺序排序） 。
					List<GuestPriceJsonBean> groupList = tempMap.get(keyUuid);
					
					//交通费的税后 合计包括往返
					double islandwayPriceTotal = 0;
					double roomPriceTotal = 0;
					double roomPrice = 0;
					double mealPriceTotal = 0;
					for(GuestPriceJsonBean temp:groupList){
						if(temp.getIslandwayAmountTotal()!=null){
							islandwayPriceTotal+=temp.getIslandwayAmountTotal();
						}
						if(temp.getRoomAmountTotal()!=null){
							roomPriceTotal+=temp.getRoomAmountTotal();
							roomPrice+=temp.getRoomAmount();
						}
						if(temp.getMealAmountTotal()!=null){
							mealPriceTotal+=temp.getMealAmountTotal();
						}
					}
					
					//可以使用优惠进行计算
					if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_1+"", preGuestPriceJsonBean.getTravelerType())){
						//房税补交
						double roomTax = priceTaxex4PreType1(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_1+"", preGuestPriceJsonBean,roomPrice,groupList.size(),dataUtil);
						preGuestPriceJsonBean.setAmount(roomTax);
					}else{//如果不需要计算房费的优惠则加入之前的房费
						double roomPriceTotalRMB = tranfer2RMB4exchangeRate(roomPriceTotal, dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
						preGuestPriceJsonBean.setAmount(formatDouble(roomPriceTotalRMB));
					}
					
					//可以使用优惠进行计算
					if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_2+"", preGuestPriceJsonBean.getTravelerType())){
						//餐费补交
						double mealTax = priceTaxex4PreType1(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_2+"", preGuestPriceJsonBean,0d,0,dataUtil);
						preGuestPriceJsonBean.setAmount(preGuestPriceJsonBean.getAmount()+mealTax);
					}else{//如果不需要计算餐费的优惠则会增加之前的餐费总和
						double mealPriceTotalRMB = tranfer2RMB4exchangeRate(mealPriceTotal, dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
						preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()+mealPriceTotalRMB));
					}
					
					//可以使用优惠进行计算
					if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_3+"", preGuestPriceJsonBean.getTravelerType())){
						//交通费补交
						double islandwayTax = priceTaxex4PreType1(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_3+"", preGuestPriceJsonBean,islandwayPriceTotal,0,dataUtil);
						preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()+islandwayTax));
					}else{//如果不需要计算交通费的优惠则会增加之前的交通费总和
						double islandwayPriceTotalRMB = tranfer2RMB4exchangeRate(islandwayPriceTotal, dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
						preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()+islandwayPriceTotalRMB));
					}
					
				}
				
				
				
			}else if(PreferentialTemplates.type2Uuid.get(PreferentialTemplates.PREFERENTIAL_TEMPLATE_TYPE_2).equals(matter.getPreferentialTemplatesUuid())){//打包优惠
				HotelPlPreferentialMatterValue value = myMap.get(HotelPlPreferentialMatterValue.MYKEY_TOTAL);
				Double totalPrice = Double.parseDouble(value.getMyValue());//打包的总价
				
				//默认取第一个游客类型的所有报价详情
				GuestPriceJsonBean prebean = jsonBean.getGuestPriceList().get(0);
				if(MapUtils.isNotEmpty(dataUtil.getGroupDetailPriceList4TravelTypeMap().get(0))){
					List<GuestPriceJsonBean> groupList = dataUtil.getGroupDetailPriceList4TravelTypeMap().get(0).get(prebean.getTravelerType());
					double result = priceTaxex4PreType2(pre, prebean, totalPrice, groupList.size(),dataUtil);
					jsonBean.setTotalPrice(formatDouble(result));
					jsonBean.setTotalPriceCurrencyId(dataUtil.getRmbCurrencyId().toString());
					jsonBean.setTotalPriceCurrencyText("￥");
					
					//打包价格没有第三人的价格处理
					if(pre.getRequire().getApplicableThirdPerson()==0){
						
					}
				}
				
				
			}else if(PreferentialTemplates.type2Uuid.get(PreferentialTemplates.PREFERENTIAL_TEMPLATE_TYPE_3).equals(matter.getPreferentialTemplatesUuid())){//房餐交通优惠
				//房餐交通优惠和提前预订优惠相同算法
				
				//游客类型的价格计算 （每个游客类型合计后的优惠价格）
				List<GuestPriceJsonBean> jsonBeanGuestPriceList = jsonBean.getGuestPriceList();
				for(GuestPriceJsonBean preGuestPriceJsonBean:jsonBeanGuestPriceList){
					Map<String, List<GuestPriceJsonBean>> tempMap = new HashMap<String, List<GuestPriceJsonBean>>();
					
					String keyUuid="";
					if(preGuestPriceJsonBean.getIsThirdPerson()==0){//当前价格是游客类型价格
						tempMap = dataUtil.getGroupDetailPriceList4TravelTypeMap().get(0);
						keyUuid=preGuestPriceJsonBean.getTravelerType();
					}else{//当前价格是第N人价格
						tempMap = dataUtil.getGroupDetailPriceList4TravelTypeMap().get(1);
						if(pre.getRequire().getApplicableThirdPerson()!=0){//如果当前优惠不支持第三人则会跳过循环
							continue;
						}
						keyUuid=preGuestPriceJsonBean.getGuestType();
					}
					
					if(preGuestPriceJsonBean.getAmount()==null||preGuestPriceJsonBean.getAmount()==0
							||preGuestPriceJsonBean.getRoomAmount()==null||preGuestPriceJsonBean.getRoomAmount()==0){//没有报价结果的游客类型跳过
						continue;
					}
					
					//报价后的明细价格集合 按 游客类型 分组排序，KEY是游客类型，VALUE是对应的每天价格集合（按价格顺序排序） 。
					List<GuestPriceJsonBean> groupList = tempMap.get(keyUuid);
					
					//交通费的税后 合计包括往返
					double islandwayPriceTotal = 0;
					double roomPriceTotal = 0;
					double roomPrice = 0;
					double mealPriceTotal = 0;
					for(GuestPriceJsonBean temp:groupList){
						if(temp.getIslandwayAmountTotal()!=null){
							islandwayPriceTotal+=temp.getIslandwayAmountTotal();
						}
						if(temp.getRoomAmountTotal()!=null){
							roomPriceTotal+=temp.getRoomAmountTotal();
							roomPrice+=temp.getRoomAmount();
						}
						if(temp.getMealAmountTotal()!=null){
							mealPriceTotal+=temp.getMealAmountTotal();
						}
					}
					
					//可以使用优惠进行计算
					if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_1+"", preGuestPriceJsonBean.getTravelerType())){
						//房税补交
						double roomTax = priceTaxex4PreType1(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_1+"", preGuestPriceJsonBean,roomPrice,groupList.size(),dataUtil);
						preGuestPriceJsonBean.setAmount(roomTax);
					}else{
						double roomPriceTotalRMB = tranfer2RMB4exchangeRate(roomPriceTotal, dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
						preGuestPriceJsonBean.setAmount(formatDouble(roomPriceTotalRMB));
					}
					
					//可以使用优惠进行计算
					if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_2+"", preGuestPriceJsonBean.getTravelerType())){
						//餐费补交
						double mealTax = priceTaxex4PreType1(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_2+"", preGuestPriceJsonBean,0d,0,dataUtil);
						preGuestPriceJsonBean.setAmount(preGuestPriceJsonBean.getAmount()+mealTax);
					}else{//如果不需要计算餐费的优惠则会增加之前的餐费总和
						double mealPriceTotalRMB = tranfer2RMB4exchangeRate(mealPriceTotal, dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
						preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()+mealPriceTotalRMB));
					}
					
					//可以使用优惠进行计算
					if(checkUsePrefer(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_3+"", preGuestPriceJsonBean.getTravelerType())){
						//交通费补交
						double islandwayTax = priceTaxex4PreType1(pre, HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_3+"", preGuestPriceJsonBean,islandwayPriceTotal,0,dataUtil);
						preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()+islandwayTax));
					}else{//如果不需要计算交通费的优惠则会增加之前的交通费总和
						double islandwayPriceTotalRMB = tranfer2RMB4exchangeRate(islandwayPriceTotal, dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
						preGuestPriceJsonBean.setAmount(formatDouble(preGuestPriceJsonBean.getAmount()+islandwayPriceTotalRMB));
					}
					
				}
				
			}else{
				//V1只有4种优惠，多余的不做处理
			}
		} catch (NumberFormatException e) {
			logger.error("优惠信息中的模板输入信息格式不对！",e);
			
		}
	}

	
	
	
	/**
	 * 住付优惠的计算费用。
	 * 根据 优惠信息中的税金设置 对 费用类型（房、餐、交通）的费用进行税金的计算
	 * --V1需求 只 针对 房费做税金的补交计算 餐费、交通费不在计算在内
	 * add by zhanghao
	 * 
	 * 计算公式：住N免M晚
	 * 		N晚的成人价格合计（即报价结果中的游客类型价格合计包括房、餐、交通）
	 * 		-M晚的成人房费合计（即报价结果中的明细价格最低的M晚记录含税房费）
	 * 		+M晚的补交房费税金（原不含税的房费计算税金）
	 * 		+M晚的餐费（合计金额）
	 * 		+   合计交通费-原来交通费合计
	 * 		或者+合计交通费*打折 - 原来交通费合计
	 * 		或者-减金额
	 * 
	 * @param pre 当前优惠信息
	 * @param taxType 税费类型（1、房费；2、餐费；3、交通费）
	 * @param guestPriceBean 税金基础对象
	 * @param islandwayPriceTotal 打折时需要的 总价（目前只有交通费的打折会使用）
	 * @param dataUtil 公用数据对象
	 * @return 
	 */
	private double priceTaxex4PreType0(HotelPlPreferential pre,String taxType,GuestPriceJsonBean guestPriceBean,Double islandwayPriceTotal,AutoQuotedPriceDataUtil dataUtil){
		double result=0d;
		//得到 当前价单 的 税金集合
		if(dataUtil.getTaxPriceMap().containsKey(guestPriceBean.getHotelPlUuid())&&guestPriceBean.getRoomAmount()!=null){
			List<HotelPlTaxPrice> taxList = dataUtil.getTaxPriceMap().get(guestPriceBean.getHotelPlUuid());
			
			//按税金的类型进行分组 1、政府税；2、服务费；3、床税；4、其他
			Map<Object, HotelPlTaxPrice> taxMap = BeanUtil.groupByKeyString2Obj("taxType", taxList);
			
			//当前优惠信息的 税金设置
			Map<String, List<HotelPlPreferentialTax>> taxs = pre.getMatter().getPreferentialTaxMap();
			if(MapUtils.isNotEmpty(taxs)){
				if(taxs.containsKey(taxType)){
					for(HotelPlPreferentialTax tax:taxs.get(taxType)){
						
						//筛选出 同税费类型（1、房费；2、餐费；3、交通费） 、同游客类型的 税费 设置
						if(tax.getType()==Integer.parseInt(taxType) &&tax.getTravelerTypeUuid().equals(guestPriceBean.getTravelerType())){
							//房费 税金的处理--餐费和交通费不会维护税金，不需要缴税
							
							if(Integer.parseInt(taxType)==HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_1){
								String isTaxs = tax.getIstax();
								if(StringUtils.isNotBlank(isTaxs)){
									boolean isDazhe=false;
									//多个税金类型的分隔
									String[] array = isTaxs.split(HotelPlTaxException.REGEX);
									for(String isTax:array){
										if(taxMap.containsKey(Integer.parseInt(isTax))){
											HotelPlTaxPrice price = taxMap.get(Integer.parseInt(isTax));
											if(price.getStartDate()==null||price.getEndDate()==null){
												//dataUtil.putMessage("税金字典设置的日期读取失败！");
												continue;
											}
											//判断是否在收取税费的 有效期内
											if(price.getStartDate().before(DateUtils.string2Date(guestPriceBean.getInDate()))
													&&price.getEndDate().after(DateUtils.string2Date(guestPriceBean.getInDate()))){
												
												//根据价单的税金字典设置 计算 税金
												if(price.getChargeType()!=null && price.getChargeType()==1){//百分比收税
//													result +=guestPriceBean.getRoomAmount()*price.getAmount()/100;
													//原不含税的房费计算税金
													isDazhe=true;
													result=getTaxAmount4Type(result, guestPriceBean.getRoomAmount(), price.getAmount(),dataUtil,price.getHotelPlUuid());
												}else if(price.getChargeType()!=null && price.getChargeType()==2){//固定的金额收税
													result = result+price.getAmount();
												}
												
											}
										}
									}
									if(isDazhe){
										result-=guestPriceBean.getRoomAmount();
									}
									break;
								}
							}else{//需要补交的餐费或者交通费
								
								//判断 报价条件中的基础餐型（或者升级餐型）和交通方式是否在优惠信息中配置，如果配置过可以使用优惠，否则不进行计算
								boolean mealUsed = checkPreUsed4Meal(taxType, dataUtil, tax);
								boolean islandUsed = checkPreUsed4Islandway(taxType, dataUtil, tax);
								//存在配置可以使用
								if(mealUsed || islandUsed){
									
									//根据优惠信息中的税金维护 计算 需要补交的餐费和交通费
									if(tax.getPreferentialAmount()!=null && tax.getPreferentialAmount()!=0){
										if(tax.getPreferentialType()==HotelPlPreferentialTax.PREFERENTIAL_TYPE_1){//合计直接返回金额
											result = tax.getPreferentialAmount()-islandwayPriceTotal;
										}else if(tax.getPreferentialType()==HotelPlPreferentialTax.PREFERENTIAL_TYPE_2){//打折需要按原价格进行计算
											result = islandwayPriceTotal*tax.getPreferentialAmount()/100-islandwayPriceTotal;
										}else if(tax.getPreferentialType()==HotelPlPreferentialTax.PREFERENTIAL_TYPE_3){//减金额 直接返回负数
											result = 0-tax.getPreferentialAmount();
										}
									}
									break;
								}
							}
						}
					}
				}
				
			}
		}
		//税金的汇率转换
		result = tranfer2RMB4exchangeRate(result, dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
		return result;
	}
	
	/**
	 * 提前预订 计算费用
	 * 根据 优惠信息中的税金设置 对 费用类型（房、餐、交通）的费用进行税金的计算
	 * --V1需求 只 针对 房费做税金的补交计算 餐费、交通费不在计算在内
	 * 计算公式：提前 60 天预订
	 * 		成人合计房费（不含税房费）*打折
	 * 	     或 成人合计房费（不含税房费）- 减金额
	 * 	    +优惠后的房费 的 税金
	 * 		+合计的餐费
	 * 		+合计的交通费-原交通费
	 * 		或+原交通费*打折 - 原交通费
	 * 		或-减金额 
	 * 
	 * add by zhanghao
	 * @param pre 当前优惠信息
	 * @param taxType 税费类型（1、房费；2、餐费；3、交通费）
	 * @param guestPriceBean 税金基础对象
	 * @param priceTotal 打折时需要的 总价（交通费和房费打折都可用）
	 * @param bednum 需要缴纳的 床税 个数
	 * @return 
	 */
	private double priceTaxex4PreType1(HotelPlPreferential pre,String taxType,GuestPriceJsonBean guestPriceBean,Double priceTotal,int bednum,AutoQuotedPriceDataUtil dataUtil){
		double result=0d;
		//得到 当前价单 的 税金集合
		if(dataUtil.getTaxPriceMap().containsKey(guestPriceBean.getHotelPlUuid())&&guestPriceBean.getRoomAmount()!=null){
			List<HotelPlTaxPrice> taxList = dataUtil.getTaxPriceMap().get(guestPriceBean.getHotelPlUuid());
			
			//按税金的类型进行分组 1、政府税；2、服务费；3、床税；4、其他
			Map<Object, HotelPlTaxPrice> taxMap = BeanUtil.groupByKeyString2Obj("taxType", taxList);
			
			//当前优惠信息的 税金设置
			Map<String, List<HotelPlPreferentialTax>> taxs = pre.getMatter().getPreferentialTaxMap();
			if(MapUtils.isNotEmpty(taxs)){
				if(taxs.containsKey(taxType)){

					for(HotelPlPreferentialTax tax:taxs.get(taxType)){
						if(guestPriceBean.getTravelerType().equals(tax.getTravelerTypeUuid())){
							
							//筛选出 同税费类型（1、房费；2、餐费；3、交通费） 、同游客类型的 税费 设置
							if(tax.getType()==Integer.parseInt(taxType) &&tax.getTravelerTypeUuid().equals(guestPriceBean.getTravelerType())){
								
								
								//房费 税金的处理--餐费和交通费不会维护税金，不需要缴税
								if(Integer.parseInt(taxType)==HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_1){
									
									//房费的 打折或者减金额计算 通过 房费总计进行计算（即：含税的价格）
									if(tax.getPreferentialAmount()!=null && tax.getPreferentialAmount()!=0){
										if(tax.getPreferentialType()==HotelPlPreferentialTax.PREFERENTIAL_TYPE_2){//打折需要按原价格进行计算
											result = priceTotal*tax.getPreferentialAmount()/100;
										}else if(tax.getPreferentialType()==HotelPlPreferentialTax.PREFERENTIAL_TYPE_3){//减金额 直接返回负数
											result = priceTotal-tax.getPreferentialAmount();
										}
									}
									
									String isTaxs = tax.getIstax();
									if(StringUtils.isNotBlank(isTaxs)){
										
										//多个税金类型的分隔
										String[] array = isTaxs.split(HotelPlTaxException.REGEX);
										for(String isTax:array){
											if(taxMap.containsKey(Integer.parseInt(isTax))){
												HotelPlTaxPrice price = taxMap.get(Integer.parseInt(isTax));
												if(price.getStartDate()==null||price.getEndDate()==null){
													//dataUtil.putMessage("税金字典设置的日期读取失败！");
													continue;
												}
												//判断是否在收取税费的 有效期内
												if(price.getStartDate().before(DateUtils.string2Date(guestPriceBean.getInDate()))
														&&price.getEndDate().after(DateUtils.string2Date(guestPriceBean.getInDate()))){
													
													//根据价单的税金字典设置 计算 税金
													if(price.getChargeType()!=null && price.getChargeType()==1){//百分比收税
//														result += priceTotal*price.getAmount()/100;
														result = getTaxAmount4Type(result, priceTotal, price.getAmount(),dataUtil,price.getHotelPlUuid());
													}else if(price.getChargeType()!=null && price.getChargeType()==2){//固定的金额收税
														result += price.getAmount()*bednum;
													}
													
												}
											}
										}
									}
									
								}else{
									
									//判断 报价条件中的基础餐型（或者升级餐型）和交通方式是否在优惠信息中配置，如果配置过可以使用优惠，否则不进行计算
									boolean mealUsed = checkPreUsed4Meal(taxType, dataUtil, tax);
									boolean islandUsed = checkPreUsed4Islandway(taxType, dataUtil, tax);
									
									//存在配置可以使用
									if(mealUsed || islandUsed){
										//根据优惠信息中的税金维护 计算 需要补交的餐费和交通费
										if(tax.getPreferentialAmount()!=null && tax.getPreferentialAmount()!=0){
											if(tax.getPreferentialType()==HotelPlPreferentialTax.PREFERENTIAL_TYPE_1){//合计直接返回金额
												result = tax.getPreferentialAmount();
											}else if(tax.getPreferentialType()==HotelPlPreferentialTax.PREFERENTIAL_TYPE_2){//打折需要按原价格进行计算
												result = priceTotal*tax.getPreferentialAmount()/100;
											}else if(tax.getPreferentialType()==HotelPlPreferentialTax.PREFERENTIAL_TYPE_3){//减金额 直接返回负数
												result = priceTotal-tax.getPreferentialAmount();
											}
										}
									}
								}
								
								break;
							}
						
						}
					}
				}
			}
		}
		//税金的汇率转换
		result = tranfer2RMB4exchangeRate(result, dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
		return result;
	}
	
	
	/**
	 * 打包 计算费用
	 * 只计算整体价格
	 * 
	 * add by zhanghao
	 * @param pre 当前优惠信息
	 * @param taxType 税费类型（1、房费；2、餐费；3、交通费）
	 * @param guestPriceBean 税金基础对象
	 * @param priceTotal 打折时需要的 总价（交通费和房费打折都可用）
	 * @param bednum 需要缴纳的 床税 个数
	 * @return 
	 */
	private double priceTaxex4PreType2(HotelPlPreferential pre,GuestPriceJsonBean guestPriceBean,Double priceTotal,int bednum,AutoQuotedPriceDataUtil dataUtil){
		double result=priceTotal;
		//得到 当前价单 的 税金集合
		if(dataUtil.getTaxPriceMap().containsKey(guestPriceBean.getHotelPlUuid())&&guestPriceBean.getRoomAmount()!=null){
			List<HotelPlTaxPrice> taxList = dataUtil.getTaxPriceMap().get(guestPriceBean.getHotelPlUuid());
			
			//按税金的类型进行分组 1、政府税；2、服务费；3、床税；4、其他
			Map<Object, HotelPlTaxPrice> taxMap = BeanUtil.groupByKeyString2Obj("taxType", taxList);
			
//			
			//当前优惠信息的 税金设置
			Map<String, List<HotelPlPreferentialTax>> taxs = pre.getMatter().getPreferentialTaxMap();
			if(MapUtils.isNotEmpty(taxs)){
				List<HotelPlPreferentialTax> list = taxs.get(HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_4+"");
				if(CollectionUtils.isNotEmpty(list)){
					for(HotelPlPreferentialTax tax:list){
						
						//打包价格时的 税金 设置，游客类型为null
						if(tax.getTravelerTypeUuid()==null){
							String isTaxs = tax.getIstax();
							if(StringUtils.isNotBlank(isTaxs)){
								
								//多个税金类型的分隔
								String[] array = isTaxs.split(HotelPlTaxException.REGEX);
								for(String isTax:array){
									if(taxMap.containsKey(Integer.parseInt(isTax))){
										HotelPlTaxPrice price = taxMap.get(Integer.parseInt(isTax));
										//根据价单的税金字典设置 计算 税金
										if(price.getChargeType()!=null && price.getChargeType()==1){//百分比收税
											//result += priceTotal*price.getAmount()/100;
											result = getTaxAmount4Type(result, priceTotal, price.getAmount(),dataUtil,price.getHotelPlUuid());
										}else if(price.getChargeType()!=null && price.getChargeType()==2){//固定的金额收税
											result += price.getAmount()*bednum;
										}
									}
								}
							}
						}
						break;
					}
				}
			}
			
		}
		//税金的汇率转换
		result = tranfer2RMB4exchangeRate(result, dataUtil.getHotelPlMap().get(pre.getHotelPlUuid()).getCurrencyId().toString(),dataUtil);
		return result;
	}
	
	/**
	 * 不同费用类型（房型、餐费、交通）的税金处理，如果需要缴税则会把税后的价格返回否则返回原价格 add by zhanghao
	 * @param amount
	 * @param hotelPlTaxExceptionQuery
	 * @return
	 */
	private double priceTaxex(double amount,HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery,AutoQuotedPriceDataUtil dataUtil){
		return priceTaxex(amount, hotelPlTaxExceptionQuery, 1,dataUtil);
	}
	private double priceTaxex(double amount,HotelPlTaxExceptionQuery hotelPlTaxExceptionQuery,Integer bedTaxNum,AutoQuotedPriceDataUtil dataUtil){
		
		List<HotelPlTaxException> list = hotelPlTaxExceptionService.getHotelPlTaxException4AutoQuotedPrice(hotelPlTaxExceptionQuery);
		
		//缴税基数
		double baseAmount = amount;
		
		//没有添加的例外的税金处理
		if(CollectionUtils.isNotEmpty(list)&&list.size()>0){
			HotelPlTaxException taxException = list.get(0);
			String taxTypes = taxException.getTaxType();
			if(StringUtils.isNotBlank(taxTypes)){
				for(String tt: taxTypes.split(HotelPlTaxException.REGEX)){
					HotelPlTaxPriceQuery hotelPlTaxPriceQuery = new HotelPlTaxPriceQuery();
					hotelPlTaxPriceQuery.setHotelPlUuid(hotelPlTaxExceptionQuery.getHotelPlUuid());
					hotelPlTaxPriceQuery.setStartDate(hotelPlTaxExceptionQuery.getStartDate());
					hotelPlTaxPriceQuery.setEndDate(hotelPlTaxExceptionQuery.getStartDate());
					hotelPlTaxPriceQuery.setTaxType(Integer.parseInt(tt));
					
					//查询税金字典设置 
					List<HotelPlTaxPrice> priceList = hotelPlTaxPriceService.getHotelPlTaxPrice4AutoQuotedPrice(hotelPlTaxPriceQuery);
					
					//存在税金字典设置---对金额进行税金处理
					if(CollectionUtils.isNotEmpty(priceList)&&priceList.size()>0){
						HotelPlTaxPrice price = priceList.get(0);
						
						if(price.getAmount()==null){
							String message = "当前价单税率存在空值！";
							if(dataUtil.getMessage().indexOf(message)==-1){
								dataUtil.putMessage(message);
							}
							continue;
						}
						if(price.getChargeType()!=null && price.getChargeType()==1){//百分比收税
//							amount+=baseAmount*price.getAmount()/100;
							amount=getTaxAmount4Type(amount, baseAmount, price.getAmount(),dataUtil,price.getHotelPlUuid());
						}else if(price.getChargeType()!=null && price.getChargeType()==2){//固定的金额收税
							amount+=price.getAmount()*bedTaxNum;
						}
						
					}
					
				}
			}
		}
		return amount;
	}
	
	/**
	 * 根据当前税率计算方法计算税率（分乘或者连乘）
	 * 默认计算税率时床税是最后一个，价单保存时顺序不要改变
	 * 否则计算时的税率可能错误。
	 * add by zhanghao
	 * @param amount 税后的金额
	 * @param baseAmount 税前的金额
	 * @param rate 税率
	 * @param dataUtil
	 * @param hotelPlUuid 当前价单UUID
	 * @return
	 */
	private double getTaxAmount4Type(double amount,double baseAmount,Double rate,AutoQuotedPriceDataUtil dataUtil,String hotelPlUuid){
		HotelPl hp = dataUtil.getHotelPlMap().get(hotelPlUuid);
		if(rate==null){
			if(MapUtils.isNotEmpty(dataUtil.getHotelPlMap())&&dataUtil.getHotelPlMap().containsKey(hotelPlUuid)){
				
				String message = "当前价单\""+hp.getName()+"\"税率不存在，当前价格不含税，请维护税率！";
				if(dataUtil.getMessage().indexOf(message)==-1){
					dataUtil.putMessage(message);
				}
			}
			return amount;
		}
		int type = 0;
		/**设置计算税率的公式类型；1分乘2连乘*/
		if(hp.getTaxArithmetic()!=null){
			type = hp.getTaxArithmetic();
		}
		
		if(amount==0){
			amount=baseAmount;
		}
		if(baseAmount==0){
			baseAmount=amount;
		}
		if(type==1){//分乘--即：在原价格的基础上计算税金
			amount += baseAmount*rate/100;
		}else if(type==2){//连乘--即：在最新的价格上计算税金
			amount += amount*rate/100;
		}else{//默认连乘
			amount += amount*rate/100;
			String message = "当前价单\""+hp.getName()+"\"税金算法没有维护，当前价格按默认“连乘”算法计算！";
			if(dataUtil.getMessage().indexOf(message)==-1){
				dataUtil.putMessage(message);
			}
		}
		
		
		return amount;
	}


	/**
	 * 判断 报价条件中的基础餐型（或者升级餐型）和交通方式是否在优惠信息中配置，如果配置过可以使用优惠，否则不进行计算
	 * @param taxType
	 * @param dataUtil
	 * @param tax
	 * @return
	 */
	private boolean checkPreUsed4Meal(String taxType,AutoQuotedPriceDataUtil dataUtil,HotelPlPreferentialTax tax){
		boolean mealUsed = false;
		if(Integer.parseInt(taxType)==HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_2){
			if(CollectionUtils.isNotEmpty(dataUtil.getMealSet())){
				for(String mealUuid:dataUtil.getMealSet()){
					if(tax.getHotelMealUuids().indexOf(mealUuid)>-1){
						mealUsed = true;
						break;
					}
				}
			}
		}
		return mealUsed;
	}
	/**
	 * 判断 报价条件中的基础餐型（或者升级餐型）和交通方式是否在优惠信息中配置，如果配置过可以使用优惠，否则不进行计算
	 * @param taxType
	 * @param dataUtil
	 * @param tax
	 * @return
	 */
	private boolean checkPreUsed4Islandway(String taxType,AutoQuotedPriceDataUtil dataUtil,HotelPlPreferentialTax tax){
		boolean islandUsed = false;
		if(Integer.parseInt(taxType)==HotelPlPreferentialTax.PREFERENTIAL_TAX_TYPE_3){
			if(CollectionUtils.isNotEmpty(dataUtil.getIslandSet())){
				for(String islandway:dataUtil.getIslandSet()){
					if(tax.getIslandWayUuids().indexOf(islandway)>-1){
						islandUsed = true;
						break;
					}
				}
			}
		}
		return islandUsed;
	}
	
}
