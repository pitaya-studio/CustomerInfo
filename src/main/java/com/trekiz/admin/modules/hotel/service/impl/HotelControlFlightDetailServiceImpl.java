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
import com.trekiz.admin.modules.hotel.dao.HotelControlFlightDetailDao;
import com.trekiz.admin.modules.hotel.entity.HotelControlFlightDetail;
import com.trekiz.admin.modules.hotel.service.HotelControlFlightDetailService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelControlFlightDetailServiceImpl  extends BaseService implements HotelControlFlightDetailService{
	@Autowired
	private HotelControlFlightDetailDao hotelControlFlightDetailDao;

	public void save (HotelControlFlightDetail hotelControlFlightDetail){
		super.setOptInfo(hotelControlFlightDetail, BaseService.OPERATION_ADD);
		hotelControlFlightDetailDao.saveObj(hotelControlFlightDetail);
	}
	
	public void update (HotelControlFlightDetail hotelControlFlightDetail){
		super.setOptInfo(hotelControlFlightDetail, BaseService.OPERATION_UPDATE);
		hotelControlFlightDetailDao.updateObj(hotelControlFlightDetail);
	}
	
	public HotelControlFlightDetail getById(java.lang.Integer value) {
		return hotelControlFlightDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelControlFlightDetail obj = hotelControlFlightDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelControlFlightDetail> find(Page<HotelControlFlightDetail> page, HotelControlFlightDetail hotelControlFlightDetail) {
		DetachedCriteria dc = hotelControlFlightDetailDao.createDetachedCriteria();
		
	   	if(hotelControlFlightDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelControlFlightDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelControlFlightDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getHotelControlUuid())){
			dc.add(Restrictions.like("hotelControlUuid", "%"+hotelControlFlightDetail.getHotelControlUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getHotelControlDetailUuid())){
			dc.add(Restrictions.like("hotelControlDetailUuid", "%"+hotelControlFlightDetail.getHotelControlDetailUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getAirline())){
			dc.add(Restrictions.like("airline", "%"+hotelControlFlightDetail.getAirline()+"%"));
		}
	   	if(hotelControlFlightDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelControlFlightDetail.getWholesalerId()));
	   	}
	   	if(hotelControlFlightDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelControlFlightDetail.getCreateBy()));
	   	}
		if(hotelControlFlightDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelControlFlightDetail.getCreateDate()));
		}
	   	if(hotelControlFlightDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelControlFlightDetail.getUpdateBy()));
	   	}
		if(hotelControlFlightDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelControlFlightDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelControlFlightDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelControlFlightDetailDao.find(page, dc);
	}
	
	public List<HotelControlFlightDetail> find( HotelControlFlightDetail hotelControlFlightDetail) {
		DetachedCriteria dc = hotelControlFlightDetailDao.createDetachedCriteria();
		
	   	if(hotelControlFlightDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelControlFlightDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelControlFlightDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getHotelControlUuid())){
			dc.add(Restrictions.like("hotelControlUuid", "%"+hotelControlFlightDetail.getHotelControlUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getHotelControlDetailUuid())){
			dc.add(Restrictions.like("hotelControlDetailUuid", "%"+hotelControlFlightDetail.getHotelControlDetailUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getAirline())){
			dc.add(Restrictions.like("airline", "%"+hotelControlFlightDetail.getAirline()+"%"));
		}
	   	if(hotelControlFlightDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelControlFlightDetail.getWholesalerId()));
	   	}
	   	if(hotelControlFlightDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelControlFlightDetail.getCreateBy()));
	   	}
		if(hotelControlFlightDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelControlFlightDetail.getCreateDate()));
		}
	   	if(hotelControlFlightDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelControlFlightDetail.getUpdateBy()));
	   	}
		if(hotelControlFlightDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelControlFlightDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelControlFlightDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelControlFlightDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelControlFlightDetailDao.find(dc);
	}
	
	public HotelControlFlightDetail getByUuid(String uuid) {
		return hotelControlFlightDetailDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelControlFlightDetail hotelControlFlightDetail = getByUuid(uuid);
		hotelControlFlightDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelControlFlightDetail);
	}
	public List<HotelControlFlightDetail> getByHotelControlDetailUuid(String hotelControlDetailUuid){
		List<HotelControlFlightDetail> list = this.hotelControlFlightDetailDao.find("from HotelControlFlightDetail where hotelControlDetailUuid=? and delFlag=?", hotelControlDetailUuid, BaseEntity.DEL_FLAG_NORMAL);
		return list;
	}
}
