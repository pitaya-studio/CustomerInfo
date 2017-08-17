/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.common.input.BaseOut4MT;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.jsonbean.*;
import com.trekiz.admin.modules.mtourOrder.entity.MtourOrderParam;
import com.trekiz.admin.modules.sys.entity.Currency;


/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface MtourOrderService{
	
	/**
	 * 机票快速生成订单保存
	 * @author hhx
	 * @param input
	 * @return
	 */
	public BaseOut4MT saveAirticketOrder(BaseInput4MT input);
	
	/**
	 * 机票快速生成订单保存（非美图使用）
	 * @author hhx
	 * @param input
	 * @return
	 */
	public BaseOut4MT saveAirticketOrderForCommon(BaseInput4MT input);
	
	/**
	 * 机票订单追散保存
	 * @author hhx
	 * @param input
	 * @return
	 */
	public BaseOut4MT addToAirticketOrder(BaseInput4MT input);
	
	/**
	 * 机票订单追散保存（非美图使用）
	 * @author hhx
	 * @param input
	 * @return
	 */
	public BaseOut4MT addToAirticketOrderForCommon(BaseInput4MT input);
	
	/**
	 * 美图机票订单详情(订单、产品)
	 * @param orderId  机票订单id
	 * @return
	 */
	public List<Map<String,String>> getBaseInfoDetail(String orderId);
	
	/**
	 * 美图机票订单详情(渠道、联系人)
	 * @param orderId  机票订单id
	 * @return
	 */
	public List<Map<String,Object>> getReservationDetail(String orderId);
	
	/**
	 * 美图机票订单详情(航班)
	 * @param productId  机票产品id
	 * @return
	 */
	public List<Map<String,String>> getFlightDetail(String productId);
	
	/**
	 * 美图机票订单详情(游客)
	 * @param orderId  机票订单id
	 * @return
	 */
	public List<Map<String,Object>> getTravelerDetail(String orderId);
	
	/**
	 * 美图机票订单详情(证件)
	 * @param travelerId  游客id
	 * @return
	 */
	public List<Map<String,Object>> getTravelerCredentials(String travelerId);
	
	/**
	 * 美图机票订单详情(签证)
	 * @param travelerId  游客id
	 * @return
	 */
	public List<Map<String,Object>> getTravelerVisas(String travelerId);
	
	/**
	 * 美图机票订单详情(附件)
	 * @param productId  机票产品id
	 * @return
	 */
	public List<Map<String,String>> getAttachmentDetail(String productId);
	
	/**
	 * 美图机票订单详情(外报价)
	 * @param orderId  机票订单id
	 * @return
	 */
	public List<Map<String,String>> getSalePriceChangeDetail(String orderId);
	
	/**
	 * 美图机票订单详情(追加成本)
	 * @param orderId  机票订单id
	 * @return
	 */
	public List<Map<String,String>> getAdditionalPriceChangeDetail(String orderId,int moneyType);
	
	/**
	 * 美图机票订单详情(各种价格)
	 * @param orderId  机票订单id
	 * @param priceType 价格类型（订单总额、成本总额等）
	 * @return
	 */
	public List<Map<String,String>> getPriceDetail(String priceType,String orderId);
	
	/**
	 * 美图机票订单详情(组)
	 * @param orderId  airticket_order_pnr.uuid
	 * @return
	 */
	public List<Map<String,Object>> getInvoiceOriginalDetail(String pnrGroupUuid);
	
	/**
	 * 美图机票订单详情(航段及价格)
	 * @param orderId  Pnr表UUID
	 * @return
	 */
	public List<Map<String,Object>> getAirlineDetail(String PnrUUID);
	
	/**
	 * PNR列表基本信息
	 * @param orderUuid
	 * @return
	 */
	public List<Map<String,Object>> getBaseInfoPNR(String orderId);
	
	/**
	 * 查询PNR组内PNR集合
	 * @param pnrGroupUuid
	 * @return
	 */
	public List<Map<String,Object>> getPNRInvoiceOriginals(String pnrGroupUuid);
	
	/**
	 * PNR(列表)-展开-查看记录
	 * @param orderId
	 * @param invoiceOriginalGroupUuid
	 * @return
	 */
	public List<PNRRecordJsonBean> getPNRRecord(String orderId,String invoiceOriginalGroupUuid);
	
	/**
	 * PNR(列表)-展开-修改人数
	 * @param invoiceOriginalGroupUuid
	 * @param drawerCount
	 * @param reserveCount
	 * @return
	 */
	public String updatePNRTravelerNum(String drawerCount,String reserveCount,String invoiceOriginalGroupUuid);
	
	/**
	 * PNR修改预定人数时更新机票订单总人数
	 * @param value
	 * @return
	 */
	public void updateAirticketOrderPersonNum(Integer value,String orderId);
	
	/**
	 * xu.wang
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> getOrderReceiptList(String orderId);
	
	public List<Map<String,String>> getOrderPayDocList(String docIds);
	
	/**
	 * 收款撤销
	 * @param receiveId
	 */
	public void orderReceiptCancel(String receiveId, Integer orderId);
	
	/**
	 * 成本记录和其他收入删除
	 * @param costId
	 */
	public void deleteCostRecord(String costId);
	
	/**
	 * 订单列表接口
	 * @author gao
	 * 2015年10月21日
	 * @param searchParam
	 * @param filterParam
	 * @param pageParam
	 * @return
	 */
	public MtourOrderJsonBean getMtourOrderJsonBean(MtourOrderParam mtourOrderParam);
	
	public Map<String,Object> getOrderReceiptDetail(String receiveId);
	
	/**
	 * 保存订单支付信息接口
		 * @Title: saveOrderpayInfo
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-10-22 下午9:18:03
	 */
	public boolean saveOrderpayInfo(BaseInput4MT input);

	/**
	 * 查询订单的未收金额和外保总额
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> getOrderPriceDetail(String orderId);
	
	/**
	 * PNR组数据
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> getinvoiceOriginalGroupDetail(String orderId);
	
	/**
	 *  其他收款详情
	 * @param costRecordId
	 * @return
	 */
	public Map<String, Object> getOtherReceiptDetail(String costRecordId);
	
	/**
	 * 订单取消验证
	 * 1:草稿状态:提示确认后,直接取消
	2:订单还没有提交过借款、退款、追加成本、成本录入、订单收款、其他收入的（注意，保存操作不算提交），点击取消订单后，弹出取消确认弹窗，点击“是”后订单状态变更为“已取消”
	3:单提交过借款、退款、追加成本、成本录入、订单收款、其他收入的（注意，保存操作不算提交），但还可撤销的（提交借款、退款、追加成本、成本录入但财务未付款，就可撤销；
	     提交了订单收款、其他收入，但财务未确认收款，就可撤销），点击取消订单后，弹出确认弹窗，点击“是”后，已提交的借款、退款、追加成本、成本录入、订单收款、其他收入都自动撤销，且订单状态变更为“已取消”
	4:订单提交过借款、退款、追加成本、成本录入、订单收款、其他收入的（注意，保存操作不算提交），但不可撤销的（提交借款、退款、追加成本、成本录入且财务已付款，就不可撤销；提交了订单收款、其他收入，但财务已确认收款，就不可撤销），
	     点击取消订单后，弹出提示窗
		 * @Title: cancelOrderValidate
	     * @return Map<String,String>
	     * @author majiancheng       
	     * @date 2015-11-1 下午3:12:59
	 */
	public Map<String, String> cancelOrderValidate(String orderUuid);
	
	/**
	 *  订单取消
		描述:取消验证后,真正的取消订单
		 * @Title: cancelOrder
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-11-1 下午5:00:10
	 */
	public boolean cancelOrder(String orderUuid);

	/**
	 * 查看给定订单的支付状态，返回对应的代码(通过到账金额判断)
	 * @param orderId 机票订单id
	 * @return
	 */
	public Integer checkAirticketOrderPaymentStatus(String orderId);
	
	/**
	 * 查看给定订单的支付状态，返回对应的代码(通过已收金额判断)
	 * @param orderId 机票订单id
	 * @return
	 */
	public Integer getAirticketOrderPaymentStatus(String orderId);

	/**
	 * 更新订单的付款状态
	 * @param orderId
	 */
	public void updateAirticketOrderPaymentStatus(String orderId);
	
	/**
	 * 根据订单id,币种id查询订单汇率.
	 * 查询为空时,返回基础信息Currency.
	 * @author hhx
	 * @param orderId
	 * @param currencyId
	 * @return
	 */
	public Currency getOriginalCurrency(Long orderId,String currencyId);
	
	/**
	 * 其他收入、订单收款单独查询货币
	 * @author hhx
	 * @param orderId
	 * @param type
	 * @return
	 */
	public List<Map<String, String>> getExistsCurrency(Long orderId,String type);
	/**
	 * 订单列表支出单查询接口
	 * @param orderUuid
	 * @return
	 * @param orderUuid 订单id
	 * @author zhangchao
	 * @time 2016/1/29
	 */
	public QueryOrderJsonBean queryPayOrder(String orderUuid);
	
	/**
	 * 修改重复提交的状态
	 * @param receiveUuid
	 * @author chao.zhang@quauq.com
	 * @time 2016/6/6
	 */
	public void updateOrderPayRepeatSubmit(Long receiveUuid);
	/**
	 * 根据订单的id，查询是否有已确认的收款数据，或者是否有已付款的退款和已付款的追加成本数据。如果能够查到该订单的上面3项数据，
	 * 则预订人信息项不可以修改，返回false。查询不到则可以修改，返回true。对应需求0294. yudong.xu 2016.6.7
	 * @param orderId
	 * @return
	 */
	public Boolean reservationEditable(String orderId);
	
	/**
	 * 待确认订单生成正常订单接口
	 * @param orderUuid 订单id
	 * @return
	 * @author wangyang
	 * @date 2016.6.15
	 * */
	public boolean confirmOrder(String orderUuid);
	
	/**
	 * 获取非签约渠道
	 * @param page
	 * @param request
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public Page<Map<String,Object>> getNoAgent(Page<Map<String,Object>> page,HttpServletRequest request);
	
	/**
	 * 获得团号（用于组装批量下载收入单的团号）
	 * @param orderUuids
	 * @return
	 */
	public List<Map<String,Object>> getGroupCode(String orderUuids);
	
	/**
	 * 获得收款对象
	 * @param orderUuid
	 * @return
	 */
	public List<Map<String,Object>> getPayObj(Integer orderUuid);
	
	/**
	 * 获得具体收款信息
	 * @param orderUuid
	 * @return
	 */
	public List<Map<String,Object>> getPaymentObject(Integer orderUuid);
	public BatchOrderPaymentJsonBean batchQueryPayOrder(String ids);

}
