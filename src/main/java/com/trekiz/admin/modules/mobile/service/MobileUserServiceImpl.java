package com.trekiz.admin.modules.mobile.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.trekiz.admin.agentToOffice.agentInfo.dao.AgentInfoDao;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.mobile.entity.CorrelationUser;
import com.trekiz.admin.modules.mobile.entity.MobileUser;
import com.trekiz.admin.modules.mobile.repository.MobileUserDao;
import com.trekiz.admin.modules.order.entity.OrderProgressTracking;
import com.trekiz.admin.modules.order.repository.OrderProgressTrackingDao;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.repository.SupplierContactsDao;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;

@Service
@Transactional(readOnly = true)
public class MobileUserServiceImpl extends BaseService implements MobileUserService{

	@Autowired
	private MobileUserDao mobileUserDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private AgentInfoDao agentInfoDao;

	@Autowired
	private SupplierContactsDao supplierContactsDao;
	
	@Autowired
    private TravelActivityDao activityDao;
	
	@Autowired
    private OrderProgressTrackingDao progressDao;

	/**
	 * 微信账号列表
	 * param 搜索项
	 */
	public JSONObject selectUserList( Map<String, Object> paramMap){
		JSONObject map = new JSONObject();
		//待关联0、已关联1、已删除2
		String type = checkNull(paramMap.get("type")) == "" ? "0" : checkNull(paramMap.get("type")).toString();
		StringBuffer sql = new StringBuffer();
		//是否关联T1账号type(0:未关联，1：已关联，2：已删除)
		//如果type==2 则查询已删除的微信用户信息，type == 1为未关联T1账号的微信用户信息,type == 0则表示未关联T1账号的微信账号的用户信息 
		if(Integer.parseInt(type) == 0){	//未关联
			sql.append("SELECT m1.id, m1.agentName, m1.`name`, m1.telephone, m1.wechatCode, CONCAT(IfNULL(m1.areaCode,''),'-',m1.phone) as phone,m1.t1UserId,m1.unbundleReason FROM mobile_user m1  WHERE  m1.isRegist = '1' and m1.delFlag = 0 ");
		}else if(Integer.parseInt(type) == 1){	//已关联
			sql.append("SELECT m1.id, m1.agentName, m1.`name`, m1.telephone, m1.wechatCode, CONCAT(IfNULL(m1.areaCode,''),'-',m1.phone) as phone, m1.t1UserId, u.loginName " +
						" FROM mobile_user m1 LEFT JOIN sys_user u ON u.id = m1.t1UserId WHERE m1.isRegist = '1' and m1.delFlag = 2 ");
		}else if (Integer.parseInt(type) == 2){	//已删除
			sql.append("SELECT m1.id, m1.agentName, m1.`name`, m1.telephone, m1.wechatCode, CONCAT(IfNULL(m1.areaCode,''),'-',m1.phone) as phone,m1.t1UserId,m1.unbundleReason FROM mobile_user m1  WHERE  m1.isRegist = '0' and m1.delFlag = 1 ");
		}

		//电话号
		if(StringUtils.isNotBlank(checkNull(paramMap.get("telephone")))){
			sql.append(" and m1.telephone like '%" +paramMap.get("telephone") +"%' ");
		}
		//座机区号
		if(StringUtils.isNotBlank(checkNull(paramMap.get("areaCode")))){
			sql.append(" and m1.areaCode like '%"+ paramMap.get("areaCode")+"%' ");
		}
		//座机
		if(StringUtils.isNotBlank(checkNull(paramMap.get("phone")))){
			sql.append(" and m1.phone like '%"+ paramMap.get("phone")+"%' ");
		}
		//用户名
		if(StringUtils.isNotBlank(checkNull(paramMap.get("name")))){
			sql.append(" and m1.`name` like '%"+paramMap.get("name")+"%' ");
		}
		//微信号
		if(StringUtils.isNotBlank(checkNull(paramMap.get("wechatCode")))){
			sql.append(" and m1.wechatCode like '%" +paramMap.get("wechatCode") +"%' ");
		}
		//旅行社名称
		if(StringUtils.isNotBlank(checkNull(paramMap.get("agentName")))){
			sql.append(" and m1.agentName like '%" +paramMap.get("agentName") +"%' ");
		}
		sql.append(" order by m1.updateDate desc,m1.id asc ");
		String countSql = "SELECT COUNT(*) count FROM ( " + sql + " ) t";
		List<Map<String,Object>> countList = mobileUserDao.findBySql(countSql, Map.class);

		if(!countList.isEmpty()){
			Integer count = Integer.valueOf(countList.get(0).get("count").toString());
			map.put("count", count); // 记录总条数
			map.put("totalPage", Double.valueOf(Math.ceil((double)count / 10)).intValue());// 总页数
		}
		String pageNo = checkNull(paramMap.get("pageNo"));
		String pageSize = paramMap.get("pageSize") == null ? "10":paramMap.get("pageSize").toString();
		if (StringUtils.isNotBlank(pageNo)) {
			map.put("pageNo", paramMap.get("pageNo"));
			map.put("pageSize", pageSize);

			int num = 0;
			if (Integer.parseInt(pageNo) > 0) {
				num = (Integer.parseInt(pageNo)-1) * Integer.parseInt(pageSize);
			}
			sql.append(" limit " + num + ", "+Integer.parseInt(pageSize));
		} else {
			map.put("pageNo", 0);
			sql.append(" limit 0 , 10 ");
		}
		List<Map<String,Object>> mobileUserList = mobileUserDao.findBySql(sql.toString(), Map.class);
		//预匹配T1账号 （只有未关联的微信账号进行与匹配）
		if(!mobileUserList.isEmpty() && Integer.parseInt(type) == 0){
			getT1user(mobileUserList);
		}

		map.put("mobileUsers",mobileUserList);
		map.put("result", true);
		map.put("msg", "请求成功");
		return map;
	}

