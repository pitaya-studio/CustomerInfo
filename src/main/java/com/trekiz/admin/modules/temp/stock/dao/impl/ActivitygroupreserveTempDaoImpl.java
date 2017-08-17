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
import com.trekiz.admin.modules.temp.stock.dao.ActivitygroupreserveTempDao;
import com.trekiz.admin.modules.temp.stock.entity.ActivitygroupreserveTemp;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivitygroupreserveTempDaoImpl extends BaseDaoImpl<ActivitygroupreserveTemp>  implements ActivitygroupreserveTempDao{
	@Override
	public ActivitygroupreserveTemp getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivitygroupreserveTemp activitygroupreserveTemp where activitygroupreserveTemp.uuid=? and activitygroupreserveTemp.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivitygroupreserveTemp)entity;
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
		int count = super.createSqlQuery("update activitygroupreserve_temp set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
	/**
	 * 根据散拼切位临时表获取散拼切位临时集合
	 * @Description: 
	 * @param @param reserveTempUuids
	 * @param @return   
	 * @return List<ActivitygroupreserveTemp>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-23 下午8:09:50
	 */
	@SuppressWarnings("unchecked")
	public List<ActivitygroupreserveTemp> getByUuids(List<String> reserveTempUuids) {
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
		
		return super.createSqlQuery("select * from activitygroupreserve_temp reserveTemp where reserveTemp.uuid in ("+ sb.toString() +") and reserveTemp.delFlag=? ", BaseEntity.DEL_FLAG_NORMAL).addEntity(ActivitygroupreserveTemp.class).list();
	}
	/**
	 * 修改activityreservefile_temp的delFlag
	 * @author chao.zhang
	 */
	@Override
	public void delFile(Long docId,String uuid) {
		super.updateBySql("update activityreservefile_temp set delFlag='1' where srcDocId=? and reserve_temp_uuid=?", docId.intValue(),uuid);
	}
	
}
