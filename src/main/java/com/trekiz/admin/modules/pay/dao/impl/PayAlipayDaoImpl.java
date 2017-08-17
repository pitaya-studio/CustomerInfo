package com.trekiz.admin.modules.pay.dao.impl;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.pay.dao.PayAlipayDao;
import com.trekiz.admin.modules.pay.entity.PayAlipay;
import org.springframework.stereotype.Repository;

/**
 * Created by quauq on 2016/7/25.
 */
@Repository
public class PayAlipayDaoImpl extends BaseDaoImpl<PayAlipay> implements PayAlipayDao {

    @Override
    public PayAlipay getByUuid(String uuid) {
        Object entity = super.createQuery("from PayAlipay p where p.uuid=? and p.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
        return (PayAlipay)entity;
    }
}
