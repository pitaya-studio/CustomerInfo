/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao;
import java.math.BigDecimal;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface IslandMoneyAmountDao  extends BaseDao<IslandMoneyAmount> {
	
	public IslandMoneyAmount getByUuid(String uuid);
	
	/**
	 *  根据流水号和币种Id获取islandMoneyAmount集合
	*<p>Title: findAmountBySerialNumAndCurrencyId</p>
	* @return List<MoneyAmount> 返回类型
	* @author majiancheng
	* @date 2015-6-16 上午11:00:32
	* @throws
	 */
	public List<IslandMoneyAmount> findAmountBySerialNumAndCurrencyId(String serialNum,Integer currencyId);
	
	/**
	 * 根据uuid更新金额数据
	*<p>Title: updateAmount</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午2:24:32
	* @throws
	 */
	public boolean updateAmount(String uuid, double amount);
	
	
	/**
	 * 根据uuid更新金额数据
	*<p>Title: updateAmount</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午2:24:32
	* @throws
	 */
	public boolean updateAmount(String uuid, BigDecimal amount);
	/**
	 * 根据reviewId查询moneyAmount add by chy 2015年6月19日14:13:37
	 */
	public List<IslandMoneyAmount> findAmountByReviewId(Long reviewId);
	public List<IslandMoneyAmount> findAmount(String serialNum);
	public List<IslandMoneyAmount> findAmount(String orderUuid, Integer moneyType);

	List<IslandMoneyAmount> findAmountByReviewUuId(String reviewId);
}
