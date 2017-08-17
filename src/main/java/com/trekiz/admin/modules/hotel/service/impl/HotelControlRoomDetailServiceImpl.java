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
import com.trekiz.admin.modules.hotel.dao.HotelControlRoomDetailDao;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;
import com.trekiz.admin.modules.hotel.service.HotelControlRoomDetailService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelControlRoomDetailServiceImpl  extends BaseService implements HotelControlRoomDetailService{
	@Autowired
	private HotelControlRoomDetailDao hotelControlRoomDetailDao;

	public void save (HotelControlRoomDetail hotelControlRoomDetail){
		super.setOptInfo(hotelControlRoomDetail, BaseService.OPERATION_ADD);
		hotelControlRoomDetailDao.saveObj(hotelControlRoomDetail);
	}
	
	public void update (HotelControlRoomDetail hotelControlRoomDetail){
		super.setOptInfo(hotelControlRoomDetail, BaseService.OPERATION_UPDATE);
		hotelControlRoomDetailDao.updateObj(hotelControlRoomDetail);
	}
	
	public HotelControlRoomDetail getById(java.lang.Integer value) {
		return hotelControlRoomDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelControlRoomDetail obj = hotelControlRoomDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelControlRoomDetail> find(Page<HotelControlRoomDetail> page, HotelControlRoomDetail hotelControlRoomDetail) {
		DetachedCriteria dc = hotelControlRoomDetailDao.createDetachedCriteria();
		
	   	if(hotelControlRoomDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelControlRoomDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelControlRoomDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getHotelControlUuid())){
			dc.add(Restrictions.like("hotelControlUuid", "%"+hotelControlRoomDetail.getHotelControlUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getHotelControlDetailUuid())){
			dc.add(Restrictions.like("hotelControlDetailUuid", "%"+hotelControlRoomDetail.getHotelControlDetailUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getRoomUuid())){
			dc.add(Restrictions.like("roomUuid", "%"+hotelControlRoomDetail.getRoomUuid()+"%"));
		}
	   	if(hotelControlRoomDetail.getNight()!=null){
	   		dc.add(Restrictions.eq("night", hotelControlRoomDetail.getNight()));
	   	}
	   	if(hotelControlRoomDetail.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelControlRoomDetail.getSort()));
	   	}
	   	if(hotelControlRoomDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelControlRoomDetail.getWholesalerId()));
	   	}
	   	if(hotelControlRoomDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelControlRoomDetail.getCreateBy()));
	   	}
		if(hotelControlRoomDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelControlRoomDetail.getCreateDate()));
		}
	   	if(hotelControlRoomDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelControlRoomDetail.getUpdateBy()));
	   	}
		if(hotelControlRoomDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelControlRoomDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelControlRoomDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelControlRoomDetailDao.find(page, dc);
	}
	
	public List<HotelControlRoomDetail> find( HotelControlRoomDetail hotelControlRoomDetail) {
		DetachedCriteria dc = hotelControlRoomDetailDao.createDetachedCriteria();
		
	   	if(hotelControlRoomDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelControlRoomDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelControlRoomDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getHotelControlUuid())){
			dc.add(Restrictions.like("hotelControlUuid", "%"+hotelControlRoomDetail.getHotelControlUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getHotelControlDetailUuid())){
			dc.add(Restrictions.like("hotelControlDetailUuid", "%"+hotelControlRoomDetail.getHotelControlDetailUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getRoomUuid())){
			dc.add(Restrictions.like("roomUuid", "%"+hotelControlRoomDetail.getRoomUuid()+"%"));
		}
	   	if(hotelControlRoomDetail.getNight()!=null){
	   		dc.add(Restrictions.eq("night", hotelControlRoomDetail.getNight()));
	   	}
	   	if(hotelControlRoomDetail.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelControlRoomDetail.getSort()));
	   	}
	   	if(hotelControlRoomDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelControlRoomDetail.getWholesalerId()));
	   	}
	   	if(hotelControlRoomDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelControlRoomDetail.getCreateBy()));
	   	}
		if(hotelControlRoomDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelControlRoomDetail.getCreateDate()));
		}
	   	if(hotelControlRoomDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelControlRoomDetail.getUpdateBy()));
	   	}
		if(hotelControlRoomDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelControlRoomDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelControlRoomDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelControlRoomDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelControlRoomDetailDao.find(dc);
	}
	
	public HotelControlRoomDetail getByUuid(String uuid) {
		return hotelControlRoomDetailDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelControlRoomDetail hotelControlRoomDetail = getByUuid(uuid);
		hotelControlRoomDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelControlRoomDetail);
	}
	
	public List<HotelControlRoomDetail> getByHotelControlDetailUuid(String hotelControlDetailUuid){
		List<HotelControlRoomDetail> list = this.hotelControlRoomDetailDao.find("from HotelControlRoomDetail where hotelControlDetailUuid=? and delFlag=?", hotelControlDetailUuid, BaseEntity.DEL_FLAG_NORMAL);
		return list;
	}

}
