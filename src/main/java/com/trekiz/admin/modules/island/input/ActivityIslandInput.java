/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.input;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMealRise;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;
import com.trekiz.admin.modules.island.entity.ActivityIslandShare;
import com.trekiz.admin.modules.island.entity.ActivityIslandVisaFile;
import com.trekiz.admin.modules.island.util.StringUtil;
/**
 * @author LiuXueLiang
 */
public class ActivityIslandInput implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private java.lang.String uuid;// "唯一性标识uuid"
	private java.lang.String activitySerNum;// "产品编号,如SG0001"
	private java.lang.String activityName;	// "海岛游产品的名称"
	private java.lang.String country;// "国家"
	private java.lang.String islandUuid;// "海岛"
	private java.lang.String hotelUuid;// "酒店"
	private java.lang.Integer currencyId;// "币种"
	private java.lang.String memo;// "备注"
	
	private ActivityIsland dataObj ;
	
	private String[] groupCodes;//团期编号
	private String[] groupOpenDates;	//出团日期
	private String[] roomsNights; //新增格式 [房型-晚数;房型-晚数]  修改格式[UUid-房型-晚数;UUid-房型-晚数]
	//新增： [基础餐型-升级餐型@币种@价格#升级餐型@币种@价格;...] 修改[UUid@基础餐型-UUid@升级餐型@币种@价格#UUid升级餐型@币种@价格;...]
	private String[] islandGroupMeals;
	private String[] islandWays; // 上岛方式[上岛方式；上岛方式]
	//新增 [关联航班-航班号-出发时间-到达时间-天数-舱位等级-控票数量-非控票数量-余位-price;...]修改[UUid-联航班-航班号-出发时间-到达时间-天数-舱位等级-控票数量-非控票数量-余位;...]
	private String[] islandGroupAirlines;
	 //新增[游客类型@币种@价格-游客类型@币种@价格；游客类型@币种@价格-游客类型@币种@价格]
	private String[] islandGroupPrices;
	private String[] advNumbers;//预收人数
	private String[] currencyIds; //单房差币种
	private String[] singlePrices; // 单房差
	private String[] singlePriceUnits;//单房差单位
	private String[] frontMoneyCurrencyIds; // 定金币种
	private String[] frontMoneys; // 定金
	private String[] priorityDeductions;//优先扣减
	private String[] memos;//团期表备注
	//修改时使用
	private String[] islandGroupUuids;  //海岛游产品团期表  2级明细
	private String[] islandGroupAirlineUuids;  //海岛游产品参考航班表  3级明细
	private String[] islandGroupRoomUuids;//海岛游3级明细  存放的是3级明细下所有的uuid
	private String[] islandGroupPriceUuids;//海岛游3级明细（海岛游产品团期价格表）
	private String[] islandGroupMealUuids;//海岛游4级明细（海岛游产品基础餐型表）绑定在room表上
	private String[] islandGroupMealRiseUuids;//海岛游5级明细（海岛游产品团期基础餐型升餐表）
	private String[] shareUser;//产品分享用户
	private String[] visaCountry;//签证国家
	private String[] visaType;//签证类型
	//海岛游产品团期表
	private List<ActivityIslandGroup> activityIslandGroupLists;
	//海岛游产品上传签证资料表
	private List<ActivityIslandVisaFile> activityVisaFileLists;
	//附件信息
	List<HotelAnnex> prodSchList = null;
	List<HotelAnnex> costProtocolList = null;
	List<HotelAnnex> otherProtocolList = null;
	List[] eachVisaFileList = null;
	
	public ActivityIslandInput() {
	}

	// 数据库映射bean转换成表单提交bean
	public ActivityIslandInput(ActivityIsland obj) {
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public ActivityIsland getActivityIsland() {
		dataObj = new ActivityIsland();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		
		return dataObj;
	}

	 // 数据库中读取并加载库存数据
	public void initActivityIslandInput(ActivityIsland activityIsland){
		this.setUuid(activityIsland.getUuid());
		this.setActivitySerNum(activityIsland.getActivitySerNum());
		this.setActivityName(activityIsland.getActivityName());
		this.setCountry(activityIsland.getCountry());
		this.setIslandUuid(activityIsland.getIslandUuid());
		this.setHotelUuid(activityIsland.getHotelUuid());
		this.setCurrencyId(activityIsland.getCurrencyId());
		this.setMemo(activityIsland.getMemo());
		this.setActivityIslandGroupLists(activityIsland.getActivityIslandGroupList());
	}	
	 /***
	  * (一级表)装载ActivityIsland
	  * @param flag="update" 更新   其他为新增
	  * @author LiuXueLiang
	  * @return activityisland
	  */
	public ActivityIsland transfer2ActivityIsland(String flag){
		if(! validateFormInput()){
			return null;
		}
		ActivityIsland activityisland = new ActivityIsland();
		activityisland.setUuid(uuid);
		activityisland.setActivitySerNum(activitySerNum);
		activityisland.setActivityName(activityName);
		activityisland.setCountry(country);
		activityisland.setIslandUuid(islandUuid);
		activityisland.setHotelUuid(hotelUuid);
		activityisland.setCurrencyId(currencyId);
		activityisland.setMemo(memo);
		activityisland.setActivityIslandGroupList(this.transfer2ActivityIslandGroup(flag));//装载二级表
		activityisland.setActivityIslandVisaFile(this.transfer2ActivityIslandVisaFile(uuid,flag));//装载二级表
		return activityisland;
	}
	/***
	  * 仅仅装载ActivityIsland，只是装载一级表，二级及以上的不用管
	  * @param flag="update" 
	  * @author wangXK
	  * @return activityisland
	  */
	public ActivityIsland getActivityIslandFromUpdate(String flag){
		if(! validateFormInput()){
			return null;
		}
		ActivityIsland activityisland = new ActivityIsland();
		activityisland.setUuid(uuid);
		activityisland.setActivitySerNum(activitySerNum);
		activityisland.setActivityName(activityName);
		activityisland.setMemo(memo);
		return activityisland;
	}
	/***
	 * (二级表)装载ActivityIslandGroup flag='update' 更新  否则新增
	 * @author LiuXueLiang 
	 * @return list
	 */
	public List<ActivityIslandGroup> transfer2ActivityIslandGroup(String flag) {
		if(!validateFormInput()) {
			return null;
		}
		ActivityIslandGroup islandGroup = null;
		List<ActivityIslandGroup> list = new ArrayList<ActivityIslandGroup>();
		try {
			if(groupOpenDates!=null && groupOpenDates.length>0){
				for(int i=0;i<groupOpenDates.length;i++){
					islandGroup = new ActivityIslandGroup();
					if(islandGroupUuids.length>i && StringUtil.isNotBlank(islandGroupUuids[i])){//用于修改时，UUid不为空，需要赋值
						islandGroup.setGroupCode(islandGroupUuids[i]);
					}
					if(groupCodes.length>i && StringUtil.isNotBlank(groupCodes[i])){//团号
						islandGroup.setGroupCode(groupCodes[i]);
					}
					if(groupOpenDates.length>i && StringUtil.isNotBlank(groupOpenDates[i])){//日期
						islandGroup.setGroupOpenDate(DateUtils.string2Date(groupOpenDates[i]));
					}
					//新增格式：[房型@晚数-基础餐型-升级餐型@币种@价格#升级餐型@币种@价格;房型@晚数-基础餐型-升级餐型@币种@价格#升级餐型@币种@价格]
					//修改格式：[Uuid@房型@晚数-Uuid@基础餐型-uuid@升级餐型@币种@价格#升级餐型@币种@价格;Uuid@房型@晚数-Uuid@基础餐型-uuid@升级餐型@币种@价格#升级餐型@币种@价格]
					if(roomsNights.length>i && StringUtil.isNotBlank(roomsNights[i])){
						islandGroup.setActivityIslandGroupRoomList(this.transfer2ActivityIslandGroupRoom(roomsNights[i],flag));	
					}
					
					if (islandWays.length>i && StringUtil.isNotBlank(islandWays[i])) {//上岛方式
						islandGroup.setIslandWay(islandWays[i]);
					}
					if(islandGroupAirlines.length>i && StringUtil.isNotBlank(islandGroupAirlines[i])){//航班相关，格式：[关联航班-航班号-出发时间-到达时间-天数-舱位等级-控票数量-非控票数量-余位;关联航班-航班号-出发时间-到达时间-天数-舱位等级-控票数量-非控票数量-余位]
						islandGroup.setActivityIslandGroupAirlineList(this.transfer2ActivityIslandGroupAirline(islandGroupAirlines[i],groupOpenDates[i],flag));
					}
//					if (StringUtil.isNotBlank(islandGroupPrices[i])){//价格相关,格式：[游客类型@币种@价格-游客类型@币种@价格；游客类型@币种@价格-游客类型@币种@价格]
//						islandGroup.setActivityIslandGroupPriceList(this.transfer2ActivityIslandGroupPrice(islandGroupPrices[i]));
//					}
					if (advNumbers.length>i && StringUtil.isNotBlank(advNumbers[i])) {//预收人数
						islandGroup.setAdvNumber(Integer.parseInt(advNumbers[i]));
					}
					if (currencyIds.length>i && StringUtil.isNotBlank(currencyIds[i])) {//单房差币种
						islandGroup.setCurrencyId(Integer.parseInt(currencyIds[i]));
					}
					if (singlePrices.length>i && StringUtil.isNotBlank(singlePrices[i])) {//单房差
						islandGroup.setSinglePrice((Double.parseDouble(singlePrices[i])));
					}
					if (singlePriceUnits.length>i && StringUtil.isNotBlank(singlePriceUnits[i])){//单房差单位
						islandGroup.setSinglePriceUnit(Integer.parseInt(singlePriceUnits[i]));
					}
					if (frontMoneyCurrencyIds.length>i && StringUtil.isNotBlank(frontMoneyCurrencyIds[i])) {//定金币种
						islandGroup.setFrontMoneyCurrencyId(Integer.parseInt(frontMoneyCurrencyIds[i]));
					}
					if (frontMoneys.length>i && StringUtil.isNotBlank(frontMoneys[i])) {//定金
						islandGroup.setFrontMoney(Double.parseDouble(frontMoneys[i]));
					}
					if (priorityDeductions.length>i && StringUtil.isNotBlank(priorityDeductions[i])){//优先扣减
						islandGroup.setPriorityDeduction(Integer.parseInt(priorityDeductions[i]));
					}
					if (memos.length>i && StringUtil.isNotBlank(memos[i])){//备注
						islandGroup.setMemo(memos[i]);
					}
					list.add(islandGroup);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}


	/***
	 * （三级表/房型@晚数）装载ActivityIslandGroupRoom
	 * @author LiuXueLiang
	 * 添加格式：[房型@晚数-基础餐型-升级餐型@币种@价格#升级餐型@币种@价格;房型@晚数-基础餐型-升级餐型@币种@价格#升级餐型@币种@价格]
	 * 修改格式：[Uuid@房型@晚数-Uuid@基础餐型-uuid@升级餐型@币种@价格#升级餐型@币种@价格;Uuid@房型@晚数-Uuid@基础餐型-uuid@升级餐型@币种@价格#升级餐型@币种@价格]
	 * @param roomsNightsFoods
	 * @return list  update 
	 */
	public List<ActivityIslandGroupRoom> transfer2ActivityIslandGroupRoom(String roomsNight,String flag) {
		if(StringUtil.isEmpty(roomsNight)){
			return null;
		}
		ActivityIslandGroupRoom activityIslandGroupRoom = null;
		ActivityIslandGroupMeal groupMeal = null;
		List<ActivityIslandGroupRoom> list = new ArrayList<ActivityIslandGroupRoom>();
		if("update".equals(flag)){//更新
			String[] ss = roomsNight.split(";");//[单一的一个拼接 ] Uuid@房型@晚数-Uuid@基础餐型-uuid@升级餐型@币种@价格#升级餐型@币种@价格
			for(int i=0;i<ss.length;i++){
				if(StringUtil.isNotBlank(ss[i])){
					String[] strs = ss[i].split("-");
					
					if(strs.length>0 && StringUtil.isNotBlank(strs[0])){
						activityIslandGroupRoom = new ActivityIslandGroupRoom();
						String[] roomstrs = strs[0].split("@");
						if(roomstrs.length>0 && StringUtil.isNotBlank(roomstrs[0])){//UUid
							activityIslandGroupRoom.setUuid(roomstrs[0]);	
						}
						if(roomstrs.length>1 && StringUtil.isNotBlank(roomstrs[1])){//房型
							activityIslandGroupRoom.setHotelRoomUuid(roomstrs[1]);	
						}
						if(roomstrs.length>2 && StringUtil.isNotBlank(roomstrs[2])){//晚数
							activityIslandGroupRoom.setNights(Integer.parseInt(roomstrs[2]));	
						}
					}
					if(strs.length>2 && StringUtil.isNotBlank(strs[1])&&StringUtil.isNotBlank(strs[2])){
						activityIslandGroupRoom.setActivityIslandGroupMealList(transfer2ActivityIslandGroupMeal(strs[1],strs[2],"update"));
					}
					list.add(activityIslandGroupRoom);
				}
			}
			
		}else if(StringUtil.isNotBlank(roomsNight)){//新增  //[单一的一个拼接 ] 房型@晚数-基础餐型-升级餐型@币种@价格#升级餐型@币种@价格
			String[] ss = roomsNight.split(";");//
			for(int i=0;i<ss.length;i++){
				if(StringUtil.isNotBlank(ss[i])){
					activityIslandGroupRoom = new ActivityIslandGroupRoom();
					String[] strs = ss[i].split("-");//分三级
					if(strs.length>0 && StringUtil.isNotBlank(strs[0])){
						String[] roomstrs = strs[0].split("@");
						if(roomstrs.length>1 && StringUtil.isNotBlank(roomstrs[0])){
							activityIslandGroupRoom.setHotelRoomUuid(roomstrs[0]);	
						}
						if(roomstrs.length>2 && StringUtil.isNotBlank(roomstrs[1])){
							activityIslandGroupRoom.setNights(Integer.parseInt(roomstrs[1]));	
						}
					}
					if(strs.length>2 && StringUtil.isNotBlank(strs[1])&&StringUtil.isNotBlank(strs[2])){
						activityIslandGroupRoom.setActivityIslandGroupMealList(transfer2ActivityIslandGroupMeal(strs[1],strs[2],"add"));
					}
					
					list.add(activityIslandGroupRoom);
				}
			}
		}
		return list;
	}
	
	
	
	/***
	 * (四级表/基础餐型)装载ActivityIslandGroupMeal
	 * @author LiuXueLiang
	 * @param islandGroupMeal(新增格式：islandGroupMeal 基础餐型,islandGroupRiseMeal 升级餐型@币种@价格#升级餐型@币种@价格;基础餐型-升级餐型@币种@价格#升级餐型@币种@价格)
	 * @param flag (餐型相关，修改格式：[uuid@基础餐型-uuid@升级餐型@币种@价格#uuid@升级餐型@币种@价格;...])
	 * @return
	 */
	public List<ActivityIslandGroupMeal> transfer2ActivityIslandGroupMeal(String islandGroupMeal,String islandGroupRiseMeal,String flag) {
		if(StringUtil.isBlank(islandGroupMeal)||StringUtil.isBlank(islandGroupRiseMeal)){
			return null;
		}
		ActivityIslandGroupMeal activityIslandGroupMeal = new ActivityIslandGroupMeal();
		List <ActivityIslandGroupMeal> list  = new ArrayList<ActivityIslandGroupMeal>();
		
		if("update".equals(flag)){//修改
			String[] ss = islandGroupMeal.split("@");//uuid@基础餐型;uuid@升级餐型@币种@价格#uuid@升级餐型@币种@价格;
			for(int i=0;i<ss.length;i++){ 
				activityIslandGroupMeal = new ActivityIslandGroupMeal();
				String[] strs = ss[i].split("-");
				if(strs.length>0 && StringUtil.isNotBlank(strs[0])){
					activityIslandGroupMeal.setUuid(strs[0]);//Uuid
				}
				if(strs.length>1 && StringUtil.isNotBlank(strs[1])){
					activityIslandGroupMeal.setHotelMealUuid(strs[1]);//基础餐型
				}
			}
			
			if(StringUtil.isNotBlank(islandGroupMeal) && StringUtil.isNotBlank(islandGroupRiseMeal)){
				activityIslandGroupMeal.setActivityIslandGroupMealRiseList(this.transfer2ActivityIslandGroupMealRise(islandGroupRiseMeal,flag));//升级餐型，格式：升级餐型@币种@价格#升级餐型@币种@价格
			}
			list.add(activityIslandGroupMeal);

			
		}else{//ss[i]格式：基础餐型-uuid@升级餐型@币种@价格#升级餐型@币种@价格
				activityIslandGroupMeal = new ActivityIslandGroupMeal();
				
				if(StringUtil.isNotBlank(islandGroupMeal)){
					activityIslandGroupMeal.setHotelMealUuid(islandGroupMeal);//基础餐型
				}
				if(StringUtil.isNotBlank(islandGroupMeal) && StringUtil.isNotBlank(islandGroupRiseMeal)){
					activityIslandGroupMeal.setActivityIslandGroupMealRiseList(this.transfer2ActivityIslandGroupMealRise(islandGroupRiseMeal,"add"));//升级餐型，格式：升级餐型@币种@价格#升级餐型@币种@价格
				}
				list.add(activityIslandGroupMeal);
		}
		
		return list;
	}
	/**
	 * （五级表/升级餐型）装再ActivityIslandGroupMealRise
	 * @author LiuXueLiang  
	 * @param islandGroupMealRise(格式：  升级餐型@币种@价格#升级餐型@币种@价格)
	 * @param flag 修改  Uuid@升级餐型@币种@价格#升级餐型@币种@价格#Uuid@升级餐型@币种@价格#升级餐型@币种@价格
	 * @return
	 */
	public List<ActivityIslandGroupMealRise> transfer2ActivityIslandGroupMealRise(String islandGroupMealRise,String flag){
		if(StringUtil.isBlank(islandGroupMealRise)){
			return null;
		}
		ActivityIslandGroupMealRise activityIslandGroupMealRise = null;
		List<ActivityIslandGroupMealRise> list = new ArrayList<ActivityIslandGroupMealRise>();
		String[] ss = islandGroupMealRise.split("#");//[uuid@升级餐型@币种@价格] 
		if("update".equals(flag)){
			for(int i=0;i<ss.length;i++){
				activityIslandGroupMealRise = new ActivityIslandGroupMealRise();
				if(StringUtil.isNotBlank(ss[i])){
					String[] strs = ss[i].split("@");
					if(strs.length>0 && StringUtil.isNotBlank(strs[0])){
						activityIslandGroupMealRise.setUuid(strs[0]);//UUid	
					}
					if(strs.length>1 && StringUtil.isNotBlank(strs[1])){
						activityIslandGroupMealRise.setHotelMealUuid(strs[1]);//升级餐型	
					}
					if(strs.length>2 && StringUtil.isNotBlank(strs[2])){
						activityIslandGroupMealRise.setCurrencyId(Integer.parseInt(strs[2]));//币种	
					}
					if(strs.length>3 && StringUtil.isNotBlank(strs[3])){
						activityIslandGroupMealRise.setPrice(Double.parseDouble(strs[3]));//价格	
					}
					list.add(activityIslandGroupMealRise);
				}
			}
		}else{
			for(int i=0;i<ss.length;i++){
				activityIslandGroupMealRise = new ActivityIslandGroupMealRise();
				if(StringUtil.isNotBlank(ss[i])){
					String[] strs = ss[i].split("@");
					if(strs.length>0 && StringUtil.isNotBlank(strs[0])){
						activityIslandGroupMealRise.setHotelMealUuid(strs[0]);//升级餐型	
					}
					if(strs.length>1 && StringUtil.isNotBlank(strs[1])){
						activityIslandGroupMealRise.setCurrencyId(Integer.parseInt(strs[1]));//币种	
					}
					if(strs.length>2 && StringUtil.isNotBlank(strs[2])){
						activityIslandGroupMealRise.setPrice(Double.parseDouble(strs[2]));//价格	
					}
					list.add(activityIslandGroupMealRise);
				}
			}
		}
		
		return list;
	}
	/***
	 * (三级表/参考航班)装载ActivityIslandGroupAirline
	 * @author LiuXueLiang
	 * @param airline(格式：关联航班-航班号-出发时间-到达时间-天数-舱位等级-控票数量-非控票数量-余位-游客类型@币种@价格#游客类型@币种@价格;...)
	 * @param flag (//航班相关，格式：[UUid-关联航班-航班号-出发时间-到达时间-天数-舱位等级-控票数量-非控票数量-余位-UUid@游客类型@币种@价格#游客类型@币种@价格;...])
	 * @return list
	 */
	public List<ActivityIslandGroupAirline> transfer2ActivityIslandGroupAirline(String airline,String openDate,String flag) {
		if(StringUtil.isBlank(airline)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		ActivityIslandGroupAirline activityIslandGroupAirline = null;
		List <ActivityIslandGroupAirline> list  = new ArrayList<ActivityIslandGroupAirline>();
		String[] ss = airline.split(";");//[关联航班-航班号-出发时间-到达时间-天数-舱位等级-控票数量-非控票数量-余位-price]
		if("update".equals(flag)){  //[UUid-关联航班-航班号-出发时间-到达时间-天数-舱位等级-控票数量-非控票数量-余位-UUid@游客类型@币种@价格#UUid@游客类型@币种@价格;...])
			for(int i=0;i<ss.length;i++){
				activityIslandGroupAirline = new ActivityIslandGroupAirline();
				String[] strs = ss[i].split("-");
				if(strs.length>0 && StringUtil.isNotBlank(strs[0])){//UUid
					activityIslandGroupAirline.setUuid(strs[0]);
				}
				if(strs.length>1 && StringUtil.isNotBlank(strs[1])){//关联航班
					activityIslandGroupAirline.setAirline(strs[1]);
				}
				if(strs.length>2 && StringUtil.isNotBlank(strs[2])){//航班号
					activityIslandGroupAirline.setFlightNumber(strs[2]);
				}
				if(strs.length>3 && StringUtil.isNotBlank(strs[3])){//出发时间
					try {
							if(StringUtil.isNotBlank(openDate)){
								String depTimeStr = openDate+" "+strs[3];	
								activityIslandGroupAirline.setDepartureTime(sdf.parse(depTimeStr));
							}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(strs.length>4 && StringUtil.isNotBlank(strs[4]) && StringUtil.isNotBlank(strs[5])){//到达时间
					try {
						if(StringUtil.isNotBlank(openDate)){//日期
							String arrTimeStr = openDate+" "+strs[4];//开始时间+日期即到达日期
							Date arrDate = sdf.parse(arrTimeStr);
							Long aa = arrDate.getTime();
							Integer dayNum = Integer.parseInt(strs[5]);//天数
							Long bb = (long) (dayNum*1000*60*60*24);
							Date arrTime = new Date(aa+bb);
							activityIslandGroupAirline.setArriveTime(arrTime);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(strs.length>5 && StringUtil.isNotBlank(strs[5])){//天数
					activityIslandGroupAirline.setDayNum(Integer.parseInt(strs[4]));
				}
				if(strs.length>6 && StringUtil.isNotBlank(strs[6])){//舱位等级
					activityIslandGroupAirline.setSpaceLevel(strs[6]);
				}
				if(strs.length>7 && StringUtil.isNotBlank(strs[7])){//控票数量
					activityIslandGroupAirline.setControlNum(Integer.parseInt(strs[7]));
				}
				if(strs.length>8 && StringUtil.isNotBlank(strs[8])){//非控票数量
					activityIslandGroupAirline.setUncontrolNum(Integer.parseInt(strs[8]));
				}
				if(strs.length>9 && StringUtil.isNotBlank(strs[9])){//余位
					activityIslandGroupAirline.setRemNumber(Integer.parseInt(strs[9]));
				}
				if(strs.length>10 && StringUtil.isNotBlank(strs[10])){//价格
					if (StringUtil.isNotBlank(strs[10])){//价格相关,格式：[UUid@游客类型@币种@价格#UUid@游客类型@币种@价格;UUid@游客类型@币种@价格#UUid@游客类型@币种@价格]
						activityIslandGroupAirline.setActivityIslandGroupPriceList(this.transfer2ActivityIslandGroupPrice(strs[10],flag));
					}
				}
				list.add(activityIslandGroupAirline);
			}
		}else{
			for(int i=0;i<ss.length;i++){
				activityIslandGroupAirline = new ActivityIslandGroupAirline();
				String[] strs = ss[i].split("-");
				if(strs.length>0 && StringUtil.isNotBlank(strs[0])){//关联航班
					activityIslandGroupAirline.setAirline(strs[0]);
				}
				if(strs.length>1 && StringUtil.isNotBlank(strs[1])){//有 航班号
					activityIslandGroupAirline.setFlightNumber(strs[1]);
				}
				if(strs.length>2 && StringUtil.isNotBlank(strs[2])){//出发时间
					try {
							if(StringUtil.isNotBlank(openDate)){
								String depTimeStr = openDate+" "+strs[2];	
								activityIslandGroupAirline.setDepartureTime(sdf.parse(depTimeStr));
							}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(strs.length>4 && StringUtil.isNotBlank(strs[3]) && StringUtil.isNotBlank(strs[4])){//到达时间
					try {
						if(StringUtil.isNotBlank(openDate)){//日期
							String arrTimeStr = openDate+" "+strs[3];
							Date arrDate = sdf.parse(arrTimeStr);
							Long aa = arrDate.getTime();
							Integer dayNum = Integer.parseInt(strs[4]);
							Long bb = (long) (dayNum*1000*60*60*24);
							Date arrTime = new Date(aa+bb);
							activityIslandGroupAirline.setArriveTime(arrTime);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(strs.length>4 && StringUtil.isNotBlank(strs[4])){//天数
					activityIslandGroupAirline.setDayNum(Integer.parseInt(strs[4]));
				}
				if(strs.length>5 && StringUtil.isNotBlank(strs[5])){//舱位等级
					activityIslandGroupAirline.setSpaceLevel(strs[5]);
				}
				if(strs.length>6 && StringUtil.isNotBlank(strs[6])){//控票数量
					activityIslandGroupAirline.setControlNum(Integer.parseInt(strs[6]));
				}
				if(strs.length>7 && StringUtil.isNotBlank(strs[7])){//非控票数量
					activityIslandGroupAirline.setUncontrolNum(Integer.parseInt(strs[7]));
				}
				if(strs.length>8 && StringUtil.isNotBlank(strs[8])){//余位
					activityIslandGroupAirline.setRemNumber(Integer.parseInt(strs[8]));
				}
				if(strs.length>9 && StringUtil.isNotBlank(strs[9])){//价格
					if (StringUtil.isNotBlank(strs[9])){//价格相关,格式：[游客类型@币种@价格#游客类型@币种@价格;游客类型@币种@价格#游客类型@币种@价格]
						activityIslandGroupAirline.setActivityIslandGroupPriceList(this.transfer2ActivityIslandGroupPrice(strs[9],"add"));
					}
				}
				list.add(activityIslandGroupAirline);
			}
		}
		
		return list;
	}
	
	/***
	 * （三级表/同行价）装载ActivityIslandGroupPrice 
	 * @author LiuXueLiang
	 * @paramislandGroupPrice(格式：游客类型@币种@价格#游客类型@币种@价格)
	 * @param flag ((格式：UUid@游客类型@币种@价格#UUid@游客类型@币种@价格#UUid@游客类型@币种@价格#UUid@游客类型@币种@价格)
	 * @return list
	 */
	public List<ActivityIslandGroupPrice> transfer2ActivityIslandGroupPrice(String islandGroupPrice,String flag) {
		if(StringUtil.isBlank(islandGroupPrice)){
			return null;
		}
		ActivityIslandGroupPrice activityIslandGroupPrice = null;
		List <ActivityIslandGroupPrice> list  = new ArrayList<ActivityIslandGroupPrice>();
		String[] ss = islandGroupPrice.split("#");//[UUid@游客类型@币种@价格#]
		if("update".equals(flag)){
			for(int i=0;i<ss.length;i++){//游客类型@币种@价格
				if(ss.length>i && StringUtil.isNotBlank(ss[i])){
					String[] strs = ss[i].split("@");
					activityIslandGroupPrice = new ActivityIslandGroupPrice();
					
					if(strs.length>0 && StringUtil.isNotBlank(strs[0])){
						activityIslandGroupPrice.setUuid(strs[0]);
					}
					if(strs.length>1 && StringUtil.isNotBlank(strs[1])){
						activityIslandGroupPrice.setType(strs[1]);
					}
					if(strs.length>2 && StringUtil.isNotBlank(strs[2])){
						activityIslandGroupPrice.setCurrencyId(Integer.parseInt(strs[2]));
					}
					if(strs.length>3 && StringUtil.isNotBlank(strs[3])){
						activityIslandGroupPrice.setPrice(Double.parseDouble(strs[3]));
					}
					list.add(activityIslandGroupPrice);
				}
			}
		}else{
			for(int i=0;i<ss.length;i++){//游客类型@币种@价格
				if(ss.length>i && StringUtil.isNotBlank(ss[i])){
					String[] strs = ss[i].split("@");
					activityIslandGroupPrice = new ActivityIslandGroupPrice();
					
					if(strs.length>0 && StringUtil.isNotBlank(strs[0])){
						activityIslandGroupPrice.setType(strs[0]);
					}
					if(strs.length>1 && StringUtil.isNotBlank(strs[1])){
						activityIslandGroupPrice.setCurrencyId(Integer.parseInt(strs[1]));
					}
					if(strs.length>2 && StringUtil.isNotBlank(strs[2])){
						activityIslandGroupPrice.setPrice(Double.parseDouble(strs[2]));
					}
					list.add(activityIslandGroupPrice);
				}
			}
		}
		return list;
	}
	/**
	 * 
	 * @param flag
	 * @return
	 */
    public List<ActivityIslandVisaFile> transfer2ActivityIslandVisaFile(String ActivityIslandUuid,String flag){
    	if(visaCountry ==null || visaCountry.length<2){
    		return null;
    	}
    	List <ActivityIslandVisaFile> list  = new ArrayList<ActivityIslandVisaFile>();
    	for(int i=1;i<visaCountry.length;i++){
    		ActivityIslandVisaFile visaFile = new ActivityIslandVisaFile();
    		if("update".equals(flag) && ActivityIslandUuid!=null && !"".equals(ActivityIslandUuid)){
    			visaFile.setActivityIslandUuid(ActivityIslandUuid);
    		}
    		if(StringUtil.isNotBlank(visaCountry[i])){
    			visaFile.setCountry(visaCountry[i]);
    		}
    		if(StringUtil.isNotBlank(visaType[i])){
    			visaFile.setVisaTypeId(Integer.valueOf(visaType[i]));
    		}
    		list.add(visaFile);
    	}
    	return list;
    }
	/**
	 * 保存前的必填校验，返回false不能保存
	 * 
	 * @return
	 */
	public boolean validateFormInput() {
		/*boolean flag = true;
		if (flag) {
			return flag;
		}
		if (ArrayUtils.isEmpty(groupOpenDates)) {
			return false;
		}
		if (ArrayUtils.isEmpty(islandGroupAirlines)) {
			return false;
		}
		if (ArrayUtils.isEmpty(islandWays)) {
			return false;
		}
		if (ArrayUtils.isEmpty(advNumbers)) {
			return false;
		}
		if (ArrayUtils.isEmpty(singlePrices)) {
			return false;
		}
		if (ArrayUtils.isEmpty(frontMoneys)) {
			return false;
		}*/
		return true;
	}
	public String[] getIsLandWays() {
		return islandWays;
	}



	
	public String[] getGroupOpenDates() {
		return groupOpenDates;
	}
	public void setGroupOpenDates(String[] groupOpenDates) {
		this.groupOpenDates = groupOpenDates;
	}
	public void setIslandWays(String[] islandWays) {
		this.islandWays = islandWays;
	}

	public String[] getSinglePrices() {
		return singlePrices;
	}

	public void setSinglePrices(String[] singlePrices) {
		this.singlePrices = singlePrices;
	}

	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}

	public java.lang.String getUuid() {
		return this.uuid;
	}

	public void setActivitySerNum(java.lang.String value) {
		this.activitySerNum = value;
	}

	public java.lang.String getActivitySerNum() {
		return this.activitySerNum;
	}

	public void setActivityName(java.lang.String value) {
		this.activityName = value;
	}

	public java.lang.String getActivityName() {
		return this.activityName;
	}

	public void setCountry(java.lang.String value) {
		this.country = value;
	}

	public java.lang.String getCountry() {
		return this.country;
	}

	public void setIslandUuid(java.lang.String value) {
		this.islandUuid = value;
	}

	public java.lang.String getIslandUuid() {
		return this.islandUuid;
	}

	public void setHotelUuid(java.lang.String value) {
		this.hotelUuid = value;
	}

	public java.lang.String getHotelUuid() {
		return this.hotelUuid;
	}

	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}

	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setMemo(java.lang.String value) {
		this.memo = value;
	}

	public java.lang.String getMemo() {
		return this.memo;
	}

	public List<ActivityIslandGroup> getActivityIslandGroupLists() {
		return activityIslandGroupLists;
	}

	public void setActivityIslandGroupLists(List<ActivityIslandGroup> activityIslandGroupLists) {
		this.activityIslandGroupLists = activityIslandGroupLists;
	}
	
	public String[] getCurrencyIds() {
		return currencyIds;
	}
	
	public void setSingleCurrencyIds(String[] currencyIds) {
		this.currencyIds = currencyIds;
	}
	
	public String[] getFrontCurrencyIds() {
		return frontMoneyCurrencyIds;
	}
	
	public void setFrontCurrencyIds(String[] frontCurrencyIds) {
		this.frontMoneyCurrencyIds = frontCurrencyIds;
	}
	
	public String[] getFrontPrices() {
		return frontMoneys;
	}
	
	public void setFrontPrices(String[] frontPrices) {
		this.frontMoneys = frontPrices;
	}

	public String[] getIslandGroupAirlines() {
		return islandGroupAirlines;
	}
	public void setIslandGroupAirlines(String[] islandGroupAirlines) {
		this.islandGroupAirlines = islandGroupAirlines;
	}
	public String[] getIslandWays() {
		return islandWays;
	}
	public String[] getAdvNumbers() {
		return advNumbers;
	}
	public void setAdvNumbers(String[] advNumbers) {
		this.advNumbers = advNumbers;
	}
	public String[] getIslandGroupPrices() {
		return islandGroupPrices;
	}
	public void setIslandGroupPrices(String[] islandGroupPrices) {
		this.islandGroupPrices = islandGroupPrices;
	}
	public String[] getIslandGroupUuids() {
		return islandGroupUuids;
	}
	public void setIslandGroupUuids(String[] islandGroupUuids) {
		this.islandGroupUuids = islandGroupUuids;
	}
	public String[] getIslandGroupRoomUuids() {
		return islandGroupRoomUuids;
	}
	public void setIslandGroupRoomUuids(String[] islandGroupRoomUuids) {
		this.islandGroupRoomUuids = islandGroupRoomUuids;
	}
	public String[] getIslandGroupPriceUuids() {
		return islandGroupPriceUuids;
	}
	public void setIslandGroupPriceUuids(String[] islandGroupPriceUuids) {
		this.islandGroupPriceUuids = islandGroupPriceUuids;
	}

	public String[] getShareUser() {
		return shareUser;
	}
	public void setShareUser(String[] shareUser) {
		this.shareUser = shareUser;
	}
	public String[] getIslandGroupMeals() {
		return islandGroupMeals;
	}
	public void setIslandGroupMeals(String[] islandGroupMeals) {
		this.islandGroupMeals = islandGroupMeals;
	}
	public String[] getRoomsNights() {
		return roomsNights;
	}
	public void setRoomsNights(String[] roomsNights) {
		this.roomsNights = roomsNights;
	}
	public String[] getGroupCodes() {
		return groupCodes;
	}
	public void setGroupCodes(String[] groupCodes) {
		this.groupCodes = groupCodes;
	}
	public String[] getFrontMoneyCurrencyIds() {
		return frontMoneyCurrencyIds;
	}
	public void setFrontMoneyCurrencyIds(String[] frontMoneyCurrencyIds) {
		this.frontMoneyCurrencyIds = frontMoneyCurrencyIds;
	}
	public String[] getFrontMoneys() {
		return frontMoneys;
	}
	public void setFrontMoneys(String[] frontMoneys) {
		this.frontMoneys = frontMoneys;
	}
	public void setCurrencyIds(String[] currencyIds) {
		this.currencyIds = currencyIds;
	}
	public String[] getSinglePriceUnits() {
		return singlePriceUnits;
	}
	public void setSinglePriceUnits(String[] singlePriceUnits) {
		this.singlePriceUnits = singlePriceUnits;
	}
	public String[] getPriorityDeductions() {
		return priorityDeductions;
	}
	public void setPriorityDeductions(String[] priorityDeductions) {
		this.priorityDeductions = priorityDeductions;
	}
	public String[] getMemos() {
		return memos;
	}
	public void setMemos(String[] memos) {
		this.memos = memos;
	}
	public String[] getIslandGroupAirlineUuids() {
		return islandGroupAirlineUuids;
	}
	public void setIslandGroupAirlineUuids(String[] islandGroupAirlineUuids) {
		this.islandGroupAirlineUuids = islandGroupAirlineUuids;
	}
	
	public List<HotelAnnex> getProdSchList() {
		return prodSchList;
	}
	public void setProdSchList(List<HotelAnnex> prodSchList) {
		this.prodSchList = prodSchList;
	}
	public List<HotelAnnex> getCostProtocolList() {
		return costProtocolList;
	}
	public void setCostProtocolList(List<HotelAnnex> costProtocolList) {
		this.costProtocolList = costProtocolList;
	}
	public List<HotelAnnex> getOtherProtocolList() {
		return otherProtocolList;
	}
	public void setOtherProtocolList(List<HotelAnnex> otherProtocolList) {
		this.otherProtocolList = otherProtocolList;
	}
	public List[] getEachVisaFileList() {
		return eachVisaFileList;
	}
	public void setEachVisaFileList(List[] eachVisaFileList) {
		this.eachVisaFileList = eachVisaFileList;
	}
	public String[] getIslandGroupMealUuids() {
		return islandGroupMealUuids;
	}
	public void setIslandGroupMealUuids(String[] islandGroupMealUuids) {
		this.islandGroupMealUuids = islandGroupMealUuids;
	}
	public String[] getIslandGroupMealRiseUuids() {
		return islandGroupMealRiseUuids;
	}
	public void setIslandGroupMealRiseUuids(String[] islandGroupMealRiseUuids) {
		this.islandGroupMealRiseUuids = islandGroupMealRiseUuids;
	}
	public String[] getVisaCountry() {
		return visaCountry;
	}

	public void setVisaCountry(String[] visaCountry) {
		this.visaCountry = visaCountry;
	}
	public String[] getVisaType() {
		return visaType;
	}

	public void setVisaType(String[] visaType) {
		this.visaType = visaType;
	}

	public List<ActivityIslandVisaFile> getActivityVisaFileLists() {
		return activityVisaFileLists;
	}

	public void setActivityVisaFileLists(
			List<ActivityIslandVisaFile> activityVisaFileLists) {
		this.activityVisaFileLists = activityVisaFileLists;
	}
	/**
	 * 设置接收人
	 * @return
	 */
	public List<ActivityIslandShare> transfer2ActivityIslandShare() {
		if(!validateFormInput()){
			return null;
		}
		List<ActivityIslandShare> shareList=new ArrayList<ActivityIslandShare>();
		if(shareUser!=null&&shareUser.length>0){
			for(int i=0;i<shareUser.length;i++){
				ActivityIslandShare islandShare=new ActivityIslandShare();
				if(shareUser[i].contains("_")){
					continue;
				}
				islandShare.setAcceptShareUser(Long.parseLong(shareUser[i]));
				shareList.add(islandShare);
			}
		}
		return shareList;
	}
	
}
