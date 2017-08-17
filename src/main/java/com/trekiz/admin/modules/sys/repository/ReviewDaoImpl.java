package com.trekiz.admin.modules.sys.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDaoImpl;

@Repository
public class ReviewDaoImpl extends BaseDaoImpl<Map<String,Object>> implements IReviewDao {
	
	
	public int updateSql(String sql) {
		//String batchUpdateVisaStatusSql = "update visa set visa_stauts="+status+" where id in ("+visaIds+")";
		return  updateBySql(sql);
		 
	}
	 
	@Override
	public List<Map<String, Object>> findCompanyReview(String companyId) {
	String Sql = " SELECT t1.id, dept.name AS deptName,t1.deptId, t1.companyId ,t1.redo,t2.id AS flowId, "
      +" t2.flowName, t1.topLevel, maoouter.total AS totalLevel " 
      +"  FROM review_company t1  " 
        +"  LEFT JOIN department dept ON t1.deptId=dept.id   " 
      +"  LEFT JOIN review_flow t2   " 
       +" ON  t1.review_flow_id = t2.id  "  
        +"  LEFT JOIN (    " 
	     +"          SELECT review_company_id ,COUNT(*) AS total FROM (SELECT reviewLevel,review_company_id FROM  review_role_level  WHERE (delFlag IS NULL OR delFlag=0)  GROUP BY reviewLevel, review_company_id) AS tt GROUP BY tt.review_company_id  "   
	   	 +" 	  ) maoouter ON maoouter.review_company_id = t1.id  " 
         +"   WHERE  (t1.delFlag IS NULL OR t1.delFlag='0')  AND t1.companyId=? ";		
	 List<Map<String,Object>> review = findBySql(Sql, Map.class, Long.parseLong(companyId));
          return review;
	}
	
	@Override
	public List<Map<String, Object>> getNextReviewJob(long reviewCompanyId, int reviewLevel,int orderType,long dept,long parentdept ){
		 
		String Sql = " SELECT DISTINCT sysuser.name  FROM  review_role_level  l "
		+" JOIN sys_job job ON l.sys_job_id=job.id  AND l.delFlag=0 AND job.delFlag=0 "
		+" JOIN sys_user_dept_job sysjob ON sysjob.job_id=job.id AND sysjob.delFlag=0 "
        +" JOIN sys_user sysuser ON sysuser.id=sysjob.user_id AND  sysuser.delFlag=0  and (sysjob.dept_id="+dept +" or sysjob.dept_id="+parentdept +")"  
        +" WHERE l.review_company_id="+reviewCompanyId+" AND l.reviewLevel="+reviewLevel+"  AND job.orderType= "+ orderType;
		List<Map<String,Object>> review = findBySql(Sql, Map.class);
         return review;
	}
	
	@Override
	public List<Map<String, Object>> getEndReviewLevel(long reviewCompanyId, int reviewLevel,int orderType){		 
		String Sql = " SELECT l.id  FROM  review_role_level  l "
		+" JOIN sys_job job ON l.sys_job_id=job.id  AND l.delFlag=0 AND job.delFlag=0 and l.is_end=1 "	
        +" WHERE l.review_company_id="+reviewCompanyId+" AND l.reviewLevel="+reviewLevel+"  AND job.orderType= "+ orderType;
		List<Map<String,Object>> review = findBySql(Sql, Map.class);
         return review;
	}
	
