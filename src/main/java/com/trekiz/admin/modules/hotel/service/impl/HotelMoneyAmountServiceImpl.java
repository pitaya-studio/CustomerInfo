/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

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
import com.trekiz.admin.modules.hotel.dao.HotelMoneyAmountDao;
import com.trekiz.admin.modules.hotel.dao.HotelOrderDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.input.HotelMoneyAmountInput;
import com.trekiz.admin.modules.hotel.query.HotelMoneyAmountQuery;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
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
public class HotelMoneyAmountServiceImpl  extends BaseService implements HotelMoneyAmountService{
	@Autowired
	private HotelMoneyAmountDao hotelMoneyAmountDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private HotelOrderDao hotelOrderDao;

	public void save (HotelMoneyAmount hotelMoneyAmount) {
		if (hotelMoneyAmount != null) {
			super.setOptInfo(hotelMoneyAmount, BaseService.OPERATION_ADD);
			Currency currency = currencyDao.findOne(Long.parseLong(hotelMoneyAmount.getCurrencyId().toString()));
			hotelMoneyAmount.setExchangerate(currency.getConvertLowest().doubleValue());
			hotelMoneyAmountDao.saveObj(hotelMoneyAmount);
		}
	}
	
	public void save (HotelMoneyAmountInput hotelMoneyAmountInput){
		HotelMoneyAmount hotelMoneyAmount = hotelMoneyAmountInput.getHotelMoneyAmount();
		super.setOptInfo(hotelMoneyAmount, BaseService.OPERATION_ADD);
		Currency currency = currencyDao.findOne(Long.parseLong(hotelMoneyAmount.getCurrencyId().toString()));
		hotelMoneyAmount.setExchangerate(currency.getConvertLowest().doubleValue());
		hotelMoneyAmountDao.saveObj(hotelMoneyAmount);
	}
	
	public void update (HotelMoneyAmount hotelMoneyAmount){
		super.setOptInfo(hotelMoneyAmount, BaseService.OPERATION_UPDATE);
		List<HotelMoneyAmount> exsitMaList = hotelMoneyAmountDao.findAmountBySerialNumAndCurrencyId(
				hotelMoneyAmount.getSerialNum(), hotelMoneyAmount.getCurrencyId());
		if (exsitMaList != null && exsitMaList.size() > 0) {
			hotelMoneyAmount.setId(exsitMaList.get(0).getId());
			hotelMoneyAmount.setExchangerate(exsitMaList.get(0).getExchangerate());
		} else {
			Currency currency = currencyDao.findOne(Long.parseLong(hotelMoneyAmount.getCurrencyId().toString()));
			hotelMoneyAmount.setExchangerate(currency.getConvertLowest().doubleValue());
		}
		hotelMoneyAmountDao.updateObj(hotelMoneyAmount);
	}
	
