/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.flight.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.flight.dao.FlightControlDao;
import com.trekiz.admin.modules.flight.dao.FlightControlDetailDao;
import com.trekiz.admin.modules.flight.dao.FlightControlHotelDetailDao;
import com.trekiz.admin.modules.flight.entity.FlightControl;
import com.trekiz.admin.modules.flight.entity.FlightControlDetail;
import com.trekiz.admin.modules.flight.entity.FlightControlHotelDetail;
import com.trekiz.admin.modules.flight.input.FlightControlInput;
import com.trekiz.admin.modules.flight.query.FlightControlQuery;
import com.trekiz.admin.modules.flight.service.FlightControlService;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class FlightControlServiceImpl  extends BaseService implements FlightControlService{
	@Autowired
	private FlightControlDao flightControlDao;

	@Autowired
	private FlightControlDetailDao flightControlDetailDao;
	
	@Autowired
	private FlightControlHotelDetailDao flightControlHotelDetailDao;
	
	@Autowired
	private HotelAnnexDao hotelAnnexDao;
	
	@Autowired
	private FlightControlHotelDetailDao controlHotelDetailDao;
	
	public void save (FlightControl flightControl){
		super.setOptInfo(flightControl, BaseService.OPERATION_ADD);
		flightControlDao.saveObj(flightControl);
	}
	
	public void update (FlightControl flightControl){
		super.setOptInfo(flightControl, BaseService.OPERATION_UPDATE);
		flightControlDao.updateObj(flightControl);
	}
	
	public FlightControl getById(java.lang.Integer value) {
		return flightControlDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		FlightControl obj = flightControlDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<FlightControl> find(Page<FlightControl> page, FlightControl flightControl) {
		DetachedCriteria dc = flightControlDao.createDetachedCriteria();
		
	   	if(flightControl.getId()!=null){
	   		dc.add(Restrictions.eq("id", flightControl.getId()));
	   	}
		if (StringUtils.isNotEmpty(flightControl.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+flightControl.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControl.getName())){
			dc.add(Restrictions.like("name", "%"+flightControl.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControl.getCountry())){
			dc.add(Restrictions.like("country", "%"+flightControl.getCountry()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControl.getIslandUuid())){
			dc.add(Restrictions.like("islandUuid", "%"+flightControl.getIslandUuid()+"%"));
		}
	   	if(flightControl.getAirline()!=null){
	   		dc.add(Restrictions.eq("airline", flightControl.getAirline()));
	   	}
	   	if(flightControl.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", flightControl.getCurrencyId()));
	   	}
		if (StringUtils.isNotEmpty(flightControl.getMemo())){
			dc.add(Restrictions.like("memo", "%"+flightControl.getMemo()+"%"));
		}
	   	if(flightControl.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", flightControl.getWholesalerId()));
	   	}
	   	if(flightControl.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", flightControl.getCreateBy()));
	   	}
		if(flightControl.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", flightControl.getCreateDate()));
		}
	   	if(flightControl.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", flightControl.getUpdateBy()));
	   	}
		if(flightControl.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", flightControl.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(flightControl.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+flightControl.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return flightControlDao.find(page, dc);
	}
	
	public List<FlightControl> find( FlightControl flightControl) {
		DetachedCriteria dc = flightControlDao.createDetachedCriteria();
		
	   	if(flightControl.getId()!=null){
	   		dc.add(Restrictions.eq("id", flightControl.getId()));
	   	}
		if (StringUtils.isNotEmpty(flightControl.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+flightControl.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControl.getName())){
			dc.add(Restrictions.like("name", "%"+flightControl.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControl.getCountry())){
			dc.add(Restrictions.like("country", "%"+flightControl.getCountry()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControl.getIslandUuid())){
			dc.add(Restrictions.like("islandUuid", "%"+flightControl.getIslandUuid()+"%"));
		}
	   	if(flightControl.getAirline()!=null){
	   		dc.add(Restrictions.eq("airline", flightControl.getAirline()));
	   	}
	   	if(flightControl.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", flightControl.getCurrencyId()));
	   	}
		if (StringUtils.isNotEmpty(flightControl.getMemo())){
			dc.add(Restrictions.like("memo", "%"+flightControl.getMemo()+"%"));
		}
	   	if(flightControl.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", flightControl.getWholesalerId()));
	   	}
	   	if(flightControl.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", flightControl.getCreateBy()));
	   	}
		if(flightControl.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", flightControl.getCreateDate()));
		}
	   	if(flightControl.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", flightControl.getUpdateBy()));
	   	}
		if(flightControl.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", flightControl.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(flightControl.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+flightControl.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return flightControlDao.find(dc);
	}
	
	public FlightControl getByUuid(String uuid) {
		return flightControlDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		FlightControl flightControl = getByUuid(uuid);
		flightControl.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(flightControl);
	}
	/**
	 * 保存机票库存、机票库存明细、机票库存参考酒店明细 
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	@Override
	public Page<Map<String, Object>> getFlightList(HttpServletRequest request,
			HttpServletResponse response, FlightControlQuery flightControlQuery) {
		
		String orderBy = request.getParameter("orderBy");
		String activityStatus = request.getParameter("activityStatus");// 0：已提交；1：已保存草稿；2：已删除；
		String showType = request.getParameter("showType");// 1是日期列表，2是酒店列表
		StringBuffer  sb = new StringBuffer();
		
		if("2".equals(showType)){
			sb.append(" SELECT fc.uuid AS uuid,fc.country AS country,fc.airline AS airline,fc.island_uuid AS island,fc.name AS name,GROUP_CONCAT(fcd.uuid) AS fcdUuids");
			sb.append(" FROM  flight_control fc LEFT JOIN flight_control_detail fcd ON fc.uuid = fcd.flight_control_uuid AND fcd.delFlag ='0'");
			sb.append(" LEFT JOIN flight_control_hotel_detail fchd ON fc.uuid = fchd.flight_control_uuid AND fchd.delFlag ='0'");
			sb.append(" LEFT JOIN sys_user su ON su.id=fc.createBy  AND su.delFlag = '0' WHERE fc.delFlag='0'");
			sb.append(" AND EXISTS (SELECT 1 FROM flight_control_detail fcd1 WHERE fcd1.flight_control_uuid=fc.uuid and fcd1.delFlag='0')");
			sb.append(" AND fc.wholesaler_id="+UserUtils.getUser().getCompany().getId());
		}
		if(!"3".equals(activityStatus)){
			sb.append(" AND  fcd.status="+activityStatus);
		}
		if (StringUtils.isNotBlank(flightControlQuery.getCountry())) {
			sb.append(" AND  fc.country= '"+flightControlQuery.getCountry()+"'");
		}
		if (StringUtils.isNotBlank(flightControlQuery.getHotelUuid())) {
			sb.append(" AND  fchd.hotel_uuid='"+flightControlQuery.getHotelUuid()+"'");
		}
		if (StringUtils.isNotBlank(flightControlQuery.getIslandUuid())) {
			sb.append(" AND  fc.island_uuid='"+flightControlQuery.getIslandUuid()+"'");
		}
		if (StringUtils.isNotBlank(flightControlQuery.getName())) {
			sb.append(" AND  fc.name like '%"+flightControlQuery.getName()+"%'");
		}
		if (StringUtils.isNotBlank(flightControlQuery.getAirline())) {
			sb.append(" AND  fc.airline ='"+flightControlQuery.getAirline()+"'");
		}
		if (StringUtils.isNotBlank(flightControlQuery.getCreateUser())) {
			sb.append(" AND  su.name like '%"+flightControlQuery.getCreateUser()+"%'");
		}
		if (StringUtils.isNotBlank(flightControlQuery.getUpdateUser())) {
			sb.append(" AND  su.name like '%"+flightControlQuery.getUpdateUser()+"%'");
		}
		if (StringUtils.isNotBlank(flightControlQuery.getEndDepartureDateString())) {
			sb.append(" AND  fcd.departure_date <= '"+flightControlQuery.getEndDepartureDateString()+"'");
		}
		if (StringUtils.isNotBlank(flightControlQuery.getStartDepartureDateString())) {
			sb.append(" AND  fcd.departure_date >= '"+flightControlQuery.getStartDepartureDateString()+"'");
		}
		if (flightControlQuery.getEndPrice()!=null) {
			sb.append(" AND  fcd.price <= "+flightControlQuery.getEndPrice());
		}
		if (flightControlQuery.getStartPrice()!=null) {
			sb.append(" AND  fcd.price  >= "+flightControlQuery.getStartPrice());
		}
		if (flightControlQuery.getEndStock()!=null) {
			sb.append(" AND  fcd.stock <= "+flightControlQuery.getEndStock());
		}
		if (flightControlQuery.getStartStock()!=null) {
			sb.append(" AND  fcd.stock >= "+flightControlQuery.getStartStock());
		}
		if (flightControlQuery.getSpaceGradeType()!=null) {
			sb.append(" AND  fcd.spaceGrade_type  = "+flightControlQuery.getSpaceGradeType());
		}
		if (flightControlQuery.getStatus()!=null) {
			sb.append(" AND  fcd.status  = "+flightControlQuery.getStatus());
		}
		if("2".equals(showType)){
			sb.append("  GROUP BY fc.uuid  ");
		}
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" fc.id DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		if("2".equals(showType)){
			String sql =" select * from ( "+sb.toString()+" ) u";
			return flightControlDao.findBySql(page,sql,sb.toString(),Map.class);
		} else {
		    return flightControlDao.findBySql(page,sb.toString(),Map.class);
		}
	}

	@Override
	public List<List<Map<String, String>>> getFlightListSubs(
			List<Map<String, Object>> loop) {
		
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT fcd.uuid AS uuid,fcd.spaceGrade_type AS spaceGradeType,fcd.price AS price,fcd.price_currency_id AS priceId,fcd.taxes_price AS taxesPrice,fcd.taxes_currency_id AS taxesId,fcd.stock AS stock,");
		sb.append(" GROUP_CONCAT(fchd.hotel_uuid) AS hotelUuid,fcd.status AS status,fcd.createBy AS createBy,fcd.updateBy AS updateBy,fcd.createDate AS createDate,fcd.updateDate AS updateDate,fcd.departure_date AS departureDate");
		sb.append(" FROM flight_control_detail fcd LEFT JOIN flight_control_hotel_detail fchd ON fcd.uuid = fchd.flight_control_detail_uuid AND fchd.delFlag ='0'");
		sb.append(" WHERE fcd.delFlag='0' ");
		
		if (CollectionUtils.isNotEmpty(loop)) {
			for (Map<String, Object> map : loop) {
				StringBuffer fcdUuids = new StringBuffer();
				String uuidsArray =(String)map.get("fcdUuids");
				String[] uuids = uuidsArray.split(",");
				for(String uuid : uuids){
					fcdUuids.append("'"+uuid+"'"+",");
				   }	
				String sqlin =" AND fcd.uuid IN ("+fcdUuids.deleteCharAt(fcdUuids.length()-1)+")";
				String sqlgroup =" GROUP BY  fcd.uuid";
				List<Map<String, String>> listMap = flightControlDao
						.findBySql(sb.toString()+sqlin+sqlgroup, Map.class);
				list.add(listMap);
			}
		}
		
		return list;
	}
	
	public Map<String,String> saveFlightControl(FlightControlInput input) throws Exception{
		Map<String,String> result = new HashMap<String, String>();
		result.put("status", "");
		result.put("message", "");
		
		//机票库存表 封装对应的数据 ，用于批量更新
		List<FlightControlDetail> fliConDetails = new ArrayList<FlightControlDetail>();
		List<FlightControlHotelDetail> FCHotelDetails = new ArrayList<FlightControlHotelDetail>();
		
		if(UserUtils.getCompanyIdForData() != null){
			if(input.validateFormInput()){  //校验通过
				FlightControl fc = input.transfer2FlightControl();
				fc.setWholesalerId(Integer.parseInt(String.valueOf(UserUtils.getCompanyIdForData() )));
				this.setOptInfo(fc, null);
				flightControlDao.saveObj(fc);
				
				if(CollectionUtils.isNotEmpty(fc.getFlightControlDetails())){
					for(FlightControlDetail fcd: fc.getFlightControlDetails()){
						fcd.setFlightControlUuid(fc.getUuid());
						fcd.setWholesalerId(fc.getWholesalerId());
						this.setOptInfo(fcd, null);
						fliConDetails.add(fcd);
						
						if(CollectionUtils.isNotEmpty(fcd.getFlightControlHotelDetails())){
							for(FlightControlHotelDetail fchd:fcd.getFlightControlHotelDetails()){
								fchd.setFlightControlDetailUuid(fcd.getUuid());
								fchd.setFlightControlUuid(fcd.getFlightControlUuid());
								fchd.setWholesalerId(fc.getWholesalerId());
								this.setOptInfo(fchd, null);
								FCHotelDetails.add(fchd);
							}
						}
					}
					//记录机票库存
					try{
						this.recordFlightControlRule(fliConDetails);
					}catch(Exception e){
						result.put("message", "3");
						result.put("error", e.getMessage());
						throw e ;
					}
					
					//批量插入：
					flightControlDetailDao.batchSave(fliConDetails);
					flightControlHotelDetailDao.batchSave(FCHotelDetails);
					
					result.put("status", "success");
					result.put("message", "1");
					return result;
				}else{
					result.put("status", "fail");
					result.put("message", "提交发布的明细已存在，或者有重复的机票库存明细信息！");
					return result;
				}
				
			}else{
				result.put("status", "fail");
				result.put("message", "表单信息验证失败！");
			}
			
		}else{
			result.put("status", "fail");
			result.put("message", "批发商信息读取失败！");
		}
		
		
		
		return result;
	}
	
	/**
	 * 校验库存规则并为机票库存设置规则UUid
		* 
		* @param flightControlDetails	机票库存详情信息
		* @return boolean
		* @author majiancheng
		* @Time 2015-5-21
	 */
	private boolean recordFlightControlRule(List<FlightControlDetail> flightControlDetails) throws Exception {
		
		return true;
	}

	@Override
	public Map<String, String> saveFlightControlDate(FlightControlInput input)throws Exception {
		Map<String,String> result = new HashMap<String, String>();
		result.put("status", "");
		result.put("message", "");
		
		//机票库存表 封装对应的数据 ，更新机票库存，保存机票库存明细和机票库存参考酒店明细记录
		List<FlightControlDetail> fliConDetails = new ArrayList<FlightControlDetail>();
		List<FlightControlHotelDetail> FCHotelDetails = new ArrayList<FlightControlHotelDetail>();
		
		if(UserUtils.getCompanyIdForData() != null){
			if(input.validateFormInput()){  //校验通过
				FlightControl fc = input.transfer2FlightControl();
				//fc.setWholesalerId(Integer.parseInt(String.valueOf(UserUtils.getCompanyIdForData() )));
				this.setOptInfo(fc, "update");
				FlightControl flightControl = flightControlDao.getByUuid(fc.getUuid());
				flightControl.setUpdateDate(fc.getUpdateDate());
				flightControl.setUpdateBy(fc.getUpdateBy());
				flightControl.setCurrencyId(fc.getCurrencyId());
				
				flightControlDao.updateObj(flightControl);
				
				if(CollectionUtils.isNotEmpty(fc.getFlightControlDetails())){
					for(FlightControlDetail fcd: fc.getFlightControlDetails()){
						fcd.setFlightControlUuid(flightControl.getUuid());
						fcd.setWholesalerId(flightControl.getWholesalerId());
						this.setOptInfo(fcd, null);
						fliConDetails.add(fcd);
						
						if(CollectionUtils.isNotEmpty(fcd.getFlightControlHotelDetails())){
							for(FlightControlHotelDetail fchd:fcd.getFlightControlHotelDetails()){
								fchd.setFlightControlDetailUuid(fcd.getUuid());
								fchd.setFlightControlUuid(fcd.getFlightControlUuid());
								fchd.setWholesalerId(flightControl.getWholesalerId());
								this.setOptInfo(fchd, null);
								FCHotelDetails.add(fchd);
							}
						}
					}
					//记录机票库存
					try{
						this.recordFlightControlRule(fliConDetails);
					}catch(Exception e){
						result.put("message", "3");
						result.put("error", e.getMessage());
						throw e ;
					}
					
					//批量插入：
					flightControlDetailDao.batchSave(fliConDetails);
					flightControlHotelDetailDao.batchSave(FCHotelDetails);
					
					result.put("status", "success");
					result.put("message", "1");
					return result;
				}else{
					result.put("status", "fail");
					result.put("message", "提交发布的明细已存在，或者有重复的机票库存明细信息！");
					return result;
				}
				
			}else{
				result.put("status", "fail");
				result.put("message", "表单信息验证失败！");
			}
			
		}else{
			result.put("status", "fail");
			result.put("message", "批发商信息读取失败！");
		}
		
		
		
		return result;
	}

	/**
	 * 修改机票库存 add by sy
	 * @param 
	 * @return
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public  Map<String,String> updateFlightControl(FlightControlInput flightControlInput, boolean updateFlag,List<HotelAnnex> axList){
		Map<String,String> result = new HashMap<String,String>();
		result.put("message", "");
		result.put("error", "");
		
		hotelAnnexDao.synDocInfo(flightControlInput.getUuid(),HotelAnnex.ANNEX_TYPE_FOR_FLIGHT_CONTROL, UserUtils.getCompanyIdForData().intValue(), axList);
		//读取批发商信息
		
		if(UserUtils.getCompanyIdForData() == null) {
			result.put("message", "3");
			result.put("error", "批发商信息读取失败！");
			return result;
		}
		//批量更新
//		List<FlightControlDetail> controlDetails = new ArrayList<FlightControlDetail>();
//		List<FlightControlHotelDetail> allHotelDetails = new  ArrayList<FlightControlHotelDetail>();
		
		//组装表数据
		FlightControl fc = flightControlInput.transfer2FlightControl();
		FlightControl entity =flightControlDao.getByUuid(fc.getUuid());
		
		if(entity != null){
			entity.setName(fc.getName());
			entity.setCountry(fc.getCountry());
			entity.setIslandUuid(fc.getIslandUuid());
			entity.setCurrencyId(fc.getCurrencyId());
			if(updateFlag){
				entity.setMemo(fc.getMemo());
			}
			this.setOptInfo(entity, OPERATION_UPDATE);
			flightControlDao.updateObj(entity);
		}
		
		if(CollectionUtils.isNotEmpty(fc.getFlightControlDetails())){
			for(FlightControlDetail fd : fc.getFlightControlDetails()){
				if(fd ==null){
					continue;
				}
				FlightControlDetail controlDetail = flightControlDetailDao.getByUuid(fd.getUuid());
				controlDetail.setTaxesCurrencyId(fd.getTaxesCurrencyId());
				controlDetail.setTaxesPrice(fd.getTaxesPrice());
				controlDetail.setStock(fd.getStock());
				controlDetail.setSpaceGradeType(fd.getSpaceGradeType());
				controlDetail.setCreateDate(fd.getCreateDate());
				controlDetail.setPriceCurrencyId(fd.getPriceCurrencyId());
				controlDetail.setPrice(fd.getPrice());
				controlDetail.setMemo(fd.getMemo());
				this.setOptInfo(controlDetail, OPERATION_UPDATE);
				
				List<FlightControlHotelDetail> hotelDetails = new ArrayList<FlightControlHotelDetail>();
				
				if(CollectionUtils.isNotEmpty(fd.getFlightControlHotelDetails())){
					for(FlightControlHotelDetail hotel : fd.getFlightControlHotelDetails()){
						FlightControlHotelDetail hotelDetail = controlHotelDetailDao.getByUuid(hotel.getUuid());
						if(hotelDetail == null){
							 continue;
						}
						this.setOptInfo(hotelDetail, OPERATION_UPDATE);
						hotelDetails.add(hotelDetail);
					}
				}
				
			}
		}
		
		return null;
	}
}
