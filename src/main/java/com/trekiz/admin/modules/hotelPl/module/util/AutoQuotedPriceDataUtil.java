package com.trekiz.admin.modules.hotelPl.module.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.trekiz.admin.modules.hotel.entity.HotelGuestType;
import com.trekiz.admin.modules.hotel.entity.SysGuestType;
import com.trekiz.admin.modules.hotelPl.entity.HotelPl;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxPrice;
import com.trekiz.admin.modules.hotelPl.module.bean.GuestPriceJsonBean;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.traveler.entity.TravelerType;


public class AutoQuotedPriceDataUtil implements java.io.Serializable {
	
	public static String REGEX=";";
	
	/**批发商下所有的价单UUID和价单的映射**/
	private Map<String,HotelPl> hotelPlMap = new HashMap<String,HotelPl>();
	
	/**价单UUID和该价单的税金字典集合的映射*/
	private Map<String,List<HotelPlTaxPrice>> taxPriceMap = new HashMap<String,List<HotelPlTaxPrice>>();
	
	/**当前批发商下 游客类型 的简写和TravelerType map*/
	private Map<String,TravelerType> travelerTypeMap;
	
	/**查询批发商下的所有绑定住客类型 返回SysGuestType实例（实例中的uuid是hotel_guest_type中的uuid）*/
	/**key：根据房型UUID进行分组，value是 房型下的所有系统住客类型集合 */
	private Map<Object, List<SysGuestType>> guestTypeMap4HotelRoom;

	/**系统内所有游客类型和住客类型的映射关系 KEY:系统游客类型UUID，VALUE:系统住客类型UUID数据集合*/
	private Map<String, List<String>> guestTypeMap4TravelerType;
	
	/**得到游客类型和对应的游客人数 KEY是游客类型的简写 ：A成人 B婴儿 C儿童 O老人 S特殊人群 VALUE 是对应的游客人数*/
	private Map<String, Integer> perNumMap ;

	/**批发商下的币种map*/
	private Map<Object,List<Currency>> currencyMap;
	/**房型和容住率的对应关系*/
	private Map<String,String> roomOccupancyRateMap;
	
	/**房型和容住率备注的对应关系*/
	private Map<String,String> roomOccupancyRateNoteMap;
	
	/**报价后的明细价格集合 按 游客类型 分组排序，KEY是游客类型，VALUE是对应的每天价格集合（按价格顺序排序） 。*/
	private Map<Integer,Map<String,List<GuestPriceJsonBean>>> groupDetailPriceList4TravelTypeMap;

	private Long rmbCurrencyId;//rmb的id
	
	
	
	private StringBuffer message = new StringBuffer();//存放一些异常信息，返回前台使用
	
	//存放报价条件中的的餐型（如果有升餐则是升餐类型）和交通类型，用于优惠时选择的基础餐型和交通类型过滤使用
	private Set<String> mealSet ;
	private Set<String> islandSet ;
	
	
	
	//儿童和成人的简写集合
	private List<String> childList=new ArrayList<String>();
	private List<String> adultList=new ArrayList<String>();
	
	/**第N人的 简写和批发商住客类型UUID 映射*/
	private Map<Object, List<HotelGuestType>> shortNameAndGuestTypeMap;
	
	
	
	private TravelerType sortPreferObj;//优惠排序用的游客类型对象，基于此对象进行优惠后的价格排序。默认是第一个人数不为0的成人类型的游客类型
	
	public Map<String, List<HotelPlTaxPrice>> getTaxPriceMap() {
		return taxPriceMap;
	}

	public void setTaxPriceMap(Map<String, List<HotelPlTaxPrice>> taxPriceMap) {
		this.taxPriceMap = taxPriceMap;
	}

	public Map<String, TravelerType> getTravelerTypeMap() {
		return travelerTypeMap;
	}

	public void setTravelerTypeMap(Map<String, TravelerType> travelerTypeMap) {
		this.travelerTypeMap = travelerTypeMap;
	}

	public Map<Object, List<SysGuestType>> getGuestTypeMap4HotelRoom() {
		return guestTypeMap4HotelRoom;
	}

	public void setGuestTypeMap4HotelRoom(
			Map<Object, List<SysGuestType>> guestTypeMap4HotelRoom) {
		this.guestTypeMap4HotelRoom = guestTypeMap4HotelRoom;
	}

	public Map<String, Integer> getPerNumMap() {
		return perNumMap;
	}

	public void setPerNumMap(Map<String, Integer> perNumMap) {
		this.perNumMap = perNumMap;
	}

	public Map<Object, List<Currency>> getCurrencyMap() {
		return currencyMap;
	}

