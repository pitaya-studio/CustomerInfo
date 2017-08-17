package com.trekiz.admin.modules.statistics.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.statistics.product.bean.ProductCount;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.finance.entity.ReturnDifference;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.statistics.order.bean.OrderDetail;
import com.trekiz.admin.modules.statistics.product.bean.ProductInfo;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.CommonUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

public class ExcelUtils {
	private static OrderPayService orderPayService = SpringContextHolder.getBean(OrderPayService.class);
	private static ReturnDifferenceService returnDifferenceService = SpringContextHolder.getBean(ReturnDifferenceService.class);
	private static CurrencyService currencyService = SpringContextHolder.getBean(CurrencyService.class);
	/**
     * 根据订单数据信息生成相应的Excel文件
     * @param orderDetailList    订单的数据信息
     * @return
     * @author shijun.liu
     */
    public static Workbook createOrderExcel(List<OrderDetail> orderDetailList){
    	String fontStyle = "Courier New";
    	//表头
    	String[] heads = {"供应商", "预定渠道", "订单号", "下单人",
    			"预定时间", "产品名称", "团号", "人数",
    			"订单状态", "订单总额", "已付金额", "到账金额",
    			"业务类型", "销售姓名", "出发地", "区域",
    			"国家", "目的地","渠道类型"};
    	//创建Excel
    	HSSFWorkbook workBook = new HSSFWorkbook();
    	
    	//表头字体和样式
    	HSSFFont headFont = workBook.createFont();
    	HSSFCellStyle headStyle = workBook.createCellStyle();
    	headFont.setFontHeightInPoints((short)10);
    	headFont.setFontName(fontStyle);
    	headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	headStyle.setFont(headFont);
    	headStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	headStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	headStyle.setBorderTop(CellStyle.BORDER_THIN);
    	headStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	//订单数据字体和样式
    	HSSFFont dataFont = workBook.createFont();
    	HSSFCellStyle dataStyle = workBook.createCellStyle();
    	dataFont.setFontHeightInPoints((short)10);
    	dataFont.setFontName(fontStyle);
    	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	dataStyle.setFont(dataFont);
    	dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	dataStyle.setBorderTop(CellStyle.BORDER_THIN);
    	dataStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	HSSFSheet sheet = workBook.createSheet();
    	    	
    	HSSFRow headRow = sheet.createRow(0);
    	headRow.setHeightInPoints((short)24);
    	for (int i = 0; i<heads.length; i++) {
    		HSSFCell headCell = headRow.createCell(i);
    		headCell.setCellValue(heads[i]);
        	headCell.setCellStyle(headStyle);
        	sheet.setColumnWidth(i, 5800);
		}
    	for (int i = 0; i < orderDetailList.size(); i++) {
    		HSSFRow dataRow = sheet.createRow(i+1);
    		dataRow.setHeightInPoints((short)20);
    		createCell(dataRow, 0, orderDetailList.get(i).getSupplierName(),dataStyle);
    		createCell(dataRow, 1, orderDetailList.get(i).getAgentName(),dataStyle);
    		createCell(dataRow, 2, orderDetailList.get(i).getOrderNum(),dataStyle);
    		createCell(dataRow, 3, orderDetailList.get(i).getOrderPersonName(),dataStyle);
    		createCell(dataRow, 4, orderDetailList.get(i).getOrderTime(),dataStyle);
    		if(StringUtils.isNotBlank(orderDetailList.get(i).getAirticketName())){
    			createCell(dataRow, 5, orderDetailList.get(i).getAirticketName(),dataStyle);
    		}else{
    			createCell(dataRow, 5, orderDetailList.get(i).getProductName(),dataStyle);
    		}
    		createCell(dataRow, 6, orderDetailList.get(i).getGroupCode(),dataStyle);
    		createCell(dataRow, 7, orderDetailList.get(i).getOrderPersonCount(),dataStyle);
    		createCell(dataRow, 8, orderDetailList.get(i).getOrderStatus(),dataStyle);
    		createCell(dataRow, 9, orderDetailList.get(i).getTotalMoney(),dataStyle);
    		createCell(dataRow, 10, orderDetailList.get(i).getPayedMoney(),dataStyle);
    		createCell(dataRow, 11, orderDetailList.get(i).getAccountedMoney(),dataStyle);
    		createCell(dataRow, 12, orderDetailList.get(i).getProductType(),dataStyle);
    		createCell(dataRow, 13, orderDetailList.get(i).getSalerName(),dataStyle);
    		createCell(dataRow, 14, orderDetailList.get(i).getDepartureAddress(),dataStyle);
    		createCell(dataRow, 15, orderDetailList.get(i).getArea(),dataStyle);
    		createCell(dataRow, 16, orderDetailList.get(i).getCountry(),dataStyle);
    		createCell(dataRow, 17, orderDetailList.get(i).getArriviedAddress(),dataStyle);
    		createCell(dataRow, 18, orderDetailList.get(i).getAgentType(),dataStyle);
		}
    	return workBook;
    }
    
    /**
     * 创建单元格
     * @param row          row对象
     * @param index        列索引
     * @param value        单元格值
     * @param cellStyle    单元格样式
     * @author shijun.liu
     */
    private static void createCell(HSSFRow row, int index, String value, CellStyle cellStyle){
    	HSSFCell cell = row.createCell(index);
    	cell.setCellValue(value);
    	cell.setCellStyle(cellStyle);
    }
    
    /**
     * 创建单元格
     * @param row          row对象
     * @param index        列索引
     * @param value        单元格值
     * @param cellStyle    单元格样式
     * @author ZHAOHAIMING
     */
    private static void createCell(XSSFRow row, int index, String value, CellStyle cellStyle){
    	XSSFCell cell = row.createCell(index);
    	cell.setCellValue(value);
    	cell.setCellStyle(cellStyle);
    }
    
    /**
     * 根据产品数据信息生成相应的Excel文件
     * @param orderDetailList    订单的数据信息
     * @return
     * @author shijun.liu
     */
    public static Workbook createProductExcel(List<ProductInfo> productInfoList){
    	String fontStyle = "Courier New";
    	//表头
    	String[] heads = {"公司","产品名称","计调","出发城市",
    			"到达城市","签证","团号","出团日期","截团日期","签证国家","签证类型",
    			"签证领区","成本价格","应收价格","发布时间","资料截止日期","成人同行价",
    			"儿童同行价","特殊人群同行价","成人直客价","儿童直客价","特殊人群直客价",
    			"定金","单房差","预收","余位","切位","产品编号","机票类型","税费","定金时限",
    			"取消时限","出票日期","航空公司","仓位","出发机场","到达机场","起飞时间","到达时间",
    			"订单类型","产品ID","版本"};
    	//创建Excel
    	HSSFWorkbook workBook = new HSSFWorkbook();
    	
    	//表头字体和样式
    	HSSFFont headFont = workBook.createFont();
    	HSSFCellStyle headStyle = workBook.createCellStyle();
    	headFont.setFontHeightInPoints((short)10);
    	headFont.setFontName(fontStyle);
    	headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	headStyle.setFont(headFont);
    	headStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	headStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	headStyle.setBorderTop(CellStyle.BORDER_THIN);
    	headStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	//订单数据字体和样式
    	HSSFFont dataFont = workBook.createFont();
    	HSSFCellStyle dataStyle = workBook.createCellStyle();
    	dataFont.setFontHeightInPoints((short)10);
    	dataFont.setFontName(fontStyle);
    	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	dataStyle.setFont(dataFont);
    	dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	dataStyle.setBorderTop(CellStyle.BORDER_THIN);
    	dataStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	HSSFSheet sheet = workBook.createSheet();
    	HSSFRow headRow = sheet.createRow(0);
    	headRow.setHeightInPoints((short)24);
    	for (int i = 0; i<heads.length; i++) {
    		HSSFCell headCell = headRow.createCell(i);
    		headCell.setCellValue(heads[i]);
    		if(i==0){
    			headStyle.setHidden(true);
    		}
        	headCell.setCellStyle(headStyle);
        	sheet.setColumnWidth(i, 5800);
		}
    	for (int i = 0; i < productInfoList.size(); i++) {
    		HSSFRow dataRow = sheet.createRow(i+1);
    		dataRow.setHeightInPoints((short)20);
    		createCell(dataRow, 0, productInfoList.get(i).getCompanyName(),dataStyle);
    		createCell(dataRow, 1, productInfoList.get(i).getProductName(),dataStyle);
    		createCell(dataRow, 2, productInfoList.get(i).getOperator(),dataStyle);
    		createCell(dataRow, 3, productInfoList.get(i).getDepartureCity(),dataStyle);
    		createCell(dataRow, 4, productInfoList.get(i).getArrivedCity(),dataStyle);
    		createCell(dataRow, 5, productInfoList.get(i).getVisa(),dataStyle);
    		createCell(dataRow, 6, productInfoList.get(i).getGroupCode(),dataStyle);
    		createCell(dataRow, 7, productInfoList.get(i).getGroupOpenDate(),dataStyle);
    		createCell(dataRow, 8, productInfoList.get(i).getGroupCloseDate(),dataStyle);
    		createCell(dataRow, 9, productInfoList.get(i).getVisaCountry(),dataStyle);
    		createCell(dataRow, 10, productInfoList.get(i).getVisaType(),dataStyle);
    		createCell(dataRow, 11, productInfoList.get(i).getVisaArea(),dataStyle);
    		createCell(dataRow, 12, productInfoList.get(i).getCostMoney(),dataStyle);
    		createCell(dataRow, 13, productInfoList.get(i).getReceiveMoney(),dataStyle);
    		createCell(dataRow, 14, productInfoList.get(i).getCreateDate(),dataStyle);
    		createCell(dataRow, 15, productInfoList.get(i).getInfoDate(),dataStyle);
    		createCell(dataRow, 16, productInfoList.get(i).getSettlementAdultPrice(),dataStyle);
    		createCell(dataRow, 17, productInfoList.get(i).getSettlementChildPrice(),dataStyle);
    		createCell(dataRow, 18, productInfoList.get(i).getSettlementSpecialPrice(),dataStyle);
    		createCell(dataRow, 19, productInfoList.get(i).getSuggestAdultPrice(),dataStyle);
    		createCell(dataRow, 20, productInfoList.get(i).getSuggestChildPrice(),dataStyle);
    		createCell(dataRow, 21, productInfoList.get(i).getSuggestSpecialPrice(),dataStyle);
    		createCell(dataRow, 22, productInfoList.get(i).getPayDeposit(),dataStyle);
    		createCell(dataRow, 23, productInfoList.get(i).getSingleDiff(),dataStyle);
    		createCell(dataRow, 24, productInfoList.get(i).getPlanPosition(),dataStyle);
    		createCell(dataRow, 25, productInfoList.get(i).getFreePosition(),dataStyle);
    		createCell(dataRow, 26, productInfoList.get(i).getPayReservePosition(),dataStyle);
    		createCell(dataRow, 27, productInfoList.get(i).getProductCode(),dataStyle);
    		createCell(dataRow, 28, productInfoList.get(i).getAirType(),dataStyle);
    		createCell(dataRow, 29, productInfoList.get(i).getTaxamt(),dataStyle);
    		createCell(dataRow, 30, productInfoList.get(i).getDepositTime(),dataStyle);
    		createCell(dataRow, 31, productInfoList.get(i).getCancelTimeLimit(),dataStyle);
    		createCell(dataRow, 32, productInfoList.get(i).getOutTicketTime(),dataStyle);
    		createCell(dataRow, 33, productInfoList.get(i).getAirCompany(),dataStyle);
    		createCell(dataRow, 34, productInfoList.get(i).getAirSpace(),dataStyle);
    		createCell(dataRow, 35, productInfoList.get(i).getLeaveAirport(),dataStyle);
    		createCell(dataRow, 36, productInfoList.get(i).getArrivedAirport(),dataStyle);
    		createCell(dataRow, 37, productInfoList.get(i).getStartTime(),dataStyle);
    		createCell(dataRow, 38, productInfoList.get(i).getArrivalTime(),dataStyle);
    		String orderType = productInfoList.get(i).getOrderType();
    		String orderTypeName = OrderCommonUtil.getChineseOrderType(orderType);
    		createCell(dataRow, 39, orderTypeName,dataStyle);
    		createCell(dataRow, 40, productInfoList.get(i).getProductId(),dataStyle);
    		createCell(dataRow, 41, productInfoList.get(i).getVersion(),dataStyle);
		}
    	return workBook;
    }
    
