package com.trekiz.admin.modules.airticketorder.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.AirTicketOrderLendMoneyService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.service.IAirticketPreOrderService;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AirportService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;

@Controller
@RequestMapping(value = "${adminPath}/order/lendmoney")
public class AirTicketOrderLendMoneyController {

	private static final int REFUND_PRODUCTTYPE_AIRTICKET = 7;
	
	private static final Logger log = Logger.getLogger(AirTicketOrderLendMoneyController.class);
	
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	
	@Autowired
	private VisaOrderDao visaOrderDao;
	
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
	private AirportService airportService;
	
	@Autowired
	private AirlineInfoService airlineInfoService;

	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private OfficeService officeService;

	@Autowired
	private SysIncreaseService sysIncreaseService;
	
	@Autowired
	private IAirticketPreOrderService airticketPreOrderService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private AreaService areaService;
	 
	@Autowired
    private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;

	@Autowired
	private OrderCommonService orderService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private AirTicketOrderLendMoneyService airTicketOrderLendMoneyService;
	/**
	 * 新行者机票订单借款明细查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="airticketLendMoneyList")
	public String airticketLendMoneyList(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		
		return "modules/order/lendmoney/airticketLendMoneyList";
	}
	/**
	 * 借款明细查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="airticketLendMoneyByReviewId")
	public String airticketLendMoneyByReviewId(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		Map<String, Object> orderDetail = queryAirticketOrderDetailById(orderId);//查询订单明细
		List<ReviewDetail> rdlist = new ArrayList<ReviewDetail>();
		Review review = null;
		try{
			if(reviewId!=null){
				review = reviewService.findReviewInfo(Long.valueOf(reviewId));
				rdlist= reviewService.queryReviewDetailList(reviewId);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//将总金额和币种显示出来
		List<ReviewDetail> borrowPricesList = new ArrayList<ReviewDetail>();
		if(rdlist!=null&&rdlist.size()>0){
			for(int i = 0;i<rdlist.size();i++){
				if("borrowPrices".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
				if("currencyMarks".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
				if("borrowRemark".equals(rdlist.get(i).getMykey())){
					model.addAttribute("borrowRemark",rdlist.get(i).getMyvalue());
				}
			}
		}
		
		List<BorrowingBean> blist = BorrowingBean.transferReviewDetail2BorrowingBean(rdlist);
		List<BorrowingBean> tralist = new ArrayList<BorrowingBean>();
		List<BorrowingBean> teamlist= new ArrayList<BorrowingBean>();
		List<BorrowingBean> borrowList = BorrowingBean.transferReviewDetail2BorrowingBean(borrowPricesList);
		//将blist 拆分为团队借款和游客借款两部分
		if(blist!=null&&blist.size()>0){
			int size = blist.size();
			for(int i = 0;i<size;i++){
				BorrowingBean bean = blist.get(i);
				if("0".equals(bean.getTravelerId())){//团队借款
					teamlist.add(bean);
				}else{//游客借款
					tralist.add(bean);
				}
			}
			
		}
		
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
	    	List<ReviewLog> rLog=reviewService.findReviewLog(Long.parseLong(reviewId));
			model.addAttribute("rLog",rLog);
	    }
		if(review!=null){
			model.addAttribute("applyDate", review.getCreateDate());
		}
		model.addAttribute("tralist", tralist);
		model.addAttribute("teamlist",teamlist);
		model.addAttribute("borrowList", borrowList);
		model.addAttribute("totalsize", borrowList.size());
		model.addAttribute("reviewId", reviewId);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		model.addAttribute("orderDetail", orderDetail);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		model.addAttribute("fromAreas", areaService.findFromCityList(""));// 出发城市
        model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("airlineList", airlineInfoService.getAirlineInfoList(companyId));
		
		return "modules/order/lendmoney/airticketLendMoneyDetail";
	}
	
	
	/**
	 * 根据订单编号，查询新行者借款订单的明细
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> queryAirticketOrderDetailById(String orderId){
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		
		return orderDetailInfoMap;
	}
	
	/**
	 * 新行者机票订单借款申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="airticketLendMoneyApply")
	public String airticketLendMoneyApply(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String orderId= request.getParameter("orderId");
		String reviewId = request.getParameter("review_id");
		Map<String, Object> orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		RefundBean reviewObj = null;
		model.addAttribute("reviewObj", reviewObj);
		model.addAttribute("orderId", orderId);
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("orderDetail", orderDetail);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		return "modules/order/lendmoney/airticketLendMoneyList";
	}
	
	/**
	 * 流程互斥校验
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="beforeAddLendMoneyApply")
	public String beforeAddLendMoneyApply(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
//		String orderId = request.getParameter("orderId");
//		int validate = airTicketOrderLendMoneyService.validateProcess(Integer.parseInt(orderId));
		String result = "";
		//注释掉暂时取消互斥
//		if(validate>0){
//		 result = "还有未完成申请"; 
//		}   
		return result;
	}
	
	/**
	 * 新行者机票订单借款列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "borrowAmountList")
	public String borrowAmountList( HttpServletRequest request, HttpServletResponse response, Model model, String orderId,Integer flowType,Integer productType) {
		//int temp= reviewService.getOperTotal();
		//Integer flowType = 19;
		//Integer productType = 7;
		AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.valueOf(orderId));
		Long pid = order.getAirticketId();
		ActivityAirTicket p = activityAirTicketDao.findOne(pid);
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(productType,flowType, orderId, false,p.getDeptId());
		
		List<BorrowingBean> reviewList = getBorrowingBeanList(reviewMapList);
		for(BorrowingBean borr :reviewList){
			if(borr.getTravelerId().contains(BorrowingBean.REGEX)||"0".equals(borr.getTravelerId())){
				borr.setTravelerName("团队");
			}
			if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(BorrowingBean.REGEX)){
				String compPrice = "";
				if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
					String[] cMarks = borr.getCurrencyMarks().split(BorrowingBean.REGEX);	
					String[] cPrices = borr.getBorrowPrices().split(BorrowingBean.REGEX);
					for(int i=0;i<cMarks.length;i++){
						compPrice+=cMarks[i]+cPrices[i]+"+";
					}
					borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
				}
				
			}else{
				    borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
			}
		}
		Collections.reverse(reviewList);
		model.addAttribute("bAList", reviewList);
		model.addAttribute("orderId", orderId);
		return "modules/order/lendmoney/airticketborrowAmountList";
	}
	/**
	 * 组装BorrowingBean对象
	 */
	private List<BorrowingBean> getBorrowingBeanList(
			List<Map<String, String>> reviewMapList) {
		List<BorrowingBean> aList = new ArrayList<BorrowingBean>();
		if (null == reviewMapList || reviewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, String> map : reviewMapList) {
			aList.add(new BorrowingBean(map));
		}
		return aList;
	}

