/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.temp.stock.entity.AirticketactivityreserveTemp;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface AirticketactivityreserveTempDao  extends BaseDao<AirticketactivityreserveTemp> {
	
	public AirticketactivityreserveTemp getByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	

	/**
	 * 修改activityreservefile_temp 的delFlag
	 * @param docId
	 * @param uuid
	 * @author chao.zhang
	 */
	public void delFile(Long docId, String uuid);
	

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
	public List<AirticketactivityreserveTemp> getByUuids(List<String> reserveTempUuids);
	

}
