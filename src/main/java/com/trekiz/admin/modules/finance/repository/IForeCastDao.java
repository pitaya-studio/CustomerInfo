package com.trekiz.admin.modules.finance.repository;

import com.trekiz.admin.modules.finance.entity.ForeCast;

/**
 *
 * Copyright 2016 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，预报单的DAO接口
 * @author shijun.liu
 * @date 2016年05月05日
 */
public interface IForeCastDao{

    /**
     * 根据ForeCast对象是否带有id保存或者更新ForeCast对象，保存其子对象信息。
     */
    public void saveOrUpdateForeCast(ForeCast foreCast);

    /**
     * 根据orderType和groupIdUuid查询数据库，返回包括基本信息，订单信息，境内信息，境外信息的ForeCast对象。
     */
    public ForeCast findForeCast(Integer orderType,String groupIdUuid);

    public ForeCast findSimpleForeCast(Integer orderType,String groupIdUuid);

    //删除订单收款，境内外付款item。
    public void deleteSub(String entityName,String forecastUuid);


}