	@Override
	public List<Map<String, Object>> getPayReviewPerson(long costId,long reviewCompanyId, int jobType,int orderType ){
		 
		String Sql = " SELECT  sysuser.name, job.jobName,l.reviewLevel FROM  review_role_level  l "
				+" JOIN sys_job job ON l.sys_job_id=job.id  AND l.delFlag=0 AND job.delFlag=0 AND job.jobType="+jobType
      +" JOIN cost_record_log costlog ON   costid="+ costId +"  AND costlog.nowLevel=l.reviewLevel"
      +" JOIN sys_user sysuser ON costlog.createBy=sysuser.id"
      +"  WHERE l.review_company_id="+reviewCompanyId+"   AND job.orderType="+ orderType;
		List<Map<String,Object>> review = findBySql(Sql, Map.class);
         return review;
	}
	
	
	@Override
	public List<Map<String, Object>> findReviewJob(Long reviewCompanyId,Integer orderType) {
	String Sql = "  SELECT review.reviewLevel AS level,sys_job.jobName AS job FROM review_role_level review "
    +" JOIN sys_job ON review.sys_job_id= sys_job.id "
    +" WHERE review.review_company_id=? AND review.delFlag=0  AND sys_job.orderType=?  order by level" ;		
	 List<Map<String,Object>> review = findBySql(Sql, Map.class, reviewCompanyId,orderType);
          return review;
	}	

	
	@Override
	public List<Map<String, Object>> findDeptList(Long userid){
		String Sql = "  SELECT dept.name,dept.id as deptid,dept.level,mydept.id FROM sys_user_dept mydept JOIN department dept ON  mydept.department_id=dept.id WHERE  mydept.user_id=? and mydept.delFlag='0' order by dept.id" ;
		 List<Map<String,Object>> review = findBySql(Sql, Map.class, userid);
         return review;				
	}
	
	
	/*public List<Map<String, Object>> findDeptJob(Long userid){
		String Sql = " SELECT me.id,me.user_id AS userId, me.dept_id AS deptId, dept.name AS deptName,sys_job.jobName ,dept.level as deptLevel" 
    +" FROM sys_user_dept_job me LEFT JOIN department dept ON  me.dept_id=dept.id "
    +" LEFT JOIN  sys_job ON me.job_id=sys_job.id WHERE  me.user_id=? and me.delFlag='0' order by dept.id" ;
		List<Map<String,Object>> review = findBySql(Sql, Map.class, userid);
        return review;		
	}*/
	public List<Map<String, Object>> findDeptJob(Long userid){
		String Sql = " SELECT me.id, me.user_id AS userId, me.dept_id AS deptId, dept. NAME AS deptName, sj. NAME jobName, dept. LEVEL AS deptLevel "
				+"FROM sys_user_dept_job_new me LEFT JOIN department dept ON me.dept_id = dept.id LEFT JOIN sys_job_new sj "
				+"ON me.job_id = sj.id WHERE me.user_id = ? AND me.del_flag = '0' ORDER BY dept.id" ;
		List<Map<String,Object>> review = findBySql(Sql, Map.class, userid);
        return review;		
	}
	
	
	@Override
	public List<Map<String, Object>> findCompanyReview(String companyId,Long dept) {
		String Sql = " SELECT t1.id, dept.name AS deptName,t1.deptId, t1.companyId ,t1.redo,t2.id AS flowId, "
			      +" t2.flowName, t1.topLevel, maoouter.total AS totalLevel " 
			      +"  FROM review_company t1  " 
			        +"  LEFT JOIN department dept ON t1.deptId=dept.id   " 
			      +"  LEFT JOIN review_flow t2   " 
			       +" ON  t1.review_flow_id = t2.id  "  
			        +"  LEFT JOIN (    " 
				     +"          SELECT review_company_id ,COUNT(*) AS total FROM (SELECT reviewLevel,review_company_id FROM  review_role_level  WHERE (delFlag IS NULL OR delFlag=0)  GROUP BY reviewLevel, review_company_id) AS tt GROUP BY tt.review_company_id  "   
				   	 +" 	  ) maoouter ON maoouter.review_company_id = t1.id  " 
               +" WHERE  t1.companyId=? and t1.deptId=? and  (t1.delFlag='0' or t1.delFlag IS NULL)  ";
		
	 List<Map<String,Object>> review = findBySql(Sql, Map.class, Long.parseLong(companyId), dept);
          return review;
	}
	
	
	
