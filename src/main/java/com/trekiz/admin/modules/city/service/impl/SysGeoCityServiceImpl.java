/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.city.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.city.dao.SysGeoCityDao;
import com.trekiz.admin.modules.city.entity.SysGeoCity;
import com.trekiz.admin.modules.city.service.SysGeoCityService;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.region.entity.SysRegion;
import com.trekiz.admin.modules.region.service.SysRegionService;
import com.trekiz.admin.modules.sys.entity.UserDefineDict;
import com.trekiz.admin.modules.sys.repository.UserDefineDictDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SysGeoCityServiceImpl  extends BaseService implements SysGeoCityService{
	@Autowired
	private SysGeoCityDao sysGeoCityDao;
	@Autowired
	private SysGeographyService sysGeographyService;
	@Autowired
	private SysRegionService sysRegionService;
	@Autowired
	private UserDefineDictDao userDefineDictDao;

	public void save (SysGeoCity sysGeoCity){
		super.setOptInfo(sysGeoCity, BaseService.OPERATION_ADD);
		sysGeoCityDao.saveObj(sysGeoCity);
	}
	
	public void update (SysGeoCity sysGeoCity){
		super.setOptInfo(sysGeoCity, BaseService.OPERATION_UPDATE);
		sysGeoCityDao.updateObj(sysGeoCity);
	}
	
	public SysGeoCity getById(java.lang.Integer value) {
		return sysGeoCityDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		SysGeoCity obj = sysGeoCityDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<SysGeoCity> find(Page<SysGeoCity> page, SysGeoCity sysGeoCity) {
		DetachedCriteria dc = sysGeoCityDao.createDetachedCriteria();
		
	   	if(sysGeoCity.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysGeoCity.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoCity.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+sysGeoCity.getUuid()+"%"));
		}
	   	if(sysGeoCity.getGeoId()!=null){
	   		dc.add(Restrictions.eq("geoId", sysGeoCity.getGeoId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoCity.getGeoUuid())){
			dc.add(Restrictions.like("geoUuid", "%"+sysGeoCity.getGeoUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(sysGeoCity.getType())){
			dc.add(Restrictions.like("type", "%"+sysGeoCity.getType()+"%"));
		}
	   	if(sysGeoCity.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysGeoCity.getCreateBy()));
	   	}
		if(sysGeoCity.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysGeoCity.getCreateDate()));
		}
	   	if(sysGeoCity.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysGeoCity.getUpdateBy()));
	   	}
		if(sysGeoCity.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysGeoCity.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysGeoCity.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysGeoCity.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysGeoCityDao.find(page, dc);
	}
	
	public List<SysGeoCity> find( SysGeoCity sysGeoCity) {
		DetachedCriteria dc = sysGeoCityDao.createDetachedCriteria();
		
	   	if(sysGeoCity.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysGeoCity.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoCity.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+sysGeoCity.getUuid()+"%"));
		}
	   	if(sysGeoCity.getGeoId()!=null){
	   		dc.add(Restrictions.eq("geoId", sysGeoCity.getGeoId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoCity.getGeoUuid())){
			dc.add(Restrictions.like("geoUuid", "%"+sysGeoCity.getGeoUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(sysGeoCity.getType())){
			dc.add(Restrictions.like("type", "%"+sysGeoCity.getType()+"%"));
		}
	   	if(sysGeoCity.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysGeoCity.getCreateBy()));
	   	}
		if(sysGeoCity.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysGeoCity.getCreateDate()));
		}
	   	if(sysGeoCity.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysGeoCity.getUpdateBy()));
	   	}
		if(sysGeoCity.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysGeoCity.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysGeoCity.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysGeoCity.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysGeoCityDao.find(dc);
	}
		
	public List<Map<String,Object>> getGeoCityList(String type){
		String sql = "SELECT  sgc.geo_id as id,sgc.type as type ,sg.name_cn as name, sgc.id as sgcId FROM sys_geo_city  sgc LEFT JOIN sys_geography  sg  " +
				" ON  sgc.geo_id=sg.id  AND sg.delFlag ='0'  WHERE sgc.delFlag ='0' AND sgc.type='"+type+"'" +
				"  AND sgc.wholesaler_id ="+UserUtils.getCompanyIdForData();		
		return sysGeoCityDao.findBySql(sql, Map.class);
	}

	@Override
	public void updateAreas(Integer[] startCityId, Integer[] transCityId,
			Integer[] leaveCityId, Integer[] interCityId) {
		updateAreasByType(startCityId,"from_area");//出发城市
		updateAreasByType(transCityId,"arrivals_area");//到达城市
		updateAreasByType(leaveCityId,"out_area");//离境城市
		updateAreasByType(interCityId,"Intermodal_area");//联运城市
	};
	
	public void updateAreasByType(Integer[] areaIds,  String type){
		List<Map<String,Object>> areaList = getGeoCityList(type);
		if(areaIds==null){
			for(Map<String,Object> map : areaList){
				this.removeById((Integer)map.get("sgcId"));
			}
			return;
		}
		for( int i =0;i<areaIds.length;i++){
			for(Map<String,Object> map : areaList){
			     if(areaIds[i].equals((Integer)(map.get("id")))){
			    	 areaIds[i]=-1;
			    	 areaList.remove(map);
			    	 break;
			     }
			}
		}
		//新增
		for(int areaId:areaIds){
			if(areaId ==-1){
				continue;
			}
			SysGeoCity sysGeoCity = new SysGeoCity();
			sysGeoCity.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			sysGeoCity.setGeoId(areaId);
			sysGeoCity.setGeoUuid(sysGeographyService.getById(areaId).getUuid());
			sysGeoCity.setType(type);
			sysGeoCity.setCreateBy(UserUtils.getUser().getId().intValue());
			sysGeoCity.setCreateDate(new Date());
			sysGeoCity.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
			this.save(sysGeoCity);
			
		}
		//删除
		for(Map<String,Object> map:areaList){
			this.removeById((Integer)map.get("sgcId"));
		}
	}

	@Override
	public List<Map<String, Object>> getDestSelectList() {
		String sql = "SELECT sr.name as name ,sr.id as id ,sr.uuid as uuid  " +
				"FROM sys_region sr  WHERE sr.delFlag =0 AND sr.status=0 ";
		//借用sysGeoCityDao里面的方法
		return sysGeoCityDao.findBySql(sql, Map.class);
	}

	@Override
	public List<Map<String, Object>> getDestinationList() {
		String sql = "  SELECT sr.name AS name,sr.id AS id ,sr.uuid AS uuid ,udd.id as udefid FROM sys_region sr,userdefinedict udd  " +
				     "  WHERE  udd.main_uuid=sr.uuid  AND udd.type ='dest' AND udd.delFlag=0 AND udd.companyId="+UserUtils.getCompanyIdForData();
		//借用sysGeoCityDao里面的方法
		return sysGeoCityDao.findBySql(sql, Map.class);
	}

	@Override
	public void addDest(String id) {
		SysRegion sysRegion = sysRegionService.getById(Integer.parseInt(id));
		String uuid = sysRegion.getUuid();
		UserDefineDict userDefineDict = new UserDefineDict();
		userDefineDict.setCompanyId(UserUtils.getCompanyIdForData());
		userDefineDict.setCreateBy(UserUtils.getUser().getId());
		userDefineDict.setCreateDate(new Date());
		userDefineDict.setDictId(Long.parseLong(id));
		userDefineDict.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		userDefineDict.setType("dest");
		userDefineDict.setMainUuid(uuid);
		userDefineDictDao.save(userDefineDict);
	}

	@Override
	public void delDest(String id) {
		UserDefineDict userDefineDict = userDefineDictDao.getById(Long.parseLong(id));
		userDefineDict.setUpdateBy(UserUtils.getUser().getId());
		userDefineDict.setUpdateDate(new Date());
		userDefineDict.setDelFlag("1");
		userDefineDictDao.updateObj(userDefineDict);
	}
}
