package com.trekiz.admin.modules.reviewflow.repository;

import java.util.List;

import javax.persistence.OrderBy;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewflow.entity.ReviewRoleLevel;
import com.trekiz.admin.modules.sys.entity.Role;

interface ReviewRoleLevelDaoCustom extends BaseDao<ReviewRoleLevel> {
}

public interface ReviewRoleLevelDao extends ReviewRoleLevelDaoCustom, CrudRepository<ReviewRoleLevel, Long> {
	   
	  // @Query("from ReviewRoleLevel where rid = ?1 and delFlag ='0' ")
	   //public  List<ReviewRoleLevel> findReviewRoleLevel(Long rid);
	   
	 
	   @Query("select reviewLevel from ReviewRoleLevel where jobId = ?1 and reviewCompanyId=?2 and delFlag ='0' ")
	   @OrderBy("reviewLevel")
	   public  List<Integer> findReviewJobLevel(Long jobId,Long reviewCompanyId);
	  
	   
	   @Query("select distinct  reviewLevel from ReviewRoleLevel where  reviewCompanyId=?1 and reviewLevel=?2 and jobId in (?3)  and isEnd=1 and delFlag ='0' ")
	   public  List<Integer> findReviewJobList(Long reviewCompanyId,Integer reviewLevel,List<Long> jobId);	   
	   	 
	   @Query("select distinct  reviewLevel from ReviewRoleLevel where  reviewCompanyId=?1 and jobId in (?2)  and delFlag ='0' ")
	   public  List<Integer> findReviewJobList(Long reviewCompanyId,List<Long> jobId);	
	   /**
	    * 根据reviewCompanyId查找 相关流程节点的角色ids
	    * wxw added 2014-12-15 
	    * @param reviewCompanyId
	    * @return
	    */
	   @Query("select jobId from ReviewRoleLevel where reviewCompanyId=?1 and delFlag ='0' ")
	   public  List<Long> findjobIdsByReviewCompanyId(Long reviewCompanyId);
	   
	   @Query("select jobId from ReviewRoleLevel where reviewCompanyId=?1 and reviewLevel=?2 and delFlag ='0' ")
	   public  List<Long> findjobIdsByReviewCompanyId(Long reviewCompanyId,Integer reviewLevel);
	   
	   @Query("from ReviewRoleLevel where reviewCompanyId=?1 and isEnd=1 and delFlag ='0' ")
	   public  List<ReviewRoleLevel> findReviewRoleLevelEnd(Long reviewCompanyId);
	   
	   @Query("from ReviewRoleLevel where reviewCompanyId=?1")
	   public  List<Long> findRoles(Long reviewCompanyId);
	   
	   @Query("from Role where id in (select t1.jobId  from ReviewRoleLevel t1 where t1.reviewCompanyId in (?1) and t1.jobId in (?2) and delFlag ='0' )")
	    public List<Role> findReviewRole(List<Long> reviewCompanyId,List<Long> jobIds);
	   
	   
	     
	   @Query("from Role where id in (select t1.jobId  from ReviewRoleLevel t1 where t1.reviewCompanyId=?1 and t1.jobId in (?2) and delFlag ='0' )")
	    public List<Role> findReviewRole(Integer reviewCompanyId,List<Long> jobIds);
	  
	   
	   @Query("select t1.jobId  from ReviewRoleLevel t1 where t1.reviewCompanyId=?2 and t1.jobId in (?3) and nowLevel=?1 and delFlag ='0' ")
	    public List<Long> findReview(Integer nowLevel,Long reviewCompanyId,List<Long> jobIds);	
	  
	   @Query("from Role where id in (select t1.jobId  from ReviewRoleLevel t1 where t1.reviewCompanyId =?1 and t1.jobId in (?2) and delFlag ='0' )")
	    public List<Role> findReviewRole(Long reviewCompanyId,List<Long> jobIds);	
	  
	   /**
	    * 根据reviewCompanyId查找 相关流程某个层级的角色id
	    * wxw added 2015-2-04
	    * @param reviewCompanyId
	    * @return
	    */
	   @Query("select jobId from ReviewRoleLevel where reviewCompanyId=?1 and reviewLevel=?2 and delFlag ='0' ")
	   public  Long findRoleIdsByreviewCompanyIdAndLevel(Long reviewCompanyId,Integer level);
	   
	   @Query("select jobId from ReviewRoleLevel where reviewCompanyId in (?1) and delFlag ='0' ")
	   public  List<Long> findRoleIdsByReviewCompanyId(List<Long> reviewCompanyIds);
	   
	   @Query("select jobId from ReviewRoleLevel where reviewCompanyId in (?1) and delFlag ='0' ")
	   public  List<Long> findRoleIdsByReviewCompanyId(Long reviewCompanyIds);
	   
	   /**
	    * 根据cpid查找 相关流程某个层级的角色id
	    * wxw added 2015-2-04
	    * @param reviewCompanyId
	    * @return
	    */
	   @Query("select jobId from ReviewRoleLevel where reviewCompanyId=?1 and reviewLevel=?2 and delFlag ='0' ")
	   public  Long findRoleIdByCpidAndLevel(Long reviewCompanyId,Integer level);
	 
	   
}

class ReviewRoleLevelDaoImpl extends BaseDaoImpl<ReviewRoleLevel> implements ReviewRoleLevelDaoCustom {
	
	
	
}