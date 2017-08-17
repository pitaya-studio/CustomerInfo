package com.trekiz.admin.modules.statisticAnalysis.cache.service.impl;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.CacheUtils;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.statisticAnalysis.cache.service.IStatisticAnalysisCacheService;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计分析业务缓存接口实现类
 *
 * @author mbmr
 * @date 2017-01-03
 */
public class IStatisticAnalysisCacheServiceImpl implements IStatisticAnalysisCacheService {


    @Override
    public Map<String, Object> getCacheData(Map<String, Object> map) {
        String orderType = (String) map.get("orderType");//产品类型（类似：0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 7：机票 10：游轮）
        Map<String, Object> returnMap = new HashMap<>();

        if (orderType.equals(Context.PRODUCT_TYPE_DAN_TUAN)) {//单团
            returnMap = getOrderTypeMap(map);
        } else if (orderType.equals(Context.PRODUCT_TYPE_SAN_PIN)) {//散拼
            returnMap = getOrderTypeMap(map);
        } else if (orderType.equals(Context.PRODUCT_TYPE_QIAN_ZHENG)) {//签证
            returnMap = getOrderTypeMap(map);
        } else if (orderType.equals("0")) {//全部
            returnMap = getOrderTypeMap(map);
        }

        return returnMap;
    }

