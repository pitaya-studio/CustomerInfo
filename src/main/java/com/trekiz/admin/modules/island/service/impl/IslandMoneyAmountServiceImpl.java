/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.island.dao.IslandMoneyAmountDao;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.input.IslandMoneyAmountInput;
import com.trekiz.admin.modules.island.query.IslandMoneyAmountQuery;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class IslandMoneyAmountServiceImpl  extends BaseService implements IslandMoneyAmountService{
	@Autowired
	private IslandMoneyAmountDao islandMoneyAmountDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private IslandOrderDao islandOrderDao;

	public void save (IslandMoneyAmount islandMoneyAmount){
		super.setOptInfo(islandMoneyAmount, BaseService.OPERATION_ADD);
		islandMoneyAmountDao.saveObj(islandMoneyAmount);
	}
	
	public void save (IslandMoneyAmountInput islandMoneyAmountInput){
		IslandMoneyAmount islandMoneyAmount = islandMoneyAmountInput.getIslandMoneyAmount();
		super.setOptInfo(islandMoneyAmount, BaseService.OPERATION_ADD);
		islandMoneyAmountDao.saveObj(islandMoneyAmount);
	}
	
	public void update (IslandMoneyAmount islandMoneyAmount){
		super.setOptInfo(islandMoneyAmount, BaseService.OPERATION_UPDATE);
		islandMoneyAmountDao.updateObj(islandMoneyAmount);
	}
	
	public IslandMoneyAmount getById(java.lang.Long value) {
		return islandMoneyAmountDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		IslandMoneyAmount obj = islandMoneyAmountDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<IslandMoneyAmount> find(Page<IslandMoneyAmount> page, IslandMoneyAmountQuery islandMoneyAmountQuery) {
		DetachedCriteria dc = islandMoneyAmountDao.createDetachedCriteria();
		
	   	if(islandMoneyAmountQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandMoneyAmountQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandMoneyAmountQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandMoneyAmountQuery.getUuid()));
		}
	   	if(islandMoneyAmountQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", islandMoneyAmountQuery.getCurrencyId()));
	   	}
	   	if(islandMoneyAmountQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", islandMoneyAmountQuery.getAmount()));
	   	}
	   	if(islandMoneyAmountQuery.getExchangerate()!=null){
	   		dc.add(Restrictions.eq("exchangerate", islandMoneyAmountQuery.getExchangerate()));
	   	}
		if (StringUtils.isNotEmpty(islandMoneyAmountQuery.getBusinessUuid())){
			dc.add(Restrictions.eq("uid", islandMoneyAmountQuery.getBusinessUuid()));
		}
	   	if(islandMoneyAmountQuery.getMoneyType()!=null){
	   		dc.add(Restrictions.eq("moneyType", islandMoneyAmountQuery.getMoneyType()));
	   	}
	   	if(islandMoneyAmountQuery.getBusinessType()!=null){
	   		dc.add(Restrictions.eq("businessType", islandMoneyAmountQuery.getBusinessType()));
	   	}
	   	if(islandMoneyAmountQuery.getReviewId()!=null){
	   		dc.add(Restrictions.eq("reviewId", islandMoneyAmountQuery.getReviewId()));
	   	}
	   	if(StringUtils.isNotEmpty(islandMoneyAmountQuery.getSerialNum())){
	   		dc.add(Restrictions.eq("serialNum", islandMoneyAmountQuery.getSerialNum()));
	   	}
	   	if(islandMoneyAmountQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandMoneyAmountQuery.getCreateBy()));
	   	}
		if(islandMoneyAmountQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandMoneyAmountQuery.getCreateDate()));
		}
	   	if(islandMoneyAmountQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandMoneyAmountQuery.getUpdateBy()));
	   	}
		if(islandMoneyAmountQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandMoneyAmountQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandMoneyAmountQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandMoneyAmountQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandMoneyAmountDao.find(page, dc);
	}
	
	public List<IslandMoneyAmount> find( IslandMoneyAmountQuery islandMoneyAmountQuery) {
		DetachedCriteria dc = islandMoneyAmountDao.createDetachedCriteria();
		
	   	if(islandMoneyAmountQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandMoneyAmountQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandMoneyAmountQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandMoneyAmountQuery.getUuid()));
		}
	   	if(islandMoneyAmountQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", islandMoneyAmountQuery.getCurrencyId()));
	   	}
	   	if(islandMoneyAmountQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", islandMoneyAmountQuery.getAmount()));
	   	}
	   	if(islandMoneyAmountQuery.getExchangerate()!=null){
	   		dc.add(Restrictions.eq("exchangerate", islandMoneyAmountQuery.getExchangerate()));
	   	}
		if (StringUtils.isNotEmpty(islandMoneyAmountQuery.getBusinessUuid())){
			dc.add(Restrictions.eq("businessUuid", islandMoneyAmountQuery.getBusinessUuid()));
		}
	   	if(islandMoneyAmountQuery.getMoneyType()!=null){
	   		dc.add(Restrictions.eq("moneyType", islandMoneyAmountQuery.getMoneyType()));
	   	}
	   	if(islandMoneyAmountQuery.getBusinessType()!=null){
	   		dc.add(Restrictions.eq("businessType", islandMoneyAmountQuery.getBusinessType()));
	   	}
	   	if(islandMoneyAmountQuery.getReviewId()!=null){
	   		dc.add(Restrictions.eq("reviewId", islandMoneyAmountQuery.getReviewId()));
	   	}
	   	if(StringUtils.isNotEmpty(islandMoneyAmountQuery.getSerialNum())){
	   		dc.add(Restrictions.eq("serialNum", islandMoneyAmountQuery.getSerialNum()));
	   	}
	   	if(islandMoneyAmountQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandMoneyAmountQuery.getCreateBy()));
	   	}
		if(islandMoneyAmountQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandMoneyAmountQuery.getCreateDate()));
		}
	   	if(islandMoneyAmountQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandMoneyAmountQuery.getUpdateBy()));
	   	}
		if(islandMoneyAmountQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandMoneyAmountQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandMoneyAmountQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandMoneyAmountQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandMoneyAmountDao.find(dc);
	}
	
	public IslandMoneyAmount getByUuid(String uuid) {
		return islandMoneyAmountDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		IslandMoneyAmount islandMoneyAmount = getByUuid(uuid);
		islandMoneyAmount.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(islandMoneyAmount);
	}
	
	public void saveOrUpdate(IslandMoneyAmount islandMoneyAmount) {
		if(StringUtils.isNotEmpty(islandMoneyAmount.getUuid())) {
			update(islandMoneyAmount);
		} else {
			save(islandMoneyAmount);
		}
	}
	
	public List<IslandMoneyAmount> getMoneyAmonutBySerialNum(String serialNum) {
		if(StringUtils.isEmpty(serialNum)) {
			return null;
		}
		
		return islandMoneyAmountDao.find("from IslandMoneyAmount islandMoneyAmount where islandMoneyAmount.serialNum = ? and islandMoneyAmount.delFlag = ?", serialNum, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	/**
	 * 根据uuid获取金额字符串
	 * @param serialNum
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoneyStr(String serialNum, boolean isUserAdd) {
		String sql = "SELECT c.currency_mark, sum(m.amount) from island_money_amount m, currency c where m.currencyId = c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = islandMoneyAmountDao.getSession().createSQLQuery(sql).list();

		String money = "";

		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				if (null != amount[1]) {
					if (isUserAdd) {
						money += " + " + amount[0]
								+ " "
								+ d.format(new BigDecimal(amount[1].toString()));
					} else {
						if (amount[1].toString().contains("-")) {
							money += " - " + amount[0]
									+ " "
									+ d.format(new BigDecimal(amount[1].toString().replace("-", "")));
						} else {
							money += " + " + amount[0]
									+ " "
									+ d.format(new BigDecimal(amount[1].toString()));
						}
					}
				}
			}
			if (StringUtils.isNotBlank(money) && money.indexOf("+") == 1) {
				money = money.substring(2);
			}
		} else {
			money = "¥ 0.00";
		}

		return money;
	}
	
	public boolean saveOrUpdateMoneyAmounts(String serialNum, List<IslandMoneyAmount> moneyAmounts) throws Exception {
		if (moneyAmounts == null) {
			return false;
		}

		for (IslandMoneyAmount ma : moneyAmounts) {
			List<IslandMoneyAmount> exsitMaList = islandMoneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, ma.getCurrencyId());
			if (exsitMaList != null && exsitMaList.size() > 0) {
				ma.setId(exsitMaList.get(0).getId());
				ma.setExchangerate(exsitMaList.get(0).getExchangerate());
			} else {
				Currency currency = currencyDao.findOne(Long.parseLong(ma.getCurrencyId().toString()));
				ma.setExchangerate(currency.getConvertLowest().doubleValue());
			}
			
			saveOrUpdateMoneyAmount(ma);
		}
		return false;
	}
	
	/**
	 * 此接口用于保存金额的业务，如果与支付有关的接口需要调用saveMoneyAmounts
	 * 
	 * @param moneyAmount
	 * @return
	 */
	private boolean saveOrUpdateMoneyAmount(IslandMoneyAmount moneyAmount) throws Exception {
		if (moneyAmount == null) {
			return false;
		}
		
		try{
			List<IslandMoneyAmount> exsitMaList = islandMoneyAmountDao.findAmountBySerialNumAndCurrencyId(moneyAmount.getSerialNum(), moneyAmount.getCurrencyId());
			if (exsitMaList != null && exsitMaList.size() > 0) {
				moneyAmount.setId(exsitMaList.get(0).getId());
				moneyAmount.setExchangerate(exsitMaList.get(0).getExchangerate().doubleValue());
			} else {
				Currency currency = currencyDao.findOne(Long.parseLong(moneyAmount.getCurrencyId().toString()));
				moneyAmount.setExchangerate(currency.getConvertLowest().doubleValue());
			}
			this.save(moneyAmount);
		} catch(Exception e) {
			throw e;
		}
		return true;
	}
	
	/**
	 * 多币种相加或相减
	 * @param srcPrice 原始金额
	 * @param targetPrice 被减金额
	 * @param isAdd 相加或相减
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
				String srcCurrencyPrice = srcCurreney.get(i).split(" ")[1].replace(",", "");
				for (int j = 0; j < targetCurreney.size(); j++) {
					String targetCurrencyMark = targetCurreney.get(j).split(" ")[0];
					String targetCurrencyPrice = targetCurreney.get(j).split(" ")[1].replace(",", "");
					if (srcCurrencyMark.equals(targetCurrencyMark)) {
						BigDecimal srcPrice = new BigDecimal(srcCurrencyPrice);
						BigDecimal targetPrice = new BigDecimal(targetCurrencyPrice);
						if (isAdd) {
							srcCurrencyPrice = srcPrice.add(targetPrice).toString();
						} else {
							srcCurrencyPrice = srcPrice.subtract(targetPrice).toString();
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
				String targetPrice = targetCurreney.get(i).split(" ")[1].replace(",", "");
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
			} else {
				result = "¥ 0.00";
			}
		}
		return result;
	}
	
	/**
	 * 根据流水号获取多币种信息
	 * @param serialNum
	 * @return
	 */
	public List<Object[]> getMoneyAmonut(String serialNum) {
		String sql = "SELECT m.currencyId, c.currency_name, c.currency_mark, sum(m.amount), m.exchangerate " + 
					 "FROM island_money_amount m,currency c where m.currencyId = c.currency_id and m.serialNum = '" + serialNum + 
					  "' GROUP BY m.currencyId ORDER BY m.currencyId";
		return islandMoneyAmountDao.findBySql(sql);
	}
	
	/**
	 * 多币种相减
	 * @param srcPrice 原始金额
	 * @param targetPrice 被减金额
	 * @return
	 */
	public List<String> subtract(List<String> srcCurreney, List<String> targetCurreney) {
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
					String targetCurrencyPrice = targetCurreney.get(j).split(" ")[1];
					if (srcCurrencyId.equals(targetCurrencyId)) {
						BigDecimal srcPrice = new BigDecimal(srcCurrencyPrice);
						BigDecimal targetPrice = new BigDecimal(targetCurrencyPrice);
						srcCurrencyPrice = srcPrice.subtract(targetPrice).toString();
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
					priceArr += "-" + targetPrice + ",";
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
	
	public List<IslandMoneyAmount> findAmountBySerialNumAndCurrencyId(String serialNum, Integer currencyId) {
		if(StringUtils.isEmpty(serialNum) || currencyId == null) {
			return null;
		}
		
		return islandMoneyAmountDao.find("from IslandMoneyAmount islandMoneyAmount where islandMoneyAmount.serialNum = ? and islandMoneyAmount.currencyId=? and islandMoneyAmount.delFlag = ?", serialNum, currencyId, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	/**
	 * 根据流水号修改款项类型
	*<p>Title: updateMoneyTypeBySerialNum</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午12:02:01
	* @throws
	 */
	public boolean updateMoneyTypeBySerialNum(String serialNum, int moneyType) {
		islandMoneyAmountDao.createQuery("update IslandMoneyAmount amount set amount.moneyType=? where amount.serialNum=? and amount.delFlag=?", moneyType, serialNum, BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
		return true;
	}
	
	/**
	 * 更新金额，增加或减少特定币种金额
	*<p>Title: updateMoneyAmount</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午1:33:08
	* @throws
	 */
	public boolean addOrSubtractMoneyAmount(IslandMoneyAmount islandMoneyAmount, String serialNum, boolean isAdd) {
		IslandMoneyAmount payedMoney;
		List<IslandMoneyAmount> list = islandMoneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, islandMoneyAmount.getCurrencyId());
		if (list != null && list.size() > 0) {
			payedMoney = list.get(0);
			if (isAdd) {
				islandMoneyAmountDao.updateAmount(payedMoney.getUuid(), payedMoney.getAmount()+ islandMoneyAmount.getAmount());
			} else {
				islandMoneyAmountDao.updateAmount(payedMoney.getUuid(), payedMoney.getAmount() - islandMoneyAmount.getAmount());
			}
		}
		return true;
	}

	@Override
	public Double getExchangerateByUuid(String serialNum, Integer currencyId) {
		List<IslandMoneyAmount> maList = islandMoneyAmountDao
				.findAmountBySerialNumAndCurrencyId(serialNum, currencyId);
		if (CollectionUtils.isNotEmpty(maList)) {
			return maList.get(0).getExchangerate();
		} else {
			Currency currency = currencyDao.findOne(Long.parseLong(currencyId
					.toString()));
			if (currency != null) {
				return currency.getConvertLowest().doubleValue();
			} else {
				return null;
			}
		}
	}

	/**
	 * add by chy2015年6月19日14:12:24
	 * 根据reviewid查询moneyAmount
	 */
	@Override
	public List<IslandMoneyAmount> findAmountByReviewId(Long reviewId) {
		
		return islandMoneyAmountDao.findAmountByReviewId(reviewId);
	}
	
	/**
	 * add by chy2015年12月2日18:17:30
	 * 根据reviewid查询moneyAmount
	 */
	@Override
	public List<IslandMoneyAmount> findAmountByReviewUuId(String reviewId) {
		
		return islandMoneyAmountDao.findAmountByReviewUuId(reviewId);
	}
	
	/**
	 * 多币种付款公用接口：仅用于更改已付金额
	 * @param moneyAmounts
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public boolean saveMoneyAmounts(List<IslandMoneyAmount> moneyAmounts) {
		if (CollectionUtils.isNotEmpty(moneyAmounts)) {

			String serialNum = getOrderPayedMoneyUuid(moneyAmounts.get(0));

			for (int i = 0; i < moneyAmounts.size(); i++) {
				IslandMoneyAmount moneyAmount = moneyAmounts.get(i);
				if (moneyAmount != null && moneyAmount.getAmount() != null) {
					if (StringUtils.isBlank(moneyAmount.getSerialNum())) {
						moneyAmount.setSerialNum(UUID.randomUUID().toString());
					}
					moneyAmount.setCreateBy(UserUtils.getUser().getId().intValue());
					saveOrUpdateMoneyAmount(moneyAmount, serialNum, "add", moneyAmount.getMoneyType());
				}
			}
		}
		return true;
	}
	
	/**
	 * 根据支付流水查询订单已付金额UUID
	 * @param moneyAmount
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	private String getOrderPayedMoneyUuid(IslandMoneyAmount moneyAmount) {
		IslandOrder islandOrder = islandOrderDao.getByUuid(moneyAmount.getBusinessUuid());
		String  serialNum = islandOrder.getPayedMoney();
		if (StringUtils.isEmpty(serialNum)) {
			serialNum = UuidUtils.generUuid();
			islandOrder.setPayedMoney(serialNum);
			islandOrderDao.updateObj(islandOrder);
		}
		return serialNum;
	}
	
	/**
	 * 保存或更新金额
	 * 
	 * @param moneyAmount
	 * @param serialNum
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void saveOrUpdateMoneyAmount(IslandMoneyAmount moneyAmount, String serialNum, String operate, Integer moneyType) {
		IslandMoneyAmount payedMoney;

		List<IslandMoneyAmount> list = islandMoneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, moneyAmount.getCurrencyId());
		if (CollectionUtils.isNotEmpty(list)) {
			payedMoney = list.get(0);
			if (StringUtils.isNotEmpty(operate) && "add".equals(operate)) {
				payedMoney.setAmount(payedMoney.getAmount() + moneyAmount.getAmount());
			} else if (StringUtils.isNotEmpty(operate) && "subtract".equals(operate)) {
				payedMoney.setAmount(payedMoney.getAmount() - moneyAmount.getAmount());
			}
			islandMoneyAmountDao.updateObj(payedMoney);
		} else {
			payedMoney = new IslandMoneyAmount();
			payedMoney.setSerialNum(serialNum);
			if (StringUtils.isNotEmpty(operate) && "add".equals(operate)) {
				payedMoney.setAmount(moneyAmount.getAmount());
			} else if (StringUtils.isNotEmpty(operate) && "subtract".equals(operate)) {
				payedMoney.setAmount(new BigDecimal(moneyAmount.getAmount()).negate().doubleValue());// 加了取反
			}
			payedMoney.setBusinessType(moneyAmount.getBusinessType());
			payedMoney.setCreateBy(moneyAmount.getCreateBy());
			payedMoney.setCreateDate(moneyAmount.getCreateDate());
			payedMoney.setCurrencyId(moneyAmount.getCurrencyId());
			payedMoney.setMoneyType(moneyType);
			payedMoney.setBusinessUuid(moneyAmount.getBusinessUuid());

			try {
				saveOrUpdateMoneyAmount(payedMoney);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Transactional(readOnly = true)
	public List<IslandMoneyAmount> findAmount(String serialNum) {
		return islandMoneyAmountDao.findAmount(serialNum);
	}
	@Transactional(readOnly = true)
	public List<IslandMoneyAmount> findAmount(String orderUuid, Integer moneyType) {
		return islandMoneyAmountDao.findAmount(orderUuid, moneyType);
	}
}
