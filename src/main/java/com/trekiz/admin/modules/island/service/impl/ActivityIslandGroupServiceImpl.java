/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityIslandGroupServiceImpl  extends BaseService implements ActivityIslandGroupService{
	@Autowired
	private ActivityIslandGroupDao activityIslandGroupDao;
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;

	public void save (ActivityIslandGroup activityIslandGroup){
		super.setOptInfo(activityIslandGroup, BaseService.OPERATION_ADD);
		activityIslandGroupDao.saveObj(activityIslandGroup);
	}
	
	public void save (ActivityIslandGroupInput activityIslandGroupInput){
		ActivityIslandGroup activityIslandGroup = activityIslandGroupInput.getActivityIslandGroup();
		super.setOptInfo(activityIslandGroup, BaseService.OPERATION_ADD);
		activityIslandGroupDao.saveObj(activityIslandGroup);
	}
	
	public void update (ActivityIslandGroup activityIslandGroup){
		super.setOptInfo(activityIslandGroup, BaseService.OPERATION_UPDATE);
		activityIslandGroupDao.updateObj(activityIslandGroup);
	}
	
	public ActivityIslandGroup getById(java.lang.Integer value) {
		return activityIslandGroupDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityIslandGroup obj = activityIslandGroupDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityIslandGroup> find(Page<ActivityIslandGroup> page, ActivityIslandGroupQuery activityIslandGroupQuery) {
		DetachedCriteria dc = activityIslandGroupDao.createDetachedCriteria();
		
	   	if(activityIslandGroupQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getGroupCode())){
			dc.add(Restrictions.eq("groupCode", activityIslandGroupQuery.getGroupCode()));
		}
		if(activityIslandGroupQuery.getGroupOpenDate()!=null){
			dc.add(Restrictions.eq("groupOpenDate", activityIslandGroupQuery.getGroupOpenDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", activityIslandGroupQuery.getIslandWay()));
		}
	   	if(activityIslandGroupQuery.getAdvNumber()!=null){
	   		dc.add(Restrictions.eq("advNumber", activityIslandGroupQuery.getAdvNumber()));
	   	}
	   	if(activityIslandGroupQuery.getRemNumber()!=null){
	   		dc.add(Restrictions.eq("remNumber", activityIslandGroupQuery.getRemNumber()));
	   	}
	   	if(activityIslandGroupQuery.getSinglePrice()!=null){
	   		dc.add(Restrictions.eq("singlePrice", activityIslandGroupQuery.getSinglePrice()));
	   	}
	   	if(activityIslandGroupQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityIslandGroupQuery.getCurrencyId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getStatus())){
			dc.add(Restrictions.eq("status", activityIslandGroupQuery.getStatus()));
		}
	   	if(activityIslandGroupQuery.getLockStatus()!=null){
	   		dc.add(Restrictions.eq("lockStatus", activityIslandGroupQuery.getLockStatus()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getForcastStatus())){
			dc.add(Restrictions.eq("forcastStatus", activityIslandGroupQuery.getForcastStatus()));
		}
	   	if(activityIslandGroupQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupQuery.getCreateBy()));
	   	}
		if(activityIslandGroupQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupQuery.getCreateDate()));
		}
	   	if(activityIslandGroupQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupQuery.getDelFlag()));
		}
	   	if(activityIslandGroupQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", activityIslandGroupQuery.getFrontMoney()));
	   	}
	   	if(activityIslandGroupQuery.getFrontMoneyCurrencyId()!=null){
	   		dc.add(Restrictions.eq("frontMoneyCurrencyId", activityIslandGroupQuery.getFrontMoneyCurrencyId()));
	   	}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupDao.find(page, dc);
	}
	
	public List<ActivityIslandGroup> find( ActivityIslandGroupQuery activityIslandGroupQuery) {
		DetachedCriteria dc = activityIslandGroupDao.createDetachedCriteria();
		
	   	if(activityIslandGroupQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandGroupQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandGroupQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandGroupQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getGroupCode())){
			dc.add(Restrictions.eq("groupCode", activityIslandGroupQuery.getGroupCode()));
		}
		if(activityIslandGroupQuery.getGroupOpenDate()!=null){
			dc.add(Restrictions.eq("groupOpenDate", activityIslandGroupQuery.getGroupOpenDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getIslandWay())){
			dc.add(Restrictions.eq("islandWay", activityIslandGroupQuery.getIslandWay()));
		}
	   	if(activityIslandGroupQuery.getAdvNumber()!=null){
	   		dc.add(Restrictions.eq("advNumber", activityIslandGroupQuery.getAdvNumber()));
	   	}
	   	if(activityIslandGroupQuery.getRemNumber()!=null){
	   		dc.add(Restrictions.eq("remNumber", activityIslandGroupQuery.getRemNumber()));
	   	}
	   	if(activityIslandGroupQuery.getSinglePrice()!=null){
	   		dc.add(Restrictions.eq("singlePrice", activityIslandGroupQuery.getSinglePrice()));
	   	}
	   	if(activityIslandGroupQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", activityIslandGroupQuery.getCurrencyId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getStatus())){
			dc.add(Restrictions.eq("status", activityIslandGroupQuery.getStatus()));
		}
	   	if(activityIslandGroupQuery.getLockStatus()!=null){
	   		dc.add(Restrictions.eq("lockStatus", activityIslandGroupQuery.getLockStatus()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getForcastStatus())){
			dc.add(Restrictions.eq("forcastStatus", activityIslandGroupQuery.getForcastStatus()));
		}
	   	if(activityIslandGroupQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandGroupQuery.getCreateBy()));
	   	}
		if(activityIslandGroupQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandGroupQuery.getCreateDate()));
		}
	   	if(activityIslandGroupQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandGroupQuery.getUpdateBy()));
	   	}
		if(activityIslandGroupQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandGroupQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandGroupQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandGroupQuery.getDelFlag()));
		}
	   	if(activityIslandGroupQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", activityIslandGroupQuery.getFrontMoney()));
	   	}
	   	if(activityIslandGroupQuery.getFrontMoneyCurrencyId()!=null){
	   		dc.add(Restrictions.eq("frontMoneyCurrencyId", activityIslandGroupQuery.getFrontMoneyCurrencyId()));
	   	}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandGroupDao.find(dc);
	}
	
	public ActivityIslandGroup getByUuid(String uuid) {
		return activityIslandGroupDao.getByUuid(uuid);
	}
	
	public List<ActivityIslandGroup> getByActivityIslandUuid(String ActivityIslandUuid){
		return activityIslandGroupDao.find("from ActivityIslandGroup where activityIslandUuid=? and delFlag=?", ActivityIslandUuid,BaseEntity.DEL_FLAG_NORMAL);
	}
	
	public void removeByUuid(String uuid) {
		ActivityIslandGroup activityIslandGroup = getByUuid(uuid);
		activityIslandGroup.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIslandGroup);
	}
	
	public List<ActivityIslandGroup> findGroupByActivityIslandUuid(String activityIslandUuid) {
		return activityIslandGroupDao.findGroupByActivityIslandUuid(activityIslandUuid);
	}
	
	public int batchUpdateStatusByGroupUuidArr(String[] uuidArray, String status) {
		return activityIslandGroupDao.batchUpdateStatusByGroupUuidArr(uuidArray, status);
	}
	
	public int getRemNumberByGroupAirlineList(List<ActivityIslandGroupAirline> groupAirlineList) {
		int remNumber = 0;
		if(CollectionUtils.isNotEmpty(groupAirlineList)) {
			for(ActivityIslandGroupAirline groupAirline : groupAirlineList) {
				if(groupAirline.getRemNumber() != null) {
					remNumber += groupAirline.getRemNumber();
				}
			}
		}
		return remNumber;
	}
	
	/**
	 * 根据团期UUID获取团期余位数 Title: getRemNumberByGroupAirlineList
	 * 
	 * @return int 返回类型
	 * @author hhx
	 * @date 2015-6-12 下午5:23:26
	 * @throws
	 */
	public int getRemNumberByGroupUuid(String groupUuid) {
		List<ActivityIslandGroupAirline> groupAirlines = activityIslandGroupAirlineService
				.getByactivityIslandGroup(groupUuid);
		int remNumber = 0;
		if (CollectionUtils.isNotEmpty(groupAirlines)) {
			for (ActivityIslandGroupAirline groupAirline : groupAirlines) {
				if (groupAirline.getRemNumber() != null) {
					remNumber += groupAirline.getRemNumber();
				}
			}
		}
		return remNumber;
	}

	@Override
	public List<Map<String, Object>> getProductInfoForSettleForcast(String groupUUID) {
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
		   .append(" activity_island_group g left join island_order o on o.activity_island_group_uuid = g.uuid")
		   .append(" and (o.payStatus not in (99,111) or o.payStatus is null),")
		   .append(" activity_island p")
		   .append(" WHERE p.uuid = g.activity_island_uuid and p.wholesaler_id=").append(companyId)
		   .append(" and g.uuid = '").append(groupUUID).append("'");
		return activityIslandGroupDao.findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getRefundInfoForcastAndSettle(
			Integer budgetType, String groupUUID, Integer orderType) {
		 StringBuffer str = new StringBuffer();
		 str.append("SELECT activity_island_group_uuid,saler,agentName,")
		    .append(" IFNULL(totalMoney, 0) AS totalMoney,")
		    .append(" IFNULL(accountedMoney, 0) AS accountedMoney,")
		    .append(" IFNULL((totalMoney - accountedMoney), 0) AS notAccountedMoney,")
		    .append(" IFNULL(r.refundprice, 0) AS refundprice")
		    .append(" FROM (")
		    .append(" SELECT o.activity_island_group_uuid,")
		    .append(" (SELECT su.NAME FROM sys_user su WHERE su.id = o.salerId) AS saler,")
		    .append(" (SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany) AS agentName,")
		    .append(" (SELECT ifnull(sum(m1.amount * m1.exchangerate), 0) FROM island_money_amount m1 ")
		    .append(" WHERE m1.serialNum = o.total_money) AS totalMoney,")
		    .append(" (SELECT ifnull(sum(m2.amount * m2.exchangerate), 0) FROM island_money_amount m2 ")
		    .append(" WHERE m2.serialNum = o.accounted_money) AS accountedMoney ")
		    .append(" FROM ")
		    .append(" island_order o")
		    .append(" WHERE o.delFlag = '0' ")
		    //.append(" AND o.payStatus NOT IN (99, 111)")暂不使用此字段
		    .append(" AND o.activity_island_group_uuid = '").append(groupUUID).append("'")
		    .append(" ) t1 LEFT JOIN (")
		    .append(" SELECT cost.activity_uuid,sum(cost.price) AS refundprice ")
		    .append(" FROM cost_record_island cost ")
		    .append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType =").append(orderType)
		    .append(" AND cost.budgetType = ").append(budgetType);
		 if(budgetType == 1){
			 str.append(" AND cost.reviewStatus not in ('已取消','已驳回') ");
		 }
		 str.append(" GROUP BY cost.activity_uuid ")
		    .append(" ) r on r.activity_uuid = t1.activity_island_group_uuid ");
		 return activityIslandGroupDao.findBySql(str.toString(), Map.class);
	}

	@Override
	public List<ActivityIslandGroup> find(String groupCode) {
		DetachedCriteria dc = activityIslandGroupDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(groupCode)){
	   		dc.add(Restrictions.like("groupCode", groupCode+"%"));
	   	}
		dc.add(Restrictions.eq("delFlag", "0")); // 未删除的团期
		dc.add(Restrictions.eq("status", ActivityIslandGroup.STATUS_PUTAWAY_FLAG+"")); // 正在上架状态的团期
		List<ActivityIslandGroup> list = activityIslandGroupDao.find(dc);
		
		// 只保留前十位团期号
		if(list!=null && !list.isEmpty() && list.size()>10){
			List<ActivityIslandGroup> back = new ArrayList<ActivityIslandGroup>();
			Iterator<ActivityIslandGroup> iter = list.iterator();
			int n = 0;
			while(iter.hasNext()){
				if(n<10){
					back.add(iter.next());
					n++;
				}else{
					break;
				}
			}
			return back;
		}else{
			return list;
		}
	}
}
