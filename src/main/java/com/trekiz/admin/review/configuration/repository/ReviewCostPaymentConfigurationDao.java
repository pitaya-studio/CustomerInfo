package com.trekiz.admin.review.configuration.repository;

import com.trekiz.admin.review.configuration.entity.ReviewCostPaymentConfiguration;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * 成本付款配置Dao
 * @author yanzhenxing
 * @date 2015/12/9
 */
public interface ReviewCostPaymentConfigurationDao extends CrudRepository<ReviewCostPaymentConfiguration,String> {

    /**
     * 查询配置列表
     * @param deptId
     * @param productType
     * @param processType
     * @return
     */
    @Query("from ReviewCostPaymentConfiguration where deptId=?1 and productType=?2 and processType=?3 and delFlag=0")
    List<ReviewCostPaymentConfiguration> findConfigurations(String deptId,String productType,String processType);

    /**
     * 删除指定的配置
     * @param deptIds
     * @param productTypes
     * @param processType
     */
    @Modifying
    @Query(value = "update ReviewCostPaymentConfiguration set delFlag=1 where deptId in ?1 and productType in ?2 and processType=?3 and delFlag=0")
    void deleteConfigurations(Set<String> deptIds,Set<String> productTypes,String processType);
}
