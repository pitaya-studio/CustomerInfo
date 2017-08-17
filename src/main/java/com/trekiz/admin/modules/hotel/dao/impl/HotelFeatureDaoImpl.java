/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.hotel.dao.HotelFeatureDao;
import com.trekiz.admin.modules.hotel.entity.HotelFeature;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelFeatureDaoImpl extends BaseDaoImpl<HotelFeature>  implements HotelFeatureDao{
	
	public HotelFeature getByUuid(String value){
		HotelFeature h = (HotelFeature)super.getSession().createQuery("from HotelFeature hf where hf.uuid='"+value+"'").uniqueResult();
		return h;
	}
	
}
