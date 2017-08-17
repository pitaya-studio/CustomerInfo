package com.trekiz.admin.modules.statisticAnalysis.product.service.impl;

import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.modules.statisticAnalysis.product.dao.IOrderProductAnalysisDao;
import com.trekiz.admin.modules.statisticAnalysis.product.service.IOrderProductAnalysisService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.AfterTransaction;

import java.util.List;
import java.util.Map;

/**
 * Created by quauq on 2017/3/13.
 */
@Service
public class OrderProductAnalysisServiceImpl implements IOrderProductAnalysisService {

    @Autowired
    private IOrderProductAnalysisDao orderProductAnalysisDaoImpl;

    /**
     * 获取前五名产品的销量
     * @param map
     * @return
     */
    public List<Map<String, Object>> getOrderProductList(Map map){
        StringBuffer sbf = new StringBuffer();
        //analysisType: //1:订单数 2：收客人数 3：订单金额
        if ("1".equals(map.get("analysisType"))){
            
            sbf.append("SELECT ").append("ta.acitivityName AS productName, ")
                    .append("IFNULL(temp.order_number, 0) AS orderNum ")
                    //.append("IFNULL(temp.amount, 0)-IFNULL((SELECT mat.amount * mat.exchangerate FROM money_amount mat WHERE mat.serialNum=temp.differenceMoney),0) AS orderNum ")
                    .append("FROM ")
                    .append("travelactivity ta ")
                    .append("LEFT JOIN ( ").append("SELECT ").append("po.productId AS productId, ")
                    .append("COUNT(po.id) AS order_number ")
                    .append("FROM ")
                    .append("productorder po ")
                    .append("INNER JOIN order_progress_tracking opt ON po.id = opt.order_id ")
                    .append("AND opt.ask_time IS NOT NULL ")
                    .append("AND opt.ask_num IS NOT NULL ")
                    .append("AND opt.order_id IS NOT NULL ")
                    .append("AND opt.company_id = '"+UserUtils.getUser().getCompany().getId()+"' ")
                    .append("WHERE 1 = 1 ")
                    .append("AND po.orderStatus = '2' ")
                    .append("AND po.payStatus IN (3, 4, 5) ");
            if (!"".equals(map.get("startDate"))) {
                sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') >= '" + map.get("startDate") + "' ");
                        
            }
            if(!"".equals(map.get("endDate"))){
            	sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') <= '" + map.get("endDate") + "' ");
            }
            sbf.append("GROUP BY po.productId ")
                    .append(") AS temp ON ta.id = temp.productId ")
                    .append("INNER JOIN (SELECT COUNT(ag.srcActivityId), ag.srcActivityId AS activityId FROM activitygroup ag WHERE ag.is_t1 = '1' OR ag.pricingStrategyStatus = '1' OR ag.pricingStrategyStatus = '2' GROUP BY ag.srcActivityId ) AS t1product ON ta.id = t1product.activityId ")
                    .append("AND ta.proCompany='" + UserUtils.getUser().getCompany().getId() +"' ")//.append("AND ta.delFlag = '0' ")
                    .append("ORDER BY orderNum DESC ")
                    .append("LIMIT 0,5; ");

        }else if ("2".equals(map.get("analysisType"))){
            
            sbf.append("SELECT ")
                    .append("ta.acitivityName AS productName, ")
                    .append("IFNULL(temp.orderPerson, 0) AS orderNum ")
                    .append("FROM ")
                    .append("travelactivity ta ")
                    .append("LEFT JOIN ( ")
                    .append("SELECT ")
                    .append("po.productId AS productId, ")
                    .append("SUM(po.orderPersonNum) AS orderPerson ")
                    .append("FROM ")
                    .append("productorder po ")
                    .append("INNER JOIN  order_progress_tracking opt ON po.id = opt.order_id ")
                    .append("AND opt.ask_time IS NOT NULL AND opt.order_id IS NOT NULL ")
                    .append("AND opt.ask_num IS NOT NULL ")

                    .append("AND opt.company_id = '" + UserUtils.getUser().getCompany().getId() + "' ")
                    .append("WHERE ")
                    .append("1 = 1 ")
                    .append("AND po.orderStatus = '2' ").append("AND po.payStatus IN (3, 4, 5) ");
            if (!"".equals(map.get("startDate"))) {
                sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') >= '" + map.get("startDate") + "' ");
            }
            if (!"".equals(map.get("endDate"))){
            	sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') <= '" + map.get("endDate") + "' ");
            }
            sbf.append("GROUP BY ")
                    .append("po.productId ")
                    .append(") AS temp ON ta.id = temp.productId ")
                    .append("INNER JOIN (SELECT COUNT(ag.srcActivityId), ag.srcActivityId AS activityId FROM activitygroup ag WHERE ag.is_t1 = '1' OR ag.pricingStrategyStatus = '1' OR ag.pricingStrategyStatus = '2' GROUP BY ag.srcActivityId ) AS t1product ON ta.id = t1product.activityId ")
                    .append("AND ta.proCompany='" + UserUtils.getUser().getCompany().getId() +"' ")//.append("AND ta.delFlag = '0' ")
                    .append("ORDER BY ").append("orderNum DESC ").append("limit 0,5; ");

        }else {//查询订单金额
            sbf.append("SELECT ")
                    .append("ta.acitivityName AS productName, ")
                    .append("IFNULL(temp.totalMoney, 0)-IFNULL(temp.diffMoney,0) AS orderNum ")
                    //.append("IFNULL(temp.amount, 0) AS orderNum ")
                    .append("FROM ")
                    .append("travelactivity ta ")
                    .append("LEFT JOIN ( ")
                    .append("SELECT ")
                    .append("po.productId AS productId, ")
                    .append("SUM(diffAmount.amount) AS diffMoney, ")
                    .append("SUM((SELECT SUM(mot.amount * mot.exchangerate) AS totalamount FROM money_amount mot WHERE mot.serialNum = po.total_money)) AS totalMoney ")
                    .append("FROM ")
                    .append("productorder po ")
                    .append("LEFT JOIN money_amount diffAmount ")
                    .append("ON po.differenceMoney = diffAmount.serialNum ")
                    .append("INNER JOIN  order_progress_tracking opt ON po.id = opt.order_id ")
                    .append("AND opt.ask_time IS NOT NULL AND opt.order_id IS NOT NULL ")
                    .append("AND opt.ask_num IS NOT NULL ")
                    .append("AND opt.company_id = '"+UserUtils.getUser().getCompany().getId()+"' ")
                    .append("WHERE 1 = 1 ")
                    .append("AND po.orderStatus = '2' ")
                    .append("AND po.payStatus IN (3, 4, 5)");
            if (!"".equals(map.get("startDate"))) {
                sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') >= '" + map.get("startDate") + "' ");
                        
            }
            if(!"".equals(map.get("endDate"))){
            	sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') <= '" + map.get("endDate") + "' ");
            }
            sbf.append("GROUP BY po.productId ")
                    .append(") AS temp ON ta.id = temp.productId ")
                    .append("INNER JOIN (SELECT COUNT(ag.srcActivityId), ag.srcActivityId AS activityId FROM activitygroup ag WHERE ag.is_t1 = '1' OR ag.pricingStrategyStatus = '1' OR ag.pricingStrategyStatus = '2' GROUP BY ag.srcActivityId ) AS t1product ON ta.id = t1product.activityId ")
                    .append("AND ta.proCompany='" + UserUtils.getUser().getCompany().getId() +"' ")//.append("AND ta.delFlag = '0' ")
                    .append("ORDER BY orderNum DESC ").append("LIMIT 0,5;");

        }

        List<Map<String,Object>> list = orderProductAnalysisDaoImpl.findBySql(sbf.toString(), Map.class);

        return list;
    }

