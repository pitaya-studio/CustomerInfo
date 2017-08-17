package com.trekiz.admin.agentToOffice.quauqstrategy.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.agentToOffice.T2.entity.OfficeRate;
import com.trekiz.admin.agentToOffice.quauqstrategy.entity.QuauqGroupStrategy;
import com.trekiz.admin.common.persistence.BaseDao;

/**
 * @time 2016/08/10
 * @author chao.zhang@quauq.com
 *
 */
public interface QuauqGroupStrategyDao extends BaseDao{

    /**
     * 查询相应团期设置的汇率
     * @param companyUuid	批发商UUID
     * @param activityId	团期或者产品ID
     * @param productType	产品类型
     * @param agentId		渠道ID
     * @return
     * @author	shijun.liu
     * @date	2016.08.11
     */
    public List<Map<String, Object>> getGroupRate(String companyUuid, Long activityId, Integer productType, Long agentId);
    
    public QuauqGroupStrategy getGroupRateById(Integer id);
    
    public OfficeRate getCompanyRateById(Integer id);

	public QuauqGroupStrategy getGroupRateByCG(String agentId,String companyUuid);
	
	public QuauqGroupStrategy getByGroupIdandCompanyUUID(Long activityId,String companyuuid);

	public String getMaxRate(Long groupId, Integer productType,
			BigDecimal quauqAdultPrice);
}
