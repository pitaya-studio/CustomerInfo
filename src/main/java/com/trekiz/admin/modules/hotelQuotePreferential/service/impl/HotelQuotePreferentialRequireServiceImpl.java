/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service.impl;

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
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRequireDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRequire;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialRequireInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialRequireQuery;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialRequireService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialRequireServiceImpl  extends BaseService implements HotelQuotePreferentialRequireService{
	@Autowired
	private HotelQuotePreferentialRequireDao hotelQuotePreferentialRequireDao;

	public void save (HotelQuotePreferentialRequire hotelQuotePreferentialRequire){
		super.setOptInfo(hotelQuotePreferentialRequire, BaseService.OPERATION_ADD);
		hotelQuotePreferentialRequireDao.saveObj(hotelQuotePreferentialRequire);
	}
	
	public void save (HotelQuotePreferentialRequireInput hotelQuotePreferentialRequireInput){
		HotelQuotePreferentialRequire hotelQuotePreferentialRequire = hotelQuotePreferentialRequireInput.getHotelQuotePreferentialRequire();
		super.setOptInfo(hotelQuotePreferentialRequire, BaseService.OPERATION_ADD);
		hotelQuotePreferentialRequireDao.saveObj(hotelQuotePreferentialRequire);
	}
	
	public void update (HotelQuotePreferentialRequire hotelQuotePreferentialRequire){
		super.setOptInfo(hotelQuotePreferentialRequire, BaseService.OPERATION_UPDATE);
		hotelQuotePreferentialRequireDao.updateObj(hotelQuotePreferentialRequire);
	}
	
	public HotelQuotePreferentialRequire getById(java.lang.Integer value) {
		return hotelQuotePreferentialRequireDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuotePreferentialRequire obj = hotelQuotePreferentialRequireDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuotePreferentialRequire> find(Page<HotelQuotePreferentialRequire> page, HotelQuotePreferentialRequireQuery hotelQuotePreferentialRequireQuery) {
		DetachedCriteria dc = hotelQuotePreferentialRequireDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialRequireQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialRequireQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialRequireQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialRequireQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialRequireQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialRequireQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialRequireQuery.getHotelQuotePreferentialUuid()));
		}
	   	if(hotelQuotePreferentialRequireQuery.getBookingNights()!=null){
	   		dc.add(Restrictions.eq("bookingNights", hotelQuotePreferentialRequireQuery.getBookingNights()));
	   	}
	   	if(hotelQuotePreferentialRequireQuery.getBookingNumbers()!=null){
	   		dc.add(Restrictions.eq("bookingNumbers", hotelQuotePreferentialRequireQuery.getBookingNumbers()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getNotApplicableDate())){
			dc.add(Restrictions.eq("notApplicableDate", hotelQuotePreferentialRequireQuery.getNotApplicableDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getNotApplicableRoom())){
			dc.add(Restrictions.eq("notApplicableRoom", hotelQuotePreferentialRequireQuery.getNotApplicableRoom()));
		}
	   	if(hotelQuotePreferentialRequireQuery.getApplicableThirdPerson()!=null){
	   		dc.add(Restrictions.eq("applicableThirdPerson", hotelQuotePreferentialRequireQuery.getApplicableThirdPerson()));
	   	}
	   	if(hotelQuotePreferentialRequireQuery.getIsSuperposition()!=null){
	   		dc.add(Restrictions.eq("isSuperposition", hotelQuotePreferentialRequireQuery.getIsSuperposition()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelQuotePreferentialRequireQuery.getMemo()));
		}
	   	if(hotelQuotePreferentialRequireQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialRequireQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialRequireQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialRequireQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialRequireQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialRequireQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialRequireQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialRequireQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialRequireQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialRequireDao.find(page, dc);
	}
	
	public List<HotelQuotePreferentialRequire> find( HotelQuotePreferentialRequireQuery hotelQuotePreferentialRequireQuery) {
		DetachedCriteria dc = hotelQuotePreferentialRequireDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialRequireQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialRequireQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialRequireQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialRequireQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialRequireQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialRequireQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getHotelQuotePreferentialUuid())){
			dc.add(Restrictions.eq("hotelQuotePreferentialUuid", hotelQuotePreferentialRequireQuery.getHotelQuotePreferentialUuid()));
		}
	   	if(hotelQuotePreferentialRequireQuery.getBookingNights()!=null){
	   		dc.add(Restrictions.eq("bookingNights", hotelQuotePreferentialRequireQuery.getBookingNights()));
	   	}
	   	if(hotelQuotePreferentialRequireQuery.getBookingNumbers()!=null){
	   		dc.add(Restrictions.eq("bookingNumbers", hotelQuotePreferentialRequireQuery.getBookingNumbers()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getNotApplicableDate())){
			dc.add(Restrictions.eq("notApplicableDate", hotelQuotePreferentialRequireQuery.getNotApplicableDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getNotApplicableRoom())){
			dc.add(Restrictions.eq("notApplicableRoom", hotelQuotePreferentialRequireQuery.getNotApplicableRoom()));
		}
	   	if(hotelQuotePreferentialRequireQuery.getApplicableThirdPerson()!=null){
	   		dc.add(Restrictions.eq("applicableThirdPerson", hotelQuotePreferentialRequireQuery.getApplicableThirdPerson()));
	   	}
	   	if(hotelQuotePreferentialRequireQuery.getIsSuperposition()!=null){
	   		dc.add(Restrictions.eq("isSuperposition", hotelQuotePreferentialRequireQuery.getIsSuperposition()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelQuotePreferentialRequireQuery.getMemo()));
		}
	   	if(hotelQuotePreferentialRequireQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialRequireQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialRequireQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialRequireQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialRequireQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialRequireQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialRequireQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialRequireQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialRequireQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialRequireQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialRequireDao.find(dc);
	}
	
	public HotelQuotePreferentialRequire getByUuid(String uuid) {
		return hotelQuotePreferentialRequireDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuotePreferentialRequire hotelQuotePreferentialRequire = getByUuid(uuid);
		hotelQuotePreferentialRequire.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuotePreferentialRequire);
	}
}
