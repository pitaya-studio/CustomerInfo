/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelControl;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface HotelControlDao  extends BaseDao<HotelControl> {
	
	public HotelControl getByUuid(String uuid);
	
	public <E> Page<E> findBySql(Page<E> page, String sqlString,String oldsqlString, Class<?> resultClass, Object... parameter);
}
