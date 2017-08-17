package com.trekiz.admin.modules.money.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.hotel.dao.HotelMoneyAmountDao;
import com.trekiz.admin.modules.hotel.dao.HotelOrderDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.island.dao.IslandMoneyAmountDao;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.pay.entity.PayGroup;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;

import net.sf.json.JSONObject;

/**
 * 
 * @ClassName: MoneyAmountService
 * @Description: TODO(订单、游客金额接口实现类)
 * @author kai.xiao
 * @date 2014年11月13日 下午8:52:21
 * 
 */
@Service
@Transactional(readOnly = true)
public class MoneyAmountService extends BaseService {

	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private ProductOrderCommonDao productOrderDao;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private VisaDao visaDao;
	@Autowired
	private AirticketPreOrderDao airticketPreOrderDao;
	@Autowired
	private RefundDao refundDao;
	@Autowired
	private OrderpayDao orderpayDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private IslandOrderDao islandOrderDao;
	@Autowired
	private HotelOrderDao hotelOrderDao;
	
	@Autowired
	private HotelMoneyAmountDao hotelMoneyAmountDao;
	@Autowired
	private IslandMoneyAmountDao islandMoneyAmountDao;	
	

	/**
	 * 
	 * @Title: generateUUID
	 * @Description: TODO(生成UUID)
	 * @return String 返回类型
	 * @throws
	 */
	private String generateUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @Description 金额删除
	 * @author yakun.bai
	 * @Date 2015-11-16
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void deleteById(Long id) {
		if (id != null) {
			moneyAmountDao.delete(id);
		}
	}
	
	/**
	 * 多币种付款公用接口,普通保存多币种的调用 saveOrUpdateMoneyAmounts
	 * 
	 * @Title: saveMoneyAmount
	 * @Description: TODO(保存流水实体)
	 * @param @param moneyAmount
	 * @return boolean 返回类型
	 * @throws
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public boolean saveMoneyAmounts(List<MoneyAmount> moneyAmounts) {
		if (moneyAmounts != null && moneyAmounts.size() > 0) {

			Integer moneyType = moneyAmounts.get(0).getMoneyType();
			String serialNum = null;

			if (moneyType != null
					&& (moneyType == 1 || moneyType == 2 || moneyType == 3 || moneyType == 11)) {
				// 订单支付
				serialNum = getOrderPayedMoneyUid(moneyAmounts.get(0));
			}

			for (int i = 0; i < moneyAmounts.size(); i++) {
				MoneyAmount moneyAmount = moneyAmounts.get(i);
				if (moneyAmount != null && moneyAmount.getAmount() != null) {
					if (StringUtils.isBlank(moneyAmount.getSerialNum())) {
						moneyAmount.setSerialNum(UUID.randomUUID().toString());
					}
					moneyAmount.setCreatedBy(UserUtils.getUser().getId());
					saveOrUpdateMoneyAmount(moneyAmount);

					moneyType = moneyAmount.getMoneyType();

					if (moneyType != null && ( moneyType == 1 || moneyType == 2 || moneyType == 3) ) {
						// 1、全款 2、尾款 3、订金 增加订单的已收
						saveOrUpdateMoneyAmount(moneyAmount, serialNum, "add", 5);

					} else if (moneyType != null  && moneyType == 11) {
						// 11、退款 减少订单的已收 业务变更，退款不再更新已收和达账 注释掉这些代码 by chy 2015年5月28日16:19:54
//						saveOrUpdateMoneyAmount(moneyAmount, serialNum,
//								"subtract", 5);
//						// 更新订单的达帐
//						String[] money = getAccountedMoneyUid(
//								moneyAmount.getUid(),
//								moneyAmount.getOrderType());
//						saveOrUpdateMoneyAmount(moneyAmount, money[0],
//								"subtract", 5);
						// updateOrderAccountedMoney(moneyAmount.getUid(),
						// moneyAmount.getOrderType(), moneyType);
					} else if (moneyType != null && moneyType == 7) {
						// //7、收押金，增加押金的已收
						// Visa visa = (Visa)
						// visaDao.getSession().get(Visa.class,
						// moneyAmount.getUid());
						// serialNum = visa.getPayedDeposit();
						// if(StringUtils.isEmpty(serialNum)){
						// serialNum = generateUUID();
						// visa.setPayedDeposit(serialNum);
						// visaDao.getSession().update(visa);
						// }
						//
						// saveOrUpdateMoneyAmount(moneyAmount, serialNum,
						// "add", 16);

					} else if (moneyType != null  && moneyType == 15) {
						// 15、退押金，增加押金的财务已退
						Visa visa = (Visa) visaDao.getSession().get(Visa.class,
								moneyAmount.getUid());
						serialNum = visa.getReturnedDeposit();
						if (StringUtils.isEmpty(serialNum)) {
							serialNum = generateUUID();
							visa.setReturnedDeposit(serialNum);
							visaDao.getSession().update(visa);
						}

						saveOrUpdateMoneyAmount(moneyAmount, serialNum, "add", 17);
					} else if (moneyType != null && moneyType == 9) {
						// TODO转款，修改新、老订单的应收、已收、达帐
					}

				}
			}
		}
		// updateOrderAccountedMoney(moneyAmounts.get(0).getUid(),
		// moneyAmounts.get(0).getOrderType());
		return true;
	}
	/**
	 * 返佣专用
	 * @param moneyAmounts
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public boolean saveNewMoneyAmounts(List<MoneyAmount> moneyAmounts) {
		if (moneyAmounts != null && moneyAmounts.size() > 0) {

			for (int i = 0; i < moneyAmounts.size(); i++) {
				MoneyAmount moneyAmount = moneyAmounts.get(i);
				if (moneyAmount != null && moneyAmount.getAmount() != null) {
					if (StringUtils.isBlank(moneyAmount.getSerialNum())) {
						moneyAmount.setSerialNum(UUID.randomUUID().toString());
					}
					moneyAmount.setCreatedBy(UserUtils.getUser().getId());
					moneyAmountDao.save(moneyAmount);
				}
			}
		}
		return true;
	}

	/**
	 * 更新订单达帐金额(按单次付款的金额进行确认)，订单财务确认达帐后调用此接口
	 * 
	 * @param orderId
	 * @param orderType
	 * @param moneyType
	 * @param moneySerialNum
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void updateOrderAccountedMoney(Long orderId, int orderType,
			int moneyType, String moneySerialNum) {
		orderpayDao.updateIsAsAccount(orderId, moneyType, orderType,
				moneySerialNum);

		String[] money = getAccountedMoneyUid(orderId, orderType);
		String accStr = money[0];
		List<MoneyAmount> payedMoney = moneyAmountDao
				.findAmountBySerialNum(moneySerialNum);

		if (payedMoney != null && payedMoney.size() > 0) {
			for (int i = 0; i < payedMoney.size(); i++) {

				MoneyAmount payed = payedMoney.get(i);
				List<MoneyAmount> result = moneyAmountDao
						.findAmountBySerialNumAndCurrencyId(accStr,
								payed.getCurrencyId());
				if (result != null && result.size() > 0) {
					result.get(0).setAmount(
							result.get(0).getAmount().add(payed.getAmount()));
				} else {
					MoneyAmount accountedMoney = new MoneyAmount();
					accountedMoney.setSerialNum(accStr);
					accountedMoney.setMoneyType(4);
					accountedMoney.setAmount(payed.getAmount());
					accountedMoney.setBusindessType(payed.getBusindessType());
					accountedMoney.setCreateTime(new Date());
					accountedMoney.setCreatedBy(UserUtils.getUser().getId());
					accountedMoney.setCurrencyId(payed.getCurrencyId());
					accountedMoney.setOrderType(payed.getOrderType());
					accountedMoney.setUid(payed.getUid());
					saveOrUpdateMoneyAmount(accountedMoney);
				}
			}
		}
	}

	/**
	 * 更新押金达帐金额，财务确认后调用此接口
	 * 
	 * @param visaId
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void updateAccountedDeposit(Long visaId, String moneySerialNum) {
		orderpayDao.updateIsAsAccount(moneySerialNum);

		Visa visa = (Visa) visaDao.getSession().get(Visa.class, visaId);
		String accountedDeposit = visa.getAccountedDeposit();
		// String payedDeposit = visa.getPayedDeposit();

		if (StringUtils.isEmpty(accountedDeposit)) {
			accountedDeposit = generateUUID();
			visa.setAccountedDeposit(accountedDeposit);
			visa.setIsAccounted(1);
			visaDao.getSession().update(visa);
		}

		List<MoneyAmount> payedMoney = moneyAmountDao
				.findAmountBySerialNum(moneySerialNum);

		if (payedMoney != null && payedMoney.size() > 0) {
			for (int i = 0; i < payedMoney.size(); i++) {

				MoneyAmount payed = payedMoney.get(i);
				List<MoneyAmount> result = moneyAmountDao
						.findAmountBySerialNumAndCurrencyId(accountedDeposit,
								payed.getCurrencyId());
				if (result != null && result.size() > 0) {
					result.get(0).setAmount(
							result.get(0).getAmount().add(payed.getAmount()));
				} else {
					MoneyAmount payedMny = payedMoney.get(i);

					MoneyAmount accountedMoney = new MoneyAmount();
					accountedMoney.setSerialNum(accountedDeposit);
					accountedMoney.setMoneyType(18);
					accountedMoney.setAmount(payedMny.getAmount());
					accountedMoney
							.setBusindessType(payedMny.getBusindessType());
					accountedMoney.setCreateTime(new Date());
					accountedMoney.setCreatedBy(UserUtils.getUser().getId());
					accountedMoney.setCurrencyId(payedMny.getCurrencyId());
					accountedMoney.setOrderType(payedMny.getOrderType());
					accountedMoney.setUid(payedMny.getUid());
					saveOrUpdateMoneyAmount(accountedMoney);
				}

			}
		}
	}

	/**
	 * 公用接口，根据流水号多币种信息
	 * 
	 * @param serialNum
	 *            流水号
	 * @author xiaoyang.tao
	 */
	public List<Object[]> getMoneyAmonut(String serialNum) {
		String sql = "SELECT m.currencyId,c.currency_name,c.currency_mark,sum(m.amount),m.exchangerate,m.moneyType from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		return moneyAmountDao.findBySql(sql);
	}
	
	public List<Map<String,Object>> getMoneyBySerialNum(String orderType,String serialNum){
		String tableName=" ";
		if(StringUtils.isNotBlank(orderType)){
			Integer ot = Integer.valueOf(orderType);
			if(Context.ORDER_TYPE_HOTEL == ot){
				tableName="hotel_money_amount";
			}else if(Context.ORDER_TYPE_ISLAND == ot){
				tableName="island_money_amount";
			}else{
				tableName="money_amount";
			}
		}else{
			tableName="money_amount";
		}
		String sql = "SELECT m.currencyId,c.currency_mark currencymark,sum(m.amount) amount from " +
				tableName +
				" m, currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		return moneyAmountDao.findBySql(sql,Map.class);
		
	}
	public List<Object[]> getMoneyAmonut(List<String> serialNum) {
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isNotEmpty(serialNum)){
			int size = serialNum.size();
			for(int i=0;i<size;i++){
				if (i != serialNum.size() - 1) {
					sb.append("'" + serialNum.get(i) + "',");
				} else {
					sb.append("'" + serialNum.get(i) + "'");
				}
			}
		}
		
