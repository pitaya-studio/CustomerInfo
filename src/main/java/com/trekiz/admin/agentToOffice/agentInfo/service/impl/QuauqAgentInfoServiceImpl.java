package com.trekiz.admin.agentToOffice.agentInfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.PricingStrategy.repository.PriceStrategyDao;
import com.trekiz.admin.agentToOffice.agentInfo.dao.AgentInfoDao;
import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.entity.AgentInfoContacts;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
@Service
@Transactional(readOnly = true)
public class QuauqAgentInfoServiceImpl extends BaseService implements QuauqAgentInfoService{

	@Autowired
	private AgentInfoDao agentInfoDao;
	@Autowired
	private AgentinfoDao agentinfoDao;
	@Autowired
	private PriceStrategyDao priceStrategyDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private OfficeDao officeDao;
	
	
	/**
	 * 根据字典类型和批发商id查询
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<Sysdefinedict> getDefineDictByCompanyIdAndType(Long companyId,
			String type) {
		List<Sysdefinedict> list = agentInfoDao.getDefineDictByCompanyIdAndType(companyId, type);
		return list;
	}
	
	/**
	 * 保存
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void saveSysDefineDict(Sysdefinedict sysDefineDict) {
		super.setOptInfo(sysDefineDict, BaseService.OPERATION_ADD);
		agentInfoDao.saveSysDefineDict(sysDefineDict);
		
	}

	@Override
	public void updateSysDefineDict(Sysdefinedict sysDefineDict) {
		
		
	}
	
	/**
	 * 删除
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void deleteSysDefineDict(Long sysDefineDictId) {
		agentInfoDao.deleteSysDefineDict(sysDefineDictId);
		
	}
	/**
	 * 验证渠道等级
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public boolean getDefineDictByLabel(Integer companyId,
			String label, String type) {
		List<Sysdefinedict> list = agentInfoDao.getDefineDictByLabel(companyId, label, type);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean checkTypeOrGrade(String id,String label) {
		List<Map<String,Object>> list = priceStrategyDao.getGradeAndType();
		for(Map<String,Object> agentPriceStrategy:list){
			if(label.equals("agent_grade")){
				if(agentPriceStrategy.get("agentLevelIds").toString().indexOf(id)>-1){
					return true;
				}
			}
			if(label.equals("agent_type")){
				if(agentPriceStrategy.get("agentTypeIds").toString().indexOf(id)>-1){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Map<String, Object> handleUserInfo(User oldUser, Agentinfo agentInfo,
			Long quauqCompanyId, AgentInfoContacts agentInfoContacts) {
		
		Map<String, Object> userInfoParam = new HashMap<>();
		userInfoParam.put("id", oldUser.getId());
		userInfoParam.put("oldLoginName", oldUser.getLoginName());
		userInfoParam.put("newLoginName", agentInfoContacts.getQuauqAgentUserLoginName());
		userInfoParam.put("oldPassword", oldUser.getPassword());
		userInfoParam.put("newPassword", agentInfoContacts.getNewPassword());  // TODO
		userInfoParam.put("name", agentInfo.getAgentName());
		userInfoParam.put("differenceRights", agentInfoContacts.getDifferenceRights());//下单权限
		/** 临时策略（待删除） */
		userInfoParam.put("lingxianwangshuai", agentInfoContacts.getLingxianwangshuai());
		Office office = officeDao.findById(quauqCompanyId);
		userInfoParam.put("company", office);
		userInfoParam.put("agent", agentInfo);
		// 微信用户id
		userInfoParam.put("mobileUserId", agentInfoContacts.getMobileUserId());
		
