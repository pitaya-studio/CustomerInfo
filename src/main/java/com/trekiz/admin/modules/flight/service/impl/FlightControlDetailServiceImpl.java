/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.flight.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.flight.dao.FlightControlDetailDao;
import com.trekiz.admin.modules.flight.entity.FlightControlDetail;
import com.trekiz.admin.modules.flight.query.FlightControlQuery;
import com.trekiz.admin.modules.flight.service.FlightControlDetailService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class FlightControlDetailServiceImpl  extends BaseService implements FlightControlDetailService{
	@Autowired
	private FlightControlDetailDao flightControlDetailDao;

	public void save (FlightControlDetail flightControlDetail){
		super.setOptInfo(flightControlDetail, BaseService.OPERATION_ADD);
		flightControlDetailDao.saveObj(flightControlDetail);
	}
	
	public void update (FlightControlDetail flightControlDetail){
		super.setOptInfo(flightControlDetail, BaseService.OPERATION_UPDATE);
		flightControlDetailDao.updateObj(flightControlDetail);
	}
	
	public FlightControlDetail getById(java.lang.Integer value) {
		return flightControlDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		FlightControlDetail obj = flightControlDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<FlightControlDetail> find(Page<FlightControlDetail> page, FlightControlDetail flightControlDetail) {
		DetachedCriteria dc = flightControlDetailDao.createDetachedCriteria();
		
	   	if(flightControlDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", flightControlDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(flightControlDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+flightControlDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControlDetail.getFlightControlUuid())){
			dc.add(Restrictions.like("flightControlUuid", "%"+flightControlDetail.getFlightControlUuid()+"%"));
		}
		if(flightControlDetail.getDepartureDate()!=null){
			dc.add(Restrictions.eq("departureDate", flightControlDetail.getDepartureDate()));
		}
	   	if(flightControlDetail.getSpaceGradeType()!=null){
	   		dc.add(Restrictions.eq("spaceGradeType", flightControlDetail.getSpaceGradeType()));
	   	}
	   	if(flightControlDetail.getPriceCurrencyId()!=null){
	   		dc.add(Restrictions.eq("priceCurrencyId", flightControlDetail.getPriceCurrencyId()));
	   	}
	   	if(flightControlDetail.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", flightControlDetail.getPrice()));
	   	}
	   	if(flightControlDetail.getTaxesCurrencyId()!=null){
	   		dc.add(Restrictions.eq("taxesCurrencyId", flightControlDetail.getTaxesCurrencyId()));
	   	}
	   	if(flightControlDetail.getTaxesPrice()!=null){
	   		dc.add(Restrictions.eq("taxesPrice", flightControlDetail.getTaxesPrice()));
	   	}
	   	if(flightControlDetail.getStock()!=null){
	   		dc.add(Restrictions.eq("stock", flightControlDetail.getStock()));
	   	}
	   	if(flightControlDetail.getSellStock()!=null){
	   		dc.add(Restrictions.eq("sellStock", flightControlDetail.getSellStock()));
	   	}
	   	if(flightControlDetail.getPreStock()!=null){
	   		dc.add(Restrictions.eq("preStock", flightControlDetail.getPreStock()));
	   	}
		if (StringUtils.isNotEmpty(flightControlDetail.getMemo())){
			dc.add(Restrictions.like("memo", "%"+flightControlDetail.getMemo()+"%"));
		}
	   	if(flightControlDetail.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", flightControlDetail.getStatus()));
	   	}
		if (StringUtils.isNotEmpty(flightControlDetail.getValidateFlag())){
			dc.add(Restrictions.like("validateFlag", "%"+flightControlDetail.getValidateFlag()+"%"));
		}
	   	if(flightControlDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", flightControlDetail.getWholesalerId()));
	   	}
	   	if(flightControlDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", flightControlDetail.getCreateBy()));
	   	}
		if(flightControlDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", flightControlDetail.getCreateDate()));
		}
	   	if(flightControlDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", flightControlDetail.getUpdateBy()));
	   	}
		if(flightControlDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", flightControlDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(flightControlDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+flightControlDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return flightControlDetailDao.find(page, dc);
	}
	
	public List<FlightControlDetail> find( FlightControlDetail flightControlDetail) {
		DetachedCriteria dc = flightControlDetailDao.createDetachedCriteria();
		
	   	if(flightControlDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", flightControlDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(flightControlDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+flightControlDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(flightControlDetail.getFlightControlUuid())){
			dc.add(Restrictions.like("flightControlUuid", "%"+flightControlDetail.getFlightControlUuid()+"%"));
		}
		if(flightControlDetail.getDepartureDate()!=null){
			dc.add(Restrictions.eq("departureDate", flightControlDetail.getDepartureDate()));
		}
	   	if(flightControlDetail.getSpaceGradeType()!=null){
	   		dc.add(Restrictions.eq("spaceGradeType", flightControlDetail.getSpaceGradeType()));
	   	}
	   	if(flightControlDetail.getPriceCurrencyId()!=null){
	   		dc.add(Restrictions.eq("priceCurrencyId", flightControlDetail.getPriceCurrencyId()));
	   	}
	   	if(flightControlDetail.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", flightControlDetail.getPrice()));
	   	}
	   	if(flightControlDetail.getTaxesCurrencyId()!=null){
	   		dc.add(Restrictions.eq("taxesCurrencyId", flightControlDetail.getTaxesCurrencyId()));
	   	}
	   	if(flightControlDetail.getTaxesPrice()!=null){
	   		dc.add(Restrictions.eq("taxesPrice", flightControlDetail.getTaxesPrice()));
	   	}
	   	if(flightControlDetail.getStock()!=null){
	   		dc.add(Restrictions.eq("stock", flightControlDetail.getStock()));
	   	}
	   	if(flightControlDetail.getSellStock()!=null){
	   		dc.add(Restrictions.eq("sellStock", flightControlDetail.getSellStock()));
	   	}
	   	if(flightControlDetail.getPreStock()!=null){
	   		dc.add(Restrictions.eq("preStock", flightControlDetail.getPreStock()));
	   	}
		if (StringUtils.isNotEmpty(flightControlDetail.getMemo())){
			dc.add(Restrictions.like("memo", "%"+flightControlDetail.getMemo()+"%"));
		}
	   	if(flightControlDetail.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", flightControlDetail.getStatus()));
	   	}
		if (StringUtils.isNotEmpty(flightControlDetail.getValidateFlag())){
			dc.add(Restrictions.like("validateFlag", "%"+flightControlDetail.getValidateFlag()+"%"));
		}
	   	if(flightControlDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", flightControlDetail.getWholesalerId()));
	   	}
	   	if(flightControlDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", flightControlDetail.getCreateBy()));
	   	}
		if(flightControlDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", flightControlDetail.getCreateDate()));
		}
	   	if(flightControlDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", flightControlDetail.getUpdateBy()));
	   	}
		if(flightControlDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", flightControlDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(flightControlDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+flightControlDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return flightControlDetailDao.find(dc);
	}
	
	public FlightControlDetail getByUuid(String uuid) {
		return flightControlDetailDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		FlightControlDetail flightControlDetail = getByUuid(uuid);
		flightControlDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(flightControlDetail);
	}
	
	public void delStatusByUuid(String uuid) {
		FlightControlDetail flightControlDetail = getByUuid(uuid);
		flightControlDetail.setStatus(FlightControlDetail.STATUS_DEL_FLAG);//0：已提交；1：已保存草稿；2：已删除；
		update(flightControlDetail);
	}
	
	/**
	 * @author liuXueliang
	 */
	@Override
	public Page<Map<String, Object>> FlightControlList(FlightControlQuery query,HttpServletRequest request,HttpServletResponse response) {
		String activityStatus = request.getParameter("activityStatus");// 0：已提交；1：已保存草稿；2：已删除；
		String showType = request.getParameter("showType");// 1是日期列表，2是酒店列表
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer();
	
		if(!"2".equals(showType)){//默认查询按日期排列的列表数据
			sb.append(" SELECT fcd.uuid as uuid,fcd.departure_date as departureDate,fc.country as country,fc.airline as airline,fcd.spaceGrade_type as spaceGradeType,fcd.price_currency_id as priceCurrencyId,");
			sb.append(" fcd.price as price,fcd.taxes_currency_id as taxesCurrencyId,fcd.taxes_price as taxesPrice,fcd.stock as stock,GROUP_CONCAT(fchd.hotel_uuid) as hotelUuid,");
			sb.append(" fcd.STATUS as STATUS,su1.name as createBy,su2.name as updateBy,fcd.createDate as createDate,fcd.updateDate as updateDate");
			sb.append(" FROM flight_control_detail fcd LEFT JOIN flight_control fc on fc.uuid = fcd.flight_control_uuid AND fc.delFlag = '0'");
			sb.append(" LEFT JOIN flight_control_hotel_detail fchd on fchd.flight_control_detail_uuid = fcd.uuid AND fchd.delFlag = '0'");
			sb.append(" LEFT JOIN sys_user su1 ON su1.id = fcd.createBy	AND su1.delFlag = '0'");
			sb.append(" LEFT JOIN sys_user su2 ON su2.id = fcd.updateBy AND su2.delFlag = '0'");
			sb.append(" WHERE fcd.delFlag = '0'");
			sb.append(" AND fcd.wholesaler_id = "+UserUtils.getUser().getCompany().getId());
			if(!"3".equals(activityStatus)){
				sb.append(" AND fcd.STATUS="+activityStatus);
			}
			if(query.getStartDepartureDate()!=null){
				String startDepartureDate = sdf.format(query.getStartDepartureDate());
				if(StringUtils.isNotBlank(startDepartureDate)){
					sb.append(" AND fcd.departure_date >='"+startDepartureDate+"'");
				}
			}
			if(query.getEndDepartureDate()!=null){
				String endDepartureDate = sdf.format(query.getEndDepartureDate());
				if(StringUtils.isNotBlank(endDepartureDate)){
					sb.append(" AND fcd.departure_date <='"+endDepartureDate+"'");
				}
			}
			if(StringUtils.isNotBlank(query.getName())){
				sb.append(" AND fc.NAME LIKE '%"+query.getName()+"%'");
			}
			if(StringUtils.isNotBlank(query.getIslandUuid())){
				sb.append(" AND fc.island_uuid = '"+query.getIslandUuid()+"'");
			}
			if(query.getAirline()!=null && StringUtils.isNotBlank(query.getAirline().toString())){
				sb.append(" AND fc.airline="+query.getAirline());
			}
			if(query.getSpaceGradeType()!=null && StringUtils.isNotBlank(query.getSpaceGradeType().toString())){
				sb.append(" AND fcd.spaceGrade_type ="+query.getAirline());
			}
			if(query.getStartPrice()!=null && StringUtils.isNotBlank(query.getStartPrice().toString())){
				sb.append(" AND fcd.price >="+query.getStartPrice());
			}
			if(query.getEndPrice()!=null && StringUtils.isNotBlank(query.getEndPrice().toString())){
				sb.append(" AND fcd.price <="+query.getEndPrice());
			}
			if(query.getStartStock()!=null && StringUtils.isNotBlank(query.getStartStock().toString())){
				sb.append(" AND fcd.stock >="+query.getStartStock());
			}
			if(query.getEndStock()!=null && StringUtils.isNotBlank(query.getEndStock().toString())){
				sb.append(" AND fcd.stock <="+query.getEndStock());
			}
			if(query.getHotelUuid()!=null && StringUtils.isNotBlank(query.getHotelUuid().toString())){
				sb.append(" AND fchd.hotel_uuid= '"+query.getHotelUuid()+"'");
			}
			if(query.getStatus()!=null && StringUtils.isNotBlank(query.getStatus().toString())){
				sb.append(" AND fcd.STATUS ="+query.getStatus());
			}
			if(StringUtils.isNotBlank(query.getCreateUser())){
				sb.append(" AND su1.NAME LIKE '%"+query.getCreateUser()+"%'");
			}
			if(StringUtils.isNotBlank(query.getUpdateUser())){
				sb.append(" AND su2.NAME LIKE '%"+query.getUpdateUser()+"%'");
			}
			sb.append(" GROUP BY fcd.uuid");
			Page<Map<String,Object>> page = new Page<Map<String,Object>>(request,response);
			String orderBy = request.getParameter("orderBy");
			if(StringUtils.isBlank(orderBy)){
				page.setOrderBy("");
			}else{
				page.setOrderBy(orderBy);
			}
			return flightControlDetailDao.findBySql(page, sb.toString(), Map.class);
		}
		return null;
	}

	@Override
	public List<FlightControlDetail> getByFlightControlUuid(String flightControluuid) {
		List<FlightControlDetail> list = this.flightControlDetailDao.find("from FlightControlDetail where flightControlUuid=? and delFlag=?", flightControluuid,BaseEntity.DEL_FLAG_NORMAL);
		return list;
	}


}
