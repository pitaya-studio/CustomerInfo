/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.temp.stock.entity.ActivitygroupreserveTemp;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface ActivitygroupreserveTempDao  extends BaseDao<ActivitygroupreserveTemp> {
	
	public ActivitygroupreserveTemp getByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
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
	public List<ActivitygroupreserveTemp> getByUuids(List<String> reserveTempUuids);
	/**
	 * 删除docInfo（修改期delFlage状态）
	 * @param docId
	 * @author chao.zhang
	 */
	public void delFile(Long docId,String uuid);
}
