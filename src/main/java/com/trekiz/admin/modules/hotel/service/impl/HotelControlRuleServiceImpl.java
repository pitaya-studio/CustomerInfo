/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.entity.*;
import com.trekiz.admin.modules.hotel.dao.*;
import com.trekiz.admin.modules.hotel.service.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelControlRuleServiceImpl  extends BaseService implements HotelControlRuleService{
	@Autowired
	private HotelControlRuleDao hotelControlRuleDao;

	public void save (HotelControlRule hotelControlRule){
		super.setOptInfo(hotelControlRule, BaseService.OPERATION_ADD);
		hotelControlRuleDao.saveObj(hotelControlRule);
	}
	
	public void update (HotelControlRule hotelControlRule){
		super.setOptInfo(hotelControlRule, BaseService.OPERATION_UPDATE);
		hotelControlRuleDao.updateObj(hotelControlRule);
	}
	
	public HotelControlRule getById(java.lang.Integer value) {
		return hotelControlRuleDao.getById(value);
	}
	
	
	public Page<HotelControlRule> find(Page<HotelControlRule> page, HotelControlRule hotelControlRule) {
		DetachedCriteria dc = hotelControlRuleDao.createDetachedCriteria();
		
	   	if(hotelControlRule.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelControlRule.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlRule.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelControlRule.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRule.getHotelControlRule())){
			dc.add(Restrictions.like("hotelControlRule", "%"+hotelControlRule.getHotelControlRule()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRule.getRuleUuid())){
			dc.add(Restrictions.like("ruleUuid", "%"+hotelControlRule.getRuleUuid()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelControlRuleDao.find(page, dc);
	}
	
	public List<HotelControlRule> find( HotelControlRule hotelControlRule) {
		DetachedCriteria dc = hotelControlRuleDao.createDetachedCriteria();
		
	   	if(hotelControlRule.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelControlRule.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlRule.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelControlRule.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRule.getHotelControlRule())){
			dc.add(Restrictions.like("hotelControlRule", "%"+hotelControlRule.getHotelControlRule()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlRule.getRuleUuid())){
			dc.add(Restrictions.like("ruleUuid", "%"+hotelControlRule.getRuleUuid()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelControlRuleDao.find(dc);
	}
	
	public HotelControlRule getByUuid(String uuid) {
		return hotelControlRuleDao.getByUuid(uuid);
	}
	
}
