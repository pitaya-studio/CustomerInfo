package com.trekiz.admin.modules.statisticAnalysis.home.service.impl;


import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.statisticAnalysis.common.GetEachCycleFirstDay;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.StatisticAnalysisDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.StatisticAnalysisService;

import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by quauq on 2016/12/22.
 */
@Service
@Transactional
public class StatisticAnalysisServiceImpl implements StatisticAnalysisService {

    @Autowired
    private StatisticAnalysisDao statisticAnalysisDao;

    /**
     * 查询单位时间内的订单数
     * @param mapRequest
     * @return
     */
    public List findOrderNum(Map<String ,String> mapRequest) throws  Exception{

        //获取当前系统时间
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date(System.currentTimeMillis());
        //当前时间，用于结束循环
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = sdf.format(currentDate);
        //获取当前时间的小时值，用于遍历循环
        Calendar cr = Calendar.getInstance();
        cr.setTime(currentDate);
        int temp = cr.get(Calendar.HOUR_OF_DAY) + 1;
        //时间区间取值
        String timeRegion = null;
        List list = new ArrayList();
        //第一对比时间
        String firstTimeSlice = "";
        // 第二对比时间
        String secondTimeSlice = "";
        //第三对比时间
        String thirdTimeSlice = "";

        String num = "";
        String num2 = "";
        String num3 = "";

        if(StringUtils.isNotBlank(mapRequest.get("searchDate"))){  //搜索框不是自定义
            //今日
            if(Context.ORDER_DATA_STATISTICS_TODAY.equals(mapRequest.get("searchDate"))){
                //查询的维度为小时
                if ("h".equals(mapRequest.get("unit"))){
                    //查询时间段
                    String today = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
                    //获取昨日时间（2016-12-12）
                    String yesterday = GetEachCycleFirstDay.getYesterdayStartDate(currentDate);
                    //获取上周同期时间
                    String lastWeekSameDay = GetEachCycleFirstDay.getLastWeekSameDayDate(currentDate);
                    //查询本日各时间段对应的数据
                    for (int i=0; i<temp; i++){

                        if(i<10){
                            firstTimeSlice  = today + " 0" + i;
                            secondTimeSlice  = yesterday + " 0" + i;
                            thirdTimeSlice  = lastWeekSameDay + " 0" + i;
                            timeRegion = "0" + i + ":00";
                        }else {
                            firstTimeSlice = today + " " +  i;
                            secondTimeSlice = yesterday + " " +  i;
                            thirdTimeSlice  = lastWeekSameDay + " " + i;
                            timeRegion = i + ":00";
                        }
                        //查询并封装数据
                        findDataHour(mapRequest, list, timeRegion, firstTimeSlice, secondTimeSlice, thirdTimeSlice);
                    }
                }

            }else if (mapRequest.get("searchDate").equals(Context.ORDER_DATA_STATISTICS_WEEK)){ //本周

                //获取本周第一天的时间
                String beginDate = GetEachCycleFirstDay.getThisWeekFirstDayDate(currentDate);

                Date parse = sdf.parse(beginDate);
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(parse);
                int day = cr.get(Calendar.DAY_OF_MONTH) - startDate.get(Calendar.DAY_OF_MONTH);

                if (day <1){
                    if ("h".equals(mapRequest.get("unit"))){
                        String weekTime = null;
                        String lastWeekTime = null;
                        //查询维度是小时
                        for (;beginDate.compareTo(currentDateTime) <= 0;){ //遍历周内的每天

                            //获取上周同一天时间
                            String lastWeek = GetEachCycleFirstDay.getLastWeekSameDay(beginDate);
                            for (int i=0; i<temp; i++){
                                if(i<10){
                                    firstTimeSlice = beginDate + " 0" + i;
                                    secondTimeSlice = lastWeek + " 0" + i;
                                }else {
                                    firstTimeSlice = beginDate + " " + i;
                                    secondTimeSlice = lastWeek + " " + i;
                                }
                                timeRegion = firstTimeSlice.substring(5) + ":00";
                                //查询并封装数据
                                findDataHour(mapRequest, list, timeRegion, firstTimeSlice, secondTimeSlice, null);
                            }
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                        }
                    }
                } else { //本周  时间区间大于1天
                    if ("h".equals(mapRequest.get("unit"))){
                        //查询维度是小时
                        for (;beginDate.compareTo(currentDateTime) <= 0;){ //遍历周内的每天
                            //获取上周同一天时间
                            String lastWeek = GetEachCycleFirstDay.getLastWeekSameDay(beginDate);
                            if(beginDate.equals(currentDateTime)) {
                                for (int i = 0; i < temp; i++) {
                                    if (i < 10) {
                                        firstTimeSlice  = beginDate + " 0" + i;
                                        secondTimeSlice  = lastWeek + " 0" + i;
                                    } else {
                                        firstTimeSlice  = beginDate + " " + i;
                                        secondTimeSlice  = lastWeek + " " + i;
                                    }
                                    timeRegion = firstTimeSlice .substring(5) + ":00";
                                    //查询并封装数据
                                    findDataHour(mapRequest, list, timeRegion, firstTimeSlice, secondTimeSlice, null);
                                }
                            }else {
                                for (int i = 0; i < 24; i++) {
                                    if (i < 10) {
                                        firstTimeSlice = beginDate + " 0" + i;
                                        secondTimeSlice = lastWeek + " 0" + i;
                                    } else {
                                        firstTimeSlice = beginDate + " " + i;
                                        secondTimeSlice = lastWeek + " " + i;
                                    }
                                    timeRegion = firstTimeSlice .substring(5) + ":00";
                                    //查询并封装数据
                                    findDataHour(mapRequest, list, timeRegion, firstTimeSlice, secondTimeSlice, null);
                                }
                            }

                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                        }
                    }

                    //本周-----查询维度是天
                    if ("d".equals(mapRequest.get("unit"))){
                        for (;beginDate.compareTo(currentDateTime) <= 0;){ //遍历周内的每天
                            firstTimeSlice = beginDate;
                            //获取上周同一天时间
                            secondTimeSlice = GetEachCycleFirstDay.getLastWeekSameDay(beginDate);
                            timeRegion = beginDate.substring(5, 10);
                            //查询并封装数据
                            findDataDay(mapRequest, list, timeRegion, firstTimeSlice, secondTimeSlice, null);
                            //循环条件
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                        }
                    }
                }

            }else if (Context.ORDER_DATA_STATISTICS_MONTH.equals(mapRequest.get("searchDate"))){    //本月
                //获取本月第一天   格式：yyyy-MM-dd
                String beginDate = GetEachCycleFirstDay.getThisMonthFirst(currentDate);
                //获取前一月第一天
                String beginDate2 = GetEachCycleFirstDay.getFirstDayofLastMonthDate();
                //获取去年同月第一天时间
                String beginDate3 = GetEachCycleFirstDay.getFirstDateofLastYearMonth();

                Date parse = sdf.parse(beginDate);
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(parse);
                int day = cr.get(Calendar.DAY_OF_MONTH) - startDate.get(Calendar.DAY_OF_MONTH);
                if (day <1) {
                    //查询维度是小时
                    if ("h".equals(mapRequest.get("unit"))) {

                        for (int i = 0; i < temp; i++) {
                            if (i < 10) {
                                firstTimeSlice = beginDate + " 0" + i;
                                secondTimeSlice = beginDate2 + " 0" + i;
                                thirdTimeSlice = beginDate3 + " 0" + i;
                            } else {
                                firstTimeSlice = beginDate + " " + i;
                                secondTimeSlice = beginDate2 + " " + i;
                                thirdTimeSlice = beginDate3 + " " + i;
                            }
                            timeRegion = beginDate.substring(5) + ":00";
                            //查询并封装数据
                            findDataHour(mapRequest, list, timeRegion, firstTimeSlice, secondTimeSlice, thirdTimeSlice);
                        }
                    }
                }else if (day >= 1 && day < 8){

                    //查询维度是小时
                    if ("h".equals(mapRequest.get("unit"))) {
                        //查询时间不是当天
                        for (;beginDate.compareTo(currentDateTime) <= 0;) {
                            if (beginDate.compareTo(currentDateTime) < 0) {
                                for (int i = 0; i < 24; i++) {
                                    if (i < 10) {
                                        firstTimeSlice = beginDate + " 0" + i;
                                        secondTimeSlice = beginDate2 + " 0" + i;
                                        thirdTimeSlice = beginDate3 + " 0" + i;
                                    } else {
                                        firstTimeSlice = beginDate + " " + i;
                                        secondTimeSlice = beginDate2 + " " + i;
                                        thirdTimeSlice = beginDate3 + " " + i;
                                    }
                                    timeRegion = firstTimeSlice.substring(5) + ":00";
                                    //查询并封装数据
                                    findDataHour(mapRequest, list, timeRegion, firstTimeSlice, secondTimeSlice, thirdTimeSlice);
                                }
                            }
                            //查询时间为当天
                            if (beginDate.compareTo(currentDateTime) == 0) {
                                for (int i = 0; i < temp; i++) {
                                    if (i < 10) {
                                        firstTimeSlice = beginDate + " 0" + i;
                                        secondTimeSlice = beginDate2 + " 0" + i;
                                        thirdTimeSlice = beginDate3 + " 0" + i;
                                    } else {
                                        firstTimeSlice = beginDate + " " + i;
                                        secondTimeSlice = beginDate2 + " " + i;
                                        thirdTimeSlice = beginDate3 + " " + i;
                                    }
                                    timeRegion = firstTimeSlice.substring(5) + ":00";
                                    //查询并封装数据
                                    findDataHour(mapRequest, list, timeRegion, firstTimeSlice, secondTimeSlice, thirdTimeSlice);
                                }
                            }

                            //每次循环增加一天
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                            beginDate2 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate2, 1);
                            beginDate3 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate3, 1);
                        }
                    }

                    //查询维度为天
                    if ("d".equals(mapRequest.get("unit"))){

                        for (;beginDate.compareTo(currentDateTime) <= 0;){
                            timeRegion = beginDate.substring(5);

                            //查询并封装数据
                            findDataDay(mapRequest, list, timeRegion, beginDate, beginDate2, beginDate3);

                            //每次循环增加一天
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                            beginDate2 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate2, 1);
                            beginDate3 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate3, 1);
                        }
                    }
                }else if (day>= 8 && day < 15){
                    //查询维度为天
                    if ("d".equals(mapRequest.get("unit"))){

                        for (;beginDate.compareTo(currentDateTime) <= 0;){
                            timeRegion = beginDate.substring(5);

                            //查询并封装数据
                            findDataDay(mapRequest, list, timeRegion, beginDate, beginDate2, beginDate3);

                            //每次循环增加一天
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                            beginDate2 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate2, 1);
                            beginDate3 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate3, 1);
                        }
                    }
                }else {
                    //查询维度为天
                    if ("d".equals(mapRequest.get("unit"))) {

                        for (; beginDate.compareTo(currentDateTime) <= 0; ) {
                            timeRegion = beginDate.substring(5);

                            //查询并封装数据
                            findDataDay(mapRequest, list, timeRegion, beginDate, beginDate2, beginDate3);

                            //每次循环增加一天
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                            beginDate2 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate2, 1);
                            beginDate3 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate3, 1);
                        }
                    }

                    if (mapRequest.get("unit").equals("w")) {   //本月按周查询

                        for (; beginDate.compareTo(currentDateTime) <= 0; ) {

                            timeRegion = getTimeRegion2(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), currentDateTime);
                            //查询并封装数据

                            //查询订单数
                            if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findOrderNumByWeekAndMonth(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findOrderNumByWeekAndMonth(beginDate2, mapRequest);
                                num3 = statisticAnalysisDao.findOrderNumByWeekAndMonth(beginDate3, mapRequest);
                            }
                            //查询订单人数
                            if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findPersonNumByWeekAndMonth(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findPersonNumByWeekAndMonth(beginDate2, mapRequest);
                                num3 = statisticAnalysisDao.findPersonNumByWeekAndMonth(beginDate3, mapRequest);
                            }
                            //查询金额
                            if (Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findMoneyAmountByWeekAndMonth(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findMoneyAmountByWeekAndMonth(beginDate2, mapRequest);
                                num3 = statisticAnalysisDao.findMoneyAmountByWeekAndMonth(beginDate3, mapRequest);
                            }
                            //封装数据
                            weaveData(list, timeRegion, num, num2, num3);

                            beginDate = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate);
                            beginDate2 = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate2);
                            beginDate3 = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate3);
                        }
                    }
                }

            }else if(mapRequest.get("searchDate").equals(Context.ORDER_DATA_STATISTICS_YEAR)){ //查询区间为本年

                //获取本年的开始时间
                String beginDate = GetEachCycleFirstDay.getThisYearFirstDate(currentDate);
                //获取去年第一天时间
                String beginDate2 = GetEachCycleFirstDay.getLastYearFirstDate();
                //获取当前的日期
                Calendar c = Calendar.getInstance();
                c.setTime(sf.parse(beginDate + " 00:00:00"));
                long minTimeInMillis = c.getTimeInMillis();
                long timeInMillis = System.currentTimeMillis() - minTimeInMillis;
                Long day = timeInMillis/1000/3600/24;
                //null != map.get("beginTime") && null != map.get("lastTime")
                mapRequest.put("beginTime", beginDate);
                mapRequest.put("lastTime",currentDateTime);


                if (day <= 1){   //周期一天
                    String todayTime = "";
                    String lastYearSameDayTime = "";
                    //查询本日各时间段对应的订单数
                    for (int i=0; i<temp; i++){
                        if(i<10){
                            todayTime = currentDateTime + " 0" + i;
                            lastYearSameDayTime = beginDate2 + " 0" + i;
                            timeRegion = "0" + i + ":00";
                        }else {
                            todayTime = currentDateTime + " " + i;
                            lastYearSameDayTime = beginDate2 + " " + i;
                            timeRegion = i + ":00";
                        }
                        //查询并封装数据
                        findDataHour(mapRequest, list, timeRegion, todayTime, lastYearSameDayTime, null);
                    }
                }else if (day > 1 && day < 8){ //周期2-8天

                    if ("h".equals(mapRequest.get("unit"))){
                        //查询维度是小时
                        for (;!beginDate.equals(currentDateTime);){ //遍历周内的每天
                            if (beginDate.compareTo(currentDateTime) < 0){
                                for (int i=0; i<24; i++){

                                    String beginDateTime = "";
                                    String beginDateTime2 = "";
                                    if(i<10){
                                        beginDateTime = beginDate + " 0" + i;
                                        beginDateTime2 = beginDate2 + " 0" + i;
                                    }else {
                                        beginDateTime = beginDate + " " + i;
                                        beginDateTime2 = beginDate2 + " " + i;
                                    }
                                    timeRegion = beginDateTime.substring(5) + ":00";
                                    //查询并封装数据
                                    findDataHour(mapRequest, list, timeRegion, beginDateTime, beginDateTime2, null);

                                }
                            }else {
                                for (int i = 0; i < temp; i++) {

                                    String beginDateTime = "";
                                    String beginDateTime2 = "";
                                    if (i < 10) {
                                        beginDateTime = beginDate + " 0" + i;
                                        beginDateTime2 = beginDate2 + " 0" + i;
                                    } else {
                                        beginDateTime = beginDate + " " + i;
                                        beginDateTime2 = beginDate2 + " " + i;
                                    }
                                    timeRegion = beginDateTime.substring(5) + ":00";

                                    //查询并封装数据
                                    findDataHour(mapRequest, list, timeRegion, beginDateTime, beginDateTime2, null);
                                }
                            }
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                            beginDate2 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate2, 1);
                        }
                    }
                    //本年按天查询
                    if ("d".equals(mapRequest.get("unit"))){

                        //查询维度是天
                        for ( ;!beginDate.equals(currentDateTime); ){ //遍历周内的每天
                            timeRegion = beginDate.substring(5);

                            //查询并封装数据
                            findDataDay(mapRequest, list, timeRegion, beginDate, beginDate2, null);

                            //今年时间增加一天
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                            //去年时间增加一天
                            beginDate2 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate2, 1);
                        }
                    }

                }else if (day >= 8 && day < 14){    //周期9-14天

                    //查询维度是天
                    for (;beginDate.compareTo(currentDateTime) <= 0;){ //遍历周内的每天
                        timeRegion = beginDate.substring(5);

                        //查询并封装数据
                        findDataDay(mapRequest, list, timeRegion, beginDate, beginDate2, null);

                        //封装数据
                        weaveData(list, timeRegion, num, num2, num3);
                        //今年时间增加一天
                        beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                        //去年时间增加一天
                        beginDate2 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate2, 1);
                    }

                }else if (day >= 14 && day < 58){   //周期15-62天

                    //获取去年同一天时间
                    String lastYearSameDay = GetEachCycleFirstDay.getLastYearSameDate(beginDate);
                    if ("d".equals(mapRequest.get("unit"))){
                        //查询维度是天
                        for (;beginDate.compareTo(currentDateTime) <= 0;) { //遍历周内的每天
                            timeRegion = beginDate.substring(5);

                            //查询并封装数据
                            findDataDay(mapRequest, list, timeRegion, beginDate, beginDate2, null);

                            //今年时间增加一天
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                            //去年时间增加一天
                            beginDate2 = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate2, 1);
                        }
                    }

                    if ("w".equals(mapRequest.get("unit"))){

                        for (;beginDate.compareTo(currentDateTime) <= 0;){

                            timeRegion = getTimeRegion2(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), currentDateTime);
                            //查询订单数
                            if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findOrderNumByWeek(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findOrderNumByWeek(beginDate2, mapRequest);
                            }
                            //查询订单人数
                            if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findPersonNumByWeek(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findPersonNumByWeek(beginDate2, mapRequest);
                            }
                            //查询金额
                            if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findMoneyAmountByWeek(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findMoneyAmountByWeek(beginDate2, mapRequest);
                            }

                            //封装数据
                            weaveData(list, timeRegion, num, num2, num3);

                            beginDate = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate);
                            beginDate2 = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate2);
                        }
                    }

                }else {     //周期63天及以上

                    String lastYearSomeday = GetEachCycleFirstDay.getLastYearSameDate(beginDate);
                    if ("w".equals(mapRequest.get("unit"))){

                        for (;beginDate.compareTo(currentDateTime) <= 0;){
                            //timeRegion = 
                            timeRegion = getTimeRegion2(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), currentDateTime);
                            //查询订单数
                            if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findOrderNumByWeek(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findOrderNumByWeek(beginDate2, mapRequest);
                            }
                            //查询订单人数
                            if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findPersonNumByWeek(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findPersonNumByWeek(beginDate2, mapRequest);
                            }
                            //查询金额
                            if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findMoneyAmountByWeek(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findMoneyAmountByWeek(beginDate2, mapRequest);
                            }
                            //封装数据
                            weaveData(list, timeRegion, num, num2, num3);

                            //获取下周日期
                            beginDate = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate);
                            beginDate2 = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate2);
                        }
                    }

                    if ("m".equals(mapRequest.get("unit"))){
                        for ( ;beginDate.compareTo(currentDateTime) <= 0; ){
                            timeRegion = getTimeRegionMonth(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), currentDateTime);
                            //查询订单数
                            if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findOrderNumByMonth(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findOrderNumByMonth(beginDate2, mapRequest);
                            }
                            //查询订单人数
                            if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findPersonNumByMonth(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findPersonNumByMonth(beginDate2, mapRequest);
                            }
                            //查询金额
                            if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findMoneyAmountByMonth(beginDate, mapRequest);
                                num2 = statisticAnalysisDao.findMoneyAmountByMonth(beginDate2, mapRequest);
                            }
                            //封装数据
                            weaveData(list, timeRegion, num, num2, num3);
                            //循环到下月
                            beginDate = GetEachCycleFirstDay.getFirstDayofNextMonth(beginDate);
                            beginDate2 = GetEachCycleFirstDay.getFirstDayofNextMonth(beginDate2);
                        }
                    }
                }

            } else { //全部

                //获取开始时间
                String beginDate = statisticAnalysisDao.getTimeMin();
                String lastTime = statisticAnalysisDao.getTimeMax();//因需求更改，现已改为当前时间
                //求时间天数
                Long day = getDayNumber(beginDate, lastTime);

                if (day < 1){   //周期一天
                    String beginTime = null;
                    //判断时间处于哪个时间段
                    if ("h".equals(mapRequest.get("unit"))){

                        //查询本日各时间段对应的订单数
                        for (int i=0; i<temp; i++){
                            if(i<10){
                                beginTime = beginDate + " 0" + i;
                            }else {
                                beginTime = beginDate + " " + i;
                            }
                            timeRegion = beginTime.substring(11) + ":00";
                            //查询并封装数据
                            findDataHour(mapRequest, list, timeRegion, beginTime, null, null);
                        }
                    }

                }else if (day >= 1 && day < 8){ //周期2-8天
                    //查询维度
                    if ("h".equals(mapRequest.get("unit"))){

                        //查询维度是小时
                        if (mapRequest.get("unit").equals("h")){

                            for (;beginDate.compareTo(lastTime) <= 0;){ //遍历周内的每天
                                if (beginDate.compareTo(lastTime) < 0){
                                    for (int i=0; i<24; i++){
                                        if(i<10){
                                            beginDate = beginDate + " 0" + i;
                                        }else {
                                            beginDate = beginDate + " " + i;
                                        }
                                        timeRegion = beginDate.substring(5) + ":00";
                                        //查询并封装数据
                                        findDataHour(mapRequest, list, timeRegion, beginDate, null, null);
                                    }
                                }else {
                                    for (int i = 0; i < temp; i++) {
                                        if (i < 10) {
                                            beginDate = beginDate + " 0" + i;
                                        } else {
                                            beginDate = beginDate + " " + i;
                                        }
                                        timeRegion = beginDate.substring(5) + ":00";
                                        //查询并封装数据
                                        findDataHour(mapRequest, list, timeRegion, beginDate, null, null);
                                    }
                                }
                                //天数加1
                                beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                            }
                        }
                        //本年按天查询
                        if ("d".equals(mapRequest.get("unit"))){

                            //查询维度是天
                            for (;beginDate.compareTo(lastTime) <= 0;){ //遍历周内的每天
                                timeRegion = beginDate.substring(5);
                                //查询订单数
                                if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                    num = statisticAnalysisDao.findOrderNumByDay(beginDate, mapRequest);
                                }
                                //查询订单人数
                                if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                                    num = statisticAnalysisDao.findPersonNumByDay(beginDate, mapRequest);
                                }
                                //查询金额
                                if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                                    num = statisticAnalysisDao.findMoneyAmountByDay(beginDate, mapRequest);
                                }

                                //封装数据
                                weaveData(list, timeRegion, num, num2, num3);
                                //今年时间增加一天
                                beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                            }
                        }
                    }

                }else if (day >= 8 && day < 14){    //周期9-14天

                    //查询维度是天
                    for (;beginDate.compareTo(lastTime) <= 0;){ //遍历周内的每天
                        timeRegion = beginDate.substring(5);
                        //查询并封装数据
                        findDataDay(mapRequest, list, timeRegion, beginDate, null, null);

                        //今年时间增加一天
                        beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                    }

                }else if (day >= 14 && day < 59){   //周期15-59天

                    if ("d".equals(mapRequest.get("unit"))){
                        //查询维度是天
                        for (;beginDate.compareTo(lastTime) <= 0;) { //遍历周内的每天

                            //查询并封装数据
                            findDataDay(mapRequest, list, timeRegion, beginDate, null, null);

                            //今年时间增加一天
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                        }
                    }

                    if ("w".equals(mapRequest.get("unit"))){
                        //beginDate = firstTime;
                        for (;beginDate.compareTo(lastTime) <= 0;){
                            //timeRegion = beginDate.substring(5);
                            timeRegion = getTimeRegion2(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), lastTime);
                            //查询订单数
                            if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findOrderNumByWeek(beginDate, mapRequest);
                            }
                            //查询订单人数
                            if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findPersonNumByWeek(beginDate, mapRequest);
                            }
                            //查询金额
                            if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findMoneyAmountByWeek(beginDate, mapRequest);
                            }

                            //封装数据
                            weaveData(list, timeRegion, num, num2, num3);

                            beginDate = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate);
                        }
                    }
                }else if (day >= 59 && day < 365){     //周期58到365天

                    if ("w".equals(mapRequest.get("unit"))){

                        for (;beginDate.compareTo(lastTime) <= 0;){

                            timeRegion = getTimeRegion2(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), lastTime);
                            //查询订单数
                            if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findOrderNumByWeek(beginDate, mapRequest);
                            }
                            //查询订单人数
                            if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findPersonNumByWeek(beginDate, mapRequest);
                            }
                            //查询金额
                            if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findMoneyAmountByWeek(beginDate, mapRequest);
                            }
                            //封装数据
                            weaveData(list, timeRegion, num, num2, num3);

                            //获取下周日期
                            beginDate = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate);
                        }
                    }

                    if ("m".equals(mapRequest.get("unit"))){
                        for ( ;beginDate.compareTo(lastTime) <= 0; ){

                            timeRegion = getTimeRegionMonth(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), lastTime);
                            //查询订单数
                            if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findOrderNumByMonth(beginDate, mapRequest);
                            }
                            //查询订单人数
                            if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findPersonNumByMonth(beginDate, mapRequest);
                            }
                            //查询金额
                            if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findMoneyAmountByMonth(beginDate, mapRequest);
                            }

                            weaveData(list, timeRegion, num, num2, num3);
                            //循环到下月
                            beginDate = GetEachCycleFirstDay.getFirstDayofNextMonth(beginDate);
                        }
                    }
                }else { //365天以上
                    //按月展示
                    if ("m".equals(mapRequest.get("unit"))){
                        for ( ;beginDate.compareTo(lastTime) <= 0; ){

                            timeRegion = getTimeRegionMonth(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), lastTime);
                            //查询订单数
                            if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findOrderNumByMonth(beginDate, mapRequest);
                            }
                            //查询订单人数
                            if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findPersonNumByMonth(beginDate, mapRequest);
                            }
                            //查询金额
                            if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findMoneyAmountByMonth(beginDate, mapRequest);
                            }

                            weaveData(list, timeRegion, num, num2, num3);
                            //循环到下月
                            beginDate = GetEachCycleFirstDay.getFirstDayofNextMonth(beginDate);
                        }
                    }
                    //按年展示
                    if ("y".equals(mapRequest.get("unit"))){
                        for ( ;beginDate.compareTo(lastTime) <= 0; ){

                            timeRegion = beginDate.substring(0, 4);
                            //查询订单数
                            if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                                num = statisticAnalysisDao.findOrderNumByYear(beginDate, mapRequest);
                            }
                            //查询订单人数
                            if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findPersonNumByYear(beginDate, mapRequest);
                            }
                            //查询金额
                            if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                                num = statisticAnalysisDao.findMoneyAmountByYear(beginDate, mapRequest);
                            }

                            weaveData(list, timeRegion, num, num2, num3);
                            //循环到下月
                            beginDate = GetEachCycleFirstDay.getFirstDayofNextYear(beginDate);
                        }
                    }
                }
            }
        }else {//搜索框为自定义

            Date startDate = sdf.parse(mapRequest.get("startDate"));
            Date endDate  = sdf.parse(mapRequest.get("endDate"));

            Long startDateLong = startDate.getTime();
            Long endDateLong = endDate.getTime();
            //计算自定义事件相差的天数
            Long millisecond = endDateLong - startDateLong;
            Long day = millisecond/1000/3600/24;

            //获取开始时间
            String beginTime = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            String beginDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            String lastTime = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
            mapRequest.put("beginTime", beginTime);
            mapRequest.put("lastTime", lastTime);
            String beginDateTime = "";
            if (day < 1){   //周期一天
                //判断时间处于哪个时间段
                if ("h".equals(mapRequest.get("unit"))){
                    if (beginDate.compareTo(currentDateTime) == 0) {
                        //查询本日各时间段对应的订单数
                        for (int i = 0; i < temp; i++) {
                            if (i < 10) {
                                beginDateTime = beginDate + " 0" + i;
                            } else {
                                beginDateTime = beginDate + " " + i;
                            }
                            timeRegion = beginDateTime.substring(5) + ":00";
                            //查询并封装数据
                            findDataHour(mapRequest, list, timeRegion, beginDateTime, null, null);
                        }
                    } else {
                        //查询本日各时间段对应的订单数
                        for (int i = 0; i < 24; i++) {
                            if (i < 10) {
                                beginDateTime = beginDate + " 0" + i;
                            } else {
                                beginDateTime = beginDate + " " + i;
                            }
                            timeRegion = beginDateTime.substring(5) + ":00";
                            //查询并封装数据
                            findDataHour(mapRequest, list, timeRegion, beginDateTime, null, null);
                        }
                    }
                }

            }else if (day >= 1 && day < 8){ //周期2-8天

                    //查询维度是小时
                    if (mapRequest.get("unit").equals("h")){
                        //String beginDateTime = "";
                        for (;beginDate.compareTo(lastTime) <= 0;){ //遍历周内的每天
                            if (beginDate.compareTo(currentDateTime) < 0){
                                for (int i=0; i<24; i++){
                                    if(i<10){
                                        beginDateTime = beginDate + " 0" + i;
                                    }else {
                                        beginDateTime = beginDate + " " + i;
                                    }
                                    timeRegion = beginDateTime.substring(5) + ":00";
                                    //查询并封装数据
                                    findDataHour(mapRequest, list, timeRegion, beginDateTime, null, null);
                                }
                            }else {
                                for (int i = 0; i < temp; i++) {
                                    if (i < 10) {
                                        beginDateTime = beginDate + " 0" + i;
                                    } else {
                                        beginDateTime = beginDate + " " + i;
                                    }
                                    timeRegion = beginDateTime.substring(5) + ":00";
                                    //查询并封装数据
                                    findDataHour(mapRequest, list, timeRegion, beginDateTime, null, null);
                                }
                            }
                            //天数加1
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                        }
                    }
                    //本年按天查询
                    if ("d".equals(mapRequest.get("unit"))){
                        //查询维度是天
                        for (;beginDate.compareTo(lastTime) <= 0;){ //遍历周内的每天
                            timeRegion = beginDate.substring(5);
                            //查询并封装数据
                            findDataDay(mapRequest, list, timeRegion, beginDate, null, null);
                            //今年时间增加一天
                            beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                        }
                    }
            }else if (day >= 8 && day < 14){    //周期9-14天
                //查询维度是天
                for (;beginDate.compareTo(lastTime) <= 0;){ //遍历周内的每天
                    timeRegion = beginDate.substring(5);
                    //查询并封装数据
                    findDataDay(mapRequest, list, timeRegion, beginDate, null, null);
                    //今年时间增加一天
                    beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                }
            }else if (day >= 14 && day < 58){   //周期15-58天

                //查询维度是天
                if (mapRequest.get("unit").equals("d")){

                    for (;beginDate.compareTo(lastTime) <= 0;) { //遍历周内的每天

                        timeRegion = beginDate.substring(5);
                        //查询并封装数据
                        findDataDay(mapRequest, list, timeRegion, beginDate, null, null);
                        //时间增加一天
                        beginDate = GetEachCycleFirstDay.getTomorrowOfThisDay(beginDate, 1);
                    }
                }

                if ("w".equals(mapRequest.get("unit"))){

                    for (;beginDate.compareTo(lastTime) <= 0;){

                        timeRegion = getTimeRegion2(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), lastTime);
                        //查询订单数
                        if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                            num = statisticAnalysisDao.findOrderNumByWeek(beginDate, mapRequest);
                        }
                        //查询订单人数
                        if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findPersonNumByWeek(beginDate, mapRequest);
                        }
                        //查询金额
                        if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findMoneyAmountByWeek(beginDate, mapRequest);
                        }
                        //封装数据
                        weaveData(list, timeRegion, num, num2, num3);
                        //循环条件
                        beginDate = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate);
                    }
                }
            }else if (day >= 58 && day < 365){     //自定义周期58天及以上

                if (mapRequest.get("unit").equals("w")){

                    for (;beginDate.compareTo(lastTime) <= 0;){

                        timeRegion = getTimeRegion2(new SimpleDateFormat("yyyy-MM-dd").parse(beginDate), lastTime);
                        //查询订单数
                        if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                            num = statisticAnalysisDao.findOrderNumByWeek(beginDate, mapRequest);
                        }
                        //查询订单人数
                        if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findPersonNumByWeek(beginDate, mapRequest);
                        }
                        //查询金额
                        if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findMoneyAmountByWeek(beginDate, mapRequest);
                        }
                        //组装数据
                        weaveData(list, timeRegion, num, num2, num3);
                        //获取下周日期
                        beginDate = GetEachCycleFirstDay.getNextWeekFirstDay(beginDate);
                    }
                }

                if (mapRequest.get("unit").equals("m")){

                    for ( ;beginDate.compareTo(lastTime) <= 0; ){

                        timeRegion = getTimeRegionMonth(sdf.parse(beginDate), lastTime);
                        //查询订单数
                        if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                            num = statisticAnalysisDao.findOrderNumByMonth(beginDate, mapRequest);
                        }
                        //查询订单人数
                        if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findPersonNumByMonth(beginDate, mapRequest);
                        }
                        //查询金额
                        if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findMoneyAmountByMonth(beginDate, mapRequest);
                        }
                        weaveData(list, timeRegion, num, num2, num3);
                        //循环到下月
                        beginDate = GetEachCycleFirstDay.getFirstDayofNextMonth(beginDate);
                    }
                }
            }else {
                if ("m".equals(mapRequest.get("unit"))){

                    for ( ;beginDate.compareTo(lastTime) <= 0; ){

                        timeRegion = getTimeRegionMonth(sdf.parse(beginDate), lastTime);
                        //查询订单数
                        if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                            num = statisticAnalysisDao.findOrderNumByMonth(beginDate, mapRequest);
                        }
                        //查询订单人数
                        if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findPersonNumByMonth(beginDate, mapRequest);
                        }
                        //查询金额
                        if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findMoneyAmountByMonth(beginDate, mapRequest);
                        }
                        weaveData(list, timeRegion, num, num2, num3);
                        //循环到下月
                        beginDate = GetEachCycleFirstDay.getFirstDayofNextMonth(beginDate);
                    }
                }

                if ("y".equals(mapRequest.get("unit"))){
                    for ( ;beginDate.compareTo(lastTime) <= 0; ){
                        timeRegion = beginDate.substring(0, 4);

                        //查询订单数
                        if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(mapRequest.get("analysisType"))) {
                            num = statisticAnalysisDao.findOrderNumByYear(beginDate, mapRequest);
                        }
                        //查询订单人数
                        if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findPersonNumByYear(beginDate, mapRequest);
                        }
                        //查询金额
                        if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(mapRequest.get("analysisType"))){
                            num = statisticAnalysisDao.findMoneyAmountByYear(beginDate, mapRequest);
                        }
                        //封装数据
                        weaveData(list, timeRegion, num, num2, num3);
                        //循环到下月
                        beginDate = GetEachCycleFirstDay.getFirstDayofNextYear(beginDate);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 按小时查找数据
     * @param map
     * @param list
     * @param timeRegion
     * @param firstTimeSlice
     * @param secondTimeSlice
     * @param thirdTimeSlice
     * @throws Exception
     */
    private void findDataHour(Map<String,String> map, List list, String timeRegion, String firstTimeSlice, String secondTimeSlice, String thirdTimeSlice) throws Exception{
        String num = "";
        String num2 = "";
        String num3 = "";

        //查询订单数
        if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(map.get("analysisType"))) {
            if (firstTimeSlice != null) {
                num = statisticAnalysisDao.findOrderNumByHour(firstTimeSlice, map);
            }
            if (secondTimeSlice != null) {
                num2 = statisticAnalysisDao.findOrderNumByHour(secondTimeSlice, map);
            }
            if (thirdTimeSlice != null) {
                num3 = statisticAnalysisDao.findOrderNumByHour(thirdTimeSlice, map);
            }
        }
        //查询订单人数
        if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(map.get("analysisType"))){
            if (firstTimeSlice != null) {
                num = statisticAnalysisDao.findPersonNumByHour(firstTimeSlice, map);
            }
            if (secondTimeSlice != null) {
                num2 = statisticAnalysisDao.findPersonNumByHour(secondTimeSlice, map);
            }
            if (thirdTimeSlice != null) {
                num3 = statisticAnalysisDao.findPersonNumByHour(thirdTimeSlice, map);
            }
        }
        //查询金额
        if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(map.get("analysisType"))){
            if (firstTimeSlice != null) {
                num = statisticAnalysisDao.findMoneyAmountByHour(firstTimeSlice, map);
            }
            if (secondTimeSlice != null) {
                num2 = statisticAnalysisDao.findMoneyAmountByHour(secondTimeSlice, map);
            }
            if (thirdTimeSlice != null) {
                num3 = statisticAnalysisDao.findMoneyAmountByHour(thirdTimeSlice, map);
            }
        }

        //封装数据
        weaveData(list, timeRegion, num, num2, num3);
    }

    /**
     *按天查找数据
     * @param map
     * @param list
     * @param timeRegion
     * @param firstTimeSlice
     * @param secondTimeSlice
     * @param thirdTimeSlice
     * @throws Exception
     */
    private void findDataDay(Map<String,String> map, List list, String timeRegion, String firstTimeSlice, String secondTimeSlice, String thirdTimeSlice) throws Exception{

        String num = "";
        String num2 = "";
        String num3 = "";

        //查询订单数
        if (Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(map.get("analysisType"))) {
            if (firstTimeSlice != null) {
                num = statisticAnalysisDao.findOrderNumByDay(firstTimeSlice, map);
            }
            if (secondTimeSlice != null) {
                num2 = statisticAnalysisDao.findOrderNumByDay(secondTimeSlice, map);
            }
            if (thirdTimeSlice != null) {
                num3 = statisticAnalysisDao.findOrderNumByDay(thirdTimeSlice, map);
            }
        }
        //查询订单人数
        if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(map.get("analysisType"))){
            if (firstTimeSlice != null) {
                num = statisticAnalysisDao.findPersonNumByDay(firstTimeSlice, map);
            }
            if (secondTimeSlice != null) {
                num2 = statisticAnalysisDao.findPersonNumByDay(secondTimeSlice, map);
            }
            if (thirdTimeSlice != null) {
                num3 = statisticAnalysisDao.findPersonNumByDay(thirdTimeSlice, map);
            }
        }
        //查询金额
        if(Context.ORDER_DATA_STATISTICS_ORDER_MONEY.equals(map.get("analysisType"))){
            if (firstTimeSlice != null) {
                num = statisticAnalysisDao.findMoneyAmountByDay(firstTimeSlice, map);
            }
            if (secondTimeSlice != null) {
                num2 = statisticAnalysisDao.findMoneyAmountByDay(secondTimeSlice, map);
            }
            if (thirdTimeSlice != null) {
                num3 = statisticAnalysisDao.findMoneyAmountByDay(thirdTimeSlice, map);
            }
        }

        //封装数据
        weaveData(list, timeRegion, num, num2, num3);
    }

    //封装数据
    private void  weaveData(List list, String timeRegion, String num1, String num2, String num3) throws Exception{

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timeRegion", timeRegion);
        jsonObject.put("ordernum", num1);
        jsonObject.put("fordernum", num2);
        jsonObject.put("sordernum", num3);

        list.add(jsonObject);
    }

    //获取需要查询的时间天数
    private Long getDayNumber(String minTime,String maxTime) throws Exception{
        //获取时间差
        Date minTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(minTime);
        Date maxTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(maxTime);

        Calendar c = Calendar.getInstance();
        c.setTime(minTimeDate);
        long minTimeInMillis = c.getTimeInMillis();
        c.setTime(maxTimeDate);
        long maxTimeInMillis = c.getTimeInMillis();
        Long timeInMillis = maxTimeInMillis - minTimeInMillis;
        Long day = timeInMillis/1000/3600/24;

        return day;
    }

    //拼接按周查询时间轴
    private String getTimeRegion(Date date, String lastTime){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(calendar.DAY_OF_WEEK) - 1;
        if(day == 0){
            day = 7;
        }
        int month = calendar.get(Calendar.MONTH);

        for (int i = day; i > 1; i--){
            if(day != Calendar.MONDAY){
                int month1 = calendar.get(Calendar.MONTH);
                if(month == month1) {
                    calendar.add(Calendar.DAY_OF_WEEK, -1);
                }else {
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                    break;
                }

            }
        }
        Date startTimeDate = calendar.getTime();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);

        for (int j=7-day; j>0; j--){
            //if(day != Calendar.SUNDAY){
            int month2 = calendar1.get(Calendar.MONTH);
            if (month2 == month){
                calendar1.add(Calendar.DAY_OF_MONTH, 1);
            }else {
                calendar1.add(Calendar.DAY_OF_MONTH, -1);
                break;
            }
            //}
        }
        Date endTimeDate = calendar1.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String startTime = sdf.format(startTimeDate);
        String endTime = sdf.format(endTimeDate);
        if (endTime.compareTo(lastTime.substring(5,10)) > 0){
            endTime = lastTime.substring(5,10);
        }
        String timeRegion = startTime + "至" + endTime;
        return timeRegion;
    }


    /**
     * 获取按周查询拼接时间
     * @param date
     * @param lastTime
     * @return
     */
    private String getTimeRegion2(Date date, String lastTime){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String startTime = sdf.format(date).substring(5,10);

        int i = calendar.get(Calendar.DAY_OF_WEEK);

        if (i!=1) {//不是周日
            calendar.add(Calendar.DATE, -(i - 2));
            //calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            calendar.add(Calendar.DATE, 6);
        }
            //calendar.add(Calendar.DATE, -(i - 1));
            //calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            //calendar.add(Calendar.DATE, 1);

        //calendar.add(Calendar.DATE, -(i-1));
        /*calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        calendar.add(Calendar.DATE, 6);*/

        String endTime = sdf.format(calendar.getTime());
        if (endTime.compareTo(lastTime.substring(0,10)) > 0){
            endTime = lastTime.substring(5,10);
        }else {
            endTime = endTime.substring(5,10);
        }
        String timeRegion = startTime + "至" + endTime;
        return timeRegion;
    }

    /**
     * 获取按月查询拼接时间
     * @param date
     * @param lastTime
     * @return
     */
    private String getTimeRegionMonth(Date date, String lastTime){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String startTime = sdf.format(date).substring(0,4) + " " + sdf.format(date).substring(5,10);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        String endTime = sdf.format(calendar.getTime());
        if (endTime.compareTo(lastTime.substring(0,10)) > 0){
            endTime = lastTime.substring(5,10);
        }else {
            endTime = endTime.substring(5,10);
        }
        String timeRegion = startTime + "至" + endTime;
        return timeRegion;
    }

    /**
     * 获取最大时间(因需求更改，现改为当前时间)
     * @return
     */
    public String getTimeMax(){

        String time = statisticAnalysisDao.getTimeMax();
        if (StringUtils.isNotBlank(time)){
            time = time.substring(0,19);
            return time;
        }
        return null;
    };

    /**
     * 获取批发商创建账号的最早时间
     * @return
     */
    public String getTimeMin(){
        String time = statisticAnalysisDao.getTimeMin();
        if (StringUtils.isNotBlank(time)){
            time = time.substring(0,19);
            return time;
        }
        return null;
    }

    /**
     * 根据订单id和类型，更新该条记录的金额，金额增加increment。
     * @param increment 增量，在原有的基础上增加
     * @param orderId 订单id
     * @param orderType 订单类型
     */
    public void addAmountByOrderIdAndType(BigDecimal increment, String orderId, String orderType){
        if (increment == null || StringUtils.isBlank(orderId) || StringUtils.isBlank(orderType)){
            return;
        }
        statisticAnalysisDao.addAmountByOrderIdAndType(increment,orderId,orderType);
    }

    /**
     * 根据订单id和订单类型orderType来更新预统计表中的相应记录。首先，获取当前最新的该订单的数据。然后获取相应的预统计的对象实体。
     * 更新实体entity对象信息，然后保存对象。完成更新。如果该订单没有对应的预统计记录。则进行新增操作，填充数据保存。
     * @param orderId
     * @param orderType
     */
    public void updateStatisticRecord(Long orderId,Integer orderType){
        if (orderId == null || orderType == null){
            return;
        }
        Map<String,Object> orderInfo = statisticAnalysisDao.getTheLatestOrderInfo(orderId,orderType);
        OrderDataStatistics entity = statisticAnalysisDao.getEntityByOrderIdAndType(orderId,orderType);
        if (entity == null){
            entity = new OrderDataStatistics();
            entity.setUuid(UuidUtils.generUuid());
            entity.setCompanyUuid(UserUtils.getUser().getCompany().getUuid());
            entity.setCreateDate(new Date());
            entity.setDelFlag("0");
        }
        copyOrderInfo2Entity(orderInfo,entity);
        statisticAnalysisDao.saveOrUpdateEntity(entity);
    }

    // 把map数据转换为entity对象数据。
    private void copyOrderInfo2Entity(Map orderInfo,OrderDataStatistics entity){
        entity.setOrderId(Long.parseLong(orderInfo.get("orderId").toString()));
        entity.setProductId(Long.parseLong(orderInfo.get("productId").toString()));
        entity.setProductName((String)orderInfo.get("productName"));
        entity.setOrderType(Integer.parseInt(orderInfo.get("orderType").toString()));

        entity.setAmount((BigDecimal)orderInfo.get("amount"));
        entity.setAmountUuid((String)orderInfo.get("amountUuid"));
        entity.setOrderPersonNum(Integer.parseInt(orderInfo.get("orderPersonNum").toString()));
        entity.setOrderCreatetime((Date)orderInfo.get("orderCreateTime"));

        entity.setAgentinfoId(Long.parseLong(orderInfo.get("agentId").toString()));
        entity.setAgentinfoName((String)orderInfo.get("agentName"));
        entity.setSalerId(Integer.parseInt(orderInfo.get("salerId").toString()));
        entity.setSalerName((String)orderInfo.get("salerName"));

        entity.setOrderStatus(Integer.parseInt(orderInfo.get("payStatus").toString()));
    }
}
