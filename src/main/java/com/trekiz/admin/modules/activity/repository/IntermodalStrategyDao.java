package com.trekiz.admin.modules.activity.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.IntermodalStrategy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZhengZiyu on 2014/9/3.
 */
public interface IntermodalStrategyDao extends IntermodalStrategyDaoCustom, CrudRepository<IntermodalStrategy, Long> {

    @Query("from IntermodalStrategy where activityAirTicket.id = ?1")
    public List<IntermodalStrategy> findIntermodalStrategyByActivity(Long activityId);

    @Modifying
    @Query("delete from IntermodalStrategy where activityAirTicket.id = ?1")
    public void deleteIntermodalStrategyByActivity(Long activityId);
}

interface IntermodalStrategyDaoCustom extends BaseDao<IntermodalStrategy> {

}

@Repository
class IntermodalStrategyDaoImpl extends BaseDaoImpl<IntermodalStrategy> implements IntermodalStrategyDaoCustom{

}

