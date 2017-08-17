package com.trekiz.admin.modules.finance.service;

import com.trekiz.admin.modules.finance.entity.ForeCast;

import java.util.Map;

/**
 *
 * Copyright 2016 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，预报单的Service接口
 * @author shijun.liu
 * @date 2016年05月05日
 */
public interface IForeCastService {

    public Map<String,Object> getForeCast(Integer orderType, Long groupId, String groupUuid);

    /**
     * 返回ForeCast对象类型的预报单数据，包含子条目的数据。
     */
    public ForeCast getForeCastObj(Integer orderType, Long groupId, String groupUuid);
    /**
     * 返回ForeCast对象类型的预报单数据,不包含子表的数据。
     */
    public ForeCast getSimpleForeCast(Integer orderType,String groupIdUuid);

    public ForeCast getSimpleForeCast(Integer orderType,Long groupId,String groupUuid);

    public void saveForeCastByMap(Integer orderType,String groupIdUuid,Map<String,Object> resultMap);

    public void saveForeCastByMap(Integer orderType,Long groupId,String groupUuid,Map<String,Object> resultMap);
    
    public void updateForeCast(ForeCast foreCast);
    
    public void saveForeCast(ForeCast foreCast);

    //将拉美图的xml类型的团期预报单数据保存到数据库中。
    public String xml2DatabaseLMT();
}
