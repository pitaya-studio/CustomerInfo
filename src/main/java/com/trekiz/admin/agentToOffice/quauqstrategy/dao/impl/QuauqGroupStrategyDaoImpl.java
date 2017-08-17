package com.trekiz.admin.agentToOffice.quauqstrategy.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.agentToOffice.T2.entity.OfficeRate;
import com.trekiz.admin.agentToOffice.quauqstrategy.dao.QuauqGroupStrategyDao;
import com.trekiz.admin.agentToOffice.quauqstrategy.entity.QuauqGroupStrategy;
import com.trekiz.admin.common.persistence.BaseDaoImpl;

@Service
@Transactional(readOnly = true)
public class QuauqGroupStrategyDaoImpl extends BaseDaoImpl implements QuauqGroupStrategyDao{

    @Override
    public List<Map<String, Object>> getGroupRate(String companyUuid, Long activityId, Integer productType, Long agentId) {
        StringBuffer str = new StringBuffer();
        str.append(" SELECT ")
           .append(" 	quauq_rate_type,")
           .append(" 	quauq_rate,")
           .append(" 	quauq_other_rate_type,")
           .append(" 	quauq_other_rate,")
           .append(" 	agent_rate_type,")
           .append(" 	agent_rate,")
           .append(" 	agent_other_rate_type,")
           .append(" 	agent_other_rate")
           .append(" FROM")
           .append(" 	quauq_group_strategy")
           .append(" WHERE")
           .append(" 	company_uuid = ?")
           .append(" AND activity_id = ?")
           .append(" AND product_type = ?")
           .append(" AND agent_id = ?");
        List<Map<String, Object>> list = findBySql(str.toString(), Map.class,
                companyUuid, activityId, productType, agentId);
        return list;
    }

	@Override
	public QuauqGroupStrategy getGroupRateById(Integer id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM quauq_group_strategy WHERE id=?");
		List<QuauqGroupStrategy> list=this.findBySql(sbf.toString(), QuauqGroupStrategy.class,id);
		return list.get(0);
	}

	@Override
	public OfficeRate getCompanyRateById(Integer id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM sys_office_rate WHERE id=?");
		List<OfficeRate> list=this.findBySql(sbf.toString(), OfficeRate.class,id);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public QuauqGroupStrategy getGroupRateByCG(String groupId,String agentId) {
		String sql = "SELECT * FROM quauq_group_strategy WHERE delFlag = 0 and activity_id = ? and agent_id = ?";
		List<QuauqGroupStrategy> list=this.findBySql(sql, QuauqGroupStrategy.class,groupId,agentId);
		if(list.size() <= 0){
			return null;
		}
		return list.get(0);
	}

	@Override
	public QuauqGroupStrategy getByGroupIdandCompanyUUID(Long activityId,
			String companyuuid) {
		// TODO Auto-generated method stub
		String sql ="SELECT q.agent_id FROM quauq_group_strategy q WHERE q.activity_id ="+activityId+" and q.company_uuid = '"+companyuuid+"' and q.delFlag !=1";
		//String sql ="SELECT q.agent_id FROM quauq_group_strategy q WHERE q.activity_id = ? and q.company_uuid = ? and q.delFlag !=1";
		List<QuauqGroupStrategy> list = this.findBySql(sql, QuauqGroupStrategy.class);
		if(list.size() <= 0){
			return null;
		}
		return list.get(0);

	}

	@Override
	public String getMaxRate(Long groupId, Integer productType,
			BigDecimal quauqAdultPrice) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("select GetMaxRateByGroupid(?,?,?) as id ");
		List<Map<String, Object>> list=this.findBySql(sbf.toString(), Map.class,groupId,quauqAdultPrice,productType);
		if(list.size() >0){
			Map<String, Object> tempId1 = (Map<String, Object>)list.get(0);
			String tempId2 = tempId1.get("id").toString();
			if(tempId2 != null && tempId2!="null" && tempId2 != "" ){
				return tempId2;
			}
			return null;
		}
		return null;
	}
	
}