	@Override
	public List<Map<String, Object>> findDept(Long companyId) {
	//只需要给分公司级别(level=1)的部门配置审核	
	String Sql = "SELECT t.id, t.name  FROM department t where t.office_id= ? and level=1  and  (t.delFlag='0' or t.delFlag IS NULL)";    
		
	 List<Map<String,Object>> dept = findBySql(Sql, Map.class, companyId);
          return dept;
	}
	
	
	@Override
	public List<Map<String, Object>> findReviewDept(Long companyId) {
	//只需要给分公司级别(level=1 or 2)的部门配置	
	//String Sql = "SELECT t.id, t.name  FROM department t where t.office_id= ? and (level=1 or level=2) and (t.delFlag='0' or t.delFlag IS NULL)";    		
	String Sql = "SELECT t.id, t.name  FROM department t where t.office_id= ? and (t.delFlag='0' or t.delFlag IS NULL)";    		

	List<Map<String,Object>> dept = findBySql(Sql, Map.class, companyId);
          return dept;
	}
	
	@Override
	public List<Map<String, Object>> findCompanyDept(Long id) {
	String Sql = "SELECT t.id, t.redo,t.topLevel,t.deptId,d.name  FROM review_company  t " +
			"left join department d on d.id=t.deptId where t.id= ?";    
		
	 List<Map<String,Object>> dept = findBySql(Sql, Map.class, id);
          return dept;
	}
	
	@Override
	public List<Map<String, Object>> findReviewRoleList(String review_company_id){
	String Sql = "SELECT le.id,le.reviewLevel,le.is_end as isEnd,job.jobName, com.topLevel ,dp.name AS deptName,flow.flowName "   
			  +" FROM review_role_level le LEFT JOIN sys_job job ON le.sys_job_id=job.id "
			  +"  LEFT JOIN review_company com ON  le.review_company_id=com.id "
              +"  LEFT JOIN  department dp ON  com.deptId=dp.id "
              +"  LEFT JOIN review_flow flow ON com.review_flow_id=flow.id  WHERE le.review_company_id=? AND  (le.delFlag IS NULL OR le.delFlag='0') ORDER BY le.reviewLevel ";
		     
           List<Map<String,Object>> review = findBySql(Sql, Map.class, Integer.parseInt(review_company_id));
           return review;
	}
	
	@Override
	public List<Map<String, Object>> checkReviewCompany(Long companyId,Integer review_flow_id,Long deptId){
	String Sql = "SELECT review_flow_id from review_company where companyId=? and review_flow_id=? and deptId=? and (delFlag IS NULL OR delFlag='0')"; 			
           List<Map<String,Object>> review = findBySql(Sql, Map.class, companyId,review_flow_id,deptId);
          return review;
	}
	
	@Override
	public List<Map<String, Object>> checkUserDept(Long userid,Long deptid){
	String Sql = "SELECT department_id from sys_user_dept where user_id=? and department_id=?  and (delFlag IS NULL OR delFlag='0')"; 			
           List<Map<String,Object>> review = findBySql(Sql, Map.class, userid,deptid);
          return review;
	}
	
	@Override
	public List<Map<String, Object>> checkUserDeptJob(Long userid, Long deptid,Long jobid){
		//String Sql = "SELECT dept_id from sys_user_dept_job where user_id=? and dept_id=? and job_id=?  and (delFlag IS NULL OR delFlag='0')";
		String Sql = "SELECT dept_id FROM sys_user_dept_job_new WHERE user_id = ? AND dept_id =? AND job_id =? AND ( del_flag IS NULL OR del_flag = '0' )";
        List<Map<String,Object>> review = findBySql(Sql, Map.class, userid,deptid,jobid);
       return review;
		
	}
	
	@Override
	public List<Map<String, Object>> checkUserJob(Long userdeptid,Long jobid){
	String Sql = "SELECT id from  sys_user_dept_job where sys_user_dept_id=? and sys_job_id=?  and (delFlag IS NULL OR delFlag='0')"; 			
           List<Map<String,Object>> review = findBySql(Sql, Map.class, userdeptid,jobid);
          return review;
	}
	