    /**
     * 获取不同产品类型的缓存数据
     *
     * @param map
     * @return
     */
    private Map<String, Object> getOrderTypeMap(Map<String, Object> map) {
        String analysisType = (String) map.get("analysisType");//分析类型(1：订单数，2：收客人数，3：订单金额)

        Map<String, Object> returnMap = null;
        if (analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)) {//订单数
            returnMap = getAnalysisTypeMap(map);
        } else if (analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)) {//收客人数
            returnMap = getAnalysisTypeMap(map);
        } else if (analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_MONEY)) {//收款金额
            returnMap = getAnalysisTypeMap(map);
        }
        return returnMap;
    }

    /**
     * 获取不同分析类型的缓存数据
     *
     * @param map
     * @return
     */
    private Map<String, Object> getAnalysisTypeMap(Map<String, Object> map) {
        String searchDate = (String) map.get("searchDate");//时间(1：今日 2：本周 3：本月 4：本年 5：全部)
        String startDate = (String) map.get("startDate");//自定义起始日期
        String endDate = (String) map.get("endDate");//自定义截止日期

        Map<String, Object> returnMap = null;
        if (searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)) {//今日
            returnMap = getCacheDataDetail(map);
        } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_WEEK)) {//本周
            returnMap = getCacheDataDetail(map);
        } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {//本月
            returnMap = getCacheDataDetail(map);
        } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {//本年
            returnMap = getCacheDataDetail(map);
        } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_ALL)) {//全部
            returnMap = getCacheDataDetail(map);
        } else {
            if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
                returnMap = getCacheDataDetail(map);
            }
        }
        return returnMap;
    }

    /**
     * 获取不同时间维度的缓存数据
     *
     * @param map
     * @return
     */
    private Map<String, Object> getCacheDataDetail(Map<String, Object> map) {
        String analysisType = (String) map.get("analysisType");//分析类型(1：订单数，2：收客人数，3：订单金额)
        String searchDate = (String) map.get("searchDate");//时间(1：今日 2：本周 3：本月 4：本年 5：全部)
        String startDate = (String) map.get("startDate");//自定义起始日期
        String endDate = (String) map.get("endDate");//自定义截止日期
        String orderType = (String) map.get("orderType");//产品类型（类似：0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 7：机票 10：游轮）
        String unit = (String) map.get("unit");//查询单位（查询维度）(h:时、d:天、w:周、m:月)

        Map<String, Object> mapList = null;
        Map<String, Object> returnMapList = new HashMap<>();
        if (unit.equals("h")) {//小时
            mapList = (Map<String, Object>) CacheUtils.get(orderType + "_" + analysisType + "_" + searchDate + "_" + startDate + "_" + endDate + "_" + unit);
            if (mapList != null) {
                returnMapList.put("list", mapList.get("list"));
                returnMapList.put("newNum", mapList.get("newNum"));
                returnMapList.put("incrementRate", mapList.get("incrementRate"));
                returnMapList.put("maxNum", mapList.get("maxNum"));
                returnMapList.put("serverTime", mapList.get("serverTime"));
            }
        } else if (unit.equals("d")) {//天
            mapList = (Map<String, Object>) CacheUtils.get(orderType + "_" + analysisType + "_" + searchDate + "_" + startDate + "_" + endDate + "_" + unit);
            if (mapList != null) {
                returnMapList.put("list", mapList.get("list"));
                returnMapList.put("newNum", mapList.get("newNum"));
                returnMapList.put("incrementRate", mapList.get("incrementRate"));
                returnMapList.put("maxNum", mapList.get("maxNum"));
                returnMapList.put("serverTime", mapList.get("serverTime"));
            }
        } else if (unit.equals("w")) {//周
            mapList = (Map<String, Object>) CacheUtils.get(orderType + "_" + analysisType + "_" + searchDate + "_" + startDate + "_" + endDate + "_" + unit);
            if (mapList != null) {
                returnMapList.put("list", mapList.get("list"));
                returnMapList.put("newNum", mapList.get("newNum"));
                returnMapList.put("incrementRate", mapList.get("incrementRate"));
                returnMapList.put("maxNum", mapList.get("maxNum"));
                returnMapList.put("serverTime", mapList.get("serverTime"));
            }
        } else if (unit.equals("m")) {//月
            mapList = (Map<String, Object>) CacheUtils.get(orderType + "_" + analysisType + "_" + searchDate + "_" + startDate + "_" + endDate + "_" + unit);
            if (mapList != null) {
                returnMapList.put("list", mapList.get("list"));
                returnMapList.put("newNum", mapList.get("newNum"));
                returnMapList.put("incrementRate", mapList.get("incrementRate"));
                returnMapList.put("maxNum", mapList.get("maxNum"));
                returnMapList.put("serverTime", mapList.get("serverTime"));
            }
        }

        return returnMapList;
    }

    @Override
    public void removeCacheData(Map<String, Object> map) {
        String analysisType = (String) map.get("analysisType");//分析类型(1：订单数，2：收客人数，3：订单金额)
        String searchDate = (String) map.get("searchDate");//时间(1：今日 2：本周 3：本月 4：本年 5：全部)
        String startDate = (String) map.get("startDate");//自定义起始日期
        String endDate = (String) map.get("endDate");//自定义截止日期
        String orderType = (String) map.get("orderType");//产品类型（类似：0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 7：机票 10：游轮）
        String unit = (String) map.get("unit");//查询单位（查询维度）(h:时、d:天、w:周、m:月)

        Map<String, Object> mapList = getCacheData(map);
        if (mapList != null) {
            CacheUtils.remove(orderType + "_" + analysisType + "_" + searchDate + "_" + startDate + "_" + endDate + "_" + unit);
        }
    }

    @Override
    public void updateCacheData(Map<String, Object> map, Map<String, Object> dataMap) {
        String analysisType = (String) map.get("analysisType");//分析类型(1：订单数，2：收客人数，3：订单金额)
        String searchDate = (String) map.get("searchDate");//时间(1：今日 2：本周 3：本月 4：本年 5：全部)
        String startDate = (String) map.get("startDate");//自定义起始日期
        String endDate = (String) map.get("endDate");//自定义截止日期
        String orderType = (String) map.get("orderType");//产品类型（类似：0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 7：机票 10：游轮）
        String unit = (String) map.get("unit");//查询单位（查询维度）(h:时、d:天、w:周、m:月)

        String flushCache = (String) map.get("flush");//手动刷新页面(1：刷新)

        Map<String, Object> mapList = getCacheData(map);
        if (mapList != null) {
            if (flushCache.equals("1")) {
                removeCacheData(map);
            }
        }
        CacheUtils.put(orderType + "_" + analysisType + "_" + searchDate + "_" + startDate + "_" + endDate + "_" + unit, dataMap);
    }
}
