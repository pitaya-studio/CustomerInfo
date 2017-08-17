package com.trekiz.admin.modules.statisticAnalysis.cache.service;

import org.apache.http.HttpRequest;

import java.util.List;
import java.util.Map;

/**
 * 统计分析业务缓存接口
 * @author mbmr
 * @date 2017-01-03
 */
public interface IStatisticAnalysisCacheService {

    /**
     * 从缓存中获取数据
     * @param map
     * @return
     */
    public Object getCacheData(Map<String,Object> map);

    /**
     * 从缓存中移除数据
     * @param map
     */
    public void removeCacheData(Map<String,Object> map);

    /**
     * 往缓存中存储或更新数据
     * @param map
     * @param dataMap
     */
    public void updateCacheData(Map<String,Object> map,Map<String, Object> dataMap);



}
