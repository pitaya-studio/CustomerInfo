package com.trekiz.admin.modules.order.rebates.web;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.order.entity.OrderExitGroupReviewVO;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.rebates.entity.Rebates;
import com.trekiz.admin.modules.order.rebates.service.RebatesService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewLogService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 
 *  文件名: RebatesReviewController.java
 *  功能:返佣审核
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-12-24 下午3:27:27 
 *  @version 1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/order/rebates/review")
public class RebatesReviewController extends BaseController {
	
	public static final Integer REBATES_FLOW_TYPE = 9;
	@Autowired
	private RebatesService rebatesService;
	@Autowired
    private OrderCommonService orderService;
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	private OrderReviewService orderReviewService;
	@Autowired
    private SystemService systemService;
	@Autowired
	private ReviewLogService reviewLogService;
	private static Logger logger = LoggerFactory.getLogger(RebatesReviewController.class);
	/**
	 * 跳转到返佣列表
	 * @param model
	 * @param request
	 * @return
	 */
//	@RequestMapping(value ="showRebatesReviewList")
//	public String showRebatesReviewList(@ModelAttribute RebatesReviewCond vo, Model model, HttpServletRequest request, HttpServletResponse response){
//		List<Role> roleList = reviewCommonService.getWorkFlowRolesByFlowType(null, REBATES_FLOW_TYPE);
//		if(null != roleList && roleList.size() > 0){
//			List<Map<String,Object>> ls = orderReviewService.getUsersReviewOrderTypeList(roleList, REBATES_FLOW_TYPE);
//			model.addAttribute("productList",ls);
//			
//			Integer productType=Integer.parseInt(ls.get(0).get("productType").toString());
//			if(StringUtils.isBlank(vo.getOrderType())){
//				vo.setOrderType(productType+"");
//			}
//			List<Role> listRole=reviewCommonService.getWorkFlowRolesByFlowType(Integer.parseInt(vo.getOrderType()), REBATES_FLOW_TYPE);
//			if(vo.getRid()==0){								
//				vo.setRid(listRole.get(0).getId());
//			}else{
//				boolean flag=false;
//				for(Role r:listRole){
//					if(r.getId().longValue()==vo.getRid()){
//						flag=true;
//					}
//				}
//				if(!flag){
//					vo.setRid(listRole.get(0).getId());
//				}
//			}
//			List<Integer>levelList = reviewService.getRoleLevel(vo.getRid(), Integer.parseInt(vo.getOrderType()), REBATES_FLOW_TYPE);
//			vo.setUserLevel(levelList.get(0));
//			Page<Map<Object, Object>> pageInfo = rebatesService.getRebatesReviewList(new Page<Map<Object, Object>>(request,response), vo);
//			model.addAttribute("page", pageInfo);
//			model.addAttribute("roleList",listRole);
//		}
//		model.addAttribute("reviewVO", vo);
//		return "modules/order/rebates/review/rebatesReviewList";
//	}
	
