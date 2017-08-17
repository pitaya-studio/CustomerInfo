package com.trekiz.admin.modules.activity.service.sync;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.cost.entity.CostRecordLog;
import com.trekiz.admin.modules.cost.repository.CostRecordLogDao;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Remind;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
public class ActivityGroupSyncService implements IActivityGroupService {
	
	@Autowired
	@Qualifier("activityGroupService")
	private IActivityGroupService activityGroupService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ActivityGroupDao groupDao;
	
	@Autowired
	private CostRecordLogDao costRecordLogDao;
	
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Override
	public List<ActivityGroup> getGroupListByActivityId(Integer srcActivityId) {
		return activityGroupService.getGroupListByActivityId(srcActivityId);
	}
	@Override
	public ActivityGroup findById(Long id) {
		return activityGroupService.findById(id);
	}
	
	
	@Override
	public List<ActivityGroup> groupOpenDateRepeat(String activityId,
			String groupOpenDate, String groupid) {
		return activityGroupService.groupOpenDateRepeat(activityId, groupOpenDate, groupid);
	}
	
	
	@Override
	public void save(Set<ActivityGroup> activityGroups) {
		activityGroupService.save(activityGroups);
	}
	
	
	@Override
	public void saveGroups(List<ActivityGroup> groups) {
		// TODO Auto-generated method stub
		activityGroupService.saveGroups(groups);
	}
	
	//提交 成本审核
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void submitReview(Long id,Integer review) {		
		  this.groupDao.submitReview(id,review);
		 
	}
	
	//成本审核
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void updateReview(Long id,Integer review,Integer nowLevel,CostRecordLog costRecordLog) {	
		this.groupDao.updateReview(id,review,nowLevel);
		this.costRecordLogDao.save(costRecordLog);
	}
	
	/**
	 * 删除团期并同步：如果要删除的团期含有最大截团时间段，
	 * 则删除完此团期后再修改其他一个团期（目的是为了把次大的截团时间段传递过去），
	 * 此接口中最大截图时间段不为必须接口，所以要再次修改一个新的对象把最大截团时间段传递过去，
	 * 如果只有一个团期，则删除之后正向平台不显示，不必传最大截团时间段
	 * @param group
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delGroup(ActivityGroup group) throws Exception {
		activityGroupService.delGroup(group);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delGroupsByIds(List<Long> ids) throws Exception {
		if(ids.size() > 0) {
			activityGroupService.delGroupsByIds(ids);
		}
	}
	
	/**
	 * 
	 * 对未过期的产品更新相应团期里的最早出团日期、
	 * 最低成人同行价和最低成人零售价 
	 *
	 */
	@Override
	public void execTravelActivityTask() {
		// TODO Auto-generated method stub
		activityGroupService.execTravelActivityTask();
	}

	@Override
	public void execOffLineTask() {
		// TODO Auto-generated method stub
		activityGroupService.execOffLineTask();
	}

	@Override
	public void batchOffLineActivity(String updateSql, List<String> ids,
			String activityStatus) {
		// TODO Auto-generated method stub
		activityGroupService.batchOffLineActivity(updateSql, ids, activityStatus);
	}

