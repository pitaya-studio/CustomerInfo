package com.trekiz.admin.modules.review.visareturnreceipt.web;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.visareturnreceipt.service.IVisaReturnReceiptService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.BatchTravelerRelation;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.BatchRecordDao;
import com.trekiz.admin.modules.sys.repository.BatchTravelerRelationDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.SysBatchNoService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;

@Controller
@RequestMapping(value = "${adminPath}/visa/workflow/returnreceipt")
public class VisaReturnReceiptController extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(VisaReturnReceiptController.class);
	
	public static final Integer VISA_PRODUCT_TYPE=6; //签证产品订单类型6表示
	public static final Integer VISA_HSJ_FLOW_TYPE=4; //还签证收据流程类型4表示
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private IVisaReturnReceiptService visaReturnReceiptService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private MoneyAmountService  moneyAmountService;
	
	@Autowired
    private VisaProductsService visaProductsService;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private ReviewLogDao reviewLogDao;
	
	@Autowired
    private SysBatchNoService sysBatchNoService;
	
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	
	@Autowired
	private VisaOrderDao visaOrderDao;

	@Autowired
	private VisaDao visaDao;

	@Autowired
	private BatchRecordDao batchRecordDao;

	@Autowired
	private BatchTravelerRelationDao batchTravelerRelationDao;
	
	/**
	 *还签证收据申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "createVisaHSJ")
	public Map<String, Object> createVisaHSJ(HttpServletRequest request,	
		HttpServletResponse response) throws JSONException {
		
		String travelerID = request.getParameter("travelerID");//游客ID
		//System.out.println(travelerID); 
		String receiptAmount = request.getParameter("receiptAmount");//收据金额
		String receiptor = request.getParameter("receiptor");//接收人
		String returnTime = request.getParameter("returnTime");
		String returnReceiptRemark = request.getParameter("returnReceiptRemark");
		returnReceiptRemark=returnReceiptRemark==null?"":returnReceiptRemark;
		
		//申请时的addReview 要用添加dept的方法
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerID));
		
		//审核中不能再申请
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer reply = new StringBuffer("还签证收据");
		String activeBorrowAmount = visaReturnReceiptService.getHSJActiveReview(traveler.getOrderId().toString(),traveler.getId());
		if (activeBorrowAmount!=null) {
			map.put("visaHSJreply",reply.append("已经申请,审核中！").toString());//1为申请成功 2为申请失败
			map.put("orderId", traveler.getOrderId().toString());
			return map;
		}
		
		Long deptId = visaOrderService.getProductPept(traveler.getOrderId());
		
		//查询visaId
		List<Visa> visaList = visaDao.findByTravelerId1(Long.parseLong(travelerID));
		Long visaId = visaList.get(0).getId();
		
		//获取人民币币种id
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
		buffer.append(" AND c.create_company_id=");
		buffer.append(UserUtils.getUser().getCompany().getId());
		List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
		Integer borrowtotalcurrencyId = 0;
		for (int i = 0; i < currencylist.size(); i++) {
			if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
				borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
				break;
			}
		}
		
		List<Detail> listDetail = new ArrayList<Detail>();
		listDetail.add(new Detail("receiptAmount", receiptAmount));
		listDetail.add(new Detail("currencyId", borrowtotalcurrencyId+""));//默认收据币种为RMB
		listDetail.add(new Detail("receiptor", receiptor));
		listDetail.add(new Detail("returnTime", returnTime));
		listDetail.add(new Detail("returnReceiptRemark", returnReceiptRemark));
		
		/**
		 * 2015-05-26
		 * 为了批次审核，单个还收据也生成批次号
		 */
		String batchNo =   sysBatchNoService.getVisaReturnReceiptBatchNo();
		
		//-------------------------------------------

		//生成uuid
		String uuid = UUID.randomUUID().toString();

		VisaFlowBatchOpration record = new VisaFlowBatchOpration();
		record.setUuid(uuid);
		record.setBatchNo(batchNo);
		record.setBusynessType("1");
		record.setBatchPersonCount(1);
		record.setBatchTotalMoney(receiptAmount);
		record.setCreateUserId(UserUtils.getUser().getId());
		record.setCreateUserName(UserUtils.getUser().getName());
		record.setCreateTime(new Date());
		record.setPrintStatus("0");
		record.setCurrencyId(borrowtotalcurrencyId);
		record.setCurrencyName("人民币");
		record.setReviewStatus("1");
		record.setIsSubmit("2");

		batchRecordDao.getSession().save(record);

		uuid = UUID.randomUUID().toString();
		BatchTravelerRelation relation = new BatchTravelerRelation();
		relation.setUuid(uuid);
		relation.setBatchUuid(record.getUuid());
		relation.setBatchRecordNo(batchNo);
		relation.setTravelerId(Long.parseLong(travelerID));
		relation.setVisaId(visaId);
		relation.setOrderId(traveler.getOrderId());
		relation.setTravelerName(traveler.getName());
		relation.setBusinessType(2);//业务类型 1:借款 2：还收据  3：借护照 4：还护照
		relation.setTravellerBorrowMoney(new BigDecimal(receiptAmount));
		relation.setRemark(returnReceiptRemark);
		relation.setCreatebyId(UserUtils.getUser().getId());
		relation.setCreatebyName(UserUtils.getUser().getName());
		relation.setIsSubmit("2");
		relation.setSaveTime(new Date());
		relation.setSubmitbyId(UserUtils.getUser().getId());
		relation.setSubmitbyName(UserUtils.getUser().getName());
		relation.setSubmitTime(new Date());

		batchTravelerRelationDao.getSession().save(relation);

		//-------------------------------------------
		
		
		
	
		listDetail.add(new Detail("visaReturnReceiptBatchNo", batchNo));
		
		
		
		long addresult = reviewService.addReview(VisaReturnReceiptController.VISA_PRODUCT_TYPE, //产品类型 
				VisaReturnReceiptController.VISA_HSJ_FLOW_TYPE,//流程类型  flowtype
				traveler.getOrderId()+"",//订单ID
				traveler.getId().longValue(),//游客ID
				Long.parseLong("0"), //新提交的审核请置 0. 重新提交审核时,等于上次审核记录的主键
				returnReceiptRemark,//创建原因
				reply, 
				listDetail,
				deptId);
		if (addresult!=0) {//1为申请成功 2为申请失败
			reply.append("申请成功");
		}

		
		map.put("visaHSJreply",reply.toString());
		return map;
	}
	
	 /**
	  * 改方法以废弃不用
	  * @param req
	  * @return
	  */
	 @Deprecated
	 @RequestMapping(value = "createVisaBatchHsj")
	 @ResponseBody
	 public Object createVisaBatchHsj(HttpServletRequest req){
		 String visaIds = req.getParameter("visaIds");
		 String travellerIds = req.getParameter("travellerIds")+"0";
		 String returnReceiptJe = req.getParameter("returnReceiptJe")+"0";
		 String returnReceiptName = req.getParameter("returnReceiptName")+"0";
		 String returnReceiptTime = req.getParameter("returnReceiptTime")+"0";
		 String returnReceiptRemark = req.getParameter("returnReceiptRemark")+"0";
		 Map<String, Object> resultMap = null;
		 try{
			 resultMap=visaReturnReceiptService.visaBatchHsj(visaIds, travellerIds, returnReceiptJe, returnReceiptName, returnReceiptTime, returnReceiptRemark);
		 }catch(Exception e){
			 e.printStackTrace();
			 if(resultMap==null){
				 resultMap = new HashMap<String,Object>();
			 }
			 resultMap.put("msg", e.getMessage());
			 
		 }
		 return resultMap;
	 }
	
	/**
	 * 还签证收据申请审批列表
	 * 
	 * wxw added
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaReturnReceiptReviewList")
	public String visaReturnReceiptReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
	   // List<Long> roleIds = UserUtils.getUser().getRoleIdList();
		//System.out.println(roleIds);
		
		//获取流程相关角色IDs
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);

		Page<Map<String, Object>> page=null;
		if (userJobs==null||userJobs.size()<1) {
			 page = visaReturnReceiptService.queryVisaReturnReceiptReviewInfo(request, response, "0");
		}else {
			 page = visaReturnReceiptService.queryVisaReturnReceiptReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
		}
		
		model.addAttribute("page", page);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		//处理页面从菜单进入审核列表时  默认角色的处理
		if (null==userJobId) {
			if (null!=userJobs&&userJobs.size()>0) {
				conditionsMap.put("userJobId",  userJobs.get(userJobs.size()-1).getId()+"");
			}
		}else {
			conditionsMap.put("userJobId",userJobId);
		}
		model.addAttribute("conditionsMap", conditionsMap);
		
        Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
		model.addAttribute("userJobs", userJobs);
		
		//-----下单人list wxw added 2015-08-19------
		List<String> createByNameList = visaOrderService.findVisaOrderCreateBy();
		model.addAttribute("createByList", createByNameList);
		
		//处理筛选自动关闭的
		//是否显示搜索条件的关闭与收起
		String isShowSearch = request.getParameter("showFlag");
		model.addAttribute("flag", isShowSearch);//以前屏蔽现在开启
		model.addAttribute("userid", UserUtils.getUser().getCompany().getId()); 
		return "modules/visareturnreceipt/visaReturnReceiptReviewList";
	}
	
	

	/**
	 * 还签证收据申请审批列表
	 * 
	 * wxw added
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaReturnReceiptReviewList4CW")
	public String visaReturnReceiptReviewList4CW(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
	   // List<Long> roleIds = UserUtils.getUser().getRoleIdList();
		//System.out.println(roleIds);
		
		//获取流程相关角色IDs
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);

		Page<Map<String, Object>> page=null;
		if (userJobs==null||userJobs.size()<1) {
			 page = visaReturnReceiptService.queryVisaReturnReceiptReviewInfo(request, response, "0");
		}else {
			 page = visaReturnReceiptService.queryVisaReturnReceiptReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
		}
		
		model.addAttribute("page", page);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		//处理页面从菜单进入审核列表时  默认角色的处理
		if (null==userJobId) {
			if (null!=userJobs&&userJobs.size()>0) {
				conditionsMap.put("userJobId",  userJobs.get(userJobs.size()-1).getId()+"");
			}
		}else {
			conditionsMap.put("userJobId",userJobId);
		}
		model.addAttribute("conditionsMap", conditionsMap);
		
        Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
		model.addAttribute("userJobs", userJobs);
		
		//-----下单人list wxw added 2015-08-19------
		List<String> createByNameList = visaOrderService.findVisaOrderCreateBy();
		model.addAttribute("createByList", createByNameList);
		
		//处理筛选自动关闭的
		//是否显示搜索条件的关闭与收起
		String isShowSearch = request.getParameter("showFlag");
		model.addAttribute("flag", isShowSearch);//以前屏蔽现在开启
		model.addAttribute("userid", UserUtils.getUser().getCompany().getId()); 
		return "modules/visareturnreceipt/visaReturnReceiptReviewList4CW";
	}
	
	
	
	/**
	 * 查询条件处理
	 * @param request
	 * @return
	 */
	private Map<String, Object> prepareQueryCond(HttpServletRequest request) {
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("orderType", request.getParameter("orderType"));
		conditionsMap.put("groupCode", request.getParameter("groupCode"));
		
		String statusChoose = request.getParameter("statusChoose");
		if (null==statusChoose) {
			statusChoose = "1";
		}
		conditionsMap.put("statusChoose", statusChoose);
		
		// conditionsMap.put("flowType", request.getParameter("flowType"));
		conditionsMap.put("channel", request.getParameter("channel") == null|| "".equals(request.getParameter("channel").trim())
				? null
				: Integer.parseInt(request.getParameter("channel")));
		
		//-----wxw 2015-07-28 added 打印状态------
		conditionsMap.put("printstatus", request.getParameter("printstatus") == null|| "".equals(request.getParameter("printstatus").trim())
				? null: Integer.parseInt(request.getParameter("printstatus")));
		
		//------wxw 2015-08-19 added 下单人------
		String orderCreateBy = request.getParameter("orderCreateBy");
		conditionsMap.put("orderCreateBy", orderCreateBy == null || "".equals(orderCreateBy.trim())? null: Integer.parseInt(orderCreateBy));
		
		String saler = request.getParameter("saler");
		conditionsMap.put("saler", saler == null || "".equals(saler.trim())? null: Integer.parseInt(saler));
		
		String meter = request.getParameter("meter");
		conditionsMap.put("meter", meter == null || "".equals(meter.trim())? null: Integer.parseInt(meter));
		
		// conditionsMap.put("active", request.getParameter("active"));
		conditionsMap.put("startTime", request.getParameter("startTime"));
		conditionsMap.put("endTime", request.getParameter("endTime"));
		conditionsMap.put("orderBy", request.getParameter("orderBy"));
		
		//处理从url传递的这两个字段
		conditionsMap.put("userJobId", request.getParameter("userJobId"));
		conditionsMap.put("flowType", request.getParameter("flowType"));
		
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss");//订单创建日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");//订单更新日期排序标识
		conditionsMap.put("orderCreateDateSort", orderCreateDateSort);
		conditionsMap.put("orderUpdateDateSort", orderUpdateDateSort);
		conditionsMap.put("orderCreateDateCss", orderCreateDateCss);
		conditionsMap.put("orderUpdateDateCss", orderUpdateDateCss);
		
		return conditionsMap;
	}
	
	
	/**
	 * 还签证审批详情页 
	 * wangxinwei
	 *  2014年12月13日15:00:00
	 */
	@RequestMapping(value = "visaReturnReceiptReviewDetail")
	public String visaReturnReceiptReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		
		//订单相关信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));

		String totalMoney = moneyAmountService.getMoney(visaOrder.getTotalMoney());
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("visaOrder", visaOrder);
		
		//产品相关信息
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Dict collarZoning = dictService.findByValueAndType(visaProduct.getCollarZoning().toString(), "from_area");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("visaType", visaType);
		model.addAttribute("collarZoning", collarZoning);
		model.addAttribute("country", country);
		
		//游客相关信息
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
		model.addAttribute("traveler", traveler);
		
		//签证相关信息
		Visa visa = visaService.findVisaByTravlerId(Long.parseLong(travelerId));
		Dict visaStauts = dictService.findByValueAndType(visa.getVisaStauts()+"", "visa_status");
		model.addAttribute("visa", visa);
		model.addAttribute("visaStauts", visaStauts);
		
		//还签证收据申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		if (reviewAndDetailInfoMap!=null) {
			model.addAttribute("revCreateDate", reviewAndDetailInfoMap.get("createDate").subSequence(0, 19));//报批日期
			model.addAttribute("revCreateReason", reviewAndDetailInfoMap.get("createReason"));//申报原因
			model.addAttribute("revReceiptAmount", reviewAndDetailInfoMap.get("receiptAmount"));//收据金额
			model.addAttribute("revReceiptor", reviewAndDetailInfoMap.get("receiptor"));//还收据人
			String currencyId = reviewAndDetailInfoMap.get("currencyId");
			if (null!=currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);//还收据人
			}
		}
		model.addAttribute("nowLevel",nowLevel);
		model.addAttribute("orderId",orderId);
		model.addAttribute("travelerId",travelerId);
		model.addAttribute("revid",revid);
		model.addAttribute("flowType",flowType);
		model.addAttribute("flag", flag);
		model.addAttribute("rid",revid);
		
		//model.addAttribute("airticketReturnDetailInfoMap",airticketReturnDetailInfoMap);
		return "modules/visareturnreceipt/visaReturnReceiptReviewDetail";
	}
	
	
	/**
	 * 还签证收据审核：驳回 或 通过
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "reviewVisaReturnReceipt")
	public String reviewVisaReturnReceipt(Model model,HttpServletRequest request, HttpServletResponse response) {
		StringBuffer reply = new StringBuffer();
		String revid = request.getParameter("revid");
		if (revid == null || "".equals(revid)) {
			reply.append("审批id不能为空");
		}
		String nowLevel = request.getParameter("nowLevel");
		if (nowLevel == null || "".equals(nowLevel)) {
			reply.append("审核 层级不能为空");
		}
		String denyReason = request.getParameter("denyReason");
		
		String result = request.getParameter("result");
		if (result == null || "".equals(result)) {
			reply.append("审批结果不能为空");
		}
		if (reply != null && !"".equals(reply.toString())) {
			model.addAttribute("reply", reply);
			return visaReturnReceiptReviewDetail(model, request, response);
		}
		//num == 1 审核通过
		//int num = 
		reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),denyReason);
		
	/*	List<Review> list = reviewDao.findReviewActive(Long.parseLong(revid));
		if(list.get(0).getTopLevel() == Integer.parseInt(nowLevel)){//如果当前层级为最高层级  说明该流程已经审核结束
			if (1==num) {//审核成功
			   //批量审核上线后要改为：还收据后状态暂定改为3
				reviewService.reviewOperationDone(Long.parseLong(revid),3); //在业务上没有后续操作：整个流程审批及后续操作完毕
			}
		}*/
		
		return visaReturnReceiptReviewList(model, request, response);
	}
	
	

	
	//-------------------------还款单开始 -------------------------
	/**
	 * 签证费还款单 wangxinwei 2015年4月16日
	 */
	@RequestMapping(value = "visaReturnMoneyFeePrint")
	public String visaReturnMoneyFeePrint(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		/*
		 * 填写日期取签务（计调人员）发起借款申请的日期；（ok） 
		 * 借款单位指签务（计调人员）所在部门；（ok） ---默认 签证部（暂不从系统取） 
		 * 经办人指申请人；（ok） 
		 * 审核暂时为空； ----------- 
		 * 财务主管暂时为空；-----------  
		 * 复核（审批）指最后一个审批人； （ok） 
		 * 借款金额人民币大写；（ok）  
		 * 领款人指申请人；（ok）  
		 * 主管审批（总经理）指最后一个审批人； （ok） 
		 * 出纳暂时为空；------------ 
		 * 确认付款日期即财务人员最后确认付款的日期； （ok）
		 */
		
		String revid = request.getParameter("revid");
		model.addAttribute("revid", revid);//吧revid 传到模板模板的html页面，下载模板时使用
		
		
		/*listDetail.add(new Detail("receiptAmount", receiptAmount));
		listDetail.add(new Detail("currencyId", "1"));//默认收据币种为RMB
		listDetail.add(new Detail("receiptor", receiptor));
		listDetail.add(new Detail("returnTime", returnTime));
		listDetail.add(new Detail("returnReceiptRemark", returnReceiptRemark));
		listDetail.add(new Detail("visaReturnReceiptBatchNo", null));*/
		
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		if (reviewAndDetailInfoMap != null) {
			model.addAttribute("revCreateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));// 填写日期
			model.addAttribute("returnReceiptRemark",reviewAndDetailInfoMap.get("returnReceiptRemark"));// 申报原因(变动)
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			if (null != user) {
				model.addAttribute("operatorName", user.getName());// 经办人、领款人都为还收据申请人
			} else {
				model.addAttribute("operatorName", "未知");
			}
			model.addAttribute("payDate", DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate")));// 付款日期
			model.addAttribute("receiptAmount",fmtMicrometer(reviewAndDetailInfoMap.get("receiptAmount")));// 借款金额
			String currencyId = reviewAndDetailInfoMap.get("currencyId");
			if (null != currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency.getRemark());
			}
		}
		
		//出纳以外的最后一个审批人：对签证借款流程来说level为3
		
		//Long userCompanyId = UserUtils.getUser().getCompany().getId();
		//int toplivelLong = reviewCompanyDao.findTopLevel(userCompanyId, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));
	    //List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));
	    
		/*if (null!=reviewLogs&&reviewLogs.size()>0) {
			User user = UserUtils.getUser(reviewLogs.get(reviewLogs.size()-1).getCreateBy());
			if (null!=user) {
				model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			}
		}*/
		
		model.addAttribute("revReturnAmountDx", digitUppercase(Double.parseDouble(reviewAndDetailInfoMap.get("receiptAmount"))));// 借款金额大写
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT,reviewLogs);
		
		//2015-04-16王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户财务主管为空
		 */
		if (null!=jobtypeusernameMap.get(9)) {//财务主管
			if (68!=UserUtils.getUser().getCompany().getId()) {
				model.addAttribute("cwmanager", jobtypeusernameMap.get(9));
			}else {
				model.addAttribute("cwmanager", "");
			}
		}else {
			model.addAttribute("cwmanager", "");
		}
		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			model.addAttribute("cw", jobtypeusernameMap.get(8));
		}else {
			model.addAttribute("cw", "");
		}
		
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (null!=jobtypeusernameMap.get(6)) {//出纳
			if (68!=UserUtils.getUser().getCompany().getId()) {
				model.addAttribute("cashier", jobtypeusernameMap.get(6));
			}else {
				model.addAttribute("cashier", "");
			}
		}else {
			model.addAttribute("cashier", "");
		}
		
		//model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
		if (null!=jobtypeusernameMap.get(10)) {//总经理
			model.addAttribute("majorCheckPerson", jobtypeusernameMap.get(10));
		}else {
			model.addAttribute("majorCheckPerson", "");
		}
		
		if (null!=jobtypeusernameMap.get(7)) {//部门经理
			model.addAttribute("deptmanager", jobtypeusernameMap.get(7));
		}else {
			model.addAttribute("deptmanager", "");
		}
		
		
		/*Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&null==review.getPrintFlag()) { 
			Date printDate = new Date();
			model.addAttribute("printDate",printDate );
			try {
				visaReturnReceiptService.updateReviewPrintInfoById(Long.parseLong(revid),printDate);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("签证费还款单日期格式化错", e);
				throw e;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}else {
			model.addAttribute("printDate",review.getPrintTime());
		}*/
		
		Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&null==review.getPrintFlag()) { 
			Date printDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(printDate);
			model.addAttribute("printDate",printDateStr );
		}else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(review.getPrintTime());
			model.addAttribute("printDate",printDateStr);
		}
	
		return "modules/visareturnreceipt/visaReturnMoneyFeePrint";
	}
	
	/**
	 * 点击打印按钮后更新打印日期
	 */
	@RequestMapping(value = "visaReturnMoneyFeePrintAjax")
	@ResponseBody
	public Map<String, Object> visaReturnMoneyFeePrintAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String revid = request.getParameter("revid");
		String printDatestr = request.getParameter("printDate");
		Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&null==review.getPrintFlag()) { 
			//Date printDate = new Date();
			//model.addAttribute("printDate",printDate );
		
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
				Date printDate = simpleDateFormat.parse(printDatestr);
				visaReturnReceiptService.updateReviewPrintInfoById(Long.parseLong(revid),printDate);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("签证费借款单日期格式化错", e);
				throw e;
			} catch (Exception e) {
				map.put("result",2);
				e.printStackTrace();
				return map;
				
			}
		}
		
		//map.put("error", "签证借款申请失败！");
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	/**
	 * 打印签证费还款单
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value="downloadVisaReturnMoneySheet")
	public ResponseEntity<byte[]> downloadVisaReturnMoneySheet(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String revid = request.getParameter("revid");
		File file = visaReturnReceiptService.createVisaReturnMoneySheetDownloadFile(Long.parseLong(revid));
		
		//签证费借款单生成后,更新借款单 的  打印状态和首次打印时间
/*		if(file != null && file.exists()){
			visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid));
		} */
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "签证费还款单" + nowDate + ".doc";
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
	
	 /**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零
     * 要用到正则表达式
     */
    public static String digitUppercase(double n){
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};
        String head = n < 0? "红字": ""; //负 -》红字
        n = Math.abs(n);  
        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){
            s = "整";   
        }
        int integerPart = (int)Math.floor(n);
 
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p ="";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
    
	
	
	/**
	 * 数字格式化
	 * @param text
	 * @return
	 */
	public String fmtMicrometer(String text){
		DecimalFormat df = null;
		df = new DecimalFormat("###,##0.00");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}
	
	//-------------------------还款单结束 -------------------------
	
	//--------------------------------------------------------------------------------------------------------
	/*********************   批次借款审核相关     开始       *********************/
	/*********************   批次借款审核相关     开始       *********************/
	/*********************   批次借款审核相关     开始       *********************/
	
	
	/**
	 * 还签证收据批次审批列表
	 * 
	 * 2015-05-28 wxw added
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaReturnReceiptBatchReviewList")
	public String visaReturnReceiptBatchReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
	   // List<Long> roleIds = UserUtils.getUser().getRoleIdList();
		//System.out.println(roleIds);
		
		//获取流程相关角色IDs
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);

		Page<Map<String, Object>> page=null;
		if (userJobs==null||userJobs.size()<1) {
			 page = visaReturnReceiptService.queryVisaReturnReceiptBatchReviewInfo(request, response, "0");
		}else {
			 page = visaReturnReceiptService.queryVisaReturnReceiptBatchReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
			 //visaReturnReceiptService.getJobReviewCountbyUserJob(userJobs.get(userJobs.size()-1));
		}
		
		model.addAttribute("page", page);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		//处理页面从菜单进入审核列表时  默认角色的处理
		if (null==userJobId) {
			if (null!=userJobs&&userJobs.size()>0) {
				conditionsMap.put("userJobId",  userJobs.get(userJobs.size()-1).getId()+"");
			}
		}else {
			conditionsMap.put("userJobId",userJobId);
		}
		model.addAttribute("conditionsMap", conditionsMap);
		
		//---wxw added 处理批量审核时角色显示的待审核记录不正确的问题 2015-09-29 ---
        for (UserJob userJob : userJobs) {
        	int userjobReviewCount = visaReturnReceiptService.getJobReviewCountbyUserJob(userJob);
        	userJob.setCount(userjobReviewCount);
		}
        
        Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
        
        
        
		model.addAttribute("userJobs", userJobs);
		
		
		//处理筛选自动关闭的
		//是否显示搜索条件的关闭与收起
		String isShowSearch = request.getParameter("showFlag");
		model.addAttribute("flag", isShowSearch);
		
		return "modules/visareturnreceipt/visaReturnReceiptBatchReviewList";
	}
	
	
	/**
	 * 还签证收据批次审批列表
	 * 
	 * 2015-05-28 wxw added
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaReturnReceiptBatchReviewList4CW")
	public String visaReturnReceiptBatchReviewList4CW(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
	   // List<Long> roleIds = UserUtils.getUser().getRoleIdList();
		//System.out.println(roleIds);
		
		//获取流程相关角色IDs
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);

		Page<Map<String, Object>> page=null;
		if (userJobs==null||userJobs.size()<1) {
			 page = visaReturnReceiptService.queryVisaReturnReceiptBatchReviewInfo(request, response, "0");
		}else {
			 page = visaReturnReceiptService.queryVisaReturnReceiptBatchReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
		}
		
		model.addAttribute("page", page);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		//处理页面从菜单进入审核列表时  默认角色的处理
		if (null==userJobId) {
			if (null!=userJobs&&userJobs.size()>0) {
				conditionsMap.put("userJobId",  userJobs.get(userJobs.size()-1).getId()+"");
			}
		}else {
			conditionsMap.put("userJobId",userJobId);
		}
		model.addAttribute("conditionsMap", conditionsMap);
		
        Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
    	//---wxw added 处理批量审核时角色显示的待审核记录不正确的问题 2015-09-29 ---
        for (UserJob userJob : userJobs) {
        	int userjobReviewCount = visaReturnReceiptService.getJobReviewCountbyUserJob(userJob);
        	userJob.setCount(userjobReviewCount);
		}
		model.addAttribute("userJobs", userJobs);
		
		//处理筛选自动关闭的
		//是否显示搜索条件的关闭与收起
		String isShowSearch = request.getParameter("showFlag");
		model.addAttribute("flag", isShowSearch);
		
		return "modules/visareturnreceipt/visaReturnReceiptBatchReviewList4CW";
	}
	
	
	/**
	 * 还签证收据批次审批详情页 
	 * wangxinwei
	 *  2015年05月28日15:19:00
	 */
	@RequestMapping(value = "visaReturnReceiptBatchReviewDetail")
	public String visaReturnReceiptBatchReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		
		String batchno = request.getParameter("batchno");
		
		String fromflag = request.getParameter("fromflag");
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
		buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
		buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");
		buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaReturnReceiptBatchNo' AND revd.myValue= vfbo.batch_no ");
		buffer.append(" AND vfbo.busyness_type = 1 AND rev.travelerId = tr.id AND vfbo.batch_no=");
		buffer.append("'"+batchno+"'");
		List<Map<String, Object>> list = reviewDao.findBySql(buffer.toString(), Map.class);
		//Map<String, Object>  mp =  list.get(0);
		//totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ct[i]);
		
		StringBuilder revids = new StringBuilder("");
		StringBuilder remarks = new StringBuilder("");
		for (Map<String, Object> map : list) {
			//签证借款申请相关信息
			Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong((Integer)map.get("reviewid")+""));
			map.put("receiptAmount", reviewAndDetailInfoMap.get("receiptAmount"));
			map.put("returnReceiptRemark", reviewAndDetailInfoMap.get("returnReceiptRemark"));
			map.put("receiptor", reviewAndDetailInfoMap.get("receiptor"));
			revids.append(reviewAndDetailInfoMap.get("id")).append(",");
			remarks.append(reviewAndDetailInfoMap.get("returnReceiptRemark")).append(",");
		}
		Map<String, String> reviewAndDetailInfoMapforone = reviewService.findReview(Long.parseLong(revid));
		model.addAttribute("revCreateDate", reviewAndDetailInfoMapforone.get("createDate").subSequence(0, 19));//报批日期
		
	
		model.addAttribute("nowLevel",nowLevel);
		model.addAttribute("orderId",orderId);
		model.addAttribute("travelerId",travelerId);
		model.addAttribute("revid",revid);
		model.addAttribute("flowType",flowType);
		model.addAttribute("flag", flag);
		model.addAttribute("rid",revid);
		
		//批量审批用到
		model.addAttribute("returnreceiptinfolist", list);
		model.addAttribute("batchno",batchno);
		model.addAttribute("revids",revids.toString());
		model.addAttribute("remarks",remarks.toString());
		
		model.addAttribute("fromflag",fromflag);
		
		//model.addAttribute("airticketReturnDetailInfoMap",airticketReturnDetailInfoMap);
		return "modules/visareturnreceipt/visaReturnReceiptBatchReviewDetail";
	}
	
	
	/**
	 * 按批次号审批
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "batchReviewvisaReturnReceiptbyBatchNo")
	@Transactional
	public String batchReviewvisaReturnReceiptbyBatchNo(Model model,HttpServletRequest request, HttpServletResponse response) {
		
        String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remark = request.getParameter("denyReason");//批量审核驳回原因
		//String[] remarksarray = remarks.split(",");
		String nowLevel = request.getParameter("nowLevel");//当前审核层级
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] revidsarray = revids.split(",");	
		
		String batchno = request.getParameter("batchno");
		
		boolean batchreviewstaus = false;//如审核通过更新批次社和状态
		
		/**
		 * 2015-05-28王新伟添加
		 * 签证借款批量审核：
		 * 
		 */
		for (int i = 0; i < revidsarray.length; i++) {
			StringBuffer reply = new StringBuffer();
			if (result == null || "".equals(result)) {
				reply.append("审批结果不能为空");
			}
			
			String revid = revidsarray[i];
			//String remark = remarksarray[i];
			
			/**
			 * 2015-05-28 wxw added
			 * 1.审核通过后在MoneyAmount中保存借款信息
			 * 2.处理与老数据兼容的相关问题：如果traveler的jkSerialNum为空，生成新的UUID并进行更新
			 * uuid取游客的jkSerialNum
			 */
			//UpdateReview返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
			int num = reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),remark);
			List<Review> list = reviewDao.findReviewActive(Long.parseLong(revid));
			if(list.get(0).getTopLevel() == Integer.parseInt(nowLevel)){//如果当前层级为最高层级  说明该流程已经审核结束
				if (1==num&&"1".equals(result)) {
					batchreviewstaus = true;
				}
			}	
		}
		
		//更新批次审批状态
		if (batchreviewstaus) {
			visaFlowBatchOprationDao.updateVisaFlowBatchOprationStatusByBatchNo(batchno, "1");
		}
		String fromflag = request.getParameter("fromflag");
		if ("CW".equalsIgnoreCase(fromflag)) {
			return visaReturnReceiptBatchReviewList4CW(model, request, response);
		}else {
			return visaReturnReceiptBatchReviewList(model, request, response);
		}
		
	}
	
	
	//-------------------------还款单开始 -------------------------
	/**
	 * 签证费还款单 wangxinwei 2015年06月01日
	 */
	@RequestMapping(value = "visaReturnMoneyBatchFeePrint")
	public String visaReturnMoneyBatchFeePrint(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		/*
		 * 填写日期取签务（计调人员）发起借款申请的日期；（ok） 
		 * 借款单位指签务（计调人员）所在部门；（ok） ---默认 签证部（暂不从系统取） 
		 * 经办人指申请人；（ok） 
		 * 审核暂时为空； ----------- 
		 * 财务主管暂时为空；-----------  
		 * 复核（审批）指最后一个审批人； （ok） 
		 * 借款金额人民币大写；（ok）  
		 * 领款人指申请人；（ok）  
		 * 主管审批（总经理）指最后一个审批人； （ok） 
		 * 出纳暂时为空；------------ 
		 * 确认付款日期即财务人员最后确认付款的日期； （ok）
		 */
		
		String revid = request.getParameter("revid");
		String batchno = request.getParameter("batchno");
		model.addAttribute("revid", revid);//吧revid 传到模板模板的html页面，下载模板时使用
		model.addAttribute("batchno", batchno);
		
		//获取批次借款金额
		VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"1");
		String batchborrowtotalMoney = visaFlowBatchOpration.getBatchTotalMoney();
		
		
		/*listDetail.add(new Detail("receiptAmount", receiptAmount));
		listDetail.add(new Detail("currencyId", "1"));//默认收据币种为RMB
		listDetail.add(new Detail("receiptor", receiptor));
		listDetail.add(new Detail("returnTime", returnTime));
		listDetail.add(new Detail("returnReceiptRemark", returnReceiptRemark));
		listDetail.add(new Detail("visaReturnReceiptBatchNo", null));*/
		
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		if (reviewAndDetailInfoMap != null) {
			model.addAttribute("revCreateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));// 填写日期
			model.addAttribute("returnReceiptRemark",reviewAndDetailInfoMap.get("returnReceiptRemark"));// 申报原因(变动)
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			if (null != user) {
				model.addAttribute("operatorName", user.getName());// 经办人、领款人都为还收据申请人
			} else {
				model.addAttribute("operatorName", "未知");
			}
			model.addAttribute("payDate", DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate")));// 付款日期
			model.addAttribute("receiptAmount",fmtMicrometer(batchborrowtotalMoney));// 借款金额   借款金额 改为批次金额
			String currencyId = reviewAndDetailInfoMap.get("currencyId");
			if (null != currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency.getRemark());
			}
		}
		
		//出纳以外的最后一个审批人：对签证借款流程来说level为3
		
		//Long userCompanyId = UserUtils.getUser().getCompany().getId();
		//int toplivelLong = reviewCompanyDao.findTopLevel(userCompanyId, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));
	    //List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));
	    
		/*if (null!=reviewLogs&&reviewLogs.size()>0) {
			User user = UserUtils.getUser(reviewLogs.get(reviewLogs.size()-1).getCreateBy());
			if (null!=user) {
				model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			}
		}*/
		
		model.addAttribute("revReturnAmountDx", digitUppercase(Double.parseDouble(batchborrowtotalMoney)));// 借款金额大写     借款金额 改为批次金额
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT,reviewLogs);
		
		//2015-04-16王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户财务主管为空
		 */
		if (null!=jobtypeusernameMap.get(9)) {//财务主管
			if (68!=UserUtils.getUser().getCompany().getId()) {
				model.addAttribute("cwmanager", jobtypeusernameMap.get(9));
			}else {
				model.addAttribute("cwmanager", "");
			}
		}else {
			model.addAttribute("cwmanager", "");
		}
		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			model.addAttribute("cw", jobtypeusernameMap.get(8));
		}else {
			model.addAttribute("cw", "");
		}
		
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (null!=jobtypeusernameMap.get(6)) {//出纳
			if (68!=UserUtils.getUser().getCompany().getId()) {
				model.addAttribute("cashier", jobtypeusernameMap.get(6));
			}else {
				model.addAttribute("cashier", "");
			}
		}else {
			model.addAttribute("cashier", "");
		}
		
		//model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
		if (null!=jobtypeusernameMap.get(10)) {//总经理
			model.addAttribute("majorCheckPerson", jobtypeusernameMap.get(10));
		}else {
			model.addAttribute("majorCheckPerson", "");
		}
		
		if (null!=jobtypeusernameMap.get(7)) {//部门经理
			model.addAttribute("deptmanager", jobtypeusernameMap.get(7));
		}else {
			model.addAttribute("deptmanager", "");
		}
		
		
		/*Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&null==review.getPrintFlag()) { 
			Date printDate = new Date();
			model.addAttribute("printDate",printDate );
			try {
				visaReturnReceiptService.updateReviewPrintInfoById(Long.parseLong(revid),printDate);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("签证费还款单日期格式化错", e);
				throw e;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}else {
			model.addAttribute("printDate",review.getPrintTime());
		}*/
		
		Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&(null==review.getPrintFlag()||"0".equals(review.getPrintFlag()+""))) { 
			Date printDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(printDate);
			model.addAttribute("printDate",printDateStr );
		}else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(review.getPrintTime());
			model.addAttribute("printDate",printDateStr);
		}
		
		/**
		 * wangxinwei 20151103 added
		 * 解决还签证收据审核打印（批次），还款理由只能显示一个游客备注问题
		 * 改为显示多个游客的备注，以空格作为间隔，依靠模板自身做换行显示
		 */
		String remarkString = visaReturnReceiptService.getReturnReCeiptBatchPrintAppReason(batchno);
		model.addAttribute("returnReceiptRemark",remarkString);
	
		return "modules/visareturnreceipt/visaReturnMoneyBatchFeePrint";
	}
	
	

	/**
	 * 点击打印按钮后更新打印日期
	 */
	@RequestMapping(value = "visaReturnMoneyBatchFeePrintAjax")
	@ResponseBody
	@Transactional
	public Map<String, Object> visaReturnMoneyBatchFeePrintAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//String revid = request.getParameter("revid");
		String printDatestr = request.getParameter("printDate");
		
		String batchno = request.getParameter("batchno");
		VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"1");
		
		if (null!=visaFlowBatchOpration&&!"1".equals(visaFlowBatchOpration.getPrintStatus())) {
			//Date printDate = new Date();
			//model.addAttribute("printDate",printDate );
			
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
    		buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
    		buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");
    		buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaReturnReceiptBatchNo' AND revd.myValue= vfbo.batch_no ");
    		buffer.append(" AND vfbo.busyness_type = 1 AND rev.travelerId = tr.id AND vfbo.batch_no=");
    		buffer.append("'"+batchno+"'");
    		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		
    		Date printDate = null;
    		try {
    			
    			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
    			printDate = simpleDateFormat.parse(printDatestr);
    			
    			if (null!=list&&list.size()>0) {
    				for (Map<String, Object> map2 : list) {
    					visaReturnReceiptService.updateReviewPrintInfoById(Long.parseLong((Integer)map2.get("reviewid")+""),printDate);
    				}
    				visaFlowBatchOprationDao.updateVisaFlowBatchPrintTimeAndPrintStatus(batchno, "1", printDate);
    			}
    			
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("签证费还款单日期格式化错", e);
				throw e;
			} catch (Exception e) {
				map.put("result",2);
				e.printStackTrace();
				
			}
		}
		
		//map.put("error", "签证借款申请失败！");
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	
	/**
	 * 打印签证费还款单
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value="batchDownloadVisaReturnMoneySheet")
	public ResponseEntity<byte[]> batchDownloadVisaReturnMoneySheet(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String revid = request.getParameter("revid");
		String batchno = request.getParameter("batchno");
		File file = visaReturnReceiptService.createBatchVisaReturnMoneySheetDownloadFile(Long.parseLong(revid),batchno);
		
		//签证费借款单生成后,更新借款单 的  打印状态和首次打印时间
/*		if(file != null && file.exists()){
			visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid));
		} */
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "签证费批次还款单" + nowDate + ".doc";
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
	
	
	
	/**
	 * 审核回退：根据批次号进行批次回退操作,即返回上一级审核
	 * 规则如下：
	 * 1.第一层级 和 最后一层级没有退回操作
	 * 2.隔级审核后不能再进行退回操作
	 * 3.只有审核中状态的才可能进行退回操作
	 * wangxinwei 2015年06月02日10:23:00
	 */
	@RequestMapping(value = "visaReturnReceiptBatchCancelAjax")
	@ResponseBody
	public Map<String, Object> visaReturnReceiptBatchCancelAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		String batchno = request.getParameter("batchno");
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
			buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
			buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");//AND revd.myKey = 'visaReturnReceiptBatchNo'
			buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaReturnReceiptBatchNo' AND revd.myValue= vfbo.batch_no ");
			buffer.append(" AND vfbo.busyness_type = 1 AND rev.travelerId = tr.id AND vfbo.batch_no=");//vfbo.busyness_type = 1
			buffer.append("'"+batchno+"'");
			List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		
			if (null!=list&&list.size()>0) {
				for (Map<String, Object> map2 : list) {
					//审核回退
					reviewService.CancelReview(Long.parseLong((Integer)map2.get("reviewid")+""));
				}
			}
		}catch (Exception e) {
			map.put("result",2);
			e.printStackTrace();
			return map;
		}	
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	
	/*********************   批次借款审核相关     结束       *********************/
	/*********************   批次借款审核相关     结束      *********************/
	/*********************   批次借款审核相关     结束       *********************/
	
	
	//-----------------------取回审批-------------------------
	/**
	 * 审核回退：根据revid进行批次回退操作,即返回上一级审核
	 * 规则如下：
	 * 1.第一层级 和 最后一层级没有退回操作
	 * 2.隔级审核后不能再进行退回操作
	 * 3.只有审核中状态的才可能进行退回操作
	 * wangxinwei 2015年06月01日21:23:00
	 */
	@RequestMapping(value = "returnReceiptCancelAjax")
	@ResponseBody
	public Map<String, Object> returnReceiptCancelAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		String revid = request.getParameter("revid");
		try {
			
			//审核回退
		    if(null!=revid) {
		    	reviewService.CancelReview(Long.parseLong(revid));
			}
				
		}catch (Exception e) {
			map.put("result",2);
			e.printStackTrace();
			return map;
		}	
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	//-------------------多项选择审批   开始---------------------
	/**
	 * wxw added 2015-08-11
	 * 签证还收据单游客   不定个数审核
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "multiReviewVisaReturnReceipt")
	public Object multiReviewVisaReturnReceipt(Model model,HttpServletRequest request, HttpServletResponse response) {
		
        String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		
		
		for (int i = 0; i < levelandrevids.length; i++) {
			StringBuffer reply = new StringBuffer();
			if (result == null || "".equals(result)) {
				reply.append("审批结果不能为空");
			}
			
			String nowLevel = levelandrevids[i].split("@")[0];
			String revid = levelandrevids[i].split("@")[1];
			
			
			/**
			 * 2015-08-11 wxw added 不定量多个还签证收据同时审批
			 */
			//UpdateReview返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
			reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),remarks);
		}
		
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("msg", "操作成功！");
		 return resultMap;
		
	}
	
	
	/**
	 * wxw added 2015-08-11
	 * 签证还收据   不定个数审核
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "multiBatchReviewvisaReturnReceiptbyBatchNo")
	public Object multiBatchReviewvisaReturnReceiptbyBatchNo(Model model,HttpServletRequest request, HttpServletResponse response) {
		
		String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		String batchnons = request.getParameter("batchnons");
		
		boolean isSuccess = visaReturnReceiptService.multiBatchReviewvisaReturnReceiptbyBatchNo(result, remarks, batchnons);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (isSuccess) {
			resultMap.put("msg", "操作成功！");
		}else {
			resultMap.put("msg", "操作失败！");
		}
		return resultMap;
		
	}
	
	//-------------------多项选择审批   结束---------------------
	

}
