package com.trekiz.admin.modules.order.airticket.web;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.order.entity.OrderExitGroupReviewVO;
import com.trekiz.admin.modules.order.rebates.entity.Rebates;
import com.trekiz.admin.modules.order.rebates.service.RebatesService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.review.airticketreturn.service.IAirTicketReturnService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 机票返佣审核
 * 
 * @author wangxv
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/airticketRebates")
public class AirticketRebatesController {

	protected static final String LIST_PAGE = "modules/review/airticketRebatesList";
	protected static final String DETAIL_PAGE = "modules/review/airticketRebatesDetail";
	protected static final String REVIEW_PAGE = "modules/review/airticketRebatesReview";
	@Autowired
	private CurrencyService currencyService;                            
	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	private OrderReviewService orderReviewService;
	@Autowired
	private RebatesService rebatesService;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	@Qualifier("activityGroupSyncService")
	private IActivityGroupService activityGroupService;
	@Autowired
	private IAirTicketReturnService airTicketReturnService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private SystemService systemService;

	/**
	 * add by wangxv 跳转到机票返佣列表
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getAirticketRebatesList")
	public String getAirticketRebatesList(
			@ModelAttribute OrderExitGroupReviewVO vo, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		/*List<UserJob> userJobs = reviewCommonService.getReviewByFlowType(
		Context.REBATES_FLOW_TYPE, Context.ORDER_TYPE_JP);*/
		Object obj = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(
				Context.REBATES_FLOW_TYPE);
		
		List <UserJob> userJobList = Lists.newArrayList();
		
