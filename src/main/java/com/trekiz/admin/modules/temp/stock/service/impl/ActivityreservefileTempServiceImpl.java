/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.service.impl;

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
import com.trekiz.admin.modules.temp.stock.dao.ActivityreservefileTempDao;
import com.trekiz.admin.modules.temp.stock.entity.ActivityreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.ActivityreservefileTempInput;
import com.trekiz.admin.modules.temp.stock.query.ActivityreservefileTempQuery;
import com.trekiz.admin.modules.temp.stock.service.ActivityreservefileTempService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityreservefileTempServiceImpl  extends BaseService implements ActivityreservefileTempService{
	@Autowired
	private ActivityreservefileTempDao activityreservefileTempDao;

	public void save (ActivityreservefileTemp activityreservefileTemp){
		super.setOptInfo(activityreservefileTemp, BaseService.OPERATION_ADD);
		activityreservefileTempDao.saveObj(activityreservefileTemp);
	}
	
	public void save (ActivityreservefileTempInput activityreservefileTempInput){
		ActivityreservefileTemp activityreservefileTemp = activityreservefileTempInput.getActivityreservefileTemp();
		super.setOptInfo(activityreservefileTemp, BaseService.OPERATION_ADD);
		activityreservefileTempDao.saveObj(activityreservefileTemp);
	}
	
	public void update (ActivityreservefileTemp activityreservefileTemp){
		super.setOptInfo(activityreservefileTemp, BaseService.OPERATION_UPDATE);
		activityreservefileTempDao.updateObj(activityreservefileTemp);
	}
	
	public ActivityreservefileTemp getById(java.lang.Integer value) {
		return activityreservefileTempDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityreservefileTemp obj = activityreservefileTempDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityreservefileTemp> find(Page<ActivityreservefileTemp> page, ActivityreservefileTempQuery activityreservefileTempQuery) {
		DetachedCriteria dc = activityreservefileTempDao.createDetachedCriteria();
		
	   	if(activityreservefileTempQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityreservefileTempQuery.getId()));
	   	}
	   	if(activityreservefileTempQuery.getSrcActivityId()!=null){
	   		dc.add(Restrictions.eq("srcActivityId", activityreservefileTempQuery.getSrcActivityId()));
	   	}
	   	if(activityreservefileTempQuery.getActivityGroupId()!=null){
	   		dc.add(Restrictions.eq("activityGroupId", activityreservefileTempQuery.getActivityGroupId()));
	   	}
	   	if(activityreservefileTempQuery.getAgentId()!=null){
	   		dc.add(Restrictions.eq("agentId", activityreservefileTempQuery.getAgentId()));
	   	}
	   	if(activityreservefileTempQuery.getSrcDocId()!=null){
	   		dc.add(Restrictions.eq("srcDocId", activityreservefileTempQuery.getSrcDocId()));
	   	}
		if (StringUtils.isNotEmpty(activityreservefileTempQuery.getFileName())){
			dc.add(Restrictions.eq("fileName", activityreservefileTempQuery.getFileName()));
		}
		if(activityreservefileTempQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityreservefileTempQuery.getCreateDate()));
		}
	   	if(activityreservefileTempQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityreservefileTempQuery.getCreateBy()));
	   	}
		if(activityreservefileTempQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityreservefileTempQuery.getUpdateDate()));
		}
	   	if(activityreservefileTempQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityreservefileTempQuery.getUpdateBy()));
	   	}
		if (StringUtils.isNotEmpty(activityreservefileTempQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityreservefileTempQuery.getDelFlag()));
		}
	   	if(activityreservefileTempQuery.getReserveOrderId()!=null){
	   		dc.add(Restrictions.eq("reserveOrderId", activityreservefileTempQuery.getReserveOrderId()));
	   	}
		
		//dc.addOrder(Order.desc("id"));
		return activityreservefileTempDao.find(page, dc);
	}
	
	public List<ActivityreservefileTemp> find( ActivityreservefileTempQuery activityreservefileTempQuery) {
		DetachedCriteria dc = activityreservefileTempDao.createDetachedCriteria();
		
	   	if(activityreservefileTempQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityreservefileTempQuery.getId()));
	   	}
	   	if(activityreservefileTempQuery.getSrcActivityId()!=null){
	   		dc.add(Restrictions.eq("srcActivityId", activityreservefileTempQuery.getSrcActivityId()));
	   	}
	   	if(activityreservefileTempQuery.getActivityGroupId()!=null){
	   		dc.add(Restrictions.eq("activityGroupId", activityreservefileTempQuery.getActivityGroupId()));
	   	}
	   	if(activityreservefileTempQuery.getAgentId()!=null){
	   		dc.add(Restrictions.eq("agentId", activityreservefileTempQuery.getAgentId()));
	   	}
	   	if(activityreservefileTempQuery.getSrcDocId()!=null){
	   		dc.add(Restrictions.eq("srcDocId", activityreservefileTempQuery.getSrcDocId()));
	   	}
		if (StringUtils.isNotEmpty(activityreservefileTempQuery.getFileName())){
			dc.add(Restrictions.eq("fileName", activityreservefileTempQuery.getFileName()));
		}
		if(activityreservefileTempQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityreservefileTempQuery.getCreateDate()));
		}
	   	if(activityreservefileTempQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityreservefileTempQuery.getCreateBy()));
	   	}
		if(activityreservefileTempQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityreservefileTempQuery.getUpdateDate()));
		}
	   	if(activityreservefileTempQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityreservefileTempQuery.getUpdateBy()));
	   	}
		if (StringUtils.isNotEmpty(activityreservefileTempQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityreservefileTempQuery.getDelFlag()));
		}
	   	if(activityreservefileTempQuery.getReserveOrderId()!=null){
	   		dc.add(Restrictions.eq("reserveOrderId", activityreservefileTempQuery.getReserveOrderId()));
	   	}
		
		//dc.addOrder(Order.desc("id"));
		return activityreservefileTempDao.find(dc);
	}
	
	public ActivityreservefileTemp getByUuid(String uuid) {
		return activityreservefileTempDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityreservefileTemp activityreservefileTemp = getByUuid(uuid);
		activityreservefileTemp.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityreservefileTemp);
	}
	
	public boolean batchDelete(String[] uuids) {
		return activityreservefileTempDao.batchDelete(uuids);
	}
	
	/**
	 * 根据草稿箱uuid获取草稿箱文件集合
	 * @Description: 
	 * @param @param reserveTempUuid
	 * @param @return   
	 * @return List<ActivityreservefileTemp>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-23 下午9:36:02
	 */
	public List<ActivityreservefileTemp> getFilesByReserveTempUuid(String reserveTempUuid) {
		return activityreservefileTempDao.getFilesByReserveTempUuid(reserveTempUuid);
	}
	
}
