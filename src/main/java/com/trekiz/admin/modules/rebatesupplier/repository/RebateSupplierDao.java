package com.trekiz.admin.modules.rebatesupplier.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.rebatesupplier.entity.RebateSupplier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 返佣供应商dao接口
 * @author yanzhenxing
 * @date 2016/1/7
 */
public interface RebateSupplierDao extends CrudRepository<RebateSupplier,Long>,RebateSupplierDaoCustom{

    /**
     * 根据id和删除标识位查找
     * @param id
     * @param delFlag
     * @return
     */
    RebateSupplier findByIdAndDelFlag(Long id,Integer delFlag);

    /**
     * 根据公司uuid，状态查找供应商列表
     * @param companyUuid
     * @param status
     * @param delFlag
     * @return
     */
    List<RebateSupplier> findByCompanyUuidAndStatusAndDelFlag(String companyUuid,Integer status,Integer delFlag);

    /**
     * 根据公司id、跟进计调id，状态查找供应商列表
     * @param companyUuid
     * @param operatorId
     * @param status
     * @param delFlag
     * @return
     */
    List<RebateSupplier> findByCompanyUuidAndOperatorIdAndStatusAndDelFlag(String companyUuid,Long operatorId,Integer status,Integer delFlag);

    /**
     * 删除供应商，同时设置更新者和更新时间
     * @param id
     * @param updateBy
     * @param updateDate
     */
    @Modifying
    @Query("update RebateSupplier set delFlag=1,updateBy=?2,updateDate=?3 where id=?1")
    void deleteSupplier(Long id, Long updateBy, Date updateDate);
}

/**
 * DAO自定义接口
 */
interface RebateSupplierDaoCustom extends BaseDao<RebateSupplier> {

}

/**
 * DAO自定义接口实现
 */
@Repository
class RebateSupplierDaoImpl extends BaseDaoImpl<RebateSupplier> implements RebateSupplierDaoCustom {

}