	/**
	 * 匹配T1账号
	 * @return
	 */
	public void getT1user(List<Map<String, Object>> mobileUserList) {
		    //判断是否匹配有T1账号
		    for(Map<String,Object> user:mobileUserList){
		      //telephone ,name,wechatcode不能为空
		      if(StringUtils.isNotBlank(checkNull(user.get("telephone"))) && StringUtils.isNotBlank(checkNull(user.get("name"))) && StringUtils.isNotBlank(checkNull(user.get("wechatCode")))){
			      StringBuffer sql = new StringBuffer();
			      sql.append("SELECT s.id as contactId,a.id as agentId,u.id as sysUserId, u.loginName ," +
			          "( '"+user.get("telephone").toString()+"' = s.contactMobile ) mobileOrder, " +
			          "('"+user.get("name").toString()+"' = s.contactName) nameOrder, " +
			          "( '"+user.get("wechatCode").toString()+"' = s.wechatCode ) wechatOrder " +
			          "FROM agentinfo a LEFT JOIN sys_user u ON u.agentId = a.id  AND u.mobileUserId IS NULL LEFT JOIN supplier_contacts s ON s.supplierId = u.agentId  " +
			          " WHERE s.delFlag = 0 AND s.type = 0 and u.mobileUserId is null " );
			      StringBuffer tempSql = new StringBuffer();
			      //判断telephone是否为空
			      if(StringUtils.isNotBlank(checkNull(user.get("telephone")))){
			        tempSql.append(" s.contactMobile LIKE '"+user.get("telephone").toString()+"' OR ");
			      }
			      //判断name是否为空
			      if(StringUtils.isNotBlank(checkNull(user.get("name")))){
			        tempSql.append(" s.contactName LIKE '"+user.get("name").toString()+"' OR ");
			      }
			      //判断微信号是否为空
			      if(StringUtils.isNotBlank(checkNull(user.get("wechatCode")))){
			        tempSql.append(" s.wechatCode LIKE '"+user.get("wechatCode")+"' ");
			      }else if(tempSql.length() > 0){
			        String tempSqlStr =  tempSql.toString().trim();
			        tempSql = new StringBuffer(tempSqlStr.substring(0, tempSqlStr.length()-2));
			      }
			      if(tempSql.length() > 0){
			        sql.append(" AND ( "+tempSql+" ) ");
			      }
			      sql.append("ORDER BY mobileOrder desc,nameOrder desc ,wechatOrder desc,u.id desc ");
			      List<Map<String,Object>> users = mobileUserDao.findBySql(sql.toString(), Map.class);
			      user.put("t1Users",users);
			}
		}
	}
	  
