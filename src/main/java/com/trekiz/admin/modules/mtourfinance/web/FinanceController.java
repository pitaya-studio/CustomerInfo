package com.trekiz.admin.modules.mtourfinance.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.common.input.BaseOut4MT;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.ZipUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.ActivityAirTicketServiceImpl;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.mtourCommon.service.SerialNumberService;
import com.trekiz.admin.modules.mtourCommon.web.MtourBaseController;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderMoneyAmount;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnr;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirlinePrice;
import com.trekiz.admin.modules.mtourOrder.jsonbean.MtourOrderJsonBean;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderMoneyAmountService;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderPnrAirlinePriceService;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderPnrService;
import com.trekiz.admin.modules.mtourfinance.json.BigCode;
import com.trekiz.admin.modules.mtourfinance.json.CostRecordJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.FrontMoneyStatData;
import com.trekiz.admin.modules.mtourfinance.json.FrontMoneyStatJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.OperatingRevenueData;
import com.trekiz.admin.modules.mtourfinance.json.OperatingRevenueJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.OrderDetailJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.OrderpayJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.Page;
import com.trekiz.admin.modules.mtourfinance.json.PayDetailJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.PaySheetJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.ReceiptRecordJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.RefundJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.RefundRecordsJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.RefundTypeDetailJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.SecondReceiveListJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.SecondRefundRecordsJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.SettlementJsonBean;
import com.trekiz.admin.modules.mtourfinance.pojo.AccountAgeParam;
import com.trekiz.admin.modules.mtourfinance.pojo.ConfirmSheet;
import com.trekiz.admin.modules.mtourfinance.pojo.PayedDetail;
import com.trekiz.admin.modules.mtourfinance.pojo.PayedMoneyPojo;
import com.trekiz.admin.modules.mtourfinance.pojo.ReceiveOrderListParam;
import com.trekiz.admin.modules.mtourfinance.pojo.RefundRecord;
import com.trekiz.admin.modules.mtourfinance.service.FinanceService;
import com.trekiz.admin.modules.mtourfinance.util.CommonUtils;
import com.trekiz.admin.modules.mtourfinance.util.ComparatorDate;
import com.trekiz.admin.modules.mtourfinance.util.ExcelUtils;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.pay.dao.PayGroupDao;
import com.trekiz.admin.modules.pay.entity.PayGroup;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 
* @ClassName: FinanceController
* @Description: TODO(美途国际财务控制器)
* @author kai.xiao
* @date 2015年10月15日 下午8:50:40
*
 */
@Controller
//wangxinwei modified   ${adminPath}/mtourfinance -> ${adminPath}/mtour/mtourfinance
@RequestMapping(value = "${adminPath}/mtour/mtourfinance")
public class FinanceController extends MtourBaseController {
	
	public static final String CBZF = "成本支付";
	public static final String YFK = "预付款";
	public static final String TK ="退款";
	public static final String ZJCB = "追加成本";
	
	@Autowired
	private FinanceService financeService;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private PayGroupDao payGroupDao;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Autowired
	private AirticketOrderMoneyAmountService  airticketOrderMoneyAmountService;
	
	@Autowired
	private CostManageService costManageService;
	
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private ActivityAirTicketServiceImpl activityAirTicketServiceImpl;
	
	@Autowired
	private AirticketOrderPnrAirlinePriceService airticketOrderPnrAirlinePriceService;
	
	@Autowired
	private AirticketOrderPnrService airticketOrderPnrService;
	
	@Autowired
	private RefundService refundService;
	
	@Autowired
	private SystemService systemService;

	@Autowired
	private SerialNumberService serialNumberService;
	/**
	 * 跳转财务首页 add by zhanghao
	 * @return
	 */
	@RequestMapping(value = "financeAging")
	public String financeAging() {
        return "modules/mtour/financeAging";
	}
	@RequestMapping(value = "financePay")
	public String financePay() {
        return "modules/mtour/financePay";
	}
	@RequestMapping(value = "financeReceive")
	public String financeReceive() {
        return "modules/mtour/financeReceive";
	}
	
	/**
	 * 收入单打印页面
		 * @Title: financeReceive
	     * @return String
	     * @author majiancheng       
	     * @date 2015-11-2 下午9:42:55
	 */
	@RequestMapping(value = "printIncomeInfoPage")
	public String printIncomeInfoPage() {
        return "modules/mtour/printIncomeInfoPage";
	}
	
	/**
	 * 确认单打印页面
		 * @Title: printConfirmPage
	     * @return String
	     * @author majiancheng       
	     * @date 2015-11-2 下午9:43:23
	 */
	@RequestMapping(value = "printConfirmPage")
	public String printConfirmPage() {
        return "modules/mtour/printConfirmPage";
	}
	
