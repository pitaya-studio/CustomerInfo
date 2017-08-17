package com.trekiz.admin.modules.activity.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;

/**
 * 旅游出团信息DAO接口
 * @author liangjingming
 *
 */
public interface ActivityGroupDao extends ActivityGroupDaoCustom,CrudRepository<ActivityGroup, Long>{

	@Query("from ActivityGroup where srcActivityId = ?1 and delFlag ='"+ActivityGroup.DEL_FLAG_NORMAL+"' order by groupOpenDate asc")
	public List<ActivityGroup> findGroupByActivityId(Integer srcActivityId);

	@Query("from ActivityGroup where srcActivityId = ?1 and delFlag ='"+ActivityGroup.DEL_FLAG_NORMAL+"' and is_t1 = 1 order by groupOpenDate asc")
	public List<ActivityGroup> findT1GroupByActivityId(Integer srcActivityId);
	
	@Query("from ActivityGroup where srcActivityId = ?1 and delFlag ='"+ActivityGroup.DEL_FLAG_NORMAL+"' and is_t1 = 1 and suggestAdultPrice > 0 and groupOpenDate >= CURRENT_DATE() order by groupOpenDate asc")
	public List<ActivityGroup> findWeixinGroupByActivityId(Integer srcActivityId);
	
	@Query(value="select * from activitygroup where srcActivityId = ?1 and delFlag ='"+ActivityGroup.DEL_FLAG_NORMAL+"' order by groupOpenDate asc",nativeQuery=true)
	public List<Object[]> findGroupByActivityId(Long srcActivityId);
	
	@Modifying
	@Query("update ActivityGroup set nowLevel=1,review = ?2 where id = ?1 ")
	public void submitReview(Long id, Integer review);
	
	@Modifying
	@Query("update ActivityGroup set review = ?2,nowLevel=?3 where id = ?1 and review!=2")
	public void updateReview(Long id, Integer review,Integer nowLevel);
	
	@Modifying
	@Query("update ActivityGroup set delFlag ='" + ActivityGroup.DEL_FLAG_DELETE + "' where id in ?1")
	public void delGroupsByIds(List<Long> ids);
	
	@Modifying
	@Query("update ActivityGroup set delFlag ='" + ActivityGroup.DEL_FLAG_DELETE + "' where id = ?1")
	public void delGroupById(Long id);
	
	@Query("from ActivityGroup where id<> ?3 and srcActivityId=?1 and groupOpenDate=?2 and delFlag ='" + ActivityGroup.DEL_FLAG_NORMAL + "'")
	public List<ActivityGroup> groupOpenDateRepeat(Integer activityId, Date groupOpenDate, Long groupid);
	
	@Query("from ActivityGroup where groupCode=?1 and delFlag = '" + ActivityGroup.DEL_FLAG_NORMAL + "' and srcActivityId in (select id from TravelActivity where proCompany = ?2 and delFlag = '" + ActivityGroup.DEL_FLAG_NORMAL + "')")
	public List<ActivityGroup> findByGroupCodeAndCompany(String groupCode, Long companyId);
	
	@Query("from ActivityGroup where id=?1 and delFlag = '" + ActivityGroup.DEL_FLAG_NORMAL + "' and srcActivityId in (select id from TravelActivity where proCompany = ?2 and delFlag = '" + ActivityGroup.DEL_FLAG_NORMAL + "')")
	public List<ActivityGroup> findByGroupIdAndCompany(Long groupId, Long companyId);
	
	@Query("from ActivityGroup where delFlag = '" + ActivityGroup.DEL_FLAG_NORMAL + "' and srcActivityId in (select id from TravelActivity where activityKind = ?1 and proCompany = ?2 and delFlag = '" + ActivityGroup.DEL_FLAG_NORMAL + "')")
	public List<ActivityGroup> findByProductTypeAndCompany(Integer productType, Long companyId);
	
	@Query(value="select _nextval(?1)",nativeQuery=true)
	public Object getMaxCountForSequence(Long companyId);
	@Query(value="select _nextval_new(?1,?2)",nativeQuery=true)
	public Object getMaxCountForSequenceNew(Long companyId,int date);
	
	@Query("select id from ActivityGroup where currency_type like ?1")
	public List<Integer> findIdsByCurrencyType(String CurrencyId);
	
	/**
	 * 通过ActivityGroup id更新ActivityGroup对应数据的costStatus属性
	 * @author lihua.xu
	 * @param groupId ActivityGroup id
	 * @param costStatus costStatus值
	 */
	@Modifying
	@Query("update ActivityGroup set costStatus=?2 where id=?1")
	public void resetCostStatus(Long groupId,Integer costStatus);

