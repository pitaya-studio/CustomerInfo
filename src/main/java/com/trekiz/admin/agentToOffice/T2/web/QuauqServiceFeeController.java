package com.trekiz.admin.agentToOffice.T2.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.agentToOffice.T2.entity.QuauqServiceFee;
import com.trekiz.admin.agentToOffice.T2.service.QuauqServiceFeeService;
import com.trekiz.admin.agentToOffice.T2.utils.ExportExcelForAgentUtils;
import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/quauqAgent/manage")
public class QuauqServiceFeeController extends BaseController {
	@Autowired
	private QuauqServiceFeeService suauqServiceFeeService;
	@Autowired
	private OfficeService officeService;
/*	@Autowired
	private AgentinfoService agentinfoService;*/
	@Autowired
	private QuauqAgentInfoService quauqAgentInfoService;
		
	/**
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("all")
	// @RequiresPermissions("agent:grade:agent")
	@RequestMapping(value = "quauqServiceFeeStatistics")
	public String quauqServiceFeeStatisticsList(@ModelAttribute QuauqServiceFee fee, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String officeId = request.getParameter("officeId");
		String pageSize = request.getParameter("pageSize");
		if (StringUtils.isBlank(pageSize)) {
			pageSize = "20";
		}
		Page<Map<String,Object>> quauqServiceFeeStatistics = suauqServiceFeeService.getQuauqServiceFeeStatistics(
				new Page<Map<String,Object>>(request, response, Integer.parseInt(pageSize)), false, officeId);
		Page<Map<String,Object>> quauqServiceFeeStatisticsTotal = suauqServiceFeeService.getQuauqServiceFeeStatistics(
				new Page<QuauqServiceFee>(request, response,Integer.parseInt(pageSize)), true, officeId);
		
		Map<String, Object> map = suauqServiceFeeService.findSum2OrderAndPersonCount(); // 批发商订单数和人数总计
		Map<String,Object> totalMap = quauqServiceFeeStatisticsTotal.getList().get(0);
		
		totalMap.putAll(map);
		
		totalMap.put("quauqChargeTotal", suauqServiceFeeService.findSum2QuauqCharge(officeId)); // QUAUQ服务费总额
		totalMap.put("agentChargeTotal", suauqServiceFeeService.findSum2AgentCharge(officeId)); // 渠道服务费总额
		totalMap.put("cutChargeTotal", suauqServiceFeeService.findSum2CutCharge(officeId)); // 抽成服务费总额
		// 统计全部的QUAUQ服务费信息
		model.addAttribute("total", totalMap);
		// 统计按照供应商分别的QUAUQ服务费信息
		model.addAttribute("page", quauqServiceFeeStatistics);
		model.addAttribute("fee", fee);
		model.addAttribute("officeList", UserUtils.getOfficeList(true, null, null));
		return "agentToOffice/T2/quauqServiceFeeStatistics";
	}
	
	

	/**
	 * @Description 导出供应商交易服务费信息
	 * @author yakun.bai
	 * @Date 2016-5-5
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = { "downloadAllQuauqServiceFeeStatistics" })
	public void downloadAllOrder(@ModelAttribute QuauqServiceFee fee, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<Map<String,Object>> quauqServiceFeeStatisticsTotal = suauqServiceFeeService.getQuauqServiceFeeStatistics(
				new Page<Map<String,Object>>(request, response, -1), false, request.getParameter("officeId"));
		List<Object[]> orderList = Lists.newArrayList();
		
		try {
			if (CollectionUtils.isNotEmpty(quauqServiceFeeStatisticsTotal.getList())) {
				int i = 0;
				for (Map<String,Object> cell : quauqServiceFeeStatisticsTotal.getList()) {
					i++;
					Object[] obj = new Object[10];
					// 序号
					obj[0] = i;
					// 供应商名称
					obj[1] = cell.get("officeName");
					// 订单数
					obj[2] = cell.get("orderCount");
					// 人数
					obj[3] = cell.get("personCount");
					// 服务费总额
					obj[4] = cell.get("serviceFeeTotalCount");
					// QUAUQ服务费总额
					if(cell.get("quauqTotalMoney") == null){
						obj[5] = "¥0.00";
					}else{
						obj[5] = cell.get("quauqTotalMoney");
					}
					// 渠道服务费总额
					if(cell.get("agentTotalMoney") == null){
						obj[6] = "¥0.00";
					}else{
						obj[6] = cell.get("agentTotalMoney");
					}
					// 抽成服务费总额
					if(cell.get("cutTotalMoney") == null){
						obj[7] = "¥0.00";
					}else{
						obj[7] = cell.get("cutTotalMoney");
					}
					// 交易服务费已结清总额
					obj[8] = cell.get("serviceFeeSettled");
					// 交易服务费未结清总额
					obj[9] = cell.get("serviceFeeUnsettled");
					orderList.add(obj);
				}
			}

			// 文件名称
			String fileName = "供应商交易服务费信息";
			// Excel各行名称
			String[] cellTitle = { "序号", "供应商名称", "订单数", "人数", "服务费总额", "QUAUQ服务费总额", "渠道服务费总额", "抽成服务费总额", "交易服务费已结清总额", "交易服务费未结清总额" };
			// 文件首行标题
			String firstTitle = "供应商交易服务费信息";
			createExcle(fileName, orderList, cellTitle, firstTitle, null, request, response);
		} catch (Exception e) {
			logger.error("下载出错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 供应商下的交易明细
	 * @param agentinfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping(value ="tradeDetail")
	public String tradeDetail(@ModelAttribute Agentinfo agentinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 处理页面条件参数
		Map<String, String> mapRequest = handleParams(request, model);
		
        // 除了quauq后台，其余批发商只能看到自己下面交易服务费统计
        if(StringUtils.isBlank(mapRequest.get("officeId"))){  // 交易服务费统计菜单
        	model.addAttribute("listFlag", "quauq");// 交易服务费统计&&交易明细列表标识
        	if(!Context.SUPPLIER_UUID_QUAUQ.equals(UserUtils.getUser().getCompany().getUuid())){
        		mapRequest.put("officeId", UserUtils.getUser().getCompany().getId().toString());
        		model.addAttribute("officeId", UserUtils.getUser().getCompany().getId().toString());
        	}
        }
        if(StringUtils.isNotBlank(mapRequest.get("officeId"))){
        	model.addAttribute("officeName",officeService.get(Long.parseLong(mapRequest.get("officeId"))).getName());
        }
        
		// 查询交易明细信息
		Page<Map<Object, Object>> pageAgent = suauqServiceFeeService.quauqTradeDetail(new Page<Map<Object, Object>>(request, response), mapRequest);
		
		// 查询合计
		Map<String, Object> sumList = suauqServiceFeeService.findSum2Office(mapRequest);
		// 内部销售人员的名单
		model.addAttribute("useruuid", UserUtils.getUser().getId());
		model.addAttribute("agentinfo", agentinfo);
		model.addAttribute("officeList",officeService.findSyncOffice());
		model.addAttribute("agentList", quauqAgentInfoService.getAllQuauqAgentinfos());
		model.addAttribute("page", pageAgent);
		model.addAttribute("sumList", sumList);
		return "agentToOffice/T2/tradeDetail";
	}
	
	/**
	 * 设置改变缴费状态
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="changeIsPayedCharge")
	@ResponseBody
	public Object changeIsPayedCharge(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = Maps.newHashMap();
		
		String orderIds = request.getParameter("orderIds");
		String changeType = request.getParameter("changeType");
		if(StringUtils.isNotBlank(orderIds)){
			suauqServiceFeeService.changeOrderIsPayedCharge(orderIds, Integer.parseInt(changeType));

			// 处理页面条件参数
			Map<String, String> mapRequest = handleParams(request, model);
			
			// 查询合计 使得总计已缴费未缴费局部刷新
			Map<String, Object> sumList = suauqServiceFeeService.findSum2Office(mapRequest);
			map.put("result", "success");
			map.putAll(sumList);
		}else{
			map.put("result", "failed");
		}
		return map;
	}
	
	/**
	 * @Description 导出交易明细信息
	 * @Date 2016-6-17
	 */
	@RequestMapping(value = "downloadTradeOrder")
	public void downloadTradeOrder(@ModelAttribute QuauqServiceFee fee, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		// 处理页面条件参数
		Map<String, String> mapRequest = handleParams(request, model);
        String officeId = mapRequest.get("officeId");
        
		Page<Map<Object, Object>> pageAgent = suauqServiceFeeService.quauqTradeDetail(new Page<Map<Object, Object>>(request, response, -1), mapRequest);
		List<Object[]> orderList = Lists.newArrayList();
		
		// 获取订单状态并封装成map
		List<Dict> dictList = DictUtils.getDictList("order_pay_status");
		Map<String, String> dictMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(dictList)) {
			for (Dict dict : dictList) {
				dictMap.put(dict.getValue(), dict.getLabel());
			}
		}
		
