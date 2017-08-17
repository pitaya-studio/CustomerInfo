package com.trekiz.admin.modules.reviewflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewflow.entity.ReviewCompany;

interface ReviewCompanyDaoCustom extends BaseDao<ReviewCompany> {
}


public interface ReviewCompanyDao extends ReviewCompanyDaoCustom, CrudRepository<ReviewCompany, Long> {
	   @Query("from ReviewCompany  where id=?1")
	    public List<ReviewCompany> findReviewCompany(Long id);	  
	
	    @Query("select t1.id from ReviewCompany t1  where  t1.companyId=?1  and t1.reviewFlowId=?2 and t1.deptId=?3 and t1.delFlag ='0' ")
	    public List<Long> findReviewCompanyList(Long companyId,Integer flowType,Long deptId); 
	    
	    @Query("select t1.id from ReviewCompany t1  where  t1.companyId=?1 and  t1.reviewFlowId=?2 and t1.delFlag ='0' ")
	    public List<Long> findReviewCompanyList(Long companyId,Integer flowType); 
	    

	    @Query("select t1.topLevel from ReviewCompany t1  where   t1.companyId=?1 and t1.reviewFlowId=?2 and t1.delFlag ='0' ")
	    public Integer findTopLevel(Long companyId,Integer flowType);
	    

	    @Query("select t1.id from ReviewCompany t1 where   t1.companyId=?1 and t1.reviewFlowId=?2 and t1.deptId=?3 and t1.delFlag='0'")
		 public List<Long> findReviewCompanyListDept(Long companyId,Integer flowId,Long deptId); 
	    
	   
	   
	    //成本审核获得与渠道商无关的记录主键
	    //@Query("select t1.id from ReviewCompany t1 , ReviewFlow t2 where  t1.reviewFlowId=t2.id and  t1.companyId is NULL and   t2.id=?2")
	    //public List<Long> findReviewCompany(Integer productType,Integer flowType); 
	    
	   
	    //@Query("select t1.id from ReviewCompany t1 , ReviewFlow t2 where  t1.reviewFlowId=t2.id and  t1.companyId=?1 and t1.deptId in (?3)  and  t2.id=?2 and t1.delFlag ='0' ")
	    //public List<Long> findReviewCompanyList(Long companyReviewId,Integer flowType,List<Long> deptId); 
	   
	   /**
	    * add by chy 2014年12月11日19:43:12为了查询所有产品的level
	    * @param companyId
	    * @param flowType
	    * @return
	    */
	   @Query("select t1.id from ReviewCompany t1 where t1.reviewFlowId=?2 and  t1.companyId=?1 and t1.delFlag='0'")
	    public List<Long> findReviewCompanyList(Long companyId, Long flowId); 
	   
	   @Query("from ReviewCompany where companyId = ?1 and reviewFlowId=?2 and delFlag='0'")
	    public List<ReviewCompany> findReviewCompany(Long companyId,Long flowId);	
	   
	   @Query("from ReviewCompany where companyId = ?1 and reviewFlowId=?2 and deptId=?3 and delFlag='0'")
	    public List<ReviewCompany> findReviewCompanyDept(Long companyId,Integer flowId,Long deptId);  	 
  
	   @Query("select id from ReviewCompany where companyId = ?1 and reviewFlowId=?2 and deptId=?3 and delFlag='0'")
	    public List<Long> findReviewCompanyId(Long companyId,Integer flowId,Long deptId);  	 
 
	   
	   @Query("select id from  ReviewCompany where companyId = ?1 and reviewFlowId=?2 and deptId in (?3)  and delFlag='0'")
	    public List<Long> findReviewCompanyIds(Long companyId,Integer flowId,List<Long> deptIds);
 	 

	   
}

class ReviewCompanyDaoImpl extends BaseDaoImpl<ReviewCompany> implements ReviewCompanyDaoCustom {


}