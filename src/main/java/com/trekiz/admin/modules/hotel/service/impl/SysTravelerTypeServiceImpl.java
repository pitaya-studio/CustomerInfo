/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.SysTravelerTypeDao;
import com.trekiz.admin.modules.hotel.entity.SysTravelerType;
import com.trekiz.admin.modules.hotel.input.SysTravelerTypeInput;
import com.trekiz.admin.modules.hotel.query.SysTravelerTypeQuery;
import com.trekiz.admin.modules.hotel.service.SysTravelerTypeService;
import com.trekiz.admin.modules.traveler.dao.TravelerTypeDao;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SysTravelerTypeServiceImpl  extends BaseService implements SysTravelerTypeService{
	@Autowired
	private SysTravelerTypeDao sysTravelerTypeDao;
	@Autowired
	private TravelerTypeDao travelerTypeDao;

	public void save (SysTravelerType sysTravelerType){
		super.setOptInfo(sysTravelerType, BaseService.OPERATION_ADD);
		sysTravelerTypeDao.saveObj(sysTravelerType);
	}
	
	public void save (SysTravelerTypeInput sysTravelerTypeInput){
		SysTravelerType sysTravelerType = sysTravelerTypeInput.getSysTravelerType();
		super.setOptInfo(sysTravelerType, BaseService.OPERATION_ADD);
		sysTravelerTypeDao.saveObj(sysTravelerType);
	}
	
	public void update (SysTravelerType sysTravelerType){
		super.setOptInfo(sysTravelerType, BaseService.OPERATION_UPDATE);
		sysTravelerTypeDao.updateObj(sysTravelerType);
		//根据系统游客类型批量更新历史游客类型数据
		travelerTypeDao.batchUpdateTravelerType(sysTravelerType.getUuid());
	}
	
	public SysTravelerType getById(java.lang.Integer value) {
		return sysTravelerTypeDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		SysTravelerType obj = sysTravelerTypeDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<SysTravelerType> find(Page<SysTravelerType> page, SysTravelerTypeQuery sysTravelerTypeQuery) {
		DetachedCriteria dc = sysTravelerTypeDao.createDetachedCriteria();
		
	   	if(sysTravelerTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysTravelerTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysTravelerTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", sysTravelerTypeQuery.getUuid()));
		}
	   	if(sysTravelerTypeQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", sysTravelerTypeQuery.getSort()));
	   	}
		if (StringUtils.isNotEmpty(sysTravelerTypeQuery.getName())){
			dc.add(Restrictions.like("name", "%"+sysTravelerTypeQuery.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(sysTravelerTypeQuery.getShortName())){
			dc.add(Restrictions.eq("shortName", sysTravelerTypeQuery.getShortName()));
		}
		if (sysTravelerTypeQuery.getPersonType()!=null){
			dc.add(Restrictions.eq("personType", sysTravelerTypeQuery.getPersonType()));
		}
	   	if(sysTravelerTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysTravelerTypeQuery.getCreateBy()));
	   	}
		if(sysTravelerTypeQuery.getCreateDate()!=null){
			//dc.add(Restrictions.eq("createDate", sysTravelerTypeQuery.getCreateDate()));
			String time = new SimpleDateFormat("yyyy-MM-dd").format(sysTravelerTypeQuery.getCreateDate());
			dc.add(Restrictions.sqlRestriction("createDate >=?", time+" 00:00:00", StandardBasicTypes.STRING));
			dc.add(Restrictions.sqlRestriction("createDate <=?", time+" 23:59:59", StandardBasicTypes.STRING));
		}
	   	if(sysTravelerTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysTravelerTypeQuery.getUpdateBy()));
	   	}
		if(sysTravelerTypeQuery.getUpdateDate()!=null){
			//dc.add(Restrictions.eq("updateDate", sysTravelerTypeQuery.getUpdateDate()));
			String time = new SimpleDateFormat("yyyy-MM-dd").format(sysTravelerTypeQuery.getUpdateDate());
			dc.add(Restrictions.sqlRestriction("updateDate >=?", time+" 00:00:00", StandardBasicTypes.STRING));
			dc.add(Restrictions.sqlRestriction("updateDate <=?", time+" 23:59:59", StandardBasicTypes.STRING));
		}
		if (StringUtils.isNotEmpty(sysTravelerTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", sysTravelerTypeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysTravelerTypeDao.find(page, dc);
	}
	
	public List<SysTravelerType> find( SysTravelerTypeQuery sysTravelerTypeQuery) {
		DetachedCriteria dc = sysTravelerTypeDao.createDetachedCriteria();
		
	   	if(sysTravelerTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysTravelerTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysTravelerTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", sysTravelerTypeQuery.getUuid()));
		}
	   	if(sysTravelerTypeQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", sysTravelerTypeQuery.getSort()));
	   	}
		if (StringUtils.isNotEmpty(sysTravelerTypeQuery.getName())){
			dc.add(Restrictions.eq("name", sysTravelerTypeQuery.getName()));
		}
		if (StringUtils.isNotEmpty(sysTravelerTypeQuery.getShortName())){
			dc.add(Restrictions.eq("shortName", sysTravelerTypeQuery.getShortName()));
		}
		if (sysTravelerTypeQuery.getPersonType()!=null){
			dc.add(Restrictions.eq("personType", sysTravelerTypeQuery.getPersonType()));
		}
	   	if(sysTravelerTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysTravelerTypeQuery.getCreateBy()));
	   	}
		if(sysTravelerTypeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysTravelerTypeQuery.getCreateDate()));
		}
	   	if(sysTravelerTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysTravelerTypeQuery.getUpdateBy()));
	   	}
		if(sysTravelerTypeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysTravelerTypeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysTravelerTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", sysTravelerTypeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysTravelerTypeDao.find(dc);
	}
	
	public SysTravelerType getByUuid(String uuid) {
		return sysTravelerTypeDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		SysTravelerType sysTravelerType = getByUuid(uuid);
		sysTravelerType.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(sysTravelerType);
	}

	@Override
	public boolean findIsNameExist(String uuid, String travelerTypeName) {
		String sql = "SELECT  COUNT(*) FROM sys_traveler_type stt WHERE stt.uuid != ? AND stt.name = ?  AND stt.delFlag = ?";
		BigInteger count = (BigInteger)sysTravelerTypeDao.createSqlQuery(sql, uuid,travelerTypeName,BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		return count.intValue()>0?true:false;
	}
	
	@Override
	public boolean findIsShortNameExist(String uuid, String travelerTypeShortName) {
		String sql = "SELECT  COUNT(*) FROM sys_traveler_type stt WHERE stt.uuid != ? AND stt.short_name = ?  AND stt.delFlag = ?";
		BigInteger count = (BigInteger)sysTravelerTypeDao.createSqlQuery(sql, uuid,travelerTypeShortName,BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		return count.intValue()>0?true:false;
	}
	
	@Override
	public List<String> findExistTravelerUuids(String sysGuestTypeUuid,String value,String type) {
		String sql = "SELECT sgrr.sys_traveler_type_uuid  FROM sys_guest_type sgt RIGHT JOIN sys_guest_traveler_type_rel sgrr ON sgt.uuid=sgrr.sys_guest_type_uuid  "
                    +" WHERE sgt.type=? AND sgt.value=?  AND  sgt.uuid != ? AND sgt.delFlag = ? ";
		@SuppressWarnings("unchecked")
		List<String> list = sysTravelerTypeDao.createSqlQuery(sql,type,value,sysGuestTypeUuid,BaseEntity.DEL_FLAG_NORMAL).list();
		return list;
	}
}