	/**
	 * 借款撤销申请
	 */
	@ResponseBody
	@RequestMapping(value = "cancelBorrowAmount")
	public String cancelBorrowAmount( HttpServletRequest request) {
		Integer id = Integer.parseInt(request.getParameter("id"));
		String msg = "";
		List<Byte> vresult = airTicketOrderLendMoneyService.validateReviewStatus(id);
		if(vresult.get(0)!=1){
			msg="当前状态不许撤销";
		    return msg;
		}
		int cresult = airTicketOrderLendMoneyService.cancelBorrowReview(id);
		if(cresult<=0){
			msg="撤销失败";
		}
        return msg;
	}

	/**
	 * 流程申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="LendMoneyApply")
	public String LendMoneyApply(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");
//		String[] travelerIds = request.getParameterValues("travelerId");
		String travelerId="0";
		List<String> paramNamesList = new ArrayList<String>();
		paramNamesList.add("travelerId");
		paramNamesList.add("travelerName");
		paramNamesList.add("currencyId");
		paramNamesList.add("currencyName");
		paramNamesList.add("currencyMark");
		paramNamesList.add("currencyExchangerate");
		paramNamesList.add("payPrice");
		paramNamesList.add("lendPrice");
		paramNamesList.add("remark");
		paramNamesList.add("lendName");
//		paramNamesList.add("borrowRemark");
		List<String> paramList = new ArrayList<String>();
		paramList.add("currencyIds");
		paramList.add("currencyNames");
		paramList.add("currencyMarks");
		paramList.add("borrowPrices");
		//paramList.add("currencyExchangerates");
		
		List<Detail> listReview = BorrowingBean.exportDetail4Request(request, paramNamesList,paramList,"lendPrice");
		if(request.getParameter("borrowPrices") != null&& request.getParameter("currencyIds")!= null ){
			float  fc = currencyConverter(request.getParameter("borrowPrices"),request.getParameter("currencyIds"));
			Detail d = new Detail();
			d.setKey("currencyConverter");
			d.setValue(fc+"");
			listReview.add(d);
		}
		if(request.getParameter("borrowRemark") != null&& request.getParameter("borrowRemark")!= null ){
			Detail d = new Detail();
			d.setKey("borrowRemark");
			d.setValue(request.getParameter("borrowRemark"));
			listReview.add(d);
		}
		if(request.getParameter("currencyExchangerates") != null ){
			Detail d = new Detail();
			d.setKey("currencyExchangerates");
			d.setValue(request.getParameter("currencyExchangerates"));
			listReview.add(d);
		}
		
		if (StringUtils.isEmpty(reviewId)) {
			reviewId = "0";
		}
		AirticketOrder airOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
		
		Long airticketId = airOrder.getAirticketId();
		if(airticketId == null){
			return null;
		}
		ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(airticketId);
		if(activityAirTicket == null){
			return null;
		}
		Long deptId = activityAirTicket.getDeptId();
		if(deptId == null){
			return null;
		}
		StringBuffer reply = new StringBuffer();
		Long result=0L; //返回的是reviewId
		String[] travels =  listReview.get(0).getValue().split("#");
		if(travels!=null&&travels.length>0){
			if(travels.length>1){
				travelerId="0";
			}else if(travels.length==1 && ("0").equals(travels[0])){
				travelerId="0";
			}else{
				travelerId=travels[0];
			}
		}
		String  borrowRemark  = request.getParameter("borrowRemark");
		result = reviewService.addReview(REFUND_PRODUCTTYPE_AIRTICKET, 19, orderId, Long.parseLong(travelerId), Long.parseLong(reviewId), borrowRemark, reply, listReview, deptId);
		
		//Map<String, Object> map = new HashMap<String, Object>();
		String msg="";
		if(result==0L){
			//map.put("error", reply.toString());
			msg="申请失败!";
		} else {
			//map.put("success", "success");
			msg="申请成功!";
		}
		return msg;
	}
	
	
	/**
	 * 其他币种转换成人民币
	 */
	@ResponseBody
	public float currencyConverter(String count,String currencyId)
	{
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		String [] ct= count.split("#");
		String [] ci= currencyId.split("#");
		double totalMoney =0;
		for(int i=0;i<ct.length;i++)
		{
			if(StringUtils.isNotBlank(ct[i])){
				String ctStr = ct[i];
				if(ctStr.indexOf(",")>-1){
					ctStr = ct[i].replaceAll(",", "");
				}
				//String ctStr = ct[i].replaceAll(",", "");
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=?");
				//buffer.append(ci[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class,ci[i]);
				Map<String, Object>  mp =  list.get(0);
				totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ctStr);
			}
			
		}
		 BigDecimal   b   =   new   BigDecimal(totalMoney); 
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-05-12
	 * 审核借款申请信息
	 */
	@RequestMapping(value ="reviewPlaneBorrowingInfo")
	public String reviewPlaneBorrowingInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,String orderId,long roleId,Integer userLevel){
		String reviewId = rid+"";
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		Map<String, Object> orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		List<ReviewDetail> rdlist = new ArrayList<ReviewDetail>();
		Review review = null;
		try{
			if(reviewId!=null){
				review = reviewService.findReviewInfo(rid); 
				rdlist = reviewService.queryReviewDetailList(reviewId);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//将总金额和币种显示出来
				List<ReviewDetail> borrowPricesList = new ArrayList<ReviewDetail>();
				if(rdlist!=null&&rdlist.size()>0){
					for(int i = 0;i<rdlist.size();i++){
						if("borrowPrices".equals(rdlist.get(i).getMykey())){
							borrowPricesList.add(rdlist.get(i));
						}
						if("currencyMarks".equals(rdlist.get(i).getMykey())){
							borrowPricesList.add(rdlist.get(i));
						}
					}
				}
				
				List<BorrowingBean> blist = BorrowingBean.transferReviewDetail2BorrowingBean(rdlist);
				List<BorrowingBean> tralist = new ArrayList<BorrowingBean>();
				List<BorrowingBean> teamlist= new ArrayList<BorrowingBean>();
				List<BorrowingBean> borrowList = BorrowingBean.transferReviewDetail2BorrowingBean(borrowPricesList);
				//将blist 拆分为团队借款和游客借款两部分
				if(blist!=null&&blist.size()>0){
					int size = blist.size();
					for(int i = 0;i<size;i++){
						BorrowingBean bean = blist.get(i);
						if("0".equals(bean.getTravelerId())){//团队借款
							teamlist.add(bean);
						}else{//游客借款
							tralist.add(bean);
						}
					}
					
				}
				
				if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
			    	List<ReviewLog> rLog=reviewService.findReviewLog(Long.parseLong(reviewId));
					model.addAttribute("rLog",rLog);
			    }
				if(review!=null){
					model.addAttribute("applyDate", review.getCreateDate());
				}
				model.addAttribute("tralist", tralist);
				model.addAttribute("teamlist",teamlist);
				model.addAttribute("borrowList", borrowList);
				model.addAttribute("totalsize", borrowList.size());
				model.addAttribute("reviewId", reviewId);
				List<Currency> currencyList = currencyService.findCurrencyList(companyId);
				model.addAttribute("orderDetail", orderDetail);
				model.addAttribute("currencyList", currencyList);
				model.addAttribute("orderId", orderId);
				model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
				model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
				model.addAttribute("fromAreas", areaService.findFromCityList(""));// 出发城市
		        model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
				model.addAttribute("airlineList", airlineInfoService.getAirlineInfoList(companyId));
		
		model.addAttribute("rid",rid);
		model.addAttribute("roleId",roleId);
		model.addAttribute("userLevel",userLevel);
		return "modules/review/reviewPlaneBorrowingInfo";
	}

}
