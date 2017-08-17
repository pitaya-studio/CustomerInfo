/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao.impl;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockGroupRelDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class CruiseshipStockGroupRelDaoImpl extends BaseDaoImpl<CruiseshipStockGroupRel>  implements CruiseshipStockGroupRelDao{
	@Override
	public CruiseshipStockGroupRel getByUuid(String uuid) {
		Object entity = super.createQuery("from CruiseshipStockGroupRel cruiseshipStockGroupRel where cruiseshipStockGroupRel.uuid=? and cruiseshipStockGroupRel.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (CruiseshipStockGroupRel)entity;
		}
		return null;
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
		int count = super.createSqlQuery("update cruiseship_stock_group_rel set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
	/**
	 * 根据库存uuid和关联状态获取关联产品信息
	 * @Description: 
	 * @param @param cruiseshipStockUuid
	 * @param @param queryStatus
	 * @param @return   
	 * @return List<CruiseshipStockGroupRel>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	@SuppressWarnings("unchecked")
	public List<CruiseshipStockGroupRel> queryRelProducts(String cruiseshipStockUuid, String queryStatus) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  ");
		sql.append(" DISTINCT activity.acitivityName AS activityName,  ");
		sql.append(" rel.activity_id AS activityId, ");
		sql.append(" sd.label AS fromArea, ");
		sql.append(" rel.activity_type AS activityType ");
		sql.append(" FROM cruiseship_stock_group_rel rel ");
		sql.append(" LEFT JOIN travelactivity activity ON activity.id = rel.activity_id ");
		sql.append(" LEFT JOIN sys_dict sd on sd.`value` =  activity.fromArea ");
		sql.append(" WHERE sd.type = 'from_area' and  rel.cruiseship_stock_uuid=? AND rel.STATUS in (0,1) AND rel.delFlag=?");
		//临时
//		return (List<CruiseshipStockGroupRel>)super.findCustomObjBySql(sql.toString(), CruiseshipStockGroupRel.class, cruiseshipStockUuid,"0", BaseEntity.DEL_FLAG_NORMAL);
//		return (List<CruiseshipStockGroupRel>)super.findCustomObjBySql(sql.toString(), CruiseshipStockGroupRel.class, cruiseshipStockUuid, queryStatus, BaseEntity.DEL_FLAG_NORMAL);
		return (List<CruiseshipStockGroupRel>)super.findCustomObjBySql(sql.toString(), CruiseshipStockGroupRel.class, cruiseshipStockUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	
	/**
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @author xinwei.wang
	 * @date 2016年3月9日下午4:42:32
	 * @param cruiseshipUUid
	 * @param shipdate
	 * @return    
	 * @throws
	 */
	@Override
	public CruiseshipStockGroupRel getCruiseShipRelByActivityGroupId(Integer activitygroupid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT csgr.id,csgr.UUID FROM cruiseship_stock_group_rel csgr WHERE csgr.STATUS=0 and csgr.activitygroup_id = " +activitygroupid);
		List<Map<String, Object>> temp =super.findBySql(sql.toString(), Map.class);
		CruiseshipStockGroupRel cruiseshipStockGroupRel = null;
		Integer cruiseship_stock_group_rel = null;
		if (null!=temp && temp.size()>0) {
			cruiseship_stock_group_rel = (Integer)temp.get(0).get("id");
			cruiseshipStockGroupRel = getById(cruiseship_stock_group_rel);
		}
		return cruiseshipStockGroupRel;
	}
    /**
     * 223:tgy
     * 根据团期id查询表cruiseship_group_rel和activitygroup表,获得关联状态,cruiseship_stock_detail表的id,关联日期和操作人
     */
	@Override
	public List<Map<String, Object>> getRelInfo(String agId) {
		StringBuffer sql=new StringBuffer();
		sql.append(" SELECT csgr.STATUS AS rel_status,su.name AS operator,csgr.createDate AS rel_date,csgr.cruiseship_stock_detail_id AS csd_id ")
		   .append(" FROM cruiseship_stock_group_rel csgr ")
		   .append(" LEFT JOIN activitygroup a ")
		   .append(" ON  csgr.activitygroup_id=a.id ")
		   .append(" LEFT JOIN sys_user su ")
		   .append(" ON  csgr.createBy=su.id ")
		   .append(" WHERE a.id='"+agId+"'  ")
		   .append(" AND csgr.delFlag='0' AND csgr.wholesaler_id='"+UserUtils.getUser().getCompany().getId()+"' ");
		return super.findBySql(sql.toString(), Map.class);
	}
	
}
