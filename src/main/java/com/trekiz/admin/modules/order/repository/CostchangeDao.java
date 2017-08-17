/**
 *
 */
package com.trekiz.admin.modules.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.Costchange;


 /**
 *  文件名: CostchangeDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:33
 *  @version 1.0
 */
public interface CostchangeDao extends CostchangeDaoCustom, CrudRepository<Costchange, Long> {
    @Query("from Costchange where travelerId = ?1 and status = 2")
    public List<Costchange> findCostchangeBytravelerId(Long travelerId);

     @Query("from Costchange where travelerId = ?1 and status = ?2 and reviewUuid is not null and reviewUuid <> ''")
     public List<Costchange> findCostchangeBytravelerIdAndStatus(Long travelerId, Integer status);
    
    @Modifying
    @Query("delete Costchange where travelerId = ?1 and businessType = 0")
    public void deleteCostchangeByTravelerId(Long travelerId);
    
    @Modifying
    @Query("delete Costchange where id in(?1) and businessType = 0")
    public void deleteByIds(List<Long> listcostId);

     @Modifying
     @Query("update Costchange set status = ?1 where reviewUuid = ?2")
     public void updateStatusByReviewUuid(Integer status, String reviewUuid);

}


 /**
 *  文件名: CostchangeDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:25
 *  @version 1.0
 */
interface CostchangeDaoCustom extends BaseDao<Costchange> {

}

 /**
 *  文件名: CostchangeDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:15
 *  @version 1.0
 */
@Repository
class CostchangeDaoImpl extends BaseDaoImpl<Costchange> implements CostchangeDaoCustom {

}
