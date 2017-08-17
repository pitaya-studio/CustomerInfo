package com.trekiz.admin.modules.stock.web;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.ActivityGroupView;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrder;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrderView;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.ActivityReserveOrderService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.IActivityGroupViewService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.entity.ActivityReserveFile;
import com.trekiz.admin.modules.stock.jsonBean.ReturnReserveJsonBean;
import com.trekiz.admin.modules.stock.service.StockService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
/**
 * 旅游产品信息控制器
 * @author zzy
 *
 */
@Controller
@RequestMapping(value="${adminPath}/stock/manager/apartGroup")
public class StockController extends BaseController{

	private static final Log logger = LogFactory.getLog(StockController.class);
	/**
	 * 散拼切位订单产品类型
	 */
	private static final Integer LOOSE_RESERVE_PRODUCT_TYPE=9;
	/**
	 * 机票切位订单产品类型
	 */
	private static final Integer AIR_TICKET_RESERVE_PRODUCT_TYPE=8;
	/**
	 * 切位退款类型
	 */
	private static final Integer LOOSE_RESERVE_FLOW_TYPE=1;
	@Autowired
	@Qualifier("activityGroupViewService")
	private IActivityGroupViewService activityGroupViewService;
	
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;

	@Autowired
	@Qualifier("activityGroupSyncService")
	private IActivityGroupService activityGroupService;		
	
	@Autowired
	private ActivityReserveOrderService activityReserveOrderService;
	
	@Autowired
	@Qualifier("activityAirTicketServiceImpl")
	private IActivityAirTicketService activityAirTicketService;
	
	@Autowired
	private SysIncreaseService sysIncreaseService;
	
	@Autowired
	private StockService stockService;
	@Autowired
    private CurrencyService currencyService;

	@Resource
	private AgentinfoService agentinfoService;
	@Autowired
    private DepartmentService departmentService;
	@Autowired
    private OrderCommonService orderService;
	@Autowired
    private ReviewService reviewService;
	@Autowired
    private OrderReviewService orderReviewService;
	@ModelAttribute
	public TravelActivity get(@RequestParam(required=false) Long id) {
		if(id!=null){
			return travelActivityService.findById(id);
		}else
			return new TravelActivity();
	} 
	
	/**
	 * 
	 *  功能:库存查询
	 *
	 *  @author WangDuo
	 *  @DateTime 2014-11-15
	 *  @param travelActivity
	 *  @param request
	 *  @param response
	 *  @param model
	 *  @return
	 */	 
	@RequestMapping(value={"list",""})
	public String list(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response, Model model){
		User user = UserUtils.getUser();
		//批发商uuid
		String companyUUID = user.getCompany().getUuid();
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
				
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");		
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
		String wholeSalerKey = request.getParameter("wholeSalerKey");
		travelActivity.setAcitivityName(wholeSalerKey);
		Long agentId = null;
		if(StringUtils.isNotBlank(request.getParameter("agentId"))){
			try {
				agentId = StringUtils.toLong(request.getParameter("agentId"));				
			} catch (NumberFormatException e) {
			}
		}	

		int typeId=2; //散拼类型
		String review="";
		Long companyId = UserUtils.getUser().getCompany().getId();
		Page<ActivityGroupView> page = activityGroupViewService.findActivityGroupReview(new Page<ActivityGroupView>(request, response), travelActivity,  
				settlementAdultPriceStart, settlementAdultPriceEnd, agentId,typeId,review, companyId,common);
		
		model.addAttribute("travelActivity", travelActivity);
        model.addAttribute("page", page);
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
//		model.addAttribute("targetAreaList", activityGroupViewService.findTargetArea());
		model.addAttribute("companyUUID", companyUUID);
		boolean isDY = false;
		if(Context.SUPPLIER_UUID_DYGL.equals(companyUUID)){
			isDY = true;
		}
		model.addAttribute("isDY", isDY);
		//model.addAttribute("departmentList", departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId()));
	    return "modules/stock/apartgroup/activityList";
	}	
	/**
	 * 
	 *  功能:C325需求查询 团期列表查询,查询散拼产品是有效期内的
	 *  @author WangXK
	 *  @DateTime 2015-11-27
	 *  @param travelActivity
	 *  @param request
	 *  @param response
	 *  @param model
	 *  @return
	 */	 
	@RequestMapping(value={"getProductLooseList"})
	public String getProductLooseList(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response, Model model){
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);//按部门展示
		Map<String,String> argsMap = new HashMap<String,String>();		
		String wholeSalerKey = request.getParameter("wholeSalerKey");//输入产品名称、团号，支持模糊匹配
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");//价格范围	
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
		String groupOpenDateStart = request.getParameter("groupOpenDateStart");//出团日期
		String groupOpenDateEnd = request.getParameter("groupOpenDateEnd");
		String groupCloseDateStart = request.getParameter("groupCloseDateStart");//截团日期
		String groupCloseDateEnd = request.getParameter("groupCloseDateEnd");
		String freePositionStart = request.getParameter("freePositionStart");//余位数
		String freePositionEnd = request.getParameter("freePositionEnd");
		String source =  request.getParameter("source");//update by zhanghao 跳转来源，批量切位或者归还余位 （前端需要传递该参数，机票中也需要增加此默认查询条件）
		Long agentId = null;
		if(StringUtils.isNotEmpty(request.getParameter("agentId"))) {
			agentId = Long.parseLong(request.getParameter("agentId"));//切位渠道
		}
		argsMap.put("settlementAdultPriceStart", settlementAdultPriceStart);
		argsMap.put("settlementAdultPriceEnd", settlementAdultPriceEnd);
		argsMap.put("groupOpenDateStart", groupOpenDateStart);
		argsMap.put("groupOpenDateEnd", groupOpenDateEnd);
		argsMap.put("groupCloseDateStart", groupCloseDateStart);
		argsMap.put("groupCloseDateEnd", groupCloseDateEnd);
		argsMap.put("freePositionStart", freePositionStart);
		argsMap.put("freePositionEnd", freePositionEnd);
		argsMap.put("source", source);//update by zhanghao 跳转来源，批量切位或者归还余位 （前端需要传递该参数，机票中也需要增加此默认查询条件）
		
		travelActivity.setAcitivityName(wholeSalerKey);
		Page<ActivityGroupView> page = activityGroupViewService.findActivityGroupReviewC325(new Page<ActivityGroupView>(request, response,-1), travelActivity,  
				argsMap, agentId,Context.ProductType.PRODUCT_LOOSE,common);
		