	@Query("from ActivityGroup where groupCode like ?1 and createBy.id = ?2 ORDER BY id desc")
	public List<ActivityGroup> findGroupCodeByUserId(String groupCode, Long userId);
	
	/**
	 * 更新计算成本状态
	 * @zhaohaiming
	 * */
	@Modifying
	@Query("update ActivityGroup set iscommission=?2 where id=?1")
	public void updateIsCommission(Long groupId,int isCommission);

	@Query("from ActivityGroup where delFlag = ?2 and srcActivityId = ?1")
	List<ActivityGroup> findBySrcActivityIdAndDelFlag(Integer id, String delFlag);
	
	@Query("from ActivityGroup where id = ?1")
	ActivityGroup findById(Long id);

	@Modifying
	@Query("update ActivityGroup set pricingStrategyStatus = ?2 where id = ?1")
	public void updatePSStatusById(Long groupId, Integer status);

}

/**
* 自定义DAO接口
* @author liangjingming
*
*/
interface ActivityGroupDaoCustom extends BaseDao<ActivityGroup>{
	int updateByOptLock(ActivityGroup activityGroup, String versionString);
	/**
	 * 修改六个字段：产品的预收人数、产品的剩余位置、各渠道总的占位人数、各渠道总的切位人数、售出占位、售出切位
	 * @param activityGroup
	 * @param versionString
	 * @return
	 */
	int updatePositionNumByOptLock(ActivityGroup activityGroup, String versionString);
	
	/**
	 * 查询批发商下某类型的所有产品的部分信息 。主要信息：产品id/产品名称/团期id/团号/出团日期/截团日期/产品发布人 等。
	 * @param companyId 批发商id
	 * @param activityType 产品类型
	 * @return
	 */
	List<Map<String, Object>> getPartInfoByTypeAndCompany(Long companyId, Integer activityType);
	
	public List<Map<String, Object>> getPartInfoByTypeAndCompany(
			Long companyId, Integer activityType, String groupCode,
			String activityName, String groupOpenDate, String groupCloseDate,
			String creator);
	
	public List<Map<String, Object>> getAllCreators(Long companyId,
			Integer activityType);
	
	public List<Map<String, Object>> getPartInfoByGroupAndActivity(
			Long companyId, Integer activityType, String activityIdStr, String groupIdStr);
}

/**
* 自定义DAO接口实现
* @author liangjingming
*
*/
@Repository
class ActivityGroupDaoImpl extends BaseDaoImpl<ActivityGroup> implements ActivityGroupDaoCustom{

	@Override
	public int updateByOptLock(ActivityGroup activityGroup, String versionString) {
		int row = 0;
		Integer freePosition = activityGroup.getFreePosition();
		Date groupCloseDate = activityGroup.getGroupCloseDate();
		Date groupOpenDate = activityGroup.getGroupOpenDate();
//		Integer nopayReservePosition = activityGroup.getNopayReservePosition();
		BigDecimal payDeposit = activityGroup.getPayDeposit();
//		Integer payReservePosition = activityGroup.getPayReservePosition();
		Integer planPosition = activityGroup.getPlanPosition();
		BigDecimal settlementAdultPrice = activityGroup.getSettlementAdultPrice();
		BigDecimal settlementcChildPrice = activityGroup.getSettlementcChildPrice();
		BigDecimal singleDiff = activityGroup.getSingleDiff();
//		Integer soldNopayPosition = activityGroup.getSoldNopayPosition();
//		Integer soldPayPosition = activityGroup.getSoldPayPosition();
		BigDecimal suggestAdultPrice = activityGroup.getSuggestAdultPrice();
		BigDecimal suggestChildPrice = activityGroup.getSuggestChildPrice();
		String visaCountry = activityGroup.getVisaCountry();
		Date visaDate = activityGroup.getVisaDate();
		
		
		
		
		String versionNumberOld = activityGroup.getVersionNumber();
		row = this.update("update ActivityGroup set " +
				"versionNumber = ? , " +
				"planPosition = ? , " +
				"freePosition = ? , " +
				"groupOpenDate = ? , " +
				"groupCloseDate = ? , " +
				"visaCountry = ? , " +
				"visaDate = ? , " +
				"settlementAdultPrice = ? , " +
				"settlementcChildPrice = ? , " +
				"suggestAdultPrice = ? , " +
				"suggestChildPrice = ? , " +
				"singleDiff = ? , " +
				"payDeposit = ? " +
				"where versionNumber = ? and id = ? ",
//				"where versionNumber = ? and id = ? and " +
//				"delFlag ='" + ActivityGroup.DEL_FLAG_NORMAL + "'", 
				versionString, 
				planPosition, freePosition, 
				groupOpenDate, groupCloseDate, 
				visaCountry, visaDate, 
				settlementAdultPrice, settlementcChildPrice,
				suggestAdultPrice, suggestChildPrice,
				singleDiff, payDeposit,
				versionNumberOld, activityGroup.getId());
		return row;
	}