		Page<Map<Object, Object>> pageInfo = orderReviewService
				.queryActivityRebatesList(new Page<Map<Object, Object>>(request, response), vo, userJobs);
		// 获取页面上需要展示的字段信息,合并相同的币种数据
		if (pageInfo != null && pageInfo.getList() != null
				&& pageInfo.getList().size() > 0) {
			for (Map<Object, Object> map : pageInfo.getList()) {
				String id = "";
				String rebatesDiff ="";
				String currencyName = "";
				List<Rebates> rebates = rebatesService
						.findRebatesListByRid(Long.parseLong(map.get("rid").toString()));
				if(CollectionUtils.isNotEmpty(rebates)){
				Map<Long, List<Rebates>> groupMap = new HashMap<Long, List<Rebates>>();
					for (Rebates rebate : rebates) {
						id += rebate.getId() + ",";
						if (groupMap.containsKey(rebate.getCurrencyId())) {
							groupMap.get(rebate.getCurrencyId()).add(rebate);
						} else {
							List<Rebates> glist = new ArrayList<Rebates>();
							glist.add(rebate);
							groupMap.put(rebate.getCurrencyId(), glist);
						}
					}
				for (Map.Entry<Long, List<Rebates>> entry : groupMap.entrySet()) {
					BigDecimal amount = new BigDecimal("0");
					currencyName += entry.getKey()+ ",";
					List<Rebates> ls=entry.getValue(); 
					for(Rebates reb : ls ){
						amount = amount.add(reb.getRebatesDiff());
					}
					rebatesDiff +=amount +",";
				}
				obj = map.get("createDate");
				if(obj!=null){
					Date createDate = (Date)obj;
					map.put("createDate", sdf.format(createDate));
				}
				
				map.put("id", id.substring(0, id.length() - 1)); // 获取order_rebates表主键ID
				map.put("costname", rebates.get(0).getCostname()); // 获取款项
				map.put("rebatesDiff",
						rebatesDiff.substring(0, rebatesDiff.length() - 1));// 获取返佣差额
				map.put("currencyName", currencyName.substring(0, currencyName.length() - 1));
				AirticketOrder order = airticketOrderDao.getAirticketOrderById(rebates.get(0).getOrderId());
				map.put("groupid", order.getAirticketId());
				map.put("orderStatus", order.getProductTypeId());
				map.put("orderNo", order.getOrderNo());
				map.put("totalMoney", order.getTotalMoney());
				map.put("payedMoney", order.getPayedMoney());
				map.put("accountedMoney", order.getAccountedMoney());
				/**
				 * add by ruyi.chen
				 * 增加订单预计返佣金额展示
				 * add date 2015-08-18
				 */
				String rebatesStr = OrderCommonUtil.getAllRebatesMoney(rebates.get(0).getOrderId(), 7);
				if(null == rebatesStr || 0 == rebatesStr.trim().length()){
					map.put("rebatesSign", 0);
				}else{
					map.put("rebatesSign", 1);
				}
				map.put("rebatesStr", rebatesStr);
				}
			}
		}
		vo.setOrderType("7");//机票产品
		if(null != userJobs && 0 < userJobs.size()){
			for( UserJob uj : userJobs){
				if(uj.getOrderType().toString().equals(vo.getOrderType())){
					userJobList.add(uj);
				}
			}
		}	
		model.addAttribute("paymentProject", "机票返佣");
		model.addAttribute("page", pageInfo);
		//model.addAttribute("userJobs", userJobs);
		model.addAttribute("userJobs", userJobList);
		model.addAttribute("reviewVO", vo);
		model.addAttribute("users", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		return LIST_PAGE;
	}
	/**
	 * add by wangXK 返佣审核机票------迁移到财务审核下面
	 */
	@RequestMapping(value = "queryAirticketRebatesList")
	public String queryAirticketRebatesList(@ModelAttribute OrderExitGroupReviewVO vo, Model model,HttpServletRequest request, HttpServletResponse response) {
		Object obj = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//String printFlag = request.getParameter("printFlag");
		//vo.setPrintFlag(printFlag);
		
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REBATES_FLOW_TYPE);//用户角色
		List <UserJob> userJobList = Lists.newArrayList();
		Map<String,String>printMap  = new LinkedHashMap<String,String>();
		printMap.put("1", "已打印");
		printMap.put("2", "未打印");
		Page<Map<Object, Object>> pageInfo = orderReviewService
				.queryActivityRebatesList(new Page<Map<Object, Object>>(request, response), vo, userJobs);
		// 获取页面上需要展示的字段信息,合并相同的币种数据
		if (pageInfo != null && pageInfo.getList() != null
				&& pageInfo.getList().size() > 0) {
			for (Map<Object, Object> map : pageInfo.getList()) {
				String id = "";
				String rebatesDiff ="";
				String currencyName = "";
				List<Rebates> rebates = rebatesService
						.findRebatesListByRid(Long.parseLong(map.get("rid").toString()));
				if(CollectionUtils.isNotEmpty(rebates)){
				Map<Long, List<Rebates>> groupMap = new HashMap<Long, List<Rebates>>();
					for (Rebates rebate : rebates) {
						id += rebate.getId() + ",";
						if (groupMap.containsKey(rebate.getCurrencyId())) {
							groupMap.get(rebate.getCurrencyId()).add(rebate);
						} else {
							List<Rebates> glist = new ArrayList<Rebates>();
							glist.add(rebate);
							groupMap.put(rebate.getCurrencyId(), glist);
						}
					}
				for (Map.Entry<Long, List<Rebates>> entry : groupMap.entrySet()) {
					BigDecimal amount = new BigDecimal("0");
					currencyName += entry.getKey()+ ",";
					List<Rebates> ls=entry.getValue(); 
					for(Rebates reb : ls ){
						amount = amount.add(reb.getRebatesDiff());
					}
					rebatesDiff +=amount +",";
				}
				obj = map.get("createDate");
				if(obj!=null){
					Date createDate = (Date)obj;
					map.put("createDate", sdf.format(createDate));
				}
				
				map.put("id", id.substring(0, id.length() - 1)); // 获取order_rebates表主键ID
				map.put("costname", rebates.get(0).getCostname()); // 获取款项
				map.put("rebatesDiff",
						rebatesDiff.substring(0, rebatesDiff.length() - 1));// 获取返佣差额
				map.put("currencyName", currencyName.substring(0, currencyName.length() - 1));
				AirticketOrder order = airticketOrderDao.getAirticketOrderById(rebates.get(0).getOrderId());
				map.put("groupid", order.getAirticketId());
				map.put("orderStatus", order.getProductTypeId());
				map.put("orderNo", order.getOrderNo());
				map.put("totalMoney", order.getTotalMoney());
				map.put("payedMoney", order.getPayedMoney());
				map.put("accountedMoney", order.getAccountedMoney());
				/**
				 * add by ruyi.chen
				 * 增加订单预计返佣金额展示
				 * add date 2015-08-18
				 */
				String rebatesStr = OrderCommonUtil.getAllRebatesMoney(rebates.get(0).getOrderId(), 7);
				if(null == rebatesStr || 0 == rebatesStr.trim().length()){
					map.put("rebatesSign", 0);
				}else{
					map.put("rebatesSign", 1);
				}
				map.put("rebatesStr", rebatesStr);
				}
			}
		}
		vo.setOrderType("7");//机票产品
		//vo.setPrintFlag(printFlag);
		if(null != userJobs && 0 < userJobs.size()){
			for( UserJob uj : userJobs){
				if(uj.getOrderType().toString().equals(vo.getOrderType())){
					userJobList.add(uj);
				}
			}
		}	
		model.addAttribute("printMap", printMap);
		model.addAttribute("paymentProject", "机票返佣");
		model.addAttribute("page", pageInfo);
		//model.addAttribute("userJobs", userJobs);
		model.addAttribute("userJobs", userJobList);
		model.addAttribute("reviewVO", vo);
		model.addAttribute("users", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		return "modules/review/airticketRebatesListQuery";
	}
	/**
	 * 跳转到返佣详情界面
	 * 
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value = "showAirticketRebatesDetail/{reviewId}")
	public String showRebatesReviewDetail(@PathVariable Long reviewId,
			Model model, HttpServletRequest request) {
		getDetailList(reviewId, model);
		return DETAIL_PAGE;
	}

	/**
	 * 跳转到机票返佣审核界面
	 * 审核相关操作都在原rebatesreviewcontrol中
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="showAirticketRebatesReview/{reviewId}/{userLevel}")
	public String showRebatesReview(@PathVariable Long reviewId,@PathVariable Integer userLevel, Model model, HttpServletRequest request){
		getDetailList(reviewId, model);
		model.addAttribute("userLevel", userLevel);
		return REVIEW_PAGE;
	}
	/**
	 * 返佣详情和审核页面公用方法
	 * @param reviewId
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	private void getDetailList(Long reviewId, Model model) {
		List<Rebates> rebates = rebatesService.findRebatesListByRid(reviewId);
		AirticketOrder order = airticketOrderDao.getAirticketOrderById(rebates.get(0).getOrderId());
		String currencyName = ""; String rebatesDiff = "";
		if(CollectionUtils.isNotEmpty(rebates)){
			Map<Long, List<Rebates>> groupMap = new HashMap<Long, List<Rebates>>();
			for (Rebates rebate : rebates) {
				if (groupMap.containsKey(rebate.getCurrencyId())) {
					groupMap.get(rebate.getCurrencyId()).add(rebate);
				} else {
					List<Rebates> glist = new ArrayList<Rebates>();
					glist.add(rebate);
					groupMap.put(rebate.getCurrencyId(), glist);
				}
				
				/**
				 * add by ruyi.chen
				 * add date 2015-08-19
				 * 根据不同返佣申请类型加载对应类型的预计返佣金额
				 */
				String travelerId = null;
				if(null != rebate.getTravelerId() && 0 < rebate.getTravelerId()){
					travelerId = rebate.getTravelerId().toString();
				}
				String rebatesStr = OrderCommonUtil.getRebatesMoney(order.getId(), 7, travelerId);
				if(null == rebatesStr || 0 == rebatesStr.trim().length()){
					rebate.setRebatesSign(0);
				}else{
					rebate.setRebatesSign(1);
				}
				rebate.setRebatesStr(rebatesStr);
				
			}
			for (Map.Entry<Long, List<Rebates>> entry : groupMap.entrySet()) {
				BigDecimal amount = new BigDecimal("0");
				currencyName += entry.getKey()+ ",";
				List<Rebates> ls=entry.getValue(); 
				for(Rebates reb : ls ){
					amount = amount.add(reb.getRebatesDiff());
				}
				rebatesDiff +=amount +",";
			}
		}
		/**
		 * 详情里面获取团队返佣金额
		 */
		List<String> teamCum = rebatesService.getNotStatus2AllCumulative(rebates.get(0).getOrderId().toString());
		String teamMonery = "";
		String mt = "";
		for(String strMonery : teamCum){
			teamMonery = strMonery;
		}
		if(null!=teamMonery && !("").equals(teamMonery)){
			String[] arry = teamMonery.split("\\+");
	        Map<String, Double> map = new HashMap<String, Double>();
	        Map<String, String> map2 = new HashMap<String, String>();
	        for (String str : arry){
	            String[] s = str.split("\\:");
	            if (map.containsKey(s[0]))
	            {
	                map.put(s[0], map.get(s[0]) + Double.valueOf(s[1]));
	            }
	            else
	            {
	                map.put(s[0], Double.valueOf(s[1]));
	            }
		    }
	        Double val;
	        String name;
	        Set<String> keySet = map.keySet();
	        DecimalFormat myformat = new DecimalFormat();
			myformat.applyPattern("##,###.00");
	        
	        for (String  key : keySet) {
				val = map.get(key);
				name = currencyService.findCurrency(Long.parseLong(key)).getCurrencyName();
				map2.put(name, myformat.format(val));
			}
		    mt = map2.toString();
		    mt=mt.replaceAll("\\=", " ");
		    mt=mt.substring(1, mt.length()-1);
		}
		
		//获取预计返佣 add start by jiangyang 2015-8-7
		Long orderId = null;
		if(CollectionUtils.isNotEmpty(rebates)){
			orderId = rebates.get(0).getOrderId();
		}
		Map<String, Object> orderDetail = new HashMap<String,Object>();
		if(orderId!=null){
			orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId.toString());
		}
		String groupRebate = null;
		List<Map<String,Object>> travelInfoList = null;
		if (orderDetail != null){
			groupRebate = (String)orderDetail.get("groupRebate");
			travelInfoList = (List<Map<String,Object>>) orderDetail.get("travelInfoList");			
		}
		model.addAttribute("groupRebate", groupRebate);
		if(CollectionUtils.isNotEmpty(travelInfoList)){
			model.addAttribute("travelInfoList", travelInfoList);
		}
		//获取预计返佣 add end   by jiangyang 2015-8-7
		
		model.addAttribute("teamMonery", mt);
		model.addAttribute("paymentProject", "机票返佣");
		model.addAttribute("bremarks", rebates.get(0).getRemark());
		model.addAttribute("currencyName", currencyName);
		model.addAttribute("rebatesDiff", rebatesDiff);
		AirticketOrder productOrder = airticketOrderDao.getAirticketOrderById(rebates.get(0).getOrderId());
		model.addAttribute("productOrder", productOrder);
		ActivityAirTicket product = activityAirTicketService.findById(productOrder.getAirticketId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getAirticketId());
		model.addAttribute("productGroup", productGroup);
		model.addAttribute("orderStatusStr",OrderCommonUtil.getChineseOrderType(productOrder.getProductTypeId().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(String.valueOf(productOrder.getOccupyType())));
		model.addAttribute("rebates", rebates);
		List<ReviewLog> reviewLogList = reviewService.findReviewLog(reviewId);
		Map<String, Object> airticketReturnDetailInfoMap = airTicketReturnService.queryAirTicketReturnInfoById(Long.toString(rebates.get(0).getOrderId()));
		model.addAttribute("reviewLogList", reviewLogList);
		model.addAttribute("airticketReturnDetailInfoMap", airticketReturnDetailInfoMap);
	}
	
	/**
	 * 支付凭单 
	 * 
	 */
	@RequestMapping(value = "airticketRebatesReviewPrint")
	public String airticketRebatesReviewPrint(Model model,HttpServletRequest request, HttpServletResponse response) {

		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String payId = request.getParameter("payId");	//返佣申请ID
		String option = request.getParameter("option");
		String groupCode = request.getParameter("groupCode");	//团号
		Map<String,Object> map = null;
		if("pay".equals(option)) {
			map = rebatesService.airticketRebatesPrintData(reviewId, payId);
		}else if("order".equals(option)) {
			map = rebatesService.airticketRebatesPrintData(reviewId);
		}
		
		map.put("reviewId", reviewId);
		map.put("payId", payId);
		map.put("groupCode", groupCode);
		map.put("option", option);
		model.addAttribute("map", map);
		return "modules/review/airticketRebatesPrint";
	}
	
	/**
	 * 下载支出凭单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="airticketRebatesDownload")
	public ResponseEntity<byte[]> airticketRebatesDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String payId = request.getParameter("payId");	// refund ID
		String option = request.getParameter("option");
		String groupCode = request.getParameter("groupCode");	//团号
		Map<String,Object> map = null;
		if("pay".equals(option)) {
			map = rebatesService.airticketRebatesPrintData(reviewId, payId);
		}else if("order".equals(option)) {
			map = rebatesService.airticketRebatesPrintData(reviewId);
		}
		map.put("reviewId", reviewId);
		map.put("payId", payId);
		map.put("groupCode", groupCode);
		map.put("createDate", DateUtils.formatDate((Date)map.get("createDate"), "yyyy年 MM月dd日 "));
		//更新打印时间
		updatePrintTime(reviewId);
		
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
		if (null != review && null == review.getPrintFlag() ) { 
			Date printDate = new Date();
			try {
				reviewService.updateReviewPrintInfoById(printDate,Long.parseLong(reviewId));
			} catch (Exception e) {
				e.printStackTrace();
				//logger.error("格式化日期错误", e);
				throw e;
			} 
		}
	}
}
