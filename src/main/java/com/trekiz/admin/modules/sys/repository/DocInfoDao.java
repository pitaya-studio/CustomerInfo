package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.DocInfo;

/**
 * 文件上传附件DAO接口
 * @author liangjingming
 *
 */
public interface DocInfoDao extends DocInfoDaoCustom,CrudRepository<DocInfo, Long>{

    @Modifying
    @Query("delete from DocInfo where id = ?1")
    public void delDocInfoById(Long id);
    
    @Query("from DocInfo where payOrderId = ?1")
    public List<DocInfo> findDocInfoByPayOrderId(Long orderId);
    
    @Query("from DocInfo where id in (?1)")
    public List<DocInfo> findDocInfoByIds(List<Long> docIdList);
    
    @Modifying 
    @Query("update DocInfo set payOrderId =?1 where id in(?2)")
    public void updateDocInfoPayOrderId(Long payOrderId, List<Long> docIdList);
}

/**
 * 自定义接口
 * @author liangjingming
 *
 */
interface DocInfoDaoCustom extends BaseDao<DocInfo>{
	
}

/**
 * 自定义接口实现
 * @author liangjingming
 *
 */
@Repository
class DocInfoDaoImpl extends BaseDaoImpl<DocInfo> implements DocInfoDaoCustom{
	
}
