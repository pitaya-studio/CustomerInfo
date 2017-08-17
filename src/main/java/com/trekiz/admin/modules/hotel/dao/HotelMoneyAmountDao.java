/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.math.BigDecimal;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelMoneyAmountDao  extends BaseDao<HotelMoneyAmount> {
	
	public HotelMoneyAmount getByUuid(String uuid);
	
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
	* @date 2015-8-12 上午11:38:17
	* @throws
	 */
	public boolean updateAmount(String uuid, BigDecimal amount);
	
	public List<HotelMoneyAmount> findAmountBySerialNumAndCurrencyId(String serialNum,Integer currencyId);
	
	/**
	 * 根据reviewID查询moneyAmount
	 * @param uuid
	 * @return
	 */
	public List<HotelMoneyAmount> getHotelMoneyAmountByReviewid(Long reviewId);
	
	public List<HotelMoneyAmount> findAmount(String uuid);
	public List<HotelMoneyAmount> findAmount(String orderUuid, Integer moneyType);

	List<HotelMoneyAmount> getHotelMoneyAmountByReviewUuid(String reviewId);
}
