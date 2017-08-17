package com.trekiz.admin.modules.traveler.repository;

import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.modules.visa.entity.OrderTravelerPayRelation;


/**
 * 游客订单关系DAO接口
 * @author taoxiaoyang
 *
 */
public interface OrderTravelerPayRelationDao extends CrudRepository<OrderTravelerPayRelation, String>{

}
