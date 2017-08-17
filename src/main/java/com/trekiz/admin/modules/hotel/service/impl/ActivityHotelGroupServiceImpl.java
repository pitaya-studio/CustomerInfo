/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityHotelGroupServiceImpl  extends BaseService implements ActivityHotelGroupService{
	@Autowired
	private ActivityHotelGroupDao activityHotelGroupDao;

	public void save (ActivityHotelGroup activityHotelGroup){
		super.setOptInfo(activityHotelGroup, BaseService.OPERATION_ADD);
		activityHotelGroupDao.saveObj(activityHotelGroup);
	}
	
	public void save (ActivityHotelGroupInput activityHotelGroupInput){
		ActivityHotelGroup activityHotelGroup = activityHotelGroupInput.getActivityHotelGroup();
		super.setOptInfo(activityHotelGroup, BaseService.OPERATION_ADD);
		activityHotelGroupDao.saveObj(activityHotelGroup);
	}
	
	public void update (ActivityHotelGroup activityHotelGroup){
		super.setOptInfo(activityHotelGroup, BaseService.OPERATION_UPDATE);
		activityHotelGroupDao.updateObj(activityHotelGroup);
	}
	
	public ActivityHotelGroup getById(java.lang.Integer value) {
		return activityHotelGroupDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityHotelGroup obj = activityHotelGroupDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityHotelGroup> find(Page<ActivityHotelGroup> page, ActivityHotelGroupQuery activityHotelGroupQuery) {
		DetachedCriteria dc = activityHotelGroupDao.createDetachedCriteria();
		
	   	if(activityHotelGroupQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getGroupCode())){
			dc.add(Restrictions.eq("groupCode", activityHotelGroupQuery.getGroupCode()));
		}
		if(activityHotelGroupQuery.getGroupOpenDate()!=null){
			dc.add(Restrictions.eq("groupOpenDate", activityHotelGroupQuery.getGroupOpenDate()));
		}
		if(activityHotelGroupQuery.getGroupEndDate()!=null){
			dc.add(Restrictions.eq("groupEndDate", activityHotelGroupQuery.getGroupEndDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", activityHotelGroupQuery.getIslandWay()));
		}
	   	if(activityHotelGroupQuery.getSinglePrice()!=null){
	   		dc.add(Restrictions.eq("singlePrice", activityHotelGroupQuery.getSinglePrice()));
	   	}
	   	if(activityHotelGroupQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityHotelGroupQuery.getCurrencyId()));
	   	}
	   	if(activityHotelGroupQuery.getSinglePriceUnit()!=null){
	   		dc.add(Restrictions.eq("singlePriceUnit", activityHotelGroupQuery.getSinglePriceUnit()));
	   	}
	   	if(activityHotelGroupQuery.getControlNum()!=null){
	   		dc.add(Restrictions.eq("controlNum", activityHotelGroupQuery.getControlNum()));
	   	}
	   	if(activityHotelGroupQuery.getUncontrolNum()!=null){
	   		dc.add(Restrictions.eq("uncontrolNum", activityHotelGroupQuery.getUncontrolNum()));
	   	}
	   	if(activityHotelGroupQuery.getRemNumber()!=null){
	   		dc.add(Restrictions.eq("remNumber", activityHotelGroupQuery.getRemNumber()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getAirline())){
			dc.add(Restrictions.eq("airline", activityHotelGroupQuery.getAirline()));
		}
	   	if(activityHotelGroupQuery.getPriorityDeduction()!=null){
	   		dc.add(Restrictions.eq("priorityDeduction", activityHotelGroupQuery.getPriorityDeduction()));
	   	}
	   	if(activityHotelGroupQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", activityHotelGroupQuery.getFrontMoney()));
	   	}
	   	if(activityHotelGroupQuery.getFrontMoneyCurrencyId()!=null){
	   		dc.add(Restrictions.eq("frontMoneyCurrencyId", activityHotelGroupQuery.getFrontMoneyCurrencyId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getMemo())){
			dc.add(Restrictions.eq("memo", activityHotelGroupQuery.getMemo()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getStatus())){
			dc.add(Restrictions.eq("status", activityHotelGroupQuery.getStatus()));
		}
	   	if(activityHotelGroupQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupQuery.getCreateBy()));
	   	}
		if(activityHotelGroupQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupQuery.getCreateDate()));
		}
	   	if(activityHotelGroupQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupDao.find(page, dc);
	}
	
	public List<ActivityHotelGroup> find( ActivityHotelGroupQuery activityHotelGroupQuery) {
		DetachedCriteria dc = activityHotelGroupDao.createDetachedCriteria();
		
	   	if(activityHotelGroupQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityHotelGroupQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityHotelGroupQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getActivityHotelUuid())){
			dc.add(Restrictions.eq("activityHotelUuid", activityHotelGroupQuery.getActivityHotelUuid()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getGroupCode())){
			dc.add(Restrictions.eq("groupCode", activityHotelGroupQuery.getGroupCode()));
		}
		if(activityHotelGroupQuery.getGroupOpenDate()!=null){
			dc.add(Restrictions.eq("groupOpenDate", activityHotelGroupQuery.getGroupOpenDate()));
		}
		if(activityHotelGroupQuery.getGroupEndDate()!=null){
			dc.add(Restrictions.eq("groupEndDate", activityHotelGroupQuery.getGroupEndDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", activityHotelGroupQuery.getIslandWay()));
		}
	   	if(activityHotelGroupQuery.getSinglePrice()!=null){
	   		dc.add(Restrictions.eq("singlePrice", activityHotelGroupQuery.getSinglePrice()));
	   	}
	   	if(activityHotelGroupQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityHotelGroupQuery.getCurrencyId()));
	   	}
	   	if(activityHotelGroupQuery.getSinglePriceUnit()!=null){
	   		dc.add(Restrictions.eq("singlePriceUnit", activityHotelGroupQuery.getSinglePriceUnit()));
	   	}
	   	if(activityHotelGroupQuery.getControlNum()!=null){
	   		dc.add(Restrictions.eq("controlNum", activityHotelGroupQuery.getControlNum()));
	   	}
	   	if(activityHotelGroupQuery.getUncontrolNum()!=null){
	   		dc.add(Restrictions.eq("uncontrolNum", activityHotelGroupQuery.getUncontrolNum()));
	   	}
	   	if(activityHotelGroupQuery.getRemNumber()!=null){
	   		dc.add(Restrictions.eq("remNumber", activityHotelGroupQuery.getRemNumber()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getAirline())){
			dc.add(Restrictions.eq("airline", activityHotelGroupQuery.getAirline()));
		}
	   	if(activityHotelGroupQuery.getPriorityDeduction()!=null){
	   		dc.add(Restrictions.eq("priorityDeduction", activityHotelGroupQuery.getPriorityDeduction()));
	   	}
	   	if(activityHotelGroupQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", activityHotelGroupQuery.getFrontMoney()));
	   	}
	   	if(activityHotelGroupQuery.getFrontMoneyCurrencyId()!=null){
	   		dc.add(Restrictions.eq("frontMoneyCurrencyId", activityHotelGroupQuery.getFrontMoneyCurrencyId()));
	   	}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getMemo())){
			dc.add(Restrictions.eq("memo", activityHotelGroupQuery.getMemo()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getStatus())){
			dc.add(Restrictions.eq("status", activityHotelGroupQuery.getStatus()));
		}
	   	if(activityHotelGroupQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityHotelGroupQuery.getCreateBy()));
	   	}
		if(activityHotelGroupQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityHotelGroupQuery.getCreateDate()));
		}
	   	if(activityHotelGroupQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityHotelGroupQuery.getUpdateBy()));
	   	}
		if(activityHotelGroupQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityHotelGroupQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityHotelGroupQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityHotelGroupQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityHotelGroupDao.find(dc);
	}
	
	public ActivityHotelGroup getByUuid(String uuid) {
		return activityHotelGroupDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityHotelGroup activityHotelGroup = getByUuid(uuid);
		activityHotelGroup.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityHotelGroup);
	}

	@Override
	public List<ActivityHotelGroup> findGroupsByActivityHotelUuid(String uuid) {
		return activityHotelGroupDao.findGroupsByActivityHotelUuid(uuid);
		
	}

	@Override
	public List<Map<String, Object>> getProductInfoForSettleForcast(
			String groupUUID) {
		StringBuffer str = new StringBuffer();
		Long companyId = UserUtils.getUser().getCompany().getId();
		str.append("SELECT")
		   .append(" (SELECT su.`name` FROM sys_user su WHERE su.id = g.createBy) AS createBy,")
		   .append(" g.createBy AS createById,")
		   .append(" g.groupCode,p.activityName as productName,")
		   .append(" SUM(o.orderPersonNum) as orderPersonNum,")
		   .append(" g.groupOpenDate,g.groupEndDate as groupCloseDate,")
		   .append(" g.lockStatus, g.forcastStatus")
		   .append(" FROM ")
		   .append(" activity_hotel_group g left join hotel_order o on ")
		   .append(" o.activity_hotel_group_uuid = g.uuid ")
		   .append(" and (o.payStatus not in (99,111) or o.payStatus is null), ")
		   .append(" activity_hotel p")
		   .append(" WHERE p.uuid = g.activity_hotel_uuid and p.wholesaler_id=").append(companyId)
		   .append(" and g.uuid = '").append(groupUUID).append("'");
		return activityHotelGroupDao.findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getRefundInfoForcastAndSettle(
			Integer budgetType, String groupUUID, Integer orderType) {
		StringBuffer str = new StringBuffer();
		 str.append("SELECT activity_hotel_group_uuid,saler,agentName,")
		    .append(" IFNULL(totalMoney, 0) AS totalMoney,")
		    .append(" IFNULL(accountedMoney, 0) AS accountedMoney,")
		    .append(" IFNULL((totalMoney - accountedMoney), 0) AS notAccountedMoney,")
		    .append(" IFNULL(r.refundprice, 0) AS refundprice")
		    .append(" FROM (")
		    .append(" SELECT o.activity_hotel_group_uuid,")
		    .append(" (SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId) AS saler,")
		    .append(" (SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany) AS agentName,")
		    .append(" (SELECT ifnull(sum(m1.amount * m1.exchangerate), 0) FROM hotel_money_amount m1 ")
		    .append(" WHERE m1.serialNum = o.total_money) AS totalMoney,")
		    .append(" (SELECT ifnull(sum(m2.amount * m2.exchangerate), 0) FROM hotel_money_amount m2 ")
		    .append(" WHERE m2.serialNum = o.accounted_money) AS accountedMoney ")
		    .append(" FROM ")
		    .append(" hotel_order o")
		    .append(" WHERE o.delFlag = '0' ")
		    //.append(" AND o.payStatus NOT IN (99, 111)") 暂不使用此字段
		    .append(" AND o.activity_hotel_group_uuid = '").append(groupUUID).append("'")
		    .append(" ) t1 LEFT JOIN (")
		    .append(" SELECT cost.activity_uuid,sum(cost.price) AS refundprice ")
		    .append(" FROM cost_record_hotel cost ")
		    .append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType =").append(orderType)
		    .append(" AND cost.budgetType = ").append(budgetType);
		 if(budgetType == 1){
			 str.append(" AND cost.reviewStatus not in ('已取消','已驳回') ");
		 }
		 str.append(" GROUP BY cost.activity_uuid ")
		    .append(" ) r on r.activity_uuid = t1.activity_hotel_group_uuid ");
		 return activityHotelGroupDao.findBySql(str.toString(), Map.class);
	}

	/**
	 * 根据产品的uuid来跟新产品下团期的状态
	 * @param uuid
	 * @param status
	 * @return
	 */
	public int updateGroupStatusByActivityUuid(String uuid, String status) {
		return activityHotelGroupDao.updateGroupStatusByActivityUuid(getUuidString(uuid, ";"), status);
	}
	/**
	 * 根据uuid来更新团期的状态
	 * @param uuid
	 * @param status
	 * @return
	 */
	public int updateGroupStatusByUuid(String uuid, String status) {
		return activityHotelGroupDao.updateGroupStatusByUuid(getUuidString(uuid,";"), status);
	}
	
	/**
	 * 拼接uuid字符串
	 * @param uuid
	 * @return
	 */
	private String getUuidString(String uuid,String regex){
		String result = "";
		if(StringUtils.isNotBlank(uuid)){
			String[] array = uuid.split(regex);
			for(String s:array){
				result+="'"+s+"',";
			}
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
}
