package com.trekiz.admin.modules.statisticAnalysis.home.web;



import com.google.common.collect.Maps;
import com.ning.http.client.Request;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.statisticAnalysis.home.service.CountAndRateService;
import com.trekiz.admin.modules.statisticAnalysis.home.service.StatisticAnalysisService;
import com.trekiz.admin.modules.statisticAnalysis.home.util.StatisticAnalysisUtil;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by quauq on 2016/12/22.
 */
@Controller
@RequestMapping(value = "${adminPath}/statisticAnalysis/orderData")
public class StatisticAnalysisController extends BaseController {

    @Autowired
    private StatisticAnalysisService statisticAnalysisService;

    @Autowired
    private CountAndRateService countAndRateService;

    @RequestMapping(value = "orderDataHomePage")
    @ResponseBody
    public String homePageHead(HttpServletRequest request, HttpServletResponse response){

        String params = "orderType,analysisType,searchDate,startDate,endDate,unit";

        Map<String,String> mapRequest = Maps.newHashMap();

        StatisticAnalysisUtil.handlePara(params, mapRequest, request);

        JSONObject jsonObject = new JSONObject();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String currentTime = sf.format(date);
        String maxNum = "0";
        List list = null;
        Map<String, Object> countAndRate = null;
        try {

            list = statisticAnalysisService.findOrderNum(mapRequest);

            jsonObject.put("serverTime",currentTime);
            jsonObject.put("list",list);
            //遍历获取最大值
            for (Object obj : list) {
                JSONObject json = (JSONObject)obj;

                String ordernum = json.getString("ordernum");
                String fordernum = json.getString("fordernum");
                String sordernum = json.getString("sordernum");

                maxNum = maxNum.compareTo(ordernum) > 0 ? maxNum : ordernum ;
                maxNum = maxNum.compareTo(fordernum) > 0 ? maxNum : fordernum ;
                maxNum = maxNum.compareTo(sordernum) > 0 ? maxNum : sordernum ;

            }
            jsonObject.put("maxNum", maxNum);
            countAndRate = countAndRateService.getCountAndRate(mapRequest);
            jsonObject.put("newNum", countAndRate.get("newNum"));
            jsonObject.put("incrementRate", countAndRate.get("incrementRate"));

        } catch (Exception e) {
            e.printStackTrace();
        }
         return jsonObject.toString();
    }

    /**
     * 获取全部订单的最大时间和最小时间
     * @param response
     */
    @ResponseBody
    @RequestMapping(value="getTime")
    public void getStartTimeAndEndTime(HttpServletResponse response){

        Map<String,String> map = Maps.newHashMap();
        String maxTime = statisticAnalysisService.getTimeMax();
        String minTime = statisticAnalysisService.getTimeMin();
        map.put("minTime", minTime);
        map.put("maxTime", maxTime);

        String json = com.alibaba.fastjson.JSONObject.toJSONString(map);
        ServletUtil.print(response,json);
    }

}
