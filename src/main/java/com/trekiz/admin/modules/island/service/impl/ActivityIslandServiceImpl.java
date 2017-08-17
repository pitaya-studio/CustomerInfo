/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.island.dao.ActivityIslandDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupAirlineDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupLowpriceDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupMealDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupMealRiseDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupPriceDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupRoomDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandShareDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandVisaFileDao;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupLowprice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMealRise;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;
import com.trekiz.admin.modules.island.entity.ActivityIslandShare;
import com.trekiz.admin.modules.island.entity.ActivityIslandVisaFile;
import com.trekiz.admin.modules.island.input.ActivityIslandInput;
import com.trekiz.admin.modules.island.input.ActivityIslandJsonBeanInput;
import com.trekiz.admin.modules.island.query.ActivityIslandQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupLowpriceService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.ActivityIslandShareService;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityIslandServiceImpl extends BaseService implements ActivityIslandService {

	private static final Logger log = Logger.getLogger(ActivityIslandServiceImpl.class);

	@Autowired
	private ActivityIslandDao activityIslandDao;
	@Autowired
	private ActivityIslandGroupDao activityIslandGroupDao;
	@Autowired
	private ActivityIslandGroupAirlineDao activityIslandGroupAirlineDao;
	@Autowired
	private ActivityIslandGroupRoomDao activityIslandGroupRoomDao;
	@Autowired
	private ActivityIslandGroupMealDao activityIslandGroupMealDao;
	@Autowired
	private ActivityIslandGroupMealRiseDao activityIslandGroupMealRiseDao;
	@Autowired
	private ActivityIslandGroupPriceDao activityIslandGroupPriceDao;
	@Autowired
	private ActivityIslandGroupLowpriceDao activityIslandGroupLowpriceDao;
	@Autowired
	private ActivityIslandVisaFileDao activityIslandVisaFileDao;
	@Autowired
	private ActivityIslandGroupLowpriceService activityIslandGroupLowpriceService;
	@Autowired
	private ActivityIslandShareService activityIslandShareService;
	@Autowired
	private HotelAnnexDao hotelAnnexDao;
	@Autowired
	private ActivityIslandShareDao activityIslandShareDao;
	@Autowired
	private AirlineInfoService airlineInfoService;

	public void save(ActivityIsland activityIsland) {
		super.setOptInfo(activityIsland, BaseService.OPERATION_ADD);
		activityIslandDao.saveObj(activityIsland);
	}
	/**
	 * edit by wangXK
	 * 产品新增
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> save(ActivityIslandInput activityIslandInput,String status) {
		Map<String, String> result = new HashMap<String, String>();
		// 海岛游产品团期表 封装数据
		List<ActivityIslandGroup> allGroupList = new ArrayList<ActivityIslandGroup>();
		List<ActivityIslandVisaFile> allFileList = new ArrayList<ActivityIslandVisaFile>();
		List<ActivityIslandGroupAirline> allAirlineList = new ArrayList<ActivityIslandGroupAirline>();
		List<ActivityIslandGroupRoom> allRoomList = new ArrayList<ActivityIslandGroupRoom>();
		List<ActivityIslandGroupMeal> allMealList = new ArrayList<ActivityIslandGroupMeal>();
		List<ActivityIslandGroupMealRise> allRiseList = new ArrayList<ActivityIslandGroupMealRise>();
		List<ActivityIslandGroupPrice> allPriceList = new ArrayList<ActivityIslandGroupPrice>();
		ActivityIslandGroupPrice activityIslandGroupPrice = new ActivityIslandGroupPrice();
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		//表单验证失败
		if(! activityIslandInput.validateFormInput()) {
			result.put("status", "fail");
			result.put("message", "表单信息验证失败！");
			return result;
		}
		
		//海岛游产品信息保存
		ActivityIsland activityIsland = activityIslandInput.getActivityIsland();
		activityIsland.setWholesalerId(companyId.intValue());
		Set<Department> userDeptList = UserUtils.getDepartmentByJob();
		if (CollectionUtils.isNotEmpty(userDeptList)) {
			activityIsland.setDeptId(userDeptList.iterator().next().getId().intValue());
		}
		super.setOptInfo(activityIsland, BaseService.OPERATION_ADD);
		this.activityIslandDao.saveObj(activityIsland);

		// 海岛游产品团期信息封装
		List<ActivityIslandGroup> groupList = activityIslandInput.getActivityIslandGroupLists();// 从input中取值
		if(CollectionUtils.isNotEmpty(groupList)) {
			for(ActivityIslandGroup group : groupList) {
				group.setActivityIslandUuid(activityIsland.getUuid());
				group.setStatus(status);// 1：上架；2：下架；3：草稿；4：已删除
				super.setOptInfo(group, BaseService.OPERATION_ADD);

				List<ActivityIslandGroupAirline> airlineList = group.getActivityIslandGroupAirlineList();
				if(CollectionUtils.isNotEmpty(airlineList)) {
					for(ActivityIslandGroupAirline airline : airlineList) {
						airline.setActivityIslandUuid(activityIsland.getUuid());// 一级表uuid
						airline.setActivityIslandGroupUuid(group.getUuid());// 二级表uuid
						super.setOptInfo(airline,BaseService.OPERATION_ADD);// 添加默认信息
					}
					allAirlineList.addAll(airlineList);
				}
				
				// 同一游客类型不同舱位等级的UUid赋值进来
				List<List<ActivityIslandGroupPrice>> tourList = group.getJsonActivityIslandGroupPriceList();
				if (CollectionUtils.isNotEmpty(tourList)) {
					for(int i=0;i<tourList.size();i++){
						List<ActivityIslandGroupPrice> spaceLevelList = tourList.get(i);
						for (int j=0; j<spaceLevelList.size(); j++) {
							activityIslandGroupPrice = spaceLevelList.get(j);
							
							//如果团期价格为空，继续添加下一个团期价格
							if(activityIslandGroupPrice.getPrice() == null) {
								continue;
							}
							
							activityIslandGroupPrice.setActivityIslandUuid(activityIsland.getUuid());
							activityIslandGroupPrice.setActivityIslandGroupUuid(group.getUuid());
							activityIslandGroupPrice.setActivityIslandGroupAirlineUuid(airlineList.get(j).getUuid());
							super.setOptInfo(activityIslandGroupPrice,BaseService.OPERATION_ADD);
							
							allPriceList.add(activityIslandGroupPrice);
						}
					}
				}
				
				// (三级表)activity_island_group_room
				List<ActivityIslandGroupRoom> roomList = group.getActivityIslandGroupRoomList();
				if (CollectionUtils.isNotEmpty(roomList)) {
					for (ActivityIslandGroupRoom room : roomList) {
						room.setActivityIslandUuid(activityIsland.getUuid());
						room.setActivityIslandGroupUuid(group.getUuid());
						super.setOptInfo(room,BaseService.OPERATION_ADD);
						
						List<ActivityIslandGroupMeal> mealList = room.getActivityIslandGroupMealList();
						// (四级表)activity_island_group_meal
						if (CollectionUtils.isNotEmpty(mealList)) {
							for (ActivityIslandGroupMeal meal : mealList) {
								meal.setActivityIslandUuid(activityIsland.getUuid());
								meal.setActivityIslandGroupUuid(group.getUuid());
								meal.setActivityIslandGroupRoomUuid(room.getUuid());
								super.setOptInfo(meal,BaseService.OPERATION_ADD);

								// (五级表)activity_island_group_meal_rise
								List<ActivityIslandGroupMealRise> riseList = meal.getActivityIslandGroupMealRiseList();
								if (CollectionUtils.isNotEmpty(riseList)) {
									for (ActivityIslandGroupMealRise rise : riseList) {
										rise.setActivityIslandUuid(activityIsland.getUuid());
										rise.setActivityIslandGroupUuid(group.getUuid());
										rise.setActivityIslandGroupMealUuid(meal.getUuid());
										super.setOptInfo(rise,BaseService.OPERATION_ADD);
									}
									allRiseList.addAll(riseList);
								}
							}
							
							allMealList.addAll(mealList);
						}
					}
					
					allRoomList.addAll(roomList);
				}
			}
			
			allGroupList.addAll(groupList);
		}
		
		//保存的时候，必须有团期，才能有附件的上传，可以写在这个判断里面，如果是更新，就不能写在这个判断里面了 保存签证文件的上传 
		List<ActivityIslandVisaFile> visaFileList = activityIslandInput.transfer2ActivityIslandVisaFile(activityIsland.getUuid(),BaseService.OPERATION_ADD);
		if(CollectionUtils.isNotEmpty(visaFileList)) {
			for(ActivityIslandVisaFile visaFile : visaFileList) {
				visaFile.setActivityIslandUuid(activityIsland.getUuid());
				super.setOptInfo(visaFile,BaseService.OPERATION_ADD);// 添加默认信息
			}
			allFileList.addAll(visaFileList);
		}
		
		// 保存产品分享用户数据
		activityIslandShareService.saveActivityIslandShareData(activityIslandInput.getShareUser(), activityIsland.getCreateBy().longValue(), activityIsland.getUuid());
		
		// 批量保存操作
		this.activityIslandGroupDao.batchSave(allGroupList);
		this.activityIslandGroupAirlineDao.batchSave(allAirlineList);
		this.activityIslandGroupPriceDao.batchSave(allPriceList);
		this.activityIslandGroupRoomDao.batchSave(allRoomList);
		this.activityIslandGroupMealDao.batchSave(allMealList);
		this.activityIslandGroupMealRiseDao.batchSave(allRiseList);
		this.activityIslandVisaFileDao.batchSave(allFileList);
		
		activityIslandGroupLowpriceService.getLowPriceList(activityIsland.getUuid());
		
		// 保存附件信息
		hotelAnnexDao.synDocInfo(activityIsland.getUuid(), HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_PRODSCH, companyId.intValue(), activityIslandInput.getProdSchList());
		hotelAnnexDao.synDocInfo(activityIsland.getUuid(), HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_COSTPROTOCOL, companyId.intValue(), activityIslandInput.getCostProtocolList());
		hotelAnnexDao.synDocInfo(activityIsland.getUuid(), HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_OTHERPROTOCOL, companyId.intValue(), activityIslandInput.getOtherProtocolList());
		
		List[] eachVisaFileList = activityIslandInput.getEachVisaFileList();
		if(CollectionUtils.isNotEmpty(allFileList) && eachVisaFileList!=null){
			for(int k=0;k<allFileList.size();k++){
				hotelAnnexDao.synDocInfo(allFileList.get(k).getUuid(),HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_VISAFILE, companyId.intValue(),eachVisaFileList[k]);
			}
		}
		
		result.put("status", "success");
		result.put("message", "1");
		return result;

	}
	/**
	 * 更新一级、二级表 ，三级及三级以上的需要先删除，在添加修改之后的数据
	 * 更新产品团期
	 * add by WangXK
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> updateAcitivityIsland(ActivityIslandInput activityIslandInput, String flag) {
		String activityIslandUuid = activityIslandInput.getUuid();
		String islandGroupUuid = "";
		Map<String, String> result = new HashMap<String, String>();
		result.put("message", "");
		result.put("error", "");

		if (UserUtils.getCompanyIdForData() == null) {
			result.put("message", "3");
			result.put("error", "批发商信息读取失败！");
			return result;
		}
		// 组装数据
		//ActivityIsland island = activityIslandInput.getActivityIslandFromUpdate(flag);
		ActivityIsland entity = activityIslandDao.getByUuid(activityIslandInput.getUuid());// 从数据库中查询出来
		if (entity != null) {
			entity.setActivityName(activityIslandInput.getActivityName());
			entity.setActivitySerNum(activityIslandInput.getActivitySerNum());
			entity.setMemo(activityIslandInput.getMemo());
		}
		this.setOptInfo(entity, "update");
		activityIslandDao.updateObj(entity);
		//更新2级表
		List<ActivityIslandGroup> islandGrouplist = new ArrayList<ActivityIslandGroup>();
		List<ActivityIslandVisaFile> allFileList = new ArrayList<ActivityIslandVisaFile>();
		//更新3级
		List<ActivityIslandGroupAirline> airlinelist = new ArrayList<ActivityIslandGroupAirline>();
		List<ActivityIslandGroupPrice> pricegrouplist = new ArrayList<ActivityIslandGroupPrice>();
		List<ActivityIslandGroupRoom> roomgrouplist = new ArrayList<ActivityIslandGroupRoom>();
		List<ActivityIslandGroupMeal> mealBaseAllList = new ArrayList<ActivityIslandGroupMeal>();
		List<ActivityIslandGroupMealRise> mealRiseAllList = new ArrayList<ActivityIslandGroupMealRise>();
		ActivityIslandGroupPrice activityIslandGroupPrice = new ActivityIslandGroupPrice();
		if (CollectionUtils.isNotEmpty(activityIslandInput.getActivityIslandGroupLists())) {
			for (ActivityIslandGroup islandGroup : activityIslandInput.getActivityIslandGroupLists()) {// 从页面中获取的明细 更新从数据库中查出来的数据
				if (islandGroup == null) {
					continue;
				}
				//更新二级表是不更新状态的
				ActivityIslandGroup groupentity = activityIslandGroupDao.getByUuid(islandGroup.getUuid());
				groupentity.setGroupCode(islandGroup.getGroupCode());//团期编号
				groupentity.setGroupOpenDate(islandGroup.getGroupOpenDate());//出团日期
				groupentity.setIslandWay(islandGroup.getIslandWay());//上岛方式
				groupentity.setAdvNumber(islandGroup.getAdvNumber());//预收人数
				groupentity.setSinglePrice(islandGroup.getSinglePrice());//单房差
				groupentity.setCurrencyId(islandGroup.getCurrencyId());//单房差币种
				groupentity.setSinglePriceUnit(islandGroup.getSinglePriceUnit());//单房差单位（系统常量：1人2间3晚）
				groupentity.setPriorityDeduction(islandGroup.getPriorityDeduction());//优先扣减（系统常量：1控票数2非控票数）
				groupentity.setFrontMoney(islandGroup.getFrontMoney());//定金
				groupentity.setFrontMoneyCurrencyId(islandGroup.getFrontMoneyCurrencyId());//定金币种
				groupentity.setMemo(islandGroup.getMemo());//备注
				//groupentity.setStatus(islandGroup.getStatus());//状态，少了成本录入锁 lockStatus和预售锁 forcastStatus
				this.setOptInfo(groupentity, "update");
				islandGrouplist.add(groupentity);
				//在保存之前，先将3级表更新为逻辑删除
				islandGroupUuid = groupentity.getUuid();
				activityIslandUuid = groupentity.getActivityIslandUuid();
				
				if(islandGroupUuid!=null &&!"".equals(islandGroupUuid)){
					//逻辑删除航班的舱位等级信息，将DelFlag修改为1
					activityIslandGroupAirlineDao.updateByActivityIslandGroupUUid(islandGroupUuid);
					activityIslandGroupRoomDao.updateByActivityIslandGroupUUid(islandGroupUuid);
					activityIslandGroupMealDao.updateByActivityIslandGroupUUid(islandGroupUuid);
					activityIslandGroupMealRiseDao.updateByActivityIslandGroupUUid(islandGroupUuid);
					activityIslandGroupPriceDao.updateByActivityIslandGroupUUid(islandGroupUuid);
					activityIslandGroupLowpriceDao.updateByActivityIslandGroupUUid(islandGroupUuid);
					
				}
				// (三级表)activity_island_group_airline
				List<ActivityIslandGroupAirline> airlineList = islandGroup.getActivityIslandGroupAirlineList();// 从input中取值
				if (CollectionUtils.isNotEmpty(airlineList)) {// 非空验证
					for (ActivityIslandGroupAirline airline : airlineList) {// 遍历
						airline.setActivityIslandUuid(activityIslandUuid);// 一级表uuid
						airline.setActivityIslandGroupUuid(islandGroupUuid);// 二级表uuid
						super.setOptInfo(airline,BaseService.OPERATION_ADD);// 添加默认信息
						airlinelist.add(airline);// 统一保存到allAirlineList集合中
					}
				}
				
				// 同一游客类型不同舱位等级的UUid赋值进来
				List<List<ActivityIslandGroupPrice>> tourList = islandGroup.getJsonActivityIslandGroupPriceList();
				if (CollectionUtils.isNotEmpty(tourList)) {
					for(int i=0;i<tourList.size();i++){
						List<ActivityIslandGroupPrice> spaceLevelList = tourList.get(i);
						for (int j=0;j<spaceLevelList.size();j++) {
							activityIslandGroupPrice = spaceLevelList.get(j);
							if(activityIslandGroupPrice.getPrice()==null){
								continue;
							}
							activityIslandGroupPrice.setActivityIslandUuid(activityIslandUuid);
							activityIslandGroupPrice.setActivityIslandGroupUuid(islandGroup.getUuid());
							activityIslandGroupPrice.setActivityIslandGroupAirlineUuid(airlinelist.get(j).getUuid());
							super.setOptInfo(activityIslandGroupPrice,BaseService.OPERATION_ADD);// 添加默认信息
							pricegrouplist.add(activityIslandGroupPrice);// 统一保存到allPriceList集合中
						}
					}
				}
				// (三级表)activity_island_group_room
				List<ActivityIslandGroupRoom> roomList = islandGroup.getActivityIslandGroupRoomList();// 从input中取值
				if (CollectionUtils.isNotEmpty(roomList)) {// 非空验证
					for (ActivityIslandGroupRoom room : roomList) {// 遍历
						room.setActivityIslandUuid(activityIslandUuid);// 一级表uuid
						room.setActivityIslandGroupUuid(islandGroupUuid);// 二级表uuid
						super.setOptInfo(room,BaseService.OPERATION_ADD);// 添加默认信息
						
						List<ActivityIslandGroupMeal> mealList = room.getActivityIslandGroupMealList();// 从input中取值
						// (四级表)activity_island_group_meal
						if (CollectionUtils.isNotEmpty(mealList)) {// 非空验证
							for (ActivityIslandGroupMeal meal : mealList) {// 遍历
								meal.setActivityIslandUuid(activityIslandUuid);// 一级表uuid
								meal.setActivityIslandGroupUuid(islandGroupUuid);// 二级表uuid
								meal.setActivityIslandGroupRoomUuid(room.getUuid());// 三级表uuid
								
								super.setOptInfo(meal,BaseService.OPERATION_ADD);// 添加默认信息
								mealBaseAllList.add(meal);// 统一保存到allMealList集合中

								// (五级表)activity_island_group_meal_rise
								List<ActivityIslandGroupMealRise> riseList = meal.getActivityIslandGroupMealRiseList();// 从input中取值
								if (CollectionUtils.isNotEmpty(riseList)) {// 非空验证
									for (ActivityIslandGroupMealRise rise : riseList) {
										rise.setActivityIslandUuid(activityIslandUuid);
										rise.setActivityIslandGroupUuid(islandGroupUuid);
										rise.setActivityIslandGroupMealUuid(meal.getUuid());
										super.setOptInfo(rise,BaseService.OPERATION_ADD);// 添加默认信息
										mealRiseAllList.add(rise);// 统一保存到allRiseList集合中
									}
								}
							}
						}
						
						roomgrouplist.add(room);// 统一保存到allRoomList集合中
					}
				}

			}// 以上部分是不存团期的部分
			
			// 保存产品分享用户数据
			//activityIslandShareService.saveActivityIslandShareData(activityIslandInput.getShareUser(), activityIsland.getCreateBy().longValue(), activityIsland.getUuid());
			// 批量保存操作
			if (CollectionUtils.isNotEmpty(islandGrouplist)) {// 二级表是更新，3级及以上的表时逻辑删除
				this.activityIslandGroupDao.batchUpdate(islandGrouplist);
				if (CollectionUtils.isNotEmpty(airlinelist)) {// 航班不为空
					this.activityIslandGroupAirlineDao.batchSave(airlinelist);
					if (CollectionUtils.isNotEmpty(pricegrouplist)) {
						this.activityIslandGroupPriceDao.batchSave(pricegrouplist);
						//allLowpriceList = this.getLowpriceByPriceList(allPriceList,BaseService.OPERATION_ADD);// 装载数据
						//this.activityIslandGroupLowpriceDao.batchSave(allLowpriceList);
						activityIslandGroupLowpriceService.getLowPriceList(activityIslandUuid);
					}
				}
				if (CollectionUtils.isNotEmpty(roomgrouplist)) {
					this.activityIslandGroupRoomDao.batchSave(roomgrouplist);
				}
				if (CollectionUtils.isNotEmpty(mealBaseAllList)) {// 基础餐型不为空
					this.activityIslandGroupMealDao.batchSave(mealBaseAllList);
					if (CollectionUtils.isNotEmpty(mealRiseAllList)) {
						this.activityIslandGroupMealRiseDao.batchSave(mealRiseAllList);
					}
				}

			}
			
		}
		// 保存产品分享用户数据
		//先删除在修改
		List<ActivityIslandShare> allShareList=new ArrayList<ActivityIslandShare>();
		List<ActivityIslandShare> delShareList=activityIslandShareDao.findShareUserByIsland(activityIslandUuid);
		if(CollectionUtils.isNotEmpty(delShareList)){
			for(ActivityIslandShare list:delShareList){
				list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			}
			activityIslandShareDao.batchUpdate(delShareList);
		}
		List<ActivityIslandShare> upShareList=activityIslandInput.transfer2ActivityIslandShare();
		for(ActivityIslandShare temp:upShareList){
			temp.setShareUser(Long.parseLong(activityIslandDao.getByUuid(activityIslandUuid).getCreateBy().toString()));
			temp.setActivityIslandUuid(activityIslandUuid);
			super.setOptInfo(temp, OPERATION_ADD);
			allShareList.add(temp);
		}
		if(CollectionUtils.isNotEmpty(upShareList)){
			this.activityIslandShareDao.batchSave(allShareList);
		}
		//保存附件信息的数据写在这里,更新
		List<ActivityIslandVisaFile> visaFileList = activityIslandInput.transfer2ActivityIslandVisaFile(activityIslandUuid,BaseService.OPERATION_ADD);
		if(CollectionUtils.isNotEmpty(visaFileList)){
			//先更新状态为1  updateByActivityIslandGroupUUid
			activityIslandVisaFileDao.updateByActivityIslandUUid(activityIslandInput.getUuid());
			for(ActivityIslandVisaFile visaFile: visaFileList){
				if(visaFile==null){
					continue;
				}
				visaFile.setActivityIslandUuid(activityIslandInput.getUuid());
				super.setOptInfo(visaFile,BaseService.OPERATION_ADD);// 添加默认信息
				allFileList.add(visaFile);
			}
			
			if (CollectionUtils.isNotEmpty(allFileList)) {
				this.activityIslandVisaFileDao.batchSave(allFileList);
			}
		}
		
		List[] eachVisaFileList = activityIslandInput.getEachVisaFileList();
		if(CollectionUtils.isNotEmpty(allFileList)&&eachVisaFileList!=null){
			for(int k=0;k<allFileList.size();k++){
				hotelAnnexDao.synDocInfo(allFileList.get(k).getUuid(),HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_VISAFILE,
						UserUtils.getCompanyIdForData().intValue(),eachVisaFileList[k]);	
			}
		}
		hotelAnnexDao.synDocInfo(activityIslandInput.getUuid(),HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_PRODSCH,
				UserUtils.getCompanyIdForData().intValue(),activityIslandInput.getProdSchList());
		hotelAnnexDao.synDocInfo(activityIslandInput.getUuid(),HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_COSTPROTOCOL,
				UserUtils.getCompanyIdForData().intValue(),activityIslandInput.getCostProtocolList());
		hotelAnnexDao.synDocInfo(activityIslandInput.getUuid(),HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_OTHERPROTOCOL,
						UserUtils.getCompanyIdForData().intValue(),activityIslandInput.getOtherProtocolList());
		
		result.put("status", "success");
		result.put("message", "2");	
		return result;
	}
	/**
	 * 只有一个团期
	 * 团期新增并保存
	 */
	public Map<String,String> saveActivityIslandGroup(ActivityIslandInput activityIslandInput){
		Map<String, String> result = new HashMap<String, String>();

		// 海岛游产品团期表 封装数据
		List<ActivityIslandGroup> allGroupList = new ArrayList<ActivityIslandGroup>();
		List<ActivityIslandGroupAirline> allAirlineList = new ArrayList<ActivityIslandGroupAirline>();
		List<ActivityIslandGroupRoom> allRoomList = new ArrayList<ActivityIslandGroupRoom>();
		List<ActivityIslandGroupMeal> allMealList = new ArrayList<ActivityIslandGroupMeal>();
		List<ActivityIslandGroupMealRise> allRiseList = new ArrayList<ActivityIslandGroupMealRise>();
		List<ActivityIslandGroupPrice> allPriceList = new ArrayList<ActivityIslandGroupPrice>();
		ActivityIslandGroupPrice activityIslandGroupPrice = new ActivityIslandGroupPrice();
		if (UserUtils.getCompanyIdForData() != null) {
			if (activityIslandInput.validateFormInput()) {
				
				String activityIslandUuid = "";
				// (二级表)activity_island_group
				List<ActivityIslandGroup> groupList = activityIslandInput.getActivityIslandGroupLists();// 从input中取值
				if (CollectionUtils.isNotEmpty(groupList)) {
					for (ActivityIslandGroup group : groupList) {
						activityIslandUuid = group.getActivityIslandUuid();
						//group.setActivityIslandUuid(group.getActivityIslandUuid());// 一级表uuid
						
						super.setOptInfo(group, BaseService.OPERATION_ADD);// 添加默认信息
						allGroupList.add(group);// 统一保存到allGroupList集合中
						
						// (三级表)activity_island_group_airline
						List<ActivityIslandGroupAirline> airlineList = group.getActivityIslandGroupAirlineList();// 从input中取值
						if (CollectionUtils.isNotEmpty(airlineList)) {// 非空验证
							for (ActivityIslandGroupAirline airline : airlineList) {// 遍历
								airline.setActivityIslandUuid(activityIslandUuid);// 一级表uuid
								airline.setActivityIslandGroupUuid(group.getUuid());// 二级表uuid
								super.setOptInfo(airline,BaseService.OPERATION_ADD);// 添加默认信息
								allAirlineList.add(airline);// 统一保存到allAirlineList集合中
							}
						}
						
						// 同一游客类型不同舱位等级的UUid赋值进来
						List<List<ActivityIslandGroupPrice>> tourList = group.getJsonActivityIslandGroupPriceList();
						if (CollectionUtils.isNotEmpty(tourList)) {
							for(int i=0;i<tourList.size();i++){
								List<ActivityIslandGroupPrice> spaceLevelList = tourList.get(i);
								for (int j=0;j<spaceLevelList.size();j++) {
									activityIslandGroupPrice = spaceLevelList.get(j);
									if(activityIslandGroupPrice.getPrice()==null){
										continue;
									}
									activityIslandGroupPrice.setActivityIslandUuid(activityIslandUuid);
									activityIslandGroupPrice.setActivityIslandGroupUuid(group.getUuid());
									activityIslandGroupPrice.setActivityIslandGroupAirlineUuid(allAirlineList.get(j).getUuid());
									super.setOptInfo(activityIslandGroupPrice,BaseService.OPERATION_ADD);// 添加默认信息
									allPriceList.add(activityIslandGroupPrice);// 统一保存到allPriceList集合中
								}
							}
						}
						// (三级表)activity_island_group_room
						List<ActivityIslandGroupRoom> roomList = group
								.getActivityIslandGroupRoomList();// 从input中取值
						if (CollectionUtils.isNotEmpty(roomList)) {// 非空验证
							for (ActivityIslandGroupRoom room : roomList) {// 遍历
								room.setActivityIslandUuid(activityIslandUuid);// 一级表uuid
								room.setActivityIslandGroupUuid(group.getUuid());// 二级表uuid
								super.setOptInfo(room,BaseService.OPERATION_ADD);// 添加默认信息
								
								List<ActivityIslandGroupMeal> mealList = room.getActivityIslandGroupMealList();// 从input中取值
								// (四级表)activity_island_group_meal
								if (CollectionUtils.isNotEmpty(mealList)) {// 非空验证
									for (ActivityIslandGroupMeal meal : mealList) {// 遍历
										meal.setActivityIslandUuid(activityIslandUuid);// 一级表uuid
										meal.setActivityIslandGroupUuid(group.getUuid());// 二级表uuid
										meal.setActivityIslandGroupRoomUuid(room.getUuid());// 三级表uuid
										
										super.setOptInfo(meal,BaseService.OPERATION_ADD);// 添加默认信息
										allMealList.add(meal);// 统一保存到allMealList集合中

										// (五级表)activity_island_group_meal_rise
										List<ActivityIslandGroupMealRise> riseList = meal.getActivityIslandGroupMealRiseList();// 从input中取值
										if (CollectionUtils.isNotEmpty(riseList)) {// 非空验证
											for (ActivityIslandGroupMealRise rise : riseList) {
												rise.setActivityIslandUuid(activityIslandUuid);
												rise.setActivityIslandGroupUuid(group.getUuid());
												rise.setActivityIslandGroupMealUuid(meal.getUuid());
												super.setOptInfo(rise,BaseService.OPERATION_ADD);// 添加默认信息
												allRiseList.add(rise);// 统一保存到allRiseList集合中
											}
										}
									}
								}
								
								allRoomList.add(room);// 统一保存到allRoomList集合中
							}
						}

					}
					// 保存产品分享用户数据
					//activityIslandShareService.saveActivityIslandShareData(activityIslandInput.getShareUser(), activityIsland.getCreateBy().longValue(), activityIsland.getUuid());
					// 批量保存操作
					if (CollectionUtils.isNotEmpty(allGroupList)) {// 团期表不为空
						this.activityIslandGroupDao.batchSave(allGroupList);
						if (CollectionUtils.isNotEmpty(allAirlineList)) {// 航班不为空
							this.activityIslandGroupAirlineDao.batchSave(allAirlineList);
							if (CollectionUtils.isNotEmpty(allPriceList)) {
								this.activityIslandGroupPriceDao.batchSave(allPriceList);
								//allLowpriceList = this.getLowpriceByPriceList(allPriceList,BaseService.OPERATION_ADD);// 装载数据
								//this.activityIslandGroupLowpriceDao.batchSave(allLowpriceList);
								activityIslandGroupLowpriceService.getLowPriceList(activityIslandUuid);
							}
						}
						if (CollectionUtils.isNotEmpty(allRoomList)) {
							this.activityIslandGroupRoomDao.batchSave(allRoomList);
						}
						if (CollectionUtils.isNotEmpty(allMealList)) {// 基础餐型不为空
							this.activityIslandGroupMealDao.batchSave(allMealList);
							if (CollectionUtils.isNotEmpty(allRiseList)) {
								this.activityIslandGroupMealRiseDao.batchSave(allRiseList);
							}
						}

					}
					result.put("status", "success");
					result.put("message", "1");
					return result;
				} else {
					result.put("status", "fail");
					result.put("message", "海岛游产品团期表没有数据！ ");
					return result;
				}

			} else {
				result.put("status", "fail");
				result.put("message", "表单信息验证失败！");
				return result;
			}
		} else {
			result.put("status", "fail");
			result.put("message", "批发商信息读取失败！");
			return result;
		}
	}
	/**
	 * add by wangxk
	 * 团期修改 
	 * @return
	 */
	public Map<String,String> updateActivityIslandGroup(ActivityIslandGroup islandGroup,String flag){
		Map<String, String> result = new HashMap<String, String>();
		result.put("message", "");
		result.put("error", "");

		if (UserUtils.getCompanyIdForData() == null) {
			result.put("message", "3");
			result.put("error", "批发商信息读取失败！");
			return result;
		}
		
		//更新2级表 一条数据
		List<ActivityIslandGroupAirline> airlinelist = new ArrayList<ActivityIslandGroupAirline>();
		List<ActivityIslandGroupPrice> allPriceList = new ArrayList<ActivityIslandGroupPrice>();
		List<ActivityIslandGroupRoom> roomgrouplist = new ArrayList<ActivityIslandGroupRoom>();
		List<ActivityIslandGroupMeal> mealBaseAllList = new ArrayList<ActivityIslandGroupMeal>();
		List<ActivityIslandGroupMealRise> mealRiseAllList = new ArrayList<ActivityIslandGroupMealRise>();
		ActivityIslandGroupPrice activityIslandGroupPrice = new ActivityIslandGroupPrice();
		
		if (islandGroup == null) {
			result.put("message", "3");
			result.put("error", "读取团期信息为空！");
			return result;
		}
		
		ActivityIslandGroup groupentity = activityIslandGroupDao.getByUuid(islandGroup.getUuid());//二级表修改
		
		if(groupentity==null){
			result.put("status", "fail");
			result.put("message", "海岛游产品团期表没有查询到要修改的团期的数据！需要修改的团期的Uuid为：  " + islandGroup.getUuid());
			return result;
		}
		groupentity.setGroupCode(islandGroup.getGroupCode());//团期编号
		groupentity.setGroupOpenDate(islandGroup.getGroupOpenDate());//出团日期
		groupentity.setIslandWay(islandGroup.getIslandWay());//上岛方式
		groupentity.setAdvNumber(islandGroup.getAdvNumber());//预收人数
		groupentity.setSinglePrice(islandGroup.getSinglePrice());//单房差
		groupentity.setCurrencyId(islandGroup.getCurrencyId());//单房差币种
		groupentity.setSinglePriceUnit(islandGroup.getSinglePriceUnit());//单房差单位（系统常量：1人2间3晚）
		groupentity.setPriorityDeduction(islandGroup.getPriorityDeduction());//优先扣减（系统常量：1控票数2非控票数）
		groupentity.setFrontMoney(islandGroup.getFrontMoney());//定金
		groupentity.setFrontMoneyCurrencyId(islandGroup.getFrontMoneyCurrencyId());//定金币种
		groupentity.setMemo(islandGroup.getMemo());//备注
		groupentity.setStatus(islandGroup.getStatus());//状态，少了成本录入锁 lockStatus和预售锁 forcastStatus
		this.setOptInfo(groupentity, BaseService.OPERATION_UPDATE);
		
		//三级表 新增 参考航班表(舱位等级为单位) ,先根据团期的UUid更新之前的表数据为逻辑删除
		String activityIslandUuid = groupentity.getActivityIslandUuid();
		// 在进行修改之前，先根据activityIslandGroup的UUid删除之前的数据
		String islandGroupUuid = islandGroup.getUuid();
		
		if(islandGroupUuid!=null &&!"".equals(islandGroupUuid)){
			//逻辑删除航班的舱位等级信息，将DelFlag修改为1
			activityIslandGroupAirlineDao.updateByActivityIslandGroupUUid(islandGroupUuid);
			activityIslandGroupRoomDao.updateByActivityIslandGroupUUid(islandGroupUuid);
			activityIslandGroupMealDao.updateByActivityIslandGroupUUid(islandGroupUuid);
			activityIslandGroupMealRiseDao.updateByActivityIslandGroupUUid(islandGroupUuid);
			activityIslandGroupPriceDao.updateByActivityIslandGroupUUid(islandGroupUuid);
			activityIslandGroupLowpriceDao.updateByActivityIslandGroupUUid(islandGroupUuid);
			
		}
		
		List<ActivityIslandGroupAirline> airlineList = islandGroup.getActivityIslandGroupAirlineList();//从解析的json串中取值
		if (CollectionUtils.isNotEmpty(airlineList)) {// 非空验证
			for (ActivityIslandGroupAirline airline : airlineList) {// 遍历
				airline.setActivityIslandUuid(activityIslandUuid);// 一级表UUid
				airline.setActivityIslandGroupUuid(islandGroup.getUuid());// 二级表UUid
				super.setOptInfo(airline,BaseService.OPERATION_ADD);// 添加默认信息
				airlinelist.add(airline);// 统一保存到allAirlineList集合中
				}
		}
		
		// 同一游客类型不同舱位等级的UUid赋值进来
		List<List<ActivityIslandGroupPrice>> tourList = islandGroup.getJsonActivityIslandGroupPriceList();
		if (CollectionUtils.isNotEmpty(tourList)) {
			for(int i=0;i<tourList.size();i++){
				List<ActivityIslandGroupPrice> spaceLevelList = tourList.get(i);
				for (int j=0;j<spaceLevelList.size();j++) {
					activityIslandGroupPrice = spaceLevelList.get(j);
					if(activityIslandGroupPrice.getPrice()==null){
						continue;
					}
					activityIslandGroupPrice.setActivityIslandUuid(activityIslandUuid);
					activityIslandGroupPrice.setActivityIslandGroupUuid(islandGroup.getUuid());
					activityIslandGroupPrice.setActivityIslandGroupAirlineUuid(airlinelist.get(j).getUuid());
					super.setOptInfo(activityIslandGroupPrice,BaseService.OPERATION_ADD);// 添加默认信息
					allPriceList.add(activityIslandGroupPrice);// 统一保存到allPriceList集合中
				}
			}
		}
		// (三级表)activity_island_group_room
		List<ActivityIslandGroupRoom> roomList = islandGroup.getActivityIslandGroupRoomList();// 从json中取值
		if (CollectionUtils.isNotEmpty(roomList)) {// 非空验证
			for (ActivityIslandGroupRoom room : roomList) {// 遍历
				room.setActivityIslandUuid(activityIslandUuid);// 一级表uuid
				room.setActivityIslandGroupUuid(islandGroup.getUuid());// 二级表uuid
				super.setOptInfo(room,BaseService.OPERATION_ADD);// 添加默认信息
				
				List<ActivityIslandGroupMeal> mealList = room.getActivityIslandGroupMealList();// 从json中取值
				// (四级表)activity_island_group_meal
				if (CollectionUtils.isNotEmpty(mealList)) {// 非空验证
					for (ActivityIslandGroupMeal meal : mealList) {// 遍历
						meal.setActivityIslandUuid(activityIslandUuid);// 一级表uuid
						meal.setActivityIslandGroupUuid(islandGroup.getUuid());// 二级表uuid
						meal.setActivityIslandGroupRoomUuid(room.getUuid());// 三级表uuid
						
						super.setOptInfo(meal,BaseService.OPERATION_ADD);// 添加默认信息
						mealBaseAllList.add(meal);// 统一保存到allMealList集合中

						// (五级表)activity_island_group_meal_rise
						List<ActivityIslandGroupMealRise> riseList = meal.getActivityIslandGroupMealRiseList();// 从input中取值
						if (CollectionUtils.isNotEmpty(riseList)) {// 非空验证
							for (ActivityIslandGroupMealRise rise : riseList) {
								rise.setActivityIslandUuid(activityIslandUuid);
								rise.setActivityIslandGroupUuid(islandGroup.getUuid());
								rise.setActivityIslandGroupMealUuid(meal.getUuid());
								super.setOptInfo(rise,BaseService.OPERATION_ADD);// 添加默认信息
								mealRiseAllList.add(rise);// 统一保存到allRiseList集合中
							}
						}
					}
				}

				super.setOptInfo(room,BaseService.OPERATION_ADD);// 添加默认信息
				roomgrouplist.add(room);// 统一保存到allRoomList集合中
			}
		}
		
		// 批量更新
		if(groupentity!=null){	
			activityIslandGroupDao.updateObj(groupentity);         //二级表
			if (CollectionUtils.isNotEmpty(airlinelist)) {// 航班不为空
				this.activityIslandGroupAirlineDao.batchSave(airlinelist);
				if (CollectionUtils.isNotEmpty(allPriceList)) {
					this.activityIslandGroupPriceDao.batchSave(allPriceList);
					//allLowpriceList = this.getLowpriceByPriceList(pricegrouplist,BaseService.OPERATION_ADD);// 装载数据
					//this.activityIslandGroupLowpriceDao.batchSave(allLowpriceList);
					activityIslandGroupLowpriceService.getLowPriceList(activityIslandUuid);
				}
			}
			if (CollectionUtils.isNotEmpty(roomgrouplist)) {
				this.activityIslandGroupRoomDao.batchSave(roomgrouplist);
			}
			if (CollectionUtils.isNotEmpty(mealBaseAllList)) {// 基础餐型不为空
				this.activityIslandGroupMealDao.batchSave(mealBaseAllList);
				if (CollectionUtils.isNotEmpty(mealRiseAllList)) {
					this.activityIslandGroupMealRiseDao.batchSave(mealRiseAllList);
				}
			}
			result.put("status", "success");
			result.put("message", "2");
		}
		
		return result;
	}
	
	public String getJsonStringByActivityIslandGroupUuid(String activityIslandGroupUuid){
		String roomuuid = "";
		String mealuuid = "";
		String airlineuuid = "";
		ActivityIslandGroupRoom room = new ActivityIslandGroupRoom();
		ActivityIslandGroupMeal meal = new ActivityIslandGroupMeal();
		ActivityIslandGroupAirline airline = new ActivityIslandGroupAirline();
		ActivityIslandJsonBeanInput jsonInput = new ActivityIslandJsonBeanInput();
		List<ActivityIslandGroupPrice> priceList = new ArrayList<ActivityIslandGroupPrice>();//四级表
		List<ActivityIslandGroupMeal> mealList = new ArrayList<ActivityIslandGroupMeal>();//四级表
		List<ActivityIslandGroupMealRise> riseList = new ArrayList<ActivityIslandGroupMealRise>();//五级表
		ActivityIslandGroup islandGroup = activityIslandGroupDao.getByUuid(activityIslandGroupUuid);//二级表
		List<ActivityIslandGroupRoom> roomList = activityIslandGroupRoomDao.getRoomListByGroupUuid(activityIslandGroupUuid);//三级表
		List<ActivityIslandGroupAirline> airlist = activityIslandGroupAirlineDao.getByActivityIslandGroupUuid(activityIslandGroupUuid);//三级表
		
		if(roomList!=null && roomList.size()>0){
			for(int i=0;i<roomList.size();i++){
				mealList = new ArrayList<ActivityIslandGroupMeal>();
				room = roomList.get(i);
				roomuuid = room.getUuid();
				mealList = activityIslandGroupMealDao.getByactivityIslandGroupRoomUuid(roomuuid);
				if(mealList!=null && mealList.size()>0){
					for(int j=0;j<mealList.size();j++){
						meal = mealList.get(j);
						mealuuid = meal.getUuid();
						riseList = activityIslandGroupMealRiseDao.getbyGroupMealUuid(mealuuid);
						meal.setActivityIslandGroupMealRiseList(riseList);
					}
				}
				room.setActivityIslandGroupMealList(mealList);
			}
		}
		if(airlist!=null && airlist.size()>0){
			for(int i=0;i<airlist.size();i++){
				airline = airlist.get(i);
				airlineuuid = airline.getUuid();
				priceList = activityIslandGroupPriceDao.getGroupPriceByAirlineUuid(airlineuuid);
				airline.setActivityIslandGroupPriceList(priceList);
			}
		}
		islandGroup.setActivityIslandGroupRoomList(roomList);
		islandGroup.setActivityIslandGroupAirlineList(airlist);
		
		jsonInput.initActivityIslandJsonInput(islandGroup);
		String jsonStr = JSON.toJSONString(jsonInput);
		return jsonStr;
	}
	public void update(ActivityIsland activityIsland) {
		super.setOptInfo(activityIsland, BaseService.OPERATION_UPDATE);
		activityIslandDao.updateObj(activityIsland);
	}

	public ActivityIsland getById(java.lang.Integer value) {
		return activityIslandDao.getById(value);
	}

	public void removeById(java.lang.Integer value) {
		ActivityIsland obj = activityIslandDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}

	public Page<ActivityIsland> find(Page<ActivityIsland> page,
			ActivityIslandQuery activityIslandQuery) {
		DetachedCriteria dc = activityIslandDao.createDetachedCriteria();

		if (activityIslandQuery.getId() != null) {
			dc.add(Restrictions.eq("id", activityIslandQuery.getId()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getUuid())) {
			dc.add(Restrictions.eq("uuid", activityIslandQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getActivitySerNum())) {
			dc.add(Restrictions.eq("activitySerNum",activityIslandQuery.getActivitySerNum()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getActivityName())) {
			dc.add(Restrictions.eq("activityName",activityIslandQuery.getActivityName()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getCountry())) {
			dc.add(Restrictions.eq("country", activityIslandQuery.getCountry()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getIslandUuid())) {
			dc.add(Restrictions.eq("islandUuid",activityIslandQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getHotelUuid())) {
			dc.add(Restrictions.eq("hotelUuid",activityIslandQuery.getHotelUuid()));
		}
		if (activityIslandQuery.getCurrencyId() != null) {
			dc.add(Restrictions.eq("currencyId",activityIslandQuery.getCurrencyId()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getMemo())) {
			dc.add(Restrictions.eq("memo", activityIslandQuery.getMemo()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getStatus())) {
			dc.add(Restrictions.eq("status", activityIslandQuery.getStatus()));
		}
		if (activityIslandQuery.getWholesalerId() != null) {
			dc.add(Restrictions.eq("wholesalerId",activityIslandQuery.getWholesalerId()));
		}
		if (activityIslandQuery.getDeptId() != null) {
			dc.add(Restrictions.eq("deptId", activityIslandQuery.getDeptId()));
		}
		if (activityIslandQuery.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy",activityIslandQuery.getCreateBy()));
		}
		if (activityIslandQuery.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate",activityIslandQuery.getCreateDate()));
		}
		if (activityIslandQuery.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy",activityIslandQuery.getUpdateBy()));
		}
		if (activityIslandQuery.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate",activityIslandQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getDelFlag())) {
			dc.add(Restrictions.eq("delFlag", activityIslandQuery.getDelFlag()));
		}

		// dc.addOrder(Order.desc("id"));
		return activityIslandDao.find(page, dc);
	}

	public List<ActivityIsland> find(ActivityIslandQuery activityIslandQuery) {
		DetachedCriteria dc = activityIslandDao.createDetachedCriteria();

		if (activityIslandQuery.getId() != null) {
			dc.add(Restrictions.eq("id", activityIslandQuery.getId()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getUuid())) {
			dc.add(Restrictions.eq("uuid", activityIslandQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getActivitySerNum())) {
			dc.add(Restrictions.eq("activitySerNum",activityIslandQuery.getActivitySerNum()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getActivityName())) {
			dc.add(Restrictions.eq("activityName",activityIslandQuery.getActivityName()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getCountry())) {
			dc.add(Restrictions.eq("country", activityIslandQuery.getCountry()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getIslandUuid())) {
			dc.add(Restrictions.eq("islandUuid",activityIslandQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getHotelUuid())) {
			dc.add(Restrictions.eq("hotelUuid",activityIslandQuery.getHotelUuid()));
		}
		if (activityIslandQuery.getCurrencyId() != null) {
			dc.add(Restrictions.eq("currencyId",activityIslandQuery.getCurrencyId()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getMemo())) {
			dc.add(Restrictions.eq("memo", activityIslandQuery.getMemo()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getStatus())) {
			dc.add(Restrictions.eq("status", activityIslandQuery.getStatus()));
		}
		if (activityIslandQuery.getWholesalerId() != null) {
			dc.add(Restrictions.eq("wholesalerId",activityIslandQuery.getWholesalerId()));
		}
		if (activityIslandQuery.getDeptId() != null) {
			dc.add(Restrictions.eq("deptId", activityIslandQuery.getDeptId()));
		}
		if (activityIslandQuery.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy",activityIslandQuery.getCreateBy()));
		}
		if (activityIslandQuery.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate",activityIslandQuery.getCreateDate()));
		}
		if (activityIslandQuery.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy",activityIslandQuery.getUpdateBy()));
		}
		if (activityIslandQuery.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate",activityIslandQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandQuery.getDelFlag())) {
			dc.add(Restrictions.eq("delFlag", activityIslandQuery.getDelFlag()));
		}

		// dc.addOrder(Order.desc("id"));
		return activityIslandDao.find(dc);
	}

	public ActivityIsland getByUuid(String uuid) {
		return activityIslandDao.getByUuid(uuid);
	}

	public void removeByUuid(String uuid) {
		ActivityIsland activityIsland = getByUuid(uuid);
		activityIsland.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIsland);
	}

	/**
	 * 列表方法
	 */
	@Override
	public Page<Map<String, Object>> getlandProductList(
			ActivityIslandQuery query, HttpServletRequest request,
			HttpServletResponse response) {
		String orderBy = request.getParameter("orderBy");
		String status = request.getParameter("status");
		String showType = request.getParameter("showType");// 1是团期列表，2是产品列表
		Long companyId = UserUtils.getUser().getCompany().getId();
		Long userId = UserUtils.getUser().getId();
		StringBuffer sb = new StringBuffer();
		if ("2".equals(showType)) {
			sb.append("SELECT ai.uuid AS uuid,ai.activityName AS activityName,ai.country AS country,ai.island_uuid AS island_uuid,ai.hotel_uuid AS hotel_uuid,"
			+ "(SELECT hs.value FROM hotel_star hs,hotel h where hs.uuid = h.star and h.uuid = ai.hotel_uuid and h.delFlag = '0' and hs.delFlag = '0') as hotel_star,"
			+ "GROUP_CONCAT(aig.uuid) as aigUuids,ai.currency_id as currency_id "
			+ "FROM activity_island ai  LEFT JOIN activity_island_group_lowprice aigl on ai.uuid = aigl.activity_island_uuid and aigl.delFlag='0',activity_island_group aig "
			+ "WHERE ai.uuid = aig.activity_island_uuid "
			+ "AND ai.delFlag = '0' "
			+ "AND aig.delFlag = '0' "
			+ "AND ai.wholesaler_id = "
			+ companyId
			+ " and (ai.createBy = "
			+ userId
			+ " OR EXISTS (select 1 from activity_island_share ais "
			+ "where ais.activity_island_uuid = ai.uuid and ais.delFlag='0' and ais.accept_share_user="
			+ userId + " )) ");
		} else {// 默认查询团期列表数据
			sb.append("select aig.uuid AS uuid,aig.groupCode AS groupCode,aig.groupOpenDate AS groupOpenDate,ai.activityName as activityName, "
					+ "ai.island_uuid as island_uuid,ai.hotel_uuid as hotel_uuid,ai.currency_id as currency, "
					+ "(select hs.value from hotel_star hs,hotel h "
					+ "where h.uuid = ai.hotel_uuid and h.star = hs.uuid and h.delFlag = '0' and hs.delFlag = '0') as hotel_star, "
					+ "aig.island_way AS island_way,aig.advNumber AS advNumber,aig.currency_id AS currency_id,aig.singlePrice AS singlePrice,aig.front_money AS front_money, "
					+ "aig.front_money_currency_id AS front_money_currency_id,aig.status AS status, "
					+ "(select sum(io.orderPersonNum) from island_order io where io.activity_island_uuid = aig.activity_island_uuid "
					+ "and io.activity_island_group_uuid = aig.uuid and io.orderStatus = '1') as total_num,ai.uuid as ai_uuid, "
					+ "aig.createDate as createDate,aig.updateDate as updateDate, "
                    + "(select sum(remNumber) from activity_island_group_airline aigasum where " 
                    + "aigasum.activity_island_group_uuid = aig.uuid and aigasum.delFlag='0') as sumRemnum "
					+ "from activity_island ai,activity_island_group aig,activity_island_group_price aigp,activity_island_group_airline aiga "
					+ "where ai.uuid = aig.activity_island_uuid and aig.uuid = aigp.activity_island_group_uuid "
					+ "and ai.uuid = aigp.activity_island_uuid and aiga.activity_island_uuid = ai.uuid and aiga.activity_island_group_uuid = aig.uuid "
					+ "and aiga.delFlag = '0' and ai.delFlag = '0' and aig.delFlag = '0' and aigp.delFlag = '0' "
					+ "AND ai.wholesaler_id = "
					+ companyId
					+ " and (ai.createBy = "
					+ userId
					+ " OR EXISTS (select 1 from activity_island_share ais "
					+ "where ais.activity_island_uuid = ai.uuid and ais.delFlag='0' and ais.accept_share_user= "
					+ userId + " )) ");
		}
		// 查询条件
		// 产品状态
		if (StringUtils.isNotBlank(status) && !"0".equals(status)) {
			sb.append(" and  aig.status ='" + status + "'");
		}else if (StringUtils.isNotBlank(query.getStatus()) && !"0".equals(query.getStatus())) {
			sb.append(" and  aig.status ='" + query.getStatus() + "'");
		}
		// 团号
		if (StringUtils.isNotBlank(query.getGroupCode())) {
			sb.append(" and  aig.groupCode like '%" + query.getGroupCode() + "%'");
		}
		// 产品名
		if (StringUtils.isNotBlank(query.getActivityName())) {
			sb.append(" and  ai.activityName like '%" + query.getActivityName()
					+ "%'");
		}
		// 岛屿
		if (StringUtils.isNotBlank(query.getIsland())) {
			sb.append(" and EXISTS(SELECT 1 FROM island i WHERE ai.island_uuid=i.uuid and i.uuid = '"
					+ query.getIsland() + "')");
		}
		// 上岛方式
		if (StringUtils.isNotBlank(query.getIslandway())) {
			sb.append(" and aig.island_way like '%" + query.getIslandway()
					+ "%'");
		}
		// 酒店
		if (StringUtils.isNotBlank(query.getHotel())) {
			sb.append(" and EXISTS(SELECT 1 FROM hotel h WHERE ai.hotel_uuid=h.uuid and h.uuid = '"
					+ query.getHotel() + "')");
		}
		// 房型
		if (StringUtils.isNotBlank(query.getRoomtype())) {
			sb.append(" and EXISTS(SELECT 1 FROM activity_island_group_room aigm WHERE ai.uuid=aigm.activity_island_uuid"
					+ " and aig.uuid=aigm.activity_island_group_uuid and aigm.hotel_room_uuid = '"
					+ query.getRoomtype() + "')");
		}
		// 酒店星级
		if (StringUtils.isNotBlank(query.getHotelstar())) {
			sb.append(" and EXISTS(SELECT 1 FROM hotel_star hs,hotel h WHERE h.uuid=ai.hotel_uuid and h.star = hs.uuid and hs.uuid='"
					+ query.getHotelstar() + "')");
		}
		// 餐型
		if (StringUtils.isNotBlank(query.getFoodtype())) {
			sb.append(" and EXISTS(SELECT 1 FROM activity_island_group_meal aigm WHERE aigm.activity_island_group_uuid = aig.uuid "
					+ " and aigm.activity_island_uuid = ai.uuid and aigm.delFlag = '0' and aigm.uuid = '"
					+ query.getFoodtype() + "')");
		}
		// 开团日期
		if (StringUtils.isNotBlank(query.getStartGroupDate())) {
			sb.append(" and aig.groupOpenDate >='" + query.getStartGroupDate()
					+ "'");
		}
		// 结束日期
		if (StringUtils.isNotBlank(query.getEndGroupDate())) {
			sb.append(" and aig.groupOpenDate <='" + query.getEndGroupDate()
					+ "'");
		}
		// 币种
		if (query.getCurrencyId() != null) {
			if ("2".equals(showType)) {
				sb.append(" and aigl.currency_id =" + query.getCurrencyId());
			} else {
				sb.append(" and aigp.currency_id ="
						+ query.getCurrencyId());
			}
		}
		// 同行价格
		if (query.getStartPrice() != null) {
			if ("2".equals(showType)) {
				sb.append(" and aigl.price >=" + query.getStartPrice());
			} else {
				sb.append(" and exists (select 1 from traveler_type tt where tt.uuid=aigp.type and tt.sys_traveler_type='3b23624f1db94deaa32861d642f56f79' and tt.delFlag='0') " +
						" and aigp.price >="
						+ query.getStartPrice());
			}
		}
		// 同行价格
		if (query.getEndPrice() != null) {
			if ("2".equals(showType)) {
				sb.append(" and aigl.price <=" + query.getEndPrice());
			} else {
				sb.append(" and exists (select 1 from traveler_type tt where tt.uuid=aigp.type and tt.sys_traveler_type='3b23624f1db94deaa32861d642f56f79' and tt.delFlag='0') " +
						" and aigp.price <="
						+ query.getEndPrice());
			}
		}
		// 航空公司
		if (StringUtils.isNotBlank(query.getAirline())) {
			sb.append(" and exists (select 1 from sys_airline_info sai where sai.airline_code = aiga.airline and aiga.airline = '"
					+ query.getAirline() + "') ");
		}
		// 航班号
		if (StringUtils.isNotBlank(query.getFlightnum())) {
			sb.append(" and exists (select 1 from sys_airline_info sai where sai.airline_code = aiga.airline and aiga.flight_number = '"
					+ query.getFlightnum() + "')");
		}
		// 舱位等级
		if (StringUtils.isNotBlank(query.getSpacelevel())) {
			sb.append(" and exists (select 1 from sys_airline_info sai where sai.airline_code = aiga.airline and aiga.space_level = '"
					+ query.getSpacelevel() + "')");
		}
		if ("2".equals(showType)) {
			sb.append(" GROUP BY ai.uuid  ");
		} else {
			sb.append(" GROUP BY aig.uuid");
		}
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,
				response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" ");
		} else {
			page.setOrderBy(orderBy);
		}

		String oldsql = " select * from ( " + sb.toString() + " ) u";
		String newsql = " select * from ( "+oldsql+" ) w";
		if("1".equals(showType)){
			return activityIslandDao.findBySql(page, newsql, oldsql, Map.class);
		}
		return activityIslandDao.findBySql(page, oldsql, sb.toString(), Map.class);

	}

	/**
	 * 按产品展示时，查询每个产品对应的详情
	 */
	@Override
	public List<List<Map<String, String>>> getlandProductListSubs(
			List<Map<String, Object>> loop) {
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT aig.uuid as uuid,aig.groupCode AS groupCode,aig.groupOpenDate AS groupOpenDate,aig.airline AS airline,aig.departureTime AS departureTime,aig.arriveTime AS arriveTime,");
		sb.append("  aig.island_way AS islandWay, GROUP_CONCAT(CONCAT_WS(';',aigr.hotel_room_uuid,aigr.nights,aigr.hotel_meal_uuid)) AS roomNightMeal,aig.status as status,");
		sb.append(" aig.remNumber AS remNumber,aig.advNumber AS advNumber,aig.singlePrice AS singlePrice,aig.currency_id AS currencyId,GROUP_CONCAT(CONCAT_WS(';',aigp.type,aigp.currency_id,aigp.price)) AS tradePrice,");
		sb.append(" aig.front_money AS frontMoney,aig.front_money_currency_id AS frontMoneyCurrencyId,aigp.currency_id AS currenId,aigp.price AS price");
		sb.append(" FROM activity_island_group aig LEFT  JOIN activity_island_group_room aigr ON aig.uuid = aigr.activity_island_group_uuid AND aigr.delFlag='0'");
		sb.append(" LEFT JOIN  activity_island_group_price aigp ON aig.uuid = aigp.activity_island_group_uuid AND aigp.delFlag='0'");

		if (CollectionUtils.isNotEmpty(loop)) {
			for (Map<String, Object> map : loop) {
				StringBuffer aigUuids = new StringBuffer();
				String uuidsArray = (String) map.get("aigUuids");
				String[] uuids = uuidsArray.split(",");
				for (String uuid : uuids) {
					aigUuids.append("'" + uuid + "'" + ",");
				}
				String sqlin = " where aig.uuid IN ("
						+ aigUuids.deleteCharAt(aigUuids.length() - 1) + ")";
				String sqlgroup = " GROUP BY  aig.uuid";
				List<Map<String, String>> listMap = activityIslandDao
						.findBySql(sb.toString() + sqlin + sqlgroup, Map.class);
				list.add(listMap);
			}
		}

		return list;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> saveActivityIsland(ActivityIslandInput activityIslandInput) {
		Map<String, String> result = new HashMap<String, String>();
		List<ActivityIslandGroup> activityIslandGroupList = new ArrayList<ActivityIslandGroup>();
		List<ActivityIslandGroupPrice> activityIslandGroupPriceList = new ArrayList<ActivityIslandGroupPrice>();
		List<ActivityIslandGroupRoom> activityIslandGroupRoomList = new ArrayList<ActivityIslandGroupRoom>();
		result.put("status", "");
		result.put("message", "");
		if (UserUtils.getCompanyIdForData() != null) {
			if (activityIslandInput.validateFormInput()) {
				ActivityIsland activityIsland = activityIslandInput
						.transfer2ActivityIsland("add");
				activityIsland.setWholesalerId(Integer.parseInt(String
						.valueOf(UserUtils.getCompanyIdForData())));
				this.setOptInfo(activityIsland, null);
				activityIslandDao.saveObj(activityIsland);
				if (CollectionUtils.isNotEmpty(activityIsland
						.getActivityIslandGroupList())) {
					for (ActivityIslandGroup aig : activityIsland
							.getActivityIslandGroupList()) {
						aig.setActivityIslandUuid(activityIsland.getUuid());
						this.setOptInfo(aig, null);
						activityIslandGroupList.add(aig);

						if (CollectionUtils.isNotEmpty(aig
								.getActivityIslandGroupRoomList())) {
							for (ActivityIslandGroupRoom aigr : aig
									.getActivityIslandGroupRoomList()) {
								aigr.setActivityIslandUuid(activityIsland
										.getUuid());
								aigr.setActivityIslandGroupUuid(aig.getUuid());
								this.setOptInfo(aigr, null);
								activityIslandGroupRoomList.add(aigr);
							}
						}

						if (CollectionUtils.isNotEmpty(aig
								.getActivityIslandGroupPriceList())) {
							for (ActivityIslandGroupPrice aigp : aig
									.getActivityIslandGroupPriceList()) {
								aigp.setActivityIslandUuid(activityIsland
										.getUuid());
								aigp.setActivityIslandGroupUuid(aig.getUuid());
								this.setOptInfo(aigp, null);
								activityIslandGroupPriceList.add(aigp);
							}
						}
					}
				}

				// 保存产品分享用户数据
				activityIslandShareService.saveActivityIslandShareData(
						activityIslandInput.getShareUser(), activityIsland
								.getCreateBy().longValue(), activityIsland
								.getUuid());

				activityIslandGroupDao.batchSave(activityIslandGroupList);

				activityIslandGroupPriceDao
						.batchSave(activityIslandGroupPriceList);

				activityIslandGroupRoomDao
						.batchSave(activityIslandGroupRoomList);

			}
		}
		return null;
	}


	public int batchUpdateStatusByIslandUuidArr(String[] islandUuidArray,
			String status) {
		return activityIslandGroupDao.batchUpdateStatusByIslandUuidArr(
				islandUuidArray, status);
	}

	@Override
	public List<ActivityIslandGroupLowprice> getLowpriceByPriceList(
			List<ActivityIslandGroupPrice> activityIslandGroupPriceList,
			String flag) {
		if (CollectionUtils.isNotEmpty(activityIslandGroupPriceList)) {// 判断参数非空
			// 变量初始化
			List<ActivityIslandGroupLowprice> lowpriceList = new ArrayList<ActivityIslandGroupLowprice>();// 海岛游产品团期起价表
																											// 集合（保存返回值）
			List<ActivityIslandGroupPrice> audltPriceList = new ArrayList<ActivityIslandGroupPrice>();// 保存所有成人价格的数据
			Set<Integer> currencyIdSet = new HashSet<Integer>();// 保存所有币种
			List<Integer> currencyIdList = new ArrayList<Integer>();// 保存所有币种

			/* 第一步：筛选出成人价格的数据 ,筛选出币种 */
			for (ActivityIslandGroupPrice obj : activityIslandGroupPriceList) {
				if (TravelerType.ALIAS_ADULT_UUID.equals(obj.getType())) {// 成人
					audltPriceList.add(obj);
					currencyIdSet.add(obj.getCurrencyId());
				}
			}

			if (CollectionUtils.isNotEmpty(currencyIdSet)) {// 筛选出币种集合的非空判断
				/* 第二步：一个币种对应一个ActivityIslandGroupPrice对象 */
				// 币种保存到List集合中
				for (Integer currencyId : currencyIdSet) {
					currencyIdList.add(currencyId);
				}
				// 保存所有成人价格最低价的数据(与币种数量一致)
				ActivityIslandGroupPrice[] minPriceArray = new ActivityIslandGroupPrice[currencyIdSet
						.size()];

				// 给minPriceArray装载数据
				for (int i = 0; i < currencyIdList.size(); i++) {// 遍历币种
					Integer currencyId = currencyIdList.get(i);
					Double tempPrice = null;
					for (int j = 0; j < audltPriceList.size(); j++) {// 遍历成人价格数据
						ActivityIslandGroupPrice obj = audltPriceList.get(j);
						if (currencyId == obj.getCurrencyId()) {
							if (tempPrice == null) {// 第一次比较
								tempPrice = obj.getPrice();
							} else {
								if (obj.getPrice() < tempPrice) {
									tempPrice = obj.getPrice();
									minPriceArray[i] = obj;
								}
							}
						}

					}
				}

				/*
				 * 第三步：把ActivityIslandGroupPrice对象转换成ActivityIslandGroupLowprice对象
				 * ，并装到集合中 返回
				 */
				// 给lowpriceList装载数据（返回值）
				for (int i = 0; i < minPriceArray.length; i++) {
					ActivityIslandGroupPrice price = minPriceArray[i];
					if (price != null) {
						ActivityIslandGroupLowprice lowprice = new ActivityIslandGroupLowprice();
						lowprice.setActivityIslandUuid(price
								.getActivityIslandUuid());
						lowprice.setActivityIslandGroupUuid(price
								.getActivityIslandGroupUuid());
						lowprice.setCurrencyId(price.getCurrencyId());
						lowprice.setPrice(price.getPrice());
						super.setOptInfo(lowprice, flag);
						lowpriceList.add(lowprice);
					}
				}
				return lowpriceList;
			}
		}
		return null;
	}

	/**
	 * 获取单个产品对应的起价列表 按产品和币种分组
	 * 
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> getIslandLowPriceList(String uuid) {
		List<Map<String, Object>> lowPriceList = new ArrayList<Map<String, Object>>();
		StringBuffer sb = new StringBuffer(
				"select aigl.activity_island_uuid as activity_island_uuid,min(aigl.price) as price,"
						+ "aigl.activity_island_group_uuid as activity_island_group_uuid,aigl.currency_id as currency_id "
						+ "from activity_island_group_lowprice aigl "
						+ "where aigl.delFlag = '0' "
						+ "and aigl.activity_island_uuid = '"
						+ uuid
						+ "' group by aigl.currency_id,aigl.activity_island_uuid");
		lowPriceList = activityIslandDao.findBySql(sb.toString(), Map.class);
		return lowPriceList;
	}

	/**
	 * 获取单个产品对应的团期列表
	 * 
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> getIslandGroupList(String uuid,ActivityIslandQuery query) {
		List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
		StringBuffer sb = new StringBuffer(
				"select aig.uuid AS uuid,aig.groupCode AS groupCode,aig.groupOpenDate AS groupOpenDate,ai.activityName as activityName, "
						+ "ai.island_uuid as island_uuid,ai.hotel_uuid as hotel_uuid, "
						+ "aig.island_way AS island_way,aig.advNumber AS advNumber,aig.currency_id AS currency_id,aig.singlePrice AS singlePrice,aig.front_money AS front_money, "
						+ "aig.front_money_currency_id AS front_money_currency_id,aig.status AS status,ai.uuid as activityIslandUuid, "
						+ "(select sum(io.orderPersonNum) from island_order io where io.activity_island_uuid = aig.activity_island_uuid "
						+ "and io.activity_island_group_uuid = aig.uuid and io.orderStatus = '1') as total_num "
						+ "from activity_island ai,activity_island_group aig,activity_island_group_price aigp "
						+ "where ai.uuid = aig.activity_island_uuid and aig.uuid = aigp.activity_island_group_uuid and ai.uuid = aigp.activity_island_uuid "
						+ "and ai.delFlag = '0' and aig.delFlag = '0' and aigp.delFlag = '0' "
						+ "and ai.uuid = '" + uuid + "' ");
		if(StringUtils.isNotBlank(query.getStatus()) && !"0".equals(query.getStatus())){
			sb.append(" and aig.status = " + query.getStatus());
		}
		sb.append(" group by aig.uuid");
		groupList = activityIslandDao.findBySql(sb.toString(), Map.class);
		return groupList;
	}

	/**
	 * 获取游客类型对应的价格 列表
	 * 
	 * @param list
	 * @param showType
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> getTravelerPriceList(
			String uuid, String groupUuids) {
		Map<String, List<Map<String, Object>>> result = new TreeMap<String, List<Map<String, Object>>>();
		StringBuffer sb = new StringBuffer(
				"select CONCAT_WS(',',IFNULL(aiga.space_level,'$'),aiga.remNumber,aiga.control_num,aiga.uncontrol_num) as space_level,aiga.remNumber as remNumber, "
						+ "aigp.type as type,aigp.currency_id as currency_id,aigp.price as price from activity_island_group_price aigp,activity_island_group_airline aiga "
						+ "where aiga.uuid = aigp.activity_island_group_airline_uuid and aigp.delFlag='0' and aiga.delFlag='0' "
						+ "and aigp.activity_island_uuid = '"
						+ uuid
						+ "' "
						+ "and aigp.activity_island_group_uuid in ('"
						+ groupUuids + "')");
		List<Map<String, Object>> list = activityIslandDao.findBySql(
				sb.toString(), Map.class);
		for (Map<String, Object> map : list) {
			for (String s : map.keySet()) {
				if ("space_level".equals(s)) {
					String key = (String) map.get(s);
					if (!result.containsKey(key)) {
						result.put(key, new ArrayList<Map<String, Object>>());
					}
					result.get(key).add(map);
				} else {
					continue;
				}
			}
		}
		return result;
	}
	
	public Map<String, Object> getJsonAirlineInfo() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<AirlineInfo> airInfoList = airlineInfoService.getDistinctAirline(companyId);//获得所有的航空公司 航空公司名字及二字码
		Map<String,Object> airlines = new HashMap<String,Object>();
		for(int i=0;i<airInfoList.size();i++){
			Map<String,Object> airlineMap = new HashMap<String,Object>();	
			AirlineInfo airline = airInfoList.get(i);
			List<AirlineInfo> airFlightNums = airlineInfoService.getDistinctFlightNum(companyId, airline.getAirlineCode());//查询航空公司对应下的航班号，起飞时间到达时间
			Map<String,Map<String, String>> flyNos = new HashMap<String,Map<String, String>>();
			for(AirlineInfo flyNo:airFlightNums){
				Map<String,String> startEnd = new HashMap<String,String>();
				Date startDate = flyNo.getDeparturetime();
				if(startDate!=null){
					startEnd.put("start", sdf.format(startDate));
				}
				Date endDate = flyNo.getArrivaltime();
				if(endDate!=null){
					startEnd.put("end", sdf.format(endDate));
				}
				if(flyNo.getDayNum()!=null){
					startEnd.put("day", flyNo.getDayNum().toString());
				}
				
				if(StringUtils.isNotEmpty(flyNo.getFlightnumber())){
					startEnd.put("name", flyNo.getFlightnumber());
					flyNos.put(flyNo.getFlightnumber(), startEnd);
				}
			}
			if(StringUtils.isNotEmpty(airline.getAirlineName())){
				airlineMap.put("name", airline.getAirlineName());
			}
			if(MapUtils.isNotEmpty(flyNos)){
				airlineMap.put("flights", flyNos);
			}
			String key = airline.getAirlineCode();
			airlines.put(key,airlineMap);
		}
		return airlines;
	}
	
	/**
	 * 获取舱位等级及同行价
	 * @param uuid
	 * @param groupUuids
	 * @param hotelUuid
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> getTravelerPriceList(
			String uuid, String groupUuids,String hotelUuid) {
		Map<String, List<Map<String, Object>>> result = new TreeMap<String, List<Map<String, Object>>>();
		StringBuffer sb = new StringBuffer(
				"select CONCAT_WS(',',IFNULL(aiga.space_level,'$'),aiga.remNumber,aiga.control_num,aiga.uncontrol_num) as space_level,aiga.remNumber as remNumber, "
						+ "aigp.type as type,aigp.currency_id as currency_id,aigp.price as price from activity_island_group_price aigp,activity_island_group_airline aiga "
						+ "where aiga.uuid = aigp.activity_island_group_airline_uuid and aigp.delFlag='0' and aiga.delFlag='0' "
						+ "and aigp.activity_island_uuid = '"
						+ uuid
						+ "' "
						+ "and aigp.activity_island_group_uuid in ('"
						+ groupUuids + "') " 
						+"and aigp.type in " 
	                    +"(select tt.uuid from traveler_type tt join hotel_traveler_type_relation httr on tt.uuid = httr.traveler_type_uuid " 
	                    +"where tt.status='1' and tt.delFlag='0' and httr.delFlag='0' and httr.hotel_uuid = '"+ hotelUuid +"' )");
		List<Map<String, Object>> list = activityIslandDao.findBySql(
				sb.toString(), Map.class);
		for (Map<String, Object> map : list) {
			for (String s : map.keySet()) {
				if ("space_level".equals(s)) {
					String key = (String) map.get(s);
					if (!result.containsKey(key)) {
						result.put(key, new ArrayList<Map<String, Object>>());
					}
					result.get(key).add(map);
				} else {
					continue;
				}
			}
		}
		return result;
	}
}