	public void setCurrencyMap(Map<Object, List<Currency>> currencyMap) {
		this.currencyMap = currencyMap;
	}
	
	/**
	 * 根据币种ID 或得当前币种实体 add by zhanghao
	 * @param id
	 * @return
	 */
	public Currency getCurrencyObject(Long id){
		Map<Object, List<Currency>> map = getCurrencyMap();
		if(MapUtils.isNotEmpty(map)){
			List<Currency> list = map.get(id);
			if(CollectionUtils.isNotEmpty(list)){
				return list.get(0);
			}
		}
		return null;
	}

	public Map<String, String> getRoomOccupancyRateMap() {
		return roomOccupancyRateMap;
	}

	public void setRoomOccupancyRateMap(Map<String, String> roomOccupancyRateMap) {
		this.roomOccupancyRateMap = roomOccupancyRateMap;
	}
	
	
	public Map<String, String> getRoomOccupancyRateNoteMap() {
		return roomOccupancyRateNoteMap;
	}

	public void setRoomOccupancyRateNoteMap(
			Map<String, String> roomOccupancyRateNoteMap) {
		this.roomOccupancyRateNoteMap = roomOccupancyRateNoteMap;
	}

	/**
	 * 根据房型UUID或得容住率 add by zhanghao
	 * @param uuid
	 * @return
	 */
	public String getHotelRoomOccupancyRateString(String uuid){
		if(roomOccupancyRateMap==null){
			roomOccupancyRateMap = new HashMap<String,String>();
		}
		return roomOccupancyRateMap.get(uuid);
	}
	
	public String getHotelRoomOccupancyRateNoteString(String uuid){
		if(roomOccupancyRateNoteMap==null){
			roomOccupancyRateNoteMap = new HashMap<String,String>();
		}
		return roomOccupancyRateNoteMap.get(uuid);
	}

	public Map<Integer, Map<String, List<GuestPriceJsonBean>>> getGroupDetailPriceList4TravelTypeMap() {
		return groupDetailPriceList4TravelTypeMap;
	}

	public void setGroupDetailPriceList4TravelTypeMap(
			Map<Integer, Map<String, List<GuestPriceJsonBean>>> groupDetailPriceList4TravelTypeMap) {
		this.groupDetailPriceList4TravelTypeMap = groupDetailPriceList4TravelTypeMap;
	}

	public Long getRmbCurrencyId() {
		return rmbCurrencyId;
	}

	public void setRmbCurrencyId(Long rmbCurrencyId) {
		this.rmbCurrencyId = rmbCurrencyId;
	}

	public Map<String, HotelPl> getHotelPlMap() {
		return hotelPlMap;
	}

	public void setHotelPlMap(Map<String, HotelPl> hotelPlMap) {
		this.hotelPlMap = hotelPlMap;
	}


	public StringBuffer getMessage() {
		return message;
	}

	public void putMessage(String message){
		this.getMessage().append(message);
		this.getMessage().append(AutoQuotedPriceDataUtil.REGEX);
	}
	
	public void setMessage(StringBuffer message) {
		this.message = message;
	}

	public Set<String> getMealSet() {
		return mealSet;
	}

	public void setMealSet(Set<String> mealSet) {
		this.mealSet = mealSet;
	}

	public Set<String> getIslandSet() {
		return islandSet;
	}

	public void setIslandSet(Set<String> islandSet) {
		this.islandSet = islandSet;
	}

	public List<String> getChildList() {
		return childList;
	}

	public void setChildList(List<String> childList) {
		this.childList = childList;
	}

	public List<String> getAdultList() {
		return adultList;
	}

	public void setAdultList(List<String> adultList) {
		this.adultList = adultList;
	}

	public Map<String, List<String>> getGuestTypeMap4TravelerType() {
		return guestTypeMap4TravelerType;
	}

	public void setGuestTypeMap4TravelerType(
			Map<String, List<String>> guestTypeMap4TravelerType) {
		this.guestTypeMap4TravelerType = guestTypeMap4TravelerType;
	}

	public Map<Object, List<HotelGuestType>> getShortNameAndGuestTypeMap() {
		return shortNameAndGuestTypeMap;
	}

	public void setShortNameAndGuestTypeMap(
			Map<Object, List<HotelGuestType>> shortNameAndGuestTypeMap) {
		this.shortNameAndGuestTypeMap = shortNameAndGuestTypeMap;
	}

	public TravelerType getSortPreferObj() {
		return sortPreferObj;
	}

	public void setSortPreferObj(TravelerType sortPreferObj) {
		this.sortPreferObj = sortPreferObj;
	}

	
	
}
