/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferential;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatter;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatterValue;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRel;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRelHotel;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequire;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRoom;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialMatterDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialMatterValueDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRelDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRelHotelDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRequireDao;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRoomDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferential;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatter;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialMatterValue;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRel;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRelHotel;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRequire;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRoom;
import com.trekiz.admin.modules.hotelQuotePreferential.input.HotelQuotePreferentialInput;
import com.trekiz.admin.modules.hotelQuotePreferential.query.HotelQuotePreferentialQuery;
import com.trekiz.admin.modules.hotelQuotePreferential.service.HotelQuotePreferentialService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialServiceImpl  extends BaseService implements HotelQuotePreferentialService{
	@Autowired
	private HotelQuotePreferentialDao hotelQuotePreferentialDao;
	@Autowired
	private HotelQuotePreferentialRoomDao hotelQuotePreferentialRoomDao;
	@Autowired
	private HotelQuotePreferentialRequireDao hotelQuotePreferentialRequireDao;
	@Autowired
	private HotelQuotePreferentialRelHotelDao hotelQuotePreferentialRelHotelDao;
	@Autowired
	private HotelQuotePreferentialRelDao hotelQuotePreferentialRelDao;
	@Autowired
	private HotelQuotePreferentialMatterValueDao hotelQuotePreferentialMatterValueDao;
	@Autowired
	private HotelQuotePreferentialMatterDao hotelQuotePreferentialMatterDao;

	public void save (HotelQuotePreferential hotelQuotePreferential){
		super.setOptInfo(hotelQuotePreferential, BaseService.OPERATION_ADD);
		hotelQuotePreferentialDao.saveObj(hotelQuotePreferential);
	}
	
	public void save (HotelQuotePreferentialInput hotelQuotePreferentialInput){
		HotelQuotePreferential hotelQuotePreferential = hotelQuotePreferentialInput.getHotelQuotePreferential();
		super.setOptInfo(hotelQuotePreferential, BaseService.OPERATION_ADD);
		hotelQuotePreferentialDao.saveObj(hotelQuotePreferential);
	}
	
	public void update (HotelQuotePreferential hotelQuotePreferential){
		super.setOptInfo(hotelQuotePreferential, BaseService.OPERATION_UPDATE);
		hotelQuotePreferentialDao.updateObj(hotelQuotePreferential);
	}
	
	public HotelQuotePreferential getById(java.lang.Integer value) {
		return hotelQuotePreferentialDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuotePreferential obj = hotelQuotePreferentialDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuotePreferential> find(Page<HotelQuotePreferential> page, HotelQuotePreferentialQuery hotelQuotePreferentialQuery) {
		DetachedCriteria dc = hotelQuotePreferentialDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getPreferentialName())){
			dc.add(Restrictions.eq("preferentialName", hotelQuotePreferentialQuery.getPreferentialName()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getBookingCode())){
			dc.add(Restrictions.eq("bookingCode", hotelQuotePreferentialQuery.getBookingCode()));
		}
		if(hotelQuotePreferentialQuery.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelQuotePreferentialQuery.getInDate()));
		}
		if(hotelQuotePreferentialQuery.getOutDate()!=null){
			dc.add(Restrictions.eq("outDate", hotelQuotePreferentialQuery.getOutDate()));
		}
		if(hotelQuotePreferentialQuery.getBookingStartDate()!=null){
			dc.add(Restrictions.eq("bookingStartDate", hotelQuotePreferentialQuery.getBookingStartDate()));
		}
		if(hotelQuotePreferentialQuery.getBookingEndDate()!=null){
			dc.add(Restrictions.eq("bookingEndDate", hotelQuotePreferentialQuery.getBookingEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelQuotePreferentialQuery.getIslandWay()));
		}
	   	if(hotelQuotePreferentialQuery.getIsRelation()!=null){
	   		dc.add(Restrictions.eq("isRelation", hotelQuotePreferentialQuery.getIsRelation()));
	   	}
	   	if(hotelQuotePreferentialQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialDao.find(page, dc);
	}
	
	public List<HotelQuotePreferential> find( HotelQuotePreferentialQuery hotelQuotePreferentialQuery) {
		DetachedCriteria dc = hotelQuotePreferentialDao.createDetachedCriteria();
		
	   	if(hotelQuotePreferentialQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuotePreferentialQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuotePreferentialQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getHotelPlUuid())){
			dc.add(Restrictions.eq("hotelPlUuid", hotelQuotePreferentialQuery.getHotelPlUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuotePreferentialQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuotePreferentialQuery.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getPreferentialName())){
			dc.add(Restrictions.eq("preferentialName", hotelQuotePreferentialQuery.getPreferentialName()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getBookingCode())){
			dc.add(Restrictions.eq("bookingCode", hotelQuotePreferentialQuery.getBookingCode()));
		}
		if(hotelQuotePreferentialQuery.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelQuotePreferentialQuery.getInDate()));
		}
		if(hotelQuotePreferentialQuery.getOutDate()!=null){
			dc.add(Restrictions.eq("outDate", hotelQuotePreferentialQuery.getOutDate()));
		}
		if(hotelQuotePreferentialQuery.getBookingStartDate()!=null){
			dc.add(Restrictions.eq("bookingStartDate", hotelQuotePreferentialQuery.getBookingStartDate()));
		}
		if(hotelQuotePreferentialQuery.getBookingEndDate()!=null){
			dc.add(Restrictions.eq("bookingEndDate", hotelQuotePreferentialQuery.getBookingEndDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", hotelQuotePreferentialQuery.getIslandWay()));
		}
	   	if(hotelQuotePreferentialQuery.getIsRelation()!=null){
	   		dc.add(Restrictions.eq("isRelation", hotelQuotePreferentialQuery.getIsRelation()));
	   	}
	   	if(hotelQuotePreferentialQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuotePreferentialQuery.getCreateBy()));
	   	}
		if(hotelQuotePreferentialQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuotePreferentialQuery.getCreateDate()));
		}
	   	if(hotelQuotePreferentialQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuotePreferentialQuery.getUpdateBy()));
	   	}
		if(hotelQuotePreferentialQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuotePreferentialQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuotePreferentialQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuotePreferentialQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuotePreferentialDao.find(dc);
	}
	
	public HotelQuotePreferential getByUuid(String uuid) {
		return hotelQuotePreferentialDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuotePreferential hotelQuotePreferential = getByUuid(uuid);
		hotelQuotePreferential.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuotePreferential);
	}
	
	public HotelPlPreferential getWholePlPreferentialByUuid(String uuid) {
		HotelQuotePreferential hotelQuotePreferential = this.getByUuid(uuid);
		HotelPlPreferential hotelPlPreferential = new HotelPlPreferential();
		BeanUtils.copyProperties(hotelPlPreferential,hotelQuotePreferential);
		this.setPlPreferentialParam(hotelPlPreferential);
		
		return hotelPlPreferential;
	}
	/**
	 * 查询的是报价的优惠，转换成报价类的结构，为显示数据
	 * @param hotelQuoteUuid
	 * @return
	 */
	public List<HotelPlPreferential> findQuotePreferentialsByHotelQuoteUuid (String hotelQuoteUuid){
		List<HotelPlPreferential> plPreferentialLists = hotelQuotePreferentialDao.findQuotePreferentialsByHotelQuoteUuid(hotelQuoteUuid);
		/*if(CollectionUtils.isNotEmpty(plPreferentialLists)) {
			for(HotelPlPreferential hotelPlPreferential :plPreferentialLists){
				this.setPlPreferentialParam(hotelPlPreferential);
			}
		}*/
		return plPreferentialLists;
	}
	
	private void setPlPreferentialParam(HotelPlPreferential hotelPlPreferential){
		
		List<HotelQuotePreferentialRoom> roomList = hotelQuotePreferentialRoomDao.findRoomsByPreferentialUuid(hotelPlPreferential.getUuid());
		List<HotelPlPreferentialRoom> plRoomLIst = new ArrayList<HotelPlPreferentialRoom>();
		for(HotelQuotePreferentialRoom room :roomList){
			HotelPlPreferentialRoom plRoom = new HotelPlPreferentialRoom();
			//BeanUtils.copyProperties(room,plRoom);
			BeanUtil.copySimpleProperties(plRoom,room,true);
			plRoom.setHotelPlPreferentialUuid(room.getHotelQuotePreferentialUuid());
			plRoom.setHotelMealList(room.getHotelMealList());
			plRoomLIst.add(plRoom);
		}
		hotelPlPreferential.setPreferentialRoomList(plRoomLIst);
		
		HotelQuotePreferentialRequire require = hotelQuotePreferentialRequireDao.findRequireByPreferentialUuid(hotelPlPreferential.getUuid());
		HotelPlPreferentialRequire  plRequire = new HotelPlPreferentialRequire();
		//BeanUtils.copyProperties(plRequire,require);
		BeanUtil.copySimpleProperties(plRequire,require,true);
		plRequire.setHotelPlPreferentialUuid(require.getHotelQuotePreferentialUuid());
		hotelPlPreferential.setRequire(plRequire);
		
		HotelQuotePreferentialRelHotel relHotel = hotelQuotePreferentialRelHotelDao.getRelHotelByPreferentialUuid(hotelPlPreferential.getUuid());
		HotelPlPreferentialRelHotel  plRelHotel = new HotelPlPreferentialRelHotel();
		//BeanUtils.copyProperties(plRelHotel,relHotel);
		//BeanUtil.copySimpleProperties(plRelHotel,relHotel,true);
		if(relHotel!=null){
			BeanUtils.copyProperties(relHotel,plRelHotel);
			if (StringUtils.isNotBlank(relHotel.getHotelQuotePreferentialUuid())) {
				plRelHotel.setHotelPlPreferentialUuid(relHotel.getHotelQuotePreferentialUuid());
			}
		}
		hotelPlPreferential.setHotelPlPreferentialRelHotel(plRelHotel);
		
		List<HotelQuotePreferentialRel> relList = hotelQuotePreferentialRelDao.getPreferentialRelsByPreferentialUuid(hotelPlPreferential.getUuid());
		List<HotelPlPreferentialRel> plRelList = new ArrayList<HotelPlPreferentialRel>();
		for(HotelQuotePreferentialRel rel : relList){
			HotelPlPreferentialRel plRel = new HotelPlPreferentialRel();
			//BeanUtils.copyProperties(plRel,rel);
			BeanUtil.copySimpleProperties(plRel,rel,true);
			plRel.setHotelPlPreferentialUuid(rel.getRelHotelPlPreferentialName());
			plRel.setRelHotelPlPreferentialUuid(rel.getRelHotelQuotePreferentialUuid());
			plRelList.add(plRel);
		}
		hotelPlPreferential.setHotelPlPreferentialRels(plRelList);
		
	  //hotelPlPreferential.setHotelPlPreferentialTaxs(hotelPlPreferentialTaxDao.getTaxsByPreferentialUuid(hotelPlPreferential.getUuid()));
		HotelQuotePreferentialMatter  matter = hotelQuotePreferentialMatterDao.findMatterByPreferentialUuid(hotelPlPreferential.getUuid());
		HotelPlPreferentialMatter plMatter = new HotelPlPreferentialMatter();
		BeanUtils.copyProperties(matter,plMatter);
		//BeanUtil.copySimpleProperties(plMatter,matter,true);
		plMatter.setHotelPlPreferentialUuid(matter.getHotelQuotePreferentialUuid());
		hotelPlPreferential.setMatter(plMatter);
		
		List<HotelQuotePreferentialMatterValue> valueList = hotelQuotePreferentialMatterValueDao.findMatterValueListByPreferentialUuid(hotelPlPreferential.getUuid());
		List<HotelPlPreferentialMatterValue> plvalueList = new ArrayList<HotelPlPreferentialMatterValue>();
		for(HotelQuotePreferentialMatterValue value :valueList){
			HotelPlPreferentialMatterValue plValue = new HotelPlPreferentialMatterValue();
			BeanUtils.copyProperties(value,plValue);
			//BeanUtil.copySimpleProperties(plValue,value,true);
			plValue.setHotelPlPreferentialMatterUuid(value.getHotelQuotePreferentialMatterUuid());
			plValue.setHotelPlPreferentialUuid(value.getHotelQuotePreferentialUuid());
			plValue.setMyKey(value.getMyKeyvar());
			plvalueList.add(plValue);
		}
		hotelPlPreferential.setValueList(plvalueList);
	}
	
}
