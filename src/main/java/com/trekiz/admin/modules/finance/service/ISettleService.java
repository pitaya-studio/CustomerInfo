package com.trekiz.admin.modules.finance.service;

import com.trekiz.admin.modules.cost.entity.CostRecordVO;
import com.trekiz.admin.modules.finance.entity.Settle;

import java.util.Map;

/**
 *
 * Copyright 2016 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，结算单的Service接口
 * @author shijun.liu
 * @date 2016年05月05日
 */
public interface ISettleService {
    
    public Map<String,Object> getSettle(Integer orderType, Long groupId, String groupUuid);

    /**
     * 获取Settle对象的类型的结算单数据
     */
    public Settle getSettleObj(Integer orderType, Long groupId, String groupUuid);

    /**
     * 获取Settle对象的类型的结算单数据
     */
    public Settle getSimpleSettleObj(Integer orderType,String groupIdUuid);

    public Settle getSimpleSettleObj(Integer orderType,Long groupId,String groupUuid);

    public void saveSettleByMap(Integer orderType,String groupIdUuid,Map<String,Object> resultMap);

    public void saveSettleByMap(Integer orderType,Long groupId,String groupUuid,Map<String,Object> resultMap);

    public void updateSettle(Settle settle);
    
    public void saveSettle(Settle settle);

    /**
     * 获取结算单对象
     * @param orderType   订单类型
     * @param groupId     团期或者产品ID(机票、签证为产品ID)
     * @param groupUuid   海岛酒店团期ID
     * @return
     * @date    2016.07.13
     * @author  shijun.liu
     */
    public Map<String, Object> getSettleMap(Integer orderType, Long groupId, String groupUuid);

    /**
     * 将CostRecordVO转换为Map对象
     * @param costRecordVO    CostRecordVO对象
     * @param orderType       订单类型
     * @param type            0，预报单，1，结算单
     * @return
     */
    public Map<String, Object> castCostRecordVO2Map(CostRecordVO costRecordVO, Integer orderType, Integer type);
}
