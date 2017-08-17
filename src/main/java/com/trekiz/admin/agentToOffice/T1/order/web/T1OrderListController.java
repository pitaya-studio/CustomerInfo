package com.trekiz.admin.agentToOffice.T1.order.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.agentToOffice.T1.order.service.T1OrderListService;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityFile;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityFileService;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
/**
 * T1平台：订单查询
 * @author yakun.bai
 * @Date 2016-5-10
 */
@Controller
@RequestMapping(value = "${adminPath}/t1/orderList/manage")
public class T1OrderListController extends BaseController {
    
    protected static final Logger logger = LoggerFactory.getLogger(T1OrderListController.class);

	@Autowired
	private OrderCommonService orderService;
    @Autowired
    private T1OrderListService orderListService;
    @Autowired
    private AgentinfoService agentinfoService;
    @Autowired
    private MoneyAmountService moneyAmountService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private ActivityFileService activityFileService;
	@Autowired
	private DocInfoService docInfoService;
   
	
	@RequestMapping(value ="updateHasSeen")
	@ResponseBody
	public Object updateHasSeen(HttpServletResponse response,HttpServletRequest request) throws Exception {
		JSONObject resobj = new JSONObject();
		  //查询新增订单数  hasSeen
		  Map<String, String> mapRequest = Maps.newHashMap();
		  mapRequest.put("orderOrGroup", "order");
		  mapRequest.put("hasSeen", "1");
		  orderListService.updateOrderSeenBatch(response, request, mapRequest);
		  resobj.put("flag", "ok");
		return resobj;
	}
	
	@RequestMapping(value ="countHasSeen")
	@ResponseBody
	public Object countHasSeen(HttpServletResponse response,HttpServletRequest request) throws Exception {
		JSONObject resobj = new JSONObject();
		  //查询新增订单数  hasSeen
		 Map<String, String> mapRequest = Maps.newHashMap();
		  mapRequest.put("orderOrGroup", "order");
		  mapRequest.put("hasSeen", "1");
		  Page<Map<Object, Object>> pageOrder = orderListService.getOrderList(new Page<Map<Object, Object>>(request,response,-1), new TravelActivity(), mapRequest,"0");
		  resobj.put("hasSeenCount",  pageOrder.getList().size());
		return resobj;
	}
   
	/**
	 * 订单查询：T1平台
	 * @author yakun.bai
	 * @Date 2016-5-10
	 */
	@RequestMapping(value ="showOrderList/{orderStatus}")
	public String showOrderList(@PathVariable String orderStatus, @ModelAttribute TravelActivity travelActivity, HttpServletResponse response, 
			Model model, HttpServletRequest request) throws Exception {
		
		//查询条件
        Map<String, String> mapRequest = Maps.newHashMap();
        
        //参数处理：去除空格和处理特殊字符并传递到后台
        //参数解释：订单号或产品名称或团号、供应商ID、订单开始时间、订单结束时间、团期开始时间、团期结束时间,支付状态
        String paras = "orderNumOrProductNameOrGroupCode,supplierId,orderTimeBegin,orderTimeEnd,groupOpenDateBegin,groupOpenDateEnd,payStatus,moneyStrMin,moneyStrMax,inlineStatus,orderType";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        
        //订单或团期列表标识：order为订单、group为团期，默认查询团期列表
        String orderOrGroup = request.getParameter("orderOrGroup");
        if (StringUtils.isBlank(orderOrGroup)) {
        	orderOrGroup = "order";
        }
        mapRequest.put("orderOrGroup", orderOrGroup);
                
        //排序方式：默认为出团日期降序排列
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "orderTime DESC";
        }
        mapRequest.put("orderBy", orderBy);
        
        //订单或团期查询以及处理页码     //t1页面的翻页部分与t2不同
        Page<Map<Object, Object>> page = new Page<Map<Object, Object>>(request, response);
        page.setToStringFlag("t1ForProductAbove");
        
