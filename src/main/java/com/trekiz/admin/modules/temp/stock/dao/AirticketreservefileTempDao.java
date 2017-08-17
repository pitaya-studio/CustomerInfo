/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.temp.stock.entity.AirticketreservefileTemp;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface AirticketreservefileTempDao  extends BaseDao<AirticketreservefileTemp> {
	
	public AirticketreservefileTemp getByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据机票草稿箱uuid获取草稿箱上传文件集合
	 * @Description: 
	 * @param @param reserveTempUuid
	 * @param @return   
	 * @return List<AirticketreservefileTemp>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-24 下午8:08:18
	 */
	public List<AirticketreservefileTemp> getByReserveTempUuid(String reserveTempUuid);
	
}
