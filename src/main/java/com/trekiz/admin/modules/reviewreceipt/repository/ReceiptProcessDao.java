package com.trekiz.admin.modules.reviewreceipt.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewreceipt.entity.ReceiptProcess;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 单据流程dao
 * @author yanzhenxing
 * @date 2015/11/30
 */
public interface ReceiptProcessDao extends ReceiptProcessDaoCustom,CrudRepository<ReceiptProcess,String>{
}

interface ReceiptProcessDaoCustom extends BaseDao<ReceiptProcess>{

}

@Repository
class ReceiptProcessDaoImpl extends BaseDaoImpl<ReceiptProcess> implements ReceiptProcessDaoCustom{

}
