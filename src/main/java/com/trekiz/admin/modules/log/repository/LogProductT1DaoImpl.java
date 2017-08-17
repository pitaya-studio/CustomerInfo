package com.trekiz.admin.modules.log.repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.log.entity.LogProductT1;
import org.springframework.stereotype.Repository;

/**
 * Created by zong on 2016/7/26.
 */
@Repository
public class LogProductT1DaoImpl extends BaseDaoImpl<LogProductT1> implements LogProductT1Dao {
    @Override
    public void save(LogProductT1 logProductT1) {
        this.getSession().save(logProductT1);
    }
}
