package com.trekiz.admin.modules.finance;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.mtourfinance.util.ExcelUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务模块工具类
 */
public class FinanceUtils {

    public static final String AGENT_NAME = "非签约渠道";
    public static final String AGENT_NAME_WEIQIAN = "未签";
    public static final String AGENT_NAME_ZHIKE = "直客";
    /**
     * 计算天数，仅使用于预报单和结算单
     * @param orderType             产品类型
     * @param groupCloseDate        结束日期
     * @param groupOpenDate         开始日期
     * @param activityDuration      天数
     * @return
     * @throws Exception
     */
    public static String getDays(Integer orderType, String groupCloseDate, String groupOpenDate, String activityDuration){
        String days = "";
        if(orderType == Context.ORDER_TYPE_QZ){//签证为空
            days = "";
        }else if(orderType == Context.ORDER_TYPE_JP){
            if(StringUtils.isEmpty(groupCloseDate) || StringUtils.isEmpty(groupOpenDate)){
                days = "1";
            }else{
                long endDate = DateUtils.string2Date(groupCloseDate).getTime();
                long startDate = DateUtils.string2Date(groupOpenDate).getTime();
                long day = (endDate - startDate)/(24*60*60*1000);
                //0470 天马运通天数可以是负数，2016.07.06
                if(day>1 || Context.SUPPLIER_UUID_TMYT.equals(UserUtils.getUser().getCompany().getUuid())){
                    days = String.valueOf(day);
                }else{
                    days = "1";
                }
            }
        }else{
            days = activityDuration;
        }
        return days;
    }

    /**
     * 如果对象时空的则返回空字符串
     * @param obj
     * @return
     * @author  shijun.liu
     * @date    2016.07.13
     */
    public static String blankReturnEmpty(Object obj){
        return StringUtils.blankReturnEmpty(obj);
    }

