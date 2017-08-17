/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.input.HotelOrderInput;
import com.trekiz.admin.modules.hotel.query.HotelOrderQuery;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.pay.entity.PayHotelOrder;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelOrderService {
	
	public void save (HotelOrder hotelOrder);
	
	public void update (HotelOrder hotelOrder);
	
	public HotelOrder getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelOrder> find(Page<HotelOrder> page, HotelOrderQuery hotelOrderQuery);
	
	public List<HotelOrder> find( HotelOrderQuery hotelOrderQuery);
	
	public List<HotelOrder> getByHotelUuid(String hotelUuid);
	
	public HotelOrder getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 保存酒店订单表以及订单子表数据
	*<p>Title: saveHotelOrder</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-6-3 下午12:16:44
	* @throws
	 */
	public Map<String, String> saveHotelOrder(HotelOrderInput hotelOrderInput) throws Exception;
	
	/**
	 * 撤消支付确认操作
	*<p>Title: hotelOrderCancel</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 上午10:44:14
	* @throws
	 */
	public boolean hotelOrderCancel(String payUuid, String orderUuid);
	
	/**
	 * 酒店达帐撤销
	*<p>Title: DTcancelOrder</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午12:21:55
	* @throws
	 */
	public boolean DTcancelOrder(String payUuid, HotelOrder hotelOrder);
	
	/**
	 *  初始化预报名页面数据
	 *<p>Title: initHotelOrderPageData</p>
	 * @return void 返回类型
	 * @author majiancheng
	 * @date 2015-6-17 下午10:37:08
	 * @throws
	 */
	public void initHotelOrderPageData(String hotelOrderUuid, Model model);
	/**
	 * 酒店团期预报名人数
	 * @param groupUuid
	 * @return
	 */
	public Integer  getBookingPersonNum(String groupUuid);
	
	/**
	 * 酒店团期预报名间数
	 * @param groupUuid
	 * @return
	 */
	public Integer  getForecaseReportNum(String groupUuid);
	
	/**
	 * 封装订单支付数据
	*<p>Title: buildOrderPayData</p>
	* @return OrderPayInput 返回类型
	* @author majiancheng
	* @date 2015-6-18 上午10:59:29
	* @throws
	 */
	public OrderPayInput buildOrderPayData(List<String> orderUuids, String[] resultCurrency, String[] resultAmount, String cancelPayUrl);
	
	/**
	 * 查询酒店订单列表
	 * @param page
	 * @param hotelOrderQuery 查询对象
	 * @param common 部门对象
	 * @return
	 */
	public Page<Map<Object, Object>> findOrderList(Page<Map<Object, Object>> page, HotelOrderQuery hotelOrderQuery, DepartmentCommon common);
	
	/**
     * 根据团期IDS查询订单
     * @param groupList
     * @param orderSql
     * @return 订单列表
     */
    public Page<Map<Object, Object>> findByGroupIds(Page<Map<Object, Object>> page, List<String> groupList, String orderSql);
    
    /**
	 * 清除session中Orderpay的缓存
	 * @param role
	 * @return
	 */
	public Object clearObject(Object object);
	
	/**
	 * 订单财务收款提示信息： 确认达帐例子：财务已确认订金/尾款/全款达帐金额$100；
	 * 						撤销达帐例子：财务已撤销订金/尾款/全款达帐金额$100；
	 * 						驳回且保留占位例子：财务驳回订金/尾款/全款付款￥12500元，需重新发起付款；
	 * 						驳回且不保留占位例子：财务驳回订金/尾款/全款付款￥12500元，并取消占位，需重新发起预定；
	 * @param orderUuid 订单UUID
	 * @param isCanceledOrder 订单是否因驳回取消
	 * @return
	 */
	public String getOrderPrompt(String orderUuid, boolean isCanceledOrder);
	
	/**
	 * 订单锁定
	 * @param uuid 订单uuid
	 */
	public void lockOrder(String uuid);
	
	/**
	 * 订单解锁
	 * @param uuid 订单uuid
	 */
	public void unLockOrder(String uuid);
	
	/**
	 * 订单关联文件
	 * @param uuid 订单uuid
	 * @param uuid 文件ids
	 */
	public void setOrderFiles(String orderUuid, String fileIds);
	
	/**
	 * 获取订单关联文件信息
	 * @param orderUuid 订单uuid
	 */
	public List<DocInfo> getFilesInfo(String orderUuid);
	
   /**
    * 根据UUIDS查询订单列表
    * @param orderUuids
    * @return
    */
	public List<HotelOrder> getByUuids(List<String> orderUuids);
	
	/**
     * 激活订单
     * @param uuid 订单uuid
     * @param request
     */
	public String invokeOrder(String uuid, HttpServletRequest request);
	
    /**
     * 取消订单
     * @param hotelOrder 酒店订单
     * @param description 取消理由
     * @param request
     */
   public void cancelOrder(HotelOrder hotelOrder, String description, HttpServletRequest request);
   
   /**
    * 修改凭证上传图片文件
    * @param docInfoList 文件列表
    * @param payHotelOrder 支付实体
    * @param orderUuid 订单UUID
    * @param mode
    * @param request
    * @return
    * @throws OptimisticLockHandleException
    * @throws PositionOutOfBoundException
    * @throws Exception
    */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> updatepayVoucherFile(ArrayList<DocInfo> docInfoList, PayHotelOrder payHotelOrder, String orderUuid, ModelMap mode, 
			HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException,Exception;
	
	/**
	 * 获取退团审核信息列表
	 * @param flowType 流程类型
	 * @param orderId 订单id
	 * @return
	 */
	public List<Map<String, String>> getExitGroupReviewInfo(Integer flowType, String orderId);
	
	/**
	 * 获取退团审核详情
	 * @param rid 审核id
	 * @return
	 */
	public Map<String, Object> getExitGroupReviewInfoById(long rid);
	
	/**
	 * 根据订单UUID查询游客列表
	 * @param orderUuid
	 * @return
	 */
	public List<Map<Object, Object>> getTravelerByOrderId(String orderUuid);
	
	/**
	 * 保存退团申请
	 * @param productType
	 * @param flowType
	 * @param travelerId
	 * @param travelerName
	 * @param exitReason
	 * @param orderUuid
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> saveExitGroupReviewInfo(Integer productType, Integer flowType, 
			Long[] travelerId,String[] travelerName, String[] exitReason, String orderUuid) throws Exception;
	
	/**
	 * 修改酒店订单信息
	 * @param jsonData
	 * @return
	 */
	public int updateHotelOrder(String jsonData)throws Exception;
	
	/**
	 * 获取订单基本信息
	 * @param orderUuid
	 * @param model
	 */
	public void getOrderBaseInfo(String orderUuid, Model model);
	
	/**
	 * 从数据库根据表名和条件获取指定的值
	 * @param table 表名
	 * @param queryName 查询字段名
	 * @param queryValue 查询字段名值
	 * @param getName 查询值
	 * @return
	 */
	public String getData(String tableName, String queryName, String queryValue, String getName);
}
