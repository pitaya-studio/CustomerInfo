package com.trekiz.admin.agentToOffice.line.utils;

import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.common.utils.SpringContextHolder;

/**
 * 游玩线路工具类
 * */
public class TouristLineUtils {
	
	private static TouristLineService touristLineService = SpringContextHolder.getBean(TouristLineService.class);

	/**
	 * 根据所属区域ids 获取区域名称
	 * @param areaIds 所属区域ids
	 * @return 区域名称（以','分隔）
	 * @author yang.wang@quauq.com
	 * */
	public static String getAreaNamesByIds(String areaIds) {
		return touristLineService.getDistrictNameByIds(areaIds);
	}

}
