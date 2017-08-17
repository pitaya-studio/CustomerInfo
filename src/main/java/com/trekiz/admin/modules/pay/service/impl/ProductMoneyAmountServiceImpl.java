package com.trekiz.admin.modules.pay.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.pay.model.ProductMoneyAmount;
import com.trekiz.admin.modules.pay.service.ProductMoneyAmountService;
import com.trekiz.admin.modules.pay.transfer.MoneyAmountTransfer;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;

@Service
@Transactional(readOnly = true)
public class ProductMoneyAmountServiceImpl implements ProductMoneyAmountService{
	
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private CurrencyDao currencyDao;

	@Override
	public boolean saveOrUpdateMoneyAmounts(String serialNum, List<ProductMoneyAmount> moneyAmountList, int orderType) {
		if(StringUtils.isEmpty(serialNum) || CollectionUtils.isEmpty(moneyAmountList)) {
			return false;
		}
		
		for (ProductMoneyAmount ma : moneyAmountList) {
			List<ProductMoneyAmount> exsitMaList = this.findAmountBySerialNumAndCurrencyId(serialNum, ma.getCurrencyId(), orderType);
			if (exsitMaList != null && exsitMaList.size() > 0) {
				ma.setId(exsitMaList.get(0).getId());
				ma.setExchangerate(exsitMaList.get(0).getExchangerate());
			} else {
				Currency currency = currencyDao.findOne(Long.parseLong(ma.getCurrencyId().toString()));
				ma.setExchangerate(currency.getConvertLowest().doubleValue());
			}
			saveOrUpdateMoneyAmount(ma, orderType);
		}

		return true;
	}
	
	public List<ProductMoneyAmount> findAmountBySerialNumAndCurrencyId(String serialNum, Integer currencyId, int orderType) {
		List<ProductMoneyAmount> productMoneyAmountList = new ArrayList<ProductMoneyAmount>();
		
		if(Context.ORDER_TYPE_ISLAND == orderType) {
			productMoneyAmountList = MoneyAmountTransfer.transfer2ProductMoneyAmountListFromIsland(islandMoneyAmountService.findAmountBySerialNumAndCurrencyId(serialNum, currencyId));
		} else if(Context.ORDER_TYPE_HOTEL == orderType) {
			productMoneyAmountList = MoneyAmountTransfer.transfer2ProductMoneyAmountListFromHotel(hotelMoneyAmountService.findAmountBySerialNumAndCurrencyId(serialNum, currencyId));
		}
		
		return productMoneyAmountList;
	}
	
	public boolean saveOrUpdateMoneyAmount(ProductMoneyAmount moneyAmount, int orderType) {
		
		if (moneyAmount == null) {
			return false;
		}
		
		if(Context.ORDER_TYPE_ISLAND == orderType) {
			islandMoneyAmountService.saveOrUpdate(MoneyAmountTransfer.transfer2IslandMoneyAmount(moneyAmount));
		} else if(Context.ORDER_TYPE_HOTEL == orderType) {
			hotelMoneyAmountService.saveOrUpdate(MoneyAmountTransfer.transfer2HotelMoneyAmount(moneyAmount));
		}
		
		return true;
	}

	@Override
	public boolean updateMoneyType(int orderType, String serialNum, int moneyType) {
		if(Context.ORDER_TYPE_ISLAND == orderType) {
			islandMoneyAmountService.updateMoneyTypeBySerialNum(serialNum, moneyType);
		} else if(Context.ORDER_TYPE_HOTEL == orderType) {
			hotelMoneyAmountService.updateMoneyTypeBySerialNum(serialNum, moneyType);
		}
		return false;
	}

}
