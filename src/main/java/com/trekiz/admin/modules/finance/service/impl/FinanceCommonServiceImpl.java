package com.trekiz.admin.modules.finance.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.modules.finance.repository.IFinanceCommonDao;
import com.trekiz.admin.modules.finance.service.IFinanceCommonService;
import com.trekiz.admin.modules.finance.service.ISettleService;
import com.trekiz.admin.modules.island.util.StringUtil;

/**
 * Created by quauq on 2016/7/14.
 */
@Service
@Transactional(readOnly = true)
public class FinanceCommonServiceImpl implements IFinanceCommonService {

    @Autowired
    private IFinanceCommonDao financeCommonDao;
    @Autowired
    private ISettleService settleService;

    private Map<Object,BigDecimal[]> handleResult(List<Map<String,Object>> result){
        Map<Object,BigDecimal[]> group = new HashMap<>();
        BigDecimal zero = new BigDecimal(0);
        for (Map<String, Object> map : result) {
            Map<Object,String> userMap = financeCommonDao.getUserNameDept((Integer)map.get("salerId"));
            map.put("userName",userMap.get("userName"));
            String deptNames = userMap.get("deptNames");
            map.put("deptNames",deptNames);
            map.put("deptName",getLimitedVal(deptNames,0));

            String unAgentNames = (String)map.get("unAgentNames");
            map.put("unAgentName",getLimitedVal(unAgentNames,0));

            String agentNames = (String)map.get("agentNames");
            map.put("agentName",getLimitedVal(agentNames,0));

            BigInteger orderType = (BigInteger)map.get("orderType");
            Integer activityId = (Integer)map.get("activityId");
            Map<String,Object> settleMap = settleService.getSettleMap(orderType.intValue(),activityId.longValue(),null);
            String outMoneySum = ((String)settleMap.get("outMoneySum")).replace(",","");
            String orderPersonNum = (String)settleMap.get("orderPersonNum");
            if (StringUtil.isBlank(outMoneySum) || StringUtil.isBlank(orderPersonNum) || "0".equals(orderPersonNum)){
                map.put("costPerPerson","");
            }else {
                BigDecimal costPerPerson = new BigDecimal(outMoneySum).divide(new BigDecimal(orderPersonNum),2,BigDecimal.ROUND_HALF_UP);
                if (zero.compareTo(costPerPerson) == 0){
                    map.put("costPerPerson","");
                } else {
                    map.put("costPerPerson","￥" + costPerPerson);
                }
            }
            String profitRate = (String)settleMap.get("profitRate");
            BigDecimal rate = null;
            if (StringUtil.isNotBlank(profitRate)){
                rate = new BigDecimal(profitRate.substring(0,profitRate.length()-1));
                rate = rate.divide(new BigDecimal(100));
            }
            String moneySerial = (String)map.get("accountedMoney");
            String[] moneyArr = getCurrencyMoneySum(moneySerial,rate,group);
            map.put("accountedMoney",moneyArr[0]);
            map.put("profit",moneyArr[1]);
        }
        return group;
    }

    /**
     * 用于导出Excel统计用的获取数据。 yudong.xu 2016.7.22
     */
    public  Map<String,Object> getSalesPerformance( Map<String,Object> params){
        List<Map<String,Object>> result = financeCommonDao.getSalesPerformance(params);
        Map<Object,BigDecimal[]> sumInfo = handleResult(result);
        Map<String,Object> map = new HashMap<>();
        map.put("result",result);
        map.put("sumInfo",sumInfo);
        return map;
    }

    /**
     * 页面用的获取数据。 yudong.xu 2016.7.22
     */
    public void getSalesPerformance(Page<Map<String,Object>> page, Map<String,Object> params){
        financeCommonDao.getSalesPerformance(page,params);
        List<Map<String,Object>> result = page.getList();
        handleResult(result);
    }

    public String[] getCurrencyMoneySum(String serials,BigDecimal rate,Map<Object,BigDecimal[]> group){
        List<Map<String,Object>> result = financeCommonDao.getCurrencyAmount(serials);
        if (CollectionUtils.isEmpty(result) || rate == null)
            return new String[]{"",""};
        Map<Object,BigDecimal> subGroup = new HashMap<>();
        for (Map<String, Object> map : result) {
            String mark = (String) map.get("mark");
            BigDecimal amount =(BigDecimal)map.get("amount");
            if (subGroup.containsKey(mark)){
                amount = subGroup.get(mark).add(amount);
                subGroup.put(mark,amount);
            }else {
                subGroup.put(mark,amount);
            }
        }
        StringBuilder amountBuilder = new StringBuilder();
        StringBuilder profitBuilder = new StringBuilder();
        BigDecimal zero = new BigDecimal(0);
        for (Map.Entry<Object, BigDecimal> entry : subGroup.entrySet()) {
            Object key = entry.getKey(); //币种符号
            BigDecimal val = entry.getValue();
            if (val != null && val.compareTo(zero) != 0){
                BigDecimal profit = val.multiply(rate); //乘以毛利得到利润
                if (group.containsKey(key)){ // 总的统计
                    BigDecimal[] values = group.get(key);
                    values[0] = values[0].add(val); //统计金额
                    values[1] = values[1].add(profit); //统计毛利
                }else {
                    group.put(key,new BigDecimal[]{val,profit});
                }
                String amountStr = MoneyNumberFormat.getRoundMoney(val,2,BigDecimal.ROUND_HALF_UP);
                amountBuilder.append(key).append(amountStr).append("+");
                String profitStr = MoneyNumberFormat.getRoundMoney(profit,2,BigDecimal.ROUND_HALF_UP);
                profitBuilder.append(key).append(profitStr).append("+");
            }
        }
        String[] moneyArr = new String[2];
        if (amountBuilder.length() > 0){
            moneyArr[0] = amountBuilder.substring(0,amountBuilder.length()-1);
            moneyArr[1] = profitBuilder.substring(0,profitBuilder.length()-1);
        } else {
            moneyArr[0] = "";
            moneyArr[1] = "";;
        }
        return moneyArr;
    }


    public String getLimitedVal(String str,Integer idx){
        if (StringUtil.isBlank(str))
            return "";
        String[] array = str.split(",");
        int length = array.length;
        StringBuilder result = new StringBuilder();
        if (idx + 1 > length || idx < 0){
            idx = length - 1;
        }
        for (int i = 0; i < idx; i++) {
            result.append(array[i]).append(",");
        }
        result.append(array[idx]);
        if (idx < length -1){
            result.append("...");
        }
        return result.toString();
    }
}
