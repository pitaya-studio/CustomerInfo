/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.module.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.trekiz.admin.common.utils.BeanUtil;



/**
 * 报价结果中的动态游客类型价格
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class GuestPriceJsonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String travelerType;//游客类型或者住客类型UUID（第N人时是住客类型UUID）
	private String travelerTypeText;//游客类型页面输出
	private String currencyId;//币种id
	private String currencyText;//币种输出符号
	private Double amount;//优惠后的金额
	private Double preferAmount;//优惠力度（即减少的金额）
	
	private int isThirdPerson=0;//是否是第三人（0否，1第三人）
	private String guestType;//是第三人时会存入对应的住客类型UUID
	
	
	/**绑定的入住日期，报价优惠时需要 按日期 补交 税金餐费等*/
	private String inDate;//入住日期
	
	/**优惠时需要单独计算的价格，在报价时需要把这些值存储，以便计算优惠时进行扣减*/
	private Double roomAmount;//房费
	private Double roomAmountTotal;//含税的房费
	private Double mealAmount;//餐费
	private Double mealAmountTotal;//含税的餐费
	private Double islandwayAmount;//交通费
	private Double islandwayAmountTotal;//含税的交通费
	private String hotelPlUuid;//报价结果所依赖的价单UUID
	private String currencyIdPrefer;//使用优惠时计算需要的币种id 
	
	public String getTravelerType() {
		return travelerType;
	}
	public void setTravelerType(String travelerType) {
		this.travelerType = travelerType;
	}
	public String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyText() {
		return currencyText;
	}
	public void setCurrencyText(String currencyText) {
		this.currencyText = currencyText;
	}
	public Double getAmount() {
		return amount;
	}
	public String getAmountString() {
		return BeanUtil.numberFormat2String(amount);
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getTravelerTypeText() {
		return travelerTypeText;
	}
	public void setTravelerTypeText(String travelerTypeText) {
		this.travelerTypeText = travelerTypeText;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public Double getRoomAmount() {
		return roomAmount;
	}
	public void setRoomAmount(Double roomAmount) {
		this.roomAmount = roomAmount;
	}
	public Double getRoomAmountTotal() {
		return roomAmountTotal;
	}
	public void setRoomAmountTotal(Double roomAmountTotal) {
		this.roomAmountTotal = roomAmountTotal;
	}
	public Double getMealAmount() {
		return mealAmount;
	}
	public void setMealAmount(Double mealAmount) {
		this.mealAmount = mealAmount;
	}
	public Double getMealAmountTotal() {
		return mealAmountTotal;
	}
	public void setMealAmountTotal(Double mealAmountTotal) {
		this.mealAmountTotal = mealAmountTotal;
	}
	public Double getIslandwayAmount() {
		return islandwayAmount;
	}
	public void setIslandwayAmount(Double islandwayAmount) {
		this.islandwayAmount = islandwayAmount;
	}
	public Double getIslandwayAmountTotal() {
		return islandwayAmountTotal;
	}
	public void setIslandwayAmountTotal(Double islandwayAmountTotal) {
		this.islandwayAmountTotal = islandwayAmountTotal;
	}
	public String getHotelPlUuid() {
		return hotelPlUuid;
	}
	public void setHotelPlUuid(String hotelPlUuid) {
		this.hotelPlUuid = hotelPlUuid;
	}
	public String getCurrencyIdPrefer() {
		return currencyIdPrefer;
	}
	public void setCurrencyIdPrefer(String currencyIdPrefer) {
		this.currencyIdPrefer = currencyIdPrefer;
	}
	
    public static final String FORMAT_IN_DATE = "yyyy-MM-dd";
	
	public Date getinDateForDate (){
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_IN_DATE);
		Date date = null;
		try {
			date = sdf.parse(inDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public Double getPreferAmount() {
		return preferAmount;
	}
	public String getPreferAmountString() {
		return BeanUtil.numberFormat2String(preferAmount);
	}
	public void setPreferAmount(Double preferAmount) {
		this.preferAmount = preferAmount;
	}
	public int getIsThirdPerson() {
		return isThirdPerson;
	}
	public void setIsThirdPerson(int isThirdPerson) {
		this.isThirdPerson = isThirdPerson;
	}
	public String getGuestType() {
		return guestType;
	}
	public void setGuestType(String guestType) {
		this.guestType = guestType;
	}    
	
}

