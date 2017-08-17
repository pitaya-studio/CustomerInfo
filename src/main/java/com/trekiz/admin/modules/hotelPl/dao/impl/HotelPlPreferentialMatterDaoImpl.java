/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao.impl;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialMatterDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialMatterValueDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialTaxDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatter;
import com.trekiz.admin.modules.preferential.dao.PreferentialTemplatesDao;
import com.trekiz.admin.modules.preferential.entity.PreferentialTemplates;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelPlPreferentialMatterDaoImpl extends BaseDaoImpl<HotelPlPreferentialMatter>  implements HotelPlPreferentialMatterDao{
	@Autowired
	private HotelPlPreferentialTaxDao hotelPlPreferentialTaxDao;
	@Autowired
	private PreferentialTemplatesDao preferentialTemplatesDao;
	@Autowired
	private HotelPlPreferentialMatterValueDao hotelPlPreferentialMatterValueDao;
	@Override
	public HotelPlPreferentialMatter getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelPlPreferentialMatter hotelPlPreferentialMatter where hotelPlPreferentialMatter.uuid=? and hotelPlPreferentialMatter.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelPlPreferentialMatter)entity;
		}
		return null;
	}
	
	public HotelPlPreferentialMatter findMatterByPreferentialUuid(String preferentialUuid) {
		Object entity = super.createQuery("from HotelPlPreferentialMatter hotelPlPreferentialMatter where hotelPlPreferentialMatter.hotelPlPreferentialUuid=? and hotelPlPreferentialMatter.delFlag=?", preferentialUuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			//加载价单要求的基本数据
			HotelPlPreferentialMatter matter = (HotelPlPreferentialMatter)entity;
			matter.setPreferentialTaxMap(hotelPlPreferentialTaxDao.getTaxMapByPreferentialUuid(preferentialUuid));
			
			PreferentialTemplates preferentialTemplates = preferentialTemplatesDao.getByUuid(matter.getPreferentialTemplatesUuid());
			if(preferentialTemplates != null) {
				matter.setType(preferentialTemplates.getType());
				
				matter.setPreferentialTemplatesText(preferentialTemplates.getName());
			}
			
			matter.setMatterValues(hotelPlPreferentialMatterValueDao.findMatterValueListByPreferentialUuid(preferentialUuid));
			return matter;
		}
		return null;
	}
	
	public int deleteByPlPreferentialUuids(List<String> preferentialUuids) {
		if(CollectionUtils.isEmpty(preferentialUuids)) {
			return 0;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM hotel_pl_preferential_matter WHERE hotel_pl_preferential_uuid IN ");
		sb.append("(");
		for(String preferentialUuid : preferentialUuids) {
			sb.append("'" + preferentialUuid + "',");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		
		return super.createSqlQuery(sb.toString()).executeUpdate();
	}
	
}
