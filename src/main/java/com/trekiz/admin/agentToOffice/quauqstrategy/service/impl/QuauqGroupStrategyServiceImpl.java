package com.trekiz.admin.agentToOffice.quauqstrategy.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.agentToOffice.T2.entity.OfficeRate;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.service.QuauqServiceFeeService;
import com.trekiz.admin.agentToOffice.T2.utils.JudgeStringType;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.agentToOffice.quauqstrategy.dao.QuauqGroupStrategyDao;
import com.trekiz.admin.agentToOffice.quauqstrategy.entity.QuauqGroupStrategy;
import com.trekiz.admin.agentToOffice.quauqstrategy.entity.QueryQuauqStrategy;
import com.trekiz.admin.agentToOffice.quauqstrategy.service.QuauqGroupStrategyService;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.log.entity.QuauqBusinessLog;
import com.trekiz.admin.modules.log.service.QuauqBusinessLogService;

@Service
@Transactional(readOnly = true)
public class QuauqGroupStrategyServiceImpl extends BaseService implements QuauqGroupStrategyService{
	@Autowired
	private QuauqGroupStrategyDao quauqGroupStrategyDao;
	
	@Autowired
	private QuauqBusinessLogService quauqBusinessLogService;
	
	@Autowired
	private QuauqServiceFeeService quauqServiceFeeService;
	
