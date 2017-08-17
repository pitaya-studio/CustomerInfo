package com.trekiz.admin.modules.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.Visa;

public interface VisaDao extends VisaDaoCustom, CrudRepository<Visa, Long>{
	
	@Query("select t.id, v.totalDeposit, v.visaStauts from Traveler t,Visa v where t.orderId = ?1 and t.id = v.travelerId and delFlag in (0,3,5) and v.guaranteeStatus=" + Visa.GUARANTEE_NOMEL_NONE)
	public List<Object[]> findTravelerVisaByOrderId(Long orderId);
	
	@Query("select t.id, v.totalDeposit, v.accountedDeposit from Traveler t, Visa v, MoneyAmount m where t.orderId = ?1 and t.id = v.travelerId and t.orderType = " + Context.PRODUCT_TYPE_QIAN_ZHENG + " and t.delFlag in (0,3,5) and v.accountedDeposit = m.serialNum and m.amount > 0")
	public List<Object[]> findTravelerDepositRefundVisaByOrderId(Long orderId);
	
	@Query("select t.id, v.totalDeposit, v.accountedDeposit, o.orderNo from Traveler t, Visa v, VisaOrder o, MoneyAmount m where t.id in ?1 and t.id = v.travelerId and t.orderType = " + Context.PRODUCT_TYPE_QIAN_ZHENG + " and t.delFlag in (0,3,5) and v.accountedDeposit = m.serialNum and m.amount > 0 and t.orderId=o.id")
	public List<Object[]> findTravelerDepositRefundVisaByTravelerIds(List<Long> travelerIdList);
	
	@Query("select id, payPriceSerialNum from Traveler where orderId = ?1 and orderType = " + Context.PRODUCT_TYPE_QIAN_ZHENG + " and delFlag in (0,3,5) and accountedMoney is not null")
	public List<Object[]> findTravelerRefundVisaByOrderId(Long orderId);
	
	@Query("select t.id, v.totalDeposit, v.accountedDeposit from Traveler t,Visa v where t.id = ?1 and t.id = v.travelerId")
	public List<Object[]> findTravelerVisaByTravelerId(Long travelerId);
	
	@Query("select t.id, t.name, v.visaStauts, v.contract, v.totalDeposit, t.passportCode, t.passportValidity " +
			"from Traveler t, Visa v " +
			"where t.orderId = ?1 and t.orderType = ?2 and t.id = v.travelerId and t.mainOrderId is null and delFlag = '" + Context.DEL_FLAG_NORMAL + "'")
	public List<Object[]> findJoinGroupTravelerVisa(Long orderId, Integer orderType);
	
	@Query("select t.id, t.name, v.visaStauts, v.contract, v.totalDeposit, t.passportCode, t.passportValidity " +
			"from Traveler t, Visa v " +
			"where t.id = ?1 and t.id = v.travelerId and t.mainOrderId is null and delFlag = '" + Context.DEL_FLAG_NORMAL + "'")
	public List<Object[]> findJoinGroupByTravelerId(Long travelerId);
	
	/**
	 * 根据传入的Traveler类的id查询单个游客
	 */
	@Query("select t.id, t.name, v.visaStauts, v.contract, v.totalDeposit, t.passportCode, t.passportValidity " +
			"from Traveler t, Visa v " +
			"where t.orderId = ?1 and t.orderType = ?2 and t.id = ?3 and t.id = v.travelerId and delFlag = '" + Context.DEL_FLAG_NORMAL + "'")
	public List<Object[]> findJoinGroupTravelerVisaForOne(Long orderId, Integer orderType,Long travelerId);

	
	public Visa findByTravelerId(Long travelerId);
	
	@Modifying
	@Query("delete from Visa where travelerId = ?1")
	public void delVisaByTravelerId(Long travelerId);
	
	@Query("from Visa where travelerId = ?1")
	public List<Visa> findByTravelerId1(Long travelerId);
	
	@Query("select t.id, t.name, o.orderNo, v.totalDeposit, v.accountedDeposit, v.guaranteeStatus, o.id from Traveler t, Visa v, VisaOrder o where t.id in ?1 and t.id = v.travelerId and t.orderType = " + Context.PRODUCT_TYPE_QIAN_ZHENG + " and t.orderId=o.id and delFlag!=1")
	public List<Object[]> findTravelerInfoByIdsToRefund(List<Long> travelerIds);
}
interface VisaDaoCustom extends BaseDao<Visa> {

}

@Repository
class VisaDaoImpl extends BaseDaoImpl<Visa> implements VisaDaoCustom {

}