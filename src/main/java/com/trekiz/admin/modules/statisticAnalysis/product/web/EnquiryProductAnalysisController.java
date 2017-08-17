package com.trekiz.admin.modules.statisticAnalysis.product.web;

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.product.service.EnquiryProductAnalysisService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品询单记录
 * Created by quauq on 2017/3/8.
 */
@Controller
@RequestMapping(value="${adminPath}/statistic/enquiryAnalysis")
public class EnquiryProductAnalysisController {

    @Autowired
    private EnquiryProductAnalysisService enquiryProductAnalysisImpl;
    @RequestMapping(value = "list")
    @ResponseBody
    public List enquiryList(HttpServletRequest request, HttpServletResponse response){
        Map<String, String> paramMap = getParamMap(request);
        List<Map<String,Object>> list = null;
        try {
            //获取产品询单数量排名前五的产品
            list = enquiryProductAnalysisImpl.getEnquiryProduct(paramMap);

            //获取产品总数
            String total = enquiryProductAnalysisImpl.getAllEnquiryProductNum(paramMap);
            Integer num = 0;
            for (Map map:list){
                if (map.get("orderNum") != null) {
                    num += Integer.parseInt(map.get("orderNum").toString());
                }
            }
            //将其他产品放入list
            Map<String, Object> map = new HashMap();
            map.put("productName","其他");
            map.put("orderNum", Integer.parseInt(total)-num);
            list.add(map);
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取页面传来的参数
     * @return
     */
    private Map<String, String> getParamMap(HttpServletRequest request){

        Map<String, String> map = new HashMap<String, String>();

        String overView = request.getParameter("overView");//获取页签（询单/订单）dd：订单总览  xd：询单总览
        if (StringUtils.isNotBlank(overView)){
            map.put("overView", overView);
        }
        
        //获取查询时间 1：今日   -1：昨日   3：本月   -3：上月   4：本年   -4：去年    5：全部
        String searchDate = request.getParameter("searchDate");
        //searchDate = "1";
        if (StringUtils.isNotBlank(searchDate)){
            Calendar c = Calendar.getInstance();
            if ("1".equals(searchDate)){
                map.put("startDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
                map.put("endDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
            }
            if ("-1".equals(searchDate)){
                c.add(Calendar.DATE, -1);
                map.put("startDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
                map.put("endDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
            }
            if ("3".equals(searchDate)){
                c.set(Calendar.DAY_OF_MONTH, 1);
                map.put("startDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
                c.add(Calendar.MONTH, 1);
                c.add(Calendar.DAY_OF_MONTH, -1);
                map.put("endDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
            }
            if ("-3".equals(searchDate)){
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.add(Calendar.DATE, -1);
                map.put("endDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
                c.set(Calendar.DAY_OF_MONTH, 1);
                map.put("startDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
            }
            if ("4".equals(searchDate)){
                c.set(Calendar.MONTH, 0);
                c.set(Calendar.DAY_OF_MONTH, 1);
                map.put("startDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DAY_OF_MONTH, 31);
                map.put("endDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
            }
            if ("-4".equals(searchDate)){
                c.add(Calendar.YEAR, -1);
                c.set(Calendar.MONTH, 0);
                c.set(Calendar.DAY_OF_MONTH, 1);
                map.put("startDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DAY_OF_MONTH, 31);
                map.put("endDate", DateUtils.date2String(c.getTime(), "yyyy-MM-dd"));
            }
            if ("5".equals(searchDate)){

                map.put("startDate", "");
                map.put("endDate", "");
            }

        }else{
        	//获取自定义开始时间
            String startDate = request.getParameter("startDate");
            if (StringUtils.isNotBlank(startDate)){
                map.put("startDate", startDate);
            }else{
            	map.put("startDate", "");
            }

            //获取自定义结束时间
            String endDate = request.getParameter("endDate");
            if (StringUtils.isNotBlank(endDate)){
                map.put("endDate", endDate);
            }else{
            	map.put("endDate", "");
            }
        }
        return map;
    }
}
