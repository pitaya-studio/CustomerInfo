package com.trekiz.admin.modules.activity.service;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.ChineseToEnglish;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.LogProductDao;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.money.entity.AmountBean;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.Remind;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.TravelerUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 产品出团信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public class ActivityGroupService extends BaseService implements IActivityGroupService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private SysIncreaseService sysIncreaseService;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private LogProductDao logProductDao;
	@Autowired
	private ProductOrderService productOrderService;
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#save(java.util.Set)
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void save(Set<ActivityGroup> activityGroups){
		activityGroupDao.save(activityGroups);
	}
	
	
	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void save(ActivityGroup activityGroup){
		activityGroupDao.save(activityGroup);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#getGroupListByActivityId(java.lang.Integer)
	 */
	@Override
	public List<ActivityGroup> getGroupListByActivityId(Integer srcActivityId){
		
		return activityGroupDao.findGroupByActivityId(srcActivityId);
	}
	public ActivityGroup getGroupListByGroupId(Long id){
		return activityGroupDao.findById(id);
	}
	
	public List<Object[]> getGroupListByActivityId(Long srcActivityId){
		
		return activityGroupDao.findGroupByActivityId(srcActivityId);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#findById(java.lang.Long)
	 */
	@Override
	public ActivityGroup findById(Long id){
		return activityGroupDao.findOne(id);
	}
	
	/**
	 * 根据团号查找当前批发商团期
	 */
	@Override
	public ActivityGroup findByGroupCode(String groupCode) {
		// TODO Auto-generated method stub
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
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#delGroup(com.trekiz.admin.modules.activity.entity.ActivityGroup)
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor={Exception.class})
	public void delGroup(ActivityGroup group){
		activityGroupDao.delGroupById(group.getId());
	}
	
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#saveGroups(java.util.List)
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor={Exception.class})
	public void saveGroups(List<ActivityGroup> groups){
		activityGroupDao.save(groups);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#delGroupsByIds(java.util.List)
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor={Exception.class})
	public void delGroupsByIds(List<Long> ids){		
		activityGroupDao.delGroupsByIds(ids);
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#execTravelActivityTask()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execTravelActivityTask(){
		
		logger.info("开始更新未过期团期的的最早出团日期、最低成人同行价和最低成人零售价");
		int curPageNum = 1;
		int curPage = 0;
		int perPage = 100;
		List<Map<String,Object>> dataList = Lists.newArrayList();
		List<Object[]> resList = Lists.newArrayList();
		Query query;
		
		String querySql = "select srcActivityId,"
				+"min(groupOpenDate) minGroupOpenDate,"
				+"max(groupOpenDate) maxGroupOpenDate,"
				+"min(settlementAdultPrice) settlementAdultPrice,"
				+"min(suggestAdultPrice) suggestAdultPrice "
				+"from activitygroup "
				+"where delFlag = '"+ActivityGroup.DEL_FLAG_NORMAL+"' "
				+"and groupOpenDate > '"+DateUtils.getDate()+"' "
				+"group by srcActivityId "
				+"order by srcActivityId";
		
		String updateSql = "update travelactivity set groupOpenDate = ?,"
						+"groupCloseDate=?,"
						+"settlementAdultPrice=?,"
						+"suggestAdultPrice=? "
						+"where id = ?";
		
		while(true){
			
			query = activityGroupDao.createSqlQuery(querySql);
			query.setFirstResult(curPage);
			query.setMaxResults(perPage);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
			dataList = query.list();
			if(dataList!=null && dataList.size()!=0){
				for(Map<String,Object> map : dataList){
					Object[] objs = new Object[5];
					String srcActivityId = map.get("srcActivityId")==null?"":String.valueOf(map.get("srcActivityId"));
					String minGroupOpenDate = map.get("minGroupOpenDate")==null? null:String.valueOf(map.get("minGroupOpenDate"));
					String maxGroupOpenDate = map.get("maxGroupOpenDate")==null? null:String.valueOf(map.get("maxGroupOpenDate"));
					String settlementAdultPrice = map.get("settlementAdultPrice")==null?"":String.valueOf(map.get("settlementAdultPrice"));
					String suggestAdultPrice = map.get("suggestAdultPrice")==null?"":String.valueOf(map.get("suggestAdultPrice"));
					objs[0] = srcActivityId;
					objs[1] = minGroupOpenDate;
					objs[2] = maxGroupOpenDate;
					objs[3] = settlementAdultPrice;
					objs[4] = suggestAdultPrice;
					resList.add(objs);
				}
				batchUpdateActivity(updateSql,resList);
			}
			
			if(dataList==null || dataList.size()<perPage)
				break;
			else{
				logger.info("更新了"+dataList.size()+"条产品数据");
				curPageNum ++;
				curPage = (curPageNum-1)*perPage;
			}
			
		}
		logger.info("更新结束");
		
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#execOffLineTask()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execOffLineTask(){
		
		logger.info("开始对过期的产品进行下架处理");
		int curPageNum = 1;
		int curPage = 0;
		int perPage = 100;
		List<Map<String,Object>> dataList = Lists.newArrayList();
		List<String> idList = Lists.newArrayList();
		Query query;
		
		String querySql = "select t.srcActivityId from ("
						+"select t1.srcActivityId,"
						+"max(t1.groupOpenDate) maxGroupOpenDate "
						+"from activitygroup t1,travelactivity t2 "
						+"where t1.groupOpenDate is not null "
						+"and t1.srcActivityId = t2.id "
						+"and t2.activityStatus = "+Context.PRODUCT_ONLINE_STATUS+") t "
						+"where t.maxGroupOpenDate <= '"+DateUtils.getDate()+"' ";		
		
		String updateSql = "update travelactivity set activityStatus = ? where id = ?";
		
		while(true){
			
			query = activityGroupDao.createSqlQuery(querySql);
			query.setFirstResult(curPage);
			query.setMaxResults(perPage);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
			dataList = query.list();
			if(dataList!=null && dataList.size()!=0){
				for(Map<String,Object> map : dataList){
					String srcActivityId = map.get("srcActivityId")==null?"":String.valueOf(map.get("srcActivityId"));
					if(StringUtils.isNotBlank(srcActivityId))
						idList.add(srcActivityId);
				}
				batchOffLineActivity(updateSql, idList, Context.PRODUCT_OFFLINE_STATUS);
			}
			
			if(dataList==null || dataList.size()<perPage)
				break;
			else{
				logger.info("对"+dataList.size()+"条产品进行下架");
				curPageNum ++;
				curPage = (curPageNum-1)*perPage;
			}
			
		}
		logger.info("更新下架产品结束");
		
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#batchOffLineActivity(java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor={Exception.class})
	public void batchOffLineActivity(String updateSql,final List<String> ids,final String activityStatus){
		
		getJdbcTemplate().batchUpdate(updateSql.toString(),
				new BatchPreparedStatementSetter() {

				public int getBatchSize() {
					return ids.size();
				}

				public void setValues(PreparedStatement ps, int index)throws SQLException {
					String id = ids.get(index);					
					ps.setInt(1, Integer.parseInt(activityStatus));
					ps.setLong(2, Long.parseLong(id));
				
				}

		});
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#batchUpdateActivity(java.lang.String, java.util.List)
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor={Exception.class})
	public void batchUpdateActivity(String updateSql,final List<Object[]> datas){
		
		getJdbcTemplate().batchUpdate(updateSql.toString(),
				new BatchPreparedStatementSetter() {

				public int getBatchSize() {
					return datas.size();
				}

				public void setValues(PreparedStatement ps, int index)throws SQLException {
					Object[] data = datas.get(index);
					String srcActivityId = data[0].toString();
					String minGroupOpenDate = data[1]==null? "":data[1].toString();
					String maxGroupOpenDate = data[2]==null? "":data[2].toString();
					String settlementAdultPrice = data[3].toString();
					String suggestAdultPrice = data[4].toString();
					
					if(data[3]==null || "".equals(data[3].toString()))
						settlementAdultPrice = "0";
					if(data[4]==null || "".equals(data[4].toString()))
						suggestAdultPrice = "0";
					
					if(StringUtils.isNotBlank(srcActivityId) && StringUtils.isNotBlank(minGroupOpenDate) && StringUtils.isNotBlank(maxGroupOpenDate)){
						ps.setString(1, minGroupOpenDate);
						ps.setString(2, maxGroupOpenDate);
						ps.setDouble(3, Double.parseDouble(settlementAdultPrice));
						ps.setDouble(4, Double.parseDouble(suggestAdultPrice));
						ps.setLong(5, Long.parseLong(srcActivityId));
					}
				
				}

		});
	}
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#groupOpenDateRepeat(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<ActivityGroup> groupOpenDateRepeat(String activityId,String groupOpenDate,String groupid) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");        
		Date date = null;  
		try {
			date = format.parse(groupOpenDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		date = java.sql.Date.valueOf(groupOpenDate);
		return activityGroupDao.groupOpenDateRepeat(StringUtils.toInteger(activityId),date,StringUtils.toLong(groupid));
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#updateByOptLock(com.trekiz.admin.modules.activity.entity.ActivityGroup, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void updateByOptLock(ActivityGroup activityGroup, String versionString) throws OptimisticLockHandleException, PositionOutOfBoundException{
		int row = this.activityGroupDao.updateByOptLock(activityGroup, versionString);
		if(row <= 0){
			for(int i = 0; i < 3; i++){
				ActivityGroup tmp = this.activityGroupDao.findOne(activityGroup.getId());
				activityGroup.setVersionNumber(tmp.getVersionNumber());
				Integer freePosition = tmp.getFreePosition() + activityGroup.getPlusFreePosition();
				Integer nopayReservePosition = tmp.getNopayReservePosition() + activityGroup.getPlusNopayReservePosition();
				Integer payReservePosition = tmp.getPayReservePosition() + activityGroup.getPlusPayReservePosition();
				Integer planPosition = tmp.getPlanPosition() + activityGroup.getPlusPlanPosition();
				Integer soldNopayPosition = tmp.getSoldNopayPosition() + activityGroup.getPlusSoldNopayPosition();
				Integer soldPayPosition = tmp.getSoldPayPosition() + activityGroup.getPlusSoldPayPosition();
				activityGroup.setFreePosition(freePosition);
				activityGroup.setNopayReservePosition(nopayReservePosition);
				activityGroup.setPayReservePosition(payReservePosition);
				activityGroup.setPlanPosition(planPosition);
				activityGroup.setSoldNopayPosition(soldNopayPosition);
				activityGroup.setSoldPayPosition(soldPayPosition);
				if(freePosition < 0 || nopayReservePosition < 0 || 
						payReservePosition < 0 || planPosition < 0 || 
						soldNopayPosition < 0 || soldPayPosition < 0){
					throw new PositionOutOfBoundException("团期位置出现负值，请检查错误");
				}
				row = this.activityGroupDao.updateByOptLock(activityGroup, versionString);
				if(row > 0){
					break;
				}
				if(i == 2 && row <= 0){
					throw new OptimisticLockHandleException("团期位置不能正常更新");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupService#updatePositionNumByOptLock(com.trekiz.admin.modules.activity.entity.ActivityGroup, java.lang.String)
	 */
	@Override
	public void updatePositionNumByOptLock(ActivityGroup activityGroup, String versionString) throws OptimisticLockHandleException, PositionOutOfBoundException{
		int row = this.activityGroupDao.updatePositionNumByOptLock(activityGroup, versionString);
		
//		if(row <= 0){
//			for(int i = 0; i < 3; i++){
//				ActivityGroup tmp = this.activityGroupDao.findOne(activityGroup.getId());
//				Integer freePosition = tmp.getFreePosition() + activityGroup.getPlusFreePosition();
//				Integer nopayReservePosition = tmp.getNopayReservePosition() + activityGroup.getPlusNopayReservePosition();
//				Integer payReservePosition = tmp.getPayReservePosition() + activityGroup.getPlusPayReservePosition();
//				Integer planPosition = tmp.getPlanPosition() + activityGroup.getPlusPlanPosition();
//				Integer soldNopayPosition = tmp.getSoldNopayPosition() + activityGroup.getPlusSoldNopayPosition();
//				Integer soldPayPosition = tmp.getSoldPayPosition() + activityGroup.getPlusSoldPayPosition();
//				tmp.setFreePosition(freePosition);
//				tmp.setNopayReservePosition(nopayReservePosition);
//				tmp.setPayReservePosition(payReservePosition);
//				tmp.setPlanPosition(planPosition);
//				tmp.setSoldNopayPosition(soldNopayPosition);
//				tmp.setSoldPayPosition(soldPayPosition);
//				if(freePosition < 0 || nopayReservePosition < 0 || 
//						payReservePosition < 0 || planPosition < 0 || 
//						soldNopayPosition < 0 || soldPayPosition < 0){
//					throw new PositionOutOfBoundException("团期位置出现负值，请检查错误");
//				}
//				row = this.activityGroupDao.updatePositionNumByOptLock(tmp, versionString);
//				if(row > 0){
//					break;
//				}
//				if(i == 2 && row <= 0){
//					System.out.println(activityGroup.getId());
//					System.out.println(versionString);
//					throw new OptimisticLockHandleException("团期位置不能正常更新");
//				}
//			}
//		}
	}
	
	private  String filterCtrlChars(String source){
	      StringBuffer sf = new StringBuffer();
	      if (source!=null) {
			
	    	  for (char c : source.toCharArray()){
	    		  if (Character.isISOControl(c)){
	    			  sf.append("\\").append(Integer.toOctalString(c));       
	    		  }else{
	    			  sf.append(c);
	    		  }
	    	  }
	    	  
		  }else{
			  return "";
		  }
	      
	      return sf.toString();
	  }
	
	/**
	 * 团号对于同一批发商不可重复     团期类产品新增     判断团号是否重复
	 */
	@Override
	public boolean groupCodeValidator(String groupCode, String groupid) {
		
		boolean flag = false;
//		Long companyId = UserUtils.getUser().getCompany().getId();
//		if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")
//				|| UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")
//				|| UserUtils.getUser().getCompany().getUuid().contains("7a81a26b77a811e5bc1e000c29cf2586")
//				|| UserUtils.getUser().getCompany().getUuid().contains("7a45838277a811e5bc1e000c29cf2586")
//				|| UserUtils.getUser().getCompany().getUuid().contains("ed88f3507ba0422b859e6d7e62161b00")
//				|| UserUtils.getUser().getCompany().getUuid().contains("f5c8969ee6b845bcbeb5c2b40bac3a23")
//				|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")){
			//true有重复
			if(groupNoCheck(groupCode)){
				flag = true;
				return flag;
			}
//		}
		//false没有重复
//		List<ActivityGroup> list = activityGroupDao.findByGroupCodeAndCompany(groupCode, companyId);
//		if(list != null && list.size() > 0) {
//			for(ActivityGroup group : list) {
//				if(ActivityGroup.DEL_FLAG_NORMAL.equals(group.getTravelActivity().getDelFlag()) && !group.getId().toString().equals(groupid)) {
//					flag = true;
//					return flag;
//				}
//			}
//		}
		return flag;
	}

	@Override
	public void resetCostStatus(Long groupId, Integer costStatus) {
		activityGroupDao.resetCostStatus(groupId, costStatus);
		
	}

	@Override
	public String getCurrentDateMaxGroupCode(String groupOpenDate) {
		String maxGroupCode = "";
		String currentDate = "";
		if(StringUtils.isBlank(groupOpenDate))
			currentDate = DateUtils.getDate("yyyyMMdd");
		else
			currentDate = groupOpenDate.replaceAll("-", "");
			
		maxGroupCode = getMaxCountIngroupNumForComp("BJ-__", "0001");
		
		return currentDate + Context.GROUPCODE_SPLIT_FLAG + maxGroupCode;
	}
	
	/**
	 * 获取指定格式团号的最大累加值
	 * @author jiachen
	 * @DateTime 2015年3月3日 下午5:16:56
	 * @return String
	 */
	public String getMaxCountIngroupNumForComp(String currentCode, String defaultCount) {
		String maxGroupCode = "";
		Integer numCountOuter = 0;
		Long companyId = UserUtils.getUser().getCompany().getId();
		//如果是环球行，则累加值取团号中间位置的
		if(68 == companyId)	 {
			numCountOuter = 2;
		//如果是新行者，则累加值取团号最后位置的
		}else if(71 == companyId) {
			numCountOuter = 4;
		}
		String sql = "SELECT  MAX(tb.maxNum+0) FROM ("
					+ "SELECT MAX(SUBSTRING_INDEX(zz.max, '-', -1)+0) maxNum FROM  ("
					+ "	SELECT SUBSTRING_INDEX(yy.groupCode, '-', " + numCountOuter + ") max  FROM ("
					+ "		SELECT a.groupCode FROM activitygroup a left JOIN travelactivity t ON a.srcActivityId=t.id WHERE t.proCompany = ? AND a.groupCode LIKE ? ) yy)zz"
					+ " UNION ALL "
					+ "SELECT MAX(SUBSTRING_INDEX(zz.max, '-', -1)+0) maxNum FROM  ("
					+ "	SELECT SUBSTRING_INDEX(yy.group_code, '-', " + numCountOuter + ") max  FROM ("
					+ "		SELECT a.group_code FROM activity_airticket a WHERE  a.group_code LIKE ? ) yy)zz "
					+ " UNION ALL "
					+ "SELECT MAX(SUBSTRING_INDEX(zz.max, '-', -1)+0) maxNum FROM  ("
					+ "	SELECT SUBSTRING_INDEX(yy.groupCode, '-', " + numCountOuter + ") max  FROM ("
					+ "		SELECT a.groupCode FROM visa_products a WHERE  a.groupCode LIKE ? ) yy)zz) tb";
		
		
		/**
		 * 		String sql = "SELECT  MAX(tb.maxNum+0) FROM ("
				+ "SELECT MAX(SUBSTRING_INDEX(zz.max, '-', -1)+0) maxNum FROM  ("
				+ "	SELECT SUBSTRING_INDEX(yy.groupCode, '-', " + numCountOuter + ") max  FROM ("
				+ "		SELECT a.groupCode FROM activitygroup a RIGHT JOIN travelactivity t ON a.srcActivityId=t.id WHERE t.proCompany = ? AND a.groupCode LIKE ? AND a.delFlag =0 AND t.delFlag=0) yy)zz"
				+ " UNION ALL "
				+ "SELECT MAX(SUBSTRING_INDEX(zz.max, '-', -1)+0) maxNum FROM  ("
				+ "	SELECT SUBSTRING_INDEX(yy.group_code, '-', " + numCountOuter + ") max  FROM ("
				+ "		SELECT a.group_code FROM activity_airticket a WHERE  a.group_code LIKE ?  AND a.delflag=0) yy)zz "
				+ " UNION ALL "
				+ "SELECT MAX(SUBSTRING_INDEX(zz.max, '-', -1)+0) maxNum FROM  ("
				+ "	SELECT SUBSTRING_INDEX(yy.groupCode, '-', " + numCountOuter + ") max  FROM ("
				+ "		SELECT a.groupCode FROM visa_products a WHERE  a.groupCode LIKE ?  AND a.delFlag=0) yy)zz) tb";
		String currentCodeForSql = (currentCode.startsWith("BJ-") ? "" : "%") + currentCode + "%";
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		
			String currentCodeForSql = (currentCode.startsWith("BJ-") ? "" : "%") + currentCode + "%";
			List<Object> maxNum = activityGroupDao.findBySql(sql, List.class, UserUtils.getUser().getCompany().getId(), currentCodeForSql, currentCodeForSql, currentCodeForSql);

		if(!maxNum.isEmpty()) {
			@SuppressWarnings("unchecked")
			List<String> maxNumList = (List<String>)maxNum.get(0);
			Integer manNumInt = StringUtils.toInteger(maxNumList.get(0)) + 1;
			maxGroupCode = getZeroCode(manNumInt.toString(), defaultCount.length());
		}else{
			maxGroupCode = defaultCount;
		}
		return maxGroupCode;
	}

	/**
	 * 
	 *  功能:
	 *	给指定的数补0位
	 *  @author xiaoyang.tao
	 *  @update jiachen
	 *  @DateTime 2014-11-7 下午3:38:14
	 *  @param maxNum countLength
	 *  @return
	 */
	public String getZeroCode(String maxNum, int countLength){
		if(null != maxNum) {
			String zeroStr = "00000000000000000000000";//00000000000000000000000
			if(null == Integer.valueOf(countLength) || zeroStr.length() < countLength) {
				countLength = 4;
			}
			String numStr=null;
			try {
				numStr = zeroStr.substring(0, countLength-1).substring(0, countLength - maxNum.length()) + maxNum;
			} catch (Exception e) {
				//针对 新行者  机票   发布时  的  bug 13133      暂时特殊处理
				e.printStackTrace();
				SimpleDateFormat sd  = new SimpleDateFormat("yyyyMMdd");
				numStr = sd.format(new Date());
				return numStr;
			}
			
			
			return numStr;
		}else{
			return maxNum;
		}
		
//		int numLength = maxNum.length();
//		switch(numLength){
//			case 1:
//				if(3 == countLength) {
//					maxGroupCode = "00" + maxNum;
//				}else if(4 == countLength) {
//					maxGroupCode = "000" + maxNum;
//				}
//				break;
//			case 2:
//				if(3 == countLength) {
//					maxGroupCode = "0" + maxNum;
//				}else if(4 == countLength) {
//					maxGroupCode = "00" + maxNum;
//				}
//				break;
//			case 3:
//				if(4 == countLength) {
//					maxGroupCode = "0" + maxNum;
//				}
//				break;
//			default:
//				maxGroupCode = maxNum;
//		}
	}
	
	@Override
	public String uploadGroupFile(MultipartFile file) {
		return null;
	}


	public Map<String, Object> reviewLogs(Map<String, Long> condition) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Long reviewId = condition.get("reviewId");
		Review review = reviewService.findReviewInfo(reviewId);
		List<ReviewLog> rLog=reviewService.findReviewLog(reviewId);
		map.put("review", review);//denyReason
		map.put("rLog", rLog);
		return map;
	}
	
	/**
	 * 为批发商环球行提供特定的团号生成规则(包含签证订单的虚拟团号)
	 * @author jiachen
	 * @DateTime 2015年3月3日 下午4:03:21
	 * @return String
	 * @param deptId：部门Id groupOpenDate：单团类（出团日期） 签证、机票订单（null） 
	 * 
	 * 规则：“ 14TY-0001-0925“ 
	 * 			“ 年份（后两位）+部门简称+序号+出团日期”
	 */
	public String getGroupNumForTTS(String deptId, String groupOpenDate) {
		//声明带有环球行前缀团号
		StringBuffer groupNum = new StringBuffer();
		if(StringUtils.isNotBlank(deptId)) {
			//获取部门
			Department dept = departmentService.findById(StringUtils.toLong(deptId));
			//获取部门地域
//			String deptCity = dept.getCity();
			//获取地域首字母
//			String deptCityPY = null!=deptCity ? sysIncreaseService.pyConvert(deptCity) : "";
			
			//获取当前日期年份和月份
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
			String year = String.valueOf(cal.get(Calendar.YEAR)).substring(2);
			//处理月份(个位数月份补0)
			String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			int monthLength = month.length();
			if(1 == monthLength) {
				month = "0" + month;
			}
			groupNum.append(year);
			
//			groupNum.append(Context.GROUPCODE_SPLIT_FLAG + deptCityPY);
			//groupNum.append(year);
//			if(null == groupOpenDate)
//			{
//				groupOpenDate=year;
//				groupNum.append(groupOpenDate);
//			}
//			else	
//			groupNum.append(groupOpenDate.substring(2, 4));
			//获取部门简称

			String code = "";
			//如果部门地域不是BJ(总公司)，则部门编号需要去其上级的。
			if(!"BJ".equals(ChineseToEnglish.getPinYinHeadChar(dept.getCity())) && 2 == dept.getLevel()) {
				code = dept.getParent().getCode();
			}else{
				code = dept.getCode();
			}
			groupNum.append(code);
			//单团类产品生成团号
			if(StringUtils.isNotBlank(groupOpenDate)) {
				//获取出团日期
				//groupNum.append(groupOpenDate.substring(2, 4));
				String[] groupOpenDateArr = groupOpenDate.split("-");
//			if(groupOpenDateArr.length==1)
//			return groupNum.toString()+"-"+month;
				String openDate = groupOpenDateArr[1] + groupOpenDateArr[2];
				
				//获取累计编号，凡是以TTS开头的团号都算
				String maxCount = "";
				if("68".equals(UserUtils.getUser().getCompany().getId().toString()))
					maxCount = getMaxCountIngroupNumForCompNew(groupOpenDate.substring(2, 4)+"__-____-____", "0001");
				else
					 maxCount = getMaxCountIngroupNumForComp("____-____", "0001");
				
				
				groupNum.append(Context.GROUPCODE_SPLIT_FLAG + maxCount);
				groupNum.append(Context.GROUPCODE_SPLIT_FLAG + openDate);
				// 15TY-6219-0303
				String tmp = groupOpenDate.substring(2, 4)+groupNum.toString().substring(2, groupNum.toString().length());
				return tmp;
				
			//生成签证订单虚拟团号
			}else{
				groupNum.append(Context.GROUPCODE_SPLIT_FLAG + month);
			}
			
		}
		
		return groupNum.toString();
	}
	
	private String getMaxCountIngroupNumForCompNew(String currentCode, String defaultCount) {
		// TODO Auto-generated method stub
		String maxGroupCode = "";
		Integer numCountOuter = 2;
		String sql = "SELECT  MAX(tb.maxNum+0) FROM ("
				+ "SELECT MAX(SUBSTRING_INDEX(zz.max, '-', -1)+0) maxNum FROM  ("
				+ "	SELECT SUBSTRING_INDEX(yy.groupCode, '-', " + numCountOuter + ") max  FROM ("
				+ "		SELECT a.groupCode FROM activitygroup a LEFT JOIN travelactivity t ON a.srcActivityId=t.id WHERE t.proCompany = ? AND a.groupCode LIKE ?  ) yy)zz"
				+ " UNION ALL "
				+ "SELECT MAX(SUBSTRING_INDEX(zz.max, '-', -1)+0) maxNum FROM  ("
				+ "	SELECT SUBSTRING_INDEX(yy.group_code, '-', " + numCountOuter + ") max  FROM ("
				+ "		SELECT a.group_code FROM activity_airticket a WHERE  a.group_code LIKE ?   AND a.proCompany=68) yy)zz "
				+ " UNION ALL "
				+ "SELECT MAX(SUBSTRING_INDEX(zz.max, '-', -1)+0) maxNum FROM  ("
				+ "	SELECT SUBSTRING_INDEX(yy.groupCode, '-', " + numCountOuter + ") max  FROM ("
				+ "		SELECT a.groupCode FROM visa_products a WHERE  a.groupCode LIKE ?    AND a.proCompanyId=68) yy)zz) tb";
		String currentCodeForSql = (currentCode.startsWith("BJ-") ? "" : "%") + currentCode + "%";
		List<Object> maxNum = activityGroupDao.findBySql(sql, UserUtils.getUser().getCompany().getId(), currentCodeForSql, currentCodeForSql, currentCodeForSql);
		
		if(maxNum.size()>0) {
			Integer manNumInt = StringUtils.toInteger(maxNum.get(0)) + 1;
			maxGroupCode = getZeroCode(manNumInt.toString(), defaultCount.length());
		}else{
			maxGroupCode = defaultCount;
		}
		return maxGroupCode;
	}


	/**
	 * 获取当前供应商团号累加最大值
	 * @author jiachen
	 * @DateTime 2015年6月9日 上午11:46:12
	 * @return Long
	 */
	@Override
	public Long getMaxCountForSequence(int date) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		Object sequence =null;
		if(68 == companyId)
			sequence = activityGroupDao.getMaxCountForSequenceNew(companyId, date);
		else
			sequence = activityGroupDao.getMaxCountForSequence(companyId);
		// getMaxCountForSequenceNew
		if(null != sequence) {
			return Long.valueOf(sequence.toString());
		}else{
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getProductInfoForSettle(Long groupId) {
		StringBuffer str = new StringBuffer();
		Long companyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		String isLockedIn = "";
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			//C486锁定后的数据不计入结算单 add by shijun.liu 2015.12.25
			//结算单锁定之后录入的新订单数据(settle_locked_in == 1)，不进入结算单,解锁后(settle_locked_in == null)进入
			isLockedIn = " AND o.settle_locked_in is null ";
		}
		//0258懿洋假期 发票税  王洋  2016.3.31
		String invoiceTax = "";
		if(Context.SUPPLIER_UUID_YYJQ.equals(currentCompanyUuid)){
			invoiceTax = " g.invoice_tax AS invoiceTax, ";
		}
		
		str.append("SELECT (SELECT su.`name` FROM sys_user su WHERE su.id = g.createBy) AS createBy,")
		   .append(" g.createBy as createById, g.groupCode,p.acitivityName AS productName,")
		   .append(" p.activityDuration,p.group_lead AS grouplead,FORMAT(g.settlementAdultPrice,2)")
		   .append(" AS settlementAdultPrice,g.currency_type AS currencyType, ")
		   .append(" sum(o.orderPersonNum) AS orderPersonNumSum,").append(invoiceTax)
		   .append(" g.groupOpenDate,DATE_ADD(g.groupOpenDate,INTERVAL p.activityDuration DAY) groupCloseDate, ")
		   .append(" g.groupCloseDate AS realGroupCloseDate,")
		   .append(" g.lockStatus, g.forcastStatus FROM activitygroup g left join productorder o ")
		   .append(" on o.productGroupId = g.id and o.payStatus not in (7, 99, 111) ").append(isLockedIn)
		   .append(" , travelactivity p WHERE ")
		   .append(" g.srcActivityId = p.id and p.proCompany =").append(companyId)
		   .append(" AND o.delFlag = '0' ").append(" and g.id = ").append(groupId);
		return activityGroupDao.findBySql(str.toString(), Map.class);
	}
	
	@Override
	public List<Map<String, Object>> getProductInfoForForecast(Long groupId) {
		StringBuffer str = new StringBuffer();
		Long companyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		String isLockedIn = "";
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			//C457锁定后的数据不计入预报单 add by shijun.liu 2015.12.25
			//预报单锁定之后录入的新订单数据(forecast_locked_in == 1)，不进入预报单,解锁后(forecast_locked_in == null)进入
			isLockedIn = " AND o.forecast_locked_in is null ";
		}
		str.append("SELECT (SELECT su.`name` FROM sys_user su WHERE su.id = g.createBy) AS createBy,")
		   .append(" g.createBy as createById, g.groupCode,p.acitivityName AS productName,")
		   .append(" p.activityDuration,p.group_lead AS grouplead,")
		   .append(" sum(o.orderPersonNum) AS orderPersonNumSum,")
		   .append(" g.groupOpenDate,DATE_ADD(g.groupOpenDate,INTERVAL p.activityDuration DAY) groupCloseDate, g.groupCloseDate AS groupEndDate, ")
		   .append(" g.lockStatus, g.forcastStatus, p.settlementAdultPrice FROM activitygroup g left join productorder o ")
		   .append(" on o.productGroupId = g.id and o.payStatus not in (7, 99, 111)").append(isLockedIn)
		   .append(" , travelactivity p WHERE ").append(" g.srcActivityId = p.id and p.proCompany =")
		   .append(companyId).append(" AND o.delFlag = '0' ")
		   .append(" and g.id = ").append(groupId);
		return activityGroupDao.findBySql(str.toString(), Map.class);
	}


	@Override
	public List<Map<String, Object>> getOrderAndRefundInfoForcast(Long productId, Integer orderType) {
		StringBuffer str = new StringBuffer();
		String lockedIn = "";
		String lockedInRefund = "";
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		
		//拉美途 过滤预报单锁定之后生成的订单数据，退款数据 C457 add by shijun.liu 2015.12.21
		// forecast_locked_in = 1 表示是预报单锁定之后生成的数据
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			lockedIn = " AND o.forecast_locked_in is null ";
			lockedInRefund = " AND cost.forecast_locked_in is null ";
		}
		str.append("SELECT id,saler,agentName,")
		   .append(" IFNULL(totalMoney, 0) AS totalMoney,")
		   .append(" IFNULL(accountedMoney, 0) AS accountedMoney,")
		   .append(" IFNULL((totalMoney - accountedMoney), 0) AS notAccountedMoney,")
		   .append(" IFNULL(r.refundprice, 0) AS refundprice, ")
		   .append(" adultNum,childrenNum,specialNum, ") // 0546 wangyang 2016.11.8
		   .append(" adultAmount,childrenAmount,specialAmount, ") // 0546 wangyang 2016.11.8
		   .append(" orderPersonNum,IFNULL(adultCostPriceSum,0) adultCostPriceSum,IFNULL(childrenCostPriceSum,0) childrenCostPriceSum," +
		   					"IFNULL(specialCostPriceSum,0) specialCostPriceSum, ")
		   .append("IFNULL(adultRefundSum,0) adultRefundSum, ")
		   .append("IFNULL(childrenRefundSum,0) childrenRefundSum, ")
		   .append("IFNULL(specialRefundSum,0) specialRefundSum, ")
		   .append("IFNULL(adultRefundGroupPrice, 0) adultRefundGroupPrice, ")
			.append("IFNULL(childrenRefundGroupPrice, 0) childrenRefundGroupPrice, ")
			.append("IFNULL(specialRefundGroupPrice, 0) specialRefundGroupPrice, ")
			.append("IFNULL(adultPersonCount, 0) adultPersonCount, ")
			.append("IFNULL(childrenPersonCount, 0) childrenPersonCount, ")
			.append("IFNULL(specialPersonCount, 0) specialPersonCount, ")
			.append("IFNULL(adultZGPriceSum, 0) adultZGPriceSum, ")
			.append("IFNULL(childrenZGPriceSum, 0) childrenZGPriceSum, ")
			.append("IFNULL(specialZGPriceSum, 0) specialZGPriceSum, ")
			.append("IFNULL(adultZGPersonCount, 0) adultZGPersonCount, ")
			.append("IFNULL(childrenZGPersonCount, 0) childrenZGPersonCount, ")
			.append("IFNULL(specialZGPersonCount, 0) specialZGPersonCount, ")
			.append("IFNULL(adultCostPriceSumZero, 0) adultCostPriceSumZero, ")
			.append("IFNULL(childrenCostPriceSumZero, 0) childrenCostPriceSumZero, ")
			.append("IFNULL(specialCostPriceSumZero, 0) specialCostPriceSumZero ")
		   .append(" FROM (")
		   .append(" SELECT o.id,")
		   .append(" (SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId) AS saler,")
		   .append(" (SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany) AS agentName,")
		   .append(" (SELECT ifnull(sum(m1.amount * m1.exchangerate), 0) FROM money_amount m1 ")
		   .append(" WHERE m1.serialNum = o.total_money) AS totalMoney,")
		   .append(" (SELECT ifnull(sum(m2.amount * m2.exchangerate), 0) FROM money_amount m2 ")
		   .append(" WHERE m2.serialNum = o.accounted_money) AS accountedMoney, ")
			// 0546 wangyang 2016.11.8
		   .append(" o.orderPersonNumAdult AS adultNum, ")
		   .append(" o.orderPersonNumChild AS childrenNum, ")
		   .append(" o.orderPersonNumSpecial AS specialNum, ")
		   .append(" ( SELECT ma.amount * ma.exchangerate FROM money_amount ma WHERE ma.serialNum = o.settlementAdultPrice ) AS adultAmount, ")
		   .append(" ( SELECT ma.amount * ma.exchangerate FROM money_amount ma WHERE ma.serialNum = o.settlementcChildPrice ) AS childrenAmount, ")
		   .append(" ( SELECT ma.amount * ma.exchangerate FROM money_amount ma WHERE ma.serialNum = o.settlementSpecialPrice ) AS specialAmount, ")
		   .append(" o.orderPersonNum ")
		   .append(",SUM(case when rn.`status`=2 and rn.process_type =10 and c.businessType=1 and tv.personType=1 then costPrice*currency.currency_exchangerate end ) adultCostPriceSum, ")
		   .append("SUM(case when rn.`status`=2 and rn.process_type =10 and c.businessType=1 and tv.personType=2 then costPrice*currency.currency_exchangerate end ) childrenCostPriceSum,")
		   .append("SUM(case when rn.`status`=2 and rn.process_type =10 and c.businessType=1 and tv.personType=3 then costPrice*currency.currency_exchangerate end ) specialCostPriceSum, ")
		   .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =1 and cr.reviewType=1 and cr.delFlag =0 and trv.personType = 1 and cr.orderType="+orderType+" and cr.budgetType =0 THEN cr.price END) adultRefundSum,")
		   .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =1 and cr.reviewType=1 and cr.delFlag =0 and trv.personType = 2 and cr.orderType="+orderType+" and cr.budgetType =0  THEN cr.price END) childrenRefundSum,")
		  .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =1 and cr.reviewType=1 and cr.delFlag =0 and trv.personType = 3 and cr.orderType="+orderType+" and cr.budgetType =0  THEN cr.price END) specialRefundSum, ")
		  .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 1  AND tra.delFlag = 3 AND rp.moneyType = 25 THEN rp.amount END ) adultRefundGroupPrice, ")
		   .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 2 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN rp.amount END ) childrenRefundGroupPrice, ")
		   .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 3 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN rp.amount END ) specialRefundGroupPrice, ")
		   .append("count(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 1 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN tra.id END) adultPersonCount, ")
		   .append("count(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 2 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN tra.id END) childrenPersonCount, ")
		   .append("count(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 3 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN tra.id END) specialPersonCount, ")	
		   .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType = 1 AND tra.delFlag =5 THEN ma.amount END) adultZGPriceSum, ")
		   .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType = 2 AND tra.delFlag =5 THEN ma.amount END) childrenZGPriceSum, ")
			.append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType = 3 AND tra.delFlag =5 THEN ma.amount END) specialZGPriceSum, ")
			.append("count(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType=1 AND tra.delFlag = 5 THEN tra.id END) adultZGPersonCount, ")
			.append("count(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType=2 AND tra.delFlag = 5 THEN tra.id END) childrenZGPersonCount, ")
			.append("count(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType=3 AND tra.delFlag = 5 THEN tra.id END) specialZGPersonCount, ")
			.append("SUM(CASE WHEN rn.`status` = 2 and rn.process_type =10 AND c.businessType = 1 AND tv.personType = 1 AND costPrice<0 THEN costPrice*currency.currency_exchangerate END ) adultCostPriceSumZero, ")
			.append("SUM(CASE WHEN rn.`status` = 2 and rn.process_type =10 AND c.businessType = 1 AND tv.personType = 2 AND costPrice<0 THEN costPrice*currency.currency_exchangerate END ) childrenCostPriceSumZero, ")
			.append("SUM(CASE WHEN rn.`status` = 2 and rn.process_type =10 AND c.businessType = 1 AND tv.personType = 3 AND costPrice<0 THEN costPrice*currency.currency_exchangerate END ) specialCostPriceSumZero ")
		   .append(" FROM ")
		   .append(" productorder o ")
		   .append("LEFT JOIN review_new rn ON o.id = rn.order_id LEFT JOIN costchange c ON rn.id = c.review_uuid  ")
		   .append("LEFT JOIN traveler tv ON tv.id = c.travelerId ")
		   .append("LEFT JOIN cost_record cr ON cr.reviewUuid = rn.id ")
		   .append("LEFT JOIN traveler trv ON trv.id = rn.traveller_id ")
		   .append("LEFT JOIN review_process_money_amount rp ON rp.reviewId = rn.id  LEFT JOIN traveler tra ON rn.traveller_id = tra.id ")
		   .append("	LEFT JOIN money_amount ma ON tra.subtract_moneySerialNum = ma.serialNum ")
		   .append("	LEFT JOIN currency currency ON currency.currency_id = c.price_currency ")
		   .append(" WHERE o.delFlag = '0' ")
		   .append(" AND o.payStatus NOT IN (7, 99, 111)")
		   .append(" AND o.orderStatus = ").append(orderType)
		   .append(" AND o.productGroupId = ").append(productId)
		   .append(lockedIn).append(" group By o.id ")
		   .append(" ) t1 LEFT JOIN (")
		   .append(" SELECT cost.orderId,sum(cost.price) AS refundprice ")
		   .append(" FROM cost_record cost ")
		   .append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType =").append(orderType)
		   .append(" AND cost.budgetType = ").append(CostManageService.BUDGET_TYPE).append(lockedInRefund)
		   .append(" GROUP BY cost.orderId ")
		   .append(" ) r on r.orderId = t1.id ");
		

		List<Map<String, Object>> orderIncomes = activityGroupDao.findBySql(str.toString(), Map.class);
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> exceptIncome4LZJQ = new ArrayList<>();
		// 报名游客类型 adult--成人 children--儿童 special--特殊人群
		String[] customerType = {"adult", "children", "special"};
		Currency currency = currencyService.findRMBCurrency();
		for (Map<String, Object> orderIncome : orderIncomes) {
			ProductOrderCommon orderCommon = productOrderService.getProductorderById(Long.parseLong(orderIncome.get("id").toString()));
			Map<String, Object> amountMap = getTravelerJieSuanJia(orderIncome.get("id").toString());
			Map<String, Object> countMap = productOrderService.getTravelerCount(orderCommon.getId());
			for (String type : customerType) {
				Integer num = (Integer) orderIncome.get(type + "Num");
				//改价所有的负值
				BigDecimal priceDecimal = new BigDecimal(orderIncome.get(type+"CostPriceSumZero").toString());
				//服务费
				Double fee = 0.00;
				if(orderCommon.getPriceType() == 2){
					fee = TravelerUtils.getServiceFee(orderCommon, currency, priceDecimal.toString());
				}
				if (num > 0) { // 存在对应类型客户 拆分为一条数据
					Map<String, Object> income = new HashMap<>();
					BigDecimal price = new BigDecimal(orderIncome.get(type + "Amount").toString());
					BigDecimal totalPrice = price.multiply(new BigDecimal(num));
					//加上退团人数*金额
					totalPrice = new BigDecimal(amountMap.get(type+"TotalPrice").toString());
					//减去退款
					totalPrice = totalPrice.subtract(new BigDecimal(orderIncome.get(type+"RefundSum").toString()));
					//游客表里存在的游客人数
					int personNum = 0;
					if(countMap != null){
						personNum = Integer.parseInt(countMap.get(type+"Num").toString());
					}
					//转团人数
					int zgroupNum = Integer.parseInt(orderIncome.get(type+"ZGPersonCount").toString());
					//退团人数
					int refundNum = Integer.parseInt(orderIncome.get(type+"PersonCount").toString());
					
					//原始订单人数
					Integer count = num + zgroupNum+refundNum;
					
					//没有添加游客的金额
					BigDecimal noNameMoney = new BigDecimal(count-personNum).multiply(price);
					if(count-personNum >= 0){
						totalPrice = totalPrice.add(noNameMoney);
					}
					// 组团社（渠道）
					income.put("agentName", orderIncome.get("agentName"));	
					// 单价
					String remark = "";
					if(new BigDecimal(orderIncome.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString().equals("0.000000")){
						remark = "";
					}else{
						remark = "改价总额：￥"+MoneyNumberFormat.getThousandsByRegex(new BigDecimal(orderIncome.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString(),2);
					}
					if(! orderIncome.get(type+"RefundSum").toString().equals("0.00")){
						if(StringUtils.isNotBlank(remark)){
							remark+="，退款：￥"+orderIncome.get(type+"RefundSum");
						}else{
							remark = "退款：￥"+orderIncome.get(type+"RefundSum");
						}
					}
					if(Integer.parseInt(orderIncome.get(type+"PersonCount").toString())>0){
						if(StringUtils.isNotBlank(remark)){
							remark += "，退团人数："+orderIncome.get(type+"PersonCount")+"人，退团剩余金额：￥"+orderIncome.get(type+"RefundGroupPrice");
						}else{
							remark = "退团人数："+orderIncome.get(type+"PersonCount")+"人，退团剩余金额：￥"+orderIncome.get(type+"RefundGroupPrice");
						}
					}
					if(Integer.parseInt(orderIncome.get(type+"ZGPersonCount").toString())>0){
						if(StringUtils.isNotBlank(remark)){
							remark += "，转团人数："+orderIncome.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+orderIncome.get(type+"ZGPriceSum");
						}else{
							remark = "转团人数："+orderIncome.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+orderIncome.get(type+"ZGPriceSum");
						}
					}
					
					income.put("price", MoneyNumberFormat.getThousandsByRegex(price.toString(), 2));
					income.put("remark", remark);
					// 人数
					income.put("personNum", num);	
					// 金额（人数*单价）
					income.put("totalPrice", MoneyNumberFormat.getThousandsByRegex(totalPrice.toString(), 2));	
					// 销售姓名
					income.put("saler", orderIncome.get("saler"));			
					exceptIncome4LZJQ.add(income);
				}else{
					BigDecimal totalPrice = new BigDecimal("0.00");
					//改价
					BigDecimal costPrice = new BigDecimal(orderIncome.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee.toString()));
					costPrice = costPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
					//退款
					BigDecimal refundPrice = new BigDecimal(orderIncome.get(type+"RefundSum").toString());
					if(Integer.parseInt(orderIncome.get(type+"PersonCount").toString())>0 && Integer.parseInt(orderIncome.get(type+"ZGPersonCount").toString())<=0){
						Map<String, Object> income = new HashMap<>();
						BigDecimal price = new BigDecimal(orderIncome.get(type + "Amount").toString());
						income.put("agentName", orderIncome.get("agentName"));	
						income.put("price", MoneyNumberFormat.getThousandsByRegex(price.toString(), 2));
						String remark = "";
						if(costPrice.toString().equals("0.00")){
							remark = "";
						}else{
							remark = "改价总额：￥"+MoneyNumberFormat.getThousandsByRegex(new BigDecimal(orderIncome.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString(),2);
						}
						if(! refundPrice.toString().equals("0.00")){
							if(StringUtils.isNotBlank(remark)){
								remark+="，退款：￥"+refundPrice;
							}else{
								remark = "退款：￥"+refundPrice;
							}
						}
						if(StringUtils.isNotBlank(remark)){
							remark += "，退团人数："+orderIncome.get(type+"PersonCount")+"人，退团剩余金额：￥"+orderIncome.get(type+"RefundGroupPrice");
						}else{
							remark = "退团人数："+orderIncome.get(type+"PersonCount")+"人，退团剩余金额：￥"+orderIncome.get(type+"RefundGroupPrice");
						}
						income.put("remark", remark);
						// 人数
						income.put("personNum", 0);	
						totalPrice = new BigDecimal(amountMap.get(type+"TotalPrice").toString()).subtract(refundPrice);
						// 金额（人数*单价）
						income.put("totalPrice",MoneyNumberFormat.getThousandsByRegex(totalPrice.toString(), 2) );	
						// 销售姓名
						income.put("saler", orderIncome.get("saler"));			
						exceptIncome4LZJQ.add(income);
					}
					if(Integer.parseInt(orderIncome.get(type+"PersonCount").toString())<=0 &&Integer.parseInt(orderIncome.get(type+"ZGPersonCount").toString())>0){
						Map<String, Object> income = new HashMap<>();
						BigDecimal price = new BigDecimal(orderIncome.get(type + "Amount").toString());
						income.put("agentName", orderIncome.get("agentName"));	
						income.put("price", MoneyNumberFormat.getThousandsByRegex(price.toString(), 2));
						String remark = "";
						if(costPrice.toString().equals("0.00")){
							remark = "";
						}else{
							remark = "改价总额：￥"+MoneyNumberFormat.getThousandsByRegex(new BigDecimal(orderIncome.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString(),2);
						}
						if(! refundPrice.toString().equals("0.00")){
							if(StringUtils.isNotBlank(remark)){
								remark+="，退款：￥"+refundPrice;
							}else{
								remark = "退款：￥"+refundPrice;
							}
						}
						if(StringUtils.isNotBlank(remark)){
							remark += "，转团人数："+orderIncome.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+orderIncome.get(type+"ZGPriceSum");
						}else{
							remark = "转团人数："+orderIncome.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+orderIncome.get(type+"ZGPriceSum");
						}
						income.put("remark", remark);
						// 人数
						income.put("personNum", 0);	
						// 金额（人数*单价）
						totalPrice = new BigDecimal(amountMap.get(type+"TotalPrice").toString()).subtract(refundPrice);
						income.put("totalPrice", MoneyNumberFormat.getThousandsByRegex(totalPrice.toString(), 2));	
						// 销售姓名
						income.put("saler", orderIncome.get("saler"));			
						exceptIncome4LZJQ.add(income);
					}
					if(Integer.parseInt(orderIncome.get(type+"PersonCount").toString())>0 &&Integer.parseInt(orderIncome.get(type+"ZGPersonCount").toString())>0){
						Map<String, Object> income = new HashMap<>();
						BigDecimal price = new BigDecimal(orderIncome.get(type + "Amount").toString());
						income.put("agentName", orderIncome.get("agentName"));	
						income.put("price", MoneyNumberFormat.getThousandsByRegex(price.toString(), 2));
						String remark = "";
						if(costPrice.toString().equals("0.00")){
							remark = "";
						}else{
							remark = "改价总额：￥"+MoneyNumberFormat.getThousandsByRegex(new BigDecimal(orderIncome.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString(),2);
						}
						if(! refundPrice.toString().equals("0.00")){
							if(StringUtils.isNotBlank(remark)){
								remark+="，退款：￥"+refundPrice;
							}else{
								remark = "退款：￥"+refundPrice;
							}
						}
						if(StringUtils.isNotBlank(remark)){
							remark += "，退团人数："+orderIncome.get(type+"PersonCount")+"人，退团剩余金额：￥"+orderIncome.get(type+"RefundGroupPrice");
						}else{
							remark = "退团人数："+orderIncome.get(type+"PersonCount")+"人，退团剩余金额：￥"+orderIncome.get(type+"RefundGroupPrice");
						}
						if(StringUtils.isNotBlank(remark)){
							remark += "，转团人数："+orderIncome.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+orderIncome.get(type+"ZGPriceSum");
						}else{
							remark = "转团人数："+orderIncome.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+orderIncome.get(type+"ZGPriceSum");
						}
						income.put("remark", remark);
						// 人数
						income.put("personNum", 0);	
						totalPrice = new BigDecimal(amountMap.get(type+"TotalPrice").toString()).subtract(refundPrice);
						// 金额（人数*单价）
						income.put("totalPrice",MoneyNumberFormat.getThousandsByRegex(totalPrice.toString(), 2) );	
						// 销售姓名
						income.put("saler", orderIncome.get("saler"));			
						exceptIncome4LZJQ.add(income);
					}
				}
			}
		}
		map.put("exceptIncome4LZJQ", exceptIncome4LZJQ);
		orderIncomes.add(map);
		return orderIncomes;
	}
	
	@Override
	public List<Map<String, Object>> getOrderAndRefundInfoSettle(Long productId, Integer orderType) {
		StringBuffer str = new StringBuffer();
		String lockedIn = "";
		String lockedInRefund = "";
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		if (Context.SUPPLIER_UUID_LZJQ.equals(currentCompanyUuid)){
			return getOrderAndRefundInfoSettleForLZJQ(productId,orderType);
		}
		//拉美途 过滤结算单锁定之后生成的订单数据，退款数据 C486 add by shijun.liu 2015.12.21
		// settle_locked_in = 1 表示是结算单锁定之后生成的数据
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
			lockedIn = " AND o.settle_locked_in is null ";
			lockedInRefund = " AND cost.settle_locked_in is null ";
		}
		 str.append("SELECT id,saler,agentName,")
		    .append(" IFNULL(totalMoney, 0) AS totalMoney,")
		    .append(" IFNULL(accountedMoney, 0) AS accountedMoney,")
		    .append(" IFNULL((totalMoney - accountedMoney), 0) AS notAccountedMoney,")
		    .append(" IFNULL(r.refundprice, 0) AS refundprice, ")
			.append(" orderPersonNum ")
		    .append(" FROM (")
		    .append(" SELECT o.id,")
		    .append(" (SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId) AS saler,")
		    .append(" (SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany) AS agentName,")
		    .append(" (SELECT ifnull(sum(m1.amount * m1.exchangerate), 0)  FROM money_amount m1 ")
		    .append(" WHERE m1.serialNum = o.total_money) AS totalMoney,")
		    .append(" (SELECT ifnull(sum(m2.amount * m2.exchangerate), 0) FROM money_amount m2 ")
		    .append(" WHERE m2.serialNum = o.accounted_money) AS accountedMoney, ")
			.append(" o.orderPersonNum ")
		    .append(" FROM ")
		    .append(" productorder o ")
		    .append(" WHERE o.delFlag = '0' ")
		    .append(" AND o.payStatus NOT IN (7, 99, 111)")
		    .append(" AND o.orderStatus = ").append(orderType)
		    .append(" AND o.productGroupId = ").append(productId)
		    .append(lockedIn)
		    .append(" ) t1 LEFT JOIN (")
		    .append(" SELECT cost.orderId,sum(cost.price) AS refundprice ")
		    .append(" FROM cost_record cost ")
		    .append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType =").append(orderType)
		    .append(" AND cost.budgetType = ").append(CostManageService.ACTUAL_TYPE).append(lockedInRefund);
		    //start 136 & 135 需求 拉美途要求返佣、退款审批通过后再计入结算单； by chy 2016年1月25日09:52:06
		    if(Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)){
		    	str.append(" AND cost.reviewStatus in ('审批通过', '审核通过') ");
		    } else {
		    	str.append(" AND cost.reviewStatus not in ('已取消','已驳回') ");
		    }
			//start 136 & 135 需求 拉美途要求返佣、退款审批通过后再计入结算单； by chy 2016年1月25日09:52:06
		 str.append(" GROUP BY cost.orderId ").append(" ) r on r.orderId = t1.id ");
		 return activityGroupDao.findBySql(str.toString(), Map.class);
	}


	/**
	 * 针对骡子假期的查询使用下面的sql。
	 * @param productId
	 * @param orderType
     * @return
	 * @author yudong.xu 2016.11.8
     */
	private List<Map<String, Object>> getOrderAndRefundInfoSettleForLZJQ(Long productId, Integer orderType) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t1.id, t1.saler, t1.agentName,IFNULL(t1.totalMoney, 0) AS totalMoney, ")
		.append("IFNULL(t1.accountedMoney, 0) AS accountedMoney,IFNULL((t1.totalMoney - t1.accountedMoney), 0 )")
		.append(" AS notAccountedMoney,t1.orderPersonNum,FORMAT(t1.adultPrice,2) AS adultPrice,")
		.append("FORMAT(t1.childPrice,2) AS childrenPrice,FORMAT(t1.specialPrice,2) AS specialPrice,")
		.append(" t1.adultNum,t1.childNum AS childrenNum, t1.specialNum,")
		.append("(t1.adultPrice * t1.adultNum) AS adultMoney,")
		.append("(t1.childPrice * t1.childNum) AS childrenMoney,")
		.append("(t1.specialPrice * t1.specialNum) AS specialMoney, ")
		.append(" IFNULL(r.refundprice, 0) AS refundprice, ")
		.append(" IFNULL(t1.adultCostPriceSum,0) adultCostPriceSum,IFNULL(t1.childrenCostPriceSum,0) childrenCostPriceSum," +
		   					"IFNULL(t1.specialCostPriceSum,0) specialCostPriceSum, ")
		.append("IFNULL(adultRefundSum,0) adultRefundSum, ")
		.append("IFNULL(childrenRefundSum,0) childrenRefundSum, ")
		.append("IFNULL(specialRefundSum,0) specialRefundSum, ")
		.append("IFNULL(adultRefundGroupPrice, 0) adultRefundGroupPrice, ")
		.append("IFNULL(childrenRefundGroupPrice, 0) childrenRefundGroupPrice, ")
		.append("IFNULL(specialRefundGroupPrice, 0) specialRefundGroupPrice, ")
		.append("IFNULL(adultPersonCount, 0) adultPersonCount, ")
		.append("IFNULL(childrenPersonCount, 0) childrenPersonCount, ")
		.append("IFNULL(specialPersonCount, 0) specialPersonCount, ")
		.append("IFNULL(adultZGPriceSum, 0) adultZGPriceSum, ")
		.append("IFNULL(childrenZGPriceSum, 0) childrenZGPriceSum, ")
		.append("IFNULL(specialZGPriceSum, 0) specialZGPriceSum, ")
		.append("IFNULL(adultZGPersonCount, 0) adultZGPersonCount, ")
		.append("IFNULL(childrenZGPersonCount, 0) childrenZGPersonCount, ")
		.append("IFNULL(specialZGPersonCount, 0) specialZGPersonCount, ")
		.append("IFNULL(adultCostPriceSumZero, 0) adultCostPriceSumZero, ")
		.append("IFNULL(childrenCostPriceSumZero, 0) childrenCostPriceSumZero, ")
		.append("IFNULL(specialCostPriceSumZero, 0) specialCostPriceSumZero ")
		.append(" FROM ")
		.append("( SELECT o.id, ( SELECT su. NAME FROM sys_user su WHERE su.id = o.salerId ) AS saler,")
		.append("(SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany ) AS agentName,")
		.append("( SELECT ifnull( sum(m0.amount * m0.exchangerate), 0 ) FROM money_amount m0 WHERE ")
		.append(" m0.serialNum = o.total_money ) AS totalMoney,")
		.append("(SELECT ifnull( sum(m1.amount * m1.exchangerate), 0 ) FROM money_amount m1 WHERE ")
		.append(" m1.serialNum = o.accounted_money ) AS accountedMoney,")
		.append("( SELECT ifnull( m2.amount * m2.exchangerate, 0 )")
		.append(" FROM money_amount m2 WHERE m2.serialNum = o.settlementAdultPrice ) AS adultPrice, ")
		.append(" ( SELECT ifnull( m3.amount * m3.exchangerate, 0 ) FROM money_amount m3 WHERE ")
		.append(" m3.serialNum = o.settlementcChildPrice ) AS childPrice,(SELECT ifnull( m4.amount * m4.exchangerate, 0)")
		.append(" FROM money_amount m4 WHERE m4.serialNum = o.settlementSpecialPrice ) AS specialPrice, ")
		.append(" o.orderPersonNum, o.orderPersonNumAdult AS adultNum, o.orderPersonNumChild AS childNum, ")
		.append(" o.orderPersonNumSpecial AS specialNum,  ")
		.append("SUM(case when rn.`status`=2 and rn.process_type =10 and c.businessType=1 and tv.personType=1 then costPrice*currency.currency_exchangerate end ) adultCostPriceSum, ")
	    .append("SUM(case when rn.`status`=2 and rn.process_type =10 and c.businessType=1 and tv.personType=2 then costPrice*currency.currency_exchangerate end ) childrenCostPriceSum,")
	    .append("SUM(case when rn.`status`=2 and rn.process_type =10 and c.businessType=1 and tv.personType=3 then costPrice*currency.currency_exchangerate end ) specialCostPriceSum, ")
	    .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =1 and cr.reviewType=1 and cr.delFlag =0 and trv.personType = 1 and cr.orderType="+orderType+" and cr.budgetType =0 THEN cr.price END) adultRefundSum,")
	    .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =1 and cr.reviewType=1 and cr.delFlag =0 and trv.personType = 2 and cr.orderType="+orderType+" and cr.budgetType =0  THEN cr.price END) childrenRefundSum,")
	    .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =1 and cr.reviewType=1 and cr.delFlag =0 and trv.personType = 3 and cr.orderType="+orderType+" and cr.budgetType =0  THEN cr.price END) specialRefundSum, ")
	    .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 1  AND tra.delFlag = 3 AND rp.moneyType = 25 THEN rp.amount END ) adultRefundGroupPrice, ")
	    .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 2 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN rp.amount END ) childrenRefundGroupPrice, ")
	    .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 3 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN rp.amount END ) specialRefundGroupPrice, ")
	    .append("count(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 1 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN tra.id END) adultPersonCount, ")
	    .append("count(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 2 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN tra.id END) childrenPersonCount, ")
	    .append("count(CASE WHEN rn.`status`=2 and rn.process_type =8 and tra.personType = 3 AND tra.delFlag = 3 AND rp.moneyType = 25 THEN tra.id END) specialPersonCount, ")	
	    .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType = 1 AND tra.delFlag =5 THEN ma.amount*ma.exchangerate  END) adultZGPriceSum, ")
	    .append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType = 2 AND tra.delFlag =5 THEN ma.amount*ma.exchangerate END) childrenZGPriceSum, ")
		.append("SUM(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType = 3 AND tra.delFlag =5 THEN ma.amount*ma.exchangerate END) specialZGPriceSum, ")
		.append("count(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType=1 AND tra.delFlag = 5 THEN tra.id END) adultZGPersonCount, ")
		.append("count(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType=2 AND tra.delFlag = 5 THEN tra.id END) childrenZGPersonCount, ")
		.append("count(CASE WHEN rn.`status`=2 and rn.process_type =11 and tra.personType=3 AND tra.delFlag = 5 THEN tra.id END) specialZGPersonCount, ")
		.append("SUM(CASE WHEN rn.`status` = 2 and rn.process_type =10 AND c.businessType = 1 AND tv.personType = 1 AND costPrice<0 THEN costPrice*currency.currency_exchangerate END ) adultCostPriceSumZero, ")
		.append("SUM(CASE WHEN rn.`status` = 2 and rn.process_type =10 AND c.businessType = 1 AND tv.personType = 2 AND costPrice<0 THEN costPrice*currency.currency_exchangerate END ) childrenCostPriceSumZero, ")
		.append("SUM(CASE WHEN rn.`status` = 2 and rn.process_type =10 AND c.businessType = 1 AND tv.personType = 3 AND costPrice<0 THEN costPrice*currency.currency_exchangerate END ) specialCostPriceSumZero ")
		.append("FROM productorder o ")
		.append("LEFT JOIN review_new rn ON o.id = rn.order_id LEFT JOIN costchange c ON rn.id = c.review_uuid  ")
		.append("LEFT JOIN traveler tv ON tv.id = c.travelerId ")
		.append("LEFT JOIN cost_record cr ON cr.reviewUuid = rn.id ")
		.append("LEFT JOIN traveler trv ON trv.id = rn.traveller_id ")
		.append("LEFT JOIN review_process_money_amount rp ON rp.reviewId = rn.id ")
		.append("LEFT JOIN traveler tra ON rn.traveller_id = tra.id ")
		.append("LEFT JOIN money_amount ma ON tra.subtract_moneySerialNum = ma.serialNum ")
		.append("	LEFT JOIN currency currency ON currency.currency_id = c.price_currency ")
		.append("WHERE o.delFlag = '0' ")
		.append(" AND o.payStatus NOT IN (7, 99, 111) AND o.orderStatus = ? AND o.productGroupId = ? group by o.id) t1 ")
		.append(" LEFT JOIN ( SELECT cost.orderId, sum(cost.price) AS refundprice FROM cost_record cost ")
		.append(" WHERE cost.reviewType = 1 AND cost.delFlag = '0' AND cost.orderType = ? AND cost.budgetType = 1 ")
		.append(" AND cost.reviewStatus NOT IN ('已取消', '已驳回') GROUP BY cost.orderId ) r ON r.orderId = t1.id ");
		List<Map<String,Object>> list = activityGroupDao.findBySql(sql.toString(), Map.class,orderType,productId,orderType);
		// 报名游客类型 adult--成人 children--儿童 special--特殊人群
		String[] customerType = {"adult", "children", "special"};
		Currency currency = currencyService.findRMBCurrency();
		for(Map<String,Object> map : list){
			ProductOrderCommon orderCommon = productOrderService.getProductorderById(Long.parseLong(map.get("id").toString()));
			Map<String, Object> amountMap = getTravelerJieSuanJia(map.get("id").toString());
			Map<String, Object> countMap = productOrderService.getTravelerCount(orderCommon.getId());
			for(String type : customerType){
				Integer num = (Integer) map.get(type + "Num");
				//改价所有的负值
				BigDecimal priceDecimal = new BigDecimal(map.get(type+"CostPriceSumZero").toString());
				//服务费
				Double fee = 0.00;
				if(orderCommon.getPriceType()==2){
					fee = TravelerUtils.getServiceFee(orderCommon, currency, priceDecimal.toString());
				}
				if(num> 0){
					//转团人数
					int zgroupNum = Integer.parseInt(map.get(type+"ZGPersonCount").toString());
					//退团人数
					int refundNum = Integer.parseInt(map.get(type+"PersonCount").toString());
					//原始订单人数
					Integer count = num + zgroupNum+refundNum;
					//游客表里存在的游客人数
					Integer personNum = Integer.parseInt(countMap.get(type+"Num").toString());
					//没有添加游客的金额
					BigDecimal noNameMoney = new BigDecimal(count-personNum).multiply(new BigDecimal(map.get(type+"Price").toString().replace(",","")));
					BigDecimal adultMoney = new BigDecimal(amountMap.get(type+"TotalPrice").toString()).subtract(new BigDecimal(map.get(type+"RefundSum").toString()));
					if(count - personNum >= 0){
						adultMoney = adultMoney.add(noNameMoney);
					}
					if(amountMap.get(type+"TotalPrice").toString().equals("0.000000")){
						adultMoney = new BigDecimal(map.get(type+"Money").toString());
					}
					String remark = "";
					if(new BigDecimal(map.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString().equals("0.000000")){
						remark = "";
					}else{
						remark = "改价总额：￥"+MoneyNumberFormat.getThousandsByRegex(new BigDecimal(map.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString(),2);
					}
					if(! map.get(type+"RefundSum").toString().equals("0.00")){
						if(StringUtils.isNotBlank(remark)){
							remark+="，退款：￥"+map.get(type+"RefundSum");
						}else{
							remark = "退款：￥"+map.get(type+"RefundSum");
						}
					}
					if(Integer.parseInt(map.get(type+"PersonCount").toString())>0){
						if(StringUtils.isNotBlank(remark)){
							remark += "，退团人数："+map.get(type+"PersonCount")+"人，退团剩余金额：￥"+map.get(type+"RefundGroupPrice");
						}else{
							remark = "退团人数："+map.get(type+"PersonCount")+"人，退团剩余金额：￥"+map.get(type+"RefundGroupPrice");
						}
					}
					if(Integer.parseInt(map.get(type+"ZGPersonCount").toString())>0){
						if(StringUtils.isNotBlank(remark)){
							remark += "，转团人数："+map.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+MoneyNumberFormat.getThousandsByRegex(map.get(type+"ZGPriceSum").toString(),2);
						}else{
							remark = "转团人数："+map.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+MoneyNumberFormat.getThousandsByRegex(map.get(type+"ZGPriceSum").toString(),2);
						}
					}
					map.put(type+"Remark", remark);
					map.put(type+"Money",MoneyNumberFormat.getThousandsByRegex(adultMoney.toString(), 2));
				}else{
					BigDecimal totalPrice = new BigDecimal("0.00");
					//改价
					BigDecimal costPrice = new BigDecimal(map.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee.toString()));
					costPrice = costPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
					//退款
					BigDecimal refundPrice = new BigDecimal(map.get(type+"RefundSum").toString());
					if(Integer.parseInt(map.get(type+"PersonCount").toString())>0 && Integer.parseInt(map.get(type+"ZGPersonCount").toString())<=0){
						String remark = "";
						if(costPrice.toString().equals("0.00")){
								remark = "";
						}else{
							remark = "改价总额：￥"+MoneyNumberFormat.getThousandsByRegex(new BigDecimal(map.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString(),2);
						}
						if(! refundPrice.toString().equals("0.00")){
							if(StringUtils.isNotBlank(remark)){
								remark+="，退款：￥"+refundPrice;
							}else{
								remark = "退款：￥"+refundPrice;
							}
						}
						if(StringUtils.isNotBlank(remark)){
							remark += "，退团人数："+map.get(type+"PersonCount")+"人，退团剩余金额：￥"+map.get(type+"RefundGroupPrice");
						}else{
							remark = "退团人数："+map.get(type+"PersonCount")+"人，退团剩余金额：￥"+map.get(type+"RefundGroupPrice");
						}
						map.put(type+"Remark", remark);
						totalPrice= new BigDecimal(amountMap.get(type+"TotalPrice").toString()).subtract(refundPrice);
						map.put(type+"Money",MoneyNumberFormat.getThousandsByRegex(totalPrice.toString(), 2));
					}
					if(Integer.parseInt(map.get(type+"PersonCount").toString())<=0 && Integer.parseInt(map.get(type+"ZGPersonCount").toString())>0){
						String remark = "";
						if(costPrice.toString().equals("0.00")){
							remark = "";
						}else{
							remark = "改价总额：￥"+MoneyNumberFormat.getThousandsByRegex(new BigDecimal(map.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString(),2);
						}
						if(! refundPrice.toString().equals("0.00")){
							if(StringUtils.isNotBlank(remark)){
								remark+="，退款：￥"+refundPrice;
							}else{
								remark = "退款：￥"+refundPrice;
							}
						}
						if(StringUtils.isNotBlank(remark)){
							remark += "，转团人数："+map.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+MoneyNumberFormat.getThousandsByRegex(map.get(type+"ZGPriceSum").toString(),2);
						}else{
							remark = "转团人数："+map.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+MoneyNumberFormat.getThousandsByRegex(map.get(type+"ZGPriceSum").toString(),2);
						}
						map.put(type+"Remark", remark);
						totalPrice= new BigDecimal(amountMap.get(type+"TotalPrice").toString()).subtract(refundPrice);
						map.put(type+"Money",MoneyNumberFormat.getThousandsByRegex(totalPrice.toString(), 2));
					}
					if(Integer.parseInt(map.get(type+"PersonCount").toString())>0 && Integer.parseInt(map.get(type+"ZGPersonCount").toString())>0){
						String remark = "";
						if(costPrice.toString().equals("0.00")){
							remark = "";
						}else{
							remark = "改价总额：￥"+MoneyNumberFormat.getThousandsByRegex(new BigDecimal(map.get(type+"CostPriceSum").toString()).add(new BigDecimal(fee+"")).toString(),2);
						}
						if(! refundPrice.toString().equals("0.00")){
							if(StringUtils.isNotBlank(remark)){
								remark+="，退款：￥"+refundPrice;
							}else{
								remark = "退款：￥"+refundPrice;
							}
						}
						if(StringUtils.isNotBlank(remark)){
							remark += "，退团人数："+map.get(type+"PersonCount")+"人，退团剩余金额：￥"+map.get(type+"RefundGroupPrice");
						}else{
							remark = "退团人数："+map.get(type+"PersonCount")+"人，退团剩余金额：￥"+map.get(type+"RefundGroupPrice");
						}
						if(StringUtils.isNotBlank(remark)){
							remark += "，转团人数："+map.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+MoneyNumberFormat.getThousandsByRegex(map.get(type+"ZGPriceSum").toString(),2);
						}else{
							remark = "转团人数："+map.get(type+"ZGPersonCount")+"人，转团剩余金额：￥"+MoneyNumberFormat.getThousandsByRegex(map.get(type+"ZGPriceSum").toString(),2);
						}
						map.put(type+"Remark", remark);
						totalPrice= new BigDecimal(amountMap.get(type+"TotalPrice").toString()).subtract(refundPrice);
						map.put(type+"Money",MoneyNumberFormat.getThousandsByRegex(totalPrice.toString(), 2));
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 为批发商青岛凯撒提供特定的团号生成规则(出境游)
	 * @author nan
	 * @DateTime 2015年8月19日
	 * @return String
	 * @param deptCode：部门团号缩写
	 * @param groupOpenDate：单团类（出团日期） 签证、机票订单（null） 
	 * 规则："CRTS-TW-Z20150612001"
	 * "公司名称+部门编码+团号姓氏缩写+出团日期(yyyyMMdd)+序号"
	 */
	public synchronized String getGroupNumForCRTS(String deptCode, String groupOpenDate) {
		//声明带有青岛凯撒前缀团号
		StringBuffer groupNum = new StringBuffer();
		if(StringUtils.isNotBlank(deptCode)) {
			User user = UserUtils.getUser();
			//公司名称
			groupNum.append("CRTS");
			groupNum.append(Context.GROUPCODE_SPLIT_FLAG);
			//部门编码
			groupNum.append(deptCode);
			groupNum.append(Context.GROUPCODE_SPLIT_FLAG);
			//团号姓氏缩写
			groupNum.append(user.getGroupeSurname());
			//出团日期(yyyyMMdd)
			groupNum.append(groupOpenDate.replace("-", ""));
//			if("KR".equals(deptCode)) {//韩国
//				groupNum.append(groupOpenDate.replace("-", "").substring(2));
//			}else{
//				groupNum.append(groupOpenDate.replace("-", ""));
//			}
			
			//查询团号
			List<ActivityGroup> activityGroupList = activityGroupDao.findGroupCodeByUserId(groupNum.toString()+"%", user.getId());
			
			//序号
			if(activityGroupList != null && activityGroupList.size() > 0){
				String groupCode = activityGroupList.get(0).getGroupCode();
				groupCode = groupCode.substring(groupCode.length()-3);
				Integer num = Integer.parseInt(groupCode) + 1;
				groupNum.append("000".substring(0,3-num.toString().length())+num);
			}else{
				groupNum.append("001");
			}
//			if(activityGroupList != null && activityGroupList.size() > 0){
//				String groupCode = activityGroupList.get(0).getGroupCode();
//				if("KR".equals(deptCode)) {//韩国
//					groupCode = groupCode.substring(groupCode.length()-1);
//					char c = groupCode.charAt(0);
//					groupNum.append((char)(c+1));
//				}else{
//					groupCode = groupCode.substring(groupCode.length()-3);
//					Integer num = Integer.parseInt(groupCode) + 1;
//					groupNum.append("000".substring(0,3-num.toString().length())+num);
//				}
//			}else{
//				if("KR".equals(deptCode)) {//韩国
//					groupNum.append("A");
//				}else{
//					groupNum.append("001");
//				}
//			}
			//验证新生成团号在产品基本信息表和机票产品信息表是否存在。
			boolean flag = false;
			while(!flag){
				if(!groupNoCheck(groupNum.toString())){
					flag =true;
				}else{
					String groupCode = groupNum.toString();
					String groupCodeBefore = groupCode.substring(0,groupCode.length()-3);
					String groupCodeAfter = groupCode.substring(groupCode.length()-3);
					Integer num = Integer.parseInt(groupCodeAfter) + 1;
					groupNum.delete(0, groupNum.length());
					groupNum.append(groupCodeBefore);
					groupNum.append("000".substring(0,3-num.toString().length())+num);
				}
			}
		}
		return groupNum.toString();
	}
	
	/**
	 * 为批发商名扬国际提供特定的团号生成规则
	 * @author nan
	 * @DateTime 2015年10月20日
	 * @return String
	 * @param deptCode：部门团号缩写
	 * @param groupOpenDate：单团类（出团日期） 签证、机票订单（null） 
	 * 规则："JPZ20151020001"
	 * "区域首字母+出团日期(yyyyMMdd)+序号(001-999)"
	 */
	public synchronized String getGroupNumForMYGJ(String deptCode, String groupOpenDate) {
		StringBuffer groupNum = new StringBuffer();
		String groupCode = "";
		if(StringUtils.isNotBlank(deptCode)) {
			//团号标识
			groupNum.append(deptCode);
			//出团日期(yyyyMMdd)
			groupNum.append(groupOpenDate.replace("-", ""));
			
			//查询团号
			String companyUUID = UserUtils.getUser().getCompany().getUuid();
			StringBuffer sql = new StringBuffer();
			sql.append("select gc.groupCode,gc.companyUUID from groupcode_companyuuid gc where gc.groupCode like '"+groupNum+"%' and gc.companyUUID='"+companyUUID+"' ORDER BY groupCode desc");
			List<Map<String,String>> groupCodeList = activityGroupDao.findBySql(sql.toString(), Map.class);
			
			//序号
			if(groupCodeList != null && groupCodeList.size() > 0){
				String groupNo = groupCodeList.get(0).get("groupCode");
				groupNo = groupNo.substring(groupNo.length()-3);
				Integer num = Integer.parseInt(groupNo) + 1;
				if(num>=1000){
					return "";
				}
				groupNum.append("000".substring(0,3-num.toString().length())+num);
			}else{
				groupNum.append("001");
			}
			//验证新生成团号在产品基本信息表和机票产品信息表是否存在。
			groupCode = groupCodeCheck(groupNum.toString());
		}
		return groupCode;
	}
	
	/**
	 * 取得没有重复的团号，有重复就+1(各个产品线，包括机票，签证)
	 * @author nan
	 * @DateTime 2015年8月12日 15:30:00
	 * @return String
	 * @param groupCode：团号
	 */
	public synchronized String groupCodeCheck(String groupCode) {
		if(StringUtils.isNotBlank(groupCode)) {
			//查询团号
			String companyUUID = UserUtils.getUser().getCompany().getUuid();
			boolean flag = false;
			while(!flag){
				StringBuffer sql = new StringBuffer();
				sql.append("select gc.groupCode,gc.companyUUID from groupcode_companyuuid gc where gc.groupCode = '"+groupCode+"' and gc.companyUUID='"+companyUUID+"' ORDER BY groupCode desc");
				List<Map<String,String>> groupCodeList = activityGroupDao.findBySql(sql.toString(), Map.class);
				
				if(groupCodeList != null && groupCodeList.size() > 0){
					String groupCodeBefore = groupCode.substring(0,groupCode.length()-3);
					String groupCodeAfter = groupCode.substring(groupCode.length()-3);
					Integer num = Integer.parseInt(groupCodeAfter) + 1;
					if(num>=1000){
						return "";
					}
					groupCode = groupCodeBefore+("000".substring(0,3-num.toString().length())+num);
				}else{
					flag = true;
				}
			}
			return groupCode;
		}else{
			return groupCode;
		}
	}
	
	/**
	 * 验证团号是否已存在(各个团期类产品线及签证机票)
	 * @author nan
	 * @DateTime 2015年10月26日
	 * @param groupCode：团号
	 * @return boolean true:团号已存在
	 */
	public synchronized boolean groupNoCheck(String groupCode) {
		boolean flag = false;
		groupCode = filterCtrlChars(groupCode);
		groupCode = groupCode.replace("\\", "\\\\");
		//查询团号
		String companyUUID = UserUtils.getUser().getCompany().getUuid();
		StringBuffer sql = new StringBuffer();
		sql.append("select gc.groupCode,gc.companyUUID from groupcode_companyuuid gc where gc.groupCode = '"+groupCode+"' and gc.companyUUID='"+companyUUID+"' ORDER BY groupCode desc");
		List<Map<String,String>> groupCodeList = activityGroupDao.findBySql(sql.toString(), Map.class);
			
		if(groupCodeList != null && groupCodeList.size() > 0){
			flag = true;
		}
		return flag;
	}

/**
 * 校验团号是否有重复  团期类产品修改团号是否重复判断
 * @param set
 * @param id
 * @return
 */
	public int getGroupSize(Set<String> set, String id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT  gc.groupCode FROM   groupcode_companyuuid gc  WHERE  gc.groupCode  NOT  IN ");
		buffer.append("(SELECT  a.groupCode FROM activitygroup a LEFT  JOIN  travelactivity t ON    a.srcActivityId = t.id WHERE t.id=");
		buffer.append(id);
		buffer.append(") AND gc.groupCode IS NOT NULL AND gc.groupCode IN ( ");
		int i=0;
		for (String str : set) { 
			
			str = filterCtrlChars(str);
			str = str.replace("\\", "\\\\");
			
			i++;
		    if(i == set.size())  
		    {
		    	buffer.append("'"+str+"'");
		    }
		    else
		    {
		    	buffer.append("'"+str+"',");
		    }
		} 
		buffer.append(")");
		buffer.append(" AND gc.companyUUID ='");
		buffer.append(UserUtils.getUser().getCompany().getUuid());
		buffer.append("'");
		List<?> list  =activityGroupDao.findBySql(buffer.toString(), Map.class);
		return list.size();
	}
	
	/**
	 * 依据团期id，获取各种游客优惠额度
	 * @author yang.jiang 2016-2-16 20:27:53 
	 * @param groupId 团期id
	 * @return
	 */
	public Map<String, Object> getDiscountMapByGroupId(String groupId){
		
		Map<String, Object> discountMap = new HashMap<>();
		//默认人民币 0
		Currency currencyRMB = currencyService.findRMBCurrency();
		AmountBean amountRMB=null;
		if (currencyRMB!=null) {
			amountRMB = new AmountBean(BigDecimal.ZERO, currencyRMB);
		}
		discountMap.put("groupId", groupId);
		discountMap.put("adult", amountRMB);
		discountMap.put("child", amountRMB);
		discountMap.put("special", amountRMB);
		
		//获取团期
		ActivityGroup group = activityGroupDao.findOne(Long.parseLong(groupId));
		if (group != null) {
			//获取团期中诸币种
			String currencys = group.getCurrencyType();
			String [] currencyArray;
			if (StringUtils.isNotBlank(currencys)) {
				//拆分币种，获取散拼产品币种数组的第9，第10，第11个币种
				currencyArray = currencys.split(",");
				if (currencyArray.length == 11) {
					//成人优惠额
					Currency tempCurrency = currencyService.findCurrency(Long.parseLong(currencyArray[8]));
					AmountBean tempAmount = new AmountBean(group.getAdultDiscountPrice(), tempCurrency);
					discountMap.put("adult", tempAmount);
					//儿童优惠额
					tempCurrency = currencyService.findCurrency(Long.parseLong(currencyArray[9]));
					tempAmount = new AmountBean(group.getChildDiscountPrice(), tempCurrency);
					discountMap.put("child", tempAmount);
					//老人优惠额
					tempCurrency = currencyService.findCurrency(Long.parseLong(currencyArray[10]));
					tempAmount = new AmountBean(group.getSpecialDiscountPrice(), tempCurrency);
					discountMap.put("special", tempAmount);
				}
			}
		}
		return discountMap;
	}
	
	/**
	 * 查询同一产品下团号相同的団期id，仅游轮新增舱型时产生
	 * for bug12826
	 * @param groupCode
	 * @param productId
	 * @return
	 */
	public List<Object> getGroupIdByGroupCode(String groupCode,String groupCodeOld,String productId,String groupOpenDate){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT a.id FROM activitygroup a WHERE (a.groupCode=? OR a.groupCode=?) AND a.srcActivityId=? AND a.groupOpenDate=? AND a.delFlag=0");
		
		return activityGroupDao.findBySql(buffer.toString(),groupCode,groupCodeOld,productId,groupOpenDate);
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
			JSONObject jsonObject = JSONObject.fromObject(map);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	@Override
	public JSONArray getAllActivityByType(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Map<String, Object>> getAllCreators(Integer activityType) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public JSONArray getActivityInfoByRemind(Remind remind) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据团期的id，查询对应的成人同行价，预计收款。(都联表转换成人民币)
	 * 获取团期的成人同行价，以及预计收款。预计收款=成人同行价*预收人数*汇率。针对拉美图需求0396.
	 * @author yudong.xu --2016/4/21--20:09
	 */
	public Map<String,Object> getGroupInfo(Long groupId){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT FORMAT(g.planPosition*g.settlementAdultPrice*c.currency_exchangerate,2) AS preIncome,")
				.append("FORMAT(g.settlementAdultPrice*c.currency_exchangerate,2) AS adultPrice,g.planPosition FROM activitygroup g,currency c")
				.append(" WHERE g.id=? AND c.currency_id = SUBSTRING_INDEX(g.currency_type,',',1);");
		List<Map<String,Object>> result =  activityGroupDao.findBySql(sql.toString(),Map.class,groupId);
		if (result.size() == 0){
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("preIncome","0.00");
			temp.put("adultPrice","0.00");
			temp.put("planPosition","0");
			return temp;
		}
		return result.get(0);
	}

	/**
	 * 根据订单类型orderType,判断是否是拉美图的团期
	 * @author yudong.xu --2016/4/21--21:32
	 */
	public static boolean isLMTTuanQi(Integer orderType){
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)){
			if (orderType > 0 && orderType < 6 || orderType == 10){
				return true;
			}
		}
		return false;
	}


	@Override
	public void updateObj(ActivityGroup activityGroup) {
		this.activityGroupDao.updateObj(activityGroup);
	}

	public ActivityGroup findByGroupCodeAndOffice(String groupCode, Long officeId) {
		ActivityGroup activityGroup = null;
		if(StringUtils.isNotBlank(groupCode)) {
			List<ActivityGroup> groupList = activityGroupDao.findByGroupCodeAndCompany(groupCode, officeId);
			if(!groupList.isEmpty()) {
				activityGroup = groupList.get(0);
			}
		}
		return activityGroup;
	}

	public void updateActivityGroup(String groupId, Integer isT1) {
		//更新团期表中T1上架状态
		String sql = "UPDATE activitygroup set is_t1 = ? where id = ?";
		activityGroupDao.updateBySql(sql, isT1, groupId);
	}

	public List<Map<String, Object>> findLogProductList(Long groupId) {
		return logProductDao.findLogProductList(groupId);
	}
	
	@Override
	public Map<String, Object> countOrderChildAndSpecialNum(Long productGroupId,String containSelf) {
		 StringBuffer sb = new StringBuffer();
		 	if(StringUtils.isBlank(containSelf)){
		 		sb.append("select SUM(orderPersonNumChild) orderPersonNumChild,sum(orderPersonNumSpecial) orderPersonNumSpecial from productorder where productGroupId =? and payStatus in (3,4,5) and delFlag = 0 ");
		 		List<Map <String, Object>> map =  activityGroupDao.findBySql(sb.toString(), Map.class, productGroupId);
		 		return map.get(0);
		 	}else{
		 		sb.append("select SUM(orderPersonNumChild) orderPersonNumChild,sum(orderPersonNumSpecial) orderPersonNumSpecial from productorder where productGroupId =? and id != ? and  payStatus in (3,4,5) and delFlag = 0 ");
		 		List<Map <String, Object>> map =  activityGroupDao.findBySql(sb.toString(), Map.class, productGroupId,containSelf);
		 		return map.get(0);

		 	}
	}

	/**
	 * 更新团期中的设置价格策略状态
	 * @param groupId
	 * @param status
     */
	public void updatePSStatusById(Long groupId, Integer status){
		activityGroupDao.updatePSStatusById(groupId, status);
	}
	
	/**
	 * 通过orderId获得游客结算价
	 * @param travelerIds
	 * @return
	 */
	public Map<String,Object> getTravelerJieSuanJia(String orderId){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("IFNULL(sum(CASE WHEN tr.personType =1 THEN ma.amount * ma.exchangerate END),0.00) adultTotalPrice, ");
		sbf.append("IFNULL(sum(CASE WHEN tr.personType =2 THEN ma.amount * ma.exchangerate END),0.00) childrenTotalPrice, ");
		sbf.append("IFNULL(sum(CASE WHEN tr.personType =3 THEN ma.amount * ma.exchangerate END),0.00) specialTotalPrice ");
		sbf.append("FROM ");
		sbf.append("productorder o ");
		sbf.append("LEFT JOIN traveler tr ON tr.orderId = o.id ");
		sbf.append("LEFT JOIN money_amount ma ON tr.payPriceSerialNum = ma.serialNum ");
		sbf.append("WHERE ");
		sbf.append("o.id = ? ");
		sbf.append("group by o.id ");
		List<Map<String,Object>> list = activityGroupDao.findBySql(sbf.toString(), Map.class, orderId);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
