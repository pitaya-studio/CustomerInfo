/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.region.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.region.dao.SysGeoRegionRelDao;
import com.trekiz.admin.modules.region.dao.SysRegionDao;
import com.trekiz.admin.modules.region.entity.SysGeoRegionRel;
import com.trekiz.admin.modules.region.entity.SysRegion;
import com.trekiz.admin.modules.region.service.SysRegionService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SysRegionServiceImpl  extends BaseService implements SysRegionService{
	@Autowired
	private SysRegionDao sysRegionDao;
	
	@Autowired
	private SysGeographyService sysGeographyService;
	
	@Autowired
	private SysGeoRegionRelDao sysGeoRegionRelDao;
	
	
	public void save (SysRegion sysRegion){
		sysRegionDao.saveObj(sysRegion);
	}
	
	public void update (SysRegion sysRegion){
		sysRegionDao.updateObj(sysRegion);
	}
	
	public SysRegion getById(java.lang.Integer value) {
		return sysRegionDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		sysRegionDao.removeById(value);
	}	
	
	//添加地理区域时，同时维护与行政区域的关联表
	public void addSysRegionRel(SysRegion sysRegion,String cells) throws ServiceException {
		 String uuid = sysRegion.getUuid();
		 int id = sysRegion.getId();
		 if(cells==null||"".equals(cells)){
			 return;
		 }
		 String[] temps = cells.split(",");
		
		for(int i=0;i<temps.length;i++){
//			Integer tempId =0;
			try{
//				tempId = Integer.parseInt(temps[i]);
			}catch(Exception e){
				throw new ServiceException("ID异常");
			}
			SysGeography sysGeography = sysGeographyService.getById(Integer.parseInt(temps[i]));
			if(sysGeography==null){
				throw new ServiceException("找不到对应地区实例（SysGeography）");
			}
			SysGeoRegionRel sysGeoRegionRel = new SysGeoRegionRel();
			sysGeoRegionRel.setCreateBy(UserUtils.getUser().getId().intValue());
			sysGeoRegionRel.setCreateDate(new Date());
			sysGeoRegionRel.setUuid(UuidUtils.generUuid());
			sysGeoRegionRel.setGeoId(sysGeography.getId());
			sysGeoRegionRel.setGeoUuid(sysGeography.getUuid());
			sysGeoRegionRel.setRegionId(id);
			sysGeoRegionRel.setRegionUuid(uuid);
			sysGeoRegionRelDao.saveObj(sysGeoRegionRel);
			
		}
		
	}
	
	public Page<SysRegion> find(Page<SysRegion> page, SysRegion sysRegion) {
		DetachedCriteria dc = sysRegionDao.createDetachedCriteria();
		
	   	if(sysRegion.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysRegion.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysRegion.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+sysRegion.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(sysRegion.getName())){
			dc.add(Restrictions.like("name", "%"+sysRegion.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(sysRegion.getStatus())){
			dc.add(Restrictions.like("status", "%"+sysRegion.getStatus()+"%"));
		}
		if (StringUtils.isNotEmpty(sysRegion.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysRegion.getDelFlag()+"%"));
		}
		if (StringUtils.isNotEmpty(sysRegion.getIsHome())){
			dc.add(Restrictions.like("isHome", "%"+sysRegion.getIsHome()+"%"));
		}
		if(sysRegion.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysRegion.getCreateDate()));
		}
		if(sysRegion.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysRegion.getUpdateDate()));
		}
	   	if(sysRegion.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysRegion.getCreateBy()));
	   	}
	   	if(sysRegion.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysRegion.getUpdateBy()));
	   	}
	   	if(sysRegion.getLevel()!=null){
	   		dc.add(Restrictions.eq("level", sysRegion.getLevel()));
	   	}
		if (StringUtils.isNotEmpty(sysRegion.getDescription())){
			dc.add(Restrictions.like("description", "%"+sysRegion.getDescription()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysRegionDao.find(page, dc);
	}
	
	public List<SysRegion> find( SysRegion sysRegion) {
		DetachedCriteria dc = sysRegionDao.createDetachedCriteria();
		
	   	if(sysRegion.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysRegion.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysRegion.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+sysRegion.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(sysRegion.getName())){
			dc.add(Restrictions.like("name", "%"+sysRegion.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(sysRegion.getStatus())){
			dc.add(Restrictions.like("status", "%"+sysRegion.getStatus()+"%"));
		}
		if (StringUtils.isNotEmpty(sysRegion.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysRegion.getDelFlag()+"%"));
		}
		if (StringUtils.isNotEmpty(sysRegion.getIsHome())){
			dc.add(Restrictions.like("isHome", "%"+sysRegion.getIsHome()+"%"));
		}
		if(sysRegion.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysRegion.getCreateDate()));
		}
		if(sysRegion.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysRegion.getUpdateDate()));
		}
	   	if(sysRegion.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysRegion.getCreateBy()));
	   	}
	   	if(sysRegion.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysRegion.getUpdateBy()));
	   	}
	   	if(sysRegion.getLevel()!=null){
	   		dc.add(Restrictions.eq("level", sysRegion.getLevel()));
	   	}
		if (StringUtils.isNotEmpty(sysRegion.getDescription())){
			dc.add(Restrictions.like("description", "%"+sysRegion.getDescription()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysRegionDao.find(dc);
	}
    //新增的列表展示的方法，通过关联取值
	@Override
	public Page<Map<String,Object>> findRegionList(HttpServletRequest request,
			HttpServletResponse response, SysRegion sysRegion) {
		String sql ="  SELECT u.nameCns AS nameCns,sgm.id AS id,sgm.name AS name,sgm.status AS status FROM  sys_region sgm LEFT JOIN " +
				    "  (SELECT sr.id AS srrrid,  GROUP_CONCAT(sg.name_cn) AS nameCns "+   
					"  FROM  sys_region sr  , sys_geo_region_rel sgrr , sys_geography sg "+
					"  WHERE sr.id= sgrr.region_id  AND  sr.delFlag = '0'  AND sgrr.delFlag = '0' "+
					"   AND sgrr.geo_id = sg.id  AND sg.delFlag ='0'" +
					"   GROUP BY sr.id) u " +
					"  ON sgm.id = u.srrrid   WHERE sgm.delFlag = '0' AND sgm.isHome ='"+sysRegion.getIsHome()+"'";
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		sql =" select temp.nameCns AS nameCns,temp.id AS id,temp.name AS name,temp.status AS status from ("+sql+") temp";
		return sysRegionDao.findBySql(page, sql ,Map.class);
	}
	
	//详细信息展示方法
	@Override
	public List<Map<String, Object>> showRegionDetail(int id ){
		String sql ="  SELECT u.nameCns AS nameCns,sgm.description AS description,sgm.name AS name,sgm.status AS status FROM  sys_region sgm LEFT JOIN " +
			    "  (SELECT sr.id AS srrrid,  GROUP_CONCAT(sg.name_cn) AS nameCns "+   
				"  FROM  sys_region sr  , sys_geo_region_rel sgrr , sys_geography sg "+
				"  WHERE sr.id= sgrr.region_id  AND  sr.delFlag = '0'  AND sgrr.delFlag = '0' "+
				"   AND sgrr.geo_id = sg.id  AND sg.delFlag ='0'" +
				"   GROUP BY sr.id) u " +
				"  ON sgm.id = u.srrrid   WHERE sgm.delFlag = '0' AND sgm.id="+id;
		return sysRegionDao.findBySql(sql ,Map.class);
	}
}
