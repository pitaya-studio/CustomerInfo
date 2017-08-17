/**
 *
 */
package com.trekiz.admin.modules.traveler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.traveler.entity.Traveler;


 /**
 *  文件名: TravelerDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:33
 *  @version 1.0
 */
public interface TravelerDao extends TravelerDaoCustom, CrudRepository<Traveler, Long> {
    
    String listOutBean = null;

	@Query("from Traveler where orderId = ?1 order by id asc")
    public List<Traveler> findTravelerByOrderId(Long orderId);
    
	@Query("from Traveler where orderId = ?1 and order_type = ?2 and delFlag='" + Traveler.DEL_FLAG_NORMAL + "' order by id asc")
    public List<Traveler> findTravelerByOrderIdAndOrderType(Long orderId, int orderType);
	
	@Query("from Traveler where orderId = ?1 and order_type = ?2 and delFlag='" + Traveler.DEL_FLAG_NORMAL + "' and isAirticketFlag = 1 order by id asc")
	public List<Traveler> findTravelerByOrderIdAndOrderTypeExt(Long orderId, int orderType);
	
	@Query("from Traveler where orderId = ?1 and order_type = ?2 and delFlag='" + Traveler.DEL_FLAG_NORMAL + "' order by id asc limit 1")
    public Traveler findFirstTravelerByOrderIdAndOrderType(Long orderId, int orderType);
	
    @Query("from Traveler where orderId = ?1 and orderType = ?2 and delFlag in ?3 order by id asc")
    public List<Traveler> findTravelerByOrderIdAndOrderType(Long orderId, int orderType, List<Integer> delFlag);
    
    /**
     * 根据游客主订单ID查询签证游客，游客状态为正常或退团审核或转团审核
     * @param mainOrderId
     * @return
     */
	@Query("SELECT distinct t FROM Traveler t where t.mainOrderId = ?1 and orderType = 6 and t.delFlag in (0,2,4) ORDER BY id asc")
	public List<Traveler> findTravelersByMainOrderId(Long mainOrderId);
	
	/**
	 * 根据主订单游客id查询签证对应游客
	 * @param mainOrderId
	 * @return
	 */
	@Query("FROM Traveler where mainOrderTravelerId = ?1 and orderType = 6 and delFlag in (0,2,4)")
	public List<Traveler> findTravelersByMainTravelerId(Long mainOrderId);
    
    /**
     * @param orderId
     * @param orderType
     * @param isAirticket -1表示查询所有
     * @return
     */
    @Query("from Traveler where orderId = ?1 and order_type = ?2 and (?3=-1 or isAirticketFlag=?3)")
    public List<Traveler> getTicketTravelersByOrderIdAndOrderType(Long orderId, int orderType,int isAirticket);
    
    @Query(value="select '' as rowNum,trl.name,trl.nameSpell,case when trl.sex=1 then 'M' else 'F' end,date_format(trl.birthDay,'%Y/%c/%d')," +
    		"country.countryName_cn,trl.idCard,trl.passportCode,date_format(trl.passportValidity,'%Y/%c/%d') as validityDate," +
    		"trl.passportType,trl.telephone as telephone,p.orderCompanyName as companyName, " +
    		"(CASE agent.id WHEN -1 THEN u.`name` ELSE IF(agent.is_quauq_agent = 1,p.salerName,GROUP_CONCAT(user.`name` SEPARATOR ',')) END) saler, trl.remark as remark " +
    		"from traveler trl left join productorder p on p.id = trl.orderId left join sys_country country on trl.nationality = country.id " +
    		"left join agentinfo agent ON p.orderCompany = agent.id LEFT JOIN sys_user user ON FIND_IN_SET(user.id,agent.agentSalerId) LEFT JOIN sys_user u ON p.createBy = u.id " +
    		"where p.payStatus != 111 and p.payStatus != 99 and trl.orderId = ?1 and trl.order_type = ?2 and trl.delFlag in (0,2,4) GROUP BY trl.id order by p.createDate ASC, p.id ASC", nativeQuery = true)
    public List<Object[]> findByOrderId(Long orderId, Integer orderType);
    
    @Query(value="SELECT p.salerName, '' AS rowNum, trl. NAME, CASE WHEN trl.sex = 1 THEN 'M' ELSE 'F' END sex, trl.nameSpell, date_format(trl.birthDay, '%Y/%c/%d') birthday, "+
    			" trl.hometown AS hometown, trl.passportCode, trl.issuePlace1,date_format(trl.issueDate, '%Y/%c/%d') AS validityDate,date_format(trl.passportValidity, '%Y/%c/%d') as passportValidity, trl.remark, trl.telephone AS telephonem,'' as room  "+
    			"FROM productorder p LEFT JOIN traveler trl ON p.id = trl.orderId LEFT JOIN sys_country country ON trl.nationality = country.id LEFT JOIN sys_user u ON p.createBy = u.id "+
    			"WHERE p.payStatus != 111 AND p.payStatus != 99 AND p.id = ?1 AND trl.order_type = ?2 AND trl.delFlag IN (0, 2, 4) GROUP BY trl.id ORDER BY p.createDate ASC, p.id ASC", nativeQuery = true)
    public List<Object[]> findTravelAndOrderIdByOrderId(Long orderId, Integer orderType);
    
