/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.island.entity.Island;

import com.trekiz.admin.modules.island.dao.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class IslandDaoImpl extends BaseDaoImpl<Island>  implements IslandDao{
	
	@Override
	public Island getByUuid(String uuid) {
		Object entity = super.createQuery("from Island island where island.uuid=? and island.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (Island)entity;
		}
		return null;
	}
	
	@Override
	public List<Island> findListByCompanyId(Integer companyId){
		List<Island> islandList = new ArrayList<Island>();
		islandList = super.find("from Island island where island.delFlag="+BaseEntity.DEL_FLAG_NORMAL+" and island.wholesalerId=?",companyId);
		return islandList;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysCompanyDictView> findIslandWaysByIslandUuid(String islandUuid) {
		if(StringUtils.isEmpty(islandUuid)) {
			return null;
		}
		Island island = this.getByUuid(islandUuid);
		if(island == null || StringUtils.isEmpty(island.getIslandWay())) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT uuid,label FROM sys_company_dict_view where ");
		sb.append("delFlag = ? and ");
		
		String[] islandWayUuidArr = island.getIslandWay().split(",");
		sb.append("uuid in(");
		for(String islandWayUuid : islandWayUuidArr) {
			sb.append("'" + islandWayUuid + "',");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		
		return (List<SysCompanyDictView>) super.findCustomObjBySql(sb.toString(), SysCompanyDictView.class,  BaseEntity.DEL_FLAG_NORMAL);
	}
}
