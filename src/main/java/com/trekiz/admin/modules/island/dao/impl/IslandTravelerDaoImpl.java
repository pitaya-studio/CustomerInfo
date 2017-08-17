/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.island.dao.IslandTravelerDao;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.traveler.entity.Traveler;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class IslandTravelerDaoImpl extends BaseDaoImpl<IslandTraveler>  implements IslandTravelerDao{
	@Override
	public IslandTraveler getByUuid(String uuid) {
		Object entity = super.createQuery("from IslandTraveler islandTraveler where islandTraveler.uuid=? ", uuid).uniqueResult();
		if(entity != null) {
			return (IslandTraveler)entity;
		}
		return null;
	}

	@Override
	public List<IslandTraveler> findTravelerByOrderUuid(String orderUuid, boolean hasDelFlag) {
		List<IslandTraveler> list = Lists.newArrayList();
		if (hasDelFlag) {
			list = super.find("from IslandTraveler where orderUuid=? order by id asc", orderUuid);
		} else {
			list = super.find("from IslandTraveler where orderUuid=? and status != 2 and status != 5" +
					" and delFlag='" + Traveler.DEL_FLAG_NORMAL + "' order by id asc", orderUuid);
		}
		return list;
	}
	
}
