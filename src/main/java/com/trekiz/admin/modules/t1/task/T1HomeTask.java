package com.trekiz.admin.modules.t1.task;

import com.alibaba.fastjson.JSON;
import com.quauq.multi.tenant.datasource.DataSourceContainer;
import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.task.ScheduledTask;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.CacheUtils;
import com.trekiz.admin.modules.t1.service.IT1Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzk on 2016/10/20.
 */
public class T1HomeTask extends ScheduledTask {
    @Autowired
    private IT1Service t1Service;

    @Override
    public void execute() {
        for (String tenant : DataSourceContainer.getTenants()) {
            FacesContext.setCurrentTenant(tenant);
            // 只在tts库执行
            if (tenant.equals("fd358566-f017-459c-a3e9-3b479250a92c")) {
                task();
            }
        }
    }

    @Override
    protected void task() {
        System.out.println("-----T1首页的定时任务开始执行-----");
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
        //如果有先清空，再存储
        if(CacheUtils.get(Context.T1HOMEFOREIGNCACHE) != null){
            CacheUtils.remove(Context.T1HOMEFOREIGNCACHE);
        }
        CacheUtils.put(Context.T1HOMEFOREIGNCACHE, foreighMap);

        // 国内游
        Map<String, Object> inlandMap = new HashMap<String, Object>();
        inlandMap.put("tourOutIn", Context.FREE_TRAVEL_INLAND);
        inlandMap.put("tourOutInList", list);
        t1LogoList = t1Service.getT1LogoList(Context.FREE_TRAVEL_INLAND, null, "false");
        handle(t1LogoList, Context.FREE_TRAVEL_INLAND);
        inlandMap.put("tourInlandDistrict", t1LogoList);
        if(CacheUtils.get(Context.T1HOMEINLANDCACHE) != null){
            CacheUtils.remove(Context.T1HOMEINLANDCACHE);
        }
        CacheUtils.put(Context.T1HOMEINLANDCACHE, inlandMap);

        System.out.println("-----T1首页的定时任务执行结束-----");

    }

    /**
     * 添加批发商信息
     * @param t1LogoList
     * @param tourOutIn
     */
    private void handle(List<Map<String, Object>> t1LogoList, String tourOutIn) {
        for (int i = 0; i < t1LogoList.size(); i++) {
            Map<String, Object> map =  t1LogoList.get(i);
            String tourDistrictId = map.get("tourDistrictId").toString(); // 区域id
            List<Map<String, Object>> suppliers = t1Service.getSuppliers(tourDistrictId, tourOutIn);
            map.put("travelAgency", suppliers);
        }
    }
}
