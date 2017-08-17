/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipAnnexDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipAnnex;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class CruiseshipAnnexDaoImpl extends BaseDaoImpl<CruiseshipAnnex>  implements CruiseshipAnnexDao{
	
	@Autowired
	private DocInfoDao docInfoDao;
	
	@Override
	public CruiseshipAnnex getByUuid(String uuid) {
		Object entity = super.createQuery("from CruiseshipAnnex cruiseshipAnnex where cruiseshipAnnex.uuid=? and cruiseshipAnnex.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (CruiseshipAnnex)entity;
		}
		return null;
	}
	
	/**
	 * by songyang 
	 */
	@Override
	public List<CruiseshipAnnex> getByStockUuid(String stockUuid){
		return super.find("from CruiseshipAnnex cruiseshipAnnex where cruiseshipAnnex.mainUuid=? and cruiseshipAnnex.delFlag=?",stockUuid,BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public boolean batchDelete(String[] uuids) {
		if(ArrayUtils.isEmpty(uuids)) {
			return false;
		}
		
		StringBuffer sb = new StringBuffer();
		for(String uuid : uuids) {
			sb.append("'");
			sb.append(uuid);
			sb.append("'");
			sb.append(",");
		}
		
		sb.deleteCharAt(sb.length()-1);
		int count = super.createSqlQuery("update cruiseship_annex set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void synDocInfo(String mainUuid ,int type,int wholesalerId, List<CruiseshipAnnex> axList){
		List<CruiseshipAnnex> axoldlist = super.createQuery("from CruiseshipAnnex CruiseshipAnnex where CruiseshipAnnex.mainUuid=? and CruiseshipAnnex.type=? and CruiseshipAnnex.delFlag='0'", mainUuid,type).list();
		
		if(CollectionUtils.isNotEmpty(axList)){
			for(CruiseshipAnnex ha:axList){
				if(!tranferList2Map(axoldlist).containsKey(ha.getDocId().toString())){//新的数据不在数据库中 是 新增
					ha.setCreateBy(Integer.parseInt(UserUtils.getUser().getId().toString()));
					ha.setCreateDate(new Date());
					ha.setUpdateBy(Integer.parseInt(UserUtils.getUser().getId().toString()));
					ha.setUpdateDate(new Date());
					ha.setDelFlag("0");
					ha.setUuid(UuidUtils.generUuid());
					ha.setMainUuid(mainUuid);
					ha.setType(type);
					ha.setWholesalerId(wholesalerId);
					super.saveObj(ha);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(axoldlist)){
			for(CruiseshipAnnex ha:axoldlist){
				if(!tranferList2Map(axList).containsKey(ha.getDocId().toString())){//数据库中的数据不在新的数据中 是删除
					ha.setDelFlag("1");
					super.updateObj(ha);
					docInfoDao.delDocInfoById(Long.parseLong(ha.getDocId().toString()));
				}
			}
		}
		
	}
	
	private Map<String,CruiseshipAnnex> tranferList2Map(List<CruiseshipAnnex> list){
		Map<String,CruiseshipAnnex> map = new HashMap<String,CruiseshipAnnex>();
		if(list!=null){
			for(CruiseshipAnnex ha:list){
				map.put(ha.getDocId().toString(), ha);
			}
		}
		return map;
	}
}
