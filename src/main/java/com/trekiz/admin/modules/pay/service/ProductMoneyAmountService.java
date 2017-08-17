package com.trekiz.admin.modules.pay.service;

import java.util.List;

import com.trekiz.admin.modules.pay.model.ProductMoneyAmount;

public interface ProductMoneyAmountService {
	
	/**
	 * 保存或更新金额表
	*<p>Title: saveOrUpdateMoneyAmounts</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-16 下午9:52:17
	* @throws
	 */
	public boolean saveOrUpdateMoneyAmounts(String serialNum, List<ProductMoneyAmount> moneyAmountList, int orderType);
	
	/**
	 * 修改金额表中的付款方式类型为财务取消状态
	*<p>Title: updateMoneyType</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 上午11:58:08
	* @throws
	 */
	public boolean updateMoneyType(int orderType, String serialNum, int moneyType);
	
	/**
	 * 保存或更新金额表数据
	*<p>Title: saveOrUpdateMoneyAmount</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-7-1 下午8:45:26
	* @throws
	 */
	public boolean saveOrUpdateMoneyAmount(ProductMoneyAmount moneyAmount, int orderType);
	
}
