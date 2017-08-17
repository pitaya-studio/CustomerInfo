/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import java.util.*;

import com.trekiz.admin.modules.hotelPl.entity.*;
import com.trekiz.admin.modules.hotelPl.module.bean.HotelPlRoom;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlDao  extends BaseDao<HotelPl> {
	
	public HotelPl getByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取去重后的酒店价单房型
	*<p>Title: getDistinctHotelPlRoomsByUuid</p>
	* @return List<HotelPlRoom> 返回类型
	* @author majiancheng
	* @date 2015-6-28 下午5:01:02
	* @throws
	 */
	public List<HotelPlRoom> getDistinctHotelPlRoomsByUuid(String hotelPlUuid);
	
	/**
	 * 根据“酒店名称”“采购类型”“地接供应商”3个条件来判断该价单是否已经存在
	*<p>Title: findIsExist</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-7-18 下午3:09:10
	* @throws
	 */
	public boolean findIsExist(String hotelUuid, int purchaseType, int supplierInfoId);
	
	/**
	 * 根据uuid数组删除酒店价单信息
		 * @Title: batchDelete
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-10-23 下午3:16:26
	 */
	public boolean batchDelete(String[] uuids);
}
