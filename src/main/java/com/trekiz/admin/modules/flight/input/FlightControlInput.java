package com.trekiz.admin.modules.flight.input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.flight.entity.FlightControl;
import com.trekiz.admin.modules.flight.entity.FlightControlDetail;
import com.trekiz.admin.modules.flight.entity.FlightControlHotelDetail;

/**
 * 机票库存页面表单输入 原型
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 * 
 */
public class FlightControlInput implements Serializable {

	private static final long serialVersionUID = -8607497341266971722L;
	
	private java.lang.String uuid;//uuid
	private java.lang.String name;//控票名称
	private java.lang.String country;//国家
	private java.lang.String islandUuid;//岛屿UUID
	private java.lang.String airline;//航空公司（字典）
	private java.lang.String currencyId;//币种ID
	private java.lang.String memo;//备注

	
	private String[] hotelUuids;//库存参考酒店明细 uuids 多个参考酒店用“;”分隔
	private String[] departureDates;//出发日期
	private String[] spaceGradeTypes;//舱位等级（字典）
	private String[] flightNumbers;//航班号
	private String[] priceCurrencyIds;//价格币种
	private String[] prices;//总价
	private String[] taxesCurrencyIds;//税费币种
	private String[] taxesPrices;//税费总价
	private String[] stocks;//库存（票）
	private String[] sellStocks;//已售
	private String[] preStocks;//预占位（票）
	private String[] memos;//备注
	private String[] statuss;//查询状态
	
	//修改时使用
	private String[] flightHotelUuids;//机票库存参考酒店明细uuids
	private String[] detailUuids;//库存明细uuids
	//酒店控房单详情列表
	private List<FlightControlDetail> flightControlDetails;
	
	/**
	 * 页面数组转换成 HotelControl 对象集合
	 * @return
	 */
	public FlightControl transfer2FlightControl(){
		if(! validateFormInput()){
			return null;
		}
		
		FlightControl flightControl = new FlightControl();
		flightControl.setUuid(uuid);
		flightControl.setName(name);
		flightControl.setCountry(country);
		flightControl.setIslandUuid(islandUuid);
		flightControl.setAirline(airline);
		flightControl.setCurrencyId(Integer.parseInt(currencyId));
		flightControl.setMemo(memo);
		flightControl.setFlightControlDetails(this.transfer2FlightControlDetail());
		return flightControl;
	}
	
