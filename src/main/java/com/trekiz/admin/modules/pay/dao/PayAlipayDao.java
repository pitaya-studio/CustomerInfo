package com.trekiz.admin.modules.pay.dao;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.pay.entity.PayAlipay;

/**
 * Created by quauq on 2016/7/25.
 */
public interface PayAlipayDao extends BaseDao<PayAlipay> {

    public PayAlipay getByUuid(String uuid);
}