	/**
	 * 单个匹配T1账号 param Map <"telephone":231123,"name"：大娃娃,"wechatCode":dwad>
	 */
	public List<Map<String, Object>> singleMatch(Map<String, Object> user) {
	    if(user.isEmpty()){
	      return null;
	    }
	    if(StringUtils.isNotBlank(checkNull(user.get("telephone"))) && StringUtils.isNotBlank(checkNull(user.get("name"))) && StringUtils.isNotBlank(checkNull(user.get("wechatCode")))){
	    StringBuffer sql = new StringBuffer();                                                                                              
	    sql.append("SELECT s.id as contactId,u.agentId,u.id as sysUserId,a.agentName,s.contactMobile,s.contactPhone,s.contactName,s.wechatCode, u.loginName, " +
	        "( '"+user.get("telephone").toString()+"' = s.contactMobile ) mobileOrder, " +
	        "('"+user.get("name").toString()+"' = s.contactName) nameOrder, " +
	        "( '"+user.get("wechatCode").toString()+"' = s.wechatCode ) wechatOrder " +
	        "FROM agentinfo a LEFT JOIN sys_user u ON u.agentId = a.id AND u.mobileUserId IS NULL LEFT JOIN supplier_contacts s ON s.supplierId = u.agentId " +
	        " WHERE s.delFlag = 0 AND s.type = 0  and u.mobileUserId is null " );
	    StringBuffer tempSql = new StringBuffer();
	    //判断telephone是否为空
	    if(StringUtils.isNotBlank(checkNull(user.get("telephone")))){
	      tempSql.append(" s.contactMobile LIKE '"+user.get("telephone").toString()+"' OR ");
	    }
	    //判断name是否为空
	    if(StringUtils.isNotBlank(checkNull(user.get("name")))){
	      tempSql.append(" s.contactName LIKE '"+user.get("name").toString()+"' OR ");
	    }
	    //判断微信号是否为空
	    if(StringUtils.isNotBlank(checkNull(user.get("wechatCode")))){
	      tempSql.append(" s.wechatCode LIKE '"+user.get("wechatCode")+"' ");
	    }else if(tempSql.length() > 0){
	      String tempSqlStr =  tempSql.toString().trim();
	      tempSql = new StringBuffer(tempSqlStr.substring(0, tempSqlStr.length()-2));
	    }
	    if(tempSql.length() > 0){
	      sql.append(" AND ( "+tempSql+" ) ");
	    }
	    sql.append(" ORDER BY mobileOrder desc,nameOrder desc ,wechatOrder desc,u.id desc ");
	    return mobileUserDao.findBySql(sql.toString(), Map.class);
	    }else{
	      return null;
		}

	}
	
