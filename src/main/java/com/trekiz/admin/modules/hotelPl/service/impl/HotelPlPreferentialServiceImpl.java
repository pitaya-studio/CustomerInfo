/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.SysCompanyDictViewDao;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialMatterDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialMatterValueDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRelDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRelHotelDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRequireDao;
import com.trekiz.admin.modules.hotelPl.dao.HotelPlPreferentialRoomDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.input.HotelPlPreferentialInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPreferentialQuery;
import com.trekiz.admin.modules.hotelPl.service.HotelPlPreferentialService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelPlPreferentialServiceImpl  extends BaseService implements HotelPlPreferentialService{
	@Autowired
	private HotelPlPreferentialDao hotelPlPreferentialDao;
	@Autowired
	private HotelPlPreferentialRoomDao hotelPlPreferentialRoomDao;
	@Autowired
	private HotelPlPreferentialMatterDao hotelPlPreferentialMatterDao;
	@Autowired
	private HotelPlPreferentialRequireDao hotelPlPreferentialRequireDao;
	@Autowired
	private HotelPlPreferentialRelHotelDao hotelPlPreferentialRelHotelDao;
	@Autowired
	private HotelPlPreferentialRelDao hotelPlPreferentialRelDao;
	@Autowired
	private HotelPlPreferentialMatterValueDao hotelPlPreferentialMatterValueDao;
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;

	public void save (HotelPlPreferential hotelPlPreferential){
		super.setOptInfo(hotelPlPreferential, BaseService.OPERATION_ADD);
		hotelPlPreferentialDao.saveObj(hotelPlPreferential);
	}
	
	public void save (HotelPlPreferentialInput hotelPlPreferentialInput){
		HotelPlPreferential hotelPlPreferential = hotelPlPreferentialInput.getHotelPlPreferential();
		super.setOptInfo(hotelPlPreferential, BaseService.OPERATION_ADD);
		hotelPlPreferentialDao.saveObj(hotelPlPreferential);
	}
	
	public void update (HotelPlPreferential hotelPlPreferential){
		super.setOptInfo(hotelPlPreferential, BaseService.OPERATION_UPDATE);
		hotelPlPreferentialDao.updateObj(hotelPlPreferential);
	}
	
	public HotelPlPreferential getById(java.lang.Integer value) {
		return hotelPlPreferentialDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelPlPreferential obj = hotelPlPreferentialDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelPlPreferential> find(Page<HotelPlPreferential> page, HotelPlPreferentialQuery hotelPlPreferentialQuery) {
		DetachedCriteria dc = hotelPlPreferentialDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getPreferentialName())){
			dc.add(Restrictions.eq("preferentialName", hotelPlPreferentialQuery.getPreferentialName()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getBookingCode())){
			dc.add(Restrictions.eq("bookingCode", hotelPlPreferentialQuery.getBookingCode()));
		}
		if(hotelPlPreferentialQuery.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelPlPreferentialQuery.getInDate()));
		}
		if(hotelPlPreferentialQuery.getOutDate()!=null){
			dc.add(Restrictions.eq("outDate", hotelPlPreferentialQuery.getOutDate()));
		}
		if(hotelPlPreferentialQuery.getBookingStartDate()!=null){
			dc.add(Restrictions.eq("bookingStartDate", hotelPlPreferentialQuery.getBookingStartDate()));
		}
		if(hotelPlPreferentialQuery.getBookingEndDate()!=null){
			dc.add(Restrictions.eq("bookingEndDate", hotelPlPreferentialQuery.getBookingEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelPlPreferentialQuery.getIslandWay()));
		}
	   	if(hotelPlPreferentialQuery.getIsRelation()!=null){
	   		dc.add(Restrictions.eq("isRelation", hotelPlPreferentialQuery.getIsRelation()));
	   	}
	   	if(hotelPlPreferentialQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialDao.find(page, dc);
	}
	
	public List<HotelPlPreferential> find( HotelPlPreferentialQuery hotelPlPreferentialQuery) {
		DetachedCriteria dc = hotelPlPreferentialDao.createDetachedCriteria();
		
	   	if(hotelPlPreferentialQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelPlPreferentialQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelPlPreferentialQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelPlPreferentialQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelPlPreferentialQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelPlPreferentialQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getPreferentialName())){
			dc.add(Restrictions.eq("preferentialName", hotelPlPreferentialQuery.getPreferentialName()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getBookingCode())){
			dc.add(Restrictions.eq("bookingCode", hotelPlPreferentialQuery.getBookingCode()));
		}
		if(hotelPlPreferentialQuery.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelPlPreferentialQuery.getInDate()));
		}
		if(hotelPlPreferentialQuery.getOutDate()!=null){
			dc.add(Restrictions.eq("outDate", hotelPlPreferentialQuery.getOutDate()));
		}
		if(hotelPlPreferentialQuery.getBookingStartDate()!=null){
			dc.add(Restrictions.eq("bookingStartDate", hotelPlPreferentialQuery.getBookingStartDate()));
		}
		if(hotelPlPreferentialQuery.getBookingEndDate()!=null){
			dc.add(Restrictions.eq("bookingEndDate", hotelPlPreferentialQuery.getBookingEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelPlPreferentialQuery.getIslandWay()));
		}
	   	if(hotelPlPreferentialQuery.getIsRelation()!=null){
	   		dc.add(Restrictions.eq("isRelation", hotelPlPreferentialQuery.getIsRelation()));
	   	}
	   	if(hotelPlPreferentialQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelPlPreferentialQuery.getCreateBy()));
	   	}
		if(hotelPlPreferentialQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelPlPreferentialQuery.getCreateDate()));
		}
	   	if(hotelPlPreferentialQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelPlPreferentialQuery.getUpdateBy()));
	   	}
		if(hotelPlPreferentialQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelPlPreferentialQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelPlPreferentialQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelPlPreferentialQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelPlPreferentialDao.find(dc);
	}
	
	public HotelPlPreferential getByUuid(String uuid) {
		return hotelPlPreferentialDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelPlPreferential hotelPlPreferential = getByUuid(uuid);
		hotelPlPreferential.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelPlPreferential);
	}
	
	public List<HotelPlPreferential> findPlPreferentialsByHotelPlUuid(String hotelPlUuid) {
		List<HotelPlPreferential> hotelPlPreferentials = hotelPlPreferentialDao.findPlPreferentialsByHotelPlUuid(hotelPlUuid);
		this.setPlPreferentialsParam(hotelPlPreferentials);
		return hotelPlPreferentials;
	}
	
	/**
	 * 设置价单信息的详细信息（方法抽取成公用）add by zhanghao
	 * @param hotelPlPreferentials
	 */
	public void setPlPreferentialsParam(List<HotelPlPreferential> hotelPlPreferentials){
		if(CollectionUtils.isNotEmpty(hotelPlPreferentials)) {
			for(HotelPlPreferential hotelPlPreferential : hotelPlPreferentials) {
				this.setPlPreferentialParam(hotelPlPreferential);
				
				//设置上岛方式
				if(StringUtils.isNotBlank(hotelPlPreferential.getIslandWay())){
					String[] array = hotelPlPreferential.getIslandWay().split(";");
					if(ArrayUtils.isNotEmpty(array)){
						List<SysCompanyDictView> islandWayList = new ArrayList<SysCompanyDictView>();
						for(String islandWay:array){
							SysCompanyDictView src = sysCompanyDictViewDao.getByUuId(islandWay);
							
							SysCompanyDictView dest = new SysCompanyDictView();
							BeanUtil.copySimpleProperties(dest, src);
							islandWayList.add(dest);
						}
						hotelPlPreferential.setIslandWayList(islandWayList);
					}
				}
			}
		}
	}
	
	private void setPlPreferentialParam(HotelPlPreferential hotelPlPreferential){
		hotelPlPreferential.setPreferentialRoomList(hotelPlPreferentialRoomDao.findRoomsByPreferentialUuid(hotelPlPreferential.getUuid()));
		hotelPlPreferential.setRequire(hotelPlPreferentialRequireDao.findRequireByPreferentialUuid(hotelPlPreferential.getUuid()));
		hotelPlPreferential.setHotelPlPreferentialRelHotel(hotelPlPreferentialRelHotelDao.getRelHotelByPreferentialUuid(hotelPlPreferential.getUuid()));
		hotelPlPreferential.setHotelPlPreferentialRels(hotelPlPreferentialRelDao.getPreferentialRelsByPreferentialUuid(hotelPlPreferential.getUuid()));
//		hotelPlPreferential.setHotelPlPreferentialTaxs(hotelPlPreferentialTaxDao.getTaxsByPreferentialUuid(hotelPlPreferential.getUuid()));
		hotelPlPreferential.setMatter(hotelPlPreferentialMatterDao.findMatterByPreferentialUuid(hotelPlPreferential.getUuid()));
		hotelPlPreferential.setValueList(hotelPlPreferentialMatterValueDao.findMatterValueListByPreferentialUuid(hotelPlPreferential.getUuid()));
	}
	
	/**
	 * 根据优惠uuid获取优惠所有信息（包括子表数据）
	*<p>Title: getWholePlPreferentialByUuid</p>
	* @return HotelPlPreferential 返回类型
	* @author majiancheng
	* @date 2015-7-17 下午5:46:51
	* @throws
	 */
	public HotelPlPreferential getWholePlPreferentialByUuid(String uuid) {
		HotelPlPreferential hotelPlPreferential = this.getByUuid(uuid);
		
		this.setPlPreferentialParam(hotelPlPreferential);
		
		return hotelPlPreferential;
	}
	
	
	
	/**
	 * 自动报价 根据条件筛选 符合条件的优惠信息 add by zhanghao
	 * 只筛选符合日期的优惠，具体是否适用会进一步做筛选
	 * @return
	 */
	public List<HotelPlPreferential> getHotelPlPreferentials4AutoQuotedPrice( HotelPlPreferentialQuery hotelPlPreferentialQuery){
		List<HotelPlPreferential> hotelPlPreferentials = hotelPlPreferentialDao.getHotelPlPreferentials4AutoQuotedPrice(hotelPlPreferentialQuery);
		
		this.setPlPreferentialsParam(hotelPlPreferentials);
		return hotelPlPreferentials;
	}
	
	public List<HotelPlPreferential> getRelPlPreferentialsByPlUuid(String hotelPlUuid) {
		return hotelPlPreferentialDao.getRelPlPreferentialsByPlUuid(hotelPlUuid);
	}
}
