package com.trekiz.admin.modules.finance.repository.impl;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.finance.entity.ForeCast;
import com.trekiz.admin.modules.finance.entity.ForeCastIn;
import com.trekiz.admin.modules.finance.entity.ForeCastOrder;
import com.trekiz.admin.modules.finance.entity.ForeCastOther;
import com.trekiz.admin.modules.finance.entity.ForeCastOut;
import com.trekiz.admin.modules.finance.repository.IForeCastDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * Copyright 2016 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，预报单的DAO接口实现类
 * @author shijun.liu
 * @date 2016年05月05日
 */
@Repository
public class ForeCastDaoImpl extends BaseDaoImpl<ForeCast> implements IForeCastDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据ForeCast对象是否带有id保存或者更新ForeCast对象，并保存其子对象信息。
     */
    public void saveOrUpdateForeCast(ForeCast foreCast){
        if (null == foreCast)
            return;
        if (foreCast.getId() == null){
            entityManager.persist(foreCast);
        }else {
            entityManager.merge(foreCast);
        }
        saveOrders(foreCast.getUuid(),foreCast.getExpectedIncome());
        saveInList(foreCast.getUuid(),foreCast.getExpectedInList());
        saveOutList(foreCast.getUuid(),foreCast.getExpectedOutList());
        //骡子假期增加其他收入收款
        if(UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_LZJQ)){
        	saveOthers(foreCast.getUuid(),foreCast.getOtherRecordList());
        }
      }
    private void saveOthers(String forecastUuid,List<ForeCastOther> others){
    	 if (null == others) return;
         for (ForeCastOther other : others) {
        	 other.setForecastUuid(forecastUuid);
        	 other.setUuid(UuidUtils.generUuid());
             entityManager.persist(other);
         }
    }
    private void saveOrders(String forecastUuid,List<ForeCastOrder> orders){
        if (null == orders) return;
        for (ForeCastOrder order : orders) {
            order.setForecastUuid(forecastUuid);
            order.setUuid(UuidUtils.generUuid());
            entityManager.persist(order);
        }
    }

    private void saveInList(String forecastUuid,List<ForeCastIn> inList){
        if (null == inList) return;
        for (ForeCastIn foreCastIn : inList) {
            foreCastIn.setForecastUuid(forecastUuid);
            foreCastIn.setUuid(UuidUtils.generUuid());
            entityManager.persist(foreCastIn);
        }
    }

    private void saveOutList(String forecastUuid,List<ForeCastOut> outList){
        if (null == outList) return;
        for (ForeCastOut foreCastOut : outList) {
            foreCastOut.setForecastUuid(forecastUuid);
            foreCastOut.setUuid(UuidUtils.generUuid());
            entityManager.persist(foreCastOut);
        }
    }

    /**
     * 根据orderType和groupIdUuid查询数据库，返回包括基本信息，订单信息，境内信息，境外信息的ForeCast对象。
     */
    public ForeCast findForeCast(Integer orderType,String groupIdUuid){
        ForeCast foreCast = findSimpleForeCast(orderType,groupIdUuid);
        if (null == foreCast)
            return null;
        List<ForeCastOrder> orders = findOrders(foreCast.getUuid());
        List<ForeCastIn> inList = findInList(foreCast.getUuid());
        List<ForeCastOut> outList = findOutList(foreCast.getUuid());
        foreCast.setExpectedIncome(orders);
        foreCast.setExpectedInList(inList);
        foreCast.setExpectedOutList(outList);
        //其他收入收款 骡子假期
        if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
        	List<ForeCastOther> others = findOthers(foreCast.getUuid());
        	foreCast.setOtherRecordList(others);
        }
        return foreCast;
    }
    
    public List<ForeCastOther> findOthers(String forecastUuid){
    	String jpql = "FROM ForeCastOther WHERE forecastUuid=?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,forecastUuid);
        List<ForeCastOther> list = query.getResultList();
        return list;
    }
    public ForeCast findSimpleForeCast(Integer orderType,String groupIdUuid){
        String jpql = "FROM ForeCast WHERE orderType=?1 AND groupIdUuid=?2";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,orderType).setParameter(2,groupIdUuid);
        List<ForeCast> list = query.getResultList();
        if (null ==list || list.size() == 0)
            return null;
        return list.get(0);
    }

    private List<ForeCastOrder> findOrders(String forecastUuid){
        String jpql = "FROM ForeCastOrder WHERE forecastUuid=?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,forecastUuid);
        List<ForeCastOrder> list = query.getResultList();
        return list;
    }

    private List<ForeCastIn> findInList(String forecastUuid){
        String jpql = "FROM ForeCastIn WHERE forecastUuid=?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,forecastUuid);
        List<ForeCastIn> list = query.getResultList();
        return list;
    }

    private List<ForeCastOut> findOutList(String forecastUuid){
        String jpql = "FROM ForeCastOut WHERE forecastUuid=?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,forecastUuid);
        List<ForeCastOut> list = query.getResultList();
        return list;
    }

    //删除订单收款，境内外付款item。
    public void deleteSub(String entityName,String forecastUuid){
        StringBuilder jpql = new StringBuilder();
        jpql.append("DELETE FROM ").append(entityName).append(" WHERE forecastUuid=?1");
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter(1,forecastUuid);
        query.executeUpdate();
    }




}