	@Override
	public void batchUpdateActivity(String updateSql, List<Object[]> datas) {
		// TODO Auto-generated method stub
		activityGroupService.batchUpdateActivity(updateSql, datas);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void updateByOptLock(ActivityGroup activityGroup,
			String versionString) throws OptimisticLockHandleException,
			PositionOutOfBoundException, Exception {
		activityGroupService.updateByOptLock(activityGroup, versionString);
	}

	public void updatePositionNumByOptLock(ActivityGroup activityGroup,
			String versionString) throws OptimisticLockHandleException,
			PositionOutOfBoundException, Exception {
		activityGroupService.updatePositionNumByOptLock(activityGroup, versionString);
	}

	@Override
	public boolean groupCodeValidator(String groupCode, String groupid) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Deprecated
	@Override
	public void resetCostStatus(Long groupId, Integer costStatus) {
		groupDao.resetCostStatus(groupId, costStatus);
		
	}
	
	@Deprecated
	@Override
	public void save(ActivityGroup activityGroup) {
		groupDao.save(activityGroup);
		
	}
	@Override
	public String uploadGroupFile(MultipartFile file) {
		DocInfo docInfo = null;
		String fileName = file.getOriginalFilename();
		if(StringUtils.isNotBlank(fileName)){
			try {
	        		String path = FileUtils.uploadFile(file.getInputStream(),fileName);
	        		docInfo = new DocInfo();
	        		docInfo.setDocName(fileName);
	        		docInfo.setDocPath(path);
	        		docInfoDao.save(docInfo);
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return docInfo.getId().toString();
		}else{
			return "";
		}
	}
	@Override
	public String getCurrentDateMaxGroupCode(String groupOpenDate) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ActivityGroup findByGroupCode(String groupCode) {
		ActivityGroup activityGroup = null;
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(StringUtils.isNotBlank(groupCode)) {
			List<ActivityGroup> groupList = activityGroupDao.findByGroupCodeAndCompany(groupCode, companyId);
			if(!groupList.isEmpty()) {
				activityGroup = groupList.get(0);
			}
		}
		return activityGroup;
	}
	@Override
	public String getGroupNumForTTS(String deptId, String groupOpenDate) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getMaxCountIngroupNumForComp(String currentCode,
			String defaultCount) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getZeroCode(String maxNum, int countLength) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 获取当前供应商团号累加最大值
	 * @author jiachen
	 * @DateTime 2015年6月9日 上午11:46:12
	 * @param companyId
	 * @return Long
	 */
	@Override
	public Long getMaxCountForSequence(int date) {
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getProductInfoForForecast(Long groupId) {
		//此方法没有写实现，具体实现在com.trekiz.admin.modules.activity.service.ActivityGroupService中
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getProductInfoForSettle(Long groupId) {
		//此方法没有写实现，具体实现在com.trekiz.admin.modules.activity.service.ActivityGroupService中
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getOrderAndRefundInfoForcast(Long productId, Integer orderType) {
		//此方法没有写实现，具体实现在com.trekiz.admin.modules.activity.service.ActivityGroupService中
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getOrderAndRefundInfoSettle(Long productId, Integer orderType) {
		//此方法没有写实现，具体实现在com.trekiz.admin.modules.activity.service.ActivityGroupService中
		return null;
	}
	@Override
	public String groupCodeCheck(String groupCode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean groupNoCheck(String groupCode) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 依据产品类型获取所有产品信息
	 * @param activityType 产品类型
	 * @return
	 */
	@Override
	public JSONArray getAllActivityByType(Integer activityType) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 查询批发商下某类型的所有产品的部分信息 。主要信息：产品id/产品名称/团期id/团号/出团日期/截团日期/产品发布人 等。
		List<Map<String, Object>> actInfos = activityGroupDao.getPartInfoByTypeAndCompany(companyId, activityType);
		// 转化 list 为 jsonarray
		JSONArray jsonArray = new JSONArray();
		for (Map<String, Object> map : actInfos) {
			map.put("groupOpenDate", DateUtils.formatDate(DateUtils.parseDate(map.get("groupOpenDate")), DateUtils.DATE_PATTERN_YYYY_MM_DD));
			map.put("groupCloseDate", DateUtils.formatDate(DateUtils.parseDate(map.get("groupCloseDate")), DateUtils.DATE_PATTERN_YYYY_MM_DD));
			JSONObject jsonObject = JSONObject.fromObject(map);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	@Override
	public JSONArray getAllActivityByType(Map<String, Object> paramMap) {
		
		Integer activityType  = paramMap.get("activityType") == null ? null : Integer.parseInt(paramMap.get("activityType").toString());
		if (activityType == null) {
			return null;
		}
		String groupCode = paramMap.get("groupCode") == null ? null : paramMap.get("groupCode").toString();
		String activityName = paramMap.get("activityName") == null ? null : paramMap.get("activityName").toString();
		String groupOpenDate = paramMap.get("groupOpenDate") == null ? null : paramMap.get("groupOpenDate").toString();
		String groupCloseDate = paramMap.get("groupCloseDate") == null ? null : paramMap.get("groupCloseDate").toString();
		String creator = paramMap.get("creator") == null ? null : paramMap.get("creator").toString();
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 查询批发商下某类型的所有产品的部分信息 。主要信息：产品id/产品名称/团期id/团号/出团日期/截团日期/产品发布人 等。
		List<Map<String, Object>> actInfos = activityGroupDao.getPartInfoByTypeAndCompany(companyId, activityType, groupCode, activityName, groupOpenDate, groupCloseDate, creator);
		// 转化 list 为 jsonarray
		JSONArray jsonArray = new JSONArray();
		if (CollectionUtils.isNotEmpty(actInfos)) {			
			for (Map<String, Object> map : actInfos) {
				if (map.get("groupOpenDate") != null && StringUtils.isNotBlank(map.get("groupOpenDate").toString())) {					
					map.put("groupOpenDate", DateUtils.formatDate(DateUtils.parseDate(map.get("groupOpenDate")), DateUtils.DATE_PATTERN_YYYY_MM_DD));
				} else {
					map.put("groupOpenDate", "");
				}
				if (map.get("groupCloseDate") != null && StringUtils.isNotBlank(map.get("groupCloseDate").toString())) {					
					map.put("groupCloseDate", DateUtils.formatDate(DateUtils.parseDate(map.get("groupCloseDate")), DateUtils.DATE_PATTERN_YYYY_MM_DD));
				} else {
					map.put("groupCloseDate", "");
				}
				JSONObject jsonObject = JSONObject.fromObject(map);
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray;
	}
	
	@Override
	public List<Map<String, Object>> getAllCreators(Integer activityType) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> creators = activityGroupDao.getAllCreators(companyId, activityType);
		return creators;
	}
	
	@Override
	public JSONArray getActivityInfoByRemind(Remind remind) {
		// 获取 产品id、团期id
		String activityIdStr = remind.getProductIds();
		String groupIdStr = remind.getActivityGroupIds();
		if (StringUtils.isNotBlank(remind.getSelectedRemindOrderType()) && StringUtils.isNotBlank(activityIdStr) && StringUtils.isNotBlank(groupIdStr)) {
			Integer activityType = Integer.parseInt(remind.getSelectedRemindOrderType());
			Long companyId = UserUtils.getUser().getCompany().getId();
			List<Map<String, Object>> actInfos = activityGroupDao.getPartInfoByGroupAndActivity(companyId, activityType, activityIdStr, groupIdStr);
			// 转化 list 为 jsonarray
			JSONArray jsonArray = new JSONArray();
			if (CollectionUtils.isNotEmpty(actInfos)) {			
				for (Map<String, Object> map : actInfos) {
					if (map.get("groupOpenDate") != null && StringUtils.isNotBlank(map.get("groupOpenDate").toString())) {					
						map.put("groupOpenDate", DateUtils.formatDate(DateUtils.parseDate(map.get("groupOpenDate")), DateUtils.DATE_PATTERN_YYYY_MM_DD));
					} else {
						map.put("groupOpenDate", "");
					}
					if (map.get("groupCloseDate") != null && StringUtils.isNotBlank(map.get("groupCloseDate").toString())) {					
						map.put("groupCloseDate", DateUtils.formatDate(DateUtils.parseDate(map.get("groupCloseDate")), DateUtils.DATE_PATTERN_YYYY_MM_DD));
					} else {
						map.put("groupCloseDate", "");
					}
					JSONObject jsonObject = JSONObject.fromObject(map);
					jsonArray.add(jsonObject);
				}
			}
			return jsonArray;
		} else {
			return null;
		}
	}
	
	@Override
	public void updateObj(ActivityGroup activityGroup) {
		this.groupDao.updateObj(activityGroup);
	}
	
	@Override
	public Map<String, Object> countOrderChildAndSpecialNum(Long productGroupId,String containSelf) {
		 StringBuffer sb = new StringBuffer();
		 if(StringUtil.isBlank(containSelf)){
			 sb.append("select SUM(orderPersonNumChild) orderPersonNumChild,sum(orderPersonNumSpecial) orderPersonNumSpecial from productorder where productGroupId =? and payStatus in (3,4,5) and delFlag = 0 ");
			 List<Map <String, Object>> map =  groupDao.findBySql(sb.toString(), Map.class, productGroupId);
			 return map.get(0);
		 }else{
			 sb.append("select SUM(orderPersonNumChild) orderPersonNumChild,sum(orderPersonNumSpecial) orderPersonNumSpecial from productorder where productGroupId =? and id != ? and payStatus in (3,4,5) and delFlag = 0 ");
			 List<Map <String, Object>> map =  groupDao.findBySql(sb.toString(), Map.class, productGroupId,containSelf);
			 return map.get(0);
		 }
	}

	public void updatePSStatusById(Long groupId, Integer status){
		activityGroupService.updatePSStatusById(groupId, status);
	}

}
