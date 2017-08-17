/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.input.IslandMoneyAmountInput;
import com.trekiz.admin.modules.island.query.IslandMoneyAmountQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface IslandMoneyAmountService {
	
	public void save (IslandMoneyAmount islandMoneyAmount);
	
	public void save (IslandMoneyAmountInput islandMoneyAmountInput);
	
	public void update (IslandMoneyAmount islandMoneyAmount);
	
	public IslandMoneyAmount getById(java.lang.Long value);
	
	public void removeById(java.lang.Long value);
	
	public Page<IslandMoneyAmount> find(Page<IslandMoneyAmount> page, IslandMoneyAmountQuery islandMoneyAmountQuery);
	
	public List<IslandMoneyAmount> find( IslandMoneyAmountQuery islandMoneyAmountQuery);
	
	public IslandMoneyAmount getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public void saveOrUpdate(IslandMoneyAmount islandMoneyAmount);
	
	/**
	 * 根据流水号获取islandMoneyAmount集合
	*<p>Title: getMoneyAmonutBySerialNum</p>
	* @return List<IslandMoneyAmount> 返回类型
	* @author majiancheng
	* @date 2015-6-16 下午4:38:22
	* @throws
	 */
	public List<IslandMoneyAmount> getMoneyAmonutBySerialNum(String serialNum);
	
	/**
	 * 根据uuid获取金额字符串
	 * @param serialNum
	 * @return
	 */
	public String getMoneyStr(String serialNum, boolean isUserAdd);
	
	/**
	 * 此接口用于保存金额的业务，如果与支付有关的接口需要调用save
	*<p>Title: saveOrUpdateMoneyAmounts</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-16 上午10:51:52
	* @throws
	 */
	public boolean saveOrUpdateMoneyAmounts(String serialNum, List<IslandMoneyAmount> moneyAmounts) throws Exception ;
	
	/**
	 * 多币种相加或相减
	 * @param srcPrice 原始金额
	 * @param targetPrice 被减金额
	 * @param isAdd 相加或相减
	 * @return
	 */
	public String addOrSubtract(String srcCurreneyStr, String targetCurreneyStr, boolean isAdd);
	
	/**
	 * 根据流水号获取多币种信息
	 * @param serialNum
	 * @return
	 */
	public List<Object[]> getMoneyAmonut(String serialNum);
	
	/**
	 * 多币种相减
	 * @param srcPrice 原始金额
	 * @param targetPrice 被减金额
	 * @return
	 */
	public List<String> subtract(List<String> srcCurreney, List<String> targetCurreney);
	
	/**
	 * 根据流水号和币种id获取海岛游金额表集合
	*<p>Title: findAmountBySerialNumAndCurrencyId</p>
	* @return List<IslandMoneyAmount> 返回类型
	* @author majiancheng
	* @date 2015-6-16 下午8:38:11
	* @throws
	 */
	public List<IslandMoneyAmount> findAmountBySerialNumAndCurrencyId(String serialNum, Integer currencyId);
	
	/**
	 * 根据reviewId查询moneyAmount记录 
	 * add by chy 2015-6-19 14:11:59
	 */
	public List<IslandMoneyAmount> findAmountByReviewId(Long reviewId);
	/**
	 * 根据流水号修改款项类型
	*<p>Title: updateMoneyTypeBySerialNum</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午12:02:01
	* @throws
	 */
	public boolean updateMoneyTypeBySerialNum(String serialNum, int moneyType);

	/**
	 * 更新金额，增加或减少特定币种金额
	*<p>Title: updateMoneyAmount</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午1:33:08
	* @throws
	 */
	public boolean addOrSubtractMoneyAmount(IslandMoneyAmount islandMoneyAmount, String serialNum, boolean isAdd);
	
	public Double getExchangerateByUuid(String serialNum, Integer currencyId);
	
	/**
	 * 多币种付款公用接口
	 * @param moneyAmounts
	 * @return
	 */
	public boolean saveMoneyAmounts(List<IslandMoneyAmount> moneyAmounts);
	
	/**
	 * 保存或更新金额
	 * 
	 * @param moneyAmount
	 * @param serialNum
	 */
	public void saveOrUpdateMoneyAmount(IslandMoneyAmount moneyAmount, String serialNum, String operate, Integer moneyType);
	public List<IslandMoneyAmount> findAmount(String serialNum);
	public List<IslandMoneyAmount> findAmount(String orderUuid, Integer moneyType);

	List<IslandMoneyAmount> findAmountByReviewUuId(String reviewId);
}
