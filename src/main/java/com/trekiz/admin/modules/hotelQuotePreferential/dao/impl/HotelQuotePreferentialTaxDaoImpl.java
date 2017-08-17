/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.dao.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotel.dao.HotelMealDao;
import com.trekiz.admin.modules.hotel.dao.SysCompanyDictViewDao;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialTax;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialTaxDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialTax;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.traveler.dao.TravelerTypeDao;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialTaxDaoImpl extends BaseDaoImpl<HotelQuotePreferentialTax>  implements HotelQuotePreferentialTaxDao{
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private TravelerTypeDao travelerTypeDao;
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;
	@Autowired
	private HotelMealDao hotelMealDao;
	
	private static Map<String, String> travelerTypeMap = new HashMap<String, String>(); 
	
	@Override
	public HotelQuotePreferentialTax getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelQuotePreferentialTax hotelQuotePreferentialTax where hotelQuotePreferentialTax.uuid=? and hotelQuotePreferentialTax.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelQuotePreferentialTax)entity;
		}
		return null;
	}

	@Override
	public Map<String, List<HotelPlPreferentialTax>> getTaxMapByPreferentialUuid(String preferentialUuid) {
		List<HotelQuotePreferentialTax> quoteTaxs = super.find("from HotelQuotePreferentialTax hotelQuotePreferentialTax where hotelQuotePreferentialTax.hotelQuotePreferentialUuid=? and hotelQuotePreferentialTax.delFlag=? and hotelQuotePreferentialTax.preferentialType!=0", preferentialUuid, BaseEntity.DEL_FLAG_NORMAL);
		List<HotelPlPreferentialTax> taxs = new ArrayList<HotelPlPreferentialTax>();
		if(CollectionUtils.isNotEmpty(quoteTaxs)){
			for(HotelQuotePreferentialTax quoteTax : quoteTaxs){
				HotelPlPreferentialTax plTax = new HotelPlPreferentialTax();
				BeanUtil.copySimpleProperties(plTax,quoteTax,true);
				taxs.add(plTax);
			}
		}
		Map<String, List<HotelPlPreferentialTax>> preferentialTaxMap = new LinkedHashMap<String, List<HotelPlPreferentialTax>>();
		
		if(CollectionUtils.isNotEmpty(taxs)) {
			for(HotelPlPreferentialTax tax : taxs) {
				if(tax.getChargeType() != null && HotelPlPreferentialTax.CHARGE_TYPE_1 == tax.getChargeType()) {
					tax.setChargeTypeText("%");
				} else if(tax.getChargeType() != null && HotelPlPreferentialTax.CHARGE_TYPE_2 == tax.getChargeType()) {
					Currency currency = currencyDao.getById(tax.getCurrencyId().longValue());
					if(currency != null) {
						tax.setChargeTypeText(currency.getCurrencyMark());
					}
				}
				
				
				//交通方式转换成名字输出
				StringBuffer sb = new StringBuffer();
				if(StringUtils.isNotEmpty(tax.getIslandWayUuids())){
					String[] islandWayUuidArray = tax.getIslandWayUuids().split(";");
					if(islandWayUuidArray != null && islandWayUuidArray.length > 0) {
						for(String uuid : islandWayUuidArray){
							SysCompanyDictView sysDict = sysCompanyDictViewDao.getByUuId(uuid);
							sb.append(sysDict.getLabel());
							sb.append(",");
						}
						if(sb.length()>0){
							sb.deleteCharAt(sb.lastIndexOf(","));
						}
						tax.setIslandWayText(sb.toString());
					}
				}
				//基础餐型转换成名字输出
				if(StringUtils.isNotEmpty(tax.getHotelMealUuids())){
					sb = new StringBuffer();
					String[] hotelMealUuidArray = tax.getHotelMealUuids().split(";");
					for(String uuid : hotelMealUuidArray){
						HotelMeal hotelMeal = hotelMealDao.getByUuid(uuid);
						sb.append(hotelMeal.getMealName());
						sb.append(",");
					}
					if(sb.length()>0){
						sb.deleteCharAt(sb.lastIndexOf(","));
					}
					tax.setHotelMealText(sb.toString());
				}
				
				//税金的页面输出
				if(StringUtils.isNotEmpty(tax.getIstax())){
					sb = new StringBuffer();
					String[] istaxArray = tax.getIstax().split(";");
					for(String istax:istaxArray){//1、政府税；2、服务费；3、床税；4、其他   输出文本
						if(Integer.parseInt(istax)==1){
							sb.append("政府税");
							sb.append(",");
						}else if(Integer.parseInt(istax)==2){
							sb.append("服务费");
							sb.append(",");
						}else if(Integer.parseInt(istax)==3){
							sb.append("床税");
							sb.append(",");
						}
					}
					if(sb.length()>0){
						sb.deleteCharAt(sb.lastIndexOf(","));
					}
					tax.setIstaxText(sb.toString());
				}
				
				//初始化游客类型数据
				if(StringUtils.isNotEmpty(tax.getTravelerTypeUuid())) {
					TravelerType travelerType = travelerTypeDao.getByUuid(tax.getTravelerTypeUuid());
					
					if(travelerTypeMap.get(tax.getTravelerTypeUuid()) == null) {
						if(travelerType != null) {
							travelerTypeMap.put(tax.getTravelerTypeUuid(), travelerType.getName());
						}
					}
					//过滤掉已经删除的游客类型
					if(travelerType == null || TravelerType.STATUS_OFF.equals(travelerType.getStatus())) {
						continue;
					}
					
					tax.setTravelerTypeText(travelerTypeMap.get(tax.getTravelerTypeUuid()));
				}
				
				if(preferentialTaxMap.get(tax.getType().toString()) == null) {
					List<HotelPlPreferentialTax> preferentialTaxs = new ArrayList<HotelPlPreferentialTax>();
					preferentialTaxs.add(tax);
					
					preferentialTaxMap.put(tax.getType().toString(), preferentialTaxs);
				} else {
					preferentialTaxMap.get(tax.getType().toString()).add(tax);
				}
			}
		}
		return preferentialTaxMap;
	}
	
}