		String sql = "SELECT m.currencyId,c.currency_name,c.currency_mark,sum(m.amount),c.currency_exchangerate from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in("
				+ sb.toString() + ") GROUP BY m.currencyId ORDER BY m.currencyId";
		return moneyAmountDao.findBySql(sql);
	}


	/**
	 * 公用接口，根据流水号多币种信息(海岛游)
	 * 
	 * @param serialNum
	 *            流水号
	 * @author xiaoyang.tao
	 */
	public List<Object[]> getIslandMoneyAmount(String serialNum) {
		String sql = "SELECT m.currencyId,c.currency_name,c.currency_mark,sum(m.amount),c.currency_exchangerate from island_money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		return moneyAmountDao.findBySql(sql);
	}
	/**
	 * 公用接口，根据流水号多币种信息(酒店)
	 * 
	 * @param serialNum
	 *            流水号
	 * @author xiaoyang.tao
	 */
	public List<Object[]> getHotelMoneyAmount(String serialNum) {
		String sql = "SELECT m.currencyId,c.currency_name,c.currency_mark,sum(m.amount),c.currency_exchangerate from hotel_money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		return moneyAmountDao.findBySql(sql);
	}
	/**
	 * 公用接口，根据多个流水号多币种信息(海岛游)
	 * 
	 * @param serialNum
	 *            流水号
	 * @author xiaoyang.tao
	 */
	public List<Object[]> getIslandMoneyAmountBySerialNums(String serialNum) {
		String sql = "SELECT m.currencyId,c.currency_name,c.currency_mark,sum(m.amount),c.currency_exchangerate from island_money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in("
				+ serialNum + ") GROUP BY m.currencyId ORDER BY m.currencyId";
		return moneyAmountDao.findBySql(sql);
	}
	/**
	 * 公用接口，根据多个流水号多币种信息(酒店)
	 * 
	 * @param serialNum
	 *            流水号
	 * @author xiaoyang.tao
	 */
	public List<Object[]> getHotelMoneyAmounttBySerialNums(String serialNum) {
		String sql = "SELECT m.currencyId,c.currency_name,c.currency_mark,sum(m.amount),c.currency_exchangerate from hotel_money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in("
				+ serialNum + ") GROUP BY m.currencyId ORDER BY m.currencyId";
		return moneyAmountDao.findBySql(sql);
	}
	/**
	 * 公用接口，根据流水号多币种信息
	 * 
	 * @param serialNum
	 *            流水号
	 * @param orderType
	 *            订单类型
	 * @author shijun.liu
	 */
	public List<Object[]> getMoneyAmonut(String serialNum, Integer orderType, Integer moneyType) {
		StringBuffer str = new StringBuffer();
		str.append(
				"SELECT m.currencyId,c.currency_name,c.currency_mark,sum(m.amount), ")
				.append(" c.currency_exchangerate from money_amount m,currency c where m.currencyId=c.currency_id ")
				.append(" and m.serialNum = '").append(serialNum)
				.append("' and m.orderType = ").append(orderType);
				if(orderType != Context.ORDER_TYPE_QZ){
					//为解决交易明细--签证产品 到账金额显示，而已付金额不显示的问题，
					//针对签证产品去掉moneyType条件。
					//但是其他产品需要加此条件。
					str.append(" and m.moneyType = ").append(moneyType);
				}
		str.append(" GROUP BY m.currencyId ORDER BY m.currencyId ");
		return moneyAmountDao.findBySql(str.toString());
	}
	
	/**
	 *根据流水号获取多币种 moneyAmount 实体list
	 * 
	 * @param serialNum 流水号
	 * @author jyang
	 */
	public List<MoneyAmount> getMoneyAmonutListIgnoreDelflag(String serialNum) {
		return moneyAmountDao.findAmountBySerialNum(serialNum);
	}
	
	/**
	 * 公用接口，根据流水号取金额，对多币种进行拼接
	 * 
	 * @param serialNum
	 *            流水号UUID
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoney(String serialNum) {
		String sql = "SELECT m.currencyId,c.currency_name,sum(m.amount),c.currency_mark from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = moneyAmountDao.getSession().createSQLQuery(sql).list();

		String money = "";

		if (results != null && results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				// money = money + amount[1] + d.format(new
				// BigDecimal(amount[2].toString())) + amount[3] + " ";
				//新加校验 当金额为0时 不输出此金额 by chy 2016年2月17日09:47:16 bug号12353 start
				BigDecimal bigDecimal = new BigDecimal(amount[2].toString());
				if(BigDecimal.ZERO.doubleValue() == bigDecimal.doubleValue()){
					continue;
				}
				//新加校验 当金额为0时 不输出此金额 by chy 2016年2月17日09:47:16 bug号12353 end
				money = money + amount[3] + d.format(bigDecimal);
				if (i != results.size() - 1) {
					money = money + " + ";
				}
			}
		} else {
			money = "";//¥ 0.00 当根据UUID 查询不到结果时，默认返回空 不在返回¥ 0.00 by chy 2015年12月30日16:51:56 bug号11480
		}
		if(money.lastIndexOf("+") != -1 && money.trim().endsWith("+")){
			money = money.substring(0, money.lastIndexOf("+"));
		}

		return money;
	}
	
	/**
	 * 公用接口，根据流水号取金额，对多币种进行拼接(币种名称拼接)
	 * 
	 * @param serialNum
	 *            流水号UUID
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoneyCurrenName(String serialNum) {
		String sql = "SELECT m.currencyId,c.currency_name,sum(m.amount),c.currency_mark from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = moneyAmountDao.getSession()
				.createSQLQuery(sql).list();

		String money = "";

		if (results != null && results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				// money = money + amount[1] + d.format(new
				// BigDecimal(amount[2].toString())) + amount[3] + " ";
				money = money + amount[1]
						+ d.format(new BigDecimal(amount[2].toString()));
				if (i != results.size() - 1) {
					money = money + " + ";
				}
			}
		} else {
			money = "¥ 0.00";
		}

		return money;
	}
	/**
	 * 
	 * @param uuid
	 * @return
	 */
	public String getOrderMoney(String uuid)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT  GROUP_CONCAT(tmp.tm SEPARATOR '+') money FROM (SELECT  CONCAT(c.currency_mark,FORMAT(SUM(ma.amount),2))   tm  FROM money_amount ma");
		buffer.append(" LEFT JOIN  currency c ON ma.currencyId = c.currency_id  WHERE ma.orderPaySerialNum='");
		buffer.append(uuid);
		buffer.append("' GROUP BY ma.currencyId) tmp");
		return moneyAmountDao.findBySql(buffer.toString()).get(0).toString();
		
	}

	
	
	
	

	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoAmount(String serialNum) {
		String sql = "SELECT m.currencyId,c.currency_name,sum(m.amount),c.currency_mark from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = moneyAmountDao.getSession()
				.createSQLQuery(sql).list();

		String money = "";

		if (results != null && results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				money = money + amount[2].toString();
				if (i != results.size() - 1) {
					money = money + " + ";
				}
			}
		} else {
			money = "¥ 0.00";
		}

		return money;
	}

	@Transactional(readOnly = true)
	public String getMergeMoney(String serialNum) {
		List<MoneyAmount> maList = moneyAmountDao
				.findAmountBySerialNum(serialNum);
		if (maList == null || maList.size() == 0) {
			return "";
		}

		MoneyAmount ma = maList.get(0);
		// if (Context.RMB_CURRENCY_ID.equals(ma.getCurrencyId())) {
		DecimalFormat d = new DecimalFormat(",##0.00");
		return "¥ " + d.format(ma.getAmount());
		// }
		// return "";
	}

	/**
	 * 公用接口，根据流水号取金额，对多币种进行拼接
	 * 
	 * @param visaOrderId
	 *            流水号UUID,多个用逗号隔开，每个UUID用单引号包裹
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoneys(String visaOrderId) {
		String sql = "SELECT CONCAT(yy.currency_mark, SUM(yy.amount)) totalMoney FROM (SELECT payed_deposit from visa v"
				+ " WHERE v.traveler_id IN (SELECT id FROM traveler t WHERE t.orderId = "
				+ visaOrderId
				+ " AND t.order_type = 6)) xx"
				+ " LEFT JOIN (SELECT ma.serialNum, ma.amount, c.currency_mark FROM money_amount ma"
				+ " LEFT JOIN currency c ON ma.currencyId = c.currency_id) yy ON xx.payed_deposit = yy.serialNum WHERE yy.amount IS NOT NULL AND yy.amount <> '' GROUP BY yy.currency_mark";
		List<String> results = moneyAmountDao.getSession().createSQLQuery(sql)
				.list();
		String money = "";
		if (results != null && results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				String amount = results.get(i);
				if (i == 0) {
					money = money + amount;
				} else {
					money = money + "+" + amount;
				}
			}
		} else {
			money = "¥ 0.00";
		}

		return money;
	}
	
	/**
	 * 订单成人价、儿童价、特殊人群价格、订单总价
	 * 
	 * @param serialNum
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoneyStr(String serialNum) {
		String sql = "SELECT c.currency_mark, sum(m.amount) from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = moneyAmountDao.getSession()
				.createSQLQuery(sql).list();

		String money = "";

		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				if (i == results.size() - 1) {
					if (null != amount[1]) {
						money += amount[0]
								+ " "
								+ d.format(new BigDecimal(amount[1].toString()));
					}
				} else {
					if (null != amount[1]) {
						money += amount[0]
								+ " "
								+ d.format(new BigDecimal(amount[1].toString()))
								+ " + ";
					}
				}
			}
		} else {
			money = "¥ 0.00";
		}

		return money;
	}

	/**
	 * 订单成人价、儿童价、特殊人群价格、订单总价，显示币种名称
	 *
	 * @param serialNum
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoneyNameStr(String serialNum) {
		String sql = "SELECT c.currency_name, sum(m.amount) from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = moneyAmountDao.getSession()
				.createSQLQuery(sql).list();

		String money = "";

		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				if (i == results.size() - 1) {
					if (null != amount[1]) {
						money += amount[0]
//								+ " "
								+ d.format(new BigDecimal(amount[1].toString()));
					}
				} else {
					if (null != amount[1]) {
						money += amount[0]
//								+ " "
								+ d.format(new BigDecimal(amount[1].toString()))
								+ " + ";
					}
				}
			}
		} else {
			money = "人民币0.00";
		}

		return money;
	}

	/**
	 * 根据序列号查询值和:格式为（人民币：1,000.00）
	 * 
	 * @param serialNumList
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoneyStr(List<String> serialNumList) {
		StringBuffer serialNum = new StringBuffer("");
		if (CollectionUtils.isNotEmpty(serialNumList)) {
			for (int i = 0; i < serialNumList.size(); i++) {
				if (i != serialNumList.size() - 1) {
					serialNum.append("'" + serialNumList.get(i) + "',");
				} else {
					serialNum.append("'" + serialNumList.get(i) + "'");
				}
			}
		} else {
			return null;
		}
		String sql = "SELECT c.currency_name, sum(m.amount) from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in ("
				+ serialNum + ") GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = moneyAmountDao.getSession()
				.createSQLQuery(sql).list();

		String money = "";

		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				if (i == results.size() - 1) {
					money += amount[0]
							+ d.format(new BigDecimal(amount[1].toString()));
				} else {
					money += amount[0]
							+ d.format(new BigDecimal(amount[1].toString()))
							+ " + ";
				}
			}
		}
		return money;
	}
	
	/**
	 * 根据序列号查询值和:格式为（￥：1,000.00）
	 * 
	 * @param serialNumList
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getCurrMarkMoneyAmountStr(List<String> serialNumList) {
		StringBuffer serialNum = new StringBuffer("");
		if (CollectionUtils.isNotEmpty(serialNumList)) {
			for (int i = 0; i < serialNumList.size(); i++) {
				if (i != serialNumList.size() - 1) {
					serialNum.append("'" + serialNumList.get(i) + "',");
				} else {
					serialNum.append("'" + serialNumList.get(i) + "'");
				}
			}
		} else {
			return null;
		}
		String sql = "SELECT c.currency_mark, sum(m.amount) from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in ("
				+ serialNum + ") GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = moneyAmountDao.getSession()
				.createSQLQuery(sql).list();

		String money = "";

		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				if (i == results.size() - 1) {
					money += amount[0]
							+ d.format(new BigDecimal(amount[1].toString()));
				} else {
					money += amount[0]
							+ d.format(new BigDecimal(amount[1].toString()))
							+ " + ";
				}
			}
		}
		return money;
	}
	
	/**
	 * 获取已付金额 支持 海岛游 酒店的 by chy 2015年6月30日22:58:14
	 */
	public String getRefundPayedMoney2(String recordid, String moneyType, String ordertype){
		String result = "";
		Currency currency;
		int n = 0;
		DecimalFormat df = new DecimalFormat("#0.00");
		if("11".equals(ordertype)){
			List<HotelMoneyAmount> hotelPayedMoney = getHotelPayedMoney(Long.parseLong(recordid), Integer.parseInt(moneyType));
			for(HotelMoneyAmount temp : hotelPayedMoney) {
				temp.getCurrencyId();
				currency = currencyDao.findOne(Long.parseLong(temp.getCurrencyId().toString()));
				if(n == 0){
					result += currency.getCurrencyMark() + " " + (temp.getAmount() == null || temp.getAmount() == 0L ? "0.00" : df.format(temp.getAmount()));
					n++;
				} else {
					result += " + " + currency.getCurrencyMark() + " " + (temp.getAmount() == null || temp.getAmount() == 0L ? "0.00" : df.format(temp.getAmount()));
				}
			}
		} else if("12".equals(ordertype)){
			List<IslandMoneyAmount> islandPayedMoney = getIslandPayedMoney(Long.parseLong(recordid), Integer.parseInt(moneyType));
			for(IslandMoneyAmount temp : islandPayedMoney) {
				temp.getCurrencyId();
				currency = currencyDao.findOne(Long.parseLong(temp.getCurrencyId().toString()));
				if(n == 0){
					result += currency.getCurrencyMark() + " " + (temp.getAmount() == null || temp.getAmount() == 0L ? "0.00" : df.format(temp.getAmount()));
					n++;
				} else {
					result += " + " + currency.getCurrencyMark() + " " + (temp.getAmount() == null || temp.getAmount() == 0L ? "0.00" : df.format(temp.getAmount()));
				}
			}
		} else {
			result = getRefundPaydMoney(recordid, moneyType);
		}
		return result;
	}

	/**
	 * 根据reviewId查询付款已付数据
	 *  酒店
	 * @param reviewId
	 * @return
	 */
	private List<HotelMoneyAmount> getHotelPayedMoney(Long reviewId, Integer paytype) {
		
		List<HotelMoneyAmount> result = new ArrayList<HotelMoneyAmount>();
		// 根据reviewId查询付款记录
		List<Refund> list = refundDao.findByRecordId(reviewId, paytype);
		List<HotelMoneyAmount> hotelAmounts = new ArrayList<HotelMoneyAmount>();
		List<HotelMoneyAmount> lis = new ArrayList<HotelMoneyAmount>();
		String serialNum = "";
		int n = 0;
		for (Refund temp : list) {
			if(temp.getMoneySerialNum() == null || "".equals(temp.getMoneySerialNum())){
				continue;
			}
			if(n==0){
				serialNum += "'" + temp.getMoneySerialNum() + "'";
				n++;
			} else {
				serialNum += ",'" + temp.getMoneySerialNum() + "'";
			}
		}
		if("".equals(serialNum)){
			return result;
		}
		lis = hotelMoneyAmountDao.find("from HotelMoneyAmount hotelMoneyAmount where hotelMoneyAmount.serialNum in ("+serialNum+") and hotelMoneyAmount.delFlag = ?", BaseEntity.DEL_FLAG_NORMAL);
		hotelAmounts.addAll(lis);
		Set<Integer> curSet = new HashSet<Integer>();
		for(HotelMoneyAmount temp : hotelAmounts) {
			if(curSet.contains(temp.getCurrencyId())){
				for(HotelMoneyAmount temp2 : result){
					if(temp2.getCurrencyId() == temp.getCurrencyId()){
						temp2.setAmount(temp2.getAmount() + temp.getAmount());
					}
				}
				continue;
			}
			curSet.add(temp.getCurrencyId());
			result.add(temp);
		}
		return result;
	}

	/**
	 * 根据reviewId查询付款已付数据
	 * 海岛游
	 * @param reviewId
	 * @return
	 */
	private List<IslandMoneyAmount> getIslandPayedMoney(Long reviewId, Integer paytype) {
		
		List<IslandMoneyAmount> result = new ArrayList<IslandMoneyAmount>();
		// 根据reviewId查询付款记录
		List<Refund> list = refundDao.findByRecordId(reviewId, paytype);
		List<IslandMoneyAmount> islandAmounts = new ArrayList<IslandMoneyAmount>();
		List<IslandMoneyAmount> lis = new ArrayList<IslandMoneyAmount>();
		String serialNum = "";
		int n = 0;
		for (Refund temp : list) {
			if(temp.getMoneySerialNum() == null || "".equals(temp.getMoneySerialNum())){
				continue;
			}
			if(n==0){
				serialNum +=  "'" + temp.getMoneySerialNum() + "'";
				n++;
			} else {
				serialNum += ",'" + temp.getMoneySerialNum() + "'";
			}
		}
		if("".equals(serialNum)){
			return result;
		}
		lis = islandMoneyAmountDao.find("from IslandMoneyAmount islandMoneyAmount where islandMoneyAmount.serialNum in (" + serialNum + ") and islandMoneyAmount.delFlag = ?", BaseEntity.DEL_FLAG_NORMAL);
		islandAmounts.addAll(lis);
		Set<Integer> curSet = new HashSet<Integer>();
		for(IslandMoneyAmount temp : islandAmounts) {
			if(curSet.contains(temp.getCurrencyId())){
				for(IslandMoneyAmount temp2 : result){
					if(temp2.getCurrencyId() == temp.getCurrencyId()){
						temp2.setAmount(temp2.getAmount() + temp.getAmount());
					}
				}
				continue;
			}
			curSet.add(temp.getCurrencyId());
			result.add(temp);
		}
		return result;
	}
	
	public String getPayee(String recordId,String orderType,String moneyType){
		StringBuffer payee = new StringBuffer();
		List<Map<String,String>> list = refundDao.findByRecordIDAndOrderTypeAndMoneyType(recordId,orderType,moneyType);
		if(CollectionUtils.isNotEmpty(list)){
			int size = list.size();
			for(int i=0;i<size;i++){
				Map<String,String> r = list.get(i);
				if(i==size-1){
					if(StringUtils.isNotBlank(r.get("payee"))){
					payee.append(r.get("payee"));
					}
				}else{
					if(StringUtils.isNotBlank(r.get("payee"))){
						payee.append(r.get("payee")).append("  |  ");
						}
				}
			}
		}
		return payee.toString();
	}
	
	/**

	 * 
	 * */
	@SuppressWarnings("unchecked")
	public String getRefundPaydMoney(String recordid, String moneyType) {
		List<Refund> refundList = refundDao.findByRecordId(
				Long.valueOf(recordid), Integer.valueOf(moneyType));
		List<Object[]> paydmoney = new ArrayList<Object[]>(); // 付款的时候合并了的记录
																// 0标示不合并 1标示合并
		List<Object[]> mergepaydmoney = new ArrayList<Object[]>();// 付款的时候没有合并的记录
		StringBuffer serialNum = new StringBuffer("");
		StringBuffer mergeSerialNum = new StringBuffer("");
		String money = "";
		int num = refundList.size();
		if (num > 0) {
			for (int i = 0; i < num; i++) {
				if ("0".equals(refundList.get(i).getMergePayFlag())) {
					// 没合并
					serialNum.append("'"
							+ refundList.get(i).getMoneySerialNum() + "',");
				} else {
					// 合并过
					mergeSerialNum
							.append("'"
									+ refundList.get(i)
											.getMergeMoneySerialNum() + "',");
				}
			}
		}
		String serialNumStr = serialNum.toString();
		String mergeSerialNumString = mergeSerialNum.toString();
		if (StringUtils.isNotBlank(serialNumStr)) {
			String serialsql = "SELECT c.currency_id id, c.currency_mark, sum(m.amount) from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in ("
					+ serialNumStr.substring(0, serialNumStr.length() - 1)
					+ ") GROUP BY m.currencyId ORDER BY m.currencyId";
			paydmoney = moneyAmountDao.getSession().createSQLQuery(serialsql)
					.list();
		}
		if (StringUtils.isNotBlank(mergeSerialNumString)) {
		
			String mergeserialsql = "SELECT m.currencyId, sum(m.amount) from money_amount m where  m.serialNum in ("
					+ mergeSerialNumString.substring(0,
							mergeSerialNumString.length() - 1)
					+ ") GROUP BY m.currencyId ORDER BY m.currencyId";
			mergepaydmoney = moneyAmountDao.getSession()
					.createSQLQuery(mergeserialsql).list();
		}
		if (CollectionUtils.isNotEmpty(paydmoney)) {
			if (CollectionUtils.isNotEmpty(mergepaydmoney)) {
				for (int i = 0; i < mergepaydmoney.size(); i++) {
					Object[] amount = mergepaydmoney.get(i);
					// BigDecimal amount = mergepaydmoney.get(i);
					DecimalFormat d = new DecimalFormat(",##0.00");
					if (i == mergepaydmoney.size() - 1) {
						// money += amount[1] + d.format(new
						// BigDecimal(amount[2].toString()));
						money += "¥"
								+ d.format(new BigDecimal(amount[1].toString()));
					} else {
						// money += amount[1] + d.format(new
						// BigDecimal(amount[2].toString())) + " + ";
						money += "¥"
								+ d.format(new BigDecimal(amount[1].toString()))
								+ " + ";
					}
				}
			} else {
				for (int i = 0; i < paydmoney.size(); i++) {
					Object[] amount = paydmoney.get(i);
					DecimalFormat d = new DecimalFormat(",##0.00");
					if (i == paydmoney.size() - 1) {
						money += amount[1]
								+ d.format(new BigDecimal(amount[2].toString()));
					} else {
						money += amount[1]
								+ d.format(new BigDecimal(amount[2].toString()))
								+ " + ";
					}
				}
			}
		} else {
			if (CollectionUtils.isNotEmpty(mergepaydmoney)) {
				for (int i = 0; i < mergepaydmoney.size(); i++) {
					Object[] amount = mergepaydmoney.get(i);
					DecimalFormat d = new DecimalFormat(",##0.00");
					if (i == mergepaydmoney.size() - 1) {
						// money += amount[1] + d.format(new
						// BigDecimal(amount[2].toString()));
						money += "¥"
								+ d.format(new BigDecimal(amount[1].toString()));
					} else {
						// money += amount[1] + d.format(new
						// BigDecimal(amount[2].toString())) + " + ";
						money += "¥"
								+ d.format(new BigDecimal(amount[1].toString()))
								+ " + ";
					}
				}
			}
		}
		return money;
	}

	
	/**应付账款列表获取已付金额并转换成人民币
	 * @param recordId
	 * @param moneyType
	 * @param orderType
	 * @return String
	 * @author zhaohaiming
	 * */
	@SuppressWarnings("unchecked")
	public String getRefundPaydMoney(String recordId, Integer moneyType,Integer orderType) {
		String amount="0.00";
		StringBuffer sql = new StringBuffer("select ");
		if(orderType == Context.ORDER_TYPE_HOTEL){
			
		}else if(orderType == Context.ORDER_TYPE_ISLAND){
			
		}else{
			sql.append("sum(( CASE r.mergePayFlag WHEN 0 THEN ( SELECT m.amount * IFNULL(m.exchangerate, 1) FROM money_amount m WHERE ")
				.append(" m.serialNum = r.money_serial_num ) WHEN 1 THEN ( SELECT m.amount FROM money_amount m WHERE m.serialNum = ")
				.append(" r.merge_money_serial_num ) ELSE '' END )) amount");
		}
		sql.append(" FROM refund r WHERE r.moneyType in( ").append(moneyType).append(")")
			.append(" AND r.orderType = ").append(orderType)
			.append(" AND r.`status` IS NULL AND r.record_id IN (").append(recordId).append(")");
		List<Double> list = moneyAmountDao.getSession().createSQLQuery(sql.toString()).list();
		if(CollectionUtils.isNotEmpty(list)){
			//集合里只有一个元素，直接取
			if(list.get(0) != null){
			amount = list.get(0).toString();
			}
		}
		return amount;
	}
	// /**
	// *
	// * @Title: getRefundPaydMoneyList
	// * @Description: TODO(获取付款金额数据)
	// * @param @param recordid
	// * @return List<Object[]> 返回类型
	// * @throws
	// */
	// @SuppressWarnings("unchecked")
	// public List<Object[]> getRefundPaydMoneyList(String recordid){
	// List<Refund> refundList =
	// refundDao.findByRecordId(Long.valueOf(recordid));
	// List<Object[]> paydmoney = new ArrayList<Object[]>(); //付款的时候合并了的记录
	// 0标示不合并 1标示合并
	//
	// StringBuffer serialNum = new StringBuffer("");
	// StringBuffer mergeSerialNum = new StringBuffer("");
	// // String money="";
	// int num = refundList.size();
	// if(num>0){
	// for(int i = 0;i<num;i++){
	// if("0".equals(refundList.get(i).getMergePayFlag())){
	// //没合并
	// serialNum.append("'" + refundList.get(i).getMoneySerialNum() + "',");
	// }else{
	// //合并过
	// mergeSerialNum.append("'"+refundList.get(i).getMergeMoneySerialNum()+"',");
	// }
	// }
	// }
	// String serialNumStr=serialNum.toString();
	// String mergeSerialNumString=mergeSerialNum.toString();
	// if(StringUtils.isNotBlank(serialNumStr)){
	// String serialsql =
	// "SELECT c.currency_id id, c.currency_mark, sum(m.amount) from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in ("
	// + serialNumStr.substring(0, serialNumStr.length()-1) +
	// ") GROUP BY m.currencyId ORDER BY m.currencyId";
	// paydmoney = moneyAmountDao.getSession().createSQLQuery(serialsql).list();
	// }
	// if(StringUtils.isNotBlank(mergeSerialNumString)){
	// // String mergeserialsql =
	// "SELECT c.currency_id id, c.currency_mark, sum(m.amount) from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in ("
	// // + mergeSerialNumString.substring(0, mergeSerialNumString.length()-1) +
	// ") GROUP BY m.currencyId ORDER BY m.currencyId";
	// //
	// String mergeserialsql =
	// "SELECT m.currencyId, sum(m.amount) from money_amount m where  m.serialNum in ("
	// + mergeSerialNumString.substring(0, mergeSerialNumString.length()-1) +
	// ") GROUP BY m.currencyId ORDER BY m.currencyId";
	// paydmoney =
	// moneyAmountDao.getSession().createSQLQuery(mergeserialsql).list();
	// }
	//
	// return paydmoney;
	// }

	/**
	 * 根据序列号查询值和
	 * 
	 * @param serialNumList
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public List<String> getMoneyIdAndPrice(List<String> serialNumList) {
		StringBuffer serialNum = new StringBuffer("");
		if (CollectionUtils.isNotEmpty(serialNumList)) {
			for (int i = 0; i < serialNumList.size(); i++) {
				if (i != serialNumList.size() - 1) {
					serialNum.append("'" + serialNumList.get(i) + "',");
				} else {
					serialNum.append("'" + serialNumList.get(i) + "'");
				}
			}
		} else {
			return null;
		}
		String sql = "SELECT c.currency_id, sum(m.amount) from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum in ("
				+ serialNum + ") GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = moneyAmountDao.getSession()
				.createSQLQuery(sql).list();

		List<String> srcCurrency = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				srcCurrency.add(amount[0] + " " + amount[1]);
			}
		}

		return srcCurrency;
	}

	/**
	 * 此接口用于保存金额的业务，如果与支付有关的接口需要调用saveMoneyAmounts
	 *
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean saveOrUpdateMoneyAmounts(String serialNum, List<MoneyAmount> moneyAmounts) {

		try {
			if (moneyAmounts == null) {
				return false;
			}

			for (MoneyAmount ma : moneyAmounts) {
				//下面一段不需要故注释 在saveOrUpdateMoneyAmount 有下面的代码  update by shijun.liu 2016.03.08
				/*List<MoneyAmount> exsitMaList = moneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, ma.getCurrencyId());
				if (exsitMaList != null && exsitMaList.size() > 0) {
					ma.setId(exsitMaList.get(0).getId());
					ma.setExchangerate(exsitMaList.get(0).getExchangerate());
				} else {
					Currency currency = currencyDao.findOne(Long.parseLong(ma.getCurrencyId().toString()));
					ma.setExchangerate(currency.getConvertLowest());
				}*/
				saveOrUpdateMoneyAmount(ma);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 此接口用于保存金额的业务，如果与支付有关的接口需要调用saveMoneyAmounts
	 * 
	 * @param moneyAmount
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean saveOrUpdateMoneyAmount(MoneyAmount moneyAmount) {
		String companyUuid = UserUtils.getCompanyUuid();
		try {
			if (moneyAmount == null) {
				return false;
			}
			List<MoneyAmount> exsitMaList = moneyAmountDao.findAmountBySerialNumAndCurrencyId(moneyAmount.getSerialNum(), moneyAmount.getCurrencyId());
			if (exsitMaList != null && exsitMaList.size() > 0 ) {
				if(Context.SUPPLIER_UUID_MTGJ.equals(companyUuid)) {
					// 针对美图国际 
					if(exsitMaList.get(0).getAmount().equals(BigDecimal.ZERO)){
						moneyAmount.setId(exsitMaList.get(0).getId());
					}
				} else {
					moneyAmount.setId(exsitMaList.get(0).getId());
				}
				moneyAmount.setExchangerate(exsitMaList.get(0).getExchangerate());
			} else if(null == moneyAmount.getExchangerate()){
				Currency currency = currencyDao.findOne(Long.parseLong(moneyAmount.getCurrencyId().toString()));
				moneyAmount.setExchangerate(currency.getConvertLowest());
			}
			moneyAmountDao.save(moneyAmount);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * 根据支付流水查询订单已付金额UUID
	 * 
	 * @param moneyAmount
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	private String getOrderPayedMoneyUid(MoneyAmount moneyAmount) {
		Integer orderType = moneyAmount.getOrderType();
		String serialNum = null;
		if (orderType != null && orderType == 7) {
			// 机票订单修改已收
			AirticketOrder airticketOrder = airticketPreOrderDao
					.findOne(moneyAmount.getUid());
			serialNum = airticketOrder.getPayedMoney();

			if (StringUtils.isEmpty(serialNum)) {
				serialNum = generateUUID();
				airticketOrder.setPayedMoney(serialNum);
				//如果机票订单状态不为已支付且不为待财务确认则是订金已支付（这里的逻辑不知道是不是对） add by： yakun.bai；需求号： C362；Date： 2015.11.16
				if (airticketOrder.getOrderState() != Integer.parseInt(Context.ORDER_PAYSTATUS_YZF) 
						&& airticketOrder.getOrderState() != Integer.parseInt(Context.ORDER_PAYSTATUS_CW)) {
					airticketOrder.setOrderState(4);
				}

				airticketOrderDao.getSession().update(airticketOrder);
			}
		} else if (orderType != null && orderType == 6) {
			// 签证订单修改已收
			VisaOrder visaOrder = (VisaOrder) visaOrderDao.getSession().get(
					VisaOrder.class, moneyAmount.getUid());
			serialNum = visaOrder.getPayedMoney();
			if (StringUtils.isEmpty(serialNum)) {
				serialNum = generateUUID();
				visaOrder.setPayedMoney(serialNum);
				visaOrderDao.getSession().update(visaOrder);
			}
		} else {
			// 单团、散拼、游学、大客户、自由行 订单修改已收
			ProductOrderCommon productOrder = (ProductOrderCommon) productOrderDao
					.getSession().get(ProductOrderCommon.class,
							moneyAmount.getUid());
			serialNum = productOrder.getPayedMoney();
			if (StringUtils.isEmpty(serialNum)) {
				serialNum = generateUUID();
				productOrder.setPayedMoney(serialNum);
				productOrderDao.getSession().update(productOrder);
			}
		}
		return serialNum;
	}

	/**
	 * 保存或更新金额
	 * 
	 * @param moneyAmount
	 * @param serialNum
	 */
	public MoneyAmount saveOrUpdateMoneyAmount(MoneyAmount moneyAmount, String serialNum, String operate, Integer moneyType) {
		MoneyAmount payedMoney;

		List<MoneyAmount> list = moneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, moneyAmount.getCurrencyId());
		if (list != null && list.size() > 0) {
			payedMoney = list.get(0);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				BigDecimal amount = payedMoney.getAmount().add(moneyAmount.getAmount());
				moneyAmountDao.updateOrderForAmount(payedMoney.getId(), payedMoney.getAmount().add(moneyAmount.getAmount()));
				payedMoney.setAmount(amount);
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				BigDecimal amount = payedMoney.getAmount().subtract(moneyAmount.getAmount());
				moneyAmountDao.updateOrderForAmount(payedMoney.getId(), amount);
				payedMoney.setAmount(amount);
			}
		} else {
			payedMoney = new MoneyAmount();
			payedMoney.setSerialNum(serialNum);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				payedMoney.setAmount(moneyAmount.getAmount());
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				payedMoney.setAmount(moneyAmount.getAmount().negate());// 加了取反
																		// 2015年1月31日17:03:31
			}
			payedMoney.setBusindessType(moneyAmount.getBusindessType());
			payedMoney.setCreatedBy(moneyAmount.getCreatedBy());
			payedMoney.setCreateTime(moneyAmount.getCreateTime());
			payedMoney.setCurrencyId(moneyAmount.getCurrencyId());
			payedMoney.setMoneyType(moneyType);
			payedMoney.setUid(moneyAmount.getUid());
			payedMoney.setOrderType(moneyAmount.getOrderType());

			saveOrUpdateMoneyAmount(payedMoney);
		}
		return payedMoney;
	}
	
	/**
	 * 保存或更新金额(传入新订单。由于转款的时候，如果出现了没有过的币种，存amount的时候取orderType不应该是old订单的类型，而应该是新订单的类型)
	 * 
	 * @param moneyAmount
	 * @param serialNum
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	private MoneyAmount saveOrUpdateMoneyAmount(MoneyAmount moneyAmount, String serialNum, String operate, Integer moneyType, Integer orderType) {
		MoneyAmount payedMoney;

		List<MoneyAmount> list = moneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, moneyAmount.getCurrencyId());
		if (list != null && list.size() > 0) {
			payedMoney = list.get(0);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				BigDecimal amount = payedMoney.getAmount().add(moneyAmount.getAmount());
				moneyAmountDao.updateOrderForAmount(payedMoney.getId(), payedMoney.getAmount().add(moneyAmount.getAmount()));
				payedMoney.setAmount(amount);
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				BigDecimal amount = payedMoney.getAmount().subtract(moneyAmount.getAmount());
				moneyAmountDao.updateOrderForAmount(payedMoney.getId(), amount);
				payedMoney.setAmount(amount);
			}
		} else {
			payedMoney = new MoneyAmount();
			payedMoney.setSerialNum(serialNum);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				payedMoney.setAmount(moneyAmount.getAmount());
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				payedMoney.setAmount(moneyAmount.getAmount().negate());// 加了取反
																		// 2015年1月31日17:03:31
			}
			payedMoney.setBusindessType(moneyAmount.getBusindessType());
			payedMoney.setCreatedBy(moneyAmount.getCreatedBy());
			payedMoney.setCreateTime(moneyAmount.getCreateTime());
			payedMoney.setCurrencyId(moneyAmount.getCurrencyId());
			payedMoney.setMoneyType(moneyType);
			payedMoney.setUid(moneyAmount.getUid());
			payedMoney.setOrderType(orderType);

			saveOrUpdateMoneyAmount(payedMoney);
		}
		return payedMoney;
	}

	/**
	 * 保存或更新金额(新审批moneyamount)
	 * 
	 * @param moneyAmount
	 * @param serialNum
	 */
	@Transactional
	public boolean saveOrUpdateMoneyAmount(NewProcessMoneyAmount moneyAmount, String serialNum, String operate, Integer moneyType) {
		boolean flag = true;
		MoneyAmount payedMoney;
		List<MoneyAmount> list = moneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, moneyAmount.getCurrencyId());
		if (list != null && list.size() > 0) {
			payedMoney = list.get(0);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				payedMoney.setAmount(payedMoney.getAmount().add(moneyAmount.getAmount()));
				moneyAmountDao.getSession().update(payedMoney);
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				payedMoney.setAmount(payedMoney.getAmount().subtract(moneyAmount.getAmount()));
				moneyAmountDao.getSession().update(payedMoney);
			}
		} else {
			payedMoney = new MoneyAmount();
			payedMoney.setSerialNum(serialNum);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				payedMoney.setAmount(moneyAmount.getAmount());
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				payedMoney.setAmount(moneyAmount.getAmount().negate());// 加了取反 // 2015年1月31日17:03:31
			}
			payedMoney.setBusindessType(moneyAmount.getBusindessType());
			payedMoney.setCreatedBy(moneyAmount.getCreatedBy());
			payedMoney.setCreateTime(moneyAmount.getCreateTime());
			payedMoney.setCurrencyId(moneyAmount.getCurrencyId());
			payedMoney.setMoneyType(moneyType);
			payedMoney.setUid(moneyAmount.getUid());
			payedMoney.setOrderType(moneyAmount.getOrderType());

			flag = saveOrUpdateMoneyAmount(payedMoney);
		}
		return flag;
	}
	
	/**
	 * 保存或更新金额(新审批moneyamount),指定orderType
	 * 
	 * @param moneyAmount
	 * @param serialNum
	 */
	@Transactional
	public boolean saveOrUpdateMoneyAmount(NewProcessMoneyAmount moneyAmount, String serialNum, String operate, Integer moneyType, Integer orderType) {
		boolean flag = true;
		MoneyAmount payedMoney;
		List<MoneyAmount> list = moneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, moneyAmount.getCurrencyId());
		if (list != null && list.size() > 0) {
			payedMoney = list.get(0);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				payedMoney.setAmount(payedMoney.getAmount().add(moneyAmount.getAmount()));
				payedMoney.setOrderType(orderType);
				moneyAmountDao.getSession().update(payedMoney);
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				payedMoney.setAmount(payedMoney.getAmount().subtract(moneyAmount.getAmount()));
				payedMoney.setOrderType(orderType);
				moneyAmountDao.getSession().update(payedMoney);
			}
		} else {
			payedMoney = new MoneyAmount();
			payedMoney.setSerialNum(serialNum);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				payedMoney.setAmount(moneyAmount.getAmount());
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				payedMoney.setAmount(moneyAmount.getAmount().negate());// 加了取反 // 2015年1月31日17:03:31
			}
			payedMoney.setBusindessType(moneyAmount.getBusindessType());
			payedMoney.setCreatedBy(moneyAmount.getCreatedBy());
			payedMoney.setCreateTime(moneyAmount.getCreateTime());
			payedMoney.setCurrencyId(moneyAmount.getCurrencyId());
			payedMoney.setMoneyType(moneyType);
			payedMoney.setUid(moneyAmount.getUid());
			payedMoney.setOrderType(orderType);

			flag = saveOrUpdateMoneyAmount(payedMoney);
		}
		return flag;
	}
	
	/**
	 * 返回订单已付金额和达帐金额的UUID
	 * 
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String[] getAccountedMoneyUid(Long orderId, int orderType) {
		String payedMoney = null;
		String accountedMoney = null;
		if (orderType == Context.ORDER_TYPE_JP) {
			// 机票订单
			AirticketOrder airticketOrder = airticketPreOrderDao
					.findOne(orderId);// airticketOrderDao.getAirticketOrderById(orderId);
			accountedMoney = airticketOrder.getAccountedMoney();
			payedMoney = airticketOrder.getPayedMoney();

			if (StringUtils.isEmpty(accountedMoney)) {
				accountedMoney = generateUUID();
				airticketOrder.setAccountedMoney(accountedMoney);
				airticketOrderDao.getSession().update(airticketOrder);
			}
		} else if (orderType == Context.ORDER_TYPE_QZ) {
			// 签证订单
			VisaOrder visaOrder = (VisaOrder) visaOrderDao.getSession().get(
					VisaOrder.class, orderId);
			accountedMoney = visaOrder.getAccountedMoney();
			payedMoney = visaOrder.getPayedMoney();

			if (StringUtils.isEmpty(accountedMoney)) {
				accountedMoney = generateUUID();
				visaOrder.setAccountedMoney(accountedMoney);
				visaOrderDao.getSession().update(visaOrder);
			}
		} else if ((orderType > 0 && orderType < 6) || orderType == Context.ORDER_TYPE_CRUISE) {
			// 单团订单
			ProductOrderCommon pOrder = (ProductOrderCommon) productOrderDao
					.getSession().get(ProductOrderCommon.class, orderId);
			accountedMoney = pOrder.getAccountedMoney();
			payedMoney = pOrder.getPayedMoney();

			if (StringUtils.isEmpty(accountedMoney)) {
				accountedMoney = generateUUID();
				pOrder.setAccountedMoney(accountedMoney);
				productOrderDao.getSession().update(pOrder);
			}
		}

		String[] money = { accountedMoney, payedMoney };
		return money;
	}
	
	/**
	 * 返回订单已付金额和达帐金额的UUID
	 * 
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public String[] getAccountedMoneyUid(String orderUuid, int orderType) {
		String payedMoney = null;
		String accountedMoney = null;
		
		if(Context.ORDER_TYPE_ISLAND == orderType) {
			//海岛游订单
			IslandOrder islandOrder = islandOrderDao.getByUuid(orderUuid);
			accountedMoney = islandOrder.getAccountedMoney();
			payedMoney = islandOrder.getPayedMoney();
			
			if(StringUtils.isEmpty(accountedMoney)) {
				accountedMoney = UuidUtils.generUuid();
				islandOrder.setAccountedMoney(accountedMoney);
				islandOrderDao.updateObj(islandOrder);
			}
		} else if(Context.ORDER_TYPE_HOTEL == orderType) {
			//酒店订单
			HotelOrder hotelOrder = hotelOrderDao.getByUuid(orderUuid);
			accountedMoney = hotelOrder.getAccountedMoney();
			payedMoney = hotelOrder.getPayedMoney();
			if(StringUtils.isEmpty(accountedMoney)) {
				accountedMoney = UuidUtils.generUuid();
				hotelOrder.setAccountedMoney(accountedMoney);
				hotelOrderDao.updateObj(hotelOrder);
			}
		}
		
		String[] money = { accountedMoney, payedMoney };
		return money;
	}

	/**
	 * 多币种相减
	 * 
	 * @param srcPrice
	 *            原始金额
	 * @param targetPrice
	 *            被减金额
	 * @return
	 */
	public List<String> subtract(List<String> srcCurreney,
			List<String> targetCurreney) {
		List<String> result = Lists.newArrayList();
		String idArr = "";
		String priceArr = "";
		if (CollectionUtils.isNotEmpty(srcCurreney)) {
			// 获取原始金额币种id和金额，查询被减金额中是否存在此种类型币种，如果有则相减
			for (int i = 0; i < srcCurreney.size(); i++) {
				String srcCurrencyId = srcCurreney.get(i).split(" ")[0];
				String srcCurrencyPrice = srcCurreney.get(i).split(" ")[1];
				for (int j = 0; j < targetCurreney.size(); j++) {
					String targetCurrencyId = targetCurreney.get(j).split(" ")[0];
					String targetCurrencyPrice = targetCurreney.get(j).split(
							" ")[1];
					if (srcCurrencyId.equals(targetCurrencyId)) {
						BigDecimal srcPrice = new BigDecimal(srcCurrencyPrice);
						BigDecimal targetPrice = new BigDecimal(
								targetCurrencyPrice);
						srcCurrencyPrice = srcPrice.subtract(targetPrice)
								.toString();
						break;
					}
				}
				if (!"0.00".equals(srcCurrencyPrice)) {
					idArr += srcCurrencyId + ",";
					priceArr += srcCurrencyPrice + ",";
				}
			}

			// 如果被减金额中有原始金额没有的币种，则写为负值
			for (int i = 0; i < targetCurreney.size(); i++) {
				String targetId = targetCurreney.get(i).split(" ")[0];
				String targetPrice = targetCurreney.get(i).split(" ")[1];
				boolean isHaveId = false;
				for (int j = 0; j < srcCurreney.size(); j++) {
					String srcId = srcCurreney.get(j).split(" ")[0];
					if (targetId.equals(srcId)) {
						isHaveId = true;
						break;
					}
				}
				if (!isHaveId && !"0.00".equals(targetPrice)) {
					idArr += targetId + ",";
					//targetPrice小于等于0，不加“-”号-------by---junhao.zhao--2017-03-22--
					if(new BigDecimal(targetPrice).compareTo(new BigDecimal("0"))<=0){
						priceArr += targetPrice + ",";
					}else {
						priceArr += "-" + targetPrice + ",";
					}
				}
			}

			if (StringUtils.isNotBlank(idArr)
					&& StringUtils.isNotBlank(priceArr)) {
				idArr = idArr.substring(0, idArr.lastIndexOf(","));
				priceArr = priceArr.substring(0, priceArr.lastIndexOf(","));
				result.add(idArr);
				result.add(priceArr);
			}
		}
		return result;
	}

	/**
	 * 多币种相加或相减
	 * 
	 * @param srcPrice
	 *            原始金额
	 * @param targetPrice
	 *            被减金额
	 * @param isAdd
	 *            相加或相减
	 * @return
	 */
	public String addOrSubtract(String srcCurreneyStr, String targetCurreneyStr, boolean isAdd) {
		List<String> srcCurreney = Lists.newArrayList();
		List<String> targetCurreney = Lists.newArrayList();

		// 把原始金额字符串按 + 分割放入list
		if (StringUtils.isNotBlank(srcCurreneyStr)) {
			String[] srcCurrencyArr = srcCurreneyStr.split("\\+");
			if (srcCurrencyArr != null && srcCurrencyArr.length > 0) {
				for (int i = 0; i < srcCurrencyArr.length; i++) {
					String temp = srcCurrencyArr[i].trim();
					if (StringUtils.isNotBlank(temp) && temp.split(" ").length == 2) {
						srcCurreney.add(temp);
					}
				}
			}
		}

		// 把目标金额字符串按 + 分割放入list
		if (StringUtils.isNotBlank(targetCurreneyStr)) {
			String[] targetCurrencyArr = targetCurreneyStr.split("\\+");
			if (targetCurrencyArr != null && targetCurrencyArr.length > 0) {
				for (int i = 0; i < targetCurrencyArr.length; i++) {
					String temp = targetCurrencyArr[i].trim();
					if (StringUtils.isNotBlank(temp) && temp.split(" ").length == 2) {
						targetCurreney.add(temp);
					}
				}
			}
		}

		String result = "";
		if (CollectionUtils.isNotEmpty(srcCurreney)) {
			// 获取原始金额币种id和金额，查询被减金额中是否存在此种类型币种，如果有则相减
			for (int i = 0; i < srcCurreney.size(); i++) {
				String srcCurrencyMark = srcCurreney.get(i).split(" ")[0];
				String srcCurrencyPrice = srcCurreney.get(i).split(" ")[1];
				for (int j = 0; j < targetCurreney.size(); j++) {
					String targetCurrencyMark = targetCurreney.get(j)
							.split(" ")[0];
					String targetCurrencyPrice = targetCurreney.get(j).split(
							" ")[1];
					if (srcCurrencyMark.equals(targetCurrencyMark)) {
						BigDecimal srcPrice = new BigDecimal(srcCurrencyPrice);
						BigDecimal targetPrice = new BigDecimal(
								targetCurrencyPrice);
						if (isAdd) {
							srcCurrencyPrice = srcPrice.add(targetPrice)
									.toString();
						} else {
							srcCurrencyPrice = srcPrice.subtract(targetPrice)
									.toString();
						}
						break;
					}
				}
				if (!"0.00".equals(srcCurrencyPrice)) {
					result += srcCurrencyMark + " " + srcCurrencyPrice + "+";
				}
			}

			// 如果被减金额中有原始金额没有的币种，则写为负值
			for (int i = 0; i < targetCurreney.size(); i++) {
				String targetMark = targetCurreney.get(i).split(" ")[0];
				String targetPrice = targetCurreney.get(i).split(" ")[1];
				boolean isHaveId = false;
				for (int j = 0; j < srcCurreney.size(); j++) {
					String srcId = srcCurreney.get(j).split(" ")[0];
					if (targetMark.equals(srcId)) {
						isHaveId = true;
						break;
					}
				}
				if (!isHaveId && !"0.00".equals(targetPrice)) {
					if (isAdd) {
						result += targetMark + " " + targetPrice + "+";
					} else {
						if (targetPrice.contains("-")) {
							result += targetMark + " " + targetPrice.replace("-", "") + "+";
						} else {
							result += targetMark + " -" + targetPrice + "+";
						}
					}
				}
			}

			if (StringUtils.isNotBlank(result)) {
				result = result.substring(0, result.lastIndexOf("+"));
			}
		}
		return result;
	}

	/**
	 * 转款
	 * 
	 * @param newOrderId
	 * @param oldOrderId
	 * @param uuid
	 * @param orderType
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String transferMoney(Long newOrderId, Long oldOrderId, String uuid,
			int orderType) {
		String flag = "success";
		if (orderType == Context.ORDER_TYPE_JP) {
			// 机票订单

		} else if (orderType == Context.ORDER_TYPE_QZ) {
			// 签证订单

		} else if (orderType > 0 && orderType < 6) {
			// 单团订单
			ProductOrderCommon oldOrder = (ProductOrderCommon) productOrderDao
					.getSession().get(ProductOrderCommon.class, oldOrderId);

			if (true) {
				ProductOrderCommon newOrder = (ProductOrderCommon) productOrderDao
						.getSession().get(ProductOrderCommon.class, newOrderId);
				if (StringUtils.isEmpty(newOrder.getAccountedMoney())) {
					newOrder.setAccountedMoney(generateUUID());
				}
				if (StringUtils.isEmpty(newOrder.getPayedMoney())) {
					newOrder.setPayedMoney(generateUUID());
				}

				List<MoneyAmount> list = moneyAmountDao.findAmountBySerialNum(uuid);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String payedSerialNum = oldOrder.getPayedMoney();
						String accountedSerialNum = oldOrder.getAccountedMoney();
						//如果原订单没有实收金额，则不用进行实收转款处理
						if (StringUtils.isNotBlank(payedSerialNum) && !"¥ 0.00".equals(getMoneyStr(payedSerialNum))) {
//							saveOrUpdateMoneyAmount(list.get(i), oldOrder.getPayedMoney(), "subtract", Context.MONEY_TYPE_YS);
//							saveOrUpdateMoneyAmount(list.get(i), newOrder.getPayedMoney(), "add", Context.MONEY_TYPE_YS);
							saveOrUpdateMoneyAmount(list.get(i), oldOrder.getPayedMoney(), "subtract", Context.MONEY_TYPE_YS, oldOrder.getOrderStatus());
							saveOrUpdateMoneyAmount(list.get(i), newOrder.getPayedMoney(), "add", Context.MONEY_TYPE_YS, newOrder.getOrderStatus());
						}
						//如果原订单没有达帐金额，则不用进行达帐转款处理
						if (StringUtils.isNotBlank(accountedSerialNum) && !"¥ 0.00".equals(getMoneyStr(accountedSerialNum))) {
//							saveOrUpdateMoneyAmount(list.get(i), oldOrder.getAccountedMoney(), "subtract", Context.MONEY_TYPE_DZ);
//							saveOrUpdateMoneyAmount(list.get(i), newOrder.getAccountedMoney(), "add", Context.MONEY_TYPE_DZ);
							saveOrUpdateMoneyAmount(list.get(i), oldOrder.getAccountedMoney(), "subtract", Context.MONEY_TYPE_DZ, oldOrder.getOrderStatus());
							saveOrUpdateMoneyAmount(list.get(i), newOrder.getAccountedMoney(), "add", Context.MONEY_TYPE_DZ, newOrder.getOrderStatus());
						}
					}
				}
			}
			// else if(payedMoneyFlag==2){
			// flag = "已付金额不足！";
			// }else if(accountedMoneyFlag==2){
			// flag = "达账金额不足！";
			// }
		}

		return flag;

	}

	@Transactional(readOnly = true)
	public List<MoneyAmount> findAmountBySerialNum(String serialNum) {
		return moneyAmountDao.findAmountBySerialNum(serialNum);
	}

	@Transactional(readOnly = false)
	public List<MoneyAmount> findAmountBySerialNumAndCurrencyId(
			String serialNum, Integer currencyId) {
		return moneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum,
				currencyId);
	}

	@Transactional(readOnly = true)
	public List<MoneyAmount> findAmount(Long uid, Integer moneyType,
			Integer orderType) {
		return moneyAmountDao.findAmount(uid, moneyType, orderType);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delMoneyAmountBySerialNum(String serialNum) {
		moneyAmountDao.delMoneyAmountBySerialNum(serialNum);
	}
	
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void delMoneyAmountBySerialNum2Delflag(String serialNum) {
		moneyAmountDao.delMoneyAmountBySerialNum2Delflag(serialNum);
	}

	@Transactional(readOnly = true)
	public MoneyAmount findOneAmountBySerialNum(String serialNum) {
		return moneyAmountDao.findOneAmountBySerialNum(serialNum);
	}
	
	@Transactional(readOnly = true)
	public List<MoneyAmount> findBySerialNum(String serialNum) {
		return moneyAmountDao.findAmountListBySerialNum(serialNum);
	}

	/**
	 * 两个UUID对应的金额相加
	 * 
	 * @param uuid1
	 * @param uuid2
	 * @return
	 */
	public List<MoneyAmount> addMoney(String uuid1, String uuid2) {
		return addOrDecreaseMoney(uuid1, uuid2, "add");
	}

	/**
	 * 两个UUID对应的金额相减 （uuid1对应的值要大于uuid2对应的值）
	 * 
	 * @param uuid1
	 * @param uuid2
	 * @return
	 */
	public List<MoneyAmount> decreaseMoney(String uuid1, String uuid2) {
		return addOrDecreaseMoney(uuid1, uuid2, "decrease");
	}

	/**
	 * 两个UUID对应的金额相减 （uuid1对应的值要大于uuid2对应的值）
	 * 
	 * @param uuid1
	 * @param uuid2
	 * @return
	 */
	public List<MoneyAmount> decreaseMoneyNew(String uuid1, String uuid2) {
		return addOrDecreaseMoneyNew(uuid1, uuid2, "decrease");
	}
	/**
	 * 金额相减 ，0.00不计算
	 * @param uuid1
	 * @param uuid2
	 * @param flag
	 * @return
	 */
	private List<MoneyAmount> addOrDecreaseMoneyNew(String uuid1, String uuid2,
			String flag) {
		List<MoneyAmount> result = new ArrayList<MoneyAmount>();
		List<MoneyAmount> list1 = moneyAmountDao.findAmountBySerialNum(uuid1);
		List<MoneyAmount> list2 = moneyAmountDao.findAmountBySerialNum(uuid2);
		Map<String, BigDecimal> map1 = new LinkedHashMap<>();
		if (list1 != null) {
			for (MoneyAmount ma1 : list1) {
				map1.put(ma1.getCurrencyId().toString(), ma1.getAmount());
			}
		}
		if (list2 != null) {
			for (MoneyAmount ma2 : list2) {
				String key = ma2.getCurrencyId().toString();
				if (map1.containsKey(key)) {
					if ("add".equals(flag)) {
						map1.put(key, map1.get(key).add(ma2.getAmount()));
					} else {
						map1.put(key, map1.get(key).subtract(ma2.getAmount()));
					}
				} else {
					if ("add".equals(flag)) {
						map1.put(key, ma2.getAmount());
					} else {
						map1.put(key,
								new BigDecimal(0).subtract(ma2.getAmount()));
					}
				}
			}
		}
		for (String key : map1.keySet()) {
			//0.00不计算
			if(!("0.00").equals(map1.get(key).toString())){
				MoneyAmount ma = new MoneyAmount();
				ma.setCurrencyId(Integer.parseInt(key));
				ma.setAmount(map1.get(key));
				result.add(ma);
			}
		}
		return result;
	}


	/**
	 * 由指定的flag对两个UUID对应的金额相加或相减
	 * 
	 * @param uuid1
	 * @param uuid2
	 * @param flag
	 * @return
	 */
	private List<MoneyAmount> addOrDecreaseMoney(String uuid1, String uuid2,
			String flag) {

		List<MoneyAmount> result = new ArrayList<MoneyAmount>();

		List<MoneyAmount> list1 = moneyAmountDao.findAmountBySerialNum(uuid1);
		List<MoneyAmount> list2 = moneyAmountDao.findAmountBySerialNum(uuid2);

		Map<String, BigDecimal> map1 = new LinkedHashMap<>();
		if (list1 != null) {
			for (MoneyAmount ma1 : list1) {
				map1.put(ma1.getCurrencyId().toString(), ma1.getAmount());
			}
		}

		if (list2 != null) {
			for (MoneyAmount ma2 : list2) {
				String key = ma2.getCurrencyId().toString();
				if (map1.containsKey(key)) {
					if ("add".equals(flag)) {
						map1.put(key, map1.get(key).add(ma2.getAmount()));
					} else {
						map1.put(key, map1.get(key).subtract(ma2.getAmount()));
					}
				} else {
					if ("add".equals(flag)) {
						map1.put(key, ma2.getAmount());
					} else {
						map1.put(key,
								new BigDecimal(0).subtract(ma2.getAmount()));
					}
				}
			}
		}

		for (String key : map1.keySet()) {
			MoneyAmount ma = new MoneyAmount();
			ma.setCurrencyId(Integer.parseInt(key));
			ma.setAmount(map1.get(key));
			result.add(ma);
		}

		return result;
	}
	
	
	/**
	 * 多币种金额乡间，如果有为零的记录则忽略不计
	 * 例子：￥100-（￥1+$0+£0）=￥99
	 * @param uuid1
	 * @param uuid2
	 * @param flag
	 * @return
	 */
	public List<MoneyAmount> minusMoney(String uuid1, String uuid2,String flag) {

		List<MoneyAmount> result = new ArrayList<MoneyAmount>();

		List<MoneyAmount> list1 = moneyAmountDao.findAmountBySerialNum(uuid1);
		List<MoneyAmount> list2 = moneyAmountDao.findAmountBySerialNumAndAmount(uuid2);

		Map<String, BigDecimal> map1 = new LinkedHashMap<>();
		if (list1 != null) {
			for (MoneyAmount ma1 : list1) {
				map1.put(ma1.getCurrencyId().toString(), ma1.getAmount());
			}
		}

		if (list2 != null) {
			for (MoneyAmount ma2 : list2) {
				String key = ma2.getCurrencyId().toString();
				if (map1.containsKey(key)) {
					if ("add".equals(flag)) {
						map1.put(key, map1.get(key).add(ma2.getAmount()));
					} else {
						map1.put(key, map1.get(key).subtract(ma2.getAmount()));
					}
				} else {
					if ("add".equals(flag)) {
						map1.put(key, ma2.getAmount());
					} else {
						map1.put(key,
								new BigDecimal(0).subtract(ma2.getAmount()));
					}
				}
			}
		}

		for (String key : map1.keySet()) {
			MoneyAmount ma = new MoneyAmount();
			ma.setCurrencyId(Integer.parseInt(key));
			ma.setAmount(map1.get(key));
			result.add(ma);
		}

		return result;
	}
	
	/**
	 * 更新金额
	 * 
	 * @param uuid
	 * @param newList
	 */
	public void updateMoneyAmount(String uuid, List<MoneyAmount> newList) {
		List<MoneyAmount> oldList = moneyAmountDao.findAmountBySerialNum(uuid);

		for (MoneyAmount oldMa : oldList) {
			for (MoneyAmount newMa : newList) {
				if (oldMa.getCurrencyId().equals(newMa.getCurrencyId())) {
					oldMa.setAmount(newMa.getAmount());
					break;
				}
			}
		}
		saveOrUpdateMoneyAmounts(uuid, oldList);
	}

	/**
	 * 取得汇率（先查询moneyAmount表中是否有该币种ID对应的汇率，如果有，则返回；没有则查询currency表，返回其中的公司最低汇率）
	 * 
	 * @param serialNum
	 *            金额的UUID
	 * @param currencyId
	 *            币种ID
	 * @return
	 */
	public BigDecimal getExchangerateByUuid(String serialNum, Integer currencyId) {
		List<MoneyAmount> maList = moneyAmountDao
				.findAmountBySerialNumAndCurrencyId(serialNum, currencyId);
		if (CollectionUtils.isNotEmpty(maList)) {
			return maList.get(0).getExchangerate();
		} else {
			Currency currency = currencyDao.findOne(Long.parseLong(currencyId
					.toString()));
			if (currency != null) {
				return currency.getConvertLowest();
			} else {
				return null;
			}
		}
	}

	/**
	 * 根据reviewId查询对应的moneyAmount记录 add by chy 2015年5月25日12:18:35
	 */
	public List<MoneyAmount> findAmountsByReviewId(Long reviewId) {
		return moneyAmountDao.findAmountsByReviewId(reviewId);
	}
	
	/**
	 * 根据reviewId查询多币种情况下带币种符号的金额。例如：$200.00,     $200.00 + ¥300.00
	 * 根据reviewId查询对应的moneyAmount记录 add by chy 2015年5月25日12:18:35
	 */
	public List<MoneyAmount> findAmountsByReviewUuId(String reviewId) {
		return moneyAmountDao.findAmountsByReviewUuId(reviewId);
	}
	
	/**
	 * 根据reviewId查询多币种情况下带币种符号的金额。例如：$200.00,     $200.00 + ￥ 300.00
	 * 注意：此方法只适用于借款付款 金额的查询
	 * @param reviewId
	 * @author shijun.liu
	 * @return
	 */
	public List<Map<String, String>> getMoneyByReviewId(Integer reviewId){
		StringBuffer str = new StringBuffer();
		str.append("SELECT ")
		   .append(" group_concat(concat(cny.currency_mark, FORMAT(m.amount,2)) SEPARATOR '+') AS mark_money,")
		   .append(" group_concat(concat(cny.currency_name, FORMAT(m.amount,2)) SEPARATOR '+') AS name_money ")
		   .append(" FROM money_amount m LEFT JOIN currency cny ON m.currencyId = cny.currency_id ")
		   .append(" WHERE reviewId = ").append(reviewId)
		   .append(" group by reviewId ");
		return moneyAmountDao.findBySql(str.toString(), Map.class);
	}
	
	/**
	 * 单团类，机票，签证产品
	 * 根据reviewUuid查询多币种情况下带币种符号的金额。例如：$200.00,     $200.00 + ¥300.00
	 * @param reviewUuid	新审批表的uuid(review_new id字段)
	 * @author shijun.liu
	 * @return
	 * @date 	2015.12.01
	 */
	public List<Map<String, String>> getMoneyByReviewUuid(String reviewUuid){
		StringBuffer str = new StringBuffer();
		str.append("SELECT ")
		   .append(" group_concat(concat(cny.currency_mark, FORMAT(m.amount,2)) SEPARATOR '+') AS mark_money,")
		   .append(" group_concat(concat(cny.currency_name, FORMAT(m.amount,2)) SEPARATOR '+') AS name_money ")
		   .append(" FROM money_amount m LEFT JOIN currency cny ON m.currencyId = cny.currency_id ")
		   .append(" WHERE review_uuid = '").append(reviewUuid).append("'")
		   .append(" group by review_uuid ");
		return moneyAmountDao.findBySql(str.toString(), Map.class);
	}
	
	/**
	 * 海岛游的钱查询
	 * @param reviewId
	 * @return
	 */
	public List<Map<String, String>> getMoneyIslandByReviewId(Integer reviewId){
		StringBuffer str = new StringBuffer();
		str.append("SELECT ")
		   .append(" group_concat(concat(cny.currency_mark, FORMAT(m.amount,2)) SEPARATOR '+') AS mark_money,")
		   .append(" group_concat(concat(cny.currency_name, FORMAT(m.amount,2)) SEPARATOR '+') AS name_money ")
		   .append(" FROM island_money_amount m LEFT JOIN currency cny ON m.currencyId = cny.currency_id ")
		   .append(" WHERE reviewId = ").append(reviewId)
		   .append(" group by reviewId ");
		return moneyAmountDao.findBySql(str.toString(), Map.class);
	}
	
	/**
	 * 海岛游产品
	 * 根据reviewUuid查询多币种情况下带币种符号的金额。例如：$200.00,     $200.00 + ¥300.00
	 * @param reviewUuid 	新审批表的uuid(review_new id字段)
	 * @return
	 * @author shijun.liu
	 * @date 	2015.12.01
	 */
	public List<Map<String, String>> getMoneyIslandByReviewUuid(String reviewUuid){
		StringBuffer str = new StringBuffer();
		str.append("SELECT ")
		   .append(" group_concat(concat(cny.currency_mark, FORMAT(m.amount,2)) SEPARATOR '+') AS mark_money,")
		   .append(" group_concat(concat(cny.currency_name, FORMAT(m.amount,2)) SEPARATOR '+') AS name_money ")
		   .append(" FROM island_money_amount m LEFT JOIN currency cny ON m.currencyId = cny.currency_id ")
		   .append(" WHERE review_uuid = '").append(reviewUuid).append("' ")
		   .append(" group by review_uuid ");
		return moneyAmountDao.findBySql(str.toString(), Map.class);
	}
	
	/**
	 * 酒店的钱查询
	 * @param reviewId
	 * @return
	 */
	public List<Map<String, String>> getMoneyHotelByReviewId(Integer reviewId){
		StringBuffer str = new StringBuffer();
		str.append("SELECT ")
		   .append(" group_concat(concat(cny.currency_mark, FORMAT(m.amount,2)) SEPARATOR '+') AS mark_money,")
		   .append(" group_concat(concat(cny.currency_name, FORMAT(m.amount,2)) SEPARATOR '+') AS name_money ")
		   .append(" FROM hotel_money_amount m LEFT JOIN currency cny ON m.currencyId = cny.currency_id ")
		   .append(" WHERE reviewId = ").append(reviewId)
		   .append(" group by reviewId ");
		return moneyAmountDao.findBySql(str.toString(), Map.class);
	}
	
	/**
	 * 酒店产品
	 * 根据reviewUuid查询多币种情况下带币种符号的金额。例如：$200.00,     $200.00 + ¥300.00
	 * @param reviewUuid 	新审批表的uuid(review_new id字段)
	 * @return
	 * @author shijun.liu
	 * @date 	2015.12.01
	 */
	public List<Map<String, String>> getMoneyHotelByReviewUuid(String reviewUuid){
		StringBuffer str = new StringBuffer();
		str.append("SELECT ")
		   .append(" group_concat(concat(cny.currency_mark, FORMAT(m.amount,2)) SEPARATOR '+') AS mark_money,")
		   .append(" group_concat(concat(cny.currency_name, FORMAT(m.amount,2)) SEPARATOR '+') AS name_money ")
		   .append(" FROM hotel_money_amount m LEFT JOIN currency cny ON m.currencyId = cny.currency_id ")
		   .append(" WHERE review_uuid = '").append(reviewUuid).append("' ")
		   .append(" group by review_uuid ");
		return moneyAmountDao.findBySql(str.toString(), Map.class);
	}
	
	/**
	 * 2015-05-25  添加借款记录   moneyamount，不通过serialnum 更新 
	 * @param moneyAmount
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public boolean addMoneyAmount(MoneyAmount moneyAmount){

		try {
			if (moneyAmount == null) {
				return false;
			}
			
			Currency currency = currencyDao.findOne(Long.parseLong(moneyAmount.getCurrencyId().toString()));
			moneyAmount.setExchangerate(currency.getConvertLowest());
			
			moneyAmountDao.save(moneyAmount);
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	/**
	 * 公用接口，根据流水号取金额，对多币种进行拼接
	 * @param serialNum 流水号UUID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Currency> getMoneyNew(String serialNum){
		String sql = "SELECT m.currencyId,c.currency_name,m.amount amount,c.currency_mark from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum +"'";
		List<Map<String, Object>> results = moneyAmountDao.findBySql(sql, Map.class);
		List<Currency> list = new ArrayList<>();
		for(Map<String, Object> mp:results)
		{
			Currency currency = new Currency();
			currency.setCurrencyName((String) mp.get("currency_name"));//币种中文名称
			currency.setCurrencyMark((String) mp.get("currency_mark")); // 币种符号
			currency.setId(Long.parseLong(mp.get("currencyId").toString())); // 币种id
			currency.setConvertCash((BigDecimal) mp.get("amount"));   //  币种金额
			list.add(currency);
		}
		return list;
	}
	
	/**
	 * 查询环球行签证借款的已付金额
	 * @param batchId		批次ID
	 * @param orderType     订单类型
	 * @author shijun.liu
	 * @return
	 */
	public String getTTSQZPayedMoney(Integer batchId, Integer orderType){
		String payedMoney = null;
		StringBuffer str = new StringBuffer();
		str.append("select sum(m.amount) as amount from money_amount m, refund r ")
		   .append(" WHERE r.moneyType = ").append(Refund.MONEY_TYPE_BATCHBORROW)
		   .append(" and r.status is null and r.record_id = ")
		   .append(batchId).append(" and m.serialNum = r.money_serial_num")
		   .append(" and m.orderType = ").append(orderType);
		List<Map<String, Object>> list = moneyAmountDao.findBySql(str.toString(), Map.class);
		if(CollectionUtils.isNotEmpty(list)){
			Object obj = list.get(0).get("amount");
			if(null != obj){
				payedMoney = String.valueOf(obj);
			}
		}
		return payedMoney;
	}

	
	public String getAccountMoney(String orderType, String orderId)
	{
		
		String uuid ="";
		List<Object> ls = null;
		if("1".equals(orderType)||"2".equals(orderType)||"3".equals(orderType)||"4".equals(orderType)||"5".equals(orderType)||"10".equals(orderType))
			ls=moneyAmountDao.findBySql("SELECT p.accounted_money FROM productorder p where p.id="+orderId);
		if("6".equals(orderType))
			ls=moneyAmountDao.findBySql("SELECT vo.accounted_money FROM visa_order vo where vo.id="+orderId);
		if("7".equals(orderType))
			ls=moneyAmountDao.findBySql("SELECT ao.accounted_money FROM airticket_order ao where ao.id="+orderId);
		if("11".equals(orderType))
			ls=moneyAmountDao.findBySql("SELECT ho.accounted_money FROM hotel_order ho where ho.id="+orderId);
		if("12".equals(orderType))
			ls=moneyAmountDao.findBySql("SELECT ioo.accounted_money FROM island_order ioo where ioo.id="+orderId);
		if(ls.get(0) ==null)
			return "";
		else
			uuid	= ls.get(0).toString();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT IFNULL(GROUP_CONCAT(c.currency_mark,FORMAT(mt.amount,2)),'0.00') mtt FROM currency c");
		buffer.append(" RIGHT JOIN (SELECT ma.currencyId,ma.amount FROM money_amount ma WHERE ma.serialNum='");
		buffer.append(uuid);
		buffer.append("')mt  ON c.currency_id = mt.currencyId");
		return moneyAmountDao.findBySql(buffer.toString()).get(0).toString();
	}
	
	/**
	 * 根据订单支付表信息保存moneyAmount（金额表）
		 * @Title: saveMoneyAmountByOrderpayInfo
	     * businessType表示// 业务类型(1表示订单，2表示游客,3表示询价报价,4表示团期)
		 * uid 表示对应业务类型：订单、游客、询价报价或团期主键id
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-10-23 下午8:28:08
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean saveMoneyAmountByOrderpayInfo(Orderpay orderpay, String[] payCurrencyIds, String[] payCurrencyPrice, String[] convertLowests, int businessType , Long uid) {
		//businessType表示// 业务类型(1表示订单，2表示游客,3表示询价报价,4表示团期)
		//uid 表示对应业务类型：订单、游客、询价报价或团期主键id
		boolean flag = false;
		
		if(ArrayUtils.isEmpty(payCurrencyIds) || ArrayUtils.isEmpty(payCurrencyPrice) || (payCurrencyIds.length != payCurrencyPrice.length)) {
			return false;
		}
		
		try{
			List<MoneyAmount> moneyAmounts = new ArrayList<MoneyAmount>();
			
			for(int i=0; i<payCurrencyIds.length; i++) {
				//组装moneyAmount对象
				MoneyAmount moneyAmount = new MoneyAmount();
				moneyAmount.setSerialNum(orderpay.getMoneySerialNum());//金额流水号

				//该币种汇率取自订单中的汇率
				Long currencyId = Long.parseLong(payCurrencyIds[i]);
				/*Currency currency = currencyDao.findById(currencyId);*/
				
				moneyAmount.setCurrencyId(currencyId.intValue());
				moneyAmount.setExchangerate(new BigDecimal(convertLowests[i]));
				moneyAmount.setAmount(new BigDecimal(payCurrencyPrice[i]));//收款金额
				moneyAmount.setUid(uid);//订单ID
				moneyAmount.setMoneyType(orderpay.getPayPriceType());//款项类型
				moneyAmount.setOrderType(orderpay.getOrderType());//订单或产品类型
				moneyAmount.setBusindessType(businessType);//业务类型(1表示订单，2表示游客，4表示团期)
				moneyAmount.setCreatedBy(UserUtils.getUser().getId());//记录人ID
				moneyAmount.setCreateTime(new Date());//记录创建时间
				moneyAmount.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);//删除标识
				moneyAmounts.add(moneyAmount);
			}
			moneyAmountDao.batchSave(moneyAmounts);// 保存支付的moneyAmount信息
			flag = true; /*saveOrUpdateMoneyAmounts(orderpay.getMoneySerialNum(), moneyAmounts);// 保存支付的moneyAmount信息
*/		} catch(Exception e) {
			e.printStackTrace();
			flag = false;
			throw e;
		}
		
		return flag;
	}
	
	/**
	 * 根据团期支付表信息保存moneyAmount（金额表）
	 * @Title: saveMoneyAmountByOrderpayInfo
	 * businessType表示// 业务类型(1表示订单，2表示游客,3表示询价报价,4表示团期)
	 * uid 表示对应业务类型：订单、游客、询价报价或团期主键id
	 * @return boolean
	 * @author majiancheng
	 * @date 2015-10-24 下午6:11:21
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean saveMoneyAmountByPayGroupInfo(PayGroup payGroup, String[] payCurrencyIds, String[] exchangeRates,
												 String[] payCurrencyPrice, int businessType , Long uid) {
		//businessType表示// 业务类型(1表示订单，2表示游客,3表示询价报价,4表示团期)
		//uid 表示对应业务类型：订单、游客、询价报价或团期主键id
		boolean flag = false;
		
		if(ArrayUtils.isEmpty(payCurrencyIds) || ArrayUtils.isEmpty(exchangeRates) || ArrayUtils.isEmpty(payCurrencyPrice)) {
			return flag;
		}else if(payCurrencyIds.length != exchangeRates.length || payCurrencyIds.length != payCurrencyPrice.length){
			return flag;
		}
		
		try{
			List<MoneyAmount> moneyAmounts = new ArrayList<MoneyAmount>();
			for(int i=0; i<payCurrencyIds.length; i++) {
				//组装moneyAmount对象
				MoneyAmount moneyAmount = new MoneyAmount();
				moneyAmount.setSerialNum(payGroup.getPayPrice());//金额流水号
				Long currencyId = Long.parseLong(payCurrencyIds[i]);
				moneyAmount.setCurrencyId(currencyId.intValue());
				moneyAmount.setExchangerate(new BigDecimal(exchangeRates[i]));
				moneyAmount.setAmount(new BigDecimal(payCurrencyPrice[i]));//收款金额
				moneyAmount.setUid(uid);//业务ID
				moneyAmount.setMoneyType(payGroup.getPayPriceType());//款项类型
				moneyAmount.setOrderType(payGroup.getOrderType());//订单或产品类型
				moneyAmount.setBusindessType(businessType);//业务类型(1表示订单，2表示游客，3 表示询价报价，4表示团期)
				moneyAmount.setCreatedBy(UserUtils.getUser().getId());//记录人ID
				moneyAmount.setCreateTime(new Date());//记录创建时间
				moneyAmount.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);//删除标识
				moneyAmounts.add(moneyAmount);
				moneyAmountDao.saveObj(moneyAmount);
			}
			// 此处可以不使用下面的方法   update by shijun.liu 2016.03.11 美途国际其他收入录入
			//flag = saveOrUpdateMoneyAmounts(payGroup.getPayPrice(), moneyAmounts);// 保存支付的moneyAmount信息
		} catch(Exception e) {
			e.printStackTrace();
			flag = false;
			throw e;
		}
		
		return flag;
	}
	
	/**
	 * 取消付款金额->（总达帐金额-本次已付金额），并更新moneyAmount状态
	 * payedMoneySerialNum  本次已付金额
	 * accountedMoneySerialNum 总达帐金额
	 * @Title: cancelMoneyAmount
	 * @return boolean
	 * @author majiancheng
	 * @date 2015-11-4 下午8:58:03
	 */
	public boolean subtractMoneyAmount(String orderMoneySerialNum, String orderpayMoneySerialNum) {
		boolean flag = true;
		//此次支付的金额集合
		List<MoneyAmount> payedMoney = moneyAmountDao.getAmountByUid(orderpayMoneySerialNum);
		
		//转换成key为币种id，value为moneyAmount对象
		Map<String, MoneyAmount> payedMoneyMap = new HashMap<String, MoneyAmount>();
		if(CollectionUtils.isNotEmpty(payedMoney)) {
			for(MoneyAmount moneyAmount : payedMoney) {
				payedMoneyMap.put(String.valueOf(moneyAmount.getCurrencyId()), moneyAmount);
			}
		}
		
		//该订单订单的金额集合
		List<MoneyAmount> accountedMoney = moneyAmountDao.getAmountByUid(orderMoneySerialNum);
		if(CollectionUtils.isNotEmpty(accountedMoney)) {
			for(MoneyAmount moneyAmount : accountedMoney) {
				//达帐金额减去已付金额
				BigDecimal accountedMoneyAmount = moneyAmount.getAmount();
				MoneyAmount payedMoneyEntity = payedMoneyMap.get(moneyAmount.getCurrencyId().toString());
				
				if(payedMoneyEntity == null) {
					continue;
				}
				
				BigDecimal payedMoneyAmount = payedMoneyEntity.getAmount();
				
				moneyAmount.setAmount(accountedMoneyAmount.subtract(payedMoneyAmount));
				moneyAmount.setMoneyType(Context.MONEY_TYPE_CANCEL);
			}
		}
		
		moneyAmountDao.batchUpdate(accountedMoney);
		
		return flag;
	}

	/**
	 *  针对订单收款，进行撤销，驳回操作
	 * @param orderSerialNum       机票订单表对应的uuid，(payed_money, accounted_money)
	 * @param orderpaySerialNum    订单收款表(orderpay)对应的moneySerialnum
	 * @param status				money_amount需要更改为的状态
	 * @return
	 * @author shijun.liu
	 * @date   2016.03.10
	 */
	public boolean subtractMoneyAmount(String orderSerialNum, String orderpaySerialNum, Integer status) {
		boolean flag = false;
		//此次支付的金额集合
		List<MoneyAmount> payRecordMoneyList = moneyAmountDao.getAmountByUid(orderpaySerialNum);
		//该订单订单的金额集合(已付金额或达帐金额)
		List<MoneyAmount> orderMoneyList = moneyAmountDao.getAmountByUid(orderSerialNum);

		if(CollectionUtils.isNotEmpty(payRecordMoneyList) && CollectionUtils.isNotEmpty(orderMoneyList)) {
			MoneyAmount payRecordMoney = payRecordMoneyList.get(0);
			for(MoneyAmount orderMoneyAmount : orderMoneyList) {
				//订单的已收金额或达帐金额
				BigDecimal orderAmount = orderMoneyAmount.getAmount();
				//订单的收款金额
				BigDecimal payAmount = payRecordMoney.getAmount();
				if(null != orderAmount && null != payAmount){
					BigDecimal subAmount = orderAmount.subtract(payAmount);
					orderMoneyAmount.setAmount(subAmount);
					if(null != status){
						orderMoneyAmount.setMoneyType(status);
					}
					moneyAmountDao.saveObj(orderMoneyAmount);
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 针对美途国际订单收款，进行撤销，驳回操作，（注意，此方法只针对美途国际）
	 * @param orderSerialNum		机票订单表对应的uuid，(payed_money, accounted_money)
	 * @param orderpaySerialNum		订单收款表(orderpay)对应的moneySerialnum
	 * @param status				money_amount需要更改为的状态
	 * @return
	 * @author shijun.liu
	 * @date   2016.03.10
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean subtractMoneyAmountForMtour(String orderSerialNum, String orderpaySerialNum, Integer status) {
		boolean flag = false;
		//此次支付的金额集合
		List<MoneyAmount> payRecordMoneyList = moneyAmountDao.getAmountByUid(orderpaySerialNum);
		//该订单订单的金额集合(已付金额或达帐金额)
		List<MoneyAmount> orderMoneyList = moneyAmountDao.getAmountByUid(orderSerialNum);
		if(CollectionUtils.isNotEmpty(payRecordMoneyList) && CollectionUtils.isNotEmpty(orderMoneyList)){
			//支付记录的金额
			MoneyAmount payRecordMoney = payRecordMoneyList.get(0);
			String payUUID = payRecordMoney.getSerialNum();
			for (MoneyAmount orderMoney : orderMoneyList){
				if(payUUID.equals(orderMoney.getPayedAccountedUUID())){
					//订单的已收金额或达帐金额
					BigDecimal orderAmount = orderMoney.getAmount();
					//排除历史残留数据
					if(orderAmount.compareTo(BigDecimal.ZERO) > 0){
						//订单的收款金额
						BigDecimal payAmount = payRecordMoney.getAmount();
						if(null != orderAmount && null != payAmount){
							BigDecimal subAmount = orderAmount.subtract(payAmount);
							orderMoney.setAmount(subAmount);
							if(null != status){
								orderMoney.setMoneyType(status);
							}
							moneyAmountDao.saveObj(orderMoney);
							flag = true;
						}
					}else{
						orderMoney.setAmount(new BigDecimal(0));
						if(null != status){
							orderMoney.setMoneyType(status);
						}
						moneyAmountDao.saveObj(orderMoney);
						flag = true;
					}
				}else if(StringUtils.isBlank(orderMoney.getPayedAccountedUUID())) {
					// 0094汇率可修改之前的历史数据（之前是相同币种的金要相加）
					if(orderMoney.getCurrencyId().intValue() == payRecordMoney.getCurrencyId().intValue()){
						//订单的已收金额或达帐金额
						BigDecimal orderAmount = orderMoney.getAmount();
						//订单的收款金额
						BigDecimal payAmount = payRecordMoney.getAmount();
						if(null != orderAmount && null != payAmount){
							BigDecimal subAmount = orderAmount.subtract(payAmount);
							orderMoney.setAmount(subAmount);
							if(null != status){
								orderMoney.setMoneyType(status);
							}
							moneyAmountDao.saveObj(orderMoney);
							flag = true;
						}
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * 添加付款金额->(订单收款时使用)
	 * @Title: addPayedMoneyAmount
	 * @return boolean
	 * @author majiancheng
	 * @date 2015-11-4 下午10:20:05
	 */
	public boolean addPayedMoneyAmount(String serialNum, String orderPayMoneySerialNum,
									   Integer orderId, Integer orderType, String[] moneys, String[] currencyIds, String[] convertLowests) {
		boolean flag = true;
		if(StringUtils.isEmpty(serialNum) || orderId==null || orderType==null || moneys==null || currencyIds == null || (moneys.length != currencyIds.length)) {
			return false;
		}
		List<MoneyAmount> payedMoneyAmounts = moneyAmountDao.getAmountByUid(serialNum);
		//新建付款金额,因为美途国际(除华尔远航)每次收款的汇率可能改变，所以美途国际账号的时候也是新建付款金额。update by shijun.liu 0094需求
		if(CollectionUtils.isEmpty(payedMoneyAmounts) || UserUtils.isMtourUser()) {
			payedMoneyAmounts = new ArrayList<MoneyAmount>();
			for(int i=0; i<moneys.length; i++) {
				MoneyAmount moneyAmount = new MoneyAmount();
				moneyAmount.setSerialNum(serialNum);
				
				//汇率取自创建订单是的币种汇率信息
				Long currencyId = Long.parseLong(currencyIds[i]);
				/*Currency currency = currencyDao.findById(currencyId);*/
				
				moneyAmount.setCurrencyId(currencyId.intValue());
				//设置汇率
				moneyAmount.setExchangerate(new BigDecimal(convertLowests[i]));
				moneyAmount.setAmount(new BigDecimal(moneys[i]));
				moneyAmount.setUid(orderId.longValue());//业务ID
				moneyAmount.setMoneyType(Context.MONEY_TYPE_13);
				moneyAmount.setOrderType(orderType);
				moneyAmount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
				moneyAmount.setCreatedBy(UserUtils.getUser().getId());
				moneyAmount.setCreateTime(new Date());
				moneyAmount.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				if(StringUtils.isNotBlank(orderPayMoneySerialNum)){
					moneyAmount.setPayedAccountedUUID(orderPayMoneySerialNum);
				}
				payedMoneyAmounts.add(moneyAmount);
			}
			moneyAmountDao.batchSave(payedMoneyAmounts);
		//在原有付款金额上新增金额
		} else {
			
			//重构币种及金额信息；key为币种id，value为金额信息
			Map<String, MoneyAmount> moneyInfoMap = new HashMap<String, MoneyAmount>();
			
			for(MoneyAmount moneyAmount : payedMoneyAmounts) {
				String currencyId = moneyAmount.getCurrencyId().toString();
				if(moneyInfoMap.get(currencyId) == null) {
					moneyInfoMap.put(currencyId, moneyAmount);
				} else {
					//原有金额和现有金额进行添加
					MoneyAmount earlyMoneyAmount = moneyInfoMap.get(currencyId);
					earlyMoneyAmount.setAmount(earlyMoneyAmount.getAmount().add(moneyAmount.getAmount()));
				}
			}
			
			for(int i=0; i<moneys.length; i++) {
				String currencyId = currencyIds[i];
				String money = moneys[i];
				
				MoneyAmount entity = moneyInfoMap.get(currencyId);
				if(entity != null) {
					entity.setAmount(entity.getAmount().add(new BigDecimal(money)));
					
				//假如币种不存在则添加moneyAmount
				} else {
					MoneyAmount moneyAmount = new MoneyAmount();
					moneyAmount.setSerialNum(serialNum);
					
					//汇率取自创建订单是的币种汇率信息
					/*Currency currency = currencyDao.findById(Long.parseLong(currencyId));*/
					
					moneyAmount.setCurrencyId(Integer.parseInt(currencyId));
					//设置汇率
					moneyAmount.setExchangerate(new BigDecimal(convertLowests[i]));
					moneyAmount.setAmount(new BigDecimal(money));
					moneyAmount.setUid(orderId.longValue());//业务ID
					moneyAmount.setMoneyType(Context.MONEY_TYPE_13);
					moneyAmount.setOrderType(orderType);
					moneyAmount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
					moneyAmount.setCreatedBy(UserUtils.getUser().getId());
					moneyAmount.setCreateTime(new Date());
					moneyAmount.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
					payedMoneyAmounts.add(moneyAmount);
				}
			}
			
			moneyAmountDao.batchSave(payedMoneyAmounts);
			
		}
		
		return flag;
	}

	public  List<Map<Object, Object>> getMoneyIdList()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id  FROM currency c LEFT JOIN sys_office so  ON c.create_company_id = so.id ");
		buffer.append(" WHERE so.uuid='7a81b21a77a811e5bc1e000c29cf2586' AND c.currency_name LIKE '%人民币%'");
	   return 	moneyAmountDao.findBySql(buffer.toString(), Map.class);
		
	}

	public List<Map<String, Object>> getYuJiFanYong(String orderId)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT  GROUP_CONCAT(totalMoney SEPARATOR '+') yujifanyong FROM ( ");
		buffer.append("SELECT  CONCAT(tmp.mark,SUM(tmp.amount))  totalMoney ");
		buffer.append("FROM (SELECT ma.currencyId,ma.amount,c.currency_mark mark FROM visa_order vo ");
		buffer.append("LEFT JOIN money_amount ma ON vo.groupRebate =ma.serialNum ");
		buffer.append("LEFT JOIN currency c ON ma.currencyId= c.currency_id ");
		buffer.append("WHERE vo.id=");
		buffer.append(orderId);
		buffer.append(" AND ma.amount !=0.00 ");
		buffer.append("UNION ALL ");
		buffer.append("SELECT ma.currencyId, ma.amount,c.currency_mark mark FROM traveler t LEFT JOIN  visa_order vo ON t.orderId = vo.id ");
		buffer.append("LEFT JOIN  money_amount ma ON t.rebates_moneySerialNum = ma.serialNum ");
		buffer.append("LEFT JOIN currency c ON ma.currencyId= c.currency_id ");
		buffer.append("WHERE vo.id=");
		buffer.append(orderId);
		//处理bug 13516：签证订单 要对 签证类型进行过滤  添加   AND t.order_type = 6
		buffer.append(" AND ma.amount IS NOT NULL  AND t.order_type = 6  AND ma.amount !=0.00) tmp  LEFT JOIN currency c ON tmp.currencyId = c.currency_id  ");
		buffer.append("GROUP BY tmp.currencyId) tmp2");
		return moneyAmountDao.findBySql(buffer.toString());
	}

	/**
	 * @param orderId
	 * @return
	 */
	public String getShiJiFanYong(String orderId)
	{
		//新实际返佣      $154.00+₩44.00+€33.00
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT AHV.TEXT_ FROM  ACT_HI_VARINST AHV WHERE AHV.PROC_INST_ID_ IN  ");
		buffer.append("(SELECT rn.proc_inst_id FROM review_new rn WHERE rn.order_id=");
		buffer.append(orderId);
		buffer.append("  AND rn.product_type='6' and rn.status=2 ");
		buffer.append("AND rn.process_type='9' )   AND   AHV.NAME_ ='markTotalMoney'");
		List<?> newFanYong =  moneyAmountDao.findBySql(buffer.toString());
		//List<Map<String, String>> listNew = new ArrayList<Map<String, String>>();
		StringBuffer buffer2 = new StringBuffer();
		buffer2.append("SELECT  rd.myValue  FROM review_detail rd WHERE rd.review_id  ");
		buffer2.append("IN (SELECT id FROM review r  WHERE r.orderId=");
		buffer2.append(orderId);
		buffer2.append(" AND r.flowType=9 and r.status=2 )   ");
		buffer2.append("AND rd.myKey='totalRebatesJe'");
		//旧实际返佣      {'97':'200','95':'400','100':'400'}   {'97':'200','95':'620','100':'444'}
		List<Map<String, Object>> oldFanYong =  moneyAmountDao.findBySql(buffer2.toString(), Map.class);
		//新返佣记录汇总值
		Map<String, String> mmp_new = new HashMap<>();
		
		//旧返佣记录汇总值
		Map<String, String> mmp_old = new HashMap<>();
		
		if(newFanYong.size()==0 && oldFanYong.size() == 0)
			return "";
		else
		{
			if(newFanYong.size() > 0)
			{
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				for(int x=0;x<newFanYong.size();x++)
				{
					String data =newFanYong.get(x).toString().replace(",", "");
					String[] newData =data.split("\\+");
					for (int i = 0; i < newData.length; i++)
					{
						Pattern p = Pattern.compile("(\\D+)[^\\.]");
						Matcher m = p.matcher(newData[i]);
						if (m.find())
						{
							Map<String, String> map = new HashMap<String, String>();
							map.put(m.group(1), newData[i].replace(m.group(1), ""));
							list.add(map);
						}
					}
				}
				// [{$=2000.00}, {$=600.00}, {$=100.00}, {$=1000.00}]
				for(Map<String, String> mp:list)
				{
					for (String key : mp.keySet()) {
							if(mmp_new.get(key) == null)
								mmp_new.put(key, mp.get(key));
							else
								mmp_new.put(key,  String.valueOf(Float.valueOf(mp.get(key)) +Float.valueOf(mmp_new.get(key))));
						  }
				}
			}
			
			if(oldFanYong.size()>0)
			{
				List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
				for(int y=0;y<oldFanYong.size();y++)
				{
				  Map<String, Object> data  = new HashMap<String, Object>();
				  JSONObject jsonObject = JSONObject.fromObject(oldFanYong.get(y).get("myValue"));  
			       Iterator<?> it = jsonObject.keys();  
			       // 遍历jsonObject数据，添加到Map对象  
			       while (it.hasNext())  
			       {  
			           String key = String.valueOf(it.next());  
			           String value = (String) jsonObject.get(key);  
			           data.put(key, value);  
			       }  
					
			       for (Map.Entry<String, Object> entry : data.entrySet()) {  
			    	   
			    	   // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
			    	    List<?> ls= moneyAmountDao.findBySql("SELECT c.currency_mark FROM currency c WHERE c.currency_id ="+ entry.getKey());
			    	    Map<String, String> mp  = new HashMap<String, String>();
			    	    mp.put(ls.get(0).toString(), entry.getValue().toString());
						list2.add(mp);
			    	}  
				}
				
				
				for(Map<String, String> mp:list2)
				{
					for (String key : mp.keySet()) {
							if(mmp_old.get(key) == null)
								mmp_old.put(key, mp.get(key));
							else
								mmp_old.put(key,  String.valueOf(Float.valueOf(mp.get(key)) +Float.valueOf(mmp_old.get(key))));
						  }
				}
			}
			
			
			
		}
		Set<String> totalSet = new HashSet<String>();
		Set<String>  mmp_new_set = mmp_new.keySet();
		Set<String> mmp_old_set = mmp_old.keySet();
		totalSet.addAll(mmp_new_set);
		totalSet.addAll(mmp_old_set);
		int flag =0;
		String xx = "";
		for (String str : totalSet) {  
			if(flag == totalSet.size()-1 )
			{
				float f=0;
				if(null != mmp_new.get(str))
					f= f+	Float.valueOf(mmp_new.get(str));
					if(null != mmp_old.get(str))	
					{
						f= f+	Float.valueOf(mmp_old.get(str));	
					}
					xx =str+fmtMicrometer(String.valueOf(f));
				    
			}
			else
			{
				float f=0;
				if(null != mmp_new.get(str))
					f= f+	Float.valueOf(mmp_new.get(str));
					if(null != mmp_old.get(str))	
					{
						f= f+	Float.valueOf(mmp_old.get(str));
						 xx =xx+fmtMicrometer(String.valueOf(f))+"+";	
					}
					xx =xx+"+"+str+fmtMicrometer(String.valueOf(f));
			}
		}
		if(xx.startsWith("+"))
		 return xx.substring(1);
		else 
			return xx;
	}
	
	
	
	/**
	 * 金额格式转化
	 * copy from visaBorrowMoneyController
	 * @param text
	 * @return
	 */
	private String fmtMicrometer(String text){
		DecimalFormat df = null;
		df = new DecimalFormat("###,##0.00");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	/**
	 * 
	 * @param totalMoneySerilNum
	 * @param quauqServiceCharge
	 * @return
	 */
	public String getMoneyNameStr4Quauq(String totalMoneySerilNum, String quauqServiceCharge) {
		if (StringUtils.isBlank(totalMoneySerilNum)) {
			return null;
		}
		List<MoneyAmount> totalAmounts = moneyAmountDao.getAmountByUid(totalMoneySerilNum);
		List<MoneyAmount> chargeAmounts = new ArrayList<>();
		if (StringUtils.isNotBlank(quauqServiceCharge)) {			
			chargeAmounts = moneyAmountDao.getAmountByUid(quauqServiceCharge);
		}
		// 总结算价 = 总额 - 服务费
		for (MoneyAmount chargeMoney : chargeAmounts) {
			for (MoneyAmount totalMoney : totalAmounts) {
				if (chargeMoney.getCurrencyId() == totalMoney.getCurrencyId()) {
					totalMoney.setAmount(totalMoney.getAmount().subtract(chargeMoney.getAmount()));
					break;
				}
			}
		}
		
		String money = "";
		if (CollectionUtils.isNotEmpty(totalAmounts)) {
			for (int i = 0; i < totalAmounts.size(); i++) {
				MoneyAmount clearMoney = totalAmounts.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				Currency currency = currencyDao.findById(Long.parseLong(clearMoney.getCurrencyId().toString()));
				if (i == totalAmounts.size() - 1) {
					if (null != clearMoney.getAmount()) {
						money += currency.getCurrencyName() + d.format(clearMoney.getAmount());
					}
				} else {
					if (null != clearMoney.getAmount()) {
						money += currency.getCurrencyName() + d.format(clearMoney.getAmount()) + " + ";
					}
				}
			}
		} else {
			money = "人民币0.00";
		}

		return money;
		
	}

	/**
	 * moneyAmountList加、减法(对应币种)
	 * @param money1
	 * @param money2
	 * @return
	 */
	public List<MoneyAmount> calculation4MoneyAmountList(Integer calculation, List<MoneyAmount> moneyList1, List<MoneyAmount> moneyList2) {
		// 判空
		if (calculation == null || CollectionUtils.isEmpty(moneyList1) || CollectionUtils.isEmpty(moneyList2)) {
			return null;
		}
		// 获取参与计算所有币种
		Set<Integer> currencyIds = new HashSet<>();
		for (MoneyAmount money1 : moneyList1) {
			currencyIds.add(money1.getCurrencyId());
		}
		for (MoneyAmount money2 : moneyList2) {
			currencyIds.add(money2.getCurrencyId());			
		}
		// 返回结果
		List<MoneyAmount> resultAmounts = new ArrayList<>();
		// 初始化：使用纯粹的金额、币种的计算，不存储其他信息
		for (Integer currencyId : currencyIds) {
			MoneyAmount moneyAmount = new MoneyAmount();
			moneyAmount.setAmount(BigDecimal.ZERO);
			moneyAmount.setCurrencyId(currencyId);
			Currency tempCurrency = currencyDao.findOne(Long.parseLong(currencyId.toString()));
			moneyAmount.setExchangerate(tempCurrency.getConvertLowest());
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			moneyAmount.setCreateTime(new Date());
			moneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
			resultAmounts.add(moneyAmount);
		}
		// 加上 money1
		for (MoneyAmount result : resultAmounts) {
			for (MoneyAmount money1 : moneyList1) {
				if (result.getCurrencyId().intValue() == money1.getCurrencyId().intValue()) {
					result.setAmount(result.getAmount().add(money1.getAmount()));
					break;
				}
			}
		}
		// 区分计算方法，与 money2进行计算
		for (MoneyAmount result : resultAmounts) {
			for (MoneyAmount money2 : moneyList2) {
				if (result.getCurrencyId().intValue() == money2.getCurrencyId().intValue()) {
					BigDecimal amount = money2.getAmount();
					if (amount == null) {
						amount = new BigDecimal(0);
					}
					if (Context.ADD.intValue() == calculation.intValue()) {						
						result.setAmount(result.getAmount().add(amount));
						break;
					} else if (Context.SUBTRACT.intValue() == calculation.intValue()) {
						result.setAmount(result.getAmount().subtract(amount));
						break;
					} else {
						// TODO
					}
				}
			}
		}
		return resultAmounts;
	}

	/**
	 * 获取金额字符串
	 * @param newTotal
	 * @return
	 */
	public String getMoneyStrFromAmountList(String nameOrMark, List<MoneyAmount> moneyAmountList) {
		// 判空
		if (StringUtils.isBlank(nameOrMark) || CollectionUtils.isEmpty(moneyAmountList)) {
			return null;
		}
		// 获取所有币种
		Set<Integer> currencyIdSet = new HashSet<>();
		for (MoneyAmount moneyAmount : moneyAmountList) {
			currencyIdSet.add(moneyAmount.getCurrencyId());
		}
		List<Currency> allCurrencies = new ArrayList<>();
		for (Integer currencyId : currencyIdSet) {
			allCurrencies.add(currencyDao.findOne(Long.parseLong(currencyId.toString())));
		}
		// 遍历list，show + 金额 
		String tempString = "";
		DecimalFormat df = new DecimalFormat(",##0.00");
		for (MoneyAmount moneyAmount : moneyAmountList) {			
			for (Currency currency : allCurrencies) {
				if (moneyAmount.getCurrencyId().toString().equals(currency.getId().toString())) {
					if (StringUtils.isNotBlank(tempString)) {
						tempString += " + ";
					}
					if ("name".equals(nameOrMark)) {
						tempString += currency.getCurrencyName() + df.format(moneyAmount.getAmount());
					} else if ("mark".equals(nameOrMark)) {
						tempString += currency.getCurrencyMark() + df.format(moneyAmount.getAmount());						
					} else {
						// TODO
					}
					break;
				}
			}
		}
		return tempString;
	}

	/**
	 * 同币种相加处理
	 * @param payPriceSum
	 * @return
	 */
	public List<MoneyAmount> sameCurrencySum(List<MoneyAmount> moneyAmountList) {
		// 判空
		if (CollectionUtils.isEmpty(moneyAmountList)) {
			return null;
		}
		// 获取所有币种
		Set<Integer> currencyIdSet = new HashSet<>();
		for (MoneyAmount moneyAmount : moneyAmountList) {
			currencyIdSet.add(moneyAmount.getCurrencyId());
		}
		List<Currency> allCurrencies = new ArrayList<>();
		for (Integer currencyId : currencyIdSet) {
			allCurrencies.add(currencyDao.findOne(Long.parseLong(currencyId.toString())));
		}
		// 返回结果
		List<MoneyAmount> resultAmounts = new ArrayList<>();
		// 初始化：使用纯粹的金额、币种的计算，不存储其他信息
		for (Currency tempCurrency : allCurrencies) {
			MoneyAmount moneyAmount = new MoneyAmount();
			moneyAmount.setAmount(BigDecimal.ZERO);
			moneyAmount.setCurrencyId(Integer.parseInt(tempCurrency.getId().toString()));
			moneyAmount.setExchangerate(tempCurrency.getConvertLowest());
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			moneyAmount.setCreateTime(new Date());
			moneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
			resultAmounts.add(moneyAmount);
		}
		// 加上 money
		for (MoneyAmount result : resultAmounts) {
			for (MoneyAmount money : moneyAmountList) {
				if (result.getCurrencyId().intValue() == money.getCurrencyId().intValue()) {
					result.setSerialNum(money.getSerialNum());
					result.setUid(money.getUid());
					result.setMoneyType(money.getMoneyType());
					result.setOrderType(money.getOrderType());
					result.setBusindessType(money.getBusindessType());
					result.setAmount(result.getAmount().add(money.getAmount()));
				}
			}
		}
		return resultAmounts;
	}

	/**
	 * 通过序列号，查询出该序列号下对应的MoneyAmount，根据币种进行分组求和。获取币种id的集合、对应的求和金额集合
	 * 和带币种符号的求和金额拼接字符串。
	 * @param serialNum 序列号
	 * @return String数组：0：对应币种id；1：和币种id下标相对应的求和金额；2：带币种符号的求和金额拼接字符串。
     */
	public String[] getCurrencyAndMoneySum(String serialNum){
		List<Map<String,Object>> result = moneyAmountDao.getCurrencyAndMoneySum(serialNum);
		if (CollectionUtils.isEmpty(result)){
			return new String[]{"","",""};
		}

		StringBuilder currencyIds = new StringBuilder();
		StringBuilder amounts = new StringBuilder();
		StringBuilder currencyMoney = new StringBuilder();
		DecimalFormat format = new DecimalFormat("###,##0.00");

		int maxIndex = result.size() - 1; // List里数组的最大下标，从0开始

		for (int i = 0; i < maxIndex; i++) {
			Map<String,Object> map = result.get(i);
			currencyMoney.append(map.get("currencyMark")).append(format.format(map.get("moneySum"))).append("+");
			currencyIds.append(map.get("currencyId")).append(",");
			amounts.append(map.get("moneySum")).append(",");
		}

		// 获取最后一组数值，主要是为了去除分隔号
		Map<String,Object> map = result.get(maxIndex);
		currencyMoney.append(map.get("currencyMark")).append(format.format(map.get("moneySum")));
		currencyIds.append(map.get("currencyId"));
		amounts.append(map.get("moneySum"));

		return new String[]{currencyIds.toString(),amounts.toString(),currencyMoney.toString()};
	}
	
}
