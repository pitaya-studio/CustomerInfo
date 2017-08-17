/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.module.query;

import java.util.Date;


/**报价条件映射类
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class QuotedPriceRoomQuery  {
	private Date inDate;//入住日期
	private String hotelRoomUuid;//房型
	private String hotelRoomText;//房型文本
	private String hotelMealUuid;//基础餐型
	private String hotelMealText;//基础餐型文本
	private String hotelMealRiseUuid;//升级餐型
	private String hotelMealRiseText;//升级餐型文本
	private String hotelCapacity;//房型的容住率
	private Integer nights;//晚数
	
	
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public String getHotelRoomUuid() {
		return hotelRoomUuid;
	}
	public void setHotelRoomUuid(String hotelRoomUuid) {
		this.hotelRoomUuid = hotelRoomUuid;
	}
	public String getHotelMealUuid() {
		return hotelMealUuid;
	}
	public void setHotelMealUuid(String hotelMealUuid) {
		this.hotelMealUuid = hotelMealUuid;
	}
	public String getHotelMealRiseUuid() {
		return hotelMealRiseUuid;
	}
	public void setHotelMealRiseUuid(String hotelMealRiseUuid) {
		this.hotelMealRiseUuid = hotelMealRiseUuid;
	}
	public String getHotelRoomText() {
		return hotelRoomText;
	}
	public void setHotelRoomText(String hotelRoomText) {
		this.hotelRoomText = hotelRoomText;
	}
	public String getHotelMealText() {
		return hotelMealText;
	}
	public void setHotelMealText(String hotelMealText) {
		this.hotelMealText = hotelMealText;
	}
	public String getHotelMealRiseText() {
		return hotelMealRiseText;
	}
	public void setHotelMealRiseText(String hotelMealRiseText) {
		this.hotelMealRiseText = hotelMealRiseText;
	}
	public Integer getNights() {
		return nights;
	}
	public void setNights(Integer nights) {
		this.nights = nights;
	}
	public String getHotelCapacity() {
		return hotelCapacity;
	}
	public void setHotelCapacity(String hotelCapacity) {
		this.hotelCapacity = hotelCapacity;
	}
	
}