	/**
     * 接受的参数如果对象为空，返回空字符串""，否则返回其字符串
     * @param obj
     * @return
     */
    private String checkNull(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    @Override
	public Map<String, Object> confirmMatchingPage(long agentId, long mobileUserId) {
		String mobileUserSql = "SELECT u.id mobileUserId,u.telephone wxMobile,u.name wxName,u.agentName wxAgentName ,u.wechatCode wxChatCode,IFNULL(u.areaCode,'') areaCode,IFNULL(u.phone,'') wxPhone FROM mobile_user u WHERE u.id = " + mobileUserId;
		String t1UserSql = "SELECT USER .id userId, agent.id agentId, contact.contactMobile agentMobile, contact.contactName agentContactName, agent.agentName, contact.wechatCode agentWeixin, IF(contact.contactPhone = '-','',contact.contactPhone) agentPhone, agent.agentBrand FROM agentinfo agent LEFT JOIN supplier_contacts contact ON contact.supplierId = agent.id LEFT JOIN sys_user USER ON agent.id = USER .agentId WHERE contact.delFlag = 0 AND contact.type = 0 AND agent.id = " + agentId;

		// 微信用户和t1用户信息
		List<Map<String, Object>> moList = mobileUserDao.findBySql(mobileUserSql, Map.class);
		List<Map<String, Object>> t1List = mobileUserDao.findBySql(t1UserSql, Map.class);

		Map<String, Object> map = new HashMap<String, Object>();
		if (moList.size() > 0){
			map = moList.get(0);
			// 当区号不为空，电话为空时，区号后面拼一个”-“返回，如022-
			if (StringUtils.isNotBlank(map.get("areaCode").toString()) && StringUtils.isBlank(map.get("wxPhone").toString())){
				map.put("wxPhone", map.get("areaCode").toString()+"-");
			}
			// 当区号、电话都不为空时
			if (StringUtils.isNotBlank(map.get("areaCode").toString()) && StringUtils.isNotBlank(map.get("wxPhone").toString())){
				map.put("wxPhone", map.get("areaCode").toString() + "-" + map.get("wxPhone").toString());
			}
		}
		if (t1List.size() > 0){
			map.putAll(t1List.get(0));
		}
		return map;
	}

    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String, Object> confirmMatch(CorrelationUser correlationUser){
		MobileUser mobileUser = null;
		User user = null;
		Agentinfo agentinfo = null;
		SupplierContacts supplierContacts = null;
		Map<String, Object> result = new HashMap<>();
		if(StringUtils.isNotBlank(correlationUser.getMobileUserId()) && StringUtils.isNotBlank(correlationUser.getUserId()) && StringUtils.isNotBlank(correlationUser.getAgentId())){
			user = userDao.findById(Long.parseLong(correlationUser.getUserId()));
			mobileUser = mobileUserDao.findOne(Long.parseLong(correlationUser.getMobileUserId()));
			agentinfo = agentInfoDao.getAgentInfoById(Long.parseLong(correlationUser.getAgentId()));
			List<SupplierContacts> contactsList = supplierContactsDao.findNormalContactsByAgentInfoId(Long.parseLong(correlationUser.getAgentId()));
			if(contactsList.size() > 0){
				supplierContacts = contactsList.get(0);
			}
		} else {
			return null;
		}
		// 微信用户 delFlag 2：已关联
		mobileUser.setDelFlag("2");
		mobileUser.setT1User(user);

		// t1用户
		user.setMobileUser(mobileUser);
		//账号来源，0 内部 1 微信
//		user.setAccountFrom(0);

		// 渠道
		agentinfo.setAgentName(correlationUser.getAgentName());
		agentinfo.setAgentBrand(correlationUser.getAgentBrand());
		agentinfo.setAgentAddress(correlationUser.getAgentAddress());
		agentinfo.setAgentAddressProvince(correlationUser.getAgentAddressProvince());
		agentinfo.setAgentAddressCity(correlationUser.getAgentAddressCity());
		agentinfo.setAgentAddressStreet(correlationUser.getAgentAddressStreet());

		// 渠道联系人
		supplierContacts.setContactMobile(correlationUser.getAgentContactMobile());
		supplierContacts.setContactName(correlationUser.getAgentContact());
		supplierContacts.setWechatCode(correlationUser.getWxCode());
		if(StringUtils.isNotBlank(correlationUser.getLeftAgentContactTel()) && StringUtils.isNotBlank(correlationUser.getRightAgentContactTel())){
			supplierContacts.setContactPhone(correlationUser.getLeftAgentContactTel() + "-" + correlationUser.getRightAgentContactTel());
		} else {
			supplierContacts.setContactPhone(correlationUser.getLeftAgentContactTel() + correlationUser.getRightAgentContactTel());
		}

		// 账号绑定成功后，数据迁移
		copyWechatToT1(mobileUser.getId(),user.getId());

		result.put("result", "true");
		result.put("msg", "匹配成功");
		return result;
	}

	@Override
	public boolean delMobileUser(Long mobileUserId) {
		boolean flag = false;
		MobileUser user = mobileUserDao.findOne(mobileUserId);
		//只有未绑定账号才能删除
		if(user != null && user.getDelFlag().equals("0")){
			User t1User = user.getT1User();
			user.setDelFlag("1");
			user.setIsRegist("0");
			user.setUnbundleReason(null);
			user.setT1User(null);
			mobileUserDao.updateObj(user);
			mobileUserDao.findBySql("select del_mobile_user_record ("+mobileUserId+")",null);
			if(t1User != null){
				t1User.setMobileUser(null);
				userDao.updateObj(t1User);
			}
			flag = true;
		}
		return flag;
	}
	
	@Override
	public boolean unBoundMobileUser(Long mobileUserId) {
		boolean flag = false;
		MobileUser user = mobileUserDao.findOne(mobileUserId);
		//只有绑定账号才能解绑
		if(user != null && user.getDelFlag().equals("2")){
			User t1User = user.getT1User();
			user.setDelFlag("0");
			user.setUnbundleReason("[{\"type\":\"T2端解除关联\"}]");
			user.setT1User(null);
			mobileUserDao.updateObj(user);
			if(t1User != null){
				t1User.setMobileUser(null);
				userDao.updateObj(t1User);
			}
			flag = true;
		}
		return flag;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean copyWechatToT1(Long mobileUserId, Long t1UserId) {
		boolean flag = false;
		if(mobileUserId != null && t1UserId != null){
			copyProgressData(mobileUserId, t1UserId);
			mobileUserDao.findBySql("select copyWechatToT1 ("+mobileUserId+","+t1UserId+")", null);
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 复制订单跟踪数据
	 * @param mobileUserId
	 * @param t1UserId
	 * @author yakun.bai
	 * @Date 2017-2-7
	 */
	public void copyProgressData(Long mobileUserId, Long t1UserId) {
		
		MobileUser mobileUser = mobileUserDao.getById(mobileUserId);
		// 查询对应用户
		User user = userDao.findOne(mobileUser.getT1User().getId());
		// 询单渠道
		Long agentId = user.getAgentId();
		// 查询需要复制的数据
		/*String sql = "SELECT productId, salerId, max(createTime) createTime FROM mobile_dial_agent_phone WHERE openId = '" + mobileUser.getOpenId() 
				+ "' AND t1UserId IS NULL AND salerId > 0 AND delFlag = 0 GROUP BY productId";*/
		String sql = "SELECT productId, salerId, createTime FROM mobile_dial_agent_phone WHERE openId = '" + mobileUser.getOpenId() 
				+ "' AND t1UserId IS NULL AND salerId > 0 AND delFlag = 0 ";
		List<Map<Object, Object>> list = progressDao.findBySql(sql, Map.class);
		
		// 把所有需要复制的数据都放到list中，实现批量保存
		/*List<OrderProgressTracking> trackingList = Lists.newArrayList();
		for (Map<Object, Object> map : list) {
			Long productId = Long.parseLong(map.get("productId").toString());
			String createTime = map.get("createTime").toString();
			Long salerId = Long.parseLong(map.get("salerId").toString()) ;
			try {
				trackingList.add(saveOrderProgressTracking(productId, user.getId(), agentId, createTime.substring(0, createTime.length() - 2),salerId));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (CollectionUtils.isNotEmpty(trackingList)) {
			progressDao.save(trackingList);
		}*/
		for (Map<Object, Object> map : list) {
			Long productId = Long.parseLong(map.get("productId").toString());
			String createTime = map.get("createTime").toString();
			Long salerId = Long.parseLong(map.get("salerId").toString()) ;
			try {
				saveOrderProgressTracking(productId, user.getId(), agentId, createTime.substring(0, createTime.length() - 2),salerId);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 添加订单跟踪记录
	 * @author yakun.bai
	 * @throws ParseException 
	 * @Date 2017-1-22
	 */
	private OrderProgressTracking saveOrderProgressTracking(Long productId, Long userId, Long agentId, String createTime,Long salerId) throws ParseException {
		// 询单时间
		Date date = DateUtils.dateFormat(createTime);
		// 询单编号（时间戳）
		String askNum = DateUtils.date2String(date, "yyyyMMddHHmmss");
		// 询单团期实体类
		
		TravelActivity activity = activityDao.findOne(productId);
		
		// 订单跟踪团期获取方法：如果产品下的团期都已经过了出团日期，则随机选择一个团期；反之则选择最近的出团日期
		ActivityGroup group = null;
		Set<ActivityGroup> groupSet = activity.getActivityGroups();
		String dateStr = DateUtils.date2String(date, DateUtils.DATE_PATTERN_YYYY_MM_DD) + " 00:00:00";
		Date askDate = DateUtils.dateFormat(dateStr);
		for (ActivityGroup temp : groupSet) {
			if (temp.getIsT1() == 1 && askDate.compareTo(temp.getGroupOpenDate()) <= 0) {
				if (group == null) {
					group = temp;
				} else {
					// 如果团期日期小于临时团期日期并且大于当前时间，则需要替换
					if (temp.getGroupOpenDate().before(group.getGroupOpenDate())) {
						group = temp;
					}
				}
			}
		}
		
		if (group != null) {
			OrderProgressTracking tracking = new OrderProgressTracking();
	    	tracking.setAskNum(askNum);
			tracking.setAskTime(date);
			tracking.setAskUserId(userId);
			tracking.setAskAgentId(agentId);
			tracking.setCompanyId(group.getTravelActivity().getProCompany());
			tracking.setActivityId(productId);
			tracking.setGroupId(group.getId());
			tracking.setUpdateById(userId);
			tracking.setUpdateDate(date);
			tracking.setOrderType(1);
			tracking.setPreOrderId(null);
			//添加被询单销售Id
			tracking.setAskSalerId(salerId);
			//设置询单来源	1:微信端 ， 0：为T1端
			tracking.setT1Flag(1);
	    	
	    	// 如果没有此团期询单记录或如果已经存在询单数据但已经生成订单则添加一条询单记录
			/*List<OrderProgressTracking> trackingList = progressDao.findByAskUserIdAndGroupId(userId, group.getId(), 1,salerId);
			if (CollectionUtils.isEmpty(trackingList)) {
				return tracking;
			} else {
				// 如果是询批发商则需要更新询单时间；如果是预报名则需重新生成订单
				OrderProgressTracking orderProgress = trackingList.get(0);
				orderProgress.setAskTime(date);
				orderProgress.setUpdateById(userId);
				orderProgress.setUpdateDate(date);
				return orderProgress;
			}/**/
			List<OrderProgressTracking> trackingList = progressDao.findByAskUserIdAndProductIdAndSalerIdAndAskTime(userId, productId, 1, salerId,1);
			if (CollectionUtils.isEmpty(trackingList)) {
				progressDao.saveObj(tracking);
				return null;
			}else{
				return null;
				
			}
		} else {
			return null;
		}
	}
	
	@Override
	public MobileUser getEntity(Long mobileUserId) {
		return mobileUserDao.getById(mobileUserId);
	}
	
	
}
