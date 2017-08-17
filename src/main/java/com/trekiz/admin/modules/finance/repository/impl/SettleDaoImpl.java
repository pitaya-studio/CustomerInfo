package com.trekiz.admin.modules.finance.repository.impl;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.finance.entity.*;
import com.trekiz.admin.modules.finance.repository.ISettleDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 *
 * Copyright 2016 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，结算单的DAO接口实现类
 * @author shijun.liu
 * @date 2016年05月05日
 */
@Repository
public class SettleDaoImpl implements ISettleDao{

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据Settle对象是否带有id保存或者更新Settle对象，保存其子对象信息。
     */
    public void saveOrUpdateSettle(Settle settle){
        if (null == settle)
            return;
        if (settle.getId() == null){
            entityManager.persist(settle);
        }else {
            entityManager.merge(settle);
        }
        saveOrders(settle.getUuid(),settle.getActualIncome());
        saveInList(settle.getUuid(),settle.getActualInList());
        saveOutList(settle.getUuid(),settle.getActualOutList());
        if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
        	saveOthers(settle.getUuid(),settle.getOtherRecordList());
        }
    }
    private void saveOthers(String settleUuid,List<SettleOther> others){
    	if(null == others) return;
        for (SettleOther other : others) {
        	other.setSettleUuid(settleUuid);
        	other.setUuid(UuidUtils.generUuid());
            entityManager.persist(other);
        }
    }
    
    private void saveOrders(String settleUuid,List<SettleOrder> orders){
        if(null == orders) return;
        for (SettleOrder order : orders) {
            order.setSettleUuid(settleUuid);
            order.setUuid(UuidUtils.generUuid());
            entityManager.persist(order);
        }
    }

    private void saveInList(String settleUuid,List<SettleIn> inList){
        if (null == inList) return;
        for (SettleIn settleIn : inList) {
            settleIn.setSettleUuid(settleUuid);
            settleIn.setUuid(UuidUtils.generUuid());
            entityManager.persist(settleIn);
        }
    }

    private void saveOutList(String settleUuid,List<SettleOut> outList){
        if (null == outList) return;
        for (SettleOut settleOut : outList) {
            settleOut.setSettleUuid(settleUuid);
            settleOut.setUuid(UuidUtils.generUuid());
            entityManager.persist(settleOut);
        }
    }

    /**
     * 根据orderType和groupIdUuid查询数据库，返回包括基本信息，订单信息，境内信息，境外信息的Settle对象。
     */
    public Settle findSettle(Integer orderType,String groupIdUuid){
        Settle settle = findSimpleSettle(orderType,groupIdUuid);
        if (null == settle)
            return null;
        List<SettleOrder> orders = findOrders(settle.getUuid());
        List<SettleIn> inList = findInList(settle.getUuid());
        List<SettleOut> outList = findOutList(settle.getUuid());
        settle.setActualIncome(orders);
        settle.setActualInList(inList);
        settle.setActualOutList(outList);
        if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
        	List<SettleOther> others = findOthers(settle.getUuid());
        	settle.setOtherRecordList(others);
        }
        return settle;
    }

    public Settle findSimpleSettle(Integer orderType,String groupIdUuid){
        String jpql = "FROM Settle WHERE orderType=?1 AND groupIdUuid=?2";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,orderType).setParameter(2,groupIdUuid);
        List<Settle> list = query.getResultList();
        if (null ==list || list.size() == 0)
            return null;
        return list.get(0);
    }

    private List<SettleOrder> findOrders(String settleUuid){
        String jpql = "FROM SettleOrder WHERE settleUuid=?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,settleUuid);
        List<SettleOrder> list = query.getResultList();
        return list;
    }
    private List<SettleOther> findOthers(String settleUuid){
        String jpql = "FROM SettleOther WHERE settleUuid=?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,settleUuid);
        List<SettleOther> list = query.getResultList();
        return list;
    }
    private List<SettleIn> findInList(String settleUuid){
        String jpql = "FROM SettleIn WHERE settleUuid=?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,settleUuid);
        List<SettleIn> list = query.getResultList();
        return list;
    }

    private List<SettleOut> findOutList(String settleUuid){
        String jpql = "FROM SettleOut WHERE settleUuid=?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,settleUuid);
        List<SettleOut> list = query.getResultList();
        return list;
    }

    //删除订单收款，境内外付款item。
    public void deleteSub(String entityName,String settleUuid){
        StringBuilder jpql = new StringBuilder();
        jpql.append("DELETE FROM ").append(entityName).append(" WHERE settleUuid=?1");
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter(1,settleUuid);
        query.executeUpdate();
    }
}
