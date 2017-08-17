package com.trekiz.admin.modules.review.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trekiz.admin.modules.order.util.ReviewCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewFlowDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.RoleDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 流程审核过程公用的不好归到某个service中的方法
 * @author 
 */
@Component
public class ReviewCommonService {
	
	@Autowired
	private ReviewFlowDao reviewFlowDao;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	@Autowired
	private ReviewRoleLevelDao reviewRoleLevelDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserJobDao userJobDao;
//	@Autowired
//	private OrderReviewService orderReviewService;
	
	public  List<UserJob>  getWorkFlowJobByFlowType(Integer flowType) {
		
		//1.根据flowType 和 产品类型找出  review_flow 的ID
		// 由于有些流程是不区分产品类型的 如退款 是针对所有的产品的 并且产品类型是作为查询条件的 新增当产品类型为null时 不过滤这个查询条件 modify by chy 2014年12月15日15:36:10
		
	  long companyId = UserUtils.getUser().getCompany().getId();
	  long userId = UserUtils.getUser().getId();
	  
	  List<UserJob> userJobList = new ArrayList<UserJob>();
	  List<UserJob> myUserJobList = new ArrayList<UserJob>();	  
	  userJobList=userJobDao.getUserJobList(userId);	 
	  
	  for (UserJob userjob : userJobList) { 		  
		  Long deptId;
		  if(userjob.getDeptLevel()==1){
			  deptId=userjob.getDeptId();
		  }else if(userjob.getDeptLevel()==2){
			  deptId=userjob.getParentDept();
		  } else{
			  return null;
		 }
		 List<Long> reviewCompanyId = new ArrayList<Long>();
		 
		 reviewCompanyId = reviewCompanyDao.findReviewCompanyList(companyId, flowType,deptId);
		 
		  if(reviewCompanyId.size()>0){
		      List<Integer> reviewList;	
		       reviewList =reviewRoleLevelDao.findReviewJobLevel(userjob.getJobId(),(long)reviewCompanyId.get(0));
		      if(reviewList.size() >0){
		    	 myUserJobList.add(userjob);
		       }
		  }		  
	  }		  
	//装配审核职务对应的审核状态  审核条数  
	ReviewCommonUtil.getUserJobsReviewCountByType(flowType,myUserJobList,1);	 
	return myUserJobList;
		
	}
	
	public  List<UserJob>  getReviewByFlowType(Integer flowType,Integer orderType) {
		
		//获得审核业务需要的的 userJob 类
	 List<UserJob> userJobList = new ArrayList<UserJob>();
	  long companyId = UserUtils.getUser().getCompany().getId();
	  long userId = UserUtils.getUser().getId();
	  List<Long> deptList= getDeptList(userId);
	  /* if(deptList.size()==0){
			System.out.println("没有给用户配置部门");
			return userJobList;
		} */ 	   
		List<UserJob> myUserJobList = new ArrayList<UserJob>();	  
		userJobList=userJobDao.getUserJobList(userId);		
		List<Long> listCompany = new ArrayList<Long>();
		listCompany = reviewCompanyDao.findReviewCompanyIds(companyId,flowType,deptList);
		for (UserJob userjob : userJobList) { 
			 if (userjob.getOrderType()==orderType){		 
			    Long deptId;
				  if(userjob.getDeptLevel()==1){
					  deptId=userjob.getDeptId();
				  }else if(userjob.getDeptLevel()==2){
					  deptId=userjob.getParentDept();
				  } else{
					  continue;
				 }
				 List<Long> reviewCompanyId = new ArrayList<Long>();
				 if(listCompany.size()==0){
					 /*如果部门没有审核配置，则使用默认审核配置*/
				   reviewCompanyId = reviewCompanyDao.findReviewCompanyList((long)0, flowType,(long)0);
				 } else{
				   reviewCompanyId = reviewCompanyDao.findReviewCompanyList(companyId, flowType,deptId);	 
				 }
				 if(reviewCompanyId.size()>0){
				      List<Integer> reviewList;	
				       reviewList =reviewRoleLevelDao.findReviewJobLevel(userjob.getJobId(),(long)reviewCompanyId.get(0));
				      if(reviewList.size() >0){
					     myUserJobList.add(userjob);
				       }
				  }	  
			  }	
		}
		ReviewCommonUtil.getUserJobsCostReviewCountByType(myUserJobList);
	return myUserJobList;		
	}
	
