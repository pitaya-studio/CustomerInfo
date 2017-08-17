package com.trekiz.admin.modules.pay.transfer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.pay.model.ProductMoneyAmount;

public class MoneyAmountTransfer {
	
	public static ProductMoneyAmount transfer2ProductMoneyAmount(IslandMoneyAmount src) {
		ProductMoneyAmount dest = new ProductMoneyAmount();
		BeanUtil.copySimpleProperties(dest, src, false);
		return dest;
	}
	
	public static ProductMoneyAmount transfer2ProductMoneyAmount(HotelMoneyAmount src) {
		ProductMoneyAmount dest = new ProductMoneyAmount();
		BeanUtil.copySimpleProperties(dest, src, false);
		return dest;
	}
	
	public static IslandMoneyAmount transfer2IslandMoneyAmount(ProductMoneyAmount src) {
		IslandMoneyAmount dest = new IslandMoneyAmount();
		BeanUtil.copySimpleProperties(dest, src, false);
		return dest;
	}
	
	public static HotelMoneyAmount transfer2HotelMoneyAmount(ProductMoneyAmount src) {
		HotelMoneyAmount dest = new HotelMoneyAmount();
		BeanUtil.copySimpleProperties(dest, src, false);
		return dest;
	}
	
	public static List<ProductMoneyAmount> transfer2ProductMoneyAmountListFromIsland(List<IslandMoneyAmount> srcList) {
		List<ProductMoneyAmount> destList = new ArrayList<ProductMoneyAmount>();
		if(CollectionUtils.isEmpty(srcList)) {
			return destList;
		}
		
		for(IslandMoneyAmount src : srcList) {
			destList.add(transfer2ProductMoneyAmount(src));
		}
		
		return destList;
	}
	
	public static List<ProductMoneyAmount> transfer2ProductMoneyAmountListFromHotel(List<HotelMoneyAmount> srcList) {
		List<ProductMoneyAmount> destList = new ArrayList<ProductMoneyAmount>();
		if(CollectionUtils.isEmpty(srcList)) {
			return destList;
		}
		
		for(HotelMoneyAmount src : srcList) {
			destList.add(transfer2ProductMoneyAmount(src));
		}
		return destList;
	}
	
	public static List<IslandMoneyAmount> transfer2IslandMoneyAmountList(List<ProductMoneyAmount> srcList) {
		List<IslandMoneyAmount> destList = new ArrayList<IslandMoneyAmount>();
		if(CollectionUtils.isEmpty(srcList)) {
			return destList;
		}
		
		for(ProductMoneyAmount src : srcList) {
			destList.add(transfer2IslandMoneyAmount(src));
		}
		return destList;
	}
	
	public static List<HotelMoneyAmount> transfer2HotelMoneyAmountList(List<ProductMoneyAmount> srcList) {
		List<HotelMoneyAmount> destList = new ArrayList<HotelMoneyAmount>();
		if(CollectionUtils.isEmpty(srcList)) {
			return destList;
		}
		
		for(ProductMoneyAmount src : srcList) {
			destList.add(transfer2HotelMoneyAmount(src));
		}
		return destList;
	}
	
}
