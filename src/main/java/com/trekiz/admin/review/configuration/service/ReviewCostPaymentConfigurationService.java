package com.trekiz.admin.review.configuration.service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.review.configuration.entity.ReviewCostPaymentConfiguration;
import com.trekiz.admin.review.configuration.repository.ReviewCostPaymentConfigurationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 成本付款审批配置服务类
 * @author yanzhenxing
 * @date 2015/12/9
 */
@Service
public class ReviewCostPaymentConfigurationService {

    private static Logger logger= LoggerFactory.getLogger(ReviewCostPaymentConfigurationService.class);

    @Autowired
    private ReviewCostPaymentConfigurationDao reviewCostPaymentConfigurationDao;

    /**
     * 保存配置
     * @param userId 当前操作用户id
     * @param companyId 当前公司id，使用uuid
     * @param deptIds 部门列表
     * @param productTypes 产品类型列表
     * @param processType 流程类型
     */
    @Transactional
    public void save(String userId, String companyId, Set<String> deptIds,Set<String> productTypes,String processType,Integer config){

        Assert.hasText(userId,"userId should not be empty!");
        Assert.hasText(companyId,"companyId should not be empty!");
        Assert.hasText(processType,"processType should not be empty!");
        Assert.notEmpty(deptIds,"deptIds should not be empty!");
        Assert.notEmpty(productTypes,"productTypes should not be empty!");
        Assert.notNull(config,"config should not be null!");

        /**
         * 校验processType 是否是成本付款
         */
        Assert.isTrue(Context.REVIEW_FLOWTYPE_PAYMENT.equals(Integer.parseInt(processType)),"processType 只能为成本付款审批");
        /**
         * 先删除原来的配置，再保存新配置
         */
        reviewCostPaymentConfigurationDao.deleteConfigurations(deptIds,productTypes,processType);

        List<ReviewCostPaymentConfiguration> configurations=new ArrayList<>();
        Date currentDate=new Date();

        for (String deptId:deptIds){
            for (String productType:productTypes){
                ReviewCostPaymentConfiguration configuration=new ReviewCostPaymentConfiguration();
                configuration.setId(UuidUtils.generUuid());
                configuration.setCompanyId(companyId);
                configuration.setCreateBy(userId);
                configuration.setCreateDate(currentDate);
                configuration.setDelFlag(0);
                configuration.setIsPaymentEqualsCost(config);
                configuration.setProcessType(processType);
                configuration.setProductType(productType);
                configuration.setDeptId(deptId);

                configurations.add(configuration);
            }
        }
        reviewCostPaymentConfigurationDao.save(configurations);
    }

    /**
     * 根据部门id、产品类型、流程类型查询配置
     * @param deptId 部门id
     * @param productType 产品类型
     * @param processType 流程类型
     * @return ReviewCostPaymentConfiguration 查不到时返回null
     */
    public ReviewCostPaymentConfiguration getConfiguration(String deptId,String productType,String processType){
        Assert.hasText(deptId,"deptId should not be empty!");
        Assert.hasText(productType,"productType should not be empty!");
        Assert.hasText(processType,"processType should not be empty!");

        List<ReviewCostPaymentConfiguration> configurations=reviewCostPaymentConfigurationDao.findConfigurations(deptId,productType,processType);

        if (configurations==null||configurations.size()!=1){
            logger.error("wrong size of configurations!");
            return null;
        }

        return configurations.get(0);
    }
}