		//如果是返还切位则根据渠道商过滤查询结果
		List<String> activityGroupIds = new ArrayList<String>();
		if("isReturn".equals(source) && CollectionUtils.isNotEmpty(page.getList())) {
			for(ActivityGroupView groupView : page.getList()) {
				activityGroupIds.add(String.valueOf(groupView.getId()));
			}
			//返回列表中所有的团期切位集合信息
			List<ActivityGroupReserve> reserves = stockService.getReservesByGroupIds(activityGroupIds, agentId);
			Map<String, ActivityGroupReserve> reserveMap = new HashMap<String, ActivityGroupReserve>();
			if(CollectionUtils.isNotEmpty(reserves)) {
				for(ActivityGroupReserve reserve : reserves) {
					reserveMap.put(String.valueOf(reserve.getActivityGroupId()), reserve);
				}
			}
			//修改当前团期的切位数和已售切位数
			Iterator<ActivityGroupView> groupViewIter = page.getList().iterator();
			while(groupViewIter.hasNext()) {
				ActivityGroupView groupView = groupViewIter.next();
				
				ActivityGroupReserve currReserve = reserveMap.get(String.valueOf(groupView.getId()));
				if(currReserve != null) {
					//查询切位数过滤条件
					if(StringUtils.isNotEmpty(freePositionStart)) {
						if(currReserve.getPayReservePosition() < Integer.parseInt(freePositionStart)) {
							groupViewIter.remove();
							continue;
						}
					}
					if(StringUtils.isNotEmpty(freePositionEnd)) {
						if(currReserve.getPayReservePosition() > Integer.parseInt(freePositionEnd)) {
							groupViewIter.remove();
							continue;
						}
					}
					if(currReserve.getPayReservePosition()-currReserve.getSoldPayPosition() > 0) {
						groupView.setSoldPayPosition(currReserve.getSoldPayPosition());
						groupView.setPayReservePosition(currReserve.getPayReservePosition());
					} else {
						groupViewIter.remove();
					}
				} else {
					groupViewIter.remove();
				}
			}
		}
		
		model.addAttribute("travelActivity", travelActivity);
        model.addAttribute("page", page);
        model.addAttribute("searchParam", argsMap);
        model.addAttribute("selectedProducts", request.getParameter("selectedProducts"));
        model.addAttribute("source", source);
        model.addAttribute("agentId", agentId);

		return "modules/stock/apartgroup/productGroup";
	}
	/**
	 * 
	 *  功能:库存详情
	 *
	 *  @author zj
	 *  @DateTime 2014-2-11 上午11:53:21
	 *  @param travelActivity
	 *  @param request
	 *  @param response
	 *  @param model
	 *  @return
	 */
	 
	@RequestMapping(value="detail")
	public String detail(@ModelAttribute TravelActivity travelActivity,@RequestParam(required=false) Long id,@RequestParam(required=false) Long agentId,@RequestParam(required=false) Long activityGroupId,HttpServletRequest request, HttpServletResponse response,Model model){
		if(agentId != null){
			String agentName = AgentInfoUtils.getAgentName(agentId);
			if(StringUtils.isNotEmpty(agentName))model.addAttribute("agentName", agentName);
		}
		String showReserve = request.getParameter("showReserve");
		model.addAttribute("showReserve", showReserve);
		return "modules/stock/apartgroup/detail";
	}
	
	
	/**
	 * 功能：散拼产品切位订单详情
	 * @author haiming.zhao
	 * @DateTime 2014-12-26 
	 * @param request
	 * @param response
	 * @param orderNum    订单编号
	 * */
	 
	@RequestMapping(value="reserveOrderInfo")
	public String reserveOrderInfo(HttpServletRequest request, HttpServletResponse response,Model model){
		String orderNum = request.getParameter("orderNum");
	  //  ReserveOrderInfoView ro = stockService.findReserveOrderInfoByOrderNum(orderNum);
		ActivityReserveOrder activityReserveOrder = stockService.findReserveOrderInfoByOrderNum(orderNum);
		ActivityGroup activityGroup = stockService.findActivityGroupById(activityReserveOrder.getActivityGroupId());
		if(activityGroup != null){
			model.addAttribute("activityGroup", activityGroup);
		}
//		List<ActivityReserveFile> list = stockService.findByAgentIdAndSrcActivityIdAndActivityGroupId(activityReserveOrder.getAgentId(), activityReserveOrder.getSrcActivityId(), activityReserveOrder.getActivityGroupId());
//	    List<ActivityReserveFile> list = stockService.findByAgentIdAndSrcActivityIdAndActivityGroupId(activityReserveOrder.getAgentId(), activityReserveOrder.getActivityGroupId());
	    List<ActivityReserveFile> list = stockService.findByAgentIdAndSrcActivityIdAndActivityGroupId(activityReserveOrder.getAgentId(),activityReserveOrder.getId());
		if(list!=null){
	    	String payVoucherIds = Collections3.extractToString(list,"srcDocId",",");
	    	model.addAttribute("payVoucherIds", payVoucherIds);
	    }
		if(activityReserveOrder !=null){
	    	model.addAttribute("activityReserveOrder", activityReserveOrder);
	    }
		model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
		return "modules/stock/apartgroup/reserveOrderInfo";
	}
	/**	 
	 *  功能:产品切位
	 *
	 *  @author zj
	 *  @DateTime 2014-2-11 上午11:52:40
	 *  @param travelActivity
	 *  @param request
	 *  @param response
	 *  @param model
	 *  @return
	 */
	 
	@RequestMapping(value="reserve")
	public String reserve(@ModelAttribute TravelActivity travelActivity,@RequestParam(required=false) Long id,@RequestParam(required=false) Long activityGroupId,@RequestParam(required=false) Long agentId,HttpServletRequest request, HttpServletResponse response,Model model){
		Map<Long, ActivityGroupReserve> hashMap = new HashMap<Long, ActivityGroupReserve>();
		Map<Long, List<ActivityReserveFile>> fileMap = new HashMap<Long, List<ActivityReserveFile>>();
		if( agentId != null && id != null){
			List<ActivityGroupReserve> reserveList = stockService.findGroupReserve(agentId,id);
			
			List<ActivityReserveFile> activityReserveFileList = stockService.findByAgentIdAndSrcActivityId(agentId, id);
			if(reserveList != null || activityReserveFileList != null){
				for(ActivityGroupReserve groupReserve : reserveList){
					hashMap.put(groupReserve.getActivityGroupId(), groupReserve);
				}
				for(ActivityReserveFile file:activityReserveFileList){
					List<ActivityReserveFile> filelist = fileMap.get(file.getActivityGroupId());
					if(filelist==null){
						filelist = new ArrayList<ActivityReserveFile>();
						fileMap.put(file.getActivityGroupId(),filelist);
					}
					filelist.add(file);
				}
				Set<ActivityGroup> groupset = travelActivity.getActivityGroups();
				for(ActivityGroup group:groupset){
					group.setActivityGroupReserve(hashMap.get(group.getId()));
					group.setActivityReserveFileList(fileMap.get(group.getId()));
				}
			}
		}   
		List<Map<String, Object>> maps = stockService.findReserveOrder(activityGroupId,agentId);
		model.addAttribute("reserveList", maps);
		model.addAttribute("isRequested",agentId==null?true:false);
		model.addAttribute("agentId",agentId);
		model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
		return "modules/stock/apartgroup/reserve";
	}
	
	
	
	/**
	 * 
	 *  功能:产品切位
	 *
	 *  @author zj
	 *  @DateTime 2014-2-11 上午11:52:40
	 *  @param travelActivity
	 *  @param request
	 *  @param response
	 *  @param model
	 *  @return
	 * @throws OptimisticLockHandleException 
	 * @throws PositionOutOfBoundException 
	 */
	 
	@RequestMapping(value="doreserve")
	public String doreserve(@ModelAttribute TravelActivity travelActivity,@RequestParam(required=false) Long id,@RequestParam(required=false) Long groupId,@RequestParam(required=false) Long agentId,HttpServletRequest request, HttpServletResponse response,Model model) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		String[] reserveids = request.getParameterValues("reserveid");
		String[] activityGroupIds = request.getParameterValues("activityGroupId");
		String[] payReservePositions = request.getParameterValues("payReservePosition");
		String[] frontMoneys = request.getParameterValues("frontMoney");
		String[] remarks = request.getParameterValues("remark");
		String[] payType = request.getParameterValues("payType");
		String[] reservation = request.getParameterValues("reservation");
