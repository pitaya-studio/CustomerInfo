package com.trekiz.admin.modules.review.visaborrowmoney.web;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.order.util.ReviewCommonUtil;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.visaborrowmoney.service.IVisaBorrowMoneyService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
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
import com.trekiz.admin.modules.sys.utils.DictUtils;
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
import com.trekiz.admin.modules.visa.web.VisaPreOrderController;

@Controller
@RequestMapping(value = "${adminPath}/visa/workflow/borrowmoney")
public class VisaBorrowMoneyController extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(VisaBorrowMoneyController.class);
	
	public static final Integer VISA_PRODUCT_TYPE=6; //产品订单类型6表示2015/05/25
	public static final Integer VISA_JK_FLOW_TYPE=5; //签证借款流程
	public static final String SPLITMARK = "#@!#!@#";
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private IVisaBorrowMoneyService visaBorrowMoneyService;

	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private OrderCommonService orderCommonService;
	
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
	private ReviewLogDao reviewLogDao;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private ReviewDetailDao reviewDetailDao;
	
	@Autowired
	private VisaOrderDao visaOrderDao;
	
	@Autowired
    private SysBatchNoService sysBatchNoService;

	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;

	@Autowired
	private VisaDao visaDao;

	@Autowired
	private BatchRecordDao batchRecordDao;

	@Autowired
	private BatchTravelerRelationDao batchTravelerRelationDao;
	
	@Autowired
	private RefundService refundService;
	

	/**
	 * 签证借款申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "createVisaJK")
	public Map<String, Object> createVisaJK(HttpServletRequest request,
		HttpServletResponse response) throws JSONException {
		
		String travelerID = request.getParameter("travelerID");//游客ID
		//System.out.println(travelerID);
		String borrowAmount = request.getParameter("borrowAmount");//借款金额
		String borrowRemark = request.getParameter("borrowRemark");//借款原因
		String borrowTime = request.getParameter("borrowTime");//借款金额\
		
		//查询visaId
		List<Visa> visaList = visaDao.findByTravelerId1(Long.parseLong(travelerID));
		Long visaId = visaList.get(0).getId();
		//申请时的addReview 要用添加dept的方法
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerID));
		
		
		//审核中不能再申请
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer reply = new StringBuffer("签证借款");
		String activeBorrowAmount = visaBorrowMoneyService.getHQXActiveReview(traveler.getOrderId().toString(),traveler.getId());
		if (activeBorrowAmount!=null) {
			map.put("visaJKreply",reply.append("已经申请,审核中！").toString());//1为申请成功 2为申请失败
			map.put("orderId", traveler.getOrderId().toString());
			return map;
		}
		
		//处理以保存
		List<BatchTravelerRelation> batchTravelerRelationList = batchTravelerRelationDao.findBatchNo(traveler.getId(), 1);
		if (batchTravelerRelationList != null && batchTravelerRelationList.size() > 0) {
			for(BatchTravelerRelation list : batchTravelerRelationList){
				if("1".equals(list.getIsSubmit())){
					map.put("visaJKreply",reply.append("该游客已经被保存到待提交批次列表！").toString());//1为申请成功 2为申请失败
					map.put("orderId", traveler.getOrderId().toString());
					return map;
				}
			}
		}
	
		
		//通过orderId获取产品的发布部门
		Long deptId = visaOrderService.getProductPept(traveler.getOrderId());
		
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
		
		/**
		 * 为了批次审核，单个借款的用户也生成批次号
		 * 
		 */
		String batchNo =   sysBatchNoService.getVisaBorrowMoneyBatchNo();
		
		//----------------------
		
		//生成uuid
		String uuid = UUID.randomUUID().toString();

		VisaFlowBatchOpration record = new VisaFlowBatchOpration();
		record.setUuid(uuid);
		record.setBatchNo(batchNo);
		record.setBusynessType("2");
		record.setBatchPersonCount(1);
		record.setBatchTotalMoney(borrowAmount);
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
		relation.setBusinessType(1);//业务类型 1:借款 2：还收据  3：借护照 4：还护照
		relation.setTravellerBorrowMoney(new BigDecimal(borrowAmount));
		relation.setRemark(borrowRemark);
		relation.setCreatebyId(UserUtils.getUser().getId());
		relation.setCreatebyName(UserUtils.getUser().getName());
		relation.setIsSubmit("2");
		relation.setSaveTime(new Date());
		relation.setSubmitbyId(UserUtils.getUser().getId());
		relation.setSubmitbyName(UserUtils.getUser().getName());
		relation.setSubmitTime(new Date());

		batchTravelerRelationDao.getSession().save(relation);
		
		//----------------------
		

		List<Detail> listDetail = new ArrayList<Detail>();
		listDetail.add(new Detail("borrowAmount", borrowAmount));

		listDetail.add(new Detail("currencyId", borrowtotalcurrencyId+""));//默认借款币种为RMB
		
		listDetail.add(new Detail("borrowRemark", borrowRemark));
		listDetail.add(new Detail("borrowTime", borrowTime));
		

		listDetail.add(new Detail("visaBorrowMoneyBatchNo", batchNo));
		
		long  addresult = reviewService.addReview(VisaBorrowMoneyController.VISA_PRODUCT_TYPE, //产品类型 
				VisaBorrowMoneyController.VISA_JK_FLOW_TYPE,//流程类型  flowtype
				traveler.getOrderId()+"",//订单ID
				traveler.getId().longValue(),//游客ID
				Long.parseLong("0"), //新提交的审核请置 0. 重新提交审核时,等于上次审核记录的主键
				borrowRemark,//创建原因
				reply, 
				listDetail,
				deptId);
		if (addresult!=0) {
			reply.append("申请成功");
		}

		//map.put("error", "签证借款申请失败！");
		map.put("visaJKreply",reply.toString());//1为申请成功 2为申请失败
		Review review = getTravelerBorrowedStatusAndTime(traveler.getOrderId().toString(), traveler.getId());
	    map.put("borrowStatus", ReviewCommonUtil.getNextReview(review.getId()));
		
		return map;
	}
	
	
	
	/**
	 * 获取游客借款金额的字符串  否则返回null
	 * @param orderId
	 * @param travelerId
	 * @return 游客借款金额的字符串
	 */
	private Review getTravelerBorrowedStatusAndTime(String orderId, Long travelerId){
		List<Review> listReview = new ArrayList<Review>();
		if (StringUtils.isNotEmpty(orderId) && travelerId != null) {
			listReview = reviewDao.findReviewActive(6, 5, orderId, travelerId);
		}
		if(null == listReview || listReview.size() == 0){
			return null;
		}else{
			return listReview.get(0);
		}
	}
	
	
	 @RequestMapping(value = "createBatchVisaJK")
	 @ResponseBody
	 public Object createBatchVisaJK(HttpServletRequest req){
		 String visaIds = req.getParameter("visaIds")+"0";
		 String travellerIds = req.getParameter("travellerIds")+"0";
		 String persons = req.getParameter("passportOperator")+"0";
		 String dates = req.getParameter("passportOperateTime")+"0";
		 String moneys = req.getParameter("moneys")+"0";
		 String others = req.getParameter("passportOperateRemark")+"0";
		 Map<String, Object> resultMap = null;
		 try{
			 resultMap=visaBorrowMoneyService.visaBatchJk(visaIds,travellerIds,persons,dates,moneys,others);
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
	 * 签证借款申请审批列表
	 * 
	 * xinwei.wang added
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaBorrowMoneyReviewList")
	public String visaBorrowMoneyReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
	    // 按点击数倒序 
		//sortList(userJobs);
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
	
		Page<Map<String, Object>> page = null;
		if (userJobs==null||userJobs.size()<1) {
			page = visaBorrowMoneyService.queryVisaBorrowMoneyReviewInfo(request, response, "0");
		}else {
			//如果默认相关审核流程有多个审核职位，给一个默认职位已确认一个审核层级
			page = visaBorrowMoneyService.queryVisaBorrowMoneyReviewInfo(request, response, userJobs.get(0).getId()+"");
		}
		
		model.addAttribute("page", page);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		//处理页面从菜单进入审核列表时  默认角色的处理
		if (null==userJobId) {
			if (null!=userJobs&&userJobs.size()>0) {
				conditionsMap.put("userJobId",  userJobs.get(0).getId()+"");
			}
		}else {
			conditionsMap.put("userJobId",userJobId);
		}
		/**
		 * 测试URL
		 * http://localhost:8080/trekiz_wholesaler_tts/a/visa/workflow/borrowmoney/visaBorrowMoneyReviewList?flowType=5
		 */                                             
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
  /*      Collections.sort(userJobs, new Comparator<UserJob>() {  
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
        });*/
		model.addAttribute("userJobs", userJobs); //部门 + 下划线 + 职务，即 UserJob 类中的 deptName_jobName, 例如： “北京分公司签证部_计调（机票）”
		//System.out.println("================"+UserUtils.getUser().getCompany().getId());
		model.addAttribute("userid", UserUtils.getUser().getCompany().getId()); 
		
		List<String> createByNameList = visaOrderService.findVisaOrderCreateBy();
		model.addAttribute("createByList", createByNameList);
		
		return "modules/visaborrowmoney/visaBorrowMoneyReviewList";
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
				? null: Integer.parseInt(request.getParameter("channel")));

		
		//-----wxw 2015-07-28 added 打印状态------
		conditionsMap.put("printstatus", request.getParameter("printstatus") == null|| "".equals(request.getParameter("printstatus").trim())
				? null: Integer.parseInt(request.getParameter("printstatus")));
		//-----wxw 2015-07-29 added 游客姓名------
		conditionsMap.put("travlerName", request.getParameter("travlerName") == null|| "".equals(request.getParameter("travlerName").trim())
				? null: request.getParameter("travlerName"));
		//-----wxw 2015-07-29 added 借款金额------
		String batchBorrowAmountStart = request.getParameter("batchBorrowAmountStart");
		conditionsMap.put("batchBorrowAmountStart", batchBorrowAmountStart == null|| "".equals(batchBorrowAmountStart)
				? null: batchBorrowAmountStart);
		
		String batchBorrowAmountEnd = request.getParameter("batchBorrowAmountEnd");
		conditionsMap.put("batchBorrowAmountEnd", batchBorrowAmountEnd == null|| "".equals(batchBorrowAmountEnd)
				? null: batchBorrowAmountEnd);
		
		//------wxw 2015-08-19 added 下单人------
		String orderCreateBy = request.getParameter("orderCreateBy");
		conditionsMap.put("orderCreateBy", orderCreateBy == null || "".equals(orderCreateBy.trim())? null: Integer.parseInt(orderCreateBy));
		

		String saler = request.getParameter("saler");
		conditionsMap.put("saler", saler == null || "".equals(saler.trim())? null: Integer.parseInt(saler));
		
		//签证国家
		String sysCountryId = request.getParameter("sysCountryId");
		conditionsMap.put("sysCountryId", sysCountryId == null || "".equals(sysCountryId.trim())
				? null: Integer.parseInt(sysCountryId));
		
		String meter = request.getParameter("meter");
		conditionsMap.put("meter", meter == null || "".equals(meter.trim())
				? null: Integer.parseInt(meter));
		
		//签证类型的查询
		String visaType = request.getParameter("visaType");
		conditionsMap.put("visaType", visaType == null || "".equals(visaType.trim())
				? null: Integer.parseInt(visaType));
		
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
	 * 签证借款审批详情页 
	 * wangxinwei
	 *  2014年12月21日15:15:00
	 */
	@RequestMapping(value = "visaBorrowMoneyReviewDetail")
	public String visaBorrowMoneyReviewDetail(Model model,
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
		model.addAttribute("country", country);
		model.addAttribute("collarZoning", collarZoning);
		
		//游客相关信息
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
		model.addAttribute("traveler", traveler);
		
		//签证相关信息
		Visa visa = visaService.findVisaByTravlerId(Long.parseLong(travelerId));
		Dict visaStauts = dictService.findByValueAndType(visa.getVisaStauts()+"", "visa_status");
		model.addAttribute("visa", visa);
		model.addAttribute("visaStauts", visaStauts);
		
		//签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		if (reviewAndDetailInfoMap!=null) {
			model.addAttribute("revCreateDate", reviewAndDetailInfoMap.get("createDate").subSequence(0, 19));//报批日期
			model.addAttribute("revBorrowRemark", reviewAndDetailInfoMap.get("borrowRemark"));//申报原因
			model.addAttribute("revBorrowAmount", reviewAndDetailInfoMap.get("borrowAmount"));//收据金额
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
		return "modules/visaborrowmoney/visaBorrowMoneyReviewDetail";
	}
	
	
	/**
	 * 签证借款审核：驳回 或 通过
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "reviewVisaBorrowMoney")
	public String reviewVisaBorrowMoney(Model model,HttpServletRequest request, HttpServletResponse response) {
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
			return visaBorrowMoneyReviewDetail(model, request, response);
		}
		
		/*
		 * 2015-05-07 wxw added
		 * 1.审核通过后在MoneyAmount中保存借款信息
		 * 2.处理与老数据兼容的相关问题：如果traveler的jkSerialNum为空，生成新的UUID并进行更新
		 * uuid取游客的jkSerialNum
		 */
		//UpdateReview返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
		int num = reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),denyReason);
		List<Review> list = reviewDao.findReviewActive(Long.parseLong(revid));
		Traveler traveler = travelerService.findTravelerById(list.get(0).getTravelerId());
		String jkSerialNum = UUID.randomUUID().toString();
		if (null==traveler.getJkSerialNum()||"".equals(traveler.getJkSerialNum())) {
			travelerService.updateJkSerialNumByTravelerId(jkSerialNum,traveler.getId());
			traveler.setJkSerialNum(jkSerialNum);
		}
		
		//if(list.get(0).getTopLevel() == Integer.parseInt(nowLevel)){//如果当前层级为最高层级  说明该流程已经审核结束
			if (null!=traveler&&1==num&&"1".equals(result)) {
				//获取借款币种
				List<ReviewDetail> currencyId =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "currencyId");
				//获取借款金额
				List<ReviewDetail> borrowAmount =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "borrowAmount");
			    BigDecimal price = new BigDecimal(borrowAmount.get(0).getMyvalue());
			    
				MoneyAmount costMoneyAmount = new MoneyAmount(traveler.getJkSerialNum(), //款项UUID
						Integer.parseInt(currencyId.get(0).getMyvalue()),//币种ID
						price,//相应币种的金额
			    		traveler.getId(), //订单或游客ID
			    		Context.MONEY_TYPE_JK, //款项类型: 借款
			    		Context.ORDER_TYPE_QZ,//订单类型
			    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1表示订单，2表示游客
			    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
				costMoneyAmount.setReviewId(Long.parseLong(revid));
			    moneyAmountService.addMoneyAmount(costMoneyAmount);
			}
		//}
		return visaBorrowMoneyReviewList(model, request, response);
	}
	
	
	/**
	 * 签证费借款单 wangxinwei 2014年2月3日11:30:00
	 */
	@RequestMapping(value = "visaBorrowMoneyFeePrint")
	public String visaBorrowMoneyFeePrint(Model model,
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
		
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		if (reviewAndDetailInfoMap != null) {
			model.addAttribute("revCreateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));// 填写日期
			
			//----- wxw added 20151008 -----单需求C221 ， 处理付款确认时间
			model.addAttribute("revUpdateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate")));// 更新日期
			
			
			model.addAttribute("revBorrowRemark",reviewAndDetailInfoMap.get("borrowRemark"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			model.addAttribute("productCreater", productCreater);
			
			
			if (null != user) {
				model.addAttribute("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				model.addAttribute("operatorName", "未知");
			}
			model.addAttribute("payDate", DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate")));// 付款日期
			model.addAttribute("revBorrowAmount",MoneyNumberFormat.getThousandsMoney(Double.valueOf(reviewAndDetailInfoMap.get("borrowAmount")), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 借款金额
			String currencyId = reviewAndDetailInfoMap.get("currencyId");
			if (null != currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);
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
		
		
		model.addAttribute("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(
				MoneyNumberFormat.getThousandsMoney(Double.valueOf(reviewAndDetailInfoMap.get("borrowAmount")), MoneyNumberFormat.POINT_TWO))));// 借款金额大写
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY,reviewLogs);
		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			model.addAttribute("cw", jobtypeusernameMap.get(8));
		}else {
			model.addAttribute("cw", "");
		}
		
		//lihong  123
		//2015-04-09王新伟添加
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
		
	/*	Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&null==review.getPrintFlag()) { 
			Date printDate = new Date();
			model.addAttribute("printDate",printDate );
			try {
				visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid),printDate);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("签证费借款单日期格式化错", e);
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
		
		
		//----- wxw added 20151008 -----单需求C221， 新行者签证借款付款状态，payStatus：1 显示upDateDate时间，0不显示
		//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		Long companyId = UserUtils.getUser().getCompany().getId();	
		StringBuffer buffer = new StringBuffer();
		String  payStatus = null;
		buffer.append("SELECT rev.payStatus FROM review rev WHERE rev.id =");
		buffer.append("'"+revid+"'");
		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		if (null!=list&&list.size()>0) {
			payStatus = list.get(0).get("payStatus").toString();
			if (companyId==88||companyId==68) {
				model.addAttribute("payStatus","0");
			}else {
				model.addAttribute("payStatus",payStatus);
			}
		}else {
			model.addAttribute("payStatus","0");
		}
		
		return "modules/visaborrowmoney/visaBorrowMoneyFeePrint";
	}
	
	
	/**
	 * 签证费借款单 wangxinwei 2014年2月3日11:30:00
	 */
	@RequestMapping(value = "visaBorrowMoneyFeePrintAjax")
	@ResponseBody
	public Map<String, Object> visaBorrowMoneyFeePrintAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String revid = request.getParameter("revid");
//		String payId = request.getParameter("payId");
		
		String printDatestr = request.getParameter("printDate");
		Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&null==review.getPrintFlag()) { 
		
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
				Date printDate = simpleDateFormat.parse(printDatestr);
				visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid),printDate);
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
		
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	@RequestMapping(value="downloadVisaBorrowMoneySheet")
	public ResponseEntity<byte[]> downloadVisaBorrowMoneySheet(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String revid = request.getParameter("revid");
		File file = visaBorrowMoneyService.createVisaBorrowMoneySheetDownloadFile(Long.parseLong(revid));
		
		//签证费借款单生成后,更新借款单 的  打印状态和首次打印时间
/*		if(file != null && file.exists()){
			visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid));
		} */
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "签证费借款单" + nowDate + ".doc";
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
	
	public void sortList(List<UserJob> UserJobs){
    	Collections.sort(UserJobs, new Comparator<UserJob>() {
			@Override
			public int compare(UserJob o1, UserJob o2) {
				long o1Id = o1.getId();
				long o2Id = o2.getId();
				if (o1Id > o2Id) {  
                    return 1;  
                } else if (o1Id == o2Id) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
			}
		});
    }
	

	/**
	 * 签证借款批量审核
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchReviewVisaBorrowMoney")
	public Object batchReviewVisaBorrowMoney(Model model,HttpServletRequest request, HttpServletResponse response) {
		
        String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		
		
		/**
		 * 2015-04-20王新伟添加
		 * 签证借款批量审核：
		 * 
		 */
		for (int i = 0; i < levelandrevids.length; i++) {
			StringBuffer reply = new StringBuffer();
			if (result == null || "".equals(result)) {
				reply.append("审批结果不能为空");
			}
			
			String revid = levelandrevids[i].split("@")[1];
			String nowLevel = levelandrevids[i].split("@")[0];
			
			/**
			 * 2015-05-07 wxw added
			 * 1.审核通过后在MoneyAmount中保存借款信息
			 * 2.处理与老数据兼容的相关问题：如果traveler的jkSerialNum为空，生成新的UUID并进行更新
			 * uuid取游客的jkSerialNum
			 */
			//UpdateReview返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
			int num = reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),remarks);
			List<Review> list = reviewDao.findReviewActive(Long.parseLong(revid));
			Traveler traveler = travelerService.findTravelerById(list.get(0).getTravelerId());
			String jkSerialNum = UUID.randomUUID().toString();
			//如果游客没有添加借款序列号，则生成，否则取已有序列号
			if (null==traveler.getJkSerialNum()||"".equals(traveler.getJkSerialNum())) {
				travelerService.updateJkSerialNumByTravelerId(jkSerialNum,traveler.getId());
				traveler.setJkSerialNum(jkSerialNum);
			}
			
			//if(list.get(0).getTopLevel() == Integer.parseInt(nowLevel)){//如果当前层级为最高层级  说明该流程已经审核结束
				if (null!=traveler&&1==num&&"1".equals(result)) {
					//获取借款币种
					List<ReviewDetail> currencyId =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "currencyId");
					//获取借款金额
					List<ReviewDetail> borrowAmount =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "borrowAmount");
				    BigDecimal price = new BigDecimal(borrowAmount.get(0).getMyvalue());
				    
					MoneyAmount costMoneyAmount = new MoneyAmount(traveler.getJkSerialNum(), //款项UUID
							Integer.parseInt(currencyId.get(0).getMyvalue()),//币种ID
							price,//相应币种的金额
				    		traveler.getId(), //订单或游客ID
				    		Context.MONEY_TYPE_JK, //款项类型: 借款
				    		Context.ORDER_TYPE_QZ,//订单类型
				    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1表示订单，2表示游客
				    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
					costMoneyAmount.setReviewId(Long.parseLong(revid));
				    moneyAmountService.addMoneyAmount(costMoneyAmount);
				}
			//}
		}
		
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("msg", "操作成功！");
		 return resultMap;
		
		//审核后跳转到审核列表页面
		//return "redirect:"+Global.getAdminPath()+"/visa/workflow/borrowmoney/visaBorrowMoneyReviewList?flowType=5";
		//return visaBorrowMoneyReviewList(model, request, response);
	
	}
	
	/**
	 * 新行者  借款详情 页面
	 */
	@RequestMapping(value="visaBorrowMoneyDetail")
	public String visaBorrowMoneyDetail(@RequestParam Long visaOrderId,Model model){
		//订单封装
		VisaOrder visaOrder = visaOrderService.findVisaOrder(visaOrderId);
		String totalMoney = moneyAmountService.getMoney(visaOrder.getTotalMoney());
		@SuppressWarnings("unchecked")
		List<Integer>  visaStatus = (List<Integer>) orderCommonService.findOrderStatusByOrderId(visaOrderId);
		 
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("orderStatus", visaStatus.get(0));
		//产品封装
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		Currency currency = currencyService.findCurrency(visaOrder.getProOriginCurrencyId().longValue());
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
				
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("currency", currency);
		model.addAttribute("visaType", visaType);
		model.addAttribute("country", country);
		//游客信息
		List<Traveler> travelers = travelerService.findTravelerByOrderIdAndOrderType(visaOrderId, 6);
		for (int i = 0; i < travelers.size(); i++) {
			String payPriceSerialNumInfo = moneyAmountService.getMoney(travelers.get(i).getPayPriceSerialNum());
			travelers.get(i).setPayPriceSerialNumInfo(payPriceSerialNumInfo);
			String borrowMoneyandCheckStatus = visaOrderService.getTravelerBorrowedMoney(travelers.get(i).getOrderId()+"", travelers.get(i).getId());
			if (borrowMoneyandCheckStatus!=null) {
				travelers.get(i).setBorrowMoney(borrowMoneyandCheckStatus.split(",")[0]);
				travelers.get(i).setBorrowMoneyCheckStatus(borrowMoneyandCheckStatus.split(",")[1]);
			}
		}
		model.addAttribute("travelers", travelers);
		return "modules/visa/visaBorrowMoneyDetail";
	}
	
	
	
	/*********************   新行者借款相关     开始      *********************/
	/*********************   新行者借款相关     开始       *********************/
	/*********************   新行者借款相关     开始       *********************/

	/**
	 * 
	 * @author jiachen
	 * @DateTime 2015年5月6日 上午11:54:23
	 * @param visaOrderId
	 * @param model
	 * @return String
	 */
	@RequestMapping("applyForm")
	public String applyForm(@RequestParam(value="visaOrderId", required=true)String visaOrderId, Model model) {
		if(StringUtils.isNotBlank(visaOrderId)) {
			//签证订单
			VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(visaOrderId));
			model.addAttribute("visaOrder", visaOrder);
			model.addAttribute("visaOrderId", visaOrderId);
			
			
			//签证产品
			Long proId = visaOrder.getVisaProductId();
			if(null != proId && 0 != proId) {
				VisaProducts visaProduct = visaProductsService.findByVisaProductsId(proId);
				model.addAttribute("visaProduct", visaProduct);
			}
			
			//游客列表
			List<Traveler> travelerList = visaBorrowMoneyService.getTravelerList(visaOrderId);
			model.addAttribute("travelerList", travelerList);
			
			//币种列表
			List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
			model.addAttribute("currencyList", currencyList);
		}
		return "modules/visa/batchBorrowMoneyForm";
	}

	/**
	 * 新行者签证借款申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "createVisaJK4XXZ")
	public Map<String, Object> createVisaJK4XXZ(HttpServletRequest request,
		HttpServletResponse response) throws JSONException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer reply = new StringBuffer("签证借款");
		
		String orderid = request.getParameter("visaOrderId");//订单id
	/*	String activeBorrowAmount = visaBorrowMoneyService.getXinXingZheActiveReview(orderid);
		if (activeBorrowAmount!=null) {
			map.put("visaJKreply",reply.append("已经申请,审核中！").toString());//1为申请成功 2为申请失败
			map.put("orderId", orderid);
			return map;
		}*/
		
		// test url
		//http://localhost:8080/trekiz_wholesaler_tts/a/visa/workflow/borrowmoney/applyForm?visaOrderId=300 申请页面
		//
		
		/**
		 * 
		 * 1.新行者字符串分割符号    #@!#!@#
		 * 
		 * 2.游客借款数据
		 *   游客id：trvids
		 *   游客姓名：trvnames
		 *   游客币种：trvcurrents
		 *   游客借款额：trvamounts
		 *   游客结算价：trvsettlementprices
		 *   游客借款备注：trvborrownotes
		 *   
		 * 3.团队借款数据
		 *   
		 *   订单id：orderid
		 *   团队款项名称：groupborrownames
		 *   团队款项币种：groupborrowcurrents
		 *   团队款项借款额：groupborrowamounts
		 *   团队借款备注：groupborrownodes
		 *   
		 * 4.借款总额：totalborrowamount  币种，符号，额度 #@!#!@#币种，符号，额度
		 * 5.借款总备注：grouptotalborrownode
		 * 
		 */
		List<Detail> listDetail = new ArrayList<Detail>();
		
		//游客借款信息
		String[] trvids = request.getParameterValues("trvids");//游客ID
		String[] trvnames = request.getParameterValues("trvnames");//游客姓名
		String[] borrowtrvcurrents = request.getParameterValues("refundCurrency");//游客币种
		String[] trvamounts = request.getParameterValues("lendPrice");//借款金额  trvamounts
		String[] trvsettlementprices = request.getParameterValues("trvsettlementprices");//游客结算价
		String[] trvborrownotes = request.getParameterValues("trvborrownotes");//游客借款备注
		
		//团队借款信息
		String[] groupborrownames = request.getParameterValues("groupborrownames");//团队款项名称
		String[] groupborrowcurrents = request.getParameterValues("teamCurrency");//团队款项币种  groupborrowcurrents
		String[] groupborrowamounts = request.getParameterValues("teamMoney");//团队款项借款额  groupborrowamounts
		String[] groupborrownodes = request.getParameterValues("groupborrownodes");//团队借款备注
		
		String grouptotalborrownode = request.getParameter("otherRemarks");//总团队借款备注
		//String totalborrowamount = request.getParameter("totalborrowamount");//团队借款总额
		
		//处理订单 游客的 借款信息-----------------------------
		StringBuilder sb_trvids = new StringBuilder("");//游客ID
		StringBuilder sb_trvnames = new StringBuilder("");//游客姓名
		StringBuilder sb_borrowtrvcurrents = new StringBuilder("");//游客币种
		StringBuilder sb_trvamounts = new StringBuilder("");//借款金额
		StringBuilder sb_trvsettlementprices = new StringBuilder("");//游客结算价
		StringBuilder sb_trvborrownotes = new StringBuilder("");//游客借款备注
		
		//保存游客借款汇率、币种名称、币种符号
		StringBuilder sb_trvborrowexchangerates = new StringBuilder("");//游客借款备注
		StringBuilder sb_trvcurrencyNames = new StringBuilder("");//游客借款备注
		StringBuilder sb_trvcurrencyMarks = new StringBuilder("");//游客借款备注
		
		
		
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		
		for (int i = 0; i < trvamounts.length; i++) {
			if (null!=trvamounts[i]&&!"".equals(trvamounts[i].trim())) {
				sb_trvids.append(trvids[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客ID
				sb_trvnames.append(trvnames[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客姓名
				sb_borrowtrvcurrents.append(borrowtrvcurrents[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客币种
				sb_trvamounts.append(trvamounts[i]).append(VisaBorrowMoneyController.SPLITMARK);//借款金额
				sb_trvsettlementprices.append(trvsettlementprices[i]).append(VisaBorrowMoneyController.SPLITMARK);//游客结算价
				String trvborrownote = "".equals(trvborrownotes[i])?" ":trvborrownotes[i];
				sb_trvborrownotes.append(trvborrownote).append(VisaBorrowMoneyController.SPLITMARK);//游客借款备注
				
				//获取游客借款汇率
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.currency_name,c.currency_mark,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
				buffer.append(borrowtrvcurrents[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
				Map<String, Object>  mp =  list.get(0);
				sb_trvborrowexchangerates.append(mp.get("convert_lowest")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_trvcurrencyNames.append(mp.get("currency_name")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_trvcurrencyMarks.append(mp.get("currency_mark")).append(VisaBorrowMoneyController.SPLITMARK);
			}
		}
		listDetail.add(new Detail("trvids", sb_trvids.toString()));//游客ID
		listDetail.add(new Detail("trvnames", sb_trvnames.toString()));//游客姓名
		listDetail.add(new Detail("borrowtrvcurrents", sb_borrowtrvcurrents.toString()));//游客币种
		listDetail.add(new Detail("trvamounts", sb_trvamounts.toString()));//借款金额
		listDetail.add(new Detail("trvsettlementprices", sb_trvsettlementprices.toString()));//游客结算价
		listDetail.add(new Detail("trvborrownotes", sb_trvborrownotes.toString()));//游客借款备注
		//保存游客借款汇率、币种名称、币种符号
		listDetail.add(new Detail("trvborrowexchangerates", sb_trvborrowexchangerates.toString()));//游客借款汇率
		listDetail.add(new Detail("trvcurrencyNames", sb_trvcurrencyNames.toString()));//游客借款币种名称
		listDetail.add(new Detail("trvcurrencyMarks", sb_trvcurrencyMarks.toString()));//游客借款币种符号
		
		
		//处理订单的  借款信息-------------------------
		StringBuilder sb_groupborrownames = new StringBuilder("");//团队款项名称
		StringBuilder sb_groupborrowcurrents = new StringBuilder("");//团队款项币种
		StringBuilder sb_groupborrowamounts = new StringBuilder("");//团队款项借款额
		StringBuilder sb_groupborrownodes = new StringBuilder("");//团队借款备注
		
		//保存订单借款汇率、币种名称、币种符号
		StringBuilder sb_groupborrowexchangerates = new StringBuilder("");//游客借款备注
		StringBuilder sb_groupcurrencyNames = new StringBuilder("");//游客借款备注
		StringBuilder sb_groupcurrencyMarks = new StringBuilder("");//游客借款备注
		
		for (int i = 0; i < groupborrowamounts.length; i++) {
			if (null!=groupborrowamounts[i]&&!"".equals(groupborrowamounts[i].trim())) {
				String groupborrowname = "".equals(groupborrownames[i])?" ":groupborrownames[i];
				sb_groupborrownames.append(groupborrowname).append(VisaBorrowMoneyController.SPLITMARK);//团队款项名称
				sb_groupborrowcurrents.append(groupborrowcurrents[i]).append(VisaBorrowMoneyController.SPLITMARK);//团队款项币种
				sb_groupborrowamounts.append(groupborrowamounts[i]).append(VisaBorrowMoneyController.SPLITMARK);//团队款项借款额
				String groupborrownode = "".equals(groupborrownodes[i])?" ":groupborrownodes[i];
				sb_groupborrownodes.append(groupborrownode).append(VisaBorrowMoneyController.SPLITMARK);//团队借款备注
				
				//获取订单借款汇率
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.currency_name,c.currency_mark,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
				buffer.append(groupborrowcurrents[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
				Map<String, Object>  mp =  list.get(0);
				sb_groupborrowexchangerates.append(mp.get("convert_lowest")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_groupcurrencyNames.append(mp.get("currency_name")).append(VisaBorrowMoneyController.SPLITMARK);
				sb_groupcurrencyMarks.append(mp.get("currency_mark")).append(VisaBorrowMoneyController.SPLITMARK);
				
			}
		}
		listDetail.add(new Detail("groupborrownames", sb_groupborrownames.toString()));//团队款项名称
		listDetail.add(new Detail("groupborrowcurrents", sb_groupborrowcurrents.toString()));//团队款项币种
		listDetail.add(new Detail("groupborrowamounts", sb_groupborrowamounts.toString()));//团队款项借款额
		listDetail.add(new Detail("groupborrownodes", sb_groupborrownodes.toString()));//团队借款备注
		
		listDetail.add(new Detail("orderid", orderid));//订单ID
		listDetail.add(new Detail("grouptotalborrownode", grouptotalborrownode));//总团队借款备注
		
		//保存游客借款汇率、币种名称、币种符号
		listDetail.add(new Detail("groupborrowexchangerates", sb_groupborrowexchangerates.toString()));//游客借款备注
		listDetail.add(new Detail("groupcurrencyNames", sb_groupcurrencyNames.toString()));//游客借款币种名称
		listDetail.add(new Detail("groupcurrencyMarks", sb_groupcurrencyMarks.toString()));//游客借款币种符号
		
		
		
		//将借款多币种总和根据汇率转换成人民币
		String count = (sb_trvamounts.toString()+sb_groupborrowamounts.toString()).replace(VisaBorrowMoneyController.SPLITMARK, ",");
		String currencyId = (sb_borrowtrvcurrents.toString()+sb_groupborrowcurrents.toString()).replace(VisaBorrowMoneyController.SPLITMARK, ",");
		float totalborrow4rmb = visaBorrowMoneyService.currencyConverter(count, currencyId);
		listDetail.add(new Detail("borrowAmount", totalborrow4rmb+""));//按照汇率转为人民币的团队借款总额
		
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
		listDetail.add(new Detail("currencyId", borrowtotalcurrencyId+""));//汇总后各个渠道的人民币币种id
		
		//将币种和金额汇总后转换成如下格式  借款金额：￥800+$800
		StringBuilder sbcurentcy = new StringBuilder("");
		Map<String, BigDecimal> currentMap =  getTotalMoney(currencyId.split(","),count.split(","));
		Set<String> keys = currentMap.keySet();
		for (String key : keys) {
			Currency currency = currencyService.findCurrency(Long.parseLong(key));
			sbcurentcy.append(currency.getCurrencyMark()).append(currentMap.get(key).toString()).append(" ");
		}
		String totalborrowamount = sbcurentcy.toString().replace(" ", "+");
		listDetail.add(new Detail("totalborrowamount", totalborrowamount.substring(0,totalborrowamount.length()-1)));//团队借款总额str.subString(0,str.length()-1)
		
		
		/**
		 * 处理review表中是否需要保存游客id
		 * 规则如下：
		 * 1.如果有团队借款则不保存
		 * 2.如没有团队借款则看游客借款是否为一条，如为一条则保存
		 */
		String[] travelerIds = sb_trvids.toString().split(VisaBorrowMoneyController.SPLITMARK);
		String travelerId = "0";
		if (null==sb_groupborrowamounts||sb_groupborrowamounts.toString().length()==0) {
			travelerId = travelerIds.length==1?travelerIds[0]:"0";
		}
		
		//通过orderId获取产品的发布部门
		Long deptId = visaOrderService.getProductPept(orderid);
		
		long  addresult = reviewService.addReview(Context.ORDER_TYPE_QZ, //产品类型 
				Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY,//流程类型  flowtype
				orderid,//订单ID
				Long.parseLong(travelerId),//游客ID
				Long.parseLong("0"), //新提交的审核请置 0. 重新提交审核时,等于上次审核记录的主键
				"",//创建原因
				reply, 
				listDetail,
				deptId);
		if (addresult!=0) {
			reply.append("申请成功");
		}

		
		//map.put("error", "签证借款申请失败！");
		map.put("visaJKreply",reply.toString());//1为申请成功 2为申请失败
		map.put("orderId", orderid);
		return map;
		
	}
	
	/**
	 * 将币种和金额汇总后转换成如下格式  借款金额：￥800+$800
	 * 
	 * @param currencyIds:{"1,2,34,5","1,3,34,7"};
	 * @param currencyPrices:{"120.00,50.00,40.00,70.00","100.00,50.00,40.00,50.00"}
	 * @return
	 */
	private Map<String, BigDecimal> getTotalMoney(String[] currencyIds,String[] currencyPrices ){
		//合并所有游客各个币种的钱数
		Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
		for (int i = 0; i < currencyIds.length; i++) {
			String currencyId = currencyIds[i];
			String currencyPrice = currencyPrices[i];
			String[] currIds = currencyId.split(",");
			String[] currPrices = currencyPrice.split(",");
			for (int j = 0; j < currIds.length; j++) {
				String currId = currIds[j];
				String currPrice = currPrices[j];

				if (!map.containsKey(currId)) {
					map.put(currId, new BigDecimal(currPrice));
				} else {
					map.put(currId,map.get(currId).add(new BigDecimal(currPrice)));
				}
			}
		}
		return map;
	}
	
	
	/**
	 * 新行者签证借款申请审批列表
	 * 
	 * xinwei.wang added
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaBorrowMoney4XXZReviewList")
	public String visaBorrowMoney4XXZReviewList(Model model,HttpServletRequest request, HttpServletResponse response) {
		
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY );
	    // 按点击数倒序 
		sortList(userJobs);
		
		Page<Map<String, Object>> page = null;
		if (userJobs==null||userJobs.size()<1) {
			page = visaBorrowMoneyService.queryVisaBorrowMoney4XXZReviewInfo(request, response, "0");
		}else {
			//如果默认相关审核流程有多个审核职位，给一个默认职位已确认一个审核层级
			page = visaBorrowMoneyService.queryVisaBorrowMoney4XXZReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
		}
		
		model.addAttribute("page", page);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond4XXZBorrowMoney(request);
		String userJobId = request.getParameter("userJobId");
		//处理页面从菜单进入审核列表时  默认角色的处理
		if (null==userJobId) {
			if (null!=userJobs&&userJobs.size()>0) {
				conditionsMap.put("userJobId",  userJobs.get(userJobs.size()-1).getId()+"");
			}
		}else {
			conditionsMap.put("userJobId",userJobId);
		}
		/**
		 * 测试URL
		 * http://localhost:8080/trekiz_wholesaler_tts/a/visa/workflow/borrowmoney/visaBorrowMoney4XXZReviewList?flowType=20
		 */
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
  /*      Collections.sort(userJobs, new Comparator<UserJob>() {  
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
        });*/
		model.addAttribute("userJobs", userJobs); //部门 + 下划线 + 职务，即 UserJob 类中的 deptName_jobName, 例如： “北京分公司签证部_计调（机票）”
		
		model.addAttribute("flowType", "20"); 
		
		List<String> createByNameList = visaOrderService.findVisaOrderCreateBy();
		model.addAttribute("createByList", createByNameList);
		
		
		return "modules/visaborrowmoney/visaBorrowMoney4XinXingZheReviewList";
	}
	
	
	/**
	 * 查询条件处理
	 * @param request
	 * @return
	 */
	private Map<String, Object> prepareQueryCond4XXZBorrowMoney(HttpServletRequest request) {
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("orderType", request.getParameter("orderType"));
		conditionsMap.put("groupCode", request.getParameter("groupCode"));
		conditionsMap.put("statusChoose", request.getParameter("statusChoose"));
		// conditionsMap.put("flowType", request.getParameter("flowType"));
		conditionsMap.put("channel", request.getParameter("channel") == null
				|| "".equals(request.getParameter("channel").trim())
				? null: Integer.parseInt(request.getParameter("channel")));
		
		//------wxw 2015-08-19 added 下单人------
		String orderCreateBy = request.getParameter("orderCreateBy");
		conditionsMap.put("orderCreateBy", orderCreateBy == null || "".equals(orderCreateBy.trim())? null: Integer.parseInt(orderCreateBy));
		
		String saler = request.getParameter("saler");
		conditionsMap.put("saler", saler == null || "".equals(saler.trim())? null: Integer.parseInt(saler));
		
		String statusChoose = request.getParameter("statusChoose");
		if (null==statusChoose) {
			statusChoose = "1";
		}
		conditionsMap.put("statusChoose", statusChoose);
		
		
		String meter = request.getParameter("meter");
		conditionsMap.put("meter", meter == null || "".equals(meter.trim())
				? null: Integer.parseInt(meter));
		
		//签证类型的查询
		String visaType = request.getParameter("visaType");
		conditionsMap.put("visaType", visaType == null || "".equals(visaType.trim())
				? null: Integer.parseInt(visaType));
		
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
	 * 新行者签证借款审批详情页 
	 * created by wangxinwei
	 */
	@RequestMapping(value = "visaBorrowMoney4XXZReviewDetail")
	public String visaBorrowMoney4XXZReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		
		//签证订单
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(orderId));
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("visaOrderId", orderId);
		
		//签证产品
		Long proId = visaOrder.getVisaProductId();
		if(null != proId && 0 != proId) {
			VisaProducts visaProduct = visaProductsService.findByVisaProductsId(proId);
			model.addAttribute("visaProduct", visaProduct);
		}
		
		
		//签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		
		//String[] trvids = reviewAndDetailInfoMap.get("trvids").split(VisaBorrowMoneyController.SPLITMARK);//游客ID
		String[] trvnames = reviewAndDetailInfoMap.get("trvnames").split(VisaBorrowMoneyController.SPLITMARK);//游客姓名
		String[] borrowtrvcurrents = reviewAndDetailInfoMap.get("borrowtrvcurrents").split(VisaBorrowMoneyController.SPLITMARK);//游客币种
		String[] trvamounts = reviewAndDetailInfoMap.get("trvamounts").split(VisaBorrowMoneyController.SPLITMARK);//借款金额  trvamounts
		String[] trvsettlementprices = reviewAndDetailInfoMap.get("trvsettlementprices").split(VisaBorrowMoneyController.SPLITMARK);//游客结算价
		String[] trvborrownotes = reviewAndDetailInfoMap.get("trvborrownotes").split(VisaBorrowMoneyController.SPLITMARK);//游客借款备注
		//1.游客列表
		List<Map<String, String>> travelerList = new ArrayList<Map<String,String>>();
		for (int i = 0; i < trvamounts.length; i++) {
			if (!"".equals(trvamounts[i])) {
				Map<String, String> trvborrowmap = new HashMap<String, String>();
				trvborrowmap.put("tname", trvnames[i]);
				Currency currency = currencyService.findCurrency(Long.parseLong(borrowtrvcurrents[i]));
				trvborrowmap.put("crrencyName", currency.getCurrencyName());
				trvborrowmap.put("trvsettlementprice", trvsettlementprices[i]);
				trvborrowmap.put("trvamount", currency.getCurrencyMark()+trvamounts[i]);
				trvborrowmap.put("trvborrownote", trvborrownotes[i]);
				travelerList.add(trvborrowmap);
			}

		}
		model.addAttribute("travelerList", travelerList);
		
		//2.团队借款列表
		String[] groupborrownames = reviewAndDetailInfoMap.get("groupborrownames").split(VisaBorrowMoneyController.SPLITMARK);//团队款项名称
		String[] groupborrowcurrents = reviewAndDetailInfoMap.get("groupborrowcurrents").split(VisaBorrowMoneyController.SPLITMARK);//团队款项币种  groupborrowcurrents
		String[] groupborrowamounts = reviewAndDetailInfoMap.get("groupborrowamounts").split(VisaBorrowMoneyController.SPLITMARK);//团队款项借款额  groupborrowamounts
		String[] groupborrownodes = reviewAndDetailInfoMap.get("groupborrownodes").split(VisaBorrowMoneyController.SPLITMARK);//团队借款备注
		List<Map<String, String>> groupList = new ArrayList<Map<String,String>>();
		//System.out.println(groupborrowcurrents.length);
		
		for (int i = 0; i < groupborrowamounts.length; i++) {
			if (!"".equals(groupborrowamounts[i])) {
				Map<String, String> groupborrowmap = new HashMap<String, String>();
				groupborrowmap.put("groupborrowname", groupborrownames[i]);
				Currency currency = currencyService.findCurrency(Long.parseLong(groupborrowcurrents[i]));
				groupborrowmap.put("groupborrowcurrent", currency.getCurrencyName());
				groupborrowmap.put("groupborrowamount", currency.getCurrencyMark()+groupborrowamounts[i]);
				groupborrowmap.put("groupborrownode", groupborrownodes[i]);
				groupList.add(groupborrowmap);
			}

		}
			
		
		
		
		model.addAttribute("totalMoney", visaOrder.getTotalMoney());//用于前台的标签计算订单金额
		model.addAttribute("groupList", groupList);
		
		//处理总额的显示
		String totalborrowamount = reviewAndDetailInfoMap.get("totalborrowamount");
	/*	if (totalborrowamount!=null&&totalborrowamount.trim().length()>0) {
			for (int i = 0; i < groupborrownodes.length; i++) {
				
				
			}
		}*/
		model.addAttribute("totalborrowamount", totalborrowamount);//借款单总额
		model.addAttribute("grouptotalborrownode", reviewAndDetailInfoMap.get("grouptotalborrownode"));//借款单总额
		
		if (reviewAndDetailInfoMap!=null) {
			model.addAttribute("revCreateDate", reviewAndDetailInfoMap.get("createDate").subSequence(0, 19));//报批日期
			model.addAttribute("revBorrowRemark", reviewAndDetailInfoMap.get("borrowRemark"));//申报原因
			model.addAttribute("revBorrowAmount", reviewAndDetailInfoMap.get("borrowAmount"));//收据金额
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
		return "modules/visaborrowmoney/visaBorrowMoney4XinXingZheReviewDetail";
	}
	
	/**
	 * 签证借款审核：驳回 或 通过
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "review4XXZVisaBorrowMoney")
	public String review4XXZVisaBorrowMoney(Model model,HttpServletRequest request, HttpServletResponse response) {
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
			return visaBorrowMoneyReviewDetail(model, request, response);
		}
		
		/*
		 * 2015-05-07 wxw added
		 * 1.审核通过后在MoneyAmount中保存借款信息
		 * 2.处理与老数据兼容的相关问题：如果traveler的jkSerialNum为空，生成新的UUID并进行更新
		 * uuid取游客的jkSerialNum
		 */
		//UpdateReview返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
		int num = reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),denyReason);
		
		List<Review> list = reviewDao.findReviewActive(Long.parseLong(revid));
		VisaOrder visaOrder = visaOrderDao.findOne(Long.parseLong(list.get(0).getOrderId()));
		String jkSerialNum = UUID.randomUUID().toString();
		if (null==visaOrder.getJkSerialnum()||"".equals(null==visaOrder.getJkSerialnum())) {
			visaOrderService.updateOrderJkSerialnum(jkSerialNum,visaOrder.getId());
			visaOrder.setJkSerialnum(jkSerialNum);
		}
		
		//if(list.get(0).getTopLevel() == Integer.parseInt(nowLevel)){//如果当前层级为最高层级  说明该流程已经审核结束
			if (null!=visaOrder&&1==num&&"1".equals(result)) {
				//获取借款币种
				List<ReviewDetail> currencyId =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "currencyId");
				
				//获取借款金额
				List<ReviewDetail> borrowAmount =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "borrowAmount");
			    BigDecimal price = new BigDecimal(borrowAmount.get(0).getMyvalue());
			    
			    //获取订单借款汇率
			    Integer borrowtotalcurrencyId = 0;
			    if (null!=currencyId&&currencyId.size()>0) {
			    	borrowtotalcurrencyId = Integer.parseInt(currencyId.get(0).getMyvalue());
				}else{
					StringBuffer buffer = new StringBuffer();
					buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
					buffer.append(" AND c.create_company_id=");
					buffer.append(UserUtils.getUser().getCompany().getId());
					List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
					
					for (int i = 0; i < currencylist.size(); i++) {
						if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
							borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
							break;
						}
					}
				}
				
				
				MoneyAmount costMoneyAmount = new MoneyAmount(visaOrder.getJkSerialnum(), //款项UUID
						borrowtotalcurrencyId,//币种ID
						price,//相应币种的金额
						visaOrder.getId(), //借款订单或游客ID
			    		Context.MONEY_TYPE_JK, //款项类型: 借款
			    		Context.ORDER_TYPE_QZ,//订单类型
			    		VisaPreOrderController.BUSINDESS_TYPE_ORDER,//1表示订单，2表示游客
			    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
				costMoneyAmount.setReviewId(Long.parseLong(revid));
				moneyAmountService.addMoneyAmount(costMoneyAmount);
			    
			}
		//}
		return visaBorrowMoney4XXZReviewList(model, request, response);
	}
	
	
	/**
	 * 新行者签证费借款单 wangxinwei 2015年05月12日9:41:00
	 */
	@RequestMapping(value = "visaBorrowMoney4XXZFeePrint")
	public String visaBorrowMoney4XXZFeePrint(Model model,
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
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		String orderType = request.getParameter("orderType");
		String payStatus = null; //签证借款审核付款状态
		
		model.addAttribute("revid", revid);//把revid 传到模板模板的html页面，下载模板时使用
		model.addAttribute("payId", payId);
		model.addAttribute("orderType", orderType);
		model.addAttribute("option", option);
		
		// 签证借款申请相关信息
		String revCreateDate = "";
		String revUpdateDate = "";
		String groupTotalBorrowNode = "";
		String productCreater = "";
		String operaterName = "未知";
		String payDate = "";
		String revBorrowAmount = "";
		String revBorrowAmountDx = "";
		Currency revCurrency = null;
		
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		if (reviewAndDetailInfoMap != null) {
			revCreateDate = reviewAndDetailInfoMap.get("createDate");
			revUpdateDate = reviewAndDetailInfoMap.get("updateDate");
			groupTotalBorrowNode = reviewAndDetailInfoMap.get("grouptotalborrownode");
								
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			productCreater = visaProducts.getCreateBy().getName();
					
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));	
			if (null != user) {
				operaterName = user.getName();				
			} 
			
			payDate = reviewAndDetailInfoMap.get("updateDate");
			
			if("order".equals(option)) {
				Double borrowAmount = Double.valueOf(reviewAndDetailInfoMap.get("borrowAmount"));
				model.addAttribute("revBorrowAmount", MoneyNumberFormat.getThousandsMoney(borrowAmount, MoneyNumberFormat.THOUSANDST_POINT_TWO));// 借款金额
				model.addAttribute("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(borrowAmount, MoneyNumberFormat.POINT_TWO))));// 借款金额大写
			}
			
			String currencyId = reviewAndDetailInfoMap.get("currencyId");
			if (null != currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				revCurrency = currency;
			}
			
			//----- wxw added 20151008 -----需求C221， 新行者签证借款付款状态，payStatus：1 显示upDateDate时间，0不显示
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();	
			payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
					model.addAttribute("payStatus","0");
			}else {
				if(StringUtils.isNotBlank(payStatus)) {
					model.addAttribute("payStatus",payStatus);
				}else {
					model.addAttribute("payStatus","0");
				}
			}
		}
		
		model.addAttribute("revCreateDate",DateUtils.dateFormat(revCreateDate));// 填写日期
		//----- wxw added 20151008 -----单需求C221 ， 处理付款确认时间
		model.addAttribute("revUpdateDate",DateUtils.dateFormat(revUpdateDate));// 更新日期
		model.addAttribute("grouptotalborrownode", groupTotalBorrowNode);		// 申报原因（对新行者来说取总的备注信息）
		
		model.addAttribute("productCreater", productCreater);		
		model.addAttribute("operatorName", operaterName);						// 经办人、领款人都为借款申请人
		
		model.addAttribute("payDate", DateUtils.dateFormat(payDate));			// 付款日期
		
		model.addAttribute("revCurrency", revCurrency);
		
		if("pay".equals(option)) {
			if(StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, Context.ORDER_STATUS_VISA);
				if (payDetail != null) {
					if (Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != BigDecimal.ZERO.doubleValue()) {
						revBorrowAmount = payDetail.getRefundRMBDispStyle();
						revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.parseDouble(revBorrowAmount.replaceAll(",", "")));// 借款金额大写
					}
				}
			}
			
			model.addAttribute("revBorrowAmount", revBorrowAmount);
			model.addAttribute("revBorrowAmountDx", revBorrowAmountDx);
		}
								
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));		
			
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY,reviewLogs);
		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			model.addAttribute("cw", jobtypeusernameMap.get(8));
		}else {
			model.addAttribute("cw", "");
		}
		
		//lihong  123
		//2015-04-09王新伟添加
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
		
		return "modules/visaborrowmoney/visaBorrowMoney4XXZFeePrint";
	}
	
	
	@RequestMapping(value="downloadVisaBorrowMoney4XXZSheet")
	public ResponseEntity<byte[]> downloadVisaBorrowMoney4XXZSheet(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String revid = request.getParameter("revid");
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		File file = visaBorrowMoneyService.createVisaBorrowMoney4XXZSheetDownloadFile(Long.parseLong(revid), payId, option);
		
		//签证费借款单生成后,更新借款单 的  打印状态和首次打印时间
/*		if(file != null && file.exists()){
			visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid));
		} */
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "签证费借款单" + nowDate + ".doc";
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
	 * 签证借款去取消
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "cancelVisaBorrowMOney")
	@Transactional
	public Map<String, Object> cancelVisaBorrowMOney(HttpServletRequest request,
		HttpServletResponse response) throws JSONException {
		
		Map<String, Object> map = null;
		
		String revId = request.getParameter("revId");
		
		reviewDao.removeMyReview(Long.parseLong(revId));
		
		map =new HashMap<String, Object>();
		
		map.put("success", "取消成功！");
		
		return map;
		
	}
		
	
	
	
	/*********************   新行者借款相关     结束       *********************/
	/*********************   新行者借款相关     结束       *********************/
	/*********************   新行者借款相关     结束       *********************/
	
	
	
	//--------------------------------------------------------------------------------------------------------
	/*********************   批次借款审核相关     开始       *********************/
	/*********************   批次借款审核相关     开始       *********************/
	/*********************   批次借款审核相关     开始       *********************/
	
	/**
	 * 2015-05-26
	 * 签证借款申请批次审批列表
	 * 
	 * xinwei.wang added
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaBorrowMoneyBatchReviewList")
	public String visaBorrowMoneyBatchReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
	    // 按点击数倒序 
		//sortList(userJobs);
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
		
		Page<Map<String, Object>> page = null;
		if (userJobs==null||userJobs.size()<1) {
			page = visaBorrowMoneyService.queryVisaBorrowMoneyBatchReviewInfo(request, response, "0");
		}else {
			//如果默认相关审核流程有多个审核职位，给一个默认职位已确认一个审核层级
			page = visaBorrowMoneyService.queryVisaBorrowMoneyBatchReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
		}
		
		model.addAttribute("page", page);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		//处理页面从菜单进入审核列表时  默认角色的处理
		if (null==userJobId) {
			if (null!=userJobs&&userJobs.size()>0) {
				conditionsMap.put("userJobId",  userJobs.get(0).getId()+"");
			}
		}else {
			conditionsMap.put("userJobId",userJobId);
		}
		/**
		 * 测试URL
		 * http://localhost:8080/trekiz_wholesaler_tts/a/visa/workflow/borrowmoney/visaBorrowMoneyReviewList?flowType=5
		 */
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
  /*      Collections.sort(userJobs, new Comparator<UserJob>() {  
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
        });*/
		
		//---wxw added 处理批量审核时角色显示的待审核记录不正确的问题 2015-09-25 ---
		//用新查询的值替换原有接口的角色待审核数
        for (UserJob userJob : userJobs) {
        	int userjobReviewCount = visaBorrowMoneyService.getJobReviewCountbyUserJob(userJob);
        	userJob.setCount(userjobReviewCount);
		}
		model.addAttribute("userJobs", userJobs); //部门 + 下划线 + 职务，即 UserJob 类中的 deptName_jobName, 例如： “北京分公司签证部_计调（机票）”
		
		//查询可以办理签证产品的国家信息，包含国家表主键和中文名称
		List<Object[]> countryInfoList = visaProductsService.findCountryInfoList();
		model.addAttribute("countryInfoList", countryInfoList);
		model.addAttribute("userid", UserUtils.getUser().getCompany().getId()); 
		//System.out.println(UserUtils.getUser().getId()+"==========="+UserUtils.getUser().getLoginName()+"===="+UserUtils.getUser().getCompany().getId());
		return "modules/visaborrowmoney/visaBorrowMoneyBatchReviewList";
	}
	
	
	/**
	 * 签证借款批量审批详情页 
	 * wangxinwei
	 *  2015年05月26日20:11:00
	 */
	@RequestMapping(value = "visaBorrowMoneyBatchReviewDetail")
	public String visaBorrowMoneyBatchReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		String batchno = request.getParameter("batchno");
		
	 /*   String refundDate =null;	*/
		
		String fromflag = request.getParameter("fromflag");
		
		
		/*SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid,
		  rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername
		FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr
		WHERE rev.id = revd.review_id AND revd.myKey = 'visaBorrowMoneyBatchNo' AND revd.myValue= vfbo.batch_no
		AND vfbo.busyness_type = 2 AND rev.travelerId = tr.id AND vfbo.batch_no='20150326-0003'*/
		
	
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
		buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
		buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");
		buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaBorrowMoneyBatchNo' AND revd.myValue= vfbo.batch_no ");
		buffer.append(" AND vfbo.busyness_type = 2 AND rev.travelerId = tr.id AND vfbo.batch_no=");
		buffer.append("'"+batchno+"'");
		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		//Map<String, Object>  mp =  list.get(0);
		//totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ct[i]);
		
		StringBuilder revids = new StringBuilder("");
		StringBuilder remarks = new StringBuilder("");
		for (Map<String, Object> map : list) {
			//签证借款申请相关信息
			Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong((Integer)map.get("reviewid")+""));
			map.put("borrowAmount", reviewAndDetailInfoMap.get("borrowAmount"));
			map.put("borrowRemark", reviewAndDetailInfoMap.get("borrowRemark"));
			revids.append(reviewAndDetailInfoMap.get("id")).append(",");
			remarks.append(reviewAndDetailInfoMap.get("borrowRemark")).append(",");
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
		model.addAttribute("borrowinfolist", list);
		model.addAttribute("batchno",batchno);
		model.addAttribute("revids",revids.toString());
		model.addAttribute("remarks",remarks.toString());
		model.addAttribute("fromflag",fromflag);
		
		//270-qyl-begin-签证批量借款还款日期的详情展示
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT refund_date FROM  visa_flow_batch_opration WHERE busyness_type = 2  AND  batch_no =");
		sql.append("'"+batchno+"'");
		List<Object>refundDate1=visaOrderDao.findBySql(sql.toString());
	    model.addAttribute("refundDate",refundDate1.get(0));
		//270-qyl-end
		return "modules/visaborrowmoney/visaBorrowMoneyBatchReviewDetail";
	}
	
	
	

	/**
	 * 按批次号审批
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "batchReviewVisaBorrowMoneybyBatchNo")
	@Transactional
	public String batchReviewVisaBorrowMoneybyBatchNo(Model model,HttpServletRequest request, HttpServletResponse response) {
		
        String result =  request.getParameter("result"); //1审核通过，0，驳回
		//String remarks = request.getParameter("remarks");
       // String[] remarksarray = remarks.split(",");
        String remark = request.getParameter("denyReason");//批量审核驳回原因
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
		 * 2015-04-20王新伟添加
		 * 签证借款批量审核：
		 * 
		 */
		for (int i = 0; i < revidsarray.length; i++) {
			StringBuffer reply = new StringBuffer();
			if (result == null || "".equals(result)) {
				reply.append("审批结果不能为空");
			}
			
			String revid = revidsarray[i];
			
			
			/**
			 * 2015-05-07 wxw added
			 * 1.审核通过后在MoneyAmount中保存借款信息
			 * 2.处理与老数据兼容的相关问题：如果traveler的jkSerialNum为空，生成新的UUID并进行更新
			 * uuid取游客的jkSerialNum
			 */
			//UpdateReview返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
			int num = reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),remark);
			List<Review> list = reviewDao.findReviewActive(Long.parseLong(revid));
			Traveler traveler = travelerService.findTravelerById(list.get(0).getTravelerId());
			String jkSerialNum = UUID.randomUUID().toString();
			//如果游客没有添加借款序列号，则生成，否则取已有序列号
			if (null==traveler.getJkSerialNum()||"".equals(traveler.getJkSerialNum())) {
				travelerService.updateJkSerialNumByTravelerId(jkSerialNum,traveler.getId());
				traveler.setJkSerialNum(jkSerialNum);
			}
			
			//if(list.get(0).getTopLevel() == Integer.parseInt(nowLevel)){//如果当前层级为最高层级  说明该流程已经审核结束
				if (null!=traveler&&1==num&&"1".equals(result)) {
					//获取借款币种
					List<ReviewDetail> currencyId =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "currencyId");
					//获取借款金额
					List<ReviewDetail> borrowAmount =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "borrowAmount");
				    BigDecimal price = new BigDecimal(borrowAmount.get(0).getMyvalue());
				    
					MoneyAmount costMoneyAmount = new MoneyAmount(traveler.getJkSerialNum(), //款项UUID
							Integer.parseInt(currencyId.get(0).getMyvalue()),//币种ID
							price,//相应币种的金额
				    		traveler.getId(), //订单或游客ID
				    		Context.MONEY_TYPE_JK, //款项类型: 借款
				    		Context.ORDER_TYPE_QZ,//订单类型
				    		VisaPreOrderController.BUSINDESS_TYPE_TRAVELER,//1表示订单，2表示游客
				    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
					
					costMoneyAmount.setReviewId(Long.parseLong(revid));
				    moneyAmountService.addMoneyAmount(costMoneyAmount);
				    batchreviewstaus = true;
				}
			//}
		}
		
		//更新批次审批状态
		if (batchreviewstaus) {
			visaFlowBatchOprationDao.updateVisaFlowBatchOprationStatusByBatchNo(batchno, "2");
		}
		
		String fromflag = request.getParameter("fromflag");
		if ("CW".equalsIgnoreCase(fromflag)) {
			return visaBorrowMoneyBatchReviewList4CW(model, request, response);
		}else {
			return visaBorrowMoneyBatchReviewList(model, request, response);
		}
		
		
	
	}
	
	
	/**
	 * 签证费借款单 wangxinwei 2015年5月29日14:27:00
	 */
	@RequestMapping(value = "visaBorrowMoneyBatchFeePrint")
	public String visaBorrowMoneyBatchFeePrint(Model model,
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
		String isPrintFlag = request.getParameter("isPrintFlag");
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		String payStatus = null;
	
		
		model.addAttribute("revid", revid);//吧revid 传到模板模板的html页面，下载模板时使用
		model.addAttribute("batchno", batchno);
		model.addAttribute("isPrintFlag", isPrintFlag);
		model.addAttribute("payId", payId);
		model.addAttribute("option", option);
				
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		if (reviewAndDetailInfoMap != null) {
			model.addAttribute("revCreateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));// 填写日期
			
			//----- wxw added 20151008 ----- 需求C221， 处理付款确认时间
			model.addAttribute("revUpdateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate")));// 更新日期
			
			model.addAttribute("revBorrowRemark",reviewAndDetailInfoMap.get("borrowRemark"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			model.addAttribute("productCreater", productCreater);
			
			
			if (null != user) {
				model.addAttribute("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				model.addAttribute("operatorName", "未知");
			}
			model.addAttribute("payDate", DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate")));// 付款日期
			String currencyId = reviewAndDetailInfoMap.get("currencyId");
			if (null != currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);
			}
			
			if("order".equals(option)) {
				//获取批次借款金额
				VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"2");
				String batchborrowtotalMoney = visaFlowBatchOpration.getBatchTotalMoney();
				
				model.addAttribute("revBorrowAmount", MoneyNumberFormat.getThousandsMoney(Double.parseDouble(batchborrowtotalMoney), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 借款金额   改为批次总金额
				model.addAttribute("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(
						MoneyNumberFormat.getThousandsMoney(Double.parseDouble(batchborrowtotalMoney), MoneyNumberFormat.POINT_TWO))));// 借款金额大写  改为批次总金额
			}
			

			//----- wxw added 20151008 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid)||Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				model.addAttribute("payStatus","0");
			}else {
				if(StringUtils.isNotBlank(payStatus)) {
					model.addAttribute("payStatus",payStatus);
				}else {
					model.addAttribute("payStatus","0");
				}
			}	
		}
		
		if("pay".equals(option)) {
			//45需求，借款金额以每次的支付金额为标准
			String revBorrowAmount = "";
			String revBorrowAmountDx = "";
			if(StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, Context.ORDER_STATUS_VISA);
				if (payDetail != null) {
					if (Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != BigDecimal.ZERO.doubleValue()) {
						revBorrowAmount = payDetail.getRefundRMBDispStyle();
						revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.parseDouble(revBorrowAmount.replaceAll(",", "")));// 借款金额大写
					}
				}
			}
			model.addAttribute("revBorrowAmount", revBorrowAmount);
			model.addAttribute("revBorrowAmountDx", revBorrowAmountDx);
		}
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY,reviewLogs);
		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			model.addAttribute("cw", jobtypeusernameMap.get(8));
		}else {
			model.addAttribute("cw", "");
		}
		
		//lihong  123
		//2015-04-09王新伟添加
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
		
		Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&(null==review.getPrintFlag()||0==review.getPrintFlag())) { 
			Date printDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(printDate);
			model.addAttribute("printDate",printDateStr );
		}else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = null;
			try {
				   printDateStr = simpleDateFormat.format(review.getPrintTime());
			} catch (Exception e) {
				e.printStackTrace();
				printDateStr = simpleDateFormat.format(new Date()); 
			}
			
			model.addAttribute("printDate",printDateStr);
		}
		
		/**
		 * wangxinwei 20151102 added
		 * 解决签证借款打印（批次）申请原因只能显示一个游客申请原因的问题
		 */
		String remarkString = visaBorrowMoneyService.getVisaBorrowBatchPrintAppReason(batchno);
		model.addAttribute("revBorrowRemark",remarkString);
		
		return "modules/visaborrowmoney/visaBorrowMoneyBatchFeePrint";
	}
	
	/**
	 * 通过批次号查询该批次下游客的信息(针对签证借款)
	 * @author jiachen
	 * @DateTime 2015年5月28日 上午10:07:17
	 * @param batchNo
	 * @return List<Map<String, String>>
	 */
	@ResponseBody
	@RequestMapping("getTravelerList")
	public List<Map<String, String>> getTravelerList(String batchNo, String busynessType) {
		List<Map<String, String>> travelerList = null;
				if(StringUtils.isNotBlank(batchNo)) {
					travelerList = new ArrayList<Map<String, String>>();
					if("1".equals(busynessType)) {
						visaBorrowMoneyService.getTravelerListForReturnReceipt(batchNo, travelerList);
					}else if("2".equals(busynessType)) {
						visaBorrowMoneyService.getTravelerList(batchNo, travelerList);
					}
				}
		return travelerList;
	}
	
	/**
	 * 签证费批次借款单: 在点击打印或下载按钮时更新打印状态  和   打印时间
	 * wangxinwei 2015年05月29日15:53:00
	 */
	@RequestMapping(value = "visaBorrowMoneyBatchFeePrintAjax")
	@ResponseBody
	@Transactional
	public Map<String, Object> visaBorrowMoneyBatchFeePrintAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//String revid = request.getParameter("revid");
		
		String printDatestr = request.getParameter("printDate");
		String batchno = request.getParameter("batchno");
		VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"2");
		
		visaFlowBatchOprationDao.updateVisaFlowBatchUpdateTime(batchno, "2", 
									new Date(), UserUtils.getUser().getId());
		//第一次打印需要更新状态
        if (null!=visaFlowBatchOpration&&!"1".equals(visaFlowBatchOpration.getPrintStatus())) {
    		
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
    		buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
    		buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");
    		buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaBorrowMoneyBatchNo' AND revd.myValue= vfbo.batch_no ");
    		buffer.append(" AND vfbo.busyness_type = 2 AND rev.travelerId = tr.id AND vfbo.batch_no=");
    		buffer.append("'"+batchno+"'");
    		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
    		
    		//Date printDate = new Date();
    		//model.addAttribute("printDate",printDate );
    		Date printDate = null;
    		try {
    			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
    			printDate = simpleDateFormat.parse(printDatestr);
    			
    			if (null!=list&&list.size()>0) {
    				for (Map<String, Object> map2 : list) {
    					visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong((Integer)map2.get("reviewid")+""),printDate);
    				}
    				visaFlowBatchOprationDao.updateVisaFlowBatchPrintTimeAndPrintStatus(batchno, "2", printDate);
    			}
    			
    		} catch (NumberFormatException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			log.error("签证费借款单日期格式化错", e);
    			throw e;
    		} catch (Exception e) {
    			map.put("result",2);
    			e.printStackTrace();
    		}	
			
		}
			
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}

	/**
	 * 签证借款批次审批导出游客信息
	 * @author jiachen
	 * @DateTime 2015年5月29日 上午10:51:06
	 * @return void
	 */
	@RequestMapping("exportTravelerInfo")
	public void exportTravelerInfo(String batchNo, HttpServletRequest request, HttpServletResponse response) {
		visaBorrowMoneyService.exportTravelerInfo(batchNo, request, response);
	}
	
	/**
	 * wxw added 2015-05-29
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value="batchdownloadVisaBorrowMoneySheet")
	public ResponseEntity<byte[]> batchdownloadVisaBorrowMoneySheet(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String revid = request.getParameter("revid");
		String batchno = request.getParameter("batchno");
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		File file = visaBorrowMoneyService.createBatchVisaBorrowMoneySheetDownloadFile(Long.parseLong(revid),batchno, payId, option);
		
		//签证费借款单生成后,更新借款单 的  打印状态和首次打印时间
/*		if(file != null && file.exists()){
			visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid));
		} */
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "签证费借款单" + nowDate + ".doc";
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
	 * wangxinwei 2015年06月01日21:23:00
	 */
	@RequestMapping(value = "visaBorrowMoneyBatchCancelAjax")
	@ResponseBody
	public Map<String, Object> visaBorrowMoneyBatchCancelAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		String batchno = request.getParameter("batchno");
		int gobackresult = 0;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid, rev.orderId AS orderid, ");
			buffer.append(" rev.travelerId AS travelerid, rev.nowLevel as curentlevel, tr.`name` AS travelername ");
			buffer.append("FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr ");
			buffer.append("WHERE rev.id = revd.review_id AND revd.myKey = 'visaBorrowMoneyBatchNo' AND revd.myValue= vfbo.batch_no ");
			buffer.append(" AND vfbo.busyness_type = 2 AND rev.travelerId = tr.id AND vfbo.batch_no=");
			buffer.append("'"+batchno+"'");
			List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
			
			if (null!=list&&list.size()>0) {
				for (Map<String, Object> map2 : list) {
					//审核回退
					gobackresult = reviewService.CancelReview(Long.parseLong((Integer)map2.get("reviewid")+""));
				}
			}
		}catch (Exception e) {
			map.put("result",2);
			e.printStackTrace();
			return map;
		}	
		
		if (1==gobackresult) {
			map.put("result",1);
		}else {
			map.put("result",2);
		}
		
		//1成功   2为申请失败
		
		return map;
	}
	
	
	
	/*********************   批次借款审核相关     结束       *********************/
	/*********************   批次借款审核相关     结束      *********************/
	/*********************   批次借款审核相关     结束       *********************/
	

	/**
	 * 审核回退：根据revid进行批次回退操作,即返回上一级审核
	 * 规则如下：
	 * 1.第一层级 和 最后一层级没有退回操作
	 * 2.隔级审核后不能再进行退回操作
	 * 3.只有审核中状态的才可能进行退回操作
	 * wangxinwei 2015年06月01日21:23:00
	 */
	@RequestMapping(value = "visaBorrowMoneyCancelAjax")
	@ResponseBody
	public Map<String, Object> visaBorrowMoneyCancelAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		String revid = request.getParameter("revid");
		int cancelResult=0;
		try {
			//审核回退
		    if(null!=revid) {
		    	cancelResult=reviewService.CancelReview(Long.parseLong(revid));
			}
				
		}catch (Exception e) {
			map.put("result",2);
			e.printStackTrace();
			return map;
		}	
		if(1==cancelResult){
			map.put("result",1);//1成功   2为申请失败
		}else {
			map.put("result",2);//1成功   2为申请失败
		}
		
		return map;
	}
	
	
	
	//--------------签证批量借款财务审核列表开始--------------
	/**
	 * 2015-05-26
	 * 签证借款申请批次审批列表
	 * 
	 * xinwei.wang added
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaBorrowMoneyBatchReviewList4CW")
	public String visaBorrowMoneyBatchReviewList4CW(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
	    // 按点击数倒序 
		sortList(userJobs);
		
		Page<Map<String, Object>> page = null;
		if (userJobs==null||userJobs.size()<1) {
			page = visaBorrowMoneyService.queryVisaBorrowMoneyBatchReviewInfo(request, response, "0");
		}else {
			//如果默认相关审核流程有多个审核职位，给一个默认职位已确认一个审核层级
			page = visaBorrowMoneyService.queryVisaBorrowMoneyBatchReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
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
		/**
		 * 测试URL
		 * http://localhost:8080/trekiz_wholesaler_tts/a/visa/workflow/borrowmoney/visaBorrowMoneyReviewList?flowType=5
		 */
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
  /*      Collections.sort(userJobs, new Comparator<UserJob>() {  
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
        });*/
		//---wxw added 处理批量审核时角色显示的待审核记录不正确的问题 2015-09-25 ---
		//用批量待审核记录数代替原有  接口  审核角色待审核数
        for (UserJob userJob : userJobs) {
        	int userjobReviewCount = visaBorrowMoneyService.getJobReviewCountbyUserJob(userJob);
        	userJob.setCount(userjobReviewCount);
		}
		model.addAttribute("userJobs", userJobs); //部门 + 下划线 + 职务，即 UserJob 类中的 deptName_jobName, 例如： “北京分公司签证部_计调（机票）”
		return "modules/visaborrowmoney/visaBorrowMoneyBatchReviewList4CW";
	}
	
	
	//--------------签证批量借款财务审核列表结束--------------
	
	
	//-------------------多项选择审批   开始---------------------
	/**
	 * wxw added 2015-08-11
	 * 同时审批多个批次签证借款   不定个数借款批次审核
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "multiBatchReviewVisaBorrowMoneybyBatchNo")
	public Object multiBatchReviewVisaBorrowMoneybyBatchNo(Model model,HttpServletRequest request, HttpServletResponse response) {
		
        String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		String batchnons = request.getParameter("batchnons");
		
		boolean isSuccess = visaBorrowMoneyService.multiBatchReviewVisaBorrowMoneybyBatchNo(result, remarks, batchnons);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (isSuccess) {
			resultMap.put("msg", "操作成功！");
		}else {
			resultMap.put("msg", "操作失败！");
		}
		return resultMap;
		 
	}   
	
	
	/**
	 * wxw added 2015-08-13
	 * 新型者借款   不定个数多选审核审核
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "multiReview4XXZVisaBorrowMoney")
	public Object multiReview4XXZVisaBorrowMoney(Model model,HttpServletRequest request, HttpServletResponse response) {
		
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
			 * 2015-08-13 wxw added 不定量多个还签证收据同时审批
			 */
			/*
			 * 2015-05-07 wxw added
			 * 1.审核通过后在MoneyAmount中保存借款信息
			 * 2.处理与老数据兼容的相关问题：如果traveler的jkSerialNum为空，生成新的UUID并进行更新
			 * uuid取游客的jkSerialNum
			 */
			//UpdateReview返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
			int num = reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),remarks);
			
			List<Review> list = reviewDao.findReviewActive(Long.parseLong(revid));
			VisaOrder visaOrder = visaOrderDao.findOne(Long.parseLong(list.get(0).getOrderId()));
			String jkSerialNum = UUID.randomUUID().toString();
			if (null==visaOrder.getJkSerialnum()||"".equals(null==visaOrder.getJkSerialnum())) {
				visaOrderService.updateOrderJkSerialnum(jkSerialNum,visaOrder.getId());
				visaOrder.setJkSerialnum(jkSerialNum);
			}
			
			if (null!=visaOrder&&1==num&&"1".equals(result)) {
				//获取借款币种
				List<ReviewDetail> currencyId =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "currencyId");
				
				//获取借款金额
				List<ReviewDetail> borrowAmount =reviewDetailDao.findReviewDetailByMykey(Long.parseLong(revid), "borrowAmount");
			    BigDecimal price = new BigDecimal(borrowAmount.get(0).getMyvalue());
			    
			    //获取订单借款汇率
			    Integer borrowtotalcurrencyId = 0;
			    if (null!=currencyId&&currencyId.size()>0) {
			    	borrowtotalcurrencyId = Integer.parseInt(currencyId.get(0).getMyvalue());
				}else{
					StringBuffer buffer = new StringBuffer();
					buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
					buffer.append(" AND c.create_company_id=");
					buffer.append(UserUtils.getUser().getCompany().getId());
					List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
					
					for (int j = 0; j < currencylist.size(); j++) {
						if ("￥".equals(currencylist.get(j).get("currency_mark"))
								||"人民币".equals(currencylist.get(j).get("currency_name"))) {
							borrowtotalcurrencyId = (Integer)currencylist.get(j).get("currency_id");
							break;
						}
					}
				}
				
				
				MoneyAmount costMoneyAmount = new MoneyAmount(visaOrder.getJkSerialnum(), //款项UUID
						borrowtotalcurrencyId,//币种ID
						price,//相应币种的金额
						visaOrder.getId(), //借款订单或游客ID
			    		Context.MONEY_TYPE_JK, //款项类型: 借款
			    		Context.ORDER_TYPE_QZ,//订单类型
			    		VisaPreOrderController.BUSINDESS_TYPE_ORDER,//1表示订单，2表示游客
			    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
				costMoneyAmount.setReviewId(Long.parseLong(revid));
				moneyAmountService.addMoneyAmount(costMoneyAmount);
			}
		 }
		
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("msg", "操作成功！");
		 return resultMap;
		 
	}
	
	//-------------------多项选择审批   结束---------------------
	
}
