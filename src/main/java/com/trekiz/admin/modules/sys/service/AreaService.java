/**
 *
 */
package com.trekiz.admin.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.config.Context;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.invoice.repository.OrderinvoiceDao;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.repository.AreaDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

import static org.hibernate.secure.internal.HibernatePermission.UPDATE;

/**
 * 区域Service
 * @author zj
 * @version 2013-11-19
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends BaseService {

	@Autowired
	private AreaDao areaDao;
	
	public Area get(Long id) {
		return areaDao.findOne(id);
	}
	
	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}
	
	public List<Area> findByCityList(){
		return UserUtils.getAreaCityList();
	}
	
	
	 /**
	 *  功能:
	 *
	 *  @author zj
	 *  @DateTime 2014-5-6 上午11:58:23
	 *  @param childAreaIds
	 *  @param areaIds 去重判断，如果为null，则不去重
	 *  @param targetAreas
	 */
	public void appendParentArea(List<Long> childAreaIds,List<Long> areaIds,List<Area> targetAreas) {
		if(childAreaIds!=null && childAreaIds.size()>0){
			List<Area> areas = findByFilter(childAreaIds);
			if(areas!=null && areas.size()!=0){
				targetAreas.addAll(areas);
				List<Long> parentAreaIds = Lists.newArrayList();
				for(Area area:areas){
					if(area.getParent()!=null){
						if(areaIds != null && !areaIds.contains(area.getParent().getId()))
							parentAreaIds.add(area.getParent().getId());
							areaIds.add(area.getParent().getId());
					}
				}
				if(parentAreaIds != null && parentAreaIds.size()>0){
					appendParentArea(parentAreaIds,areaIds,targetAreas);
				}
			}
		}
	}
	
	/**
	 * 过滤批发商录过的产品目标区域
	 *     
	 * 创建人：liangjingming  
	 * 创建时间：2014-2-10 下午3:10:06      
	 *
	 */
	public List<Area> findByFilter(List<Long> areaIds){
		return UserUtils.findByFilter(areaIds);
	}

	@Transactional(readOnly = false)
	public void save(Area area) {
		area.setParent(this.get(area.getParent().getId()));
		String oldParentIds = area.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		area.setParentIds(area.getParent().getParentIds()+area.getParent().getId()+",");
		areaDao.clear();
		areaDao.save(area);
		// 更新子节点 parentIds
		List<Area> list = areaDao.findByParentIdsLike("%,"+area.getId()+",%");
		for (Area e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
		}
		areaDao.save(list);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		areaDao.deleteById(id, "%,"+id+",%");
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	public List<Area> findAreasByActivity(Long activityId){
		return areaDao.findAreasByActivity(activityId);
	}
	
	public List<Area> findAreaByName(String name){
		return areaDao.findAreaByName(name);
	}
	
	/**
	 * 取得除中国外的其他国家
	 * @return
	 */
	public Object findCountrysNotChina() {

		JSONArray results = new JSONArray();
		// 第一个为空行
		JSONObject resobj = new JSONObject();
		resobj.put("id", "");
		resobj.put("label", "");
		results.add(resobj);

		Long companyId = UserUtils.getUser().getCompany().getId();
		
		String sql = "select t.id,t.name from sys_area t,userdefinedict t2 where t.id = t2.dictId and t2.type='area' and t2.companyId = '"+companyId+"' and t.delFlag='0' and t.type='2' and (t.id in (select t1.parentId from sys_area t1 where t1.delFlag='0' and (t1.type='3' or t1.type='4'))) and t.name <> '中国'";
		List<Map<String, String>> list = areaDao.findBySql(sql, Map.class);

		if (list != null) {
			for (Map<String, String> map : list) {
				resobj = new JSONObject();

				resobj.put("id", map.get("id"));
				resobj.put("label", map.get("name"));
				results.add(resobj);
			}
		}

		return results;
	}
	
	/**
	 * 由国家ID取得相应的城市信息
	 * @param countryId
	 * @return
	 */
	public Object findCityByCountryId(String countryId){
		
		JSONArray results = new JSONArray();
		// 第一个为空行
		JSONObject resobj = new JSONObject();
		resobj.put("id", "");
		resobj.put("label", "");
		results.add(resobj);
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		String sql = "(select t.id,t.name from sys_area t,userdefinedict t2 where t.id = t2.dictId and t2.companyId = '"+companyId+"' and t.delFlag = '0' and t.parentIds LIKE ? and t.type = '4') union (select t.id,t.name from sys_area t,userdefinedict t2 where t.id = t2.dictId and t2.companyId = '"+companyId+"' and t.delFlag = '0' and t.parentId = ? and t.type = '4')";
		List<Map<String, String>> list = areaDao.findBySql(sql, Map.class,
				"%"+countryId+"%", countryId);
		
		if (list != null) {
			for (Map<String, String> map : list) {
				resobj = new JSONObject();

				resobj.put("id", map.get("id"));
				resobj.put("label", map.get("name"));
				results.add(resobj);
			}
		}
		
		return results;
	}
	
	/**
	 * 由国家名称取得该对象
	 * @param countryName 国家名称
	 * @return
	 */
	public Area findCountryByName(String countryName) {
		List<Area> list = areaDao.findCountryByName(countryName);
		if (list != null && list.size() != 0) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * 获取所有有机场的城市
	 * @param areaName 地区名称 关联公司
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Area> findAirportCityList(String areaName) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		String hql = " select area" +
				" from Area area, UserDefineDict udict1, AirportInfo air " +
				" where area.id = udict1.dictId " +
				" and udict1.companyId=" + companyId +
				" and area.id = air.cityId " +
				" and area.type = '4' " +
				" and air.companyId = " +companyId;
		if(StringUtils.isNotBlank(areaName)){
			  hql += " and area.name like '%" + areaName + "%' " ;
		}
		hql += " group by area.id ";
		Query query = areaDao.getSession().createQuery(hql);
		return query.list();
	}
	
	/**
	 * 获取所有到达城市 by chy 2015年7月15日15:23:12 用于显示城市名称
	 */
	@SuppressWarnings("unchecked")
	public  List<Area> findAirportCityList(){
		String sql = "select area from Area area where area.type = '4'";
		Query query = areaDao.getSession().createQuery(sql);
		return query.list();
	}
	
	/**
	 * 获取所有有机场的城市
	 * @param areaName 地区名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public  List<Area> findAirportCityList2(String areaName) {
		// TODO Auto-generated method stub
		Long companyId = UserUtils.getUser().getCompany().getId();
		String  hql = " select area" +
				" from Area  area,   AirportInfo air " +
				" where  area.id = air.cityId  " +
				" and area.type ='4' " +
				" and air.companyId =" +companyId;
		if(!StringUtils.isBlank(areaName)){
			  hql +=" and area.name like '%"+areaName+"%' 	" ;
		}
		hql+=" group by area.id	";
		Query query = areaDao.getSession().createQuery(hql);
		return query.list();
	}
	
	
	@SuppressWarnings("rawtypes")
    public  List<Map> findAirportCityList2(Map map) {
		// TODO Auto-generated method stub
		String hql = "";
		if(map.get("areaName")!=null){
			if(StringUtils.isBlank(map.get("areaName").toString())){
				  hql = " select area.id,area.name from sys_area area,sys_airport_info air where area.id = air.city_id and area.type ='4'  group by area.id	";
			}else{
				  hql = " select area.id,area.name from sys_area area,sys_airport_info air where area.id = air.city_id and area.type ='4'  and area.name like'%"+map.get("areaName").toString()+"%' group by area.id	" ;
			}
		}
		if(map.get("id")!=null){
				  hql = " select area.id,area.name from sys_area area,sys_airport_info air where area.id = air.city_id and area.type ='4' and area.id = "+map.get("id").toString()+"  group by area.id	";
		}
		
		OrderinvoiceDao ad = SpringContextHolder.getBean("orderinvoiceDao");
		return ad.findBySql(hql,Map.class);
	}
	
	
	/**
	 * 获取出发城市（取自字典表)
	 * @param areaName 城市名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public  List<Dict> findFromCityList(String areaName) {
		// TODO Auto-generated method stub
		Long companyId = UserUtils.getUser().getCompany().getId();
		String hql= " select dict  from Dict dict ,UserDefineDict udict " +
			  		" where udict.type='fromarea' " +
			  		" and udict.dictId=dict.id " +
			  		" and  dict.type='from_area' " +
			  		" and dict.delFlag='0'" +
			  		" and udict.companyId = " + companyId;
			  if(!StringUtils.isBlank(areaName)){
				  hql += " and dict.label like '%"+areaName+"%'";
			  }
			  hql += " ORDER BY dict.sort	";
		Query query = areaDao.getSession().createQuery(hql);
		return query.list();
	}
	
	/**
	 * 获取离境口岸（取自字典表)
	 * @param areaName 城市名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public  List<Dict> findOutCityList(String areaName) {
		// TODO Auto-generated method stub
		Long companyId = UserUtils.getUser().getCompany().getId();
		String hql= " select dict  from Dict dict ,UserDefineDict udict " +
			  		" where udict.type='outarea' " +
			  		" and udict.dictId=dict.id " +
			  		" and  dict.type='out_area' " +
			  		" and dict.delFlag='0'" +
			  		" and udict.companyId = " + companyId;
			  if(!StringUtils.isBlank(areaName)){
				  hql += " and dict.label like '%"+areaName+"%'";
			  }
			  hql += " ORDER BY dict.sort	";
		Query query = areaDao.getSession().createQuery(hql);
		return query.list();
	}
	
	/**
	 * 获取线路国家
	 * @param areaName 城市名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Area>  findCountryList(String areaName) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		String  hql = " select area" +
				" from Area  area, UserDefineDict udict1 , AirportInfo air " +
				" where area.id = udict1.dictId " +
				" and udict1.companyId="+companyId+
				" and area.type ='2' " ;
		if(!StringUtils.isBlank(areaName)){
			  hql +=" and area.name like '%"+areaName+"%' 	" ;
		}
		hql+=" group by area.id	";
		Query query = areaDao.getSession().createQuery(hql);
		return query.list();
		
		
	}
	
	/**
	 * 获取entity
	 * @param areaId
	 * @return
	 */
	public Area findById(Long areaId){
		return areaDao.findOne(areaId);
	}
	
	/**
	 * 根据ID获取name
	 * @param areaId
	 * @return
	 */
	public String findNameById(Long areaId){
		if (areaId == null) {
			return "";
		}
		Area area = areaDao.findOne(areaId);
		return area == null ? "" : area.getName();
	}
	public List<Area> findByParentId(Integer pId){
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM sys_area WHERE parentId=? AND delFlag=?");
		List<Area> list=areaDao.findBySql(sbf.toString(),Area.class, pId,0);
		return list;
	}

	public List<Map<String, Object>> getCountry4T1() {
		return areaDao.getCountry4T1();
	}

	public List<Map<String, Object>> getTargetArea4T1(String type) {
		return areaDao.getTargetArea4T1(type);
	}

	public boolean isExist(String name) {
		List<Area> list = areaDao.isExist(name);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据城市id获取国家
	 * @param cityId
	 * @return
	 */
	public Map<String, Object> getCountryByCityId(String cityId) {
		String sql = "SELECT sa.id, sa.name from sys_area sa, (select id, parentId from sys_area where id = ?) a" +
				" where sa.id = a.parentId";
		List<Map<String, Object>> list = areaDao.findBySql(sql, Map.class, cityId);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

    /**
     * 根据id更新T1区域字段
     * @param districtId
     * @param id
     */
	public void updateDistrictById(Long districtId, Long id) {
        areaDao.updateDistrictById(districtId, id);
    }


    public String getIdsByDistrictId(Long districtId) {
		String sql = "SELECT GROUP_CONCAT(id) ids FROM sys_area sa " +
				"WHERE sa.sys_district_id = ? AND delFlag = " + Context.DEL_FLAG_NORMAL;
		List<Map<String, String>> list = areaDao.findBySql(sql, Map.class, districtId);
		if (list != null || list.size() > 0) {
			return list.get(0).get("ids");
		}
		return null;
	}

    public void deleteDistrictId(Long id) {
		String sql = "UPDATE sys_area SET sys_district_id = null where sys_district_id = ?";
		areaDao.updateBySql(sql, id);
    }
}