		try {
			// Excel各行名称
			String[] cellTitle = null;
			if (CollectionUtils.isNotEmpty(pageAgent.getList()) && MapUtils.isNotEmpty(dictMap)) {
				if (UserUtils.getUser().getId() == 2060) {
					if (StringUtils.isBlank(officeId)) {
						getObjectForQuauqAllOffice(pageAgent.getList(),dictMap,orderList);
						cellTitle = new String[]{ "序号", "供应商名称","渠道名称", "下单日期", "出团日期", "团号", "订单号", "订单人数", "销售", "订单总额", "服务费总额", "QUAUQ服务费", "渠道服务费", "抽成服务费", "订单状态", "缴费状态" };
					} else {
						getObjectForQuauqSingleOffice(pageAgent.getList(),dictMap,orderList);
						cellTitle = new String[]{ "序号", "渠道名称", "下单日期", "出团日期", "团号", "订单号", "订单人数", "销售", "订单总额", "服务费总额", "QUAUQ服务费", "渠道服务费", "抽成服务费", "订单状态", "缴费状态" };
					}
				} else {
					getObjectForOffice(pageAgent.getList(),dictMap,orderList);
					cellTitle = new String[]{ "序号", "渠道名称", "下单日期", "出团日期", "团号", "订单号", "订单人数", "销售", "订单总额", "服务费总额", "订单状态", "缴费状态" };
				}
			}else{
				//当搜索结果为空，导出excel表格为空表
				cellTitle = new String[]{ "序号", "渠道名称", "下单日期", "出团日期", "团号", "订单号", "订单人数", "销售", "订单总额", "服务费总额", "订单状态", "缴费状态" };
			}
			// 文件名称
			String fileName = "交易明细";
			// 文件首行标题
			String firstTitle = "交易明细";
			createExcle(fileName, orderList, cellTitle, firstTitle, officeId, request, response);
			
		} catch (Exception e) {
			logger.error("下载出错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理交易明细列表页面条件参数
	 * @param request
	 * @param model
	 * @return
	 */
	private  Map<String, String> handleParams(HttpServletRequest request,  Model model){
		// 查询条件
        Map<String, String> mapRequest = Maps.newHashMap();
        
        // 参数处理：渠道名称、订单状态、创建开始日期、创建结束日期、缴费状态、订单编号、团号、批发商,出团日期
        String paras = "agentId,officeIds,orderStatus,orderTimeBegin,orderTimeEnd,orderNum,isPayedCharge,groupCode,officeId,groupTimeBegin,groupTimeEnd";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        
        return mapRequest;
	}
	
	/**
	 *  封装导出的交易明细统计数据--为quauqadmin账号导出所有批发商统计数据
	 * @param list
	 * @param dictMap
	 * @param orderList
	 */
	private void getObjectForQuauqAllOffice(List<Map<Object, Object>> list,Map<String, String> dictMap,List<Object[]> orderList){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int i = 0;
		for (Map<Object, Object> map : list) {
			i++;
			Object[] obj = new Object[16];
			// 序号
			obj[0] = i;
			//供应商名称
			obj[1] = map.get("officeName");
			// 渠道名称
			obj[2] = map.get("agentName");
			// 下单日期
			obj[3] = formatter.format(map.get("createDate"));
			// 出团日期
			obj[4] = map.get("groupOpenDate");
			// 团号
			obj[5] = map.get("groupCode");
			// 订单号
			obj[6] = map.get("orderNum");
			// 订单人数
			obj[7] = map.get("orderPersonNum");
			// 销售
			obj[8] = map.get("salename");
			// 订单总额
			obj[9] = map.get("totalMoney");
			// 服务费总额
			obj[10] = map.get("chargeMoney");
			// QUAUQ服务费
			obj[11] = map.get("quauqChargeMoney");
			// 渠道服务费
			obj[12] = map.get("agentChargeMoney");
			// 抽成服务费
			obj[13] = map.get("cutChargeMoney");
			// 订单状态
			obj[14] = dictMap.get(map.get("orderStatus").toString());
			// 缴费状态
			obj[15] = (Byte) map.get("isPayedCharge") == 1 ? "已缴费" : "未缴费";
			orderList.add(obj);
		}
	}
	
	/**
	 * 封装导出的交易明细统计数据--quauqadmin账号导出有某个批发商下的数据
	 * @param list
	 * @param dictMap
	 * @param orderList
	 */
	private void getObjectForQuauqSingleOffice(List<Map<Object, Object>> list,Map<String, String> dictMap,List<Object[]> orderList){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int i = 0;
		for (Map<Object, Object> map : list) {
			i++;
			Object[] obj = new Object[15];
			// 序号
			obj[0] = i;
			// 渠道名称
			obj[1] = map.get("agentName");
			// 下单日期
			obj[2] = formatter.format(map.get("createDate"));
			// 出团日期
			obj[3] = map.get("groupOpenDate");
			// 团号
			obj[4] = map.get("groupCode");
			// 订单号
			obj[5] = map.get("orderNum");
			// 订单人数
			obj[6] = map.get("orderPersonNum");
			// 销售
			obj[7] = map.get("salename");
			// 订单总额
			obj[8] = map.get("totalMoney");
			// 服务费总额
			obj[9] = map.get("chargeMoney");
			// QUAUQ服务费
			obj[10] = map.get("quauqChargeMoney");
			// 渠道服务费
			obj[11] = map.get("agentChargeMoney");
			// 抽成服务费
			obj[12] = map.get("cutChargeMoney");
			// 订单状态
			obj[13] = dictMap.get(map.get("orderStatus").toString());
			// 缴费状态
			obj[14] = (Byte) map.get("isPayedCharge") == 1 ? "已缴费" : "未缴费";
			orderList.add(obj);
		}
	}
	
	/**
	 * 封装导出的交易明细统计数据--为非quauqadmin账号
	 * @param list
	 * @param dictMap
	 * @param orderList
	 */
	private void getObjectForOffice(List<Map<Object, Object>> list,Map<String, String> dictMap,List<Object[]> orderList){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int i = 0;
		for (Map<Object, Object> map : list) {
			i++;
			Object[] obj = new Object[12];
			// 序号
			obj[0] = i;
			// 渠道名称
			obj[1] = map.get("agentName");
			// 下单日期
			obj[2] = formatter.format(map.get("createDate"));
			// 出团日期
			obj[3] = map.get("groupOpenDate");
			// 团号
			obj[4] = map.get("groupCode");
			// 订单号
			obj[5] = map.get("orderNum");
			// 订单人数
			obj[6] = map.get("orderPersonNum");
			// 销售
			obj[7] = map.get("salename");
			// 订单总额
			obj[8] = map.get("totalMoney");
			// 服务费总额
			obj[9] = map.get("chargeMoney");
			// 订单状态
			obj[10] = dictMap.get(map.get("orderStatus").toString());
			// 缴费状态
			obj[11] = (Byte) map.get("isPayedCharge") == 1 ? "已缴费" : "未缴费";
			orderList.add(obj);
		}
	}
	
	
	/**
	 * 生成、下载Excel文件(统计用)
	 * 
	 * @param fileName
	 *            ：文件名称
	 * @param list
	 *            ：要生成数据
	 * @param cellTitle
	 *            ：Excle各列名称
	 * @param firstTitle
	 *            ：Excel首行标题
	 */
	public static void createExcle(String fileName, List<Object[]> list,
			String[] cellTitle, String firstTitle , String officeId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int rowNum = 0;
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = ExportExcelForAgentUtils.creatSheet(workBook);
		
		HSSFRow titleRow = sheet.createRow((short) rowNum++);
		// 字体设置
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setFontHeight((short) HSSFFont.BOLDWEIGHT_NORMAL);
		font.setColor(HSSFFont.COLOR_NORMAL);

		// 样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);
		
		// 样式 订单总额 && 交易服务费总额
		HSSFCellStyle stylePay = workBook.createCellStyle();
		stylePay.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		stylePay.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font2 = (HSSFFont) workBook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font2.setColor(HSSFColor.RED.index);
		HSSFDataFormat format = workBook.createDataFormat();
		stylePay.setDataFormat(format.getFormat("#,##0.00"));
		stylePay.setFont(font2);
		// 交易服务费 && 已结清总额
		HSSFCellStyle styleAccount = workBook.createCellStyle();
		styleAccount.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleAccount.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font3 = (HSSFFont) workBook.createFont();
		font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font3.setColor(HSSFColor.GREEN.index);
		styleAccount.setDataFormat(format.getFormat("#,##0.00"));
		styleAccount.setFont(font3);
		
		short countColumn = 0;
		if (cellTitle != null) {
			countColumn = (short) cellTitle.length;
		}
		HSSFCell titleCell = titleRow.createCell( (countColumn / 2));
		titleRow.setHeight((short)500);//设置首行高度
		titleCell.setCellValue(firstTitle);
		titleCell.setCellStyle(cellStyle);
//		HSSFRow rowTwo = sheet.createRow(rowNum++);

		HSSFFont rowTwoCellFont = workBook.createFont();
		rowTwoCellFont.setFontName("宋体");
		rowTwoCellFont.setColor(HSSFFont.COLOR_NORMAL);

		HSSFCellStyle rowTwoCellStyle = workBook.createCellStyle();
		rowTwoCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		rowTwoCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		rowTwoCellStyle.setFont(rowTwoCellFont);

		if ("供应商交易服务费信息".equals(fileName)) {  // 针对服务费统计
			// 未结清总额
			HSSFCellStyle styleUnpay = workBook.createCellStyle();
			styleUnpay.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			styleUnpay.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			HSSFFont font4 = (HSSFFont) workBook.createFont();
			font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font4.setColor(HSSFColor.BLUE.index);
			styleUnpay.setDataFormat(format.getFormat("#,##0.00"));
			styleUnpay.setFont(font4);
			
			// 订单总额  && 交易服务费总额
			int totalCount = 2;
			Set<String> totalTrade = new HashSet<String>();
			List<String> totalTradeList = null;
			// QUAUQ服务费总额
			int quauqTotalCount = 2;
			Set<String> quauqTotalTrade = new HashSet<String>();
			List<String> quauqTotalTradeList = null;
			// 渠道服务费总额
			int agentTotalCount = 2;
			Set<String> agentTotalTrade = new HashSet<String>();
			List<String> agentTotalTradeList = null;
			// 抽成服务费总额
			int cutTotalCount = 2;
			Set<String> cutTotalTrade = new HashSet<String>();
			List<String> cutTotalTradeList = null;
			// 交易服务费  已结清
			int payCount = 2;
			Set<String> serverPayedSet = new HashSet<String>();
			List<String> serverPayedList = null;
			// 交易服务费  未结清
			int unpayCount = 2;
			Set<String> serverUnpayedSet = new HashSet<String>();
			List<String> serverUnpayedList = null;
			
			for(Object[] ite:list){
				String total = null;
				String quauqTotal = null;
				String agentTotal = null;
				String cutTotal = null;
				String payed = null;
				String unpayed = null;
				if(ite[4] != null) {
					total = ite[4].toString();
				}
				if(ite[5] != null) {
					quauqTotal = ite[5].toString();
				}
				if(ite[6] != null) {
					agentTotal = ite[6].toString();
				}
				if(ite[7] != null) {
					cutTotal = ite[7].toString();
				}
				if(ite[8] != null){
					payed = ite[8].toString();
				}
				if(ite[9] != null){
					unpayed = ite[9].toString();
				}
				if(total != null){
					total = total.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					total = total.replaceAll(",", "").replaceAll(" ", "");
					totalTrade.addAll(Arrays.asList(total.split("\\+")));
				}
				if(quauqTotal != null){
					quauqTotal = quauqTotal.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					quauqTotal = quauqTotal.replaceAll(",", "").replaceAll(" ", "");
					quauqTotalTrade.addAll(Arrays.asList(quauqTotal.split("\\+")));
				}
				if(agentTotal != null){
					agentTotal = agentTotal.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					agentTotal = agentTotal.replaceAll(",", "").replaceAll(" ", "");
					agentTotalTrade.addAll(Arrays.asList(agentTotal.split("\\+")));
				}
				if(cutTotal != null){
					cutTotal = cutTotal.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					cutTotal = cutTotal.replaceAll(",", "").replaceAll(" ", "");
					cutTotalTrade.addAll(Arrays.asList(cutTotal.split("\\+")));
				}
				if(payed != null){
					payed = payed.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					payed = payed.replaceAll(",", "").replaceAll(" ", "");
					serverPayedSet.addAll(Arrays.asList(payed.split("\\+")));
				}
				if(unpayed != null){
					unpayed = unpayed.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					unpayed = unpayed.replaceAll(",", "").replaceAll(" ", "");
					serverUnpayedSet.addAll(Arrays.asList(unpayed.split("\\+")));
				}
			}
			
			if(totalTrade.size() > 0){
				totalCount = totalTrade.size()*2;
				totalTradeList = new ArrayList<String>(totalTrade);
				Collections.sort(totalTradeList);
			}
			if(quauqTotalTrade.size() > 0){
				quauqTotalCount = quauqTotalTrade.size()*2;
				quauqTotalTradeList = new ArrayList<String>(quauqTotalTrade);
				Collections.sort(quauqTotalTradeList);
			}
			if(agentTotalTrade.size() > 0){
				agentTotalCount = agentTotalTrade.size()*2;
				agentTotalTradeList = new ArrayList<String>(agentTotalTrade);
				Collections.sort(agentTotalTradeList);
			}
			if(cutTotalTrade.size() > 0){
				cutTotalCount = cutTotalTrade.size()*2;
				cutTotalTradeList = new ArrayList<String>(cutTotalTrade);
				Collections.sort(cutTotalTradeList);
			}
			if(serverPayedSet.size() > 0){
				payCount = serverPayedSet.size()*2;
				serverPayedList = new ArrayList<String>(serverPayedSet);
				Collections.sort(serverPayedList);
			}
			if(serverUnpayedSet.size() > 0){
				unpayCount = serverUnpayedSet.size()*2;
				serverUnpayedList = new ArrayList<String>(serverUnpayedSet);
				Collections.sort(serverUnpayedList);
			}
			
			// 产生表格标题行,设置币种宽度
			int columnIndex = 0;
			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < totalTrade.size() + quauqTotalTrade.size() + agentTotalTrade.size() + cutTotalTrade.size() + serverPayedSet.size() 
					+ serverUnpayedSet.size(); ++i) {
				sheet.setColumnWidth(4 + i * 2, 5 * 256);
			}
			
			// 行标题
			for (int i = 0; i < cellTitle.length; i++) {
				HSSFCell cell = row.createCell(columnIndex);
				cell.setCellStyle(rowTwoCellStyle);
				HSSFRichTextString text = new HSSFRichTextString(cellTitle[i]);
				cell.setCellValue(text);
				if ("服务费总额".equals(cellTitle[i])) {
					sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
							columnIndex + totalCount - 1));
					columnIndex = columnIndex + totalCount;
				} else if ("QUAUQ服务费总额".equals(cellTitle[i])) {
					sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
							columnIndex + quauqTotalCount - 1));
					columnIndex = columnIndex + quauqTotalCount;
				} else if ("渠道服务费总额".equals(cellTitle[i])) {
					sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
							columnIndex + agentTotalCount - 1));
					columnIndex = columnIndex + agentTotalCount;
				} else if ("抽成服务费总额".equals(cellTitle[i])) {
					sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
							columnIndex + cutTotalCount - 1));
					columnIndex = columnIndex + cutTotalCount;
				} else if ("交易服务费已结清总额".equals(cellTitle[i])) {
					sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
							columnIndex + payCount - 1));
					columnIndex = columnIndex + payCount;
				} else if ("交易服务费未结清总额".equals(cellTitle[i])) {
					sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
							columnIndex + unpayCount - 1));
					columnIndex = columnIndex + unpayCount;
				} else {
					sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
							columnIndex));
					columnIndex = columnIndex + 1;
				}
			}
			
			rowNum = 1;
			for (int j = 0; j < list.size(); j++) {
				Object[] o = (Object[]) list.get(j);
				HSSFRow dataRow = sheet.createRow(rowNum++);
				HSSFCell dataCel = null;
				columnIndex = 0;  // 计数
				for (int n = 0; n < o.length; n++) {
					if (n == 4) {   // 总额
						columnIndex = exportValueforMoney(o[4],dataCel,dataRow,stylePay,columnIndex,totalTradeList);
					} else if (n == 5) {   // QUAUQ服务费总额
						columnIndex = exportValueforMoney(o[5],dataCel,dataRow,styleAccount,columnIndex,quauqTotalTradeList);
					} else if (n == 6) {   // 渠道服务费总额
						columnIndex = exportValueforMoney(o[6],dataCel,dataRow,styleAccount,columnIndex,agentTotalTradeList);
					} else if (n == 7) {   // 渠道服务费总额
						columnIndex = exportValueforMoney(o[7],dataCel,dataRow,styleAccount,columnIndex,cutTotalTradeList);
					} else if ( n == 8) {		// 已结清交易服务费
						columnIndex = exportValueforMoney(o[8],dataCel,dataRow,stylePay,columnIndex,serverPayedList);
					} else if (n == 9) {
						columnIndex = exportValueforMoney(o[9],dataCel,dataRow,styleUnpay,columnIndex,serverUnpayedList);
					} else {
						columnIndex = exportValue(dataRow,dataCel,columnIndex,rowTwoCellStyle,o[n]);
						columnIndex++;
					}
				}
				
			}
		}else{   // 针对交易明细
			// 订单总额
			int totalCount = 2;
			Set<String> totalSet = new HashSet<String>();
			List<String> totalList = null;
			// 服务费总额
			int allCount = 2;
			Set<String> allServerSet = new HashSet<String>();
			List<String> allServerList = null;
			// QUAUQ服务费
			int quauqCount = 2;
			Set<String> serverSet = new HashSet<String>();
			List<String> serverList = null;
			// 渠道服务费
			int agentCount = 2;
			Set<String> agentServerSet = new HashSet<String>();
			List<String> agentServerList = null;
			// 抽成服务费
			int cutCount = 2;
			Set<String> cutServerSet = new HashSet<String>();
			List<String> cutServerList = null;
			
			for(Object[] ite:list){
				String total = null;
				String all = null;
				String quauq = null;
				String agent = null;
				String cut = null;
				if (ite.length == 16) {
					if (ite[9] != null) {
						total = ite[9].toString();
					}
					if (ite[10] != null) {
						all = ite[10].toString();
					}
					if (ite[11] != null) {
						quauq = ite[11].toString();
					}
					if (ite[12] != null) {
						agent = ite[12].toString();
					}
					if (ite[13] != null) {
						cut = ite[13].toString();
					}
				} else if (ite.length == 15) {
					if (ite[8] != null) {
						total = ite[8].toString();
					}
					if (ite[9] != null) {
						all = ite[9].toString();
					}
					if (ite[10] != null) {
						quauq = ite[10].toString();
					}
					if (ite[11] != null) {
						agent = ite[11].toString();
					}
					if (ite[12] != null) {
						cut = ite[12].toString();
					}
				} else {
					if(ite[8] != null) {
						total = ite[8].toString();
					}
					if (ite[9] != null) {
						all = ite[9].toString();
					}
				}
				if(total != null){
					total = total.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					total = total.replaceAll(",", "").replaceAll(" ", "");
					totalSet.addAll(Arrays.asList(total.split("\\+")));
				}else{
					totalSet.add(" ");
				}
				if(all != null){
					all = all.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					all = all.replaceAll(",", "").replaceAll(" ", "");
					allServerSet.addAll(Arrays.asList(all.split("\\+")));
				}else{
					if (allServerSet.size() == 0) {
						allServerSet.add(" ");
					}
				}
				if(quauq != null){
					quauq = quauq.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					quauq = quauq.replaceAll(",", "").replaceAll(" ", "");
					serverSet.addAll(Arrays.asList(quauq.split("\\+")));
				}else{
					if (serverSet.size() == 0) {
						serverSet.add(" ");
					}
				}
				if(agent != null){
					agent = agent.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					agent = agent.replaceAll(",", "").replaceAll(" ", "");
					agentServerSet.addAll(Arrays.asList(agent.split("\\+")));
				}else{
					if (agentServerSet.size() == 0) {
						agentServerSet.add(" ");
					}
				}
				if(cut != null){
					cut = cut.replaceAll("(-?\\d+)(\\.\\d+)?", "");
					cut = cut.replaceAll(",", "").replaceAll(" ", "");
					cutServerSet.addAll(Arrays.asList(cut.split("\\+")));
				}else{
					if (cutServerSet.size() == 0) {
						cutServerSet.add(" ");
					}
				}
			}
			
			if(totalSet.size() > 0){
				totalCount = totalSet.size()*2;
				totalList = new ArrayList<String>(totalSet);
				Collections.sort(totalList);
			}
			if(allServerSet.size() > 0){
				allCount = allServerSet.size()*2;
				allServerList = new ArrayList<String>(allServerSet);
				Collections.sort(allServerList);
			}
			if(serverSet.size() > 0){
				quauqCount = serverSet.size()*2;
				serverList = new ArrayList<String>(serverSet);
				Collections.sort(serverList);
			}
			if(agentServerSet.size() > 0){
				agentCount = agentServerSet.size()*2;
				agentServerList = new ArrayList<String>(agentServerSet);
				Collections.sort(agentServerList);
			}
			if(cutServerSet.size() > 0){
				cutCount = cutServerSet.size()*2;
				cutServerList = new ArrayList<String>(cutServerSet);
				Collections.sort(cutServerList);
			}
			
			// 产生表格标题行,设置币种宽度
			int columnIndex = 0;
			HSSFRow row = sheet.createRow(0);
			if (UserUtils.getUser().getId() == 2060) {
				if (StringUtils.isBlank(officeId)) {
					for (int i = 0; i < totalSet.size() + allServerSet.size() + serverSet.size() + agentServerSet.size() + cutServerSet.size(); ++i) {
						sheet.setColumnWidth(9 + i * 2, 5 * 256);
					}
				} else {
					for (int i = 0; i < totalSet.size() + allServerSet.size() + serverSet.size() + agentServerSet.size() + cutServerSet.size(); ++i) {
						sheet.setColumnWidth(8 + i * 2, 5 * 256);
					}
				}
			}else{
				for (int i = 0; i < totalSet.size() + allServerSet.size(); ++i) {
					sheet.setColumnWidth(8 + i * 2, 5 * 256);
				}
			}
			//创建表头
			createTitle(cellTitle, sheet, row, rowTwoCellStyle, columnIndex, totalCount, allCount, quauqCount, agentCount, cutCount);
			rowNum = 1;
			for (int j = 0; j < list.size(); j++) {
				Object[] o = (Object[]) list.get(j);
				HSSFRow dataRow = sheet.createRow(rowNum++);
				HSSFCell dataCel = null;
				columnIndex = 0;  // 计数
				//创建单元格并为其赋值
				if (UserUtils.getUser().getId() == 2060) {
					if (StringUtils.isBlank(officeId)) {
						for (int n = 0; n < o.length; n++) {
							if (n == 9) {   // 订单总额
								columnIndex = exportValueforMoney(o[9] == null ? " " :o[9],dataCel,dataRow,styleAccount,columnIndex,totalList);
							} else if ( n == 10) {		// 服务费总额
								columnIndex = exportValueforMoney(o[10] == null ? " " :o[10],dataCel,dataRow,stylePay,columnIndex,allServerList);
							} else if ( n == 11) {		// QUAUQ服务费
								columnIndex = exportValueforMoney(o[11] == null ? " " :o[11],dataCel,dataRow,stylePay,columnIndex,serverList);
							} else if ( n == 12) {		// 渠道服务费
								columnIndex = exportValueforMoney(o[12] == null ? " " :o[12],dataCel,dataRow,styleAccount,columnIndex,agentServerList);
							} else if ( n == 13) {		// 抽成服务费
								columnIndex = exportValueforMoney(o[13] == null ? " " :o[13],dataCel,dataRow,styleAccount,columnIndex,cutServerList);
							} else {
								columnIndex = exportValue(dataRow,dataCel,columnIndex,rowTwoCellStyle,o[n]);
								columnIndex++;
							}
						}
					} else {
						for (int n = 0; n < o.length; n++) {
							if (n == 8) {   // 订单总额
								columnIndex = exportValueforMoney(o[8] == null ? " " :o[8],dataCel,dataRow,styleAccount,columnIndex,totalList);
							} else if ( n == 9) {		// 服务费总额
								columnIndex = exportValueforMoney(o[9] == null ? " " :o[9],dataCel,dataRow,stylePay,columnIndex,allServerList);
							} else if ( n == 10) {		// QUAUQ服务费
								columnIndex = exportValueforMoney(o[10] == null ? " " :o[10],dataCel,dataRow,stylePay,columnIndex,serverList);
							} else if ( n == 11) {		// 渠道服务费
								columnIndex = exportValueforMoney(o[11] == null ? " " :o[11],dataCel,dataRow,styleAccount,columnIndex,agentServerList);
							} else if ( n == 12) {		// 抽成服务费
								columnIndex = exportValueforMoney(o[12] == null ? " " :o[12],dataCel,dataRow,styleAccount,columnIndex,cutServerList);
							} else {
								columnIndex = exportValue(dataRow,dataCel,columnIndex,rowTwoCellStyle,o[n]);
								columnIndex++;
							}
						}
					}
				} else {
					for (int n = 0; n < o.length; n++) {
						if (n == 8) {   // 订单总额
							columnIndex = exportValueforMoney(o[8] == null ? " " :o[8],dataCel,dataRow,styleAccount,columnIndex,totalList);
						} else if ( n == 9) {		// 服务费总额
							columnIndex = exportValueforMoney(o[9] == null ? " " :o[9],dataCel,dataRow,stylePay,columnIndex,allServerList);
						}  else {
							columnIndex = exportValue(dataRow,dataCel,columnIndex,rowTwoCellStyle,o[n]);
							columnIndex++;
						}
					}
				}
			}
		}
		//导出excel文档
		ExportExcelForAgentUtils.exportExcel(workBook,fileName, request, response);
	}
	/**
	 * 为交易明细表创建表头
	 * @param cellTitle1
	 * @param sheet
	 * @param row
	 * @param cellStyle
	 * @param celNum
	 * @param totalCount1
	 * @param quauqCount1
	 * @param agentCount1
	 * @return
	 */
	private static int createTitle(String[] cellTitle1, HSSFSheet sheet, HSSFRow row, HSSFCellStyle cellStyle, int celNum, int totalCount1, 
			int allCount1, int quauqCount1, int agentCount1, int cutCount1){
		for (int i = 0; i < cellTitle1.length; i++) {
			HSSFCell cell = row.createCell(celNum);
			cell.setCellStyle(cellStyle);
			HSSFRichTextString text = new HSSFRichTextString(cellTitle1[i]);
			cell.setCellValue(text);
			if ("订单总额".equals(cellTitle1[i])) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, celNum,
						celNum + totalCount1 - 1));
				celNum = celNum + totalCount1;
			} else if ("服务费总额".equals(cellTitle1[i])) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, celNum,
						celNum + allCount1 - 1));
				celNum = celNum + allCount1;
			} else if ("QUAUQ服务费".equals(cellTitle1[i])) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, celNum,
						celNum + quauqCount1 - 1));
				celNum = celNum + quauqCount1;
			} else if ("渠道服务费".equals(cellTitle1[i])) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, celNum,
						celNum + agentCount1 - 1));
				celNum = celNum + agentCount1;
			} else if ("抽成服务费".equals(cellTitle1[i])) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, celNum,
						celNum + cutCount1 - 1));
				celNum = celNum + cutCount1;
			} else {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, celNum,
						celNum));
				celNum = celNum + 1;
			}
		}
		return celNum;
	}
	/**
	 * 创建订单总额、QUAUQ服务费、渠道服务费单元格，并为其赋值
	 * @param o
	 * @param dataCel
	 * @param dataRow
	 * @param cellStyle
	 * @param cellNum
	 * @param valueList
	 * @return
	 */
	private static int exportValueforMoney(Object o,HSSFCell dataCel,HSSFRow dataRow,HSSFCellStyle cellStyle,int cellNum,List<String> valueList){
		String server = (o == null ? "" : o.toString());
		// 将数据分成数组
		List<Object[]> servers = MoneyNumberFormat.getMoneyFromString(server, "\\+");				
		for (String str : valueList) {
			boolean hasValue = false;
			Double initValue = null;
			if(CollectionUtils.isNotEmpty(servers)) {
				for (Object[] objects : servers) {
					if (str.equals(objects[0].toString())) {
						hasValue = true;
						initValue = Double.valueOf(objects[1].toString());
					}
				}
			}
			dataCel = dataRow.createCell(cellNum);
			dataCel.setCellValue(str);
			dataCel.setCellStyle(cellStyle);
			cellNum ++;
			if (hasValue) {
				dataCel = dataRow.createCell(cellNum);
				dataCel.setCellValue(initValue);
				dataCel.setCellStyle(cellStyle);
				cellNum ++;
			} else {
				dataCel = dataRow.createCell(cellNum);
				dataCel.setCellValue("");
				dataCel.setCellStyle(cellStyle);
				cellNum ++;
			}
		}
		return cellNum;
	}
	/**
	 * 创建单元格，并赋值
	 * 如果值为空，则显示“-”
	 * @param dataRow
	 * @param dataCel
	 * @param columnIndex
	 * @param rowTwoCellStyle
	 * @param o 
	 */
	private static int exportValue(HSSFRow dataRow,HSSFCell dataCel ,int column,HSSFCellStyle rowTwoCellStyle,Object o){
		if (o != null) {
			dataCel = dataRow.createCell(column);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.toString());
			
		}else{
			dataCel = dataRow.createCell(column);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o == null ? "-" :o.toString());
		}
		return column++;
	}
	
	/**
	 * 进入结算价操作记录页面
	 * @param orderType 产品线类型
	 * @param orderId 订单id
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
//	@RequestMapping(value = "gotoPayPriceLog/{orderType}/{orderId}")
//	public String gotoPayPriceLog(@PathVariable String orderType, @PathVariable String orderId, Model model, HttpServletRequest request, HttpServletResponse response){
//		
//		Map<String, Object> xx = suauqServiceFeeService.getPayPriceLogData();
//		
//		
//		return "agentToOffice/T2/payPriceLog";
//	}
	
	/**
     * 获取代结算价操作记录列表
     * @param request
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "gotoPayPriceLog")
	public Map<String, Object> gotoPayPriceLog(HttpServletRequest request) {
    	Map<String, Object> resultMap = new HashMap<>();
		String orderType = request.getParameter("orderType");  // 产品线类型
		String orderId = request.getParameter("orderId");  // 订单id
		// 获取代替下单用户列表
		JSONArray logJson = suauqServiceFeeService.getPayPriceLogData(orderType, orderId);
		resultMap.put("flag", "success");
		resultMap.put("data", logJson);
		return resultMap;
	}

	/**
	 * 获取批发商默认的费率值。
	 * @author yudong.xu 2016.8.10
     */
	@RequestMapping(value = "ratelist")
	public String getOfficeRate(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String,String> map = new HashMap<>();
		String companyName = request.getParameter("companyName");
		map.put("companyName",companyName);
		model.addAttribute("companyName",companyName);
		String chouchengRateMin = request.getParameter("chouchengRateMin");
		map.put("chouchengRateMin", chouchengRateMin);
		model.addAttribute("chouchengRateMin",chouchengRateMin);
		String chouchengRateMax = request.getParameter("chouchengRateMax");
		map.put("chouchengRateMax", chouchengRateMax);
		model.addAttribute("chouchengRateMax",chouchengRateMax);
		String quauqRateMin = request.getParameter("quauqRateMin");
		map.put("quauqRateMin", quauqRateMin);
		model.addAttribute("quauqRateMin",quauqRateMin);
		String quauqRateMax = request.getParameter("quauqRateMax");
		map.put("quauqRateMax", quauqRateMax);
		model.addAttribute("quauqRateMax",quauqRateMax);
		Page<Map<Object,Object>> page = new Page<>(request,response);
		suauqServiceFeeService.getOfficeRate(page,map);
		model.addAttribute("page",page);
		return "agentToOffice/T2/officeRateList";
	}

	/**
	 * 保存用户设置的批发商的默认费率值。
	 * @param request
	 * @author yudong.xu 2016.8.16
     */
	@RequestMapping(value = "saveDefaultRate")
	@ResponseBody
	public String saveDefaultRate(HttpServletRequest request){
		String itemStr = request.getParameter("items");
		String uuidStr = request.getParameter("uuids");
		String flag = "1";
		try {
			suauqServiceFeeService.saveDefaultRate(itemStr,uuidStr);
		}catch (Exception e){
			flag = "0";
		}
		return flag;
	}
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String,String> map = new HashMap<>();
		String companyName = request.getParameter("companyName");
		map.put("companyName",companyName);
		model.addAttribute("companyName",companyName);
		String chouchengRateMin = request.getParameter("chouchengRateMin");
		map.put("chouchengRateMin", chouchengRateMin);
		model.addAttribute("chouchengRateMin",chouchengRateMin);
		String chouchengRateMax = request.getParameter("chouchengRateMax");
		map.put("chouchengRateMax", chouchengRateMax);
		model.addAttribute("chouchengRateMax",chouchengRateMax);
		String quauqRateMin = request.getParameter("quauqRateMin");
		map.put("quauqRateMin", quauqRateMin);
		model.addAttribute("quauqRateMin",quauqRateMin);
		String quauqRateMax = request.getParameter("quauqRateMax");
		map.put("quauqRateMax", quauqRateMax);
		model.addAttribute("quauqRateMax",quauqRateMax);
		List<Map<Object,Object>> list = suauqServiceFeeService.getOfficeRateList(map);
		HSSFWorkbook workbook = suauqServiceFeeService.createRateExcel(list);
		String fileName="批发商费率导出EXCEL.xls";
		response.setContentType("octets/stream");
		OutputStream os=null;
		try {
			response.setHeader("Content-Disposition", "attachment; filename="+ new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
			os=response.getOutputStream();
			workbook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
