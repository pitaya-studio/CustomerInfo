package com.trekiz.admin.modules.log.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.log.entity.LogProductT1;

/**
 * Created by zong on 2016/7/26.
 */
public interface LogProductT1Dao extends BaseDao<LogProductT1> {

    public void save(LogProductT1 logProductT1);
}
