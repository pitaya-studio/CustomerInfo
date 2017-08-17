/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.flight.service.impl;

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
import com.trekiz.admin.modules.flight.dao.FlightControlHotelDetailDao;
import com.trekiz.admin.modules.flight.entity.FlightControlHotelDetail;
import com.trekiz.admin.modules.flight.service.FlightControlHotelDetailService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class FlightControlHotelDetailServiceImpl  extends BaseService implements FlightControlHotelDetailService{
	@Autowired
	private FlightControlHotelDetailDao flightControlHotelDetailDao;

	public void save (FlightControlHotelDetail flightControlHotelDetail){
		super.setOptInfo(flightControlHotelDetail, BaseService.OPERATION_ADD);
		flightControlHotelDetailDao.saveObj(flightControlHotelDetail);
	}
	
	public void update (FlightControlHotelDetail flightControlHotelDetail){
		super.setOptInfo(flightControlHotelDetail, BaseService.OPERATION_UPDATE);
		flightControlHotelDetailDao.updateObj(flightControlHotelDetail);
	}
	
	public FlightControlHotelDetail getById(java.lang.Integer value) {
		return flightControlHotelDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		FlightControlHotelDetail obj = flightControlHotelDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<FlightControlHotelDetail> find(Page<FlightControlHotelDetail> page, FlightControlHotelDetail flightControlHotelDetail) {
		DetachedCriteria dc = flightControlHotelDetailDao.createDetachedCriteria();
		
	   	if(flightControlHotelDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", flightControlHotelDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+flightControlHotelDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getFlightControlUuid())){
			dc.add(Restrictions.like("flightControlUuid", "%"+flightControlHotelDetail.getFlightControlUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getFlightControlDetailUuid())){
			dc.add(Restrictions.like("flightControlDetailUuid", "%"+flightControlHotelDetail.getFlightControlDetailUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+flightControlHotelDetail.getHotelUuid()+"%"));
		}
	   	if(flightControlHotelDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", flightControlHotelDetail.getWholesalerId()));
	   	}
	   	if(flightControlHotelDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", flightControlHotelDetail.getCreateBy()));
	   	}
		if(flightControlHotelDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", flightControlHotelDetail.getCreateDate()));
		}
	   	if(flightControlHotelDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", flightControlHotelDetail.getUpdateBy()));
	   	}
		if(flightControlHotelDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", flightControlHotelDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+flightControlHotelDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return flightControlHotelDetailDao.find(page, dc);
	}
	
	public List<FlightControlHotelDetail> find( FlightControlHotelDetail flightControlHotelDetail) {
		DetachedCriteria dc = flightControlHotelDetailDao.createDetachedCriteria();
		
	   	if(flightControlHotelDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", flightControlHotelDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+flightControlHotelDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getFlightControlUuid())){
			dc.add(Restrictions.like("flightControlUuid", "%"+flightControlHotelDetail.getFlightControlUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getFlightControlDetailUuid())){
			dc.add(Restrictions.like("flightControlDetailUuid", "%"+flightControlHotelDetail.getFlightControlDetailUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+flightControlHotelDetail.getHotelUuid()+"%"));
		}
	   	if(flightControlHotelDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", flightControlHotelDetail.getWholesalerId()));
	   	}
	   	if(flightControlHotelDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", flightControlHotelDetail.getCreateBy()));
	   	}
		if(flightControlHotelDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", flightControlHotelDetail.getCreateDate()));
		}
	   	if(flightControlHotelDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", flightControlHotelDetail.getUpdateBy()));
	   	}
		if(flightControlHotelDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", flightControlHotelDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(flightControlHotelDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+flightControlHotelDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return flightControlHotelDetailDao.find(dc);
	}
	
	public FlightControlHotelDetail getByUuid(String uuid) {
		return flightControlHotelDetailDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		FlightControlHotelDetail flightControlHotelDetail = getByUuid(uuid);
		flightControlHotelDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(flightControlHotelDetail);
	}
	/**
	 * @author LiuXueLiang
	 */
	@Override
	public List<FlightControlHotelDetail> findByFlightControlDetailUuid(
			String flightControlDetailUuid) {
		List<FlightControlHotelDetail> list = this.flightControlHotelDetailDao.
				find("from FlightControlHotelDetail where flightControlDetailUuid=? and delFlag=?", flightControlDetailUuid,BaseEntity.DEL_FLAG_NORMAL);
		return list;
	}
	
	/**
	 * @author sy
	 */
	public List<FlightControlHotelDetail> getByFlightControlDetailUuid(String flightControlDetailUuid){
		
//		List<FlightControlHotelDetail> list = this.flightControlHotelDetailDao.find("from FlightControlHotelDetail where flightControlDetailUuid=? and delFlag=?",flightControlDetailUuid,BaseEntity.DEL_FLAG_NORMAL);
		return null;
	}
	
}