		page.setOrderBy(orderBy);
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotBlank(pageNo)){
			page.setPageNo(Integer.valueOf(pageNo));
		}
		if(StringUtils.isNotBlank(pageSize)){
			page.setPageSize(Integer.valueOf(pageSize));
		}
        
        Page<Map<Object, Object>> pageOrder = (Page<Map<Object, Object>>) orderListService.getOrderList(page, travelActivity, mapRequest, orderStatus);
        model.addAttribute("page", pageOrder);
        
        Page<Map<Object, Object>> page2 = new Page<Map<Object, Object>>(request, response);
        BeanUtils.copyProperties(page, page2);
        page2.setToStringFlag("t1ForProductBottom");
		model.addAttribute("page2", page2);
        
        List<Map<Object, Object>> listorder = pageOrder.getList();
        orderBy = pageOrder.getOrderBy().replace("agp", "pro");
        pageOrder.setOrderBy(orderBy);
	        
        List<String> groupIdList = Lists.newArrayList();
        
        for (Map<Object, Object> listin : listorder) {
        	if ("group".equals(orderOrGroup) && listin.get("id") != null) {
        		groupIdList.add(listin.get("id").toString());
        	} else if ("order".equals(orderOrGroup) && listin.get("id") != null) {
        		// 金额处理
            	handlePrice(listin);
            	// 差额返还
            	if (null != listin.get("differenceFlag") && listin.get("differenceFlag").toString().equals("1")) {
            		String differenceMoney = moneyAmountService.getMoney(listin.get("differenceMoney").toString());
            		listin.put("differenceMoney", differenceMoney);
            	}
        	}
        	// 行程单ID
        	String activityId = listin.get("activityId").toString();
    		List<ActivityFile> activityFileList = activityFileService.findFileListByPid(Integer.parseInt(activityId));
    		String docIds = "";
    		if (CollectionUtils.isNotEmpty(activityFileList)) {
    			for (ActivityFile activityFile : activityFileList) {
    				if (activityFile.getDocInfo() != null) {
    					docIds += activityFile.getDocInfo().getId() + ",";
    				}
    			}
    		}
    		listin.put("docIds", docIds);
        }
        
        List<Map<Object, Object>> orderList = null;
        if ("group".equals(orderOrGroup) && CollectionUtils.isNotEmpty(groupIdList)) {
        	orderList = orderListService.findByGroupIds(new Page<Map<Object, Object>>(request, response), groupIdList, mapRequest.get("orderSql")).getList();
        	
        	for (Map<Object, Object> listin : orderList) {
        		//金额处理
        		handlePrice(listin);
	        }
        }
        
        String setTab = request.getParameter("setTab");
        if(StringUtils.isBlank(setTab)){
        	setTab = "setTab01";
        }
        model.addAttribute("setTab", setTab);
        model.addAttribute("orderStatus", orderStatus); 
        model.addAttribute("orderOrGroup", orderOrGroup);
        model.addAttribute("orders", orderList);
        model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
        model.addAttribute("fromAreas", DictUtils.getVisaTypeMap("from_area"));
        model.addAttribute("officeList", officeService.findByShelfRightsStatus(0));
        model.addAttribute("ctxs",1);
        return "/agentToOffice/T1/order/t1OrderList";
	}	
	
	
	@RequestMapping(value ="downloadConfirmFiles")
	public String downloadConfirmFiles(HttpServletRequest request,Model model){
		String orderId = request.getParameter("orderId");
		if(!StringUtils.isBlank(orderId)) {
    		ProductOrderCommon order = orderService.getProductorderById(Long.parseLong(orderId));
    		String docFileIds = order.getConfirmationFileId();
    		String[] fileIds = docFileIds.replace(",", " ").trim().split(" ");
    		List<DocInfo> fileList = new ArrayList<DocInfo>();
    		for(String fileId:fileIds){
    			fileList.add(docInfoService.getDocInfo(new Long(fileId)));
    		}
    		model.addAttribute("docList", fileList);
    		model.addAttribute("orderId", orderId);
    		model.addAttribute("downloadUrl", "t1/orderList/manage/userDownloadConfirmFile");
		}
	    return "include/downloadFiles";
	}
	
	@RequestMapping(value ="userDownloadConfirmFile/{orderId}/{docid}")
	public String userDownloadConfirmFile(@PathVariable("docid") Long docid,@PathVariable("orderId") Long orderId,HttpServletResponse response) {
		return "redirect:" + Global.getAdminPath() + "/sys/docinfo/download/" + docid;
	}
	
	
	/**
	 * 金额处理：金额千位符与金额多币种id和数值读取
	 * @param listin
	 */
	private void handlePrice(Map<Object, Object> listin) {
		
		// 千位符处理：订单总金额、已付金额、到账金额
		List<String> priceList = Lists.newArrayList();
		priceList.add("remainderMoney");
		priceList.add("totalMoney");
		priceList.add("payedMoney");
		priceList.add("accountedMoney");
		
		// 未达帐金额
		String payedMoney = listin.get("payedMoney") != null ? listin.get("payedMoney").toString() : "";
		String accountedMoney = listin.get("accountedMoney") != null ? listin.get("accountedMoney").toString() : "";
		String notAccountedMoney = moneyAmountService.addOrSubtract(payedMoney, accountedMoney, false);
		if (StringUtils.isNoneBlank(notAccountedMoney)) {
			listin.put("notAccountedMoney", notAccountedMoney.replaceAll(" ", ""));
		} else {
			listin.put("notAccountedMoney", "");
		}
		
		handlePrice(listin, priceList);
	}
	
	/**
	 * 订单金额千位符处理：订单总金额、已付金额、到账金额
	 * @param listin
	 * @param paraList
	 */
	private void handlePrice(Map<Object, Object> listin, List<String> paraList) {
		
		//千位符
		DecimalFormat d = new DecimalFormat(",##0.00");
		if (CollectionUtils.isNotEmpty(paraList)) {
			for (String para : paraList) {
				if (listin.get(para) != null) {
					String moneyStr = listin.get(para).toString();
					String allMoney [] = moneyStr.split("\\+");
					
					if ( listin.get("totalMoney") != null && ("remainderMoney".equals(para))) {
						//获取总额、已付 字符串 eg: ￥ 200+$ 100 
						String totalStr = listin.get("totalMoney").toString();
						if (listin.get("payedMoney") == null) {
							String totalArr [] = totalStr.split("\\+");
							List<String> totalList = Lists.newArrayList(totalArr);
							String tempMoneyStr = "";
							for (int i = 0; i < totalList.size(); i++) {
								String money [] = totalList.get(i).split(" ");
								if (money.length > 0 && !"0.00".equals(money[1])) {
									tempMoneyStr += money[0] + d.format(new BigDecimal(money[1])) + "+";
								}
							}
							if(StringUtils.isNotBlank(tempMoneyStr)) {
								listin.put(para, tempMoneyStr.substring(0, tempMoneyStr.length()-1));
							}
						}else {							
							String payedStr = listin.get("payedMoney").toString();
							//拆分字符串，得到 币种，金额 Map
							String totalArr [] = totalStr.split("\\+");
							String payedArr [] = payedStr.split("\\+");
							List<String> totalList = Lists.newArrayList(totalArr);
							List<String> payedList = Lists.newArrayList(payedArr);
							String remainderString = getRemainderMoneyStr(totalList, payedList);
							listin.put(para, remainderString);
						}
					}else{
						if (allMoney.length > 1) {
							String tempMoneyStr = "";
							for (int i=0;i<allMoney.length;i++) {
								String money [] = allMoney[i].split(" ");
								//币种价格等于0的时候不显示
								if (money.length > 1 && !"0.00".equals(money[1])) {
									tempMoneyStr += money[0] + d.format(new BigDecimal(money[1])) + "+";
								}
							}
							if(StringUtils.isNotBlank(tempMoneyStr)) {
								listin.put(para, tempMoneyStr.substring(0, tempMoneyStr.length()-1));
							}
						} else {
							String money [] = allMoney[0].split(" ");
							if (money.length > 1) {
								String currencyMark = money[0].toString();
								String currencyMoney = money[1].toString();
								String moneyAmonut = d.format(new BigDecimal(currencyMoney));
								listin.put(para, currencyMark + moneyAmonut);
							}
						}
					}
				}
			}
		}
	}
	
	private String getRemainderMoneyStr(List<String> totalList, List<String> payedList){
		//千位符
		DecimalFormat d = new DecimalFormat(",##0.00");
		List<String> reusltList = moneyAmountService.subtract(totalList, payedList);
		String tempMoneyStr = "";
		if (CollectionUtils.isNotEmpty(reusltList)) {			
			String currencyArr [] = reusltList.get(0).split("\\,");
			String amountArr [] = reusltList.get(1).split("\\,");
			for (int i = 0; i < amountArr.length; i++) {
				if (amountArr.length > 0 && !"0.00".equals(amountArr[0])) {
					tempMoneyStr += currencyArr[i] + d.format(new BigDecimal(amountArr[i])) + "+";
				}
			}
			if(StringUtils.isNotBlank(tempMoneyStr)) {
				tempMoneyStr = tempMoneyStr.substring(0, tempMoneyStr.length()-1);
			}
		}else{
			//如果未收余额为空，则默认显示为0.00
			tempMoneyStr = "0.00";
		}
		return tempMoneyStr;
	}
	

}

	