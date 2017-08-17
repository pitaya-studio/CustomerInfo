/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service.impl;

import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteResultDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteResult;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteResultInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteResultQuery;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteResultService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuoteResultServiceImpl  extends BaseService implements HotelQuoteResultService{
	@Autowired
	private HotelQuoteResultDao hotelQuoteResultDao;

	public void save (HotelQuoteResult hotelQuoteResult){
		super.setOptInfo(hotelQuoteResult, BaseService.OPERATION_ADD);
		hotelQuoteResultDao.saveObj(hotelQuoteResult);
	}
	
	public void save (HotelQuoteResultInput hotelQuoteResultInput){
		HotelQuoteResult hotelQuoteResult = hotelQuoteResultInput.getHotelQuoteResult();
		super.setOptInfo(hotelQuoteResult, BaseService.OPERATION_ADD);
		hotelQuoteResultDao.saveObj(hotelQuoteResult);
	}
	
	public void update (HotelQuoteResult hotelQuoteResult){
		super.setOptInfo(hotelQuoteResult, BaseService.OPERATION_UPDATE);
		hotelQuoteResultDao.updateObj(hotelQuoteResult);
	}
	
	public HotelQuoteResult getById(java.lang.Integer value) {
		return hotelQuoteResultDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuoteResult obj = hotelQuoteResultDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuoteResult> find(Page<HotelQuoteResult> page, HotelQuoteResultQuery hotelQuoteResultQuery) {
		DetachedCriteria dc = hotelQuoteResultDao.createDetachedCriteria();
		
	   	if(hotelQuoteResultQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteResultQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteResultQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteResultQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getHotelQuoteConditionUuid())){
			dc.add(Restrictions.eq("hotelQuoteConditionUuid", hotelQuoteResultQuery.getHotelQuoteConditionUuid()));
		}
	   	if(hotelQuoteResultQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", hotelQuoteResultQuery.getPrice()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getPreferentialPrice())){
			dc.add(Restrictions.eq("preferentialPrice", hotelQuoteResultQuery.getPreferentialPrice()));
		}
	   	if(hotelQuoteResultQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", hotelQuoteResultQuery.getPriceType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getTypeUuid())){
			dc.add(Restrictions.eq("typeUuid", hotelQuoteResultQuery.getTypeUuid()));
		}
	   	if(hotelQuoteResultQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteResultQuery.getCreateBy()));
	   	}
		if(hotelQuoteResultQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteResultQuery.getCreateDate()));
		}
	   	if(hotelQuoteResultQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteResultQuery.getUpdateBy()));
	   	}
		if(hotelQuoteResultQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteResultQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteResultQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteResultDao.find(page, dc);
	}
	
	public List<HotelQuoteResult> find( HotelQuoteResultQuery hotelQuoteResultQuery) {
		DetachedCriteria dc = hotelQuoteResultDao.createDetachedCriteria();
		
	   	if(hotelQuoteResultQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteResultQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteResultQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteResultQuery.getHotelQuoteUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getHotelQuoteConditionUuid())){
			dc.add(Restrictions.eq("hotelQuoteConditionUuid", hotelQuoteResultQuery.getHotelQuoteConditionUuid()));
		}
	   	if(hotelQuoteResultQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", hotelQuoteResultQuery.getPrice()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getPreferentialPrice())){
			dc.add(Restrictions.eq("preferentialPrice", hotelQuoteResultQuery.getPreferentialPrice()));
		}
	   	if(hotelQuoteResultQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", hotelQuoteResultQuery.getPriceType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getTypeUuid())){
			dc.add(Restrictions.eq("typeUuid", hotelQuoteResultQuery.getTypeUuid()));
		}
	   	if(hotelQuoteResultQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteResultQuery.getCreateBy()));
	   	}
		if(hotelQuoteResultQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteResultQuery.getCreateDate()));
		}
	   	if(hotelQuoteResultQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteResultQuery.getUpdateBy()));
	   	}
		if(hotelQuoteResultQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteResultQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteResultQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteResultQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteResultDao.find(dc);
	}
	
	public HotelQuoteResult getByUuid(String uuid) {
		return hotelQuoteResultDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuoteResult hotelQuoteResult = getByUuid(uuid);
		hotelQuoteResult.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuoteResult);
	}

	@Override
	public List<HotelQuoteResult> findByHotelQuoteUuid(String uuid) {
		String qlString = " from HotelQuoteResult where delFlag='0' and hotelQuoteConditionUuid=? order by typeUuid desc";
		return hotelQuoteResultDao.find(qlString, uuid);
	}
	
	public List<HotelQuoteResult> findByHotelQuoteConditionUuid(String uuid,String hotelUuid) {
		String sqlString = "select id,uuid,hotel_quote_uuid,hotel_pl_uuid,hotel_quote_condition_uuid,price, "+
				           "preferential_price,price_type,type_uuid,createBy,createDate,updateBy, "+
				           "updateDate,delFlag "+
				           "from hotel_quote_result where delFlag='0' and hotel_quote_condition_uuid = ?  and type_uuid " +
				           "in (select tt.uuid from traveler_type tt join hotel_traveler_type_relation httr on tt.uuid = httr.traveler_type_uuid "+ 
		                   "where tt.status='1' and tt.delFlag='0' and httr.delFlag='0' and httr.hotel_uuid = ? )"+
				           "order by type_uuid desc";
		return hotelQuoteResultDao.findBySql(sqlString, HotelQuoteResult.class, uuid,hotelUuid);
	}

	@Override
	public List<Map<String,Object>> getByQuoteUuid(String quoteUuid) {
		
		return hotelQuoteResultDao.getByQuoteUuid(quoteUuid);
	}

	@Override
	public List<HotelQuoteResult> findByHotelQuoteConditionUuid(String uuid) {
		String sql="from HotelQuoteResult where delFlag='0' and hotelQuoteConditionUuid=? order by priceType asc,typeUuid asc";
		return hotelQuoteResultDao.find(sql, uuid);
	}
	
	@Override
	public Object getDates(String uuid){
		return hotelQuoteResultDao.getDates(uuid);
	}
}
