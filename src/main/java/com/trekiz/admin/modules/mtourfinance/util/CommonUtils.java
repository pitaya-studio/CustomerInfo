package com.trekiz.admin.modules.mtourfinance.util;

import com.trekiz.admin.modules.mtourfinance.json.OperatingRevenueData;
import com.trekiz.admin.modules.mtourfinance.json.OperatingRevenueJsonBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by quauq on 2016/6/22.
 */
public class CommonUtils {

    /**
     * 营业收入数据方法中转换数据类型。
     * OperatingRevenue = OR
     */
    public static List<OperatingRevenueJsonBean> convertDataForOR(Map<Integer,List<OperatingRevenueData>> result){
        if (null == result)
            return new ArrayList<>();
        List<OperatingRevenueJsonBean> list = new ArrayList<>();
        for (Map.Entry<Integer, List<OperatingRevenueData>> entry : result.entrySet()) {
            OperatingRevenueJsonBean bean = new OperatingRevenueJsonBean();
            OperatingRevenueData data = entry.getValue().get(0);
            bean.setSalerId(data.getSalerId());
            bean.setSalerName(data.getSalerName());
            bean.setOrders(entry.getValue());
            list.add(bean);
        }
        return list;
    }

    /**
     * map中的key是名字，value是出现的次数减1。name出现一次，次数加一。第一次出现的不加后缀，后面再从1开始加。
     * yudong.xu 2016.7.7
     */
    public static String checkName(Map<String,Integer> resource,String name){
        Integer count =  resource.get(name);
        if (count == null) {
            resource.put(name,0);
        }else {
            resource.put(name,++count);
            name = name + count;
        }
        return name;
    }
}