	@Override
	public int updatePositionNumByOptLock(ActivityGroup activityGroup,
			String versionString) {
		int row = 0;
		Integer freePosition = activityGroup.getFreePosition();//剩余位置
		Integer nopayReservePosition = activityGroup.getNopayReservePosition();//各渠道总的占位人数
		Integer payReservePosition = activityGroup.getPayReservePosition();//各渠道总的切位人数
		Integer planPosition = activityGroup.getPlanPosition();//预收人数
		Integer soldNopayPosition = activityGroup.getSoldNopayPosition();//售出占位
		Integer soldPayPosition = activityGroup.getSoldPayPosition();//售出切位
		
		
		String versionNumberOld = activityGroup.getVersionNumber();
		row = this.update("update ActivityGroup set " +
				"versionNumber = ? , " +
				"planPosition = ? , " +
				"freePosition = ? , " +
				"nopayReservePosition = ? , " +
				"payReservePosition = ? , " +
				"soldNopayPosition = ? , " +
				"soldPayPosition = ? " +
				"where versionNumber = ? and id = ? ",
//				"where versionNumber = ? and id = ? and " +
//				"delFlag ='" + ActivityGroup.DEL_FLAG_NORMAL + "'", 
				versionString, 
				planPosition, freePosition, 
				nopayReservePosition, payReservePosition, 
				soldNopayPosition, soldPayPosition, 
				versionNumberOld, activityGroup.getId());
		return row;
	}