	/**
	 * 从数据库中读取并加载库存数据
	 * @return
	 */
	public void initFlightControlInput(FlightControl fc){
		this.setUuid(fc.getUuid());
		this.setName(fc.getName());
		this.setCountry(fc.getCountry());
		this.setIslandUuid(fc.getIslandUuid());
		this.setAirline(fc.getAirline().toString());
		this.setCurrencyId(fc.getCurrencyId().toString());
		this.setMemo(fc.getMemo());
		this.setFlightControlDetails(fc.getFlightControlDetails());
	}	
	private List<FlightControlDetail> transfer2FlightControlDetail() {
		if(!validateFormInput()) {
			return null;
		}
		List<FlightControlDetail> flightControlDetails = new ArrayList<FlightControlDetail>();
		try{
			for(int i=0; i<departureDates.length; i++) {
				FlightControlDetail flightControlDetail = new FlightControlDetail();
				if(StringUtils.isNotEmpty(departureDates[i])) {
					flightControlDetail.setDepartureDate(DateUtils.string2Date(departureDates[i]));
				}
				
				if(detailUuids != null && i< detailUuids.length) {
					flightControlDetail.setUuid(detailUuids[i]);
				}
				
				if(spaceGradeTypes != null && i< spaceGradeTypes.length) {
					flightControlDetail.setSpaceGradeType(spaceGradeTypes[i] == null ? null :spaceGradeTypes[i]);
				}
				if(flightNumbers != null && i< flightNumbers.length) {
					flightControlDetail.setFlightNumber(flightNumbers[i] == null ? null :flightNumbers[i]);
				}
				if(priceCurrencyIds != null && i< priceCurrencyIds.length) {
					flightControlDetail.setPriceCurrencyId(priceCurrencyIds[i] == null ? null : Integer.parseInt(priceCurrencyIds[i]));
				}
				
				if(prices != null && i< prices.length) {
					flightControlDetail.setPrice(prices[i] == null ? null : Double.parseDouble(prices[i]));
				}
				
				if(taxesCurrencyIds != null && i< taxesCurrencyIds.length) {
					flightControlDetail.setTaxesCurrencyId(Integer.parseInt(taxesCurrencyIds[i]));
				}
				
				if(taxesPrices != null && i< taxesPrices.length) {
					flightControlDetail.setTaxesPrice(taxesPrices[i] == null ? null : Double.parseDouble(taxesPrices[i]));
				}
				
				if(stocks != null && i< stocks.length) {
					flightControlDetail.setStock(stocks[i] == null ? null : Integer.parseInt(stocks[i]));
				}
				
				if(sellStocks != null && i< sellStocks.length) {
					flightControlDetail.setSellStock(sellStocks[i] == null ? 0 : Integer.parseInt(sellStocks[i]));
				}
				
				if(preStocks != null && i< preStocks.length) {
					flightControlDetail.setPreStock(preStocks[i] == null ? 0 : Integer.parseInt(preStocks[i]));
				}
				
				if(memos != null && i< memos.length) {
					flightControlDetail.setMemo(memos[i]);
				}
				
				if(statuss != null && i< statuss.length) {
					flightControlDetail.setStatus(Integer.parseInt(statuss[i]));
				}
				
				if(hotelUuids != null && i< hotelUuids.length) {
					if(flightHotelUuids != null && i < flightHotelUuids.length) {
						flightControlDetail.setFlightControlHotelDetails(this.transfer2FlightControlHotelDetail(hotelUuids[i], flightHotelUuids[i]));
					} else {
						flightControlDetail.setFlightControlHotelDetails(this.transfer2FlightControlHotelDetail(hotelUuids[i], null));
					}
				}
				
				flightControlDetails.add(flightControlDetail);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return flightControlDetails;
	}
	
	/**
	 * 根据页面传递的 hotelUuids字符串转换 成 FlightControlHotelDetail
	 * @param hotelUuids
	 * @param uuids
	 * @return
	 */
	private List<FlightControlHotelDetail> transfer2FlightControlHotelDetail(String hotelUuids, String uuids){
		if(StringUtils.isBlank(hotelUuids)){
			return null;
		}
		
		String[] hotelUuidData = hotelUuids.split(";");
		String[] uuidData = null ;
		if(StringUtils.isNotEmpty(uuids)) {
			uuidData = uuids.split(";");
		}
		
		List<FlightControlHotelDetail> list = new ArrayList<FlightControlHotelDetail>();
		for(int i=0; i<hotelUuidData.length; i++) {
			FlightControlHotelDetail deatil = new FlightControlHotelDetail();
			if(uuidData != null) {
				deatil.setUuid(uuidData[i]);
			}
			deatil.setHotelUuid(hotelUuidData[i]);
			
			list.add(deatil);
		}
		return list;
	}
	
	public boolean validateFormInput() {
		return true;
	}
	
	public java.lang.String getUuid() {
		return uuid;
	}
	public void setUuid(java.lang.String uuid) {
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
	public java.lang.String getIslandUuid() {
		return islandUuid;
	}
	public void setIslandUuid(java.lang.String islandUuid) {
		this.islandUuid = islandUuid;
	}
	public java.lang.String getAirline() {
		return airline;
	}
	public void setAirline(java.lang.String airline) {
		this.airline = airline;
	}
	public java.lang.String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(java.lang.String currencyId) {
		this.currencyId = currencyId;
	}
	public java.lang.String getMemo() {
		return memo;
	}
	public void setMemo(java.lang.String memo) {
		this.memo = memo;
	}
	public String[] getDetailUuids() {
		return detailUuids;
	}
	public void setDetailUuids(String[] detailUuids) {
		this.detailUuids = detailUuids;
	}
	public String[] getHotelUuids() {
		return hotelUuids;
	}
	public void setHotelUuids(String[] hotelUuids) {
		this.hotelUuids = hotelUuids;
	}
	public String[] getDepartureDates() {
		return departureDates;
	}
	public void setDepartureDates(String[] departureDates) {
		this.departureDates = departureDates;
	}
	public String[] getSpaceGradeTypes() {
		return spaceGradeTypes;
	}
	public void setSpaceGradeTypes(String[] spaceGradeTypes) {
		this.spaceGradeTypes = spaceGradeTypes;
	}
	public String[] getPriceCurrencyIds() {
		return priceCurrencyIds;
	}
	public void setPriceCurrencyIds(String[] priceCurrencyIds) {
		this.priceCurrencyIds = priceCurrencyIds;
	}
	public String[] getPrices() {
		return prices;
	}
	public void setPrices(String[] prices) {
		this.prices = prices;
	}
	public String[] getTaxesCurrencyIds() {
		return taxesCurrencyIds;
	}
	public void setTaxesCurrencyIds(String[] taxesCurrencyIds) {
		this.taxesCurrencyIds = taxesCurrencyIds;
	}
	public String[] getTaxesPrices() {
		return taxesPrices;
	}
	public void setTaxesPrices(String[] taxesPrices) {
		this.taxesPrices = taxesPrices;
	}
	public String[] getStocks() {
		return stocks;
	}
	public void setStocks(String[] stocks) {
		this.stocks = stocks;
	}
	public String[] getSellStocks() {
		return sellStocks;
	}
	public void setSellStocks(String[] sellStocks) {
		this.sellStocks = sellStocks;
	}
	public String[] getPreStocks() {
		return preStocks;
	}
	public void setPreStocks(String[] preStocks) {
		this.preStocks = preStocks;
	}
	public String[] getMemos() {
		return memos;
	}
	public void setMemos(String[] memos) {
		this.memos = memos;
	}
	public String[] getStatuss() {
		return statuss;
	}
	public void setStatuss(String[] statuss) {
		this.statuss = statuss;
	}
	public List<FlightControlDetail> getFlightControlDetails() {
		return flightControlDetails;
	}
	public void setFlightControlDetails(List<FlightControlDetail> flightControlDetails) {
		this.flightControlDetails = flightControlDetails;
	}

	public String[] getFlightHotelUuids() {
		return flightHotelUuids;
	}

	public void setFlightHotelUuids(String[] flightHotelUuids) {
		this.flightHotelUuids = flightHotelUuids;
	}
	public String[] getFlightNumbers() {
		return flightNumbers;
	}

	public void setFlightNumbers(String[] flightNumbers) {
		this.flightNumbers = flightNumbers;
	}
}
