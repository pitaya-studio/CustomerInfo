/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.dao.HotelContactDao;
import com.trekiz.admin.modules.hotel.dao.HotelDao;
import com.trekiz.admin.modules.hotel.dao.HotelStarDao;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelContact;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelServiceImpl  extends BaseService implements HotelService{
	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private HotelContactDao hotelContactDao;
	@Autowired
	private HotelAnnexDao hotelAnnexDao;
	@Autowired
	private HotelStarDao hotelStarDao;

	public void save (Hotel hotel,List<HotelContact> hcList,List<HotelAnnex> axList){
		try {
			super.setOptInfo(hotel, null);
			hotelDao.saveObj(hotel);
			if(hcList!=null&&hcList.size()>0){
				for(HotelContact hc:hcList){
					super.setOptInfo(hc, null);
					hc.setHotelUuid(hotel.getUuid());
					hotelContactDao.saveObj(hc);
				}
			}
			if(axList!=null&&axList.size()>0){
				for(HotelAnnex ha:axList){
					super.setOptInfo(ha, null);
					ha.setMainUuid(hotel.getUuid());
					ha.setType(1);
					ha.setWholesalerId(hotel.getWholesalerId());
					hotelAnnexDao.saveObj(ha);
				}
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存酒店及相关信息失败！" );
		}
	}
	
	public void update (Hotel hotel,List<HotelContact> hcList,List<HotelAnnex> axList){
		
		try {
			super.setOptInfo(hotel, "update");
			
			
			hotelDao.updateObj(hotel);

			hotelContactDao.delByHotelUuid(hotel.getUuid());
			for(HotelContact hc:hcList){
				super.setOptInfo(hc, null);
				hc.setHotelUuid(hotel.getUuid());
				hotelContactDao.saveObj(hc);
			}
			
			hotelAnnexDao.synDocInfo(hotel.getUuid(), 1, hotel.getWholesalerId(), axList);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存酒店及相关信息失败！" );
		}
	}
	
	@SuppressWarnings("unused")
	private Map<String,HotelAnnex> tranferList2Map(List<HotelAnnex> list){
		Map<String,HotelAnnex> map = new HashMap<String,HotelAnnex>();
		for(HotelAnnex ha:list){
			map.put(ha.getDocId().toString(), ha);
		}
		return map;
	}
	
	public Hotel getById(java.lang.Integer value) {
		return hotelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		Hotel obj = hotelDao.getById(value);
		obj.setDelFlag("1");
		hotelDao.updateObj(obj);
	}	
	public void removeByUuid(java.lang.String value){
		Hotel obj = hotelDao.getByUuid(value);
		if(obj!=null && obj.getWholesalerId().toString().equals(UserUtils.getCompanyIdForData().toString())){
			obj.setDelFlag("1");
			hotelDao.updateObj(obj);
		}
	}	
	
	public Hotel getByUuid(String value){
		return hotelDao.getByUuid(value);
	}
	
	public Page<Hotel> find(Page<Hotel> page, Hotel hotel) {
		DetachedCriteria dc = hotelDao.createDetachedCriteria();
		
	   	if(hotel.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotel.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotel.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getNameCn())){
			dc.add(Restrictions.like("nameCn", "%"+hotel.getNameCn()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getShortNameCn())){
			dc.add(Restrictions.like("shortNameCn", "%"+hotel.getShortNameCn()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getNameEn())){
			dc.add(Restrictions.like("nameEn", "%"+hotel.getNameEn()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getShortNameEn())){
			dc.add(Restrictions.like("shortNameEn", "%"+hotel.getShortNameEn()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getSpelling())){
			dc.add(Restrictions.like("spelling", "%"+hotel.getSpelling()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getShortSpelling())){
			dc.add(Restrictions.like("shortSpelling", "%"+hotel.getShortSpelling()+"%"));
		}
	   	if(hotel.getPosition()!=null){
	   		dc.add(Restrictions.eq("position", hotel.getPosition()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getCountry())){
			dc.add(Restrictions.like("country", "%"+hotel.getCountry()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getProvince())){
			dc.add(Restrictions.like("province", "%"+hotel.getProvince()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getCity())){
			dc.add(Restrictions.like("city", "%"+hotel.getCity()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getDistrict())){
			dc.add(Restrictions.like("district", "%"+hotel.getDistrict()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getRegion())){
			dc.add(Restrictions.like("region", "%"+hotel.getRegion()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getShortAddress())){
			dc.add(Restrictions.like("shortAddress", "%"+hotel.getShortAddress()+"%"));
		}
	   	if(hotel.getAreaType()!=null){
	   		dc.add(Restrictions.eq("areaType", hotel.getAreaType()));
	   	}
	   	if(StringUtils.isNotEmpty(hotel.getType()) && !("-1".equals(hotel.getType()))){
	   		dc.add(Restrictions.eq("type", hotel.getType()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getBrand())){
			dc.add(Restrictions.like("brand", "%"+hotel.getBrand()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getStar())){
			dc.add(Restrictions.like("star", "%"+hotel.getStar()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getFloor())){
			dc.add(Restrictions.like("floor", "%"+hotel.getFloor()+"%"));
		}
		if(hotel.getOpeningDate()!=null){
			dc.add(Restrictions.eq("openingDate", hotel.getOpeningDate()));
		}
		if(hotel.getLastDecoDate()!=null){
			dc.add(Restrictions.eq("lastDecoDate", hotel.getLastDecoDate()));
		}
		if (StringUtils.isNotEmpty(hotel.getTopic())){
			dc.add(Restrictions.like("topic", "%"+hotel.getTopic()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getFacilities())){
			dc.add(Restrictions.like("facilities", "%"+hotel.getFacilities()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getFeature())){
			dc.add(Restrictions.like("feature", "%"+hotel.getFeature()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getAddress())){
			dc.add(Restrictions.like("address", "%"+hotel.getAddress()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getWebsite())){
			dc.add(Restrictions.like("website", "%"+hotel.getWebsite()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getTelephone())){
			dc.add(Restrictions.like("telephone", "%"+hotel.getTelephone()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getFax())){
			dc.add(Restrictions.like("fax", "%"+hotel.getFax()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargePerson())){
			dc.add(Restrictions.like("chargePerson", "%"+hotel.getChargePerson()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargeMobile())){
			dc.add(Restrictions.like("chargeMobile", "%"+hotel.getChargeMobile()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargeTelephone())){
			dc.add(Restrictions.like("chargeTelephone", "%"+hotel.getChargeTelephone()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargeFax())){
			dc.add(Restrictions.like("chargeFax", "%"+hotel.getChargeFax()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargeEmail())){
			dc.add(Restrictions.like("chargeEmail", "%"+hotel.getChargeEmail()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getRemark())){
			dc.add(Restrictions.like("remark", "%"+hotel.getRemark()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotel.getDescription()+"%"));
		}
		if(hotel.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotel.getInDate()));
		}
		if(hotel.getOutDate()!=null){
			dc.add(Restrictions.eq("outDate", hotel.getOutDate()));
		}
		if (StringUtils.isNotEmpty(hotel.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotel.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotel.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotel.getIslandWay()));
		}
	   	if(hotel.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotel.getCreateBy()));
	   	}
		if(hotel.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotel.getCreateDate()));
		}
	   	if(hotel.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotel.getUpdateBy()));
	   	}
		if(hotel.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotel.getUpdateDate()));
		}
	   	if(hotel.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", hotel.getStatus()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotel.getDelFlag()+"%"));
		}
		if(hotel.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotel.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getLongitude())){
			dc.add(Restrictions.like("longitude", "%"+hotel.getLongitude()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getLatitude())){
			dc.add(Restrictions.like("latitude", "%"+hotel.getLatitude()+"%"));
		}
		
		return hotelDao.find(page, dc);
	}
	
	public List<Hotel> find( Hotel hotel) {
		DetachedCriteria dc = hotelDao.createDetachedCriteria();
		
	   	if(hotel.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotel.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotel.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getNameCn())){
			dc.add(Restrictions.like("nameCn", "%"+hotel.getNameCn()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getShortNameCn())){
			dc.add(Restrictions.like("shortNameCn", "%"+hotel.getShortNameCn()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getNameEn())){
			dc.add(Restrictions.like("nameEn", "%"+hotel.getNameEn()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getShortNameEn())){
			dc.add(Restrictions.like("shortNameEn", "%"+hotel.getShortNameEn()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getSpelling())){
			dc.add(Restrictions.like("spelling", "%"+hotel.getSpelling()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getShortSpelling())){
			dc.add(Restrictions.like("shortSpelling", "%"+hotel.getShortSpelling()+"%"));
		}
	   	if(hotel.getPosition()!=null){
	   		dc.add(Restrictions.eq("position", hotel.getPosition()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getCountry())){
			dc.add(Restrictions.like("country", "%"+hotel.getCountry()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getProvince())){
			dc.add(Restrictions.like("province", "%"+hotel.getProvince()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getCity())){
			dc.add(Restrictions.like("city", "%"+hotel.getCity()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getDistrict())){
			dc.add(Restrictions.like("district", "%"+hotel.getDistrict()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getRegion())){
			dc.add(Restrictions.like("region", "%"+hotel.getRegion()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getShortAddress())){
			dc.add(Restrictions.like("shortAddress", "%"+hotel.getShortAddress()+"%"));
		}
	   	if(hotel.getAreaType()!=null){
	   		dc.add(Restrictions.eq("areaType", hotel.getAreaType()));
	   	}
	   	if(hotel.getType()!=null){
	   		dc.add(Restrictions.eq("type", hotel.getType()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getBrand())){
			dc.add(Restrictions.like("brand", "%"+hotel.getBrand()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getStar())){
			dc.add(Restrictions.like("star", "%"+hotel.getStar()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getFloor())){
			dc.add(Restrictions.like("floor", "%"+hotel.getFloor()+"%"));
		}
		if(hotel.getOpeningDate()!=null){
			dc.add(Restrictions.eq("openingDate", hotel.getOpeningDate()));
		}
		if(hotel.getLastDecoDate()!=null){
			dc.add(Restrictions.eq("lastDecoDate", hotel.getLastDecoDate()));
		}
		if (StringUtils.isNotEmpty(hotel.getTopic())){
			dc.add(Restrictions.like("topic", "%"+hotel.getTopic()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getFacilities())){
			dc.add(Restrictions.like("facilities", "%"+hotel.getFacilities()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getFeature())){
			dc.add(Restrictions.like("feature", "%"+hotel.getFeature()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getAddress())){
			dc.add(Restrictions.like("address", "%"+hotel.getAddress()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getWebsite())){
			dc.add(Restrictions.like("website", "%"+hotel.getWebsite()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getTelephone())){
			dc.add(Restrictions.like("telephone", "%"+hotel.getTelephone()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getFax())){
			dc.add(Restrictions.like("fax", "%"+hotel.getFax()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargePerson())){
			dc.add(Restrictions.like("chargePerson", "%"+hotel.getChargePerson()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargeMobile())){
			dc.add(Restrictions.like("chargeMobile", "%"+hotel.getChargeMobile()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargeTelephone())){
			dc.add(Restrictions.like("chargeTelephone", "%"+hotel.getChargeTelephone()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargeFax())){
			dc.add(Restrictions.like("chargeFax", "%"+hotel.getChargeFax()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getChargeEmail())){
			dc.add(Restrictions.like("chargeEmail", "%"+hotel.getChargeEmail()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getRemark())){
			dc.add(Restrictions.like("remark", "%"+hotel.getRemark()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotel.getDescription()+"%"));
		}
		if(hotel.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotel.getInDate()));
		}
		if(hotel.getOutDate()!=null){
			dc.add(Restrictions.eq("outDate", hotel.getOutDate()));
		}
		if (StringUtils.isNotEmpty(hotel.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotel.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotel.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotel.getIslandWay()));
		}
	   	if(hotel.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotel.getCreateBy()));
	   	}
		if(hotel.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotel.getCreateDate()));
		}
	   	if(hotel.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotel.getUpdateBy()));
	   	}
		if(hotel.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotel.getUpdateDate()));
		}
	   	if(hotel.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", hotel.getStatus()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotel.getDelFlag()+"%"));
		}
		if(hotel.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotel.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(hotel.getLongitude())){
			dc.add(Restrictions.like("longitude", "%"+hotel.getLongitude()+"%"));
		}
		if (StringUtils.isNotEmpty(hotel.getLatitude())){
			dc.add(Restrictions.like("latitude", "%"+hotel.getLatitude()+"%"));
		}
		return hotelDao.find(dc);
	}
	
	public List<Hotel> findByIslandUuidAndCompany(String islandUuid, int companyId) {
		
		return hotelDao.findByIslandUuidAndCompany(islandUuid, companyId);
	}
	
	public Integer getHotelStarValByHotelUuid(String hotelUuid) {
		Hotel hotel = this.getByUuid(hotelUuid);
		if(hotel == null || StringUtils.isEmpty(hotel.getStar())) {
			return null;
		}
		
		HotelStar hotelStar = hotelStarDao.getByUuid(hotel.getStar());
		if(hotelStar != null) {
			return hotelStar.getValue();
		}
		return null;
	}
	
	public String getHotelGroupByUuid(String hotelUuid) {
		if(StringUtils.isEmpty(hotelUuid)) {
			return "";
		}
		return hotelDao.getHotelGroupByUuid(hotelUuid);
	}
	
	/**
	 * 获取批发商下的所有酒店
	*<p>Title: findByCompany</p>
	* @return List<Hotel> 返回类型
	* @author majiancheng
	* @date 2015-7-18 下午3:23:30
	* @throws
	 */
	public List<Hotel> findHotelsByCompanyId(Integer companyId) {
		return hotelDao.findHotelsByCompanyId(companyId);
	}
	
	public List<Hotel> findHotelsByCompanyIdAndCountry(Integer companyId, String country) {
		return hotelDao.findHotelsByCompanyIdAndCountry(companyId, country);
	}
}
