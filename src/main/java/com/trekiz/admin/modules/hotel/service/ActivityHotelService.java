/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.input.ActivityHotelInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityHotelService{
	
	public void save (ActivityHotel activityHotel);
	
	public Map<String,String> save (ActivityHotelInput activityHotelInput,String status);
	
	public void update (ActivityHotel activityHotel);
	
	public ActivityHotel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityHotel> find(Page<ActivityHotel> page, ActivityHotelQuery activityHotelQuery);
	
	public List<ActivityHotel> find( ActivityHotelQuery activityHotelQuery);
	
	public ActivityHotel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 构造酒店列表页面add by hhx
	 * @param query
	 * @param request
	 * @param response
	 * @return
	 */
	public Page<Map<String,Object>> getActivityHotelList(ActivityHotelQuery query, HttpServletRequest request,
			HttpServletResponse response);
	
	/**
	 * 构造单个产品对应的团期列表add by hhx
	 * @param activityHotelUuid
	 * @return
	 */
	public List<Map<String,Object>> getActivityHotelGroupList(String activityHotelUuid,ActivityHotelQuery query);
	
	/**
	 * 保存酒店产品的修改
	 * @param activityHotelInput
	 * @param jsonstr
	 * @return
	 */
	public Map<String, String> updateActivityHotel(ActivityHotelInput activityHotelInput,String[] jsonstr);
	
	public Map<String, String> saveorUpdateActivityHotel(ActivityHotelGroup activityHotelGroup, String activityHotelUuid,String status);

	/**
	 * 订单转报名时扣减余位方法
	 * Map<String,String>
	 *         key:result。value：success有剩余库存扣减成功，fail没有库存扣减失败，error参数异常。
	 *         key:message。value:fail和error情况下的异常信息
	 * @param hotelGroupUuid  酒店团期表uuid
	 * @param forecaseReportNum 需扣减的房间数
	 * @return
	 */
	public Map<String, String> deductRemNumber(String hotelGroupUuid,Integer forecaseReportNum);

}