    @Query(value="SELECT p.id FROM  productorder p where p.payStatus != 111 and p.payStatus != 99 and p.productGroupId = ?1 order by p.createDate ASC, p.id ASC", nativeQuery = true)
    public List<Integer> findorderIdByGroupId(Long groupId);
    
    /**
     * 0507--骡子假期
     * @param orderId
     * @param orderType
     * @return
     * @author zhanyu.gu
     */
    @Query(value="SELECT p.id,p.orderPersonNum, '' AS rowNum1, p.salerName, CONCAT(p.orderCompanyName,' ',p.orderPersonName) agent, trl. NAME, CASE WHEN trl.id IS NULL THEN ' ' ELSE ( CASE WHEN trl.sex = 1 THEN 'M' ELSE 'F' END )END sex, '' AS rowNum2, trl.telephone, '' AS rowNum3, '' AS rowNum4," +
    		" payMoney.moneyamount1, acountMoney.moneyamount2, '' AS rowNum5, p.specialDemand, date_format(p.orderTime, '%Y/%c/%d'), '' AS rowNum6, '' AS rowNum7 "+
    		"FROM productorder p LEFT JOIN traveler trl ON p.id = trl.orderId and trl.order_type = ?2 LEFT JOIN activitygroup ag ON p.productGroupId = ag.id "+
    		/*"LEFT JOIN agentinfo agi on  p.orderCompany = agi.id  "+*/
    		"LEFT JOIN ( SELECT mao.serialNum, GROUP_CONCAT( CONCAT( c.currency_mark, ' ', mao.amount ) ORDER BY mao.currencyId SEPARATOR '+' ) moneyamount1 "+
    		"FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id WHERE c.create_company_id = 384 and mao.amount !=0.00 GROUP BY mao.serialNum ) payMoney ON p.payed_money = payMoney.serialNum and p.payStatus != 5 "+
    		
    		"LEFT JOIN ( SELECT mao.serialNum, GROUP_CONCAT( CONCAT( c.currency_mark, ' ', mao.amount ) ORDER BY mao.currencyId SEPARATOR '+' ) moneyamount2 "+
    		"FROM money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id WHERE c.create_company_id = 384 and mao.amount !=0.00 GROUP BY mao.serialNum ) acountMoney ON p.accounted_money = acountMoney.serialNum "+
    		"WHERE p.payStatus != 111 AND p.payStatus != 99 AND p.productGroupId = ?1 and p.id =?3 AND (trl.delFlag IN (0, 2, 4) or trl.delFlag is NULL) ORDER BY p.createDate ASC, p.id ASC", nativeQuery = true)
    public List<Object[]> findActivityGroupByOrderId(Long groupId ,Integer orderType ,Long orderId);
    /**
     * 
     * @param orderIdR
     * @return
     */
    @Query(value = "select ag.groupCode , CONCAT( c.currency_mark, ' ', ag.settlementAdultPrice) tonghangjia, ta.group_lead " +
    		" from productorder p LEFT JOIN activitygroup ag on p.productGroupId = ag.id left join currency c on c.currency_id = ag.currency_type left join travelactivity ta on ta.id = p.productId "+
    		" where p.productGroupId =?1 order by p.createDate ASC, p.id ASC",nativeQuery = true)
    public List<Object[]> findActivityGroupByTopOrderId(Long orderId);
    
    /**
     * 	2016年4月12日  update by pengfei.shang 需求0228
     * @param orderId
     * @param orderType
     * groupNum：组团社序号 ; groupCode:团队编号(团号); groupOpenDate：年份(出团日期); groupLead：领队
     * groupLeadCode：领队证号;number:编号; passportPlace：发证机关; issuePlace：发证日期
     * @return
     */
    @Query(value="select '' as groupNum,g.groupCode,g.groupOpenDate,t.group_lead,'' as groupLeadCode,'' as number,'' as rowNum,trl.name,trl.nameSpell,case when trl.sex=1 then 'M' else 'F' end,date_format(trl.birthDay,'%Y/%c/%d')," +
    		"country.countryName_cn,trl.idCard,trl.passportCode,trl.passportPlace,trl.issuePlace,date_format(trl.passportValidity,'%Y/%c/%d') as validityDate," +
    		"trl.passportType,trl.telephone as telephone,p.orderCompanyName as companyName, " +
    		"(CASE agent.id WHEN -1 THEN user.`name` ELSE IF(agent.is_quauq_agent = 1,p.salerName,GROUP_CONCAT(u.`name` SEPARATOR ',')) END) saler, trl.remark as remark " +
    		"from traveler trl left join productorder p on p.id = trl.orderId left join sys_country country on trl.nationality = country.id " +
    		"LEFT JOIN agentinfo agent ON p.orderCompany = agent.id left join sys_user u on FIND_IN_SET(u.id,agent.agentSalerId) " +
    		"left join activitygroup g ON g.id = p.productGroupId left join travelactivity t ON p.productId = t.id LEFT JOIN sys_user user ON p.createBy = user.id " +
    		"where p.payStatus != 111 and p.payStatus != 99 and trl.orderId = ?1 and trl.order_type = ?2 and trl.delFlag in (0,2,4) GROUP BY trl.id order by p.createDate ASC, p.id ASC", nativeQuery = true)
    public List<Object[]> findRdhytByOrderId(Long orderId, Integer orderType);
    
