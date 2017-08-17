/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

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
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupControlDetailDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupControlDetail;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupControlDetailInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupControlDetailQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupControlDetailService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupControlDetailServiceImpl  extends BaseService implements ActivityHotelGroupControlDetailService{
	@Autowired
	private ActivityHotelGroupControlDetailDao activityHotelGroupControlDetailDao;

	public void save (ActivityHotelGroupControlDetail activityHotelGroupControlDetail){
		super.setOptInfo(activityHotelGroupControlDetail, BaseService.OPERATION_ADD);
		activityHotelGroupControlDetailDao.saveObj(activityHotelGroupControlDetail);
	}
	
	public void save (ActivityHotelGroupControlDetailInput activityHotelGroupControlDetailInput){
		ActivityHotelGroupControlDetail activityHotelGroupControlDetail = activityHotelGroupControlDetailInput.getActivityHotelGroupControlDetail();
		super.setOptInfo(activityHotelGroupControlDetail, BaseService.OPERATION_ADD);
		activityHotelGroupControlDetailDao.saveObj(activityHotelGroupControlDetail);
	}
	
	public void update (ActivityHotelGroupControlDetail activityHotelGroupControlDetail){
		super.setOptInfo(activityHotelGroupControlDetail, BaseService.OPERATION_UPDATE);
		activityHotelGroupControlDetailDao.updateObj(activityHotelGroupControlDetail);
	}
	
	public ActivityHotelGroupControlDetail getById(java.lang.Integer value) {
		return activityHotelGroupControlDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityHotelGroupControlDetail obj = activityHotelGroupControlDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityHotelGroupControlDetail> find(Page<ActivityHotelGroupControlDetail> page, ActivityHotelGroupControlDetailQuery activityHotelGroupControlDetailQuery) {
		DetachedCriteria dc = activityHotelGroupControlDetailDao.createDetachedCriteria();
		
	   	if(activityHotelGroupControlDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupControlDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupControlDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupControlDetailQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupControlDetailQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getHotelControlDetailUuid())){
			dc.add(Restrictions.eq("hotelControlDetailUuid", activityHotelGroupControlDetailQuery.getHotelControlDetailUuid()));
		}
	   	if(activityHotelGroupControlDetailQuery.getNum()!=null){
	   		dc.add(Restrictions.eq("num", activityHotelGroupControlDetailQuery.getNum()));
	   	}
	   	if(activityHotelGroupControlDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupControlDetailQuery.getCreateBy()));
	   	}
		if(activityHotelGroupControlDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupControlDetailQuery.getCreateDate()));
		}
	   	if(activityHotelGroupControlDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupControlDetailQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupControlDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupControlDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupControlDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupControlDetailDao.find(page, dc);
	}
	
	public List<ActivityHotelGroupControlDetail> find( ActivityHotelGroupControlDetailQuery activityHotelGroupControlDetailQuery) {
		DetachedCriteria dc = activityHotelGroupControlDetailDao.createDetachedCriteria();
		
	   	if(activityHotelGroupControlDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupControlDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupControlDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupControlDetailQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getActivityHotelGroupUuid())){
			dc.add(Restrictions.eq("activityHotelGroupUuid", activityHotelGroupControlDetailQuery.getActivityHotelGroupUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getHotelControlDetailUuid())){
			dc.add(Restrictions.eq("hotelControlDetailUuid", activityHotelGroupControlDetailQuery.getHotelControlDetailUuid()));
		}
	   	if(activityHotelGroupControlDetailQuery.getNum()!=null){
	   		dc.add(Restrictions.eq("num", activityHotelGroupControlDetailQuery.getNum()));
	   	}
	   	if(activityHotelGroupControlDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupControlDetailQuery.getCreateBy()));
	   	}
		if(activityHotelGroupControlDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupControlDetailQuery.getCreateDate()));
		}
	   	if(activityHotelGroupControlDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupControlDetailQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupControlDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupControlDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupControlDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupControlDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupControlDetailDao.find(dc);
	}
	
	public ActivityHotelGroupControlDetail getByUuid(String uuid) {
		return activityHotelGroupControlDetailDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityHotelGroupControlDetail activityHotelGroupControlDetail = getByUuid(uuid);
		activityHotelGroupControlDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityHotelGroupControlDetail);
	}

	@Override
	public List<ActivityHotelGroupControlDetail> getDetailListByGroupUuid(String uuid) {
		return activityHotelGroupControlDetailDao.getDetailListByGroupUuid(uuid);
	}
}
