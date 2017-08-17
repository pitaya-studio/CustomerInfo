/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MD5Utils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.dao.HotelControlDao;
import com.trekiz.admin.modules.hotel.dao.HotelControlDetailDao;
import com.trekiz.admin.modules.hotel.dao.HotelControlFlightDetailDao;
import com.trekiz.admin.modules.hotel.dao.HotelControlRoomDetailDao;
import com.trekiz.admin.modules.hotel.dao.HotelControlRuleDao;
import com.trekiz.admin.modules.hotel.dao.HotelRoomMealDao;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelControl;
import com.trekiz.admin.modules.hotel.entity.HotelControlDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlFlightDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlRule;
import com.trekiz.admin.modules.hotel.entity.HotelRoomMeal;
import com.trekiz.admin.modules.hotel.input.HotelControlInput;
import com.trekiz.admin.modules.hotel.model.HotelControlDetailModel;
import com.trekiz.admin.modules.hotel.query.HotelControlQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelControlDetailService;
import com.trekiz.admin.modules.hotel.service.HotelControlService;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandOrderControlDetail;
import com.trekiz.admin.modules.island.entity.IslandOrderPrice;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupPriceService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandOrderPriceService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelControlServiceImpl extends BaseService implements
		HotelControlService {
	@Autowired
	private HotelControlDao hotelControlDao;

	@Autowired
	private HotelControlDetailDao hotelControlDetailDao;

	@Autowired
	private HotelControlFlightDetailDao hotelControlFlightDetailDao;

	@Autowired
	private HotelControlRoomDetailDao hotelControlRoomDetailDao;

	@Autowired
	private HotelControlRuleDao hotelControlRuleDao;

	@Autowired
	private ActivityIslandService activityIslandService;

	@Autowired
	private ActivityHotelService activityHotelService;

	@Autowired
	private HotelControlDetailService hotelControlDetailService;

	@Autowired
	private IslandOrderPriceService islandOrderPriceService;
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;

	@Autowired
	private HotelAnnexDao hotelAnnexDao;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityIslandGroupPriceService activityIslandGroupPriceService;
	@Autowired
	private HotelRoomMealDao hotelRoomMealDao;

	public void save(HotelControl hotelControl) {
		super.setOptInfo(hotelControl, BaseService.OPERATION_ADD);
		hotelControlDao.saveObj(hotelControl);
	}

	public void update(HotelControl hotelControl) {
		super.setOptInfo(hotelControl, BaseService.OPERATION_UPDATE);
		hotelControlDao.updateObj(hotelControl);
	}

	public HotelControl getById(java.lang.Integer value) {
		return hotelControlDao.getById(value);
	}

	public void removeById(java.lang.Integer value) {
		HotelControl obj = hotelControlDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}

	public Page<HotelControl> find(Page<HotelControl> page,
			HotelControl hotelControl) {
		DetachedCriteria dc = hotelControlDao.createDetachedCriteria();

		if (hotelControl.getId() != null) {
			dc.add(Restrictions.eq("id", hotelControl.getId()));
		}
		if (StringUtils.isNotEmpty(hotelControl.getUuid())) {
			dc.add(Restrictions.like("uuid", "%" + hotelControl.getUuid() + "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getName())) {
			dc.add(Restrictions.like("name", "%" + hotelControl.getName() + "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getCountry())) {
			dc.add(Restrictions.like("country", "%" + hotelControl.getCountry()
					+ "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getIslandUuid())) {
			dc.add(Restrictions.like("islandUuid",
					"%" + hotelControl.getIslandUuid() + "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getHotelUuid())) {
			dc.add(Restrictions.like("hotelUuid",
					"%" + hotelControl.getHotelUuid() + "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getHotelGroup())) {
			dc.add(Restrictions.like("hotelGroup",
					"%" + hotelControl.getHotelGroup() + "%"));
		}
		if (hotelControl.getGroundSupplier() != null) {
			dc.add(Restrictions.eq("groundSupplier",
					hotelControl.getGroundSupplier()));
		}
		if (hotelControl.getPurchaseType() != null) {
			dc.add(Restrictions.eq("purchaseType",
					hotelControl.getPurchaseType()));
		}
		if (hotelControl.getCurrencyId() != null) {
			dc.add(Restrictions.eq("currencyId", hotelControl.getCurrencyId()));
		}
		if (hotelControl.getWholesalerId() != null) {
			dc.add(Restrictions.eq("wholesalerId",
					hotelControl.getWholesalerId()));
		}
		if (StringUtils.isNotEmpty(hotelControl.getMemo())) {
			dc.add(Restrictions.like("memo", "%" + hotelControl.getMemo() + "%"));
		}
		if (hotelControl.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy", hotelControl.getCreateBy()));
		}
		if (hotelControl.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate", hotelControl.getCreateDate()));
		}
		if (hotelControl.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy", hotelControl.getUpdateBy()));
		}
		if (hotelControl.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate", hotelControl.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelControl.getDelFlag())) {
			dc.add(Restrictions.like("delFlag", "%" + hotelControl.getDelFlag()
					+ "%"));
		}

		// dc.addOrder(Order.desc("id"));
		return hotelControlDao.find(page, dc);
	}

	public List<HotelControl> find(HotelControl hotelControl) {
		DetachedCriteria dc = hotelControlDao.createDetachedCriteria();

		if (hotelControl.getId() != null) {
			dc.add(Restrictions.eq("id", hotelControl.getId()));
		}
		if (StringUtils.isNotEmpty(hotelControl.getUuid())) {
			dc.add(Restrictions.like("uuid", "%" + hotelControl.getUuid() + "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getName())) {
			dc.add(Restrictions.like("name", "%" + hotelControl.getName() + "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getCountry())) {
			dc.add(Restrictions.like("country", "%" + hotelControl.getCountry()
					+ "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getIslandUuid())) {
			dc.add(Restrictions.like("islandUuid",
					"%" + hotelControl.getIslandUuid() + "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getHotelUuid())) {
			dc.add(Restrictions.like("hotelUuid",
					"%" + hotelControl.getHotelUuid() + "%"));
		}
		if (StringUtils.isNotEmpty(hotelControl.getHotelGroup())) {
			dc.add(Restrictions.like("hotelGroup",
					"%" + hotelControl.getHotelGroup() + "%"));
		}
		if (hotelControl.getGroundSupplier() != null) {
			dc.add(Restrictions.eq("groundSupplier",
					hotelControl.getGroundSupplier()));
		}
		if (hotelControl.getPurchaseType() != null) {
			dc.add(Restrictions.eq("purchaseType",
					hotelControl.getPurchaseType()));
		}
		if (hotelControl.getCurrencyId() != null) {
			dc.add(Restrictions.eq("currencyId", hotelControl.getCurrencyId()));
		}
		if (hotelControl.getWholesalerId() != null) {
			dc.add(Restrictions.eq("wholesalerId",
					hotelControl.getWholesalerId()));
		}
		if (StringUtils.isNotEmpty(hotelControl.getMemo())) {
			dc.add(Restrictions.like("memo", "%" + hotelControl.getMemo() + "%"));
		}
		if (hotelControl.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy", hotelControl.getCreateBy()));
		}
		if (hotelControl.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate", hotelControl.getCreateDate()));
		}
		if (hotelControl.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy", hotelControl.getUpdateBy()));
		}
		if (hotelControl.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate", hotelControl.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelControl.getDelFlag())) {
			dc.add(Restrictions.like("delFlag", "%" + hotelControl.getDelFlag()
					+ "%"));
		}

		// dc.addOrder(Order.desc("id"));
		return hotelControlDao.find(dc);
	}

	public HotelControl getByUuid(String uuid) {
		return hotelControlDao.getByUuid(uuid);
	}

	public void removeByUuid(String uuid) {
		HotelControl hotelControl = getByUuid(uuid);
		hotelControl.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelControl);
	}

	/**
	 * 保存控房记录 add by zhanghao
	 * 
	 * @param input
	 * @return Map<String,String> key:status 等于success 时成功、等于fail时失败 key:message
	 *         保存失败信息
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> saveHotelControl(HotelControlInput input, String flag, List<HotelAnnex> listAnnex) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put("status", "");
		result.put("message", "");

		// 控房表封装数据，用于批量更新
		List<HotelControlDetail> controlDetails = new ArrayList<HotelControlDetail>();
		List<HotelControlFlightDetail> allFlightDetails = new ArrayList<HotelControlFlightDetail>();
		List<HotelControlRoomDetail> allRoomDetails = new ArrayList<HotelControlRoomDetail>();
		if (UserUtils.getCompanyIdForData() != null) {
			if (input.validateFormInput()) {

				HotelControl hc = input.transfer2HotelControl();
				hc.setWholesalerId(Integer.parseInt(String.valueOf(UserUtils.getCompanyIdForData())));// 批发商ID

				if ("1".equals(flag)) {// 库房新增日期
					HotelControl hotelControl = hotelControlDao.getByUuid(hc.getUuid());
					hotelControl.setCurrencyId(hc.getCurrencyId());

					this.setOptInfo(hotelControl, "update");
					hotelControlDao.updateObj(hotelControl);
				} else {
					this.setOptInfo(hc, null);
					hotelControlDao.saveObj(hc);
				}
				hotelAnnexDao.synDocInfo(hc.getUuid(), HotelAnnex.ANNEX_TYPE_FOR_HOTEL_CONTROL, UserUtils.getCompanyIdForData().intValue(), listAnnex);
				if (CollectionUtils.isNotEmpty(hc.getDetailList())) {
					for (HotelControlDetail hd : hc.getDetailList()) {
						hd.setHotelControlUuid(hc.getUuid());
						hd.setWholesalerId(hc.getWholesalerId());
						this.setOptInfo(hd, null);
						controlDetails.add(hd);

						if (CollectionUtils.isNotEmpty(hd.getFlightList())) {
							for (HotelControlFlightDetail flight : hd
									.getFlightList()) {
								flight.setHotelControlUuid(hc.getUuid());
								flight.setHotelControlDetailUuid(hd.getUuid());
								flight.setWholesalerId(hc.getWholesalerId());
								this.setOptInfo(flight, null);
								allFlightDetails.add(flight);
							}
						}

						if (CollectionUtils.isNotEmpty(hd.getRoomList())) {
							for (HotelControlRoomDetail room : hd.getRoomList()) {
								room.setHotelControlUuid(hc.getUuid());
								room.setHotelControlDetailUuid(hd.getUuid());
								room.setWholesalerId(hc.getWholesalerId());
								this.setOptInfo(room, null);
								allRoomDetails.add(room);
							}
						}
					}

					// 记录酒店控房规则记录并为控房详情设置规则唯一标识
					try {
						this.recordHotelControlRule(controlDetails);
					} catch (Exception e) {
						result.put("message", "3");
						result.put("error", e.getMessage());
						throw e;
					}

					// 批量更新操作
					hotelControlDetailDao.batchSave(controlDetails);
					hotelControlFlightDetailDao.batchSave(allFlightDetails);
					hotelControlRoomDetailDao.batchSave(allRoomDetails);

					if ("1".equals(flag)) {
						result.put("status", "success");
						result.put("message", "2");
					} else {
						result.put("status", "success");
						result.put("message", "1");
					}

					return result;
				} else {
					result.put("status", "fail");
					result.put("message", "提交发布的明细已存在，或者有重复的控房明细信息！");
					return result;
				}

			} else {
				result.put("status", "fail");
				result.put("message", "表单信息验证失败！");
			}
		} else {
			result.put("status", "fail");
			result.put("message", "批发商信息读取失败！");
		}

		return result;
	}

	/**
	 * 修改控房记录 add by zhanghao 修改控房记录前要做唯一性的验证
	 * 
	 * @param input
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public Map<String, String> updateHotelControl(
			HotelControlInput hotelControlInput, boolean updateFlag,
			List<HotelAnnex> axList) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put("message", "");
		result.put("error", "");
		
		// 修改附件
		hotelAnnexDao.synDocInfo(hotelControlInput.getUuid(), HotelAnnex.ANNEX_TYPE_FOR_HOTEL_CONTROL, UserUtils.getCompanyIdForData().intValue(), axList);

		// 读取批发商信息
		if (UserUtils.getCompanyIdForData() == null) {
			result.put("message", "3");
			result.put("error", "批发商信息读取失败！");
			return result;
		}

		// 验证表单数据
		if (!hotelControlInput.validateFormInput()) {
			result.put("message", "3");
			result.put("error", "表单信息验证失败！");
			return result;
		}

		// 控房表封装数据，用于批量更新
		List<HotelControlDetail> controlDetails = new ArrayList<HotelControlDetail>();
		List<HotelControlFlightDetail> allFlightDetails = new ArrayList<HotelControlFlightDetail>();
		List<HotelControlRoomDetail> allRoomDetails = new ArrayList<HotelControlRoomDetail>();

		// 组装表数据
		HotelControl hc = hotelControlInput.transfer2HotelControl();
		HotelControl entity = hotelControlDao.getByUuid(hc.getUuid());
		if (entity != null) {
			entity.setName(hc.getName());
			entity.setCountry(hc.getCountry());
			entity.setIslandUuid(hc.getIslandUuid());
			entity.setHotelUuid(hc.getHotelUuid());
			entity.setHotelGroup(hc.getHotelGroup());
			entity.setCurrencyId(hc.getCurrencyId());
			entity.setGroundSupplier(hc.getGroundSupplier());
			entity.setPurchaseType(hc.getPurchaseType());
			entity.setMemo(hc.getMemo());
			this.setOptInfo(entity, OPERATION_UPDATE);
			hotelControlDao.updateObj(entity);
		}

		if (CollectionUtils.isNotEmpty(hc.getDetailList())) {
			for (HotelControlDetail hd : hc.getDetailList()) {
				if (hd == null) {
					continue;
				}
				HotelControlDetail controlDetail = hotelControlDetailDao.getByUuid(hd.getUuid());
				controlDetail.setInDate(hd.getInDate());
				controlDetail.setIslandWay(hd.getIslandWay());
				controlDetail.setTotalPrice(hd.getTotalPrice());
				controlDetail.setCurrencyId(hd.getCurrencyId());
				controlDetail.setStock(hd.getStock());
				controlDetail.setValidateFlag(hd.getValidateFlag());
				controlDetail.setMemo(hd.getMemo());
				this.setOptInfo(controlDetail, OPERATION_UPDATE);

				List<HotelControlFlightDetail> flightDetails = new ArrayList<HotelControlFlightDetail>();
				List<HotelControlRoomDetail> roomDetails = new ArrayList<HotelControlRoomDetail>();

				if (CollectionUtils.isNotEmpty(hd.getFlightList())) {
					for (HotelControlFlightDetail flight : hd.getFlightList()) {
						HotelControlFlightDetail flightDetail = hotelControlFlightDetailDao.getByUuid(flight.getUuid());
						if (flightDetail == null) {
							flightDetail = new HotelControlFlightDetail();
							flightDetail.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
							flightDetail.setHotelControlUuid(entity.getUuid());
							flightDetail.setHotelControlDetailUuid(controlDetail.getUuid());
							flightDetail.setAirline(flight.getAirline());
							this.setOptInfo(flightDetail, OPERATION_ADD);
							hotelControlFlightDetailDao.saveObj(flightDetail);
							continue;
						}
						flightDetail.setAirline(flight.getAirline());
						this.setOptInfo(flightDetail, OPERATION_UPDATE);
						flightDetails.add(flightDetail);
					}
				}

				if (CollectionUtils.isNotEmpty(hd.getRoomList())) {
					for (HotelControlRoomDetail room : hd.getRoomList()) {
						HotelControlRoomDetail roomDetail = hotelControlRoomDetailDao.getByUuid(room.getUuid());
						if (roomDetail == null) {
							continue;
						}
						roomDetail.setRoomUuid(room.getRoomUuid());
						roomDetail.setNight(room.getNight());
						roomDetail.setHotelMeals(room.getHotelMeals());
						this.setOptInfo(roomDetail, OPERATION_UPDATE);
						roomDetails.add(roomDetail);
					}
				}

				// 用于批量更新
				allFlightDetails.addAll(flightDetails);
				allRoomDetails.addAll(roomDetails);

				// 为控房明细信息装载数据
				controlDetail.setFlightList(flightDetails);
				controlDetail.setRoomList(roomDetails);

				controlDetails.add(controlDetail);
			}
		}

		// 记录酒店控房规则记录并为控房详情设置规则唯一标识
		try {
			this.recordHotelControlRule(controlDetails);
		} catch (Exception e) {
			result.put("message", "3");
			result.put("error", e.getMessage());
			throw e;
		}

		// 批量更新操作
		hotelControlDetailDao.batchUpdate(controlDetails);
		hotelControlFlightDetailDao.batchUpdate(allFlightDetails);
		hotelControlRoomDetailDao.batchUpdate(allRoomDetails);

		result.put("message", "2");
		return result;
	}

	/**
	 * 控房列表方法
	 * 
	 * @author wangxv
	 */
	public Page<Map<String, Object>> hotelControlList(
			HttpServletRequest request, HttpServletResponse response,
			HotelControlQuery hotelControlQuery) {
		String orderBy = request.getParameter("orderBy");
		String activityStatus = request.getParameter("activityStatus");// 0：已提交；1：已保存草稿；2：已删除；
		String showType = request.getParameter("showType");// 1是日期列表，2是酒店列表

		StringBuffer sb = new StringBuffer();
		if ("2".equals(showType)) {
			sb.append(" SELECT hc.uuid AS uuid ,hc.island_uuid AS islandUuid ,hc.hotel_uuid AS hotelUuid,hc.name AS name,GROUP_CONCAT(hcd.uuid) AS hcduuids,hc.ground_supplier AS ground,hc.purchase_type AS purchase,SUM(hcd.stock) AS stock ");
			sb.append(" FROM hotel_control  hc LEFT JOIN hotel_control_detail hcd ON hc.uuid=hcd.hotel_control_uuid AND hcd.delFlag='0'");
			// sb.append(" LEFT JOIN hotel_control_room_detail hcrd ON hcrd.hotel_control_uuid=hc.uuid AND hcrd.delFlag='0'");
			sb.append(" LEFT JOIN hotel_control_flight_detail hcfd ON  hcd.uuid=hcfd.hotel_control_detail_uuid AND hcfd.delFlag='0'");
			sb.append(" LEFT JOIN sys_user su ON su.id=hc.createBy AND su.delFlag = '0'");
			sb.append(" WHERE hc.delFlag='0' AND EXISTS (SELECT 1 FROM hotel_control_detail  hcd  WHERE hcd.hotel_control_uuid = hc.uuid and hcd.delFlag='0')");
			sb.append(" AND hc.wholesaler_id="
					+ UserUtils.getUser().getCompany().getId());
		} else {// 默认查询按日期排列的列表数据
			sb.append(" SELECT hcd.uuid AS uuid, hcd.in_date AS inDate, hcfd.airline AS airLine,hc.island_uuid AS isLand,hc.hotel_uuid AS hotel,hc.ground_supplier AS ground,hc.purchase_type AS purchase,");
			sb.append(" hcd.island_way AS islandWay,hcd.stock AS stock,hcd.total_price AS totalPrice,hcd.createBy AS createBy,hcd.updateBy AS updateBy,");
			sb.append(" hcd.createDate AS createDate,hcd.updateDate AS updateDate,hcd.status AS status,hcd.currency_id AS currencyId FROM  hotel_control_detail hcd");
			sb.append(" LEFT JOIN hotel_control_flight_detail hcfd ON  hcd.uuid=hcfd.hotel_control_detail_uuid AND hcfd.delFlag='0'");
			sb.append(" LEFT JOIN hotel_control hc ON hc.uuid=hcd.hotel_control_uuid AND hc.delFlag='0'");
			// sb.append(" LEFT JOIN hotel_control_room_detail hcrd ON hcrd.hotel_control_detail_uuid=hcd.uuid AND hcrd.delFlag='0'");
			sb.append(" LEFT JOIN sys_user su ON su.id=hcd.createBy  AND su.delFlag = '0'");
			sb.append(" WHERE hcd.delFlag='0'");
			sb.append(" AND hcd.wholesaler_id = "
					+ UserUtils.getUser().getCompany().getId());
		}

		if (!"3".equals(activityStatus)) {
			sb.append(" and  hcd.status=" + activityStatus);
		}
		if (StringUtils.isNotBlank(hotelControlQuery.getGroupOpenDate())) {
			sb.append(" and  hcd.in_date >='"
					+ hotelControlQuery.getGroupOpenDate() + "'");
		}
		if (StringUtils.isNotBlank(hotelControlQuery.getGroupCloseDate())) {
			sb.append(" and  hcd.in_date <='"
					+ hotelControlQuery.getGroupCloseDate() + "'");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getCountry())) {
			sb.append(" and  hc.country ='" + hotelControlQuery.getCountry()
					+ "'");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getIsland())) {
			sb.append(" and  hc.island_uuid ='" + hotelControlQuery.getIsland()
					+ "'");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getHotel())) {
			sb.append(" and  hc.hotel_uuid ='" + hotelControlQuery.getHotel()
					+ "'");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getFlights())) {
			sb.append(" and  hcfd.airline like '%"
					+ hotelControlQuery.getFlights() + "%'");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getIslandway())) {
			sb.append(" and  hcd.island_way ='"
					+ hotelControlQuery.getIslandway() + "'");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getRoomtype())) {
			sb.append("and EXISTS (SELECT 1 FROM hotel_control_room_detail hcrd  "
					+ "WHERE hcrd.hotel_control_uuid = hc.uuid and  hcrd.room_uuid ='"
					+ hotelControlQuery.getRoomtype() + "')");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getRoomnum())) {
			sb.append(" and  hcd.stock " + hotelControlQuery.getRoomnum() + "");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getPublishperson())) {
			sb.append(" and  su.name like '%"
					+ hotelControlQuery.getPublishperson() + "%'");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getHoteljituan())) {
			sb.append(" and  hc.hotel_group ='"
					+ hotelControlQuery.getHoteljituan() + "'");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getFoodtype())) {
			sb.append(" and  hcd.hotel_meal ='"
					+ hotelControlQuery.getFoodtype() + "'");
		}
		if (!StringUtils.isBlank(hotelControlQuery.getControlname())) {
			sb.append(" and  hc.name like '%"
					+ hotelControlQuery.getControlname() + "%'");
		}
		if ("2".equals(showType)) {
			sb.append("  GROUP BY hc.uuid,hc.island_uuid,hc.hotel_uuid,hc.name ");
		}
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,
				response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy(" hcd.status");
		} else {
			page.setOrderBy(orderBy);
		}
		if ("2".equals(showType)) {
			String sql = " select * from ( " + sb.toString() + " ) u";
			return hotelControlDao.findBySql(page, sql, sb.toString(),
					Map.class);
		} else {
			return hotelControlDao.findBySql(page, sb.toString(), Map.class);
		}
	}

	/**
	 * 查询房型和天数
	 * 
	 * @param loop
	 * @author wangxv
	 * @return
	 */
	public List<List<Map<String, String>>> getForDateSubs(
			List<Map<String, Object>> loop) {
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		String sb = "SELECT  room_uuid as room,night as night,hotel_meals as hotelMeals FROM hotel_control_room_detail hcrd";

		if (CollectionUtils.isNotEmpty(loop)) {
			for (Map<String, Object> map : loop) {
				String sql = "  WHERE hcrd.hotel_control_detail_uuid ='"
						+ map.get("uuid") + "'";
				List<Map<String, String>> listMap = hotelControlDao.findBySql(
						sb + sql, Map.class);
				list.add(listMap);
			}
		}
		return list;
	}

	/**
	 * 查找hotelcontroldetail表中数据
	 * 
	 * @author wangxv
	 */
	public List<List<Map<String, String>>> getForHotelSubs(
			List<Map<String, Object>> loop) {
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT hcd.uuid AS uuid,hcd.in_date AS inDate,hcfd.airline AS airLine,hcd.island_way AS islandWay,hcd.stock AS stock,");
		sb.append(" hcd.total_price AS totalPrice,hcd.createBy AS createBy,hcd.updateBy AS updateBy,hcd.createDate AS createDate,hcd.updateDate AS updateDate,");
		sb.append(" hcd.status AS status,GROUP_CONCAT(hcrd.room_uuid) AS room,GROUP_CONCAT(hcrd.night)AS night,GROUP_CONCAT(hcrd.hotel_meals) AS hotelMeals,hcd.currency_id AS currencyId FROM  hotel_control_detail hcd");
		sb.append(" LEFT JOIN hotel_control_flight_detail hcfd ON hcd.uuid=hcfd.hotel_control_detail_uuid AND hcfd.delFlag='0' ");
		sb.append(" LEFT JOIN hotel_control_room_detail hcrd ON hcrd.hotel_control_detail_uuid=hcd.uuid AND hcrd.delFlag='0'");
		sb.append(" WHERE hcd.delFlag='0'  ");
		// sb.append(" LEFT JOIN sys_user su ON su.id=hcd.createBy  AND su.delFlag = '0' ");

		if (CollectionUtils.isNotEmpty(loop)) {
			for (Map<String, Object> map : loop) {
				StringBuffer hcdUuids = new StringBuffer();
				String uuidsArray = (String) map.get("hcduuids");
				String[] uuids = uuidsArray.split(",");
				for (String uuid : uuids) {
					hcdUuids.append("'" + uuid + "'" + ",");
				}
				String sqlin = " AND hcd.uuid IN ("
						+ hcdUuids.deleteCharAt(hcdUuids.length() - 1) + ")";
				String sqlgroup = " GROUP BY  hcd.uuid ,hcd.in_date,hcfd.airline,hcd.hotel_meal,hcd.island_way,hcd.stock,hcd.total_price,hcd.createBy,hcd.updateBy,hcd.createDate ,hcd.updateDate,hcd.status order by hcd.status";
				List<Map<String, String>> listMap = hotelControlDao.findBySql(
						sb.toString() + sqlin + sqlgroup, Map.class);
				list.add(listMap);
			}
		}

		return list;
	}

	/**
	 * 校验控房规则并为酒店控房设置规则UUid
	 * 
	 * @param hotelControlDetails
	 *            酒店控房详情信息
	 * @return boolean
	 * @author majiancheng
	 * @Time 2015-5-15
	 */
	private boolean recordHotelControlRule(List<HotelControlDetail> hotelControlDetails) throws Exception {
		boolean flag = true;
		if(flag) {
			return flag;
		}
		List<HotelControlRule> hotelControlRules = new ArrayList<HotelControlRule>();
		if (CollectionUtils.isNotEmpty(hotelControlDetails)) {

			List<String> md5KeyList = new ArrayList<String>();

			for (HotelControlDetail controlDetail : hotelControlDetails) {
				// 只有状态在已提交的时候才能记录唯一标识规则
				if (controlDetail.getStatus() != HotelControlDetail.STATUS_SUBMIT_FLAG) {
					continue;
				}

				HotelControlRule hotelControlRule = new HotelControlRule();
				// 拼接控房规则字符串
				String hotelControlRuleStr = hotelControlDetailDao
						.getHotelControlRule(controlDetail,
								controlDetail.getRoomList());
				String md5Code = MD5Utils.generateMD5Code(hotelControlRuleStr);

				if (md5KeyList.contains(md5Code)) {
					throw new Exception("控房规则只能唯一，请重新修改控房单");
				} else {
					md5KeyList.add(md5Code);
				}

				hotelControlRule.setHotelControlRule(hotelControlRuleStr);
				hotelControlRule.setRuleUuid(md5Code);
				super.setOptInfo(hotelControlRule, OPERATION_ADD);

				hotelControlRules.add(hotelControlRule);

				if (!hotelControlDetailDao.findRuleIsExist(controlDetail
						.getUuid(), hotelControlRule.getRuleUuid(),
						controlDetail.getWholesalerId().longValue())) {
					controlDetail.setValidateFlag(hotelControlRule
							.getRuleUuid());
				} else {
					throw new Exception("控房规则只能唯一，请重新修改入住日期为："
							+ DateUtils.formatDate(controlDetail.getInDate(),
									DateUtils.DATE_PATTERN_YYYY_MM_DD) + "的控房单");
				}
			}
		}

		// 批量更新
		hotelControlRuleDao.batchSave(hotelControlRules);
		return true;
	}

	/**
	 * 扣减余位 接口定义zhanghao，实现sy 海岛游订单 转报名后 扣减余位操作接口（同时扣减海岛游产品余位和酒店控房的余位
	 * 。注：V1版本不关联机票库存。） 扣减余位之前要判断 当前库存是否满足需要扣除的数量。
	 * 
	 * @param order
	 *            当前操作的订单实体对象
	 * @param detailList
	 *            需要扣减的控房信息结合（集合中的对象属性 hotelControlDetailUuid和num是必填项）
	 * @return Map<String,Object>
	 *         key:result。value：success有剩余库存扣减成功，fail没有库存扣减失败，error参数异常。
	 *         key:message。value:fail和error情况下的异常信息
	 * 
	 */
	public Map<String, Object> deductRemNumber(IslandOrder order,
			List<IslandOrderControlDetail> detailList) {

		Map<String, Object> result = new HashMap<String, Object>();
		
		String uuid = order.getUuid();
		String groupUuid = order.getActivityIslandGroupUuid();

		List<ActivityIslandGroupAirline> airlinelist = activityIslandGroupAirlineService.getAirlineByGroupUuid(groupUuid);

		//订单总人数
		Integer orderRember = order.getOrderPersonNum();

		try {
			for (ActivityIslandGroupAirline airline : airlinelist) {
				int airRemNumber = airline.getRemNumber();
				// 无舱位等级 只有一条航班记录
				if (StringUtils.isBlank(airline.getSpaceLevel())) {
					// 如果 预定人数>余位人数 不能报名
					if (orderRember > airRemNumber) {
						result.put("ret", "fail");
						result.put("msg", "预定人数大于余位数,不能报名.");
						break;
					} else {
						airRemNumber = airRemNumber - orderRember;
						airline.setRemNumber(airRemNumber);
						result.put("ret", "success");
						result.put("msg", "预定人数小于余位数,正常扣减.");
						activityIslandGroupAirlineService.save(airline);
					}
				// 舱位等级不为空 可能存在多条航班记录
				} else {
					List<IslandOrderPrice> orderPriceList = islandOrderPriceService.getOrderPriceByOrderUuid(uuid);
					Map<String,List<IslandOrderPrice>> map = new HashMap<String,List<IslandOrderPrice>>();
					//按舱位等级分组
					for (IslandOrderPrice orderPrice : orderPriceList) {
						if (StringUtils.isNotBlank(orderPrice.getSpaceLevel())) {							
							if(!map.containsKey(orderPrice.getSpaceLevel())){
								map.put(orderPrice.getSpaceLevel(), new ArrayList<IslandOrderPrice>());
							}	
							map.get(orderPrice.getSpaceLevel()).add(orderPrice);
						}
					}
					
					for(String spaceStr:map.keySet()){
						int orderNumTem = 0;
						String priceuuid = map.get(spaceStr).get(0).getActivityIslandGroupPriceUuid();
						for(IslandOrderPrice orderPriceTem:map.get(spaceStr)){
							orderNumTem += orderPriceTem.getNum();
						}
						ActivityIslandGroupPrice groupPrice = activityIslandGroupPriceService.getByUuid(priceuuid);
						if (groupPrice != null) {
							if (airline.getUuid().equals(groupPrice.getActivityIslandGroupAirlineUuid())) {
								if (orderNumTem > airRemNumber) {
									result.put("ret", "fail");
									result.put("msg", "预定人数大于余位数,不能报名.");
								} else {
									airRemNumber = airRemNumber - orderNumTem;
									airline.setRemNumber(airRemNumber);
									result.put("ret", "success");
									result.put("msg", "预定人数小于余位数,正常扣减.");
									activityIslandGroupAirlineService.save(airline);
								}
							}
						}
					}
				}
			}
			// 控房数扣减
			if (CollectionUtils.isNotEmpty(detailList)) {
				for (IslandOrderControlDetail islandOrderControlDetail : detailList) {
					if (islandOrderControlDetail.getNum() != null) {
						HotelControlDetail controlDetail = hotelControlDetailService
								.getByUuid(islandOrderControlDetail
										.getHotelControlDetailUuid());
						if (controlDetail.getStock() >= islandOrderControlDetail
								.getNum()) {
							// 扣减余位之前要判断 当前库存是否满足需要扣除的数量。
							controlDetail.setStock(controlDetail.getStock()
									- islandOrderControlDetail.getNum());
						}
					}
				}
			}
		} catch (Exception e) {
			result.put("ret", "fail");
			result.put("msg", "系统异常,余位扣减失败,请重新操作.");
		}
		return result;
	}

	public List<HotelControlDetailModel> getControlDetailsByHotelUuid(String hotelUuid) {
		List<HotelControlDetailModel> detailModels = hotelControlDetailDao.getControlDetailsByHotelUuid(hotelUuid);

		if (CollectionUtils.isNotEmpty(detailModels)) {
			for (HotelControlDetailModel model : detailModels) {
				model.setRooms(hotelControlRoomDetailDao.getNamesByDetailUuid(model.getHotelControlDetailUuid()));
			}
		}
		return detailModels;
	}
	
	
	public String getHotelRoomMealsData(String hotelUuid) {
		List<HotelRoomMeal> entitys = hotelRoomMealDao.findByHotelUuid(hotelUuid);
		Map<String, List<HotelRoomMeal>> allMealTypesMap = new LinkedHashMap<String, List<HotelRoomMeal>>();
		
		if(CollectionUtils.isNotEmpty(entitys)) {
			for(HotelRoomMeal hotelRoomMeal : entitys) {
				if(allMealTypesMap.get(hotelRoomMeal.getHotelRoomUuid()) == null) {
					List<HotelRoomMeal> hotelRoomMeals = new ArrayList<HotelRoomMeal>();
					hotelRoomMeals.add(hotelRoomMeal);
					
					allMealTypesMap.put(hotelRoomMeal.getHotelRoomUuid(), hotelRoomMeals);
				} else {
					allMealTypesMap.get(hotelRoomMeal.getHotelRoomUuid()).add(hotelRoomMeal);
				}
			}
		}
		
		return JSON.toJSONStringWithDateFormat(allMealTypesMap, "yyyy-MM-dd");
	}
	
	public String getHotelDetailsData(String hotelControlUuid) {
		List<HotelControlRoomDetail> roomDetails = hotelControlRoomDetailDao.getByHotelControlUuid(hotelControlUuid);
		
		//Map结构：Map<房型详情uuid, Map<房型uuid, List<餐型uuid>>>
		Map<String, Map<String, List<String>>> dataMap = new LinkedHashMap<String, Map<String, List<String>>>();
		
		if(CollectionUtils.isNotEmpty(roomDetails)) {
			for(HotelControlRoomDetail roomDetail : roomDetails) {
				Map<String, List<String>> roomMap = new LinkedHashMap<String, List<String>>();
				
				List<String> hotelMealUuids = new ArrayList<String>();
				if(StringUtils.isNotEmpty(roomDetail.getHotelMeals())) {
					String[] hotelMealArr = roomDetail.getHotelMeals().split(";");
					for(String hotelMeal : hotelMealArr) {
						hotelMealUuids.add(hotelMeal);
					}
				}
				
				if(dataMap.get(roomDetail.getUuid()) == null) {
					roomMap.put(roomDetail.getRoomUuid(), hotelMealUuids);
					
					dataMap.put(roomDetail.getUuid(), roomMap);
				} else {
					roomMap = dataMap.get(roomDetail.getUuid());
					
					if(roomMap.get(roomDetail.getRoomUuid()) == null) {
						roomMap.put(roomDetail.getRoomUuid(), hotelMealUuids);
					} else {
						roomMap.get(roomDetail.getRoomUuid()).addAll(hotelMealUuids);
					}
				}
			}
		}
		
		return JSON.toJSONStringWithDateFormat(dataMap, "yyyy-MM-dd");
	}
	
}
