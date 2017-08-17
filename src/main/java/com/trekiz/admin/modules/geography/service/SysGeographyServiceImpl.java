/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.geography.service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.geography.dao.SysGeographyDao;
import com.trekiz.admin.modules.geography.entity.SysGeography;

/**
 * @author trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class SysGeographyServiceImpl extends BaseService implements
		SysGeographyService {
	@Autowired
	private SysGeographyDao sysGeographyDao;

	public void save(SysGeography sysGeography) {
		sysGeographyDao.save(sysGeography);
	}

	public void update(SysGeography sysGeography) {
		sysGeographyDao.update(sysGeography);
	}

	public SysGeography getById(java.lang.Integer value) {
		return sysGeographyDao.getById(value);
	}

	public void removeById(java.lang.Integer value) {
		sysGeographyDao.removeById(value);
	}

	public Page<SysGeography> find(Page<SysGeography> page,
			SysGeography sysGeography) {
		DetachedCriteria dc = sysGeographyDao.createDetachedCriteria();

		if (sysGeography.getId() != null) {
			dc.add(Restrictions.eq("id", sysGeography.getId()));
		}
		if (StringUtils.isNotEmpty(sysGeography.getUuid())) {
			dc.add(Restrictions.like("uuid", "%" + sysGeography.getUuid() + "%"));
		}
		if (sysGeography.getParentId() != null) {
			dc.add(Restrictions.eq("parentId", sysGeography.getParentId()));
		}
		if (StringUtils.isNotEmpty(sysGeography.getParentUuid())) {
			dc.add(Restrictions.like("parentUuid",
					"%" + sysGeography.getParentUuid() + "%"));
		}
		if (sysGeography.getLevel() != null) {
			dc.add(Restrictions.eq("level", sysGeography.getLevel()));
		}
		if (sysGeography.getSort() != null) {
			dc.add(Restrictions.eq("sort", sysGeography.getSort()));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameCn())) {
			dc.add(Restrictions.like("nameCn", "%" + sysGeography.getNameCn()
					+ "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameShortCn())) {
			dc.add(Restrictions.like("nameShortCn",
					"%" + sysGeography.getNameShortCn() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameEn())) {
			dc.add(Restrictions.like("nameEn", "%" + sysGeography.getNameEn()
					+ "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameShortEn())) {
			dc.add(Restrictions.like("nameShortEn",
					"%" + sysGeography.getNameShortEn() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNamePinyin())) {
			dc.add(Restrictions.like("namePinyin",
					"%" + sysGeography.getNamePinyin() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameShortPinyin())) {
			dc.add(Restrictions.like("nameShortPinyin",
					"%" + sysGeography.getNameShortPinyin() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getCrossSection())) {
			dc.add(Restrictions.like("crossSection",
					"%" + sysGeography.getCrossSection() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getDescription())) {
			dc.add(Restrictions.like("description",
					"%" + sysGeography.getDescription() + "%"));
		}
		if (sysGeography.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy", sysGeography.getCreateBy()));
		}
		if (sysGeography.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate", sysGeography.getCreateDate()));
		}
		if (sysGeography.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy", sysGeography.getUpdateBy()));
		}
		if (sysGeography.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate", sysGeography.getUpdateDate()));
		}
		if (sysGeography.getStatus() != null) {
			dc.add(Restrictions.eq("status", sysGeography.getStatus()));
		}
		if (StringUtils.isNotEmpty(sysGeography.getDelFlag())) {
			dc.add(Restrictions.like("delFlag", "%" + sysGeography.getDelFlag()
					+ "%"));
		}
		// sysGeography.setParentId(0);
		// dc.add(Restrictions.eq("parentId", sysGeography.getParentId()));
		// dc.addOrder(Order.desc("id"));
		return sysGeographyDao.find(page, dc);
	}

	public List<SysGeography> find(SysGeography sysGeography) {
		DetachedCriteria dc = sysGeographyDao.createDetachedCriteria();

		if (sysGeography.getId() != null) {
			dc.add(Restrictions.eq("id", sysGeography.getId()));
		}
		if (StringUtils.isNotEmpty(sysGeography.getUuid())) {
			dc.add(Restrictions.like("uuid", "%" + sysGeography.getUuid() + "%"));
		}
		if (sysGeography.getParentId() != null) {
			dc.add(Restrictions.eq("parentId", sysGeography.getParentId()));
		}
		if (StringUtils.isNotEmpty(sysGeography.getParentUuid())) {
			dc.add(Restrictions.like("parentUuid",
					"%" + sysGeography.getParentUuid() + "%"));
		}
		if (sysGeography.getLevel() != null) {
			dc.add(Restrictions.eq("level", sysGeography.getLevel()));
		}
		if (sysGeography.getSort() != null) {
			dc.add(Restrictions.eq("sort", sysGeography.getSort()));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameCn())) {
			dc.add(Restrictions.like("nameCn", "%" + sysGeography.getNameCn()
					+ "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameShortCn())) {
			dc.add(Restrictions.like("nameShortCn",
					"%" + sysGeography.getNameShortCn() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameEn())) {
			dc.add(Restrictions.like("nameEn", "%" + sysGeography.getNameEn()
					+ "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameShortEn())) {
			dc.add(Restrictions.like("nameShortEn",
					"%" + sysGeography.getNameShortEn() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNamePinyin())) {
			dc.add(Restrictions.like("namePinyin",
					"%" + sysGeography.getNamePinyin() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getNameShortPinyin())) {
			dc.add(Restrictions.like("nameShortPinyin",
					"%" + sysGeography.getNameShortPinyin() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getCrossSection())) {
			dc.add(Restrictions.like("crossSection",
					"%" + sysGeography.getCrossSection() + "%"));
		}
		if (StringUtils.isNotEmpty(sysGeography.getDescription())) {
			dc.add(Restrictions.like("description",
					"%" + sysGeography.getDescription() + "%"));
		}
		if(sysGeography.getPosition() != null) {
			dc.add(Restrictions.eq("position", sysGeography.getPosition()));
		}
		if (sysGeography.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy", sysGeography.getCreateBy()));
		}
		if (sysGeography.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate", sysGeography.getCreateDate()));
		}
		if (sysGeography.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy", sysGeography.getUpdateBy()));
		}
		if (sysGeography.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate", sysGeography.getUpdateDate()));
		}
		if (sysGeography.getStatus() != null) {
			dc.add(Restrictions.eq("status", sysGeography.getStatus()));
		}
		if (StringUtils.isNotEmpty(sysGeography.getDelFlag())) {
			dc.add(Restrictions.like("delFlag", "%" + sysGeography.getDelFlag()
					+ "%"));
		}

		// dc.addOrder(Order.desc("id"));
		return sysGeographyDao.find(dc);
	}

	@Override
	public List<SysGeography> getAllByContion(Map<String, String> conditon) {
		StringBuffer sb = new StringBuffer();
		String label = (String)conditon.get("lable");
		if ("guonei".equals(label)) {
			sb.append(" select ge.id as id ,"
					+ " ge.name_cn as nameCn,"
					+ " ge.parent_id as parentId,"
					+ " ge.sort as sort ,"
					+ " sum(eg.id) as isLast,"
					+ "  ge.level as level "
					+ "  from sys_geography    ge"
				    + " LEFT JOIN sys_geography eg ON ge.id = eg.parent_id and eg.delFlag='0' where (ge.parent_id=2 ) and ge.delFlag='0' and ge.position=1 "
				    + " GROUP BY ge.id,ge.name_cn,ge.parent_id,ge.sort  order by ge.sort  ");
		} else {
			sb.append(" select ge.id as id ,"
					+ " ge.name_cn as nameCn,"
					+ " ge.parent_id as parentId,"
					+ " ge.sort as sort ,"
					+ " sum(eg.id) as isLast,"
					+ " ge.level as level "
					+ "  from sys_geography    ge"
				    + " LEFT JOIN sys_geography eg ON ge.id = eg.parent_id and eg.delFlag='0' where (ge.parent_id=0 ) and ge.delFlag='0' and ge.position=2 "
				    + " GROUP BY ge.id,ge.name_cn,ge.parent_id,ge.sort  order by ge.sort  ");
		}

		// sb.append(" select ge.id as id,ge.name_cn as nameCn,ge.parent_id as parentId,ge.sort as sort  from sys_geography  ge where ge.parent_id=? or ge.id=1 or ge.id=2");
		return sysGeographyDao.findBySql(sb.toString());
	}
    //点击搜索时返回数据的查询语句
	@Override
	public List<SysGeography> getSearchList(Map<String, String> conditon) {
		int position =1;
		String a = conditon.get("search");
		String lable = conditon.get("lable");
		if ("guoji".equals(lable)){
			position=2;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" select ge.id as id,ge.name_cn as nameCn,ge.parent_id as parentId,ge.sort as sort,'' as isLast,ge.level as level  from sys_geography  ge where ge.name_cn like '%"
				+ a + "%' and ge.position="+position+" and ge.delFlag='0' order by ge.sort ");
		return sysGeographyDao.findBySql(sb.toString());
	}

	// 点击三角符号获取下一级区域信息
	@Override
	public List<Map<String, Object>> getChildList(Map<String, String> conditon) {
		String id = conditon.get("id");
		StringBuffer sb = new StringBuffer();
		// sb.append(" select ge.id as id,ge.name_cn as nameCn,ge.parent_id as parentId  from sys_geography  ge where ge.parent_id=("+a+") ");
		// sb.append(" select *  from sys_geography  ge where ge.parent_id=("+a+") ");
		sb.append(" select ge.id as id,ge.name_cn as nameCn,ge.parent_id as parentId,ge.sort as sort ,sum(eg.id) as isLast ,ge.level as level "
				+ "from sys_geography  ge"
				+ " LEFT JOIN sys_geography eg ON ge.id = eg.parent_id and eg.delFlag='0'"
				+ " where ge.delFlag='0' and  ge.parent_id= "
				+ id
				+ "   GROUP BY ge.id,ge.name_cn,ge.parent_id,ge.sort"
				+ " order by ge.sort");
		return sysGeographyDao.findBySql(sb.toString(), Map.class);
	}

	@Override
	public String getParentName(Map<String, String> conditon) {
		String id = conditon.get("id");
		String lable = conditon.get("lable");
		String parentName = "";
		int topId=0;
		if("guonei".equals(lable)) {
			topId=1;
		}
		while(true){
			String sql = "select  parent_id  , name_cn from sys_geography where id ="+id;
			List<Object[]> result = sysGeographyDao.findBySql(sql);
			if(result==null||!result.iterator().hasNext()){
				break;
			}
			Object[] obj = result.iterator().next();
			parentName =obj[1]+"-"+parentName;
			
			if((int)obj[0]==topId){
				break;
			}
			id=obj[0].toString();
			
		}
		return parentName.substring(0, (parentName.equals("")?1:parentName.length())-1);
	}

	@Override
	public Map<String, String> delGeography(Long id) {
		Map<String, String> map = Maps.newHashMap();
		map.put("msg", null);
		del(sysGeographyDao.getById(id.intValue()));
		return map;
		
	}
	
	
	public void del(SysGeography geo){
		geo.setDelFlag("1");
		sysGeographyDao.update(geo);
		List<SysGeography> list = sysGeographyDao.find("from SysGeography where delFlag ='0' and parentId = "+ geo.getId());
		for(SysGeography bean:list){
			del(bean);
		}
	}
	
	
	
	@Override
	public List<Map<String, Object>> getModifyGeography(Map<String, String> conditon) {
		String id = conditon.get("id");
		StringBuffer sb = new StringBuffer();
		sb.append(" select ge.name_cn as	nameCn , ge.name_short_cn  as nameShortCn , ge.name_pinyin as namePinyin ,ge.name_short_pinyin as nameShortPinyin,"+
                  "ge.name_en as nameEn , ge.name_short_en as nameShortEn , ge.sort as sort , ge.cross_section as crossSection , ge.description  as description " +
                  "from sys_geography ge   where ge.id=" + id);
		return sysGeographyDao.findBySql(sb.toString(), Map.class);
	}
	//更新排序的方法
	@Override
	public void saveSort(String[] id ,String [] sort,SysGeography sysGeography){
		for(int i=0;i<id.length;i++){
			if(id[i]!=null&&id[i]!=""){
			String hql = " UPDATE SysGeography SET sort =?   WHERE id = ? ";
			sysGeographyDao.getSession().createQuery(hql)
			.setParameter(0, Integer.parseInt(sort[i]))
			.setParameter(1, Integer.parseInt(id[i]))
			.executeUpdate();
			}	
		}
		
	}
	//地理区域界面添加覆盖范围时使用,添加树形结构的公共方法
	public List<Object[]> getGeographyList(String lable,String parentUuids) {
		StringBuffer sb = new StringBuffer();
		if ("guonei".equals(lable)) {
			sb.append(" select ge.id as id , "
					+ " ge.name_cn as nameCn, "
					+ " ge.parent_id as parentId, "
					+ " sum(eg.id) as isLast  "
					+ " from sys_geography  ge LEFT JOIN sys_geography eg ON ge.id = eg.parent_id and eg.delFlag='0' "
				    + " where ge.position=1 and ge.id !=2 and ge.delFlag='0' ");
			 if("".equals(parentUuids)){
				 sb.append(" and  ge.level=2 ");
			 } else{
				 sb.append(" and ge.parent_uuid in ("+parentUuids+")");
			 } 
			 sb.append(" GROUP BY ge.name_cn,ge.parent_id,ge.id ");
		} else {
			sb.append( " select ge.id as id , "+
					   " ge.name_cn as nameCn, "+
					   " ge.parent_id as parentId,"+
					   " sum(eg.id) as isLast"+					
					   " from sys_geography  ge LEFT JOIN sys_geography eg ON ge.id = eg.parent_id and eg.delFlag='0'"+
				       " where  ge.position=2 and ge.delFlag='0' ");
			 if("".equals(parentUuids)){
				 sb.append(" and ge.level=0");
			 } else {
				 sb.append(" and ge.parent_uuid in ("+parentUuids+",'0' )");
			 } 
			 sb.append(" GROUP BY ge.name_cn,ge.parent_id,ge.id ");
		}
		return sysGeographyDao.findBySql(sb.toString());
	}
	
	public Map<String, String> getAllCountry() {
		Map<String, String> allCountry = new LinkedHashMap<String, String>();
		//String sql = "select * from sys_geography where level=1 and delFlag = '0' order by id";
		//List<SysGeography> sysGeographys = sysGeographyDao.findBySql(sql, SysGeography.class);
		List<SysGeography> sysGeographys = sysGeographyDao.find("from SysGeography where level=1 and delFlag = '0' order by id");
		for(SysGeography sysGeography : sysGeographys) {
			if(StringUtils.isEmpty(sysGeography.getUuid()) || (StringUtils.isEmpty(sysGeography.getNameCn()))) {
				continue;
			}
			
			allCountry.put(sysGeography.getUuid(), sysGeography.getNameCn());
		}
		
		return allCountry;
	}
	//地理表中需关联行政区域表中的id
	@Override
	public  String getGeoIds(String id) {
		String sql = "SELECT  GROUP_CONCAT(sgrr.geo_id)  FROM sys_geo_region_rel sgrr WHERE sgrr.delFlag = '0' and sgrr.region_id = "+id;
		List<String> list = sysGeographyDao.findBySql(sql);
		return list.get(0);
		
	}
	//城市维护时使用
	@Override
	public List<Object[]> getAllGeographyList() {
		String sql = "   select ge.id as id , ge.name_cn as nameCn,  ge.parent_id as parentId  " +
				     "   from sys_geography  ge   where  ge.delFlag='0' and ge.level in (0,1,2,3)";
		return sysGeographyDao.findBySql(sql);
	}
	@Override
	public List<Object[]> getGeographyLevel(String lable) {
		StringBuffer sb = new StringBuffer();
		if ("guonei".equals(lable)) {
			sb.append(" select ge.id as id , "
					+ " ge.name_cn as nameCn, "
					+ " ge.parent_id as parentId "					
					+ "  from sys_geography  ge  "
				    + "  where ge.position=1 and ge.id !=2 and ge.delFlag='0' and ge.level in (0,1,2,3) ");
		} else {
			sb.append(" select ge.id as id , "
					+ " ge.name_cn as nameCn, "
					+ " ge.parent_id as parentId "					
					+ "  from sys_geography   ge "
				    + "  where  ge.position=2 and ge.delFlag='0'  and ge.level in (0,1,2) ");
		}
		return sysGeographyDao.findBySql(sb.toString());
	}

	@Override
	public List<Object[]> getSecondList(int id) {
		String sql = "  SELECT ge.id AS id , ge.name_cn AS nameCn, ge.parent_id AS parentId ,SUM(eg.id) AS isLast " +
				"  FROM sys_geography  ge   LEFT JOIN sys_geography eg  "+
				"  ON ge.id = eg.parent_id AND eg.delFlag='0'  "+
				"  WHERE ge.delFlag='0' AND ge.parent_id ="+id+"  GROUP BY  ge.id,ge.name_cn,ge.parent_id  "	;
		return sysGeographyDao.findBySql(sql);
	}
	
	@Override
	public List<Object[]> getSecondList1(int id) {
		//SysGeography geo = sysGeographyDao.getById(id);
		//String sql = "SELECT ge.id AS id , ge.name_cn AS nameCn, ge.parent_id AS parentId,(CASE WHEN exists (select id from sys_geography gs where gs.parent_id=ge.id and gs.level in (0,1,2,3,4)) THEN 1 ELSE 0 END) AS isLast "+
		//" from sys_geography  ge where ge.parent_uuids like '%"+geo.getUuid()+"%'  and ge.level in (0,1,2,3,4)";
		
		String sql = "  SELECT ge.id AS id , ge.name_cn AS nameCn, ge.parent_id AS parentId ,SUM(eg.id) AS isLast " +
			"  FROM sys_geography  ge   LEFT JOIN sys_geography eg  "+
				"  ON ge.id = eg.parent_id AND eg.delFlag='0'  "+
				"  WHERE ge.delFlag='0' AND ge.parent_id ="+id+" and ge.position = 2 GROUP BY  ge.id,ge.name_cn,ge.parent_id  "	;
		return sysGeographyDao.findBySql(sql);
	}

	@Override
	public String getParentUuids(String id) {
    	String sqlid ="select ge.cross_section from sys_geography ge where ge.id="+id;
		StringBuffer parentUuids = new StringBuffer();
		List<String> listid = sysGeographyDao.findBySql(sqlid);
		if("".equals(listid.get(0))||listid.get(0)==null||"null".equals(listid.get(0))){
			return ""; 
			}
		String[] cross_section = listid.get(0).split(",");
		for(String uuid : cross_section){
			parentUuids.append("'"+uuid+"'"+",");
		   }
		
		String sqluuids ="select ge.parent_uuids from sys_geography ge where ge.id in("+parentUuids.deleteCharAt(parentUuids.length()-1)+")";
		//返回的父节点id有重复，用set清除
		List<String> listuuids = sysGeographyDao.findBySql(sqluuids);
		Set<String> setuuids = new  HashSet<String>();
		 for(String a :listuuids){
		  if(a==null){
			  continue;
		  }else if(a.contains(",")){
		    	String[] temp =a.split(",")	;
		    	 for(String b :temp){
		    	        setuuids.add(b);
		    	        }
		    }else{
		      setuuids.add(a);
		    }
		 }
		//Set<String> setuuids = new  HashSet<String>();
		//setuuids.addAll(listuuids);
		//String[] uuids = setuuids.toArray(new String[] {}); 
		String[] uuids = setuuids.toArray(new String[] {}); 
		parentUuids.setLength(0);
		for(String uuid : uuids){			
			parentUuids.append("'"+uuid+"'"+",");
		   }
		return parentUuids.deleteCharAt(parentUuids.length()-1).toString();
	}
	//地理区域修改界面添加父级节点
	@Override
	public String getParentUuidsForRegion(String ids){
		StringBuffer parentUuids = new StringBuffer();
		String sqluuids ="select ge.parent_uuids from sys_geography ge where ge.id in("+ids+")";
		//返回的父节点id有重复，用set清除
		List<String> listuuids = sysGeographyDao.findBySql(sqluuids);
		Set<String> setuuids = new  HashSet<String>();
		 for(String a :listuuids){
		  if(a==null){
			 continue; 
		  }else if(a.contains(",")){
				String[] temp =a.split(",")	;
				   for(String b :temp){
				    setuuids.add(b);
				   }
				    }else{
				      setuuids.add(a);
				    }
				 }
		String[] uuids = setuuids.toArray(new String[] {}); 
		if(uuids == null|| uuids.length==0){
			return "";
		}
	    for(String uuid : uuids){			
			parentUuids.append("'"+uuid+"'"+",");
	     }
	    return parentUuids.deleteCharAt(parentUuids.length()-1).toString();
	}
	
	public void updateGeographys(List<SysGeography> sysGeographys) {
		sysGeographyDao.updateGeographys(sysGeographys);
	}
	
	public List<Map<String, Object>> getGeographysBySql(String sql, Object... parameter) {
		return sysGeographyDao.findBySql(sql, parameter);
	}
	
	public String getNameCnByUuid(String uuid) {
		SysGeography sysGeography = sysGeographyDao.getByUuid(uuid);
		if(sysGeography != null) {
			return sysGeography.getNameCn();
		}
		
		return "";
	}
	
	
	/**
	 * 获取所有的国家名称
	 */
	public List<SysGeography> getAllCountryName(){
		//String sql = "select * from sys_geography where level=1 and delFlag = '0' order by id";
		//List<SysGeography> sysGeographys = sysGeographyDao.findBySql(sql, SysGeography.class);
		List<SysGeography> sysGeographys = sysGeographyDao.find("from SysGeography where level=1 and delFlag = '0' order by id");
		for(SysGeography sysGeography : sysGeographys) {
			if(StringUtils.isEmpty(sysGeography.getUuid()) || (StringUtils.isEmpty(sysGeography.getNameCn()))) {
				sysGeographys.remove(sysGeography);
			}
		}
		return sysGeographys;
	}
}
