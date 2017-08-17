package com.trekiz.admin.review.money.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.repository.NewProcessMoneyAmountDao;

/**
 * 
 * @author songyang by 2015年11月5日21:00:42
 * 
 *         用于activity审核流程对于金额业务的数据操作
 *
 */

@Service
@Transactional(readOnly = true)
public class NewProcessMoneyAmountService extends BaseService {

	@Autowired
	private NewProcessMoneyAmountDao newProcessMoneyAmountDao;

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void saveNewProcessMoneyAmount(NewProcessMoneyAmount newProcessMoneyAmount) {

		newProcessMoneyAmountDao.save(newProcessMoneyAmount);

	}

	@Transactional(readOnly = true, rollbackFor = { Exception.class })
	public NewProcessMoneyAmount findByReviewId(String reviewId) {
		NewProcessMoneyAmount newProcessMoneyAmount = newProcessMoneyAmountDao.findByReviewId(reviewId);
		return newProcessMoneyAmount;

	}
	
	@Transactional(readOnly = true, rollbackFor = { Exception.class })
	public List<NewProcessMoneyAmount> findListByReviewId(String reviewId) {
		List<NewProcessMoneyAmount> newProcessMoneyAmountList = newProcessMoneyAmountDao.findListByReviewId(reviewId);
		return newProcessMoneyAmountList;
	}

	/**
	 * 从review_process_money_amount表中查询，多币种展示
	 * @author jyang
	 * @param serialNum  序列号
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoneyStr(String serialNum) {
		String sql = "SELECT c.currency_mark, sum(m.amount) from review_process_money_amount m,currency c where m.currencyId=c.currency_id and m.serial_number = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = newProcessMoneyAmountDao.getSession().createSQLQuery(sql).list();

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
