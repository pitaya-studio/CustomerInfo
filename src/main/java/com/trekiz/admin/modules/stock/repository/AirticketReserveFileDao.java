package com.trekiz.admin.modules.stock.repository;

/**
 * Created by ZhengZiyu on 2014/11/5.
 */

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.stock.entity.AirticketReserveFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 切位信息DAO接口
 * @author zj
 *
 */
public interface AirticketReserveFileDao extends AirticketReserveFileDaoCustom,CrudRepository<AirticketReserveFile, Long> {

    public List<AirticketReserveFile> findByAgentIdAndAirticketActivityId(Long agentId,Long srcActivityId);
    
    public List<AirticketReserveFile> findByAgentIdAndReserveOrderId(Long agentId,Long reserveOrderId);
    
} 

/**
 * 自定义DAO接口
 * @author liangjingming
 *
 */
interface AirticketReserveFileDaoCustom extends BaseDao<AirticketReserveFile> {

}

/**
 * 自定义DAO接口实现
 * @author liangjingming
 *
 */
@Repository
class AirticketReserveFileDaoImpl extends BaseDaoImpl<AirticketReserveFile> implements AirticketReserveFileDaoCustom{

}