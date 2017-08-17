package com.trekiz.admin.modules.log.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.log.entity.QuauqBusinessLog;
import org.springframework.data.repository.CrudRepository;

/**
 * 系统中业务操作日志
 */
interface QuauqBusinessLogDaoCustom extends BaseDao<QuauqBusinessLog> {
}

public interface QuauqBusinessLogDao extends QuauqBusinessLogDaoCustom, CrudRepository<QuauqBusinessLog, Long> {

}

class QuauqBusinessLogDaoImpl extends BaseDaoImpl<QuauqBusinessLog> implements QuauqBusinessLogDaoCustom{

}