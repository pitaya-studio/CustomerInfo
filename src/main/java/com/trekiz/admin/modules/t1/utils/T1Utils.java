package com.trekiz.admin.modules.t1.utils;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.CacheUtils;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.t1.service.IT1Service;
import com.trekiz.admin.modules.t1.service.impl.T1ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzk on 2016/11/15.
 * T1工具类
 */
public class T1Utils {
    private static IT1Service t1Service = SpringContextHolder.getBean(T1ServiceImpl.class);

    /**
     * 当T1有上下架操作时更新缓存
     */
    public static synchronized void updateT1HomeCache() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("id", Context.FREE_TRAVEL_FOREIGN);
        m1.put("name", Context.FREE_TRAVEL_FOREIGN_CHINA);
        list.add(m1);

        Map<String, Object> m2 = new HashMap<String, Object>();
        m2.put("id", Context.FREE_TRAVEL_INLAND);
        m2.put("name", Context.FREE_TRAVEL_INLAND_CHINA);
        list.add(m2);

        // 出境游
        Map<String, Object> foreighMap = new HashMap<String, Object>();
        foreighMap.put("tourOutIn", Context.FREE_TRAVEL_FOREIGN);
        foreighMap.put("tourOutInList", list);
        List<Map<String, Object>> t1LogoList = t1Service.getT1LogoList(Context.FREE_TRAVEL_FOREIGN, null, "false");
        handle(t1LogoList, Context.FREE_TRAVEL_FOREIGN);
        foreighMap.put("tourForeignDistrict", t1LogoList);
        CacheUtils.put(Context.T1HOMEFOREIGNCACHE, foreighMap);

        // 国内游
        Map<String, Object> inlandMap = new HashMap<String, Object>();
        inlandMap.put("tourOutIn", Context.FREE_TRAVEL_INLAND);
        inlandMap.put("tourOutInList", list);
        t1LogoList = t1Service.getT1LogoList(Context.FREE_TRAVEL_INLAND, null, "false");
        handle(t1LogoList, Context.FREE_TRAVEL_INLAND);
        inlandMap.put("tourInlandDistrict", t1LogoList);
        CacheUtils.put(Context.T1HOMEINLANDCACHE, inlandMap);
    }

    /**
     * 添加批发商信息
     * @param t1LogoList
     * @param tourOutIn
     */
    private static void handle(List<Map<String, Object>> t1LogoList, String tourOutIn) {
        for (int i = 0; i < t1LogoList.size(); i++) {
            Map<String, Object> map =  t1LogoList.get(i);
            String tourDistrictId = map.get("tourDistrictId").toString(); // 区域id
            List<Map<String, Object>> suppliers = t1Service.getSuppliers(tourDistrictId, tourOutIn);
            map.put("travelAgency", suppliers);
        }
    }
}