    /**
     * 下载销售人员业绩表的Excel表。 yudong.xu 2016.7.18
     */
    public static Workbook downloadSalesPerformance(List<Map<String,Object>> data, Map<Object,BigDecimal[]> sumInfo,String title){
        String[] head={"序号","部门","销售人员姓名","产品名称","团号","非签约渠道","签约渠道","收款金额","人数","平均每人成本","毛利","备注"};
        Workbook wb = new HSSFWorkbook();
        Map<String,CellStyle> styles = ExcelUtils.createStyles(wb);
        Sheet sheet = wb.createSheet();
        //标题
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,head.length-1));
        Row r0 = sheet.createRow(0);
        ExcelUtils.createCell(r0,0,title,styles.get("titleStyle1"));
        createHead(head,sheet,styles.get("headStyle1"),2);
        sheet.createFreezePane(0,4,0,4);
        Map<String,Object> sumMap = new HashMap<>();
        sumMap.put("count",1);
        sumMap.put("personNum",new BigDecimal(0));
        sumMap.put("costPerPerson",new BigDecimal(0));
        Integer rowIdx = 4;
        for (Map<String, Object> map : data) {
            Row row = sheet.createRow(rowIdx);
            fillingRow(map,row,styles,sumMap);
            rowIdx++;
        }

        StringBuilder moneySum = new StringBuilder();
        StringBuilder profitSum = new StringBuilder();
        if (sumInfo != null && sumInfo.size() > 0){
            for (Map.Entry<Object, BigDecimal[]> entry : sumInfo.entrySet()) {
                String money = MoneyNumberFormat.getRoundMoney(entry.getValue()[0],2,BigDecimal.ROUND_HALF_UP);
                String profit = MoneyNumberFormat.getRoundMoney(entry.getValue()[1],2,BigDecimal.ROUND_HALF_UP);
                moneySum.append(entry.getKey()).append(money).append("+");
                profitSum.append(entry.getKey()).append(profit).append("+");
            }
            moneySum.deleteCharAt(moneySum.length()-1);
            profitSum.deleteCharAt(profitSum.length()-1);
        }

        Row totalRow = sheet.createRow(rowIdx);
        for (int i = 0; i < head.length; i++) {
            totalRow.createCell(i).setCellStyle(styles.get("commonCR"));
        }
        Cell c0 = totalRow.getCell(0);
        c0.setCellStyle(styles.get("titleStyle"));
        c0.setCellValue("合计");
        sheet.addMergedRegion(new CellRangeAddress(rowIdx,rowIdx,0,3));

        totalRow.getCell(7).setCellValue(moneySum.toString());
        Cell c8 = totalRow.getCell(8);
        c8.setCellStyle(styles.get("contentStyle"));
        c8.setCellValue(((BigDecimal)sumMap.get("personNum")).intValue());
        String costSum = MoneyNumberFormat.getRoundMoney((BigDecimal)sumMap.get("costPerPerson"),2,BigDecimal.ROUND_HALF_UP);
        totalRow.getCell(9).setCellValue("￥"+costSum);
        totalRow.getCell(10).setCellValue(profitSum.toString());
        return wb;
    }

    private static void createHead(String[] head,Sheet sheet,CellStyle style,int rowNum){
        Row row1 = sheet.createRow(rowNum);
        Row row2 = sheet.createRow(++rowNum);
        for (int i = 0; i < head.length; i++) {
            Cell cell1 = row1.createCell(i);
            Cell cell2 = row2.createCell(i);
            cell1.setCellStyle(style);
            cell2.setCellStyle(style);
            if (i == 5 || i == 6){
                cell2.setCellValue(head[i]);
            } else {
                cell1.setCellValue(head[i]);
                sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum,i,i));
            }
            sheet.setColumnWidth(i,5800);
        }
        Cell c5 = row1.getCell(5);
        c5.setCellValue("客户来源");
        sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,5,6));
        sheet.setColumnWidth(0,4000);
        sheet.setColumnWidth(8,4000);
    }


    private static void fillingRow(Map<String,Object> map,Row row,Map<String, CellStyle> styles,Map<String,Object> sumMap){
        CellStyle commonCR = styles.get("commonCR");
        CellStyle contentStyle = styles.get("contentStyle");

        Integer count = (Integer)sumMap.get("count");
        Cell c0 = row.createCell(0);
        c0.setCellStyle(contentStyle);
        c0.setCellValue(count);
        sumMap.put("count",++count);

        Cell c1 = row.createCell(1);
        c1.setCellStyle(contentStyle);
        c1.setCellValue((String) map.get("deptNames"));

        Cell c2 = row.createCell(2);
        c2.setCellStyle(contentStyle);
        c2.setCellValue((String) map.get("userName"));

        Cell c3 = row.createCell(3);
        c3.setCellStyle(contentStyle);
        c3.setCellValue((String) map.get("productName"));

        Cell c4 = row.createCell(4);
        c4.setCellStyle(contentStyle);
        c4.setCellValue((String) map.get("groupCode"));

        Cell c5 = row.createCell(5);
        c5.setCellStyle(contentStyle);
        c5.setCellValue((String) map.get("unAgentNames"));

        Cell c6 = row.createCell(6);
        c6.setCellStyle(contentStyle);
        c6.setCellValue((String) map.get("agentNames"));

        Cell c7 = row.createCell(7);
        c7.setCellStyle(commonCR);
        c7.setCellValue((String) map.get("accountedMoney"));

        BigDecimal personNum = (BigDecimal) map.get("personNum");
        Cell c8 = row.createCell(8);
        c8.setCellStyle(contentStyle);
        c8.setCellValue(personNum.intValue());
        personNum = personNum.add((BigDecimal)sumMap.get("personNum"));
        sumMap.put("personNum",personNum);

        Cell c9 = row.createCell(9);
        c9.setCellStyle(commonCR);
        String costPerPerson = (String)map.get("costPerPerson");
        c9.setCellValue(costPerPerson);

        if (costPerPerson.length() > 0){ // ￥5.30
            costPerPerson = costPerPerson.substring(1); // 去除￥
            BigDecimal cost = new BigDecimal(costPerPerson);
            cost = ((BigDecimal)sumMap.get("costPerPerson")).add(cost);
            sumMap.put("costPerPerson",cost);
        }

        Cell c10 = row.createCell(10);
        c10.setCellStyle(commonCR);
        c10.setCellValue((String) map.get("profit"));

        Cell c11 = row.createCell(11);
        c11.setCellStyle(contentStyle);
    }

}
