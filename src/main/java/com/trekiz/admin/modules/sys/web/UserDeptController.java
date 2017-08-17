package com.trekiz.admin.modules.sys.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewFlowDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserDeptJob;
import com.trekiz.admin.modules.sys.entity.UserDeptJobNew;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.repository.UserDeptDao;
import com.trekiz.admin.modules.sys.repository.UserDeptJobDao;
import com.trekiz.admin.modules.sys.repository.UserDeptJobNewDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.ReviewCompanyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 旅游产品信息控制器
 * @author zzy
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/userDept")
public class UserDeptController extends BaseController{	
	
	@Autowired
	private  ReviewCompanyService  reviewCompanyService;
	@Autowired
	private ReviewRoleLevelDao reviewRoleLevelDao;
	@Autowired
	private UserDeptDao userDeptDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserDeptJobDao userDeptJobDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private ReviewFlowDao reviewFlowDao;
	@Autowired
	private  ReviewCommonService  reviewCommonService;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired 
	private UserDeptJobNewDao userDeptJobNewDao;
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value={"deptList"})
	public String list(@RequestParam(required=true)Long userid, Model model,HttpServletRequest request, HttpServletResponse response){
		User user=new User();
		user=userDao.findById(userid);
		/*
		Long companyId= UserUtils.getUser().getCompany().getId();			 
		if( user==null ||  ! user.getCompany().getId().equals(companyId)){
			model.addAttribute("userid","");
			model.addAttribute("username","");
			return null;
		}   */
		model.addAttribute("userid",user.getId());
		model.addAttribute("username",user.getName());
        model.addAttribute("deptList", reviewCompanyService.findDeptJob(userid));	
        model.addAttribute("userid",userid);      
      
        return "modules/sys/userDept";
	}
	
	@RequestMapping(value = "userDeptEdit")
	public String userDeptEdit(@RequestParam(required=true) Long userid, Model model) {					
		 Long companyid= UserUtils.getUser().getCompany().getId();	
	     model.addAttribute("deptList", reviewCompanyService.findReviewDept(companyid));	
	     model.addAttribute("jobList", reviewCompanyService.findJobList(companyid));	
	     model.addAttribute("companyid",companyid); 
	     model.addAttribute("userid",userid); 	     
	      return "modules/sys/userDeptEdit";
		}
		
	    @RequiresPermissions("sys:user:view")
		@RequestMapping(value = "userDeptSave")
		public String userDeptSave( @RequestParam(required=true) Long userid, @RequestParam(required=true) Long deptid, @RequestParam(required=true) Long jobid,  Model model) {
	    	/* UserDeptJob  userDeptJob= new UserDeptJob ();			
			 userDeptJob.setUserId(userid);
			 userDeptJob.setDeptId(deptid);
			 userDeptJob.setJobId(jobid);
			 userDeptJob.setDelFlag("0");*/
	    	
	    	User byId = userDao.getById(userid);
	    	Office company = byId.getCompany();
	    	
			 UserDeptJobNew userDeptJobNew = new UserDeptJobNew();
			 userDeptJobNew.setUser_id(userid);
			 userDeptJobNew.setDept_id(deptid);
			 userDeptJobNew.setJob_id(jobid);
			 userDeptJobNew.setCompany_uuid(company.getUuid());
			 userDeptJobNew.setDelFlag(0);
			 userDeptJobNew.setCreate_date(new Date());
			 userDeptJobNew.setCreate_by(UserUtils.getUser().getId().toString());
			 
			 List<Map<String, Object>> checkReviewCompany=reviewCompanyService.checkUserDeptJob(userid,deptid,jobid);
			 
			/* if(checkReviewCompany.size()==0) {
			   userDeptJobDao.save(userDeptJobNew);	
			 }*/
			 if(checkReviewCompany.size()==0){
				userDeptJobNewDao.save(userDeptJobNew) ;
			 }
			 
			return "redirect:"+Global.getAdminPath()+"/sys/userDept/deptList?userid="+userid;			    	  
		}
				
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value="delete")
	public String delete(@RequestParam(required=true)Long id,@RequestParam(required=true)Long userid,Model model, HttpServletRequest request)  {
		reviewCompanyService.deleteUserDeptJob(id,userid);		
		Long companyid= UserUtils.getUser().getCompany().getId();
	    return "redirect:"+Global.getAdminPath()+"/sys/userDept/deptList?userid="+userid+"&companyid="+ companyid;
	}
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value="deleteRole")
	public String deleteRole(@RequestParam(required=true)Long id,@RequestParam(required=true)String reviewcompanyid, Model model, HttpServletRequest request)  {
		reviewCompanyService.deleteReviewRole(id);
	    return "redirect:"+Global.getAdminPath()+"/sys/review/reviewRole?reviewcompanyid="+ reviewcompanyid;
	}
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value={"userJob"})
	public String userJobList(@RequestParam(required=true)Long userdeptid,@RequestParam(required=true)Long userid, Model model, HttpServletRequest request){
	
		List<Map<String, Object>> jobList= reviewCompanyService.findUserJobList(userdeptid);
		if(jobList.size()>=1){			
			//model.addAttribute("productName",roleList.get(0).get("productName"));			
			//model.addAttribute("flowName", roleList.get(0).get("flowName"));
			//model.addAttribute("deptName",roleList.get(0).get("deptName"));
		}
		model.addAttribute("userdeptid", userdeptid);			
		model.addAttribute("userid", userid);	 
		model.addAttribute("jobList", jobList);
		Long companyid= UserUtils.getUser().getCompany().getId();
		model.addAttribute("companyid", companyid);
		
		 return "modules/sys/userJob";
	}
	
	
	//修改时回显
	@RequestMapping(value = "userJobEdit")
	public String userJobEdit(@RequestParam(required=true) Long userdeptid,@RequestParam(required=true) Long userid,Model model) {
		//Long companyId= UserUtils.getUser().getCompany().getId();
	   model.addAttribute("jobList", reviewCompanyService.findJobList());
       model.addAttribute("userdeptid",userdeptid);  
       model.addAttribute("userid",userid); 
       return "modules/sys/userJobEdit";
	}
	
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "userJobSave") 
	public String save( @RequestParam(required=true) Long userdeptid,  @RequestParam(required=true) Long jobid,@RequestParam(required=true) Long userid,
			    Model model) {
			
		      	   UserDeptJob userDeptJob  = new  UserDeptJob();	 
		      	   userDeptJob.setJobId(jobid);
		      	   userDeptJob.setDeptId(userdeptid);
		      	   userDeptJob.setDelFlag("0");
		      	   userDeptJobDao.save( userDeptJob);
		      
			return "redirect:"+Global.getAdminPath()+"/sys/userDept/userJob?userdeptid="+userdeptid+"&userid="+userid;
		}
		
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value="deleteJob")
	public String deleteJob(@RequestParam(required=true)Long userdeptid,@RequestParam(required=true)Long jobid,@RequestParam(required=true)Long userid,Model model, HttpServletRequest request)  {
		reviewCompanyService.deleteUserJob(userdeptid,jobid);		
		//Long companyid= UserUtils.getUser().getCompany().getId();
	    return "redirect:"+Global.getAdminPath()+"/sys/userDept/userJob?userdeptid="+userid+"&userid="+ userid;
	}
	
			
		@ResponseBody		
		@RequestMapping(value = "checkUserDeptJob")
		public String checkUserDeptJob(@RequestParam(required=true) Long userid,@RequestParam(required=true) Long deptid,@RequestParam(required=true) Long jobid,Model model) {
					//Long companyId= UserUtils.getUser().getCompany().getId();	
					List<Map<String, Object>> checkReviewCompany=reviewCompanyService.checkUserDeptJob(userid,deptid,jobid);
				    return ""+checkReviewCompany.size();			      
			     
		}

		@ResponseBody		
		@RequestMapping(value = "checkReviewJob")
		public String checkUserJob(@RequestParam(required=true) Long userdeptid,@RequestParam(required=true) Long jobid,Model model) {
				List<Map<String, Object>> checkReviewJob=reviewCompanyService.checkUserJob( userdeptid,jobid);
				return ""+checkReviewJob.size();			      
			     
		}
}