    /**
     * 应付账款下载EXCEL
     * @author zhaohaiming
     * */
    public static Workbook createPayListExcel(List<Map<Object,Object>> list){
    	String[] title={"序号","部门","计调","团号","收款单位","应付总额","已付总额","应付余额","应付账期","未到期应付"};
    	HSSFWorkbook workbook = new HSSFWorkbook();
		
		HSSFFont headFont = workbook.createFont();
		HSSFCellStyle headStyle = workbook.createCellStyle();
    	headFont.setFontHeightInPoints((short)10);
    	headFont.setFontName("Courier New");
    	headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	headStyle.setFont(headFont);
    	headStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	headStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	headStyle.setBorderTop(CellStyle.BORDER_THIN);
    	headStyle.setBorderRight(CellStyle.BORDER_THIN);
		
    	//数据字体和样式
    	Font dataFont = workbook.createFont();
    	CellStyle dataStyle = workbook.createCellStyle();
    	dataFont.setFontHeightInPoints((short)10);
    	dataFont.setFontName("Courier New");
    	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	dataStyle.setFont(dataFont);
    	dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	dataStyle.setBorderTop(CellStyle.BORDER_THIN);
    	dataStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	HSSFSheet sheet = workbook.createSheet();
    	
    	Row headRow = sheet.createRow(0);
    	headRow.setHeightInPoints((short)24);
    	for (int i = 0; i<10; i++) {
    		Cell headCell = headRow.createCell(i);
    		headCell.setCellValue(title[i]);
        	headCell.setCellStyle(headStyle);
        	sheet.setColumnWidth(i, 5800);
		}
    	
    	for (int i = 0; i < list.size(); i++) {
    		String total = "";  //应付总额
        	String payed = "";  //已付总额
        	String sub = "";    //应付-已付
        	BigDecimal t = null; //应付总额
        	BigDecimal p = null;//已付总额
        	HSSFRow dataRow = sheet.createRow(i+1);
    		dataRow.setHeightInPoints((short)20);
    		createCell(dataRow, 0, ""+(i+1),dataStyle);
    		createCell(dataRow, 1, list.get(i).get("deptId") != null?CommonUtils.getDeptNameById(Long.valueOf(list.get(i).get("deptId").toString())):"",dataStyle);
    		createCell(dataRow, 2, list.get(i).get("jd") !=null?list.get(i).get("jd").toString():"",dataStyle);
    		createCell(dataRow, 3, list.get(i).get("groupCode")!=null?list.get(i).get("groupCode").toString():"",dataStyle);
    		createCell(dataRow, 4, list.get(i).get("payee")!=null?list.get(i).get("payee").toString():"",dataStyle);
    		if( list.get(i).get("total") != null){
    			total = list.get(i).get("total").toString();
    			t = new BigDecimal(total);
    		}
    		if(list.get(i).get("payed") != null){
    			payed = list.get(i).get("payed").toString();
    			p = new BigDecimal(payed);
    		}
    		if(t != null){
    			if(p != null){
    				sub = t.subtract(p).toString();
    			}else{
    				sub = t.toString();
    			}
    		}else{
    			if(p!= null){
    				sub = p.multiply(new BigDecimal("-1")).toString();
    			}
    		}
    		createCell(dataRow, 5, StringUtils.isNotBlank(total)?"¥"+MoneyNumberFormat.getThousandsMoney(Double.valueOf(total), MoneyNumberFormat.THOUSANDST_POINT_TWO):"¥0.00",dataStyle);
            createCell(dataRow, 6, StringUtils.isNotBlank(payed)?"¥"+MoneyNumberFormat.getThousandsMoney(Double.valueOf(payed), MoneyNumberFormat.THOUSANDST_POINT_TWO):"¥0.00",dataStyle);
    		createCell(dataRow, 7, StringUtils.isNotBlank(sub)?"¥"+MoneyNumberFormat.getThousandsMoney(Double.valueOf(sub), MoneyNumberFormat.THOUSANDST_POINT_TWO):"¥0.00",dataStyle);
    		String date = DateUtils.date2String((Date) list.get(i).get("paymentDay"), "yyyy-MM-dd");
    		String now =  DateUtils.getDate();
    		createCell(dataRow, 8, StringUtils.isNotBlank(date)?date:"",dataStyle);
    		int flag = DateUtils.compareDate(date, "yyyy-MM-dd", now, "yyyy-MM-dd");
    		if(flag == -1){
    			createCell(dataRow, 9, "¥0.00",dataStyle);
    		}else{
    			createCell(dataRow, 9, StringUtils.isNotBlank(sub)?"¥"+MoneyNumberFormat.getThousandsMoney(Double.valueOf(sub), MoneyNumberFormat.THOUSANDST_POINT_TWO):"¥0.00",dataStyle);
    		}
    		
		}
    	return workbook;
    }
    
    /**
     * 签证押金收款导出excel
     * @author zhaohaiming
     * updated by xianglei.dong
     * 2016-03-28 根据247需求，将之前合并列的单元格进行拆分，并拆分金额列币种与金额
     * */
    public static Workbook exportOrderListForVisaList(List<Map<Object,Object>> list){
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	//excel 标题样式
    	HSSFFont headFont = workbook.createFont();
		HSSFCellStyle headStyle = workbook.createCellStyle();
    	headFont.setFontHeightInPoints((short)10);
    	headFont.setFontName("Courier New");
    	headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	headStyle.setFont(headFont);
    	
    	//数据字体和样式
    	Font dataFont = workbook.createFont();
    	HSSFCellStyle dataStyle = workbook.createCellStyle();
    	dataFont.setFontHeightInPoints((short)10);
    	dataFont.setFontName("Courier New");
    	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	dataStyle.setFont(dataFont);
    	
    	//数字显示格式
    	HSSFCellStyle numberStyle = workbook.createCellStyle();
    	numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);
    	numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
   		numberStyle.setFont(dataFont);
		HSSFDataFormat format = workbook.createDataFormat();
 		numberStyle.setDataFormat(format.getFormat("#,##0.00"));//设置单元类型
    	
    	//押金金额、已付金额、到账金额所含币种
    	List<String> depositMoneyCurrency = Lists.newArrayList();
    	List<String> payedMoneyCurrency = Lists.newArrayList();
    	List<String> arrivedMoneyCurrency = Lists.newArrayList();
    			
    	Map<Integer, List<Object[]>> depositMoneyMap = Maps.newHashMap();
    	Map<Integer, List<Object[]>> payedMoneyMap = Maps.newHashMap();
    	Map<Integer, List<Object[]>> arrivedMoneyMap = Maps.newHashMap();
    			
    	if(CollectionUtils.isNotEmpty(list)){
            for(int num =1;num<=list.size();num++){
            	//押金金额
            	String depositMoney = list.get(num-1).get("total_money")!=null?list.get(num-1).get("total_money").toString():"";
            	if(StringUtils.isNotBlank(depositMoney)) {
            		List<Object[]> objects = MoneyNumberFormat.getMoneyFromString(depositMoney, "\\+");
            		depositMoneyMap.put(num, objects);
            		if(CollectionUtils.isNotEmpty(objects)) {
            			for(int i=0; i<objects.size(); i++) {
            				Object[] amount = objects.get(i);
            				if(!depositMoneyCurrency.contains(amount[0])) {
            					depositMoneyCurrency.add((String)amount[0]);
            				}
            			}
            		}
            	}
            	
            	//已付金额
            	String payed_money = list.get(num-1).get("payed_money")!=null?list.get(num-1).get("payed_money").toString():"";
	    	    if(StringUtils.isNotBlank(payed_money)){
	    	    	List<Object[]> objects = MoneyNumberFormat.getMoneyFromString(payed_money, "\\+");
            		payedMoneyMap.put(num, objects);
            		if(CollectionUtils.isNotEmpty(objects)) {
            			for(int i=0; i<objects.size(); i++) {
            				Object[] amount = objects.get(i);
            				if(!payedMoneyCurrency.contains(amount[0])) {
            					payedMoneyCurrency.add((String)amount[0]);
            				}
            			}
            		}
	    	    }
	    	    //到账金额
            	String is_accounted = list.get(num-1).get("is_accounted")!=null?list.get(num-1).get("is_accounted").toString():"";
	    	    if(StringUtils.isNotBlank(is_accounted) && "1".equals(is_accounted) && StringUtils.isNotBlank(payed_money)){
	    	    	String arrived_money = payed_money;
	    	    	List<Object[]> objects = MoneyNumberFormat.getMoneyFromString(arrived_money, "\\+");
	    	    	arrivedMoneyMap.put(num, objects);
            		if(CollectionUtils.isNotEmpty(objects)) {
            			for(int i=0; i<objects.size(); i++) {
            				Object[] amount = objects.get(i);
            				if(!arrivedMoneyCurrency.contains(amount[0])) {
            					arrivedMoneyCurrency.add((String)amount[0]);
            				}
            			}
            		}
	    	    }    		    		
    		}
    	}
    	//对币种类型进行排序
    	Collections.sort(depositMoneyCurrency);
    	Collections.sort(payedMoneyCurrency);
    	Collections.sort(arrivedMoneyCurrency);
    	
    	int depositMoneyCurrencySize = depositMoneyCurrency.size() == 0? 1:depositMoneyCurrency.size()*2;
		int payedMoneyCurrencySize = payedMoneyCurrency.size() == 0? 1:payedMoneyCurrency.size()*2;
		int arrivedMoneyCurrencySize = arrivedMoneyCurrency.size() == 0? 1:arrivedMoneyCurrency.size()*2;
    	
    	String[] title = {"序号","收款日期","银行到账日期","订单号","团号","产品名称","游客姓名","计调","销售","下单人","渠道名称","来款单位","收款银行","押金金额","已收金额","到账金额","收款方式","打印确认"};
    	HSSFSheet sheet = workbook.createSheet();
    	sheet.setDefaultColumnWidth(15);// 设置单元格宽度
    	Row titleRow = sheet.createRow(0);
    	//创建标题,前十四个定项
    	for(int i=0; i<13; i++) {
    		Cell headCell = titleRow.createCell(i);
    		headCell.setCellValue(title[i]);
        	headCell.setCellStyle(headStyle);
    	}
    	
