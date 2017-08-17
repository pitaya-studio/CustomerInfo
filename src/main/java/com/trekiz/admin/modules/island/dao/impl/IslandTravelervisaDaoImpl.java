/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.island.dao.IslandTravelervisaDao;
import com.trekiz.admin.modules.island.entity.IslandTravelervisa;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class IslandTravelervisaDaoImpl extends BaseDaoImpl<IslandTravelervisa>  implements IslandTravelervisaDao{
	@Override
	public IslandTravelervisa getByUuid(String uuid) {
		Object entity = super.createQuery("from IslandTravelervisa islandTravelervisa where islandTravelervisa.uuid=? and islandTravelervisa.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (IslandTravelervisa)entity;
		}
		return null;
	}
	
}
