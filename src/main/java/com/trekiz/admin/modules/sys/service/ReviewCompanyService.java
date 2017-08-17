package com.trekiz.admin.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.sys.repository.IReviewDao;


@Service
@Transactional(readOnly = true)
public class ReviewCompanyService extends BaseService {
	
	
	@Autowired
	private IReviewDao reviewDao;
	
	public List<Map<String, Object>> findCompanyReview(String companyId){
		 return reviewDao.findCompanyReview(companyId);
	}
	
	public List<Map<String, Object>> findCompanyReview(String companyId,Long dept){
		 return reviewDao.findCompanyReview(companyId, dept);
	}
	
	public List<Map<String, Object>> findDept(Long companyId){
		 return reviewDao.findDept(companyId);
	}
	
	public List<Map<String, Object>> findCompanyDept(Long id){
		 return reviewDao.findCompanyDept(id);
	}
	
	public List<Map<String, Object>> findReviewRoleList(String cpid){
		 return reviewDao.findReviewRoleList(cpid);
	}
	
	public List<Map<String, Object>> findReviewJob(Long reviewCompanyId,Integer orderType){
		 return reviewDao.findReviewJob(reviewCompanyId,orderType);
	}
	/*
	public List<Map<String, Object>> findFlowInfo(String id){
		 return reviewDao.findFlowInfo(id);
	}*/
	
	public List<Map<String, Object>> findRoleList(String officeId){
		  return reviewDao.findRoleList(officeId);
	}
	
	public List<Map<String, Object>> findReviewFlow(){
		  return reviewDao.findReviewFlow();
	}
	
	public int deleteReview(String id){
		 return reviewDao.deleteReview(id);
	}
	
	public int deleteUserDeptJob(Long  id,Long userid){
		 return reviewDao.deleteUserDeptJob(id,userid);
	}	
	
	public int deleteReviewRole(Long id){
		 return reviewDao.deleteReviewRole(id);
	}
	
	public int updateSql (String sql){
		 return reviewDao.updateSql(sql);
	}
	
	
	public List<Map<String, Object>> checkReviewCompany(Long companyId,Integer reviewId,Long deptId){
		 return reviewDao.checkReviewCompany(companyId, reviewId,deptId);
	}
	
	public List<Map<String, Object>> checkUserDept(Long userid,Long deptid){
		 return reviewDao.checkUserDept(userid, deptid);
	}
	
	public List<Map<String, Object>> checkUserDeptJob(Long userid,Long deptid,Long jobid){
		 return reviewDao.checkUserDeptJob(userid, deptid,jobid);
	}
	
	public List<Map<String, Object>> checkReviewJob(Long reviewcompanyid,Integer jobid,long id){
		 return reviewDao.checkReviewJob( reviewcompanyid,jobid,id);
	}
	
	public List<Map<String, Object>> findDeptList(Long userid){
		 return reviewDao.findDeptList(userid);
	}
	
	public List<Map<String, Object>> findDeptJob(Long userid){
		 return reviewDao.findDeptJob(userid);
	}

	public Object findReviewDept(Long companyId) {
		return reviewDao.findReviewDept(companyId);
	}

	public List<Map<String, Object>> checkUserJob(Long userid, Long jobid) {
		return reviewDao.checkUserJob(userid,jobid);
	}

	public List<Map<String, Object>> findUserJobList(Long userdeptid) {
		return reviewDao.findUserJobList(userdeptid);
	}

	public List<Map<String, Object>> findJobList() {
		return reviewDao.findJobList();
	}
	
	public List<Map<String, Object>> findJobList(Long companyId) {
		return reviewDao.findJobList(companyId);
	}

	public int deleteUserJob(Long userdeptid, Long jobid) {
		 return reviewDao.deleteUserJob(userdeptid,jobid);
		
	}
	
	
}
