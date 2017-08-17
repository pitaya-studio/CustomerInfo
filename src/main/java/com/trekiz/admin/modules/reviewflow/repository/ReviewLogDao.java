package com.trekiz.admin.modules.reviewflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;

interface ReviewLogDaoCustom extends BaseDao<ReviewLog> {
}

public interface ReviewLogDao extends ReviewLogDaoCustom, CrudRepository<ReviewLog, Long> {
	   
	   @Query("from ReviewLog where reviewId = ?1 order by createDate ")
	    public  List<ReviewLog> findReviewLog(Long reviewId);
	   
	    @Query("from ReviewLog where reviewId = ?1 and nowLevel=?2")
	    public  List<ReviewLog> findReviewLogByNowLevel(Long reviewId, Integer nowLevel);
	  
	    @Modifying
	    @Query("delete from ReviewLog where Id = ?1")
	    public void removeReviewLog(Long orderId);
	    
	   //  @Modifying
	   // @Query("update ReviewLog set ID=1 where orderType = ?1 and orderId=?2 and pid=?3")
	   // public void updateReviewLog(Integer orderType,Integer orderId,Integer pid);	 
	    
	    public List<ReviewLog> findByReviewIdAndNowLevel(Long rid, Integer nowLevel);
	    
	    
	    @Query("from ReviewLog where reviewId = ?1 order by id desc ")
	    public  List<ReviewLog> findReviewLogByOderedId(Long reviewId);

}

class ReviewLogDaoImpl extends BaseDaoImpl<ReviewLog> implements ReviewLogDaoCustom {
	
}