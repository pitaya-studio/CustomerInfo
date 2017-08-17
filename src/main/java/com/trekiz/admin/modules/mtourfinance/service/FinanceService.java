package com.trekiz.admin.modules.mtourfinance.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.modules.mtourfinance.json.*;
import com.trekiz.admin.modules.mtourfinance.pojo.*;
import org.apache.poi.ss.usermodel.Workbook;

import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.mtourOrder.jsonbean.MtourOrderJsonBean;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.pay.entity.PayGroup;

import freemarker.template.TemplateException;

public interface FinanceService {
	
	/**
	 * 获取支付信息
	     * <p>@Description TODO</p>
		 * @Title: getPayInfo
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-10-16 下午2:42:41
	 */
	public OrderpayJsonBean getPayInfo(BaseInput4MT input) throws Exception;
	
	/**
	 * 获取付款详情信息
	     * <p>@Description TODO</p>
		 * @Title: getRefundInfo
	     * @return RefundJsonBean
	     * @author majiancheng       
	     * @date 2015-10-21 下午12:04:50
	 */
	public RefundJsonBean getRefundInfo(BaseInput4MT input) throws Exception ;
	
	/**
	 * 撤销付款记录
		 * @Title: undoRefundPayInfo
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-10-21 下午5:45:12
	 */
	public boolean undoRefundPayInfo(BaseInput4MT input) throws Exception ;
	
	
	
	/**
	 * 获取订单列表款项明细
	 * @author songyang
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public RefundRecordsJsonBean getOrderPaymentInfo (BaseInput4MT input) throws Exception;
	
	
	
	/**
	 * 获取确认单数据
	 * @author 赵海明
	 * @date 2015-10-19
	 * @param id 批发商ID
	 * @param orderId 机票订单Id
	 * @return ConfirmSheet
	 * */
	public ConfirmSheet getConfirmSheetData(BaseInput4MT input,HttpServletRequest request);
	
	/**
	 * 获取收入单信息
	     * <p>@Description TODO</p>
		 * @Title: getIncomeInfoByInput
	     * @return Map<String,String>
	     * @author majiancheng       
	     * @date 2015-10-20 上午10:06:32
	 */
	public Map<String, Object> getIncomeInfoByInput(String fundsType, String receiveUuid) throws Exception;
	
	/**
	 * 创建收入单下载文件
	     * <p>@Description TODO</p>
		 * @Title: createIncomeSheetDownloadFile
	     * @return File
	     * @author majiancheng       
	     * @date 2015-10-20 下午3:00:33
	 */
	public File createIncomeSheetDownloadFile(Map<String, Object> data) throws Exception ;
	
	public void genExcl(HttpServletRequest request,HttpServletResponse response,ConfirmSheet cs);

	/**
	 *获取付款历史记录
	 *@param baseInput4MT
	 *@retrun RefundRecord
	 *@author zhaohaiming
	  * */
	public List<RefundRecord> getRefundRecord(BaseInput4MT input);

	
	/**
	 * 收款记录列表
	 * @param 查询条件
	 * @return 
	 */
	public List<Map<String, Object>> getReceiptRecordList(BaseInput4MT input);
	
	public List<Map<String, Object>> getReceiptRecordDetail(String orderPayId);
	
	public List<Map<String, String>> getReceiptDocDetail(String docId);
	/**
	 * 成本录入列表
	 * @author zhankui.zong
	 * @param orderId
	 * @return
	 */
	public List<CostRecordJsonBean> getCostRecordList(Integer orderId);
	
	/**
	 * 其他收入录入列表
	 * @author zhankui.zong
	 * @param orderId
	 * @return
	 */
	public List<CostRecordJsonBean> getOtherCostRecordList(Integer orderId);
	
	/**
	 * 提交成本录入/其他收入录入
	 * @author zhankui.zong
	 * @param id
	 */
	public void submitCostRecord(Integer id);
	
	/**
	 * 撤回成本录入/其他收入录入
	 * @author zhankui.zong
	 * @param id
	 */
	public void cancelCostRecord(Integer id);
	
	/**
	 * 获取航段名称
	 * @param orderId 订单Id
	 * @param invoiceOriginalUuid 大编号uuid
	 * @return
	 */
	public List<Map<String, Object>> getAirlineNames(Integer orderId, String invoiceOriginalUuid); 
	
	/**
	 * 添加/修改成本录入
	 * @author zhankui.zong
	 * @param costRecord
	 */
	public void saveOrUpdateCostRecord(CostRecord costRecord);
	
	/**
	 * 添加/修改其他收入录入
	 * @author zhankui.zong
	 * @param costRecord
	 */
	public void saveOrUpdateOtherCostRecord(CostRecord costRecord, PayGroup payGroup);

	/**
	 * 大编号列表
	 * @author zhankui.zong
	 * @param orderId
	 * @return
	 */
	public List<BigCode> getBigCodeList(Integer orderId);
	
