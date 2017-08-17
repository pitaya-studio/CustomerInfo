/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.SysDict;

import com.trekiz.admin.modules.hotel.dao.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class SysCompanyDictViewDaoImpl extends BaseDaoImpl<SysCompanyDictView>  implements SysCompanyDictViewDao{
	
	public int findMaxValueByCompanyIdAndType(String type) {
		String sql = "select max(CONVERT(value,UNSIGNED)) from sys_company_dict_view s where s.type = ?";

		Query query = super.createSqlQuery(sql, type);
		Object obj = query.uniqueResult();
		if(obj != null) {
			return Integer.parseInt(obj.toString());
		}
		return 0;
	}
	
	public SysCompanyDictView getByUuId(String uuid){
		Object entity = super.createQuery("from SysCompanyDictView sysCompanyDictView where sysCompanyDictView.uuid=? and sysCompanyDictView.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (SysCompanyDictView)entity;
		}
		return null;
	}
	
	
	@SuppressWarnings("unused")
	private String getColumnsSql(){
		String sql = "id,label,value,type,defaultFlag,sort,description,companyId,createBy,createDate,updateBy,updateDate,remarks,delFlag,uuid";
		return sql;
	}
	
	public List<SysCompanyDictView> findByUuids(String[] uuids) {
		List<SysCompanyDictView> list = new ArrayList<SysCompanyDictView>();
		if(uuids != null && uuids.length > 0) {
			for(String uuid : uuids) {
				if(null!=this.getByUuId(uuid)){
					SysCompanyDictView entity = new SysCompanyDictView();
					BeanUtil.copySimpleProperties(entity, this.getByUuId(uuid));
					list.add(entity);
				}
			}
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysDict> findByType(String type) {
		return super.createQuery("from SysDict sysDict where sysDict.type=? and sysDict.delFlag=?", type, BaseEntity.DEL_FLAG_NORMAL).list();
	}
	
}
