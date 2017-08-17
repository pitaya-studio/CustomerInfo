package com.trekiz.admin.modules.eprice.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.modules.eprice.form.SpeedOrderInput;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;

public interface SpeedGenOrderService {
	public void save(HotelOrder hotelOrder);

	public void update(HotelOrder hotelOrder);

	public HotelOrder getById(java.lang.Integer value);

	public HotelOrder getByUuid(String uuid);

	/**
	 * 保存酒店产品信息，酒店预报名
	 * @author star
	 * @param speedOrderInput
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> saveHotelOrder(SpeedOrderInput speedOrderInput,HttpServletRequest request)
			throws Exception;


	/**
	 * 清除session中Orderpay的缓存
	 * 
	 * @param role
	 * @return
	 */
	public Object clearObject(Object object);
	
	/**
	 * 保存产品相关信息
	 * @param speedOrderInput
	 * @param request
	 */
	public void saveActivityHotel(SpeedOrderInput speedOrderInput,HttpServletRequest request);
	
	/**
	 * 获取当前批发商下所有已上架的团期
	 * @param saleId
	 * @return
	 */
	public List<ActivityHotelGroup> getListBySaleId(String saleId);
}