		return userInfoParam;
	}
	
	
	/**
	 * @Description ququa渠道统计
	 * @author yakun.bai
	 * @Date 2016-5-3
	 */
	public Page<Map<Object, Object>> quauqAgentStatistics(Page<Map<Object, Object>> page, Map<String, String> mapRequest) {
		
		// 获取查询条件
		String where = getWhereSql(mapRequest);
		
		// 获取查询语句
		String sql = getStatisticsSql(where);
		
		// 数据查询
		Page<Map<Object, Object>> pageMap = priceStrategyDao.findPageBySql(page, sql, Map.class);
		
		return pageMap;
	}
	
	/**
	 * @Description ququa渠道订单导出
	 * @author yakun.bai
	 * @Date 2016-5-5
	 */
	public List<Map<Object, Object>> downloadAllOrder(Map<String, String> mapRequest) {
		
		// 获取查询条件
		String where = getWhereSql(mapRequest);
		
		// 获取查询语句
		String sql = getStatisticsSql(where);
		
		// 数据查询
		List<Map<Object, Object>> list = priceStrategyDao.findBySql(sql, Map.class);
		
		return list;
	}
	
    /**
     * @Description 获取查询条件
     * @author yakun.bai
     * @Date 2016-5-4
     */
    public String getWhereSql(Map<String,String> map) {
    	
        StringBuffer sqlWhere = new StringBuffer("");
        
        // 订单类型
        sqlWhere.append(" and pro.orderStatus = " + Context.ORDER_STATUS_LOOSE);
        
        // 渠道
        String agentId = map.get("agentId");
        if (StringUtils.isNotBlank(agentId)){
        	sqlWhere.append(" and pro.orderCompany = " + agentId + " ");
        }
        
        // 订单状态
        String orderStatus = map.get("orderStatus");
        if (StringUtils.isNotBlank(orderStatus)){
        	sqlWhere.append(" and pro.payStatus = " + orderStatus + " ");
        }
        
        // 订单创建时间
        String orderTimeBegin = map.get("orderTimeBegin");
        String orderTimeEnd = map.get("orderTimeEnd");
        if (StringUtils.isNotBlank(orderTimeBegin)) {
            sqlWhere.append( " and pro.orderTime >= '" + orderTimeBegin + " 00:00:00" + "'");
        }
        if (StringUtils.isNotBlank(orderTimeEnd)) {
            sqlWhere.append( " and pro.orderTime <= '" + orderTimeEnd + " 23:59:59" + "'");
        }
        
        // 订单号
        String orderNum = map.get("orderNum");
        if (StringUtils.isNotBlank(orderNum)){
        	sqlWhere.append(" and pro.orderNum like '%" + orderNum + "%' ");
        }
        
        // 团号
        String groupCode = map.get("groupCode");
        if (StringUtils.isNotBlank(groupCode)){
        	sqlWhere.append(" and agp.groupCode like '%" + groupCode + "%' ");
        }
        
        // 批发商
        String companyId = map.get("companyId");
        if (StringUtils.isNotBlank(companyId)) {
        	sqlWhere.append(" and office.id = " + companyId + " ");
        }
        
        return sqlWhere.toString();
    }
	
    /**
     * @Description 获取统计查询sql
     * @author yakun.bai
     * @Date 2016-5-4
     */
    private String getStatisticsSql(String where) {
    	StringBuilder sql = new StringBuilder("");
    	sql.append("SELECT pro.id orderId, pro.delFlag delFlag, agent.agentName agentName, pro.createDate createDate, pro.orderNum orderNum, ")
					.append("pro.orderPersonNum orderPersonNum, pro.payStatus orderStatus, office.name as officeName, totalOuter.moneyStr totalMoney, ")
					.append("quauqTotalOuter.moneyStr quauqTotalMoney, quauqServiceChargeOuter.moneyStr quauqServiceCharge, agp.groupCode groupCode ")
				.append("FROM agentinfo agent, ")
					.append("activitygroup agp, ")
					.append("sys_office office, ")
					.append("productorder pro ")
				
				//订单应收金额多币种查询
				.append(" LEFT JOIN ( ")
					.append("SELECT mao.serialNum ")
						.append(",GROUP_CONCAT(CONCAT ( ")
							.append("c.currency_mark, ' ', ")
							.append("mao.amount ")
							.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
					.append("FROM money_amount mao ")
					.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
					.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_YSH + " ")
			    			.append("AND mao.orderType = " + Context.ORDER_STATUS_LOOSE + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
					.append("GROUP BY mao.serialNum ")
					.append(") totalOuter ON totalOuter.serialNum = pro.total_money ")
					
				//订单实收金额多币种查询
				.append("LEFT JOIN ( ")
					.append("SELECT mao.serialNum ")
						.append(",GROUP_CONCAT(CONCAT ( ")
							.append("c.currency_mark, ' ', ")
							.append("mao.amount ")
							.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
					.append("FROM money_amount mao ")
					.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
					.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_YS + " ")
			    			.append("AND mao.orderType = " + Context.ORDER_STATUS_LOOSE + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
					.append("GROUP BY mao.serialNum ")
					.append(") quauqTotalOuter ON quauqTotalOuter.serialNum = pro.quauq_total_money ")
					
				//订单已达帐金额多币种查询
				.append("LEFT JOIN ( ")
					.append("SELECT mao.serialNum ")
						.append(",GROUP_CONCAT(CONCAT ( ")
							.append("c.currency_mark, ' ', ")
							.append("mao.amount ")
							.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
					.append("FROM money_amount mao ")
					.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
					.append("WHERE mao.moneyType = " + Context.MONEY_TYPE_DZ + " ")
			    			.append("AND mao.orderType = " + Context.ORDER_STATUS_LOOSE + " ")
			    			.append("AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER + " ")
					.append("GROUP BY mao.serialNum ")
					.append(") quauqServiceChargeOuter ON quauqServiceChargeOuter.serialNum = pro.quauq_service_charge ")
				
					//查询包含正常生成订单和待生成订单、未生成订单
				.append("WHERE (pro.delFlag = " + ProductOrderCommon.DEL_FLAG_NORMAL)
				.append(" OR pro.delFlag = " + ProductOrderCommon.DEL_FLAG_TO_GENERATE)
				.append(" OR pro.delFlag = ").append(ProductOrderCommon.DEL_FLAG_NOT_GENERATE).append(") ")
					.append("AND pro.orderCompany = agent.id ")
					.append("AND pro.productGroupId = agp.id ")
					.append("AND agent.supplyId = office.id ")
					.append("AND agent.is_quauq_agent = 1 ")
					.append(where);
    	return sql.toString();
    }
	@Override
	public List<Agentinfo> getAllQuauqAgentinfos() {
		// 获取quauq
		List<Agentinfo> quauqAgentinfos = new ArrayList<>();
		Office quauqOffice = userDao.getOfficeByCompanyUuid(Context.SUPPLIER_UUID_QUAUQ);
		if (quauqOffice != null) {
			quauqAgentinfos = agentinfoDao.findAgentByCompany(quauqOffice.getId());
			Iterator<Agentinfo> it = quauqAgentinfos.iterator();
			while(it.hasNext()) {
				String agentName = it.next().getAgentName();
				if (agentName.equals("思锐创途") || agentName.equals("测试专用") 
						|| agentName.equals("思锐创途销售专用")) {
					it.remove();
				}
			}
		}
		return quauqAgentinfos;
	}

	@Override
	public List<User> getAllQuauqAgentLoginUser() {
		List<Agentinfo> allQuauqAgent = getAllQuauqAgentinfos();
		List<User> allLoginUsers = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(allQuauqAgent)) {
			for (Agentinfo agentinfo : allQuauqAgent) {
				List<User> agentUsers = userDao.findByQuauqAgentId(agentinfo.getId());
				if (CollectionUtils.isNotEmpty(agentUsers)) {
					allLoginUsers.add(agentUsers.get(0));
				}
			}
		}
		return allLoginUsers;
	}

	@Override
	public List<User> getQuauqAgentLoginUsers(List<String> agentIdList) {
		List<User> allLoginUsers = new ArrayList<>();
		List<Agentinfo> quauqAgents = agentinfoDao.findAgentinfosByIds(agentIdList);
		if (CollectionUtils.isNotEmpty(quauqAgents)) {
			for (Agentinfo agentinfo : quauqAgents) {
				List<User> agentUsers = userDao.findByQuauqAgentId(agentinfo.getId());
				if (CollectionUtils.isNotEmpty(agentUsers)) {
					allLoginUsers.add(agentUsers.get(0));
				}
			}
		}
		return allLoginUsers;
	}

	@Override
	public Page<Map<Object, Object>> findQuauqAgentList(Page<Map<Object, Object>> page, Map<String, Object> paramMap) {
		String queryStr = getQuerySql(paramMap,1);
		Page<Map<Object, Object>> tempPage =  agentinfoDao.findPageBySql(page, queryStr, Map.class);
		return tempPage;
	}
	
	/**
	 * 获取查询渠道账号列表的sql
	 * @param paramMap
	 * @param queryType 查询类型 1 为账号列表查询 2 为所有账号查询
	 * @return
	 */
	private String getQuerySql(Map<String, Object> paramMap,int queryType){
		Office quauqOffice = userDao.getOfficeByCompanyUuid(Context.SUPPLIER_UUID_QUAUQ);
		if (quauqOffice == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		if(queryType == 1){
			sb.append("SELECT tt.`name`, tt.loginName, tt.userId, tt.contactName, tt.contactMobile, tt.contactId, tt.agentId, tt.agentName," +
					"( CASE tt.accountFrom WHEN 0 THEN '内部' WHEN 1 THEN '微信' ELSE '' END ) AS accountFrom,	tt.isBound,  ")
			  .append(" tt.isQuauqAgent, tt.enableQuauqAgent, tt.delFlag, tt.agentType, tt.agentParent, tt.supplyId , tt.agentBrand ,tt.abbreviation ");
		}else if(queryType == 2){
			sb.append("SELECT tt.userId as t1UserId, tt.contactName, tt.contactMobile,  tt.agentId, tt.agentName ,tt.contactPhone, tt.wechatCode,tt.contactId ");
		}else{
			return null;
		}
		  sb.append(" FROM ( SELECT u.`name`, u.loginName, u.id AS userId,u.mobileUserId, c.contactName,c.wechatCode, c.contactMobile,c.contactPhone, c.id AS contactId, a.id AS agentId, ");
		  sb.append(" u.accountFrom, (CASE  ISNULL(u.mobileUserId) when 0 then '已关联' else '未关联' END)  as isBound, ");
		  sb.append(" a.agentName,a.abbreviation, a.`is_quauq_agent` AS isQuauqAgent, a.enable_quauq_agent AS enableQuauqAgent, a.delFlag, a.agentBrand , ");
		  sb.append(" ( SELECT sct.`name` FROM sys_customer_type sct WHERE sct.del_flag = '0' AND sct.`value` = a.agent_type ) AS agentType, ");
		  sb.append(" ( CASE a.agent_parent WHEN '-1' THEN '-1' ELSE ( SELECT b.agentName FROM agentinfo b WHERE b.id = a.agent_parent AND b.delFlag = 0 ) END ) AS agentParent, ");
		  sb.append(" a.supplyId supplyId ");
		//账号是否关联微信
		  if(queryType == 2  && paramMap.get("mobileUserId") != null && StringUtils.isNoneBlank(paramMap.get("mobileUserId").toString())){
			  sb.append("from (select * from sys_user where mobileUserId is null) u LEFT JOIN ");
		  }else{
			  sb.append("from (select * from sys_user ) u LEFT JOIN ");
		  }
		  sb.append(" (select * from agentinfo b WHERE b.is_quauq_agent = 1 AND b.delFlag = 0 AND b.supplyId ="+quauqOffice.getId()+") a ON a.id = u.agentId ");
		  sb.append(" LEFT JOIN ( SELECT sc.supplierId, sc.contactName, sc.contactMobile, sc.id, sc.contactPhone,sc.wechatCode FROM supplier_contacts sc ");
		  sb.append(" WHERE sc.type = 0 AND sc.delFlag = 0 GROUP BY sc.supplierId ) c ON c.supplierId = a.id WHERE 1=1 ");
		  
		// 类型
		if (paramMap.get("agentType") != null && StringUtils.isNoneBlank(paramMap.get("agentType").toString()) 
				&& !paramMap.get("agentType").equals("-1")) {
			sb.append(" and a.agent_type = ").append(paramMap.get("agentType").toString());
		}
		  
		sb.append(" ) tt ");
//		sb.append(" WHERE tt.isQuauqAgent = 1 AND tt.delFlag = 0 AND tt.supplyId = ").append(quauqOffice.getId());
		  sb.append(" where tt.agentId is not null ");
		
		// quaqu渠道名称
		if (paramMap.get("agentName") != null && StringUtils.isNotBlank(paramMap.get("agentName").toString())){
			sb.append(" and tt.agentName like '%").append(paramMap.get("agentName").toString()).append("%' ");
		}
		// 登陆名
		if (paramMap.get("loginName") != null && StringUtils.isNotBlank(paramMap.get("loginName").toString())){
			sb.append(" and tt.loginName like '%").append(paramMap.get("loginName").toString()).append("%' ");
		}
		// 联系人姓名
		if (paramMap.get("contactName") != null && StringUtils.isNotBlank(paramMap.get("contactName").toString())){
			sb.append(" and tt.contactName like '%").append(paramMap.get("contactName").toString()).append("%' ");
		}
		// 上级关系
		if (paramMap.get("agentParent") != null && StringUtils.isNoneBlank(paramMap.get("agentParent").toString())
				&& !paramMap.get("agentParent").equals("-1")) {
			sb.append(" and tt.agentParent like '%").append(paramMap.get("agentParent").toString()).append("%' ");
		}
		//关联状态
		if(paramMap.get("isBound") != null && StringUtils.isNotBlank(paramMap.get("isBound").toString())){
			sb.append(" AND tt.isBound = '"+paramMap.get("isBound").toString()+"'");
		}
		//渠道来源
		if(paramMap.get("accountFrom") != null && StringUtils.isNotBlank(paramMap.get("accountFrom").toString())){
			int accountFrom = Integer.parseInt((paramMap.get("accountFrom").toString()));
			sb.append(" and tt.accountFrom = "+accountFrom);
			
		}
		
		//账号是否是启用状态
		if (paramMap.get("enableQuauqAgent") != null && StringUtils.isNoneBlank(paramMap.get("enableQuauqAgent").toString())) {
			sb.append(" and tt.enableQuauqAgent = "+paramMap.get("enableQuauqAgent"));
		} 
		
		sb.append(" ORDER BY tt.agentId DESC ");
		return sb.toString();
	}

	@Override
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void deleteQuauqAgent(long agentid) {
		userDao.deleteByAgentAndQuauqflag(agentid, Context.QUAUQ_AGENT_LOGIN_USER_YES);  // 删除渠道登录账号
		agentinfoDao.delAgentinfo(agentid);  // 删除渠道
	}
	
	@Override
	public boolean isExist(String string) {
		String[] strArray = string.replace(",", " ").trim().split(" ");
		for(String str:strArray){
			if(agentInfoDao.getDefineDictByiD(new Long(str)) == null){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Map<String, Object>> getAgentTypeList() {
		return agentInfoDao.getAgentType4Select();
	}

	@Override
	public List<Map<String, String>> getAgentParentList(Long agentId) {
		return agentInfoDao.getAgentParent4Select(agentId);
	}

	@Override
	public List<Map<Object, Object>> findAgentNotBoundList(Map<String, Object> paramMap) {
		String querySql = getQuerySql(paramMap,2);
		return agentInfoDao.findBySql(querySql, Map.class, null);
	}
	
	
}
