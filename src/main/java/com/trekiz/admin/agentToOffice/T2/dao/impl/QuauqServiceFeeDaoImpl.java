package com.trekiz.admin.agentToOffice.T2.dao.impl;

import com.trekiz.admin.agentToOffice.T2.entity.OfficeRate;
import org.springframework.stereotype.Service;

import com.trekiz.admin.agentToOffice.T2.dao.QuauqServiceFeeDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class QuauqServiceFeeDaoImpl extends BaseDaoImpl implements QuauqServiceFeeDao {

    @Override
    public List<Map<String, Object>> getCompanyRate(String companyUuid, Integer agentType) {
        StringBuffer str = new StringBuffer();
        str.append(" SELECT ")
           .append(" 	quauq_rate_type,")
           .append(" 	quauq_rate,")
           .append(" 	quauq_other_rate_type,")
           .append(" 	quauq_other_rate,")
           .append(" 	agent_rate_type,")
           .append(" 	agent_rate,")
           .append(" 	agent_other_rate_type,")
           .append(" 	agent_other_rate,")
           .append(" chouchengRateType,")
           .append(" chouchengRate")
           .append(" FROM")
           .append("    sys_office_rate ")
           .append(" WHERE")
           .append(" 	company_uuid = ?")
           .append(" AND agent_type = ?");
        List<Map<String, Object>> list = findBySql(str.toString(), Map.class, companyUuid, agentType);
        return list;
    }

    @Override
    public List<OfficeRate> getCompanyRates(Set<String> uuidSet, Integer agentType){
        if (uuidSet == null || uuidSet.size() == 0){
            return new ArrayList<>();
        }
        String sql = "FROM OfficeRate WHERE agentType = :agentType AND companyUuid IN :uuidSet";
        Query query = super.getEntityManager().createQuery(sql,OfficeRate.class);
        query.setParameter("agentType",agentType);
        query.setParameter("uuidSet",uuidSet);
        return query.getResultList();
    }

}