    	//押金金额
    	int columIndex = 13;
    	for(int i=0; i<depositMoneyCurrencySize; i++) {
        	Cell headCell = titleRow.createCell(columIndex+i);
        	headCell.setCellValue(title[13]);
    		headCell.setCellStyle(headStyle);
    		if(depositMoneyCurrencySize>1 && 0==i%2) {
    			sheet.setColumnWidth(columIndex+i, 5*256);
    		}
        } 
    	//合并订单金额单元格
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex+depositMoneyCurrencySize-1));
    	
    	//已付金额
    	columIndex += depositMoneyCurrencySize;
    	for(int i=0; i<payedMoneyCurrencySize; i++) {
        	Cell headCell = titleRow.createCell(columIndex+i);
        	headCell.setCellValue(title[14]);
    		headCell.setCellStyle(headStyle);
    		if(payedMoneyCurrencySize>1 && 0==i%2) {
    			sheet.setColumnWidth(columIndex+i, 5*256);
    		}
        }
    	//合并已付金额列
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex+payedMoneyCurrencySize-1));
    	
    	//到账金额
    	columIndex += payedMoneyCurrencySize;
    	for(int i=0; i<arrivedMoneyCurrencySize; i++) {
        	Cell headCell = titleRow.createCell(columIndex+i);
        	headCell.setCellValue(title[15]);
    		headCell.setCellStyle(headStyle);
    		if(arrivedMoneyCurrencySize>1 && 0==i%2) {
    			sheet.setColumnWidth(columIndex+i, 5*256);
    		}
        }
    	//合并已付金额列
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex+arrivedMoneyCurrencySize-1));
    	
    	//支付方式
    	columIndex += arrivedMoneyCurrencySize;
    	Cell payedTypeCell = titleRow.createCell(columIndex);
    	payedTypeCell.setCellValue(title[16]);
    	payedTypeCell.setCellStyle(headStyle);
    	//打印确认
    	Cell printConfirmCell = titleRow.createCell(columIndex+1);
    	printConfirmCell.setCellValue(title[17]);
    	printConfirmCell.setCellStyle(headStyle);
    	
    	//填充数据
    	if(CollectionUtils.isNotEmpty(list)){
    		for(int i=1;i<=list.size();i++){
    			int colnum = 0;    //记录列
    			
    			Row dataRow = sheet.createRow(i);    			
    			//序号
	    		Cell numCell = dataRow.createCell(colnum++);
	    		numCell.setCellStyle(dataStyle);
	    	    numCell.setCellValue(i);
	    		
	    	    //付款日期
	    	    Cell payedDateCell = dataRow.createCell(colnum++);
	    	    payedDateCell.setCellStyle(dataStyle);
	    	    payedDateCell.setCellValue(list.get(i-1).get("orderpaycreatedate")!=null?DateUtils.formatCustomDate((Date)list.get(i-1).get("orderpaycreatedate"), DateUtils.DATE_PATTERN_YYYY_MM_DD):"");
	    	    
	    	    //银行到账日期
	    	    Cell accountDateCell = dataRow.createCell(colnum++);
	    	    accountDateCell.setCellStyle(dataStyle);
	    	    String confirm = list.get(i-1).get("is_accounted")!=null?list.get(i-1).get("is_accounted").toString():"";
	    	    if(StringUtils.isNotBlank(confirm) && "1".equals(confirm)){
	    	        accountDateCell.setCellValue(list.get(i-1).get("accountDate")!=null?DateUtils.formatCustomDate((Date)list.get(i-1).get("accountDate"), DateUtils.DATE_PATTERN_YYYY_MM_DD):"");
	    	    }
	    	    
	    	    //切位订单号
	    	    Cell orderNumCell = dataRow.createCell(colnum++);
	    	    orderNumCell.setCellStyle(dataStyle);
	    	    orderNumCell.setCellValue(list.get(i-1).get("orderNum")!=null?list.get(i-1).get("orderNum").toString():"");
	    	    //团号
	    	    Cell groupCodeCell = dataRow.createCell(colnum++);
	    	    groupCodeCell.setCellStyle(dataStyle);
	    	    groupCodeCell.setCellValue(list.get(i-1).get("groupCode")!=null?list.get(i-1).get("groupCode").toString():"");
	    	    
	    	    //产品名称
	    	    Cell productNameCell = dataRow.createCell(colnum++);
	    	    productNameCell.setCellStyle(dataStyle);
	    	    productNameCell.setCellValue(list.get(i-1).get("productName")!=null?list.get(i-1).get("productName").toString():"");
	    	    
	    	    //游客姓名
	    	    Cell travelerNameCell = dataRow.createCell(colnum++);
	    	    travelerNameCell.setCellStyle(dataStyle);
	    	    travelerNameCell.setCellValue(list.get(i-1).get("travlerName")!=null?list.get(i-1).get("travlerName").toString():"");
	    	    
	    	    //计调
	    	    Cell jdCell = dataRow.createCell(colnum++);
	    	    jdCell.setCellStyle(dataStyle);
	    	    String jd = list.get(i-1).get("createBy")!=null?list.get(i-1).get("createBy").toString():"";
	    	    if(StringUtils.isNotBlank(jd)){
	    	    	jdCell.setCellValue(UserUtils.getUser(jd).getName());
	    	    }
	    	    
	    	    //销售
	    	    Cell salerCell = dataRow.createCell(colnum++);
	    	    salerCell.setCellStyle(dataStyle);
	    	    String salerName = list.get(i-1).get("saler")!=null?list.get(i-1).get("saler").toString():"";
	    	    if(StringUtils.isNotBlank(salerName)){
	    	    	 salerCell.setCellValue(salerName);
	    	    }	    	   
	    	    
	    	    //下单人
	    	    Cell bookPersonCell = dataRow.createCell(colnum++);
	    	    bookPersonCell.setCellStyle(dataStyle);
	    	    String createBy = list.get(i-1).get("creator")!=null?list.get(i-1).get("creator").toString():"";
	    	    if(StringUtils.isNotBlank(createBy)){
	    	    	bookPersonCell.setCellValue(UserUtils.getUser(createBy).getName());
	    	    }
	    	    	    	    
	    	    //渠道名称
	    	    Cell agentCell = dataRow.createCell(colnum++);
	    	    agentCell.setCellStyle(dataStyle);
	    	    agentCell.setCellValue(list.get(i-1).get("agentName")!=null?list.get(i-1).get("agentName").toString():"");
	    	     	    	    	    
	    	    //来款单位
	    	    Cell payeeCell = dataRow.createCell(colnum++);
	    	    payeeCell.setCellStyle(dataStyle);
	    	    payeeCell.setCellValue(list.get(i-1).get("payerName")!=null?list.get(i-1).get("payerName").toString():"");
	    	    	
	    	    //收款银行
	    	    Cell receiveBankCell = dataRow.createCell(colnum++);
	    	    receiveBankCell.setCellStyle(dataStyle);
	    	    receiveBankCell.setCellValue(list.get(i-1).get("toBankNname")!=null?list.get(i-1).get("toBankNname").toString():"");
	    	    
	    	    //押金金额
	    	    int depositmoneyIndex = colnum;                     //记录订单金额填充开始位置
	    		if(depositMoneyCurrency.size() != 0) {
	    			for(int j=0; j<depositMoneyCurrency.size()*2; j++) {     //将depositMoneyCurrency中的货币类型填充至单元格中
		    			Cell depositMoneyCell = dataRow.createCell(colnum++);   		
			    		if(1 == (j+1)%2) {
			    			depositMoneyCell.setCellStyle(dataStyle);
			    			depositMoneyCell.setCellValue(depositMoneyCurrency.get(j/2));
			    		}
			    		else {
			    			depositMoneyCell.setCellStyle(numberStyle);
			    		}
		    		}
		    		//填充货币金额
		    		if(depositMoneyMap.containsKey(i)) {
		    			List<Object[]> depositMoneyList = depositMoneyMap.get(i);
			    		for(int j=0; j<depositMoneyList.size(); j++) {
			    			Object[] objects = depositMoneyList.get(j);
			    			int index = depositMoneyCurrency.indexOf(objects[0]);
			    			dataRow.getCell(depositmoneyIndex+index*2+1).setCellValue(Double.valueOf(objects[1].toString()));
			    		}
		    		}
	    		} else {  //若没有，产生单一空行
	    			Cell orderTotalMoneyCell = dataRow.createCell(colnum++);
	    			orderTotalMoneyCell.setCellStyle(dataStyle);
	    			orderTotalMoneyCell.setCellValue("");
	    		}
	    	    	    			    	  	    	    
	    		//已付金额
	    		int payedmoneyIndex = colnum;                     //记录已付金额填充开始位置
	    		if(payedMoneyCurrency.size() != 0) {
	    			for(int j=0; j<payedMoneyCurrency.size()*2; j++) {     //将payedMoneyCurrency中的货币类型填充至单元格中
		    			Cell orderPayMoneyCell = dataRow.createCell(colnum++);
			    		if(1 == (j+1)%2) {
			    			orderPayMoneyCell.setCellStyle(dataStyle);
			    			orderPayMoneyCell.setCellValue(payedMoneyCurrency.get(j/2));
			    		}else {
			    			orderPayMoneyCell.setCellStyle(numberStyle);
			    		}
		    		}
		    		//填充货币金额
		    		if(payedMoneyMap.containsKey(i)) {
		    			List<Object[]> payedMoneyList = payedMoneyMap.get(i);
			    		for(int j=0; j<payedMoneyList.size(); j++) {
			    			Object[] objects = payedMoneyList.get(j);
			    			int index = payedMoneyCurrency.indexOf(objects[0]);
			    			dataRow.getCell(payedmoneyIndex+index*2+1).setCellValue(Double.valueOf(objects[1].toString()));
			    		}
		    		}
	    		} else {
	    			Cell orderPayMoneyCell = dataRow.createCell(colnum++);
	    			orderPayMoneyCell.setCellStyle(dataStyle);
	    			orderPayMoneyCell.setCellValue("");
	    		}
	    		    		
	    		//到账金额
	    		int arrivedmoneyIndex = colnum;                     //记录已付金额填充开始位置
	    		if(arrivedMoneyCurrency.size() != 0) {
	    			for(int j=0; j<arrivedMoneyCurrency.size()*2; j++) {     //将arrivedMoneyCurrency中的货币类型填充至单元格中
		    			Cell hasArrivedCell = dataRow.createCell(colnum++);	    			
			    		if(1 == (j+1)%2) {
			    			hasArrivedCell.setCellStyle(dataStyle);
			    			hasArrivedCell.setCellValue(payedMoneyCurrency.get(j/2));
			    		} else {
			    			hasArrivedCell.setCellStyle(numberStyle);
			    		}
		    		}
		    		//填充货币金额
		    		if(arrivedMoneyMap.containsKey(i)) {
		    			List<Object[]> arrivedMoneyList = arrivedMoneyMap.get(i);
		    			for(int j=0; j<arrivedMoneyList.size(); j++) {
			    			Object[] objects = arrivedMoneyList.get(j);
			    			int index = arrivedMoneyCurrency.indexOf(objects[0]);
			    			dataRow.getCell(arrivedmoneyIndex+index*2+1).setCellValue(Double.valueOf(objects[1].toString()));
			    		}
		    		}
	    		} else {
	    			Cell hasArrivedCell = dataRow.createCell(colnum++);	 
	    			hasArrivedCell.setCellStyle(dataStyle);
	    			hasArrivedCell.setCellValue("");
	    		}	    	    
	    	    
	    	    //支付方式
	    	    Cell payTypeCell = dataRow.createCell(colnum++);
	    	    payTypeCell.setCellStyle(dataStyle);
	    	    String payType = list.get(i-1).get("payType")!=null?list.get(i-1).get("payType").toString():"";
	    		if(StringUtils.isNotBlank(payType)){
	    			if("1".equals(payType)){
	    				payTypeCell.setCellValue("支票");
	    			}else if("3".equals(payType) || "5".equals(payType)){
	    				payTypeCell.setCellValue("现金");
	    			}else if("4".equals(payType)){
	    				payTypeCell.setCellValue("汇款");
	    			}else if("6".equals(payType)){
	    				payTypeCell.setCellValue("银行转账");
	    			}else if("7".equals(payType)){
	    				payTypeCell.setCellValue("汇票");
	    			}else if("8".equals(payType) || "2".equals(payType)){
	    				payTypeCell.setCellValue("POS");
	    			}
	    		}
	    	    
	    		//打印状态
	    		Cell printStatusCell = dataRow.createCell(colnum);
	    		printStatusCell.setCellStyle(dataStyle);
	    		String printFlag = list.get(i-1).get("printFlag")!=null?list.get(i-1).get("printFlag").toString():"";
	    		if(StringUtils.isNotBlank(printFlag)){
	    			if("0".equals(printFlag)){
	    				printStatusCell.setCellValue("未打印");
	    			}else if("1".equals(printFlag)){
	    				printStatusCell.setCellValue("已打印");
	    			}
	    		}
	    	    
    		}
    	}
    	return workbook;
    }
    
    /*
     * 签证订单收款导出excel
     * created by xianglei.dong
     * 2016-03-29 
     */
    public static Workbook exportVisaOrderPayList(List<Map<Object,Object>> list) {
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	//excel 标题样式
    	HSSFFont headFont = workbook.createFont();
		HSSFCellStyle headStyle = workbook.createCellStyle();
    	headFont.setFontHeightInPoints((short)10);
    	headFont.setFontName("Courier New");
    	headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	headStyle.setFont(headFont);
    	
    	//数据字体和样式
    	Font dataFont = workbook.createFont();
    	HSSFCellStyle dataStyle = workbook.createCellStyle();
    	dataFont.setFontHeightInPoints((short)10);
    	dataFont.setFontName("Courier New");
    	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	dataStyle.setFont(dataFont);
    	
    	//数字显示格式
    	HSSFCellStyle numberStyle = workbook.createCellStyle();
    	numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);
    	numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
   		numberStyle.setFont(dataFont);
		HSSFDataFormat format = workbook.createDataFormat();
 		numberStyle.setDataFormat(format.getFormat("#,##0.00"));//设置单元类型
 		
 		//获取订单金额、累计到帐金额、已付金额、到账金额所含币种
 		List<String> totalMoneyCurrency = Lists.newArrayList();
 		List<String> accountedMoneyCurrency = Lists.newArrayList();
 		List<String> payedMoneyCurrency = Lists.newArrayList();
 		List<String> arrivedMoneyCurrency = Lists.newArrayList();
 				
 		Map<Integer, List<Object[]>> totalMoneyMap = Maps.newHashMap();
 		Map<Integer, List<Object[]>> accountedMoneyMap = Maps.newHashMap();
 		Map<Integer, List<Object[]>> payedMoneyMap = Maps.newHashMap();
 		Map<Integer, List<Object[]>> arrivedMoneyMap = Maps.newHashMap();
 				
 		if(CollectionUtils.isNotEmpty(list)){
 			for(int num =1;num<=list.size();num++){ 			    		
 			    //订单金额
 			    String totalmoney = list.get(num-1).get("total_money")!=null?list.get(num-1).get("total_money").toString():"";
 			    if(StringUtils.isNoneBlank(totalmoney)){
 			    	List<Object[]> objects = MoneyNumberFormat.getMoneyFromString(totalmoney, "\\+");
 			    	totalMoneyMap.put(num, objects);
 			    	if(CollectionUtils.isNotEmpty(objects)) {
 			    		for (int i = 0; i < objects.size(); i++) {
 			    			Object[] amount = objects.get(i);
 			    			if(!totalMoneyCurrency.contains(amount[0])) {
 			    				totalMoneyCurrency.add((String) amount[0]);
 			    			}
 			    		}
 			    	}
 			    }
 			    
 			    //累计到帐金额	
 			    String accountmoney = "";
 			    if("7a45838277a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())) {
 			    	accountmoney = list.get(num-1).get("no_pay_money")!=null?list.get(num-1).get("no_pay_money").toString():"";
 			    	if(StringUtils.isBlank(accountmoney)) {
 			    		accountmoney = list.get(num-1).get("accounted_money")!=null?list.get(num-1).get("accounted_money").toString():"";
 			    	}
 			    }else {
 			    	String cancleFlag = list.get(num-1).get("cancleFlag").toString();
 			    	if(StringUtils.isNotBlank(cancleFlag) && cancleFlag.substring(5).equals("1")) {
 			    		accountmoney = list.get(num-1).get("accounted_money")!=null? list.get(num-1).get("accounted_money").toString():"";
 			    	}
 			    }
 			    		
 			    if(StringUtils.isNoneBlank(accountmoney)){
 			    	List<Object[]> objects = MoneyNumberFormat.getMoneyFromString(accountmoney, "\\+");
 			    	accountedMoneyMap.put(num, objects);
 			    	if(CollectionUtils.isNotEmpty(objects)) {
 			    		for (int i = 0; i < objects.size(); i++) {
 			    			Object[] amount = objects.get(i);
 			    			if(!accountedMoneyCurrency.contains(amount[0])) {
 			    				accountedMoneyCurrency.add((String) amount[0]);
 			    			}	
 			    		}
 			    	}
 			    }
 			    
 			    //已付金额
 			    String payMoney = list.get(num-1).get("payed_money")!=null?list.get(num-1).get("payed_money").toString():"";
 			    if(StringUtils.isNoneBlank(payMoney)){
 			    	List<Object[]> objects = MoneyNumberFormat.getMoneyFromString(payMoney, "\\+");
 			    	payedMoneyMap.put(num, objects);
 			    	if(CollectionUtils.isNotEmpty(objects)) {
 			   			for (int i = 0; i < objects.size(); i++) {
 			   				Object[] amount = objects.get(i);
 			   				if(!payedMoneyCurrency.contains(amount[0])) {
 			   					payedMoneyCurrency.add((String) amount[0]);
 		    				}	    					
 		    			}
		    		}
 		    	}
 			    
 				//到账金额
 	    		String account = list.get(num-1).get("isAsAccount")!=null?list.get(num-1).get("isAsAccount").toString():"";
 			    if(StringUtils.isNotBlank(account) && account.substring(5).equals("1")){
 			    	String arrivedMoney = list.get(num-1).get("payed_money")!=null?list.get(num-1).get("payed_money").toString():"";
 			    	if(StringUtils.isNoneBlank(arrivedMoney)){
 			    	    List<Object[]> objects = MoneyNumberFormat.getMoneyFromString(arrivedMoney, "\\+");
 			    	    arrivedMoneyMap.put(num, objects);
 			    	    if(CollectionUtils.isNotEmpty(objects)) {
 			    	    	for (int i = 0; i < objects.size(); i++) {
 			    	    		Object[] amount = objects.get(i);
 			    	    		if(!arrivedMoneyCurrency.contains(amount[0])) {
 			    	    			arrivedMoneyCurrency.add((String) amount[0]);
 			    	    		}
 			    	    	}
 			    	    }
 			    	}
 			    }
 			}
 		}
 		//对币种类型进行排序
 		Collections.sort(totalMoneyCurrency);
 		Collections.sort(accountedMoneyCurrency);
 		Collections.sort(payedMoneyCurrency);
 		Collections.sort(arrivedMoneyCurrency);
 				
 		int totalMoneyCurrencySize = totalMoneyCurrency.size() == 0? 1:totalMoneyCurrency.size()*2;
 		int accountedMoneyCurrencySize = accountedMoneyCurrency.size() == 0? 1:accountedMoneyCurrency.size()*2;
 		int payedMoneyCurrencySize = payedMoneyCurrency.size() == 0? 1:payedMoneyCurrency.size()*2;
 		int arrivedMoneyCurrencySize = arrivedMoneyCurrency.size() == 0? 1:arrivedMoneyCurrency.size()*2;
 				
 		String [] title ={"序号","收款日期","银行到账日期","订单号","团号","产品名称","销售","下单人","计调","渠道名称","来款单位","收款银行","订单金额","累计到帐金额","已收金额","到账金额","收款方式","打印确认"};
 		HSSFSheet sheet = workbook.createSheet();
 		sheet.setDefaultColumnWidth(15);// 设置单元格宽度
 		Row titleRow = sheet.createRow(0);
 		//创建标题,前十二个定项
 		for(int i=0; i<12; i++) {
 		    Cell headCell = titleRow.createCell(i);
 		    headCell.setCellValue(title[i]);
 		    headCell.setCellStyle(headStyle);
 		}
 		    	
		// 订单金额
		int columIndex = 12;
		for (int i = 0; i < totalMoneyCurrencySize; i++) {
			Cell headCell = titleRow.createCell(columIndex + i);
			headCell.setCellValue(title[12]);
			headCell.setCellStyle(headStyle);
			if (totalMoneyCurrencySize>1 && 0==i%2) {
				sheet.setColumnWidth(columIndex + i, 5 * 256);
			}
		}
		// 合并订单金额单元格
		sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex + totalMoneyCurrencySize - 1));

		// 累计达帐金额
		columIndex += totalMoneyCurrencySize;
		for (int i = 0; i < accountedMoneyCurrencySize; i++) {
			Cell headCell = titleRow.createCell(columIndex + i);
			headCell.setCellValue(title[13]);
			headCell.setCellStyle(headStyle);
			if (accountedMoneyCurrencySize>1 && 0==i%2) {
				sheet.setColumnWidth(columIndex + i, 5 * 256);
			}
		}
		// 合并累计达帐金额单元格
		sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex + accountedMoneyCurrencySize - 1));

		// 已付金额
		columIndex += accountedMoneyCurrencySize;
		for (int i = 0; i < payedMoneyCurrencySize; i++) {
			Cell headCell = titleRow.createCell(columIndex + i);
			headCell.setCellValue(title[14]);
			headCell.setCellStyle(headStyle);
			if (payedMoneyCurrencySize>1 && 0==i%2) {
				sheet.setColumnWidth(columIndex + i, 5 * 256);
			}
		}
		// 合并已付金额列
		sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex + payedMoneyCurrencySize - 1));

		// 到账金额
		columIndex += payedMoneyCurrencySize;
		for (int i = 0; i < arrivedMoneyCurrencySize; i++) {
			Cell headCell = titleRow.createCell(columIndex + i);
			headCell.setCellValue(title[15]);
			headCell.setCellStyle(headStyle);
			if (arrivedMoneyCurrencySize>1 && 0==i%2) {
				sheet.setColumnWidth(columIndex + i, 5 * 256);
			}
		}
		// 合并已付金额列
		sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex + arrivedMoneyCurrencySize - 1));

		// 支付方式
		columIndex += arrivedMoneyCurrencySize;
		Cell payedTypeCell = titleRow.createCell(columIndex);
		payedTypeCell.setCellValue(title[16]);
		payedTypeCell.setCellStyle(headStyle);
		// 打印确认
		Cell printConfirmCell = titleRow.createCell(columIndex + 1);
		printConfirmCell.setCellValue(title[17]);
		printConfirmCell.setCellStyle(headStyle);

		// 填充Excel表
		if (CollectionUtils.isNotEmpty(list)) {
			for (int num = 1; num <= list.size(); num++) {
				int rownum = num; // 记录行
				int colnum = 0; // 记录列

				HSSFRow dataRow = sheet.createRow(rownum);
				// 序号
				Cell cell0 = dataRow.createCell(colnum++);
				cell0.setCellStyle(dataStyle);
				cell0.setCellValue(num);

				// 付款日期
				Cell cell1 = dataRow.createCell(colnum++);
				cell1.setCellStyle(dataStyle);
				cell1.setCellValue(list.get(num - 1).get("odate") != null ? DateUtils.date2String(DateUtils.string2Date(list.get(num - 1).get("odate").toString(), "yyyy-MM-dd"), "yyyy-MM-dd"):""); 
				// 银行到账日期
				Cell accountDateCell = dataRow.createCell(colnum++);
				accountDateCell.setCellStyle(dataStyle);
				if("1".equals(list.get(num-1).get("isAsAccount").toString().substring(5))){
					accountDateCell.setCellValue(list.get(num - 1).get("accountDate") != null ? DateUtils.date2String(DateUtils.string2Date(list.get(num - 1).get("accountDate").toString(), "yyyy-MM-dd"), "yyyy-MM-dd") : "");
				}

				// 订单号
				Cell orderNumCell = dataRow.createCell(colnum++);
				orderNumCell.setCellStyle(dataStyle);
				orderNumCell.setCellValue(list.get(num - 1).get("orderNum") != null ? list.get(num - 1).get("orderNum").toString(): "");

				// 团号
				Cell groupCodeCell = dataRow.createCell(colnum++);
				groupCodeCell.setCellStyle(dataStyle);
				groupCodeCell.setCellValue(list.get(num - 1).get("groupCode") != null ? list.get(num - 1).get("groupCode").toString(): "");

				// 产品名称
				Cell productNameCell = dataRow.createCell(colnum++);
				productNameCell.setCellStyle(dataStyle);
				productNameCell.setCellValue(list.get(num - 1).get("productName") != null ? list.get(num - 1).get("productName").toString() : "");

				// 销售
				Cell salerCell = dataRow.createCell(colnum++);
				salerCell.setCellStyle(dataStyle);
				salerCell.setCellValue(list.get(num - 1).get("sName") != null ? list.get(num - 1).get("sName").toString() : "");
				
				// 下单人
				Cell bookPersonCell = dataRow.createCell(colnum++);
				bookPersonCell.setCellStyle(dataStyle);
				bookPersonCell.setCellValue(list.get(num - 1).get("suuName") != null ? list.get(num - 1).get("suuName").toString() : "");
				
				// 计调
				Cell jdCell = dataRow.createCell(colnum++);
				jdCell.setCellStyle(dataStyle);
				jdCell.setCellValue(list.get(num - 1).get("jd_Name") != null ? list.get(num - 1).get("jd_Name").toString(): "");

				// 渠道名称
				Cell agentCell = dataRow.createCell(colnum++);
				agentCell.setCellStyle(dataStyle);
				agentCell.setCellValue(list.get(num - 1).get("agentName") != null ? list.get(num - 1).get("agentName").toString() : "");

				// 来款单位
				Cell payeeCell = dataRow.createCell(colnum++);
				payeeCell.setCellStyle(dataStyle);
				payeeCell.setCellValue(list.get(num - 1).get("payerName") != null ? list.get(num - 1).get("payerName").toString(): "");

				// 收款行
				Cell toBankNnameCell = dataRow.createCell(colnum++);
				toBankNnameCell.setCellStyle(dataStyle);
				toBankNnameCell.setCellValue(list.get(num - 1).get("toBankNname") != null ? list.get(num - 1).get("toBankNname").toString() : "");

				// 订单金额
				int totalmoneyIndex = colnum; // 记录订单金额填充开始位置
				if (totalMoneyCurrency.size() != 0) {
					for (int i = 0; i < totalMoneyCurrency.size() * 2; i++) { // 将totalMoneyCurrency中的货币类型填充至单元格中
						HSSFCell orderTotalMoneyCell = dataRow.createCell(colnum++);
						if (1 == (i + 1) % 2) {
							orderTotalMoneyCell.setCellStyle(dataStyle);
							orderTotalMoneyCell.setCellValue(totalMoneyCurrency.get(i / 2));
						} else {
							orderTotalMoneyCell.setCellStyle(numberStyle);
						}
					}
					// 填充货币金额
					if (totalMoneyMap.containsKey(num)) {
						List<Object[]> totalMoneyList = totalMoneyMap.get(num);
						for (int i = 0; i < totalMoneyList.size(); i++) {
							Object[] objects = totalMoneyList.get(i);
							int index = totalMoneyCurrency.indexOf(objects[0]);
							dataRow.getCell(totalmoneyIndex + index * 2 + 1).setCellValue(Double.valueOf(objects[1].toString()));
						}
					}
				} else { // 若没有，产生单一空行
					HSSFCell orderTotalMoneyCell = dataRow.createCell(colnum++);
					orderTotalMoneyCell.setCellStyle(dataStyle);
					orderTotalMoneyCell.setCellValue("");
				}

				// 累计到帐金额
				int accountedmoneyIndex = colnum; // 记录累计到帐金额填充开始位置
				if (accountedMoneyCurrency.size() != 0) {
					for (int i = 0; i < accountedMoneyCurrency.size() * 2; i++) { // 将accountedMoneyCurrency中的货币类型填充至单元格中
						HSSFCell hasAccountToalCell = dataRow.createCell(colnum++);
						if (1 == (i + 1) % 2) {
							hasAccountToalCell.setCellStyle(dataStyle);
							hasAccountToalCell.setCellValue(accountedMoneyCurrency.get(i / 2));
						} else {
							hasAccountToalCell.setCellStyle(numberStyle);
						}
					}
					// 填充货币金额
					if (accountedMoneyMap.containsKey(num)) {
						List<Object[]> accountedMoneyList = accountedMoneyMap.get(num);
						for (int i = 0; i < accountedMoneyList.size(); i++) {
							Object[] objects = accountedMoneyList.get(i);
							int index = accountedMoneyCurrency.indexOf(objects[0]);
							dataRow.getCell(accountedmoneyIndex + index * 2 + 1).setCellValue(Double.valueOf(objects[1].toString()));
						}
					}
				} else {
					HSSFCell hasAccountToalCell = dataRow.createCell(colnum++);
					hasAccountToalCell.setCellStyle(dataStyle);
					hasAccountToalCell.setCellValue("");
				}

				// 已付金额
				int payedmoneyIndex = colnum; // 记录已付金额填充开始位置
				if (payedMoneyCurrency.size() != 0) {
					for (int i = 0; i < payedMoneyCurrency.size() * 2; i++) { // 将payedMoneyCurrency中的货币类型填充至单元格中
						HSSFCell orderPayMoneyCell = dataRow.createCell(colnum++);
						if (1 == (i + 1) % 2) {
							orderPayMoneyCell.setCellStyle(dataStyle);
							orderPayMoneyCell.setCellValue(payedMoneyCurrency.get(i / 2));
						} else {
							orderPayMoneyCell.setCellStyle(numberStyle);
						}
					}
					// 填充货币金额					
					if (payedMoneyMap.containsKey(num)) {
						List<Object[]> payedMoneyList = payedMoneyMap.get(num);
						for (int i = 0; i < payedMoneyList.size(); i++) {
							Object[] objects = payedMoneyList.get(i);
							int index = payedMoneyCurrency.indexOf(objects[0]);
							dataRow.getCell(payedmoneyIndex + index * 2 + 1).setCellValue(Double.valueOf(objects[1].toString()));
						}
					}
				} else {
					HSSFCell orderPayMoneyCell = dataRow.createCell(colnum++);
					orderPayMoneyCell.setCellStyle(dataStyle);
					orderPayMoneyCell.setCellValue("");
				}

				// 到账金额
				int arrivedmoneyIndex = colnum; // 记录已付金额填充开始位置
				if (arrivedMoneyCurrency.size() != 0) {
					for (int i = 0; i < arrivedMoneyCurrency.size() * 2; i++) { // 将arrivedMoneyCurrency中的货币类型填充至单元格中
						HSSFCell hasArrivedCell = dataRow.createCell(colnum++);
						if (1 == (i + 1) % 2) {
							hasArrivedCell.setCellStyle(dataStyle);
							hasArrivedCell.setCellValue(payedMoneyCurrency.get(i / 2));
						} else {
							hasArrivedCell.setCellStyle(numberStyle);
						}
					}
					// 填充货币金额
					if (arrivedMoneyMap.containsKey(num)) {
						List<Object[]> arrivedMoneyList = arrivedMoneyMap.get(num);
						for (int i = 0; i < arrivedMoneyList.size(); i++) {
							Object[] objects = arrivedMoneyList.get(i);
							int index = arrivedMoneyCurrency.indexOf(objects[0]);
							dataRow.getCell(arrivedmoneyIndex + index * 2 + 1).setCellValue(Double.valueOf(objects[1].toString()));
						}
					}
				} else {
					HSSFCell hasArrivedCell = dataRow.createCell(colnum++);
					hasArrivedCell.setCellStyle(dataStyle);
					hasArrivedCell.setCellValue("");
				}

				// 支付方式
				Cell payTypeCell = dataRow.createCell(colnum++);
				payTypeCell.setCellStyle(dataStyle);
				String payType = list.get(num - 1).get("payType") != null ? list.get(num - 1).get("payType").toString(): "";
				if (StringUtils.isNotBlank(payType)) {
					if ("1".equals(payType)) {
						payTypeCell.setCellValue("支票");
					} else if ("2".equals(payType)) {
						payTypeCell.setCellValue("POS机付款");
					} else if ("3".equals(payType)) {
						payTypeCell.setCellValue("现金支付");
					} else if ("4".equals(payType)) {
						payTypeCell.setCellValue("汇款");
					} else if("5".equals(payType)) {
						payTypeCell.setCellValue("快速支付");
					} else if ("6".equals(payType)) {
						payTypeCell.setCellValue("银行转账");
					} else if ("7".equals(payType)) {
						payTypeCell.setCellValue("汇票");
					} else if ("8".equals(payType)) {
						payTypeCell.setCellValue("POS机刷卡");
						
					}
				}

				// 打印状态
				Cell printStatusCell = dataRow.createCell(colnum++);
				printStatusCell.setCellStyle(dataStyle);
				String printFlag = list.get(num - 1).get("printFlag") != null ? list.get(num - 1).get("printFlag").toString(): "";
				if (StringUtils.isNotBlank(printFlag)) {
					if ("0".equals(printFlag)) {
						printStatusCell.setCellValue("未打印");
					} else if ("1".equals(printFlag)) {
						printStatusCell.setCellValue("已打印");
					}
				}
			}
		}

		return workbook;
    }
    
    /**
     * 切位收款导出excel
     * @author zhaohaiming
     * updated by xianglei.dong
     * 2016-03-28 根据247需求，将之前合并列的单元格进行拆分，并拆分金额列币种与金额
     * */
    public static Workbook exportReserveOrderList(List<Map<Object,Object>> list){
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	String[] title ={"序号","收款日期","银行到账日期","切位订单号","团号","产品名称","计调","销售","下单人","渠道名称","团队类型","来款单位","收款银行","订单状态","订单金额","订单金额","已收金额","已收金额","到账金额","到账金额","收款方式","打印确认"};
    	HSSFFont headFont = workbook.createFont();
    	//title 样式
		HSSFCellStyle headStyle = workbook.createCellStyle();
    	headFont.setFontHeightInPoints((short)10);
    	headFont.setFontName("Courier New");
    	headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	headStyle.setFont(headFont);
    	
    	//数据字体和样式
    	Font dataFont = workbook.createFont();
    	HSSFCellStyle dataStyle = workbook.createCellStyle();
    	dataFont.setFontHeightInPoints((short)10);
    	dataFont.setFontName("Courier New");
    	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	dataStyle.setFont(dataFont);
    	
    	//数字显示格式
	    HSSFCellStyle numberStyle = workbook.createCellStyle();
	    numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);
	    numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    numberStyle.setFont(dataFont);
	    HSSFDataFormat format = workbook.createDataFormat();
	    numberStyle.setDataFormat(format.getFormat("#,##0.00"));//设置单元类型
    	
    	HSSFSheet sheet = workbook.createSheet();
    	sheet.setDefaultColumnWidth(15);// 设置单元格宽度
    	int length = title.length;
    	Row titleRow = sheet.createRow(0);
    	//创建标题
    	for(int i=0;i<length;i++){
    		Cell headCell = titleRow.createCell(i);
    		headCell.setCellStyle(headStyle);  			
    		headCell.setCellValue(title[i]);
    	}
    	//合并单元格
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, 14, 15));
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, 16, 17));
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, 18, 19));
    	sheet.setColumnWidth(14, 5*256);
    	sheet.setColumnWidth(16, 5*256);
    	sheet.setColumnWidth(18, 5*256);
    	
    	//填充数据
    	if(CollectionUtils.isNotEmpty(list)){
	    	for(int i=1;i<=list.size();i++){
	    		Row dataRow = sheet.createRow(i);
	    		
	    		//序号
	    		Cell numCell = dataRow.createCell(0);
	    		numCell.setCellStyle(dataStyle);
	    	    numCell.setCellValue(i);
	    		
	    	    //付款日期
	    	    Cell payedDateCell = dataRow.createCell(1);
	    	    payedDateCell.setCellStyle(dataStyle);
	    	    payedDateCell.setCellValue(list.get(i-1).get("createDate")!=null?DateUtils.formatCustomDate((Date)list.get(i-1).get("createDate"), DateUtils.DATE_PATTERN_YYYY_MM_DD):"");
	    	    
	    	    //银行到账日期
	    	    Cell accountDateCell = dataRow.createCell(2);
	    	    accountDateCell.setCellStyle(dataStyle);
	    	    String confirm = list.get(i-1).get("confirm")!=null?list.get(i-1).get("confirm").toString():"";
	    	    if(StringUtils.isNotBlank(confirm) && "1".equals(confirm)){
	    	        accountDateCell.setCellValue(list.get(i-1).get("updateDate")!=null?DateUtils.formatCustomDate((Date)list.get(i-1).get("updateDate"), DateUtils.DATE_PATTERN_YYYY_MM_DD):"");
	    	    }
	    	    
	    	    //切位订单号
	    	    Cell orderNumCell = dataRow.createCell(3);
	    	    orderNumCell.setCellStyle(dataStyle);
	    	    orderNumCell.setCellValue(list.get(i-1).get("orderNum")!=null?list.get(i-1).get("orderNum").toString():"");
	    	    
	    	    //团号
	    	    Cell groupCodeCell = dataRow.createCell(4);
	    	    groupCodeCell.setCellStyle(dataStyle);
	    	    groupCodeCell.setCellValue(list.get(i-1).get("groupCode")!=null?list.get(i-1).get("groupCode").toString():"");
	    	    
	    	    //产品名称
	    	    Cell productNameCell = dataRow.createCell(5);
	    	    productNameCell.setCellStyle(dataStyle);
	    	    productNameCell.setCellValue(list.get(i-1).get("acitivityName")!=null?list.get(i-1).get("acitivityName").toString():"");
	    	    
	    	    //计调
	    	    Cell jdCell = dataRow.createCell(6);
	    	    jdCell.setCellStyle(dataStyle);
	    	    String jd = list.get(i-1).get("operator")!=null?list.get(i-1).get("operator").toString():"";
	    	    if(StringUtils.isNotBlank(jd)){
	    	    	jdCell.setCellValue(UserUtils.getUser(jd).getName());
	    	    }
	    	    
	    	    //销售
	    	    Cell salerCell = dataRow.createCell(7);
	    	    salerCell.setCellStyle(dataStyle);
	    	    String salerId = list.get(i-1).get("saleId")!=null?list.get(i-1).get("saleId").toString():"";
	    	    if(StringUtils.isNotBlank(salerId)){
	    	    	 salerCell.setCellValue(UserUtils.getUser(salerId).getName());
	    	    }
	    	       	    
	    	    //下单人
	    	    Cell bookPersonCell = dataRow.createCell(8);
	    	    bookPersonCell.setCellStyle(dataStyle);
	    	    String createBy = list.get(i-1).get("createBy")!=null?list.get(i-1).get("createBy").toString():"";
	    	    if(StringUtils.isNotBlank(createBy)){
	    	    	bookPersonCell.setCellValue(UserUtils.getUser(createBy).getName());
	    	    }
	    	    
	    	    //渠道名称
	    	    Cell agentCell = dataRow.createCell(9);
	    	    agentCell.setCellStyle(dataStyle);
	    	    agentCell.setCellValue(list.get(i-1).get("agentName")!=null?list.get(i-1).get("agentName").toString():"");
	    	    
	    	    //团队类型
	    	    Cell orderTypeCell = dataRow.createCell(10);
	    	    orderTypeCell.setCellStyle(dataStyle);
	    	    String orderType = list.get(i-1).get("orderType")!=null?list.get(i-1).get("orderType").toString():"";
	    	    if(StringUtils.isNotBlank(orderType)){
	    	    	orderTypeCell.setCellValue(DictUtils.getDictLabel(orderType, "order_type", ""));
	    	    }	    	    
	    	    
	    	    //来款单位
	    	    Cell payeeCell = dataRow.createCell(11);
	    	    payeeCell.setCellStyle(dataStyle);
	    	    payeeCell.setCellValue("");
	    	    	
	    	    //收款银行
	    	    Cell receiveBankCell = dataRow.createCell(12);
	    	    receiveBankCell.setCellStyle(dataStyle);
	    	    receiveBankCell.setCellValue("");
	    	    
	    	    //订单状态
	    	    Cell orderStatusCell = dataRow.createCell(13);
	    	    orderStatusCell.setCellStyle(dataStyle);
	    	    String orderStatus = list.get(i-1).get("orderStatus")!=null?list.get(i-1).get("orderStatus").toString():"";
	    	    if(StringUtils.isNotBlank(orderStatus)){
	    	    	if("0".equals(orderStatus)){
	    	    		orderStatusCell.setCellValue("未收订金");
	    	    	}else{
	    	    		orderStatusCell.setCellValue("已收订金");
	    	    	}	    	    	
	    	    }
	    	    
	    	    //订单金额
	    	    Cell orderTotalMoneyCurrency = dataRow.createCell(14);
	    	    orderTotalMoneyCurrency.setCellStyle(dataStyle);
	    	    orderTotalMoneyCurrency.setCellValue("¥");
	    	    
	    	    Cell orderTotalMoneyCell = dataRow.createCell(15);
	    	    orderTotalMoneyCell.setCellStyle(numberStyle);
	    	    String totalMoney = list.get(i-1).get("orderMoney")!=null?list.get(i-1).get("orderMoney").toString():"";
	    	    if(StringUtils.isNotBlank(totalMoney)){
//	    	    	orderTotalMoneyCell.setCellValue(MoneyNumberFormat.getThousandsByRegex(totalMoney,2));
	    	    	orderTotalMoneyCell.setCellValue(Double.valueOf(totalMoney));
	    	    }
	    	    	    
	    	    //已付金额
	    	    Cell hasPayedCurrency = dataRow.createCell(16);
	    	    hasPayedCurrency.setCellStyle(dataStyle);
	    	    hasPayedCurrency.setCellValue("¥");
	    	    
	    	    Cell hasPayedCell = dataRow.createCell(17);
	    	    hasPayedCell.setCellStyle(numberStyle);
	    	    if(StringUtils.isNotBlank(totalMoney)){
//	    	    	hasPayedCell.setCellValue(MoneyNumberFormat.getThousandsByRegex(totalMoney,2));
	    	    	hasPayedCell.setCellValue(Double.valueOf(totalMoney));
	    	    }
	    	    
	    	    //到账金额
	    	    Cell accountCellCurrency = dataRow.createCell(18);
	    	    accountCellCurrency.setCellStyle(dataStyle);
	    	    accountCellCurrency.setCellValue("¥");
	    	    
	    	    Cell accountCell = dataRow.createCell(19);
	    	    accountCell.setCellStyle(numberStyle);
	    	    String accountMoney = list.get(i-1).get("payMoney")!=null?list.get(i-1).get("payMoney").toString():"";
	    	    if(StringUtils.isNotBlank(accountMoney) && StringUtils.isNotBlank(confirm) && "1".equals(confirm)){
//	    	    	accountCell.setCellValue(MoneyNumberFormat.getThousandsByRegex(accountMoney,2));
	    	    	accountCell.setCellValue(Double.valueOf(accountMoney));
	    	    }    	   
	    	    
	    	    //支付方式
	    	    Cell payTypeCell = dataRow.createCell(20);
	    	    payTypeCell.setCellStyle(dataStyle);
	    	    String payType = list.get(i-1).get("payType")!=null?list.get(i-1).get("payType").toString():"";
	    		if(StringUtils.isNotBlank(payType)){
	    			if("1".equals(payType)){
	    				payTypeCell.setCellValue("支票");
	    			}else if("3".equals(payType) || "5".equals(payType)){
	    				payTypeCell.setCellValue("现金");
	    			}else if("4".equals(payType)){
	    				payTypeCell.setCellValue("汇款");
	    			}else if("6".equals(payType)){
	    				payTypeCell.setCellValue("银行转账");
	    			}else if("7".equals(payType)){
	    				payTypeCell.setCellValue("汇票");
	    			}else if("8".equals(payType) || "2".equals(payType)){
	    				payTypeCell.setCellValue("POS");
	    			}
	    		}
	    	    
	    	    //打印状态
	    	    Cell printStatusCell = dataRow.createCell(21);
	    	    printStatusCell.setCellStyle(dataStyle);
	    	    printStatusCell.setCellValue("");	    	    
	    	}
	    }
    	return workbook;
    }
    
    /**
     * 订单收款导出excel
     * @author zhaohaiming
     * updated by xianglei.dong
     * 2016-03-28 247需求单元格拆分
     * **/
    public static Workbook genOrderListForDZExel(List<Map<Object,Object>> list){
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	//title样式
    	HSSFFont headFont = workbook.createFont();
		HSSFCellStyle headStyle = workbook.createCellStyle();
    	headFont.setFontHeightInPoints((short)10);
    	headFont.setFontName("Courier New");
    	headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	headStyle.setFont(headFont);
	
    	//数据字体和样式
    	Font dataFont = workbook.createFont();
    	HSSFCellStyle dataStyle = workbook.createCellStyle();
    	dataFont.setFontHeightInPoints((short)10);
    	dataFont.setFontName("Courier New");
    	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	dataStyle.setFont(dataFont);
    	
		//数字显示格式
		HSSFCellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		numberStyle.setFont(dataFont);
		HSSFDataFormat format = workbook.createDataFormat();
		numberStyle.setDataFormat(format.getFormat("#,##0.00"));//设置单元类型
		
		//获取订单金额、累计达帐金额、已付金额、达账金额所含币种
		List<String> totalMoneyCurrency = Lists.newArrayList();
		List<String> accountedMoneyCurrency = Lists.newArrayList();
		List<String> payedMoneyCurrency = Lists.newArrayList();
		List<String> arrivedMoneyCurrency = Lists.newArrayList();
		List<String> noPayMoneyCurrency = Lists.newArrayList();//未收金额
		
		Map<String, List<Object[]>> totalMoneyMap = Maps.newHashMap();
		Map<String, List<Object[]>> accountedMoneyMap = Maps.newHashMap();
		Map<String, List<Object[]>> payedMoneyMap = Maps.newHashMap();
		Map<String, List<Object[]>> arrivedMoneyMap = Maps.newHashMap();
		Map<String, List<Object[]>> noPayMoneyMap = Maps.newHashMap();
		
		if(CollectionUtils.isNotEmpty(list)){
	    	for(int num =1;num<=list.size();num++){
	    		//团队类型
	    		String orderType = list.get(num-1).get("ordertype")!=null?list.get(num-1).get("ordertype").toString():"";
	    		//订单金额
	    		String totalmoneySerial = list.get(num-1).get("totalMoney")!=null?list.get(num-1).get("totalMoney").toString():"";
	    		if(StringUtils.isNoneBlank(totalmoneySerial) && StringUtils.isNoneBlank(orderType)){
	    			List<Object[]> objects = OrderCommonUtil.getMoneyAmountForBG(Integer.valueOf(orderType), totalmoneySerial, 2);
	    			totalMoneyMap.put(totalmoneySerial, objects);
	    			if(CollectionUtils.isNotEmpty(objects)) {
	    				for (int i = 0; i < objects.size(); i++) {
	    					Object[] amount = objects.get(i);
	    					if(!totalMoneyCurrency.contains(amount[0])) {
	    						totalMoneyCurrency.add((String) amount[0]);
	    					}
	    				}
	    			}
	    		}
	    		//累计达帐金额	
	    		String accountmoneySerial = list.get(num-1).get("accountedmoney")!=null?list.get(num-1).get("accountedmoney").toString():"";
	    		if(StringUtils.isNoneBlank(accountmoneySerial) && StringUtils.isNoneBlank(orderType)){
	    			List<Object[]> objects = OrderCommonUtil.getMoneyAmountForBG(Integer.valueOf(orderType), accountmoneySerial, 2);
	    			accountedMoneyMap.put(accountmoneySerial, objects);
	    			if(CollectionUtils.isNotEmpty(objects)) {
	    				for (int i = 0; i < objects.size(); i++) {
	    					Object[] amount = objects.get(i);
	    					if(!accountedMoneyCurrency.contains(amount[0])) {
	    						accountedMoneyCurrency.add((String) amount[0]);
	    					}	
	    				}
	    			}
	    		}
	    		//已付金额
	    		//-------------用于 538需求 差额的查询-------------
	    		Orderpay orderpay = orderPayService.findOrderpayById(Long.parseLong(list.get(num-1).get("payid").toString()));
	    		ReturnDifference returnDifference = null;
	    		Currency currency = null;
	    		if(StringUtils.isNotBlank(orderpay.getDifferenceUuid())){
	    			returnDifference = returnDifferenceService.getReturnDifferenceByUuid(orderpay.getDifferenceUuid());
	    			currency = currencyService.findById(returnDifference.getCurrencyId().longValue());
	    		}
	    		//---------------------end -------------------------
	    		String payMoneySerial = list.get(num-1).get("payedMoney")!=null?list.get(num-1).get("payedMoney").toString():"";
	    		if(StringUtils.isNoneBlank(payMoneySerial) && StringUtils.isNoneBlank(orderType)){
	    			List<Object[]> objects = OrderCommonUtil.getMoneyAmountForBG(Integer.valueOf(orderType), payMoneySerial, 2);
	    			payedMoneyMap.put(payMoneySerial, objects);
	    			if(CollectionUtils.isNotEmpty(objects)) {
	    				for (int i = 0; i < objects.size(); i++) {
	    					Object[] amount = objects.get(i);
	    					//538需求 判断是否存在差额，若存在差额 到账金额 加上差额
	    					if(StringUtils.isNotBlank(orderpay.getDifferenceUuid())){
	    						if(currency.getCurrencyMark().equals(amount[0].toString())){
	    							amount[1] = (new BigDecimal(amount[1].toString()).add(returnDifference.getReturnPrice()));
	    						}
	    					}
	    					if(!payedMoneyCurrency.contains(amount[0])) {
	    						payedMoneyCurrency.add((String) amount[0]);
	    					}	    					
	    				}
	    			}
	    		}
	    		//未收金额（友创国际）
	    		if (Context.SUPPLIER_UUID_YCGJ.equals(UserUtils.getUser().getCompany().getUuid())) {
					if (StringUtils.isNoneBlank(totalmoneySerial) && StringUtils.isNoneBlank(orderType)) {
						List<Object[]> objectsT = OrderCommonUtil.getMoneyAmountForBG(Integer.valueOf(orderType), totalmoneySerial, 2);
						List<Object[]> objectsP = Lists.newArrayList();
						List<Object[]> objectsN = Lists.newArrayList();
						if (StringUtils.isNoneBlank(payMoneySerial)) {
							objectsP = OrderCommonUtil.getMoneyAmountForBG(Integer.valueOf(orderType), payMoneySerial, 2);
							for (Object[] t : objectsT) {
								for (int i = 0; i < objectsP.size(); i++) {
									Object[] p = objectsP.get(i);
									if (t[0].equals(p[0])) {
										objectsN.add(new Object[]{t[0], Double.valueOf(t[1].toString()) - Double.valueOf(p[1].toString())});
										break;
									}
									if (i == objectsP.size() - 1) {
										objectsN.add(t);
									}
								}
							}
							noPayMoneyMap.put(totalmoneySerial, objectsN);
						} else {
							noPayMoneyMap.put(totalmoneySerial, objectsT);
						}
						
						if (CollectionUtils.isNotEmpty(objectsN)) {
							for (int i = 0; i < objectsN.size(); i++) {
								Object[] amount = objectsN.get(i);
								if (!noPayMoneyCurrency.contains(amount[0])) {
									noPayMoneyCurrency.add(amount[0].toString());
								}
							}
						}
					}
				}
				//到账金额
	    		String account = list.get(num-1).get("account")!=null?list.get(num-1).get("account").toString():"";
	    		if(StringUtils.isNotBlank(account)){
	    			//1表示达账，达账厚 达账金额=已付金额
	    			if("1".equals(account)){
	    				String accountMoneySerial = list.get(num-1).get("payedMoney")!=null?list.get(num-1).get("payedMoney").toString():"";
	    	    		if(StringUtils.isNoneBlank(accountMoneySerial) && StringUtils.isNoneBlank(orderType)){
	    	    			List<Object[]> objects = OrderCommonUtil.getMoneyAmountForBG(Integer.valueOf(orderType), accountMoneySerial, 2);
	    	    			arrivedMoneyMap.put(accountMoneySerial, objects);
	    	    			if(CollectionUtils.isNotEmpty(objects)) {
	    	    				for (int i = 0; i < objects.size(); i++) {
	    	    					Object[] amount = objects.get(i);
	    	    					//538需求 判断是否存在差额，若存在差额 到账金额 加上差额
	    	    					if(StringUtils.isNotBlank(orderpay.getDifferenceUuid())){
	    	    						if(currency.getCurrencyMark().equals(amount[0].toString())){
			    							amount[1] = (new BigDecimal(amount[1].toString()).add(returnDifference.getReturnPrice()));
			    						}
	    	    					}
	    	    					if(!arrivedMoneyCurrency.contains(amount[0])) {
	    	    						arrivedMoneyCurrency.add((String) amount[0]);
	    	    					}
	    	    				}
	    	    			}
	    	    		}
	    			}
	    		}
	    		
	    	}
		}
		//对币种类型进行排序
		Collections.sort(totalMoneyCurrency);
		Collections.sort(accountedMoneyCurrency);
		Collections.sort(payedMoneyCurrency);
		Collections.sort(arrivedMoneyCurrency);
		Collections.sort(noPayMoneyCurrency);
		
		int totalMoneyCurrencySize = totalMoneyCurrency.size() == 0? 1:totalMoneyCurrency.size()*2;
		int accountedMoneyCurrencySize = accountedMoneyCurrency.size() == 0? 1:accountedMoneyCurrency.size()*2;
		int payedMoneyCurrencySize = payedMoneyCurrency.size() == 0? 1:payedMoneyCurrency.size()*2;
		int arrivedMoneyCurrencySize = arrivedMoneyCurrency.size() == 0? 1:arrivedMoneyCurrency.size()*2;
		int noPayMoneyCurrencySize = noPayMoneyCurrency.size() == 0 ? 1 : noPayMoneyCurrency.size()*2;
		
    	String[] title = null;
    	// bug 15803 友创国际添加未收金额一列   modify by wangyang
		if(Context.SUPPLIER_UUID_YCGJ.equals(UserUtils.getUser().getCompany().getUuid())){
    		title = new String[]{"序号","收款日期","银行到账日期","订单号","团队类型","团号","产品名称","计调","销售","下单人","渠道名称","来款单位","收款银行","订单状态","订单金额","未收金额","累计达帐金额","已收金额","到账金额","收款方式","打印确认"};
    	}else{
    		title = new String[]{"序号","收款日期","银行到账日期","订单号","团队类型","团号","产品名称","计调","销售","下单人","渠道名称","来款单位","收款银行","订单状态","订单金额","累计达帐金额","已收金额","到账金额","收款方式","打印确认"};
    	}
		
    	HSSFSheet sheet = workbook.createSheet();
    	sheet.setDefaultColumnWidth(15);// 设置单元格宽度
    	Row titleRow = sheet.createRow(0);
    	//创建标题,前十四个定项
    	for(int i=0; i<14; i++) {
    		Cell headCell = titleRow.createCell(i);
    		headCell.setCellValue(title[i]);
        	headCell.setCellStyle(headStyle);
    	}
    	
    	Integer titleIndex = 14;
    	//订单金额
    	int columIndex = 14;
    	for(int i=0; i<totalMoneyCurrencySize; i++) {
        	Cell headCell = titleRow.createCell(columIndex+i);
        	headCell.setCellValue(title[titleIndex]);
    		headCell.setCellStyle(headStyle);
    		if(totalMoneyCurrencySize>1 && 0==i%2 ) {
    			sheet.setColumnWidth(columIndex+i, 5*256);
    		}
        } 
    	//合并订单金额单元格
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex+totalMoneyCurrencySize-1));
    	titleIndex++;
    	
    	if (Context.SUPPLIER_UUID_YCGJ.equals(UserUtils.getUser().getCompany().getUuid())) {
			columIndex += totalMoneyCurrencySize;
			for (int i = 0; i < totalMoneyCurrencySize; i++) {
				Cell headCell = titleRow.createCell(columIndex+i);
	        	headCell.setCellValue(title[titleIndex]);
	    		headCell.setCellStyle(headStyle);
	    		if(totalMoneyCurrencySize>1 && 0==i%2 ) {
	    			sheet.setColumnWidth(columIndex+i, 5*256);
	    		}
			}
			sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex + noPayMoneyCurrencySize - 1));
			titleIndex++;
		}
		//累计达帐金额
    	columIndex += totalMoneyCurrencySize;
    	for(int i=0; i<accountedMoneyCurrencySize; i++) {
        	Cell headCell = titleRow.createCell(columIndex+i);
        	headCell.setCellValue(title[titleIndex]);
    		headCell.setCellStyle(headStyle);
    		if(accountedMoneyCurrencySize>1 && 0==i%2) {
    			sheet.setColumnWidth(columIndex+i, 5*256);
    		}
        }
    	//合并累计达帐金额单元格
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex+accountedMoneyCurrencySize-1));
    	titleIndex++;
    	
    	//已付金额
    	columIndex += accountedMoneyCurrencySize;
    	for(int i=0; i<payedMoneyCurrencySize; i++) {
        	Cell headCell = titleRow.createCell(columIndex+i);
        	headCell.setCellValue(title[titleIndex]);
    		headCell.setCellStyle(headStyle);
    		if(payedMoneyCurrencySize>1 && 0==i%2) {
    			sheet.setColumnWidth(columIndex+i, 5*256);
    		}
        }
    	//合并已付金额列
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex+payedMoneyCurrencySize-1));
    	titleIndex++;
    	
    	//到账金额
    	columIndex += payedMoneyCurrencySize;
    	for(int i=0; i<arrivedMoneyCurrencySize; i++) {
        	Cell headCell = titleRow.createCell(columIndex+i);
        	headCell.setCellValue(title[titleIndex]);
    		headCell.setCellStyle(headStyle);
    		if(arrivedMoneyCurrencySize>1 && 0==i%2) {
    			sheet.setColumnWidth(columIndex+i, 5*256);
    		}
        }
    	//合并已付金额列
    	sheet.addMergedRegion(createCellRangeAddress(0, 0, columIndex, columIndex+arrivedMoneyCurrencySize-1));
    	titleIndex++;
    	
    	//支付方式
    	columIndex += arrivedMoneyCurrencySize;
    	Cell payedTypeCell = titleRow.createCell(columIndex);
    	payedTypeCell.setCellValue(title[titleIndex]);
    	payedTypeCell.setCellStyle(headStyle);
    	titleIndex++;
    	//打印确认
    	Cell printConfirmCell = titleRow.createCell(columIndex+1);
    	printConfirmCell.setCellValue(title[titleIndex]);
    	printConfirmCell.setCellStyle(headStyle);
    	    	  	
    	//填充Excel表
    	if(CollectionUtils.isNotEmpty(list)){
	    	for(int num =1;num<=list.size();num++){
	    		int rownum = num;  //记录行
	    		int colnum = 0;    //记录列
	    		
	    		HSSFRow dataRow = sheet.createRow(rownum);
	    		//序号
	    		Cell cell0 = dataRow.createCell(colnum++);
	    		cell0.setCellStyle(dataStyle);
	    		cell0.setCellValue(num);
	    		
	    		//付款日期
	    		Cell cell1 = dataRow.createCell(colnum++);
	    		cell1.setCellStyle(dataStyle);
	    		cell1.setCellValue(list.get(num-1).get("createDate") != null?DateUtils.formatCustomDate((Date)(list.get(num-1).get("createDate")), DateUtils.DATE_PATTERN_YYYY_MM_DD):"");
	    		
	    		//银行到账日期
	    		Cell accountDateCell = dataRow.createCell(colnum++);
	    		accountDateCell.setCellStyle(dataStyle);
	    		String isAsAccount = list.get(num-1).get("account")!=null? list.get(num-1).get("account").toString():"";
	    		if(StringUtils.isNotBlank(isAsAccount)&&isAsAccount.equals("1")) {
	    			accountDateCell.setCellValue(list.get(num-1).get("accountDate") != null?DateUtils.formatCustomDate((Date)(list.get(num-1).get("accountDate")), DateUtils.DATE_PATTERN_YYYY_MM_DD):"");
	    		}
	    		
	    		//订单号
	    		Cell orderNumCell = dataRow.createCell(colnum++);
	    		orderNumCell.setCellStyle(dataStyle);
	    		orderNumCell.setCellValue(list.get(num-1).get("orderNum")!=null?list.get(num-1).get("orderNum").toString():"");
	    		
	    		//团队类型
	    		Cell orderTypeCell = dataRow.createCell(colnum++);
	    		orderTypeCell.setCellStyle(dataStyle);
	    		String orderType = list.get(num-1).get("ordertype")!=null?list.get(num-1).get("ordertype").toString():"";
	    		if(StringUtils.isNotBlank(orderType)){
	    			orderTypeCell.setCellValue(DictUtils.getDictLabel(orderType, "order_type", ""));
	    		}else{
	    			orderTypeCell.setCellValue("");
	    		}
	    		
	    		//团号
	    		Cell groupCodeCell = dataRow.createCell(colnum++);
	    		groupCodeCell.setCellStyle(dataStyle);
	    		groupCodeCell.setCellValue(list.get(num-1).get("groupCode")!=null?list.get(num-1).get("groupCode").toString():"");
	    		
	    		//产品名称
	    		Cell productNameCell = dataRow.createCell(colnum++);
	    		productNameCell.setCellStyle(dataStyle);
	    		productNameCell.setCellValue(list.get(num-1).get("acitivityName")!=null?list.get(num-1).get("acitivityName").toString():"");
	    		
	    		//计调
	    		Cell jdCell = dataRow.createCell(colnum++);
	    		jdCell.setCellStyle(dataStyle);
	    		String jdId = list.get(num-1).get("opCreateBy")!=null?list.get(num-1).get("opCreateBy").toString():"";
	    		if(StringUtils.isNotBlank(jdId)){
	    			jdCell.setCellValue(UserUtils.getUser(jdId).getName());
	    		}else{
	    			
	    		}
	    		
	    		//销售  下单人
	    		if(Context.ORDER_TYPE_HOTEL.equals(orderType) || Context.ORDER_TYPE_ISLAND.equals(orderType)){
	    			//销售
	        		Cell salerCell = dataRow.createCell(colnum++);
	        		salerCell.setCellStyle(dataStyle);
	        		salerCell.setCellValue(list.get(num-1).get("createUserName")!=null?list.get(num-1).get("createUserName").toString():"");
	        		//下单人
	        		Cell bookPersonCell = dataRow.createCell(colnum++);
	        		bookPersonCell.setCellValue("");			
	    		}else{
		    		//销售
		    		Cell salerCell = dataRow.createCell(colnum++);
		    		salerCell.setCellStyle(dataStyle);
		    		salerCell.setCellValue(list.get(num-1).get("saler")!=null?list.get(num-1).get("saler").toString():"");
		    		//下单人
		    		Cell bookPersonCell = dataRow.createCell(colnum++);
		    		bookPersonCell.setCellStyle(dataStyle);
		    		bookPersonCell.setCellValue(list.get(num-1).get("creator")!=null?list.get(num-1).get("creator").toString():"");
		    	}
	    		
	    		//渠道名称
	    		Cell agentCell = dataRow.createCell(colnum++);
	    		agentCell.setCellStyle(dataStyle);
	    		agentCell.setCellValue(list.get(num-1).get("orderCompanyName")!=null?list.get(num-1).get("orderCompanyName").toString():"");
	    		
	    		//来款单位
	    		Cell payeeCell = dataRow.createCell(colnum++);
	    		payeeCell.setCellStyle(dataStyle);
	    		payeeCell.setCellValue(list.get(num-1).get("payerName")!=null?list.get(num-1).get("payerName").toString():"");
	    		
	    		//收款行
	    		Cell toBankNnameCell = dataRow.createCell(colnum++);
	    		toBankNnameCell.setCellStyle(dataStyle);
	    		toBankNnameCell.setCellValue(list.get(num-1).get("toBankNname")!=null?list.get(num-1).get("toBankNname").toString():"");
	    		
	    		//订单状态
	    		String payStatus = list.get(num-1).get("payStatus")!=null?list.get(num-1).get("payStatus").toString():"";
	    		if(Context.ORDER_TYPE_HOTEL.equals(orderType) || Context.ORDER_TYPE_ISLAND.equals(orderType)){
	    			//订单状态
	        		Cell orderStatusCell = dataRow.createCell(colnum++);
	        		orderStatusCell.setCellStyle(dataStyle);
	        		if("1".equals(payStatus)){
	        			orderStatusCell.setCellValue("待确认报名");
	        		}else if("2".equals(payStatus)){
	        			orderStatusCell.setCellValue("已确认报名");
	        		}
	    		}else{
	    			//订单状态
	        		Cell orderStatusCell = dataRow.createCell(colnum++);
	        		orderStatusCell.setCellStyle(dataStyle);
	        		if(StringUtils.isNotBlank(payStatus)){
	        			orderStatusCell.setCellValue(DictUtils.getDictLabel(payStatus, "order_pay_status", ""));
	        		}
	    		}
	    		
	    		//订单金额
	    		int totalmoneyIndex = colnum;                     //记录订单金额填充开始位置
	    		if(totalMoneyCurrency.size() != 0) {
	    			for(int i=0; i<totalMoneyCurrency.size()*2; i++) {     //将totalMoneyCurrency中的货币类型填充至单元格中
		    			HSSFCell orderTotalMoneyCell = dataRow.createCell(colnum++);   		
			    		if(1 == (i+1)%2) {
			    			orderTotalMoneyCell.setCellStyle(dataStyle);
			    			orderTotalMoneyCell.setCellValue(totalMoneyCurrency.get(i/2));
			    		}
			    		else {
			    			orderTotalMoneyCell.setCellStyle(numberStyle);
			    		}
		    		}
		    		//填充货币金额
		    		String totalmoneySerial = list.get(num-1).get("totalMoney")!=null?list.get(num-1).get("totalMoney").toString():"";
		    		if(totalMoneyMap.containsKey(totalmoneySerial)) {
		    			List<Object[]> totalMoneyList = totalMoneyMap.get(totalmoneySerial);
			    		for(int i=0; i<totalMoneyList.size(); i++) {
			    			Object[] objects = totalMoneyList.get(i);
			    			int index = totalMoneyCurrency.indexOf(objects[0]);
			    			dataRow.getCell(totalmoneyIndex+index*2+1).setCellValue(Double.valueOf(objects[1].toString()));
			    		}
		    		}
	    		} else {  //若没有，产生单一空行
	    			HSSFCell orderTotalMoneyCell = dataRow.createCell(colnum++);
	    			orderTotalMoneyCell.setCellStyle(dataStyle);
	    			orderTotalMoneyCell.setCellValue("");
	    		}
	    		
	    		if (UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_YCGJ)) {
	    			int noPayMoneyIndex = colnum;                     //记录订单金额填充开始位置
		    		if(noPayMoneyCurrency.size() != 0) {
		    			for(int i = 0; i < noPayMoneyCurrency.size()*2; i++) {     //将totalMoneyCurrency中的货币类型填充至单元格中
			    			HSSFCell orderNoPayMoneyCell = dataRow.createCell(colnum++);   		
				    		if(1 == (i+1)%2) {
				    			orderNoPayMoneyCell.setCellStyle(dataStyle);
				    			orderNoPayMoneyCell.setCellValue(noPayMoneyCurrency.get(i/2));
				    		}
				    		else {
				    			orderNoPayMoneyCell.setCellStyle(numberStyle);
				    		}
			    		}
			    		//填充货币金额
			    		String totalmoneySerial = list.get(num-1).get("totalMoney")!=null?list.get(num-1).get("totalMoney").toString():"";
			    		if (noPayMoneyMap.containsKey(totalmoneySerial)) {
			    			List<Object[]> noPayMoneyList = noPayMoneyMap.get(totalmoneySerial);
			    			for (int i = 0; i < noPayMoneyList.size(); i++) {
			    				Object[] objects = noPayMoneyList.get(i);
			    				int index = noPayMoneyCurrency.indexOf(objects[0]);
			    				dataRow.getCell(noPayMoneyIndex + index*2 + 1).setCellValue(Double.valueOf(objects[1].toString()));
			    			}
			    		}
		    		} else {  //若没有，产生单一空行
		    			HSSFCell orderTotalMoneyCell = dataRow.createCell(colnum++);
		    			orderTotalMoneyCell.setCellStyle(dataStyle);
		    			orderTotalMoneyCell.setCellValue("");
		    		}
	    		}
	    			       		
	    		//累计达帐金额
	    		int accountedmoneyIndex = colnum;                     //记录累计达帐金额填充开始位置
	    		if(accountedMoneyCurrency.size() != 0) {
	    			for(int i=0; i<accountedMoneyCurrency.size()*2; i++) {     //将accountedMoneyCurrency中的货币类型填充至单元格中
		    			HSSFCell hasAccountToalCell = dataRow.createCell(colnum++);
			    		if(1 == (i+1)%2) {
			    			hasAccountToalCell.setCellStyle(dataStyle);
			    			hasAccountToalCell.setCellValue(accountedMoneyCurrency.get(i/2));
			    		}else {
			    			hasAccountToalCell.setCellStyle(numberStyle);
			    		}
		    		}
		    		//填充货币金额
		    		String accountmoneySerial = list.get(num-1).get("accountedmoney")!=null?list.get(num-1).get("accountedmoney").toString():"";
		    		if(accountedMoneyMap.containsKey(accountmoneySerial)) {
		    			List<Object[]> accountedMoneyList = accountedMoneyMap.get(accountmoneySerial);
			    		for(int i=0; i<accountedMoneyList.size(); i++) {
			    			Object[] objects = accountedMoneyList.get(i);
			    			int index = accountedMoneyCurrency.indexOf(objects[0]);
			    			dataRow.getCell(accountedmoneyIndex+index*2+1).setCellValue(Double.valueOf(objects[1].toString()));
			    		}
		    		}
	    		} else {
	    			HSSFCell hasAccountToalCell = dataRow.createCell(colnum++);
	    			hasAccountToalCell.setCellStyle(dataStyle);
	    			hasAccountToalCell.setCellValue("");
	    		}
	    				
	    		//已付金额
	    		int payedmoneyIndex = colnum;                     //记录已付金额填充开始位置
	    		if(payedMoneyCurrency.size() != 0) {
	    			for(int i=0; i<payedMoneyCurrency.size()*2; i++) {     //将payedMoneyCurrency中的货币类型填充至单元格中
		    			HSSFCell orderPayMoneyCell = dataRow.createCell(colnum++);
			    		if(1 == (i+1)%2) {
			    			orderPayMoneyCell.setCellStyle(dataStyle);
			    			orderPayMoneyCell.setCellValue(payedMoneyCurrency.get(i/2));
			    		}else {
			    			orderPayMoneyCell.setCellStyle(numberStyle);
			    		}
		    		}
		    		//填充货币金额
		    		String payMoneySerial = list.get(num-1).get("payedMoney")!=null?list.get(num-1).get("payedMoney").toString():"";
		    		if(payedMoneyMap.containsKey(payMoneySerial)) {
		    			List<Object[]> payedMoneyList = payedMoneyMap.get(payMoneySerial);
			    		for(int i=0; i<payedMoneyList.size(); i++) {
			    			Object[] objects = payedMoneyList.get(i);
			    			int index = payedMoneyCurrency.indexOf(objects[0]);
			    			dataRow.getCell(payedmoneyIndex+index*2+1).setCellValue(Double.valueOf(objects[1].toString()));
			    		}
		    		}
	    		} else {
	    			HSSFCell orderPayMoneyCell = dataRow.createCell(colnum++);
	    			orderPayMoneyCell.setCellStyle(dataStyle);
	    			orderPayMoneyCell.setCellValue("");
	    		}
	    		    		
	    		//到账金额
	    		int arrivedmoneyIndex = colnum;                     //记录已付金额填充开始位置
	    		if(arrivedMoneyCurrency.size() != 0) {
	    			for(int i=0; i<arrivedMoneyCurrency.size()*2; i++) {     //将arrivedMoneyCurrency中的货币类型填充至单元格中
		    			HSSFCell hasArrivedCell = dataRow.createCell(colnum++);	    			
			    		if(1 == (i+1)%2) {
			    			hasArrivedCell.setCellStyle(dataStyle);
			    			hasArrivedCell.setCellValue(payedMoneyCurrency.get(i/2));
			    		} else {
			    			hasArrivedCell.setCellStyle(numberStyle);
			    		}
		    		}
		    		//填充货币金额
		    		String arrivedMoneySerial = list.get(num-1).get("payedMoney")!=null?list.get(num-1).get("payedMoney").toString():"";
		    		if(arrivedMoneyMap.containsKey(arrivedMoneySerial)) {
		    			List<Object[]> arrivedMoneyList = arrivedMoneyMap.get(arrivedMoneySerial);
		    			for(int i=0; i<arrivedMoneyList.size(); i++) {
			    			Object[] objects = arrivedMoneyList.get(i);
			    			int index = arrivedMoneyCurrency.indexOf(objects[0]);
			    			dataRow.getCell(arrivedmoneyIndex+index*2+1).setCellValue(Double.valueOf(objects[1].toString()));
			    		}
		    		}
	    		} else {
	    			HSSFCell hasArrivedCell = dataRow.createCell(colnum++);	 
	    			hasArrivedCell.setCellStyle(dataStyle);
	    			hasArrivedCell.setCellValue("");
	    		}
	    		  		    		
	    		//支付方式
	    		Cell payTypeCell= dataRow.createCell(colnum++);
	    		payTypeCell.setCellStyle(dataStyle);
	    		String payType = list.get(num-1).get("payType")!=null?list.get(num-1).get("payType").toString():"";
	    		if(StringUtils.isNotBlank(payType)){
	    			if("1".equals(payType)){
	    				payTypeCell.setCellValue("支票");
	    			}else if("3".equals(payType) || "5".equals(payType)){
	    				payTypeCell.setCellValue("现金");
	    			}else if("4".equals(payType)){
	    				payTypeCell.setCellValue("汇款");
	    			}else if("6".equals(payType)){
	    				payTypeCell.setCellValue("银行转账");
	    			}else if("7".equals(payType)){
	    				payTypeCell.setCellValue("汇票");
	    			}else if("8".equals(payType) || "2".equals(payType)){
	    				payTypeCell.setCellValue("POS");
	    			}else if("9".equals(payType)){
	    				payTypeCell.setCellValue("因公支付宝");
	    			}
	    		}
	    		
	    		//打印状态
	    		Cell printStatusCell = dataRow.createCell(colnum++);
	    		printStatusCell.setCellStyle(dataStyle);
	    		String printFlag = list.get(num-1).get("printFlag")!=null?list.get(num-1).get("printFlag").toString():"";
	    		if(StringUtils.isNotBlank(printFlag)){
	    			if("0".equals(printFlag)){
	    				printStatusCell.setCellValue("未打印");
	    			}else if("1".equals(printFlag)){
	    				printStatusCell.setCellValue("已打印");
	    			}
	    		}
	    	}
	    }
    	return workbook;
    }
    
    public static CellRangeAddress createCellRangeAddress(int firstRow,int lastRow,int firstCol,int lastCol){
    	return new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
    }
    public static void unionCell(HSSFSheet sheet,int firstRow,int lastRow,int firstCol,int lastCol){
    	sheet.addMergedRegion(createCellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

	//下载统计客户发布产品数量的excel.
	public static Workbook createProductCountWB(List<ProductCount> result){
		String[] head = {"公司名称","产品数量"};
		Workbook wb = new HSSFWorkbook();

		Font headFont = wb.createFont();
		headFont.setFontHeightInPoints((short)10);
		headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle headStyle = wb.createCellStyle();
		headStyle.setFont(headFont);
		headStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headStyle.setBorderTop(CellStyle.BORDER_THIN);
		headStyle.setBorderRight(CellStyle.BORDER_THIN);
		headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//LAVENDER
		headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		CellStyle commonStyle = wb.createCellStyle();
		commonStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		commonStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		commonStyle.setWrapText(true);

		Sheet sheet = wb.createSheet();
		Row headRow = sheet.createRow(0);
		for (int i = 0; i < head.length; i++) {
			Cell c1 = headRow.createCell(i);
			c1.setCellValue(head[i]);
			c1.setCellStyle(headStyle);
			sheet.setColumnWidth(i,5800);
		}

		Integer rowIdx = 1;
		for (ProductCount productCount : result) {
			Row contentRow = sheet.createRow(rowIdx);
			Cell c1 = contentRow.createCell(0);
			Cell c2 = contentRow.createCell(1);
			c1.setCellValue(productCount.getCompanyName());
			c1.setCellStyle(commonStyle);
			c2.setCellValue(productCount.getProductNum());
			c2.setCellStyle(commonStyle);
			rowIdx++;
		}
		return wb;
	}
}
