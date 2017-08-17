package com.trekiz.admin.modules.statisticAnalysis.home.dao;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * Created by quauq on 2016/12/22.
 */
public interface StatisticAnalysisDao extends BaseDao {

    public String getTimeMax();

    public String getTimeMin();

    //订单数
    public String findOrderNumByHour(String time, Map map);
    public String findOrderNumByDay(String time, Map map);
    public String findOrderNumByWeek(String time, Map<String, String> map);
    public String findOrderNumByMonth(String time, Map<String, String> map);
    public String findOrderNumByWeekAndMonth(String time, Map map);
    public String findOrderNumByYear(String time, Map map);

    //订单人数
    public String findPersonNumByHour(String time, Map map);
    public String findPersonNumByDay(String time, Map map);
    public String findPersonNumByWeekAndMonth(String time, Map map);
    public String findPersonNumByWeek(String time, Map<String, String> map);
    public String findPersonNumByMonth(String time, Map<String, String> map);
    public String findPersonNumByYear(String time, Map map);

    //订单金额
    public  String findMoneyAmountByHour(String time, Map map);
    public  String findMoneyAmountByDay(String time, Map map);
    public String findMoneyAmountByWeekAndMonth(String time, Map map);
    public  String findMoneyAmountByWeek(String time, Map<String, String> map);
    public  String findMoneyAmountByMonth(String time, Map<String, String> map);
    public  String findMoneyAmountByYear(String time, Map map);

    // 更新数据接口
    public void addAmountByOrderIdAndType(BigDecimal increment, String orderId, String orderType);

    // 根据entity是否存在id，保存或者更新entity对象。
    public void saveOrUpdateEntity(OrderDataStatistics entity);

    // 获取订单数据
    public Map<String,Object> getTheLatestOrderInfo(Long orderId,Integer orderType);
    public OrderDataStatistics getEntityByOrderIdAndType(Long orderId, Integer orderType);
}
