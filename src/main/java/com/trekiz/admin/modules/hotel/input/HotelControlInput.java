/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotel.entity.HotelControl;
import com.trekiz.admin.modules.hotel.entity.HotelControlDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlFlightDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;

/**
 * 控房页面表单输入 原型
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 * 
 */

public class HotelControlInput implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private java.lang.String uuid;//UUID
	private java.lang.String name;//控房名称
	private java.lang.String country;//国家
	private java.lang.String hotelGroup;//酒店集团，字典表
	private java.lang.String hotelUuid;//酒店
	private java.lang.String islandUuid;//海岛
	private java.lang.String currencyId;//币种
	private java.lang.Integer groundSupplier;//地接供应商
	private java.lang.Integer purchaseType;//采购类型
	
	private String[] inDates;//入住日期
	private String[] airlines;//航空公司，多个航空公司用“;”分隔
	private String[] rooms;//房型和晚数拼接字符串的数组；格式如[“水上屋uuid*1晚数-沙滩屋uuid*2晚数”，“水上屋uuid*1晚数-沙滩屋uuid*2晚数”]
	private String[] hotelMeals;//餐型；格式如[“餐型uuid1;餐型uuid2-餐型uuid3;餐型uuid4”，“餐型uuid5;餐型uuid6-餐型uuid7;餐型uuid8”]
	private String[] islandWays;//上岛方式
	private String[] detailCurrencys;//控房单详情币种id
	private String[] totalPrices;//总价
	private String[] stocks;//库存
	private String[] statuss;//控房状态
	private String[] memos;//备注
	private String[] detailUuids;//控房单详情uuids
	private String[] airlineUuids;//航空公司uuids
	private String[] roomUuids;//房型uuids
	
	private java.lang.String memo;//主表备注
	
	//酒店控房单详情列表
	private List<HotelControlDetail> hotelControlDetails;

	/**
	 * 页面数组转换成 HotelControl 对象集合
	 * @return
	 */
	public HotelControl transfer2HotelControl(){
		if(validateFormInput()){
			HotelControl hc = new HotelControl();
			hc.setUuid(uuid);
			hc.setName(name);
			hc.setCountry(country);
			hc.setHotelGroup(hotelGroup);
			hc.setHotelUuid(hotelUuid);
			hc.setIslandUuid(islandUuid);
			hc.setCurrencyId(Integer.parseInt(currencyId));
			hc.setMemo(memo);
			hc.setGroundSupplier(groundSupplier);
			hc.setPurchaseType(purchaseType);
			hc.setDetailList(this.transfer2HotelControlDetail());
			return hc;
		}
		return null;
	}
	
	/**
	 * 从数据库中读取并加载控房单数据
	 * @return
	 */
	public void initHotelControlInput(HotelControl hc){
		this.setUuid(hc.getUuid());
		this.setName(hc.getName());
		this.setCountry(hc.getCountry());
		this.setHotelGroup(hc.getHotelGroup());
		this.setHotelUuid(hc.getHotelUuid());
		this.setIslandUuid(hc.getIslandUuid());
		this.setCurrencyId(String.valueOf(hc.getCurrencyId()));
		this.setMemo(hc.getMemo());
		this.setGroundSupplier(hc.getGroundSupplier());
		this.setPurchaseType(hc.getPurchaseType());
		this.setHotelControlDetails(hc.getDetailList());
	}
	
	/**
	 * 页面数组转换成 HotelControlDetail 对象集合
	 * @return
	 */
	public List<HotelControlDetail> transfer2HotelControlDetail(){
		if(validateFormInput()){
			List<HotelControlDetail> list = new ArrayList<HotelControlDetail>();
			try {
				for(int i=0;i<inDates.length;i++){
					HotelControlDetail detail = new HotelControlDetail();
					if(StringUtils.isNotBlank(inDates[i])){
						detail.setInDate(DateUtils.string2Date(inDates[i]));
					}
					if(detailUuids != null && i<detailUuids.length) {
						detail.setUuid(detailUuids[i]);
					}
					if(islandWays != null && i<islandWays.length){
						detail.setIslandWay(islandWays[i]);
					}
					if(detailCurrencys != null && i<detailCurrencys.length) {
						detail.setCurrencyId(Integer.parseInt(detailCurrencys[i]));
					}
					if(totalPrices != null && i<totalPrices.length){
						detail.setTotalPrice(totalPrices[i]);
					}
					if(stocks != null && i<stocks.length){
						if(StringUtils.isNotEmpty(stocks[i])) {
							detail.setStock(Integer.parseInt(stocks[i]));
						}
					}
					if(statuss != null && i<statuss.length){
						detail.setStatus(Integer.parseInt(statuss[i]));
					}
					if(memos != null && i<memos.length){
						detail.setMemo(memos[i]);
					}
					if(airlines != null && i<airlines.length){
						if(airlineUuids != null && i<airlineUuids.length) {
							detail.setFlightList(this.transfer2HotelControlFlightDetail(airlines[i], airlineUuids[i]));
						} else {
							detail.setFlightList(this.transfer2HotelControlFlightDetail(airlines[i], null));
						}
					}
					
					if(rooms != null && i<rooms.length){
						if(roomUuids != null && i<roomUuids.length) {
							detail.setRoomList(this.transfer2HotelControlRoomDetail(rooms[i], hotelMeals[i], roomUuids[i]));
						} else {
							detail.setRoomList(this.transfer2HotelControlRoomDetail(rooms[i], hotelMeals[i], null));
						}
					}
					
					list.add(detail);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}
		
		return null;
	}
	
	/**
	 * 根据页面传递的 room字符串转换 成 HotelControlRoomDetail
	 * @param room
	 * @return
	 */
	private List<HotelControlRoomDetail> transfer2HotelControlRoomDetail(String room, String hotelMeal, String uuid){
		if(StringUtils.isBlank(room)){
			return null;
		}
		List<HotelControlRoomDetail> list = new ArrayList<HotelControlRoomDetail>();
		
		String[] roomData = room.split("-");
		String[] hotelMealData = hotelMeal.split("-");
		String[] uuidData = null;
		if(StringUtils.isNotEmpty(uuid)) {
			uuidData = uuid.split(";");
		}
		if(ArrayUtils.isNotEmpty(roomData)){
			for(int i=0; i<roomData.length; i++) {
				String data = roomData[i];
				HotelControlRoomDetail detail = new HotelControlRoomDetail();
				if(StringUtils.isNotEmpty(uuid)) {
					detail.setUuid(uuidData[i]);
				}
				String[] roomColumns = data.split("\\*");
				if(ArrayUtils.isNotEmpty(roomColumns)){
					detail.setRoomUuid(roomColumns[0]);
					detail.setNight(Integer.parseInt(roomColumns[1].trim()));
				}
				//设置酒店餐型
				detail.setHotelMeals(hotelMealData[i]);
				list.add(detail);
			}
		}
		
		return list;
	}
	/**
	 * 根据页面传递的 airline字符串转换 成 HotelControlFlightDetail
	 * @param airline
	 * @return
	 */
	private List<HotelControlFlightDetail> transfer2HotelControlFlightDetail(String airline, String uuid){
		if(StringUtils.isBlank(airline)){
			return null;
		}
		List<HotelControlFlightDetail> list = new ArrayList<HotelControlFlightDetail>();
		HotelControlFlightDetail deatil = new HotelControlFlightDetail();
		deatil.setUuid(uuid);
		deatil.setAirline(airline);
		list.add(deatil);
		return list;
	}
	
	/**
	 * 保存前的必填校验，返回false不能保存
	 * @return
	 */
	public boolean validateFormInput(){
		boolean flag = true;
		if(flag) {
			return flag;
		}
		
		if(StringUtils.isBlank(country)){
			return false;
		}
		if(StringUtils.isBlank(hotelGroup)){
			return false;
		}
		if(StringUtils.isBlank(hotelUuid)){
			return false;
		}
		if(StringUtils.isBlank(islandUuid)){
			return false;
		}
		if(StringUtils.isBlank(currencyId)){
			return false;
		}
		if(ArrayUtils.isEmpty(inDates)){
			return false;
		}
		if(ArrayUtils.isEmpty(airlines)){
			return false;
		}
		if(ArrayUtils.isEmpty(rooms)){
			return false;
		}
		if(ArrayUtils.isEmpty(hotelMeals)){
			return false;
		}
		if(ArrayUtils.isEmpty(islandWays)){
			return false;
		}
		if(ArrayUtils.isEmpty(totalPrices)){
			return false;
		}
		if(ArrayUtils.isEmpty(stocks)){
			return false;
		}
		if(ArrayUtils.isEmpty(statuss)){
			return false;
		}
		if(groundSupplier == null){
			return false;
		}
		if(StringUtils.isBlank(purchaseType+"")){
			return false;
		}
		return true;
	}
	
	public java.lang.String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public java.lang.String getCountry() {
		return country;
	}

	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	public java.lang.String getHotelGroup() {
		return hotelGroup;
	}

	public void setHotelGroup(java.lang.String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}

	public java.lang.String getHotelUuid() {
		return hotelUuid;
	}

	public void setHotelUuid(java.lang.String hotelUuid) {
		this.hotelUuid = hotelUuid;
	}

	public java.lang.String getIslandUuid() {
		return islandUuid;
	}

	public void setIslandUuid(java.lang.String islandUuid) {
		this.islandUuid = islandUuid;
	}
	
	public java.lang.String getCurrencyId() {
		return this.currencyId;
	}
	
	public void setCurrencyId(java.lang.String currencyId) {
		this.currencyId = currencyId;
	}

	

	public String[] getInDates() {
		return inDates;
	}

	public void setInDates(String[] inDates) {
		this.inDates = inDates;
	}

	public String[] getAirlines() {
		return airlines;
	}

	public void setAirlines(String[] airlines) {
		this.airlines = airlines;
	}

	public String[] getRooms() {
		return rooms;
	}

	public void setRooms(String[] rooms) {
		this.rooms = rooms;
	}

	public String[] getHotelMeals() {
		return hotelMeals;
	}

	public void setHotelMeals(String[] hotelMeals) {
		this.hotelMeals = hotelMeals;
	}

	public String[] getIslandWays() {
		return islandWays;
	}

	public void setIslandWays(String[] islandWays) {
		this.islandWays = islandWays;
	}
	
	public String[] getDetailCurrencys() {
		return detailCurrencys;
	}

	public void setDetailCurrencys(String[] detailCurrencys) {
		this.detailCurrencys = detailCurrencys;
	}

	public String[] getTotalPrices() {
		return totalPrices;
	}

	public void setTotalPrices(String[] totalPrices) {
		this.totalPrices = totalPrices;
	}

	public String[] getStocks() {
		return stocks;
	}

	public void setStocks(String[] stocks) {
		this.stocks = stocks;
	}

	public String[] getStatuss() {
		return statuss;
	}

	public void setStatuss(String[] statuss) {
		this.statuss = statuss;
	}

	public String[] getMemos() {
		return memos;
	}

	public void setMemos(String[] memos) {
		this.memos = memos;
	}
	
	public String[] getDetailUuids() {
		return detailUuids;
	}

	public void setDetailUuids(String[] detailUuids) {
		this.detailUuids = detailUuids;
	}

	public String[] getAirlineUuids() {
		return airlineUuids;
	}

	public void setAirlineUuids(String[] airlineUuids) {
		this.airlineUuids = airlineUuids;
	}

	public String[] getRoomUuids() {
		return roomUuids;
	}

	public void setRoomUuids(String[] roomUuids) {
		this.roomUuids = roomUuids;
	}

	public java.lang.String getMemo() {
		return memo;
	}

	public void setMemo(java.lang.String memo) {
		this.memo = memo;
	}

	public List<HotelControlDetail> getHotelControlDetails() {
		return hotelControlDetails;
	}

	public void setHotelControlDetails(List<HotelControlDetail> hotelControlDetails) {
		this.hotelControlDetails = hotelControlDetails;
	}

	public java.lang.Integer getGroundSupplier() {
		return groundSupplier;
	}

	public void setGroundSupplier(java.lang.Integer groundSupplier) {
		this.groundSupplier = groundSupplier;
	}

	public java.lang.Integer getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(java.lang.Integer purchaseType) {
		this.purchaseType = purchaseType;
	}
	
	
}