	/**
	 * add by zhangcl
	 * 跳转到返佣列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="showRebatesReviewList")
	public String showRebatesReviewList(@ModelAttribute OrderExitGroupReviewVO vo, Model model, HttpServletRequest request, HttpServletResponse response){
		//获取部门职务列表		
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REBATES_FLOW_TYPE);
		String flag = request.getParameter("flag");
		Set<Integer> set = Sets.newHashSet();
		List <UserJob> userJobList = Lists.newArrayList();
		Page<Map<Object, Object>> pageInfo = null;
		String returnStr = "modules/order/rebates/review/rebatesReviewList";
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		if(null != userJobs && 0 < userJobs.size()){
			if(!StringUtils.isNotBlank(vo.getOrderType())){
				for( UserJob uj : userJobs){
					set.add(uj.getOrderType());
				}
				vo.setOrderType(set.toArray()[0].toString());
				
			}
			for( UserJob uj : userJobs){
				if(uj.getOrderType().toString().equals(vo.getOrderType())){
					userJobList.add(uj);
				}
			}
			if(null !=vo.getOrderType() && vo.getOrderType().equals("6")){
				return "redirect:" + Global.getAdminPath() + "/review/visaRebates/list";
			}else if(null !=vo.getOrderType() && vo.getOrderType().equals("7")){
				if("1".equals(flag)){//add by wangXK
					return "redirect:" + Global.getAdminPath() + "/airticketRebates/queryAirticketRebatesList";
				}else{
					return "redirect:" + Global.getAdminPath() + "/airticketRebates/getAirticketRebatesList";
				}
				//return "redirect:" + Global.getAdminPath() + "/airticketRebates/getAirticketRebatesList";
			}else if(null !=vo.getOrderType() && vo.getOrderType().equals("11")){
				pageInfo = orderReviewService.getHotelRebatesReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
			}else if(null !=vo.getOrderType() && vo.getOrderType().equals("12")){
				pageInfo = orderReviewService.getIslandRebatesReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
			}else {
				pageInfo = orderReviewService.getRebatesReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
			}
				
		}else if(null !=vo.getOrderType() && vo.getOrderType().equals("6")){
			 return "redirect:" + Global.getAdminPath() + "/review/visaRebates/list";
		}else if(null !=vo.getOrderType() && vo.getOrderType().equals("7")){
			if("1".equals(flag)){
				return "redirect:" + Global.getAdminPath() + "/airticketRebates/queryAirticketRebatesList";
			}else{
				return "redirect:" + Global.getAdminPath() + "/airticketRebates/getAirticketRebatesList";
			}
			 
		}else if(null !=vo.getOrderType() && vo.getOrderType().equals("11")){
			pageInfo = orderReviewService.getHotelRebatesReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
		}else if(null !=vo.getOrderType() && vo.getOrderType().equals("12")){
			pageInfo = orderReviewService.getIslandRebatesReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
		}
		
		//获取页面上需要展示的字段信息
		if(pageInfo != null &&pageInfo.getList() != null &&pageInfo.getList().size()>0){
			for(Map<Object,Object> map : pageInfo.getList()){
				Rebates rebates = null;
				if(null != map.get("rid") ){
//					rebates = rebatesService.findRebatesByRid(Long.parseLong(map.get("rid").toString()));
					//暂时修改，如意继续
					List<Rebates> rebatesList = rebatesService.findRebatesListByRid(Long.parseLong(map.get("rid").toString()));
					if (CollectionUtils.isNotEmpty(rebatesList)) {
						rebates = rebatesList.get(0);
					}
					if( null != rebates){
						map.put("id", rebates.getId());				//获取order_rebates表主键ID
						map.put("costname", rebates.getCostname());	//获取款项
//						String rebatesDiff=OrderCommonUtil.getMoneyAmountBySerialNum(rebates.getNewRebates(), 1);
						map.put("rebatesDiff", rebates.getRebatesDiff());//获取返佣差额
						map.put("currencyName", rebates.getCurrency().getCurrencyName());
						if(null !=vo.getOrderType()){
							int orderType = Integer.parseInt(vo.getOrderType());
							if(6 > orderType || 10 == orderType){
								ProductOrderCommon productOrder = orderService.getProductorderById(rebates.getOrderId());
								map.put("groupid", productOrder.getProductGroupId());
								map.put("orderStatus", productOrder.getOrderStatus());
								map.put("totalMoney", OrderCommonUtil.getMoneyAmountBySerialNum(productOrder.getTotalMoney(),2));
								map.put("payedMoney", OrderCommonUtil.getMoneyAmountBySerialNum(productOrder.getPayedMoney(),2));
								map.put("accountedMoney", OrderCommonUtil.getMoneyAmountBySerialNum(productOrder.getAccountedMoney(),2));
								if(null != map.get("travelerId")){
									map.get("travelerId").toString();
								}
								String rebatesStr = OrderCommonUtil.getAllRebatesMoney(productOrder.getId(), orderType);
								if(null == rebatesStr || 0 == rebatesStr.trim().length()){
									map.put("rebatesSign", 0);
								}else{
									map.put("rebatesSign", 1);
								}
								map.put("rebatesStr", rebatesStr);
							}							
						}						
					}
				}				
			}
		}
		model.addAttribute("flag", flag);
		model.addAttribute("page", pageInfo);
		model.addAttribute("userJobs", userJobList);
		model.addAttribute("reviewVO", vo);
		Map<String,String>printMap  = new LinkedHashMap<String,String>();
		printMap.put("1", "已打印");
		printMap.put("2", "未打印");
		model.addAttribute("printMap", printMap);
		//add by jiangyang
		model.addAttribute("users",systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
				
		return returnStr;
	}
	
	/**
	 * 跳转到返佣详情界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="showRebatesReviewDetail/{rebatesId}")
	public String showRebatesReviewDetail(@PathVariable Long rebatesId, Model model, HttpServletRequest request){
		Rebates rebates = rebatesService.findRebatesById(rebatesId);
		ProductOrderCommon productOrder = orderService.getProductorderById(rebates.getOrderId());
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("rebates", rebates);
		model.addAttribute("rid", rebates.getRid());
		List<ReviewLog> reviewLogList =  reviewService.findReviewLog(rebates.getReview().getId());
		model.addAttribute("reviewLogList", reviewLogList);
		String travelerId = null;
		if(null != rebates.getTravelerId() && 0 < rebates.getTravelerId()){
			travelerId = rebates.getTravelerId().toString();
		}
		String rebatesStr = OrderCommonUtil.getRebatesMoney(productOrder.getId(), productOrder.getOrderStatus(), travelerId);
		if(null == rebatesStr || 0 == rebatesStr.trim().length()){
			model.addAttribute("rebatesSign", 0);
		}else{
			model.addAttribute("rebatesSign", 1);
		}
		model.addAttribute("rebatesStr", rebatesStr);
		return "modules/order/rebates/review/rebatesReviewDetail";
	}
	
	/**
	 * 跳转到返佣审核界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="showRebatesReview/{rebatesId}/{userLevel}")
	public String showRebatesReview(@PathVariable Long rebatesId,@PathVariable Integer userLevel, Model model, HttpServletRequest request){
		Rebates rebates = rebatesService.findRebatesById(rebatesId);
		ProductOrderCommon productOrder = orderService.getProductorderById(rebates.getOrderId());
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("rebates", rebates);
		model.addAttribute("rid", rebates.getRid());
		List<ReviewLog> reviewLogList =  reviewService.findReviewLog(rebates.getReview().getId());
		model.addAttribute("reviewLogList", reviewLogList);
		model.addAttribute("userLevel", userLevel);
		String travelerId = null;
		if(null != rebates.getTravelerId() && 0 < rebates.getTravelerId()){
			travelerId = rebates.getTravelerId().toString();
		}
		String rebatesStr = OrderCommonUtil.getRebatesMoney(productOrder.getId(), productOrder.getOrderStatus(), travelerId);
		if(null == rebatesStr || 0 == rebatesStr.trim().length()){
			model.addAttribute("rebatesSign", 0);
		}else{
			model.addAttribute("rebatesSign", 1);
		}
		model.addAttribute("rebatesStr", rebatesStr);
		
		return "modules/order/rebates/review/rebatesReview";
	}
	
	/**
	 * 审核返佣信息
	 * @param rid
	 * @param result
	 * @param denyReason
	 * @param userLevel
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="reviewRebates")
	public String reviewRebates(long rid, Integer result, String denyReason, Integer userLevel, Model model, HttpServletRequest request){
		try {
			rebatesService.reviewRebates(rid, result, denyReason, userLevel);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "error";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
	}
	
	/**
	 * 批量审核返佣信息
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="batchReviewRebates")
	public Map<String,Object> batchReviewRebates(HttpServletResponse response,  Model model, HttpServletRequest request){
		String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		String userLevel = request.getParameter("userLevel"); // 当前用户审核等级
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		int num = 0;
		Map<String, Object> back =new HashMap<String,Object>();
		StringBuffer reply = new StringBuffer();
		if (result == null || "".equals(result)) {
			reply.append("审批结果不能为空");
			back.put("res", "error");
			back.put("msg", "审批结果不能为空");
			return back;
		}
		for (int i = 0; i < levelandrevids.length; i++) {
			String revid = levelandrevids[i].split("@")[1];
			try {
				if("1".equals(result)){
					rebatesService.reviewRebates(Long.valueOf(revid), Integer.valueOf(result), "", Integer.valueOf(userLevel));
				}else{
					rebatesService.reviewRebates(Long.valueOf(revid), Integer.valueOf(result), remarks, Integer.valueOf(userLevel));
				}

				ReviewLog reviewLog = new ReviewLog();
				reviewLog.setReviewId(Long.valueOf(revid));
				reviewLog.setNowLevel(Integer.valueOf(userLevel));
				reviewLog.setResult(result);
				reviewLog.setRemark(remarks);
				reviewLogService.saveReviewLog(reviewLog);
				num++;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error("批量审核返佣申请：审核ID为："+revid+"；  报错原因："+e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("批量审核返佣申请：审核ID为："+revid+"；  报错原因："+e.getMessage());
			}
		}
		back.put("res", "success");
		back.put("msg", "共有"+num+"条记录操作成功。");
		return back;
	}
	
	/**
	 * 机票审核返佣信息
	 * @param rid
	 * @param result
	 * @param denyReason
	 * @param userLevel
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="airticketReviewRebates")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String airticketReviewRebates(long rid, Integer result, String denyReason, Integer userLevel, Model model, HttpServletRequest request){
		
		try {
			
			int reviewResult = reviewService.UpdateReview(rid, userLevel, result, denyReason);
			Map<String,String> map = reviewService.findReview(rid);
			//审核成功状态
			if(reviewResult == 1){
				
				rebatesService.reviewSuccess(map);
			
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "error";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
	}

	/**
	 * 机票批量审核返佣信息
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="batchAirticketReviewRebates")
	public Map<String,Object> batchAirticketReviewRebates(HttpServletResponse response,  Model model, HttpServletRequest request){
		String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		String userLevel = request.getParameter("userLevel"); // 当前用户审核等级
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		int num = 0;
		Map<String, Object> back =new HashMap<String,Object>();
		StringBuffer reply = new StringBuffer();
		if (result == null || "".equals(result)) {
			reply.append("审批结果不能为空");
			back.put("res", "error");
			back.put("msg", "审批结果不能为空");
			return back;
		}
		for (int i = 0; i < levelandrevids.length; i++) {
			String revid = levelandrevids[i].split("@")[1];
			int reviewResult =0;
			try {
				if("1".equals(result)){
					reviewResult = reviewService.UpdateReview(Long.valueOf(revid), Integer.valueOf(userLevel), Integer.valueOf(result), remarks);
					Map<String,String> map = reviewService.findReview(Long.valueOf(revid));
					//审核成功状态
					if(reviewResult == 1){
						rebatesService.reviewSuccess(map);
					}
				}else{
					reviewResult = reviewService.UpdateReview(Long.valueOf(revid), Integer.valueOf(userLevel), Integer.valueOf(result), remarks);
					
				}
				num++;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error("机票批量审核返佣申请：审核ID为："+revid+"；  报错原因："+e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("机票批量审核返佣申请：审核ID为："+revid+"；  报错原因："+e.getMessage());
			}
		}
		back.put("res", "success");
		back.put("msg", "共有"+num+"条记录操作成功。");
		return back;
	}
	
	
	/**  
	 * 验证返佣信息
	 * @param rid
	 * @param userLevel
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="validReviewRebates")
	public String validReviewRebates(long rid, Integer userLevel, Model model, HttpServletRequest request){
		try {
			List<ReviewLog> reviewLog = rebatesService.validReviewRebates(rid, userLevel);
			if(reviewLog != null && reviewLog.size() > 0){
				return "false";
			}else{
				return "true";
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "error";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	
	/**
	 * 支付凭单 2015年5月27日
	 */
	@RequestMapping(value = "rebatesReviewPrint")
	public String visaReturnMoneyFeePrint(Model model,HttpServletRequest request, HttpServletResponse response) {

		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String payId = request.getParameter("payId");	//返佣申请ID
		String groupCode = request.getParameter("groupCode");	//团号
		String option = request.getParameter("option");
		
		Map<String,Object> map = null;
		if("pay".equals(option)) {
			map = rebatesService.buildPrintData(reviewId,payId);
		}else if("order".equals(option)) {
			map = rebatesService.buildPrintData(reviewId);
		}
		
		map.put("reviewId", reviewId);
		map.put("payId", payId);
		map.put("groupCode", groupCode);
		map.put("option", option);
				
		model.addAttribute("map", map);
	
		return "modules/order/rebates/review/rebatesReviewPrint";
	}
	
	
	/**
	 * 下载支出凭单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="rebatesReviewDownload")
	public ResponseEntity<byte[]> downloadBorrowMoneySheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String payId = request.getParameter("payId");	//refund ID
		String groupCode = request.getParameter("groupCode");	//团号		
		String option = request.getParameter("option");
		
		Map<String,Object> map = null;
		if("pay".equals(option)) {
			map = rebatesService.buildPrintData(reviewId,payId);
		}else if("order".equals(option)) {
			map = rebatesService.buildPrintData(reviewId);
		}
		map.put("groupCode", groupCode);
		map.put("createDate", DateUtils.formatDate((Date)map.get("createDate"), "yyyy年 MM月dd日 "));
		
		//更新打印时间
		this.updatePrintTime(reviewId);
		
		File file = rebatesService.createRebatesSheetDownloadFile(map);
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "支出凭单" + nowDate + ".doc";
		OutputStream os = null;
    	try {
			if(file != null && file.exists()){
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
		    	os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(file));
	            os.flush();
			}       		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(os!=null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
		return null;
	}
	
	@RequestMapping(value="updatePrintTime")
	@ResponseBody
	public void  updatePrintTime(String reviewId){
		Review review = reviewService.findReviewInfo(Long.parseLong(reviewId));
		if (null != review && (null == review.getPrintFlag() || 0 == review.getPrintFlag())) { 
			Date printDate = new Date();
			try {
				reviewService.updateReviewPrintInfoById(printDate,Long.parseLong(reviewId));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("格式化日期错误", e);
				throw e;
			} 
		}
	}
	
}
