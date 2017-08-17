package com.trekiz.admin.modules.visa.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.order.entity.OrderContacts;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.visa.entity.VisaOrder;

public interface VisaOrderDao extends VisaOrderDaoCustom, CrudRepository<VisaOrder, Long>{
	
	/**
	 * 查看预约表
	 * @author jiachen
	 * @DateTime 2014-12-16 下午12:24:23
	 * @return List<Object>
	 */
	@Query(value="SELECT "
			+//[0]游客姓名
			"t.name, " 
			+//1英文名
			"t.nameSpell, "
			+//2性别
			"t.sex, " 
			+//3约签日期
			"v.contract, " 
			+//4约签国家
			"p.sysCountryId, " 
			+//5签证类型
			"p.visaType, " 
			+//6领区 
			"dict.label, "
			+//7护照ID
			"t.passportCode, " 
			+//8出生日期
			"t.birthDay, " 
			+//9销售
			"o.salerName, " 
			+//10签证状态
			"v.visa_stauts, " 
			+//AA码
			"v.AA_code," 
			+//制表人
			"v.make_table " 
			+
			"FROM visa_products p left join sys_dict dict  on  p.collarZoning = dict.value and dict.type ='from_area'  , visa v, traveler t INNER JOIN visa_order o " +
			"ON o.id = t.orderId AND o.id = ?1 WHERE t.delFlag<>1 AND o.visa_product_id = p.id AND t.id = v.traveler_id",nativeQuery=true)
	public List<Object[]> findOrderTableByOrderId(Long orderId);
	
	@Modifying
    @Query("update VisaOrder set lockStatus=?2 where id=?1")
    public int updateOrderLockStatus(Long orderId, int status);
    
    @Modifying
    @Query("update VisaOrder set payStatus=?2,activationDate=?3 where id=?1")
    public int invokeOrder(Long id, Integer payStatus, Date activationDate );
	
	@Query("FROM Dict WHERE delFlag ='0' AND type ='new_visa_type'")
	public List<Dict> queryVisaType();

	@Query("from VisaOrder vo where vo.agentinfoId=?1 and (vo.payStatus != 99 and vo.delFlag != 1)")
	List<VisaOrder> findByAgentId(Long agentId);
	
	@Query("FROM VisaOrder WHERE delFlag = '0' AND visaProductId = ?1 AND id in " +
			"(SELECT t.orderId FROM Traveler t where mainOrderTravelerId = ?2 and orderType = 6)")
	public List<VisaOrder> findByProductIdAndMainOrderTravelerId(Long visaProductId, Long mainOrderTravelerId);
	@Query("")
	public VisaOrder getVisaInfoByOrderNo(String orderNo);
	
	@Modifying
    @Query("update VisaOrder set jkSerialnum=?2 where id=?1")
    public int updateOrderJkSerialnum(Long orderId, String jkSerialnum);
	
	/**
	 * 删除订单 
	 * @param orderId
	 * @return
	 */
	@Modifying
    @Query("update VisaOrder set delFlag=1 where id=?1")
    public int updateOrder4Delete(Long orderId);
	
	@Query("FROM VisaOrder WHERE id =?1")
	public VisaOrder findByOrderId(Long orderId);

	@Query("FROM OrderContacts WHERE orderId=?1 and orderType=6")
	public List<OrderContacts> findContactsNameByOrderId(Long orderId);

}

interface VisaOrderDaoCustom extends BaseDao<VisaOrder> {

	/**
	 * 批量更新签证状态
	 */
	boolean batchUpdateVisaStatus(String visaIds, String status);
	/**
	 * 批量更新护照状态
	 */
	boolean batchUpdatePassportStatus(String passportStatus, String travellerIds);
	
	/*
	 * 根据产品id获取获取订单id和订单No
	 * @param productId 产品id
	 * @return 订单id和No的map列表
	 * @author xianglei.dong 
	 */
	public List<Map<String, Object>> findOrderIdAndNoByProductId(String productId);
}

@Repository
class VisaOrderDaoImpl extends BaseDaoImpl<VisaOrder> implements VisaOrderDaoCustom {

	/**
	 * 批量更新签证状态
	 */
	public boolean batchUpdateVisaStatus(String visaIds, String status) {
		String batchUpdateVisaStatusSql = "update visa set visa_stauts="+status+" where id in ("+visaIds+")";
		int count = updateBySql(batchUpdateVisaStatusSql);
		return count==1?true:false;
	}
	
	/**
	 * 批量更新护照状态
	 */
	public boolean batchUpdatePassportStatus(String passportStatus, String travellerIds) {
		travellerIds = travellerIds.substring(0, travellerIds.length()-1);
		String Sql = "update traveler set passportStatus="+Integer.parseInt(passportStatus)+" where id in ("+travellerIds+")";
		int count = updateBySql(Sql);
		return count==1?true:false;
	}
	
	/*
	 * 根据产品id获取获取订单id和订单No
	 * @param productId 产品id
	 * @return 订单id和No的map列表
	 * @author xianglei.dong 
	 */
	public List<Map<String, Object>> findOrderIdAndNoByProductId(String productId) {
		String sql = "select vo.id as orderId, vo.order_no as orderNo from visa_order vo where vo.visa_order_status <> 111 " +
				"and vo.visa_order_status <> 100 and vo.payStatus <> 99 and vo.del_flag='0' and vo.visa_product_id="+productId;
		List<Map<String, Object>> list = findBySql(sql, Map.class);
		return list;
	}
}