	/*提交成本审核,获得用户所在部门deptLevel=1 的部门列表 */
	public  List<Long>  getDeptList(long userId) {		 
		  List<UserJob> userJobList = new ArrayList<UserJob>();		 
		  userJobList=userJobDao.getUserJobList(userId);	 
		  List<Long> deptList=new ArrayList<Long>();
		  for (UserJob userjob : userJobList) {   
			  {
				 if(userjob.getDeptLevel()==1){
				  deptList.add(userjob.getDeptId());
			     }else if(userjob.getDeptLevel()==2){
				  deptList.add(userjob.getParentDept());
			    }	  
			 }
		  }
		  return deptList;
	}
	/**
	 * 新加方法 针对退款流程分为销售退款和计调退款
	 * @param flowTypes
	 * @return
	 */
	public  List<UserJob>  getWorkFlowJobByFlowType(List<Integer> flowTypes) {
		
		if(flowTypes == null || flowTypes.size() == 0) {
			return new ArrayList<UserJob>();
		}
		//循环查询各流程的职位
		List<UserJob> list = new ArrayList<UserJob>();
		for(Integer flowType : flowTypes){
			list.addAll(this.getWorkFlowJobByFlowType(flowType));
		}
		Set<Long> sets = new HashSet<Long>();
		List<UserJob> resultList = new ArrayList<UserJob>();
		//过滤重复的职位
		for(UserJob userJob : list){
			if(!sets.contains(userJob.getId())){
				sets.add(userJob.getId());
				resultList.add(userJob);
			}
			//装配不同职位当前审核状态的  审核条数 
			else{
				for( UserJob uj : resultList){
					if(userJob.getId().longValue() == uj.getId().longValue()){
						uj.setCount(uj.getCount() + userJob.getCount());
					}
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 说明：流程审核过程，一个用户会有多个角色涉及一个流程多个环节审核的情况，
	 * 次方就是根据Integer productType,Integer flowType  获取登录用户 流程相关的角色
	 * 
	 * @param productType
	 * @param flowType
	 * @return
	 */
	public  List<Role>  getWorkFlowRolesByFlowType(Integer productType,Integer flowType) {
			
		  return null;
		  
			//1.根据flowType 和 产品类型找出  review_flow 的ID
			// 由于有些流程是不区分产品类型的 如退款 是针对所有的产品的 并且产品类型是作为查询条件的 新增当产品类型为null时 不过滤这个查询条件 modify by chy 2014年12月15日15:36:10
			/*
		  long companyId = UserUtils.getUser().getCompany().getId();
		 
		  //获得用户部门列表
		  List<Long> deptList = new ArrayList<Long>();		  
		  deptList=UserUtils.getUser().getDeptList();
		  
		  List<Long> reviewCompanyId = new ArrayList<Long>();
		  if(productType == null) {
				reviewCompanyId = reviewCompanyDao.findReviewCompanyList(companyId, flowType,deptList);
			 } else {
				reviewCompanyId = reviewCompanyDao.findReviewCompanyList(companyId, productType, flowType,deptList);
			}
		  
//			 if (productType != null && reviewCompanyId.size() >1){
//				return null; //数据错误,应该只有1条记录
//			}
			
			List<Long> userRoleIDs= new ArrayList<Long>();
			userRoleIDs=UserUtils.getUser().getRoleIdList();
			
			List<Role> reviewRelatedRoles = new ArrayList<Role>();
			
			if(reviewCompanyId.size()>0){
				
				reviewRelatedRoles =reviewRoleLevelDao.findReviewRole(reviewCompanyId,userRoleIDs);
				
			}				
			 
			return reviewRelatedRoles;
			*/
		
		
		}
	
	//是否有财务成本录入审核 nowLevel 层级的审核权限 0无权限, 1有权限
	public  int  checkAuthentication(Integer nowLevel,Integer productType,Integer flowType) {
		List<Long> reviewCompanyId = new ArrayList<Long>();
		//reviewCompanyId ;// = reviewCompanyDao.findReviewCompany(productType, flowType);
		if(reviewCompanyId.size()!= 1)  return 0;
		
		List<Long> userRoleIDs= new ArrayList<Long>();
		userRoleIDs=UserUtils.getUser().getRoleIdList();		
		List<Long> roleIds= new ArrayList<Long>();
		roleIds =reviewRoleLevelDao.findReview(nowLevel,userRoleIDs.get(0),userRoleIDs);		
		
		if (roleIds.size()==0) return 0; 
		else return 1;		
	}
	
	//获得财务成本录入审核 topLevel,总共2层（财务审核，经理审核）直接返回 2
		public  int  getTopLevel(Integer productType,Integer flowType) {
			return 2;	
             /*
             List<Integer> reviewCompanyId = new ArrayList<Integer>();
			reviewCompanyId  = reviewCompanyDao.findTopLevel(productType, flowType);			
			if(reviewCompanyId.size()!= 1)  return 2;
		     else return reviewCompanyId.get(0);
			 */			
		}
		
		/**
		 * 根据userId 和  flowType获取游客的审核角色
		 * @param flowType
		 * @param userId
		 * @return
		 */
		private  List<UserJob>  getWorkFlowJobs4OneFlow(Integer flowType,long userId) {			
			  long companyId =UserUtils.getUser(userId).getCompany().getId();
			  List<UserJob> userJobList = new ArrayList<UserJob>();
			  List<UserJob> myUserJobList = new ArrayList<UserJob>();	  
			  userJobList=userJobDao.getUserJobList(userId);	 
			  for (UserJob userjob : userJobList) { 		  
				  Long deptId;
				  if(userjob.getDeptLevel()==1){
					  deptId=userjob.getDeptId();
				  }else if(userjob.getDeptLevel()==2){
					  deptId=userjob.getParentDept();
				  } else{
					  return null;
				 }
				 List<Long> reviewCompanyId = new ArrayList<Long>();
				 
				 reviewCompanyId = reviewCompanyDao.findReviewCompanyList(companyId, flowType,deptId);
				 
				  if(reviewCompanyId.size()>0){
				      List<Integer> reviewList;	
				       reviewList =reviewRoleLevelDao.findReviewJobLevel(userjob.getJobId(),(long)reviewCompanyId.get(0));
				      if(reviewList.size() >0){
				    	 myUserJobList.add(userjob);
				       }
				  }		  
			  }		  
			  return myUserJobList;
		}
		
		/**
		 * 根据各个审核角色的类型  和 审核人的name
		 * sys_job 职务表的职务类型jobType 有如下值 
		 * 
		 * 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		 * 7-部门经理 8-财务 9财务经理 10-总经理 0-其他
		 * 
		 * @param reviewLogs
		 * @return
		 */
		public Map<Integer,String> getReviewJobName(Integer flowType, List<ReviewLog> reviewLogs){
			Map<Integer, String> resmap = new HashMap<Integer, String>();
			List<UserJob> totalUserJobs = new ArrayList<UserJob>();
			for (ReviewLog reviewLog : reviewLogs) {
				 List<UserJob> myUserJobList = getWorkFlowJobs4OneFlow(flowType,reviewLog.getCreateBy());
				totalUserJobs.addAll(myUserJobList);
			}
			
			for (UserJob userJob : totalUserJobs) {
				resmap.put(userJob.getJobType(), UserUtils.getUser(userJob.getUserId()).getName());
			}
			return resmap;
		}	
			
}
