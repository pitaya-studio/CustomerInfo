package com.trekiz.admin.modules.sys.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReviewDao {
	/* 付款确认或撤销,设置 payStatus=1(已付款)0(未付款) */
	void confirmOrCancelPay(Integer payStatus, Long userId, Date nowDate, String id);
	List<Map<String, Object>> findCompanyReview(String companyId);
	
	List<Map<String, Object>> getNextReviewJob(long reviewCompanyId, int reviewLevel,int orderType,long dept,long parentdept );
	
	List<Map<String, Object>> getEndReviewLevel(long reviewCompanyId, int reviewLevel,int orderType);
	
	
	List<Map<String, Object>> getPayReviewPerson(long costId,long reviewCompanyId, int jobType,int orderType );
	/*获取下一环节审核人邮件信息*/
	List<Map<String, Object>> getNextReviewEmail(long reviewCompanyId, int reviewLevel,int orderType,long dept,long parentdept );
	
	List<Map<String, Object>> findReviewRoleList(String cpid);
	
	int  deleteReview(String id);

	int updateSql(String sql);

	int deleteReviewRole(Long id);

	List<Map<String, Object>> findRoleList(String officeId);

	List<Map<String, Object>> findReviewFlow();

	List<Map<String, Object>> checkReviewCompany(Long companyId, Integer reviewId,Long deptId);
	
	List<Map<String, Object>> checkReviewJob(Long reviewcompanyid,Integer jobid,Long id);

	
	List<Map<String, Object>> findDept(Long companyId);

	List<Map<String, Object>> findCompanyReview(String companyId, Long dept);

	List<Map<String, Object>> findCompanyDept(Long id);

	List<Map<String, Object>> findDeptList(Long userid);

	List<Map<String, Object>> findReviewDept(Long companyId);

	List<Map<String, Object>> checkUserDept(Long userid, Long deptid);

	List<Map<String, Object>> checkUserJob(Long userid, Long jobid);

	List<Map<String, Object>> findUserJobList(Long userdeptid);

	List<Map<String, Object>> findJobList(Long companyId);
	List<Map<String, Object>> findJobList();
	
	int deleteUserDeptJob(Long id, Long userid);

	int deleteUserJob(Long userdeptid, Long jobid);

	List<Map<String, Object>> findDeptJob(Long userid);

	List<Map<String, Object>> checkUserDeptJob(Long userid, Long deptid,
			Long jobid);

	List<Map<String, Object>> findReviewJob(Long reviewCompanyId,Integer orderType);

	
	 
}
