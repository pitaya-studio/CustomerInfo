package com.trekiz.admin.modules.traveler.repository;

import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.modules.traveler.entity.TravelerOrder;


/**
 * 游客订单关系DAO接口
 * @author taoxiaoyang
 *
 */
public interface TravelerOrderDao extends CrudRepository<TravelerOrder, Long>{

}
