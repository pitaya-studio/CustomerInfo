/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.entity.*;
import com.trekiz.admin.modules.hotel.dao.*;
import com.trekiz.admin.modules.hotel.service.*;
import com.trekiz.admin.modules.traveler.dao.TravelerTypeDao;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelRoomServiceImpl  extends BaseService implements HotelRoomService{
	@Autowired
	private HotelRoomDao hotelRoomDao;
	@Autowired
	private HotelRoomOccuRateDao hotelRoomOccuRateDao;
	@Autowired
	private HotelRoomOccuRateDetailDao hotelRoomOccuRateDetailDao;
	@Autowired
	private TravelerTypeDao travelerTypeDao;

	public void save (HotelRoom hotelRoom){
		setOptInfo(hotelRoom, OPERATION_ADD);
		hotelRoomDao.saveObj(hotelRoom);
	}
	
	public void update (HotelRoom hotelRoom){
		setOptInfo(hotelRoom, OPERATION_UPDATE);
		hotelRoomDao.updateObj(hotelRoom);
	}
	
	public HotelRoom getById(java.lang.Long value) {
		return hotelRoomDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		HotelRoom obj = hotelRoomDao.getById(value);
		obj.setDelFlag("1");
		update(obj);
	}	
	
	
	public Page<HotelRoom> find(Page<HotelRoom> page, HotelRoom hotelRoom) {
		DetachedCriteria dc = hotelRoomDao.createDetachedCriteria();
		
	   	if(hotelRoom.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelRoom.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoom.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelRoom.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getHotelUuid())){
			dc.add(Restrictions.like("hotelUuid", "%"+hotelRoom.getHotelUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getRoomName())){
			dc.add(Restrictions.like("roomName", "%"+hotelRoom.getRoomName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getShowName())){
			dc.add(Restrictions.like("showName", "%"+hotelRoom.getShowName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getOccupancyRate())){
			dc.add(Restrictions.like("occupancyRate", "%"+hotelRoom.getOccupancyRate()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getBed())){
			dc.add(Restrictions.like("bed", "%"+hotelRoom.getBed()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getFloor())){
			dc.add(Restrictions.like("floor", "%"+hotelRoom.getFloor()+"%"));
		}
	   	if(hotelRoom.getExtraBedNum()!=null){
	   		dc.add(Restrictions.eq("extraBedNum", hotelRoom.getExtraBedNum()));
	   	}
		if (hotelRoom.getExtraBedCost() != null){
			dc.add(Restrictions.eq("extraBedCost", hotelRoom.getExtraBedCost()));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getExtraBedCustomer())){
			dc.add(Restrictions.like("extraBedCustomer", "%"+hotelRoom.getExtraBedCustomer()+"%"));
		}
	   	if(hotelRoom.getRoomArea()!=null){
	   		dc.add(Restrictions.eq("roomArea", hotelRoom.getRoomArea()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoom.getRoomFeatures())){
			dc.add(Restrictions.like("roomFeatures", "%"+hotelRoom.getRoomFeatures()+"%"));
		}
	   	if(hotelRoom.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelRoom.getSort()));
	   	}
		if(hotelRoom.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelRoom.getInDate()));
		}
		if(hotelRoom.getOutDate()!=null){
			dc.add(Restrictions.eq("outDate", hotelRoom.getOutDate()));
		}
	   	if(hotelRoom.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelRoom.getCreateBy()));
	   	}
		if(hotelRoom.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelRoom.getCreateDate()));
		}
	   	if(hotelRoom.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelRoom.getUpdateBy()));
	   	}
		if(hotelRoom.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelRoom.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelRoom.getDelFlag()+"%"));
		}
	   	if(hotelRoom.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelRoom.getWholesalerId()));
	   	}

		dc.addOrder(Order.asc("sort"));
		return hotelRoomDao.find(page, dc);
	}
	
	public List<HotelRoom> find( HotelRoom hotelRoom) {
		DetachedCriteria dc = hotelRoomDao.createDetachedCriteria();
		
	   	if(hotelRoom.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelRoom.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoom.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelRoom.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelRoom.getHotelUuid()));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getRoomName())){
			dc.add(Restrictions.like("roomName", "%"+hotelRoom.getRoomName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getShowName())){
			dc.add(Restrictions.eq("showName", hotelRoom.getShowName()));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getOccupancyRate())){
			dc.add(Restrictions.eq("occupancyRate", hotelRoom.getOccupancyRate()));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getBed())){
			dc.add(Restrictions.eq("bed", hotelRoom.getBed()));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getFloor())){
			dc.add(Restrictions.eq("floor", hotelRoom.getFloor()));
		}
	   	if(hotelRoom.getExtraBedNum()!=null){
	   		dc.add(Restrictions.eq("extraBedNum", hotelRoom.getExtraBedNum()));
	   	}
	   	if (hotelRoom.getExtraBedCost() != null){
			dc.add(Restrictions.eq("extraBedCost", hotelRoom.getExtraBedCost()));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getExtraBedCustomer())){
			dc.add(Restrictions.eq("extraBedCustomer", hotelRoom.getExtraBedCustomer()));
		}
	   	if(hotelRoom.getRoomArea()!=null){
	   		dc.add(Restrictions.eq("roomArea", hotelRoom.getRoomArea()));
	   	}
		if (StringUtils.isNotEmpty(hotelRoom.getRoomFeatures())){
			dc.add(Restrictions.eq("roomFeatures", hotelRoom.getRoomFeatures()));
		}
	   	if(hotelRoom.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelRoom.getSort()));
	   	}
		if(hotelRoom.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelRoom.getInDate()));
		}
		if(hotelRoom.getOutDate()!=null){
			dc.add(Restrictions.eq("outDate", hotelRoom.getOutDate()));
		}
	   	if(hotelRoom.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelRoom.getCreateBy()));
	   	}
		if(hotelRoom.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelRoom.getCreateDate()));
		}
	   	if(hotelRoom.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelRoom.getUpdateBy()));
	   	}
		if(hotelRoom.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelRoom.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelRoom.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelRoom.getDelFlag()));
		}
	   	if(hotelRoom.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelRoom.getWholesalerId()));
	   	}

		dc.addOrder(Order.asc("sort"));
		return hotelRoomDao.find(dc);
	}
	
	public HotelRoom getByUuid(String uuid) {
		return hotelRoomDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelRoom hotelRoom = getByUuid(uuid);
		hotelRoom.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelRoom);
	}
	
	public boolean findIsExist(String hotelUuid, String uuid, String roomName, Long companyId) {
		StringBuffer sb = new StringBuffer("from HotelRoom hotelRoom where hotelRoom.uuid != ? and hotelRoom.hotelUuid = ? and hotelRoom.roomName = ? and hotelRoom.wholesalerId = ? and hotelRoom.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		
		List<HotelRoom> hotelRooms = hotelRoomDao.find(sb.toString(), uuid, hotelUuid, roomName, companyId.intValue());
		
		if(hotelRooms == null || hotelRooms.size() == 0) {
			return false;
		}
		return true;
	}
	
	public void saveHotelRoom (HotelRoom hotelRoom, String occupancyRates) {
		Map<HotelRoom, Map<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>>> hotelRoomInfo = buildHotelRoomOccuRate(hotelRoom, occupancyRates, "");
		
		//保存酒店房型和酒店容住率表数据
		Set<HotelRoom> hotelRoomInfoSet = hotelRoomInfo.keySet();
		for(HotelRoom hotelRoomEntity : hotelRoomInfoSet) {
			setOptInfo(hotelRoomEntity, OPERATION_ADD);
			hotelRoomDao.saveObj(hotelRoomEntity);
			
			Map<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>> hotelRoomOccuRateMap = hotelRoomInfo.get(hotelRoomEntity);
			Set<HotelRoomOccuRate> hotelRoomOccuRateSet = hotelRoomOccuRateMap.keySet();
			
			if(hotelRoomOccuRateSet.isEmpty()) {
				continue;
			}			
			for(HotelRoomOccuRate hotelRoomOccuRate : hotelRoomOccuRateSet) {
				hotelRoomOccuRate.setHotelRoomUuid(hotelRoomEntity.getUuid());
				
				setOptInfo(hotelRoomOccuRate, OPERATION_ADD);
				hotelRoomOccuRateDao.saveObj(hotelRoomOccuRate);
				
				List<HotelRoomOccuRateDetail> hotelRoomOccuRateDetails = hotelRoomOccuRateMap.get(hotelRoomOccuRate);
				
				if(hotelRoomOccuRateDetails.isEmpty()) {
					continue;
				}
				for(HotelRoomOccuRateDetail hotelRoomOccuRateDetail : hotelRoomOccuRateDetails) {
					hotelRoomOccuRateDetail.setHotelRoomUuid(hotelRoomOccuRate.getHotelRoomUuid());
					hotelRoomOccuRateDetail.setHotelRoomOccuRateUuid(hotelRoomOccuRate.getUuid());

					setOptInfo(hotelRoomOccuRateDetail, OPERATION_ADD);
					hotelRoomOccuRateDetailDao.saveObj(hotelRoomOccuRateDetail);
				}
			}
		}
	}
	
	/**
	 * 组装酒店房型和酒店容住率数据
		* 
		* @param hotelRoom 酒店房型
		* @param occupancyRates 容住率
		* @param occupancys 可住人数
		* @return void
		* @author majiancheng
		* @Time 2015-5-4
	 */
	private Map<HotelRoom, Map<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>>> buildHotelRoomOccuRate(HotelRoom hotelRoom, String occupancyRates, String roomOccuRateUuids) {
		//occupancyRates （容住率）的字符串格式为b664a75e0c8f4d8086202404bf9b1b80_1,ea699f1d49f84380948a353828c58a70_2;b664a75e0c8f4d8086202404bf9b1b80_2,ea699f1d49f84380948a353828c58a70_2
		//occupancys （可住人数）的字符串格式为3;4
		Map<HotelRoom, Map<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>>> hotelRoomInfo = new HashMap<HotelRoom, Map<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>>>(); 
		
		if(StringUtils.isEmpty(occupancyRates)) {
			hotelRoom.setOccupancyRate("");
			
			Map<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>> hotelRoomOccuRateMap = new HashMap<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>>();
			if(StringUtils.isNotEmpty(roomOccuRateUuids)) {
				String[] roomOccuRateUuidArray = roomOccuRateUuids.split(";");
				for(int i=0; i<roomOccuRateUuidArray.length; i++) {
					HotelRoomOccuRate hotelRoomOccuRate = hotelRoomOccuRateDao.getByUuid(roomOccuRateUuidArray[i]);
					hotelRoomOccuRate.setOccupancy(0);
					hotelRoomOccuRate.setOccupancyRate("");
					hotelRoomOccuRateMap.put(hotelRoomOccuRate, new ArrayList<HotelRoomOccuRateDetail>());
				}
			}
			
			hotelRoomInfo.put(hotelRoom, hotelRoomOccuRateMap);
			
			return hotelRoomInfo;
		}
		
		String[] occupancyRateArray = occupancyRates.split(";");
		String[] roomOccuRateUuidArray = roomOccuRateUuids.split(";");
		
		Map<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>> hotelRoomOccuRateMap = new HashMap<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>>();
		
		StringBuffer hotelRoomOccupancyRate = new StringBuffer();
		for(int i=0; i < occupancyRateArray.length; i++) {
			if(StringUtils.isEmpty(occupancyRateArray[i]) || occupancyRateArray[i].indexOf(",") == -1) {
				if(roomOccuRateUuidArray.length > i) {
					HotelRoomOccuRate hotelRoomOccuRate = hotelRoomOccuRateDao.getByUuid(roomOccuRateUuidArray[i]);
					hotelRoomOccuRate.setOccupancy(0);
					hotelRoomOccuRate.setOccupancyRate("");
					hotelRoomOccuRateMap.put(hotelRoomOccuRate, new ArrayList<HotelRoomOccuRateDetail>());
				}
				continue;
			}
			String occuRemark = occupancyRateArray[i].substring(occupancyRateArray[i].lastIndexOf(",")+1, occupancyRateArray[i].length());
			String uuidAndNumStrs = occupancyRateArray[i].substring(0, occupancyRateArray[i].lastIndexOf(","));
			String[] uuidAndNumArray = uuidAndNumStrs.split(",");
			
			HotelRoomOccuRate hotelRoomOccuRate = null; 
			if(roomOccuRateUuidArray.length > i) {
				hotelRoomOccuRate = hotelRoomOccuRateDao.getByUuid(roomOccuRateUuidArray[i]);
			}
			
			List<HotelRoomOccuRateDetail> hotelRoomOccuRateDetails = new ArrayList<HotelRoomOccuRateDetail>();
			
			StringBuffer occupancyRate = new StringBuffer();
			int occupancy = 0;
			
			for(int j=0; j<uuidAndNumArray.length; j++) {
				String uuidAndNumStr = uuidAndNumArray[j];
				
				String[] uuidAndNum = uuidAndNumStr.split("_");
				
				HotelRoomOccuRateDetail hotelRoomOccuRateDetail = null;
				TravelerType travelerType = travelerTypeDao.getByUuid(uuidAndNum[0]);
				if(hotelRoomOccuRate != null) {
					hotelRoomOccuRateDetail = hotelRoomOccuRateDetailDao.getByRoomOccuRateUuidAndTravelerTypeUuid(hotelRoomOccuRate.getUuid(), uuidAndNum[0]);
				} 
				
				if(hotelRoomOccuRate == null || hotelRoomOccuRateDetail == null) {
					hotelRoomOccuRateDetail = new HotelRoomOccuRateDetail();
					hotelRoomOccuRateDetail.setHotelUuid(hotelRoom.getHotelUuid());
				}
				
				
				if(travelerType != null) {
					try{
						hotelRoomOccuRateDetail.setTravelerTypeUuid(travelerType.getUuid());
						hotelRoomOccuRateDetail.setShortName(travelerType.getShortName());
						hotelRoomOccuRateDetail.setCount(Integer.parseInt(uuidAndNum[1]));
						
						//组装hotelRoomOccuRate里面的容住率，格式为2A+C
						occupancyRate.append(uuidAndNum[1]);
						occupancyRate.append(travelerType.getShortName());
						occupancyRate.append("+");
						
						occupancy += Integer.parseInt(uuidAndNum[1]);
					} catch(Exception e) {
						e.printStackTrace();
					}
					
				}
				
				hotelRoomOccuRateDetails.add(hotelRoomOccuRateDetail);
			}
			
			if(hotelRoomOccuRate != null) {
				hotelRoomOccuRate.setOccupancyRate(occupancyRate.substring(0, occupancyRate.lastIndexOf("+")));
				hotelRoomOccuRate.setOccupancy(occupancy);
				hotelRoomOccuRate.setRemark(occuRemark);
			} else {
				hotelRoomOccuRate = new HotelRoomOccuRate();
				hotelRoomOccuRate.setHotelUuid(hotelRoom.getHotelUuid());
				hotelRoomOccuRate.setOccupancyRate(occupancyRate.substring(0, occupancyRate.lastIndexOf("+")));
				hotelRoomOccuRate.setOccupancy(occupancy);
				hotelRoomOccuRate.setRemark(occuRemark);
			}
			
			hotelRoomOccuRateMap.put(hotelRoomOccuRate, hotelRoomOccuRateDetails);
			
			//组装hotelRoom里面的容住率，格式为2A+C/3A/2B+C
			hotelRoomOccupancyRate.append(hotelRoomOccuRate.getOccupancyRate());
			hotelRoomOccupancyRate.append("/");
		}
		
		if(hotelRoomOccupancyRate.indexOf("/") != -1) {
			hotelRoom.setOccupancyRate(hotelRoomOccupancyRate.substring(0, hotelRoomOccupancyRate.lastIndexOf("/")));
		} else {
			hotelRoom.setOccupancyRate("");
		}
		
		hotelRoomInfo.put(hotelRoom, hotelRoomOccuRateMap);
		
		return hotelRoomInfo;
		
	}
	
	public void updateHotelRoom(HotelRoom hotelRoom, String occupancyRates, String roomOccuRateUuids) {
		
		//删除容住率信息
		List<HotelRoomOccuRate> hotelRoomOccuRates = hotelRoomOccuRateDao.getByHotelRoomUuid(hotelRoom.getUuid());
		//获取更新前所有的容住率详情表中的uuid集合
		List<String> hotelRoomOccuRateDetailUuids = hotelRoomOccuRateDetailDao.getDetailUuidsByRoomUuid(hotelRoom.getUuid());
		String[] roomOccuRateUuidArray = roomOccuRateUuids.split(";");
		List<String> roomOccuRateUuidList = Arrays.asList(roomOccuRateUuidArray);
		if(!hotelRoomOccuRates.isEmpty()) {
			for(HotelRoomOccuRate hotelRoomOccuRate : hotelRoomOccuRates) {
				if(roomOccuRateUuidList.contains(hotelRoomOccuRate.getUuid())) {
					
				} else {
					hotelRoomOccuRateDetailDao.removeByRoomOccuRateUuid(hotelRoomOccuRate.getUuid());
					
					hotelRoomOccuRate.setDelFlag("1");
					setOptInfo(hotelRoomOccuRate, OPERATION_UPDATE);
					hotelRoomOccuRateDao.updateObj(hotelRoomOccuRate);
				}
			}
		}

		//组装酒店房型信息和酒店容住率信息
		Map<HotelRoom, Map<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>>> hotelRoomInfo = buildHotelRoomOccuRate(hotelRoom, occupancyRates, roomOccuRateUuids);
		
		//更新酒店房型和酒店容住率表数据
		Set<HotelRoom> hotelRoomInfoSet = hotelRoomInfo.keySet();
		for(HotelRoom hotelRoomEntity : hotelRoomInfoSet) {
			setOptInfo(hotelRoomEntity, OPERATION_UPDATE);
			hotelRoomDao.updateObj(hotelRoomEntity);
			
			Map<HotelRoomOccuRate, List<HotelRoomOccuRateDetail>> hotelRoomOccuRateMap = hotelRoomInfo.get(hotelRoomEntity);
			Set<HotelRoomOccuRate> hotelRoomOccuRateSet = hotelRoomOccuRateMap.keySet();
			
			if(hotelRoomOccuRateSet.isEmpty()) {
				continue;
			}
			for(HotelRoomOccuRate hotelRoomOccuRate : hotelRoomOccuRateSet) {
				if(StringUtils.isEmpty(hotelRoomOccuRate.getUuid())) {
					hotelRoomOccuRate.setHotelRoomUuid(hotelRoomEntity.getUuid());
					
					setOptInfo(hotelRoomOccuRate, OPERATION_ADD);
					hotelRoomOccuRateDao.saveObj(hotelRoomOccuRate);
				} else {
					setOptInfo(hotelRoomOccuRate, OPERATION_UPDATE);
					hotelRoomOccuRateDao.updateObj(hotelRoomOccuRate);
				}
				
				List<HotelRoomOccuRateDetail> hotelRoomOccuRateDetails = hotelRoomOccuRateMap.get(hotelRoomOccuRate);
				
				if(hotelRoomOccuRateDetails.isEmpty()) {
					hotelRoomOccuRateDetailDao.removeByRoomOccuRateUuid(hotelRoomOccuRate.getUuid());
					continue;
				}
				for(HotelRoomOccuRateDetail hotelRoomOccuRateDetail : hotelRoomOccuRateDetails) {
					if(StringUtils.isEmpty(hotelRoomOccuRateDetail.getUuid())) {
						hotelRoomOccuRateDetail.setHotelRoomUuid(hotelRoomOccuRate.getHotelRoomUuid());
						hotelRoomOccuRateDetail.setHotelRoomOccuRateUuid(hotelRoomOccuRate.getUuid());

						setOptInfo(hotelRoomOccuRateDetail, OPERATION_ADD);
						hotelRoomOccuRateDetailDao.saveObj(hotelRoomOccuRateDetail);
					} else {
						if(hotelRoomOccuRateDetailUuids.contains(hotelRoomOccuRateDetail.getUuid())) {
							hotelRoomOccuRateDetailUuids.remove(hotelRoomOccuRateDetail.getUuid());
						}
						
						hotelRoomOccuRateDetailDao.updateObj(hotelRoomOccuRateDetail);
					}
					
					
				}
			}
		}
		
		//删除未用的容住率明细信息
		hotelRoomOccuRateDetailDao.removeByOccuRateDetailUuids(hotelRoomOccuRateDetailUuids);
	}
	
}
