/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipInfo;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface CruiseshipInfoDao  extends BaseDao<CruiseshipInfo> {
	
	public CruiseshipInfo getByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据批发商id获取该批发商下所有的游轮基础信息
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return List<CruiseshipInfo>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-23
	 */
	public List<CruiseshipInfo> findByWholesalerId(Long wholesalerId);
	
}
