/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupControlDetail;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelShare;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class ActivityHotelInput  extends BaseInput {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3493815646441297435L;
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
	//columns START
	//"唯一主键，自主递增"
	private java.lang.Integer id;
	//"唯一性标识uuid"
	private java.lang.String uuid;
	//"控房单号,如SG0001"
	private java.lang.String activitySerNum;
	//"酒店产品的名称"
	private java.lang.String activityName;
	//"国家"
	private java.lang.String country;
	//"海岛"
	private java.lang.String islandUuid;
	//"酒店"
	private java.lang.String hotelUuid;
	//"币种"
	private java.lang.Integer currencyId;
	//"备注"
	private java.lang.String memo;
	
	//columns END
	private ActivityHotel dataObj ;
	private java.lang.String wholesalerId;
	
	//团期表数据
	private String[] groupCodes;//团期编号
	private String[] groupOpenDates;//开团日期
	private String[] islandWays;//上岛方式
	private String[] singlePrices;//单房差
	private String[] currencyIds;//单房差币种
	private String[] singlePriceUnits;//单房差单位（系统常量：1人2间3晚）
	private String[] controlNums;//控房间数(选择关联控房单中的库存数量)
	private String[] uncontrolNums;//非控房间数
	private String[] remNumbers;//余位数（发布产品时余位等于控房和非控房之和）
	private String[] airlines;//参考航班（文本输入，不和航班有关联）
	private String[] priorityDeductions;//优先扣减（系统常量：1控票数2非控票数）
	private String[] frontMoneys;//定金
	private String[] frontMoneyCurrencyIds;//定金币种
	private String[] memos;//团期备注
	private String[] status;//1：上架；2：下架；3：草稿；4：已删除
	
	//产品分享
	private String[] shareUser;
	//房型&晚数 空数据请用 “_”占位
	private String[] hotelGroupRooms; //[房型-晚数;房型-晚数]
	//升级餐型 空数据请用 “_”占位
	private String[] hotelGroupMeals;//[基础餐型-升级餐型@币种@价格#升级餐型@币种@价格；基础餐型-升级餐型@币种@价格#升级餐型@币种@价格]
	//游客价格 空数据请用 “_”占位
	private String[] hotelGroupPrices; //[游客类型-币种-价格;游客类型-币种-价格]
	//酒店控房明细uuid&使用库存数
	private String[] hotelControlNums;//[uuid-num;uuid-num]
	
	//酒店产品团期明细
	private List<ActivityHotelGroup> activityhotelGroupLists;
	//附件信息
	List<HotelAnnex> prodSchList = null;
	List<HotelAnnex> costProtocolList = null;
	List<HotelAnnex> otherProtocolList = null;

	public ActivityHotelInput(){
	}
	//数据库映射bean转换成表单提交bean
	public ActivityHotelInput(ActivityHotel obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public ActivityHotel getActivityHotel() {
		dataObj = new ActivityHotel();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		//装载二级明细
		//dataObj.setActivityHotelGroupList(this.transfer2ActivityHotelGroup());
		//dataObj.setActivityHotelShareList(this.transfer2ActivityHotelShare());
		
		return dataObj;
	}
	
	
	public void initActivityHotelInput(ActivityHotel activityHotel){
		this.setUuid(activityHotel.getUuid());
		this.setActivitySerNum(activityHotel.getActivitySerNum());
		this.setActivityName(activityHotel.getActivityName());
		this.setCountry(activityHotel.getCountry());
		this.setIslandUuid(activityHotel.getIslandUuid());
		this.setHotelUuid(activityHotel.getHotelUuid());
		this.setCurrencyId(activityHotel.getCurrencyId());
		this.setMemo(activityHotel.getMemo());
		this.setActivityhotelGroupLists(activityHotel.getActivityHotelGroupList());
	}
	
	/**
	 * 分享信息 数据封装 update by zhanghao
	 * @return
	 */
	public List<ActivityHotelShare> transfer2ActivityHotelShare(){
		if(!validateFormInput()){
			return null;
		}
		List<ActivityHotelShare> list = new ArrayList<ActivityHotelShare>();
		if(shareUser!=null && shareUser.length>0){
			for(int i=0;i<shareUser.length;i++){
				ActivityHotelShare hotelShare = new ActivityHotelShare();
				if(StringUtils.isNotEmpty(shareUser[i])){
					if(shareUser[i].contains("_")){
						continue;
					}
					hotelShare.setAcceptShareUser(Long.parseLong(shareUser[i]));
				}	
				list.add(hotelShare);
			}
		}
		return list;
	}
	/**
	 * 团期信息的数据封装  update by zhanghao
	 * @return
	 */
	public List<ActivityHotelGroup> transfer2ActivityHotelGroup(){
		if(!validateFormInput()){
			return null;
		}
		List<ActivityHotelGroup> list = new ArrayList<ActivityHotelGroup>();
		try{
			if(groupOpenDates!=null && groupOpenDates.length>0){
				for(int i=0;i<groupOpenDates.length;i++){
					ActivityHotelGroup hotelGroup = new ActivityHotelGroup();
					if(StringUtils.isNotEmpty(groupCodes[i])){
						hotelGroup.setGroupCode(groupCodes[i]);
					}
					if(StringUtils.isNotEmpty(groupOpenDates[i])){
						hotelGroup.setGroupOpenDate(DateUtils.string2Date(groupOpenDates[i]));
					}
					if(StringUtils.isNotEmpty(islandWays[i])){
						hotelGroup.setIslandWay(islandWays[i]);
					}
					if(StringUtils.isNotEmpty(singlePrices[i])){
						hotelGroup.setSinglePrice(Double.parseDouble(singlePrices[i]));
					}
					if(StringUtils.isNotEmpty(currencyIds[i])){
						hotelGroup.setCurrencyId(Integer.parseInt(currencyIds[i]));
					}
					if(StringUtils.isNotEmpty(singlePriceUnits[i])){
						hotelGroup.setSinglePriceUnit(Integer.parseInt(singlePriceUnits[i]));
					}
					if(StringUtils.isNotEmpty(controlNums[i])){
						hotelGroup.setControlNum(Integer.parseInt(controlNums[i]));
					}
					if(StringUtils.isNotEmpty(uncontrolNums[i])){
						hotelGroup.setUncontrolNum(Integer.parseInt(uncontrolNums[i]));
					}
					if(StringUtils.isNotEmpty(remNumbers[i])){
						hotelGroup.setRemNumber(Integer.parseInt(remNumbers[i]));
					}
					if(StringUtils.isNotEmpty(airlines[i])){
						hotelGroup.setAirline(airlines[i]);
					}
					if(StringUtils.isNotEmpty(priorityDeductions[i])){
						hotelGroup.setPriorityDeduction(Integer.parseInt(priorityDeductions[i]));
					}
					if(StringUtils.isNotEmpty(frontMoneys[i])){
						hotelGroup.setFrontMoney(Double.parseDouble(frontMoneys[i]));
					}
					if(StringUtils.isNotEmpty(frontMoneyCurrencyIds[i])){
						hotelGroup.setFrontMoneyCurrencyId(Integer.parseInt(frontMoneyCurrencyIds[i]));
					}
					if(StringUtils.isNotEmpty(memos[i])){
						hotelGroup.setMemo(memos[i]);
					}
					if(StringUtils.isNotEmpty(status[i])){
						hotelGroup.setStatus(status[i]);
					}
					if(StringUtils.isNotEmpty(hotelGroupRooms[i])){
						hotelGroup.setActivityHotelGroupRoomList(this.transfer2ActivityHotelGroupRoom(hotelGroupRooms[i]));
					}
					if(StringUtils.isNotEmpty(hotelGroupMeals[i])){
						hotelGroup.setActivityHotelGroupMealList(this.transfer2ActivityHotelGroupMeal(hotelGroupMeals[i]));
					}
					if(StringUtils.isNotEmpty(hotelGroupPrices[i])){
						hotelGroup.setActivityHotelGroupPriceList(this.transfer2ActivityHotelGroupPrice(hotelGroupPrices[i]));
					}
					if(StringUtils.isNotEmpty(hotelControlNums[i])){
						hotelGroup.setActivityHotelGroupControlDetail(this.transfer2ActivityHotelGroupControlDetail(hotelControlNums[i]));
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	/***
	 * （三级表/房型*晚数）装载ActivityHotelGroupRoom
	 * @author LiuXueLiang
	 * @param roomsNightsFoods(格式：房型-晚数；房型-晚数)
	 * @return list
	 */
	public List<ActivityHotelGroupRoom> transfer2ActivityHotelGroupRoom(String roomsNight) {
		if(StringUtils.isBlank(roomsNight)){
			return null;
		}
		ActivityHotelGroupRoom activityHotelGroupRoom = new ActivityHotelGroupRoom();
		List<ActivityHotelGroupRoom> list = new ArrayList<ActivityHotelGroupRoom>();
		String[] ss = roomsNight.split(";");//[房型-晚数]
		for(int i=0;i<ss.length;i++){
			String[] strs = ss[i].split("-");
			if(ArrayUtils.isEmpty(strs)){
				continue;
			}
			if(ArrayUtils.contains(strs, REGEX)){
				if(!strs[0].equals(REGEX)){
					activityHotelGroupRoom.setHotelRoomUuid(strs[0]);
				}
				if(!strs[1].equals(REGEX)){
					activityHotelGroupRoom.setNights(Integer.parseInt(ss[1]));
				}
			}else{
				activityHotelGroupRoom.setHotelRoomUuid(strs[0]);
				activityHotelGroupRoom.setNights(Integer.parseInt(ss[1]));
			}
			list.add(activityHotelGroupRoom);
		}
		return list;
	}
	/***
	 * (三级表/基础餐型)装载ActivityHotelGroupMeal
	 * @author LiuXueLiang
	 * @param islandGroupMeal(格式：餐型；餐型)
	 * @return
	 */
	public List<ActivityHotelGroupMeal> transfer2ActivityHotelGroupMeal(String hotelGroupMeal) {
		if(StringUtils.isNotBlank(hotelGroupMeal)){
			return null;
		}
		ActivityHotelGroupMeal activityHotelGroupMeal = null;
		List <ActivityHotelGroupMeal> list  = new ArrayList<ActivityHotelGroupMeal>();
		String[] ss = hotelGroupMeal.split(";");//[基础餐型-升级餐型@币种@价格#升级餐型@币种@价格]
		for(int i=0;i<ss.length;i++){ //ss[i]格式：基础餐型-升级餐型@币种@价格#升级餐型@币种@价格
			activityHotelGroupMeal = new ActivityHotelGroupMeal();
			String[] strs = ss[i].split("-");
			if(ArrayUtils.isEmpty(strs)){
				continue;
			}
			if(ArrayUtils.contains(strs, REGEX)){
				if(!strs[0].equals(REGEX)){
					if(strs.length>0 && StringUtils.isNotBlank(strs[0])){
						activityHotelGroupMeal.setHotelMealUuid(strs[0]);//基础餐型
					}
				}
				if(!strs[1].equals(REGEX)){
					if(strs.length>1 && StringUtils.isNotBlank(strs[1])){
						activityHotelGroupMeal.setActivityHotelGroupMealsRiseList(this.transfer2ActivityHotelGroupMealRise(strs[1]));//升级餐型，格式：升级餐型@币种@价格#升级餐型@币种@价格
					}
				}
			}else{
				if(strs.length>0 && StringUtils.isNotBlank(strs[0])){
					activityHotelGroupMeal.setHotelMealUuid(strs[0]);//基础餐型
				}
				if(strs.length>1 && StringUtils.isNotBlank(strs[1])){
					activityHotelGroupMeal.setActivityHotelGroupMealsRiseList(this.transfer2ActivityHotelGroupMealRise(strs[1]));//升级餐型，格式：升级餐型@币种@价格#升级餐型@币种@价格
				}
			}
			
			list.add(activityHotelGroupMeal);
		}
		
		return list;
	}
	/**
	 * （四级表/升级餐型）装再ActivityHotelGroupMealRise
	 * @author LiuXueLiang  
	 * @param islandGroupMealRise(格式：  升级餐型@币种@价格#升级餐型@币种@价格)
	 * @return
	 */
	public List<ActivityHotelGroupMealRise> transfer2ActivityHotelGroupMealRise(String islandGroupMealRise){
		if(StringUtils.isNotBlank(islandGroupMealRise)){
			return null;
		}
		ActivityHotelGroupMealRise activityHotelGroupMealRise = null;
		List<ActivityHotelGroupMealRise> list = new ArrayList<ActivityHotelGroupMealRise>();
		String[] ss = islandGroupMealRise.split("#");//[升级餐型@币种@价格] 
		for(int i=0;i<ss.length;i++){
			activityHotelGroupMealRise = new ActivityHotelGroupMealRise();
			if(StringUtils.isNotBlank(ss[i])){
				String[] strs = ss[i].split("@");
				if(ArrayUtils.isEmpty(strs)){
					continue;
				}
				if(ArrayUtils.contains(strs, REGEX)){
					if(!strs[0].equals(REGEX)){
						if(strs.length>0 && StringUtils.isNotBlank(strs[0])){
							activityHotelGroupMealRise.setHotelMealUuid(ss[0]);//升级餐型	
						}
					}
					if(!strs[1].equals(REGEX)){
						if(strs.length>1 && StringUtils.isNotBlank(strs[1])){
							activityHotelGroupMealRise.setCurrencyId(Integer.parseInt(ss[1]));//币种	
						}
					}
					if(!strs[2].equals(REGEX)){
						if(strs.length>2 && StringUtils.isNotBlank(strs[2])){
							activityHotelGroupMealRise.setPrice(Double.parseDouble(ss[2]));//价格	
						}
					}
				}else{
					if(strs.length>0 && StringUtils.isNotBlank(strs[0])){
						activityHotelGroupMealRise.setHotelMealUuid(ss[0]);//升级餐型	
					}
					if(strs.length>1 && StringUtils.isNotBlank(strs[1])){
						activityHotelGroupMealRise.setCurrencyId(Integer.parseInt(ss[1]));//币种	
					}
					if(strs.length>2 && StringUtils.isNotBlank(strs[2])){
						activityHotelGroupMealRise.setPrice(Double.parseDouble(ss[2]));//价格	
					}
				}
				
				list.add(activityHotelGroupMealRise);
			}
		}
		return list;
	}
	/***
	 * （三级表/同行价）装载ActivityHotelGroupPrice 
	 * @author LiuXueLiang
	 * @paramislandGroupPrice(格式：游客类型-币种-价格;游客类型-币种-价格) 
	 * @return list
	 */
	public List<ActivityHotelGroupPrice> transfer2ActivityHotelGroupPrice(String hotelGroupPrice) {
		if(StringUtils.isBlank(hotelGroupPrice)){
			return null;
		}
		ActivityHotelGroupPrice activityHotelGroupPrice = new ActivityHotelGroupPrice();
		List <ActivityHotelGroupPrice> list  = new ArrayList<ActivityHotelGroupPrice>();
		String[] ss = hotelGroupPrice.split(";");//[游客类型-币种-价格]
		for(int i=0;i<ss.length;i++){
			String[] strs = ss[i].split("-");
			
			if(ArrayUtils.isEmpty(strs)){
				continue;
			}
			if(ArrayUtils.contains(strs, REGEX)){
				if(!strs[0].equals(REGEX)){
					activityHotelGroupPrice.setActivityHotelGroupUuid(strs[0]);//游客类型
				}
				if(!strs[1].equals(REGEX)){
					activityHotelGroupPrice.setType(strs[1]);//币种
				}
				if(!strs[2].equals(REGEX)){
					activityHotelGroupPrice.setCurrencyId(Integer.parseInt(strs[2]));//价格
				}
			}else{
				activityHotelGroupPrice.setActivityHotelGroupUuid(strs[0]);//游客类型
				activityHotelGroupPrice.setType(strs[1]);//币种
				activityHotelGroupPrice.setCurrencyId(Integer.parseInt(strs[2]));//价格
			}
			
			list.add(activityHotelGroupPrice);
		}
		return list;
	}
	/***
	 * （三级表/同行价）装载ActivityHotelGroupControlDetail 
	 * @author wangxv
	 * @param
	 * @return list
	 */
	public List<ActivityHotelGroupControlDetail> transfer2ActivityHotelGroupControlDetail(String hotelControlNum) {
		if(StringUtils.isBlank(hotelControlNum)){
			return null;
		}
		ActivityHotelGroupControlDetail activityHotelGroupControlDetail = new ActivityHotelGroupControlDetail();
		List <ActivityHotelGroupControlDetail> list  = new ArrayList<ActivityHotelGroupControlDetail>();
		String[] ss = hotelControlNum.split(";");//[游客类型-币种-价格]
		for(int i=0;i<ss.length;i++){
			String[] strs = ss[i].split("-");
			
			if(ArrayUtils.isEmpty(strs)){
				continue;
			}
			if(ArrayUtils.contains(strs, REGEX)){
				if(!strs[0].equals(REGEX)){
					activityHotelGroupControlDetail.setHotelControlDetailUuid(strs[0]);//酒店控房明细uuid
				}
				if(!strs[1].equals(REGEX)){
					activityHotelGroupControlDetail.setNum(Integer.valueOf(strs[1]));//使用库存数
				}
				
			}else{
				activityHotelGroupControlDetail.setHotelControlDetailUuid(strs[0]);//酒店控房明细uuid
				activityHotelGroupControlDetail.setNum(Integer.valueOf(strs[1]));//使用库存数
			}
			
			list.add(activityHotelGroupControlDetail);
		}
		return list;
	}
	/**
	 * 保存前的必填校验，返回false不能保存
	 * 
	 * @return
	 */
	public boolean validateFormInput() {
//		boolean flag = true;
		
		return true;
	}
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.lang.Integer getId() {
		return this.id;
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
	public ActivityHotel getDataObj() {
		return dataObj;
	}
	public void setDataObj(ActivityHotel dataObj) {
		this.dataObj = dataObj;
	}
	public String[] getGroupOpenDates() {
		return groupOpenDates;
	}
	public void setGroupOpenDates(String[] groupOpenDates) {
		this.groupOpenDates = groupOpenDates;
	}
	public String[] getIslandWays() {
		return islandWays;
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
	public String[] getCurrencyIds() {
		return currencyIds;
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
	public String[] getControlNums() {
		return controlNums;
	}
	public void setControlNums(String[] controlNums) {
		this.controlNums = controlNums;
	}
	public String[] getUncontrolNums() {
		return uncontrolNums;
	}
	public void setUncontrolNums(String[] uncontrolNums) {
		this.uncontrolNums = uncontrolNums;
	}
	public String[] getRemNumbers() {
		return remNumbers;
	}
	public void setRemNumbers(String[] remNumbers) {
		this.remNumbers = remNumbers;
	}
	public String[] getAirlines() {
		return airlines;
	}
	public void setAirlines(String[] airlines) {
		this.airlines = airlines;
	}
	public String[] getPriorityDeductions() {
		return priorityDeductions;
	}
	public void setPriorityDeductions(String[] priorityDeductions) {
		this.priorityDeductions = priorityDeductions;
	}
	public String[] getFrontMoneys() {
		return frontMoneys;
	}
	public void setFrontMoneys(String[] frontMoneys) {
		this.frontMoneys = frontMoneys;
	}
	public String[] getFrontMoneyCurrencyIds() {
		return frontMoneyCurrencyIds;
	}
	public void setFrontMoneyCurrencyIds(String[] frontMoneyCurrencyIds) {
		this.frontMoneyCurrencyIds = frontMoneyCurrencyIds;
	}
	public String[] getMemos() {
		return memos;
	}
	public void setMemos(String[] memos) {
		this.memos = memos;
	}
	public String[] getStatus() {
		return status;
	}
	public void setStatus(String[] status) {
		this.status = status;
	}
	public String[] getShareUser() {
		return shareUser;
	}
	public void setShareUser(String[] acceptShareUsers) {
		this.shareUser = acceptShareUsers;
	}
	public String[] getHotelGroupRooms() {
		return hotelGroupRooms;
	}
	public void setHotelGroupRooms(String[] hotelGroupRooms) {
		this.hotelGroupRooms = hotelGroupRooms;
	}
	public String[] getHotelGroupMeals() {
		return hotelGroupMeals;
	}
	public void setHotelGroupMeals(String[] hotelGroupMeals) {
		this.hotelGroupMeals = hotelGroupMeals;
	}
	public String[] getHotelGroupPrices() {
		return hotelGroupPrices;
	}
	public void setHotelGroupPrices(String[] hotelGroupPrices) {
		this.hotelGroupPrices = hotelGroupPrices;
	}
	public List<ActivityHotelGroup> getActivityhotelGroupLists() {
		return activityhotelGroupLists;
	}
	public void setActivityhotelGroupLists(
			List<ActivityHotelGroup> activityhotelGroupLists) {
		this.activityhotelGroupLists = activityhotelGroupLists;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public String[] getGroupCodes() {
		return groupCodes;
	}
	public void setGroupCodes(String[] groupCodes) {
		this.groupCodes = groupCodes;
	}
	public java.lang.String getWholesalerId() {
		return wholesalerId;
	}
	public void setWholesalerId(java.lang.String wholesalerId) {
		this.wholesalerId = wholesalerId;
	}
	
}

