/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;

import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatterValue;

import java.util.*;

import com.trekiz.admin.modules.hotelPl.dao.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlPreferentialMatterValueDaoImpl extends BaseDaoImpl<HotelPlPreferentialMatterValue>  implements HotelPlPreferentialMatterValueDao{
	@Override
	public HotelPlPreferentialMatterValue getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlPreferentialMatterValue hotelPlPreferentialMatterValue where hotelPlPreferentialMatterValue.uuid=? and hotelPlPreferentialMatterValue.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlPreferentialMatterValue)entity;
		}
		return null;
	}
	
	/**
	 * 根据优惠信息uuid获取优惠事项信息
	*<p>Title: findMatterByPreferentialUuid</p>
	* @return HotelPlPreferentialMatter 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午10:51:07
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<HotelPlPreferentialMatterValue> findMatterValueListByPreferentialUuid(String preferentialUuid){
		List<HotelPlPreferentialMatterValue> list = super.createQuery("from HotelPlPreferentialMatterValue hotelPlPreferentialMatterValue where hotelPlPreferentialMatterValue.hotelPlPreferentialUuid=? and hotelPlPreferentialMatterValue.delFlag=?", preferentialUuid, BaseEntity.DEL_FLAG_NORMAL).list();
		return list;
	}
	
	public int deleteByPlPreferentialUuids(List<String> preferentialUuids) {
		if(CollectionUtils.isEmpty(preferentialUuids)) {
			return 0;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM hotel_pl_preferential_matter_value WHERE hotel_pl_preferential_uuid IN ");
		sb.append("(");
		for(String preferentialUuid : preferentialUuids) {
			sb.append("'" + preferentialUuid + "',");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		
		return super.createSqlQuery(sb.toString()).executeUpdate();
	}
	
}
