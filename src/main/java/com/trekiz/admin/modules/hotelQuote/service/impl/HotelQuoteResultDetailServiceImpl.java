/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service.impl;

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
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteResultDetailDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResultDetail;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteResultDetailInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteResultDetailQuery;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteResultDetailService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuoteResultDetailServiceImpl  extends BaseService implements HotelQuoteResultDetailService{
	@Autowired
	private HotelQuoteResultDetailDao hotelQuoteResultDetailDao;

	public void save (HotelQuoteResultDetail hotelQuoteResultDetail){
		super.setOptInfo(hotelQuoteResultDetail, BaseService.OPERATION_ADD);
		hotelQuoteResultDetailDao.saveObj(hotelQuoteResultDetail);
	}
	
	public void save (HotelQuoteResultDetailInput hotelQuoteResultDetailInput){
		HotelQuoteResultDetail hotelQuoteResultDetail = hotelQuoteResultDetailInput.getHotelQuoteResultDetail();
		super.setOptInfo(hotelQuoteResultDetail, BaseService.OPERATION_ADD);
		hotelQuoteResultDetailDao.saveObj(hotelQuoteResultDetail);
	}
	
	public void update (HotelQuoteResultDetail hotelQuoteResultDetail){
		super.setOptInfo(hotelQuoteResultDetail, BaseService.OPERATION_UPDATE);
		hotelQuoteResultDetailDao.updateObj(hotelQuoteResultDetail);
	}
	
	public HotelQuoteResultDetail getById(java.lang.Integer value) {
		return hotelQuoteResultDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuoteResultDetail obj = hotelQuoteResultDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuoteResultDetail> find(Page<HotelQuoteResultDetail> page, HotelQuoteResultDetailQuery hotelQuoteResultDetailQuery) {
		DetachedCriteria dc = hotelQuoteResultDetailDao.createDetachedCriteria();
		
	   	if(hotelQuoteResultDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteResultDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteResultDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteResultDetailQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getHotelQuoteResultUuid())){
			dc.add(Restrictions.eq("hotelQuoteResultUuid", hotelQuoteResultDetailQuery.getHotelQuoteResultUuid()));
		}
	   	if(hotelQuoteResultDetailQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", hotelQuoteResultDetailQuery.getPrice()));
	   	}
	   	if(hotelQuoteResultDetailQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", hotelQuoteResultDetailQuery.getPriceType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getTypeUuid())){
			dc.add(Restrictions.eq("typeUuid", hotelQuoteResultDetailQuery.getTypeUuid()));
		}
	   	if(hotelQuoteResultDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteResultDetailQuery.getCreateBy()));
	   	}
		if(hotelQuoteResultDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteResultDetailQuery.getCreateDate()));
		}
	   	if(hotelQuoteResultDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteResultDetailQuery.getUpdateBy()));
	   	}
		if(hotelQuoteResultDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteResultDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteResultDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteResultDetailDao.find(page, dc);
	}
	
	public List<HotelQuoteResultDetail> find( HotelQuoteResultDetailQuery hotelQuoteResultDetailQuery) {
		DetachedCriteria dc = hotelQuoteResultDetailDao.createDetachedCriteria();
		
	   	if(hotelQuoteResultDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteResultDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteResultDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteResultDetailQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getHotelQuoteResultUuid())){
			dc.add(Restrictions.eq("hotelQuoteResultUuid", hotelQuoteResultDetailQuery.getHotelQuoteResultUuid()));
		}
	   	if(hotelQuoteResultDetailQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", hotelQuoteResultDetailQuery.getPrice()));
	   	}
	   	if(hotelQuoteResultDetailQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", hotelQuoteResultDetailQuery.getPriceType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getTypeUuid())){
			dc.add(Restrictions.eq("typeUuid", hotelQuoteResultDetailQuery.getTypeUuid()));
		}
	   	if(hotelQuoteResultDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteResultDetailQuery.getCreateBy()));
	   	}
		if(hotelQuoteResultDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteResultDetailQuery.getCreateDate()));
		}
	   	if(hotelQuoteResultDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteResultDetailQuery.getUpdateBy()));
	   	}
		if(hotelQuoteResultDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteResultDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteResultDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteResultDetailDao.find(dc);
	}
	
	public HotelQuoteResultDetail getByUuid(String uuid) {
		return hotelQuoteResultDetailDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuoteResultDetail hotelQuoteResultDetail = getByUuid(uuid);
		hotelQuoteResultDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuoteResultDetail);
	}

	@Override
	public List<HotelQuoteResultDetail> findByHotelQuoteUuid(String uuid) {
		String qlString = " from HotelQuoteResultDetail where delFlag='0' and hotelQuoteUuid=? order by typeUuid desc";
		return hotelQuoteResultDetailDao.find(qlString, uuid);
	}

	@Override
	public List<HotelQuoteResultDetail> findByHotelQuoteConditionUuid(
			String uuid) {
		String qlString = " from HotelQuoteResultDetail where delFlag='0' and hotelQuoteConditionUuid=? order by priceType asc,typeUuid asc,inDate asc";
		return hotelQuoteResultDetailDao.find(qlString, uuid);
	}
}