//		String srcActivityId  = request.getParameter("srcActivityId");
		String activityGroupId = request.getParameter("activityGroupId");
		if (groupId==null){
			groupId=(long)0;
		} 
		Long reserveOrderId= null;
		if(activityGroupIds!=null&&reserveids!=null){
			List<ActivityGroupReserve> activityGroupReserveList = new ArrayList<ActivityGroupReserve>();
			List<ActivityReserveOrder> activityReserveOrderList = new ArrayList<ActivityReserveOrder>();
			
			for(int i=0;i<1;i++){
				int payReservePosition = StringUtils.toInteger(payReservePositions[i]);
				if(payReservePosition>0){
					Long reserveid = StringUtils.toLong(reserveids[i]);
					ActivityGroupReserve groupreserve = new ActivityGroupReserve();					
					
					groupreserve.setId(reserveid);
					groupreserve.setSrcActivityId(id);
					groupreserve.setActivityGroupId(StringUtils.toLong(activityGroupIds[i]));
					
					groupreserve.setAgentId(agentId);
					//切位时 soldPayPosition=0
					groupreserve.setSoldPayPosition(0);
					groupreserve.setReserveType(0);
					groupreserve.setPayReservePosition(payReservePosition);
					//填写价格信息列表中的订金列设为非必填项，如果没填入数据则为0，20150910
					BigDecimal money = null;
					if(StringUtils.isNotBlank(frontMoneys[i])) {
						money = new BigDecimal(frontMoneys[i]);
					}else {
						money = new BigDecimal("0");
					}
					groupreserve.setFrontMoney(money);
					groupreserve.setRemark(remarks[i]);
					groupreserve.setPayType(StringUtils.toInteger(payType[i]));
					groupreserve.setReservation(reservation[i]);
					
					ActivityReserveOrder activityReserveOrder  = new ActivityReserveOrder();					
					activityReserveOrder.setSrcActivityId(id);
					activityReserveOrder.setActivityGroupId(StringUtils.toLong(activityGroupIds[i]));
					activityReserveOrder.setCreateBy(UserUtils.getUser().getId());
					
					String companyName=UserUtils.getUser().getCompany().getName();
					Long companyId=UserUtils.getUser().getCompany().getId();
					String orderNum = sysIncreaseService.updateSysIncrease(companyName
							.length() > 3 ? companyName.substring(0, 3) : companyName,
									companyId, null, Context.ORDER_NUM_TYPE);
					
					activityReserveOrder.setOrderNum(orderNum);
					
					activityReserveOrder.setAgentId(agentId);
					Date createDate= new Date();
					activityReserveOrder.setCreateDate(createDate);
					activityReserveOrder.setUpdateDate(createDate);
					activityReserveOrder.setSaleId(UserUtils.getUser().getId());
					activityReserveOrder.setRemark(remarks[i]);
					activityReserveOrder.setOrderStatus(1); //订单状态(0:未付定金,1:已付定金)
					activityReserveOrder.setConfirm(0); //0:收款未确认 
					activityReserveOrder.setReserveType(0); //0:散拼，1：机票
					activityReserveOrder.setMoneyType(1);  //切位都用人民币
					activityReserveOrder.setPayReservePosition(payReservePosition);
					activityReserveOrder.setPayType(StringUtils.toInteger(payType[i]));
					activityReserveOrder.setReservation(reservation[i]);
					activityReserveOrder.setOrderMoney(money);
					activityReserveOrder.setPayMoney(money);
					
					ActivityGroup group = new ActivityGroup();
					group=activityGroupService.findById(StringUtils.toLong(activityGroupIds[i]));
				
					activityReserveOrder.setStartDate(group.getGroupOpenDate());
					activityReserveOrder.setEndDate(group.getGroupCloseDate());				
		
					activityGroupReserveList.add(groupreserve);
					activityReserveOrderList.add(activityReserveOrder);
					
				}
			}
			try {
				reserveOrderId=stockService.saveGroupReserveList(activityGroupReserveList,activityReserveOrderList, request);				
				getFileList(request,reserveOrderId,id);
				
			} catch (OptimisticLockHandleException e) {
				logger.error("团期修改失败", e);
				throw new OptimisticLockHandleException("存入团期内容失败，请重试。");
			}
		}
	
		
		//by sy
		 return "redirect:"+ Global.getAdminPath()+"/stock/manager/apartGroup/detail?id="+ id+"&activityGroupId="+activityGroupId;
		
