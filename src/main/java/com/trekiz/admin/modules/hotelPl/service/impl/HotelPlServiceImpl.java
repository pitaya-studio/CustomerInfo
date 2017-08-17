/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.geography.dao.SysGeographyDao;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.dao.HotelDao;
import com.trekiz.admin.modules.hotel.dao.HotelGuestTypeDao;
import com.trekiz.admin.modules.hotel.dao.HotelGuestTypeRelationDao;
import com.trekiz.admin.modules.hotel.dao.HotelRoomDao;
import com.trekiz.admin.modules.hotel.dao.HotelRoomMealDao;
import com.trekiz.admin.modules.hotel.dao.HotelRoomOccuRateDao;
import com.trekiz.admin.modules.hotel.dao.HotelStarDao;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerTypeRelationDao;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelGuestType;
import com.trekiz.admin.modules.hotel.entity.HotelGuestTypeRelation;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoom;
import com.trekiz.admin.modules.hotel.entity.HotelRoomMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoomOccuRate;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.service.HotelMealService;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlHolidayMealDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlIslandwayDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlIslandwayMemoDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlMealriseDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialMatterDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialMatterValueDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRelDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRelHotelDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRequireDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRequireNotAppDateDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRoomDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialTaxDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPriceDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlRisemealMemoDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlRoomMemoDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlTaxExceptionDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlTaxPriceDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPl;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlHolidayMeal;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandway;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandwayMemo;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlMealrise;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatter;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatterValue;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRel;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRelHotel;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequire;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequireNotAppDate;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRoom;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialTax;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPrice;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlRisemealMemo;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlRoomMemo;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxException;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlTaxPrice;
import com.trekiz.admin.modules.hotelPl.input.HotelPlInput;
import com.trekiz.admin.modules.hotelPl.module.bean.HotelPlPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.module.bean.HotelPlRoom;
import com.trekiz.admin.modules.hotelPl.query.HotelPlQuery;
import com.trekiz.admin.modules.hotelPl.service.HotelPlPreferentialService;
import com.trekiz.admin.modules.hotelPl.service.HotelPlService;
import com.trekiz.admin.modules.island.dao.IslandDao;
import com.trekiz.admin.modules.island.entity.Island;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.preferential.dao.PreferentialTemplatesDao;
import com.trekiz.admin.modules.preferential.entity.PreferentialTemplates;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelPlServiceImpl  extends BaseService implements HotelPlService{
	@Autowired
	private HotelPlDao hotelPlDao;
	@Autowired
	private HotelPlTaxPriceDao hotelPlTaxPriceDao;
	@Autowired
	private HotelPlTaxExceptionDao hotelPlTaxExceptionDao;
	@Autowired
	private HotelPlPriceDao hotelPlPriceDao;
	@Autowired
	private HotelPlIslandwayDao hotelPlIslandwayDao;
	@Autowired
	private HotelPlMealriseDao hotelPlMealriseDao;
	@Autowired
	private HotelPlHolidayMealDao hotelPlHolidayMealDao;
	@Autowired
	private HotelPlRoomMemoDao hotelPlRoomMemoDao;
	@Autowired
	private HotelPlIslandwayMemoDao hotelPlIslandwayMemoDao;
	@Autowired
	private HotelPlRisemealMemoDao hotelPlRisemealMemoDao;
	@Autowired
	private HotelPlPreferentialService hotelPlPreferentialService;
	@Autowired
	private SupplierInfoService supplierInfoService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private HotelRoomDao hotelRoomDao;
	@Autowired
	private HotelRoomMealDao hotelRoomMealDao;
	@Autowired
	private HotelGuestTypeDao hotelGuestTypeDao;
	@Autowired
	private IslandDao islandDao;
	@Autowired
	private HotelMealService hotelMealservice;
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private HotelAnnexDao hotelAnnexDao;
	@Autowired
	private HotelPlPreferentialDao hotelPlPreferentialDao;
	@Autowired
	private HotelPlPreferentialRelHotelDao hotelPlPreferentialRelHotelDao;
	@Autowired
	private HotelPlPreferentialRoomDao hotelPlPreferentialRoomDao;
	@Autowired
	private HotelPlPreferentialRequireDao hotelPlPreferentialRequireDao;
	@Autowired
	private HotelPlPreferentialMatterDao hotelPlPreferentialMatterDao;
	@Autowired
	private HotelPlPreferentialTaxDao hotelPlPreferentialTaxDao;
	@Autowired
	private HotelPlPreferentialRelDao hotelPlPreferentialRelDao;
	@Autowired
	private PreferentialTemplatesDao preferentialTemplatesDao;
	@Autowired
	private HotelPlPreferentialMatterValueDao hotelPlPreferentialMatterValueDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private HotelStarDao hotelStarDao;
	@Autowired
	private SysGeographyDao sysGeographyDao;
	@Autowired
	private HotelGuestTypeRelationDao hotelGuestTypeRelationDao;
	@Autowired
	private HotelTravelerTypeRelationDao hotelTravelerTypeRelationDao;
	@Autowired
	private HotelRoomOccuRateDao hotelRoomOccuRateDao;
	@Autowired
	private HotelPlPreferentialRequireNotAppDateDao hotelPlPreferentialRequireNotAppDateDao;
	@Autowired
	private IslandService islandService;
	
	public void save (HotelPl hotelPl){
		super.setOptInfo(hotelPl, BaseService.OPERATION_ADD);
		hotelPlDao.saveObj(hotelPl);
	}
	
	public void save (HotelPlInput hotelPlInput){
		HotelPl hotelPl = hotelPlInput.getHotelPl();
		super.setOptInfo(hotelPl, BaseService.OPERATION_ADD);
		hotelPlDao.saveObj(hotelPl);
	}
	
	public void update (HotelPl hotelPl){
		super.setOptInfo(hotelPl, BaseService.OPERATION_UPDATE);
		hotelPlDao.updateObj(hotelPl);
	}
	
	public HotelPl getById(java.lang.Integer value) {
		return hotelPlDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPl obj = hotelPlDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPl> find(Page<HotelPl> page, HotelPlQuery hotelPlQuery) {
		DetachedCriteria dc = hotelPlDao.createDetachedCriteria();
		
	   	if(hotelPlQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlQuery.getUuid()));
		}
	   	if(hotelPlQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelPlQuery.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getName())){
			dc.add(Restrictions.like("name", "%" + hotelPlQuery.getName() + "%"));
		}
	   	if(hotelPlQuery.getSupplierInfoId()!=null){
	   		dc.add(Restrictions.eq("supplierInfoId", hotelPlQuery.getSupplierInfoId()));
	   	}
	   	if(hotelPlQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlQuery.getCurrencyId()));
	   	}
	   	if(hotelPlQuery.getPosition()!=null){
	   		dc.add(Restrictions.eq("position", hotelPlQuery.getPosition()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getCountry())){
			dc.add(Restrictions.eq("country", hotelPlQuery.getCountry()));
		}
	   	if(hotelPlQuery.getAreaType()!=null){
	   		dc.add(Restrictions.eq("areaType", hotelPlQuery.getAreaType()));
	   	}
	   	if(hotelPlQuery.getPurchaseType()!=null){
	   		dc.add(Restrictions.eq("purchaseType", hotelPlQuery.getPurchaseType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlQuery.getHotelUuid()));
		}
	   	if(hotelPlQuery.getMixliveCurrencyId()!=null){
	   		dc.add(Restrictions.eq("mixliveCurrencyId", hotelPlQuery.getMixliveCurrencyId()));
	   	}
	   	if(hotelPlQuery.getMixliveAmount()!=null){
	   		dc.add(Restrictions.eq("mixliveAmount", hotelPlQuery.getMixliveAmount()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getGalamealMemo())){
			dc.add(Restrictions.eq("galamealMemo", hotelPlQuery.getGalamealMemo()));
		}
		if (StringUtils.isNotEmpty(hotelPlQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlQuery.getMemo()));
		}
	   	if(hotelPlQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlQuery.getCreateBy()));
	   	}
	   	if(hotelPlQuery.getStartCreateDate()!=null) {
	   		dc.add(Restrictions.ge("createDate", hotelPlQuery.getStartCreateDate()));
	   	}
	   	if(hotelPlQuery.getEndCreateDate()!=null) {
	   		dc.add(Restrictions.le("createDate", hotelPlQuery.getEndCreateDate()));
	   	}
		if(hotelPlQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlQuery.getCreateDate()));
		}
	   	if(hotelPlQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlQuery.getUpdateBy()));
	   	}
		if(hotelPlQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlQuery.getDelFlag()));
		}
		
		//酒店集团查询条件
		if (StringUtils.isNotEmpty(hotelPlQuery.getHotelGroup()) && !("-1".equals(hotelPlQuery.getHotelGroup()))) {
			List<Hotel> hotels = hotelDao.findHotelsByGroupAndCompany(hotelPlQuery.getHotelGroup(), UserUtils.getUser().getCompany().getId().intValue());
			if(CollectionUtils.isNotEmpty(hotels)) {
				List<String> hotelUuids = new ArrayList<String>();
				for(Hotel hotel : hotels) {
					hotelUuids.add(hotel.getUuid());
				}
				dc.add(Restrictions.in("hotelUuid", hotelUuids));
			} else {
				dc.add(Restrictions.in("hotelUuid", new String[]{"-1"}));
			}
		}
		
		if(StringUtils.isEmpty(hotelPlQuery.getOrderBy())) {
			dc.addOrder(Order.desc("createDate"));
			hotelPlQuery.setOrderBy("createDate");
			hotelPlQuery.setAscOrDesc("desc");
		} else {
			if("asc".equals(hotelPlQuery.getAscOrDesc())) {
				dc.addOrder(Order.asc(hotelPlQuery.getOrderBy()));
			} else if("desc".equals(hotelPlQuery.getAscOrDesc())) {
				dc.addOrder(Order.desc(hotelPlQuery.getOrderBy()));
			}
		}
		
		return hotelPlDao.find(page, dc);
	}
	
	public List<HotelPl> find( HotelPlQuery hotelPlQuery) {
		DetachedCriteria dc = hotelPlDao.createDetachedCriteria();
		
	   	if(hotelPlQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlQuery.getUuid()));
		}
	   	if(hotelPlQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelPlQuery.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getName())){
			dc.add(Restrictions.like("name", "%" + hotelPlQuery.getName() + "%"));
		}
	   	if(hotelPlQuery.getSupplierInfoId()!=null){
	   		dc.add(Restrictions.eq("supplierInfoId", hotelPlQuery.getSupplierInfoId()));
	   	}
	   	if(hotelPlQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelPlQuery.getCurrencyId()));
	   	}
	   	if(hotelPlQuery.getPosition()!=null){
	   		dc.add(Restrictions.eq("position", hotelPlQuery.getPosition()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getCountry())){
			dc.add(Restrictions.eq("country", hotelPlQuery.getCountry()));
		}
	   	if(hotelPlQuery.getAreaType()!=null){
	   		dc.add(Restrictions.eq("areaType", hotelPlQuery.getAreaType()));
	   	}
	   	if(hotelPlQuery.getPurchaseType()!=null){
	   		dc.add(Restrictions.eq("purchaseType", hotelPlQuery.getPurchaseType()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlQuery.getHotelUuid()));
		}
	   	if(hotelPlQuery.getMixliveCurrencyId()!=null){
	   		dc.add(Restrictions.eq("mixliveCurrencyId", hotelPlQuery.getMixliveCurrencyId()));
	   	}
	   	if(hotelPlQuery.getMixliveAmount()!=null){
	   		dc.add(Restrictions.eq("mixliveAmount", hotelPlQuery.getMixliveAmount()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlQuery.getGalamealMemo())){
			dc.add(Restrictions.eq("galamealMemo", hotelPlQuery.getGalamealMemo()));
		}
		if (StringUtils.isNotEmpty(hotelPlQuery.getMemo())){
			dc.add(Restrictions.eq("memo", hotelPlQuery.getMemo()));
		}
	   	if(hotelPlQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlQuery.getCreateBy()));
	   	}
		if(hotelPlQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlQuery.getCreateDate()));
		}
	   	if(hotelPlQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlQuery.getUpdateBy()));
	   	}
		if(hotelPlQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlDao.find(dc);
	}
	
	public HotelPl getByUuid(String uuid) {
		return hotelPlDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPl hotelPl = getByUuid(uuid);
		hotelPl.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPl);
	}
	
	/**
	 * 根据uuid数组批量删除价单信息
	     * @discription 
	     * @author majiancheng       
	     * @created 2015-10-23 下午3:14:29      
	     * @param uuids
	     * @return     
	 */
	public boolean batchDelete(String[] uuids) {
		return hotelPlDao.batchDelete(uuids);
	}
	
	public HotelPl getAllHotelPlDataByUuid(String hotelPlUuid) {
		if(StringUtils.isEmpty(hotelPlUuid)) {
			return null;
		}
		HotelPl hotelPl = this.getByUuid(hotelPlUuid);
		if(hotelPl == null) {
			return new HotelPl();
		}
		/** 酒店税金集合 */
		hotelPl.setHotelPlTaxPrices(hotelPlTaxPriceDao.findHotelPlTaxPricesByHotelPlUuid(hotelPlUuid));
		/** 酒店税金例外集合 */
		hotelPl.setHotelPlTaxExceptions(hotelPlTaxExceptionDao.findTaxExceptionsByHotelPlUuids(hotelPlUuid));
		/** 酒店房型价格集合 */
		hotelPl.setHotelPlPrices(hotelPlPriceDao.findPlPricesByHotelPlUuid(hotelPlUuid));
		/** 交通费用集合 */
		hotelPl.setHotelPlIslandways(hotelPlIslandwayDao.findIslandWaysByHotelPlUuid(hotelPlUuid));
		/** 升餐费用集合 */
		hotelPl.setHotelPlMealrises(hotelPlMealriseDao.findPlMealRisesByHotelPlUuid(hotelPlUuid));
		/** 强制性节日餐集合 */
		hotelPl.setMealMap(hotelPlHolidayMealDao.findPlHolidayMealMapByHotelPlUuid(hotelPlUuid));
		/** 优惠信息集合 */
		hotelPl.setHotelPlPreferentials(hotelPlPreferentialService.findPlPreferentialsByHotelPlUuid(hotelPlUuid));
		
		/** 酒店价单房型备注信息 */
		hotelPl.setHotelPlRoomMemos(hotelPlRoomMemoDao.findPlRoomMemosByHotelPlUuid(hotelPlUuid));
		/** 酒店价单上岛方式备注信息 */
		hotelPl.setHotelPlIslandwayMemos(hotelPlIslandwayMemoDao.findPlIslandwayMemosByHotelPlUuid(hotelPlUuid));
		/** 酒店价单升餐备注信息 */
		hotelPl.setHotelPlRisemealMemos(hotelPlRisemealMemoDao.findPlRisemealMemosByHotelPlUuid(hotelPlUuid));
		
		return hotelPl;
	}
	
	public List<HotelPlRoom> getDistinctHotelPlRoomsByUuid(String hotelPlUuid) {
		return hotelPlDao.getDistinctHotelPlRoomsByUuid(hotelPlUuid);
	}
	

	public void initSaveHotelPlPageData(Model model) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		//读取地接社信息集合
        model.addAttribute("supplierInfos", supplierInfoService.findSupplierInfoByCompanyId(companyId));
		//读取公司配置币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));

		//获取当前批发商下所有的住客类型
		List<HotelGuestType> hotelGuestTypes = hotelGuestTypeDao.findByWholesalerId(companyId.intValue());
		model.addAttribute("hotelGuestTypes", JSON.toJSONStringWithDateFormat(hotelGuestTypes, "yyyy-MM-dd"));
		
		//加载批发商下所有的上岛方式
		List<SysCompanyDictView> islandWayList = sysCompanyDictViewService.findByType(Context.BaseInfo.ISLANDS_LANDING_STYLE, companyId);
		model.addAttribute("islandWayList", islandWayList);
		
		//加载酒店优惠模板
		List<PreferentialTemplates> templateses = preferentialTemplatesDao.getAllTemplates();
		model.addAttribute("templateses", templateses);
		model.addAttribute("templatesesJson", JSON.toJSONStringWithDateFormat(templateses, "yyyy-MM-dd"));
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String,String> saveHotelPlTaxInfo(String hotelPlTaxPriceJsonData, String hotelPlTaxExceptionJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			List<HotelPlTaxPrice> hotelPlTaxPrices = JSON.parseArray(hotelPlTaxPriceJsonData, HotelPlTaxPrice.class);
			List<HotelPlTaxException> hotelPlTaxExceptions = JSON.parseArray(hotelPlTaxExceptionJsonData, HotelPlTaxException.class);
			
			if(CollectionUtils.isNotEmpty(hotelPlTaxPrices)) {
				for(HotelPlTaxPrice hotelPlTaxPrice : hotelPlTaxPrices) {
					hotelPlTaxPrice.setWholesalerId(hotelPl.getWholesalerId());
					hotelPlTaxPrice.setSupplierInfoId(hotelPl.getSupplierInfoId());
					hotelPlTaxPrice.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlTaxPrice.setHotelUuid(hotelPl.getHotelUuid());
					
					super.setOptInfo(hotelPlTaxPrice, BaseService.OPERATION_ADD);
				}
			}
			
			if(CollectionUtils.isNotEmpty(hotelPlTaxExceptions)) {
				for(HotelPlTaxException hotelPlTaxException : hotelPlTaxExceptions) {
					hotelPlTaxException.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlTaxException.setHotelUuid(hotelPl.getHotelUuid());
					if(StringUtils.isNotEmpty(hotelPlTaxException.getTaxType())) {
						hotelPlTaxException.setTaxType(hotelPlTaxException.getTaxType().replace(",", ";"));
					}
					if(StringUtils.isNotEmpty(hotelPlTaxException.getTravelType())) {
						hotelPlTaxException.setTravelType(hotelPlTaxException.getTravelType().replace(",", ";"));
					}
					
					super.setOptInfo(hotelPlTaxException, BaseService.OPERATION_ADD);
				}
			}
			
			hotelPlTaxPriceDao.batchSave(hotelPlTaxPrices);
			hotelPlTaxExceptionDao.batchSave(hotelPlTaxExceptions);
			
			datas.put("result", "1");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		
		return datas;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String,String> updateHotelPlTaxInfo(String hotelPlTaxPriceJsonData, String hotelPlTaxExceptionJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			hotelPlTaxPriceDao.deleteHotelPlTaxByHotelPlUuid(hotelPl.getUuid());
			hotelPlTaxExceptionDao.deleteHotelPlTaxByHotelPlUuid(hotelPl.getUuid());
			
			this.saveHotelPlTaxInfo(hotelPlTaxPriceJsonData, hotelPlTaxExceptionJsonData, hotelPl);
			
			datas.put("result", "2");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		return datas;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> saveHotelPlPriceInfo(String hotelPlPriceJsonData, String roomMemoJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			List<HotelPlPrice> hotelPlPrices = JSON.parseArray(hotelPlPriceJsonData, HotelPlPrice.class);
			//房型价格备注json字符串数据
			List<HotelPlRoomMemo> hotelPlRoomMemos = JSON.parseArray(roomMemoJsonData, HotelPlRoomMemo.class);
			
			if(CollectionUtils.isNotEmpty(hotelPlPrices)) {
				for(HotelPlPrice hotelPlPrice : hotelPlPrices) {
					hotelPlPrice.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlPrice.setHotelUuid(hotelPl.getHotelUuid());
					
					String hotelMealUuids = hotelPlPrice.getHotelMealUuids();
					if(StringUtils.isNotEmpty(hotelMealUuids)) {
						hotelPlPrice.setHotelMealUuids(hotelMealUuids.replace(",", ";"));
					}
					
					super.setOptInfo(hotelPlPrice, BaseService.OPERATION_ADD);
				}
			}
			
			if(CollectionUtils.isNotEmpty(hotelPlRoomMemos)) {
				for(HotelPlRoomMemo hotelPlRoomMemo : hotelPlRoomMemos) {
					hotelPlRoomMemo.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlRoomMemo.setHotelUuid(hotelPl.getHotelUuid());
					
					super.setOptInfo(hotelPlRoomMemo, BaseService.OPERATION_ADD);
				}
			}
			
			hotelPlPriceDao.batchSave(hotelPlPrices);
			hotelPlRoomMemoDao.batchSave(hotelPlRoomMemos);
			
			datas.put("result", "1");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		return datas;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> updateHotelPlPriceInfo(String hotelPlPriceJsonData, String roomMemoJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			hotelPlPriceDao.deleteHotelPlPriceByHotelPlUuid(hotelPl.getUuid());
			hotelPlRoomMemoDao.deleteHotelPlRoomMemoByHotelPlUuid(hotelPl.getUuid());
			
			this.saveHotelPlPriceInfo(hotelPlPriceJsonData, roomMemoJsonData, hotelPl);
			
			datas.put("result", "2");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		return datas;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> saveHotelPlIslandWayInfo(String hotelPlIslandWayJsonData, String islandWayMemoJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			List<HotelPlIslandway> hotelPlIslandways = JSON.parseArray(hotelPlIslandWayJsonData, HotelPlIslandway.class);
			//价单上岛方式备注信息
			List<HotelPlIslandwayMemo> hotelPlIslandwayMemos = JSON.parseArray(islandWayMemoJsonData, HotelPlIslandwayMemo.class);
			if(CollectionUtils.isNotEmpty(hotelPlIslandways)) {
				for(HotelPlIslandway hotelPlIslandway : hotelPlIslandways) {
					hotelPlIslandway.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlIslandway.setHotelUuid(hotelPl.getHotelUuid());
					
					super.setOptInfo(hotelPlIslandway, BaseService.OPERATION_ADD);
				}
			}
			
			if(CollectionUtils.isNotEmpty(hotelPlIslandwayMemos)) {
				for(HotelPlIslandwayMemo hotelPlIslandwayMemo : hotelPlIslandwayMemos) {
					hotelPlIslandwayMemo.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlIslandwayMemo.setHotelUuid(hotelPl.getHotelUuid());
					
					super.setOptInfo(hotelPlIslandwayMemo, BaseService.OPERATION_ADD);
				}
			}
			
			hotelPlIslandwayDao.batchSave(hotelPlIslandways);
			hotelPlIslandwayMemoDao.batchSave(hotelPlIslandwayMemos);
			
			datas.put("result", "1");
		} catch(Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		return datas;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> updateHotelPlIslandWayInfo(String hotelPlIslandWayJsonData, String islandWayMemoJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			hotelPlIslandwayDao.deletePlIslandwayByPlUuid(hotelPl.getUuid());
			hotelPlIslandwayMemoDao.deletePlIslandwayMemoByPlUuid(hotelPl.getUuid());
			
			this.saveHotelPlIslandWayInfo(hotelPlIslandWayJsonData, islandWayMemoJsonData, hotelPl);
			datas.put("result", "2");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		return datas;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> saveHotelPlRiseMealInfo(String hotelPlRiseMealJsonData, String riseMealMemoJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			List<HotelPlMealrise> hotelPlMealrises = JSON.parseArray(hotelPlRiseMealJsonData, HotelPlMealrise.class);
			List<HotelPlRisemealMemo> hotelPlRisemealMemos = JSON.parseArray(riseMealMemoJsonData, HotelPlRisemealMemo.class);
			if(CollectionUtils.isNotEmpty(hotelPlMealrises)) {
				for(HotelPlMealrise hotelPlMealrise : hotelPlMealrises) {
					hotelPlMealrise.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlMealrise.setHotelUuid(hotelPl.getHotelUuid());
					
					super.setOptInfo(hotelPlMealrise, BaseService.OPERATION_ADD);
				}
			}
			
			if(CollectionUtils.isNotEmpty(hotelPlRisemealMemos)) {
				for(HotelPlRisemealMemo hotelPlRisemealMemo : hotelPlRisemealMemos) {
					hotelPlRisemealMemo.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlRisemealMemo.setHotelUuid(hotelPl.getHotelUuid());

					super.setOptInfo(hotelPlRisemealMemo, BaseService.OPERATION_ADD);
				}
			}
			hotelPlMealriseDao.batchSave(hotelPlMealrises);
			hotelPlRisemealMemoDao.batchSave(hotelPlRisemealMemos);
			
			datas.put("result", "1");
		} catch(Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		return datas;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> updateHotelPlRiseMealInfo(String hotelPlRiseMealJsonData, String riseMealMemoJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			hotelPlMealriseDao.deletePlMealriseByPlUuid(hotelPl.getUuid());
			hotelPlRisemealMemoDao.deletePlRisemealMemoByPlUuid(hotelPl.getUuid());
			
			this.saveHotelPlRiseMealInfo(hotelPlRiseMealJsonData, riseMealMemoJsonData, hotelPl);
			datas.put("result", "2");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		return datas;
	}
	public Map<String, String> saveHotelPlHolidayMealInfo(String hotelPlHolidayMealJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			List<HotelPlHolidayMeal> hotelPlHolidayMeals = JSON.parseArray(hotelPlHolidayMealJsonData, HotelPlHolidayMeal.class);
			if(CollectionUtils.isNotEmpty(hotelPlHolidayMeals)) {
				for(HotelPlHolidayMeal hotelPlHolidayMeal : hotelPlHolidayMeals) {
					hotelPlHolidayMeal.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlHolidayMeal.setHotelUuid(hotelPl.getHotelUuid());
					
					super.setOptInfo(hotelPlHolidayMeal, BaseService.OPERATION_ADD);
				}
			}
			hotelPlHolidayMealDao.batchSave(hotelPlHolidayMeals);
			
			datas.put("result", "1");
		} catch(Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		return datas;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> updateHotelPlHolidayMealInfo(String hotelPlHolidayMealJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		try{
			hotelPlHolidayMealDao.deletePlHolidayMealByPlUuid(hotelPl.getUuid());
			
			this.saveHotelPlHolidayMealInfo(hotelPlHolidayMealJsonData, hotelPl);
			
			datas.put("result", "2");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
		}
		return datas;
	}
	
	public void buildBaseData(HotelPl hotelPl, Map<String, String> datas) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		//加载酒店下的房型信息
		List<Map<String, Object>> hotelRoomList = new ArrayList<Map<String, Object>>();
		
		List<HotelRoom> hotelRooms = hotelRoomDao.findHotelRoomsByHotelUuid(hotelPl.getHotelUuid());
		if(CollectionUtils.isNotEmpty(hotelRooms)) {
			for(HotelRoom hotelRoom : hotelRooms) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("hotelRoomUuid", hotelRoom.getUuid());
				map.put("hotelRoomText", hotelRoom.getRoomName());
				map.put("occupancyRate", hotelRoom.getOccupancyRate());
				
				Map<String, String> hotelMealMap = new LinkedHashMap<String, String>();
				List<HotelRoomMeal> hotelRoomMeals = hotelRoomMealDao.getByRoomUuid(hotelRoom.getUuid());
				if(CollectionUtils.isNotEmpty(hotelRoomMeals)) {
					for(HotelRoomMeal hotelRoomMeal : hotelRoomMeals) {
						hotelMealMap.put(hotelRoomMeal.getHotelMealUuid(), hotelRoomMeal.getHotelMealName());
					}
				}
				hotelRoom.setHotelRoomMeals(hotelRoomMeals);
				map.put("hotelMealMap", hotelMealMap);
				
				List<HotelGuestTypeRelation> hotelGuestTypeList = hotelGuestTypeRelationDao.getByHotelRoomUuid(hotelRoom.getUuid());
				map.put("hotelGuestTypeList", hotelGuestTypeList);
				
				map.put("roomNum", hotelRoom.getRoomNumb());
				
				String hotelRoomRemark = "";
				
				List<HotelRoomOccuRate> roccurateList = hotelRoomOccuRateDao.getByHotelRoomUuid(hotelRoom.getUuid());
				if(CollectionUtils.isNotEmpty(roccurateList)){
					StringBuffer sb = new StringBuffer();
					for(HotelRoomOccuRate rocc:roccurateList){
						if(StringUtils.isNotBlank(rocc.getRemark())){
							sb.append(rocc.getRemark());
							sb.append(HotelRoomOccuRate.SPLIT_FLAG);
						}
					}
					if(sb.length()>0){
						sb.deleteCharAt(sb.lastIndexOf(HotelRoomOccuRate.SPLIT_FLAG));
					}
					hotelRoomRemark = sb.toString();
				}

				map.put("memo", hotelRoomRemark);
				
				hotelRoomList.add(map);
			}
		}
		
		
		//组装优惠信息基础数据(所需房型)
		Map<String,Object> houseTypes = new LinkedHashMap<String,Object>();
		if(CollectionUtils.isNotEmpty(hotelRooms)) {
			for(HotelRoom hotelRoom : hotelRooms) {
				houseTypes.put(hotelRoom.getUuid(), hotelRoom);
			}
		}
		
		
		//edit by majiancheng (2015-11-18) E391需求上岛方式数据源取自海岛游维护页
		/*List<SysCompanyDictView> islandWays = sysCompanyDictViewService.findByType(Context.BaseInfo.ISLANDS_LANDING_STYLE, companyId);*/
		List<SysCompanyDictView> islandWays = islandService.findIslandWaysByIslandUuid(hotelPl.getIslandUuid());
		
		List<HotelMeal> hotelMeals = hotelMealservice.getMealListByUuid(hotelPl.getHotelUuid());

		//关联酒店集合
		List<Hotel> relevancyHotels = hotelDao.findHotelsByCompanyId(companyId.intValue());
		Iterator<Hotel> relevancyHotelIters = relevancyHotels.iterator();
		while(relevancyHotelIters.hasNext()) {
			Hotel hotel = relevancyHotelIters.next();
			if(hotelPl.getHotelUuid().equals(hotel.getUuid())) {
				relevancyHotelIters.remove();
			} else {
				Island island = islandDao.getByUuid(hotel.getIslandUuid());
				if(island != null && StringUtils.isNotEmpty(island.getIslandName())) {
					hotel.setNameCn(island.getIslandName() + "+" + hotel.getNameCn());
				}
			}
		}
		
		//酒店房型集合
		datas.put("hotelRoomList", JSON.toJSONStringWithDateFormat(hotelRoomList, "yyyy-MM-dd"));
		//酒店价单uuid
		datas.put("hotelPlUuid", hotelPl.getUuid());
		//上岛方式集合
		datas.put("islandWays", JSON.toJSONStringWithDateFormat(islandWays,"yyyy-MM-dd"));
		//酒店餐型集合
		datas.put("hotelMeals", JSON.toJSONStringWithDateFormat(hotelMeals,"yyyy-MM-dd"));
		
		//优惠信息酒店房型集合
		datas.put("houseTypes", JSON.toJSONStringWithDateFormat(houseTypes, "yyyy-MM-dd"));
		
		//关联酒店集合（查询海岛游下面的去除当前选择酒店的集合）
		datas.put("relevancyHotels", JSON.toJSONStringWithDateFormat(relevancyHotels, "yyyy-MM-dd"));
		
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> saveHotelPlPreferentialInfo(String hotelPlHolidayMealJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();
		
		try{
			List<HotelPlPreferential> hotelPlPreferentials = JSON.parseArray(hotelPlHolidayMealJsonData, HotelPlPreferential.class);
			
			//关联酒店集合
			List<HotelPlPreferentialRelHotel> hotelPlPreferentialRelHotels = new ArrayList<HotelPlPreferentialRelHotel>();
			//房型信息集合
			List<HotelPlPreferentialRoom> preferentialRoomList = new ArrayList<HotelPlPreferentialRoom>();
			//要求集合
			List<HotelPlPreferentialRequire> requires = new ArrayList<HotelPlPreferentialRequire>();
			//事项集合
			List<HotelPlPreferentialMatter> matters = new ArrayList<HotelPlPreferentialMatter>();
			//酒店价单优惠信息税金集合
			List<HotelPlPreferentialTax> hotelPlPreferentialTaxs = new ArrayList<HotelPlPreferentialTax>();
			//酒店价单优惠关联集合
			List<HotelPlPreferentialRel> hotelPlPreferentialRels = new ArrayList<HotelPlPreferentialRel>();
			//优惠事项输入信息集合
			List<HotelPlPreferentialMatterValue> matterValues = new ArrayList<HotelPlPreferentialMatterValue>();
			//不适用日期集合
			List<HotelPlPreferentialRequireNotAppDate> notAppDates = new ArrayList<HotelPlPreferentialRequireNotAppDate>();
			
			//存放所有优惠的uuid
			List<String> preferentialUuids = new ArrayList<String>();
			
			//组装酒店价单所有的优惠信息
			if(CollectionUtils.isNotEmpty(hotelPlPreferentials)) {
				for(HotelPlPreferential hotelPlPreferential : hotelPlPreferentials) {
					hotelPlPreferential.setIslandUuid(hotelPl.getIslandUuid());
					hotelPlPreferential.setHotelUuid(hotelPl.getHotelUuid());
					hotelPlPreferential.setHotelPlUuid(hotelPl.getUuid());
					
					//组装交通方式信息
					StringBuffer plPreSb = new StringBuffer();
					if(CollectionUtils.isNotEmpty(hotelPlPreferential.getIslandWayList())) {
						for(SysCompanyDictView islandWay : hotelPlPreferential.getIslandWayList()) {
							plPreSb.append(islandWay.getUuid());
							plPreSb.append(";");
						}
						
						plPreSb.deleteCharAt(plPreSb.length() - 1);
						hotelPlPreferential.setIslandWay(plPreSb.toString());
					}


					//将价单优惠信息id置空，直接使用uuid作为主键关联
					hotelPlPreferential.setId(null);
					//保存或更新酒店优惠信息
					if(StringUtils.isEmpty(hotelPlPreferential.getUuid())) {
						super.setOptInfo(hotelPlPreferential, BaseService.OPERATION_ADD);
						hotelPlPreferentialDao.saveObj(hotelPlPreferential);
					} else {
						
						HotelPlPreferential entity = hotelPlPreferentialDao.getByUuid(hotelPlPreferential.getUuid());
						BeanUtil.copySimpleProperties(entity, hotelPlPreferential, true);
						
						super.setOptInfo(entity, BaseService.OPERATION_UPDATE);
						
						try {
							hotelPlPreferentialDao.updateObj(entity);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					preferentialUuids.add(hotelPlPreferential.getUuid());
					
					//组装关联酒店信息
					if(hotelPlPreferential.getHotelPlPreferentialRelHotel() != null) {
						HotelPlPreferentialRelHotel relHotel = hotelPlPreferential.getHotelPlPreferentialRelHotel();
						relHotel.setHotelPlUuid(hotelPlPreferential.getHotelPlUuid());
						relHotel.setHotelPlPreferentialUuid(hotelPlPreferential.getUuid());
						super.setOptInfo(relHotel, BaseService.OPERATION_ADD);
						
						StringBuffer sb = new StringBuffer();
						//组装交通方式信息
						if(CollectionUtils.isNotEmpty(relHotel.getIslandWayList())) {
							for(SysCompanyDictView islandWay : relHotel.getIslandWayList()) {
								sb.append(islandWay.getUuid());
								sb.append(";");
							}
							
							sb.deleteCharAt(sb.length() - 1);
							relHotel.setIslandWay(sb.toString());
						}
						
						
						hotelPlPreferentialRelHotels.add(relHotel);
					}
					
					//组装联酒店房型信息
					if(CollectionUtils.isNotEmpty(hotelPlPreferential.getPreferentialRoomList())) {
						for(HotelPlPreferentialRoom preferentialRoom : hotelPlPreferential.getPreferentialRoomList()) {
							preferentialRoom.setHotelPlUuid(hotelPlPreferential.getHotelPlUuid());
							preferentialRoom.setHotelPlPreferentialUuid(hotelPlPreferential.getUuid());
							super.setOptInfo(preferentialRoom, BaseService.OPERATION_ADD);
							
							//设置酒店房型的酒店餐型uuids
							if(CollectionUtils.isNotEmpty(preferentialRoom.getHotelMealList())) {
								StringBuffer hotelMealSb = new StringBuffer();
								for(HotelMeal hotelMeal : preferentialRoom.getHotelMealList()) {
									hotelMealSb.append(hotelMeal.getUuid());
									hotelMealSb.append(";");
								}
								hotelMealSb.deleteCharAt(hotelMealSb.length() - 1);
								preferentialRoom.setHotelMealUuids(hotelMealSb.toString());
							}
						}
						preferentialRoomList.addAll(hotelPlPreferential.getPreferentialRoomList());
					}
					
					//组装优惠信息要求信息
					if(hotelPlPreferential.getRequire() != null) {
						HotelPlPreferentialRequire require = hotelPlPreferential.getRequire();
						require.setHotelPlUuid(hotelPlPreferential.getHotelPlUuid());
						require.setIslandUuid(hotelPl.getIslandUuid());
						require.setHotelUuid(hotelPl.getHotelUuid());
						require.setHotelPlPreferentialUuid(hotelPlPreferential.getUuid());
						super.setOptInfo(require, BaseService.OPERATION_ADD);
						
						requires.add(require);
						
						//组装不适用日期集合（不适用日期格式：2015-11-12~2015-11-13;2015-11-14~2015-11-15;）
						String notApplicableDate = require.getNotApplicableDate();
						String[] notAppDateArr = notApplicableDate.split(";");//日期段
						if(ArrayUtils.isNotEmpty(notAppDateArr)) {
							for(String notAppDate : notAppDateArr) {
								if(StringUtils.isBlank(notAppDate.trim()))continue;//add by 20151113 zhanghao
								String[] dateArr = notAppDate.split("~");
								Date startDate = DateUtils.string2Date(dateArr[0]);//开始日期
								Date endDate = null;
								//edit by majiancheng 2015-11-19 处理历史数据，结束日期为开始日期
								if(dateArr.length == 1) {
									endDate = startDate;//结束日期
								} else {
									endDate = DateUtils.string2Date(dateArr[1]);//结束日期
								}
								
								//组装不适用日期对象信息
								HotelPlPreferentialRequireNotAppDate notAppDateBean = new HotelPlPreferentialRequireNotAppDate();
								notAppDateBean.setHotelPlUuid(hotelPl.getUuid());
								notAppDateBean.setHotelPlPreferentialUuid(hotelPlPreferential.getUuid());
								notAppDateBean.setHotelPlPreferentialRequireUuid(require.getUuid());
								notAppDateBean.setStartDate(startDate);
								notAppDateBean.setEndDate(endDate);
								notAppDateBean.setDayNum(DateUtils.daysBetween(startDate, endDate));
								super.setOptInfo(notAppDateBean, BaseService.OPERATION_ADD);
								
								notAppDates.add(notAppDateBean);
							}
						}
						
					}
					
					//组装酒店价单优惠关联数据
					if(CollectionUtils.isNotEmpty(hotelPlPreferential.getHotelPlPreferentialRels())) {
						for(HotelPlPreferentialRel rel : hotelPlPreferential.getHotelPlPreferentialRels()) {
							rel.setHotelPlUuid(hotelPl.getUuid());
							rel.setIslandUuid(hotelPl.getIslandUuid());
							rel.setHotelUuid(hotelPl.getHotelUuid());
							rel.setHotelPlPreferentialUuid(hotelPlPreferential.getUuid());
							super.setOptInfo(rel, BaseService.OPERATION_ADD);
						}
						
						hotelPlPreferentialRels.addAll(hotelPlPreferential.getHotelPlPreferentialRels());
					}
					
					//组装优惠事项信息
					if(hotelPlPreferential.getMatter() != null) {
						HotelPlPreferentialMatter matter = hotelPlPreferential.getMatter();
						matter.setHotelPlUuid(hotelPlPreferential.getHotelPlUuid());
						matter.setHotelPlPreferentialUuid(hotelPlPreferential.getUuid());
						matter.setIslandUuid(hotelPlPreferential.getIslandUuid());
						matter.setHotelUuid(hotelPlPreferential.getHotelUuid());
						
						super.setOptInfo(matter, BaseService.OPERATION_ADD);
						
						//组装优惠模板税金集合
						if(matter.getPreferentialTaxMap() != null) {
							Map<String,List<HotelPlPreferentialTax>> preferentialTaxMap = matter.getPreferentialTaxMap();
							Set<String> taxKeySet = preferentialTaxMap.keySet();
							for(String taxKey : taxKeySet) {
								List<HotelPlPreferentialTax> taxs = preferentialTaxMap.get(taxKey);
								if(CollectionUtils.isNotEmpty(taxs)) {
									for(HotelPlPreferentialTax tax : taxs) {
										tax.setHotelPlUuid(hotelPlPreferential.getHotelPlUuid());
										tax.setIslandUuid(hotelPlPreferential.getIslandUuid());
										tax.setHotelUuid(hotelPlPreferential.getHotelUuid());
										tax.setHotelPlPreferentialUuid(hotelPlPreferential.getUuid());
										
										super.setOptInfo(tax, BaseService.OPERATION_ADD);
									}
								}
								
								hotelPlPreferentialTaxs.addAll(taxs);
							}
						}
						matters.add(matter);
						
						if(CollectionUtils.isNotEmpty(matter.getMatterValues())) {
							for(HotelPlPreferentialMatterValue matterValue : matter.getMatterValues()) {
								matterValue.setHotelPlUuid(hotelPlPreferential.getHotelPlUuid());
								matterValue.setIslandUuid(hotelPlPreferential.getIslandUuid());
								matterValue.setHotelUuid(hotelPlPreferential.getHotelUuid());
								matterValue.setHotelPlPreferentialUuid(hotelPlPreferential.getUuid());
								matterValue.setHotelPlPreferentialMatterUuid(matter.getUuid());
								super.setOptInfo(matterValue, BaseService.OPERATION_ADD);
							}
							
							matterValues.addAll(matter.getMatterValues());
						}
					}
				}
			}
			
			//批量保存
			hotelPlPreferentialRelHotelDao.batchSave(hotelPlPreferentialRelHotels);
			hotelPlPreferentialRoomDao.batchSave(preferentialRoomList);
			hotelPlPreferentialRequireDao.batchSave(requires);
			hotelPlPreferentialMatterDao.batchSave(matters);
			hotelPlPreferentialTaxDao.batchSave(hotelPlPreferentialTaxs);
			hotelPlPreferentialRelDao.batchSave(hotelPlPreferentialRels);
			hotelPlPreferentialMatterValueDao.batchSave(matterValues);
			hotelPlPreferentialRequireNotAppDateDao.batchSave(notAppDates);
			
			datas.put("result", "1");
			
			//获取可关联的优惠信息集合
			List<HotelPlPreferential> relevancyFavors = hotelPlPreferentialDao.getRelPlPreferentialsByPlUuid(hotelPl.getUuid());
			datas.put("relevancyFavors", JSON.toJSONStringWithDateFormat(relevancyFavors, "yyyy-MM-dd"));
			
			datas.put("preferentialUuids", JSON.toJSONStringWithDateFormat(preferentialUuids, "yyyy-MM-dd"));
		} catch(Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
			throw e;
		}
		return datas;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> updateHotelPlPreferentialInfo(String hotelPlHolidayMealJsonData, HotelPl hotelPl) throws Exception {
		Map<String, String> datas = new HashMap<String, String>();

		try{
			List<String> existsPlPreferentialUuids = new ArrayList<String>();
			//删除已经存在的优惠模板子表数据
			List<HotelPlPreferential> hotelPlPreferentials = JSON.parseArray(hotelPlHolidayMealJsonData, HotelPlPreferential.class);
			if(CollectionUtils.isNotEmpty(hotelPlPreferentials)) {
				for(HotelPlPreferential hotelPlPreferential : hotelPlPreferentials) {
					if(StringUtils.isNotEmpty(hotelPlPreferential.getUuid())) {
						existsPlPreferentialUuids.add(hotelPlPreferential.getUuid());
					}
				}
			}
			
			hotelPlPreferentialDao.deleteNotContainPreferentialUuids(existsPlPreferentialUuids, hotelPl.getUuid());
			hotelPlPreferentialRelHotelDao.deleteByPlPreferentialUuids(existsPlPreferentialUuids);
			hotelPlPreferentialRoomDao.deleteByPlPreferentialUuids(existsPlPreferentialUuids);
			hotelPlPreferentialRequireDao.deleteByPlPreferentialUuids(existsPlPreferentialUuids);
			hotelPlPreferentialMatterDao.deleteByPlPreferentialUuids(existsPlPreferentialUuids);
			hotelPlPreferentialTaxDao.deleteByPlPreferentialUuids(existsPlPreferentialUuids);
			hotelPlPreferentialRelDao.deleteByPlPreferentialUuids(existsPlPreferentialUuids);
			hotelPlPreferentialMatterValueDao.deleteByPlPreferentialUuids(existsPlPreferentialUuids);
			hotelPlPreferentialRequireNotAppDateDao.deleteByPlPreferentialUuids(existsPlPreferentialUuids);
			
			datas = this.saveHotelPlPreferentialInfo(hotelPlHolidayMealJsonData, hotelPl);
			
			datas.put("result", "2");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统发生异常，请重新操作！");
			throw e;
		}
		return datas;
	}

	@Override
	public void saveHOtelAnnex(List<HotelAnnex> buildAnnexList,
			HotelPl hotelPl) {
		for(HotelAnnex hotelAnnex:buildAnnexList){
			super.setOptInfo(hotelAnnex, BaseService.OPERATION_ADD);
			hotelAnnex.setType(15);
			hotelAnnex.setMainUuid(hotelPl.getUuid());
			hotelAnnex.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
			hotelAnnexDao.saveObj(hotelAnnex);
		}
		
	}
	
	public Map<String,Object> getHotelRoomsInfoByHotelUuid(String hotelUuid) {
		//组装优惠信息基础数据(所需房型)
		Map<String,Object> houseTypes = new LinkedHashMap<String,Object>();
		
		List<HotelRoom> hotelRooms = hotelRoomDao.findHotelRoomsByHotelUuid(hotelUuid);
		if(CollectionUtils.isNotEmpty(hotelRooms)) {
			for(HotelRoom hotelRoom : hotelRooms) {
				//查询房型下的所有关联餐型
				List<HotelRoomMeal> hotelRoomMeals = hotelRoomMealDao.getByRoomUuid(hotelRoom.getUuid());
				hotelRoom.setHotelRoomMeals(hotelRoomMeals);

				houseTypes.put(hotelRoom.getUuid(), hotelRoom);
			}
		}
		
		return houseTypes;
	}
	
	public Map<String,String> updateHotelPlMemo(String hotelPlUuid, String hotelPlMemo) {
		Map<String,String> datas = new LinkedHashMap<String,String>();
		HotelPl hotelPl = this.getByUuid(hotelPlUuid);
		hotelPl.setMemo(hotelPlMemo);
		this.update(hotelPl);
		datas.put("result", "1");
		datas.put("message", "酒店备注保存成功！");
		return datas;
	}
	
	public void initHotelPlData(HotelPl hotelPl, Model model) {
		//组装价单基础信息
		if(hotelPl != null) {
			if(hotelPl.getCurrencyId() != null) {
				Currency currency = currencyDao.getById(hotelPl.getCurrencyId().longValue());
				hotelPl.setCurrencyMark(currency.getCurrencyMark());
				hotelPl.setCurrencyText(currency.getCurrencyName());
			}
			
			if(StringUtils.isNotEmpty(hotelPl.getHotelUuid())) {
				Hotel hotel = hotelDao.getByUuid(hotelPl.getHotelUuid());
				hotelPl.setHotelText(hotel.getNameCn());
				hotelPl.setHotelStar(hotel.getStar());
				hotelPl.setHotelAddress(hotel.getAddress());
				hotelPl.setContactPhone(hotel.getTelephone());
				
				HotelStar hotelStar = hotelStarDao.getByUuid(hotel.getStar());
				if(hotelStar != null) {
					hotelPl.setHotelStar(hotelStar.getLabel());
				}
				
				//获取当前酒店关联的所有游客类型
				List<TravelerType> travelerTypes = hotelTravelerTypeRelationDao.getTravelerTypesByHotelUuid(hotelPl.getHotelUuid());
				model.addAttribute("travelerTypes", JSON.toJSONStringWithDateFormat(travelerTypes, "yyyy-MM-dd"));
				model.addAttribute("travelerTypeList", travelerTypes);
				
			}
			
			if(StringUtils.isNotEmpty(hotelPl.getIslandUuid())) {
				Island island = islandDao.getByUuid(hotelPl.getIslandUuid());
				if(island != null) {
					hotelPl.setIslandText(island.getIslandName());
				}
			}
			
			if(StringUtils.isNotEmpty(hotelPl.getCountry())) {
				SysGeography sysGeography = sysGeographyDao.getByUuid(hotelPl.getCountry());
				if(sysGeography != null) {
					hotelPl.setCountryText(sysGeography.getNameCn());
				}
			}
			//组装酒店税金信息
			List<HotelPlTaxPrice> hotelPlTaxPriceList = hotelPlTaxPriceDao.findHotelPlTaxPricesByHotelPlUuid(hotelPl.getUuid());
			model.addAttribute("hotelPlTaxPriceList", JSON.toJSONStringWithDateFormat(hotelPlTaxPriceList, "yyyy-MM-dd"));
			
			//组装酒店税金例外信息
			List<HotelPlTaxException> hotelPlTaxExceptionList = hotelPlTaxExceptionDao.findTaxExceptionsByHotelPlUuids(hotelPl.getUuid());
			model.addAttribute("hotelPlTaxExceptionList", JSON.toJSONStringWithDateFormat(hotelPlTaxExceptionList, "yyyy-MM-dd"));
			
			//组装酒店房型价格信息
			Map<String, List<HotelPlPriceJsonBean>> roomPrices = hotelPlPriceDao.findHotelPlPricesByHotelPlUuid(hotelPl.getUuid());
			model.addAttribute("roomPrices", JSON.toJSONStringWithDateFormat(roomPrices, "yyyy-MM-dd"));

			//房型价格备注信息集合
			List<HotelPlRoomMemo> hotelPlRoomMemoList = hotelPlRoomMemoDao.findPlRoomMemosByHotelPlUuid(hotelPl.getUuid());
			model.addAttribute("hotelPlRoomMemoList", JSON.toJSONStringWithDateFormat(hotelPlRoomMemoList, "yyyy-MM-dd"));
			
			//组装交通费用信息
			Map<String, List<HotelPlIslandway>> islandWayPriceList = hotelPlIslandwayDao.findHotelPlIslandwaysByHotelPlUuid(hotelPl.getUuid());
			model.addAttribute("islandWayPriceList", JSON.toJSONStringWithDateFormat(islandWayPriceList, "yyyy-MM-dd"));

			//交通备注信息集合
			List<HotelPlIslandwayMemo> islandWayMemoList = hotelPlIslandwayMemoDao.findPlIslandwayMemosByHotelPlUuid(hotelPl.getUuid());
			model.addAttribute("islandWayMemoList", JSON.toJSONStringWithDateFormat(islandWayMemoList, "yyyy-MM-dd"));
			
			//组装升餐费用信息
			Map<String, List<HotelPlMealrise>> hotelRiseMealMap = hotelPlMealriseDao.findHotelPlMealriseByHotelPlUuid(hotelPl.getUuid());
			model.addAttribute("hotelRiseMealMap", JSON.toJSONStringWithDateFormat(hotelRiseMealMap, "yyyy-MM-dd"));
			
			//升餐费用备注
			List<HotelPlRisemealMemo> hotelPlRiseMealMemoList = hotelPlRisemealMemoDao.findPlRisemealMemosByHotelPlUuid(hotelPl.getUuid());
			model.addAttribute("hotelPlRiseMealMemoList", JSON.toJSONStringWithDateFormat(hotelPlRiseMealMemoList, "yyyy-MM-dd"));

			//强制性节日餐
			List<HotelPlHolidayMeal> hotelPlHolidayMealList = hotelPlHolidayMealDao.findPlHolidayMealsByHotelPlUuid(hotelPl.getUuid());
			model.addAttribute("hotelPlHolidayMealList", JSON.toJSONStringWithDateFormat(hotelPlHolidayMealList, "yyyy-MM-dd"));

			/** 优惠信息集合 */
			List<HotelPlPreferential> favorInfos = hotelPlPreferentialService.findPlPreferentialsByHotelPlUuid(hotelPl.getUuid());
			model.addAttribute("favorInfos", JSON.toJSONStringWithDateFormat(favorInfos, "yyyy-MM-dd"));
			
			//获取可关联的优惠信息集合
			List<HotelPlPreferential> relevancyFavors = hotelPlPreferentialDao.getRelPlPreferentialsByPlUuid(hotelPl.getUuid());
			model.addAttribute("relevancyFavors", JSON.toJSONStringWithDateFormat(relevancyFavors, "yyyy-MM-dd"));
			
		}
		model.addAttribute("baseInfo", JSON.toJSONStringWithDateFormat(hotelPl, "yyyy-MM-dd"));

		
	}
	
	public boolean findIsExist(String hotelUuid, int purchaseType, int supplierInfoId) {
		return hotelPlDao.findIsExist(hotelUuid, purchaseType, supplierInfoId);
	}
}
