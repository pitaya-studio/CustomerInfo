package com.trekiz.admin.modules.finance.repository;

import com.trekiz.admin.modules.finance.entity.Settle;

/**
 *
 * Copyright 2016 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，结算单的DAO接口
 * @author shijun.liu
 * @date 2016年05月05日
 */
public interface ISettleDao {

    /**
     * 根据Settle对象是否带有id保存或者更新Settle对象，保存其子对象信息。
     */
    public void saveOrUpdateSettle(Settle settle);

    /**
     * 根据orderType和groupIdUuid查询数据库，返回包括基本信息，订单信息，境内信息，境外信息的Settle对象。
     */
    public Settle findSettle(Integer orderType,String groupIdUuid);

    public Settle findSimpleSettle(Integer orderType,String groupIdUuid);

    public void deleteSub(String entityName,String settleUuid);

}