//		 return "redirect:"+ Global.getAdminPath()+"/stock/manager/apartGroup/uploadme/"+id+"/"+groupId+"/"+agentId+"/"+reserveOrderId;        
	      
		//return "redirect:"+Global.getAdminPath()+"/stock/manager/apartGroup/detail?id="+id+"&activityGroupId="+ groupId;
	}
	
	
	
	
	/**
	 * 切位附件上传
	 * by songyang 二〇一五年十二月二日 21:13:23
	 * @param request
	 * @param reserveOrderId
	 * @param srcActivityId
	 */
	
	public void getFileList(HttpServletRequest request,Long reserveOrderId,Long srcActivityId){
		
        String activityGroupId = request.getParameter("activityGroupId");
		String agentId = request.getParameter("agentId");
		if(ArrayUtils.isNotEmpty(request.getParameterValues("docId"))){
			String[] docId = request.getParameterValues("docId");
			String[] docName = request.getParameterValues("docName");
			for(int i=0;i<docId.length;i++){
				if(StringUtils.isNotBlank(docId[i])){
					 //保存附件表数据
		    		ActivityReserveFile actReserve = new ActivityReserveFile();
		    		actReserve.setSrcActivityId(srcActivityId);
		    		actReserve.setActivityGroupId(Long.parseLong(activityGroupId));
		    		actReserve.setAgentId(Long.parseLong(agentId));
		    		actReserve.setReserveOrderId(reserveOrderId);
		    		actReserve.setFileName(docName[i]);
		    		actReserve.setSrcDocId(Long.parseLong(docId[i]));
		    		stockService.saveReserveFile(actReserve);
				}
			}
		}
	}
	
	
	
	
	/**
	 * 归还切位
	 * @param activityGroupId
	 * @param agentId
	 * @param @param agentId
	 * @param agentId
	 * @param fontMoneyBackAmount
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="returnReserve")
	public String returnReserve(Model model, HttpServletRequest request,
			@RequestParam Long activityGroupId,@RequestParam Long agentId,@RequestParam Integer reserveBackAmount,
			@RequestParam Integer fontMoneyBackAmount) {
		
		//Long activityGroupId = Long.parseLong(request.getParameter("activityGroupId"));
		// reserveBackAmount=Integer.parseInt(request.getParameter("reserveBackAmount"));
		String returnRemark=request.getParameter("returnRemark");
		if (returnRemark==null) returnRemark="";
		model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
		try {
			stockService.returnReserve(activityGroupId,reserveBackAmount,fontMoneyBackAmount,agentId,request,returnRemark);
			return "success";
		} catch (Exception e) {
			logger.error("归还余位失败，团期ID：" + activityGroupId);
			return "fail";
		}
	}
	

	@RequestMapping(value="chaxun")
	public String chaxun(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,Model model){
		
		return "modules/kucun/kucun-chaxun";
	}
	
	
	@RequestMapping(value="revise")
	public String revise(@ModelAttribute TravelActivity travelActivity,@RequestParam(required=true)Long groupreserveId,HttpServletRequest request, HttpServletResponse response,Model model){
		if(groupreserveId != null){
			ActivityGroup activitygroup = stockService.findByActivityGroupId(groupreserveId);
			ActivityGroupReserve groupReserve = stockService.findByGroupreserveId(groupreserveId);
			model.addAttribute("activitygroup", activitygroup);
			model.addAttribute("groupReserve", groupReserve);
		}
		return "modules/kucun/kucun-revise";
	}
	@RequestMapping(value="update")
	public String update(@ModelAttribute TravelActivity travelActivity,ActivityGroupReserve groupReserve,Model model, HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		try {
			stockService.saveActivityGroupReserve(groupReserve, request);
		} catch (OptimisticLockHandleException e) {
			logger.error("团期修改失败", e);
			throw new OptimisticLockHandleException("存入团期内容失败，请重试。");
		}
		model.addAttribute("travelActivity", travelActivity);
		return "redirect:"+Global.getAdminPath()+"/stock/manager/detail?id="+groupReserve.getSrcActivityId()+"&agentId="+groupReserve.getAgentId();
	}
	@RequestMapping(value="delete")
	public String delete(@ModelAttribute TravelActivity travelActivity,@RequestParam(required=true)Long groupreserveId,@RequestParam(required=true)Long id,@RequestParam(required=false)Long agentId,Model model, HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		try {
			stockService.deleteActivityGroupReserve(groupreserveId, request);
		} catch (OptimisticLockHandleException e) {
			logger.error("团期修改失败", e);
			throw new OptimisticLockHandleException("存入团期内容失败，请重试。");
		}
		return "redirect:"+Global.getAdminPath()+"/stock/manager/apartGroup/detail?id="+id+"&agentId="+(agentId==null?"":agentId);
	}
	
	
	
	@RequestMapping(value="productGroupOrdersDetail")
	@ResponseBody
	public Object productGroupOrdersDetail(@RequestParam(required=true)Long productGroupId,@RequestParam(required=true)String status,Model model) {
		return stockService.findProductGroupOrders(productGroupId, status);
	}	
 	
	
	@RequestMapping(value="agentInfo")
	@ResponseBody
	public Object getAgentInfo(@RequestParam(required=true)Long agentId,Model model) {
		Map<String,Object> agentinfoMap = new HashMap<String,Object>();
		if(agentId!=null) {
			Agentinfo agentinfo = stockService.findOne(agentId);
			List<User> salerUser = UserUtils.getUserListByIds(agentinfo.getAgentSalerId());
			agentinfo.setAgentSalerUser(salerUser);
			agentinfoMap.put("agentSalerUser", agentinfoService.getSalerJsonStr(salerUser));
			agentinfoMap.put("id", agentinfo.getId());
			agentinfoMap.put("agentName", agentinfo.getAgentName());
			return agentinfoMap;
		}else{
			return new HashMap<String,Object>();
		}
	}
	
	
	/**
	 * 订单售出切位详情
	 * @author liangjingming
	 */
	@ResponseBody
	@RequestMapping(value="soldNopayPosition/{productGroupId}")
	public Object soldNopayPosition(@PathVariable String productGroupId){
		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> soldNopanList = Lists.newArrayList();
		soldNopanList= stockService.findSoldNopayPosition(Long.parseLong(productGroupId));
		   if(soldNopanList!=null && soldNopanList.size()!=0){
	            for(Map<String, Object> objs:soldNopanList){
	                Map<String, Object> data = new HashMap<String, Object>();
	                data.putAll(objs);
	                Object payStatus = null;
	                //已付金额
	                if(objs.get("payMoney")==null) data.put("payMoney", " ");
	                
	                if(objs.get("createUserName")==null) data.put("createUserName", " ");
	                
	                //订单状态
	                if(objs.get("pay")!=null){
	                   for(Dict dict:DictUtils.getDictList("order_pay_status")){
	                     if( (objs.get("pay").toString()).equals(dict.getValue()))
	                        payStatus = dict.getLabel();	                   
	                   }
	                }else {
	                   payStatus= " ";
	                }
	                data.put("payStatus",payStatus);	                
	                datas.add(data);
	            }
	        }
	        return datas;
		/*
		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();	
	    List<Object[]> soldNopanList = Lists.newArrayList();
		soldNopanList = stockService.findSoldNopayPosition(Long.parseLong(productGroupId));
		if(soldNopanList!=null && soldNopanList.size()!=0){
			for(Object[] objs:soldNopanList){
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("orderCompanyName", objs[0]);
				data.put("payReservePosition", objs[1]);
				data.put("leftpayReservePosition", objs[2]);
				data.put("orderPersonName", objs[3]);
				data.put("orderNum", objs[4]);
				data.put("totalMoney", objs[5]);
				data.put("payed_money", objs[6]);
				data.put("orderPersonNum", objs[7]);
				Object payStatus = null;
				for(Dict dict:DictUtils.getDictList("order_pay_status")){
					if((objs[8].toString()).equals(dict.getValue()))
					payStatus = dict.getLabel();
				}
				data.put("payStatus",payStatus);
				data.put("remark", objs[9]);
				data.put("orderCompany", objs[10]);
				datas.add(data);
			}
		}	
		return datas; */
	}
	
	@RequestMapping(value="uploadform")
	public String uploadform(@RequestParam("srcActivityId") Long srcActivityId,
			@RequestParam("activityGroupId") Long activityGroupId,@RequestParam("agentId") Long agentId, Model model) {
		List<ActivityReserveFile> activityReserveFileList = stockService.findByAgentIdAndSrcActivityIdAndActivityGroupId(agentId, srcActivityId, activityGroupId);
		model.addAttribute("activityReserveFileList", activityReserveFileList);
		return "modules/stock/apartgroup/uploadform";
	}
	
	/**
	 * 团期详情
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="groupDetail")
	public String groupDetail(@RequestParam("srcActivityId")String srcActivityId,HttpServletRequest request,Model model) throws UnsupportedEncodingException {
		String groupCode = URLDecoder.decode(request.getParameter("groupCode"), "UTF-8");
		List<ProductOrderCommon> orderList = stockService.findProductorderByGroupCode(groupCode,srcActivityId);
		List<TravelActivity> activityList = stockService.findTravelActivityByGroupCode(srcActivityId);
		List<ActivityGroup> group = stockService.findGroup(groupCode,srcActivityId);
		model.addAttribute("orderList", orderList);
		model.addAttribute("groupCode", groupCode);
		if(!group.isEmpty())
			model.addAttribute("group", group.get(0));
		model.addAttribute("travelActivity", activityList.get(0));
		return "modules/stock/apartgroup/groupDetail";
	}
	/**
	 * 修改团期备注
	 */
	@ResponseBody
	@RequestMapping(value="groupRemarks")
	public String groupRemarks(@RequestParam("groupId")String groupId,@RequestParam("remarks")String remarks) {
		ActivityGroup group = activityGroupService.findById(StringUtils.toLong(groupId));
		List<ActivityGroup> list = new ArrayList<ActivityGroup>();
		try {
			group.setRemarks(remarks);
			Set<ActivityGroup> set = new HashSet<ActivityGroup>();
			set.add(group);
			activityGroupService.save(set);
			list.add(group);
		} catch (Exception e) {
			return "";
		}
		return remarks;
	}
	
    @RequestMapping(value="uploadme/{srcActivityId}/{activityGroupId}/{agentId}/{reserveOrderId}")
    public String uploadme(@PathVariable Long srcActivityId,@PathVariable Long activityGroupId,@PathVariable Long agentId, @PathVariable Long reserveOrderId,Model model) {
        model.addAttribute("srcActivityId", srcActivityId);
        model.addAttribute("activityGroupId", activityGroupId);
        model.addAttribute("agentId", agentId);
        model.addAttribute("reserveOrderId", reserveOrderId);
        return "modules/stock/apartgroup/uploadme";
    } 
    
	
	/**
	 * 上传支付凭证
	 */
	@RequestMapping(value="upload")
	public String upload(@RequestParam("srcActivityId") Long srcActivityId,
			@RequestParam("activityGroupId") Long activityGroupId,@RequestParam("agentId") Long agentId,@RequestParam("reserveOrderId") Long reserveOrderId,
			@RequestParam(value = "payVoucher", required = false) List<MultipartFile> files, Model model, RedirectAttributes redirectAttributes) {
	    DocInfo docInfo =null;
	    if(files!=null){
	        try {
	        	int count = 0;
		    	for(MultipartFile file:files){
			        String fileName = file.getOriginalFilename();
			        if(StringUtils.isNotBlank(fileName)){
			        	++count;
				        docInfo = new DocInfo();
		//	        //保存  
			            String path = FileUtils.uploadFile(file.getInputStream(),fileName);
			            docInfo.setDocName(fileName);
			            docInfo.setDocPath(path);
		//	            //保存附件表数据
			    		ActivityReserveFile actReserve = new ActivityReserveFile();
			    		actReserve.setSrcActivityId(srcActivityId);
			    		actReserve.setActivityGroupId(activityGroupId);
			    		actReserve.setAgentId(agentId);
			    		actReserve.setReserveOrderId(reserveOrderId);
			    		actReserve.setFileName(fileName);
			    		stockService.saveOrderPay(docInfo,actReserve);
			        }
		    	}
		    	if(count>0){
		    		addMessage(redirectAttributes, "已成功上传 "+count+" 个文件!");
		    	}
	        } catch (Exception e) {  
	            logger.error("error:",e);
		    	addMessage(redirectAttributes, "上传文件失败!");
	        }
	    }
	    return "redirect:"+ Global.getAdminPath()+"/stock/manager/apartGroup/detail?id="+ srcActivityId+"&activityGroupId="+activityGroupId;
		//return "redirect:"+Global.getAdminPath()+"/stock/manager/apartGroup/uploadform?srcActivityId="+srcActivityId+"&activityGroupId="+activityGroupId+"&agentId="+agentId;
	}
	/**
	 * 查询团期的切位数
	 */
	@ResponseBody
	@RequestMapping(value="payReservePosition")
	public List<ActivityGroupReserve> getPayReservePosition(@RequestParam("srcActivityId") String srcActivityId,HttpServletRequest request) {
	   Long agentId = UserUtils.getUser().getId();
	   String agentIds= request.getParameter("agentId");
	   if(StringUtils.isNotBlank(agentIds)){
	       agentId = Long.parseLong(agentIds);
	   }
	   List<ActivityGroupReserve> list = stockService.findPayReservePosition(StringUtils.toLong(srcActivityId),agentId);
	   return list;
//    	if(stockService.findPayReservePosition(StringUtils.toLong(srcActivityId),agentId)!=null)
//    		return stockService.findPayReservePosition(StringUtils.toLong(srcActivityId),agentId);
//		return null;
	}
	
	/**
	 * 根据团期ID查询该团期所有切位
	 * 
	 */
	
	@ResponseBody
	@RequestMapping(value="getReserveByGroupId")
	public List<ActivityGroupReserve> getReserveByGroupId(@RequestParam Long activityGroupId){
		List<ActivityGroupReserve> activityGroupReserveList=stockService.findPayReserveByGroupId(activityGroupId);
		List<ActivityGroupReserve> activityGroupReserveListReturn=new LinkedList<ActivityGroupReserve>();
		
		for(ActivityGroupReserve activityGroupReserve:activityGroupReserveList){
			if(activityGroupReserve.getLeftpayReservePosition()>0){
				activityGroupReserveListReturn.add(activityGroupReserve);
			}
		}
		return activityGroupReserveListReturn;
	}
	/**
	 * 
	 *  功能:散拼切位订单信息列表
	 *
	 *  @author ruyi.chen
	 *  @DateTime 2014-12-18
	 *  @param travelActivity
	 *  @param request
	 *  @param response
	 *  @param model
	 *  @return
	 */
	@RequestMapping(value="getReserveOrderList")
	public String getReserveOrderList(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,Model model){
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
				
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
		String paymentType =  request.getParameter("paymentType");
		String wholeSalerKey = request.getParameter("wholeSalerKey");
		travelActivity.setAcitivityName(wholeSalerKey);
		Long agentId = null;
		if(StringUtils.isNotBlank(request.getParameter("agentId"))){
			try {
				agentId = StringUtils.toLong(request.getParameter("agentId"));
			} catch (NumberFormatException e) {
			}
		}		
	
		Page<ActivityReserveOrderView> page = activityGroupViewService.findReserveOrderList(new Page<ActivityReserveOrderView>(request, response), travelActivity,  
				settlementAdultPriceStart, settlementAdultPriceEnd, agentId,paymentType, common);
		
		
//		Page<ActivityReserveOrder> page2 = activityGroupViewService.findReserveOrderListInfo(new Page<ActivityReserveOrder>(request, response), travelActivity,  
//				settlementAdultPriceStart, settlementAdultPriceEnd, agentId, common);targetAreaIds
		model.addAttribute("targetAreaName", travelActivity.getTargetAreaNamess());
		model.addAttribute("targetAreaIds", travelActivity.getTargetAreaIds());
		model.addAttribute("travelActivity", travelActivity);
        model.addAttribute("page", page);
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		model.addAttribute("paymentType", request.getParameter("paymentType"));
//		model.addAttribute("targetAreaList", activityGroupViewService.findTargetArea());
		model.addAttribute("departmentList", departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId()));
	    return "modules/stock/reserveOrder/reserveOrderList";
	}
	/**
	 * 查看散拼切位退款列表
	 * @author  chenry
	 * add Date 2014-12-22
	 */
	@RequestMapping(value = "viewGroupRefund")
	public String viewGroupRefund(Model model,HttpServletRequest request,HttpServletResponse response,Long orderId){
		StringBuffer sbf=new StringBuffer();
		LinkedHashMap<String, List<RefundBean>> pageOrder=orderService.getGroupRefundReviewInfo(LOOSE_RESERVE_PRODUCT_TYPE, LOOSE_RESERVE_FLOW_TYPE, orderId,sbf);
		//判断批次审核是否结束
		if(sbf.length() > 0){
			model.addAttribute("sign", 1);
		}else{
			model.addAttribute("sign", 0);
		}
		model.addAttribute("page", pageOrder);
		model.addAttribute("orderId", orderId);	
		return "modules/stock/reserveOrder/viewGroupRefund";
	}
	
	/**
	 * 查看机票切位退款列表
	 * @author  chenry
	 * add Date 2014-12-22
	 */
	@RequestMapping(value = "viewAirTicketRefund")
	public String viewAirTicketRefund(Model model,HttpServletRequest request,HttpServletResponse response,Long orderId){
		StringBuffer sbf=new StringBuffer();
		LinkedHashMap<String, List<RefundBean>> pageOrder=orderService.getGroupRefundReviewInfo(AIR_TICKET_RESERVE_PRODUCT_TYPE, LOOSE_RESERVE_FLOW_TYPE, orderId,sbf);
		model.addAttribute("page", pageOrder);
		model.addAttribute("orderId", orderId);	
		model.addAttribute("airTicketMark", "airTicketMark");
		return "modules/stock/reserveOrder/viewGroupRefund";
	}
	
	/**
	 * 散拼切位申请退款
	 * @author  ruyi.chen
	 * add Date 2014-12-22
	 */
	@RequestMapping(value = "applyGroupRefund")
	public String applyGroupRefund(Model model,HttpServletRequest request,HttpServletResponse response,Integer aid){
		ActivityReserveOrderView av=activityGroupViewService.findReserveOrderInfo(aid);
		List<Currency> currencyList = currencyService
				.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("refundInfo", av);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId",aid);

		TravelActivity product = travelActivityService.findById(av.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(av.getId());
		model.addAttribute("productGroup",productGroup);
		return "modules/stock/reserveOrder/applyGroupRefund";
	}
	
	/**
	 * 机票切位申请退款
	 * @author  ruyi.chen
	 * add Date 2014-12-22
	 */
	@RequestMapping(value = "applyAirTicketRefund")
	public String applyAirTicketRefund(Model model,HttpServletRequest request,HttpServletResponse response,Long aid){
		List<Currency> currencyList = currencyService
				.findCurrencyList(UserUtils.getUser().getCompany().getId());
		
		ActivityReserveOrder ao = activityReserveOrderService.findOne(aid);
		String agentName = agentinfoService.findOne(ao.getAgentId()).getAgentName();
				
		model.addAttribute("agentName", agentName);
		model.addAttribute("currencyName", CurrencyUtils.getCurrencyInfo(ao.getMoneyType().toString(), 0, "name"));
		model.addAttribute("refundInfo", ao);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId",aid);
		
		ActivityAirTicket at = activityAirTicketService.getActivityAirTicketById(ao.getSrcActivityId());
		model.addAttribute("activityAirTicket", at);

		return "modules/stock/reserveOrder/applyAirTicketRefund";
	}
	/**
	 * 提交保存切位退款申请，发起审核流程
	 * @author  chenry
	 * add Date 2014-12-23
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@ResponseBody
	@RequestMapping(value="saveRefundInfo")
	public Map<String,Object> saveRefundInfo(Model model,HttpServletRequest request, HttpServletResponse response,Integer productType,Integer flowType, String remark,Long orderId,String refundRecordsStr) {
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			List<RefundBean> refundRecords = net.sf.json.JSONArray.toList(
					net.sf.json.JSONArray.fromObject(refundRecordsStr), RefundBean.class);
			if (CollectionUtils.isNotEmpty(refundRecords)) {
				Currency currency = currencyService.getRMBCurrencyId();
				if (currency != null) {
					for (RefundBean bean : refundRecords) {
						bean.setCurrencyId(currency.getId().toString());
					}
				}
			}
			/////////////////////////////////////////////////////////////
			//添加退款互斥情况验证，若游客或者团队有退款流程或者互斥流程进行中，则不能进行退款申请操作
			Map<String,String> travelers = Maps.newHashMap();
			List<Long> travelerIds = getRefundBeanIds(refundRecords,travelers);			
			Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(orderId, productType.toString(), Context.REVIEW_FLOWTYPE_REFUND, travelerIds);
			Map<String,Object> resultMap = (Map<String,Object>) rMap.get(Context.MUTEX_RESULT_lIST);
			boolean flag = false;
			StringBuffer sf = new StringBuffer();
			for(Long tid : travelerIds){
				if("1".equals(resultMap.get(tid.toString()).toString().split("/")[0])){
					flag = true;
					sf.append(travelers.get(tid.toString())+" "+resultMap.get(tid.toString()).toString().split("/")[1]+" ");
				}
			}
			if(flag){
				result.put("sign", 0);
				result.put("result", sf);
				return result;
			}
			//流程互斥部分结束
			/////////////////////////////////////////////////////////////				
			result = orderService.saveGroupRefundReviewInfo(refundRecords, productType, flowType, orderId);
		} catch (Exception e) {
			result.put("sign", 0);
			result.put("result", e.getMessage());
			return result;
		}
		
		return result;
	}
	private List<Long>getRefundBeanIds(List<RefundBean> beans,Map<String,String> travelers){
		Set<Long> set = Sets.newHashSet();
		List<Long> beanIds = Lists.newArrayList();
		
		for(RefundBean bean : beans){
			if(null == bean.getTravelerId()){
				set.add((long)0);
				if(null == bean.getTravelerName()){
					bean.setTravelerName("团队");
				}
				travelers.put("0", bean.getTravelerName());
			}else{
				set.add(Long.parseLong(bean.getTravelerId()));
				travelers.put(bean.getTravelerId(), bean.getTravelerName());
			}
		}
		Iterator<Long> it = set.iterator();
		while(it.hasNext()){
			beanIds.add(it.next());
		}
		return beanIds;
	}
		/**
		 * 查看散拼切位退款审核详情
		 * @author ruyi.chen
		 * add Date 2014 12-23
		 */
		@RequestMapping(value = "viewApplyRefundInfo")
		public String viewApplyRefundInfo(Model model,HttpServletRequest request,HttpServletResponse response,Integer productType,Long rid,Long orderId,Integer aid){
			Map<String, Object> travelerList=orderService.getReserveRefundInfoById(rid);
			model.addAttribute("hashMap", travelerList);
			
			//添加一个机票产品的切位退款申请详情
			if(9 == productType) {
				ActivityReserveOrderView av=activityGroupViewService.findReserveOrderInfo(aid);
				model.addAttribute("refundInfo", av);
				TravelActivity product = travelActivityService.findById(av.getProductId());
				model.addAttribute("product", product);
				ActivityGroup productGroup = activityGroupService.findById(av.getId());
				model.addAttribute("productGroup",productGroup);
				
				return "modules/stock/reserveOrder/viewRefundInfo";
				
				
			}else if (10 == productType){
				ActivityReserveOrder ao = activityReserveOrderService.findOne(aid.longValue());
				model.addAttribute("refundInfo", ao);
				model.addAttribute("orderId",aid);
				ActivityAirTicket at = activityAirTicketService.getActivityAirTicketById(ao.getSrcActivityId());
				model.addAttribute("activityAirTicket", at);
				
				return "modules/stock/reserveOrder/viewAirTicketRefundInfo";
			}else{
				return "";
			}
		}
		
		/**
		 * 批量切位页面信息
		 * @Description: 
		 * @param @param model
		 * @param @param request
		 * @param @param response
		 * @param @return   
		 * @return String  
		 * @throws
		 * @author majiancheng
		 * @date 2015-11-30 下午2:22:20
		 */
		@RequiresPermissions("stock:loose:batchreserve")
		@RequestMapping(value = "batchReceiveInfo")
		public String batchReceiveInfo(Model model,HttpServletRequest request,HttpServletResponse response) {
			String channels = request.getParameter("channels");
			if(StringUtils.isNotEmpty(channels)) {
				List<String> agentInfoIdList = Arrays.asList(channels.split(","));
				List<Agentinfo> agentinfos = agentinfoService.findAgentByIdsWithSaler(agentInfoIdList);
				model.addAttribute("agentinfos", agentinfos);
				
				//初始化页面所需的渠道json信息{channelName:"西安国旅 凯德广场门市部",channelCode:319,salerName:"张三"}
				model.addAttribute("agentJsonInfos", agentinfoService.getAgentinfoJsonBean(agentinfos));
			}
			
			model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
			
			List<Dict> payTypes = DictUtils.getDictList(Context.ORDER_PAYTYPE);
			model.addAttribute("payTypes", payTypes);
			model.addAttribute("currDate", new Date());
			
			return "modules/stock/apartgroup/batchReceiveInfo";
		}
		
		/**
		 * 批量切位操作
		 * @Description: 
		 * @param @param model
		 * @param @param request
		 * @param @param response
		 * @param @return   
		 * @return String  
		 * @throws
		 * @author majiancheng
		 * @date 2015-11-30 下午6:26:30
		 */
		@ResponseBody
		@RequestMapping(value = "batchReceive")
		public String batchReceive(Model model,HttpServletRequest request,HttpServletResponse response) {
			String reserveJsonData = request.getParameter("reserveJsonData");
			String uploadJsonData = request.getParameter("uploadJsonData");
			Map<String, String> data = new HashMap<String, String>();
			
			try{
				data = stockService.batchReceive(reserveJsonData, uploadJsonData, request);
			} catch(Exception e) {
				if((e.getMessage().endsWith("此团期没有足够的余位")) || (e.getMessage().endsWith("有产品团期不存在"))) {
					data.put("result", "2");
					data.put("message", e.getMessage());
				} else {
					data.put("result", "3");
					data.put("message", "系统出现异常，请重试！");
				}
				
				e.printStackTrace();
			}
			
			return JSON.toJSONStringWithDateFormat(data, "yyyy-MM-dd");
		}
		
		/**
		 * 批量归还切位页面信息
		 * @Description: 
		 * @param @param model
		 * @param @param request
		 * @param @param response
		 * @param @return   
		 * @return String  
		 * @throws
		 * @author majiancheng
		 * @date 2015-11-30 下午6:24:32
		 */
		@RequiresPermissions("stock:loose:batchreserve")
		@RequestMapping(value = "batchReturnReceiveInfo")
		public String batchReturnReceiveInfo(Model model,HttpServletRequest request,HttpServletResponse response) {
			String channels = request.getParameter("channels");
			if(StringUtils.isNotEmpty(channels)) {
				List<String> agentInfoIdList = Arrays.asList(channels.split(","));
				List<Agentinfo> agentinfos = agentinfoService.findAgentByIdsWithSaler(agentInfoIdList);
				model.addAttribute("agentinfos", agentinfos);
				

				//初始化页面所需的渠道json信息{channelName:"西安国旅 凯德广场门市部",channelCode:319,salerName:"张三"}
				model.addAttribute("agentJsonInfos", agentinfoService.getAgentinfoJsonBean(agentinfos));
			}
			
			model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
			
			return "modules/stock/apartgroup/batchReturnReceiveInfo";
		}
		
		/**
		 * 批量归还切位操作
		 * @Description: 
		 * @param @param model
		 * @param @param request
		 * @param @param response
		 * @param @return   
		 * @return String  
		 * @throws
		 * @author majiancheng
		 * @date 2015-11-30 下午6:25:38
		 */
		@ResponseBody
		@RequestMapping(value = "batchReturnReceive")
		public String batchReturnReceive(Model model,HttpServletRequest request,HttpServletResponse response) {
			Map<String, String> data = new HashMap<String, String>();
			String returnJsonStr = request.getParameter("returnJsonStr");//批量归还切位的json对象
			String returnType = request.getParameter("returnType");//批量切位的类型--->散拼0,机票1
			if(StringUtils.isNotBlank(returnJsonStr)&&StringUtils.isNotBlank(returnType)){
				List<ReturnReserveJsonBean> list = JSON.parseArray(returnJsonStr, ReturnReserveJsonBean.class);
				String returnRemark=request.getParameter("returnRemark");
				if (returnRemark==null) returnRemark="";
				model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
				try {
					stockService.batchReturnReserve(list, Integer.parseInt(returnType), request);
					data.put("result", "1");
					data.put("message", "批量归还切位成功");
				} catch (Exception e) {
					e.printStackTrace();
					data.put("result", "2");
					data.put("message", "批量归还切位失败");
				}
			}
			return JSON.toJSONStringWithDateFormat(data, "yyyy-MM-dd");
		}
		
		/**
		 * 切位时弹出添加团期弹出框
		 * @Description: 
		 * @param @return   
		 * @return String  
		 * @throws
		 * @author majiancheng
		 * @date 2015-12-1 下午5:21:50
		 */
		@RequestMapping(value = "getProductGroupPage")
		public String getProductGroupPage(Model model,HttpServletRequest request,HttpServletResponse response) {
			//勾选的团期
			String selectedProducts = request.getParameter("selectedProducts");
			model.addAttribute("selectedProducts", selectedProducts);
			
			//切位和归还切位状态标识
			String source = request.getParameter("source");
			model.addAttribute("source", source);
			
			//渠道商id
			String agentId = request.getParameter("agentId");
			model.addAttribute("agentId", agentId);
			
			return "modules/stock/apartgroup/productGroup";
		}
		
		
		/**
		 *  查询产品团期的切位信息列表 add by zhanghao
		 * @param sourceId 散拼团期ID/机票产品ID
		 * @param reserveType 散拼0,机票1
		 * @return Map<String,Object> key:code (成功：0000，失败：0001)
		 * 							  key:message 失败返回的信息
		 * 							  key:list 成功返回的数据信息	
		 */
		@ResponseBody
		@RequestMapping(value = "queryReserveList")
		public String queryReserveList(Long sourceId,Integer reserveType){
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("code", "0000");
			result.put("message", "");
			if(sourceId!=null && reserveType!=null){
				try {
					List<Map<String, Object>> list = this.stockService.queryReserveList4ReserveType(sourceId, reserveType);
					if(CollectionUtils.isNotEmpty(list)){
						result.put("list", list);
					}
				} catch (Exception e) {
					result.put("code", "0001");
					result.put("message", "查询库存切位信息异常，请联系管理员！");
					logger.error("查询库存切位信息异常，请联系管理员！",e);
				}
			}else{
				result.put("code", "0001");
				result.put("message", "传递参数异常！");
			}
			
			
			return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd");
		}
}
