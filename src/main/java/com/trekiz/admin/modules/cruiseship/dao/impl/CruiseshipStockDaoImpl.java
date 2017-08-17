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
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStock;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class CruiseshipStockDaoImpl extends BaseDaoImpl<CruiseshipStock>  implements CruiseshipStockDao{
	@Override
	public CruiseshipStock getByUuid(String uuid) {
		Object entity = super.createQuery("from CruiseshipStock cruiseshipStock where cruiseshipStock.uuid=? and cruiseshipStock.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (CruiseshipStock)entity;
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
		int count = super.createSqlQuery("update cruiseship_stock set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
	/**
	 * 根据游轮信息uuid获取该游轮下所有的船期
	 * @Description: 
	 * @param @param shipInfoUuid
	 * @param @return   
	 * @return List<Date>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	@SuppressWarnings("unchecked")
	public List<CruiseshipStock> getStocksByShipInfoUuid(String cruiseshipInfoUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT UUID AS cruiseshipStockUuid,ship_date AS shipDate FROM cruiseship_stock WHERE cruiseship_info_uuid=? AND delFlag=?");
		return (List<CruiseshipStock>)super.findCustomObjBySql(sql.toString(), CruiseshipStock.class, cruiseshipInfoUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
    /**
     * 获得游轮的库存信息(游轮名称,uuid)-223
     */
	@Override
	public List<Map<String,Object>> getCruiseshipNamesUuids() {
		StringBuffer sqlString=new StringBuffer();
		sqlString.append(" SELECT ")
				 .append(" ci.name AS cruiseship_name, cs.cruiseship_info_uuid AS cruiseship_uuid ")
				 .append(" FROM cruiseship_stock cs ")
				 .append(" LEFT JOIN cruiseship_info ci ")
				 .append(" ON cs.cruiseship_info_uuid=ci.uuid ")
				 .append(" WHERE ci.delFlag='0' ")
				 .append(" AND cs.wholesaler_id='"+UserUtils.getUser().getCompany().getId()+"'")
				 .append(" GROUP BY cs.cruiseship_info_uuid ");//分组的目的是为了去重,因为cs表中游轮uuid相同的有多个(一个游轮对应多个船期所致)
		return super.findBySql(sqlString.toString(), Map.class);
	}
   
	/**
	 * 223-tgy
	 * 根据游轮的uuid获取该游轮的所有效船期.
	 */
	@Override
	public List<Map<String, Object>> getShipDateByCruiseUuid(String cruiseshipUUid) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT cs.ship_date AS ship_date")
		   .append(" FROM cruiseship_stock cs")
		   .append(" WHERE cs.cruiseship_info_uuid='"+cruiseshipUUid+"' ")
		   .append(" AND cs.delFlag='0' ")
		   .append(" AND cs.wholesaler_id='"+UserUtils.getUser().getCompany().getId()+"'  ")//增加了批发商的限制
		   .append(" ORDER BY cs.ship_date ASC ");
		return super.findBySql(sql.toString(), Map.class);
	}
	
}
