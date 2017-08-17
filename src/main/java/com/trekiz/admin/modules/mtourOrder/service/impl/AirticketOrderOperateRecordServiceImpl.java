/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service.impl;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderOperateRecordDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderOperateRecord;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderOperateRecordInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderOperateRecordQuery;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderOperateRecordService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class AirticketOrderOperateRecordServiceImpl  extends BaseService implements AirticketOrderOperateRecordService{
	@Autowired
	private AirticketOrderOperateRecordDao airticketOrderOperateRecordDao;

	public void save (AirticketOrderOperateRecord airticketOrderOperateRecord){
		super.setOptInfo(airticketOrderOperateRecord, BaseService.OPERATION_ADD);
		airticketOrderOperateRecordDao.saveObj(airticketOrderOperateRecord);
	}
	
	public void save (AirticketOrderOperateRecordInput airticketOrderOperateRecordInput){
		AirticketOrderOperateRecord airticketOrderOperateRecord = airticketOrderOperateRecordInput.getAirticketOrderOperateRecord();
		super.setOptInfo(airticketOrderOperateRecord, BaseService.OPERATION_ADD);
		airticketOrderOperateRecordDao.saveObj(airticketOrderOperateRecord);
	}
	
	public void update (AirticketOrderOperateRecord airticketOrderOperateRecord){
		super.setOptInfo(airticketOrderOperateRecord, BaseService.OPERATION_UPDATE);
		airticketOrderOperateRecordDao.updateObj(airticketOrderOperateRecord);
	}
	
	public AirticketOrderOperateRecord getById(java.lang.Integer value) {
		return airticketOrderOperateRecordDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		AirticketOrderOperateRecord obj = airticketOrderOperateRecordDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<AirticketOrderOperateRecord> find(Page<AirticketOrderOperateRecord> page, AirticketOrderOperateRecordQuery airticketOrderOperateRecordQuery) {
		DetachedCriteria dc = airticketOrderOperateRecordDao.createDetachedCriteria();
		
	   	if(airticketOrderOperateRecordQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderOperateRecordQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderOperateRecordQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderOperateRecordQuery.getUuid()));
		}
	   	if(airticketOrderOperateRecordQuery.getTargetType()!=null){
	   		dc.add(Restrictions.eq("targetType", airticketOrderOperateRecordQuery.getTargetType()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderOperateRecordQuery.getTargetUuid())){
			dc.add(Restrictions.eq("targetUuid", airticketOrderOperateRecordQuery.getTargetUuid()));
		}
		if (StringUtils.isNotEmpty(airticketOrderOperateRecordQuery.getContent())){
			dc.add(Restrictions.eq("content", airticketOrderOperateRecordQuery.getContent()));
		}
	   	if(airticketOrderOperateRecordQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderOperateRecordQuery.getCreateBy()));
	   	}
		if(airticketOrderOperateRecordQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderOperateRecordQuery.getCreateDate()));
		}
	   	if(airticketOrderOperateRecordQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderOperateRecordQuery.getUpdateBy()));
	   	}
		if(airticketOrderOperateRecordQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderOperateRecordQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderOperateRecordQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderOperateRecordQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderOperateRecordDao.find(page, dc);
	}
	
	public List<AirticketOrderOperateRecord> find( AirticketOrderOperateRecordQuery airticketOrderOperateRecordQuery) {
		DetachedCriteria dc = airticketOrderOperateRecordDao.createDetachedCriteria();
		
	   	if(airticketOrderOperateRecordQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderOperateRecordQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderOperateRecordQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderOperateRecordQuery.getUuid()));
		}
	   	if(airticketOrderOperateRecordQuery.getTargetType()!=null){
	   		dc.add(Restrictions.eq("targetType", airticketOrderOperateRecordQuery.getTargetType()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderOperateRecordQuery.getTargetUuid())){
			dc.add(Restrictions.eq("targetUuid", airticketOrderOperateRecordQuery.getTargetUuid()));
		}
		if (StringUtils.isNotEmpty(airticketOrderOperateRecordQuery.getContent())){
			dc.add(Restrictions.eq("content", airticketOrderOperateRecordQuery.getContent()));
		}
	   	if(airticketOrderOperateRecordQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderOperateRecordQuery.getCreateBy()));
	   	}
		if(airticketOrderOperateRecordQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderOperateRecordQuery.getCreateDate()));
		}
	   	if(airticketOrderOperateRecordQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderOperateRecordQuery.getUpdateBy()));
	   	}
		if(airticketOrderOperateRecordQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderOperateRecordQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderOperateRecordQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderOperateRecordQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderOperateRecordDao.find(dc);
	}
	
	public AirticketOrderOperateRecord getByUuid(String uuid) {
		return airticketOrderOperateRecordDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		AirticketOrderOperateRecord airticketOrderOperateRecord = getByUuid(uuid);
		airticketOrderOperateRecord.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(airticketOrderOperateRecord);
	}
}
