package com.trekiz.admin.modules.money.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.mtourfinance.pojo.MoneyAmountVO;
/**
 * 
 * @interfaceName: MoneyAmountDao
 * @Description: TODO(自定义接口)
 * @author kai.xiao
 * @date 2014年12月1日 下午9:48:09
 *
 */
public interface MoneyAmountDao extends MoneyAmountDaoCustom,
		CrudRepository<MoneyAmount, Long> {
	@Query("from MoneyAmount where id = ?1")
	public List<MoneyAmount> findMoneyAmount(Long id);

	@Query("from MoneyAmount where serialNum = ?1 order by currencyId")
	public List<MoneyAmount> findAmountBySerialNum(String serialNum);
	
	@Query("from MoneyAmount where serialNum = ?1 and amount >0 order by currencyId")
	public List<MoneyAmount> findAmountBySerialNumAndAmount(String serialNum);
	
	@Query("from MoneyAmount where serialNum = ?1 and delFlag = '0' order by currencyId")
	public List<MoneyAmount> findAmountBySerialNumNormal(String serialNum);
	
	@Query("from MoneyAmount where serialNum = ?1 and currencyId = ?2 and (delFlag <> '1' or delFlag is NULL)")
	public List<MoneyAmount> findAmountBySerialNumAndCurrencyId(String serialNum,Integer currencyId);

	@Query("from MoneyAmount where uid = ?1")
	public List<MoneyAmount> findAmountByOrderId(Long uid);

	@Query("from MoneyAmount where moneyType = ?1")
	public List<MoneyAmount> findAmountByMoneyType(Integer moneyType);

	@Query("from MoneyAmount where orderType = ?1")
	public List<MoneyAmount> findAmountByOrderType(Integer orderType);

	@Query("from MoneyAmount where uid = ?1 and moneyType = ?2")
	public List<MoneyAmount> findAmount(Long uid, Integer moneyType);

	@Query("from MoneyAmount where uid = ?1 and orderType = ?2")
	public List<MoneyAmount> findAmountByUidAndOrderType(Long uid,
			Integer orderType);

	@Query("from MoneyAmount where uid = ?1 and moneyType = ?2 and orderType = ?3")
	public List<MoneyAmount> findAmount(Long uid, Integer moneyType,
			Integer orderType);
	
	@Query("from MoneyAmount where uid = ?1 and moneyType = ?2 and orderType = ?3 and businessType = ?4")
	public List<MoneyAmount> findAmount(Long uid, Integer moneyType,
			Integer orderType, Integer businessType);

	@Modifying
	@Query("update MoneyAmount set amount = ?2  where id = ?1")
	public void updateOrderForAmount(Long id, BigDecimal amount);

	@Modifying
	@Query("update MoneyAmount set exchangerate = ?1  where reviewId = ?2")
	public void updateRate(BigDecimal rate, Long reviewId);
		
	@Modifying
	@Query("update MoneyAmount set amount = ?3  where serialNum = ?1 and currencyId=?2")
	public void updateOrderForAmount(String serialNum,Integer currencyId, BigDecimal amount);
	/**
	 * 更新改签后的游客结算价 
	 * add by songyang 二〇一五年十二月十日 11:34:11
	 * @param serialNum
	 */
	@Modifying
	@Query("update MoneyAmount set amount = 0 where serialNum = ?1")
	public void updateOrderPayPriceAmount(String serialNum);
	
	@Modifying
	@Query("update MoneyAmount set delFlag='1'  where serialNum = ?1 and currencyId=?2")
	public void deleteOrderForAmount(String serialNum,Integer currencyId);
	
	@Modifying
	@Query("update MoneyAmount set delFlag='1' where serialNum = ?1")
	public void delMoneyAmountBySerialNum2Delflag(String serialNum);
	
	@Modifying
	@Query("delete from MoneyAmount where serialNum = ?1")
	public void delMoneyAmountBySerialNum(String serialNum);
	
	@Modifying
	@Query("update MoneyAmount set moneyType = ?2  where serialNum = ?1")
	public void updateMoneyType(String serialNum, Integer moneyType);
	
	@Query("from MoneyAmount where serialNum = ?1")
	public List<MoneyAmount> findAmountListBySerialNum(String serialNum);
	
	@Query("from MoneyAmount where serialNum = ?1")
	public MoneyAmount findOneAmountBySerialNum(String serialNum);

	@Query("from MoneyAmount where payedAccountedUUID = ?1 and moneyType = 13")
	public MoneyAmount findOneAmountByUUID(String uuid);
	
	@Query("from MoneyAmount where payedAccountedUUID = ?1")
	public List<MoneyAmount> findAmountListByUUID(String uuid);

	@Query("from MoneyAmount where reviewId = ?1")
	public List<MoneyAmount> findAmountsByReviewId(Long reviewId);
	
	@Query("from MoneyAmount where reviewUuid = ?1")
	public List<MoneyAmount> findAmountsByReviewUuId(String reviewId);
	
	@Modifying
	@Query("update MoneyAmount set amount = ?2 , moneyType=102 where serialNum = ?1 and currencyId = ?3")
	public void updateMoneyAmount(String serialNum, BigDecimal amount,Integer currencyId);
	@Query("from MoneyAmount where serialNum = ?1  order by currencyId")
	public List<MoneyAmount> getAmountByUid(String serialNum);
	@Modifying
	@Query("update MoneyAmount set amount = ?1  where serialNum = ?2 ")
	public void updateMoneyAmountByUid(BigDecimal amount, String serialNum);

	@Modifying
	@Query("update MoneyAmount set amount = 0  where id = ?1")
	public void updateAmountById(Long id);
	@Modifying
	@Query("update MoneyAmount set amount = ?1  where id = ?2")
	public void updateAmountById(BigDecimal amount, Long id);
	
	@Modifying
	@Query("update MoneyAmount set amount = ?1,exchangerate = ?2  where id = ?3")
	public void updateAmountById(BigDecimal amount, BigDecimal exchangerate, Long id);
	
	@Query("select id from MoneyAmount where currencyId = ?1") 
	public List<Integer> findIdsByCurrencyId(Integer currencyId);

}

