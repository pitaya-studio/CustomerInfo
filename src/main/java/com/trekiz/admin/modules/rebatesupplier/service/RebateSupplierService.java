package com.trekiz.admin.modules.rebatesupplier.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.quauq.review.core.utils.IdGenerator;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.rebatesupplier.entity.RebateSupplier;
import com.trekiz.admin.modules.rebatesupplier.repository.RebateSupplierDao;
import com.trekiz.admin.modules.sys.service.PlatBankService;

/**
 * 返佣供应商服务类
 * @author yanzhenxing
 * @date 2016/1/7
 */
@Service
public class RebateSupplierService {

    @Autowired
    private RebateSupplierDao rebateSupplierDao;

    @Autowired
    private PlatBankService platBankService;

    /**
     * 保存供应商实体
     * @param rebateSupplier
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public RebateSupplier save(RebateSupplier rebateSupplier) throws Exception {
    	checkAndInitial(rebateSupplier);
    	return rebateSupplierDao.save(rebateSupplier);
        
    }

    /**
     * 保存银行信息并更新供应商基本信息状态
     * @param platId
     * @param platType
     * @param belongTypes
     * @param defaultFlags
     * @param accountNames
     * @param bankNames
     * @param bankAddrs
     * @param bankAccountCodes
     * @param rountings
     * @param swiftNums
     * @param phoneNums
     * @param remarks
     */
    @Transactional
    public void saveBankInfoAndSubmit(Long platId,Integer platType,String[] belongTypes,String[] defaultFlags,String[] accountNames,
                                      String[] bankNames,String[] bankAddrs,String[] bankAccountCodes,String[] rountings,String[] swiftNums,
                                      String[] phoneNums,String[] remarks){
        Assert.notNull(platId,"platId should not be null!");
        Assert.notNull(platType,"platType should not be null!");
        RebateSupplier supplier=obtain(platId);
        if (supplier==null){
            throw new RuntimeException("找不到供应商，id："+platId);
        }
        if(!supplier.getStatus().equals(RebateSupplier.STATUS_COMMITED)){//如果不是提交状态，进行提交
            supplier.setStatus(RebateSupplier.STATUS_COMMITED);
            rebateSupplierDao.save(supplier);
        }
        platBankService.batchSave(platId, platType,belongTypes,defaultFlags,accountNames,
                bankNames,bankAddrs,bankAccountCodes, rountings, swiftNums,phoneNums,remarks);
    }

    /**
     * 根据id删除供应商连带其银行账号信息
     * @param id
     */
    @Transactional
    public void delete(Long id,Long userId){
        Assert.notNull(id,"id should not be null!");
        Assert.notNull(userId,"userId should not be null!");
        rebateSupplierDao.deleteSupplier(id,userId,new Date());
        platBankService.delete(id, Context.PLAT_TYPE_SUP);
    }

    /**
     * 根据id获取供应商实体
     * @param id
     * @return
     */
    public RebateSupplier obtain(Long id){
        return rebateSupplierDao.findByIdAndDelFlag(id,RebateSupplier.DEL_FLAG_NORMAL);
    }

    /**
     * 验证供应商必填项并初始化uuid（如果不存在）
     * @param rebateSupplier
     */
    private void checkAndInitial(RebateSupplier rebateSupplier){
        Assert.notNull(rebateSupplier,"供应商对象不能为空!");
        Assert.hasText(rebateSupplier.getName(),"供应商名称不能为空!");
        Assert.hasText(rebateSupplier.getBrand(),"供应商品牌不能为空!");

        if (StringUtils.isBlank(rebateSupplier.getUuid())){
            rebateSupplier.setUuid(IdGenerator.generate32BitUuid());
        }
    }

    /**
     * 查询供应商page
     * @param page
     * @param queryParams
     * @return
     */
    public Page<Map<Object, Object>> getSupplierPage(Page<Map<Object, Object>> page,Map<String,String> queryParams){
        StringBuilder sql=new StringBuilder();
        /**
         * 组装sql
         * SELECT id,NAME,brand,person_in_charge AS personInCharge,operator_name AS operatorName,description FROM rebate_supplier WHERE company_id = 68 AND del_flag=0;
         */

        sql.append("SELECT id,name,brand,person_in_charge AS personInCharge,operator_name AS operatorName,description FROM rebate_supplier ");
        sql.append(" WHERE del_flag=0 AND status=1");//没有被删除并且状态为已提交
        if (queryParams!=null){
            if (queryParams.containsKey("companyId")){//查询当前批发商下的供应商
                sql.append(" AND company_id=").append(queryParams.get("companyId"));
            }
            if (queryParams.containsKey("name")){//供应商名称模糊查询
                sql.append(" AND name LIKE '%").append(queryParams.get("name")).append("%'");
            }
            if (queryParams.containsKey("operatorId")){//跟进计调人员查询
                sql.append(" AND operator_id=").append(queryParams.get("operatorId"));
            }
        }

        return rebateSupplierDao.findBySql(page,sql.toString(),Map.class);
    }

    /**
     * 根据公司uuid获取公司的可返佣的供应商
     * @param companyUuid
     * @return
     */
    public List<RebateSupplier> obtainSupplier(String companyUuid){
        Assert.hasText(companyUuid,"companyUuid should not be empty!");
        return rebateSupplierDao.findByCompanyUuidAndStatusAndDelFlag(companyUuid,RebateSupplier.STATUS_COMMITED,0);
    }

    /**
     * 根据公司uuid和跟进计调id获取可返佣的供应商
     * @param companyUuid
     * @param operatorId
     * @return
     */
    public List<RebateSupplier> obtainSupplierWithOperator(String companyUuid,Long operatorId){
        Assert.hasText(companyUuid,"companyUuid should not be empty!");
        Assert.notNull(operatorId,"operatorId should not be null!");
        return rebateSupplierDao.findByCompanyUuidAndOperatorIdAndStatusAndDelFlag(companyUuid,operatorId,RebateSupplier.STATUS_COMMITED,0);
    }

}