	/**
	 * 付款-款项明细-成本录入
	 * @author zhankui.zong
	 * @param costId
	 * @return
	 */
	public List<Map<String, Object>> getPaymentCost(Integer costId);
	/**
	 * 各种款项信息
	 * @param money_amount.serialNum
	 * @return 
	 */
	public List<Map<String, Object>> getMoneyInfo(String serialNum);
	
	/**
	 * 生成美途国际机票结算通知单Excel对象
	 * @param orderId
	 * @return
	 * @author shijun.liu
	 */
	public Workbook createSettlement(Long orderId);

	/**
	 * 打包下载结算单
	 * @param orderIds
	 * @return
	 * @author shijun.liu
	 */
	public File batchZipSettlement(String orderIds);

	/**
	 * 结算单锁定
	 * @param orderId
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.08
	 */
	public void lockSettlement(Long orderId);
	
	/**
	 * 结算单解锁
	 * @param orderId
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.13
	 */
	public void unlockSettlement(Long orderId);
	
	/**
	 * 生成美途国际机票结算通知单Json对象
	 * @param orderId
	 * @return
	 * @author shijun.liu
	 */
	public SettlementJsonBean getSettlementJson(Long orderId);
	
	/**
	 * 付款列表
	 * @param input
	 * @param page
	 * @return list
	 * @author zhaohaiming
	 * @param
	 * */

	public Page getPayList(BaseInput4MT input);

	/**
	 * 收款列表
	 * @param input
	 * @return page
	 * @author zhaohaiming
	 * 
	 * */
	public com.trekiz.admin.modules.mtourfinance.json.Page showOrderList(BaseInput4MT input ,Page page);
	/**
	 * 成本记录详情
	 * @author hhx
	 * @date 2015-10-21
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> getCostDetail(BaseInput4MT input);
	
	/**
	 * 其他收入记录详情
	 * @author hhx
	 * @date 2015-10-22
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> getOtherPayDetail(BaseInput4MT input);


	/**
	 * 收款駁回操作
	 * @param input
	 * @return boolean
	 * @author zhaohaiming
	 * */
	public boolean confirmRejectOper(BaseInput4MT input);
	
	/**
	 * 收款确认接口
	 * @param input
	 * @author zhaohaiming
	 * **/
	public boolean payedConfirm(BaseInput4MT input);

	

	/**
	 * 美途支出申请单 下载
	 * @author wangxinwei
	 * @date 2015-10-22
	 * @param paymentUuid
	 * @param paymentType
	 * @return
	 */
	public File createMtourPaySheetDownLoadFile(String paymentUuid,String fundsType)  throws IOException, TemplateException;

    
	/**
	 * 美途支出申请单合开 下载
	 * @author songyang
	 * @date  2016年2月2日14:40:09
	 * @return
	 */
	public File createMtourMergePaySheetDownLoadFile(String inputParam,String fileName)  throws IOException, TemplateException;

	
	
	
	/**
	 * 订单收款详情
	 * @param input
	 * @return
	 * @author zhaohaiming
	 * */
	public PayedDetail getPayedDetail(BaseInput4MT input);
	
	
	/**
	 * 查询应付金额
	 * @param orderId, fundsType
	 * @author gaoang
	 * @return
	 */
	public List<Map<String,Object>> queryPayableAmount(String orderId,String fundsType);
	
	/**
	 * 查询已付金额
	 * 
	 */
	public List<Map<String,Object>>  queryPayedAmount(String paymentUuid);

    /**
     * 收款-提交-查询收款金额接口
     * @param input
     * @author zhaohaiming
     * */
	public PayedMoneyPojo getPayedMoneyInfo(BaseInput4MT input);
    
	/**
	 * 获取付款的应付金额和未付金额
	 * @param input
	 * @author zhaohaiming
	 * */
	public PayedMoneyPojo getPayedMoneyInfoForPay(BaseInput4MT input);
	
	/**
	 * 订单明细
	 * @author gao
	 * 2015年10月23日
	 * @param channelId'渠道商id',
	 * @return
	 */
	public List<OrderDetailJsonBean> getOrderDetail(String channelId);
	
	/**
	 * 付款记录保存接口
		 * @Title: saveRefundInfo
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-10-24 上午10:33:27
	 */
	public boolean saveRefundInfo(BaseInput4MT input);
	
	/**
	 * 根据订单金额表uuid和款项类型获取相应的款项明细；其中（moneyType为款项类型（借款：1，退款：2，追加成本：3））
		 * @Title: getRefundTypeDetailInfo
	     * @return RefundTypeDetailJsonBean
	     * @author majiancheng       
	     * @date 2015-10-24 下午4:00:42
	 */
	public RefundTypeDetailJsonBean getRefundTypeDetailInfo(String moneyAmountUuid, Integer moneyType);
	
	/**
	 * 账龄查询
	 * @param accountAgeParam	账龄查询需要的参数
	 * @return
	 * @author shijun.liu
	 */
	public Page getAccountAgeList(AccountAgeParam accountAgeParam);
	
