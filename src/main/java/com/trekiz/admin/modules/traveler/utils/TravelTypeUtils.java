package com.trekiz.admin.modules.traveler.utils;

import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.traveler.dao.TravelerTypeDao;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * 海岛游工具类
 * @author gao
 *  2015年7月21日
 */
public class TravelTypeUtils {
	
	private static TravelerTypeDao travelerTypeDao = SpringContextHolder.getBean(TravelerTypeDao.class);
	
	
	/**
	 * 根据uuid，获取游客分类名称
	 */
	public static String getTravelerTypeCn(String uuid){
		String typeCn = new String();
		TravelerType type = travelerTypeDao.getByUuid(uuid);
		if(type!=null){
			typeCn = type.getName();
		}else{
			typeCn = "";
		}
		return typeCn;
	}
}
