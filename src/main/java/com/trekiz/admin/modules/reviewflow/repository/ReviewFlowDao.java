package com.trekiz.admin.modules.reviewflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewflow.entity.ReviewFlow;

interface ReviewFlowDaoCustom extends BaseDao<ReviewFlow> {
}

public interface ReviewFlowDao extends ReviewFlowDaoCustom, CrudRepository<ReviewFlow, Integer> {
	   
	   @Query("from ReviewFlow where id = ?1 ")
	    public  List<ReviewFlow> findReviewFlow(Long id);
	   
	   @Query("from ReviewFlow where productType= ?1 and flowType=?2 ")
	    public  List<ReviewFlow> findReviewFlow(Integer productType,Integer flowType);	  
	   
	   /**
	    * 新加如下查询 by chy2014年12月15日15:37:38 查询所有产品的某个流程的reviewflow信息
	    * @param flowType
	    * @return
	    */
	   @Query("from ReviewFlow where id=?1 ")
	    public  List<ReviewFlow> findReviewFlow(Integer flowType);
	   

}

class ReviewFlowDaoImpl extends BaseDaoImpl<ReviewFlow> implements ReviewFlowDaoCustom {
	
}