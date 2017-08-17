package com.trekiz.admin.modules.sys.repository;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.SysModuleConfig;


public interface SysModuleConfigDao extends SysModuleConfigDaoCustom,CrudRepository<SysModuleConfig, String>{
	//public void saveModuleConfig(SysModuleConfig sysModuleConfig);
	
	/*public SysModuleConfig querySysModuleConfigById(String id);
	public SysModuleConfig querySysModuleConfigByEntity(SysModuleConfig sysModuleConfig);*/
	
	@Query("from SysModuleConfig where id = ?1 ")
	public SysModuleConfig querySysModuleConfigById(String id);
	@Query("from SysModuleConfig where path=?1 and companyid=?2 ")
	public SysModuleConfig queryModuleByPathAndCompanyId(String path,String companyid);
	@Modifying
	@Query("update SysModuleConfig set companyId=?2, fmoduleId=?3, moduleId=?4, pageName=?5, path=?6, prePath=?7, updateBy=?8, updateDate=?9,fmodulename=?10,modulename=?11 where id = ?1")
	public int updateSysModuleConfig(String id,Long lcompanyId,String fmoduleId,String moduleId,String pageName,String path,String prepath,String updateBy,Date date,String fmodulename,String modulename);
	
}

interface SysModuleConfigDaoCustom extends BaseDao<SysModuleConfig>{

	public  Page<Map<String,Object>> querySysModuleConfigList(HttpServletRequest request,HttpServletResponse response,Map<String,Object> condMap,SysModuleConfig sysModule,String companyId);
}

@Repository
class SysModuleConfigDaoImpl extends BaseDaoImpl<SysModuleConfig>implements SysModuleConfigDaoCustom{
	
	public void saveModuleConfig(SysModuleConfig sysModuleConfig){
		Session session = getSession();
		Transaction tx = getSession().getTransaction();
		tx.begin();
		session.saveOrUpdate(sysModuleConfig);
		tx.commit();
		session.close();
	}
	
	public  Page<Map<String,Object>> querySysModuleConfigList(HttpServletRequest request,HttpServletResponse response,Map<String,Object> condMap,SysModuleConfig sysModule,String companyId){
		String basesql = "select m.id id,m.companyid,s.name companyname,fmoduleid,fmodulename,moduleid,modulename,pagename,path,prepath,m.createby,m.createdate from sys_config_company m,sys_office s where m.companyid = s.id and 1=1 ";
		StringBuffer condsql = new StringBuffer();
		if(sysModule.getId()!=null&&!"".equals(sysModule.getId())){
			condsql = condsql.append(" and m.id = " + sysModule.getId());
		}
		if((companyId!=null&&!"".equals((companyId)))){
			condsql = condsql.append(" and m.companyid = " + sysModule.getCompanyId());
		}
		if(sysModule.getFmoduleId()!=null&&!"".equals(sysModule.getFmoduleId())){
			condsql = condsql.append(" and m.fmoduleid = " + sysModule.getFmoduleId());
		}
		if(sysModule.getModuleId()!=null&&!"".equals(sysModule.getModuleId())){
			condsql = condsql.append(" and m.moduleid = '" + sysModule.getModuleId() + "'");
		}
		if(sysModule.getPageName()!=null && !"".equals(sysModule.getPageName())){
			condsql = condsql.append(" and m.pagename like %" + sysModule.getPageName() + "%");
		}
		if(sysModule.getPath()!=null && !"".equals(sysModule.getPath())){
			condsql = condsql.append(" and m.path like %" + sysModule.getPath() + "%");
		}
		if(sysModule.getPrePath()!=null&&!"".equals(sysModule.getPrePath())){
			condsql = condsql.append(" and m.prepath like %" + sysModule.getPrePath() + "%");
		}
		
		
		//排序条件,默认按时间倒叙排列
		String orderbysql = " order by 1 = 1 ";
		
		if(condMap.get("sysModuleIdSort")!=null &&!"".equals(condMap.get("sysModuleIdSort"))){
			 orderbysql+=", m.fmodulename  "+condMap.get("sysModuleIdSort").toString();
		}
		if(condMap.get("sysModuleSonIdSort")!=null && !"".equals(condMap.get("sysModuleSonIdSort"))){
			orderbysql+=", m.modulename "+condMap.get("sysModuleSonIdSort").toString();
		}
		if(condMap.get("sysOfficeSort")!=null &&!"".equals(condMap.get("sysOfficeSort"))){
			 orderbysql+=", m.companyid  "+condMap.get("sysOfficeSort").toString();
		}else{
			orderbysql+=",createdate desc ";
		}
		
		
		String sqlString = basesql + condsql.toString() + orderbysql;
//		System.out.println("执行查询sql： " + sqlString);
		return findBySql(new Page<Map<String,Object>>(request, response),sqlString,Map.class);
	}
}