	@Override
	public List<Map<String, Object>> getPartInfoByTypeAndCompany(Long companyId, Integer activityType) {
		
		List<Map<String, Object>> list = new ArrayList<>();
		StringBuffer sqlStr = new StringBuffer();
		if (companyId != null && activityType != null) {
			sqlStr.append(" SELECT ag.id as groupId, ag.groupCode, ta.id as activityId, ta.acitivityName, ag.groupOpenDate, ");
			sqlStr.append(" IFNULL(ag.groupCloseDate,'0000-00-00') as groupCloseDate, ta.createBy AS activityCreatorId, u.`name` as activityCreator ");
			sqlStr.append(" FROM travelactivity ta ");
			sqlStr.append(" JOIN activitygroup ag ON ta.id = ag.srcActivityId ");
			sqlStr.append(" JOIN sys_user u ON ta.createBy = u.id ");
			sqlStr.append(" WHERE ta.proCompany = " + companyId);
			sqlStr.append(" AND ta.activity_kind = " + activityType);
		}
		list = findBySql(sqlStr.toString(), Map.class);
		
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getPartInfoByTypeAndCompany(
			Long companyId, Integer activityType, String groupCode,
			String activityName, String groupOpenDate, String groupCloseDate,
			String creator) {
		
		List<Map<String, Object>> list = new ArrayList<>();
		StringBuffer sqlStr = new StringBuffer();
		if (companyId != null && activityType != null) {
			
			// 单团类，必须要有groupId
			if (Context.ACTIVITY_KINDS_DT.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_SP.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_YX.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_DKH.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_ZYX.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_YL.equals(activityType.toString())) {
				
				sqlStr.append(" SELECT ag.id as groupId, ag.groupCode, ta.id as activityId, ta.acitivityName, IFNULL(ag.groupOpenDate,'0000-00-00') as groupOpenDate, ");
				sqlStr.append(" IFNULL(ag.groupCloseDate,'0000-00-00') as groupCloseDate, ta.createBy AS activityCreatorId, u.`name` as activityCreator ");
				sqlStr.append(" FROM travelactivity ta ");
				sqlStr.append(" JOIN activitygroup ag ON ta.id = ag.srcActivityId ");
				sqlStr.append(" JOIN sys_user u ON ta.createBy = u.id ");
				sqlStr.append(" WHERE ta.proCompany = " + companyId);
				sqlStr.append(" AND ta.activity_kind = " + activityType);
				if (StringUtils.isNotBlank(groupCode)) {				
					sqlStr.append(" AND ag.groupCode like '%" + groupCode.trim() + "%'");
				}
				if (StringUtils.isNotBlank(activityName)) {
					sqlStr.append(" AND ta.acitivityName like '%" + activityName + "%'");
				}
				if (StringUtils.isNotBlank(groupOpenDate)) {
					sqlStr.append(" AND ag.groupOpenDate > '" + groupOpenDate + "'");
				}
				if (StringUtils.isNotBlank(groupCloseDate)) {				
					sqlStr.append(" AND ag.groupCloseDate < '" + groupCloseDate + "'");
				}
				if (StringUtils.isNotBlank(creator)) {				
					sqlStr.append(" AND ag.createBy = " + creator);
				}
				
			} else if (Context.ACTIVITY_KINDS_JP.equals(activityType.toString())) {  // 机票必须要有 activityId
				
				sqlStr.append(" SELECT ta.id AS groupId, IFNULL(ta.product_code, '') AS groupCode, ta.id AS activityId, IFNULL(ta.activity_airticket_name,'') AS acitivityName, IFNULL(ta.groupOpenDate,'') as groupOpenDate, ");
				sqlStr.append(" '' AS groupCloseDate, ta.createBy AS activityCreatorId, u.`name` AS activityCreator ");
				sqlStr.append(" FROM activity_airticket ta ");
				sqlStr.append(" JOIN sys_user u ON ta.createBy = u.id ");
				sqlStr.append(" WHERE ta.proCompany = " + companyId);
				if (StringUtils.isNotBlank(groupCode)) {				
					sqlStr.append(" AND ta.product_code like '%" + groupCode.trim() + "%'");
				}
				if (StringUtils.isNotBlank(activityName)) {
					sqlStr.append(" AND ta.activity_airticket_name like '%" + activityName + "%'");
				}
				if (StringUtils.isNotBlank(groupOpenDate)) {
					sqlStr.append(" AND ta.groupOpenDate > '" + groupOpenDate + "'");
				}
				// 机票没有截团日期
				if (StringUtils.isNotBlank(creator)) {				
					sqlStr.append(" AND ta.createBy = " + creator);
				}
				
			} else if (Context.ACTIVITY_KINDS_QZ.equals(activityType.toString())) {  // 签证必须要有 activityId
				
				sqlStr.append(" SELECT ta.id AS groupId, IFNULL(ta.groupCode, '') AS groupCode, ta.id AS activityId, ta.productName AS acitivityName, '' AS groupOpenDate, ");
				sqlStr.append(" '' AS groupCloseDate, ta.createBy AS activityCreatorId, u.`name` AS activityCreator ");
				sqlStr.append(" FROM visa_products ta ");
				sqlStr.append(" JOIN sys_user u ON ta.createBy = u.id ");
				sqlStr.append(" WHERE ta.proCompanyId = " + companyId);
				if (StringUtils.isNotBlank(groupCode)) {				
					sqlStr.append(" AND ta.groupCode like '%" + groupCode.trim() + "%'");
				}
				if (StringUtils.isNotBlank(activityName)) {
					sqlStr.append(" AND ta.productName like '%" + activityName + "%'");
				}
				if (StringUtils.isNotBlank(creator)) {				
					sqlStr.append(" AND ag.createBy = " + creator);
				}
			} else {
				// TODO
			}
			
		}
		list = findBySql(sqlStr.toString(), Map.class);
		
		return list;
		
	}

	@Override
	public List<Map<String, Object>> getAllCreators(Long companyId,
			Integer activityType) {
		
		List<Map<String, Object>> list = new ArrayList<>();
		StringBuffer sqlStr = new StringBuffer();
		if (companyId != null && activityType != null) {
			
			// 单团类，必须要有groupId
			if (Context.ACTIVITY_KINDS_DT.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_SP.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_YX.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_DKH.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_ZYX.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_YL.equals(activityType.toString())) {
				
				sqlStr.append(" select u.id, u.`name` ");
				sqlStr.append(" from travelactivity t, activitygroup a, sys_user u ");
				sqlStr.append(" WHERE t.id = a.srcActivityId and a.createBy = u.id ");
				sqlStr.append(" and t.proCompany = " + companyId);
				sqlStr.append(" and t.activity_kind = " + activityType);
				sqlStr.append(" group by u.id ");
				
			} else if (Context.ACTIVITY_KINDS_JP.equals(activityType.toString())) {  // 机票必须要有 activityId
				
				sqlStr.append(" select u.id, u.`name` ");
				sqlStr.append(" from activity_airticket t, sys_user u ");
				sqlStr.append(" WHERE t.createBy = u.id ");
				sqlStr.append(" and t.proCompany = " + companyId);
				sqlStr.append(" group by u.id ");
				
			} else if (Context.ACTIVITY_KINDS_QZ.equals(activityType.toString())) {  // 签证必须要有 activityId
				
				sqlStr.append(" select u.id, u.`name` ");
				sqlStr.append(" from visa_products t, sys_user u ");
				sqlStr.append(" WHERE t.createBy = u.id ");
				sqlStr.append(" and t.proCompanyId = " + companyId);
				sqlStr.append(" group by u.id ");
				
			} else {
				// TODO
			}
		}
		list = findBySql(sqlStr.toString(), Map.class);
		
		return list;
		
	}

	@Override
	public List<Map<String, Object>> getPartInfoByGroupAndActivity(
			Long companyId, Integer activityType, String activityIdStr, String groupIdStr) {
		
		List<Map<String, Object>> list = new ArrayList<>();
		StringBuffer sqlStr = new StringBuffer();
		if (companyId != null) {
			// 单团类，必须要有groupId
			if (Context.ACTIVITY_KINDS_DT.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_SP.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_YX.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_DKH.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_ZYX.equals(activityType.toString())
					|| Context.ACTIVITY_KINDS_YL.equals(activityType.toString())) {
				
				if (StringUtils.isNotBlank(groupIdStr)) {
					// 去逗号
					if (groupIdStr.endsWith(",")) {
						groupIdStr = groupIdStr.substring(0, groupIdStr.length() - 1);
					}
					
					sqlStr.append(" SELECT ag.id as groupId, ag.groupCode, ta.id as activityId, ta.acitivityName, ag.groupOpenDate, ");
					sqlStr.append(" IFNULL(ag.groupCloseDate,'0000-00-00') as groupCloseDate, ta.createBy AS activityCreatorId, u.`name` as activityCreator ");
					sqlStr.append(" FROM travelactivity ta ");
					sqlStr.append(" JOIN activitygroup ag ON ta.id = ag.srcActivityId ");
					sqlStr.append(" JOIN sys_user u ON ta.createBy = u.id ");
					sqlStr.append(" WHERE ta.proCompany = " + companyId);
					sqlStr.append(" AND ta.activity_kind = " + activityType);
					sqlStr.append(" AND ag.id in (" + groupIdStr + ")");
				}
				
			} else if (Context.ACTIVITY_KINDS_JP.equals(activityType.toString())) {  // 机票必须要有 activityId
				
				if (StringUtils.isNotBlank(activityIdStr)) {
					// 去逗号
					if (activityIdStr.endsWith(",")) {
						activityIdStr = activityIdStr.substring(0, activityIdStr.length() - 1);
					}
					// sqlStr.append();
					sqlStr.append(" SELECT ta.id AS groupId, IFNULL(ta.product_code, '') AS groupCode, ta.id AS activityId, IFNULL(ta.activity_airticket_name,'') AS acitivityName, IFNULL(ta.groupOpenDate,'') as groupOpenDate, ");
					sqlStr.append(" '' AS groupCloseDate, ta.createBy AS activityCreatorId, u.`name` AS activityCreator ");
					sqlStr.append(" FROM activity_airticket ta ");
					sqlStr.append(" JOIN sys_user u ON ta.createBy = u.id ");
					sqlStr.append(" WHERE ta.proCompany = " + companyId);
					sqlStr.append(" AND ta.id in (" + activityIdStr + ")");
				}
				
			} else if (Context.ACTIVITY_KINDS_QZ.equals(activityType.toString())) {  // 签证必须要有 activityId
				if (StringUtils.isNotBlank(activityIdStr)) {
					// 去逗号
					if (activityIdStr.endsWith(",")) {
						activityIdStr = activityIdStr.substring(0, activityIdStr.length() - 1);
					}
					// sqlStr.append();
					sqlStr.append(" SELECT ta.id AS groupId, IFNULL(ta.groupCode, '') AS groupCode, ta.id AS activityId, ta.productName AS acitivityName, '' AS groupOpenDate, ");
					sqlStr.append(" '' AS groupCloseDate, ta.createBy AS activityCreatorId, u.`name` AS activityCreator ");
					sqlStr.append(" FROM visa_products ta ");
					sqlStr.append(" JOIN sys_user u ON ta.createBy = u.id ");
					sqlStr.append(" WHERE ta.proCompanyId = " + companyId);
					sqlStr.append(" AND ta.id in (" + activityIdStr + ")");
				}
				
			} else {
				// TODO
			}
			
		}
		list = findBySql(sqlStr.toString(), Map.class);
		
		return list;
		
	}
	
}

