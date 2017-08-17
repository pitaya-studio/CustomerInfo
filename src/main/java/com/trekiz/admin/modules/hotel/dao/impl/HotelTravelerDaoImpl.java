/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerDao;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.traveler.entity.Traveler;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelTravelerDaoImpl extends BaseDaoImpl<HotelTraveler>  implements HotelTravelerDao {
	@Override
	public HotelTraveler getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelTraveler hotelTraveler where hotelTraveler.uuid=? ", uuid).uniqueResult();
		if(entity != null) {
			return (HotelTraveler)entity;
		}
		return null;
	}

	/**
	 * @Description 查询游客
	 * @param isNormal 是否要查询正常游客（不包括已退团和已转团游客）
	 * @author yakun.bai
	 * @Date 2015-11-18
	 */
	@Override
	public List<HotelTraveler> findTravelerByOrderUuid(String orderUuid, boolean isNormal) {
		List<HotelTraveler> list = Lists.newArrayList();
		if (isNormal) {
			list = super.find("from HotelTraveler where orderUuid=? and status != 2 and status != 5 " +
					"and delFlag = '" + Traveler.DEL_FLAG_NORMAL + "' order by id asc", orderUuid);
		} else {
			list = super.find("from HotelTraveler where orderUuid=? and delFlag = '" + Traveler.DEL_FLAG_NORMAL + "' order by id asc", orderUuid);
		}
		return list;
	}
	
}
