/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuote;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelQuoteService{
	
	public void save (HotelQuote hotelQuote);
	
	public void save (HotelQuoteInput hotelQuoteInput);
	
	public void update (HotelQuote hotelQuote);
	
	public HotelQuote getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelQuote> find(Page<HotelQuote> page, HotelQuoteQuery hotelQuoteQuery);
	
	public Page<Map<String, Object>> hotelQuoteList(Page<Map<String, Object>> page, HotelQuoteQuery hotelQuoteQuery);
	
	public List<List<Map<String, String>>> getQuoteRoomList(List<Map<String, Object>> list);
	
	public List<HotelQuote> find( HotelQuoteQuery hotelQuoteQuery);
	
	public HotelQuote getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据价单优惠json信息和报价json信息保存报价信息
	*<p>Title: saveQuotedPriceInfo</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-7-17 下午4:24:56
	* @throws
	 */
	public boolean saveQuotedPriceInfo(HotelQuoteInput hotelQuoteInput);

}