    @Modifying
    @Query("delete Traveler where orderId = ?1")
    public void deleteTravelerByOrderId(Long orderId);
    
    @Modifying
    @Query("update Traveler set payPriceSerialNum = ?1 where id = ?2")
    public void updateSerialNumByTravelerId(String payPriceSerialNum, Long travelerId);
    
    
    @Modifying
    @Query("update Traveler set costPriceSerialNum = ?1 where id = ?2")
    public void updateCostSerialNumByTravelerId(String costPriceSerialNum, Long travelerId);
   
    @Modifying
    @Query("update Traveler set rebatesMoneySerialNum = ?1 where id = ?2")
    public void updateRebatesMoneySerialNumByTravelerId(String rebatesMoneySerialNum, Long travelerId);
    
    /**
     * 2015-04-07 wxw added 
     * 为了与老数据兼容更新借款uuid
     * @param costPriceSerialNum
     * @param travelerId
     */
    @Modifying
    @Query("update Traveler set jkSerialNum = ?1 where id = ?2")
    public void updateJkSerialNumByTravelerId(String jkSerialNum, Long travelerId);
 
    
    
    /**
     * add by ruyi.chen
     * add date 2014-11-19
     * @param delFlag
     * @param travelerId
     */
    @Modifying
    @Query("update Traveler set delFlag = ?1 where id = ?2")
    public void updateTravelerDelFlag(Integer delFlag, Long travelerId);
    
    
    @Modifying
    @Query("update Traveler set orderId = ?1,order_type = 2 where orderId = ?2 and order_type = 0")
    public void updateTravelerByApplyOrderId(Long orderId, Long applyOrderId);
    
    /**
     * 修改游客 原始游客结算价
     * xinwei.wang added
     * @param originalPayPriceSerialNum
     * @param travelerId
     */
    @Modifying
    @Query("update Traveler set originalPayPriceSerialNum = ?1 where id = ?2")
    public void updateOriginalPayPriceSerialNumByTravelerId(String originalPayPriceSerialNum, Long travelerId);

    @Query("from Traveler where id = ?1")
	public Traveler findById(Long id);
    
    /**
     * 批量查询游客
     * @author jiachen
     * @DateTime 2015年3月12日 下午2:22:54
     * @return List<Traveler>
     */
    @Query("from Traveler where id in ?1")
    public List<Traveler> findByIds(List<Long> ids);
    @Query("")
	public Traveler getTravleById(Long id);
    @Modifying
    @Query("update Traveler set payedMoneySerialNum = ?1  where id = ?2")
	public void updateUUid(String payedMoneySerialNum,Long id);
    
    /**
     * 修改扣减金额流水号
     * @param subtractMoneySerialNum
     * @param travelerId
     */
    @Modifying
    @Query("update Traveler set subtractMoneySerialNum = ?1 where id = ?2")
    public void updateTravelerSubtractMoneySerialNum(String subtractMoneySerialNum, Long travelerId);
    
    /**
     * 查询批发商下的游客类型
     * @param companyId
     * @return
     */
    @Query("from Traveler where wholesaler_id = ?1 and delFlag='0' ")
    public List<Traveler> getListByCompanyId(Long companyId);


}


 /**
 *  文件名: TravelerDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:25
 *  @version 1.0
 */
interface TravelerDaoCustom extends BaseDao<Traveler> {

	public Traveler findTravelerById(Long id);
	
}

 /**
 *  文件名: TravelerDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:15
 *  @version 1.0
 */
@Repository
class TravelerDaoImpl extends BaseDaoImpl<Traveler> implements TravelerDaoCustom {

	@Override
	public Traveler findTravelerById(Long id) {
		String sql = "select * from traveler where id = ?";
		List<Object> list = findBySql(sql ,Traveler.class, id );
		Traveler traveler = new Traveler();
		if(list.size() > 0){
			traveler = (Traveler) findBySql(sql ,Traveler.class, id ).get(0);
		}
		return traveler;
	}
	
}
