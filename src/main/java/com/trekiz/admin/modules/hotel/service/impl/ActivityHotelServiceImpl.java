/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupControlDetailDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupLowpriceDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupMealDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupMealRiseDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupPriceDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupRoomDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelShareDao;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupControlDetail;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupLowprice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelShare;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.input.ActivityHotelInput;
import com.trekiz.admin.modules.hotel.input.ActivityHotelJsonBeanInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupLowpriceService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelShareService;
import com.trekiz.admin.modules.island.dao.ActivityIslandDao;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class ActivityHotelServiceImpl extends BaseService implements
		ActivityHotelService {
	@Autowired
	private ActivityHotelDao activityHotelDao;
	@Autowired
	private ActivityIslandDao activityIslandDao;
	@Autowired
	private ActivityHotelGroupDao activityHotelGroupDao;
	@Autowired
	private ActivityHotelGroupRoomDao activityHotelGroupRoomDao;
	@Autowired
	private ActivityHotelGroupMealDao activityHotelGroupMealDao;
	@Autowired
	private ActivityHotelGroupMealRiseDao activityHotelGroupMealRiseDao;
	@Autowired
	private ActivityHotelGroupPriceDao activityHotelGroupPriceDao;
	@Autowired
	private ActivityHotelGroupLowpriceDao activityHotelGroupLowpriceDao;
	@Autowired
	private ActivityHotelShareService activityHotelShareService;
	@Autowired
	private ActivityHotelShareDao activityHotelShareDao;
	@Autowired
	private HotelAnnexDao hotelAnnexDao;
	@Autowired
	private ActivityHotelGroupControlDetailDao activityHotelGroupControlDetailDao;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private ActivityHotelGroupLowpriceService activityHotelGroupLowpriceService;


	public void save(ActivityHotel activityHotel) {
		super.setOptInfo(activityHotel, BaseService.OPERATION_ADD);
		activityHotelDao.saveObj(activityHotel);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> save(ActivityHotelInput activityHotelInput,String status) {

		Map<String, String> result = new HashMap<String, String>();
		// 酒店产品团期表 封装数据
		List<ActivityHotelShare> allShareList = new ArrayList<ActivityHotelShare>();
		List<ActivityHotelGroup> allGroupList = new ArrayList<ActivityHotelGroup>();
		List<ActivityHotelGroupRoom> allRoomList = new ArrayList<ActivityHotelGroupRoom>();
		List<ActivityHotelGroupMeal> allMealList = new ArrayList<ActivityHotelGroupMeal>();
		List<ActivityHotelGroupMealRise> allRiseList = new ArrayList<ActivityHotelGroupMealRise>();
		List<ActivityHotelGroupPrice> allPriceList = new ArrayList<ActivityHotelGroupPrice>();
		List<ActivityHotelGroupLowprice> allLowpriceList = new ArrayList<ActivityHotelGroupLowprice>();
		List<ActivityHotelGroupControlDetail> allControlDetailList = new ArrayList<ActivityHotelGroupControlDetail>();

		if (UserUtils.getCompanyIdForData() != null) {
			if (activityHotelInput.validateFormInput()) {
				ActivityHotel activityHotel = activityHotelInput.getActivityHotel();// 从input中取值
				activityHotel.setWholesalerId(Integer.parseInt(UserUtils
						.getCompanyIdForData().toString()));
				Set<Department> userDeptList = UserUtils.getDepartmentByJob();
				if (CollectionUtils.isNotEmpty(userDeptList)) {
					activityHotel.setDeptId(Integer.parseInt(userDeptList.iterator().next().getId().toString()));
				}
				super.setOptInfo(activityHotel, BaseService.OPERATION_ADD);// 添加创建人等信息
				// 保存一级表 activity_hotel
				this.activityHotelDao.saveObj(activityHotel);
				// activity_hotel表uuid
				String activityHotelUuid = activityHotel.getUuid();
                //取json转换成的对象
				List<ActivityHotelGroup> groupList = activityHotelInput.getActivityhotelGroupLists();
				if (CollectionUtils.isNotEmpty(groupList)) {
					for (ActivityHotelGroup group : groupList) {
						group.setActivityHotelUuid(activityHotelUuid);// 一级表uuid
						group.setStatus(status);// 1：上架；2：下架；3：草稿；4：已删除
						super.setOptInfo(group, BaseService.OPERATION_ADD);// 添加默认信息
						allGroupList.add(group);// 统一保存到allGroupList集合中
						//activity_hotel_group表uuid
						String activityGroupUuid = group.getUuid();
						// (三级表)activity_hotel_group_price
						List<ActivityHotelGroupPrice> priceList = group
								.getActivityHotelGroupPriceList();
						if (CollectionUtils.isNotEmpty(priceList)) {
							for (int i = 0; i < priceList.size(); i++) {
								ActivityHotelGroupPrice activityHotelGroupPrice = priceList
										.get(i);
								if (activityHotelGroupPrice == null
										|| activityHotelGroupPrice.getPrice() == null
										|| activityHotelGroupPrice.getPrice() == 0.0) {
									continue;
								} else {
									activityHotelGroupPrice
											.setActivityHotelUuid(activityHotelUuid);
									activityHotelGroupPrice
											.setActivityHotelGroupUuid(activityGroupUuid);

									super.setOptInfo(activityHotelGroupPrice,
											BaseService.OPERATION_ADD);// 添加默认信息
									allPriceList.add(activityHotelGroupPrice);// 统一保存到allPriceList集合中
								}
							}
						}
						// (三级表)activity_hotel_group_room
						List<ActivityHotelGroupRoom> roomList = group
								.getActivityHotelGroupRoomList();// 从input中取值
						if (CollectionUtils.isNotEmpty(roomList)) {// 非空验证
							for (ActivityHotelGroupRoom room : roomList) {// 遍历
								room.setActivityHotelUuid(activityHotelUuid);// 一级表uuid
								room.setActivityHotelGroupUuid(activityGroupUuid);// 二级表uuid
								super.setOptInfo(room,
										BaseService.OPERATION_ADD);// 添加默认信息
								allRoomList.add(room);// 统一保存到allRoomList集合中
								//组装餐型表的数据
								List<ActivityHotelGroupMeal> mealList = room.getActivityHotelGroupMealList();
								if(CollectionUtils.isNotEmpty(mealList)){
									for (ActivityHotelGroupMeal meal : mealList) {
										meal.setActivityHotelUuid(activityHotelUuid);
										meal.setActivityHotelGroupUuid(activityGroupUuid);
										meal.setActivityHotelGroupRoomUuid(room.getUuid());
										super.setOptInfo(meal,BaseService.OPERATION_ADD);
										allMealList.add(meal);
										//组装升餐标的数据
										List<ActivityHotelGroupMealRise> riseList = meal.getActivityHotelGroupMealsRiseList();// 从input中取值
										if (CollectionUtils.isNotEmpty(mealList)) {// 非空验证
											for (ActivityHotelGroupMealRise rise : riseList) {
												rise.setActivityHotelUuid(activityHotelUuid);
												rise.setActivityHotelGroupUuid(activityGroupUuid);
												rise.setActivityHotelGroupMealUuid(meal.getUuid());
												super.setOptInfo(rise,BaseService.OPERATION_ADD);// 添加默认信息
												allRiseList.add(rise);// 统一保存到allRiseList集合中
											}
										}
									}
								}
							}
						}

						// 装配activity_hotel_group_controlDetail
						List<ActivityHotelGroupControlDetail> controlDetail = group.getActivityHotelGroupControlDetail();
						if (CollectionUtils.isNotEmpty(controlDetail)) {
							for (ActivityHotelGroupControlDetail ahgcd : controlDetail) {
								ahgcd.setActivityHotelUuid(activityHotelUuid);
								ahgcd.setActivityHotelGroupUuid(activityGroupUuid);
								super.setOptInfo(ahgcd,BaseService.OPERATION_ADD);
								allControlDetailList.add(ahgcd);
							}
						}
					}

					// 保存产品分享用户数据
					List<ActivityHotelShare> shareList = activityHotelInput.transfer2ActivityHotelShare();
					if(CollectionUtils.isNotEmpty(shareList)){
						for (ActivityHotelShare share : shareList) {
							share.setActivityHotelUuid(activityHotelUuid);
							share.setShareUser(Long.parseLong(activityHotel
									.getCreateBy().toString()));
							super.setOptInfo(share, BaseService.OPERATION_ADD);// 添加默认信息
							allShareList.add(share);
						}
					}
					// 保存附件信息
					hotelAnnexDao.synDocInfo(activityHotelUuid,
							HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_HOTEL_PRODSCH,
							UserUtils.getCompanyIdForData().intValue(),
							activityHotelInput.getProdSchList());
					hotelAnnexDao.synDocInfo(activityHotelUuid,
							HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_HOTEL_COSTPROTOCOL,
							UserUtils.getCompanyIdForData().intValue(),
							activityHotelInput.getCostProtocolList());
					hotelAnnexDao.synDocInfo(activityHotelUuid,
									HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_HOTEL_OTHERPROTOCOL,
									UserUtils.getCompanyIdForData().intValue(),
									activityHotelInput.getOtherProtocolList());

					// 批量保存操作
					if (CollectionUtils.isNotEmpty(allGroupList)) {// 团期表不为空
						this.activityHotelGroupDao.batchSave(allGroupList);
						if (CollectionUtils.isNotEmpty(allLowpriceList)) {// 航班不为空
							this.activityHotelGroupLowpriceDao
									.batchSave(allLowpriceList);
						}
						if (CollectionUtils.isNotEmpty(allRoomList)) {
							this.activityHotelGroupRoomDao
									.batchSave(allRoomList);
						}
						if (CollectionUtils.isNotEmpty(allMealList)) {// 基础餐型不为空
							this.activityHotelGroupMealDao
									.batchSave(allMealList);
							if (CollectionUtils.isNotEmpty(allRiseList)) {
								this.activityHotelGroupMealRiseDao
										.batchSave(allRiseList);
							}
						}
						if (CollectionUtils.isNotEmpty(allPriceList)) {
							this.activityHotelGroupPriceDao.batchSave(allPriceList);
						}
						if (CollectionUtils.isNotEmpty(allShareList)) {
							this.activityHotelShareDao.batchSave(allShareList);
						}
						if (CollectionUtils.isNotEmpty(allControlDetailList)) {
							this.activityHotelGroupControlDetailDao.batchSave(allControlDetailList);
						}

					}
					activityHotelGroupLowpriceService.getPriceList(activityHotelUuid);
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

	public void update(ActivityHotel activityHotel) {
		super.setOptInfo(activityHotel, BaseService.OPERATION_UPDATE);
		activityHotelDao.updateObj(activityHotel);
	}

	public ActivityHotel getById(java.lang.Integer value) {
		return activityHotelDao.getById(value);
	}

	public void removeById(java.lang.Integer value) {
		ActivityHotel obj = activityHotelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}

	public Page<ActivityHotel> find(Page<ActivityHotel> page,
			ActivityHotelQuery activityHotelQuery) {
		DetachedCriteria dc = activityHotelDao.createDetachedCriteria();

		if (activityHotelQuery.getId() != null) {
			dc.add(Restrictions.eq("id", activityHotelQuery.getId()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getUuid())) {
			dc.add(Restrictions.eq("uuid", activityHotelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getActivitySerNum())) {
			dc.add(Restrictions.eq("activitySerNum",
					activityHotelQuery.getActivitySerNum()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getActivityName())) {
			dc.add(Restrictions.eq("activityName",
					activityHotelQuery.getActivityName()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getCountry())) {
			dc.add(Restrictions.eq("country", activityHotelQuery.getCountry()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getIslandUuid())) {
			dc.add(Restrictions.eq("islandUuid",
					activityHotelQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getHotelUuid())) {
			dc.add(Restrictions.eq("hotelUuid",
					activityHotelQuery.getHotelUuid()));
		}
		if (activityHotelQuery.getCurrencyId() != null) {
			dc.add(Restrictions.eq("currencyId",
					activityHotelQuery.getCurrencyId()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getMemo())) {
			dc.add(Restrictions.eq("memo", activityHotelQuery.getMemo()));
		}
		if (activityHotelQuery.getWholesalerId() != null) {
			dc.add(Restrictions.eq("wholesalerId",
					activityHotelQuery.getWholesalerId()));
		}
		if (activityHotelQuery.getDeptId() != null) {
			dc.add(Restrictions.eq("deptId", activityHotelQuery.getDeptId()));
		}
		if (activityHotelQuery.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy", activityHotelQuery.getCreateBy()));
		}
		if (activityHotelQuery.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate",
					activityHotelQuery.getCreateDate()));
		}
		if (activityHotelQuery.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy", activityHotelQuery.getUpdateBy()));
		}
		if (activityHotelQuery.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate",
					activityHotelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getDelFlag())) {
			dc.add(Restrictions.eq("delFlag", activityHotelQuery.getDelFlag()));
		}

		// dc.addOrder(Order.desc("id"));
		return activityHotelDao.find(page, dc);
	}

	public List<ActivityHotel> find(ActivityHotelQuery activityHotelQuery) {
		DetachedCriteria dc = activityHotelDao.createDetachedCriteria();

		if (activityHotelQuery.getId() != null) {
			dc.add(Restrictions.eq("id", activityHotelQuery.getId()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getUuid())) {
			dc.add(Restrictions.eq("uuid", activityHotelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getActivitySerNum())) {
			dc.add(Restrictions.eq("activitySerNum",
					activityHotelQuery.getActivitySerNum()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getActivityName())) {
			dc.add(Restrictions.eq("activityName",
					activityHotelQuery.getActivityName()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getCountry())) {
			dc.add(Restrictions.eq("country", activityHotelQuery.getCountry()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getIslandUuid())) {
			dc.add(Restrictions.eq("islandUuid",
					activityHotelQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getHotelUuid())) {
			dc.add(Restrictions.eq("hotelUuid",
					activityHotelQuery.getHotelUuid()));
		}
		if (activityHotelQuery.getCurrencyId() != null) {
			dc.add(Restrictions.eq("currencyId",
					activityHotelQuery.getCurrencyId()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getMemo())) {
			dc.add(Restrictions.eq("memo", activityHotelQuery.getMemo()));
		}
		if (activityHotelQuery.getWholesalerId() != null) {
			dc.add(Restrictions.eq("wholesalerId",
					activityHotelQuery.getWholesalerId()));
		}
		if (activityHotelQuery.getDeptId() != null) {
			dc.add(Restrictions.eq("deptId", activityHotelQuery.getDeptId()));
		}
		if (activityHotelQuery.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy", activityHotelQuery.getCreateBy()));
		}
		if (activityHotelQuery.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate",
					activityHotelQuery.getCreateDate()));
		}
		if (activityHotelQuery.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy", activityHotelQuery.getUpdateBy()));
		}
		if (activityHotelQuery.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate",
					activityHotelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelQuery.getDelFlag())) {
			dc.add(Restrictions.eq("delFlag", activityHotelQuery.getDelFlag()));
		}

		// dc.addOrder(Order.desc("id"));
		return activityHotelDao.find(dc);
	}

	public ActivityHotel getByUuid(String uuid) {
		return activityHotelDao.getByUuid(uuid);
	}

	public void removeByUuid(String uuid) {
		ActivityHotel activityHotel = getByUuid(uuid);
		activityHotel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityHotel);
	}

	/**
	 * 构造酒店列表页面add by hhx
	 * 
	 * @param query
	 * @param request
	 * @param response
	 * @return
	 */
	public Page<Map<String, Object>> getActivityHotelList(
			ActivityHotelQuery query, HttpServletRequest request,
			HttpServletResponse response) {
		String showType = request.getParameter("showType");
		String orderBy = request.getParameter("orderBy");
		String status = request.getParameter("status");
		StringBuffer sb = new StringBuffer();
		if ("2".equals(showType)) {
			sb.append("select ah.activitySerNum as activitySerNum,activityName as activityName,country as country,island_uuid as island_uuid,ah.hotel_uuid, "
					+ "(select hs.value from hotel_star hs,hotel h where h.star=hs.uuid and h.uuid=ah.hotel_uuid) as hotel_star,ah.uuid "
					+ "from activity_hotel ah left join activity_hotel_group_lowprice ahgl on ah.uuid = ahgl.activity_hotel_uuid and ahgl.delFlag='0', "
					+ "activity_hotel_group ahg " 
					+ "where ah.uuid = ahg.activity_hotel_uuid "
					+ "and ah.delFlag='0' and ahg.delFlag='0' "
					+ "and ah.wholesaler_id = "
					+ UserUtils.getUser().getCompany().getId());
		} else {
			sb.append("select ahg.uuid AS uuid,ahg.groupCode AS groupCode,ahg.groupOpenDate AS groupOpenDate,ah.activityName as activityName,ah.country as country,ahg.priority_deduction as priority_deduction, "
					+ "ah.island_uuid as island_uuid,ah.hotel_uuid as hotel_uuid,ahg.island_way AS island_way,ahg.currency_id AS currency_id, "
					+ "ahg.singlePrice AS singlePrice,ahg.front_money AS front_money,ahg.remNumber as remNumber,ahg.control_num as control_num, " 
					+ "(select hs.value from hotel_star hs,hotel h where h.star=hs.uuid and h.uuid=ah.hotel_uuid) as hotel_star,ahg.uncontrol_num as uncontrol_num, "
					+ "ahg.front_money_currency_id AS front_money_currency_id,ahg.status AS status,ah.uuid as activityHotelUuid,ah.activitySerNum as activitySerNum "
					+ "from activity_hotel_group ahg LEFT JOIN activity_hotel_group_price ahgp on ahg.uuid = ahgp.activity_hotel_group_uuid,activity_hotel ah "
					+ "where ah.uuid = ahg.activity_hotel_uuid "
					+ "and ah.delFlag = '0' and ahg.delFlag = '0' "
					+ "and ah.wholesaler_id = "
					+ UserUtils.getUser().getCompany().getId());
		}

		genAppendSql(query, showType, status, sb);

		if ("2".equals(showType)) {
			sb.append(" GROUP BY ah.uuid");
		} else {
			sb.append(" GROUP BY ahg.uuid");
		}
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,
				response);
		if (StringUtils.isNotBlank(orderBy)) {
			page.setOrderBy(orderBy);
		}
		String sql = " select * from ( " + sb.toString() + " ) u";
		//return activityHotelDao.findBySql(page, sb.toString(), Map.class);
		return activityIslandDao.findBySql(page, sql, sb.toString(), Map.class);
	}

	private void genAppendSql(ActivityHotelQuery query, String showType,
			String status, StringBuffer sb) {
		// 产品号
		if (StringUtils.isNotBlank(query.getActivitySerNum())) {
			sb.append(" and ah.activitySerNum like '%"
					+ query.getActivitySerNum().trim() + "%'");
		}
		if (StringUtils.isNotBlank(query.getGroupCode())) {
			sb.append(" and ahg.groupCode like '%"
					+ query.getGroupCode().trim() + "%'");
		}
		// 产品名称
		if (StringUtils.isNotBlank(query.getActivityName())) {
			sb.append(" and ah.activityName like '%" + query.getActivityName()
					+ "%'");
		}
		// 团期状态
		if (StringUtils.isNotBlank(query.getStatus()) && !"0".equals(query.getStatus())) {
			sb.append(" and ahg.status = " + query.getStatus());
		}
		// 岛屿
		if (StringUtils.isNotBlank(query.getIsland())) {
			sb.append(" and EXISTS(SELECT 1 FROM island i WHERE ah.island_uuid=i.uuid and i.uuid = '"
					+ query.getIsland() + "')");
		}
		// 上岛方式
		if (StringUtils.isNotBlank(query.getIslandway())) {
			sb.append(" and ahg.island_way like '%" + query.getIslandway()
					+ "%'");
		}
		// 酒店
		if (StringUtils.isNotBlank(query.getHotel())) {
			sb.append(" and EXISTS(SELECT 1 FROM hotel h WHERE ah.hotel_uuid=h.uuid and h.uuid = '"
					+ query.getHotel() + "')");
		}
		// 房型
		if (StringUtils.isNotBlank(query.getRoomtype())) {
			sb.append(" and EXISTS(SELECT 1 FROM activity_hotel_group_room ahgm WHERE ah.uuid=ahgm.activity_hotel_uuid"
					+ " and ahg.uuid=ahgm.activity_hotel_group_uuid and ahgm.hotel_room_uuid = '"
					+ query.getRoomtype() + "')");
		}
		// 酒店星级
		if (StringUtils.isNotBlank(query.getHotelstar())) {
			sb.append(" and EXISTS(SELECT 1 FROM hotel_star hs,hotel h WHERE h.uuid=ah.hotel_uuid and h.star = hs.uuid and hs.uuid='"
					+ query.getHotelstar() + "')");
		}
		// 餐型
		if (StringUtils.isNotBlank(query.getFoodtype())) {
			sb.append(" and EXISTS(SELECT 1 FROM activity_hotel_group_meal ahgm WHERE ahgm.activity_hotel_group_uuid = ahg.uuid "
					+ " and ahgm.activity_hotel_uuid = ah.uuid and ahgm.delFlag = '0' and ahgm.uuid = '"
					+ query.getFoodtype() + "')");
		}
		// 开团日期
		if (StringUtils.isNotBlank(query.getStartGroupDate())) {
			sb.append(" and ahg.groupOpenDate >='" + query.getStartGroupDate()
					+ "'");
		}
		// 结束日期
		if (StringUtils.isNotBlank(query.getEndGroupDate())) {
			sb.append(" and ahg.groupOpenDate <='" + query.getEndGroupDate()
					+ "'");
		}
		// 币种
		if (query.getCurrencyId() != null) {
			if ("2".equals(showType)) {
				sb.append(" and ahgl.currency_id =" + query.getCurrencyId());
			} else {
				if(query.getStartPrice() != null || query.getEndPrice() != null)
				sb.append(" and ahgp.currency_id ="+ query.getCurrencyId());
			}
		}
		// 同行价格
		if (query.getStartPrice() != null) {
			if ("2".equals(showType)) {
				sb.append(" and ahgl.price >=" + query.getStartPrice());
			} else {
				sb.append(" and exists (select 1 from traveler_type tt where tt.uuid=ahgp.type and tt.sys_traveler_type='3b23624f1db94deaa32861d642f56f79' and tt.delFlag='0') " +
						" and ahgp.price >="
						+ query.getStartPrice());
			}
		}
		// 同行价格
		if (query.getEndPrice() != null) {
			if ("2".equals(showType)) {
				sb.append(" and ahgl.price <=" + query.getEndPrice());
			} else {
				sb.append(" and exists (select 1 from traveler_type tt where tt.uuid=ahgp.type and tt.sys_traveler_type='3b23624f1db94deaa32861d642f56f79' and tt.delFlag='0') " +
						" and ahgp.price <="
						+ query.getEndPrice());
			}
		}
	}

	/**
	 * 构造单个产品对应的团期列表add by hhx
	 * 
	 * @param activityHotelUuid
	 * @return
	 */
	public List<Map<String, Object>> getActivityHotelGroupList( 
			String activityHotelUuid,ActivityHotelQuery query) {
		StringBuffer sb = new StringBuffer(
				"select ahg.uuid AS uuid,ahg.groupCode AS groupCode,ahg.groupOpenDate AS groupOpenDate,ah.activityName as activityName,ahg.priority_deduction as priority_deduction, "
						+ "ah.island_uuid as island_uuid,ah.hotel_uuid as hotel_uuid,ahg.island_way AS island_way,ahg.currency_id AS currency_id, "
						+ "ahg.singlePrice AS singlePrice,ahg.front_money AS front_money,ahg.remNumber as remNumber,ahg.control_num as control_num, "
						+ "ahg.front_money_currency_id AS front_money_currency_id,ahg.status AS status,ah.uuid as activityHotelUuid,ahg.uncontrol_num as uncontrol_num "
						+ "from activity_hotel_group ahg LEFT JOIN activity_hotel_group_price ahgp on ahg.uuid = ahgp.activity_hotel_group_uuid,activity_hotel ah "
						+ "where ah.uuid = ahg.activity_hotel_uuid "
						+ "and ah.delFlag = '0' and ahg.delFlag = '0' "
						+ "and ah.uuid = '"+activityHotelUuid+"'");
		if(StringUtils.isNotBlank(query.getStatus()) && !"0".equals(query.getStatus())){
			sb.append(" AND ahg.status = " + query.getStatus());
		}
	    sb.append(" group by ahg.uuid");
		return activityHotelDao.findBySql(sb.toString(), Map.class);
	}
	
	/**
	 * add by wangxv
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> updateActivityHotel(ActivityHotelInput activityHotelInput,String[] jsontext) {
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("message", "");
		result.put("error", "");

		if (UserUtils.getCompanyIdForData() == null) {
			result.put("message", "3");
			result.put("error", "批发商信息读取失败！");
			return result;
		}
		//update by WangXK 20151020 activityHotelInput 添加空指针的判断
		String activityHotelUuid = "";
		if(activityHotelInput != null){
			activityHotelUuid = activityHotelInput.getUuid();
			ActivityHotel activityHotel = activityHotelDao.getByUuid(activityHotelUuid);
			if (activityHotel != null) {
				activityHotel.setActivityName(activityHotelInput.getActivityName());
				activityHotel.setActivitySerNum(activityHotelInput.getActivitySerNum());
				activityHotel.setMemo(activityHotelInput.getMemo());
			}
			this.setOptInfo(activityHotel, "update");

			activityHotelDao.updateObj(activityHotel);
		}
		List<ActivityHotelGroup> activityhotelGroupLists = new ArrayList<ActivityHotelGroup>();
		if(jsontext != null){
			for(String jsonstr : jsontext){
				 ActivityHotelJsonBeanInput info = JSON.parseObject(jsonstr,ActivityHotelJsonBeanInput.class);
				 ActivityHotelGroup ahg = info.transfer2ActivityHotelGroup();
				 activityhotelGroupLists.add(ahg);
			}
		}
		List<ActivityHotelGroup> allGroupList = new ArrayList<ActivityHotelGroup>();
		//先删除库中所有已存在的信息
		if (CollectionUtils.isNotEmpty(activityhotelGroupLists)){
			for (ActivityHotelGroup hotelGroup : activityhotelGroupLists) {
				String groupUuid = hotelGroup.getUuid();
				//statusList.add(hotelGroup.getStatus());
				//hotelGroup.setUuid("");//先将原参数设置为空
				//此处是更新酒店团期表，不是删除
				ActivityHotelGroup upHotelGroup = activityHotelGroupDao.getByUuid(groupUuid);
				BeanUtil.copySimpleProperties(upHotelGroup, hotelGroup,true);
				this.setOptInfo(upHotelGroup, BaseService.OPERATION_UPDATE);
				allGroupList.add(upHotelGroup);//添加到列表，待统一更新
				List<ActivityHotelGroupRoom> roomLists = activityHotelGroupRoomDao.getRoomListByGroupUuid(groupUuid);
				for(ActivityHotelGroupRoom  list :roomLists){
					list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
				}
				activityHotelGroupRoomDao.batchUpdate(roomLists);
				List<ActivityHotelGroupMeal> mealList = activityHotelGroupMealDao.getMealListByGroupUuid(groupUuid);
				for(ActivityHotelGroupMeal list :mealList){
					list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
				}
				activityHotelGroupMealDao.batchUpdate(mealList);
				List<ActivityHotelGroupMealRise> riseMealList = activityHotelGroupMealRiseDao.getbyGroupUuid(groupUuid);
				for(ActivityHotelGroupMealRise list :riseMealList){
					list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
				}
				activityHotelGroupMealRiseDao.batchUpdate(riseMealList);
				List<ActivityHotelGroupPrice> priceList = activityHotelGroupPriceDao.getPriceListByGroupUuid(groupUuid);
				for(ActivityHotelGroupPrice list :priceList){
					list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
				}
				activityHotelGroupPriceDao.batchUpdate(priceList);
				List<ActivityHotelGroupControlDetail> controlDetailList = activityHotelGroupControlDetailDao.getDetailListByGroupUuid(groupUuid);
				for(ActivityHotelGroupControlDetail list :controlDetailList){
					list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
				}
				activityHotelGroupControlDetailDao.batchUpdate(controlDetailList);
			}
		}
		// 酒店产品团期表 封装数据
		List<ActivityHotelShare> allShareList = new ArrayList<ActivityHotelShare>();
		List<ActivityHotelGroupRoom> allRoomList = new ArrayList<ActivityHotelGroupRoom>();
		List<ActivityHotelGroupMeal> allMealList = new ArrayList<ActivityHotelGroupMeal>();
		List<ActivityHotelGroupMealRise> allRiseList = new ArrayList<ActivityHotelGroupMealRise>();
		List<ActivityHotelGroupPrice> allPriceList = new ArrayList<ActivityHotelGroupPrice>();
		List<ActivityHotelGroupLowprice> allLowpriceList = new ArrayList<ActivityHotelGroupLowprice>();
		List<ActivityHotelGroupControlDetail> allControlDetailList = new ArrayList<ActivityHotelGroupControlDetail>();

		
		if (CollectionUtils.isNotEmpty(activityhotelGroupLists)) {
			for (ActivityHotelGroup group : activityhotelGroupLists){
    			String activityGroupUuid = group.getUuid();
				// (三级表)activity_hotel_group_price
				List<ActivityHotelGroupPrice> priceList = group.getActivityHotelGroupPriceList();
				if (CollectionUtils.isNotEmpty(priceList)) {
					for (int i = 0; i < priceList.size(); i++) {
						ActivityHotelGroupPrice activityHotelGroupPrice = priceList.get(i);
						if (activityHotelGroupPrice == null	|| activityHotelGroupPrice.getPrice() == null || activityHotelGroupPrice.getPrice() == 0.0) {
							continue;
						}else{activityHotelGroupPrice.setActivityHotelUuid(activityHotelUuid);
							  activityHotelGroupPrice.setActivityHotelGroupUuid(activityGroupUuid);
							  super.setOptInfo(activityHotelGroupPrice,BaseService.OPERATION_ADD);// 添加默认信息
							  allPriceList.add(activityHotelGroupPrice);// 统一保存到allPriceList集合中
						}
					}
				}
				// (三级表)activity_hotel_group_room
				List<ActivityHotelGroupRoom> roomList = group.getActivityHotelGroupRoomList();
				if (CollectionUtils.isNotEmpty(roomList)) {// 非空验证
					for (ActivityHotelGroupRoom room : roomList) {// 遍历
						room.setActivityHotelUuid(activityHotelUuid);// 一级表uuid
						room.setActivityHotelGroupUuid(activityGroupUuid);// 二级表uuid
						super.setOptInfo(room,BaseService.OPERATION_ADD);// 添加默认信息
						allRoomList.add(room);// 统一保存到allRoomList集合中
						//组装餐型表的数据
						List<ActivityHotelGroupMeal> mealList = room.getActivityHotelGroupMealList();
						if(CollectionUtils.isNotEmpty(mealList)){
							for (ActivityHotelGroupMeal meal : mealList) {
								meal.setActivityHotelUuid(activityHotelUuid);
								meal.setActivityHotelGroupUuid(activityGroupUuid);
								meal.setActivityHotelGroupRoomUuid(room.getUuid());
								super.setOptInfo(meal,BaseService.OPERATION_ADD);
								allMealList.add(meal);
								//组装升餐标的数据
								List<ActivityHotelGroupMealRise> riseList = meal.getActivityHotelGroupMealsRiseList();
								if (CollectionUtils.isNotEmpty(mealList)) {// 非空验证
									for (ActivityHotelGroupMealRise rise : riseList) {
										rise.setActivityHotelUuid(activityHotelUuid);
										rise.setActivityHotelGroupUuid(activityGroupUuid);
										rise.setActivityHotelGroupMealUuid(meal.getUuid());
										super.setOptInfo(rise,BaseService.OPERATION_ADD);// 添加默认信息
										allRiseList.add(rise);// 统一保存到allRiseList集合中
									}
								}
							}
						}
					}
				}

				// 装配activity_hotel_group_controlDetail
				List<ActivityHotelGroupControlDetail> controlDetail = group.getActivityHotelGroupControlDetail();
				if (CollectionUtils.isNotEmpty(controlDetail)) {
					for (ActivityHotelGroupControlDetail ahgcd : controlDetail) {
						ahgcd.setActivityHotelUuid(activityHotelUuid);
						ahgcd.setActivityHotelGroupUuid(activityGroupUuid);
						super.setOptInfo(ahgcd,BaseService.OPERATION_ADD);
						allControlDetailList.add(ahgcd);
					}
				}
			}
			// 批量保存操作
			if (CollectionUtils.isNotEmpty(allGroupList)) {// 团期表不为空
				this.activityHotelGroupDao.batchSave(allGroupList);
				if (CollectionUtils.isNotEmpty(allLowpriceList)) {// 航班不为空
					this.activityHotelGroupLowpriceDao.batchSave(allLowpriceList);
				}
				if (CollectionUtils.isNotEmpty(allRoomList)) {
					this.activityHotelGroupRoomDao.batchSave(allRoomList);
				}
				if (CollectionUtils.isNotEmpty(allMealList)) {// 基础餐型不为空
					this.activityHotelGroupMealDao.batchSave(allMealList);
					if (CollectionUtils.isNotEmpty(allRiseList)) {
						this.activityHotelGroupMealRiseDao.batchSave(allRiseList);
					}
				}
				if (CollectionUtils.isNotEmpty(allPriceList)) {
					this.activityHotelGroupPriceDao.batchSave(allPriceList);
				}
				if (CollectionUtils.isNotEmpty(allControlDetailList)) {
					this.activityHotelGroupControlDetailDao.batchSave(allControlDetailList);
				}

			}
			activityHotelGroupLowpriceService.getPriceList(activityHotelUuid);
		}
		// 保存产品分享用户数据(先删除再新增)
		List<ActivityHotelShare> delShareList = activityHotelShareDao.findByActivityHotelUuid(activityHotelUuid);
		if(CollectionUtils.isNotEmpty(delShareList)){
			for(ActivityHotelShare list : delShareList){
				list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		    }
		}
		activityHotelShareDao.batchUpdate(delShareList);
		//update by WangXK 20151020 添加空指针的判断
		if(activityHotelInput != null){
			List<ActivityHotelShare> shareList = activityHotelInput.transfer2ActivityHotelShare();
			for (ActivityHotelShare share : shareList) {
				share.setActivityHotelUuid(activityHotelUuid);
				share.setShareUser(Long.parseLong(activityHotelDao.getByUuid(activityHotelUuid).getCreateBy().toString()));
				super.setOptInfo(share, BaseService.OPERATION_ADD);// 添加默认信息
				allShareList.add(share);
			}
		}
		
		if (CollectionUtils.isNotEmpty(allShareList)) {
			this.activityHotelShareDao.batchSave(allShareList);
		}
		// 保存附件信息
		//update by WangXK 20151020 添加空指针的判断
		if(UserUtils.getCompanyIdForData()!=null){
			hotelAnnexDao.synDocInfo(activityHotelUuid,HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_HOTEL_PRODSCH,
					UserUtils.getCompanyIdForData().intValue(),activityHotelInput.getProdSchList());
			hotelAnnexDao.synDocInfo(activityHotelUuid,HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_HOTEL_COSTPROTOCOL,
					UserUtils.getCompanyIdForData().intValue(),activityHotelInput.getCostProtocolList());
			hotelAnnexDao.synDocInfo(activityHotelUuid,HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_HOTEL_OTHERPROTOCOL,
							UserUtils.getCompanyIdForData().intValue(),activityHotelInput.getOtherProtocolList());
		}
		/*List<ActivityHotelGroupRoom> groupRoomsList = new ArrayList<ActivityHotelGroupRoom>();
		List<ActivityHotelGroupPrice> groupPriceList = new ArrayList<ActivityHotelGroupPrice>();
		List<ActivityHotelGroupMeal> groupMealList = new ArrayList<ActivityHotelGroupMeal>();
		List<ActivityHotelGroupMealRise> groupMealRiseList = new ArrayList<ActivityHotelGroupMealRise>();
	    List<ActivityHotelGroupLowprice> groupLowpriceList = new ArrayList<ActivityHotelGroupLowprice>();
	    List<ActivityHotelGroupControlDetail> groupControlDetailList = new ArrayList<ActivityHotelGroupControlDetail>();
		
		if (CollectionUtils.isNotEmpty(activityhotelGroupLists)) {
			for (ActivityHotelGroup ahg : activityhotelGroupLists) {
				ActivityHotelGroup hotelGroup = activityHotelGroupDao.getByUuid(ahg.getUuid());
				if (hotelGroup != null) {
					hotelGroup.setGroupCode(ahg.getGroupCode());
					hotelGroup.setGroupOpenDate(ahg.getGroupOpenDate());
					hotelGroup.setAirline(ahg.getAirline());
					hotelGroup.setControlNum(ahg.getControlNum());
					hotelGroup.setUncontrolNum(ahg.getUncontrolNum());
					hotelGroup.setMemo(ahg.getMemo());
					hotelGroup.setIslandWay(ahg.getIslandWay());
					hotelGroup.setCurrencyId(ahg.getCurrencyId());
					hotelGroup.setSinglePriceUnit(ahg.getSinglePriceUnit());
					hotelGroup.setSinglePrice(ahg.getSinglePrice());
					hotelGroup.setFrontMoneyCurrencyId(ahg.getFrontMoneyCurrencyId());
					hotelGroup.setFrontMoney(ahg.getFrontMoney());
				}
				this.setOptInfo(hotelGroup, "update");
				activityHotelGroupDao.updateObj(hotelGroup);
		
				if (CollectionUtils.isNotEmpty(ahg.getActivityHotelGroupRoomList())) {
					for (ActivityHotelGroupRoom groupRooms : ahg.getActivityHotelGroupRoomList()) {
						if (groupRooms == null) {
							continue;
						}
						ActivityHotelGroupRoom groupRoom = activityHotelGroupRoomDao.getByUuid(groupRooms.getUuid());
						groupRoom.setHotelRoomUuid(groupRooms.getHotelRoomUuid());
						groupRoom.setNights(groupRooms.getNights());
						this.setOptInfo(groupRoom, "update");
						groupRoomsList.add(groupRoom);
					}
				}
				if (CollectionUtils.isNotEmpty(ahg.getActivityHotelGroupPriceList())) {
					for (ActivityHotelGroupPrice groupPrices : ahg.getActivityHotelGroupPriceList()) {
						if (groupPrices == null) {
							continue;
						}
						ActivityHotelGroupPrice groupPrice = activityHotelGroupPriceDao.getByUuid(groupPrices.getUuid());
						groupPrice.setType(groupPrices.getType());
						groupPrice.setPrice(groupPrices.getPrice());
						groupPrice.setCurrencyId(groupPrices.getCurrencyId());
						this.setOptInfo(groupPrice, "update");
						groupPriceList.add(groupPrice);
					}
				}
				if (CollectionUtils.isNotEmpty(ahg.getActivityHotelGroupMealList())) {
					for (ActivityHotelGroupMeal groupMeals : ahg.getActivityHotelGroupMealList()) {
						if (groupMeals == null) {
							continue;
						}
						ActivityHotelGroupMeal groupMeal = activityHotelGroupMealDao.getByUuid(groupMeals.getUuid());
						groupMeal.setHotelMealUuid(groupMeals.getHotelMealUuid());
						// 升餐表数据
						if (CollectionUtils.isNotEmpty(groupMeals.getActivityHotelGroupMealsRiseList())) {
							for (ActivityHotelGroupMealRise groupMealRises : groupMeals.getActivityHotelGroupMealsRiseList()) {
								if (groupMealRises == null) {
									continue;
								}
								ActivityHotelGroupMealRise groupMealRise = new ActivityHotelGroupMealRise();
								groupMealRise.setCurrencyId(groupMealRises.getCurrencyId());
								groupMealRise.setPrice(groupMealRises.getPrice());
		     					groupMealRise.setHotelMealUuid(groupMealRises.getHotelMealUuid());
								this.setOptInfo(groupMealRise, "update");
								groupMealRiseList.add(groupMealRise);
							}
							this.setOptInfo(groupMeal, "update");
							groupMealList.add(groupMeal);
						}
					}
				}
	       }
      }
		activityHotelGroupRoomDao.batchUpdate(groupRoomsList);
		activityHotelGroupPriceDao.batchUpdate(groupPriceList);
		activityHotelGroupMealDao.batchUpdate(groupMealList);
		activityHotelGroupMealRiseDao.batchUpdate(groupMealRiseList);*/
	    
		result.put("message", "2");
		return result;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> saveorUpdateActivityHotel(ActivityHotelGroup activityHotelGroup,String activityHotelUuid,String status) {
		
		Map<String, String> result = new HashMap<String, String>();
        // 酒店产品团期表 封装数据
		List<ActivityHotelGroup> allGroupList = new ArrayList<ActivityHotelGroup>();
		List<ActivityHotelGroupRoom> allRoomList = new ArrayList<ActivityHotelGroupRoom>();
		List<ActivityHotelGroupMeal> allMealList = new ArrayList<ActivityHotelGroupMeal>();
		List<ActivityHotelGroupMealRise> allRiseList = new ArrayList<ActivityHotelGroupMealRise>();
		List<ActivityHotelGroupPrice> allPriceList = new ArrayList<ActivityHotelGroupPrice>();
		List<ActivityHotelGroupControlDetail> allControlDetailList = new ArrayList<ActivityHotelGroupControlDetail>();
		
		//先删除库中所有已存在的信息
		String groupUuid = activityHotelGroup.getUuid();
		if (StringUtils.isNotEmpty(groupUuid)) {
			//更新团期数据,此处不是删除
			ActivityHotelGroup upHotelGroup = activityHotelGroupDao.getByUuid(groupUuid);
			//status = upHotelGroup.getStatus();//保存原团期状态
			//activityHotelGroup.setUuid("");//先将原参数设置为空
			BeanUtil.copySimpleProperties(upHotelGroup, activityHotelGroup,true);
			this.setOptInfo(upHotelGroup, BaseService.OPERATION_UPDATE);
			activityHotelGroupDao.updateObj(upHotelGroup);
			List<ActivityHotelGroupRoom> roomLists = activityHotelGroupRoomDao.getRoomListByGroupUuid(groupUuid);
			for(ActivityHotelGroupRoom  list :roomLists){
				list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			}
			activityHotelGroupRoomDao.batchUpdate(roomLists);
			List<ActivityHotelGroupMeal> mealList = activityHotelGroupMealDao.getMealListByGroupUuid(groupUuid);
			for(ActivityHotelGroupMeal list :mealList){
				list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			}
			activityHotelGroupMealDao.batchUpdate(mealList);
			List<ActivityHotelGroupMealRise> riseMealList = activityHotelGroupMealRiseDao.getbyGroupUuid(groupUuid);
			for(ActivityHotelGroupMealRise list :riseMealList){
				list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			}
			activityHotelGroupMealRiseDao.batchUpdate(riseMealList);
			List<ActivityHotelGroupPrice> priceList = activityHotelGroupPriceDao.getPriceListByGroupUuid(groupUuid);
			for(ActivityHotelGroupPrice list :priceList){
				list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			}
			activityHotelGroupPriceDao.batchUpdate(priceList);
			List<ActivityHotelGroupControlDetail> controlDetailList = activityHotelGroupControlDetailDao.getDetailListByGroupUuid(groupUuid);
			for(ActivityHotelGroupControlDetail list :controlDetailList){
				list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			}
			/*activityHotelGroupControlDetailDao.batchUpdate(controlDetailList);
			List<ActivityHotelGroupLowprice> lowpriceList = activityHotelGroupLowpriceDao.getLowPriceListByGroupUuid(groupUuid);
			for(ActivityHotelGroupLowprice list :lowpriceList){
				list.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			}
			activityHotelGroupLowpriceDao.batchUpdate(lowpriceList);*/
		}
		if(StringUtils.isEmpty(groupUuid)){
			activityHotelGroup.setActivityHotelUuid(activityHotelUuid);// 一级表uuid
			activityHotelGroup.setStatus(status==null?"3":status);// 1：上架；2：下架；3：草稿；4：已删除
			super.setOptInfo(activityHotelGroup, BaseService.OPERATION_ADD);// 添加默认信息
			allGroupList.add(activityHotelGroup);// 统一保存到allGroupList集合中
		}
		//再取一次activity_hotel_group表uuid
		String activityGroupUuid = activityHotelGroup.getUuid();
		// (三级表)activity_hotel_group_price
		List<ActivityHotelGroupPrice> priceList = activityHotelGroup.getActivityHotelGroupPriceList();
		if (CollectionUtils.isNotEmpty(priceList)) {
			for (int i = 0; i < priceList.size(); i++) {
				ActivityHotelGroupPrice activityHotelGroupPrice = priceList.get(i);
				if (activityHotelGroupPrice == null|| activityHotelGroupPrice.getPrice() == null
						|| activityHotelGroupPrice.getPrice() == 0.0) {
					continue;
				} else {
					activityHotelGroupPrice.setActivityHotelUuid(activityHotelUuid);
					activityHotelGroupPrice.setActivityHotelGroupUuid(activityGroupUuid);
					super.setOptInfo(activityHotelGroupPrice,BaseService.OPERATION_ADD);// 添加默认信息
					allPriceList.add(activityHotelGroupPrice);// 统一保存到allPriceList集合中
				}
			}
		}
		// (三级表)activity_hotel_group_room
		List<ActivityHotelGroupRoom> roomList = activityHotelGroup.getActivityHotelGroupRoomList();// 从input中取值
		if (CollectionUtils.isNotEmpty(roomList)) {// 非空验证
			for (ActivityHotelGroupRoom room : roomList) {// 遍历
				room.setActivityHotelUuid(activityHotelUuid);// 一级表uuid
				room.setActivityHotelGroupUuid(activityGroupUuid);// 二级表uuid
				super.setOptInfo(room,
						BaseService.OPERATION_ADD);// 添加默认信息
				allRoomList.add(room);// 统一保存到allRoomList集合中
				//组装餐型表的数据
				List<ActivityHotelGroupMeal> mealList = room.getActivityHotelGroupMealList();
				if(CollectionUtils.isNotEmpty(mealList)){
					for (ActivityHotelGroupMeal meal : mealList) {
						meal.setActivityHotelUuid(activityHotelUuid);
						meal.setActivityHotelGroupUuid(activityGroupUuid);
						meal.setActivityHotelGroupRoomUuid(room.getUuid());
						super.setOptInfo(meal,BaseService.OPERATION_ADD);
						allMealList.add(meal);
						//组装升餐标的数据
						List<ActivityHotelGroupMealRise> riseList = meal.getActivityHotelGroupMealsRiseList();// 从input中取值
						if (CollectionUtils.isNotEmpty(mealList)) {// 非空验证
							for (ActivityHotelGroupMealRise rise : riseList) {
								rise.setActivityHotelUuid(activityHotelUuid);
								rise.setActivityHotelGroupUuid(activityGroupUuid);
								rise.setActivityHotelGroupMealUuid(meal.getUuid());
								super.setOptInfo(rise,BaseService.OPERATION_ADD);// 添加默认信息
								allRiseList.add(rise);// 统一保存到allRiseList集合中
							}
						}
					}
				}
			}
		}
		// 装配activity_hotel_group_controlDetail
		List<ActivityHotelGroupControlDetail> controlDetail = activityHotelGroup.getActivityHotelGroupControlDetail();
		if (CollectionUtils.isNotEmpty(controlDetail)) {
			for (ActivityHotelGroupControlDetail ahgcd : controlDetail) {
				ahgcd.setActivityHotelUuid(activityHotelUuid);
				ahgcd.setActivityHotelGroupUuid(activityGroupUuid);
				super.setOptInfo(ahgcd,BaseService.OPERATION_ADD);
				allControlDetailList.add(ahgcd);
			}
		}
	// 批量保存操作
		if (CollectionUtils.isNotEmpty(allGroupList)) {// 团期表不为空
			this.activityHotelGroupDao.batchSave(allGroupList);
		}
		if (CollectionUtils.isNotEmpty(allRoomList)) {
			this.activityHotelGroupRoomDao
					.batchSave(allRoomList);
		}
		if (CollectionUtils.isNotEmpty(allMealList)) {// 基础餐型不为空
			this.activityHotelGroupMealDao
					.batchSave(allMealList);
			if (CollectionUtils.isNotEmpty(allRiseList)) {
				this.activityHotelGroupMealRiseDao
						.batchSave(allRiseList);
			}
		}
		if (CollectionUtils.isNotEmpty(allPriceList)) {
			this.activityHotelGroupPriceDao.batchSave(allPriceList);
		}
	
	activityHotelGroupLowpriceService.getPriceList(activityHotelUuid);
	result.put("status", "success");
	result.put("message", "1");
	return result;
	}
	
	/**
	 * 订单转报名时扣减余位方法
	 * Map<String,String>
	 *         key:result。value：success有剩余库存扣减成功，fail没有库存扣减失败，error参数异常。
	 *         key:message。value:fail和error情况下的异常信息
	 * @param hotelGroupUuid  酒店团期表uuid
	 * @param forecaseReportNum 需扣减的房间数
	 * @return
	 */
	@Override
	public Map<String, String> deductRemNumber(String hotelGroupUuid,Integer forecaseReportNum){
		
		Map<String, String> result = new HashMap<String, String>();
		if(forecaseReportNum == null || StringUtils.isEmpty(hotelGroupUuid)){
			result.put("result", "fail");
			result.put("message", "参数为空.");
			return result;
		}
		//String hotelGroupUuid = order.getActivityHotelGroupUuid();
		//Integer forecaseReportNum = order.getForecaseReportNum();
		ActivityHotelGroup activityHotelGroup = activityHotelGroupDao.getByUuid(hotelGroupUuid);
		if(activityHotelGroup == null ){
			result.put("result", "fail");
			result.put("message", "团期为空.");
			return result;
		}
		Integer remNumber = activityHotelGroup.getRemNumber();
		if(forecaseReportNum>remNumber){
			result.put("result", "fail");
			result.put("message", "余位不足，目前只有余位："+remNumber);
			return result;
		}
		Integer priorityDeduction = activityHotelGroup.getPriorityDeduction();
		Integer controlNum = activityHotelGroup.getControlNum();
		Integer uncontrolNum = activityHotelGroup.getUncontrolNum();
		
		//快速订单余位扣减
		if(priorityDeduction==null && controlNum==null && uncontrolNum==null){
			activityHotelGroup.setRemNumber(remNumber-forecaseReportNum);
			activityHotelGroupService.save(activityHotelGroup);
			result.put("result", "success");
			result.put("message", "扣减成功！");
			return result;
		}
		
		Integer controlNumRes = 0;//扣减结果
		Integer uncontrolNumRes = 0;//扣减结果
		if(ActivityHotelGroup.PRIORITY_CONTROL.equals(priorityDeduction)){//优先扣减控票
			if(forecaseReportNum<controlNum){
				controlNumRes = forecaseReportNum;
				activityHotelGroup.setControlNum(controlNum-forecaseReportNum);
				activityHotelGroup.setRemNumber(remNumber-forecaseReportNum);
				activityHotelGroupService.save(activityHotelGroup);
			}else{
				controlNumRes = controlNum;
				uncontrolNumRes = forecaseReportNum-controlNum;
				activityHotelGroup.setControlNum(0);
				activityHotelGroup.setUncontrolNum(uncontrolNum+controlNum-forecaseReportNum);
				activityHotelGroup.setRemNumber(remNumber-forecaseReportNum);
				activityHotelGroupService.save(activityHotelGroup);
			}
		}else{
			if(forecaseReportNum<uncontrolNum){
				uncontrolNumRes = forecaseReportNum;
				activityHotelGroup.setUncontrolNum(uncontrolNum-forecaseReportNum);
				activityHotelGroup.setRemNumber(remNumber-forecaseReportNum);
				activityHotelGroupService.save(activityHotelGroup);
			}else{
				uncontrolNumRes = uncontrolNum;
				controlNumRes = forecaseReportNum-uncontrolNum;
				activityHotelGroup.setUncontrolNum(0);
				activityHotelGroup.setControlNum(uncontrolNum+controlNum-forecaseReportNum);
				activityHotelGroup.setRemNumber(remNumber-forecaseReportNum);
				activityHotelGroupService.save(activityHotelGroup);
			}
		}
		result.put("result", "success");
		result.put("message", "扣减成功！");
		return result;
	}
}