	public HotelMoneyAmount getById(java.lang.Long value) {
		return hotelMoneyAmountDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		HotelMoneyAmount obj = hotelMoneyAmountDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelMoneyAmount> find(Page<HotelMoneyAmount> page, HotelMoneyAmountQuery hotelMoneyAmountQuery) {
		DetachedCriteria dc = hotelMoneyAmountDao.createDetachedCriteria();
		
	   	if(hotelMoneyAmountQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelMoneyAmountQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelMoneyAmountQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelMoneyAmountQuery.getUuid()));
		}
	   	if(hotelMoneyAmountQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelMoneyAmountQuery.getCurrencyId()));
	   	}
	   	if(hotelMoneyAmountQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelMoneyAmountQuery.getAmount()));
	   	}
	   	if(hotelMoneyAmountQuery.getExchangerate()!=null){
	   		dc.add(Restrictions.eq("exchangerate", hotelMoneyAmountQuery.getExchangerate()));
	   	}
		if (StringUtils.isNotEmpty(hotelMoneyAmountQuery.getBusinessUuid())){
			dc.add(Restrictions.eq("businessUuid", hotelMoneyAmountQuery.getBusinessUuid()));
		}
	   	if(hotelMoneyAmountQuery.getMoneyType()!=null){
	   		dc.add(Restrictions.eq("moneyType", hotelMoneyAmountQuery.getMoneyType()));
	   	}
	   	if(hotelMoneyAmountQuery.getBusinessType()!=null){
	   		dc.add(Restrictions.eq("businessType", hotelMoneyAmountQuery.getBusinessType()));
	   	}
	   	if(hotelMoneyAmountQuery.getReviewId()!=null){
	   		dc.add(Restrictions.eq("reviewId", hotelMoneyAmountQuery.getReviewId()));
	   	}
		if (StringUtils.isNotEmpty(hotelMoneyAmountQuery.getSerialNum())){
			dc.add(Restrictions.eq("serialNum", hotelMoneyAmountQuery.getSerialNum()));
		}
	   	if(hotelMoneyAmountQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelMoneyAmountQuery.getCreateBy()));
	   	}
		if(hotelMoneyAmountQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelMoneyAmountQuery.getCreateDate()));
		}
	   	if(hotelMoneyAmountQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelMoneyAmountQuery.getUpdateBy()));
	   	}
		if(hotelMoneyAmountQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelMoneyAmountQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelMoneyAmountQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelMoneyAmountQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelMoneyAmountDao.find(page, dc);
	}
	
	public List<HotelMoneyAmount> find( HotelMoneyAmountQuery hotelMoneyAmountQuery) {
		DetachedCriteria dc = hotelMoneyAmountDao.createDetachedCriteria();
		
	   	if(hotelMoneyAmountQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelMoneyAmountQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelMoneyAmountQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelMoneyAmountQuery.getUuid()));
		}
	   	if(hotelMoneyAmountQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelMoneyAmountQuery.getCurrencyId()));
	   	}
	   	if(hotelMoneyAmountQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", hotelMoneyAmountQuery.getAmount()));
	   	}
	   	if(hotelMoneyAmountQuery.getExchangerate()!=null){
	   		dc.add(Restrictions.eq("exchangerate", hotelMoneyAmountQuery.getExchangerate()));
	   	}
		if (StringUtils.isNotEmpty(hotelMoneyAmountQuery.getBusinessUuid())){
			dc.add(Restrictions.eq("businessUuid", hotelMoneyAmountQuery.getBusinessUuid()));
		}
	   	if(hotelMoneyAmountQuery.getMoneyType()!=null){
	   		dc.add(Restrictions.eq("moneyType", hotelMoneyAmountQuery.getMoneyType()));
	   	}
	   	if(hotelMoneyAmountQuery.getBusinessType()!=null){
	   		dc.add(Restrictions.eq("businessType", hotelMoneyAmountQuery.getBusinessType()));
	   	}
	   	if(hotelMoneyAmountQuery.getReviewId()!=null){
	   		dc.add(Restrictions.eq("reviewId", hotelMoneyAmountQuery.getReviewId()));
	   	}
		if (StringUtils.isNotEmpty(hotelMoneyAmountQuery.getSerialNum())){
			dc.add(Restrictions.eq("serialNum", hotelMoneyAmountQuery.getSerialNum()));
		}
	   	if(hotelMoneyAmountQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelMoneyAmountQuery.getCreateBy()));
	   	}
		if(hotelMoneyAmountQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelMoneyAmountQuery.getCreateDate()));
		}
	   	if(hotelMoneyAmountQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelMoneyAmountQuery.getUpdateBy()));
	   	}
		if(hotelMoneyAmountQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelMoneyAmountQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelMoneyAmountQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelMoneyAmountQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelMoneyAmountDao.find(dc);
	}
	
	public HotelMoneyAmount getByUuid(String uuid) {
		return hotelMoneyAmountDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelMoneyAmount hotelMoneyAmount = getByUuid(uuid);
		hotelMoneyAmount.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelMoneyAmount);
	}
	
	public void saveOrUpdate(HotelMoneyAmount hotelMoneyAmount) {
		if(StringUtils.isEmpty(hotelMoneyAmount.getUuid())) {
			this.save(hotelMoneyAmount);
		} else {
			this.update(hotelMoneyAmount);
		}
	}
	
	public List<HotelMoneyAmount> findAmountBySerialNumAndCurrencyId(String serialNum, Integer currencyId) {
		if(StringUtils.isEmpty(serialNum) || currencyId == null) {
			return null;
		}
		
		return hotelMoneyAmountDao.find("from HotelMoneyAmount hotelMoneyAmount where hotelMoneyAmount.serialNum = ? and hotelMoneyAmount.currencyId=? and hotelMoneyAmount.delFlag = ?", serialNum, currencyId, BaseEntity.DEL_FLAG_NORMAL);
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
		hotelMoneyAmountDao.createQuery("update HotelMoneyAmount amount set amount.moneyType=? where amount.serialNum=? and amount.delFlag=?", moneyType, serialNum, BaseEntity.DEL_FLAG_NORMAL).executeUpdate();
		return true;
	}
	
	public List<HotelMoneyAmount> getMoneyAmonutBySerialNum(String serialNum) {
		if(StringUtils.isEmpty(serialNum)) {
			return null;
		}
		
		return hotelMoneyAmountDao.find("from HotelMoneyAmount hotelMoneyAmount where hotelMoneyAmount.serialNum = ? and hotelMoneyAmount.delFlag = ?", serialNum, BaseEntity.DEL_FLAG_NORMAL);
	}
	
	/**
	 * 更新金额，增加或减少特定币种金额
	*<p>Title: updateMoneyAmount</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午1:33:08
	* @throws
	 */
	public boolean addOrSubtractMoneyAmount(HotelMoneyAmount hotelMoneyAmount, String serialNum, boolean isAdd) {
		HotelMoneyAmount payedMoney;
		List<HotelMoneyAmount> list = this.findAmountBySerialNumAndCurrencyId(serialNum, hotelMoneyAmount.getCurrencyId());
		if (list != null && list.size() > 0) {
			payedMoney = list.get(0);
			if (isAdd) {
				hotelMoneyAmountDao.updateAmount(payedMoney.getUuid(), payedMoney.getAmount() + hotelMoneyAmount.getAmount());
			} else {
				hotelMoneyAmountDao.updateAmount(payedMoney.getUuid(), payedMoney.getAmount() - hotelMoneyAmount.getAmount());
			}
		}
		return true;
	}
	
	public boolean saveOrUpdateMoneyAmounts(String serialNum, List<HotelMoneyAmount> moneyAmounts) throws Exception {
		if (moneyAmounts == null) {
			return false;
		}

		for (HotelMoneyAmount ma : moneyAmounts) {
			List<HotelMoneyAmount> exsitMaList = this.findAmountBySerialNumAndCurrencyId(serialNum, ma.getCurrencyId());
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
	private boolean saveOrUpdateMoneyAmount(HotelMoneyAmount moneyAmount) throws Exception {
		if (moneyAmount == null) {
			return false;
		}
		
		try{
			List<HotelMoneyAmount> exsitMaList = this.findAmountBySerialNumAndCurrencyId(moneyAmount.getSerialNum(), moneyAmount.getCurrencyId());
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
	 * 根据uuid获取金额字符串
	 * @param serialNum
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public String getMoneyStr(String serialNum, boolean isUserAdd) {
		String sql = "SELECT c.currency_mark, sum(m.amount) from hotel_money_amount m, currency c where m.currencyId = c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = hotelMoneyAmountDao.getSession().createSQLQuery(sql).list();

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
					 "FROM hotel_money_amount m,currency c where m.currencyId = c.currency_id and m.serialNum = '" + serialNum + 
					  "' GROUP BY m.currencyId ORDER BY m.currencyId";
		return hotelMoneyAmountDao.findBySql(sql);
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

	@Override
	public Double getExchangerateByUuid(String serialNum, Integer currencyId) {
		List<HotelMoneyAmount> maList = hotelMoneyAmountDao
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
	 * 根据reviewId查询记录
	 */
	@Override
	public List<HotelMoneyAmount> getMoneyAmonutByReviewId(Long reviewId) {
		
		return hotelMoneyAmountDao.getHotelMoneyAmountByReviewid(reviewId);
	}
	
	/**
	 * 根据reviewUuid获取hotelMoneyAmount集合
	 * @param serialNum
	 * @return
	 * @author 曹红义 2015年12月2日18:07:36
	 */
	public List<HotelMoneyAmount> getMoneyAmonutByReviewUuId(String reviewUuid){
		
		return hotelMoneyAmountDao.getHotelMoneyAmountByReviewUuid(reviewUuid);
	}
	
	/**
	 * 多币种付款公用接口：仅用于更改已付金额
	 * @param moneyAmounts
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public boolean saveMoneyAmounts(List<HotelMoneyAmount> moneyAmounts) {
		if (CollectionUtils.isNotEmpty(moneyAmounts)) {

			String serialNum = getOrderPayedMoneyUuid(moneyAmounts.get(0));

			for (int i = 0; i < moneyAmounts.size(); i++) {
				HotelMoneyAmount moneyAmount = moneyAmounts.get(i);
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
	private String getOrderPayedMoneyUuid(HotelMoneyAmount moneyAmount) {
		HotelOrder hotelOrder = hotelOrderDao.getByUuid(moneyAmount.getBusinessUuid());
		String  serialNum = hotelOrder.getPayedMoney();
		if (StringUtils.isEmpty(serialNum)) {
			serialNum = UuidUtils.generUuid();
			hotelOrder.setPayedMoney(serialNum);
			hotelOrderDao.updateObj(hotelOrder);
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
	public void saveOrUpdateMoneyAmount(HotelMoneyAmount moneyAmount, String serialNum, String operate, Integer moneyType) {
		HotelMoneyAmount payedMoney;

		List<HotelMoneyAmount> list = hotelMoneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, moneyAmount.getCurrencyId());
		if (CollectionUtils.isNotEmpty(list)) {
			payedMoney = list.get(0);
			if (StringUtils.isNotEmpty(operate) && "add".equals(operate)) {
				payedMoney.setAmount(payedMoney.getAmount() + moneyAmount.getAmount());
			} else if (StringUtils.isNotEmpty(operate) && "subtract".equals(operate)) {
				payedMoney.setAmount(payedMoney.getAmount() - moneyAmount.getAmount());
			}
			hotelMoneyAmountDao.updateObj(payedMoney);
		} else {
			payedMoney = new HotelMoneyAmount();
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
	public List<HotelMoneyAmount> findAmount(String uuid) {
		return hotelMoneyAmountDao.findAmount(uuid);
	}
	@Transactional(readOnly = true)
	public List<HotelMoneyAmount> findAmount(String orderUuid, Integer moneyType) {
		return hotelMoneyAmountDao.findAmount(orderUuid, moneyType);
	}
}
