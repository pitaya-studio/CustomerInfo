/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRel;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelQuotePreferentialRelDao  extends BaseDao<HotelQuotePreferentialRel> {
	
	public HotelQuotePreferentialRel getByUuid(String uuid);
	
	public List<HotelQuotePreferentialRel> getPreferentialRelsByPreferentialUuid(String preferentialUuid);
}
