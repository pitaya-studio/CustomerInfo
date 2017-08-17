package com.trekiz.admin.modules.reviewreceipt.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewreceipt.entity.ReviewReceiptConfiguration;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author yanzhenxing
 * @date 2015/11/30
 */
public interface ReviewReceiptConfigurationDao extends ReviewReceiptConfigurationDaoCustom,CrudRepository<ReviewReceiptConfiguration,String>{

    /**
     * 获取审批单据配置列表
     * @param companyId 公司id，uuid
     * @param receiptType 单据类别
     * @return
     */
    @Query("from ReviewReceiptConfiguration where companyId=?1 and receiptType=?2 and delFlag=0")
    List<ReviewReceiptConfiguration> findConfigurations(String companyId,String receiptType);

    /**
     * 删除审批单据配置
     * @param userId
     * @param updateDate
     * @param companyId
     * @param receiptType
     */
    @Modifying
    @Query("update ReviewReceiptConfiguration set updateBy =?1 ,updateDate=?2 ,delFlag=1 where companyId=?3 and receiptType=?4 and delFlag=0")
    void deleteConfigurations(String userId, Date updateDate,String companyId, String receiptType);
}

interface ReviewReceiptConfigurationDaoCustom extends BaseDao<ReviewReceiptConfiguration>{

}

@Repository
class ReviewReceiptConfigurationDaoImpl extends BaseDaoImpl<ReviewReceiptConfiguration> implements ReviewReceiptConfigurationDaoCustom{

}
