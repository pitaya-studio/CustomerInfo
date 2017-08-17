/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockDetailDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class CruiseshipStockDetailDaoImpl extends BaseDaoImpl<CruiseshipStockDetail>  implements CruiseshipStockDetailDao{
	@Override
	public CruiseshipStockDetail getByUuid(String uuid) {
		Object entity = super.createQuery("from CruiseshipStockDetail cruiseshipStockDetail where cruiseshipStockDetail.uuid=? and cruiseshipStockDetail.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (CruiseshipStockDetail)entity;
		}
		return null;
	}
	
	@Override
	public CruiseshipStockDetail getByStockUuid(String stockUuid){
		Object entity = super.createQuery("from CruiseshipStockDetail cruiseshipStockDetail where cruiseshipStockDetail.cruiseshipStockUuid=? and cruiseshipStockDetail.delFlag=?", stockUuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (CruiseshipStockDetail)entity;
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
		int count = super.createSqlQuery("update cruiseship_stock_detail set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
	/**
	 * 根据邮轮uuid和船期日期获取所有的邮轮切位和余位信息
	 * @Description: 
	 * @param @param shipInfoUuid
	 * @param @param shipDate
	 * @param @return   
	 * @return List<CruiseshipStock>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	public List<CruiseshipStockDetail> getStockDetailInfos(String shipInfoUuid, Date shipDate) {
		return super.find("from CruiseshipStockDetail cruiseshipStockDetail where cruiseshipStockDetail.cruiseshipInfoUuid=? and cruiseshipStockDetail.shipDate=? and cruiseshipStockDetail.delFlag=?", shipInfoUuid, shipDate, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	/**
	 * 根据游轮库存uuid获取所有的船期信息
	 * @Description: 
	 * @param @param cruiseshipStockUuid
	 * @param @return   
	 * @return List<CruiseshipStockDetail>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	@SuppressWarnings("unchecked")
	public List<CruiseshipStockDetail> getStockDetailInfos(String cruiseshipStockUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" stockDetail.uuid AS cruiseshipStockDetailUuid, ");
		sql.append(" stockDetail.cruiseship_cabin_uuid AS cruiseshipCabinUuid, "); 
		sql.append(" cabin.name AS cruiseshipCabinName, ");
		sql.append(" stockDetail.stock_amount AS stockAmount, "); 
		sql.append(" stockDetail.free_position AS freePosition ");
		sql.append(" FROM cruiseship_stock_detail stockDetail ");
		sql.append(" LEFT JOIN cruiseship_cabin cabin ON cabin.uuid = stockDetail.cruiseship_cabin_uuid ");
		sql.append(" WHERE stockDetail.cruiseship_stock_uuid=? AND stockDetail.delFlag=?");
		return (List<CruiseshipStockDetail>)super.findCustomObjBySql(sql.toString(), CruiseshipStockDetail.class, cruiseshipStockUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	/**
     * 223-tgy:
     *  获得cruiseship_stock_detail表(以及cruiseship_cabin中对应的舱型名称)中的信息.
     */
	@Override
	public List<Map<String, Object>> getShipStockDetailByUuidAndShipdate(String cruiseshipUUid,String shipdate) {
		StringBuffer sql = new StringBuffer();
		/*sql.append(" SELECT csd.cruiseship_info_uuid AS cruiseship_uuid,")
		   .append(" csd.id AS detailId,")
		   .append(" csd.ship_date AS ship_date, ")
		   .append("  cc.name AS cabin_name,")
		   .append(" csd.stock_amount AS stock_amount,") 
		   .append(" csd.free_position AS free_positon") 
		   .append(" FROM cruiseship_stock_detail csd,cruiseship_cabin cc")
		   .append(" WHERE csd.cruiseship_info_uuid='"+cruiseshipUUid+"'")
		   .append(" AND csd.ship_date='"+shipdate+"'")
		   .append(" AND cc.cruiseship_info_uuid=csd.cruiseship_info_uuid  AND csd.delFlag='0'");*/
		sql.append(" SELECT csd.cruiseship_info_uuid AS cruiseship_uuid, ")
		   .append(" csd.id AS detailId, ")  
		   .append(" cc.name AS cabin_name, ")
		   .append(" csd.stock_amount AS stock_amount, ")
		   .append(" csd.free_position AS free_positon ") 
		   .append(" FROM cruiseship_stock_detail csd ")
		   .append(" LEFT JOIN cruiseship_cabin cc ")
		   .append(" ON csd.cruiseship_cabin_uuid=cc.uuid ")
		   .append(" WHERE csd.cruiseship_info_uuid='"+cruiseshipUUid+"'")
		   .append(" AND csd.ship_date='"+shipdate+"'")
		   .append(" AND csd.delFlag='0'")
		   .append(" AND cc.wholesaler_id='"+UserUtils.getUser().getCompany().getId()+"'");//分批发商,id为69的才可以
		//System.out.println("-----"+UserUtils.getUser().getCompany().getId());
		
		
		/*for (Map<String, Object> map : temp) {
			//TODO
		}
		
		
		List<Map<String, Object>> temp = super.findBySql(sql.toString(), Map.class);
		for (Map<String, Object> map : temp) {
			map.put("cruiseShipStockDetailId", "");
		}
		
		
		return temp;*/
		return super.findBySql(sql.toString(),Map.class);
	}
    /**
     * 223:tgy
     *  根据csd表的主键id查询船期信息:船期,游轮名称,创建时间,游轮的uuid,创建人的id
     */
	@Override
	public List<Map<String, Object>> doGetDetailsById(String keyId){
		
		/*CruiseshipStockDetail cruiseshipStockDetail = getById(keyId);
		String cruiseshipStockUuid =cruiseshipStockDetail.getCruiseshipStockUuid();
		String keyIds = null;
		int count=0;
		List<Map<String, Object>> tempIDS= super.findBySql("SELECT csd.id  FROM cruiseship_stock_detail csd WHERE csd.cruiseship_stock_uuid = '"+cruiseshipStockUuid+"'");
		for (Map<String, Object> map : tempIDS) {
			count+=1;
			if(count!=(tempIDS.size()-1)){
				keyIds+=map.get("id").toString();
				keyIds+=",";
			}else{
				keyIds+=map.get("id").toString();
			}
		}*/
		
		StringBuffer sql=new StringBuffer();
		   sql.append(" SELECT ")
		      //查询的字段:游轮的uuid,游轮的船期,游轮的名称,明细表中记录的创建日期(占位目的),关联日期,操作人,关联状态//
		   	 // .append(" csd.cruiseship_info_uuid AS ship_uuid,csd.ship_date AS ship_date,ci.name AS ship_name,csd.createDate AS create_date,csgr.createDate AS rel_date,su.name AS operator,csgr.STATUS AS rel_status ")
		   	  .append(" csd.cruiseship_info_uuid AS ship_uuid,csd.ship_date AS ship_date,ci.name AS ship_name,csd.createDate AS create_date")
		      .append(" FROM cruiseship_stock_detail csd  ")
		   	  .append(" LEFT JOIN cruiseship_info ci ")
		   	  .append(" ON csd.cruiseship_info_uuid=ci.uuid  ")
		   	  .append(" WHERE  csd.id='"+keyId+"'")
		   	  .append(" AND csd.delFlag='0' ")
		   	  .append(" AND csd.wholesaler_id='"+UserUtils.getUser().getCompany().getId()+"'");
		return super.findBySql(sql.toString(), Map.class);
	}
	
}
