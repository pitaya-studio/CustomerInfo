package com.trekiz.admin.modules.sys.repository;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.LogOperate;

/**
 * DAO自定义接口实现
 * @author wangxk
 */
@Repository
public class LogOperateSaveImpl extends BaseDaoImpl<LogOperate>  implements LogOperateSaveDao {
	
	
	public LogOperate findOne(String id){
		
		return null;
	};
	
	public void save(LogOperate logOpe){
		this.getSession().save(logOpe);
	}
	
	public static void main(String[] args) {
		LogOperate ope = new LogOperate();
		ope.setId("asdf1116");
		ope.setOpe_id(1);
		ope.setOpe_loginname("super");
		ope.setOpe_name("管理员");
		ope.setOpe_comid(2);
		ope.setOpe_comname("efengxing");
		
		ope.setModular_id("1");
		ope.setModular_name("产品");
		ope.setOpe_type("2");
		ope.setContent("添加旅游散拼产品，id是1232332");
		//ope.setCreate_date("2015-03-15 11:21:12");

		LogOperateSaveImpl dao = new LogOperateSaveImpl();
		dao.save(ope);
		
	}
	
}