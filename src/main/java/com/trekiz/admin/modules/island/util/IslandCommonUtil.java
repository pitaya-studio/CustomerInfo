package com.trekiz.admin.modules.island.util;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.hotel.entity.HotelControl;
import com.trekiz.admin.modules.hotel.entity.HotelControlDetail;
import com.trekiz.admin.modules.hotel.service.HotelControlDetailService;
import com.trekiz.admin.modules.hotel.service.HotelControlService;

/**
 * 
 *  文件名: IslandCommonUtil.java
 *  功能:海岛游相关通用工具类
 *  
 *  修改记录:   
 *  
 *  @author ruyi.chen
 *  @DateTime 2015-06-24
 *  @version 1.0
 */
public class IslandCommonUtil {

	public static final String ISLAND_REFUND_REVIEWLIST = "island_refund_reviewList";//海岛游订单退款列表
	public static final String ISLAND_EXIT_GROUP_REVIEWLIST = "island_exit_group_reviewList";//海岛游订单退团列表
	public static final String ISLAND_CHANGE_GROUP_REVIEWLIST = "island_change_group_reviewList";//海岛游订单转团列表
	public static final String ISLAND_CHANGE_MONEY_REVIEWLIST = "island_change_money_reviewList";//海岛游订单转款列表
	public static final String ISLAND_CHANGE_PRICE_REVIEWLIST = "island_change_price_reviewList";//海岛游订单改价列表	
	public static final String ISLAND_REBATES_REVIEWLIST = "island_rebates_reviewList";//海岛游订单返佣列表
	public static final String ISLAND_BORROWING_REVIEWLIST = "island_borrowing_reviewList";//海岛游订单借款列表
	
	private static  HotelControlDetailService hotelControlDetailService  = SpringContextHolder.getBean(HotelControlDetailService.class);
	private static HotelControlService hotelControlService = SpringContextHolder.getBean("HotelControl.class");
	/**
	 * 根据uuid，获取HotelControlDetail 实体
	 * @author gao
	 * @param uuid
	 * @return
	 */
	public static final HotelControlDetail getHotelControlDetailByUuid(String uuid){
		HotelControlDetail detail = new HotelControlDetail();
		if(StringUtils.isNotBlank(uuid)){
			detail = hotelControlDetailService.getByUuid(uuid);
		}
		return detail;
	}
	/**
	 * 根据uuid，获取HotelControl 实体
	 * @author gao
	 * @param uuid
	 * @return
	 */
	public static final HotelControl getHotelControlByUuid(String uuid){
		HotelControl cont = new HotelControl();
		if(StringUtils.isNotBlank(uuid)){
			cont = hotelControlService.getByUuid(uuid);
		}
		return cont;
	}
}
