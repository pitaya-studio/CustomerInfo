/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.dao.impl;

import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.temp.stock.dao.ActivityreservefileTempDao;
import com.trekiz.admin.modules.temp.stock.entity.ActivityreservefileTemp;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityreservefileTempDaoImpl extends BaseDaoImpl<ActivityreservefileTemp>  implements ActivityreservefileTempDao{
	@Override
	public ActivityreservefileTemp getByUuid(String uuid) {
		Object entity = super.createQuery("from ActivityreservefileTemp activityreservefileTemp where activityreservefileTemp.uuid=? and activityreservefileTemp.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (ActivityreservefileTemp)entity;
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
		int count = super.createSqlQuery("update activityreservefile_temp set delFlag=? where uuid in("+sb.toString()+")", BaseEntity.DEL_FLAG_DELETE).executeUpdate();
		
		if(count > 0){
			return true;
		}
			
		return false;
	}
	
	/**
	 * 根据草稿箱uuid获取草稿箱文件集合
	 * @Description: 
	 * @param @param reserveTempUuid
	 * @param @return   
	 * @return List<ActivityreservefileTemp>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-23 下午9:36:02
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityreservefileTemp> getFilesByReserveTempUuid(String reserveTempUuid) {
		return super.createQuery("from ActivityreservefileTemp activityreservefileTemp where activityreservefileTemp.reserveTempUuid=? and activityreservefileTemp.delFlag=?", reserveTempUuid, BaseEntity.DEL_FLAG_NORMAL).list();
	}
	
}
