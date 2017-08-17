/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.island.dao.IslandDao;
import com.trekiz.admin.modules.island.entity.Island;
import com.trekiz.admin.modules.island.service.IslandService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class IslandServiceImpl  extends BaseService implements IslandService{
	@Autowired
	private IslandDao islandDao;
	@Autowired
	private HotelAnnexDao hotelAnnexDao;

	public void save (Island island){
		super.setOptInfo(island, BaseService.OPERATION_ADD);
		islandDao.saveObj(island);
	}
	
	public void update (Island island){
		super.setOptInfo(island, BaseService.OPERATION_UPDATE);
		islandDao.updateObj(island);
	}
	
	public Island getById(java.lang.Integer value) {
		return islandDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		Island obj = islandDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<Island> find(Page<Island> page, Island island) {
		DetachedCriteria dc = islandDao.createDetachedCriteria();
		
	   	if(island.getId()!=null){
	   		dc.add(Restrictions.eq("id", island.getId()));
	   	}
		if (StringUtils.isNotEmpty(island.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+island.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getIslandName())){
			dc.add(Restrictions.like("islandName", "%"+island.getIslandName()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getShortName())){
			dc.add(Restrictions.like("shortName", "%"+island.getShortName()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getSpelling())){
			dc.add(Restrictions.like("spelling", "%"+island.getSpelling()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getShortSpelling())){
			dc.add(Restrictions.like("shortSpelling", "%"+island.getShortSpelling()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getIslandNameEn())){
			dc.add(Restrictions.like("islandNameEn", "%"+island.getIslandNameEn()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getShortNameEn())){
			dc.add(Restrictions.like("shortNameEn", "%"+island.getShortNameEn()+"%"));
		}
	   	if(island.getPosition()!=null){
	   		dc.add(Restrictions.eq("position", island.getPosition()));
	   	}
		if (StringUtils.isNotEmpty(island.getCountry())){
			dc.add(Restrictions.like("country", "%"+island.getCountry()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getProvince())){
			dc.add(Restrictions.like("province", "%"+island.getProvince()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getCity())){
			dc.add(Restrictions.like("city", "%"+island.getCity()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getDistrict())){
			dc.add(Restrictions.like("district", "%"+island.getDistrict()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getRegion())){
			dc.add(Restrictions.like("region", "%"+island.getRegion()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getShortAddress())){
			dc.add(Restrictions.like("shortAddress", "%"+island.getShortAddress()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getType()) && !("-1".equals(island.getType()))){
			dc.add(Restrictions.like("type", "%"+island.getType()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getTopic())){
			dc.add(Restrictions.like("topic", "%"+island.getTopic()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getIslandWay())){
			dc.add(Restrictions.like("islandWay", "%"+island.getIslandWay()+"%"));
		}
	   	if(island.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", island.getSort()));
	   	}
		if (StringUtils.isNotEmpty(island.getDescription())){
			dc.add(Restrictions.like("description", "%"+island.getDescription()+"%"));
		}
	   	if(island.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", island.getCreateBy()));
	   	}
		if(island.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", island.getCreateDate()));
		}
	   	if(island.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", island.getUpdateBy()));
	   	}
		if(island.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", island.getUpdateDate()));
		}
	   	if(island.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", island.getStatus()));
	   	}
		if (StringUtils.isNotEmpty(island.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+island.getDelFlag()+"%"));
		}
	   	if(island.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", island.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(island.getLongitude())){
			dc.add(Restrictions.like("longitude", "%"+island.getLongitude()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getLatitude())){
			dc.add(Restrictions.like("latitude", "%"+island.getLatitude()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getIslandAddress())){
			dc.add(Restrictions.like("islandAddress", "%"+island.getIslandAddress()+"%"));
		}
		
		dc.addOrder(Order.asc("sort"));
		return islandDao.find(page, dc);
	}
	
	public List<Island> find( Island island) {
		DetachedCriteria dc = islandDao.createDetachedCriteria();
		
	   	if(island.getId()!=null){
	   		dc.add(Restrictions.eq("id", island.getId()));
	   	}
		if (StringUtils.isNotEmpty(island.getUuid())){
			dc.add(Restrictions.eq("uuid", island.getUuid()));
		}
		if (StringUtils.isNotEmpty(island.getIslandName())){
			dc.add(Restrictions.like("islandName", "%"+island.getIslandName()+"%"));
		}
		if (StringUtils.isNotEmpty(island.getShortName())){
			dc.add(Restrictions.eq("shortName", island.getShortName()));
		}
		if (StringUtils.isNotEmpty(island.getSpelling())){
			dc.add(Restrictions.eq("spelling", island.getSpelling()));
		}
		if (StringUtils.isNotEmpty(island.getShortSpelling())){
			dc.add(Restrictions.eq("shortSpelling", island.getShortSpelling()));
		}
		if (StringUtils.isNotEmpty(island.getIslandNameEn())){
			dc.add(Restrictions.eq("islandNameEn", island.getIslandNameEn()));
		}
		if (StringUtils.isNotEmpty(island.getShortNameEn())){
			dc.add(Restrictions.eq("shortNameEn", island.getShortNameEn()));
		}
	   	if(island.getPosition()!=null){
	   		dc.add(Restrictions.eq("position", island.getPosition()));
	   	}
		if (StringUtils.isNotEmpty(island.getCountry())){
			dc.add(Restrictions.eq("country", island.getCountry()));
		}
		if (StringUtils.isNotEmpty(island.getProvince())){
			dc.add(Restrictions.eq("province", island.getProvince()));
		}
		if (StringUtils.isNotEmpty(island.getCity())){
			dc.add(Restrictions.eq("city", island.getCity()));
		}
		if (StringUtils.isNotEmpty(island.getDistrict())){
			dc.add(Restrictions.eq("district", island.getDistrict()));
		}
		if (StringUtils.isNotEmpty(island.getRegion())){
			dc.add(Restrictions.eq("region", island.getRegion()));
		}
		if (StringUtils.isNotEmpty(island.getShortAddress())){
			dc.add(Restrictions.eq("shortAddress", island.getShortAddress()));
		}
		if (StringUtils.isNotEmpty(island.getType())){
			dc.add(Restrictions.eq("type", island.getType()));
		}
		if (StringUtils.isNotEmpty(island.getTopic())){
			dc.add(Restrictions.eq("topic", island.getTopic()));
		}
		if (StringUtils.isNotEmpty(island.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", island.getIslandWay()));
		}
	   	if(island.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", island.getSort()));
	   	}
		if (StringUtils.isNotEmpty(island.getDescription())){
			dc.add(Restrictions.eq("description", island.getDescription()));
		}
	   	if(island.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", island.getCreateBy()));
	   	}
		if(island.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", island.getCreateDate()));
		}
	   	if(island.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", island.getUpdateBy()));
	   	}
		if(island.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", island.getUpdateDate()));
		}
	   	if(island.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", island.getStatus()));
	   	}
		if (StringUtils.isNotEmpty(island.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", island.getDelFlag()));
		}
	   	if(island.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", island.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(island.getLongitude())){
			dc.add(Restrictions.eq("longitude", island.getLongitude()));
		}
		if (StringUtils.isNotEmpty(island.getLatitude())){
			dc.add(Restrictions.eq("latitude", island.getLatitude()));
		}
		if (StringUtils.isNotEmpty(island.getIslandAddress())){
			dc.add(Restrictions.eq("islandAddress", island.getIslandAddress()));
		}

		dc.addOrder(Order.asc("sort"));
		return islandDao.find(dc);
	}
	
	public Island getByUuid(String uuid) {
		return islandDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		Island island = getByUuid(uuid);
		island.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(island);
	}
	
	public boolean findIsExist(String uuid, String islandName, Long companyId) {
		StringBuffer sb = new StringBuffer("from Island island where island.uuid != ? and island.islandName = ? and island.wholesalerId = ? and island.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		
		List<Island> islands = islandDao.find(sb.toString(), uuid, islandName, companyId.intValue());
		
		if(islands == null || islands.size() == 0) {
			return false;
		}
		return true;
	}
	
	public void saveIsland(Island island, List<HotelAnnex> annexList) {
		super.setOptInfo(island, BaseService.OPERATION_ADD);
		islandDao.saveObj(island);
		
		if(annexList == null || annexList.isEmpty()) {
			return ;
		}
		
		for(HotelAnnex hotelAnnex : annexList) {
			super.setOptInfo(hotelAnnex, BaseService.OPERATION_ADD);
			hotelAnnex.setMainUuid(island.getUuid());
			hotelAnnex.setType(3);
			hotelAnnex.setWholesalerId(island.getWholesalerId());
			hotelAnnexDao.saveObj(hotelAnnex);
		}
	}
	
	public void updateIsland(Island island, List<HotelAnnex> annexList) {
		super.setOptInfo(island, BaseService.OPERATION_UPDATE);
		islandDao.updateObj(island);
		
		//同步更新酒店附件信息
		hotelAnnexDao.synDocInfo(island.getUuid(), 3, island.getWholesalerId(), annexList);
	}

	@Override
	public List<Island> findListByCompanyId(Integer companyId) {
		List<Island> islandList = new ArrayList<Island>();
		if(companyId != null){
			islandList = islandDao.findListByCompanyId(companyId);
		}
		return islandList;
	}
	
	public List<SysCompanyDictView> findIslandWaysByIslandUuid(String islandUuid) {
		return islandDao.findIslandWaysByIslandUuid(islandUuid);
	}
	
}