/**
 * 
 * @interfaceName: MoneyAmountDaoCustom
 * @Description: TODO(自定义接口)
 * @author kai.xiao
 * @date 2014年12月1日 下午9:48:09
 *
 */
interface MoneyAmountDaoCustom extends BaseDao<MoneyAmount> {
	public List<MoneyAmount> mergeAmountBySerialNum(String serialNum);
	public MoneyAmountVO amountxexchangerate(String serialNum);
	public void updateAmountByIdImpl(Long id, BigDecimal amount);
	public void updateAmountByRecordId(Integer id, Integer orderType, Integer moneyType, BigDecimal rate);
	public List<Map<String,Object>> getCurrencyAndMoneySum(String serialNum);
}

/**
 * 
 * @ClassName: MoneyAmountDaoImpl
 * @Description: TODO(自定义接口实现类)
 * @author kai.xiao
 * @date 2014年12月1日 下午9:50:25
 *
 */
@Repository
class MoneyAmountDaoImpl extends BaseDaoImpl<MoneyAmount> implements
		MoneyAmountDaoCustom {

	public List<MoneyAmount> mergeAmountBySerialNum(String serialNum){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("moneyAmount.currencyId AS currencyId, ");
		sql.append("moneyAmount.exchangerate AS exchangerate, ");
		sql.append("sum( ");
		sql.append("amount * moneyAmount.exchangerate ");
		sql.append(") AS amount, ");
		sql.append("c.convert_lowest, ");
		sql.append("c.currency_name AS currencyName ");
		sql.append("FROM ");
		sql.append("money_amount moneyAmount ");
		sql.append("LEFT JOIN currency c ON c.currency_id = moneyAmount.currencyId ");
		sql.append("WHERE ");
		sql.append("moneyAmount.serialNum = ? ");
		sql.append("GROUP BY ");
		sql.append("moneyAmount.currencyId ");
		List<MoneyAmount> amountList = new ArrayList<MoneyAmount>();
		List<Map<String,Object>> templist = new ArrayList<Map<String,Object>>();
		templist = findBySql(sql.toString(),Map.class,serialNum);
		if(!templist.isEmpty()){
			MoneyAmount moneyAmount = new MoneyAmount();
			
			//将币种信息做累加
			BigDecimal allMoney = new BigDecimal(0);
			for(Map<String,Object> map : templist){
				Integer currencyId = (Integer)map.get("currencyId");
				BigDecimal exchangerate = (BigDecimal)map.get("exchangerate");
				BigDecimal amount = (BigDecimal)map.get("amount");
				String currencyName = map.get("currencyName").toString();
				if(exchangerate == null) {
					exchangerate = (BigDecimal)map.get("convert_lowest");
					
					//添加默认汇率，默认汇率为1
					if(exchangerate == null) {
						exchangerate = new BigDecimal(1);
					}
				}
				
				if("人民币".equals(currencyName)) {
					moneyAmount.setCurrencyId(currencyId);
				} else {
					amount = amount;
				}
				if(amount == null ){
					allMoney = allMoney.add(new BigDecimal(0));
				}else{
					allMoney = allMoney.add(amount);
				}
			}
			moneyAmount.setAmount(allMoney);
			
			amountList.add(moneyAmount);
		}
		return amountList;
	}

	@Override
	public MoneyAmountVO amountxexchangerate(String serialNum) {
		String sql = "select sum(amount*IFNULL(ma.exchangerate,IFNULL(c.convert_lowest,1))) total from money_amount ma,currency c " +
							"where ma.currencyId=c.currency_id AND ma.serialNum=?";
		List<Map<String,Object>> list = findBySql(sql, Map.class,serialNum);
		BigDecimal total  = (BigDecimal) list.get(0).get("total");
		MoneyAmountVO vo = new MoneyAmountVO();
		if(total != null){
			vo.setAmount(Double.valueOf(total.toString()));
		}
		return vo;
	}
	
	/**
	 * 更新订单的金额（使用sql）
	 * @param id money_amount的id
	 * @param amount 金额
	 */
	public void updateAmountByIdImpl(Long id, BigDecimal amount) {
		String sqlString = "update money_amount set amount = ? where id = ?";
		super.updateBySql(sqlString, amount, id);
		flush();
	}

	@Override
	public void updateAmountByRecordId(Integer id, Integer orderType, Integer moneyType, BigDecimal rate) {
		// TODO Auto-generated method stub
//		StringBuilder sb = new StringBuilder("update money_amount set exchangerate = ? where serialNum IN (");
//		sb.append("select money_serial_num  from refund r where r.record_id = ? and r.orderType=? and r.moneyType=?").append("union");
//		sb.append("select merge_money_serial_num from refund r where r.mergePayFlag=1 and r.record_id = ? and r.orderType=? and r.moneyType=?"); 
//	
//		super.updateBySql(sb.toString(), rate, id, orderType, moneyType, id, orderType, moneyType);
//		flush();	
	}

	/**
	 * 根据序列号查询MoneyAmount表,按照币种进行分组。获取币种id,币种符号和同币种下金额总和。
	 * @param serialNum
	 * @return
	 * @author yudong.xu 2016.9.5
     */
	public List<Map<String,Object>> getCurrencyAndMoneySum(String serialNum){
		String sql = "SELECT ma.currencyId,c.currency_mark AS currencyMark,SUM(ma.amount) AS moneySum FROM " +
			"money_amount ma,currency c WHERE ma.currencyId=c.currency_id AND ma.amount != 0 AND ma.serialNum=? " +
			"GROUP BY ma.currencyId";
		return super.findBySql(sql,Map.class,serialNum);
	}

}
