package com.trekiz.admin.review.cost.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.ActivityAirTicketServiceImpl;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

@Controller
@RequestMapping(value="${adminPath}/cost/common")
public class CostCommonController {
	
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	private ICostCommonService costCommonService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private ReviewService oldReviewService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private ActivityAirTicketServiceImpl activityAirTicketService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private CurrencyService currencyService;
	private List<Map<String, Object>> totalPrice;
	
	@RequestMapping("uploadFilesPage")
	public String getUploadFilesPage() {
//		String costId = request.getParameter("costId");
//		model.addAttribute("costId", costId);
		return "review/cost/mulUploadFile";
	}
	
	/**
	 * 上传附件
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="upload",method=RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public void MulUploadFile (MultipartHttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, MultipartFile> fileMap=request.getFileMap();
		//List<String> newFileNameList=new ArrayList<String>();
		Map<String,String> fileNameMap=new HashMap<String,String>();
		PrintWriter out = response.getWriter();
		//上传完成后，需要返回上传文件对象
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()){
			MultipartFile file=entity.getValue();
			String originalName = file.getOriginalFilename();  
			String ext = FilenameUtils.getExtension(originalName).toLowerCase(Locale.ENGLISH);

			//使用唯一标识码生成文件名
			String newName=UUID.randomUUID().toString()+"."+(ext!=null?ext:"");
	
			File uploadFile=null;
			StringBuilder sb=new StringBuilder();
	
			sb.append(FileUtils.getUploadFilePath().get(1));
			uploadFile=new File(sb.toString());
			if(!uploadFile.exists()){
				uploadFile.mkdirs();
			}
			uploadFile=new File(sb.toString(), newName);
			try {
				FileCopyUtils.copy(file.getBytes(), uploadFile);
				//此处可进行数据库操作

				//保存到DocInfo
				DocInfo doc = new DocInfo();
				doc.setDocName(originalName);
				doc.setDocPath(FileUtils.getUploadFilePath().get(0) + newName);
				Long docId = docInfoService.saveDocInfo(doc).getId();
				fileNameMap.put(docId.toString(), originalName + "=" + FileUtils.getUploadFilePath().get(0) + newName);
			} catch (IOException e) {
				out.println("false");
				e.printStackTrace();
			}
		}
		response.getWriter().println(fileNameMap);
		try{
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
	}
	
	//更新实际成本附件
	@RequestMapping("updateCostVoucher")
	@Deprecated
	public void updateCostVoucher(@RequestParam String docIds, @RequestParam Long costId) {
		costCommonService.updateCostVoucher(docIds, costId);
	}
	
	@RequestMapping("deleteCostVoucher")
	@Deprecated
	public void deleteCostVoucher(@RequestParam String docId, @RequestParam Long costId) {
		costCommonService.deleteCostVoucher(docId, costId);
	}
	
	/**
	 * 成本录入：查找附件（用于下载附件弹框）
	 * @param costId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("getCostVouchers/{costId}")
	public String getCostVouchers(@PathVariable Long costId, Model model, HttpServletRequest request) {
		String delFlag = request.getParameter("delFlag");
		model.addAttribute("delFlag", delFlag);
		
		List<DocInfo> docInfoList = new ArrayList<DocInfo>();
		String vouchers = costCommonService.getCostVouchers(costId);
		if(StringUtils.isNotBlank(vouchers)) {
			String[] docIds = vouchers.split(",");
			for (int i = 0; i < docIds.length; i++) {
				DocInfo docInfo = docInfoService.getDocInfo(Long.parseLong(docIds[i]));
				docInfoList.add(docInfo);
			}
		}
		model.addAttribute("docInfoList", docInfoList);
		model.addAttribute("costId", costId);
		model.addAttribute("userId", UserUtils.getUser().getId());
		return "review/cost/download";
	}

	
	/**
	 * 成本录入中的其他收入录入、预算成本录入、实际成本录入、代收服务费查询
	 * @param model
	 * @param groupId
	 * @param costId
	 * @param productType
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getCostRecordList/{groupId}/{productType}", method=RequestMethod.GET)
	public String getCostRecordList(Model model, @PathVariable(value="groupId") Long groupId,Long costId,
									@PathVariable(value="productType") Integer productType, HttpServletRequest request) {
		model.addAttribute("costId",costId);
		User user = UserUtils.getUser();
		Long companyId= user.getCompany().getId();
		model.addAttribute("companyId",companyId);
		String companyUuid= user.getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		//签证
		if (productType == Context.ORDER_TYPE_QZ) {
			//获取签证产品
			VisaProducts visaProducts = visaProductsService.findByVisaProductsId(groupId);
			model.addAttribute("activityGroup", visaProducts);
			model.addAttribute("deptId", visaProducts.getDeptId());
			if(visaProducts.getDeptId() != null) {
				//获取无需审批状态
				CostUtils.getApproveStatus(companyUuid, visaProducts.getDeptId().toString(), "6", model);
			}
			//机票
		} else if (productType == Context.ORDER_TYPE_JP) {
			//获取机票产品
			ActivityAirTicket activityAirTicket = activityAirTicketService.getActivityAirTicketById(groupId);
			model.addAttribute("activityGroup", activityAirTicket);
			model.addAttribute("deptId", activityAirTicket.getDeptId());
			if(activityAirTicket.getDeptId() != null) {
				//获取无需审批状态
				CostUtils.getApproveStatus(companyUuid, activityAirTicket.getDeptId().toString(), "7", model);
			}
		//团期类
		} else {
			ActivityGroup activityGroup = activityGroupService.findById(groupId);
			model.addAttribute("activityGroup", activityGroup);
			//获得产品
			TravelActivity travelActivity = travelActivityService.findById(activityGroup.getSrcActivityId().longValue());
			model.addAttribute("deptId", travelActivity.getDeptId());
			model.addAttribute("activityId", travelActivity.getId());
			//无需审批状态
			if(travelActivity.getDeptId() != null) {
				//获取无需审批状态
				CostUtils.getApproveStatus(companyUuid, travelActivity.getDeptId().toString(), productType.toString(), model);
			}
		}
		
		//获取成本录入详情页其他收入、预算、实际成本录入列表
		costManageService.costInputList(model, groupId, productType);

		model.addAttribute("orderType", productType);
		//根据币种状态，获取币种列表
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);

		//是否计调职务
		model.addAttribute("isOperator",oldReviewService.checkJobType(3,4));
		//是否操作职务
		model.addAttribute("isOpt",oldReviewService.checkJobType(5));

		//天马运通Uuid
		model.addAttribute("TMYT", Context.SUPPLIER_UUID_TMYT);
		//鼎鸿假期uuid
		model.addAttribute("DHJQ", Context.SUPPLIER_UUID_DHJQ);
		//拉美途
		model.addAttribute("LMT", Context.SUPPLIER_UUID_LMT);
		if(Context.SUPPLIER_UUID_LMT.equals(companyUuid)) {
			//获取预报单锁定状态
			String budgetLock = costCommonService.getLockStatus(productType, groupId);
			model.addAttribute("budgetLock", budgetLock);
		}

		String flag = request.getParameter("flag");
		String read = request.getParameter("read");
		String budgetType = request.getParameter("budgetType");

		//目前只有散拼产品拥有服务费
		if(Context.ORDER_TYPE_SP == productType.intValue()){
			List<Map<String, String>> serviceCharge = new ArrayList<Map<String, String>>();
			//quauq服务费
			List<Map<String, String>> quauqServiceCharge = costCommonService.getQuauqServiceAmount(productType, groupId);
			//渠道服务费
			List<Map<String, String>> agentServiceCharge = costCommonService.getAgentServiceAmount(productType, groupId);
			String serviceChargeSum = "";
			if(!Collections3.isEmpty(quauqServiceCharge)){
				for (Map<String, String> map: quauqServiceCharge){
					serviceCharge.add(map);
				}
			}

			if(!Collections3.isEmpty(agentServiceCharge)){
				for (Map<String, String> map: agentServiceCharge){
					serviceCharge.add(map);
				}

			}
			model.addAttribute("serviceCharge", serviceCharge);
			//总额 查询代收服务费的总额（多币种展示）  总额=渠道服务费+quauq服务费
			 List<Map<String,Object>> list = costCommonService.getTotalPrice(productType, groupId);
			 for(Map<String,Object> tPrice : list){
				 if(null != tPrice.get("amount")){
					 //当金额为零时不显示在总额内  504需求
					 if(Double.parseDouble(tPrice.get("amount").toString())>0){
						 if(StringUtils.isBlank(serviceChargeSum)){
							 serviceChargeSum = tPrice.get("currencyMark")+""+tPrice.get("amount");
						 }else{
							 serviceChargeSum = serviceChargeSum+"+"+tPrice.get("currencyMark")+""+tPrice.get("amount");
						 }
					 }
				 }
				
			 }
			model.addAttribute("serviceChargeSum", serviceChargeSum);
		}

		if("1".equals(flag)) {
			model.addAttribute("read", read);
			model.addAttribute("budgetType", budgetType);
			return "review/cost/costRecordReviewList";
		}else{
			return "review/cost/costRecordList";
		}
	}
}
