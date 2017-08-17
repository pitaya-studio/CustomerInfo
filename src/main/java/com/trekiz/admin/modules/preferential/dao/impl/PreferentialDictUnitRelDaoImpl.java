/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.dao.impl;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.preferential.entity.PreferentialDictUnitRel;

import java.util.*;

import com.trekiz.admin.modules.preferential.dao.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class PreferentialDictUnitRelDaoImpl extends BaseDaoImpl<PreferentialDictUnitRel>  implements PreferentialDictUnitRelDao{
	@Override
	public PreferentialDictUnitRel getByUuid(String uuid) {
		Object entity = super.createQuery("from PreferentialDictUnitRel preferentialDictUnitRel where preferentialDictUnitRel.uuid=? and preferentialDictUnitRel.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (PreferentialDictUnitRel)entity;
		}
		return null;
	}
	
	public List<Map<String, String>> getUnitsByDictUuid(String uuid) {
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT rel.unit_uuid,unit.name FROM preferential_dict_unit_rel rel");
		sql.append(" LEFT JOIN preferential_unit unit ON rel.unit_uuid = unit.uuid");
		sql.append(" WHERE rel.dict_uuid =? AND rel.delFlag=?");
		
		SQLQuery query = super.createSqlQuery(sql.toString(), uuid, BaseEntity.DEL_FLAG_NORMAL);

		List<?> list = (List<?>)query.list();
		
		if(CollectionUtils.isNotEmpty(list)) {
			for(Object obj : list) {
				Map<String, String> map = new HashMap<String, String>();
				Object[] objArr = (Object[])obj;
				map.put("unitUuid", String.valueOf(objArr[0]));
				map.put("unitName", String.valueOf(objArr[1]));
				datas.add(map);
			}
			return datas;
		}
		
		return datas;
	}
	
	/**
	 * 批量删除数据字典和单位的关联表 addbyzhanghao
	 * @param dictUuids
	 */
	public void removeByDictUuids(String[] dictUuids){
		org.hibernate.Query query = createQuery("delete PreferentialDictUnitRel preferentialDictUnitRel where preferentialDictUnitRel.delFlag=? and preferentialDictUnitRel.dictUuid in (:uuids)",  BaseEntity.DEL_FLAG_NORMAL);
		query.setParameterList("uuids", dictUuids);
		query.executeUpdate();
	}
}
