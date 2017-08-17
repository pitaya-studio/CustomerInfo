/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.dao.TravelerTypeDao;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

import com.trekiz.admin.modules.hotel.dao.HotelTravelerTypeRelationDao;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatterValue;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRel;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRoom;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialTax;
import com.trekiz.admin.modules.hotelPl.module.bean.GuestPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.PreferentialJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.QuotedPriceDetailJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.QuotedPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceQuery;
import com.trekiz.admin.modules.hotelPl.module.query.QuotedPriceRoomQuery;
import com.trekiz.admin.modules.hotelPl.service.HotelPlPreferentialService;
import com.trekiz.admin.modules.hotelQuote.entity.*;
import com.trekiz.admin.modules.hotelQuote.dao.*;
import com.trekiz.admin.modules.hotelQuote.service.*;
import com.trekiz.admin.modules.hotelQuote.input.*;
import com.trekiz.admin.modules.hotelQuote.query.*;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialMatterDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialMatterValueDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRelDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRelHotelDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRequireDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRoomDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialTaxDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferential;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatter;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatterValue;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRel;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRelHotel;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRequire;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRoom;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialTax;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuoteServiceImpl  extends BaseService implements HotelQuoteService{
	@Autowired
	private HotelQuoteDao hotelQuoteDao;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private HotelPlPreferentialService hotelPlPreferentialService;
	@Autowired
	private HotelQuotePreferentialDao hotelQuotePreferentialDao;
	@Autowired
	private HotelQuotePreferentialRoomDao hotelQuotePreferentialRoomDao;
	@Autowired
	private HotelQuotePreferentialMatterDao hotelQuotePreferentialMatterDao;
	@Autowired
	private HotelQuotePreferentialMatterValueDao hotelQuotePreferentialMatterValueDao;
	@Autowired
	private HotelQuotePreferentialRequireDao hotelQuotePreferentialRequireDao;
	@Autowired
	private HotelQuotePreferentialRelHotelDao hotelQuotePreferentialRelHotelDao;
	@Autowired
	private HotelQuotePreferentialRelDao hotelQuotePreferentialRelDao;
	@Autowired
	private HotelQuoteConditionPreferentialRelDao hotelQuoteConditionPreferentialRelDao;
	@Autowired
	private TravelerTypeDao travelerTypeDao;
	@Autowired
	private HotelQuoteResultDao hotelQuoteResultDao;
	@Autowired
	private HotelQuoteResultDetailDao hotelQuoteResultDetailDao;
	@Autowired
	private HotelQuoteConditionDao hotelQuoteConditionDao;
	@Autowired
	private HotelQuoteConditionDetailDao hotelQuoteConditionDetailDao;
	@Autowired
	private HotelQuoteConditionDetailPersonNumDao hotelQuoteConditionDetailPersonNumDao;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private HotelTravelerTypeRelationDao hotelTravelerTypeRelationDao;
	@Autowired
	private HotelQuotePreferentialTaxDao hotelQuotePreferentialTaxDao;
	public void save (HotelQuote hotelQuote){
		super.setOptInfo(hotelQuote, BaseService.OPERATION_ADD);
		hotelQuoteDao.saveObj(hotelQuote);
	}
	
	public void save (HotelQuoteInput hotelQuoteInput){
		HotelQuote hotelQuote = hotelQuoteInput.getHotelQuote();
		super.setOptInfo(hotelQuote, BaseService.OPERATION_ADD);
		hotelQuoteDao.saveObj(hotelQuote);
	}
	
	public void update (HotelQuote hotelQuote){
		super.setOptInfo(hotelQuote, BaseService.OPERATION_UPDATE);
		hotelQuoteDao.updateObj(hotelQuote);
	}
	
	public HotelQuote getById(java.lang.Integer value) {
		return hotelQuoteDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuote obj = hotelQuoteDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuote> find(Page<HotelQuote> page, HotelQuoteQuery hotelQuoteQuery) {
		DetachedCriteria dc = hotelQuoteDao.createDetachedCriteria();
		
	   	if(hotelQuoteQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getWholesalerId())){
			dc.add(Restrictions.eq("wholesalerId", hotelQuoteQuery.getWholesalerId()));
		}
	   	if(hotelQuoteQuery.getUserId()!=null){
	   		dc.add(Restrictions.eq("userId", hotelQuoteQuery.getUserId()));
	   	}
	   	if(hotelQuoteQuery.getQuoteType()!=null){
	   		dc.add(Restrictions.eq("quoteType", hotelQuoteQuery.getQuoteType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getQuoteObject())){
			dc.add(Restrictions.eq("quoteObject", hotelQuoteQuery.getQuoteObject()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getLinkName())){
			dc.add(Restrictions.eq("linkName", hotelQuoteQuery.getLinkName()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getLinkPhone())){
			dc.add(Restrictions.eq("linkPhone", hotelQuoteQuery.getLinkPhone()));
		}
	   	if(hotelQuoteQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelQuoteQuery.getCurrencyId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelQuoteQuery.getMemo()));
		}
	   	if(hotelQuoteQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteQuery.getCreateBy()));
	   	}
		if(hotelQuoteQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteQuery.getCreateDate()));
		}
	   	if(hotelQuoteQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteQuery.getUpdateBy()));
	   	}
		if(hotelQuoteQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteDao.find(page, dc);
	}
	
	public List<HotelQuote> find( HotelQuoteQuery hotelQuoteQuery) {
		DetachedCriteria dc = hotelQuoteDao.createDetachedCriteria();
		
	   	if(hotelQuoteQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getWholesalerId())){
			dc.add(Restrictions.eq("wholesalerId", hotelQuoteQuery.getWholesalerId()));
		}
	   	if(hotelQuoteQuery.getUserId()!=null){
	   		dc.add(Restrictions.eq("userId", hotelQuoteQuery.getUserId()));
	   	}
	   	if(hotelQuoteQuery.getQuoteType()!=null){
	   		dc.add(Restrictions.eq("quoteType", hotelQuoteQuery.getQuoteType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getQuoteObject())){
			dc.add(Restrictions.eq("quoteObject", hotelQuoteQuery.getQuoteObject()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getLinkName())){
			dc.add(Restrictions.eq("linkName", hotelQuoteQuery.getLinkName()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getLinkPhone())){
			dc.add(Restrictions.eq("linkPhone", hotelQuoteQuery.getLinkPhone()));
		}
	   	if(hotelQuoteQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelQuoteQuery.getCurrencyId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelQuoteQuery.getMemo()));
		}
	   	if(hotelQuoteQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteQuery.getCreateBy()));
	   	}
		if(hotelQuoteQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteQuery.getCreateDate()));
		}
	   	if(hotelQuoteQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteQuery.getUpdateBy()));
	   	}
		if(hotelQuoteQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteDao.find(dc);
	}
	
	public HotelQuote getByUuid(String uuid) {
		return hotelQuoteDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuote hotelQuote = getByUuid(uuid);
		hotelQuote.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuote);
	}

	@Override
	public Page<Map<String, Object>> hotelQuoteList(Page<Map<String, Object>> page,HotelQuoteQuery hotelQuoteQuery) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT hq.wholesaler_id AS wholesalerId,hq.user_id AS userId,hq.quote_object AS quoteObject,hq.link_name AS linkName,");
		sql.append(" hqc.island_uuid AS islandUuid,hq.quote_type AS quoteType,");
		sql.append(" hqc.hotel_uuid AS hotelUuid,hqc.createDate AS createDate,hqc.uuid AS uuid,hqc.hotel_quote_uuid AS hotelQuoteUuid,hqc.roomNum AS roomNum");
		sql.append(" FROM hotel_quote_condition hqc LEFT JOIN hotel_quote hq ON hqc.hotel_quote_uuid = hq.uuid AND hq.delFlag = 0");
		sql.append(" WHERE hqc.delFlag = 0  and hq.wholesaler_id ="+ UserUtils.getUser().getCompany().getId()); 
		//createDate为datetime类型
		if (StringUtils.isNotBlank(hotelQuoteQuery.getFromDate())) {
			sql.append(" and  hqc.createDate >='"+ hotelQuoteQuery.getFromDate() + " 00:00:00'");
		}
		if (StringUtils.isNotBlank(hotelQuoteQuery.getEndDate())) {
			sql.append(" and  hqc.createDate <='"+ hotelQuoteQuery.getEndDate() + " 23:59:59'");
		}
		//分公司(也即是部门)查询条件转换为查询部门下的所有人员
		if (StringUtils.isNotBlank(hotelQuoteQuery.getWholesalerId())) {
			sql.append(" and  hq.user_id IN ( SELECT sur.userId  FROM  sys_role sr LEFT JOIN sys_user_role sur ON sr.id = sur.roleId  WHERE sr.delFlag = 0 AND sr.companyId = "+ 
		                 UserUtils.getUser().getCompany().getId()+" AND sr.deptId= "+ hotelQuoteQuery.getWholesalerId() + ")");
		}
		if (hotelQuoteQuery.getUserId()!=null) {
			sql.append(" and  hq.user_id = "+ hotelQuoteQuery.getUserId());
		}
		//询价客户
		if (StringUtils.isNotBlank(hotelQuoteQuery.getQuoteObject())) {
			String idOrName = hotelQuoteQuery.getQuoteObject();
			if(idOrName.matches("[0-9]{1,}")){
			  sql.append(" and  hq.quote_object ='"+ idOrName + "'");
			}else{
			  sql.append(" and  hq.quote_object LIKE'%"+ idOrName + "%'");
			}
		}
		if (StringUtils.isNotBlank(hotelQuoteQuery.getIslandUuid())) {
			sql.append(" and  hqc.island_uuid ='"+ hotelQuoteQuery.getIslandUuid() + "'");
		}
		//酒店名称
		if (StringUtils.isNotBlank(hotelQuoteQuery.getHotel())) {
			sql.append(" and hqc.hotel_uuid = '"+ hotelQuoteQuery.getHotel() + "'");
		}
		//酒店集团
		if (StringUtils.isNotBlank(hotelQuoteQuery.getHotelGroupUuid())) {
			sql.append(" and hqc.hotel_uuid IN ( SELECT  uuid FROM hotel h WHERE h.hotel_group = '"+ hotelQuoteQuery.getHotelGroupUuid() + "')");
		}
		//排序
		if (StringUtils.isNotBlank(hotelQuoteQuery.getOrderBy())) {
			page.setOrderBy(hotelQuoteQuery.getOrderBy());
		}
		
		return hotelQuoteDao.findBySql(page,sql.toString(),Map.class);
	}

	@Override
	public List<List<Map<String, String>>> getQuoteRoomList(List<Map<String, Object>> list) {
		List<List<Map<String, String>>> resultList = new ArrayList<List<Map<String, String>>>();
		String sb = "SELECT hqcd.hotel_room_uuid AS hotelRoomUuid  FROM  hotel_quote_condition_detail hqcd ";

		if (CollectionUtils.isNotEmpty(list)) {
			for (Map<String, Object> map : list) {
				//循环取房型的同时，添加酒店集团信息
				map.put("hotelGroup",hotelService.getHotelGroupByUuid((String)map.get("hotelUuid")));
				String sql = "  WHERE hqcd.hotel_quote_condition_uuid ='"
						+ map.get("uuid") + "'";
				List<Map<String, String>> listMap = hotelQuoteDao.findBySql(
						sb + sql, Map.class);
				resultList.add(listMap);
			}
		}
		return resultList;
	}
	

	
	public boolean saveQuotedPriceInfo(HotelQuoteInput hotelQuoteInput) {
		boolean flag = true;

		List<QuotedPriceJsonBean> quotedPriceJsonBeans = hotelQuoteInput.getQuotedPriceJsonStr();
		List<PreferentialJsonBean> preferentialJsonBeans = hotelQuoteInput.getPreferentialPriceJson();
		
		User user = UserUtils.getUser();
		//组装酒店报价信息
		HotelQuote hotelQuote = new HotelQuote();
		BeanUtil.copySimpleProperties(hotelQuote, hotelQuoteInput);
		hotelQuote.setUserId(user.getId().intValue());
		hotelQuote.setWholesalerId(user.getCompany().getId().intValue());
		
		super.setOptInfo(hotelQuote, BaseService.OPERATION_ADD);
		
		List<HotelQuoteCondition> hotelQuoteConditions = new ArrayList<HotelQuoteCondition>();
		List<HotelQuoteConditionDetail> hotelQuoteConditionDetails = new ArrayList<HotelQuoteConditionDetail>();
		List<HotelQuoteConditionDetailPersonNum> hotelQuoteConditionDetailPersonNums = new ArrayList<HotelQuoteConditionDetailPersonNum>();
		List<HotelQuoteConditionPreferentialRel> hotelQuoteConditionPreferentialRels = new ArrayList<HotelQuoteConditionPreferentialRel>();
		List<HotelQuoteResult> hotelQuoteResults = new ArrayList<HotelQuoteResult>();
		List<HotelQuoteResultDetail> hotelQuoteResultDetails = new ArrayList<HotelQuoteResultDetail>();
		/*酒店报价优惠信息集合*/
		List<HotelQuotePreferential> hotelQuotePreferentials = new ArrayList<HotelQuotePreferential>();
		/*报价优惠房型信息集合*/
		List<HotelQuotePreferentialRoom> hotelQuotePreferentialRooms = new ArrayList<HotelQuotePreferentialRoom>();
		/*报价优惠事项集合*/
		List<HotelQuotePreferentialMatter> hotelQuotePreferentialMatters = new ArrayList<HotelQuotePreferentialMatter>();
		/*事项的值集合*/
		List<HotelQuotePreferentialMatterValue> hotelQuotePreferentialMatterValues = new ArrayList<HotelQuotePreferentialMatterValue>();
		/*报价优惠要求*/
		List<HotelQuotePreferentialRequire> hotelQuotePreferentialRequires = new ArrayList<HotelQuotePreferentialRequire>();
		/*报价优惠关联酒店*/
		List<HotelQuotePreferentialRelHotel> hotelQuotePreferentialRelHotels = new ArrayList<HotelQuotePreferentialRelHotel>();
		/*酒店报价优惠关联集合*/
		List<HotelQuotePreferentialRel> hotelQuotePreferentialRels = new ArrayList<HotelQuotePreferentialRel>();
		/*酒店报价优惠税金集合*/
		List<HotelQuotePreferentialTax> hotelQuotePreferentialTaxs = new ArrayList<HotelQuotePreferentialTax>();
		
		int k=0;
		Currency currency = currencyService.getRMBCurrencyId();

		
		if(CollectionUtils.isNotEmpty(quotedPriceJsonBeans)) {
			for(QuotedPriceJsonBean quotedPriceJsonBean : quotedPriceJsonBeans) {
				
				//报价器查询条件
				HotelQuoteCondition hotelQuoteCondition = new HotelQuoteCondition();
				
				if(currency != null) {
					hotelQuote.setCurrencyId(Integer.parseInt(currency.getId().toString()));
				}
				
				//组装报价条件信息
				QuotedPriceQuery quotedPriceQuery = quotedPriceJsonBean.getQuotedPriceQuery();
				
				BeanUtil.copySimpleProperties(hotelQuoteCondition, quotedPriceQuery);
				hotelQuoteCondition.setPosition(quotedPriceQuery.getPosition());
				hotelQuoteCondition.setHotelQuoteUuid(hotelQuote.getUuid());
				hotelQuoteCondition.setHotelPlUuid(quotedPriceJsonBean.getHotelPlUuid());
				super.setOptInfo(hotelQuoteCondition, BaseService.OPERATION_ADD);
				
				hotelQuoteConditions.add(hotelQuoteCondition);

				//组装报价条件明细信息
				if(CollectionUtils.isNotEmpty(quotedPriceQuery.getQuotedPriceRoomList())) {
					for(QuotedPriceRoomQuery quotedPriceRoomQuery : quotedPriceQuery.getQuotedPriceRoomList()) {
						HotelQuoteConditionDetail hotelQuoteConditionDetail = new HotelQuoteConditionDetail();
						hotelQuoteConditionDetail.setHotelQuoteUuid(hotelQuote.getUuid());
						hotelQuoteConditionDetail.setHotelQuoteConditionUuid(hotelQuoteCondition.getUuid());
						BeanUtil.copySimpleProperties(hotelQuoteConditionDetail, quotedPriceRoomQuery);
						
						super.setOptInfo(hotelQuoteConditionDetail, BaseService.OPERATION_ADD);
						
						hotelQuoteConditionDetails.add(hotelQuoteConditionDetail);
					}
				}
				
				//组装报价条件游客人数信息
				List<TravelerType> travelerTypes = hotelTravelerTypeRelationDao.getTravelerTypesByHotelUuid(hotelQuoteCondition.getHotelUuid());
				Iterator<TravelerType> travelerTypeIter = travelerTypes.iterator();
				if(quotedPriceQuery.getPersonNum() != null && quotedPriceQuery.getPersonNum().length > 0) {
					for(String item : quotedPriceQuery.getPersonNum()) {
						HotelQuoteConditionDetailPersonNum hotelQuoteConditionDetailPersonNum = new HotelQuoteConditionDetailPersonNum();
						hotelQuoteConditionDetailPersonNum.setHotelQuoteUuid(hotelQuote.getUuid());
						TravelerType travelerType = travelerTypeIter.next();
						
						hotelQuoteConditionDetailPersonNum.setHotelQuoteConditionUuid(hotelQuoteCondition.getUuid());
						hotelQuoteConditionDetailPersonNum.setTravelerType(travelerType.getUuid());
						if(StringUtils.isNotEmpty(item)) {
							hotelQuoteConditionDetailPersonNum.setPersonNum(Integer.parseInt(item));
						}
						super.setOptInfo(hotelQuoteConditionDetailPersonNum, BaseService.OPERATION_ADD);
						
						hotelQuoteConditionDetailPersonNums.add(hotelQuoteConditionDetailPersonNum);
					}
				}
				PreferentialJsonBean preferentialJsonBean=null;
				//使用优惠的情况则读取优惠的信息
				if(CollectionUtils.isNotEmpty(preferentialJsonBeans)&&preferentialJsonBeans.size()>k){
					preferentialJsonBean = preferentialJsonBeans.get(k++);
				}else{//不使用优惠则不保存优惠后的价格
					preferentialJsonBean = new PreferentialJsonBean();
				}
				//组装酒店优惠前结果
				//组装混住费价格
				HotelQuoteResult mixQuoteResult = new HotelQuoteResult();
				mixQuoteResult.setHotelQuoteUuid(hotelQuote.getUuid());
				mixQuoteResult.setHotelQuoteConditionUuid(hotelQuoteCondition.getUuid());
				mixQuoteResult.setPriceType(HotelQuoteResult.PRICE_TYPE_MIX);
				mixQuoteResult.setPreferentialPrice(preferentialJsonBean.getMixlivePrice());
				mixQuoteResult.setPrice(quotedPriceJsonBean.getMixlivePrice());
				mixQuoteResult.setHotelPlUuid(quotedPriceJsonBean.getHotelPlUuid());
				super.setOptInfo(mixQuoteResult, BaseService.OPERATION_ADD);
				hotelQuoteResults.add(mixQuoteResult);
                //打包费用
				if(preferentialJsonBean.getTotalPrice()!=null){
				   HotelQuoteResult totalPriceResult = new HotelQuoteResult();
				   totalPriceResult.setHotelQuoteUuid(hotelQuote.getUuid());
				   totalPriceResult.setHotelQuoteConditionUuid(hotelQuoteCondition.getUuid());
				   totalPriceResult.setPriceType(HotelQuoteResult.PRICE_TYPE_TOTAL);
				   totalPriceResult.setPreferentialPrice(preferentialJsonBean.getTotalPrice());
				   totalPriceResult.setHotelPlUuid(quotedPriceJsonBean.getHotelPlUuid());
				   super.setOptInfo(totalPriceResult, BaseService.OPERATION_ADD);
				   hotelQuoteResults.add(totalPriceResult);
				}
				
				//组装游客费用价格
				List<GuestPriceJsonBean> beforeGuestPriceJsonBeans = quotedPriceJsonBean.getGuestPriceList();
				List<GuestPriceJsonBean> afterGuestPriceJsonBeans = preferentialJsonBean.getGuestPriceList();
				for(int j=0; j<beforeGuestPriceJsonBeans.size(); j++) {
					GuestPriceJsonBean beforeGuestPriceJsonBean = beforeGuestPriceJsonBeans.get(j);
					
					
					HotelQuoteResult travelerQuoteResult = new HotelQuoteResult();
					travelerQuoteResult.setHotelQuoteUuid(hotelQuote.getUuid());
					travelerQuoteResult.setHotelQuoteConditionUuid(hotelQuoteCondition.getUuid());
					if(beforeGuestPriceJsonBean.getIsThirdPerson()==0){
						travelerQuoteResult.setPriceType(HotelQuoteResult.PRICE_TYPE_TRAVELER);
						travelerQuoteResult.setTypeUuid(beforeGuestPriceJsonBean.getTravelerType());
					}else{
						travelerQuoteResult.setPriceType(HotelQuoteResult.PRICE_TYPE_THIRD_PEOPLE);
						travelerQuoteResult.setTypeUuid(beforeGuestPriceJsonBean.getGuestType());
					}
					travelerQuoteResult.setPrice(beforeGuestPriceJsonBean.getAmount());
					
					if(CollectionUtils.isNotEmpty(afterGuestPriceJsonBeans)){//使用优惠后的优惠价格保存
						GuestPriceJsonBean afterGuestPriceJsonBean = afterGuestPriceJsonBeans.get(j);
						travelerQuoteResult.setPreferentialPrice(afterGuestPriceJsonBean.getAmount());
					}
					travelerQuoteResult.setHotelPlUuid(quotedPriceJsonBean.getHotelPlUuid());
					super.setOptInfo(travelerQuoteResult, BaseService.OPERATION_ADD);
					hotelQuoteResults.add(travelerQuoteResult);
					
				}
				
				//组装酒店报价结果明细信息
				List<QuotedPriceDetailJsonBean> quotedPriceDetailJsonBeans = quotedPriceJsonBean.getDetailList();
				if(CollectionUtils.isNotEmpty(quotedPriceDetailJsonBeans)) {
					for(QuotedPriceDetailJsonBean quotedPriceDetailJsonBean : quotedPriceDetailJsonBeans) {
						if(CollectionUtils.isNotEmpty(quotedPriceDetailJsonBean.getGuestPriceList())) {
							for(GuestPriceJsonBean  guestPriceJsonBean: quotedPriceDetailJsonBean.getGuestPriceList()) {
								HotelQuoteResultDetail travelerTypeResultDetail = new HotelQuoteResultDetail();
								travelerTypeResultDetail.setHotelQuoteUuid(hotelQuote.getUuid());
								travelerTypeResultDetail.setHotelQuoteConditionUuid(hotelQuoteCondition.getUuid());
								BeanUtil.copySimpleProperties(travelerTypeResultDetail, guestPriceJsonBean);
								if(guestPriceJsonBean.getIsThirdPerson()==0){
									travelerTypeResultDetail.setPriceType(HotelQuoteResultDetail.PRICE_TYPE_TRAVELER);
									travelerTypeResultDetail.setTypeUuid(guestPriceJsonBean.getTravelerType());
								}else{
									travelerTypeResultDetail.setPriceType(HotelQuoteResultDetail.PRICE_TYPE_THIRD);
									travelerTypeResultDetail.setTypeUuid(guestPriceJsonBean.getGuestType());
								}
								travelerTypeResultDetail.setInDate(guestPriceJsonBean.getinDateForDate());
								travelerTypeResultDetail.setPrice(guestPriceJsonBean.getAmount());
								travelerTypeResultDetail.setHotelRoomUuid(quotedPriceDetailJsonBean.getHotelRoomUuid());
								travelerTypeResultDetail.setHotelMealUuid(quotedPriceDetailJsonBean.getHotelMealUuid());
								super.setOptInfo(travelerTypeResultDetail, BaseService.OPERATION_ADD);
								hotelQuoteResultDetails.add(travelerTypeResultDetail);
							}
						}
					}
				}
			}
		}
		
		//组装报价优惠信息
		if(CollectionUtils.isNotEmpty(preferentialJsonBeans)) {
			for(int i=0; i < preferentialJsonBeans.size(); i++) {
				PreferentialJsonBean preferentialJsonBean = preferentialJsonBeans.get(i);
				//保存酒店报价优惠信息集合
				List<HotelPlPreferential> preferentialList = preferentialJsonBean.getPreferentialList();
				if(CollectionUtils.isNotEmpty(preferentialList)) {
					for(HotelPlPreferential hotelPlPreferential : preferentialList) {
						HotelQuotePreferential hotelQuotePreferential = new HotelQuotePreferential();
						BeanUtil.copySimpleProperties(hotelQuotePreferential, hotelPlPreferential);
						hotelQuotePreferential.setUuid("");
						super.setOptInfo(hotelQuotePreferential, BaseService.OPERATION_ADD);
						
						hotelQuotePreferentials.add(hotelQuotePreferential);
						
						//报价优惠房型信息集合
						if(CollectionUtils.isNotEmpty(hotelPlPreferential.getPreferentialRoomList())) {
							for(HotelPlPreferentialRoom hotelPlPreferentialRoom : hotelPlPreferential.getPreferentialRoomList()) {
								HotelQuotePreferentialRoom hotelQuotePreferentialRoom = new HotelQuotePreferentialRoom();
								BeanUtil.copySimpleProperties(hotelQuotePreferentialRoom, hotelPlPreferentialRoom);
								hotelQuotePreferentialRoom.setHotelQuotePreferentialUuid(hotelQuotePreferential.getUuid());
								
								super.setOptInfo(hotelQuotePreferentialRoom, BaseService.OPERATION_ADD);
								
								hotelQuotePreferentialRooms.add(hotelQuotePreferentialRoom);
							}
						}
						//报价优惠关联集合
						if(CollectionUtils.isNotEmpty(hotelPlPreferential.getHotelPlPreferentialRels())) {
							for(HotelPlPreferentialRel hotelPlPreferentialRel : hotelPlPreferential.getHotelPlPreferentialRels()) {
								HotelQuotePreferentialRel hotelQuotePreferentialRel = new HotelQuotePreferentialRel();
								BeanUtil.copySimpleProperties(hotelQuotePreferentialRel, hotelPlPreferentialRel);
								hotelQuotePreferentialRel.setHotelQuotePreferentialUuid(hotelQuotePreferential.getUuid());
								
								super.setOptInfo(hotelQuotePreferentialRel, BaseService.OPERATION_ADD);
								hotelQuotePreferentialRels.add(hotelQuotePreferentialRel);
							}
						}
						
						//事项
						if(hotelPlPreferential.getMatter() != null) {
							HotelQuotePreferentialMatter hotelQuotePreferentialMatter = new HotelQuotePreferentialMatter();
							BeanUtil.copySimpleProperties(hotelQuotePreferentialMatter, hotelPlPreferential.getMatter());
							super.setOptInfo(hotelQuotePreferentialMatter, BaseService.OPERATION_ADD);
							hotelQuotePreferentialMatter.setHotelQuotePreferentialUuid(hotelQuotePreferential.getUuid());
							
							hotelQuotePreferentialMatters.add(hotelQuotePreferentialMatter);
							//税金
							if(hotelPlPreferential.getMatter().getPreferentialTaxMap()!=null) {
								Map<String,List<HotelPlPreferentialTax>> preferentialTaxMap = hotelPlPreferential.getMatter().getPreferentialTaxMap();
								Set<String> taxKeySet = preferentialTaxMap.keySet();
								for(String taxKey : taxKeySet) {
									List<HotelPlPreferentialTax> taxs = preferentialTaxMap.get(taxKey);
									if(CollectionUtils.isNotEmpty(taxs)) {
										for(HotelPlPreferentialTax tax : taxs) {
											HotelQuotePreferentialTax quoteTax = new HotelQuotePreferentialTax();
											BeanUtil.copySimpleProperties(quoteTax,tax);
											quoteTax.setHotelPlUuid(hotelPlPreferential.getHotelPlUuid());
											quoteTax.setIslandUuid(hotelPlPreferential.getIslandUuid());
											quoteTax.setHotelUuid(hotelPlPreferential.getHotelUuid());
											quoteTax.setHotelQuotePreferentialUuid(hotelQuotePreferential.getUuid());
											super.setOptInfo(quoteTax, BaseService.OPERATION_ADD);
											hotelQuotePreferentialTaxs.add(quoteTax);
										}
									}
								}
							}
							//事项的值
							if(CollectionUtils.isNotEmpty(hotelPlPreferential.getMatter().getMatterValues())) {
								for(HotelPlPreferentialMatterValue hotelPlPreferentialMatterValue : hotelPlPreferential.getMatter().getMatterValues()) {
									HotelQuotePreferentialMatterValue hotelQuotePreferentialMatterValue = new HotelQuotePreferentialMatterValue();
									BeanUtil.copySimpleProperties(hotelQuotePreferentialMatterValue, hotelPlPreferentialMatterValue);
									super.setOptInfo(hotelQuotePreferentialMatterValue, BaseService.OPERATION_ADD);
									hotelQuotePreferentialMatterValue.setHotelQuotePreferentialUuid(hotelQuotePreferential.getUuid());
									hotelQuotePreferentialMatterValue.setHotelQuotePreferentialMatterUuid(hotelPlPreferential.getMatter().getUuid());
									hotelQuotePreferentialMatterValue.setMyKeyvar(hotelPlPreferentialMatterValue.getMyKey());
									
									hotelQuotePreferentialMatterValues.add(hotelQuotePreferentialMatterValue);
								}
							}
						}
						
						//优惠要求
						if(hotelPlPreferential.getRequire() != null) {
							HotelQuotePreferentialRequire hotelQuotePreferentialRequire = new HotelQuotePreferentialRequire();
							BeanUtil.copySimpleProperties(hotelQuotePreferentialRequire, hotelPlPreferential.getRequire());
							super.setOptInfo(hotelQuotePreferentialRequire, BaseService.OPERATION_ADD);
							hotelQuotePreferentialRequire.setHotelQuotePreferentialUuid(hotelQuotePreferential.getUuid());
							   
							hotelQuotePreferentialRequires.add(hotelQuotePreferentialRequire);
						}
						
						//关联酒店
						if(hotelPlPreferential.getHotelPlPreferentialRelHotel() != null) {
							HotelQuotePreferentialRelHotel hotelQuotePreferentialRelHotel = new HotelQuotePreferentialRelHotel();
							BeanUtil.copySimpleProperties(hotelQuotePreferentialRelHotel, hotelPlPreferential.getHotelPlPreferentialRelHotel());
							super.setOptInfo(hotelQuotePreferentialRelHotel, BaseService.OPERATION_ADD);
							hotelQuotePreferentialRelHotel.setHotelQuotePreferentialUuid(hotelQuotePreferential.getUuid());
							
							hotelQuotePreferentialRelHotels.add(hotelQuotePreferentialRelHotel);
						}
						
						//优惠关联信息
						HotelQuoteConditionPreferentialRel preferentialRel = new HotelQuoteConditionPreferentialRel();
						preferentialRel.setHotelQuoteUuid(hotelQuote.getUuid());
						preferentialRel.setHotelQuoteConditionUuid(hotelQuoteConditions.get(i).getUuid());
						preferentialRel.setHotelPlPreferentialUuid(hotelPlPreferential.getUuid());
						preferentialRel.setHotelQuotePreferentialUuid(hotelQuotePreferential.getUuid());
						super.setOptInfo(preferentialRel, BaseService.OPERATION_ADD);
						hotelQuoteConditionPreferentialRels.add(preferentialRel);
					}
				}
			}
		}
		
		//批量保存
		hotelQuoteDao.saveObj(hotelQuote);
		hotelQuoteConditionDao.batchSave(hotelQuoteConditions);
		hotelQuoteConditionDetailDao.batchSave(hotelQuoteConditionDetails);
		hotelQuoteConditionDetailPersonNumDao.batchSave(hotelQuoteConditionDetailPersonNums);
		hotelQuoteResultDao.batchSave(hotelQuoteResults);
		hotelQuoteResultDetailDao.batchSave(hotelQuoteResultDetails);
		hotelQuotePreferentialDao.batchSave(hotelQuotePreferentials);
		hotelQuotePreferentialRoomDao.batchSave(hotelQuotePreferentialRooms);
		hotelQuotePreferentialMatterDao.batchSave(hotelQuotePreferentialMatters);
		hotelQuotePreferentialMatterValueDao.batchSave(hotelQuotePreferentialMatterValues);
		hotelQuotePreferentialRequireDao.batchSave(hotelQuotePreferentialRequires);
		hotelQuotePreferentialRelHotelDao.batchSave(hotelQuotePreferentialRelHotels);
		hotelQuotePreferentialRelDao.batchSave(hotelQuotePreferentialRels);
		hotelQuoteConditionPreferentialRelDao.batchSave(hotelQuoteConditionPreferentialRels);
		hotelQuotePreferentialTaxDao.batchSave(hotelQuotePreferentialTaxs);
		return flag;
	}
}
