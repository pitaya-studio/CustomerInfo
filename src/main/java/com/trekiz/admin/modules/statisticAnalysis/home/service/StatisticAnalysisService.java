package com.trekiz.admin.modules.statisticAnalysis.home.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by quauq on 2016/12/22.
 */
public interface StatisticAnalysisService {

    //public boolean findOrderTypeIsExist();

    public List findOrderNum(Map<String ,String> map) throws  Exception;

    public String getTimeMax();

    public String getTimeMin();

    public void addAmountByOrderIdAndType(BigDecimal increment, String orderId, String orderType);

    public void updateStatisticRecord(Long orderId,Integer orderType);


}
