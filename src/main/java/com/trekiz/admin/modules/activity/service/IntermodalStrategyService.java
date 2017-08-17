package com.trekiz.admin.modules.activity.service;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.activity.entity.IntermodalStrategy;
import com.trekiz.admin.modules.activity.repository.IntermodalStrategyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZhengZiyu on 2014/9/2.
 */
@Service
@Transactional(readOnly = true)
public class IntermodalStrategyService extends BaseService {

    @Autowired
    private IntermodalStrategyDao intermodalStrategyDao;

    public List<IntermodalStrategy> getActivityIntermodalStrategies(Long activityId){
        return this.intermodalStrategyDao.findIntermodalStrategyByActivity(activityId);
    }

    public IntermodalStrategy getOne(Long id){
        return this.intermodalStrategyDao.findOne(id);
    }
}
