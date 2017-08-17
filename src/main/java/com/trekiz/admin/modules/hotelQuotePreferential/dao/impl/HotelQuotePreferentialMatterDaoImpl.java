/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.dao.impl;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialMatterDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialMatterValueDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialTaxDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatter;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatterValue;
import com.trekiz.admin.modules.preferential.dao.PreferentialTemplatesDao;
import com.trekiz.admin.modules.preferential.entity.PreferentialTemplates;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialMatterDaoImpl extends BaseDaoImpl<HotelQuotePreferentialMatter>  implements HotelQuotePreferentialMatterDao{
	@Autowired
	private PreferentialTemplatesDao preferentialTemplatesDao;
	@Autowired
	private HotelQuotePreferentialTaxDao hotelQuotePreferentialTaxDao;
	@Autowired
	private HotelQuotePreferentialMatterValueDao hotelQuotePreferentialMatterValueDao;
	
	@Override
	public HotelQuotePreferentialMatter getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelQuotePreferentialMatter hotelQuotePreferentialMatter where hotelQuotePreferentialMatter.uuid=? and hotelQuotePreferentialMatter.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelQuotePreferentialMatter)entity;
		}
		return null;
	}
	
	public HotelQuotePreferentialMatter findMatterByPreferentialUuid(String preferentialUuid) {
		Object entity = super.createQuery("from HotelQuotePreferentialMatter hotelQuotePreferentialMatter where hotelQuotePreferentialMatter.hotelQuotePreferentialUuid=? and hotelQuotePreferentialMatter.delFlag=?", preferentialUuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			//加载价单要求的基本数据
			HotelQuotePreferentialMatter matter = (HotelQuotePreferentialMatter)entity;
		    matter.setPreferentialTaxMap(hotelQuotePreferentialTaxDao.getTaxMapByPreferentialUuid(preferentialUuid));
			PreferentialTemplates preferentialTemplates = preferentialTemplatesDao.getByUuid(matter.getPreferentialTemplatesUuid());
			if(preferentialTemplates != null) {
				String type = preferentialTemplates.getType();
				matter.setType(type);
				matter.setPreferentialTemplatesText(preferentialTemplates.getName());
				//目前系统中就有这么多模板类型
				String detailText = preferentialTemplates.getOutHtml();
				List<HotelQuotePreferentialMatterValue> valueList = hotelQuotePreferentialMatterValueDao.findMatterValueListByPreferentialUuid(preferentialUuid);
				if("1".equals(type)){//住付优惠
					if(CollectionUtils.isNotEmpty(valueList)){
						for(HotelQuotePreferentialMatterValue val : valueList){
							if(val.getMyKeyvar().equals("liveNights")){
								detailText = detailText.replace("<input type=\"text\" name=\"liveNights\" id=\"liveNights\" />", val.getMyValue());
							}else {
								detailText = detailText.replace("<input type=\"text\" name=\"freeNights\" id=\"freeNights\" />", val.getMyValue());
							}
						}
					}
				}else if ("2".equals(type)){//提前预定
					if(CollectionUtils.isNotEmpty(valueList)){
						detailText = detailText.replace("<input type=\"text\" name=\"earlyDays\" id=\"earlyDays\" />", valueList.get(0).getMyValue());
					}
				}else if ("3".equals(type)){//打包价格
					if(CollectionUtils.isNotEmpty(valueList)){
						detailText = detailText.replace("<input type=\"text\" name=\"totalPrice\" id=\"totalPrice\" class=\"currency\" style=\"width: 60px;padding-right: 10px\"/>", valueList.get(0).getMyValue());
					}
				}else {//房/餐/交通优惠
					
				}
				matter.setPreferentialTemplatesDetailText(detailText);
			}
			return matter;
		}
		return null;
	}
}
