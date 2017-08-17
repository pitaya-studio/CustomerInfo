package com.trekiz.admin.agentToOffice.T2.dao;

import com.trekiz.admin.agentToOffice.T2.entity.OfficeRate;
import com.trekiz.admin.common.persistence.BaseDao;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QuauqServiceFeeDao extends BaseDao {

    /**
     * 查询相应批发商设置的Quauq费率和渠道汇率
     * @param companyUuid       批发商UUID
     * @param agentType         渠道类型(门店、总社、集团客户)
     * @return
     * @author  shijun.liu
     * @date    2016.08.11
     */
    public List<Map<String, Object>> getCompanyRate(String companyUuid, Integer agentType);

    /**
     * 查询给定多个公司uuid的集合下的对应agentType下的所有记录。
     * @param uuidSet 批发商uuid的集合
     * @param agentType 渠道类型
     * @return 多个公司的agentType下的记录
     * @author  yudong.xu
     * @date  2016.08.16
     */
    public List<OfficeRate> getCompanyRates(Set<String> uuidSet, Integer agentType);

}