    /**
     * 获取产品的总销量
     * @param map
     * @return
     */
    public String getAllOrderProductNum(Map<String, String> map){
        StringBuffer sbf = new StringBuffer();
        //analysisType: //1:订单数 2：收客人数 3：订单金额
        if ("1".equals(map.get("analysisType"))) {
            sbf.append("SELECT ")
                    .append("COUNT(po.id) AS num ")
                    .append("FROM ").append("productorder po ")
                    .append("INNER JOIN order_progress_tracking opt ON po.id = opt.order_id ")
                    .append("AND opt.ask_time IS NOT NULL ")
                    .append("AND opt.ask_num IS NOT NULL ")
                    .append("AND opt.order_id IS NOT NULL ")
                    .append("AND opt.company_id = '"+UserUtils.getUser().getCompany().getId()+"' ").append("WHERE 1 = 1 ")
                    .append("AND po.orderStatus = '2' ").append("AND po.payStatus IN (3, 4, 5) ");
            //startDate为“”时，表示查询“全部”数据
            if (!"".equals(map.get("startDate"))) {
                sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') >= '").append(map.get("startDate")).append("' ");
            }
            if (!"".equals(map.get("endDate"))){
            	sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') <= '").append(map.get("endDate")).append("'; ");
            }
        } else if ("2".equals(map.get("analysisType"))){
            sbf.append("SELECT ")
                    .append("SUM(po.orderPersonNum) AS num ")
                    .append("FROM ")
                    .append("productorder po ")
                    .append("INNER JOIN  order_progress_tracking opt ON po.id = opt.order_id ")
                    .append("AND opt.ask_time IS NOT NULL AND opt.order_id IS NOT NULL ")
                    .append("AND opt.ask_num IS NOT NULL ")
                    .append("AND opt.company_id = '"+UserUtils.getUser().getCompany().getId()+"' ")
                    .append("WHERE 1 = 1 ")
                    .append("AND po.orderStatus = '2' ")
                    .append("AND po.payStatus IN (3, 4, 5) ");
                    if (!"".equals(map.get("startDate"))) {
                        sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') >= '" + map.get("startDate") + "' ");
                                
                    }
                    if (!"".equals(map.get("endDate"))){
                    	sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') <= '" + map.get("endDate") + "' ");
                    }

        }else {
            sbf.append("SELECT ")
                    //.append("SUM(mot.amount * mot.exchangerate) AS num ")
                    .append("SUM(IFNULL(mot.amount * mot.exchangerate, 0)-IFNULL((SELECT mat.amount * mat.exchangerate FROM money_amount mat WHERE mat.serialNum=po.differenceMoney),0)) AS num ")
                    .append("FROM ")
                    .append("productorder po ")
                    .append("INNER JOIN money_amount mot ")
                    .append("ON po.id = mot.uid ")
                    .append("AND po.total_money = mot.serialNum ")
                    .append("AND mot.orderType = '2'")
                    .append("INNER JOIN  order_progress_tracking opt ON po.id = opt.order_id ")
                    .append("AND opt.ask_time IS NOT NULL AND opt.order_id IS NOT NULL ")
                    .append("AND opt.ask_num IS NOT NULL ")
                    .append("AND opt.company_id = '"+UserUtils.getUser().getCompany().getId()+"' ")
                    .append("WHERE 1 = 1 ").append("AND po.orderStatus = '2' ")
                    .append("AND po.payStatus IN (3, 4, 5)");
            if (!"".equals(map.get("startDate"))) {
                sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') >= '" + map.get("startDate") + "' ");
                        
            }
            if (!"".equals(map.get("endDate"))){
            	sbf.append("AND DATE_FORMAT(po.orderTime,'%Y-%m-%d') <= '" + map.get("endDate") + "' ");
            }

        }

        List<Map<String, Object>> list = orderProductAnalysisDaoImpl.findBySql(sbf.toString(), Map.class);
        if (!Collections3.isEmpty(list)){
            if (null != list.get(0).get("num")){
                return list.get(0).get("num").toString();
            }
        }
        return "0";
    }
}
