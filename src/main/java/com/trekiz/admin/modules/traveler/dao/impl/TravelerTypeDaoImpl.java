/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.traveler.dao.impl;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.SysTravelerTypeDao;
import com.trekiz.admin.modules.hotel.entity.SysTravelerType;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

import com.trekiz.admin.modules.traveler.dao.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class TravelerTypeDaoImpl extends BaseDaoImpl<TravelerType>  implements TravelerTypeDao{
	@Autowired
	private SysTravelerTypeDao sysTravelerTypeDao;
	
	@Override
	public TravelerType getByUuid(String uuid) {
		Object entity = super.createQuery("from TravelerType travelerType where travelerType.uuid=? and travelerType.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (TravelerType)entity;
		}
		return null;
	}
	
	@Override
	public TravelerType getTravelerName(String uuid) {
		Object entity = super.createQuery("from TravelerType travelerType where travelerType.uuid=? ", uuid).uniqueResult();
		if(entity != null) {
			return (TravelerType)entity;
		}
		return null;
	}
	
	public List<TravelerType> getTravelerTypesByWholesalerId(Integer wholesalerId) {
		return super.find("from TravelerType travelerType where travelerType.wholesalerId=? and travelerType.status=? and travelerType.delFlag=?", wholesalerId, TravelerType.STATUS_ON, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public boolean batchUpdateTravelerType(String sysTravelerTypeUuid) {
		boolean flag = false;
		SysTravelerType sysTravelerType = sysTravelerTypeDao.getByUuid(sysTravelerTypeUuid);
		if(sysTravelerType == null) {
			return flag;
		}
		
		SQLQuery sqlQuery = super.createSqlQuery("update traveler_type set short_name=? , person_type = ? where sys_traveler_type = ? and delFlag = ?", sysTravelerType.getShortName(), sysTravelerType.getPersonType(), sysTravelerTypeUuid, BaseEntity.DEL_FLAG_NORMAL);
		if(sqlQuery.executeUpdate() >= 0) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 根据批发商id获取所有的游客类型
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return List<TravelerType>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-18 下午1:50:19
	 */
	public List<TravelerType> findAllByWholesalerId(Integer wholesalerId) {
		return super.find("from TravelerType travelerType where travelerType.wholesalerId=? and travelerType.delFlag=? order by travelerType.sort asc, travelerType.updateDate desc", wholesalerId, BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