	/**
	 * 根据款项类型美途机票快速订单金额表ID计算出付款状态
		 * @Title: buildPaystatusByMoneyAmountId
	     * @return int
	     * @author majiancheng       
	     * @date 2015-11-2 下午9:05:09
	 */
	public int buildPaystatusByMoneyAmountId(Integer moneyType, Integer moneyAmountId);
	
	/**
	 * 根据其他收入id计算出付款状态
		 * @Title: buildPaystatusByCostRecordId
	     * @return int
	     * @author majiancheng
	     * @date 2015-11-4 下午4:26:07
	 */
	public int buildPaystatusByCostRecordId(Integer moneyType, Integer costRecordId);
	
	/**
	 * 订单收款撤销接口（财务模块）
		 * @Title: cancelReceive
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-11-4 下午5:27:18
	 */
	public boolean cancelReceive(BaseInput4MT input);
	
	public Map<String, Object> getOtherReceiptDetail(String costRecordId);
	
	public List<Map<String,String>> getOrderPayDocList(String docIds);
	
	/**
	 * 根据 表明和记录id从serial_number查询流水号 
	 * @param tableName 表名
	 * @param recordid 表中记录id
	 * @author xinwei.wang@quauq.com
	 * @date 2015年11月11日13:48:55
	 * @return null or "":如查询不到流水号返回 ""
	 */
	public String getSerialNumByTableNameAndRecordId(String tableName,Integer recordid);
	

	/**
	 * 付款--订单列表---展开子列表：支付对象查询接口
	 * @author chao.zhang
	 * 
	 */
	public List<Map<String,Object>> getPayChildrenList(String orderUuid);

	/**
	 * 批量保存付款信息接口
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	public boolean batchSaveRefundInfo(BaseInput4MT input);
	
	/**
	 * 根据订单uuid和付款对象id获取
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return RefundRecordsJsonBean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	public RefundRecordsJsonBean getBatchRefundInfo(BaseInput4MT input);
	
	/**
	 * 批量撤销付款记录
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-22
	 */
	public boolean batchUndoRefundPayInfo(BaseInput4MT input);
	
	/**
	 * 根据付款信息撤销付款记录
	 * @Description: 
	 * @param @param refund
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-22
	 */
	public boolean undoRefundPayInfo(Refund refund);
	
	/**
	 * 获取订单列表（付款页面）
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return OrderRecordJsonBean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	public MtourOrderJsonBean getRefundOrderPage(BaseInput4MT input);
	
	/**
	 * 更新机票订单的付款状态
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	public boolean updateOrderRefundFlag(Long orderId);
	
	/**
	 * 初始化所有美途订单的付款状态
	 * @Description: 
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	public boolean initOrderRefundFlag();
	
	/**
	 * 订单列表-二级列表-付款记录-详情
	 * @param input
	 * @return List<Map<String,Object>>
	 * @author zhangchao
	 */
	public PayDetailJsonBean queryPayJDetail(BaseInput4MT input);
	
	/**
	 * 根据订单uuid和付款对象uuid获取付款记录
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return List<SecondRecordsJsonBean>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-29
	 */
	public List<SecondRefundRecordsJsonBean> getRefundRecordsInfo(BaseInput4MT input);
	
	
	/**
	 * 根据收款订单uuid回传收款订单的二级子列表
	 * @param input
	 * @return List<SecondReceiveListJsonBean>
	 * @throws
	 * @author wangyang
	 * @date 2016-3-9
	 * */
	public List<SecondReceiveListJsonBean> getReceiveOrderSubList(String orderUuid);

	/**
	 * 财务中心-收款-订单列表
	 * @param param
	 * @return
	 * @author shijun.liu
	 * @date 2016.03.09
	 */
	public Page receiveOrderList(ReceiveOrderListParam param);

	/**
	 * 获取待确认订单的订金统计数据
	 * @param 
	 * @return List<FrontMoneyStatJsonBean>
	 * @throws
	 * @author wangyang
	 * @date 2016.6.20
	 * */
	public List<FrontMoneyStatJsonBean> getFrontMoneyStat(String statDateBegin, String statDateEnd);

	/**
	 * 获取指定年月下的，该公司的所有营业统计信息。包括订单团号，销售，订单总额，订单到账金额等。
	 * 然后根据销售人员的id进行分组。	yudong.xu 2016.6.17
	 */
	public Map<Integer,List<OperatingRevenueData>> getOperatingRevenueByDate(String startDate,String endDate,Boolean isStr);

	/**
	 * 批量指出单查询组装data
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> getBatchIncomeInfoByInput(String orderId,String csIds,String oterIds);

	/**
	 * 获取支出单文件列表。用于批量下载支出单。 yudong.xu 2016.7.7
	 */
	public List<File> getPaySheetFileList(String params) throws Exception;
}