	/**
	 * 支出单打印页面
		 * @Title: printOutPayPage
	     * @return String
	     * @author majiancheng       
	     * @date 2015-11-2 下午9:43:34
	 */
	@RequestMapping(value = "printOutPayPage")
	public String printOutPayPage() {
        return "modules/mtour/printOutPayPage";
	}
	
	
	/**
	 * 支出单打印页面
		 * @Title: printOutPayPage
	     * @return String
	     * @author songyang       
	     * @date   2016年2月2日13:52:20
	 */
	@RequestMapping(value = "mergePrintOutPayPage")
	public String mergePrintOutPayPage() {
        return "modules/mtour/mergePrintOutPayPage";
	}
	
	
	/**
	 * 结算单打印页面
		 * @Title: printSettlementPage
	     * @return String
	     * @author majiancheng       
	     * @date 2015-11-2 下午9:43:43
	 */
	@RequestMapping(value = "printSettlementPage")
	public String printSettlementPage() {
        return "modules/mtour/printSettlementPage";
	}
	
	
	/**
	 * 付款-记录列表-详情接口
	 * @Title: getRefundInfo
	 * @return String
	 * @author majiancheng
	 * @date 2015-10-21 下午12:03:29
	 */
	@ResponseBody
	@RequestMapping(value = "getRefundInfo", method = RequestMethod.POST)
	public String getRefundInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			RefundJsonBean data = financeService.getRefundInfo(input);
			if(data != null) {
				// 将当前设置为达帐时间，传到前台
				out.setData(data);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("传参出现异常，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用确认收款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	
	/**
	 * 付款-记录列表-撤销接口
	 * @Title: undoRefundPayInfo
	 * @return String
	 * @author majiancheng
	 * @date 2015-10-21 下午5:32:02
	 */
	@ResponseBody
	@RequestMapping(value = "undoRefundPayInfo", method = RequestMethod.POST)
	public String undoRefundPayInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			boolean flag = financeService.undoRefundPayInfo(input);
			if(flag) {
				// 将当前设置为达帐时间，传到前台
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Fail();
				out.putMsgDescription("付款记录撤销失败，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用付款记录撤销接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 确认收款(详情)接口
	     * <p>@Description TODO</p>
		 * @Title: getReceivedOrderInfo
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-16 下午3:35:25
	 */
	@ResponseBody
	@RequestMapping(value = "getReceivedOrderInfo", method = RequestMethod.POST)
	public String getReceivedOrderInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			OrderpayJsonBean data = financeService.getPayInfo(input);
			if(data != null) {
				//设置银行到款日期为当前日期
				if(data.getArrivalBankDate() == null) {
					data.setArrivalBankDate(new Date());
				}
				out.setData(data);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("传参出现异常，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用确认收款(详情)接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 获取美途国际收款列表
	 * @return String
	 * @author zhaohaiming
	 * @date 2015-10-16
	 * */
	@ResponseBody
	@RequestMapping(value="showOrderList")
	public String showOrderList(BaseInput4MT input ){
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			Page page = financeService.showOrderList(input, new Page());
			if(page != null) {
				
				out.setData(page);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("传参出现异常，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用确认收款(详情)接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}

	/**
	 * 确认单下载
	 * @param request	
	 * @param response	
	 * @author zhaohaiming
	 * @date 2015-10-19
	 * 
	 * */
	@RequestMapping(value="downloadConfirmSheet")
	public void downloadConfirmSheet(HttpServletRequest request,HttpServletResponse response,BaseInput4MT input){
		
		try{
			ConfirmSheet cs = financeService.getConfirmSheetData(input,request);
			financeService.genExcl(request, response,cs);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取确认单数据
	 * @param input
	 * @return String
	 * @author zhaohaiming
	 * */
	@ResponseBody
	@RequestMapping(value="getConfirmSheetData",method=RequestMethod.POST)
	public String getConfirmSheetData(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			ConfirmSheet cs = financeService.getConfirmSheetData(input, null);
			if(cs != null) {
				out.setData(cs);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("传参出现异常，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用获取收入单接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	/**
	 * 获取收入单信息
	     * <p>@Description TODO</p>
		 * @Title: showIncomeInfo
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-19 下午5:28:15
	 */
	@ResponseBody
	@RequestMapping(value = "showIncomeInfo", method = RequestMethod.POST)
	public String showIncomeInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		
		String fundsType = input.getParamValue("fundsType");
		String receiveUuid = input.getParamValue("receiveUuid");
		
		try{
			Map<String, Object> data = financeService.getIncomeInfoByInput(fundsType, receiveUuid);
			if(data != null) {
				out.setData(data);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("传参出现异常，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用获取收入单接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 下载收入单
	     * <p>@Description TODO</p>
		 * @Title: downloadIncomeSheet
	     * @return ResponseEntity<byte[]>
	     * @author majiancheng       
	     * @date 2015-10-20 下午3:04:17
	 */
	@RequestMapping(value="downloadIncomeSheet")
	public ResponseEntity<byte[]> downloadIncomeSheet(BaseInput4MT input, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String receiveUuid = request.getParameter("receiveUuid");
		String fundstype = request.getParameter("fundstype");
		OutputStream os = null;
    	try {
    		Map<String, Object> data = financeService.getIncomeInfoByInput(fundstype, receiveUuid);
    		File file = financeService.createIncomeSheetDownloadFile(data);
    		
    		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
    		String fileName =  "收入单" + nowDate + ".doc";
    		
			if(file != null && file.exists()){
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
				response.addHeader("Content-Length", "" + file.length()); 
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
	 * 收款记录列表方法
	 * @param input 查询条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getReceiptRecordList")
	public String getReceiptRecordList(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		ReceiptRecordJsonBean data = new ReceiptRecordJsonBean();
		List<Map<String, Object>> receiptRecordList = financeService.getReceiptRecordList(input);
		if(CollectionUtils.isNotEmpty(receiptRecordList)){
			for(Map<String, Object> temp : receiptRecordList){
				if(temp.get("totalMoney")!=null){
					List<Map<String, Object>> totalSalePrice = financeService.getMoneyInfo(temp.remove("totalMoney").toString());
					temp.put("totalSalePrice", totalSalePrice);
				}
				if(temp.get("accountedMoney")!=null){
					List<Map<String, Object>> totalArrivedAmount = financeService.getMoneyInfo(temp.remove("accountedMoney").toString());
					temp.put("totalArrivedAmount", totalArrivedAmount);
				}
				if(temp.get("payedMoney")!=null){
					List<Map<String, Object>> receivedAmount = financeService.getMoneyInfo(temp.remove("payedMoney").toString());
					temp.put("receivedAmount", receivedAmount);
				}
				if(temp.get("moneyAccountde")!=null){
					List<Map<String, Object>> arrivedAmount = financeService.getMoneyInfo(temp.remove("moneyAccountde").toString());
					temp.put("arrivedAmount", arrivedAmount);
				}
			}
			Map<String,String> totalRowCount = new HashMap<String,String>();
			totalRowCount.put("totalRowCount", receiptRecordList.get(0).remove("totalRowCount").toString());
			data.setPage(totalRowCount);
			data.setResults(receiptRecordList);
			out.setData(data);
		}
		out.setResponseCode4Success();
		return toAndCacheJSONString(out);
	}
	
	@ResponseBody
	@RequestMapping(value = "getReceiptRecordDetail")
	public String getReceiptRecordDetail(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		//获得传递的参数值
		String receiveId = input.getParamValue("receiveUuid");
		if(StringUtils.isNotBlank(receiveId)){
			List<Map<String,Object>> list = financeService.getReceiptRecordDetail(receiveId);
			if(CollectionUtils.isNotEmpty(list)){
				if(list.get(0).get("docId")!=null){
					String temp = list.get(0).get("docId").toString();
					String docIds =temp.substring(0, temp.lastIndexOf(","));
					List<Map<String,String>> docDetail = financeService.getReceiptDocDetail(docIds);
					list.get(0).put("attachments", docDetail);
				};
				out.setData(list.get(0));
			}
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 获取付款历史记录
	 * @param input
	 * @return String
	 * @author zhaohaiming
	 * */
	@ResponseBody
	@RequestMapping(value="getPayRecord")
	public String getPayRecord(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			List<RefundRecord>  list= financeService.getRefundRecord(input);
			out.setData(list);
			out.setResponseCode4Success();
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用付款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 获取成本录入列表
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getCostRecords")
	public String getCostRecords(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String orderId = input.getParamValue("orderUuid");	//订单id
		if(StringUtils.isNotEmpty(orderId)) {
			List<CostRecordJsonBean> list = financeService.getCostRecordList(Integer.parseInt(orderId));
			out.setResponseCode4Success();
			out.setData(list);
		}else{
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用成本录入列表接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 获取其他收入录入列表
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getOtherCostRecords")
	public String getOtherCostRecords(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String orderId = input.getParamValue("orderUuid");	//订单id
		if(StringUtils.isNotEmpty(orderId)) {
			List<CostRecordJsonBean> list = financeService.getOtherCostRecordList(Integer.parseInt(orderId));
			out.setResponseCode4Success();
			out.setData(list);
		}else{
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用成本录入列表接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}

	/**
	 * 提交成本录入/其他收入录入
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="submitCostRecord")
	public String submitCostRecord(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		
		Integer costId = null;
		if(jsonObj.containsKey("costUuid")) {//成本id
			costId = jsonObj.getIntValue("costUuid");
		}else if(jsonObj.containsKey("otherRevenueUuid")) {//其他收入id
			costId = jsonObj.getIntValue("otherRevenueUuid");
		}
		if(costId != null) {
			financeService.submitCostRecord(costId);
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用提交成本录入接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 成本记录-批量提交
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="batchSubmitCostRecord")
	public String batchSubmitCostRecord(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONArray jsonArr = JSONObject.parseArray(input.getParam());
		try {
			for(int i = 0; i < jsonArr.size(); i++) {
				JSONObject obj= jsonArr.getJSONObject(i);
				String costId = obj.get("costUuid").toString();
				if(StringUtils.isNotBlank(costId)) {
					financeService.submitCostRecord(Integer.parseInt(costId));
				}
			}
			out.setResponseCode4Success();
		} catch (Exception e) {
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用批量提交成本录入接口出现异常，请重新操作！");
		}
		
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 撤回成本录入/其他收入录入
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="cancelCostRecord")
	public String cancelCostRecord(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		
		Integer costId = null;
		if(jsonObj.containsKey("costUuid")) {//成本id
			costId = jsonObj.getIntValue("costUuid");
		}else if(jsonObj.containsKey("otherRevenueUuid")) {//其他收入id
			costId = jsonObj.getIntValue("otherRevenueUuid");
		}
		
		if(costId != null) {
			financeService.cancelCostRecord(costId);
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用提交成本录入接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 根据订单id获取航段名称
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getAirlineNames")
	public String getAirlineNames(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		
		Integer orderId = jsonObj.getIntValue("orderUuid");//订单id
		String invoiceOriginalUuid = jsonObj.getString("invoiceOriginalUuid");//大编号Uuid
//		if(jsonObj.containsKey("orderUuid")) {//订单id
//			orderId = jsonObj.getIntValue("orderUuid");
//		}
		
		if(orderId != null) {
			List<Map<String,Object>> airlineNames = financeService.getAirlineNames(orderId, invoiceOriginalUuid);
			out.setData(airlineNames);
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用获取航段名称接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 成本录入-添加/修改
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveOrUpdateCostRecord4Old")
	public String saveOrUpdateCostRecord4Old(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try {
			JSONObject jsonObj = JSONObject.parseObject(input.getParam());
			Integer id = null;
			if(jsonObj.containsKey("costUuid")) {
				id = jsonObj.getIntValue("costUuid");
			}
			CostRecord costRecord = null;
			User user = UserUtils.getUser();
			Date date = new Date();
			//通过id判断是添加还是修改
			if(id != null && StringUtils.isNotEmpty(id.toString())) {
				costRecord = costRecordDao.findOne(Long.parseLong(id.toString()));
				costRecord.setUpdateBy(user.getId());
				costRecord.setUpdateDate(date);
//				costRecord.setUuid(UuidUtils.generUuid());
			}else{
				costRecord = new CostRecord();
				costRecord.setCreateBy(user);
				costRecord.setCreateDate(date);
				costRecord.setUpdateBy(user.getId());
				costRecord.setUpdateDate(date);
				costRecord.setUuid(UuidUtils.generUuid());
			}
			String saveType = input.getParamValue("saveType");
			if("0".equals(saveType)) {//保存
				costRecord.setPayStatus(2);
			} else if ("1".equals(saveType)) {//保存并提交
				costRecord.setPayStatus(3);
			}
			String orderId = input.getParamValue("orderUuid");
			if(StringUtils.isNotEmpty(orderId)) {
				costRecord.setOrderId(Long.parseLong(orderId));
			}
			//获取机票产品id
			String sql = "SELECT airticket_id FROM airticket_order where id = ?";
			List<Map<String, Integer>> airticketIdList = airticketOrderDao.findBySql(sql, Map.class, orderId);
			if(airticketIdList != null && airticketIdList.size() > 0) {
				Integer airticketId = airticketIdList.get(0).get("airticket_id");
				if(airticketId != null) {
					costRecord.setActivityId(Long.parseLong(airticketId.toString()));
				}
			}
			costRecord.setOrderType(7);//产品类型
			costRecord.setReviewType(0);
			
			costRecord.setName(input.getParamValue("fundsName"));//款项名称
//			costRecord.setBigCode(input.getParamValue("invoiceOriginalTypeCode"));
//			costRecord.setPNR(input.getParamValue("PNR"));
//			costRecord.setTourOperatorUuid(input.getParamValue("tourOperatorUuid"));
//			costRecord.setTourOperatorName(input.getParamValue("tourOperatorName"));
			
			String quantity = input.getParamValue("peopleCount");//人数
			if(StringUtils.isNotEmpty(quantity)) {
				costRecord.setQuantity(Integer.parseInt(quantity));
			}
			String price = input.getParamValue("price");//单价
			if(StringUtils.isNotEmpty(price)) {
				costRecord.setPrice(new BigDecimal(price));
			}
			String supplyType = input.getParamValue("tourOperatorChannelCategoryCode");//客户类别：地接社、渠道商
			if(StringUtils.isNotEmpty(supplyType)) {
				costRecord.setSupplyType(Integer.parseInt(supplyType));
			}
			String supplierType = input.getParamValue("tourOperatorOrChannelTypeCode");//地接社类型
			if(StringUtils.isNotEmpty(supplierType)) {
				costRecord.setSupplierType(Integer.parseInt(supplierType));
			}
			String supplyName = input.getParamValue("tourOperatorOrChannelName");//地接社、渠道商id
			costRecord.setSupplyName(supplyName);
			Integer supplyId = jsonObj.getIntValue("tourOperatorOrChannelUuid");//地接社、渠道商名称
			if(supplyId != null) {
				costRecord.setSupplyId(supplyId);
			}
			
			Double rate = jsonObj.getDoubleValue("exchangeRate");//汇率
			if(rate != null) {
				costRecord.setRate(new BigDecimal(rate));
			}
			Integer currencyId = jsonObj.getIntValue("currencyUuid");//转换前币种
			if(currencyId != null) {
				costRecord.setCurrencyId(currencyId);
			}
			Integer currencyAfter = jsonObj.getIntValue("convertedCurrencyUuid");//转换后币种
			if(currencyAfter != null) {
				costRecord.setCurrencyAfter(currencyAfter);
			}
			
			Double priceAfter = jsonObj.getDoubleValue("convertedAmount");//总价
			if(priceAfter != null) {
				costRecord.setPriceAfter(new BigDecimal(priceAfter));
			}
			Integer invoiceOriginalTypeCode = jsonObj.getIntValue("invoiceOriginalTypeCode");//区分大编号还是地接社
			if(invoiceOriginalTypeCode != null) {
				costRecord.setIsJoin(invoiceOriginalTypeCode);
			}
			if(input.getParamValue("PNR") != null && "".equals(input.getParamValue("PNR"))) {
				costRecord.setBigCode(input.getParamValue("tourOperatorOrChannelUuid"));//大编号为空时存地接社id
			}else{
				costRecord.setBigCode(input.getParamValue("PNR"));//大编号
			}
			String pnrUuid = input.getParamValue("invoiceOriginalUuid");
			costRecord.setPnrUuid(pnrUuid);
//			AirticketOrderPnr airticketOrderPnr = airticketOrderPnrService.getByUuid(pnrUuid);
//			costRecord.setAirline(airticketOrderPnr.getAirline());
			costRecord.setBankName(input.getParamValue("paymentBank"));//银行名称
			costRecord.setBankAccount(input.getParamValue("paymentAccount"));//银行账号
			costRecord.setComment(input.getParamValue("memo"));//备注
			costRecord.setBudgetType(1);
			costRecord.setOverseas(0);
//			costRecord.setAirlineUuid(input.getParamValue("airlineUuid"));//航段表uuid
//			costRecord.setAirlineName(input.getParamValue("airlineName"));//航段名称
//			Double costTotalDeposit = jsonObj.getDoubleValue("costTotalDeposit");//定金总额
//			if(costTotalDeposit != null) {
//				costRecord.setCostTotalDeposit(new BigDecimal(costTotalDeposit));
//			}
			
			//同步更新订单成本
//			updateOrderCost(costRecord);
			
			financeService.saveOrUpdateCostRecord(costRecord);
			out.setResponseCode4Success();
		} catch(Exception e) {
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用成本录入保存更新接口出现异常，请重新操作！");
		}
		
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 成本录入-添加/修改
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveOrUpdateCostRecord")
	public String saveOrUpdateCostRecord(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try {
			JSONObject jsonObj = JSONObject.parseObject(input.getParam());
			Integer id = null;
			if(jsonObj.containsKey("costUuid")) {
				id = jsonObj.getIntValue("costUuid");
			}
			CostRecord costRecord = null;
			User user = UserUtils.getUser();
			Date date = new Date();
			//通过id判断是添加还是修改
			if(id != null && StringUtils.isNotEmpty(id.toString())) {
				costRecord = costRecordDao.findOne(Long.parseLong(id.toString()));
				costRecord.setUpdateBy(user.getId());
				costRecord.setUpdateDate(date);
//				costRecord.setUuid(UuidUtils.generUuid());
			}else{
				costRecord = new CostRecord();
				costRecord.setCreateBy(user);
				costRecord.setCreateDate(date);
				costRecord.setUpdateBy(user.getId());
				costRecord.setUpdateDate(date);
				costRecord.setUuid(UuidUtils.generUuid());
			}
			String saveType = input.getParamValue("saveType");
			if("0".equals(saveType)) {//保存
				costRecord.setPayStatus(Context.MTOUR_PAYSTATUS_2);
			} else if ("1".equals(saveType)) {//保存并提交
				costRecord.setPayStatus(Context.MTOUR_PAYSTATUS_3);
			}
			String orderId = input.getParamValue("orderUuid");
			if(StringUtils.isNotEmpty(orderId)) {
				costRecord.setOrderId(Long.parseLong(orderId));
			}
			//获取机票产品id
			String sql = "SELECT airticket_id FROM airticket_order where id = ?";
			List<Map<String, Integer>> airticketIdList = airticketOrderDao.findBySql(sql, Map.class, orderId);
			if(airticketIdList != null && airticketIdList.size() > 0) {
				Integer airticketId = airticketIdList.get(0).get("airticket_id");
				if(airticketId != null) {
					costRecord.setActivityId(Long.parseLong(airticketId.toString()));
				}
			}
			costRecord.setOrderType(Context.ORDER_TYPE_JP);//产品类型
			costRecord.setReviewType(0);
			//if(UserUtils.isMtourUser() && id != null && StringUtils.isNotEmpty(id.toString())){
				costRecord.setName(input.getParamValue("fundsName"));//款项名称
//			}else{
//				costRecord.setName(input.getParamValue("tourOperatorOrChannelName"));//款项名称
//			}
//			costRecord.setBigCode(input.getParamValue("invoiceOriginalTypeCode"));
//			costRecord.setPNR(input.getParamValue("PNR"));
//			costRecord.setTourOperatorUuid(input.getParamValue("tourOperatorUuid"));
//			costRecord.setTourOperatorName(input.getParamValue("tourOperatorName"));
			
			String quantity = input.getParamValue("peopleCount");//人数
			if(StringUtils.isNotEmpty(quantity)) {
				costRecord.setQuantity(Integer.parseInt(quantity));
			}
			String price = input.getParamValue("price");//单价
			if(StringUtils.isNotEmpty(price)) {
				costRecord.setPrice(new BigDecimal(price));
			}
			String supplyType = input.getParamValue("tourOperatorChannelCategoryCode");//客户类别：地接社、渠道商
			if(StringUtils.isNotEmpty(supplyType)) {
				costRecord.setSupplyType(Integer.parseInt(supplyType));
			}
			String supplierType = input.getParamValue("tourOperatorOrChannelTypeCode");//地接社类型
			if(StringUtils.isNotEmpty(supplierType)) {
				costRecord.setSupplierType(Integer.parseInt(supplierType));
			}
			String supplyName = input.getParamValue("tourOperatorOrChannelName");//地接社、渠道商id
//			if(UserUtils.isMtourUser() && id != null && StringUtils.isNotEmpty(id.toString())){
//				supplyName = input.getParamValue("fundsName");
//			}
			costRecord.setSupplyName(supplyName);
			Integer supplyId = jsonObj.getIntValue("tourOperatorOrChannelUuid");//地接社、渠道商名称
			if(supplyId != null) {
				costRecord.setSupplyId(supplyId);
			}
			
			Double rate = jsonObj.getDoubleValue("exchangeRate");//汇率
			if(rate != null) {
				costRecord.setRate(new BigDecimal(rate));
			}
			Integer currencyId = jsonObj.getIntValue("currencyUuid");//转换前币种
			if(currencyId != null) {
				costRecord.setCurrencyId(currencyId);
			}
			Integer currencyAfter = jsonObj.getIntValue("convertedCurrencyUuid");//转换后币种
			if(currencyAfter != null) {
				costRecord.setCurrencyAfter(currencyAfter);
			}
			
			Double priceAfter = jsonObj.getDoubleValue("convertedAmount");//总价
			if(priceAfter != null) {
				costRecord.setPriceAfter(new BigDecimal(priceAfter));
			}
			Integer invoiceOriginalTypeCode = jsonObj.getIntValue("invoiceOriginalTypeCode");//区分大编号还是地接社
			if(invoiceOriginalTypeCode != null) {
				costRecord.setIsJoin(invoiceOriginalTypeCode);
			}
			if(input.getParamValue("PNR") != null && "".equals(input.getParamValue("PNR"))) {
				costRecord.setBigCode(input.getParamValue("tourOperatorOrChannelUuid"));//大编号为空时存地接社id
			}else{
				costRecord.setBigCode(input.getParamValue("PNR"));//大编号
			}
			String pnrUuid = input.getParamValue("invoiceOriginalUuid");
			costRecord.setPnrUuid(pnrUuid);
			AirticketOrderPnr airticketOrderPnr = airticketOrderPnrService.getByUuid(pnrUuid);
			costRecord.setAirline(airticketOrderPnr.getAirline());
			costRecord.setBankName(input.getParamValue("paymentBank"));//银行名称
			costRecord.setBankAccount(input.getParamValue("paymentAccount"));//银行账号
			costRecord.setComment(input.getParamValue("memo"));//备注
			costRecord.setBudgetType(1);
			costRecord.setOverseas(0);
			costRecord.setAirlineUuid(input.getParamValue("airlineUuid"));//航段表uuid
			costRecord.setAirlineName(input.getParamValue("airlineName"));//航段名称
			Double costTotalDeposit = jsonObj.getDoubleValue("costTotalDeposit");//定金总额
			if(costTotalDeposit != null) {
				costRecord.setCostTotalDeposit(new BigDecimal(costTotalDeposit));
			}
			
			//同步更新订单成本
			updateOrderCost(costRecord);
			
			financeService.saveOrUpdateCostRecord(costRecord);
			// 提交成本记录时，更新订单的付款状态
			if("1".equals(saveType)){
				financeService.updateOrderRefundFlag(Long.valueOf(orderId));
			}
			out.setResponseCode4Success();
		} catch(Exception e) {
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用成本录入保存更新接口出现异常，请重新操作！");
		}
		
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 修改成本录入时同步更新订单成本
	 * @param costRecord
	 */
	private void updateOrderCost(CostRecord costRecord) {
		AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice = null;
		if(costRecord.getUuid() != null) {
			String uuid = costRecord.getUuid();
			airticketOrderPnrAirlinePrice = airticketOrderPnrAirlinePriceService.getByCostRecordUuid(uuid);
			if(airticketOrderPnrAirlinePrice != null) {
				airticketOrderPnrAirlinePrice.setAirticketOrderPnrAirlineUuid(costRecord.getAirlineUuid());//航段表uuid
				airticketOrderPnrAirlinePrice.setPersonNum(costRecord.getQuantity());//人数
				airticketOrderPnrAirlinePrice.setCurrencyId(costRecord.getCurrencyId());//币种
				airticketOrderPnrAirlinePrice.setExchangerate(costRecord.getRate().doubleValue());//汇率
				airticketOrderPnrAirlinePrice.setPrice(costRecord.getPrice().doubleValue());//价格
				airticketOrderPnrAirlinePrice.setFrontMoney(costRecord.getCostTotalDeposit().doubleValue());//定金
				airticketOrderPnrAirlinePriceService.update(airticketOrderPnrAirlinePrice);
			}else{
				airticketOrderPnrAirlinePrice = new AirticketOrderPnrAirlinePrice();
				airticketOrderPnrAirlinePrice.setUuid(UuidUtils.generUuid());
				airticketOrderPnrAirlinePrice.setAirticketOrderId(costRecord.getOrderId().intValue());
				airticketOrderPnrAirlinePrice.setAirticketOrderPnrUuid(costRecord.getPnrUuid());
				airticketOrderPnrAirlinePrice.setAirticketOrderPnrAirlineUuid(costRecord.getAirlineUuid());
				airticketOrderPnrAirlinePrice.setCostRecordUuid(costRecord.getUuid());
				airticketOrderPnrAirlinePrice.setPriceType(0);
				airticketOrderPnrAirlinePrice.setCurrencyId(costRecord.getCurrencyId());
				airticketOrderPnrAirlinePrice.setExchangerate(costRecord.getRate().doubleValue());
				airticketOrderPnrAirlinePrice.setPersonNum(costRecord.getQuantity());
				airticketOrderPnrAirlinePrice.setPrice(costRecord.getPrice().doubleValue());
				airticketOrderPnrAirlinePrice.setFrontMoney(costRecord.getCostTotalDeposit().doubleValue());
				airticketOrderPnrAirlinePrice.setDelFlag("0");
				airticketOrderPnrAirlinePriceService.save(airticketOrderPnrAirlinePrice);
			}
		}
	}
	
	/**
	 * 其他收入录入-添加/修改
	 * @author zhankui.zong
	 * @param input
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value="saveOrUpdateOtherCostRecord")
	public String saveOrUpdateOtherCostRecord(BaseInput4MT input) throws ParseException {
		BaseOut4MT out = new BaseOut4MT(input);
		try {
			JSONObject jsonObject = JSONObject.parseObject(input.getParam());
			Integer id = null;
			if(jsonObject.containsKey("otherRevenueUuid")) {
				id = jsonObject.getIntValue("otherRevenueUuid");
			}
			CostRecord costRecord = null;
			PayGroup payGroup = null;
			User user = UserUtils.getUser();
			Date date = new Date();
			//根据id判断是添加还是删除
			if(id != null && StringUtils.isNotEmpty(id.toString())) {
				costRecord = costRecordDao.findOne(Long.parseLong(id.toString()));
				costRecord.setUpdateBy(user.getId());
				costRecord.setUpdateDate(date);
				
				List<PayGroup> payGroupList = payGroupDao.findById(Integer.parseInt(id.toString()));
				if(payGroupList != null && payGroupList.size() > 0) {
					payGroup = payGroupList.get(0);
					payGroup.setUpdateBy(Integer.parseInt(user.getId().toString()));
					payGroup.setUpdateDate(date);
				}else{
					payGroup = new PayGroup();
					payGroup.setCreateBy(Integer.parseInt(user.getId().toString()));
					payGroup.setCreateDate(date);
					payGroup.setUpdateBy(Integer.parseInt(user.getId().toString()));
					payGroup.setUpdateDate(date);
				}
			}else{
				costRecord = new CostRecord();
				costRecord.setUuid(UuidUtils.generUuid());
				costRecord.setCreateBy(user);
				costRecord.setCreateDate(date);
				costRecord.setUpdateBy(user.getId());
				costRecord.setUpdateDate(date);
				
				payGroup = new PayGroup();
				payGroup.setCreateBy(Integer.parseInt(user.getId().toString()));
				payGroup.setCreateDate(date);
				payGroup.setUpdateBy(Integer.parseInt(user.getId().toString()));
				payGroup.setUpdateDate(date);
			}
			
			String saveType = input.getParamValue("saveType");
			if("0".equals(saveType)) {//保存
				costRecord.setPayStatus(2);
			} else if ("1".equals(saveType)) {//保存并提交
				costRecord.setPayStatus(3);
			}
			String orderId = input.getParamValue("orderUuid");//订单id
			if(StringUtils.isNotEmpty(orderId)) {
				costRecord.setOrderId(Long.parseLong(orderId));
			}
			//获取机票产品id
			String sql = "SELECT airticket_id FROM airticket_order where id = ?";
			List<Map<String, Integer>> airticketIdList = airticketOrderDao.findBySql(sql, Map.class, orderId);
			if(airticketIdList != null && airticketIdList.size() > 0) {
				Integer airticketId = airticketIdList.get(0).get("airticket_id");
				if(airticketId != null) {
					costRecord.setActivityId(Long.parseLong(airticketId.toString()));
				}
			}
			costRecord.setOrderType(Context.ORDER_TYPE_JP);
			costRecord.setReviewType(0);
			
			costRecord.setName(input.getParamValue("fundsName"));//款项名称
			costRecord.setQuantity(1);

			String price = input.getParamValue("amount");//单价
			if(StringUtils.isNotEmpty(price)) {
				costRecord.setPrice(new BigDecimal(price));
			}
			String supplyType = input.getParamValue("tourOperatorChannelCategoryCode");//区分地接社还是渠道商
			if(StringUtils.isNotEmpty(supplyType)) {
				costRecord.setSupplyType(Integer.parseInt(supplyType));
			}
			String supplierType = input.getParamValue("tourOperatorOrChannelTypeCode");//地接社类型
			if(StringUtils.isNotEmpty(supplierType)) {
				costRecord.setSupplierType(Integer.parseInt(supplierType));
			}
			String supplyName = input.getParamValue("tourOperatorOrChannelName");//地接社、渠道商名称
			costRecord.setSupplyName(supplyName);
			Integer supplyId = jsonObject.getIntValue("tourOperatorOrChannelUuid");//地接社、渠道商id
			if(supplyId != null) {
				costRecord.setSupplyId(supplyId);
			}
			
			costRecord.setBankName(input.getParamValue("paymentBank"));//银行名称
			costRecord.setBankAccount(input.getParamValue("paymentAccount"));//银行账号
			costRecord.setComment(input.getParamValue("memo"));//备注
			costRecord.setBudgetType(2);
			costRecord.setOverseas(0);
			
//			List<DocInfo> docList = new ArrayList<DocInfo>();
			
			Double rate = jsonObject.getDoubleValue("exchangeRate");//汇率
			if(rate != null) {
				costRecord.setRate(new BigDecimal(rate));
			}else{
				throw new RuntimeException("汇率不能为空！");
			}
			Integer currencyId = jsonObject.getIntValue("currencyUuid");//转换前币种
			if(currencyId != null) {
				costRecord.setCurrencyId(currencyId);
			}
			Integer currencyAfter = jsonObject.getIntValue("convertedCurrencyUuid");//转换后币种
			if(currencyAfter != null) {
				costRecord.setCurrencyAfter(currencyAfter);
			}
			//区分大编号还是地接社，其他收入没用，可以注掉
			Integer invoiceOriginalTypeCode = jsonObject.getIntValue("invoiceOriginalTypeCode");
			if(invoiceOriginalTypeCode != null) {
				costRecord.setIsJoin(invoiceOriginalTypeCode);
			}
			costRecord.setBigCode(input.getParamValue("PNR"));
			Double priceAfter = jsonObject.getDoubleValue("convertedAmount");//转换后总价
			if(priceAfter != null) {
				costRecord.setPriceAfter(new BigDecimal(priceAfter));
			}
			
			//附件id
			String attachmentIds = "";
			JSONArray jsonArray = jsonObject.getJSONArray("attachments");
			if(jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					attachmentIds += obj.getString("attachmentUuid") + ",";
				}
			}

			//付款方式
			Integer payType = jsonObject.getIntValue("paymentMethodCode");
			if(payType != null) {
				payGroup.setPayType(payType);
			}
			
			if(payType == 1) { //支票
				payGroup.setPayTypeName("支票");
				payGroup.setCheckNumber(input.getParamValue("checkNo"));
				String invoiceDate = input.getParamValue("checkIssueDate");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(StringUtils.isNotEmpty(invoiceDate)) {
					payGroup.setInvoiceDate(sdf.parse(invoiceDate));
				}
			}else if(payType == 3) { //现金
				payGroup.setPayTypeName("现金");
			}else if(payType == 4) { //汇款
				payGroup.setPayTypeName("汇款");
				payGroup.setBankName(input.getParamValue("paymentBank"));
				payGroup.setBankAccount(input.getParamValue("paymentAccount"));
				payGroup.setToBankNname(input.getParamValue("receiveBank"));
				payGroup.setToBankAccount(input.getParamValue("receiveAccount"));
			}
			payGroup.setPayVoucher(attachmentIds);//附件id
			payGroup.setUuid(UuidUtils.generUuid());
			payGroup.setPayerName(input.getParamValue("payer"));//付款单位
			payGroup.setIsAsAccount(99);//是否达帐
			payGroup.setOrderType(Context.ORDER_TYPE_JP);
			payGroup.setPayPriceType(0);//支付款类型
			String payStatus = input.getParamValue("speedyClearance");//即时结算
			if(payStatus != null) {
				payGroup.setPaymentStatus(Integer.parseInt(payStatus));
			}
			
//			if("1".equals(payStatus)) {
//				payGroup.setPaymentStatus(0);
//			}else if("0".equals(payStatus)) {
//				payGroup.setPaymentStatus(1);
//			}
			payGroup.setDelFlag("0");
			
			//设置流水号uuid
			String moneySerialNum = UuidUtils.generUuid();
			payGroup.setPayPrice(moneySerialNum);
			AirticketOrder airticketOrder = null;
			if(orderId != null) {
				airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
			}
			Integer airticketId = null;
			if(airticketOrder != null && airticketOrder.getAirticketId() != null) {
				airticketId = airticketOrder.getAirticketId().intValue();
			}
			payGroup.setGroupId(airticketId);
			
			String currencyIdStr = "";
			if(currencyId != null) {
				currencyIdStr = currencyId.toString();
			}
			String[] currencyIds = new String[]{currencyIdStr};
			String[] amounts = new String[]{price};
			String[] exchangeRates = new String[]{costRecord.getRate().toString()};
			//保存money_amount
			moneyAmountService.saveMoneyAmountByPayGroupInfo(payGroup, currencyIds, exchangeRates, amounts,
					Context.BUSINESS_TYPE_GROUP, airticketId != null ? airticketId.longValue() : null);
			financeService.saveOrUpdateOtherCostRecord(costRecord, payGroup);
			out.setResponseCode4Success();
		} catch (Exception e) {
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用其他收入录入保存更新接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 获取大编号列表
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getBigCodes")
	public String getBigCodes(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String orderId = input.getParamValue("orderUuid");	//订单id
		if(!StringUtils.isEmpty(orderId)) {
			List<BigCode> list = financeService.getBigCodeList(Integer.parseInt(orderId));
			out.setResponseCode4Success();
			out.setData(list);
		}else{
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用大编号列表接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 付款-款项明细-成本录入
	 * @author zhankui.zong
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getPaymentCost")
	public String getPaymentCost(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String costId = input.getParamValue("paymentUuid");
		if(StringUtils.isNotEmpty(costId)) {
			List<Map<String,Object>> list = financeService.getPaymentCost(Integer.parseInt(costId));
			if(!list.isEmpty()){
				out.setData(list.get(0));
			}
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用付款-款项明细-成本录入接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	
	/**
	 * 订单-二级列表-款项明细查询
	 * @author songyang
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getOrderPaymentInfo")
	public String getOrderPaymentInfo(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		if(StringUtils.isNotEmpty(input.getParamValue("orderUuid"))){
			try {
				RefundRecordsJsonBean data = financeService.getOrderPaymentInfo(input);
				out.setData(data);
				out.setResponseCode4Success();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用付款-款项明细-成本录入接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	
	/**
	 * 付款列表页
	 * @param input
	 * @return String
	 * @author zhaohaiming
	 * */
	@ResponseBody
	@RequestMapping(value="getPayList")
	public String getPayList(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		Page page = financeService.getPayList(input);
		if(page != null){
			out.setResponseCode4Success();
			out.setData(page);
		}else{
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用付款列表接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out).replaceAll("pNR","PNR");
	}
	
	/**
	 * 成本记录详情
	 * @author hhx
	 * @date 2015-10-21
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getCostDetail")
	public String getCostDetail(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			List<Map<String,Object>> list = financeService.getCostDetail(input);
			if(!list.isEmpty()){
				out.setData(list.get(0));
			}
			out.setResponseCode4Success();
		}catch (Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 其他收入记录详情
	 * @author hhx
	 * @date 2015-10-22
	 * @param input
	 * @return
	 */
	public String getOtherPayDetail(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			List<Map<String,Object>> list = financeService.getOtherPayDetail(input);
			for(int i=0;i<list.size();i++){
				String temp = list.get(i).get("docId").toString();
				if(StringUtils.isNotBlank(temp)){
					List<Map<String,String>> docDetail = financeService.getReceiptDocDetail(temp.substring(0, temp.length()-1));
					list.get(i).put("attachments", docDetail);
				}
			}
			out.setData(list);
			out.setResponseCode4Success();
		}catch (Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		return toAndCacheJSONString(out);
	}
	
	
	

	/**
	 * 
	 * 测试url 和  参数 ，测试参数为 203库的数据
	 * localhost:8080/trekiz_wholesaler_tts/static/mtour/doc/api/apitest.html
	 * testurl:http://localhost:8080/trekiz_wholesaler_tts/a/mtourfinance/getMtourPaySheet {"paymentUuid":"970","fundsType":"4"}
	 * 参数：{"paymentUuid":"970","fundsType":"4"}  {"paymentUuid":"","fundsType":"1"}
	 * 
	 * 203上的137库
	 *  cost_record                               airticket_order_moneyAmount
	 *  {"paymentUuid":"383","fundsType":"4"},{"paymentUuid":"401","fundsType":"1"}
	 * 
	 * 美途支出凭单数据接口：
	 * wangxinwei 20151021 added
	 * @author wangxinwei
	 * @param 
	 * @param 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getMtourPaySheet")
	public String getMtourPaySheet(BaseInput4MT input){

		/**
		 * 当 fundsType为:借款：1，退款：2，追加成本：3，这三种类型时 取  airticket_order_moneyAmount
	     * 当 fundsType为 :1,2,3以外的其它类型 取cost_record表数据
		 * paymentUuid:订单ID cost_record, 或 airticket_order_moneyAmount 表id
		 * type:借款：1，退款：2，追加成本：3，其他 为成本:4。
		 */

		BaseOut4MT out = new BaseOut4MT(input);
		String paymentUuid = input.getParamValue("paymentUuid");//渠道Id,批发商Id
		String fundsType = input.getParamValue("fundsType");//0表示批发商,1表示地接社,2表示渠道
		PaySheetJsonBean paySheetJsonBean = null;
		if (null==paymentUuid||null==fundsType) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常,支出凭单类型或ID为空.");
		}else {
		
			try {
				if ("1".equals(fundsType)||"2".equals(fundsType)||"3".equals(fundsType)) {
					AirticketOrderMoneyAmount airticketOrderMoneyAmount =airticketOrderMoneyAmountService.getById(Integer.parseInt(paymentUuid));
					if (null!=airticketOrderMoneyAmount) {
						paySheetJsonBean = objest2MtourPaySheetJsonBean(airticketOrderMoneyAmount,fundsType);
						out.setResponseCode4Success();
						out.setData(paySheetJsonBean);
					}else {
						out.setResponseCode4Fail();
						out.putMsgCode(BaseOut4MT.CODE_0001);
						out.putMsgDescription("传递参数异常,支出凭单类型或ID为空或无效ID.");
					}
					
				}else {
					CostRecord costRecord = costManageService.findOne(Long.parseLong(paymentUuid));
					if (null!=costRecord) {
						paySheetJsonBean = objest2MtourPaySheetJsonBean(costRecord,fundsType);
						out.setResponseCode4Success();
						out.setData(paySheetJsonBean);
						/*out.setData("");*/
					}else {
						out.setResponseCode4Fail();
						out.putMsgCode(BaseOut4MT.CODE_0001);
						out.putMsgDescription("传递参数异常,支出凭单类型或ID为空或无效ID.");
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("支出申请单数据接口出现异常，请重新操作！");
			}
		}
		return toAndCacheJSONString(out);
	}
	
	
	
	
	/**
	 * 
	 * http://localhost:8080/trekiz_wholesaler_tts/a/mtour/mtourfinance/mergePrintOutPayPage?mergeParam={%22orderUuid%22:4264,%22paymentObjectUuid%22:1333,%22fundsTypePayList%22:[{%22fundsType%22:%223%22,%22paymentUuid%22:2283},{%22fundsType%22:%222%22,%22paymentUuid%22:2284},{%22fundsType%22:%223%22,%22paymentUuid%22:2285}]}
	 * 合开支出单
	 * 获取美途支出单JsonBean
	 * @author songyang
	 * JSONObject jsonObj = JSONObject.parseObject(input.getParam());
	 * JSONArray addAmountList = jsonObj.getJSONArray("totalSalePrice");
	 * Integer currencyId = obj.getInteger("currencyUuid");
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="mergePaymentDoc")
	public String  mergePaymentDoc(BaseInput4MT input4mt){
		BaseOut4MT out = new BaseOut4MT(input4mt);
		PaySheetJsonBean paySheetJsonBean = new PaySheetJsonBean();
		//订单ID  
		String orderId = input4mt.getParamValue("orderUuid");  
		//付款对象UUID
		String paymentObjectUuid = input4mt.getParamValue("paymentObjectUuid");
		//付款类型数组
		JSONObject jsonObj = JSONObject.parseObject(input4mt.getParam());
		JSONArray fundsTypePayList = jsonObj.getJSONArray("fundsTypePayList");
		AirticketOrder airOrder =  airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
		Agentinfo agentinfoo = agentinfoService.findOne(airOrder.getAgentinfoId());
		paySheetJsonBean.setTourOperatorName(agentinfoo.getAgentName());
		//日期集合
		List<Date>  listApplicantDate = new ArrayList<Date>(); 
		String applicantDateStr = ""; 
		double BigTotalRMB = 0;//人民币
		double BigTotalOther = 0 ;//外币
		//流水号List
		List<String[]> listObtainSerialNumber = new ArrayList<String[]>();
		Set<Object> purpose = new  HashSet<Object>();//用途
		String purposeStr = ""; //多用途拼接
		Set<Object> pnr = new  HashSet<Object>();//PNR
		String pnrStr = ""; //PNR
		Set<Object> createBy = new HashSet<Object>();//申请人
		//出纳
		Set<Object> paymentPeople = new HashSet<Object>();
		String paymentPeopleStr = "";
		paySheetJsonBean.setGroupNo(airOrder.getGroupCode());//团号
		if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(paymentObjectUuid)||null==fundsTypePayList){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常,支出凭单类型或ID为空.");
		}else{
			try {
				for (int i = 0; i < fundsTypePayList.size(); i++) {
					JSONObject obj = fundsTypePayList.getJSONObject(i);
					if("1".equals(obj.get("fundsType"))||"2".equals(obj.get("fundsType"))||"3".equals(obj.get("fundsType"))){
						AirticketOrderMoneyAmount airticketOrderMoneyAmount =airticketOrderMoneyAmountService.getById(Integer.parseInt(obj.getString("paymentUuid")));
						listApplicantDate.add(airticketOrderMoneyAmount.getCreateDate());
						//借款：1，退款：2，追加成本：3  //改成款项名称 ，手填的， 2016年2月17日11:22:04 产品确认
//						if("1".equals(obj.get("fundsType"))){
//							purpose.add("借款");//用途
//						}else if("2".equals(obj.get("fundsType"))){
//							purpose.add("退款");//用途
//						}else if("3".equals(obj.get("fundsType"))){
//							purpose.add("追加成本");//用途
//						}
						
						purpose.add(airticketOrderMoneyAmount.getFundsName());
						
						//处理币种
						Currency currency = currencyDao.findById(airticketOrderMoneyAmount.getCurrencyId().longValue());
						Double amount = airticketOrderMoneyAmount.getAmount();
						amount = (null==amount)?0:amount; 
						//放到流水号List集合中
				        String[] strObtainSerialNumber = {obj.get("fundsType").toString(),obj.getString("paymentUuid")};
//				        String[] strObtainSerialNumber = {obj.getString("paymentUuid"),obj.get("fundsType").toString()};
				        listObtainSerialNumber.add(strObtainSerialNumber);
				        
						/**
						 * 如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
						 * 借款：1，退款：2，追加成本：3,成本付款：4
						 * 
						 */
						if ("1".equals(obj.get("fundsType"))) {
							paySheetJsonBean.setTourOperatorOrChannelName("");//'支付对象名称'
						}else if ("2".equals(obj.get("fundsType"))||"3".equals(obj.get("fundsType"))) {
							Agentinfo agentinfo = agentinfoService.findOne(airOrder.getAgentinfoId());
							if (null!=agentinfo) {
								paySheetJsonBean.setTourOperatorOrChannelName(agentinfo.getAgentName());//'支付对象名称'
							}else {
								paySheetJsonBean.setTourOperatorOrChannelName("非签渠道");//'支付对象名称'
							}
						}else{
							paySheetJsonBean.setTourOperatorOrChannelName("非签渠道");//'支付对象名称'
						}

				        
						if(null!=currency){
							if ("¥".equals(currency.getCurrencyMark())) {
								BigTotalRMB =  BigTotalRMB+amount;
								paySheetJsonBean.setTotalRMB(currency.getCurrencyMark()+fmtMicrometer(BigTotalRMB+""));//'计人民币'
								if (UserUtils.isMtourUser()) {
									paySheetJsonBean.setTotalRMB_CN(MoneyAmountUtils.generUppercase(currency.getCurrencyName(), BigTotalRMB));//'计人民币(大写)'
								}else {
									paySheetJsonBean.setTotalRMB_CN(digitUppercase(BigTotalRMB));//'计人民币(大写)'
								}
							}else{
								BigTotalOther = BigTotalOther+amount;
								paySheetJsonBean.setTotalOther(currency.getCurrencyMark()+fmtMicrometer(BigTotalOther+"")); //外币
								String totalother_cn = digitUppercase(BigTotalOther);
								totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
								if (UserUtils.isMtourUser()) {
									//0044需求修改 add by majiancheng 2016-01-11
									paySheetJsonBean.setTotalOther_CN(MoneyAmountUtils.generUppercase(currency.getCurrencyName(), BigTotalOther)); //外币中文
								}else {
									paySheetJsonBean.setTotalOther_CN(totalother_cn); //外币中文
								}
							}
						}
						createBy.add(Long.parseLong(airticketOrderMoneyAmount.getCreateBy().toString()));

						//添加出纳显示信息
						Integer moneyType = null;
						if(airticketOrderMoneyAmount.getMoneyType() == 1) {
							moneyType = Refund.MONEY_TYPE_BORROW;
						} else if(airticketOrderMoneyAmount.getMoneyType() == 2) {
							moneyType = Refund.MONEY_TYPE_RETURNMONEY;
						} else if(airticketOrderMoneyAmount.getMoneyType() == 3) {
							moneyType = Refund.MONEY_TYPE_ADDCOST;
						}
						List<Refund> refunds = refundService.getRefundsByRecordId(airticketOrderMoneyAmount.getId().longValue(), moneyType);
						StringBuffer sb = new StringBuffer();
						List<String> userIds = new ArrayList<String>();
						if(CollectionUtils.isNotEmpty(refunds)) {
							for(Refund refund : refunds) {
								if(!userIds.contains(refund.getCreateBy().getId().toString())) {
									sb.append(refund.getCreateBy().getName());
									sb.append(",");
									userIds.add(refund.getCreateBy().getId().toString());
								}
							}
							sb.deleteCharAt(sb.length()-1);
						}
						paymentPeople.add(sb.toString());
					}else{
						CostRecord costRecord = costManageService.findOne(Long.parseLong(obj.getString("paymentUuid")));
						pnr.add(costRecord.getBigCode());
						listApplicantDate.add(costRecord.getCreateDate());
						//放到流水号List集合中
						String[] strObtainSerialNumber = {obj.get("fundsType").toString(),obj.getString("paymentUuid")};
						listObtainSerialNumber.add(strObtainSerialNumber);
						//成本
						if (null!=costRecord.getSupplyName()) {
							paySheetJsonBean.setTourOperatorOrChannelName(costRecord.getSupplyName());//'支付对象名称'
						}else{
						    paySheetJsonBean.setTourOperatorOrChannelName("");//'支付对象名称'
						}
						//处理币种
						Currency currency = currencyDao.findById(costRecord.getCurrencyId().longValue());
						if ("¥".equals(currency.getCurrencyMark())) {
							BigTotalRMB =  BigTotalRMB+Double.parseDouble(costRecord.getPriceAfter().toString());
							paySheetJsonBean.setTotalRMB(currency.getCurrencyMark()+fmtMicrometer(BigTotalRMB+""));//'计人民币'
							if (UserUtils.isMtourUser()) {
								paySheetJsonBean.setTotalRMB_CN(MoneyAmountUtils.generUppercase(currency.getCurrencyName(), BigTotalRMB));//'计人民币(大写)'
							}else {
								paySheetJsonBean.setTotalRMB_CN(digitUppercase(BigTotalRMB));//'计人民币(大写)'
							}
						}else{
							Double amount = costRecord.getPrice().doubleValue()*costRecord.getQuantity();
							BigTotalOther = BigTotalOther+amount;
							paySheetJsonBean.setTotalOther(currency.getCurrencyMark()+fmtMicrometer(BigTotalOther+"")); //外币
							String totalother_cn = digitUppercase(BigTotalOther);
							totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
							if (UserUtils.isMtourUser()) {
								paySheetJsonBean.setTotalOther_CN(MoneyAmountUtils.generUppercase(currency.getCurrencyName(), BigTotalOther)); //外币中文
							}else {
								paySheetJsonBean.setTotalOther_CN(totalother_cn); //外币中文
							}
						}
						purpose.add(costRecord.getName());//用途
						//申请人
						createBy.add(costRecord.getCreateBy().getId());
						
						//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
						List<Refund> refunds = refundService.getRefundsByRecordId(costRecord.getId(), Refund.MONEY_TYPE_COST);
						StringBuffer sb = new StringBuffer();
						List<String> userIds = new ArrayList<String>();
						if(CollectionUtils.isNotEmpty(refunds)) {
							for(Refund refund : refunds) {
								if(!userIds.contains(refund.getCreateBy().getId().toString())) {
									sb.append(refund.getCreateBy().getName());
									sb.append(",");
									userIds.add(refund.getCreateBy().getId().toString());
								}
							}
							sb.deleteCharAt(sb.length()-1);
						}
						paymentPeople.add(sb.toString());
					}
				}
				//出纳
				for (Object paymentPeoples : paymentPeople) {
					paymentPeopleStr+=paymentPeoples+",";
				}
				if(StringUtils.isNotEmpty(paymentPeopleStr)){
					paySheetJsonBean.setPaymentPeople(paymentPeopleStr.substring(0, paymentPeopleStr.length()-1));
				}
				
				//流水号
				String obtainSerialNumber = serialNumberService.obtainSerialNumber(listObtainSerialNumber);
				if(fundsTypePayList.size()>1){
					paySheetJsonBean.setPrintNo(obtainSerialNumber+"(合)");
				}else{
					paySheetJsonBean.setPrintNo(obtainSerialNumber);
				}
				
				//pnr
				for (Object spnr : pnr) {
					pnrStr+=spnr+",";
				}
				if(StringUtils.isNotEmpty(pnrStr)){
					paySheetJsonBean.setPnr(pnrStr.substring(0, pnrStr.length()-1));
				}
				
				//用途
				for (Object pstr : purpose) {
					purposeStr += pstr+",";
				}
				if(StringUtils.isNotEmpty(purposeStr)){
					paySheetJsonBean.setPurpose(purposeStr.substring(0, purposeStr.length()-1));
				}
				//申请人
				String createStr  = "";
				for (Object create : createBy) {
					createStr += UserUtils.getUserNameById(Long.parseLong(create.toString()));
				}
	
				paySheetJsonBean.setApplicant(createStr);
				
				//获取最大时间
			    ComparatorDate c = new ComparatorDate();  
			    Collections.sort(listApplicantDate, c);  
				for (Object date : listApplicantDate) {
					SimpleDateFormat sFormat = new SimpleDateFormat(DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY);
					applicantDateStr= sFormat.format(date); 
				}

				paySheetJsonBean.setApplicantDate(applicantDateStr);
				
				out.setResponseCode4Success();
				out.setData(paySheetJsonBean);
			} catch (Exception e) {
				e.printStackTrace();
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("支出申请单数据接口出现异常，请重新操作！");
			}
		}
		
		return toAndCacheJSONString(out);
	}
	
	
	
	/**
	 * 20151021 added
	 * 获取美途支出单JsonBean
	 * @author wangxinwei
	 * @param obj：CostRecord 或  AirticketOrderMoneyAmount
	 * @param fundsType：
	 * 当 fundsType为:借款：1，退款：2，追加成本：3，这三种类型时 取  airticket_order_moneyAmount
	 * 当 fundsType为 :1,2,3以外的其它类型 取cost_record表数据
	 * @return
	 */
	private PaySheetJsonBean objest2MtourPaySheetJsonBean(Object obj,String fundsType){
		PaySheetJsonBean paySheetJsonBean = null;
		
		//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
		Long recordId = null;
		Integer moneyType = null;
		if (null!=obj) {
			paySheetJsonBean = new PaySheetJsonBean();
			if (obj instanceof CostRecord) {
				CostRecord costRecord = (CostRecord)obj;
				paySheetJsonBean.setPaymentUuid(costRecord.getId()+"");//'付款Uuid'
				
				//提交该笔付款申请的日期
				paySheetJsonBean.setApplicantDate(DateUtils.formatDate(costRecord.getCreateDate(), "yyyy年MM月dd日"));//'日期'
				//团号
				AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(costRecord.getOrderId());
				
				ActivityAirTicket activityAirTicket =  activityAirTicketServiceImpl.getActivityAirTicketById(airticketOrder.getAirticketId());
				paySheetJsonBean.setGroupNo(activityAirTicket.getGroupCode());//'团号'
				
				//这三项暂时先去掉
				//如果为成本 InvoiceOriginalTypeCode 值取PNR， PNR取costRecord.getBigCode()
				//paySheetJsonBean.setInvoiceOriginalTypeCode("PNR");//'PNR或者地接社的的代码',//大编号(票源类型)
				//如果是成本付款，则取成本付款中的大编号；如果是退款、追加成本、借款，则为空
				//paySheetJsonBean.setPNR(costRecord.getBigCode());//'PNR编号',//大编号为PNR的时候才有意义
				//各种情况都置为空 不用取值
				//paySheetJsonBean.setTourOperatorName("");//'地接社Name'//大编号为地接社的时候才有意义  

				/**
				 * 如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
				 */
				if (null!=costRecord.getSupplyName()) {
					paySheetJsonBean.setTourOperatorOrChannelName(costRecord.getSupplyName());//'支付对象名称'
				}else{
				    paySheetJsonBean.setTourOperatorOrChannelName("");//'支付对象名称'
				}
				
				/**
				 *用途取值
				 *1.如果是借款，则为预付款；
				 *2.如果是退款，则为退款
				 *3.如果是追加成本，则为追加成本；
				 *4.如果是成本付款，则为成本支付；
				 *wangxinwei 20151111 added 用途有变化，改为取输入的款项名称:
				 *paySheetJsonBean.setPurpose(FinanceController.CBZF); ->paySheetJsonBean.setPurpose(costRecord.getName());
				 */
				paySheetJsonBean.setPurpose(costRecord.getName());//'用途'
				
				//流水号
				String serialNum = financeService.getSerialNumByTableNameAndRecordId("cost_record", costRecord.getId().intValue());
				paySheetJsonBean.setPrintNo(serialNum); //流水号
				
				//
				//处理币种
				Currency currency = currencyDao.findById(costRecord.getCurrencyId().longValue());
				if ("¥".equals(currency.getCurrencyMark())) {
					paySheetJsonBean.setTotalRMB(currency.getCurrencyMark()+fmtMicrometer(costRecord.getPriceAfter()+""));//'计人民币'
					
					if (UserUtils.isMtourUser()) {
						//0044需求修改 add by majiancheng 2016-01-11
						paySheetJsonBean.setTotalRMB_CN(MoneyAmountUtils.generUppercase(currency.getCurrencyName(), Double.parseDouble(costRecord.getPriceAfter()+"")));//'计人民币(大写)'
					}else {
						paySheetJsonBean.setTotalRMB_CN(digitUppercase(Double.parseDouble(costRecord.getPriceAfter()+"")));//'计人民币(大写)'
					}
					
					
					paySheetJsonBean.setTotalOther("");
					paySheetJsonBean.setTotalOther_CN("");
				}else{
					paySheetJsonBean.setTotalRMB("");//'计人民币'
					paySheetJsonBean.setTotalRMB_CN("");//'计人民币(大写)'
					
//					Double amount = costRecord.getPrice().doubleValue();
//					外币抓取单价*人数---修改于20151217 by 王新伟
					Double amount = costRecord.getPrice().doubleValue()*costRecord.getQuantity();
					paySheetJsonBean.setTotalOther(currency.getCurrencyMark()+fmtMicrometer(amount.toString())); //外币
					
					String totalother_cn = digitUppercase(amount);
					totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
					
					if (UserUtils.isMtourUser()) {
						//0044需求修改 add by majiancheng 2016-01-11
						paySheetJsonBean.setTotalOther_CN(MoneyAmountUtils.generUppercase(currency.getCurrencyName(), amount)); //外币中文
					}else {
						paySheetJsonBean.setTotalOther_CN(totalother_cn); //外币中文
					}
				}
				
				paySheetJsonBean.setApplicant(costRecord.getCreateBy().getName());//'申请人
				
				//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
				recordId = costRecord.getId();
				moneyType = Refund.MONEY_TYPE_COST;
				
			}else if (obj instanceof AirticketOrderMoneyAmount) {
				AirticketOrderMoneyAmount airticketOrderMoneyAmount = (AirticketOrderMoneyAmount)obj;
				paySheetJsonBean.setPaymentUuid(airticketOrderMoneyAmount.getUuid());//'付款Uuid'
				paySheetJsonBean.setApplicantDate(DateUtils.date2String(airticketOrderMoneyAmount.getCreateDate(), "yyyy年MM月dd日"));//'日期'
				
				//airticket_order_id
				//团号
				AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(airticketOrderMoneyAmount.getAirticketOrderId()+""));
				//airticketOrder.getAirticketId()
				ActivityAirTicket activityAirTicket =  activityAirTicketServiceImpl.getActivityAirTicketById(airticketOrder.getAirticketId());
				paySheetJsonBean.setGroupNo(activityAirTicket.getGroupCode());//'团号'
				
				
				/**
				 * 这三项暂时先去掉
				 */
				//如果为成本 InvoiceOriginalTypeCode 值取PNR， PNR取costRecord.getBigCode()
				//paySheetJsonBean.setInvoiceOriginalTypeCode("");//'PNR或者地接社的的代码',//大编号(票源类型)
				//paySheetJsonBean.setPNR("");//'PNR编号',//大编号为PNR的时候才有意义
				//各种情况都置为空 不用取值
				//paySheetJsonBean.setTourOperatorName("");//'地接社Name'//大编号为地接社的时候才有意义
				
				/**
				 * 如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
				 * 借款：1，退款：2，追加成本：3,成本付款：4
				 */
				if ("1".equals(fundsType)) {
					paySheetJsonBean.setTourOperatorOrChannelName("");//'支付对象名称'
				}else if ("2".equals(fundsType)||"3".equals(fundsType)) {
					Agentinfo agentinfo = agentinfoService.findOne(airticketOrder.getAgentinfoId());
					if (null!=agentinfo) {
						paySheetJsonBean.setTourOperatorOrChannelName(agentinfo.getAgentName());//'支付对象名称'
					}else {
						paySheetJsonBean.setTourOperatorOrChannelName("非签渠道");//'支付对象名称'
					}
				}else{
					paySheetJsonBean.setTourOperatorOrChannelName("非签渠道");//'支付对象名称'
				}
				
				
				/**
				 *用途取值
				 *1.如果是借款，则为预付款；
				 *2.如果是退款，则为退款
				 *3.如果是追加成本，则为追加成本；
				 *4.如果是成本付款，则为成本支付；
				 *wangxinwei 20151111 added 用途有变化，改为取输入的款项名称:
				 *paySheetJsonBean.setPurpose(FinanceController.CBZF); ->paySheetJsonBean.setPurpose(costRecord.getName());
				 */
				/*if ("1".equals(fundsType)) {
					paySheetJsonBean.setPurpose(FinanceController.YFK);//'用途'   预付款
				}else if ("2".equals(fundsType)) {
					paySheetJsonBean.setPurpose(FinanceController.TK);//'用途'   退款
				}else if ("3".equals(fundsType)) {
					paySheetJsonBean.setPurpose(FinanceController.ZJCB);//'用途'  追加成本
				}*/
				paySheetJsonBean.setPurpose(airticketOrderMoneyAmount.getFundsName()); //用途
				
				//流水号
				String serialNum = financeService.getSerialNumByTableNameAndRecordId("airticket_order_moneyAmount", airticketOrderMoneyAmount.getId());
				paySheetJsonBean.setPrintNo(null==serialNum?"":serialNum); //流水号
				
				//处理币种
				Currency currency = currencyDao.findById(airticketOrderMoneyAmount.getCurrencyId().longValue());
				Double amount = airticketOrderMoneyAmount.getAmount();
				amount = (null==amount)?0:amount; 
				
				if(null!=currency){
					if ("¥".equals(currency.getCurrencyMark())) {
						paySheetJsonBean.setTotalRMB(currency.getCurrencyMark()+fmtMicrometer(amount.toString()));//'计人民币'
						
						if (UserUtils.isMtourUser()) {
							//0044需求修改 add by majiancheng 2016-01-11
							paySheetJsonBean.setTotalRMB_CN(MoneyAmountUtils.generUppercase(currency.getCurrencyName(), amount));//'计人民币(大写)'
						}else {
							paySheetJsonBean.setTotalRMB_CN(digitUppercase(amount));//'计人民币(大写)'
						}
						
						paySheetJsonBean.setTotalOther("");
						paySheetJsonBean.setTotalOther_CN("");
					}else{
						paySheetJsonBean.setTotalRMB("");//'计人民币'
						paySheetJsonBean.setTotalRMB_CN("");//'计人民币(大写)'
						
						paySheetJsonBean.setTotalOther(currency.getCurrencyMark()+fmtMicrometer(amount.toString())); //外币
						
						String totalother_cn = digitUppercase(amount);
						totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
						
						if (UserUtils.isMtourUser()) {
							//0044需求修改 add by majiancheng 2016-01-11
							paySheetJsonBean.setTotalOther_CN(MoneyAmountUtils.generUppercase(currency.getCurrencyName(), amount)); //外币中文
						}else {
							paySheetJsonBean.setTotalOther_CN(totalother_cn); //外币中文
						}
						
						
					}
				}
				
			
				
				paySheetJsonBean.setApplicant(UserUtils.getUser(Long.parseLong(airticketOrderMoneyAmount.getCreateBy()+"")).getName());//'申请人
				
				//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
				recordId = airticketOrderMoneyAmount.getId().longValue();
				if(airticketOrderMoneyAmount.getMoneyType() == 1) {
					moneyType = Refund.MONEY_TYPE_BORROW;
				} else if(airticketOrderMoneyAmount.getMoneyType() == 2) {
					moneyType = Refund.MONEY_TYPE_RETURNMONEY;
				} else if(airticketOrderMoneyAmount.getMoneyType() == 3) {
					moneyType = Refund.MONEY_TYPE_ADDCOST;
				}
			}
			
			//paySheetJsonBean.setFundsType(fundsType); //设置支出类型
		}
		
		//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
		List<Refund> refunds = refundService.getRefundsByRecordId(recordId, moneyType);
		StringBuffer sb = new StringBuffer();
		List<String> userIds = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(refunds)) {
			for(Refund refund : refunds) {
				if(!userIds.contains(refund.getCreateBy().getId().toString())) {
					sb.append(refund.getCreateBy().getName());
					sb.append(",");
					userIds.add(refund.getCreateBy().getId().toString());
				}
			}
			sb.deleteCharAt(sb.length()-1);
		}
		
		paySheetJsonBean.setPaymentPeople(sb.toString());
		
		return paySheetJsonBean;
	}
	
    /**
     * 将金额转换为 大写汉字
     * @param n:要转换的金额
     * @return
     */
    private static String digitUppercase(double n){
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};
        String head = n < 0? "负": ""; ////负 -》红字
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
    private String fmtMicrometer(String text){
		DecimalFormat df = null;
/*		if (text.indexOf(".") > 0) {
			if (text.length() - text.indexOf(".") - 1 == 0) {
				df = new DecimalFormat("###,##0.");
			} else if (text.length() - text.indexOf(".") - 1 == 1){
				df = new DecimalFormat("###,##0.0");
			} else{
				df = new DecimalFormat("###,##0.00");
			}

		} else{
			df = new DecimalFormat("###,##0");
		}*/
		df = new DecimalFormat("###,##0.00");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	/**
	 * 收款駁回操作
	 * @param input
	 * @author zhaohaiming
	 * @return String
	 */
    @ResponseBody
    @RequestMapping(value="confirmRejectOper",method=RequestMethod.POST)
    public String confirmRejectOper(BaseInput4MT input){
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			boolean flag = financeService.confirmRejectOper(input);
			if(flag) {
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("调用确认收款接口出现异常，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用确认收款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
    }

    
    /**
     * 测试url中的参数为    203库数据
     * testURl:  
     * http://localhost:8080/trekiz_wholesaler_tts/a/mtour/mtourfinance/downloadMtourPaySheet?paymentUuid=383&fundsType=4
     * http://localhost:8080/trekiz_wholesaler_tts/a/mtour/mtourfinance/downloadMtourPaySheet?paymentUuid=401&fundsType=1
     * 美途支出申请单下载   20151022 added
     * @author wangxinwei
     * @param request
     * @param response
     * @return
     * @throws NumberFormatException
     * @throws Exception
     */
	@RequestMapping(value="downloadMtourPaySheet")
	public ResponseEntity<byte[]> downloadMtourPaySheet(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String paymentUuid = request.getParameter("paymentUuid");
		String fundsType = request.getParameter("fundsType");
		File file = financeService.createMtourPaySheetDownLoadFile(paymentUuid,fundsType);
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "支出申请单" + nowDate + ".doc";
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
     * @author songyang
     * @param request
     * @param response
     * @return
     * @throws NumberFormatException
     * @throws Exception
     */
	@RequestMapping(value="downloadMtourMergePaySheet")
	public ResponseEntity<byte[]> downloadMtourMergePaySheet(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String inputPrama = request.getParameter("mergeParam");
		File file = financeService.createMtourMergePaySheetDownLoadFile(inputPrama,"mtourpaysheet");
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "支出申请单" + nowDate + ".doc";
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
     * 收款确认
     * @param input
     * @author zhaohaiming
     * */
    @ResponseBody
    @RequestMapping(value="payedConfirm")
    public String payedConfirm(BaseInput4MT input){
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			boolean flag = financeService.payedConfirm(input);
			if(flag) {
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("调用确认收款接口出现异常，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用确认收款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
    }
    
    /**
     * 收款确认列表页的详情功能
     * @param input
     * @return String
     * @author zhaohaiming
     * */
    @ResponseBody
    @RequestMapping(value="getPayedConfirmDetail",method=RequestMethod.POST)
    public String getPayedConfirmDetail(BaseInput4MT input){
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			PayedDetail pd  = financeService.getPayedDetail(input);
			if(pd != null) {
				out.setData(pd);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("调用确认收款接口出现异常，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用确认收款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
    }

    /**
     * 付款-提交-查询付款金额
     * @author gaoang
     * @param input
     * @return
     */
    @ResponseBody
    @RequestMapping(value="queryPaymentAmount")
    public String queryPaymentAmount(BaseInput4MT input){
    	BaseOut4MT out = new BaseOut4MT(input);
    	try{
    		String orderId = input.getParamValue("orderUuid");		//订单Id
    		String paymentUuid = input.getParamValue("paymentUuid");//付款Uuid
    		String fundsType = input.getParamValue("fundsType");	//款项类型
    		if(StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(paymentUuid) && StringUtils.isNotBlank(fundsType)){
    			Map<String,Object> resultMap = new HashMap<String,Object>();  			
    			//应付金额
    			List<Map<String,Object>> payableAmountList = financeService.queryPayableAmount(orderId, fundsType);
    			if(CollectionUtils.isNotEmpty(payableAmountList))
    			resultMap.put("payableAmount", payableAmountList);
    			//应付金额-已付金额
    			List<Map<String,Object>> payedAmountList = financeService.queryPayedAmount(paymentUuid);
    			for(Map<String,Object> payableMap : payableAmountList){
    				for(Map<String,Object> payedMap : payedAmountList){
    					if(payableMap.get("currencyUuid").toString().equals(payedMap.get("currencyUuid").toString())){
    						BigDecimal b = new BigDecimal(payableMap.get("amount").toString());
    						BigDecimal b1 = new BigDecimal(payedMap.get("amount").toString());
    						payableMap.put("amount", b.subtract(b1));
    					}
    				}
    			}
    			resultMap.put("payingAmount", payableAmountList);
    			
    			out.setResponseCode4Success();
    			out.setData(resultMap);	
    		}else{
    			out.setResponseCode4Fail();
    			out.putMsgCode(BaseOut4MT.CODE_0001);
    			out.putMsgDescription("参数传递错误！");
    		}
	
    	}catch(Exception e){
    		e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用查询付款金额接口出现异常，请重新操作！");
    	}
    	
    	return toAndCacheJSONString(out);
    }
    

    /**
     * 订单明细
     * @author gao                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
     * 2015年10月23日
     * @param input
     * @return
     */
    @ResponseBody
    @RequestMapping(value="getOrderDetail")
    public String getOrderDetail(BaseInput4MT input){
    	BaseOut4MT out = new BaseOut4MT(input);
    	String channelId = input.getParamValue("channelUuid"); // 渠道商ID
		try{
			List<OrderDetailJsonBean> beanList = financeService.getOrderDetail(channelId);
			out.setResponseCode4Success();
			if(CollectionUtils.isNotEmpty(beanList)){
				out.setData(beanList);
			}else{
				out.setData(null);
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("系统处理异常");
		}
		return toAndCacheJSONString(out);
    }
    
    /**
     * 结算单显示
     * @param input	前台传递的参数
     * @return
     * @author shijun.liu
     */
    @ResponseBody
    @RequestMapping(value="getSettlement",method=RequestMethod.POST)
    public String getSettlement(BaseInput4MT input){
    	BaseOut4MT out = new BaseOut4MT(input);
    	String orderIdStr = input.getParamValue("orderUuid"); // 订单ID，为数字类型
    	try {
    		if(StringUtils.isNotBlank(orderIdStr)){
        		Long orderId = Long.parseLong(orderIdStr);
        		SettlementJsonBean settlementJsonBean = financeService.getSettlementJson(orderId);
        		settlementJsonBean.setOrderUuid(orderIdStr);
        		out.setResponseCode4Success();
        		out.setData(settlementJsonBean);
        	}else{
        		out.setResponseCode4Fail();
    			out.putMsgCode(BaseOut4MT.CODE_0001);
    			out.putMsgDescription("传递的订单ID应为数字，请检查");
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON.toJSONString(out).replaceAll("\\bpNR\\b", "PNR");
    }
    
    /**
     * 结算单下载
     * @return
     * @author shijun.liu
     */
    @ResponseBody
    @RequestMapping(value="downLoadSettlemnt",method=RequestMethod.GET)
    public void downLoadSettlemnt(HttpServletRequest request, HttpServletResponse response){
    	String orderIdStr = request.getParameter("orderUuid"); // 订单ID，为数字类型
    	if(StringUtils.isNotBlank(orderIdStr)){
    		Long orderId = Long.parseLong(orderIdStr);
    		Workbook workBook = financeService.createSettlement(orderId);
    		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
    		ServletUtil.downLoadExcel(response, "机票结算单-"+nowDate + ".xls", workBook);
    	}else{
    		BaseOut4MT out = new BaseOut4MT();
    		out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递的订单ID应为数字，请检查");
			ServletUtil.print(response, JSON.toJSONString(out));
    	}
    }

	/**
     * 打包下载结算单
     * @return
     * @author shijun.liu
     */
    @ResponseBody
    @RequestMapping(value="batchDownLoadSettlemnt",method=RequestMethod.GET)
    public void batchDownLoadSettlemnt(HttpServletRequest request, HttpServletResponse response){
    	String orderIdStr = request.getParameter("orderUuids"); // 订单ID，为数字类型
    	if(StringUtils.isNotBlank(orderIdStr)){
    		File zipFile = financeService.batchZipSettlement(orderIdStr);
    		ServletUtil.downLoadFile(response, zipFile);
    	}else{
    		BaseOut4MT out = new BaseOut4MT();
    		out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("下载结算单出错，请检查");
			ServletUtil.print(response, JSON.toJSONString(out));
    	}
    }
    
    /**
     * 结算单锁定
     * @param input	前台传递的参数
     * @return
     * @author shijun.liu
     * @date 2016.01.08
     */
    @ResponseBody
    @RequestMapping(value="lockSettlement",method=RequestMethod.POST)
    public String lockSettlement(BaseInput4MT input){
    	String orderIdStr = input.getParamValue("orderUuid");
    	BaseOut4MT out = new BaseOut4MT();
    	if(StringUtils.isNotBlank(orderIdStr)){
    		Long orderId = Long.parseLong(orderIdStr);
    		try{
    			financeService.lockSettlement(orderId);
    			out.setResponseCode4Success();
    		}catch(RuntimeException e){
    			out.setResponseCode4Fail();
    			out.putMsgCode(BaseOut4MT.CODE_0001);
    			out.putMsgDescription(e.getMessage());
    			e.printStackTrace();
    		}catch(Exception e){
    			out.setResponseCode4Fail();
    			out.putMsgCode(BaseOut4MT.CODE_0001);
    			out.putMsgDescription("结算单锁定失败");
    			e.printStackTrace();
    		}
    	}else{
    		out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递的订单ID应为数字，请检查");
    	}
    	return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
    }
    
    /**
     * 结算单解锁
     * @param input	前台传递的参数
     * @return
     * @author shijun.liu
     * @date 2016.01.13
     */
    @ResponseBody
    @RequestMapping(value="unlockSettlement",method=RequestMethod.POST)
    public String unlockSettlement(BaseInput4MT input){
    	String orderIdStr = input.getParamValue("orderUuid");
    	BaseOut4MT out = new BaseOut4MT();
    	if(StringUtils.isNotBlank(orderIdStr)){
    		Long orderId = Long.parseLong(orderIdStr);
    		try{
    			financeService.unlockSettlement(orderId);
    			out.setResponseCode4Success();
    		}catch(RuntimeException e){
    			out.setResponseCode4Fail();
    			out.putMsgCode(BaseOut4MT.CODE_0001);
    			out.putMsgDescription(e.getMessage());
    			e.printStackTrace();
    		}catch(Exception e){
    			out.setResponseCode4Fail();
    			out.putMsgCode(BaseOut4MT.CODE_0001);
    			out.putMsgDescription("结算单解锁失败");
    			e.printStackTrace();
    		}
    	}else{
    		out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递的订单ID应为数字，请检查");
    	}
    	return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
    }
    
    /**
     * 付款-提交-确认付款接口
	 * @Title: saveRefundInfo
	 * @return String
	 * @author majiancheng
	 * @date 2015-10-23 下午9:58:17
     */
    @ResponseBody
    @RequestMapping(value="saveRefundInfo",method=RequestMethod.POST)
    public String saveRefundInfo(BaseInput4MT input) {
    	
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			
			boolean flag = financeService.saveRefundInfo(input);
			if(flag) {
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("调用付款-提交-确认付款接口失败，请重新操作！");
			}
			
		} catch(Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("调用付款-提交-确认付款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
    }
    
    /**
     * 付款-款项明细-借款接口
	 * @Title: getLoanRefundInfo
	 * @return String
	 * @author majiancheng
	 * @date 2015-10-24 上午10:16:09
     */
    @ResponseBody
    @RequestMapping(value="getLoanRefundInfo",method=RequestMethod.POST)
    public String getLoanRefundInfo(BaseInput4MT input) {
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			String paymentUuid = input.getParamValue("paymentUuid");
			
			if(StringUtils.isEmpty(paymentUuid)) {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数异常,请重新操作！");
			} else {
				//获取借款的款项明细。
				RefundTypeDetailJsonBean data = financeService.getRefundTypeDetailInfo(paymentUuid, 1);
				out.setData(data);
				out.setResponseCode4Success();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用付款-款项明细-借款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
    }
    
    /**
     * 付款-款项明细-退款接口
	 * @Title: getReturnRefundInfo
	 * @return String
	 * @author majiancheng
	 * @date 2015-10-24 上午10:18:41
     */
    @ResponseBody
    @RequestMapping(value="getReturnRefundInfo",method=RequestMethod.POST)
    public String getReturnRefundInfo(BaseInput4MT input) {
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			String paymentUuid = input.getParamValue("paymentUuid");
			
			if(StringUtils.isEmpty(paymentUuid)) {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数异常,请重新操作！");
			} else {
				//获取退款的款项明细。
				RefundTypeDetailJsonBean data = financeService.getRefundTypeDetailInfo(paymentUuid, 2);
				out.setData(data);
				out.setResponseCode4Success();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用付款-款项明细-退款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
    }
    
    /**
     * 付款-款项明细追加成本接口
	 * @Title: getAddCostRefundInfo
	 * @return String
	 * @author majiancheng
	 * @date 2015-10-24 上午10:19:38
     */
    @ResponseBody
    @RequestMapping(value="getAddCostRefundInfo",method=RequestMethod.POST)
    public String getAddCostRefundInfo(BaseInput4MT input) {
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			String paymentUuid = input.getParamValue("paymentUuid");
			
			if(StringUtils.isEmpty(paymentUuid)) {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数异常,请重新操作！");
			} else {
				//获取追加成本的款项明细。
				RefundTypeDetailJsonBean data = financeService.getRefundTypeDetailInfo(paymentUuid, 3);
				out.setData(data);
				out.setResponseCode4Success();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用付款-款项明细-退款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
    }

//    /**
//     * 收款-提交-查询收款金额接口
//     * @param input
//     * @return String
//     * @author zhaohaiming
//     * **/
//    @ResponseBody
//    @RequestMapping(value="getPayedMoneyInfo")
//	public String getPayedMoneyInfo(BaseInput4MT input){
//		BaseOut4MT out = new BaseOut4MT(input);
//		try{
//			PayedMoneyPojo  money = financeService.getPayedMoneyInfo(input);
//			
//			if(money!= null){
//				out.setData(money);
//				out.setResponseCode4Success();
//			}
//		} catch(Exception e) {
//			out.setResponseCode4Fail();
//			out.putMsgCode(BaseOut4MT.CODE_0001);
//			out.putMsgDescription("传递参数异常.");
//		}
//		return toAndCacheJSONString(out);
//	}
	
    /**
     * 付款-查询金额接口
     * @param input
     * @return String
     * @author zhaohaiming
     * */
    @ResponseBody
    @RequestMapping(value="getPayedMoneyInfoForPay",method=RequestMethod.POST)
    public String getPayedMoneyInfoForPay(BaseInput4MT input){
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			PayedMoneyPojo  money = financeService.getPayedMoneyInfoForPay(input);
			if(money!= null){
				out.setData(money);
				out.setResponseCode4Success();
			}
		} catch(Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常.");
		}
		return toAndCacheJSONString(out);
    }
    
	/**
     * 账龄查询
     * @param input
     * @return String
     * @author shijun.liu
     */
	@ResponseBody
    @RequestMapping(value="getAccountAgeList",method=RequestMethod.POST)
	public String getAccountAgeList(BaseInput4MT input){
		//参数处理
		AccountAgeParam accountAgeParam = optionParams(input);
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			Page page = financeService.getAccountAgeList(accountAgeParam);
			out.setData(page);
			out.setResponseCode4Success();
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常.");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 参数结果处理
	 * 参数格式如下：{"searchParam":{"searchType":"1","searchKey":"KCE2003"},
	 * 			 "filterParam":{"accountReceivableAgeStatusCode":"1",
	 * 							"channels":"123,6554,34",
	 * 							"sales":"123,456"
	 * 						   },
	 *			   "pageParam":{"currentIndex":"3","rowCount":"10"}
	 *			  }
	 * @param input  参数对象
	 * @return
	 * @author shijun.liu
	 */
	private AccountAgeParam optionParams(BaseInput4MT input){
		String receivableAmountRMB = "receivableAmountRMB"; //应收金额
		String receivedAmountRMB = "receivedAmountRMB"; //已收金额
		String arrivedAmountRMB = "arrivedAmountRMB"; //到账金额
		String unreceiveAmountRMB = "unreceiveAmountRMB"; //未收金额:
		AccountAgeParam accountAgeParam = new AccountAgeParam();
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		if(null != jsonObj){
			//输入的搜索参数
			JSONObject searchParam = jsonObj.getJSONObject("searchParam");
			//分页参数
			JSONObject pageParam = jsonObj.getJSONObject("pageParam");
			//过滤参数
			JSONObject filterParam = jsonObj.getJSONObject("filterParam");
			//排序参数
			JSONObject sortInfo = jsonObj.getJSONObject("sortInfo");
			if(null != searchParam){
				//搜索类型
				String searchType = searchParam.getString("searchType");
				//搜索关键字
				String searchKey = searchParam.getString("searchKey");
				accountAgeParam.setSearchType(searchType);
				accountAgeParam.setSearchKey(searchKey);
			}
			if(null != pageParam){
				//当前页
				Integer currentIndex = pageParam.getInteger("currentIndex");
				if(currentIndex.intValue() < 1){	//输入页数小于0时，显示第一页
					currentIndex = 1;
				}
				//每页显示条数
				Integer rowCount = pageParam.getInteger("rowCount");
				accountAgeParam.setPageNow(currentIndex);
				accountAgeParam.setPageCount(rowCount);
			}
			if(null != filterParam){
				String statusCode = filterParam.getString("accountReceivableAgeStatusCode");
				//渠道ID数组  12321,12312,12312
				String channels = filterParam.getString("channels");
				//跟进销售数组 3,4,5
				String sales = filterParam.getString("sales");
				String channelType = filterParam.getString("channelTypeCode");
				accountAgeParam.setAccountAgeStatus(statusCode);
				accountAgeParam.setChannelId(channels);
				accountAgeParam.setSalerId(sales);
				accountAgeParam.setChannelType(channelType);
			}
			if(null != sortInfo){
				String orderBy = " order by ";		//注意不要删除空格
				String orderKey = sortInfo.getString("sortKey");
				String descAsc = sortInfo.getBoolean("dec") ? " DESC " : " ASC ";//注意空格不要删除
				if(receivableAmountRMB.equals(orderKey)){
					orderBy += "totalMoney" + descAsc;
				}else if(receivedAmountRMB.equals(orderKey)){
					orderBy += "payedMoney" + descAsc;
				}else if(arrivedAmountRMB.equals(orderKey)){
					orderBy += "accountedMoney" + descAsc;
				}else if(unreceiveAmountRMB.equals(orderKey)){
					orderBy += "notReceiviedMoney" + descAsc;
				}
				accountAgeParam.setOrderBy(orderBy);
			}
		}
		return accountAgeParam;
	}
	
	/**
	 * 收款撤销接口（财务模块）
	 * @Title: cancelReceive
	 * @return String
	 * @author majiancheng
	 * @date 2015-11-4 下午4:56:37
	 */
	@ResponseBody
    @RequestMapping(value="cancelReceive")
	public String cancelReceive(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			boolean flag = financeService.cancelReceive(input);
			if(flag) {
				out.setResponseCode4Success();
				out.putMsgDescription("撤销成功");
			} else {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数异常,请重新操作！");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用收款撤销接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 其他收款详情
	 * @param input
	 * @return
	 * @author wangxv
	 */
	@ResponseBody
	@RequestMapping(value = "getCostReceiptDetail")
	public String getOtherReceiptDetail(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		//获得传递的参数值
		Integer costRecordId = jsonObj.getInteger("receiveUuid");
		if(costRecordId!=null){
			Map<String, Object> result = financeService.getOtherReceiptDetail(costRecordId.toString());
			if(result!=null){
				if(result.get("docIds")!=null){
					String ids = result.remove("docIds").toString();
					if(StringUtils.isNotBlank(ids)){
						String docIds =ids.substring(0, ids.lastIndexOf(","));
						if(StringUtils.isNotBlank(docIds)){
							List<Map<String,String>> docDetail = financeService.getOrderPayDocList(docIds);
							result.put("attachments", docDetail);
						}
					}
				}
			}
			out.setData(result);
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
 		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
 	}
	
	/**
	 * 订单列表-二级列表-付款-确认付款
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
    @ResponseBody
    @RequestMapping(value="batchSaveRefundInfo",method=RequestMethod.POST)
    public String batchSaveRefundInfo(BaseInput4MT input) {
    	
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			
			boolean flag = financeService.batchSaveRefundInfo(input);
			if(flag) {
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("订单列表-二级列表-付款-确认付款接口失败，请重新操作！");
			}
			
		} catch(Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("订单列表-二级列表-付款-确认付款接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
    }
    
    /**
     * 订单列表-二级列表-付款-查询付款记录
     * @Description: 
     * @param @param input
     * @param @return   
     * @return String  
     * @throws
     * @author majiancheng
     * @date 2016-1-21
     */
    @ResponseBody
    @RequestMapping(value="getBatchRefundInfo",method=RequestMethod.POST)
    public String getBatchRefundInfo(BaseInput4MT input) {
    	BaseOut4MT out = new BaseOut4MT(input);
		try{
			
			RefundRecordsJsonBean data = financeService.getBatchRefundInfo(input);
			if(data != null) {
				out.setData(data);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("订单列表-二级列表-付款-查询付款记录接口失败，请重新操作！");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("订单列表-二级列表-付款-查询付款记录接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
    }
    
    /**
     * 订单列表-二级列表-付款记录-撤销
     * @Description: 
     * @param @param input
     * @param @return   
     * @return String  
     * @throws
     * @author majiancheng
     * @date 2016-1-21
     */
	@ResponseBody
	@RequestMapping(value = "batchUndoRefundPayInfo", method = RequestMethod.POST)
	public String batchUndoRefundPayInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			boolean flag = financeService.batchUndoRefundPayInfo(input);
			if(flag) {
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Fail();
				out.putMsgDescription("调用订单列表-二级列表-付款记录-撤销接口失败，请重新操作！");
			} 
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用订单列表-二级列表-付款记录-撤销接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	/***
	 * 展开子列表查询接口
	 * @param input
	 * @return
	 * @author zhangchao
	 */
	@ResponseBody
	@RequestMapping(value="getPayChildrenList")
	public String getPayChildrenList(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		List<Map<String,Object>> list = financeService.getPayChildrenList(input.getParamValue("orderUuid"));
		//System.out.println(result);
		out.setData(list);
		out.setResponseCode4Success();
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 订单列表(付款-订单列表)
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	@ResponseBody
	@RequestMapping(value = "getRefundOrderPage", method = RequestMethod.POST)
	public String getRefundOrderPage(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			MtourOrderJsonBean data = financeService.getRefundOrderPage(input);
			if(data != null) {
				out.setData(data);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Fail();
				out.putMsgDescription("调用订单列表接口失败，请重新操作！");
			} 
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用订单列表接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 初始化所有美途订单的付款状态
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	@RequestMapping(value = "initOrderRefundFlag")
	public String initOrderRefundFlag(BaseInput4MT input, HttpServletRequest request) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			boolean flag = true;
			
			String orderId = request.getParameter("orderId");
			if(StringUtils.isNotEmpty(orderId)) {
				flag = financeService.updateOrderRefundFlag(Long.parseLong(orderId));
			} else {
				flag = financeService.initOrderRefundFlag();
			}
			
			if(flag) {
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Fail();
				out.putMsgDescription("初始化所有美途订单的付款状态接口失败，请重新操作！");
			} 
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("初始化所有美途订单的付款状态出现异常，请重新操作！");
		}
		return "redirect:" + Global.getAdminPath();
	}
	/**
	 * 付款记录，详情查询接口
	 * @param input
	 * @return
	 * @author zhangchao
	 * @time 2016/1/28
	 */
	@ResponseBody
	@RequestMapping(value="queryPayJDetail")
	public String queryPayJDetail(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		PayDetailJsonBean detail = financeService.queryPayJDetail(input);
		out.setData(detail);
		out.setResponseCode4Success();
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 订单列表-二级列表-付款记录
	 * @Description: 获取付款对象的付款记录
	 * @param @param input
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-29
	 */
	@ResponseBody
	@RequestMapping(value = "getRefundRecordsInfo", method = RequestMethod.POST)
	public String getRefundRecordsInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			List<SecondRefundRecordsJsonBean> data = financeService.getRefundRecordsInfo(input);
			if(data != null) {
				out.setData(data);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Fail();
				out.putMsgDescription("调用订单列表接口失败，请重新操作！");
			} 
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用订单列表接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}

	/**
	 * 财务中心-收款-订单列表
	 * @param input
	 * @return String
	 * @author shijun.liu
	 * @date 2016.03.09
	 */
	@ResponseBody
	@RequestMapping(value="receiveOrderList", method=RequestMethod.POST)
	public String receiveOrderList(BaseInput4MT input){
		//参数处理
		ReceiveOrderListParam params = optionReceiveOrderListParam(input);
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			Page page = financeService.receiveOrderList(params);
			out.setData(page);
			out.setResponseCode4Success();
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常.");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}

	/**
	 *  {
	 *		searchParam:{
	 *			searchType:'搜索类型',
	 *			searchKey:'搜索输入的文本'
	 *		},
	 *		filterParam:{
	 *		    orderDateTime:'下单日期',
	 *			departureDate:'出团日期'
	 *			ordererId:'下单人Id',
	 *			orderReceiveStatusCodeStatusCode:'订单收款状态'//0-待收款,1-部分定金，2-已收定金,3-已收全款
	 *		},
	 *		sortInfo:{
	 *			sortKey: '排序项',//下单时间:orderDateTime,出团日期:departureDate
	 *			dec: '是否倒序'
	 *		},
	 *		pageParam:{//分页搜索信息
	 *			currentIndex:'当前页码',
	 *			rowCount:'每页总行数'
	 *		}
	 *	}
	 * 处理参数转换为对象
	 * @param input
	 * @return
	 */
	private ReceiveOrderListParam optionReceiveOrderListParam(BaseInput4MT input){
		ReceiveOrderListParam param = new ReceiveOrderListParam();
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		if(null != jsonObj){
			//输入的搜索参数
			JSONObject searchParam = jsonObj.getJSONObject("searchParam");
			//分页参数
			JSONObject pageParam = jsonObj.getJSONObject("pageParam");
			//过滤参数
			JSONObject filterParam = jsonObj.getJSONObject("filterParam");
			//排序参数
			JSONObject sortInfo = jsonObj.getJSONObject("sortInfo");
			if(null != searchParam){
				//搜索类型
				String searchType = searchParam.getString("searchType");
				//搜索关键字
				String searchKey = searchParam.getString("searchKey");
				param.setSearchType(searchType);
				param.setSearchKey(searchKey);
			}
			if(null != pageParam){
				//当前页
				Integer currentIndex = pageParam.getInteger("currentIndex");
				if(currentIndex.intValue() < 1){	//输入页数小于0时，显示第一页
					currentIndex = 1;
				}
				//每页显示条数
				Integer rowCount = pageParam.getInteger("rowCount");
				param.setPageNow(currentIndex);
				param.setPageCount(rowCount);
			}
			if(null != filterParam){
				//下单时间
				String orderDateTime = filterParam.getString("orderDateTime");
				//出团日期
				String departureDate = filterParam.getString("departureDate");
				//下单人
				String orderedId = filterParam.getString("ordererId");
				//订单收款状态
				String receiveStatus = filterParam.getString("orderReceiveStatusCodeStatusCode");
				param.setOrderDateTime(orderDateTime);
				param.setDepartureDate(departureDate);
				param.setOrderedId(orderedId);
				param.setReceiveStatus(receiveStatus);
			}
			if(null != sortInfo){
				String orderBy = " order by ";		//注意不要删除空格
				String orderKey = sortInfo.getString("sortKey");
				String descAsc = sortInfo.getBoolean("dec") ? " DESC " : " ASC ";//注意空格不要删除
				param.setOrderBy(orderBy + orderKey + descAsc);
			}
		}
		return param;
	}

	/**
	 * 收款-订单列表(展开)-二级子列表
	 * @Description:点击展开操作，获取收款列表的二级子列表
	 * @param input
	 * @return String
	 * @throws
	 * @author wangyang
	 * @date 2016.3.8
	 * */
	@ResponseBody
	@RequestMapping(value = "getReceiveOrderSubList")
	public String getReceiveOrderSubList(BaseInput4MT input){
		
		JSONObject param = JSON.parseObject(input.getParam());
		String orderUuid = param.getString("orderUuid");
		BaseOut4MT out = new BaseOut4MT(input);
		
		try{
			List<SecondReceiveListJsonBean> bean = financeService.getReceiveOrderSubList(orderUuid);
			if(bean != null) {
				out.setData(bean);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("传参出现异常，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用确认收款(详情)接口出现异常，请重新操作！");
		}
		//System.out.println(toAndCacheJSONString(out));
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
		
	}
	
	/**
	 * 订金统计-用于美途国际订金统计操作
	 * @author wangyang
	 * @date 2016.6.20
	 * */
	@ResponseBody
	@RequestMapping(value = "frontMoneyStat")
	public String frontMoneyStat(BaseInput4MT input){
		
		String statDateBegin = input.getParamValue("startDate"); // 开始日期
		String statDateEnd = input.getParamValue("endDate"); // 结束日期
		
		BaseOut4MT out = new BaseOut4MT(input);
		try {
			List<FrontMoneyStatJsonBean> bean = financeService.getFrontMoneyStat(statDateBegin, statDateEnd);
			if(bean != null){
				out.setData(bean);
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传参出现异常，请重新操作！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用确认收款(详情)接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 订金统计-用于美途国际订金统计Excel下载操作
	 * @author wangyang
	 * @throws IOException 
	 * @date 2016.6.20
	 * */
	@RequestMapping(value = "downloadFrontMoneyStatExcel")
	public void downloadFrontMoneyStatExcel(HttpServletResponse response, HttpServletRequest request) throws IOException{

		//起止时间
		String statDateBegin = request.getParameter("startDate");
		String statDateEnd = request.getParameter("endDate");
		//获取订金统计数据
		List<FrontMoneyStatJsonBean> beanlist = financeService.getFrontMoneyStat(statDateBegin, statDateEnd);
		//工作簿名称及表头
		String title = DateUtils.getYear() + "年" + DateUtils.getMonth() + "月";
		String[] headers = new String[]{"销售", "团号", "机票全款", "定金", "未收定金", "未收全款"};
		//Excel文件名
		String fileName = "定金统计表（未结账）.xls";
		String excelTitle = "美途国旅" + statDateBegin + "至" + statDateEnd + "机票定金统计表（未结账）";
		buildExcelForFrontMoneyStat(beanlist, title, headers, fileName, response, excelTitle);
		
	}

	/**
	 * 根据设定参数，生成excel
	 * @param beanlist excel数据内容
	 * @param title 工作簿名称
	 * @param headers excel数据标题
	 * @param fileName 文件名称
	 * @author wangyang
	 * @date 2016.6.22
	 * */
	private void buildExcelForFrontMoneyStat(
			List<FrontMoneyStatJsonBean> beanlist, String title,
			String[] headers, String fileName, HttpServletResponse response, 
			String excelTitle) throws IOException {
		
		
		response.setContentType("octets/stream");
		response.setHeader("Content-Disposition", "attachment; filename=" 
				+ new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
		
		//声明一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		//生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		//设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(20);
		//生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//标题样式
		HSSFCellStyle styleTitle = workbook.createCellStyle();
		styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont fontTitle = workbook.createFont();
		fontTitle.setFontHeightInPoints((short) 20);
		styleTitle.setFont(fontTitle);
		//单位：元 样式
		HSSFCellStyle styleUnit = workbook.createCellStyle();
		styleUnit.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleUnit.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		
		//excel表标题
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(styleTitle);
		cell.setCellValue(excelTitle);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, headers.length - 1));
		
		//单位：元
		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellStyle(styleUnit);
		cell.setCellValue("单位：元");
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, headers.length - 1));
		
		//产生表格标题行
		int columnIndex = 0;
		row = sheet.createRow(3);
		for(int i = 0; i < headers.length; i++){
			cell = row.createCell(columnIndex);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
			columnIndex++;
		}
		
		int rowIndex = 3;
		//数据填充
		for(int i = 0; i < beanlist.size(); i++){
			
			int rowItem = rowIndex;
			columnIndex = 0;
			
			FrontMoneyStatJsonBean fmsjb = beanlist.get(i);
			rowIndex += fmsjb.getOrders().size();
			
			row = sheet.createRow(rowItem + 1);
			
			//销售-合并多行
			cell = row.createCell(columnIndex);
			cell.setCellValue(fmsjb.getSalerName());
			cell.setCellStyle(style);
			sheet.addMergedRegion(new CellRangeAddress(rowItem + 1, rowIndex, 0, 0));
			//columnIndex++;
			
			for(int k = 0; k < fmsjb.getOrders().size(); k++){
				
				FrontMoneyStatData fmsd = fmsjb.getOrders().get(k);
				if(k != 0){
					row = sheet.createRow(rowItem + 1);
				}
								
				columnIndex = 1;
				//团号
				cell = row.createCell(columnIndex);
				cell.setCellValue(fmsd.getOrderNum());
				cell.setCellStyle(style);
				columnIndex++;
				
				//机票全款
				cell = row.createCell(columnIndex);
				cell.setCellValue("￥" + fmsd.getTotalMoney());
				cell.setCellStyle(style);
				columnIndex++;
				
				//定金
				cell = row.createCell(columnIndex);
				cell.setCellValue("￥" + fmsd.getFrontMoney());
				cell.setCellStyle(style);
				columnIndex++;
				
				//未收定金
				cell = row.createCell(columnIndex);
				cell.setCellValue("￥" + fmsd.getNotAccountFrontMoney());
				cell.setCellStyle(style);
				columnIndex++;
				
				//未收全款
				cell = row.createCell(columnIndex);
				cell.setCellValue("￥" + fmsd.getNotAccountedTotalMoney());
				cell.setCellStyle(style);
				columnIndex++;
				
				rowItem++;
			}
		}
		//输出Excel
		OutputStream os = response.getOutputStream();
		workbook.write(os);
		os.close();		
	}

	/**
	 * 获取营业收入json数据 yudong.xu 2016.6.22
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getOperatingRevenue")
	public String getOperatingRevenue(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		String startDate = input.getParamValue("startDate"); // 开始日期
		String endDate = input.getParamValue("endDate"); // 结束日期
		try{
			Map<Integer,List<OperatingRevenueData>> result = financeService.getOperatingRevenueByDate(startDate,endDate,true);
			List<OperatingRevenueJsonBean> beans = CommonUtils.convertDataForOR(result);
			out.setResponseCode4Success();
			if(CollectionUtils.isNotEmpty(beans)){
				out.setData(beans);
			}else{
				out.setData(null);
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("系统处理异常");
		}
		return toAndCacheJSONString(out);
	}

	//指向营业收入统计打印的页面。
	@RequestMapping(value="showFinanceIncome")
	public String showFinanceIncome(){
		return "modules/mtour/showFinanceIncome";
	}

	/**
	 * 下载营业收入统计信息的Excel表。传入开始日期和结束日期，如 2016-06-23。 yudong.xu 2016.6.20
     */
	@RequestMapping(value = "downloadOperatingRevenue")
	public void downloadOperatingRevenue(HttpServletRequest request,HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Map<Integer,List<OperatingRevenueData>> result = financeService.getOperatingRevenueByDate(startDate,endDate,false);
		if (StringUtils.isBlank(startDate))
			startDate = "从开始";
		if (StringUtils.isBlank(endDate)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			endDate = sdf.format(new Date());
		}
		StringBuilder fileName = new StringBuilder();
		fileName.append("美途国旅").append(startDate).append("至").append(endDate).append("营业收入统计表");
		Workbook wb = ExcelUtils.getOperatingRevenueWB(result,fileName.toString());
		fileName.append(".xls");
		ServletUtil.downLoadExcel(response,fileName.toString(),wb);
	}
	
	/**
	 * 批量下载收入单
	 * @param input
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="batchDownloadIncomeSheet")
	public void batchDownloadIncomeSheet(BaseInput4MT input, HttpServletRequest request, HttpServletResponse response){
		JSONObject jobj = JSONObject.parseObject(request.getParameter("params"));
		if(jobj.getJSONArray("income")!=null && jobj.getJSONArray("income").size()!=0){
			JSONArray array1 = jobj.getJSONArray("income");
			List<Map<String,Object>> incomes=(List)array1;
			String csIds="";
			String oterIds="";
			List<File> listFile=new ArrayList<File>();
			String prefix = FileUtils.getTempDir();
			Integer flag=1;
			for(Map<String,Object> income:incomes ){
			if(income.get("paymentObj")!=null &&((List) income.get("paymentObj")).size()!=0){
				List<Map<String,Object>> pamentObj =(List<Map<String,Object>>) income.get("paymentObj");
				for(Map<String,Object> mapObj:pamentObj){
						if(mapObj.get("payPriceType").toString().equals("1") || mapObj.get("payPriceType").toString().equals("2")
								|| mapObj.get("payPriceType").toString().equals("3") || mapObj.get("payPriceType").toString().equals("4")){
							if(csIds.equals("")){
								csIds=mapObj.get("payObjectUuid").toString();
							}else{
								csIds=mapObj.get("payObjectUuid").toString()+","+csIds;
							}
						}else{
							if(oterIds.equals("")){
								oterIds=mapObj.get("payObjectUuid").toString();
							}else{
								oterIds=mapObj.get("payObjectUuid").toString()+","+oterIds;
							}
						}
				}
				List<Map<String,Object>> list = financeService.getBatchIncomeInfoByInput(income.get("orderId").toString(), csIds, oterIds);
				int i=1;
				for(Map<String,Object> data:list){
			    	try {
			    		//编号
						data.put("printNo","000000"+flag+"（合）");
						flag++;
			    		File file = financeService.createIncomeSheetDownloadFile(data);
			    		boolean ContainFileName=false;
			    		String fileName="";
			    		fileName = data.get("groupNo") +" "+data.get("fromInfo").toString()+".doc";
			    		if(listFile.size()!=0){
			    			for(File f:listFile){
				    			if(f.getName().equals(data.get("groupNo") +" "+data.get("fromInfo").toString()+ ".doc")){
				    				ContainFileName=true;
				    			}
				    		}
			    			if(ContainFileName){
			    				i++;
			    				fileName = data.get("groupNo") +" "+data.get("fromInfo").toString()+" "+i+".doc";
			    			}
			    		}
			    		File fileTo=new File(FreeMarkerUtil.class.getResource("/").getPath()+"word/"+fileName);
			    		file.renameTo(fileTo);
						listFile.add(fileTo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			}	
			if(null !=listFile && listFile.size()!=0){
			 String nowDate = DateUtils.formatCustomDate(new Date(), "yyyyMMddHHmmss");
				String fileN="收入单"+nowDate+".zip";
				try{
					File zipFile = new File(prefix, fileN);
					ZipUtils.zipFileList(listFile, zipFile);
					ServletUtil.downLoadFile(response, zipFile);
					FileUtils.timingDeleteFile(10,listFile);
					FileUtils.timingDeleteFile(10,zipFile);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}		
	}

	/**
	 * 批量下载支出单。 yudong.xu 2016.7.6
	 */
	@RequestMapping(value ="batchDownloadPaySheet")
	public void batchDownloadPaySheet(HttpServletRequest request,HttpServletResponse response){
		String params = request.getParameter("params");
		try {
			//Tomcat 已进行了转码，此处不需要再次转码
			//params = new String(params.getBytes("ISO-8859-1"),"utf-8");
			List<File> fileList = financeService.getPaySheetFileList(params);
			String zipName = "支出单" + DateUtils.formatCustomDate(new Date(),"yyyyMMddHHmmss") + ".zip";
			String prefix = FileUtils.getTempDir();
			File zipFile = new File(prefix,zipName);
			ZipUtils.zipFileList(fileList,zipFile);
			ServletUtil.downLoadFile(response,zipFile);
			FileUtils.timingDeleteFile(10,fileList);
			FileUtils.timingDeleteFile(10,zipFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