	/**
	 * 查询未设置的策略弹窗列表
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/20
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<Map<String, Object>> getAllQuauqAgentStrate(
			Page<Map<String, Object>> page, QueryQuauqStrategy quauqStrategy) {
		if(StringUtils.isEmpty(quauqStrategy.getGroupId())){
			return null;
		}
		if(StringUtils.isEmpty(quauqStrategy.getProductType())){
			return null;
		}
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT a.id AS agentId,a.agentName,a.agent_type AS agentType,sor.*,sct.name as customName ");
		sbf.append("FROM ");
		sbf.append("agentinfo a LEFT JOIN (SELECT sor.* FROM sys_office_rate sor WHERE  sor.company_uuid=? AND sor.del_flag=0 ) sor "); 
		sbf.append("ON sor.agent_type=a.agent_type " );
		sbf.append("LEFT JOIN sys_customer_type sct on sct.value=a.agent_type " );
		sbf.append(" WHERE a.is_quauq_agent=1 AND a.agent_parent=-1 AND a.delFlag=0 AND a.id NOT IN( ");
		sbf.append("SELECT qgs.agent_id FROM ");
		sbf.append("quauq_group_strategy qgs LEFT JOIN agentinfo a ON qgs.agent_id=a.id ");
		sbf.append("WHERE qgs.activity_id=? AND qgs.product_type=? AND qgs.delFlag=0 AND a.agent_parent=-1) ");
		if(StringUtils.isNotEmpty(quauqStrategy.getAgentName())){
			sbf.append("AND a.agentName LIKE '%"+quauqStrategy.getAgentName()+"%' ");
		}
		if(StringUtils.isNotEmpty(quauqStrategy.getChannelType())){
			sbf.append("AND a.agent_type="+quauqStrategy.getChannelType()+" ");
		}
		page=quauqGroupStrategyDao.findPageBySql(page, sbf.toString(), Map.class, quauqStrategy.getCompanyUuid(),
						Integer.parseInt(quauqStrategy.getGroupId()),Integer.parseInt(quauqStrategy.getProductType()));
		return page;
	}
	
	
	/**
	 * 获取所有的T1渠道商 不分页
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<Object, Object>> getAllT1Agent() {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT a.id AS agentId,a.agentName,a.agent_type AS agentType,sct.name as customName ");
		sbf.append("FROM agentinfo a LEFT JOIN sys_customer_type sct on sct.value=a.agent_type ");
		sbf.append(" WHERE a.is_quauq_agent=1 AND a.delFlag=0 ");
		return quauqGroupStrategyDao.findBySql(sbf.toString(), Map.class);
	}



	@SuppressWarnings("unchecked")
	@Override
	public Page<Map<String, Object>> getAllQuauqAgent(
			Page<Map<String, Object>> page, QueryQuauqStrategy quauqStrategy) {
		if(StringUtils.isEmpty(quauqStrategy.getProductType())){
			return null;
		}
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT a.id AS agentId,a.agentName,a.agent_type AS agentType,sct.name as customName ");
		sbf.append("FROM agentinfo a LEFT JOIN sys_customer_type sct on sct.value=a.agent_type ");
		sbf.append(" WHERE a.is_quauq_agent=1 AND a.agent_parent=-1 AND a.delFlag=0 ");
		if(StringUtils.isNotEmpty(quauqStrategy.getAgentName())){
			sbf.append("AND a.agentName LIKE '%"+quauqStrategy.getAgentName()+"%' ");
		}
		if(StringUtils.isNotEmpty(quauqStrategy.getChannelType())){
			sbf.append("AND a.agent_type="+quauqStrategy.getChannelType()+" ");
		}
		page=quauqGroupStrategyDao.findPageBySql(page, sbf.toString(), Map.class,null);
		return page;
	}




	/**
	 * 查询已设置的策略弹窗列表
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/20
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<Map<String, Object>> getQuauqAgentStrate(
			Page<Map<String, Object>> page, QueryQuauqStrategy quauqStrategy) {
		if(StringUtils.isEmpty(quauqStrategy.getGroupId())){
			return null;
		}
		if(StringUtils.isEmpty(quauqStrategy.getProductType())){
			return null;
		}
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT qgs.id AS groupRateId,qgs.*,a.agentName,a.agent_type as agentType,a.id AS agentId,sct.name as customName FROM ");
		sbf.append("quauq_group_strategy qgs LEFT JOIN agentinfo a ON qgs.agent_id=a.id ");
		sbf.append("LEFT JOIN sys_customer_type sct ON sct.value=a.agent_type ");
		sbf.append("WHERE qgs.activity_id=? AND qgs.product_type=? AND qgs.delFlag=0 AND a.agent_parent=-1 ");
		if(StringUtils.isNotEmpty(quauqStrategy.getAgentName())){
			sbf.append("AND a.agentName LIKE '%"+quauqStrategy.getAgentName()+"%' ");
		}
		if(StringUtils.isNotEmpty(quauqStrategy.getChannelType())){
			sbf.append("AND a.agent_type="+quauqStrategy.getChannelType()+" ");
		}
		page=quauqGroupStrategyDao.findPageBySql(page, sbf.toString(), Map.class, 
						Integer.parseInt(quauqStrategy.getGroupId()),Integer.parseInt(quauqStrategy.getProductType()));
		return page;
	}
	
	/**
	 * 保存和批量保存费率
	 * @param array
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/10
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void batchSaveStrategy(JSONArray array) {
		for(int i=0;array!=null && i<array.size();i++){
			JSONObject jobj = (JSONObject)array.get(i);
			QuauqGroupStrategy quauqGroupStrategy=null ;
			QuauqGroupStrategy quauqGroupStrategy1=null;
			if(jobj.getInteger("id")==null){
				quauqGroupStrategy=new QuauqGroupStrategy();
				quauqGroupStrategy1=new QuauqGroupStrategy();
			}else{
				quauqGroupStrategy = quauqGroupStrategyDao.getGroupRateById(jobj.getInteger("id"));
				quauqGroupStrategy1=new QuauqGroupStrategy();
				quauqGroupStrategy1.setId(quauqGroupStrategy.getId());
				quauqGroupStrategy1.setUuid(quauqGroupStrategy.getUuid());
			}
			if(jobj.getString("companyUuid")!=null){
				quauqGroupStrategy.setCompanyUuid(jobj.getString("companyUuid"));
				quauqGroupStrategy1.setCompanyUuid(jobj.getString("companyUuid"));
			}
			
			if(jobj.getInteger("groupId")!=null){
				quauqGroupStrategy.setActivityId(jobj.getInteger("groupId"));
				quauqGroupStrategy1.setActivityId(jobj.getInteger("groupId"));
			}
			if(jobj.getInteger("productType")!=null){
				quauqGroupStrategy.setProductType(jobj.getInteger("productType"));
				quauqGroupStrategy1.setProductType(jobj.getInteger("productType"));
			}
			if(jobj.getInteger("agentId")!=null){
				quauqGroupStrategy.setAgentId(jobj.getInteger("agentId"));
				quauqGroupStrategy1.setAgentId(jobj.getInteger("agentId"));
			}
			if(jobj.getInteger("quauqRateType")!=null){
				quauqGroupStrategy.setQuauqRateType(jobj.getInteger("quauqRateType"));
				quauqGroupStrategy1.setQuauqRateType(jobj.getInteger("oldQuauqRateType"));
			}
			if(jobj.getDouble("quauqRate")!=null){
				if(jobj.getInteger("quauqRateType")==0){
					quauqGroupStrategy.setQuauqRate(jobj.getDouble("quauqRate")/100);
				}else{
					quauqGroupStrategy.setQuauqRate(jobj.getDouble("quauqRate"));
				}
				quauqGroupStrategy1.setQuauqRate(jobj.getDouble("oldQuauqRate"));
			}else{
				quauqGroupStrategy.setQuauqRate(0.00);
			}
			if(jobj.getInteger("quauqOtherRateType")!=null){
				quauqGroupStrategy.setQuauqOtherRateType(jobj.getInteger("quauqOtherRateType"));
				quauqGroupStrategy1.setQuauqOtherRateType(jobj.getInteger("oldQuauqOtherRateType"));
			}
			if(jobj.getDouble("quauqOtherRate")!=null){
				if(jobj.getInteger("quauqOtherRateType")==0){
					quauqGroupStrategy.setQuauqOtherRate(jobj.getDouble("quauqOtherRate")/100);
				}else{
					quauqGroupStrategy.setQuauqOtherRate(jobj.getDouble("quauqOtherRate"));
				}
				quauqGroupStrategy1.setQuauqOtherRate(jobj.getDouble("oldQuauqOtherRate"));
			}else{
				quauqGroupStrategy.setQuauqOtherRate(0.00);
			}
			if(jobj.getInteger("agentRateType")!=null){
				quauqGroupStrategy.setAgentRateType(jobj.getInteger("agentRateType"));
				quauqGroupStrategy1.setAgentRateType(jobj.getInteger("oldAgentRateType"));
			}
			if(jobj.getDouble("agentRate")!=null){
				if(jobj.getInteger("agentRateType")==0){
					quauqGroupStrategy.setAgentRate(jobj.getDouble("agentRate")/100);
				}else{
					quauqGroupStrategy.setAgentRate(jobj.getDouble("agentRate"));
				}
				quauqGroupStrategy1.setAgentRate(jobj.getDouble("oldAgentRate"));
			}else{
				quauqGroupStrategy.setAgentRate(0.00);
			}
			if(jobj.getInteger("agentOtherRateType")!=null){
				quauqGroupStrategy.setAgentOtherRateType(jobj.getInteger("agentOtherRateType"));
				quauqGroupStrategy1.setAgentOtherRateType(jobj.getInteger("oldAgentOtherRateType"));
			}
			if(jobj.getDouble("agentOtherRate")!=null){
				if(jobj.getInteger("agentOtherRateType")==0){
					quauqGroupStrategy.setAgentOtherRate(jobj.getDouble("agentOtherRate")/100);
				}else{
					quauqGroupStrategy.setAgentOtherRate(jobj.getDouble("agentOtherRate"));
				}
				quauqGroupStrategy1.setAgentOtherRate(jobj.getDouble("oldAgentOtherRate"));
			}else{
				quauqGroupStrategy.setAgentOtherRate(0.00);
			}
			if(jobj.getInteger("id")==null){
				String uuid = UuidUtils.generUuid();
				quauqGroupStrategy.setUuid(uuid);
				super.setOptInfo(quauqGroupStrategy, BaseService.OPERATION_ADD);
				quauqGroupStrategyDao.getSession().save(quauqGroupStrategy);
				quauqBusinessLogService.log(quauqGroupStrategy1.toString()+"变为"+quauqGroupStrategy.toString(), QuauqBusinessLog.ADD_GROUP_STRATEGY);
			}else{
				super.setOptInfo(quauqGroupStrategy, BaseService.OPERATION_UPDATE);
				quauqGroupStrategyDao.updateObj(quauqGroupStrategy);
				quauqBusinessLogService.log(quauqGroupStrategy1.toString()+"变为"+quauqGroupStrategy.toString(), QuauqBusinessLog.UPDATE_GROUP_STRATEGY);
			}
		}
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public void batchSaveGroupRate(String groupIds, JSONArray array,String productType,String companyUuid) {
		String[] groupids = groupIds.replace(",", " ").trim().split(" ");
		for(int i = 0; i < groupids.length; ++i){
			String groupId = groupids[i];
			if(!JudgeStringType.isPositiveInteger(groupId)){
				throw new RuntimeException("团期ID数据格式错误");
			}
			for(int j=0;array!=null && j<array.size();j++){
				JSONObject jobj = (JSONObject)array.get(j);
				String oldData = "";
				QuauqGroupStrategy quauqGroupStrategy=null ;
				boolean isAdd = true;
				
				//判断是否存在非默认费率
				String agentId = jobj.getString("agentId");
				if(!JudgeStringType.isPositiveInteger(agentId)){
					throw new RuntimeException("批发商ID数据格式错误");
				}
				String agentType =  jobj.getString("agentType");
				if(!JudgeStringType.isPlusMinusInteger(agentType)){
					throw new RuntimeException("批发商类型数据格式错误");
				}
				if(!StringUtils.isBlank(agentId)){
					quauqGroupStrategy = quauqGroupStrategyDao.getGroupRateByCG(groupId,agentId);
					if(quauqGroupStrategy == null){
						//获取批发商默认汇率
						if(!StringUtils.isBlank(companyUuid)){
							Rate rate = quauqServiceFeeService.getCompanyRate(companyUuid, new Integer(agentType));
							oldData = rate.getRateDesc(companyUuid, groupId, productType, agentId);
						}
						quauqGroupStrategy = new QuauqGroupStrategy();
						quauqGroupStrategy.setCompanyUuid(companyUuid);
						quauqGroupStrategy.setActivityId(new Integer(groupId));
						quauqGroupStrategy.setProductType(new Integer(productType));
						quauqGroupStrategy.setAgentId(new Integer(agentId));
					}else{
						isAdd = false;
						oldData = quauqGroupStrategy.toString();
					}
				}
				
				if(jobj.getInteger("quauqRateType")!=null){
					quauqGroupStrategy.setQuauqRateType(jobj.getInteger("quauqRateType"));
				}
				if(jobj.getDouble("quauqRate")!=null){
					if(jobj.getInteger("quauqRateType")==0){
						quauqGroupStrategy.setQuauqRate(jobj.getDouble("quauqRate")/100);
					}else{
						quauqGroupStrategy.setQuauqRate(jobj.getDouble("quauqRate"));
					}
				}else{
					quauqGroupStrategy.setQuauqRate(0.00);
				}
				if(jobj.getInteger("quauqOtherRateType")!=null){
					quauqGroupStrategy.setQuauqOtherRateType(jobj.getInteger("quauqOtherRateType"));
				}
				if(jobj.getDouble("quauqOtherRate")!=null){
					if(jobj.getInteger("quauqOtherRateType")==0){
						quauqGroupStrategy.setQuauqOtherRate(jobj.getDouble("quauqOtherRate")/100);
					}else{
						quauqGroupStrategy.setQuauqOtherRate(jobj.getDouble("quauqOtherRate"));
					}
				}else{
					quauqGroupStrategy.setQuauqOtherRate(0.00);
				}
				if(jobj.getInteger("agentRateType")!=null){
					quauqGroupStrategy.setAgentRateType(jobj.getInteger("agentRateType"));
				}
				if(jobj.getDouble("agentRate")!=null){
					if(jobj.getInteger("agentRateType")==0){
						quauqGroupStrategy.setAgentRate(jobj.getDouble("agentRate")/100);
					}else{
						quauqGroupStrategy.setAgentRate(jobj.getDouble("agentRate"));
					}
				}else{
					quauqGroupStrategy.setAgentRate(0.00);
				}
				if(jobj.getInteger("agentOtherRateType")!=null){
					quauqGroupStrategy.setAgentOtherRateType(jobj.getInteger("agentOtherRateType"));
				}
				if(jobj.getDouble("agentOtherRate")!=null){
					if(jobj.getInteger("agentOtherRateType")==0){
						quauqGroupStrategy.setAgentOtherRate(jobj.getDouble("agentOtherRate")/100);
					}else{
						quauqGroupStrategy.setAgentOtherRate(jobj.getDouble("agentOtherRate"));
					}
				}else{
					quauqGroupStrategy.setAgentOtherRate(0.00);
				}
				
				if(isAdd){
					String uuid = UuidUtils.generUuid();
					quauqGroupStrategy.setUuid(uuid);
					super.setOptInfo(quauqGroupStrategy, BaseService.OPERATION_ADD);
					quauqGroupStrategyDao.getSession().save(quauqGroupStrategy);
					quauqBusinessLogService.log(oldData+"变为"+quauqGroupStrategy.toString(), QuauqBusinessLog.ADD_GROUP_STRATEGY);
				}else{
					super.setOptInfo(quauqGroupStrategy, BaseService.OPERATION_UPDATE);
					quauqGroupStrategyDao.updateObj(quauqGroupStrategy);
					quauqBusinessLogService.log(oldData+"变为"+quauqGroupStrategy.toString(), QuauqBusinessLog.UPDATE_GROUP_STRATEGY);
				}
			}
		}
	}



	/**
	 * 设置费率页面查看
	 * @param request
	 * @return
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/10
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getChildrenAgentList(
			QueryQuauqStrategy quauqStrategy) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT a.agentName FROM agentinfo a ");
		sbf.append(" WHERE a.delFlag=0 AND a.agent_parent=? AND a.is_quauq_agent=1 ");
		List<Map<String,Object>> list=quauqGroupStrategyDao.findBySql(sbf.toString(),Map.class,
				Integer.parseInt(quauqStrategy.getAgentId()));
		return list;
	}

	@Override
	public Rate getGroupRate(String companyUuid, Long activityId, Integer productType, Long agentId) {
		List<Map<String, Object>> list = quauqGroupStrategyDao.getGroupRate(companyUuid, activityId, productType, agentId);
		Rate rate = new Rate();
		if(!Collections3.isEmpty(list)){
			Map<String, Object> map = list.get(0);
			rate = RateUtils.assignmentRate(map);
		}
		return rate;
	}

	@Override
	public TravelActivity getTravelActivityName(String groupId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT tr.* FROM activitygroup ag LEFT JOIN travelactivity tr ");
		sbf.append("ON ag.srcActivityId=tr.id ");
		sbf.append("WHERE ag.id=? ");
		List<TravelActivity> list=quauqGroupStrategyDao.findBySql(sbf.toString(), TravelActivity.class,groupId);
		return list.get(0);
	}


	@Override
	public QuauqGroupStrategy queryAgentByOfficeUUIdAndActivityId(String officeUUID,
			Long activityId) {
		/*String sql ="SELECT q.agent_id FROM quauq_group_strategy q WHERE q.activity_id ="+activityId+" and q.company_uuid ="+officeUUID+" and q.delFlag !=1";
		List<Object> findBySql = quauqGroupStrategyDao.findBySql(sql, activityId,officeUUID);*/
		QuauqGroupStrategy quauqGroupStrategy = quauqGroupStrategyDao.getByGroupIdandCompanyUUID(activityId,officeUUID);
		return quauqGroupStrategy;
	}


	@Override
	public Rate getMaxRate(Long groupId, Integer productType,
			BigDecimal quauqAdultPrice) {
		// TODO Auto-generated method stub
		String id = quauqGroupStrategyDao.getMaxRate(groupId,productType,quauqAdultPrice);
		if(id != null){
			if(id.startsWith("O")){
				OfficeRate officeRate = quauqGroupStrategyDao.getCompanyRateById(Integer.parseInt(id.substring(1)));
				return this.assignmentOfficeRate(officeRate);
			}else if(id.startsWith("G")){
				QuauqGroupStrategy auauqGroupStrategy = quauqGroupStrategyDao.getGroupRateById(Integer.parseInt(id.substring(1)));
				return this.assignmentGroupRate(auauqGroupStrategy);
			}
		}
		return null;
	}
	
	 /**
     * 将Map中的数据转换为Rate对象
     * @param map   map数据对象
     * @return
     * @author  xin.li
     * @date    2016.11.17
     */
    private Rate assignmentGroupRate(QuauqGroupStrategy quauqGroupStrategy){
        Rate rate = new Rate();
        if(null == quauqGroupStrategy){
            return rate;
        }
        Object quauqRateType = quauqGroupStrategy.getQuauqRateType();
        Object quauqRate = quauqGroupStrategy.getQuauqRate();
        Object quauqOtherRateType = quauqGroupStrategy.getQuauqOtherRateType();
        Object quauqOtherRate = quauqGroupStrategy.getQuauqOtherRate();
        Object agentRateType = quauqGroupStrategy.getAgentRateType();
        Object agentRate = quauqGroupStrategy.getAgentRate();
        Object agentOtherRateType = quauqGroupStrategy.getAgentOtherRateType();
        Object agentOtherRate = quauqGroupStrategy.getAgentOtherRate();
        rate.setQuauqRateType(Integer.valueOf(quauqRateType.toString()));
        rate.setQuauqRate(new BigDecimal(quauqRate.toString()));
        rate.setQuauqOtherRateType(Integer.valueOf(quauqOtherRateType.toString()));
        rate.setQuauqOtherRate(new BigDecimal(quauqOtherRate.toString()));
        rate.setAgentRateType(Integer.valueOf(agentRateType.toString()));
        rate.setAgentRate(new BigDecimal(agentRate.toString()));
        rate.setAgentOtherRateType(Integer.valueOf(agentOtherRateType.toString()));
        rate.setAgentOtherRate(new BigDecimal(agentOtherRate.toString()));
        return rate;
    }
    
    /**
     * 将Map中的数据转换为Rate对象
     * @param map   map数据对象
     * @return
     * @author  xin.li
     * @date    2016.11.17
     */
    private Rate assignmentOfficeRate(OfficeRate officeRate){
        Rate rate = new Rate();
        if(null == officeRate){
            return rate;
        }
        Object quauqRateType = officeRate.getQuauqRateType();
        Object quauqRate = officeRate.getQuauqRate();
        Object quauqOtherRateType = officeRate.getQuauqOtherRateType();
        Object quauqOtherRate = officeRate.getQuauqOtherRate();
        Object agentRateType = officeRate.getAgentRateType();
        Object agentRate = officeRate.getAgentRate();
        Object agentOtherRateType = officeRate.getAgentOtherRateType();
        Object agentOtherRate = officeRate.getAgentOtherRate();
        rate.setQuauqRateType(Integer.valueOf(quauqRateType.toString()));
        rate.setQuauqRate(new BigDecimal(quauqRate.toString()));
        rate.setQuauqOtherRateType(Integer.valueOf(quauqOtherRateType.toString()));
        rate.setQuauqOtherRate(new BigDecimal(quauqOtherRate.toString()));
        rate.setAgentRateType(Integer.valueOf(agentRateType.toString()));
        rate.setAgentRate(new BigDecimal(agentRate.toString()));
        rate.setAgentOtherRateType(Integer.valueOf(agentOtherRateType.toString()));
        rate.setAgentOtherRate(new BigDecimal(agentOtherRate.toString()));
        return rate;
    }
}
