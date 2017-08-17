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
import com.trekiz.admin.common.persistence.Page;

import com.trekiz.admin.modules.preferential.entity.PreferentialDictTemplatesRel;

import java.util.*;

import com.trekiz.admin.modules.preferential.dao.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class PreferentialDictTemplatesRelDaoImpl extends BaseDaoImpl<PreferentialDictTemplatesRel>  implements PreferentialDictTemplatesRelDao{
	@Override
	public PreferentialDictTemplatesRel getByUuid(String uuid) {
		Object entity = super.createQuery("from PreferentialDictTemplatesRel preferentialDictTemplatesRel where preferentialDictTemplatesRel.uuid=? and preferentialDictTemplatesRel.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (PreferentialDictTemplatesRel)entity;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findDictUuidAndDictNameByType(Integer type) {
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select dict.uuid,dict.name from preferential_dict_unit_rel dictUnitRel");
		sql.append(" LEFT JOIN preferential_dict dict ON dictUnitRel.dict_uuid = dict.uuid");
		sql.append(" WHERE dict.type=? AND dictUnitRel.delFlag=?");
		
		SQLQuery query = super.createSqlQuery(sql.toString(), type, BaseEntity.DEL_FLAG_NORMAL);
		List<Object[]> list = query.list();
		if(CollectionUtils.isNotEmpty(list)) {
			for(Object[] obj : list) {
				Map<String, String> map = new HashMap<String,String>();
				map.put("dictUuid", String.valueOf(obj[0]));
				map.put("dictName", String.valueOf(obj[1]));
				results.add(map);
			}
		}
		
		return results;
	}
	
	public Page<Map<String, String>> findTemplatesInfos(Page<Map<String, String>> page, String tempName) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT temp.UUID,temp.NAME,dict.name AS dictName,unit.name AS unitName,dict.type FROM preferential_templates temp");
		sql.append(" LEFT JOIN preferential_dict_templates_rel dictTempRel ON temp.uuid = dictTempRel.templates_uuid");
		sql.append(" LEFT JOIN preferential_dict dict ON dict.uuid = dictTempRel.dict_uuid");
		sql.append(" LEFT JOIN preferential_unit unit ON unit.uuid = dictTempRel.unit_uuid");
		sql.append(" WHERE temp.delFlag = ?");
		sql.append(" AND temp.NAME like ?");
		page.setOrderBy("temp.id ASC,dict.type ASC");

		return super.findPageBySql(page, sql.toString(), Map.class, BaseEntity.DEL_FLAG_NORMAL, tempName != null ? "%"+tempName+"%" : "%%");
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findTempsInfoByTempsUuid(String uuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT dictTempRel.templates_uuid,dict.name AS dictName,unit.name AS unitName,dict.type,dict.data_type AS dataType FROM preferential_dict_templates_rel dictTempRel");
		sql.append(" LEFT JOIN preferential_dict dict ON dict.uuid = dictTempRel.dict_uuid");
		sql.append(" LEFT JOIN preferential_unit unit ON unit.uuid = dictTempRel.unit_uuid");
		sql.append(" WHERE dictTempRel.delFlag = ? AND dictTempRel.templates_uuid = ?");
		sql.append(" ORDER BY dict.type ASC");
		SQLQuery query = super.createSqlQuery(sql.toString(), BaseEntity.DEL_FLAG_NORMAL, uuid);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findRelInfoByTempUuid(String tempUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT dictTempRel.uuid, dictTempRel.templates_uuid,dictTempRel.dict_uuid,dictTempRel.unit_uuid,dict.type FROM preferential_dict_templates_rel dictTempRel");
		sql.append(" LEFT JOIN preferential_dict dict ON dict.uuid = dictTempRel.dict_uuid");
		sql.append(" WHERE dictTempRel.delFlag = ? AND dictTempRel.templates_uuid = ?");
		SQLQuery query = super.createSqlQuery(sql.toString(), BaseEntity.DEL_FLAG_NORMAL, tempUuid);
		
		return query.list();
	}
	
	public int removeByTemplatesUuid(String templatesUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE preferential_dict_templates_rel SET delFlag = ? WHERE templates_uuid = ? AND delFlag = ?");
		
		SQLQuery query = super.createSqlQuery(sql.toString(), BaseEntity.DEL_FLAG_DELETE, templatesUuid, BaseEntity.DEL_FLAG_NORMAL);
		return query.executeUpdate();
	}
	
}
