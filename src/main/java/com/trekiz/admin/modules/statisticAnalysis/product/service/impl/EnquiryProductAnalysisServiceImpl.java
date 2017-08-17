package com.trekiz.admin.modules.statisticAnalysis.product.service.impl;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.modules.statisticAnalysis.product.dao.EnquiryProductAnalysisDao;
import com.trekiz.admin.modules.statisticAnalysis.product.service.EnquiryProductAnalysisService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by quauq on 2017/3/8.
 */
@Service
public class EnquiryProductAnalysisServiceImpl implements EnquiryProductAnalysisService {

    @Autowired
    private EnquiryProductAnalysisDao enquiryProductAnalysisDaoImpl;

    /**
     * 获取有效产品的询单数，并降序排列，取前五名产品的信息。
     * @return
     */
    public List<Map<String,Object>> getEnquiryProduct(Map<String, String> map){

        StringBuffer sbf = new StringBuffer();

        sbf .append("SELECT ta.acitivityName AS productName, IFNULL(num ,0) AS orderNum ")
                .append("FROM travelactivity ta LEFT JOIN (SELECT opt.activity_id, COUNT(opt.id) AS num FROM order_progress_tracking opt  ").append("WHERE opt.ask_time IS NOT NULL ");
        if (!"".equals(map.get("startDate"))){
            sbf.append("AND DATE_FORMAT(opt.ask_time,'%Y-%m-%d') >= '")
                    .append(map.get("startDate")).append("' ");
                    
        }
        if (!"".equals(map.get("endDate"))){
        	sbf.append(" AND DATE_FORMAT(opt.ask_time,'%Y-%m-%d') <= '").append(map.get("endDate")).append("' ");
        }
        sbf.append("GROUP BY opt.activity_id ").
                append(") AS temp ON ta.id = temp.activity_id ")
                .append("INNER JOIN (SELECT COUNT(ag.srcActivityId), ag.srcActivityId AS activityId FROM activitygroup ag WHERE ag.is_t1 = '1' OR ag.pricingStrategyStatus = '1' OR ag.pricingStrategyStatus = '2' GROUP BY ag.srcActivityId ) AS t1product ON ta.id = t1product.activityId ")
                .append("WHERE ta.proCompany = '").append(UserUtils.getUser().getCompany().getId()).append("' ")
                .append("ORDER BY ")
                .append("orderNum DESC, ")
                .append("productName ASC ")
                .append(" LIMIT 0,5");

        List<Map<String,Object>> list = enquiryProductAnalysisDaoImpl.findBySql(sbf.toString(),Map.class);
        return list;
    }

    public String getAllEnquiryProductNum(Map<String, String> map){
        StringBuffer sbf = new StringBuffer();
        sbf.append("SELECT ")
                .append("IFNULL(count(opt.activity_id),0) AS num ")
                .append("FROM ")
                .append("order_progress_tracking opt ")
                .append("WHERE ")
                //.append("1 = 1 ")
                //.append("AND opt.ask_time >= '").append(map.get("startDate")).append("' ")
                //.append("AND opt.ask_time <= '").append(map.get("endDate")).append("' ")
                .append("opt.company_id = '").append(UserUtils.getUser().getCompany().getId()).append("' ")
                //.append("AND opt.orderStatus = '0' ")
                .append("AND opt.ask_time IS NOT NULL ");
        if (!"".equals(map.get("startDate"))){
            sbf.append("AND DATE_FORMAT(opt.ask_time,'%Y-%m-%d') >= '")
                    .append(map.get("startDate"))
                    .append("'  AND DATE_FORMAT(opt.ask_time,'%Y-%m-%d') <= '").append(map.get("endDate")).append("' ");
        }
        List<Map<String,Object>> list = enquiryProductAnalysisDaoImpl.findBySql(sbf.toString(),Map.class);
        if (!Collections3.isEmpty(list)){
            return list.get(0).get("num").toString();
        }
        return "0";
    }
}
