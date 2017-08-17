/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.module.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 报价结果明细展示
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class QuotedPriceDetailJsonBean  {
	private String inDate;//入住日期
	private String hotelRoomUuid;//房型UUID
	private String hotelRoomName;//房型nanme
	private String hotelRoomOccupancyRate;//容住率
	private String memo;//容住率备注
	
	private String hotelMealUuid;//房型uuid
	private String hotelMealText;//房型输出
	
	
	private List<GuestPriceJsonBean> guestPriceList;//动态游客类型价格
	
	
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getHotelRoomUuid() {
		return hotelRoomUuid;
	}
	public void setHotelRoomUuid(String hotelRoomUuid) {
		this.hotelRoomUuid = hotelRoomUuid;
	}
	public String getHotelRoomName() {
		return hotelRoomName;
	}
	public void setHotelRoomName(String hotelRoomName) {
		this.hotelRoomName = hotelRoomName;
	}
	public String getHotelRoomOccupancyRate() {
		return hotelRoomOccupancyRate;
	}
	public void setHotelRoomOccupancyRate(String hotelRoomOccupancyRate) {
		this.hotelRoomOccupancyRate = hotelRoomOccupancyRate;
	}
	public String getHotelMealUuid() {
		return hotelMealUuid;
	}
	public void setHotelMealUuid(String hotelMealUuid) {
		this.hotelMealUuid = hotelMealUuid;
	}
	public String getHotelMealText() {
		return hotelMealText;
	}
	public void setHotelMealText(String hotelMealText) {
		this.hotelMealText = hotelMealText;
	}
	
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<GuestPriceJsonBean> getGuestPriceList() {
		return guestPriceList;
	}
	public void setGuestPriceList(List<GuestPriceJsonBean> guestPriceList) {
		this.guestPriceList = guestPriceList;
	}
	public static final String FORMAT_IN_DATE = "yyyy-MM-dd";
	
	public Date getInDateForDate (){
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_IN_DATE);
		Date date = null;
		try {
			date = sdf.parse(inDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}    
	
}

