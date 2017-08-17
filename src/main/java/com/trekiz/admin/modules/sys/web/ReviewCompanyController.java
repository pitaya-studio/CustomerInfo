package com.trekiz.admin.modules.sys.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.airticketorder.service.AirTicketOrderLendMoneyService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.service.TransferMoneyService;
import com.trekiz.admin.modules.review.airticketreturn.service.IAirTicketReturnService;
import com.trekiz.admin.modules.review.changeprice.service.IChangePriceReviewService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.depositereview.service.IDepositeRefundReviewService;
import com.trekiz.admin.modules.review.deposittowarrantreview.service.DepositToWarrantReviewService;
import com.trekiz.admin.modules.review.refundreview.service.IAirTicketRefundReviewService;
import com.trekiz.admin.modules.review.visaborrowmoney.service.IVisaBorrowMoneyService;
import com.trekiz.admin.modules.review.visareturnreceipt.service.IVisaReturnReceiptService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewCompany;
import com.trekiz.admin.modules.reviewflow.entity.ReviewFlow;
import com.trekiz.admin.modules.reviewflow.entity.ReviewRoleLevel;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewFlowDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.SysJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.SysJobDao;
import com.trekiz.admin.modules.sys.service.ReviewCompanyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;


/**
 * 旅游产品信息控制器
 * @author zzy
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/review")
public class ReviewCompanyController extends BaseController{	
	
	@Autowired
	private  ReviewCompanyService  reviewCompanyService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private ReviewRoleLevelDao reviewRoleLevelDao;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private ReviewFlowDao reviewFlowDao;
	@Autowired
	private SysJobDao sysJobDao;
	@Autowired
	private  ReviewCommonService  reviewCommonService;
	@Autowired
	private DepositToWarrantReviewService depositToWarrantReviewService;
	@Autowired
	private OrderReviewService orderReviewService;
	@Autowired
    TransferMoneyService transferMoneyService;
	@Autowired
	private AirTicketOrderLendMoneyService airTicketOrderLendMoneyService;
	@Autowired
	private IChangePriceReviewService changePriceReviewService;
	@Autowired
	private IAirTicketRefundReviewService airTicketRefundReviewService;
	@Autowired
	private IAirTicketReturnService airticketReturnService;
	@Autowired
	private IDepositeRefundReviewService depositeRefundReviewService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private IVisaReturnReceiptService iVisaReturnReceiptService;
	@Autowired
	private IVisaBorrowMoneyService iVisaBorrowMoneyService;
	
	
	
	
	@RequiresPermissions("sys:review:edit")
	@RequestMapping(value={"reviewCompany"})
	public String list( Model model,HttpServletRequest request, HttpServletResponse response){
	Long companyId= UserUtils.getUser().getCompany().getId();
	    String deptId = request.getParameter("deptid");	
        if (companyId !=null) {
           if(deptId== null || deptId.equals("") ){
		     model.addAttribute("companyReviewList", reviewCompanyService.findCompanyReview(companyId.toString()));	
           }else{
             model.addAttribute("companyReviewList", reviewCompanyService.findCompanyReview(companyId.toString(),Long.parseLong(deptId)));	
             model.addAttribute("deptid",deptId);
           }
		model.addAttribute("deptList", reviewCompanyService.findDept(companyId));		
        }      
           
        return "modules/sys/reviewCompany";
	}
	
	@RequiresPermissions("sys:review:edit")
	@RequestMapping(value="delete")
	public String delete(@RequestParam(required=true)String id,Model model, HttpServletRequest request)  {
		reviewCompanyService.deleteReview(id);
	    return "redirect:"+Global.getAdminPath()+"/sys/review/reviewCompany";
	}
	
	@RequiresPermissions("sys:review:edit")
	@RequestMapping(value="deleteRole")
	public String deleteRole(@RequestParam(required=true)Long id,@RequestParam(required=true)Long reviewcompanyid, Model model, HttpServletRequest request)  {

		ReviewCompany reviewCompany = reviewCompanyDao.findOne(reviewcompanyid);
		int flowId= reviewCompany.getReviewFlowId();		
		ReviewRoleLevel reviewRoleLevel =reviewRoleLevelDao.findOne(id);
		int nowLevel=reviewRoleLevel.getReviewLevel();
		Long jobId=reviewRoleLevel.getJobId();
		SysJob sysJob=sysJobDao.findOne(jobId);
		
		String sql="";
		//删除审核时，更新对应的业务数据为审核通过
		if(sysJob!=null && sysJob.getDelFlag().equals("0") && (reviewRoleLevel.getIsEnd()==1 )){	
			int orderType=sysJob.getOrderType();
			if (flowId==15){ //成本审核
				sql="update cost_record set review=2 where reviewcompanyid="+reviewcompanyid +" and review=1 and orderType="+orderType+" and nowlevel="+nowLevel;			
			}else if(flowId==18){ //付款审核
				sql="update cost_record set payreview=2 where  payreviewcompanyid="+reviewcompanyid +" and payreview=1 and orderType="+orderType+" and paynowlevel="+nowLevel;	
			}else{ //其它审核
				sql="update review set status=2 where review_company_id="+reviewcompanyid +" and status=1 and productType="+orderType+" and nowlevel="+nowLevel;	
			    
				List<Review> reviewList=reviewDao.findReviewForPass(reviewcompanyid, orderType, nowLevel);
				for(Review review: reviewList){
					if(flowId==1){//退款流程
						airTicketRefundReviewService.doRefund(review);
						reviewService.updateCostRecordStatus(review,"审核通过");

					}else if(flowId==2){//发票流程
						
					}else if(flowId==3){//退票流程
						airticketReturnService.doReturnPosition(review);
					}else if(flowId==4){//还签证收据
						iVisaReturnReceiptService.visaReturnReceiptPostReviewPassedTackle4HQX(review);
					}else if(flowId==5){//签证借款流程
						iVisaBorrowMoneyService.visaBorrowMoneyPostReviewPassedTackle4HQX(review);
					}else if(flowId==6){//签证押金转担保
						depositToWarrantReviewService.updateWarrantTypeReview(review);
					}else if(flowId==7){//退签证押金
						depositeRefundReviewService.doDepositeRefund(review);
					}else if(flowId==8){//退团流程
						try {
							sql="update review set status=3 where review_company_id="+reviewcompanyid +" and status=1 and productType="+orderType+" and nowlevel="+nowLevel;	
							orderReviewService.exitGroup(Long.parseLong(review.getOrderId()), review.getId(), review.getTravelerId(), request);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(flowId==9){//返佣流程
						reviewService.updateCostRecordStatus(review,"审核通过");

					}else if(flowId==10){//改价流程
						changePriceReviewService.doChangePrice(review);
					}else if(flowId==11){//转团流程
						sql="update review set status=3 where review_company_id="+reviewcompanyid +" and status=1 and productType="+orderType+" and nowlevel="+nowLevel;	
						Map<String ,String> r = reviewService.findReview(review.getId());
						String newGroupCode = r.get("newGroupCode");
						ActivityGroup newGroup =  activityGroupService.findByGroupCode(newGroupCode);
						try{
						Map<String,Object> map = orderReviewService.changeGroupSuccess(Long.parseLong(r.get("oldOrderId")), review.getTravelerId(), newGroup.getId(),review.getId(), request);
						if(map.get("res").toString().equals("0")){
							ProductOrderCommon order = (ProductOrderCommon)map.get("newOrder");
							if(null != order && 0 < order.getId()){
								List<Detail>ls = new ArrayList<Detail>();
								ls.add(new Detail("newOrderId",order.getId()+""));
								reviewService.addReviewDetail(review.getId(),ls);
							}
							orderService.updateTravelerStatus(Context.TRAVELER_DELFLAG_TURNROUNDED, review.getTravelerId());
							reviewService.reviewOperationDone(review.getId(), Context.REFUND_STATUS_DONE);
						}else{	
							throw new Exception(map.get("message").toString());
							
						}				
						}catch(Exception e){
							e.printStackTrace();
						}
						
					}else if(flowId==12){//转团转款
						sql="update review set status=2 where review_company_id="+reviewcompanyid +" and status=1 and productType="+orderType+" and nowlevel="+nowLevel;	
						String rl = transferMoneyService.transferMoneyApplyDone(review.getId());
						if("success".equals(rl)){
							reviewService.reviewOperationDone(review.getId(), Context.REFUND_STATUS_DONE);
						}
					}else if(flowId==13){//还签证押金收据
						
					}else if(flowId==14){//改签流程						
						try {
							airTicketOrderService.planeReview(Long.valueOf(review.getOrderId()), Long.valueOf(review.getId()), Long.valueOf(review.getTravelerId()));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if(flowId==16){//计调退款流程
						airTicketRefundReviewService.doRefund(review);
					}else if(flowId==19){//借款审核
						sql="update review set status=2 where review_company_id="+reviewcompanyid +" and status=1 and productType="+orderType+" and nowlevel="+nowLevel;	
						airTicketOrderLendMoneyService.saveLendMoney2MoneyAmount(review.getId(), review.getOrderId(), review.getProductType());
					}else if(flowId==20){//新行者借款
						iVisaBorrowMoneyService.visaBorrowMoneyPostReviewPassedTackle4XXZ(review);
					}
				}
			}					
			reviewCompanyService.updateSql(sql);
			
		}	
		reviewCompanyService.deleteReviewRole(id);
		
	    return "redirect:"+Global.getAdminPath()+"/sys/review/reviewRole?reviewcompanyid="+ reviewcompanyid;
	}
	
	@RequiresPermissions("sys:review:edit")
	@RequestMapping(value={"reviewRole"})
	public String roleReviewList(@RequestParam(required=true)String reviewcompanyid, Model model, HttpServletRequest request){
	
		List<Map<String, Object>> roleList= reviewCompanyService.findReviewRoleList(reviewcompanyid);
		if(roleList.size()>=1){			
			//model.addAttribute("productName",roleList.get(0).get("productName"));			
			model.addAttribute("flowName", roleList.get(0).get("flowName"));
			model.addAttribute("deptName",roleList.get(0).get("deptName"));
			model.addAttribute("topLevel",roleList.get(0).get("topLevel"));
		}
		model.addAttribute("reviewcompanyid", reviewcompanyid);
		model.addAttribute("reviewList", reviewCompanyService.findReviewRoleList(reviewcompanyid));
		
		 return "modules/sys/reviewRole";
	}
	
	//修改时回显
	@RequestMapping(value = "reviewRoleEdit")
	public String reviewRoleEdit(@RequestParam(required=false) Long id,@RequestParam(required=true) Long reviewcompanyid,Model model) {
		Long companyId= UserUtils.getUser().getCompany().getId();
		
        
        model.addAttribute("roleList", reviewCompanyService.findRoleList(companyId.toString()));
        if(id !=null){
    	   ReviewRoleLevel reviewRoleLevel=reviewRoleLevelDao.findOne(id);
           model.addAttribute("reviewRoleLevel",reviewRoleLevel);
           model.addAttribute("id",id);
           ReviewCompany reviewCompany=reviewCompanyDao.findOne(reviewRoleLevel.getReviewCompanyId());
           model.addAttribute("topLevel",reviewCompany.getTopLevel());
       } else{
    	   model.addAttribute("id",0);
    	   model.addAttribute("topLevel",0);
       }
      
       model.addAttribute("topLevel",reviewCompanyDao.findOne(reviewcompanyid).getTopLevel());
       model.addAttribute("reviewcompanyid",reviewcompanyid); 
      return "modules/sys/reviewRoleEdit";
	}
	
	
	//修改时回显
	/*
	@RequestMapping(value = "reviewRoleEdit")
	public String roleEdit(@RequestParam(required=false) Long id,@RequestParam(required=true) Long reviewcompanyid,Model model) {
		Long companyId= UserUtils.getUser().getCompany().getId();
		
        
        model.addAttribute("roleList", reviewCompanyService.findRoleList(companyId.toString()));
        if(id !=null){
    	   ReviewRoleLevel reviewRoleLevel=reviewRoleLevelDao.findOne(id);
           model.addAttribute("reviewRoleLevel",reviewRoleLevel);
       } 
       model.addAttribute("topLevel",reviewCompanyDao.findOne(reviewcompanyid).getTopLevel());
       model.addAttribute("reviewcompanyid",reviewcompanyid); 
      return "modules/sys/reviewRoleEdit";
	} */
	
	
	@RequiresPermissions("sys:review:edit")
	@RequestMapping(value = "roleReviewSave")
	public String save( @RequestParam(required=true) Long roleLevelId,  @RequestParam(required=true) Long roleList,
				@RequestParam(required=true) Long reviewcompanyid,
				@RequestParam(required=true) Integer end,
				@RequestParam(required=true) Integer reviewLevel,Model model) {

		       if(roleLevelId !=null && roleLevelId > 0){		    	  
		    	   ReviewRoleLevel reviewRoleLevel=reviewRoleLevelDao.findOne(roleLevelId);
		    	   reviewRoleLevel.setJobId(roleList);
		    	   reviewRoleLevel.setReviewLevel(reviewLevel);
		    	   reviewRoleLevel.setIsEnd(end);
		    	   reviewRoleLevelDao.save(reviewRoleLevel);
		       }else{		    	   
		    	   ReviewRoleLevel reviewRoleLevel= new ReviewRoleLevel();
		    	   reviewRoleLevel.setJobId(roleList);
		    	   reviewRoleLevel.setReviewLevel(reviewLevel);
		    	   reviewRoleLevel.setReviewCompanyId(reviewcompanyid);
		    	   reviewRoleLevel.setIsEnd(end);
		    	   reviewRoleLevel.setDelFlag("0");
		    	   reviewRoleLevelDao.save(reviewRoleLevel);
		       }
			return "redirect:"+Global.getAdminPath()+"/sys/review/reviewRole?reviewcompanyid="+reviewcompanyid;
		}
		
	
	@RequestMapping(value = "reviewCompanyEdit")
	public String roleCompanyEdit(@RequestParam(required=false) Long reviewcompanyid,Model model) {
			Long companyId= UserUtils.getUser().getCompany().getId();			
			
	       if(reviewcompanyid !=null){
	    	 ReviewCompany reviewCompany = reviewCompanyDao.findOne(reviewcompanyid);
	    	   //ReviewCompany reviewCompany = 
	    		//List<Map<String, Object>> company=reviewCompanyService.findCompanyDept(reviewcompanyid);
	    		//if(company.size()==1){
	    	       model.addAttribute("reviewCompany",reviewCompany);
	    	   
	    	       ReviewFlow reviewFlow= reviewFlowDao.findOne(reviewCompany.getReviewFlowId());
	    	     
	    	       model.addAttribute("flowName",reviewFlow.getFlowName()); 
	    	       model.addAttribute("reviewcompanyid",reviewcompanyid); 
	    	       Department dept= departmentDao.findOne(reviewCompany.getDeptId());
	    	       model.addAttribute("deptName",dept.getName()); 
	    	      
	    		//}
	       }else{
	    	   model.addAttribute("reviewFlow", reviewCompanyService.findReviewFlow());	
	    	   model.addAttribute("deptList", reviewCompanyService.findDept(companyId));	
	       }
	       model.addAttribute("companyId",companyId); 
	      
	      return "modules/sys/reviewCompanyEdit";
		}
		
	    @RequiresPermissions("sys:review:edit")
		@RequestMapping(value = "reviewCompanySave")
		public String reviewCompanySave( @RequestParam(required=false) Integer roleList, @RequestParam(required=false) Long deptid,  @RequestParam(required=true) Integer topLevel,
						@RequestParam(required=true) Long reviewcompanyid, @RequestParam(required=true) Integer redo,Model model) {
					
				       if(reviewcompanyid !=null){		    	  
				    	   ReviewCompany  reviewCompany=reviewCompanyDao.findOne(reviewcompanyid);
				    	   reviewCompany.setTopLevel(topLevel);	
				    	   reviewCompany.setRedo(redo);
				    	   reviewCompanyDao.save(reviewCompany);
				    	   
				       }else{		    	   
				    	   ReviewCompany  reviewCompany= new  ReviewCompany ();
				    	   reviewCompany.setTopLevel(topLevel);	
				    	   reviewCompany.setReviewFlowId(roleList);	
				    	   reviewCompany.setDeptId(deptid);	
				    	   reviewCompany.setRedo(redo);
				    	   reviewCompany.setDelFlag("0");
				    	   reviewCompany.setForces(1);
				    	   Long companyId= UserUtils.getUser().getCompany().getId();
				    	   reviewCompany.setCompanyId(companyId);
				    	   List<Map<String, Object>> checkReviewCompany=reviewCompanyService.checkReviewCompany(companyId,roleList,deptid);
						   if(checkReviewCompany.size()==0) {
				    		   reviewCompanyDao.save(reviewCompany);				    		  
				    		   return "redirect:"+Global.getAdminPath()+"/sys/review/reviewRole?reviewcompanyid="+reviewCompany.getId();
				    	   }
				       }
					return "redirect:"+Global.getAdminPath()+"/sys/review/reviewCompany";
		}
				
		
		@ResponseBody		
		@RequestMapping(value = "checkReviewCompany")
		public String checkReviewCompany(@RequestParam(required=true) Integer reviewFlowId,@RequestParam(required=true) Long deptid,Model model) {
					Long companyId= UserUtils.getUser().getCompany().getId();	
					List<Map<String, Object>> checkReviewCompany=reviewCompanyService.checkReviewCompany(companyId, reviewFlowId,deptid);
			       return ""+checkReviewCompany.size();			      
			     
		}

		@ResponseBody		
		@RequestMapping(value = "checkReviewJob")
		public String checkReviewJob(@RequestParam(required=true) Integer jobid,@RequestParam(required=true) Long  reviewcompanyid,@RequestParam(required=true) Long  id,Model model) {
				
			    //System.out.println(reviewcompanyid);
				 
				 List<Map<String, Object>> checkReviewJob=reviewCompanyService.checkReviewJob( reviewcompanyid,jobid,id);
				   //System.out.println(checkReviewJob.size());
				 return ""+checkReviewJob.size();			      
			     
		}
}