	@Override
	public List<Map<String, Object>> checkReviewJob(Long reviewcompanyid,Integer jobid,Long id){
	String Sql = "SELECT sys_job_id from review_role_level where review_company_id=? and sys_job_id=? and id<>? and (delFlag IS NULL OR delFlag='0')"; 			
           List<Map<String,Object>> review = findBySql(Sql, Map.class, reviewcompanyid,jobid,id);
          return review;
	}	

	
	@Override
	public List<Map<String, Object>>  findUserJobList(Long userdeptid){
	String Sql = "SELECT userdept.id,sys_job.id as jobid,userdept.sys_job_id, sys_job.jobName from sys_user_dept_job userdept join sys_job on userdept.sys_job_id=sys_job.id where userdept.sys_user_dept_id=?  and (userdept.delFlag IS NULL OR userdept.delFlag='0') order by sys_job.id "; 			
           List<Map<String,Object>> review = findBySql(Sql, Map.class, userdeptid);
          return review;
	}
		
	@Override
	public List<Map<String, Object>>  findJobList(Long companyId){
	String Sql = "SELECT id,jobName from sys_job where  delFlag IS NULL OR delFlag='0'"; 			
           List<Map<String,Object>> review = findBySql(Sql, Map.class);
          return review;
	}
	
	@Override
	public List<Map<String, Object>>  findJobList(){
	String Sql = "SELECT id,jobName from sys_job where  delFlag IS NULL OR delFlag='0'"; 			
           List<Map<String,Object>> review = findBySql(Sql, Map.class);
          return review;
	}
		
	
	@Override
	public int  deleteReview(String id){
		String Sql="update review_company set delFlag=1 where  id=?";
		 return updateBySql(Sql, Long.parseLong(id));
	}
	
	@Override
	public int deleteUserDeptJob(Long  id,Long userid){
		String Sql="update sys_user_dept_job set delFlag=1 where  id=? and user_id=?";
		return updateBySql(Sql, id,userid);
	}
	
	@Override
	public int deleteUserJob(Long userdeptid, Long jobid) {		
		String Sql="update sys_user_dept_job set delFlag=1 where  sys_user_dept_id=? and sys_job_id=?";
		return updateBySql(Sql, userdeptid,jobid);
	}
	
	@Override
	public int  deleteReviewRole(Long id){
		String Sql="update review_role_level set delFlag=1 where  id=?";
		 return updateBySql(Sql, id);
	}
	
	@Override
	public List<Map<String, Object>> findRoleList(String officeId){
	String Sql = "SELECT le.id,le.jobname as name  FROM sys_job le "
            +"   WHERE  (le.delFlag IS NULL OR le.delFlag='0') ";	
	  List<Map<String,Object>> review = findBySql(Sql, Map.class);  
           //List<Map<String,Object>> review = findBySql(Sql, Map.class, Integer.parseInt(officeId));
          return review;
	}
	
	@Override
	public List<Map<String, Object>> findReviewFlow(){
	String Sql = "SELECT id,flowName from review_flow";		     
           List<Map<String,Object>> review = findBySql(Sql, Map.class);
          return review;
	}

	/**
	 * 查询下一环节审核人邮件信息 by chy 2015年5月5日17:37:33
	 */
	@Override
	public List<Map<String, Object>> getNextReviewEmail(long reviewCompanyId,
			int reviewLevel, int orderType, long dept, long parentdept) {
		String Sql = " SELECT  sysuser.name,sysuser.email,job.jobName FROM  review_role_level  l "
				+" JOIN sys_job job ON l.sys_job_id=job.id  AND l.delFlag=0 AND job.delFlag=0 "
				+" JOIN sys_user_dept_job sysjob ON sysjob.job_id=job.id AND sysjob.delFlag=0 "
		        +" JOIN sys_user sysuser ON sysuser.id=sysjob.user_id AND  sysuser.delFlag=0  and (sysjob.dept_id="+dept +" or sysjob.dept_id="+parentdept +")"  
		        +" WHERE l.review_company_id="+reviewCompanyId+" AND l.reviewLevel="+reviewLevel+"  AND job.orderType= "+ orderType;
				List<Map<String,Object>> review = findBySql(Sql, Map.class);
        return review;
	}

	@Override
	public void confirmOrCancelPay(Integer payStatus, Long userId,Date nowDate, String id) {
		StringBuffer sql = new StringBuffer("update review set payStatus=?, updateBy=?, updateDate=? where id in(?)");
		this.updateBySql(sql.toString(), payStatus,userId,nowDate,id);
		
	}
}
