package com.trekiz.admin.agentToOffice.T1.money.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trekiz.admin.agentToOffice.T1.money.entity.T1MoneyAmount;
import com.trekiz.admin.agentToOffice.T1.money.repository.T1MoneyAmountDao;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.modules.money.service.MoneyAmountService;

/**
 * t1金额service
 * @author yakun.bai
 * @Date 2016-10-12
 */
@Service
@Transactional(readOnly = true)
public class T1MoneyAmountService extends BaseService {

	@Autowired
	private T1MoneyAmountDao moneyAmountDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Transactional(readOnly = true)
	public List<T1MoneyAmount> findBySerialNum(String serialNum) {
		return moneyAmountDao.findAmountListBySerialNum(serialNum);
	}
	
	@Transactional(readOnly = false, rollbackFor = {ServiceException.class })
	public void save(T1MoneyAmount amount) {
		if (null != amount) {
			moneyAmountDao.save(amount);
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = {ServiceException.class })
	public void saveOrUpdateMoneyAmounts(List<T1MoneyAmount> moneyAmountList) {
		if (CollectionUtils.isNotEmpty(moneyAmountList)) {
			for (T1MoneyAmount moneyAmount : moneyAmountList) {
				if (null == moneyAmount) {
					continue;
				} else {
					moneyAmountDao.save(moneyAmount);
				}
			}
		}
	}
	
	public List<T1MoneyAmount> mergeMoneyAmount(List<T1MoneyAmount> moneyAmountList) {
		List<T1MoneyAmount> tempList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(moneyAmountList)) {
			Set<Integer> tempSet = Sets.newHashSet();
			for (T1MoneyAmount moneyAmount : moneyAmountList) {
				if (null == moneyAmount) {
					continue;
				} else {
					if (tempSet.contains(moneyAmount.getCurrencyId())) {
						for (T1MoneyAmount amount : tempList) {
							if (amount.getCurrencyId().intValue() == moneyAmount.getCurrencyId().intValue()) {
								amount.setAmount(amount.getAmount().add(moneyAmount.getAmount()));
							}
						}
					} else {
						tempList.add(moneyAmount);
						tempSet.add(moneyAmount.getCurrencyId());
					}
				}
			}
		}
		return tempList;
	}
	
	/**
	 * 获取单个游客类型价格总和
	 * @param price
	 * @param num
	 * @param currencyMark
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-20
	 */
	public String getSumMoneyStr(BigDecimal price, Integer num) {
		if (null != price && null != num) {
			DecimalFormat d = new DecimalFormat("##0.00");
			return d.format(price.multiply(new BigDecimal(num)));
		} else {
			return "0";
		}
	}
	
	/**
	 * 获取此单利润：实际结算价-系统结算价
	 * @param adultSum
	 * @param companyAdultSum
	 * @param childSum
	 * @param companyChildSum
	 * @param specialSum
	 * @param companySpecialSum
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-20
	 */
	public String getProfitsStr(String adultSum, String companyAdultSum, 
			String childSum, String companyChildSum, String specialSum, String companySpecialSum) {
		
		BigDecimal adultProfits = new BigDecimal(adultSum).subtract(new BigDecimal(companyAdultSum));
		BigDecimal childProfits = new BigDecimal(childSum).subtract(new BigDecimal(companyChildSum));
		BigDecimal specialProfits = new BigDecimal(specialSum).subtract(new BigDecimal(companySpecialSum));
		
		BigDecimal profits = adultProfits.add(childProfits).add(specialProfits);
		
		return profits.toString();
	}
	
}