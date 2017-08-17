package com.trekiz.admin.modules.traveler.repository;

import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.modules.visa.entity.OrderPayRelation;


/**
 * 游客订单关系DAO接口
 * @author taoxiaoyang
 *
 */
public interface OrderPayRelationDao extends CrudRepository<OrderPayRelation, String>{

}
