package com.trekiz.admin.review.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.review.money.repository.NewProcessMoneyAmountDao;

public class ReviewMoneyAmountUtils {
	
	private static NewProcessMoneyAmountDao newAmountDao = SpringContextHolder.getBean(NewProcessMoneyAmountDao.class);	
	
	/**
	 * 从review_process_money_amount表中查询，多币种展示
	 * 
	 * @param serialNum  序列号
	 * @param showType 币种显示效果：name：币种中文名称  mark：币种符号
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public static String getReviewMoneyStrByUUID(String serialNum, String showType) {
		String selectName = "c.currency_mark";
		String tableName = "review_process_money_amount";
		if ("name".equals(showType)) {
			selectName = "c.currency_name";
		} else if ("mark".equals(showType)) {
			selectName = "c.currency_mark";
		}		
		String sql = "SELECT " + selectName + ", sum(m.amount) from " +tableName+" m, currency c "
				+ "where m.currencyId = c.currency_id and m.serial_number = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		
		List<Object[]> results = newAmountDao.getSession().createSQLQuery(sql).list();
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
	 * 订单成人价、儿童价、特殊人群价格、订单总价
	 * 
	 * @param serialNum
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public static String getReviewMoneyStrByUUID(String serialNum) {
		String selectName = "c.currency_mark";
		String tableName = "review_process_money_amount";		
		String sql = "SELECT " + selectName + ", sum(m.amount) from " +tableName+" m, currency c "
				+ "where m.currencyId = c.currency_id and m.id = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = newAmountDao.getSession().createSQLQuery(sql).list();
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
	

}
