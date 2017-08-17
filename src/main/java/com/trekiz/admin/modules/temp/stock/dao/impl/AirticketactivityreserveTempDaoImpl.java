/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.dao.impl;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.temp.stock.dao.AirticketactivityreserveTempDao;
import com.trekiz.admin.modules.temp.stock.entity.AirticketactivityreserveTemp;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class AirticketactivityreserveTempDaoImpl extends BaseDaoImpl<AirticketactivityreserveTemp>  implements AirticketactivityreserveTempDao{
	@Override
	public AirticketactivityreserveTemp getByUuid(String uuid) {
		Object entity = super.createQuery("from AirticketactivityreserveTemp airticketactivityreserveTemp where airticketactivityreserveTemp.uuid=? and airticketactivityreserveTemp.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (AirticketactivityreserveTemp)entity;
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
		int count = super.createSqlQuery("update airticketactivityreserve_temp set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}

	/**
	 * 修改activityreservefile_temp 的delFlag
	 * @author chao.zhang
	 */
	public void delFile(Long docId, String uuid) {
		super.updateBySql("update airticketreservefile_temp set delFlag='1' where srcDocId=? and reserve_temp_uuid=?", docId.intValue(),uuid);
		
	}
	
	/**
	 * 根据机票草稿箱uuid集合查询所有的机票草稿箱信息
	 * @Description: 
	 * @param @param reserveTempUuids
	 * @param @return   
	 * @return List<AirticketactivityreserveTemp>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-24 下午7:51:26
	 */
	@SuppressWarnings("unchecked")
	public List<AirticketactivityreserveTemp> getByUuids(List<String> reserveTempUuids) {
		if(CollectionUtils.isEmpty(reserveTempUuids)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(String reserveTempUuid : reserveTempUuids) {
			sb.append("'");
			sb.append(reserveTempUuid);
			sb.append("',");
		}
		
		sb.deleteCharAt(sb.length()-1);
		
		return super.createSqlQuery("select * from airticketactivityreserve_temp reserveTemp where reserveTemp.uuid in ("+ sb.toString() +") and reserveTemp.delFlag=? ", BaseEntity.DEL_FLAG_NORMAL).addEntity(AirticketactivityreserveTemp.class).list();
	}
	
}
