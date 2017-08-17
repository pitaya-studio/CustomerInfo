/**
 *
 */
package com.trekiz.admin.common.service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.quauq.multi.tenant.datasource.DataSourceContainer;
import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.util.MultiTenantUtil;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.Reflections;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.sys.entity.LogOperate;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.LogOperateSaveDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * Service基类
 * @author zj
 * @version 2013-11-19
 */
public abstract class BaseService {
	
	@Autowired
	protected EntityManagerFactory entityManagerFactory;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	protected Session sessionInstance;
	@Autowired
	protected LogOperateSaveDao logOpeDao;
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public final static String OPERATION_ADD = "add";
	public final static String OPERATION_UPDATE = "update";
	
	/**
	 * 获取HibernateSessions
	 * @return
	 */
	protected Session getHibernateSession(){
		
		if(getSessionInstance()==null){
			
			setSessionInstance((Session)entityManagerFactory.createEntityManager().getDelegate());
		}
		return getSessionInstance();
	}
	
	public Session getSessionInstance() {
		return sessionInstance;
	}

	public void setSessionInstance(Session sessionInstance) {
		this.sessionInstance = sessionInstance;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public FullTextEntityManager getFullTextEntityManager() {
		
		return Search.getFullTextEntityManager(getEntityManager());
	}

	public JdbcTemplate getJdbcTemplate() {
		/**
		 * 多租户开关，为"true"时关闭，默认打开
		 */
		if (!MultiTenantUtil.turnOnMulitTenant()) {
			return (JdbcTemplate)SpringContextHolder.getBean("jdbcTemplate");
		}else{
			return new JdbcTemplate(DataSourceContainer.getDataSource(FacesContext.getCurrentTenant()));
		}
	}
	

	/**
	 * 
	 * 添加日志
	 * @param moduleid 日志模块id     Context.log_type_product
	 * @param modularname 日志模块名称：预定、订单、运控……   Context.log_type_notice_name
	 * @param content 日志内容
	 * @param ope_type 操作状态  1 增加；2 修改；3 删除  Context.log_state_add
	 * @param bussinessType 业务类型 1 单团；2 散拼；3 游学；4 大客户；5 自由行；6 签证；7 机票；10 游轮；11 酒店；12 海岛游；
	 * @return
	 */
	public String saveLogOperate(String moduleid, String modularname, String content, String ope_type, Integer bussinessType, Long bussinessId){
		
		User user = UserUtils.getUser();
		// 未登录，则跳转到登录页
		if(user.getId() == null){
			return "redirect:"+Global.getAdminPath()+"/login";
		}
		
		
		LogOperate logOpe = new LogOperate();
		String id =  UUID.randomUUID().toString();
		
		Date createDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datestr = sdf.format(createDate);
		
		String loginname = user.getLoginName();
		logOpe.setId(id);
		logOpe.setOpe_id(user.getId());
		logOpe.setOpe_loginname(loginname);
		logOpe.setOpe_name(user.getName());
		logOpe.setOpe_comid(user.getCompany().getId());
		logOpe.setOpe_comname(user.getCompany().getCompanyName());
		logOpe.setModular_id(moduleid);
		logOpe.setModular_name(modularname);
		logOpe.setOpe_type(ope_type);
		logOpe.setContent(content);
		logOpe.setCreate_date(datestr);
		logOpe.setBussiness_type(bussinessType);
		logOpe.setBussiness_id(bussinessId);
		logOpeDao.save(logOpe);
		
		return "";
	}
	
	
	/**
	 * 保存对象或者更新对象时，增加默认的创建人信息等 add by zhanghao
	 * 数据库ID数据类型不统一导致映射类出现Long和Integer俩种数据类型，此处用反射自动处理相应的数据类型
	 * @param obj
	 * @param type
	 */
	protected void  setOptInfo(Object obj,String type) {
		User user = UserUtils.getUser();
		if(user != null) {
			if(type==null ||(StringUtils.isNotBlank(type)&&type.equals(OPERATION_ADD))){
				try {
					
					Field[] fields = obj.getClass().getDeclaredFields();
					for(Field field:fields){
						if(field.getName().equals("createBy")){
							if(field.getType().toString().indexOf("java.lang.Long")>-1){
								Reflections.setFieldValue(obj, "createBy", user.getId());
							}else if(field.getType().toString().indexOf("java.lang.Integer")>-1){
								Reflections.setFieldValue(obj, "createBy", Integer.parseInt(user.getId()+""));
							}
						}else if(field.getName().equals("updateBy")){
							if(field.getType().toString().indexOf("java.lang.Long")>-1){
								Reflections.setFieldValue(obj, "updateBy", user.getId());
							}else if(field.getType().toString().indexOf("java.lang.Integer")>-1){
								Reflections.setFieldValue(obj, "updateBy", Integer.parseInt(user.getId()+""));
							}
						}
					}
					
				} catch (Exception e) {
					/*logger.error(obj.getClass()+"不存在属性或类型错误:createBy,updateBy 。set方法失败！" );*/
				}
				try {
					Reflections.setFieldValue(obj, "createDate", new Date());
				} catch (Exception e) {
					/*logger.error(obj.getClass()+"不存在属性或类型错误:createDate 。set方法失败！" );*/
				}
				try {
					Reflections.setFieldValue(obj, "updateDate", new Date());
				} catch (Exception e) {
					/*logger.error(obj.getClass()+"不存在属性或类型错误:updateDate 。set方法失败！" );*/
				}
				try {
					Reflections.setFieldValue(obj, "delFlag", "0");
				} catch (Exception e) {
					/*logger.error(obj.getClass()+"不存在属性或类型错误:delFlag 。set方法失败！" );*/
				}
				try {
					String uuid = (String)Reflections.getFieldValue(obj, "uuid");
					if(StringUtils.isEmpty(uuid)){
						Reflections.setFieldValue(obj, "uuid", UuidUtils.generUuid());
					}
				} catch (Exception e) {
					/*logger.error(obj.getClass()+"不存在属性或类型错误:uuid 。set方法失败！" );*/
				}
			}else if(StringUtils.isNotBlank(type)&&type.equals(OPERATION_UPDATE)){

				try {
					
					Field[] fields = obj.getClass().getDeclaredFields();
					for(Field field:fields){
						if(field.getName().equals("updateBy")){
							if(field.getType().toString().indexOf("java.lang.Long")>-1){
								Reflections.setFieldValue(obj, "updateBy", user.getId());
							}else if(field.getType().toString().indexOf("java.lang.Integer")>-1){
								Reflections.setFieldValue(obj, "updateBy", Integer.parseInt(user.getId()+""));
							}
							break;
						}
					}
					
				} catch (Exception e) {
					/*logger.error(obj.getClass()+"不存在属性或类型错误:updateBy 。set方法失败！" );*/
				}
				try {
					Reflections.setFieldValue(obj, "updateDate", new Date());
				} catch (Exception e) {
					/*logger.error(obj.getClass()+"不存在属性或类型错误:updateDate 。set方法失败！" );*/
				}
			
			}
		}
	}
}
